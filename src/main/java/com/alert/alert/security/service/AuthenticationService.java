package com.alert.alert.security.service;

import com.alert.alert.security.payload.request.AuthenticationRequest;
import com.alert.alert.security.payload.request.RegisterRequest;
import com.alert.alert.security.payload.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
