package hes.sudoku.kt

class Puzzle {

    private val _cells: MutableList<Cell> = ArrayList(81)
    private val _groups: MutableList<Group> = ArrayList(27)

    // Derived properties

    val cells: List<Cell>
        get() = _cells
    val groups: List<Group>
        get() = _groups
    val boxes: List<Group>
        get() = _groups.subList(0, 9)
    val rows: List<Group>
        get() = _groups.subList(9, 18)
    val columns: List<Group>
        get() = _groups.subList(18, 27)

    //
    // Init
    //

    constructor() : this(true)

    internal constructor(init: Boolean) {
        if (init) {
            initCells()
            initGroups()
        }
    }

    constructor(original: Puzzle) {
        for (cell in original._cells) {
            _cells.add(Cell(cell))
        }
        initGroups()
    }

    private fun initCells() {
        for (i in 0..80) {
            _cells.add(Cell(i / 9, i % 9))
        }
    }

    private fun initGroups() {

        // Boxes
        _groups.add(initBox("Box 1", 0, 0))
        _groups.add(initBox("Box 2", 3, 0))
        _groups.add(initBox("Box 3", 6, 0))
        _groups.add(initBox("Box 4", 0, 3))
        _groups.add(initBox("Box 5", 3, 3))
        _groups.add(initBox("Box 6", 6, 3))
        _groups.add(initBox("Box 7", 0, 6))
        _groups.add(initBox("Box 8", 3, 6))
        _groups.add(initBox("Box 9", 6, 6))

        // Rows
        for (row in 0..8) {
            _groups.add(initRow("Row " + (row + 1), row))
        }

        // Columns
        for (column in 0..8) {
            _groups.add(initColumn("Col " + (column + 1), column))
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
        return _cells[y * 9 + x]
    }

    fun apply(move: Move) {
        apply(getCell(move.cell.column, move.cell.row), move.number)
    }

    fun apply(cell: Cell, number: Int) {
        cell.setNumber(number)

        // Update cells
        for (i in _groups.indices) {
            if (_groups[i].cells.contains(cell)) {
                _groups[i].eliminate(number)
            }
        }
    }

    fun isSolved(): Boolean {
        for (cell in _cells) {
            if (!cell.known) {
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