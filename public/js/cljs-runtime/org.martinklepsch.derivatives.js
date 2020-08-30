goog.provide('org.martinklepsch.derivatives');
org.martinklepsch.derivatives.prefix_id = (function org$martinklepsch$derivatives$prefix_id(){
return cljs.core.random_uuid();
});
/**
 * Variation of `depend` that takes a list of dependencies instead of one
 */
org.martinklepsch.derivatives.depend_SINGLEQUOTE_ = (function org$martinklepsch$derivatives$depend_SINGLEQUOTE_(graph,node,deps){
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (p1__38935_SHARP_,p2__38936_SHARP_){
return com.stuartsierra.dependency.depend(p1__38935_SHARP_,node,p2__38936_SHARP_);
}),graph,deps);
});
/**
 * Turn a given spec into a dependency graph
 */
org.martinklepsch.derivatives.spec__GT_graph = (function org$martinklepsch$derivatives$spec__GT_graph(spec){
return cljs.core.reduce_kv((function (graph,id,p__38945){
var vec__38946 = p__38945;
var dependencies = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38946,(0),null);
return org.martinklepsch.derivatives.depend_SINGLEQUOTE_(graph,id,dependencies);
}),com.stuartsierra.dependency.graph(),spec);
});
/**
 * Calculate all dependencies for `ks` and return a set with the dependencies and `ks`
 */
org.martinklepsch.derivatives.calc_deps = (function org$martinklepsch$derivatives$calc_deps(graph,ks){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(clojure.set.union,cljs.core.set(ks),cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__38953_SHARP_){
return com.stuartsierra.dependency.transitive_dependencies(graph,p1__38953_SHARP_);
}),ks));
});
/**
 * Platform-agnostic helper to determine if something is watchable (atom, etc)
 */
org.martinklepsch.derivatives.watchable_QMARK_ = (function org$martinklepsch$derivatives$watchable_QMARK_(x){
if((!((x == null)))){
if((((x.cljs$lang$protocol_mask$partition1$ & (2))) || ((cljs.core.PROTOCOL_SENTINEL === x.cljs$core$IWatchable$)))){
return true;
} else {
if((!x.cljs$lang$protocol_mask$partition1$)){
return cljs.core.native_satisfies_QMARK_(cljs.core.IWatchable,x);
} else {
return false;
}
}
} else {
return cljs.core.native_satisfies_QMARK_(cljs.core.IWatchable,x);
}
});
org.martinklepsch.derivatives.not_required = (function org$martinklepsch$derivatives$not_required(drv_map,required_QMARK_){
if(cljs.core.set_QMARK_(required_QMARK_)){
} else {
throw (new Error("Assert failed: (set? required?)"));
}

return cljs.core.reduce_kv((function (xs,k,drv_val){
if(cljs.core.truth_((required_QMARK_.cljs$core$IFn$_invoke$arity$1 ? required_QMARK_.cljs$core$IFn$_invoke$arity$1(k) : required_QMARK_.call(null,k)))){
return xs;
} else {
return cljs.core.conj.cljs$core$IFn$_invoke$arity$2(xs,drv_val);
}
}),cljs.core.PersistentVector.EMPTY,drv_map);
});
/**
 * Update the derivatives map `drv-map` so that all keys passed in `order`
 *   are statisfied and any superfluous keys are removed.
 *   Values of superfluous keys that implement IDisposable they will also be disposed.
 */
