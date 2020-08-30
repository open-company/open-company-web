goog.provide('oc.web.ws.notify_client');
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.notify_client !== 'undefined') && (typeof oc.web.ws.notify_client.channelsk !== 'undefined')){
} else {
oc.web.ws.notify_client.channelsk = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.notify_client !== 'undefined') && (typeof oc.web.ws.notify_client.ch_chsk !== 'undefined')){
} else {
oc.web.ws.notify_client.ch_chsk = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.notify_client !== 'undefined') && (typeof oc.web.ws.notify_client.ch_state !== 'undefined')){
} else {
oc.web.ws.notify_client.ch_state = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.notify_client !== 'undefined') && (typeof oc.web.ws.notify_client.chsk_send_BANG_ !== 'undefined')){
} else {
oc.web.ws.notify_client.chsk_send_BANG_ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.notify_client !== 'undefined') && (typeof oc.web.ws.notify_client.ch_pub !== 'undefined')){
} else {
oc.web.ws.notify_client.ch_pub = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$0();
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.notify_client !== 'undefined') && (typeof oc.web.ws.notify_client.last_ws_link !== 'undefined')){
} else {
oc.web.ws.notify_client.last_ws_link = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.notify_client !== 'undefined') && (typeof oc.web.ws.notify_client.last_interval !== 'undefined')){
} else {
oc.web.ws.notify_client.last_interval = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.notify_client !== 'undefined') && (typeof oc.web.ws.notify_client.publication !== 'undefined')){
} else {
oc.web.ws.notify_client.publication = cljs.core.async.pub.cljs$core$IFn$_invoke$arity$2(oc.web.ws.notify_client.ch_pub,new cljs.core.Keyword(null,"topic","topic",-1960480691));
}
oc.web.ws.notify_client.send_BANG_ = (function oc$web$ws$notify_client$send_BANG_(var_args){
var args__4742__auto__ = [];
var len__4736__auto___43040 = arguments.length;
var i__4737__auto___43041 = (0);
while(true){
if((i__4737__auto___43041 < len__4736__auto___43040)){
args__4742__auto__.push((arguments[i__4737__auto___43041]));

var G__43042 = (i__4737__auto___43041 + (1));
i__4737__auto___43041 = G__43042;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.ws.notify_client.send_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.ws.notify_client.send_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (chsk_send_BANG_,args){
return oc.web.ws.utils.send_BANG_("Notify",chsk_send_BANG_,oc.web.ws.notify_client.ch_state,args);
}));

(oc.web.ws.notify_client.send_BANG_.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.ws.notify_client.send_BANG_.cljs$lang$applyTo = (function (seq42781){
var G__42782 = cljs.core.first(seq42781);
var seq42781__$1 = cljs.core.next(seq42781);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__42782,seq42781__$1);
}));

oc.web.ws.notify_client.notifications_watch = (function oc$web$ws$notify_client$notifications_watch(){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.notify-client",null,36,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Watching notifications."], null);
}),null)),null,-1059006186);

