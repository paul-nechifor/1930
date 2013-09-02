package ro.minimul.romania1930.web_logic;

import ro.minimul.romania1930.comm.Connection;
import ro.minimul.romania1930.logic.Player;

public class WebPlayer extends Player<WebPlayerEvents> {
    public WebPlayer(int id, Connection connection) {
        super(id, true, new WebPlayerEvents(connection));
    }
}
