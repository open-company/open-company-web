goog.provide('oc.web.components.ui.emoji_picker');
oc.web.components.ui.emoji_picker.emojiable_class = "emojiable";
oc.web.components.ui.emoji_picker.emojiable_active_QMARK_ = (function oc$web$components$ui$emoji_picker$emojiable_active_QMARK_(){
return (document.activeElement.className.indexOf(oc.web.components.ui.emoji_picker.emojiable_class) >= (0));
});
oc.web.components.ui.emoji_picker.remove_markers = (function oc$web$components$ui$emoji_picker$remove_markers(s){
var temp__5735__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.emoji-picker","caret-pos","oc.web.components.ui.emoji-picker/caret-pos",691962097).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(temp__5735__auto__)){
var caret_pos = temp__5735__auto__;
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(caret_pos),"rangy")){
return rangy.removeMarkers(new cljs.core.Keyword(null,"selection","selection",975998651).cljs$core$IFn$_invoke$arity$1(caret_pos));
} else {
return null;
}
} else {
return null;
}
});
oc.web.components.ui.emoji_picker.on_click_out = (function oc$web$components$ui$emoji_picker$on_click_out(s,e){
if(cljs.core.truth_(oc.web.lib.utils.event_inside_QMARK_(e,rum.core.ref_node(s,"emoji-picker")))){
return null;
} else {
oc.web.components.ui.emoji_picker.remove_markers(s);

if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.emoji-picker","visible","oc.web.components.ui.emoji-picker/visible",-2022661816).cljs$core$IFn$_invoke$arity$1(s)))){
var will_close_picker_45788 = new cljs.core.Keyword(null,"will-close-picker","will-close-picker",695931188).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s)));
if(cljs.core.fn_QMARK_(will_close_picker_45788)){
(will_close_picker_45788.cljs$core$IFn$_invoke$arity$0 ? will_close_picker_45788.cljs$core$IFn$_invoke$arity$0() : will_close_picker_45788.call(null));
} else {
}
} else {
}