return oc.web.ws.notify_client.send_BANG_.cljs$core$IFn$_invoke$arity$variadic(oc.web.ws.notify_client.chsk_send_BANG_,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("watch","notifications","watch/notifications",1798672548),cljs.core.PersistentArrayMap.EMPTY], null)], 0));
});
oc.web.ws.notify_client.subscribe = (function oc$web$ws$notify_client$subscribe(topic,handler_fn){
var ws_nc_chan = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$0();
cljs.core.async.sub.cljs$core$IFn$_invoke$arity$3(oc.web.ws.notify_client.publication,topic,ws_nc_chan);

var c__27167__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__27168__auto__ = (function (){var switch__27075__auto__ = (function (state_42847){
var state_val_42848 = (state_42847[(1)]);
if((state_val_42848 === (1))){
var state_42847__$1 = state_42847;
var statearr_42849_43043 = state_42847__$1;
(statearr_42849_43043[(2)] = null);

(statearr_42849_43043[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_42848 === (2))){
var state_42847__$1 = state_42847;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_42847__$1,(4),ws_nc_chan);
} else {
if((state_val_42848 === (3))){
var inst_42845 = (state_42847[(2)]);
var state_42847__$1 = state_42847;
return cljs.core.async.impl.ioc_helpers.return_chan(state_42847__$1,inst_42845);
} else {
if((state_val_42848 === (4))){
var inst_42841 = (state_42847[(2)]);
var inst_42842 = (handler_fn.cljs$core$IFn$_invoke$arity$1 ? handler_fn.cljs$core$IFn$_invoke$arity$1(inst_42841) : handler_fn.call(null,inst_42841));
var state_42847__$1 = (function (){var statearr_42850 = state_42847;
(statearr_42850[(7)] = inst_42842);

return statearr_42850;
})();
var statearr_42851_43044 = state_42847__$1;
(statearr_42851_43044[(2)] = null);

(statearr_42851_43044[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
});
return (function() {
var oc$web$ws$notify_client$subscribe_$_state_machine__27076__auto__ = null;
var oc$web$ws$notify_client$subscribe_$_state_machine__27076__auto____0 = (function (){
var statearr_42852 = [null,null,null,null,null,null,null,null];
(statearr_42852[(0)] = oc$web$ws$notify_client$subscribe_$_state_machine__27076__auto__);

(statearr_42852[(1)] = (1));

return statearr_42852;
});
var oc$web$ws$notify_client$subscribe_$_state_machine__27076__auto____1 = (function (state_42847){
while(true){
var ret_value__27077__auto__ = (function (){try{while(true){
var result__27078__auto__ = switch__27075__auto__(state_42847);
if(cljs.core.keyword_identical_QMARK_(result__27078__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__27078__auto__;
}
break;
}
}catch (e42853){var ex__27079__auto__ = e42853;
var statearr_42854_43045 = state_42847;
(statearr_42854_43045[(2)] = ex__27079__auto__);


if(cljs.core.seq((state_42847[(4)]))){
var statearr_42855_43046 = state_42847;
(statearr_42855_43046[(1)] = cljs.core.first((state_42847[(4)])));

} else {
throw ex__27079__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__27077__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__43047 = state_42847;
state_42847 = G__43047;
continue;
} else {
return ret_value__27077__auto__;
}
break;
}
});
oc$web$ws$notify_client$subscribe_$_state_machine__27076__auto__ = function(state_42847){
switch(arguments.length){
case 0:
return oc$web$ws$notify_client$subscribe_$_state_machine__27076__auto____0.call(this);
case 1:
return oc$web$ws$notify_client$subscribe_$_state_machine__27076__auto____1.call(this,state_42847);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
oc$web$ws$notify_client$subscribe_$_state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$0 = oc$web$ws$notify_client$subscribe_$_state_machine__27076__auto____0;
oc$web$ws$notify_client$subscribe_$_state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$1 = oc$web$ws$notify_client$subscribe_$_state_machine__27076__auto____1;
return oc$web$ws$notify_client$subscribe_$_state_machine__27076__auto__;
})()
})();
var state__27169__auto__ = (function (){var statearr_42856 = f__27168__auto__();
(statearr_42856[(6)] = c__27167__auto__);

return statearr_42856;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__27169__auto__);
}));

return c__27167__auto__;
});
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.notify_client !== 'undefined') && (typeof oc.web.ws.notify_client.event_handler !== 'undefined')){
} else {
/**
 * Multimethod to handle our internal events
 */
oc.web.ws.notify_client.event_handler = (function (){var method_table__4619__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var prefer_table__4620__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var method_cache__4621__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var cached_hierarchy__4622__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var hierarchy__4623__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$3(cljs.core.PersistentArrayMap.EMPTY,new cljs.core.Keyword(null,"hierarchy","hierarchy",-1053470341),(function (){var fexpr__42857 = cljs.core.get_global_hierarchy;
return (fexpr__42857.cljs$core$IFn$_invoke$arity$0 ? fexpr__42857.cljs$core$IFn$_invoke$arity$0() : fexpr__42857.call(null));
})());
return (new cljs.core.MultiFn(cljs.core.symbol.cljs$core$IFn$_invoke$arity$2("oc.web.ws.notify-client","event-handler"),(function() { 
var G__43048__delegate = function (event,_){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.notify-client",null,54,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["event-handler",event], null);
}),null)),null,1677450121);

return event;
};
var G__43048 = function (event,var_args){
var _ = null;
if (arguments.length > 1) {
var G__43049__i = 0, G__43049__a = new Array(arguments.length -  1);
while (G__43049__i < G__43049__a.length) {G__43049__a[G__43049__i] = arguments[G__43049__i + 1]; ++G__43049__i;}
  _ = new cljs.core.IndexedSeq(G__43049__a,0,null);
} 
return G__43048__delegate.call(this,event,_);};
G__43048.cljs$lang$maxFixedArity = 1;
G__43048.cljs$lang$applyTo = (function (arglist__43050){
var event = cljs.core.first(arglist__43050);
var _ = cljs.core.rest(arglist__43050);
return G__43048__delegate(event,_);
});
G__43048.cljs$core$IFn$_invoke$arity$variadic = G__43048__delegate;
return G__43048;
})()
,new cljs.core.Keyword(null,"default","default",-1987822328),hierarchy__4623__auto__,method_table__4619__auto__,prefer_table__4620__auto__,method_cache__4621__auto__,cached_hierarchy__4622__auto__));
})();
}
oc.web.ws.notify_client.event_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"default","default",-1987822328),(function() { 
var G__43051__delegate = function (_,r){
return taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.ws.notify-client",null,59,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["No event handler defined for",_], null);
}),null)),null,881875220);
};
var G__43051 = function (_,var_args){
var r = null;
if (arguments.length > 1) {
var G__43052__i = 0, G__43052__a = new Array(arguments.length -  1);
while (G__43052__i < G__43052__a.length) {G__43052__a[G__43052__i] = arguments[G__43052__i + 1]; ++G__43052__i;}
  r = new cljs.core.IndexedSeq(G__43052__a,0,null);
} 
return G__43051__delegate.call(this,_,r);};
G__43051.cljs$lang$maxFixedArity = 1;
G__43051.cljs$lang$applyTo = (function (arglist__43053){
var _ = cljs.core.first(arglist__43053);
var r = cljs.core.rest(arglist__43053);
return G__43051__delegate(_,r);
});
G__43051.cljs$core$IFn$_invoke$arity$variadic = G__43051__delegate;
return G__43051;
})()
);
oc.web.ws.notify_client.event_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("chsk","ws-ping","chsk/ws-ping",191675304),(function() { 
var G__43055__delegate = function (_,r){
var c__27167__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__27168__auto__ = (function (){var switch__27075__auto__ = (function (state_42881){
var state_val_42882 = (state_42881[(1)]);
if((state_val_42882 === (1))){
var inst_42875 = [new cljs.core.Keyword(null,"topic","topic",-1960480691)];
var inst_42876 = [new cljs.core.Keyword("chsk","ws-ping","chsk/ws-ping",191675304)];
var inst_42877 = cljs.core.PersistentHashMap.fromArrays(inst_42875,inst_42876);
var state_42881__$1 = state_42881;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_42881__$1,(2),oc.web.ws.notify_client.ch_pub,inst_42877);
} else {
if((state_val_42882 === (2))){
var inst_42879 = (state_42881[(2)]);
var state_42881__$1 = state_42881;
return cljs.core.async.impl.ioc_helpers.return_chan(state_42881__$1,inst_42879);
} else {
return null;
}
}
});
return (function() {
var oc$web$ws$notify_client$state_machine__27076__auto__ = null;
var oc$web$ws$notify_client$state_machine__27076__auto____0 = (function (){
var statearr_42888 = [null,null,null,null,null,null,null];
(statearr_42888[(0)] = oc$web$ws$notify_client$state_machine__27076__auto__);

(statearr_42888[(1)] = (1));

return statearr_42888;
});
var oc$web$ws$notify_client$state_machine__27076__auto____1 = (function (state_42881){
while(true){
var ret_value__27077__auto__ = (function (){try{while(true){
var result__27078__auto__ = switch__27075__auto__(state_42881);
if(cljs.core.keyword_identical_QMARK_(result__27078__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__27078__auto__;
}
break;
}
}catch (e42889){var ex__27079__auto__ = e42889;
var statearr_42890_43056 = state_42881;
(statearr_42890_43056[(2)] = ex__27079__auto__);


if(cljs.core.seq((state_42881[(4)]))){
var statearr_42891_43057 = state_42881;
(statearr_42891_43057[(1)] = cljs.core.first((state_42881[(4)])));

} else {
throw ex__27079__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__27077__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__43058 = state_42881;
state_42881 = G__43058;
continue;
} else {
return ret_value__27077__auto__;
}
break;
}
});
oc$web$ws$notify_client$state_machine__27076__auto__ = function(state_42881){
switch(arguments.length){
case 0:
return oc$web$ws$notify_client$state_machine__27076__auto____0.call(this);
case 1:
return oc$web$ws$notify_client$state_machine__27076__auto____1.call(this,state_42881);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
oc$web$ws$notify_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$0 = oc$web$ws$notify_client$state_machine__27076__auto____0;
oc$web$ws$notify_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$1 = oc$web$ws$notify_client$state_machine__27076__auto____1;
return oc$web$ws$notify_client$state_machine__27076__auto__;
})()
})();
var state__27169__auto__ = (function (){var statearr_42897 = f__27168__auto__();
(statearr_42897[(6)] = c__27167__auto__);

return statearr_42897;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__27169__auto__);
}));

return c__27167__auto__;
};
var G__43055 = function (_,var_args){
var r = null;
if (arguments.length > 1) {
var G__43059__i = 0, G__43059__a = new Array(arguments.length -  1);
while (G__43059__i < G__43059__a.length) {G__43059__a[G__43059__i] = arguments[G__43059__i + 1]; ++G__43059__i;}
  r = new cljs.core.IndexedSeq(G__43059__a,0,null);
} 
return G__43055__delegate.call(this,_,r);};
G__43055.cljs$lang$maxFixedArity = 1;
G__43055.cljs$lang$applyTo = (function (arglist__43060){
var _ = cljs.core.first(arglist__43060);
var r = cljs.core.rest(arglist__43060);
return G__43055__delegate(_,r);
});
G__43055.cljs$core$IFn$_invoke$arity$variadic = G__43055__delegate;
return G__43055;
})()
);
oc.web.ws.notify_client.event_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("user","notifications","user/notifications",1689372584),(function (_,body){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.notify-client",null,68,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Notifications list event",body], null);
}),null)),null,-2081234968);

