goog.provide('shadow.cljs.devtools.client.browser');
shadow.cljs.devtools.client.browser.devtools_msg = (function shadow$cljs$devtools$client$browser$devtools_msg(var_args){
var args__4742__auto__ = [];
var len__4736__auto___36273 = arguments.length;
var i__4737__auto___36274 = (0);
while(true){
if((i__4737__auto___36274 < len__4736__auto___36273)){
args__4742__auto__.push((arguments[i__4737__auto___36274]));

var G__36275 = (i__4737__auto___36274 + (1));
i__4737__auto___36274 = G__36275;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return shadow.cljs.devtools.client.browser.devtools_msg.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(shadow.cljs.devtools.client.browser.devtools_msg.cljs$core$IFn$_invoke$arity$variadic = (function (msg,args){
if(cljs.core.seq(shadow.cljs.devtools.client.env.log_style)){
return console.log.apply(console,cljs.core.into_array.cljs$core$IFn$_invoke$arity$1(cljs.core.into.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [["%cshadow-cljs: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(msg)].join(''),shadow.cljs.devtools.client.env.log_style], null),args)));
} else {
return console.log.apply(console,cljs.core.into_array.cljs$core$IFn$_invoke$arity$1(cljs.core.into.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [["shadow-cljs: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(msg)].join('')], null),args)));
}
}));

(shadow.cljs.devtools.client.browser.devtools_msg.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(shadow.cljs.devtools.client.browser.devtools_msg.cljs$lang$applyTo = (function (seq36125){
var G__36126 = cljs.core.first(seq36125);
var seq36125__$1 = cljs.core.next(seq36125);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__36126,seq36125__$1);
}));

shadow.cljs.devtools.client.browser.script_eval = (function shadow$cljs$devtools$client$browser$script_eval(code){
return goog.globalEval(code);
});
shadow.cljs.devtools.client.browser.do_js_load = (function shadow$cljs$devtools$client$browser$do_js_load(sources){
var seq__36128 = cljs.core.seq(sources);
var chunk__36129 = null;
var count__36130 = (0);
var i__36131 = (0);
while(true){
if((i__36131 < count__36130)){
var map__36138 = chunk__36129.cljs$core$IIndexed$_nth$arity$2(null,i__36131);
var map__36138__$1 = (((((!((map__36138 == null))))?(((((map__36138.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__36138.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__36138):map__36138);
var src = map__36138__$1;
var resource_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36138__$1,new cljs.core.Keyword(null,"resource-id","resource-id",-1308422582));
var output_name = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36138__$1,new cljs.core.Keyword(null,"output-name","output-name",-1769107767));
var resource_name = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36138__$1,new cljs.core.Keyword(null,"resource-name","resource-name",2001617100));
var js = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36138__$1,new cljs.core.Keyword(null,"js","js",1768080579));
$CLJS.SHADOW_ENV.setLoaded(output_name);

shadow.cljs.devtools.client.browser.devtools_msg.cljs$core$IFn$_invoke$arity$variadic("load JS",cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([resource_name], 0));

shadow.cljs.devtools.client.env.before_load_src(src);

try{shadow.cljs.devtools.client.browser.script_eval([cljs.core.str.cljs$core$IFn$_invoke$arity$1(js),"\n//# sourceURL=",cljs.core.str.cljs$core$IFn$_invoke$arity$1($CLJS.SHADOW_ENV.scriptBase),cljs.core.str.cljs$core$IFn$_invoke$arity$1(output_name)].join(''));
}catch (e36140){var e_36276 = e36140;
console.error(["Failed to load ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(resource_name)].join(''),e_36276);

throw (new Error(["Failed to load ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(resource_name),": ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(e_36276.message)].join('')));
}

var G__36277 = seq__36128;
var G__36278 = chunk__36129;
var G__36279 = count__36130;
var G__36280 = (i__36131 + (1));
seq__36128 = G__36277;
chunk__36129 = G__36278;
count__36130 = G__36279;
i__36131 = G__36280;
continue;
} else {
var temp__5735__auto__ = cljs.core.seq(seq__36128);
if(temp__5735__auto__){
var seq__36128__$1 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__36128__$1)){
var c__4556__auto__ = cljs.core.chunk_first(seq__36128__$1);
var G__36281 = cljs.core.chunk_rest(seq__36128__$1);
var G__36282 = c__4556__auto__;
var G__36283 = cljs.core.count(c__4556__auto__);
var G__36284 = (0);
seq__36128 = G__36281;
chunk__36129 = G__36282;
count__36130 = G__36283;
i__36131 = G__36284;
continue;
} else {
var map__36141 = cljs.core.first(seq__36128__$1);
var map__36141__$1 = (((((!((map__36141 == null))))?(((((map__36141.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__36141.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__36141):map__36141);
var src = map__36141__$1;
var resource_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36141__$1,new cljs.core.Keyword(null,"resource-id","resource-id",-1308422582));
var output_name = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36141__$1,new cljs.core.Keyword(null,"output-name","output-name",-1769107767));
var resource_name = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36141__$1,new cljs.core.Keyword(null,"resource-name","resource-name",2001617100));
var js = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36141__$1,new cljs.core.Keyword(null,"js","js",1768080579));
$CLJS.SHADOW_ENV.setLoaded(output_name);

shadow.cljs.devtools.client.browser.devtools_msg.cljs$core$IFn$_invoke$arity$variadic("load JS",cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([resource_name], 0));

shadow.cljs.devtools.client.env.before_load_src(src);

try{shadow.cljs.devtools.client.browser.script_eval([cljs.core.str.cljs$core$IFn$_invoke$arity$1(js),"\n//# sourceURL=",cljs.core.str.cljs$core$IFn$_invoke$arity$1($CLJS.SHADOW_ENV.scriptBase),cljs.core.str.cljs$core$IFn$_invoke$arity$1(output_name)].join(''));
}catch (e36143){var e_36285 = e36143;
console.error(["Failed to load ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(resource_name)].join(''),e_36285);

throw (new Error(["Failed to load ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(resource_name),": ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(e_36285.message)].join('')));
}

var G__36286 = cljs.core.next(seq__36128__$1);
var G__36287 = null;
var G__36288 = (0);
var G__36289 = (0);
seq__36128 = G__36286;
chunk__36129 = G__36287;
count__36130 = G__36288;
i__36131 = G__36289;
continue;
}
} else {
return null;
}
}
break;
}
});
shadow.cljs.devtools.client.browser.do_js_reload = (function shadow$cljs$devtools$client$browser$do_js_reload(msg,sources,complete_fn,failure_fn){
return shadow.cljs.devtools.client.env.do_js_reload.cljs$core$IFn$_invoke$arity$4(cljs.core.assoc.cljs$core$IFn$_invoke$arity$variadic(msg,new cljs.core.Keyword(null,"log-missing-fn","log-missing-fn",732676765),(function (fn_sym){
return null;
}),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"log-call-async","log-call-async",183826192),(function (fn_sym){
return shadow.cljs.devtools.client.browser.devtools_msg(["call async ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(fn_sym)].join(''));
}),new cljs.core.Keyword(null,"log-call","log-call",412404391),(function (fn_sym){
return shadow.cljs.devtools.client.browser.devtools_msg(["call ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(fn_sym)].join(''));
})], 0)),(function (){
return shadow.cljs.devtools.client.browser.do_js_load(sources);
}),complete_fn,failure_fn);
});
/**
 * when (require '["some-str" :as x]) is done at the REPL we need to manually call the shadow.js.require for it
 * since the file only adds the shadow$provide. only need to do this for shadow-js.
 */
shadow.cljs.devtools.client.browser.do_js_requires = (function shadow$cljs$devtools$client$browser$do_js_requires(js_requires){
var seq__36144 = cljs.core.seq(js_requires);
var chunk__36145 = null;
var count__36146 = (0);
var i__36147 = (0);
while(true){
if((i__36147 < count__36146)){
var js_ns = chunk__36145.cljs$core$IIndexed$_nth$arity$2(null,i__36147);
var require_str_36290 = ["var ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(js_ns)," = shadow.js.require(\"",cljs.core.str.cljs$core$IFn$_invoke$arity$1(js_ns),"\");"].join('');
shadow.cljs.devtools.client.browser.script_eval(require_str_36290);


var G__36291 = seq__36144;
var G__36292 = chunk__36145;
var G__36293 = count__36146;
var G__36294 = (i__36147 + (1));
seq__36144 = G__36291;
chunk__36145 = G__36292;
count__36146 = G__36293;
i__36147 = G__36294;
continue;
} else {
var temp__5735__auto__ = cljs.core.seq(seq__36144);
if(temp__5735__auto__){
var seq__36144__$1 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__36144__$1)){
var c__4556__auto__ = cljs.core.chunk_first(seq__36144__$1);
var G__36295 = cljs.core.chunk_rest(seq__36144__$1);
var G__36296 = c__4556__auto__;
var G__36297 = cljs.core.count(c__4556__auto__);
var G__36298 = (0);
seq__36144 = G__36295;
chunk__36145 = G__36296;
count__36146 = G__36297;
i__36147 = G__36298;
continue;
} else {
var js_ns = cljs.core.first(seq__36144__$1);
var require_str_36299 = ["var ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(js_ns)," = shadow.js.require(\"",cljs.core.str.cljs$core$IFn$_invoke$arity$1(js_ns),"\");"].join('');
shadow.cljs.devtools.client.browser.script_eval(require_str_36299);


var G__36300 = cljs.core.next(seq__36144__$1);
var G__36301 = null;
var G__36302 = (0);
var G__36303 = (0);
seq__36144 = G__36300;
chunk__36145 = G__36301;
count__36146 = G__36302;
i__36147 = G__36303;
continue;
}
} else {
return null;
}
}
break;
}
});
shadow.cljs.devtools.client.browser.handle_build_complete = (function shadow$cljs$devtools$client$browser$handle_build_complete(runtime,p__36157){
var map__36158 = p__36157;
var map__36158__$1 = (((((!((map__36158 == null))))?(((((map__36158.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__36158.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__36158):map__36158);
var msg = map__36158__$1;
var info = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36158__$1,new cljs.core.Keyword(null,"info","info",-317069002));
var reload_info = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36158__$1,new cljs.core.Keyword(null,"reload-info","reload-info",1648088086));
var warnings = cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentVector.EMPTY,cljs.core.distinct.cljs$core$IFn$_invoke$arity$1((function (){var iter__4529__auto__ = (function shadow$cljs$devtools$client$browser$handle_build_complete_$_iter__36160(s__36161){
return (new cljs.core.LazySeq(null,(function (){
var s__36161__$1 = s__36161;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__36161__$1);
if(temp__5735__auto__){
var xs__6292__auto__ = temp__5735__auto__;
var map__36166 = cljs.core.first(xs__6292__auto__);
var map__36166__$1 = (((((!((map__36166 == null))))?(((((map__36166.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__36166.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__36166):map__36166);
var src = map__36166__$1;
var resource_name = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36166__$1,new cljs.core.Keyword(null,"resource-name","resource-name",2001617100));
var warnings = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36166__$1,new cljs.core.Keyword(null,"warnings","warnings",-735437651));
if(cljs.core.not(new cljs.core.Keyword(null,"from-jar","from-jar",1050932827).cljs$core$IFn$_invoke$arity$1(src))){
var iterys__4525__auto__ = ((function (s__36161__$1,map__36166,map__36166__$1,src,resource_name,warnings,xs__6292__auto__,temp__5735__auto__,map__36158,map__36158__$1,msg,info,reload_info){
return (function shadow$cljs$devtools$client$browser$handle_build_complete_$_iter__36160_$_iter__36162(s__36163){
return (new cljs.core.LazySeq(null,((function (s__36161__$1,map__36166,map__36166__$1,src,resource_name,warnings,xs__6292__auto__,temp__5735__auto__,map__36158,map__36158__$1,msg,info,reload_info){
return (function (){
var s__36163__$1 = s__36163;
while(true){
var temp__5735__auto____$1 = cljs.core.seq(s__36163__$1);
if(temp__5735__auto____$1){
var s__36163__$2 = temp__5735__auto____$1;
if(cljs.core.chunked_seq_QMARK_(s__36163__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__36163__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__36165 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__36164 = (0);
while(true){
if((i__36164 < size__4528__auto__)){
var warning = cljs.core._nth(c__4527__auto__,i__36164);
cljs.core.chunk_append(b__36165,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(warning,new cljs.core.Keyword(null,"resource-name","resource-name",2001617100),resource_name));

var G__36304 = (i__36164 + (1));
i__36164 = G__36304;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__36165),shadow$cljs$devtools$client$browser$handle_build_complete_$_iter__36160_$_iter__36162(cljs.core.chunk_rest(s__36163__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__36165),null);
}
} else {
var warning = cljs.core.first(s__36163__$2);
return cljs.core.cons(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(warning,new cljs.core.Keyword(null,"resource-name","resource-name",2001617100),resource_name),shadow$cljs$devtools$client$browser$handle_build_complete_$_iter__36160_$_iter__36162(cljs.core.rest(s__36163__$2)));
}
} else {
return null;
}
break;
}
});})(s__36161__$1,map__36166,map__36166__$1,src,resource_name,warnings,xs__6292__auto__,temp__5735__auto__,map__36158,map__36158__$1,msg,info,reload_info))
,null,null));
});})(s__36161__$1,map__36166,map__36166__$1,src,resource_name,warnings,xs__6292__auto__,temp__5735__auto__,map__36158,map__36158__$1,msg,info,reload_info))
;
var fs__4526__auto__ = cljs.core.seq(iterys__4525__auto__(warnings));
if(fs__4526__auto__){
return cljs.core.concat.cljs$core$IFn$_invoke$arity$2(fs__4526__auto__,shadow$cljs$devtools$client$browser$handle_build_complete_$_iter__36160(cljs.core.rest(s__36161__$1)));
} else {
var G__36305 = cljs.core.rest(s__36161__$1);
s__36161__$1 = G__36305;
continue;
}
} else {
var G__36306 = cljs.core.rest(s__36161__$1);
s__36161__$1 = G__36306;
continue;
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(new cljs.core.Keyword(null,"sources","sources",-321166424).cljs$core$IFn$_invoke$arity$1(info));
})()));
var seq__36169_36307 = cljs.core.seq(warnings);
var chunk__36170_36308 = null;
var count__36171_36309 = (0);
var i__36172_36310 = (0);
while(true){
if((i__36172_36310 < count__36171_36309)){
var map__36177_36311 = chunk__36170_36308.cljs$core$IIndexed$_nth$arity$2(null,i__36172_36310);
var map__36177_36312__$1 = (((((!((map__36177_36311 == null))))?(((((map__36177_36311.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__36177_36311.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__36177_36311):map__36177_36311);
var w_36313 = map__36177_36312__$1;
var msg_36314__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36177_36312__$1,new cljs.core.Keyword(null,"msg","msg",-1386103444));
var line_36315 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36177_36312__$1,new cljs.core.Keyword(null,"line","line",212345235));
var column_36316 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36177_36312__$1,new cljs.core.Keyword(null,"column","column",2078222095));
var resource_name_36317 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36177_36312__$1,new cljs.core.Keyword(null,"resource-name","resource-name",2001617100));
console.warn(["BUILD-WARNING in ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(resource_name_36317)," at [",cljs.core.str.cljs$core$IFn$_invoke$arity$1(line_36315),":",cljs.core.str.cljs$core$IFn$_invoke$arity$1(column_36316),"]\n\t",cljs.core.str.cljs$core$IFn$_invoke$arity$1(msg_36314__$1)].join(''));


var G__36318 = seq__36169_36307;
var G__36319 = chunk__36170_36308;
var G__36320 = count__36171_36309;
var G__36321 = (i__36172_36310 + (1));
seq__36169_36307 = G__36318;
chunk__36170_36308 = G__36319;
count__36171_36309 = G__36320;
i__36172_36310 = G__36321;
continue;
} else {
var temp__5735__auto___36322 = cljs.core.seq(seq__36169_36307);
if(temp__5735__auto___36322){
var seq__36169_36323__$1 = temp__5735__auto___36322;
if(cljs.core.chunked_seq_QMARK_(seq__36169_36323__$1)){
var c__4556__auto___36324 = cljs.core.chunk_first(seq__36169_36323__$1);
var G__36325 = cljs.core.chunk_rest(seq__36169_36323__$1);
var G__36326 = c__4556__auto___36324;
var G__36327 = cljs.core.count(c__4556__auto___36324);
var G__36328 = (0);
seq__36169_36307 = G__36325;
chunk__36170_36308 = G__36326;
count__36171_36309 = G__36327;
i__36172_36310 = G__36328;
continue;
} else {
var map__36179_36329 = cljs.core.first(seq__36169_36323__$1);
var map__36179_36330__$1 = (((((!((map__36179_36329 == null))))?(((((map__36179_36329.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__36179_36329.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__36179_36329):map__36179_36329);
var w_36331 = map__36179_36330__$1;
var msg_36332__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36179_36330__$1,new cljs.core.Keyword(null,"msg","msg",-1386103444));
var line_36333 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36179_36330__$1,new cljs.core.Keyword(null,"line","line",212345235));
var column_36334 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36179_36330__$1,new cljs.core.Keyword(null,"column","column",2078222095));
var resource_name_36335 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36179_36330__$1,new cljs.core.Keyword(null,"resource-name","resource-name",2001617100));
console.warn(["BUILD-WARNING in ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(resource_name_36335)," at [",cljs.core.str.cljs$core$IFn$_invoke$arity$1(line_36333),":",cljs.core.str.cljs$core$IFn$_invoke$arity$1(column_36334),"]\n\t",cljs.core.str.cljs$core$IFn$_invoke$arity$1(msg_36332__$1)].join(''));


var G__36336 = cljs.core.next(seq__36169_36323__$1);
var G__36337 = null;
var G__36338 = (0);
var G__36339 = (0);
seq__36169_36307 = G__36336;
chunk__36170_36308 = G__36337;
count__36171_36309 = G__36338;
i__36172_36310 = G__36339;
continue;
}
} else {
}
}
break;
}

if((!(shadow.cljs.devtools.client.env.autoload))){
return shadow.cljs.devtools.client.hud.load_end_success();
} else {
if(((cljs.core.empty_QMARK_(warnings)) || (shadow.cljs.devtools.client.env.ignore_warnings))){
var sources_to_get = shadow.cljs.devtools.client.env.filter_reload_sources(info,reload_info);
if(cljs.core.not(cljs.core.seq(sources_to_get))){
return shadow.cljs.devtools.client.hud.load_end_success();
} else {
if(cljs.core.seq(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(msg,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"reload-info","reload-info",1648088086),new cljs.core.Keyword(null,"after-load","after-load",-1278503285)], null)))){
} else {
shadow.cljs.devtools.client.browser.devtools_msg.cljs$core$IFn$_invoke$arity$variadic("reloading code but no :after-load hooks are configured!",cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["https://shadow-cljs.github.io/docs/UsersGuide.html#_lifecycle_hooks"], 0));
}

return shadow.cljs.devtools.client.shared.load_sources(runtime,sources_to_get,(function (p1__36156_SHARP_){
return shadow.cljs.devtools.client.browser.do_js_reload(msg,p1__36156_SHARP_,shadow.cljs.devtools.client.hud.load_end_success,shadow.cljs.devtools.client.hud.load_failure);
}));
}
} else {
return null;
}
}
});
shadow.cljs.devtools.client.browser.page_load_uri = (cljs.core.truth_(goog.global.document)?goog.Uri.parse(document.location.href):null);
shadow.cljs.devtools.client.browser.match_paths = (function shadow$cljs$devtools$client$browser$match_paths(old,new$){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2("file",shadow.cljs.devtools.client.browser.page_load_uri.getScheme())){
var rel_new = cljs.core.subs.cljs$core$IFn$_invoke$arity$2(new$,(1));
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(old,rel_new)) || (clojure.string.starts_with_QMARK_(old,[rel_new,"?"].join(''))))){
return rel_new;
} else {
return null;
}
} else {
var node_uri = goog.Uri.parse(old);
var node_uri_resolved = shadow.cljs.devtools.client.browser.page_load_uri.resolve(node_uri);
var node_abs = node_uri_resolved.getPath();
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$1(shadow.cljs.devtools.client.browser.page_load_uri.hasSameDomainAs(node_uri))) || (cljs.core.not(node_uri.hasDomain())))){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(node_abs,new$)){
return new$;
} else {
return false;
}
} else {
return false;
}
}
});
shadow.cljs.devtools.client.browser.handle_asset_update = (function shadow$cljs$devtools$client$browser$handle_asset_update(p__36181){
var map__36182 = p__36181;
var map__36182__$1 = (((((!((map__36182 == null))))?(((((map__36182.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__36182.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__36182):map__36182);
var msg = map__36182__$1;
var updates = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36182__$1,new cljs.core.Keyword(null,"updates","updates",2013983452));
var seq__36184 = cljs.core.seq(updates);
var chunk__36186 = null;
var count__36187 = (0);
var i__36188 = (0);
while(true){
if((i__36188 < count__36187)){
var path = chunk__36186.cljs$core$IIndexed$_nth$arity$2(null,i__36188);
if(clojure.string.ends_with_QMARK_(path,"css")){
var seq__36215_36340 = cljs.core.seq(cljs.core.array_seq.cljs$core$IFn$_invoke$arity$1(document.querySelectorAll("link[rel=\"stylesheet\"]")));
var chunk__36218_36341 = null;
var count__36219_36342 = (0);
var i__36220_36343 = (0);
while(true){
if((i__36220_36343 < count__36219_36342)){
var node_36344 = chunk__36218_36341.cljs$core$IIndexed$_nth$arity$2(null,i__36220_36343);
var path_match_36345 = shadow.cljs.devtools.client.browser.match_paths(node_36344.getAttribute("href"),path);
if(cljs.core.truth_(path_match_36345)){
var new_link_36346 = (function (){var G__36225 = node_36344.cloneNode(true);
G__36225.setAttribute("href",[cljs.core.str.cljs$core$IFn$_invoke$arity$1(path_match_36345),"?r=",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.rand.cljs$core$IFn$_invoke$arity$0())].join(''));

return G__36225;
})();
(new_link_36346.onload = ((function (seq__36215_36340,chunk__36218_36341,count__36219_36342,i__36220_36343,seq__36184,chunk__36186,count__36187,i__36188,new_link_36346,path_match_36345,node_36344,path,map__36182,map__36182__$1,msg,updates){
return (function (){
return goog.dom.removeNode(node_36344);
});})(seq__36215_36340,chunk__36218_36341,count__36219_36342,i__36220_36343,seq__36184,chunk__36186,count__36187,i__36188,new_link_36346,path_match_36345,node_36344,path,map__36182,map__36182__$1,msg,updates))
);

shadow.cljs.devtools.client.browser.devtools_msg.cljs$core$IFn$_invoke$arity$variadic("load CSS",cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([path_match_36345], 0));

goog.dom.insertSiblingAfter(new_link_36346,node_36344);


var G__36347 = seq__36215_36340;
var G__36348 = chunk__36218_36341;
var G__36349 = count__36219_36342;
var G__36350 = (i__36220_36343 + (1));
seq__36215_36340 = G__36347;
chunk__36218_36341 = G__36348;
count__36219_36342 = G__36349;
i__36220_36343 = G__36350;
continue;
} else {
var G__36351 = seq__36215_36340;
var G__36352 = chunk__36218_36341;
var G__36353 = count__36219_36342;
var G__36354 = (i__36220_36343 + (1));
seq__36215_36340 = G__36351;
chunk__36218_36341 = G__36352;
count__36219_36342 = G__36353;
i__36220_36343 = G__36354;
continue;
}
} else {
var temp__5735__auto___36355 = cljs.core.seq(seq__36215_36340);
if(temp__5735__auto___36355){
var seq__36215_36356__$1 = temp__5735__auto___36355;
if(cljs.core.chunked_seq_QMARK_(seq__36215_36356__$1)){
var c__4556__auto___36357 = cljs.core.chunk_first(seq__36215_36356__$1);
var G__36358 = cljs.core.chunk_rest(seq__36215_36356__$1);
var G__36359 = c__4556__auto___36357;
var G__36360 = cljs.core.count(c__4556__auto___36357);
var G__36361 = (0);
seq__36215_36340 = G__36358;
chunk__36218_36341 = G__36359;
count__36219_36342 = G__36360;
i__36220_36343 = G__36361;
continue;
} else {
var node_36362 = cljs.core.first(seq__36215_36356__$1);
var path_match_36363 = shadow.cljs.devtools.client.browser.match_paths(node_36362.getAttribute("href"),path);
if(cljs.core.truth_(path_match_36363)){
var new_link_36364 = (function (){var G__36226 = node_36362.cloneNode(true);
G__36226.setAttribute("href",[cljs.core.str.cljs$core$IFn$_invoke$arity$1(path_match_36363),"?r=",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.rand.cljs$core$IFn$_invoke$arity$0())].join(''));

return G__36226;
})();
(new_link_36364.onload = ((function (seq__36215_36340,chunk__36218_36341,count__36219_36342,i__36220_36343,seq__36184,chunk__36186,count__36187,i__36188,new_link_36364,path_match_36363,node_36362,seq__36215_36356__$1,temp__5735__auto___36355,path,map__36182,map__36182__$1,msg,updates){
return (function (){
return goog.dom.removeNode(node_36362);
});})(seq__36215_36340,chunk__36218_36341,count__36219_36342,i__36220_36343,seq__36184,chunk__36186,count__36187,i__36188,new_link_36364,path_match_36363,node_36362,seq__36215_36356__$1,temp__5735__auto___36355,path,map__36182,map__36182__$1,msg,updates))
);

shadow.cljs.devtools.client.browser.devtools_msg.cljs$core$IFn$_invoke$arity$variadic("load CSS",cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([path_match_36363], 0));

goog.dom.insertSiblingAfter(new_link_36364,node_36362);


var G__36365 = cljs.core.next(seq__36215_36356__$1);
var G__36366 = null;
var G__36367 = (0);
var G__36368 = (0);
seq__36215_36340 = G__36365;
chunk__36218_36341 = G__36366;
count__36219_36342 = G__36367;
i__36220_36343 = G__36368;
continue;
} else {
var G__36369 = cljs.core.next(seq__36215_36356__$1);
var G__36370 = null;
var G__36371 = (0);
var G__36372 = (0);
seq__36215_36340 = G__36369;
chunk__36218_36341 = G__36370;
count__36219_36342 = G__36371;
i__36220_36343 = G__36372;
continue;
}
}
} else {
}
}
break;
}


var G__36373 = seq__36184;
var G__36374 = chunk__36186;
var G__36375 = count__36187;
var G__36376 = (i__36188 + (1));
seq__36184 = G__36373;
chunk__36186 = G__36374;
count__36187 = G__36375;
i__36188 = G__36376;
continue;
} else {
var G__36377 = seq__36184;
var G__36378 = chunk__36186;
var G__36379 = count__36187;
var G__36380 = (i__36188 + (1));
seq__36184 = G__36377;
chunk__36186 = G__36378;
count__36187 = G__36379;
i__36188 = G__36380;
continue;
}
} else {
var temp__5735__auto__ = cljs.core.seq(seq__36184);
if(temp__5735__auto__){
var seq__36184__$1 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__36184__$1)){
var c__4556__auto__ = cljs.core.chunk_first(seq__36184__$1);
var G__36381 = cljs.core.chunk_rest(seq__36184__$1);
var G__36382 = c__4556__auto__;
var G__36383 = cljs.core.count(c__4556__auto__);
var G__36384 = (0);
seq__36184 = G__36381;
chunk__36186 = G__36382;
count__36187 = G__36383;
i__36188 = G__36384;
continue;
} else {
var path = cljs.core.first(seq__36184__$1);
if(clojure.string.ends_with_QMARK_(path,"css")){
var seq__36227_36385 = cljs.core.seq(cljs.core.array_seq.cljs$core$IFn$_invoke$arity$1(document.querySelectorAll("link[rel=\"stylesheet\"]")));
var chunk__36230_36386 = null;
var count__36231_36387 = (0);
var i__36232_36388 = (0);
while(true){
if((i__36232_36388 < count__36231_36387)){
var node_36389 = chunk__36230_36386.cljs$core$IIndexed$_nth$arity$2(null,i__36232_36388);
var path_match_36390 = shadow.cljs.devtools.client.browser.match_paths(node_36389.getAttribute("href"),path);
if(cljs.core.truth_(path_match_36390)){
var new_link_36391 = (function (){var G__36237 = node_36389.cloneNode(true);
G__36237.setAttribute("href",[cljs.core.str.cljs$core$IFn$_invoke$arity$1(path_match_36390),"?r=",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.rand.cljs$core$IFn$_invoke$arity$0())].join(''));

return G__36237;
})();
(new_link_36391.onload = ((function (seq__36227_36385,chunk__36230_36386,count__36231_36387,i__36232_36388,seq__36184,chunk__36186,count__36187,i__36188,new_link_36391,path_match_36390,node_36389,path,seq__36184__$1,temp__5735__auto__,map__36182,map__36182__$1,msg,updates){
return (function (){
return goog.dom.removeNode(node_36389);
});})(seq__36227_36385,chunk__36230_36386,count__36231_36387,i__36232_36388,seq__36184,chunk__36186,count__36187,i__36188,new_link_36391,path_match_36390,node_36389,path,seq__36184__$1,temp__5735__auto__,map__36182,map__36182__$1,msg,updates))
);

shadow.cljs.devtools.client.browser.devtools_msg.cljs$core$IFn$_invoke$arity$variadic("load CSS",cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([path_match_36390], 0));

goog.dom.insertSiblingAfter(new_link_36391,node_36389);


var G__36392 = seq__36227_36385;
var G__36393 = chunk__36230_36386;
var G__36394 = count__36231_36387;
var G__36395 = (i__36232_36388 + (1));
seq__36227_36385 = G__36392;
chunk__36230_36386 = G__36393;
count__36231_36387 = G__36394;
i__36232_36388 = G__36395;
continue;
} else {
var G__36396 = seq__36227_36385;
var G__36397 = chunk__36230_36386;
var G__36398 = count__36231_36387;
var G__36399 = (i__36232_36388 + (1));
seq__36227_36385 = G__36396;
chunk__36230_36386 = G__36397;
count__36231_36387 = G__36398;
i__36232_36388 = G__36399;
continue;
}
} else {
var temp__5735__auto___36400__$1 = cljs.core.seq(seq__36227_36385);
if(temp__5735__auto___36400__$1){
var seq__36227_36401__$1 = temp__5735__auto___36400__$1;
if(cljs.core.chunked_seq_QMARK_(seq__36227_36401__$1)){
var c__4556__auto___36402 = cljs.core.chunk_first(seq__36227_36401__$1);
var G__36403 = cljs.core.chunk_rest(seq__36227_36401__$1);
var G__36404 = c__4556__auto___36402;
var G__36405 = cljs.core.count(c__4556__auto___36402);
var G__36406 = (0);
seq__36227_36385 = G__36403;
chunk__36230_36386 = G__36404;
count__36231_36387 = G__36405;
i__36232_36388 = G__36406;
continue;
} else {
var node_36407 = cljs.core.first(seq__36227_36401__$1);
var path_match_36408 = shadow.cljs.devtools.client.browser.match_paths(node_36407.getAttribute("href"),path);
if(cljs.core.truth_(path_match_36408)){
var new_link_36409 = (function (){var G__36238 = node_36407.cloneNode(true);
G__36238.setAttribute("href",[cljs.core.str.cljs$core$IFn$_invoke$arity$1(path_match_36408),"?r=",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.rand.cljs$core$IFn$_invoke$arity$0())].join(''));

return G__36238;
})();
(new_link_36409.onload = ((function (seq__36227_36385,chunk__36230_36386,count__36231_36387,i__36232_36388,seq__36184,chunk__36186,count__36187,i__36188,new_link_36409,path_match_36408,node_36407,seq__36227_36401__$1,temp__5735__auto___36400__$1,path,seq__36184__$1,temp__5735__auto__,map__36182,map__36182__$1,msg,updates){
return (function (){
return goog.dom.removeNode(node_36407);
});})(seq__36227_36385,chunk__36230_36386,count__36231_36387,i__36232_36388,seq__36184,chunk__36186,count__36187,i__36188,new_link_36409,path_match_36408,node_36407,seq__36227_36401__$1,temp__5735__auto___36400__$1,path,seq__36184__$1,temp__5735__auto__,map__36182,map__36182__$1,msg,updates))
);

shadow.cljs.devtools.client.browser.devtools_msg.cljs$core$IFn$_invoke$arity$variadic("load CSS",cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([path_match_36408], 0));

goog.dom.insertSiblingAfter(new_link_36409,node_36407);


var G__36410 = cljs.core.next(seq__36227_36401__$1);
var G__36411 = null;
var G__36412 = (0);
var G__36413 = (0);
seq__36227_36385 = G__36410;
chunk__36230_36386 = G__36411;
count__36231_36387 = G__36412;
i__36232_36388 = G__36413;
continue;
} else {
var G__36414 = cljs.core.next(seq__36227_36401__$1);
var G__36415 = null;
var G__36416 = (0);
var G__36417 = (0);
seq__36227_36385 = G__36414;
chunk__36230_36386 = G__36415;
count__36231_36387 = G__36416;
i__36232_36388 = G__36417;
continue;
}
}
} else {
}
}
break;
}


var G__36418 = cljs.core.next(seq__36184__$1);
var G__36419 = null;
var G__36420 = (0);
var G__36421 = (0);
seq__36184 = G__36418;
chunk__36186 = G__36419;
count__36187 = G__36420;
i__36188 = G__36421;
continue;
} else {
var G__36422 = cljs.core.next(seq__36184__$1);
var G__36423 = null;
var G__36424 = (0);
var G__36425 = (0);
seq__36184 = G__36422;
chunk__36186 = G__36423;
count__36187 = G__36424;
i__36188 = G__36425;
continue;
}
}
} else {
return null;
}
}
break;
}
});
shadow.cljs.devtools.client.browser.global_eval = (function shadow$cljs$devtools$client$browser$global_eval(js){
if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2("undefined",typeof(module))){
return eval(js);
} else {
return (0,eval)(js);;
}
});
shadow.cljs.devtools.client.browser.repl_init = (function shadow$cljs$devtools$client$browser$repl_init(runtime,p__36239){
var map__36240 = p__36239;
var map__36240__$1 = (((((!((map__36240 == null))))?(((((map__36240.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__36240.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__36240):map__36240);
var repl_state = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36240__$1,new cljs.core.Keyword(null,"repl-state","repl-state",-1733780387));
return shadow.cljs.devtools.client.shared.load_sources(runtime,cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentVector.EMPTY,cljs.core.remove.cljs$core$IFn$_invoke$arity$2(shadow.cljs.devtools.client.env.src_is_loaded_QMARK_,new cljs.core.Keyword(null,"repl-sources","repl-sources",723867535).cljs$core$IFn$_invoke$arity$1(repl_state))),(function (sources){
shadow.cljs.devtools.client.browser.do_js_load(sources);

return shadow.cljs.devtools.client.browser.devtools_msg("ready!");
}));
});
shadow.cljs.devtools.client.browser.client_info = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"host","host",-1558485167),(cljs.core.truth_(goog.global.document)?new cljs.core.Keyword(null,"browser","browser",828191719):new cljs.core.Keyword(null,"browser-worker","browser-worker",1638998282)),new cljs.core.Keyword(null,"user-agent","user-agent",1220426212),[(cljs.core.truth_(goog.userAgent.OPERA)?"Opera":(cljs.core.truth_(goog.userAgent.product.CHROME)?"Chrome":(cljs.core.truth_(goog.userAgent.IE)?"MSIE":(cljs.core.truth_(goog.userAgent.EDGE)?"Edge":(cljs.core.truth_(goog.userAgent.GECKO)?"Firefox":(cljs.core.truth_(goog.userAgent.SAFARI)?"Safari":(cljs.core.truth_(goog.userAgent.WEBKIT)?"Webkit":null)))))))," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(goog.userAgent.VERSION)," [",cljs.core.str.cljs$core$IFn$_invoke$arity$1(goog.userAgent.PLATFORM),"]"].join(''),new cljs.core.Keyword(null,"dom","dom",-1236537922),(!((goog.global.document == null)))], null);
if((typeof shadow !== 'undefined') && (typeof shadow.cljs !== 'undefined') && (typeof shadow.cljs.devtools !== 'undefined') && (typeof shadow.cljs.devtools.client !== 'undefined') && (typeof shadow.cljs.devtools.client.browser !== 'undefined') && (typeof shadow.cljs.devtools.client.browser.ws_was_welcome_ref !== 'undefined')){
} else {
shadow.cljs.devtools.client.browser.ws_was_welcome_ref = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(false);
}
if(((shadow.cljs.devtools.client.env.enabled) && ((shadow.cljs.devtools.client.env.worker_client_id > (0))))){
(shadow.cljs.devtools.client.shared.Runtime.prototype.shadow$remote$runtime$api$IEvalJS$ = cljs.core.PROTOCOL_SENTINEL);

(shadow.cljs.devtools.client.shared.Runtime.prototype.shadow$remote$runtime$api$IEvalJS$_js_eval$arity$2 = (function (this$,code){
var this$__$1 = this;
return shadow.cljs.devtools.client.browser.global_eval(code);
}));

(shadow.cljs.devtools.client.shared.Runtime.prototype.shadow$cljs$devtools$client$shared$IHostSpecific$ = cljs.core.PROTOCOL_SENTINEL);

(shadow.cljs.devtools.client.shared.Runtime.prototype.shadow$cljs$devtools$client$shared$IHostSpecific$do_invoke$arity$2 = (function (this$,p__36244){
var map__36245 = p__36244;
var map__36245__$1 = (((((!((map__36245 == null))))?(((((map__36245.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__36245.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__36245):map__36245);
var _ = map__36245__$1;
var js = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36245__$1,new cljs.core.Keyword(null,"js","js",1768080579));
var this$__$1 = this;
return shadow.cljs.devtools.client.browser.global_eval(js);
}));

(shadow.cljs.devtools.client.shared.Runtime.prototype.shadow$cljs$devtools$client$shared$IHostSpecific$do_repl_init$arity$4 = (function (runtime,p__36247,done,error){
var map__36248 = p__36247;
var map__36248__$1 = (((((!((map__36248 == null))))?(((((map__36248.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__36248.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__36248):map__36248);
var repl_sources = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36248__$1,new cljs.core.Keyword(null,"repl-sources","repl-sources",723867535));
var runtime__$1 = this;
return shadow.cljs.devtools.client.shared.load_sources(runtime__$1,cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentVector.EMPTY,cljs.core.remove.cljs$core$IFn$_invoke$arity$2(shadow.cljs.devtools.client.env.src_is_loaded_QMARK_,repl_sources)),(function (sources){
shadow.cljs.devtools.client.browser.do_js_load(sources);

return (done.cljs$core$IFn$_invoke$arity$0 ? done.cljs$core$IFn$_invoke$arity$0() : done.call(null));
}));
}));

(shadow.cljs.devtools.client.shared.Runtime.prototype.shadow$cljs$devtools$client$shared$IHostSpecific$do_repl_require$arity$4 = (function (runtime,p__36250,done,error){
var map__36251 = p__36250;
var map__36251__$1 = (((((!((map__36251 == null))))?(((((map__36251.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__36251.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__36251):map__36251);
var msg = map__36251__$1;
var sources = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36251__$1,new cljs.core.Keyword(null,"sources","sources",-321166424));
var reload_namespaces = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36251__$1,new cljs.core.Keyword(null,"reload-namespaces","reload-namespaces",250210134));
var js_requires = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36251__$1,new cljs.core.Keyword(null,"js-requires","js-requires",-1311472051));
var runtime__$1 = this;
var sources_to_load = cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentVector.EMPTY,cljs.core.remove.cljs$core$IFn$_invoke$arity$2((function (p__36253){
var map__36254 = p__36253;
var map__36254__$1 = (((((!((map__36254 == null))))?(((((map__36254.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__36254.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__36254):map__36254);
var src = map__36254__$1;
var provides = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36254__$1,new cljs.core.Keyword(null,"provides","provides",-1634397992));
var and__4115__auto__ = shadow.cljs.devtools.client.env.src_is_loaded_QMARK_(src);
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(cljs.core.some(reload_namespaces,provides));
} else {
return and__4115__auto__;
}
}),sources));
if(cljs.core.not(cljs.core.seq(sources_to_load))){
var G__36256 = cljs.core.PersistentVector.EMPTY;
return (done.cljs$core$IFn$_invoke$arity$1 ? done.cljs$core$IFn$_invoke$arity$1(G__36256) : done.call(null,G__36256));
} else {
return shadow.remote.runtime.shared.call.cljs$core$IFn$_invoke$arity$3(runtime__$1,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"op","op",-1882987955),new cljs.core.Keyword(null,"cljs-load-sources","cljs-load-sources",-1458295962),new cljs.core.Keyword(null,"to","to",192099007),shadow.cljs.devtools.client.env.worker_client_id,new cljs.core.Keyword(null,"sources","sources",-321166424),cljs.core.into.cljs$core$IFn$_invoke$arity$3(cljs.core.PersistentVector.EMPTY,cljs.core.map.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"resource-id","resource-id",-1308422582)),sources_to_load)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"cljs-sources","cljs-sources",31121610),(function (p__36257){
var map__36258 = p__36257;
var map__36258__$1 = (((((!((map__36258 == null))))?(((((map__36258.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__36258.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__36258):map__36258);
var msg__$1 = map__36258__$1;
var sources__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36258__$1,new cljs.core.Keyword(null,"sources","sources",-321166424));
try{shadow.cljs.devtools.client.browser.do_js_load(sources__$1);

if(cljs.core.seq(js_requires)){
shadow.cljs.devtools.client.browser.do_js_requires(js_requires);
} else {
}

return (done.cljs$core$IFn$_invoke$arity$1 ? done.cljs$core$IFn$_invoke$arity$1(sources_to_load) : done.call(null,sources_to_load));
}catch (e36260){var ex = e36260;
return (error.cljs$core$IFn$_invoke$arity$1 ? error.cljs$core$IFn$_invoke$arity$1(ex) : error.call(null,ex));
}})], null));
}
}));

shadow.cljs.devtools.client.shared.add_plugin_BANG_(new cljs.core.Keyword("shadow.cljs.devtools.client.browser","client","shadow.cljs.devtools.client.browser/client",-1461019282),cljs.core.PersistentHashSet.EMPTY,(function (p__36261){
var map__36262 = p__36261;
var map__36262__$1 = (((((!((map__36262 == null))))?(((((map__36262.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__36262.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__36262):map__36262);
var env = map__36262__$1;
var runtime = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36262__$1,new cljs.core.Keyword(null,"runtime","runtime",-1331573996));
var svc = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"runtime","runtime",-1331573996),runtime], null);
shadow.remote.runtime.api.add_extension(runtime,new cljs.core.Keyword("shadow.cljs.devtools.client.browser","client","shadow.cljs.devtools.client.browser/client",-1461019282),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"on-welcome","on-welcome",1895317125),(function (){
cljs.core.reset_BANG_(shadow.cljs.devtools.client.browser.ws_was_welcome_ref,true);

shadow.cljs.devtools.client.hud.connection_error_clear_BANG_();

shadow.cljs.devtools.client.env.patch_goog_BANG_();

return shadow.cljs.devtools.client.browser.devtools_msg(["#",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"client-id","client-id",-464622140).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(new cljs.core.Keyword(null,"state-ref","state-ref",2127874952).cljs$core$IFn$_invoke$arity$1(runtime))))," ready!"].join(''));
}),new cljs.core.Keyword(null,"on-disconnect","on-disconnect",-809021814),(function (e){
if(cljs.core.truth_(cljs.core.deref(shadow.cljs.devtools.client.browser.ws_was_welcome_ref))){
shadow.cljs.devtools.client.hud.connection_error("The Websocket connection was closed!");

return cljs.core.reset_BANG_(shadow.cljs.devtools.client.browser.ws_was_welcome_ref,false);
} else {
return null;
}
}),new cljs.core.Keyword(null,"on-reconnect","on-reconnect",1239988702),(function (e){
return shadow.cljs.devtools.client.hud.connection_error("Reconnecting ...");
}),new cljs.core.Keyword(null,"ops","ops",1237330063),new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"access-denied","access-denied",959449406),(function (msg){
cljs.core.reset_BANG_(shadow.cljs.devtools.client.browser.ws_was_welcome_ref,false);

return shadow.cljs.devtools.client.hud.connection_error(["Stale Output! Your loaded JS was not produced by the running shadow-cljs instance."," Is the watch for this build running?"].join(''));
}),new cljs.core.Keyword(null,"cljs-runtime-init","cljs-runtime-init",1305890232),(function (msg){
return shadow.cljs.devtools.client.browser.repl_init(runtime,msg);
}),new cljs.core.Keyword(null,"cljs-asset-update","cljs-asset-update",1224093028),(function (p__36264){
var map__36265 = p__36264;
var map__36265__$1 = (((((!((map__36265 == null))))?(((((map__36265.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__36265.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__36265):map__36265);
var msg = map__36265__$1;
var updates = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36265__$1,new cljs.core.Keyword(null,"updates","updates",2013983452));
return shadow.cljs.devtools.client.browser.handle_asset_update(msg);
}),new cljs.core.Keyword(null,"cljs-build-configure","cljs-build-configure",-2089891268),(function (msg){
return null;
}),new cljs.core.Keyword(null,"cljs-build-start","cljs-build-start",-725781241),(function (msg){
shadow.cljs.devtools.client.hud.hud_hide();

shadow.cljs.devtools.client.hud.load_start();

return shadow.cljs.devtools.client.env.run_custom_notify_BANG_(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(msg,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"build-start","build-start",-959649480)));
}),new cljs.core.Keyword(null,"cljs-build-complete","cljs-build-complete",273626153),(function (msg){
var msg__$1 = shadow.cljs.devtools.client.env.add_warnings_to_info(msg);
shadow.cljs.devtools.client.hud.hud_warnings(msg__$1);

shadow.cljs.devtools.client.browser.handle_build_complete(runtime,msg__$1);

return shadow.cljs.devtools.client.env.run_custom_notify_BANG_(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(msg__$1,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"build-complete","build-complete",-501868472)));
}),new cljs.core.Keyword(null,"cljs-build-failure","cljs-build-failure",1718154990),(function (msg){
shadow.cljs.devtools.client.hud.load_end();

shadow.cljs.devtools.client.hud.hud_error(msg);

return shadow.cljs.devtools.client.env.run_custom_notify_BANG_(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(msg,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"build-failure","build-failure",-2107487466)));
}),new cljs.core.Keyword("shadow.cljs.devtools.client.env","worker-notify","shadow.cljs.devtools.client.env/worker-notify",-1456820670),(function (p__36267){
var map__36268 = p__36267;
var map__36268__$1 = (((((!((map__36268 == null))))?(((((map__36268.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__36268.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__36268):map__36268);
var event_op = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36268__$1,new cljs.core.Keyword(null,"event-op","event-op",200358057));
var client_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36268__$1,new cljs.core.Keyword(null,"client-id","client-id",-464622140));
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"client-disconnect","client-disconnect",640227957),event_op)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(client_id,shadow.cljs.devtools.client.env.worker_client_id)))){
shadow.cljs.devtools.client.hud.connection_error_clear_BANG_();

return shadow.cljs.devtools.client.hud.connection_error("The watch for this build was stopped!");
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"client-connect","client-connect",-1113973888),event_op)){
shadow.cljs.devtools.client.hud.connection_error_clear_BANG_();

return shadow.cljs.devtools.client.hud.connection_error("The watch for this build was restarted. Reload required!");
} else {
return null;
}
}
})], null)], null));

return svc;
}),(function (p__36270){
var map__36271 = p__36270;
var map__36271__$1 = (((((!((map__36271 == null))))?(((((map__36271.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__36271.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__36271):map__36271);
var svc = map__36271__$1;
var runtime = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36271__$1,new cljs.core.Keyword(null,"runtime","runtime",-1331573996));
return shadow.remote.runtime.api.del_extension(runtime,new cljs.core.Keyword("shadow.cljs.devtools.client.browser","client","shadow.cljs.devtools.client.browser/client",-1461019282));
}));

shadow.cljs.devtools.client.shared.init_runtime_BANG_(shadow.cljs.devtools.client.browser.client_info,shadow.cljs.devtools.client.websocket.start,shadow.cljs.devtools.client.websocket.send,shadow.cljs.devtools.client.websocket.stop);
} else {
}

//# sourceMappingURL=shadow.cljs.devtools.client.browser.js.map
