package ro.minimul.romania1930.logic;

import java.util.HashSet;
import java.util.Set;
import ro.minimul.romania1930.comm.msg.PlayerSubMsg;

public abstract class Player<T extends PlayerEvents> {
    public final int id;
    public final boolean isHuman;
    public final Set<OwnedZone> zones = new HashSet<OwnedZone>();
    public final T playerEvents;
    private String name;
    
    public Player(int id, boolean isHuman, T playerEvents) {
        this.id = id;
        this.isHuman = isHuman;
        this.playerEvents = playerEvents;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public PlayerSubMsg getSubMsg() {
        return new PlayerSubMsg(id, isHuman, name, getZoneIds());
    }
    
    private int[] getZoneIds() {
        int[] ret = new int[zones.size()];
        
        int i = 0;
        for (OwnedZone oz : zones) {
            ret[i] = oz.zone.id;
            i++;
        }
        
        return ret;
    }
    
    public Set<OwnedZone> getAttackableNeighbours() {
        final Set<OwnedZone> ret = new HashSet<OwnedZone>();
        
        for (OwnedZone my : zones) {
            skipZone:
            for (OwnedZone neigh : my.neighbours) {
                // Not my zones, or duplicate zones.
                if (zones.contains(neigh) || ret.contains(neigh)) {
                    continue skipZone;
                }
                
                // Not zones which are attacking others.
                if (neigh.isAttacking != null) {
                    continue skipZone;
                }
                
                // Not zones which I am already attacking.
                Attack a = neigh.attack;
                if (a != null) {
                    if (zones.contains(a.firstAggressor)) {
                        continue skipZone;
                    }
                    for (OwnedZone o : a.otherAggressors) {
                        if (zones.contains(o)) {
                            continue skipZone;
                        }
                    }
                }
                
                // It's a good for attacking.
                ret.add(neigh);
            }
        }
        
        return ret;
    }
}
