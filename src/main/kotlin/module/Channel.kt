package module

import tornadofx.Controller
import util.reverseBit
import kotlin.random.Random

class Channel : Controller() {

    private var parameterPe = 0.0

    fun init(parameterPe: Double) {
        this.parameterPe = parameterPe
    }

    fun send(vector: Array<Int>?): Pair<Array<Int>, Array<Boolean>>? {
        vector ?: return null

        val mistakes = Array(vector.size) { false }
        for (index in vector.indices) {
            val mistakeProbability = Random.nextDouble(0.0, 1.0)
            if (mistakeProbability < parameterPe) {
                vector[index] = vector[index].reverseBit()
                mistakes[index] = true
            }
        }

        return Pair(vector, mistakes)
    }
}
