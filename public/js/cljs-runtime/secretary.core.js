goog.provide('secretary.core');

/**
 * @interface
 */
secretary.core.IRouteMatches = function(){};

var secretary$core$IRouteMatches$route_matches$dyn_32128 = (function (this$,route){
var x__4428__auto__ = (((this$ == null))?null:this$);
var m__4429__auto__ = (secretary.core.route_matches[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$2(this$,route) : m__4429__auto__.call(null,this$,route));
} else {
var m__4426__auto__ = (secretary.core.route_matches["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$2(this$,route) : m__4426__auto__.call(null,this$,route));
} else {
throw cljs.core.missing_protocol("IRouteMatches.route-matches",this$);
}
}
});
secretary.core.route_matches = (function secretary$core$route_matches(this$,route){
if((((!((this$ == null)))) && ((!((this$.secretary$core$IRouteMatches$route_matches$arity$2 == null)))))){
return this$.secretary$core$IRouteMatches$route_matches$arity$2(this$,route);
} else {
return secretary$core$IRouteMatches$route_matches$dyn_32128(this$,route);
}
});


/**
 * @interface
 */
secretary.core.IRouteValue = function(){};

var secretary$core$IRouteValue$route_value$dyn_32142 = (function (this$){
var x__4428__auto__ = (((this$ == null))?null:this$);
var m__4429__auto__ = (secretary.core.route_value[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$1 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__4429__auto__.call(null,this$));
} else {
var m__4426__auto__ = (secretary.core.route_value["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$1 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__4426__auto__.call(null,this$));
} else {
throw cljs.core.missing_protocol("IRouteValue.route-value",this$);
}
}
});
secretary.core.route_value = (function secretary$core$route_value(this$){
if((((!((this$ == null)))) && ((!((this$.secretary$core$IRouteValue$route_value$arity$1 == null)))))){
return this$.secretary$core$IRouteValue$route_value$arity$1(this$);
} else {
return secretary$core$IRouteValue$route_value$dyn_32142(this$);
}
});


/**
 * @interface
 */
secretary.core.IRenderRoute = function(){};

var secretary$core$IRenderRoute$render_route$dyn_32147 = (function() {
var G__32148 = null;
var G__32148__1 = (function (this$){
var x__4428__auto__ = (((this$ == null))?null:this$);
var m__4429__auto__ = (secretary.core.render_route[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$1 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__4429__auto__.call(null,this$));
} else {
var m__4426__auto__ = (secretary.core.render_route["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$1 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__4426__auto__.call(null,this$));
} else {
throw cljs.core.missing_protocol("IRenderRoute.render-route",this$);
}
}
});
var G__32148__2 = (function (this$,params){
var x__4428__auto__ = (((this$ == null))?null:this$);
var m__4429__auto__ = (secretary.core.render_route[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$2(this$,params) : m__4429__auto__.call(null,this$,params));
} else {
var m__4426__auto__ = (secretary.core.render_route["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$2(this$,params) : m__4426__auto__.call(null,this$,params));
} else {
throw cljs.core.missing_protocol("IRenderRoute.render-route",this$);
}
}
});
G__32148 = function(this$,params){
switch(arguments.length){
case 1:
return G__32148__1.call(this,this$);
case 2:
return G__32148__2.call(this,this$,params);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
G__32148.cljs$core$IFn$_invoke$arity$1 = G__32148__1;
G__32148.cljs$core$IFn$_invoke$arity$2 = G__32148__2;
return G__32148;
})()
;
secretary.core.render_route = (function secretary$core$render_route(var_args){
var G__31746 = arguments.length;
switch (G__31746) {
case 1:
return secretary.core.render_route.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return secretary.core.render_route.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(secretary.core.render_route.cljs$core$IFn$_invoke$arity$1 = (function (this$){
if((((!((this$ == null)))) && ((!((this$.secretary$core$IRenderRoute$render_route$arity$1 == null)))))){
return this$.secretary$core$IRenderRoute$render_route$arity$1(this$);
} else {
return secretary$core$IRenderRoute$render_route$dyn_32147(this$);
}
}));

(secretary.core.render_route.cljs$core$IFn$_invoke$arity$2 = (function (this$,params){
if((((!((this$ == null)))) && ((!((this$.secretary$core$IRenderRoute$render_route$arity$2 == null)))))){
return this$.secretary$core$IRenderRoute$render_route$arity$2(this$,params);
} else {
return secretary$core$IRenderRoute$render_route$dyn_32147(this$,params);
}
}));

(secretary.core.render_route.cljs$lang$maxFixedArity = 2);


secretary.core._STAR_config_STAR_ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"prefix","prefix",-265908465),""], null));
/**
 * Gets a value for *config* at path.
 */
secretary.core.get_config = (function secretary$core$get_config(path){
var path__$1 = ((cljs.core.sequential_QMARK_(path))?path:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [path], null));
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(secretary.core._STAR_config_STAR_),path__$1);
});
/**
 * Associates a value val for *config* at path.
 */
secretary.core.set_config_BANG_ = (function secretary$core$set_config_BANG_(path,val){
var path__$1 = ((cljs.core.sequential_QMARK_(path))?path:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [path], null));
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(secretary.core._STAR_config_STAR_,cljs.core.assoc_in,path__$1,val);
});
secretary.core.encode = encodeURIComponent;
if((typeof secretary !== 'undefined') && (typeof secretary.core !== 'undefined') && (typeof secretary.core.encode_pair !== 'undefined')){
} else {
/**
 * Given a key and a value return and encoded key-value pair.
 */
secretary.core.encode_pair = (function (){var method_table__4619__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var prefer_table__4620__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var method_cache__4621__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var cached_hierarchy__4622__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var hierarchy__4623__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$3(cljs.core.PersistentArrayMap.EMPTY,new cljs.core.Keyword(null,"hierarchy","hierarchy",-1053470341),(function (){var fexpr__31809 = cljs.core.get_global_hierarchy;
return (fexpr__31809.cljs$core$IFn$_invoke$arity$0 ? fexpr__31809.cljs$core$IFn$_invoke$arity$0() : fexpr__31809.call(null));
})());
return (new cljs.core.MultiFn(cljs.core.symbol.cljs$core$IFn$_invoke$arity$2("secretary.core","encode-pair"),(function (p__31813){
var vec__31815 = p__31813;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31815,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31815,(1),null);
if(((cljs.core.sequential_QMARK_(v)) || (cljs.core.set_QMARK_(v)))){
return new cljs.core.Keyword("secretary.core","sequential","secretary.core/sequential",-347187207);
} else {
if((function (){var or__4126__auto__ = cljs.core.map_QMARK_(v);
if(or__4126__auto__){
return or__4126__auto__;
} else {
if((!((v == null)))){
if((((v.cljs$lang$protocol_mask$partition0$ & (67108864))) || ((cljs.core.PROTOCOL_SENTINEL === v.cljs$core$IRecord$)))){
return true;
} else {
if((!v.cljs$lang$protocol_mask$partition0$)){
return cljs.core.native_satisfies_QMARK_(cljs.core.IRecord,v);
} else {
return false;
}
}
} else {
return cljs.core.native_satisfies_QMARK_(cljs.core.IRecord,v);
}
}
})()){
return new cljs.core.Keyword("secretary.core","map","secretary.core/map",-31086646);
} else {
return null;
}
}
}),new cljs.core.Keyword(null,"default","default",-1987822328),hierarchy__4623__auto__,method_table__4619__auto__,prefer_table__4620__auto__,method_cache__4621__auto__,cached_hierarchy__4622__auto__));
})();
}
secretary.core.key_index = (function secretary$core$key_index(var_args){
var G__31842 = arguments.length;
switch (G__31842) {
case 1:
return secretary.core.key_index.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return secretary.core.key_index.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(secretary.core.key_index.cljs$core$IFn$_invoke$arity$1 = (function (k){
return [cljs.core.name(k),"[]"].join('');
}));

(secretary.core.key_index.cljs$core$IFn$_invoke$arity$2 = (function (k,index){
return [cljs.core.name(k),"[",cljs.core.str.cljs$core$IFn$_invoke$arity$1(index),"]"].join('');
}));

(secretary.core.key_index.cljs$lang$maxFixedArity = 2);

secretary.core.encode_pair.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("secretary.core","sequential","secretary.core/sequential",-347187207),(function (p__31848){
var vec__31850 = p__31848;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31850,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31850,(1),null);
var encoded = cljs.core.map_indexed.cljs$core$IFn$_invoke$arity$2((function (i,x){
var pair = ((cljs.core.coll_QMARK_(x))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [secretary.core.key_index.cljs$core$IFn$_invoke$arity$2(k,i),x], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [secretary.core.key_index.cljs$core$IFn$_invoke$arity$1(k),x], null));
return secretary.core.encode_pair.cljs$core$IFn$_invoke$arity$1(pair);
}),v);
return clojure.string.join.cljs$core$IFn$_invoke$arity$2("&",encoded);
}));
secretary.core.encode_pair.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("secretary.core","map","secretary.core/map",-31086646),(function (p__31874){
var vec__31877 = p__31874;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31877,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31877,(1),null);
var encoded = cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p__31884){
var vec__31885 = p__31884;
var ik = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31885,(0),null);
var iv = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31885,(1),null);
return secretary.core.encode_pair.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [secretary.core.key_index.cljs$core$IFn$_invoke$arity$2(k,cljs.core.name(ik)),iv], null));
}),v);
return clojure.string.join.cljs$core$IFn$_invoke$arity$2("&",encoded);
}));
secretary.core.encode_pair.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"default","default",-1987822328),(function (p__31899){
var vec__31900 = p__31899;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31900,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31900,(1),null);
return [cljs.core.name(k),"=",cljs.core.str.cljs$core$IFn$_invoke$arity$1(secretary.core.encode(cljs.core.str.cljs$core$IFn$_invoke$arity$1(v)))].join('');
}));
/**
 * Convert a map of query parameters into url encoded string.
 */
