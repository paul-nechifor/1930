package ro.minimul.romania1930.comm.msg;

import ro.minimul.romania1930.comm.Message;

public class PlayerTextMsg implements Message {
    public int id;
    public String text;
    
    public PlayerTextMsg(int id, String text) {
        this.id = id;
        this.text = text;
    }
}
