/** @constructor */
function AbstractTab(tv, id) {
    this.tv = tv;
    this.id = id;
    this.mw = new MessageWindow(100);
    this.unadded = [];
}

AbstractTab.prototype.setup = function (parent) {
    var div = createTabDiv(parent);
    this.mw.setup(div);
    return div;
};

AbstractTab.prototype.onResize = function (pos) {
    var sizeY = pos.tabsHeight - 2 * pos.tabsPadding;
    var margin = pos.tabsPadding;
    this.mw.setSize(sizeY, margin);
};

AbstractTab.prototype.add = function () {
    if (this.isActive()) {
        this.mw.add(this.getElementFor.apply(this, arguments));
    } else {
        this.unadded.push(arguments);
    }
};

AbstractTab.prototype.getElementFor = function () {
    throw new Error();
};

AbstractTab.prototype.isActive = function () {
    return this.tv.activeTab === this.id;
};

AbstractTab.prototype.onActivated = function () {
    if (this.unadded.length > 0) {
        var element;
        for (var i = 0, len = this.unadded.length; i < len; i++) {
            element = this.getElementFor.apply(this, this.unadded[i]);
            this.mw.add(element);
        }
        this.unadded = [];
    }
};

AbstractTab.prototype.makeActive = function () {
    this.tv.makeTabActive(this.id);
};