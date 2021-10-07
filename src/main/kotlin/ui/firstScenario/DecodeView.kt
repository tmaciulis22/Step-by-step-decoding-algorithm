package ui.firstScenario

import javafx.geometry.Pos
import javafx.scene.text.Font
import tornadofx.*
import ui.ParametersView
import ui.ScenarioSelectorView
import util.joinBitsToString
import util.nextView
import viewModel.Decoder

class DecodeView : View() {

    private val decoder: Decoder by inject()

    private val originalVector: Array<Int> by param()
    private val vectorFromChannel: Array<Int> by param()

    private val decodedVector: Array<Int>? = decoder.decode(vectorFromChannel)

    override val root = borderpane {
        padding = insets(10.0)
        top = vbox(alignment = Pos.CENTER) {
            label("Decoding results") {
                font = Font(24.0)
            }
        }
        center = vbox(alignment = Pos.CENTER_LEFT) {
            label("Original vector: ${originalVector.joinBitsToString()}") {
                font = Font(18.0)
            }
            label("Decoded vector: ${decodedVector?.joinBitsToString() ?: "Failed to decode"}") {
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

    companion object {
        const val PARAM_ORIGINAL_VECTOR = "originalVector"
        const val PARAM_VECTOR_FROM_CHANNEL = "vectorFromChannel"
    }
}
