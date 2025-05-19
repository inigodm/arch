package com.inigo.arch.user.model

@JvmInline
value class Token (val value: String) {
    init {
        require(value.isNotBlank()) { "Token cannot be void" }
    }
}