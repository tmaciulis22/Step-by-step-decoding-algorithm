package ui.thirdScenario

import javafx.beans.property.SimpleBooleanProperty
import javafx.geometry.Pos
import javafx.scene.text.Font
import javafx.stage.FileChooser
import tornadofx.*
import util.nextView
import viewModel.ThirdScenarioViewModel

// GUI class for selecting image for encoding/decoding.
// User clicks the Choose button and file dialog is opened, where user can select the bmp image.
class ImageSelectorView : View() {

    private val thirdScenarioViewModel: ThirdScenarioViewModel by inject()

    private val imageFile
        get() = thirdScenarioViewModel.imageFile

    private val enableNextButton = SimpleBooleanProperty(false)

    override val root = borderpane {
        padding = insets(10.0)
        top = stackpane {
            label("Please choose an image in bmp format") {
                font = Font(20.0)
            }
        }
        center = stackpane {
            button("Choose") {
                action {
                    val files = chooseFile(
                        filters = arrayOf(FileChooser.ExtensionFilter("bitmap", listOf("*.bmp")))
                    )
                    if (files.isNotEmpty()) {
                        imageFile.set(files[0])
                        enableNextButton.set(true)
                    }
                }
            }
        }
        bottom = hbox(alignment = Pos.CENTER_RIGHT) {
            button("Next") {
                enableWhen(enableNextButton)
                action {
                    nextView<ImageProcessingView>()
                }
            }
        }
    }
}
