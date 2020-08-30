goog.provide('oc.web.ws.change_client');
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.change_client !== 'undefined') && (typeof oc.web.ws.change_client.last_ws_link !== 'undefined')){
} else {
oc.web.ws.change_client.last_ws_link = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.change_client !== 'undefined') && (typeof oc.web.ws.change_client.current_org !== 'undefined')){
} else {
oc.web.ws.change_client.current_org = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.change_client !== 'undefined') && (typeof oc.web.ws.change_client.current_uid !== 'undefined')){
} else {
oc.web.ws.change_client.current_uid = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.change_client !== 'undefined') && (typeof oc.web.ws.change_client.container_ids !== 'undefined')){
} else {
oc.web.ws.change_client.container_ids = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentVector.EMPTY);
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.change_client !== 'undefined') && (typeof oc.web.ws.change_client.channelsk !== 'undefined')){
} else {
oc.web.ws.change_client.channelsk = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.change_client !== 'undefined') && (typeof oc.web.ws.change_client.ch_chsk !== 'undefined')){
} else {
oc.web.ws.change_client.ch_chsk = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.change_client !== 'undefined') && (typeof oc.web.ws.change_client.ch_state !== 'undefined')){
} else {
oc.web.ws.change_client.ch_state = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.change_client !== 'undefined') && (typeof oc.web.ws.change_client.chsk_send_BANG_ !== 'undefined')){
} else {
oc.web.ws.change_client.chsk_send_BANG_ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.change_client !== 'undefined') && (typeof oc.web.ws.change_client.ch_pub !== 'undefined')){
} else {
oc.web.ws.change_client.ch_pub = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$0();
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.change_client !== 'undefined') && (typeof oc.web.ws.change_client.last_interval !== 'undefined')){
} else {
oc.web.ws.change_client.last_interval = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.change_client !== 'undefined') && (typeof oc.web.ws.change_client.publication !== 'undefined')){
} else {
oc.web.ws.change_client.publication = cljs.core.async.pub.cljs$core$IFn$_invoke$arity$2(oc.web.ws.change_client.ch_pub,new cljs.core.Keyword(null,"topic","topic",-1960480691));
}
oc.web.ws.change_client.send_BANG_ = (function oc$web$ws$change_client$send_BANG_(var_args){
var args__4742__auto__ = [];
var len__4736__auto___37429 = arguments.length;
var i__4737__auto___37430 = (0);
while(true){
if((i__4737__auto___37430 < len__4736__auto___37429)){
args__4742__auto__.push((arguments[i__4737__auto___37430]));

var G__37431 = (i__4737__auto___37430 + (1));
i__4737__auto___37430 = G__37431;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.ws.change_client.send_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.ws.change_client.send_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (chsk_send_BANG_,args){
return oc.web.ws.utils.send_BANG_("Change",chsk_send_BANG_,oc.web.ws.change_client.ch_state,args);
}));

(oc.web.ws.change_client.send_BANG_.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.ws.change_client.send_BANG_.cljs$lang$applyTo = (function (seq37239){
var G__37240 = cljs.core.first(seq37239);
var seq37239__$1 = cljs.core.next(seq37239);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__37240,seq37239__$1);
}));

/**
 * @param {...*} var_args
 */
oc.web.ws.change_client.container_watch = (function() { 
var oc$web$ws$change_client$container_watch__delegate = function (args__33705__auto__){
var ocr_37241 = cljs.core.vec(args__33705__auto__);
try{if(((cljs.core.vector_QMARK_(ocr_37241)) && ((cljs.core.count(ocr_37241) === 1)))){
try{var ocr_37241_0__37243 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_37241,(0));
if(typeof ocr_37241_0__37243 === 'string'){
var watch_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_37241,(0));
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,null,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Adding container/watch for:",watch_id], null);
}),null)),null,-1320415256);

var G__37248 = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [watch_id], null);
return (oc.web.ws.change_client.container_watch.cljs$core$IFn$_invoke$arity$1 ? oc.web.ws.change_client.container_watch.cljs$core$IFn$_invoke$arity$1(G__37248) : oc.web.ws.change_client.container_watch.call(null,G__37248));
} else {
throw cljs.core.match.backtrack;

}
}catch (e37246){if((e37246 instanceof Error)){
var e__32662__auto__ = e37246;
if((e__32662__auto__ === cljs.core.match.backtrack)){
try{var ocr_37241_0__37243 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_37241,(0));
if(cljs.core.sequential_QMARK_(ocr_37241_0__37243)){
var watch_ids = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_37241,(0));
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,null,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Adding container/watch for:",watch_ids], null);
}),null)),null,-197787527);

var current_set_37436 = cljs.core.set(cljs.core.deref(oc.web.ws.change_client.container_ids));
var new_set_37437 = cljs.core.set(watch_ids);
var union_set_37438 = clojure.set.union.cljs$core$IFn$_invoke$arity$2(current_set_37436,new_set_37437);
cljs.core.reset_BANG_(oc.web.ws.change_client.container_ids,cljs.core.vec(union_set_37438));

return (oc.web.ws.change_client.container_watch.cljs$core$IFn$_invoke$arity$0 ? oc.web.ws.change_client.container_watch.cljs$core$IFn$_invoke$arity$0() : oc.web.ws.change_client.container_watch.call(null));
} else {
throw cljs.core.match.backtrack;

}
}catch (e37247){if((e37247 instanceof Error)){
var e__32662__auto____$1 = e37247;
if((e__32662__auto____$1 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$1;
}
} else {
throw e37247;

}
}} else {
throw e__32662__auto__;
}
} else {
throw e37246;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e37244){if((e37244 instanceof Error)){
var e__32662__auto__ = e37244;
if((e__32662__auto__ === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_37241)) && ((cljs.core.count(ocr_37241) === 0)))){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,null,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Sending container/watch for:",cljs.core.deref(oc.web.ws.change_client.container_ids)], null);
}),null)),null,-334146617);

return oc.web.ws.change_client.send_BANG_.cljs$core$IFn$_invoke$arity$variadic(oc.web.ws.change_client.chsk_send_BANG_,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("container","watch","container/watch",1043726070),cljs.core.deref(oc.web.ws.change_client.container_ids)], null)], 0));
} else {
throw cljs.core.match.backtrack;

}
}catch (e37245){if((e37245 instanceof Error)){
var e__32662__auto____$1 = e37245;
if((e__32662__auto____$1 === cljs.core.match.backtrack)){
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ocr_37241)].join('')));
} else {
throw e__32662__auto____$1;
}
} else {
throw e37245;

}
}} else {
throw e__32662__auto__;
}
} else {
throw e37244;

}
}};
var oc$web$ws$change_client$container_watch = function (var_args){
var args__33705__auto__ = null;
if (arguments.length > 0) {
var G__37441__i = 0, G__37441__a = new Array(arguments.length -  0);
while (G__37441__i < G__37441__a.length) {G__37441__a[G__37441__i] = arguments[G__37441__i + 0]; ++G__37441__i;}
  args__33705__auto__ = new cljs.core.IndexedSeq(G__37441__a,0,null);
} 
return oc$web$ws$change_client$container_watch__delegate.call(this,args__33705__auto__);};
oc$web$ws$change_client$container_watch.cljs$lang$maxFixedArity = 0;
oc$web$ws$change_client$container_watch.cljs$lang$applyTo = (function (arglist__37442){
var args__33705__auto__ = cljs.core.seq(arglist__37442);
return oc$web$ws$change_client$container_watch__delegate(args__33705__auto__);
});
oc$web$ws$change_client$container_watch.cljs$core$IFn$_invoke$arity$variadic = oc$web$ws$change_client$container_watch__delegate;
return oc$web$ws$change_client$container_watch;
})()
;
oc.web.ws.change_client.container_seen = (function oc$web$ws$change_client$container_seen(org_id,container_id,seen_at){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,60,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Sending container/seen for org:",org_id,"container:",container_id,"seen at:",seen_at], null);
}),null)),null,-845891103);

return oc.web.ws.change_client.send_BANG_.cljs$core$IFn$_invoke$arity$variadic(oc.web.ws.change_client.chsk_send_BANG_,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("container","seen","container/seen",59753876),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"org-id","org-id",1485182668),org_id,new cljs.core.Keyword(null,"container-id","container-id",1274665684),container_id,new cljs.core.Keyword(null,"seen-at","seen-at",560301932),seen_at], null)], null)], 0));
});
oc.web.ws.change_client.item_seen = (function oc$web$ws$change_client$item_seen(publisher_id,org_id,container_id,item_id){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,64,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Sending item/seen for org:",org_id,"container:",container_id,"item:",item_id], null);
}),null)),null,1520909926);

