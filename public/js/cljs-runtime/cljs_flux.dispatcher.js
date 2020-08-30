goog.provide('cljs_flux.dispatcher');
cljs_flux.dispatcher.prefix = "ID_";

/**
 * @interface
 */
cljs_flux.dispatcher.IDispatcher = function(){};

var cljs_flux$dispatcher$IDispatcher$register$dyn_41252 = (function (this$,callback){
var x__4428__auto__ = (((this$ == null))?null:this$);
var m__4429__auto__ = (cljs_flux.dispatcher.register[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$2(this$,callback) : m__4429__auto__.call(null,this$,callback));
} else {
var m__4426__auto__ = (cljs_flux.dispatcher.register["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$2(this$,callback) : m__4426__auto__.call(null,this$,callback));
} else {
throw cljs.core.missing_protocol("IDispatcher.register",this$);
}
}
});
/**
 * Registers a callback to be invoked with every dispatched payload. Returns
 *  a token that can be used with `wait-for`.
 */
cljs_flux.dispatcher.register = (function cljs_flux$dispatcher$register(this$,callback){
if((((!((this$ == null)))) && ((!((this$.cljs_flux$dispatcher$IDispatcher$register$arity$2 == null)))))){
return this$.cljs_flux$dispatcher$IDispatcher$register$arity$2(this$,callback);
} else {
return cljs_flux$dispatcher$IDispatcher$register$dyn_41252(this$,callback);
}
});

var cljs_flux$dispatcher$IDispatcher$unregister$dyn_41257 = (function (this$,id){
var x__4428__auto__ = (((this$ == null))?null:this$);
var m__4429__auto__ = (cljs_flux.dispatcher.unregister[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$2(this$,id) : m__4429__auto__.call(null,this$,id));
} else {
var m__4426__auto__ = (cljs_flux.dispatcher.unregister["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$2(this$,id) : m__4426__auto__.call(null,this$,id));
} else {
throw cljs.core.missing_protocol("IDispatcher.unregister",this$);
}
}
});
/**
 * Removes a callback based on its token.
 */
cljs_flux.dispatcher.unregister = (function cljs_flux$dispatcher$unregister(this$,id){
if((((!((this$ == null)))) && ((!((this$.cljs_flux$dispatcher$IDispatcher$unregister$arity$2 == null)))))){
return this$.cljs_flux$dispatcher$IDispatcher$unregister$arity$2(this$,id);
} else {
return cljs_flux$dispatcher$IDispatcher$unregister$dyn_41257(this$,id);
}
});

var cljs_flux$dispatcher$IDispatcher$wait_for$dyn_41258 = (function (this$,ids){
var x__4428__auto__ = (((this$ == null))?null:this$);
var m__4429__auto__ = (cljs_flux.dispatcher.wait_for[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$2(this$,ids) : m__4429__auto__.call(null,this$,ids));
} else {
var m__4426__auto__ = (cljs_flux.dispatcher.wait_for["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$2(this$,ids) : m__4426__auto__.call(null,this$,ids));
} else {
throw cljs.core.missing_protocol("IDispatcher.wait-for",this$);
}
}
});
/**
 * Waits for the callbacks specified to be invoked before continuing execution.
 */
cljs_flux.dispatcher.wait_for = (function cljs_flux$dispatcher$wait_for(this$,ids){
if((((!((this$ == null)))) && ((!((this$.cljs_flux$dispatcher$IDispatcher$wait_for$arity$2 == null)))))){
return this$.cljs_flux$dispatcher$IDispatcher$wait_for$arity$2(this$,ids);
} else {
return cljs_flux$dispatcher$IDispatcher$wait_for$dyn_41258(this$,ids);
}
});

var cljs_flux$dispatcher$IDispatcher$dispatch$dyn_41262 = (function (this$,payload){
var x__4428__auto__ = (((this$ == null))?null:this$);
var m__4429__auto__ = (cljs_flux.dispatcher.dispatch[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$2(this$,payload) : m__4429__auto__.call(null,this$,payload));
} else {
var m__4426__auto__ = (cljs_flux.dispatcher.dispatch["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$2(this$,payload) : m__4426__auto__.call(null,this$,payload));
} else {
throw cljs.core.missing_protocol("IDispatcher.dispatch",this$);
}
}
});
/**
 * Dispatches a payload to all registered callbacks
 */
cljs_flux.dispatcher.dispatch = (function cljs_flux$dispatcher$dispatch(this$,payload){
if((((!((this$ == null)))) && ((!((this$.cljs_flux$dispatcher$IDispatcher$dispatch$arity$2 == null)))))){
return this$.cljs_flux$dispatcher$IDispatcher$dispatch$arity$2(this$,payload);
} else {
return cljs_flux$dispatcher$IDispatcher$dispatch$dyn_41262(this$,payload);
}
});

var cljs_flux$dispatcher$IDispatcher$_invoke_callback$dyn_41265 = (function (this$,id){
var x__4428__auto__ = (((this$ == null))?null:this$);
var m__4429__auto__ = (cljs_flux.dispatcher._invoke_callback[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$2(this$,id) : m__4429__auto__.call(null,this$,id));
} else {
var m__4426__auto__ = (cljs_flux.dispatcher._invoke_callback["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$2(this$,id) : m__4426__auto__.call(null,this$,id));
} else {
throw cljs.core.missing_protocol("IDispatcher.-invoke-callback",this$);
}
}
});
/**
 * Invoke the registered callback.
 */
cljs_flux.dispatcher._invoke_callback = (function cljs_flux$dispatcher$_invoke_callback(this$,id){
if((((!((this$ == null)))) && ((!((this$.cljs_flux$dispatcher$IDispatcher$_invoke_callback$arity$2 == null)))))){
return this$.cljs_flux$dispatcher$IDispatcher$_invoke_callback$arity$2(this$,id);
} else {
return cljs_flux$dispatcher$IDispatcher$_invoke_callback$dyn_41265(this$,id);
}
});


/**
* @constructor
 * @implements {cljs.core.IRecord}
 * @implements {cljs.core.IKVReduce}
 * @implements {cljs.core.IEquiv}
 * @implements {cljs.core.IHash}
 * @implements {cljs.core.ICollection}
 * @implements {cljs_flux.dispatcher.IDispatcher}
 * @implements {cljs.core.ICounted}
 * @implements {cljs.core.ISeqable}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.ICloneable}
 * @implements {cljs.core.IPrintWithWriter}
 * @implements {cljs.core.IIterable}
 * @implements {cljs.core.IWithMeta}
 * @implements {cljs.core.IAssociative}
 * @implements {cljs.core.IMap}
 * @implements {cljs.core.ILookup}
*/
cljs_flux.dispatcher.Dispatcher = (function (registered,dispatching_QMARK_,pending_payload,token,__meta,__extmap,__hash){
this.registered = registered;
this.dispatching_QMARK_ = dispatching_QMARK_;
this.pending_payload = pending_payload;
this.token = token;
this.__meta = __meta;
this.__extmap = __extmap;
this.__hash = __hash;
this.cljs$lang$protocol_mask$partition0$ = 2230716170;
this.cljs$lang$protocol_mask$partition1$ = 139264;
});
(cljs_flux.dispatcher.Dispatcher.prototype.cljs$core$ILookup$_lookup$arity$2 = (function (this__4380__auto__,k__4381__auto__){
var self__ = this;
var this__4380__auto____$1 = this;
return this__4380__auto____$1.cljs$core$ILookup$_lookup$arity$3(null,k__4381__auto__,null);
}));

(cljs_flux.dispatcher.Dispatcher.prototype.cljs$core$ILookup$_lookup$arity$3 = (function (this__4382__auto__,k41103,else__4383__auto__){
var self__ = this;
var this__4382__auto____$1 = this;
var G__41112 = k41103;
var G__41112__$1 = (((G__41112 instanceof cljs.core.Keyword))?G__41112.fqn:null);
switch (G__41112__$1) {
case "registered":
return self__.registered;

break;
case "dispatching?":
return self__.dispatching_QMARK_;

break;
case "pending-payload":
return self__.pending_payload;

break;
case "token":
return self__.token;

break;
default:
return cljs.core.get.cljs$core$IFn$_invoke$arity$3(self__.__extmap,k41103,else__4383__auto__);

}
}));

(cljs_flux.dispatcher.Dispatcher.prototype.cljs$core$IKVReduce$_kv_reduce$arity$3 = (function (this__4399__auto__,f__4400__auto__,init__4401__auto__){
var self__ = this;
var this__4399__auto____$1 = this;
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (ret__4402__auto__,p__41115){
var vec__41116 = p__41115;
var k__4403__auto__ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41116,(0),null);
var v__4404__auto__ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41116,(1),null);
return (f__4400__auto__.cljs$core$IFn$_invoke$arity$3 ? f__4400__auto__.cljs$core$IFn$_invoke$arity$3(ret__4402__auto__,k__4403__auto__,v__4404__auto__) : f__4400__auto__.call(null,ret__4402__auto__,k__4403__auto__,v__4404__auto__));
}),init__4401__auto__,this__4399__auto____$1);
}));

(cljs_flux.dispatcher.Dispatcher.prototype.cljs$core$IPrintWithWriter$_pr_writer$arity$3 = (function (this__4394__auto__,writer__4395__auto__,opts__4396__auto__){
var self__ = this;
var this__4394__auto____$1 = this;
var pr_pair__4397__auto__ = (function (keyval__4398__auto__){
return cljs.core.pr_sequential_writer(writer__4395__auto__,cljs.core.pr_writer,""," ","",opts__4396__auto__,keyval__4398__auto__);
});
return cljs.core.pr_sequential_writer(writer__4395__auto__,pr_pair__4397__auto__,"#cljs-flux.dispatcher.Dispatcher{",", ","}",opts__4396__auto__,cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [(new cljs.core.PersistentVector(null,2,(5),cljs.core.PersistentVector.EMPTY_NODE,[new cljs.core.Keyword(null,"registered","registered",-388600037),self__.registered],null)),(new cljs.core.PersistentVector(null,2,(5),cljs.core.PersistentVector.EMPTY_NODE,[new cljs.core.Keyword(null,"dispatching?","dispatching?",443224906),self__.dispatching_QMARK_],null)),(new cljs.core.PersistentVector(null,2,(5),cljs.core.PersistentVector.EMPTY_NODE,[new cljs.core.Keyword(null,"pending-payload","pending-payload",-33788942),self__.pending_payload],null)),(new cljs.core.PersistentVector(null,2,(5),cljs.core.PersistentVector.EMPTY_NODE,[new cljs.core.Keyword(null,"token","token",-1211463215),self__.token],null))], null),self__.__extmap));
}));

