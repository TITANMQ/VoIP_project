package system_1;

import CMPC3M06.AudioRecorder;
import supportClasses.Utility;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public class AudioSenderThread implements Runnable {

    private DatagramSocket sendingSocket;
    private AudioRecorder recorder;
    private int port = 55555;
    private String hostname;
    private boolean running;
    private InetAddress clientIP;
    private int totalPacketSent;
    private static final int PACKET_SIZE = 514; // authKey(2) + block(512)

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
            sendingSocket = new DatagramSocket();

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
        int encryptionKey = 15;
        short authenticationKey = 10;

        DatagramPacket packet;
        System.out.println("Recording voice ...");
        while (running) {

            try {
                block = recorder.getBlock();

                packetData = ByteBuffer.allocate(PACKET_SIZE);
//                packetData.put(block); unencrypted block data
                packetData.putShort(authenticationKey); //add auth key
                packetData.put(Utility.encryptData(block, encryptionKey)); //encrypted block data

                packet = new DatagramPacket(packetData.array(), 0, PACKET_SIZE, clientIP, port);
//                System.out.println("DATA(S): "  + Arrays.toString(packetData.array()) ); //Debug
                sendingSocket.send(packet);
                totalPacketSent++;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public int getTotalPacketSent() {
        return totalPacketSent;
    }
}
