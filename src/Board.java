import java.util.LinkedList;
import java.util.Arrays;
import java.lang.Math;

public class Board {
    
    public static int B; //number of bit for any number, this is also the offset for each mask
    public static int N; //number of value for each double
    
    //QUANDO necessito delle stirnghe? da subito??
    //private String strTiles;
    private int[] zeroPos;
    private int prevMoved;
    private int manDist;
    private int linConflit;

    private long[] ctiles; //compressed tiles

    //constructor for the first matrix
    public Board(int[][] tiles) {
        
        B = (int)Math.ceil(log2((Solver.boardSize * Solver.boardSize) + 1));
        //System.out.println("Bit per numnero " + B);
        N = (int)Math.floor(64 / B); 
        //System.out.println("Niumero di val possibili per long " + N);
        
        //Maybe not work
        int totalNum = (Solver.boardSize * Solver.boardSize);
        int dim = 1;
        while(totalNum > 0 && totalNum > N) {
            totalNum -= N;
            dim++;
        }

        //System.out.println("Dimensione dell'array " + dim);

        ctiles = new long[dim];
        zeroPos = new int[2];
        compress(tiles); // also check the zeroPos

        manDist = manhattan(tiles);
        linConflit = initialLibearConflicts();
    }

    //What need the constructor?
    private Board(long[] ctiles, int[] zeroPos, int prevMoved, int manDist, int linConflit) {
        this.ctiles = ctiles;
        this.zeroPos = zeroPos;
        this.prevMoved = prevMoved;            
        this.manDist = manDist;
        this.linConflit = linConflit; 
    }

    public int getPrevMoved() {
        return prevMoved;
    }
    
    public int getManDist() {
        return manDist;
    }

    public int getLinConflit() {
        return linConflit;
    }

    public long[] getCTiles() {
        return ctiles;
    }

    private int getVal(int r, int c, long[] newCTiles) {

        long[] board = newCTiles != null ? newCTiles : ctiles;

        int pos = getPos(r, c);
        if(pos == -1)
            return -1;

        int index = pos / N;
        pos = pos % N; // this return the new position in the right array

        long mask = getMask(pos);

        //return (int)((long)Math.floor((mask & ctiles[index])) >> (pos * B));
        return (int)((mask & board[index]) >> (pos * B));
    }
    
    private void setVal(int r, int c, long val, long[] newCTiles) {

        long[] board = newCTiles != null ? newCTiles : ctiles;

        int pos = getPos(r, c);
        if(pos == -1)
            return;

        int index = pos / N;
        pos = pos % N; // this return the new position in the right array
        long mask = getMask(pos);

        board[index] = (~(mask & board[index])) & board[index] ; //remove the value in the specified pos

        val = val << (pos * B);

        board[index] = (board[index]) | (val); //insert the val in the right place
    }

    private long getMask(int pos) {
        long mask1Bit = (long)Math.pow(2, B) - 1;
        return mask1Bit << (pos * B);
    }

    private int getPos(int r, int c) {
        
        //check out of bounds
        if(r < 0 || r >= Solver.boardSize || c < 0 || c >= Solver.boardSize)
            return -1;

        int pos = r * Solver.boardSize;
        pos += c;
        return pos;
    }

    private void compress(int[][] tiles) {
        //StringBuilder str = new StringBuilder();
        for (int i = 0; i < Solver.boardSize; i++) {
            for (int j = 0; j < Solver.boardSize; j++) {
                setVal(i, j, tiles[i][j], null);
                //str.append(tiles[i][j]);
                if(tiles[i][j] == 0) {
                    zeroPos[0] = i;
                    zeroPos[1] = j;
                }
            }
        }
    }

    private double log2(int N){
        return (Math.log(N) / Math.log(2));
    }

    public int manhattan(int[][] tiles) {
        int sum = 0;
        for(int i = 0; i < Solver.boardSize; i++) {
            for(int j = 0; j < Solver.boardSize; j++) {

                int num;
                if(tiles[i][j] == 0) // count nothing if is zero
                    continue;
                else
                    num = tiles[i][j];

                int[] rightPos = getRightPos(num);
                sum += Math.abs(rightPos[0] - i) + Math.abs(rightPos[1] - j); 
            }
        }
        return sum;
    }
    
