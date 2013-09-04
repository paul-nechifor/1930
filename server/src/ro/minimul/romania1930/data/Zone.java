package ro.minimul.romania1930.data;

import java.util.HashSet;
import java.util.Set;

public class Zone {
    public final int id;
    public final Map map;
    public final Zone[] neighbours;
    public final Set<Zone> neighborSet = new HashSet<Zone>();
    
    Zone(Map map, int id, int nNeighbours) {
        this.map = map;
        this.id = id;
        this.neighbours = new Zone[nNeighbours];
    }
    
    void fillNeighbours(int[] ids) {
        for (int i = 0; i < neighbours.length; i++) {
            neighbours[i] = map.zones[ids[i]];
            neighborSet.add(neighbours[i]);
        }
    }
}
