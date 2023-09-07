package hes.nonogram

import hes.nonogram.Cell.State.EMPTY
import hes.nonogram.Cell.State.FILLED

class Line(val hints: List<Int>, val cells: List<Cell>) {

    constructor(hints: List<Int>, s: String) : this(hints, toCells(s))

    fun copy(): Line {
        return Line(hints, cells.map { it.copy() })
    }

    val valid: Boolean
        get() {
            for (i in hints.indices) {
                val hint = hints[i]
                val left = lengthOf(hints.subList(0, i)) + emptyLeft()
                val right = lengthOf(hints.subList(i + 1, hints.size)) + emptyRight()

                if (left > cells.size - right) {
                    return false
                }

                try {
                    val segment = LineSegment(hint, cells.subList(left, cells.size - right))

                    // Minimal left side should end with empty space
                    if (left > 0 && cells[left - 1].state == FILLED) {
                        return false
                    }

                    // Minimal right side should end with empty space
                    if (right > 0 && cells[cells.size - right].state == FILLED) {
                        return false
                    }

                    // Check if segment where this hint should lie is valid
                    if (!segment.valid) {
                        return false
                    }
                } catch (e: IllegalArgumentException) {
                    println("Segment: $this with hints $hints. Left: $left; right: $right")
                    throw e
                }
            }
            return true
        }

    private fun emptyLeft(): Int {
        var total = 0
        for (cell in cells) {
            if (cell.state != EMPTY) {
                return total
            }
            total++
        }
        return total
    }

    private fun emptyRight(): Int {
        var total = 0
        for (cell in cells.reversed()) {
            if (cell.state != EMPTY) {
                return total
            }
            total++
        }
        return total
    }

    val solved: Boolean
        get() {
            val counted = mutableListOf<Int>()
            var count = 0
            for (cell in cells) {
                if (cell.state == FILLED) {
                    count++
                } else {
                    if (count > 0) {
                        counted.add(count)
                    }
                    count = 0
                }
            }
            if (count > 0) {
                counted.add(count)
            }

            return counted == hints
        }

    override fun toString(): String {
        return toString(cells)
    }
}

fun lengthOf(hints: List<Int>): Int {
    var total = 0
    for (hint in hints) {
        total += hint + 1
    }
    return total
}

class LineSegment(val hint: Int, val cells: List<Cell>) {

    constructor(hint: Int, s: String) : this(hint, toCells(s))

    val valid: Boolean
        get() {
            for (i in 0..cells.size - hint) {
                if (isValidAt(i)) {
                    return true
                }
            }
            return false
        }

    @Deprecated("Not needed")
    val solved: Boolean
        get() {
            for (i in 0..cells.size - hint) {
                if (isSolvedAt(i)) {
                    return true
                }
            }
            return false
        }

    private fun isValidAt(position: Int): Boolean {
        for (i in cells.indices) {
            if (i >= position && i < position + hint) {
                if (!cells[i].state.couldBeFilled) {
                    return false
                }
            } else {
                if (cells[i].state == FILLED)
                    return false
            }
        }
        return true
    }

    private fun isSolvedAt(position: Int): Boolean {
        for (i in cells.indices) {
            if (i >= position && i < position + hint) {
                if (cells[i].state != FILLED)
                    return false
            } else {
                if (cells[i].state == FILLED) {
                    return false
                }
            }
        }
        return true
    }

    override fun toString(): String {
        return "${toString(cells)} ($hint)"
    }
}