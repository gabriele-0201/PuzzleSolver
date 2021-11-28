import java.util.LinkedList;

public class Node implements Comparable<Node>{

    private Node previous;
    private Board board;
    private int moves;
    private int score;

    public Node(short[][] board, Node previous, int moves) {

        this.board = new Board(board);
        this.previous = previous;
        this.moves = moves;
        score = this.board.manhattan() + this.moves;
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
        return Solver.endBoard.equals(this.board.toString());
    }

    @Override
    public int compareTo(Node o) {
        int v1 = this.score;
        int v2 = o.score;
        return v1 - v2;
    }

    @Override
    public boolean equals(Object o) { 
        int v1 = this.score;
        int v2 = ((Node)o).score;
        return v1 == v2;
    }

    public LinkedList<Node> getSons() {
        LinkedList<Node> sons = new LinkedList<>();

        LinkedList<Object[]> vals = board.getMoves();
        LinkedList<short[][]> moves = new LinkedList<>();
        LinkedList<Short> prevMoves = new LinkedList<>();
        for(Object[] o : vals) {
            prevMoves.add((short)o[0]);
            moves.add((short[][])o[1]);
        }

        int sonsMoves = this.moves + 1;

        for(int i = 0; i < moves.size(); i++) {
            Node n = new Node(moves.get(i), this, sonsMoves);
            n.getBoard().setPrevMoved(prevMoves.get(i));
            sons.add(n);
        }

        return sons;
    }
}