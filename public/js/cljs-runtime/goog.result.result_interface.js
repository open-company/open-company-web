goog.provide("goog.result.Result");
goog.require("goog.Thenable");
goog.result.Result = function() {
};
goog.result.Result.prototype.wait = function(handler, opt_scope) {
};
goog.result.Result.State = {SUCCESS:"success", ERROR:"error", PENDING:"pending"};
goog.result.Result.prototype.getState = function() {
};
goog.result.Result.prototype.getValue = function() {
};
goog.result.Result.prototype.getError = function() {
};
goog.result.Result.prototype.cancel = function() {
};
goog.result.Result.prototype.isCanceled = function() {
};
goog.result.Result.CancelError = function() {
};
goog.inherits(goog.result.Result.CancelError, Error);

//# sourceMappingURL=goog.result.result_interface.js.map