if(cljs.core.truth_(oc.shared.useragent.mobile_QMARK_)){
oc.web.utils.dom.unlock_page_scroll();
} else {
}

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.emoji-picker","visible","oc.web.components.ui.emoji-picker/visible",-2022661816).cljs$core$IFn$_invoke$arity$1(s),false);
}
});
oc.web.components.ui.emoji_picker.save_caret_position = (function oc$web$components$ui$emoji_picker$save_caret_position(s){
oc.web.components.ui.emoji_picker.remove_markers(s);

var caret_pos = new cljs.core.Keyword("oc.web.components.ui.emoji-picker","caret-pos","oc.web.components.ui.emoji-picker/caret-pos",691962097).cljs$core$IFn$_invoke$arity$1(s);
var emojiable_active = oc.web.components.ui.emoji_picker.emojiable_active_QMARK_();
var active_element = document.activeElement;
if(emojiable_active){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.emoji-picker","last-active-element","oc.web.components.ui.emoji-picker/last-active-element",47170803).cljs$core$IFn$_invoke$arity$1(s),active_element);

return cljs.core.reset_BANG_(caret_pos,(cljs.core.truth_((function (){var G__45765 = active_element.tagName;
var fexpr__45764 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, ["INPUT",null,"TEXTAREA",null], null), null);
return (fexpr__45764.cljs$core$IFn$_invoke$arity$1 ? fexpr__45764.cljs$core$IFn$_invoke$arity$1(G__45765) : fexpr__45764.call(null,G__45765));
})())?new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),"default",new cljs.core.Keyword(null,"selection","selection",975998651),OCStaticTextareaSaveSelection()], null):new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),"rangy",new cljs.core.Keyword(null,"selection","selection",975998651),rangy.saveSelection(window)], null)));
} else {
return cljs.core.reset_BANG_(caret_pos,null);
}
});
oc.web.components.ui.emoji_picker.replace_with_emoji = (function oc$web$components$ui$emoji_picker$replace_with_emoji(s,emoji){
var temp__5735__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.emoji-picker","caret-pos","oc.web.components.ui.emoji-picker/caret-pos",691962097).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(temp__5735__auto__)){
var caret_pos = temp__5735__auto__;
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(caret_pos),"rangy")){
rangy.restoreSelection(new cljs.core.Keyword(null,"selection","selection",975998651).cljs$core$IFn$_invoke$arity$1(caret_pos));

return pasteHtmlAtCaret(goog.object.get(emoji,"native"),rangy.getSelection(window),false);
} else {
OCStaticTextareaRestoreSelection(new cljs.core.Keyword(null,"selection","selection",975998651).cljs$core$IFn$_invoke$arity$1(caret_pos));

return pasteTextAtSelection(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.emoji-picker","last-active-element","oc.web.components.ui.emoji-picker/last-active-element",47170803).cljs$core$IFn$_invoke$arity$1(s)),goog.object.get(emoji,"native"));
}
} else {
return null;
}
});
oc.web.components.ui.emoji_picker.check_focus = (function oc$web$components$ui$emoji_picker$check_focus(s,_){
var container_selector = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"container-selector","container-selector",6506114).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s)));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "document.body";
}
})();
var container_node = document.querySelector(container_selector);
var active_element = document.activeElement;
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.emoji-picker","disabled","oc.web.components.ui.emoji-picker/disabled",-413775631).cljs$core$IFn$_invoke$arity$1(s),(((!(oc.web.components.ui.emoji_picker.emojiable_active_QMARK_()))) || (cljs.core.not(container_node)) || (cljs.core.not(container_node.contains(active_element)))));
});
oc.web.components.ui.emoji_picker.emoji_picker = rum.core.build_defcs((function (s,p__45766){
var map__45767 = p__45766;
var map__45767__$1 = (((((!((map__45767 == null))))?(((((map__45767.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45767.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45767):map__45767);
var arg = map__45767__$1;
var will_open_picker = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45767__$1,new cljs.core.Keyword(null,"will-open-picker","will-open-picker",1855565402));
var force_enabled = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45767__$1,new cljs.core.Keyword(null,"force-enabled","force-enabled",1526826590));
var height = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__45767__$1,new cljs.core.Keyword(null,"height","height",1025178622),(25));
var container_selector = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45767__$1,new cljs.core.Keyword(null,"container-selector","container-selector",6506114));
var width = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__45767__$1,new cljs.core.Keyword(null,"width","width",-384071477),(25));
var add_emoji_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45767__$1,new cljs.core.Keyword(null,"add-emoji-cb","add-emoji-cb",-375710574));
var will_close_picker = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45767__$1,new cljs.core.Keyword(null,"will-close-picker","will-close-picker",695931188));
var tooltip_position = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45767__$1,new cljs.core.Keyword(null,"tooltip-position","tooltip-position",936197013));
var default_field_selector = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45767__$1,new cljs.core.Keyword(null,"default-field-selector","default-field-selector",179085335));
var position = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__45767__$1,new cljs.core.Keyword(null,"position","position",-2011731912),"top");
var visible = new cljs.core.Keyword("oc.web.components.ui.emoji-picker","visible","oc.web.components.ui.emoji-picker/visible",-2022661816).cljs$core$IFn$_invoke$arity$1(s);
var caret_pos = new cljs.core.Keyword("oc.web.components.ui.emoji-picker","caret-pos","oc.web.components.ui.emoji-picker/caret-pos",691962097).cljs$core$IFn$_invoke$arity$1(s);
var last_active_element = new cljs.core.Keyword("oc.web.components.ui.emoji-picker","last-active-element","oc.web.components.ui.emoji-picker/last-active-element",47170803).cljs$core$IFn$_invoke$arity$1(s);
var disabled = new cljs.core.Keyword("oc.web.components.ui.emoji-picker","disabled","oc.web.components.ui.emoji-picker/disabled",-413775631).cljs$core$IFn$_invoke$arity$1(s);
return React.createElement("div",({"ref": "emoji-picker", "style": ({"width": [cljs.core.str.cljs$core$IFn$_invoke$arity$1(width),"px"].join(''), "height": [cljs.core.str.cljs$core$IFn$_invoke$arity$1(height),"px"].join('')}), "className": "emoji-picker group"}),React.createElement("button",({"type": "button", "title": "Insert emoji", "data-placement": (function (){var or__4126__auto__ = tooltip_position;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "top";
}
})(), "data-container": "body", "data-toggle": "tooltip", "disabled": ((cljs.core.not(default_field_selector))?((cljs.core.not(force_enabled))?cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.emoji-picker","disabled","oc.web.components.ui.emoji-picker/disabled",-413775631).cljs$core$IFn$_invoke$arity$1(s)):false):false), "onMouseDown": (function (){
if(cljs.core.truth_((function (){var or__4126__auto__ = default_field_selector;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = force_enabled;
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
return cljs.core.not(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.emoji-picker","disabled","oc.web.components.ui.emoji-picker/disabled",-413775631).cljs$core$IFn$_invoke$arity$1(s)));
}
}
})())){
oc.web.components.ui.emoji_picker.save_caret_position(s);

var vis = (function (){var and__4115__auto__ = (function (){var or__4126__auto__ = default_field_selector;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = force_enabled;
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
return cljs.core.deref(caret_pos);
}
}
})();
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(cljs.core.deref(visible));
} else {
return and__4115__auto__;
}
})();
if(cljs.core.truth_(vis)){
if(cljs.core.truth_(oc.shared.useragent.mobile_QMARK_)){
oc.web.utils.dom.lock_page_scroll();
} else {
}

if(cljs.core.fn_QMARK_(will_open_picker)){
(will_open_picker.cljs$core$IFn$_invoke$arity$1 ? will_open_picker.cljs$core$IFn$_invoke$arity$1(vis) : will_open_picker.call(null,vis));
} else {
}
} else {
if(cljs.core.fn_QMARK_(will_close_picker)){
(will_close_picker.cljs$core$IFn$_invoke$arity$1 ? will_close_picker.cljs$core$IFn$_invoke$arity$1(vis) : will_close_picker.call(null,vis));
} else {
}

if(cljs.core.truth_(oc.shared.useragent.mobile_QMARK_)){
oc.web.utils.dom.unlock_page_scroll();
} else {
}
}

return cljs.core.reset_BANG_(visible,vis);
} else {
return null;
}
}), "className": "emoji-button btn-reset"})),sablono.interpreter.interpret((cljs.core.truth_(cljs.core.deref(visible))?new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.picker-container","div.picker-container",34217701),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.class_set(cljs.core.PersistentArrayMap.createAsIfByAssoc([position,true]))], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.mobile-cancel-bt","button.mlb-reset.mobile-cancel-bt",-1939477029),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
oc.web.components.ui.emoji_picker.remove_markers(s);

if(cljs.core.fn_QMARK_(will_close_picker)){
(will_close_picker.cljs$core$IFn$_invoke$arity$0 ? will_close_picker.cljs$core$IFn$_invoke$arity$0() : will_close_picker.call(null));
} else {
}

return cljs.core.reset_BANG_(visible,false);
})], null),"Cancel"], null),((oc.web.lib.utils.is_test_env_QMARK_())?null:oc.web.lib.react_utils.build(EmojiMart.Picker,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"native","native",-613060878),true,new cljs.core.Keyword(null,"autoFocus","autoFocus",-552622425),true,new cljs.core.Keyword(null,"onClick","onClick",-1991238530),(function (emoji,event){
if(cljs.core.truth_((function (){var and__4115__auto__ = default_field_selector;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.emoji-picker","caret-pos","oc.web.components.ui.emoji-picker/caret-pos",691962097).cljs$core$IFn$_invoke$arity$1(s)));
} else {
return and__4115__auto__;
}
})())){
oc.web.lib.utils.to_end_of_content_editable(document.querySelector(default_field_selector));

oc.web.components.ui.emoji_picker.save_caret_position(s);
} else {
}

var add_emoji_QMARK_ = cljs.core.boolean$(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.emoji-picker","caret-pos","oc.web.components.ui.emoji-picker/caret-pos",691962097).cljs$core$IFn$_invoke$arity$1(s)));
if(add_emoji_QMARK_){
oc.web.components.ui.emoji_picker.replace_with_emoji(s,emoji);

oc.web.components.ui.emoji_picker.remove_markers(s);

cljs.core.deref(last_active_element).focus();
} else {
}

if(cljs.core.fn_QMARK_(will_close_picker)){
(will_close_picker.cljs$core$IFn$_invoke$arity$0 ? will_close_picker.cljs$core$IFn$_invoke$arity$0() : will_close_picker.call(null));
} else {
}

cljs.core.reset_BANG_(visible,false);

if(cljs.core.fn_QMARK_(add_emoji_cb)){
var G__45778 = cljs.core.deref(last_active_element);
var G__45779 = emoji;
var G__45780 = add_emoji_QMARK_;
return (add_emoji_cb.cljs$core$IFn$_invoke$arity$3 ? add_emoji_cb.cljs$core$IFn$_invoke$arity$3(G__45778,G__45779,G__45780) : add_emoji_cb.call(null,G__45778,G__45779,G__45780));
} else {
return null;
}
})], null)))], null):null)));
}),new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.emoji-picker","visible","oc.web.components.ui.emoji-picker/visible",-2022661816)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.emoji-picker","caret-pos","oc.web.components.ui.emoji-picker/caret-pos",691962097)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.emoji-picker","last-active-element","oc.web.components.ui.emoji-picker/last-active-element",47170803)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.emoji-picker","disabled","oc.web.components.ui.emoji-picker/disabled",-413775631)),oc.web.mixins.ui.on_window_click_mixin(oc.web.components.ui.emoji_picker.on_click_out),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"init","init",-1875481434),(function (s,p){
rangy.init();

return s;
}),new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
oc.web.components.ui.emoji_picker.check_focus(s,null);