(cljs_flux.dispatcher.Dispatcher.prototype.cljs$core$IIterable$_iterator$arity$1 = (function (G__41102){
var self__ = this;
var G__41102__$1 = this;
return (new cljs.core.RecordIter((0),G__41102__$1,4,new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"registered","registered",-388600037),new cljs.core.Keyword(null,"dispatching?","dispatching?",443224906),new cljs.core.Keyword(null,"pending-payload","pending-payload",-33788942),new cljs.core.Keyword(null,"token","token",-1211463215)], null),(cljs.core.truth_(self__.__extmap)?cljs.core._iterator(self__.__extmap):cljs.core.nil_iter())));
}));

(cljs_flux.dispatcher.Dispatcher.prototype.cljs$core$IMeta$_meta$arity$1 = (function (this__4378__auto__){
var self__ = this;
var this__4378__auto____$1 = this;
return self__.__meta;
}));

(cljs_flux.dispatcher.Dispatcher.prototype.cljs$core$ICloneable$_clone$arity$1 = (function (this__4375__auto__){
var self__ = this;
var this__4375__auto____$1 = this;
return (new cljs_flux.dispatcher.Dispatcher(self__.registered,self__.dispatching_QMARK_,self__.pending_payload,self__.token,self__.__meta,self__.__extmap,self__.__hash));
}));

