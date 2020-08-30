goog.provide('oc.web.ws.interaction_client');
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.interaction_client !== 'undefined') && (typeof oc.web.ws.interaction_client.current_board_path !== 'undefined')){
} else {
oc.web.ws.interaction_client.current_board_path = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.interaction_client !== 'undefined') && (typeof oc.web.ws.interaction_client.channelsk !== 'undefined')){
} else {
oc.web.ws.interaction_client.channelsk = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.interaction_client !== 'undefined') && (typeof oc.web.ws.interaction_client.ch_chsk !== 'undefined')){
} else {
oc.web.ws.interaction_client.ch_chsk = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.interaction_client !== 'undefined') && (typeof oc.web.ws.interaction_client.ch_state !== 'undefined')){
} else {
oc.web.ws.interaction_client.ch_state = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.interaction_client !== 'undefined') && (typeof oc.web.ws.interaction_client.chsk_send_BANG_ !== 'undefined')){
} else {
oc.web.ws.interaction_client.chsk_send_BANG_ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.interaction_client !== 'undefined') && (typeof oc.web.ws.interaction_client.last_ws_link !== 'undefined')){
} else {
oc.web.ws.interaction_client.last_ws_link = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.interaction_client !== 'undefined') && (typeof oc.web.ws.interaction_client.last_board_uuids !== 'undefined')){
} else {
oc.web.ws.interaction_client.last_board_uuids = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentVector.EMPTY);
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.interaction_client !== 'undefined') && (typeof oc.web.ws.interaction_client.ch_pub !== 'undefined')){
} else {
oc.web.ws.interaction_client.ch_pub = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$0();
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.interaction_client !== 'undefined') && (typeof oc.web.ws.interaction_client.last_interval !== 'undefined')){
} else {
oc.web.ws.interaction_client.last_interval = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.interaction_client !== 'undefined') && (typeof oc.web.ws.interaction_client.publication !== 'undefined')){
} else {
oc.web.ws.interaction_client.publication = cljs.core.async.pub.cljs$core$IFn$_invoke$arity$2(oc.web.ws.interaction_client.ch_pub,new cljs.core.Keyword(null,"topic","topic",-1960480691));
}
oc.web.ws.interaction_client.send_BANG_ = (function oc$web$ws$interaction_client$send_BANG_(var_args){
var args__4742__auto__ = [];
var len__4736__auto___42204 = arguments.length;
var i__4737__auto___42205 = (0);
while(true){
if((i__4737__auto___42205 < len__4736__auto___42204)){
args__4742__auto__.push((arguments[i__4737__auto___42205]));

var G__42206 = (i__4737__auto___42205 + (1));
i__4737__auto___42205 = G__42206;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.ws.interaction_client.send_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.ws.interaction_client.send_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (chsk_send_BANG_,args){
return oc.web.ws.utils.send_BANG_("Interaction",chsk_send_BANG_,oc.web.ws.interaction_client.ch_state,args);
}));

(oc.web.ws.interaction_client.send_BANG_.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.ws.interaction_client.send_BANG_.cljs$lang$applyTo = (function (seq41977){
var G__41978 = cljs.core.first(seq41977);
var seq41977__$1 = cljs.core.next(seq41977);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__41978,seq41977__$1);
}));

oc.web.ws.interaction_client.boards_watch = (function oc$web$ws$interaction_client$boards_watch(var_args){
var G__41981 = arguments.length;
switch (G__41981) {
case 1:
return oc.web.ws.interaction_client.boards_watch.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 0:
return oc.web.ws.interaction_client.boards_watch.cljs$core$IFn$_invoke$arity$0();

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.ws.interaction_client.boards_watch.cljs$core$IFn$_invoke$arity$1 = (function (board_uuids){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.interaction-client",null,43,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Watching boards: ",board_uuids], null);
}),null)),null,1922020593);

cljs.core.reset_BANG_(oc.web.ws.interaction_client.last_board_uuids,board_uuids);

return oc.web.ws.interaction_client.boards_watch.cljs$core$IFn$_invoke$arity$0();
}));

(oc.web.ws.interaction_client.boards_watch.cljs$core$IFn$_invoke$arity$0 = (function (){
if(cljs.core.truth_(((cljs.core.fn_QMARK_(cljs.core.deref(oc.web.ws.interaction_client.chsk_send_BANG_)))?(function (){var and__4115__auto__ = cljs.core.deref(oc.web.ws.interaction_client.ch_state);
if(cljs.core.truth_(and__4115__auto__)){
return new cljs.core.Keyword(null,"open?","open?",1238443125).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(cljs.core.deref(oc.web.ws.interaction_client.ch_state)));
} else {
return and__4115__auto__;
}
})():false))){
var seq__41982 = cljs.core.seq(cljs.core.deref(oc.web.ws.interaction_client.last_board_uuids));
var chunk__41983 = null;
var count__41984 = (0);
var i__41985 = (0);
while(true){
if((i__41985 < count__41984)){
var board_uuid = chunk__41983.cljs$core$IIndexed$_nth$arity$2(null,i__41985);
oc.web.ws.interaction_client.send_BANG_.cljs$core$IFn$_invoke$arity$variadic(oc.web.ws.interaction_client.chsk_send_BANG_,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("watch","board","watch/board",-2020576432),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127),board_uuid], null)], null)], 0));


var G__42208 = seq__41982;
var G__42209 = chunk__41983;
var G__42210 = count__41984;
var G__42211 = (i__41985 + (1));
seq__41982 = G__42208;
chunk__41983 = G__42209;
count__41984 = G__42210;
i__41985 = G__42211;
continue;
} else {
var temp__5735__auto__ = cljs.core.seq(seq__41982);
if(temp__5735__auto__){
var seq__41982__$1 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__41982__$1)){
var c__4556__auto__ = cljs.core.chunk_first(seq__41982__$1);
var G__42212 = cljs.core.chunk_rest(seq__41982__$1);
var G__42213 = c__4556__auto__;
var G__42214 = cljs.core.count(c__4556__auto__);
var G__42215 = (0);
seq__41982 = G__42212;
chunk__41983 = G__42213;
count__41984 = G__42214;
i__41985 = G__42215;
continue;
} else {
var board_uuid = cljs.core.first(seq__41982__$1);
oc.web.ws.interaction_client.send_BANG_.cljs$core$IFn$_invoke$arity$variadic(oc.web.ws.interaction_client.chsk_send_BANG_,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("watch","board","watch/board",-2020576432),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127),board_uuid], null)], null)], 0));