org.martinklepsch.derivatives.sync_derivatives_BANG_ = (function org$martinklepsch$derivatives$sync_derivatives_BANG_(spec,watch_key_prefix,drv_map,order){
var seq__38964_39099 = cljs.core.seq(org.martinklepsch.derivatives.not_required(drv_map,cljs.core.set(order)));
var chunk__38965_39100 = null;
var count__38966_39101 = (0);
var i__38967_39102 = (0);
while(true){
if((i__38967_39102 < count__38966_39101)){
var drv_val_39103 = chunk__38965_39100.cljs$core$IIndexed$_nth$arity$2(null,i__38967_39102);
if((((!((drv_val_39103 == null))))?((((false) || ((cljs.core.PROTOCOL_SENTINEL === drv_val_39103.org$martinklepsch$derived$IDisposable$))))?true:(((!drv_val_39103.cljs$lang$protocol_mask$partition$))?cljs.core.native_satisfies_QMARK_(org.martinklepsch.derived.IDisposable,drv_val_39103):false)):cljs.core.native_satisfies_QMARK_(org.martinklepsch.derived.IDisposable,drv_val_39103))){
org.martinklepsch.derived.dispose_BANG_(drv_val_39103);
} else {
}


var G__39104 = seq__38964_39099;
var G__39105 = chunk__38965_39100;
var G__39106 = count__38966_39101;
var G__39107 = (i__38967_39102 + (1));
seq__38964_39099 = G__39104;
chunk__38965_39100 = G__39105;
count__38966_39101 = G__39106;
i__38967_39102 = G__39107;
continue;
} else {
var temp__5735__auto___39108 = cljs.core.seq(seq__38964_39099);
if(temp__5735__auto___39108){
var seq__38964_39109__$1 = temp__5735__auto___39108;
if(cljs.core.chunked_seq_QMARK_(seq__38964_39109__$1)){
var c__4556__auto___39110 = cljs.core.chunk_first(seq__38964_39109__$1);
var G__39111 = cljs.core.chunk_rest(seq__38964_39109__$1);
var G__39112 = c__4556__auto___39110;
var G__39113 = cljs.core.count(c__4556__auto___39110);
var G__39114 = (0);
seq__38964_39099 = G__39111;
chunk__38965_39100 = G__39112;
count__38966_39101 = G__39113;
i__38967_39102 = G__39114;
continue;
} else {
var drv_val_39115 = cljs.core.first(seq__38964_39109__$1);
if((((!((drv_val_39115 == null))))?((((false) || ((cljs.core.PROTOCOL_SENTINEL === drv_val_39115.org$martinklepsch$derived$IDisposable$))))?true:(((!drv_val_39115.cljs$lang$protocol_mask$partition$))?cljs.core.native_satisfies_QMARK_(org.martinklepsch.derived.IDisposable,drv_val_39115):false)):cljs.core.native_satisfies_QMARK_(org.martinklepsch.derived.IDisposable,drv_val_39115))){
org.martinklepsch.derived.dispose_BANG_(drv_val_39115);
} else {
}


var G__39116 = cljs.core.next(seq__38964_39109__$1);
var G__39117 = null;
var G__39118 = (0);
var G__39119 = (0);
seq__38964_39099 = G__39116;
chunk__38965_39100 = G__39117;
count__38966_39101 = G__39118;
i__38967_39102 = G__39119;
continue;
}
} else {
}
}
break;
}

return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (m,k){
var vec__38972 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(spec,k);
var direct_deps = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38972,(0),null);
var derive = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38972,(1),null);
if(cljs.core.truth_(cljs.core.get.cljs$core$IFn$_invoke$arity$2(m,k))){
return m;
} else {
if(org.martinklepsch.derivatives.watchable_QMARK_(derive)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(m,k,derive);
} else {
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(m,k,org.martinklepsch.derived.derived_value.cljs$core$IFn$_invoke$arity$3(cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__38963_SHARP_){
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(m,p1__38963_SHARP_);
}),direct_deps),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [watch_key_prefix,k], null),derive));
}
}
}),cljs.core.select_keys(drv_map,order),order);
});
/**
 * Given a spec return a map of similar structure replacing it's values with
 *   derived atoms built based on the depedency information encoded in the spec
 * 
 *   WARNING: This will create derived atoms for all keys so it may lead
 *   to some uneccesary computations To avoid this issue consider using
 *   `derivatives-pool` which manages derivatives in a registry
 *   removing them as soon as they become unused
 */
org.martinklepsch.derivatives.build = (function org$martinklepsch$derivatives$build(spec){
if(cljs.core.map_QMARK_(spec)){
} else {
throw (new Error("Assert failed: (map? spec)"));
}

return org.martinklepsch.derivatives.sync_derivatives_BANG_(spec,org.martinklepsch.derivatives.prefix_id(),cljs.core.PersistentArrayMap.EMPTY,com.stuartsierra.dependency.topo_sort(org.martinklepsch.derivatives.spec__GT_graph(spec)));
});
org.martinklepsch.derivatives.required_drvs = (function org$martinklepsch$derivatives$required_drvs(graph,registry){
var required_QMARK_ = org.martinklepsch.derivatives.calc_deps(graph,cljs.core.keys(registry));
return ((cljs.core.seq(cljs.core.filter.cljs$core$IFn$_invoke$arity$2(required_QMARK_,com.stuartsierra.dependency.topo_sort(graph)))) || (cljs.core.seq(required_QMARK_)));
});

/**
 * @interface
 */
org.martinklepsch.derivatives.IDerivativesPool = function(){};

