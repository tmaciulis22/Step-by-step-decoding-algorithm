package ui.firstScenario

import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.text.Font
import tornadofx.*
import ui.general.ParametersView
import ui.general.ScenarioSelectorView
import util.joinBitsToString
import util.nextView
import viewModel.Decoder

class DecodeView : View() {

    private val decoder: Decoder by inject()

    private val originalVector: Array<Int> by param()
    private val vectorFromChannel: Array<Int> by param()

    private var decodedVector: Array<Int>? = null

    private val originalVectorString = SimpleStringProperty()
    private val decodedVectorString = SimpleStringProperty()

    override val root = borderpane {
        padding = insets(10.0)
        top = vbox(alignment = Pos.CENTER) {
            label("Decoding results") {
                font = Font(24.0)
            }
        }
        center = vbox(alignment = Pos.CENTER_LEFT) {
            label(originalVectorString) {
                font = Font(18.0)
            }
            label(decodedVectorString) {
                font = Font(18.0)
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
        decodedVector = decoder.decode(vectorFromChannel)
        originalVectorString.set("Original vector: ${originalVector.joinBitsToString()}")
        decodedVectorString.set("Decoded Vector: ${decodedVector?.joinBitsToString() ?: "Failed to decode"}")
    }

    companion object {
        const val PARAM_ORIGINAL_VECTOR = "originalVector"
        const val PARAM_VECTOR_FROM_CHANNEL = "vectorFromChannel"
    }
}
