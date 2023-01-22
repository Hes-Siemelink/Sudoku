package hes.sudoku;

import java.util.Objects;

public record Candidate(Cell cell, int number, String reason) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidate candidate = (Candidate) o;
        return number == candidate.number && cell.equals(candidate.cell);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cell, number);
    }

    @Override
    public String toString() {
        return String.format("%s can be filled with %s - %s", cell, number, reason);
    }
}
