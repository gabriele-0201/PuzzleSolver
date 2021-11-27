import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.HashSet;
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
        HashMap<Node, Integer> nodeMap = new HashMap<>();

        //Create the first node with zero moves
        Node node = new Node(getTiles(boardInputString), null, 0); 
        
        endFound = node.checkEnd();;
        //endFound = true;;

        PriorityQueue<Node> que = new PriorityQueue<>();;
        que.add(node);;
        nodeMap.put(node, node.getMoves());

        int i = 0;
        while(!endFound) {
            //System.out.println("gen: " + i++);
            //System.out.println("que size: " + que.size());

            //if(i >= 10) break;

            node = que.poll();

            LinkedList<Node> sons = node.getSons();

            //System.out.println("Ded: " + node.getBoard().toString());
            //System.out.println("sons: ");

            for(Node s : sons) {

                //System.out.println(s.getBoard().toString());
                Object tmp;
                if((tmp = nodeMap.get(s)) != null) {

                    if((int)tmp > s.getMoves()) {
                        nodeMap.put(s, s.getMoves());
                    }

                } else {
                    nodeMap.add(s);
                    que.add(s); 
                }
                
                endFound = s.checkEnd();
                if(endFound){
                    node = s;
                    break;
                } 
            }
        }

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