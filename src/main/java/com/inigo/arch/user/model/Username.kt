package com.inigo.arch.user.model

@JvmInline
value class Username(val value: String) {
    init {
        require(value.isNotBlank()) { "Name cannot be void" }
    }
}