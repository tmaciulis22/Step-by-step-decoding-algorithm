package com.example.viewModel

import tornadofx.ViewModel
import com.example.util.getNumOfOnes
import com.example.util.multiplyTransposed
import kotlin.math.pow

class Decoder : ViewModel() {

    private lateinit var controlMatrix: MutableList<MutableList<Int>>

    private var parameterK: Int = 0

    // Map(Table) consisting of syndromes and corresponding class leader weights
    private lateinit var syndromesAndWeightsMap: Map<String, Int>

    // Initializes the Decoder with N and K parameters, and with generator matrix (G) from Encoder.
    // Generator matrix (G) is used for initializing the control matrix (H).
    // G = (I|A), it takes the A matrix, transposes it and puts it into control matrix H = (At|I).
    // This method also generates syndromes and class leaders' weights table
    // Arguments: parameters N and K, generator matrix G=(I|A) of dimensions KxN
    fun init(parameterN: Int, parameterK: Int, generatorMatrix: List<List<Int>>) {
        this.parameterK = parameterK
        controlMatrix = MutableList(parameterN - parameterK) { MutableList(parameterN) { 0 } }
        setTransposeMatrix(generatorMatrix)
        setIdentityMatrix()
        findSyndromesAndWeights(parameterN)
    }

    // Decodes an encoded vector sent through channel.
    // The algorithm used is step-by-step decoding.
    // It goes through each message vector's bit, adds one to it, and checks the modified vector's weight using syndrome.
    // If the weight is less than before, then the change to vector is not reversed and the algorithm moves to next bit.
    // If the weight is same or bigger than before modification, then the change is reversed and the algorithm moves to next bit.
    // The algorithm traverses each bit, until the weight is zero.
    // Arguments: encoded message vector of length N
    // Returns: decoded message vector of length K, with possibly corrected errors
    fun decode(vector: List<Int>): List<Int> {
        val vectorToDecode = vector.toMutableList()

        for (index in vectorToDecode.indices) {
            val syndromeString = controlMatrix.multiplyTransposed(vectorToDecode).toString()
            val weight = syndromesAndWeightsMap[syndromeString] ?: return listOf()

            if (weight == 0) break

            vectorToDecode[index] += 1
            if (vectorToDecode[index] > 1) vectorToDecode[index] = 0

            val newSyndromeString = controlMatrix.multiplyTransposed(vectorToDecode).toString()
            val newWeight = syndromesAndWeightsMap[newSyndromeString] ?: return listOf()

            if (newWeight >= weight) {
                vectorToDecode[index] -= 1
                if (vectorToDecode[index] < 0) vectorToDecode[index] = 1
            }
        }

        return vectorToDecode.subList(0, parameterK).toList()
    }

    // Uses generator matrix G=(I|A) to partially set control matrix H=(At|I)
    // It takes the A matrix from generator matrix, transposes it and sets it in first K columns of control matrix H
    // Arguments: generator matrix G=(I|A) of dimensions KxN
    private fun setTransposeMatrix(generatorMatrix: List<List<Int>>) {
        val generatorMatrixWithoutIdentity = generatorMatrix.map {
            it.drop(parameterK)
        }
        generatorMatrixWithoutIdentity.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, _ ->
                controlMatrix[colIndex][rowIndex] = generatorMatrixWithoutIdentity[rowIndex][colIndex]
            }
        }
    }

    // Sets the identity matrix in control matrix H=(At|I) in last N-K columns
    private fun setIdentityMatrix() {
        // Pointer which shows where to write positive 1 bit
        var currentPos = parameterK
        controlMatrix.forEach { row ->
            row[currentPos] = 1
            currentPos++
        }
    }

    // Generates syndromes and class leader's weights table.
    // Keys of the table are syndromes, and the values are the weights.
    // Arguments: parameter N
    private fun findSyndromesAndWeights(parameterN: Int) {
        // How many classes are there. It is calculated by using the formula from lecturer's notes
        val numOfClasses = 2.0.pow((parameterN - parameterK).toDouble()).toInt()
        val syndromes = MutableList(numOfClasses) { List(parameterN - parameterK ) { 0 } }
        val classLeadersWeights = MutableList(numOfClasses) { 0 }

        // It starts from 1, because the class leader with vector made of only zeroes is already included
        var numOfClassesFound = 1
        // For same reason we start generating vectors with one positive bit
        var numOfPositiveBits = 1
        while (numOfClassesFound < numOfClasses) {
            // Generate all possible vectors with exact number of positive bits
            val possibleClassLeaders = getVectors(parameterN, numOfPositiveBits)
            possibleClassLeaders.forEach { vector ->
                if (numOfClassesFound >= numOfClasses) return@forEach

                val syndrome = controlMatrix.multiplyTransposed(vector)
                if (!syndromes.contains(syndrome)) {
                    syndromes[numOfClassesFound] = syndrome
                    classLeadersWeights[numOfClassesFound] = vector.toList().getNumOfOnes()
                    numOfClassesFound++
                }
            }
            numOfPositiveBits++
        }

        val table = mutableMapOf<String, Int>()
        syndromes.forEachIndexed{ index, syndrome ->
            // To make it easier to lookup weights, the syndromes value is converted to string
            val syndromeString = syndrome.toString()
            table[syndromeString] = classLeadersWeights[index]
        }
        syndromesAndWeightsMap = table
    }

    // Using recursion it generates all possible vectors with exact number of positive bits
    // Arguments: parameter N, num of positive bits in vector, current vector which is being generated(used for recursion)
    // Returns: list of all generated vectors
    private fun getVectors(
        parameterN: Int,
        numOfPositiveBits: Int = 1,
        vector: MutableList<Int> = mutableListOf()
    ): List<List<Int>> {
        val vectors = mutableListOf<List<Int>>()
        if (vector.size == parameterN) {
            if (vector.getNumOfOnes() == numOfPositiveBits)
                vectors.add(vector)

            return vectors
        }

        // First assign "0" at the start of vector
        // and try for all other permutations
        // for remaining positions
        var newVector = vector.toMutableList()
        newVector.add(0 , 0)
        vectors.addAll(getVectors(parameterN, numOfPositiveBits, newVector))

        // And then assign "1" at the start of vector
        // and try for all other permutations
        // for remaining positions
        if (vector.getNumOfOnes() < numOfPositiveBits) {
            newVector = vector.toMutableList()
            newVector.add(0, 1)
            vectors.addAll(getVectors(parameterN, numOfPositiveBits, newVector))
        }

        return vectors
    }
}
