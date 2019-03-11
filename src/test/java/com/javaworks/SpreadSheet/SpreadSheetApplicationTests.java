package com.javaworks.SpreadSheet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpreadSheetApplicationTests {


	//List<String> fileLines = new ArrayList<>();
	//SET A
	//fileLines.add("2,4,1,=A0+A1*A2"); //future dependency
	//fileLines.add("=A3*(A0+1),=B2,0,=A0+1");

	//SET B
	//fileLines.add("2,4,=B2,=A0+A1*A2"); //multiple future dependency
	//fileLines.add("=A3*(A0+1),=B2,0,=A0+1");

	//SET C
		/*fileLines.add("2,4,=B1,=A0+A1*A2");  //multi level future dependency
		fileLines.add("=A3*(A0+1),=B2,0,=A0+1");*/


	@Test
	public void contextLoads() {
	}

}
