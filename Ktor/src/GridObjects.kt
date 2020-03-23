package com.example

import kotlinx.serialization.Serializable

enum class HubStatus {
    Idle, //Hub has no active sessions
    Running, //Hub has active sessions
    Offline,//Hub is offline/unreachable
    Unknown //Status has not been determined
}

@Serializable
data class Hub(
    val name : String,
    val hostname : String,
    val port : Int,
    val status : HubStatus = HubStatus.Unknown
)

enum class NodeStatus {
    IdleConnected, //Node is connected to hub, with no active session
    Running,//Node is connected, and has an active session
    Disconnected, //Node is online, but not connected to Hub
    Offline, // Node is unreachable
    Unknown, // Status has not been determined
}
@Serializable
data class Node(
    val name : String,
    val hubName : String,
    val hostname : String,
    val port : Int,
    val status : NodeStatus = NodeStatus.Unknown
)

