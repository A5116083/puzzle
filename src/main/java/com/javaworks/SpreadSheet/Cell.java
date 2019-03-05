package com.javaworks.SpreadSheet;

import java.util.HashSet;

public class Cell
{
    private int _cellValue;
    private Boolean _isExpression;
    private int _rowId;
    private int _colId;
    private String _expression;
    //private HashSet<Cell> _dependencies;

    public int get_cellValue() {
        return _cellValue;
    }

    public void set_cellValue(int _cellValue) {
        this._cellValue = _cellValue;
    }

    public Boolean get_isExpression() {
        return _isExpression;
    }

    public void set_isExpression(Boolean _isExpression) {
        this._isExpression = _isExpression;
    }

    public int get_rowId() {
        return _rowId;
    }

    public void set_rowId(int _rowId) {
        this._rowId = _rowId;
    }

    public int get_colId() {
        return _colId;
    }

    public void set_colId(int _colId) {
        this._colId = _colId;
    }

    public String get_expression() {
        return _expression;
    }

    public void set_expression(String _expression) {
        this._expression = _expression;
    }

    /*public HashSet<Cell> get_dependencies() {
        return _dependencies;
    }

    public void set_dependencies(HashSet<Cell> _dependencies) {
        this._dependencies = _dependencies;
    }*/

    public static Cell create(int row, int col, Object element){
        Cell cell = new Cell();
        cell.set_rowId(row);
        cell.set_colId(col);

        setValue(cell,element);
        return cell;
    }

    public static void setValue(Cell cell, Object element){
        Boolean isExpr=isExpression(element);
        cell.set_isExpression(isExpr);
        if (isExpr) {
            cell.set_expression(element.toString().replace("=",""));
        } else {
            cell.set_cellValue(Integer.valueOf(element.toString()));
        }
    }

    public static Boolean isExpression(Object element){
        return element.toString().startsWith("=");
    }

    // Helper method
    /*private SheetCell getCell(String s) {

        try {
            int x = (int)s.charAt(0) % 65;
            int y = Integer.parseInt(s.substring(1,s.length()))-1;
            return sheetCells[x][y];
        }catch (NumberFormatException e) {
            System.out.println("Data format error occurred while evaluating Cell" + s);
            System.exit(1);
        }
        return null;

    }*/




    private static boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
            return true;
        }
        catch (NumberFormatException e) {
            // s is not numeric
            return false;
        }
    }
}