var c__27167__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__27168__auto__ = (function (){var switch__27075__auto__ = (function (state_42909){
var state_val_42910 = (state_42909[(1)]);
if((state_val_42910 === (1))){
var inst_42903 = [new cljs.core.Keyword(null,"topic","topic",-1960480691),new cljs.core.Keyword(null,"data","data",-232669377)];
var inst_42904 = [new cljs.core.Keyword("user","notifications","user/notifications",1689372584),body];
var inst_42905 = cljs.core.PersistentHashMap.fromArrays(inst_42903,inst_42904);
var state_42909__$1 = state_42909;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_42909__$1,(2),oc.web.ws.notify_client.ch_pub,inst_42905);
} else {
if((state_val_42910 === (2))){
var inst_42907 = (state_42909[(2)]);
var state_42909__$1 = state_42909;
return cljs.core.async.impl.ioc_helpers.return_chan(state_42909__$1,inst_42907);
} else {
return null;
}
}
});
return (function() {
var oc$web$ws$notify_client$state_machine__27076__auto__ = null;
var oc$web$ws$notify_client$state_machine__27076__auto____0 = (function (){
var statearr_42917 = [null,null,null,null,null,null,null];
(statearr_42917[(0)] = oc$web$ws$notify_client$state_machine__27076__auto__);

(statearr_42917[(1)] = (1));

return statearr_42917;
});
var oc$web$ws$notify_client$state_machine__27076__auto____1 = (function (state_42909){
while(true){
var ret_value__27077__auto__ = (function (){try{while(true){
var result__27078__auto__ = switch__27075__auto__(state_42909);
if(cljs.core.keyword_identical_QMARK_(result__27078__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__27078__auto__;
}
break;
}
}catch (e42918){var ex__27079__auto__ = e42918;
var statearr_42919_43061 = state_42909;
(statearr_42919_43061[(2)] = ex__27079__auto__);


if(cljs.core.seq((state_42909[(4)]))){
var statearr_42920_43062 = state_42909;
(statearr_42920_43062[(1)] = cljs.core.first((state_42909[(4)])));

} else {
throw ex__27079__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__27077__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__43063 = state_42909;
state_42909 = G__43063;
continue;
} else {
return ret_value__27077__auto__;
}
break;
}
});
oc$web$ws$notify_client$state_machine__27076__auto__ = function(state_42909){
switch(arguments.length){
case 0:
return oc$web$ws$notify_client$state_machine__27076__auto____0.call(this);
case 1:
return oc$web$ws$notify_client$state_machine__27076__auto____1.call(this,state_42909);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
oc$web$ws$notify_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$0 = oc$web$ws$notify_client$state_machine__27076__auto____0;
oc$web$ws$notify_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$1 = oc$web$ws$notify_client$state_machine__27076__auto____1;
return oc$web$ws$notify_client$state_machine__27076__auto__;
})()
})();
var state__27169__auto__ = (function (){var statearr_42921 = f__27168__auto__();
(statearr_42921[(6)] = c__27167__auto__);

return statearr_42921;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__27169__auto__);
}));

return c__27167__auto__;
}));
oc.web.ws.notify_client.event_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("user","notification","user/notification",-217694338),(function (_,body){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.notify-client",null,73,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Live notification event",body], null);
}),null)),null,-1746672246);

