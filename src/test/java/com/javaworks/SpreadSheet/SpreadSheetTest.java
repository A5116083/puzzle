package com.javaworks.SpreadSheet;

import Model.Cell;
import Repository.CellRepository;
import Repository.ICellRepository;
import Service.CellDependencyService;
import Service.CellExprProcessorService;
import Utils.CellUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest(classes = CellDependencyService.class)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CellRepository.class, CellExprProcessorService.class, CellDependencyService.class, SpreadSheet.class})

public class SpreadSheetTest {

    @Autowired
    private SpreadSheet sheet;

    Hashtable<String, List<String>> csvLines;
    DecimalFormat df = new DecimalFormat("0.00000");
    @Before
    public void setUp(){
        csvLines = new Hashtable<>();
        CellUtils.Utils.createIntToAlphabetMap();
        csvLines.put("future", getMockCsvLines1());
        csvLines.put("multiple", getMockCsvLines2());
        csvLines.put("multilevel", getMockCsvLines3());
        csvLines.put("cyclic", getMockCsvLines4());
    }

    @After
    public void destroy(){

    }

    private  List<String> getMockCsvLines1(){

        List<String> row = new ArrayList<>();
        row.add("2,4,1,=A0+A1*A2");
        row.add("=A3*(A0+1),=B2,0,=A0+1");
        return row;
    }
    private  List<String> getMockCsvLines2(){

        List<String> row = new ArrayList<>();
        row.add("2,4,=B2,=A0+A1*A2");
        row.add("=A3*(A0+1),=B2,0,=A0+1");
        return row;
    }

    private  List<String> getMockCsvLines3(){

        List<String> row = new ArrayList<>();
        row.add("2,4,=B1,=A0+A1*A2");
        row.add("=A3*(A0+1),=B2,0,=A0+1");
        return row;
    }
    private  List<String> getMockCsvLines4(){

        List<String> row = new ArrayList<>();
        row.add("=B3,4,1,=A0+A1*A2");
        row.add("=A3*(A0+1),=B2,0,=A0+1");
        return row;
    }


    @Test
    public void whenCsvLinesAreProvided_thenComputeFutureShellsCorrect() throws Exception {

        List<String> csvCells = csvLines.get("future");
        int rowCounter = 0;
        for(String line:csvCells){
            sheet.transformToCells(rowCounter, line);
            rowCounter++;
        }

        List<String> outputRows = getFormattedRows();

        List<String> expectedOutPut = new ArrayList<>();
        expectedOutPut.add("2.00000,4.00000,1.00000,6.00000");
        expectedOutPut.add("18.00000,0.00000,0.00000,3.00000");
        Assert.assertEquals(expectedOutPut, outputRows);

    }



    @Test
    public void whenCsvLinesAreProvided_thenComputeMultipleRefCorrect() throws Exception {

        List<String> csvCells = csvLines.get("multiple");
        int rowCounter = 0;
        for(String line:csvCells){
            sheet.transformToCells(rowCounter, line);
            rowCounter++;
        }

        List<String> outputRows = getFormattedRows();

        List<String> expectedOutPut = new ArrayList<>();

        expectedOutPut.add("2.00000,4.00000,0.00000,2.00000");
        expectedOutPut.add("6.00000,0.00000,0.00000,3.00000");
        Assert.assertEquals(expectedOutPut, outputRows);

    }
    /*
    Below Test case works when run individually
     */
    @Test
    public void whenCsvLinesAreProvided_thenComputeMultiLevelRefCorrect() throws Exception {

        List<String> csvCells = csvLines.get("multilevel");
        int rowCounter = 0;
        for(String line:csvCells){
            sheet.transformToCells(rowCounter, line);
            rowCounter++;
        }
        List<String> outputRows = getFormattedRows();
        List<String> expectedOutPut = new ArrayList<>();
        expectedOutPut.add("2.00000,4.00000,0.00000,2.00000");
        expectedOutPut.add("6.00000,0.00000,0.00000,3.00000");
        Assert.assertEquals(expectedOutPut, outputRows);

    }

    private List<String> getFormattedRows() {
        List<Cell>[] sheetCells =  sheet.getSheetCells();
        List<String> outputRows = new ArrayList<>();
        for (List<Cell> cells : Arrays.stream(sheetCells)
                .filter(c-> c!=null)
                .filter(c->!c.isEmpty())
                .collect(Collectors.toList())) {

            String formattedRow = cells.stream()
                    .map(c -> df.format(c.get_cellValue()))
                    .collect(Collectors.joining(","));

            outputRows.add(formattedRow);
        }
        return outputRows;
    }
    @Test
    public void splitToken(){
        String exp = "A0+A1*A2";
        SpreadSheet sheet = new SpreadSheet();

    }


}