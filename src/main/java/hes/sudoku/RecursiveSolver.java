package hes.sudoku;

import java.util.*;

public class RecursiveSolver {

    private final Puzzle puzzle;

    public RecursiveSolver(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    public Set<Move> solve() {
        Set<Move> candidates = new LinkedHashSet<>();
        solve(candidates);
        return candidates;
    }

    public boolean solve(Set<Move> moves) {
        if (puzzle.isSolved()) {
            return true;
        }

        Collection<Move> candidates = getPossibleMoves();

        for (Move candidate : candidates) {
            Puzzle clone = new Puzzle(puzzle);
            puzzle.apply(candidate);
            if (solve(puzzle, moves)) {
                moves.add(candidate);
                return true;
            }
            System.out.format("%s does not solve the puzzle", candidate);
        }

        return false;
    }

    private boolean solve(Puzzle puzzle, Set<Move> candidates) {
        LogicSolver solver = new LogicSolver(puzzle);
        solver.sweep();
        if (puzzle.isSolved()) {
            return true;
        }

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
