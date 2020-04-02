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
    fun ApplicationCall.host() : Host? {
        return parameters["host"]?.let { hostname ->
            gridRepo.getHosts().find { it.hostname == hostname }
        }
    }

    fun ApplicationCall.service() : Service? {

       return host()?.let {
           parameters["service"]?.let { name ->
               it.services.find { it::class.simpleName?.toLowerCase() == name.toLowerCase() }
           }
       }
    }
    route("/{service}")
    {
        post {
            val servicename = call.parameters["service"]
            when (servicename)
             {
                "hub" -> {
                    call.host()?.let {
                        val hub = call.receive<Service.Hub>()
                        gridRepo.addServiceToHost(it, hub)
                    }?: call.respond(HttpStatusCode.NotFound, Error("Host ${call.parameters["host"]} not found"))
                }
                "node" -> {
                    call.host()?.let {
                        val node = call.receive<Service.Node>()
                        gridRepo.addServiceToHost(it, node)
                    } ?: call.respond(HttpStatusCode.NotFound, Error("Host ${call.parameters["host"]} not found"))
                }
                "screenrecorder" -> {
                    call.host()?.let {
                        val recorder = call.receive<Service.ScreenRecorder>()
                        gridRepo.addServiceToHost(it, recorder)
                    } ?: call.respond(HttpStatusCode.NotFound, Error("Host ${call.parameters["host"]} not found"))
                }
                "winappdriver" -> {
                call.host()?.let {
                    val driver = call.receive<Service.WinAppDriver>()
                        gridRepo.addServiceToHost(it, driver)
                    } ?: call.respond(HttpStatusCode.NotFound, Error("Host ${call.parameters["host"]} not found"))
               }
            }
        }
        route ("/session")
        {
            get {
                    call.host()?.let {host ->
                        call.service()?.let { service ->
                            val sessionId = gridRepo.getCurrentSessionIdForService(host, service)
                            sessionId?.let {
                                call.respond(ObjectId(ObjectType.Session, sessionId))
                            }?: call.respond(Error("No Active Session"))
                        } ?: call.respond(HttpStatusCode.BadRequest, Error("Host ${host.hostname} does not have service ${call.parameters["service"]}"))
                    } ?:  call.respond(HttpStatusCode.NotFound, Error("Host ${call.parameters["host"]} not found"))
                }
            }
            post {
                    call.host()?.let {host ->
                        call.service()?.let {service ->
                            val sessionId = call.receive<String>()
                            gridRepo.updateSessionIdForService(host, service, sessionId)
                            call.respond(ObjectId(ObjectType.Session, sessionId))
                        } ?: call.respond(HttpStatusCode.NotFound, Error("Host ${host.hostname} does not have service ${call.parameters["service"]}"))
                    } ?: call.respond(HttpStatusCode.NotFound, Error("Host ${call.parameters["host"]} not found"))
            }
            delete {
                call.host()?.let {host ->
                    call.service()?.let {service ->
                        gridRepo.removeSessionForService(host, service)
                        call.respond("")
                    } ?: call.respond(HttpStatusCode.NotFound, Error("Host ${host.hostname} does not have service ${call.parameters["service"]}"))
                } ?: call.respond(HttpStatusCode.NotFound, Error("Host ${call.parameters["host"]} not found"))
            }
        }

        route ("/state")
        {
            get {
                    call.host()?.let {host ->
                        call.service()?.let {service ->
                            var state = gridRepo.getServiceState(host, service)
                            call.respond(state)
                        }
                    } ?: call.respond(HttpStatusCode.BadRequest, Error("Host ${call.parameters["host"]} not found"))
                }
            }
        }
    }
}
