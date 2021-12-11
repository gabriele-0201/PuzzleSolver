import java.util.LinkedList;
import java.lang.Math;

public class Board {
    
    private int[][] tiles;
    private StringBuilder bTiles;
    private String strTiles;
    private int zeroPos;
    private int zeroIndex;
    private int prevMoved;
    private int manDist;

    //constructor for the first matrix
    public Board(int[][] tiles) {
        this.tiles = tiles;
        strTiles = toStrFromMatrix();
        findZero();

        //System.out.println("Zero index from borning: " + zeroIndex);
        //System.out.println("Zero Position borning: " + zeroPos);

        manDist = manhattan();
        manDist += initialLibearConflicts(strTiles, zeroPos);
    }

    private Board(String s, int zPos, int zIndex, int moved, int manhattan) {
        tiles = null;
        strTiles = s;
        zeroPos = zPos;
        zeroIndex = zIndex;
        prevMoved = moved;            
        manDist = manhattan;
    }

    public int getPrevMoved() {
        return prevMoved;
    }
    
    public int getManDist() {
        return manDist;
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
            manDist += initialLibearConflicts((String)son[0], (int)son[2]);
            son[4] = manDist;
            //here change with the more efficent version

            return son;
        }

        return null;
    }

    private int initialLibearConflicts(String tilesToCheck, int newZeroPos) {
        int counterConflits = 0;
        int currentPos = 1;
        int startNumb, endNumb;
        int index = 0;
        int indexConflits = 0;

        //System.out.println("Board: " + tilesToCheck);

        //first scann all the number in the board
        while(currentPos < Solver.boardSize * Solver.boardSize){
            //for each element of the board I have to scan the line and the column
            //and seacrh for the linear conflicts;

            //iterate while find a space and find the integer

            startNumb = index; //save the start of the number
            while(index < tilesToCheck.length()  && tilesToCheck.charAt(index) != ' ')
                index++;
            //set the index of the end number, one more for the subsstring
            endNumb = index;
            //skip the space
            index++;

            currentPos++;

            //minus one because we point already to the next
            if(currentPos - 1 == newZeroPos)
                continue;

            int chekingNumb = Integer.parseInt(tilesToCheck.substring(startNumb, endNumb));
            int[] checking = getRightPos(chekingNumb);
            //System.out.println("checking num: " + chekingNumb);
            //System.out.println("Curerent pos: " + currentPos - 1);
            int[] currentPositionTiles = getRightPos(currentPos - 1);

            boolean possibleLinearC = true;
            boolean possibleColumnC = true;

            //if the number we are checking is not in his line could not create a linear conflict
            if(checking[0] != currentPositionTiles[0])
                possibleLinearC = false;

            // check conflits on the RIGHT
            boolean endLine = false; 
            indexConflits = index;
            int conflitPos = currentPos - 1;
            //System.out.println("ZERO POS: " + zeroPos);

            while(possibleLinearC && !endLine) {

                startNumb = indexConflits; 
                while(indexConflits < tilesToCheck.length()  && tilesToCheck.charAt(indexConflits) != ' ')
                   indexConflits++;
                endNumb = indexConflits;
                indexConflits ++;

                conflitPos++;
                
                int toChekNumb = Integer.parseInt(tilesToCheck.substring(startNumb, endNumb));

                int[] toCheck = getRightPos(toChekNumb);
                //System.out.println("conflict pos " + conflitPos);
                int[] toCheckPosTiles = getRightPos(conflitPos);

                if(indexConflits >= tilesToCheck.length() ||  (currentPositionTiles[0] != toCheckPosTiles[0] ||  checking[0] != toCheck[0]))
                    endLine = true;
                else if (currentPositionTiles[1] == toCheck[1] && toCheckPosTiles[1] == checking[1]) {
                    counterConflits++;
                    //System.out.println("Board: " + tilesToCheck);
                    //System.out.println("Conflits between:: " + chekingNumb + " " + toChekNumb);
                }
            }
            
            // check conflits on LEFT
            // now the index point to the right number we are working with
            endLine = false; 
            // - 2 becouse we have to go back and SKIP the space and go to the end of the previous number
            indexConflits = index - 2;

            // search for the right number to start (the first on the left)
            int spaceCount = 0;
            while(indexConflits >= 0  && tilesToCheck.charAt(indexConflits) != ' ') {
                //spaceCount++;
                indexConflits--;
                //if(spaceCount == 2)
                    //break;
            }
            indexConflits--;

            //if searching for the left value we finish the stirng is done
            if(indexConflits < 0)
                endLine = true;

            conflitPos = currentPos - 1;

            //System.out.println("Index in the string" + indexConflits);

            while(possibleLinearC && !endLine) {

                endNumb = indexConflits + 1; 
                while(indexConflits >= 0  && tilesToCheck.charAt(indexConflits) != ' ')
                   indexConflits--;
                startNumb = indexConflits + 1;
                //go to the next number
                indexConflits --;

                //System.out.println("Start Number" + startNumb);
                //System.out.println("End Number" + endNumb);

                conflitPos++;
                
                int toChekNumb = Integer.parseInt(tilesToCheck.substring(startNumb, endNumb));

                int[] toCheck = getRightPos(toChekNumb);
                int[] toCheckPosTiles = getRightPos(conflitPos);

                if(indexConflits < 0 || currentPositionTiles[0] != toCheckPosTiles[0] ||  checking[0] != toCheck[0])
                    endLine = true;
                else if (currentPositionTiles[1] == toCheck[1] && toCheckPosTiles[1] == checking[1]) {
                    counterConflits++;
                    //System.out.println("Board: " + tilesToCheck);
                    //System.out.println("Conflits between:: " + chekingNumb + " " + toChekNumb);
                }
            }
        
            // check conflits up
             
            
            // cech conflits down 
        }

        return counterConflits;


    }

    private int linearConflicts(String b) {
        //Have to check all the line of the initiial 
        return 0;
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
                //System.out.println("figlio dir: " + i + " - " +  ((String)newSon[0]));
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

    private void findZero() {
        int counter = 1; 
        for(int i = 0; i < strTiles.length(); i++) {

            if(strTiles.charAt(i) == ' ')
                counter++;
            else if(strTiles.charAt(i) == '0') {
                
                //this could be done better
                //chek before is at the start
                if(i - 1 < 0) {
                        zeroPos = counter;
                        zeroIndex = i;
                } else if(strTiles.charAt(i - 1) == ' ') {
                    
                    if(i + 1 >= strTiles.length()) {
                        zeroPos = counter;
                        zeroIndex = i;
                    } else if(strTiles.charAt(i + 1) == ' ') {
                        zeroPos = counter;
                        zeroIndex = i;
                    }
                } 
            }
        }
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
