/** @constructor */
function TabbedView(gui) {
    this.gui = gui;
    this.tabLis = [];
    this.tabDivs = [];
    this.tabInstances = null;
    this.windowsContainer = null;
    this.activeTab = 0;
}

TabbedView.prototype.setup = function (tabs) {
    this.element = createGuiElement('tabbed-view');
    
    this.tabInstances = tabs.map(function (e) {
        return e.tab;
    });
    
    var tabsElem = this.createTabsElement(tabs);
    this.element.appendChild(tabsElem);
    
    var tabWindows = this.createTabWindows(tabs);
    this.element.appendChild(tabWindows);
    
    this.onResize();
};

TabbedView.prototype.onResize = function () {
    var style = this.element.style;
    var pos = this.gui.positions;
    style.width = pos.tabbedViewWidth + 'px';
    style.height = pos.uiDivideSize + 'px';
    
    this.windowsContainer.style.height = pos.tabsHeight + 'px';
    
    for (var i = 0, len = this.tabInstances.length; i < len; i++) {
        this.tabInstances[i].onResize(pos);
    }
};

TabbedView.prototype.createTabsElement = function (tabs) {
    var element = document.createElement('div');
    element.setAttribute('class', 'tabs unselectable');
    
    var ul = document.createElement('ul');
    element.appendChild(ul);
    
    var that = this;
    
    for (var i = 0, len = tabs.length; i < len; i++) {
        var li = document.createElement('li');
        ul.appendChild(li);
        addText(li, tabs[i].text);
        this.tabLis.push(li);
        
        li.addEventListener('mousedown', (function (i) {
            return function (e) {
                that.onTabActivated(i);
            };
        })(i), true);
    }
    
    this.tabLis[this.activeTab].setAttribute('class', 'active');
    
    var h1 = document.createElement('h1');
    element.appendChild(h1);
    addText(h1, STR.gameTitle);
    
    return element;
};

TabbedView.prototype.createTabWindows = function (tabs) {
    this.windowsContainer = document.createElement('div');
    this.windowsContainer.setAttribute('class', 'tab-windows');
    
    for (var i = 0, len = tabs.length; i < len; i++) {
        var tab = tabs[i].tab;
        var tabDiv = tab.setup(this.windowsContainer);
        this.tabDivs.push(tabDiv);
        tab.onResize(this.gui.positions);
    }
    
    this.tabDivs[0].style.display = 'block';
    
    return this.windowsContainer;
};

TabbedView.prototype.onTabActivated = function (i) {
    if (i === this.activeTab) {
        return;
    }
    
    this.tabLis[this.activeTab].removeAttribute('class');
    this.tabDivs[this.activeTab].style.display = 'none';
    this.activeTab = i;
    this.tabLis[this.activeTab].setAttribute('class', 'active');
    this.tabDivs[this.activeTab].style.display = 'block';
    
    this.tabInstances[i].onActivated();
};

TabbedView.prototype.makeTabActive = function (i) {
    this.onTabActivated(i);
};