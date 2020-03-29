package com.example

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlin.Exception

class GridRepo(
    val httpClient : HttpClient
) {

    private val hosts = mutableListOf(
        Host("Venom", "localhost", mutableListOf(Service.Hub())),
        Host("Agent1", "localhost", mutableListOf(Service.Node("Venom"), Service.ScreenRecorder()))
    )

    fun addHost(host : Host)
    {
        hosts.add(host)
    }

    fun addServiceToHost(name : String, service : Service)
    {
        hosts.find { it.name == name }?.services?.apply {
            add(service)
        }
    }

    fun getHosts() = hosts

    suspend fun getStateForService(host : Host, service : Service) : ServiceState<*>
    {
        return when(service)
        {
            is Service.Hub -> getHubState(host, service)
            is Service.Node -> getNodeState(host, service)
            is Service.ScreenRecorder -> getScreenRecorderState(host, service)
            is Service.WinAppDriver -> ServiceState.Unknown
            else -> ServiceState.Unknown
        }
    }

    suspend fun getHubState(host : Host, hub : Service.Hub) : ServiceState<SeleniumStatus>
    {
        try {
            var status = httpClient.get<SeleniumStatus>("http://${host.hostname}:${hub.port}/wd/hub/status")
            return ServiceState.Online(status)
        } catch (e : Exception)
        {
            return ServiceState.Offline
        }
    }

    suspend fun getNodeState(host : Host, node : Service.Node) : ServiceState<AppiumStatus>
    {
        try {
            val status = httpClient.get<AppiumStatus>("http://${host.hostname}:${node.port}/wd/hub/status")
            //TODO: Is it actually connected to Hub?
            status.connected = true
            return ServiceState.Online(status)
        } catch (e : Exception)
        {
            return ServiceState.Offline
        }
    }

    suspend fun getScreenRecorderState(host : Host, screenrecorder : Service.ScreenRecorder) : ServiceState<ScreenRecorderStatus>
    {
        try {
            val status = httpClient.get<ScreenRecorderStatus>("http://${host.hostname}:${screenrecorder.port}/status")
            return ServiceState.Online(status)
        } catch (e : Exception)
        {
            return ServiceState.Offline
        }
    }
}