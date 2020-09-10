/* eslint-disable prefer-arrow-callback */

import {
  default as expect,
} from "expect";

import * as Module from "../index";

describe(`index`, function describeIndex() {
  it(`should be exported`, function it() {
    expect(Module.default).toExist();
    expect(Module.TCMention).toExist();
  });
});
