goog.provide('oc.web.lib.responsive');
oc.web.lib.responsive.big_web_min_width = (768);
oc.web.lib.responsive.tablet_max_width = (980);
oc.web.lib.responsive.navbar_height = (56);
oc.web.lib.responsive.mobile_navbar_height = (54);
oc.web.lib.responsive.ww = (function oc$web$lib$responsive$ww(){
if(cljs.core.truth_((function (){var and__4115__auto__ = document;
if(cljs.core.truth_(and__4115__auto__)){
var and__4115__auto____$1 = document.body;
if(cljs.core.truth_(and__4115__auto____$1)){
return document.body.clientWidth;
} else {
return and__4115__auto____$1;
}
} else {
return and__4115__auto__;
}
})())){
return document.body.clientWidth;
} else {
return null;
}
});
oc.web.lib.responsive._mobile = cljs.core.atom.cljs$core$IFn$_invoke$arity$1((-1));
oc.web.lib.responsive.set_browser_type_BANG_ = (function oc$web$lib$responsive$set_browser_type_BANG_(){
var force_mobile_cookie = oc.web.lib.cookies.get_cookie(new cljs.core.Keyword(null,"force-browser-type","force-browser-type",-630659403));
var is_big_web = (cljs.core.truth_(document.body)?(oc.web.lib.responsive.ww() >= oc.web.lib.responsive.big_web_min_width):true);
var fixed_browser_type = (((force_mobile_cookie == null))?(!(is_big_web)):((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(force_mobile_cookie,"mobile"))?true:false));
return cljs.core.reset_BANG_(oc.web.lib.responsive._mobile,fixed_browser_type);
});
oc.web.lib.responsive.is_mobile_size_QMARK_ = (function oc$web$lib$responsive$is_mobile_size_QMARK_(){

if((cljs.core.deref(oc.web.lib.responsive._mobile) < (0))){
oc.web.lib.responsive.set_browser_type_BANG_();
} else {
}

return cljs.core.deref(oc.web.lib.responsive._mobile);
});
oc.web.lib.responsive.left_navigation_sidebar_minimum_right_margin = (16);
oc.web.lib.responsive.left_navigation_sidebar_width = (160);
oc.web.lib.responsive.dashboard_container_width = (732);
oc.web.lib.responsive.is_tablet_or_mobile_QMARK_ = (function oc$web$lib$responsive$is_tablet_or_mobile_QMARK_(){
if(cljs.core.truth_(window._phantom)){
return false;
} else {
if(cljs.core.not(window.WURFL)){
return (oc.web.lib.responsive.ww() <= oc.web.lib.responsive.tablet_max_width);
} else {
return ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(goog.object.get(WURFL,"form_factor"),"Tablet")) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(goog.object.get(WURFL,"form_factor"),"Smartphone")) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(goog.object.get(WURFL,"form_factor"),"Other Mobile")));
}
}
});
if(cljs.core.truth_(window._phantom)){
} else {
goog.events.listen(window,goog.events.EventType.RESIZE,oc.web.lib.responsive.set_browser_type_BANG_);
}

//# sourceMappingURL=oc.web.lib.responsive.js.map
