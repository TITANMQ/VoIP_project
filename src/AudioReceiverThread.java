import CMPC3M06.AudioPlayer;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class AudioReceiverThread implements Runnable {

    private boolean running;
    private int port = 55551;
    private DatagramSocket receivingSocket;
    private AudioPlayer player;

    public AudioReceiverThread() {
        this.running = false;
    }

    public void start(){
        running = true;

        try {
            receivingSocket = new DatagramSocket(port);
            player = new AudioPlayer();
        } catch (SocketException e) {
            System.out.println("ERROR: TextReceiver: Could not open UDP socket to receive from.");
            e.printStackTrace();
            System.exit(0);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.exit(0);
        }

        Thread thread = new Thread(this);

        thread.start();

    }


    @Override
    public void run() {
        DatagramPacket packet;

        while (running){

            try {

                byte[] buffer = new byte[512];
                packet = new DatagramPacket(buffer, 0, 512);

                receivingSocket.receive(packet);

                player.playBlock(packet.getData());

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
