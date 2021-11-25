import java.util.LinkedList;

public class Board {

    private int[][] tiles;
    private int[] zeroPos;
    private int prevMoved;

    public Board(int[][] tiles) {
        this.tiles = tiles;
        zeroPos = findZero();
    }

    public void setPrevMoved(int m) {
        prevMoved = m;
    }

    public int getPrevMoved() {
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

    private int[] findZero() {
        for(int i = 0; i < Solver.boardSize; i++) {
            for(int j = 0; j < Solver.boardSize; j++) {
                if(tiles[i][j] == 0) {
                    int[] arr = {i, j};
                    return arr;
                }
            }

        }
        return null;
    }

}