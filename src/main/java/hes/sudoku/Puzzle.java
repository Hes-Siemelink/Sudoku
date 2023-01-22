package hes.sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Puzzle {

    private final List<Cell> cells;
    private final List<Group> groups;

    public Puzzle() {
        cells = initCells();
        groups = initGroups();
    }

    public Puzzle(Puzzle original) {
        cells = new ArrayList<>(81);
        for (Cell cell : original.getCells()) {
            cells.add(new Cell(cell));
        }
        groups = initGroups();
    }

    public List<Cell> getCells() {
        return Collections.unmodifiableList(cells);
    }

    public List<Group> getGroups() {
        return Collections.unmodifiableList(groups);
    }

    public List<Group> getBoxes() {
        return groups.subList(0, 9);
    }

    public List<Group> getRows() {
        return groups.subList(9, 18);
    }

    public List<Group> getColumns() {
        return groups.subList(18, 27);
    }

    private List<Cell> initCells() {
        List<Cell> cells = new ArrayList<>(81);
        for (int i = 0; i < 81; i++) {
            cells.add(new Cell(String.format("Row %s column %s", i / 9 + 1, i % 9 + 1)));
        }
        return cells;
    }

    private List<Group> initGroups() {
        List<Group> groups = new ArrayList<>(27);

        int group = 0;

        // Boxes
        groups.add(initBox("Box 1", 0 ,0));
        groups.add(initBox("Box 2", 3 ,0));
        groups.add(initBox("Box 3", 6 ,0));
        groups.add(initBox("Box 4", 0 ,3));
        groups.add(initBox("Box 5", 3 ,3));
        groups.add(initBox("Box 6", 6 ,3));
        groups.add(initBox("Box 7", 0 ,6));
        groups.add(initBox("Box 8", 3 ,6));
        groups.add(initBox("Box 9", 6 ,6));

        // Rows
        for (int row = 0; row < 9; row++) {
            groups.add(initRow("Row " + (row + 1), row));
        }

        // Columns
        for (int column = 0; column < 9; column++) {
            groups.add(initColumn("Col " + (column + 1), column));
        }

        return groups;
    }

    public Group initBox(String name, int startX, int startY) {
        Group block = new Group(name);

        block.addCell(getCell(startX + 0, startY + 0));
        block.addCell(getCell(startX + 1, startY + 0));
        block.addCell(getCell(startX + 2, startY + 0));

        block.addCell(getCell(startX + 0, startY + 1));
        block.addCell(getCell(startX + 1, startY + 1));
        block.addCell(getCell(startX + 2, startY + 1));

        block.addCell(getCell(startX + 0, startY + 2));
        block.addCell(getCell(startX + 1, startY + 2));
        block.addCell(getCell(startX + 2, startY + 2));

        return block;
    }

    private Group initRow(String name, int y) {
        Group row = new Group(name);
        for (int i = 0; i < 9; i++) {
            row.addCell(getCell(i, y));
        }
        return row;
    }

    private Group initColumn(String name, int x) {
        Group column = new Group(name);
        for (int i = 0; i < 9; i++) {
            column.addCell(getCell(x, i));
        }
        return column;
    }

    //
    // Set numbers
    //

    public Cell getCell(int x, int y) {
        return cells.get(y * 9 + x);
    }

    public void apply(Move move) {
        apply(move.number(), move.cell());
    }

    public void apply(Integer number, Cell cell) {
        cell.setNumber(number);

        // Update cells
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).getCells().contains(cell)) {
                groups.get(i).eliminate(number);
            }
        }
    }

    public boolean isSolved() {
        for (Cell cell : cells) {
            if (!cell.isKnown()) {
                return false;
            }
        }
        return true;
    }

    //
    // Parse
    //

    public static Puzzle parse(String input) {
        Puzzle puzzle = new Puzzle();
        int i = 0;
        for (char c : input.toCharArray()) {
            if (Character.isWhitespace(c)) {
                continue;
            }

            if (Character.isDigit(c)) {
                puzzle.apply(Integer.valueOf(String.valueOf(c)), puzzle.getCell(i % 9, i / 9));
            }

            i++;
        }
        return puzzle;
    }

}
