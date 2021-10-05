package controller

import model.Channel
import tornadofx.Controller

class ChannelController : Controller() {
    private lateinit var channel: Channel

    fun createChannel(parameterPe: Double) {
        if (!::channel.isInitialized) channel = Channel(parameterPe)
    }
}
