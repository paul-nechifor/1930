package ro.minimul.romania1930.comm;

import ro.minimul.romania1930.comm.Acceptor.Listener;
import ro.minimul.romania1930.web.WebSocket;
import ro.minimul.romania1930.web.WebSocketServer;

class AcceptorThread extends Thread {
    private static final Listener CLOSING_LISTENER = new Listener() {
        @Override
        public void onAccept(Connection connection) {
            connection.stop();
        }
    };
    
    private final WebSocketServer webSocketServer;
    private final MessageMapping messageMapping;
    
    private Listener listener = CLOSING_LISTENER;
    
    private volatile boolean keepRunning = false;

    AcceptorThread(WebSocketServer webSocketServer,
            MessageMapping messageMapping) {
        this.webSocketServer = webSocketServer;
        this.messageMapping = messageMapping;
    }

    @Override
    public void run() {
        WebSocket webSocket;
        Connection connection;
        
        keepRunning = true;

        while (keepRunning) {
            try {
                webSocket = webSocketServer.accept();
                connection = new Connection(webSocket, messageMapping);
                connection.start();
                listener.onAccept(connection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void stopRunning() {
        keepRunning = false;
        interrupt();
    }
    
    void setListener(Listener listener) {
        if (listener == null) {
            this.listener = CLOSING_LISTENER;
        } else {
            this.listener = listener;
        }
    }
}