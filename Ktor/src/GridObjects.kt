package com.example

import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule

val gridModule = SerializersModule {
    polymorphic(Service::class) {
        Service.Hub::class with Service.Hub.serializer()
        Service.Node::class with Service.Node.serializer()
        Service.ScreenRecorder::class with Service.ScreenRecorder.serializer()
        Service.WinAppDriver::class with Service.WinAppDriver.serializer()
    }
    polymorphic(ServiceState::class) {
        ServiceState.Offline::class with ServiceState.Offline.serializer()
        ServiceState.Unknown::class with ServiceState.Unknown.serializer()
        ServiceState.Online::class with ServiceState.Online.serializer(PolymorphicSerializer(Status::class))
    }
}

class Host(
    val name : String,
    val hostname : String,
    val services : MutableList<Service>
)

sealed class ServiceState<out T : Status>{
    @Serializable
    object Unknown : ServiceState<Nothing>()
    @Serializable
    object Offline : ServiceState<Nothing>()
    @Serializable
    class Online<out T : Status>(val status : T) : ServiceState<T>()
}

sealed class Service {
    @Serializable
    class Hub(val port : Int = 4444) : Service()
    @Serializable
    class Node(val hubName: String, val port : Int = 4723) : Service()
    @Serializable
    class WinAppDriver(val port : Int = 4724) : Service()
    @Serializable
    class ScreenRecorder(val port : Int = 8080) : Service()
}

@Serializable
open class Status

data class ScreenRecorderStatus(
    val isRecording : Boolean,
    val recordingId : String?
) : Status()

@Serializable
data class AppiumStatus(
    val status :Int,
    val sessionId : String,
    //TODO: Determine if this will actually work, since it isnt returned by the appium node.
    var connected : Boolean = false
) : Status()

@Serializable
data class SeleniumStatus(
    val status :Int,
    val value : Value
) : Status() {
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
