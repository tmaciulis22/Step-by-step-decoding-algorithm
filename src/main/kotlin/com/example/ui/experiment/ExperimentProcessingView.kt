package com.example.ui.experiment

import javafx.geometry.Pos
import javafx.scene.text.Font
import tornadofx.*
import com.example.util.nextView
import com.example.viewModel.ExperimentViewModel

class ExperimentProcessingView : View() {

    private val experimentViewModel: ExperimentViewModel by inject()

    override val root = vbox(alignment = Pos.CENTER, spacing = 8) {
        padding = insets(16.0)
        label("Processing...") {
            font = Font(20.0)
        }
        progressbar {
            useMaxWidth = true
        }
    }

    override fun onDock() {
        super.onDock()
        runAsync {
            experimentViewModel.runExperiments()
        } ui {
            nextView<ExperimentResultsView>()
        }
    }
}
