package cvut.fel.pjv.pelikma3.server;

import java.io.*;
import java.net.Socket;

public class ServerGameHandler implements Runnable{

    private final ClientHandler clientHandler1;
    private final ClientHandler clientHandler2;

    public ServerGameHandler(ClientHandler clientHandler1, ClientHandler clientHandler2){
        this.clientHandler1 = clientHandler1;
        this.clientHandler2 = clientHandler2;
    }

    public void sendMessageToOtherClient(String message, int id){
        if(clientHandler2.getId() != id){
            clientHandler2.sendMessageToClient(message);
        }
        else if(clientHandler1.getId() != id){
            clientHandler1.sendMessageToClient(message);
        }
    }

    @Override
    public void run() {

    }
}
