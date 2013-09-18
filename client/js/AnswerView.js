/** @constructor */
function AnswerView(attackView, x, y, i) {
    this.attackView = attackView;
    this.x = x;
    this.y = y;
    this.i = i;
    
    this.element = null;
    this.div = null;
}

AnswerView.prototype.setup = function (parent) {
    this.element = document.createElement('td');
    parent.appendChild(this.element);
    this.div = document.createElement('div');
    this.element.appendChild(this.div);
    
    var span = document.createElement('span');
    this.div.appendChild(span);
    addText(span, String.fromCharCode(65 + this.i));
    
    addText(this.div, 'Ce faci azi? wfw ef wefwefwe fwefwe fwefw efwef ef ');
    
    var that = this;
    this.div.onclick = function () {
        that.attackView.onFourAnswer(that.i);
    };
    
    this.onResize();
};

AnswerView.prototype.onResize = function () {
    var pos = this.attackView.gui.positions;
    
    var style = this.element.style;
    style.width = pos.answerWidth + 'px';
    
    if (this.x === 0) {
        style.paddingRight = pos.attackViewPadding + 'px';
    }
    if (this.y === 0) {
        style.paddingBottom = pos.attackViewPadding + 'px';
    }
    
    var dStyle = this.div.style;
    dStyle.padding = pos.answerPadding + 'px';
    dStyle.fontSize = pos.answerFontSize + 'px';
};

AnswerView.prototype.removeListener = function () {
    this.div.onclick = function () {};
};
