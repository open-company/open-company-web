# undate

Undoable update for HTMLTextAreaElement

[![NPM version](http://img.shields.io/npm/v/undate.svg)](https://www.npmjs.com/package/undate)
[![Build Status](https://travis-ci.org/yuku-t/undate.svg?branch=master)](https://travis-ci.org/yuku-t/undate)
[![Code Climate](https://codeclimate.com/github/yuku-t/undate/badges/gpa.svg)](https://codeclimate.com/github/yuku-t/undate)

## Install

```bash
npm install --save undate
```

## Usage

```js
import {update, wrapCursor} from 'undate';

const textareaElement = document.getElementById('textarea');

textareaElement.value; //=> ''

// Update whole value
update(textareaElement, 'string before cursor', 'optional string after cursor');

textareaElement.value; //=> 'string before cursoroptional string after cursor'

// Update around the cursor
wrapCursor(textareaElement, ' _', '_ ');

textareaElement.value; //=> 'string before cursor __ optional string after cursor'

// Press cmd-z

textareaElement.value; //=> 'string before cursoroptional string after cursor'

textareaElement.setSelectionRange(14, 27);

textareaElement.value; //=> 'string before cursoroptional string after cursor'
                       //                  ^^^^^^^^^^^^^^ selected

wrapCursor(textareaElement, '**', '**');

textareaElement.value; //=> 'string before **cursoroptional** string after cursor'
                       //                    ^^^^^^^^^^^^^^ selected

// Press cmd-z

textareaElement.value; //=> 'string before cursoroptional string after cursor'
                       //                  ^^^^^^^^^^^^^^ selected

// Press cmd-z

textareaElement.value; //=> ''
```

## License

The [MIT](https://github.com/yuku-t/undate/blob/master/LICENSE) License
