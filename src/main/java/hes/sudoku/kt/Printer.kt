package hes.sudoku.kt

import java.io.PrintStream

interface Printer {

    fun println(message: Any)

    fun printStart()

    fun printEnd()

    fun printPuzzle()

    fun printAllCandidates()
}

class DefaultPrinter(
        private val puzzle: Puzzle,
        private val out: PrintStream = System.out) : Printer {

    private var start = System.currentTimeMillis()

    override fun println(message: Any) {
        out.println(message)
    }

    override fun printStart() {
        printPuzzle()
        printAllCandidates()
        start = System.currentTimeMillis()
    }

    override fun printEnd() {
        val duration = System.currentTimeMillis() - start
        out.println("\nSolution:")
        printPuzzle()
        System.out.format("Solved in %s ms.\n", duration)
    }

    override fun printPuzzle() {
        printBox("+-------+-------+-------+") {
            it.text
        }
    }

    override fun printAllCandidates() {
        printBox("+-------------------------------+-------------------------------+-------------------------------+") {
            it.candidatesAsString
        }
    }

    private fun printBox(divider: String, getCellContent: (Cell) -> String) {

        out.println(divider)

        for (i in puzzle.cells.indices) {

            // Left edge
            if (i % 9 == 0) {
                out.print("| ")
            }

            // Content
            out.print(getCellContent(puzzle.cells[i]))
            out.print(" ")

            // Vertical block divider
            val next = i + 1
            if (next % 3 == 0) {
                out.print("| ")
            }

            // New line
            if (next % 9 == 0) {
                out.println()
            }

            // Horizontal block divider
            if (next % 27 == 0) {
                out.println(divider)
            }
        }
    }
}

val SILENT_PRINTER = object : Printer {
    override fun println(message: Any) {}
    override fun printStart() {}
    override fun printEnd() {}
    override fun printPuzzle() {}
    override fun printAllCandidates() {}
}
