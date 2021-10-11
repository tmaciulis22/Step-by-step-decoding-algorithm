package com.coding.ui.thirdScenario

import javafx.geometry.Pos
import javafx.scene.text.Font
import tornadofx.*
import com.coding.util.nextView
import com.coding.viewModel.ThirdScenarioViewModel

// GUI class which shows progress bar as long as the previously selected image is being processed(encoded, decoded, etc.)
// Once the processing is done, the next view with results is automatically shown.
class ImageProcessingView : View() {

    private val thirdScenarioViewModel: ThirdScenarioViewModel by inject()

    override val root = vbox(alignment = Pos.CENTER, spacing = 8) {
        padding = insets(16.0)
        label("Processing image...") {
            font = Font(20.0)
        }
        progressbar {
            useMaxWidth = true
        }
    }

    override fun onDock() {
        super.onDock()
        runAsync {
            thirdScenarioViewModel.processImage()
        } ui {
            nextView<ImageProcessingResultsView>()
        }
    }
}
