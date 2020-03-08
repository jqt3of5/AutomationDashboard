package com.example

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.html.*
import kotlinx.html.*
import kotlinx.css.*
import io.ktor.auth.*
import io.ktor.http.ContentDisposition.Companion.File
import io.ktor.http.content.default
import io.ktor.http.content.resources
import io.ktor.http.content.static

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(Authentication) {
        basic("myBasicAuth") {
            realm = "Ktor Server"
            validate {
                if (it.name == "test" && it.password == "password") UserIdPrincipal(it.name) else null }
        }
    }
    routing {

        static("/")
        {
            resources(".")
            default("resources/index.html")
        }
        route("/api"){
            route("/testruns"){
                get{
                    call.respondText { "TestRuns" }
                }
                post {
                    call.respondText { "TestrunAdded" }
                }
            }
            route("/tests/{namespace}/{fixture}/{testname}")
            {
                get {
                    call.respondText { "{}" }
                }
                post {
                    call.respondText{"test added"}
                }
                route("/{testId}"){
                    get {
                        call.respondText { call.parameters["testId"] ?: "" }
                    }
                }
            }
            route("/fixtures")
            {
                get{
                    call.respondText { "tests" }
                }
                route("/{fixtureId"){
                    get {
                        call.respondText { "tests" }
                    }
                }
            }
        }

       authenticate("myBasicAuth") {
            get("/protected/route/basic") {
                val principal = call.principal<UserIdPrincipal>()!!
                call.respondText("Hello ${principal.name}")
            }
        }
    }
}

fun FlowOrMetaDataContent.styleCss(builder: CSSBuilder.() -> Unit) {
    style(type = ContentType.Text.CSS.toString()) {
        +CSSBuilder().apply(builder).toString()
    }
}

fun CommonAttributeGroupFacade.style(builder: CSSBuilder.() -> Unit) {
    this.style = CSSBuilder().apply(builder).toString().trim()
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}
