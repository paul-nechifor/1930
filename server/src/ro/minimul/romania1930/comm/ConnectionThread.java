package ro.minimul.romania1930.comm;

import java.io.IOException;
import ro.minimul.romania1930.comm.Connection.Listener;
import ro.minimul.romania1930.web.WebSocket;

class ConnectionThread extends Thread {
    private static final Listener DEFAULT_LISTENER = new Listener() {
        @Override
        public void onMessage(Message message) {
        }

        @Override
        public void onEnd(boolean error) {
        }
    };
    
    private final WebSocket webSocket;
    private final MessageMapping messageMapping;
    
    private Listener listener = DEFAULT_LISTENER;

    private volatile boolean keepRunning = false;

    ConnectionThread(WebSocket webSocket,
            MessageMapping messageMapping) {
        this.webSocket = webSocket;
        this.messageMapping = messageMapping;
    }

    @Override
    public void run() {
        keepRunning = true;
        Message message;

        try {
            while (keepRunning) {
                message = messageMapping.read(webSocket);
                if (message == null) {
                    onEnd(true);
                    return;
                } else {
                    listener.onMessage(message);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            onEnd(true);
            return;
        }

        onEnd(false);
    }

    void stopRunning() {
        keepRunning = false;
        interrupt();
    }
    
    void setListener(Listener listener) {
        if (listener == null) {
            this.listener = DEFAULT_LISTENER;
        } else {
            this.listener = listener;
        }
    }

    private void onEnd(boolean error) {
        keepRunning = false;
        try {
            webSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        listener.onEnd(error);
    }
}