var c__27167__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__27168__auto__ = (function (){var switch__27075__auto__ = (function (state_42928){
var state_val_42929 = (state_42928[(1)]);
if((state_val_42929 === (1))){
var inst_42922 = [new cljs.core.Keyword(null,"topic","topic",-1960480691),new cljs.core.Keyword(null,"data","data",-232669377)];
var inst_42923 = [new cljs.core.Keyword("user","notification","user/notification",-217694338),body];
var inst_42924 = cljs.core.PersistentHashMap.fromArrays(inst_42922,inst_42923);
var state_42928__$1 = state_42928;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_42928__$1,(2),oc.web.ws.notify_client.ch_pub,inst_42924);
} else {
if((state_val_42929 === (2))){
var inst_42926 = (state_42928[(2)]);
var state_42928__$1 = state_42928;
return cljs.core.async.impl.ioc_helpers.return_chan(state_42928__$1,inst_42926);
} else {
return null;
}
}
});
return (function() {
var oc$web$ws$notify_client$state_machine__27076__auto__ = null;
var oc$web$ws$notify_client$state_machine__27076__auto____0 = (function (){
var statearr_42955 = [null,null,null,null,null,null,null];
(statearr_42955[(0)] = oc$web$ws$notify_client$state_machine__27076__auto__);

(statearr_42955[(1)] = (1));

return statearr_42955;
});
var oc$web$ws$notify_client$state_machine__27076__auto____1 = (function (state_42928){
while(true){
var ret_value__27077__auto__ = (function (){try{while(true){
var result__27078__auto__ = switch__27075__auto__(state_42928);
if(cljs.core.keyword_identical_QMARK_(result__27078__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__27078__auto__;
}
break;
}
}catch (e42956){var ex__27079__auto__ = e42956;
var statearr_42957_43064 = state_42928;
(statearr_42957_43064[(2)] = ex__27079__auto__);


if(cljs.core.seq((state_42928[(4)]))){
var statearr_42958_43065 = state_42928;
(statearr_42958_43065[(1)] = cljs.core.first((state_42928[(4)])));

} else {
throw ex__27079__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__27077__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__43066 = state_42928;
state_42928 = G__43066;
continue;
} else {
return ret_value__27077__auto__;
}
break;
}
});
oc$web$ws$notify_client$state_machine__27076__auto__ = function(state_42928){
switch(arguments.length){
case 0:
return oc$web$ws$notify_client$state_machine__27076__auto____0.call(this);
case 1:
return oc$web$ws$notify_client$state_machine__27076__auto____1.call(this,state_42928);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
oc$web$ws$notify_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$0 = oc$web$ws$notify_client$state_machine__27076__auto____0;
oc$web$ws$notify_client$state_machine__27076__auto__.cljs$core$IFn$_invoke$arity$1 = oc$web$ws$notify_client$state_machine__27076__auto____1;
return oc$web$ws$notify_client$state_machine__27076__auto__;
})()
})();
var state__27169__auto__ = (function (){var statearr_42959 = f__27168__auto__();
(statearr_42959[(6)] = c__27167__auto__);

return statearr_42959;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__27169__auto__);
}));

return c__27167__auto__;
}));
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.ws !== 'undefined') && (typeof oc.web.ws.notify_client !== 'undefined') && (typeof oc.web.ws.notify_client._event_msg_handler !== 'undefined')){
} else {
/**
 * Multimethod to handle Sente `event-msg`s
 */
oc.web.ws.notify_client._event_msg_handler = (function (){var method_table__4619__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var prefer_table__4620__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var method_cache__4621__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var cached_hierarchy__4622__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var hierarchy__4623__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$3(cljs.core.PersistentArrayMap.EMPTY,new cljs.core.Keyword(null,"hierarchy","hierarchy",-1053470341),(function (){var fexpr__42963 = cljs.core.get_global_hierarchy;
return (fexpr__42963.cljs$core$IFn$_invoke$arity$0 ? fexpr__42963.cljs$core$IFn$_invoke$arity$0() : fexpr__42963.call(null));
})());
return (new cljs.core.MultiFn(cljs.core.symbol.cljs$core$IFn$_invoke$arity$2("oc.web.ws.notify-client","-event-msg-handler"),new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"default","default",-1987822328),hierarchy__4623__auto__,method_table__4619__auto__,prefer_table__4620__auto__,method_cache__4621__auto__,cached_hierarchy__4622__auto__));
})();
}
/**
 * Wraps `-event-msg-handler` with logging, error catching, etc.
 */
