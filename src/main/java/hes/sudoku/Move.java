package hes.sudoku;

import java.util.Objects;

public record Move(Cell cell, Integer number, String reason) {

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (otherObject == null || getClass() != otherObject.getClass()) return false;
        Move other = (Move) otherObject;
        return cell.getRow() == other.cell().getRow()
                && cell.getColumn() == other.cell().getColumn()
                && number == other.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cell().getRow(), cell.getColumn(), number);
    }

    @Override
    public String toString() {
        return String.format("Write %s in row %s, column %s - %s", number, cell.getRow() + 1, cell.getColumn() + 1, reason);
    }
}
