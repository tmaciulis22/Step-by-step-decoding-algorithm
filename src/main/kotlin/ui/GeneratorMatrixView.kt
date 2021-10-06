package ui

import module.Encoder
import javafx.event.EventTarget
import javafx.geometry.Pos
import javafx.scene.text.Font
import module.Decoder
import tornadofx.*

class GeneratorMatrixView : View() {

    private val encoder: Encoder by inject()
    private val decoder: Decoder by inject()

    override val root = borderpane {
        top = hbox(alignment = Pos.CENTER) {
            label("${encoder.parameterK}x${encoder.parameterN} matrix") {
                useMaxWidth = true
                font = Font(30.0)
            }
        }
        center = gridpane {
            subscribe<MatrixLoadedEvent> { event ->
                this@gridpane.clear()
                event.matrix.forEachIndexed { rowIndex, row ->
                    row {
                        row.forEachIndexed { colIndex, value ->
                            if (colIndex >= encoder.parameterK)
                                getEditableTextField(value, rowIndex, colIndex)
                            else
                                getNonEditableTextField(value)
                        }
                    }
                }
            }
        }
        bottom = hbox(10, alignment = Pos.CENTER) {
            button("Random") {
                action {
                    encoder.generateRandomRemainingMatrix()
                    fire(MatrixLoadedEvent(encoder.generatorMatrix))
                }
            }
            button("Continue") {
                action {
                    decoder.init(encoder.parameterN, encoder.parameterK, encoder.generatorMatrix)
                    replaceWith(
                        ScenarioSelectorView::class,
                        ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
                    )
                }
            }
            padding = insets(bottom = 20.0)
        }
    }

    override fun onDock() {
        super.onDock()
        fire(MatrixLoadedEvent(encoder.generatorMatrix))
    }

    private fun EventTarget.getEditableTextField(
        value: Int,
        rowIndex: Int,
        colIndex: Int
    ) = textfield(value.toString()) {
        textProperty().addListener { _, _, new ->
            if (new == "") return@addListener

            val newIntValue = new.toInt()
            if (newIntValue == 0 || newIntValue == 1)
                encoder.generatorMatrix[rowIndex][colIndex] = newIntValue
        }
    }

    private fun EventTarget.getNonEditableTextField(
        value: Int
    ) = textfield(value.toString()) {
        isEditable = false
    }
}

class MatrixLoadedEvent(val matrix: Array<Array<Int>>) : FXEvent()
