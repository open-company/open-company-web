#!/bin/bash

cssnano < $1/target/public/css/normalize.css >> $2
cssnano < $1/target/public/css/app.main.css >> $2
cssnano < $1/target/public/css/emojione/autocomplete.css >> $2
cssnano < $1/target/public/css/medium-editor/medium-editor.css >> $2
cssnano < $1/target/public//css/medium-editor/default.css >> $2
cssnano < $1/target/public/css/emojione.css >> $2
cssnano < $1/target/public/css/emojione-picker.css >> $2
cssnano < $1/target/public/css/emojione.sprites.css >> $2
