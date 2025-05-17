package com.inigo.arch.user.model

import java.util.*

interface TokenService {
    fun generateToken(username: String, email: String, id: UUID, userRole: Int): String

    fun parseToken(token: String): LoggedInUser
}
