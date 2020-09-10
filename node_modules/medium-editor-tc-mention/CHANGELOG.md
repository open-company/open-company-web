# Change Log

All notable changes to this project will be documented in this file. See [standard-version](https://github.com/conventional-changelog/standard-version) for commit guidelines.

<a name="2.2.4"></a>
## [2.2.4](https://github.com/tomchentw/medium-editor-tc-mention/compare/v2.2.3...v2.2.4) (2016-05-31)



<a name="2.2.3"></a>
## [2.2.3](https://github.com/tomchentw/medium-editor-tc-mention/compare/v2.2.2...v2.2.3) (2016-02-22)


### Bug Fixes

* **TCMention:** emit editableInput event in selectMentionCallback ([6bf2172](https://github.com/tomchentw/medium-editor-tc-mention/commit/6bf2172)), closes [#6](https://github.com/tomchentw/medium-editor-tc-mention/issues/6)



<a name="2.2.2"></a>
## [2.2.2](https://github.com/tomchentw/medium-editor-tc-mention/compare/v2.2.1...v2.2.2) (2016-02-17)


### Bug Fixes

* **TCMention:** use this.base.unsubscribe ([db9044c](https://github.com/tomchentw/medium-editor-tc-mention/commit/db9044c))



<a name="2.2.1"></a>
## [2.2.1](https://github.com/tomchentw/medium-editor-tc-mention/compare/v2.2.0...v2.2.1) (2016-02-17)


### Bug Fixes

* **TCMention:** should unsubscribe callbacks when destroy ([0d911c1](https://github.com/tomchentw/medium-editor-tc-mention/commit/0d911c1))



<a name="2.2.0"></a>
# [2.2.0](https://github.com/tomchentw/medium-editor-tc-mention/compare/v2.1.1...v2.2.0) (2016-01-25)




<a name="2.1.1"></a>
## [2.1.1](https://github.com/tomchentw/medium-editor-tc-mention/compare/v2.1.0...v2.1.1) (2015-11-25)


### Bug Fixes

* **TCMention:** detects if is left arrow key ([fbf4b16](https://github.com/tomchentw/medium-editor-tc-mention/commit/fbf4b16)), closes [#5](https://github.com/tomchentw/medium-editor-tc-mention/issues/5)
* **TCMention:** select the text only when element is first time created ([59b54be](https://github.com/tomchentw/medium-editor-tc-mention/commit/59b54be))



<a name="2.1.0"></a>
# [2.1.0](https://github.com/tomchentw/medium-editor-tc-mention/compare/v2.0.0...v2.1.0) (2015-11-24)


### Features

* **TCMention:** add extra className map for trigger ([0b6a64e](https://github.com/tomchentw/medium-editor-tc-mention/commit/0b6a64e))
* **TCMention:** move caret to after this.activeMentionAt when hidePanel() ([0b3d8e1](https://github.com/tomchentw/medium-editor-tc-mention/commit/0b3d8e1))
* **TCMention:** rename options for setting className ([40d2822](https://github.com/tomchentw/medium-editor-tc-mention/commit/40d2822))
* **TCMention:** use activeTriggerClassNameMap to mark mention at tag ([4c6503d](https://github.com/tomchentw/medium-editor-tc-mention/commit/4c6503d))


### BREAKING CHANGES

* TCMention: use extraPanelClassName and extraActivePanelClassName instead.

Before & After:

```js
extraClassName -> extraPanelClassName
extraActiveClassName -> extraActivePanelClassName
```



<a name="2.0.0"></a>
# [2.0.0](https://github.com/tomchentw/medium-editor-tc-mention/compare/v1.4.1...v2.0.0) (2015-11-13)


### Features

* **TCMention:** rewrite mention detection using different algorithm ([58d6666](https://github.com/tomchentw/medium-editor-tc-mention/commit/58d6666))


### BREAKING CHANGES

* TCMention: detect the "word" based on current caret position

    The intention is to make its behaviour much closer to Facebook/Medium. Also rename the option of `autoHideOnBlurDelay` to `hideOnBlurDelay`.

    Before:

    The user type in @ or # to trigger mention panel immediately. If you already type one and navigate it back to the position using your arrow keys on keyboard, the mention panel won't show up this time.

    After:

    The user has to type in @ or # with one more word to trigger mention panel. Navigating back to the word will show up the mention panel.



<a name="1.4.1"></a>
## [1.4.1](https://github.com/tomchentw/medium-editor-tc-mention/compare/v1.4.0...v1.4.1) (2015-11-11)


### Bug Fixes

* **TCMention:** add autoHideOnBlurDelay to options ([2227777](https://github.com/tomchentw/medium-editor-tc-mention/commit/2227777)), closes [#3](https://github.com/tomchentw/medium-editor-tc-mention/issues/3)



<a name="1.4.0"></a>
# [1.4.0](https://github.com/tomchentw/medium-editor-tc-mention/compare/v1.3.0...v1.4.0) (2015-11-11)


### Bug Fixes

* **TCMention:** should include pageXOffset for left as well ([7f24f3e](https://github.com/tomchentw/medium-editor-tc-mention/commit/7f24f3e))

### Features

* **TCMention:** panel position to be the end of activeMentionAt element ([cb7768a](https://github.com/tomchentw/medium-editor-tc-mention/commit/cb7768a))



<a name="1.3.0"></a>
# [1.3.0](https://github.com/tomchentw/medium-editor-tc-mention/compare/v1.2.2...v1.3.0) (2015-11-03)


### Bug Fixes

* **TCMention:** hide mention panel on editableBlur ([65f007b](https://github.com/tomchentw/medium-editor-tc-mention/commit/65f007b)), closes [#2](https://github.com/tomchentw/medium-editor-tc-mention/issues/2)



<a name="1.2.2"></a>
## [1.2.2](https://github.com/tomchentw/medium-editor-tc-mention/compare/v1.2.1...v1.2.2) (2015-10-23)


### Bug Fixes

* **TCMention:** check if event.shiftKey is active for @ and # ([5207462](https://github.com/tomchentw/medium-editor-tc-mention/commit/5207462)), closes [#1](https://github.com/tomchentw/medium-editor-tc-mention/issues/1)



<a name="1.2.1"></a>
## [1.2.1](https://github.com/tomchentw/medium-editor-tc-mention/compare/v1.2.0...v1.2.1) (2015-10-16)


### Features

* **TCMention:** add extraActiveClassName to options ([8815c5d](https://github.com/tomchentw/medium-editor-tc-mention/commit/8815c5d))
* **mention-panel.min.css:** add overflow property as well ([286a281](https://github.com/tomchentw/medium-editor-tc-mention/commit/286a281))



<a name="1.2.0"></a>
# [1.2.0](https://github.com/tomchentw/medium-editor-tc-mention/compare/v1.1.1...v1.2.0) (2015-10-16)


### Features

* **TCMention:** add destroyPanelContent for clean up ([bee5dd7](https://github.com/tomchentw/medium-editor-tc-mention/commit/bee5dd7))



<a name="1.1.1"></a>
## [1.1.1](https://github.com/tomchentw/medium-editor-tc-mention/compare/v1.1.0...v1.1.1) (2015-10-16)


### Bug Fixes

* **TCMention:** BACKSPACE should call renderPanelContent as well ([07e8b03](https://github.com/tomchentw/medium-editor-tc-mention/commit/07e8b03))



<a name="1.1.0"></a>
# [1.1.0](https://github.com/tomchentw/medium-editor-tc-mention/compare/v1.0.0...v1.1.0) (2015-10-16)


### Features

* **TCMention:** add extraClassName to options ([0ca57c7](https://github.com/tomchentw/medium-editor-tc-mention/commit/0ca57c7))



<a name="1.0.0"></a>
# 1.0.0 (2015-10-14)


### Features

* **src:** add TCMention as es6 module ([d1191be](https://github.com/tomchentw/medium-editor-tc-mention/commit/d1191be))
