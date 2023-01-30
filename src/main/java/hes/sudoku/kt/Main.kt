package hes.sudoku.kt

import java.util.function.Consumer

object Main {

    fun solve(puzzle: Puzzle) {
        val printer = Printer(puzzle)
        val solver = LogicSolver(puzzle)

        // Print
        printer.printStart()

        // Solve with logic
        printer.println("\nMoves:")
        solver.solve()
        if (puzzle.isSolved) {
            printer.printEnd()
            return
        }

        // Solve recursively
        printer.println("\nSolving recursively")
        val moves: Collection<Move> = RecursiveSolver(puzzle).solve()
        printer.println("\nMoves:")
        moves.forEach(Consumer { message: Move -> printer.println(message) })
        moves.forEach(Consumer { move: Move -> puzzle.apply(move) })
        solver.fillNumbers()
        printer.printEnd()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val puzzle = Puzzle.parse(Samples.HARD)
        solve(puzzle)
    }
}