package cvut.fel.pjv.pelikma3.server;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

public class Client {
    private final static Logger logger = Logger.getLogger(Client.class.getName());

    private final String url;
    private final int port;
    Sender sender;
    Queue<String> receivedMessages = new LinkedList<>();

    public Client(String url, int port){
        this.url = url;
        this.port = port;

    }

    public String getMessage(){
        String message = null;
        synchronized (receivedMessages){
            if(receivedMessages.stream().count() > 0){
                message = receivedMessages.poll();
            }
        }
        return message;
    }

    public void sendMessage(String message){
        sender.sendMessage(message);
    }

    public void startListening(Socket socket){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try(InputStream inputStream = socket.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    DataInputStream dataInputStream = new DataInputStream(inputStream);
                ) {
                    String line = null;
                    while((line = bufferedReader.readLine())!= null) {
                        System.out.println("received message from server: " + line);
                        synchronized (receivedMessages){
                            receivedMessages.add(line);
                        }
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void connect(){
        Socket socket = null;

        try {
            socket = new Socket(url, port);
            logger.info("client connected");
            this.startListening(socket);
            sender = new Sender(socket);
            new Thread(sender).start();

//            try(InputStreamReader inputStreamReaderConsole = new InputStreamReader(System.in);
//                BufferedReader bufferedReaderConsole = new BufferedReader(inputStreamReaderConsole);
//                ){
//
//                String line;
//                while((line = bufferedReaderConsole.readLine()) != null){
//                    logger.info("sending message: " + line + " to server");
//                    sender.sendMessage(line);
//                }
//
//            }
//            catch (Exception e){
//
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("client disconnecting");
    }
}
