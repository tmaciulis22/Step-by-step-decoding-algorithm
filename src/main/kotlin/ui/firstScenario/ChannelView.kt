package ui.firstScenario

import javafx.event.EventTarget
import javafx.geometry.Pos
import javafx.scene.text.Font
import viewModel.Channel
import tornadofx.*
import ui.VectorChangeEvent
import util.nextView
import util.textFieldBit
import viewModel.FirstScenarioViewModel

class ChannelView : View() {

    private val channel: Channel by inject()

    private val firstScenarioViewModel: FirstScenarioViewModel by inject()

    private val encodedVector
        get() = firstScenarioViewModel.encodedVector
    private val fromChannelVector
        get() = firstScenarioViewModel.fromChannelVector
    private val mistakesVector
        get() = firstScenarioViewModel.mistakesVector

    override val root = scrollpane(fitToWidth = true, fitToHeight = true) {
        borderpane {
            padding = insets(10.0)
            top = vbox(alignment = Pos.CENTER) {
                label("Vector from channel") {
                    font = Font(20.0)
                }
            }
            center = hbox {
                subscribe<VectorChangeEvent> {
                    this@hbox.clear()
                    fromChannelVector.value.forEachIndexed { index, value ->
                        getBitAndMistakeCell(value, mistakesVector.value[index], index)
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
                            nextView<DecodeView>()
                        }
                    }
                }
            }
        }
    }

    override fun onDock() {
        super.onDock()
        val vectorAndMistakes = channel.send(encodedVector.value.toMutableList())
        fromChannelVector.set(vectorAndMistakes?.first?.toMutableList())
        mistakesVector.set(vectorAndMistakes?.second?.toMutableList())
        fire(VectorChangeEvent())
    }

    private fun EventTarget.getBitAndMistakeCell(
        bit: Int,
        isMistake: Boolean,
        index: Int
    ) = vbox(alignment = Pos.CENTER) {
        textFieldBit(bit) {
            fromChannelVector.value[index] = it
            mistakesVector.value[index] = !mistakesVector.value[index]
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
}
