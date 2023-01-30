package hes.sudoku.kt

import java.util.*
import java.util.function.Consumer

class Group(private val name: String) {
    private val cells: MutableSet<Cell> = LinkedHashSet(9)
    fun getCells(): Set<Cell> {
        return Collections.unmodifiableSet(cells)
    }

    fun addCell(cell: Cell) {
        cells.add(cell)
        cell.add(this)
    }

    fun eliminate(number: Int?) {
        cells.forEach(Consumer { c: Cell -> c.eliminate(number) })
    }

    fun candidates(): Int {
        var candidates = 9
        for (cell in cells) {
            if (cell.isKnown) {
                candidates--
            }
        }
        return candidates
    }

    override fun toString(): String {
        return name
    }
}