return oc.web.ws.change_client.send_BANG_.cljs$core$IFn$_invoke$arity$variadic(oc.web.ws.change_client.chsk_send_BANG_,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("item","seen","item/seen",-524275154),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"publisher-id","publisher-id",-664531128),publisher_id,new cljs.core.Keyword(null,"org-id","org-id",1485182668),org_id,new cljs.core.Keyword(null,"container-id","container-id",1274665684),container_id,new cljs.core.Keyword(null,"item-id","item-id",-1804511607),item_id,new cljs.core.Keyword(null,"seen-at","seen-at",560301932),oc.lib.time.current_timestamp()], null)], null)], 0));
});
oc.web.ws.change_client.item_read = (function oc$web$ws$change_client$item_read(org_id,container_id,item_id,user_name,avatar_url){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,68,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Sending item/read for container:",container_id,"item:",item_id], null);
}),null)),null,1438870413);

return oc.web.ws.change_client.send_BANG_.cljs$core$IFn$_invoke$arity$variadic(oc.web.ws.change_client.chsk_send_BANG_,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("item","read","item/read",1141000506),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"org-id","org-id",1485182668),org_id,new cljs.core.Keyword(null,"container-id","container-id",1274665684),container_id,new cljs.core.Keyword(null,"item-id","item-id",-1804511607),item_id,new cljs.core.Keyword(null,"name","name",1843675177),user_name,new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103),avatar_url,new cljs.core.Keyword(null,"read-at","read-at",-936006929),oc.lib.time.current_timestamp()], null)], null)], 0));
});
oc.web.ws.change_client.who_read_count = (function oc$web$ws$change_client$who_read_count(item_ids){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,77,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Sending item/who-read-count for item-ids:",item_ids], null);
}),null)),null,689548528);

return oc.web.ws.change_client.send_BANG_.cljs$core$IFn$_invoke$arity$variadic(oc.web.ws.change_client.chsk_send_BANG_,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("item","who-read-count","item/who-read-count",24637173),item_ids], null)], 0));
});
oc.web.ws.change_client.who_read = (function oc$web$ws$change_client$who_read(item_ids){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,81,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Sending item/who-read for item-ids:",item_ids], null);
}),null)),null,-813706269);

return oc.web.ws.change_client.send_BANG_.cljs$core$IFn$_invoke$arity$variadic(oc.web.ws.change_client.chsk_send_BANG_,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("item","who-read","item/who-read",-1546365380),item_ids], null)], 0));
});
oc.web.ws.change_client.follow_list = (function oc$web$ws$change_client$follow_list(){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,87,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Sending follow-list for user-id:",cljs.core.deref(oc.web.ws.change_client.current_uid),"org-slug:",cljs.core.deref(oc.web.ws.change_client.current_org)], null);
}),null)),null,-1820526034);

return oc.web.ws.change_client.send_BANG_.cljs$core$IFn$_invoke$arity$variadic(oc.web.ws.change_client.chsk_send_BANG_,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("follow","list","follow/list",1912779396),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"user-id","user-id",-206822291),cljs.core.deref(oc.web.ws.change_client.current_uid),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051),cljs.core.deref(oc.web.ws.change_client.current_org)], null)], null)], 0));
});
oc.web.ws.change_client.followers_count = (function oc$web$ws$change_client$followers_count(){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,94,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Sending followers-count for org-slug:",cljs.core.deref(oc.web.ws.change_client.current_org)], null);
}),null)),null,1163695915);

return oc.web.ws.change_client.send_BANG_.cljs$core$IFn$_invoke$arity$variadic(oc.web.ws.change_client.chsk_send_BANG_,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("followers","count","followers/count",-1253725598),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"org-slug","org-slug",-726595051),cljs.core.deref(oc.web.ws.change_client.current_org)], null)], null)], 0));
});
oc.web.ws.change_client.publisher_watch = (function oc$web$ws$change_client$publisher_watch(){
return oc.web.ws.change_client.send_BANG_.cljs$core$IFn$_invoke$arity$variadic(oc.web.ws.change_client.chsk_send_BANG_,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("user","watch","user/watch",383542320),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"org-slug","org-slug",-726595051),cljs.core.deref(oc.web.ws.change_client.current_org),new cljs.core.Keyword(null,"user-ids","user-ids",-652076250),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.deref(oc.web.ws.change_client.current_uid)], null)], null)], null)], 0));
});
oc.web.ws.change_client.publishers_follow = (function oc$web$ws$change_client$publishers_follow(publisher_uuids){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,103,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Sending publishers/follow for user-id:",cljs.core.deref(oc.web.ws.change_client.current_uid),"org-slug:",cljs.core.deref(oc.web.ws.change_client.current_org),"with uuids:",publisher_uuids], null);
}),null)),null,-548999608);

return oc.web.ws.change_client.send_BANG_.cljs$core$IFn$_invoke$arity$variadic(oc.web.ws.change_client.chsk_send_BANG_,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("publishers","follow","publishers/follow",639463049),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"user-id","user-id",-206822291),cljs.core.deref(oc.web.ws.change_client.current_uid),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051),cljs.core.deref(oc.web.ws.change_client.current_org),new cljs.core.Keyword(null,"publisher-uuids","publisher-uuids",1855461704),cljs.core.vec(publisher_uuids)], null)], null)], 0));
});
oc.web.ws.change_client.publisher_follow = (function oc$web$ws$change_client$publisher_follow(publisher_uuid){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,110,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Sending publisher/follow for user-id:",cljs.core.deref(oc.web.ws.change_client.current_uid),"org-slug:",cljs.core.deref(oc.web.ws.change_client.current_org),"with uuid:",publisher_uuid], null);
}),null)),null,1793805756);

return oc.web.ws.change_client.send_BANG_.cljs$core$IFn$_invoke$arity$variadic(oc.web.ws.change_client.chsk_send_BANG_,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("publisher","follow","publisher/follow",973499022),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"user-id","user-id",-206822291),cljs.core.deref(oc.web.ws.change_client.current_uid),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051),cljs.core.deref(oc.web.ws.change_client.current_org),new cljs.core.Keyword(null,"publisher-uuid","publisher-uuid",-1907490269),publisher_uuid], null)], null)], 0));
});
oc.web.ws.change_client.publisher_unfollow = (function oc$web$ws$change_client$publisher_unfollow(publisher_uuid){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,117,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Sending publisher/unfollow for user-id:",cljs.core.deref(oc.web.ws.change_client.current_uid),"org-slug:",cljs.core.deref(oc.web.ws.change_client.current_org),"with uuid:",publisher_uuid], null);
}),null)),null,-1739953573);

return oc.web.ws.change_client.send_BANG_.cljs$core$IFn$_invoke$arity$variadic(oc.web.ws.change_client.chsk_send_BANG_,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("publisher","unfollow","publisher/unfollow",1978883361),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"user-id","user-id",-206822291),cljs.core.deref(oc.web.ws.change_client.current_uid),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051),cljs.core.deref(oc.web.ws.change_client.current_org),new cljs.core.Keyword(null,"publisher-uuid","publisher-uuid",-1907490269),publisher_uuid], null)], null)], 0));
});
oc.web.ws.change_client.boards_unfollow = (function oc$web$ws$change_client$boards_unfollow(board_uuids){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,126,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Sending boards/unfollow for user-id:",cljs.core.deref(oc.web.ws.change_client.current_uid),"org-slug:",cljs.core.deref(oc.web.ws.change_client.current_org),"with uuids:",board_uuids], null);
}),null)),null,605743553);

return oc.web.ws.change_client.send_BANG_.cljs$core$IFn$_invoke$arity$variadic(oc.web.ws.change_client.chsk_send_BANG_,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("boards","unfollow","boards/unfollow",389165266),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"user-id","user-id",-206822291),cljs.core.deref(oc.web.ws.change_client.current_uid),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051),cljs.core.deref(oc.web.ws.change_client.current_org),new cljs.core.Keyword(null,"board-uuids","board-uuids",515371926),cljs.core.vec(board_uuids)], null)], null)], 0));
});
oc.web.ws.change_client.board_follow = (function oc$web$ws$change_client$board_follow(board_uuid){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,133,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Sending board/follow for user-id:",cljs.core.deref(oc.web.ws.change_client.current_uid),"org-slug:",cljs.core.deref(oc.web.ws.change_client.current_org),"with uuid:",board_uuid], null);
}),null)),null,222835089);

return oc.web.ws.change_client.send_BANG_.cljs$core$IFn$_invoke$arity$variadic(oc.web.ws.change_client.chsk_send_BANG_,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("board","follow","board/follow",-915817092),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"user-id","user-id",-206822291),cljs.core.deref(oc.web.ws.change_client.current_uid),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051),cljs.core.deref(oc.web.ws.change_client.current_org),new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127),board_uuid], null)], null)], 0));
});
oc.web.ws.change_client.board_unfollow = (function oc$web$ws$change_client$board_unfollow(board_uuid){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,140,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Sending board/unfollow for user-id:",cljs.core.deref(oc.web.ws.change_client.current_uid),"org-slug:",cljs.core.deref(oc.web.ws.change_client.current_org),"with uuid:",board_uuid], null);
}),null)),null,642662137);

