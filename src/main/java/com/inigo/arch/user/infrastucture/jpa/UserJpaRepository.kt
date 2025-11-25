package com.inigo.arch.user.infrastucture.jpa

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface UserJpaRepository : JpaRepository<UserJpa, UUID> {
    fun findByUsername(username: String): Optional<UserJpa>

    fun findByEmail(email: String): Optional<UserJpa>
    @Modifying
    @Transactional
    @Query("UPDATE UserJpa u SET u.role = :role WHERE u.id = :id")
    fun updateUserTypeById(id: UUID, role: String): Int
}