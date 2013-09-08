package ro.minimul.romania1930.logic;

import java.util.ArrayList;
import java.util.List;
import ro.minimul.romania1930.data.Config;
import ro.minimul.romania1930.data.Map;

public class RoomInfo {
    public final List<Player> players = new ArrayList<Player>();
    public final boolean[] idUsed;
    public final Map map;
    public final OwnedZone[] zones;
    public final List<Attack> attacks = new ArrayList<Attack>();
    
    RoomInfo(Map map, Config config) {
        this.map = map;
        idUsed = new boolean[config.maximumPlayers];
        
        zones = new OwnedZone[this.map.zones.length];
        for (int i = 0; i < zones.length; i++) {
            zones[i] = new OwnedZone(this.map.zones[i]);
        }
        
        for (int i = 0; i < zones.length; i++) {
            zones[i].setYourNeighbours(zones);
        }
    }
}
