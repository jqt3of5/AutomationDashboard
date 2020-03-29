package com.example

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

        hubs.forEach {
            try {
                var status = httpClient.get<HubState.Online.SeleniumStatus>("http://${it.hostname}:${it.port}/wd/hub/status")
                it.status = HubState.Online(status)
            } catch (e : Exception)
            {
                it.status = HubState.Offline
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
        nodes.forEach {
            try {
                val status = httpClient.get<NodeState.AppiumStatus>("http://${it.hostname}:${it.port}/wd/hub/status")
                //TODO: Is it actually connected to Hub?
                it.status = NodeState.Connected(status)
            } catch (e : Exception)
            {
                it.status = NodeState.Offline
            }
        }
       return nodes
    }
}