var G__42216 = cljs.core.next(seq__41982__$1);
var G__42217 = null;
var G__42218 = (0);
var G__42219 = (0);
seq__41982 = G__42216;
chunk__41983 = G__42217;
count__41984 = G__42218;
i__41985 = G__42219;
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
}));

(oc.web.ws.interaction_client.boards_watch.cljs$lang$maxFixedArity = 1);

oc.web.ws.interaction_client.board_unwatch = (function oc$web$ws$interaction_client$board_unwatch(callback){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.interaction-client",null,54,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Unwatching all boards."], null);
}),null)),null,-1113219377);

cljs.core.reset_BANG_(oc.web.ws.interaction_client.last_board_uuids,cljs.core.PersistentVector.EMPTY);

return oc.web.ws.interaction_client.send_BANG_.cljs$core$IFn$_invoke$arity$variadic(oc.web.ws.interaction_client.chsk_send_BANG_,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("unwatch","board","unwatch/board",1591876165)], null),(10000),callback], 0));
});
oc.web.ws.interaction_client.subscribe = (function oc$web$ws$interaction_client$subscribe(topic,handler_fn){
var ws_ic_chan = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$0();
cljs.core.async.sub.cljs$core$IFn$_invoke$arity$3(oc.web.ws.interaction_client.publication,topic,ws_ic_chan);

var c__27167__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__27168__auto__ = (function (){var switch__27075__auto__ = (function (state_42014){
var state_val_42015 = (state_42014[(1)]);
if((state_val_42015 === (1))){
var state_42014__$1 = state_42014;
var statearr_42016_42220 = state_42014__$1;
(statearr_42016_42220[(2)] = null);

(statearr_42016_42220[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_42015 === (2))){
var state_42014__$1 = state_42014;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_42014__$1,(4),ws_ic_chan);
} else {
if((state_val_42015 === (3))){
var inst_42012 = (state_42014[(2)]);
var state_42014__$1 = state_42014;
return cljs.core.async.impl.ioc_helpers.return_chan(state_42014__$1,inst_42012);
} else {
if((state_val_42015 === (4))){
var inst_42008 = (state_42014[(2)]);
var inst_42009 = (handler_fn.cljs$core$IFn$_invoke$arity$1 ? handler_fn.cljs$core$IFn$_invoke$arity$1(inst_42008) : handler_fn.call(null,inst_42008));
var state_42014__$1 = (function (){var statearr_42017 = state_42014;
(statearr_42017[(7)] = inst_42009);

return statearr_42017;
})();
var statearr_42018_42223 = state_42014__$1;
(statearr_42018_42223[(2)] = null);

(statearr_42018_42223[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
});
return (function() {
var oc$web$ws$interaction_client$subscribe_$_state_machine__27076__auto__ = null;
var oc$web$ws$interaction_client$subscribe_$_state_machine__27076__auto____0 = (function (){
var statearr_42019 = [null,null,null,null,null,null,null,null];
(statearr_42019[(0)] = oc$web$ws$interaction_client$subscribe_$_state_machine__27076__auto__);

(statearr_42019[(1)] = (1));

return statearr_42019;
});
var oc$web$ws$interaction_client$subscribe_$_state_machine__27076__auto____1 = (function (state_42014){
while(true){
var ret_value__27077__auto__ = (function (){try{while(true){
var result__27078__auto__ = switch__27075__auto__(state_42014);
if(cljs.core.keyword_identical_QMARK_(result__27078__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__27078__auto__;
}
break;
}
}catch (e42020){var ex__27079__auto__ = e42020;
var statearr_42021_42228 = state_42014;
(statearr_42021_42228[(2)] = ex__27079__auto__);


if(cljs.core.seq((state_42014[(4)]))){
var statearr_42022_42229 = state_42014;
(statearr_42022_42229[(1)] = cljs.core.first((state_42014[(4)])));

} else {
throw ex__27079__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__27077__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__42230 = state_42014;
state_42014 = G__42230;
continue;
} else {
return ret_value__27077__auto__;
}
break;
}
});
oc$web$ws$interaction_client$subscribe_$_state_machine__27076__auto__ = function(state_42014){
switch(arguments.length){
case 0:
return oc$web$ws$interaction_client$subscribe_$_state_machine__27076__auto____0.call(this);
case 1:
return oc$web$ws$interaction_client$subscribe_$_state_machine__27076__auto____1.call(this,state_42014);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
oc$web$ws$interaction_client$subscribe_$_state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$0 = oc$web$ws$interaction_client$subscribe_$_state_machine__27076__auto____0;
oc$web$ws$interaction_client$subscribe_$_state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$1 = oc$web$ws$interaction_client$subscribe_$_state_machine__27076__auto____1;
return oc$web$ws$interaction_client$subscribe_$_state_machine__27076__auto__;
})()
})();
var state__27169__auto__ = (function (){var statearr_42023 = f__27168__auto__();
(statearr_42023[(6)] = c__27167__auto__);

return statearr_42023;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__27169__auto__);
}));

return c__27167__auto__;
});
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.interaction_client !== 'undefined') && (typeof oc.web.ws.interaction_client.event_handler !== 'undefined')){
} else {
/**
 * Multimethod to handle our internal events
 */
oc.web.ws.interaction_client.event_handler = (function (){var method_table__4619__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var prefer_table__4620__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var method_cache__4621__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var cached_hierarchy__4622__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var hierarchy__4623__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$3(cljs.core.PersistentArrayMap.EMPTY,new cljs.core.Keyword(null,"hierarchy","hierarchy",-1053470341),(function (){var fexpr__42024 = cljs.core.get_global_hierarchy;
return (fexpr__42024.cljs$core$IFn$_invoke$arity$0 ? fexpr__42024.cljs$core$IFn$_invoke$arity$0() : fexpr__42024.call(null));
})());
return (new cljs.core.MultiFn(cljs.core.symbol.cljs$core$IFn$_invoke$arity$2("oc.web.ws.interaction-client","event-handler"),(function() { 
var G__42233__delegate = function (event,_){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.interaction-client",null,70,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["event-handler",event], null);
}),null)),null,-820970192);

return event;
};
var G__42233 = function (event,var_args){
var _ = null;
if (arguments.length > 1) {
var G__42234__i = 0, G__42234__a = new Array(arguments.length -  1);
while (G__42234__i < G__42234__a.length) {G__42234__a[G__42234__i] = arguments[G__42234__i + 1]; ++G__42234__i;}
  _ = new cljs.core.IndexedSeq(G__42234__a,0,null);
} 
return G__42233__delegate.call(this,event,_);};
G__42233.cljs$lang$maxFixedArity = 1;
G__42233.cljs$lang$applyTo = (function (arglist__42235){
var event = cljs.core.first(arglist__42235);
var _ = cljs.core.rest(arglist__42235);
return G__42233__delegate(event,_);
});
G__42233.cljs$core$IFn$_invoke$arity$variadic = G__42233__delegate;
return G__42233;
})()
,new cljs.core.Keyword(null,"default","default",-1987822328),hierarchy__4623__auto__,method_table__4619__auto__,prefer_table__4620__auto__,method_cache__4621__auto__,cached_hierarchy__4622__auto__));
})();
}
oc.web.ws.interaction_client.event_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"default","default",-1987822328),(function() { 
var G__42236__delegate = function (_,r){
return taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.ws.interaction-client",null,75,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["No event handler defined for",_], null);
}),null)),null,-521246350);
};
var G__42236 = function (_,var_args){
var r = null;
if (arguments.length > 1) {
var G__42237__i = 0, G__42237__a = new Array(arguments.length -  1);
while (G__42237__i < G__42237__a.length) {G__42237__a[G__42237__i] = arguments[G__42237__i + 1]; ++G__42237__i;}
  r = new cljs.core.IndexedSeq(G__42237__a,0,null);
} 
return G__42236__delegate.call(this,_,r);};
G__42236.cljs$lang$maxFixedArity = 1;
G__42236.cljs$lang$applyTo = (function (arglist__42238){
var _ = cljs.core.first(arglist__42238);
var r = cljs.core.rest(arglist__42238);
return G__42236__delegate(_,r);
});
G__42236.cljs$core$IFn$_invoke$arity$variadic = G__42236__delegate;
return G__42236;
})()
);
oc.web.ws.interaction_client.event_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("chsk","ws-ping","chsk/ws-ping",191675304),(function() { 
var G__42239__delegate = function (_,r){
var c__27167__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__27168__auto__ = (function (){var switch__27075__auto__ = (function (state_42031){
var state_val_42032 = (state_42031[(1)]);
if((state_val_42032 === (1))){
var inst_42025 = [new cljs.core.Keyword(null,"topic","topic",-1960480691)];
var inst_42026 = [new cljs.core.Keyword("chsk","ws-ping","chsk/ws-ping",191675304)];
var inst_42027 = cljs.core.PersistentHashMap.fromArrays(inst_42025,inst_42026);
var state_42031__$1 = state_42031;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_42031__$1,(2),oc.web.ws.interaction_client.ch_pub,inst_42027);
} else {
if((state_val_42032 === (2))){
var inst_42029 = (state_42031[(2)]);
var state_42031__$1 = state_42031;
return cljs.core.async.impl.ioc_helpers.return_chan(state_42031__$1,inst_42029);
} else {
return null;
}
}
});
return (function() {
var oc$web$ws$interaction_client$state_machine__27076__auto__ = null;
var oc$web$ws$interaction_client$state_machine__27076__auto____0 = (function (){
var statearr_42041 = [null,null,null,null,null,null,null];
(statearr_42041[(0)] = oc$web$ws$interaction_client$state_machine__27076__auto__);

(statearr_42041[(1)] = (1));

return statearr_42041;
});
var oc$web$ws$interaction_client$state_machine__27076__auto____1 = (function (state_42031){
while(true){
var ret_value__27077__auto__ = (function (){try{while(true){
var result__27078__auto__ = switch__27075__auto__(state_42031);
if(cljs.core.keyword_identical_QMARK_(result__27078__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__27078__auto__;
}
break;
}
}catch (e42042){var ex__27079__auto__ = e42042;
var statearr_42043_42240 = state_42031;
(statearr_42043_42240[(2)] = ex__27079__auto__);


if(cljs.core.seq((state_42031[(4)]))){
var statearr_42044_42241 = state_42031;
(statearr_42044_42241[(1)] = cljs.core.first((state_42031[(4)])));

} else {
throw ex__27079__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__27077__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__42242 = state_42031;
state_42031 = G__42242;
continue;
} else {
return ret_value__27077__auto__;
}
break;
}
});
oc$web$ws$interaction_client$state_machine__27076__auto__ = function(state_42031){
switch(arguments.length){
case 0:
return oc$web$ws$interaction_client$state_machine__27076__auto____0.call(this);
case 1:
return oc$web$ws$interaction_client$state_machine__27076__auto____1.call(this,state_42031);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
oc$web$ws$interaction_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$0 = oc$web$ws$interaction_client$state_machine__27076__auto____0;
oc$web$ws$interaction_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$1 = oc$web$ws$interaction_client$state_machine__27076__auto____1;
return oc$web$ws$interaction_client$state_machine__27076__auto__;
})()
})();
var state__27169__auto__ = (function (){var statearr_42045 = f__27168__auto__();
(statearr_42045[(6)] = c__27167__auto__);

return statearr_42045;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__27169__auto__);
}));

return c__27167__auto__;
};
var G__42239 = function (_,var_args){
var r = null;
if (arguments.length > 1) {
var G__42243__i = 0, G__42243__a = new Array(arguments.length -  1);
while (G__42243__i < G__42243__a.length) {G__42243__a[G__42243__i] = arguments[G__42243__i + 1]; ++G__42243__i;}
  r = new cljs.core.IndexedSeq(G__42243__a,0,null);
} 
return G__42239__delegate.call(this,_,r);};
G__42239.cljs$lang$maxFixedArity = 1;
G__42239.cljs$lang$applyTo = (function (arglist__42244){
var _ = cljs.core.first(arglist__42244);
var r = cljs.core.rest(arglist__42244);
return G__42239__delegate(_,r);
});
G__42239.cljs$core$IFn$_invoke$arity$variadic = G__42239__delegate;
return G__42239;
})()
);
oc.web.ws.interaction_client.event_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("interaction-comment","add","interaction-comment/add",-357724065),(function (_,body){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.interaction-client",null,83,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Comment add event",body], null);
}),null)),null,-1769934111);

