/** @constructor */
function TopView(gui) {
    this.gui = gui;
    
    this.element = null;
    this.attackTimers = [];
}

TopView.prototype.setup = function () {
    this.element = createGuiElement('top-view');
    
    setInterval(this.tick.bind(this), 100);
    
    this.onResize();
};

TopView.prototype.onResize = function () {
    var pos = this.gui.positions;
    
    var style = this.element.style;
    style.paddingTop = pos.topViewPadding + 'px';
    style.paddingBottom = pos.topViewPadding + 'px';
    style.width = pos.windowWidth + 'px';
    style.height = pos.topViewContentHeight + 'px';
};

TopView.prototype.addInvolvedAttack = function (attack) {
    var attackTimer = new AttackTimer(this, attack);
    attackTimer.setup(this.element);
    this.attackTimers.push(attackTimer);
};

TopView.prototype.tick = function () {
    var now = Date.now();
    var attackTimer, left;
    
    var len = this.attackTimers.length;
    // Looping in reverse because they might get removed.
    for (var i = len - 1; i >= 0; i--) {
        attackTimer = this.attackTimers[i];
        left = attackTimer.tick(now);
        if (left <= 0) {
            attackTimer.remove();
            this.attackTimers.splice(i, 1);
        }
    }
};