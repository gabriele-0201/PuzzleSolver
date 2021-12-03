import java.util.LinkedList;

public class Node implements Comparable<Node>{

    private Node previous;
    private Board board;
    private int moves;
    private int score;

    public Node(String strBoard, Node previous, int moves) {
        this.board = new Board(getTiles(strBoard));
        this.previous = previous;
        this.moves = moves;
        score = this.board.manhattan() + this.moves;
    }
    
    //this constructuor is only called from inside so it could be private
    private Node(Board board, Node previous, int moves) {
        this.board = board;
        this.previous = previous;
        this.moves = moves;
        //have to change this
        //score = this.board.manhattan() + this.moves;
        score = this.board.getManDist() + this.moves;
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
        return Solver.endBoard.equals(this.board.getStrBuilder());
    }

    @Override
    public int compareTo(Node o) {
        return this.score - o.score;
    }

    @Override
    public boolean equals(Object o) { 
        return this.score == ((Node)o).score;
    }

    //Maybe I could do that the board create already the board object son
    //So the return of the get sons is already a list af board with all already done
    
    public LinkedList<Node> getSons() {
        LinkedList<Node> sons = new LinkedList<>();
        LinkedList<Board> moves = board.getMoves();

        int sonsMoves = this.moves + 1;

        for(int i = 0; i < moves.size(); i++) {
            sons.add(new Node(moves.get(i), this, sonsMoves));
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
