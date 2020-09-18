import {default as $} from "jquery";
import {emojiIndex} from "emoji-mart";
import {default as textcomplete} from "textcomplete";

function createHTMLUnicode(unicode){
  return "&#x"+unicode+";"
}
function unicodeChar(unicode){
  if (unicode.indexOf("-") > -1){
    var unicodes = unicode.split("-");
    return encodeURI(unicodes.map(createHTMLUnicode).join(""));
  }else{
    return encodeURI(createHTMLUnicode(unicode));
  }
}

function emojiAutocomplete(selector) {
    var sel = selector || ".emoji-autocomplete";
    $(sel).textcomplete([ {
            match: /\B:([\-+\w]{1,30})$/,
            search: function (term, callback) {
                callback(emojiIndex.search(term));
            },
            unicodeFromShortname: function(shortname){
              return emojiIndex.emojis[shortname].unified;
            },
            imageTemplate: function(unicode){
              return unicodeChar(unicode);
            },
            SVGImageFromShortname: function(shortname){
              return emojiIndex.emojis[shortname].unified;
            },
            PNGImageFromShortname: function(shortname){
              var unicode = this.unicodeFromShortname(shortname);
              return this.imageTemplate(unicode);
            },
            template: function (emoji) {
              // Load emoji images one by one
              return emoji.native +' '+emoji.colons;
            },
            replace: function (emoji) {
                var unicode = emoji.unified;
                return this.imageTemplate(unicode);
            },
            index: 1
        }
        ],{
            zIndex: 1136,
            maxCount: 20
        }).on({'textComplete:select': function (e, value, strategy) {
          // This will make sure React inputs receive a change event
          // after the textcompletion has inserted new contents
          var event = new Event('input', { bubbles: true });
          strategy.el.dispatchEvent(event);
        }})
};

export default emojiAutocomplete;