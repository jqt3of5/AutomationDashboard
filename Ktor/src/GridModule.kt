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


@Suppress("unused") // Referenced in application.conf
fun Application.gridModule(gridRepo : GridRepo)
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
            }
            route("/nodes") {
                get {
                    val nodes = gridRepo.getNodes()
                    call.respond(nodes)
                }
            }
            route("/node") {
                post {
                    val node = call.receive<Node>()
                    val name = gridRepo.addNode(node)
                    call.respond(ObjectId(ObjectType.Node, name))
                }
            }
        }
    }
}