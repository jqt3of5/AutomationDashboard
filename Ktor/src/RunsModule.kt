package com.example

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing

val repo = TestRepo()

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.runsModule(testing: Boolean = false) {

    routing {
        route("/api") {
            route("/fixtureruns")
            {
                get {
                    call.respond(repo.FixtureRuns)
                }
            }
            route("/fixturerun")
            {
                post {
                    var run = call.receive<FixtureRun>()
                    repo.addTestFixtureRun(run)
                    call.respond(HttpStatusCode.OK, ObjectId(ObjectType.Fixture, run.fixtureRunId))
                }
                route("/{fixturerunid}")
                {
                    get {
                        call.parameters["fixturerunid"]?.let {
                            repo.getTestFixtureRun(it)
                        }?.let {
                            call.respond(it)
                        }?: call.respond(HttpStatusCode.NotFound, Error("Could not find fixturerun with id"))
                    }
                    route("/session")
                    {
                        post {
                            val session = call.receive<Session>()
                            repo.addSession(session)
                            call.respond("")
                        }
                        get {
                            call.parameters["fixturerunid"]?.let {
                                repo.getSessionForFixture(it)
                            }?.let {
                                call.respond(it)
                            }?: call.respond(HttpStatusCode.NotFound, Error("Could not find fixturerun with id"))
                        }
                    }
                    route("/testruns")
                    {
                        get {
                            call.parameters["fixturerunid"]?.let {
                               repo.getTestRunsForFixtureRun(it)
                            }?.let {
                                call.respond(it)
                            } ?: call.respond(HttpStatusCode.BadRequest, Error("Could not find tests for fixture id"))
                        }
                    }
                }
            }
            route("/testrun") {
                post {
                    var testrun = call.receive<TestRun>()
                    repo.addTestRun(testrun)
                    call.respond(HttpStatusCode.OK, ObjectId(ObjectType.Test, testrun.testRunId))
                }
                route("/{testrunid}")
                {
                    get {
                        call.parameters["testrunid"]?.let {
                            repo.getTestRun(it)
                        }?.let {
                            call.respond(it)
                        } ?: call.respond(HttpStatusCode.NotFound, Error("Could not find test run with id"))
                    }
                    route("/steps")
                    {
                        get {
                            call.parameters["testrunid"]?.let {
                                repo.getTestStepsForTestRun(it)
                            }?.let {
                                call.respond(it)
                            } ?: call.respond(HttpStatusCode.NotFound, Error("Could not find test run with id"))
                        }
                    }
                }
            }
            route("/teststep")
            {
                post {
                    var teststep = call.receive<TestStep>()
                    repo.addTestStep(teststep)
                    call.respond(HttpStatusCode.OK, ObjectId(ObjectType.TestStep, teststep.testStepId))
                }
            }
        }
    }
}