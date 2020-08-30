goog.provide('oc.web.ws.utils');
oc.web.ws.utils.sentry_report = (function oc$web$ws$utils$sentry_report(var_args){
var args__4742__auto__ = [];
var len__4736__auto___41339 = arguments.length;
var i__4737__auto___41340 = (0);
while(true){
if((i__4737__auto___41340 < len__4736__auto___41339)){
args__4742__auto__.push((arguments[i__4737__auto___41340]));

var G__41344 = (i__4737__auto___41340 + (1));
i__4737__auto___41340 = G__41344;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((3) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((3)),(0),null)):null);
return oc.web.ws.utils.sentry_report.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),argseq__4743__auto__);
});

(oc.web.ws.utils.sentry_report.cljs$core$IFn$_invoke$arity$variadic = (function (message,chsk_send_BANG_,ch_state,p__41232){
var vec__41233 = p__41232;
var action_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41233,(0),null);
var infos = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41233,(1),null);
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.utils",null,13,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Sentry report:",message], null);
}),null)),null,-1058051909);

var connection_status = (cljs.core.truth_(cljs.core.deref(ch_state))?cljs.core.deref(cljs.core.deref(ch_state)):null);
var ch_send_fn_QMARK_ = cljs.core.fn_QMARK_(cljs.core.deref(chsk_send_BANG_));
var ctx = new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"action","action",-811238024),action_id,new cljs.core.Keyword(null,"connection-status","connection-status",2011317083),connection_status,new cljs.core.Keyword(null,"send-fn","send-fn",351002041),ch_send_fn_QMARK_,new cljs.core.Keyword(null,"infos","infos",-927309652),infos,new cljs.core.Keyword(null,"sessionURL","sessionURL",-2099350313),oc.web.lib.fullstory.session_url()], null);
oc.web.lib.sentry.capture_message_with_extra_context_BANG_(ctx,message);

return taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"error","error",-978969032),"oc.web.ws.utils",null,23,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [message,ctx], null);
}),null)),null,212978138);
}));

(oc.web.ws.utils.sentry_report.cljs$lang$maxFixedArity = (3));

/** @this {Function} */
(oc.web.ws.utils.sentry_report.cljs$lang$applyTo = (function (seq41228){
var G__41229 = cljs.core.first(seq41228);
var seq41228__$1 = cljs.core.next(seq41228);
var G__41230 = cljs.core.first(seq41228__$1);
var seq41228__$2 = cljs.core.next(seq41228__$1);
var G__41231 = cljs.core.first(seq41228__$2);
var seq41228__$3 = cljs.core.next(seq41228__$2);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__41229,G__41230,G__41231,seq41228__$3);
}));

oc.web.ws.utils.real_send = (function oc$web$ws$utils$real_send(service_name,chsk_send_BANG_,ch_state,args){
try{return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(chsk_send_BANG_),args);
}catch (e41245){if((e41245 instanceof cljs.core.ExceptionInfo)){
var e = e41245;
return oc.web.ws.utils.sentry_report.cljs$core$IFn$_invoke$arity$variadic(["Error sending event for ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(service_name)].join(''),chsk_send_BANG_,ch_state,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.ffirst(args),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"rest-args","rest-args",48618520),cljs.core.rest(cljs.core.first(args)),new cljs.core.Keyword(null,"ex-message","ex-message",1526142375),e.message], null)], 0));
} else {
throw e41245;

}
}});
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.utils !== 'undefined') && (typeof oc.web.ws.utils.cmd_queue !== 'undefined')){
} else {
oc.web.ws.utils.cmd_queue = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
}
oc.web.ws.utils.buffer_cmd = (function oc$web$ws$utils$buffer_cmd(service_name,args){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.utils",null,40,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Queuing message for",service_name,args], null);
}),null)),null,-1422475839);

var service_key = cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(service_name);
var service_queue = (function (){var G__41251 = cljs.core.deref(oc.web.ws.utils.cmd_queue);
return (service_key.cljs$core$IFn$_invoke$arity$1 ? service_key.cljs$core$IFn$_invoke$arity$1(G__41251) : service_key.call(null,G__41251));
})();
var service_next_queue = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(service_queue,args);
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(oc.web.ws.utils.cmd_queue,cljs.core.assoc,service_key,service_next_queue);
});
oc.web.ws.utils.reset_queue = (function oc$web$ws$utils$reset_queue(service_name){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.utils",null,47,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Reset queue for",service_name], null);
}),null)),null,269703524);