    private int[] getRightPos(int v) {

        //if(v == 0) return new int[] {Solver.boardSize - 1, Solver.boardSize - 1};
        if(v == 0) return new int[] {-1,-1};

        int[] rightPos = new int[2];
        rightPos[0]= (v - 1) / Solver.boardSize;
        rightPos[1] = (v - (rightPos[0] * Solver.boardSize)) - 1;
        return rightPos;
    }

    //dir: 1 - up, 2 - right, 3 - down, 4 - left
    private Object[] makeMove(int dir) {
        Object[] son = null;

        int moved = -1;
        int[] newZeroPos = new int[2];

        switch(dir) {
            case 1: //up
                moved = getVal(zeroPos[0] - 1, zeroPos[1], null);
                break;
            case 3: //down
                moved = getVal(zeroPos[0] + 1, zeroPos[1], null);
                break;
            case 2: //right
                moved = getVal(zeroPos[0], zeroPos[1] + 1, null);
                break;
            case 4: //left
                moved = getVal(zeroPos[0], zeroPos[1] - 1, null);
                break;
        }

        if(moved == -1 || moved == prevMoved)
            return null;
            
        //copy the array
        long[] newctiles = new long[ctiles.length];
        System.arraycopy(ctiles, 0, newctiles, 0, ctiles.length);

        // work with all sons 
        // 1 place : ctiles of son
        // 2 place : new zero position of the son
        // 3 place : int of the previuos moved number
        // 4 place : new manhattan
        // 5 place : new linConflit

        //update the conglits
       switch (dir) {
           case 1:
               newZeroPos[0] = zeroPos[0] - 1;
               newZeroPos[1] = zeroPos[1];
               break;
           case 2:
               newZeroPos[0] = zeroPos[0];
               newZeroPos[1] = zeroPos[1] + 1;
               break;
           case 3:
               newZeroPos[0] = zeroPos[0] + 1;
               newZeroPos[1] = zeroPos[1];
               break;
           case 4:
               newZeroPos[0] = zeroPos[0];
               newZeroPos[1] = zeroPos[1] - 1;
               break;
       }

       setVal(newZeroPos[0], newZeroPos[1], 0, newctiles);
       setVal(zeroPos[0], zeroPos[1], moved, newctiles);

       son = new Object[5];
       son[0] = newctiles;
       son[1] = newZeroPos;
       son[2] = moved;

       //update new manhattan with the conflits
        int newConflits = 0;
        switch (dir) {
           case 1:
           case 3:
                
               //System.out.println("Padre: " + getString());
               //System.out.println("Figlio: " + getString(newctiles));
               //System.out.println("Conflitti da rimuovere: ");
               newConflits -= linConflitRowVal(newZeroPos[0], newZeroPos[1], moved, ctiles);
               //System.out.println("Conflitti da aggiungere: ");
               newConflits += linConflitRowVal(zeroPos[0], zeroPos[1], moved, newctiles);

               break;
           case 2:
           case 4:

               //System.out.println("Padre: " + getString());
               //System.out.println("Figlio: " + getString(newctiles));
               //System.out.println("Conflitti da rimuovere: ");
               newConflits -= linConflitColumnVal(newZeroPos[0], newZeroPos[1], moved, ctiles);
               //System.out.println("Conflitti da aggiungere: ");
               newConflits += linConflitColumnVal(zeroPos[0], zeroPos[1], moved, newctiles);

               break;
        }

        //System.out.println("Padre: " + getString());
        //System.out.println("Figlio: " + getString(newctiles));
        //System.out.println(linConflit + " " +  newConflits);

        //if(linConflit + newConflits < 0) 
        //   System.out.println("FUCK " + linConflit + " " +  newConflits);


       int[] rightPos = getRightPos(moved);
       
       if(Math.abs(newZeroPos[0] - rightPos[0]) < Math.abs(zeroPos[0] - rightPos[0])) {
            son[3] = manDist + 1;
       } else if(Math.abs(newZeroPos[1] - rightPos[1]) < Math.abs(zeroPos[1] - rightPos[1])) {
            son[3] = manDist + 1;
       } else {
            son[3] = manDist - 1;
       }

        son[4] = linConflit + newConflits;

        //System.out.println("precedente  Manhattan: " + manDist);
        //System.out.println("Nuovo Manhattan: " + (int)son[3]);
        //System.out.println("precedente  Conflitti: " + linConflit);
        //System.out.println("Nuovo Confliti: " + (int)son[4]);
        
        /*
        if((int)son[4] < 0) {
            System.out.println("Padre: " + getString());
            System.out.println("Figlio: " + getString(newctiles));
            System.out.println("cambio conflitti: " + newConflits);
            System.out.println("precedente  Manhattan: " + manDist);
            System.out.println("Nuovo Manhattan: " + (int)son[3]);
            System.out.println("precedente  Conflitti: " + linConflit);
            System.out.println("Nuovo Confliti: " + (int)son[4]);
        }
        */
        

        return son;
    }