return oc.web.ws.change_client.send_BANG_.cljs$core$IFn$_invoke$arity$variadic(oc.web.ws.change_client.chsk_send_BANG_,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("board","unfollow","board/unfollow",1057370427),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"user-id","user-id",-206822291),cljs.core.deref(oc.web.ws.change_client.current_uid),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051),cljs.core.deref(oc.web.ws.change_client.current_org),new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127),board_uuid], null)], null)], 0));
});
oc.web.ws.change_client.subscribe = (function oc$web$ws$change_client$subscribe(topic,handler_fn){
var ws_cc_chan = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$0();
cljs.core.async.sub.cljs$core$IFn$_invoke$arity$3(oc.web.ws.change_client.publication,topic,ws_cc_chan);

var c__27167__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__27168__auto__ = (function (){var switch__27075__auto__ = (function (state_37257){
var state_val_37258 = (state_37257[(1)]);
if((state_val_37258 === (1))){
var state_37257__$1 = state_37257;
var statearr_37259_37448 = state_37257__$1;
(statearr_37259_37448[(2)] = null);

(statearr_37259_37448[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_37258 === (2))){
var state_37257__$1 = state_37257;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_37257__$1,(4),ws_cc_chan);
} else {
if((state_val_37258 === (3))){
var inst_37255 = (state_37257[(2)]);
var state_37257__$1 = state_37257;
return cljs.core.async.impl.ioc_helpers.return_chan(state_37257__$1,inst_37255);
} else {
if((state_val_37258 === (4))){
var inst_37251 = (state_37257[(2)]);
var inst_37252 = (handler_fn.cljs$core$IFn$_invoke$arity$1 ? handler_fn.cljs$core$IFn$_invoke$arity$1(inst_37251) : handler_fn.call(null,inst_37251));
var state_37257__$1 = (function (){var statearr_37260 = state_37257;
(statearr_37260[(7)] = inst_37252);

return statearr_37260;
})();
var statearr_37261_37450 = state_37257__$1;
(statearr_37261_37450[(2)] = null);

(statearr_37261_37450[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
});
return (function() {
var oc$web$ws$change_client$subscribe_$_state_machine__27076__auto__ = null;
var oc$web$ws$change_client$subscribe_$_state_machine__27076__auto____0 = (function (){
var statearr_37262 = [null,null,null,null,null,null,null,null];
(statearr_37262[(0)] = oc$web$ws$change_client$subscribe_$_state_machine__27076__auto__);

(statearr_37262[(1)] = (1));

return statearr_37262;
});
var oc$web$ws$change_client$subscribe_$_state_machine__27076__auto____1 = (function (state_37257){
while(true){
var ret_value__27077__auto__ = (function (){try{while(true){
var result__27078__auto__ = switch__27075__auto__(state_37257);
if(cljs.core.keyword_identical_QMARK_(result__27078__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__27078__auto__;
}
break;
}
}catch (e37263){var ex__27079__auto__ = e37263;
var statearr_37264_37462 = state_37257;
(statearr_37264_37462[(2)] = ex__27079__auto__);


if(cljs.core.seq((state_37257[(4)]))){
var statearr_37265_37464 = state_37257;
(statearr_37265_37464[(1)] = cljs.core.first((state_37257[(4)])));

} else {
throw ex__27079__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__27077__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__37468 = state_37257;
state_37257 = G__37468;
continue;
} else {
return ret_value__27077__auto__;
}
break;
}
});
oc$web$ws$change_client$subscribe_$_state_machine__27076__auto__ = function(state_37257){
switch(arguments.length){
case 0:
return oc$web$ws$change_client$subscribe_$_state_machine__27076__auto____0.call(this);
case 1:
return oc$web$ws$change_client$subscribe_$_state_machine__27076__auto____1.call(this,state_37257);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
oc$web$ws$change_client$subscribe_$_state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$0 = oc$web$ws$change_client$subscribe_$_state_machine__27076__auto____0;
oc$web$ws$change_client$subscribe_$_state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$1 = oc$web$ws$change_client$subscribe_$_state_machine__27076__auto____1;
return oc$web$ws$change_client$subscribe_$_state_machine__27076__auto__;
})()
})();
var state__27169__auto__ = (function (){var statearr_37266 = f__27168__auto__();
(statearr_37266[(6)] = c__27167__auto__);

return statearr_37266;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__27169__auto__);
}));

return c__27167__auto__;
});
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.change_client !== 'undefined') && (typeof oc.web.ws.change_client.event_handler !== 'undefined')){
} else {
/**
 * Multimethod to handle our internal events
 */
oc.web.ws.change_client.event_handler = (function (){var method_table__4619__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var prefer_table__4620__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var method_cache__4621__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var cached_hierarchy__4622__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var hierarchy__4623__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$3(cljs.core.PersistentArrayMap.EMPTY,new cljs.core.Keyword(null,"hierarchy","hierarchy",-1053470341),(function (){var fexpr__37267 = cljs.core.get_global_hierarchy;
return (fexpr__37267.cljs$core$IFn$_invoke$arity$0 ? fexpr__37267.cljs$core$IFn$_invoke$arity$0() : fexpr__37267.call(null));
})());
return (new cljs.core.MultiFn(cljs.core.symbol.cljs$core$IFn$_invoke$arity$2("oc.web.ws.change-client","event-handler"),(function() { 
var G__37476__delegate = function (event,_){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,159,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["event-handler",event], null);
}),null)),null,416756065);

return event;
};
var G__37476 = function (event,var_args){
var _ = null;
if (arguments.length > 1) {
var G__37477__i = 0, G__37477__a = new Array(arguments.length -  1);
while (G__37477__i < G__37477__a.length) {G__37477__a[G__37477__i] = arguments[G__37477__i + 1]; ++G__37477__i;}
  _ = new cljs.core.IndexedSeq(G__37477__a,0,null);
} 
return G__37476__delegate.call(this,event,_);};
G__37476.cljs$lang$maxFixedArity = 1;
G__37476.cljs$lang$applyTo = (function (arglist__37478){
var event = cljs.core.first(arglist__37478);
var _ = cljs.core.rest(arglist__37478);
return G__37476__delegate(event,_);
});
G__37476.cljs$core$IFn$_invoke$arity$variadic = G__37476__delegate;
return G__37476;
})()
,new cljs.core.Keyword(null,"default","default",-1987822328),hierarchy__4623__auto__,method_table__4619__auto__,prefer_table__4620__auto__,method_cache__4621__auto__,cached_hierarchy__4622__auto__));
})();
}
oc.web.ws.change_client.event_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"default","default",-1987822328),(function() { 
var G__37479__delegate = function (_,r){
return taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.ws.change-client",null,164,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["No event handler defined for",_], null);
}),null)),null,-1013311075);
};
var G__37479 = function (_,var_args){
var r = null;
if (arguments.length > 1) {
var G__37483__i = 0, G__37483__a = new Array(arguments.length -  1);
while (G__37483__i < G__37483__a.length) {G__37483__a[G__37483__i] = arguments[G__37483__i + 1]; ++G__37483__i;}
  r = new cljs.core.IndexedSeq(G__37483__a,0,null);
} 
return G__37479__delegate.call(this,_,r);};
G__37479.cljs$lang$maxFixedArity = 1;
G__37479.cljs$lang$applyTo = (function (arglist__37484){
var _ = cljs.core.first(arglist__37484);
var r = cljs.core.rest(arglist__37484);
return G__37479__delegate(_,r);
});
G__37479.cljs$core$IFn$_invoke$arity$variadic = G__37479__delegate;
return G__37479;
})()
);
oc.web.ws.change_client.event_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("chsk","ws-ping","chsk/ws-ping",191675304),(function() { 
var G__37485__delegate = function (_,r){
var c__27167__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__27168__auto__ = (function (){var switch__27075__auto__ = (function (state_37274){
var state_val_37275 = (state_37274[(1)]);
if((state_val_37275 === (1))){
var inst_37268 = [new cljs.core.Keyword(null,"topic","topic",-1960480691)];
var inst_37269 = [new cljs.core.Keyword("chsk","ws-ping","chsk/ws-ping",191675304)];
var inst_37270 = cljs.core.PersistentHashMap.fromArrays(inst_37268,inst_37269);
var state_37274__$1 = state_37274;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_37274__$1,(2),oc.web.ws.change_client.ch_pub,inst_37270);
} else {
if((state_val_37275 === (2))){
var inst_37272 = (state_37274[(2)]);
var state_37274__$1 = state_37274;
return cljs.core.async.impl.ioc_helpers.return_chan(state_37274__$1,inst_37272);
} else {
return null;
}
}
});
return (function() {
var oc$web$ws$change_client$state_machine__27076__auto__ = null;
var oc$web$ws$change_client$state_machine__27076__auto____0 = (function (){
var statearr_37276 = [null,null,null,null,null,null,null];
(statearr_37276[(0)] = oc$web$ws$change_client$state_machine__27076__auto__);

(statearr_37276[(1)] = (1));

return statearr_37276;
});
var oc$web$ws$change_client$state_machine__27076__auto____1 = (function (state_37274){
while(true){
var ret_value__27077__auto__ = (function (){try{while(true){
var result__27078__auto__ = switch__27075__auto__(state_37274);
if(cljs.core.keyword_identical_QMARK_(result__27078__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__27078__auto__;
}
break;
}
}catch (e37277){var ex__27079__auto__ = e37277;
var statearr_37278_37487 = state_37274;
(statearr_37278_37487[(2)] = ex__27079__auto__);


if(cljs.core.seq((state_37274[(4)]))){
var statearr_37279_37489 = state_37274;
(statearr_37279_37489[(1)] = cljs.core.first((state_37274[(4)])));

} else {
throw ex__27079__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__27077__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__37491 = state_37274;
state_37274 = G__37491;
continue;
} else {
return ret_value__27077__auto__;
}
break;
}
});
oc$web$ws$change_client$state_machine__27076__auto__ = function(state_37274){
switch(arguments.length){
case 0:
return oc$web$ws$change_client$state_machine__27076__auto____0.call(this);
case 1:
return oc$web$ws$change_client$state_machine__27076__auto____1.call(this,state_37274);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
oc$web$ws$change_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$0 = oc$web$ws$change_client$state_machine__27076__auto____0;
oc$web$ws$change_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$1 = oc$web$ws$change_client$state_machine__27076__auto____1;
return oc$web$ws$change_client$state_machine__27076__auto__;
})()
})();
var state__27169__auto__ = (function (){var statearr_37280 = f__27168__auto__();
(statearr_37280[(6)] = c__27167__auto__);

return statearr_37280;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__27169__auto__);
}));

return c__27167__auto__;
};
var G__37485 = function (_,var_args){
var r = null;
if (arguments.length > 1) {
var G__37493__i = 0, G__37493__a = new Array(arguments.length -  1);
while (G__37493__i < G__37493__a.length) {G__37493__a[G__37493__i] = arguments[G__37493__i + 1]; ++G__37493__i;}
  r = new cljs.core.IndexedSeq(G__37493__a,0,null);
} 
return G__37485__delegate.call(this,_,r);};
G__37485.cljs$lang$maxFixedArity = 1;
G__37485.cljs$lang$applyTo = (function (arglist__37495){
var _ = cljs.core.first(arglist__37495);
var r = cljs.core.rest(arglist__37495);
return G__37485__delegate(_,r);
});
G__37485.cljs$core$IFn$_invoke$arity$variadic = G__37485__delegate;
return G__37485;
})()
);
oc.web.ws.change_client.event_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("container","status","container/status",1617877110),(function (_,body){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,172,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Status event :container/status",body], null);
}),null)),null,1692337019);

