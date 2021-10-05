package util

fun Array<Array<Int>>.multiply(vector: Array<Int>): Array<Int>? {
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

fun Array<Array<Int>>.includes(vector: Array<Int>?): Boolean = this.any { it.contentEquals(vector) }

fun Array<Int>.getNumOfOnes(): Int = this.reduce { acc, bit -> acc + bit }
