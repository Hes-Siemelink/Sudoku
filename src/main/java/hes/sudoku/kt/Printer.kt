package hes.sudoku.kt

import java.io.PrintStream

open class Printer @JvmOverloads constructor(private val puzzle: Puzzle, val out: PrintStream = System.out) {

    private var start = System.currentTimeMillis()

    open fun println(message: Any, vararg args: Any?) {
        if (args == null) {
            out.println(message)
        } else {
            out.format(message.toString(), *args)
            out.println()
        }
    }

    open fun printStart() {
        printPuzzle()
        start = System.currentTimeMillis()
    }

    open fun printEnd() {
        val duration = System.currentTimeMillis() - start
        out.println("\nSolution:")
        printPuzzle()
        System.out.format("Solved in %s ms.\n", duration)
    }

    open fun printPuzzle() {
        out.println("+-------+-------+-------+")
        for (i in puzzle!!.getCells().indices) {
            if (i % 9 == 0) {
                out.print("| ")
            }
            out.print(puzzle.getCells()[i].text())

            // Block divider
            val next = i + 1
            if (next % 3 == 0) {
                out.print("| ")
            }

            // New line
            if (next % 9 == 0) {
                out.println()
            }

            // Block line
            if (next % 27 == 0) {
                out.println("+-------+-------+-------+")
            }
        }
    }

    open fun printAllCandidates() {
        out.println("+----------------------------+-----------------------------+-----------------------------+")
        for (i in puzzle!!.getCells().indices) {
            if (i % 9 == 0) {
                out.print("|")
            }
            out.print(puzzle.getCells()[i].allCandidatesAsString())

            // Block divider
            val next = i + 1
            if (next % 3 == 0) {
                out.print(" | ")
            }

            // New line
            if (next % 9 == 0) {
                out.println()
            }

            // Block line
            if (next % 27 == 0) {
                out.println("+----------------------------+-----------------------------+-----------------------------+")
            }
        }
    }

    open fun silentPrinter(): Printer {
        return object : Printer(Puzzle(false)) {
            override fun println(message: Any, vararg args: Any?) {}
            override fun printStart() {}
            override fun printEnd() {}
            override fun printPuzzle() {}
            override fun printAllCandidates() {}
        }
    }
}