secretary.core.encode_query_params = (function secretary$core$encode_query_params(query_params){
return clojure.string.join.cljs$core$IFn$_invoke$arity$2("&",cljs.core.map.cljs$core$IFn$_invoke$arity$2(secretary.core.encode_pair,query_params));
});
/**
 * Like js/encodeURIComponent excepts ignore slashes.
 */
secretary.core.encode_uri = (function secretary$core$encode_uri(uri){
return clojure.string.join.cljs$core$IFn$_invoke$arity$2("/",cljs.core.map.cljs$core$IFn$_invoke$arity$2(secretary.core.encode,clojure.string.split.cljs$core$IFn$_invoke$arity$2(uri,/\//)));
});
secretary.core.decode = decodeURIComponent;
/**
 * Parse a value from a serialized query-string key index. If the
 *   index value is empty 0 is returned, if it's a digit it returns the
 *   js/parseInt value, otherwise it returns the extracted index.
 */
secretary.core.parse_path = (function secretary$core$parse_path(path){
var index_re = /\[([^\]]*)\]*/;
var parts = cljs.core.re_seq(index_re,path);
return cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p__31908){
var vec__31909 = p__31908;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31909,(0),null);
var part = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31909,(1),null);
if(cljs.core.empty_QMARK_(part)){
return (0);
} else {
if(cljs.core.truth_(cljs.core.re_matches(/\d+/,part))){
return parseInt(part);
} else {
return part;

}
}
}),parts);
});
/**
 * Return a key path for a serialized query-string entry.
 * 
 *   Ex.
 * 
 *  (key-parse "foo[][a][][b]")
 *  ;; => ("foo" 0 "a" 0 "b")
 *   
 */
