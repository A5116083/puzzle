package Repository;


import Model.Cell;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes =  {CellRepository.class})
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class CellRepositoryTest {

    List<Cell> _sheetLineEntry = null;

    @Before
    public void init(){
      _sheetLineEntry =   getMockRow();
    }
    private  List<Cell> getMockRow(){
        List<Cell> row1 = new ArrayList<>();
        row1.add(0, Cell.create(0,0, "A0", 1));
        row1.add(1, Cell.create(0,1, "A1", 2));
        row1.add(2, Cell.create(0,2, "A2", 3));
        row1.add(3, Cell.create(0,3, "A3", 4));
        return row1;
    }

    @Test
    public void whenSheetIsInit_thenCheckRowCountCorrect() {
        CellRepository repository = new CellRepository();
        List<Cell>[] sheets=  repository.get_sheetCells();
        Assert.assertEquals(sheets.length, 26);
    }

    @Test
    public void whenRowIsAddedToSheet_thenValidateEntryCorrect() {
        CellRepository repository = new CellRepository();
        _sheetLineEntry.forEach(cell -> {
            String key = cell.toString();
            repository.addToSheetCell(key, cell);

        });
        List<Cell> cellsFromRepo = repository.get_sheetCells()[0];

        Assert.assertEquals(_sheetLineEntry, cellsFromRepo);
    }
    @Test
    public void whenRowIsAddedToSheet_thenGetByIndexPositionCorrect() {
        CellRepository repository = new CellRepository();
        _sheetLineEntry.forEach(cell -> {
            String key = cell.toString();
            repository.addToSheetCell(key, cell);

        });

        Cell cell = repository.getSheetCell("0|2");

        Assert.assertEquals("0|2", cell.toString());
    }

    @Test
    public void whenRowIsAddedToSheet_thenCheckContainsKeyCorrect() {
        CellRepository repository = new CellRepository();
        _sheetLineEntry.forEach(cell -> {
            String key = cell.toString();
            repository.addToSheetCell(key, cell);

        });

        boolean containsCellKey = repository.containsCellKey("0|2");

        Assert.assertEquals(Boolean.TRUE, containsCellKey);
    }

    @Test
    public void whenDependencyIsAdded_thenGetDependencyByKeyCorrect() {
        CellRepository repository = new CellRepository();
        Cell depCell = Cell.create(0,0, "A0", 1);
        repository.addDependenciesOfCell("1|2", depCell);

        Cell depCellFromRepo = repository.getDependenciesOfCell("1|2").get(0);
        Assert.assertEquals(depCell.toString(), depCellFromRepo.toString());
    }

    @Test
    public void whenDependencyIsAdded_thenContainsKeyCorrect() {
        CellRepository repository = new CellRepository();
        Cell depCell = Cell.create(0,0, "A0", 1);
        repository.addDependenciesOfCell("1|2", depCell);

        boolean conatinsKey = repository.containsDependencyKey("1|2");
        Assert.assertEquals(Boolean.TRUE, conatinsKey);
    }



}