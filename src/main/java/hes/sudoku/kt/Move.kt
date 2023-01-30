package hes.sudoku.kt

import java.util.Objects

data class Move(val cell: Cell, val number: Int, val reason: String) {

    override fun equals(otherObject: Any?): Boolean {
        if (this === otherObject) return true
        if (otherObject == null || javaClass != otherObject.javaClass) return false
        val (cell1, number1) = otherObject as Move
        return cell.row == cell1.row && cell.column == cell1.column && number === number1
    }

    override fun hashCode(): Int {
        return Objects.hash(cell.row, cell.column, number)
    }

    override fun toString(): String {
        return String.format("%s in row %s, column %s - %s", number, cell.row + 1, cell.column + 1, reason)
    }
}