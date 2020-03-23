package com.example

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing

val gridRepo = GridRepo()

@Suppress("unused") // Referenced in application.conf
fun Application.gridModule()
{
    routing {

        install(StatusPages)
        {
            exception<Exception> {cause ->
               call.respond(HttpStatusCode.BadRequest, Error("Exception in Grid API. Cause: $cause"))
            }
        }
        route("/api") {
            route("/hubs"){
                get {
                   call.respond(gridRepo.getHubs())
                }
            }
            route ("/hub") {
                post {
                    val hub = call.receive<Hub>()
                    val name = gridRepo.addHub(hub)
                    call.respond(ObjectId(ObjectType.Hub, name))
                }
                route("/{hubName}")
                {
                    route("/nodes") {
                        get {
                            call.parameters["hubName"]?.let {
                                val nodes = gridRepo.getNodesForHub(it)
                                call.respond(nodes)
                            } ?: call.respond(HttpStatusCode.BadRequest, Error("No Hub Name"))
                        }
                    }
                }
            }
        }
    }
}