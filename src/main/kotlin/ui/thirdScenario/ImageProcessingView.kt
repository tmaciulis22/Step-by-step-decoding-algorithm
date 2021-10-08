package ui.thirdScenario

import javafx.geometry.Pos
import javafx.scene.text.Font
import tornadofx.*
import util.nextView
import viewModel.ThirdScenarioViewModel

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
