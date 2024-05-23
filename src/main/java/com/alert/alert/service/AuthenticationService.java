package com.alert.alert.service;

import com.alert.alert.payload.request.AuthenticationRequest;
import com.alert.alert.payload.request.RegisterRequest;
import com.alert.alert.payload.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    boolean confirmEmail(String confirmationToken);
}
