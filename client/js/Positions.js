/** @constructor */
function Positions(data) {
    this.contextWidth = 200;
    this.tabsPadding = 4;
    this.chatInputHeight = 24;
    this.topViewContentHeight = 40;
    this.topViewPadding = 6;
    
    this.windowWidth = -1;
    this.windowHeight = -1;
    this.uiDivideSize = -1;
    this.uiDivideY = -1;
    this.minimapWidth = -1;
    this.tabbedViewWidth = -1;
    this.tabsHeight = -1;
    this.attackViewTop = -1;
    this.attackViewHeight = -1;
    this.attackViewPadding = -1;
    this.attackViewBigText = -1;
    this.answerWidth = -1;
    this.answerPadding = -1;
    this.answerFontSize = -1;
    
    this.mapRatio = data.size[0] / data.size[1];
}

Positions.prototype.realign = function (windowWidth, windowHeight) {
    this.windowWidth = windowWidth;
    this.windowHeight = windowHeight;
    this.uiDivideSize = chooseSize(windowHeight, DIMS.uiDivideSizes);
    this.uiDivideY = windowHeight - this.uiDivideSize;
    this.minimapWidth = (this.uiDivideSize * this.mapRatio) | 0;
    this.tabbedViewWidth = windowWidth - this.minimapWidth - this.contextWidth;
    this.tabsHeight = this.uiDivideSize - 20;
    this.attackViewTop = this.topViewContentHeight + 2 * this.topViewPadding;
    this.attackViewHeight = windowHeight - this.attackViewTop
            - this.uiDivideSize;
    this.attackViewPadding = chooseSize(windowHeight, DIMS.attackViewPaddings);
    this.attackViewBigText = chooseSize(windowHeight, DIMS.attackViewBigTexts);
    this.answerWidth = windowWidth - 3 * this.attackViewPadding;
    this.answerPadding = chooseSize(windowHeight, DIMS.answerPaddings);
    this.answerFontSize = chooseSize(windowHeight, DIMS.answerFontSizes);
}