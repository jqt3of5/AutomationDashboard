package com.example

import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*

fun Application.runsModule(testRepo : TestRepo) {

    routing {
        route("/api") {
            route("/fixtureruns")
            {
                get {
                    call.respond(testRepo.FixtureRuns)
                }
            }
            registerFixtureApi(testRepo)
            registerTestApi(testRepo)
        }
    }
}

fun Route.registerTestApi(testRepo: TestRepo)
{
    fun ApplicationCall.testRunId() : String? {
        return parameters["testrunid"]
    }
    suspend fun ApplicationCall.testRun() : TestRun? {
        return testRunId()?.let {
            testRepo.getTestRun(it)
        }
    }
    route("/testrun") {
        post {
            var testrun = call.receive<TestRun>()
            testRepo.addTestRun(testrun)
            call.respond(HttpStatusCode.OK, ObjectId(ObjectType.Test, testrun.testRunId))
        }
        route("/{testrunid}")
        {
            get {
                call.testRun()?.let {
                    call.respond(it)
                } ?: call.respond(HttpStatusCode.NotFound, Error("Could not find test run with id"))
            }
            route("/steps")
            {
                get {
                    call.testRunId()?.let {
                        testRepo.getTestStepsForTestRun(it)
                    }?.let {
                        call.respond(it)
                    } ?: call.respond(HttpStatusCode.NotFound, Error("Could not find test run with id"))
                }
            }
            route("/teststep")
            {
                post {
                    var teststep = call.receive<TestStep>()
                    testRepo.addTestStep(teststep)
                    call.respond(HttpStatusCode.OK, ObjectId(ObjectType.TestStep, teststep.testStepId))
                }
            }
        }
    }
}
fun Route.registerFixtureApi(testRepo : TestRepo)
{
    fun ApplicationCall.fixtureRunId() : String? {
        return parameters["fixturerunid"]
    }

    suspend fun ApplicationCall.fixtureRun() : FixtureRun? {
        return fixtureRunId()?.let {
            testRepo.getTestFixtureRun(it)
        }
    }
    route("/fixturerun")
    {
        post {
            var run = call.receive<FixtureRun>()
            testRepo.addTestFixtureRun(run)
            call.respond(HttpStatusCode.OK, ObjectId(ObjectType.Fixture, run.fixtureRunId))
        }
        route("/{fixturerunid}")
        {
            get {
                call.fixtureRunId()?.let {
                    call.respond(it)
                } ?: call.respond(HttpStatusCode.NotFound, Error("Could not find fixture run with id"))
            }
            route("/session")
            {
                get {
                    call.fixtureRunId()?.let {
                        testRepo.getSessionForFixtureRun(it)?.let {
                            call.respond(it)
                        } ?: call.respond(HttpStatusCode.NotFound, Error("FixtureRun with id does not have session"))
                    } ?: call.respond(HttpStatusCode.NotFound, Error("Could not find fixturerun with id"))
                }
                post {
                    call.fixtureRunId()?.let {
                        val session = call.receive<Session>()
                        testRepo.addSession(session)

                        call.respond(ObjectId(ObjectType.Session, session.sessionId))
                    } ?: call.respond(
                        HttpStatusCode.NotFound,
                        Error("Could not find fixture run with id")
                    )
                }
            }

            route("/testruns")
            {
                get {
                    call.fixtureRunId()?.let {
                        call.respond(it)
                    } ?: call.respond(HttpStatusCode.BadRequest, Error("Could not find tests for fixture id"))
                }
            }
        }
    }
}