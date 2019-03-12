package Service;

import Model.Cell;
import Model.CellMapper;
import Repository.CellRepository;
import Repository.ICellRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.HashSet;
import java.util.List;

@Service
public class CellDependencyService implements ICellDependencyService {

    @Autowired
    private ICellExprProcessorService _cellExprProcessorService;
    @Autowired
    private ICellRepository _cellRepository;

    ScriptEngine engine = null;

    public CellDependencyService(){//(CellExprProcessorService _cellExprProcessorService, CellRepository _cellRepository) {
        /*this._cellExprProcessorService = _cellExprProcessorService;
        this._cellRepository = _cellRepository;*/
        ScriptEngineManager mgr = new ScriptEngineManager();
        engine = mgr.getEngineByName("JavaScript");
    }

    @Override
    public  void resolveDependencies(Cell cell){
        if(cell.get_dependencies()== null) return;
        String key = cell.toString();
        if(_cellRepository.containsDependencyKey(key)) {
            List<Cell> cells = _cellRepository.getDependenciesOfCell(key);
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
    @Override
    public void manageDependencies(Cell cell){

        String expToEvaluate = cell.get_expression();
        List<CellMapper> depCellMappers = _cellExprProcessorService.getDependentCellMappersFromExpr(expToEvaluate, cell);
        HashSet<Cell> depCells =  _cellExprProcessorService.buildDependencies(cell, depCellMappers);
        cell.set_dependencies(depCells);
        _cellExprProcessorService.addToDependencySet(cell, _cellRepository.get_dependencyDictionary());
    }

    @Override
    public Cell evaluate(Cell currentCell)
    {
        String expression = currentCell.get_expression().replace("=","");
        for(Cell depCell:currentCell.get_dependencies())
        {

            String key = depCell.toString();
            String token = depCell.getToken();

            Cell refCell =  _cellRepository.getSheetCell(key);

            //TODO: Detect cyclic dependency

           /* if(refCell.get_isExpression())
                refCell = evaluate(refCell);*/

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

    }
}
