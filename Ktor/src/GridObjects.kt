package com.example

import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule

val gridModule = SerializersModule {
    polymorphic(Service::class) {
        Service.Hub::class with Service.Hub.serializer()
        Service.Node::class with Service.Node.serializer()
        Service.ScreenRecorder::class with Service.ScreenRecorder.serializer()
    }
    polymorphic(NodeState::class) {
        NodeState.Offline::class with NodeState.Offline.serializer()
        NodeState.Unknown::class with NodeState.Unknown.serializer()
        NodeState.Connected::class with NodeState.Connected.serializer()
        NodeState.Disconnected::class with NodeState.Disconnected.serializer()
    }
}

@Serializable
open class ServiceStatus
{

}

@Serializable
sealed class ServiceState<out T : ServiceStatus> {
    object Unknown : ServiceState<Nothing>()
    object Offline : ServiceState<Nothing>()

    class Online<T:ServiceStatus>(val status : T) : ServiceState<T>()
}
@Serializable
sealed class Service<T>(
    val port : Int,
    var state : ServiceState<*> = ServiceState.Unknown
){
    class Hub(port : Int, state : ServiceState) : Service<SeleniumStatus>(port, state)
    class Node(port : Int, state : ServiceState, val hubName: String) : Service<AppiumStatus>(port, state)
    class ScreenRecorder(port : Int, state : ServiceState) : Service<Nothing>(port, state)
}

@Serializable
data class AppiumStatus(
    val status :Int,
    val sessionId : String
)

@Serializable
data class SeleniumStatus(
    val status :Int,
    val value : Value
){
    @Serializable
    data class Value (
        val ready : Boolean,
        val message : String,
        val os : Os,
        val build : Build,
        val java : Java
    ) {
        @Serializable
        data class Java(
            val version: String
        )
        @Serializable
        data class Build(
            val revision: String,
            val time: String,
            val version: String
        )
        @Serializable
        data class Os(
            val arch: String,
            val name: String,
            val version: String
        )
    }
}
