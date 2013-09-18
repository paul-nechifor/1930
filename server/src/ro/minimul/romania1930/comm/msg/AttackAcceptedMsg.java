package ro.minimul.romania1930.comm.msg;

import ro.minimul.romania1930.comm.Message;

public class AttackAcceptedMsg implements Message {
    public long id;
    public int from;
    public int to;
    public int s;
    
    public AttackAcceptedMsg(long id, int from, int to, int secondsToAnswer) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.s = secondsToAnswer;
    }
}
