package ro.minimul.romania1930.comm.msg;

import java.util.List;
import ro.minimul.romania1930.comm.Message;
import ro.minimul.romania1930.logic.Player;
import ro.minimul.romania1930.logic.RoomInfo;

public class RoomInfoMsg implements Message {
    public final PlayerSubMsg self;
    public final PlayerSubMsg[] players;
    public final boolean isFull;
    
    public RoomInfoMsg(PlayerSubMsg self, PlayerSubMsg[] players,
            boolean isFull) {
        this.self = self;
        this.players = players;
        this.isFull = isFull;
    }
    
    public static RoomInfoMsg fromFull() {
        return new RoomInfoMsg(null, null, true);
    }
    
    public static RoomInfoMsg fromSelfAndRoomInfo(Player self,
            RoomInfo roomInfo) {
        return new RoomInfoMsg(self.getSubMsg(), getPlayers(roomInfo), false);
    }
    
    private static PlayerSubMsg[] getPlayers(RoomInfo roomInfo) {
        List<Player> players = roomInfo.players;
        PlayerSubMsg[] ret = new PlayerSubMsg[players.size()];
        
        for (int i = 0; i < ret.length; i++) {
            ret[i] = players.get(i).getSubMsg();
        }
        
        return ret;
    }
}
