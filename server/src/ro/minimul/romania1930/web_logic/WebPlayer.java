package ro.minimul.romania1930.web_logic;

import ro.minimul.romania1930.comm.WebPlayerConnection;
import ro.minimul.romania1930.logic.Player;

public class WebPlayer extends Player {
    public WebPlayer(int id, WebPlayerConnection connection) {
        super(id, true, new WebPlayerEvents(connection));
    }
}
