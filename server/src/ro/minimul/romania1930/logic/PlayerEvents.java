package ro.minimul.romania1930.logic;

import ro.minimul.romania1930.data.Zone;

public interface PlayerEvents {
    public void onPersonalStart(Player self, PlayerControls controls, RoomInfo roomInfo);
    
    /**
     * This player (maybe all) is forcefully kicked out.
     * @param reason 
     */
    public void onForcedExit(String reason);
    public void onEnteredRoom(Player player);
    public void onExitedRoom(Player player, boolean error);
    public void onChangedName(Player player, String name);
    public void onSaid(Player player, String text);
    public void onAttackZone(Player player, Zone zone);
    public void onAttackQuestion(AttackQuestion question);
    public void onDonatedZones(Player from, Player to, Zone[] zones);
    public void onAnsweredQuestion(AttackQuestion question, int answer);
    public void onQuestionDone(AttackQuestion question, QuestionAnswers answers);
    public void onReplace(Player oldOne, Player newOne);
    public void onAttackDone();
}
