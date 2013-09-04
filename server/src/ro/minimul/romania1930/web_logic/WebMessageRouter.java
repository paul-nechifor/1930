package ro.minimul.romania1930.web_logic;

import java.util.HashMap;
import java.util.Map;
import ro.minimul.romania1930.comm.Message;
import ro.minimul.romania1930.comm.msg.AttackZoneMsg;
import ro.minimul.romania1930.comm.msg.FromTextMsg;
import ro.minimul.romania1930.logic.OwnedZone;
import ro.minimul.romania1930.logic.PlayerControls;
import ro.minimul.romania1930.logic.Room;

/**
 * Handles translating from Message objects to PlayerControls calls.
 */
public class WebMessageRouter {
    private static interface Call<M extends Message> {
        public void call(PlayerControls controls, M message);
    }
    
    private final Map<Class<? extends Message>, Call> map
            = new HashMap<Class<? extends Message>, Call>();
    
    private final Room room;
    private final OwnedZone[] zones;
    
    {
        map.put(FromTextMsg.class, new Call<FromTextMsg>() {
            @Override
            public void call(PlayerControls controls, FromTextMsg message) {
                controls.say(message.text);
            }
        });
        map.put(AttackZoneMsg.class, new Call<AttackZoneMsg>() {
            @Override
            public void call(PlayerControls controls, AttackZoneMsg message) {
                controls.attackZone(zones[message.from], zones[message.to]);
            }
        });
    }
    
    public WebMessageRouter(Room room) {
        this.room = room;
        this.zones = room.getRoomInfo().zones;
    }

    public void route(PlayerControls controls, Message message) {
        map.get(message.getClass()).call(controls, message);
    }
}
