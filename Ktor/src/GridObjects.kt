package com.example

import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule

val gridModule = SerializersModule {
    polymorphic(HubStatus::class) {
        HubStatus.Offline::class with HubStatus.Offline.serializer()
        HubStatus.Unknown::class with HubStatus.Unknown.serializer()
        HubStatus.OnlineHub::class with HubStatus.OnlineHub.serializer()
    }
    polymorphic(NodeStatus::class) {
        NodeStatus.Offline::class with NodeStatus.Offline.serializer()
        NodeStatus.Unknown::class with NodeStatus.Unknown.serializer()
        NodeStatus.OnlineNode::class with NodeStatus.OnlineNode.serializer()
        NodeStatus.Disconnected::class with NodeStatus.Disconnected.serializer()
    }
}
@Serializable
sealed class HubStatus {
    @Serializable
    object Offline : HubStatus()
    @Serializable
    object Unknown : HubStatus()

    @Serializable
    data class Value (
        val ready : Boolean,
        val message : String
    )
    @Serializable
    data class Os (
        val arch : String,
        val name : String,
        val version : String
    )
    @Serializable
    data class Status(
        val status :Int,
        val value : Value,
        val os : Os
    )
    @Serializable
    class OnlineHub(val status : Status) : HubStatus()
}

@Serializable
sealed class NodeStatus {
    @Serializable
    object Offline : NodeStatus()
    @Serializable
    object Disconnected : NodeStatus()
    @Serializable
    object Unknown : NodeStatus()
    @Serializable
    class OnlineNode(val status : Status) : NodeStatus()


    @Serializable
    data class Status(val status :Int, val sessionId : String)
}
@Serializable
data class Hub(
    val name : String,
    val hostname : String,
    val port : Int,
    var status : HubStatus = HubStatus.Unknown
)


@Serializable
data class Node(
    val name : String,
    val hubName : String,
    val hostname : String,
    val port : Int,
    var status : NodeStatus = NodeStatus.Unknown
)

