/** @constructor */
function Player(opts) {
    this.id = opts.id;
    this.name = opts.name;
    this.isRobot = opts.isRobot;
    this.zones = {};
}