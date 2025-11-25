package com.inigo.arch.user.domain

import java.util.UUID

data class AuthenticationData(
    val userId: UUID,
    val username: String,
    val email: String,
    val role: String
)