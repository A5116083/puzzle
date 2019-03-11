package com.javaworks.SpreadSheet;
import Model.Cell;
import Repository.CellRepository;
import Repository.ICellRepository;
import Service.CellDependencyService;
import Service.CellExprProcessorService;
import Service.ICellDependencyService;
import Service.ICellExprProcessorService;
import Utils.CellUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


//@Component
public class SpreadSheet {


    //private static  List<Cell>[] sheetCells;


    //@Autowired
    private CellRepository _cellRepository;
    //@Autowired
    private CellExprProcessorService _cellExprProcessorService;
    //@Autowired
    private CellDependencyService _cellDependencyService;


    public SpreadSheet() {
        _cellRepository = new CellRepository();
        _cellExprProcessorService = new CellExprProcessorService(_cellRepository);
        _cellDependencyService = new CellDependencyService(_cellExprProcessorService, _cellRepository);
    }

    public void transformToCells(final int rownumber, String rowEntry){
        int colnumber =0;
        for(String cellVal:rowEntry.split(","))
        {

            Cell cell = getOrCreate(cellVal, rownumber, colnumber);
            if(cell.get_isExpression()) {
                _cellDependencyService.manageDependencies(cell);
                if (cell.allDependenciesResolved()) {
                    _cellDependencyService.evaluate(cell);
                    _cellDependencyService.resolveDependencies(cell);
                }
            }
            else {
                cell.set_isResolved(true);
                _cellDependencyService.resolveDependencies(cell);
            }
            colnumber++;
        }

    }

    private Cell getOrCreate(String cellVal, int rownumber, int colCounter){

        String key = CellUtils.Utils.buildKey(rownumber, colCounter);
        Cell cell;
        //if(_cellRepository.get_cellDictionary().containsKey(key)) {
        if(_cellRepository.containsCellKey(key)) {
            cell = _cellRepository.getSheetCell(key);
            Cell.setValue(cell,cellVal);


        }
        else {
            cell = Cell.create(rownumber,
                    colCounter,
                    CellUtils.Utils.getRowAlphaExprKey(rownumber, colCounter), cellVal);

            _cellRepository.addToSheetCell(key, cell);
        }
        return cell;
    }

    /*private  void resolveDependencies(Cell cell ){
        if(cell.get_dependencies()== null) return;
        String key = cell.toString();
        if(dependencyDictionary.containsKey(key)) {
            List<Cell> cells = dependencyDictionary.get(key);
            for (Cell depCell : cells) {
                if (depCell.allDependenciesResolved()) {
                    evaluate(depCell);
                    //dependencyDictionary.get(key).remove(depCell);
                    resolveDependencies(depCell);
                }
                depCell.set_isResolved(true);
            }
            cell.set_isResolved(true);
        }

        //cell.get_dependencies().clear();
    }
    private void manageDependencies(Cell cell){

        String expToEvaluate = cell.get_expression();
        List<CellMapper> depCellMappers = _cellExprProcessorService.getDependentCellMappersFromExpr(expToEvaluate, cell);
        HashSet<Cell> depCells =  _cellExprProcessorService.buildDependencies(cell, depCellMappers, cellDictionary);
        cell.set_dependencies(depCells);
        _cellExprProcessorService.addToDependencySet(cell, dependencyDictionary);
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

           *//* if(refCell.get_isExpression())
                refCell = evaluate(refCell);*//*

            expression = expression.replace(token, String.valueOf(refCell.get_cellValue()));
        }
        currentCell.set_cellValue(computeExpression(expression));
        currentCell.set_isResolved(true);
        return currentCell;
    }


    private double computeExpression(String exp)
    {
        try {
            return (double) engine.eval(exp);
        } catch (ScriptException e) {
            e.printStackTrace();
            return  -1;
        }

    }*/







}
