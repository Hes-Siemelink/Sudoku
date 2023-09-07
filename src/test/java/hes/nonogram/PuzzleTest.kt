package hes.nonogram

import hes.nonogram.Cell.State.FILLED
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.opentest4j.AssertionFailedError

class PuzzleTest {

    @Test
    fun `create new puzzle`() {

        val puzzle = nonogram {
            row(1)
            row()

            column(1)
            column()
        }

        assertEquals("..", toString(puzzle.rows[0].cells))
        assertEquals("..", toString(puzzle.rows[1].cells))
        assertEquals("..", toString(puzzle.columns[0].cells))
        assertEquals("..", toString(puzzle.columns[1].cells))

        assertTrue(puzzle.valid, "Puzzle should be in valid state.")
        assertFalse(puzzle.solved, "Puzzle should not be solved.")

        puzzle.cell(0, 0).state = FILLED

        assertTrue(puzzle.valid, "Puzzle should be in valid state.")
        assertTrue(puzzle.solved, "Puzzle should be solved.")
    }

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

        assertEquals("*.", toString(puzzle.rows[0].cells).replace('-', '.'))
        assertEquals("..", toString(puzzle.rows[1].cells).replace('-', '.'))
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

    @Test
    fun `copy`() {
        val puzzle = nonogram {
            row(1)
            row()

            column(1)
            column()
        }

        assertEquals(2, puzzle.rows.size)
        assertEquals(2, puzzle.columns.size)

        val copy = puzzle.copy()
        copy.rows[0].cells[0].state = FILLED

        assertEquals(2, copy.rows.size)
        assertEquals(2, copy.columns.size)

        assertEquals("*.", toString(copy.rows[0].cells))
        assertEquals("..", toString(puzzle.rows[0].cells))
    }

}