(cljs_flux.dispatcher.Dispatcher.prototype.cljs$core$ICounted$_count$arity$1 = (function (this__4384__auto__){
var self__ = this;
var this__4384__auto____$1 = this;
return (4 + cljs.core.count(self__.__extmap));
}));

(cljs_flux.dispatcher.Dispatcher.prototype.cljs$core$IHash$_hash$arity$1 = (function (this__4376__auto__){
var self__ = this;
var this__4376__auto____$1 = this;
var h__4238__auto__ = self__.__hash;
if((!((h__4238__auto__ == null)))){
return h__4238__auto__;
} else {
var h__4238__auto____$1 = (function (coll__4377__auto__){
return (1579914422 ^ cljs.core.hash_unordered_coll(coll__4377__auto__));
})(this__4376__auto____$1);
(self__.__hash = h__4238__auto____$1);

return h__4238__auto____$1;
}
}));

(cljs_flux.dispatcher.Dispatcher.prototype.cljs$core$IEquiv$_equiv$arity$2 = (function (this41106,other41107){
var self__ = this;
var this41106__$1 = this;
return (((!((other41107 == null)))) && ((this41106__$1.constructor === other41107.constructor)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this41106__$1.registered,other41107.registered)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this41106__$1.dispatching_QMARK_,other41107.dispatching_QMARK_)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this41106__$1.pending_payload,other41107.pending_payload)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this41106__$1.token,other41107.token)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this41106__$1.__extmap,other41107.__extmap)));
}));