var org$martinklepsch$derivatives$IDerivativesPool$get_BANG_$dyn_39120 = (function (this$,drv_k,token){
var x__4428__auto__ = (((this$ == null))?null:this$);
var m__4429__auto__ = (org.martinklepsch.derivatives.get_BANG_[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$3 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$3(this$,drv_k,token) : m__4429__auto__.call(null,this$,drv_k,token));
} else {
var m__4426__auto__ = (org.martinklepsch.derivatives.get_BANG_["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$3 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$3(this$,drv_k,token) : m__4426__auto__.call(null,this$,drv_k,token));
} else {
throw cljs.core.missing_protocol("IDerivativesPool.get!",this$);
}
}
});
org.martinklepsch.derivatives.get_BANG_ = (function org$martinklepsch$derivatives$get_BANG_(this$,drv_k,token){
if((((!((this$ == null)))) && ((!((this$.org$martinklepsch$derivatives$IDerivativesPool$get_BANG_$arity$3 == null)))))){
return this$.org$martinklepsch$derivatives$IDerivativesPool$get_BANG_$arity$3(this$,drv_k,token);
} else {
return org$martinklepsch$derivatives$IDerivativesPool$get_BANG_$dyn_39120(this$,drv_k,token);
}
});

var org$martinklepsch$derivatives$IDerivativesPool$release_BANG_$dyn_39121 = (function (this$,drv_k,token){
var x__4428__auto__ = (((this$ == null))?null:this$);
var m__4429__auto__ = (org.martinklepsch.derivatives.release_BANG_[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$3 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$3(this$,drv_k,token) : m__4429__auto__.call(null,this$,drv_k,token));
} else {
var m__4426__auto__ = (org.martinklepsch.derivatives.release_BANG_["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$3 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$3(this$,drv_k,token) : m__4426__auto__.call(null,this$,drv_k,token));
} else {
throw cljs.core.missing_protocol("IDerivativesPool.release!",this$);
}
}
});
org.martinklepsch.derivatives.release_BANG_ = (function org$martinklepsch$derivatives$release_BANG_(this$,drv_k,token){
if((((!((this$ == null)))) && ((!((this$.org$martinklepsch$derivatives$IDerivativesPool$release_BANG_$arity$3 == null)))))){
return this$.org$martinklepsch$derivatives$IDerivativesPool$release_BANG_$arity$3(this$,drv_k,token);
} else {
return org$martinklepsch$derivatives$IDerivativesPool$release_BANG_$dyn_39121(this$,drv_k,token);
}
});


/**
* @constructor
 * @implements {cljs.core.IRecord}
 * @implements {cljs.core.IKVReduce}
 * @implements {cljs.core.IEquiv}
 * @implements {cljs.core.IHash}
 * @implements {cljs.core.ICollection}
 * @implements {org.martinklepsch.derivatives.IDerivativesPool}
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
org.martinklepsch.derivatives.DerivativesPool = (function (spec,watch_key_prefix,graph,state,__meta,__extmap,__hash){
this.spec = spec;
this.watch_key_prefix = watch_key_prefix;
this.graph = graph;
this.state = state;
this.__meta = __meta;
this.__extmap = __extmap;
this.__hash = __hash;
this.cljs$lang$protocol_mask$partition0$ = 2230716170;
this.cljs$lang$protocol_mask$partition1$ = 139264;
});
(org.martinklepsch.derivatives.DerivativesPool.prototype.cljs$core$ILookup$_lookup$arity$2 = (function (this__4380__auto__,k__4381__auto__){
var self__ = this;
var this__4380__auto____$1 = this;
return this__4380__auto____$1.cljs$core$ILookup$_lookup$arity$3(null,k__4381__auto__,null);
}));

(org.martinklepsch.derivatives.DerivativesPool.prototype.cljs$core$ILookup$_lookup$arity$3 = (function (this__4382__auto__,k39005,else__4383__auto__){
var self__ = this;
var this__4382__auto____$1 = this;
var G__39052 = k39005;
var G__39052__$1 = (((G__39052 instanceof cljs.core.Keyword))?G__39052.fqn:null);
switch (G__39052__$1) {
case "spec":
return self__.spec;

break;
case "watch-key-prefix":
return self__.watch_key_prefix;

break;
case "graph":
return self__.graph;

break;
case "state":
return self__.state;

break;
default:
return cljs.core.get.cljs$core$IFn$_invoke$arity$3(self__.__extmap,k39005,else__4383__auto__);

}
}));

(org.martinklepsch.derivatives.DerivativesPool.prototype.cljs$core$IKVReduce$_kv_reduce$arity$3 = (function (this__4399__auto__,f__4400__auto__,init__4401__auto__){
var self__ = this;
var this__4399__auto____$1 = this;
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (ret__4402__auto__,p__39064){
var vec__39067 = p__39064;
var k__4403__auto__ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__39067,(0),null);
var v__4404__auto__ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__39067,(1),null);
return (f__4400__auto__.cljs$core$IFn$_invoke$arity$3 ? f__4400__auto__.cljs$core$IFn$_invoke$arity$3(ret__4402__auto__,k__4403__auto__,v__4404__auto__) : f__4400__auto__.call(null,ret__4402__auto__,k__4403__auto__,v__4404__auto__));
}),init__4401__auto__,this__4399__auto____$1);
}));

(org.martinklepsch.derivatives.DerivativesPool.prototype.cljs$core$IPrintWithWriter$_pr_writer$arity$3 = (function (this__4394__auto__,writer__4395__auto__,opts__4396__auto__){
var self__ = this;
var this__4394__auto____$1 = this;
var pr_pair__4397__auto__ = (function (keyval__4398__auto__){
return cljs.core.pr_sequential_writer(writer__4395__auto__,cljs.core.pr_writer,""," ","",opts__4396__auto__,keyval__4398__auto__);
});
return cljs.core.pr_sequential_writer(writer__4395__auto__,pr_pair__4397__auto__,"#org.martinklepsch.derivatives.DerivativesPool{",", ","}",opts__4396__auto__,cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [(new cljs.core.PersistentVector(null,2,(5),cljs.core.PersistentVector.EMPTY_NODE,[new cljs.core.Keyword(null,"spec","spec",347520401),self__.spec],null)),(new cljs.core.PersistentVector(null,2,(5),cljs.core.PersistentVector.EMPTY_NODE,[new cljs.core.Keyword(null,"watch-key-prefix","watch-key-prefix",-1926186767),self__.watch_key_prefix],null)),(new cljs.core.PersistentVector(null,2,(5),cljs.core.PersistentVector.EMPTY_NODE,[new cljs.core.Keyword(null,"graph","graph",1558099509),self__.graph],null)),(new cljs.core.PersistentVector(null,2,(5),cljs.core.PersistentVector.EMPTY_NODE,[new cljs.core.Keyword(null,"state","state",-1988618099),self__.state],null))], null),self__.__extmap));
}));

(org.martinklepsch.derivatives.DerivativesPool.prototype.cljs$core$IIterable$_iterator$arity$1 = (function (G__39004){
var self__ = this;
var G__39004__$1 = this;
return (new cljs.core.RecordIter((0),G__39004__$1,4,new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"spec","spec",347520401),new cljs.core.Keyword(null,"watch-key-prefix","watch-key-prefix",-1926186767),new cljs.core.Keyword(null,"graph","graph",1558099509),new cljs.core.Keyword(null,"state","state",-1988618099)], null),(cljs.core.truth_(self__.__extmap)?cljs.core._iterator(self__.__extmap):cljs.core.nil_iter())));
}));

(org.martinklepsch.derivatives.DerivativesPool.prototype.cljs$core$IMeta$_meta$arity$1 = (function (this__4378__auto__){
var self__ = this;
var this__4378__auto____$1 = this;
return self__.__meta;
}));

(org.martinklepsch.derivatives.DerivativesPool.prototype.cljs$core$ICloneable$_clone$arity$1 = (function (this__4375__auto__){
var self__ = this;
var this__4375__auto____$1 = this;
return (new org.martinklepsch.derivatives.DerivativesPool(self__.spec,self__.watch_key_prefix,self__.graph,self__.state,self__.__meta,self__.__extmap,self__.__hash));
}));

(org.martinklepsch.derivatives.DerivativesPool.prototype.cljs$core$ICounted$_count$arity$1 = (function (this__4384__auto__){
var self__ = this;
var this__4384__auto____$1 = this;
return (4 + cljs.core.count(self__.__extmap));
}));

(org.martinklepsch.derivatives.DerivativesPool.prototype.cljs$core$IHash$_hash$arity$1 = (function (this__4376__auto__){
var self__ = this;
var this__4376__auto____$1 = this;
var h__4238__auto__ = self__.__hash;
if((!((h__4238__auto__ == null)))){
return h__4238__auto__;
} else {
var h__4238__auto____$1 = (function (coll__4377__auto__){
return (-726615506 ^ cljs.core.hash_unordered_coll(coll__4377__auto__));
})(this__4376__auto____$1);
(self__.__hash = h__4238__auto____$1);

return h__4238__auto____$1;
}
}));

(org.martinklepsch.derivatives.DerivativesPool.prototype.cljs$core$IEquiv$_equiv$arity$2 = (function (this39006,other39007){
var self__ = this;
var this39006__$1 = this;
return (((!((other39007 == null)))) && ((this39006__$1.constructor === other39007.constructor)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this39006__$1.spec,other39007.spec)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this39006__$1.watch_key_prefix,other39007.watch_key_prefix)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this39006__$1.graph,other39007.graph)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this39006__$1.state,other39007.state)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this39006__$1.__extmap,other39007.__extmap)));
}));

(org.martinklepsch.derivatives.DerivativesPool.prototype.org$martinklepsch$derivatives$IDerivativesPool$ = cljs.core.PROTOCOL_SENTINEL);

(org.martinklepsch.derivatives.DerivativesPool.prototype.org$martinklepsch$derivatives$IDerivativesPool$get_BANG_$arity$3 = (function (this$,drv_k,token){
var self__ = this;
var this$__$1 = this;
if(cljs.core.not(cljs.core.get.cljs$core$IFn$_invoke$arity$2(self__.spec,drv_k))){
throw cljs.core.ex_info.cljs$core$IFn$_invoke$arity$2(["No derivative defined for ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(drv_k)].join(''),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),drv_k], null));
} else {
var new_reg = cljs.core.update.cljs$core$IFn$_invoke$arity$4(new cljs.core.Keyword(null,"registry","registry",1021159018).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(self__.state)),drv_k,cljs.core.fnil.cljs$core$IFn$_invoke$arity$2(cljs.core.conj,cljs.core.PersistentHashSet.EMPTY),token);
var new_drvs = org.martinklepsch.derivatives.sync_derivatives_BANG_(self__.spec,self__.watch_key_prefix,new cljs.core.Keyword(null,"derivatives","derivatives",-1346736355).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(self__.state)),org.martinklepsch.derivatives.required_drvs(self__.graph,new_reg));
cljs.core.reset_BANG_(self__.state,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"derivatives","derivatives",-1346736355),new_drvs,new cljs.core.Keyword(null,"registry","registry",1021159018),new_reg], null));

return cljs.core.get.cljs$core$IFn$_invoke$arity$2(new_drvs,drv_k);
}
}));

(org.martinklepsch.derivatives.DerivativesPool.prototype.org$martinklepsch$derivatives$IDerivativesPool$release_BANG_$arity$3 = (function (this$,drv_k,token){
var self__ = this;
var this$__$1 = this;
var registry = new cljs.core.Keyword(null,"registry","registry",1021159018).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(self__.state));
var new_reg = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentHashSet.createAsIfByAssoc([token]),cljs.core.get.cljs$core$IFn$_invoke$arity$2(registry,drv_k)))?cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(registry,drv_k):cljs.core.update.cljs$core$IFn$_invoke$arity$4(registry,drv_k,cljs.core.disj,token));
var new_drvs = org.martinklepsch.derivatives.sync_derivatives_BANG_(self__.spec,self__.watch_key_prefix,new cljs.core.Keyword(null,"derivatives","derivatives",-1346736355).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(self__.state)),org.martinklepsch.derivatives.required_drvs(self__.graph,new_reg));
cljs.core.reset_BANG_(self__.state,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"derivatives","derivatives",-1346736355),new_drvs,new cljs.core.Keyword(null,"registry","registry",1021159018),new_reg], null));

return null;
}));

(org.martinklepsch.derivatives.DerivativesPool.prototype.cljs$core$IMap$_dissoc$arity$2 = (function (this__4389__auto__,k__4390__auto__){
var self__ = this;
var this__4389__auto____$1 = this;
if(cljs.core.contains_QMARK_(new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"state","state",-1988618099),null,new cljs.core.Keyword(null,"watch-key-prefix","watch-key-prefix",-1926186767),null,new cljs.core.Keyword(null,"spec","spec",347520401),null,new cljs.core.Keyword(null,"graph","graph",1558099509),null], null), null),k__4390__auto__)){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core._with_meta(cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,this__4389__auto____$1),self__.__meta),k__4390__auto__);
} else {
return (new org.martinklepsch.derivatives.DerivativesPool(self__.spec,self__.watch_key_prefix,self__.graph,self__.state,self__.__meta,cljs.core.not_empty(cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(self__.__extmap,k__4390__auto__)),null));
}
}));

(org.martinklepsch.derivatives.DerivativesPool.prototype.cljs$core$IAssociative$_assoc$arity$3 = (function (this__4387__auto__,k__4388__auto__,G__39004){
var self__ = this;
var this__4387__auto____$1 = this;
var pred__39085 = cljs.core.keyword_identical_QMARK_;
var expr__39086 = k__4388__auto__;
if(cljs.core.truth_((pred__39085.cljs$core$IFn$_invoke$arity$2 ? pred__39085.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"spec","spec",347520401),expr__39086) : pred__39085.call(null,new cljs.core.Keyword(null,"spec","spec",347520401),expr__39086)))){
return (new org.martinklepsch.derivatives.DerivativesPool(G__39004,self__.watch_key_prefix,self__.graph,self__.state,self__.__meta,self__.__extmap,null));
} else {
if(cljs.core.truth_((pred__39085.cljs$core$IFn$_invoke$arity$2 ? pred__39085.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"watch-key-prefix","watch-key-prefix",-1926186767),expr__39086) : pred__39085.call(null,new cljs.core.Keyword(null,"watch-key-prefix","watch-key-prefix",-1926186767),expr__39086)))){
return (new org.martinklepsch.derivatives.DerivativesPool(self__.spec,G__39004,self__.graph,self__.state,self__.__meta,self__.__extmap,null));
} else {
if(cljs.core.truth_((pred__39085.cljs$core$IFn$_invoke$arity$2 ? pred__39085.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"graph","graph",1558099509),expr__39086) : pred__39085.call(null,new cljs.core.Keyword(null,"graph","graph",1558099509),expr__39086)))){
return (new org.martinklepsch.derivatives.DerivativesPool(self__.spec,self__.watch_key_prefix,G__39004,self__.state,self__.__meta,self__.__extmap,null));
} else {
if(cljs.core.truth_((pred__39085.cljs$core$IFn$_invoke$arity$2 ? pred__39085.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"state","state",-1988618099),expr__39086) : pred__39085.call(null,new cljs.core.Keyword(null,"state","state",-1988618099),expr__39086)))){
return (new org.martinklepsch.derivatives.DerivativesPool(self__.spec,self__.watch_key_prefix,self__.graph,G__39004,self__.__meta,self__.__extmap,null));
} else {
return (new org.martinklepsch.derivatives.DerivativesPool(self__.spec,self__.watch_key_prefix,self__.graph,self__.state,self__.__meta,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(self__.__extmap,k__4388__auto__,G__39004),null));
}
}
}
}
}));

(org.martinklepsch.derivatives.DerivativesPool.prototype.cljs$core$ISeqable$_seq$arity$1 = (function (this__4392__auto__){
var self__ = this;
var this__4392__auto____$1 = this;
return cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [(new cljs.core.MapEntry(new cljs.core.Keyword(null,"spec","spec",347520401),self__.spec,null)),(new cljs.core.MapEntry(new cljs.core.Keyword(null,"watch-key-prefix","watch-key-prefix",-1926186767),self__.watch_key_prefix,null)),(new cljs.core.MapEntry(new cljs.core.Keyword(null,"graph","graph",1558099509),self__.graph,null)),(new cljs.core.MapEntry(new cljs.core.Keyword(null,"state","state",-1988618099),self__.state,null))], null),self__.__extmap));
}));

(org.martinklepsch.derivatives.DerivativesPool.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (this__4379__auto__,G__39004){
var self__ = this;
var this__4379__auto____$1 = this;
return (new org.martinklepsch.derivatives.DerivativesPool(self__.spec,self__.watch_key_prefix,self__.graph,self__.state,G__39004,self__.__extmap,self__.__hash));
}));

(org.martinklepsch.derivatives.DerivativesPool.prototype.cljs$core$ICollection$_conj$arity$2 = (function (this__4385__auto__,entry__4386__auto__){
var self__ = this;
var this__4385__auto____$1 = this;
if(cljs.core.vector_QMARK_(entry__4386__auto__)){
return this__4385__auto____$1.cljs$core$IAssociative$_assoc$arity$3(null,cljs.core._nth(entry__4386__auto__,(0)),cljs.core._nth(entry__4386__auto__,(1)));
} else {
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3(cljs.core._conj,this__4385__auto____$1,entry__4386__auto__);
}
}));

(org.martinklepsch.derivatives.DerivativesPool.getBasis = (function (){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"spec","spec",1988051928,null),new cljs.core.Symbol(null,"watch-key-prefix","watch-key-prefix",-285655240,null),new cljs.core.Symbol(null,"graph","graph",-1096336260,null),new cljs.core.Symbol(null,"state","state",-348086572,null)], null);
}));

(org.martinklepsch.derivatives.DerivativesPool.cljs$lang$type = true);

(org.martinklepsch.derivatives.DerivativesPool.cljs$lang$ctorPrSeq = (function (this__4423__auto__){
return (new cljs.core.List(null,"org.martinklepsch.derivatives/DerivativesPool",null,(1),null));
}));

(org.martinklepsch.derivatives.DerivativesPool.cljs$lang$ctorPrWriter = (function (this__4423__auto__,writer__4424__auto__){
return cljs.core._write(writer__4424__auto__,"org.martinklepsch.derivatives/DerivativesPool");
}));

/**
 * Positional factory function for org.martinklepsch.derivatives/DerivativesPool.
 */
org.martinklepsch.derivatives.__GT_DerivativesPool = (function org$martinklepsch$derivatives$__GT_DerivativesPool(spec,watch_key_prefix,graph,state){
return (new org.martinklepsch.derivatives.DerivativesPool(spec,watch_key_prefix,graph,state,null,null,null));
});

/**
 * Factory function for org.martinklepsch.derivatives/DerivativesPool, taking a map of keywords to field values.
 */
org.martinklepsch.derivatives.map__GT_DerivativesPool = (function org$martinklepsch$derivatives$map__GT_DerivativesPool(G__39008){
var extmap__4419__auto__ = (function (){var G__39088 = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(G__39008,new cljs.core.Keyword(null,"spec","spec",347520401),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"watch-key-prefix","watch-key-prefix",-1926186767),new cljs.core.Keyword(null,"graph","graph",1558099509),new cljs.core.Keyword(null,"state","state",-1988618099)], 0));
if(cljs.core.record_QMARK_(G__39008)){
return cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,G__39088);
} else {
return G__39088;
}
})();
return (new org.martinklepsch.derivatives.DerivativesPool(new cljs.core.Keyword(null,"spec","spec",347520401).cljs$core$IFn$_invoke$arity$1(G__39008),new cljs.core.Keyword(null,"watch-key-prefix","watch-key-prefix",-1926186767).cljs$core$IFn$_invoke$arity$1(G__39008),new cljs.core.Keyword(null,"graph","graph",1558099509).cljs$core$IFn$_invoke$arity$1(G__39008),new cljs.core.Keyword(null,"state","state",-1988618099).cljs$core$IFn$_invoke$arity$1(G__39008),null,cljs.core.not_empty(extmap__4419__auto__),null));
});