secretary.core.key_parse = (function secretary$core$key_parse(k){
var re = /([^\[\]]+)((?:\[[^\]]*\])*)?/;
var vec__31914 = cljs.core.re_matches(re,k);
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31914,(0),null);
var key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31914,(1),null);
var path = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31914,(2),null);
var parsed_path = (cljs.core.truth_(path)?secretary.core.parse_path(path):null);
return cljs.core.cons(key,parsed_path);
});
/**
 * Like assoc-in but numbers in path create vectors instead of maps.
 * 
 *   Ex.
 * 
 *  (assoc-in-query-params {} ["foo" 0] 1)
 *  ;; => {"foo" [1]}
 * 
 *  (assoc-in-query-params {} ["foo" 0 "a"] 1)
 *  ;; => {"foo" [{"a" 1}]}
 *   
 */
secretary.core.assoc_in_query_params = (function secretary$core$assoc_in_query_params(m,path,v){
var heads = (function (xs){
return cljs.core.map_indexed.cljs$core$IFn$_invoke$arity$2((function (i,_){
return cljs.core.take.cljs$core$IFn$_invoke$arity$2((i + (1)),xs);
}),xs);
});
var hs = heads(path);
var m__$1 = cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (m__$1,h){
if(((typeof cljs.core.last(h) === 'number') && ((!(cljs.core.vector_QMARK_(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(m__$1,cljs.core.butlast(h)))))))){
return cljs.core.assoc_in(m__$1,cljs.core.butlast(h),cljs.core.PersistentVector.EMPTY);
} else {
return m__$1;
}
}),m,hs);
if((cljs.core.last(path) === (0))){
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$4(m__$1,cljs.core.butlast(path),cljs.core.conj,v);
} else {
return cljs.core.assoc_in(m__$1,path,v);
}
});
/**
 * Extract a map of query parameters from a query string.
 */