(cljs_flux.dispatcher.Dispatcher.prototype.cljs$core$IMap$_dissoc$arity$2 = (function (this__4389__auto__,k__4390__auto__){
var self__ = this;
var this__4389__auto____$1 = this;
if(cljs.core.contains_QMARK_(new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"dispatching?","dispatching?",443224906),null,new cljs.core.Keyword(null,"token","token",-1211463215),null,new cljs.core.Keyword(null,"pending-payload","pending-payload",-33788942),null,new cljs.core.Keyword(null,"registered","registered",-388600037),null], null), null),k__4390__auto__)){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core._with_meta(cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,this__4389__auto____$1),self__.__meta),k__4390__auto__);
} else {
return (new cljs_flux.dispatcher.Dispatcher(self__.registered,self__.dispatching_QMARK_,self__.pending_payload,self__.token,self__.__meta,cljs.core.not_empty(cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(self__.__extmap,k__4390__auto__)),null));
}
}));

(cljs_flux.dispatcher.Dispatcher.prototype.cljs$core$IAssociative$_assoc$arity$3 = (function (this__4387__auto__,k__4388__auto__,G__41102){
var self__ = this;
var this__4387__auto____$1 = this;
var pred__41131 = cljs.core.keyword_identical_QMARK_;
var expr__41132 = k__4388__auto__;
if(cljs.core.truth_((pred__41131.cljs$core$IFn$_invoke$arity$2 ? pred__41131.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"registered","registered",-388600037),expr__41132) : pred__41131.call(null,new cljs.core.Keyword(null,"registered","registered",-388600037),expr__41132)))){
return (new cljs_flux.dispatcher.Dispatcher(G__41102,self__.dispatching_QMARK_,self__.pending_payload,self__.token,self__.__meta,self__.__extmap,null));
} else {
if(cljs.core.truth_((pred__41131.cljs$core$IFn$_invoke$arity$2 ? pred__41131.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"dispatching?","dispatching?",443224906),expr__41132) : pred__41131.call(null,new cljs.core.Keyword(null,"dispatching?","dispatching?",443224906),expr__41132)))){
return (new cljs_flux.dispatcher.Dispatcher(self__.registered,G__41102,self__.pending_payload,self__.token,self__.__meta,self__.__extmap,null));
} else {
if(cljs.core.truth_((pred__41131.cljs$core$IFn$_invoke$arity$2 ? pred__41131.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"pending-payload","pending-payload",-33788942),expr__41132) : pred__41131.call(null,new cljs.core.Keyword(null,"pending-payload","pending-payload",-33788942),expr__41132)))){
return (new cljs_flux.dispatcher.Dispatcher(self__.registered,self__.dispatching_QMARK_,G__41102,self__.token,self__.__meta,self__.__extmap,null));
} else {
if(cljs.core.truth_((pred__41131.cljs$core$IFn$_invoke$arity$2 ? pred__41131.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"token","token",-1211463215),expr__41132) : pred__41131.call(null,new cljs.core.Keyword(null,"token","token",-1211463215),expr__41132)))){
return (new cljs_flux.dispatcher.Dispatcher(self__.registered,self__.dispatching_QMARK_,self__.pending_payload,G__41102,self__.__meta,self__.__extmap,null));
} else {
return (new cljs_flux.dispatcher.Dispatcher(self__.registered,self__.dispatching_QMARK_,self__.pending_payload,self__.token,self__.__meta,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(self__.__extmap,k__4388__auto__,G__41102),null));
}
}
}
}
}));

(cljs_flux.dispatcher.Dispatcher.prototype.cljs$core$ISeqable$_seq$arity$1 = (function (this__4392__auto__){
var self__ = this;
var this__4392__auto____$1 = this;
return cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [(new cljs.core.MapEntry(new cljs.core.Keyword(null,"registered","registered",-388600037),self__.registered,null)),(new cljs.core.MapEntry(new cljs.core.Keyword(null,"dispatching?","dispatching?",443224906),self__.dispatching_QMARK_,null)),(new cljs.core.MapEntry(new cljs.core.Keyword(null,"pending-payload","pending-payload",-33788942),self__.pending_payload,null)),(new cljs.core.MapEntry(new cljs.core.Keyword(null,"token","token",-1211463215),self__.token,null))], null),self__.__extmap));
}));

