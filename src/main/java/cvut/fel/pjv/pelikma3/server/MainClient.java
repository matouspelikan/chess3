package cvut.fel.pjv.pelikma3.server;

public class MainClient {
    public static void main(String[] args) {
        Client client = new Client("34.89.199.137", 9800);
        client.connect();
    }
}
