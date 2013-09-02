package ro.minimul.romania1930.ai;

import ro.minimul.romania1930.data.Config;

public class AiContainer {
    private final AiThread aiThread;
    
    public AiContainer(Config config) {
        aiThread = new AiThread(config.aiTick);
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
