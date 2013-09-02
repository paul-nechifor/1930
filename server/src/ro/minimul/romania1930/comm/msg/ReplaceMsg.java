package ro.minimul.romania1930.comm.msg;

import ro.minimul.romania1930.comm.Message;

public class ReplaceMsg implements Message {
    // The id the players share.
    public int id;
    // If the replacing bot is a human.
    public boolean isHuman;
    
    public ReplaceMsg(int id, boolean isHuman) {
        this.id = id;
        this.isHuman = isHuman;
    }
}
