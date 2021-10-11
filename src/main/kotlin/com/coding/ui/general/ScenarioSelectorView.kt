package com.coding.ui.general

import javafx.geometry.Pos
import javafx.scene.text.Font
import tornadofx.*
import com.coding.ui.firstScenario.EncodeView
import com.coding.ui.secondScenario.TextInputView
import com.coding.ui.thirdScenario.ImageSelectorView
import com.coding.util.nextView
import com.coding.viewModel.Encoder
import com.coding.viewModel.FirstScenarioViewModel
import com.coding.viewModel.SecondScenarioViewModel
import com.coding.viewModel.ThirdScenarioViewModel

// GUI class for selecting scenario.
// Once scenario is selected appropriate business logic ViewModel is initialized.
class ScenarioSelectorView : View() {

    private val encoder: Encoder by inject()

    private val firstScenarioViewModel: FirstScenarioViewModel by inject()
    private val secondScenarioViewModel: SecondScenarioViewModel by inject()
    private val thirdScenarioViewModel: ThirdScenarioViewModel by inject()

    override val root = vbox(10, alignment = Pos.CENTER) {
        label("Select scenario") {
            font = Font(30.0)
        }
        button("First - vector message") {
            action {
                firstScenarioViewModel.init(encoder.parameterN, encoder.parameterK)
                nextView<EncodeView>()
            }
        }
        button("Second - text message") {
            action {
                secondScenarioViewModel.init()
                nextView<TextInputView>()
            }
        }
        button("Third - image message") {
            action {
                thirdScenarioViewModel.init()
                nextView<ImageSelectorView>()
            }
        }
    }
}
