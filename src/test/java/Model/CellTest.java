package Model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@SpringBootTest(classes =  {Cell.class})
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class CellTest {

    Cell _testCell ;
    Cell _expCell;

    @Before
    public void init(){

        _testCell = Cell.create(1,0,"A0", 5);
        _expCell = Cell.create(1,1,"A1", "=A1+A2");
    }
    @Test
    public void whenCellIsCreated_thenCheckCellValueDetailsCorrect() {
        Double val = 5.0;
        int row=1;
        int col=0 ;
        Assert.assertEquals(val, _testCell.get_cellValue(),0);
        Assert.assertEquals(row,_testCell.get_rowId());
        Assert.assertEquals(col,_testCell.get_colId());
        Assert.assertEquals(Boolean.FALSE, _testCell.get_isExpression());
        Assert.assertEquals("", _testCell.get_expression());
    }

    @Test
    public void whenCellIsCreated_thenAllDependencyResolvedCorrect(){
       _testCell.addDependency(_expCell);
       boolean allResolved = _testCell.allDependenciesResolved();
       Assert.assertEquals(Boolean.FALSE, allResolved);

    }

    @Test
    public void whenCellIsCreated_thenCheckToStringCorrect(){
        String key = _testCell.toString();
        Assert.assertEquals("1|0", key);

    }

}
