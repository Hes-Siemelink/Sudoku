package hes.sudoku.kt

object Main {

    fun solve(puzzle: Puzzle) {
        val printer = Printer(puzzle)
        val solver = LogicSolver(puzzle)

        // Print
        printer.printStart()

        // Solve with logic
        printer.println("\nMoves:")
        solver.solve()
        if (puzzle.isSolved()) {
            printer.printEnd()
            return
        }

        // Solve recursively
        printer.println("\nSolving recursively")
        val moves: Collection<Move> = RecursiveSolver(puzzle).solve()
        printer.println("\nMoves:")
        moves.forEach { printer.println(it) }
        moves.forEach { puzzle.apply(it) }
        solver.fillNumbers()
        printer.printEnd()
    }
}

fun main() {
    val puzzle = Puzzle.parse(Samples.HARD)
    Main.solve(puzzle)
}
