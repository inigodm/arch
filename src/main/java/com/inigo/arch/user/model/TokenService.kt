package com.inigo.arch.user.model;

import java.util.UUID;

public interface TokenService {
    String generateToken(String username, String email, UUID id, int userRole);

    LogedInUser parseToken(String token);
}