return cljs.core.reset_BANG_(oc.web.ws.utils.cmd_queue,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.deref(oc.web.ws.utils.cmd_queue),cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(service_name),cljs.core.PersistentVector.EMPTY));
});
oc.web.ws.utils.send_queue = (function oc$web$ws$utils$send_queue(service_name,chsk_send_BANG_,ch_state){
if(cljs.core.truth_((function (){var and__4115__auto__ = cljs.core.deref(chsk_send_BANG_);
if(cljs.core.truth_(and__4115__auto__)){
return new cljs.core.Keyword(null,"open?","open?",1238443125).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(cljs.core.deref(ch_state)));
} else {
return and__4115__auto__;
}
})())){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.utils",null,52,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Send queue for",service_name,cljs.core.count((function (){var G__41258 = cljs.core.deref(oc.web.ws.utils.cmd_queue);
var fexpr__41257 = cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(service_name);
return (fexpr__41257.cljs$core$IFn$_invoke$arity$1 ? fexpr__41257.cljs$core$IFn$_invoke$arity$1(G__41258) : fexpr__41257.call(null,G__41258));
})())], null);
}),null)),null,581752131);

var seq__41259_41430 = cljs.core.seq((function (){var G__41266 = cljs.core.deref(oc.web.ws.utils.cmd_queue);
var fexpr__41265 = cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(service_name);
return (fexpr__41265.cljs$core$IFn$_invoke$arity$1 ? fexpr__41265.cljs$core$IFn$_invoke$arity$1(G__41266) : fexpr__41265.call(null,G__41266));
})());
var chunk__41260_41431 = null;
var count__41261_41432 = (0);
var i__41262_41433 = (0);
while(true){
if((i__41262_41433 < count__41261_41432)){
var qargs_41440 = chunk__41260_41431.cljs$core$IIndexed$_nth$arity$2(null,i__41262_41433);
oc.web.ws.utils.real_send(service_name,chsk_send_BANG_,ch_state,qargs_41440);


var G__41441 = seq__41259_41430;
var G__41442 = chunk__41260_41431;
var G__41443 = count__41261_41432;
var G__41444 = (i__41262_41433 + (1));
seq__41259_41430 = G__41441;
chunk__41260_41431 = G__41442;
count__41261_41432 = G__41443;
i__41262_41433 = G__41444;
continue;
} else {
var temp__5735__auto___41445 = cljs.core.seq(seq__41259_41430);
if(temp__5735__auto___41445){
var seq__41259_41446__$1 = temp__5735__auto___41445;
if(cljs.core.chunked_seq_QMARK_(seq__41259_41446__$1)){
var c__4556__auto___41447 = cljs.core.chunk_first(seq__41259_41446__$1);
var G__41448 = cljs.core.chunk_rest(seq__41259_41446__$1);
var G__41449 = c__4556__auto___41447;
var G__41450 = cljs.core.count(c__4556__auto___41447);
var G__41451 = (0);
seq__41259_41430 = G__41448;
chunk__41260_41431 = G__41449;
count__41261_41432 = G__41450;
i__41262_41433 = G__41451;
continue;
} else {
var qargs_41452 = cljs.core.first(seq__41259_41446__$1);
oc.web.ws.utils.real_send(service_name,chsk_send_BANG_,ch_state,qargs_41452);


var G__41453 = cljs.core.next(seq__41259_41446__$1);
var G__41454 = null;
var G__41455 = (0);
var G__41456 = (0);
seq__41259_41430 = G__41453;
chunk__41260_41431 = G__41454;
count__41261_41432 = G__41455;
i__41262_41433 = G__41456;
continue;
}
} else {
}
}
break;
}

return oc.web.ws.utils.reset_queue(service_name);
} else {
return null;
}
});
oc.web.ws.utils.send_BANG_ = (function oc$web$ws$utils$send_BANG_(service_name,chsk_send_BANG_,ch_state,args){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.utils",null,58,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Send!",service_name,args], null);
}),null)),null,-816586896);

if(cljs.core.truth_((function (){var and__4115__auto__ = cljs.core.deref(chsk_send_BANG_);
if(cljs.core.truth_(and__4115__auto__)){
return new cljs.core.Keyword(null,"open?","open?",1238443125).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(cljs.core.deref(ch_state)));
} else {
return and__4115__auto__;
}
})())){
oc.web.ws.utils.send_queue(service_name,chsk_send_BANG_,ch_state);

return oc.web.ws.utils.real_send(service_name,chsk_send_BANG_,ch_state,args);
} else {
return oc.web.ws.utils.buffer_cmd(service_name,args);
}
});
oc.web.ws.utils.check_interval = (function oc$web$ws$utils$check_interval(last_interval,service_name,chsk_send_BANG_,ch_state,reconnect_cb){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.utils",null,69,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Set check-interval for",service_name], null);
}),null)),null,-1093784125);

