package ro.minimul.romania1930.web_logic;

import ro.minimul.romania1930.comm.Connection;
import ro.minimul.romania1930.comm.msg.AttackFailedMsg;
import ro.minimul.romania1930.comm.msg.AttackZoneMsg;
import ro.minimul.romania1930.comm.msg.PlayerTextMsg;
import ro.minimul.romania1930.comm.msg.ReplaceMsg;
import ro.minimul.romania1930.comm.msg.RoomInfoMsg;
import ro.minimul.romania1930.data.Zone;
import ro.minimul.romania1930.logic.Attack;
import ro.minimul.romania1930.logic.OwnedZone;
import ro.minimul.romania1930.logic.Player;
import ro.minimul.romania1930.logic.PlayerControls;
import ro.minimul.romania1930.logic.PlayerEvents;
import ro.minimul.romania1930.logic.QuestionAnswers;
import ro.minimul.romania1930.logic.RoomInfo;

public class WebPlayerEvents implements PlayerEvents {
    private final Connection connection;
    
    public WebPlayerEvents(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void onPersonalStart(Player self, PlayerControls controls,
            RoomInfo roomInfo) {
        connection.sendMessage(RoomInfoMsg.fromSelfAndRoomInfo(self, roomInfo));
    }

    @Override
    public void onForcedExit(String message) {
        connection.stop();
    }

    @Override
    public void onEnteredRoom(Player player) {
    }

    @Override
    public void onChangedName(Player player, String name) {
    }

    @Override
    public void onSaid(Player player, String text) {
        connection.sendMessage(new PlayerTextMsg(player.id, text));
    }
    
    @Override
    public void onAttackFailed(OwnedZone from, OwnedZone to) {
        connection.sendMessage(new AttackFailedMsg(from.zone.id, to.zone.id));
    }

    @Override
    public void onAttackZone(OwnedZone from, OwnedZone to) {
        connection.sendMessage(new AttackZoneMsg(from.zone.id, to.zone.id));
    }

    @Override
    public void onAttackQuestion(Attack question) {
    }

    @Override
    public void onDonatedZones(Player from, Player to, Zone[] zones) {
    }

    @Override
    public void onAnsweredQuestion(Attack question, int answer) {
    }

    @Override
    public void onQuestionDone(Attack question, QuestionAnswers answers) {
    }

    @Override
    public void onAttackDone() {
    }

    @Override
    public void onReplace(Player oldOne, Player newOne) {
        connection.sendMessage(new ReplaceMsg(oldOne.id, newOne.isHuman));
    }
}
