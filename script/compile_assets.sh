#!/bin/bash

java -jar ~/closure_compiler/closure-compiler-v{{ closure_compiler_version }}.jar /
-W QUIET \
--js_output_file resources/public/js/oc_assets.js \
--create_source_map public/js/oc_assets.js.map \
--js resources/public/lib/print_ascii.js \
--js resources/public/lib/truncate/jquery.truncate.js \
--js resources/public/lib/scrollTo/scrollTo.min.js \
--js resources/public/js/emojione/autocomplete.js \
--js resources/public/lib/js-utils/svg-utils.js \
--js resources/public/lib/js-utils/pasteHtmlAtCaret.js \
--js resources/public/lib/cleanHTML/cleanHTML.js \
--js resources/public/lib/MediumEditorAutolist/autolist.js \
--js resources/public/lib/select2/js/select2.js \
--output_wrapper '%output%\n//# sourceMappingURL={{ web_endpoint }}/js/oc_assets.js.map'