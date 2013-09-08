package ro.minimul.romania1930.logic;

import ro.minimul.romania1930.data.Zone;

public class OwnedZone {
    public final Zone zone;
    public final OwnedZone[] neighbors;
    public OwnedZone isAttacking = null;
    public Player owner = null;
    
    // Only an attacked zone has this field.
    public Attack attack = null;
    
    OwnedZone(Zone zone) {
        this.zone = zone;
        this.neighbors = new OwnedZone[this.zone.neighbors.length];
    }
    
    void setYourNeighbors(OwnedZone zones[]) {
        for (int i = 0; i < zone.neighbors.length; i++) {
            neighbors[i] = zones[zone.neighbors[i].id];
        }
    }
}
