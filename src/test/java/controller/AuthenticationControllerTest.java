package controller;

import com.alert.alert.controller.security.AuthenticationController;
import com.alert.alert.entity.enums.Role;
import com.alert.alert.payload.request.AuthenticationRequest;
import com.alert.alert.payload.request.RefreshTokenRequest;
import com.alert.alert.payload.request.RegisterRequest;
import com.alert.alert.payload.response.AuthenticationResponse;
import com.alert.alert.payload.response.RefreshTokenResponse;
import com.alert.alert.service.security.AuthenticationService;
import com.alert.alert.service.security.JwtService;
import com.alert.alert.service.security.RefreshTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        RegisterRequest registerRequest = new RegisterRequest("John", "Doe", "john.doe@example.com", "password", Role.USER);
        AuthenticationResponse authResponse = new AuthenticationResponse(1L, "john.doe@example.com", List.of("ROLE_USER"), "access-token", "refresh-token", "Bearer");

        when(authenticationService.register(any(RegisterRequest.class))).thenReturn(authResponse);
        when(jwtService.generateJwtCookie(any(String.class))).thenReturn(ResponseCookie.from("jwt", "jwt-cookie").build());
        when(refreshTokenService.generateRefreshTokenCookie(any(String.class))).thenReturn(ResponseCookie.from("refresh", "refresh-cookie").build());

        ResponseEntity<AuthenticationResponse> response = authenticationController.register(registerRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(authResponse, response.getBody());
        assertEquals("jwt=jwt-cookie", response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0));
        assertEquals("refresh=refresh-cookie", response.getHeaders().get(HttpHeaders.SET_COOKIE).get(1));
    }

    @Test
    void testAuthenticate() {
        AuthenticationRequest authRequest = new AuthenticationRequest("john.doe@example.com", "password");
        AuthenticationResponse authResponse = new AuthenticationResponse(1L, "john.doe@example.com", List.of("ROLE_USER"), "access-token", "refresh-token", "Bearer");

        when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenReturn(authResponse);
        when(jwtService.generateJwtCookie(any(String.class))).thenReturn(ResponseCookie.from("jwt", "jwt-cookie").build());
        when(refreshTokenService.generateRefreshTokenCookie(any(String.class))).thenReturn(ResponseCookie.from("refresh", "refresh-cookie").build());

        ResponseEntity<AuthenticationResponse> response = authenticationController.authenticate(authRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(authResponse, response.getBody());
        assertEquals("jwt=jwt-cookie", response.getHeaders().get(HttpHeaders.SET_COOKIE).get(0));
        assertEquals("refresh=refresh-cookie", response.getHeaders().get(HttpHeaders.SET_COOKIE).get(1));
    }

    @Test
    void testRefreshToken() {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest("refresh-token");
        RefreshTokenResponse refreshTokenResponse = new RefreshTokenResponse("new-access-token", "refreshToken", "Bearer ");

        when(refreshTokenService.generateNewToken(any(RefreshTokenRequest.class))).thenReturn(refreshTokenResponse);

        ResponseEntity<RefreshTokenResponse> response = authenticationController.refreshToken(refreshTokenRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(refreshTokenResponse, response.getBody());
    }
}
