package hes.sudoku;

import java.util.Collection;

public class Main {

    public static void solve(Puzzle puzzle) {

        Printer printer = new Printer(puzzle);
        LogicSolver solver = new LogicSolver(puzzle);

        // Print
        printer.printStart();

        // Solve with logic
        solver.solve();

        if (puzzle.isSolved()) {
            printer.printEnd();
            return;
        }

        // Solve recursively
        printer.out.println("Solving recursively");
        Collection<Move> moves = new RecursiveSolver(puzzle).solve();

        printer.out.println("Solution:");
        moves.forEach(printer.out::println);
        moves.forEach(puzzle::apply);

        solver.sweep();

        printer.printEnd();
    }

    public static void main(String[] args) {
        Puzzle puzzle = Puzzle.parse(Samples.GOOI_EN_EEMBODE_26_JAN_2023);
        solve(puzzle);
    }
}