(cljs_flux.dispatcher.Dispatcher.prototype.cljs_flux$dispatcher$IDispatcher$ = cljs.core.PROTOCOL_SENTINEL);

(cljs_flux.dispatcher.Dispatcher.prototype.cljs_flux$dispatcher$IDispatcher$register$arity$2 = (function (_,f){
var self__ = this;
var ___$1 = this;
var id = [cljs_flux.dispatcher.prefix,cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(self__.token,cljs.core.inc))].join('');
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(self__.registered,cljs.core.assoc_in,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [id,new cljs.core.Keyword(null,"callback","callback",-705136228)], null),f);

return id;
}));

(cljs_flux.dispatcher.Dispatcher.prototype.cljs_flux$dispatcher$IDispatcher$unregister$arity$2 = (function (_,id){
var self__ = this;
var ___$1 = this;
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(self__.registered,cljs.core.dissoc,id);
}));

(cljs_flux.dispatcher.Dispatcher.prototype.cljs_flux$dispatcher$IDispatcher$_invoke_callback$arity$2 = (function (_,id){
var self__ = this;
var ___$1 = this;
var callback = new cljs.core.Keyword(null,"callback","callback",-705136228).cljs$core$IFn$_invoke$arity$1(cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(self__.registered),id));
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(self__.registered,cljs.core.assoc_in,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [id,new cljs.core.Keyword(null,"pending?","pending?",-2133618792)], null),true);

var G__41145_41302 = cljs.core.deref(self__.pending_payload);
(callback.cljs$core$IFn$_invoke$arity$1 ? callback.cljs$core$IFn$_invoke$arity$1(G__41145_41302) : callback.call(null,G__41145_41302));

return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(self__.registered,cljs.core.assoc_in,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [id,new cljs.core.Keyword(null,"handled?","handled?",-538613463)], null),true);
}));

