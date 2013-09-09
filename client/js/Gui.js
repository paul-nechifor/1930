/** @constructor */
function Gui(game) {
    this.game = game;
    
    this.positions = null;
    
    this.minimap = null;
    this.map = null
    this.topView = null;
    this.attackView = null;
    this.contextView = null;
    this.tabbedView = null;
    this.messages = null;
    this.others = null;
    this.self = null;
}

Gui.prototype.setup = function () {
    this.setupSize();
    
    this.minimap = new Minimap(this);
    this.minimap.setup();
    this.map = new Map(this);
    this.map.setup();
    this.topView = new TopView(this);
    this.topView.setup();
    this.attackView = new AttackView(this);
    this.attackView.setup();
    this.attackView.open();
    
    this.contextView = new ContextView(this);
    this.contextView.setup();
    this.tabbedView = new TabbedView(this);
    
    this.messages = new MessagesTab(this.tabbedView, 0);
    this.others = new OthersTab(this.tabbedView, 1);
    this.self = new SelfTab(this.tabbedView, 2);
    
    var tabs = [
        {text: STR.messages, tab: this.messages},
        {text: STR.others, tab: this.others},
        {text: STR.self, tab: this.self}
    ];
    
    this.tabbedView.setup(tabs);
    
    window.onkeydown = this.onKeyDown.bind(this);
};

Gui.prototype.setupSize = function () {
    var that = this;
    this.positions = new Positions(this.game.data);
    that.positions.realign(window.innerWidth, window.innerHeight);
    
    window.onresize = function () {
        that.positions.realign(window.innerWidth, window.innerHeight);
        that.minimap.onResize();
        that.map.onResize();
        that.topView.onResize();
        that.attackView.onResize();
        that.contextView.onResize();
        that.tabbedView.onResize();
    };
};

Gui.prototype.onMinimapClick = function (mapX, mapY) {
    this.map.moveCamera(mapX, mapY);
};

Gui.prototype.onMapUpdate = function (x, y, width, height) {
    this.minimap.updateArea(x, y, width, height);
};

Gui.prototype.onZoneClicked = function (zone) {
    this.map.clearSelection();
    
    var showAttack = zone.owner.id === this.game.pc.id
            && zone.isAttacking === null
            && zone.attack === null
            && !zone.blockedForConfirmation;
    
    this.map.setSelectedZone(zone, showAttack);
    this.contextView.showZoneInfo(zone);
};

Gui.prototype.onZoneSelectedForAttack = function (to) {
    var from = this.map.selectedZone;
    this.map.clearSelection();
    
    this.game.onAttackZone(from, to);
    
    this.contextView.refreshContext();
};

Gui.prototype.onKeyDown = function (e) {
    var code = e.keyCode;
    
    if (code === 13) { // Enter key.
        if (this.messages.inputHaveFocus()) {
            var value = this.messages.clearInput();
            this.game.sendTextMessage(value);
        } else {
            this.messages.focusOnInput();
        }
        return false;
    } else if (code === 191) { // Slash key
        if (!this.messages.inputHaveFocus()) {
            this.messages.focusOnInput();
        }
    } else if (code === 37) { // Left key.
        this.map.nudgeCamera(-1, 0);
    } else if (code === 38) { // Up key.
        this.map.nudgeCamera(0, -1);
    } else if (code === 39) { // Right key.
        this.map.nudgeCamera(1, 0);
    } else if (code === 40) { // Down key.
        this.map.nudgeCamera(0, 1);
    } else if (code === 219) { // [
        this.map.zoomCamera(1);
    } else if (code === 221) { // ]
        this.map.zoomCamera(-1);
    }
    
    return true;
};