package ro.minimul.romania1930.logic;

import ro.minimul.romania1930.data.Zone;

public class OwnedZone {
    public final Zone zone;
    public OwnedZone isAttacking = null;
    public Player owner = null;
    
    // Only an attacked zone has this field.
    public Attack attack = null;
    
    public OwnedZone(Zone zone) {
        this.zone = zone;
    }
}
