package com.example

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlin.Exception


class GridRepo {

    val hubs = mutableListOf(Hub("Venom","localhost", 4444))
    val nodes = mutableListOf(Node("agent1", "Venom", "localhost", 4723))


    fun addHub(hub : Hub) : String
    {
        hubs.add(hub)
        return hub.name
    }

    fun addNode(node : Node) : String
    {
        nodes.add(node)
        return node.name
    }

    suspend fun getHubs() : List<Hub> {
        return hubs
    }

    suspend fun getNodesForHub(hubName : String) : List<Node>
    {
        val client = HttpClient()
        val hub = hubs.find { it.name == hubName } ?: throw Exception("Hub with name: $hubName does not exist")

        val nodes = client.get<List<Node>>("${hub.hostname}:4444/X1Proxy")


       return nodes
    }

    suspend fun getSessionsForHub(hubName : String) : List<Session>
    {
        val client = HttpClient()
        val hub = hubs.find { it.name == hubName } ?: throw Exception("Hub with name: $hubName does not exist")

        return client.get("${hub.hostname}:4444/wd/hub/sessions")
    }

    suspend fun getSessionsForNode(nodeName : String) : List<Session>
    {
        val client = HttpClient()
        val node = nodes.find { it.name == nodeName } ?: throw Exception("Node with name: $nodeName does not exist")

        return client.get("${node.hostname}:4723/wd/hub/sessions")
    }

}