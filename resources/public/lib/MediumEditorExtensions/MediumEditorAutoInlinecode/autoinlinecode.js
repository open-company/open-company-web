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
    keyCodes: {BACKQUOTE: 192,
               RIGHTARROW: 39},
    codeCollection: null,
    throttledCheck: null,

    init: function(){
      var editor = this.getEditorElements()[0];
      this.codeCollection = editor.getElementsByTagName('code');

      this.throttledCheck = MediumEditor.util.throttle(function () {
        // Make sure all the code tags have the data-disable-toolbar option
        for (var i = 0; i < this.codeCollection.length; i++) {
          if (!this.codeCollection[i].dataset.disableToolbar) {
            this.codeCollection[i].dataset.disableToolbar = true;
          }
        }
      }.bind(this));
      this.subscribe('editableKeyup', this.onKeyup.bind(this));

      this.on(editor, 'click', this.throttledCheck);

      this.throttledCheck();
    },

    onKeyup: function (keyUpEvent) {
      // If event adds backquote
      if (MediumEditor.util.isKey(keyUpEvent, [this.keyCodes.BACKQUOTE])) {
        var element = this.base.getSelectedParentElement();
        var paragraphEl = element && this.getParagraphElement(element);
        if (element && !this.getCodeTag(element) && paragraphEl && element.innerHTML.match(/`[^`]+`/)) {
          // Replace text contained btw 2 backquote
          var r = /([^`]*)`([^`]+)`([^`]*)/; //NB Do not use g option or it will infinite loop since it uses look behind
          element.innerHTML = element.innerHTML.replace(r, function(all, prev, center, after){
            var cleanText = center.replace(/<\/?[^>]+(>|$)/ig, ''),
                fixedAfter = (after.search(/^(\s|&nbsp;)/i) > -1)? after : " " + after;
            return prev + "<code class=\"oc-latest-code\" data-disable-toolbar=\"true\">" + cleanText + "</code>" + fixedAfter;
          });
          var lastAddedCode = element.querySelector("code.oc-latest-code"),
              nextSibling = lastAddedCode.nextSibling;
              nextSiblingText = nextSibling && (nextSibling.nodeType === Node.TEXT_NODE? nextSibling.textContent :
                                                                                         nextSibling.innerText);
          if (lastAddedCode) {
            MediumEditor.selection.moveCursor(this.document, nextSibling, (nextSiblingText.length > 0? 1 : 0));
            lastAddedCode.classList.remove("oc-latest-code");
          }
        }
      } else if (MediumEditor.util.isKey(keyUpEvent, [this.keyCodes.RIGHTARROW])) {
        // Make sure there is at least 1 space after the code element
        var codeEl = this.getCodeTag(this.base.getSelectedParentElement());

        if (codeEl && (!codeEl.nextSibling ||
                       (codeEl.nextSibling.nodeType === Node.ELEMENT_NODE &&
                        codeEl.nextSibling.tagName.toLowerCase() === "br"))) {
          var textNode = this.document.createTextNode('\u00A0');
          if (codeEl.nextSibling) {
            codeEl.parentNode.insertBefore(textNode, codeEl.nextSibling);
          } else {
            codeEl.parentNode.appendChild(textNode);
          }

          MediumEditor.selection.moveCursor(this.document, textNode, 1);
        }
      }

      this.throttledCheck();
    },

    getCodeTag: function(node) {
      return MediumEditor.util.traverseUp(node, function(el){return el.nodeName.toLowerCase() === 'code';});
    },

    getParagraphElement: function (node) {
      return MediumEditor.util.traverseUp(node, function(el){return el.nodeName.toLowerCase() === 'p';});
    }
  });

  return AutoInlinecode;

}(typeof require === 'function' ? require('medium-editor') : MediumEditor)));