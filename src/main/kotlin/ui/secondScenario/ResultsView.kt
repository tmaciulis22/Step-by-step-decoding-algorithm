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

class ResultsView : View() {

    private val secondScenarioViewModel: SecondScenarioViewModel by inject()

    private val originalText
        get() = secondScenarioViewModel.originalText
    private val notCodedProcessedText
        get() = secondScenarioViewModel.notCodedProcessedText
    private val codedProcessedText
        get() = secondScenarioViewModel.codedProcessedText

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
