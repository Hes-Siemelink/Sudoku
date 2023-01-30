package hes.sudoku.kt

class Cell {
    val row: Int
    val column: Int
    private var number = 0

    var isKnown = false
        private set

    private val _candidates: MutableSet<Int> = LinkedHashSet(9)
    private val _groups: MutableSet<Group> = LinkedHashSet()

    val candidates: Set<Int> = _candidates
    val groups: Set<Group> = _groups

    constructor(row: Int, column: Int) {
        this.row = row
        this.column = column
        for (number in 1..9) {
            _candidates.add(number)
        }
    }

    constructor(original: Cell) {
        number = original.number
        isKnown = original.isKnown
        _candidates.addAll(original._candidates)
        row = original.row
        column = original.column
    }

    fun getNumber(): Int {
        return number
    }

    // For unit tests
    fun _setCandidates(candidates: MutableSet<Int>) {
        this._candidates.clear()
        this._candidates.addAll(candidates)
    }

    val name: String
        get() = String.format("Row %s column %s", row + 1, column + 1)

    fun add(group: Group) {
        _groups.add(group)
    }

    fun setNumber(number: Int) {
        this.number = number
        isKnown = true
        _candidates.clear()
    }

    fun eliminate(number: Int?) {
        _candidates.remove(number)
        check(!(!isKnown && _candidates.size == 0)) { String.format("No more candidates for %s after eliminating %s", toString(), number) }
    }

    fun text(): String {
        return if (isKnown) {
            "$number "
        } else ". "
    }

    fun allCandidatesAsString(): String {
        if (isKnown) {
            return String.format("        .")
        }
        
        val builder = StringBuilder(9)
        _candidates.forEach { builder.append(it) }

        return String.format("%9s", builder)
    }

    override fun toString(): String {
        return String.format("Row %s column %s", row + 1, column + 1)
    }
}