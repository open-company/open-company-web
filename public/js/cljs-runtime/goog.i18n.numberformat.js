goog.provide("goog.i18n.NumberFormat");
goog.provide("goog.i18n.NumberFormat.CurrencyStyle");
goog.provide("goog.i18n.NumberFormat.Format");
goog.require("goog.asserts");
goog.require("goog.i18n.CompactNumberFormatSymbols");
goog.require("goog.i18n.NumberFormatSymbols");
goog.require("goog.i18n.NumberFormatSymbols_u_nu_latn");
goog.require("goog.i18n.currency");
goog.require("goog.math");
goog.require("goog.string");
goog.i18n.NumberFormat = function(pattern, opt_currency, opt_currencyStyle, opt_symbols) {
  if (opt_currency && !goog.i18n.currency.isValid(opt_currency)) {
    throw new TypeError("Currency must be valid ISO code");
  }
  this.intlCurrencyCode_ = opt_currency ? opt_currency.toUpperCase() : null;
  this.currencyStyle_ = opt_currencyStyle || goog.i18n.NumberFormat.CurrencyStyle.LOCAL;
  this.overrideNumberFormatSymbols_ = opt_symbols || null;
  this.maximumIntegerDigits_ = 40;
  this.minimumIntegerDigits_ = 1;
  this.significantDigits_ = 0;
  this.maximumFractionDigits_ = 3;
  this.minimumFractionDigits_ = 0;
  this.minExponentDigits_ = 0;
  this.useSignForPositiveExponent_ = false;
  this.showTrailingZeros_ = false;
  this.positivePrefix_ = "";
  this.positiveSuffix_ = "";
  this.negativePrefix_ = this.getNumberFormatSymbols_().MINUS_SIGN;
  this.negativeSuffix_ = "";
  this.multiplier_ = 1;
  this.negativePercentSignExpected_ = false;
  this.groupingArray_ = [];
  this.decimalSeparatorAlwaysShown_ = false;
  this.useExponentialNotation_ = false;
  this.compactStyle_ = goog.i18n.NumberFormat.CompactStyle.NONE;
  this.baseFormattingNumber_ = null;
  this.pattern_;
  if (typeof pattern == "number") {
    this.applyStandardPattern_(pattern);
  } else {
    this.applyPattern_(pattern);
  }
};
goog.i18n.NumberFormat.Format = {DECIMAL:1, SCIENTIFIC:2, PERCENT:3, CURRENCY:4, COMPACT_SHORT:5, COMPACT_LONG:6};
goog.i18n.NumberFormat.CurrencyStyle = {LOCAL:0, PORTABLE:1, GLOBAL:2};
goog.i18n.NumberFormat.CompactStyle = {NONE:0, SHORT:1, LONG:2};
goog.i18n.NumberFormat.enforceAsciiDigits_ = false;
goog.i18n.NumberFormat.setEnforceAsciiDigits = function(doEnforce) {
  goog.i18n.NumberFormat.enforceAsciiDigits_ = doEnforce;
};
goog.i18n.NumberFormat.isEnforceAsciiDigits = function() {
  return goog.i18n.NumberFormat.enforceAsciiDigits_;
};
goog.i18n.NumberFormat.prototype.getNumberFormatSymbols_ = function() {
  return this.overrideNumberFormatSymbols_ || (goog.i18n.NumberFormat.enforceAsciiDigits_ ? goog.i18n.NumberFormatSymbols_u_nu_latn : goog.i18n.NumberFormatSymbols);
};
goog.i18n.NumberFormat.prototype.getCurrencyCode_ = function() {
  return this.intlCurrencyCode_ || this.getNumberFormatSymbols_().DEF_CURRENCY_CODE;
};
goog.i18n.NumberFormat.prototype.setMinimumFractionDigits = function(min) {
  if (this.significantDigits_ > 0 && min > 0) {
    throw new Error("Can't combine significant digits and minimum fraction digits");
  }
  this.minimumFractionDigits_ = min;
  return this;
};
goog.i18n.NumberFormat.prototype.getMinimumFractionDigits = function() {
  return this.minimumFractionDigits_;
};
goog.i18n.NumberFormat.prototype.setMaximumFractionDigits = function(max) {
  if (max > 308) {
    throw new Error("Unsupported maximum fraction digits: " + max);
  }
  this.maximumFractionDigits_ = max;
  return this;
};
goog.i18n.NumberFormat.prototype.getMaximumFractionDigits = function() {
  return this.maximumFractionDigits_;
};
goog.i18n.NumberFormat.prototype.setSignificantDigits = function(number) {
  if (this.minimumFractionDigits_ > 0 && number >= 0) {
    throw new Error("Can't combine significant digits and minimum fraction digits");
  }
  this.significantDigits_ = number;
  return this;
};
goog.i18n.NumberFormat.prototype.getSignificantDigits = function() {
  return this.significantDigits_;
};
goog.i18n.NumberFormat.prototype.setShowTrailingZeros = function(showTrailingZeros) {
  this.showTrailingZeros_ = showTrailingZeros;
  return this;
};
goog.i18n.NumberFormat.prototype.setBaseFormatting = function(baseFormattingNumber) {
  goog.asserts.assert(baseFormattingNumber === null || isFinite(baseFormattingNumber));
  this.baseFormattingNumber_ = baseFormattingNumber;
  return this;
};
goog.i18n.NumberFormat.prototype.getBaseFormatting = function() {
  return this.baseFormattingNumber_;
};
goog.i18n.NumberFormat.prototype.applyPattern_ = function(pattern) {
  this.pattern_ = pattern.replace(/ /g, " ");
  var pos = [0];
  this.positivePrefix_ = this.parseAffix_(pattern, pos);
  var trunkStart = pos[0];
  this.parseTrunk_(pattern, pos);
  var trunkLen = pos[0] - trunkStart;
  this.positiveSuffix_ = this.parseAffix_(pattern, pos);
  if (pos[0] < pattern.length && pattern.charAt(pos[0]) == goog.i18n.NumberFormat.PATTERN_SEPARATOR_) {
    pos[0]++;
    if (this.multiplier_ != 1) {
      this.negativePercentSignExpected_ = true;
    }
    this.negativePrefix_ = this.parseAffix_(pattern, pos);
    pos[0] += trunkLen;
    this.negativeSuffix_ = this.parseAffix_(pattern, pos);
  } else {
    this.negativePrefix_ += this.positivePrefix_;
    this.negativeSuffix_ += this.positiveSuffix_;
  }
};
goog.i18n.NumberFormat.prototype.applyStandardPattern_ = function(patternType) {
  switch(patternType) {
    case goog.i18n.NumberFormat.Format.DECIMAL:
      this.applyPattern_(this.getNumberFormatSymbols_().DECIMAL_PATTERN);
      break;
    case goog.i18n.NumberFormat.Format.SCIENTIFIC:
      this.applyPattern_(this.getNumberFormatSymbols_().SCIENTIFIC_PATTERN);
      break;
    case goog.i18n.NumberFormat.Format.PERCENT:
      this.applyPattern_(this.getNumberFormatSymbols_().PERCENT_PATTERN);
      break;
    case goog.i18n.NumberFormat.Format.CURRENCY:
      this.applyPattern_(goog.i18n.currency.adjustPrecision(this.getNumberFormatSymbols_().CURRENCY_PATTERN, this.getCurrencyCode_()));
      break;
    case goog.i18n.NumberFormat.Format.COMPACT_SHORT:
      this.applyCompactStyle_(goog.i18n.NumberFormat.CompactStyle.SHORT);
      break;
    case goog.i18n.NumberFormat.Format.COMPACT_LONG:
      this.applyCompactStyle_(goog.i18n.NumberFormat.CompactStyle.LONG);
      break;
    default:
      throw new Error("Unsupported pattern type.");
  }
};
goog.i18n.NumberFormat.prototype.applyCompactStyle_ = function(style) {
  this.compactStyle_ = style;
  this.applyPattern_(this.getNumberFormatSymbols_().DECIMAL_PATTERN);
  this.setMinimumFractionDigits(0);
  this.setMaximumFractionDigits(2);
  this.setSignificantDigits(2);
};
goog.i18n.NumberFormat.prototype.parse = function(text, opt_pos) {
  var pos = opt_pos || [0];
  if (this.compactStyle_ != goog.i18n.NumberFormat.CompactStyle.NONE) {
    throw new Error("Parsing of compact numbers is unimplemented");
  }
  var ret = NaN;
  text = text.replace(/ |\u202f/g, " ");
  var gotPositive = text.indexOf(this.positivePrefix_, pos[0]) == pos[0];
  var gotNegative = text.indexOf(this.negativePrefix_, pos[0]) == pos[0];
  if (gotPositive && gotNegative) {
    if (this.positivePrefix_.length > this.negativePrefix_.length) {
      gotNegative = false;
    } else {
      if (this.positivePrefix_.length < this.negativePrefix_.length) {
        gotPositive = false;
      }
    }
  }
  if (gotPositive) {
    pos[0] += this.positivePrefix_.length;
  } else {
    if (gotNegative) {
      pos[0] += this.negativePrefix_.length;
    }
  }
  if (text.indexOf(this.getNumberFormatSymbols_().INFINITY, pos[0]) == pos[0]) {
    pos[0] += this.getNumberFormatSymbols_().INFINITY.length;
    ret = Infinity;
  } else {
    ret = this.parseNumber_(text, pos);
  }
  if (gotPositive) {
    if (!(text.indexOf(this.positiveSuffix_, pos[0]) == pos[0])) {
      return NaN;
    }
    pos[0] += this.positiveSuffix_.length;
  } else {
    if (gotNegative) {
      if (!(text.indexOf(this.negativeSuffix_, pos[0]) == pos[0])) {
        return NaN;
      }
      pos[0] += this.negativeSuffix_.length;
    }
  }
  return gotNegative ? -ret : ret;
};
goog.i18n.NumberFormat.prototype.parseNumber_ = function(text, pos) {
  var sawDecimal = false;
  var sawExponent = false;
  var sawDigit = false;
  var exponentPos = -1;
  var scale = 1;
  var decimal = this.getNumberFormatSymbols_().DECIMAL_SEP;
  var grouping = this.getNumberFormatSymbols_().GROUP_SEP;
  var exponentChar = this.getNumberFormatSymbols_().EXP_SYMBOL;
  if (this.compactStyle_ != goog.i18n.NumberFormat.CompactStyle.NONE) {
    throw new Error("Parsing of compact style numbers is not implemented");
  }
  grouping = grouping.replace(/\u202f/g, " ");
  var normalizedText = "";
  for (; pos[0] < text.length; pos[0]++) {
    var ch = text.charAt(pos[0]);
    var digit = this.getDigit_(ch);
    if (digit >= 0 && digit <= 9) {
      normalizedText += digit;
      sawDigit = true;
    } else {
      if (ch == decimal.charAt(0)) {
        if (sawDecimal || sawExponent) {
          break;
        }
        normalizedText += ".";
        sawDecimal = true;
      } else {
        if (ch == grouping.charAt(0) && (" " != grouping.charAt(0) || pos[0] + 1 < text.length && this.getDigit_(text.charAt(pos[0] + 1)) >= 0)) {
          if (sawDecimal || sawExponent) {
            break;
          }
          continue;
        } else {
          if (ch == exponentChar.charAt(0)) {
            if (sawExponent) {
              break;
            }
            normalizedText += "E";
            sawExponent = true;
            exponentPos = pos[0];
          } else {
            if (ch == "+" || ch == "-") {
              if (sawDigit && exponentPos != pos[0] - 1) {
                break;
              }
              normalizedText += ch;
            } else {
              if (this.multiplier_ == 1 && ch == this.getNumberFormatSymbols_().PERCENT.charAt(0)) {
                if (scale != 1) {
                  break;
                }
                scale = 100;
                if (sawDigit) {
                  pos[0]++;
                  break;
                }
              } else {
                if (this.multiplier_ == 1 && ch == this.getNumberFormatSymbols_().PERMILL.charAt(0)) {
                  if (scale != 1) {
                    break;
                  }
                  scale = 1000;
                  if (sawDigit) {
                    pos[0]++;
                    break;
                  }
                } else {
                  break;
                }
              }
            }
          }
        }
      }
    }
  }
  if (this.multiplier_ != 1) {
    scale = this.multiplier_;
  }
  return parseFloat(normalizedText) / scale;
};
goog.i18n.NumberFormat.prototype.format = function(number) {
  if (isNaN(number)) {
    return this.getNumberFormatSymbols_().NAN;
  }
  var parts = [];
  var baseFormattingNumber = this.baseFormattingNumber_ === null ? number : this.baseFormattingNumber_;
  var unit = this.getUnitAfterRounding_(baseFormattingNumber, number);
  number = goog.i18n.NumberFormat.decimalShift_(number, -unit.divisorBase);
  parts.push(unit.prefix);
  var isNegative = number < 0.0 || number == 0.0 && 1 / number < 0.0;
  parts.push(isNegative ? this.negativePrefix_ : this.positivePrefix_);
  if (!isFinite(number)) {
    parts.push(this.getNumberFormatSymbols_().INFINITY);
  } else {
    number *= isNegative ? -1 : 1;
    number *= this.multiplier_;
    this.useExponentialNotation_ ? this.subformatExponential_(number, parts) : this.subformatFixed_(number, this.minimumIntegerDigits_, parts);
  }
  parts.push(isNegative ? this.negativeSuffix_ : this.positiveSuffix_);
  parts.push(unit.suffix);
  return parts.join("");
};
goog.i18n.NumberFormat.prototype.roundNumber_ = function(number) {
  var shift = goog.i18n.NumberFormat.decimalShift_;
  var shiftedNumber = shift(number, this.maximumFractionDigits_);
  if (this.significantDigits_ > 0) {
    shiftedNumber = this.roundToSignificantDigits_(shiftedNumber, this.significantDigits_, this.maximumFractionDigits_);
  }
  shiftedNumber = Math.round(shiftedNumber);
  var intValue, fracValue;
  if (isFinite(shiftedNumber)) {
    intValue = Math.floor(shift(shiftedNumber, -this.maximumFractionDigits_));
    fracValue = Math.floor(shiftedNumber - shift(intValue, this.maximumFractionDigits_));
  } else {
    intValue = number;
    fracValue = 0;
  }
  return {intValue:intValue, fracValue:fracValue};
};
goog.i18n.NumberFormat.prototype.formatNumberGroupingRepeatingDigitsParts_ = function(parts, zeroCode, intPart, groupingArray, repeatedDigitLen) {
  var nonRepeatedGroupCompleteCount = 0;
  var currentGroupSizeIndex = 0;
  var currentGroupSize = 0;
  var grouping = this.getNumberFormatSymbols_().GROUP_SEP;
  var digitLen = intPart.length;
  for (var i = 0; i < digitLen; i++) {
    parts.push(String.fromCharCode(zeroCode + Number(intPart.charAt(i)) * 1));
    if (digitLen - i > 1) {
      currentGroupSize = groupingArray[currentGroupSizeIndex];
      if (i < repeatedDigitLen) {
        var repeatedDigitIndex = repeatedDigitLen - i;
        if (currentGroupSize === 1 || currentGroupSize > 0 && repeatedDigitIndex % currentGroupSize === 1) {
          parts.push(grouping);
        }
      } else {
        if (currentGroupSizeIndex < groupingArray.length) {
          if (i === repeatedDigitLen) {
            currentGroupSizeIndex += 1;
          } else {
            if (currentGroupSize === i - repeatedDigitLen - nonRepeatedGroupCompleteCount + 1) {
              parts.push(grouping);
              nonRepeatedGroupCompleteCount += currentGroupSize;
              currentGroupSizeIndex += 1;
            }
          }
        }
      }
    }
  }
  return parts;
};
goog.i18n.NumberFormat.prototype.formatNumberGroupingNonRepeatingDigitsParts_ = function(parts, zeroCode, intPart, groupingArray) {
  var grouping = this.getNumberFormatSymbols_().GROUP_SEP;
  var currentGroupSizeIndex;
  var currentGroupSize = 0;
  var digitLenLeft = intPart.length;
  var rightToLeftParts = [];
  for (currentGroupSizeIndex = groupingArray.length - 1; currentGroupSizeIndex >= 0 && digitLenLeft > 0; currentGroupSizeIndex--) {
    currentGroupSize = groupingArray[currentGroupSizeIndex];
    for (var rightDigitIndex = 0; rightDigitIndex < currentGroupSize && digitLenLeft - rightDigitIndex - 1 >= 0; rightDigitIndex++) {
      rightToLeftParts.push(String.fromCharCode(zeroCode + Number(intPart.charAt(digitLenLeft - rightDigitIndex - 1)) * 1));
    }
    digitLenLeft -= currentGroupSize;
    if (digitLenLeft > 0) {
      rightToLeftParts.push(grouping);
    }
  }
  parts.push.apply(parts, rightToLeftParts.reverse());
  return parts;
};
goog.i18n.NumberFormat.prototype.subformatFixed_ = function(number, minIntDigits, parts) {
  if (this.minimumFractionDigits_ > this.maximumFractionDigits_) {
    throw new Error("Min value must be less than max value");
  }
  if (!parts) {
    parts = [];
  }
  var rounded = this.roundNumber_(number);
  var intValue = rounded.intValue;
  var fracValue = rounded.fracValue;
  var numIntDigits = intValue == 0 ? 0 : this.intLog10_(intValue) + 1;
  var fractionPresent = this.minimumFractionDigits_ > 0 || fracValue > 0 || this.showTrailingZeros_ && numIntDigits < this.significantDigits_;
  var minimumFractionDigits = this.minimumFractionDigits_;
  if (fractionPresent) {
    if (this.showTrailingZeros_ && this.significantDigits_ > 0) {
      minimumFractionDigits = this.significantDigits_ - numIntDigits;
    } else {
      minimumFractionDigits = this.minimumFractionDigits_;
    }
  }
  var intPart = "";
  var translatableInt = intValue;
  while (translatableInt > 1E20) {
    intPart = "0" + intPart;
    translatableInt = Math.round(goog.i18n.NumberFormat.decimalShift_(translatableInt, -1));
  }
  intPart = translatableInt + intPart;
  var decimal = this.getNumberFormatSymbols_().DECIMAL_SEP;
  var zeroCode = this.getNumberFormatSymbols_().ZERO_DIGIT.charCodeAt(0);
  var digitLen = intPart.length;
  var nonRepeatedGroupCount = 0;
  if (intValue > 0 || minIntDigits > 0) {
    for (var i = digitLen; i < minIntDigits; i++) {
      parts.push(String.fromCharCode(zeroCode));
    }
    if (this.groupingArray_.length >= 2) {
      for (var j = 1; j < this.groupingArray_.length; j++) {
        nonRepeatedGroupCount += this.groupingArray_[j];
      }
    }
    var repeatedDigitLen = digitLen - nonRepeatedGroupCount;
    if (repeatedDigitLen > 0) {
      parts = this.formatNumberGroupingRepeatingDigitsParts_(parts, zeroCode, intPart, this.groupingArray_, repeatedDigitLen);
    } else {
      parts = this.formatNumberGroupingNonRepeatingDigitsParts_(parts, zeroCode, intPart, this.groupingArray_);
    }
  } else {
    if (!fractionPresent) {
      parts.push(String.fromCharCode(zeroCode));
    }
  }
  if (this.decimalSeparatorAlwaysShown_ || fractionPresent) {
    parts.push(decimal);
  }
  var fracPart = String(fracValue);
  var fracPartSplit = fracPart.split("e+");
  if (fracPartSplit.length == 2) {
    var floatFrac = parseFloat(fracPartSplit[0]);
    fracPart = String(this.roundToSignificantDigits_(floatFrac, this.significantDigits_, 1));
    fracPart = fracPart.replace(".", "");
    var exp = parseInt(fracPartSplit[1], 10);
    fracPart += goog.string.repeat("0", exp - fracPart.length + 1);
  }
  if (this.maximumFractionDigits_ + 1 > fracPart.length) {
    var zeroesToAdd = this.maximumFractionDigits_ - fracPart.length;
    fracPart = "1" + goog.string.repeat("0", zeroesToAdd) + fracPart;
  }
  var fracLen = fracPart.length;
  while (fracPart.charAt(fracLen - 1) == "0" && fracLen > minimumFractionDigits + 1) {
    fracLen--;
  }
  for (var i = 1; i < fracLen; i++) {
    parts.push(String.fromCharCode(zeroCode + Number(fracPart.charAt(i)) * 1));
  }
};
goog.i18n.NumberFormat.prototype.addExponentPart_ = function(exponent, parts) {
  parts.push(this.getNumberFormatSymbols_().EXP_SYMBOL);
  if (exponent < 0) {
    exponent = -exponent;
    parts.push(this.getNumberFormatSymbols_().MINUS_SIGN);
  } else {
    if (this.useSignForPositiveExponent_) {
      parts.push(this.getNumberFormatSymbols_().PLUS_SIGN);
    }
  }
  var exponentDigits = "" + exponent;
  var zeroChar = this.getNumberFormatSymbols_().ZERO_DIGIT;
  for (var i = exponentDigits.length; i < this.minExponentDigits_; i++) {
    parts.push(zeroChar);
  }
  parts.push(exponentDigits);
};
goog.i18n.NumberFormat.prototype.getMantissa_ = function(value, exponent) {
  return goog.i18n.NumberFormat.decimalShift_(value, -exponent);
};
goog.i18n.NumberFormat.prototype.subformatExponential_ = function(number, parts) {
  if (number == 0.0) {
    this.subformatFixed_(number, this.minimumIntegerDigits_, parts);
    this.addExponentPart_(0, parts);
    return;
  }
  var exponent = goog.math.safeFloor(Math.log(number) / Math.log(10));
  number = this.getMantissa_(number, exponent);
  var minIntDigits = this.minimumIntegerDigits_;
  if (this.maximumIntegerDigits_ > 1 && this.maximumIntegerDigits_ > this.minimumIntegerDigits_) {
    var remainder = exponent % this.maximumIntegerDigits_;
    if (remainder < 0) {
      remainder = this.maximumIntegerDigits_ + remainder;
    }
    number = goog.i18n.NumberFormat.decimalShift_(number, remainder);
    exponent -= remainder;
    minIntDigits = 1;
  } else {
    if (this.minimumIntegerDigits_ < 1) {
      exponent++;
      number = goog.i18n.NumberFormat.decimalShift_(number, -1);
    } else {
      exponent -= this.minimumIntegerDigits_ - 1;
      number = goog.i18n.NumberFormat.decimalShift_(number, this.minimumIntegerDigits_ - 1);
    }
  }
  this.subformatFixed_(number, minIntDigits, parts);
  this.addExponentPart_(exponent, parts);
};
goog.i18n.NumberFormat.prototype.getDigit_ = function(ch) {
  var code = ch.charCodeAt(0);
  if (48 <= code && code < 58) {
    return code - 48;
  } else {
    var zeroCode = this.getNumberFormatSymbols_().ZERO_DIGIT.charCodeAt(0);
    return zeroCode <= code && code < zeroCode + 10 ? code - zeroCode : -1;
  }
};
goog.i18n.NumberFormat.PATTERN_ZERO_DIGIT_ = "0";
goog.i18n.NumberFormat.PATTERN_GROUPING_SEPARATOR_ = ",";
goog.i18n.NumberFormat.PATTERN_DECIMAL_SEPARATOR_ = ".";
goog.i18n.NumberFormat.PATTERN_PER_MILLE_ = "‰";
goog.i18n.NumberFormat.PATTERN_PERCENT_ = "%";
goog.i18n.NumberFormat.PATTERN_DIGIT_ = "#";
goog.i18n.NumberFormat.PATTERN_SEPARATOR_ = ";";
goog.i18n.NumberFormat.PATTERN_EXPONENT_ = "E";
goog.i18n.NumberFormat.PATTERN_PLUS_ = "+";
goog.i18n.NumberFormat.PATTERN_CURRENCY_SIGN_ = "¤";
goog.i18n.NumberFormat.QUOTE_ = "'";
goog.i18n.NumberFormat.prototype.parseAffix_ = function(pattern, pos) {
  var affix = "";
  var inQuote = false;
  var len = pattern.length;
  for (; pos[0] < len; pos[0]++) {
    var ch = pattern.charAt(pos[0]);
    if (ch == goog.i18n.NumberFormat.QUOTE_) {
      if (pos[0] + 1 < len && pattern.charAt(pos[0] + 1) == goog.i18n.NumberFormat.QUOTE_) {
        pos[0]++;
        affix += "'";
      } else {
        inQuote = !inQuote;
      }
      continue;
    }
    if (inQuote) {
      affix += ch;
    } else {
      switch(ch) {
        case goog.i18n.NumberFormat.PATTERN_DIGIT_:
        case goog.i18n.NumberFormat.PATTERN_ZERO_DIGIT_:
        case goog.i18n.NumberFormat.PATTERN_GROUPING_SEPARATOR_:
        case goog.i18n.NumberFormat.PATTERN_DECIMAL_SEPARATOR_:
        case goog.i18n.NumberFormat.PATTERN_SEPARATOR_:
          return affix;
        case goog.i18n.NumberFormat.PATTERN_CURRENCY_SIGN_:
          if (pos[0] + 1 < len && pattern.charAt(pos[0] + 1) == goog.i18n.NumberFormat.PATTERN_CURRENCY_SIGN_) {
            pos[0]++;
            affix += this.getCurrencyCode_();
          } else {
            switch(this.currencyStyle_) {
              case goog.i18n.NumberFormat.CurrencyStyle.LOCAL:
                affix += goog.i18n.currency.getLocalCurrencySignWithFallback(this.getCurrencyCode_());
                break;
              case goog.i18n.NumberFormat.CurrencyStyle.GLOBAL:
                affix += goog.i18n.currency.getGlobalCurrencySignWithFallback(this.getCurrencyCode_());
                break;
              case goog.i18n.NumberFormat.CurrencyStyle.PORTABLE:
                affix += goog.i18n.currency.getPortableCurrencySignWithFallback(this.getCurrencyCode_());
                break;
              default:
                break;
            }
          }
          break;
        case goog.i18n.NumberFormat.PATTERN_PERCENT_:
          if (!this.negativePercentSignExpected_ && this.multiplier_ != 1) {
            throw new Error("Too many percent/permill");
          } else {
            if (this.negativePercentSignExpected_ && this.multiplier_ != 100) {
              throw new Error("Inconsistent use of percent/permill characters");
            }
          }
          this.multiplier_ = 100;
          this.negativePercentSignExpected_ = false;
          affix += this.getNumberFormatSymbols_().PERCENT;
          break;
        case goog.i18n.NumberFormat.PATTERN_PER_MILLE_:
          if (!this.negativePercentSignExpected_ && this.multiplier_ != 1) {
            throw new Error("Too many percent/permill");
          } else {
            if (this.negativePercentSignExpected_ && this.multiplier_ != 1000) {
              throw new Error("Inconsistent use of percent/permill characters");
            }
          }
          this.multiplier_ = 1000;
          this.negativePercentSignExpected_ = false;
          affix += this.getNumberFormatSymbols_().PERMILL;
          break;
        default:
          affix += ch;
      }
    }
  }
  return affix;
};
goog.i18n.NumberFormat.prototype.parseTrunk_ = function(pattern, pos) {
  var decimalPos = -1;
  var digitLeftCount = 0;
  var zeroDigitCount = 0;
  var digitRightCount = 0;
  var groupingCount = -1;
  var len = pattern.length;
  for (var loop = true; pos[0] < len && loop; pos[0]++) {
    var ch = pattern.charAt(pos[0]);
    switch(ch) {
      case goog.i18n.NumberFormat.PATTERN_DIGIT_:
        if (zeroDigitCount > 0) {
          digitRightCount++;
        } else {
          digitLeftCount++;
        }
        if (groupingCount >= 0 && decimalPos < 0) {
          groupingCount++;
        }
        break;
      case goog.i18n.NumberFormat.PATTERN_ZERO_DIGIT_:
        if (digitRightCount > 0) {
          throw new Error('Unexpected "0" in pattern "' + pattern + '"');
        }
        zeroDigitCount++;
        if (groupingCount >= 0 && decimalPos < 0) {
          groupingCount++;
        }
        break;
      case goog.i18n.NumberFormat.PATTERN_GROUPING_SEPARATOR_:
        if (groupingCount > 0) {
          this.groupingArray_.push(groupingCount);
        }
        groupingCount = 0;
        break;
      case goog.i18n.NumberFormat.PATTERN_DECIMAL_SEPARATOR_:
        if (decimalPos >= 0) {
          throw new Error('Multiple decimal separators in pattern "' + pattern + '"');
        }
        decimalPos = digitLeftCount + zeroDigitCount + digitRightCount;
        break;
      case goog.i18n.NumberFormat.PATTERN_EXPONENT_:
        if (this.useExponentialNotation_) {
          throw new Error('Multiple exponential symbols in pattern "' + pattern + '"');
        }
        this.useExponentialNotation_ = true;
        this.minExponentDigits_ = 0;
        if (pos[0] + 1 < len && pattern.charAt(pos[0] + 1) == goog.i18n.NumberFormat.PATTERN_PLUS_) {
          pos[0]++;
          this.useSignForPositiveExponent_ = true;
        }
        while (pos[0] + 1 < len && pattern.charAt(pos[0] + 1) == goog.i18n.NumberFormat.PATTERN_ZERO_DIGIT_) {
          pos[0]++;
          this.minExponentDigits_++;
        }
        if (digitLeftCount + zeroDigitCount < 1 || this.minExponentDigits_ < 1) {
          throw new Error('Malformed exponential pattern "' + pattern + '"');
        }
        loop = false;
        break;
      default:
        pos[0]--;
        loop = false;
        break;
    }
  }
  if (zeroDigitCount == 0 && digitLeftCount > 0 && decimalPos >= 0) {
    var n = decimalPos;
    if (n == 0) {
      n++;
    }
    digitRightCount = digitLeftCount - n;
    digitLeftCount = n - 1;
    zeroDigitCount = 1;
  }
  if (decimalPos < 0 && digitRightCount > 0 || decimalPos >= 0 && (decimalPos < digitLeftCount || decimalPos > digitLeftCount + zeroDigitCount) || groupingCount == 0) {
    throw new Error('Malformed pattern "' + pattern + '"');
  }
  var totalDigits = digitLeftCount + zeroDigitCount + digitRightCount;
  this.maximumFractionDigits_ = decimalPos >= 0 ? totalDigits - decimalPos : 0;
  if (decimalPos >= 0) {
    this.minimumFractionDigits_ = digitLeftCount + zeroDigitCount - decimalPos;
    if (this.minimumFractionDigits_ < 0) {
      this.minimumFractionDigits_ = 0;
    }
  }
  var effectiveDecimalPos = decimalPos >= 0 ? decimalPos : totalDigits;
  this.minimumIntegerDigits_ = effectiveDecimalPos - digitLeftCount;
  if (this.useExponentialNotation_) {
    this.maximumIntegerDigits_ = digitLeftCount + this.minimumIntegerDigits_;
    if (this.maximumFractionDigits_ == 0 && this.minimumIntegerDigits_ == 0) {
      this.minimumIntegerDigits_ = 1;
    }
  }
  this.groupingArray_.push(Math.max(0, groupingCount));
  this.decimalSeparatorAlwaysShown_ = decimalPos == 0 || decimalPos == totalDigits;
};
goog.i18n.NumberFormat.CompactNumberUnit;
goog.i18n.NumberFormat.NULL_UNIT_ = {prefix:"", suffix:"", divisorBase:0};
goog.i18n.NumberFormat.prototype.getUnitFor_ = function(base, plurality) {
  var table = this.compactStyle_ == goog.i18n.NumberFormat.CompactStyle.SHORT ? goog.i18n.CompactNumberFormatSymbols.COMPACT_DECIMAL_SHORT_PATTERN : goog.i18n.CompactNumberFormatSymbols.COMPACT_DECIMAL_LONG_PATTERN;
  if (table == null) {
    table = goog.i18n.CompactNumberFormatSymbols.COMPACT_DECIMAL_SHORT_PATTERN;
  }
  if (base < 3) {
    return goog.i18n.NumberFormat.NULL_UNIT_;
  } else {
    var shift = goog.i18n.NumberFormat.decimalShift_;
    base = Math.min(14, base);
    var patterns = table[shift(1, base)];
    var previousNonNullBase = base - 1;
    while (!patterns && previousNonNullBase >= 3) {
      patterns = table[shift(1, previousNonNullBase)];
      previousNonNullBase--;
    }
    if (!patterns) {
      return goog.i18n.NumberFormat.NULL_UNIT_;
    }
    var pattern = patterns[plurality];
    if (!pattern || pattern == "0") {
      return goog.i18n.NumberFormat.NULL_UNIT_;
    }
    var parts = /([^0]*)(0+)(.*)/.exec(pattern);
    if (!parts) {
      return goog.i18n.NumberFormat.NULL_UNIT_;
    }
    return {prefix:parts[1], suffix:parts[3], divisorBase:previousNonNullBase + 1 - (parts[2].length - 1)};
  }
};
goog.i18n.NumberFormat.prototype.getUnitAfterRounding_ = function(formattingNumber, pluralityNumber) {
  if (this.compactStyle_ == goog.i18n.NumberFormat.CompactStyle.NONE) {
    return goog.i18n.NumberFormat.NULL_UNIT_;
  }
  formattingNumber = Math.abs(formattingNumber);
  pluralityNumber = Math.abs(pluralityNumber);
  var initialPlurality = this.pluralForm_(formattingNumber);
  var base = formattingNumber <= 1 ? 0 : this.intLog10_(formattingNumber);
  var initialDivisor = this.getUnitFor_(base, initialPlurality).divisorBase;
  var pluralityAttempt = goog.i18n.NumberFormat.decimalShift_(pluralityNumber, -initialDivisor);
  var pluralityRounded = this.roundNumber_(pluralityAttempt);
  var formattingAttempt = goog.i18n.NumberFormat.decimalShift_(formattingNumber, -initialDivisor);
  var formattingRounded = this.roundNumber_(formattingAttempt);
  var finalPlurality = this.pluralForm_(pluralityRounded.intValue + pluralityRounded.fracValue);
  return this.getUnitFor_(initialDivisor + this.intLog10_(formattingRounded.intValue), finalPlurality);
};
goog.i18n.NumberFormat.prototype.intLog10_ = function(number) {
  if (!isFinite(number)) {
    return number > 0 ? number : 0;
  }
  var i = 0;
  while ((number /= 10) >= 1) {
    i++;
  }
  return i;
};
goog.i18n.NumberFormat.decimalShift_ = function(number, digitCount) {
  goog.asserts.assert(digitCount % 1 == 0, 'Cannot shift by fractional digits "%s".', digitCount);
  if (!number || !isFinite(number) || digitCount == 0) {
    return number;
  }
  var numParts = String(number).split("e");
  var magnitude = parseInt(numParts[1] || 0, 10) + digitCount;
  return parseFloat(numParts[0] + "e" + magnitude);
};
goog.i18n.NumberFormat.decimalRound_ = function(number, decimalCount) {
  goog.asserts.assert(decimalCount % 1 == 0, 'Cannot round to fractional digits "%s".', decimalCount);
  if (!number || !isFinite(number)) {
    return number;
  }
  var shift = goog.i18n.NumberFormat.decimalShift_;
  return shift(Math.round(shift(number, decimalCount)), -decimalCount);
};
goog.i18n.NumberFormat.prototype.roundToSignificantDigits_ = function(number, significantDigits, scale) {
  if (!number) {
    return number;
  }
  var digits = this.intLog10_(number);
  var magnitude = significantDigits - digits - 1;
  if (magnitude < -scale) {
    return goog.i18n.NumberFormat.decimalRound_(number, -scale);
  } else {
    return goog.i18n.NumberFormat.decimalRound_(number, magnitude);
  }
};
goog.i18n.NumberFormat.prototype.pluralForm_ = function(quantity) {
  return "other";
};
goog.i18n.NumberFormat.prototype.isCurrencyCodeBeforeValue = function() {
  var posCurrSymbol = this.pattern_.indexOf("¤");
  var posPound = this.pattern_.indexOf("#");
  var posZero = this.pattern_.indexOf("0");
  var posCurrValue = Number.MAX_VALUE;
  if (posPound >= 0 && posPound < posCurrValue) {
    posCurrValue = posPound;
  }
  if (posZero >= 0 && posZero < posCurrValue) {
    posCurrValue = posZero;
  }
  return posCurrSymbol < posCurrValue;
};

//# sourceMappingURL=goog.i18n.numberformat.js.map
