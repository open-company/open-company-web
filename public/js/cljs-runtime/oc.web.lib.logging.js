goog.provide('oc.web.lib.logging');
oc.web.lib.logging.carrot_log_level = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
oc.web.lib.logging.config_log_level_BANG_ = (function oc$web$lib$logging$config_log_level_BANG_(level){
var level_kw = cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(cuerdas.core.lower(level));
if(cljs.core.truth_((function (){var fexpr__51338 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"report","report",1394055010),null,new cljs.core.Keyword(null,"warn","warn",-436710552),null,new cljs.core.Keyword(null,"trace","trace",-1082747415),null,new cljs.core.Keyword(null,"debug","debug",-1608172596),null,new cljs.core.Keyword(null,"fatal","fatal",1874419888),null,new cljs.core.Keyword(null,"info","info",-317069002),null,new cljs.core.Keyword(null,"error","error",-978969032),null], null), null);
return (fexpr__51338.cljs$core$IFn$_invoke$arity$1 ? fexpr__51338.cljs$core$IFn$_invoke$arity$1(level_kw) : fexpr__51338.call(null,level_kw));
})())){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.lib.logging",null,10,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Log level:",level_kw], null);
}),null)),null,462128564);

cljs.core.reset_BANG_(oc.web.lib.logging.carrot_log_level,level_kw);

return taoensso.timbre.merge_config_BANG_(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"level","level",1290497552),level_kw], null));
} else {
return null;
}
});
oc.web.lib.logging.dbg = (function oc$web$lib$logging$dbg(var_args){
var args__4742__auto__ = [];
var len__4736__auto___51340 = arguments.length;
var i__4737__auto___51341 = (0);
while(true){
if((i__4737__auto___51341 < len__4736__auto___51340)){
args__4742__auto__.push((arguments[i__4737__auto___51341]));

var G__51342 = (i__4737__auto___51341 + (1));
i__4737__auto___51341 = G__51342;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.lib.logging.dbg.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.lib.logging.dbg.cljs$core$IFn$_invoke$arity$variadic = (function (args){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.lib.logging.carrot_log_level),new cljs.core.Keyword(null,"debug","debug",-1608172596))){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(console.log,args);
} else {
return null;
}
}));

(oc.web.lib.logging.dbg.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.lib.logging.dbg.cljs$lang$applyTo = (function (seq51339){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq51339));
}));

(window.OCWebConfigLogLevel = oc.web.lib.logging.config_log_level_BANG_);

//# sourceMappingURL=oc.web.lib.logging.js.map
