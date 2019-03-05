package com.javaworks.SpreadSheet;

public class CellMapper {

    private int row;
    private  int col;
    private String cellKey;
    private int currRow;
    private int currCol;


    public CellMapper(int row, int col, String cellKey, int currRow, int currCol) {
        this.row = row;
        this.col = col;
        this.cellKey = cellKey;
        this.currRow = currRow;
        this.currCol = currCol;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getCellKey() {
        return cellKey;
    }

    public  Boolean hasDependency()
    {

        if((row > currRow) || (col > currCol))
            return true;
        else return false;
    }

    @Override
    public String toString() {
        return  String.format("%s|%s", String.valueOf(this.getRow()), String.valueOf(this.getCol()));
    }
}
