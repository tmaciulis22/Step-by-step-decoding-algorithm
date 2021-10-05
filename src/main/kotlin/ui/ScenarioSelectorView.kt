package ui

import javafx.geometry.Pos
import javafx.scene.text.Font
import tornadofx.*
import ui.firstScenario.FirstScenarioView

class ScenarioSelectorView : View() {
    override val root = vbox(10, alignment = Pos.CENTER) {
        label("Select scenario") {
            font = Font(30.0)
        }
        button("First - vector message") {
            action {
                replaceWith(
                    FirstScenarioView::class,
                    ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
                )
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
