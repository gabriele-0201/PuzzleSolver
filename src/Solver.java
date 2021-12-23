import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Solver {

    public static void main(String[] args) {

        String fileName = null;

        try {
            fileName = args[0];
        } catch (Exception e) {
            System.out.println("Missin file name!!");
            return;
        }

        String boardInputString = null;

        try {
            File file = new File(fileName);
            Scanner reader = new Scanner(file);
            boardSize = reader.nextInt();
            reader.nextLine();
            boardInputString = reader.nextLine();
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found!");
            return;
        }

        Solver solve = new Solver();

        solve.solveBoard(boardInputString);

    }

    public static int boardSize;

    Solver() {}

    void solveBoard(String strBoard) {

        boolean endFound = false;

        HashMap<Board, Node> nodeMap = new HashMap<>();
        //HashMap<long[], Board> nodeMap = new HashMap<>();

        //Create the first node with zero moves
        Node node = new Node(strBoard, null, 0); 
        
        endFound = node.checkEnd();;

        PriorityQueue<Node> que = new PriorityQueue<>();;
        que.add(node);
        nodeMap.put(node.getBoard(), node);
        
        int i = 0;

        while(!endFound) {
            
            node = que.poll();

            LinkedList<Node> sons = node.getSons();

            for(Node s : sons) {

                Object tmp;
                if((tmp = nodeMap.get(s.getBoard())) != null) {

                    if(((Node)tmp).getMoves() > s.getMoves()) {

                        nodeMap.put(s.getBoard(), s);

                        //que.remove((Node)tmp);
                        que.add(s);

                    }

                } else {
                    nodeMap.put(s.getBoard(), s);
                    que.add(s); 
                }
                
                endFound = s.checkEnd();
                if(endFound){
                    node = s;
                    break;
                } 
            }
        }
        
        System.out.println();

        System.out.println(node.getMoves());

        LinkedList<String> strBoards = new LinkedList<>();

        while(node != null) {
            strBoards.add(node.getBoard().toString());
            node = node.getPrevious();
        }

        while(!strBoards.isEmpty()) {
            System.out.println(strBoards.removeLast());
        }
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

    class Node implements Comparable<Node>{
    
        private Node previous;
        private Board board;
        private int moves;
        private int score;
    
        public Node(String strBoard, Node previous, int moves) {
            this.board = new Board(getTiles(strBoard));
            this.previous = previous;
            this.moves = moves;
            this.score = this.board.getManDist() + this.board.getLinConflit() + this.moves;
        }
        
        //this constructuor is only called from inside so it could be private
        private Node(Board board, Node previous, int moves) {
            this.board = board;
            this.previous = previous;
            this.moves = moves;
            this.score = this.board.getManDist() + this.board.getLinConflit() + this.moves;
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
    }

}
