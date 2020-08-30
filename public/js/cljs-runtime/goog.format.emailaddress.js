goog.provide("goog.format.EmailAddress");
goog.require("goog.string");
goog.format.EmailAddress = function(opt_address, opt_name) {
  this.name_ = opt_name || "";
  this.address = opt_address || "";
};
goog.format.EmailAddress.OPENERS_ = '"\x3c([';
goog.format.EmailAddress.CLOSERS_ = '"\x3e)]';
goog.format.EmailAddress.SPECIAL_CHARS = '()\x3c\x3e@:\\".[]';
goog.format.EmailAddress.ADDRESS_SEPARATORS_ = ",;";
goog.format.EmailAddress.CHARS_REQUIRE_QUOTES_ = goog.format.EmailAddress.SPECIAL_CHARS + goog.format.EmailAddress.ADDRESS_SEPARATORS_;
goog.format.EmailAddress.ALL_DOUBLE_QUOTES_ = /"/g;
goog.format.EmailAddress.ESCAPED_DOUBLE_QUOTES_ = /\\"/g;
goog.format.EmailAddress.ALL_BACKSLASHES_ = /\\/g;
goog.format.EmailAddress.ESCAPED_BACKSLASHES_ = /\\\\/g;
goog.format.EmailAddress.LOCAL_PART_REGEXP_STR_ = "[+a-zA-Z0-9_.!#$%\x26'*\\/\x3d?^`{|}~-]+";
goog.format.EmailAddress.DOMAIN_PART_REGEXP_STR_ = "([a-zA-Z0-9-]+\\.)+[a-zA-Z0-9]{2,63}";
goog.format.EmailAddress.LOCAL_PART_ = new RegExp("^" + goog.format.EmailAddress.LOCAL_PART_REGEXP_STR_ + "$");
goog.format.EmailAddress.DOMAIN_PART_ = new RegExp("^" + goog.format.EmailAddress.DOMAIN_PART_REGEXP_STR_ + "$");
goog.format.EmailAddress.EMAIL_ADDRESS_ = new RegExp("^" + goog.format.EmailAddress.LOCAL_PART_REGEXP_STR_ + "@" + goog.format.EmailAddress.DOMAIN_PART_REGEXP_STR_ + "$");
goog.format.EmailAddress.prototype.getName = function() {
  return this.name_;
};
goog.format.EmailAddress.prototype.getAddress = function() {
  return this.address;
};
goog.format.EmailAddress.prototype.setName = function(name) {
  this.name_ = name;
};
goog.format.EmailAddress.prototype.setAddress = function(address) {
  this.address = address;
};
goog.format.EmailAddress.prototype.toString = function() {
  return this.toStringInternal(goog.format.EmailAddress.CHARS_REQUIRE_QUOTES_);
};
goog.format.EmailAddress.isQuoteNeeded_ = function(name, specialChars) {
  for (var i = 0; i < specialChars.length; i++) {
    var specialChar = specialChars[i];
    if (goog.string.contains(name, specialChar)) {
      return true;
    }
  }
  return false;
};
goog.format.EmailAddress.prototype.toStringInternal = function(specialChars) {
  var name = this.getName();
  name = name.replace(goog.format.EmailAddress.ALL_DOUBLE_QUOTES_, "");
  if (goog.format.EmailAddress.isQuoteNeeded_(name, specialChars)) {
    name = '"' + name.replace(goog.format.EmailAddress.ALL_BACKSLASHES_, "\\\\") + '"';
  }
  if (name == "") {
    return this.address;
  }
  if (this.address == "") {
    return name;
  }
  return name + " \x3c" + this.address + "\x3e";
};
goog.format.EmailAddress.prototype.isValid = function() {
  return goog.format.EmailAddress.isValidAddrSpec(this.address);
};
goog.format.EmailAddress.isValidAddress = function(str) {
  return goog.format.EmailAddress.parse(str).isValid();
};
goog.format.EmailAddress.isValidAddrSpec = function(str) {
  return goog.format.EmailAddress.EMAIL_ADDRESS_.test(str);
};
goog.format.EmailAddress.isValidLocalPartSpec = function(str) {
  return goog.format.EmailAddress.LOCAL_PART_.test(str);
};
goog.format.EmailAddress.isValidDomainPartSpec = function(str) {
  return goog.format.EmailAddress.DOMAIN_PART_.test(str);
};
goog.format.EmailAddress.parseInternal = function(addr, ctor) {
  var name = "";
  var address = "";
  for (var i = 0; i < addr.length;) {
    var token = goog.format.EmailAddress.getToken_(addr, i);
    if (token.charAt(0) == "\x3c" && token.indexOf("\x3e") != -1) {
      var end = token.indexOf("\x3e");
      address = token.substring(1, end);
    } else {
      if (address == "") {
        name += token;
      }
    }
    i += token.length;
  }
  if (address == "" && name.indexOf("@") != -1) {
    address = name;
    name = "";
  }
  name = goog.string.collapseWhitespace(name);
  name = goog.string.stripQuotes(name, "'");
  name = goog.string.stripQuotes(name, '"');
  name = name.replace(goog.format.EmailAddress.ESCAPED_DOUBLE_QUOTES_, '"');
  name = name.replace(goog.format.EmailAddress.ESCAPED_BACKSLASHES_, "\\");
  address = goog.string.collapseWhitespace(address);
  return new ctor(address, name);
};
goog.format.EmailAddress.parse = function(addr) {
  return goog.format.EmailAddress.parseInternal(addr, goog.format.EmailAddress);
};
goog.format.EmailAddress.parseListInternal = function(str, parser, separatorChecker) {
  var result = [];
  var email = "";
  var token;
  str = goog.string.collapseWhitespace(str);
  for (var i = 0; i < str.length;) {
    token = goog.format.EmailAddress.getToken_(str, i);
    if (separatorChecker(token) || token == " " && parser(email).isValid()) {
      if (!goog.string.isEmptyOrWhitespace(email)) {
        result.push(parser(email));
      }
      email = "";
      i++;
      continue;
    }
    email += token;
    i += token.length;
  }
  if (!goog.string.isEmptyOrWhitespace(email)) {
    result.push(parser(email));
  }
  return result;
};
goog.format.EmailAddress.parseList = function(str) {
  return goog.format.EmailAddress.parseListInternal(str, goog.format.EmailAddress.parse, goog.format.EmailAddress.isAddressSeparator);
};
goog.format.EmailAddress.getToken_ = function(str, pos) {
  var ch = str.charAt(pos);
  var p = goog.format.EmailAddress.OPENERS_.indexOf(ch);
  if (p == -1) {
    return ch;
  }
  if (goog.format.EmailAddress.isEscapedDlQuote_(str, pos)) {
    return ch;
  }
  var closerChar = goog.format.EmailAddress.CLOSERS_.charAt(p);
  var endPos = str.indexOf(closerChar, pos + 1);
  while (endPos >= 0 && goog.format.EmailAddress.isEscapedDlQuote_(str, endPos)) {
    endPos = str.indexOf(closerChar, endPos + 1);
  }
  var token = endPos >= 0 ? str.substring(pos, endPos + 1) : ch;
  return token;
};
goog.format.EmailAddress.isEscapedDlQuote_ = function(str, pos) {
  if (str.charAt(pos) != '"') {
    return false;
  }
  var slashCount = 0;
  for (var idx = pos - 1; idx >= 0 && str.charAt(idx) == "\\"; idx--) {
    slashCount++;
  }
  return slashCount % 2 != 0;
};
goog.format.EmailAddress.isAddressSeparator = function(ch) {
  return goog.string.contains(goog.format.EmailAddress.ADDRESS_SEPARATORS_, ch);
};

//# sourceMappingURL=goog.format.emailaddress.js.map
