package hes.sudoku;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


class LogicSolverTest {

    @Test
    void eliminateBasedOnUniqueSets() {
        Cell cell_0 = new Cell(0, 0);
        cell_0._setCandidates(setOf(1, 2, 3));

        Cell cell_1 = new Cell(0, 1);
        cell_1._setCandidates(setOf(2, 3));

        Cell cell_2 = new Cell(0, 2);
        cell_2._setCandidates(setOf(2, 3));

        Cell cell_3 = new Cell(0, 1);
        cell_3._setCandidates(setOf(4, 5, 6, 7, 8, 9));

        Group group = new Group("Test Group");
        group.addCell(cell_0);
        group.addCell(cell_1);
        group.addCell(cell_2);
        group.addCell(cell_3);

        Set<Move> eliminations = new LinkedHashSet<>();
        LogicSolver.eliminateBasedOnUniqueSets(group, eliminations);

        assertThat(eliminations).hasSize(2);
        assertThat(eliminations).contains(new Move(cell_0, 2, ""));
        assertThat(eliminations).contains(new Move(cell_0, 3, ""));
    }

    //
    // Util
    //

    private static <T> Set<T> setOf(T... candidates) {
        return new LinkedHashSet<>(Arrays.asList(candidates));
    }
}