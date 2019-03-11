package Service;

import Model.Cell;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

public class CsvReaderService {

    public void writeToCsv(String fileName, List<Cell>[] sheetCells)
        throws IOException{
        DecimalFormat df = new DecimalFormat("#.000000");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))){
            for (List<Cell> cells : sheetCells) {

                String formattedRow = cells.stream()
                        .map(c -> df.format(c.get_cellValue()))
                        .collect(Collectors.joining(","));
                writer.write(formattedRow);
            }
        }catch (IOException ex){
            ex.printStackTrace();

            throw  new IOException("Failed to write output csv");

        }
    }

}
