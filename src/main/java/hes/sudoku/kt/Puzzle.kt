package hes.sudoku.kt

import java.util.*

class Puzzle {
    private val cells: MutableList<Cell> = ArrayList(81)
    private val groups: MutableList<Group> = ArrayList(27)

    constructor() : this(true)

    internal constructor(init: Boolean) {
        if (init) {
            initCells()
            initGroups()
        }
    }

    constructor(original: Puzzle) {
        for (cell in original.getCells()) {
            cells.add(Cell(cell))
        }
        initGroups()
    }

    fun getCells(): List<Cell> {
        return Collections.unmodifiableList(cells)
    }

    fun getGroups(): List<Group> {
        return Collections.unmodifiableList(groups)
    }

    val boxes: List<Group>
        get() = groups.subList(0, 9)
    val rows: List<Group>
        get() = groups.subList(9, 18)
    val columns: List<Group>
        get() = groups.subList(18, 27)

    private fun initCells() {
        for (i in 0..80) {
            cells.add(Cell(i / 9, i % 9))
        }
    }

    private fun initGroups() {
        val group = 0

        // Boxes
        groups.add(initBox("Box 1", 0, 0))
        groups.add(initBox("Box 2", 3, 0))
        groups.add(initBox("Box 3", 6, 0))
        groups.add(initBox("Box 4", 0, 3))
        groups.add(initBox("Box 5", 3, 3))
        groups.add(initBox("Box 6", 6, 3))
        groups.add(initBox("Box 7", 0, 6))
        groups.add(initBox("Box 8", 3, 6))
        groups.add(initBox("Box 9", 6, 6))

        // Rows
        for (row in 0..8) {
            groups.add(initRow("Row " + (row + 1), row))
        }

        // Columns
        for (column in 0..8) {
            groups.add(initColumn("Col " + (column + 1), column))
        }
    }

    private fun initBox(name: String, startX: Int, startY: Int): Group {
        val box = Group(name)
        box.addCell(getCell(startX + 0, startY + 0))
        box.addCell(getCell(startX + 1, startY + 0))
        box.addCell(getCell(startX + 2, startY + 0))
        box.addCell(getCell(startX + 0, startY + 1))
        box.addCell(getCell(startX + 1, startY + 1))
        box.addCell(getCell(startX + 2, startY + 1))
        box.addCell(getCell(startX + 0, startY + 2))
        box.addCell(getCell(startX + 1, startY + 2))
        box.addCell(getCell(startX + 2, startY + 2))

        return box
    }

    private fun initRow(name: String, y: Int): Group {
        val row = Group(name)
        for (i in 0..8) {
            row.addCell(getCell(i, y))
        }
        return row
    }

    private fun initColumn(name: String, x: Int): Group {
        val column = Group(name)
        for (i in 0..8) {
            column.addCell(getCell(x, i))
        }
        return column
    }

    //
    // Set numbers
    //

    fun getCell(x: Int, y: Int): Cell {
        return cells[y * 9 + x]
    }

    fun apply(move: Move) {
        apply(getCell(move.cell.column, move.cell.row), move.number)
    }

    fun apply(cell: Cell, number: Int) {
        cell.setNumber(number)

        // Update cells
        for (i in groups.indices) {
            if (groups[i].getCells().contains(cell)) {
                groups[i].eliminate(number)
            }
        }
    }

    val isSolved: Boolean
        get() {
            for (cell in cells) {
                if (!cell.isKnown) {
                    return false
                }
            }
            return true
        }

    //
    // Parse
    //

    companion object {
        fun parse(input: String): Puzzle {
            val puzzle = Puzzle()
            var i = 0
            for (c in input.toCharArray()) {
                if (Character.isWhitespace(c)) {
                    continue
                }
                if (Character.isDigit(c)) {
                    puzzle.apply(puzzle.getCell(i % 9, i / 9), Integer.valueOf(c.toString()))
                }
                i++
            }
            return puzzle
        }
    }
}