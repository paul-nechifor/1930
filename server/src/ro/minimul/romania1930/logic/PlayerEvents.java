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
    
    /**
     * This notifies all players of a new attack or of a new player joining in
     * the attack. The attack object will be at to.attack.
     * 
     * @param attack
     */
    public void onAttackZone(OwnedZone from, OwnedZone to);
    
    /**
     * This notifies all the involved players of the four answer question data.
     * This will initially be sent to the two players in the initial attack and
     * then to all the players who join in. This is always sent after
     * {@link #onAttackZone(ro.minimul.romania1930.logic.Attack) onAttackZone()}.
     * 
     * @param attack 
     */
    public void onAttackFaQuestion(Attack attack);
    
    /**
     * This notifies all the players that the FA question is done. This only
     * happens after the deadline. After this no other player can join in the
     * attack.
     * 
     * This message contains whether or not the answers resulted in a winner in
     * which case the attack is done, the zones are transfered, and they are
     * once again available for attack. If not, a new NA question will decide
     * the winner and a new deadline is set.
     * 
     * Uninvolved players should only lookup if there is a winner or a new
     * deadline is set.
     * 
     * Involved players should lookup: what the correct answer was, what answer
     * was given by each player (though is possible some didn't answer) and
     * if there was a winner.
     * 
     * If there wasn't a winner they should also lookup what the NA question is
     * and the deadline. Note that only the players who answered correctly can
     * answer this question.
     * 
     * @param attack 
     */
    public void onAttackFaResult(Attack attack);
    
    /**
     * This notifies all the players that the NA question is done. This only
     * happens after the deadline.
     * 
     * This message contains who the winner is. After this the zones are
     * transfered and they are once again available for attack.
     * 
     * Uninvolved players should only lookup what the transfered zones are.
     * 
     * Involved players should lookup the correct answer, the given answers and
     * the time for each.
     * 
     * @param attack 
     */
    public void onAttackNaResult(Attack attack);
    
    /**
     * One player gives another player some of his zones for free. This is
     * usually done by bots between each other to keep the game balanced.
     * 
     * @param from
     * @param to
     * @param zones 
     */
    public void onDonatedZones(Player from, Player to, Zone[] zones);
    
    public void onReplace(Player oldOne, Player newOne);
}
