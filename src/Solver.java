import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Solver {

    //public static long[] endBoard;
    public static int boardSize;

    public static void main(String[] args) {

        //Scanner in = new Scanner(System.in);
        //in.nextInt();

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

        //setEndBoard();

        boolean endFound = false;

        //Some problem with this, do not know how but the queue is at finally empty WHYY
        HashMap<Board, Node> nodeMap = new HashMap<>();

        //Create the first node with zero moves
        Node node = new Node(boardInputString, null, 0); 
        
        endFound = node.checkEnd();;

        PriorityQueue<Node> que = new PriorityQueue<>();;
        que.add(node);;
        nodeMap.put(node.getBoard(), node);
        
        int i = 0;

        while(!endFound) {


            //if((i++) == 3)
                //break;
            
            //System.out.println("Size que: " + que.size());
            
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
}
