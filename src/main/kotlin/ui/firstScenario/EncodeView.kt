package ui.firstScenario

import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import viewModel.Encoder
import javafx.scene.text.Font
import tornadofx.*
import ui.VectorChangeEvent
import util.textFieldBit
import util.nextView
import viewModel.FirstScenarioViewModel

// GUI class for inputting message vector of length K and encoding it.
class EncodeView : View() {

    private val encoder: Encoder by inject()

    private val firstScenarioViewModel: FirstScenarioViewModel by inject()

    private val encodedVector
        get() = firstScenarioViewModel.encodedVector
    private val originalVector
        get() = firstScenarioViewModel.originalVector

    private val headerString = SimpleStringProperty()

    override val root = scrollpane(fitToWidth = true, fitToHeight = true) {
        borderpane {
            padding = insets(10.0)
            top = stackpane {
                label(headerString) {
                    font = Font(20.0)
                }
            }
            center = vbox(alignment = Pos.CENTER_RIGHT, spacing = 8) {
                hbox(spacing = 100 / encoder.parameterK) {
                    subscribe<VectorChangeEvent> {
                        this@hbox.clear()
                        originalVector.value.forEachIndexed { index, value ->
                            textFieldBit(value) {
                                originalVector.value[index] = it
                            }
                        }
                    }
                }
                button("Encode") {
                    action {
                        encodedVector.set(encoder.encode(originalVector.value))
                    }
                }
            }
            bottom = vbox(alignment = Pos.CENTER_RIGHT) {
                separator {
                    padding = insets(top = 10.0)
                }
                stackpane {
                    label(encodedVector) {
                        font = Font(18.0)
                    }
                }
                button("Send") {
                    action {
                        nextView<ChannelView>()
                    }
                }
            }
        }
    }

    override fun onDock() {
        super.onDock()
        headerString.set("Enter k=${encoder.parameterK} length vector:")
        fire(VectorChangeEvent())
    }
}
