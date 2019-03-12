package Repository;

import Model.Cell;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public interface ICellRepository {
    boolean containsCellKey(String key);
     List<Cell>[] get_sheetCells();

    Hashtable<String, List<Cell>> get_dependencyDictionary();

    Cell getSheetCell(String key);

    void addToSheetCell(String key, Cell cell);

    List<Cell> getDependenciesOfCell(String key);

    void addDependenciesOfCell(String key, Cell depCell);

    boolean containsDependencyKey(String key);
}
