import java.util.LinkedList;

public class Board {

    //La matrice potrei eliminarla una volta avuto i figli
    private int[][] tiles;
    private StringBuilder bTiles;
    private int zeroPos;
    private int prevMoved;
    private int manDist;

    public Board(int[][] tiles) {
        this.tiles = tiles;
        zeroPos = findZero();
    }

    private Board(StringBuilder s, int[] zPos, int prevMov, int prevManhattan) {
        tiles = null;
        this.bTiles = s;
        zeroPos = zPos();
        prevMoved = prevMov;            

        manDist = prevManhattan;
    }

    public short getPrevMoved() {
        return prevMoved;
    }
    
    /*
    public short[][] getTiles() {
        return tiles;
    }
    */
    
    /*
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
    */

    public int manhattan() {
        if(tiles == null)
            return null;

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

    //Have to decide if return direct the right array af Object with all the information
    //Or return only the string builder
    //
    //OBVIOUSLY is better the first idea
    private StringBuilder skipLine(char dir) {
        StringBuilder son = new StringBuilder(bTiles);
        int counter = 0; 
        int index = zeroPos;
        //index used for the replace, other side of the index
        int otherIndex;
        int moved;
        boolean done = false;

        switch(dir) {
            case 'u': //up
                index --; 

                while(index >= 0 || counter < Solver.boardSize) {
                    if(bTiles.charAt(index) == ' ') {
                        otherIndex = index;
                        counter++; 
                    }
                    index--; 
                }

                if(counter == Solver.boardSize) {
                    String strMoved = Integer.parseInt(son.substring(index, otherIndex));
                    moved = (int)strMoved;
                    son.replace(index, otherIndex, intMovement);
                    son.replace(zeroPos, zeroPos, strMoved);
                    done = true;
                }

                break;

            case 'd': //down
                
                index ++;

                while(index <= bTiles.length() || counter < Solver.boardSize) {
                    if(bTiles.charAt(index) == ' ') {
                        otherIndex = index;
                        counter++; 
                    }
                    index++; 
                }

                if(counter == Solver.boardSize) {
                    String strMoved = Integer.parseInt(son.substring(index, otherIndex));
                    moved = (int)strMoved;
                    son.replace(otherIndex, index, intMovement);
                    son.replace(zeroPos, zeroPos, strMoved);
                    done = true;
                }

                break;
        }



    }

    public LinkedList<Object[]> getMoves() {

        LinkedList<Object[]> sons = new LinkedList<>();
        Object[] son;

        
        

        /*
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
        */
    }

    private StringBuilder getNewStr(short v) {

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
