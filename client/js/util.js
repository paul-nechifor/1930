var SVGNS = 'http://www.w3.org/2000/svg';

function createGuiElement(id) {
    var element = document.createElement('div');
    document.body.appendChild(element);
    element.setAttribute('id', id);
    return element;
}

function svgCreate(parent, type, props) {
    var element = document.createElementNS(SVGNS, type);
    parent.appendChild(element);
    
    for (var key in props) {
        element.setAttribute(key, props[key]);
    }
    
    return element;
}

function setSvgViewBox(svg, x, y, w, h) {
    svg.setAttribute('viewBox', x + ' ' + y + ' ' + w + ' ' + h);
}

function getJson(url, callback) {
    var req = new XMLHttpRequest();
    req.open('GET', url, true);
    req.onload = function () {
        callback(JSON.parse(req.responseText));
    };
    req.send(null);
}

function createClassedSpan(parent, cls, text) {
    var span = document.createElement('span')
    parent.appendChild(span);
    span.setAttribute('class', cls);
    span.appendChild(document.createTextNode(text));
    return span;
}

function createTabDiv(parent) {    
    var div = document.createElement('div');
    parent.appendChild(div);
    div.style.width = '100%';
    div.style.height = '100%';
    div.style.display = 'none';
    
    return div;
}

function hsvToRgbHex(h, s, v) {
    var r, g, b, i, f, p, q, t;
    
    i = Math.floor(h * 6);
    f = h * 6 - i;
    p = v * (1 - s);
    q = v * (1 - f * s);
    t = v * (1 - (1 - f) * s);
    
    switch (i % 6) {
        case 0: r = v, g = t, b = p; break;
        case 1: r = q, g = v, b = p; break;
        case 2: r = p, g = v, b = t; break;
        case 3: r = p, g = q, b = v; break;
        case 4: r = t, g = p, b = v; break;
        case 5: r = v, g = p, b = q; break;
    }
    
    return '#' + Math.floor(r * 255).toString(16)
        + Math.floor(g * 255).toString(16)
        + Math.floor(b * 255).toString(16);
}

/**
 * This is the equivalent of a if-elseif-else chain.
 * if size < array[0] then array[1] else if ... else array[n-1].
 */
function chooseSize(size, array) {
    for (var i = 0, len = array.length; i < len; i += 2) {
        if (size < array[i]) {
            return array[i + 1];
        }
    }
    
    return array[array.length - 1];
}