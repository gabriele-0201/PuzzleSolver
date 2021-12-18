import  java.lang.Math;

class Example {

    public static long[] ctiles; //compressed tiles
    public static int B; //number of bit for any number, this is also the offset for each mask
    public static int N; //number of value for each double

    Example(int[][] tiles) {
        System.out.println("risultato per il log " + log2(3 * 3));
        B = (int)Math.ceil(log2(3 * 3));
        System.out.println("Bit per numnero " + B);
        N = (int)Math.floor(64 / B); 
        System.out.println("Numero di val possibili per long " + N);

        
        //Maybe not work
        int totalNum = (3 * 3);
        int dim = 1;
        while(totalNum > 0 && totalNum > N) {
            totalNum -= N;
            dim++;
        }

        System.out.println("Dimensione dell'array " + dim);

        ctiles = new long[dim];
        compress(tiles);
    }

    public static int getVal(int r, int c) {
        
        int pos = getPos(r, c);
        int index = pos / N;
        pos = pos % N; // this return the new position in the right array

        //int mask = (Math.pow(2, B + 1) - 1) * Math.pow(2, pos * B); //mask to extract the number
        long mask = getMask(pos);

        return (int)Math.floor((mask & ctiles[index]) / Math.pow(2, pos * B));
    }

    public static void setVal(int r, int c, long val) {

        int pos = getPos(r, c);
        System.out.println("Position total: "  + pos);
        int index = pos / N;
        System.out.println("Index of the array: "  + index);
        pos = pos % N; // this return the new position in the right array
        System.out.println("Position in the array: "  + pos);

        //int mask = (Math.pow(2, B + 1) - 1) * Math.pow(2, pos * B); //mask to extract the number
        long mask = getMask(pos);
        System.out.println();
        printBit(mask);

        ctiles[index] = (~(mask & ctiles[index])) & ctiles[index] ; //remove the value in the specified pos
        val = val * (long)Math.pow(2, pos * B);
        ctiles[index] = (ctiles[index]) & (val); //insert the val in the right place

        System.out.println(ctiles[0]);
        printBit(ctiles[0]);
        System.out.println();
    }

    public static void printBit(long v) {
        for(int i = 63; i >=0; i--)
            if((v & ((long)1 << i)) != 0)
                System.out.print("1");
            else
                System.out.print("0");
    }

    public static long getMask(int pos) {
        long mask1Bit = (long)Math.pow(2, B) - 1;
        printBit(mask1Bit) ;
        System.out.println();
        System.out.println(Long.toBinaryString(mask1Bit));
        return mask1Bit * (long)Math.pow(2, pos * B);
    }

    public static int getPos(int r, int c) {
        int pos = r * 3;
        pos += c;
        return pos;
    }

    public static void compress(int[][] tiles) {
        //StringBuilder str = new StringBuilder();
        int counterPosition = 1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                setVal(i, j, tiles[i][j]);
                //str.append(tiles[i][j]);
            }
        }
    }

     public static double log2(int N){
        return (Math.log(N) / Math.log(2));
    }
public static void main (String[] args) {
    

    int[][] tiles = new int[3][3];

    int c = 1;
    for(int i=0; i < 3; i++)
        for(int j=0; j < 3; j++)
            tiles[i][j]=c++;

    Example ex = new Example(tiles);
    System.out.println(ex.ctiles[0]);


}
}