var focusin = goog.events.listen(document,goog.events.EventType.FOCUSIN,cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.components.ui.emoji_picker.check_focus,s));
var focusout = goog.events.listen(document,goog.events.EventType.FOCUSOUT,cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.components.ui.emoji_picker.check_focus,s));
var ff_click = (cljs.core.truth_(oc.shared.useragent.firefox_QMARK_)?goog.events.listen(window,goog.events.EventType.CLICK,cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.components.ui.emoji_picker.check_focus,s)):null);
var ff_keypress = (cljs.core.truth_(oc.shared.useragent.firefox_QMARK_)?goog.events.listen(window,goog.events.EventType.KEYPRESS,cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.components.ui.emoji_picker.check_focus,s)):null);
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([s,new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword("oc.web.components.ui.emoji-picker","focusin-listener","oc.web.components.ui.emoji-picker/focusin-listener",-541796955),focusin,new cljs.core.Keyword("oc.web.components.ui.emoji-picker","focusout-listener","oc.web.components.ui.emoji-picker/focusout-listener",-797633021),focusout,new cljs.core.Keyword("oc.web.components.ui.emoji-picker","ff-window-click","oc.web.components.ui.emoji-picker/ff-window-click",-708125160),ff_click,new cljs.core.Keyword("oc.web.components.ui.emoji-picker","ff-keypress","oc.web.components.ui.emoji-picker/ff-keypress",982780267),ff_keypress], null)], 0));
}),new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
oc.web.lib.utils.after((100),(function (){
return oc.web.components.ui.emoji_picker.check_focus(s,null);
}));

