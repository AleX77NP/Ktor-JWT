package com.alexandar12

import com.alexandar12.models.User
import io.ktor.application.*
import io.ktor.auth.*

val ApplicationCall.user get() = authentication.principal<User>()