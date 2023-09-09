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

        assertTrue(solution.isValid(), "Puzzle should be in valid state.")
        assertTrue(solution.isSolved(), "Puzzle should be solved.")

        assertEquals("*.", Cell.toString(solution.rows[0].cells).replace('-', '.'))
        assertEquals("..", Cell.toString(solution.rows[1].cells).replace('-', '.'))
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

        assertTrue(solution.isValid(), "Puzzle should be in valid state.")
        assertTrue(solution.isSolved(), "Puzzle should be solved.")
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

        assertTrue(solution.isValid(), "Puzzle should be in valid state.")
        assertTrue(solution.isSolved(), "Puzzle should be solved.")
    }

    @Test
    fun `solve 5x5 puzzle`() {
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

        solution.print()

        assertTrue(solution.isValid(), "Puzzle should be in valid state.")
        assertTrue(solution.isSolved(), "Puzzle should be solved.")
    }

    @Test
    fun `solve 20x10 puzzle`() {
        val puzzle = nonogram {
            row(3, 3, 2)
            row(5, 3, 2)
            row(2, 2, 2, 2)
            row(2, 2, 2, 2)
            row(3, 2, 2, 1, 2)

            row(7, 3, 3, 3)
            row(7, 10)
            row(3, 3, 9)
            row(2, 2, 3, 3)
            row(2, 3, 2, 1)

            column(3)
            column(6)
            column(7)
            column(7)
            column(2, 2)

            column(3, 2)
            column(7)
            column(1, 7)
            column(2, 3)
            column(6, 1)

            column(7)
            column(5)
            column(4)
            column(4)
            column(4)

            column(4)
            column(4)
            column(4)
            column(8)
            column(7)
        }

        val solution = puzzle.solve() ?: throw AssertionFailedError("Puzzle should have a solution")

        solution.print()

        assertTrue(solution.isValid(), "Puzzle should be in valid state.")
        assertTrue(solution.isSolved(), "Puzzle should be solved.")
    }

    @Test
    fun `solve 15x15 puzzle`() {
        val puzzle = nonogram {
            row(2, 1)
            row(2, 2)
            row(4)
            row(3)
            row(5)

            row(7, 2)
            row(2, 8, 1)
            row(4, 3, 1)
            row(3, 2, 1)
            row(3, 1)

            row(3, 1)
            row(4)
            row(3, 1)
            row(3, 3)
            row(2, 4)

            column(2)
            column(3)
            column(3)
            column(5)
            column(2, 3)

            column(2, 3)
            column(1, 2, 3)
            column(1, 10)
            column(1, 5, 2)
            column(7)

            column(5)
            column(4, 1)
            column(3, 2, 2)
            column(1, 3)
            column(3, 2)
        }

        val solution = puzzle.solve() ?: throw AssertionFailedError("Puzzle should have a solution")

        solution.print()

        assertTrue(solution.isValid(), "Puzzle should be in valid state.")
        assertTrue(solution.isSolved(), "Puzzle should be solved.")
    }

    // Another one to try:
    // https://activityworkshop.net/puzzlesgames/nonograms/puzzle1.html

}