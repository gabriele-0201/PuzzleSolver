import  java.lang.Math;

class Example {

    public static int size = 10;

    public static long[] ctiles; //compressed tiles
    public static int B; //number of bit for any number, this is also the offset for each mask
    public static int N; //number of value for each double

    Example(int[][] tiles) {
        System.out.println("risultato per il log " + log2(size * size));
        B = (int)Math.ceil(log2((size * size) - 1));
        System.out.println("Bit per numnero " + B);
        N = (int)Math.floor(64 / B); 
        System.out.println("Niumero di val possibili per long " + N);

        
        //Maybe not work
        int totalNum = (size * size);
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

        long mask = getMask(pos);

        //return (int)((long)Math.floor((mask & ctiles[index])) >> (pos * B));
        return (int)((mask & ctiles[index]) >> (pos * B));
    }

    public static void setVal(int r, int c, long val) {

        int pos = getPos(r, c);
        //System.out.println("Position total: "  + pos);
        int index = pos / N;
        //System.out.println("Index of the array: "  + index);
        pos = pos % N; // this return the new position in the right array
        //System.out.println("Position in the array: "  + pos);

        //int mask = (Math.pow(2, B + 1) - 1) * Math.pow(2, pos * B); //mask to extract the number
        long mask = getMask(pos);
        //printBit(mask);
        //System.out.println();

        ctiles[index] = (~(mask & ctiles[index])) & ctiles[index] ; //remove the value in the specified pos
        //printBit(ctiles[index]);
        //System.out.println();

        val = val << (pos * B);
        //printBit(val);
        //System.out.println();

        ctiles[index] = (ctiles[index]) | (val); //insert the val in the right place
        //System.out.println("index " + index);
        //printBit(ctiles[index]);
        //System.out.println();
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
        //printBit(mask1Bit) ;
        //System.out.println();
        //System.out.println(Long.toBinaryString(mask1Bit));
        return mask1Bit << (pos * B);
    }

    public static int getPos(int r, int c) {
        int pos = r * size;
        pos += c;
        return pos;
    }

    public static void compress(int[][] tiles) {
        //StringBuilder str = new StringBuilder();
        //int counterPosition = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                setVal(i, j, tiles[i][j]);
                //str.append(tiles[i][j]);
            }
        }
    }

     public static double log2(int N){
        return (Math.log(N) / Math.log(2));
    }
public static void main (String[] args) {
    

    int[][] tiles = new int[size][size];

    int c = 1;
    for(int i=0; i < size; i++)
        for(int j=0; j < size; j++)
            if(c == size * size)
                tiles[i][j] = 0;
            else
                tiles[i][j]=c++;

    Example ex = new Example(tiles);

    for(int i=0; i < size; i++){
        for(int j=0; j < size; j++)
            System.out.print(ex.getVal(i, j) + " ");
        System.out.println();
    }
}
}