secretary.core.decode_query_params = (function secretary$core$decode_query_params(query_string){
var parts = clojure.string.split.cljs$core$IFn$_invoke$arity$2(query_string,/&/);
var params = cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (m,part){
var vec__31917 = clojure.string.split.cljs$core$IFn$_invoke$arity$3(part,/=/,(2));
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31917,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31917,(1),null);
return secretary.core.assoc_in_query_params(m,secretary.core.key_parse(secretary.core.decode(k)),secretary.core.decode(v));
}),cljs.core.PersistentArrayMap.EMPTY,parts);
var params__$1 = clojure.walk.keywordize_keys(params);
return params__$1;
});
/**
 * Like re-matches but result is a always vector. If re does not
 *   capture matches then it will return a vector of [m m] as if it had a
 *   single capture. Other wise it maintains consistent behavior with
 *   re-matches. 
 */
secretary.core.re_matches_STAR_ = (function secretary$core$re_matches_STAR_(re,s){
var ms = cljs.core.re_matches(re,s);
if(cljs.core.truth_(ms)){
if(cljs.core.sequential_QMARK_(ms)){
return ms;
} else {
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [ms,ms], null);
}
} else {
return null;
}
});
secretary.core.re_escape_chars = cljs.core.set("\\.*+|?()[]{}$^");
secretary.core.re_escape = (function secretary$core$re_escape(s){
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (s__$1,c){
if(cljs.core.truth_((secretary.core.re_escape_chars.cljs$core$IFn$_invoke$arity$1 ? secretary.core.re_escape_chars.cljs$core$IFn$_invoke$arity$1(c) : secretary.core.re_escape_chars.call(null,c)))){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(s__$1),"\\",cljs.core.str.cljs$core$IFn$_invoke$arity$1(c)].join('');
} else {
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(s__$1),cljs.core.str.cljs$core$IFn$_invoke$arity$1(c)].join('');
}
}),"",s);
});
/**
 * Attempt to lex a single token from s with clauses. Each clause is a
 *   pair of [regexp action] where action is a function. regexp is
 *   expected to begin with ^ and contain a single capture. If the
 *   attempt is successful a vector of [s-without-token (action capture)]
 *   is returned. Otherwise the result is nil.
 */
secretary.core.lex_STAR_ = (function secretary$core$lex_STAR_(s,clauses){
return cljs.core.some((function (p__31920){
var vec__31921 = p__31920;
var re = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31921,(0),null);
var action = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31921,(1),null);
var temp__5735__auto__ = cljs.core.re_find(re,s);
if(cljs.core.truth_(temp__5735__auto__)){
var vec__31924 = temp__5735__auto__;
var m = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31924,(0),null);
var c = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31924,(1),null);
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.subs.cljs$core$IFn$_invoke$arity$2(s,cljs.core.count(m)),(action.cljs$core$IFn$_invoke$arity$1 ? action.cljs$core$IFn$_invoke$arity$1(c) : action.call(null,c))], null);
} else {
return null;
}
}),clauses);
});
/**
 * Return a pair of [regex params]. regex is a compiled regular
 *   expression for matching routes. params is a list of route param
 *   names (:*, :id, etc.). 
 */
secretary.core.lex_route = (function secretary$core$lex_route(s,clauses){
var s__$1 = s;
var pattern = "";
var params = cljs.core.PersistentVector.EMPTY;
while(true){
if(cljs.core.seq(s__$1)){
var vec__31942 = secretary.core.lex_STAR_(s__$1,clauses);
var s__$2 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31942,(0),null);
var vec__31945 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31942,(1),null);
var r = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31945,(0),null);
var p = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31945,(1),null);
var G__32269 = s__$2;
var G__32270 = [pattern,cljs.core.str.cljs$core$IFn$_invoke$arity$1(r)].join('');
var G__32271 = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(params,p);
s__$1 = G__32269;
pattern = G__32270;
params = G__32271;
continue;
} else {
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.re_pattern(["^",pattern,"$"].join('')),cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,params)], null);
}
break;
}
});
/**
 * Given a route return an instance of IRouteMatches.
 */
