package system_2;

import CMPC3M06.AudioPlayer;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class AudioReceiverThread implements Runnable {

    private boolean running;
    private int port;
    private DatagramSocket socket1;
    private DatagramSocket socket2;
    private DatagramSocket socket3;
    private AudioPlayer player;
    private int totalPacketReceived;

    public AudioReceiverThread(int port) {
        this.port = port;
        this.running = false;
        totalPacketReceived = 0;
    }


    public void start() {
        running = true;

        try {
            socket1 = new DatagramSocket(port);
            // socket2 = new DatagramSocket(port);
            // socket3 = new DatagramSocket(port);
            player = new AudioPlayer();
        } catch (SocketException e) {
            System.out.println("ERROR: TextReceiver: Could not open UDP socket to receive from.");
            e.printStackTrace();
            System.exit(0);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.exit(0);
        }

    }


    @Override
    public void run() {
        start();
        DatagramPacket packet;
        int key = 15;

        while (running){

            try {

                byte[] buffer = new byte[512];
                
                packet = new DatagramPacket(buffer, 0, 512);

                socket1.receive(packet);
                // socket2.receive(packet);
                // socket3.receive(packet);
                totalPacketReceived++;

                byte[] decryptedBlock = decryptData(buffer, key);

                player.playBlock(decryptedBlock);
//                System.out.println("DATA(R): " + Arrays.toString(packet.getData())); //debug


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
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

    public void setRunning(boolean running) {
        this.running = running;
    }

    public int getTotalPacketReceived() {
        return totalPacketReceived;
    }
}
