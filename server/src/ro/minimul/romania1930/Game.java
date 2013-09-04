package ro.minimul.romania1930;

import java.io.IOException;
import ro.minimul.romania1930.comm.Acceptor;
import ro.minimul.romania1930.data.Config;
import ro.minimul.romania1930.data.Map;
import ro.minimul.romania1930.data.QuestionSet;
import ro.minimul.romania1930.logic.Room;

public class Game {
    private Config config;
    private QuestionSet questionSet;
    private Map map;
    
    private Acceptor connectionManager;
    private Room room;
    
    public Game() {
    }
    
    public void load() throws IOException, ClassNotFoundException {
        config = Config.load("config.json");
        questionSet = QuestionSet.load(config);
        map = Map.load(config);
        
        connectionManager = new Acceptor(config);
        room = new Room(this);
    }
    
    public void start() throws IOException {
        room.open();
        connectionManager.start();

        System.out.println("Started.");
        System.in.read();
        System.out.println("Exiting...");

        connectionManager.stop();
        room.close();
    }
    
    public Config getConfig() {
        return this.config;
    }
    
    public QuestionSet getQuestionSet() {
        return this.questionSet;
    }
    
    public Acceptor getConnectionManager() {
        return this.connectionManager;
    }
    
    public Map getMap() {
        return this.map;
    }
}
