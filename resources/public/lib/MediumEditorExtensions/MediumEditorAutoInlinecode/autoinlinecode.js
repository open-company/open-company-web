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
    detectingKeyCodes: [192],
    init: function(){
      this.subscribe('editableKeyup', this.onKeyup.bind(this));
    },

    onKeyup: function (keyUpEvent) {
      // If event adds backquote
      if (MediumEditor.util.isKey(keyUpEvent, this.detectingKeyCodes)) {
        var element = this.base.getSelectedParentElement();
        var paragraphEl = element && this.getParagraphElement(element);
        if (element && !this.getCodeTag(element) && paragraphEl && element.innerHTML.match(/`[^`]+`/)) {
          var r = /([^`]*)`([^`]+)`([^`]*)/; //NB Do not use g option or it will infinite loop since it uses look behind    
          element.innerHTML = element.innerHTML.replace(r, function(all,prev,center,after){
            return prev + "<code class=\"oc-latest-code\">" + center + "</code>" + (after || "&nbsp;");
          });
          var lastAddedCode = element.querySelector("code.oc-latest-code");
          if (lastAddedCode) {
            MediumEditor.selection.moveCursor(this.document, element.querySelector("code.oc-latest-code"), 1);
            lastAddedCode.classList.remove("oc-latest-code");
          }
        }
      } else if (MediumEditor.util.isKey(keyUpEvent, [39])) {
        var codeEl = this.getCodeTag(this.base.getSelectedParentElement());

        if (codeEl && (!codeEl.nextSibling ||
                       (codeEl.nextSibling.nodeType == 1 &&
                        codeEl.nextSibling.tagName.toLowerCase() == "br"))) {
          var textNode = this.document.createTextNode('\u00A0');
          if (codeEl.nextSibling) {
            codeEl.parentNode.insertBefore(textNode, codeEl.nextSibling);
          } else {
            codeEl.parentNode.appendChild(textNode);
          }

          MediumEditor.selection.moveCursor(this.document, textNode, 1);
        }
      }
    },

    getCodeTag: function(node) {
      return MediumEditor.util.traverseUp(node, function(el){return el.nodeName.toLowerCase() == 'code';});
    },

    getParagraphElement: function (node) {
      return MediumEditor.util.traverseUp(node, function(el){return el.nodeName.toLowerCase() == 'p';});
    }
  });

  return AutoInlinecode;

}(typeof require === 'function' ? require('medium-editor') : MediumEditor)));