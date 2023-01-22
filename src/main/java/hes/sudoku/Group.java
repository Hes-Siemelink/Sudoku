package hes.sudoku;

import java.util.*;

public class Group {

    private final String name;
    private final Set<Cell> cells = new LinkedHashSet<>(9);

    public Group(String name) {
        this.name = name;
    }

    public Set<Cell> getCells() {
        return Collections.unmodifiableSet(cells);
    }

    public void addCell(Cell cell) {
        cells.add(cell);
        cell.add(this);
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

    @Override
    public String toString() {
        return name;
    }
}
