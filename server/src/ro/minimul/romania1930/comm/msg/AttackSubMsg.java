package ro.minimul.romania1930.comm.msg;

public class AttackSubMsg {
    // All the ids of the attackers, the first one being the firstAggressor.
    public int[] froms;
    
    // The victim.
    public int to;
    
    // Milliseconds left to answer according to the time at which it was send.
    public int ms;
}
