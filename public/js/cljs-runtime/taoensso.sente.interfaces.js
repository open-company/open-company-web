goog.provide('taoensso.sente.interfaces');

/**
 * @interface
 */
taoensso.sente.interfaces.IServerChan = function(){};

var taoensso$sente$interfaces$IServerChan$sch_open_QMARK_$dyn_43544 = (function (sch){
var x__4428__auto__ = (((sch == null))?null:sch);
var m__4429__auto__ = (taoensso.sente.interfaces.sch_open_QMARK_[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$1 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$1(sch) : m__4429__auto__.call(null,sch));
} else {
var m__4426__auto__ = (taoensso.sente.interfaces.sch_open_QMARK_["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$1 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$1(sch) : m__4426__auto__.call(null,sch));
} else {
throw cljs.core.missing_protocol("IServerChan.sch-open?",sch);
}
}
});
/**
 * Returns true iff the channel is currently open.
 */
taoensso.sente.interfaces.sch_open_QMARK_ = (function taoensso$sente$interfaces$sch_open_QMARK_(sch){
if((((!((sch == null)))) && ((!((sch.taoensso$sente$interfaces$IServerChan$sch_open_QMARK_$arity$1 == null)))))){
return sch.taoensso$sente$interfaces$IServerChan$sch_open_QMARK_$arity$1(sch);
} else {
return taoensso$sente$interfaces$IServerChan$sch_open_QMARK_$dyn_43544(sch);
}
});

var taoensso$sente$interfaces$IServerChan$sch_close_BANG_$dyn_43545 = (function (sch){
var x__4428__auto__ = (((sch == null))?null:sch);
var m__4429__auto__ = (taoensso.sente.interfaces.sch_close_BANG_[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$1 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$1(sch) : m__4429__auto__.call(null,sch));
} else {
var m__4426__auto__ = (taoensso.sente.interfaces.sch_close_BANG_["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$1 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$1(sch) : m__4426__auto__.call(null,sch));
} else {
throw cljs.core.missing_protocol("IServerChan.sch-close!",sch);
}
}
});
/**
 * If the channel is open when called: closes the channel and returns true.
 *  Otherwise noops and returns false.
 */
taoensso.sente.interfaces.sch_close_BANG_ = (function taoensso$sente$interfaces$sch_close_BANG_(sch){
if((((!((sch == null)))) && ((!((sch.taoensso$sente$interfaces$IServerChan$sch_close_BANG_$arity$1 == null)))))){
return sch.taoensso$sente$interfaces$IServerChan$sch_close_BANG_$arity$1(sch);
} else {
return taoensso$sente$interfaces$IServerChan$sch_close_BANG_$dyn_43545(sch);
}
});

var taoensso$sente$interfaces$IServerChan$sch_send_BANG_$dyn_43546 = (function (sch,websocket_QMARK_,msg){
var x__4428__auto__ = (((sch == null))?null:sch);
var m__4429__auto__ = (taoensso.sente.interfaces.sch_send_BANG_[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$3 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$3(sch,websocket_QMARK_,msg) : m__4429__auto__.call(null,sch,websocket_QMARK_,msg));
} else {
var m__4426__auto__ = (taoensso.sente.interfaces.sch_send_BANG_["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$3 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$3(sch,websocket_QMARK_,msg) : m__4426__auto__.call(null,sch,websocket_QMARK_,msg));
} else {
throw cljs.core.missing_protocol("IServerChan.sch-send!",sch);
}
}
});
/**
 * If the channel is open when called: sends a message over channel and
 *  returns true. Otherwise noops and returns false.
 */
taoensso.sente.interfaces.sch_send_BANG_ = (function taoensso$sente$interfaces$sch_send_BANG_(sch,websocket_QMARK_,msg){
if((((!((sch == null)))) && ((!((sch.taoensso$sente$interfaces$IServerChan$sch_send_BANG_$arity$3 == null)))))){
return sch.taoensso$sente$interfaces$IServerChan$sch_send_BANG_$arity$3(sch,websocket_QMARK_,msg);
} else {
return taoensso$sente$interfaces$IServerChan$sch_send_BANG_$dyn_43546(sch,websocket_QMARK_,msg);
}
});


/**
 * @interface
 */
taoensso.sente.interfaces.IServerChanAdapter = function(){};

var taoensso$sente$interfaces$IServerChanAdapter$ring_req__GT_server_ch_resp$dyn_43547 = (function (sch_adapter,ring_req,callbacks_map){
var x__4428__auto__ = (((sch_adapter == null))?null:sch_adapter);
var m__4429__auto__ = (taoensso.sente.interfaces.ring_req__GT_server_ch_resp[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$3 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$3(sch_adapter,ring_req,callbacks_map) : m__4429__auto__.call(null,sch_adapter,ring_req,callbacks_map));
} else {
var m__4426__auto__ = (taoensso.sente.interfaces.ring_req__GT_server_ch_resp["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$3 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$3(sch_adapter,ring_req,callbacks_map) : m__4426__auto__.call(null,sch_adapter,ring_req,callbacks_map));
} else {
throw cljs.core.missing_protocol("IServerChanAdapter.ring-req->server-ch-resp",sch_adapter);
}
}
});
/**
 * Given a Ring request (WebSocket handshake or Ajax GET/POST), returns
 *  a Ring response map with a web-server-specific channel :body that
 *  implements Sente's IServerChan protocol.
 * 
 *  Configures channel callbacks with a callbacks map using keys:
 *    :on-open  - (fn [server-ch websocket?]) called exactly once after
 *                channel is available for sending.
 *    :on-close - (fn [server-ch websocket? status]) called exactly once
 *                after channel is closed for any cause, incl. an explicit
 *                call to `close!`. `status` type is currently undefined.
 *    :on-msg   - (fn [server-ch websocket? msg]) called for each String or
 *                byte[] message received from client.
 *    :on-error - (fn [server-ch websocket? error]) currently unused.
 */
taoensso.sente.interfaces.ring_req__GT_server_ch_resp = (function taoensso$sente$interfaces$ring_req__GT_server_ch_resp(sch_adapter,ring_req,callbacks_map){
if((((!((sch_adapter == null)))) && ((!((sch_adapter.taoensso$sente$interfaces$IServerChanAdapter$ring_req__GT_server_ch_resp$arity$3 == null)))))){
return sch_adapter.taoensso$sente$interfaces$IServerChanAdapter$ring_req__GT_server_ch_resp$arity$3(sch_adapter,ring_req,callbacks_map);
} else {
return taoensso$sente$interfaces$IServerChanAdapter$ring_req__GT_server_ch_resp$dyn_43547(sch_adapter,ring_req,callbacks_map);
}
});


/**
 * Extension pt. for client<->server comms data un/packers:
 *   arbitrary Clojure data <-> serialized strings.
 * @interface
 */
taoensso.sente.interfaces.IPacker = function(){};

var taoensso$sente$interfaces$IPacker$pack$dyn_43548 = (function (_,x){
var x__4428__auto__ = (((_ == null))?null:_);
var m__4429__auto__ = (taoensso.sente.interfaces.pack[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$2(_,x) : m__4429__auto__.call(null,_,x));
} else {
var m__4426__auto__ = (taoensso.sente.interfaces.pack["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$2(_,x) : m__4426__auto__.call(null,_,x));
} else {
throw cljs.core.missing_protocol("IPacker.pack",_);
}
}
});
taoensso.sente.interfaces.pack = (function taoensso$sente$interfaces$pack(_,x){
if((((!((_ == null)))) && ((!((_.taoensso$sente$interfaces$IPacker$pack$arity$2 == null)))))){
return _.taoensso$sente$interfaces$IPacker$pack$arity$2(_,x);
} else {
return taoensso$sente$interfaces$IPacker$pack$dyn_43548(_,x);
}
});

var taoensso$sente$interfaces$IPacker$unpack$dyn_43549 = (function (_,x){
var x__4428__auto__ = (((_ == null))?null:_);
var m__4429__auto__ = (taoensso.sente.interfaces.unpack[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$2(_,x) : m__4429__auto__.call(null,_,x));
} else {
var m__4426__auto__ = (taoensso.sente.interfaces.unpack["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$2(_,x) : m__4426__auto__.call(null,_,x));
} else {
throw cljs.core.missing_protocol("IPacker.unpack",_);
}
}
});
taoensso.sente.interfaces.unpack = (function taoensso$sente$interfaces$unpack(_,x){
if((((!((_ == null)))) && ((!((_.taoensso$sente$interfaces$IPacker$unpack$arity$2 == null)))))){
return _.taoensso$sente$interfaces$IPacker$unpack$arity$2(_,x);
} else {
return taoensso$sente$interfaces$IPacker$unpack$dyn_43549(_,x);
}
});


//# sourceMappingURL=taoensso.sente.interfaces.js.map
