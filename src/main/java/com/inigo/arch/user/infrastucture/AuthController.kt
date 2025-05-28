package com.inigo.arch.user.infrastucture

import com.inigo.arch.user.application.Login
import com.inigo.arch.user.domain.Password
import com.inigo.arch.user.domain.Username
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
@Validated
class AuthController(val login: Login) {
    @PostMapping("/login")
    fun login(@Valid @RequestBody request: AuthRequest): ResponseEntity<*> {
        val token = login.execute(Username(request.username!!), Password(request.password!!))
        return ResponseEntity.ok(AuthResponse(token.value))
    }
}

class AuthRequest {
    @field:NotNull(message = "username must not be null") val username: String? = null
    @field:NotNull(message = "password must not be null") val password: String? = null
}

data class AuthResponse (val token: String)