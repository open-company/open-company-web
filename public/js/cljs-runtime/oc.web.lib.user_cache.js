goog.provide('oc.web.lib.user_cache');
var module$node_modules$localforage$dist$localforage=shadow.js.require("module$node_modules$localforage$dist$localforage", {});
oc.web.lib.user_cache.get_key = (function oc$web$lib$user_cache$get_key(data_key){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1((function (){var or__4126__auto__ = oc.web.lib.jwt.user_id();
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "anonymous";
}
})()),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(data_key)].join('');
});
/**
 * Add an item to the localforage for later use.
 */
oc.web.lib.user_cache.set_item = (function oc$web$lib$user_cache$set_item(var_args){
var args__4742__auto__ = [];
var len__4736__auto___41859 = arguments.length;
var i__4737__auto___41860 = (0);
while(true){
if((i__4737__auto___41860 < len__4736__auto___41859)){
args__4742__auto__.push((arguments[i__4737__auto___41860]));

var G__41861 = (i__4737__auto___41860 + (1));
i__4737__auto___41860 = G__41861;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((2) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((2)),(0),null)):null);
return oc.web.lib.user_cache.set_item.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__4743__auto__);
});

(oc.web.lib.user_cache.set_item.cljs$core$IFn$_invoke$arity$variadic = (function (data_key,data_value,p__41843){
var vec__41844 = p__41843;
var completed_cb = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41844,(0),null);
var fixed_key = oc.web.lib.user_cache.get_key(data_key);
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.lib.user-cache",null,14,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["set-item for",fixed_key], null);
}),null)),null,-1385853670);

return module$node_modules$localforage$dist$localforage.setItem(fixed_key,cljs.core.clj__GT_js(clojure.walk.stringify_keys(data_value)),(function (err){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.lib.user-cache",null,19,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["   - set-item error",err], null);
}),null)),null,467412092);

if(cljs.core.fn_QMARK_(completed_cb)){
return (completed_cb.cljs$core$IFn$_invoke$arity$1 ? completed_cb.cljs$core$IFn$_invoke$arity$1(err) : completed_cb.call(null,err));
} else {
return null;
}
}));
}));

(oc.web.lib.user_cache.set_item.cljs$lang$maxFixedArity = (2));

/** @this {Function} */
(oc.web.lib.user_cache.set_item.cljs$lang$applyTo = (function (seq41840){
var G__41841 = cljs.core.first(seq41840);
var seq41840__$1 = cljs.core.next(seq41840);
var G__41842 = cljs.core.first(seq41840__$1);
var seq41840__$2 = cljs.core.next(seq41840__$1);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__41841,G__41842,seq41840__$2);
}));

/**
 * Remove an item from the localforage.
 */
oc.web.lib.user_cache.remove_item = (function oc$web$lib$user_cache$remove_item(data_key){
var fixed_key = oc.web.lib.user_cache.get_key(data_key);
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.lib.user-cache",null,27,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["remove-item for",fixed_key], null);
}),null)),null,-2075347122);

return module$node_modules$localforage$dist$localforage.removeItem(fixed_key);
});
/**
 * Get an item from the localforage and return it.
 */
oc.web.lib.user_cache.get_item = (function oc$web$lib$user_cache$get_item(data_key,get_item_cb){
var fixed_key = oc.web.lib.user_cache.get_key(data_key);
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.lib.user-cache",null,34,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["get-item for",fixed_key], null);
}),null)),null,-1633289233);

return module$node_modules$localforage$dist$localforage.getItem(fixed_key,(function (err,value){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.lib.user-cache",null,38,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["   - get-item for",fixed_key,value,err], null);
}),null)),null,-143878878);

if(cljs.core.fn_QMARK_(get_item_cb)){
var clj_value = (cljs.core.truth_(value)?clojure.walk.keywordize_keys(cljs.core.js__GT_clj.cljs$core$IFn$_invoke$arity$1(value)):value);
return (get_item_cb.cljs$core$IFn$_invoke$arity$2 ? get_item_cb.cljs$core$IFn$_invoke$arity$2(clj_value,err) : get_item_cb.call(null,clj_value,err));
} else {
return null;
}
}));
});

//# sourceMappingURL=oc.web.lib.user_cache.js.map
