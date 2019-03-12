package Repository;

import Model.Cell;
import Utils.CellRef;
import Utils.CellUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CellRepository implements ICellRepository {

    private HashMap<Integer,Cell>[] _sheetCells   = null;

    private Hashtable<String, List<Cell>> _dependencyDictionary = new Hashtable<>();

    public CellRepository() {
        _sheetCells   = new HashMap[26];
        _dependencyDictionary = new Hashtable<>();

        initSheetCells();
    }

    private void initSheetCells(){
        for(int count =0; count< 26;count ++){
            HashMap<Integer, Cell> cells = new HashMap<>();
            _sheetCells[count] = cells;
        }
    }

    public List<Cell>[] get_sheetCells() {

        Stream<List<Cell>> streamCellsList=  Arrays.stream(_sheetCells).map(cellMap-> {

            TreeMap<Integer, Cell> sortedCellsMap = new TreeMap<>(Collections.reverseOrder());
            sortedCellsMap.putAll(cellMap);
            return cellMap.values().stream().collect(Collectors.toList());
        });

        return streamCellsList.toArray(item-> new ArrayList[item]);

       /* List<Cell>[] sheetCellAsArray = new ArrayList[26];

        for(List<Cell> cellsList :streamCellsList){

        }*/

    }

    @Override
    public boolean containsCellKey(String key) {
        CellRef cellRef = CellUtils.Utils.buildCellRefFromKey(key);
        if(validateCellRef(cellRef)){
            HashMap<Integer, Cell> cells = _sheetCells[cellRef.getRow()];

            return cells.containsKey(cellRef.getCol());

            /*if(cells.isEmpty()) return false;
            if(cellRef.getCol() > cells.size()-1) return false;
            try {

                Cell keyCell = cells.get(cellRef.getCol());

                if (keyCell.toString().equals(key)) return true;
            }catch (IndexOutOfBoundsException ex){
                return false;
            }*/
        }
            return false;
    }
    @Override
    public Cell getSheetCell(String key){

        CellRef cellRef = CellUtils.Utils.buildCellRefFromKey(key);
        if(validateCellRef(cellRef)){
            return _sheetCells[cellRef.getRow()].get(cellRef.getCol());
        }
        return null;

    }

    @Override
    public void addToSheetCell(String key, Cell cell){
        CellRef cellRef = CellUtils.Utils.buildCellRefFromKey(key);
        if(validateCellRef(cellRef)){
            _sheetCells[cellRef.getRow()].put(cellRef.getCol(), cell);
        }

    }



    @Override
    public Hashtable<String, List<Cell>> get_dependencyDictionary() {
        return _dependencyDictionary;
    }



    @Override
    public List<Cell> getDependenciesOfCell(String key){
        return _dependencyDictionary.getOrDefault(key, null);
    }

    @Override
    public void addDependenciesOfCell(String key, Cell depCell){
        if(!_dependencyDictionary.containsKey(key))
            _dependencyDictionary.put(key, new ArrayList<Cell>());
        _dependencyDictionary.get(key).add(depCell);
    }

    @Override
    public boolean containsDependencyKey(String key){
        return _dependencyDictionary.containsKey(key);

    }

    private boolean validateCellRef(CellRef cellRef){
        if(cellRef.getCol() == -1 || cellRef.getCol() == -1) return false;
        if(cellRef.getCol()> 5000000 || cellRef.getCol()> 26) return false;
       return true;
    }



}
