/** @constructor */
function Attack(victim, firstAggressor, secondsToAnswer) {
    this.victim = victim;
    this.firstAggressor = firstAggressor;
    this.secondsToAnswer = secondsToAnswer;
    this.otherAggressors = [];
}
