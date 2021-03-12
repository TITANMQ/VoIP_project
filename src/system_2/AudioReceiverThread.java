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
    private DatagramSocket socket2;
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
            socket2 = new DatagramSocket(port);
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
        Object[] packetArray = new Object[16];

        while (running){

            try {

                byte[] buffer = new byte[512];
                
                for(int i = 0; i < 17; i++){
                    packet = new DatagramPacket(buffer, 0, 512);
                    socket2.receive(packet);
                    packetArray[i] = packet;
                                       
                }
                
                packetArray = Utility.deInterleave(packetArray);
                
                for(int k = 0; k < 17; k++){
                    byte[] decryptedBlock = decryptData(packetArray[k].getData(), key);
                    player.playBlock(decryptedBlock);
                }


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
