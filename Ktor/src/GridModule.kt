package com.example

import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*

fun Application.gridModule(gridRepo : GridRepo)
{
    routing {


        route("/api") {
            route ("/hosts")
            {
                get {
                    call.respond(gridRepo.getHosts())
                }
            }

            route ("/host")
            {
                route("/{host}")
                {
                    get {
                        gridRepo.getHosts().find {
                            it.id == call.parameters["host"]
                        }?.let {
                            call.respond(it)
                        }?: call.respond(Error("No host with name ${call.parameters["name"]}"))
                    }
                    route("/services")
                    {
                        get {
                            gridRepo.getHosts().find {
                                it.id == call.parameters["host"]
                            }?.let {
                                call.respond(it.services)
                            }?: call.respond(Error("No host with name ${call.parameters["name"]}"))
                        }
                    }
                    registerServiceApi(gridRepo)
                }
            }
        }
    }
}

fun Route.registerServiceApi(gridRepo : GridRepo) {
    fun ApplicationCall.host(): Host? {
        return parameters["host"]?.let { hostname ->
            gridRepo.getHosts().find { it.hostname == hostname }
        }
    }

    fun ApplicationCall.service(): Service? {

        return host()?.let {
            parameters["service"]?.let { name ->
                it.services.find { it::class.simpleName?.toLowerCase() == name.toLowerCase() }
            }
        }
    }
    route("/service")
    {
        post {
            val service = call.receive<Service>()
            call.host()?.let {
                gridRepo.addServiceToHost(it, service)
            } ?: call.respond(
                HttpStatusCode.NotFound,
                Error("Host ${call.parameters["host"]} not found")
            )
        }
        route("/{service}")
        {
            route("/session")
            {
                get {
                    call.host()?.let { host ->
                        call.service()?.let { service ->
                            val sessionId = gridRepo.getCurrentSessionIdForService(host, service)
                            sessionId?.let {
                                call.respond(ObjectId(ObjectType.Session, sessionId))
                            } ?: call.respond(Error("No Active Session"))
                        } ?: call.respond(
                            HttpStatusCode.BadRequest,
                            Error("Host ${host.hostname} does not have service ${call.parameters["service"]}")
                        )
                    } ?: call.respond(HttpStatusCode.NotFound, Error("Host ${call.parameters["host"]} not found"))
                }

                delete {
                    call.host()?.let { host ->
                        call.service()?.let { service ->
                            gridRepo.removeSessionForService(host, service)
                            call.respond("")
                        } ?: call.respond(
                            HttpStatusCode.NotFound,
                            Error("Host ${host.hostname} does not have service ${call.parameters["service"]}")
                        )
                    } ?: call.respond(HttpStatusCode.NotFound, Error("Host ${call.parameters["host"]} not found"))
                }
            }

            route("/state")
            {
                get {
                    call.host()?.let { host ->
                        call.service()?.let { service ->
                            var state = gridRepo.getServiceState(host, service)
                            call.respond(state)
                        }
                    } ?: call.respond(HttpStatusCode.BadRequest, Error("Host ${call.parameters["host"]} not found"))
                }
            }
        }
    }
}
