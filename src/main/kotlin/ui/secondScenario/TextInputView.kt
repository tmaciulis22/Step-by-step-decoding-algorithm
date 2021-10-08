package ui.secondScenario

import javafx.geometry.Pos
import javafx.scene.text.Font
import tornadofx.*
import util.nextView
import viewModel.SecondScenarioViewModel

class TextInputView : View() {

    private val secondScenarioViewModel: SecondScenarioViewModel by inject()

    private val originalText
        get() = secondScenarioViewModel.originalData

    override val root = borderpane {
        padding = insets(10.0)
        top = label("Input text:") {
            font = Font(20.0)
        }
        center = textarea(originalText) {
            isWrapText = true
        }
        bottom = hbox(alignment = Pos.CENTER_RIGHT) {
            padding = insets(top = 10.0)
            button("Next") {
                action {
                    nextView<TextProcessingView>()
                }
            }
        }
    }
}
