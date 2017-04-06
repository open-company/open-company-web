#!/bin/bash

cssnano < target/public/css/normalize.css \
target/public/css/app.main.css \
target/public/css/emojione/autocomplete.css \
target/public/css/medium-editor/medium-editor.css \
target/public//css/medium-editor/default.css \
target/public/css/emojione.css \
target/public/css/emojione-picker.css \
target/public/css/emojione.sprites.css \
> target/public/css/main_$1.css