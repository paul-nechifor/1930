/** @constructor */
function Minimap(gui) {
    this.gui = gui;
    
    this.svg = null;
    this.areaRect = null;
    this.paths = [];
}

Minimap.prototype.setup = function () {
    var element = createGuiElement('minimap');
    var gui = this.gui;
    var size = gui.game.data.size;
    var pos = gui.positions;
    
    this.svg = svgCreate(element, 'svg', {
        width: pos.minimapWidth,
        height: pos.uiDivideSize
    });
    setSvgViewBox(this.svg, 0, 0, size[0], size[1]);
    
    element.addEventListener('mousedown', function (e) {
        e.preventDefault();
        var ratio = size[0] / pos.minimapWidth;
        gui.onMinimapClick(e.pageX * ratio, (e.pageY - pos.uiDivideY) * ratio);
    }, true);
    
    this.setupDrawData();
};

Minimap.prototype.onResize = function () {
    var pos = this.gui.positions;
    this.svg.setAttribute('width', pos.minimapWidth);
    this.svg.setAttribute('height', pos.uiDivideSize);
};

Minimap.prototype.setupDrawData = function () {
    var dataZones = this.gui.game.data.zones;
    
    for (var i = 0, len = dataZones.length; i < len; i++) {
        this.paths[i] = svgCreate(this.svg, 'path', {
            d: dataZones[i].path,
            fill: '#FFF',
            stroke: '#000',
            'stroke-width': 80
        });
    }
    
    this.areaRect = svgCreate(this.svg, 'rect', {
        x: 0,
        y: 0,
        width: 1,
        height: 1,
        fill: '#CCCCCC',
        'fill-opacity': 0.6
    });
};

Minimap.prototype.updateArea = function (x, y, width, height) {
    var ar = this.areaRect;
    ar.setAttribute('x', x);
    ar.setAttribute('y', y);
    ar.setAttribute('width', width);
    ar.setAttribute('height', height);
};