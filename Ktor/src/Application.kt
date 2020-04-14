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
import io.ktor.features.StatusPages
import io.ktor.response.respond
import io.ktor.serialization.json
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.modules.SerializersModule

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

val gridModule = SerializersModule {
    polymorphic(Service::class) {
        Service.Hub::class with Service.Hub.serializer()
        Service.Node::class with Service.Node.serializer()
        Service.ScreenRecorder::class with Service.ScreenRecorder.serializer()
        Service.WinAppDriver::class with Service.WinAppDriver.serializer()
    }
    polymorphic(ServiceState::class) {
        ServiceState.Offline::class with ServiceState.Offline.serializer()
        ServiceState.Unknown::class with ServiceState.Unknown.serializer()
        ServiceState.Online::class with ServiceState.Online.serializer(PolymorphicSerializer(Status::class))
    }
    polymorphic(StepResult::class) {
        StepResult.FailedAction::class with StepResult.FailedAction.serializer()
        StepResult.FailedToObtainElement::class with StepResult.FailedToObtainElement.serializer()
        StepResult.FailedToActivateWindow::class with StepResult.FailedToActivateWindow.serializer()
        StepResult.SuccessActionResult::class with StepResult.SuccessActionResult.serializer()
    }
}
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
    install(StatusPages)
    {
        exception<Throwable> {cause ->
            call.respond(HttpStatusCode.BadRequest, Error("Exception in API. Cause: $cause"))
        }
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
