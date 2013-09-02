package ro.minimul.romania1930.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ro.minimul.romania1930.Game;
import ro.minimul.romania1930.ai.AiContainer;
import ro.minimul.romania1930.ai.AiPlayer;
import ro.minimul.romania1930.comm.Acceptor;
import ro.minimul.romania1930.comm.Connection;
import ro.minimul.romania1930.comm.Message;
import ro.minimul.romania1930.comm.msg.RoomInfoMsg;
import ro.minimul.romania1930.data.Config;
import ro.minimul.romania1930.data.Zone;
import ro.minimul.romania1930.web_logic.WebMessageRouter;
import ro.minimul.romania1930.web_logic.WebPlayer;

public class Room {
    private final Game game;
    private final AiContainer aiContainer = new AiContainer();
    private Config config;
    private RoomInfo roomInfo;
    
    private class Controls implements PlayerControls {
        final Player player;
        
        public Controls(Player player) {
            this.player = player;
        }
        
        @Override
        public void exitRoom(boolean error) {
            Room.this.exitRoom(player, error);
        }

        @Override
        public void changeName(String name) {
            Room.this.changeName(player, name);
        }

        @Override
        public void say(String text) {
            Room.this.say(player, text);
        }

        @Override
        public void attackZone(Zone zone) {
        }

        @Override
        public void answerQuestion(AttackQuestion question, int answer) {
        }

        @Override
        public void donateZones(PlayerEvents to, Zone[] zones) {
        }
    }

    public Room(Game game) {
        this.game = game;
        
        Acceptor cm = this.game.getConnectionManager();
        final WebMessageRouter messageRouter
                = game.getConnectionManager().messageRouter;
        
        cm.setOnAcceptListener(new Acceptor.Listener() {
            @Override
            public void onAccept(Connection connection) {
                final Controls playerControls = tryToAccept(connection);
                if (playerControls == null) {
                    return;
                }
                
                connection.setListener(new Connection.Listener() {
                    @Override
                    public void onMessage(Message message) {
                        messageRouter.route(playerControls, message);
                    }

                    @Override
                    public void onEnd(boolean error) {
                        playerControls.exitRoom(error);
                    }
                });
            }
        });
    }
    
    public synchronized void open() {
        config = this.game.getConfig();
        roomInfo = new RoomInfo(this.game.getMap(), config);
        initializeWithBots();
        aiContainer.start();
    }
    
    public synchronized void close() {
        for (Player player : roomInfo.players) {
            player.playerEvents.onForcedExit("Going down.");
        }
        aiContainer.stop();
    }
    
    private synchronized Controls tryToAccept(Connection c) {
        //Player bot = getBestBot();
        AiPlayer bot = getRandomBot();
        if (bot == null) {
            c.sendMessage(RoomInfoMsg.fromFull());
            return null;
        }
        
        WebPlayer player = new WebPlayer(bot.id, c);
        replacePlayers(bot, player);
        player.setName(bot.getName());
        
        Controls playerControls = addWebPlayerToRoom(player);

        return playerControls;
    }
    
    private synchronized void exitRoom(Player player, boolean error) {
        AiPlayer bot = new AiPlayer(player.id);
        replacePlayers(player, bot);
        addAiPlayerToRoom(bot);
    }

    private synchronized void changeName(Player player, String name) {
        if (name.equals("Paul")) {
            return;
        }

        player.setName(name);

        for (Player other : roomInfo.players) {
            other.playerEvents.onChangedName(player, name);
        }
    }

    private synchronized void say(Player player, String text) {
        String sendText = text;
        if (sendText.length() > config.maxTextMessageSize) {
            sendText = sendText.substring(0, config.maxTextMessageSize);
        }

        for (Player other : roomInfo.players) {
            other.playerEvents.onSaid(player, sendText);
        }
    }
    
    private void initializeWithBots() {
        int nBots = roomInfo.idUsed.length;
        int nZones = roomInfo.map.zones.length;
        int zonesPerBot = nZones / nBots;
        
        List<Integer> ids = new ArrayList<Integer>();
        List<AiPlayer> unadded = new ArrayList<AiPlayer>();
        
        for (int i = 0; i < nBots; i++) {
            ids.add(i);
            unadded.add(null);
        }
        
        Collections.shuffle(ids);
        
        for (int i = 0; i < nBots; i++) {
            AiPlayer player = new AiPlayer(ids.get(i));
            player.setName("Jucătorul nr. " + (i + 1));
            roomInfo.idUsed[i] = true;
            unadded.set(ids.get(i), player);
            
            int total = zonesPerBot;
            // Give the last bot all the remaining zones.
            if (i == nBots - 1) {
                total += nZones % nBots;
            }
            
            for (int j = 0; j < total; j++) {
                int zoneId = i * zonesPerBot + j;
                transferZone(roomInfo.map.zones[zoneId], player);
            }
        }
        
        for (AiPlayer player : unadded) {
            addAiPlayerToRoom(player);
        }
    }
    
    private AiPlayer getBestBot() {
        Player best = null;
        int bestNZones = 0;
        int nZones;
        
        for (Player player : roomInfo.players) {
            nZones = player.zones.size();
            if (!player.isHuman && nZones > bestNZones) {
                best = player;
                bestNZones = nZones;
            }
        }
        
        return (AiPlayer) best;
    }
    
    private AiPlayer getRandomBot() {
        List<AiPlayer> bots = new ArrayList<AiPlayer>();
        
        for (Player player : roomInfo.players) {
            if (!player.isHuman) {
                bots.add((AiPlayer) player);
            }
        }
        
        if (bots.isEmpty()) {
            return null;
        }
        
        return bots.get((int) (Math.random() * bots.size()));
    }
    
    private void addAiPlayerToRoom(AiPlayer aiPlayer) {
        addPlayerToRoom(aiPlayer, new Controls(aiPlayer));
        aiContainer.addPlayer(aiPlayer);
    }
    
    private Controls addWebPlayerToRoom(WebPlayer webPlayer) {
        Controls playerControls = new Controls(webPlayer);
        addPlayerToRoom(webPlayer, playerControls);
        return playerControls;
    }
    
    private void addPlayerToRoom(Player player, PlayerControls playerControls) {
        player.playerEvents.onPersonalStart(player, playerControls, roomInfo);
        roomInfo.players.add(player);
    }
    
    private void replacePlayers(Player<?> oldOne, Player<?> newOne) {
        for (Zone zone : oldOne.zones.toArray(new Zone[0])) {
            transforZone(zone, oldOne, newOne);
        }
        
        // Tell the old player to forcefully exit (just in case) and remove him.
        oldOne.playerEvents.onForcedExit("You are being replaced.");
        roomInfo.players.remove(oldOne);
        
        if (oldOne instanceof AiPlayer) {
            aiContainer.removePlayer((AiPlayer) oldOne);
        }
        
        // Tell all the other players that he's been replaced.
        for (Player player : roomInfo.players) {
            player.playerEvents.onReplace(oldOne, newOne);
        }
    }
    
    private void transforZone(Zone zone, Player from, Player to) {
        from.zones.remove(zone);
        transferZone(zone, to);
    }
    
    private void transferZone(Zone zone, Player to) {
        to.zones.add(zone);
        roomInfo.zoneOwners[zone.id] = to;
    }
}
