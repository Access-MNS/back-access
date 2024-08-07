package service;

import com.alert.alert.entity.ConfirmationToken;
import com.alert.alert.entity.RefreshToken;
import com.alert.alert.entity.User;
import com.alert.alert.entity.enums.Role;
import com.alert.alert.entity.enums.TokenType;
import com.alert.alert.payload.request.AuthenticationRequest;
import com.alert.alert.payload.request.RegisterRequest;
import com.alert.alert.payload.response.AuthenticationResponse;
import com.alert.alert.repository.ConfirmationTokenRepository;
import com.alert.alert.repository.UserRepository;
import com.alert.alert.service.impl.security.AuthenticationServiceImpl;
import com.alert.alert.service.security.EmailService;
import com.alert.alert.service.security.JwtService;
import com.alert.alert.service.security.RefreshTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationServiceImpl(
                passwordEncoder, jwtService, userRepository, authenticationManager,
                refreshTokenService, confirmationTokenRepository, emailService);
    }

    @Test
    void testRegisterSuccess() {
        RegisterRequest request = new RegisterRequest("John", "Doe", "john.doe@example.com", "password", Role.USER);
        User user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .mail("john.doe@example.com")
                .password("encoded_password")
                .role(Role.USER)
                .build();

        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt_token");
        when(refreshTokenService.createRefreshToken(anyLong())).thenReturn(new RefreshToken(1L, user, "refresh_token", Instant.now().plusSeconds(3600), false));
        when(confirmationTokenRepository.save(any(ConfirmationToken.class))).thenReturn(new ConfirmationToken(user));
        doNothing().when(emailService).sendEmail(any(SimpleMailMessage.class));

        AuthenticationResponse response = authenticationService.register(request);

        assertNotNull(response);
        assertEquals("jwt_token", response.getAccessToken());
        assertEquals("john.doe@example.com", response.getEmail());
        assertEquals("refresh_token", response.getRefreshToken());
        assertEquals(TokenType.BEARER.name(), response.getTokenType());
        verify(userRepository, times(1)).save(any(User.class));
        verify(jwtService, times(1)).generateToken(any(User.class));
        verify(refreshTokenService, times(1)).createRefreshToken(anyLong());
        verify(confirmationTokenRepository, times(1)).save(any(ConfirmationToken.class));
        verify(emailService, times(1)).sendEmail(any(SimpleMailMessage.class));
    }

    @Test
    void testAuthenticateSuccess() {
        AuthenticationRequest request = new AuthenticationRequest("john.doe@example.com", "password");
        User user = User.builder()
                .id(1L)
                .mail("john.doe@example.com")
                .role(Role.USER)
                .build();

        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userRepository.findByMail(anyString())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt_token");
        when(refreshTokenService.createRefreshToken(anyLong())).thenReturn(new RefreshToken(1L, user, "refresh_token", Instant.now().plusSeconds(3600), false));

        AuthenticationResponse response = authenticationService.authenticate(request);

        assertNotNull(response);
        assertEquals("jwt_token", response.getAccessToken());
        assertEquals("john.doe@example.com", response.getEmail());
        assertEquals("refresh_token", response.getRefreshToken());
        assertEquals(TokenType.BEARER.name(), response.getTokenType());
        verify(userRepository, times(1)).findByMail(anyString());
        verify(jwtService, times(1)).generateToken(any(User.class));
        verify(refreshTokenService, times(1)).createRefreshToken(anyLong());
    }

    @Test
    void testAuthenticateFailure() {
        AuthenticationRequest request = new AuthenticationRequest("john.doe@example.com", "wrong_password");

        when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException("Authentication failed"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            authenticationService.authenticate(request);
        });

        assertEquals("Authentication failed", exception.getMessage());
        verify(userRepository, never()).findByMail(anyString());
        verify(jwtService, never()).generateToken(any(User.class));
        verify(refreshTokenService, never()).createRefreshToken(anyLong());
    }

    @Test
    void testConfirmEmailSuccess() {
        String token = "confirmation_token";
        User user = User.builder()
                .id(1L)
                .mail("john.doe@example.com")
                .isEnabled(false)
                .build();
        ConfirmationToken confirmationToken = new ConfirmationToken(user);

        when(confirmationTokenRepository.findByConfirmationToken(token)).thenReturn(confirmationToken);
        when(userRepository.findByMail(user.getMail())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        boolean result = authenticationService.confirmEmail(token);

        assertTrue(result);
        assertTrue(user.isEnabled());
        verify(userRepository, times(1)).findByMail(user.getMail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testConfirmEmailFailure() {
        String token = "invalid_token";

        when(confirmationTokenRepository.findByConfirmationToken(token)).thenReturn(null);

        boolean result = authenticationService.confirmEmail(token);

        assertFalse(result);
        verify(userRepository, never()).findByMail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
}
