'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _react = require('react');

var _react2 = _interopRequireDefault(_react);

var _propTypes = require('prop-types');

var _propTypes2 = _interopRequireDefault(_propTypes);

var _data = require('../data');

var _data2 = _interopRequireDefault(_data);

var _utils = require('../utils');

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var SHEET_COLUMNS = 49;

var _getPosition = function _getPosition(props) {
  var _getData2 = _getData(props);

  var sheet_x = _getData2.sheet_x;
  var sheet_y = _getData2.sheet_y;
  var multiply = 100 / (SHEET_COLUMNS - 1);

  return multiply * sheet_x + '% ' + multiply * sheet_y + '%';
};

var _getData = function _getData(props) {
  var emoji = props.emoji;
  var skin = props.skin;
  var set = props.set;

  return (0, _utils.getData)(emoji, skin, set);
};

var _getSanitizedData = function _getSanitizedData(props) {
  var emoji = props.emoji;
  var skin = props.skin;
  var set = props.set;

  return (0, _utils.getSanitizedData)(emoji, skin, set);
};

var _handleClick = function _handleClick(e, props) {
  if (!props.onClick) {
    return;
  }
  var onClick = props.onClick;
  var emoji = _getSanitizedData(props);

  onClick(emoji, e);
};

var _handleOver = function _handleOver(e, props) {
  if (!props.onOver) {
    return;
  }
  var onOver = props.onOver;
  var emoji = _getSanitizedData(props);

  onOver(emoji, e);
};

var _handleLeave = function _handleLeave(e, props) {
  if (!props.onLeave) {
    return;
  }
  var onLeave = props.onLeave;
  var emoji = _getSanitizedData(props);

  onLeave(emoji, e);
};

var Emoji = function Emoji(props) {
  for (var k in Emoji.defaultProps) {
    if (props[k] == undefined && Emoji.defaultProps[k] != undefined) {
      props[k] = Emoji.defaultProps[k];
    }
  }

  var data = _getData(props);
  if (!data) {
    return null;
  }

  var unified = data.unified;
  var custom = data.custom;
  var short_names = data.short_names;
  var imageUrl = data.imageUrl;
  var style = {};
  var children = props.children;
  var className = 'emoji-mart-emoji';
  var title = null;

  if (!unified && !custom) {
    return null;
  }

  if (props.tooltip) {
    title = short_names[0];
  }

  if (props.native && unified) {
    className += ' emoji-mart-emoji-native';
    style = { fontSize: props.size };
    children = (0, _utils.unifiedToNative)(unified);

    if (props.forceSize) {
      style.display = 'inline-block';
      style.width = props.size;
      style.height = props.size;
    }
  } else if (custom) {
    className += ' emoji-mart-emoji-custom';
    style = {
      width: props.size,
      height: props.size,
      display: 'inline-block',
      backgroundImage: 'url(' + imageUrl + ')',
      backgroundSize: 'contain'
    };
  } else {
    var setHasEmoji = _getData(props)['has_img_' + props.set];

    if (!setHasEmoji) {
      return null;
    }

    style = {
      width: props.size,
      height: props.size,
      display: 'inline-block',
      backgroundImage: 'url(' + props.backgroundImageFn(props.set, props.sheetSize) + ')',
      backgroundSize: 100 * SHEET_COLUMNS + '%',
      backgroundPosition: _getPosition(props)
    };
  }

  return _react2.default.createElement(
    'span',
    {
      key: props.emoji.id || props.emoji,
      onClick: function onClick(e) {
        return _handleClick(e, props);
      },
      onMouseEnter: function onMouseEnter(e) {
        return _handleOver(e, props);
      },
      onMouseLeave: function onMouseLeave(e) {
        return _handleLeave(e, props);
      },
      title: title,
      className: className
    },
    _react2.default.createElement(
      'span',
      { style: style },
      children
    )
  );
};

Emoji.defaultProps = {
  skin: 1,
  set: 'apple',
  sheetSize: 64,
  native: false,
  forceSize: false,
  tooltip: false,
  backgroundImageFn: function backgroundImageFn(set, sheetSize) {
    return 'https://unpkg.com/emoji-datasource-' + set + '@' + '3.0.0' + '/img/' + set + '/sheets/' + sheetSize + '.png';
  },
  onOver: function onOver() {},
  onLeave: function onLeave() {},
  onClick: function onClick() {}
};

exports.default = Emoji;