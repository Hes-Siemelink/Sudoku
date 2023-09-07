package hes.nonogram

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.opentest4j.AssertionFailedError

class PuzzleTest {

    @Test
    fun `solve simple puzzle`() {
        val puzzle = nonogram {
            row(1)
            row()

            column(1)
            column()
        }

        val solution = puzzle.solve() ?: throw AssertionFailedError("Puzzle should have a solution")

        solution.print()

        assertTrue(solution.valid, "Puzzle should be in valid state.")
        assertTrue(solution.solved, "Puzzle should be solved.")

        assertEquals("*.", toString(solution.rows[0].cells).replace('-', '.'))
        assertEquals("..", toString(solution.rows[1].cells).replace('-', '.'))
    }

    @Test
    fun `solve 3x3 puzzle`() {
        val puzzle = nonogram {
            row(1)
            row(1, 1)
            row(1)

            column(1)
            column(1, 1)
            column(1)
        }

        val solution = puzzle.solve() ?: throw AssertionFailedError("Puzzle should have a solution")

        solution.print()

        assertTrue(solution.valid, "Puzzle should be in valid state.")
        assertTrue(solution.solved, "Puzzle should be solved.")
    }

    @Test
    fun `solve 4x4 puzzle`() {
        val puzzle = nonogram {
            row(1)
            row(3)
            row(3)
            row(1, 1)

            column(1)
            column(3)
            column(2)
            column(3)
        }

        val solution = puzzle.solve() ?: throw AssertionFailedError("Puzzle should have a solution")

        solution.print()

        assertTrue(solution.valid, "Puzzle should be in valid state.")
        assertTrue(solution.solved, "Puzzle should be solved.")
    }

    @Test
    fun `solve another puzzle`() {
        val puzzle = nonogram {
            row(3)
            row(1, 1, 1)
            row(3)
            row(1, 1)
            row(1, 1)

            column(1, 1)
            column(1, 2)
            column(3)
            column(1, 2)
            column(1, 1)
        }

        val solution = puzzle.solve() ?: throw AssertionFailedError("Puzzle should have a solution")

        println(solution)

        assertTrue(solution.valid, "Puzzle should be in valid state.")
        assertTrue(solution.solved, "Puzzle should be solved.")

//        assertEquals("*.", toString(puzzle.rows[0].cells).replace('-', '.'))
//        assertEquals("..", toString(puzzle.rows[1].cells).replace('-', '.'))
    }

    // Another one to try:
    // https://activityworkshop.net/puzzlesgames/nonograms/puzzle1.html


}