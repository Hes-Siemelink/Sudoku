package hes.sudoku.kt

class Cell {

    val row: Int
    val column: Int
    var number = 0
        private set

    private val _candidates: MutableSet<Int> = LinkedHashSet(9)
    private val _groups: MutableSet<Group> = LinkedHashSet()

    // Derived properties

    val known: Boolean
        get() = number != 0

    val candidates: Set<Int> = _candidates
    val groups: Set<Group> = _groups

    val name: String
        get() = String.format("Row %s column %s", row + 1, column + 1)

    val text: String
        get() = if (known) "$number " else ". "

    val candidatesAsString: String
        get() {
            if (known) return String.format("        .")

            val builder = StringBuilder(9)
            _candidates.forEach { builder.append(it) }

            return String.format("%9s", builder)
        }

    override fun toString(): String {
        return name
    }

    //
    // Init
    //

    constructor(row: Int, column: Int) {
        this.row = row
        this.column = column

        for (number in 1..9) _candidates.add(number)
    }

    constructor(original: Cell) {
        number = original.number
        _candidates.addAll(original._candidates)
        row = original.row
        column = original.column
    }

    fun add(group: Group) {
        _groups.add(group)
    }

    // For unit tests
    internal fun replaceCandidates(candidates: Set<Int>): Cell {
        this._candidates.clear()
        this._candidates.addAll(candidates)

        return this
    }

    //
    // Methods
    //

    fun setNumber(number: Int) {
        this.number = number
        _candidates.clear()
    }

    fun eliminate(number: Int?) {
        _candidates.remove(number)
        check(!(!known && _candidates.size == 0)) { String.format("No more candidates for %s after eliminating %s", toString(), number) }
    }


}