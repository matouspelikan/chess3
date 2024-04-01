package cvut.fel.pjv.pelikma3.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ChessServer {

    private static final Logger logger = Logger.getLogger(ChessServer.class.getName());

    private final int port;

    public ChessServer(int port){
        this.port = port;
    }

    public void start(){
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        List<Socket> clientSockets = new ArrayList<>();
        Map<Integer, Socket> clientSocketsD = new HashMap<>();

        List<ClientHandler> clientHandlers = new ArrayList<>();
        Object monitor = new Object();

        ClientLinker linker = new ClientLinker(monitor, clientHandlers);
        Thread linkerThread = new Thread(linker);
        linkerThread.start();

        int clientCounter = 0;
        try{
            serverSocket = new ServerSocket(port);
            logger.info("server initialized");

            logger.info("Server is listening oni port " + port + " ....");
            while(true){
                clientSocket = serverSocket.accept();

                logger.info("Client number" + clientCounter + " connected");

//                ClientHandler clientHandler = new ClientHandler(clientCounter, clientSocket, linker);
//                Thread clientHandlerThread = new Thread(clientHandler);
//                clientHandlerThread.start();
//                synchronized (monitor){
//                    clientHandlers.add(clientHandler);
//                }
//                clientCounter++;
            }
        }
        catch (IOException e){

        }
    }




}
