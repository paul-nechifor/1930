package ro.minimul.romania1930.logic;

import java.util.HashSet;
import java.util.Set;
import ro.minimul.romania1930.comm.msg.AttackSubMsg;
import ro.minimul.romania1930.data.FaQuestion;
import ro.minimul.romania1930.data.NaQuestion;

public class Attack {
    public static int NEW_ID = 1;
    
    public final long id;
    public final OwnedZone victim;
    public final OwnedZone firstAggressor;
    public final Set<OwnedZone> otherAggressors = new HashSet<OwnedZone>();
    public final int secondsToAnswer;
    public final long deadline;
    public final FaQuestion faQuestion;
    public final NaQuestion naQuestion;
    
    public Attack(OwnedZone victim, OwnedZone firstAggressor,
            int secondsToAnswer, FaQuestion faQuestion, NaQuestion naQuestion) {
        this.id = NEW_ID;
        this.victim = victim;
        this.firstAggressor = firstAggressor;
        this.secondsToAnswer = secondsToAnswer;
        this.deadline = System.nanoTime() + secondsToAnswer * 1000000000;
        this.faQuestion = faQuestion;
        this.naQuestion = naQuestion;
        
        NEW_ID++;
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
