package hes.sudoku;

import java.io.PrintStream;
import java.util.List;

public class Printer {

    public final PrintStream out;

    private final Puzzle puzzle;
    private long start = System.currentTimeMillis();

    public Printer(Puzzle puzzle) {
        this(puzzle, System.out);
    }

    public Printer(Puzzle puzzle, PrintStream out) {
        this.puzzle = puzzle;
        this.out = out;
    }

    public void println(Object message, Object... args) {
        if (args == null) {
            out.println(message);
        } else {
            out.format(String.valueOf(message), args);
            out.println();
        }
    }

    public void printStart() {
        printPuzzle();
        start = System.currentTimeMillis();
    }

    public void printEnd() {
        long duration = System.currentTimeMillis() - start;
        out.println("\nSolution:");
        printPuzzle();
        System.out.format("Solved in %s ms.\n", duration);
    }

    public void printPuzzle() {

        out.println("+-------+-------+-------+");
        for (int i = 0; i < puzzle.getCells().size(); i++) {

            if (i % 9 == 0) {
                out.print("| ");
            }

            out.print(puzzle.getCells().get(i).text());

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
        for (int i = 0; i < puzzle.getCells().size(); i++) {

            if (i % 9 == 0) {
                out.print("|");
            }

            out.print(puzzle.getCells().get(i).allCandidatesAsString());

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

    public static Printer silentPrinter() {
        return new Printer(null) {
            @Override
            public void println(Object message, Object... args) { }

            @Override
            public void printStart() { }

            @Override
            public void printEnd() { }

            @Override
            public void printPuzzle() { }

            @Override
            public void printAllCandidates() { }
        };
    }
}
