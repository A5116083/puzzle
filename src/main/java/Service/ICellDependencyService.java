package Service;

import Model.Cell;
import org.springframework.stereotype.Service;


public interface ICellDependencyService {
    void resolveDependencies(Cell cell);

    void manageDependencies(Cell cell);

    Cell evaluate(Cell currentCell);
}
