package hes.sudoku;

import java.util.*;

public class RecursiveSolver {

    private final Puzzle puzzle;
    private final Printer printer;

    public RecursiveSolver(Puzzle puzzle) {
        this.puzzle = puzzle;
        this.printer = new Printer(puzzle);
    }

    public Set<Move> solve() {
        Set<Move> moves = new LinkedHashSet<>();
        solve(moves);

        return moves;
    }

    public boolean solve(Set<Move> moves) {
        if (puzzle.isSolved()) {
            return true;
        }

        Collection<Move> candidates = getPossibleMoves();

        for (Move candidate : candidates) {
            printer.println("Trying " + candidate);

            Puzzle clonedPuzzle = new Puzzle(puzzle);
            clonedPuzzle.apply(candidate);

            if (solve(clonedPuzzle, moves)) {
                moves.add(candidate);
                return true;
            }
            printer.println("%s does not solve the puzzle", candidate);
        }

        return false;
    }

    private boolean solve(Puzzle puzzle, Set<Move> candidates) {
        LogicSolver solver = new LogicSolver(puzzle, Printer.silentPrinter());
        solver.fillNumbers();
        if (puzzle.isSolved()) {
            printer.println("Puzzle solved recursively.");
            return true;
        }

        new Printer(puzzle).printPuzzle();
        printer.println("Puzzle not solved. Trying recursively.");

        return new RecursiveSolver(puzzle).solve(candidates);
    }

    private Collection<Move> getPossibleMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        for (Cell cell : puzzle.getCells()) {
            for (Integer  number : cell.getCandidates()) {
                moves.add(new Move(cell, number, "Recursive guess out of " + cell.getCandidates().size()));
            }
        }

        moves.sort(new CellCandidateComparator());

        return moves;
    }

    private static class CellCandidateComparator implements Comparator<Move> {
        @Override
        public int compare(Move o1, Move o2) {
            return o1.cell().getCandidates().size() - o2.cell().getCandidates().size();
        }
    }
}
