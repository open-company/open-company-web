import _extends from '../polyfills/extends';
import _Object$getPrototypeOf from '../polyfills/objectGetPrototypeOf';
import _classCallCheck from 'babel-runtime/helpers/classCallCheck';
import _createClass from '../polyfills/createClass';
import _possibleConstructorReturn from '../polyfills/possibleConstructorReturn';
import _inherits from '../polyfills/inherits';
import React from 'react';
import PropTypes from 'prop-types';

import { Emoji, Skins } from '.';
import { getData } from '../utils';

var Preview = function (_React$PureComponent) {
  _inherits(Preview, _React$PureComponent);

  function Preview(props) {
    _classCallCheck(this, Preview);

    var _this = _possibleConstructorReturn(this, (Preview.__proto__ || _Object$getPrototypeOf(Preview)).call(this, props));

    _this.state = { emoji: null };
    return _this;
  }

  _createClass(Preview, [{
    key: 'render',
    value: function render() {
      var emoji = this.state.emoji;
      var _props = this.props;
      var emojiProps = _props.emojiProps;
      var skinsProps = _props.skinsProps;
      var title = _props.title;
      var idleEmoji = _props.emoji;


      if (emoji) {
        var emojiData = getData(emoji);
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

        return React.createElement(
          'div',
          { className: 'emoji-mart-preview' },
          React.createElement(
            'div',
            { className: 'emoji-mart-preview-emoji' },
            Emoji(_extends({ key: emoji.id, emoji: emoji }, emojiProps))
          ),
          React.createElement(
            'div',
            { className: 'emoji-mart-preview-data' },
            React.createElement(
              'div',
              { className: 'emoji-mart-preview-name' },
              emoji.name
            ),
            React.createElement(
              'div',
              { className: 'emoji-mart-preview-shortnames' },
              emojiData.short_names.map(function (short_name) {
                return React.createElement(
                  'span',
                  { key: short_name, className: 'emoji-mart-preview-shortname' },
                  ':',
                  short_name,
                  ':'
                );
              })
            ),
            React.createElement(
              'div',
              { className: 'emoji-mart-preview-emoticons' },
              listedEmoticons.map(function (emoticon) {
                return React.createElement(
                  'span',
                  { key: emoticon, className: 'emoji-mart-preview-emoticon' },
                  emoticon
                );
              })
            )
          )
        );
      } else {
        return React.createElement(
          'div',
          { className: 'emoji-mart-preview' },
          React.createElement(
            'div',
            { className: 'emoji-mart-preview-emoji' },
            idleEmoji && idleEmoji.length && Emoji(_extends({ emoji: idleEmoji }, emojiProps))
          ),
          React.createElement(
            'div',
            { className: 'emoji-mart-preview-data' },
            React.createElement(
              'span',
              { className: 'emoji-mart-title-label' },
              title
            )
          ),
          React.createElement(
            'div',
            { className: 'emoji-mart-preview-skins' },
            React.createElement(Skins, skinsProps)
          )
        );
      }
    }
  }]);

  return Preview;
}(React.PureComponent);

export default Preview;