/**
 * Given a derivatives spec return a map with `get!` and `free!` functions.
 * 
 *   - (get! derivative-id token) will retrieve a derivative for
 *  `derivative-id` registering the usage with `token`
 *   - (free! derivative-id token) will indicate the derivative `derivative-id`
 *  is no longer needed by `token`, if there are no more tokens needing
 *  the derivative it will be removed
 */
org.martinklepsch.derivatives.derivatives_pool = (function org$martinklepsch$derivatives$derivatives_pool(spec){
return org.martinklepsch.derivatives.map__GT_DerivativesPool(new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"spec","spec",347520401),spec,new cljs.core.Keyword(null,"watch-key-prefix","watch-key-prefix",-1926186767),org.martinklepsch.derivatives.prefix_id(),new cljs.core.Keyword(null,"graph","graph",1558099509),org.martinklepsch.derivatives.spec__GT_graph(spec),new cljs.core.Keyword(null,"state","state",-1988618099),cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY)], null));
});
var get_k_39123 = "org.martinklepsch.derivatives/get";
var release_k_39124 = "org.martinklepsch.derivatives/release";
var context_types_39125 = cljs.core.PersistentArrayMap.createAsIfByAssoc([get_k_39123,PropTypes.func,release_k_39124,PropTypes.func]);
/**
 * Given the passed spec add get!/release! derivative functions to
 *  the child context so they can be seen by components using the `deriv`
 *  mixin.
 */
