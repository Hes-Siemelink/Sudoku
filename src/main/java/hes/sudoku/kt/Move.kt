package hes.sudoku.kt

data class Move(val cell: Cell, val number: Int) {

    private var description = ""

    fun withDescription(description: String): Move {
        this.description = description
        return this
    }

    override fun toString(): String {
        return "$number in row ${cell.row + 1}, column ${cell.column + 1} - $description"
    }
}