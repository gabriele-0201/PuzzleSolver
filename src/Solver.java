import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import java.util.LinkedList;

public class Solver {

    public static Stirng endBoard;

    public static void main(String[] args) {

        String fileName = null;

        try {
            fileName = args[0];
        } catch (Exception e) {
            System.out.println("Missin file name!!");
        }

        String boardInputString = null;
        int boardSize = 0;

        try {
            File file = new File(fileName);
            Scanner reader = new Scanner(file);
            boardSize = reader.nextInt();
            reader.nextLine();
            boardInputString = reader.nextLine();
            reader.close();
        } catch (Exception e) {
            System.out.println("File Not Found!");
        }

        setEndBoard(boardSize);

        int[][] tiles = getTiles(boardSize, boardInputString);

        Board firstBoard = new Board(tiles);

        
    }

    private static int[][] getTiles(int size, String board) {
        String[] chars = board.split(" ");
        int[][] tiles = new int[size][size];
        int i = 0;
        int j = 0;
        for(String c : chars) {
            tiles[i][j++] = Integer.parseInt(c);

            if(j >= size) {
                i++;
                j = 0;
            }
        }
        return tiles;
    }

    private static void setEndBoard(int size) {
        int n = size * size;
        for(int i = 1; i <= size; i++) {
            
        }
    }
}
