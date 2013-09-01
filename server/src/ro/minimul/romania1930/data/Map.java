package ro.minimul.romania1930.data;

import java.io.File;
import java.io.IOException;
import ro.minimul.romania1930.util.Json;
import ro.minimul.romania1930.util.Util;

public class Map {
    public final Zone[] zones;
    
    private Map(Zone[] zones) {
        this.zones = zones;
    }
    
    public static Map load(Config config) throws IOException {
        File file = Util.getJarRelative(config.mapFile);
        MapJson mapJson = Json.fromFilePath(file, MapJson.class);
        
        Zone[] zones = new Zone[mapJson.zones.length];
        Map map = new Map(zones);
        
        for (int i = 0; i < zones.length; i++) {
            zones[i] = new Zone(map, i, mapJson.zones[i].neighs.length);
        }
        
        for (int i = 0; i < zones.length; i++) {
            zones[i].fillNeighbours(mapJson.zones[i].neighs);
        }
        
        return map;
    }
}

class MapJson {
    ZoneJson[] zones;
}

class ZoneJson {
    int[] neighs;
}