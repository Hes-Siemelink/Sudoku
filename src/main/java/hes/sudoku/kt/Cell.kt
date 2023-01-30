package hes.sudoku.kt

import java.util.*
import java.util.function.Consumer

class Cell {
    val row: Int
    val column: Int
    private var number = 0

    var isKnown = false
        private set

    private var candidates: MutableSet<Int> = LinkedHashSet(9)
    private val groups: MutableSet<Group> = LinkedHashSet()

    constructor(row: Int, column: Int) {
        this.row = row
        this.column = column
        for (number in 1..9) {
            candidates.add(number)
        }
    }

    constructor(original: Cell) {
        number = original.number
        isKnown = original.isKnown
        candidates.addAll(original.candidates)
        row = original.row
        column = original.column
    }

    fun getNumber(): Int {
        return number
    }

    fun getCandidates(): Set<Int> {
        return Collections.unmodifiableSet(candidates);
    }

    // For unit tests
    fun _setCandidates(candidates: MutableSet<Int>) {
        this.candidates = candidates
    }

    fun getGroups(): Set<Group> {
        return Collections.unmodifiableSet(groups)
    }

    val name: String
        get() = String.format("Row %s column %s", row + 1, column + 1)

    fun add(group: Group) {
        groups.add(group)
    }

    fun setNumber(number: Int) {
        this.number = number
        isKnown = true
        candidates = mutableSetOf()
    }

    fun eliminate(number: Int?) {
        candidates.remove(number)
        check(!(!isKnown && candidates.size == 0)) { String.format("No more candidates for %s after eliminating %s", toString(), number) }
    }

    fun removeCandidates(invalidCandidates: Set<Int?>?) {
        candidates.removeAll(invalidCandidates!!)
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
        candidates.forEach(Consumer { obj: Int? -> builder.append(obj) })
        return String.format("%9s", builder)
    }

    override fun toString(): String {
        return String.format("Row %s column %s", row + 1, column + 1)
    }

    fun hasCandidate(number: Int?): Boolean {
        return candidates.contains(number)
    }
}