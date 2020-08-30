goog.provide('oc.web.mixins.ui');
oc.web.mixins.ui.refresh_tooltips_mixin = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
$("[data-toggle=\"tooltip\"]",rum.core.dom_node(s)).tooltip();

return s;
}),new cljs.core.Keyword(null,"did-remount","did-remount",1362550500),(function (_,s){
$("[data-toggle=\"tooltip\"]",rum.core.dom_node(s)).each((function (p1__45662_SHARP_,p2__45661_SHARP_){
var G__45663 = $(p2__45661_SHARP_);
G__45663.tooltip("fixTitle");

G__45663.tooltip("hide");

return G__45663;
}));

return s;
})], null);
oc.web.mixins.ui.strict_refresh_tooltips_mixin = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
$("[data-toggle=\"tooltip\"]",rum.core.dom_node(s)).tooltip();

return s;
}),new cljs.core.Keyword(null,"did-update","did-update",-2143702256),(function (s){
$("[data-toggle=\"tooltip\"]",rum.core.dom_node(s)).each((function (p1__45665_SHARP_,p2__45664_SHARP_){
var G__45666 = $(p2__45664_SHARP_);
G__45666.tooltip("fixTitle");

G__45666.tooltip("hide");

return G__45666;
}));

return s;
})], null);
/**
 * Mixin used to check if the body has aleady the no-scroll class, if it does it's a no-op.
 * If it doesn't it remember to remove it once the component is going to unmount.
 */
oc.web.mixins.ui.no_scroll_mixin = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (state){
oc.web.utils.dom.lock_page_scroll();

return state;
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (state){
oc.web.utils.dom.unlock_page_scroll();

return state;
})], null);
/**
 * Adds a flag to the component state with a boolean value.
 * It's true if the component is currently mounted,
 * false if not or is being unmounted.
 */
oc.web.mixins.ui.mounted_flag = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"init","init",-1875481434),(function (p1__45667_SHARP_){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__45667_SHARP_,new cljs.core.Keyword("oc.web.mixins.ui","mounted?","oc.web.mixins.ui/mounted?",1625749500),false);
}),new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (p1__45668_SHARP_){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__45668_SHARP_,new cljs.core.Keyword("oc.web.mixins.ui","mounted?","oc.web.mixins.ui/mounted?",1625749500),true);
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (p1__45669_SHARP_){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__45669_SHARP_,new cljs.core.Keyword("oc.web.mixins.ui","mounted?","oc.web.mixins.ui/mounted?",1625749500),false);
})], null);
/**
 * This mixin will add a :first-render-done atom to your component state. It will
 * be false when the component is not mounted, and true when it is. Very useful for
 * appear or disappear animations or to track down the component state.
 */
oc.web.mixins.ui.first_render_mixin = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"init","init",-1875481434),(function (state){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(state,new cljs.core.Keyword(null,"first-render-done","first-render-done",1105112667),cljs.core.atom.cljs$core$IFn$_invoke$arity$1(false));
}),new cljs.core.Keyword(null,"after-render","after-render",1997533433),(function (state){
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword(null,"first-render-done","first-render-done",1105112667).cljs$core$IFn$_invoke$arity$1(state)))){
} else {
cljs.core.reset_BANG_(new cljs.core.Keyword(null,"first-render-done","first-render-done",1105112667).cljs$core$IFn$_invoke$arity$1(state),true);

rum.core.request_render(new cljs.core.Keyword("rum","react-component","rum/react-component",-1879897248).cljs$core$IFn$_invoke$arity$1(state));
}

