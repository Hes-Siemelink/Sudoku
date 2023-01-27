package hes.sudoku;

import java.util.*;

public class LogicSolver {

    private final Puzzle puzzle;
    private Printer printer;

    public LogicSolver(Puzzle puzzle) {
        this.puzzle = puzzle;
        this.printer = new Printer(puzzle);
    }

    public void solve() {
        while (fillNumbers() || eliminateCandidates());
    }

    public boolean fillNumbers() {
        Set<Move> moves = Collections.EMPTY_SET;
        boolean foundSomething = false;
        do {
            moves = new LinkedHashSet();

            moves.addAll(findUniqueCandidatesInGroup());
            moves.addAll(findUniqueCandidates());

            if (!moves.isEmpty()) {
                applyMoves(moves);
                foundSomething = true;
            }

        } while (!moves.isEmpty());

        printer.out.println("No more moves found.");

        return foundSomething;
    }

    public boolean eliminateCandidates() {
        Set<Move> eliminations = Collections.EMPTY_SET;
        boolean foundSomething = false;
        do {
            eliminations = new LinkedHashSet();

            eliminations.addAll(eliminateCrossGroupDependencies());
            eliminations.addAll(eliminateBasedOnUniqueSetsInGroup());

            if (!eliminations.isEmpty()) {
                applyEliminations(eliminations);
                foundSomething = true;
            }

        } while (!eliminations.isEmpty());

        printer.out.println("No more eliminations found.");

        return foundSomething;
    }

    private void applyMoves(Set<Move> moves) {
        if (!moves.isEmpty()) {
            printer.out.println("Moves:");
        }

        for (Move move : moves) {
            printer.out.println(move);
            puzzle.apply(move);
        }
    }

    private void applyEliminations(Set<Move> eliminations) {
        if (!eliminations.isEmpty()) {
            printer.out.println("\nEliminating candidates:");
        }

        for (Move elimination : eliminations) {
            printer.out.println(elimination);
            Cell cell = puzzle.getCell(elimination.cell().getColumn(), elimination.cell().getRow());
            cell.eliminate(elimination.number());
        }
    }


    //
    // Find candidates
    //

    private Set<Move> findUniqueCandidates() {
        Set<Move> moves = new LinkedHashSet();
        for (Cell cell : puzzle.getCells()) {
            if (cell.getCandidates().size() == 1) {
                moves.add(new Move(cell, getCandidate(cell), "No other candidate for this cell"));
            }
        }
        return moves;
    }

    public Integer getCandidate(Cell cell) {
        return cell.getCandidates().iterator().next();
    }


    private Set<Move> findUniqueCandidatesInGroup() {
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
                candidates.add(new Move(cell, number, String.format("Only possibility in %s", group)));
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

    private Set<Move> eliminateCrossGroupDependencies() {
        Set<Move> eliminations = new LinkedHashSet();

        for (Group group : puzzle.getGroups()) {
            for (int number = 1; number <= 9; number++) {
                eliminateCrossGroupDependencies(group, number, puzzle.getBoxes(), eliminations);
                eliminateCrossGroupDependencies(group, number, puzzle.getRows(), eliminations);
                eliminateCrossGroupDependencies(group, number, puzzle.getColumns(), eliminations);
            }
        }

        return eliminations;
    }

    private void eliminateCrossGroupDependencies(Group group, Integer number, List<Group> ignore, Set<Move> eliminations) {

        Set<Group> overlapping = getOverlappingGroupsWithSameNumber(group, number);
        overlapping.removeAll(ignore);

        if (overlapping.size() == 1) {
            Group other = overlapping.iterator().next();
            Set<Cell> cells = new LinkedHashSet<>(other.getCells());
            cells.removeAll(group.getCells());

            for (Cell cell: cells) {
                if (!cell.hasCandidate(number)) {
                    continue;
                }
                eliminations.add(new Move(cell, number, String.format("Eliminating %s because it needs to be in %s, %s", number, group, other)));
            }
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

    public Set<Move> eliminateBasedOnUniqueSetsInGroup() {
        Set<Move> eliminations = new LinkedHashSet();

        for (Group group : puzzle.getGroups()) {
            eliminateBasedOnUniqueSets(group, eliminations);
        }

        return eliminations;
    }

    private static void eliminateBasedOnUniqueSets(Group group, Set<Move> eliminations) {
        Map<Set<Integer>, Integer> candidateSetCount = new LinkedHashMap<>();
        for (Cell cell : group.getCells()) {
            add(candidateSetCount, cell.getCandidates());
        }

        for (Map.Entry<Set<Integer>, Integer> entry : candidateSetCount.entrySet()) {
            if (entry.getKey().size() == entry.getValue()) {
                removeOtherDependencies(group, entry.getKey(), eliminations);
            }
        }
    }

    private static void removeOtherDependencies(Group group, Set<Integer> set, Set<Move> eliminations) {
        for (Cell cell : group.getCells()) {
            if (!cell.getCandidates().equals(set)) {
                for (Integer number : set) {
                    if (cell.hasCandidate(number)) {
                        eliminations.add(new Move(cell, number, String.format("Eliminate based on unique set in %s", group)));
                    }
                }
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
