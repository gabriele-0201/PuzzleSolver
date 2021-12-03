import java.util.LinkedList;

public class Board {

    //La matrice potrei eliminarla una volta avuto i figli
    private int[][] tiles;
    private StringBuilder bTiles;
    private int zeroPos;
    private int prevMoved;
    private int manDist;

    public Board(int[][] tiles) {
        this.tiles = tiles;
        toStrBuilder();
        zeroPos = findZero();
        manDist = manhattan();
    }

    private Board(StringBuilder s, int zPos, int moved, int manhattan) {
        tiles = null;
        this.bTiles = s;
        zeroPos = zPos;
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
    private Object[] makeMove(int dir) {
        Object[] son = null;
         
        StringBuilder strSon = new StringBuilder(bTiles);

        int counter = 0; 
        int index = zeroPos;
        //index used for the replace, other side of the index
        int otherIndex = -1;
        int moved = -1;
        boolean done = false;

        switch(dir) {
            case 1: //up
                index --; 
               
                // go until find the right value to replace
                while(index >= 0 || counter < Solver.boardSize) {
                    if(bTiles.charAt(index) == ' ') {
                        otherIndex = index;
                        counter++; 
                    }
                    index--; 
                }
                
                // replace the value and store the moved char
                if(counter == Solver.boardSize) 
                    done = true;
                else 
                    return null;

                break;

            case 3: //down
                
                index ++;

                while(index <= bTiles.length() || counter < Solver.boardSize) {
                    if(bTiles.charAt(index) == ' ') {
                        otherIndex = index;
                        counter++; 
                    }
                    index++; 
                }

                if(counter == Solver.boardSize) 
                    done = true;
                else 
                    return null;

                break;

            case 2: //right
                
                index++;

                if(index >= bTiles.length())
                    return null;
               
                otherIndex = index;
                while(bTiles.charAt(index) != ' ')
                    index++;
                index--;

                done = true;

                break;

            case 4: //left

                index--;

                if(index < 0)
                    return null;
               
                otherIndex = index;
                while(bTiles.charAt(index) != ' ')
                    index--;
                index++;

                done = true;

                break;
        }

        if(done) {

            String strMoved;

            switch (dir) {
                case 1:
                case 4:

                    strMoved = strSon.substring(index, otherIndex);
                    moved = Integer.parseInt(strMoved);

                    if (prevMoved == moved)
                        return null;

                    strSon.replace(index, otherIndex, "0");
                    strSon.replace(zeroPos, zeroPos, strMoved);

                    break;
                case 3:
                case 2:

                    strMoved = strSon.substring(otherIndex, index);
                    moved = Integer.parseInt(strMoved);
                    
                    if (prevMoved == moved)
                        return null;

                    strSon.replace(otherIndex, index, "0");
                    strSon.replace(zeroPos, zeroPos, strMoved);

                    break;
            }

            son = new Object[4];
            son[0] = strSon;
            son[1] = moved;
            
            int[] newPos = null;
            
            switch (dir) {
                case 1:
                    son[2] = zeroPos - Solver.boardSize;
                    newPos = getRightPos(zeroPos - Solver.boardSize);
                    break; 
                case 2:
                    son[2] = zeroPos + 1;
                    newPos = getRightPos(zeroPos + 1);
                    break; 
                case 3:
                    son[2] = zeroPos + Solver.boardSize;
                    newPos = getRightPos(zeroPos + Solver.boardSize);
                    break; 
            }

            // How Can I calculate the new Manhattan?
            // Now the number moved is in the ZERO POSITION
            // So I can calculate the manhattan like the zero is the moved number
            // and see if it is increased or decreased
            
            int[] oldPos = getRightPos(zeroPos);
            int[] rightPos = getRightPos(moved);
            
            if((oldPos[0] - rightPos[0]) < (newPos[0] - rightPos[0])) {
               son[3] = manDist + 1;
            } else if((oldPos[1] - rightPos[1]) < (newPos[1] - rightPos[1])) {
               son[3] = manDist + 1;
            } else {
                son[3] = manDist - 1;
            }

            return son;
        }

        return null;
    }

        // work with all sons 
        // 1 place : str of son
        // 2 palce : int of the previuos moved number
        // 3 place : new zero position of the son
        // 4 palce : new manhattan
   

    public LinkedList<Board> getMoves() {

        LinkedList<Board> sons = new LinkedList<>();
        Object[] son;

        for(int i = 1; i <= 4; i++) {
            Object[] newSon = makeMove(i);
            if(newSon != null)
                sons.add( new Board((StringBuilder)newSon[0], (int)newSon[2], (int)newSon[1], (int)newSon[3]) );
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

    private int findZero() {
        int counter = 0; 
        for(int i = 0; i < bTiles.length(); i++) {

            if(bTiles.charAt(i) == ' ')
                counter++;
            else if(bTiles.charAt(i) == '0') {
                counter++;
                return counter;    
            }

        }
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        return bTiles.equals((StringBuilder)o);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
