(function (root, factory) {
    'use strict';
    if (typeof module === 'object') {
        module.exports = factory;
    } else if (typeof define === 'function' && define.amd) {
        define(factory);
    } else {
        root.CarrotFileDragging = factory;
    }
}(this, function (MediumEditor) {

var CLASS_DRAG_OVER = 'medium-editor-dragover';

function clearClassNames(element) {
    var editable = MediumEditor.util.getContainerEditorElement(element),
        existing = Array.prototype.slice.call(editable.parentElement.querySelectorAll('.' + CLASS_DRAG_OVER));

    existing.forEach(function (el) {
        el.classList.remove(CLASS_DRAG_OVER);
    });
}

var CarrotFileDragging = MediumEditor.Extension.extend({
    name: 'carrotFileDragging',

    allowedTypes: ['.*'],

    options: undefined,

    constructor: function (options) {
      if (options) {
        this.options = options;
      }

      MediumEditor.Extension.call(this, this.options);
    },

    init: function () {
        MediumEditor.Extension.prototype.init.apply(this, arguments);

        this.subscribe('editableDrag', this.handleDrag.bind(this));
        this.subscribe('editableDrop', this.handleDrop.bind(this));
    },

    handleDrag: function (event) {
        event.preventDefault();
        if (this.options.uploadHandler) {
            event.dataTransfer.dropEffect = 'copy';

            var target = event.target.classList ? event.target : event.target.parentElement;

            // Ensure the class gets removed from anything that had it before
            clearClassNames(target);

            if (event.type === 'dragover') {
                target.classList.add(CLASS_DRAG_OVER);
            }
        }
    },

    handleDrop: function (event) {
        // Prevent file from opening in the current window
        event.preventDefault();
        if (this.options.uploadHandler) {
            event.stopPropagation();
            // Select the dropping target, and set the selection to the end of the target
            // https://github.com/yabwe/medium-editor/issues/980
            this.base.selectElement(event.target);
            var selection = this.base.exportSelection();
            selection.start = selection.end;
            this.base.importSelection(selection);
            // IE9 does not support the File API, so prevent file from opening in the window
            // but also don't try to actually get the file
            if (event.dataTransfer.files) {
                Array.prototype.slice.call(event.dataTransfer.files).forEach(function (file) {
                    this.options.uploadHandler(this, file);
                }, this);
            }

            // Make sure we remove our class from everything
            clearClassNames(event.target);
        }
    },

    isAllowedFile: function (file) {
        return this.allowedTypes.some(function (fileType) {
            return !!file.type.match(fileType);
        });
    },

    insertImageFile: function (file, imageURL, thumbnailURL) {
        var addImageElement = this.document.createElement('img');
        var that = this;
        addImageElement.onload = function(){
            addImageElement.width = this.width;
            addImageElement.height = this.height;
            addImageElement.className = "carrot-no-preview";
            addImageElement.dataset.mediaType = "image";
            if (thumbnailURL) {
                addImageElement.dataset.thumbnail = photoThumbnail;
            }
            MediumEditor.util.insertHTMLCommand(that.document, addImageElement.outerHTML);
            that.insertNewParagraph();
        };
        addImageElement.src = imageURL;
    },

    insertNewParagraph: function() {
        this.base.execAction('insertparagraph');
    }
});

return CarrotFileDragging;

}(typeof require === 'function' ? require('medium-editor') : MediumEditor)));