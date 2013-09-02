package ro.minimul.romania1930.ai;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

class AiThread extends Thread {
    private final Set<AiPlayer> aiPlayers = new HashSet<AiPlayer>();
    
    private volatile boolean keepRunning = false;
    
    @Override 
    public void run() {
        keepRunning = true;
        
        while (keepRunning) {
            synchronized (this) {
                // Using the iterator because elements might be removed.
                Iterator<AiPlayer> iterator = aiPlayers.iterator();
                double time = System.nanoTime() / 1E9;
                while (iterator.hasNext()) {
                    AiPlayer aiPlayer = iterator.next();
                    try {
                        aiPlayer.tick(time);
                    } catch (Exception ex) {
                        if (keepRunning) {
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
