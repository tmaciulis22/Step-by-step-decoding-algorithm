package ui.firstScenario

import javafx.beans.property.SimpleStringProperty
import javafx.event.EventTarget
import javafx.geometry.Pos
import module.Encoder
import javafx.scene.text.Font
import tornadofx.*

class FirstScenarioView : View() {

    private val encoder: Encoder by inject()
    private val vector = Array(encoder.parameterK) { 0 }
    private val encodedVectorProperty = SimpleStringProperty("--------")

    override val root = borderpane {
        padding = insets(10.0)
        top = stackpane {
            label("Enter k=${encoder.parameterK} length vector:") {
                font = Font(20.0)
            }
        }
        center = hbox(spacing = 100 / encoder.parameterK) {
            vector.forEachIndexed { index, value ->
                getEditableTextField(value, index)
            }
        }
        bottom = vbox(alignment = Pos.CENTER_RIGHT) {
            button("Encode") {
                action {
                    encoder.encode(vector)
                    encodedVectorProperty.set(encoder.encodedVector?.toList().toString())
                }
            }
            separator {
                padding = insets(top = 10.0)
            }
            stackpane {
                label(encodedVectorProperty) {
                    font = Font(18.0)
                }
            }
            button("Send") {
                action {

                }
            }
        }
    }

    private fun EventTarget.getEditableTextField(
        value: Int,
        index: Int
    ) = textfield(value.toString()) {
        textProperty().addListener { _, _, new ->
            if (new == "") return@addListener

            val newIntValue = new.toInt()
            if (newIntValue == 0 || newIntValue == 1)
                vector[index] = newIntValue
        }
    }
}