if(cljs.core.truth_(cljs.core.deref(last_interval))){
window.clearInterval(cljs.core.deref(last_interval));
} else {
}

return cljs.core.reset_BANG_(last_interval,window.setInterval((function (){
if(((cljs.core.not(cljs.core.deref(ch_state))) || (cljs.core.not(cljs.core.deref(cljs.core.deref(ch_state)))) || (cljs.core.not(new cljs.core.Keyword(null,"open?","open?",1238443125).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(cljs.core.deref(ch_state))))))){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.utils",null,77,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["WS check-interval connection down for",service_name], null);
}),null)),null,-502086325);

if(cljs.core.truth_(new cljs.core.Keyword(null,"udt-next-reconnect","udt-next-reconnect",-1990375733).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(cljs.core.deref(ch_state))))){
return taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.utils",null,81,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Will reconnect automatically at",oc.web.lib.utils.js_date.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"udt-next-reconnect","udt-next-reconnect",-1990375733).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(cljs.core.deref(ch_state)))], 0))], null);
}),null)),null,1674422628);
} else {
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.utils",null,84,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["No auto reconnect set, forcing reconnection"], null);
}),null)),null,1626953312);

oc.web.ws.utils.sentry_report(["No auto reconnect set for",cljs.core.str.cljs$core$IFn$_invoke$arity$1(service_name),". Forcing reconnect!"].join(''),chsk_send_BANG_,ch_state);

return oc.web.lib.utils.after((0),reconnect_cb);
}
} else {
return null;
}
}),(oc.web.local_settings.ws_monitor_interval * (1000))));
});
oc.web.ws.utils.reconnected = (function oc$web$ws$utils$reconnected(last_interval,service_name,chsk_send_BANG_,ch_state,reconnect_cb){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.utils",null,90,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Reconnect",service_name], null);
}),null)),null,-1662187416);

oc.web.ws.utils.send_queue(service_name,chsk_send_BANG_,ch_state);

return oc.web.ws.utils.check_interval(last_interval,service_name,chsk_send_BANG_,ch_state,reconnect_cb);
});
oc.web.ws.utils.report_invalid_jwt = (function oc$web$ws$utils$report_invalid_jwt(service_name,ch_state,rep){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.utils",null,95,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Report invalid-jwt",service_name], null);
}),null)),null,94994410);

var connection_status = (cljs.core.truth_(cljs.core.deref(ch_state))?cljs.core.deref(cljs.core.deref(ch_state)):null);
var ctx = new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"jwt","jwt",1504015441),oc.web.lib.jwt.jwt(),new cljs.core.Keyword(null,"connection-status","connection-status",2011317083),connection_status,new cljs.core.Keyword(null,"timestamp","timestamp",579478971),(new Date()).getTime(),new cljs.core.Keyword(null,"rep","rep",-1226820564),rep,new cljs.core.Keyword(null,"sessionURL","sessionURL",-2099350313),oc.web.lib.fullstory.session_url()], null);
oc.web.lib.sentry.capture_message_with_extra_context_BANG_(ctx,[cljs.core.str.cljs$core$IFn$_invoke$arity$1(service_name)," WS: not valid JWT"].join(''));

return taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"error","error",-978969032),"oc.web.ws.utils",null,104,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [service_name,"WS: not valid JWT",ctx], null);
}),null)),null,-922172161);
});
oc.web.ws.utils.report_connect_timeout = (function oc$web$ws$utils$report_connect_timeout(service_name,ch_state){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.utils",null,107,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Report connection-timeout",service_name], null);
}),null)),null,322350694);

var connection_status = (cljs.core.truth_(cljs.core.deref(ch_state))?cljs.core.deref(cljs.core.deref(ch_state)):null);
var ctx = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"timestamp","timestamp",579478971),(new Date()).getTime(),new cljs.core.Keyword(null,"connection-status","connection-status",2011317083),connection_status,new cljs.core.Keyword(null,"sessionURL","sessionURL",-2099350313),oc.web.lib.fullstory.session_url()], null);
oc.web.lib.sentry.capture_message_with_extra_context_BANG_(ctx,[cljs.core.str.cljs$core$IFn$_invoke$arity$1(service_name)," WS: handshake timeout"].join(''));

return taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"error","error",-978969032),"oc.web.ws.utils",null,114,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [service_name,"WS: handshake timeout",ctx], null);
}),null)),null,877293024);
});
oc.web.ws.utils.auth_check = (function oc$web$ws$utils$auth_check(service_name,ch_state,chsk_send_BANG_,channelsk,jwt_refresh_cb,reconnect_cb,success_cb,rep){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.utils",null,117,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Auth-check",service_name], null);
}),null)),null,-2118658584);

if(cljs.core.truth_(((taoensso.sente.cb_success_QMARK_(rep))?new cljs.core.Keyword(null,"valid","valid",155614240).cljs$core$IFn$_invoke$arity$1(rep):false))){
if(cljs.core.fn_QMARK_(success_cb)){
return (success_cb.cljs$core$IFn$_invoke$arity$1 ? success_cb.cljs$core$IFn$_invoke$arity$1(rep) : success_cb.call(null,rep));
} else {
return null;
}
} else {
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"warn","warn",-436710552),"oc.web.ws.utils",null,123,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Disconnecting client due to invalid JWT!",rep], null);
}),null)),null,655517869);

taoensso.sente.chsk_disconnect_BANG_(cljs.core.deref(channelsk));

if(oc.web.lib.jwt.expired_QMARK_()){
return (jwt_refresh_cb.cljs$core$IFn$_invoke$arity$1 ? jwt_refresh_cb.cljs$core$IFn$_invoke$arity$1(reconnect_cb) : jwt_refresh_cb.call(null,reconnect_cb));
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(rep,new cljs.core.Keyword("chsk","timeout","chsk/timeout",-319776489))){
oc.web.ws.utils.report_connect_timeout(service_name,ch_state);

if(cljs.core.truth_((function (){var and__4115__auto__ = cljs.core.deref(ch_state);
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(new cljs.core.Keyword(null,"udt-next-reconnect","udt-next-reconnect",-1990375733).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(cljs.core.deref(ch_state))));
} else {
return and__4115__auto__;
}
})())){
return oc.web.lib.utils.after(((10) * (1000)),(reconnect_cb.cljs$core$IFn$_invoke$arity$0 ? reconnect_cb.cljs$core$IFn$_invoke$arity$0() : reconnect_cb.call(null)));
} else {
return null;
}
} else {
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(rep,new cljs.core.Keyword("chsk","closed","chsk/closed",-922855264))) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(rep,"closed")))){
oc.web.ws.utils.sentry_report.cljs$core$IFn$_invoke$arity$variadic(["Auth failed for",cljs.core.str.cljs$core$IFn$_invoke$arity$1(service_name)].join(''),chsk_send_BANG_,ch_state,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["jwt-validation/auth-check",new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword("chsk","closed","chsk/closed",-922855264),cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(rep,new cljs.core.Keyword("chsk","closed","chsk/closed",-922855264)),new cljs.core.Keyword(null,"closed","closed",-919675359),cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(rep,"closed")], null)], 0));

if(cljs.core.truth_((function (){var and__4115__auto__ = cljs.core.deref(ch_state);
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(new cljs.core.Keyword(null,"udt-next-reconnect","udt-next-reconnect",-1990375733).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(cljs.core.deref(ch_state))));
} else {
return and__4115__auto__;
}
})())){
return oc.web.lib.utils.after(((10) * (1000)),(reconnect_cb.cljs$core$IFn$_invoke$arity$0 ? reconnect_cb.cljs$core$IFn$_invoke$arity$0() : reconnect_cb.call(null)));
} else {
return null;
}
} else {
return oc.web.ws.utils.report_invalid_jwt(service_name,ch_state,rep);

}
}
}
}
});
oc.web.ws.utils.post_handshake_auth = (function oc$web$ws$utils$post_handshake_auth(jwt_refresh_cb,auth_cb){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.utils",null,151,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Trying post handshake jwt auth"], null);
}),null)),null,720338744);

if(oc.web.lib.jwt.expired_QMARK_()){
return (jwt_refresh_cb.cljs$core$IFn$_invoke$arity$1 ? jwt_refresh_cb.cljs$core$IFn$_invoke$arity$1(auth_cb) : jwt_refresh_cb.call(null,auth_cb));
} else {
return (auth_cb.cljs$core$IFn$_invoke$arity$0 ? auth_cb.cljs$core$IFn$_invoke$arity$0() : auth_cb.call(null));
}
});
oc.web.ws.utils.after = oc.web.lib.utils.after;

//# sourceMappingURL=oc.web.ws.utils.js.map
