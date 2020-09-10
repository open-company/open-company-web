import _Object$getPrototypeOf from '../polyfills/objectGetPrototypeOf';
import _classCallCheck from 'babel-runtime/helpers/classCallCheck';
import _createClass from '../polyfills/createClass';
import _possibleConstructorReturn from '../polyfills/possibleConstructorReturn';
import _inherits from '../polyfills/inherits';
import React from 'react';
import PropTypes from 'prop-types';

var Skins = function (_React$PureComponent) {
  _inherits(Skins, _React$PureComponent);

  function Skins(props) {
    _classCallCheck(this, Skins);

    var _this = _possibleConstructorReturn(this, (Skins.__proto__ || _Object$getPrototypeOf(Skins)).call(this, props));

    _this.state = {
      opened: false
    };

    _this.handleClick = _this.handleClick.bind(_this);
    return _this;
  }

  _createClass(Skins, [{
    key: 'handleClick',
    value: function handleClick(e) {
      var skin = e.currentTarget.getAttribute('data-skin');
      var onChange = this.props.onChange;


      if (!this.state.opened) {
        this.setState({ opened: true });
      } else {
        this.setState({ opened: false });
        if (skin != this.props.skin) {
          onChange(skin);
        }
      }
    }
  }, {
    key: 'render',
    value: function render() {
      var skin = this.props.skin;
      var opened = this.state.opened;


      var skinToneNodes = [];

      for (var i = 0; i < 6; i++) {
        var skinTone = i + 1;
        var selected = skinTone == skin;

        skinToneNodes.push(React.createElement(
          'span',
          {
            key: 'skin-tone-' + skinTone,
            className: 'emoji-mart-skin-swatch ' + (selected ? 'emoji-mart-skin-swatch-selected' : '')
          },
          React.createElement('span', {
            onClick: this.handleClick,
            'data-skin': skinTone,
            className: 'emoji-mart-skin emoji-mart-skin-tone-' + skinTone
          })
        ));
      }

      return React.createElement(
        'div',
        null,
        React.createElement(
          'div',
          {
            className: 'emoji-mart-skin-swatches ' + (opened ? 'emoji-mart-skin-swatches-opened' : '')
          },
          skinToneNodes
        )
      );
    }
  }]);

  return Skins;
}(React.PureComponent);

export default Skins;


Skins.defaultProps = {
  onChange: function onChange() {}
};