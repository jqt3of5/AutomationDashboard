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
        get("/") {
            call.respondHtml {
                 head {
                       link(rel = "icon", href="/public/favicon.ico")
                       link(rel = "manifest", href="/public/manifest.json")
                     meta(charset="utf-8")
                     meta (name="viewport", content = "width=device-width,initial-scale=1")
                     meta (name="theme-color", content="#000000")
                     link(rel="apple-touch-icon", href="/public/logo192.png")
                     title {
                         "Automation Dashboard"
                     }
                 }
                 body {
                     noScript {
                         "You need to enable JavaScript to run this app."
                     }
                     script (type="text/javascript", src="/index.js"){}

                     div {id = "root"}
                 }
            }
        }

        static("/public")
        {
            resources("React/public/")
        }
        static("/")
        {
            resources("React/src")
        }
        get("/styles.css") {
            call.respondCss {
                body {
                    backgroundColor = Color.red
                }
                p {
                    fontSize = 2.em
                }
                rule("p.myclass") {
                    color = Color.blue
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