return state;
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (state){
cljs.core.reset_BANG_(new cljs.core.Keyword(null,"first-render-done","first-render-done",1105112667).cljs$core$IFn$_invoke$arity$1(state),false);

return state;
})], null);
oc.web.mixins.ui.previous_scrolls = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentVector.EMPTY);
oc.web.mixins.ui.load_more_items_did_scroll = (function oc$web$mixins$ui$load_more_items_did_scroll(s,e){
var load_next_fn = cljs.core.deref(new cljs.core.Keyword(null,"load-more-items-next-fn","load-more-items-next-fn",-1183169184).cljs$core$IFn$_invoke$arity$1(s));
var load_prev_fn = cljs.core.deref(new cljs.core.Keyword(null,"load-more-items-prev-fn","load-more-items-prev-fn",-2069326501).cljs$core$IFn$_invoke$arity$1(s));
if(((cljs.core.fn_QMARK_(load_next_fn)) || (cljs.core.fn_QMARK_(load_prev_fn)))){
var win_height = window.innerHeight;
var total_scroll_height = document.body.scrollHeight;
var scroll_offset = (total_scroll_height - win_height);
var current_scroll = window.scrollY;
var scroll_index = new cljs.core.Keyword(null,"-scroll-index","-scroll-index",-932715443).cljs$core$IFn$_invoke$arity$1(s);
var previous_scroll = cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.mixins.ui.previous_scrolls),scroll_index);
var direction = (((current_scroll > cljs.core.deref(previous_scroll)))?new cljs.core.Keyword(null,"down","down",1565245570):new cljs.core.Keyword(null,"up","up",-269712113));
var scroll_limit = new cljs.core.Keyword(null,"-scroll-offset","-scroll-offset",527083650).cljs$core$IFn$_invoke$arity$1(s);
cljs.core.reset_BANG_(cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.mixins.ui.previous_scrolls),scroll_index),current_scroll);

if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(direction,new cljs.core.Keyword(null,"up","up",-269712113))){
if(((cljs.core.fn_QMARK_(load_next_fn)) && ((current_scroll < scroll_limit)))){
return (load_next_fn.cljs$core$IFn$_invoke$arity$1 ? load_next_fn.cljs$core$IFn$_invoke$arity$1(current_scroll) : load_next_fn.call(null,current_scroll));
} else {
return null;
}
} else {
if(((cljs.core.fn_QMARK_(load_prev_fn)) && ((current_scroll > (scroll_offset - scroll_limit))))){
return (load_prev_fn.cljs$core$IFn$_invoke$arity$1 ? load_prev_fn.cljs$core$IFn$_invoke$arity$1(current_scroll) : load_prev_fn.call(null,current_scroll));
} else {
return null;
}
}
} else {
return null;
}
});
/**
 * Given a scroll offset, listen for the page scroll and when they are set calls the callback functions.
 * The callback functions must be set as atoms at :load-more-items-next-fn and :load-more-items-prev-fn.
 * It's responsible of the component to remove the functions to avoid multiple calls.
 * The functions are called when the scroll direction is right and the give offset is reached.
 * 
 * Save :-scroll-index for the last scroll array atom. Use :-scroll-offset to save the passed offset. Use
 * :-scroll-listener to save the scroll listener.
 */
oc.web.mixins.ui.load_more_items = (function oc$web$mixins$ui$load_more_items(offset){
return new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"init","init",-1875481434),(function (s){
var scroll_index = cljs.core.count(cljs.core.deref(oc.web.mixins.ui.previous_scrolls));
cljs.core.reset_BANG_(oc.web.mixins.ui.previous_scrolls,cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.mixins.ui.previous_scrolls),cljs.core.atom.cljs$core$IFn$_invoke$arity$1((0)))));

return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(s,new cljs.core.Keyword(null,"-scroll-index","-scroll-index",-932715443),scroll_index),new cljs.core.Keyword(null,"-scroll-offset","-scroll-offset",527083650),offset);
}),new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
var scroll_listener = goog.events.listen(window,goog.events.EventType.SCROLL,cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.mixins.ui.load_more_items_did_scroll,s));
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(s,new cljs.core.Keyword(null,"-scroll-listener","-scroll-listener",460234985),scroll_listener);
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
if(cljs.core.truth_(new cljs.core.Keyword(null,"-scroll-listener","-scroll-listener",460234985).cljs$core$IFn$_invoke$arity$1(s))){
goog.events.unlistenByKey(new cljs.core.Keyword(null,"-scroll-listener","-scroll-listener",460234985).cljs$core$IFn$_invoke$arity$1(s));

cljs.core.reset_BANG_(oc.web.mixins.ui.previous_scrolls,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.deref(oc.web.mixins.ui.previous_scrolls),new cljs.core.Keyword(null,"-scroll-index","-scroll-index",-932715443).cljs$core$IFn$_invoke$arity$1(s),null));

