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


@Suppress("unused") // Referenced in application.conf
fun Application.gridModule(gridRepo : GridRepo)
{
    routing {

        install(StatusPages)
        {
            exception<Throwable> {cause ->
               call.respond(HttpStatusCode.BadRequest, Error("Exception in Grid API. Cause: $cause"))
            }
        }
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
                    route("/service")
                    {
                        serviceApi(gridRepo)
                    }
                }
            }
        }
    }
}


fun Route.serviceApi(gridRepo : GridRepo)
{
    suspend fun ApplicationCall.hostAndService( block: suspend (host : Host?, service : Service?) -> Unit) {
        var host = parameters["host"]?.let { hostname ->
            gridRepo.getHosts().find { it.hostname == hostname }
        }

        var service = host?.let {
            parameters["service"]?.let { name ->
                host.services.find { it::class.simpleName.toLowerCase() == name.toLowerCase() }
            }
        }

        block(host, service)
    }
    route("/{service}")
    {
        route ("/session")
        {
            get {
                call.hostAndService(){ host, service ->
                    host?.let {
                        service?.let {
                            val sessionId = gridRepo.getCurrentSessionIdForService(service)
                            sessionId?.let {
                                call.respond(ObjectId(ObjectType.Session, sessionId))
                            }?: call.respond(Error("No Active Session"))
                        } ?: call.respond(HttpStatusCode.BadRequest, Error("Host ${host.hostname} does not have service ${call.parameters["service"]}"))
                    }
                }
            }
            post {
                call.hostAndService(){ host, service ->
                    host?.let {
                        service?.let {
                            var sessionId = call.receive<String>()
                            //TODO: Save sessionID
                            call.respond(ObjectId(ObjectType.Session, sessionId))
                        } ?: call.respond(HttpStatusCode.NotFound, Error("Host ${host.hostname} does not have service ${call.parameters["service"]}"))
                    } ?: call.respond(HttpStatusCode.NotFound, Error("Host ${call.parameters["host"]} not found"))
                }
            }
            delete {

            }
        }

        route ("/state")
        {
            get {
                call.hostAndService(){ host, service ->
                    host?.let {
                        service?.let {
                            var state = gridRepo.getServiceState(host, service)
                            call.respond(state)
                        }
                    } ?: call.respond(HttpStatusCode.BadRequest, Error("Host ${call.parameters["host"]} not found"))
                }
            }
        }
    }
}