oc.web.ws.notify_client.event_msg_handler = (function oc$web$ws$notify_client$event_msg_handler(p__42964){
var map__42965 = p__42964;
var map__42965__$1 = (((((!((map__42965 == null))))?(((((map__42965.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42965.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42965):map__42965);
var ev_msg = map__42965__$1;
var id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42965__$1,new cljs.core.Keyword(null,"id","id",-1388402092));
var _QMARK_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42965__$1,new cljs.core.Keyword(null,"?data","?data",-9471433));
var event = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42965__$1,new cljs.core.Keyword(null,"event","event",301435442));
return oc.web.ws.notify_client._event_msg_handler.cljs$core$IFn$_invoke$arity$1(ev_msg);
});
oc.web.ws.notify_client._event_msg_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"default","default",-1987822328),(function (p__42970){
var map__42971 = p__42970;
var map__42971__$1 = (((((!((map__42971 == null))))?(((((map__42971.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42971.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42971):map__42971);
var ev_msg = map__42971__$1;
var event = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42971__$1,new cljs.core.Keyword(null,"event","event",301435442));
return taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"warn","warn",-436710552),"oc.web.ws.notify-client",null,91,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Unhandled event: ",event], null);
}),null)),null,-1644958394);
}));
oc.web.ws.notify_client._event_msg_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("chsk","state","chsk/state",-1991397620),(function (p__42977){
var map__42978 = p__42977;
var map__42978__$1 = (((((!((map__42978 == null))))?(((((map__42978.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42978.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42978):map__42978);
var ev_msg = map__42978__$1;
var _QMARK_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42978__$1,new cljs.core.Keyword(null,"?data","?data",-9471433));
var vec__42991 = (function (){var e = (function (){try{if(cljs.core.vector_QMARK_(_QMARK_data)){
return null;
} else {
return taoensso.truss.impl._dummy_error;
}
}catch (e42994){if((e42994 instanceof Error)){
var e = e42994;
return e;
} else {
throw e42994;

}
}})();
if((e == null)){
return _QMARK_data;
} else {
return taoensso.truss.impl._invar_violation_BANG_(true,"oc.web.ws.notify-client",95,"(vector? ?data)",_QMARK_data,e,null);
}
})();
var old_state_map = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42991,(0),null);
var new_state_map = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42991,(1),null);
if(cljs.core.truth_(new cljs.core.Keyword(null,"first-open?","first-open?",396686530).cljs$core$IFn$_invoke$arity$1(new_state_map))){
return taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.notify-client",null,97,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Channel socket successfully established!: ",new_state_map], null);
}),null)),null,-799015298);
} else {
return taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.notify-client",null,98,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Channel socket state change: ",new_state_map], null);
}),null)),null,633350765);
}
}));
oc.web.ws.notify_client._event_msg_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("chsk","recv","chsk/recv",561097091),(function (p__42998){
var map__42999 = p__42998;
var map__42999__$1 = (((((!((map__42999 == null))))?(((((map__42999.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42999.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42999):map__42999);
var ev_msg = map__42999__$1;
var _QMARK_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42999__$1,new cljs.core.Keyword(null,"?data","?data",-9471433));
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.notify-client",null,102,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Push event from server: ",_QMARK_data], null);
}),null)),null,-246722731);

