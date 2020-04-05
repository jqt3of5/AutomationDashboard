package com.example

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.auth.*
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.serialization.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

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
        json(Json(context = gridModule))
    }


    var testRepo = TestRepo()
    runsModule(testRepo)
    testsModule(testRepo)

    val gridRepo = GridRepo(httpClient)
    gridModule(gridRepo)
}

val httpClient = HttpClient(Apache){
   install(JsonFeature)
   {
        serializer = KotlinxSerializer(Json(JsonConfiguration(ignoreUnknownKeys = true)))
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
