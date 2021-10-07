package viewModel

import tornadofx.ViewModel
import util.reverseBit
import kotlin.random.Random

class Channel : ViewModel() {

    private var parameterPe = 0.0

    fun init(parameterPe: Double) {
        this.parameterPe = parameterPe
    }

    fun send(vector: MutableList<Int>?): Pair<List<Int>, List<Boolean>>? {
        vector ?: return null

        val mistakes = MutableList(vector.size) { false }
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
