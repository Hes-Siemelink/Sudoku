package hes.nonogram

import hes.nonogram.State.FILLED
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class LineTest {

    @Test
    fun `string representation`() {
        val cells = Cell.fromString(".-*")

        assertEquals(3, cells.size)
        assertEquals(State.UNKNOWN, cells[0].state)
        assertEquals(State.EMPTY, cells[1].state)
        assertEquals(FILLED, cells[2].state)

        assertEquals(".-*", Cell.toString(cells))
    }

    @Test
    fun `segment validation with hint of size 1`() {
        assertTrue(LineSegment(1, ".").isValid())
        assertTrue(LineSegment(1, "..").isValid())
        assertTrue(LineSegment(1, "*.").isValid())
        assertTrue(LineSegment(1, ".*").isValid())
        assertTrue(LineSegment(1, "*-").isValid())
        assertTrue(LineSegment(1, "-*").isValid())
        assertTrue(LineSegment(1, "--*..").isValid())

        assertFalse(LineSegment(1, "**").isValid())
        assertFalse(LineSegment(1, "--").isValid())
    }

    @Test
    fun `segment validation with hint of size 2`() {
        assertTrue(LineSegment(2, "..").isValid())
        assertTrue(LineSegment(2, "*.").isValid())
        assertTrue(LineSegment(2, ".*").isValid())
        assertTrue(LineSegment(2, "--*..").isValid())
        assertTrue(LineSegment(2, "**").isValid())

        assertFalse(LineSegment(2, "*-").isValid())
        assertFalse(LineSegment(2, "-*").isValid())
        assertFalse(LineSegment(2, "--").isValid())
    }

    @Test
    fun `line validation for 1 hint`() {
        assertTrue(Line(listOf(1), ".").isValid())
        assertTrue(Line(listOf(1), "..").isValid())
        assertTrue(Line(listOf(2), "*.").isValid())
        assertTrue(Line(listOf(3), "-***").isValid())
        assertTrue(Line(listOf(3), "***--").isValid())
        assertTrue(Line(listOf(3), "-*...").isValid())
    }

    @Test
    fun `line validation for 2 hints`() {
        assertTrue(Line(listOf(1, 1), "...").isValid())
        assertTrue(Line(listOf(1, 1), "*.*").isValid())
        assertTrue(Line(listOf(1, 3), "..*.*").isValid())
        assertTrue(Line(listOf(1, 1), "-*..").isValid())
        assertTrue(Line(listOf(1, 2), "*-*..").isValid())
        assertTrue(Line(listOf(1, 1, 1), "*-*.*").isValid())
        assertTrue(Line(listOf(4, 1), "..****..").isValid())
        assertTrue(Line(listOf(1, 4), "..****..").isValid())
        assertTrue(Line(listOf(1, 1), "*-*..").isValid())

        assertFalse(Line(listOf(1, 1), ".*.").isValid())
        assertFalse(Line(listOf(1, 1, 1), ".*.*.").isValid())
        assertFalse(Line(listOf(1, 1), "---*").isValid())
        assertFalse(Line(listOf(2, 1), ".***..").isValid())
        assertFalse(Line(listOf(2, 2), "****").isValid())
    }

    @Test
    fun `line validation for 3 hints`() {
        assertTrue(Line(listOf(1, 1, 1), ".....").isValid())

        assertFalse(Line(listOf(2, 2, 2), "**....-.").isValid())
    }

    @Test
    fun `line solved`() {
        assertTrue(Line(listOf(1), "*").isSolved())
        assertTrue(Line(listOf(1, 2), "*-**.").isSolved())

        assertFalse(Line(listOf(1, 2), "*-.*.").isSolved())
        assertFalse(Line(listOf(1, 2), "*-***").isSolved())
    }

    @Test
    fun `copy`() {
        val line = Line(listOf(1), "...")

        val copy = line.copy()
        copy.cells[0].state = FILLED

        assertEquals("*..", Cell.toString(copy.cells))
        assertEquals("...", Cell.toString(line.cells))
    }

    @Test
    fun `generate possibilities for (1) length 5`() {
        val result = allPossibilities(listOf(1), 5).map { State.toString(it) }

        result.forEach {
            println(it)
        }

        assertIterableEquals(
            listOf(
                "*----",
                "-*---",
                "--*--",
                "---*-",
                "----*"
            ),
            result
        )
    }

    @Test
    fun `generate possibilities for (1,1) length 5`() {
        val result = allPossibilities(listOf(1, 1), 5).map { State.toString(it) }

        result.forEach {
            println(it)
        }

        assertIterableEquals(
            listOf(
                "*-*--",
                "*--*-",
                "*---*",
                "-*-*-",
                "-*--*",
                "--*-*"
            ),
            result
        )
    }
}