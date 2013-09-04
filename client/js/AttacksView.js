/** @constructor */
function AttacksView(gui) {
    this.gui = gui;
    
    this.element = null;
}

AttacksView.prototype.setup = function () {
    this.element = createGuiElement('attacks-view');
    
    this.onResize();
};

AttacksView.prototype.onResize = function () {
    var pos = this.gui.positions;
    
    var style = this.element.style;
    style.left = pos.attackViewPadding + 'px';
    style.top = pos.attackViewPadding + 'px';
    style.width = pos.attackViewWidth + 'px';
    style.height = pos.attackViewHeight + 'px';
};
