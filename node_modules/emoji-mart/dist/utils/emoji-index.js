'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _data = require('../data');

var _data2 = _interopRequireDefault(_data);

var _ = require('.');

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var originalPool = {};
var index = {};
var emojisList = {};
var emoticonsList = {};

var _loop = function _loop(emoji) {
  var emojiData = _data2.default.emojis[emoji];
  var short_names = emojiData.short_names;
  var emoticons = emojiData.emoticons;
  var id = short_names[0];

  if (emoticons) {
    emoticons.forEach(function (emoticon) {
      if (emoticonsList[emoticon]) {
        return;
      }

      emoticonsList[emoticon] = id;
    });
  }

  emojisList[id] = (0, _.getSanitizedData)(id);
  originalPool[id] = emojiData;
};

for (var emoji in _data2.default.emojis) {
  _loop(emoji);
}

function addCustomToPool(custom, pool) {
  custom.forEach(function (emoji) {
    var emojiId = emoji.id || emoji.short_names[0];

    if (emojiId && !pool[emojiId]) {
      pool[emojiId] = (0, _.getData)(emoji);
      emojisList[emojiId] = (0, _.getSanitizedData)(emoji);
    }
  });
}

function search(value) {
  var _ref = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};

  var emojisToShowFilter = _ref.emojisToShowFilter;
  var maxResults = _ref.maxResults;
  var include = _ref.include;
  var exclude = _ref.exclude;
  var _ref$custom = _ref.custom;
  var custom = _ref$custom === undefined ? [] : _ref$custom;

  addCustomToPool(custom, originalPool);

  maxResults || (maxResults = 75);
  include || (include = []);
  exclude || (exclude = []);

  var results = null,
      pool = originalPool;

  if (value.length) {
    if (value == '-' || value == '-1') {
      return [emojisList['-1']];
    }

    var values = value.toLowerCase().split(/[\s|,|\-|_]+/),
        allResults = [];

    if (values.length > 2) {
      values = [values[0], values[1]];
    }

    if (include.length || exclude.length) {
      pool = {};

      _data2.default.categories.forEach(function (category) {
        var isIncluded = include && include.length ? include.indexOf(category.name.toLowerCase()) > -1 : true;
        var isExcluded = exclude && exclude.length ? exclude.indexOf(category.name.toLowerCase()) > -1 : false;
        if (!isIncluded || isExcluded) {
          return;
        }

        category.emojis.forEach(function (emojiId) {
          return pool[emojiId] = _data2.default.emojis[emojiId];
        });
      });

      if (custom.length) {
        var customIsIncluded = include && include.length ? include.indexOf('custom') > -1 : true;
        var customIsExcluded = exclude && exclude.length ? exclude.indexOf('custom') > -1 : false;
        if (customIsIncluded && !customIsExcluded) {
          addCustomToPool(custom, pool);
        }
      }
    }

    allResults = values.map(function (value) {
      var aPool = pool,
          aIndex = index,
          length = 0;

      for (var charIndex = 0; charIndex < value.length; charIndex++) {
        var char = value[charIndex];
        length++;

        aIndex[char] || (aIndex[char] = {});
        aIndex = aIndex[char];

        if (!aIndex.results) {
          (function () {
            var scores = {};

            aIndex.results = [];
            aIndex.pool = {};

            for (var _id in aPool) {
              var emoji = aPool[_id];
              var _search = emoji.search;
              var sub = value.substr(0, length);
              var subIndex = _search.indexOf(sub);

              if (subIndex != -1) {
                var score = subIndex + 1;
                if (sub == _id) score = 0;

                aIndex.results.push(emojisList[_id]);
                aIndex.pool[_id] = emoji;

                scores[_id] = score;
              }
            }

            aIndex.results.sort(function (a, b) {
              var aScore = scores[a.id],
                  bScore = scores[b.id];

              return aScore - bScore;
            });
          })();
        }

        aPool = aIndex.pool;
      }

      return aIndex.results;
    }).filter(function (a) {
      return a;
    });

    if (allResults.length > 1) {
      results = _.intersect.apply(null, allResults);
    } else if (allResults.length) {
      results = allResults[0];
    } else {
      results = [];
    }
  }

  if (results) {
    if (emojisToShowFilter) {
      results = results.filter(function (result) {
        return emojisToShowFilter(_data2.default.emojis[result.id]);
      });
    }

    if (results && results.length > maxResults) {
      results = results.slice(0, maxResults);
    }
  }

  return results;
}

exports.default = { search: search, emojis: emojisList, emoticons: emoticonsList };