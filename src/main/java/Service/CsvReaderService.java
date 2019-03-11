package Service;

import java.io.*;
import java.util.stream.Stream;

public class CsvReaderService  {


    public Stream<String> processCsvFile(String inputFilePath) {
        Stream<String> csvLines = Stream.empty();
        try{
            File inputF = new File(inputFilePath);
            InputStream inputFS = new FileInputStream(inputF);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
            // skip the header of the csv
            csvLines = br.lines();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return csvLines ;
    }
}
