package com.example

import com.example.ui.general.ParametersView
import javafx.stage.Stage
import tornadofx.App

class MyApp : App(ParametersView::class) {
    override fun start(stage: Stage) {
        super.start(stage)
        stage.minHeight = WINDOW_HEIGHT
        stage.minWidth = WINDOW_WIDTH
    }

    companion object {
        const val WINDOW_HEIGHT = 350.0
        const val WINDOW_WIDTH = 550.0
    }
}