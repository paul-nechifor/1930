package ro.minimul.romania1930.ai;

public class AiContainer {
    private final AiThread aiThread;
    
    public AiContainer() {
        aiThread = new AiThread();
    }
    
    public void start() {
        aiThread.start();
    }
    
    public void stop() {
        aiThread.stopRunning();
    }
    
    public void addPlayer(AiPlayer player) {
        aiThread.addPlayer(player);
    }
    
    public void removePlayer(AiPlayer player) {
        aiThread.removePlayer(player);
    }
}
