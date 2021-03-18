package system_2;

import CMPC3M06.AudioPlayer;
import uk.ac.uea.cmp.voip.DatagramSocket2;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Comparator;

public class AudioReceiverThread implements Runnable {

    private boolean running;
    private int port;
    private DatagramSocket2 socket2;
    private AudioPlayer player;
    private int totalPacketReceived;
    private final int sendTotal = 16;

    public AudioReceiverThread(int port) {
        this.port = port;
        this.running = false;
        totalPacketReceived = 0;
    }


    public void start() {
        running = true;

        try {
            socket2 = new DatagramSocket2(port);
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
        Object[] packetArray = new DatagramPacket[sendTotal];

        while (running){

            try {

                ByteBuffer packetData;
                byte[] buffer;
//                short seqNum;

                for (int i = 0; i < sendTotal; i++) {
                    buffer = new byte[514];
                    packet = new DatagramPacket(buffer, 0, 514);
                    socket2.receive(packet);

                    packetArray[i] = packet;
                    totalPacketReceived++;

                }

                if (totalPacketReceived % sendTotal == 0) {

                    Comparator<DatagramPacket> bySeq =
                            (DatagramPacket packet1, DatagramPacket packet2) -> {
                                ByteBuffer packetData1 = ByteBuffer.wrap(packet1.getData());
                                short seqNum1 = packetData1.getShort(0);

                                ByteBuffer packetData2 = ByteBuffer.wrap(packet2.getData());
                                short seqNum2 = packetData2.getShort(0);

                                return Integer.compare(seqNum1, seqNum2);
                            };
                    Arrays.sort((DatagramPacket[]) packetArray, bySeq);


                    System.out.println("-------------------------------");
                    for (int k = 0; k < sendTotal; k++) {

                        packet = (DatagramPacket) packetArray[k];
                        packetData = ByteBuffer.wrap(packet.getData());
                        short seqNum = packetData.getShort(0);
                        System.out.println("Packet " + seqNum); //debug
                        byte[] block = Arrays.copyOfRange(packetData.array(), 2, 514);

                        player.playBlock(block);
                    }
                    System.out.println("-------------------------------");
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
