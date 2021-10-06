package ui.firstScenario

import javafx.beans.property.SimpleObjectProperty
import javafx.event.EventTarget
import javafx.geometry.Pos
import javafx.scene.text.Font
import module.Channel
import module.Encoder
import tornadofx.*
import util.textFieldCell

class ChannelView : View() {

    private val encoder: Encoder by inject()
    private val channel: Channel by inject()
    private val vectorProperty = SimpleObjectProperty<Array<Int>>()
    private val mistakesProperty = SimpleObjectProperty<Array<Boolean>>()

    init {
        val vectorAndMistakes = channel.send(encoder.encodedVector)
        vectorProperty.set(vectorAndMistakes?.first)
        mistakesProperty.set(vectorAndMistakes?.second)
    }

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
                        TODO()
                    }
                }
            }
        }
    }

    override fun onDock() {
        super.onDock()
        fire(VectorChangeEvent())
    }

    private fun EventTarget.getBitAndMistakeCell(
        bit: Int,
        isMistake: Boolean,
        index: Int
    ) = vbox(alignment = Pos.CENTER) {
        textFieldCell(bit) {
            if (it == "") return@textFieldCell

            val newIntValue = it.toInt()
            if (newIntValue == 0 || newIntValue == 1) {
                vectorProperty.value[index] = newIntValue
                mistakesProperty.value[index] = !mistakesProperty.value[index]
                fire(VectorChangeEvent())
            }
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

class VectorChangeEvent : FXEvent()
