goog.provide('shadow.remote.runtime.tap_support');
shadow.remote.runtime.tap_support.tap_subscribe = (function shadow$remote$runtime$tap_support$tap_subscribe(p__35925,p__35926){
var map__35927 = p__35925;
var map__35927__$1 = (((((!((map__35927 == null))))?(((((map__35927.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__35927.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__35927):map__35927);
var svc = map__35927__$1;
var subs_ref = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35927__$1,new cljs.core.Keyword(null,"subs-ref","subs-ref",-1355989911));
var obj_support = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35927__$1,new cljs.core.Keyword(null,"obj-support","obj-support",1522559229));
var runtime = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35927__$1,new cljs.core.Keyword(null,"runtime","runtime",-1331573996));
var map__35928 = p__35926;
var map__35928__$1 = (((((!((map__35928 == null))))?(((((map__35928.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__35928.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__35928):map__35928);
var msg = map__35928__$1;
var from = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35928__$1,new cljs.core.Keyword(null,"from","from",1815293044));
var summary = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35928__$1,new cljs.core.Keyword(null,"summary","summary",380847952));
var history = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35928__$1,new cljs.core.Keyword(null,"history","history",-247395220));
var num = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__35928__$1,new cljs.core.Keyword(null,"num","num",1985240673),(10));
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(subs_ref,cljs.core.assoc,from,msg);

if(cljs.core.truth_(history)){
return shadow.remote.runtime.shared.reply(runtime,msg,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"op","op",-1882987955),new cljs.core.Keyword(null,"tap-subscribed","tap-subscribed",-1882247432),new cljs.core.Keyword(null,"history","history",-247395220),cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentVector.EMPTY,cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (oid){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"oid","oid",-768692334),oid,new cljs.core.Keyword(null,"summary","summary",380847952),shadow.remote.runtime.obj_support.obj_describe_STAR_(obj_support,oid)], null);
}),shadow.remote.runtime.obj_support.get_tap_history(obj_support,num)))], null));
} else {
return null;
}
});
shadow.remote.runtime.tap_support.tap_unsubscribe = (function shadow$remote$runtime$tap_support$tap_unsubscribe(p__35944,p__35945){
var map__35946 = p__35944;
var map__35946__$1 = (((((!((map__35946 == null))))?(((((map__35946.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__35946.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__35946):map__35946);
var subs_ref = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35946__$1,new cljs.core.Keyword(null,"subs-ref","subs-ref",-1355989911));
var map__35947 = p__35945;
var map__35947__$1 = (((((!((map__35947 == null))))?(((((map__35947.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__35947.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__35947):map__35947);
var from = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35947__$1,new cljs.core.Keyword(null,"from","from",1815293044));
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(subs_ref,cljs.core.dissoc,from);
});
shadow.remote.runtime.tap_support.request_tap_history = (function shadow$remote$runtime$tap_support$request_tap_history(p__35954,p__35955){
var map__35956 = p__35954;
var map__35956__$1 = (((((!((map__35956 == null))))?(((((map__35956.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__35956.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__35956):map__35956);
var obj_support = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35956__$1,new cljs.core.Keyword(null,"obj-support","obj-support",1522559229));
var runtime = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35956__$1,new cljs.core.Keyword(null,"runtime","runtime",-1331573996));
var map__35957 = p__35955;
var map__35957__$1 = (((((!((map__35957 == null))))?(((((map__35957.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__35957.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__35957):map__35957);
var msg = map__35957__$1;
var num = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__35957__$1,new cljs.core.Keyword(null,"num","num",1985240673),(10));
var tap_ids = shadow.remote.runtime.obj_support.get_tap_history(obj_support,num);
return shadow.remote.runtime.shared.reply(runtime,msg,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"op","op",-1882987955),new cljs.core.Keyword(null,"tap-history","tap-history",-282803347),new cljs.core.Keyword(null,"oids","oids",-1580877688),tap_ids], null));
});
shadow.remote.runtime.tap_support.tool_disconnect = (function shadow$remote$runtime$tap_support$tool_disconnect(p__35965,tid){
var map__35968 = p__35965;
var map__35968__$1 = (((((!((map__35968 == null))))?(((((map__35968.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__35968.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__35968):map__35968);
var svc = map__35968__$1;
var subs_ref = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35968__$1,new cljs.core.Keyword(null,"subs-ref","subs-ref",-1355989911));
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(subs_ref,cljs.core.dissoc,tid);
});
shadow.remote.runtime.tap_support.start = (function shadow$remote$runtime$tap_support$start(runtime,obj_support){
var subs_ref = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var tap_fn = (function shadow$remote$runtime$tap_support$start_$_runtime_tap(obj){
if((!((obj == null)))){
var oid = shadow.remote.runtime.obj_support.register(obj_support,obj,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"from","from",1815293044),new cljs.core.Keyword(null,"tap","tap",-1086702463)], null));
var seq__35978 = cljs.core.seq(cljs.core.deref(subs_ref));
var chunk__35979 = null;
var count__35980 = (0);
var i__35981 = (0);
while(true){
if((i__35981 < count__35980)){
var vec__35991 = chunk__35979.cljs$core$IIndexed$_nth$arity$2(null,i__35981);
var tid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__35991,(0),null);
var tap_config = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__35991,(1),null);
shadow.remote.runtime.api.relay_msg(runtime,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"op","op",-1882987955),new cljs.core.Keyword(null,"tap","tap",-1086702463),new cljs.core.Keyword(null,"to","to",192099007),tid,new cljs.core.Keyword(null,"oid","oid",-768692334),oid], null));


var G__36001 = seq__35978;
var G__36002 = chunk__35979;
var G__36003 = count__35980;
var G__36004 = (i__35981 + (1));
seq__35978 = G__36001;
chunk__35979 = G__36002;
count__35980 = G__36003;
i__35981 = G__36004;
continue;
} else {
var temp__5735__auto__ = cljs.core.seq(seq__35978);
if(temp__5735__auto__){
var seq__35978__$1 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__35978__$1)){
var c__4556__auto__ = cljs.core.chunk_first(seq__35978__$1);
var G__36005 = cljs.core.chunk_rest(seq__35978__$1);
var G__36006 = c__4556__auto__;
var G__36007 = cljs.core.count(c__4556__auto__);
var G__36008 = (0);
seq__35978 = G__36005;
chunk__35979 = G__36006;
count__35980 = G__36007;
i__35981 = G__36008;
continue;
} else {
var vec__35994 = cljs.core.first(seq__35978__$1);
var tid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__35994,(0),null);
var tap_config = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__35994,(1),null);
shadow.remote.runtime.api.relay_msg(runtime,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"op","op",-1882987955),new cljs.core.Keyword(null,"tap","tap",-1086702463),new cljs.core.Keyword(null,"to","to",192099007),tid,new cljs.core.Keyword(null,"oid","oid",-768692334),oid], null));


var G__36009 = cljs.core.next(seq__35978__$1);
var G__36010 = null;
var G__36011 = (0);
var G__36012 = (0);
seq__35978 = G__36009;
chunk__35979 = G__36010;
count__35980 = G__36011;
i__35981 = G__36012;
continue;
}
} else {
return null;
}
}
break;
}
} else {
return null;
}
});
var svc = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"runtime","runtime",-1331573996),runtime,new cljs.core.Keyword(null,"obj-support","obj-support",1522559229),obj_support,new cljs.core.Keyword(null,"tap-fn","tap-fn",1573556461),tap_fn,new cljs.core.Keyword(null,"subs-ref","subs-ref",-1355989911),subs_ref], null);
shadow.remote.runtime.api.add_extension(runtime,new cljs.core.Keyword("shadow.remote.runtime.tap-support","ext","shadow.remote.runtime.tap-support/ext",1019069674),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"ops","ops",1237330063),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"tap-subscribe","tap-subscribe",411179050),(function (p1__35971_SHARP_){
return shadow.remote.runtime.tap_support.tap_subscribe(svc,p1__35971_SHARP_);
}),new cljs.core.Keyword(null,"tap-unsubscribe","tap-unsubscribe",1183890755),(function (p1__35972_SHARP_){
return shadow.remote.runtime.tap_support.tap_unsubscribe(svc,p1__35972_SHARP_);
}),new cljs.core.Keyword(null,"request-tap-history","request-tap-history",-670837812),(function (p1__35973_SHARP_){
return shadow.remote.runtime.tap_support.request_tap_history(svc,p1__35973_SHARP_);
})], null),new cljs.core.Keyword(null,"on-tool-disconnect","on-tool-disconnect",693464366),(function (p1__35974_SHARP_){
return shadow.remote.runtime.tap_support.tool_disconnect(svc,p1__35974_SHARP_);
})], null));

cljs.core.add_tap(tap_fn);

return svc;
});
shadow.remote.runtime.tap_support.stop = (function shadow$remote$runtime$tap_support$stop(p__35998){
var map__35999 = p__35998;
var map__35999__$1 = (((((!((map__35999 == null))))?(((((map__35999.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__35999.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__35999):map__35999);
var svc = map__35999__$1;
var tap_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35999__$1,new cljs.core.Keyword(null,"tap-fn","tap-fn",1573556461));
var runtime = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35999__$1,new cljs.core.Keyword(null,"runtime","runtime",-1331573996));
cljs.core.remove_tap(tap_fn);

return shadow.remote.runtime.api.del_extension(runtime,new cljs.core.Keyword("shadow.remote.runtime.tap-support","ext","shadow.remote.runtime.tap-support/ext",1019069674));
});

//# sourceMappingURL=shadow.remote.runtime.tap_support.js.map