return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(oc.web.ws.notify_client.event_handler,_QMARK_data);
}));
oc.web.ws.notify_client._event_msg_handler.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("chsk","handshake","chsk/handshake",64910686),(function (p__43001){
var map__43002 = p__43001;
var map__43002__$1 = (((((!((map__43002 == null))))?(((((map__43002.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43002.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43002):map__43002);
var ev_msg = map__43002__$1;
var _QMARK_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43002__$1,new cljs.core.Keyword(null,"?data","?data",-9471433));
var auth_cb_43076 = cljs.core.partial.cljs$core$IFn$_invoke$arity$variadic(oc.web.ws.utils.auth_check,"Notify",oc.web.ws.notify_client.ch_state,oc.web.ws.notify_client.chsk_send_BANG_,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([oc.web.ws.notify_client.channelsk,oc.web.actions.jwt.jwt_refresh,(function (){
var G__43004 = cljs.core.deref(oc.web.ws.notify_client.last_ws_link);
var G__43005 = oc.web.lib.jwt.user_id();
return (oc.web.ws.notify_client.reconnect.cljs$core$IFn$_invoke$arity$2 ? oc.web.ws.notify_client.reconnect.cljs$core$IFn$_invoke$arity$2(G__43004,G__43005) : oc.web.ws.notify_client.reconnect.call(null,G__43004,G__43005));
}),oc.web.ws.notify_client.notifications_watch], 0));
oc.web.ws.utils.post_handshake_auth(oc.web.actions.jwt.jwt_refresh,(function (){
return oc.web.ws.notify_client.send_BANG_.cljs$core$IFn$_invoke$arity$variadic(oc.web.ws.notify_client.chsk_send_BANG_,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("auth","jwt","auth/jwt",1503073961),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"jwt","jwt",1504015441),oc.web.lib.jwt.jwt()], null)], null),(60000),auth_cb_43076], 0));
}));

