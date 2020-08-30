goog.provide("goog.result");
goog.require("goog.array");
goog.require("goog.result.DependentResult");
goog.require("goog.result.Result");
goog.require("goog.result.SimpleResult");
goog.result.successfulResult = function(value) {
  var result = new goog.result.SimpleResult;
  result.setValue(value);
  return result;
};
goog.result.failedResult = function(opt_error) {
  var result = new goog.result.SimpleResult;
  result.setError(opt_error);
  return result;
};
goog.result.canceledResult = function() {
  var result = new goog.result.SimpleResult;
  result.cancel();
  return result;
};
goog.result.wait = function(result, handler, opt_scope) {
  result.wait(handler, opt_scope);
};
goog.result.waitOnSuccess = function(result, handler, opt_scope) {
  goog.result.wait(result, function(res) {
    if (res.getState() == goog.result.Result.State.SUCCESS) {
      handler.call(this, res.getValue(), res);
    }
  }, opt_scope);
};
goog.result.waitOnError = function(result, handler, opt_scope) {
  goog.result.wait(result, function(res) {
    if (res.getState() == goog.result.Result.State.ERROR) {
      handler.call(this, res.getError(), res);
    }
  }, opt_scope);
};
goog.result.transform = function(result, transformer) {
  var returnedResult = new goog.result.DependentResultImpl_([result]);
  goog.result.wait(result, function(res) {
    if (res.getState() == goog.result.Result.State.SUCCESS) {
      returnedResult.setValue(transformer(res.getValue()));
    } else {
      returnedResult.setError(res.getError());
    }
  });
  return returnedResult;
};
goog.result.chain = function(result, actionCallback, opt_scope) {
  var dependentResult = new goog.result.DependentResultImpl_([result]);
  goog.result.wait(result, function(result) {
    if (result.getState() == goog.result.Result.State.SUCCESS) {
      var contingentResult = actionCallback.call(opt_scope, result);
      dependentResult.addParentResult(contingentResult);
      goog.result.wait(contingentResult, function(contingentResult) {
        if (contingentResult.getState() == goog.result.Result.State.SUCCESS) {
          dependentResult.setValue(contingentResult.getValue());
        } else {
          dependentResult.setError(contingentResult.getError());
        }
      });
    } else {
      dependentResult.setError(result.getError());
    }
  });
  return dependentResult;
};
goog.result.combine = function(var_args) {
  var results = goog.array.clone(arguments);
  var combinedResult = new goog.result.DependentResultImpl_(results);
  var isResolved = function(res) {
    return res.getState() != goog.result.Result.State.PENDING;
  };
  var checkResults = function() {
    if (combinedResult.getState() == goog.result.Result.State.PENDING && goog.array.every(results, isResolved)) {
      combinedResult.setValue(results);
    }
  };
  goog.array.forEach(results, function(result) {
    goog.result.wait(result, checkResults);
  });
  return combinedResult;
};
goog.result.combineOnSuccess = function(var_args) {
  var results = goog.array.clone(arguments);
  var combinedResult = new goog.result.DependentResultImpl_(results);
  var resolvedSuccessfully = function(res) {
    return res.getState() == goog.result.Result.State.SUCCESS;
  };
  goog.result.wait(goog.result.combine.apply(goog.result.combine, results), function(res) {
    var results = res.getValue();
    if (goog.array.every(results, resolvedSuccessfully)) {
      combinedResult.setValue(results);
    } else {
      combinedResult.setError(results);
    }
  });
  return combinedResult;
};
goog.result.cancelParentResults = function(dependentResult) {
  var anyCanceled = false;
  var results = dependentResult.getParentResults();
  for (var n = 0; n < results.length; n++) {
    anyCanceled |= results[n].cancel();
  }
  return !!anyCanceled;
};
goog.result.DependentResultImpl_ = function(parentResults) {
  goog.result.DependentResultImpl_.base(this, "constructor");
  this.parentResults_ = parentResults;
};
goog.inherits(goog.result.DependentResultImpl_, goog.result.SimpleResult);
goog.result.DependentResultImpl_.prototype.addParentResult = function(parentResult) {
  this.parentResults_.push(parentResult);
};
goog.result.DependentResultImpl_.prototype.getParentResults = function() {
  return this.parentResults_;
};

//# sourceMappingURL=goog.result.resultutil.js.map
