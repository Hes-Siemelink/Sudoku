package hes.sudoku;

import java.util.ArrayList;
import java.util.List;

public class Puzzle {

    private final List<Cell> cells = new ArrayList<>(81);
    private final List<Group> groups = new ArrayList<>(27);

    public Puzzle() {
        initCells();
        initGroups();
    }

    public List<Cell> getCells() {
        return cells;
    }

    public List<Group> getGroups() {
        return groups;
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

    private void initCells() {
        for (int i = 0; i < 81; i++) {
            cells.add(new Cell(String.format("r%sc%s", i / 9 + 1, i % 9 + 1)));
        }
    }

    private void initGroups() {
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
    }

    public Group initBox(String name, int startX, int startY) {
        Group block = new Group(name);

        block.addCell(cell(startX + 0, startY + 0));
        block.addCell(cell(startX + 1, startY + 0));
        block.addCell(cell(startX + 2, startY + 0));

        block.addCell(cell(startX + 0, startY + 1));
        block.addCell(cell(startX + 1, startY + 1));
        block.addCell(cell(startX + 2, startY + 1));

        block.addCell(cell(startX + 0, startY + 2));
        block.addCell(cell(startX + 1, startY + 2));
        block.addCell(cell(startX + 2, startY + 2));

        return block;
    }

    private Group initRow(String name, int y) {
        Group row = new Group(name);
        for (int i = 0; i < 9; i++) {
            row.addCell(cell(i, y));
        }
        return row;
    }

    private Group initColumn(String name, int x) {
        Group column = new Group(name);
        for (int i = 0; i < 9; i++) {
            column.addCell(cell(x, i));
        }
        return column;
    }

    //
    // Set numbers
    //

    public Cell cell(int x, int y) {
        return cells.get(y * 9 + x);
    }

    public void setNumber(Integer number, int x, int y) {
        setNumber(number, cell(x, y));
    }

    public void setNumber(Integer number, Cell cell) {
        cell.setNumber(number);

        // Update cells
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).contains(cell)) {
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
                puzzle.setNumber(Integer.valueOf(String.valueOf(c)), i % 9, i / 9);
            }

            i++;
        }
        return puzzle;
    }

}
