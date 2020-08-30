rangy.init();

var HighlighterButton = MediumEditor.extensions.button.extend({
  name: 'highlighter',

  tagNames: ['mark'], // nodeName which indicates the button should be 'active' when isAlreadyApplied() is called
  contentDefault: '<b>H</b>', // default innerHTML of the button
  aria: 'Highlight', // used as both aria-label and title attributes
  action: 'highlight', // used as the data-action attribute of the button

  className: 'highlight',

  keyCodes: {RIGHTARROW: 39},

  init: function () {
    MediumEditor.extensions.button.prototype.init.call(this);

    this.classApplier = rangy.createClassApplier(this.className, {
      elementTagName: 'mark',
      normalize: true
    });

    this.subscribe('editableKeyup', this.onKeyup.bind(this));
  },

  handleClick: function (event) {
    this.classApplier.toggleSelection();
    this.base.checkContentChanged();
  },

  onKeyup: function (keyUpEvent) {
    // If event adds backquote
    if (MediumEditor.util.isKey(keyUpEvent, [this.keyCodes.RIGHTARROW])) {
      // Make sure there is at least 1 space after the code element
      var markEl = this.getMarkTag(this.base.getSelectedParentElement());

      if (markEl && (!markEl.nextSibling ||
                     (markEl.nextSibling.nodeType === Node.ELEMENT_NODE &&
                      markEl.nextSibling.tagName.toLowerCase() === "br"))) {
        var textNode = this.document.createTextNode('\u00A0');
        if (markEl.nextSibling) {
          markEl.parentNode.insertBefore(textNode, markEl.nextSibling);
        } else {
          markEl.parentNode.appendChild(textNode);
        }

        MediumEditor.selection.moveCursor(this.document, textNode, 1);
      }
    }
  },

  getMarkTag: function(node) {
    return MediumEditor.util.traverseUp(node, function(el){return el.nodeName.toLowerCase() === 'mark';});
  }
});