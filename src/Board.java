import java.util.LinkedList;
import java.lang.Math;

public class Board {
    
    //private int[][] tiles;
    //private StringBuilder bTiles;
    private String strTiles;
    private int zeroPos;
    //private int zeroIndex;
    private int prevMoved;
    private int manDist;
    private int linConflit;

    private double[] ctiles; //compressed tiles

    public static int N; //number of bit for any number, this is also the offset for each mask
    public static int M; //number of value for each double



    //constructor for the first matrix
    public Board(int[][] tiles) {
        //this.tiles = tiles;
        
        N = Math.floor(log2(Solver.boardSize));
        M = Math.floor(64 / N); 
        
        ctiles = compress(tiles); //also create the string
        //strTiles = toStrFromMatrix(tiles);
        findZero();
        manDist = manhattan();
        linConflit = initialLibearConflicts();
    }

    private Board(String s, int zPos, int zIndex, int moved, int manhattan) {
        tiles = null;
        strTiles = s;
        zeroPos = zPos;
        zeroIndex = zIndex;
        prevMoved = moved;            
        manDist = manhattan;
        linConflit = initialLibearConflicts();
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

    //MAYBE I can work only with position, the change value and oll other things
    private int getVal(int r, int c) {


        return 0; 
    }

    private void setVal(int r, int c, int val) {


        return 0; 
    }

    private int getPos(int r, int c) {

        return 0;
    }

    private void compress(int[][] tiles) {
        StringBuilder str = new StringBuilder();
        int counterPosition = 1;
        for (int i = 0; i < Solver.boardSize; i++) {
            for (int j = 0; j < Solver.boardSize; j++) {
                setVal(counterPosition, tiles[i][j]);
                str.append();
            }
        }
    }

    private int log2(int N){
        int result = (int)(Math.log(N) / Math.log(2));
        return result;
    }

    public int manhattan() {
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
        //System.out.println("Direction: " + dir);
        Object[] son = null;
         
        StringBuilder strSon = new StringBuilder(strTiles);

        int counter = 0; 
        int index = zeroIndex;
        int newZeroIndex = -1;
        //index used for the replace, other side of the index
        int otherIndex = -1;
        int moved = -1;
        boolean done = false;
        
        //System.out.println("Zero index: " + index);
        //System.out.println("Zero Position: " + zeroPos);
        //System.out.println("right index" + getRightIndex());

        switch(dir) {
            case 1: //up
                counter = -1;
                index--; 
               
                // go until find the right value to replace
                while(index >= 0 && !done) {
                    if(strTiles.charAt(index) == ' ' ) {

                        counter++; 

                        if (counter == Solver.boardSize - 1){
                            otherIndex = index - 1;
                        }
                        else if (counter >= Solver.boardSize) {
                            done = true;
                            index += 2;
                        }

                    }
                    index--; 
                }
                
                if(!done && counter == Solver.boardSize - 1) {
                    done = true;
                    index++;
                } else if(!done) 
                    return null;

                //System.out.println(index + " " + otherIndex);

                break;

            case 3: //down

                counter = -1;
                index++; 
               
                // go until find the right value to replace
                while(index < strTiles.length() && !done) {
                    if(strTiles.charAt(index) == ' ') {

                        counter++; 

                        if (counter == Solver.boardSize - 1)
                            otherIndex = index + 1;
                        else if (counter >= Solver.boardSize) {
                            done = true;
                            index -= 2;
                        }

                    }
                    index++; 
                }
                
                //check if the while is finished BEFORE finding a new space
                //so the done variable is not set to true
                if(!done && counter == Solver.boardSize - 1) {
                    done = true;
                    index--;
                } else if(!done) 
                    return null;

                //System.out.println(index + " " + otherIndex);

                break;

            case 2: //right
                index+=2;

                //if(index > bTiles.length())
                if(index > strTiles.length())
                    return null;

                //Add the check to the line
                // The right number have to be in the same position of the previous 
                if((zeroPos - 1) / Solver.boardSize != (zeroPos) / Solver.boardSize )
                    return null;
                
                //System.out.println("QUI SuPerA");

                otherIndex = index; //save the start of the number

                while(index < strTiles.length()  && strTiles.charAt(index) != ' ')
                    index++;
                index--;

                done = true;

                //System.out.println(index + " " + otherIndex);

                break;

            case 4: //left

                index-=2;

                if(index < 0)
                    return null;
                
                //Add the check to the line
                // The right number have to be in the same position of the previous 
                if((zeroPos - 1) / Solver.boardSize != (zeroPos - 2) / Solver.boardSize )
                    return null;

                otherIndex = index;
                while(index >= 0 && strTiles.charAt(index) != ' ')
                    index--;
                index++;

                //System.out.println(index + " " + otherIndex);

                done = true;

                break;
        }

        if(done) {

            String strMoved;

            switch (dir) {
                case 1:
                case 4:

                    strMoved = strTiles.substring(index, otherIndex + 1);
                    moved = Integer.parseInt(strMoved);

                    //System.out.println("Number to move: " + moved);

                    if (prevMoved == moved)
                        return null;

                    strSon.replace(zeroIndex, zeroIndex + 1, strMoved);
                    strSon.replace(index, otherIndex + 1, "0");

                    newZeroIndex = index;

                    break;
                case 3:
                case 2:

                    strMoved = strTiles.substring(otherIndex, index + 1);
                    moved = Integer.parseInt(strMoved);

                    //System.out.println("Number to move: " + moved);
                    
                    if (prevMoved == moved)
                        return null;

                    strSon.replace(otherIndex, index + 1, "0");
                    strSon.replace(zeroIndex, zeroIndex + 1, strMoved);

                    newZeroIndex = otherIndex + strMoved.length() - 1;

                    break;
            }

            son = new Object[5];
            son[0] = strSon.toString();
            son[1] = moved;
            
            int[] oldPos = null;
            
            switch (dir) {
                case 1:
                    son[2] = zeroPos - Solver.boardSize;
                    oldPos = getRightPos(zeroPos - Solver.boardSize);
                    break; 
                case 2:
                    son[2] = zeroPos + 1;
                    oldPos = getRightPos(zeroPos + 1);
                    break; 
                case 3:
                    son[2] = zeroPos + Solver.boardSize;
                    oldPos = getRightPos(zeroPos + Solver.boardSize);
                    break; 
                case 4:
                    son[2] = zeroPos - 1;
                    oldPos = getRightPos(zeroPos - 1);
                    break; 
            }

            son[3] = newZeroIndex;

            // How Can I calculate the new Manhattan?
            // Now the number moved is in the ZERO POSITION
            // So I can calculate the manhattan like the zero is the moved number
            // and see if it is increased or decreased
            
            int[] newPos = getRightPos(zeroPos);
            int[] rightPos = getRightPos(moved);
            int newManDist;
            
            if(Math.abs(oldPos[0] - rightPos[0]) < Math.abs(newPos[0] - rightPos[0])) {
                newManDist = manDist + 1;
            } else if(Math.abs(oldPos[1] - rightPos[1]) < Math.abs(newPos[1] - rightPos[1])) {
                newManDist = manDist + 1;
            } else {
                newManDist = manDist - 1;
            }

            //the zeroPos should be the position of the the just moved number
            //I have to pass also the new string board
            //manDist += initialLibearConflicts((String)son[0], (int)son[2]);
            son[4] = manDist;
            //here change with the more efficent version

            return son;
        }

        return null;
    }

    private int initialLibearConflicts() {
        int counterConflits = 0;
        int currentPos = 1;
        int startNumb, endNumb;
        int index = 0;
        int indexConflits = 0;

        System.out.println("Board: " + strTiles);
        //the values has to be in the column they have to be


        //first scann all the number in the board
        while(currentPos <= (Solver.boardSize * Solver.boardSize)){
            //for each element of the board I have to scan the line and the column
            //and seacrh for the linear conflicts;

            startNumb = index; //save the start of the number

            //iterate while find a space and find the integer

            while(index < strTiles.length()  && strTiles.charAt(index) != ' ')
                index++;
            //set the index of the end number, one more for the subsstring
            endNumb = index;
            //skip the space
            index++;

            currentPos++;

            //minus one because we point already to the next
            //is not a conflit with zero
            if(currentPos - 1 == zeroPos) {
                continue;
            }
            
            int chekingNumb = Integer.parseInt(strTiles.substring(startNumb, endNumb));
            int[] checking = getRightPos(chekingNumb);
            int[] currentPositionTiles = getRightPos(currentPos - 1);

            boolean possibleLinearC = true;
            boolean possibleColumnC = true;

            //if the number we are checking is not in his line could not create a linear conflict

            /* 
            System.out.println("Numero che devo controllare "  + chekingNumb);
            System.out.println("posizione del Numero "  + (int)(currentPos - 1));
            System.out.println("Posizione in cui dovrebbe essere - riga: " + checking[0] + " colonna: " + checking[1]);
            System.out.println("Posizione in cui si trova - riga: " + currentPositionTiles[0] + " colonna: " + currentPositionTiles[1]);
            */

            //if already in the right place
            if(checking[0] == currentPositionTiles[0] && checking[1] == currentPositionTiles[1]) {
                continue;
            }

            if(checking[0] != currentPositionTiles[0] || checking[1] < currentPositionTiles[1])
                possibleLinearC = false;
            //if(checking[1] < currentPositionTiles[1])
                //possibleLinearC = false;
            if(checking[1] != currentPositionTiles[1] || checking[0] < currentPositionTiles[0])
                possibleColumnC = false;
            //if(checking[0] < currentPositionTiles[0])
                //possibleColumnC = false;

            // check conflits on the RIGHT
            
            boolean endLine = false; 
            indexConflits = index;
            int conflitPos = currentPos - 1;

            if(indexConflits > strTiles.length())
                endLine = true;

            while(possibleLinearC && !endLine) {

                startNumb = indexConflits; 
                while(indexConflits < strTiles.length()  && strTiles.charAt(indexConflits) != ' ')
                   indexConflits++;
                endNumb = indexConflits;
                indexConflits ++;

                conflitPos++;
                
                int toChekNumb = Integer.parseInt(strTiles.substring(startNumb, endNumb));

                /* 
                System.out.println("RIGHT");
                System.out.println("checking with " + chekingNumb);
                System.out.println("to check number " + toChekNumb);
                */ 

                int[] toCheck = getRightPos(toChekNumb);
                int[] toCheckPosTiles = getRightPos(conflitPos);

                /*
                System.out.println("Posizione in cui dovrebbe essere - riga: " + checking[0] + " colonna: " + checking[1]);
                System.out.println("Posizione in cui si trova - riga: " + currentPositionTiles[0] + " colonna: " + currentPositionTiles[1]);
                System.out.println("Posizione in cui il nuovo dovrebbe essere - riga: " + toCheck[0] + " colonna: " + toCheck[1]);
                System.out.println("Posizione in cui il nuovo si trova - riga: " + toCheckPosTiles[0] + " colonna: " + toCheckPosTiles[1]);
                */

                ///    ||  (currentPositionTiles[0] != toCheckPosTiles[0] ||  checking[0] != toCheck[0]))
                //else if (currentPositionTiles[1] == toCheck[1] && toCheckPosTiles[1] == checking[1]) {
                if((currentPositionTiles[0] == toCheck[0] && currentPositionTiles[1] == toCheck[1]) && (checking[0] == toCheckPosTiles[0] && checking[1] == toCheckPosTiles[1])) {
                    counterConflits += 2;
                    //System.out.println("Board: " + tilesToCheck);
                    //System.out.println("CONFLIT");
                    //System.out.println("RIGHT");
                    //System.out.println(strTiles);
                    System.out.println("Conflits between:: " + chekingNumb + " " + toChekNumb);
                }

                //if the next number is on a the line stop
                if( indexConflits >= strTiles.length() || currentPositionTiles[0] != getRightPos(conflitPos + 1)[0])
                    endLine = true;
            }
            
            // cech conflits DOWN
            boolean endColumn = false;
            // set the first index of the right number
            indexConflits = index;

            //if searching for the left value we finish the stirng is done
            if(indexConflits > strTiles.length())
                endColumn = true;

            conflitPos = currentPos - 1;

            //System.out.println("board " + tilesToCheck);
            while(possibleColumnC && !endColumn) {

                //serch the number to check
                // go until find the right value
                boolean done = false;
                int counter = 0;
                while(indexConflits < strTiles.length() && !done) {
                    if(strTiles.charAt(indexConflits) == ' ' ) {
                        counter++; 
                        if (counter == Solver.boardSize - 1){
                            startNumb = indexConflits + 1;
                        }
                        else if (counter >= Solver.boardSize) {
                            done = true;
                            endNumb = indexConflits;
                        }
                
                    }
                    indexConflits++; 
                }
                indexConflits++; 

                //if is finishd the columns END THE LINE
                if(!done && (counter == Solver.boardSize - 1)) {
                    done = true;
                    endNumb = indexConflits - 1;
                } else if(!done) {
                    //System.out.println("finished column");
                    endColumn = true;
                    continue;
                }

                conflitPos += Solver.boardSize; 
                
                //System.out.println("Start Number" + startNumb);
                //System.out.println("End Number" + endNumb);
                
                //remember to check the position of the column and the value we are cheking
                //check if there is a conflit between the numbers
                
                int toChekNumb = Integer.parseInt(strTiles.substring(startNumb, endNumb));

                /* 
                System.out.println("DOWN");
                System.out.println("checking with " + chekingNumb);
                System.out.println("to check number " + toChekNumb);
                */

                int[] toCheck = getRightPos(toChekNumb);
                int[] toCheckPosTiles = getRightPos(conflitPos);

                /*
                System.out.println("number coumn " + checking[1]);
                System.out.println("to check number coumn " + toCheck[1]);
                System.out.println("current colum" + currentPositionTiles[1]);
                System.out.println("position to check colum" + toCheckPosTiles[1]);

                System.out.println("INDEX CONFLITSSSS " + indexConflits);
                */
                /*
                if((indexConflits < 0 && !done) || currentPositionTiles[1] != toCheckPosTiles[1] ||  checking[1] != toCheck[1])
                    endLine = true;
                else if (currentPositionTiles[0] == toCheck[0] && toCheckPosTiles[0] == checking[0]) {
                */
                if((currentPositionTiles[0] == toCheck[0] && currentPositionTiles[1] == toCheck[1]) && (checking[0] == toCheckPosTiles[0] && checking[1] == toCheckPosTiles[1])) {
                    counterConflits += 2;
                    //System.out.println("Board: " + tilesToCheck);
                    //System.out.println("CONFLIT");
                    //System.out.println("DOWN");
                    //System.out.println(strTiles);
                    System.out.println("Conflits between:: " + chekingNumb + " " + toChekNumb);
                }

                if(indexConflits > strTiles.length())
                    endColumn = true;

            }
        }

        return counterConflits;
    }

        // work with all sons 
        // 1 place : str of son
        // 2 palce : int of the previuos moved number
        // 3 place : new zero position of the son
        // 4 place : new zero index
        // 5 palce : new manhattan

    public LinkedList<Board> getMoves() {

        LinkedList<Board> sons = new LinkedList<>();
        Object[] son;

        //System.out.println("Starting board : \n" + strTiles);

        for(int i = 1; i <= 4; i++) {
            Object[] newSon = makeMove(i);
            if(newSon != null) {
                //System.out.println("figlio dir: " +  ((String)newSon[0]));
                sons.add( new Board((String)newSon[0], (int)newSon[2], (int)newSon[3], (int)newSon[1], (int)newSon[4]));
            }
            //else 
                //System.out.println("impossible movement dir: " + i);
        }

        return sons; 
    }


    private String toStrFromMatrix() {
        StringBuilder bTiles = new StringBuilder();

        for(int i = 0; i < Solver.boardSize; i++) {
            for(int j = 0; j < Solver.boardSize; j++) {
                bTiles.append(tiles[i][j] + " ");
            }
        }

        bTiles.deleteCharAt(bTiles.length() - 1);
        return bTiles.toString();
    }

    private void findZero(int[] tiles) {
        for(int i = 0; i < ti) 
    }

    @Override
    public String toString(){
        return strTiles;
        //return bTiles.toString();
    }

    @Override
    public boolean equals(Object o) {
        return strTiles.equals(((Board)o).toString());
        //return (bTiles.toString()).equals(((Board)o).toString());
    }

    @Override
    public int hashCode() {
        return (strTiles).hashCode();
        //return (bTiles.toString()).hashCode();
    }
}
