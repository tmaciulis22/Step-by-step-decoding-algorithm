package ui

import controller.CodeController
import javafx.event.EventTarget
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.text.Font
import tornadofx.*

class GeneratorMatrixView : View() {

    private val codeController: CodeController by inject()

    override val root = borderpane {
        top = hbox(alignment = Pos.CENTER) {
            label("${codeController.parameterK}x${codeController.parameterN} matrix") {
                useMaxWidth = true
                font = Font(30.0)
            }
        }
        center = gridpane {
            subscribe<MatrixLoadedEvent> { event ->
                this@gridpane.clear()
                event.matrix.forEachIndexed { rowIndex, column ->
                    row {
                        column.forEachIndexed { colIndex, value ->
                            if (colIndex >= codeController.parameterK)
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
                    codeController.generateRandomRemainingMatrix()
                    fire(MatrixLoadedEvent(codeController.generatorMatrix))
                }
            }
            button("Continue") {
                action {
                    replaceWith(
                        ScenarioSelectorView::class,
                        ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
                    )
                }
            }
            padding = Insets(0.0, 0.0, 20.0, 0.0)
        }
    }

    override fun onDock() {
        super.onDock()
        fire(MatrixLoadedEvent(codeController.generatorMatrix))
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
                codeController.generatorMatrix[rowIndex][colIndex] = newIntValue
        }
    }

    private fun EventTarget.getNonEditableTextField(
        value: Int
    ) = textfield(value.toString()) {
        isEditable = false
    }
}

class MatrixLoadedEvent(val matrix: Array<Array<Int>>) : FXEvent()
