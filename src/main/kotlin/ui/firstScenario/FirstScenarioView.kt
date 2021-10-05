package ui.firstScenario

import module.Encoder
import javafx.geometry.Pos
import javafx.scene.text.Font
import tornadofx.View
import tornadofx.label
import tornadofx.textfield
import tornadofx.vbox

class FirstScenarioView : View() {

    private val encoder: Encoder by inject()

    override val root = vbox(10, alignment = Pos.CENTER) {
        label("Enter k=${encoder.parameterK} length vector:") {
            font = Font(20.0)
        }
        textfield {  }
    }
}