package module

import tornadofx.Controller
import util.multiply
import kotlin.random.Random

class Encoder : Controller() {

    var parameterN: Int = 0
        private set
    var parameterK: Int = 0
        private set

    lateinit var generatorMatrix: Array<Array<Int>>
        private set

    var originalVector: Array<Int>? = null
        private set
    var encodedVector: Array<Int>? = null
        private set

    fun init(parameterN: Int, parameterK: Int) {
        // Inicializuojame kodo struktura, generuojancia matrica inicializuojame nuliais, veliau ja uzpildysime reikiamomis reiksmemis
        this.parameterN = parameterN
        this.parameterK = parameterK

        generatorMatrix = Array(parameterK) { Array(parameterN) { 0 } }
        // generuojancioje matricoje nustatom vienetine matrica (irasomi 1 reikiamose pozicijose)
        setIdentityMatrix()
    }

    fun generateRandomRemainingMatrix() {
        generatorMatrix.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, _ ->
                if (colIndex >= parameterK)
                    generatorMatrix[rowIndex][colIndex] = Random.nextInt(0, 2)
            }
        }
    }

    fun encode(vector: Array<Int>) {
        if (vector.size > parameterK) return

        originalVector = vector.copyOf()
        encodedVector = generatorMatrix.multiply(vector) ?: Array(parameterN) { 0 }
    }

    private fun setIdentityMatrix() {
        // zymeklis, kuris nurodo kur bus irasomas 1
        var currentPos = 0
        generatorMatrix.forEach { row ->
            row[currentPos] = 1
            currentPos++
        }
    }
}
