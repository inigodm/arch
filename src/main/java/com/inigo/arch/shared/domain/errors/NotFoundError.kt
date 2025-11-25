package com.inigo.arch.shared.domain.errors

class NotFoundError: RuntimeException {
    private constructor(message: String) : super(message)

    companion object {

        fun becauseUserIdNotFound(userId: String): NotFoundError =
            NotFoundError("User with ID $userId does not exist")
    }
}