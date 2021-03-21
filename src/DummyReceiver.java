import uk.ac.uea.cmp.voip.DatagramSocket2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.nio.ByteBuffer;


public class DummyReceiver implements Runnable {

    private DatagramSocket2 socket;
    //    private DatagramSocket3 socket;
    private int port;

    public DummyReceiver(int port) {
        this.port = port;
    }

    @Override
    public void run() {

        try {
            socket = new DatagramSocket2(port);
//            socket = new DatagramSocket3(port);

        } catch (SocketException e) {
            e.printStackTrace();
        }


        DatagramPacket packet;
        byte[] buffer;
        ByteBuffer packetData;
        short seqNum;

        System.out.println("receiver started ...");
        for (int i = 0; i < 10; i++) {


            try {

                buffer = new byte[514];
                packet = new DatagramPacket(buffer, 514);
                socket.receive(packet);

                packetData = ByteBuffer.wrap(buffer);

                seqNum = packetData.getShort();

                System.out.println("Packet: " + seqNum);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
}
