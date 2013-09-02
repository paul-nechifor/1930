package ro.minimul.romania1930.ai;

import ro.minimul.romania1930.logic.Player;

public class AiPlayer extends Player<AiPlayerEvents> {   
    public AiPlayer(int id) {
        super(id, false, new AiPlayerEvents());
    }
    
    public void tick(double time) {
    }
}
