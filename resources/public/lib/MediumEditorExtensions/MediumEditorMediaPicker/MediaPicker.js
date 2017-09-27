function log(){
  var args = Array.prototype.slice.call(arguments);
  console.log("MediaPicker", args);
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
    options: {buttons: ['entry', 'photo', 'video', 'chart', 'attachment', 'divider-line'],
              delegateMethods: {}},
    /* Internal private properties */
    _lastSelection: undefined,
    _waitingCB: false,

    constructor: function (options) {
      log("constructor", options);
      if (options) {
        this.options = options;
      }
      MediumEditor.Extension.call(this, this.options);
    },

    init: function(){
      log("init", this);
      // Create picker
      this.pickerElement = this.createPicker();
      this.getEditorElements()[0].parentNode.appendChild(this.pickerElement);
      // Events
      var that = this;
      this.getEditorElements().forEach(function(element){
        that.on(element, 'click', that.togglePicker.bind(that));
        that.on(element, 'keyup', that.togglePicker.bind(that));
        // that.on(element, 'blur', that.hide.bind(that));
        that.on(that.window, 'click', that.windowClick.bind(that));
        // that.on(element, 'focus', that.onFocus.bind(that));
        that.on(element, 'editableInput', that.togglePicker.bind(that));
      });
      MediumEditor.Extension.prototype.init.apply(this, arguments);
    },

    delegate: function(event, arg) {
      if (typeof this.delegateMethods[event] === "function") {
        this.delegateMethods[event](this, arg);
      }
    },

    destroy: function(){
      // Remove the previous saved selection markers if any
      if (this._lastSelection) {
        rangy.removeMarkers(this._lastSelection);
        this._lastSelection = undefined;
      }
      this.pickerElement.parentNode.removeChild(this.pickerElement);
      this.pickerElement = undefined;
      this.mainButton = undefined;
      this.mediaButtonsContainer = undefined;
      this.pickerButtons = undefined;
    },

    windowClick: function(event){
      if (this._waitingCB) {
        return;
      }
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

    /* Caret helpers */

    moveCaret: function($el, position){
      var range, sel, el, textEl;

      position = position || 0;
      range = document.createRange();
      sel = window.getSelection();
      el = $el.get(0);

      if (!el.childNodes.length) {
          textEl = document.createTextNode(' ');
          el.appendChild(textEl);
      }

      range.setStart(el.childNodes[0], position);
      range.collapse(true);
      sel.removeAllRanges();
      sel.addRange(range);
    },

    /**/
    getAddableElement: function(element) {
      // If the element is not a P or a DIV clib the tree back to the first P or DIV
      while(element !== this.getEditorElements()[0] &&  element.tagName !== "DIV" && element.tagName !== "P") {
        element = element.parentNode;
      }
      return element;
    },

    /* Picker buttons handlers */

    entryClick: function(event){
      this.collapse();
      this._waitingCB = true;
      this.delegate("onPickerClick", "entry");
    },

    photoClick: function(event){
      this.collapse();
      this._waitingCB = true;
      this.delegate("onPickerClick", "photo");
    },

    addPhoto: function(photoUrl, photoThumbnail, width, height){
      log("addPhoto", photoUrl, photoThumbnail, width, height);
      if (this._lastSelection) {
        rangy.restoreSelection(this._lastSelection);
        this._lastSelection = undefined;
      }
      if (photoUrl) {
        // 2 cases: it's directly the div.medium-editor or it's a p already

        var sel = this.window.getSelection(),
            element = sel.getRangeAt(0).commonAncestorContainer,
            p;
        element = this.getAddableElement(element);
        // if the selection is in a DIV means it's the main editor element
        if (element.tagName == "DIV") {
          // we need to add a p to insert the HR in
          p = this.document.createElement("p");
          element.appendChild(p);
        // if it's a P already
        } else if (element.tagName == "P"){
          // if it has a BR inside
          if (element.childNodes.length == 1){
            // remove it
            element.removeChild(element.childNodes[0]);
          }
          p = element;
        }
        var img = this.document.createElement("img");
        img.src = photoUrl;
        img.className = "carrot-no-preview";
        img.dataset.mediaType = "image";
        img.dataset.thumbnail = photoThumbnail;
        img.width = width;
        img.height = height;
        p.appendChild(img);

        var nextP = this.document.createElement("p");
        var br = this.document.createElement("br");
        nextP.appendChild(br);
        this.insertAfter(nextP, p);
        this.moveCaret($(nextP), 0);
        this.base.checkContentChanged();
      }
      this._waitingCB = false;
      setTimeout(this.togglePicker(), 100);
    },

    videoClick: function(event){
      this.collapse();
      this._waitingCB = true;
      this.delegate("onPickerClick", "video");
    },

    addVideo: function(videoUrl, videoType, videoId, videoThumbnail) {
      log("addVideo", videoUrl, videoType, videoId, videoThumbnail);
      if (this._lastSelection) {
        rangy.restoreSelection(this._lastSelection);
        this._lastSelection = undefined;
      }
      if (videoUrl) {
        // 2 cases: it's directly the div.medium-editor or it's a p already

        var sel = this.window.getSelection(),
            element = this.getAddableElement(sel.getRangeAt(0).commonAncestorContainer),
            p;
        // if the selection is in a DIV means it's the main editor element
        if (element.tagName == "DIV") {
          // we need to add a p to insert the HR in
          p = this.document.createElement("p");
          element.appendChild(p);
        // if it's a P already
        } else if (element.tagName == "P"){
          // if it has a BR inside
          if (element.childNodes.length == 1 && element.childNodes[0].tagName == "BR"){
            // remove it
            element.removeChild(element.childNodes[0]);
          }
          p = element;
        }
        var iframe = this.document.createElement("iframe");
        iframe.className = "carrot-no-preview";
        iframe.setAttribute("frameborder", 0);
        iframe.setAttribute("webkitallowfullscreen", true);
        iframe.setAttribute("mozallowfullscreen",true);
        iframe.setAttribute("allowfullscreen", true);
        iframe.dataset.thumbnail = videoThumbnail;
        iframe.dataset.videoType = videoType;
        iframe.dataset.videoId = videoId;
        iframe.setAttribute("src", videoUrl);
        iframe.setAttribute("width", 560);
        iframe.setAttribute("height", 315);
        p.appendChild(iframe);

        var nextP = this.document.createElement("p");
        var br = this.document.createElement("br");
        nextP.appendChild(br);
        this.insertAfter(nextP, p);
        this.moveCaret($(nextP), 0);
        this.base.checkContentChanged();
      }
      this._waitingCB = false;
      setTimeout(this.togglePicker(), 100);
    },

    chartClick: function(event){
      this.collapse();
      this._waitingCB = true;
      this.delegate("onPickerClick", "chart");
    },

    addChart: function(chartUrl, chartId, chartThumbnail) {
      log("addChart", chartUrl, chartId, chartThumbnail);
      if (this._lastSelection) {
        rangy.restoreSelection(this._lastSelection);
        this._lastSelection = undefined;
      }
      if (chartUrl) {
        // 2 cases: it's directly the div.medium-editor or it's a p already

        var sel = this.window.getSelection(),
            element = this.getAddableElement(sel.getRangeAt(0).commonAncestorContainer),
            p;
        // if the selection is in a DIV means it's the main editor element
        if (element.tagName == "DIV") {
          // we need to add a p to insert the HR in
          p = this.document.createElement("p");
          element.appendChild(p);
        // if it's a P already
        } else if (element.tagName == "P"){
          // if it has a BR inside
          if (element.childNodes.length == 1 && element.childNodes[0].tagName == "BR"){
            // remove it
            element.removeChild(element.childNodes[0]);
          }
          p = element;
        }
        var iframe = this.document.createElement("iframe");
        iframe.className = "carrot-no-preview";
        iframe.setAttribute("frameborder", 0);
        iframe.setAttribute("webkitallowfullscreen", true);
        iframe.setAttribute("mozallowfullscreen",true);
        iframe.setAttribute("allowfullscreen", true);
        iframe.dataset.thumbnail = chartThumbnail;
        iframe.dataset.chartId = chartId;
        iframe.setAttribute("src", chartUrl);
        iframe.setAttribute("width", 550);
        iframe.setAttribute("height", 430);
        p.appendChild(iframe);

        var nextP = this.document.createElement("p");
        var br = this.document.createElement("br");
        nextP.appendChild(br);
        this.insertAfter(nextP, p);
        this.moveCaret($(nextP), 0);
        this.base.checkContentChanged();
      }
      this._waitingCB = false;
      setTimeout(this.togglePicker(), 100);
    },

    attachmentClick: function(event){
      this.collapse();
      this._waitingCB = true;
      this.delegate("onPickerClick", "attachment");
    },

    addAttachment: function(attachmentUrl, attachmentData){
      log("addAttachment", attachmentUrl, attachmentData);
      if (this._lastSelection) {
        rangy.restoreSelection(this._lastSelection);
        this._lastSelection = undefined;
      }
      if (attachmentUrl) {
        // 2 cases: it's directly the div.medium-editor or it's a p already

        var sel = this.window.getSelection(),
            element = this.getAddableElement(sel.getRangeAt(0).commonAncestorContainer),
            p;
        // if the selection is in a DIV means it's the main editor element
        if (element.tagName == "DIV") {
          // we need to add a p to insert the HR in
          p = this.document.createElement("p");
          element.appendChild(p);
        // if it's a P already
        } else if (element.tagName == "P"){
          // if it has a BR inside
          if (element.childNodes.length == 1 && element.childNodes[0].tagName == "BR"){
            // remove it
            element.removeChild(element.childNodes[0]);
          }
          p = element;
        }
        var uniqueID = "remove-bt-" + parseInt(Math.random() * 100000);
        var link = this.document.createElement("a");
        link.className = "carrot-no-preview media-attachment";
        link.setAttribute("contenteditable", false);
        link.setAttribute("href", attachmentUrl);
        link.setAttribute("target", "_blank");
        link.setAttribute("id", uniqueID);
        link.dataset.name = attachmentData.fileName;
        link.dataset.mimetype = attachmentData.fileType;
        link.dataset.size = attachmentData.fileSize;
        link.dataset.disablePreview = true;
        var icon = this.document.createElement("i");
        icon.setAttribute("contenteditable", false);
        icon.className = "file-mimetype fa " + attachmentData.icon;
        link.appendChild(icon);
        var title = this.document.createElement("label");
        title.setAttribute("contenteditable", false);
        title.className = "media-attachment-title";
        title.innerHTML = attachmentData.title;
        link.appendChild(title);
        var subtitle = this.document.createElement("label");
        subtitle.setAttribute("contenteditable", false);
        subtitle.className = "media-attachment-subtitle";
        subtitle.innerHTML = attachmentData.subtitle;
        link.appendChild(subtitle);
        var removeBt = this.document.createElement("button");
        removeBt.className = "mlb-reset remove-attachment";
        removeBt.setAttribute("onclick", "javascript: MediaPicker.RemoveAnchor('" + uniqueID + "', this);");
        removeBt.setAttribute("title", "Remove attachment");
        var removeX = this.document.createElement("i");
        removeX.className = "fa fa-times";
        removeBt.appendChild(removeX);
        link.appendChild(removeBt);
        p.appendChild(link);

        var nextP = this.document.createElement("p");
        var br = this.document.createElement("br");
        nextP.appendChild(br);
        this.insertAfter(nextP, p);
        this.moveCaret($(nextP), 0);
        this.base.checkContentChanged();
      }
      this._waitingCB = false;
      setTimeout(this.togglePicker(), 100);
    },

    insertAfter: function(newNode, referenceNode) {
      referenceNode.parentNode.insertBefore(newNode, referenceNode.nextSibling);
    },

    dividerLineClick: function(event){
      log("dividerLineClick", this, event);
      this.collapse();
      event.stopPropagation();
      this.delegate("onPickerClick", "divider-line");
      if (this._lastSelection) {
        rangy.restoreSelection(this._lastSelection);
      }
      // 2 cases: it's directly the div.medium-editor or it's a p already

      var sel = this.window.getSelection(),
          element = this.getAddableElement(sel.getRangeAt(0).commonAncestorContainer),
          p;
      // if the selection is in a DIV means it's the main editor element
      if (element.tagName == "DIV") {
        // we need to add a p to insert the HR in
        p = this.document.createElement("p");
        element.appendChild(p);
      // if it's a P already
      } else if (element.tagName == "P"){
        // if it has a BR inside
        if (element.childNodes.length == 1 && element.childNodes[0].tagName == "BR"){
          // remove it
          element.removeChild(element.childNodes[0]);
        }
        p = element;
      }
      var hr = this.document.createElement("hr");
      hr.className = "media-divider-line";
      p.appendChild(hr);

      var nextP = this.document.createElement("p");
      var br = this.document.createElement("br");
      nextP.appendChild(br);
      this.insertAfter(nextP, p);
      this.moveCaret($(nextP), 0);

      this.base.checkContentChanged();
      setTimeout(this.togglePicker(), 100);
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
        } else if (opt === 'photo') {
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
    /* Expand, collapse and check current state*/

    hidePlaceholder: function(){
      this.base.getExtensionByName("placeholder").hidePlaceholder();
    },

    isExpanded: function(){
      return this.mainButton.classList.contains(this.expandedClass);
    },

    expand: function(){
      if (this._waitingCB) {
        return;
      }
      this.delegate("willExpand");
      this.getEditorElements().forEach(function(element){
        element.classList.add("medium-editor-placeholder-hidden");
      });
      // Hide the placeholder
      this.hidePlaceholder();
      // Save the current selection
      this._lastSelection = rangy.saveSelection();
      // Unfocus the field
      this.getEditorElements().forEach(function(el){
        el.blur();
      });
      this.mainButton.classList.add(this.expandedClass);
      this.mediaButtonsContainer.classList.add(this.expandedClass);

      this.delegate("didExpand");
    },

    collapse: function(){
      if (this._waitingCB) {
        return;
      }
      this.delegate("willCollapse");
      this.mainButton.classList.remove(this.expandedClass);
      this.mediaButtonsContainer.classList.remove(this.expandedClass);
      this.delegate("didCollapse");
    },

    toggleExpand: function(event){
      if (event) {
        this._waitingCB = false;
      }
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
      if (this._waitingCB) {
        return;
      }
      log("show");
      this.delegate("willShow");
      this.pickerElement.style.display = 'block';
      this.delegate("didShow");
    },

    hide: function(){
      if (this._waitingCB) {
        return;
      }
      log("hide", this);
      this.delegate("willHide");
      this.collapse();
      this.pickerElement.style.display = 'none';
      this.delegate("didHide");
    },

    isRangySelectionBoundary: function(el) {
      return (el.tagName == "SPAN" && el.classList.contains("rangySelectionBoundary"));
    },

    isBR: function(el) {
      return (el.tagName == "BR");
    },

    paragraphIsEmpty: function(element){
      // Empty body
      if (element.childNodes.length == 0 && $(element).html() == "") {
        return true;
      }
      // Empty body like: <p><br/><p/>
      if ((element.childNodes.length == 1 &&
           this.isBR(element.childNodes[0])) ||
          (element.childNodes.length == 2 &&
            ((this.isBR(element.childNodes[0]) &&
              this.isRangySelectionBoundary(element.childNodes[1])) ||
             (this.isBR(element.childNodes[1]) &&
              this.isRangySelectionBoundary(element.childNodes[0]))))) {
        return true;
      }
      return false;
    },

    togglePicker: function(event, editable){
      if (this._waitingCB) {
        return;
      }
      if (event) {
        log("togglePicker 1", event, event.type);
      } else {
        log("togglePicker 2", "no event");
      }
      var sel = this.window.getSelection(),
          element;
      log("   ", sel, sel.rangeCount, sel.rangeCount.length);
      if (sel.rangeCount > 0) {
        element = sel.getRangeAt(0).commonAncestorContainer;
        log("   sel.rangeCount > 0", element);
        if (sel !== undefined || element !== undefined) {
          if (this.paragraphIsEmpty(element)){
            var top = ($(element).offset().top - $(this.pickerElement.parentNode).offset().top - 10);
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

  MediaPicker.RemoveAnchor = function(uniqueID, e){
    log("RemoveAnchor", arguments, window.event);
    if (window.event) {
      window.event.preventDefault();
    }
    if (e.preventDefault){
      e.preventDefault();
    }
    var p = document.querySelector("#" + uniqueID).parentNode;
    p.parentNode.removeChild(p);
    $("div.media-picker-body").blur();
    $("div.media-picker-body").focus();
  };

  return MediaPicker;

}(typeof require === 'function' ? require('medium-editor') : MediumEditor)));