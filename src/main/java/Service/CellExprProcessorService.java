package Service;

import com.javaworks.SpreadSheet.Cell;
import com.javaworks.SpreadSheet.CellMapper;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CellExprProcessorService {

    private HashMap<Integer, String> _rowToAlphabet = new HashMap<Integer, String>();

    public CellExprProcessorService() {
        _rowToAlphabet = _rowToAlphabet;
        _rowToAlphabet.put(0,"A");
        _rowToAlphabet.put(1,"B");
        _rowToAlphabet.put(2,"C");
        _rowToAlphabet.put(3,"D");
        _rowToAlphabet.put(4,"E");
    }

    public String getRowAlphaExprKey(int row, int col)
    {
        return String.format("%s%a", _rowToAlphabet.get(row), col);
    }

    public List<CellMapper> getDependentCellMappersFromExpr(String expression, Cell cell){
        return splitToken(expression).filter(str-> !isNumber(str))
                .map(token-> getCell(token, cell.get_rowId(), cell.get_colId()))
                .collect(Collectors.toList());


    }

    public void addToDependecySet(Cell cell, Hashtable<String, List<Cell>> dependencyDict){

        for(Cell depCell: cell.get_dependencies()){
            String key = depCell.toString();
            if(!dependencyDict.containsKey(key)){
                dependencyDict.put(key, new ArrayList<>());
            }
            dependencyDict.get(key).add(cell);
        }
    }

    public static Boolean isFutureDependency(Cell cell, int currRow, int currCol)
    {
        return  ((cell.get_colId() > currRow) || ((cell.get_colId() >= currRow) && ( cell.get_colId() > currCol)));
    }

    public HashSet<Cell> buildDependencies(Cell cell, List<CellMapper> cellMappers, HashMap<String, Cell> dictCells){

        return new HashSet<Cell>( cellMappers.stream()
                .map(cellMapper->{
                    String key = Cell.buildKey(cellMapper.getRow(), cellMapper.getCol());
                    Cell depCell= null;
                    if(dictCells.containsKey(key))
                        depCell = dictCells.get(key);

                    if(isFutureDependency(cell,cellMapper.getRow(),cellMapper.getCol()) && depCell ==null)
                    {
                        depCell= Cell.create(cellMapper.getRow(),cellMapper.getCol(),cellMapper.getCellKey(), 0);
                        depCell.setResolved(false);
                        dictCells.put(key, depCell);
                    }

                    return depCell;
                })
                //.filter(cellFilter->cellFilter!=null)
                .collect(Collectors.toList()));

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
    private Stream<String> splitToken(String exp){
        final String operatorsRegex = "[-\\+\\*\\(\\)]";
        //String exp = "A0+A1*A2";
        Stream<String> expTokens =Stream.of(exp
                .split(operatorsRegex))
                .filter(t-> !StringUtils.isEmpty(t))
                .distinct();

        return expTokens;

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
