package com.javaworks.SpreadSheet;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
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

        Arrays.stream(rowEntry
                .split(","))
                .map(col ->
                {
                    int localCol = colCounter.get();
                    colCounter.set(localCol++);
                   return Cell.create(rownumber, localCol, col);
                })
        .map(cell-> {

            if(cell.get_isExpression()){
                String expToEvaluate = cell.get_expression();
                Stream<CellMapper> expTokens =Stream.of(cell.get_expression()
                                    .split(operatorsRegex)).map(token-> getCell(token, rownumber, colCounter.get()));

                if(expTokens.anyMatch(t-> t.hasDependency())){

                    //Add dependency to the Hashable and leave it
                    if(dependencies.containsKey(cell.toString())){
                        //TODO:
                        dependencies.get(cell.toString()).add(cell);
                    }else {
                        List<Cell> cells = new ArrayList<Cell>();
                        cells.add(cell);
                        dependencies.put(cell.toString(),cells  );
                    }
                    return cell;
                } else {
                    //Cell has no dependency , Ok to evaluate
                    return evaluate(cell);
                }


            }
            //return the cell value
            return  cell;
        });


                return null;
    }

    private Cell evaluate(Cell cell)
    {

        return cell;
    }

    private  CellMapper getCell(String token, int currentRow, int currentCol) {
        try {
            int row = (int)token.charAt(0) % 65;
            int col = Integer.parseInt(token.substring(1,token.length()))-1;

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
