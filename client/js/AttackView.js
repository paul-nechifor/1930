/** @constructor */
function AttackView(gui) {
    this.gui = gui;
}


AttackView.prototype.setup = function () {
    this.element = createGuiElement('attack-view');
    
    var close = document.createElement('div');
    this.element.appendChild(close);
    addText(close, "×");
    close.setAttribute('class', 'close-btn');
    
    this.onResize();
};

AttackView.prototype.onResize = function () {
    var pos = this.gui.positions;
    
    var style = this.element.style;
    style.top = pos.attackViewTop + 'px';
    style.width = pos.windowWidth + 'px';
    style.height = pos.attackViewHeight + 'px';
};