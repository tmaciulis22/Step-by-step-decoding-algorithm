package viewModel

import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.Image
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream

class ThirdScenarioViewModel : ProcessingViewModel<Image>() {

    val imageFile = SimpleObjectProperty<File>()

    // Header info of bmp image, which is not sent through channel
    private lateinit var headerInfo: ByteArray

    fun processImage() {
        val imageByteArrayWithoutHeader = setHeaderInfoAndReturnRemainingBytes()
        val imageBinaryString = bytesToBinaryString(imageByteArrayWithoutHeader)
        val binaryVectors = binaryStringToBinaryVectors(imageBinaryString)
        val fromChannelMessageVectors = binaryVectors.map { channel.send(it) }
        notCodedProcessedData.set(binaryVectorsToImage(fromChannelMessageVectors))

        val encodedBinaryVectors = binaryVectors.map { encoder.encode(it) }
        val encodedFromChannelBinaryVectors = encodedBinaryVectors.map { channel.send(it) }
        val decodedBinaryVectors = encodedFromChannelBinaryVectors.map { decoder.decode(it) }
        codedProcessedData.set(binaryVectorsToImage(decodedBinaryVectors))
    }

    private fun setHeaderInfoAndReturnRemainingBytes(): ByteArray =
        FileInputStream(imageFile.value).use {
            originalData.set(Image(it))

            val imageByteArray = it.readBytes()
            headerInfo = imageByteArray.take(BMP_HEADER_SIZE_IN_BYTES).toByteArray()

            return imageByteArray.drop(BMP_HEADER_SIZE_IN_BYTES).toByteArray()
        }

    private fun binaryVectorsToImage(binaryVectors: List<List<Int>>): Image {
        val bytes = binaryVectorsToBytes(binaryVectors).toMutableList()

        val bytesWithHeader = headerInfo.toMutableList()
        bytesWithHeader.addAll(bytes)
        val byteArray = bytesWithHeader.toByteArray()
        val byteArrayStream = ByteArrayInputStream(byteArray)

        return Image(byteArrayStream)
    }

    companion object {
        const val BMP_HEADER_SIZE_IN_BYTES = 54
    }
}
