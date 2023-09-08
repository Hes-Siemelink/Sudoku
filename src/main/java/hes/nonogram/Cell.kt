package hes.nonogram

class Cell(var state: State = State.UNKNOWN) {

    constructor(char: Char) : this(State.from(char))

    fun copy(): Cell {
        return Cell(state)
    }

    override fun toString(): String {
        return state.toString()
    }
}

enum class State(private val value: Char) {

    UNKNOWN('.'), EMPTY('-'), FILLED('*');

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

        fun toString(states: List<State>): String {
            return buildString {
                states.forEach { append(it.toString()) }
            }
        }
    }
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

