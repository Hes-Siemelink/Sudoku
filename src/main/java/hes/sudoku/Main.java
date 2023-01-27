package hes.sudoku;

import java.util.Collection;

public class Main {

    public static void solve(Puzzle puzzle) {

        Printer printer = new Printer(puzzle);
        LogicSolver solver = new LogicSolver(puzzle);

        // Print
        printer.printStart();

        // Solve with logic
        printer.println("\nMoves:");
        solver.solve();

        if (puzzle.isSolved()) {
            printer.printEnd();
            return;
        }

        // Solve recursively
        printer.println("\nSolving recursively");
        Collection<Move> moves = new RecursiveSolver(puzzle).solve();

        printer.println("\nMoves:");
        moves.forEach(printer::println);
        moves.forEach(puzzle::apply);

        solver.fillNumbers();

        printer.printEnd();
    }

    public static void main(String[] args) {
        Puzzle puzzle = Puzzle.parse(Samples.HARD);
        solve(puzzle);
    }
}