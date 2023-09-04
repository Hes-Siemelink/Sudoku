package hes.nonogram

class Cell(var state: State = State.UNKNOWN) {

    constructor(char: Char) : this(State.from(char))

    infix fun and(other: Cell): Boolean {
        return state == other.state
    }

    override fun toString(): String {
        return state.toString()
    }

    enum class State(private val value: Char) {

        UNKNOWN('.'), EMPTY('-'), FILLED('*');

        val couldBeFilled: Boolean
            get() {
                return this == FILLED || this == UNKNOWN
            }

        val couldbeEmpty: Boolean
            get() {
                return this == EMPTY || this == UNKNOWN
            }

        override fun toString(): String {
            return value.toString()
        }

        companion object {
            fun from(char: Char): State {
                State.values().forEach {
                    if (it.value == char) {
                        return it
                    }
                }
                throw IllegalArgumentException("The character '$char' does not have a state.")
            }
        }
    }
}


fun shouldBeEmpty(cells: List<Cell>): Boolean {
    return cells.all { it.state.couldbeEmpty }
}

fun toCells(s: String): MutableList<Cell> {
    val cells = mutableListOf<Cell>()
    for (char: Char in s) {
        cells.add(Cell(char))
    }
    return cells
}

fun toString(cells: List<Cell>): String {
    return buildString {
        cells.forEach { append(it.toString()) }
    }
}
