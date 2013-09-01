package ro.minimul.romania1930.comm;

import java.io.IOException;
import ro.minimul.romania1930.web.WebSocket;

public class WebPlayerConnection {
    private final WebSocket webSocket;
    private final MessageMapping messageMapping;
    private final ConnectionThread connectionThread;
    
    public WebPlayerConnection(WebSocket webSocket,
            MessageMapping messageMapping) {
        this.webSocket = webSocket;
        this.messageMapping = messageMapping;
        this.connectionThread = new ConnectionThread(webSocket, messageMapping);
    }

    /**
     * Starts the thread and socket associated with this connection.
     */
    void start() {
        connectionThread.start();
    }

    /**
     * Shuts down the thread and socket associated with this connection.
     */
    public void stop() {
        connectionThread.stopRunning();
    }

    public void sendMessage(Message message) {
        try {
            messageMapping.write(webSocket, message);
        } catch (IOException ex) {
            stop();
        }
    }
    
    public void setListener(ConnectionThread.Listener listener) {
        connectionThread.setListener(listener);
    }
}
