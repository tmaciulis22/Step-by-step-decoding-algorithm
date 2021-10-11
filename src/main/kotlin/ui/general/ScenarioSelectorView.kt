package ui.general

import javafx.geometry.Pos
import javafx.scene.text.Font
import tornadofx.*
import ui.firstScenario.EncodeView
import ui.secondScenario.TextInputView
import ui.thirdScenario.ImageSelectorView
import util.nextView
import viewModel.Encoder
import viewModel.FirstScenarioViewModel
import viewModel.SecondScenarioViewModel
import viewModel.ThirdScenarioViewModel

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
