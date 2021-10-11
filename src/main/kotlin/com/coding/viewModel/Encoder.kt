package com.coding.viewModel

import tornadofx.ViewModel
import com.coding.util.multiply
import kotlin.random.Random

class Encoder : ViewModel() {

    var parameterN: Int = 0
        private set
    var parameterK: Int = 0
        private set

    lateinit var generatorMatrix: MutableList<MutableList<Int>>
        private set

    // Initializes the encoder with N and K parameters.
    // Also, it sets identity matrix I in generator matrix G = (I|A).
    // Arguments: parameter N, parameter K
    fun init(parameterN: Int, parameterK: Int) {
        this.parameterN = parameterN
        this.parameterK = parameterK

        generatorMatrix = MutableList(parameterK) { MutableList(parameterN) { 0 } }
        setIdentityMatrix()
    }

    // Fills remaining position in generator matrix after identity matrix
    // G = (I|A) - it fills the A matrix with random bits
    fun generateRandomRemainingMatrix() {
        generatorMatrix.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, _ ->
                if (colIndex >= parameterK)
                    generatorMatrix[rowIndex][colIndex] = Random.nextInt(0, 2)
            }
        }
    }

    // Encodes a message vector of length K by multiplying it with generator matrix
    // Arguments: message vector of length K
    // Returns: encoded vector of length N
    fun encode(vector: List<Int>): List<Int> = generatorMatrix.multiply(vector)

    // Sets identity matrix I in generator matrix G = (I|A)
    private fun setIdentityMatrix() {
        // pointer which shows where to write positive bit 1
        var currentPos = 0
        generatorMatrix.forEach { row ->
            row[currentPos] = 1
            currentPos++
        }
    }
}
