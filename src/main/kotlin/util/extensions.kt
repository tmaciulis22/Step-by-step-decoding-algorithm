package util

import javafx.event.EventTarget
import tornadofx.*
import javax.naming.OperationNotSupportedException
import kotlin.reflect.KClass

// GUI related extensions
fun <T> EventTarget.textFieldCell(
    value: T,
    isEditable: Boolean = true,
    changeListener: ((String) -> Unit)? = null
) = textfield(value.toString()) {
        this.isEditable = isEditable
        textProperty().addListener { _, _, new ->
            changeListener?.invoke(new)
        }
    }

inline fun <reified T : UIComponent> View.nextView(
    params: Map<String, Any>? = null,
    transition: ViewTransition? = ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
) = replaceWith(find(T::class, scope, params), transition)

// Extension function used for multiplication of a matrix and a transposed vector
// Arguments: Matrix[K, N] (Receiver type), vector[N]
// Returns: Vector[K]
fun Array<Array<Int>>.multiplyTransposed(vector: Array<Int>): Array<Int> {
    if (this[0].size != vector.size) throw OperationNotSupportedException()

    val result = Array(this.size) { 0 }

    this.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { colIndex, value ->
            result[rowIndex] += value * vector[colIndex]
            if (result[rowIndex] > 1) result[rowIndex] = 0
        }
    }

    return result
}

// Extensions function used for multiplication of a matrix and vector (not transposed)
// Arguments: Matrix[K, N] (Receiver type), vector[K]
// Returns: Vector[N]
fun Array<Array<Int>>.multiply(vector: Array<Int>): Array<Int> {
    if (this.size != vector.size) throw OperationNotSupportedException()

    val result = Array(this[0].size) { 0 }

    for (colIndex in 0 until this[0].size) {
        for (rowIndex in 0 until this.size) {
            result[colIndex] += this[rowIndex][colIndex] * vector[rowIndex]
            if (result[colIndex] > 1) result[colIndex] = 0
        }
    }

    return result
}

// Extension function which checks if matrix rows include the vector provided in the arguments
// Arguments: Any 2d Matrix[X, Y] (Receiver type), vector[Y]
// Returns: Boolean
fun Array<Array<Int>>.includes(vector: Array<Int>): Boolean =
    if (this[0].size != vector.size)
        throw OperationNotSupportedException()
    else
        this.any { it.contentEquals(vector) }

fun Array<Int>.joinBitsToString() = this.joinToString { it.toString() }

// Extension function which calculates how many positive bits are in vector
// Arguments: Any 1d vector (Receiver type)
// Returns: Int
fun Collection<Int>.getNumOfOnes(): Int =
    if (this.isEmpty())
        0
    else
        this.reduce { acc, bit -> acc + bit }

// Extension function which reverses 0 bit to 1 and vice versa
// Arguments: Any 0 or 1 bit (Receiver type)
// Returns: Reversed(flipped) bit
fun Int.reverseBit(): Int =
    when(this) {
        0 -> 1
        1 -> 0
        else -> throw OperationNotSupportedException()
    }
