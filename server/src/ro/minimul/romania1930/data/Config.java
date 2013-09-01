package ro.minimul.romania1930.data;

import java.io.File;
import java.io.IOException;
import ro.minimul.romania1930.util.Json;
import ro.minimul.romania1930.util.Util;

public class Config {
    public int webSocketPort;
    public int maximumPlayers;
    public int maxTextMessageSize;
    public String faFile;
    public String naFile;
    public String mapFile;
    public String[] messageCodes;
    
    private Config() {
    }
    
    public static Config load(String relativePath) throws IOException {
        File file = Util.getJarRelative(relativePath);
        return Json.fromFilePath(file, Config.class);
    }
}
