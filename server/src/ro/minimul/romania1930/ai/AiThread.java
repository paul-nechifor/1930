package ro.minimul.romania1930.ai;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

class AiThread extends Thread {
    private final Set<AiPlayer> aiPlayers = new HashSet<AiPlayer>();
    private final int aiTick;
    private volatile boolean keepRunning = false;

    public AiThread(int aiTick) {
        this.aiTick = aiTick;
    }
    
    @Override 
    public void run() {
        keepRunning = true;
        long lastTime = System.nanoTime();
        long now, passed;
        Iterator<AiPlayer> iterator;
        
        while (keepRunning) {
            try {
                Thread.sleep(aiTick);
            } catch (InterruptedException ex) {
            }
            
            now = System.nanoTime();
            passed = now - lastTime;
            lastTime = now;
            
            synchronized (this) {
                // Using the iterator because elements might be removed.
                iterator = aiPlayers.iterator();
                while (iterator.hasNext()) {
                    AiPlayer aiPlayer = iterator.next();
                    try {
                        aiPlayer.playerEvents.tick(passed);
                    } catch (Exception ex) {
                        if (keepRunning) {
                            ex.printStackTrace();
                            ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
                            // TODO: This doesn't work right. If the player
                            // is kicked the iterator will have a concurent XXX
                            aiPlayer.playerEvents.codeFailure();
                        } else {
                            return;
                        }
                    }
                }
            }
        }
    }

    void stopRunning() {
        keepRunning = false;
        interrupt();
    }
    
    public synchronized void addPlayer(AiPlayer player) {
        aiPlayers.add(player);
    }
    
    public synchronized void removePlayer(AiPlayer player) {
        aiPlayers.remove(player);
    }
}
