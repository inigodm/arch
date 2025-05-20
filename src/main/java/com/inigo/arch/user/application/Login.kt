package com.inigo.arch.user.application

import com.inigo.arch.user.domain.UserStore
import com.inigo.arch.user.domain.Password
import com.inigo.arch.user.domain.Token
import com.inigo.arch.user.domain.TokenGenerator
import com.inigo.arch.user.domain.Username
import org.springframework.stereotype.Component

@Component
class Login(val tokenGenerator: TokenGenerator, val store: UserStore) {
    fun execute(username: Username, password: Password): Token {
        store.checkByUsernameAndPassword(username, password)
        return tokenGenerator.generateTokenFor(username, password)
    }
}