package viewModel

import javafx.beans.property.SimpleObjectProperty
import tornadofx.ViewModel

abstract class ProcessingViewModel<T> : ViewModel() {

    // Original data(Text/Image) entered by user
    val originalData = SimpleObjectProperty<T>()
    // Data(Text/Image) which was not encoded/decoded and sent to channel as is
    val notCodedProcessedData = SimpleObjectProperty<T>()
    // Data(Text/Image) which was encoded, sent to channel and decoded
    val codedProcessedData = SimpleObjectProperty<T>()

    // How many zeros were added to the last message vector, so it would be of size K
    private var zeroesAppended = 0

    protected val encoder: Encoder by inject()
    protected val channel: Channel by inject()
    protected val decoder: Decoder by inject()

    open fun init() {
        zeroesAppended = 0
        originalData.set(null)
        notCodedProcessedData.set(null)
        codedProcessedData.set(null)
    }

    protected fun bytesToBinaryString(byteArray: ByteArray) = byteArray.joinToString("") {
        // Signed byte to unsigned (leaving least significant 8 bits)
        // 0x100 (256) is used to add zeros to the left of string if necessary to get proper 8 bit format
        Integer.toBinaryString((it.toInt() and 0xFF) + 0x100).substring(1)
    }

    protected fun binaryStringToBinaryVectors(binaryString: String): List<List<Int>> {
        val binaryVectors = mutableListOf<List<Int>>()
        val currentVector = mutableListOf<Int>()

        binaryString.forEachIndexed { index, bit ->
            val bitAsInt = bit.digitToInt()
            currentVector.add(bitAsInt)

            if (currentVector.size == encoder.parameterK) {
                binaryVectors.add(currentVector.toList())
                currentVector.clear()
            } else if (index == binaryString.lastIndex) {
                while (currentVector.size != encoder.parameterK) {
                    currentVector.add(0)
                    zeroesAppended++
                }
                binaryVectors.add(currentVector.toList())
                currentVector.clear()
            }
        }

        return binaryVectors
    }

    protected fun binaryVectorsToBytes(binaryVectors: List<List<Int>>): ByteArray {
        val bits = binaryVectors.flatten().dropLast(zeroesAppended).joinToString("")
        val bytes = mutableListOf<Byte>()

        for (i in bits.indices step 8) {
            // Get 8 bits
            val binaryByte = bits.substring(i, i + 8)
            // From binary 8 bit string to Byte
            val byte = binaryByte.toInt(2).toByte()
            bytes.add(byte)
        }

        return bytes.toByteArray()
    }
}
