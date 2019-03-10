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

       createIntToAlphabetMap();
    }

    public String getRowAlphaExprKey(int row, int col)
    {
        String token = String.format("%s%s", _rowToAlphabet.get(row), col);
        return  token;
    }

    public void createIntToAlphabetMap() {

        for (int counter =1; counter < 27;counter++){
            String aplhabet = String.valueOf((char)(counter + 64));
            _rowToAlphabet.put(counter-1, aplhabet);
        }

    }

    public List<CellMapper> getDependentCellMappersFromExpr(String expression, Cell cell){
        return splitToken(expression.replace("=","")).filter(str-> !isNumber(str))
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
        return  ((cell.get_rowId() > currRow) || ((cell.get_rowId() >= currRow) && ( cell.get_colId() > currCol)));
    }

    public static Boolean isFutureDependency(CellMapper cell)
    {
        return  ((cell.getRow() > cell.getCurrRow()) || ((cell.getRow() >= cell.getCurrRow()) && ( cell.getCol() > cell.getCurrCol())));
    }

    public HashSet<Cell> buildDependencies(Cell cell, List<CellMapper> cellMappers, HashMap<String, Cell> dictCells){

        return new HashSet<Cell>( cellMappers.stream()
                .map(cellMapper->{
                    String key = Cell.buildKey(cellMapper.getRow(), cellMapper.getCol());
                    Cell depCell= null;
                    if(dictCells.containsKey(key))
                        depCell = dictCells.get(key);

                    //if(isFutureDependency(cell,cellMapper.getCurrRow(),cellMapper.getCurrCol()) && depCell ==null)
                    if(isFutureDependency(cellMapper) && depCell ==null)

                    {
                        depCell= Cell.create(cellMapper.getRow(),cellMapper.getCol(),cellMapper.getCellKey(), cell.get_expression());
                        HashSet<Cell> depCellsSet = new HashSet<>(); depCellsSet.add(cell);
                        depCell.set_dependencies(depCellsSet);
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
