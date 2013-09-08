package ro.minimul.romania1930.data;

import java.util.HashSet;
import java.util.Set;

public class Zone {
    public final int id;
    public final Map map;
    public final Zone[] neighbors;
    public final Set<Zone> neighborSet = new HashSet<Zone>();
    
    Zone(Map map, int id, int nNeighbors) {
        this.map = map;
        this.id = id;
        this.neighbors = new Zone[nNeighbors];
    }
    
    void fillNeighbors(int[] ids) {
        for (int i = 0; i < neighbors.length; i++) {
            neighbors[i] = map.zones[ids[i]];
            neighborSet.add(neighbors[i]);
        }
    }
}
