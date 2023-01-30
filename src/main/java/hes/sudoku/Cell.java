package hes.sudoku;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class Cell {

    private Integer number = 0;
    private boolean known = false;
    private Set<Integer> candidates = new LinkedHashSet<>(9);

    private final int row;
    private final int column;
    private final Set<Group> groups = new LinkedHashSet<>();

    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
        for (int number = 1; number <= 9; number++) {
            candidates.add(number);
        }
    }

    public Cell(Cell original) {
        this.number = original.number;
        this.known = original.known;
        this.candidates.addAll(original.candidates);
        this.row = original.row;
        this.column = original.column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isKnown() {
        return known;
    }

    public Integer getNumber() {
        return number;
    }

    public Set<Integer> getCandidates() {
        return Collections.unmodifiableSet(candidates);
    }

    // For unit tests
    void _setCandidates(Set<Integer> candidates) {
        this.candidates = candidates;
    }

    public Set<Group> getGroups() {
        return Collections.unmodifiableSet(groups);
    }

    public String getName() {
        return String.format("Row %s column %s", row + 1, column + 1);
    }

    public void add(Group group) {
        groups.add(group);
    }

    public void setNumber(Integer number) {
        this.number = number;
        known = true;
        candidates = Collections.EMPTY_SET;
    }

    public void eliminate(Integer number) {
        candidates.remove(number);
        if (!known && candidates.size() == 0) {
            throw new IllegalStateException(String.format("No more candidates for %s after eliminating %s", toString(), number));
        }
    }

    public void removeCandidates(Set<Integer> invalidCandidates) {
        candidates.removeAll(invalidCandidates);

    }
    public String text() {
        if (known) {
            return String.valueOf(number) + " ";
        }

        return ". ";
    }

    public String allCandidatesAsString() {
        if (known) {
            return String.format("        .");
        }

        StringBuilder builder = new StringBuilder(9);
        candidates.forEach(builder::append);

        return String.format("%9s", builder);
    }

    @Override
    public String toString() {
        return String.format("Row %s column %s", row + 1, column + 1);
    }

}
