package ro.minimul.romania1930.comm.msg;

import ro.minimul.romania1930.comm.Message;

public class AttackFailedMsg implements Message {
    public int from;
    public int to;
    
    public AttackFailedMsg(int from, int to) {
        this.from = from;
        this.to = to;
    }
}