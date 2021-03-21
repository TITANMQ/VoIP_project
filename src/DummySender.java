import uk.ac.uea.cmp.voip.DatagramSocket2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class DummySender implements Runnable {

    private DatagramSocket2 socket;
    //    private DatagramSocket3 socket;
    private String hostname;
    private int port;

    public DummySender(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public void run() {

        InetAddress clientIP = null;
        try {
            clientIP = InetAddress.getByName(hostname);
            socket = new DatagramSocket2();
//            socket = new DatagramSocket3();

        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }

        DatagramPacket packet;
        ByteBuffer packetData;
        byte[] block = new byte[512];
//        byte[] buffer = new byte[514];

        System.out.println("sender started ...");
        for (int i = 0; i < 10; i++) {


            packetData = ByteBuffer.allocate(514);
            packetData.putShort((short) i);
            packetData.put(block);


            try {
                packet = new DatagramPacket(packetData.array(), 514, clientIP, port);
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        
    }
}
