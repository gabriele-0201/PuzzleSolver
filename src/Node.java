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

    public Node(StringBuilder strBoard, Node previous, int moves,  ) {
        this.board = new Board(strBoard,  );
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

        //This is all I receive from the board
        LinkedList<Object[]> vals = board.getMoves();

        //here I will store all the stuf separatede, MAYBE USEFEULL
        LinkedList<int[][]> moves = new LinkedList<>();
        LinkedList<Integer> prevMoves = new LinkedList<>();


        // work with all sons 
        // 1 place : str of son
        // 2 palce : int of the previuos moved number
        // 3 place : new zero position of the son
        // 4 palce : new manhattan
        
        for(Object[] o : vals) {
        
        }

        int sonsMoves = this.moves + 1;

        for(int i = 0; i < moves.size(); i++) {
            Node n = new Node(moves.get(i), this, sonsMoves);
            n.getBoard().setPrevMoved(prevMoves.get(i));
            sons.add(n);
        }

        return sons;
    }

    private static short[][] getTiles(String board) {
        String[] chars = board.split(" ");
        short[][] tiles = new short[Solver.boardSize][Solver.boardSize];
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
