/** @constructor */
function SelfTab(tv, id) {
    AbstractTab.call(this, tv, id);
}

SelfTab.prototype = new AbstractTab();

SelfTab.prototype.getElementFor = function (message, type) {
    var e = document.createElement('p');
    e.setAttribute('class', type);
    createClassedSpan(e, 'message', message);
    return e;
};