org.martinklepsch.derivatives.rum_derivatives = (function org$martinklepsch$derivatives$rum_derivatives(spec){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"static-properties","static-properties",-577838503),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"childContextTypes","childContextTypes",578717991),context_types_39125], null),new cljs.core.Keyword(null,"child-context","child-context",-1375270295),(function (_){
var pool = org.martinklepsch.derivatives.derivatives_pool(spec);
return cljs.core.PersistentArrayMap.createAsIfByAssoc([release_k_39124,cljs.core.partial.cljs$core$IFn$_invoke$arity$2(org.martinklepsch.derivatives.release_BANG_,pool),get_k_39123,cljs.core.partial.cljs$core$IFn$_invoke$arity$2(org.martinklepsch.derivatives.get_BANG_,pool)]);
})], null);
});

/**
 * Like rum-derivatives but get the spec from the arguments passed to the components (`:rum/args`) using `get-spec-fn`
 */
org.martinklepsch.derivatives.rum_derivatives_STAR_ = (function org$martinklepsch$derivatives$rum_derivatives_STAR_(get_spec_fn){
return new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"static-properties","static-properties",-577838503),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"childContextTypes","childContextTypes",578717991),context_types_39125], null),new cljs.core.Keyword(null,"init","init",-1875481434),(function (s,_){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(s,new cljs.core.Keyword("org.martinklepsch.derivatives","spec","org.martinklepsch.derivatives/spec",-1264887092),(function (){var G__39093 = new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s);
return (get_spec_fn.cljs$core$IFn$_invoke$arity$1 ? get_spec_fn.cljs$core$IFn$_invoke$arity$1(G__39093) : get_spec_fn.call(null,G__39093));
})());
}),new cljs.core.Keyword(null,"child-context","child-context",-1375270295),(function (s){
var pool = org.martinklepsch.derivatives.derivatives_pool(new cljs.core.Keyword("org.martinklepsch.derivatives","spec","org.martinklepsch.derivatives/spec",-1264887092).cljs$core$IFn$_invoke$arity$1(s));
return cljs.core.PersistentArrayMap.createAsIfByAssoc([release_k_39124,cljs.core.partial.cljs$core$IFn$_invoke$arity$2(org.martinklepsch.derivatives.release_BANG_,pool),get_k_39123,cljs.core.partial.cljs$core$IFn$_invoke$arity$2(org.martinklepsch.derivatives.get_BANG_,pool)]);
})], null);
});