secretary.core.compile_route = (function secretary$core$compile_route(orig_route){
var clauses = new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [/^\*([^\s.:*\/]*)/,(function (v){
var r = "(.*?)";
var p = ((cljs.core.seq(v))?cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(v):new cljs.core.Keyword(null,"*","*",-1294732318));
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [r,p], null);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [/^\:([^\s.:*\/]+)/,(function (v){
var r = "([^,;?/]+)";
var p = cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(v);
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [r,p], null);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [/^([^:*]+)/,(function (v){
var r = secretary.core.re_escape(v);
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [r], null);
})], null)], null);
var vec__31950 = secretary.core.lex_route(orig_route,clauses);
var re = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31950,(0),null);
var params = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31950,(1),null);
if((typeof secretary !== 'undefined') && (typeof secretary.core !== 'undefined') && (typeof secretary.core.t_secretary$core31953 !== 'undefined')){
} else {

/**
* @constructor
 * @implements {secretary.core.IRouteValue}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
 * @implements {secretary.core.IRouteMatches}
*/
secretary.core.t_secretary$core31953 = (function (orig_route,clauses,vec__31950,re,params,meta31954){
this.orig_route = orig_route;
this.clauses = clauses;
this.vec__31950 = vec__31950;
this.re = re;
this.params = params;
this.meta31954 = meta31954;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(secretary.core.t_secretary$core31953.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_31955,meta31954__$1){
var self__ = this;
var _31955__$1 = this;
return (new secretary.core.t_secretary$core31953(self__.orig_route,self__.clauses,self__.vec__31950,self__.re,self__.params,meta31954__$1));
}));

(secretary.core.t_secretary$core31953.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_31955){
var self__ = this;
var _31955__$1 = this;
return self__.meta31954;
}));

(secretary.core.t_secretary$core31953.prototype.secretary$core$IRouteValue$ = cljs.core.PROTOCOL_SENTINEL);

(secretary.core.t_secretary$core31953.prototype.secretary$core$IRouteValue$route_value$arity$1 = (function (this$){
var self__ = this;
var this$__$1 = this;
return self__.orig_route;
}));

(secretary.core.t_secretary$core31953.prototype.secretary$core$IRouteMatches$ = cljs.core.PROTOCOL_SENTINEL);

(secretary.core.t_secretary$core31953.prototype.secretary$core$IRouteMatches$route_matches$arity$2 = (function (_,route){
var self__ = this;
var ___$1 = this;
var temp__5735__auto__ = secretary.core.re_matches_STAR_(self__.re,route);
if(cljs.core.truth_(temp__5735__auto__)){
var vec__31981 = temp__5735__auto__;
var seq__31982 = cljs.core.seq(vec__31981);
var first__31983 = cljs.core.first(seq__31982);
var seq__31982__$1 = cljs.core.next(seq__31982);
var ___$2 = first__31983;
var ms = seq__31982__$1;
return cljs.core.merge_with.cljs$core$IFn$_invoke$arity$variadic(cljs.core.vector,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.PersistentArrayMap.EMPTY,cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p__31984){
var vec__31985 = p__31984;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31985,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31985,(1),null);
return (new cljs.core.MapEntry(k,v,null));
}),cljs.core.partition.cljs$core$IFn$_invoke$arity$2((2),cljs.core.interleave.cljs$core$IFn$_invoke$arity$2(self__.params,cljs.core.map.cljs$core$IFn$_invoke$arity$2(secretary.core.decode,ms))))], 0));
} else {
return null;
}
}));

(secretary.core.t_secretary$core31953.getBasis = (function (){
return new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"orig-route","orig-route",899103121,null),new cljs.core.Symbol(null,"clauses","clauses",-1199594528,null),new cljs.core.Symbol(null,"vec__31950","vec__31950",-491068568,null),new cljs.core.Symbol(null,"re","re",1869207729,null),new cljs.core.Symbol(null,"params","params",-1943919534,null),new cljs.core.Symbol(null,"meta31954","meta31954",-777383324,null)], null);
}));

(secretary.core.t_secretary$core31953.cljs$lang$type = true);

(secretary.core.t_secretary$core31953.cljs$lang$ctorStr = "secretary.core/t_secretary$core31953");

(secretary.core.t_secretary$core31953.cljs$lang$ctorPrWriter = (function (this__4369__auto__,writer__4370__auto__,opt__4371__auto__){
return cljs.core._write(writer__4370__auto__,"secretary.core/t_secretary$core31953");
}));

/**
 * Positional factory function for secretary.core/t_secretary$core31953.
 */
secretary.core.__GT_t_secretary$core31953 = (function secretary$core$compile_route_$___GT_t_secretary$core31953(orig_route__$1,clauses__$1,vec__31950__$1,re__$1,params__$1,meta31954){
return (new secretary.core.t_secretary$core31953(orig_route__$1,clauses__$1,vec__31950__$1,re__$1,params__$1,meta31954));
});

}

