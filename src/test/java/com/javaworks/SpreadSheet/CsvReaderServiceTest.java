package com.javaworks.SpreadSheet;

import Model.Cell;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CsvReaderServiceTest {

    @Test
    public void whenWriteStringUsingBufferedWritter_thenCorrect(){



    }

    private static List<Cell>[] getMockCells(){

        List<Cell>[] spreadSheet = new ArrayList[2];
        List<Cell> row1 = new ArrayList<>();
        row1.add(0, Cell.create(0,0, "A0", 1));
        row1.add(1, Cell.create(1,0, "A1", 2));
        row1.add(2, Cell.create(2,0, "A2", 3));
        row1.add(3, Cell.create(3,0, "A3", 4));
        spreadSheet[0]= row1;

        return spreadSheet;

    }
}
