package ui.firstScenario

import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import viewModel.Encoder
import javafx.scene.text.Font
import tornadofx.*
import ui.VectorChangeEvent
import util.joinBitsToString
import util.textFieldBit
import util.nextView

class EncodeView : View() {

    private val encoder: Encoder by inject()

    private lateinit var encodedVector: Array<Int>
    private val encodedVectorProperty = SimpleStringProperty()
    private lateinit var originalVector: Array<Int>

    private val headerString = SimpleStringProperty()

    override val root = borderpane {
        padding = insets(10.0)
        top = stackpane {
            label(headerString) {
                font = Font(20.0)
            }
        }
        center = hbox(spacing = 100 / encoder.parameterK) {
            subscribe<VectorChangeEvent> {
                this@hbox.clear()
                originalVector.forEachIndexed { index, value ->
                    textFieldBit(value) {
                        originalVector[index] = it
                    }
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

    override fun onDock() {
        super.onDock()
        encodedVector = Array(encoder.parameterN) { 0 }
        encodedVectorProperty.set(encodedVector.joinBitsToString())
        originalVector = Array(encoder.parameterK) { 0 }
        headerString.set("Enter k=${encoder.parameterK} length vector:")
        fire(VectorChangeEvent())
    }
}
