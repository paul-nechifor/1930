package ro.minimul.romania1930.web;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebSocketServer {
    private ServerSocket serverSocket;
    
    public WebSocketServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }
    
    public WebSocket accept() throws IOException {
        Socket socket = serverSocket.accept();
        WebSocket webSocket = new WebSocket(socket);
        webSocket.connect();
        return webSocket;
    }
}