    private int initialLibearConflicts() {
        int counterConflits = 0;

        //System.out.println("Conflitti INIZIALI: " );

        for(int i = 0; i < Solver.boardSize; i++) {
            counterConflits += linConflitColumn(i, -1, null);
            counterConflits += linConflitRow(i, -1, null);
        }

        return counterConflits;
    }

    private int linConflitColumn(int column, int valToCheck, long[] newCTiles) {

        long[] board = newCTiles != null ? newCTiles : ctiles;

        int counter = 0;
        for(int j = 0; j < Solver.boardSize - 1; j ++) {

            //for each value have to check all the value on the right and down
            int checkNumb = getVal(j, column, board);
            int[] currentValRightPos = getRightPos(checkNumb);
            if(currentValRightPos[1] != column /*|| (currentValRightPos[0] == j && currentValRightPos[1] == column)*/)
                continue;

            //System.out.println("current val" + getVal(j, column, board));
            for(int c = 1; c < Solver.boardSize - j; c++) {
                //System.out.println("COLUMN checking val" + getVal(j + c, column, board));
                int toCheckNumbWith = getVal(j + c, column, board);
                int[] otherValRightPos = getRightPos(toCheckNumbWith);

                if(otherValRightPos[1] != column ||
                    (valToCheck != -1 &&
                     !(checkNumb ==  valToCheck || toCheckNumbWith ==  valToCheck)) /*||
                     (otherValRightPos[0] == (j + c) && otherValRightPos[1] == column)*/)
                    continue;

                if(otherValRightPos[0] <= currentValRightPos[0] /*&& currentValRightPos[0] >= otherValRightPos[0]*/){
                    counter+=2;
                    //System.out.println("Board: " + getString(board));
                    //System.out.println("column Conflits: " + checkNumb + "  -  " + toCheckNumbWith);
                }

            }
        }
        return counter;
    }

    private int linConflitColumnVal(int row, int column, int valToCheck, long[] newCTiles) {
    
        //if(getString(newCTiles).equals("2 8 0 4 5 6 1 7 3"))
            //System.out.println("cazzo");

        int counter = 0;
        int[] realPos = getRightPos(valToCheck);

        if(realPos[1] != column /*|| (realPos[0] == row && realPos[1] == column)*/)
            return 0;

        for(int j = 0; j < Solver.boardSize; j ++) {

            if(j == row)
                continue;

            //for each value have to check all the value on the right and down
            int checkNumb = getVal(j, column, newCTiles);
            int[] currentValRightPos = getRightPos(checkNumb);
            if(currentValRightPos[1] != column /*|| (currentValRightPos[0] == j && currentValRightPos[1] == column)*/)
                continue;
                
            if (j < row && currentValRightPos[0] >= realPos[0] ) {
                counter+=2;
            }
            else if (j > row && currentValRightPos[0] <= realPos[0] /*&& currentValRightPos[0] >= otherValRightPos[0]*/) {
                counter+=2;
            }
        }
        return counter;
    }
    
