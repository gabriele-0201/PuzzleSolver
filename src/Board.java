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
                if(tiles[i][j] == 0) // count nothing if is zero
                    continue;
                else
                    num = tiles[i][j];

                int[] rightPos = getRightPos(num);

                sum += Math.abs(rightPos[0] - i) + Math.abs(rightPos[1] - j); 
            }
        }
        return sum;
    }

    private int[] getRightPos(int v) {
        int[] rightPos = new int[2];
        rightPos[0]= (v - 1) / Solver.boardSize;
        rightPos[1] = (v - (rightPos[0] * Solver.boardSize)) - 1;
        return rightPos;
    }

    // --> Have to decide if return direct the right array af Object with all the information
    //Or return only the string builder
    //
    //OBVIOUSLY is better the first idea
    private Object[] makeMove(char dir) {
        Object[] son = null;
         
        StringBuilder strSon = new StringBuilder(bTiles);

        int counter = 0; 
        int index = zeroPos;
        //index used for the replace, other side of the index
        int otherIndex;
        int moved;
        boolean done = false;

        switch(dir) {
            case 'u': //up
                index --; 
               
                // go until find the right value to replace
                while(index >= 0 || counter < Solver.boardSize) {
                    if(bTiles.charAt(index) == ' ') {
                        otherIndex = index;
                        counter++; 
                    }
                    index--; 
                }
                
                // replace the value and store the moved char
                if(counter == Solver.boardSize) {
                    String strMoved = Integer.parseInt(strSon.substring(index, otherIndex));
                    moved = (int)strMoved;
                    strSon.replace(index, otherIndex, intMovement);
                    strSon.replace(zeroPos, zeroPos, strMoved);
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
                    String strMoved = Integer.parseInt(strSon.substring(index, otherIndex));
                    moved = (int)strMoved;
                    strSon.replace(otherIndex, index, intMovement);
                    strSon.replace(zeroPos, zeroPos, strMoved);
                    done = true;
                }

                break;

            case 'r':

                break;

            case 'l':

                break;
        }

        if(done) {
            son = new Object[4];
            son[0] = strSon;
            son[1] = moved;
            
            int[] newPos;

            if(dir == 'u') {
                son[2] = zeroPos - Solver.boardSize;
                newPos = getRightPos(zeroPos - Solver.boardSize)
            }
            else if(dir == 'd') {
                son[2] = zeroPos + Solver.boardSize;
                newPos = getRightPos(zeroPos + Solver.boardSize)
            }

            // How Can I calculate the new Manhattan?
            // Now the number moved is in the ZERO POSITION
            // So I can calculate the manhattan like the zero is the moved number
            // and see if it is increased or decreased
            
            int[] oldPos = getRightPos[zeroPos];
            int[] rightPos = getRightPos[moved];
            
            if((oldPos[0] - rightPos[0]) < (newPos[0] - rightPos[0])) {
               son[3] = manhattan + 1;
            } else if((oldPos[1] - rightPos[1]) < (newPos[1] - rightPos[1])) {
               son[3] = manhattan + 1;
            } else {
                son[3] = manhattan - 1;
            }

            return son;
        }

        return null;
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
