package cvut.fel.pjv.pelikma3.server;

import java.net.Socket;
import java.util.*;

public class ClientLinker implements Runnable{

    private Random random = new Random();
    private List<ClientHandler> clientHandlers;
    private final Object monitor;
    Map<String, Integer> codeToIdMap = new HashMap<>();
    Queue<Request> requests = new LinkedList<>();


    public ClientLinker(Object monitor, List<ClientHandler> clientHandlers){
        this.monitor = monitor;
        this.clientHandlers = clientHandlers;
    }

    public String createNewRoom(int clientId){
        String code = this.createNewCode();
        synchronized (monitor){
            codeToIdMap.put(code, clientId);
        }
        return code;
    }

    public String createNewCode(){
        String code = this.randomCode();
        synchronized (monitor) {
            while (codeToIdMap.containsKey(code)) {
                code = this.randomCode();
            }
        }
        return code;
    }

    private String randomCode(){
        String code = "";
        for(int i = 0; i< 4; i++){
            code += (char)('a' + random.nextInt('z'-'a'+1));
        }
        return code;
    }

    public void addInvitationCode(String code, int handlerId){
        synchronized (monitor) {
            codeToIdMap.put(code, handlerId);
        }
    }

    public ClientHandler findHandler(int handlerId){
        synchronized (monitor){
            for (ClientHandler handler : this.clientHandlers) {
                if(handler.getId() == handlerId){
                    return handler;
                }
            }
        }
        return null;
    }

    @Override
    public void run() {

    }
}
