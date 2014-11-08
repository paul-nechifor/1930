package ro.minimul.romania1930.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

public class Util {
    public static final File JAR_LOCATION =
            new File(Util.class.getProtectionDomain()
            .getCodeSource().getLocation().getPath()).getParentFile();
    
    public static final long NANO = 1000000000;
    
    private Util() {
    }
    
    public static String readEntireFile(File file) throws IOException {
        FileReader in = new FileReader(file);
        StringBuilder contents = new StringBuilder();
        char[] buffer = new char[4096];
        int read = 0;
        
        do {
            contents.append(buffer, 0, read);
            read = in.read(buffer);
        } while (read >= 0);
        
        return contents.toString();
    }
    
    public static File getJarRelative(String path) {
        return new File(JAR_LOCATION, path);
    }
    
    public static <E> E chooseRandom(Set<E> set) {
        int picked = (int) (set.size() * Math.random());
        int i = 0;
        
        for (E e : set) {
            if (i == picked) {
                return e;
            }
            i++;
        }
        
        return null;
    }
}
