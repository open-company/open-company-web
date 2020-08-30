goog.provide("goog.net.Cookies");
goog.provide("goog.net.cookies");
goog.require("goog.asserts");
goog.require("goog.string");
goog.net.Cookies = function(context) {
  this.document_ = context || {cookie:""};
};
goog.net.Cookies.MAX_COOKIE_LENGTH = 3950;
goog.net.Cookies.prototype.isEnabled = function() {
  return navigator.cookieEnabled;
};
goog.net.Cookies.prototype.isValidName = function(name) {
  return !/[;=\s]/.test(name);
};
goog.net.Cookies.prototype.isValidValue = function(value) {
  return !/[;\r\n]/.test(value);
};
goog.net.Cookies.prototype.set = function(name, value, opt_maxAge, opt_path, opt_domain, opt_secure) {
  var sameSite;
  if (typeof opt_maxAge === "object") {
    goog.asserts.assert(opt_path == null);
    goog.asserts.assert(opt_domain == null);
    goog.asserts.assert(opt_secure == null);
    var options = opt_maxAge;
    sameSite = options.sameSite;
    opt_secure = options.secure;
    opt_domain = options.domain;
    opt_path = options.path;
    opt_maxAge = options.maxAge;
  }
  if (!this.isValidName(name)) {
    throw new Error('Invalid cookie name "' + name + '"');
  }
  if (!this.isValidValue(value)) {
    throw new Error('Invalid cookie value "' + value + '"');
  }
  if (opt_maxAge === undefined) {
    opt_maxAge = -1;
  }
  var domainStr = opt_domain ? ";domain\x3d" + opt_domain : "";
  var pathStr = opt_path ? ";path\x3d" + opt_path : "";
  var secureStr = opt_secure ? ";secure" : "";
  var expiresStr;
  if (opt_maxAge < 0) {
    expiresStr = "";
  } else {
    if (opt_maxAge == 0) {
      var pastDate = new Date(1970, 1, 1);
      expiresStr = ";expires\x3d" + pastDate.toUTCString();
    } else {
      var futureDate = new Date(goog.now() + opt_maxAge * 1000);
      expiresStr = ";expires\x3d" + futureDate.toUTCString();
    }
  }
  var sameSiteStr = sameSite != null ? ";samesite\x3d" + sameSite : "";
  this.setCookie_(name + "\x3d" + value + domainStr + pathStr + expiresStr + secureStr + sameSiteStr);
};
goog.net.Cookies.prototype.get = function(name, opt_default) {
  var nameEq = name + "\x3d";
  var parts = this.getParts_();
  for (var i = 0, part; i < parts.length; i++) {
    part = goog.string.trim(parts[i]);
    if (part.lastIndexOf(nameEq, 0) == 0) {
      return part.substr(nameEq.length);
    }
    if (part == name) {
      return "";
    }
  }
  return opt_default;
};
goog.net.Cookies.prototype.remove = function(name, opt_path, opt_domain) {
  var rv = this.containsKey(name);
  this.set(name, "", 0, opt_path, opt_domain);
  return rv;
};
goog.net.Cookies.prototype.getKeys = function() {
  return this.getKeyValues_().keys;
};
goog.net.Cookies.prototype.getValues = function() {
  return this.getKeyValues_().values;
};
goog.net.Cookies.prototype.isEmpty = function() {
  return !this.getCookie_();
};
goog.net.Cookies.prototype.getCount = function() {
  var cookie = this.getCookie_();
  if (!cookie) {
    return 0;
  }
  return this.getParts_().length;
};
goog.net.Cookies.prototype.containsKey = function(key) {
  return this.get(key) !== undefined;
};
goog.net.Cookies.prototype.containsValue = function(value) {
  var values = this.getKeyValues_().values;
  for (var i = 0; i < values.length; i++) {
    if (values[i] == value) {
      return true;
    }
  }
  return false;
};
goog.net.Cookies.prototype.clear = function() {
  var keys = this.getKeyValues_().keys;
  for (var i = keys.length - 1; i >= 0; i--) {
    this.remove(keys[i]);
  }
};
goog.net.Cookies.prototype.setCookie_ = function(s) {
  this.document_.cookie = s;
};
goog.net.Cookies.prototype.getCookie_ = function() {
  return this.document_.cookie;
};
goog.net.Cookies.prototype.getParts_ = function() {
  return (this.getCookie_() || "").split(";");
};
goog.net.Cookies.prototype.getKeyValues_ = function() {
  var parts = this.getParts_();
  var keys = [], values = [], index, part;
  for (var i = 0; i < parts.length; i++) {
    part = goog.string.trim(parts[i]);
    index = part.indexOf("\x3d");
    if (index == -1) {
      keys.push("");
      values.push(part);
    } else {
      keys.push(part.substring(0, index));
      values.push(part.substring(index + 1));
    }
  }
  return {keys:keys, values:values};
};
goog.net.Cookies.SetOptions = function() {
  this.maxAge;
  this.path;
  this.domain;
  this.secure;
  this.sameSite;
};
goog.net.Cookies.SameSite = {LAX:"lax", NONE:"none", STRICT:"strict", };
goog.net.cookies = new goog.net.Cookies(typeof document == "undefined" ? null : document);
goog.net.Cookies.getInstance = function() {
  return goog.net.cookies;
};

//# sourceMappingURL=goog.net.cookies.js.map
