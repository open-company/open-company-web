goog.provide('oc.web.lib.json');
oc.web.lib.json.json__GT_cljs = (function oc$web$lib$json$json__GT_cljs(json_str){
var parsed_json = JSON.parse(json_str,new cljs.core.Keyword(null,"keywordize-keys","keywordize-keys",1310784252),true);
return cljs.core.js__GT_clj.cljs$core$IFn$_invoke$arity$variadic(parsed_json,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"keywordize-keys","keywordize-keys",1310784252),true], 0));
});
oc.web.lib.json.cljs__GT_json = (function oc$web$lib$json$cljs__GT_json(coll){
var stringified_coll = clojure.walk.stringify_keys(coll);
return cljs.core.clj__GT_js(stringified_coll);
});

//# sourceMappingURL=oc.web.lib.json.js.map
