/** @constructor */
function Zone(map, id, zoneData, minimapPath) {
    this.map = map;
    this.id = id;
    this.zoneData = zoneData;
    this.minimapPath = minimapPath;
    
    this.arrows = {};
    this.owner = null;
    this.path = null;
    this.centerCircle = null;
    this.nameGroup = null;
    this.neighbors = [];
    
    this.isAttacking = null;
    this.attack = null;
    
    // This used to block attacking while waiting for the proper attack to
    // be authorized by the server.
    this.blockedForConfirmation = false;
    
    this.name = this.zoneData.name;
    if (this.zoneData.county !== undefined) {
        this.name += ' (' + this.zoneData.county + ')';
    }
    this.uName = this.name.toUpperCase();
}

Zone.prototype.addToDom = function (svg, circles) {
    this.path = svgCreate(svg, 'path', {
        d: this.zoneData.path,
        'stroke-width': DIMS.zoneStrokeWidth,
        stroke: COLORS.zoneStartStroke,
        fill: COLORS.zoneStartFill
    });
    
    var gui = this.map.gui;
    var that = this;
    
    var onMouseDown = function () {
        gui.onZoneClicked(that);
    };
    
    this.path.addEventListener('mousedown', onMouseDown);
    
    this.centerCircle = svgCreate(circles, 'circle', {
        cx: this.zoneData.center[0],
        cy: this.zoneData.center[1],
        r: DIMS.zoneCenterRadius,
        fill: '#FFF'
    });
    
    this.centerCircle.addEventListener('mousedown', onMouseDown);
};

Zone.prototype.setNewOwner = function (owner) {
    this.owner = owner;
    var color = COLORS.zoneFills[owner.id];
    this.path.setAttribute('fill', color);
    this.path.setAttribute('stroke', COLORS.arrowFills[owner.id]);
    this.minimapPath.setAttribute('fill', color);
    this.centerCircle.setAttribute('fill', COLORS.centerFills[owner.id]);
};

Zone.prototype.setSelected = function (selected) {
    var id = this.owner.id;
    
    if (selected) {
        this.path.setAttribute('fill', COLORS.arrowFills[id]);
        this.centerCircle.setAttribute('fill', COLORS.arrowStrokes[id]);
    } else {
        this.path.setAttribute('fill', COLORS.zoneFills[id]);
        this.centerCircle.setAttribute('fill', COLORS.centerFills[id]);
    }
};

Zone.prototype.showName = function (show, showAttack) {
    if (!show) {
        this.nameGroup.parentElement.removeChild(this.nameGroup);
        return;
    }
    
    var rectFill;
    
    if (showAttack) {
        rectFill = COLORS.centerFills[this.owner.id];
    } else {
        rectFill = COLORS.arrowStrokes[this.owner.id];
    }
    
    this.nameGroup = svgCreate(this.map.svgNames, 'g', {});
    var rect = svgCreate(this.nameGroup, 'rect', {
        fill: rectFill
    });
    
    // Names are all in uppercase which means there are no descenders. This
    // makes the middle of the text be a little higher than it should be for
    // uppercase only letter. This is not the best fix.
    var upperCaseFix = DIMS.zoneNameFontSize * 0.1;
    
    var text = svgCreate(this.nameGroup, 'text', {
        x: this.zoneData.center[0],
        y: this.zoneData.center[1] + upperCaseFix,
        'font-size': DIMS.zoneNameFontSize,
        'font-family': VALS.fontFamily,
        'fill': COLORS.zoneNameFontColor,
        'text-anchor': 'middle'
    });
    addText(text, this.uName);
    
    if (showAttack) {
        text.setAttribute('style', 'cursor:crosshair');
        var that = this;
        this.nameGroup.addEventListener('mousedown', function () {
            that.map.gui.onZoneSelectedForAttack(that);
        }, true);
    }
    
    // FIXME: The calculations done here are incorrect. They depend on the
    // present values.
    
    var b = text.getBBox();
    var paddingX = 30;
    var paddingY = 30;
    rect.setAttribute('x', b.x - paddingX);
    rect.setAttribute('y', b.y - paddingY / 2 + upperCaseFix);
    rect.setAttribute('width', b.width + 2 * paddingX);
    rect.setAttribute('height', b.height + 2 * paddingY);
    text.setAttribute('y', b.y + b.height);
};