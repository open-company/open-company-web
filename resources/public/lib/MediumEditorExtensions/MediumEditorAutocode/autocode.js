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
      this.subscribe('editableKeydown', this.onKeydown.bind(this));
    },

    onKeydown: function (keyDownEvent) {
      // Prevent browser from creating another PRE element
      var preElement = this.getPreElement(this.base.getSelectedParentElement());
      if (preElement && MediumEditor.util.isKey(keyDownEvent, [MediumEditor.util.keyCode.ENTER])) {

        if (!MediumEditor.util.isMetaCtrlKey(keyDownEvent) && !keyDownEvent.shiftKey) {
          keyDownEvent.preventDefault();
          keyDownEvent.stopPropagation();

          var newParagraph = this.document.createElement('p');
          newParagraph.innerHTML = "<br/>"
          if (preElement.nextSibling) {
            preElement.parentNode.insertBefore(newParagraph, preElement.nextSibling);
          } else {
            preElement.parentNode.appendChild(newParagraph);
          }

          MediumEditor.selection.moveCursor(this.document, newParagraph);
          
          return false;
        }
      }
    },

    onKeyup: function (keyUpEvent) {
      if (MediumEditor.util.isKey(keyUpEvent, [MediumEditor.util.keyCode.ENTER])) {
        var preElement =  this.getPreElement(this.base.getSelectedParentElement());

        if (!preElement) {
          // No inside a PRE, let's see if we need to create one
          var code_start = this.base.getSelectedParentElement().previousSibling && this.base.getSelectedParentElement().previousSibling.textContent.trim();
          // If the content is the code block start string:
          if( (code_start == "```") && this.base.getExtensionByName('pre')){
            var newPreEl = this.document.createElement('pre');
            newPreEl.className = "carrot-no-preview media-codeblock";
            // Get the ``` containing element
            var codeBlockElement = this.base.getSelectedParentElement().previousSibling;
            // Replace the old P element with the newly created PRE
            codeBlockElement.parentElement.replaceChild(newPreEl, codeBlockElement);
            // Move cursor inside the PRE element
            MediumEditor.selection.moveCursor(this.document, newPreEl, 0);
          }
        }
      }
    },

    getPreElement: function (node) {
      if (node.nodeName.toLowerCase() === 'pre') {
        return node;
      }

      var parentNode = node.parentNode,
          tagName = parentNode.nodeName.toLowerCase();
      while (parentNode && !MediumEditor.util.isMediumEditorElement(parentNode)) {
        if (tagName === 'pre') {
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

  return AutoCode;

}(typeof require === 'function' ? require('medium-editor') : MediumEditor)));