var COLORS = {
    lightDark: '#444',
    zoneStartFill: '#CCDDEE',
    zoneStartStroke: '#EEEEEE',
    zoneNameFontColor: '#FFFFFF',
    
    // These are generated on startup and depend on the maximum number of
    // players.
    zoneFills: [],
    centerFills: [],
    arrowFills: [],
    arrowStrokes: []
};

var DIMS = {
    zoneStrokeWidth: 30,
    zoneCenterRadius: 100,
    zoneNameFontSize: 90,
    arrowHeadHeight: 100,
    arrowBodyWidth: 50,
    arrowHeadOverflow: 30,
    minScrollBarHeight: 20,
    scrollIncrement: 15,
    nudgeIncrement: 200,
    cameraZoomValues: [3, 5, 7, 9, 11, 13],
    uiDivideSizes: [400, 150, 600, 200, 800, 250, 1000, 280, 320]
};

var VALS = {
    fontFamily: 'Helvetica, Arial'
};

var STR = {
    gameTitle: 'România 1930',
    yours: '(al tău)',
    cannotBeAttacked: '(imun)',
    youAreAttackingHim: '(îl ataci)',
    attack: 'Atacă!',
    connectionClosed: 'S-a închis conexiunea.',
    connectionError: 'Eroare de conexiune.',
    noMoreFreeSpots: 'Nu sunt locuri libere în joc. Revin-o mai târziu.',
    gameStarted: 'A început jocul.',
    robot: ' (robot)',
    me: ' (eu)',
    messages: 'Mesaje',
    others: 'Alții',
    self: 'Tu',
    messageToSend: 'mesajul de trimis',
    noWebSockets: 'Navigatorul tău n-are WebSockets. Ia Google Chrome!',
    replaceHumanWithHuman: 'Jucătorul {1} a fost înlocuit de jucătorul {2}.',
    replaceHumanWithRobot: 'Jucătorul {1} a fost înlocuit de robotul {2}.',
    replaceRobotWithHuman: 'Robotul {1} a fost înlocuit de jucătorul {2}.',
    replaceRobotWithRobot: 'Robotul {1} a fost înlocuit de robotul {2}.',
    hasAttacked: '{1} a atacat plasa „{2}“ a lui {3}.',
    ranks: [
        1, 'soldat',
        5, 'caporal',
        10, 'sergent',
        15, 'plutonier',
        20, 'maistru',
        30, 'locotenent',
        50, 'căpitan',
        80, 'maior',
        100, 'colonel',
        150, 'general',
        'mareșal'
    ]
};

// Message IDs. This is populated at start up.
var MID = {};