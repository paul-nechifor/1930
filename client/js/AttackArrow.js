/** @constructor */
function AttackArrow(from, to) {
    this.from = from;
    this.to = to;
    this.element = null;
}

AttackArrow.prototype.addToDom = function (svg) {
    var a = this.from.zoneData.center;
    var b = this.to.zoneData.center;
    
    var dx = a[0] - b[0];
    var dy = a[1] - b[1];
    var dist = Math.sqrt(dx*dx + dy*dy);
    var angle = Math.atan2(dy, dx) * 180 / Math.PI + 90;
    
    var arrowHeight = dist - DIMS.zoneCenterRadius;
    var hbw = DIMS.arrowBodyWidth / 2;
    var bh = arrowHeight - DIMS.arrowHeadHeight;
    
    var path = [
        'M0 0',
        (-hbw) + ' ' + bh,
        (-hbw - DIMS.arrowHeadOverflow) + ' ' + bh,
        '0 ' + arrowHeight,
        (hbw + DIMS.arrowHeadOverflow) + ' ' + bh,
        hbw + ' ' + bh + 'z'
    ].join('L');
    
    var trans = 'translate(' + a[0] + ' ' + a[1] + ') rotate(' + angle + ')';
    var fromId = this.from.owner.id;
    this.element = svgCreate(svg, 'path', {
        d: path,
        fill: COLORS.arrowFills[fromId],
        stroke: COLORS.arrowStrokes[fromId],
        'stroke-width': 10,
        transform: trans
    });
};

AttackArrow.prototype.remove = function () {
    this.element.parentElement.removeChild(this.element);
}