package hes.sudoku.kt

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class LogicSolverKtTest {

    @Test
    fun eliminateBasedOnUniqueSets() {

        val cell_0 = Cell(0, 0).replaceCandidates(setOf(1, 2, 3))
        val cell_1 = Cell(0, 1).replaceCandidates(setOf(2, 3))
        val cell_2 = Cell(0, 2).replaceCandidates(setOf(2, 3))
        val cell_3 = Cell(0, 1).replaceCandidates(setOf(4, 5, 6, 7, 8, 9))

        val group = Group("Test Group")
        group.addCell(cell_0)
        group.addCell(cell_1)
        group.addCell(cell_2)
        group.addCell(cell_3)

        val eliminations: MutableSet<Move> = LinkedHashSet()
        eliminateBasedOnUniqueSets(group, eliminations)

        Assertions.assertThat(eliminations).hasSize(2)
        Assertions.assertThat(eliminations).contains(Move(cell_0, 2))
        Assertions.assertThat(eliminations).contains(Move(cell_0, 3))
    }
}