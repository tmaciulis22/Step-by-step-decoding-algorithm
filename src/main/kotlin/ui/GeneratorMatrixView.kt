package ui

import javafx.beans.property.SimpleStringProperty
import viewModel.Encoder
import javafx.geometry.Pos
import javafx.scene.text.Font
import viewModel.Decoder
import tornadofx.*
import util.nextView
import util.textFieldCell

class GeneratorMatrixView : View() {

    private val encoder: Encoder by inject()
    private val decoder: Decoder by inject()

    private val headerString = SimpleStringProperty()

    override val root = borderpane {
        top = hbox(alignment = Pos.CENTER) {
            label(headerString) {
                useMaxWidth = true
                font = Font(30.0)
            }
        }
        center = gridpane {
            subscribe<MatrixChangedEvent> {
                this@gridpane.clear()
                encoder.generatorMatrix.forEachIndexed { rowIndex, row ->
                    row {
                        row.forEachIndexed { colIndex, value ->
                            if (colIndex >= encoder.parameterK) {
                                textFieldCell(value) {
                                    if (it == "") return@textFieldCell

                                    val newIntValue = it.toInt()
                                    if (newIntValue == 0 || newIntValue == 1)
                                        encoder.generatorMatrix[rowIndex][colIndex] = newIntValue
                                }
                            } else {
                                textFieldCell(value, isEditable = false)
                            }
                        }
                    }
                }
            }
        }
        bottom = hbox(10, alignment = Pos.CENTER) {
            button("Random") {
                action {
                    encoder.generateRandomRemainingMatrix()
                    fire(MatrixChangedEvent())
                }
            }
            button("Continue") {
                action {
                    decoder.init(encoder.parameterN, encoder.parameterK, encoder.generatorMatrix)
                    nextView<ScenarioSelectorView>()
                }
            }
            padding = insets(bottom = 20.0)
        }
    }

    override fun onDock() {
        super.onDock()
        headerString.set("${encoder.parameterK}x${encoder.parameterN} matrix")
        fire(MatrixChangedEvent())
    }
}
