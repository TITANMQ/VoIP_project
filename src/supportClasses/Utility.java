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
    
    
    public byte[] decryptData(byte[] data, int key){
        ByteBuffer unwrapDecrypt = ByteBuffer.allocate(data.length);
        ByteBuffer cypherText =  ByteBuffer.wrap(data);

        for(int j = 0; j < data.length/4; j++){
            int fourByte = cypherText.getInt();
            fourByte = fourByte ^ key; //XOR operation with key
            unwrapDecrypt.putInt(fourByte);
        }

        return unwrapDecrypt.array();
    }
    
    
    public byte[] encryptData(byte[] data, int key){
        ByteBuffer unwrapEncrypt = ByteBuffer.allocate(data.length);
        ByteBuffer plainText =  ByteBuffer.wrap(data);

        for(int j = 0; j < data.length/4; j++){
            int fourByte = plainText.getInt();
            fourByte = fourByte ^ key; //XOR operation with key
            unwrapEncrypt.putInt(fourByte);
        }

        return unwrapEncrypt.array();
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
