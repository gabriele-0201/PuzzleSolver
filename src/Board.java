import java.util.LinkedList;

import javax.naming.ldap.LdapName;

public class Board {

    private short[][] tiles;
    private short[] zeroPos;
    private short prevMoved;

    public Board(short[][] tiles) {

        this.tiles = tiles;
        zeroPos = findZero();

        /*
        for(int i = 0; i < tiles.length; i++){
            for(int j = 0; j < tiles[i].length; j++)
                System.out.print(tiles[i][j] + " ");
            System.out.println("");
        }
        */
    }

    public void setPrevMoved(short m) {
        prevMoved = m;
    }

    public short getPrevMoved() {
        return prevMoved;
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
               sum += Math.abs((((j + 1) * (i + 1)) - tiles[i][j])); 
            }
        }
        
        return sum;
    }

    /*
    public LinkedList<StringBuilder> getMoves() {
        LinkedList<StringBuilder> sons = new LinkedList<>();
        
        if(!(zeroPos[0] - 1 < 0) && tiles[zeroPos[0] - 1][zeroPos[1]] != prevMoved) {
           sons.add(getNewBoard(tiles[zeroPos[0] - 1][zeroPos[1]]));
        }

        if(!(zeroPos[0] + 1 >= Solver.boardSize) && tiles[zeroPos[0] + 1][zeroPos[1]] != prevMoved) {
           sons.add(getNewBoard(tiles[zeroPos[0] + 1][zeroPos[1]]));
        }

        if(!(zeroPos[1] - 1 < 0) && tiles[zeroPos[0]][zeroPos[1] - 1] != prevMoved) {
           sons.add(getNewBoard(tiles[zeroPos[0]][zeroPos[1] - 1]));
        }

        if(!(zeroPos[1] + 1 >= Solver.boardSize) && tiles[zeroPos[0]][zeroPos[1] + 1] != prevMoved) {
           sons.add(getNewBoard(tiles[zeroPos[0]][zeroPos[1] + 1]));
        }

        return sons;
    }
    */

    // Devo creare i figlio e cazzo per crearli devo stare attento a non creare un figlio che muove la stessa pedina di quello precedente
    // In questo metodo devo tornare i figli, quindi le matrici con i figli ma come cazzo faccio e tornare ANCHE la cella che ho spostato?
    // potrei passare una linked list di array di oggetti

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

    public int getSize() {
        return Solver.boardSize;
    }

    // This function return the new String with the swapped values
    private StringBuilder getNewBoard(int v) {
        StringBuilder s = new StringBuilder();
        s.append(v);
        for(int i = 0; i < Solver.boardSize; i++) {
            for(int j = 0; j < Solver.boardSize; j++) {
                if(tiles[i][j] == 0)
                    s.append(v);
                else if(tiles[i][j] == v)
                    s.append(0);
                else
                    s.append(tiles[i][j]);
                s.append(" ");
            }
        }
        return s;
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

}