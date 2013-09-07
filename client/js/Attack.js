/** @constructor */
function Attack(victim, firstAggressor, secondsToAnswer) {
    this.victim = victim;
    this.firstAggressor = firstAggressor;
    this.otherAggressors = [];
    this.deadline = Date.now() + secondsToAnswer * 1000;
}
