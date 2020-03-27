package com.example

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlin.Exception

class GridRepo {

    private val hubs = mutableListOf(Hub("Venom","localhost", 4444,
        HubStatus.OnlineHub(
            HubStatus.Status(1, HubStatus.Value(true, ""),HubStatus.Os("","","") ))))
    private val nodes = mutableListOf(Node("agent1", "Venom", "localhost", 4723))

    fun addHub(hub : Hub) : String{
        hubs.add(hub)
        return hub.name
    }

    suspend fun getHubs() : List<Hub> {

        val client = HttpClient()

        hubs.forEach {
            try {
                var status = client.get<HubStatus>("${it.hostname}:${it.port}/wd/hub/status")
                it.status = status
            } catch (e : Exception)
            {
               // it.status = HubStatus.Offline
            }
        }
        return hubs
    }

    fun addNode(node : Node) : String {
        nodes.add(node)
        return node.name
    }
    suspend fun getNodes() : List<Node>
    {
        val client = HttpClient()
        nodes.forEach {
            try {
                val status = client.get<NodeStatus>("${it.hostname}:${it.port}/wd/hub/status")
                it.status = status
            } catch (e : Exception)
            {
                it.status = NodeStatus.Offline
            }
        }

       return nodes
    }
}