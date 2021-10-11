package com.coding.ui.general

import com.coding.ui.experiment.ExperimentProcessingView
import com.coding.viewModel.Channel
import com.coding.viewModel.Encoder
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import tornadofx.*
import com.coding.util.nextView

// GUI class for inputting code parameters N and K, as well as channel parameter Pe.
// Once Continue button is pressed the parameters' validity is checked and encoder with channel are initialized
class ParametersView: View() {

    private val encoder: Encoder by inject()
    private val channel: Channel by inject()

    private val parameterN = SimpleIntegerProperty()
    private val parameterK = SimpleIntegerProperty()
    private val parameterPe = SimpleDoubleProperty()

    override val root = form {
        padding = insets(24)
        fieldset("Enter parameters:") {
            field("Code length N") {
                textfield(parameterN)
            }
            field("Code dimension K") {
                textfield(parameterK)
            }
            field("Channel error probability Pe") {
                textfield(parameterPe)
            }
        }
        hbox(spacing = 12, alignment = Pos.BOTTOM_RIGHT) {
            vgrow = Priority.ALWAYS
            button("Experiment") {
                action {
                    nextView<ExperimentProcessingView>()
                }
            }
            button("Continue") {
                action {
                    val allParametersEntered = parameterN.value > 0 && parameterK.value > 0 && parameterPe.value > 0
                    val isProbabilityValid = parameterPe.value in 0.0..1.0
                    if (allParametersEntered && parameterN.value > parameterK.value && isProbabilityValid) {
                        encoder.init(parameterN.value, parameterK.value)
                        channel.init(parameterPe.value)
                        nextView<GeneratorMatrixView>()
                    }
                }
            }
        }
    }
}
