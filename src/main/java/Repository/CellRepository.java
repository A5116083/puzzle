package Repository;

import Model.Cell;
import Utils.CellRef;
import Utils.CellUtils;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

//@Service
public class CellRepository implements ICellRepository {

    //private HashMap<String,Cell> _cellDictionary =new HashMap<>();
    private List<Cell>[] _sheetCells   = new ArrayList[26];

    private Hashtable<String, List<Cell>> _dependencyDictionary = new Hashtable<>();

    public CellRepository() {
        _sheetCells   = new ArrayList[26];
        _dependencyDictionary = new Hashtable<>();
        //_cellDictionary =new HashMap<>();
        initSheetCells();
    }

    private void initSheetCells(){
        for(int count =0; count< 26;count ++){
            List<Cell> cells = new ArrayList<>();
            _sheetCells[count] = cells;
        }
    }

    @Override
    public boolean containsCellKey(String key) {
        CellRef cellRef = CellUtils.Utils.buildCellRefFromKey(key);
        if(validateCellRef(cellRef)){
            List<Cell> cells = _sheetCells[cellRef.getRow()];

            if(cells.isEmpty()) return false;
            if(cellRef.getRow() > cells.size()) return false;
            try {

                Cell keyCell = cells.get(cellRef.getCol());

                if (keyCell.toString().equals(key)) return true;
            }catch (IndexOutOfBoundsException ex){
                return false;
            }
        }
            return false;
    }

    @Override
    public Hashtable<String, List<Cell>> get_dependencyDictionary() {
        return _dependencyDictionary;
    }



    @Override
    public Cell getSheetCell(String key){

        CellRef cellRef = CellUtils.Utils.buildCellRefFromKey(key);
        if(validateCellRef(cellRef)){
            return _sheetCells[cellRef.getRow()].get(cellRef.getCol());
        }
        return null;
        //return  _cellDictionary.getOrDefault(key, null);
    }

    @Override
    public void addToSheetCell(String key, Cell cell){
        CellRef cellRef = CellUtils.Utils.buildCellRefFromKey(key);
        if(validateCellRef(cellRef)){
            _sheetCells[cellRef.getRow()].add(cellRef.getCol(), cell);
        }
//        _cellDictionary.put(key, cell);
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
