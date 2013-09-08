/** @constructor */
function Map(gui) {
    this.gui = gui;
    
    this.svg = null;
    this.svgZones = null;
    this.svgArrows = null;
    this.svgCircles = null;
    this.svgNames = null;
    this.camera = new MapCamera(this);
    this.zones = [];
    this.selectedZone = null;
    this.selectionCleanupArrows = null;
}

Map.prototype.setup = function () {
    var element = createGuiElement('map');
    element.setAttribute('class', 'unselectable');
    
    this.svg = svgCreate(element, 'svg', {});
    
    this.setupZones();
    this.camera.setup();
    
    this.onResize();
};

Map.prototype.setupZones = function () {
    var gui = this.gui;
    var zoneDatas = gui.game.data.zones;
    var minimapPaths = gui.minimap.paths;
    
    this.svgZones = svgCreate(this.svg, 'g', {});
    this.svgArrows = svgCreate(this.svg, 'g', {});
    this.svgCircles = svgCreate(this.svg, 'g', {});
    
    var i, lenI = zoneDatas.length;
    
    for (i = 0; i < lenI; i++) {
        this.zones[i] = new Zone(this, i, zoneDatas[i], minimapPaths[i]);
        this.zones[i].addToDom(this.svgZones, this.svgCircles);
    }
    
    // Adding object references for each neighbor.
    var j, lenJ, neighbourIds;
    for (i = 0; i < lenI; i++) {
        neighbourIds = zoneDatas[i].neighs;
        for (j = 0, lenJ = neighbourIds.length; j < lenJ; j++) {
            this.zones[i].neighbours.push(this.zones[neighbourIds[j]]);
        }
    }
    
    this.svgNames = svgCreate(this.svg, 'g', {});
};

Map.prototype.onResize = function () {
    var pos = this.gui.positions;
    
    this.svg.setAttribute('width', pos.windowWidth);
    this.svg.setAttribute('height', pos.uiDivideY);
    this.svg.parentElement.style.height = pos.uiDivideY + 'px';
    
    this.updateCamera();
};

Map.prototype.updateCamera = function () {
    var c = this.camera;
    c.update();
    this.gui.onMapUpdate(c.offsetX, c.offsetY, c.canvasW, c.canvasH);
};

Map.prototype.moveCamera = function (mapX, mapY) {
    this.camera.focusX = mapX;
    this.camera.focusY = mapY;
    this.updateCamera();
};

Map.prototype.nudgeCamera = function (x, y) {
    this.camera.nudge(x * DIMS.nudgeIncrement, y * DIMS.nudgeIncrement);
    this.updateCamera();
};

Map.prototype.zoomCamera = function (direction) {
    this.camera.zoom(direction);
    this.updateCamera();
};

Map.prototype.addArrow = function (from, to) {
    var arrow = new AttackArrow(from, to);
    from.arrows[to.id] = arrow;
    
    arrow.addToDom(this.svgArrows);
};

Map.prototype.removeArrow = function (from, to) {
    var arrow = from.arrows[to.id];
    delete from.arrows[to.id];
    arrow.remove();
};

Map.prototype.setSelectedZone = function (zone, showAttack) {
    this.selectedZone = zone;
    this.selectedZone.setSelected(true);
    this.selectedZone.showName(true);
    
    if (showAttack) {
        this.showAttackOptions(zone);
    }
}

Map.prototype.clearSelection = function () {
    if (this.selectedZone !== null) {
        this.selectedZone.setSelected(false);
        this.selectedZone.showName(false);
        this.selectedZone = null;
    }
    
    var arrows = this.selectionCleanupArrows;
    if (arrows !== null) {
        var vals, from, to;
        for (var i = 0, len = arrows.length; i < len; i++) {
            vals = arrows[i];
            from = vals[0];
            to = vals[1];
            this.removeArrow(from, to);
            to.showName(false);
        }
        this.selectionCleanupArrows = null;
    }
}

Map.prototype.showAttackOptions = function (zone) {
    var neighIds = zone.zoneData.neighs;
    var zones = this.zones;
    var owner = zone.owner;
    var ownerZones = owner.zones;
    var neighId, neigh;
    
    this.selectionCleanupArrows = [];
    
    for (var i = 0, len = neighIds.length; i < len; i++) {
        neighId = neighIds[i];
        // You cannot attack one of your zones.
        if (neighId in ownerZones) {
            continue;
        }
        neigh = zones[neighId];
        // You cannot attack an attacker.
        if (neigh.isAttacking !== null) {
            continue;
        }
        // You cannot attack this zone if it is pending.
        if (neigh.blockedForConfirmation) {
            continue;
        }
        // You cannot attack the same zone with more than one of your zones.
        if (owner.attackedZones[neigh.id]) {
            continue;
        }
        neigh.showName(true, true);
        
        this.addArrow(zone, neigh);
        this.selectionCleanupArrows.push([zone, neigh]);
    }
};