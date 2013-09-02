package ro.minimul.romania1930.web_logic;

import java.util.HashMap;
import java.util.Map;
import ro.minimul.romania1930.comm.Message;
import ro.minimul.romania1930.comm.msg.FromTextMsg;
import ro.minimul.romania1930.logic.PlayerControls;

/**
 * Handles translating from Message objects to PlayerControls calls.
 */
public class WebMessageRouter {
    private static interface Call<M extends Message> {
        public void call(PlayerControls controls, M message);
    }
    
    private final static Map<Class<? extends Message>, Call> MAP
            = new HashMap<Class<? extends Message>, Call>();
    
    static {
        MAP.put(FromTextMsg.class, new Call<FromTextMsg>() {
            @Override
            public void call(PlayerControls controls, FromTextMsg message) {
                controls.say(message.text);
            }
        });
    }
    
    public WebMessageRouter() {
    }

    public void route(PlayerControls controls, Message message) {
        MAP.get(message.getClass()).call(controls, message);
    }
}
