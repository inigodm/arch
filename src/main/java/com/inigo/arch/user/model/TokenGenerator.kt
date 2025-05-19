package com.inigo.arch.user.model

interface TokenGenerator {
    fun generateTokenFor(username: Username, password: Password): Token
}