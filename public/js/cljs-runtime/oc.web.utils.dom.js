goog.provide('oc.web.utils.dom');
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.utils !== 'undefined') && (typeof oc.web.utils.dom !== 'undefined') && (typeof oc.web.utils.dom.onload_recalc_measure_class !== 'undefined')){
} else {
oc.web.utils.dom.onload_recalc_measure_class = "onload-reaclc-measure";
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.utils !== 'undefined') && (typeof oc.web.utils.dom !== 'undefined') && (typeof oc.web.utils.dom._lock_counter !== 'undefined')){
} else {
oc.web.utils.dom._lock_counter = cljs.core.atom.cljs$core$IFn$_invoke$arity$1((0));
}
/**
 * Add no-scroll class to the page body tag to lock the scroll
 */
oc.web.utils.dom.lock_page_scroll = (function oc$web$utils$dom$lock_page_scroll(){
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(oc.web.utils.dom._lock_counter,cljs.core.inc);

return dommy.core.add_class_BANG_.cljs$core$IFn$_invoke$arity$2(document.querySelector("html"),new cljs.core.Keyword(null,"no-scroll","no-scroll",1172731816));
});
/**
 * Remove no-scroll class from the page body tag to unlock the scroll
 */
oc.web.utils.dom.unlock_page_scroll = (function oc$web$utils$dom$unlock_page_scroll(){
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(oc.web.utils.dom._lock_counter,cljs.core.dec);

if((cljs.core.deref(oc.web.utils.dom._lock_counter) > (0))){
return null;
} else {
cljs.core.reset_BANG_(oc.web.utils.dom._lock_counter,(0));

return dommy.core.remove_class_BANG_.cljs$core$IFn$_invoke$arity$2(document.querySelector("html"),new cljs.core.Keyword(null,"no-scroll","no-scroll",1172731816));
}
});
/**
 * Given a DOM element return true if the bottom of it is actually visible in the viewport/
 */
oc.web.utils.dom.is_element_bottom_in_viewport_QMARK_ = (function oc$web$utils$dom$is_element_bottom_in_viewport_QMARK_(var_args){
var args__4742__auto__ = [];
var len__4736__auto___48985 = arguments.length;
var i__4737__auto___48986 = (0);
while(true){
if((i__4737__auto___48986 < len__4736__auto___48985)){
args__4742__auto__.push((arguments[i__4737__auto___48986]));

var G__48987 = (i__4737__auto___48986 + (1));
i__4737__auto___48986 = G__48987;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.utils.dom.is_element_bottom_in_viewport_QMARK_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.utils.dom.is_element_bottom_in_viewport_QMARK_.cljs$core$IFn$_invoke$arity$variadic = (function (el,p__48952){
var vec__48953 = p__48952;
var offset = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__48953,(0),null);
var fixed_offset = (function (){var or__4126__auto__ = offset;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return (0);
}
})();
var rect = el.getBoundingClientRect();
var zero_pos_QMARK_ = (function (p1__48949_SHARP_){
return (((p1__48949_SHARP_ === (0))) || ((p1__48949_SHARP_ > (0))));
});
var doc_element = document.documentElement;
var win_height = (function (){var or__4126__auto__ = doc_element.clientHeight;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return window.innerHeight;
}
})();
return (((((rect.top + rect.height) + fixed_offset) >= oc.web.lib.responsive.navbar_height)) && (((rect.top + rect.height) < win_height)));
}));

(oc.web.utils.dom.is_element_bottom_in_viewport_QMARK_.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.utils.dom.is_element_bottom_in_viewport_QMARK_.cljs$lang$applyTo = (function (seq48950){
var G__48951 = cljs.core.first(seq48950);
var seq48950__$1 = cljs.core.next(seq48950);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__48951,seq48950__$1);
}));

/**
 * Given a DOM element return true if it's actually visible in the viewport.
 */
oc.web.utils.dom.is_element_top_in_viewport_QMARK_ = (function oc$web$utils$dom$is_element_top_in_viewport_QMARK_(var_args){
var args__4742__auto__ = [];
var len__4736__auto___48989 = arguments.length;
var i__4737__auto___48990 = (0);
while(true){
if((i__4737__auto___48990 < len__4736__auto___48989)){
args__4742__auto__.push((arguments[i__4737__auto___48990]));

var G__48991 = (i__4737__auto___48990 + (1));
i__4737__auto___48990 = G__48991;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.utils.dom.is_element_top_in_viewport_QMARK_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.utils.dom.is_element_top_in_viewport_QMARK_.cljs$core$IFn$_invoke$arity$variadic = (function (el,p__48976){
var vec__48977 = p__48976;
var offset = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__48977,(0),null);
var fixed_offset = (function (){var or__4126__auto__ = offset;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return (0);
}
})();
var rect = el.getBoundingClientRect();
var zero_pos_QMARK_ = (function (p1__48965_SHARP_){
return (((p1__48965_SHARP_ === (0))) || ((p1__48965_SHARP_ > (0))));
});
var doc_element = document.documentElement;
var win_height = (function (){var or__4126__auto__ = doc_element.clientHeight;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return window.innerHeight;
}
})();
return ((((rect.top + fixed_offset) >= oc.web.lib.responsive.navbar_height)) && ((rect.top < win_height)));
}));

(oc.web.utils.dom.is_element_top_in_viewport_QMARK_.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.utils.dom.is_element_top_in_viewport_QMARK_.cljs$lang$applyTo = (function (seq48971){
var G__48972 = cljs.core.first(seq48971);
var seq48971__$1 = cljs.core.next(seq48971);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__48972,seq48971__$1);
}));

oc.web.utils.dom.viewport_width = (function oc$web$utils$dom$viewport_width(){
var or__4126__auto__ = document.documentElement.clientWidth;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return window.innerWidth;
}
});
oc.web.utils.dom.viewport_height = (function oc$web$utils$dom$viewport_height(){
var or__4126__auto__ = document.documentElement.clientHeight;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return window.innerHeight;
}
});
oc.web.utils.dom.viewport_size = (function oc$web$utils$dom$viewport_size(){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"width","width",-384071477),oc.web.utils.dom.viewport_width(),new cljs.core.Keyword(null,"height","height",1025178622),oc.web.utils.dom.viewport_height()], null);
});
oc.web.utils.dom.viewport_offset = (function oc$web$utils$dom$viewport_offset(element){
if(cljs.core.truth_(element)){
var rect = element.getBoundingClientRect();
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"x","x",2099068185),rect.left,new cljs.core.Keyword(null,"y","y",-1757859776),rect.top], null);
} else {
return null;
}
});
oc.web.utils.dom.event_inside_QMARK_ = (function oc$web$utils$dom$event_inside_QMARK_(e,el){
var element = e.target;
while(true){
if(cljs.core.truth_(element)){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(element,el)){
return true;
} else {
var G__49002 = element.parentElement;
element = G__49002;
continue;
}
} else {
return false;
}
break;
}
});
oc.web.utils.dom.event_cotainer_has_class = (function oc$web$utils$dom$event_cotainer_has_class(e,class_name){
if(cljs.core.truth_(e)){
var element = e.target;
while(true){
if(cljs.core.truth_(element)){
if(cljs.core.truth_(element.classList.contains(class_name))){
return true;
} else {
var G__49003 = element.parentElement;
element = G__49003;
continue;
}
} else {
return false;
}
break;
}
} else {
return null;
}
});

//# sourceMappingURL=oc.web.utils.dom.js.map
