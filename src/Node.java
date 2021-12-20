import java.util.LinkedList;
import java.util.Arrays;

public class Node implements Comparable<Node>{

    private Node previous;
    private Board board;
    private int moves;
    private int score;

    public Node(String strBoard, Node previous, int moves) {
        this.board = new Board(getTiles(strBoard));
        this.previous = previous;
        this.moves = moves;
        this.score = this.board.getManDist() + this.board.getLinConflit() + this.moves;
        //this.score = this.board.getManDist() + this.moves;
    }
    
    //this constructuor is only called from inside so it could be private
    private Node(Board board, Node previous, int moves) {
        this.board = board;
        this.previous = previous;
        this.moves = moves;
        this.score = this.board.getManDist() + this.board.getLinConflit() + this.moves;
        //this.score = this.board.getManDist() + this.moves;
    }

    public Node getPrevious() {
       return previous; 
    }

    public int getMoves() {
        return moves;
    }

    public void setMoves(int m) {
        this.moves = m;
    }

    public Board getBoard()  {
        return board;
    }

    public boolean checkEnd() {
        return this.board.getManDist() == 0;
        //return Arrays.equals(Board.endBoard, this.board.getCTiles());
    }

    @Override
    public int compareTo(Node o) {
        return this.score - o.score;
    }

    @Override
    public boolean equals(Object o) { 
        return this.score == ((Node)o).score;
    }

    public LinkedList<Node> getSons() {
        LinkedList<Node> sons = new LinkedList<>();
        //get a list with all the son of the current board
        LinkedList<Board> moves = board.getMoves();

        //adding one move for the all the son
        int sonsMoves = this.moves + 1;

        //System.out.println("Dead: " + board + " manhattan: " + board.getManDist());

        for(int i = 0; i < moves.size(); i++) {
            sons.add(new Node(moves.get(i), this, sonsMoves));
            //System.out.println("Son: " + moves.get(i) + " manhattan: " + (moves.get(i).getManDist() + moves.get(i).getLinConflit() + sonsMoves));
        }

        return sons;
    }

    private static int[][] getTiles(String board) {
        String[] chars = board.split(" ");
        int[][] tiles = new int[Solver.boardSize][Solver.boardSize];
        int i = 0;
        int j = 0;
        for(String c : chars) {
            tiles[i][j++] = Short.parseShort(c);

            if(j >= Solver.boardSize) {
                i++;
                j = 0;
            }
        }
        return tiles;
    }
}
