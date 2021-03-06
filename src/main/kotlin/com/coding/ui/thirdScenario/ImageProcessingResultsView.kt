package com.coding.ui.thirdScenario

import com.coding.MyApp.Companion.WINDOW_HEIGHT
import com.coding.MyApp.Companion.WINDOW_WIDTH
import javafx.geometry.Pos
import javafx.scene.text.Font
import tornadofx.*
import com.coding.ui.general.ParametersView
import com.coding.ui.general.ScenarioSelectorView
import com.coding.util.nextView
import com.coding.viewModel.ThirdScenarioViewModel

// GUI class which shows the image processing (encoding, decoding, etc.) results.
// Original image, the image which was sent through channel without coding, and also encoded/decoded image are shown as well for comparison.
// User can choose to try other scenario or to input new parameters.
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
        center = scrollpane {
            hbox(spacing = 8, alignment = Pos.CENTER) {
                vbox(spacing = 2, alignment = Pos.CENTER) {
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
        currentWindow?.width = originalImage.value.width * 3 + 60
        currentWindow?.height = originalImage.value.height + 175
    }

    override fun onUndock() {
        super.onUndock()
        currentWindow?.width = WINDOW_WIDTH
        currentWindow?.height = WINDOW_HEIGHT
    }
}
