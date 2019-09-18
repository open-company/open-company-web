// Inspired by https://github.com/varun-raj/medium-editor-autolist

(function (root, factory) {
    'use strict';
    if (typeof module === 'object') {
        module.exports = factory;
    } else if (typeof define === 'function' && define.amd) {
        define(factory);
    } else {
        root.AutoCode = factory;
    }
}(this, function (MediumEditor) {

var AutoCode = MediumEditor.Extension.extend({
    name: 'AutoCode',
    init: function(){
      this.subscribe('editableKeyup', this.onKeyup.bind(this));
    },
    onKeyup: function (keyUpEvent) {
      if (MediumEditor.util.isKey(keyUpEvent, [MediumEditor.util.keyCode.SPACE])) {
        var code_start = this.base.getSelectedParentElement().textContent.slice(0, 1);
        if( (code_start == "`") && this.base.getExtensionByName('pre')){
          this.base.execAction('append-pre');
          this.base.getSelectedParentElement().textContent = this.base.getSelectedParentElement().textContent.slice(1).trim();
        }
      }
    }
  });

  return AutoCode;

}(typeof require === 'function' ? require('medium-editor') : MediumEditor)));