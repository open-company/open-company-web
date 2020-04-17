// Inspired by https://github.com/varun-raj/medium-editor-autolist

(function (root, factory) {
    'use strict';
    if (typeof module === 'object') {
        module.exports = factory;
    } else if (typeof define === 'function' && define.amd) {
        define(factory);
    } else {
        root.AutoQuote = factory;
    }
}(this, function (MediumEditor) {

var AutoQuote = MediumEditor.Extension.extend({
    name: 'AutoQuote',
    init: function(){
      this.subscribe('editableKeyup', this.onKeyup.bind(this));
    },
    onKeyup: function (keyUpEvent) {
      if (MediumEditor.util.isKey(keyUpEvent, [MediumEditor.util.keyCode.SPACE])) {
        var quote_start = this.base.getSelectedParentElement().textContent;
        if( (quote_start === "> ") && this.base.getExtensionByName('quote')){
          this.base.execAction('append-blockquote');
          this.base.getSelectedParentElement().textContent = this.base.getSelectedParentElement().textContent.slice(1).trim();
        }
      }
    }
  });

  return AutoQuote;

}(typeof require === 'function' ? require('medium-editor') : MediumEditor)));