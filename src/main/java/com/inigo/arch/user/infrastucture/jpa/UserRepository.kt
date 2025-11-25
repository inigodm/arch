package com.inigo.arch.user.infrastucture.jpa

import com.inigo.arch.user.domain.AuthenticationData
import com.inigo.arch.user.domain.User
import com.inigo.arch.user.domain.UserStore
import com.inigo.arch.user.domain.Password
import com.inigo.arch.user.domain.Username
import com.inigo.arch.shared.domain.errors.NotFoundError
import com.inigo.arch.user.infrastucture.spring.UnauthorizedError
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UserRepository(val repo : UserJpaRepository,
                     val bCryptEncoder: BCryptPasswordEncoder = BCryptPasswordEncoder()
): UserStore {
    override fun checkByUsernameAndPassword(username: Username, password: Password): AuthenticationData {
        val optUser = repo.findByUsername(username.value)
        if (optUser.isEmpty) {
            throw UnauthorizedError.becauseUserOrPasswordNotFonud(username.value)
        }
        val user = optUser.get()
        if (!bCryptEncoder.matches(password.value, user.password)) {
            throw UnauthorizedError.becauseUserOrPasswordNotFonud(username.value)
        }
        return AuthenticationData(
                user.id,
                user.username,
                user.email,
                user.role
            )
    }

    override fun save(user: User) {
        repo.save(
            UserJpa(user.id,
                user.username.value,
                user.email.value,
                bCryptEncoder.encode(user.password.value),
                user.role.name)
            )
        }

    override fun delete(user: User) {
        repo.delete(
            UserJpa(user.id,
                user.username.value,
                user.email.value,
                user.password.value,
                user.role.name)
        )
    }

    override fun existsUserId(id: UUID) = repo.existsById(id)
    override fun findById(userId: String): User {
        return repo.findById(UUID.fromString(userId))
            .map { it.toDomain() }
            .orElseThrow { NotFoundError.becauseUserIdNotFound(userId) }
    }

    override fun existsUsername(user: User) = repo.findByUsername(user.username.value).isPresent

    override fun existsEmail(user: User) = repo.findByEmail(user.email.value).isPresent
    override fun updateUserType(userId: UUID, type: String) {
        val updatedRows = repo.updateUserTypeById(userId, type)
        require (updatedRows != 0) { "User with ID $userId not found or type update failed." }
    }
}