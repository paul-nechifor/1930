/** @constructor */
function OthersTab(tv, id) {
    AbstractTab.call(this, tv, id);
}

OthersTab.prototype = new AbstractTab();

OthersTab.prototype.getElementFor = function (message, type) {
    var e = document.createElement('p');
    e.setAttribute('class', type);
    createClassedSpan(e, 'message', message);
    return e;
};