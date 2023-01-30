package hes.sudoku.kt

class RecursiveSolver(private val puzzle: Puzzle) {

    private val printer: Printer

    init {
        printer = Printer(puzzle)
    }

    fun solve(): Set<Move> {
        val moves: MutableSet<Move> = LinkedHashSet()
        solve(moves)
        return moves
    }

    fun solve(moves: MutableSet<Move>): Boolean {
        if (puzzle.isSolved()) {
            return true
        }
        val candidates = possibleMoves
        for (candidate in candidates) {
            printer.println("Trying $candidate")
            val clonedPuzzle = Puzzle(puzzle)
            clonedPuzzle.apply(candidate)
            if (solve(clonedPuzzle, moves)) {
                moves.add(candidate)
                return true
            }
            printer.println("%s does not solve the puzzle", candidate)
        }
        return false
    }

    private fun solve(puzzle: Puzzle, candidates: MutableSet<Move>): Boolean {
        val solver = LogicSolver(puzzle, printer.silentPrinter())
        solver.fillNumbers()
        if (puzzle.isSolved()) {
            printer.println("Puzzle solved recursively.")
            return true
        }
        Printer(puzzle).printPuzzle()
        printer.println("Puzzle not solved. Trying recursively.")
        return RecursiveSolver(puzzle).solve(candidates)
    }

    private val possibleMoves: Collection<Move>
        private get() {
            val moves = ArrayList<Move>()
            for (cell in puzzle.getCells()) {
                for (number in cell.candidates) {
                    moves.add(Move(cell, number, "Recursive guess out of " + cell.candidates.size))
                }
            }
            moves.sortWith(CellCandidateComparator())
            return moves
        }

    private class CellCandidateComparator : Comparator<Move> {
        override fun compare(o1: Move, o2: Move): Int {
            return o1.cell.candidates.size - o2.cell.candidates.size
        }
    }
}