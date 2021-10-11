package viewModel

class SecondScenarioViewModel : ProcessingViewModel<String>() {

    // Processes the text. It processes the text both ways: by encoding/decoding, and not coding it at all.
    fun processText() {
        val textInBinary = bytesToBinaryString(originalData.value.toByteArray())
        val textVectors = binaryStringToBinaryVectors(textInBinary)
        val fromChannelTextVectors = textVectors.map { channel.send(it) }
        notCodedProcessedData.set(binaryVectorsToText(fromChannelTextVectors))

        val encodedTextVectors = textVectors.map { encoder.encode(it) }
        val encodedFromChannelTextVectors = encodedTextVectors.map { channel.send(it) }
        val decodedTextVectors = encodedFromChannelTextVectors.map { decoder.decode(it) }
        codedProcessedData.set(binaryVectorsToText(decodedTextVectors))
    }

    // Converts binary message vectors to text.
    // It first converts the vectors to byte array and then the bytes to chars
    // Arguments: list of binary message vectors
    // Returns: text
    private fun binaryVectorsToText(binaryVectors: List<List<Int>>): String {
        val bytes = binaryVectorsToBytes(binaryVectors)
        val chars = bytes.map { it.toInt().toChar() }.toCharArray()

        return String(chars)
    }
}
