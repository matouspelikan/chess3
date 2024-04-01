package cvut.fel.pjv.pelikma3.server;

import org.apache.maven.settings.Server;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

public class ClientHandler implements Runnable{

    private final static Logger logger = Logger.getLogger(ClientHandler.class.getName());

    private final Socket socket;
    private final int id;
    private final ClientLinker linker;
    private boolean shutdown = false;
    Sender sender;
    ServerGameHandler serverGameHandler = null;

    public Socket getSocket() {
        return socket;
    }

    public int getId() {
        return id;
    }

    public ClientHandler(int id, Socket socket, ClientLinker linker){
        this.id = id;
        this.socket = socket;
        this.linker = linker;
    }

    public void shutdown(){
        shutdown = true;
    }

    public void sendMessageToClient(String message){
        this.sender.sendMessage(message);
    }

    public void setServerGameHandler(ServerGameHandler serverGameHandler){
        this.serverGameHandler = serverGameHandler;
    }


    @Override
    public void run() {
        sender = new Sender(this.socket);
        new Thread(sender).start();
        try (InputStream inputStream = socket.getInputStream();
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader);)
            {
            String line;
                while((line = bufferedReader.readLine()) != null && shutdown == false){
                    logger.info("message received: " + line);
                    if(line.trim().equals("create")){
                        String code = linker.createNewRoom(this.id);
                        logger.info("new code " + code);
                        sendMessageToClient("New room created, share code: " + code);
                    }
                    else if(line.length() > 5 && line.charAt(4) == ' ' && line.substring(0, 4).equals("join")){
                        String code = line.substring(5, line.length());
                        logger.info("join request to: " + code);
                        if(!linker.codeToIdMap.containsKey(code)){
                            logger.warning("Room " + code + " not found");
                            sendMessageToClient("Room " + code + " not found");
                        }
                        else{
                            ClientHandler otherClient = linker.findHandler(linker.codeToIdMap.get(code));
                            if(otherClient != null){ //SUCCESS
                                sendMessageToClient("Successfully connected to room : " + code);
                                logger.warning("other client: " + String.valueOf(otherClient.id));
                                this.serverGameHandler = new ServerGameHandler(this, otherClient);
                                otherClient.setServerGameHandler(this.serverGameHandler);
                                new Thread(serverGameHandler).start();
                            }
                            else{
                                logger.warning("Room " + code + " not found");
//                                printWriter.println("Room " + code + " not found");
//                                printWriter.flush();
                                sendMessageToClient("Room " + code + " not found");
                            }
                        }
                    }
                    else{
                        if(serverGameHandler != null){
                            serverGameHandler.sendMessageToOtherClient(line, this.id);
                        }
                    }


                }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
