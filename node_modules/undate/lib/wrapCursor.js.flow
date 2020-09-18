// @flow

import update from './update';

export default function (el: HTMLTextAreaElement, before: string, after: ?string) {
  const initEnd = el.selectionEnd,
        headToCursor = el.value.substr(0, el.selectionStart) + before,
        cursorToTail = el.value.substring(el.selectionStart, initEnd) + (after || '') + el.value.substr(initEnd);
  update(el, headToCursor, cursorToTail);
  el.selectionEnd = initEnd + before.length;
  return el;
}
