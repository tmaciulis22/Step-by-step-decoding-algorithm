package com.coding.viewModel

import tornadofx.ViewModel

class ExperimentViewModel : ViewModel() {

    private val encoder: Encoder by inject()
    private val channel: Channel by inject()
    private val decoder: Decoder by inject()

    // How many zeros were added to the last message vector, so it would be of size K
    private var zeroesAppended = 0

    val parametersN = mutableListOf<Int>()
    val parametersK = mutableListOf<Int>()
    val firstExperimentResultsCoded = mutableListOf<Int>()
    val firstExperimentResultsUncoded = mutableListOf<Int>()

    val parametersPe = mutableListOf<Double>()
    val secondExperimentResultsCoded = mutableListOf<Int>()

    fun runExperiments() {
        runFirstExperiment()
        runSecondExperiment()
    }

    private fun runFirstExperiment() {
        channel.init(FIRST_EXPERIMENT_PARAMETER_PE)
        for (parameterN in FIRST_EXPERIMENT_PARAMETER_N_END downTo FIRST_EXPERIMENT_PARAMETER_N_START) {
            for (parameterK in FIRST_EXPERIMENT_PARAMETER_K_START until parameterN) {
                zeroesAppended = 0
                encoder.init(parameterN, parameterK)
                encoder.generateRandomRemainingMatrix()
                decoder.init(parameterN, parameterK, encoder.generatorMatrix)

                val encodedVectors = getMessageVectors().map { encoder.encode(it) }
                val sentVectors = encodedVectors.map { channel.send(it) }
                val decodedVectors = sentVectors.map { decoder.decode(it) }.flatten().dropLast(zeroesAppended)

                val sentUncodedVectors = channel.send(MESSAGE_VECTOR)

                val codedVersusOriginal = decodedVectors.getBitDifference(MESSAGE_VECTOR)
                val uncodedVersusOriginal = sentUncodedVectors.getBitDifference(MESSAGE_VECTOR)
                parametersN.add(parameterN)
                parametersK.add(parameterK)
                firstExperimentResultsCoded.add(codedVersusOriginal)
                firstExperimentResultsUncoded.add(uncodedVersusOriginal)
            }
        }
    }

    private fun runSecondExperiment() {
        var parameterPe = SECOND_EXPERIMENT_PARAMETER_PE_START
        encoder.init(SECOND_EXPERIMENT_PARAMETER_N, SECOND_EXPERIMENT_PARAMETER_K)
        decoder.init(SECOND_EXPERIMENT_PARAMETER_N, SECOND_EXPERIMENT_PARAMETER_K, encoder.generatorMatrix)

        while (parameterPe <= SECOND_EXPERIMENT_PARAMETER_PE_END) {
            zeroesAppended = 0
            channel.init(parameterPe)

            val encodedVectors = getMessageVectors().map { encoder.encode(it) }
            val sentVectors = encodedVectors.map { channel.send(it) }
            val decodedVectors = sentVectors.map { decoder.decode(it) }.flatten().dropLast(zeroesAppended)

            val codedVersusOriginal = decodedVectors.getBitDifference(MESSAGE_VECTOR)

            parametersPe.add(parameterPe)
            secondExperimentResultsCoded.add(codedVersusOriginal)

            parameterPe += SECOND_EXPERIMENT_PARAMETER_PE_INCREMENT
        }
    }

    private fun getMessageVectors(): List<List<Int>> {
        val binaryVectors = mutableListOf<List<Int>>()
        val currentVector = mutableListOf<Int>()

        MESSAGE_VECTOR.forEachIndexed { index, bit ->
            currentVector.add(bit)

            if (currentVector.size == encoder.parameterK) {
                binaryVectors.add(currentVector.toList())
                currentVector.clear()
            } else if (index == MESSAGE_VECTOR.lastIndex) {
                while (currentVector.size != encoder.parameterK) {
                    currentVector.add(0)
                    zeroesAppended++
                }
                binaryVectors.add(currentVector.toList())
                currentVector.clear()
            }
        }

        return binaryVectors
    }

    private fun List<Int>.getBitDifference(listToCompare: List<Int>): Int {
        if (this.size != listToCompare.size) return 0

        return mapIndexed { index, bit ->
            if (bit != listToCompare[index])
                1
            else
                0
        }.sum()
    }

    companion object {
        private val MESSAGE_VECTOR =
            listOf(1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 0, 0, 0, 1)

        private const val FIRST_EXPERIMENT_PARAMETER_PE = 0.01
        private const val FIRST_EXPERIMENT_PARAMETER_N_START = 3
        private const val FIRST_EXPERIMENT_PARAMETER_N_END = 15
        private const val FIRST_EXPERIMENT_PARAMETER_K_START = 2

        private const val SECOND_EXPERIMENT_PARAMETER_N = 5
        private const val SECOND_EXPERIMENT_PARAMETER_K = 3
        private const val SECOND_EXPERIMENT_PARAMETER_PE_START = 0.0001
        private const val SECOND_EXPERIMENT_PARAMETER_PE_END = 0.5
        private const val SECOND_EXPERIMENT_PARAMETER_PE_INCREMENT = 0.005
    }
}
