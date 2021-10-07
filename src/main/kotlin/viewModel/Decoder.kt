package viewModel

import tornadofx.ViewModel
import util.getNumOfOnes
import util.includes
import util.joinBitsToString
import util.multiplyTransposed
import kotlin.math.pow

class Decoder : ViewModel() {

    private lateinit var controlMatrix: Array<Array<Int>>

    private var parameterK: Int = 0

    // Map(Table) consisting of syndromes and corresponding class leader weights
    private lateinit var syndromesAndWeightsMap: Map<String, Int>

    fun init(parameterN: Int, parameterK: Int, generatorMatrix: Array<Array<Int>>) {
        this.parameterK = parameterK
        controlMatrix = Array(parameterN - parameterK) { Array(parameterN) { 0 } }
        setTransposeMatrix(generatorMatrix)
        setIdentityMatrix()
        findSyndromesAndWeights(parameterN)
    }

    fun decode(vector: Array<Int>): Array<Int>? {
        for (index in vector.indices) {
            val syndromeString = controlMatrix.multiplyTransposed(vector).joinBitsToString()
            val weight = syndromesAndWeightsMap[syndromeString] ?: return null

            if (weight == 0) break

            vector[index] += 1
            if (vector[index] > 1) vector[index] = 0

            val newSyndromeString = controlMatrix.multiplyTransposed(vector).joinBitsToString()
            val newWeight = syndromesAndWeightsMap[newSyndromeString] ?: return null

            if (newWeight >= weight) {
                vector[index] -= 1
                if (vector[index] < 0) vector[index] = 1
            }
        }

        return vector.copyOfRange(0, parameterK)
    }

    private fun setTransposeMatrix(generatorMatrix: Array<Array<Int>>) {
        val generatorMatrixWithoutIdentity = generatorMatrix.map {
            it.drop(parameterK)
        }
        generatorMatrixWithoutIdentity.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, _ ->
                controlMatrix[colIndex][rowIndex] = generatorMatrixWithoutIdentity[rowIndex][colIndex]
            }
        }
    }

    private fun setIdentityMatrix() {
        // zymeklis, kuris nurodo kur bus irasomas 1
        var currentPos = parameterK
        controlMatrix.forEach { row ->
            row[currentPos] = 1
            currentPos++
        }
    }

    private fun findSyndromesAndWeights(parameterN: Int) {
        // Kiek is viso klasiu yra, apskaiciuojama pagal formule is konspekto
        val numOfClasses = 2.0.pow((parameterN - parameterK).toDouble()).toInt()
        val syndromes = Array(numOfClasses) { Array(parameterN - parameterK ) { 0 } }
        val classLeadersWeights = Array(numOfClasses) { 0 }

        var numOfClassesFound = 1
        var numOfPositiveBits = 1
        while (numOfClassesFound < numOfClasses) {
            val possibleClassLeaders = getVectors(parameterN, numOfPositiveBits)
            possibleClassLeaders.forEach { vector ->
                if (numOfClassesFound >= numOfClasses) return@forEach

                val syndrome = controlMatrix.multiplyTransposed(vector)
                if (!syndromes.includes(syndrome)) {
                    syndromes[numOfClassesFound] = syndrome
                    classLeadersWeights[numOfClassesFound] = vector.toList().getNumOfOnes()
                    numOfClassesFound++
                }
            }
            numOfPositiveBits++
        }

        val table = mutableMapOf<String, Int>()
        syndromes.forEachIndexed{ index, syndrome ->
            val syndromeString = syndrome.joinBitsToString()
            table[syndromeString] = classLeadersWeights[index]
        }
        syndromesAndWeightsMap = table
    }

    private fun getVectors(
        parameterN: Int,
        numOfPositiveBits: Int = 1,
        vector: MutableList<Int> = mutableListOf()
    ): List<Array<Int>> {
        val vectors = mutableListOf<Array<Int>>()
        if (vector.size == parameterN) {
            if (vector.getNumOfOnes() == numOfPositiveBits)
                vectors.add(vector.toTypedArray())

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