return (new secretary.core.t_secretary$core31953(orig_route,clauses,vec__31950,re,params,cljs.core.PersistentArrayMap.EMPTY));
});
secretary.core.render_route_STAR_ = (function secretary$core$render_route_STAR_(var_args){
var args__4742__auto__ = [];
var len__4736__auto___32298 = arguments.length;
var i__4737__auto___32299 = (0);
while(true){
if((i__4737__auto___32299 < len__4736__auto___32298)){
args__4742__auto__.push((arguments[i__4737__auto___32299]));

var G__32311 = (i__4737__auto___32299 + (1));
i__4737__auto___32299 = G__32311;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return secretary.core.render_route_STAR_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(secretary.core.render_route_STAR_.cljs$core$IFn$_invoke$arity$variadic = (function (obj,args){
if((((!((obj == null))))?((((false) || ((cljs.core.PROTOCOL_SENTINEL === obj.secretary$core$IRenderRoute$))))?true:(((!obj.cljs$lang$protocol_mask$partition$))?cljs.core.native_satisfies_QMARK_(secretary.core.IRenderRoute,obj):false)):cljs.core.native_satisfies_QMARK_(secretary.core.IRenderRoute,obj))){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route,obj,args);
} else {
return null;
}
}));

(secretary.core.render_route_STAR_.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(secretary.core.render_route_STAR_.cljs$lang$applyTo = (function (seq31998){
var G__31999 = cljs.core.first(seq31998);
var seq31998__$1 = cljs.core.next(seq31998);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__31999,seq31998__$1);
}));

secretary.core._STAR_routes_STAR_ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentVector.EMPTY);
secretary.core.add_route_BANG_ = (function secretary$core$add_route_BANG_(obj,action){
var obj__$1 = ((typeof obj === 'string')?secretary.core.compile_route(obj):obj);
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(secretary.core._STAR_routes_STAR_,cljs.core.conj,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [obj__$1,action], null));
});
secretary.core.remove_route_BANG_ = (function secretary$core$remove_route_BANG_(obj){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(secretary.core._STAR_routes_STAR_,(function (rs){
return cljs.core.filterv((function (p__32010){
var vec__32011 = p__32010;
var x = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32011,(0),null);
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32011,(1),null);
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(x,obj);
}),rs);
}));
});
secretary.core.reset_routes_BANG_ = (function secretary$core$reset_routes_BANG_(){
return cljs.core.reset_BANG_(secretary.core._STAR_routes_STAR_,cljs.core.PersistentVector.EMPTY);
});
secretary.core.locate_route = (function secretary$core$locate_route(route){
return cljs.core.some((function (p__32020){
var vec__32021 = p__32020;
var compiled_route = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32021,(0),null);
var action = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32021,(1),null);
var temp__5735__auto__ = secretary.core.route_matches(compiled_route,route);
if(cljs.core.truth_(temp__5735__auto__)){
var params = temp__5735__auto__;
return new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"action","action",-811238024),action,new cljs.core.Keyword(null,"params","params",710516235),params,new cljs.core.Keyword(null,"route","route",329891309),compiled_route], null);
} else {
return null;
}
}),cljs.core.deref(secretary.core._STAR_routes_STAR_));
});
/**
 * Returns original route value as set in defroute when passed a URI path.
 */
secretary.core.locate_route_value = (function secretary$core$locate_route_value(uri){
return secretary.core.route_value(new cljs.core.Keyword(null,"route","route",329891309).cljs$core$IFn$_invoke$arity$1(secretary.core.locate_route(uri)));
});
secretary.core.prefix = (function secretary$core$prefix(){
return cljs.core.str.cljs$core$IFn$_invoke$arity$1(secretary.core.get_config(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"prefix","prefix",-265908465)], null)));
});
secretary.core.uri_without_prefix = (function secretary$core$uri_without_prefix(uri){
return clojure.string.replace(uri,cljs.core.re_pattern(["^",secretary.core.prefix()].join('')),"");
});
/**
 * Ensures that the uri has a leading slash
 */
secretary.core.uri_with_leading_slash = (function secretary$core$uri_with_leading_slash(uri){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2("/",cljs.core.first(uri))){
return uri;
} else {
return ["/",cljs.core.str.cljs$core$IFn$_invoke$arity$1(uri)].join('');
}
});
/**
 * Dispatch an action for a given route if it matches the URI path.
 */
