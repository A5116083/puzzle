package com.javaworks.SpreadSheet;

import Service.CsvReaderService;
import Service.CsvWriterService;
import Utils.CellUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import java.io.IOException;
import java.util.stream.Stream;

@SpringBootApplication
@ComponentScan({"com.javaworks.SpreadSheet","Service","Repository"})
public class SpreadSheetApplication implements CommandLineRunner {

    @Autowired
	private  CsvWriterService writerService;

    @Autowired
	private  CsvReaderService readerService;

	@Autowired
	private SpreadSheet sheet;


	public static void main(String[] args) {
		SpringApplication.run(SpreadSheetApplication.class, args);
	}
	@Override

	public void run(String... args) {
		System.out.println("EXECUTING : command line runner");

		ArgsHolder argsHolder = processArgs(args);
		System.out.println(argsHolder.toString());

		CellUtils.Utils.createIntToAlphabetMap();

		Stream<String> inputStream =  readerService.processCsvFile(argsHolder.get_inputFileName());

		System.out.println("Reading Input file ");

		inputStream.forEach(line-> {
			System.out.println(line);
			sheet.transformToCells( ArgsHolder.FileLineCounter, line);
			ArgsHolder.FileLineCounter++;
		});

		try {
			System.out.println("Starting output file generation process");
			writerService.writeToCsv(argsHolder.get_outputFileName(), sheet.getSheetCells());
			System.out.println("File generated successfully");
		}catch (IOException ex) {
			ex.printStackTrace();
		}
	}


	private ArgsHolder processArgs(String[] args) {
		if(args.length!= 4 ) {
			System.out.println(" invalid arguments, expects four arguments in format -i inputCsv -o outputCsv ");
		}
		String inputFileName =null;
		String outputFileName = null;

		if(args[0].equals("-i")){
		inputFileName = args[1];
		} else if (args[2].equals("-i")){
			inputFileName = args[3];
		}

		if(args[0].equals("-o")){
			outputFileName = args[1];
		} else if (args[2].equals("-o")){
			outputFileName = args[3];
		}
		return new ArgsHolder(inputFileName, outputFileName);
	}


	private static class ArgsHolder{
		private String _inputFileName;
		private String _outputFileName;
		public static int FileLineCounter=0;

		public ArgsHolder(String inputFileName, String outputFileName) {
			this._inputFileName = inputFileName;
			this._outputFileName = outputFileName;
		}

		public String get_inputFileName() {
			return _inputFileName;
		}

		public String get_outputFileName() {
			return _outputFileName;
		}

		@Override
		public String toString() {
			return String.format("InputFileName: %s, OutputFileName : %s", _inputFileName, _outputFileName);
		}
	}

}
