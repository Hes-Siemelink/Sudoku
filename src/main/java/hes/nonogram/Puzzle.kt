package hes.nonogram

import hes.nonogram.Cell.State.*

class Puzzle(
    private val rowHints: List<List<Int>>,
    private val columnHints: List<List<Int>>,
    private val cells: List<Cell> = mutableListOf()
) {

    private val width: Int = columnHints.size
    private val height: Int = rowHints.size

    val rows: List<Line>
    val columns: List<Line>

    init {

        if (cells.isEmpty() && cells is MutableList) {
            for (i in 1..width * height) {
                cells.add(Cell())
            }
        }

        rows = rowHints.mapIndexed { index, hints -> Line(hints, row(index)) }
        columns = columnHints.mapIndexed { index, hints -> Line(hints, column(index)) }
    }

    private fun row(index: Int): List<Cell> {
        return cells.subList(index * width, (index + 1) * width)
    }

    private fun column(index: Int): List<Cell> {
        val cellColumn = mutableListOf<Cell>()
        for (row in 0 until height) {
            cellColumn.add(cells[width * row + index])
        }
        return cellColumn
    }

    fun cell(row: Int, column: Int): Cell {
        return rows[row].cells[column]
    }

    val valid: Boolean
        get() = rows.all { it.valid } && columns.all { it.valid }

    val solved: Boolean
        get() = rows.all { it.solved } && columns.all { it.solved }

    fun copy(): Puzzle {
        val cellsCopy = cells.map { it.copy() }

        return Puzzle(rowHints, columnHints, cellsCopy)
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

                if (!clone.valid) {
                    this.rows[r].cells[c].state = EMPTY
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
        return Puzzle(rowHints, columnHints)
    }
}

fun nonogram(init: NonogramSpec.() -> Unit): Puzzle {
    val spec = NonogramSpec()
    spec.init()

    return spec.toPuzzle(spec.rowHints, spec.columnHints)
}