var c__27167__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__27168__auto__ = (function (){var switch__27075__auto__ = (function (state_42052){
var state_val_42053 = (state_42052[(1)]);
if((state_val_42053 === (1))){
var inst_42046 = [new cljs.core.Keyword(null,"topic","topic",-1960480691),new cljs.core.Keyword(null,"data","data",-232669377)];
var inst_42047 = [new cljs.core.Keyword("interaction-comment","add","interaction-comment/add",-357724065),body];
var inst_42048 = cljs.core.PersistentHashMap.fromArrays(inst_42046,inst_42047);
var state_42052__$1 = state_42052;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_42052__$1,(2),oc.web.ws.interaction_client.ch_pub,inst_42048);
} else {
if((state_val_42053 === (2))){
var inst_42050 = (state_42052[(2)]);
var state_42052__$1 = state_42052;
return cljs.core.async.impl.ioc_helpers.return_chan(state_42052__$1,inst_42050);
} else {
return null;
}
}
});
return (function() {
var oc$web$ws$interaction_client$state_machine__27076__auto__ = null;
var oc$web$ws$interaction_client$state_machine__27076__auto____0 = (function (){
var statearr_42059 = [null,null,null,null,null,null,null];
(statearr_42059[(0)] = oc$web$ws$interaction_client$state_machine__27076__auto__);

(statearr_42059[(1)] = (1));

return statearr_42059;
});
var oc$web$ws$interaction_client$state_machine__27076__auto____1 = (function (state_42052){
while(true){
var ret_value__27077__auto__ = (function (){try{while(true){
var result__27078__auto__ = switch__27075__auto__(state_42052);
if(cljs.core.keyword_identical_QMARK_(result__27078__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__27078__auto__;
}
break;
}
}catch (e42060){var ex__27079__auto__ = e42060;
var statearr_42062_42246 = state_42052;
(statearr_42062_42246[(2)] = ex__27079__auto__);


if(cljs.core.seq((state_42052[(4)]))){
var statearr_42069_42248 = state_42052;
(statearr_42069_42248[(1)] = cljs.core.first((state_42052[(4)])));

} else {
throw ex__27079__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__27077__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__42249 = state_42052;
state_42052 = G__42249;
continue;
} else {
return ret_value__27077__auto__;
}
break;
}
});
oc$web$ws$interaction_client$state_machine__27076__auto__ = function(state_42052){
switch(arguments.length){
case 0:
return oc$web$ws$interaction_client$state_machine__27076__auto____0.call(this);
case 1:
return oc$web$ws$interaction_client$state_machine__27076__auto____1.call(this,state_42052);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
oc$web$ws$interaction_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$0 = oc$web$ws$interaction_client$state_machine__27076__auto____0;
oc$web$ws$interaction_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$1 = oc$web$ws$interaction_client$state_machine__27076__auto____1;
return oc$web$ws$interaction_client$state_machine__27076__auto__;
})()
})();
var state__27169__auto__ = (function (){var statearr_42071 = f__27168__auto__();
(statearr_42071[(6)] = c__27167__auto__);

return statearr_42071;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__27169__auto__);
}));

return c__27167__auto__;
}));
oc.web.ws.interaction_client.event_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("interaction-comment","update","interaction-comment/update",1637403952),(function (_,body){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.interaction-client",null,88,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Comment update event",body], null);
}),null)),null,-1723707826);

