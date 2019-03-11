package Service;

import Model.Cell;
import Model.CellMapper;
import Repository.CellRepository;
import Utils.CellUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

//@Service
public class CellExprProcessorService implements ICellExprProcessorService {

    private CellRepository _cellRepository;
    public CellExprProcessorService(CellRepository cellRepository) {
        _cellRepository= cellRepository;
    }

    @Override
    public List<CellMapper> getDependentCellMappersFromExpr(String expression, Cell cell){
        return CellUtils.Utils.splitToken(expression.replace("=",""))
                .filter(str-> !CellUtils.Utils.isNumber(str))
                .map(token-> CellMapper.getCell(token, cell.get_rowId(), cell.get_colId()))
                .collect(Collectors.toList());


    }

    @Override
    public void addToDependencySet(Cell cell, Hashtable<String, List<Cell>> dependencyDict){

        if(cell.get_dependencies()== null) return;

        for(Cell depCell: cell.get_dependencies()){
            String key = depCell.toString();
            if(!dependencyDict.containsKey(key)){
                dependencyDict.put(key, new ArrayList<>());
            }
            dependencyDict.get(key).add(cell);
        }
    }

    @Override
    public HashSet<Cell> buildDependencies(Cell cell, List<CellMapper> cellMappers) {//, HashMap<String, Cell> dictCells){

        return new HashSet<Cell>( cellMappers.stream()
                .map(cellMapper->{
                    String key = CellUtils.Utils.buildKey(cellMapper.get_row(), cellMapper.get_col());
                    Cell depCell= null;
                    if(_cellRepository.containsCellKey(key)) {
                        depCell = _cellRepository.getSheetCell(key);
                        if(ICellExprProcessorService.isFutureDependency(cellMapper))
                            depCell.addDependency(cell);
                    }
                    if(ICellExprProcessorService.isFutureDependency(cellMapper) && depCell ==null)
                    {
                        depCell= Cell.create(cellMapper.get_row(),cellMapper.get_col(),cellMapper.get_cellKey(), cell.get_expression());
                        depCell.addDependency(cell);
                        depCell.set_isResolved(false);
                        _cellRepository.addToSheetCell(key, depCell);
                    }

                    return depCell;
                })
                .collect(Collectors.toList()));

    }


}
