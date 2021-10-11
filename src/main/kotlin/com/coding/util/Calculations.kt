package com.coding.util

import javax.naming.OperationNotSupportedException

// Extension function used for multiplication of a matrix and a transposed vector.
// If matrix columns number and length of vector do not match, then exception is thrown
// Arguments: Matrix[K, N] (Receiver type), vector[N]
// Returns: Vector[K]
fun List<List<Int>>.multiplyTransposed(vector: List<Int>): List<Int> {
    if (this[0].size != vector.size) throw OperationNotSupportedException()

    val result = MutableList(this.size) { 0 }

    this.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { colIndex, value ->
            result[rowIndex] += value * vector[colIndex]
            if (result[rowIndex] > 1) result[rowIndex] = 0
        }
    }

    return result
}

// Extensions function used for multiplication of a matrix and vector (not transposed)
// If matrix rows number and length of vector do not match, then exception is thrown.
// Arguments: Matrix[K, N] (Receiver type), vector[K]
// Returns: Vector[N]
fun List<List<Int>>.multiply(vector: List<Int>): List<Int> {
    if (this.size != vector.size) throw OperationNotSupportedException()

    val result = MutableList(this[0].size) { 0 }

    for (colIndex in 0 until this[0].size) {
        for (rowIndex in 0 until this.size) {
            result[colIndex] += this[rowIndex][colIndex] * vector[rowIndex]
            if (result[colIndex] > 1) result[colIndex] = 0
        }
    }

    return result
}

// Extension function which calculates how many positive bits are in vector
// Arguments: Binary vector (Receiver type)
// Returns: number of positive bits
fun List<Int>.getNumOfOnes(): Int =
    if (this.isEmpty())
        0
    else
        this.reduce { acc, bit -> acc + bit }

// Extension function which reverses 0 bit to 1 and vice versa.
// It throws exception if the receiver value is not 0 or 1.
// Arguments: 0 or 1 bit (Receiver type)
// Returns: Reversed(flipped) bit
fun Int.reverseBit(): Int =
    when(this) {
        0 -> 1
        1 -> 0
        else -> throw OperationNotSupportedException()
    }
