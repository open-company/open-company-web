(function (root, factory) {
    'use strict';
    if (typeof module === 'object') {
        module.exports = factory;
    } else if (typeof define === 'function' && define.amd) {
        define(factory);
    } else {
        root.MediaPicker = factory;
    }
}(this, function (MediumEditor) {

  var MediaPicker = MediumEditor.Extension.extend({
    name: 'media-picker',
    expandedClass: 'expanded',
    /* Is the media picker expanded? */
    expanded: false,
    /* Is the media picker visible? */
    visible: false,
    /* Contains the main picker button */
    mainButton: undefined,
    /* Contains the picker buttons */
    pickerButtons: [],
    /* Contains the picker buttons */
    options: ['entry', 'picture', 'video', 'chart', 'attachment', 'divider-line'],

    constructor: function (options) {
      if (options) {
        this.options = options;
      }
      MediumEditor.Extension.call(this, this.options);
    },

    init: function(){
      MediumEditor.Extension.prototype.init.apply(this, arguments);
      this.createPicker();
      this.getEditorElements.forEach(function(element){
        this.on(element, 'click', this.togglePicker.bind(this));
        this.on(element, 'keyup', this.togglePicker.bind(this));
        this.subscribe('editableInput', this.togglePicker.bind(this));
      });
    },

    createPicker: function(){
      var picker = this.document.createElement('div');
      picker.id = 'medium-editor-media-picker-' + this.getEditorId();
      picker.className = 'medium-editor-media-picker';
      this.mainButton = this.createPickerMainButton();
      picker.appendChild(this.mainButton);
      picker.appendChild(this.createPickerMediaButtons());
    },

    createPickerMainButton: function(){
      var mButton = this.document.createElement('button');
      mButton.className = 'media add-media-bt';
      this.on(mButton, 'click', this.toggleExpand)
      return mButton;
    },

    createPickerMediaButtons: function(){
      var container = this.document.createElement('div');
      div.className = 'media-picker-container';

      this.options.forEach(function(opt){
        var button = this.document.createElement('button');
        button.className = 'media';

        if (opt === 'entry') {
          button.classList.add('media-entry');
          this.on(button, 'click', this.entryClick.bind(this));
        } else if (opt === 'picture') {
          button.classList.add('media-photo');
          this.on(button, 'click', this.photoClick.bind(this));
        } else if (opt === 'video') {
          button.classList.add('media-video');
          this.on(button, 'click', this.videoClick.bind(this));
        } else if (opt === 'chart') {
          button.classList.add('media-chart');
          this.on(button, 'click', this.chartClick.bind(this));
        } else if (opt === 'attachment') {
          button.classList.add('media-attachment');
          this.on(button, 'click', this.attachmentClick.bind(this));
        } else if (opt === 'divider-line') {
          button.classList.add('media-divider');
          this.on(button, 'click', this.dividerLineClick.bind(this));
        }
        this.pickerButtons.push(button);
        container.appendChild(button);
      });
      return container;
    },

    /* Expand, collapse and check current state*/

    isExpanded: function(){
      return this.mainButton.classList.contains(this.expandedClass);
    },

    toggleExpand: function(){
      if (this.isExpanded()) {
        this.mainButton.classList.remove(this.expandedClass);
      } else {
        this.mainButton.classList.add(this.expandedClass);
      }
    },

    /* Show, hide and check current state */

    isVisible: function(){
      this.mainButton.style.display === 'block';
    },

    show: function(){
      this.mainButton.style.display = 'block';
    },

    hide: function(){
      this.mainButton.style.display = 'none';
    },

    paragraphIsEmpty: function(element){
      if (element.childNodes.length == 1) {
        var firstChild = element.childNodes[0];
        if (firstChild && firstChild.tagName == "BR") {
          return true;
        }
      }
      return false;
    },

    togglePicker: function(event, editable){
      var sel = this.window.getSelection(),
          element;
      if (sel.rangeCount.length > 0) {
        element = sel.getRangeAt(0).commonAncestorContainer;
        if (sel !== undefined || el !== undefined) {
          if (this.paragraphIsEmpty(element)){
            this.show();
          } else {
            this.hide();
          }
        }
      }
    },

    /* Picker buttons handlers */

    entryClick: function(event, editable){},
    photoClick: function(event, editable){},
    videoClick: function(event, editable){},
    chartClick: function(event, editable){},
    attachmentClick: function(event, editable){},
    dividerLineClick: function(event, editable){},

    
  });

  return MediaPicker;

}(typeof require === 'function' ? require('medium-editor') : MediumEditor)));