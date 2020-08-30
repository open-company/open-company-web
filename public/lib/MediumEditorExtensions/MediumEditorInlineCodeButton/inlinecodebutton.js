rangy.init();

var InlineCodeButton = MediumEditor.Extension.extend({
  name: 'inlinecode',

  init: function () {
    this.classApplier = rangy.createClassApplier('code', {
        elementTagName: 'code',
        normalize: true
    });

    this.button = this.document.createElement('button');
    this.button.classList.add('medium-editor-action', 'media-editor-action-inlinecode');
    this.button.innerHTML = '<i class="fa fa-code fa-lg"></i>';
    this.button.title = 'Code or text snippet';

    this.on(this.button, 'click', this.handleClick.bind(this));
  },

  getButton: function () {
    return this.button;
  },

  handleClick: function (event) {
    this.classApplier.toggleSelection();
    this.base.checkContentChanged();
  },

  isAlreadyApplied: function (node) {
    return node.nodeName.toLowerCase() === 'code';
  },

  isActive: function () {
    return this.button.classList.contains('medium-editor-button-active');
  },

  setInactive: function () {
    this.button.classList.remove('medium-editor-button-active');
  },

  setActive: function () {
    this.button.classList.add('medium-editor-button-active');
  }
});