    private int linConflitRow(int row, int valToCheck, long[] newCTiles) {

        long[] board = newCTiles != null ? newCTiles : ctiles;

        int counter = 0;
        for(int j = 0; j < Solver.boardSize - 1; j ++) {

            //for each value have to check all the value on the right and down
            int checkNumb = getVal(row, j, board);
            int[] currentValRightPos = getRightPos(checkNumb);

            if(currentValRightPos[0] != row /*|| (currentValRightPos[0] == row && currentValRightPos[1] == j)*/)
                continue;
            
            //System.out.println("current val" + getVal(i, j, baord));
            for(int c = 1; c < Solver.boardSize - j; c++) {
                //System.out.println("LINE checking val" + getVal(row, j + c, board));
                int toCheckNumbWith = getVal(row, j + c, board);
                int[] otherValRightPos = getRightPos(toCheckNumbWith);

                if(otherValRightPos[0] != row ||
                    (valToCheck != -1 && 
                     !(checkNumb ==  valToCheck || toCheckNumbWith ==  valToCheck)) /*||
                     (otherValRightPos[0] == row && otherValRightPos[1] == (j + c))*/)
                    continue;

                if(otherValRightPos[1] <= currentValRightPos[1] /*&& currentValRightPos[1] >= otherValRightPos[1]*/){
                    counter+=2;
                    //System.out.println("Board: " + getString(board));
                    //System.out.println("row Conflits: " + checkNumb + "  -  " + toCheckNumbWith);
                }

            }
        }
        return counter;
    }

    private int linConflitRowVal(int row, int column, int valToCheck, long[] newCTiles) {

        int counter = 0;
        int[] realPos = getRightPos(valToCheck);

        if(realPos[0] != row /*|| (realPos[0] == row && realPos[1] == column)*/)
            return 0;

        for(int j = 0; j < Solver.boardSize; j ++) {

            if(j == column)
                continue;

            //for each value have to check all the value on the right and down
            int checkNumb = getVal(row, j, newCTiles);
            int[] currentValRightPos = getRightPos(checkNumb);
            if(currentValRightPos[0] != row /*|| (currentValRightPos[0] == row && currentValRightPos[1] == j)*/)
                continue;

            if (j < column && currentValRightPos[1] >= realPos[1] /*&& currentValRightPos[0] >= otherValRightPos[0]*/) {
                counter+=2;
                //System.out.println("row Conflits: " + valToCheck + "  -  " + checkNumb);
            }
            else if (j > column && currentValRightPos[1] <= realPos[1] /*&& currentValRightPos[0] >= otherValRightPos[0]*/) {
                counter+=2;
                //System.out.println("row Conflits: " + valToCheck + "  -  " + checkNumb);
            }
        }
        return counter;
    }

    public LinkedList<Board> getMoves() {

        LinkedList<Board> sons = new LinkedList<>();
        Object[] son;

        //System.out.println("Starting board : \n" + strTiles);

        for(int i = 1; i <= 4; i++) {
            Object[] newSon = makeMove(i);
            if(newSon != null) {
                //System.out.println("figlio dir: " +  ((String)newSon[0]));
                sons.push( new Board((long[])newSon[0], (int[])newSon[1], (int)newSon[2], (int)newSon[3], (int)newSon[4]));
            }
            //else 
                //System.out.println("impossible movement dir: " + i);
        }

        return sons; 
    }


    private String getString() {
        StringBuilder bTiles = new StringBuilder();
        for(int i = 0; i < Solver.boardSize; i++) {
            for(int j = 0; j < Solver.boardSize; j++) {
                bTiles.append(getVal(i, j, null) + " ");
            }
        }
        bTiles.deleteCharAt(bTiles.length() - 1);
        return bTiles.toString();
    }

    private String getString(long[] c) {
        StringBuilder bTiles = new StringBuilder();
        for(int i = 0; i < Solver.boardSize; i++) {
            for(int j = 0; j < Solver.boardSize; j++) {
                bTiles.append(getVal(i, j, c) + " ");
            }
        }
        bTiles.deleteCharAt(bTiles.length() - 1);
        return bTiles.toString();
    }

    @Override
    public String toString(){
        return getString();
    }

    @Override
    public boolean equals(Object o) {
        //return strTiles.equals(((Board)o).toString());
        //return (bTiles.toString()).equals(((Board)o).toString());
        return Arrays.equals(ctiles, ((Board)o).getCTiles());
    }

    @Override
    public int hashCode() {
        return ctiles.hashCode();
        //return (bTiles.toString()).hashCode();
    }

}
