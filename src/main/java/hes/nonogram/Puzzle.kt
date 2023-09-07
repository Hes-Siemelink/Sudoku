package hes.nonogram

import hes.nonogram.Cell.State.*

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
        val columnsCopy = mutableListOf<Line>()
        for ((index, column) in columns.withIndex()) {
            val line = Line(column.hints, cellsByIndex(rows, index))
            columnsCopy.add(line)
        }

        return Puzzle(rowsCopy, columnsCopy)
    }

    fun solve(): Puzzle? {

        if (solved) {
            return this
        }

        for (r in rows.indices) {
            for (c in rows[r].cells.indices) {
                if (rows[r].cells[c].state != UNKNOWN) continue

                val clone = copy()
                clone.rows[r].cells[c].state = FILLED
                clone.columns[c].cells[r].state = FILLED

                if (!clone.valid) {
                    this.rows[r].cells[c].state = EMPTY
//                    this.columns[c].cells[r].state = EMPTY
                    continue
                }

                println("Checking with ($r, $c):\n$clone\n")

                val solution = clone.solve()
                if (solution != null) {
                    return solution
                }

                this.rows[r].cells[c].state = EMPTY
            }
        }

        return null
    }

    override fun toString(): String {
        return buildString {
            columns.forEach {
                append(it.hints)
                append(" ")
            }
            append("\n")
            rows.forEach {
                append(it.toString())
                append(" ")
                append(it.hints)
                append('\n')
            }
        }
    }

    fun print() {
        rows.forEach {
            println(toString(it.cells).replace('.', ' ').replace('-', '·').replace('*', '█'))
        }
    }
}

class NonogramSpec(
    val rowHints: MutableList<List<Int>> = mutableListOf(),
    val columnHints: MutableList<List<Int>> = mutableListOf()
) {
    fun row(vararg hints: Int) {
        rowHints.add(hints.asList())
    }

    fun column(vararg hints: Int) {
        columnHints.add(hints.asList())
    }

    fun toPuzzle(rowHints: List<List<Int>>, columnHints: List<List<Int>>): Puzzle {

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

        val puzzle = Puzzle(rows, columns)
        return puzzle
    }
}

fun nonogram(init: NonogramSpec.() -> Unit): Puzzle {
    val spec = NonogramSpec()
    spec.init()

    return spec.toPuzzle(spec.rowHints, spec.columnHints)
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
