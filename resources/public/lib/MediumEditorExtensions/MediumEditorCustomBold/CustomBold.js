rangy.init();

var CustomBold = MediumEditor.extensions.button.extend({
  name: 'custombold',

  tagNames: ['b', 'strong'],
  contentDefault: '<b>B</b>',
  contentFA: '<i class="fa fa-bold"></i>',
  aria: 'bold',
  action: 'custombold',
  style: {
    prop: 'font-family',
    value: 'AvenirLTStd-Heavy, Muli, Arial, sans-serif'
  },
  key: "B",

  init: function () {
    MediumEditor.extensions.button.prototype.init.call(this);

    this.subscribe('editableKeydown', this.handleKeydown.bind(this));
    this.classApplier = rangy.createClassApplier('bold', {
      elementTagName: 'b',
      tagNames: ['b', 'strong'],
      normalize: true
    });
  },

  handleKeydown: function (event) {
    var keyCode = MediumEditor.util.getKeyCode(event),
        isMeta = MediumEditor.util.isMetaCtrlKey(event),
        isShift = !!event.shiftKey,
        isAlt = !!event.altKey;

    if (keyCode === this.key.charCodeAt(0) &&
        isMeta && !isShift && !isAlt){
      event.preventDefault();
      event.stopPropagation();
      this.classApplier.toggleSelection();
      this.base.checkContentChanged();
    }
  },

  handleClick: function (event) {
    this.classApplier.toggleSelection();
    this.base.checkContentChanged();
  }
});