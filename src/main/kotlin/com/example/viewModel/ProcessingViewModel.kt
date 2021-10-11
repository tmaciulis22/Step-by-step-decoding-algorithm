package com.example.viewModel

import javafx.beans.property.SimpleObjectProperty
import tornadofx.ViewModel

// This abstract view model includes properties and methods used by second and third scenario view models
// This class uses generic type, so every view model which inherits this class can set what kind of data type it processes.
abstract class ProcessingViewModel<T> : ViewModel() {

    // Original data(e.g. Text/Image) entered by user
    val originalData = SimpleObjectProperty<T>()
    // Data(e.g. Text/Image) which was not encoded/decoded and sent to channel as is
    val notCodedProcessedData = SimpleObjectProperty<T>()
    // Data(e.g. Text/Image) which was encoded, sent to channel and decoded
    val codedProcessedData = SimpleObjectProperty<T>()

    // How many zeros were added to the last message vector, so it would be of size K
    // This information is not sent through channel.
    private var zeroesAppended = 0

    protected val encoder: Encoder by inject()
    protected val channel: Channel by inject()
    protected val decoder: Decoder by inject()

    // Initializes/resets the view model with default values
    open fun init() {
        zeroesAppended = 0
        originalData.set(null)
        notCodedProcessedData.set(null)
        codedProcessedData.set(null)
    }

    // Converts array of bytes into binary string. E.g. byte 26 to 00011010
    // Arguments: byte array
    // Returns: string of binary bits
    protected fun bytesToBinaryString(byteArray: ByteArray) = byteArray.joinToString("") {
        // Signed byte to unsigned (leaving least significant 8 bits)
        // 0x100 (256) is used to add zeros to the left of string if necessary to get proper 8 bit format
        val asd = Integer.toBinaryString((it.toInt() and 0xFF) + 0x100).substring(1)
        asd
    }

    // Converts binary string to binary message vectors of length K.
    // If needed, zeroes are appended to last vector in order to have the length K.
    // Arguments: string of binary bits
    // Returns: list of message vectors of length K
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

    // Converts decoded binary message vectors to array of bytes.
    // It merges all message vectors and drops the zeroes which were appended to last vector if needed.
    // Arguments: list of binary decoded message vectors
    // Returns: array of bytes
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
