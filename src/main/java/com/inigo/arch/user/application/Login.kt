package com.inigo.arch.user.application

import com.inigo.arch.user.model.TokenGenerator
import com.inigo.arch.user.model.Password
import com.inigo.arch.user.model.Token
import com.inigo.arch.user.model.Username
import org.springframework.stereotype.Component

@Component
class Login(val tokenGenerator: TokenGenerator) {
    fun execute(username: Username, password: Password): Token {
        return tokenGenerator.generateTokenFor(username, password)
    }
}