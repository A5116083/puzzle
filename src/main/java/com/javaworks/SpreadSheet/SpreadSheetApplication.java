package com.javaworks.SpreadSheet;

import Utils.CellUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
@ComponentScan(basePackages = {"Repository", "Service","com.javaworks.SpreadSheet"})
public class SpreadSheetApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpreadSheetApplication.class, args);
		CellUtils.Utils.createIntToAlphabetMap();

		List<String> fileLines = new ArrayList<>();
		//SET A
		fileLines.add("2,4,1,=A0+A1*A2"); //future dependency
		fileLines.add("=A3*(A0+1),=B2,0,=A0+1");

		//SET B
		//fileLines.add("2,4,=B2,=A0+A1*A2"); //multiple future dependency
		//fileLines.add("=A3*(A0+1),=B2,0,=A0+1");

		//SET C
		/*fileLines.add("2,4,=B1,=A0+A1*A2");  //multi level future dependency
		fileLines.add("=A3*(A0+1),=B2,0,=A0+1");*/

		SpreadSheet sheet = new SpreadSheet();
		AtomicInteger counter = new AtomicInteger(0);
		fileLines.stream().forEach(line -> {
			int rowCount = counter.get();
			sheet.transformToCells( rowCount, line);

			rowCount++;
			counter.set(rowCount);
		});


	}

}
