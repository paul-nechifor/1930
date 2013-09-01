package ro.minimul.romania1930.comm;

import java.io.IOException;
import ro.minimul.romania1930.data.Config;
import ro.minimul.romania1930.web.WebSocketServer;
import ro.minimul.romania1930.web_logic.MessageRouter;

public class ConnectionManager {
    private final AcceptorThread acceptorThread;
    
    public final MessageRouter messageRouter = new MessageRouter();
    
    public ConnectionManager(Config config)
            throws IOException, ClassNotFoundException {
        this.acceptorThread = new AcceptorThread(
                new WebSocketServer(config.webSocketPort),
                new MessageMapping(config));
    }
    
    public void setOnAcceptListener(AcceptorThread.Listener listener) {
        acceptorThread.setListener(listener);
    }
    
    public void start() {
        acceptorThread.start();
    }
    
    public void stop() {
        acceptorThread.stopRunning();
    }
}