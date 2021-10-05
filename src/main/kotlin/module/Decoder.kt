package module

import tornadofx.Controller
import util.getNumOfOnes
import util.includes
import util.multiply
import kotlin.math.pow

class Decoder : Controller() {

    lateinit var controlMatrix: Array<Array<Int>>
        private set
    lateinit var syndromes: Array<Array<Int>>
        private set
    lateinit var classLeadersWeights: Array<Int>
        private set

    fun init(parameterN: Int, parameterK: Int, generatorMatrix: Array<Array<Int>>) {
        controlMatrix = Array(parameterN - parameterK) { Array(parameterN) { 0 } }
        setTransposeMatrix(parameterK, generatorMatrix)
        setIdentityMatrix(parameterN, parameterK)
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

    private fun setIdentityMatrix(parameterN: Int, parameterK: Int) {
        // zymeklis, kuris nurodo kur bus irasomas 1
        var currentPos = parameterN - parameterK - 1
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

        // Kiek klasiu lyderiu jau radom. Viena lyderi jau turim, tai 00..00 vektorius
//        var classLeadersFound = 1
//        val zeroVector = Array(parameterN) { 0 }

        findSyndromesAndWeightsRec(
            Array(parameterN) { 0 },
            0,
            0,
            1
        )

        val asd = 0

//        while (classLeadersFound < numOfClasses) {
//            val possibleLeader = findSyndromesAndWeightsRec(
//                parameterN, zeroVector, 0)
//            classLeadersFound++
//        }
    }

    private fun findSyndromesAndWeightsRec(
        vector: Array<Int>,
        posOfLastPositiveBit: Int = -1,
        position: Int = 0,
        numOfClassesFound: Int = 1
    ) {
        vector[position] = 1
        val syndrome = controlMatrix.multiply(vector) ?: return
        val weight = vector.getNumOfOnes()

        var syndromeAdded = false
        if (!syndromes.includes(syndrome)) {
            syndromes[numOfClassesFound] = syndrome
            classLeadersWeights[numOfClassesFound] = weight
            syndromeAdded = true
        }

        if (numOfClassesFound >= classLeadersWeights.size) return

        val isLastPosition = position >= vector.size - 1
        if (!isLastPosition)
            vector[position] = 0
        else
            vector[posOfLastPositiveBit + 1] = 1 // TODO cia crashina

        findSyndromesAndWeightsRec(
            vector,
            posOfLastPositiveBit + if (isLastPosition) 1 else 0,
            if (isLastPosition) posOfLastPositiveBit + 1 else position + 1,
            numOfClassesFound + if (syndromeAdded) 1 else 0
        )
    }
}