/**
 * Rum mixin to retrieve derivatives for `drv-ks` using the functions in the component context
 *   To get the derived-atom use `get-ref` for swappable client/server behavior
 */
org.martinklepsch.derivatives.drv = (function org$martinklepsch$derivatives$drv(var_args){
var args__4742__auto__ = [];
var len__4736__auto___39126 = arguments.length;
var i__4737__auto___39127 = (0);
while(true){
if((i__4737__auto___39127 < len__4736__auto___39126)){
args__4742__auto__.push((arguments[i__4737__auto___39127]));

var G__39128 = (i__4737__auto___39127 + (1));
i__4737__auto___39127 = G__39128;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic = (function (drv_ks){
var token = cljs.core.rand_int((10000));
if(cljs.core.seq(drv_ks)){
} else {
throw (new Error(["Assert failed: ","The drv mixin needs at least one derivative ID","\n","(seq drv-ks)"].join('')));
}

return new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"static-properties","static-properties",-577838503),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"contextTypes","contextTypes",-2023853910),context_types_39125], null),new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
var get_drv_BANG_ = goog.object.get(goog.object.get(new cljs.core.Keyword("rum","react-component","rum/react-component",-1879897248).cljs$core$IFn$_invoke$arity$1(s),"context"),get_k_39123);
if(cljs.core.truth_(get_drv_BANG_)){
} else {
throw (new Error(["Assert failed: ","No get! derivative function found in component context","\n","get-drv!"].join('')));
}

return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (p1__39089_SHARP_,p2__39090_SHARP_){
return cljs.core.assoc_in(p1__39089_SHARP_,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("org.martinklepsch.derivatives","derivatives","org.martinklepsch.derivatives/derivatives",1339417050),p2__39090_SHARP_], null),(get_drv_BANG_.cljs$core$IFn$_invoke$arity$2 ? get_drv_BANG_.cljs$core$IFn$_invoke$arity$2(p2__39090_SHARP_,token) : get_drv_BANG_.call(null,p2__39090_SHARP_,token)));
}),s,drv_ks);
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
var release_drv_BANG_ = goog.object.get(goog.object.get(new cljs.core.Keyword("rum","react-component","rum/react-component",-1879897248).cljs$core$IFn$_invoke$arity$1(s),"context"),release_k_39124);
if(cljs.core.truth_(release_drv_BANG_)){
} else {
throw (new Error(["Assert failed: ","No release! derivative function found in component context","\n","release-drv!"].join('')));
}

