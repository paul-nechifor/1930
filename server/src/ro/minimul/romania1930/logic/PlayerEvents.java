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
    public void onChangedName(Player player, String name);
    public void onSaid(Player player, String text);
    
    /**
     * Notify the player that the attack he tried to make could not be
     * performed. This usually would happen if the other zone attacked first
     * and this player didn't know about it at the time of his attack.
     * 
     * @param from
     * @param to 
     */
    public void onAttackFailed(OwnedZone from, OwnedZone to);
    
    public void onAttackZone(OwnedZone from, OwnedZone to);
    
    public void onAttackQuestion(Attack question);
    public void onDonatedZones(Player from, Player to, Zone[] zones);
    public void onAnsweredQuestion(Attack question, int answer);
    public void onQuestionDone(Attack question, QuestionAnswers answers);
    public void onAttackDone();
    public void onReplace(Player oldOne, Player newOne);
}
