package ro.minimul.romania1930.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;

/**
 * JSON utility methods.
 * @author Paul Nechifor
 */
public class Json {
    private static final Gson GSON;

    static {
        GSON = new GsonBuilder().create();
    }
    
    private Json() {
    }
    
    public static <E> E fromString(String message, Class<E> type)
            throws IOException {
        return GSON.fromJson(message, type);
    }
    
    public static <E> E fromFilePath(File file, Class<E> type)
            throws IOException {
        return fromString(Util.readEntireFile(file), type);
    }
    
    public static String toString(Object object)
            throws IOException {
        return GSON.toJson(object);
    }
}