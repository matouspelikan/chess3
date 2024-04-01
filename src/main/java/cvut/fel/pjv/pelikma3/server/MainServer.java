package cvut.fel.pjv.pelikma3.server;

public class MainServer {
    public static void main(String[] args) {
        ChessServer chessServer = new ChessServer(9800);
        chessServer.start();
    }
}
