package Service;

import Model.Cell;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CsvWriterService {

    public void writeToCsv(String fileName, List<Cell>[] sheetCells)
            throws IOException {
        DecimalFormat df = new DecimalFormat("0.00000");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))){
            for (List<Cell> cells : Arrays.stream(sheetCells)
                                    .filter(c-> c!=null)
                                    .filter(c->!c.isEmpty())
                                    .collect(Collectors.toList())) {

                String formattedRow = cells.stream()
                        .map(c -> df.format(c.get_cellValue()))
                        .collect(Collectors.joining(","));
                System.out.println(formattedRow);
                writer.write(formattedRow);
                writer.newLine();
            }
           // writer.close();
            System.out.println("file written to output location");
        }catch (IOException ex){
            ex.printStackTrace();

            throw  new IOException("Failed to write output csv");

        }
    }
}
