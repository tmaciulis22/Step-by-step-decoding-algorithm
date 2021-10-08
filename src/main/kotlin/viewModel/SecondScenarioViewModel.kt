package viewModel

class SecondScenarioViewModel : ProcessingViewModel<String>() {

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

    private fun binaryVectorsToText(binaryVectors: List<List<Int>>): String {
        val bytes = binaryVectorsToBytes(binaryVectors)
        val chars = bytes.map { it.toInt().toChar() }.toCharArray()

        return String(chars)
    }
}
