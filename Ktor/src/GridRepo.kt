package com.example

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlin.Exception

class GridRepo {

    private val hubs = mutableListOf(Hub("Venom","localhost", 4444))
    private val nodes = mutableListOf(Node("agent1", "Venom", "localhost", 4723))

    fun addHub(hub : Hub) : String{
        hubs.add(hub)
        return hub.name
    }

    suspend fun getHubs() : List<Hub> {
        return hubs
    }

    suspend fun getNodesForHub(hubName : String) : List<Node>
    {
        val client = HttpClient()
        val hub = hubs.find { it.name == hubName } ?: throw Exception("Hub with name: $hubName does not exist")

        //val nodes = client.get<List<Node>>("${hub.hostname}:${hub.port}/X1Proxy")


       return nodes
    }
}