secretary.core.dispatch_BANG_ = (function secretary$core$dispatch_BANG_(uri){
var vec__32034 = clojure.string.split.cljs$core$IFn$_invoke$arity$2(secretary.core.uri_without_prefix(uri),/\?/);
var uri_path = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32034,(0),null);
var query_string = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32034,(1),null);
var uri_path__$1 = secretary.core.uri_with_leading_slash(uri_path);
var query_params = (cljs.core.truth_(query_string)?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"query-params","query-params",900640534),secretary.core.decode_query_params(query_string)], null):null);
var map__32037 = secretary.core.locate_route(uri_path__$1);
var map__32037__$1 = (((((!((map__32037 == null))))?(((((map__32037.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__32037.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__32037):map__32037);
var action = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__32037__$1,new cljs.core.Keyword(null,"action","action",-811238024));
var params = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__32037__$1,new cljs.core.Keyword(null,"params","params",710516235));
var action__$1 = (function (){var or__4126__auto__ = action;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.identity;
}
})();
var params__$1 = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([params,query_params], 0));
return (action__$1.cljs$core$IFn$_invoke$arity$1 ? action__$1.cljs$core$IFn$_invoke$arity$1(params__$1) : action__$1.call(null,params__$1));
});
secretary.core.invalid_params = (function secretary$core$invalid_params(params,validations){
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (m,p__32060){
var vec__32061 = p__32060;
var key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32061,(0),null);
var validation = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32061,(1),null);
var value = cljs.core.get.cljs$core$IFn$_invoke$arity$2(params,key);
if(cljs.core.truth_(cljs.core.re_matches(validation,value))){
return m;
} else {
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(m,key,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [value,validation], null));
}
}),cljs.core.PersistentArrayMap.EMPTY,cljs.core.partition.cljs$core$IFn$_invoke$arity$2((2),validations));
});
secretary.core.params_valid_QMARK_ = (function secretary$core$params_valid_QMARK_(params,validations){
return cljs.core.empty_QMARK_(secretary.core.invalid_params(params,validations));
});
goog.object.set(secretary.core.IRouteMatches,"string",true);

goog.object.set(secretary.core.route_matches,"string",(function (this$,route){
return secretary.core.compile_route(this$).secretary$core$IRouteMatches$route_matches$arity$2(null,route);
}));

(RegExp.prototype.secretary$core$IRouteMatches$ = cljs.core.PROTOCOL_SENTINEL);

(RegExp.prototype.secretary$core$IRouteMatches$route_matches$arity$2 = (function (this$,route){
var this$__$1 = this;
var temp__5735__auto__ = secretary.core.re_matches_STAR_(this$__$1,route);
if(cljs.core.truth_(temp__5735__auto__)){
var vec__32073 = temp__5735__auto__;
var seq__32074 = cljs.core.seq(vec__32073);
var first__32075 = cljs.core.first(seq__32074);
var seq__32074__$1 = cljs.core.next(seq__32074);
var _ = first__32075;
var ms = seq__32074__$1;
return cljs.core.vec(ms);
} else {
return null;
}
}));

(cljs.core.PersistentVector.prototype.secretary$core$IRouteMatches$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.PersistentVector.prototype.secretary$core$IRouteMatches$route_matches$arity$2 = (function (p__32076,route){
var vec__32077 = p__32076;
var seq__32078 = cljs.core.seq(vec__32077);
var first__32079 = cljs.core.first(seq__32078);
var seq__32078__$1 = cljs.core.next(seq__32078);
var route_string = first__32079;
var validations = seq__32078__$1;
var vec__32081 = this;
var seq__32082 = cljs.core.seq(vec__32081);
var first__32083 = cljs.core.first(seq__32082);
var seq__32082__$1 = cljs.core.next(seq__32082);
var route_string__$1 = first__32083;
var validations__$1 = seq__32082__$1;
var params = secretary.core.compile_route(route_string__$1).secretary$core$IRouteMatches$route_matches$arity$2(null,route);
if(secretary.core.params_valid_QMARK_(params,validations__$1)){
return params;
} else {
return null;
}
}));
goog.object.set(secretary.core.IRouteValue,"string",true);

goog.object.set(secretary.core.route_value,"string",(function (this$){
return secretary.core.compile_route(this$).secretary$core$IRouteValue$route_value$arity$1(null);
}));

(RegExp.prototype.secretary$core$IRouteValue$ = cljs.core.PROTOCOL_SENTINEL);

(RegExp.prototype.secretary$core$IRouteValue$route_value$arity$1 = (function (this$){
var this$__$1 = this;
return this$__$1;
}));

