package ro.minimul.romania1930.logic;

import ro.minimul.romania1930.data.Zone;

public interface PlayerControls {
    public void exitRoom(boolean error);
    public void changeName(String name);
    public void say(String text);
    public void attackZone(Zone zone);
    public void answerQuestion(AttackQuestion question, int answer);
    public void donateZones(PlayerEvents to, Zone[] zones);
}
