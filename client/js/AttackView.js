/** @constructor */
function AttackView(gui) {
    this.gui = gui;
    
    this.element = null;
    this.closeElem = null;
    this.questionElem = null;
    this.answersElem = null;
}


AttackView.prototype.setup = function () {
    this.element = createGuiElement('attack-view');
    this.element.setAttribute('class', 'unselectable');
    
    this.closeElem = document.createElement('div');
    this.element.appendChild(this.closeElem);
    addText(this.closeElem, "×");
    this.closeElem.setAttribute('class', 'close-btn');
    this.show(false);
    this.closeElem.addEventListener('click', this.onClose.bind(this), true);
    
    this.questionElem = document.createElement('div');
    this.element.appendChild(this.questionElem);
    this.questionElem.setAttribute('class', 'question');
    addText(this.questionElem, "Text text text");
    
    this.answersElem = document.createElement('div');
    this.element.appendChild(this.answersElem);
    this.answersElem.setAttribute('class', 'answers');
    
    this.addFourAnswers();
    
    this.onResize();
};

AttackView.prototype.onResize = function () {
    var pos = this.gui.positions;
    
    var style = this.element.style;
    style.top = pos.attackViewTop + 'px';
    style.width = pos.windowWidth + 'px';
    style.height = pos.attackViewHeight + 'px';
    
    var closeStyle = this.closeElem.style;
    var qStyle = this.questionElem.style;
    var aStyle = this.answersElem.style;
    closeStyle.width = 
    closeStyle.height =
    closeStyle.fontSize =
    qStyle.marginTop =
    qStyle.marginLeft =
    qStyle.marginRight = 
    aStyle.marginLeft =
    aStyle.marginBottom =
    aStyle.marginRight = pos.attackViewPadding + 'px';
    qStyle.fontSize = pos.attackViewBigText + 'px';
};

AttackView.prototype.onClose = function () {
    this.show(false);
};

AttackView.prototype.show = function (show) {
    this.element.style.display = show ? 'block' : 'none';
};

AttackView.prototype.open = function () {
    this.show(true);
};

AttackView.prototype.open = function () {
    this.show(true);
};

AttackView.prototype.addFourAnswers = function () {
    var t = document.createElement('table');
    this.answersElem.appendChild(t);
    
    for (var i = 0; i < 2; i++) {
        var tr = document.createElement('tr');
        t.appendChild(tr);
        for (var j = 0; j < 2; j++) {
            var td = document.createElement('td');
            tr.appendChild(td);
            addText(td, i + ' ' + j);
        }
    }
};