(cljs.core.PersistentVector.prototype.secretary$core$IRouteValue$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.PersistentVector.prototype.secretary$core$IRouteValue$route_value$arity$1 = (function (p__32092){
var vec__32093 = p__32092;
var seq__32094 = cljs.core.seq(vec__32093);
var first__32095 = cljs.core.first(seq__32094);
var seq__32094__$1 = cljs.core.next(seq__32094);
var route_string = first__32095;
var validations = seq__32094__$1;
var vec__32096 = this;
var seq__32097 = cljs.core.seq(vec__32096);
var first__32098 = cljs.core.first(seq__32097);
var seq__32097__$1 = cljs.core.next(seq__32097);
var route_string__$1 = first__32098;
var validations__$1 = seq__32097__$1;
return cljs.core.vec(cljs.core.cons(secretary.core.route_value(route_string__$1),validations__$1));
}));
goog.object.set(secretary.core.IRenderRoute,"string",true);

goog.object.set(secretary.core.render_route,"string",(function() {
var G__32366 = null;
var G__32366__1 = (function (this$){
return secretary.core.render_route(this$,cljs.core.PersistentArrayMap.EMPTY);
});
var G__32366__2 = (function (this$,params){
var map__32099 = params;
var map__32099__$1 = (((((!((map__32099 == null))))?(((((map__32099.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__32099.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__32099):map__32099);
var m = map__32099__$1;
var query_params = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__32099__$1,new cljs.core.Keyword(null,"query-params","query-params",900640534));
var a = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(m);
var path = this$.replace((new RegExp(":[^\\s.:*/]+|\\*[^\\s.:*/]*","g")),(function ($1){
var lookup = cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2($1,"*"))?$1:cljs.core.subs.cljs$core$IFn$_invoke$arity$2($1,(1))));
var v = cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(a),lookup);
var replacement = ((cljs.core.sequential_QMARK_(v))?(function (){
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(a,cljs.core.assoc,lookup,cljs.core.next(v));

return secretary.core.encode_uri(cljs.core.first(v));
})()
:(cljs.core.truth_(v)?secretary.core.encode_uri(v):$1));
return replacement;
}));
var path__$1 = [cljs.core.str.cljs$core$IFn$_invoke$arity$1(secretary.core.get_config(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"prefix","prefix",-265908465)], null))),cljs.core.str.cljs$core$IFn$_invoke$arity$1(path)].join('');
var temp__5733__auto__ = (function (){var and__4115__auto__ = query_params;
if(cljs.core.truth_(and__4115__auto__)){
return secretary.core.encode_query_params(query_params);
} else {
return and__4115__auto__;
}
})();
if(cljs.core.truth_(temp__5733__auto__)){
var query_string = temp__5733__auto__;
return [path__$1,"?",cljs.core.str.cljs$core$IFn$_invoke$arity$1(query_string)].join('');
} else {
return path__$1;
}
});
G__32366 = function(this$,params){
switch(arguments.length){
case 1:
return G__32366__1.call(this,this$);
case 2:
return G__32366__2.call(this,this$,params);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
G__32366.cljs$core$IFn$_invoke$arity$1 = G__32366__1;
G__32366.cljs$core$IFn$_invoke$arity$2 = G__32366__2;
return G__32366;
})()
);

(cljs.core.PersistentVector.prototype.secretary$core$IRenderRoute$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.PersistentVector.prototype.secretary$core$IRenderRoute$render_route$arity$1 = (function (this$){
var this$__$1 = this;
return secretary.core.render_route(this$__$1,cljs.core.PersistentArrayMap.EMPTY);
}));

(cljs.core.PersistentVector.prototype.secretary$core$IRenderRoute$render_route$arity$2 = (function (p__32113,params){
var vec__32114 = p__32113;
var seq__32115 = cljs.core.seq(vec__32114);
var first__32116 = cljs.core.first(seq__32115);
var seq__32115__$1 = cljs.core.next(seq__32115);
var route_string = first__32116;
var validations = seq__32115__$1;
var vec__32117 = this;
var seq__32118 = cljs.core.seq(vec__32117);
var first__32119 = cljs.core.first(seq__32118);
var seq__32118__$1 = cljs.core.next(seq__32118);
var route_string__$1 = first__32119;
var validations__$1 = seq__32118__$1;
var invalid = secretary.core.invalid_params(params,validations__$1);
if(cljs.core.empty_QMARK_(invalid)){
return secretary.core.render_route(route_string__$1,params);
} else {
throw cljs.core.ex_info.cljs$core$IFn$_invoke$arity$2("Could not build route: invalid params",invalid);
}
}));

//# sourceMappingURL=secretary.core.js.map
