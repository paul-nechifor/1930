package ro.minimul.romania1930.ai;

import ro.minimul.romania1930.data.Zone;
import ro.minimul.romania1930.logic.Attack;
import ro.minimul.romania1930.logic.OwnedZone;
import ro.minimul.romania1930.logic.Player;
import ro.minimul.romania1930.logic.PlayerControls;
import ro.minimul.romania1930.logic.PlayerEvents;
import ro.minimul.romania1930.logic.QuestionAnswers;
import ro.minimul.romania1930.logic.RoomInfo;

class AiPlayerEvents implements PlayerEvents {
    private Player self;
    private PlayerControls controls;
    private RoomInfo roomInfo;
    
    public void codeFailure() {
        controls.exitRoom(true);
    }

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
    public void onChangedName(Player player, String name) {
        // Not interested.
    }

    @Override
    public void onSaid(Player player, String text) {
        // Not interested.
    }

    @Override
    public void onAttackFailed(OwnedZone from, OwnedZone to) {
        // TODO
    }
    
    @Override
    public void onAttackZone(OwnedZone from, OwnedZone to) {
        // TODO
    }

    @Override
    public void onAttackQuestion(Attack question) {
        // TODO
    }

    @Override
    public void onDonatedZones(Player from, Player to, Zone[] zones) {
        // TODO
    }

    @Override
    public void onAnsweredQuestion(Attack question, int answer) {
        // TODO
    }

    @Override
    public void onQuestionDone(Attack question, QuestionAnswers answers) {
        // TODO
    }

    @Override
    public void onAttackDone() {
        // TODO
    }

    @Override
    public void onReplace(Player oldOne, Player newOne) {
        // Not interested.
    }
}