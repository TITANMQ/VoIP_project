package system_2;

import CMPC3M06.AudioRecorder;
import supportClasses.Utility;
import uk.ac.uea.cmp.voip.DatagramSocket2;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class AudioSenderThread implements Runnable {

    private DatagramSocket2 sendingSocket;
    private AudioRecorder recorder;
    private int port = 55555;
    private String hostname;
    private boolean running;
    private InetAddress clientIP;
    private int totalPacketSent;

    public AudioSenderThread(String hostname, int port) {
        try {
            this.port = port;
            this.hostname = hostname;
            running = false;
            recorder = new AudioRecorder();
            totalPacketSent = 0;
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    public void start(){
        running = true;
        clientIP = null;
        try {
            clientIP = InetAddress.getByName(hostname);
            sendingSocket = new DatagramSocket2();

        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }

    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        start();
        byte[] block;
        ByteBuffer packetData;
        int key = 15;
        Object[] transmitPackets = new Object[16];
        short seqNum;

        DatagramPacket packet;
        System.out.println("Recording voice ...");
        while (running) {

            try {
                for (int i = 0; i < 16; i++) {

                    seqNum = (short) i;
                    block = recorder.getBlock();

                    packetData = ByteBuffer.allocate(514); //+2 bytes seqnum
                    packetData.put(block); // unencrypted block data
                    packetData.putShort(seqNum);     //add sequence number to header
                   ata


                    packet = new DatagramPacket(packetData.array(), 0, 514, clientIP, port);
//                  System.out.println("Sender packet:  " + seqNum + " data: " + Arrays.toString(packetData.array()) ); //Debug

                    transmitPackets[i] = packet;

                }

                transmitPackets = Utility.blockInterleave(transmitPackets);

                //loop through the array and send each packet
                for (int k = 0; k < 16; k++) {
                    sendingSocket.send(((DatagramPacket) transmitPackets[k]));
                    totalPacketSent++;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getTotalPacketSent() {
        return totalPacketSent;
    }
}
