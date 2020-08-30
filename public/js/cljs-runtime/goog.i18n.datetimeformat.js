goog.provide("goog.i18n.DateTimeFormat");
goog.provide("goog.i18n.DateTimeFormat.Format");
goog.require("goog.asserts");
goog.require("goog.date");
goog.require("goog.i18n.DateTimeSymbols");
goog.require("goog.i18n.TimeZone");
goog.require("goog.string");
goog.i18n.DateTimeFormat = function(pattern, opt_dateTimeSymbols) {
  goog.asserts.assert(pattern !== undefined, "Pattern must be defined");
  goog.asserts.assert(opt_dateTimeSymbols !== undefined || goog.i18n.DateTimeSymbols !== undefined, "goog.i18n.DateTimeSymbols or explicit symbols must be defined");
  this.patternParts_ = [];
  this.dateTimeSymbols_ = opt_dateTimeSymbols || goog.i18n.DateTimeSymbols;
  if (typeof pattern == "number") {
    this.applyStandardPattern_(pattern);
  } else {
    this.applyPattern_(pattern);
  }
};
goog.i18n.DateTimeFormat.Format = {FULL_DATE:0, LONG_DATE:1, MEDIUM_DATE:2, SHORT_DATE:3, FULL_TIME:4, LONG_TIME:5, MEDIUM_TIME:6, SHORT_TIME:7, FULL_DATETIME:8, LONG_DATETIME:9, MEDIUM_DATETIME:10, SHORT_DATETIME:11};
goog.i18n.DateTimeFormat.TOKENS_ = [/^'(?:[^']|'')*('|$)/, /^(?:G+|y+|Y+|M+|k+|S+|E+|a+|h+|K+|H+|c+|L+|Q+|d+|m+|s+|v+|V+|w+|z+|Z+)/, /^[^'GyYMkSEahKHcLQdmsvVwzZ]+/];
goog.i18n.DateTimeFormat.PartTypes_ = {QUOTED_STRING:0, FIELD:1, LITERAL:2};
goog.i18n.DateTimeFormat.getHours_ = function(date) {
  return date.getHours ? date.getHours() : 0;
};
goog.i18n.DateTimeFormat.prototype.applyPattern_ = function(pattern) {
  if (goog.i18n.DateTimeFormat.removeRlmInPatterns_) {
    pattern = pattern.replace(/\u200f/g, "");
  }
  while (pattern) {
    var previousPattern = pattern;
    for (var i = 0; i < goog.i18n.DateTimeFormat.TOKENS_.length; ++i) {
      var m = pattern.match(goog.i18n.DateTimeFormat.TOKENS_[i]);
      if (m) {
        var part = m[0];
        pattern = pattern.substring(part.length);
        if (i == goog.i18n.DateTimeFormat.PartTypes_.QUOTED_STRING) {
          if (part == "''") {
            part = "'";
          } else {
            part = part.substring(1, m[1] == "'" ? part.length - 1 : part.length);
            part = part.replace(/''/g, "'");
          }
        }
        this.patternParts_.push({text:part, type:i});
        break;
      }
    }
    if (previousPattern === pattern) {
      throw new Error("Malformed pattern part: " + pattern);
    }
  }
};
goog.i18n.DateTimeFormat.prototype.format = function(date, opt_timeZone) {
  if (!date) {
    throw new Error("The date to format must be non-null.");
  }
  var diff = opt_timeZone ? (date.getTimezoneOffset() - opt_timeZone.getOffset(date)) * 60000 : 0;
  var dateForDate = diff ? new Date(date.getTime() + diff) : date;
  var dateForTime = dateForDate;
  if (opt_timeZone && dateForDate.getTimezoneOffset() != date.getTimezoneOffset()) {
    var dstDiff = (dateForDate.getTimezoneOffset() - date.getTimezoneOffset()) * 60000;
    dateForDate = new Date(dateForDate.getTime() + dstDiff);
    diff += diff > 0 ? -goog.date.MS_PER_DAY : goog.date.MS_PER_DAY;
    dateForTime = new Date(date.getTime() + diff);
  }
  var out = [];
  for (var i = 0; i < this.patternParts_.length; ++i) {
    var text = this.patternParts_[i].text;
    if (goog.i18n.DateTimeFormat.PartTypes_.FIELD == this.patternParts_[i].type) {
      out.push(this.formatField_(text, date, dateForDate, dateForTime, opt_timeZone));
    } else {
      out.push(text);
    }
  }
  return out.join("");
};
goog.i18n.DateTimeFormat.prototype.applyStandardPattern_ = function(formatType) {
  var pattern;
  if (formatType < 4) {
    pattern = this.dateTimeSymbols_.DATEFORMATS[formatType];
  } else {
    if (formatType < 8) {
      pattern = this.dateTimeSymbols_.TIMEFORMATS[formatType - 4];
    } else {
      if (formatType < 12) {
        pattern = this.dateTimeSymbols_.DATETIMEFORMATS[formatType - 8];
        pattern = pattern.replace("{1}", this.dateTimeSymbols_.DATEFORMATS[formatType - 8]);
        pattern = pattern.replace("{0}", this.dateTimeSymbols_.TIMEFORMATS[formatType - 8]);
      } else {
        this.applyStandardPattern_(goog.i18n.DateTimeFormat.Format.MEDIUM_DATETIME);
        return;
      }
    }
  }
  this.applyPattern_(pattern);
};
goog.i18n.DateTimeFormat.prototype.localizeNumbers_ = function(input) {
  return goog.i18n.DateTimeFormat.localizeNumbers(input, this.dateTimeSymbols_);
};
goog.i18n.DateTimeFormat.enforceAsciiDigits_ = false;
goog.i18n.DateTimeFormat.removeRlmInPatterns_ = false;
goog.i18n.DateTimeFormat.setEnforceAsciiDigits = function(enforceAsciiDigits) {
  goog.i18n.DateTimeFormat.enforceAsciiDigits_ = enforceAsciiDigits;
  goog.i18n.DateTimeFormat.removeRlmInPatterns_ = enforceAsciiDigits;
};
goog.i18n.DateTimeFormat.isEnforceAsciiDigits = function() {
  return goog.i18n.DateTimeFormat.enforceAsciiDigits_;
};
goog.i18n.DateTimeFormat.localizeNumbers = function(input, opt_dateTimeSymbols) {
  input = String(input);
  var dateTimeSymbols = opt_dateTimeSymbols || goog.i18n.DateTimeSymbols;
  if (dateTimeSymbols.ZERODIGIT === undefined || goog.i18n.DateTimeFormat.enforceAsciiDigits_) {
    return input;
  }
  var parts = [];
  for (var i = 0; i < input.length; i++) {
    var c = input.charCodeAt(i);
    parts.push(48 <= c && c <= 57 ? String.fromCharCode(dateTimeSymbols.ZERODIGIT + c - 48) : input.charAt(i));
  }
  return parts.join("");
};
goog.i18n.DateTimeFormat.prototype.formatEra_ = function(count, date) {
  var value = date.getFullYear() > 0 ? 1 : 0;
  return count >= 4 ? this.dateTimeSymbols_.ERANAMES[value] : this.dateTimeSymbols_.ERAS[value];
};
goog.i18n.DateTimeFormat.prototype.formatYear_ = function(count, date) {
  var value = date.getFullYear();
  if (value < 0) {
    value = -value;
  }
  if (count == 2) {
    value = value % 100;
  }
  return this.localizeNumbers_(goog.string.padNumber(value, count));
};
goog.i18n.DateTimeFormat.prototype.formatYearOfWeek_ = function(count, date) {
  var value = goog.date.getYearOfWeek(date.getFullYear(), date.getMonth(), date.getDate(), this.dateTimeSymbols_.FIRSTWEEKCUTOFFDAY, this.dateTimeSymbols_.FIRSTDAYOFWEEK);
  if (value < 0) {
    value = -value;
  }
  if (count == 2) {
    value = value % 100;
  }
  return this.localizeNumbers_(goog.string.padNumber(value, count));
};
goog.i18n.DateTimeFormat.prototype.formatMonth_ = function(count, date) {
  var value = date.getMonth();
  switch(count) {
    case 5:
      return this.dateTimeSymbols_.NARROWMONTHS[value];
    case 4:
      return this.dateTimeSymbols_.MONTHS[value];
    case 3:
      return this.dateTimeSymbols_.SHORTMONTHS[value];
    default:
      return this.localizeNumbers_(goog.string.padNumber(value + 1, count));
  }
};
goog.i18n.DateTimeFormat.validateDateHasTime_ = function(date) {
  if (date.getHours && date.getSeconds && date.getMinutes) {
    return;
  }
  throw new Error("The date to format has no time (probably a goog.date.Date). " + "Use Date or goog.date.DateTime, or use a pattern without time fields.");
};
goog.i18n.DateTimeFormat.prototype.format24Hours_ = function(count, date) {
  goog.i18n.DateTimeFormat.validateDateHasTime_(date);
  var hours = goog.i18n.DateTimeFormat.getHours_(date) || 24;
  return this.localizeNumbers_(goog.string.padNumber(hours, count));
};
goog.i18n.DateTimeFormat.prototype.formatFractionalSeconds_ = function(count, date) {
  var value = date.getMilliseconds() / 1000;
  return this.localizeNumbers_(value.toFixed(Math.min(3, count)).substr(2) + (count > 3 ? goog.string.padNumber(0, count - 3) : ""));
};
goog.i18n.DateTimeFormat.prototype.formatDayOfWeek_ = function(count, date) {
  var value = date.getDay();
  return count >= 4 ? this.dateTimeSymbols_.WEEKDAYS[value] : this.dateTimeSymbols_.SHORTWEEKDAYS[value];
};
goog.i18n.DateTimeFormat.prototype.formatAmPm_ = function(count, date) {
  goog.i18n.DateTimeFormat.validateDateHasTime_(date);
  var hours = goog.i18n.DateTimeFormat.getHours_(date);
  return this.dateTimeSymbols_.AMPMS[hours >= 12 && hours < 24 ? 1 : 0];
};
goog.i18n.DateTimeFormat.prototype.format1To12Hours_ = function(count, date) {
  goog.i18n.DateTimeFormat.validateDateHasTime_(date);
  var hours = goog.i18n.DateTimeFormat.getHours_(date) % 12 || 12;
  return this.localizeNumbers_(goog.string.padNumber(hours, count));
};
goog.i18n.DateTimeFormat.prototype.format0To11Hours_ = function(count, date) {
  goog.i18n.DateTimeFormat.validateDateHasTime_(date);
  var hours = goog.i18n.DateTimeFormat.getHours_(date) % 12;
  return this.localizeNumbers_(goog.string.padNumber(hours, count));
};
goog.i18n.DateTimeFormat.prototype.format0To23Hours_ = function(count, date) {
  goog.i18n.DateTimeFormat.validateDateHasTime_(date);
  var hours = goog.i18n.DateTimeFormat.getHours_(date);
  return this.localizeNumbers_(goog.string.padNumber(hours, count));
};
goog.i18n.DateTimeFormat.prototype.formatStandaloneDay_ = function(count, date) {
  var value = date.getDay();
  switch(count) {
    case 5:
      return this.dateTimeSymbols_.STANDALONENARROWWEEKDAYS[value];
    case 4:
      return this.dateTimeSymbols_.STANDALONEWEEKDAYS[value];
    case 3:
      return this.dateTimeSymbols_.STANDALONESHORTWEEKDAYS[value];
    default:
      return this.localizeNumbers_(goog.string.padNumber(value, 1));
  }
};
goog.i18n.DateTimeFormat.prototype.formatStandaloneMonth_ = function(count, date) {
  var value = date.getMonth();
  switch(count) {
    case 5:
      return this.dateTimeSymbols_.STANDALONENARROWMONTHS[value];
    case 4:
      return this.dateTimeSymbols_.STANDALONEMONTHS[value];
    case 3:
      return this.dateTimeSymbols_.STANDALONESHORTMONTHS[value];
    default:
      return this.localizeNumbers_(goog.string.padNumber(value + 1, count));
  }
};
goog.i18n.DateTimeFormat.prototype.formatQuarter_ = function(count, date) {
  var value = Math.floor(date.getMonth() / 3);
  return count < 4 ? this.dateTimeSymbols_.SHORTQUARTERS[value] : this.dateTimeSymbols_.QUARTERS[value];
};
goog.i18n.DateTimeFormat.prototype.formatDate_ = function(count, date) {
  return this.localizeNumbers_(goog.string.padNumber(date.getDate(), count));
};
goog.i18n.DateTimeFormat.prototype.formatMinutes_ = function(count, date) {
  goog.i18n.DateTimeFormat.validateDateHasTime_(date);
  return this.localizeNumbers_(goog.string.padNumber(date.getMinutes(), count));
};
goog.i18n.DateTimeFormat.prototype.formatSeconds_ = function(count, date) {
  goog.i18n.DateTimeFormat.validateDateHasTime_(date);
  return this.localizeNumbers_(goog.string.padNumber(date.getSeconds(), count));
};
goog.i18n.DateTimeFormat.prototype.formatWeekOfYear_ = function(count, date) {
  var weekNum = goog.date.getWeekNumber(date.getFullYear(), date.getMonth(), date.getDate(), this.dateTimeSymbols_.FIRSTWEEKCUTOFFDAY, this.dateTimeSymbols_.FIRSTDAYOFWEEK);
  return this.localizeNumbers_(goog.string.padNumber(weekNum, count));
};
goog.i18n.DateTimeFormat.prototype.formatTimeZoneRFC_ = function(count, date, opt_timeZone) {
  opt_timeZone = opt_timeZone || goog.i18n.TimeZone.createTimeZone(date.getTimezoneOffset());
  return count < 4 ? opt_timeZone.getRFCTimeZoneString(date) : this.localizeNumbers_(opt_timeZone.getGMTString(date));
};
goog.i18n.DateTimeFormat.prototype.formatTimeZone_ = function(count, date, opt_timeZone) {
  opt_timeZone = opt_timeZone || goog.i18n.TimeZone.createTimeZone(date.getTimezoneOffset());
  return count < 4 ? opt_timeZone.getShortName(date) : opt_timeZone.getLongName(date);
};
goog.i18n.DateTimeFormat.prototype.formatTimeZoneId_ = function(date, opt_timeZone) {
  opt_timeZone = opt_timeZone || goog.i18n.TimeZone.createTimeZone(date.getTimezoneOffset());
  return opt_timeZone.getTimeZoneId();
};
goog.i18n.DateTimeFormat.prototype.formatTimeZoneLocationId_ = function(count, date, opt_timeZone) {
  opt_timeZone = opt_timeZone || goog.i18n.TimeZone.createTimeZone(date.getTimezoneOffset());
  return count <= 2 ? opt_timeZone.getTimeZoneId() : opt_timeZone.getGenericLocation(date);
};
goog.i18n.DateTimeFormat.prototype.formatField_ = function(patternStr, date, dateForDate, dateForTime, opt_timeZone) {
  var count = patternStr.length;
  switch(patternStr.charAt(0)) {
    case "G":
      return this.formatEra_(count, dateForDate);
    case "y":
      return this.formatYear_(count, dateForDate);
    case "Y":
      return this.formatYearOfWeek_(count, dateForDate);
    case "M":
      return this.formatMonth_(count, dateForDate);
    case "k":
      return this.format24Hours_(count, dateForTime);
    case "S":
      return this.formatFractionalSeconds_(count, dateForTime);
    case "E":
      return this.formatDayOfWeek_(count, dateForDate);
    case "a":
      return this.formatAmPm_(count, dateForTime);
    case "h":
      return this.format1To12Hours_(count, dateForTime);
    case "K":
      return this.format0To11Hours_(count, dateForTime);
    case "H":
      return this.format0To23Hours_(count, dateForTime);
    case "c":
      return this.formatStandaloneDay_(count, dateForDate);
    case "L":
      return this.formatStandaloneMonth_(count, dateForDate);
    case "Q":
      return this.formatQuarter_(count, dateForDate);
    case "d":
      return this.formatDate_(count, dateForDate);
    case "m":
      return this.formatMinutes_(count, dateForTime);
    case "s":
      return this.formatSeconds_(count, dateForTime);
    case "v":
      return this.formatTimeZoneId_(date, opt_timeZone);
    case "V":
      return this.formatTimeZoneLocationId_(count, date, opt_timeZone);
    case "w":
      return this.formatWeekOfYear_(count, dateForTime);
    case "z":
      return this.formatTimeZone_(count, date, opt_timeZone);
    case "Z":
      return this.formatTimeZoneRFC_(count, date, opt_timeZone);
    default:
      return "";
  }
};

//# sourceMappingURL=goog.i18n.datetimeformat.js.map
