package com.inigo.arch.user.infrastucture.spring

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AuthenticationManagerImpl: AuthenticationManager {
    override fun authenticate(authentication: Authentication): Authentication {
        //TODO: Implement authentication logic here
        // customize thi class using the preferred authentication method or launching an NotAllowedException
        return UserAuthentication()
    }
}

class UserAuthentication: Authentication {
    fun getId() : UUID {
        return UUID.randomUUID()
    }

    override fun getName(): String {
        return "UserAuthentication"
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return emptyList()
    }

    override fun getCredentials(): Any {
        return "credentials"
    }

    override fun getDetails(): Any {
        return "details"
    }

    override fun getPrincipal(): Any {
        return "principal"
    }

    override fun isAuthenticated(): Boolean {
        return true
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
    }

    fun getEmail() = "email"
    fun getUserRole() = 1
}