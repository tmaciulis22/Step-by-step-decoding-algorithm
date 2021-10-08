package ui.thirdScenario

import App.Companion.WINDOW_HEIGHT
import App.Companion.WINDOW_WIDTH
import javafx.geometry.Pos
import javafx.scene.text.Font
import tornadofx.*
import ui.general.ParametersView
import ui.general.ScenarioSelectorView
import util.nextView
import viewModel.ThirdScenarioViewModel

class ImageProcessingResultsView : View() {

    private val thirdScenarioViewModel: ThirdScenarioViewModel by inject()

    private val originalImage
        get() = thirdScenarioViewModel.originalData
    private val notCodedProcessedImage
        get() = thirdScenarioViewModel.notCodedProcessedData
    private val codedProcessedImage
        get() = thirdScenarioViewModel.codedProcessedData

    override val root = borderpane {
        padding = insets(10)
        center = vbox(spacing = 2, alignment = Pos.CENTER) {
            label("Original image:") {
                font = Font(20.0)
            }
            imageview(originalImage)
        }
        vbox(spacing = 2, alignment = Pos.CENTER) {
            label("Not encoded/decoded image:") {
                font = Font(20.0)
            }
            imageview(notCodedProcessedImage)
        }
        vbox(spacing = 2, alignment = Pos.CENTER) {
            label("Encoded/decoded image:") {
                font = Font(20.0)
            }
            imageview(codedProcessedImage)
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
        currentWindow?.height = originalImage.value.height
    }

    override fun onUndock() {
        super.onUndock()
        currentWindow?.width = WINDOW_WIDTH
        currentWindow?.height = WINDOW_HEIGHT
    }
}
