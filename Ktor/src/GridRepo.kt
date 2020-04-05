package com.example

import io.ktor.application.call
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.response.respond

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

    fun addServiceToHost(host: Host, service : Service)
    {
       host.services.apply {
            add(service)
        }
    }

    fun getHosts() = hosts

    suspend fun getCurrentSessionIdForService(host : Host, service : Service) : String?
    {
        return "sessionId"
    }

    suspend fun updateSessionIdForService(host: Host, service : Service, sessionId : String)
    {

    }
    suspend fun removeSessionForService(host : Host, service : Service)
    {

    }

    suspend fun  getServiceState(host: Host, service : Service) : ServiceState<Status>
    {
        when(service)
        {
            is Service.Hub -> {
               return getHubState(host, service)
            }
            is Service.Node -> {
                return getNodeState(host, service)
            }
            is Service.ScreenRecorder -> {
                return getScreenRecorderState(host, service)
            }
            is Service.WinAppDriver -> {
                return getWinAppDriverState(host, service)
            }
        }
    }
    suspend fun getHubState(host : Host, hub : Service.Hub) : ServiceState<SeleniumStatus>
    {
        try {
            var status = httpClient.get<SeleniumStatus>("http://${host.hostname}:${hub.port}/wd/hub/status")
            return ServiceState.Online(status)
        } catch (e : Throwable)
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
        } catch (e : Throwable)
        {
            return ServiceState.Offline
        }
    }

    suspend fun getScreenRecorderState(host : Host, screenrecorder : Service.ScreenRecorder) : ServiceState<ScreenRecorderStatus>
    {
        try {
            val status = httpClient.get<ScreenRecorderStatus>("http://${host.hostname}:${screenrecorder.port}/status")
            return ServiceState.Online(status)
        } catch (e : Throwable)
        {
            return ServiceState.Offline
        }
    }
    suspend fun getWinAppDriverState(host : Host, winAppDriver: Service.WinAppDriver) : ServiceState<WinAppDriverStats>
    {
        try {
            val status = httpClient.get<WinAppDriverStats>("http://${host.hostname}:${winAppDriver.port}/status")
            return ServiceState.Online(status)
        } catch (e : Throwable)
        {
            return ServiceState.Offline
        }
    }
}