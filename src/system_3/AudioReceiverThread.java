package system_3;

import CMPC3M06.AudioPlayer;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.DatagramPacket;

import supportClasses.Utility;
import uk.ac.uea.cmp.voip.DatagramSocket3;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class AudioReceiverThread implements Runnable {

    private boolean running;
    private int port;
    private AudioPlayer player;
    private int totalPacketReceived;
    private DatagramSocket3 socket3;

    public AudioReceiverThread(int port) {
        this.port = port;
        this.running = false;
        totalPacketReceived = 0;
    }


    public void start() {
        running = true;

        try {
            socket3 = new DatagramSocket3(port);
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
        Object[] packetArray = new DatagramPacket[16];

        while (running){

            try {

                ByteBuffer packetData;
                byte[] buffer;
//                short seqNum;

                for (int i = 0; i < 16; i++) {
                    buffer = new byte[514];
                    packet = new DatagramPacket(buffer, 0, 514);
                    socket3.receive(packet);

                    packetArray[i] = packet;
                    totalPacketReceived++;

                }

                if (totalPacketReceived % 16 == 0) {
                    packetArray = Utility.deInterleave(packetArray);

                    for (int k = 0; k < 16; k++) {

                        packet = (DatagramPacket) packetArray[k];
                        packetData = ByteBuffer.wrap(packet.getData());
//                        seqNum = packetData.getShort(0);
////                        System.out.println("Packet " + seqNum); //debug
                        byte[] block = Arrays.copyOfRange(packetData.array(), 2, 514);
                 

                        player.playBlock(block);
                    }
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
