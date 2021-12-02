import java.util.LinkedList;

public class Board {

    //La matrice potrei eliminarla una volta avuto i figli
    private short[][] tiles;
    private short[] zeroPos;
    private short prevMoved;

    public Board(short[][] tiles) {

        this.tiles = tiles;
        zeroPos = findZero();
        
    }

    public void setPrevMoved(short m) {
        prevMoved = m;
    }

    public short getPrevMoved() {
        return prevMoved;
    }

    public short[][] getTiles() {
        return tiles;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < Solver.boardSize; i++) {
            for(int j = 0; j < Solver.boardSize; j++) {
               s.append(tiles[i][j]);
               s.append(" ");
            }
        }
        return s.toString();
    }

    public int manhattan() {
        int sum = 0;
        for(int i = 0; i < Solver.boardSize; i++) {
            for(int j = 0; j < Solver.boardSize; j++) {

                int num;
                if(tiles[i][j] == 0)
                    continue;
                else
                    num = tiles[i][j];

                int linePos = (num - 1) / Solver.boardSize;
                int columnPos = (num - (linePos * Solver.boardSize)) - 1;

                sum += Math.abs(linePos - i) + Math.abs(columnPos - j); 
            }
        }
        return sum;
    }

    public LinkedList<Object[]> getMoves() {
        LinkedList<Object[]> sons = new LinkedList<>();
        
        if(!(zeroPos[0] - 1 < 0) && tiles[zeroPos[0] - 1][zeroPos[1]] != prevMoved) {
            Object[] son = new Object[2];
            short v = tiles[zeroPos[0] - 1][zeroPos[1]];

            son[0] = v;
            son[1] = getNewMatrixBoard(v);
            
            sons.add(son);
        }

        if(!(zeroPos[0] + 1 >= Solver.boardSize) && tiles[zeroPos[0] + 1][zeroPos[1]] != prevMoved) {
            Object[] son = new Object[2];
            short v = tiles[zeroPos[0] + 1][zeroPos[1]];

            son[0] = v;
            son[1] = getNewMatrixBoard(v);
            
            sons.add(son);
        }

        if(!(zeroPos[1] - 1 < 0) && tiles[zeroPos[0]][zeroPos[1] - 1] != prevMoved) {

            Object[] son = new Object[2];
            short v = tiles[zeroPos[0]][zeroPos[1] - 1];

            son[0] = v;
            son[1] = getNewMatrixBoard(v);
            
            sons.add(son);
        }

        if(!(zeroPos[1] + 1 >= Solver.boardSize) && tiles[zeroPos[0]][zeroPos[1] + 1] != prevMoved) {

            Object[] son = new Object[2];
            short v = tiles[zeroPos[0]][zeroPos[1] + 1];

            son[0] = v;
            son[1] = getNewMatrixBoard(v);
            
            sons.add(son);
        }

        return sons;
    }

    private short[][] getNewMatrixBoard(short v) {
        short[][] t = new short[Solver.boardSize][Solver.boardSize];
        for(int i = 0; i < Solver.boardSize; i++) {
            for(int j = 0; j < Solver.boardSize; j++) {
                if(tiles[i][j] == 0) { 
                    t[i][j] = v;
                }
                else if(tiles[i][j] == v) {
                    t[i][j] = 0;
                } else {
                    t[i][j] = tiles[i][j];
                }
            }
        }
        return t;
    }

    private short[] findZero() {
        for(short i = 0; i < Solver.boardSize; i++) {
            for(short j = 0; j < Solver.boardSize; j++) {
                if(tiles[i][j] == 0) {
                    short[] arr = {i, j};
                    return arr;
                }
            }

        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        for(int i = 0; i < Solver.boardSize; i++) {
            for(int j = 0; j < Solver.boardSize; j++) {
                if(this.getTiles()[i][j] != ((Board)o).getTiles()[i][j])
                    return false;
            }
        }

        //return toString().hashCode() == ((Board)o).toString().hashCode();

        return true;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
