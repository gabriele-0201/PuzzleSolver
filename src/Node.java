import java.util.LinkedList;

import javax.swing.event.ListDataEvent;

public class Node implements Comparable<Node>{

    private Node previous;
    private Board board;
    private int moves;
    private int score;

    public Node(String strBoard, Node previous, int moves) {
        this.previous = previous;
        this.board = new Board(getTiles(strBoard));
        this.moves = moves;
        score = this.board.manhattan() + this.moves;
    }

    public Node getPrevious() {
       return previous; 
    }

    public int getMoves() {
        return moves;
    }

    public Board getBoard()  {
        return board;
    }

    public boolean checkEnd() {
        return Solver.endBoard.equals(this.board.toString());
    }

    @Override
    public int compareTo(Node o) {
        int v1 = this.score;
        int v2 = o.score;

        if(v1 < v2) 
            return 1;
        else if(v1 > v2)
            return -1;
        return 0;
    }

    public LinkedList<Node> getSons() {
        LinkedList<Node> sons = new LinkedList<>();
        LinkedList<String> moves = board.getMoves();
        int sonsMoves = this.moves + 1;

        for(String m : moves) {
            sons.add(new Node(m, this, sonsMoves));
        }

        return sons;
    }

    private static int[][] getTiles(String board) {
        String[] chars = board.split(" ");
        int[][] tiles = new int[Solver.boardSize][Solver.boardSize];
        int i = 0;
        int j = 0;
        for(String c : chars) {
            tiles[i][j++] = Integer.parseInt(c);

            if(j >= Solver.boardSize) {
                i++;
                j = 0;
            }
        }
        return tiles;
    }
}