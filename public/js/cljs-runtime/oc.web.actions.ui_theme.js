goog.provide('oc.web.actions.ui_theme');
oc.web.actions.ui_theme.ui_theme_cookie_name_suffix = new cljs.core.Keyword(null,"ui-theme","ui-theme",1992064701);
oc.web.actions.ui_theme.ui_theme_class_name_prefix = new cljs.core.Keyword(null,"theme-mode","theme-mode",-30029992);
oc.web.actions.ui_theme.ui_theme_values = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"dark","dark",1818973999),null,new cljs.core.Keyword(null,"light","light",1918998747),null,new cljs.core.Keyword(null,"auto","auto",-566279492),null], null), null);
oc.web.actions.ui_theme.ui_theme_default_value = new cljs.core.Keyword(null,"auto","auto",-566279492);
oc.web.actions.ui_theme.dark_allowed_path_QMARK_ = (function oc$web$actions$ui_theme$dark_allowed_path_QMARK_(){
return cljs.core.not(cljs.core.seq(window.location.pathname.match(window.OCWebUIThemeAllowedPathRegExp)));
});
oc.web.actions.ui_theme.ui_theme_cookie_name = (function oc$web$actions$ui_theme$ui_theme_cookie_name(){
return cljs.core.name(oc.web.actions.ui_theme.ui_theme_cookie_name_suffix);
});
oc.web.actions.ui_theme.save_ui_theme_cookie = (function oc$web$actions$ui_theme$save_ui_theme_cookie(v){
return oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$3(oc.web.actions.ui_theme.ui_theme_cookie_name(),cljs.core.name(v),((((60) * (60)) * (24)) * (365)));
});
oc.web.actions.ui_theme.read_ui_theme_cookie = (function oc$web$actions$ui_theme$read_ui_theme_cookie(){
var cookie_val = oc.web.lib.cookies.get_cookie(oc.web.actions.ui_theme.ui_theme_cookie_name());
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.actions.ui-theme",null,34,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Reading theme from cookie:",cookie_val], null);
}),null)),null,895297568);

if(cljs.core.seq(cookie_val)){
return cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(cookie_val);
} else {
return oc.web.actions.ui_theme.ui_theme_default_value;
}
});
oc.web.actions.ui_theme.set_ui_theme_class = (function oc$web$actions$ui_theme$set_ui_theme_class(mode){
var html_el = document.querySelector("html");
dommy.core.remove_class_BANG_.cljs$core$IFn$_invoke$arity$2(document.querySelector("html"),[cljs.core.name(oc.web.actions.ui_theme.ui_theme_class_name_prefix),"-dark"].join(''));

dommy.core.remove_class_BANG_.cljs$core$IFn$_invoke$arity$2(document.querySelector("html"),[cljs.core.name(oc.web.actions.ui_theme.ui_theme_class_name_prefix),"-light"].join(''));

return dommy.core.add_class_BANG_.cljs$core$IFn$_invoke$arity$2(document.querySelector("html"),[cljs.core.name(oc.web.actions.ui_theme.ui_theme_class_name_prefix),"-",cljs.core.name(mode)].join(''));
});
oc.web.actions.ui_theme.support_system_dark_mode_QMARK_ = (function oc$web$actions$ui_theme$support_system_dark_mode_QMARK_(){
var or__4126__auto__ = ((oc.shared.useragent.desktop_app_QMARK_)?oc.shared.useragent.mac_QMARK_:false);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
if((typeof window !== 'undefined') && (typeof window.matchMedia !== 'undefined')){
var or__4126__auto____$1 = window.matchMedia("(prefers-color-scheme: dark)").matches;
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
return window.matchMedia("(prefers-color-scheme: light)").matches;
}
} else {
return false;
}
}
});
oc.web.actions.ui_theme.system_ui_theme_enabled_QMARK_ = (function oc$web$actions$ui_theme$system_ui_theme_enabled_QMARK_(){
if(cljs.core.truth_((function (){var and__4115__auto__ = oc.shared.useragent.mac_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return ((oc.shared.useragent.desktop_app_QMARK_) && ((typeof OCCarrotDesktop !== 'undefined') && (typeof OCCarrotDesktop.isDarkMode !== 'undefined')));
} else {
return and__4115__auto__;
}
})())){
return OCCarrotDesktop.isDarkMode();
} else {
return window.matchMedia("(prefers-color-scheme: dark)").matches;
}
});
oc.web.actions.ui_theme.computed_value = (function oc$web$actions$ui_theme$computed_value(v){
if(oc.web.actions.ui_theme.dark_allowed_path_QMARK_()){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(v,new cljs.core.Keyword(null,"auto","auto",-566279492))){
if(cljs.core.truth_(oc.web.actions.ui_theme.support_system_dark_mode_QMARK_())){
if(cljs.core.truth_(oc.web.actions.ui_theme.system_ui_theme_enabled_QMARK_())){
return new cljs.core.Keyword(null,"dark","dark",1818973999);
} else {
return new cljs.core.Keyword(null,"light","light",1918998747);
}
} else {
return oc.web.actions.ui_theme.ui_theme_default_value;
}
} else {
return v;
}
} else {
return new cljs.core.Keyword(null,"light","light",1918998747);
}
});
oc.web.actions.ui_theme.get_ui_theme_setting = (function oc$web$actions$ui_theme$get_ui_theme_setting(){
var current_mode = oc.web.actions.ui_theme.read_ui_theme_cookie();
var or__4126__auto__ = current_mode;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.actions.ui_theme.ui_theme_default_value;
}
});
oc.web.actions.ui_theme.set_ui_theme = (function oc$web$actions$ui_theme$set_ui_theme(v){
var fixed_value = (function (){var or__4126__auto__ = v;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"auto","auto",-566279492);
}
})();
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.actions.ui-theme",null,78,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Saving theme:",cljs.core.name(fixed_value),"(",v,")"], null);
}),null)),null,-39031976);