return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(s,new cljs.core.Keyword(null,"-scroll-listener","-scroll-listener",460234985));
} else {
return s;
}
})], null);
});
/**
 * Trigger a re-render when the window resizes.
 * 
 *   IMPORTANT: add this mixin at the bottom of your component mixins' list
 *   if you want to use a rum/local in the passed callback.
 * 
 *   Example, note when :my-key is setup and used:
 * 
 *   (defn my-callback [state event]
 *  (reset! (:my-key state) 1))
 * 
 *   (rum/defc component < ;; Mixins: note the order of the following
 *                      (rum/local 0 :my-key)
 *                      (render-on-resize my-callback)
 *  [s]
 *  [:div])
 */
oc.web.mixins.ui.render_on_resize = (function oc$web$mixins$ui$render_on_resize(resize_cb){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(s,new cljs.core.Keyword(null,"render-on-resize-listener","render-on-resize-listener",134732161),goog.events.listen(window,goog.events.EventType.RESIZE,(function (e){
if(cljs.core.fn_QMARK_(resize_cb)){
(resize_cb.cljs$core$IFn$_invoke$arity$2 ? resize_cb.cljs$core$IFn$_invoke$arity$2(s,e) : resize_cb.call(null,s,e));
} else {
}

return rum.core.request_render(new cljs.core.Keyword("rum","react-component","rum/react-component",-1879897248).cljs$core$IFn$_invoke$arity$1(s));
})));
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
var resize_listener = new cljs.core.Keyword(null,"render-on-resize-listener","render-on-resize-listener",134732161).cljs$core$IFn$_invoke$arity$1(s);
if(cljs.core.truth_(resize_listener)){
goog.events.unlistenByKey(new cljs.core.Keyword(null,"render-on-resize-listener","render-on-resize-listener",134732161).cljs$core$IFn$_invoke$arity$1(s));
} else {
}

return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(s,new cljs.core.Keyword(null,"render-on-resize-listener","render-on-resize-listener",134732161));
})], null);
});
/**
 * Give a selector for the items to check under the component root.
 *   On each scroll event checks which items are visible with the given selector
 *   and call the passed callback with the element uuid (got via the data-itemuuid property).
 */
