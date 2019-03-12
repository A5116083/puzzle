package Utils;

import org.springframework.util.StringUtils;
import java.util.HashMap;
import java.util.stream.Stream;

public  class  CellUtils {

    public static  class  Utils {

        private static  HashMap<Integer, String> _rowToAlphabet = new HashMap<Integer, String>();

        public static String getRowAlphaExprKey(int row, int col)
        {
            String token = String.format("%s%s", _rowToAlphabet.get(row), col);
            return  token;
        }

        public static void createIntToAlphabetMap() {

            for (int counter =1; counter < 27;counter++){
                String aplhabet = String.valueOf((char)(counter + 64));
                _rowToAlphabet.put(counter-1, aplhabet);
            }

        }

        public static Stream<String> splitToken(String exp){
            final String operatorsRegex = "[-\\+\\*\\(\\)]";

            Stream<String> expTokens =Stream.of(exp.replace("=","")
                    .split(operatorsRegex))
                    .filter(t-> !StringUtils.isEmpty(t))
                    .distinct();

            return expTokens;

        }


        public static boolean isNumber(String s) {
            try {
                Double.parseDouble(s);
                return true;
            }
            catch (NumberFormatException e) {
                return false;
            }
        }

        public static int getNumber(String s) {
            try {
                return Integer.parseInt(s);
            }
            catch (NumberFormatException e) {
                return -1;
            }
        }


        public static Boolean isExpression(Object element){
            return element.toString().startsWith("=");
        }
        public static String buildKey(int row, int col){

            return   String.format("%s|%s", String.valueOf(row), String.valueOf(col));
        }

        public static CellRef buildCellRefFromKey(String key){


            String[] indexers = key.split("[|]");
            if(indexers.length==2){
               int row = getNumber(indexers[0]);
               int col = getNumber(indexers[1]);
               return  new  CellRef(row, col);
            }
            return new CellRef(-1,-1);
        }


    }

}
