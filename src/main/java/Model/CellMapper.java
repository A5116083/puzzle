package Model;

public class CellMapper {

    private int _row;
    private  int _col;
    private String _cellKey;
    private int _currRow;
    private int _currCol;


    private CellMapper(int row, int col, String cellKey, int currRow, int currCol) {
        this._row = row;
        this._col = col;
        this._cellKey = cellKey;
        this._currRow = currRow;
        this._currCol = currCol;
    }

    public int get_row() {
        return _row;
    }

    public int get_col() {
        return _col;
    }

    public String get_cellKey() {
        return _cellKey;
    }

    public int get_currRow() {
        return _currRow;
    }

    public int get_currCol() {
        return _currCol;
    }

    public static CellMapper getCell(String token, int currentRow, int currentCol) {
        try {
            int row = (int)token.charAt(0) % 65;
            int col = Integer.parseInt(token.substring(1,token.length()));

            return new CellMapper(row,col,token, currentRow, currentCol);


        }catch (NumberFormatException e) {
            System.out.println("Data cell format error" + token);
        }
        return null;
    }


    @Override
    public String toString() {
        return  String.format("%s|%s", String.valueOf(this.get_row()), String.valueOf(this.get_col()));
    }
}
