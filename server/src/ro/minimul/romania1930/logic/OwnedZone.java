package ro.minimul.romania1930.logic;

import ro.minimul.romania1930.data.Zone;

public class OwnedZone {
    public final Zone zone;
    public final OwnedZone[] neighbours;
    public OwnedZone isAttacking = null;
    public Player owner = null;
    
    // Only an attacked zone has this field.
    public Attack attack = null;
    
    OwnedZone(Zone zone) {
        this.zone = zone;
        this.neighbours = new OwnedZone[this.zone.neighbours.length];
    }
    
    void setYourNeighbours(OwnedZone zones[]) {
        for (int i = 0; i < zone.neighbours.length; i++) {
            neighbours[i] = zones[zone.neighbours[i].id];
        }
    }
}
