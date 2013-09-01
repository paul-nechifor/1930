package ro.minimul.romania1930.ai;

import ro.minimul.romania1930.data.Zone;
import ro.minimul.romania1930.logic.AttackQuestion;
import ro.minimul.romania1930.logic.Player;
import ro.minimul.romania1930.logic.PlayerControls;
import ro.minimul.romania1930.logic.PlayerEvents;
import ro.minimul.romania1930.logic.QuestionAnswers;
import ro.minimul.romania1930.logic.RoomInfo;

public class AiPlayerEvents implements PlayerEvents {
    private Player self;
    private PlayerControls controls;
    private RoomInfo roomInfo;

    @Override
    public void onPersonalStart(Player self, PlayerControls controls,
            RoomInfo roomInfo) {
        this.self = self;
        this.controls = controls;
        this.roomInfo = roomInfo;
    }

    @Override
    public void onForcedExit(String reason) {
        // Nothing to do.
    }

    @Override
    public void onEnteredRoom(Player player) {
        // Not interested.
    }

    @Override
    public void onExitedRoom(Player player, boolean error) {
        // Not interested.
    }

    @Override
    public void onChangedName(Player player, String name) {
        // Not interested.
    }

    @Override
    public void onSaid(Player player, String text) {
        // Not interested.
    }

    @Override
    public void onAttackZone(Player player, Zone zone) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onAttackQuestion(AttackQuestion question) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onDonatedZones(Player from, Player to, Zone[] zones) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onAnsweredQuestion(AttackQuestion question, int answer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onQuestionDone(AttackQuestion question, QuestionAnswers answers) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onAttackDone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onReplace(Player oldOne, Player newOne) {
        // Not interested.
    }
}