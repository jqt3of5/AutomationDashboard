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
import io.ktor.features.CORS
import io.ktor.http.ContentDisposition.Companion.File
import io.ktor.http.content.default
import io.ktor.http.content.files
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
    install(CORS){
        anyHost()
        method(HttpMethod.Get)
    }
    routing {
        route("/api"){
            route("/testruns"){
                get{
                    call.respondText { "[{\"name\":\"adsf\", \"muted\":true, \"fixture\":{\"platform\":\"adf\", \"name\":\"adsf\"}}]" }
                }
                post {
                    call.respondText { "{testrunid:3423lkjlkj234}" }
                }
                route("/{testrunid}")
                {
                    get {
                        call.respondText {"{}"}
                    }
                }
            }
            route("/tests")
            {
                get {
                    call.respondText { "[{\"name\":\"adsf\", \"muted\":true, \"fixture\":{\"platform\":\"adf\", \"name\":\"adsf\"}}]" }
                }
                post {
                    call.respondText{"{testid:sadfasdfasdf}"}
                }
                route("/{testId}"){
                    get {
                        call.respondText { call.parameters["testId"] ?: "" }
                    }
                    put {
                        //update test
                    }
                }
            }
            route("/fixtures")
            {
                get{
                    call.respondText { "tests" }
                }
                route("/{fixtureId}"){
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
