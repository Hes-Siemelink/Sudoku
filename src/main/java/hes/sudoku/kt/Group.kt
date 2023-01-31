package hes.sudoku.kt

class Group(
        private val name: String) {

    private val _cells: MutableSet<Cell> = LinkedHashSet(9)

    val cells: Set<Cell> = _cells

    fun addCell(cell: Cell) {
        _cells.add(cell)
        cell.add(this)
    }

    fun eliminate(number: Int?) {
        _cells.forEach { it.eliminate(number) }
    }

    override fun toString(): String {
        return name
    }
}