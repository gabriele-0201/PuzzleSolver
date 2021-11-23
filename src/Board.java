import java.util.LinkedList;

public class Board {

    private int[][] tiles;
    private int[] zeroPos;

    public Board(int[][] tiles) {
        this.tiles = tiles;
        zeroPos = findZero();
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

    public LinkedList<String> getMoves() {
        LinkedList<String> sons = new LinkedList<String>();
        
        if(!(zeroPos[0] - 1 < 0)) {
           sons.add(getNewBoard(tiles[zeroPos[0] - 1][zeroPos[1]]));
        }

        if(!(zeroPos[0] + 1 >= Solver.boardSize)) {
           sons.add(getNewBoard(tiles[zeroPos[0] + 1][zeroPos[1]]));
        }

        if(!(zeroPos[1] - 1 < 0)) {
           sons.add(getNewBoard(tiles[zeroPos[0]][zeroPos[0] - 1]));
        }

        if(!(zeroPos[1] + 1 >= Solver.boardSize)) {
           sons.add(getNewBoard(tiles[zeroPos[0]][zeroPos[1] + 1]));
        }

        return sons;
    }

    public int getSize() {
        return Solver.boardSize;
    }

    // This function return the new String with the swapped values
    private String getNewBoard(int v) {
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < Solver.boardSize; i++) {
            for(int j = 0; j < Solver.boardSize; j++) {
                if(tiles[i][j] == 0)
                    s.append(v);
                else if(tiles[i][j] == v)
                    s.append(0);
                else
                    s.append(tiles[i][j]);
            }
        }
        return s.toString();

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