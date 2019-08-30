function log(){
  // var args = Array.prototype.slice.call(arguments);
  // console.debug("DBG MediaPicker", args);
}

function PlaceCaretAtEnd(el) {
  el.focus();
  if (typeof window.getSelection != "undefined"
        && typeof document.createRange != "undefined") {
    var range = document.createRange();
    range.selectNodeContents(el);
    range.collapse(false);
    var sel = window.getSelection();
    sel.removeAllRanges();
    sel.addRange(range);
  } else if (typeof document.body.createTextRange != "undefined") {
    var textRange = document.body.createTextRange();
    textRange.moveToElementText(el);
    textRange.collapse(false);
    textRange.select();
  }
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
    /* Use inline plus button */
    inlinePlusButtonOptions: {inlineButtons: true,
                              alwaysExpanded: false,
                              initiallyVisible: false},
    initialButtonsShown: false,
    /* Selector to identify the click that needs save the caret position */
    saveSelectionClickElementId: undefined,
    /* Contains the picker buttons */
    options: {buttons: ['entry', 'photo', 'video', 'chart', 'attachment', 'divider-line'],
              delegateMethods: {}},
    /* Force placeholder hide of MediumEditor when the button expands*/
    hidePlaceholderOnExpand: false,
    /* Internal private properties */
    _lastSelection: undefined,
    _waitingCB: false,

    constructor: function (options) {
      if (options) {
        this.options = options;
      }
      MediumEditor.Extension.call(this, this.options);
    },

    init: function(){
      rangy.init();
      // Create picker
      if (this.inlinePlusButtonOptions.inlineButtons) {
        this.pickerElement = this.createPicker();
        this.getEditorElements()[0].parentNode.appendChild(this.pickerElement);
      }
      // Picker button events
      this.getEditorElements().forEach(function(element){
        if (this.inlinePlusButtonOptions.inlineButtons) {
          this.on(element, 'click', this.togglePicker.bind(this));
          this.on(element, 'keyup', this.togglePicker.bind(this));
          this.on(element, 'focus', this.onFocus.bind(this));
          this.on(element, 'editableInput', this.togglePicker.bind(this));
        }
        // this.on(element, 'blur', this.hide.bind(this));
        this.on(this.window, 'click', this.windowClick.bind(this));
      }, this);

      MediumEditor.Extension.prototype.init.apply(this, arguments);
      // Initialize tooltips
      $('[data-toggle=\"tooltip\"]').tooltip();

      if (this.inlinePlusButtonOptions.initiallyVisible) {
        // Force show the media picker buttons
        this.togglePicker();
      }
    },

    delegate: function(event, arg) {
      if (typeof this.delegateMethods[event] === "function") {
        this.delegateMethods[event](this, arg);
      }
    },

    destroy: function(){
      this.removeSelection();
      if (this.inlinePlusButtonOptions.inlineButtons) {
        this.pickerElement.parentNode.removeChild(this.pickerElement);
      }
      this.pickerElement = undefined;
      this.mainButton = undefined;
      this.mediaButtonsContainer = undefined;
      this.pickerButtons = undefined;
    },

    windowClick: function(event){
      if (this._waitingCB) {
        return;
      }
      // If the inline plus button is enabled
      if (this.inlinePlusButtonOptions.inlineButtons && !this.inlinePlusButtonOptions.alwaysExpanded) {
        // If the user clicked inside the editor or on the picker
        if(!MediumEditor.util.isDescendant(this.getEditorElements()[0], event.target, true) &&
           !MediumEditor.util.isDescendant(this.pickerElement, event.target, true)) {
          // Hide and collapse the picker
          this.hide();
          this.collapse();
        }
      }
      // If we have a selector for an external picker
      if (this.saveSelectionClickElementId !== undefined) {
        var target = event.target,
            el = document.getElementById(this.saveSelectionClickElementId);
        // And the target of the click is the same of the given selector
        if (target === el) {
          // Save the current selection
          this.saveSelection();
          // TODO: if the currently focused element is not a MediumEditor element
          // save a selection to add the content at the end of the field
        }
      }
    },

    onFocus: function(event, editable){
      setTimeout(this.togglePicker(), 100);
    },

    createPicker: function(){
      var picker = this.document.createElement('div');
      picker.id = 'medium-editor-media-picker-' + this.getEditorId();
      picker.className = 'medium-editor-media-picker';
      picker.style.display = "none";
      // picker.style.left = 
      this.mediaButtonsContainer = this.createPickerMediaButtons();
      if (!this.inlinePlusButtonOptions.alwaysExpanded) {
        this.mainButton = this.createPickerMainButton();
        picker.appendChild(this.mainButton);
      }
      picker.appendChild(this.mediaButtonsContainer);
      return picker;
    },

    addButtonTooltip: function(bt, msg){
      bt.setAttribute("title", msg);
      bt.dataset.toggle = "tooltip";
      bt.dataset.placement = "top";
      bt.dataset.container = "body";
    },

    createPickerMainButton: function(){
      var mButton = this.document.createElement('button');
      mButton.className = 'media add-media-bt';
      this.addButtonTooltip(mButton, "Insert media");
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
      while(!MediumEditor.util.isMediumEditorElement(element) &&  element.tagName !== "DIV" && element.tagName !== "P") {
        element = element.parentNode;
      }
      return element;
    },

    /* Picker buttons handlers */

    entryClick: function(event){
      if (this.inlinePlusButtonOptions.alwaysExpanded) {
        this.hidePlaceholder();
        this.saveSelection();
      }
      this.collapse();
      this._waitingCB = true;
      this.delegate("onPickerClick", "entry");
      $(event.target).tooltip("hide");
    },

    gifClick: function(event){
      if (this.inlinePlusButtonOptions.alwaysExpanded) {
        this.hidePlaceholder();
        this.saveSelection();
      }
      this.collapse();
      this._waitingCB = true;
      this.delegate("onPickerClick", "gif");
      $(event.target).tooltip("hide");
    },

    photoClick: function(event){
      if (this.inlinePlusButtonOptions.alwaysExpanded) {
        this.hidePlaceholder();
        this.saveSelection();
      }
      this.collapse();
      this._waitingCB = true;
      this.delegate("onPickerClick", "photo");
      $(event.target).tooltip("hide");
    },

    addGIF: function(gifUrl, gifStillThumbnail, width, height){
      this.addImage(gifUrl, gifStillThumbnail, width, height, "image/gif");
    },

    addPhoto: function(photoUrl, photoThumbnail, width, height){
      this.addImage(photoUrl, photoThumbnail, width, height, "image/*");
    },

    addImage: function(photoUrl, photoThumbnail, width, height, mediaType){
      if (this._lastSelection) {
        rangy.restoreSelection(this._lastSelection);
        this._lastSelection = undefined;
      }
      if (photoUrl) {
        // 2 cases: it's directly the div.medium-editor or it's a p already
        if (!this.base.getFocusedElement()) {
          PlaceCaretAtEnd(this.getEditorElements()[0]);
        }
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
          // If the paragraph is empty
          if (element.innerText.length === 0 || element.innerText === "\n") {
              // if it has a BR inside
            if (element.childNodes.length == 1){
              // remove it
              element.removeChild(element.childNodes[0]);
            }
            p = element;
          } else {
            // If the current P is not empty create a new paragraph, append it
            // after the current paragraph and add the image there
            p = this.document.createElement("p");
            $(element).after(p);
            element = p.parentNode;
          }
        }
        var img = this.document.createElement("img");
        img.src = photoUrl;
        img.className = "carrot-no-preview";
        img.dataset.mediaType = mediaType;
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
      if (this.inlinePlusButtonOptions.alwaysExpanded) {
        this.hidePlaceholder();
        this.saveSelection();
      }
      this.collapse();
      this._waitingCB = true;
      this.delegate("onPickerClick", "video");
      $(event.target).tooltip("hide");
    },

    addVideo: function(videoUrl, videoType, videoId, videoThumbnail) {
      log("addVideo", videoUrl, videoType, videoId, videoThumbnail);
      if (this._lastSelection) {
        rangy.restoreSelection(this._lastSelection);
        this._lastSelection = undefined;
      }
      if (videoUrl) {
        // 2 cases: it's directly the div.medium-editor or it's a p already
        if (!this.base.getFocusedElement()) {
          PlaceCaretAtEnd(this.getEditorElements()[0]);
        }
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
        iframe.dataset.mediaType = "video";
        iframe.setAttribute("frameborder", 0);
        iframe.setAttribute("webkitallowfullscreen", true);
        iframe.setAttribute("mozallowfullscreen",true);
        iframe.setAttribute("allowfullscreen", true);
        iframe.dataset.thumbnail = videoThumbnail;
        iframe.dataset.videoType = videoType;
        iframe.dataset.videoId = videoId;
        iframe.setAttribute("src", videoUrl);
        iframe.setAttribute("width", 548);
        iframe.setAttribute("height", 308);
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
      $(event.target).tooltip("hide");
    },

    addChart: function(chartUrl, chartId, chartThumbnail) {
      log("addChart", chartUrl, chartId, chartThumbnail);
      if (this._lastSelection) {
        rangy.restoreSelection(this._lastSelection);
        this._lastSelection = undefined;
      }
      if (chartUrl) {
        // 2 cases: it's directly the div.medium-editor or it's a p already
        if (!this.base.getFocusedElement()) {
          PlaceCaretAtEnd(this.getEditorElements()[0]);
        }
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
        iframe.dataset.mediaType = "chart";
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
      $(event.target).tooltip("hide");
    },

    addAttachment: function(attachmentUrl, attachmentData){
      log("addAttachment", attachmentUrl, attachmentData);
      if (this._lastSelection) {
        rangy.restoreSelection(this._lastSelection);
        this._lastSelection = undefined;
      }
      if (attachmentUrl) {
        // 2 cases: it's directly the div.medium-editor or it's a p already
        if (!this.base.getFocusedElement()) {
          PlaceCaretAtEnd(this.getEditorElements()[0]);
        }
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
        link.dataset.mediaType = "attachment";
        link.setAttribute("contenteditable", false);
        link.setAttribute("href", attachmentUrl);
        link.setAttribute("target", "_blank");
        link.setAttribute("id", uniqueID);
        link.dataset.name = attachmentData.fileName;
        link.dataset.mimetype = attachmentData.fileType;
        link.dataset.size = attachmentData.fileSize;
        link.dataset.author = attachmentData.author;
        link.dataset.createdat = attachmentData.createdat;
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
      $(event.target).tooltip("hide");

      if (this._lastSelection) {
        rangy.restoreSelection(this._lastSelection);
      }
      // 2 cases: it's directly the div.medium-editor or it's a p already
      if (!this.base.getFocusedElement()) {
        PlaceCaretAtEnd(this.getEditorElements()[0]);
      }
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
      hr.className = "carrot-no-preview media-divider-line";
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
      container.className = 'media-picker-container group media-' + this.buttons.length;
      // Create the necessary buttons
      this.buttons.forEach(function(opt, idx){
        var button = this.document.createElement('button');
        button.className = 'media';
        container.appendChild(button);

        if (opt === 'entry') {
          button.classList.add('media-entry');
          button.classList.add('media-' + idx);
          this.addButtonTooltip(button, "Add update");
          this.on(button, 'click', this.entryClick.bind(this));
        } else if (opt === 'gif') {
          button.classList.add('media-gif');
          button.classList.add('media-' + idx);
          this.addButtonTooltip(button, "Add GIF");
          this.on(button, 'click', this.gifClick.bind(this));
        } else if (opt === 'photo') {
          button.classList.add('media-photo');
          button.classList.add('media-' + idx);
          this.addButtonTooltip(button, "Add image");
          this.on(button, 'click', this.photoClick.bind(this));
        } else if (opt === 'video') {
          button.classList.add('media-video');
          button.classList.add('media-' + idx);
          this.addButtonTooltip(button, "Add video");
          this.on(button, 'click', this.videoClick.bind(this));
        } else if (opt === 'chart') {
          button.classList.add('media-chart');
          button.classList.add('media-' + idx);
          this.addButtonTooltip(button, "Add chart");
          this.on(button, 'click', this.chartClick.bind(this));
        } else if (opt === 'attachment') {
          button.classList.add('media-attachment');
          button.classList.add('media-' + idx);
          this.addButtonTooltip(button, "Add attachment");
          this.on(button, 'click', this.attachmentClick.bind(this));
        } else if (opt === 'divider-line') {
          button.classList.add('media-divider');
          button.classList.add('media-' + idx);
          this.addButtonTooltip(button, "Add divider line");
          this.on(button, 'click', this.dividerLineClick.bind(this));
        }
        this.pickerButtons.push(button);
      }, this);
      return container;
    },
    /* Expand, collapse and check current state*/

    hidePlaceholder: function(){
      this.base.getExtensionByName("placeholder").hidePlaceholder();
    },

    isExpanded: function(){
      return this.mainButton.classList.contains(this.expandedClass);
    },

    saveSelection: function() {
      // Remove the previous selection to avoid leaving
      // markers in the body that are not needed
      this.internalRemoveSelection();
      // Save the current selection
      this._lastSelection = rangy.saveSelection();
      // Unfocus the field
      this.getEditorElements().forEach(function(el){
        el.blur();
      });
    },

    internalRemoveSelection: function() {
      // Remove the previous saved selection markers if any
      if (this._lastSelection) {
        rangy.removeMarkers(this._lastSelection);
        this._lastSelection = undefined;
      }
    },

    removeSelection: function() {
      this.internalRemoveSelection();
    },

    expand: function(){
      if (this._waitingCB) {
        return;
      }
      this.delegate("willExpand");
      if (this.hidePlaceholderOnExpand) {
        this.getEditorElements().forEach(function(element){
          if (element && element.classList) {
            element.classList.add("medium-editor-placeholder-hidden");
          }
        });
      }
      // Hide the placeholder
      if (!this.inlinePlusButtonOptions.alwaysExpanded) {
        this.hidePlaceholder();
        this.saveSelection();
        this.mainButton.classList.add(this.expandedClass);
      }
      this.mediaButtonsContainer.classList.add(this.expandedClass);

      if (!this.inlinePlusButtonOptions.alwaysExpanded) {
        this.mainButton.setAttribute("title", "Close");
        $(this.mainButton).tooltip("fixTitle");
        $(this.mainButton).tooltip("hide");
      }

      this.delegate("didExpand");
    },

    collapse: function(){
      if (this._waitingCB) {
        return;
      }
      this.delegate("willCollapse");
      if (this.mediaButtonsContainer) {
        this.mediaButtonsContainer.classList.remove(this.expandedClass);
      }
      if (!this.inlinePlusButtonOptions.alwaysExpanded) {
        this.mainButton.classList.remove(this.expandedClass);

        this.mainButton.setAttribute("title", "Insert media");
        $(this.mainButton).tooltip("fixTitle");
        $(this.mainButton).tooltip("hide");
      }
      this.delegate("didCollapse");
    },

    toggleExpand: function(event){
      if (event) {
        this._waitingCB = false;
      }
      if (this.isExpanded()) {
        this.collapse();
        // Remove last selection only on direct click of the button
        this.removeSelection();
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
      this.delegate("willShow");
      this.pickerElement.style.display = 'block';
      this.delegate("didShow");
      if(this.inlinePlusButtonOptions.alwaysExpanded) {
        this.expand();
      }
    },

    hide: function(){
      if (this._waitingCB) {
        return;
      }
      if(this.inlinePlusButtonOptions.alwaysExpanded) {
        this.collapse();
      }
      this.delegate("willHide");
      this.collapse();
      this.pickerElement.style.display = 'none';
      this.delegate("didHide");
    },

    isRangySelectionBoundary: function(el) {
      if (el && el.classList) {
        return el.classList.contains("rangySelectionBoundary");
      } else {
        return false;
      }
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
      if (this.inlinePlusButtonOptions.inlineButtons) {
        if (this._waitingCB) {
          return;
        }
        var sel = this.window.getSelection(),
            element;
        if (sel.rangeCount > 0 || (this.inlinePlusButtonOptions.initiallyVisible && !this.initialButtonsShown)) {
          if (this.inlinePlusButtonOptions.initiallyVisible && !this.initialButtonsShown) {
            element = this.getEditorElements()[0];
          }else {
            element = sel.getRangeAt(0).commonAncestorContainer;  
          }
          this.initialButtonsShown = true;
          if (sel !== undefined || element !== undefined) {
            if (this.paragraphIsEmpty(element)){
              var top = ($(element).offset().top - $(this.pickerElement.parentNode).offset().top - 1);
              this.pickerElement.style.top = top + "px";
              this.show();
              return;
            }
          }
        }
        this.hide();
      }
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