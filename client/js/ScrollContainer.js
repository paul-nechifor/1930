/** @constructor */
function ScrollContainer() {
    this.outer = null;
    this.inner = null;
    this.bar = null;
    
    this.isSelectable = true;
    
    this.innerHeight = 0;
    this.outerHeight = 0;
    this.barHeight = 0;
    
    this.progress = 1;
    this.progressIncrement = 0;
    this.scrollPos = 0;
    this.maxScroll = 0;
    this.barPos = 0;
    this.maxBarScroll = 0;
}

ScrollContainer.prototype.setup = function (parent) {
    this.outer = document.createElement('div');
    parent.appendChild(this.outer);
    this.outer.setAttribute('class', 'scroll-outer');
    this.outer.style.height = this.outerHeight + 'px';
    
    this.inner = document.createElement('div');
    this.outer.appendChild(this.inner);
    this.inner.setAttribute('class', 'scroll-inner');
    
    this.bar = document.createElement('div');
    this.outer.appendChild(this.bar);
    this.bar.setAttribute('class', 'scroll-bar');
    
    var onMouseWheel = this.onMouseWheel.bind(this);
    this.outer.addEventListener('mousewheel', onMouseWheel, true);
    this.outer.addEventListener('DOMMouseScroll', onMouseWheel, true);
    
    this.bar.addEventListener('mousedown', this.onMouseDown.bind(this), true);
};

ScrollContainer.prototype.innerChanged = function () {
    this.outerHeight = this.outer.clientHeight;
    this.innerHeight = this.inner.clientHeight;
    
    this.maxScroll = this.innerHeight - this.outerHeight;
    if (this.maxScroll < 0) {
        this.maxScroll = 0;
    }
    
    this.progressIncrement = DIMS.scrollIncrement / this.innerHeight;
    
    this.barHeight = (this.outerHeight / this.innerHeight) * this.outerHeight;
    if (this.barHeight < DIMS.minScrollBarHeight) {
        this.barHeight = DIMS.minScrollBarHeight;
    }
    
    this.maxBarScroll = this.outerHeight - this.barHeight;
        
    this.bar.style.height = this.barHeight + 'px';
};

/**
 * Notify that the inner div has changed and that it should scroll to bottom
 * if it was there before the update.
 */
ScrollContainer.prototype.innerChangedAndToBottom = function (force) {
    var wasAtBottom = this.progress >= 1.0;
    this.innerChanged();

    if (wasAtBottom || force) {
        this.scroll(1.0);
    }
};

ScrollContainer.prototype.scroll = function (progress) {
    this.progress = progress;
    this.scrollPos = progress * this.maxScroll;
    this.inner.style.top = -this.scrollPos + 'px';
    
    this.barPos = this.maxBarScroll * progress;
    this.bar.style.top = this.barPos + 'px';
};

ScrollContainer.prototype.onMouseWheel = function (e) {
    // -1 or 1
    var delta = Math.max(-1, Math.min(1, (e.wheelDelta || -e.detail)));

    var progress = this.progress - delta * this.progressIncrement;
    if (progress < 0) {
        progress = 0;
    } else if (progress > 1) {
        progress = 1;
    }
    
    this.scroll(progress);
};

ScrollContainer.prototype.onMouseDown = function (e) {
    var startY = e.pageY;
    var startBarPos = this.barPos;
    var that = this;
    
    this.setSelectable(false);
    
    var onMove = function (e) {
        var totalY = e.pageY - startY;
        var barPos = startBarPos + totalY;
        if (barPos < 0) {
            barPos = 0;
        } else if (barPos > that.maxBarScroll) {
            barPos = that.maxBarScroll;
        }
        that.scroll(barPos / that.maxBarScroll);
    };
    
    var onUp = function (e) {
        that.setSelectable(true);
        that.outer.removeEventListener('mousemove', onMove, false);
        window.removeEventListener('mouseup', onUp, false);
    };
    
    this.outer.addEventListener('mousemove', onMove, false);
    window.addEventListener('mouseup', onUp, false);
};

ScrollContainer.prototype.setSelectable = function (on) {
    if (on) {
        if (!this.isSelectable) {
            this.outer.setAttribute('class', 'scroll-outer');
            this.isSelectable = on;
        }
    } else {
        if (this.isSelectable) {
            this.outer.setAttribute('class', 'scroll-outer unselectable');
            this.isSelectable = on;
        }
    }
}