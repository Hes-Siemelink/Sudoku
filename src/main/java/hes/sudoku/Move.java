package hes.sudoku;

import java.util.Objects;

public record Move(Cell cell, int number, String reason) {

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Move move = (Move) other;
        return number == move.number && cell.equals(move.cell);
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
