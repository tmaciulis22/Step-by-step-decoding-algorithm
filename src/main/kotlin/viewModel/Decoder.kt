package viewModel

import tornadofx.ViewModel
import util.getNumOfOnes
import util.includes
import util.multiplyTransposed
import kotlin.math.pow

class Decoder : ViewModel() {

    lateinit var controlMatrix: Array<Array<Int>>
        private set
    lateinit var syndromes: Array<Array<Int>>
        private set
    lateinit var classLeadersWeights: Array<Int>
        private set

    fun init(parameterN: Int, parameterK: Int, generatorMatrix: Array<Array<Int>>) {
        controlMatrix = Array(parameterN - parameterK) { Array(parameterN) { 0 } }
        setTransposeMatrix(parameterK, generatorMatrix)
        setIdentityMatrix(parameterK)
        findSyndromesAndWeights(parameterN, parameterK)
    }

    private fun setTransposeMatrix(parameterK: Int, generatorMatrix: Array<Array<Int>>) {
        val generatorMatrixWithoutIdentity = generatorMatrix.map {
            it.drop(parameterK)
        }
        generatorMatrixWithoutIdentity.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, _ ->
                controlMatrix[colIndex][rowIndex] = generatorMatrixWithoutIdentity[rowIndex][colIndex]
            }
        }
    }

    private fun setIdentityMatrix(parameterK: Int) {
        // zymeklis, kuris nurodo kur bus irasomas 1
        var currentPos = parameterK
        controlMatrix.forEach { row ->
            row[currentPos] = 1
            currentPos++
        }
    }

    private fun findSyndromesAndWeights(parameterN: Int, parameterK: Int) {
        // Kiek is viso klasiu yra, apskaiciuojama pagal formule is konspekto
        val numOfClasses = 2.0.pow((parameterN - parameterK).toDouble()).toInt()
        syndromes = Array(numOfClasses) { Array(parameterN - parameterK ) { 0 } }
        classLeadersWeights = Array(numOfClasses) { 0 }

        var numOfClassesFound = 1
        var numOfPositiveBits = 1
        while (numOfClassesFound < numOfClasses) {
            val possibleClassLeaders = getVectors(parameterN, numOfPositiveBits)
            possibleClassLeaders.forEach { vector ->
                if (numOfClassesFound >= numOfClasses) return@forEach

                val syndrome = controlMatrix.multiplyTransposed(vector)
                if (syndrome != null && !syndromes.includes(syndrome)) {
                    syndromes[numOfClassesFound] = syndrome
                    classLeadersWeights[numOfClassesFound] = vector.toList().getNumOfOnes()
                    numOfClassesFound++
                }
            }
            numOfPositiveBits++
        }
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
