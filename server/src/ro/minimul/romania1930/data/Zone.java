package ro.minimul.romania1930.data;

public class Zone {
    public final int id;
    public final Map map;
    public final Zone[] neighbours;
    
    Zone(Map map, int id, int nNeighbours) {
        this.map = map;
        this.id = id;
        this.neighbours = new Zone[nNeighbours];
    }
    
    void fillNeighbours(int[] ids) {
        for (int i = 0; i < neighbours.length; i++) {
            neighbours[i] = map.zones[ids[i]];
        }
    }
}
