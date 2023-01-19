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

        long start = System.currentTimeMillis();

        // Solve
        sweep();

        if (puzzle.isSolved()) {
            long duration = System.currentTimeMillis() - start;
            System.out.format("Solved in %s ms.\n", duration);
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
            if (cell.getCandidates().size() == 1) {
                candidates.add(new Candidate(cell, getCandidate(cell), "Only possible value for " + cell));
            }
        }
        return candidates;
    }

    public Integer getCandidate(Cell cell) {
        return cell.getCandidates().iterator().next();
    }


    private Set<Candidate> findGhostCandidates() {
        Set<Candidate> candidates = new LinkedHashSet();
        for (Group group : puzzle.getGroups()) {
            candidates.addAll(findGhostCandidates(group));
        }

        return candidates;
    }

    public Set<Candidate> findGhostCandidates(Group group) {

        Set<Candidate> candidates = new LinkedHashSet();
        for (int number = 1; number < 10; number++) {
            int nrOfCandidates = 0;
            for (Cell cell : group.getCells()) {
                if (cell.hasCandidate(number)) {
                    nrOfCandidates++;
                }
            }
            if (nrOfCandidates == 1) {
                Cell cell = getFirstGhost(number, group.getCells());
                candidates.add(new Candidate(cell, number, "Unique candidate in " + group));
            }
        }

        return candidates;
    }

    private Cell getFirstGhost(Integer number, Collection<Cell> cells) {
        for (Cell cell : cells) {
            if (cell.hasCandidate(number)) {
                return cell;
            }
        }

        throw new IllegalStateException(String.format("No ghost candidate %s in %s", number, cells));
    }


    //
    // Eliminate
    //

    private void eliminateCrossGroupDependencies() {
        for (Group group : puzzle.getGroups()) {
            for (int number = 1; number <= 9; number++) {
                eliminateCrossGroupDependencies(group, number, puzzle.getBoxes());
                eliminateCrossGroupDependencies(group, number, puzzle.getRows());
                eliminateCrossGroupDependencies(group, number, puzzle.getColumns());
            }
        }
    }

    private void eliminateCrossGroupDependencies(Group group, Integer number, List<Group> ignore) {

        Set<Group> overlapping = getOverlappingGroupsWithSameNumber(group, number);
        overlapping.removeAll(ignore);

        if (overlapping.size() == 1) {
            Group other = overlapping.iterator().next();
            Set<Cell> cells = new LinkedHashSet<>(other.getCells());
            cells.removeAll(group.getCells());

            cells.forEach(cell -> cell.eliminate(number));
        }
    }

    private Set<Group> getOverlappingGroupsWithSameNumber(Group group, Integer number) {
        Set<Group> overlapping = new HashSet<>();
        for (Cell cell : group.getCells()) {
            if (cell.hasCandidate(number)) {
                overlapping.addAll(cell.getGroups());
            }
        }
        overlapping.remove(group);

        return overlapping;
    }

    //
    //
    //

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
