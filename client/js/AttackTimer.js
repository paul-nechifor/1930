/** @constructor */
function AttackTimer(attacksView) {
    this.attacksView = attacksView;
    this.svg = null;
    this.textNode = null;
}

AttackTimer.prototype.setup = function (parent) {
    var pos = this.attacksView.gui.positions;
    
    var h = pos.attackViewHeight;
    var h2 = h / 2;
    var c2 = h * 0.4;
    
    this.svg = svgCreate(parent, 'svg', {
        width: h,
        height: h
    });
    
    svgCreate(this.svg, 'circle', {
        cx: h2,
        cy: h2,
        r: h2,
        fill: COLORS.dark
    });
    
    svgCreate(this.svg, 'circle', {
        cx: h2,
        cy: h2,
        r: c2,
        fill: COLORS.lightDark
    });
    
    var text = svgCreate(this.svg, 'text', {
        x: h2,
        y: h * 0.7,
        'font-size': h * 0.5,
        'font-family': VALS.fontFamily,
        'fill': COLORS.zoneNameFontColor,
        'text-anchor': 'middle'
    });
    this.textNode = document.createTextNode('99');
    text.appendChild(this.textNode);
};

AttackTimer.prototype.setTime = function (s) {
    var str = s.toFixed(0);
    if (str.length === 1) {
        str = '0' + str;
    }
    this.textNode.nodeValue = str;
};