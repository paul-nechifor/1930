var COLORS = {
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
    noWebSockets: 'Navigatorul tău n-are WebSockets. Ia Google Chrome!'
};

// Message IDs. This is populated at start up.
var MID = {};