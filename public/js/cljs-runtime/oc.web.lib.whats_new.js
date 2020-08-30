goog.provide('oc.web.lib.whats_new');
oc.web.lib.whats_new.initialized = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(false);
oc.web.lib.whats_new.latest_timeout = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
oc.web.lib.whats_new.whats_new_selector = ".whats-new";
/**
 * Once the number of new items is available turn on a flag in the app state if it's more than 0.
 */
oc.web.lib.whats_new.check_whats_new_badge = (function oc$web$lib$whats_new$check_whats_new_badge(){
cljs.core.reset_BANG_(oc.web.lib.whats_new.latest_timeout,null);

var sel = [oc.web.lib.whats_new.whats_new_selector," #HW_badge"].join('');
var $el = $(sel);
var parsed_val = ((($el.length === (0)))?null:parseInt($el.text(),(10)));
if(cljs.core.truth_((function (){var or__4126__auto__ = (parsed_val == null);
if(or__4126__auto__){
return or__4126__auto__;
} else {
return isNaN(parsed_val);
}
})())){
return cljs.core.reset_BANG_(oc.web.lib.whats_new.latest_timeout,oc.web.lib.utils.after((1000),oc.web.lib.whats_new.check_whats_new_badge));
} else {
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"show-whats-new-green-dot","show-whats-new-green-dot",-206531957)], null),(parsed_val > (0))], null));
}
});
/**
 * Until it's found look for the selector. When found wait for the headway internal
 * initialization to read the number of new items.
 */
oc.web.lib.whats_new.initialize = (function oc$web$lib$whats_new$initialize(){
cljs.core.reset_BANG_(oc.web.lib.whats_new.latest_timeout,null);

if(((cljs.core.not(cljs.core.deref(oc.web.lib.whats_new.initialized))) && (($(oc.web.lib.whats_new.whats_new_selector).length > (0))))){
cljs.core.reset_BANG_(oc.web.lib.whats_new.initialized,true);

var headway_config = cljs.core.clj__GT_js(new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"selector","selector",762528866),oc.web.lib.whats_new.whats_new_selector,new cljs.core.Keyword(null,"account","account",718006320),"xGYD6J",new cljs.core.Keyword(null,"position","position",-2011731912),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"y","y",-1757859776),"bottom"], null),new cljs.core.Keyword(null,"translations","translations",-1114228673),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"title","title",636505583),"What's New",new cljs.core.Keyword(null,"footer","footer",1606445390),"\uD83D\uDC49 Show me more new stuff"], null)], null));
Headway.init(headway_config);

return cljs.core.reset_BANG_(oc.web.lib.whats_new.latest_timeout,oc.web.lib.utils.after((1000),oc.web.lib.whats_new.check_whats_new_badge));
} else {
return cljs.core.reset_BANG_(oc.web.lib.whats_new.latest_timeout,oc.web.lib.utils.after((1000),(function (){
return (oc.web.lib.whats_new.initialize.cljs$core$IFn$_invoke$arity$1 ? oc.web.lib.whats_new.initialize.cljs$core$IFn$_invoke$arity$1(oc.web.lib.whats_new.whats_new_selector) : oc.web.lib.whats_new.initialize.call(null,oc.web.lib.whats_new.whats_new_selector));
})));
}
});
/**
 * Reset the initializations vars and start looking for the selector.
 */
oc.web.lib.whats_new.init = (function oc$web$lib$whats_new$init(){
cljs.core.reset_BANG_(oc.web.lib.whats_new.initialized,false);

if(cljs.core.truth_(cljs.core.deref(oc.web.lib.whats_new.latest_timeout))){
clearTimeout(cljs.core.deref(oc.web.lib.whats_new.latest_timeout));

cljs.core.reset_BANG_(oc.web.lib.whats_new.latest_timeout,null);
} else {
}

return oc.web.lib.whats_new.initialize();
});
oc.web.lib.whats_new.show = (function oc$web$lib$whats_new$show(){
if(cljs.core.truth_(cljs.core.deref(oc.web.lib.whats_new.initialized))){
Headway.show();

return oc.web.lib.utils.after((1000),oc.web.lib.whats_new.check_whats_new_badge);
} else {
return null;
}
});

//# sourceMappingURL=oc.web.lib.whats_new.js.map
