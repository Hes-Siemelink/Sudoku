package hes.sudoku;

import java.util.*;

public class Solver {

    private final Puzzle puzzle;
    private Printer printer;

    public Solver(Puzzle puzzle) {
        this.puzzle = puzzle;
        this.printer = new Printer(puzzle);
    }

    public void solve() {

        // Print
        printer.printPuzzle();

        // Solve
        sweep();

        if (puzzle.isSolved()) {
            printer.out.println("Solution:");
            printer.printPuzzle();
            return;
        }

        printer.out.println("\nEliminating candidates where there are subsets contains all possibilities.");
        printer.out.println("\nBefore:");
        printer.printAllCandidates();

        eliminateBasedOnUniqueSetsInGroup();

        printer.out.println("\nAfter:");
        printer.printAllCandidates();

        eliminateBasedOnUniqueSetsInGroup();

        printer.out.println("\nAgain:");
        printer.printAllCandidates();

        sweep();

        printer.out.println("\nAnd again:");
        printer.printAllCandidates();

        // Print
        printer.out.println("\nPuzzle:");
        printer.printPuzzle();
    }

    private void sweep() {
        Set<Candidate> candidates = Collections.EMPTY_SET;
        do {
            candidates = new LinkedHashSet();
            eliminateCrossGroupDependencies(); // TODO move out of sweep() to make it less heavy

            candidates.addAll(findByElimination());
            candidates.addAll(findGhostCandidates());

            fillInCandidates(candidates);
        } while (!candidates.isEmpty());

        printer.out.println("No more candidates found.\n");
    }

    private void fillInCandidates(Set<Candidate> candidates) {
        if (candidates.isEmpty()) {
            return;
        }

        printCandidates(candidates);
        for (Candidate candidate : candidates) {
            puzzle.setNumber(candidate.number(), candidate.cell());
        }
    }

    private void printCandidates(Set<Candidate> candidates) {
        printer.out.println("Moves:");
        for (Candidate candidate: candidates) {
            printer.out.format("%s can be filled with %s - %s\n", candidate.cell(), candidate.number(), candidate.reason());
        }
    }


    //
    // Find candidates
    //

    private Set<Candidate> findByElimination() {
        Set<Candidate> candidates = new LinkedHashSet();
        for (Cell cell : puzzle.getCells()) {
            if (cell.nrOfCandidates() == 1) {
                candidates.add(new Candidate(cell, cell.firstCandidate(), "Only possible value for " + cell));
            }
        }
        return candidates;
    }

    private Set<Candidate> findGhostCandidates() {
        Set<Candidate> candidates = new LinkedHashSet();
        for (Group group : puzzle.getGroups()) {
            candidates.addAll(group.findGhostCandidates());
        }
        return candidates;
    }


    //
    // Eliminate
    //

    private void eliminateCrossGroupDependencies() {
        for (Group group : puzzle.getGroups()) {
            for (int number = 1; number <= 9; number++) {
                eliminateCrossGroupDependencies(group, puzzle.getBoxes(), number);
                eliminateCrossGroupDependencies(group, puzzle.getRows(), number);
                eliminateCrossGroupDependencies(group, puzzle.getColumns(), number);
            }
        }
    }

    private void eliminateCrossGroupDependencies(Group group, List<Group> ignore, Integer number) {

        Set<Group> otherGroups = getOtherGroups(group, number);

        otherGroups.remove(group);
        otherGroups.removeAll(ignore);
        if (otherGroups.size() == 1) {
            Group other = otherGroups.iterator().next();
            Set<Cell> cells = new LinkedHashSet<>(other.getCells());
            cells.removeAll(group.getCells());

            cells.forEach(cell -> cell.eliminate(number));
        }
    }

    private Set<Group> getOtherGroups(Group group, Integer number) {
        Set<Group> groups = new HashSet<>();
        for (Cell cell : group.getCells()) {
            if (cell.hasCandidate(number)) {
                groups.addAll(cell.getGroups());
            }
        }
        return groups;
    }

    private void eliminateBasedOnUniqueSetsInGroup() {
        for (Group group : puzzle.getGroups()) {
            eliminateBasedOnUniqueSets(group);
        }
    }

    private void eliminateBasedOnUniqueSets(Group group) {
        Map<Set<Integer>, Integer> sets = new LinkedHashMap<>();
        for (Cell cell : group.getCells()) {
            add(cell.getCandidates(), sets);
        }

        for (Map.Entry<Set<Integer>, Integer> entry : sets.entrySet()) {
            if (entry.getKey().size() == entry.getValue()) {
                removeOtherDependencies(group, entry.getKey());
            }
        }
    }

    private void removeOtherDependencies(Group group, Set<Integer> set) {
        for (Cell cell : group.getCells()) {
            if (!cell.getCandidates().equals(set)) {
                cell.getCandidates().removeAll(set);
            }
        }
    }

    private <T> void add(T something, Map<T, Integer> count) {
//        System.out.format("Adding %s to %s\n", something, count);
        if (!count.containsKey(something)) {
            count.put(something, 1);
        } else {
            count.put(something, count.get(something) + 1);
        }
    }

    //
    // Main
    //
    
    public static void main(String[] args) {
        Puzzle puzzle = Puzzle.parse(Samples.EASY);

        // Solve
        new Solver(puzzle).solve();
    }

}
