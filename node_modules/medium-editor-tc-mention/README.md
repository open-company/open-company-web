# medium-editor-tc-mention
> MediumEditor extension for mention panels like @username or #tagging

[![Version][npm-image]][npm-url] [![Travis CI][travis-image]][travis-url] [![Quality][codeclimate-image]][codeclimate-url] [![Coverage][codeclimate-coverage-image]][codeclimate-coverage-url] [![Dependencies][gemnasium-image]][gemnasium-url] [![Gitter][gitter-image]][gitter-url]

<img width="824" alt="screen shot 2015-11-11 at 4 29 30 pm" src="https://cloud.githubusercontent.com/assets/922234/11088093/9b00fe2e-889a-11e5-801d-b6f79c568dde.png">


## Quick start: CustomizedTagComponent

```js
export function CustomizedTagComponent (props) {
  const trigger = props.currentMentionText.substring(0, 1);

  return (
    <div>
      <button onClick={() => props.selectMentionCallback(null)}>
        Cancel
      </button>
      <button onClick={() => props.selectMentionCallback(trigger + "mention")}>
        Select `{ trigger }mention`
      </button>
      CustomizedTagComponent!!!
    </div>
  );
}

this.editor = new MediumEditor(this.refs.editable, {
  extensions: {
    "mention": new TCMention({
      tagName: "b",
      renderPanelContent: function (panelEl, currentMentionText, selectMentionCallback) {
        ReactDOM.render((
          <CustomizedTagComponent
            currentMentionText={currentMentionText}
            selectMentionCallback={selectMentionCallback}
          />
        ), panelEl);
      },
      activeTriggerList: ["#", "@"]
    })
  }
});
```


## Usage

`medium-editor-tc-mention` requires __medium-editor@^5.8.3__

```sh
npm install --save medium-editor-tc-mention
```

### Import using module loaders

```js
// Default export
// Equivalent to import {default as TCMention} from "medium-editor-tc-mention";
import TCMention from "medium-editor-tc-mention";

// Alternative named export
import { TCMention } from "medium-editor-tc-mention";

// ES5, commonjs
var TCMention = require("medium-editor-tc-mention").TCMention;

require("medium-editor-tc-mention/lib/mention-panel.min.css");
```

### UMD version

You can find UMD version of this module at [`/lib/index.min.js`](https://github.com/tomchentw/medium-editor-tc-mention/blob/master/lib/index.min.js). Reference them directly in your `html`:

```html
<!doctype html>
<html>
<head>
...
  <link rel="stylesheet" href="<path_to_medium-editor>/dist/css/medium-editor.css" />
  <link rel="stylesheet" href="<path_to_medium-editor>/dist/css/themes/default.css" />
  <link rel="stylesheet" href="<path_to_medium-editor-tc-mention>/lib/mention-panel.min.css" />
...
</head>
<body>
  <div class="editable"></div>

  <script type="text/javascript" src="<path_to_medium-editor>/dist/js/medium-editor.js"></script>
  <script type="text/javascript" src="<path_to_medium-editor-tables>/lib/index.min.js"></script>

  <script type="text/javascript" charset="utf-8">
    var editor = new MediumEditor('.editable', {
      extensions: {
        mention: new TCMention()
      }
    });
  </script>
</body>
</html>
```


## Contributing

[![devDependency Status][david-dm-image]][david-dm-url]

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request


[npm-image]: https://img.shields.io/npm/v/medium-editor-tc-mention.svg?style=flat-square
[npm-url]: https://www.npmjs.org/package/medium-editor-tc-mention

[travis-image]: https://img.shields.io/travis/tomchentw/medium-editor-tc-mention.svg?style=flat-square
[travis-url]: https://travis-ci.org/tomchentw/medium-editor-tc-mention
[codeclimate-image]: https://img.shields.io/codeclimate/github/tomchentw/medium-editor-tc-mention.svg?style=flat-square
[codeclimate-url]: https://codeclimate.com/github/tomchentw/medium-editor-tc-mention
[codeclimate-coverage-image]: https://img.shields.io/codeclimate/coverage/github/tomchentw/medium-editor-tc-mention.svg?style=flat-square
[codeclimate-coverage-url]: https://codeclimate.com/github/tomchentw/medium-editor-tc-mention
[gemnasium-image]: https://img.shields.io/gemnasium/tomchentw/medium-editor-tc-mention.svg?style=flat-square
[gemnasium-url]: https://gemnasium.com/tomchentw/medium-editor-tc-mention
[gitter-image]: https://badges.gitter.im/Join%20Chat.svg
[gitter-url]: https://gitter.im/tomchentw/medium-editor-tc-mention?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge
[david-dm-image]: https://img.shields.io/david/dev/tomchentw/medium-editor-tc-mention.svg?style=flat-square
[david-dm-url]: https://david-dm.org/tomchentw/medium-editor-tc-mention#info=devDependencies