var c__27167__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__27168__auto__ = (function (){var switch__27075__auto__ = (function (state_42078){
var state_val_42079 = (state_42078[(1)]);
if((state_val_42079 === (1))){
var inst_42072 = [new cljs.core.Keyword(null,"topic","topic",-1960480691),new cljs.core.Keyword(null,"data","data",-232669377)];
var inst_42073 = [new cljs.core.Keyword("interaction-comment","update","interaction-comment/update",1637403952),body];
var inst_42074 = cljs.core.PersistentHashMap.fromArrays(inst_42072,inst_42073);
var state_42078__$1 = state_42078;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_42078__$1,(2),oc.web.ws.interaction_client.ch_pub,inst_42074);
} else {
if((state_val_42079 === (2))){
var inst_42076 = (state_42078[(2)]);
var state_42078__$1 = state_42078;
return cljs.core.async.impl.ioc_helpers.return_chan(state_42078__$1,inst_42076);
} else {
return null;
}
}
});
return (function() {
var oc$web$ws$interaction_client$state_machine__27076__auto__ = null;
var oc$web$ws$interaction_client$state_machine__27076__auto____0 = (function (){
var statearr_42080 = [null,null,null,null,null,null,null];
(statearr_42080[(0)] = oc$web$ws$interaction_client$state_machine__27076__auto__);

(statearr_42080[(1)] = (1));

return statearr_42080;
});
var oc$web$ws$interaction_client$state_machine__27076__auto____1 = (function (state_42078){
while(true){
var ret_value__27077__auto__ = (function (){try{while(true){
var result__27078__auto__ = switch__27075__auto__(state_42078);
if(cljs.core.keyword_identical_QMARK_(result__27078__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__27078__auto__;
}
break;
}
}catch (e42081){var ex__27079__auto__ = e42081;
var statearr_42082_42250 = state_42078;
(statearr_42082_42250[(2)] = ex__27079__auto__);


if(cljs.core.seq((state_42078[(4)]))){
var statearr_42083_42251 = state_42078;
(statearr_42083_42251[(1)] = cljs.core.first((state_42078[(4)])));

} else {
throw ex__27079__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__27077__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__42254 = state_42078;
state_42078 = G__42254;
continue;
} else {
return ret_value__27077__auto__;
}
break;
}
});
oc$web$ws$interaction_client$state_machine__27076__auto__ = function(state_42078){
switch(arguments.length){
case 0:
return oc$web$ws$interaction_client$state_machine__27076__auto____0.call(this);
case 1:
return oc$web$ws$interaction_client$state_machine__27076__auto____1.call(this,state_42078);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
oc$web$ws$interaction_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$0 = oc$web$ws$interaction_client$state_machine__27076__auto____0;
oc$web$ws$interaction_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$1 = oc$web$ws$interaction_client$state_machine__27076__auto____1;
return oc$web$ws$interaction_client$state_machine__27076__auto__;
})()
})();
var state__27169__auto__ = (function (){var statearr_42084 = f__27168__auto__();
(statearr_42084[(6)] = c__27167__auto__);

return statearr_42084;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__27169__auto__);
}));

return c__27167__auto__;
}));
oc.web.ws.interaction_client.event_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("interaction-comment","delete","interaction-comment/delete",2035105872),(function (_,body){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.interaction-client",null,93,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Comment delete event",body], null);
}),null)),null,-216543486);