(cljs_flux.dispatcher.Dispatcher.prototype.cljs_flux$dispatcher$IDispatcher$wait_for$arity$2 = (function (this$,ids){
var self__ = this;
var this$__$1 = this;
if(cljs.core.truth_(cljs.core.deref(self__.dispatching_QMARK_))){
} else {
throw (new Error("wait-for must be invoked while dispatching."));
}

var seq__41152 = cljs.core.seq(ids);
var chunk__41155 = null;
var count__41156 = (0);
var i__41157 = (0);
while(true){
if((i__41157 < count__41156)){
var id = chunk__41155.cljs$core$IIndexed$_nth$arity$2(null,i__41157);
var map__41183 = (function (){var fexpr__41184 = cljs.core.deref(self__.registered);
return (fexpr__41184.cljs$core$IFn$_invoke$arity$1 ? fexpr__41184.cljs$core$IFn$_invoke$arity$1(id) : fexpr__41184.call(null,id));
})();
var map__41183__$1 = (((((!((map__41183 == null))))?(((((map__41183.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__41183.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__41183):map__41183);
var pending_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41183__$1,new cljs.core.Keyword(null,"pending?","pending?",-2133618792));
var handled_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41183__$1,new cljs.core.Keyword(null,"handled?","handled?",-538613463));
var callback = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41183__$1,new cljs.core.Keyword(null,"callback","callback",-705136228));
if(cljs.core.not(handled_QMARK_)){
if(cljs.core.truth_(pending_QMARK_)){
throw (new Error(["Circular dependency detected while waiting for `",cljs.core.str.cljs$core$IFn$_invoke$arity$1(id),"'."].join('')));
} else {
if((!((callback == null)))){
this$__$1.cljs_flux$dispatcher$IDispatcher$_invoke_callback$arity$2(null,id);
} else {
throw (new Error(["`",cljs.core.str.cljs$core$IFn$_invoke$arity$1(id),"' does not map to a registered callback."].join('')));

}
}


var G__41308 = seq__41152;
var G__41309 = chunk__41155;
var G__41310 = count__41156;
var G__41311 = (i__41157 + (1));
seq__41152 = G__41308;
chunk__41155 = G__41309;
count__41156 = G__41310;
i__41157 = G__41311;
continue;
} else {
var G__41313 = seq__41152;
var G__41314 = chunk__41155;
var G__41315 = count__41156;
var G__41316 = (i__41157 + (1));
seq__41152 = G__41313;
chunk__41155 = G__41314;
count__41156 = G__41315;
i__41157 = G__41316;
continue;
}
} else {
var temp__5735__auto__ = cljs.core.seq(seq__41152);
if(temp__5735__auto__){
var seq__41152__$1 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__41152__$1)){
var c__4556__auto__ = cljs.core.chunk_first(seq__41152__$1);
var G__41319 = cljs.core.chunk_rest(seq__41152__$1);
var G__41320 = c__4556__auto__;
var G__41321 = cljs.core.count(c__4556__auto__);
var G__41322 = (0);
seq__41152 = G__41319;
chunk__41155 = G__41320;
count__41156 = G__41321;
i__41157 = G__41322;
continue;
} else {
var id = cljs.core.first(seq__41152__$1);
var map__41189 = (function (){var fexpr__41190 = cljs.core.deref(self__.registered);
return (fexpr__41190.cljs$core$IFn$_invoke$arity$1 ? fexpr__41190.cljs$core$IFn$_invoke$arity$1(id) : fexpr__41190.call(null,id));
})();
var map__41189__$1 = (((((!((map__41189 == null))))?(((((map__41189.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__41189.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__41189):map__41189);
var pending_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41189__$1,new cljs.core.Keyword(null,"pending?","pending?",-2133618792));
var handled_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41189__$1,new cljs.core.Keyword(null,"handled?","handled?",-538613463));
var callback = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41189__$1,new cljs.core.Keyword(null,"callback","callback",-705136228));
if(cljs.core.not(handled_QMARK_)){
if(cljs.core.truth_(pending_QMARK_)){
throw (new Error(["Circular dependency detected while waiting for `",cljs.core.str.cljs$core$IFn$_invoke$arity$1(id),"'."].join('')));
} else {
if((!((callback == null)))){
this$__$1.cljs_flux$dispatcher$IDispatcher$_invoke_callback$arity$2(null,id);
} else {
throw (new Error(["`",cljs.core.str.cljs$core$IFn$_invoke$arity$1(id),"' does not map to a registered callback."].join('')));

}
}


var G__41324 = cljs.core.next(seq__41152__$1);
var G__41325 = null;
var G__41326 = (0);
var G__41327 = (0);
seq__41152 = G__41324;
chunk__41155 = G__41325;
count__41156 = G__41326;
i__41157 = G__41327;
continue;
} else {
var G__41328 = cljs.core.next(seq__41152__$1);
var G__41329 = null;
var G__41330 = (0);
var G__41331 = (0);
seq__41152 = G__41328;
chunk__41155 = G__41329;
count__41156 = G__41330;
i__41157 = G__41331;
continue;
}
}
} else {
return null;
}
}
break;
}
}));

(cljs_flux.dispatcher.Dispatcher.prototype.cljs_flux$dispatcher$IDispatcher$dispatch$arity$2 = (function (this$,payload){
var self__ = this;
var this$__$1 = this;
if(cljs.core.truth_(cljs.core.deref(self__.dispatching_QMARK_))){
throw (new Error("Cannot dispatch in the middle of a dispatch."));
} else {
}

var ids = cljs.core.sort.cljs$core$IFn$_invoke$arity$1(cljs.core.keys(cljs.core.deref(self__.registered)));
cljs.core.reset_BANG_(self__.pending_payload,payload);

cljs.core.reset_BANG_(self__.dispatching_QMARK_,true);

var seq__41197_41336 = cljs.core.seq(ids);
var chunk__41198_41337 = null;
var count__41199_41338 = (0);
var i__41200_41339 = (0);
while(true){
if((i__41200_41339 < count__41199_41338)){
var id_41340 = chunk__41198_41337.cljs$core$IIndexed$_nth$arity$2(null,i__41200_41339);
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(self__.registered,cljs.core.assoc_in,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [id_41340,new cljs.core.Keyword(null,"pending?","pending?",-2133618792)], null),false);

cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(self__.registered,cljs.core.assoc_in,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [id_41340,new cljs.core.Keyword(null,"handled?","handled?",-538613463)], null),false);


var G__41341 = seq__41197_41336;
var G__41342 = chunk__41198_41337;
var G__41343 = count__41199_41338;
var G__41344 = (i__41200_41339 + (1));
seq__41197_41336 = G__41341;
chunk__41198_41337 = G__41342;
count__41199_41338 = G__41343;
i__41200_41339 = G__41344;
continue;
} else {
var temp__5735__auto___41345 = cljs.core.seq(seq__41197_41336);
if(temp__5735__auto___41345){
var seq__41197_41346__$1 = temp__5735__auto___41345;
if(cljs.core.chunked_seq_QMARK_(seq__41197_41346__$1)){
var c__4556__auto___41347 = cljs.core.chunk_first(seq__41197_41346__$1);
var G__41348 = cljs.core.chunk_rest(seq__41197_41346__$1);
var G__41349 = c__4556__auto___41347;
var G__41350 = cljs.core.count(c__4556__auto___41347);
var G__41351 = (0);
seq__41197_41336 = G__41348;
chunk__41198_41337 = G__41349;
count__41199_41338 = G__41350;
i__41200_41339 = G__41351;
continue;
} else {
var id_41352 = cljs.core.first(seq__41197_41346__$1);
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(self__.registered,cljs.core.assoc_in,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [id_41352,new cljs.core.Keyword(null,"pending?","pending?",-2133618792)], null),false);

cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(self__.registered,cljs.core.assoc_in,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [id_41352,new cljs.core.Keyword(null,"handled?","handled?",-538613463)], null),false);


var G__41353 = cljs.core.next(seq__41197_41346__$1);
var G__41354 = null;
var G__41355 = (0);
var G__41356 = (0);
seq__41197_41336 = G__41353;
chunk__41198_41337 = G__41354;
count__41199_41338 = G__41355;
i__41200_41339 = G__41356;
continue;
}
} else {
}
}
break;
}

try{var seq__41211 = cljs.core.seq(ids);
var chunk__41213 = null;
var count__41214 = (0);
var i__41215 = (0);
while(true){
if((i__41215 < count__41214)){
var id = chunk__41213.cljs$core$IIndexed$_nth$arity$2(null,i__41215);
if(cljs.core.not(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(self__.registered),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [id,new cljs.core.Keyword(null,"pending?","pending?",-2133618792)], null)))){
this$__$1.cljs_flux$dispatcher$IDispatcher$_invoke_callback$arity$2(null,id);


var G__41358 = seq__41211;
var G__41359 = chunk__41213;
var G__41360 = count__41214;
var G__41361 = (i__41215 + (1));
seq__41211 = G__41358;
chunk__41213 = G__41359;
count__41214 = G__41360;
i__41215 = G__41361;
continue;
} else {
var G__41363 = seq__41211;
var G__41364 = chunk__41213;
var G__41365 = count__41214;
var G__41366 = (i__41215 + (1));
seq__41211 = G__41363;
chunk__41213 = G__41364;
count__41214 = G__41365;
i__41215 = G__41366;
continue;
}
} else {
var temp__5735__auto__ = cljs.core.seq(seq__41211);
if(temp__5735__auto__){
var seq__41211__$1 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__41211__$1)){
var c__4556__auto__ = cljs.core.chunk_first(seq__41211__$1);
var G__41368 = cljs.core.chunk_rest(seq__41211__$1);
var G__41369 = c__4556__auto__;
var G__41370 = cljs.core.count(c__4556__auto__);
var G__41371 = (0);
seq__41211 = G__41368;
chunk__41213 = G__41369;
count__41214 = G__41370;
i__41215 = G__41371;
continue;
} else {
var id = cljs.core.first(seq__41211__$1);
if(cljs.core.not(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(self__.registered),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [id,new cljs.core.Keyword(null,"pending?","pending?",-2133618792)], null)))){
this$__$1.cljs_flux$dispatcher$IDispatcher$_invoke_callback$arity$2(null,id);


var G__41372 = cljs.core.next(seq__41211__$1);
var G__41373 = null;
var G__41374 = (0);
var G__41375 = (0);
seq__41211 = G__41372;
chunk__41213 = G__41373;
count__41214 = G__41374;
i__41215 = G__41375;
continue;
} else {
var G__41376 = cljs.core.next(seq__41211__$1);
var G__41377 = null;
var G__41378 = (0);
var G__41379 = (0);
seq__41211 = G__41376;
chunk__41213 = G__41377;
count__41214 = G__41378;
i__41215 = G__41379;
continue;
}
}
} else {
return null;
}
}
break;
}
}finally {cljs.core.reset_BANG_(self__.dispatching_QMARK_,false);

cljs.core.reset_BANG_(self__.pending_payload,null);
}}));

