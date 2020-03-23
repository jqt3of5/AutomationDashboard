package com.example

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.serialization.json

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.testsModule(testing: Boolean = false) {

    routing {
        route("/api") {
            route("/fixture")
            {
            }
            route("/test") {

            }
        }
    }
}