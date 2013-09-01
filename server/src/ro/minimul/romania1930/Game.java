package ro.minimul.romania1930;

import java.io.IOException;
import ro.minimul.romania1930.comm.ConnectionManager;
import ro.minimul.romania1930.data.Config;
import ro.minimul.romania1930.data.Map;
import ro.minimul.romania1930.data.QuestionSet;
import ro.minimul.romania1930.logic.Room;

public class Game {
    private Config config;
    private QuestionSet questionSet;
    private ConnectionManager connectionManager;
    private Map map;

    private Room room;
    
    public Game() {
    }
    
    public void load() throws IOException, ClassNotFoundException {
        config = Config.load("config.json");
        questionSet = QuestionSet.load(config);
        map = Map.load(config);

        connectionManager = new ConnectionManager(config);
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
    
    public ConnectionManager getConnectionManager() {
        return this.connectionManager;
    }
    
    public Map getMap() {
        return this.map;
    }
}
