goog.provide('oc.shared.interval');
oc.shared.interval.make_interval = (function oc$shared$interval$make_interval(p__51316){
var map__51317 = p__51316;
var map__51317__$1 = (((((!((map__51317 == null))))?(((((map__51317.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__51317.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__51317):map__51317);
var fn = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__51317__$1,new cljs.core.Keyword(null,"fn","fn",-1175266204));
var ms = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__51317__$1,new cljs.core.Keyword(null,"ms","ms",-1152709733));
return cljs.core.atom.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword("oc.shared.interval","fn","oc.shared.interval/fn",1656286328),fn,new cljs.core.Keyword("oc.shared.interval","ms","oc.shared.interval/ms",-1929552977),ms], null));
});
oc.shared.interval.stop_interval_STAR_ = (function oc$shared$interval$stop_interval_STAR_(p__51319){
var map__51320 = p__51319;
var map__51320__$1 = (((((!((map__51320 == null))))?(((((map__51320.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__51320.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__51320):map__51320);
var interval = map__51320__$1;
var fn = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__51320__$1,new cljs.core.Keyword("oc.shared.interval","fn","oc.shared.interval/fn",1656286328));
var ms = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__51320__$1,new cljs.core.Keyword("oc.shared.interval","ms","oc.shared.interval/ms",-1929552977));
var js_interval = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__51320__$1,new cljs.core.Keyword("oc.shared.interval","js-interval","oc.shared.interval/js-interval",537981236));
if(cljs.core.truth_(js_interval)){
clearInterval(js_interval);
} else {
}

return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(interval,new cljs.core.Keyword("oc.shared.interval","js-interval","oc.shared.interval/js-interval",537981236));
});
oc.shared.interval.start_interval_STAR_ = (function oc$shared$interval$start_interval_STAR_(p__51322){
var map__51324 = p__51322;
var map__51324__$1 = (((((!((map__51324 == null))))?(((((map__51324.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__51324.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__51324):map__51324);
var interval = map__51324__$1;
var fn = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__51324__$1,new cljs.core.Keyword("oc.shared.interval","fn","oc.shared.interval/fn",1656286328));
var ms = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__51324__$1,new cljs.core.Keyword("oc.shared.interval","ms","oc.shared.interval/ms",-1929552977));
var js_interval = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__51324__$1,new cljs.core.Keyword("oc.shared.interval","js-interval","oc.shared.interval/js-interval",537981236));
if(cljs.core.not(js_interval)){
var new_js_interval = setInterval(fn,ms);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(interval,new cljs.core.Keyword("oc.shared.interval","js-interval","oc.shared.interval/js-interval",537981236),new_js_interval);
} else {
return interval;
}
});
oc.shared.interval.start_interval_BANG_ = (function oc$shared$interval$start_interval_BANG_(interval_atom){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(interval_atom,oc.shared.interval.start_interval_STAR_);
});
oc.shared.interval.stop_interval_BANG_ = (function oc$shared$interval$stop_interval_BANG_(interval_atom){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(interval_atom,oc.shared.interval.stop_interval_STAR_);
});
oc.shared.interval.restart_interval_BANG_ = (function oc$shared$interval$restart_interval_BANG_(var_args){
var G__51330 = arguments.length;
switch (G__51330) {
case 1:
return oc.shared.interval.restart_interval_BANG_.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.shared.interval.restart_interval_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.shared.interval.restart_interval_BANG_.cljs$core$IFn$_invoke$arity$1 = (function (interval_atom){
return oc.shared.interval.restart_interval_BANG_.cljs$core$IFn$_invoke$arity$2(interval_atom,new cljs.core.Keyword("oc.shared.interval","ms","oc.shared.interval/ms",-1929552977).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(interval_atom)));
}));

(oc.shared.interval.restart_interval_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (interval_atom,new_ms){
oc.shared.interval.stop_interval_BANG_(interval_atom);

return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(interval_atom,(function (p1__51328_SHARP_){
return oc.shared.interval.start_interval_STAR_(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__51328_SHARP_,new cljs.core.Keyword("oc.shared.interval","ms","oc.shared.interval/ms",-1929552977),new_ms));
}));
}));

(oc.shared.interval.restart_interval_BANG_.cljs$lang$maxFixedArity = 2);


//# sourceMappingURL=oc.shared.interval.js.map
