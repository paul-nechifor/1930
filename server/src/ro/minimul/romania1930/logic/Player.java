package ro.minimul.romania1930.logic;

import java.util.ArrayList;
import java.util.List;
import ro.minimul.romania1930.comm.msg.PlayerSubMsg;
import ro.minimul.romania1930.data.Zone;

public abstract class Player<T extends PlayerEvents> {
    public final int id;
    public final boolean isHuman;
    public final List<Zone> zones = new ArrayList<Zone>();
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
        
        for (int i = 0; i < ret.length; i++) {
            ret[i] = zones.get(i).id;
        }
        
        return ret;
    }
}
