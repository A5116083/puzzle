package com.javaworks.SpreadSheet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest

public class SpreadSheetTest {

    @Test
    public void transformToCells() throws Exception {

        List<String> fileLines = new ArrayList<>();
        fileLines.add("2,4,1,=A0+A1*A2");
        fileLines.add("=A3*(A0+1),=B2,0,=A0+1");

        SpreadSheet sheet = new SpreadSheet();
        AtomicInteger counter = new AtomicInteger(0);
        fileLines.stream().forEach(line -> {
            int rowCount = counter.get();
           sheet.transformToCells( rowCount, line);

            rowCount++;
            counter.set(rowCount);
        });
    }
    @Test
    public void splitToken(){
        String exp = "A0+A1*A2";
        SpreadSheet sheet = new SpreadSheet();
        //sheet.splitToken(exp);

    }


}