package com.example

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

class Host(
    val id : String,
    val hostname : String,
    @Polymorphic val services : MutableList<Service>
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
sealed class Status

data class WinAppDriverStats(
    val something: String
) : Status()

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
