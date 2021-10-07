package ui.general

import javafx.beans.property.SimpleStringProperty
import viewModel.Encoder
import javafx.geometry.Pos
import javafx.scene.text.Font
import viewModel.Decoder
import tornadofx.*
import ui.MatrixChangedEvent
import util.nextView
import util.textFieldBit

class GeneratorMatrixView : View() {

    private val encoder: Encoder by inject()
    private val decoder: Decoder by inject()

    private val headerString = SimpleStringProperty()

    override val root = scrollpane(fitToWidth = true, fitToHeight = true) {
        borderpane {
            top = stackpane {
                label(headerString) {
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
                                    textFieldBit(value) {
                                        encoder.generatorMatrix[rowIndex][colIndex] = it.toInt()
                                    }
                                } else {
                                    textFieldBit(value, isEditable = false)
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
    }

    override fun onDock() {
        super.onDock()
        headerString.set("${encoder.parameterK}x${encoder.parameterN} matrix")
        fire(MatrixChangedEvent())
    }
}