var c__27167__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__27168__auto__ = (function (){var switch__27075__auto__ = (function (state_42091){
var state_val_42092 = (state_42091[(1)]);
if((state_val_42092 === (1))){
var inst_42085 = [new cljs.core.Keyword(null,"topic","topic",-1960480691),new cljs.core.Keyword(null,"data","data",-232669377)];
var inst_42086 = [new cljs.core.Keyword("interaction-comment","delete","interaction-comment/delete",2035105872),body];
var inst_42087 = cljs.core.PersistentHashMap.fromArrays(inst_42085,inst_42086);
var state_42091__$1 = state_42091;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_42091__$1,(2),oc.web.ws.interaction_client.ch_pub,inst_42087);
} else {
if((state_val_42092 === (2))){
var inst_42089 = (state_42091[(2)]);
var state_42091__$1 = state_42091;
return cljs.core.async.impl.ioc_helpers.return_chan(state_42091__$1,inst_42089);
} else {
return null;
}
}
});
return (function() {
var oc$web$ws$interaction_client$state_machine__27076__auto__ = null;
var oc$web$ws$interaction_client$state_machine__27076__auto____0 = (function (){
var statearr_42098 = [null,null,null,null,null,null,null];
(statearr_42098[(0)] = oc$web$ws$interaction_client$state_machine__27076__auto__);

(statearr_42098[(1)] = (1));

return statearr_42098;
});
var oc$web$ws$interaction_client$state_machine__27076__auto____1 = (function (state_42091){
while(true){
var ret_value__27077__auto__ = (function (){try{while(true){
var result__27078__auto__ = switch__27075__auto__(state_42091);
if(cljs.core.keyword_identical_QMARK_(result__27078__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__27078__auto__;
}
break;
}
}catch (e42099){var ex__27079__auto__ = e42099;
var statearr_42101_42257 = state_42091;
(statearr_42101_42257[(2)] = ex__27079__auto__);


if(cljs.core.seq((state_42091[(4)]))){
var statearr_42106_42258 = state_42091;
(statearr_42106_42258[(1)] = cljs.core.first((state_42091[(4)])));

} else {
throw ex__27079__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__27077__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__42260 = state_42091;
state_42091 = G__42260;
continue;
} else {
return ret_value__27077__auto__;
}
break;
}
});
oc$web$ws$interaction_client$state_machine__27076__auto__ = function(state_42091){
switch(arguments.length){
case 0:
return oc$web$ws$interaction_client$state_machine__27076__auto____0.call(this);
case 1:
return oc$web$ws$interaction_client$state_machine__27076__auto____1.call(this,state_42091);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
oc$web$ws$interaction_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$0 = oc$web$ws$interaction_client$state_machine__27076__auto____0;
oc$web$ws$interaction_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$1 = oc$web$ws$interaction_client$state_machine__27076__auto____1;
return oc$web$ws$interaction_client$state_machine__27076__auto__;
})()
})();
var state__27169__auto__ = (function (){var statearr_42120 = f__27168__auto__();
(statearr_42120[(6)] = c__27167__auto__);

return statearr_42120;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__27169__auto__);
}));

return c__27167__auto__;
}));
oc.web.ws.interaction_client.event_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("interaction-reaction","add","interaction-reaction/add",1243084351),(function (_,body){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.interaction-client",null,98,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Reaction add event",body], null);
}),null)),null,690304662);

var c__27167__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__27168__auto__ = (function (){var switch__27075__auto__ = (function (state_42127){
var state_val_42128 = (state_42127[(1)]);
if((state_val_42128 === (1))){
var inst_42121 = [new cljs.core.Keyword(null,"topic","topic",-1960480691),new cljs.core.Keyword(null,"data","data",-232669377)];
var inst_42122 = [new cljs.core.Keyword("interaction-reaction","add","interaction-reaction/add",1243084351),body];
var inst_42123 = cljs.core.PersistentHashMap.fromArrays(inst_42121,inst_42122);
var state_42127__$1 = state_42127;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_42127__$1,(2),oc.web.ws.interaction_client.ch_pub,inst_42123);
} else {
if((state_val_42128 === (2))){
var inst_42125 = (state_42127[(2)]);
var state_42127__$1 = state_42127;
return cljs.core.async.impl.ioc_helpers.return_chan(state_42127__$1,inst_42125);
} else {
return null;
}
}
});
return (function() {
var oc$web$ws$interaction_client$state_machine__27076__auto__ = null;
var oc$web$ws$interaction_client$state_machine__27076__auto____0 = (function (){
var statearr_42129 = [null,null,null,null,null,null,null];
(statearr_42129[(0)] = oc$web$ws$interaction_client$state_machine__27076__auto__);

(statearr_42129[(1)] = (1));

return statearr_42129;
});
var oc$web$ws$interaction_client$state_machine__27076__auto____1 = (function (state_42127){
while(true){
var ret_value__27077__auto__ = (function (){try{while(true){
var result__27078__auto__ = switch__27075__auto__(state_42127);
if(cljs.core.keyword_identical_QMARK_(result__27078__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__27078__auto__;
}
break;
}
}catch (e42130){var ex__27079__auto__ = e42130;
var statearr_42131_42264 = state_42127;
(statearr_42131_42264[(2)] = ex__27079__auto__);


if(cljs.core.seq((state_42127[(4)]))){
var statearr_42132_42265 = state_42127;
(statearr_42132_42265[(1)] = cljs.core.first((state_42127[(4)])));

} else {
throw ex__27079__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__27077__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__42266 = state_42127;
state_42127 = G__42266;
continue;
} else {
return ret_value__27077__auto__;
}
break;
}
});
oc$web$ws$interaction_client$state_machine__27076__auto__ = function(state_42127){
switch(arguments.length){
case 0:
return oc$web$ws$interaction_client$state_machine__27076__auto____0.call(this);
case 1:
return oc$web$ws$interaction_client$state_machine__27076__auto____1.call(this,state_42127);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
oc$web$ws$interaction_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$0 = oc$web$ws$interaction_client$state_machine__27076__auto____0;
oc$web$ws$interaction_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$1 = oc$web$ws$interaction_client$state_machine__27076__auto____1;
return oc$web$ws$interaction_client$state_machine__27076__auto__;
})()
})();
var state__27169__auto__ = (function (){var statearr_42133 = f__27168__auto__();
(statearr_42133[(6)] = c__27167__auto__);

return statearr_42133;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__27169__auto__);
}));

return c__27167__auto__;
}));
oc.web.ws.interaction_client.event_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("interaction-reaction","delete","interaction-reaction/delete",1121937008),(function (_,body){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.interaction-client",null,103,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Reaction delete event",body], null);
}),null)),null,-68045286);

