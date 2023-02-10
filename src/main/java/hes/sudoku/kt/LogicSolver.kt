package hes.sudoku.kt

class LogicSolver(
        val puzzle: Puzzle,
        private val printer: Printer = DefaultPrinter(puzzle)) {

    fun solve() {
        while (fillNumbers() || eliminateCandidates());
    }

    fun fillNumbers(): Boolean {
        var moves: MutableSet<Move>
        do {
            moves = mutableSetOf()
            moves.addAll(findUniqueCandidatesInGroup())
            moves.addAll(findUniqueCandidates())
            applyMoves(moves)
        } while (!moves.isEmpty())

        printer.println("No more moves found.")

        return !moves.isEmpty()
    }

    private fun eliminateCandidates(): Boolean {
        var eliminations: MutableSet<Move>
        do {
            eliminations = mutableSetOf()
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

        for (removal in eliminations) {
            printer.println(removal)
            puzzle.getCell(removal.cell.column, removal.cell.row)
                    .eliminate(removal.number)
        }
    }

    //
    // Find candidates
    //

    private fun findUniqueCandidates(): Set<Move> {
        return puzzle.cells.filter { it.candidates.size == 1 }
                .mapTo(mutableSetOf()) { cell ->
                    Move(cell, getCandidate(cell)).withDescription("No other candidate for this cell")
        }
    }

    fun getCandidate(cell: Cell): Int {
        return cell.candidates.first()
    }

    private fun findUniqueCandidatesInGroup(): Set<Move> {
        val candidates = mutableSetOf<Move>()
        for (group in puzzle.groups) {
            candidates.addAll(findGhostCandidates(group))
        }
        return candidates
    }

    fun findGhostCandidates(group: Group): Set<Move> {
        val candidates = mutableSetOf<Move>()
        for (number in 1..9) {
            var nrOfCandidates = 0
            for (cell in group.cells) {
                if (number in cell.candidates) {
                    nrOfCandidates++
                }
            }
            if (nrOfCandidates == 1) {
                val cell = getFirstGhost(number, group.cells)
                candidates.add(Move(cell, number).withDescription("Only possibility in $group"))
            }
        }
        return candidates
    }

    private fun getFirstGhost(number: Int, cells: Collection<Cell>): Cell {
        return cells.filter { number in it.candidates }.first()
    }

    //
    // Eliminate
    //

    private fun eliminateCrossGroupDependencies(): Set<Move> {
        val eliminations = mutableSetOf<Move>()
        for (group in puzzle.groups) {
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
            val cells = other.cells.toMutableSet()
            cells.removeAll(group.cells)
            for (cell in cells.filter { number in it.candidates }) {
                eliminations.add(Move(cell, number).withDescription("Eliminating $number because it needs to be in $group, $other"))
            }
        }
    }

    private fun getOverlappingGroupsWithSameNumber(group: Group, number: Int): MutableSet<Group> {
        val overlapping = mutableSetOf<Group>()
        for (cell in group.cells.filter { number in it.candidates }) {
            overlapping.addAll(cell.groups)
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
        for (group in puzzle.groups) {
            eliminateBasedOnUniqueSets(group, eliminations)
        }

        return eliminations
    }
}

fun eliminateBasedOnUniqueSets(group: Group, eliminations: MutableSet<Move>) {
    val candidateSetCount: MutableMap<Set<Int>, Int> = LinkedHashMap()
    for (cell in group.cells) {
        add(candidateSetCount, cell.candidates)
    }

    for ((key, value) in candidateSetCount) {
        if (key.size == value) {
            removeOtherDependencies(group, key, eliminations)
        }
    }
}

private fun removeOtherDependencies(group: Group, set: Set<Int>, eliminations: MutableSet<Move>) {
    for (cell in group.cells.filter { cell -> cell.candidates != set }) {
        for (number in set.filter { number -> number in cell.candidates }) {
            eliminations.add(Move(cell, number).withDescription("Eliminate $number from $cell, because it is in a unique set $set elsewhere in $group"))
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