oc.web.mixins.ui.ap_seen_mixin = (function oc$web$mixins$ui$ap_seen_mixin(items_selector,item_is_visible_cb){
var scroll_listener_kw = new cljs.core.Keyword(null,"ap-seen-mixin-scroll-listener","ap-seen-mixin-scroll-listener",-358523535);
var mounted_kw = new cljs.core.Keyword(null,"ap-seen-mixin-is-mounted","ap-seen-mixin-is-mounted",-994569882);
var check_items_fn = (function() { 
var G__45707__delegate = function (s,p__45670){
var vec__45671 = p__45670;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__45671,(0),null);
if(cljs.core.truth_(cljs.core.deref(cljs.core.get.cljs$core$IFn$_invoke$arity$2(s,mounted_kw)))){
var dom_node = rum.core.dom_node(s);
var $all_items = $(items_selector,dom_node);
return $all_items.each((function (idx,el){
if(((cljs.core.fn_QMARK_(item_is_visible_cb)) && (oc.web.utils.activity.is_element_visible_QMARK_(el)))){
var G__45674 = s;
var G__45675 = $(el).attr("data-itemuuid");
return (item_is_visible_cb.cljs$core$IFn$_invoke$arity$2 ? item_is_visible_cb.cljs$core$IFn$_invoke$arity$2(G__45674,G__45675) : item_is_visible_cb.call(null,G__45674,G__45675));
} else {
return null;
}
}));
} else {
return null;
}
};
var G__45707 = function (s,var_args){
var p__45670 = null;
if (arguments.length > 1) {
var G__45709__i = 0, G__45709__a = new Array(arguments.length -  1);
while (G__45709__i < G__45709__a.length) {G__45709__a[G__45709__i] = arguments[G__45709__i + 1]; ++G__45709__i;}
  p__45670 = new cljs.core.IndexedSeq(G__45709__a,0,null);
} 
return G__45707__delegate.call(this,s,p__45670);};
G__45707.cljs$lang$maxFixedArity = 1;
G__45707.cljs$lang$applyTo = (function (arglist__45710){
var s = cljs.core.first(arglist__45710);
var p__45670 = cljs.core.rest(arglist__45710);
return G__45707__delegate(s,p__45670);
});
G__45707.cljs$core$IFn$_invoke$arity$variadic = G__45707__delegate;
return G__45707;
})()
;
return new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"init","init",-1875481434),(function (s){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(s,mounted_kw,cljs.core.atom.cljs$core$IFn$_invoke$arity$1(false));
}),new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(s,scroll_listener_kw,goog.events.listen(window,goog.events.EventType.SCROLL,(function (e){
return oc.web.lib.utils.after((0),(function (){
return check_items_fn(s,e);
}));
})));
}),new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
cljs.core.reset_BANG_(cljs.core.get.cljs$core$IFn$_invoke$arity$2(s,mounted_kw),true);

oc.web.lib.utils.after((500),(function (){
return check_items_fn(s);
}));

return s;
}),new cljs.core.Keyword(null,"did-remount","did-remount",1362550500),(function (_,s){
oc.web.lib.utils.after((1500),(function (){
return check_items_fn(s);
}));

return s;
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
cljs.core.reset_BANG_(cljs.core.get.cljs$core$IFn$_invoke$arity$2(s,mounted_kw),false);

check_items_fn(s);

var scroll_listener = cljs.core.get.cljs$core$IFn$_invoke$arity$2(s,scroll_listener_kw);
var next_state = (cljs.core.truth_(scroll_listener)?(function (){
goog.events.unlistenByKey(scroll_listener);

return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(s,scroll_listener_kw);
})()
:s);
var next_state__$1 = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(next_state,mounted_kw);
return next_state__$1;
})], null);
});
/**
 * Give a selector for the items to check under the component root.
 *   On each scroll event checks which items are:
 *   - not truncated
 *   - the top or the bottom of the element is visible at screen
 *   The stream-item component uses 2 different bodies, one is shown initially and truncated if needed,
 *   the second is shown if it was truncated and expanded by the user.
 */