return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (p1__39092_SHARP_,p2__39091_SHARP_){
(release_drv_BANG_.cljs$core$IFn$_invoke$arity$2 ? release_drv_BANG_.cljs$core$IFn$_invoke$arity$2(p2__39091_SHARP_,token) : release_drv_BANG_.call(null,p2__39091_SHARP_,token));

return cljs.core.update.cljs$core$IFn$_invoke$arity$4(p1__39092_SHARP_,new cljs.core.Keyword("org.martinklepsch.derivatives","derivatives","org.martinklepsch.derivatives/derivatives",1339417050),cljs.core.dissoc,p2__39091_SHARP_);
}),s,drv_ks);
})], null);
}));

(org.martinklepsch.derivatives.drv.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(org.martinklepsch.derivatives.drv.cljs$lang$applyTo = (function (seq39094){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq39094));
}));

org.martinklepsch.derivatives._STAR_derivatives_STAR_ = null;
/**
 * Get the derivative identified by `drv-k` from the component state.
 * When rendering in Clojure this looks for `drv-k` in the dynvar `*derivatives`
 */
org.martinklepsch.derivatives.get_ref = (function org$martinklepsch$derivatives$get_ref(state,drv_k){
var or__4126__auto__ = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(state,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("org.martinklepsch.derivatives","derivatives","org.martinklepsch.derivatives/derivatives",1339417050),drv_k], null));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
throw cljs.core.ex_info.cljs$core$IFn$_invoke$arity$2(["No derivative found! Maybe you forgot a (drv ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(drv_k),") mixin?"].join(''),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"key","key",-1516042587),drv_k,new cljs.core.Keyword(null,"derivatives","derivatives",-1346736355),cljs.core.keys(new cljs.core.Keyword("org.martinklepsch.derivatives","derivatives","org.martinklepsch.derivatives/derivatives",1339417050).cljs$core$IFn$_invoke$arity$1(state))], null));
}
});
/**
 * Like `get-ref` wrapped in `rum.core/react`
 */
