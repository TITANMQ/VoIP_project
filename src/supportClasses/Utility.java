package supportClasses;

import java.util.Arrays;

public class Utility {


    public static Object[] blockInterleave(Object [] b){
        Object[] result = new Object[b.length];
        int d = 4; //TODO change this from hardcoded
        int c = 0;

        for(int i = 0; i < d; i++) {

            for (int j = 0; j < d; j++) {
                Object x = b[c];
                int p = j * d + (d - 1 - i);
                result[p] = x;
                c++;
            }

        }
        return result;
    }
    
    public static Object[] deInterleave(Object[] b){
        for(int i =0; i < 3; i++){
            b = blockInterleave(b);
        }
        return b;
    }


    //test harness
    public static void main(String[] args) {
        Object[] y = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};

//        System.out.println(Arrays.toString(blockInterleave(x)));

        y = blockInterleave(y);
        System.out.println(Arrays.toString(y));
        y = blockInterleave(y);
        System.out.println(Arrays.toString(y));
        y = blockInterleave(y);
        System.out.println(Arrays.toString(y));
        y = blockInterleave(y);
        System.out.println(Arrays.toString(y));

    }
}
