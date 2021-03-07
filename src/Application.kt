package com.alexandar12

import com.alexandar12.jwt.JwtConfig
import com.alexandar12.models.User
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.jackson.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header("MyCustomHeader")
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    install(Authentication) {
        jwt {
            verifier(JwtConfig.verifier)
            realm = "com.alexandar12"
            validate {
                val username = it.payload.getClaim("username").asString()
                val password = it.payload.getClaim("password").asString()
                if (username != null && password != null){
                    User(username, password)
                } else {
                    null
                }
            }
        }
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }
        post("/generate_token"){
            val user = call.receive<User>()
            val token = JwtConfig.generateToken(user)
            call.respond(token)
        }
        authenticate {
            get("/authenticate"){
                call.respond("get authenticated value from token" + "name: ${call.user?.username}")
            }
        }
    }
}

