package com.javaworks.SpreadSheet;
import Service.CellExprProcessorService;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class SpreadSheet {


    private static  List<Cell>[] sheetCells;

    HashMap<String,Cell> cellDictionary=new HashMap<String,Cell>();

    private CellExprProcessorService _cellExprProcessorService;


    Hashtable<String, List<Cell>> dependencyDictionary;
    ScriptEngine engine = null;

    public SpreadSheet() {
        //Holds the fixed Rows and dynamic cells
        sheetCells = new ArrayList[26];
        dependencyDictionary = new Hashtable<String, List<Cell>> ();
        ScriptEngineManager mgr = new ScriptEngineManager();
        engine = mgr.getEngineByName("JavaScript");
        _cellExprProcessorService = new CellExprProcessorService();
    }

    public void transformToCells(final int rownumber, String rowEntry){

        if(sheetCells[rownumber]== null)
            sheetCells[rownumber] = new ArrayList<>();

        int colnumber =0;
        for(String cellVal:rowEntry.split(","))
        {

            Cell cell = getOrCreate(cellVal, rownumber, colnumber);
            if(cell.get_isExpression()) {
                manageDependencies(cell);
                if (cell.allDependeciesResolved()) {
                    evaluate(cell);
                    resolveDependencies(cell);
                }
            }
            else {
                cell.setResolved(true);
                resolveDependencies(cell);
            }

            colnumber++;
        }


    }
    private void getDependecyFromOtherCell(Cell cell) {
        String key = cell.toString();
        if(dependencyDictionary.containsKey(key)){
            HashSet<Cell> depCells = dependencyDictionary.get(key).stream().collect(Collectors.toCollection(HashSet::new));
            cell.set_dependencies(depCells);


        }
    }

    private  void resolveDependencies(Cell cell ){
        if(cell.get_dependencies()== null) return;
        String key = cell.toString();
        if(dependencyDictionary.containsKey(key)) {
            List<Cell> cells = dependencyDictionary.get(key);
            for (Cell depCell : cells) {
                if (depCell.allDependeciesResolved()) {
                    evaluate(depCell);
                    //dependencyDictionary.get(key).remove(depCell);
                    resolveDependencies(depCell);
                }
            }
        }

        //cell.get_dependencies().clear();
    }
    private void manageDependencies(Cell cell){

        String expToEvaluate = cell.get_expression();
        List<CellMapper> depCellMappers = _cellExprProcessorService.getDependentCellMappersFromExpr(expToEvaluate, cell);
        HashSet<Cell> depCells =  _cellExprProcessorService.buildDependencies(cell, depCellMappers, cellDictionary);
        cell.set_dependencies(depCells);
        _cellExprProcessorService.addToDependecySet(cell, dependencyDictionary);
    }
    private Cell getOrCreate(String cellVal, int rownumber, int colCounter){
        //int localCol = colCounter.incrementAndGet();
        String key = Cell.buildKey(rownumber, colCounter);
        Cell cell= null;
        if(cellDictionary.containsKey(key)) {
            cell = cellDictionary.get(key);
            Cell.setValue(cell,cellVal);
            //getDependecyFromOtherCell(cell);

        }
        else {
            cell = Cell.create(rownumber,
                    colCounter,
                    _cellExprProcessorService.getRowAlphaExprKey(rownumber, colCounter), cellVal);
            cellDictionary.put(key, cell);
        }
        return cell;
    }


    private Cell evaluate(Cell currentCell)
    {
        String expression = currentCell.get_expression().replace("=","");
        for(Cell depCell:currentCell.get_dependencies())
        {

            String key = depCell.toString();
            String token = depCell.getToken();

            Cell refCell = cellDictionary.get(key);

            //TODO: Detect cyclic dependency

           /* if(refCell.get_isExpression())
                refCell = evaluate(refCell);*/

            expression = expression.replace(token, String.valueOf(refCell.get_cellValue()));
        }
        currentCell.set_cellValue(computeExpression(expression));
        currentCell.setResolved(true);
        return currentCell;
    }


    private double computeExpression(String exp)
    {
        try {
            return (double) engine.eval(exp);
        } catch (ScriptException e) {
            //TODO: OK to swallow exception or not ?
            e.printStackTrace();
            return  -1;
        }

    }







}
