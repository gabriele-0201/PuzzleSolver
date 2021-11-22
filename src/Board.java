import java.util.LinkedList;

public class Board {

    private int[][] tiles;
    private int size;
    private int[] zeroPos;
    private String tileString;

    public Board(int[][] tiles) {
        this.tiles = tiles;
        size = tiles.length;
        zeroPos = findZero();
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < tiles.length; i++) {
            for(int j = 0; j < tiles[i].length; j++) {
               s.append(tiles[i][j]);
               s.append(" ");
            }
        }
        return s.toString();
    }

    public int manhattan() {

        int sum = 0;

        for(int i = 0; i < tiles.length; i++) {
            for(int j = 0; j < tiles[i].length; j++) {
               sum += Math.abs((((j + 1) * (i + 1)) - tiles[i][j])); 
            }
        }
        
        return sum;
    }

    public LinkedList<String> getSons() {
        LinkedList<String> sons = new LinkedList();
        
        if(!(zeroPos[0] - 1 < 0)) {
           sons.add(getNewBoard(tiles[zeroPos[0] - 1][zeroPos[1]]));
        }

        if(!(zeroPos[0] + 1 >= size)) {
           sons.add(getNewBoard(tiles[zeroPos[0] + 1][zeroPos[1]]));
        }

        if(!(zeroPos[1] - 1 < 0)) {
           sons.add(getNewBoard(tiles[zeroPos[0]][zeroPos[0] - 1]));
        }

        if(!(zeroPos[1] + 1 >= size)) {
           sons.add(getNewBoard(tiles[zeroPos[0]][zeroPos[1] + 1]));
        }

        return sons;
    }

    // #TODO : with stringBuilder
    // This function return the new String with the swapped values
    private String getNewBoard(int v) {
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < tiles.length; i++) {
            for(int j = 0; j < tiles[i].length; j++) {
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
        for(int i = 0; i < tiles.length; i++) {
            for(int j = 0; j < tiles[i].length; j++) {
                if(tiles[i][j] == 0) {
                    int[] arr = {i, j};
                    return arr;
                }
            }

        }
        return null;
    }

}