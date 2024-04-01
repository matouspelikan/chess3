package cvut.fel.pjv.pelikma3.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class Sender implements Runnable{

    Socket socket;
    Queue<String> sendQueue = new LinkedList<>();
    boolean keepSending = true;

    public Sender(Socket socket){
        this.socket = socket;
    }

    public void sendMessage(String message){
        synchronized (sendQueue){
            sendQueue.add(message);
            sendQueue.notify();
        }
    }

    public void stopSending(){
        synchronized (sendQueue){
            this.keepSending = false;
            sendQueue.notify();
        }
    }

    @Override
    public void run() {
        try(OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);
        ) {
            synchronized (sendQueue){
                while(keepSending){
                    while(sendQueue.isEmpty()){
                        try {
                            sendQueue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    String line = sendQueue.poll();
                    printWriter.println(line);
                    printWriter.flush();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
