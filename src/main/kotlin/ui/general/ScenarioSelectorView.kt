package ui.general

import javafx.geometry.Pos
import javafx.scene.text.Font
import tornadofx.*
import ui.firstScenario.EncodeView
import util.nextView
import viewModel.Encoder
import viewModel.FirstScenarioViewModel

class ScenarioSelectorView : View() {

    private val encoder: Encoder by inject()
    private val firstScenarioViewModel: FirstScenarioViewModel by inject()

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
                TODO()
            }
        }
        button("Third - image message") {
            action {
                TODO()
            }
        }
    }
}
