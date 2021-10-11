package ui.secondScenario

import javafx.geometry.Pos
import javafx.scene.text.Font
import tornadofx.*
import util.nextView
import viewModel.SecondScenarioViewModel

// GUI class which shows progress bar as long as the previously inputted text is being processed(encoded, decoded, etc.)
// Once the processing is done, the next view with results is automatically shown.
class TextProcessingView : View() {

    private val secondScenarioViewModel: SecondScenarioViewModel by inject()

    override val root = vbox(alignment = Pos.CENTER, spacing = 8) {
        padding = insets(16.0)
        label("Processing text...") {
            font = Font(20.0)
        }
        progressbar {
            useMaxWidth = true
        }
    }

    override fun onDock() {
        super.onDock()
        runAsync {
            secondScenarioViewModel.processText()
        } ui {
            nextView<TextProcessingResultsView>()
        }
    }
}
