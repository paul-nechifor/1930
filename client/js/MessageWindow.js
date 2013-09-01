/** @constructor */
function MessageWindow(maxRetain) {
    this.maxRetain = maxRetain;
    this.div = null;
    this.sc = new ScrollContainer();
}

MessageWindow.prototype.setup = function (parent) {
    this.div = document.createElement('div');
    parent.appendChild(this.div);
    this.div.setAttribute('class', 'messages');
    
    this.sc.setup(this.div);
};

MessageWindow.prototype.add = function (element) {
    var children = this.sc.inner.children;
    if (children.length >= this.maxRetain) {
        this.sc.inner.removeChild(children[0]);
    }
    
    this.sc.inner.appendChild(element);
    this.sc.innerChangedAndToBottom();
};

MessageWindow.prototype.setSize = function (sizeY, margin) {
    var s = this.div.style;
    s.height = sizeY + 'px';
    s.margin = margin + 'px';
    this.sc.outer.style.height = sizeY + 'px';
}