package com.coding.viewModel

import javafx.beans.property.SimpleObjectProperty
import tornadofx.ViewModel

class FirstScenarioViewModel : ViewModel() {

    // Initial message vector of length K
    val originalVector = SimpleObjectProperty<MutableList<Int>>()
    // Encoded message vector of length N before sent to channel
    val encodedVector = SimpleObjectProperty<List<Int>>()
    // Encoded message vector after being sent to channel
    val fromChannelVector = SimpleObjectProperty<MutableList<Int>>()
    // Mistakes vector which shows where mistakes were made in fromChannelVector
    val mistakesVector = SimpleObjectProperty<MutableList<Boolean>>()
    // Decoded vector (it was decoded from fromChannelVector)
    val decodedVector = SimpleObjectProperty<List<Int>>()

    // Initializes the view model with parameters N and K
    fun init(parameterN: Int, parameterK: Int) {
        originalVector.set(MutableList(parameterK) { 0 })
        encodedVector.set(List(parameterN) { 0 })
        fromChannelVector.set(MutableList(parameterN) { 0 })
        mistakesVector.set(MutableList(parameterN) { false })
        decodedVector.set(List(parameterK) { 0 })
    }
}
