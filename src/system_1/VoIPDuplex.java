package system_1;

public class VoIPDuplex {

    public static void main(String[] args) {

        int port = 55555;
        AudioReceiverThread receiver = new AudioReceiverThread(port);
        AudioSenderThread sender = new AudioSenderThread("localhost", port);

        Thread receiverThread = new Thread(receiver);
        Thread senderThread = new Thread(sender);

        senderThread.start();
        receiverThread.start();

        try {
            senderThread.join();
            receiverThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
