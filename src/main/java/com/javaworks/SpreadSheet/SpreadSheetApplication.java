package com.javaworks.SpreadSheet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class SpreadSheetApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpreadSheetApplication.class, args);

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

}
