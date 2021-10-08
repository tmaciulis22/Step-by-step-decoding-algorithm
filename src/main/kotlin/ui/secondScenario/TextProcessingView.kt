package ui.secondScenario

import javafx.geometry.Pos
import javafx.scene.text.Font
import tornadofx.*
import util.nextView
import viewModel.SecondScenarioViewModel

class TextProcessingView : View() {

    private val secondScenarioViewModel: SecondScenarioViewModel by inject()

    private val progress
        get() = secondScenarioViewModel.progress

    override val root = vbox(alignment = Pos.CENTER, spacing = 8) {
        padding = insets(16.0)
        label("Processing text...") {
            font = Font(20.0)
        }
        progressbar(progress) {
            useMaxWidth = true
        }
    }

    override fun onDock() {
        super.onDock()
        runAsync {
            secondScenarioViewModel.processText()
        } ui {
            nextView<ResultsView>()
        }
    }
}
