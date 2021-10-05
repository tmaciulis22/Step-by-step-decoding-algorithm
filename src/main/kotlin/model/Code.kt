package model

import kotlin.random.Random

class Code(
    val parameterN: Int,
    val parameterK: Int,
    val generatorMatrix: Array<Array<Int>>
) {
    fun setIdentityMatrix() {
        // zymeklis, kuris nurodo kur bus irasomas 1
        var currentPos = 0
        generatorMatrix.forEach { column ->
            column[currentPos] = 1
            currentPos++
        }
    }

    fun generateRandomRemainingMatrix() {
        generatorMatrix.forEachIndexed { rowIndex, column ->
            column.forEachIndexed { colIndex, _ ->
                if (colIndex >= parameterK)
                    generatorMatrix[rowIndex][colIndex] = Random.nextInt(0, 2)
            }
        }
    }
}
