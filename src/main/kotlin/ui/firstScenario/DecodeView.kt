package ui.firstScenario

import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.text.Font
import tornadofx.*
import ui.general.ParametersView
import ui.general.ScenarioSelectorView
import util.nextView
import viewModel.Decoder
import viewModel.FirstScenarioViewModel

class DecodeView : View() {

    private val decoder: Decoder by inject()

    private val firstScenarioViewModel: FirstScenarioViewModel by inject()

    private val originalVector
        get() = firstScenarioViewModel.originalVector
    private val fromChannelVector
        get() = firstScenarioViewModel.fromChannelVector
    private val decodedVector
        get() = firstScenarioViewModel.decodedVector

    private val originalVectorString = SimpleStringProperty()
    private val fromChannelVectorString = SimpleStringProperty()

    override val root = borderpane {
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

    override fun onDock() {
        super.onDock()
        originalVectorString.set(originalVector.value.toString())
        fromChannelVectorString.set(fromChannelVector.value.toString())
        decodedVector.set(decoder.decode(fromChannelVector.value))
    }
}
