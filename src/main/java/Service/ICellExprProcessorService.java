package Service;

import Model.Cell;
import Model.CellMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;


public interface ICellExprProcessorService {

    static Boolean isFutureDependency(CellMapper cell)
    {
        return  ((cell.get_row() > cell.get_currRow()) || ((cell.get_row() >= cell.get_currRow()) && ( cell.get_col() > cell.get_currCol())));
    }

    List<CellMapper> getDependentCellMappersFromExpr(String expression, Cell cell);

    void addToDependencySet(Cell cell, Hashtable<String, List<Cell>> dependencyDict);

    HashSet<Cell> buildDependencies(Cell cell, List<CellMapper> cellMappers);//, HashMap<String, Cell> dictCells);
}
