package hes.sudoku;

import java.util.*;

public class RecursiveSolver {

    private final Puzzle puzzle;

    public RecursiveSolver(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    public Set<Candidate> solve() {
        Set<Candidate> candidates = new LinkedHashSet<>();
        solve(candidates);
        return candidates;
    }

    public boolean solve(Set<Candidate> candidates) {
        if (puzzle.isSolved()) {
            return true;
        }

        Collection<Candidate> possibleCandidates = getPossibleCandidates();

        for (Candidate candidate : possibleCandidates) {
            Puzzle clone = new Puzzle(puzzle);
            puzzle.setNumber(candidate.number(), candidate.cell());
            if (solve(puzzle, candidates)) {
                candidates.add(candidate);
                return true;
            }
            System.out.format("%s does not solve the puzzle", candidate);
        }

        return false;
    }

    private boolean solve(Puzzle puzzle, Set<Candidate> candidates) {
        Solver solver = new Solver(puzzle);
        solver.sweep();
        if (puzzle.isSolved()) {
            return true;
        }

        return new RecursiveSolver(puzzle).solve(candidates);
    }

    private Collection<Candidate> getPossibleCandidates() {
        ArrayList<Candidate> posssibleCandidates = new ArrayList<>();
        for (Cell cell : puzzle.getCells()) {
            for (Integer  number : cell.getCandidates()) {
                posssibleCandidates.add(new Candidate(cell, number, "Recursive guess out of " + cell.getCandidates().size()));
            }
        }

        posssibleCandidates.sort(new CellCandidateComparator());

        return posssibleCandidates;
    }

    private static class CellCandidateComparator implements Comparator<Candidate> {
        @Override
        public int compare(Candidate o1, Candidate o2) {
            return o1.cell().getCandidates().size() - o2.cell().getCandidates().size();
        }
    }
}
