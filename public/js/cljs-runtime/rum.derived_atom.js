goog.provide('rum.derived_atom');
rum.derived_atom.derived_atom = (function rum$derived_atom$derived_atom(var_args){
var G__38236 = arguments.length;
switch (G__38236) {
case 3:
return rum.derived_atom.derived_atom.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 4:
return rum.derived_atom.derived_atom.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(rum.derived_atom.derived_atom.cljs$core$IFn$_invoke$arity$3 = (function (refs,key,f){
return rum.derived_atom.derived_atom.cljs$core$IFn$_invoke$arity$4(refs,key,f,cljs.core.PersistentArrayMap.EMPTY);
}));

(rum.derived_atom.derived_atom.cljs$core$IFn$_invoke$arity$4 = (function (refs,key,f,opts){
var map__38238 = opts;
var map__38238__$1 = (((((!((map__38238 == null))))?(((((map__38238.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38238.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38238):map__38238);
var ref = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38238__$1,new cljs.core.Keyword(null,"ref","ref",1289896967));
var check_equals_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__38238__$1,new cljs.core.Keyword(null,"check-equals?","check-equals?",-2005755315),true);
var recalc = (function (){var G__38240 = cljs.core.count(refs);
switch (G__38240) {
case (1):
var vec__38241 = refs;
var a = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38241,(0),null);
return (function (){
var G__38244 = cljs.core.deref(a);
return (f.cljs$core$IFn$_invoke$arity$1 ? f.cljs$core$IFn$_invoke$arity$1(G__38244) : f.call(null,G__38244));
});

break;
case (2):
var vec__38245 = refs;
var a = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38245,(0),null);
var b = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38245,(1),null);
return (function (){
var G__38248 = cljs.core.deref(a);
var G__38249 = cljs.core.deref(b);
return (f.cljs$core$IFn$_invoke$arity$2 ? f.cljs$core$IFn$_invoke$arity$2(G__38248,G__38249) : f.call(null,G__38248,G__38249));
});

break;
case (3):
var vec__38250 = refs;
var a = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38250,(0),null);
var b = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38250,(1),null);
var c = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38250,(2),null);
return (function (){
var G__38253 = cljs.core.deref(a);
var G__38254 = cljs.core.deref(b);
var G__38255 = cljs.core.deref(c);
return (f.cljs$core$IFn$_invoke$arity$3 ? f.cljs$core$IFn$_invoke$arity$3(G__38253,G__38254,G__38255) : f.call(null,G__38253,G__38254,G__38255));
});

break;
default:
return (function (){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(f,cljs.core.map.cljs$core$IFn$_invoke$arity$2(cljs.core.deref,refs));
});

}
})();
var sink = (cljs.core.truth_(ref)?(function (){var G__38256 = ref;
cljs.core.reset_BANG_(G__38256,(recalc.cljs$core$IFn$_invoke$arity$0 ? recalc.cljs$core$IFn$_invoke$arity$0() : recalc.call(null)));

return G__38256;
})():cljs.core.atom.cljs$core$IFn$_invoke$arity$1((recalc.cljs$core$IFn$_invoke$arity$0 ? recalc.cljs$core$IFn$_invoke$arity$0() : recalc.call(null))));
var watch = (cljs.core.truth_(check_equals_QMARK_)?(function (_,___$1,___$2,___$3){
var new_val = (recalc.cljs$core$IFn$_invoke$arity$0 ? recalc.cljs$core$IFn$_invoke$arity$0() : recalc.call(null));
if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(sink),new_val)){
return cljs.core.reset_BANG_(sink,new_val);
} else {
return null;
}
}):(function (_,___$1,___$2,___$3){
return cljs.core.reset_BANG_(sink,(recalc.cljs$core$IFn$_invoke$arity$0 ? recalc.cljs$core$IFn$_invoke$arity$0() : recalc.call(null)));
}));
var seq__38257_38263 = cljs.core.seq(refs);
var chunk__38258_38264 = null;
var count__38259_38265 = (0);
var i__38260_38266 = (0);
while(true){
if((i__38260_38266 < count__38259_38265)){
var ref_38267__$1 = chunk__38258_38264.cljs$core$IIndexed$_nth$arity$2(null,i__38260_38266);
cljs.core.add_watch(ref_38267__$1,key,watch);


var G__38268 = seq__38257_38263;
var G__38269 = chunk__38258_38264;
var G__38270 = count__38259_38265;
var G__38271 = (i__38260_38266 + (1));
seq__38257_38263 = G__38268;
chunk__38258_38264 = G__38269;
count__38259_38265 = G__38270;
i__38260_38266 = G__38271;
continue;
} else {
var temp__5735__auto___38272 = cljs.core.seq(seq__38257_38263);
if(temp__5735__auto___38272){
var seq__38257_38273__$1 = temp__5735__auto___38272;
if(cljs.core.chunked_seq_QMARK_(seq__38257_38273__$1)){
var c__4556__auto___38274 = cljs.core.chunk_first(seq__38257_38273__$1);
var G__38275 = cljs.core.chunk_rest(seq__38257_38273__$1);
var G__38276 = c__4556__auto___38274;
var G__38277 = cljs.core.count(c__4556__auto___38274);
var G__38278 = (0);
seq__38257_38263 = G__38275;
chunk__38258_38264 = G__38276;
count__38259_38265 = G__38277;
i__38260_38266 = G__38278;
continue;
} else {
var ref_38279__$1 = cljs.core.first(seq__38257_38273__$1);
cljs.core.add_watch(ref_38279__$1,key,watch);


var G__38280 = cljs.core.next(seq__38257_38273__$1);
var G__38281 = null;
var G__38282 = (0);
var G__38283 = (0);
seq__38257_38263 = G__38280;
chunk__38258_38264 = G__38281;
count__38259_38265 = G__38282;
i__38260_38266 = G__38283;
continue;
}
} else {
}
}
break;
}

return sink;
}));

(rum.derived_atom.derived_atom.cljs$lang$maxFixedArity = 4);


//# sourceMappingURL=rum.derived_atom.js.map
