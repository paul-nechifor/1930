/** @constructor */
function Player(opts) {
    this.id = opts.id;
    this.name = opts.name;
    this.isHuman = opts.isHuman;
    this.zones = {};
}