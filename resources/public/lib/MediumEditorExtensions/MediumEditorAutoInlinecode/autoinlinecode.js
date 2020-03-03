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
        if (element && !this.insideCodeTag(element) && paragraphEl && element.innerHTML.match(/`[^`]+`/)) {
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
        var codeEl = this.insideCodeTag(this.base.getSelectedParentElement());

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
    insideCodeTag: function(node) {
      if (node.nodeName.toLowerCase() === 'code') {
        return node;
      }

      var parentNode = node.parentNode,
          tagName = parentNode.nodeName.toLowerCase();
      while (parentNode && !MediumEditor.util.isMediumEditorElement(parentNode)) {
        if (tagName === 'code') {
          return parentNode;
        }
        parentNode = parentNode.parentNode;
        if (parentNode) {
          tagName = parentNode.nodeName.toLowerCase();
        }
      }
      return false;
    },
    getParagraphElement: function (node) {
      if (node.nodeName.toLowerCase() === 'p') {
        return node;
      }

      var parentNode = node.parentNode,
          tagName = parentNode.nodeName.toLowerCase();
      while (parentNode && !MediumEditor.util.isMediumEditorElement(parentNode)) {
        if (tagName === 'p') {
          return parentNode;
        }
        parentNode = parentNode.parentNode;
        if (parentNode) {
          tagName = parentNode.nodeName.toLowerCase();
        }
      }
      return false;
    }
  });

  return AutoInlinecode;

}(typeof require === 'function' ? require('medium-editor') : MediumEditor)));