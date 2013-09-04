/** @constructor */
function Gui(game) {
    this.game = game;
    
    this.positions = null;
    
    this.minimap = null;
    this.map = null
    this.attacksView = null;
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
    this.attacksView = new AttacksView(this);
    this.attacksView.setup();
    
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
        that.attacksView.onResize();
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
            && zone.isAttackingZone === null;
    
    this.map.setSelectedZone(zone, showAttack);
    this.contextView.showZoneInfo(zone);
};

Gui.prototype.onZoneSelectedForAttack = function (to) {
    var from = this.map.selectedZone;
    this.map.clearSelection();
    
    this.game.onAttackZone(from, to);
    
    // Set the to busy while the server responds to the attack request.
    from.isAttackingZone = to;
    to.firstAttackerZone = from;
    to.isAttackedByPc = true;
    
    // Add the arrow, but it will be removed if the attack happened after
    // another one.
    this.map.addArrow(from.id, to.id);
    
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
    }
    
    return true;
};