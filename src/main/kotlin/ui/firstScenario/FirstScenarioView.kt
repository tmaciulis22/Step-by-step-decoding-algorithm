package ui.firstScenario

import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import viewModel.Encoder
import javafx.scene.text.Font
import tornadofx.*
import util.joinBitsToString
import util.textFieldCell
import util.nextView

class FirstScenarioView : View() {

    private val encoder: Encoder by inject()

    private var encodedVector = Array(encoder.parameterN) { 0 }
    private val encodedVectorProperty = SimpleStringProperty(encodedVector.joinBitsToString())
    private val originalVector = Array(encoder.parameterK) { 0 }

    override val root = borderpane {
        padding = insets(10.0)
        top = stackpane {
            label("Enter k=${encoder.parameterK} length vector:") {
                font = Font(20.0)
            }
        }
        center = hbox(spacing = 100 / encoder.parameterK) {
            originalVector.forEachIndexed { index, value ->
                textFieldCell(value) {
                    if (it == "") return@textFieldCell

                    val newIntValue = it.toInt()
                    if (newIntValue == 0 || newIntValue == 1)
                        originalVector[index] = newIntValue
                }
            }
        }
        bottom = vbox(alignment = Pos.CENTER_RIGHT) {
            button("Encode") {
                action {
                    encodedVector = encoder.encode(originalVector)
                    encodedVectorProperty.set(encodedVector.joinBitsToString())
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
                    nextView<ChannelView>(
                        params = mapOf(
                            ChannelView.PARAM_ORIGINAL_VECTOR to originalVector.copyOf(),
                            ChannelView.PARAM_ENCODED_VECTOR to encodedVector.copyOf()
                        )
                    )
                }
            }
        }
    }
}
