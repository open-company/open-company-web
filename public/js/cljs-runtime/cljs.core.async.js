goog.provide('cljs.core.async');
cljs.core.async.fn_handler = (function cljs$core$async$fn_handler(var_args){
var G__31179 = arguments.length;
switch (G__31179) {
case 1:
return cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$1 = (function (f){
return cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$2(f,true);
}));

(cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$2 = (function (f,blockable){
if((typeof cljs !== 'undefined') && (typeof cljs.core !== 'undefined') && (typeof cljs.core.async !== 'undefined') && (typeof cljs.core.async.t_cljs$core$async31181 !== 'undefined')){
} else {

/**
* @constructor
 * @implements {cljs.core.async.impl.protocols.Handler}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async31181 = (function (f,blockable,meta31182){
this.f = f;
this.blockable = blockable;
this.meta31182 = meta31182;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(cljs.core.async.t_cljs$core$async31181.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_31183,meta31182__$1){
var self__ = this;
var _31183__$1 = this;
return (new cljs.core.async.t_cljs$core$async31181(self__.f,self__.blockable,meta31182__$1));
}));

(cljs.core.async.t_cljs$core$async31181.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_31183){
var self__ = this;
var _31183__$1 = this;
return self__.meta31182;
}));

(cljs.core.async.t_cljs$core$async31181.prototype.cljs$core$async$impl$protocols$Handler$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async31181.prototype.cljs$core$async$impl$protocols$Handler$active_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return true;
}));

(cljs.core.async.t_cljs$core$async31181.prototype.cljs$core$async$impl$protocols$Handler$blockable_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return self__.blockable;
}));

(cljs.core.async.t_cljs$core$async31181.prototype.cljs$core$async$impl$protocols$Handler$commit$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return self__.f;
}));

(cljs.core.async.t_cljs$core$async31181.getBasis = (function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"f","f",43394975,null),new cljs.core.Symbol(null,"blockable","blockable",-28395259,null),new cljs.core.Symbol(null,"meta31182","meta31182",-222321250,null)], null);
}));

(cljs.core.async.t_cljs$core$async31181.cljs$lang$type = true);

(cljs.core.async.t_cljs$core$async31181.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async31181");

(cljs.core.async.t_cljs$core$async31181.cljs$lang$ctorPrWriter = (function (this__4369__auto__,writer__4370__auto__,opt__4371__auto__){
return cljs.core._write(writer__4370__auto__,"cljs.core.async/t_cljs$core$async31181");
}));

/**
 * Positional factory function for cljs.core.async/t_cljs$core$async31181.
 */
cljs.core.async.__GT_t_cljs$core$async31181 = (function cljs$core$async$__GT_t_cljs$core$async31181(f__$1,blockable__$1,meta31182){
return (new cljs.core.async.t_cljs$core$async31181(f__$1,blockable__$1,meta31182));
});

}

return (new cljs.core.async.t_cljs$core$async31181(f,blockable,cljs.core.PersistentArrayMap.EMPTY));
}));

(cljs.core.async.fn_handler.cljs$lang$maxFixedArity = 2);

/**
 * Returns a fixed buffer of size n. When full, puts will block/park.
 */
cljs.core.async.buffer = (function cljs$core$async$buffer(n){
return cljs.core.async.impl.buffers.fixed_buffer(n);
});
/**
 * Returns a buffer of size n. When full, puts will complete but
 *   val will be dropped (no transfer).
 */
cljs.core.async.dropping_buffer = (function cljs$core$async$dropping_buffer(n){
return cljs.core.async.impl.buffers.dropping_buffer(n);
});
/**
 * Returns a buffer of size n. When full, puts will complete, and be
 *   buffered, but oldest elements in buffer will be dropped (not
 *   transferred).
 */
cljs.core.async.sliding_buffer = (function cljs$core$async$sliding_buffer(n){
return cljs.core.async.impl.buffers.sliding_buffer(n);
});
/**
 * Returns true if a channel created with buff will never block. That is to say,
 * puts into this buffer will never cause the buffer to be full. 
 */
cljs.core.async.unblocking_buffer_QMARK_ = (function cljs$core$async$unblocking_buffer_QMARK_(buff){
if((!((buff == null)))){
if(((false) || ((cljs.core.PROTOCOL_SENTINEL === buff.cljs$core$async$impl$protocols$UnblockingBuffer$)))){
return true;
} else {
if((!buff.cljs$lang$protocol_mask$partition$)){
return cljs.core.native_satisfies_QMARK_(cljs.core.async.impl.protocols.UnblockingBuffer,buff);
} else {
return false;
}
}
} else {
return cljs.core.native_satisfies_QMARK_(cljs.core.async.impl.protocols.UnblockingBuffer,buff);
}
});
/**
 * Creates a channel with an optional buffer, an optional transducer (like (map f),
 *   (filter p) etc or a composition thereof), and an optional exception handler.
 *   If buf-or-n is a number, will create and use a fixed buffer of that size. If a
 *   transducer is supplied a buffer must be specified. ex-handler must be a
 *   fn of one argument - if an exception occurs during transformation it will be called
 *   with the thrown value as an argument, and any non-nil return value will be placed
 *   in the channel.
 */
cljs.core.async.chan = (function cljs$core$async$chan(var_args){
var G__31191 = arguments.length;
switch (G__31191) {
case 0:
return cljs.core.async.chan.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return cljs.core.async.chan.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.chan.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(cljs.core.async.chan.cljs$core$IFn$_invoke$arity$0 = (function (){
return cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(null);
}));

(cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1 = (function (buf_or_n){
return cljs.core.async.chan.cljs$core$IFn$_invoke$arity$3(buf_or_n,null,null);
}));

(cljs.core.async.chan.cljs$core$IFn$_invoke$arity$2 = (function (buf_or_n,xform){
return cljs.core.async.chan.cljs$core$IFn$_invoke$arity$3(buf_or_n,xform,null);
}));

(cljs.core.async.chan.cljs$core$IFn$_invoke$arity$3 = (function (buf_or_n,xform,ex_handler){
var buf_or_n__$1 = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(buf_or_n,(0)))?null:buf_or_n);
if(cljs.core.truth_(xform)){
if(cljs.core.truth_(buf_or_n__$1)){
} else {
throw (new Error(["Assert failed: ","buffer must be supplied when transducer is","\n","buf-or-n"].join('')));
}
} else {
}

return cljs.core.async.impl.channels.chan.cljs$core$IFn$_invoke$arity$3(((typeof buf_or_n__$1 === 'number')?cljs.core.async.buffer(buf_or_n__$1):buf_or_n__$1),xform,ex_handler);
}));

(cljs.core.async.chan.cljs$lang$maxFixedArity = 3);

/**
 * Creates a promise channel with an optional transducer, and an optional
 *   exception-handler. A promise channel can take exactly one value that consumers
 *   will receive. Once full, puts complete but val is dropped (no transfer).
 *   Consumers will block until either a value is placed in the channel or the
 *   channel is closed. See chan for the semantics of xform and ex-handler.
 */
cljs.core.async.promise_chan = (function cljs$core$async$promise_chan(var_args){
var G__31195 = arguments.length;
switch (G__31195) {
case 0:
return cljs.core.async.promise_chan.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return cljs.core.async.promise_chan.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return cljs.core.async.promise_chan.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(cljs.core.async.promise_chan.cljs$core$IFn$_invoke$arity$0 = (function (){
return cljs.core.async.promise_chan.cljs$core$IFn$_invoke$arity$1(null);
}));

(cljs.core.async.promise_chan.cljs$core$IFn$_invoke$arity$1 = (function (xform){
return cljs.core.async.promise_chan.cljs$core$IFn$_invoke$arity$2(xform,null);
}));

(cljs.core.async.promise_chan.cljs$core$IFn$_invoke$arity$2 = (function (xform,ex_handler){
return cljs.core.async.chan.cljs$core$IFn$_invoke$arity$3(cljs.core.async.impl.buffers.promise_buffer(),xform,ex_handler);
}));

(cljs.core.async.promise_chan.cljs$lang$maxFixedArity = 2);

/**
 * Returns a channel that will close after msecs
 */
cljs.core.async.timeout = (function cljs$core$async$timeout(msecs){
return cljs.core.async.impl.timers.timeout(msecs);
});
/**
 * takes a val from port. Must be called inside a (go ...) block. Will
 *   return nil if closed. Will park if nothing is available.
 *   Returns true unless port is already closed
 */
cljs.core.async._LT__BANG_ = (function cljs$core$async$_LT__BANG_(port){
throw (new Error("<! used not in (go ...) block"));
});
/**
 * Asynchronously takes a val from port, passing to fn1. Will pass nil
 * if closed. If on-caller? (default true) is true, and value is
 * immediately available, will call fn1 on calling thread.
 * Returns nil.
 */
cljs.core.async.take_BANG_ = (function cljs$core$async$take_BANG_(var_args){
var G__31201 = arguments.length;
switch (G__31201) {
case 2:
return cljs.core.async.take_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.take_BANG_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(cljs.core.async.take_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (port,fn1){
return cljs.core.async.take_BANG_.cljs$core$IFn$_invoke$arity$3(port,fn1,true);
}));

(cljs.core.async.take_BANG_.cljs$core$IFn$_invoke$arity$3 = (function (port,fn1,on_caller_QMARK_){
var ret = cljs.core.async.impl.protocols.take_BANG_(port,cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$1(fn1));
if(cljs.core.truth_(ret)){
var val_33427 = cljs.core.deref(ret);
if(cljs.core.truth_(on_caller_QMARK_)){
(fn1.cljs$core$IFn$_invoke$arity$1 ? fn1.cljs$core$IFn$_invoke$arity$1(val_33427) : fn1.call(null,val_33427));
} else {
cljs.core.async.impl.dispatch.run((function (){
return (fn1.cljs$core$IFn$_invoke$arity$1 ? fn1.cljs$core$IFn$_invoke$arity$1(val_33427) : fn1.call(null,val_33427));
}));
}
} else {
}

return null;
}));

(cljs.core.async.take_BANG_.cljs$lang$maxFixedArity = 3);

cljs.core.async.nop = (function cljs$core$async$nop(_){
return null;
});
cljs.core.async.fhnop = cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$1(cljs.core.async.nop);
/**
 * puts a val into port. nil values are not allowed. Must be called
 *   inside a (go ...) block. Will park if no buffer space is available.
 *   Returns true unless port is already closed.
 */
cljs.core.async._GT__BANG_ = (function cljs$core$async$_GT__BANG_(port,val){
throw (new Error(">! used not in (go ...) block"));
});
/**
 * Asynchronously puts a val into port, calling fn1 (if supplied) when
 * complete. nil values are not allowed. Will throw if closed. If
 * on-caller? (default true) is true, and the put is immediately
 * accepted, will call fn1 on calling thread.  Returns nil.
 */
cljs.core.async.put_BANG_ = (function cljs$core$async$put_BANG_(var_args){
var G__31207 = arguments.length;
switch (G__31207) {
case 2:
return cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 4:
return cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (port,val){
var temp__5733__auto__ = cljs.core.async.impl.protocols.put_BANG_(port,val,cljs.core.async.fhnop);
if(cljs.core.truth_(temp__5733__auto__)){
var ret = temp__5733__auto__;
return cljs.core.deref(ret);
} else {
return true;
}
}));

(cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$3 = (function (port,val,fn1){
return cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$4(port,val,fn1,true);
}));

(cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$4 = (function (port,val,fn1,on_caller_QMARK_){
var temp__5733__auto__ = cljs.core.async.impl.protocols.put_BANG_(port,val,cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$1(fn1));
if(cljs.core.truth_(temp__5733__auto__)){
var retb = temp__5733__auto__;
var ret = cljs.core.deref(retb);
if(cljs.core.truth_(on_caller_QMARK_)){
(fn1.cljs$core$IFn$_invoke$arity$1 ? fn1.cljs$core$IFn$_invoke$arity$1(ret) : fn1.call(null,ret));
} else {
cljs.core.async.impl.dispatch.run((function (){
return (fn1.cljs$core$IFn$_invoke$arity$1 ? fn1.cljs$core$IFn$_invoke$arity$1(ret) : fn1.call(null,ret));
}));
}

return ret;
} else {
return true;
}
}));

(cljs.core.async.put_BANG_.cljs$lang$maxFixedArity = 4);

cljs.core.async.close_BANG_ = (function cljs$core$async$close_BANG_(port){
return cljs.core.async.impl.protocols.close_BANG_(port);
});
cljs.core.async.random_array = (function cljs$core$async$random_array(n){
var a = (new Array(n));
var n__4613__auto___33429 = n;
var x_33430 = (0);
while(true){
if((x_33430 < n__4613__auto___33429)){
(a[x_33430] = x_33430);

var G__33431 = (x_33430 + (1));
x_33430 = G__33431;
continue;
} else {
}
break;
}

goog.array.shuffle(a);

return a;
});
cljs.core.async.alt_flag = (function cljs$core$async$alt_flag(){
var flag = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(true);
if((typeof cljs !== 'undefined') && (typeof cljs.core !== 'undefined') && (typeof cljs.core.async !== 'undefined') && (typeof cljs.core.async.t_cljs$core$async31208 !== 'undefined')){
} else {

/**
* @constructor
 * @implements {cljs.core.async.impl.protocols.Handler}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async31208 = (function (flag,meta31209){
this.flag = flag;
this.meta31209 = meta31209;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(cljs.core.async.t_cljs$core$async31208.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_31210,meta31209__$1){
var self__ = this;
var _31210__$1 = this;
return (new cljs.core.async.t_cljs$core$async31208(self__.flag,meta31209__$1));
}));

(cljs.core.async.t_cljs$core$async31208.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_31210){
var self__ = this;
var _31210__$1 = this;
return self__.meta31209;
}));

(cljs.core.async.t_cljs$core$async31208.prototype.cljs$core$async$impl$protocols$Handler$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async31208.prototype.cljs$core$async$impl$protocols$Handler$active_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.deref(self__.flag);
}));

(cljs.core.async.t_cljs$core$async31208.prototype.cljs$core$async$impl$protocols$Handler$blockable_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return true;
}));

(cljs.core.async.t_cljs$core$async31208.prototype.cljs$core$async$impl$protocols$Handler$commit$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
cljs.core.reset_BANG_(self__.flag,null);

return true;
}));

(cljs.core.async.t_cljs$core$async31208.getBasis = (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"flag","flag",-1565787888,null),new cljs.core.Symbol(null,"meta31209","meta31209",577914476,null)], null);
}));

(cljs.core.async.t_cljs$core$async31208.cljs$lang$type = true);

(cljs.core.async.t_cljs$core$async31208.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async31208");

(cljs.core.async.t_cljs$core$async31208.cljs$lang$ctorPrWriter = (function (this__4369__auto__,writer__4370__auto__,opt__4371__auto__){
return cljs.core._write(writer__4370__auto__,"cljs.core.async/t_cljs$core$async31208");
}));

/**
 * Positional factory function for cljs.core.async/t_cljs$core$async31208.
 */
cljs.core.async.__GT_t_cljs$core$async31208 = (function cljs$core$async$alt_flag_$___GT_t_cljs$core$async31208(flag__$1,meta31209){
return (new cljs.core.async.t_cljs$core$async31208(flag__$1,meta31209));
});

}

return (new cljs.core.async.t_cljs$core$async31208(flag,cljs.core.PersistentArrayMap.EMPTY));
});
cljs.core.async.alt_handler = (function cljs$core$async$alt_handler(flag,cb){
if((typeof cljs !== 'undefined') && (typeof cljs.core !== 'undefined') && (typeof cljs.core.async !== 'undefined') && (typeof cljs.core.async.t_cljs$core$async31211 !== 'undefined')){
} else {

/**
* @constructor
 * @implements {cljs.core.async.impl.protocols.Handler}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async31211 = (function (flag,cb,meta31212){
this.flag = flag;
this.cb = cb;
this.meta31212 = meta31212;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(cljs.core.async.t_cljs$core$async31211.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_31213,meta31212__$1){
var self__ = this;
var _31213__$1 = this;
return (new cljs.core.async.t_cljs$core$async31211(self__.flag,self__.cb,meta31212__$1));
}));

(cljs.core.async.t_cljs$core$async31211.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_31213){
var self__ = this;
var _31213__$1 = this;
return self__.meta31212;
}));

(cljs.core.async.t_cljs$core$async31211.prototype.cljs$core$async$impl$protocols$Handler$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async31211.prototype.cljs$core$async$impl$protocols$Handler$active_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.active_QMARK_(self__.flag);
}));

(cljs.core.async.t_cljs$core$async31211.prototype.cljs$core$async$impl$protocols$Handler$blockable_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return true;
}));

(cljs.core.async.t_cljs$core$async31211.prototype.cljs$core$async$impl$protocols$Handler$commit$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
cljs.core.async.impl.protocols.commit(self__.flag);

return self__.cb;
}));

(cljs.core.async.t_cljs$core$async31211.getBasis = (function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"flag","flag",-1565787888,null),new cljs.core.Symbol(null,"cb","cb",-2064487928,null),new cljs.core.Symbol(null,"meta31212","meta31212",-887190776,null)], null);
}));

(cljs.core.async.t_cljs$core$async31211.cljs$lang$type = true);

(cljs.core.async.t_cljs$core$async31211.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async31211");

(cljs.core.async.t_cljs$core$async31211.cljs$lang$ctorPrWriter = (function (this__4369__auto__,writer__4370__auto__,opt__4371__auto__){
return cljs.core._write(writer__4370__auto__,"cljs.core.async/t_cljs$core$async31211");
}));

/**
 * Positional factory function for cljs.core.async/t_cljs$core$async31211.
 */
cljs.core.async.__GT_t_cljs$core$async31211 = (function cljs$core$async$alt_handler_$___GT_t_cljs$core$async31211(flag__$1,cb__$1,meta31212){
return (new cljs.core.async.t_cljs$core$async31211(flag__$1,cb__$1,meta31212));
});

}

return (new cljs.core.async.t_cljs$core$async31211(flag,cb,cljs.core.PersistentArrayMap.EMPTY));
});
/**
 * returns derefable [val port] if immediate, nil if enqueued
 */
cljs.core.async.do_alts = (function cljs$core$async$do_alts(fret,ports,opts){
if((cljs.core.count(ports) > (0))){
} else {
throw (new Error(["Assert failed: ","alts must have at least one channel operation","\n","(pos? (count ports))"].join('')));
}

var flag = cljs.core.async.alt_flag();
var n = cljs.core.count(ports);
var idxs = cljs.core.async.random_array(n);
var priority = new cljs.core.Keyword(null,"priority","priority",1431093715).cljs$core$IFn$_invoke$arity$1(opts);
var ret = (function (){var i = (0);
while(true){
if((i < n)){
var idx = (cljs.core.truth_(priority)?i:(idxs[i]));
var port = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ports,idx);
var wport = ((cljs.core.vector_QMARK_(port))?(port.cljs$core$IFn$_invoke$arity$1 ? port.cljs$core$IFn$_invoke$arity$1((0)) : port.call(null,(0))):null);
var vbox = (cljs.core.truth_(wport)?(function (){var val = (port.cljs$core$IFn$_invoke$arity$1 ? port.cljs$core$IFn$_invoke$arity$1((1)) : port.call(null,(1)));
return cljs.core.async.impl.protocols.put_BANG_(wport,val,cljs.core.async.alt_handler(flag,((function (i,val,idx,port,wport,flag,n,idxs,priority){
return (function (p1__31223_SHARP_){
var G__31230 = new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [p1__31223_SHARP_,wport], null);
return (fret.cljs$core$IFn$_invoke$arity$1 ? fret.cljs$core$IFn$_invoke$arity$1(G__31230) : fret.call(null,G__31230));
});})(i,val,idx,port,wport,flag,n,idxs,priority))
));
})():cljs.core.async.impl.protocols.take_BANG_(port,cljs.core.async.alt_handler(flag,((function (i,idx,port,wport,flag,n,idxs,priority){
return (function (p1__31224_SHARP_){
var G__31233 = new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [p1__31224_SHARP_,port], null);
return (fret.cljs$core$IFn$_invoke$arity$1 ? fret.cljs$core$IFn$_invoke$arity$1(G__31233) : fret.call(null,G__31233));
});})(i,idx,port,wport,flag,n,idxs,priority))
)));
if(cljs.core.truth_(vbox)){
return cljs.core.async.impl.channels.box(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.deref(vbox),(function (){var or__4126__auto__ = wport;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return port;
}
})()], null));
} else {
var G__33449 = (i + (1));
i = G__33449;
continue;
}
} else {
return null;
}
break;
}
})();
var or__4126__auto__ = ret;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
if(cljs.core.contains_QMARK_(opts,new cljs.core.Keyword(null,"default","default",-1987822328))){
var temp__5735__auto__ = (function (){var and__4115__auto__ = flag.cljs$core$async$impl$protocols$Handler$active_QMARK_$arity$1(null);
if(cljs.core.truth_(and__4115__auto__)){
return flag.cljs$core$async$impl$protocols$Handler$commit$arity$1(null);
} else {
return and__4115__auto__;
}
})();
if(cljs.core.truth_(temp__5735__auto__)){
var got = temp__5735__auto__;
return cljs.core.async.impl.channels.box(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"default","default",-1987822328).cljs$core$IFn$_invoke$arity$1(opts),new cljs.core.Keyword(null,"default","default",-1987822328)], null));
} else {
return null;
}
} else {
return null;
}
}
});
/**
 * Completes at most one of several channel operations. Must be called
 * inside a (go ...) block. ports is a vector of channel endpoints,
 * which can be either a channel to take from or a vector of
 *   [channel-to-put-to val-to-put], in any combination. Takes will be
 *   made as if by <!, and puts will be made as if by >!. Unless
 *   the :priority option is true, if more than one port operation is
 *   ready a non-deterministic choice will be made. If no operation is
 *   ready and a :default value is supplied, [default-val :default] will
 *   be returned, otherwise alts! will park until the first operation to
 *   become ready completes. Returns [val port] of the completed
 *   operation, where val is the value taken for takes, and a
 *   boolean (true unless already closed, as per put!) for puts.
 * 
 *   opts are passed as :key val ... Supported options:
 * 
 *   :default val - the value to use if none of the operations are immediately ready
 *   :priority true - (default nil) when true, the operations will be tried in order.
 * 
 *   Note: there is no guarantee that the port exps or val exprs will be
 *   used, nor in what order should they be, so they should not be
 *   depended upon for side effects.
 */
