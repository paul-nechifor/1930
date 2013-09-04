package ro.minimul.romania1930.comm.msg;

import ro.minimul.romania1930.comm.Message;

public class AttackAcceptedMsg implements Message {
    public int from;
    public int to;
    public int s;
    
    public AttackAcceptedMsg(int from, int to, int secondsToAnswer) {
        this.from = from;
        this.to = to;
        this.s = secondsToAnswer;
    }
}