return s;
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
goog.events.unlistenByKey(new cljs.core.Keyword("oc.web.components.ui.emoji-picker","focusin-listener","oc.web.components.ui.emoji-picker/focusin-listener",-541796955).cljs$core$IFn$_invoke$arity$1(s));

goog.events.unlistenByKey(new cljs.core.Keyword("oc.web.components.ui.emoji-picker","focusout-listener","oc.web.components.ui.emoji-picker/focusout-listener",-797633021).cljs$core$IFn$_invoke$arity$1(s));

if(cljs.core.truth_(new cljs.core.Keyword("oc.web.components.ui.emoji-picker","ff-window-click","oc.web.components.ui.emoji-picker/ff-window-click",-708125160).cljs$core$IFn$_invoke$arity$1(s))){
goog.events.unlistenByKey(new cljs.core.Keyword("oc.web.components.ui.emoji-picker","ff-window-click","oc.web.components.ui.emoji-picker/ff-window-click",-708125160).cljs$core$IFn$_invoke$arity$1(s));
} else {
}

if(cljs.core.truth_(new cljs.core.Keyword("oc.web.components.ui.emoji-picker","ff-keypress","oc.web.components.ui.emoji-picker/ff-keypress",982780267).cljs$core$IFn$_invoke$arity$1(s))){
goog.events.unlistenByKey(new cljs.core.Keyword("oc.web.components.ui.emoji-picker","ff-keypress","oc.web.components.ui.emoji-picker/ff-keypress",982780267).cljs$core$IFn$_invoke$arity$1(s));
} else {
}

return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(s,new cljs.core.Keyword("oc.web.components.ui.emoji-picker","focusin-listener","oc.web.components.ui.emoji-picker/focusin-listener",-541796955),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword("oc.web.components.ui.emoji-picker","focusout-listener","oc.web.components.ui.emoji-picker/focusout-listener",-797633021),new cljs.core.Keyword("oc.web.components.ui.emoji-picker","ff-window-click","oc.web.components.ui.emoji-picker/ff-window-click",-708125160),new cljs.core.Keyword("oc.web.components.ui.emoji-picker","ff-keypress","oc.web.components.ui.emoji-picker/ff-keypress",982780267)], 0));
})], null)], null),"emoji-picker");

//# sourceMappingURL=oc.web.components.ui.emoji_picker.js.map
