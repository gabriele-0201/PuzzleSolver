import java.util.LinkedList;
import java.lang.Math;

public class Board {

    //La matrice potrei eliminarla una volta avuto i figli
    private int[][] tiles;
    private StringBuilder bTiles;
    private int zeroPos;
    private int zeroIndex;
    private int prevMoved;
    private int manDist;

    public Board(int[][] tiles) {
        this.tiles = tiles;
        toStrBuilder();
        findZero();
        manDist = manhattan();
    }

    private Board(StringBuilder s, int zPos, int zIndex, int moved, int manhattan) {
        tiles = null;
        this.bTiles = s;
        zeroPos = zPos;

        //System.out.println("Zero pos: " + zeroPos);

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

    public StringBuilder getStrBuilder() {
        return bTiles;
    }

    public int manhattan() {
        if(tiles == null)
            return -1;

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

    // --> Have to decide if return direct the right array af Object with all the information
    //Or return only the string builder
    //
    //OBVIOUSLY is better the first idea
    //
    //dir: 1 - up, 2 - right, 3 - down, 4 - left
    //
    //
    //ATTENZIONE DIFFERENZA TRA INDEXZERO E ZEROPOS
    //
    private Object[] makeMove(int dir) {
        //System.out.println("Direction: " + dir);
        Object[] son = null;
         
        StringBuilder strSon = new StringBuilder(bTiles);

        int counter = 0; 
        int index = zeroIndex;
        int newZeroIndex = -1;
        //index used for the replace, other side of the index
        int otherIndex = -1;
        int moved = -1;
        boolean done = false;
        
        //System.out.println("Starting index: " + index);
        //System.out.println("right index" + getRightIndex());
        switch(dir) {
            case 1: //up
                counter = -1;
                index--; 
               
                // go until find the right value to replace
                while(index >= 0 && !done) {
                    if(bTiles.charAt(index) == ' ' ) {

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
                while(index < bTiles.length() && !done) {
                    if(bTiles.charAt(index) == ' ') {

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
                
                //TODO have to check if the while is finished BEFORE finding a new space
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

                if(index > bTiles.length())
                    return null;

                //Add the check to the line
                // The right number have to be in the same position of the previous 
                if((zeroPos - 1) / Solver.boardSize != (zeroPos) / Solver.boardSize )
                    return null;
                
                //System.out.println("QUI SuPerA");

                otherIndex = index; //save the start of the number

                while(index < bTiles.length() && bTiles.charAt(index) != ' ')
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
                while(index >= 0 && bTiles.charAt(index) != ' ')
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

                    strMoved = strSon.substring(index, otherIndex + 1);
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

                    strMoved = strSon.substring(otherIndex, index + 1);
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
            son[0] = strSon;
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
            
            //System.out.println("Manhattan Dead: " + manDist);

            if(Math.abs(oldPos[0] - rightPos[0]) < Math.abs(newPos[0] - rightPos[0])) {
                son[4] = manDist + 1;
                //System.out.println("Manhattan aumentato per la linea: " + (int)son[4]);
            } else if(Math.abs(oldPos[1] - rightPos[1]) < Math.abs(newPos[1] - rightPos[1])) {
                son[4] = manDist + 1;

                //System.out.println("vecchia colonna: " + oldPos[1]);
                //System.out.println("nuova colonna: " + newPos[1]);
                //System.out.println("giusta colonna: " + rightPos[1]);

                //System.out.println("Manhattan aumentato per la colonna: " + (int)son[4]);
            } else {
                son[4] = manDist - 1;
                //System.out.println("Manhattan diminuito: " + (int)son[4]);
            }

            return son;
        }

        return null;
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

        //System.out.println("Starting board : \n" + bTiles.toString());

        for(int i = 1; i <= 4; i++) {
            Object[] newSon = makeMove(i);
            if(newSon != null) {
                //System.out.println("figlio dir: " + i + " - " +  ((StringBuilder)newSon[0]).toString());
                sons.add( new Board((StringBuilder)newSon[0], (int)newSon[2], (int)newSon[3], (int)newSon[1], (int)newSon[4]) );
            }
            //else 
                //System.out.println("impossible movement dir: " + i);
        }

        return sons; 
    }

    private void toStrBuilder() {
        bTiles = new StringBuilder();

        for(int i = 0; i < Solver.boardSize; i++) {
            for(int j = 0; j < Solver.boardSize; j++) {
                bTiles.append(tiles[i][j] + " ");
            }
        }

        bTiles.deleteCharAt(bTiles.length() - 1);
    }

    private void findZero() {
        int counter = 1; 
        for(int i = 0; i < bTiles.length(); i++) {

            if(bTiles.charAt(i) == ' ')
                counter++;
            else if(bTiles.charAt(i) == '0') {
                if(i - 1 >=0 && bTiles.charAt(i - 1) == ' ') {
                    if(i + 1 < bTiles.length() && bTiles.charAt(i + 1) == ' ') {
                        zeroPos = counter;
                        zeroIndex = i;
                    }
                }
            }
        }
    }

    private int getRightIndex() {
        
        for(int i = 0; i < bTiles.length(); i++) {

            if(bTiles.charAt(i) == '0') {
                if(i - 1 >=0 && bTiles.charAt(i - 1) == ' ') {
                    if(i + 1 < bTiles.length() && bTiles.charAt(i + 1) == ' ') {
                    }
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public String toString(){
        return bTiles.toString();
    }

    @Override
    public boolean equals(Object o) {
        return (bTiles.toString()).equals(((Board)o).toString());
    }

    @Override
    public int hashCode() {
        return (bTiles.toString()).hashCode();
    }
}
