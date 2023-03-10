package hes.sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Puzzle {

    private final List<Cell> cells = new ArrayList<>(81);
    private final List<Group> groups = new ArrayList<>(27);;

    public Puzzle() {
        this(true);
    }

    Puzzle(boolean init) {
        if (init) {
            initCells();
            initGroups();
        }
    }

    public Puzzle(Puzzle original) {
        for (Cell cell : original.getCells()) {
            cells.add(new Cell(cell));
        }
        initGroups();
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

    private void initCells() {
        for (int i = 0; i < 81; i++) {
            cells.add(new Cell(i / 9, i % 9));
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
        Group box = new Group(name);

        box.addCell(getCell(startX + 0, startY + 0));
        box.addCell(getCell(startX + 1, startY + 0));
        box.addCell(getCell(startX + 2, startY + 0));

        box.addCell(getCell(startX + 0, startY + 1));
        box.addCell(getCell(startX + 1, startY + 1));
        box.addCell(getCell(startX + 2, startY + 1));

        box.addCell(getCell(startX + 0, startY + 2));
        box.addCell(getCell(startX + 1, startY + 2));
        box.addCell(getCell(startX + 2, startY + 2));

        return box;
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
        apply(getCell(move.cell().getColumn(), move.cell().getRow()), move.number());
    }

    public void apply(Cell cell, Integer number) {
        cell.setNumber(number);

        // Update cells
        for (Group group : groups) {
            if (group.getCells().contains(cell)) {
                group.eliminate(number);
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
                puzzle.apply(puzzle.getCell(i % 9, i / 9), Integer.valueOf(String.valueOf(c)));
            }

            i++;
        }
        return puzzle;
    }

}
