package com.javaworks.SpreadSheet;

import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpreadSheet {

    /*
    Tow dimensional dynamic list object to hold the spreadsheet cells
     */
    List<Cell>[] sheetCells;
    private final String operatorsRegex = "-|/+|/*|//|/(|/)";

    Hashtable<String, List<Cell>> dependencies ;

    public SpreadSheet() {
        //Holds the fixed Rows and dynamic cells
        List<Cell>[] sheetCells = new ArrayList[26];
        dependencies = new Hashtable<String, List<Cell>> ();
    }

    public List<Cell> transformToCells(final int rownumber, String rowEntry){

        AtomicInteger colCounter = new AtomicInteger(0);
        Stream<Cell>  rowCellsStream =
        Arrays.stream(rowEntry
                .split(","))
                .map(col ->
                {
                    int localCol = colCounter.incrementAndGet();
                   return Cell.create(rownumber, localCol-1, col);
                })
        .map(cell-> {

            if(cell.get_isExpression()){
                String expToEvaluate = cell.get_expression();
                //convert to stream of dependent cell mappers
                Stream<CellMapper> expTokens = splitToken(expToEvaluate)
                                    .map(token-> getCell(token, cell.get_rowId(), cell.get_colId()));

                cell.set_dependencies(expTokens.collect(Collectors.toCollection(HashSet::new)));

                if(cell.get_dependencies().stream().anyMatch(t-> t.hasFutureDependency())){

                    expTokens.forEach(token-> {
                        //Add dependency to the Hashable, key = Dependent cell row|col, Value=> current cell
                        if(dependencies.containsKey(token.toString())){
                            dependencies.get(token.toString()).add(cell);
                        }else {
                            List<Cell> cells = new ArrayList<Cell>();
                            cells.add(cell);
                            dependencies.put(cell.toString(),cells);
                        }
                    });

                    return cell;
                } else {
                    //Cell has no future dependency , Ok to evaluate and recursively evaluate previous dependencies

                    return evaluate(cell);
                }


            }

            this.sheetCells[rownumber].add(cell);
            return  cell;
        });

        List<Cell> rowCellsList = rowCellsStream.collect(Collectors.toList());
        sheetCells[rownumber].addAll(rowCellsList);
        return  rowCellsList;

    }
    public Stream<String> splitToken(String exp){
        final String operatorsRegex = "[-\\+\\*\\(\\)]";
        //String exp = "A0+A1*A2";
        Stream<String> expTokens =Stream.of(exp
                .split(operatorsRegex))
                .filter(t-> !StringUtils.isEmpty(t));

        return expTokens;

    }
    private Cell evaluate(Cell currentCell)
    {
       //AtomicReference<String> expression = new AtomicReference<>(currentCell.get_expression());
        String expression = currentCell.get_expression();
        HashSet<CellMapper> dependentCells = currentCell.get_dependencies();
        dependentCells.forEach(mapper-> {

            String cellToken = mapper.getCellKey();
            String dependecyKey = mapper.toString();

            //if(this.dependencies.containsKey(dependecyKey))
            {
                int row = mapper.getRow();
                int col = mapper.getCol();
                Cell refCell = this.sheetCells[row].get(col);

                //TODO: Detect cyclic dependency
                if(refCell.get_isExpression())
                    refCell = evaluate(refCell);
                expression.replace(cellToken, String.valueOf(refCell.get_cellValue()));
            }


        });

        currentCell.set_cellValue(computeEvalString(expression));
        return currentCell;
    }

    private double computeEvalString(String exp)
    {
        //TODO: Evaluate string expression using java script libraries
        return 1;
    }
    private  CellMapper getCell(String token, int currentRow, int currentCol) {
        try {
            int row = (int)token.charAt(0) % 65;
            int col = Integer.parseInt(token.substring(1,token.length()));

            return new CellMapper(row,col,token, currentRow, currentCol);


        }catch (NumberFormatException e) {
            System.out.println("Data cell format error" + token);
        }
        return null;
    }

    private boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }




}
