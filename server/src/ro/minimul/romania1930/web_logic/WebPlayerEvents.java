package ro.minimul.romania1930.web_logic;

import ro.minimul.romania1930.comm.WebPlayerConnection;
import ro.minimul.romania1930.comm.msg.PlayerTextMsg;
import ro.minimul.romania1930.comm.msg.RoomInfoMsg;
import ro.minimul.romania1930.data.Zone;
import ro.minimul.romania1930.logic.AttackQuestion;
import ro.minimul.romania1930.logic.Player;
import ro.minimul.romania1930.logic.PlayerControls;
import ro.minimul.romania1930.logic.PlayerEvents;
import ro.minimul.romania1930.logic.QuestionAnswers;
import ro.minimul.romania1930.logic.RoomInfo;

public class WebPlayerEvents implements PlayerEvents {
    private final WebPlayerConnection connection;
    
    public WebPlayerEvents(WebPlayerConnection connection) {
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
    public void onExitedRoom(Player player, boolean error) {
    }

    @Override
    public void onChangedName(Player player, String name) {
    }

    @Override
    public void onSaid(Player player, String text) {
        connection.sendMessage(new PlayerTextMsg(player.id, text));
    }

    @Override
    public void onAttackZone(Player player, Zone zone) {
    }

    @Override
    public void onAttackQuestion(AttackQuestion question) {
    }

    @Override
    public void onDonatedZones(Player from, Player to, Zone[] zones) {
    }

    @Override
    public void onAnsweredQuestion(AttackQuestion question, int answer) {
    }

    @Override
    public void onQuestionDone(AttackQuestion question, QuestionAnswers answers) {
    }

    @Override
    public void onAttackDone() {
    }

    @Override
    public void onReplace(Player oldOne, Player newOne) {
    }
}