oc.web.actions.ui_theme.save_ui_theme_cookie(fixed_value);

oc.web.actions.ui_theme.set_ui_theme_class(oc.web.actions.ui_theme.computed_value(fixed_value));

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),oc.web.dispatcher.ui_theme_key,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"setting-value","setting-value",-97449119),fixed_value,new cljs.core.Keyword(null,"computed-value","computed-value",1336702311),oc.web.actions.ui_theme.computed_value(fixed_value)], null)], null));
});
goog.exportSymbol('oc.web.actions.ui_theme.set_ui_theme', oc.web.actions.ui_theme.set_ui_theme);
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.actions !== 'undefined') && (typeof oc.web.actions.ui_theme !== 'undefined') && (typeof oc.web.actions.ui_theme.visibility_change_listener !== 'undefined')){
} else {
oc.web.actions.ui_theme.visibility_change_listener = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
}
oc.web.actions.ui_theme.setup_ui_theme = (function oc$web$actions$ui_theme$setup_ui_theme(){
var cur_val = oc.web.actions.ui_theme.get_ui_theme_setting();
var computed_val = oc.web.actions.ui_theme.computed_value(cur_val);
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.actions.ui-theme",null,88,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Theme:",cljs.core.name(cur_val),"->",cljs.core.name(computed_val)], null);
}),null)),null,-1142488950);

oc.web.actions.ui_theme.set_ui_theme_class(computed_val);

cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.app_state,(function (p1__51337_SHARP_){
return cljs.core.assoc_in(p1__51337_SHARP_,oc.web.dispatcher.ui_theme_key,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"setting-value","setting-value",-97449119),cur_val,new cljs.core.Keyword(null,"computed-value","computed-value",1336702311),computed_val], null));
}));

if(cljs.core.truth_(oc.web.actions.ui_theme.support_system_dark_mode_QMARK_())){
(window.matchMedia("(prefers-color-scheme: light)").onchange = (function (){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.actions.ui_theme.get_ui_theme_setting(),new cljs.core.Keyword(null,"auto","auto",-566279492))){
return oc.web.actions.ui_theme.set_ui_theme(new cljs.core.Keyword(null,"auto","auto",-566279492));
} else {
return null;
}
}));

if(cljs.core.truth_(cljs.core.deref(oc.web.actions.ui_theme.visibility_change_listener))){
goog.events.unlistenByKey(cljs.core.deref(oc.web.actions.ui_theme.visibility_change_listener));
} else {
}

return cljs.core.reset_BANG_(oc.web.actions.ui_theme.visibility_change_listener,goog.events.listen(document,goog.events.EventType.VISIBILITYCHANGE,(function (){
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(document.visibilityState,"visible")) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.actions.ui_theme.get_ui_theme_setting(),new cljs.core.Keyword(null,"auto","auto",-566279492))))){
return oc.web.actions.ui_theme.set_ui_theme(new cljs.core.Keyword(null,"auto","auto",-566279492));
} else {
return null;
}
})));
} else {
return null;
}
});
oc.web.actions.ui_theme.setup_ui_theme();

//# sourceMappingURL=oc.web.actions.ui_theme.js.map
