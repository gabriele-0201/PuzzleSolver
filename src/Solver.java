import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Solver {

    public static String endBoard;
    public static int boardSize;

    public static void main(String[] args) {

        String fileName = null;

        try {
            fileName = args[0];
        } catch (Exception e) {
            System.out.println("Missin file name!!");
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
        }

        setEndBoard();

        boolean endFound = false;

        HashMap<Board, Node> nodeMap = new HashMap<>();

        //Create the first node with zero moves
        Node node = new Node(getTiles(boardInputString), null, 0); 
        
        endFound = node.checkEnd();;

        PriorityQueue<Node> que = new PriorityQueue<>();;
        que.add(node);;
        nodeMap.put(node.getBoard(), node);
        
        int i = 0;

        while(!endFound) {
            
            node = que.poll();

            LinkedList<Node> sons = node.getSons();

            for(Node s : sons) {

                //System.out.print(i++ + "\r");

                Object tmp;
                if((tmp = nodeMap.get(s.getBoard())) != null) {

                    //System.out.println("CONFLITTO " + ((Node)tmp).getBoard().toString() + " " + s.getBoard().toString());

                    if(((Node)tmp).getMoves() > s.getMoves()) {

                        nodeMap.put(s.getBoard(), s);

                        //test if without removing the element from the queue is more efficent
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

    private static void setEndBoard() {
        int n = boardSize * boardSize;
        StringBuilder s = new StringBuilder();
        for(int i = 1; i < n; i++) {
            s.append(i);
            s.append(" ");
        }
        s.append("0");
        s.append(" ");
        endBoard = s.toString();
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
