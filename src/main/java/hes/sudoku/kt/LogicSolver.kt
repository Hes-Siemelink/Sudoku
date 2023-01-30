package hes.sudoku.kt

class LogicSolver @JvmOverloads constructor(private val puzzle: Puzzle, private val printer: Printer = Printer(puzzle)) {
    fun solve() {
        while (fillNumbers() || eliminateCandidates());
    }

    fun fillNumbers(): Boolean {
        var moves: MutableSet<Move>
        do {
            moves = LinkedHashSet()
            moves.addAll(findUniqueCandidatesInGroup())
            moves.addAll(findUniqueCandidates())
            applyMoves(moves)
        } while (!moves.isEmpty())
        printer.println("No more moves found.")
        return !moves.isEmpty()
    }

    fun eliminateCandidates(): Boolean {
        var eliminations: MutableSet<Move>
        do {
            eliminations = LinkedHashSet()
            eliminations.addAll(eliminateCrossGroupDependencies())
            eliminations.addAll(eliminateBasedOnUniqueSetsInGroup())
            applyEliminations(eliminations)
        } while (!eliminations.isEmpty())
        printer.println("No more eliminations found.")
        return !eliminations.isEmpty()
    }

    private fun applyMoves(moves: Set<Move>) {
        for (move in moves) {
            printer.println(move)
            puzzle.apply(move)
        }
    }

    private fun applyEliminations(eliminations: Set<Move>) {
        if (!eliminations.isEmpty()) {
            printer.println("\nEliminating candidates:")
        }
        for (elimination in eliminations) {
            printer.println(elimination)
            val cell = puzzle.getCell(elimination!!.cell.column, elimination.cell.row)
            cell.eliminate(elimination.number)
        }
    }

    //
    // Find candidates
    //
    private fun findUniqueCandidates(): Set<Move> {
        val moves: MutableSet<Move> = LinkedHashSet()
        for (cell in puzzle.getCells()) {
            if (cell.getCandidates().size == 1) {
                moves.add(Move(cell, getCandidate(cell), "No other candidate for this cell"))
            }
        }
        return moves
    }

    fun getCandidate(cell: Cell): Int {
        return cell.getCandidates().iterator().next()
    }

    private fun findUniqueCandidatesInGroup(): Set<Move> {
        val candidates: MutableSet<Move> = LinkedHashSet()
        for (group in puzzle.getGroups()) {
            candidates.addAll(findGhostCandidates(group))
        }
        return candidates
    }

    fun findGhostCandidates(group: Group): Set<Move> {
        val candidates: MutableSet<Move> = LinkedHashSet()
        for (number in 1..9) {
            var nrOfCandidates = 0
            for (cell in group.getCells()) {
                if (cell.hasCandidate(number)) {
                    nrOfCandidates++
                }
            }
            if (nrOfCandidates == 1) {
                val cell = getFirstGhost(number, group.getCells())
                candidates.add(Move(cell, number, String.format("Only possibility in %s", group)))
            }
        }
        return candidates
    }

    private fun getFirstGhost(number: Int, cells: Collection<Cell>): Cell {
        for (cell in cells) {
            if (cell.hasCandidate(number)) {
                return cell
            }
        }
        throw IllegalStateException(String.format("No ghost candidate %s in %s", number, cells))
    }

    //
    // Eliminate
    //
    private fun eliminateCrossGroupDependencies(): Set<Move> {
        val eliminations: MutableSet<Move> = LinkedHashSet()
        for (group in puzzle.getGroups()) {
            for (number in 1..9) {
                eliminateCrossGroupDependencies(group, number, puzzle.boxes, eliminations)
                eliminateCrossGroupDependencies(group, number, puzzle.rows, eliminations)
                eliminateCrossGroupDependencies(group, number, puzzle.columns, eliminations)
            }
        }
        return eliminations
    }

    private fun eliminateCrossGroupDependencies(group: Group, number: Int, ignore: List<Group>, eliminations: MutableSet<Move>) {
        val overlapping = getOverlappingGroupsWithSameNumber(group, number)
        overlapping.removeAll(ignore)
        if (overlapping.size == 1) {
            val other = overlapping.iterator().next()
            val cells: MutableSet<Cell> = LinkedHashSet(other.getCells())
            cells.removeAll(group.getCells())
            for (cell in cells) {
                if (!cell.hasCandidate(number)) {
                    continue
                }
                eliminations.add(Move(cell, number, String.format("Eliminating %s because it needs to be in %s, %s", number, group, other)))
            }
        }
    }

    private fun getOverlappingGroupsWithSameNumber(group: Group, number: Int): MutableSet<Group> {
        val overlapping: MutableSet<Group> = HashSet()
        for (cell in group.getCells()) {
            if (cell.hasCandidate(number)) {
                overlapping.addAll(cell.getGroups())
            }
        }
        overlapping.remove(group)
        return overlapping
    }

    //
    // Eliminate candidates in a group when there is a subset of n candidates occurring n times.
    // The elements of that particular set can't be candidates anywhere else in that group then.
    // For example, you have a row that have two places with {2,4} as candidates. Then another cell
    // with {2, 4, 6} should not have {2, 4}.
    //

    fun eliminateBasedOnUniqueSetsInGroup(): Set<Move> {
        val eliminations: MutableSet<Move> = LinkedHashSet()
        for (group in puzzle.getGroups()) {
            eliminateBasedOnUniqueSets(group, eliminations)
        }
        return eliminations
    }

    companion object {
        fun eliminateBasedOnUniqueSets(group: Group, eliminations: MutableSet<Move>) {
            val candidateSetCount: MutableMap<Set<Int>, Int> = LinkedHashMap()
            for (cell in group.getCells()) {
                add(candidateSetCount, cell.getCandidates())
            }
            for ((key, value) in candidateSetCount) {
                if (key.size == value) {
                    removeOtherDependencies(group, key, eliminations)
                }
            }
        }

        private fun removeOtherDependencies(group: Group, set: Set<Int>, eliminations: MutableSet<Move>) {
            for (cell in group.getCells()) {
                if (cell.getCandidates() != set) {
                    for (number in set) {
                        if (cell.hasCandidate(number)) {
                            eliminations.add(Move(cell, number, String.format("Eliminate %s from %s, because it is in a unique set %s elsewhere in %s", number, cell, set, group)))
                        }
                    }
                }
            }
        }

        private fun <T> add(count: MutableMap<T, Int>, something: T) {
            if (!count.containsKey(something)) {
                count[something] = 1
            } else {
                count[something] = count[something]!! + 1
            }
        }
    }
}