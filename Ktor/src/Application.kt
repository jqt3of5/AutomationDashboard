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
import io.ktor.serialization.serialization
import org.json.simple.JSONObject

val repo = TestRepo()

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
    install(ContentNegotiation)
    {
        serialization()
    }
    routing {
        route("/api"){
            route("/fixtureruns")
            {
                get {
                    call.respond(repo.FixtureRuns)
                }
            }
            route("/testrun"){
                route("/{testrunid}")
                {
                    get {
                        repo.TestRuns.find {
                            it.testRunId == call.parameters["testrunid"]
                        }?.let {
                            call.respond(it)
                        } ?: call.respondText(ContentType.Application.Json, HttpStatusCode.NotFound) {
                            "{\"errorMessage\":\"Could not find testrun with id\"}"
                        }
                    }
                    post {
                        var testrun = call.receive<TestRun>()
                        repo.TestRuns.add(testrun)
                        call.respond(HttpStatusCode.OK, "")
                    }
                    route("/steps")
                    {
                        get {
                            var steps = repo.TestSteps.filter { it.testRunId == call.parameters["testrunid"] }
                            call.respond(steps)
                        }
                    }
                }
            }
            route("/teststep")
            {
                route("/{teststepid}")
                {
                    get {
                        repo.TestSteps.find {
                            it.testStepId == call.parameters["teststepid"]
                        }?.let {
                            call.respond(it)
                        } ?: call.respondText(ContentType.Application.Json, HttpStatusCode.NotFound) {
                            "{\"errorMessage\":\"Could not find testrun with id\"}"
                        }
                    }
                    post {
                        var teststep = call.receive<TestStep>()
                        repo.TestSteps.add(teststep)
                        call.respond(HttpStatusCode.OK, "")
                    }
                }
            }
            route("/fixturerun")
            {
                route("/{fixturerunid}")
                {
                    get {
                       repo.FixtureRuns.find {
                            it.fixtureRunId == call.parameters["fixturerunid"]
                        }?.let {
                            call.respond(it)
                        } ?: call.respondText(ContentType.Application.Json, HttpStatusCode.NotFound) {
                            "{\"errorMessage\":\"Could not find fixturerun with id\"}"
                        }
                    }
                    post {
                        var run = call.receive<FixtureRun>()
                        repo.FixtureRuns.add(run)
                        call.respond(HttpStatusCode.OK, "")
                    }
                    route("/testruns")
                    {
                        get {
                           var testruns = repo.TestRuns.filter { it.fixtureRunId == call.parameters["fixturerunid"] }
                            call.respond(testruns)
                        }
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
