package util

// Extension function used for multiplication of a matrix and a transposed vector
// Arguments: Matrix[K, N] (Receiver type), vector[N]
// Returns: Vector[K]
fun Array<Array<Int>>.multiplyTransposed(vector: Array<Int>): Array<Int>? {
    if (this[0].size != vector.size) return null

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
fun Array<Array<Int>>.multiply(vector: Array<Int>): Array<Int>? {
    if (this.size != vector.size) return null

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
fun Array<Array<Int>>.includes(vector: Array<Int>): Boolean = this.any { it.contentEquals(vector) }

// Extension function which calculates how many positive bits are in vector
// Arguments: Any 1d vector (Receiver type)
// Returns: Int
fun Collection<Int>.getNumOfOnes(): Int {
    if (this.isEmpty()) return 0

    return this.reduce { acc, bit -> acc + bit }
}
