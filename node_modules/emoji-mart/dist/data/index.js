'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _buildSearch = require('../utils/build-search');

var _buildSearch2 = _interopRequireDefault(_buildSearch);

var _data = require('./data');

var _data2 = _interopRequireDefault(_data);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function uncompress(list) {
  for (var short_name in list) {
    var datum = list[short_name];

    if (!datum.short_names) datum.short_names = [];
    datum.short_names.unshift(short_name);

    datum.sheet_x = datum.sheet[0];
    datum.sheet_y = datum.sheet[1];
    delete datum.sheet;

    if (!datum.text) datum.text = '';
    if (datum.added_in !== null && !datum.added_in) datum.added_in = '6.0';

    datum.search = (0, _buildSearch2.default)({
      short_names: datum.short_names,
      name: datum.name,
      keywords: datum.keywords,
      emoticons: datum.emoticons
    });
  }
}

uncompress(_data2.default.emojis);
uncompress(_data2.default.skins);

exports.default = _data2.default;