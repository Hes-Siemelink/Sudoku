package hes.nonogram

import hes.nonogram.Cell.State.FILLED

class Puzzle(val rows: List<Line>, val columns: List<Line>) {

    fun cell(row: Int, column: Int): Cell {
        return rows[row].cells[column]
    }

    val valid: Boolean
        get() = rows.all { it.valid } && columns.all { it.valid }

    val solved: Boolean
        get() = rows.all { it.solved } && columns.all { it.solved }

    fun copy(): Puzzle {
        val rowsCopy = rows.map { it.copy() }

        // Recycle cells for columns
        val columns = mutableListOf<Line>()
        for ((index, column) in columns.withIndex()) {
            val line = Line(column.hints, cellsByIndex(rowsCopy, index))
            columns.add(line)
        }

        return Puzzle(rows, columns)
    }

    fun solve(): Puzzle? {

        if (solved) {
            return this
        }

        for (r in rows.indices) {
            for (c in rows[r].cells.indices) {
                if (rows[r].cells[c].state == FILLED) continue

                val clone = copy()
                clone.rows[r].cells[c].state = FILLED

                return clone.solve() ?: continue
            }
        }

        return null
    }

    fun print() {
        rows.forEach {
            println(toString(it.cells))
        }
    }

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
