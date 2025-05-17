package com.inigo.arch.user.model

import java.util.*

@JvmRecord
data class LoggedInUser(val name: String, val email: String, val id: UUID, val userRole: Int)
