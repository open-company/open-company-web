var CustomBold = MediumEditor.extensions.button.extend({
  name: 'custombold',

  tagNames: ['b'],
  contentDefault: '<b>B</b>',
  aria: 'Bold',
  action: 'bold',

  init: function () {
    rangy.init();

    MediumEditor.extensions.button.prototype.init.call(this);

    this.classApplier = rangy.createClassApplier('bold', {
      elementTagName: 'b',
      normalize: true
    });
  },

  handleClick: function (event) {
    this.classApplier.toggleSelection();
    this.base.checkContentChanged();
    if (this.isActive()){
      this.setInactive();
    } else {
      this.setActive();
    }
  }
});