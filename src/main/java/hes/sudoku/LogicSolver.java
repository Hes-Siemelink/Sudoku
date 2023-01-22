package hes.sudoku;

import java.util.*;

public class LogicSolver {

    private final Puzzle puzzle;
    private Printer printer;

    public LogicSolver(Puzzle puzzle) {
        this.puzzle = puzzle;
        this.printer = new Printer(puzzle);
    }

    public void sweep() {
        Set<Move> candidates = Collections.EMPTY_SET;
        do {
            candidates = new LinkedHashSet();
            eliminateCrossGroupDependencies(); // TODO move out of sweep() to make it less heavy

            candidates.addAll(findByElimination());
            candidates.addAll(findGhostCandidates());

            applyMoves(candidates);
        } while (!candidates.isEmpty());

        printer.out.println("No more candidates found.\n");
    }

    private void applyMoves(Set<Move> moves) {
        if (moves.isEmpty()) {
            return;
        }

        printMoves(moves);

        for (Move move : moves) {
            puzzle.apply(move);
        }
    }

    private void printMoves(Set<Move> moves) {
        printer.out.println("Moves:");
        moves.forEach(printer.out::println);
    }


    //
    // Find candidates
    //

    private Set<Move> findByElimination() {
        Set<Move> candidates = new LinkedHashSet();
        for (Cell cell : puzzle.getCells()) {
            if (cell.getCandidates().size() == 1) {
                candidates.add(new Move(cell, getCandidate(cell), "Only possible value for " + cell));
            }
        }
        return candidates;
    }

    public Integer getCandidate(Cell cell) {
        return cell.getCandidates().iterator().next();
    }


    private Set<Move> findGhostCandidates() {
        Set<Move> candidates = new LinkedHashSet();
        for (Group group : puzzle.getGroups()) {
            candidates.addAll(findGhostCandidates(group));
        }

        return candidates;
    }

    public Set<Move> findGhostCandidates(Group group) {

        Set<Move> candidates = new LinkedHashSet();
        for (int number = 1; number < 10; number++) {
            int nrOfCandidates = 0;
            for (Cell cell : group.getCells()) {
                if (cell.hasCandidate(number)) {
                    nrOfCandidates++;
                }
            }
            if (nrOfCandidates == 1) {
                Cell cell = getFirstGhost(number, group.getCells());
                candidates.add(new Move(cell, number, "Unique candidate in " + group));
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

    public void eliminateBasedOnUniqueSetsInGroup() {
        for (Group group : puzzle.getGroups()) {
            eliminateBasedOnUniqueSets(group);
        }
    }

    private void eliminateBasedOnUniqueSets(Group group) {
        Map<Set<Integer>, Integer> candidateSetCount = new LinkedHashMap<>();
        for (Cell cell : group.getCells()) {
            add(candidateSetCount, cell.getCandidates());
        }

        for (Map.Entry<Set<Integer>, Integer> entry : candidateSetCount.entrySet()) {
            if (entry.getKey().size() == entry.getValue()) {
                removeOtherDependencies(group, entry.getKey());
            }
        }
    }

    private void removeOtherDependencies(Group group, Set<Integer> set) {
        for (Cell cell : group.getCells()) {
            if (!cell.getCandidates().equals(set)) {
                cell.removeCandidates(set);
            }
        }
    }

    private static <T> void add(Map<T, Integer> count, T something) {
        if (!count.containsKey(something)) {
            count.put(something, 1);
        } else {
            count.put(something, count.get(something) + 1);
        }
    }
}
