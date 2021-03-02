public class VoIPDuplex {

    public static void main(String[] args) {

        AudioReceiverThread receiverThread = new AudioReceiverThread();
        AudioSenderThread senderThread = new AudioSenderThread("localhost");

        receiverThread.start();
        senderThread.start();
    }
}