cljs.core.async.alts_BANG_ = (function cljs$core$async$alts_BANG_(var_args){
var args__4742__auto__ = [];
var len__4736__auto___33450 = arguments.length;
var i__4737__auto___33451 = (0);
while(true){
if((i__4737__auto___33451 < len__4736__auto___33450)){
args__4742__auto__.push((arguments[i__4737__auto___33451]));

var G__33452 = (i__4737__auto___33451 + (1));
i__4737__auto___33451 = G__33452;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return cljs.core.async.alts_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(cljs.core.async.alts_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (ports,p__31238){
var map__31239 = p__31238;
var map__31239__$1 = (((((!((map__31239 == null))))?(((((map__31239.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__31239.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__31239):map__31239);
var opts = map__31239__$1;
throw (new Error("alts! used not in (go ...) block"));
}));

(cljs.core.async.alts_BANG_.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(cljs.core.async.alts_BANG_.cljs$lang$applyTo = (function (seq31236){
var G__31237 = cljs.core.first(seq31236);
var seq31236__$1 = cljs.core.next(seq31236);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__31237,seq31236__$1);
}));

/**
 * Puts a val into port if it's possible to do so immediately.
 *   nil values are not allowed. Never blocks. Returns true if offer succeeds.
 */
cljs.core.async.offer_BANG_ = (function cljs$core$async$offer_BANG_(port,val){
var ret = cljs.core.async.impl.protocols.put_BANG_(port,val,cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$2(cljs.core.async.nop,false));
if(cljs.core.truth_(ret)){
return cljs.core.deref(ret);
} else {
return null;
}
});
/**
 * Takes a val from port if it's possible to do so immediately.
 *   Never blocks. Returns value if successful, nil otherwise.
 */
cljs.core.async.poll_BANG_ = (function cljs$core$async$poll_BANG_(port){
var ret = cljs.core.async.impl.protocols.take_BANG_(port,cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$2(cljs.core.async.nop,false));
if(cljs.core.truth_(ret)){
return cljs.core.deref(ret);
} else {
return null;
}
});
/**
 * Takes elements from the from channel and supplies them to the to
 * channel. By default, the to channel will be closed when the from
 * channel closes, but can be determined by the close?  parameter. Will
 * stop consuming the from channel if the to channel closes
 */
cljs.core.async.pipe = (function cljs$core$async$pipe(var_args){
var G__31243 = arguments.length;
switch (G__31243) {
case 2:
return cljs.core.async.pipe.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.pipe.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(cljs.core.async.pipe.cljs$core$IFn$_invoke$arity$2 = (function (from,to){
return cljs.core.async.pipe.cljs$core$IFn$_invoke$arity$3(from,to,true);
}));

(cljs.core.async.pipe.cljs$core$IFn$_invoke$arity$3 = (function (from,to,close_QMARK_){
var c__31115__auto___33456 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__31116__auto__ = (function (){var switch__30837__auto__ = (function (state_31293){
var state_val_31295 = (state_31293[(1)]);
if((state_val_31295 === (7))){
var inst_31287 = (state_31293[(2)]);
var state_31293__$1 = state_31293;
var statearr_31296_33457 = state_31293__$1;
(statearr_31296_33457[(2)] = inst_31287);

(statearr_31296_33457[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31295 === (1))){
var state_31293__$1 = state_31293;
var statearr_31297_33458 = state_31293__$1;
(statearr_31297_33458[(2)] = null);

(statearr_31297_33458[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31295 === (4))){
var inst_31270 = (state_31293[(7)]);
var inst_31270__$1 = (state_31293[(2)]);
var inst_31271 = (inst_31270__$1 == null);
var state_31293__$1 = (function (){var statearr_31298 = state_31293;
(statearr_31298[(7)] = inst_31270__$1);

return statearr_31298;
})();
if(cljs.core.truth_(inst_31271)){
var statearr_31299_33459 = state_31293__$1;
(statearr_31299_33459[(1)] = (5));

} else {
var statearr_31302_33460 = state_31293__$1;
(statearr_31302_33460[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31295 === (13))){
var state_31293__$1 = state_31293;
var statearr_31305_33461 = state_31293__$1;
(statearr_31305_33461[(2)] = null);

(statearr_31305_33461[(1)] = (14));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31295 === (6))){
var inst_31270 = (state_31293[(7)]);
var state_31293__$1 = state_31293;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_31293__$1,(11),to,inst_31270);
} else {
if((state_val_31295 === (3))){
var inst_31289 = (state_31293[(2)]);
var state_31293__$1 = state_31293;
return cljs.core.async.impl.ioc_helpers.return_chan(state_31293__$1,inst_31289);
} else {
if((state_val_31295 === (12))){
var state_31293__$1 = state_31293;
var statearr_31308_33463 = state_31293__$1;
(statearr_31308_33463[(2)] = null);

(statearr_31308_33463[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31295 === (2))){
var state_31293__$1 = state_31293;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_31293__$1,(4),from);
} else {
if((state_val_31295 === (11))){
var inst_31280 = (state_31293[(2)]);
var state_31293__$1 = state_31293;
if(cljs.core.truth_(inst_31280)){
var statearr_31311_33464 = state_31293__$1;
(statearr_31311_33464[(1)] = (12));

} else {
var statearr_31312_33465 = state_31293__$1;
(statearr_31312_33465[(1)] = (13));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31295 === (9))){
var state_31293__$1 = state_31293;
var statearr_31313_33466 = state_31293__$1;
(statearr_31313_33466[(2)] = null);

(statearr_31313_33466[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31295 === (5))){
var state_31293__$1 = state_31293;
if(cljs.core.truth_(close_QMARK_)){
var statearr_31314_33467 = state_31293__$1;
(statearr_31314_33467[(1)] = (8));

} else {
var statearr_31315_33468 = state_31293__$1;
(statearr_31315_33468[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31295 === (14))){
var inst_31285 = (state_31293[(2)]);
var state_31293__$1 = state_31293;
var statearr_31316_33469 = state_31293__$1;
(statearr_31316_33469[(2)] = inst_31285);

(statearr_31316_33469[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31295 === (10))){
var inst_31277 = (state_31293[(2)]);
var state_31293__$1 = state_31293;
var statearr_31317_33470 = state_31293__$1;
(statearr_31317_33470[(2)] = inst_31277);

(statearr_31317_33470[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31295 === (8))){
var inst_31274 = cljs.core.async.close_BANG_(to);
var state_31293__$1 = state_31293;
var statearr_31318_33471 = state_31293__$1;
(statearr_31318_33471[(2)] = inst_31274);

(statearr_31318_33471[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$state_machine__30838__auto__ = null;
var cljs$core$async$state_machine__30838__auto____0 = (function (){
var statearr_31319 = [null,null,null,null,null,null,null,null];
(statearr_31319[(0)] = cljs$core$async$state_machine__30838__auto__);

(statearr_31319[(1)] = (1));

return statearr_31319;
});
var cljs$core$async$state_machine__30838__auto____1 = (function (state_31293){
while(true){
var ret_value__30839__auto__ = (function (){try{while(true){
var result__30840__auto__ = switch__30837__auto__(state_31293);
if(cljs.core.keyword_identical_QMARK_(result__30840__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__30840__auto__;
}
break;
}
}catch (e31320){var ex__30841__auto__ = e31320;
var statearr_31321_33480 = state_31293;
(statearr_31321_33480[(2)] = ex__30841__auto__);


if(cljs.core.seq((state_31293[(4)]))){
var statearr_31322_33481 = state_31293;
(statearr_31322_33481[(1)] = cljs.core.first((state_31293[(4)])));

} else {
throw ex__30841__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__30839__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__33482 = state_31293;
state_31293 = G__33482;
continue;
} else {
return ret_value__30839__auto__;
}
break;
}
});
cljs$core$async$state_machine__30838__auto__ = function(state_31293){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__30838__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__30838__auto____1.call(this,state_31293);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__30838__auto____0;
cljs$core$async$state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__30838__auto____1;
return cljs$core$async$state_machine__30838__auto__;
})()
})();
var state__31117__auto__ = (function (){var statearr_31323 = f__31116__auto__();
(statearr_31323[(6)] = c__31115__auto___33456);

return statearr_31323;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__31117__auto__);
}));


return to;
}));

(cljs.core.async.pipe.cljs$lang$maxFixedArity = 3);

cljs.core.async.pipeline_STAR_ = (function cljs$core$async$pipeline_STAR_(n,to,xf,from,close_QMARK_,ex_handler,type){
if((n > (0))){
} else {
throw (new Error("Assert failed: (pos? n)"));
}

var jobs = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(n);
var results = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(n);
var process = (function (p__31325){
var vec__31326 = p__31325;
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31326,(0),null);
var p = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31326,(1),null);
var job = vec__31326;
if((job == null)){
cljs.core.async.close_BANG_(results);

return null;
} else {
var res = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$3((1),xf,ex_handler);
var c__31115__auto___33483 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__31116__auto__ = (function (){var switch__30837__auto__ = (function (state_31333){
var state_val_31334 = (state_31333[(1)]);
if((state_val_31334 === (1))){
var state_31333__$1 = state_31333;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_31333__$1,(2),res,v);
} else {
if((state_val_31334 === (2))){
var inst_31330 = (state_31333[(2)]);
var inst_31331 = cljs.core.async.close_BANG_(res);
var state_31333__$1 = (function (){var statearr_31335 = state_31333;
(statearr_31335[(7)] = inst_31330);

return statearr_31335;
})();
return cljs.core.async.impl.ioc_helpers.return_chan(state_31333__$1,inst_31331);
} else {
return null;
}
}
});
return (function() {
var cljs$core$async$pipeline_STAR__$_state_machine__30838__auto__ = null;
var cljs$core$async$pipeline_STAR__$_state_machine__30838__auto____0 = (function (){
var statearr_31338 = [null,null,null,null,null,null,null,null];
(statearr_31338[(0)] = cljs$core$async$pipeline_STAR__$_state_machine__30838__auto__);

(statearr_31338[(1)] = (1));

return statearr_31338;
});
var cljs$core$async$pipeline_STAR__$_state_machine__30838__auto____1 = (function (state_31333){
while(true){
var ret_value__30839__auto__ = (function (){try{while(true){
var result__30840__auto__ = switch__30837__auto__(state_31333);
if(cljs.core.keyword_identical_QMARK_(result__30840__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__30840__auto__;
}
break;
}
}catch (e31339){var ex__30841__auto__ = e31339;
var statearr_31340_33484 = state_31333;
(statearr_31340_33484[(2)] = ex__30841__auto__);


if(cljs.core.seq((state_31333[(4)]))){
var statearr_31341_33485 = state_31333;
(statearr_31341_33485[(1)] = cljs.core.first((state_31333[(4)])));

} else {
throw ex__30841__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__30839__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__33486 = state_31333;
state_31333 = G__33486;
continue;
} else {
return ret_value__30839__auto__;
}
break;
}
});
cljs$core$async$pipeline_STAR__$_state_machine__30838__auto__ = function(state_31333){
switch(arguments.length){
case 0:
return cljs$core$async$pipeline_STAR__$_state_machine__30838__auto____0.call(this);
case 1:
return cljs$core$async$pipeline_STAR__$_state_machine__30838__auto____1.call(this,state_31333);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$pipeline_STAR__$_state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$pipeline_STAR__$_state_machine__30838__auto____0;
cljs$core$async$pipeline_STAR__$_state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$pipeline_STAR__$_state_machine__30838__auto____1;
return cljs$core$async$pipeline_STAR__$_state_machine__30838__auto__;
})()
})();
var state__31117__auto__ = (function (){var statearr_31344 = f__31116__auto__();
(statearr_31344[(6)] = c__31115__auto___33483);

return statearr_31344;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__31117__auto__);
}));


cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$2(p,res);

return true;
}
});
var async = (function (p__31345){
var vec__31346 = p__31345;
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31346,(0),null);
var p = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31346,(1),null);
var job = vec__31346;
if((job == null)){
cljs.core.async.close_BANG_(results);

return null;
} else {
var res = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
(xf.cljs$core$IFn$_invoke$arity$2 ? xf.cljs$core$IFn$_invoke$arity$2(v,res) : xf.call(null,v,res));

cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$2(p,res);

return true;
}
});
var n__4613__auto___33489 = n;
var __33490 = (0);
while(true){
if((__33490 < n__4613__auto___33489)){
var G__31349_33491 = type;
var G__31349_33492__$1 = (((G__31349_33491 instanceof cljs.core.Keyword))?G__31349_33491.fqn:null);
switch (G__31349_33492__$1) {
case "compute":
var c__31115__auto___33494 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run(((function (__33490,c__31115__auto___33494,G__31349_33491,G__31349_33492__$1,n__4613__auto___33489,jobs,results,process,async){
return (function (){
var f__31116__auto__ = (function (){var switch__30837__auto__ = ((function (__33490,c__31115__auto___33494,G__31349_33491,G__31349_33492__$1,n__4613__auto___33489,jobs,results,process,async){
return (function (state_31362){
var state_val_31363 = (state_31362[(1)]);
if((state_val_31363 === (1))){
var state_31362__$1 = state_31362;
var statearr_31364_33495 = state_31362__$1;
(statearr_31364_33495[(2)] = null);

(statearr_31364_33495[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31363 === (2))){
var state_31362__$1 = state_31362;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_31362__$1,(4),jobs);
} else {
if((state_val_31363 === (3))){
var inst_31360 = (state_31362[(2)]);
var state_31362__$1 = state_31362;
return cljs.core.async.impl.ioc_helpers.return_chan(state_31362__$1,inst_31360);
} else {
if((state_val_31363 === (4))){
var inst_31352 = (state_31362[(2)]);
var inst_31353 = process(inst_31352);
var state_31362__$1 = state_31362;
if(cljs.core.truth_(inst_31353)){
var statearr_31365_33582 = state_31362__$1;
(statearr_31365_33582[(1)] = (5));

} else {
var statearr_31366_33583 = state_31362__$1;
(statearr_31366_33583[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31363 === (5))){
var state_31362__$1 = state_31362;
var statearr_31367_33584 = state_31362__$1;
(statearr_31367_33584[(2)] = null);

(statearr_31367_33584[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31363 === (6))){
var state_31362__$1 = state_31362;
var statearr_31368_33585 = state_31362__$1;
(statearr_31368_33585[(2)] = null);

(statearr_31368_33585[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31363 === (7))){
var inst_31358 = (state_31362[(2)]);
var state_31362__$1 = state_31362;
var statearr_31369_33586 = state_31362__$1;
(statearr_31369_33586[(2)] = inst_31358);

(statearr_31369_33586[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
});})(__33490,c__31115__auto___33494,G__31349_33491,G__31349_33492__$1,n__4613__auto___33489,jobs,results,process,async))
;
return ((function (__33490,switch__30837__auto__,c__31115__auto___33494,G__31349_33491,G__31349_33492__$1,n__4613__auto___33489,jobs,results,process,async){
return (function() {
var cljs$core$async$pipeline_STAR__$_state_machine__30838__auto__ = null;
var cljs$core$async$pipeline_STAR__$_state_machine__30838__auto____0 = (function (){
var statearr_31370 = [null,null,null,null,null,null,null];
(statearr_31370[(0)] = cljs$core$async$pipeline_STAR__$_state_machine__30838__auto__);

(statearr_31370[(1)] = (1));

return statearr_31370;
});
var cljs$core$async$pipeline_STAR__$_state_machine__30838__auto____1 = (function (state_31362){
while(true){
var ret_value__30839__auto__ = (function (){try{while(true){
var result__30840__auto__ = switch__30837__auto__(state_31362);
if(cljs.core.keyword_identical_QMARK_(result__30840__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__30840__auto__;
}
break;
}
}catch (e31371){var ex__30841__auto__ = e31371;
var statearr_31372_33589 = state_31362;
(statearr_31372_33589[(2)] = ex__30841__auto__);


if(cljs.core.seq((state_31362[(4)]))){
var statearr_31373_33590 = state_31362;
(statearr_31373_33590[(1)] = cljs.core.first((state_31362[(4)])));

} else {
throw ex__30841__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__30839__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__33591 = state_31362;
state_31362 = G__33591;
continue;
} else {
return ret_value__30839__auto__;
}
break;
}
});
cljs$core$async$pipeline_STAR__$_state_machine__30838__auto__ = function(state_31362){
switch(arguments.length){
case 0:
return cljs$core$async$pipeline_STAR__$_state_machine__30838__auto____0.call(this);
case 1:
return cljs$core$async$pipeline_STAR__$_state_machine__30838__auto____1.call(this,state_31362);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$pipeline_STAR__$_state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$pipeline_STAR__$_state_machine__30838__auto____0;
cljs$core$async$pipeline_STAR__$_state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$pipeline_STAR__$_state_machine__30838__auto____1;
return cljs$core$async$pipeline_STAR__$_state_machine__30838__auto__;
})()
;})(__33490,switch__30837__auto__,c__31115__auto___33494,G__31349_33491,G__31349_33492__$1,n__4613__auto___33489,jobs,results,process,async))
})();
var state__31117__auto__ = (function (){var statearr_31374 = f__31116__auto__();
(statearr_31374[(6)] = c__31115__auto___33494);

return statearr_31374;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__31117__auto__);
});})(__33490,c__31115__auto___33494,G__31349_33491,G__31349_33492__$1,n__4613__auto___33489,jobs,results,process,async))
);


break;
case "async":
var c__31115__auto___33592 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run(((function (__33490,c__31115__auto___33592,G__31349_33491,G__31349_33492__$1,n__4613__auto___33489,jobs,results,process,async){
return (function (){
var f__31116__auto__ = (function (){var switch__30837__auto__ = ((function (__33490,c__31115__auto___33592,G__31349_33491,G__31349_33492__$1,n__4613__auto___33489,jobs,results,process,async){
return (function (state_31387){
var state_val_31388 = (state_31387[(1)]);
if((state_val_31388 === (1))){
var state_31387__$1 = state_31387;
var statearr_31389_33593 = state_31387__$1;
(statearr_31389_33593[(2)] = null);

(statearr_31389_33593[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31388 === (2))){
var state_31387__$1 = state_31387;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_31387__$1,(4),jobs);
} else {
if((state_val_31388 === (3))){
var inst_31385 = (state_31387[(2)]);
var state_31387__$1 = state_31387;
return cljs.core.async.impl.ioc_helpers.return_chan(state_31387__$1,inst_31385);
} else {
if((state_val_31388 === (4))){
var inst_31377 = (state_31387[(2)]);
var inst_31378 = async(inst_31377);
var state_31387__$1 = state_31387;
if(cljs.core.truth_(inst_31378)){
var statearr_31390_33594 = state_31387__$1;
(statearr_31390_33594[(1)] = (5));

} else {
var statearr_31391_33595 = state_31387__$1;
(statearr_31391_33595[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31388 === (5))){
var state_31387__$1 = state_31387;
var statearr_31392_33596 = state_31387__$1;
(statearr_31392_33596[(2)] = null);

(statearr_31392_33596[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31388 === (6))){
var state_31387__$1 = state_31387;
var statearr_31393_33597 = state_31387__$1;
(statearr_31393_33597[(2)] = null);

(statearr_31393_33597[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31388 === (7))){
var inst_31383 = (state_31387[(2)]);
var state_31387__$1 = state_31387;
var statearr_31394_33610 = state_31387__$1;
(statearr_31394_33610[(2)] = inst_31383);

(statearr_31394_33610[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
});})(__33490,c__31115__auto___33592,G__31349_33491,G__31349_33492__$1,n__4613__auto___33489,jobs,results,process,async))
;
return ((function (__33490,switch__30837__auto__,c__31115__auto___33592,G__31349_33491,G__31349_33492__$1,n__4613__auto___33489,jobs,results,process,async){
return (function() {
var cljs$core$async$pipeline_STAR__$_state_machine__30838__auto__ = null;
var cljs$core$async$pipeline_STAR__$_state_machine__30838__auto____0 = (function (){
var statearr_31395 = [null,null,null,null,null,null,null];
(statearr_31395[(0)] = cljs$core$async$pipeline_STAR__$_state_machine__30838__auto__);

(statearr_31395[(1)] = (1));

return statearr_31395;
});
var cljs$core$async$pipeline_STAR__$_state_machine__30838__auto____1 = (function (state_31387){
while(true){
var ret_value__30839__auto__ = (function (){try{while(true){
var result__30840__auto__ = switch__30837__auto__(state_31387);
if(cljs.core.keyword_identical_QMARK_(result__30840__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__30840__auto__;
}
break;
}
}catch (e31396){var ex__30841__auto__ = e31396;
var statearr_31397_33611 = state_31387;
(statearr_31397_33611[(2)] = ex__30841__auto__);


if(cljs.core.seq((state_31387[(4)]))){
var statearr_31399_33612 = state_31387;
(statearr_31399_33612[(1)] = cljs.core.first((state_31387[(4)])));

} else {
throw ex__30841__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__30839__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__33613 = state_31387;
state_31387 = G__33613;
continue;
} else {
return ret_value__30839__auto__;
}
break;
}
});
cljs$core$async$pipeline_STAR__$_state_machine__30838__auto__ = function(state_31387){
switch(arguments.length){
case 0:
return cljs$core$async$pipeline_STAR__$_state_machine__30838__auto____0.call(this);
case 1:
return cljs$core$async$pipeline_STAR__$_state_machine__30838__auto____1.call(this,state_31387);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$pipeline_STAR__$_state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$pipeline_STAR__$_state_machine__30838__auto____0;
cljs$core$async$pipeline_STAR__$_state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$pipeline_STAR__$_state_machine__30838__auto____1;
return cljs$core$async$pipeline_STAR__$_state_machine__30838__auto__;
})()
;})(__33490,switch__30837__auto__,c__31115__auto___33592,G__31349_33491,G__31349_33492__$1,n__4613__auto___33489,jobs,results,process,async))
})();
var state__31117__auto__ = (function (){var statearr_31401 = f__31116__auto__();
(statearr_31401[(6)] = c__31115__auto___33592);

return statearr_31401;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__31117__auto__);
});})(__33490,c__31115__auto___33592,G__31349_33491,G__31349_33492__$1,n__4613__auto___33489,jobs,results,process,async))
);


break;
default:
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__31349_33492__$1)].join('')));

}

var G__33620 = (__33490 + (1));
__33490 = G__33620;
continue;
} else {
}
break;
}

var c__31115__auto___33621 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__31116__auto__ = (function (){var switch__30837__auto__ = (function (state_31424){
var state_val_31425 = (state_31424[(1)]);
if((state_val_31425 === (7))){
var inst_31420 = (state_31424[(2)]);
var state_31424__$1 = state_31424;
var statearr_31426_33622 = state_31424__$1;
(statearr_31426_33622[(2)] = inst_31420);

(statearr_31426_33622[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31425 === (1))){
var state_31424__$1 = state_31424;
var statearr_31427_33623 = state_31424__$1;
(statearr_31427_33623[(2)] = null);

(statearr_31427_33623[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31425 === (4))){
var inst_31405 = (state_31424[(7)]);
var inst_31405__$1 = (state_31424[(2)]);
var inst_31406 = (inst_31405__$1 == null);
var state_31424__$1 = (function (){var statearr_31428 = state_31424;
(statearr_31428[(7)] = inst_31405__$1);

return statearr_31428;
})();
if(cljs.core.truth_(inst_31406)){
var statearr_31429_33624 = state_31424__$1;
(statearr_31429_33624[(1)] = (5));

} else {
var statearr_31430_33625 = state_31424__$1;
(statearr_31430_33625[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31425 === (6))){
var inst_31410 = (state_31424[(8)]);
var inst_31405 = (state_31424[(7)]);
var inst_31410__$1 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
var inst_31411 = cljs.core.PersistentVector.EMPTY_NODE;
var inst_31412 = [inst_31405,inst_31410__$1];
var inst_31413 = (new cljs.core.PersistentVector(null,2,(5),inst_31411,inst_31412,null));
var state_31424__$1 = (function (){var statearr_31431 = state_31424;
(statearr_31431[(8)] = inst_31410__$1);

return statearr_31431;
})();
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_31424__$1,(8),jobs,inst_31413);
} else {
if((state_val_31425 === (3))){
var inst_31422 = (state_31424[(2)]);
var state_31424__$1 = state_31424;
return cljs.core.async.impl.ioc_helpers.return_chan(state_31424__$1,inst_31422);
} else {
if((state_val_31425 === (2))){
var state_31424__$1 = state_31424;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_31424__$1,(4),from);
} else {
if((state_val_31425 === (9))){
var inst_31417 = (state_31424[(2)]);
var state_31424__$1 = (function (){var statearr_31432 = state_31424;
(statearr_31432[(9)] = inst_31417);

return statearr_31432;
})();
var statearr_31433_33626 = state_31424__$1;
(statearr_31433_33626[(2)] = null);

(statearr_31433_33626[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31425 === (5))){
var inst_31408 = cljs.core.async.close_BANG_(jobs);
var state_31424__$1 = state_31424;
var statearr_31434_33627 = state_31424__$1;
(statearr_31434_33627[(2)] = inst_31408);

(statearr_31434_33627[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31425 === (8))){
var inst_31410 = (state_31424[(8)]);
var inst_31415 = (state_31424[(2)]);
var state_31424__$1 = (function (){var statearr_31435 = state_31424;
(statearr_31435[(10)] = inst_31415);

return statearr_31435;
})();
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_31424__$1,(9),results,inst_31410);
} else {
return null;
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$pipeline_STAR__$_state_machine__30838__auto__ = null;
var cljs$core$async$pipeline_STAR__$_state_machine__30838__auto____0 = (function (){
var statearr_31436 = [null,null,null,null,null,null,null,null,null,null,null];
(statearr_31436[(0)] = cljs$core$async$pipeline_STAR__$_state_machine__30838__auto__);

(statearr_31436[(1)] = (1));

return statearr_31436;
});
var cljs$core$async$pipeline_STAR__$_state_machine__30838__auto____1 = (function (state_31424){
while(true){
var ret_value__30839__auto__ = (function (){try{while(true){
var result__30840__auto__ = switch__30837__auto__(state_31424);
if(cljs.core.keyword_identical_QMARK_(result__30840__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__30840__auto__;
}
break;
}
}catch (e31438){var ex__30841__auto__ = e31438;
var statearr_31439_33629 = state_31424;
(statearr_31439_33629[(2)] = ex__30841__auto__);


if(cljs.core.seq((state_31424[(4)]))){
var statearr_31440_33630 = state_31424;
(statearr_31440_33630[(1)] = cljs.core.first((state_31424[(4)])));

} else {
throw ex__30841__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__30839__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__33631 = state_31424;
state_31424 = G__33631;
continue;
} else {
return ret_value__30839__auto__;
}
break;
}
});
cljs$core$async$pipeline_STAR__$_state_machine__30838__auto__ = function(state_31424){
switch(arguments.length){
case 0:
return cljs$core$async$pipeline_STAR__$_state_machine__30838__auto____0.call(this);
case 1:
return cljs$core$async$pipeline_STAR__$_state_machine__30838__auto____1.call(this,state_31424);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$pipeline_STAR__$_state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$pipeline_STAR__$_state_machine__30838__auto____0;
cljs$core$async$pipeline_STAR__$_state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$pipeline_STAR__$_state_machine__30838__auto____1;
return cljs$core$async$pipeline_STAR__$_state_machine__30838__auto__;
})()
})();
var state__31117__auto__ = (function (){var statearr_31444 = f__31116__auto__();
(statearr_31444[(6)] = c__31115__auto___33621);

return statearr_31444;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__31117__auto__);
}));


var c__31115__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__31116__auto__ = (function (){var switch__30837__auto__ = (function (state_31483){
var state_val_31484 = (state_31483[(1)]);
if((state_val_31484 === (7))){
var inst_31478 = (state_31483[(2)]);
var state_31483__$1 = state_31483;
var statearr_31485_33632 = state_31483__$1;
(statearr_31485_33632[(2)] = inst_31478);

(statearr_31485_33632[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31484 === (20))){
var state_31483__$1 = state_31483;
var statearr_31486_33633 = state_31483__$1;
(statearr_31486_33633[(2)] = null);

(statearr_31486_33633[(1)] = (21));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31484 === (1))){
var state_31483__$1 = state_31483;
var statearr_31487_33634 = state_31483__$1;
(statearr_31487_33634[(2)] = null);

(statearr_31487_33634[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31484 === (4))){
var inst_31447 = (state_31483[(7)]);
var inst_31447__$1 = (state_31483[(2)]);
var inst_31448 = (inst_31447__$1 == null);
var state_31483__$1 = (function (){var statearr_31488 = state_31483;
(statearr_31488[(7)] = inst_31447__$1);

return statearr_31488;
})();
if(cljs.core.truth_(inst_31448)){
var statearr_31489_33635 = state_31483__$1;
(statearr_31489_33635[(1)] = (5));

} else {
var statearr_31490_33636 = state_31483__$1;
(statearr_31490_33636[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31484 === (15))){
var inst_31460 = (state_31483[(8)]);
var state_31483__$1 = state_31483;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_31483__$1,(18),to,inst_31460);
} else {
if((state_val_31484 === (21))){
var inst_31473 = (state_31483[(2)]);
var state_31483__$1 = state_31483;
var statearr_31491_33637 = state_31483__$1;
(statearr_31491_33637[(2)] = inst_31473);

(statearr_31491_33637[(1)] = (13));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31484 === (13))){
var inst_31475 = (state_31483[(2)]);
var state_31483__$1 = (function (){var statearr_31493 = state_31483;
(statearr_31493[(9)] = inst_31475);

return statearr_31493;
})();
var statearr_31494_33639 = state_31483__$1;
(statearr_31494_33639[(2)] = null);

(statearr_31494_33639[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31484 === (6))){
var inst_31447 = (state_31483[(7)]);
var state_31483__$1 = state_31483;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_31483__$1,(11),inst_31447);
} else {
if((state_val_31484 === (17))){
var inst_31468 = (state_31483[(2)]);
var state_31483__$1 = state_31483;
if(cljs.core.truth_(inst_31468)){
var statearr_31495_33644 = state_31483__$1;
(statearr_31495_33644[(1)] = (19));

} else {
var statearr_31496_33645 = state_31483__$1;
(statearr_31496_33645[(1)] = (20));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31484 === (3))){
var inst_31480 = (state_31483[(2)]);
var state_31483__$1 = state_31483;
return cljs.core.async.impl.ioc_helpers.return_chan(state_31483__$1,inst_31480);
} else {
if((state_val_31484 === (12))){
var inst_31457 = (state_31483[(10)]);
var state_31483__$1 = state_31483;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_31483__$1,(14),inst_31457);
} else {
if((state_val_31484 === (2))){
var state_31483__$1 = state_31483;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_31483__$1,(4),results);
} else {
if((state_val_31484 === (19))){
var state_31483__$1 = state_31483;
var statearr_31497_33646 = state_31483__$1;
(statearr_31497_33646[(2)] = null);

(statearr_31497_33646[(1)] = (12));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31484 === (11))){
var inst_31457 = (state_31483[(2)]);
var state_31483__$1 = (function (){var statearr_31498 = state_31483;
(statearr_31498[(10)] = inst_31457);

return statearr_31498;
})();
var statearr_31499_33647 = state_31483__$1;
(statearr_31499_33647[(2)] = null);

(statearr_31499_33647[(1)] = (12));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31484 === (9))){
var state_31483__$1 = state_31483;
var statearr_31500_33648 = state_31483__$1;
(statearr_31500_33648[(2)] = null);

(statearr_31500_33648[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31484 === (5))){
var state_31483__$1 = state_31483;
if(cljs.core.truth_(close_QMARK_)){
var statearr_31501_33649 = state_31483__$1;
(statearr_31501_33649[(1)] = (8));

} else {
var statearr_31502_33650 = state_31483__$1;
(statearr_31502_33650[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31484 === (14))){
var inst_31460 = (state_31483[(8)]);
var inst_31460__$1 = (state_31483[(2)]);
var inst_31461 = (inst_31460__$1 == null);
var inst_31462 = cljs.core.not(inst_31461);
var state_31483__$1 = (function (){var statearr_31503 = state_31483;
(statearr_31503[(8)] = inst_31460__$1);

return statearr_31503;
})();
if(inst_31462){
var statearr_31504_33651 = state_31483__$1;
(statearr_31504_33651[(1)] = (15));

} else {
var statearr_31505_33652 = state_31483__$1;
(statearr_31505_33652[(1)] = (16));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31484 === (16))){
var state_31483__$1 = state_31483;
var statearr_31510_33653 = state_31483__$1;
(statearr_31510_33653[(2)] = false);

(statearr_31510_33653[(1)] = (17));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31484 === (10))){
var inst_31454 = (state_31483[(2)]);
var state_31483__$1 = state_31483;
var statearr_31519_33654 = state_31483__$1;
(statearr_31519_33654[(2)] = inst_31454);

(statearr_31519_33654[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31484 === (18))){
var inst_31465 = (state_31483[(2)]);
var state_31483__$1 = state_31483;
var statearr_31520_33655 = state_31483__$1;
(statearr_31520_33655[(2)] = inst_31465);

(statearr_31520_33655[(1)] = (17));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31484 === (8))){
var inst_31451 = cljs.core.async.close_BANG_(to);
var state_31483__$1 = state_31483;
var statearr_31521_33656 = state_31483__$1;
(statearr_31521_33656[(2)] = inst_31451);

(statearr_31521_33656[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$pipeline_STAR__$_state_machine__30838__auto__ = null;
var cljs$core$async$pipeline_STAR__$_state_machine__30838__auto____0 = (function (){
var statearr_31524 = [null,null,null,null,null,null,null,null,null,null,null];
(statearr_31524[(0)] = cljs$core$async$pipeline_STAR__$_state_machine__30838__auto__);

(statearr_31524[(1)] = (1));

return statearr_31524;
});
var cljs$core$async$pipeline_STAR__$_state_machine__30838__auto____1 = (function (state_31483){
while(true){
var ret_value__30839__auto__ = (function (){try{while(true){
var result__30840__auto__ = switch__30837__auto__(state_31483);
if(cljs.core.keyword_identical_QMARK_(result__30840__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__30840__auto__;
}
break;
}
}catch (e31525){var ex__30841__auto__ = e31525;
var statearr_31526_33657 = state_31483;
(statearr_31526_33657[(2)] = ex__30841__auto__);


if(cljs.core.seq((state_31483[(4)]))){
var statearr_31527_33658 = state_31483;
(statearr_31527_33658[(1)] = cljs.core.first((state_31483[(4)])));

} else {
throw ex__30841__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__30839__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__33659 = state_31483;
state_31483 = G__33659;
continue;
} else {
return ret_value__30839__auto__;
}
break;
}
});
cljs$core$async$pipeline_STAR__$_state_machine__30838__auto__ = function(state_31483){
switch(arguments.length){
case 0:
return cljs$core$async$pipeline_STAR__$_state_machine__30838__auto____0.call(this);
case 1:
return cljs$core$async$pipeline_STAR__$_state_machine__30838__auto____1.call(this,state_31483);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$pipeline_STAR__$_state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$pipeline_STAR__$_state_machine__30838__auto____0;
cljs$core$async$pipeline_STAR__$_state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$pipeline_STAR__$_state_machine__30838__auto____1;
return cljs$core$async$pipeline_STAR__$_state_machine__30838__auto__;
})()
})();
var state__31117__auto__ = (function (){var statearr_31528 = f__31116__auto__();
(statearr_31528[(6)] = c__31115__auto__);

return statearr_31528;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__31117__auto__);
}));

return c__31115__auto__;
});
/**
 * Takes elements from the from channel and supplies them to the to
 *   channel, subject to the async function af, with parallelism n. af
 *   must be a function of two arguments, the first an input value and
 *   the second a channel on which to place the result(s). af must close!
 *   the channel before returning.  The presumption is that af will
 *   return immediately, having launched some asynchronous operation
 *   whose completion/callback will manipulate the result channel. Outputs
 *   will be returned in order relative to  the inputs. By default, the to
 *   channel will be closed when the from channel closes, but can be
 *   determined by the close?  parameter. Will stop consuming the from
 *   channel if the to channel closes.
 */
cljs.core.async.pipeline_async = (function cljs$core$async$pipeline_async(var_args){
var G__31532 = arguments.length;
switch (G__31532) {
case 4:
return cljs.core.async.pipeline_async.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
case 5:
return cljs.core.async.pipeline_async.cljs$core$IFn$_invoke$arity$5((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(cljs.core.async.pipeline_async.cljs$core$IFn$_invoke$arity$4 = (function (n,to,af,from){
return cljs.core.async.pipeline_async.cljs$core$IFn$_invoke$arity$5(n,to,af,from,true);
}));

(cljs.core.async.pipeline_async.cljs$core$IFn$_invoke$arity$5 = (function (n,to,af,from,close_QMARK_){
return cljs.core.async.pipeline_STAR_(n,to,af,from,close_QMARK_,null,new cljs.core.Keyword(null,"async","async",1050769601));
}));

(cljs.core.async.pipeline_async.cljs$lang$maxFixedArity = 5);

/**
 * Takes elements from the from channel and supplies them to the to
 *   channel, subject to the transducer xf, with parallelism n. Because
 *   it is parallel, the transducer will be applied independently to each
 *   element, not across elements, and may produce zero or more outputs
 *   per input.  Outputs will be returned in order relative to the
 *   inputs. By default, the to channel will be closed when the from
 *   channel closes, but can be determined by the close?  parameter. Will
 *   stop consuming the from channel if the to channel closes.
 * 
 *   Note this is supplied for API compatibility with the Clojure version.
 *   Values of N > 1 will not result in actual concurrency in a
 *   single-threaded runtime.
 */
cljs.core.async.pipeline = (function cljs$core$async$pipeline(var_args){
var G__31546 = arguments.length;
switch (G__31546) {
case 4:
return cljs.core.async.pipeline.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
case 5:
return cljs.core.async.pipeline.cljs$core$IFn$_invoke$arity$5((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]));

break;
case 6:
return cljs.core.async.pipeline.cljs$core$IFn$_invoke$arity$6((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]),(arguments[(5)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(cljs.core.async.pipeline.cljs$core$IFn$_invoke$arity$4 = (function (n,to,xf,from){
return cljs.core.async.pipeline.cljs$core$IFn$_invoke$arity$5(n,to,xf,from,true);
}));

(cljs.core.async.pipeline.cljs$core$IFn$_invoke$arity$5 = (function (n,to,xf,from,close_QMARK_){
return cljs.core.async.pipeline.cljs$core$IFn$_invoke$arity$6(n,to,xf,from,close_QMARK_,null);
}));

(cljs.core.async.pipeline.cljs$core$IFn$_invoke$arity$6 = (function (n,to,xf,from,close_QMARK_,ex_handler){
return cljs.core.async.pipeline_STAR_(n,to,xf,from,close_QMARK_,ex_handler,new cljs.core.Keyword(null,"compute","compute",1555393130));
}));

(cljs.core.async.pipeline.cljs$lang$maxFixedArity = 6);

/**
 * Takes a predicate and a source channel and returns a vector of two
 *   channels, the first of which will contain the values for which the
 *   predicate returned true, the second those for which it returned
 *   false.
 * 
 *   The out channels will be unbuffered by default, or two buf-or-ns can
 *   be supplied. The channels will close after the source channel has
 *   closed.
 */
cljs.core.async.split = (function cljs$core$async$split(var_args){
var G__31577 = arguments.length;
switch (G__31577) {
case 2:
return cljs.core.async.split.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 4:
return cljs.core.async.split.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(cljs.core.async.split.cljs$core$IFn$_invoke$arity$2 = (function (p,ch){
return cljs.core.async.split.cljs$core$IFn$_invoke$arity$4(p,ch,null,null);
}));

(cljs.core.async.split.cljs$core$IFn$_invoke$arity$4 = (function (p,ch,t_buf_or_n,f_buf_or_n){
var tc = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(t_buf_or_n);
var fc = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(f_buf_or_n);
var c__31115__auto___33710 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__31116__auto__ = (function (){var switch__30837__auto__ = (function (state_31634){
var state_val_31635 = (state_31634[(1)]);
if((state_val_31635 === (7))){
var inst_31630 = (state_31634[(2)]);
var state_31634__$1 = state_31634;
var statearr_31644_33712 = state_31634__$1;
(statearr_31644_33712[(2)] = inst_31630);

(statearr_31644_33712[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31635 === (1))){
var state_31634__$1 = state_31634;
var statearr_31648_33713 = state_31634__$1;
(statearr_31648_33713[(2)] = null);

(statearr_31648_33713[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31635 === (4))){
var inst_31604 = (state_31634[(7)]);
var inst_31604__$1 = (state_31634[(2)]);
var inst_31605 = (inst_31604__$1 == null);
var state_31634__$1 = (function (){var statearr_31655 = state_31634;
(statearr_31655[(7)] = inst_31604__$1);

return statearr_31655;
})();
if(cljs.core.truth_(inst_31605)){
var statearr_31657_33714 = state_31634__$1;
(statearr_31657_33714[(1)] = (5));

} else {
var statearr_31658_33715 = state_31634__$1;
(statearr_31658_33715[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31635 === (13))){
var state_31634__$1 = state_31634;
var statearr_31661_33716 = state_31634__$1;
(statearr_31661_33716[(2)] = null);

(statearr_31661_33716[(1)] = (14));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31635 === (6))){
var inst_31604 = (state_31634[(7)]);
var inst_31613 = (p.cljs$core$IFn$_invoke$arity$1 ? p.cljs$core$IFn$_invoke$arity$1(inst_31604) : p.call(null,inst_31604));
var state_31634__$1 = state_31634;
if(cljs.core.truth_(inst_31613)){
var statearr_31665_33717 = state_31634__$1;
(statearr_31665_33717[(1)] = (9));

} else {
var statearr_31666_33718 = state_31634__$1;
(statearr_31666_33718[(1)] = (10));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31635 === (3))){
var inst_31632 = (state_31634[(2)]);
var state_31634__$1 = state_31634;
return cljs.core.async.impl.ioc_helpers.return_chan(state_31634__$1,inst_31632);
} else {
if((state_val_31635 === (12))){
var state_31634__$1 = state_31634;
var statearr_31669_33719 = state_31634__$1;
(statearr_31669_33719[(2)] = null);

(statearr_31669_33719[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31635 === (2))){
var state_31634__$1 = state_31634;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_31634__$1,(4),ch);
} else {
if((state_val_31635 === (11))){
var inst_31604 = (state_31634[(7)]);
var inst_31617 = (state_31634[(2)]);
var state_31634__$1 = state_31634;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_31634__$1,(8),inst_31617,inst_31604);
} else {
if((state_val_31635 === (9))){
var state_31634__$1 = state_31634;
var statearr_31684_33720 = state_31634__$1;
(statearr_31684_33720[(2)] = tc);

(statearr_31684_33720[(1)] = (11));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31635 === (5))){
var inst_31607 = cljs.core.async.close_BANG_(tc);
var inst_31608 = cljs.core.async.close_BANG_(fc);
var state_31634__$1 = (function (){var statearr_31687 = state_31634;
(statearr_31687[(8)] = inst_31607);

return statearr_31687;
})();
var statearr_31688_33721 = state_31634__$1;
(statearr_31688_33721[(2)] = inst_31608);

(statearr_31688_33721[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31635 === (14))){
var inst_31625 = (state_31634[(2)]);
var state_31634__$1 = state_31634;
var statearr_31696_33722 = state_31634__$1;
(statearr_31696_33722[(2)] = inst_31625);

(statearr_31696_33722[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31635 === (10))){
var state_31634__$1 = state_31634;
var statearr_31697_33723 = state_31634__$1;
(statearr_31697_33723[(2)] = fc);

(statearr_31697_33723[(1)] = (11));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31635 === (8))){
var inst_31620 = (state_31634[(2)]);
var state_31634__$1 = state_31634;
if(cljs.core.truth_(inst_31620)){
var statearr_31700_33725 = state_31634__$1;
(statearr_31700_33725[(1)] = (12));

} else {
var statearr_31702_33726 = state_31634__$1;
(statearr_31702_33726[(1)] = (13));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$state_machine__30838__auto__ = null;
var cljs$core$async$state_machine__30838__auto____0 = (function (){
var statearr_31706 = [null,null,null,null,null,null,null,null,null];
(statearr_31706[(0)] = cljs$core$async$state_machine__30838__auto__);

(statearr_31706[(1)] = (1));

return statearr_31706;
});
var cljs$core$async$state_machine__30838__auto____1 = (function (state_31634){
while(true){
var ret_value__30839__auto__ = (function (){try{while(true){
var result__30840__auto__ = switch__30837__auto__(state_31634);
if(cljs.core.keyword_identical_QMARK_(result__30840__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__30840__auto__;
}
break;
}
}catch (e31711){var ex__30841__auto__ = e31711;
var statearr_31712_33730 = state_31634;
(statearr_31712_33730[(2)] = ex__30841__auto__);


if(cljs.core.seq((state_31634[(4)]))){
var statearr_31714_33731 = state_31634;
(statearr_31714_33731[(1)] = cljs.core.first((state_31634[(4)])));

} else {
throw ex__30841__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__30839__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__33732 = state_31634;
state_31634 = G__33732;
continue;
} else {
return ret_value__30839__auto__;
}
break;
}
});
cljs$core$async$state_machine__30838__auto__ = function(state_31634){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__30838__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__30838__auto____1.call(this,state_31634);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__30838__auto____0;
cljs$core$async$state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__30838__auto____1;
return cljs$core$async$state_machine__30838__auto__;
})()
})();
var state__31117__auto__ = (function (){var statearr_31718 = f__31116__auto__();
(statearr_31718[(6)] = c__31115__auto___33710);

return statearr_31718;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__31117__auto__);
}));


return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [tc,fc], null);
}));

(cljs.core.async.split.cljs$lang$maxFixedArity = 4);

/**
 * f should be a function of 2 arguments. Returns a channel containing
 *   the single result of applying f to init and the first item from the
 *   channel, then applying f to that result and the 2nd item, etc. If
 *   the channel closes without yielding items, returns init and f is not
 *   called. ch must close before reduce produces a result.
 */
cljs.core.async.reduce = (function cljs$core$async$reduce(f,init,ch){
var c__31115__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__31116__auto__ = (function (){var switch__30837__auto__ = (function (state_31761){
var state_val_31762 = (state_31761[(1)]);
if((state_val_31762 === (7))){
var inst_31757 = (state_31761[(2)]);
var state_31761__$1 = state_31761;
var statearr_31768_33733 = state_31761__$1;
(statearr_31768_33733[(2)] = inst_31757);

(statearr_31768_33733[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31762 === (1))){
var inst_31736 = init;
var inst_31737 = inst_31736;
var state_31761__$1 = (function (){var statearr_31769 = state_31761;
(statearr_31769[(7)] = inst_31737);

return statearr_31769;
})();
var statearr_31770_33734 = state_31761__$1;
(statearr_31770_33734[(2)] = null);

(statearr_31770_33734[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31762 === (4))){
var inst_31741 = (state_31761[(8)]);
var inst_31741__$1 = (state_31761[(2)]);
var inst_31743 = (inst_31741__$1 == null);
var state_31761__$1 = (function (){var statearr_31775 = state_31761;
(statearr_31775[(8)] = inst_31741__$1);

return statearr_31775;
})();
if(cljs.core.truth_(inst_31743)){
var statearr_31778_33735 = state_31761__$1;
(statearr_31778_33735[(1)] = (5));

} else {
var statearr_31779_33736 = state_31761__$1;
(statearr_31779_33736[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31762 === (6))){
var inst_31737 = (state_31761[(7)]);
var inst_31741 = (state_31761[(8)]);
var inst_31747 = (state_31761[(9)]);
var inst_31747__$1 = (f.cljs$core$IFn$_invoke$arity$2 ? f.cljs$core$IFn$_invoke$arity$2(inst_31737,inst_31741) : f.call(null,inst_31737,inst_31741));
var inst_31748 = cljs.core.reduced_QMARK_(inst_31747__$1);
var state_31761__$1 = (function (){var statearr_31784 = state_31761;
(statearr_31784[(9)] = inst_31747__$1);

return statearr_31784;
})();
if(inst_31748){
var statearr_31785_33737 = state_31761__$1;
(statearr_31785_33737[(1)] = (8));

} else {
var statearr_31786_33738 = state_31761__$1;
(statearr_31786_33738[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31762 === (3))){
var inst_31759 = (state_31761[(2)]);
var state_31761__$1 = state_31761;
return cljs.core.async.impl.ioc_helpers.return_chan(state_31761__$1,inst_31759);
} else {
if((state_val_31762 === (2))){
var state_31761__$1 = state_31761;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_31761__$1,(4),ch);
} else {
if((state_val_31762 === (9))){
var inst_31747 = (state_31761[(9)]);
var inst_31737 = inst_31747;
var state_31761__$1 = (function (){var statearr_31816 = state_31761;
(statearr_31816[(7)] = inst_31737);

return statearr_31816;
})();
var statearr_31817_33739 = state_31761__$1;
(statearr_31817_33739[(2)] = null);

(statearr_31817_33739[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31762 === (5))){
var inst_31737 = (state_31761[(7)]);
var state_31761__$1 = state_31761;
var statearr_31827_33740 = state_31761__$1;
(statearr_31827_33740[(2)] = inst_31737);

(statearr_31827_33740[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31762 === (10))){
var inst_31755 = (state_31761[(2)]);
var state_31761__$1 = state_31761;
var statearr_31832_33742 = state_31761__$1;
(statearr_31832_33742[(2)] = inst_31755);

(statearr_31832_33742[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31762 === (8))){
var inst_31747 = (state_31761[(9)]);
var inst_31750 = cljs.core.deref(inst_31747);
var state_31761__$1 = state_31761;
var statearr_31833_33744 = state_31761__$1;
(statearr_31833_33744[(2)] = inst_31750);

(statearr_31833_33744[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$reduce_$_state_machine__30838__auto__ = null;
var cljs$core$async$reduce_$_state_machine__30838__auto____0 = (function (){
var statearr_31836 = [null,null,null,null,null,null,null,null,null,null];
(statearr_31836[(0)] = cljs$core$async$reduce_$_state_machine__30838__auto__);

(statearr_31836[(1)] = (1));

return statearr_31836;
});
var cljs$core$async$reduce_$_state_machine__30838__auto____1 = (function (state_31761){
while(true){
var ret_value__30839__auto__ = (function (){try{while(true){
var result__30840__auto__ = switch__30837__auto__(state_31761);
if(cljs.core.keyword_identical_QMARK_(result__30840__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__30840__auto__;
}
break;
}
}catch (e31840){var ex__30841__auto__ = e31840;
var statearr_31841_33745 = state_31761;
(statearr_31841_33745[(2)] = ex__30841__auto__);


if(cljs.core.seq((state_31761[(4)]))){
var statearr_31842_33746 = state_31761;
(statearr_31842_33746[(1)] = cljs.core.first((state_31761[(4)])));

} else {
throw ex__30841__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__30839__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__33747 = state_31761;
state_31761 = G__33747;
continue;
} else {
return ret_value__30839__auto__;
}
break;
}
});
cljs$core$async$reduce_$_state_machine__30838__auto__ = function(state_31761){
switch(arguments.length){
case 0:
return cljs$core$async$reduce_$_state_machine__30838__auto____0.call(this);
case 1:
return cljs$core$async$reduce_$_state_machine__30838__auto____1.call(this,state_31761);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$reduce_$_state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$reduce_$_state_machine__30838__auto____0;
cljs$core$async$reduce_$_state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$reduce_$_state_machine__30838__auto____1;
return cljs$core$async$reduce_$_state_machine__30838__auto__;
})()
})();
var state__31117__auto__ = (function (){var statearr_31843 = f__31116__auto__();
(statearr_31843[(6)] = c__31115__auto__);

return statearr_31843;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__31117__auto__);
}));

return c__31115__auto__;
});
/**
 * async/reduces a channel with a transformation (xform f).
 *   Returns a channel containing the result.  ch must close before
 *   transduce produces a result.
 */
cljs.core.async.transduce = (function cljs$core$async$transduce(xform,f,init,ch){
var f__$1 = (xform.cljs$core$IFn$_invoke$arity$1 ? xform.cljs$core$IFn$_invoke$arity$1(f) : xform.call(null,f));
var c__31115__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__31116__auto__ = (function (){var switch__30837__auto__ = (function (state_31859){
var state_val_31860 = (state_31859[(1)]);
if((state_val_31860 === (1))){
var inst_31854 = cljs.core.async.reduce(f__$1,init,ch);
var state_31859__$1 = state_31859;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_31859__$1,(2),inst_31854);
} else {
if((state_val_31860 === (2))){
var inst_31856 = (state_31859[(2)]);
var inst_31857 = (f__$1.cljs$core$IFn$_invoke$arity$1 ? f__$1.cljs$core$IFn$_invoke$arity$1(inst_31856) : f__$1.call(null,inst_31856));
var state_31859__$1 = state_31859;
return cljs.core.async.impl.ioc_helpers.return_chan(state_31859__$1,inst_31857);
} else {
return null;
}
}
});
return (function() {
var cljs$core$async$transduce_$_state_machine__30838__auto__ = null;
var cljs$core$async$transduce_$_state_machine__30838__auto____0 = (function (){
var statearr_31867 = [null,null,null,null,null,null,null];
(statearr_31867[(0)] = cljs$core$async$transduce_$_state_machine__30838__auto__);

(statearr_31867[(1)] = (1));

return statearr_31867;
});
var cljs$core$async$transduce_$_state_machine__30838__auto____1 = (function (state_31859){
while(true){
var ret_value__30839__auto__ = (function (){try{while(true){
var result__30840__auto__ = switch__30837__auto__(state_31859);
if(cljs.core.keyword_identical_QMARK_(result__30840__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__30840__auto__;
}
break;
}
}catch (e31869){var ex__30841__auto__ = e31869;
var statearr_31870_33748 = state_31859;
(statearr_31870_33748[(2)] = ex__30841__auto__);


if(cljs.core.seq((state_31859[(4)]))){
var statearr_31871_33749 = state_31859;
(statearr_31871_33749[(1)] = cljs.core.first((state_31859[(4)])));

} else {
throw ex__30841__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__30839__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__33750 = state_31859;
state_31859 = G__33750;
continue;
} else {
return ret_value__30839__auto__;
}
break;
}
});
cljs$core$async$transduce_$_state_machine__30838__auto__ = function(state_31859){
switch(arguments.length){
case 0:
return cljs$core$async$transduce_$_state_machine__30838__auto____0.call(this);
case 1:
return cljs$core$async$transduce_$_state_machine__30838__auto____1.call(this,state_31859);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$transduce_$_state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$transduce_$_state_machine__30838__auto____0;
cljs$core$async$transduce_$_state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$transduce_$_state_machine__30838__auto____1;
return cljs$core$async$transduce_$_state_machine__30838__auto__;
})()
})();
var state__31117__auto__ = (function (){var statearr_31872 = f__31116__auto__();
(statearr_31872[(6)] = c__31115__auto__);

return statearr_31872;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__31117__auto__);
}));

return c__31115__auto__;
});
/**
 * Puts the contents of coll into the supplied channel.
 * 
 *   By default the channel will be closed after the items are copied,
 *   but can be determined by the close? parameter.
 * 
 *   Returns a channel which will close after the items are copied.
 */
cljs.core.async.onto_chan_BANG_ = (function cljs$core$async$onto_chan_BANG_(var_args){
var G__31882 = arguments.length;
switch (G__31882) {
case 2:
return cljs.core.async.onto_chan_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.onto_chan_BANG_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(cljs.core.async.onto_chan_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (ch,coll){
return cljs.core.async.onto_chan_BANG_.cljs$core$IFn$_invoke$arity$3(ch,coll,true);
}));

(cljs.core.async.onto_chan_BANG_.cljs$core$IFn$_invoke$arity$3 = (function (ch,coll,close_QMARK_){
var c__31115__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__31116__auto__ = (function (){var switch__30837__auto__ = (function (state_31912){
var state_val_31913 = (state_31912[(1)]);
if((state_val_31913 === (7))){
var inst_31893 = (state_31912[(2)]);
var state_31912__$1 = state_31912;
var statearr_31914_33752 = state_31912__$1;
(statearr_31914_33752[(2)] = inst_31893);

(statearr_31914_33752[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31913 === (1))){
var inst_31887 = cljs.core.seq(coll);
var inst_31888 = inst_31887;
var state_31912__$1 = (function (){var statearr_31916 = state_31912;
(statearr_31916[(7)] = inst_31888);

return statearr_31916;
})();
var statearr_31917_33754 = state_31912__$1;
(statearr_31917_33754[(2)] = null);

(statearr_31917_33754[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31913 === (4))){
var inst_31888 = (state_31912[(7)]);
var inst_31891 = cljs.core.first(inst_31888);
var state_31912__$1 = state_31912;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_31912__$1,(7),ch,inst_31891);
} else {
if((state_val_31913 === (13))){
var inst_31905 = (state_31912[(2)]);
var state_31912__$1 = state_31912;
var statearr_31919_33759 = state_31912__$1;
(statearr_31919_33759[(2)] = inst_31905);

(statearr_31919_33759[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31913 === (6))){
var inst_31896 = (state_31912[(2)]);
var state_31912__$1 = state_31912;
if(cljs.core.truth_(inst_31896)){
var statearr_31920_33760 = state_31912__$1;
(statearr_31920_33760[(1)] = (8));

} else {
var statearr_31921_33761 = state_31912__$1;
(statearr_31921_33761[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31913 === (3))){
var inst_31909 = (state_31912[(2)]);
var state_31912__$1 = state_31912;
return cljs.core.async.impl.ioc_helpers.return_chan(state_31912__$1,inst_31909);
} else {
if((state_val_31913 === (12))){
var state_31912__$1 = state_31912;
var statearr_31922_33762 = state_31912__$1;
(statearr_31922_33762[(2)] = null);

(statearr_31922_33762[(1)] = (13));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31913 === (2))){
var inst_31888 = (state_31912[(7)]);
var state_31912__$1 = state_31912;
if(cljs.core.truth_(inst_31888)){
var statearr_31923_33763 = state_31912__$1;
(statearr_31923_33763[(1)] = (4));

} else {
var statearr_31924_33764 = state_31912__$1;
(statearr_31924_33764[(1)] = (5));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31913 === (11))){
var inst_31902 = cljs.core.async.close_BANG_(ch);
var state_31912__$1 = state_31912;
var statearr_31925_33765 = state_31912__$1;
(statearr_31925_33765[(2)] = inst_31902);

(statearr_31925_33765[(1)] = (13));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31913 === (9))){
var state_31912__$1 = state_31912;
if(cljs.core.truth_(close_QMARK_)){
var statearr_31936_33766 = state_31912__$1;
(statearr_31936_33766[(1)] = (11));

} else {
var statearr_31937_33767 = state_31912__$1;
(statearr_31937_33767[(1)] = (12));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31913 === (5))){
var inst_31888 = (state_31912[(7)]);
var state_31912__$1 = state_31912;
var statearr_31939_33768 = state_31912__$1;
(statearr_31939_33768[(2)] = inst_31888);

(statearr_31939_33768[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31913 === (10))){
var inst_31907 = (state_31912[(2)]);
var state_31912__$1 = state_31912;
var statearr_31944_33776 = state_31912__$1;
(statearr_31944_33776[(2)] = inst_31907);

(statearr_31944_33776[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31913 === (8))){
var inst_31888 = (state_31912[(7)]);
var inst_31898 = cljs.core.next(inst_31888);
var inst_31888__$1 = inst_31898;
var state_31912__$1 = (function (){var statearr_31953 = state_31912;
(statearr_31953[(7)] = inst_31888__$1);

return statearr_31953;
})();
var statearr_31954_33777 = state_31912__$1;
(statearr_31954_33777[(2)] = null);

(statearr_31954_33777[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$state_machine__30838__auto__ = null;
var cljs$core$async$state_machine__30838__auto____0 = (function (){
var statearr_31955 = [null,null,null,null,null,null,null,null];
(statearr_31955[(0)] = cljs$core$async$state_machine__30838__auto__);

(statearr_31955[(1)] = (1));

return statearr_31955;
});
var cljs$core$async$state_machine__30838__auto____1 = (function (state_31912){
while(true){
var ret_value__30839__auto__ = (function (){try{while(true){
var result__30840__auto__ = switch__30837__auto__(state_31912);
if(cljs.core.keyword_identical_QMARK_(result__30840__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__30840__auto__;
}
break;
}
}catch (e31956){var ex__30841__auto__ = e31956;
var statearr_31957_33781 = state_31912;
(statearr_31957_33781[(2)] = ex__30841__auto__);


if(cljs.core.seq((state_31912[(4)]))){
var statearr_31958_33782 = state_31912;
(statearr_31958_33782[(1)] = cljs.core.first((state_31912[(4)])));

} else {
throw ex__30841__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__30839__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__33786 = state_31912;
state_31912 = G__33786;
continue;
} else {
return ret_value__30839__auto__;
}
break;
}
});
cljs$core$async$state_machine__30838__auto__ = function(state_31912){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__30838__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__30838__auto____1.call(this,state_31912);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__30838__auto____0;
cljs$core$async$state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__30838__auto____1;
return cljs$core$async$state_machine__30838__auto__;
})()
})();
var state__31117__auto__ = (function (){var statearr_31959 = f__31116__auto__();
(statearr_31959[(6)] = c__31115__auto__);

return statearr_31959;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__31117__auto__);
}));

return c__31115__auto__;
}));

(cljs.core.async.onto_chan_BANG_.cljs$lang$maxFixedArity = 3);

/**
 * Creates and returns a channel which contains the contents of coll,
 *   closing when exhausted.
 */
cljs.core.async.to_chan_BANG_ = (function cljs$core$async$to_chan_BANG_(coll){
var ch = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(cljs.core.bounded_count((100),coll));
cljs.core.async.onto_chan_BANG_.cljs$core$IFn$_invoke$arity$2(ch,coll);

return ch;
});
/**
 * Deprecated - use onto-chan!
 */
cljs.core.async.onto_chan = (function cljs$core$async$onto_chan(var_args){
var G__31973 = arguments.length;
switch (G__31973) {
case 2:
return cljs.core.async.onto_chan.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.onto_chan.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(cljs.core.async.onto_chan.cljs$core$IFn$_invoke$arity$2 = (function (ch,coll){
return cljs.core.async.onto_chan_BANG_.cljs$core$IFn$_invoke$arity$3(ch,coll,true);
}));

(cljs.core.async.onto_chan.cljs$core$IFn$_invoke$arity$3 = (function (ch,coll,close_QMARK_){
return cljs.core.async.onto_chan_BANG_.cljs$core$IFn$_invoke$arity$3(ch,coll,close_QMARK_);
}));

(cljs.core.async.onto_chan.cljs$lang$maxFixedArity = 3);

/**
 * Deprecated - use to-chan!
 */
cljs.core.async.to_chan = (function cljs$core$async$to_chan(coll){
return cljs.core.async.to_chan_BANG_(coll);
});

/**
 * @interface
 */
cljs.core.async.Mux = function(){};

var cljs$core$async$Mux$muxch_STAR_$dyn_33795 = (function (_){
var x__4428__auto__ = (((_ == null))?null:_);
var m__4429__auto__ = (cljs.core.async.muxch_STAR_[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$1 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$1(_) : m__4429__auto__.call(null,_));
} else {
var m__4426__auto__ = (cljs.core.async.muxch_STAR_["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$1 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$1(_) : m__4426__auto__.call(null,_));
} else {
throw cljs.core.missing_protocol("Mux.muxch*",_);
}
}
});
cljs.core.async.muxch_STAR_ = (function cljs$core$async$muxch_STAR_(_){
if((((!((_ == null)))) && ((!((_.cljs$core$async$Mux$muxch_STAR_$arity$1 == null)))))){
return _.cljs$core$async$Mux$muxch_STAR_$arity$1(_);
} else {
return cljs$core$async$Mux$muxch_STAR_$dyn_33795(_);
}
});


/**
 * @interface
 */
cljs.core.async.Mult = function(){};

var cljs$core$async$Mult$tap_STAR_$dyn_33796 = (function (m,ch,close_QMARK_){
var x__4428__auto__ = (((m == null))?null:m);
var m__4429__auto__ = (cljs.core.async.tap_STAR_[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$3 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$3(m,ch,close_QMARK_) : m__4429__auto__.call(null,m,ch,close_QMARK_));
} else {
var m__4426__auto__ = (cljs.core.async.tap_STAR_["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$3 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$3(m,ch,close_QMARK_) : m__4426__auto__.call(null,m,ch,close_QMARK_));
} else {
throw cljs.core.missing_protocol("Mult.tap*",m);
}
}
});
cljs.core.async.tap_STAR_ = (function cljs$core$async$tap_STAR_(m,ch,close_QMARK_){
if((((!((m == null)))) && ((!((m.cljs$core$async$Mult$tap_STAR_$arity$3 == null)))))){
return m.cljs$core$async$Mult$tap_STAR_$arity$3(m,ch,close_QMARK_);
} else {
return cljs$core$async$Mult$tap_STAR_$dyn_33796(m,ch,close_QMARK_);
}
});

var cljs$core$async$Mult$untap_STAR_$dyn_33797 = (function (m,ch){
var x__4428__auto__ = (((m == null))?null:m);
var m__4429__auto__ = (cljs.core.async.untap_STAR_[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$2(m,ch) : m__4429__auto__.call(null,m,ch));
} else {
var m__4426__auto__ = (cljs.core.async.untap_STAR_["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$2(m,ch) : m__4426__auto__.call(null,m,ch));
} else {
throw cljs.core.missing_protocol("Mult.untap*",m);
}
}
});
cljs.core.async.untap_STAR_ = (function cljs$core$async$untap_STAR_(m,ch){
if((((!((m == null)))) && ((!((m.cljs$core$async$Mult$untap_STAR_$arity$2 == null)))))){
return m.cljs$core$async$Mult$untap_STAR_$arity$2(m,ch);
} else {
return cljs$core$async$Mult$untap_STAR_$dyn_33797(m,ch);
}
});

var cljs$core$async$Mult$untap_all_STAR_$dyn_33798 = (function (m){
var x__4428__auto__ = (((m == null))?null:m);
var m__4429__auto__ = (cljs.core.async.untap_all_STAR_[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$1 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$1(m) : m__4429__auto__.call(null,m));
} else {
var m__4426__auto__ = (cljs.core.async.untap_all_STAR_["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$1 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$1(m) : m__4426__auto__.call(null,m));
} else {
throw cljs.core.missing_protocol("Mult.untap-all*",m);
}
}
});
cljs.core.async.untap_all_STAR_ = (function cljs$core$async$untap_all_STAR_(m){
if((((!((m == null)))) && ((!((m.cljs$core$async$Mult$untap_all_STAR_$arity$1 == null)))))){
return m.cljs$core$async$Mult$untap_all_STAR_$arity$1(m);
} else {
return cljs$core$async$Mult$untap_all_STAR_$dyn_33798(m);
}
});

/**
 * Creates and returns a mult(iple) of the supplied channel. Channels
 *   containing copies of the channel can be created with 'tap', and
 *   detached with 'untap'.
 * 
 *   Each item is distributed to all taps in parallel and synchronously,
 *   i.e. each tap must accept before the next item is distributed. Use
 *   buffering/windowing to prevent slow taps from holding up the mult.
 * 
 *   Items received when there are no taps get dropped.
 * 
 *   If a tap puts to a closed channel, it will be removed from the mult.
 */
cljs.core.async.mult = (function cljs$core$async$mult(ch){
var cs = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var m = (function (){
if((typeof cljs !== 'undefined') && (typeof cljs.core !== 'undefined') && (typeof cljs.core.async !== 'undefined') && (typeof cljs.core.async.t_cljs$core$async31989 !== 'undefined')){
} else {

/**
* @constructor
 * @implements {cljs.core.async.Mult}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.async.Mux}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async31989 = (function (ch,cs,meta31990){
this.ch = ch;
this.cs = cs;
this.meta31990 = meta31990;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(cljs.core.async.t_cljs$core$async31989.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_31991,meta31990__$1){
var self__ = this;
var _31991__$1 = this;
return (new cljs.core.async.t_cljs$core$async31989(self__.ch,self__.cs,meta31990__$1));
}));

(cljs.core.async.t_cljs$core$async31989.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_31991){
var self__ = this;
var _31991__$1 = this;
return self__.meta31990;
}));

(cljs.core.async.t_cljs$core$async31989.prototype.cljs$core$async$Mux$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async31989.prototype.cljs$core$async$Mux$muxch_STAR_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return self__.ch;
}));

(cljs.core.async.t_cljs$core$async31989.prototype.cljs$core$async$Mult$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async31989.prototype.cljs$core$async$Mult$tap_STAR_$arity$3 = (function (_,ch__$1,close_QMARK_){
var self__ = this;
var ___$1 = this;
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(self__.cs,cljs.core.assoc,ch__$1,close_QMARK_);

return null;
}));

(cljs.core.async.t_cljs$core$async31989.prototype.cljs$core$async$Mult$untap_STAR_$arity$2 = (function (_,ch__$1){
var self__ = this;
var ___$1 = this;
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(self__.cs,cljs.core.dissoc,ch__$1);

return null;
}));

(cljs.core.async.t_cljs$core$async31989.prototype.cljs$core$async$Mult$untap_all_STAR_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
cljs.core.reset_BANG_(self__.cs,cljs.core.PersistentArrayMap.EMPTY);

return null;
}));

(cljs.core.async.t_cljs$core$async31989.getBasis = (function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"ch","ch",1085813622,null),new cljs.core.Symbol(null,"cs","cs",-117024463,null),new cljs.core.Symbol(null,"meta31990","meta31990",463485120,null)], null);
}));

(cljs.core.async.t_cljs$core$async31989.cljs$lang$type = true);

(cljs.core.async.t_cljs$core$async31989.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async31989");

(cljs.core.async.t_cljs$core$async31989.cljs$lang$ctorPrWriter = (function (this__4369__auto__,writer__4370__auto__,opt__4371__auto__){
return cljs.core._write(writer__4370__auto__,"cljs.core.async/t_cljs$core$async31989");
}));

/**
 * Positional factory function for cljs.core.async/t_cljs$core$async31989.
 */
cljs.core.async.__GT_t_cljs$core$async31989 = (function cljs$core$async$mult_$___GT_t_cljs$core$async31989(ch__$1,cs__$1,meta31990){
return (new cljs.core.async.t_cljs$core$async31989(ch__$1,cs__$1,meta31990));
});

}

return (new cljs.core.async.t_cljs$core$async31989(ch,cs,cljs.core.PersistentArrayMap.EMPTY));
})()
;
var dchan = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
var dctr = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
var done = (function (_){
if((cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(dctr,cljs.core.dec) === (0))){
return cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$2(dchan,true);
} else {
return null;
}
});
var c__31115__auto___33801 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__31116__auto__ = (function (){var switch__30837__auto__ = (function (state_32145){
var state_val_32146 = (state_32145[(1)]);
if((state_val_32146 === (7))){
var inst_32140 = (state_32145[(2)]);
var state_32145__$1 = state_32145;
var statearr_32150_33802 = state_32145__$1;
(statearr_32150_33802[(2)] = inst_32140);

(statearr_32150_33802[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (20))){
var inst_32044 = (state_32145[(7)]);
var inst_32057 = cljs.core.first(inst_32044);
var inst_32058 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(inst_32057,(0),null);
var inst_32059 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(inst_32057,(1),null);
var state_32145__$1 = (function (){var statearr_32151 = state_32145;
(statearr_32151[(8)] = inst_32058);

return statearr_32151;
})();
if(cljs.core.truth_(inst_32059)){
var statearr_32152_33803 = state_32145__$1;
(statearr_32152_33803[(1)] = (22));

} else {
var statearr_32153_33804 = state_32145__$1;
(statearr_32153_33804[(1)] = (23));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (27))){
var inst_32094 = (state_32145[(9)]);
var inst_32089 = (state_32145[(10)]);
var inst_32087 = (state_32145[(11)]);
var inst_32008 = (state_32145[(12)]);
var inst_32094__$1 = cljs.core._nth(inst_32087,inst_32089);
var inst_32095 = cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$3(inst_32094__$1,inst_32008,done);
var state_32145__$1 = (function (){var statearr_32154 = state_32145;
(statearr_32154[(9)] = inst_32094__$1);

return statearr_32154;
})();
if(cljs.core.truth_(inst_32095)){
var statearr_32155_33809 = state_32145__$1;
(statearr_32155_33809[(1)] = (30));

} else {
var statearr_32156_33810 = state_32145__$1;
(statearr_32156_33810[(1)] = (31));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (1))){
var state_32145__$1 = state_32145;
var statearr_32157_33811 = state_32145__$1;
(statearr_32157_33811[(2)] = null);

(statearr_32157_33811[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (24))){
var inst_32044 = (state_32145[(7)]);
var inst_32064 = (state_32145[(2)]);
var inst_32065 = cljs.core.next(inst_32044);
var inst_32017 = inst_32065;
var inst_32018 = null;
var inst_32019 = (0);
var inst_32020 = (0);
var state_32145__$1 = (function (){var statearr_32158 = state_32145;
(statearr_32158[(13)] = inst_32064);

(statearr_32158[(14)] = inst_32018);

(statearr_32158[(15)] = inst_32020);

(statearr_32158[(16)] = inst_32017);

(statearr_32158[(17)] = inst_32019);

return statearr_32158;
})();
var statearr_32159_33812 = state_32145__$1;
(statearr_32159_33812[(2)] = null);

(statearr_32159_33812[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (39))){
var state_32145__$1 = state_32145;
var statearr_32163_33813 = state_32145__$1;
(statearr_32163_33813[(2)] = null);

(statearr_32163_33813[(1)] = (41));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (4))){
var inst_32008 = (state_32145[(12)]);
var inst_32008__$1 = (state_32145[(2)]);
var inst_32009 = (inst_32008__$1 == null);
var state_32145__$1 = (function (){var statearr_32164 = state_32145;
(statearr_32164[(12)] = inst_32008__$1);

return statearr_32164;
})();
if(cljs.core.truth_(inst_32009)){
var statearr_32165_33814 = state_32145__$1;
(statearr_32165_33814[(1)] = (5));

} else {
var statearr_32166_33815 = state_32145__$1;
(statearr_32166_33815[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (15))){
var inst_32018 = (state_32145[(14)]);
var inst_32020 = (state_32145[(15)]);
var inst_32017 = (state_32145[(16)]);
var inst_32019 = (state_32145[(17)]);
var inst_32040 = (state_32145[(2)]);
var inst_32041 = (inst_32020 + (1));
var tmp32160 = inst_32018;
var tmp32161 = inst_32017;
var tmp32162 = inst_32019;
var inst_32017__$1 = tmp32161;
var inst_32018__$1 = tmp32160;
var inst_32019__$1 = tmp32162;
var inst_32020__$1 = inst_32041;
var state_32145__$1 = (function (){var statearr_32167 = state_32145;
(statearr_32167[(14)] = inst_32018__$1);

(statearr_32167[(15)] = inst_32020__$1);

(statearr_32167[(16)] = inst_32017__$1);

(statearr_32167[(17)] = inst_32019__$1);

(statearr_32167[(18)] = inst_32040);

return statearr_32167;
})();
var statearr_32168_33830 = state_32145__$1;
(statearr_32168_33830[(2)] = null);

(statearr_32168_33830[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (21))){
var inst_32068 = (state_32145[(2)]);
var state_32145__$1 = state_32145;
var statearr_32172_33831 = state_32145__$1;
(statearr_32172_33831[(2)] = inst_32068);

(statearr_32172_33831[(1)] = (18));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (31))){
var inst_32094 = (state_32145[(9)]);
var inst_32098 = m.cljs$core$async$Mult$untap_STAR_$arity$2(null,inst_32094);
var state_32145__$1 = state_32145;
var statearr_32173_33832 = state_32145__$1;
(statearr_32173_33832[(2)] = inst_32098);

(statearr_32173_33832[(1)] = (32));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (32))){
var inst_32086 = (state_32145[(19)]);
var inst_32089 = (state_32145[(10)]);
var inst_32088 = (state_32145[(20)]);
var inst_32087 = (state_32145[(11)]);
var inst_32100 = (state_32145[(2)]);
var inst_32101 = (inst_32089 + (1));
var tmp32169 = inst_32086;
var tmp32170 = inst_32088;
var tmp32171 = inst_32087;
var inst_32086__$1 = tmp32169;
var inst_32087__$1 = tmp32171;
var inst_32088__$1 = tmp32170;
var inst_32089__$1 = inst_32101;
var state_32145__$1 = (function (){var statearr_32174 = state_32145;
(statearr_32174[(19)] = inst_32086__$1);

(statearr_32174[(10)] = inst_32089__$1);

(statearr_32174[(20)] = inst_32088__$1);

(statearr_32174[(11)] = inst_32087__$1);

(statearr_32174[(21)] = inst_32100);

return statearr_32174;
})();
var statearr_32175_33833 = state_32145__$1;
(statearr_32175_33833[(2)] = null);

(statearr_32175_33833[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (40))){
var inst_32113 = (state_32145[(22)]);
var inst_32117 = m.cljs$core$async$Mult$untap_STAR_$arity$2(null,inst_32113);
var state_32145__$1 = state_32145;
var statearr_32176_33834 = state_32145__$1;
(statearr_32176_33834[(2)] = inst_32117);

(statearr_32176_33834[(1)] = (41));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (33))){
var inst_32104 = (state_32145[(23)]);
var inst_32106 = cljs.core.chunked_seq_QMARK_(inst_32104);
var state_32145__$1 = state_32145;
if(inst_32106){
var statearr_32179_33835 = state_32145__$1;
(statearr_32179_33835[(1)] = (36));

} else {
var statearr_32180_33836 = state_32145__$1;
(statearr_32180_33836[(1)] = (37));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (13))){
var inst_32033 = (state_32145[(24)]);
var inst_32036 = cljs.core.async.close_BANG_(inst_32033);
var state_32145__$1 = state_32145;
var statearr_32181_33837 = state_32145__$1;
(statearr_32181_33837[(2)] = inst_32036);

(statearr_32181_33837[(1)] = (15));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (22))){
var inst_32058 = (state_32145[(8)]);
var inst_32061 = cljs.core.async.close_BANG_(inst_32058);
var state_32145__$1 = state_32145;
var statearr_32182_33838 = state_32145__$1;
(statearr_32182_33838[(2)] = inst_32061);

(statearr_32182_33838[(1)] = (24));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (36))){
var inst_32104 = (state_32145[(23)]);
var inst_32108 = cljs.core.chunk_first(inst_32104);
var inst_32109 = cljs.core.chunk_rest(inst_32104);
var inst_32110 = cljs.core.count(inst_32108);
var inst_32086 = inst_32109;
var inst_32087 = inst_32108;
var inst_32088 = inst_32110;
var inst_32089 = (0);
var state_32145__$1 = (function (){var statearr_32183 = state_32145;
(statearr_32183[(19)] = inst_32086);

(statearr_32183[(10)] = inst_32089);

(statearr_32183[(20)] = inst_32088);

(statearr_32183[(11)] = inst_32087);

return statearr_32183;
})();
var statearr_32184_33839 = state_32145__$1;
(statearr_32184_33839[(2)] = null);

(statearr_32184_33839[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (41))){
var inst_32104 = (state_32145[(23)]);
var inst_32119 = (state_32145[(2)]);
var inst_32120 = cljs.core.next(inst_32104);
var inst_32086 = inst_32120;
var inst_32087 = null;
var inst_32088 = (0);
var inst_32089 = (0);
var state_32145__$1 = (function (){var statearr_32185 = state_32145;
(statearr_32185[(19)] = inst_32086);

(statearr_32185[(10)] = inst_32089);

(statearr_32185[(20)] = inst_32088);

(statearr_32185[(11)] = inst_32087);

(statearr_32185[(25)] = inst_32119);

return statearr_32185;
})();
var statearr_32186_33840 = state_32145__$1;
(statearr_32186_33840[(2)] = null);

(statearr_32186_33840[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (43))){
var state_32145__$1 = state_32145;
var statearr_32187_33841 = state_32145__$1;
(statearr_32187_33841[(2)] = null);

(statearr_32187_33841[(1)] = (44));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (29))){
var inst_32128 = (state_32145[(2)]);
var state_32145__$1 = state_32145;
var statearr_32188_33842 = state_32145__$1;
(statearr_32188_33842[(2)] = inst_32128);

(statearr_32188_33842[(1)] = (26));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (44))){
var inst_32137 = (state_32145[(2)]);
var state_32145__$1 = (function (){var statearr_32189 = state_32145;
(statearr_32189[(26)] = inst_32137);

return statearr_32189;
})();
var statearr_32190_33843 = state_32145__$1;
(statearr_32190_33843[(2)] = null);

(statearr_32190_33843[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (6))){
var inst_32078 = (state_32145[(27)]);
var inst_32077 = cljs.core.deref(cs);
var inst_32078__$1 = cljs.core.keys(inst_32077);
var inst_32079 = cljs.core.count(inst_32078__$1);
var inst_32080 = cljs.core.reset_BANG_(dctr,inst_32079);
var inst_32085 = cljs.core.seq(inst_32078__$1);
var inst_32086 = inst_32085;
var inst_32087 = null;
var inst_32088 = (0);
var inst_32089 = (0);
var state_32145__$1 = (function (){var statearr_32193 = state_32145;
(statearr_32193[(27)] = inst_32078__$1);

(statearr_32193[(19)] = inst_32086);

(statearr_32193[(10)] = inst_32089);

(statearr_32193[(20)] = inst_32088);

(statearr_32193[(11)] = inst_32087);

(statearr_32193[(28)] = inst_32080);

return statearr_32193;
})();
var statearr_32195_33849 = state_32145__$1;
(statearr_32195_33849[(2)] = null);

(statearr_32195_33849[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (28))){
var inst_32086 = (state_32145[(19)]);
var inst_32104 = (state_32145[(23)]);
var inst_32104__$1 = cljs.core.seq(inst_32086);
var state_32145__$1 = (function (){var statearr_32198 = state_32145;
(statearr_32198[(23)] = inst_32104__$1);

return statearr_32198;
})();
if(inst_32104__$1){
var statearr_32199_33850 = state_32145__$1;
(statearr_32199_33850[(1)] = (33));

} else {
var statearr_32200_33851 = state_32145__$1;
(statearr_32200_33851[(1)] = (34));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (25))){
var inst_32089 = (state_32145[(10)]);
var inst_32088 = (state_32145[(20)]);
var inst_32091 = (inst_32089 < inst_32088);
var inst_32092 = inst_32091;
var state_32145__$1 = state_32145;
if(cljs.core.truth_(inst_32092)){
var statearr_32201_33852 = state_32145__$1;
(statearr_32201_33852[(1)] = (27));

} else {
var statearr_32202_33853 = state_32145__$1;
(statearr_32202_33853[(1)] = (28));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (34))){
var state_32145__$1 = state_32145;
var statearr_32203_33854 = state_32145__$1;
(statearr_32203_33854[(2)] = null);

(statearr_32203_33854[(1)] = (35));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (17))){
var state_32145__$1 = state_32145;
var statearr_32205_33855 = state_32145__$1;
(statearr_32205_33855[(2)] = null);

(statearr_32205_33855[(1)] = (18));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (3))){
var inst_32142 = (state_32145[(2)]);
var state_32145__$1 = state_32145;
return cljs.core.async.impl.ioc_helpers.return_chan(state_32145__$1,inst_32142);
} else {
if((state_val_32146 === (12))){
var inst_32073 = (state_32145[(2)]);
var state_32145__$1 = state_32145;
var statearr_32207_33856 = state_32145__$1;
(statearr_32207_33856[(2)] = inst_32073);

(statearr_32207_33856[(1)] = (9));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (2))){
var state_32145__$1 = state_32145;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_32145__$1,(4),ch);
} else {
if((state_val_32146 === (23))){
var state_32145__$1 = state_32145;
var statearr_32208_33857 = state_32145__$1;
(statearr_32208_33857[(2)] = null);

(statearr_32208_33857[(1)] = (24));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (35))){
var inst_32126 = (state_32145[(2)]);
var state_32145__$1 = state_32145;
var statearr_32210_33858 = state_32145__$1;
(statearr_32210_33858[(2)] = inst_32126);

(statearr_32210_33858[(1)] = (29));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (19))){
var inst_32044 = (state_32145[(7)]);
var inst_32049 = cljs.core.chunk_first(inst_32044);
var inst_32050 = cljs.core.chunk_rest(inst_32044);
var inst_32051 = cljs.core.count(inst_32049);
var inst_32017 = inst_32050;
var inst_32018 = inst_32049;
var inst_32019 = inst_32051;
var inst_32020 = (0);
var state_32145__$1 = (function (){var statearr_32211 = state_32145;
(statearr_32211[(14)] = inst_32018);

(statearr_32211[(15)] = inst_32020);

(statearr_32211[(16)] = inst_32017);

(statearr_32211[(17)] = inst_32019);

return statearr_32211;
})();
var statearr_32212_33862 = state_32145__$1;
(statearr_32212_33862[(2)] = null);

(statearr_32212_33862[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (11))){
var inst_32017 = (state_32145[(16)]);
var inst_32044 = (state_32145[(7)]);
var inst_32044__$1 = cljs.core.seq(inst_32017);
var state_32145__$1 = (function (){var statearr_32213 = state_32145;
(statearr_32213[(7)] = inst_32044__$1);

return statearr_32213;
})();
if(inst_32044__$1){
var statearr_32215_33863 = state_32145__$1;
(statearr_32215_33863[(1)] = (16));

} else {
var statearr_32216_33864 = state_32145__$1;
(statearr_32216_33864[(1)] = (17));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (9))){
var inst_32075 = (state_32145[(2)]);
var state_32145__$1 = state_32145;
var statearr_32221_33865 = state_32145__$1;
(statearr_32221_33865[(2)] = inst_32075);

(statearr_32221_33865[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (5))){
var inst_32015 = cljs.core.deref(cs);
var inst_32016 = cljs.core.seq(inst_32015);
var inst_32017 = inst_32016;
var inst_32018 = null;
var inst_32019 = (0);
var inst_32020 = (0);
var state_32145__$1 = (function (){var statearr_32223 = state_32145;
(statearr_32223[(14)] = inst_32018);

(statearr_32223[(15)] = inst_32020);

(statearr_32223[(16)] = inst_32017);

(statearr_32223[(17)] = inst_32019);

return statearr_32223;
})();
var statearr_32224_33866 = state_32145__$1;
(statearr_32224_33866[(2)] = null);

(statearr_32224_33866[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (14))){
var state_32145__$1 = state_32145;
var statearr_32227_33868 = state_32145__$1;
(statearr_32227_33868[(2)] = null);

(statearr_32227_33868[(1)] = (15));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (45))){
var inst_32134 = (state_32145[(2)]);
var state_32145__$1 = state_32145;
var statearr_32230_33869 = state_32145__$1;
(statearr_32230_33869[(2)] = inst_32134);

(statearr_32230_33869[(1)] = (44));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (26))){
var inst_32078 = (state_32145[(27)]);
var inst_32130 = (state_32145[(2)]);
var inst_32131 = cljs.core.seq(inst_32078);
var state_32145__$1 = (function (){var statearr_32232 = state_32145;
(statearr_32232[(29)] = inst_32130);

return statearr_32232;
})();
if(inst_32131){
var statearr_32239_33871 = state_32145__$1;
(statearr_32239_33871[(1)] = (42));

} else {
var statearr_32240_33872 = state_32145__$1;
(statearr_32240_33872[(1)] = (43));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (16))){
var inst_32044 = (state_32145[(7)]);
var inst_32047 = cljs.core.chunked_seq_QMARK_(inst_32044);
var state_32145__$1 = state_32145;
if(inst_32047){
var statearr_32241_33874 = state_32145__$1;
(statearr_32241_33874[(1)] = (19));

} else {
var statearr_32242_33875 = state_32145__$1;
(statearr_32242_33875[(1)] = (20));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (38))){
var inst_32123 = (state_32145[(2)]);
var state_32145__$1 = state_32145;
var statearr_32243_33876 = state_32145__$1;
(statearr_32243_33876[(2)] = inst_32123);

(statearr_32243_33876[(1)] = (35));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (30))){
var state_32145__$1 = state_32145;
var statearr_32244_33877 = state_32145__$1;
(statearr_32244_33877[(2)] = null);

(statearr_32244_33877[(1)] = (32));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (10))){
var inst_32018 = (state_32145[(14)]);
var inst_32020 = (state_32145[(15)]);
var inst_32032 = cljs.core._nth(inst_32018,inst_32020);
var inst_32033 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(inst_32032,(0),null);
var inst_32034 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(inst_32032,(1),null);
var state_32145__$1 = (function (){var statearr_32245 = state_32145;
(statearr_32245[(24)] = inst_32033);

return statearr_32245;
})();
if(cljs.core.truth_(inst_32034)){
var statearr_32246_33878 = state_32145__$1;
(statearr_32246_33878[(1)] = (13));

} else {
var statearr_32247_33879 = state_32145__$1;
(statearr_32247_33879[(1)] = (14));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (18))){
var inst_32071 = (state_32145[(2)]);
var state_32145__$1 = state_32145;
var statearr_32248_33880 = state_32145__$1;
(statearr_32248_33880[(2)] = inst_32071);

(statearr_32248_33880[(1)] = (12));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (42))){
var state_32145__$1 = state_32145;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_32145__$1,(45),dchan);
} else {
if((state_val_32146 === (37))){
var inst_32104 = (state_32145[(23)]);
var inst_32008 = (state_32145[(12)]);
var inst_32113 = (state_32145[(22)]);
var inst_32113__$1 = cljs.core.first(inst_32104);
var inst_32114 = cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$3(inst_32113__$1,inst_32008,done);
var state_32145__$1 = (function (){var statearr_32249 = state_32145;
(statearr_32249[(22)] = inst_32113__$1);

return statearr_32249;
})();
if(cljs.core.truth_(inst_32114)){
var statearr_32250_33881 = state_32145__$1;
(statearr_32250_33881[(1)] = (39));

} else {
var statearr_32251_33882 = state_32145__$1;
(statearr_32251_33882[(1)] = (40));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32146 === (8))){
var inst_32020 = (state_32145[(15)]);
var inst_32019 = (state_32145[(17)]);
var inst_32026 = (inst_32020 < inst_32019);
var inst_32027 = inst_32026;
var state_32145__$1 = state_32145;
if(cljs.core.truth_(inst_32027)){
var statearr_32252_33883 = state_32145__$1;
(statearr_32252_33883[(1)] = (10));

} else {
var statearr_32253_33884 = state_32145__$1;
(statearr_32253_33884[(1)] = (11));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$mult_$_state_machine__30838__auto__ = null;
var cljs$core$async$mult_$_state_machine__30838__auto____0 = (function (){
var statearr_32254 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_32254[(0)] = cljs$core$async$mult_$_state_machine__30838__auto__);

(statearr_32254[(1)] = (1));

return statearr_32254;
});
var cljs$core$async$mult_$_state_machine__30838__auto____1 = (function (state_32145){
while(true){
var ret_value__30839__auto__ = (function (){try{while(true){
var result__30840__auto__ = switch__30837__auto__(state_32145);
if(cljs.core.keyword_identical_QMARK_(result__30840__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__30840__auto__;
}
break;
}
}catch (e32255){var ex__30841__auto__ = e32255;
var statearr_32256_33888 = state_32145;
(statearr_32256_33888[(2)] = ex__30841__auto__);


if(cljs.core.seq((state_32145[(4)]))){
var statearr_32257_33890 = state_32145;
(statearr_32257_33890[(1)] = cljs.core.first((state_32145[(4)])));

} else {
throw ex__30841__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__30839__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__33891 = state_32145;
state_32145 = G__33891;
continue;
} else {
return ret_value__30839__auto__;
}
break;
}
});
cljs$core$async$mult_$_state_machine__30838__auto__ = function(state_32145){
switch(arguments.length){
case 0:
return cljs$core$async$mult_$_state_machine__30838__auto____0.call(this);
case 1:
return cljs$core$async$mult_$_state_machine__30838__auto____1.call(this,state_32145);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$mult_$_state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$mult_$_state_machine__30838__auto____0;
cljs$core$async$mult_$_state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$mult_$_state_machine__30838__auto____1;
return cljs$core$async$mult_$_state_machine__30838__auto__;
})()
})();
var state__31117__auto__ = (function (){var statearr_32258 = f__31116__auto__();
(statearr_32258[(6)] = c__31115__auto___33801);

return statearr_32258;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__31117__auto__);
}));


return m;
});
/**
 * Copies the mult source onto the supplied channel.
 * 
 *   By default the channel will be closed when the source closes,
 *   but can be determined by the close? parameter.
 */
cljs.core.async.tap = (function cljs$core$async$tap(var_args){
var G__32260 = arguments.length;
switch (G__32260) {
case 2:
return cljs.core.async.tap.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.tap.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(cljs.core.async.tap.cljs$core$IFn$_invoke$arity$2 = (function (mult,ch){
return cljs.core.async.tap.cljs$core$IFn$_invoke$arity$3(mult,ch,true);
}));

(cljs.core.async.tap.cljs$core$IFn$_invoke$arity$3 = (function (mult,ch,close_QMARK_){
cljs.core.async.tap_STAR_(mult,ch,close_QMARK_);

return ch;
}));

(cljs.core.async.tap.cljs$lang$maxFixedArity = 3);

/**
 * Disconnects a target channel from a mult
 */
cljs.core.async.untap = (function cljs$core$async$untap(mult,ch){
return cljs.core.async.untap_STAR_(mult,ch);
});
/**
 * Disconnects all target channels from a mult
 */
cljs.core.async.untap_all = (function cljs$core$async$untap_all(mult){
return cljs.core.async.untap_all_STAR_(mult);
});

/**
 * @interface
 */
cljs.core.async.Mix = function(){};

var cljs$core$async$Mix$admix_STAR_$dyn_33893 = (function (m,ch){
var x__4428__auto__ = (((m == null))?null:m);
var m__4429__auto__ = (cljs.core.async.admix_STAR_[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$2(m,ch) : m__4429__auto__.call(null,m,ch));
} else {
var m__4426__auto__ = (cljs.core.async.admix_STAR_["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$2(m,ch) : m__4426__auto__.call(null,m,ch));
} else {
throw cljs.core.missing_protocol("Mix.admix*",m);
}
}
});
cljs.core.async.admix_STAR_ = (function cljs$core$async$admix_STAR_(m,ch){
if((((!((m == null)))) && ((!((m.cljs$core$async$Mix$admix_STAR_$arity$2 == null)))))){
return m.cljs$core$async$Mix$admix_STAR_$arity$2(m,ch);
} else {
return cljs$core$async$Mix$admix_STAR_$dyn_33893(m,ch);
}
});

var cljs$core$async$Mix$unmix_STAR_$dyn_33894 = (function (m,ch){
var x__4428__auto__ = (((m == null))?null:m);
var m__4429__auto__ = (cljs.core.async.unmix_STAR_[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$2(m,ch) : m__4429__auto__.call(null,m,ch));
} else {
var m__4426__auto__ = (cljs.core.async.unmix_STAR_["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$2(m,ch) : m__4426__auto__.call(null,m,ch));
} else {
throw cljs.core.missing_protocol("Mix.unmix*",m);
}
}
});
cljs.core.async.unmix_STAR_ = (function cljs$core$async$unmix_STAR_(m,ch){
if((((!((m == null)))) && ((!((m.cljs$core$async$Mix$unmix_STAR_$arity$2 == null)))))){
return m.cljs$core$async$Mix$unmix_STAR_$arity$2(m,ch);
} else {
return cljs$core$async$Mix$unmix_STAR_$dyn_33894(m,ch);
}
});

var cljs$core$async$Mix$unmix_all_STAR_$dyn_33895 = (function (m){
var x__4428__auto__ = (((m == null))?null:m);
var m__4429__auto__ = (cljs.core.async.unmix_all_STAR_[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$1 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$1(m) : m__4429__auto__.call(null,m));
} else {
var m__4426__auto__ = (cljs.core.async.unmix_all_STAR_["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$1 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$1(m) : m__4426__auto__.call(null,m));
} else {
throw cljs.core.missing_protocol("Mix.unmix-all*",m);
}
}
});
cljs.core.async.unmix_all_STAR_ = (function cljs$core$async$unmix_all_STAR_(m){
if((((!((m == null)))) && ((!((m.cljs$core$async$Mix$unmix_all_STAR_$arity$1 == null)))))){
return m.cljs$core$async$Mix$unmix_all_STAR_$arity$1(m);
} else {
return cljs$core$async$Mix$unmix_all_STAR_$dyn_33895(m);
}
});

var cljs$core$async$Mix$toggle_STAR_$dyn_33896 = (function (m,state_map){
var x__4428__auto__ = (((m == null))?null:m);
var m__4429__auto__ = (cljs.core.async.toggle_STAR_[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$2(m,state_map) : m__4429__auto__.call(null,m,state_map));
} else {
var m__4426__auto__ = (cljs.core.async.toggle_STAR_["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$2(m,state_map) : m__4426__auto__.call(null,m,state_map));
} else {
throw cljs.core.missing_protocol("Mix.toggle*",m);
}
}
});
cljs.core.async.toggle_STAR_ = (function cljs$core$async$toggle_STAR_(m,state_map){
if((((!((m == null)))) && ((!((m.cljs$core$async$Mix$toggle_STAR_$arity$2 == null)))))){
return m.cljs$core$async$Mix$toggle_STAR_$arity$2(m,state_map);
} else {
return cljs$core$async$Mix$toggle_STAR_$dyn_33896(m,state_map);
}
});

var cljs$core$async$Mix$solo_mode_STAR_$dyn_33908 = (function (m,mode){
var x__4428__auto__ = (((m == null))?null:m);
var m__4429__auto__ = (cljs.core.async.solo_mode_STAR_[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$2(m,mode) : m__4429__auto__.call(null,m,mode));
} else {
var m__4426__auto__ = (cljs.core.async.solo_mode_STAR_["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$2(m,mode) : m__4426__auto__.call(null,m,mode));
} else {
throw cljs.core.missing_protocol("Mix.solo-mode*",m);
}
}
});
cljs.core.async.solo_mode_STAR_ = (function cljs$core$async$solo_mode_STAR_(m,mode){
if((((!((m == null)))) && ((!((m.cljs$core$async$Mix$solo_mode_STAR_$arity$2 == null)))))){
return m.cljs$core$async$Mix$solo_mode_STAR_$arity$2(m,mode);
} else {
return cljs$core$async$Mix$solo_mode_STAR_$dyn_33908(m,mode);
}
});

cljs.core.async.ioc_alts_BANG_ = (function cljs$core$async$ioc_alts_BANG_(var_args){
var args__4742__auto__ = [];
var len__4736__auto___33909 = arguments.length;
var i__4737__auto___33910 = (0);
while(true){
if((i__4737__auto___33910 < len__4736__auto___33909)){
args__4742__auto__.push((arguments[i__4737__auto___33910]));

var G__33911 = (i__4737__auto___33910 + (1));
i__4737__auto___33910 = G__33911;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((3) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((3)),(0),null)):null);
return cljs.core.async.ioc_alts_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),argseq__4743__auto__);
});

(cljs.core.async.ioc_alts_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (state,cont_block,ports,p__32306){
var map__32307 = p__32306;
var map__32307__$1 = (((((!((map__32307 == null))))?(((((map__32307.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__32307.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__32307):map__32307);
var opts = map__32307__$1;
var statearr_32309_33913 = state;
(statearr_32309_33913[(1)] = cont_block);


var temp__5735__auto__ = cljs.core.async.do_alts((function (val){
var statearr_32310_33914 = state;
(statearr_32310_33914[(2)] = val);


return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state);
}),ports,opts);
if(cljs.core.truth_(temp__5735__auto__)){
var cb = temp__5735__auto__;
var statearr_32311_33916 = state;
(statearr_32311_33916[(2)] = cljs.core.deref(cb));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}));

(cljs.core.async.ioc_alts_BANG_.cljs$lang$maxFixedArity = (3));

/** @this {Function} */
(cljs.core.async.ioc_alts_BANG_.cljs$lang$applyTo = (function (seq32298){
var G__32299 = cljs.core.first(seq32298);
var seq32298__$1 = cljs.core.next(seq32298);
var G__32300 = cljs.core.first(seq32298__$1);
var seq32298__$2 = cljs.core.next(seq32298__$1);
var G__32301 = cljs.core.first(seq32298__$2);
var seq32298__$3 = cljs.core.next(seq32298__$2);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__32299,G__32300,G__32301,seq32298__$3);
}));

/**
 * Creates and returns a mix of one or more input channels which will
 *   be put on the supplied out channel. Input sources can be added to
 *   the mix with 'admix', and removed with 'unmix'. A mix supports
 *   soloing, muting and pausing multiple inputs atomically using
 *   'toggle', and can solo using either muting or pausing as determined
 *   by 'solo-mode'.
 * 
 *   Each channel can have zero or more boolean modes set via 'toggle':
 * 
 *   :solo - when true, only this (ond other soloed) channel(s) will appear
 *        in the mix output channel. :mute and :pause states of soloed
 *        channels are ignored. If solo-mode is :mute, non-soloed
 *        channels are muted, if :pause, non-soloed channels are
 *        paused.
 * 
 *   :mute - muted channels will have their contents consumed but not included in the mix
 *   :pause - paused channels will not have their contents consumed (and thus also not included in the mix)
 */
cljs.core.async.mix = (function cljs$core$async$mix(out){
var cs = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var solo_modes = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"pause","pause",-2095325672),null,new cljs.core.Keyword(null,"mute","mute",1151223646),null], null), null);
var attrs = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(solo_modes,new cljs.core.Keyword(null,"solo","solo",-316350075));
var solo_mode = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"mute","mute",1151223646));
var change = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(cljs.core.async.sliding_buffer((1)));
var changed = (function (){
return cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$2(change,true);
});
var pick = (function (attr,chs){
return cljs.core.reduce_kv((function (ret,c,v){
if(cljs.core.truth_((attr.cljs$core$IFn$_invoke$arity$1 ? attr.cljs$core$IFn$_invoke$arity$1(v) : attr.call(null,v)))){
return cljs.core.conj.cljs$core$IFn$_invoke$arity$2(ret,c);
} else {
return ret;
}
}),cljs.core.PersistentHashSet.EMPTY,chs);
});
var calc_state = (function (){
var chs = cljs.core.deref(cs);
var mode = cljs.core.deref(solo_mode);
var solos = pick(new cljs.core.Keyword(null,"solo","solo",-316350075),chs);
var pauses = pick(new cljs.core.Keyword(null,"pause","pause",-2095325672),chs);
return new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"solos","solos",1441458643),solos,new cljs.core.Keyword(null,"mutes","mutes",1068806309),pick(new cljs.core.Keyword(null,"mute","mute",1151223646),chs),new cljs.core.Keyword(null,"reads","reads",-1215067361),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(((((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(mode,new cljs.core.Keyword(null,"pause","pause",-2095325672))) && ((!(cljs.core.empty_QMARK_(solos))))))?cljs.core.vec(solos):cljs.core.vec(cljs.core.remove.cljs$core$IFn$_invoke$arity$2(pauses,cljs.core.keys(chs)))),change)], null);
});
var m = (function (){
if((typeof cljs !== 'undefined') && (typeof cljs.core !== 'undefined') && (typeof cljs.core.async !== 'undefined') && (typeof cljs.core.async.t_cljs$core$async32329 !== 'undefined')){
} else {

/**
* @constructor
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.async.Mix}
 * @implements {cljs.core.async.Mux}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async32329 = (function (change,solo_mode,pick,cs,calc_state,out,changed,solo_modes,attrs,meta32330){
this.change = change;
this.solo_mode = solo_mode;
this.pick = pick;
this.cs = cs;
this.calc_state = calc_state;
this.out = out;
this.changed = changed;
this.solo_modes = solo_modes;
this.attrs = attrs;
this.meta32330 = meta32330;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(cljs.core.async.t_cljs$core$async32329.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_32331,meta32330__$1){
var self__ = this;
var _32331__$1 = this;
return (new cljs.core.async.t_cljs$core$async32329(self__.change,self__.solo_mode,self__.pick,self__.cs,self__.calc_state,self__.out,self__.changed,self__.solo_modes,self__.attrs,meta32330__$1));
}));

(cljs.core.async.t_cljs$core$async32329.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_32331){
var self__ = this;
var _32331__$1 = this;
return self__.meta32330;
}));

(cljs.core.async.t_cljs$core$async32329.prototype.cljs$core$async$Mux$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async32329.prototype.cljs$core$async$Mux$muxch_STAR_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return self__.out;
}));

(cljs.core.async.t_cljs$core$async32329.prototype.cljs$core$async$Mix$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async32329.prototype.cljs$core$async$Mix$admix_STAR_$arity$2 = (function (_,ch){
var self__ = this;
var ___$1 = this;
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(self__.cs,cljs.core.assoc,ch,cljs.core.PersistentArrayMap.EMPTY);

return (self__.changed.cljs$core$IFn$_invoke$arity$0 ? self__.changed.cljs$core$IFn$_invoke$arity$0() : self__.changed.call(null));
}));

(cljs.core.async.t_cljs$core$async32329.prototype.cljs$core$async$Mix$unmix_STAR_$arity$2 = (function (_,ch){
var self__ = this;
var ___$1 = this;
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(self__.cs,cljs.core.dissoc,ch);

return (self__.changed.cljs$core$IFn$_invoke$arity$0 ? self__.changed.cljs$core$IFn$_invoke$arity$0() : self__.changed.call(null));
}));

(cljs.core.async.t_cljs$core$async32329.prototype.cljs$core$async$Mix$unmix_all_STAR_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
cljs.core.reset_BANG_(self__.cs,cljs.core.PersistentArrayMap.EMPTY);

return (self__.changed.cljs$core$IFn$_invoke$arity$0 ? self__.changed.cljs$core$IFn$_invoke$arity$0() : self__.changed.call(null));
}));

(cljs.core.async.t_cljs$core$async32329.prototype.cljs$core$async$Mix$toggle_STAR_$arity$2 = (function (_,state_map){
var self__ = this;
var ___$1 = this;
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(self__.cs,cljs.core.partial.cljs$core$IFn$_invoke$arity$2(cljs.core.merge_with,cljs.core.merge),state_map);

return (self__.changed.cljs$core$IFn$_invoke$arity$0 ? self__.changed.cljs$core$IFn$_invoke$arity$0() : self__.changed.call(null));
}));

(cljs.core.async.t_cljs$core$async32329.prototype.cljs$core$async$Mix$solo_mode_STAR_$arity$2 = (function (_,mode){
var self__ = this;
var ___$1 = this;
if(cljs.core.truth_((self__.solo_modes.cljs$core$IFn$_invoke$arity$1 ? self__.solo_modes.cljs$core$IFn$_invoke$arity$1(mode) : self__.solo_modes.call(null,mode)))){
} else {
throw (new Error(["Assert failed: ",["mode must be one of: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(self__.solo_modes)].join(''),"\n","(solo-modes mode)"].join('')));
}

cljs.core.reset_BANG_(self__.solo_mode,mode);

return (self__.changed.cljs$core$IFn$_invoke$arity$0 ? self__.changed.cljs$core$IFn$_invoke$arity$0() : self__.changed.call(null));
}));

(cljs.core.async.t_cljs$core$async32329.getBasis = (function (){
return new cljs.core.PersistentVector(null, 10, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"change","change",477485025,null),new cljs.core.Symbol(null,"solo-mode","solo-mode",2031788074,null),new cljs.core.Symbol(null,"pick","pick",1300068175,null),new cljs.core.Symbol(null,"cs","cs",-117024463,null),new cljs.core.Symbol(null,"calc-state","calc-state",-349968968,null),new cljs.core.Symbol(null,"out","out",729986010,null),new cljs.core.Symbol(null,"changed","changed",-2083710852,null),new cljs.core.Symbol(null,"solo-modes","solo-modes",882180540,null),new cljs.core.Symbol(null,"attrs","attrs",-450137186,null),new cljs.core.Symbol(null,"meta32330","meta32330",-654266956,null)], null);
}));

(cljs.core.async.t_cljs$core$async32329.cljs$lang$type = true);

(cljs.core.async.t_cljs$core$async32329.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async32329");

(cljs.core.async.t_cljs$core$async32329.cljs$lang$ctorPrWriter = (function (this__4369__auto__,writer__4370__auto__,opt__4371__auto__){
return cljs.core._write(writer__4370__auto__,"cljs.core.async/t_cljs$core$async32329");
}));

/**
 * Positional factory function for cljs.core.async/t_cljs$core$async32329.
 */
cljs.core.async.__GT_t_cljs$core$async32329 = (function cljs$core$async$mix_$___GT_t_cljs$core$async32329(change__$1,solo_mode__$1,pick__$1,cs__$1,calc_state__$1,out__$1,changed__$1,solo_modes__$1,attrs__$1,meta32330){
return (new cljs.core.async.t_cljs$core$async32329(change__$1,solo_mode__$1,pick__$1,cs__$1,calc_state__$1,out__$1,changed__$1,solo_modes__$1,attrs__$1,meta32330));
});

}

return (new cljs.core.async.t_cljs$core$async32329(change,solo_mode,pick,cs,calc_state,out,changed,solo_modes,attrs,cljs.core.PersistentArrayMap.EMPTY));
})()
;
var c__31115__auto___33921 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__31116__auto__ = (function (){var switch__30837__auto__ = (function (state_32456){
var state_val_32457 = (state_32456[(1)]);
if((state_val_32457 === (7))){
var inst_32365 = (state_32456[(2)]);
var state_32456__$1 = state_32456;
var statearr_32458_33922 = state_32456__$1;
(statearr_32458_33922[(2)] = inst_32365);

(statearr_32458_33922[(1)] = (4));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (20))){
var inst_32377 = (state_32456[(7)]);
var state_32456__$1 = state_32456;
var statearr_32459_33923 = state_32456__$1;
(statearr_32459_33923[(2)] = inst_32377);

(statearr_32459_33923[(1)] = (21));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (27))){
var state_32456__$1 = state_32456;
var statearr_32460_33924 = state_32456__$1;
(statearr_32460_33924[(2)] = null);

(statearr_32460_33924[(1)] = (28));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (1))){
var inst_32349 = (state_32456[(8)]);
var inst_32349__$1 = calc_state();
var inst_32351 = (inst_32349__$1 == null);
var inst_32352 = cljs.core.not(inst_32351);
var state_32456__$1 = (function (){var statearr_32461 = state_32456;
(statearr_32461[(8)] = inst_32349__$1);

return statearr_32461;
})();
if(inst_32352){
var statearr_32462_33925 = state_32456__$1;
(statearr_32462_33925[(1)] = (2));

} else {
var statearr_32463_33926 = state_32456__$1;
(statearr_32463_33926[(1)] = (3));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (24))){
var inst_32427 = (state_32456[(9)]);
var inst_32413 = (state_32456[(10)]);
var inst_32403 = (state_32456[(11)]);
var inst_32427__$1 = (inst_32403.cljs$core$IFn$_invoke$arity$1 ? inst_32403.cljs$core$IFn$_invoke$arity$1(inst_32413) : inst_32403.call(null,inst_32413));
var state_32456__$1 = (function (){var statearr_32464 = state_32456;
(statearr_32464[(9)] = inst_32427__$1);

return statearr_32464;
})();
if(cljs.core.truth_(inst_32427__$1)){
var statearr_32465_33927 = state_32456__$1;
(statearr_32465_33927[(1)] = (29));

} else {
var statearr_32466_33928 = state_32456__$1;
(statearr_32466_33928[(1)] = (30));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (4))){
var inst_32368 = (state_32456[(2)]);
var state_32456__$1 = state_32456;
if(cljs.core.truth_(inst_32368)){
var statearr_32467_33929 = state_32456__$1;
(statearr_32467_33929[(1)] = (8));

} else {
var statearr_32468_33930 = state_32456__$1;
(statearr_32468_33930[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (15))){
var inst_32397 = (state_32456[(2)]);
var state_32456__$1 = state_32456;
if(cljs.core.truth_(inst_32397)){
var statearr_32478_33931 = state_32456__$1;
(statearr_32478_33931[(1)] = (19));

} else {
var statearr_32479_33932 = state_32456__$1;
(statearr_32479_33932[(1)] = (20));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (21))){
var inst_32402 = (state_32456[(12)]);
var inst_32402__$1 = (state_32456[(2)]);
var inst_32403 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(inst_32402__$1,new cljs.core.Keyword(null,"solos","solos",1441458643));
var inst_32405 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(inst_32402__$1,new cljs.core.Keyword(null,"mutes","mutes",1068806309));
var inst_32406 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(inst_32402__$1,new cljs.core.Keyword(null,"reads","reads",-1215067361));
var state_32456__$1 = (function (){var statearr_32480 = state_32456;
(statearr_32480[(12)] = inst_32402__$1);

(statearr_32480[(13)] = inst_32405);

(statearr_32480[(11)] = inst_32403);

return statearr_32480;
})();
return cljs.core.async.ioc_alts_BANG_(state_32456__$1,(22),inst_32406);
} else {
if((state_val_32457 === (31))){
var inst_32435 = (state_32456[(2)]);
var state_32456__$1 = state_32456;
if(cljs.core.truth_(inst_32435)){
var statearr_32481_33934 = state_32456__$1;
(statearr_32481_33934[(1)] = (32));

} else {
var statearr_32482_33935 = state_32456__$1;
(statearr_32482_33935[(1)] = (33));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (32))){
var inst_32412 = (state_32456[(14)]);
var state_32456__$1 = state_32456;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_32456__$1,(35),out,inst_32412);
} else {
if((state_val_32457 === (33))){
var inst_32402 = (state_32456[(12)]);
var inst_32377 = inst_32402;
var state_32456__$1 = (function (){var statearr_32483 = state_32456;
(statearr_32483[(7)] = inst_32377);

return statearr_32483;
})();
var statearr_32484_33936 = state_32456__$1;
(statearr_32484_33936[(2)] = null);

(statearr_32484_33936[(1)] = (11));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (13))){
var inst_32377 = (state_32456[(7)]);
var inst_32384 = inst_32377.cljs$lang$protocol_mask$partition0$;
var inst_32385 = (inst_32384 & (64));
var inst_32386 = inst_32377.cljs$core$ISeq$;
var inst_32387 = (cljs.core.PROTOCOL_SENTINEL === inst_32386);
var inst_32388 = ((inst_32385) || (inst_32387));
var state_32456__$1 = state_32456;
if(cljs.core.truth_(inst_32388)){
var statearr_32485_33937 = state_32456__$1;
(statearr_32485_33937[(1)] = (16));

} else {
var statearr_32486_33938 = state_32456__$1;
(statearr_32486_33938[(1)] = (17));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (22))){
var inst_32412 = (state_32456[(14)]);
var inst_32413 = (state_32456[(10)]);
var inst_32411 = (state_32456[(2)]);
var inst_32412__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(inst_32411,(0),null);
var inst_32413__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(inst_32411,(1),null);
var inst_32414 = (inst_32412__$1 == null);
var inst_32415 = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(inst_32413__$1,change);
var inst_32416 = ((inst_32414) || (inst_32415));
var state_32456__$1 = (function (){var statearr_32487 = state_32456;
(statearr_32487[(14)] = inst_32412__$1);

(statearr_32487[(10)] = inst_32413__$1);

return statearr_32487;
})();
if(cljs.core.truth_(inst_32416)){
var statearr_32488_33939 = state_32456__$1;
(statearr_32488_33939[(1)] = (23));

} else {
var statearr_32489_33940 = state_32456__$1;
(statearr_32489_33940[(1)] = (24));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (36))){
var inst_32402 = (state_32456[(12)]);
var inst_32377 = inst_32402;
var state_32456__$1 = (function (){var statearr_32490 = state_32456;
(statearr_32490[(7)] = inst_32377);

return statearr_32490;
})();
var statearr_32491_33941 = state_32456__$1;
(statearr_32491_33941[(2)] = null);

(statearr_32491_33941[(1)] = (11));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (29))){
var inst_32427 = (state_32456[(9)]);
var state_32456__$1 = state_32456;
var statearr_32492_33942 = state_32456__$1;
(statearr_32492_33942[(2)] = inst_32427);

(statearr_32492_33942[(1)] = (31));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (6))){
var state_32456__$1 = state_32456;
var statearr_32493_33943 = state_32456__$1;
(statearr_32493_33943[(2)] = false);

(statearr_32493_33943[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (28))){
var inst_32423 = (state_32456[(2)]);
var inst_32424 = calc_state();
var inst_32377 = inst_32424;
var state_32456__$1 = (function (){var statearr_32494 = state_32456;
(statearr_32494[(7)] = inst_32377);

(statearr_32494[(15)] = inst_32423);

return statearr_32494;
})();
var statearr_32495_33944 = state_32456__$1;
(statearr_32495_33944[(2)] = null);

(statearr_32495_33944[(1)] = (11));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (25))){
var inst_32449 = (state_32456[(2)]);
var state_32456__$1 = state_32456;
var statearr_32496_33945 = state_32456__$1;
(statearr_32496_33945[(2)] = inst_32449);

(statearr_32496_33945[(1)] = (12));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (34))){
var inst_32447 = (state_32456[(2)]);
var state_32456__$1 = state_32456;
var statearr_32497_33946 = state_32456__$1;
(statearr_32497_33946[(2)] = inst_32447);

(statearr_32497_33946[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (17))){
var state_32456__$1 = state_32456;
var statearr_32498_33947 = state_32456__$1;
(statearr_32498_33947[(2)] = false);

(statearr_32498_33947[(1)] = (18));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (3))){
var state_32456__$1 = state_32456;
var statearr_32499_33948 = state_32456__$1;
(statearr_32499_33948[(2)] = false);

(statearr_32499_33948[(1)] = (4));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (12))){
var inst_32451 = (state_32456[(2)]);
var state_32456__$1 = state_32456;
return cljs.core.async.impl.ioc_helpers.return_chan(state_32456__$1,inst_32451);
} else {
if((state_val_32457 === (2))){
var inst_32349 = (state_32456[(8)]);
var inst_32356 = inst_32349.cljs$lang$protocol_mask$partition0$;
var inst_32357 = (inst_32356 & (64));
var inst_32358 = inst_32349.cljs$core$ISeq$;
var inst_32360 = (cljs.core.PROTOCOL_SENTINEL === inst_32358);
var inst_32361 = ((inst_32357) || (inst_32360));
var state_32456__$1 = state_32456;
if(cljs.core.truth_(inst_32361)){
var statearr_32500_33957 = state_32456__$1;
(statearr_32500_33957[(1)] = (5));

} else {
var statearr_32501_33958 = state_32456__$1;
(statearr_32501_33958[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (23))){
var inst_32412 = (state_32456[(14)]);
var inst_32418 = (inst_32412 == null);
var state_32456__$1 = state_32456;
if(cljs.core.truth_(inst_32418)){
var statearr_32502_33959 = state_32456__$1;
(statearr_32502_33959[(1)] = (26));

} else {
var statearr_32503_33960 = state_32456__$1;
(statearr_32503_33960[(1)] = (27));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (35))){
var inst_32438 = (state_32456[(2)]);
var state_32456__$1 = state_32456;
if(cljs.core.truth_(inst_32438)){
var statearr_32504_33961 = state_32456__$1;
(statearr_32504_33961[(1)] = (36));

} else {
var statearr_32505_33962 = state_32456__$1;
(statearr_32505_33962[(1)] = (37));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (19))){
var inst_32377 = (state_32456[(7)]);
var inst_32399 = cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,inst_32377);
var state_32456__$1 = state_32456;
var statearr_32506_33963 = state_32456__$1;
(statearr_32506_33963[(2)] = inst_32399);

(statearr_32506_33963[(1)] = (21));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (11))){
var inst_32377 = (state_32456[(7)]);
var inst_32381 = (inst_32377 == null);
var inst_32382 = cljs.core.not(inst_32381);
var state_32456__$1 = state_32456;
if(inst_32382){
var statearr_32507_33964 = state_32456__$1;
(statearr_32507_33964[(1)] = (13));

} else {
var statearr_32508_33965 = state_32456__$1;
(statearr_32508_33965[(1)] = (14));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (9))){
var inst_32349 = (state_32456[(8)]);
var state_32456__$1 = state_32456;
var statearr_32509_33966 = state_32456__$1;
(statearr_32509_33966[(2)] = inst_32349);

(statearr_32509_33966[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (5))){
var state_32456__$1 = state_32456;
var statearr_32510_33967 = state_32456__$1;
(statearr_32510_33967[(2)] = true);

(statearr_32510_33967[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (14))){
var state_32456__$1 = state_32456;
var statearr_32511_33968 = state_32456__$1;
(statearr_32511_33968[(2)] = false);

(statearr_32511_33968[(1)] = (15));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (26))){
var inst_32413 = (state_32456[(10)]);
var inst_32420 = cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(cs,cljs.core.dissoc,inst_32413);
var state_32456__$1 = state_32456;
var statearr_32512_33969 = state_32456__$1;
(statearr_32512_33969[(2)] = inst_32420);

(statearr_32512_33969[(1)] = (28));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (16))){
var state_32456__$1 = state_32456;
var statearr_32513_33970 = state_32456__$1;
(statearr_32513_33970[(2)] = true);

(statearr_32513_33970[(1)] = (18));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (38))){
var inst_32443 = (state_32456[(2)]);
var state_32456__$1 = state_32456;
var statearr_32514_33971 = state_32456__$1;
(statearr_32514_33971[(2)] = inst_32443);

(statearr_32514_33971[(1)] = (34));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (30))){
var inst_32405 = (state_32456[(13)]);
var inst_32413 = (state_32456[(10)]);
var inst_32403 = (state_32456[(11)]);
var inst_32430 = cljs.core.empty_QMARK_(inst_32403);
var inst_32431 = (inst_32405.cljs$core$IFn$_invoke$arity$1 ? inst_32405.cljs$core$IFn$_invoke$arity$1(inst_32413) : inst_32405.call(null,inst_32413));
var inst_32432 = cljs.core.not(inst_32431);
var inst_32433 = ((inst_32430) && (inst_32432));
var state_32456__$1 = state_32456;
var statearr_32515_33972 = state_32456__$1;
(statearr_32515_33972[(2)] = inst_32433);

(statearr_32515_33972[(1)] = (31));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (10))){
var inst_32349 = (state_32456[(8)]);
var inst_32373 = (state_32456[(2)]);
var inst_32374 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(inst_32373,new cljs.core.Keyword(null,"solos","solos",1441458643));
var inst_32375 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(inst_32373,new cljs.core.Keyword(null,"mutes","mutes",1068806309));
var inst_32376 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(inst_32373,new cljs.core.Keyword(null,"reads","reads",-1215067361));
var inst_32377 = inst_32349;
var state_32456__$1 = (function (){var statearr_32516 = state_32456;
(statearr_32516[(16)] = inst_32374);

(statearr_32516[(7)] = inst_32377);

(statearr_32516[(17)] = inst_32376);

(statearr_32516[(18)] = inst_32375);

return statearr_32516;
})();
var statearr_32517_33973 = state_32456__$1;
(statearr_32517_33973[(2)] = null);

(statearr_32517_33973[(1)] = (11));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (18))){
var inst_32393 = (state_32456[(2)]);
var state_32456__$1 = state_32456;
var statearr_32518_33974 = state_32456__$1;
(statearr_32518_33974[(2)] = inst_32393);

(statearr_32518_33974[(1)] = (15));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (37))){
var state_32456__$1 = state_32456;
var statearr_32523_33975 = state_32456__$1;
(statearr_32523_33975[(2)] = null);

(statearr_32523_33975[(1)] = (38));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32457 === (8))){
var inst_32349 = (state_32456[(8)]);
var inst_32370 = cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,inst_32349);
var state_32456__$1 = state_32456;
var statearr_32524_33977 = state_32456__$1;
(statearr_32524_33977[(2)] = inst_32370);

(statearr_32524_33977[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$mix_$_state_machine__30838__auto__ = null;
var cljs$core$async$mix_$_state_machine__30838__auto____0 = (function (){
var statearr_32525 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_32525[(0)] = cljs$core$async$mix_$_state_machine__30838__auto__);

(statearr_32525[(1)] = (1));

return statearr_32525;
});
var cljs$core$async$mix_$_state_machine__30838__auto____1 = (function (state_32456){
while(true){
var ret_value__30839__auto__ = (function (){try{while(true){
var result__30840__auto__ = switch__30837__auto__(state_32456);
if(cljs.core.keyword_identical_QMARK_(result__30840__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__30840__auto__;
}
break;
}
}catch (e32526){var ex__30841__auto__ = e32526;
var statearr_32527_33979 = state_32456;
(statearr_32527_33979[(2)] = ex__30841__auto__);


if(cljs.core.seq((state_32456[(4)]))){
var statearr_32528_33980 = state_32456;
(statearr_32528_33980[(1)] = cljs.core.first((state_32456[(4)])));

} else {
throw ex__30841__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__30839__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__33981 = state_32456;
state_32456 = G__33981;
continue;
} else {
return ret_value__30839__auto__;
}
break;
}
});
cljs$core$async$mix_$_state_machine__30838__auto__ = function(state_32456){
switch(arguments.length){
case 0:
return cljs$core$async$mix_$_state_machine__30838__auto____0.call(this);
case 1:
return cljs$core$async$mix_$_state_machine__30838__auto____1.call(this,state_32456);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$mix_$_state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$mix_$_state_machine__30838__auto____0;
cljs$core$async$mix_$_state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$mix_$_state_machine__30838__auto____1;
return cljs$core$async$mix_$_state_machine__30838__auto__;
})()
})();
var state__31117__auto__ = (function (){var statearr_32529 = f__31116__auto__();
(statearr_32529[(6)] = c__31115__auto___33921);

return statearr_32529;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__31117__auto__);
}));


return m;
});
/**
 * Adds ch as an input to the mix
 */
cljs.core.async.admix = (function cljs$core$async$admix(mix,ch){
return cljs.core.async.admix_STAR_(mix,ch);
});
/**
 * Removes ch as an input to the mix
 */
cljs.core.async.unmix = (function cljs$core$async$unmix(mix,ch){
return cljs.core.async.unmix_STAR_(mix,ch);
});
/**
 * removes all inputs from the mix
 */
cljs.core.async.unmix_all = (function cljs$core$async$unmix_all(mix){
return cljs.core.async.unmix_all_STAR_(mix);
});
/**
 * Atomically sets the state(s) of one or more channels in a mix. The
 *   state map is a map of channels -> channel-state-map. A
 *   channel-state-map is a map of attrs -> boolean, where attr is one or
 *   more of :mute, :pause or :solo. Any states supplied are merged with
 *   the current state.
 * 
 *   Note that channels can be added to a mix via toggle, which can be
 *   used to add channels in a particular (e.g. paused) state.
 */
cljs.core.async.toggle = (function cljs$core$async$toggle(mix,state_map){
return cljs.core.async.toggle_STAR_(mix,state_map);
});
/**
 * Sets the solo mode of the mix. mode must be one of :mute or :pause
 */
cljs.core.async.solo_mode = (function cljs$core$async$solo_mode(mix,mode){
return cljs.core.async.solo_mode_STAR_(mix,mode);
});

/**
 * @interface
 */
cljs.core.async.Pub = function(){};

var cljs$core$async$Pub$sub_STAR_$dyn_33984 = (function (p,v,ch,close_QMARK_){
var x__4428__auto__ = (((p == null))?null:p);
var m__4429__auto__ = (cljs.core.async.sub_STAR_[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$4 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$4(p,v,ch,close_QMARK_) : m__4429__auto__.call(null,p,v,ch,close_QMARK_));
} else {
var m__4426__auto__ = (cljs.core.async.sub_STAR_["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$4 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$4(p,v,ch,close_QMARK_) : m__4426__auto__.call(null,p,v,ch,close_QMARK_));
} else {
throw cljs.core.missing_protocol("Pub.sub*",p);
}
}
});
cljs.core.async.sub_STAR_ = (function cljs$core$async$sub_STAR_(p,v,ch,close_QMARK_){
if((((!((p == null)))) && ((!((p.cljs$core$async$Pub$sub_STAR_$arity$4 == null)))))){
return p.cljs$core$async$Pub$sub_STAR_$arity$4(p,v,ch,close_QMARK_);
} else {
return cljs$core$async$Pub$sub_STAR_$dyn_33984(p,v,ch,close_QMARK_);
}
});

var cljs$core$async$Pub$unsub_STAR_$dyn_33989 = (function (p,v,ch){
var x__4428__auto__ = (((p == null))?null:p);
var m__4429__auto__ = (cljs.core.async.unsub_STAR_[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$3 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$3(p,v,ch) : m__4429__auto__.call(null,p,v,ch));
} else {
var m__4426__auto__ = (cljs.core.async.unsub_STAR_["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$3 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$3(p,v,ch) : m__4426__auto__.call(null,p,v,ch));
} else {
throw cljs.core.missing_protocol("Pub.unsub*",p);
}
}
});
cljs.core.async.unsub_STAR_ = (function cljs$core$async$unsub_STAR_(p,v,ch){
if((((!((p == null)))) && ((!((p.cljs$core$async$Pub$unsub_STAR_$arity$3 == null)))))){
return p.cljs$core$async$Pub$unsub_STAR_$arity$3(p,v,ch);
} else {
return cljs$core$async$Pub$unsub_STAR_$dyn_33989(p,v,ch);
}
});

var cljs$core$async$Pub$unsub_all_STAR_$dyn_33991 = (function() {
var G__33993 = null;
var G__33993__1 = (function (p){
var x__4428__auto__ = (((p == null))?null:p);
var m__4429__auto__ = (cljs.core.async.unsub_all_STAR_[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$1 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$1(p) : m__4429__auto__.call(null,p));
} else {
var m__4426__auto__ = (cljs.core.async.unsub_all_STAR_["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$1 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$1(p) : m__4426__auto__.call(null,p));
} else {
throw cljs.core.missing_protocol("Pub.unsub-all*",p);
}
}
});
var G__33993__2 = (function (p,v){
var x__4428__auto__ = (((p == null))?null:p);
var m__4429__auto__ = (cljs.core.async.unsub_all_STAR_[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$2(p,v) : m__4429__auto__.call(null,p,v));
} else {
var m__4426__auto__ = (cljs.core.async.unsub_all_STAR_["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$2(p,v) : m__4426__auto__.call(null,p,v));
} else {
throw cljs.core.missing_protocol("Pub.unsub-all*",p);
}
}
});
G__33993 = function(p,v){
switch(arguments.length){
case 1:
return G__33993__1.call(this,p);
case 2:
return G__33993__2.call(this,p,v);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
G__33993.cljs$core$IFn$_invoke$arity$1 = G__33993__1;
G__33993.cljs$core$IFn$_invoke$arity$2 = G__33993__2;
return G__33993;
})()
;
cljs.core.async.unsub_all_STAR_ = (function cljs$core$async$unsub_all_STAR_(var_args){
var G__32542 = arguments.length;
switch (G__32542) {
case 1:
return cljs.core.async.unsub_all_STAR_.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return cljs.core.async.unsub_all_STAR_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(cljs.core.async.unsub_all_STAR_.cljs$core$IFn$_invoke$arity$1 = (function (p){
if((((!((p == null)))) && ((!((p.cljs$core$async$Pub$unsub_all_STAR_$arity$1 == null)))))){
return p.cljs$core$async$Pub$unsub_all_STAR_$arity$1(p);
} else {
return cljs$core$async$Pub$unsub_all_STAR_$dyn_33991(p);
}
}));

(cljs.core.async.unsub_all_STAR_.cljs$core$IFn$_invoke$arity$2 = (function (p,v){
if((((!((p == null)))) && ((!((p.cljs$core$async$Pub$unsub_all_STAR_$arity$2 == null)))))){
return p.cljs$core$async$Pub$unsub_all_STAR_$arity$2(p,v);
} else {
return cljs$core$async$Pub$unsub_all_STAR_$dyn_33991(p,v);
}
}));

(cljs.core.async.unsub_all_STAR_.cljs$lang$maxFixedArity = 2);


/**
 * Creates and returns a pub(lication) of the supplied channel,
 *   partitioned into topics by the topic-fn. topic-fn will be applied to
 *   each value on the channel and the result will determine the 'topic'
 *   on which that value will be put. Channels can be subscribed to
 *   receive copies of topics using 'sub', and unsubscribed using
 *   'unsub'. Each topic will be handled by an internal mult on a
 *   dedicated channel. By default these internal channels are
 *   unbuffered, but a buf-fn can be supplied which, given a topic,
 *   creates a buffer with desired properties.
 * 
 *   Each item is distributed to all subs in parallel and synchronously,
 *   i.e. each sub must accept before the next item is distributed. Use
 *   buffering/windowing to prevent slow subs from holding up the pub.
 * 
 *   Items received when there are no matching subs get dropped.
 * 
 *   Note that if buf-fns are used then each topic is handled
 *   asynchronously, i.e. if a channel is subscribed to more than one
 *   topic it should not expect them to be interleaved identically with
 *   the source.
 */
cljs.core.async.pub = (function cljs$core$async$pub(var_args){
var G__32562 = arguments.length;
switch (G__32562) {
case 2:
return cljs.core.async.pub.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.pub.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(cljs.core.async.pub.cljs$core$IFn$_invoke$arity$2 = (function (ch,topic_fn){
return cljs.core.async.pub.cljs$core$IFn$_invoke$arity$3(ch,topic_fn,cljs.core.constantly(null));
}));

(cljs.core.async.pub.cljs$core$IFn$_invoke$arity$3 = (function (ch,topic_fn,buf_fn){
var mults = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var ensure_mult = (function (topic){
var or__4126__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(mults),topic);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(mults,(function (p1__32549_SHARP_){
if(cljs.core.truth_((p1__32549_SHARP_.cljs$core$IFn$_invoke$arity$1 ? p1__32549_SHARP_.cljs$core$IFn$_invoke$arity$1(topic) : p1__32549_SHARP_.call(null,topic)))){
return p1__32549_SHARP_;
} else {
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__32549_SHARP_,topic,cljs.core.async.mult(cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((buf_fn.cljs$core$IFn$_invoke$arity$1 ? buf_fn.cljs$core$IFn$_invoke$arity$1(topic) : buf_fn.call(null,topic)))));
}
})),topic);
}
});
var p = (function (){
if((typeof cljs !== 'undefined') && (typeof cljs.core !== 'undefined') && (typeof cljs.core.async !== 'undefined') && (typeof cljs.core.async.t_cljs$core$async32598 !== 'undefined')){
} else {

/**
* @constructor
 * @implements {cljs.core.async.Pub}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.async.Mux}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async32598 = (function (ch,topic_fn,buf_fn,mults,ensure_mult,meta32599){
this.ch = ch;
this.topic_fn = topic_fn;
this.buf_fn = buf_fn;
this.mults = mults;
this.ensure_mult = ensure_mult;
this.meta32599 = meta32599;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(cljs.core.async.t_cljs$core$async32598.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_32600,meta32599__$1){
var self__ = this;
var _32600__$1 = this;
return (new cljs.core.async.t_cljs$core$async32598(self__.ch,self__.topic_fn,self__.buf_fn,self__.mults,self__.ensure_mult,meta32599__$1));
}));

(cljs.core.async.t_cljs$core$async32598.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_32600){
var self__ = this;
var _32600__$1 = this;
return self__.meta32599;
}));

(cljs.core.async.t_cljs$core$async32598.prototype.cljs$core$async$Mux$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async32598.prototype.cljs$core$async$Mux$muxch_STAR_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return self__.ch;
}));

(cljs.core.async.t_cljs$core$async32598.prototype.cljs$core$async$Pub$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async32598.prototype.cljs$core$async$Pub$sub_STAR_$arity$4 = (function (p,topic,ch__$1,close_QMARK_){
var self__ = this;
var p__$1 = this;
var m = (self__.ensure_mult.cljs$core$IFn$_invoke$arity$1 ? self__.ensure_mult.cljs$core$IFn$_invoke$arity$1(topic) : self__.ensure_mult.call(null,topic));
return cljs.core.async.tap.cljs$core$IFn$_invoke$arity$3(m,ch__$1,close_QMARK_);
}));

(cljs.core.async.t_cljs$core$async32598.prototype.cljs$core$async$Pub$unsub_STAR_$arity$3 = (function (p,topic,ch__$1){
var self__ = this;
var p__$1 = this;
var temp__5735__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(self__.mults),topic);
if(cljs.core.truth_(temp__5735__auto__)){
var m = temp__5735__auto__;
return cljs.core.async.untap(m,ch__$1);
} else {
return null;
}
}));

(cljs.core.async.t_cljs$core$async32598.prototype.cljs$core$async$Pub$unsub_all_STAR_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.reset_BANG_(self__.mults,cljs.core.PersistentArrayMap.EMPTY);
}));

(cljs.core.async.t_cljs$core$async32598.prototype.cljs$core$async$Pub$unsub_all_STAR_$arity$2 = (function (_,topic){
var self__ = this;
var ___$1 = this;
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(self__.mults,cljs.core.dissoc,topic);
}));

(cljs.core.async.t_cljs$core$async32598.getBasis = (function (){
return new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"ch","ch",1085813622,null),new cljs.core.Symbol(null,"topic-fn","topic-fn",-862449736,null),new cljs.core.Symbol(null,"buf-fn","buf-fn",-1200281591,null),new cljs.core.Symbol(null,"mults","mults",-461114485,null),new cljs.core.Symbol(null,"ensure-mult","ensure-mult",1796584816,null),new cljs.core.Symbol(null,"meta32599","meta32599",435384315,null)], null);
}));

(cljs.core.async.t_cljs$core$async32598.cljs$lang$type = true);

(cljs.core.async.t_cljs$core$async32598.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async32598");

(cljs.core.async.t_cljs$core$async32598.cljs$lang$ctorPrWriter = (function (this__4369__auto__,writer__4370__auto__,opt__4371__auto__){
return cljs.core._write(writer__4370__auto__,"cljs.core.async/t_cljs$core$async32598");
}));

/**
 * Positional factory function for cljs.core.async/t_cljs$core$async32598.
 */
cljs.core.async.__GT_t_cljs$core$async32598 = (function cljs$core$async$__GT_t_cljs$core$async32598(ch__$1,topic_fn__$1,buf_fn__$1,mults__$1,ensure_mult__$1,meta32599){
return (new cljs.core.async.t_cljs$core$async32598(ch__$1,topic_fn__$1,buf_fn__$1,mults__$1,ensure_mult__$1,meta32599));
});

}

return (new cljs.core.async.t_cljs$core$async32598(ch,topic_fn,buf_fn,mults,ensure_mult,cljs.core.PersistentArrayMap.EMPTY));
})()
;
var c__31115__auto___34041 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__31116__auto__ = (function (){var switch__30837__auto__ = (function (state_32686){
var state_val_32687 = (state_32686[(1)]);
if((state_val_32687 === (7))){
var inst_32682 = (state_32686[(2)]);
var state_32686__$1 = state_32686;
var statearr_32691_34046 = state_32686__$1;
(statearr_32691_34046[(2)] = inst_32682);

(statearr_32691_34046[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32687 === (20))){
var state_32686__$1 = state_32686;
var statearr_32692_34047 = state_32686__$1;
(statearr_32692_34047[(2)] = null);

(statearr_32692_34047[(1)] = (21));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32687 === (1))){
var state_32686__$1 = state_32686;
var statearr_32693_34048 = state_32686__$1;
(statearr_32693_34048[(2)] = null);

(statearr_32693_34048[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32687 === (24))){
var inst_32665 = (state_32686[(7)]);
var inst_32674 = cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(mults,cljs.core.dissoc,inst_32665);
var state_32686__$1 = state_32686;
var statearr_32694_34049 = state_32686__$1;
(statearr_32694_34049[(2)] = inst_32674);

(statearr_32694_34049[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32687 === (4))){
var inst_32615 = (state_32686[(8)]);
var inst_32615__$1 = (state_32686[(2)]);
var inst_32616 = (inst_32615__$1 == null);
var state_32686__$1 = (function (){var statearr_32695 = state_32686;
(statearr_32695[(8)] = inst_32615__$1);

return statearr_32695;
})();
if(cljs.core.truth_(inst_32616)){
var statearr_32696_34051 = state_32686__$1;
(statearr_32696_34051[(1)] = (5));

} else {
var statearr_32697_34052 = state_32686__$1;
(statearr_32697_34052[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32687 === (15))){
var inst_32659 = (state_32686[(2)]);
var state_32686__$1 = state_32686;
var statearr_32699_34053 = state_32686__$1;
(statearr_32699_34053[(2)] = inst_32659);

(statearr_32699_34053[(1)] = (12));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32687 === (21))){
var inst_32679 = (state_32686[(2)]);
var state_32686__$1 = (function (){var statearr_32704 = state_32686;
(statearr_32704[(9)] = inst_32679);

return statearr_32704;
})();
var statearr_32705_34058 = state_32686__$1;
(statearr_32705_34058[(2)] = null);

(statearr_32705_34058[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32687 === (13))){
var inst_32640 = (state_32686[(10)]);
var inst_32643 = cljs.core.chunked_seq_QMARK_(inst_32640);
var state_32686__$1 = state_32686;
if(inst_32643){
var statearr_32707_34062 = state_32686__$1;
(statearr_32707_34062[(1)] = (16));

} else {
var statearr_32708_34064 = state_32686__$1;
(statearr_32708_34064[(1)] = (17));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32687 === (22))){
var inst_32671 = (state_32686[(2)]);
var state_32686__$1 = state_32686;
if(cljs.core.truth_(inst_32671)){
var statearr_32709_34071 = state_32686__$1;
(statearr_32709_34071[(1)] = (23));

} else {
var statearr_32710_34072 = state_32686__$1;
(statearr_32710_34072[(1)] = (24));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32687 === (6))){
var inst_32667 = (state_32686[(11)]);
var inst_32615 = (state_32686[(8)]);
var inst_32665 = (state_32686[(7)]);
var inst_32665__$1 = (topic_fn.cljs$core$IFn$_invoke$arity$1 ? topic_fn.cljs$core$IFn$_invoke$arity$1(inst_32615) : topic_fn.call(null,inst_32615));
var inst_32666 = cljs.core.deref(mults);
var inst_32667__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(inst_32666,inst_32665__$1);
var state_32686__$1 = (function (){var statearr_32711 = state_32686;
(statearr_32711[(11)] = inst_32667__$1);

(statearr_32711[(7)] = inst_32665__$1);

return statearr_32711;
})();
if(cljs.core.truth_(inst_32667__$1)){
var statearr_32713_34081 = state_32686__$1;
(statearr_32713_34081[(1)] = (19));

} else {
var statearr_32714_34082 = state_32686__$1;
(statearr_32714_34082[(1)] = (20));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32687 === (25))){
var inst_32676 = (state_32686[(2)]);
var state_32686__$1 = state_32686;
var statearr_32715_34083 = state_32686__$1;
(statearr_32715_34083[(2)] = inst_32676);

(statearr_32715_34083[(1)] = (21));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32687 === (17))){
var inst_32640 = (state_32686[(10)]);
var inst_32650 = cljs.core.first(inst_32640);
var inst_32651 = cljs.core.async.muxch_STAR_(inst_32650);
var inst_32652 = cljs.core.async.close_BANG_(inst_32651);
var inst_32653 = cljs.core.next(inst_32640);
var inst_32626 = inst_32653;
var inst_32627 = null;
var inst_32628 = (0);
var inst_32629 = (0);
var state_32686__$1 = (function (){var statearr_32716 = state_32686;
(statearr_32716[(12)] = inst_32627);

(statearr_32716[(13)] = inst_32626);

(statearr_32716[(14)] = inst_32629);

(statearr_32716[(15)] = inst_32652);

(statearr_32716[(16)] = inst_32628);

return statearr_32716;
})();
var statearr_32717_34087 = state_32686__$1;
(statearr_32717_34087[(2)] = null);

(statearr_32717_34087[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32687 === (3))){
var inst_32684 = (state_32686[(2)]);
var state_32686__$1 = state_32686;
return cljs.core.async.impl.ioc_helpers.return_chan(state_32686__$1,inst_32684);
} else {
if((state_val_32687 === (12))){
var inst_32661 = (state_32686[(2)]);
var state_32686__$1 = state_32686;
var statearr_32718_34088 = state_32686__$1;
(statearr_32718_34088[(2)] = inst_32661);

(statearr_32718_34088[(1)] = (9));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32687 === (2))){
var state_32686__$1 = state_32686;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_32686__$1,(4),ch);
} else {
if((state_val_32687 === (23))){
var state_32686__$1 = state_32686;
var statearr_32719_34089 = state_32686__$1;
(statearr_32719_34089[(2)] = null);

(statearr_32719_34089[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32687 === (19))){
var inst_32667 = (state_32686[(11)]);
var inst_32615 = (state_32686[(8)]);
var inst_32669 = cljs.core.async.muxch_STAR_(inst_32667);
var state_32686__$1 = state_32686;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_32686__$1,(22),inst_32669,inst_32615);
} else {
if((state_val_32687 === (11))){
var inst_32626 = (state_32686[(13)]);
var inst_32640 = (state_32686[(10)]);
var inst_32640__$1 = cljs.core.seq(inst_32626);
var state_32686__$1 = (function (){var statearr_32720 = state_32686;
(statearr_32720[(10)] = inst_32640__$1);

return statearr_32720;
})();
if(inst_32640__$1){
var statearr_32721_34090 = state_32686__$1;
(statearr_32721_34090[(1)] = (13));

} else {
var statearr_32722_34091 = state_32686__$1;
(statearr_32722_34091[(1)] = (14));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32687 === (9))){
var inst_32663 = (state_32686[(2)]);
var state_32686__$1 = state_32686;
var statearr_32724_34092 = state_32686__$1;
(statearr_32724_34092[(2)] = inst_32663);

(statearr_32724_34092[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32687 === (5))){
var inst_32622 = cljs.core.deref(mults);
var inst_32624 = cljs.core.vals(inst_32622);
var inst_32625 = cljs.core.seq(inst_32624);
var inst_32626 = inst_32625;
var inst_32627 = null;
var inst_32628 = (0);
var inst_32629 = (0);
var state_32686__$1 = (function (){var statearr_32725 = state_32686;
(statearr_32725[(12)] = inst_32627);

(statearr_32725[(13)] = inst_32626);

(statearr_32725[(14)] = inst_32629);

(statearr_32725[(16)] = inst_32628);

return statearr_32725;
})();
var statearr_32726_34102 = state_32686__$1;
(statearr_32726_34102[(2)] = null);

(statearr_32726_34102[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32687 === (14))){
var state_32686__$1 = state_32686;
var statearr_32730_34103 = state_32686__$1;
(statearr_32730_34103[(2)] = null);

(statearr_32730_34103[(1)] = (15));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32687 === (16))){
var inst_32640 = (state_32686[(10)]);
var inst_32645 = cljs.core.chunk_first(inst_32640);
var inst_32646 = cljs.core.chunk_rest(inst_32640);
var inst_32647 = cljs.core.count(inst_32645);
var inst_32626 = inst_32646;
var inst_32627 = inst_32645;
var inst_32628 = inst_32647;
var inst_32629 = (0);
var state_32686__$1 = (function (){var statearr_32731 = state_32686;
(statearr_32731[(12)] = inst_32627);

(statearr_32731[(13)] = inst_32626);

(statearr_32731[(14)] = inst_32629);

(statearr_32731[(16)] = inst_32628);

return statearr_32731;
})();
var statearr_32732_34104 = state_32686__$1;
(statearr_32732_34104[(2)] = null);

(statearr_32732_34104[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32687 === (10))){
var inst_32627 = (state_32686[(12)]);
var inst_32626 = (state_32686[(13)]);
var inst_32629 = (state_32686[(14)]);
var inst_32628 = (state_32686[(16)]);
var inst_32634 = cljs.core._nth(inst_32627,inst_32629);
var inst_32635 = cljs.core.async.muxch_STAR_(inst_32634);
var inst_32636 = cljs.core.async.close_BANG_(inst_32635);
var inst_32637 = (inst_32629 + (1));
var tmp32727 = inst_32627;
var tmp32728 = inst_32626;
var tmp32729 = inst_32628;
var inst_32626__$1 = tmp32728;
var inst_32627__$1 = tmp32727;
var inst_32628__$1 = tmp32729;
var inst_32629__$1 = inst_32637;
var state_32686__$1 = (function (){var statearr_32735 = state_32686;
(statearr_32735[(12)] = inst_32627__$1);

(statearr_32735[(13)] = inst_32626__$1);

(statearr_32735[(14)] = inst_32629__$1);

(statearr_32735[(17)] = inst_32636);

(statearr_32735[(16)] = inst_32628__$1);

return statearr_32735;
})();
var statearr_32736_34111 = state_32686__$1;
(statearr_32736_34111[(2)] = null);

(statearr_32736_34111[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32687 === (18))){
var inst_32656 = (state_32686[(2)]);
var state_32686__$1 = state_32686;
var statearr_32737_34112 = state_32686__$1;
(statearr_32737_34112[(2)] = inst_32656);

(statearr_32737_34112[(1)] = (15));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32687 === (8))){
var inst_32629 = (state_32686[(14)]);
var inst_32628 = (state_32686[(16)]);
var inst_32631 = (inst_32629 < inst_32628);
var inst_32632 = inst_32631;
var state_32686__$1 = state_32686;
if(cljs.core.truth_(inst_32632)){
var statearr_32738_34113 = state_32686__$1;
(statearr_32738_34113[(1)] = (10));

} else {
var statearr_32739_34114 = state_32686__$1;
(statearr_32739_34114[(1)] = (11));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$state_machine__30838__auto__ = null;
var cljs$core$async$state_machine__30838__auto____0 = (function (){
var statearr_32740 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_32740[(0)] = cljs$core$async$state_machine__30838__auto__);

(statearr_32740[(1)] = (1));

return statearr_32740;
});
var cljs$core$async$state_machine__30838__auto____1 = (function (state_32686){
while(true){
var ret_value__30839__auto__ = (function (){try{while(true){
var result__30840__auto__ = switch__30837__auto__(state_32686);
if(cljs.core.keyword_identical_QMARK_(result__30840__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__30840__auto__;
}
break;
}
}catch (e32741){var ex__30841__auto__ = e32741;
var statearr_32742_34115 = state_32686;
(statearr_32742_34115[(2)] = ex__30841__auto__);


if(cljs.core.seq((state_32686[(4)]))){
var statearr_32743_34116 = state_32686;
(statearr_32743_34116[(1)] = cljs.core.first((state_32686[(4)])));

} else {
throw ex__30841__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__30839__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__34117 = state_32686;
state_32686 = G__34117;
continue;
} else {
return ret_value__30839__auto__;
}
break;
}
});
cljs$core$async$state_machine__30838__auto__ = function(state_32686){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__30838__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__30838__auto____1.call(this,state_32686);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__30838__auto____0;
cljs$core$async$state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__30838__auto____1;
return cljs$core$async$state_machine__30838__auto__;
})()
})();
var state__31117__auto__ = (function (){var statearr_32744 = f__31116__auto__();
(statearr_32744[(6)] = c__31115__auto___34041);

return statearr_32744;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__31117__auto__);
}));


return p;
}));

(cljs.core.async.pub.cljs$lang$maxFixedArity = 3);

/**
 * Subscribes a channel to a topic of a pub.
 * 
 *   By default the channel will be closed when the source closes,
 *   but can be determined by the close? parameter.
 */
cljs.core.async.sub = (function cljs$core$async$sub(var_args){
var G__32748 = arguments.length;
switch (G__32748) {
case 3:
return cljs.core.async.sub.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 4:
return cljs.core.async.sub.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(cljs.core.async.sub.cljs$core$IFn$_invoke$arity$3 = (function (p,topic,ch){
return cljs.core.async.sub.cljs$core$IFn$_invoke$arity$4(p,topic,ch,true);
}));

(cljs.core.async.sub.cljs$core$IFn$_invoke$arity$4 = (function (p,topic,ch,close_QMARK_){
return cljs.core.async.sub_STAR_(p,topic,ch,close_QMARK_);
}));

(cljs.core.async.sub.cljs$lang$maxFixedArity = 4);

/**
 * Unsubscribes a channel from a topic of a pub
 */
cljs.core.async.unsub = (function cljs$core$async$unsub(p,topic,ch){
return cljs.core.async.unsub_STAR_(p,topic,ch);
});
/**
 * Unsubscribes all channels from a pub, or a topic of a pub
 */
cljs.core.async.unsub_all = (function cljs$core$async$unsub_all(var_args){
var G__32752 = arguments.length;
switch (G__32752) {
case 1:
return cljs.core.async.unsub_all.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return cljs.core.async.unsub_all.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(cljs.core.async.unsub_all.cljs$core$IFn$_invoke$arity$1 = (function (p){
return cljs.core.async.unsub_all_STAR_(p);
}));

(cljs.core.async.unsub_all.cljs$core$IFn$_invoke$arity$2 = (function (p,topic){
return cljs.core.async.unsub_all_STAR_(p,topic);
}));

(cljs.core.async.unsub_all.cljs$lang$maxFixedArity = 2);

/**
 * Takes a function and a collection of source channels, and returns a
 *   channel which contains the values produced by applying f to the set
 *   of first items taken from each source channel, followed by applying
 *   f to the set of second items from each channel, until any one of the
 *   channels is closed, at which point the output channel will be
 *   closed. The returned channel will be unbuffered by default, or a
 *   buf-or-n can be supplied
 */
cljs.core.async.map = (function cljs$core$async$map(var_args){
var G__32760 = arguments.length;
switch (G__32760) {
case 2:
return cljs.core.async.map.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.map.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(cljs.core.async.map.cljs$core$IFn$_invoke$arity$2 = (function (f,chs){
return cljs.core.async.map.cljs$core$IFn$_invoke$arity$3(f,chs,null);
}));

(cljs.core.async.map.cljs$core$IFn$_invoke$arity$3 = (function (f,chs,buf_or_n){
var chs__$1 = cljs.core.vec(chs);
var out = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(buf_or_n);
var cnt = cljs.core.count(chs__$1);
var rets = cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(cnt);
var dchan = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
var dctr = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
var done = cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (i){
return (function (ret){
(rets[i] = ret);

if((cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(dctr,cljs.core.dec) === (0))){
return cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$2(dchan,rets.slice((0)));
} else {
return null;
}
});
}),cljs.core.range.cljs$core$IFn$_invoke$arity$1(cnt));
var c__31115__auto___34128 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__31116__auto__ = (function (){var switch__30837__auto__ = (function (state_32803){
var state_val_32804 = (state_32803[(1)]);
if((state_val_32804 === (7))){
var state_32803__$1 = state_32803;
var statearr_32805_34132 = state_32803__$1;
(statearr_32805_34132[(2)] = null);

(statearr_32805_34132[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32804 === (1))){
var state_32803__$1 = state_32803;
var statearr_32806_34133 = state_32803__$1;
(statearr_32806_34133[(2)] = null);

(statearr_32806_34133[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32804 === (4))){
var inst_32763 = (state_32803[(7)]);
var inst_32764 = (state_32803[(8)]);
var inst_32766 = (inst_32764 < inst_32763);
var state_32803__$1 = state_32803;
if(cljs.core.truth_(inst_32766)){
var statearr_32807_34140 = state_32803__$1;
(statearr_32807_34140[(1)] = (6));

} else {
var statearr_32808_34141 = state_32803__$1;
(statearr_32808_34141[(1)] = (7));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32804 === (15))){
var inst_32789 = (state_32803[(9)]);
var inst_32794 = cljs.core.apply.cljs$core$IFn$_invoke$arity$2(f,inst_32789);
var state_32803__$1 = state_32803;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_32803__$1,(17),out,inst_32794);
} else {
if((state_val_32804 === (13))){
var inst_32789 = (state_32803[(9)]);
var inst_32789__$1 = (state_32803[(2)]);
var inst_32790 = cljs.core.some(cljs.core.nil_QMARK_,inst_32789__$1);
var state_32803__$1 = (function (){var statearr_32809 = state_32803;
(statearr_32809[(9)] = inst_32789__$1);

return statearr_32809;
})();
if(cljs.core.truth_(inst_32790)){
var statearr_32810_34143 = state_32803__$1;
(statearr_32810_34143[(1)] = (14));

} else {
var statearr_32811_34144 = state_32803__$1;
(statearr_32811_34144[(1)] = (15));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32804 === (6))){
var state_32803__$1 = state_32803;
var statearr_32812_34145 = state_32803__$1;
(statearr_32812_34145[(2)] = null);

(statearr_32812_34145[(1)] = (9));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32804 === (17))){
var inst_32796 = (state_32803[(2)]);
var state_32803__$1 = (function (){var statearr_32814 = state_32803;
(statearr_32814[(10)] = inst_32796);

return statearr_32814;
})();
var statearr_32815_34146 = state_32803__$1;
(statearr_32815_34146[(2)] = null);

(statearr_32815_34146[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32804 === (3))){
var inst_32801 = (state_32803[(2)]);
var state_32803__$1 = state_32803;
return cljs.core.async.impl.ioc_helpers.return_chan(state_32803__$1,inst_32801);
} else {
if((state_val_32804 === (12))){
var _ = (function (){var statearr_32816 = state_32803;
(statearr_32816[(4)] = cljs.core.rest((state_32803[(4)])));

return statearr_32816;
})();
var state_32803__$1 = state_32803;
var ex32813 = (state_32803__$1[(2)]);
var statearr_32817_34147 = state_32803__$1;
(statearr_32817_34147[(5)] = ex32813);


if((ex32813 instanceof Object)){
var statearr_32818_34148 = state_32803__$1;
(statearr_32818_34148[(1)] = (11));

(statearr_32818_34148[(5)] = null);

} else {
throw ex32813;

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32804 === (2))){
var inst_32762 = cljs.core.reset_BANG_(dctr,cnt);
var inst_32763 = cnt;
var inst_32764 = (0);
var state_32803__$1 = (function (){var statearr_32819 = state_32803;
(statearr_32819[(7)] = inst_32763);

(statearr_32819[(8)] = inst_32764);

(statearr_32819[(11)] = inst_32762);

return statearr_32819;
})();
var statearr_32820_34155 = state_32803__$1;
(statearr_32820_34155[(2)] = null);

(statearr_32820_34155[(1)] = (4));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32804 === (11))){
var inst_32768 = (state_32803[(2)]);
var inst_32769 = cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(dctr,cljs.core.dec);
var state_32803__$1 = (function (){var statearr_32821 = state_32803;
(statearr_32821[(12)] = inst_32768);

return statearr_32821;
})();
var statearr_32822_34156 = state_32803__$1;
(statearr_32822_34156[(2)] = inst_32769);

(statearr_32822_34156[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32804 === (9))){
var inst_32764 = (state_32803[(8)]);
var _ = (function (){var statearr_32823 = state_32803;
(statearr_32823[(4)] = cljs.core.cons((12),(state_32803[(4)])));

return statearr_32823;
})();
var inst_32775 = (chs__$1.cljs$core$IFn$_invoke$arity$1 ? chs__$1.cljs$core$IFn$_invoke$arity$1(inst_32764) : chs__$1.call(null,inst_32764));
var inst_32776 = (done.cljs$core$IFn$_invoke$arity$1 ? done.cljs$core$IFn$_invoke$arity$1(inst_32764) : done.call(null,inst_32764));
var inst_32777 = cljs.core.async.take_BANG_.cljs$core$IFn$_invoke$arity$2(inst_32775,inst_32776);
var ___$1 = (function (){var statearr_32824 = state_32803;
(statearr_32824[(4)] = cljs.core.rest((state_32803[(4)])));

return statearr_32824;
})();
var state_32803__$1 = state_32803;
var statearr_32825_34157 = state_32803__$1;
(statearr_32825_34157[(2)] = inst_32777);

(statearr_32825_34157[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32804 === (5))){
var inst_32787 = (state_32803[(2)]);
var state_32803__$1 = (function (){var statearr_32826 = state_32803;
(statearr_32826[(13)] = inst_32787);

return statearr_32826;
})();
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_32803__$1,(13),dchan);
} else {
if((state_val_32804 === (14))){
var inst_32792 = cljs.core.async.close_BANG_(out);
var state_32803__$1 = state_32803;
var statearr_32827_34158 = state_32803__$1;
(statearr_32827_34158[(2)] = inst_32792);

(statearr_32827_34158[(1)] = (16));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32804 === (16))){
var inst_32799 = (state_32803[(2)]);
var state_32803__$1 = state_32803;
var statearr_32828_34159 = state_32803__$1;
(statearr_32828_34159[(2)] = inst_32799);

(statearr_32828_34159[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32804 === (10))){
var inst_32764 = (state_32803[(8)]);
var inst_32780 = (state_32803[(2)]);
var inst_32781 = (inst_32764 + (1));
var inst_32764__$1 = inst_32781;
var state_32803__$1 = (function (){var statearr_32829 = state_32803;
(statearr_32829[(14)] = inst_32780);

(statearr_32829[(8)] = inst_32764__$1);

return statearr_32829;
})();
var statearr_32830_34160 = state_32803__$1;
(statearr_32830_34160[(2)] = null);

(statearr_32830_34160[(1)] = (4));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32804 === (8))){
var inst_32785 = (state_32803[(2)]);
var state_32803__$1 = state_32803;
var statearr_32831_34161 = state_32803__$1;
(statearr_32831_34161[(2)] = inst_32785);

(statearr_32831_34161[(1)] = (5));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$state_machine__30838__auto__ = null;
var cljs$core$async$state_machine__30838__auto____0 = (function (){
var statearr_32832 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_32832[(0)] = cljs$core$async$state_machine__30838__auto__);

(statearr_32832[(1)] = (1));

return statearr_32832;
});
var cljs$core$async$state_machine__30838__auto____1 = (function (state_32803){
while(true){
var ret_value__30839__auto__ = (function (){try{while(true){
var result__30840__auto__ = switch__30837__auto__(state_32803);
if(cljs.core.keyword_identical_QMARK_(result__30840__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__30840__auto__;
}
break;
}
}catch (e32833){var ex__30841__auto__ = e32833;
var statearr_32834_34162 = state_32803;
(statearr_32834_34162[(2)] = ex__30841__auto__);


if(cljs.core.seq((state_32803[(4)]))){
var statearr_32835_34163 = state_32803;
(statearr_32835_34163[(1)] = cljs.core.first((state_32803[(4)])));

} else {
throw ex__30841__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__30839__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__34164 = state_32803;
state_32803 = G__34164;
continue;
} else {
return ret_value__30839__auto__;
}
break;
}
});
cljs$core$async$state_machine__30838__auto__ = function(state_32803){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__30838__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__30838__auto____1.call(this,state_32803);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__30838__auto____0;
cljs$core$async$state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__30838__auto____1;
return cljs$core$async$state_machine__30838__auto__;
})()
})();
var state__31117__auto__ = (function (){var statearr_32836 = f__31116__auto__();
(statearr_32836[(6)] = c__31115__auto___34128);

return statearr_32836;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__31117__auto__);
}));


return out;
}));

(cljs.core.async.map.cljs$lang$maxFixedArity = 3);

/**
 * Takes a collection of source channels and returns a channel which
 *   contains all values taken from them. The returned channel will be
 *   unbuffered by default, or a buf-or-n can be supplied. The channel
 *   will close after all the source channels have closed.
 */
cljs.core.async.merge = (function cljs$core$async$merge(var_args){
var G__32839 = arguments.length;
switch (G__32839) {
case 1:
return cljs.core.async.merge.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return cljs.core.async.merge.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(cljs.core.async.merge.cljs$core$IFn$_invoke$arity$1 = (function (chs){
return cljs.core.async.merge.cljs$core$IFn$_invoke$arity$2(chs,null);
}));

(cljs.core.async.merge.cljs$core$IFn$_invoke$arity$2 = (function (chs,buf_or_n){
var out = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(buf_or_n);
var c__31115__auto___34166 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__31116__auto__ = (function (){var switch__30837__auto__ = (function (state_32871){
var state_val_32872 = (state_32871[(1)]);
if((state_val_32872 === (7))){
var inst_32850 = (state_32871[(7)]);
var inst_32851 = (state_32871[(8)]);
var inst_32850__$1 = (state_32871[(2)]);
var inst_32851__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(inst_32850__$1,(0),null);
var inst_32852 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(inst_32850__$1,(1),null);
var inst_32853 = (inst_32851__$1 == null);
var state_32871__$1 = (function (){var statearr_32873 = state_32871;
(statearr_32873[(7)] = inst_32850__$1);

(statearr_32873[(8)] = inst_32851__$1);

(statearr_32873[(9)] = inst_32852);

return statearr_32873;
})();
if(cljs.core.truth_(inst_32853)){
var statearr_32874_34167 = state_32871__$1;
(statearr_32874_34167[(1)] = (8));

} else {
var statearr_32875_34168 = state_32871__$1;
(statearr_32875_34168[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32872 === (1))){
var inst_32840 = cljs.core.vec(chs);
var inst_32841 = inst_32840;
var state_32871__$1 = (function (){var statearr_32876 = state_32871;
(statearr_32876[(10)] = inst_32841);

return statearr_32876;
})();
var statearr_32877_34169 = state_32871__$1;
(statearr_32877_34169[(2)] = null);

(statearr_32877_34169[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32872 === (4))){
var inst_32841 = (state_32871[(10)]);
var state_32871__$1 = state_32871;
return cljs.core.async.ioc_alts_BANG_(state_32871__$1,(7),inst_32841);
} else {
if((state_val_32872 === (6))){
var inst_32867 = (state_32871[(2)]);
var state_32871__$1 = state_32871;
var statearr_32878_34170 = state_32871__$1;
(statearr_32878_34170[(2)] = inst_32867);

(statearr_32878_34170[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32872 === (3))){
var inst_32869 = (state_32871[(2)]);
var state_32871__$1 = state_32871;
return cljs.core.async.impl.ioc_helpers.return_chan(state_32871__$1,inst_32869);
} else {
if((state_val_32872 === (2))){
var inst_32841 = (state_32871[(10)]);
var inst_32843 = cljs.core.count(inst_32841);
var inst_32844 = (inst_32843 > (0));
var state_32871__$1 = state_32871;
if(cljs.core.truth_(inst_32844)){
var statearr_32887_34171 = state_32871__$1;
(statearr_32887_34171[(1)] = (4));

} else {
var statearr_32888_34172 = state_32871__$1;
(statearr_32888_34172[(1)] = (5));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32872 === (11))){
var inst_32841 = (state_32871[(10)]);
var inst_32860 = (state_32871[(2)]);
var tmp32882 = inst_32841;
var inst_32841__$1 = tmp32882;
var state_32871__$1 = (function (){var statearr_32889 = state_32871;
(statearr_32889[(11)] = inst_32860);

(statearr_32889[(10)] = inst_32841__$1);

return statearr_32889;
})();
var statearr_32890_34175 = state_32871__$1;
(statearr_32890_34175[(2)] = null);

(statearr_32890_34175[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32872 === (9))){
var inst_32851 = (state_32871[(8)]);
var state_32871__$1 = state_32871;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_32871__$1,(11),out,inst_32851);
} else {
if((state_val_32872 === (5))){
var inst_32865 = cljs.core.async.close_BANG_(out);
var state_32871__$1 = state_32871;
var statearr_32893_34178 = state_32871__$1;
(statearr_32893_34178[(2)] = inst_32865);

(statearr_32893_34178[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32872 === (10))){
var inst_32863 = (state_32871[(2)]);
var state_32871__$1 = state_32871;
var statearr_32894_34179 = state_32871__$1;
(statearr_32894_34179[(2)] = inst_32863);

(statearr_32894_34179[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32872 === (8))){
var inst_32850 = (state_32871[(7)]);
var inst_32851 = (state_32871[(8)]);
var inst_32852 = (state_32871[(9)]);
var inst_32841 = (state_32871[(10)]);
var inst_32855 = (function (){var cs = inst_32841;
var vec__32846 = inst_32850;
var v = inst_32851;
var c = inst_32852;
return (function (p1__32837_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(c,p1__32837_SHARP_);
});
})();
var inst_32856 = cljs.core.filterv(inst_32855,inst_32841);
var inst_32841__$1 = inst_32856;
var state_32871__$1 = (function (){var statearr_32895 = state_32871;
(statearr_32895[(10)] = inst_32841__$1);

return statearr_32895;
})();
var statearr_32896_34180 = state_32871__$1;
(statearr_32896_34180[(2)] = null);

(statearr_32896_34180[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$state_machine__30838__auto__ = null;
var cljs$core$async$state_machine__30838__auto____0 = (function (){
var statearr_32897 = [null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_32897[(0)] = cljs$core$async$state_machine__30838__auto__);

(statearr_32897[(1)] = (1));

return statearr_32897;
});
var cljs$core$async$state_machine__30838__auto____1 = (function (state_32871){
while(true){
var ret_value__30839__auto__ = (function (){try{while(true){
var result__30840__auto__ = switch__30837__auto__(state_32871);
if(cljs.core.keyword_identical_QMARK_(result__30840__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__30840__auto__;
}
break;
}
}catch (e32898){var ex__30841__auto__ = e32898;
var statearr_32899_34181 = state_32871;
(statearr_32899_34181[(2)] = ex__30841__auto__);


if(cljs.core.seq((state_32871[(4)]))){
var statearr_32900_34182 = state_32871;
(statearr_32900_34182[(1)] = cljs.core.first((state_32871[(4)])));

} else {
throw ex__30841__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__30839__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__34183 = state_32871;
state_32871 = G__34183;
continue;
} else {
return ret_value__30839__auto__;
}
break;
}
});
cljs$core$async$state_machine__30838__auto__ = function(state_32871){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__30838__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__30838__auto____1.call(this,state_32871);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__30838__auto____0;
cljs$core$async$state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__30838__auto____1;
return cljs$core$async$state_machine__30838__auto__;
})()
})();
var state__31117__auto__ = (function (){var statearr_32901 = f__31116__auto__();
(statearr_32901[(6)] = c__31115__auto___34166);

return statearr_32901;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__31117__auto__);
}));


return out;
}));

(cljs.core.async.merge.cljs$lang$maxFixedArity = 2);

/**
 * Returns a channel containing the single (collection) result of the
 *   items taken from the channel conjoined to the supplied
 *   collection. ch must close before into produces a result.
 */
cljs.core.async.into = (function cljs$core$async$into(coll,ch){
return cljs.core.async.reduce(cljs.core.conj,coll,ch);
});
/**
 * Returns a channel that will return, at most, n items from ch. After n items
 * have been returned, or ch has been closed, the return chanel will close.
 * 
 *   The output channel is unbuffered by default, unless buf-or-n is given.
 */
cljs.core.async.take = (function cljs$core$async$take(var_args){
var G__32903 = arguments.length;
switch (G__32903) {
case 2:
return cljs.core.async.take.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.take.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(cljs.core.async.take.cljs$core$IFn$_invoke$arity$2 = (function (n,ch){
return cljs.core.async.take.cljs$core$IFn$_invoke$arity$3(n,ch,null);
}));

(cljs.core.async.take.cljs$core$IFn$_invoke$arity$3 = (function (n,ch,buf_or_n){
var out = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(buf_or_n);
var c__31115__auto___34191 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__31116__auto__ = (function (){var switch__30837__auto__ = (function (state_32927){
var state_val_32928 = (state_32927[(1)]);
if((state_val_32928 === (7))){
var inst_32909 = (state_32927[(7)]);
var inst_32909__$1 = (state_32927[(2)]);
var inst_32910 = (inst_32909__$1 == null);
var inst_32911 = cljs.core.not(inst_32910);
var state_32927__$1 = (function (){var statearr_32929 = state_32927;
(statearr_32929[(7)] = inst_32909__$1);

return statearr_32929;
})();
if(inst_32911){
var statearr_32930_34192 = state_32927__$1;
(statearr_32930_34192[(1)] = (8));

} else {
var statearr_32931_34193 = state_32927__$1;
(statearr_32931_34193[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32928 === (1))){
var inst_32904 = (0);
var state_32927__$1 = (function (){var statearr_32932 = state_32927;
(statearr_32932[(8)] = inst_32904);

return statearr_32932;
})();
var statearr_32933_34200 = state_32927__$1;
(statearr_32933_34200[(2)] = null);

(statearr_32933_34200[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32928 === (4))){
var state_32927__$1 = state_32927;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_32927__$1,(7),ch);
} else {
if((state_val_32928 === (6))){
var inst_32922 = (state_32927[(2)]);
var state_32927__$1 = state_32927;
var statearr_32934_34201 = state_32927__$1;
(statearr_32934_34201[(2)] = inst_32922);

(statearr_32934_34201[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32928 === (3))){
var inst_32924 = (state_32927[(2)]);
var inst_32925 = cljs.core.async.close_BANG_(out);
var state_32927__$1 = (function (){var statearr_32935 = state_32927;
(statearr_32935[(9)] = inst_32924);

return statearr_32935;
})();
return cljs.core.async.impl.ioc_helpers.return_chan(state_32927__$1,inst_32925);
} else {
if((state_val_32928 === (2))){
var inst_32904 = (state_32927[(8)]);
var inst_32906 = (inst_32904 < n);
var state_32927__$1 = state_32927;
if(cljs.core.truth_(inst_32906)){
var statearr_32936_34202 = state_32927__$1;
(statearr_32936_34202[(1)] = (4));

} else {
var statearr_32937_34203 = state_32927__$1;
(statearr_32937_34203[(1)] = (5));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32928 === (11))){
var inst_32904 = (state_32927[(8)]);
var inst_32914 = (state_32927[(2)]);
var inst_32915 = (inst_32904 + (1));
var inst_32904__$1 = inst_32915;
var state_32927__$1 = (function (){var statearr_32938 = state_32927;
(statearr_32938[(10)] = inst_32914);

(statearr_32938[(8)] = inst_32904__$1);

return statearr_32938;
})();
var statearr_32939_34204 = state_32927__$1;
(statearr_32939_34204[(2)] = null);

(statearr_32939_34204[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32928 === (9))){
var state_32927__$1 = state_32927;
var statearr_32940_34205 = state_32927__$1;
(statearr_32940_34205[(2)] = null);

(statearr_32940_34205[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32928 === (5))){
var state_32927__$1 = state_32927;
var statearr_32941_34206 = state_32927__$1;
(statearr_32941_34206[(2)] = null);

(statearr_32941_34206[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32928 === (10))){
var inst_32919 = (state_32927[(2)]);
var state_32927__$1 = state_32927;
var statearr_32942_34207 = state_32927__$1;
(statearr_32942_34207[(2)] = inst_32919);

(statearr_32942_34207[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32928 === (8))){
var inst_32909 = (state_32927[(7)]);
var state_32927__$1 = state_32927;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_32927__$1,(11),out,inst_32909);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$state_machine__30838__auto__ = null;
var cljs$core$async$state_machine__30838__auto____0 = (function (){
var statearr_32946 = [null,null,null,null,null,null,null,null,null,null,null];
(statearr_32946[(0)] = cljs$core$async$state_machine__30838__auto__);

(statearr_32946[(1)] = (1));

return statearr_32946;
});
var cljs$core$async$state_machine__30838__auto____1 = (function (state_32927){
while(true){
var ret_value__30839__auto__ = (function (){try{while(true){
var result__30840__auto__ = switch__30837__auto__(state_32927);
if(cljs.core.keyword_identical_QMARK_(result__30840__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__30840__auto__;
}
break;
}
}catch (e32947){var ex__30841__auto__ = e32947;
var statearr_32948_34208 = state_32927;
(statearr_32948_34208[(2)] = ex__30841__auto__);


if(cljs.core.seq((state_32927[(4)]))){
var statearr_32949_34209 = state_32927;
(statearr_32949_34209[(1)] = cljs.core.first((state_32927[(4)])));

} else {
throw ex__30841__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__30839__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__34210 = state_32927;
state_32927 = G__34210;
continue;
} else {
return ret_value__30839__auto__;
}
break;
}
});
cljs$core$async$state_machine__30838__auto__ = function(state_32927){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__30838__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__30838__auto____1.call(this,state_32927);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__30838__auto____0;
cljs$core$async$state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__30838__auto____1;
return cljs$core$async$state_machine__30838__auto__;
})()
})();
var state__31117__auto__ = (function (){var statearr_32951 = f__31116__auto__();
(statearr_32951[(6)] = c__31115__auto___34191);

return statearr_32951;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__31117__auto__);
}));


return out;
}));

(cljs.core.async.take.cljs$lang$maxFixedArity = 3);

/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.map_LT_ = (function cljs$core$async$map_LT_(f,ch){
if((typeof cljs !== 'undefined') && (typeof cljs.core !== 'undefined') && (typeof cljs.core.async !== 'undefined') && (typeof cljs.core.async.t_cljs$core$async32958 !== 'undefined')){
} else {

/**
* @constructor
 * @implements {cljs.core.async.impl.protocols.Channel}
 * @implements {cljs.core.async.impl.protocols.WritePort}
 * @implements {cljs.core.async.impl.protocols.ReadPort}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async32958 = (function (f,ch,meta32959){
this.f = f;
this.ch = ch;
this.meta32959 = meta32959;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(cljs.core.async.t_cljs$core$async32958.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_32960,meta32959__$1){
var self__ = this;
var _32960__$1 = this;
return (new cljs.core.async.t_cljs$core$async32958(self__.f,self__.ch,meta32959__$1));
}));

(cljs.core.async.t_cljs$core$async32958.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_32960){
var self__ = this;
var _32960__$1 = this;
return self__.meta32959;
}));

(cljs.core.async.t_cljs$core$async32958.prototype.cljs$core$async$impl$protocols$Channel$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async32958.prototype.cljs$core$async$impl$protocols$Channel$close_BANG_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.close_BANG_(self__.ch);
}));

(cljs.core.async.t_cljs$core$async32958.prototype.cljs$core$async$impl$protocols$Channel$closed_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.closed_QMARK_(self__.ch);
}));

(cljs.core.async.t_cljs$core$async32958.prototype.cljs$core$async$impl$protocols$ReadPort$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async32958.prototype.cljs$core$async$impl$protocols$ReadPort$take_BANG_$arity$2 = (function (_,fn1){
var self__ = this;
var ___$1 = this;
var ret = cljs.core.async.impl.protocols.take_BANG_(self__.ch,(function (){
if((typeof cljs !== 'undefined') && (typeof cljs.core !== 'undefined') && (typeof cljs.core.async !== 'undefined') && (typeof cljs.core.async.t_cljs$core$async32961 !== 'undefined')){
} else {

/**
* @constructor
 * @implements {cljs.core.async.impl.protocols.Handler}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async32961 = (function (f,ch,meta32959,_,fn1,meta32962){
this.f = f;
this.ch = ch;
this.meta32959 = meta32959;
this._ = _;
this.fn1 = fn1;
this.meta32962 = meta32962;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(cljs.core.async.t_cljs$core$async32961.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_32963,meta32962__$1){
var self__ = this;
var _32963__$1 = this;
return (new cljs.core.async.t_cljs$core$async32961(self__.f,self__.ch,self__.meta32959,self__._,self__.fn1,meta32962__$1));
}));

(cljs.core.async.t_cljs$core$async32961.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_32963){
var self__ = this;
var _32963__$1 = this;
return self__.meta32962;
}));

(cljs.core.async.t_cljs$core$async32961.prototype.cljs$core$async$impl$protocols$Handler$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async32961.prototype.cljs$core$async$impl$protocols$Handler$active_QMARK_$arity$1 = (function (___$1){
var self__ = this;
var ___$2 = this;
return cljs.core.async.impl.protocols.active_QMARK_(self__.fn1);
}));

(cljs.core.async.t_cljs$core$async32961.prototype.cljs$core$async$impl$protocols$Handler$blockable_QMARK_$arity$1 = (function (___$1){
var self__ = this;
var ___$2 = this;
return true;
}));

(cljs.core.async.t_cljs$core$async32961.prototype.cljs$core$async$impl$protocols$Handler$commit$arity$1 = (function (___$1){
var self__ = this;
var ___$2 = this;
var f1 = cljs.core.async.impl.protocols.commit(self__.fn1);
return (function (p1__32957_SHARP_){
var G__32968 = (((p1__32957_SHARP_ == null))?null:(self__.f.cljs$core$IFn$_invoke$arity$1 ? self__.f.cljs$core$IFn$_invoke$arity$1(p1__32957_SHARP_) : self__.f.call(null,p1__32957_SHARP_)));
return (f1.cljs$core$IFn$_invoke$arity$1 ? f1.cljs$core$IFn$_invoke$arity$1(G__32968) : f1.call(null,G__32968));
});
}));

(cljs.core.async.t_cljs$core$async32961.getBasis = (function (){
return new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"f","f",43394975,null),new cljs.core.Symbol(null,"ch","ch",1085813622,null),new cljs.core.Symbol(null,"meta32959","meta32959",-1634693683,null),cljs.core.with_meta(new cljs.core.Symbol(null,"_","_",-1201019570,null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"tag","tag",-1290361223),new cljs.core.Symbol("cljs.core.async","t_cljs$core$async32958","cljs.core.async/t_cljs$core$async32958",-1181668768,null)], null)),new cljs.core.Symbol(null,"fn1","fn1",895834444,null),new cljs.core.Symbol(null,"meta32962","meta32962",1731479516,null)], null);
}));

(cljs.core.async.t_cljs$core$async32961.cljs$lang$type = true);

(cljs.core.async.t_cljs$core$async32961.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async32961");

(cljs.core.async.t_cljs$core$async32961.cljs$lang$ctorPrWriter = (function (this__4369__auto__,writer__4370__auto__,opt__4371__auto__){
return cljs.core._write(writer__4370__auto__,"cljs.core.async/t_cljs$core$async32961");
}));

/**
 * Positional factory function for cljs.core.async/t_cljs$core$async32961.
 */
cljs.core.async.__GT_t_cljs$core$async32961 = (function cljs$core$async$map_LT__$___GT_t_cljs$core$async32961(f__$1,ch__$1,meta32959__$1,___$2,fn1__$1,meta32962){
return (new cljs.core.async.t_cljs$core$async32961(f__$1,ch__$1,meta32959__$1,___$2,fn1__$1,meta32962));
});

}

return (new cljs.core.async.t_cljs$core$async32961(self__.f,self__.ch,self__.meta32959,___$1,fn1,cljs.core.PersistentArrayMap.EMPTY));
})()
);
if(cljs.core.truth_((function (){var and__4115__auto__ = ret;
if(cljs.core.truth_(and__4115__auto__)){
return (!((cljs.core.deref(ret) == null)));
} else {
return and__4115__auto__;
}
})())){
return cljs.core.async.impl.channels.box((function (){var G__32997 = cljs.core.deref(ret);
return (self__.f.cljs$core$IFn$_invoke$arity$1 ? self__.f.cljs$core$IFn$_invoke$arity$1(G__32997) : self__.f.call(null,G__32997));
})());
} else {
return ret;
}
}));

(cljs.core.async.t_cljs$core$async32958.prototype.cljs$core$async$impl$protocols$WritePort$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async32958.prototype.cljs$core$async$impl$protocols$WritePort$put_BANG_$arity$3 = (function (_,val,fn1){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.put_BANG_(self__.ch,val,fn1);
}));

(cljs.core.async.t_cljs$core$async32958.getBasis = (function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"f","f",43394975,null),new cljs.core.Symbol(null,"ch","ch",1085813622,null),new cljs.core.Symbol(null,"meta32959","meta32959",-1634693683,null)], null);
}));

(cljs.core.async.t_cljs$core$async32958.cljs$lang$type = true);

(cljs.core.async.t_cljs$core$async32958.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async32958");

(cljs.core.async.t_cljs$core$async32958.cljs$lang$ctorPrWriter = (function (this__4369__auto__,writer__4370__auto__,opt__4371__auto__){
return cljs.core._write(writer__4370__auto__,"cljs.core.async/t_cljs$core$async32958");
}));

/**
 * Positional factory function for cljs.core.async/t_cljs$core$async32958.
 */
cljs.core.async.__GT_t_cljs$core$async32958 = (function cljs$core$async$map_LT__$___GT_t_cljs$core$async32958(f__$1,ch__$1,meta32959){
return (new cljs.core.async.t_cljs$core$async32958(f__$1,ch__$1,meta32959));
});

}

return (new cljs.core.async.t_cljs$core$async32958(f,ch,cljs.core.PersistentArrayMap.EMPTY));
});
/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.map_GT_ = (function cljs$core$async$map_GT_(f,ch){
if((typeof cljs !== 'undefined') && (typeof cljs.core !== 'undefined') && (typeof cljs.core.async !== 'undefined') && (typeof cljs.core.async.t_cljs$core$async33002 !== 'undefined')){
} else {

/**
* @constructor
 * @implements {cljs.core.async.impl.protocols.Channel}
 * @implements {cljs.core.async.impl.protocols.WritePort}
 * @implements {cljs.core.async.impl.protocols.ReadPort}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async33002 = (function (f,ch,meta33003){
this.f = f;
this.ch = ch;
this.meta33003 = meta33003;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(cljs.core.async.t_cljs$core$async33002.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_33004,meta33003__$1){
var self__ = this;
var _33004__$1 = this;
return (new cljs.core.async.t_cljs$core$async33002(self__.f,self__.ch,meta33003__$1));
}));

(cljs.core.async.t_cljs$core$async33002.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_33004){
var self__ = this;
var _33004__$1 = this;
return self__.meta33003;
}));

(cljs.core.async.t_cljs$core$async33002.prototype.cljs$core$async$impl$protocols$Channel$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async33002.prototype.cljs$core$async$impl$protocols$Channel$close_BANG_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.close_BANG_(self__.ch);
}));

(cljs.core.async.t_cljs$core$async33002.prototype.cljs$core$async$impl$protocols$ReadPort$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async33002.prototype.cljs$core$async$impl$protocols$ReadPort$take_BANG_$arity$2 = (function (_,fn1){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.take_BANG_(self__.ch,fn1);
}));

(cljs.core.async.t_cljs$core$async33002.prototype.cljs$core$async$impl$protocols$WritePort$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async33002.prototype.cljs$core$async$impl$protocols$WritePort$put_BANG_$arity$3 = (function (_,val,fn1){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.put_BANG_(self__.ch,(self__.f.cljs$core$IFn$_invoke$arity$1 ? self__.f.cljs$core$IFn$_invoke$arity$1(val) : self__.f.call(null,val)),fn1);
}));

(cljs.core.async.t_cljs$core$async33002.getBasis = (function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"f","f",43394975,null),new cljs.core.Symbol(null,"ch","ch",1085813622,null),new cljs.core.Symbol(null,"meta33003","meta33003",1992060504,null)], null);
}));

(cljs.core.async.t_cljs$core$async33002.cljs$lang$type = true);

(cljs.core.async.t_cljs$core$async33002.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async33002");

(cljs.core.async.t_cljs$core$async33002.cljs$lang$ctorPrWriter = (function (this__4369__auto__,writer__4370__auto__,opt__4371__auto__){
return cljs.core._write(writer__4370__auto__,"cljs.core.async/t_cljs$core$async33002");
}));

/**
 * Positional factory function for cljs.core.async/t_cljs$core$async33002.
 */
cljs.core.async.__GT_t_cljs$core$async33002 = (function cljs$core$async$map_GT__$___GT_t_cljs$core$async33002(f__$1,ch__$1,meta33003){
return (new cljs.core.async.t_cljs$core$async33002(f__$1,ch__$1,meta33003));
});

}

return (new cljs.core.async.t_cljs$core$async33002(f,ch,cljs.core.PersistentArrayMap.EMPTY));
});
/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.filter_GT_ = (function cljs$core$async$filter_GT_(p,ch){
if((typeof cljs !== 'undefined') && (typeof cljs.core !== 'undefined') && (typeof cljs.core.async !== 'undefined') && (typeof cljs.core.async.t_cljs$core$async33006 !== 'undefined')){
} else {

/**
* @constructor
 * @implements {cljs.core.async.impl.protocols.Channel}
 * @implements {cljs.core.async.impl.protocols.WritePort}
 * @implements {cljs.core.async.impl.protocols.ReadPort}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async33006 = (function (p,ch,meta33007){
this.p = p;
this.ch = ch;
this.meta33007 = meta33007;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(cljs.core.async.t_cljs$core$async33006.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_33008,meta33007__$1){
var self__ = this;
var _33008__$1 = this;
return (new cljs.core.async.t_cljs$core$async33006(self__.p,self__.ch,meta33007__$1));
}));

(cljs.core.async.t_cljs$core$async33006.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_33008){
var self__ = this;
var _33008__$1 = this;
return self__.meta33007;
}));

(cljs.core.async.t_cljs$core$async33006.prototype.cljs$core$async$impl$protocols$Channel$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async33006.prototype.cljs$core$async$impl$protocols$Channel$close_BANG_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.close_BANG_(self__.ch);
}));

(cljs.core.async.t_cljs$core$async33006.prototype.cljs$core$async$impl$protocols$Channel$closed_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.closed_QMARK_(self__.ch);
}));

(cljs.core.async.t_cljs$core$async33006.prototype.cljs$core$async$impl$protocols$ReadPort$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async33006.prototype.cljs$core$async$impl$protocols$ReadPort$take_BANG_$arity$2 = (function (_,fn1){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.take_BANG_(self__.ch,fn1);
}));

(cljs.core.async.t_cljs$core$async33006.prototype.cljs$core$async$impl$protocols$WritePort$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async33006.prototype.cljs$core$async$impl$protocols$WritePort$put_BANG_$arity$3 = (function (_,val,fn1){
var self__ = this;
var ___$1 = this;
if(cljs.core.truth_((self__.p.cljs$core$IFn$_invoke$arity$1 ? self__.p.cljs$core$IFn$_invoke$arity$1(val) : self__.p.call(null,val)))){
return cljs.core.async.impl.protocols.put_BANG_(self__.ch,val,fn1);
} else {
return cljs.core.async.impl.channels.box(cljs.core.not(cljs.core.async.impl.protocols.closed_QMARK_(self__.ch)));
}
}));

(cljs.core.async.t_cljs$core$async33006.getBasis = (function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"p","p",1791580836,null),new cljs.core.Symbol(null,"ch","ch",1085813622,null),new cljs.core.Symbol(null,"meta33007","meta33007",-828193618,null)], null);
}));

(cljs.core.async.t_cljs$core$async33006.cljs$lang$type = true);

(cljs.core.async.t_cljs$core$async33006.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async33006");

(cljs.core.async.t_cljs$core$async33006.cljs$lang$ctorPrWriter = (function (this__4369__auto__,writer__4370__auto__,opt__4371__auto__){
return cljs.core._write(writer__4370__auto__,"cljs.core.async/t_cljs$core$async33006");
}));

/**
 * Positional factory function for cljs.core.async/t_cljs$core$async33006.
 */
cljs.core.async.__GT_t_cljs$core$async33006 = (function cljs$core$async$filter_GT__$___GT_t_cljs$core$async33006(p__$1,ch__$1,meta33007){
return (new cljs.core.async.t_cljs$core$async33006(p__$1,ch__$1,meta33007));
});

}

return (new cljs.core.async.t_cljs$core$async33006(p,ch,cljs.core.PersistentArrayMap.EMPTY));
});
/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.remove_GT_ = (function cljs$core$async$remove_GT_(p,ch){
return cljs.core.async.filter_GT_(cljs.core.complement(p),ch);
});
/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.filter_LT_ = (function cljs$core$async$filter_LT_(var_args){
var G__33013 = arguments.length;
switch (G__33013) {
case 2:
return cljs.core.async.filter_LT_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.filter_LT_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(cljs.core.async.filter_LT_.cljs$core$IFn$_invoke$arity$2 = (function (p,ch){
return cljs.core.async.filter_LT_.cljs$core$IFn$_invoke$arity$3(p,ch,null);
}));

(cljs.core.async.filter_LT_.cljs$core$IFn$_invoke$arity$3 = (function (p,ch,buf_or_n){
var out = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(buf_or_n);
var c__31115__auto___34216 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__31116__auto__ = (function (){var switch__30837__auto__ = (function (state_33036){
var state_val_33037 = (state_33036[(1)]);
if((state_val_33037 === (7))){
var inst_33032 = (state_33036[(2)]);
var state_33036__$1 = state_33036;
var statearr_33039_34217 = state_33036__$1;
(statearr_33039_34217[(2)] = inst_33032);

(statearr_33039_34217[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33037 === (1))){
var state_33036__$1 = state_33036;
var statearr_33040_34218 = state_33036__$1;
(statearr_33040_34218[(2)] = null);

(statearr_33040_34218[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33037 === (4))){
var inst_33017 = (state_33036[(7)]);
var inst_33017__$1 = (state_33036[(2)]);
var inst_33019 = (inst_33017__$1 == null);
var state_33036__$1 = (function (){var statearr_33041 = state_33036;
(statearr_33041[(7)] = inst_33017__$1);

return statearr_33041;
})();
if(cljs.core.truth_(inst_33019)){
var statearr_33042_34220 = state_33036__$1;
(statearr_33042_34220[(1)] = (5));

} else {
var statearr_33043_34221 = state_33036__$1;
(statearr_33043_34221[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33037 === (6))){
var inst_33017 = (state_33036[(7)]);
var inst_33023 = (p.cljs$core$IFn$_invoke$arity$1 ? p.cljs$core$IFn$_invoke$arity$1(inst_33017) : p.call(null,inst_33017));
var state_33036__$1 = state_33036;
if(cljs.core.truth_(inst_33023)){
var statearr_33044_34222 = state_33036__$1;
(statearr_33044_34222[(1)] = (8));

} else {
var statearr_33045_34223 = state_33036__$1;
(statearr_33045_34223[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33037 === (3))){
var inst_33034 = (state_33036[(2)]);
var state_33036__$1 = state_33036;
return cljs.core.async.impl.ioc_helpers.return_chan(state_33036__$1,inst_33034);
} else {
if((state_val_33037 === (2))){
var state_33036__$1 = state_33036;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_33036__$1,(4),ch);
} else {
if((state_val_33037 === (11))){
var inst_33026 = (state_33036[(2)]);
var state_33036__$1 = state_33036;
var statearr_33051_34225 = state_33036__$1;
(statearr_33051_34225[(2)] = inst_33026);

(statearr_33051_34225[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33037 === (9))){
var state_33036__$1 = state_33036;
var statearr_33052_34226 = state_33036__$1;
(statearr_33052_34226[(2)] = null);

(statearr_33052_34226[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33037 === (5))){
var inst_33021 = cljs.core.async.close_BANG_(out);
var state_33036__$1 = state_33036;
var statearr_33053_34227 = state_33036__$1;
(statearr_33053_34227[(2)] = inst_33021);

(statearr_33053_34227[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33037 === (10))){
var inst_33029 = (state_33036[(2)]);
var state_33036__$1 = (function (){var statearr_33054 = state_33036;
(statearr_33054[(8)] = inst_33029);

return statearr_33054;
})();
var statearr_33055_34249 = state_33036__$1;
(statearr_33055_34249[(2)] = null);

(statearr_33055_34249[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33037 === (8))){
var inst_33017 = (state_33036[(7)]);
var state_33036__$1 = state_33036;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_33036__$1,(11),out,inst_33017);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$state_machine__30838__auto__ = null;
var cljs$core$async$state_machine__30838__auto____0 = (function (){
var statearr_33056 = [null,null,null,null,null,null,null,null,null];
(statearr_33056[(0)] = cljs$core$async$state_machine__30838__auto__);

(statearr_33056[(1)] = (1));

return statearr_33056;
});
var cljs$core$async$state_machine__30838__auto____1 = (function (state_33036){
while(true){
var ret_value__30839__auto__ = (function (){try{while(true){
var result__30840__auto__ = switch__30837__auto__(state_33036);
if(cljs.core.keyword_identical_QMARK_(result__30840__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__30840__auto__;
}
break;
}
}catch (e33057){var ex__30841__auto__ = e33057;
var statearr_33058_34254 = state_33036;
(statearr_33058_34254[(2)] = ex__30841__auto__);


if(cljs.core.seq((state_33036[(4)]))){
var statearr_33059_34255 = state_33036;
(statearr_33059_34255[(1)] = cljs.core.first((state_33036[(4)])));

} else {
throw ex__30841__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__30839__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__34256 = state_33036;
state_33036 = G__34256;
continue;
} else {
return ret_value__30839__auto__;
}
break;
}
});
cljs$core$async$state_machine__30838__auto__ = function(state_33036){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__30838__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__30838__auto____1.call(this,state_33036);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__30838__auto____0;
cljs$core$async$state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__30838__auto____1;
return cljs$core$async$state_machine__30838__auto__;
})()
})();
var state__31117__auto__ = (function (){var statearr_33060 = f__31116__auto__();
(statearr_33060[(6)] = c__31115__auto___34216);

return statearr_33060;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__31117__auto__);
}));


return out;
}));

(cljs.core.async.filter_LT_.cljs$lang$maxFixedArity = 3);

/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.remove_LT_ = (function cljs$core$async$remove_LT_(var_args){
var G__33062 = arguments.length;
switch (G__33062) {
case 2:
return cljs.core.async.remove_LT_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.remove_LT_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(cljs.core.async.remove_LT_.cljs$core$IFn$_invoke$arity$2 = (function (p,ch){
return cljs.core.async.remove_LT_.cljs$core$IFn$_invoke$arity$3(p,ch,null);
}));

(cljs.core.async.remove_LT_.cljs$core$IFn$_invoke$arity$3 = (function (p,ch,buf_or_n){
return cljs.core.async.filter_LT_.cljs$core$IFn$_invoke$arity$3(cljs.core.complement(p),ch,buf_or_n);
}));

(cljs.core.async.remove_LT_.cljs$lang$maxFixedArity = 3);

cljs.core.async.mapcat_STAR_ = (function cljs$core$async$mapcat_STAR_(f,in$,out){
var c__31115__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__31116__auto__ = (function (){var switch__30837__auto__ = (function (state_33127){
var state_val_33128 = (state_33127[(1)]);
if((state_val_33128 === (7))){
var inst_33123 = (state_33127[(2)]);
var state_33127__$1 = state_33127;
var statearr_33129_34260 = state_33127__$1;
(statearr_33129_34260[(2)] = inst_33123);

(statearr_33129_34260[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33128 === (20))){
var inst_33091 = (state_33127[(7)]);
var inst_33104 = (state_33127[(2)]);
var inst_33105 = cljs.core.next(inst_33091);
var inst_33076 = inst_33105;
var inst_33077 = null;
var inst_33078 = (0);
var inst_33079 = (0);
var state_33127__$1 = (function (){var statearr_33130 = state_33127;
(statearr_33130[(8)] = inst_33104);

(statearr_33130[(9)] = inst_33076);

(statearr_33130[(10)] = inst_33078);

(statearr_33130[(11)] = inst_33077);

(statearr_33130[(12)] = inst_33079);

return statearr_33130;
})();
var statearr_33131_34261 = state_33127__$1;
(statearr_33131_34261[(2)] = null);

(statearr_33131_34261[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33128 === (1))){
var state_33127__$1 = state_33127;
var statearr_33132_34262 = state_33127__$1;
(statearr_33132_34262[(2)] = null);

(statearr_33132_34262[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33128 === (4))){
var inst_33065 = (state_33127[(13)]);
var inst_33065__$1 = (state_33127[(2)]);
var inst_33066 = (inst_33065__$1 == null);
var state_33127__$1 = (function (){var statearr_33133 = state_33127;
(statearr_33133[(13)] = inst_33065__$1);

return statearr_33133;
})();
if(cljs.core.truth_(inst_33066)){
var statearr_33134_34263 = state_33127__$1;
(statearr_33134_34263[(1)] = (5));

} else {
var statearr_33135_34264 = state_33127__$1;
(statearr_33135_34264[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33128 === (15))){
var state_33127__$1 = state_33127;
var statearr_33139_34269 = state_33127__$1;
(statearr_33139_34269[(2)] = null);

(statearr_33139_34269[(1)] = (16));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33128 === (21))){
var state_33127__$1 = state_33127;
var statearr_33140_34270 = state_33127__$1;
(statearr_33140_34270[(2)] = null);

(statearr_33140_34270[(1)] = (23));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33128 === (13))){
var inst_33076 = (state_33127[(9)]);
var inst_33078 = (state_33127[(10)]);
var inst_33077 = (state_33127[(11)]);
var inst_33079 = (state_33127[(12)]);
var inst_33087 = (state_33127[(2)]);
var inst_33088 = (inst_33079 + (1));
var tmp33136 = inst_33076;
var tmp33137 = inst_33078;
var tmp33138 = inst_33077;
var inst_33076__$1 = tmp33136;
var inst_33077__$1 = tmp33138;
var inst_33078__$1 = tmp33137;
var inst_33079__$1 = inst_33088;
var state_33127__$1 = (function (){var statearr_33142 = state_33127;
(statearr_33142[(14)] = inst_33087);

(statearr_33142[(9)] = inst_33076__$1);

(statearr_33142[(10)] = inst_33078__$1);

(statearr_33142[(11)] = inst_33077__$1);

(statearr_33142[(12)] = inst_33079__$1);

return statearr_33142;
})();
var statearr_33143_34287 = state_33127__$1;
(statearr_33143_34287[(2)] = null);

(statearr_33143_34287[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33128 === (22))){
var state_33127__$1 = state_33127;
var statearr_33146_34289 = state_33127__$1;
(statearr_33146_34289[(2)] = null);

(statearr_33146_34289[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33128 === (6))){
var inst_33065 = (state_33127[(13)]);
var inst_33074 = (f.cljs$core$IFn$_invoke$arity$1 ? f.cljs$core$IFn$_invoke$arity$1(inst_33065) : f.call(null,inst_33065));
var inst_33075 = cljs.core.seq(inst_33074);
var inst_33076 = inst_33075;
var inst_33077 = null;
var inst_33078 = (0);
var inst_33079 = (0);
var state_33127__$1 = (function (){var statearr_33147 = state_33127;
(statearr_33147[(9)] = inst_33076);

(statearr_33147[(10)] = inst_33078);

(statearr_33147[(11)] = inst_33077);

(statearr_33147[(12)] = inst_33079);

return statearr_33147;
})();
var statearr_33154_34291 = state_33127__$1;
(statearr_33154_34291[(2)] = null);

(statearr_33154_34291[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33128 === (17))){
var inst_33091 = (state_33127[(7)]);
var inst_33095 = cljs.core.chunk_first(inst_33091);
var inst_33097 = cljs.core.chunk_rest(inst_33091);
var inst_33098 = cljs.core.count(inst_33095);
var inst_33076 = inst_33097;
var inst_33077 = inst_33095;
var inst_33078 = inst_33098;
var inst_33079 = (0);
var state_33127__$1 = (function (){var statearr_33156 = state_33127;
(statearr_33156[(9)] = inst_33076);

(statearr_33156[(10)] = inst_33078);

(statearr_33156[(11)] = inst_33077);

(statearr_33156[(12)] = inst_33079);

return statearr_33156;
})();
var statearr_33157_34293 = state_33127__$1;
(statearr_33157_34293[(2)] = null);

(statearr_33157_34293[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33128 === (3))){
var inst_33125 = (state_33127[(2)]);
var state_33127__$1 = state_33127;
return cljs.core.async.impl.ioc_helpers.return_chan(state_33127__$1,inst_33125);
} else {
if((state_val_33128 === (12))){
var inst_33113 = (state_33127[(2)]);
var state_33127__$1 = state_33127;
var statearr_33158_34294 = state_33127__$1;
(statearr_33158_34294[(2)] = inst_33113);

(statearr_33158_34294[(1)] = (9));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33128 === (2))){
var state_33127__$1 = state_33127;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_33127__$1,(4),in$);
} else {
if((state_val_33128 === (23))){
var inst_33121 = (state_33127[(2)]);
var state_33127__$1 = state_33127;
var statearr_33159_34295 = state_33127__$1;
(statearr_33159_34295[(2)] = inst_33121);

(statearr_33159_34295[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33128 === (19))){
var inst_33108 = (state_33127[(2)]);
var state_33127__$1 = state_33127;
var statearr_33160_34296 = state_33127__$1;
(statearr_33160_34296[(2)] = inst_33108);

(statearr_33160_34296[(1)] = (16));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33128 === (11))){
var inst_33076 = (state_33127[(9)]);
var inst_33091 = (state_33127[(7)]);
var inst_33091__$1 = cljs.core.seq(inst_33076);
var state_33127__$1 = (function (){var statearr_33163 = state_33127;
(statearr_33163[(7)] = inst_33091__$1);

return statearr_33163;
})();
if(inst_33091__$1){
var statearr_33165_34297 = state_33127__$1;
(statearr_33165_34297[(1)] = (14));

} else {
var statearr_33166_34298 = state_33127__$1;
(statearr_33166_34298[(1)] = (15));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33128 === (9))){
var inst_33115 = (state_33127[(2)]);
var inst_33116 = cljs.core.async.impl.protocols.closed_QMARK_(out);
var state_33127__$1 = (function (){var statearr_33167 = state_33127;
(statearr_33167[(15)] = inst_33115);

return statearr_33167;
})();
if(cljs.core.truth_(inst_33116)){
var statearr_33168_34299 = state_33127__$1;
(statearr_33168_34299[(1)] = (21));

} else {
var statearr_33169_34300 = state_33127__$1;
(statearr_33169_34300[(1)] = (22));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33128 === (5))){
var inst_33068 = cljs.core.async.close_BANG_(out);
var state_33127__$1 = state_33127;
var statearr_33172_34305 = state_33127__$1;
(statearr_33172_34305[(2)] = inst_33068);

(statearr_33172_34305[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33128 === (14))){
var inst_33091 = (state_33127[(7)]);
var inst_33093 = cljs.core.chunked_seq_QMARK_(inst_33091);
var state_33127__$1 = state_33127;
if(inst_33093){
var statearr_33175_34312 = state_33127__$1;
(statearr_33175_34312[(1)] = (17));

} else {
var statearr_33176_34313 = state_33127__$1;
(statearr_33176_34313[(1)] = (18));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33128 === (16))){
var inst_33111 = (state_33127[(2)]);
var state_33127__$1 = state_33127;
var statearr_33178_34324 = state_33127__$1;
(statearr_33178_34324[(2)] = inst_33111);

(statearr_33178_34324[(1)] = (12));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33128 === (10))){
var inst_33077 = (state_33127[(11)]);
var inst_33079 = (state_33127[(12)]);
var inst_33085 = cljs.core._nth(inst_33077,inst_33079);
var state_33127__$1 = state_33127;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_33127__$1,(13),out,inst_33085);
} else {
if((state_val_33128 === (18))){
var inst_33091 = (state_33127[(7)]);
var inst_33101 = cljs.core.first(inst_33091);
var state_33127__$1 = state_33127;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_33127__$1,(20),out,inst_33101);
} else {
if((state_val_33128 === (8))){
var inst_33078 = (state_33127[(10)]);
var inst_33079 = (state_33127[(12)]);
var inst_33081 = (inst_33079 < inst_33078);
var inst_33082 = inst_33081;
var state_33127__$1 = state_33127;
if(cljs.core.truth_(inst_33082)){
var statearr_33179_34348 = state_33127__$1;
(statearr_33179_34348[(1)] = (10));

} else {
var statearr_33180_34349 = state_33127__$1;
(statearr_33180_34349[(1)] = (11));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$mapcat_STAR__$_state_machine__30838__auto__ = null;
var cljs$core$async$mapcat_STAR__$_state_machine__30838__auto____0 = (function (){
var statearr_33181 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_33181[(0)] = cljs$core$async$mapcat_STAR__$_state_machine__30838__auto__);

(statearr_33181[(1)] = (1));

return statearr_33181;
});
var cljs$core$async$mapcat_STAR__$_state_machine__30838__auto____1 = (function (state_33127){
while(true){
var ret_value__30839__auto__ = (function (){try{while(true){
var result__30840__auto__ = switch__30837__auto__(state_33127);
if(cljs.core.keyword_identical_QMARK_(result__30840__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__30840__auto__;
}
break;
}
}catch (e33182){var ex__30841__auto__ = e33182;
var statearr_33183_34350 = state_33127;
(statearr_33183_34350[(2)] = ex__30841__auto__);


if(cljs.core.seq((state_33127[(4)]))){
var statearr_33184_34351 = state_33127;
(statearr_33184_34351[(1)] = cljs.core.first((state_33127[(4)])));

} else {
throw ex__30841__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__30839__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__34352 = state_33127;
state_33127 = G__34352;
continue;
} else {
return ret_value__30839__auto__;
}
break;
}
});
cljs$core$async$mapcat_STAR__$_state_machine__30838__auto__ = function(state_33127){
switch(arguments.length){
case 0:
return cljs$core$async$mapcat_STAR__$_state_machine__30838__auto____0.call(this);
case 1:
return cljs$core$async$mapcat_STAR__$_state_machine__30838__auto____1.call(this,state_33127);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$mapcat_STAR__$_state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$mapcat_STAR__$_state_machine__30838__auto____0;
cljs$core$async$mapcat_STAR__$_state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$mapcat_STAR__$_state_machine__30838__auto____1;
return cljs$core$async$mapcat_STAR__$_state_machine__30838__auto__;
})()
})();
var state__31117__auto__ = (function (){var statearr_33185 = f__31116__auto__();
(statearr_33185[(6)] = c__31115__auto__);

return statearr_33185;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__31117__auto__);
}));

return c__31115__auto__;
});
/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.mapcat_LT_ = (function cljs$core$async$mapcat_LT_(var_args){
var G__33187 = arguments.length;
switch (G__33187) {
case 2:
return cljs.core.async.mapcat_LT_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.mapcat_LT_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(cljs.core.async.mapcat_LT_.cljs$core$IFn$_invoke$arity$2 = (function (f,in$){
return cljs.core.async.mapcat_LT_.cljs$core$IFn$_invoke$arity$3(f,in$,null);
}));

(cljs.core.async.mapcat_LT_.cljs$core$IFn$_invoke$arity$3 = (function (f,in$,buf_or_n){
var out = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(buf_or_n);
cljs.core.async.mapcat_STAR_(f,in$,out);

return out;
}));

(cljs.core.async.mapcat_LT_.cljs$lang$maxFixedArity = 3);

/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.mapcat_GT_ = (function cljs$core$async$mapcat_GT_(var_args){
var G__33189 = arguments.length;
switch (G__33189) {
case 2:
return cljs.core.async.mapcat_GT_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.mapcat_GT_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(cljs.core.async.mapcat_GT_.cljs$core$IFn$_invoke$arity$2 = (function (f,out){
return cljs.core.async.mapcat_GT_.cljs$core$IFn$_invoke$arity$3(f,out,null);
}));

(cljs.core.async.mapcat_GT_.cljs$core$IFn$_invoke$arity$3 = (function (f,out,buf_or_n){
var in$ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(buf_or_n);
cljs.core.async.mapcat_STAR_(f,in$,out);

return in$;
}));

(cljs.core.async.mapcat_GT_.cljs$lang$maxFixedArity = 3);

/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.unique = (function cljs$core$async$unique(var_args){
var G__33194 = arguments.length;
switch (G__33194) {
case 1:
return cljs.core.async.unique.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return cljs.core.async.unique.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(cljs.core.async.unique.cljs$core$IFn$_invoke$arity$1 = (function (ch){
return cljs.core.async.unique.cljs$core$IFn$_invoke$arity$2(ch,null);
}));

(cljs.core.async.unique.cljs$core$IFn$_invoke$arity$2 = (function (ch,buf_or_n){
var out = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(buf_or_n);
var c__31115__auto___34384 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__31116__auto__ = (function (){var switch__30837__auto__ = (function (state_33219){
var state_val_33220 = (state_33219[(1)]);
if((state_val_33220 === (7))){
var inst_33214 = (state_33219[(2)]);
var state_33219__$1 = state_33219;
var statearr_33221_34394 = state_33219__$1;
(statearr_33221_34394[(2)] = inst_33214);

(statearr_33221_34394[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33220 === (1))){
var inst_33196 = null;
var state_33219__$1 = (function (){var statearr_33222 = state_33219;
(statearr_33222[(7)] = inst_33196);

return statearr_33222;
})();
var statearr_33223_34399 = state_33219__$1;
(statearr_33223_34399[(2)] = null);

(statearr_33223_34399[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33220 === (4))){
var inst_33199 = (state_33219[(8)]);
var inst_33199__$1 = (state_33219[(2)]);
var inst_33200 = (inst_33199__$1 == null);
var inst_33201 = cljs.core.not(inst_33200);
var state_33219__$1 = (function (){var statearr_33224 = state_33219;
(statearr_33224[(8)] = inst_33199__$1);

return statearr_33224;
})();
if(inst_33201){
var statearr_33225_34402 = state_33219__$1;
(statearr_33225_34402[(1)] = (5));

} else {
var statearr_33226_34403 = state_33219__$1;
(statearr_33226_34403[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33220 === (6))){
var state_33219__$1 = state_33219;
var statearr_33227_34404 = state_33219__$1;
(statearr_33227_34404[(2)] = null);

(statearr_33227_34404[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33220 === (3))){
var inst_33216 = (state_33219[(2)]);
var inst_33217 = cljs.core.async.close_BANG_(out);
var state_33219__$1 = (function (){var statearr_33228 = state_33219;
(statearr_33228[(9)] = inst_33216);

return statearr_33228;
})();
return cljs.core.async.impl.ioc_helpers.return_chan(state_33219__$1,inst_33217);
} else {
if((state_val_33220 === (2))){
var state_33219__$1 = state_33219;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_33219__$1,(4),ch);
} else {
if((state_val_33220 === (11))){
var inst_33199 = (state_33219[(8)]);
var inst_33208 = (state_33219[(2)]);
var inst_33196 = inst_33199;
var state_33219__$1 = (function (){var statearr_33229 = state_33219;
(statearr_33229[(7)] = inst_33196);

(statearr_33229[(10)] = inst_33208);

return statearr_33229;
})();
var statearr_33230_34405 = state_33219__$1;
(statearr_33230_34405[(2)] = null);

(statearr_33230_34405[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33220 === (9))){
var inst_33199 = (state_33219[(8)]);
var state_33219__$1 = state_33219;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_33219__$1,(11),out,inst_33199);
} else {
if((state_val_33220 === (5))){
var inst_33196 = (state_33219[(7)]);
var inst_33199 = (state_33219[(8)]);
var inst_33203 = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(inst_33199,inst_33196);
var state_33219__$1 = state_33219;
if(inst_33203){
var statearr_33232_34411 = state_33219__$1;
(statearr_33232_34411[(1)] = (8));

} else {
var statearr_33233_34412 = state_33219__$1;
(statearr_33233_34412[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33220 === (10))){
var inst_33211 = (state_33219[(2)]);
var state_33219__$1 = state_33219;
var statearr_33234_34417 = state_33219__$1;
(statearr_33234_34417[(2)] = inst_33211);

(statearr_33234_34417[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33220 === (8))){
var inst_33196 = (state_33219[(7)]);
var tmp33231 = inst_33196;
var inst_33196__$1 = tmp33231;
var state_33219__$1 = (function (){var statearr_33235 = state_33219;
(statearr_33235[(7)] = inst_33196__$1);

return statearr_33235;
})();
var statearr_33236_34422 = state_33219__$1;
(statearr_33236_34422[(2)] = null);

(statearr_33236_34422[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$state_machine__30838__auto__ = null;
var cljs$core$async$state_machine__30838__auto____0 = (function (){
var statearr_33237 = [null,null,null,null,null,null,null,null,null,null,null];
(statearr_33237[(0)] = cljs$core$async$state_machine__30838__auto__);

(statearr_33237[(1)] = (1));

return statearr_33237;
});
var cljs$core$async$state_machine__30838__auto____1 = (function (state_33219){
while(true){
var ret_value__30839__auto__ = (function (){try{while(true){
var result__30840__auto__ = switch__30837__auto__(state_33219);
if(cljs.core.keyword_identical_QMARK_(result__30840__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__30840__auto__;
}
break;
}
}catch (e33238){var ex__30841__auto__ = e33238;
var statearr_33239_34424 = state_33219;
(statearr_33239_34424[(2)] = ex__30841__auto__);


if(cljs.core.seq((state_33219[(4)]))){
var statearr_33240_34425 = state_33219;
(statearr_33240_34425[(1)] = cljs.core.first((state_33219[(4)])));

} else {
throw ex__30841__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__30839__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__34426 = state_33219;
state_33219 = G__34426;
continue;
} else {
return ret_value__30839__auto__;
}
break;
}
});
cljs$core$async$state_machine__30838__auto__ = function(state_33219){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__30838__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__30838__auto____1.call(this,state_33219);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__30838__auto____0;
cljs$core$async$state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__30838__auto____1;
return cljs$core$async$state_machine__30838__auto__;
})()
})();
var state__31117__auto__ = (function (){var statearr_33241 = f__31116__auto__();
(statearr_33241[(6)] = c__31115__auto___34384);

return statearr_33241;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__31117__auto__);
}));


return out;
}));

(cljs.core.async.unique.cljs$lang$maxFixedArity = 2);

/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.partition = (function cljs$core$async$partition(var_args){
var G__33243 = arguments.length;
switch (G__33243) {
case 2:
return cljs.core.async.partition.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.partition.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(cljs.core.async.partition.cljs$core$IFn$_invoke$arity$2 = (function (n,ch){
return cljs.core.async.partition.cljs$core$IFn$_invoke$arity$3(n,ch,null);
}));

(cljs.core.async.partition.cljs$core$IFn$_invoke$arity$3 = (function (n,ch,buf_or_n){
var out = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(buf_or_n);
var c__31115__auto___34431 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__31116__auto__ = (function (){var switch__30837__auto__ = (function (state_33281){
var state_val_33282 = (state_33281[(1)]);
if((state_val_33282 === (7))){
var inst_33277 = (state_33281[(2)]);
var state_33281__$1 = state_33281;
var statearr_33283_34432 = state_33281__$1;
(statearr_33283_34432[(2)] = inst_33277);

(statearr_33283_34432[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33282 === (1))){
var inst_33244 = (new Array(n));
var inst_33245 = inst_33244;
var inst_33246 = (0);
var state_33281__$1 = (function (){var statearr_33284 = state_33281;
(statearr_33284[(7)] = inst_33246);

(statearr_33284[(8)] = inst_33245);

return statearr_33284;
})();
var statearr_33285_34433 = state_33281__$1;
(statearr_33285_34433[(2)] = null);

(statearr_33285_34433[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33282 === (4))){
var inst_33249 = (state_33281[(9)]);
var inst_33249__$1 = (state_33281[(2)]);
var inst_33250 = (inst_33249__$1 == null);
var inst_33251 = cljs.core.not(inst_33250);
var state_33281__$1 = (function (){var statearr_33286 = state_33281;
(statearr_33286[(9)] = inst_33249__$1);

return statearr_33286;
})();
if(inst_33251){
var statearr_33287_34435 = state_33281__$1;
(statearr_33287_34435[(1)] = (5));

} else {
var statearr_33288_34436 = state_33281__$1;
(statearr_33288_34436[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33282 === (15))){
var inst_33271 = (state_33281[(2)]);
var state_33281__$1 = state_33281;
var statearr_33289_34438 = state_33281__$1;
(statearr_33289_34438[(2)] = inst_33271);

(statearr_33289_34438[(1)] = (14));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33282 === (13))){
var state_33281__$1 = state_33281;
var statearr_33290_34440 = state_33281__$1;
(statearr_33290_34440[(2)] = null);

(statearr_33290_34440[(1)] = (14));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33282 === (6))){
var inst_33246 = (state_33281[(7)]);
var inst_33267 = (inst_33246 > (0));
var state_33281__$1 = state_33281;
if(cljs.core.truth_(inst_33267)){
var statearr_33291_34441 = state_33281__$1;
(statearr_33291_34441[(1)] = (12));

} else {
var statearr_33292_34442 = state_33281__$1;
(statearr_33292_34442[(1)] = (13));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33282 === (3))){
var inst_33279 = (state_33281[(2)]);
var state_33281__$1 = state_33281;
return cljs.core.async.impl.ioc_helpers.return_chan(state_33281__$1,inst_33279);
} else {
if((state_val_33282 === (12))){
var inst_33245 = (state_33281[(8)]);
var inst_33269 = cljs.core.vec(inst_33245);
var state_33281__$1 = state_33281;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_33281__$1,(15),out,inst_33269);
} else {
if((state_val_33282 === (2))){
var state_33281__$1 = state_33281;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_33281__$1,(4),ch);
} else {
if((state_val_33282 === (11))){
var inst_33261 = (state_33281[(2)]);
var inst_33262 = (new Array(n));
var inst_33245 = inst_33262;
var inst_33246 = (0);
var state_33281__$1 = (function (){var statearr_33293 = state_33281;
(statearr_33293[(7)] = inst_33246);

(statearr_33293[(10)] = inst_33261);

(statearr_33293[(8)] = inst_33245);

return statearr_33293;
})();
var statearr_33294_34443 = state_33281__$1;
(statearr_33294_34443[(2)] = null);

(statearr_33294_34443[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33282 === (9))){
var inst_33245 = (state_33281[(8)]);
var inst_33259 = cljs.core.vec(inst_33245);
var state_33281__$1 = state_33281;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_33281__$1,(11),out,inst_33259);
} else {
if((state_val_33282 === (5))){
var inst_33249 = (state_33281[(9)]);
var inst_33246 = (state_33281[(7)]);
var inst_33254 = (state_33281[(11)]);
var inst_33245 = (state_33281[(8)]);
var inst_33253 = (inst_33245[inst_33246] = inst_33249);
var inst_33254__$1 = (inst_33246 + (1));
var inst_33255 = (inst_33254__$1 < n);
var state_33281__$1 = (function (){var statearr_33295 = state_33281;
(statearr_33295[(11)] = inst_33254__$1);

(statearr_33295[(12)] = inst_33253);

return statearr_33295;
})();
if(cljs.core.truth_(inst_33255)){
var statearr_33296_34444 = state_33281__$1;
(statearr_33296_34444[(1)] = (8));

} else {
var statearr_33297_34445 = state_33281__$1;
(statearr_33297_34445[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33282 === (14))){
var inst_33274 = (state_33281[(2)]);
var inst_33275 = cljs.core.async.close_BANG_(out);
var state_33281__$1 = (function (){var statearr_33299 = state_33281;
(statearr_33299[(13)] = inst_33274);

return statearr_33299;
})();
var statearr_33300_34446 = state_33281__$1;
(statearr_33300_34446[(2)] = inst_33275);

(statearr_33300_34446[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33282 === (10))){
var inst_33265 = (state_33281[(2)]);
var state_33281__$1 = state_33281;
var statearr_33301_34447 = state_33281__$1;
(statearr_33301_34447[(2)] = inst_33265);

(statearr_33301_34447[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33282 === (8))){
var inst_33254 = (state_33281[(11)]);
var inst_33245 = (state_33281[(8)]);
var tmp33298 = inst_33245;
var inst_33245__$1 = tmp33298;
var inst_33246 = inst_33254;
var state_33281__$1 = (function (){var statearr_33302 = state_33281;
(statearr_33302[(7)] = inst_33246);

(statearr_33302[(8)] = inst_33245__$1);

return statearr_33302;
})();
var statearr_33303_34449 = state_33281__$1;
(statearr_33303_34449[(2)] = null);

(statearr_33303_34449[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$state_machine__30838__auto__ = null;
var cljs$core$async$state_machine__30838__auto____0 = (function (){
var statearr_33304 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_33304[(0)] = cljs$core$async$state_machine__30838__auto__);

(statearr_33304[(1)] = (1));

return statearr_33304;
});
var cljs$core$async$state_machine__30838__auto____1 = (function (state_33281){
while(true){
var ret_value__30839__auto__ = (function (){try{while(true){
var result__30840__auto__ = switch__30837__auto__(state_33281);
if(cljs.core.keyword_identical_QMARK_(result__30840__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__30840__auto__;
}
break;
}
}catch (e33305){var ex__30841__auto__ = e33305;
var statearr_33306_34507 = state_33281;
(statearr_33306_34507[(2)] = ex__30841__auto__);


if(cljs.core.seq((state_33281[(4)]))){
var statearr_33307_34508 = state_33281;
(statearr_33307_34508[(1)] = cljs.core.first((state_33281[(4)])));

} else {
throw ex__30841__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__30839__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__34509 = state_33281;
state_33281 = G__34509;
continue;
} else {
return ret_value__30839__auto__;
}
break;
}
});
cljs$core$async$state_machine__30838__auto__ = function(state_33281){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__30838__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__30838__auto____1.call(this,state_33281);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__30838__auto____0;
cljs$core$async$state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__30838__auto____1;
return cljs$core$async$state_machine__30838__auto__;
})()
})();
var state__31117__auto__ = (function (){var statearr_33308 = f__31116__auto__();
(statearr_33308[(6)] = c__31115__auto___34431);

return statearr_33308;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__31117__auto__);
}));


return out;
}));

(cljs.core.async.partition.cljs$lang$maxFixedArity = 3);

/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.partition_by = (function cljs$core$async$partition_by(var_args){
var G__33310 = arguments.length;
switch (G__33310) {
case 2:
return cljs.core.async.partition_by.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.partition_by.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(cljs.core.async.partition_by.cljs$core$IFn$_invoke$arity$2 = (function (f,ch){
return cljs.core.async.partition_by.cljs$core$IFn$_invoke$arity$3(f,ch,null);
}));

(cljs.core.async.partition_by.cljs$core$IFn$_invoke$arity$3 = (function (f,ch,buf_or_n){
var out = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(buf_or_n);
var c__31115__auto___34512 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__31116__auto__ = (function (){var switch__30837__auto__ = (function (state_33352){
var state_val_33353 = (state_33352[(1)]);
if((state_val_33353 === (7))){
var inst_33348 = (state_33352[(2)]);
var state_33352__$1 = state_33352;
var statearr_33359_34513 = state_33352__$1;
(statearr_33359_34513[(2)] = inst_33348);

(statearr_33359_34513[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33353 === (1))){
var inst_33311 = [];
var inst_33312 = inst_33311;
var inst_33313 = new cljs.core.Keyword("cljs.core.async","nothing","cljs.core.async/nothing",-69252123);
var state_33352__$1 = (function (){var statearr_33361 = state_33352;
(statearr_33361[(7)] = inst_33313);

(statearr_33361[(8)] = inst_33312);

return statearr_33361;
})();
var statearr_33362_34514 = state_33352__$1;
(statearr_33362_34514[(2)] = null);

(statearr_33362_34514[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33353 === (4))){
var inst_33316 = (state_33352[(9)]);
var inst_33316__$1 = (state_33352[(2)]);
var inst_33317 = (inst_33316__$1 == null);
var inst_33318 = cljs.core.not(inst_33317);
var state_33352__$1 = (function (){var statearr_33363 = state_33352;
(statearr_33363[(9)] = inst_33316__$1);

return statearr_33363;
})();
if(inst_33318){
var statearr_33368_34519 = state_33352__$1;
(statearr_33368_34519[(1)] = (5));

} else {
var statearr_33369_34520 = state_33352__$1;
(statearr_33369_34520[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33353 === (15))){
var inst_33342 = (state_33352[(2)]);
var state_33352__$1 = state_33352;
var statearr_33370_34525 = state_33352__$1;
(statearr_33370_34525[(2)] = inst_33342);

(statearr_33370_34525[(1)] = (14));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33353 === (13))){
var state_33352__$1 = state_33352;
var statearr_33371_34530 = state_33352__$1;
(statearr_33371_34530[(2)] = null);

(statearr_33371_34530[(1)] = (14));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33353 === (6))){
var inst_33312 = (state_33352[(8)]);
var inst_33337 = inst_33312.length;
var inst_33338 = (inst_33337 > (0));
var state_33352__$1 = state_33352;
if(cljs.core.truth_(inst_33338)){
var statearr_33372_34531 = state_33352__$1;
(statearr_33372_34531[(1)] = (12));

} else {
var statearr_33373_34532 = state_33352__$1;
(statearr_33373_34532[(1)] = (13));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33353 === (3))){
var inst_33350 = (state_33352[(2)]);
var state_33352__$1 = state_33352;
return cljs.core.async.impl.ioc_helpers.return_chan(state_33352__$1,inst_33350);
} else {
if((state_val_33353 === (12))){
var inst_33312 = (state_33352[(8)]);
var inst_33340 = cljs.core.vec(inst_33312);
var state_33352__$1 = state_33352;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_33352__$1,(15),out,inst_33340);
} else {
if((state_val_33353 === (2))){
var state_33352__$1 = state_33352;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_33352__$1,(4),ch);
} else {
if((state_val_33353 === (11))){
var inst_33320 = (state_33352[(10)]);
var inst_33316 = (state_33352[(9)]);
var inst_33330 = (state_33352[(2)]);
var inst_33331 = [];
var inst_33332 = inst_33331.push(inst_33316);
var inst_33312 = inst_33331;
var inst_33313 = inst_33320;
var state_33352__$1 = (function (){var statearr_33374 = state_33352;
(statearr_33374[(7)] = inst_33313);

(statearr_33374[(11)] = inst_33332);

(statearr_33374[(12)] = inst_33330);

(statearr_33374[(8)] = inst_33312);

return statearr_33374;
})();
var statearr_33375_34534 = state_33352__$1;
(statearr_33375_34534[(2)] = null);

(statearr_33375_34534[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33353 === (9))){
var inst_33312 = (state_33352[(8)]);
var inst_33328 = cljs.core.vec(inst_33312);
var state_33352__$1 = state_33352;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_33352__$1,(11),out,inst_33328);
} else {
if((state_val_33353 === (5))){
var inst_33313 = (state_33352[(7)]);
var inst_33320 = (state_33352[(10)]);
var inst_33316 = (state_33352[(9)]);
var inst_33320__$1 = (f.cljs$core$IFn$_invoke$arity$1 ? f.cljs$core$IFn$_invoke$arity$1(inst_33316) : f.call(null,inst_33316));
var inst_33321 = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(inst_33320__$1,inst_33313);
var inst_33322 = cljs.core.keyword_identical_QMARK_(inst_33313,new cljs.core.Keyword("cljs.core.async","nothing","cljs.core.async/nothing",-69252123));
var inst_33323 = ((inst_33321) || (inst_33322));
var state_33352__$1 = (function (){var statearr_33376 = state_33352;
(statearr_33376[(10)] = inst_33320__$1);

return statearr_33376;
})();
if(cljs.core.truth_(inst_33323)){
var statearr_33377_34539 = state_33352__$1;
(statearr_33377_34539[(1)] = (8));

} else {
var statearr_33378_34540 = state_33352__$1;
(statearr_33378_34540[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33353 === (14))){
var inst_33345 = (state_33352[(2)]);
var inst_33346 = cljs.core.async.close_BANG_(out);
var state_33352__$1 = (function (){var statearr_33380 = state_33352;
(statearr_33380[(13)] = inst_33345);

return statearr_33380;
})();
var statearr_33381_34541 = state_33352__$1;
(statearr_33381_34541[(2)] = inst_33346);

(statearr_33381_34541[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33353 === (10))){
var inst_33335 = (state_33352[(2)]);
var state_33352__$1 = state_33352;
var statearr_33382_34542 = state_33352__$1;
(statearr_33382_34542[(2)] = inst_33335);

(statearr_33382_34542[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33353 === (8))){
var inst_33312 = (state_33352[(8)]);
var inst_33320 = (state_33352[(10)]);
var inst_33316 = (state_33352[(9)]);
var inst_33325 = inst_33312.push(inst_33316);
var tmp33379 = inst_33312;
var inst_33312__$1 = tmp33379;
var inst_33313 = inst_33320;
var state_33352__$1 = (function (){var statearr_33383 = state_33352;
(statearr_33383[(7)] = inst_33313);

(statearr_33383[(14)] = inst_33325);

(statearr_33383[(8)] = inst_33312__$1);

return statearr_33383;
})();
var statearr_33384_34544 = state_33352__$1;
(statearr_33384_34544[(2)] = null);

(statearr_33384_34544[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$state_machine__30838__auto__ = null;
var cljs$core$async$state_machine__30838__auto____0 = (function (){
var statearr_33385 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_33385[(0)] = cljs$core$async$state_machine__30838__auto__);

(statearr_33385[(1)] = (1));

return statearr_33385;
});
var cljs$core$async$state_machine__30838__auto____1 = (function (state_33352){
while(true){
var ret_value__30839__auto__ = (function (){try{while(true){
var result__30840__auto__ = switch__30837__auto__(state_33352);
if(cljs.core.keyword_identical_QMARK_(result__30840__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__30840__auto__;
}
break;
}
}catch (e33386){var ex__30841__auto__ = e33386;
var statearr_33387_34547 = state_33352;
(statearr_33387_34547[(2)] = ex__30841__auto__);


if(cljs.core.seq((state_33352[(4)]))){
var statearr_33388_34548 = state_33352;
(statearr_33388_34548[(1)] = cljs.core.first((state_33352[(4)])));

} else {
throw ex__30841__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__30839__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__34549 = state_33352;
state_33352 = G__34549;
continue;
} else {
return ret_value__30839__auto__;
}
break;
}
});
cljs$core$async$state_machine__30838__auto__ = function(state_33352){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__30838__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__30838__auto____1.call(this,state_33352);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__30838__auto____0;
cljs$core$async$state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__30838__auto____1;
return cljs$core$async$state_machine__30838__auto__;
})()
})();
var state__31117__auto__ = (function (){var statearr_33389 = f__31116__auto__();
(statearr_33389[(6)] = c__31115__auto___34512);

return statearr_33389;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__31117__auto__);
}));


return out;
}));

(cljs.core.async.partition_by.cljs$lang$maxFixedArity = 3);


//# sourceMappingURL=cljs.core.async.js.map
