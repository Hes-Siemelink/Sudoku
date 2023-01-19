package hes.sudoku;

import java.io.PrintStream;
import java.util.List;
import java.util.function.Supplier;

public class Printer {

    public final PrintStream out;

    private final Puzzle puzzle;
    private final List<Cell> cells;

    public Printer(Puzzle puzzle) {
        this(puzzle, System.out);
    }

    public Printer(Puzzle puzzle, PrintStream out) {
        this.puzzle = puzzle;
        this.out = out;
        this.cells = puzzle.getCells();
    }

    public void printPuzzle() {

        out.println("+-------+-------+-------+");
        for (int i = 0; i < cells.size(); i++) {

            if (i % 9 == 0) {
                out.print("| ");
            }

            out.print(cells.get(i).text());

            // Block divider
            int next = i + 1;
            if (next % 3 == 0) {
                out.print("| ");
            }

            // New line
            if (next % 9 == 0) {
                out.println();
            }

            // Block line
            if (next % 27 == 0) {
                out.println("+-------+-------+-------+");
            }
        }
    }
    public void printAllCandidates() {

        out.println("+----------------------------+-----------------------------+-----------------------------+");
        for (int i = 0; i < cells.size(); i++) {

            if (i % 9 == 0) {
                out.print("|");
            }

            out.print(cells.get(i).allCandidatesAsString());

            // Block divider
            int next = i + 1;
            if (next % 3 == 0) {
                out.print(" | ");
            }

            // New line
            if (next % 9 == 0) {
                out.println();
            }

            // Block line
            if (next % 27 == 0) {
                out.println("+----------------------------+-----------------------------+-----------------------------+");
            }
        }
    }

    public void printCandidatesPerGroup() {

        out.println("Number of candidates per group:");
        for (Group group : puzzle.getGroups()) {
            out.println(String.format("%s: %s", group, group.candidates()));
        }
    }
}
