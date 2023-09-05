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
    fun `solve puzzle`() {
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
}