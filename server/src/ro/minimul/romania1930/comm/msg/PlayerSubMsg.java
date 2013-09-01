package ro.minimul.romania1930.comm.msg;

public class PlayerSubMsg {
    public int id;
    public boolean isHuman;
    public String name;
    public int[] zones;
    
    public PlayerSubMsg(int id, boolean isHuman, String name, int[] zones) {
        this.id = id;
        this.isHuman = isHuman;
        this.name = name;
        this.zones = zones;
    }
}
