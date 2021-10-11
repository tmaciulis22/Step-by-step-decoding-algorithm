package com.coding.ui.secondScenario

import javafx.geometry.Pos
import javafx.scene.text.Font
import tornadofx.*
import com.coding.util.nextView
import com.coding.viewModel.SecondScenarioViewModel

// GUI class for inputting text which will be encoded/decoded.
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
