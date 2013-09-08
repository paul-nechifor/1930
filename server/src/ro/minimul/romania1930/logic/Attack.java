package ro.minimul.romania1930.logic;

import java.util.HashSet;
import java.util.Set;
import ro.minimul.romania1930.comm.msg.AttackSubMsg;

public class Attack {
    public final OwnedZone victim;
    public final OwnedZone firstAggressor;
    public final Set<OwnedZone> otherAggressors = new HashSet<OwnedZone>();
    public final int secondsToAnswer;
    public final long deadline;
    
    public Attack(OwnedZone victim, OwnedZone firstAggressor,
            int secondsToAnswer) {
        this.victim = victim;
        this.firstAggressor = firstAggressor;
        this.secondsToAnswer = secondsToAnswer;
        this.deadline = System.nanoTime() + secondsToAnswer * 1000000000;
    }
    
    public AttackSubMsg getSubMsg() {
        AttackSubMsg ret = new AttackSubMsg();
        
        ret.froms = new int[otherAggressors.size() + 1];
        ret.froms[0] = firstAggressor.zone.id;
        int i = 1;
        for (OwnedZone z : otherAggressors) {
            ret.froms[i] = z.zone.id;
            i++;
        }
        
        ret.to = victim.zone.id;
        
        ret.ms = (int) ((deadline - System.nanoTime()) / 1000000);
        
        return ret;
    }
}
