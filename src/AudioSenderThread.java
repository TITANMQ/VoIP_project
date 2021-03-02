import CMPC3M06.AudioRecorder;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.*;

public class AudioSenderThread implements Runnable {

    private DatagramSocket sendingSocket;
    private AudioRecorder recorder;
    private int port = 55551;
    private String hostname;
    private boolean running;
    private InetAddress clientIP;

    public AudioSenderThread(String hostname) {
        try {
            this.hostname = hostname;
            running = false;
            recorder = new AudioRecorder();
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

        Thread thread = new Thread(this);

        thread.start();
    }

    @Override
    public void run() {
        byte[] block;
        DatagramPacket packet;
        System.out.println("Recording voice ...");
        while(running){

            try {
                block = recorder.getBlock();
                packet = new DatagramPacket(block, 0,512, clientIP, port);
                sendingSocket.send(packet);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}
