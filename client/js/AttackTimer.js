/** @constructor */
function AttackTimer(topView, attack) {
    this.topView = topView;
    this.attack = attack;
    
    this.svg = null;
    this.textNode = null;
}

AttackTimer.prototype.setup = function (parent) {
    var pos = this.topView.gui.positions;
    
    var h = pos.topViewContentHeight;
    var h2 = h / 2;
    var c2 = h * 0.4;
    
    this.svg = svgCreate(parent, 'svg', {
        width: h,
        height: h
    });
    this.svg.style.marginLeft = pos.topViewPadding + 'px';
    
    svgCreate(this.svg, 'circle', {
        cx: h2,
        cy: h2,
        r: h2,
        fill: COLORS.arrowFills[this.attack.victim.owner.id]
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
    this.textNode = document.createTextNode(' ');
    text.appendChild(this.textNode);
    this.tick(Date.now());
};

AttackTimer.prototype.tick = function (now) {
    var s = (this.attack.deadline - now) / 1000;
    var str = s.toFixed(0);
    if (str.length === 1) {
        str = '0' + str;
    }
    this.textNode.nodeValue = str;
    
    return s;
};

AttackTimer.prototype.remove = function () {
    this.svg.parentElement.removeChild(this.svg);
};
