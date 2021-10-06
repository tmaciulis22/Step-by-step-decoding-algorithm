package ui

import module.Channel
import module.Encoder
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Pos
import tornadofx.*

class ParametersView: View() {

    private val encoder: Encoder by inject()
    private val channel: Channel by inject()

    private val parameterN = SimpleIntegerProperty()
    private val parameterK = SimpleIntegerProperty()
    private val parameterPe = SimpleDoubleProperty()

    private val allParametersEntered: Boolean
        get() = parameterN.value > 0 && parameterK.value > 0 && parameterPe.value > 0

    override val root = form {
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
        hbox(alignment = Pos.CENTER_RIGHT) {
            button("Continue") {
                action {
                    if (allParametersEntered) {
                        encoder.init(parameterN.value, parameterK.value)
                        channel.init(parameterPe.value)
                        replaceWith(
                            GeneratorMatrixView::class,
                            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
                        )
                    }
                }
            }
        }
    }
}
