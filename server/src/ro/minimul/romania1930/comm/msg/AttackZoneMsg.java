package ro.minimul.romania1930.comm.msg;

import ro.minimul.romania1930.comm.Message;

public class AttackZoneMsg implements Message {
    public int from;
    public int to;
    
    public AttackZoneMsg(int from, int to) {
        this.from = from;
        this.to = to;
    }
}
