import _Object$getPrototypeOf from '../polyfills/objectGetPrototypeOf';
import _classCallCheck from 'babel-runtime/helpers/classCallCheck';
import _createClass from '../polyfills/createClass';
import _possibleConstructorReturn from '../polyfills/possibleConstructorReturn';
import _inherits from '../polyfills/inherits';
import React from 'react';
import PropTypes from 'prop-types';
import emojiIndex from '../utils/emoji-index';

var Search = function (_React$PureComponent) {
  _inherits(Search, _React$PureComponent);

  function Search(props) {
    _classCallCheck(this, Search);

    var _this = _possibleConstructorReturn(this, (Search.__proto__ || _Object$getPrototypeOf(Search)).call(this, props));

    _this.setRef = _this.setRef.bind(_this);
    _this.handleChange = _this.handleChange.bind(_this);
    return _this;
  }

  _createClass(Search, [{
    key: 'handleChange',
    value: function handleChange() {
      var value = this.input.value;

      this.props.onSearch(emojiIndex.search(value, {
        emojisToShowFilter: this.props.emojisToShowFilter,
        maxResults: this.props.maxResults,
        include: this.props.include,
        exclude: this.props.exclude,
        custom: this.props.custom
      }));
    }
  }, {
    key: 'setRef',
    value: function setRef(c) {
      this.input = c;
    }
  }, {
    key: 'clear',
    value: function clear() {
      this.input.value = '';
    }
  }, {
    key: 'render',
    value: function render() {
      var _props = this.props;
      var i18n = _props.i18n;
      var autoFocus = _props.autoFocus;


      return React.createElement(
        'div',
        { className: 'emoji-mart-search' },
        React.createElement('input', {
          ref: this.setRef,
          type: 'text',
          onChange: this.handleChange,
          placeholder: i18n.search,
          autoFocus: autoFocus
        })
      );
    }
  }]);

  return Search;
}(React.PureComponent);

export default Search;


Search.defaultProps = {
  onSearch: function onSearch() {},
  maxResults: 75,
  emojisToShowFilter: null,
  autoFocus: false
};