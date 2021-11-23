import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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

        //HAVE TO TEST MANHATTAN


        boolean endFound = false;

        //Create the first node with zero moves
        Node node = new Node(boardInputString, null, 0); 
        System.out.println(node.getBoard().manhattan());
        endFound = node.checkEnd();

        /*
        PriorityQueue<Node> que = new PriorityQueue<>();
        que.add(node);

        while(endFound) {
            node = que.poll();

            LinkedList<Node> sons = node.getSons();

            for(Node s : sons) {
                que.add(s); 
                
                endFound = node.checkEnd();
                if(endFound){
                    node = s;
                    break;
                } 
            }
        }

        System.out.println(node.getMoves());

        LinkedList<String> strBoards = new LinkedList<>();

        while(node.getPrevious() != null) {
            strBoards.add(node.getBoard().toString());
            node = node.getPrevious();
        }

        while(!strBoards.isEmpty()) {
            System.out.println(strBoards.removeLast());
        }*/

        
    }

    private static void setEndBoard() {
        int n = boardSize * boardSize;
        StringBuilder s = new StringBuilder();
        for(int i = 1; i <= n; i++) {
            s.append(i);
            s.append(" ");
        }
        s.append("0");
        endBoard = s.toString();
    }
}