oc.web.mixins.ui.wrt_stream_item_mixin = (function oc$web$mixins$ui$wrt_stream_item_mixin(items_selector,item_read_cb){
var scroll_listener_kw = new cljs.core.Keyword(null,"wrt-stream-mixin-scroll-listener","wrt-stream-mixin-scroll-listener",-2027002103);
var mounted_kw = new cljs.core.Keyword(null,"wrt-mixin-is-mounted","wrt-mixin-is-mounted",1355452634);
var check_item_fn = (function (s,idx,el){
if(oc.web.utils.activity.is_element_visible_QMARK_(el)){
var G__45676 = s;
var G__45677 = $(el).attr("data-itemuuid");
return (item_read_cb.cljs$core$IFn$_invoke$arity$2 ? item_read_cb.cljs$core$IFn$_invoke$arity$2(G__45676,G__45677) : item_read_cb.call(null,G__45676,G__45677));
} else {
return null;
}
});
var check_items_fn = (function() { 
var G__45711__delegate = function (s,p__45678){
var vec__45679 = p__45678;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__45679,(0),null);
if(cljs.core.truth_(cljs.core.deref(cljs.core.get.cljs$core$IFn$_invoke$arity$2(s,mounted_kw)))){
var dom_node = rum.core.dom_node(s);
var $all_items = $(items_selector,dom_node);
return $all_items.each(cljs.core.partial.cljs$core$IFn$_invoke$arity$2(check_item_fn,s));
} else {
return null;
}
};
var G__45711 = function (s,var_args){
var p__45678 = null;
if (arguments.length > 1) {
var G__45712__i = 0, G__45712__a = new Array(arguments.length -  1);
while (G__45712__i < G__45712__a.length) {G__45712__a[G__45712__i] = arguments[G__45712__i + 1]; ++G__45712__i;}
  p__45678 = new cljs.core.IndexedSeq(G__45712__a,0,null);
} 
return G__45711__delegate.call(this,s,p__45678);};
G__45711.cljs$lang$maxFixedArity = 1;
G__45711.cljs$lang$applyTo = (function (arglist__45713){
var s = cljs.core.first(arglist__45713);
var p__45678 = cljs.core.rest(arglist__45713);
return G__45711__delegate(s,p__45678);
});
G__45711.cljs$core$IFn$_invoke$arity$variadic = G__45711__delegate;
return G__45711;
})()
;
return new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"init","init",-1875481434),(function (s){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(s,mounted_kw,cljs.core.atom.cljs$core$IFn$_invoke$arity$1(false));
}),new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(s,scroll_listener_kw,goog.events.listen(window,goog.events.EventType.SCROLL,oc.web.lib.utils.throttled_debounced_fn(cljs.core.partial.cljs$core$IFn$_invoke$arity$2(check_items_fn,s),(500))));
}),new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
cljs.core.reset_BANG_(cljs.core.get.cljs$core$IFn$_invoke$arity$2(s,mounted_kw),true);

oc.web.lib.utils.after((2500),(function (){
return check_items_fn(s);
}));

