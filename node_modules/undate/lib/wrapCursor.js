'use strict';

Object.defineProperty(exports, "__esModule", {
      value: true
});

exports.default = function (el, before, after) {
      var initEnd = el.selectionEnd,
          headToCursor = el.value.substr(0, el.selectionStart) + before,
          cursorToTail = el.value.substring(el.selectionStart, initEnd) + (after || '') + el.value.substr(initEnd);
      (0, _update2.default)(el, headToCursor, cursorToTail);
      el.selectionEnd = initEnd + before.length;
      return el;
};

var _update = require('./update');

var _update2 = _interopRequireDefault(_update);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }