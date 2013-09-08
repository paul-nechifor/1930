/** @constructor */
function Game() {
    this.data = null;
    this.gui = null;
    this.players = {};
    this.pc = null; // Playing character.
    this.webSocket = null;
}

Game.prototype.start = function () {
    var that = this;
    this.loadData(function () {
        that.gui = new Gui(that);
        that.gui.setup();
        //that.createFakePlayers();
        that.createConnection();
    });
};

Game.prototype.loadData = function (callback) {
    var that = this;
    getJson('map.json', function (data) {
        that.data = data;
        callback();
    });
}

Game.prototype.createConnection = function () {
    var address = 'ws://' + GEN.webSocketAddress + ':' + GEN.webSocketPort;
    this.webSocket = new WebSocket(address);
    
    this.webSocket.onclose = this.onWebSocketClose.bind(this);
    this.webSocket.onerror = this.onWebSocketError.bind(this);
    
    var msgFuncs = [];
    var func;
    for (var i = 0, len = GEN.messageCodes.length; i < len; i++) {
        func = this['onWebSocket_' + GEN.messageCodes[i]];
        if (func !== undefined) {
            msgFuncs.push(func.bind(this));
        } else {
            msgFuncs.push(null);
        }
    }
    
    this.webSocket.onmessage = function (e) {
        var data = e.data;
        var code = data.charCodeAt(0);
        var msg = JSON.parse(data.substring(1));
        
        msgFuncs[code](msg);
    };
};

Game.prototype.setPc = function (pc) {
    this.pc = pc;
    this.pc.isHuman = true;
    
    // Replacing the color for the current player.
    changePcColors(this.pc.id);
    
    // Setting the same owner again in order to refresh the color.
    for (var key in this.pc.zones) {
        this.pc.zones[key].setNewOwner(this.pc);
    }
};

Game.prototype.onWebSocketClose = function () {
    this.fatalError(STR.connectionClosed);
};

Game.prototype.onWebSocketError = function () {
    this.fatalError(STR.connectionError);
};

Game.prototype.onWebSocket_RoomInfoMsg = function (roomInfo) {
    if (roomInfo.isFull) {
        this.fatalError(STR.noMoreFreeSpots);
        return;
    }
    
    this.initPlayers(roomInfo.players, roomInfo.self);
    this.initAttacks(roomInfo.attacks);
    
    this.gui.self.add(STR.gameStarted, 'info');
};

Game.prototype.onWebSocket_PlayerTextMsg = function (msg) {
    var player = this.players[msg.id];
    
    var from = player.name;
    if (!player.isHuman) {
        from += STR.robot;
    }
    if (msg.id === this.pc.id) {
        from += STR.me;
    }
    
    this.gui.messages.add(msg.text, 'msg', from);
};

Game.prototype.onWebSocket_AttackAcceptedMsg = function (msg) {
    var zones = this.gui.map.zones;
    var from = zones[msg.from];
    var to = zones[msg.to];
    
    to.blockedForConfirmation = false;
    from.blockedForConfirmation = false;
    
    this.showAttackText(from, to);
    
    this.setAttack(from, to, msg.s);
};

Game.prototype.onWebSocket_AttackFailedMsg = function (msg) {
    var map = this.gui.map;
    var zones = map.zones;
    var from = zones[msg.from];
    var to = zones[msg.to];
    
    to.blockedForConfirmation = false;
    from.blockedForConfirmation = false;
    map.removeArrow(from, to);
};

Game.prototype.onWebSocket_ReplaceMsg = function (msg) {
    var player = this.players[msg.id];
    
    var aHuman = player.isHuman;
    player.isHuman = msg.isHuman;
    var bHuman = player.isHuman;
    
    var format;
    
    if (aHuman && bHuman) {
        format = STR.replaceHumanWithHuman;
    } else if (aHuman && !bHuman) {
        format = STR.replaceHumanWithRobot;
    } else if (!aHuman && bHuman) {
        format = STR.replaceRobotWithHuman;
    } else if (!aHuman && !bHuman) {
        format = STR.replaceRobotWithRobot;
    }
    
    var text = fmt(format, player.name, player.name);
    
    this.gui.others.add(text, 'replace');
};

Game.prototype.sendMsg = function (code, msg) {
    this.webSocket.send(String.fromCharCode(code) + JSON.stringify(msg));
};

Game.prototype.sendTextMessage = function (text) {
    text = text.trim();
    
    if (text.length === 0) {
        return;
    }
    
    if (text.length > GEN.maxMessageSize) {
        text = text.substring(0, GEN.maxTextMessageSize);
    }
    
    this.sendMsg(MID.FromTextMsg, {text: text});
}

Game.prototype.onAttackZone = function (from, to) {
    this.sendMsg(MID.AttackZoneMsg, {from: from.id, to: to.id});
    
    to.blockedForConfirmation = true;
    from.blockedForConfirmation = true;
    
    // The arrow is drawn so that the user knows this was sent.
    this.gui.map.addArrow(from, to);
};

Game.prototype.initPlayers = function (playerInfos, self) {
    for (var i = 0, len = playerInfos.length; i < len; i++) {
        this.initPlayer(playerInfos[i]);
    }
    
    var pc = this.initPlayer(self);
    this.setPc(pc);
};

Game.prototype.initPlayer = function (playerInfo) {
    var player = new Player({
        id: playerInfo.id,
        isHuman: playerInfo.isHuman,
        name: playerInfo.name
    });
    this.players[player.id] = player;
    
    var zones = this.gui.map.zones;
    for (var k = 0; k < playerInfo.zones.length; k++) {
        player.addZone(zones[playerInfo.zones[k]]);
    }
    
    return player;
};

Game.prototype.initAttacks = function (attacks) {
    var zones = this.gui.map.zones;
    var i, j, lenI, lenJ, attack, froms, to, s;
    
    for (i = 0, lenI = attacks.length; i < lenI; i++) {
        attack = attacks[i];
        froms = attack.froms;
        to = zones[attack.to];
        s = attack.s / 1000.0;
        s = 60; // FIXME This needs to be removed.
        for (j = 0, lenJ = froms.length; j < lenJ; j++) {
            this.setAttack(zones[froms[j]], to, s);
        }
    }
};

Game.prototype.fatalError = function (text) {
    this.gui.self.add(text, 'error');
    this.gui.self.makeActive();
};

Game.prototype.showAttackText = function (from, to) {
    var who = from.owner.name;
    if (from.owner.id === this.pc.id) {
        who += STR.me;
    }
    var text = fmt(STR.hasAttacked, from.owner.name, to.name, to.owner.name);
    this.gui.others.add(text, 'attack');
};

Game.prototype.setAttack = function (from, to, secondsToAnswer) {
    from.isAttacking = to;
    
    if (to.attack === null) {
        to.attack = new Attack(to, from, secondsToAnswer);
    } else {
        to.attack.otherAggressors.push(to);
    }
    
    console.log(to.attack);
    from.owner.addAttacking(to.attack);
    to.owner.addBeingAttacked(to.attack);
    
    var pcId = this.pc.id;
    var fromOwnerId = from.owner.id;
    var toOwnerId = to.owner.id;
    
    // Add the arrow, except for my attacks which have them already.
    if (fromOwnerId !== pcId) {
        this.gui.map.addArrow(from, to);
    }
    
    if (fromOwnerId === pcId || toOwnerId === pcId) {
        this.gui.topView.addInvolvedAttack(to.attack);
    }
}