return s;
}),new cljs.core.Keyword(null,"did-remount","did-remount",1362550500),(function (_,s){
oc.web.lib.utils.after((2500),(function (){
return check_items_fn(s);
}));

return s;
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
cljs.core.reset_BANG_(cljs.core.get.cljs$core$IFn$_invoke$arity$2(s,mounted_kw),false);

check_items_fn(s);

var next_state = (function (){var temp__5733__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(s,scroll_listener_kw);
if(cljs.core.truth_(temp__5733__auto__)){
var scroll_listener = temp__5733__auto__;
goog.events.unlistenByKey(scroll_listener);

return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(s,scroll_listener_kw);
} else {
return s;
}
})();
var next_state__$1 = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(next_state,mounted_kw);
return next_state__$1;
})], null);
});
oc.web.mixins.ui.on_window_click_mixin = (function oc$web$mixins$ui$on_window_click_mixin(callback){
var click_out_kw = cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(["on-click-out-listenr-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.rand.cljs$core$IFn$_invoke$arity$1((100)))].join(''));
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
var on_click_listener = goog.events.listen(document.getElementById("app"),goog.events.EventType.CLICK,(function (e){
return (callback.cljs$core$IFn$_invoke$arity$2 ? callback.cljs$core$IFn$_invoke$arity$2(s,e) : callback.call(null,s,e));
}));
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(s,click_out_kw,on_click_listener);
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
if(cljs.core.truth_((click_out_kw.cljs$core$IFn$_invoke$arity$1 ? click_out_kw.cljs$core$IFn$_invoke$arity$1(s) : click_out_kw.call(null,s)))){
goog.events.unlistenByKey((click_out_kw.cljs$core$IFn$_invoke$arity$1 ? click_out_kw.cljs$core$IFn$_invoke$arity$1(s) : click_out_kw.call(null,s)));

return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(s,click_out_kw);
} else {
return s;
}
})], null);
});
oc.web.mixins.ui.on_window_resize_mixin = (function oc$web$mixins$ui$on_window_resize_mixin(callback){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
var on_resize_listener = goog.events.listen(document.getElementById("app"),goog.events.EventType.RESIZE,(function (e){
return (callback.cljs$core$IFn$_invoke$arity$2 ? callback.cljs$core$IFn$_invoke$arity$2(s,e) : callback.call(null,s,e));
}));
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(s,new cljs.core.Keyword(null,"on-resize-listener","on-resize-listener",-705670229),on_resize_listener);
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
if(cljs.core.truth_(new cljs.core.Keyword(null,"on-resize-listener","on-resize-listener",-705670229).cljs$core$IFn$_invoke$arity$1(s))){
goog.events.unlistenByKey(new cljs.core.Keyword(null,"on-resize-listener","on-resize-listener",-705670229).cljs$core$IFn$_invoke$arity$1(s));

return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(s,new cljs.core.Keyword(null,"on-resize-listener","on-resize-listener",-705670229));
} else {
return s;
}
})], null);
});
oc.web.mixins.ui.on_window_scroll_mixin = (function oc$web$mixins$ui$on_window_scroll_mixin(callback){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
var on_scroll_listener = goog.events.listen(window,goog.events.EventType.SCROLL,(function (e){
return (callback.cljs$core$IFn$_invoke$arity$2 ? callback.cljs$core$IFn$_invoke$arity$2(s,e) : callback.call(null,s,e));
}));
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(s,new cljs.core.Keyword(null,"on-scroll-listener","on-scroll-listener",-352850197),on_scroll_listener);
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
if(cljs.core.truth_(new cljs.core.Keyword(null,"on-scroll-listener","on-scroll-listener",-352850197).cljs$core$IFn$_invoke$arity$1(s))){
goog.events.unlistenByKey(new cljs.core.Keyword(null,"on-scroll-listener","on-scroll-listener",-352850197).cljs$core$IFn$_invoke$arity$1(s));

return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(s,new cljs.core.Keyword(null,"on-scroll-listener","on-scroll-listener",-352850197));
} else {
return s;
}
})], null);
});
/**
 * Attaches classes and click handlers to `img` tags to allow for expanding full-screen images
 */
oc.web.mixins.ui.make_images_interactive_BANG_ = (function oc$web$mixins$ui$make_images_interactive_BANG_(s,el_selector){
var dom_node = rum.core.dom_node(s);
var imgs = dommy.utils.__GT_Array(dom_node.querySelectorAll(dommy.core.selector([cljs.core.str.cljs$core$IFn$_invoke$arity$1(el_selector)," img"].join(''))));
var seq__45682_45725 = cljs.core.seq(imgs);
var chunk__45685_45726 = null;
var count__45686_45727 = (0);
var i__45687_45728 = (0);
while(true){
if((i__45687_45728 < count__45686_45727)){
var img_45729 = chunk__45685_45726.cljs$core$IIndexed$_nth$arity$2(null,i__45687_45728);
if(cljs.core.not(img_45729.classList.contains("user-avatar-img"))){
var href_45730 = img_45729.src;
dommy.core.add_class_BANG_.cljs$core$IFn$_invoke$arity$2(img_45729,new cljs.core.Keyword(null,"interactive-image","interactive-image",-1660107140));

dommy.core.listen_BANG_.cljs$core$IFn$_invoke$arity$variadic(img_45729,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"click","click",1912301393),((function (seq__45682_45725,chunk__45685_45726,count__45686_45727,i__45687_45728,href_45730,img_45729,dom_node,imgs){
return (function (){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"expand-image-src","expand-image-src",-899766588)], null),href_45730], null));
});})(seq__45682_45725,chunk__45685_45726,count__45686_45727,i__45687_45728,href_45730,img_45729,dom_node,imgs))
], 0));


