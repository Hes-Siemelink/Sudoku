package hes.nonogram

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class LineTest {

    @Test
    fun `string representation`() {
        val cells = toCells(".-*")

        assertEquals(3, cells.size)
        assertEquals(Cell.State.UNKNOWN, cells[0].state)
        assertEquals(Cell.State.EMPTY, cells[1].state)
        assertEquals(Cell.State.FILLED, cells[2].state)

        assertEquals(".-*", toString(cells))
    }

    @Test
    fun `segment validation with hint of size 1`() {
        assertTrue(LineSegment(1, ".").valid)
        assertTrue(LineSegment(1, "..").valid)
        assertTrue(LineSegment(1, "*.").valid)
        assertTrue(LineSegment(1, ".*").valid)
        assertTrue(LineSegment(1, "*-").valid)
        assertTrue(LineSegment(1, "-*").valid)
        assertTrue(LineSegment(1, "--*..").valid)

        assertFalse(LineSegment(1, "**").valid)
        assertFalse(LineSegment(1, "--").valid)
    }

    @Test
    fun `segment validation with hint of size 2`() {
        assertTrue(LineSegment(2, "..").valid)
        assertTrue(LineSegment(2, "*.").valid)
        assertTrue(LineSegment(2, ".*").valid)
        assertTrue(LineSegment(2, "--*..").valid)
        assertTrue(LineSegment(1, "**").valid)

        assertFalse(LineSegment(2, "*-").valid)
        assertFalse(LineSegment(2, "-*").valid)
        assertFalse(LineSegment(1, "--").valid)
    }

    @Test
    fun `line validation for 1 hint`() {
        assertTrue(Line(listOf(1), ".").valid)
        assertTrue(Line(listOf(1), "..").valid)
        assertTrue(Line(listOf(2), "*.").valid)
    }

    @Test
    fun `line validation for 2 hints`() {
        assertTrue(Line(listOf(1, 1), "...").valid)
        assertTrue(Line(listOf(1, 1), "*.*").valid)
        assertTrue(Line(listOf(1, 3), "..*.*").valid)

        assertFalse(Line(listOf(1, 1), ".*.").valid)
    }

    @Test
    fun `line validation for 3 hints`() {
        assertTrue(Line(listOf(1, 1, 1), ".....").valid)

        assertFalse(Line(listOf(2, 2, 2), "**....-.").valid)
    }

    @Test
    fun `segment solved`() {
        assertTrue(LineSegment(1, "*").solved)
        assertTrue(LineSegment(1, ".*.").solved)
        assertTrue(LineSegment(1, "-*-").solved)

        assertFalse(LineSegment(2, "***.").solved)
        assertFalse(LineSegment(2, "--*.").solved)
    }

    @Test
    fun `line solved`() {
        assertTrue(Line(listOf(1), "*").solved)
        assertTrue(Line(listOf(1, 2), "*-**.").solved)

        assertFalse(Line(listOf(1, 2), "*-.*.").solved)
        assertFalse(Line(listOf(1, 2), "*-***").solved)
    }
}