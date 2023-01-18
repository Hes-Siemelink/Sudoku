package hes.sudoku;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class Cell {

    private Integer number = 0;
    private boolean known = false;
    private Set<Integer> candidates = new LinkedHashSet<>(9);

    private String name;
    private Set<Group> groups = new LinkedHashSet<>();

    public Cell(String name) {
        this.name = name;
        for (int number = 1; number <= 9; number++) {
            candidates.add(number);
        }
    }

    public boolean isKnown() {
        return known;
    }

    public Integer getNumber() {
        return number;
    }

    public Set<Integer> getCandidates() {
        return candidates;
    }

    public void setNumber(Integer number) {
        this.number = number;
        known = true;
        candidates = Collections.EMPTY_SET;
    }

    public void eliminate(Integer number) {
        candidates.remove(number);
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public String text() {
        if (known) {
            return String.valueOf(number) + " ";
        }

        return ". ";
    }

    public int nrOfCandidates() {
        return candidates.size();
    }

    public Integer firstCandidate() {
        return candidates.iterator().next();
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
        return name;
    }

    public boolean hasCandidate(Integer number) {
        return candidates.contains(number);
    }
}
