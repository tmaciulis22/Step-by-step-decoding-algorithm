package ui

import javafx.scene.text.Font
import tornadofx.*

class ScenarioSelectorView : View() {
    override val root = vbox(2) {
        label("Select scenario") {
            font = Font(30.0)
        }
        button("First - vector message") {
            action {
                TODO()
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
