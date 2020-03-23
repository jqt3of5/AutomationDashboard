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
                    repo.FixtureRuns.add(run)
                    call.respond(HttpStatusCode.OK, ObjectId(ObjectType.Fixture, run.fixtureRunId))
                }
                route("/{fixturerunid}")
                {
                    get {
                        repo.FixtureRuns.find {
                            it.fixtureRunId == call.parameters["fixturerunid"]
                        }?.let {
                            call.respond(it)
                        } ?: call.respond(HttpStatusCode.NotFound, Error("Could nto find fixturerun with id"))
                    }
                    route("/session")
                    {
                        post {
                            repo.FixtureRuns.find {
                                it.fixtureRunId == call.parameters["fixturerunid"]
                            }?.let {
                                var sessionId = call.receive<String>()
                                //TODO: Use sessionId to get the node
                                it.sessionId = sessionId
                                call.respond("")
                            } ?: call.respond(HttpStatusCode.NotFound, Error("Could nto find fixturerun with id"))
                        }
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
            route("/testrun") {
                post {
                    var testrun = call.receive<TestRun>()
                    repo.TestRuns.add(testrun)
                    call.respond(HttpStatusCode.OK, ObjectId(ObjectType.Test, testrun.testRunId))
                }
                route("/{testrunid}")
                {
                    get {
                        repo.TestRuns.find {
                            it.testRunId == call.parameters["testrunid"]
                        }?.let {
                            call.respond(it)
                        } ?: call.respond(HttpStatusCode.NotFound, Error("Could not find test run with id"))
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
                post {
                    var teststep = call.receive<TestStep>()
                    repo.TestSteps.add(teststep)
                    call.respond(HttpStatusCode.OK, ObjectId(ObjectType.TestStep, teststep.testStepId))
                }
                route("/{teststepid}")
                {
                    get {
                        repo.TestSteps.find {
                            it.testStepId == call.parameters["teststepid"]
                        }?.let {
                            call.respond(it)
                        } ?: call.respond(HttpStatusCode.NotFound, Error("Could not find teststep with id"))
                    }
                }
            }

        }
    }
}