package com.example.ui.experiment

import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.NumberAxis
import tornadofx.*
import com.example.ui.general.ParametersView
import com.example.util.nextView
import com.example.viewModel.ExperimentViewModel

class ExperimentResultsView : View() {

    private val experimentViewModel: ExperimentViewModel by inject()

    private val parametersN
        get() = experimentViewModel.parametersN
    private val parametersK
        get() = experimentViewModel.parametersK
    private val firstExperimentResultsCoded
        get() = experimentViewModel.firstExperimentResultsCoded
    private val firstExperimentResultsUncoded
        get() = experimentViewModel.firstExperimentResultsUncoded

    private val parametersPe
        get() = experimentViewModel.parametersPe
    private val secondExperimentResultsCoded
        get() = experimentViewModel.secondExperimentResultsCoded

    override val root = scrollpane {
        padding = insets(10)
        vbox(spacing = 12, alignment = Pos.CENTER) {
            hbox(spacing = 8, alignment = Pos.CENTER) {
                setPrefSize(1800.0, 900.0)
                vbox(spacing = 2, alignment = Pos.CENTER) {
                    linechart("First experiment", CategoryAxis(), NumberAxis()) {
                        setPrefSize(850.0, 425.0)
                        yAxis.label = "Difference from original message"
                        xAxis.label = "Parameters N and K"
                        multiseries("Coded", "Uncoded") {
                            parametersN.forEachIndexed { index, n ->
                                data(
                                    "n = $n | k = ${parametersK[index]}",
                                    firstExperimentResultsCoded[index],
                                    firstExperimentResultsUncoded[index]
                                )
                            }
                        }
                    }
                    text("With different N and K parameters the effectiveness of coded message was checked against uncoded messages.")
                    text("Probability of mistake in channel: 0.01. Generator matrix was selected randomly.")
                }
                separator(Orientation.VERTICAL)
                vbox(spacing = 2, alignment = Pos.CENTER) {
                    linechart("Second experiment", NumberAxis(), NumberAxis()) {
                        setPrefSize(850.0, 425.0)
                        yAxis.label = "Difference from original message"
                        xAxis.label = "Parameter Pe"
                        series("Coded") {
                            parametersPe.forEachIndexed { index, p ->
                                data(
                                    p,
                                    secondExperimentResultsCoded[index],
                                )
                            }
                        }
                    }
                    text("With different probability parameter the effectiveness of coded message was checked against uncoded messages.")
                    text("Throughout the experiment these parameters were constant: N = 5, K = 3. Generator matrix was selected randomly.")
                }
            }
            button("Go back") {
                action {
                    nextView<ParametersView>(transition = ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT))
                }
            }
        }
    }

    override fun onDock() {
        super.onDock()
        currentStage?.isMaximized = true
    }

    override fun onUndock() {
        super.onUndock()
        currentStage?.isMaximized = false
    }
}
