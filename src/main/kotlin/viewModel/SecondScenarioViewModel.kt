package viewModel

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.ViewModel

class SecondScenarioViewModel : ViewModel() {

    val originalText = SimpleStringProperty("")
    val progress = SimpleDoubleProperty(0.0)

    // Text which was not encoded/decoded and sent to channel as is
    val notCodedProcessedText = SimpleStringProperty("")
    // Text which was encoded, sent to channel and decoded
    val codedProcessedText = SimpleStringProperty("")

    // How many zeros were added to the last text vector, so it would be of size K
    private var zeroesAppended = 0

    private val encoder: Encoder by inject()
    private val channel: Channel by inject()
    private val decoder: Decoder by inject()

    fun init() {
        originalText.set("")
        progress.set(0.0)
        notCodedProcessedText.set("")
        codedProcessedText.set("")
        zeroesAppended = 0
    }

    fun processText() {
        val textInBinary = textToBinaryString()
        progress.set(progress.value + PROGRESS_INCREMENT)

        val textVectors = binaryStringToVectors(textInBinary)
        progress.set(progress.value + PROGRESS_INCREMENT)

        val fromChannelTextVectors = textVectors.map { channel.send(it) }
        progress.set(progress.value + PROGRESS_INCREMENT)

        notCodedProcessedText.set(binaryVectorsToText(fromChannelTextVectors))
        progress.set(progress.value + PROGRESS_INCREMENT)

        val encodedTextVectors = textVectors.map { encoder.encode(it) }
        progress.set(progress.value + PROGRESS_INCREMENT)

        val encodedFromChannelTextVectors = encodedTextVectors.map { channel.send(it) }
        progress.set(progress.value + PROGRESS_INCREMENT)

        val decodedTextVectors = encodedFromChannelTextVectors.map { decoder.decode(it) }
        progress.set(progress.value + PROGRESS_INCREMENT)

        codedProcessedText.set(binaryVectorsToText(decodedTextVectors))
        progress.set(progress.value + PROGRESS_INCREMENT)
    }

    private fun textToBinaryString(): String {
        val builder = StringBuilder()

        originalText.value.toCharArray().forEach {
            // Get char ASCII in binary
            val binaryString = it.code.toString(2)
            // Pad zeros to the left of string if needed to form a byte
            builder.append(String.format("%08d", binaryString.toInt()))
        }

        return builder.toString()
    }

    private fun binaryVectorsToText(binaryVectors: List<List<Int>>): String {
        val bits = binaryVectors.flatten().dropLast(zeroesAppended).joinToString("")
        val chars = mutableListOf<Char>()

        for (i in bits.indices step 8) {
            val byte = bits.substring(i, i + 8) // Get 8 bits to form a byte
            val code = byte.toInt(2) // From binary to decimal value in ASCII
            chars.add(code.toChar())
        }

        return String(chars.toCharArray())
    }

    private fun binaryStringToVectors(binaryString: String): List<List<Int>> {
        val textVectors = mutableListOf<List<Int>>()
        val currentVector = mutableListOf<Int>()

        binaryString.forEachIndexed { index, bitChar ->
            val bit = bitChar.digitToInt()
            currentVector.add(bit)

            if (currentVector.size == encoder.parameterK) {
                textVectors.add(currentVector.toList())
                currentVector.clear()
            } else if (index == binaryString.lastIndex) {
                while (currentVector.size != encoder.parameterK) {
                    currentVector.add(0)
                    zeroesAppended++
                }
                textVectors.add(currentVector.toList())
                currentVector.clear()
            }
        }

        return textVectors
    }

    companion object {
        const val PROGRESS_INCREMENT = 12.5 // 100 divided by total number of function invocations in processText method
    }
}
