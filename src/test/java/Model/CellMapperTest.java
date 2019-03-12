package Model;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes =  {CellMapper.class})
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class CellMapperTest {

    CellMapper _mapper;
    @Before
    public void init(){

        _mapper = CellMapper.getCell("A0",1, 1);
    }

    @Test
    public void whenCellIsCreated_thenAllDependencyResolvedCorrect(){
       int row= _mapper.get_row();
       int col = _mapper.get_col();
       int currRow = _mapper.get_currRow();
       int currCol = _mapper.get_currCol();
       String token = _mapper.get_cellKey();
       Assert.assertEquals(0,row);
       Assert.assertEquals(col, 0);
       Assert.assertEquals(1, currRow);
       Assert.assertEquals(1, currCol);
       Assert.assertEquals("A0", token);

    }
}