var c__27167__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__27168__auto__ = (function (){var switch__27075__auto__ = (function (state_42140){
var state_val_42141 = (state_42140[(1)]);
if((state_val_42141 === (1))){
var inst_42134 = [new cljs.core.Keyword(null,"topic","topic",-1960480691),new cljs.core.Keyword(null,"data","data",-232669377)];
var inst_42135 = [new cljs.core.Keyword("interaction-reaction","delete","interaction-reaction/delete",1121937008),body];
var inst_42136 = cljs.core.PersistentHashMap.fromArrays(inst_42134,inst_42135);
var state_42140__$1 = state_42140;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_42140__$1,(2),oc.web.ws.interaction_client.ch_pub,inst_42136);
} else {
if((state_val_42141 === (2))){
var inst_42138 = (state_42140[(2)]);
var state_42140__$1 = state_42140;
return cljs.core.async.impl.ioc_helpers.return_chan(state_42140__$1,inst_42138);
} else {
return null;
}
}
});
return (function() {
var oc$web$ws$interaction_client$state_machine__27076__auto__ = null;
var oc$web$ws$interaction_client$state_machine__27076__auto____0 = (function (){
var statearr_42142 = [null,null,null,null,null,null,null];
(statearr_42142[(0)] = oc$web$ws$interaction_client$state_machine__27076__auto__);

(statearr_42142[(1)] = (1));

return statearr_42142;
});
var oc$web$ws$interaction_client$state_machine__27076__auto____1 = (function (state_42140){
while(true){
var ret_value__27077__auto__ = (function (){try{while(true){
var result__27078__auto__ = switch__27075__auto__(state_42140);
if(cljs.core.keyword_identical_QMARK_(result__27078__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__27078__auto__;
}
break;
}
}catch (e42144){var ex__27079__auto__ = e42144;
var statearr_42145_42267 = state_42140;
(statearr_42145_42267[(2)] = ex__27079__auto__);


if(cljs.core.seq((state_42140[(4)]))){
var statearr_42146_42268 = state_42140;
(statearr_42146_42268[(1)] = cljs.core.first((state_42140[(4)])));

} else {
throw ex__27079__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__27077__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__42269 = state_42140;
state_42140 = G__42269;
continue;
} else {
return ret_value__27077__auto__;
}
break;
}
});
oc$web$ws$interaction_client$state_machine__27076__auto__ = function(state_42140){
switch(arguments.length){
case 0:
return oc$web$ws$interaction_client$state_machine__27076__auto____0.call(this);
case 1:
return oc$web$ws$interaction_client$state_machine__27076__auto____1.call(this,state_42140);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
oc$web$ws$interaction_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$0 = oc$web$ws$interaction_client$state_machine__27076__auto____0;
oc$web$ws$interaction_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$1 = oc$web$ws$interaction_client$state_machine__27076__auto____1;
return oc$web$ws$interaction_client$state_machine__27076__auto__;
})()
})();
var state__27169__auto__ = (function (){var statearr_42147 = f__27168__auto__();
(statearr_42147[(6)] = c__27167__auto__);

return statearr_42147;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__27169__auto__);
}));

return c__27167__auto__;
}));
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.interaction_client !== 'undefined') && (typeof oc.web.ws.interaction_client._event_msg_handler !== 'undefined')){
} else {
/**
 * Multimethod to handle Sente `event-msg`s
 */
oc.web.ws.interaction_client._event_msg_handler = (function (){var method_table__4619__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var prefer_table__4620__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var method_cache__4621__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var cached_hierarchy__4622__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var hierarchy__4623__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$3(cljs.core.PersistentArrayMap.EMPTY,new cljs.core.Keyword(null,"hierarchy","hierarchy",-1053470341),(function (){var fexpr__42148 = cljs.core.get_global_hierarchy;
return (fexpr__42148.cljs$core$IFn$_invoke$arity$0 ? fexpr__42148.cljs$core$IFn$_invoke$arity$0() : fexpr__42148.call(null));
})());
return (new cljs.core.MultiFn(cljs.core.symbol.cljs$core$IFn$_invoke$arity$2("oc.web.ws.interaction-client","-event-msg-handler"),new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"default","default",-1987822328),hierarchy__4623__auto__,method_table__4619__auto__,prefer_table__4620__auto__,method_cache__4621__auto__,cached_hierarchy__4622__auto__));
})();
}
/**
 * Wraps `-event-msg-handler` with logging, error catching, etc.
 */
