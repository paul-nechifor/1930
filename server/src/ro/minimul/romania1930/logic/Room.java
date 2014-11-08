package ro.minimul.romania1930.logic;

import java.util.ArrayList;
import java.util.Arrays;
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
import ro.minimul.romania1930.data.QuestionSet;
import ro.minimul.romania1930.data.Zone;
import ro.minimul.romania1930.web_logic.WebMessageRouter;
import ro.minimul.romania1930.web_logic.WebPlayer;

public class Room {
    private final Game game;
    private Config config;
    private QuestionSet questionSet;
    private WebMessageRouter messageRouter;
    private RoomInfo roomInfo;
    private AiContainer aiContainer;
    private RoomThread roomThread;
    
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
        public void attackZone(OwnedZone from, OwnedZone to) {
            Room.this.attackZone(player, from, to);
        }

        @Override
        public void answerFaQuestion(Attack question, int answer) {
        }

        @Override
        public void answerNaQuestion(Attack question, int answer) {
        }

        @Override
        public void donateZones(PlayerEvents to, Zone[] zones) {
        }
    }

    public Room(Game game) {
        this.game = game;
        
        Acceptor cm = this.game.getConnectionManager();
        
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
        questionSet = this.game.getQuestionSet();
        roomInfo = new RoomInfo(this.game.getMap(), config);
        messageRouter = new WebMessageRouter(this);
        aiContainer = new AiContainer(config);
        initializeWithBots();
        aiContainer.start();
        
        roomThread = new RoomThread(this);
        roomThread.start();
    }
    
    public synchronized void close() {
        roomThread.stopRunning();
        for (Player player : roomInfo.players) {
            player.playerEvents.onForcedExit("Going down.");
        }
        aiContainer.stop();
    }
    
    synchronized void tick() {
        long now = System.nanoTime();
        
        for (Attack a : roomInfo.attacks.values()) {
            if (a.deadline > now) {
                if (a.state == Attack.FA_QUESTION) {
                    processOverDeadlineFa(a);
                } else {
                    processOverDeadlineNa(a);
                }
            }
        }
    }
    
    public synchronized RoomInfo getRoomInfo() {
        return roomInfo;
    }
    
    private synchronized Controls tryToAccept(Connection c) {
        //AiPlayer bot = getBestBot();
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
    
    private synchronized void attackZone(Player player, OwnedZone from,
            OwnedZone to) {
        if (isAttackPossible(player, from, to)) {
            createAttack(from, to);
        } else {
            player.playerEvents.onAttackFailed(from, to);
        }
    }
    
    private void initializeWithBots() {
        int nBots = roomInfo.idUsed.length;
        int nZones = roomInfo.zones.length;
        int zonesPerBot = nZones / nBots;
        
        List<String> names = Arrays.asList(config.botNames);
        Collections.shuffle(names);
        
        List<Integer> ids = new ArrayList<Integer>();
        List<AiPlayer> unadded = new ArrayList<AiPlayer>();
        
        for (int i = 0; i < nBots; i++) {
            ids.add(i);
            unadded.add(null);
        }
        
        Collections.shuffle(ids);
        
        for (int i = 0; i < nBots; i++) {
            AiPlayer player = new AiPlayer(ids.get(i));
            player.setName(names.get(i));
            roomInfo.idUsed[i] = true;
            unadded.set(ids.get(i), player);
            
            int total = zonesPerBot;
            // Give the last bot all the remaining zones.
            if (i == nBots - 1) {
                total += nZones % nBots;
            }
            
            for (int j = 0; j < total; j++) {
                int zoneId = i * zonesPerBot + j;
                transferZone(roomInfo.zones[zoneId], player);
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
        OwnedZone[] copy = oldOne.zones.toArray(new OwnedZone[0]);
        for (OwnedZone zone : copy) {
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
    
    private void transforZone(OwnedZone zone, Player from, Player to) {
        from.zones.remove(zone);
        transferZone(zone, to);
    }
    
    private void transferZone(OwnedZone zone, Player to) {
        to.zones.add(zone);
        zone.owner = to;
    }
    
    private boolean isAttackPossible(Player player, OwnedZone from,
            OwnedZone to) {
        if (!player.zones.contains(from)) {
            // Not your zone.
            return false;
        }
        
        if (player.zones.contains(to)) {
            // Cannot attack your own zone.
            return false;
        }
        
        if (!from.zone.neighborSet.contains(to.zone)) {
            // The zones aren't neighbors.
            return false;
        }
        
        if (to.isAttacking != null) {
            // The zone is attacking so it is immune.
            return false;
        }
        
        if (from.isAttacking != null) {
            // You cannot attack multiple zones from the same zone.
            return false;
        }
        
        if (from.attack != null) {
            // You cannot attack from an attacked zone.
            return false;
        }
        
        Attack attack = to.attack;
        if (attack != null) {
            if (attack.state != Attack.FA_QUESTION) {
                // You can only attack a zone which is in the first stage.
                return false;
            }
            
            for (AnswerZone az : attack.aggressors) {
                if (player.zones.contains(az.ownedZone)) {
                    // You cannot attack the same zone from more than one zone.
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private void createAttack(OwnedZone from, OwnedZone to) {
        from.isAttacking = to;
        
        Attack attack = to.attack;
        boolean newAttack = attack == null;
        
        if (newAttack)  {
            attack = Attack.create(to, from, 60, questionSet);
            to.attack = attack;
            roomInfo.attacks.put(attack.id, attack);
        } else {
            attack.addAggressor(from);
        }
        
        // Notify all the players of the attack.
        for (Player player : roomInfo.players) {
            player.playerEvents.onAttackZone(from, to);
        }
        
        // Send the question data to the involved players or newly involved
        // player.
        if (newAttack) {
            from.owner.playerEvents.onAttackFaQuestion(attack);
            to.owner.playerEvents.onAttackFaQuestion(attack);
        } else {
            from.owner.playerEvents.onAttackFaQuestion(attack);
        }
    }
    
    private void processOverDeadlineFa(Attack attack) {
        int correct = attack.faQuestion.correct;
        
        List<AnswerZone> correctOnes = new ArrayList<AnswerZone>();
        
        for (AnswerZone az : attack.aggressors) {
            if (az.faAnswer == correct) {
                correctOnes.add(az);
            }
        }
        
        if (attack.victim.faAnswer == correct) {
            correctOnes.add(attack.victim);
        }
        
        // If there is a clear winner the attack is over.
        if (correctOnes.size() == 1) {
            endAttackAtFa(attack, correctOnes.get(0));
        // Else move to the next stage.
        } else {
            moveToNaQuestion(attack, correctOnes);
        }
    }
    
    private void processOverDeadlineNa(Attack attack) {
        int correct = attack.naQuestion.answer;
        List<AnswerZone> whoCanAnswer = getWhoCanAnswer(attack);
        AnswerZone winner = AnswerZone.getWinner(whoCanAnswer, correct);
        endAttackAtNa(attack, winner);
    }
    
    private void moveToNaQuestion(Attack attack, List<AnswerZone> correctOnes) {
        setWhoCanAnswer(attack, correctOnes);
    }
    
    private void endAttackAtFa(Attack attack, AnswerZone winner) {
        ;;; // TODO
        
        endAttack(attack, winner);
    }
    
    private void endAttackAtNa(Attack attack, AnswerZone winner) {
        ;;; // TODO
        
        endAttack(attack, winner);
    }
    
    private void endAttack(Attack attack, AnswerZone winner) {
        AnswerZone loser = (attack.victim == winner)
                ? attack.aggressors.get(0)
                : attack.victim;
        
        transforZone(loser.ownedZone, loser.ownedZone.owner,
                winner.ownedZone.owner);
        
        roomInfo.attacks.remove(attack.id);
    }
    
    private void setWhoCanAnswer(Attack attack, List<AnswerZone> correctOnes) {
        // If none got it right, they all go in the next stage.
        if (correctOnes.isEmpty()) {
            attack.victim.canAnswerNa = true;
            for (AnswerZone az : attack.aggressors) {
                az.canAnswerNa = true;
            }
        // Else, only the ones that got it right can answer.
        } else {
            for (AnswerZone az : correctOnes) {
                az.canAnswerNa = true;
            }
        }
    }
    
    private List<AnswerZone> getWhoCanAnswer(Attack attack) {
        List<AnswerZone> ret = new ArrayList<AnswerZone>();
        
        if (attack.victim.canAnswerNa) {
            ret.add(attack.victim);
        }
        
        for (AnswerZone az : attack.aggressors) {
            if (az.canAnswerNa) {
                ret.add(az);
            }
        }
        
        return ret;
    }
}