(cljs_flux.dispatcher.Dispatcher.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (this__4379__auto__,G__41102){
var self__ = this;
var this__4379__auto____$1 = this;
return (new cljs_flux.dispatcher.Dispatcher(self__.registered,self__.dispatching_QMARK_,self__.pending_payload,self__.token,G__41102,self__.__extmap,self__.__hash));
}));

(cljs_flux.dispatcher.Dispatcher.prototype.cljs$core$ICollection$_conj$arity$2 = (function (this__4385__auto__,entry__4386__auto__){
var self__ = this;
var this__4385__auto____$1 = this;
if(cljs.core.vector_QMARK_(entry__4386__auto__)){
return this__4385__auto____$1.cljs$core$IAssociative$_assoc$arity$3(null,cljs.core._nth(entry__4386__auto__,(0)),cljs.core._nth(entry__4386__auto__,(1)));
} else {
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3(cljs.core._conj,this__4385__auto____$1,entry__4386__auto__);
}
}));

(cljs_flux.dispatcher.Dispatcher.getBasis = (function (){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"registered","registered",1251931490,null),new cljs.core.Symbol(null,"dispatching?","dispatching?",2083756433,null),new cljs.core.Symbol(null,"pending-payload","pending-payload",1606742585,null),new cljs.core.Symbol(null,"token","token",429068312,null)], null);
}));