var vec__43006 = _QMARK_data;
var _QMARK_uid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43006,(0),null);
var _QMARK_csrf_token = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43006,(1),null);
var _QMARK_handshake_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43006,(2),null);
return taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.notify-client",null,114,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Handshake:",_QMARK_uid,_QMARK_csrf_token,_QMARK_handshake_data], null);
}),null)),null,-1949970895);
}));
oc.web.ws.notify_client.stop_router_BANG_ = (function oc$web$ws$notify_client$stop_router_BANG_(){
if(cljs.core.truth_(cljs.core.deref(oc.web.ws.notify_client.channelsk))){
taoensso.sente.chsk_disconnect_BANG_(cljs.core.deref(oc.web.ws.notify_client.channelsk));

return taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.ws.notify-client",null,121,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Connection closed"], null);
}),null)),null,8982771);
} else {
return null;
}
});
oc.web.ws.notify_client.start_router_BANG_ = (function oc$web$ws$notify_client$start_router_BANG_(){
taoensso.sente.start_client_chsk_router_BANG_(cljs.core.deref(oc.web.ws.notify_client.ch_chsk),oc.web.ws.notify_client.event_msg_handler);

taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.ws.notify-client",null,125,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Connection estabilished"], null);
}),null)),null,310857742);

return oc.web.ws.utils.reconnected(oc.web.ws.notify_client.last_interval,"Notify",oc.web.ws.notify_client.chsk_send_BANG_,oc.web.ws.notify_client.ch_state,(function (){
var G__43019 = cljs.core.deref(oc.web.ws.notify_client.last_ws_link);
var G__43020 = oc.web.lib.jwt.user_id();
return (oc.web.ws.notify_client.reconnect.cljs$core$IFn$_invoke$arity$2 ? oc.web.ws.notify_client.reconnect.cljs$core$IFn$_invoke$arity$2(G__43019,G__43020) : oc.web.ws.notify_client.reconnect.call(null,G__43019,G__43020));
}));
});
/**
 * Connect or reconnect the WebSocket connection to the notify service
 */
