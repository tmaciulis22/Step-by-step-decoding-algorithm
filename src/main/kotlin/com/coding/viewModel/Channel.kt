package com.coding.viewModel

import tornadofx.ViewModel
import com.coding.util.reverseBit
import kotlin.random.Random

class Channel : ViewModel() {

    private var parameterPe = 0.0

    // Initializes the channel with Pe mistake probability parameter
    fun init(parameterPe: Double) {
        this.parameterPe = parameterPe
    }

    // Sends a vector through channel and marks any mistakes made in vector. Used by first scenario
    // Arguments: vector - binary vector message
    // Returns: Pair consisting of: a new possibly modified vector, a list of mistakes made in vector expressed in boolean values
    fun sendAndMarkMistakes(vector: List<Int>): Pair<List<Int>, List<Boolean>> {
        val vectorToReturn = mutableListOf<Int>()
        val mistakes = mutableListOf<Boolean>()

        vector.forEach {
            val isMistake = Random.nextDouble(0.0, 1.0) < parameterPe
            vectorToReturn.add(if (isMistake) it.reverseBit() else it)
            mistakes.add(isMistake)
        }

        return Pair(vectorToReturn, mistakes)
    }

    // Sends a vector through channel
    // Arguments: vector - binary vector message
    // Returns: possibly modified binary vector message
    fun send(vector: List<Int>): List<Int> = vector.map {
        if (Random.nextDouble(0.0, 1.0) < parameterPe)
            it.reverseBit()
        else
            it
    }
}
