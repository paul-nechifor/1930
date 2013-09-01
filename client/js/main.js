function main() {
    generateValues();
    checkRequirements(function () {
        var game = new Game();
        game.start();
    });
}

function generateValues() {
    initCodes();
    initColors();
}

function initCodes() {
    var codes = GEN.messageCodes;
    for (var i = 0, len = codes.length; i < len; i++) {
        MID[codes[i]] = i;
    }
}

function initColors() {
    var n = GEN.maximumPlayers;
    
    var hue = 0.0;
    var inc = 1.0 / n;
    
    for (var i = 0; i < n; i++) {
        COLORS.zoneFills.push(      hsvToRgbHex(hue, 0.6, 0.6));
        COLORS.centerFills.push(    hsvToRgbHex(hue, 0.6, 0.7));
        COLORS.arrowFills.push(     hsvToRgbHex(hue, 0.8, 0.7));
        COLORS.arrowStrokes.push(   hsvToRgbHex(hue, 0.6, 0.5));
        hue += inc;
    }
}

/**
 * Replace the colors for the player character so that he is different from all
 * the rest.
 */
function changePcColors(pcId) {
    COLORS.zoneFills[pcId]    = hsvToRgbHex(0.2, 0.05, 0.8);
    COLORS.centerFills[pcId]  = hsvToRgbHex(0.2, 0.05, 0.9);
    COLORS.arrowFills[pcId]   = hsvToRgbHex(0.2,  0.1, 0.9);
    COLORS.arrowStrokes[pcId] = hsvToRgbHex(0.2, 0.05, 0.7);
}

function checkRequirements(callback) {
    if (!('WebSocket' in window)) {
        alert(STR.noWebSockets);
        return;
    }
    
    callback();
}

main();