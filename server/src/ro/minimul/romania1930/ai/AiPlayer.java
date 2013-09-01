package ro.minimul.romania1930.ai;

import ro.minimul.romania1930.logic.Player;

public class AiPlayer extends Player {   
    public AiPlayer(int id) {
        super(id, false, new AiPlayerEvents());
    }
}
