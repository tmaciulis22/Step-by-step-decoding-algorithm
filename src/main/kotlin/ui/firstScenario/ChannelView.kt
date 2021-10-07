package ui.firstScenario

import javafx.beans.property.SimpleObjectProperty
import javafx.event.EventTarget
import javafx.geometry.Pos
import javafx.scene.text.Font
import viewModel.Channel
import tornadofx.*
import ui.VectorChangeEvent
import util.nextView
import util.textFieldBit

class ChannelView : View() {

    private val channel: Channel by inject()
    private val vectorProperty = SimpleObjectProperty<Array<Int>>()
    private val mistakesProperty = SimpleObjectProperty<Array<Boolean>>()

    private val originalVector: Array<Int> by param()
    private val encodedVector: Array<Int> by param()

    override val root = borderpane {
        padding = insets(10.0)
        top = vbox(alignment = Pos.CENTER) {
            label("Vector from channel") {
                font = Font(20.0)
            }
        }
        center = hbox {
            subscribe<VectorChangeEvent> {
                this@hbox.clear()
                vectorProperty.value.forEachIndexed { index, value ->
                    getBitAndMistakeCell(value, mistakesProperty.value[index], index)
                }
            }
        }
        bottom = vbox {
            label("X - mistake")
            label("_ - no mistake")
            vbox(alignment = Pos.CENTER_RIGHT) {
                useMaxWidth = true
                button("Decode") {
                    action {
                        nextView<DecodeView>(
                            params = mapOf(
                                DecodeView.PARAM_ORIGINAL_VECTOR to originalVector.copyOf(),
                                DecodeView.PARAM_VECTOR_FROM_CHANNEL to vectorProperty.value.copyOf()
                            )
                        )
                    }
                }
            }
        }
    }

    override fun onDock() {
        super.onDock()
        val vectorAndMistakes = channel.send(encodedVector)
        vectorProperty.set(vectorAndMistakes?.first)
        mistakesProperty.set(vectorAndMistakes?.second)
        fire(VectorChangeEvent())
    }

    private fun EventTarget.getBitAndMistakeCell(
        bit: Int,
        isMistake: Boolean,
        index: Int
    ) = vbox(alignment = Pos.CENTER) {
        textFieldBit(bit) {
            vectorProperty.value[index] = it
            mistakesProperty.value[index] = !mistakesProperty.value[index]
            fire(VectorChangeEvent())
        }
        getMistakeCell(isMistake)
    }

    private fun EventTarget.getMistakeCell(
        isMistake: Boolean
    ) = if (isMistake)
        text("X")
    else
        text("_")

    companion object {
        const val PARAM_ORIGINAL_VECTOR = "originalVector"
        const val PARAM_ENCODED_VECTOR = "encodedVector"
    }
}
