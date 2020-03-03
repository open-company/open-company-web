// Inspired by https://github.com/varun-raj/medium-editor-autolist

(function (root, factory) {
    'use strict';
    if (typeof module === 'object') {
        module.exports = factory;
    } else if (typeof define === 'function' && define.amd) {
        define(factory);
    } else {
        root.AutoInlinecode = factory;
    }
}(this, function (MediumEditor) {

var AutoInlinecode = MediumEditor.Extension.extend({
    name: 'AutoInlinecode',
    init: function(){
      this.subscribe('editableKeyup', this.onKeyup.bind(this));
    },
    onKeyup: function (keyUpEvent) {
      // If event adds backquote
      if (MediumEditor.util.isKey(keyUpEvent, [192])) {
        var r = /(?<!<[^>]*)`/; //NB Do not use g option or it will infinite loop since it uses look behind
        var textContent = this.base.getSelectedParentElement().textContent.trim();
        console.log("DBG groups:", textContent.match(r));
        // Get the node text content
        // var code_start = this.base.getSelectedParentElement().previousSibling && this.base.getSelectedParentElement().previousSibling.textContent.trim();
        // // If the content is the code block start string:
        // if( (code_start == "```") && this.base.getExtensionByName('pre')){
        //   var preEl = this.document.createElement("pre");
        //   // Get the ``` containing element
        //   var codeBlockParent = this.base.getSelectedParentElement().previousSibling;
        //   // Replace the old P element with the newly created PRE
        //   codeBlockParent.parentElement.replaceChild(preEl, codeBlockParent);
        //   // Move cursor inside the PRE element
        //   MediumEditor.selection.moveCursor(this.document, preEl, 0);
        // }
      }
    }
  });

  return AutoInlinecode;

}(typeof require === 'function' ? require('medium-editor') : MediumEditor)));