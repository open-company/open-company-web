'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _extends2 = require('../polyfills/extends');

var _extends3 = _interopRequireDefault(_extends2);

var _objectGetPrototypeOf = require('../polyfills/objectGetPrototypeOf');

var _objectGetPrototypeOf2 = _interopRequireDefault(_objectGetPrototypeOf);

var _classCallCheck2 = require('babel-runtime/helpers/classCallCheck');

var _classCallCheck3 = _interopRequireDefault(_classCallCheck2);

var _createClass2 = require('../polyfills/createClass');

var _createClass3 = _interopRequireDefault(_createClass2);

var _possibleConstructorReturn2 = require('../polyfills/possibleConstructorReturn');

var _possibleConstructorReturn3 = _interopRequireDefault(_possibleConstructorReturn2);

var _inherits2 = require('../polyfills/inherits');

var _inherits3 = _interopRequireDefault(_inherits2);

var _react = require('react');

var _react2 = _interopRequireDefault(_react);

var _propTypes = require('prop-types');

var _propTypes2 = _interopRequireDefault(_propTypes);

var _ = require('.');

var _utils = require('../utils');

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var Preview = function (_React$PureComponent) {
  (0, _inherits3.default)(Preview, _React$PureComponent);

  function Preview(props) {
    (0, _classCallCheck3.default)(this, Preview);

    var _this = (0, _possibleConstructorReturn3.default)(this, (Preview.__proto__ || (0, _objectGetPrototypeOf2.default)(Preview)).call(this, props));

    _this.state = { emoji: null };
    return _this;
  }

  (0, _createClass3.default)(Preview, [{
    key: 'render',
    value: function render() {
      var emoji = this.state.emoji;
      var _props = this.props;
      var emojiProps = _props.emojiProps;
      var skinsProps = _props.skinsProps;
      var title = _props.title;
      var idleEmoji = _props.emoji;


      if (emoji) {
        var emojiData = (0, _utils.getData)(emoji);
        var _emojiData$emoticons = emojiData.emoticons;
        var emoticons = _emojiData$emoticons === undefined ? [] : _emojiData$emoticons;
        var knownEmoticons = [];
        var listedEmoticons = [];

        emoticons.forEach(function (emoticon) {
          if (knownEmoticons.indexOf(emoticon.toLowerCase()) >= 0) {
            return;
          }

          knownEmoticons.push(emoticon.toLowerCase());
          listedEmoticons.push(emoticon);
        });

        return _react2.default.createElement(
          'div',
          { className: 'emoji-mart-preview' },
          _react2.default.createElement(
            'div',
            { className: 'emoji-mart-preview-emoji' },
            (0, _.Emoji)((0, _extends3.default)({ key: emoji.id, emoji: emoji }, emojiProps))
          ),
          _react2.default.createElement(
            'div',
            { className: 'emoji-mart-preview-data' },
            _react2.default.createElement(
              'div',
              { className: 'emoji-mart-preview-name' },
              emoji.name
            ),
            _react2.default.createElement(
              'div',
              { className: 'emoji-mart-preview-shortnames' },
              emojiData.short_names.map(function (short_name) {
                return _react2.default.createElement(
                  'span',
                  { key: short_name, className: 'emoji-mart-preview-shortname' },
                  ':',
                  short_name,
                  ':'
                );
              })
            ),
            _react2.default.createElement(
              'div',
              { className: 'emoji-mart-preview-emoticons' },
              listedEmoticons.map(function (emoticon) {
                return _react2.default.createElement(
                  'span',
                  { key: emoticon, className: 'emoji-mart-preview-emoticon' },
                  emoticon
                );
              })
            )
          )
        );
      } else {
        return _react2.default.createElement(
          'div',
          { className: 'emoji-mart-preview' },
          _react2.default.createElement(
            'div',
            { className: 'emoji-mart-preview-emoji' },
            idleEmoji && idleEmoji.length && (0, _.Emoji)((0, _extends3.default)({ emoji: idleEmoji }, emojiProps))
          ),
          _react2.default.createElement(
            'div',
            { className: 'emoji-mart-preview-data' },
            _react2.default.createElement(
              'span',
              { className: 'emoji-mart-title-label' },
              title
            )
          ),
          _react2.default.createElement(
            'div',
            { className: 'emoji-mart-preview-skins' },
            _react2.default.createElement(_.Skins, skinsProps)
          )
        );
      }
    }
  }]);
  return Preview;
}(_react2.default.PureComponent);

exports.default = Preview;