var c__27167__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__27168__auto__ = (function (){var switch__27075__auto__ = (function (state_37287){
var state_val_37288 = (state_37287[(1)]);
if((state_val_37288 === (1))){
var inst_37281 = [new cljs.core.Keyword(null,"topic","topic",-1960480691),new cljs.core.Keyword(null,"data","data",-232669377)];
var inst_37282 = [new cljs.core.Keyword("container","status","container/status",1617877110),body];
var inst_37283 = cljs.core.PersistentHashMap.fromArrays(inst_37281,inst_37282);
var state_37287__$1 = state_37287;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_37287__$1,(2),oc.web.ws.change_client.ch_pub,inst_37283);
} else {
if((state_val_37288 === (2))){
var inst_37285 = (state_37287[(2)]);
var state_37287__$1 = state_37287;
return cljs.core.async.impl.ioc_helpers.return_chan(state_37287__$1,inst_37285);
} else {
return null;
}
}
});
return (function() {
var oc$web$ws$change_client$state_machine__27076__auto__ = null;
var oc$web$ws$change_client$state_machine__27076__auto____0 = (function (){
var statearr_37289 = [null,null,null,null,null,null,null];
(statearr_37289[(0)] = oc$web$ws$change_client$state_machine__27076__auto__);

(statearr_37289[(1)] = (1));

return statearr_37289;
});
var oc$web$ws$change_client$state_machine__27076__auto____1 = (function (state_37287){
while(true){
var ret_value__27077__auto__ = (function (){try{while(true){
var result__27078__auto__ = switch__27075__auto__(state_37287);
if(cljs.core.keyword_identical_QMARK_(result__27078__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__27078__auto__;
}
break;
}
}catch (e37290){var ex__27079__auto__ = e37290;
var statearr_37291_37502 = state_37287;
(statearr_37291_37502[(2)] = ex__27079__auto__);


if(cljs.core.seq((state_37287[(4)]))){
var statearr_37292_37503 = state_37287;
(statearr_37292_37503[(1)] = cljs.core.first((state_37287[(4)])));

} else {
throw ex__27079__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__27077__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__37505 = state_37287;
state_37287 = G__37505;
continue;
} else {
return ret_value__27077__auto__;
}
break;
}
});
oc$web$ws$change_client$state_machine__27076__auto__ = function(state_37287){
switch(arguments.length){
case 0:
return oc$web$ws$change_client$state_machine__27076__auto____0.call(this);
case 1:
return oc$web$ws$change_client$state_machine__27076__auto____1.call(this,state_37287);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
oc$web$ws$change_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$0 = oc$web$ws$change_client$state_machine__27076__auto____0;
oc$web$ws$change_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$1 = oc$web$ws$change_client$state_machine__27076__auto____1;
return oc$web$ws$change_client$state_machine__27076__auto__;
})()
})();
var state__27169__auto__ = (function (){var statearr_37293 = f__27168__auto__();
(statearr_37293[(6)] = c__27167__auto__);

return statearr_37293;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__27169__auto__);
}));

return c__27167__auto__;
}));
oc.web.ws.change_client.event_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("org","change","org/change",-1163165546),(function (_,body){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,177,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Change event :org/change",body], null);
}),null)),null,93987710);

var c__27167__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__27168__auto__ = (function (){var switch__27075__auto__ = (function (state_37300){
var state_val_37301 = (state_37300[(1)]);
if((state_val_37301 === (1))){
var inst_37294 = [new cljs.core.Keyword(null,"topic","topic",-1960480691),new cljs.core.Keyword(null,"data","data",-232669377)];
var inst_37295 = [new cljs.core.Keyword("org","change","org/change",-1163165546),body];
var inst_37296 = cljs.core.PersistentHashMap.fromArrays(inst_37294,inst_37295);
var state_37300__$1 = state_37300;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_37300__$1,(2),oc.web.ws.change_client.ch_pub,inst_37296);
} else {
if((state_val_37301 === (2))){
var inst_37298 = (state_37300[(2)]);
var state_37300__$1 = state_37300;
return cljs.core.async.impl.ioc_helpers.return_chan(state_37300__$1,inst_37298);
} else {
return null;
}
}
});
return (function() {
var oc$web$ws$change_client$state_machine__27076__auto__ = null;
var oc$web$ws$change_client$state_machine__27076__auto____0 = (function (){
var statearr_37302 = [null,null,null,null,null,null,null];
(statearr_37302[(0)] = oc$web$ws$change_client$state_machine__27076__auto__);

(statearr_37302[(1)] = (1));

return statearr_37302;
});
var oc$web$ws$change_client$state_machine__27076__auto____1 = (function (state_37300){
while(true){
var ret_value__27077__auto__ = (function (){try{while(true){
var result__27078__auto__ = switch__27075__auto__(state_37300);
if(cljs.core.keyword_identical_QMARK_(result__27078__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__27078__auto__;
}
break;
}
}catch (e37303){var ex__27079__auto__ = e37303;
var statearr_37304_37506 = state_37300;
(statearr_37304_37506[(2)] = ex__27079__auto__);


if(cljs.core.seq((state_37300[(4)]))){
var statearr_37305_37507 = state_37300;
(statearr_37305_37507[(1)] = cljs.core.first((state_37300[(4)])));

} else {
throw ex__27079__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__27077__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__37508 = state_37300;
state_37300 = G__37508;
continue;
} else {
return ret_value__27077__auto__;
}
break;
}
});
oc$web$ws$change_client$state_machine__27076__auto__ = function(state_37300){
switch(arguments.length){
case 0:
return oc$web$ws$change_client$state_machine__27076__auto____0.call(this);
case 1:
return oc$web$ws$change_client$state_machine__27076__auto____1.call(this,state_37300);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
oc$web$ws$change_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$0 = oc$web$ws$change_client$state_machine__27076__auto____0;
oc$web$ws$change_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$1 = oc$web$ws$change_client$state_machine__27076__auto____1;
return oc$web$ws$change_client$state_machine__27076__auto__;
})()
})();
var state__27169__auto__ = (function (){var statearr_37306 = f__27168__auto__();
(statearr_37306[(6)] = c__27167__auto__);

return statearr_37306;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__27169__auto__);
}));

return c__27167__auto__;
}));
oc.web.ws.change_client.event_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("container","change","container/change",-1507058407),(function (_,body){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,182,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Change event :container/change",body], null);
}),null)),null,116477782);

