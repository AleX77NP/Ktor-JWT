package com.alexandar12.models

import java.security.Principal

data class User(
    val username: String,
    val password: String,
    val other:String = "default"
): Principal, io.ktor.auth.Principal {
    override fun getName(): String {
        return username
    }
}