oc.web.ws.interaction_client.event_msg_handler = (function oc$web$ws$interaction_client$event_msg_handler(p__42149){
var map__42150 = p__42149;
var map__42150__$1 = (((((!((map__42150 == null))))?(((((map__42150.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42150.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42150):map__42150);
var ev_msg = map__42150__$1;
var id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42150__$1,new cljs.core.Keyword(null,"id","id",-1388402092));
var _QMARK_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42150__$1,new cljs.core.Keyword(null,"?data","?data",-9471433));
var event = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42150__$1,new cljs.core.Keyword(null,"event","event",301435442));
return oc.web.ws.interaction_client._event_msg_handler.cljs$core$IFn$_invoke$arity$1(ev_msg);
});
oc.web.ws.interaction_client._event_msg_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"default","default",-1987822328),(function (p__42152){
var map__42153 = p__42152;
var map__42153__$1 = (((((!((map__42153 == null))))?(((((map__42153.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42153.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42153):map__42153);
var ev_msg = map__42153__$1;
var event = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42153__$1,new cljs.core.Keyword(null,"event","event",301435442));
return taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"warn","warn",-436710552),"oc.web.ws.interaction-client",null,122,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Unhandled event: ",event], null);
}),null)),null,877162280);
}));
oc.web.ws.interaction_client._event_msg_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("chsk","state","chsk/state",-1991397620),(function (p__42155){
var map__42156 = p__42155;
var map__42156__$1 = (((((!((map__42156 == null))))?(((((map__42156.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42156.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42156):map__42156);
var ev_msg = map__42156__$1;
var _QMARK_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42156__$1,new cljs.core.Keyword(null,"?data","?data",-9471433));
var vec__42158 = (function (){var e = (function (){try{if(cljs.core.vector_QMARK_(_QMARK_data)){
return null;
} else {
return taoensso.truss.impl._dummy_error;
}
}catch (e42173){if((e42173 instanceof Error)){
var e = e42173;
return e;
} else {
throw e42173;

}
}})();
if((e == null)){
return _QMARK_data;
} else {
return taoensso.truss.impl._invar_violation_BANG_(true,"oc.web.ws.interaction-client",126,"(vector? ?data)",_QMARK_data,e,null);
}
})();
var old_state_map = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42158,(0),null);
var new_state_map = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42158,(1),null);
if(cljs.core.truth_(new cljs.core.Keyword(null,"first-open?","first-open?",396686530).cljs$core$IFn$_invoke$arity$1(new_state_map))){
return taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.interaction-client",null,128,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Channel socket successfully established!: ",new_state_map], null);
}),null)),null,-2129445914);
} else {
return taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.interaction-client",null,129,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Channel socket state change: ",new_state_map], null);
}),null)),null,959526735);
}
}));
oc.web.ws.interaction_client._event_msg_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("chsk","recv","chsk/recv",561097091),(function (p__42176){
var map__42177 = p__42176;
var map__42177__$1 = (((((!((map__42177 == null))))?(((((map__42177.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42177.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42177):map__42177);
var ev_msg = map__42177__$1;
var _QMARK_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42177__$1,new cljs.core.Keyword(null,"?data","?data",-9471433));
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.interaction-client",null,133,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Push event from server: ",_QMARK_data], null);
}),null)),null,-73897341);

return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(oc.web.ws.interaction_client.event_handler,_QMARK_data);
}));
oc.web.ws.interaction_client._event_msg_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("chsk","handshake","chsk/handshake",64910686),(function (p__42182){
var map__42183 = p__42182;
var map__42183__$1 = (((((!((map__42183 == null))))?(((((map__42183.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42183.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42183):map__42183);
var ev_msg = map__42183__$1;
var _QMARK_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42183__$1,new cljs.core.Keyword(null,"?data","?data",-9471433));
var auth_cb_42271 = cljs.core.partial.cljs$core$IFn$_invoke$arity$variadic(oc.web.ws.utils.auth_check,"Interaction",oc.web.ws.interaction_client.ch_state,oc.web.ws.interaction_client.chsk_send_BANG_,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([oc.web.ws.interaction_client.channelsk,oc.web.actions.jwt.jwt_refresh,(function (){
var G__42185 = cljs.core.deref(oc.web.ws.interaction_client.last_ws_link);
var G__42186 = oc.web.lib.jwt.user_id();
return (oc.web.ws.interaction_client.reconnect.cljs$core$IFn$_invoke$arity$2 ? oc.web.ws.interaction_client.reconnect.cljs$core$IFn$_invoke$arity$2(G__42185,G__42186) : oc.web.ws.interaction_client.reconnect.call(null,G__42185,G__42186));
}),oc.web.ws.interaction_client.boards_watch], 0));
oc.web.ws.utils.post_handshake_auth(oc.web.actions.jwt.jwt_refresh,(function (){
return oc.web.ws.interaction_client.send_BANG_.cljs$core$IFn$_invoke$arity$variadic(oc.web.ws.interaction_client.chsk_send_BANG_,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("auth","jwt","auth/jwt",1503073961),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"jwt","jwt",1504015441),oc.web.lib.jwt.jwt()], null)], null),(60000),auth_cb_42271], 0));
}));

var vec__42187 = _QMARK_data;
var _QMARK_uid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42187,(0),null);
var _QMARK_csrf_token = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42187,(1),null);
var _QMARK_handshake_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42187,(2),null);
return taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.interaction-client",null,145,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Handshake:",_QMARK_uid,_QMARK_csrf_token,_QMARK_handshake_data], null);
}),null)),null,1004206628);
}));
/**
 * Ping the server to update the sesssion state.
 */
oc.web.ws.interaction_client.test_session = (function oc$web$ws$interaction_client$test_session(){
return oc.web.ws.interaction_client.send_BANG_.cljs$core$IFn$_invoke$arity$variadic(oc.web.ws.interaction_client.chsk_send_BANG_,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("session","status","session/status",289111609)], null)], 0));
});
oc.web.ws.interaction_client.stop_router_BANG_ = (function oc$web$ws$interaction_client$stop_router_BANG_(){
if(cljs.core.truth_(cljs.core.deref(oc.web.ws.interaction_client.channelsk))){
taoensso.sente.chsk_disconnect_BANG_(cljs.core.deref(oc.web.ws.interaction_client.channelsk));

return taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.ws.interaction-client",null,158,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Connection closed"], null);
}),null)),null,-817368667);
} else {
return null;
}
});
oc.web.ws.interaction_client.start_router_BANG_ = (function oc$web$ws$interaction_client$start_router_BANG_(){
taoensso.sente.start_client_chsk_router_BANG_(cljs.core.deref(oc.web.ws.interaction_client.ch_chsk),oc.web.ws.interaction_client.event_msg_handler);

taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.ws.interaction-client",null,162,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Connection established"], null);
}),null)),null,-297603700);

