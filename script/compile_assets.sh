#!/bin/bash

java -jar ~/closure_compiler/closure-compiler-v$1.jar \
-W QUIET \
--js_output_file $2/resources/public/js/oc_assets.js \
--create_source_map $2/public/js/oc_assets.js.map \
--js $2/resources/public/lib/print_ascii.js \
--js $2/resources/public/lib/truncate/jquery.truncate.js \
--js $2/resources/public/lib/scrollTo/scrollTo.min.js \
--js $2/resources/public/js/emojione/autocomplete.js \
--js $2/resources/public/lib/js-utils/svg-utils.js \
--js $2/resources/public/lib/js-utils/pasteHtmlAtCaret.js \
--js $2/resources/public/lib/cleanHTML/cleanHTML.js \
--js $2/resources/public/lib/MediumEditorAutolist/autolist.js \
--js $2/resources/public/lib/select2/js/select2.js \
--output_wrapper '%output%
//# sourceMappingURL=$3/js/oc_assets.js.map'