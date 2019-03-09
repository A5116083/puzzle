package com.javaworks.SpreadSheet;

import java.util.HashSet;

public class Cell
{
    private double _cellValue;
    private Boolean _isExpression;
    private int _rowId;
    private int _colId;
    private String _expression;
    private HashSet<Cell> _dependencies;

    public String getToken() {
        return token;
    }

    private String token;

    public void setResolved(boolean resolved) {
        isResolved = resolved;
    }

    private boolean isResolved;

    public boolean isResolved() {
        return isResolved;
    }
    public boolean allDependeciesResolved(){

        return _dependencies.stream().anyMatch(dep ->! dep.isResolved());
    }

    public double get_cellValue() {
        return _cellValue;
    }

    public void set_cellValue(double _cellValue) {
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

    public HashSet<Cell> get_dependencies() {
        return _dependencies;
    }

    public void set_dependencies(HashSet<Cell> _dependencies) {
        this._dependencies = _dependencies;
    }


    public static Cell create(int row, int col,String token, Object element){
        Cell cell = new Cell();
        cell.set_rowId(row);
        cell.set_colId(col);
        cell.token = token;
        setValue(cell,element);
        return cell;
    }

    public static void setValue(Cell cell, Object element){
        Boolean isExpr=isExpression(element);
        cell.set_isExpression(isExpr);
        if (isExpr) {
        } else {
            cell.set_cellValue(Integer.valueOf(element.toString()));

        }
    }

    public static Boolean isExpression(Object element){
        return element.toString().startsWith("=");
    }

    @Override
    public String toString() {
        return  buildKey(get_rowId(), get_colId());
    }

    public static String buildKey(int row, int col)
    {
        return   String.format("%s|%s", String.valueOf(row), String.valueOf(col));
    }






}
