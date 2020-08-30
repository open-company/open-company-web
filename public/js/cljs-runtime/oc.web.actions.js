goog.provide('oc.web.actions');
oc.web.actions.log = (function oc$web$actions$log(var_args){
var args__4742__auto__ = [];
var len__4736__auto___50456 = arguments.length;
var i__4737__auto___50457 = (0);
while(true){
if((i__4737__auto___50457 < len__4736__auto___50456)){
args__4742__auto__.push((arguments[i__4737__auto___50457]));

var G__50458 = (i__4737__auto___50457 + (1));
i__4737__auto___50457 = G__50458;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.actions.log.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.actions.log.cljs$core$IFn$_invoke$arity$variadic = (function (args){
return taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.actions",null,14,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.pr_str,args)], null);
}),null)),null,1546177596);
}));

(oc.web.actions.log.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.actions.log.cljs$lang$applyTo = (function (seq50442){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq50442));
}));

oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"default","default",-1987822328),(function (db,payload){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"warn","warn",-436710552),"oc.web.actions",null,17,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["No handler defined for",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.first(payload))], null);
}),null)),null,-666538872);

taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.actions",null,18,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Full event: ",cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([payload], 0))], null);
}),null)),null,-1606881019);

return db;
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"input","input",556931961),(function (db,p__50446){
var vec__50447 = p__50446;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__50447,(0),null);
var path = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__50447,(1),null);
var value = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__50447,(2),null);
return cljs.core.assoc_in(db,path,value);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"update","update",1045576396),(function (db,p__50452){
var vec__50453 = p__50452;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__50453,(0),null);
var path = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__50453,(1),null);
var value_fn = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__50453,(2),null);
if(cljs.core.fn_QMARK_(value_fn)){
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(db,path,value_fn);
} else {
return db;
}
}));

//# sourceMappingURL=oc.web.actions.js.map
