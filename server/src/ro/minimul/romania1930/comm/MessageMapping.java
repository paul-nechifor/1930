package ro.minimul.romania1930.comm;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import ro.minimul.romania1930.data.Config;
import ro.minimul.romania1930.util.Json;
import ro.minimul.romania1930.web.WebSocket;

public class MessageMapping {
    private static final String MSG_PACKAGE =
            MessageMapping.class.getPackage().getName() + ".msg.";
    
    private final Map<Class<? extends Message>, Byte> classToCode
            = new HashMap<Class<? extends Message>, Byte>();
    private final Class<? extends Message>[] codeToClass 
            = (Class<? extends Message>[]) new Class<?>[256];
    
    public MessageMapping(Config config) throws ClassNotFoundException {
        String name;
        Class<? extends Message> class_;
        for (byte code = 0; code < config.messageCodes.length; code++) {
            name = config.messageCodes[code];
            class_ = (Class<? extends Message>)
                    Class.forName(MSG_PACKAGE + name);
            
            classToCode.put(class_, code);
            codeToClass[code] = class_;
        }
    }

    /**
     * 
     * @param webSocket
     * @return a Message or null on error or finish.
     */
    public Message read(WebSocket webSocket) throws IOException {
        byte[] message = webSocket.read();
        if (message == null || message.length == 0) {
            return null;
        }
        
        byte type = message[0];
        String jsonMesg = new String(Arrays.copyOfRange(message, 1,
                message.length));
        return Json.fromString(jsonMesg, codeToClass[type]);
    }
    
    public void write(WebSocket webSocket, Message message) throws IOException {
        byte[] jsonBytes = Json.toString(message).getBytes();
        byte[] mesg = new byte[jsonBytes.length + 1];
        
        mesg[0] = classToCode.get(message.getClass());
        System.arraycopy(jsonBytes, 0, mesg, 1, jsonBytes.length);
        
        webSocket.write(mesg);
    }
}
