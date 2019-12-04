package sample;


public class PositionId {
    public int row;
    public int col;

    PositionId(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        PositionId position = (PositionId) o;

        if(row != position.row) return false;
        if(col != position.col) return false;

        return true;
    }

    public int hashCode(){
        int result = row;
        result = 31 * result + col;
        return result;
    }
}
