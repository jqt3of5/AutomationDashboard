package com.example

import com.google.gson.Gson
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
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentDisposition.Companion.File
import io.ktor.http.content.default
import io.ktor.http.content.files
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.serialization.json
import io.ktor.serialization.serialization
import org.json.simple.JSONObject


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.module()
{
    install(Authentication) {
        basic("admin") {
            realm = "Server Administration"
            validate {
                if (it.name == "test" && it.password == "password") UserIdPrincipal(it.name) else null }
        }

    }
    install(CORS){
        anyHost()
        method(HttpMethod.Get)
    }
    install(ContentNegotiation)
    {
        json()
    }
}

//
//fun FlowOrMetaDataContent.styleCss(builder: CSSBuilder.() -> Unit) {
//    style(type = ContentType.Text.CSS.toString()) {
//        +CSSBuilder().apply(builder).toString()
//    }
//}
//
//fun CommonAttributeGroupFacade.style(builder: CSSBuilder.() -> Unit) {
//    this.style = CSSBuilder().apply(builder).toString().trim()
//}
//
//suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
//    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
//}
