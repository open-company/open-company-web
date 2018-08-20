#!/bin/bash

cat $1/target/public/css/normalize.css > $2.temp
cat $1/target/public/css/app.main.css >> $2.temp
cat $1/target/public/css/emojione/autocomplete.css >> $2.temp
cat $1/target/public/css/medium-editor/medium-editor.css >> $2.temp
cat $1/target/public/css/medium-editor/default.css >> $2.temp
cat $1/target/public/css/emojione.css >> $2.temp
cat $1/target/public/css/emoji-mart.css >> $2.temp
cat $1/target/public/lib/MediumEditorExtensions/MediumEditorMediaPicker/MediaPicker.css >> $2.temp
cat $1/target/public/lib/MediumEditorExtensions/MediumEditorTCMention/mention-panel.min.css >> $2.temp

cssnano --no-zindex < $2.temp > $2