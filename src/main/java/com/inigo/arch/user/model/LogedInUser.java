package com.inigo.arch.user.model;

import java.util.UUID;

public record LogedInUser(String name, String email, UUID id, Integer userRole) {
}
