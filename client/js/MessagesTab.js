/** @constructor */
function MessagesTab(tv, id) {
    AbstractTab.call(this, tv, id);
    this.input = null;
}

MessagesTab.prototype = new AbstractTab();

MessagesTab.prototype.setup = function (parent) {
    var div = AbstractTab.prototype.setup.call(this, parent);
    this.createInput(div);
    return div;
};

MessagesTab.prototype.onResize = function (pos) {
    var sizeY = pos.tabsHeight - 2 * pos.tabsPadding - pos.chatInputHeight;
    var margin = pos.tabsPadding;
    this.mw.setSize(sizeY, margin);
    
    var iStyle = this.input.style;
    iStyle.height = (pos.chatInputHeight - 2 * pos.tabsPadding) + 'px';
    iStyle.width = (pos.tabbedViewWidth - 4 * pos.tabsPadding) + 'px';
    iStyle.marginLeft = iStyle.marginTop = pos.tabsPadding + 'px';
    iStyle.paddingLeft = iStyle.paddingRight = pos.tabsPadding + 'px';
    iStyle.paddingTop = iStyle.paddingBottom = pos.tabsPadding / 2 + 'px';
};

MessagesTab.prototype.createInput = function (parent) {
    this.input = document.createElement('input');
    parent.appendChild(this.input);
    this.input.setAttribute('type', 'text');
    this.input.setAttribute('placeholder', STR.messageToSend);
    this.input.setAttribute('value', '');
};

MessagesTab.prototype.getElementFor = function (text, type, from) {
    var e = document.createElement('p');
    e.setAttribute('class', type);
    if (from !== undefined) {
        createClassedSpan(e, 'from', from);
        addText(e, ': ');
    }
    createClassedSpan(e, 'text', text);
    return e;
};

MessagesTab.prototype.inputHaveFocus = function () {
    return document.activeElement === this.input;
};

MessagesTab.prototype.focusOnInput = function () {
    this.makeActive();
    this.input.focus();
};

MessagesTab.prototype.clearInput = function () {
    var value = this.input.value;
    this.input.value = '';
    return value;
};