var c__27167__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__27168__auto__ = (function (){var switch__27075__auto__ = (function (state_37313){
var state_val_37314 = (state_37313[(1)]);
if((state_val_37314 === (1))){
var inst_37307 = [new cljs.core.Keyword(null,"topic","topic",-1960480691),new cljs.core.Keyword(null,"data","data",-232669377)];
var inst_37308 = [new cljs.core.Keyword("container","change","container/change",-1507058407),body];
var inst_37309 = cljs.core.PersistentHashMap.fromArrays(inst_37307,inst_37308);
var state_37313__$1 = state_37313;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_37313__$1,(2),oc.web.ws.change_client.ch_pub,inst_37309);
} else {
if((state_val_37314 === (2))){
var inst_37311 = (state_37313[(2)]);
var state_37313__$1 = state_37313;
return cljs.core.async.impl.ioc_helpers.return_chan(state_37313__$1,inst_37311);
} else {
return null;
}
}
});
return (function() {
var oc$web$ws$change_client$state_machine__27076__auto__ = null;
var oc$web$ws$change_client$state_machine__27076__auto____0 = (function (){
var statearr_37315 = [null,null,null,null,null,null,null];
(statearr_37315[(0)] = oc$web$ws$change_client$state_machine__27076__auto__);

(statearr_37315[(1)] = (1));

return statearr_37315;
});
var oc$web$ws$change_client$state_machine__27076__auto____1 = (function (state_37313){
while(true){
var ret_value__27077__auto__ = (function (){try{while(true){
var result__27078__auto__ = switch__27075__auto__(state_37313);
if(cljs.core.keyword_identical_QMARK_(result__27078__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__27078__auto__;
}
break;
}
}catch (e37316){var ex__27079__auto__ = e37316;
var statearr_37317_37545 = state_37313;
(statearr_37317_37545[(2)] = ex__27079__auto__);


if(cljs.core.seq((state_37313[(4)]))){
var statearr_37318_37549 = state_37313;
(statearr_37318_37549[(1)] = cljs.core.first((state_37313[(4)])));

} else {
throw ex__27079__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__27077__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__37568 = state_37313;
state_37313 = G__37568;
continue;
} else {
return ret_value__27077__auto__;
}
break;
}
});
oc$web$ws$change_client$state_machine__27076__auto__ = function(state_37313){
switch(arguments.length){
case 0:
return oc$web$ws$change_client$state_machine__27076__auto____0.call(this);
case 1:
return oc$web$ws$change_client$state_machine__27076__auto____1.call(this,state_37313);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
oc$web$ws$change_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$0 = oc$web$ws$change_client$state_machine__27076__auto____0;
oc$web$ws$change_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$1 = oc$web$ws$change_client$state_machine__27076__auto____1;
return oc$web$ws$change_client$state_machine__27076__auto__;
})()
})();
var state__27169__auto__ = (function (){var statearr_37319 = f__27168__auto__();
(statearr_37319[(6)] = c__27167__auto__);

return statearr_37319;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__27169__auto__);
}));

return c__27167__auto__;
}));
oc.web.ws.change_client.event_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("item","change","item/change",-1168388949),(function (_,body){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,187,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Change event :item/change",body], null);
}),null)),null,1743005100);

var c__27167__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__27168__auto__ = (function (){var switch__27075__auto__ = (function (state_37326){
var state_val_37327 = (state_37326[(1)]);
if((state_val_37327 === (1))){
var inst_37320 = [new cljs.core.Keyword(null,"topic","topic",-1960480691),new cljs.core.Keyword(null,"data","data",-232669377)];
var inst_37321 = [new cljs.core.Keyword("item","change","item/change",-1168388949),body];
var inst_37322 = cljs.core.PersistentHashMap.fromArrays(inst_37320,inst_37321);
var state_37326__$1 = state_37326;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_37326__$1,(2),oc.web.ws.change_client.ch_pub,inst_37322);
} else {
if((state_val_37327 === (2))){
var inst_37324 = (state_37326[(2)]);
var state_37326__$1 = state_37326;
return cljs.core.async.impl.ioc_helpers.return_chan(state_37326__$1,inst_37324);
} else {
return null;
}
}
});
return (function() {
var oc$web$ws$change_client$state_machine__27076__auto__ = null;
var oc$web$ws$change_client$state_machine__27076__auto____0 = (function (){
var statearr_37328 = [null,null,null,null,null,null,null];
(statearr_37328[(0)] = oc$web$ws$change_client$state_machine__27076__auto__);

(statearr_37328[(1)] = (1));

return statearr_37328;
});
var oc$web$ws$change_client$state_machine__27076__auto____1 = (function (state_37326){
while(true){
var ret_value__27077__auto__ = (function (){try{while(true){
var result__27078__auto__ = switch__27075__auto__(state_37326);
if(cljs.core.keyword_identical_QMARK_(result__27078__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__27078__auto__;
}
break;
}
}catch (e37329){var ex__27079__auto__ = e37329;
var statearr_37330_37607 = state_37326;
(statearr_37330_37607[(2)] = ex__27079__auto__);


if(cljs.core.seq((state_37326[(4)]))){
var statearr_37331_37608 = state_37326;
(statearr_37331_37608[(1)] = cljs.core.first((state_37326[(4)])));

} else {
throw ex__27079__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__27077__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__37619 = state_37326;
state_37326 = G__37619;
continue;
} else {
return ret_value__27077__auto__;
}
break;
}
});
oc$web$ws$change_client$state_machine__27076__auto__ = function(state_37326){
switch(arguments.length){
case 0:
return oc$web$ws$change_client$state_machine__27076__auto____0.call(this);
case 1:
return oc$web$ws$change_client$state_machine__27076__auto____1.call(this,state_37326);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
oc$web$ws$change_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$0 = oc$web$ws$change_client$state_machine__27076__auto____0;
oc$web$ws$change_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$1 = oc$web$ws$change_client$state_machine__27076__auto____1;
return oc$web$ws$change_client$state_machine__27076__auto__;
})()
})();
var state__27169__auto__ = (function (){var statearr_37332 = f__27168__auto__();
(statearr_37332[(6)] = c__27167__auto__);

return statearr_37332;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__27169__auto__);
}));

return c__27167__auto__;
}));
oc.web.ws.change_client.event_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("item","counts","item/counts",237352049),(function (_,body){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,192,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Change event :item/counts",body], null);
}),null)),null,-1592913978);

var c__27167__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__27168__auto__ = (function (){var switch__27075__auto__ = (function (state_37339){
var state_val_37340 = (state_37339[(1)]);
if((state_val_37340 === (1))){
var inst_37333 = [new cljs.core.Keyword(null,"topic","topic",-1960480691),new cljs.core.Keyword(null,"data","data",-232669377)];
var inst_37334 = [new cljs.core.Keyword("item","counts","item/counts",237352049),body];
var inst_37335 = cljs.core.PersistentHashMap.fromArrays(inst_37333,inst_37334);
var state_37339__$1 = state_37339;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_37339__$1,(2),oc.web.ws.change_client.ch_pub,inst_37335);
} else {
if((state_val_37340 === (2))){
var inst_37337 = (state_37339[(2)]);
var state_37339__$1 = state_37339;
return cljs.core.async.impl.ioc_helpers.return_chan(state_37339__$1,inst_37337);
} else {
return null;
}
}
});
return (function() {
var oc$web$ws$change_client$state_machine__27076__auto__ = null;
var oc$web$ws$change_client$state_machine__27076__auto____0 = (function (){
var statearr_37341 = [null,null,null,null,null,null,null];
(statearr_37341[(0)] = oc$web$ws$change_client$state_machine__27076__auto__);

(statearr_37341[(1)] = (1));

return statearr_37341;
});
var oc$web$ws$change_client$state_machine__27076__auto____1 = (function (state_37339){
while(true){
var ret_value__27077__auto__ = (function (){try{while(true){
var result__27078__auto__ = switch__27075__auto__(state_37339);
if(cljs.core.keyword_identical_QMARK_(result__27078__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__27078__auto__;
}
break;
}
}catch (e37342){var ex__27079__auto__ = e37342;
var statearr_37343_37624 = state_37339;
(statearr_37343_37624[(2)] = ex__27079__auto__);


if(cljs.core.seq((state_37339[(4)]))){
var statearr_37344_37626 = state_37339;
(statearr_37344_37626[(1)] = cljs.core.first((state_37339[(4)])));

} else {
throw ex__27079__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__27077__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__37628 = state_37339;
state_37339 = G__37628;
continue;
} else {
return ret_value__27077__auto__;
}
break;
}
});
oc$web$ws$change_client$state_machine__27076__auto__ = function(state_37339){
switch(arguments.length){
case 0:
return oc$web$ws$change_client$state_machine__27076__auto____0.call(this);
case 1:
return oc$web$ws$change_client$state_machine__27076__auto____1.call(this,state_37339);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
oc$web$ws$change_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$0 = oc$web$ws$change_client$state_machine__27076__auto____0;
oc$web$ws$change_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$1 = oc$web$ws$change_client$state_machine__27076__auto____1;
return oc$web$ws$change_client$state_machine__27076__auto__;
})()
})();
var state__27169__auto__ = (function (){var statearr_37345 = f__27168__auto__();
(statearr_37345[(6)] = c__27167__auto__);

return statearr_37345;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__27169__auto__);
}));

return c__27167__auto__;
}));
oc.web.ws.change_client.event_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("item","status","item/status",-2086762296),(function (_,body){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,197,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Change event :item/status",body], null);
}),null)),null,1165932080);