var G__45731 = seq__45682_45725;
var G__45732 = chunk__45685_45726;
var G__45733 = count__45686_45727;
var G__45734 = (i__45687_45728 + (1));
seq__45682_45725 = G__45731;
chunk__45685_45726 = G__45732;
count__45686_45727 = G__45733;
i__45687_45728 = G__45734;
continue;
} else {
var G__45735 = seq__45682_45725;
var G__45736 = chunk__45685_45726;
var G__45737 = count__45686_45727;
var G__45738 = (i__45687_45728 + (1));
seq__45682_45725 = G__45735;
chunk__45685_45726 = G__45736;
count__45686_45727 = G__45737;
i__45687_45728 = G__45738;
continue;
}
} else {
var temp__5735__auto___45739 = cljs.core.seq(seq__45682_45725);
if(temp__5735__auto___45739){
var seq__45682_45740__$1 = temp__5735__auto___45739;
if(cljs.core.chunked_seq_QMARK_(seq__45682_45740__$1)){
var c__4556__auto___45741 = cljs.core.chunk_first(seq__45682_45740__$1);
var G__45742 = cljs.core.chunk_rest(seq__45682_45740__$1);
var G__45743 = c__4556__auto___45741;
var G__45744 = cljs.core.count(c__4556__auto___45741);
var G__45745 = (0);
seq__45682_45725 = G__45742;
chunk__45685_45726 = G__45743;
count__45686_45727 = G__45744;
i__45687_45728 = G__45745;
continue;
} else {
var img_45746 = cljs.core.first(seq__45682_45740__$1);
if(cljs.core.not(img_45746.classList.contains("user-avatar-img"))){
var href_45747 = img_45746.src;
dommy.core.add_class_BANG_.cljs$core$IFn$_invoke$arity$2(img_45746,new cljs.core.Keyword(null,"interactive-image","interactive-image",-1660107140));

dommy.core.listen_BANG_.cljs$core$IFn$_invoke$arity$variadic(img_45746,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"click","click",1912301393),((function (seq__45682_45725,chunk__45685_45726,count__45686_45727,i__45687_45728,href_45747,img_45746,seq__45682_45740__$1,temp__5735__auto___45739,dom_node,imgs){
return (function (){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"expand-image-src","expand-image-src",-899766588)], null),href_45747], null));
});})(seq__45682_45725,chunk__45685_45726,count__45686_45727,i__45687_45728,href_45747,img_45746,seq__45682_45740__$1,temp__5735__auto___45739,dom_node,imgs))
], 0));


var G__45748 = cljs.core.next(seq__45682_45740__$1);
var G__45749 = null;
var G__45750 = (0);
var G__45751 = (0);
seq__45682_45725 = G__45748;
chunk__45685_45726 = G__45749;
count__45686_45727 = G__45750;
i__45687_45728 = G__45751;
continue;
} else {
var G__45752 = cljs.core.next(seq__45682_45740__$1);
var G__45753 = null;
var G__45754 = (0);
var G__45755 = (0);
seq__45682_45725 = G__45752;
chunk__45685_45726 = G__45753;
count__45686_45727 = G__45754;
i__45687_45728 = G__45755;
continue;
}
}
} else {
}
}
break;
}

return s;
});
oc.web.mixins.ui.interactive_images_mixin = (function oc$web$mixins$ui$interactive_images_mixin(el_sel){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
return oc.web.mixins.ui.make_images_interactive_BANG_(s,el_sel);
}),new cljs.core.Keyword(null,"did-remount","did-remount",1362550500),(function (_,new_state){
return oc.web.mixins.ui.make_images_interactive_BANG_(new_state,el_sel);
})], null);
});
oc.web.mixins.ui.autoresize_textarea = (function oc$web$mixins$ui$autoresize_textarea(ref){
var lst = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
var autoresize = (function oc$web$mixins$ui$autoresize_textarea_$_autoresize(e){
var temp__5735__auto__ = e.target;
if(cljs.core.truth_(temp__5735__auto__)){
var this$ = temp__5735__auto__;
(this$.style.cssText = "height:auto;");

return (this$.style.cssText = ["height:",cljs.core.str.cljs$core$IFn$_invoke$arity$1(this$.scrollHeight),"px"].join(''));
} else {
return null;
}
});
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
cljs.core.reset_BANG_(lst,goog.events.listen(rum.core.ref_node(s,ref),goog.events.EventType.KEYUP,autoresize));

autoresize(({"target": rum.core.ref_node(s,ref)}));

return s;
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
if(cljs.core.truth_(cljs.core.deref(lst))){
goog.events.unlistenByKey(cljs.core.deref(lst));

cljs.core.reset_BANG_(lst,null);
} else {
}

return s;
})], null);
});
/**
 * Mixin used to listen for every click outside of a certain node.
 *   If only the callback is provided it uses the main node of the component,
 *   it uses the ref instead.
 */
