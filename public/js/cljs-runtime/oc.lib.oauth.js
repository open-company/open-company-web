goog.provide('oc.lib.oauth');
/**
 * Given a map of data return a string encoded with Base64.
 */
oc.lib.oauth.encode_state_string = (function oc$lib$oauth$encode_state_string(data){
return btoa(cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([data], 0)));
});
/**
 * Given a Base64 encoded string return the decoded data.
 */
oc.lib.oauth.decode_state_string = (function oc$lib$oauth$decode_state_string(state_str){
var state_map = clojure.edn.read_string.cljs$core$IFn$_invoke$arity$1(atob(state_str));
var G__48042 = state_map;
if(cljs.core.truth_(new cljs.core.Keyword(null,"redirect","redirect",-1975673286).cljs$core$IFn$_invoke$arity$1(state_map))){
return cljs.core.update.cljs$core$IFn$_invoke$arity$3(G__48042,new cljs.core.Keyword(null,"redirect","redirect",-1975673286),decodeURIComponent);
} else {
return G__48042;
}
});

//# sourceMappingURL=oc.lib.oauth.js.map
