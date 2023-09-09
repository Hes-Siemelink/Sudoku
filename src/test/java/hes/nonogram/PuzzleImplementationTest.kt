package hes.nonogram

import hes.nonogram.State.FILLED
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PuzzleImplementationTest {

    @Test
    fun `create new puzzle`() {

        val puzzle = simplePuzzle()

        assertEquals("..", Cell.toString(puzzle.rows[0].cells))
        assertEquals("..", Cell.toString(puzzle.rows[1].cells))
        assertEquals("..", Cell.toString(puzzle.columns[0].cells))
        assertEquals("..", Cell.toString(puzzle.columns[1].cells))
    }

    @Test
    fun `rows and columns`() {

        val puzzle = simplePuzzle()

        puzzle.cell(0, 0).state = FILLED

        assertEquals(FILLED, puzzle.rows[0].cells[0].state)
        assertEquals(FILLED, puzzle.columns[0].cells[0].state)

        puzzle.rows[1].cells[1].state = FILLED

        assertEquals(FILLED, puzzle.columns[1].cells[1].state)

        val copy = puzzle.copy()

        assertEquals(FILLED, copy.rows[0].cells[0].state)
        assertEquals(FILLED, copy.columns[0].cells[0].state)
        assertEquals(FILLED, copy.rows[1].cells[1].state)
        assertEquals(FILLED, copy.columns[1].cells[1].state)
    }

    @Test
    fun `test valid and solved`() {

        val puzzle = simplePuzzle()

        assertTrue(puzzle.isValid(), "Puzzle should be in valid state.")
        assertFalse(puzzle.isSolved(), "Puzzle should not be solved.")

        puzzle.cell(0, 0).state = FILLED

        assertTrue(puzzle.isValid(), "Puzzle should be in valid state.")
        assertTrue(puzzle.isSolved(), "Puzzle should be solved.")
    }


    @Test
    fun `copy`() {
        val puzzle = simplePuzzle()

        assertEquals(2, puzzle.rows.size)
        assertEquals(2, puzzle.columns.size)

        val copy = puzzle.copy()
        copy.rows[0].cells[0].state = FILLED

        assertEquals(2, copy.rows.size)
        assertEquals(2, copy.columns.size)

        assertEquals("*.", Cell.toString(copy.rows[0].cells))
        assertEquals("..", Cell.toString(puzzle.rows[0].cells))
    }

    private fun simplePuzzle(): Puzzle {
        return nonogram {
            row(1)
            row()

            column(1)
            column()
        }
    }
}