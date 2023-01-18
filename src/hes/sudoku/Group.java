package hes.sudoku;

import java.util.*;

public class Group {

    private String name;
    private final Set<Cell> cells = new LinkedHashSet<>(9);

    public Group(String name) {
        this.name = name;
    }

    public Set<Cell> getCells() {
        return cells;
    }

    public void addCell(Cell cell) {
        cells.add(cell);
        cell.getGroups().add(this);
    }

    public boolean contains(Cell cell) {
        return cells.contains(cell);
    }

    public void eliminate(Integer number) {
        cells.forEach(c -> c.eliminate(number));
    }

    public int candidates() {
        int candidates = 9;
        for (Cell cell : cells) {
            if (cell.isKnown()) {
                candidates--;
            }
        }

        return candidates;
    }

    public Set<Candidate> findGhostCandidates() {

        Set<Candidate> candidates = new LinkedHashSet();
        for (int number = 1; number < 10; number++) {
            int nrOfCandidates = 0;
            for (Cell cell : cells) {
                if (cell.hasCandidate(number)) {
                    nrOfCandidates++;
                }
            }
            if (nrOfCandidates == 1) {
                Cell cell = getFirstGhost(number);
                candidates.add(new Candidate(cell, number, "Unique candidate in " + name));
//                System.out.format("In group %s, the number %s can be put in %s place: %s\n", name, number, nrOfCandidates, cell);
            }
        }
        return candidates;
    }

    private Cell getFirstGhost(Integer number) {
        for (Cell cell : cells) {
            if (cell.hasCandidate(number)) {
                return cell;
            }
        }
        throw new IllegalStateException(String.format("No ghost candidate %s for group %s", number, name));

    }

    @Override
    public String toString() {
        return name;
    }
}
