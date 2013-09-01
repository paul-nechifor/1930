package ro.minimul.romania1930.logic;

import java.util.ArrayList;
import java.util.List;
import ro.minimul.romania1930.data.Config;
import ro.minimul.romania1930.data.Map;

public class RoomInfo {
    public final List<Player> players = new ArrayList<Player>();
    public final boolean[] idUsed;
    public final Map map;
    public final Player[] zoneOwners;
    
    RoomInfo(Map map, Config config) {
        this.map = map;
        idUsed = new boolean[config.maximumPlayers];
        this.zoneOwners = new Player[this.map.zones.length];
    }
}
