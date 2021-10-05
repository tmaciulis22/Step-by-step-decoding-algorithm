package controller

import model.Code
import tornadofx.Controller

class CodeController : Controller() {
    private lateinit var code: Code

    val parameterN
        get() = code.parameterN
    val parameterK
        get() = code.parameterK
    val generatorMatrix
        get() = code.generatorMatrix

    fun setCodeParameters(parameterN: Int, parameterK: Int) {
        if (!::code.isInitialized) {
            // Inicializuojame kodo struktura, generuojancia matrica inicializuojame nuliais, veliau ja uzpildysime reikiamomis reiksmemis
            code = Code(parameterN, parameterK, Array(parameterK) { Array(parameterN) { 0 } })
            // generuojancioje matricoje nustatom vienetine matrica (irasomi 1 reikiamose pozicijose)
            code.setIdentityMatrix()
        }
    }

    fun generateRandomRemainingMatrix() = code.generateRandomRemainingMatrix()
}
