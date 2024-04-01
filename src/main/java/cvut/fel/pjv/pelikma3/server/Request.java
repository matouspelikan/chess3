package cvut.fel.pjv.pelikma3.server;

public class Request {
    private String code;
    private ClientHandler clientHandler;

    public String getCode() {
        return code;
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    public Request(String code, ClientHandler clientHandler){
        this.code = code;
        this.clientHandler = clientHandler;
    }

}