oc.web.mixins.ui.on_click_out = (function oc$web$mixins$ui$on_click_out(var_args){
var G__45691 = arguments.length;
switch (G__45691) {
case 1:
return oc.web.mixins.ui.on_click_out.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.mixins.ui.on_click_out.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.mixins.ui.on_click_out.cljs$core$IFn$_invoke$arity$1 = (function (callback){
return oc.web.mixins.ui.on_click_out.cljs$core$IFn$_invoke$arity$2(null,callback);
}));

(oc.web.mixins.ui.on_click_out.cljs$core$IFn$_invoke$arity$2 = (function (optional_ref,callback){
var click_out_kw = cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(["on-click-out-listenr-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.rand.cljs$core$IFn$_invoke$arity$1((100)))].join(''));
return new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(s,new cljs.core.Keyword("oc.web.mixins.ui","click-out-mounted?","oc.web.mixins.ui/click-out-mounted?",1575388204),cljs.core.atom.cljs$core$IFn$_invoke$arity$1(true));
}),new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
var on_click_listener = goog.events.listen(document.getElementById("app"),goog.events.EventType.CLICK,(function (e){
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.mixins.ui","click-out-mounted?","oc.web.mixins.ui/click-out-mounted?",1575388204).cljs$core$IFn$_invoke$arity$1(s)))){
var temp__5735__auto__ = ((cljs.core.fn_QMARK_(optional_ref))?rum.core.ref_node(s,(optional_ref.cljs$core$IFn$_invoke$arity$1 ? optional_ref.cljs$core$IFn$_invoke$arity$1(s) : optional_ref.call(null,s))):(((!((optional_ref == null))))?rum.core.ref_node(s,optional_ref):rum.core.dom_node(s)
));
if(cljs.core.truth_(temp__5735__auto__)){
var node = temp__5735__auto__;
if(((cljs.core.not(oc.web.lib.utils.event_inside_QMARK_(e,node))) && (cljs.core.fn_QMARK_(callback)))){
return (callback.cljs$core$IFn$_invoke$arity$2 ? callback.cljs$core$IFn$_invoke$arity$2(s,e) : callback.call(null,s,e));
} else {
return null;
}
} else {
return null;
}
} else {
return null;
}
}));
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(s,click_out_kw,on_click_listener);
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.mixins.ui","click-out-mounted?","oc.web.mixins.ui/click-out-mounted?",1575388204).cljs$core$IFn$_invoke$arity$1(s),false);

if(cljs.core.truth_((click_out_kw.cljs$core$IFn$_invoke$arity$1 ? click_out_kw.cljs$core$IFn$_invoke$arity$1(s) : click_out_kw.call(null,s)))){
goog.events.unlistenByKey((click_out_kw.cljs$core$IFn$_invoke$arity$1 ? click_out_kw.cljs$core$IFn$_invoke$arity$1(s) : click_out_kw.call(null,s)));

return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(s,click_out_kw);
} else {
return s;
}
})], null);
}));

(oc.web.mixins.ui.on_click_out.cljs$lang$maxFixedArity = 2);


//# sourceMappingURL=oc.web.mixins.ui.js.map
