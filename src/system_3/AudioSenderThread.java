package system_3;

import CMPC3M06.AudioRecorder;

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

    public void start() {
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
        int key = 15;

        DatagramPacket packet;
        System.out.println("Recording voice ...");
        while (running) {

            try {
                block = recorder.getBlock();

                packetData = ByteBuffer.allocate(512);
//                packetData.put(block); unencrypted block data
                packetData.put(encryptData(block, key)); //encrypted block data

                packet = new DatagramPacket(packetData.array(), 0, 512, clientIP, port);
//                System.out.println("DATA(S): "  + Arrays.toString(packetData.array()) ); //Debug
                sendingSocket.send(packet);
                totalPacketSent++;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Encrypts data using a 32 bit XOR operation
     *
     * @param data unencrypted data
     * @param key  integer key
     * @return encrypted data
     */
    public byte[] encryptData(byte[] data, int key) {
        ByteBuffer unwrapEncrypt = ByteBuffer.allocate(data.length);
        ByteBuffer plainText = ByteBuffer.wrap(data);

        for (int j = 0; j < data.length / 4; j++) {
            int fourByte = plainText.getInt();
            fourByte = fourByte ^ key; //XOR operation with key
            unwrapEncrypt.putInt(fourByte);
        }

        return unwrapEncrypt.array();
    }

    public int getTotalPacketSent() {
        return totalPacketSent;
    }
}
