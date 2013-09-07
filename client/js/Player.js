/** @constructor */
function Player(opts) {
    this.id = opts.id;
    this.name = opts.name;
    this.isHuman = opts.isHuman;
    this.zones = {};
    this.nZones = 0;
    this.rank = null;
}

// This both adds the zone to this player and removes it from the previous owner
// (if there was one).
Player.prototype.addZone = function (zone) {
    this.nZones++;
    this.zones[zone.id] = zone;
    this.rank = chooseValue(this.nZones, STR.ranks);
    
    var prevOwner = zone.owner;
    if (prevOwner !== null) {
        prevOwner.removeZone(zone);
    }
    
    zone.setNewOwner(this);
};

Player.prototype.removeZone = function (zone) {
    this.nZones--;
    delete this.zones[zone.id];
    this.rank = chooseValue(this.nZones, STR.ranks);
};