var c__27167__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__27168__auto__ = (function (){var switch__27075__auto__ = (function (state_37352){
var state_val_37353 = (state_37352[(1)]);
if((state_val_37353 === (1))){
var inst_37346 = [new cljs.core.Keyword(null,"topic","topic",-1960480691),new cljs.core.Keyword(null,"data","data",-232669377)];
var inst_37347 = [new cljs.core.Keyword("item","status","item/status",-2086762296),body];
var inst_37348 = cljs.core.PersistentHashMap.fromArrays(inst_37346,inst_37347);
var state_37352__$1 = state_37352;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_37352__$1,(2),oc.web.ws.change_client.ch_pub,inst_37348);
} else {
if((state_val_37353 === (2))){
var inst_37350 = (state_37352[(2)]);
var state_37352__$1 = state_37352;
return cljs.core.async.impl.ioc_helpers.return_chan(state_37352__$1,inst_37350);
} else {
return null;
}
}
});
return (function() {
var oc$web$ws$change_client$state_machine__27076__auto__ = null;
var oc$web$ws$change_client$state_machine__27076__auto____0 = (function (){
var statearr_37354 = [null,null,null,null,null,null,null];
(statearr_37354[(0)] = oc$web$ws$change_client$state_machine__27076__auto__);

(statearr_37354[(1)] = (1));

return statearr_37354;
});
var oc$web$ws$change_client$state_machine__27076__auto____1 = (function (state_37352){
while(true){
var ret_value__27077__auto__ = (function (){try{while(true){
var result__27078__auto__ = switch__27075__auto__(state_37352);
if(cljs.core.keyword_identical_QMARK_(result__27078__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__27078__auto__;
}
break;
}
}catch (e37355){var ex__27079__auto__ = e37355;
var statearr_37356_37636 = state_37352;
(statearr_37356_37636[(2)] = ex__27079__auto__);


if(cljs.core.seq((state_37352[(4)]))){
var statearr_37357_37638 = state_37352;
(statearr_37357_37638[(1)] = cljs.core.first((state_37352[(4)])));

} else {
throw ex__27079__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__27077__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__37639 = state_37352;
state_37352 = G__37639;
continue;
} else {
return ret_value__27077__auto__;
}
break;
}
});
oc$web$ws$change_client$state_machine__27076__auto__ = function(state_37352){
switch(arguments.length){
case 0:
return oc$web$ws$change_client$state_machine__27076__auto____0.call(this);
case 1:
return oc$web$ws$change_client$state_machine__27076__auto____1.call(this,state_37352);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
oc$web$ws$change_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$0 = oc$web$ws$change_client$state_machine__27076__auto____0;
oc$web$ws$change_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$1 = oc$web$ws$change_client$state_machine__27076__auto____1;
return oc$web$ws$change_client$state_machine__27076__auto__;
})()
})();
var state__27169__auto__ = (function (){var statearr_37358 = f__27168__auto__();
(statearr_37358[(6)] = c__27167__auto__);

return statearr_37358;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__27169__auto__);
}));

return c__27167__auto__;
}));
oc.web.ws.change_client.event_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("entry","inbox-action","entry/inbox-action",1514943739),(function (_,body){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,202,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Change event :entry/inbox-action",body], null);
}),null)),null,-1469473093);

var c__27167__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__27168__auto__ = (function (){var switch__27075__auto__ = (function (state_37365){
var state_val_37366 = (state_37365[(1)]);
if((state_val_37366 === (1))){
var inst_37359 = [new cljs.core.Keyword(null,"topic","topic",-1960480691),new cljs.core.Keyword(null,"data","data",-232669377)];
var inst_37360 = [new cljs.core.Keyword("entry","inbox-action","entry/inbox-action",1514943739),body];
var inst_37361 = cljs.core.PersistentHashMap.fromArrays(inst_37359,inst_37360);
var state_37365__$1 = state_37365;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_37365__$1,(2),oc.web.ws.change_client.ch_pub,inst_37361);
} else {
if((state_val_37366 === (2))){
var inst_37363 = (state_37365[(2)]);
var state_37365__$1 = state_37365;
return cljs.core.async.impl.ioc_helpers.return_chan(state_37365__$1,inst_37363);
} else {
return null;
}
}
});
return (function() {
var oc$web$ws$change_client$state_machine__27076__auto__ = null;
var oc$web$ws$change_client$state_machine__27076__auto____0 = (function (){
var statearr_37367 = [null,null,null,null,null,null,null];
(statearr_37367[(0)] = oc$web$ws$change_client$state_machine__27076__auto__);

(statearr_37367[(1)] = (1));

return statearr_37367;
});
var oc$web$ws$change_client$state_machine__27076__auto____1 = (function (state_37365){
while(true){
var ret_value__27077__auto__ = (function (){try{while(true){
var result__27078__auto__ = switch__27075__auto__(state_37365);
if(cljs.core.keyword_identical_QMARK_(result__27078__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__27078__auto__;
}
break;
}
}catch (e37368){var ex__27079__auto__ = e37368;
var statearr_37369_37649 = state_37365;
(statearr_37369_37649[(2)] = ex__27079__auto__);


if(cljs.core.seq((state_37365[(4)]))){
var statearr_37370_37651 = state_37365;
(statearr_37370_37651[(1)] = cljs.core.first((state_37365[(4)])));

} else {
throw ex__27079__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__27077__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__37652 = state_37365;
state_37365 = G__37652;
continue;
} else {
return ret_value__27077__auto__;
}
break;
}
});
oc$web$ws$change_client$state_machine__27076__auto__ = function(state_37365){
switch(arguments.length){
case 0:
return oc$web$ws$change_client$state_machine__27076__auto____0.call(this);
case 1:
return oc$web$ws$change_client$state_machine__27076__auto____1.call(this,state_37365);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
oc$web$ws$change_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$0 = oc$web$ws$change_client$state_machine__27076__auto____0;
oc$web$ws$change_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$1 = oc$web$ws$change_client$state_machine__27076__auto____1;
return oc$web$ws$change_client$state_machine__27076__auto__;
})()
})();
var state__27169__auto__ = (function (){var statearr_37371 = f__27168__auto__();
(statearr_37371[(6)] = c__27167__auto__);

return statearr_37371;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__27169__auto__);
}));

return c__27167__auto__;
}));
oc.web.ws.change_client.event_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("follow","list","follow/list",1912779396),(function (_,body){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,209,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Change event :follow/list",body], null);
}),null)),null,-402582545);

