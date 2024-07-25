package com.alert.alert.service.impl.security;

import com.alert.alert.entity.ConfirmationToken;
import com.alert.alert.entity.User;
import com.alert.alert.entity.enums.Role;
import com.alert.alert.entity.enums.TokenType;
import com.alert.alert.payload.request.AuthenticationRequest;
import com.alert.alert.payload.request.RegisterRequest;
import com.alert.alert.payload.response.AuthenticationResponse;
import com.alert.alert.repository.ConfirmationTokenRepository;
import com.alert.alert.repository.UserRepository;
import com.alert.alert.service.security.AuthenticationService;
import com.alert.alert.service.security.EmailService;
import com.alert.alert.service.security.JwtService;
import com.alert.alert.service.security.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service @Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailService emailService;

    @Value("${spring.mail.username}")
    private String emailAddress;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .mail(request.getMail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        user = userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());

        var roles = user.getRole().getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .toList();

        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(emailAddress);
        mailMessage.setTo(user.getMail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, please click here : "
                +"http://localhost:8080/api/v1/auth/confirm-account/"+confirmationToken.getConfirmationToken());
        emailService.sendEmail(mailMessage);

        return AuthenticationResponse.builder()
                .accessToken(jwt)
                .email(user.getMail())
                .id(user.getId())
                .refreshToken(refreshToken.getToken())
                .roles(roles)
                .tokenType(TokenType.BEARER.name())
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getMail(), request.getPassword()));

        var user = userRepository.findByMail(request.getMail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var roles = user.getRole().getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .toList();
        var jwt = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return AuthenticationResponse.builder()
                .accessToken(jwt)
                .roles(roles)
                .email(user.getMail())
                .id(user.getId())
                .refreshToken(refreshToken.getToken())
                .tokenType( TokenType.BEARER.name())
                .build();
    }

    @Override
    public boolean confirmEmail(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if(token != null) {
            Optional<User> user = userRepository.findByMail(token.getUser().getMail());
            if (user.isPresent()) {
                user.get().setEnabled(true);
                userRepository.save(user.get());
            }
            return true;
        }
        return false;
    }
}
