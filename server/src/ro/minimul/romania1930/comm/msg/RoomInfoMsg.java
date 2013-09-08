package ro.minimul.romania1930.comm.msg;

import java.util.List;
import ro.minimul.romania1930.comm.Message;
import ro.minimul.romania1930.logic.Attack;
import ro.minimul.romania1930.logic.Player;
import ro.minimul.romania1930.logic.RoomInfo;

public class RoomInfoMsg implements Message {
    public final PlayerSubMsg self;
    public final PlayerSubMsg[] players;
    public final boolean isFull;
    public final AttackSubMsg[] attacks;
    
    public RoomInfoMsg(PlayerSubMsg self, PlayerSubMsg[] players,
            boolean isFull, AttackSubMsg[] attacks) {
        this.self = self;
        this.players = players;
        this.isFull = isFull;
        this.attacks = attacks;
    }
    
    public static RoomInfoMsg fromFull() {
        return new RoomInfoMsg(null, null, true, null);
    }
    
    public static RoomInfoMsg fromSelfAndRoomInfo(Player self,
            RoomInfo roomInfo) {
        return new RoomInfoMsg(self.getSubMsg(), getPlayers(roomInfo), false,
                getAttacks(roomInfo));
    }
    
    private static PlayerSubMsg[] getPlayers(RoomInfo roomInfo) {
        List<Player> players = roomInfo.players;
        PlayerSubMsg[] ret = new PlayerSubMsg[players.size()];
        
        for (int i = 0; i < ret.length; i++) {
            ret[i] = players.get(i).getSubMsg();
        }
        
        return ret;
    }
    
    private static AttackSubMsg[] getAttacks(RoomInfo roomInfo) {
        List<Attack> attacks = roomInfo.attacks;
        AttackSubMsg[] ret = new AttackSubMsg[attacks.size()];
        
        for (int i = 0; i < ret.length; i++) {
            ret[i] = attacks.get(i).getSubMsg();
        }
        
        return ret;
    }
}
