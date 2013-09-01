/** @constructor */
function Positions(data) {
    this.contextWidth = 200;
    this.tabsPadding = 4;
    this.chatInputHeight = 24;
    
    this.windowWidth = -1;
    this.windowHeight = -1;
    this.uiDivideSize = -1;
    this.uiDivideY = -1;
    this.minimapWidth = -1;
    
    this.mapRatio = data.size[0] / data.size[1];
}

Positions.prototype.realign = function (windowWidth, windowHeight) {
    this.windowWidth = windowWidth;
    this.windowHeight = windowHeight
    this.uiDivideSize = chooseSize(windowHeight, DIMS.uiDivideSizes);
    this.uiDivideY = windowHeight - this.uiDivideSize;
    this.minimapWidth = (this.uiDivideSize * this.mapRatio) | 0;
    this.tabbedViewWidth = windowWidth - this.minimapWidth - this.contextWidth;
    this.tabsHeight = this.uiDivideSize - 20;
}