var c__27167__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__27168__auto__ = (function (){var switch__27075__auto__ = (function (state_37378){
var state_val_37379 = (state_37378[(1)]);
if((state_val_37379 === (1))){
var inst_37372 = [new cljs.core.Keyword(null,"topic","topic",-1960480691),new cljs.core.Keyword(null,"data","data",-232669377)];
var inst_37373 = [new cljs.core.Keyword("follow","list","follow/list",1912779396),body];
var inst_37374 = cljs.core.PersistentHashMap.fromArrays(inst_37372,inst_37373);
var state_37378__$1 = state_37378;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_37378__$1,(2),oc.web.ws.change_client.ch_pub,inst_37374);
} else {
if((state_val_37379 === (2))){
var inst_37376 = (state_37378[(2)]);
var state_37378__$1 = state_37378;
return cljs.core.async.impl.ioc_helpers.return_chan(state_37378__$1,inst_37376);
} else {
return null;
}
}
});
return (function() {
var oc$web$ws$change_client$state_machine__27076__auto__ = null;
var oc$web$ws$change_client$state_machine__27076__auto____0 = (function (){
var statearr_37380 = [null,null,null,null,null,null,null];
(statearr_37380[(0)] = oc$web$ws$change_client$state_machine__27076__auto__);

(statearr_37380[(1)] = (1));

return statearr_37380;
});
var oc$web$ws$change_client$state_machine__27076__auto____1 = (function (state_37378){
while(true){
var ret_value__27077__auto__ = (function (){try{while(true){
var result__27078__auto__ = switch__27075__auto__(state_37378);
if(cljs.core.keyword_identical_QMARK_(result__27078__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__27078__auto__;
}
break;
}
}catch (e37381){var ex__27079__auto__ = e37381;
var statearr_37382_37659 = state_37378;
(statearr_37382_37659[(2)] = ex__27079__auto__);


if(cljs.core.seq((state_37378[(4)]))){
var statearr_37383_37660 = state_37378;
(statearr_37383_37660[(1)] = cljs.core.first((state_37378[(4)])));

} else {
throw ex__27079__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__27077__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__37661 = state_37378;
state_37378 = G__37661;
continue;
} else {
return ret_value__27077__auto__;
}
break;
}
});
oc$web$ws$change_client$state_machine__27076__auto__ = function(state_37378){
switch(arguments.length){
case 0:
return oc$web$ws$change_client$state_machine__27076__auto____0.call(this);
case 1:
return oc$web$ws$change_client$state_machine__27076__auto____1.call(this,state_37378);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
oc$web$ws$change_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$0 = oc$web$ws$change_client$state_machine__27076__auto____0;
oc$web$ws$change_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$1 = oc$web$ws$change_client$state_machine__27076__auto____1;
return oc$web$ws$change_client$state_machine__27076__auto__;
})()
})();
var state__27169__auto__ = (function (){var statearr_37384 = f__27168__auto__();
(statearr_37384[(6)] = c__27167__auto__);

return statearr_37384;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__27169__auto__);
}));

return c__27167__auto__;
}));
oc.web.ws.change_client.event_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("followers","count","followers/count",-1253725598),(function (_,body){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,214,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Change event :follow/list",body], null);
}),null)),null,1201275712);

var c__27167__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__27168__auto__ = (function (){var switch__27075__auto__ = (function (state_37391){
var state_val_37392 = (state_37391[(1)]);
if((state_val_37392 === (1))){
var inst_37385 = [new cljs.core.Keyword(null,"topic","topic",-1960480691),new cljs.core.Keyword(null,"data","data",-232669377)];
var inst_37386 = [new cljs.core.Keyword("followers","count","followers/count",-1253725598),body];
var inst_37387 = cljs.core.PersistentHashMap.fromArrays(inst_37385,inst_37386);
var state_37391__$1 = state_37391;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_37391__$1,(2),oc.web.ws.change_client.ch_pub,inst_37387);
} else {
if((state_val_37392 === (2))){
var inst_37389 = (state_37391[(2)]);
var state_37391__$1 = state_37391;
return cljs.core.async.impl.ioc_helpers.return_chan(state_37391__$1,inst_37389);
} else {
return null;
}
}
});
return (function() {
var oc$web$ws$change_client$state_machine__27076__auto__ = null;
var oc$web$ws$change_client$state_machine__27076__auto____0 = (function (){
var statearr_37393 = [null,null,null,null,null,null,null];
(statearr_37393[(0)] = oc$web$ws$change_client$state_machine__27076__auto__);

(statearr_37393[(1)] = (1));

return statearr_37393;
});
var oc$web$ws$change_client$state_machine__27076__auto____1 = (function (state_37391){
while(true){
var ret_value__27077__auto__ = (function (){try{while(true){
var result__27078__auto__ = switch__27075__auto__(state_37391);
if(cljs.core.keyword_identical_QMARK_(result__27078__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__27078__auto__;
}
break;
}
}catch (e37394){var ex__27079__auto__ = e37394;
var statearr_37395_37664 = state_37391;
(statearr_37395_37664[(2)] = ex__27079__auto__);


if(cljs.core.seq((state_37391[(4)]))){
var statearr_37396_37665 = state_37391;
(statearr_37396_37665[(1)] = cljs.core.first((state_37391[(4)])));

} else {
throw ex__27079__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__27077__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__37667 = state_37391;
state_37391 = G__37667;
continue;
} else {
return ret_value__27077__auto__;
}
break;
}
});
oc$web$ws$change_client$state_machine__27076__auto__ = function(state_37391){
switch(arguments.length){
case 0:
return oc$web$ws$change_client$state_machine__27076__auto____0.call(this);
case 1:
return oc$web$ws$change_client$state_machine__27076__auto____1.call(this,state_37391);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
oc$web$ws$change_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$0 = oc$web$ws$change_client$state_machine__27076__auto____0;
oc$web$ws$change_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$1 = oc$web$ws$change_client$state_machine__27076__auto____1;
return oc$web$ws$change_client$state_machine__27076__auto__;
})()
})();
var state__27169__auto__ = (function (){var statearr_37397 = f__27168__auto__();
(statearr_37397[(6)] = c__27167__auto__);

return statearr_37397;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__27169__auto__);
}));

return c__27167__auto__;
}));
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.change_client !== 'undefined') && (typeof oc.web.ws.change_client._event_msg_handler !== 'undefined')){
} else {
/**
 * Multimethod to handle Sente `event-msg`s
 */
oc.web.ws.change_client._event_msg_handler = (function (){var method_table__4619__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var prefer_table__4620__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var method_cache__4621__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var cached_hierarchy__4622__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var hierarchy__4623__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$3(cljs.core.PersistentArrayMap.EMPTY,new cljs.core.Keyword(null,"hierarchy","hierarchy",-1053470341),(function (){var fexpr__37398 = cljs.core.get_global_hierarchy;
return (fexpr__37398.cljs$core$IFn$_invoke$arity$0 ? fexpr__37398.cljs$core$IFn$_invoke$arity$0() : fexpr__37398.call(null));
})());
return (new cljs.core.MultiFn(cljs.core.symbol.cljs$core$IFn$_invoke$arity$2("oc.web.ws.change-client","-event-msg-handler"),new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"default","default",-1987822328),hierarchy__4623__auto__,method_table__4619__auto__,prefer_table__4620__auto__,method_cache__4621__auto__,cached_hierarchy__4622__auto__));
})();
}
/**
 * Wraps `-event-msg-handler` with logging, error catching, etc.
 */
oc.web.ws.change_client.event_msg_handler = (function oc$web$ws$change_client$event_msg_handler(p__37399){
var map__37400 = p__37399;
var map__37400__$1 = (((((!((map__37400 == null))))?(((((map__37400.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__37400.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__37400):map__37400);
var ev_msg = map__37400__$1;
var id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37400__$1,new cljs.core.Keyword(null,"id","id",-1388402092));
var _QMARK_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37400__$1,new cljs.core.Keyword(null,"?data","?data",-9471433));
var event = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37400__$1,new cljs.core.Keyword(null,"event","event",301435442));
return oc.web.ws.change_client._event_msg_handler.cljs$core$IFn$_invoke$arity$1(ev_msg);
});
oc.web.ws.change_client._event_msg_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"default","default",-1987822328),(function (p__37402){
var map__37403 = p__37402;
var map__37403__$1 = (((((!((map__37403 == null))))?(((((map__37403.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__37403.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__37403):map__37403);
var ev_msg = map__37403__$1;
var event = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37403__$1,new cljs.core.Keyword(null,"event","event",301435442));
return taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"warn","warn",-436710552),"oc.web.ws.change-client",null,232,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Unhandled event: ",event], null);
}),null)),null,-1210057320);
}));
oc.web.ws.change_client._event_msg_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("chsk","state","chsk/state",-1991397620),(function (p__37405){
var map__37406 = p__37405;
var map__37406__$1 = (((((!((map__37406 == null))))?(((((map__37406.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__37406.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__37406):map__37406);
var ev_msg = map__37406__$1;
var _QMARK_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37406__$1,new cljs.core.Keyword(null,"?data","?data",-9471433));
var vec__37408 = (function (){var e = (function (){try{if(cljs.core.vector_QMARK_(_QMARK_data)){
return null;
} else {
return taoensso.truss.impl._dummy_error;
}
}catch (e37411){if((e37411 instanceof Error)){
var e = e37411;
return e;
} else {
throw e37411;

}
}})();
if((e == null)){
return _QMARK_data;
} else {
return taoensso.truss.impl._invar_violation_BANG_(true,"oc.web.ws.change-client",236,"(vector? ?data)",_QMARK_data,e,null);
}
})();
var old_state_map = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37408,(0),null);
var new_state_map = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37408,(1),null);
if(cljs.core.truth_(new cljs.core.Keyword(null,"first-open?","first-open?",396686530).cljs$core$IFn$_invoke$arity$1(new_state_map))){
return taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,238,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Channel socket successfully established!: ",new_state_map], null);
}),null)),null,-1103140066);
} else {
return taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,239,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Channel socket state change: ",new_state_map], null);
}),null)),null,-206951489);
}
}));
oc.web.ws.change_client._event_msg_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("chsk","recv","chsk/recv",561097091),(function (p__37412){
var map__37413 = p__37412;
var map__37413__$1 = (((((!((map__37413 == null))))?(((((map__37413.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__37413.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__37413):map__37413);
var ev_msg = map__37413__$1;
var _QMARK_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37413__$1,new cljs.core.Keyword(null,"?data","?data",-9471433));
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,243,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Push event from server: ",_QMARK_data], null);
}),null)),null,1957302683);

return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(oc.web.ws.change_client.event_handler,_QMARK_data);
}));
oc.web.ws.change_client._event_msg_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("chsk","handshake","chsk/handshake",64910686),(function (p__37415){
var map__37416 = p__37415;
var map__37416__$1 = (((((!((map__37416 == null))))?(((((map__37416.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__37416.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__37416):map__37416);
var ev_msg = map__37416__$1;
var _QMARK_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37416__$1,new cljs.core.Keyword(null,"?data","?data",-9471433));
var vec__37418 = _QMARK_data;
var _QMARK_uid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37418,(0),null);
var _QMARK_csrf_token = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37418,(1),null);
var _QMARK_handshake_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37418,(2),null);
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,249,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Handshake:",_QMARK_uid,_QMARK_csrf_token,_QMARK_handshake_data], null);
}),null)),null,-1248308744);

