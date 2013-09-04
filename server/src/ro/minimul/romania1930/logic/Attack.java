package ro.minimul.romania1930.logic;

import java.util.HashSet;
import java.util.Set;

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
}
