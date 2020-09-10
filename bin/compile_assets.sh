#!/bin/bash

cd $2/target/public/ && \
java -jar ~/closure_compiler/closure-compiler-v$1.jar \
-W QUIET \
--js_output_file oc_assets.js \
--create_source_map oc_assets.js.map \
--js lib/print_ascii.js \
--js lib/autotrack/autotrack.js \
--js lib/autotrack/google-analytics.js \
--js lib/fullstory.js \
--js lib/scrollTo/scrollTo.min.js \
--js js/emojione/autocomplete.js \
--js lib/js-utils/pasteHtmlAtCaret.js \
--js lib/cleanHTML/cleanHTML.js \
--js lib/rangy/rangy-core.js \
--js lib/rangy/rangy-selectionsaverestore.js \
--js lib/rangy/rangy-classapplier.js \
--js lib/MediumEditorExtensions/MediumEditorToolbar/toolbar.js \
--js lib/MediumEditorExtensions/MediumEditorPaste/paste.js \
--js lib/MediumEditorExtensions/MediumEditorAutolist/autolist.js \
--js lib/MediumEditorExtensions/MediumEditorAutoquote/autoquote.js \
--js lib/MediumEditorExtensions/MediumEditorAutocode/autocode.js \
--js lib/MediumEditorExtensions/MediumEditorAutoInlinecode/autoinlinecode.js \
--js lib/MediumEditorExtensions/MediumEditorInlineCodeButton/inlinecodebutton.js \
--js lib/MediumEditorExtensions/MediumEditorMediaPicker/MediaPicker.js \
--js lib/MediumEditorExtensions/MediumEditorFileDragging/filedragging.js \
--js lib/MediumEditorExtensions/MediumEditorHighlighterButton/highlighterbutton.js \
--js lib/MediumEditorExtensions/MediumEditorTCMention/index.min.js \
--js lib/MediumEditorExtensions/MediumEditorTCMention/CustomizedTagComponent.js \
--output_wrapper "%output%
//# sourceMappingURL=$3/oc_assets.js.map"

cd $2/target/public/ && \
cat <<EOT >> oc.js

//# sourceMappingURL=$3/oc.js.map
EOT