org.martinklepsch.derivatives.react = (function org$martinklepsch$derivatives$react(state,drv_k){
return rum.core.react(org.martinklepsch.derivatives.get_ref(state,drv_k));
});
/**
 * React to multiple derivatives in the components state.
 * If any `ks` are passed, react to those and return their values
 * in a map. If no `ks` is passed return all available derivatives
 * deref'ed as a map.
 */
org.martinklepsch.derivatives.react_all = (function org$martinklepsch$derivatives$react_all(var_args){
var args__4742__auto__ = [];
var len__4736__auto___39131 = arguments.length;
var i__4737__auto___39132 = (0);
while(true){
if((i__4737__auto___39132 < len__4736__auto___39131)){
args__4742__auto__.push((arguments[i__4737__auto___39132]));

var G__39133 = (i__4737__auto___39132 + (1));
i__4737__auto___39132 = G__39133;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return org.martinklepsch.derivatives.react_all.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(org.martinklepsch.derivatives.react_all.cljs$core$IFn$_invoke$arity$variadic = (function (state,ks){
var ks__$1 = (function (){var or__4126__auto__ = cljs.core.seq(ks);
if(or__4126__auto__){
return or__4126__auto__;
} else {
return cljs.core.keys(new cljs.core.Keyword("org.martinklepsch.derivatives","derivatives","org.martinklepsch.derivatives/derivatives",1339417050).cljs$core$IFn$_invoke$arity$1(state));
}
})();
return cljs.core.zipmap(ks__$1,cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__39095_SHARP_){
return org.martinklepsch.derivatives.react(state,p1__39095_SHARP_);
}),ks__$1));
}));

(org.martinklepsch.derivatives.react_all.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(org.martinklepsch.derivatives.react_all.cljs$lang$applyTo = (function (seq39096){
var G__39097 = cljs.core.first(seq39096);
var seq39096__$1 = cljs.core.next(seq39096);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__39097,seq39096__$1);
}));


//# sourceMappingURL=org.martinklepsch.derivatives.js.map