(cljs_flux.dispatcher.Dispatcher.cljs$lang$type = true);

(cljs_flux.dispatcher.Dispatcher.cljs$lang$ctorPrSeq = (function (this__4423__auto__){
return (new cljs.core.List(null,"cljs-flux.dispatcher/Dispatcher",null,(1),null));
}));

(cljs_flux.dispatcher.Dispatcher.cljs$lang$ctorPrWriter = (function (this__4423__auto__,writer__4424__auto__){
return cljs.core._write(writer__4424__auto__,"cljs-flux.dispatcher/Dispatcher");
}));

/**
 * Positional factory function for cljs-flux.dispatcher/Dispatcher.
 */
cljs_flux.dispatcher.__GT_Dispatcher = (function cljs_flux$dispatcher$__GT_Dispatcher(registered,dispatching_QMARK_,pending_payload,token){
return (new cljs_flux.dispatcher.Dispatcher(registered,dispatching_QMARK_,pending_payload,token,null,null,null));
});

/**
 * Factory function for cljs-flux.dispatcher/Dispatcher, taking a map of keywords to field values.
 */
cljs_flux.dispatcher.map__GT_Dispatcher = (function cljs_flux$dispatcher$map__GT_Dispatcher(G__41108){
var extmap__4419__auto__ = (function (){var G__41242 = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(G__41108,new cljs.core.Keyword(null,"registered","registered",-388600037),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"dispatching?","dispatching?",443224906),new cljs.core.Keyword(null,"pending-payload","pending-payload",-33788942),new cljs.core.Keyword(null,"token","token",-1211463215)], 0));
if(cljs.core.record_QMARK_(G__41108)){
return cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,G__41242);
} else {
return G__41242;
}
})();
return (new cljs_flux.dispatcher.Dispatcher(new cljs.core.Keyword(null,"registered","registered",-388600037).cljs$core$IFn$_invoke$arity$1(G__41108),new cljs.core.Keyword(null,"dispatching?","dispatching?",443224906).cljs$core$IFn$_invoke$arity$1(G__41108),new cljs.core.Keyword(null,"pending-payload","pending-payload",-33788942).cljs$core$IFn$_invoke$arity$1(G__41108),new cljs.core.Keyword(null,"token","token",-1211463215).cljs$core$IFn$_invoke$arity$1(G__41108),null,cljs.core.not_empty(extmap__4419__auto__),null));
});

/**
 * Create and return Flux dispatcher instance
 */
cljs_flux.dispatcher.dispatcher = (function cljs_flux$dispatcher$dispatcher(){
return cljs_flux.dispatcher.map__GT_Dispatcher(new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"registered","registered",-388600037),cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY),new cljs.core.Keyword(null,"dispatching?","dispatching?",443224906),cljs.core.atom.cljs$core$IFn$_invoke$arity$1(false),new cljs.core.Keyword(null,"pending-payload","pending-payload",-33788942),cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null),new cljs.core.Keyword(null,"token","token",-1211463215),cljs.core.atom.cljs$core$IFn$_invoke$arity$1((0))], null));
});

//# sourceMappingURL=cljs_flux.dispatcher.js.map
