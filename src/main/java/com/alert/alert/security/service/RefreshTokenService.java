package com.alert.alert.security.service;

import com.alert.alert.security.model.RefreshToken;
import com.alert.alert.security.payload.request.RefreshTokenRequest;
import com.alert.alert.security.payload.response.RefreshTokenResponse;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(Long userId);
    RefreshToken verifyExpiration(RefreshToken token);
    RefreshTokenResponse generateNewToken(RefreshTokenRequest request);
}