oc.web.ws.notify_client.reconnect = (function oc$web$ws$notify_client$reconnect(ws_link,uid){
var ws_uri = goog.Uri.parse(new cljs.core.Keyword(null,"href","href",-793805698).cljs$core$IFn$_invoke$arity$1(ws_link));
var ws_domain = [cljs.core.str.cljs$core$IFn$_invoke$arity$1(ws_uri.getDomain()),(cljs.core.truth_(ws_uri.getPort())?[":",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ws_uri.getPort())].join(''):null)].join('');
var ws_org_path = ws_uri.getPath();
if(((cljs.core.not(cljs.core.deref(oc.web.ws.notify_client.ch_state))) || (cljs.core.not(new cljs.core.Keyword(null,"open?","open?",1238443125).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(cljs.core.deref(oc.web.ws.notify_client.ch_state))))))){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.ws.notify-client",null,140,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Reconnect for",new cljs.core.Keyword(null,"href","href",-793805698).cljs$core$IFn$_invoke$arity$1(ws_link),"and",uid,"current state:",cljs.core.deref(oc.web.ws.notify_client.ch_state)], null);
}),null)),null,1543917335);

if(cljs.core.truth_((function (){var and__4115__auto__ = cljs.core.deref(oc.web.ws.notify_client.ch_state);
if(cljs.core.truth_(and__4115__auto__)){
return new cljs.core.Keyword(null,"open?","open?",1238443125).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(cljs.core.deref(oc.web.ws.notify_client.ch_state)));
} else {
return and__4115__auto__;
}
})())){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.ws.notify-client",null,144,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Closing previous connection"], null);
}),null)),null,2007015771);

oc.web.ws.notify_client.stop_router_BANG_();
} else {
}

taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.ws.notify-client",null,146,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Attempting notification service connection to:",ws_domain], null);
}),null)),null,-811195390);

var map__43033 = (function (){var G__43034 = ws_org_path;
var G__43035 = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"auto","auto",-566279492),new cljs.core.Keyword(null,"host","host",-1558485167),ws_domain,new cljs.core.Keyword(null,"protocol","protocol",652470118),((oc.web.local_settings.jwt_cookie_secure)?new cljs.core.Keyword(null,"https","https",-1983909665):new cljs.core.Keyword(null,"http","http",382524695)),new cljs.core.Keyword(null,"packer","packer",66077544),new cljs.core.Keyword(null,"edn","edn",1317840885),new cljs.core.Keyword(null,"uid","uid",-1447769400),uid,new cljs.core.Keyword(null,"params","params",710516235),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"user-id","user-id",-206822291),uid], null)], null);
return (taoensso.sente.make_channel_socket_BANG_.cljs$core$IFn$_invoke$arity$2 ? taoensso.sente.make_channel_socket_BANG_.cljs$core$IFn$_invoke$arity$2(G__43034,G__43035) : taoensso.sente.make_channel_socket_BANG_.call(null,G__43034,G__43035));
})();
var map__43033__$1 = (((((!((map__43033 == null))))?(((((map__43033.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43033.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43033):map__43033);
var x = map__43033__$1;
var chsk = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43033__$1,new cljs.core.Keyword(null,"chsk","chsk",-863703081));
var ch_recv = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43033__$1,new cljs.core.Keyword(null,"ch-recv","ch-recv",-990916861));
var send_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43033__$1,new cljs.core.Keyword(null,"send-fn","send-fn",351002041));
var state = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43033__$1,new cljs.core.Keyword(null,"state","state",-1988618099));
cljs.core.reset_BANG_(oc.web.ws.notify_client.channelsk,chsk);

cljs.core.reset_BANG_(oc.web.ws.notify_client.ch_chsk,ch_recv);

cljs.core.reset_BANG_(oc.web.ws.notify_client.chsk_send_BANG_,send_fn);

if(cljs.core.truth_(cljs.core.deref(oc.web.ws.notify_client.ch_state))){
cljs.core.remove_watch(cljs.core.deref(oc.web.ws.notify_client.ch_state),new cljs.core.Keyword(null,"notify-client-state-watcher","notify-client-state-watcher",482001541));
} else {
}

cljs.core.reset_BANG_(oc.web.ws.notify_client.ch_state,state);

cljs.core.add_watch(cljs.core.deref(oc.web.ws.notify_client.ch_state),new cljs.core.Keyword(null,"notify-client-state-watcher","notify-client-state-watcher",482001541),(function (key,a,old_state,new_state){
return cljs.core.reset_BANG_(oc.web.utils.ws_client_ids.notify_client_id,new cljs.core.Keyword(null,"uid","uid",-1447769400).cljs$core$IFn$_invoke$arity$1(new_state));
}));

return oc.web.ws.notify_client.start_router_BANG_();
} else {
return oc.web.ws.notify_client.notifications_watch();
}
});

//# sourceMappingURL=oc.web.ws.notify_client.js.map
