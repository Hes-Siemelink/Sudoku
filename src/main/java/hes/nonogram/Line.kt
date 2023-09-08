package hes.nonogram

import hes.nonogram.Cell.State.*

class Line(val hints: List<Int>, val cells: List<Cell>) {

    constructor(hints: List<Int>, s: String) : this(hints, toCells(s))

    fun copy(): Line {
        return Line(hints, cells.map { it.copy() })
    }

    fun filled(): Int {
        return cells.count { it.state == FILLED }
    }

    val valid: Boolean
        get() {
            if (filled() > hints.sumOf { it }) {
                return false
            }
            if (!knownHintsOK()) {
                return false
            }

            for (i in hints.indices) {
                val hint = hints[i]
                var left = lengthOf(hints.subList(0, i)) + emptyLeft()
                val right = lengthOf(hints.subList(i + 1, hints.size)) + emptyRight()

                // Hack to detect cases like .*. with [1, 1]
                if (left > 0 && cells[left - 1].state == FILLED) {
                    left += 1
                }

                if (left > cells.size - right) {
                    return false
                }

                try {
                    val segment = LineSegment(hint, cells.subList(left, cells.size - right))

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

    private fun knownHintsOK(): Boolean {

        val filled = splitFilled()
        if (filled.size <= hints.size) {
            return (filled == hints.subList(0, filled.size))
        }
        return false
    }

    private fun splitFilled(): List<Int> {
        val filled = mutableListOf<Int>()
        var count = 0
        for (cell in cells) {
            if (cell.state == EMPTY) {
                if (count > 0) {
                    filled.add(count)
                }
                count = 0
            }
            if (cell.state == FILLED) {
                count++
            }
            if (cell.state == UNKNOWN) {
                return filled
            }
        }
        if (count > 0) {
            filled.add(count)
        }
        return filled
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

    private fun isValidAt(position: Int): Boolean {
        for (i in position until position + hint) {
            if (!cells[i].state.couldBeFilled) {
                return false
            }
        }

        if (position > 0 && cells[position - 1].state == FILLED) {
            return false
        }

        // ..* position 1 hint 1
        if (position + hint < cells.size && cells[position + hint].state == FILLED) {
            return false
        }

        return true
    }
//    private fun isValidAt(position: Int): Boolean {
//        for (i in cells.indices) {
//            if (i >= position && i < position + hint) {
//                if (!cells[i].state.couldBeFilled) {
//                    return false
//                }
//            } else {
//                if (cells[i].state == FILLED)
//                    return false
//            }
//        }
//        return true
//    }

    override fun toString(): String {
        return "${toString(cells)} ($hint)"
    }
}