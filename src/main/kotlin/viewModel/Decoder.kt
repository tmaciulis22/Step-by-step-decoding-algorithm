package viewModel

import tornadofx.ViewModel
import util.getNumOfOnes
import util.multiplyTransposed
import kotlin.math.pow

class Decoder : ViewModel() {

    private lateinit var controlMatrix: MutableList<MutableList<Int>>

    private var parameterK: Int = 0

    // Map(Table) consisting of syndromes and corresponding class leader weights
    private lateinit var syndromesAndWeightsMap: Map<String, Int>

    fun init(parameterN: Int, parameterK: Int, generatorMatrix: List<List<Int>>) {
        this.parameterK = parameterK
        controlMatrix = MutableList(parameterN - parameterK) { MutableList(parameterN) { 0 } }
        setTransposeMatrix(generatorMatrix)
        setIdentityMatrix()
        findSyndromesAndWeights(parameterN)
    }

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
        val syndromes = MutableList(numOfClasses) { List(parameterN - parameterK ) { 0 } }
        val classLeadersWeights = MutableList(numOfClasses) { 0 }

        var numOfClassesFound = 1
        var numOfPositiveBits = 1
        while (numOfClassesFound < numOfClasses) {
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
            val syndromeString = syndrome.toString()
            table[syndromeString] = classLeadersWeights[index]
        }
        syndromesAndWeightsMap = table
    }

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