oc.web.ws.change_client.follow_list();

oc.web.ws.change_client.container_watch();

oc.web.ws.change_client.publisher_watch();

return (oc.web.ws.utils.after.cljs$core$IFn$_invoke$arity$2 ? oc.web.ws.utils.after.cljs$core$IFn$_invoke$arity$2((1000),oc.web.ws.change_client.followers_count) : oc.web.ws.utils.after.call(null,(1000),oc.web.ws.change_client.followers_count));
}));
oc.web.ws.change_client.stop_router_BANG_ = (function oc$web$ws$change_client$stop_router_BANG_(){
if(cljs.core.truth_(cljs.core.deref(oc.web.ws.change_client.channelsk))){
taoensso.sente.chsk_disconnect_BANG_(cljs.core.deref(oc.web.ws.change_client.channelsk));

return taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.ws.change-client",null,262,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Connection closed"], null);
}),null)),null,-1065162873);
} else {
return null;
}
});
oc.web.ws.change_client.start_router_BANG_ = (function oc$web$ws$change_client$start_router_BANG_(){
taoensso.sente.start_client_chsk_router_BANG_(cljs.core.deref(oc.web.ws.change_client.ch_chsk),oc.web.ws.change_client.event_msg_handler);

taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.ws.change-client",null,266,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Connection estabilished"], null);
}),null)),null,1398695306);

return oc.web.ws.utils.reconnected(oc.web.ws.change_client.last_interval,"Change",oc.web.ws.change_client.chsk_send_BANG_,oc.web.ws.change_client.ch_state,(function (){
var G__37421 = cljs.core.deref(oc.web.ws.change_client.last_ws_link);
var G__37422 = cljs.core.deref(oc.web.ws.change_client.current_uid);
var G__37423 = cljs.core.deref(oc.web.ws.change_client.current_org);
var G__37424 = cljs.core.deref(oc.web.ws.change_client.container_ids);
return (oc.web.ws.change_client.reconnect.cljs$core$IFn$_invoke$arity$4 ? oc.web.ws.change_client.reconnect.cljs$core$IFn$_invoke$arity$4(G__37421,G__37422,G__37423,G__37424) : oc.web.ws.change_client.reconnect.call(null,G__37421,G__37422,G__37423,G__37424));
}));
});
/**
 * Connect or reconnect the WebSocket connection to the change service
 */
oc.web.ws.change_client.reconnect = (function oc$web$ws$change_client$reconnect(ws_link,uid,org_slug,containers){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,273,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Change service reconnect",new cljs.core.Keyword(null,"href","href",-793805698).cljs$core$IFn$_invoke$arity$1(ws_link),uid,org_slug,containers], null);
}),null)),null,-405978992);

var ws_uri = goog.Uri.parse(new cljs.core.Keyword(null,"href","href",-793805698).cljs$core$IFn$_invoke$arity$1(ws_link));
var ws_domain = [cljs.core.str.cljs$core$IFn$_invoke$arity$1(ws_uri.getDomain()),(cljs.core.truth_(ws_uri.getPort())?[":",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ws_uri.getPort())].join(''):null)].join('');
var ws_org_path = ws_uri.getPath();
cljs.core.reset_BANG_(oc.web.ws.change_client.last_ws_link,ws_link);

cljs.core.reset_BANG_(oc.web.ws.change_client.container_ids,containers);

cljs.core.reset_BANG_(oc.web.ws.change_client.current_org,org_slug);

cljs.core.reset_BANG_(oc.web.ws.change_client.current_uid,uid);

if(((cljs.core.not(cljs.core.deref(oc.web.ws.change_client.ch_state))) || (cljs.core.not(new cljs.core.Keyword(null,"open?","open?",1238443125).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(cljs.core.deref(oc.web.ws.change_client.ch_state))))) || (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.ws.change_client.current_org),org_slug)))){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,287,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Connection is down, reconnecting. Current state:",cljs.core.deref(oc.web.ws.change_client.ch_state)], null);
}),null)),null,937358181);

if(cljs.core.truth_((function (){var and__4115__auto__ = cljs.core.deref(oc.web.ws.change_client.ch_state);
if(cljs.core.truth_(and__4115__auto__)){
return new cljs.core.Keyword(null,"open?","open?",1238443125).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(cljs.core.deref(oc.web.ws.change_client.ch_state)));
} else {
return and__4115__auto__;
}
})())){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.ws.change-client",null,291,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Closing previous connection for:",cljs.core.deref(oc.web.ws.change_client.current_org)], null);
}),null)),null,34591509);

oc.web.ws.change_client.stop_router_BANG_();
} else {
}

taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.ws.change-client",null,293,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Attempting change service connection to:",ws_domain,"for org:",org_slug], null);
}),null)),null,1129037577);

var map__37425 = (function (){var G__37426 = ws_org_path;
var G__37427 = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"auto","auto",-566279492),new cljs.core.Keyword(null,"host","host",-1558485167),ws_domain,new cljs.core.Keyword(null,"protocol","protocol",652470118),((oc.web.local_settings.jwt_cookie_secure)?new cljs.core.Keyword(null,"https","https",-1983909665):new cljs.core.Keyword(null,"http","http",382524695)),new cljs.core.Keyword(null,"packer","packer",66077544),new cljs.core.Keyword(null,"edn","edn",1317840885),new cljs.core.Keyword(null,"uid","uid",-1447769400),uid,new cljs.core.Keyword(null,"params","params",710516235),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"user-id","user-id",-206822291),uid], null)], null);
return (taoensso.sente.make_channel_socket_BANG_.cljs$core$IFn$_invoke$arity$2 ? taoensso.sente.make_channel_socket_BANG_.cljs$core$IFn$_invoke$arity$2(G__37426,G__37427) : taoensso.sente.make_channel_socket_BANG_.call(null,G__37426,G__37427));
})();
var map__37425__$1 = (((((!((map__37425 == null))))?(((((map__37425.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__37425.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__37425):map__37425);
var x = map__37425__$1;
var chsk = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37425__$1,new cljs.core.Keyword(null,"chsk","chsk",-863703081));
var ch_recv = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37425__$1,new cljs.core.Keyword(null,"ch-recv","ch-recv",-990916861));
var send_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37425__$1,new cljs.core.Keyword(null,"send-fn","send-fn",351002041));
var state = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37425__$1,new cljs.core.Keyword(null,"state","state",-1988618099));
cljs.core.reset_BANG_(oc.web.ws.change_client.channelsk,chsk);

cljs.core.reset_BANG_(oc.web.ws.change_client.ch_chsk,ch_recv);

cljs.core.reset_BANG_(oc.web.ws.change_client.chsk_send_BANG_,send_fn);

if(cljs.core.truth_(cljs.core.deref(oc.web.ws.change_client.ch_state))){
cljs.core.remove_watch(cljs.core.deref(oc.web.ws.change_client.ch_state),new cljs.core.Keyword(null,"change-client-state-watcher","change-client-state-watcher",1853094914));
} else {
}

cljs.core.reset_BANG_(oc.web.ws.change_client.ch_state,state);

cljs.core.add_watch(cljs.core.deref(oc.web.ws.change_client.ch_state),new cljs.core.Keyword(null,"change-client-state-watcher","change-client-state-watcher",1853094914),(function (key,a,old_state,new_state){
return cljs.core.reset_BANG_(oc.web.utils.ws_client_ids.change_client_id,new cljs.core.Keyword(null,"uid","uid",-1447769400).cljs$core$IFn$_invoke$arity$1(new_state));
}));

return oc.web.ws.change_client.start_router_BANG_();
} else {
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.change-client",null,315,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Connection already up, watch containers"], null);
}),null)),null,-1858800111);

return oc.web.ws.change_client.container_watch();
}
});

//# sourceMappingURL=oc.web.ws.change_client.js.map
