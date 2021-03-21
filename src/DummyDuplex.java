public class DummyDuplex {

    public static void main(String[] args) {
        DummySender sender = new DummySender("localhost", 55555);
        DummyReceiver receiver = new DummyReceiver(55555);


        sender.run();
        receiver.run();
    }
}
