package com.alexandar12.jwt

import com.alexandar12.models.User
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

object JwtConfig {
    private const val secret = "mysecret123"
    private const val issuer = "com.alexandar12"
    private const val validityInMs = 36_000_00 * 24;
    private val algorithm = Algorithm.HMAC512(secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    fun generateToken(user: User): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("username", user.username)
        .withClaim("password", user.password)
        .withExpiresAt(getExp())
        .sign(algorithm)

    private fun getExp() = Date(System.currentTimeMillis() + validityInMs)
}