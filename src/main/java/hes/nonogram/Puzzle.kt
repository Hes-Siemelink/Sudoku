package hes.nonogram

class Puzzle(val rows: List<Line>, val columns: List<Line>) {

    fun cell(row: Int, column: Int): Cell {
        return rows[row].cells[column]
    }

    val valid: Boolean
        get() = rows.all { it.valid } && columns.all { it.valid }

    val solved: Boolean
        get() = rows.all { it.solved } && columns.all { it.solved }

    companion object {
        fun from(rowHints: List<List<Int>>, columnHints: List<List<Int>>): Puzzle {

            val width = columnHints.size

            // Create rows with empty cells
            val rows = mutableListOf<Line>()
            for (hint in rowHints) {
                rows.add(Line(hint, emptyCells(width)))
            }

            // Recycle cells for columns
            val columns = mutableListOf<Line>()
            for ((index, hint) in columnHints.withIndex()) {
                val column = Line(hint, cellsByIndex(rows, index))
                columns.add(column)
            }

            return Puzzle(rows, columns)
        }
    }
}

private fun emptyCells(count: Int): List<Cell> {
    val cells = mutableListOf<Cell>()
    for (i in 1..count) {
        cells.add(Cell())
    }
    return cells
}

private fun cellsByIndex(lines: List<Line>, index: Int): List<Cell> {
    val cells = mutableListOf<Cell>()
    for (line in lines) {
        cells.add(line.cells[index])
    }
    return cells
}
