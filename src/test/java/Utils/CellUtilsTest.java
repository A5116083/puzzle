package Utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.stream.Stream;

@SpringBootTest(classes =  {CellUtils.Utils.class})
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class CellUtilsTest {

    @Test
    public void whenRowColIsProvided_thenGetAlphaExprKeyCorrect() {
        CellUtils.Utils.createIntToAlphabetMap();
        String tokenKey = CellUtils.Utils.getRowAlphaExprKey(1,1);
        Assert.assertEquals("B1", tokenKey);
    }


    @Test
    public void whenExpressionIsProvided_thenGetOnlyTokenArrayCorrect() {

        String[] tokensActual =new String[] {"A0","A1","A2"};
        Stream<String> tokensStream = CellUtils.Utils.splitToken("=A0+A1*A2");

        String[] tokensArray = tokensStream.toArray(String[]::new);
        Assert.assertEquals(tokensArray,tokensActual);
    }

    @Test
    public void whenStringIsProvided_thenCheckIsNumberCorrect() {
        boolean number = CellUtils.Utils.isNumber("1");
        Assert.assertEquals(number,Boolean.TRUE);
    }
    @Test
    public void whenStringIsProvided_thenCheckIsNumberNotCorrect() {
        boolean number = CellUtils.Utils.isNumber("A1");
        Assert.assertEquals(number,Boolean.FALSE);
    }

    @Test
    public void whenStringIsProvided_thenGetNumberCorrect() {
        int number = CellUtils.Utils.getNumber("1");
        Assert.assertEquals(number,1);
    }
    @Test
    public void whenStringIsProvided_thenGetNumberNotCorrect() {
        int number = CellUtils.Utils.getNumber("A1");
        Assert.assertEquals(number,-1);
    }

    @Test
    public void whenStringIsProvided_thenGetCellRefObjectCorrect(){
        String cellRefString = "1|1";
        CellRef cellRef = CellUtils.Utils.buildCellRefFromKey(cellRefString);
        Assert.assertEquals(cellRef.col, 1);
        Assert.assertEquals(cellRef.row, 1);

    }
    @Test
    public void whenStringIsProvided_thenCheckIsExpCorrect(){
        String expString = "=A0+A1*A2";
        boolean isExp = CellUtils.Utils.isExpression(expString);
        Assert.assertEquals(isExp, Boolean.TRUE);
    }

    @Test
    public void whenStringIsProvided_thenCheckIsExpNotCorrect(){
        String expString = "A0+A1*A2";
        boolean isExp = CellUtils.Utils.isExpression(expString);
        Assert.assertEquals(isExp, Boolean.FALSE);

    }

    @Test
    public void whenRowColIntIsProvided_thenBuildCellRefKeyString(){
        int row =2; int col =3;
        String rowRefKey = CellUtils.Utils.buildKey(row, col);
        Assert.assertEquals(rowRefKey, "2|3");

    }



}
