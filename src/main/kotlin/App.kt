import javafx.stage.Stage
import tornadofx.App
import ui.general.ParametersView

class App : App(ParametersView::class) {
    override fun start(stage: Stage) {
        super.start(stage)
        stage.minHeight = 350.0
        stage.minWidth = 550.0
    }
}
