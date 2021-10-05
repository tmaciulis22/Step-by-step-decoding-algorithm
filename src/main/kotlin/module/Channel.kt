package module

import tornadofx.Controller

class Channel : Controller() {

    var parameterPe = 0.0
        private set

    fun init(parameterPe: Double) {
        this.parameterPe = parameterPe
    }
}