return oc.web.ws.utils.reconnected(oc.web.ws.interaction_client.last_interval,"Interaction",oc.web.ws.interaction_client.chsk_send_BANG_,oc.web.ws.interaction_client.ch_state,(function (){
var G__42192 = cljs.core.deref(oc.web.ws.interaction_client.last_ws_link);
var G__42193 = oc.web.lib.jwt.user_id();
return (oc.web.ws.interaction_client.reconnect.cljs$core$IFn$_invoke$arity$2 ? oc.web.ws.interaction_client.reconnect.cljs$core$IFn$_invoke$arity$2(G__42192,G__42193) : oc.web.ws.interaction_client.reconnect.call(null,G__42192,G__42193));
}));
});
oc.web.ws.interaction_client.reconnect = (function oc$web$ws$interaction_client$reconnect(ws_link,uid){
var ws_uri = goog.Uri.parse(new cljs.core.Keyword(null,"href","href",-793805698).cljs$core$IFn$_invoke$arity$1(ws_link));
var ws_domain = [cljs.core.str.cljs$core$IFn$_invoke$arity$1(ws_uri.getDomain()),(cljs.core.truth_(ws_uri.getPort())?[":",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ws_uri.getPort())].join(''):null)].join('');
var ws_board_path = ws_uri.getPath();
cljs.core.reset_BANG_(oc.web.ws.interaction_client.last_ws_link,ws_link);

if(((cljs.core.not(cljs.core.deref(oc.web.ws.interaction_client.ch_state))) || (cljs.core.not(new cljs.core.Keyword(null,"open?","open?",1238443125).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(cljs.core.deref(oc.web.ws.interaction_client.ch_state))))) || (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.ws.interaction_client.current_board_path),ws_board_path)))){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.interaction-client",null,175,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Reconnect for",new cljs.core.Keyword(null,"href","href",-793805698).cljs$core$IFn$_invoke$arity$1(ws_link),"and",uid,"current state:",cljs.core.deref(oc.web.ws.interaction_client.ch_state),"current board:",cljs.core.deref(oc.web.ws.interaction_client.current_board_path)], null);
}),null)),null,359288486);

if(cljs.core.truth_((function (){var and__4115__auto__ = cljs.core.deref(oc.web.ws.interaction_client.ch_state);
if(cljs.core.truth_(and__4115__auto__)){
return new cljs.core.Keyword(null,"open?","open?",1238443125).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(cljs.core.deref(oc.web.ws.interaction_client.ch_state)));
} else {
return and__4115__auto__;
}
})())){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.ws.interaction-client",null,180,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Closing previous connection for",cljs.core.deref(oc.web.ws.interaction_client.current_board_path)], null);
}),null)),null,-753888296);

oc.web.ws.interaction_client.stop_router_BANG_();
} else {
}

taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.ws.interaction-client",null,182,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Attempting interaction service connection to",ws_domain,"for board",ws_board_path], null);
}),null)),null,-1832200237);

var map__42199 = (function (){var G__42200 = ws_board_path;
var G__42201 = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"auto","auto",-566279492),new cljs.core.Keyword(null,"host","host",-1558485167),ws_domain,new cljs.core.Keyword(null,"protocol","protocol",652470118),((oc.web.local_settings.jwt_cookie_secure)?new cljs.core.Keyword(null,"https","https",-1983909665):new cljs.core.Keyword(null,"http","http",382524695)),new cljs.core.Keyword(null,"packer","packer",66077544),new cljs.core.Keyword(null,"edn","edn",1317840885),new cljs.core.Keyword(null,"uid","uid",-1447769400),uid,new cljs.core.Keyword(null,"params","params",710516235),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"user-id","user-id",-206822291),uid], null)], null);
return (taoensso.sente.make_channel_socket_BANG_.cljs$core$IFn$_invoke$arity$2 ? taoensso.sente.make_channel_socket_BANG_.cljs$core$IFn$_invoke$arity$2(G__42200,G__42201) : taoensso.sente.make_channel_socket_BANG_.call(null,G__42200,G__42201));
})();
var map__42199__$1 = (((((!((map__42199 == null))))?(((((map__42199.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42199.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42199):map__42199);
var x = map__42199__$1;
var chsk = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42199__$1,new cljs.core.Keyword(null,"chsk","chsk",-863703081));
var ch_recv = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42199__$1,new cljs.core.Keyword(null,"ch-recv","ch-recv",-990916861));
var send_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42199__$1,new cljs.core.Keyword(null,"send-fn","send-fn",351002041));
var state = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42199__$1,new cljs.core.Keyword(null,"state","state",-1988618099));
cljs.core.reset_BANG_(oc.web.ws.interaction_client.current_board_path,ws_board_path);

cljs.core.reset_BANG_(oc.web.ws.interaction_client.channelsk,chsk);

cljs.core.reset_BANG_(oc.web.ws.interaction_client.ch_chsk,ch_recv);

cljs.core.reset_BANG_(oc.web.ws.interaction_client.chsk_send_BANG_,send_fn);

if(cljs.core.truth_(cljs.core.deref(oc.web.ws.interaction_client.ch_state))){
cljs.core.remove_watch(cljs.core.deref(oc.web.ws.interaction_client.ch_state),new cljs.core.Keyword(null,"interaction-client-state-watcher","interaction-client-state-watcher",1244676978));
} else {
}

cljs.core.reset_BANG_(oc.web.ws.interaction_client.ch_state,state);

cljs.core.add_watch(cljs.core.deref(oc.web.ws.interaction_client.ch_state),new cljs.core.Keyword(null,"interaction-client-state-watcher","interaction-client-state-watcher",1244676978),(function (key,a,old_state,new_state){
return cljs.core.reset_BANG_(oc.web.utils.ws_client_ids.interaction_client_id,new cljs.core.Keyword(null,"uid","uid",-1447769400).cljs$core$IFn$_invoke$arity$1(new_state));
}));

return oc.web.ws.interaction_client.start_router_BANG_();
} else {
if((cljs.core.count(cljs.core.deref(oc.web.ws.interaction_client.last_board_uuids)) > (0))){
return oc.web.ws.interaction_client.boards_watch.cljs$core$IFn$_invoke$arity$0();
} else {
return null;
}
}
});

//# sourceMappingURL=oc.web.ws.interaction_client.js.map
