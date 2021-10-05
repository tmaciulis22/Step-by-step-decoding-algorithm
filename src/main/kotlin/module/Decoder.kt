package module

import tornadofx.Controller

class Decoder : Controller() {

    // TODO might delete later if there is no use to save these parameters
    var parameterN: Int = 0
        private set
    var parameterK: Int = 0
        private set

    lateinit var controlMatrix: Array<Array<Int>>
        private set

    fun init(parameterN: Int, parameterK: Int, generatorMatrix: Array<Array<Int>>) {
        this.parameterN = parameterN
        this.parameterK = parameterK
        controlMatrix = Array(parameterN - parameterK) { Array(parameterN) { 0 } }
        setTransposeMatrix(generatorMatrix)
        setIdentityMatrix()
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
        var currentPos = parameterN - parameterK - 1
        controlMatrix.forEach { row ->
            row[currentPos] = 1
            currentPos++
        }
    }
}