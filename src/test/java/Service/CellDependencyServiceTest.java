package Service;

import Model.Cell;
import Repository.CellRepository;
import Repository.ICellRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@SpringBootTest(classes = CellDependencyService.class)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CellRepository.class, CellExprProcessorService.class, CellDependencyService.class})
public class CellDependencyServiceTest {


    @Autowired
    private ICellRepository _cellRepository;
    @Autowired
    private ICellExprProcessorService _exprProcessorService;
    @Autowired
    private ICellDependencyService _cellDependencyService;
    private Cell _cell;
    private Cell _depCell;

    @Before
    public void init(){

        _cell = Cell.create(0,1,"A1", "=A0 +5");
        _depCell = Cell.create(0,0, "A0", "4");


    }

    @Test
    public void whenCellIsProvided_thenEvaluateExpressionCorrect() {

        _cell.addDependency(_depCell);
        _cellRepository.addToSheetCell(_depCell.toString(), _depCell);
        _cellDependencyService.evaluate(_cell);
        Assert.assertEquals(9, _cell.get_cellValue(), 0);

    }


}
