package ui.secondScenario

import App.Companion.WINDOW_HEIGHT
import App.Companion.WINDOW_WIDTH
import javafx.geometry.Pos
import tornadofx.*
import viewModel.SecondScenarioViewModel
import javafx.scene.text.Font
import ui.general.ParametersView
import ui.general.ScenarioSelectorView
import util.nextView

// GUI class which shows the text processing (encoding, decoding, etc.) results.
// Original text, the text which was sent through channel without coding, and also encoded/decoded text are shown as well for comparison.
// User can choose to try other scenario or to input new parameters.
class TextProcessingResultsView : View() {

    private val secondScenarioViewModel: SecondScenarioViewModel by inject()

    private val originalText
        get() = secondScenarioViewModel.originalData
    private val notCodedProcessedText
        get() = secondScenarioViewModel.notCodedProcessedData
    private val codedProcessedText
        get() = secondScenarioViewModel.codedProcessedData

    override val root = borderpane {
        padding = insets(10)
        center = hbox(spacing = 12, alignment = Pos.CENTER) {
            vbox(spacing = 2, alignment = Pos.CENTER) {
                label("Original text:") {
                    font = Font(20.0)
                }
                scrollpane {
                    text(originalText) {
                        this.wrappingWidth = WINDOW_WIDTH * 0.5
                        font = Font(18.0)
                    }
                }
            }
            vbox(spacing = 2, alignment = Pos.CENTER) {
                label("Not encoded/decoded text:") {
                    font = Font(20.0)
                }
                scrollpane {
                    text(notCodedProcessedText) {
                        this.wrappingWidth = WINDOW_WIDTH * 0.5
                        font = Font(18.0)
                    }
                }
            }
            vbox(spacing = 2, alignment = Pos.CENTER) {
                label("Encoded/decoded text:") {
                    font = Font(20.0)
                }
                scrollpane {
                    text(codedProcessedText) {
                        this.wrappingWidth = WINDOW_WIDTH * 0.5
                        font = Font(18.0)
                    }
                }
            }
        }
        bottom = hbox(spacing = 4, alignment = Pos.CENTER_RIGHT) {
            padding = insets(top = 6)
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
        currentWindow?.width = WINDOW_WIDTH * 1.75
    }

    override fun onUndock() {
        super.onUndock()
        currentWindow?.width = WINDOW_WIDTH
        currentWindow?.height = WINDOW_HEIGHT
    }
}
