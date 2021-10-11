package com.coding.ui.firstScenario

import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.text.Font
import tornadofx.*
import com.coding.ui.general.ParametersView
import com.coding.ui.general.ScenarioSelectorView
import com.coding.util.nextView
import com.coding.viewModel.Decoder
import com.coding.viewModel.FirstScenarioViewModel

// GUI class for showing the decoded message vector.
// Original message vector and the encoded vector which was sent through channel are shown as well for comparison.
// User can choose to try other scenario or to input new parameters.
class DecodeView : View() {

    private val decoder: Decoder by inject()

    private val firstScenarioViewModel: FirstScenarioViewModel by inject()

    private val originalVector
        get() = firstScenarioViewModel.originalVector
    private val fromChannelVector
        get() = firstScenarioViewModel.fromChannelVector
    private val decodedVector
        get() = firstScenarioViewModel.decodedVector

    // For some weird bug of labels not updating its value these string properties are needed
    private val originalVectorString = SimpleStringProperty()
    private val fromChannelVectorString = SimpleStringProperty()

    override val root = scrollpane(fitToHeight = true, fitToWidth = true) {
        borderpane {
            padding = insets(10.0)
            top = stackpane {
                label("Decoding results") {
                    font = Font(26.0)
                }
            }
            center = vbox(alignment = Pos.CENTER_LEFT) {
                hbox {
                    label("Original vector: ") {
                        font = Font(20.0)
                    }
                    label(originalVectorString) {
                        font = Font(20.0)
                    }
                }
                hbox {
                    label("From channel vector: ") {
                        font = Font(20.0)
                    }
                    label(fromChannelVectorString) {
                        font = Font(20.0)
                    }
                }
                hbox {
                    label("Decoded vector: ") {
                        font = Font(20.0)
                    }
                    label(decodedVector) {
                        font = Font(20.0)
                    }
                }
            }
            bottom = hbox(spacing = 6, alignment = Pos.CENTER_RIGHT) {
                button("Other scenario") {
                    action {
                        nextView<ScenarioSelectorView>(transition = null)
                    }
                }
                button("New parameters") {
                    action {
                        nextView<ParametersView>(transition = null)
                    }
                }
            }
        }
    }

    override fun onDock() {
        super.onDock()
        originalVectorString.set(originalVector.value.toString())
        fromChannelVectorString.set(fromChannelVector.value.toString())
        decodedVector.set(decoder.decode(fromChannelVector.value))
    }
}
