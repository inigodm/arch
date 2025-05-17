package com.inigo.arch.user.infrastucture

import lombok.AllArgsConstructor
import lombok.Data
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(private val authManager: AuthenticationManager, private val jwtService: BearerService) {
    @PostMapping("/login")
    fun login(@RequestBody request: AuthRequest): ResponseEntity<*> {
        val authentication = authManager.authenticate(
            UsernamePasswordAuthenticationToken(request.username, request.password)
        ) as UserAuthentication

        val token = jwtService.generateToken(
            authentication.name,
            authentication.getEmail(),
            authentication.getId(),
            authentication.getUserRole()
        )
        return ResponseEntity.ok(AuthResponse(token))
    }
}

@Data
class AuthRequest {
    val username: String? = null
    val password: String? = null
}

@Data
@AllArgsConstructor
internal data class AuthResponse (val token: String? = null)