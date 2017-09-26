function log(){
  var args = Array.prototype.slice.call(arguments);
  // console.log("MediaPicker", args);
}

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
    /* Main container */
    pickerElement: undefined,
    /* Contains the main picker button */
    mainButton: undefined,
    /* Media buttons container*/
    mediaButtonsContainer: undefined,
    /* Contains the picker buttons */
    pickerButtons: [],
    /* Contains the picker buttons */
    options: {buttons: ['entry', 'picture', 'video', 'chart', 'attachment', 'divider-line'],
              delegateMethods: {}},
    /* Internal private properties */
    _lastSelection: undefined,

    constructor: function (options) {
      console.log("constructor", options);
      if (options) {
        this.options = options;
      }
      MediumEditor.Extension.call(this, this.options);
    },

    init: function(){
      console.log("init", this);
      // Create picker
      this.pickerElement = this.createPicker();
      this.getEditorElements()[0].parentNode.appendChild(this.pickerElement);
      // Events
      var that = this;
      this.getEditorElements().forEach(function(element){
        that.on(element, 'click', that.togglePicker.bind(that));
        that.on(element, 'keyup', that.togglePicker.bind(that));
        // that.on(element, 'blur', that.hide.bind(that));
        log("init", "window:", that.window);
        that.on(that.window, 'click', that.windowClick.bind(that));
        that.on(element, 'focus', that.onFocus.bind(that));
        that.subscribe('editableInput', that.togglePicker.bind(that));
      });
      console.log("init/end", this);
      MediumEditor.Extension.prototype.init.apply(this, arguments);
    },

    delegate: function(event, arg) {
      // console.log("DELEGATE", event, arg);
      if (typeof this.delegateMethods[event] === "function") {
        this.delegateMethods[event](this, arg);
      }
    },

    destroy: function(){
      this.pickerElement.parentNode.removeChild(this.pickerElement);
      this.pickerElement = undefined;
      this.mainButton = undefined;
      this.mediaButtonsContainer = undefined;
      this.pickerButtons = undefined;
    },

    windowClick: function(event){
      log("windowClick", this.getEditorElements()[0], event.target);
      if(!MediumEditor.util.isDescendant(this.getEditorElements()[0], event.target, true) &&
         !MediumEditor.util.isDescendant(this.pickerElement, event.target, true)) {
        this.hide();
        this.collapse();
      }
    },

    onFocus: function(event, editable){
      log("onFocus", event, editable, this);
      setTimeout(this.togglePicker(), 100);
    },

    createPicker: function(){
      var picker = this.document.createElement('div');
      picker.id = 'medium-editor-media-picker-' + this.getEditorId();
      picker.className = 'medium-editor-media-picker';
      picker.style.display = "none";
      // picker.style.left = 
      this.mainButton = this.createPickerMainButton();
      this.mediaButtonsContainer = this.createPickerMediaButtons();
      picker.appendChild(this.mainButton);
      picker.appendChild(this.mediaButtonsContainer);
      return picker;
    },

    createPickerMainButton: function(){
      var mButton = this.document.createElement('button');
      mButton.className = 'media add-media-bt';
      this.on(mButton, 'click', this.toggleExpand.bind(this));
      return mButton;
    },

    /* Picker buttons handlers */

    entryClick: function(event){
      this.delegate("onPickerClick", "entry");
    },

    photoClick: function(event){
      this.delegate("onPickerClick", "photo");
    },

    videoClick: function(event){
      this.delegate("onPickerClick", "video");
    },

    chartClick: function(event){
      this.delegate("onPickerClick", "chart");
    },

    attachmentClick: function(event){
      this.delegate("onPickerClick", "attachment");
    },

    dividerLineClick: function(event){
      this.delegate("onPickerClick", "divider-line");
    },

    createPickerMediaButtons: function(){
      var container = this.document.createElement('div');
      container.className = 'media-picker-container media-' + this.buttons.length;
      // Create the necessary buttons
      var that = this;
      this.buttons.forEach(function(opt, idx){
        var button = that.document.createElement('button');
        button.className = 'media';
        container.appendChild(button);
        log("createPickerMediaButtons", opt, idx);

        if (opt === 'entry') {
          button.classList.add('media-entry');
          button.classList.add('media-' + idx);
          that.on(button, 'click', that.entryClick.bind(that));
        } else if (opt === 'picture') {
          button.classList.add('media-photo');
          button.classList.add('media-' + idx);
          that.on(button, 'click', that.photoClick.bind(that));
        } else if (opt === 'video') {
          button.classList.add('media-video');
          button.classList.add('media-' + idx);
          that.on(button, 'click', that.videoClick.bind(that));
        } else if (opt === 'chart') {
          button.classList.add('media-chart');
          button.classList.add('media-' + idx);
          that.on(button, 'click', that.chartClick.bind(that));
        } else if (opt === 'attachment') {
          button.classList.add('media-attachment');
          button.classList.add('media-' + idx);
          that.on(button, 'click', that.attachmentClick.bind(that));
        } else if (opt === 'divider-line') {
          button.classList.add('media-divider');
          button.classList.add('media-' + idx);
          that.on(button, 'click', that.dividerLineClick.bind(that));
        }
        that.pickerButtons.push(button);
      });
      return container;
    },

    pickerMediaButtonsEvents: function(){
      for (var button in this.pickerButtons) {
        log("pickerMediaButtonsEvents", button);
      }
    },

    /* Expand, collapse and check current state*/

    hidePlaceholder: function(){
      this.base.getExtensionByName("placeholder").hidePlaceholder();
    },

    isExpanded: function(){
      return this.mainButton.classList.contains(this.expandedClass);
    },

    expand: function(){
      this.delegate("willExpand");
      // Hide the placeholder
      this.hidePlaceholder();
      // Save the current selection
      this._lastSelection = rangy.saveSelection();
      this.mainButton.classList.add(this.expandedClass);
      this.mediaButtonsContainer.classList.add(this.expandedClass);

      this.delegate("didExpand");
    },

    collapse: function(){
      this.delegate("willCollapse");
      // Remove the previous saved selection markers if any
      if (this._lastSelection) {
        rangy.removeMarkers(this._lastSelection);
        this._lastSelection = undefined;
      }
      this.mainButton.classList.remove(this.expandedClass);
      this.mediaButtonsContainer.classList.remove(this.expandedClass);
      this.delegate("didCollapse");
    },

    toggleExpand: function(event){
      if (this.isExpanded()) {
        this.collapse();
      } else {
        this.expand();        
      }
      if (event !== undefined) {
        event.stopPropagation();
      }
    },

    /* Show, hide and check current state */

    isVisible: function(){
      this.pickerElement.style.display === 'block';
    },

    show: function(){
      log("show");
      this.delegate("willShow");
      this.pickerElement.style.display = 'block';
      this.delegate("didShow");
    },

    hide: function(){
      log("hide", this);
      this.delegate("willHide");
      this.collapse();
      this.pickerElement.style.display = 'none';
      this.delegate("didHide");
    },

    paragraphIsEmpty: function(element){
      // Empty body
      if (element.childNodes.length == 0 && $(element).html() == "") {
        return true;
      }
      // Empty body like: <p><br/><p/>
      if (element.childNodes.length == 1) {
        var firstChild = element.childNodes[0];
        if (firstChild &&
            (firstChild.tagName == "BR" ||
             (firstChild.tagName == "SPAN" && firstChild.classList.contains("rangySelectionBoundary")))) {
          return true;
        }
      }
      return false;
    },

    togglePicker: function(event, editable){
      if (event) {
        log("togglePicker", event.type);
      } else {
        log("togglePicker", "no event");
      }
      var sel = this.window.getSelection(),
          element;
      log("   ", sel, sel.rangeCount, sel.rangeCount.length);
      if (sel.rangeCount > 0) {
        element = sel.getRangeAt(0).commonAncestorContainer;
        log("   sel.rangeCount > 0", element);
        if (sel !== undefined || element !== undefined) {
          if (this.paragraphIsEmpty(element)){
            var top = Math.max(7, ($(element).offset().top - $(this.pickerElement.parentNode).offset().top - 10));
            log("   top:", $(element).offset().top, $(this.pickerElement.parentNode).offset().top, top + "px");
            this.pickerElement.style.top = top + "px";
            this.show();
            return;
          }
        }
      }
      this.hide();
    },
    
  });

  return MediaPicker;

}(typeof require === 'function' ? require('medium-editor') : MediumEditor)));