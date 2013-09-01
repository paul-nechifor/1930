/** @constructor */
function MapCamera(map) {
    this.map = map;
    this.pos = null;
    
    // The size of the map in the units used in the SVG paths.
    this.sizeX = -1;
    this.sizeY = -1;
    
    // The arguments for the view box in map units.
    this.offsetX = 0;
    this.offsetY = 0;
    this.canvasW = -1;
    this.canvasH = -1;
    
    // The center of the screen in map units and the zoom leve. These are the
    // ones that should be edited.
    this.focusX = -1;
    this.focusY = -1;
    this.zoom = 7;
}

MapCamera.prototype.setup = function () {
    this.pos = this.map.gui.positions;
    
    var data = this.map.gui.game.data;
    
    this.sizeX = data.size[0];
    this.sizeY = data.size[1];
    
    this.focusX = this.sizeX / 2;
    this.focusY = this.sizeY / 2;
};

MapCamera.prototype.update = function () {
    this.canvasW = this.pos.windowWidth * this.zoom;
    this.canvasH = this.pos.uiDivideY * this.zoom;
    
    var w2 = this.canvasW / 2;
    var h2 = this.canvasH / 2;
    
    if (this.focusX + w2 > this.sizeX) {
        this.offsetX = this.sizeX - this.canvasW;
    } else if (this.focusX - w2 < 0) {
        this.offsetX = 0;
    } else {
        this.offsetX = this.focusX - w2;
    }
    
    if (this.focusY + h2 > this.sizeY) {
        this.offsetY = this.sizeY - this.canvasH;
    } else if (this.focusY - h2 < 0) {
        this.offsetY = 0;
    } else {
        this.offsetY = this.focusY - h2;
    }
    
    setSvgViewBox(this.map.svg, this.offsetX, this.offsetY,
            this.canvasW, this.canvasH);
};
