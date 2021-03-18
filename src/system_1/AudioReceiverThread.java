package system_1;

import CMPC3M06.AudioPlayer;
import supportClasses.Utility;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class AudioReceiverThread implements Runnable {

    private boolean running;
    private int port;
    private DatagramSocket socket1;
    private AudioPlayer player;
    private int totalPacketReceived;
    private static final int PACKET_SIZE = 514; // authKey(2) + block(512)

    public AudioReceiverThread(int port) {
        this.port = port;
        this.running = false;
        totalPacketReceived = 0;
    }


    public void start() {
        running = true;

        try {
            socket1 = new DatagramSocket(port);
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
        short authenticationKey = 10;
        ByteBuffer packetData;
        byte[] block;


        while (running) {

            try {

                byte[] buffer = new byte[PACKET_SIZE];

                packet = new DatagramPacket(buffer, 0, PACKET_SIZE);

                socket1.receive(packet);

                packetData = ByteBuffer.wrap(buffer);

                short receivedAuthKey = packetData.getShort(0);

                if (receivedAuthKey == authenticationKey) {

                    block = Arrays.copyOfRange(packetData.array(), 2, 514);
                    block = Utility.decryptData(block, key);
                    totalPacketReceived++;
//                    System.out.println("DATA(R): " + Arrays.toString(packet.getData())); //debug
                    player.playBlock(block);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    

    public void setRunning(boolean running) {
        this.running = running;
    }

    public int getTotalPacketReceived() {
        return totalPacketReceived;
    }
}
