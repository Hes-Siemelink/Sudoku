package hes.sudoku;

import java.util.Collection;

public class Main {

    public static void solve(Puzzle puzzle) {

        Printer printer = new Printer(puzzle);
        LogicSolver solver = new LogicSolver(puzzle);

        // Print
        printer.printStart();

        // Solve
        solver.sweep();

        if (puzzle.isSolved()) {
            printer.printEnd();
            return;
        }

        printer.out.println("\nEliminating candidates where there are subsets contains all possibilities.");
        printer.out.println("\nBefore:");
        printer.printAllCandidates();

        solver.eliminateBasedOnUniqueSetsInGroup();

        printer.out.println("\nAfter:");
        printer.printAllCandidates();

        // Print
        printer.out.println("\nPuzzle:");
        printer.printPuzzle();

        printer.out.println("Solving recursively");
        Collection<Move> moves = new RecursiveSolver(puzzle).solve();

        printer.out.println("Solution:");
        moves.forEach(printer.out::println);
        moves.forEach(puzzle::apply);


        solver.sweep();

        printer.printEnd();
    }

    public static void main(String[] args) {
        Puzzle puzzle = Puzzle.parse(Samples.EMPTY);
        solve(puzzle);
    }
}