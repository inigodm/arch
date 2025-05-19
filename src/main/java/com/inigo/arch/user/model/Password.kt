package com.inigo.arch.user.model

@JvmInline
value class Password(val value: String) {
    init {
        require(value.isNotBlank()) { "Password cannot be void" }
    }
}