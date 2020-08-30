goog.provide('com.stuartsierra.dependency');

/**
 * @interface
 */
com.stuartsierra.dependency.DependencyGraph = function(){};

var com$stuartsierra$dependency$DependencyGraph$immediate_dependencies$dyn_38882 = (function (graph,node){
var x__4428__auto__ = (((graph == null))?null:graph);
var m__4429__auto__ = (com.stuartsierra.dependency.immediate_dependencies[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$2(graph,node) : m__4429__auto__.call(null,graph,node));
} else {
var m__4426__auto__ = (com.stuartsierra.dependency.immediate_dependencies["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$2(graph,node) : m__4426__auto__.call(null,graph,node));
} else {
throw cljs.core.missing_protocol("DependencyGraph.immediate-dependencies",graph);
}
}
});
/**
 * Returns the set of immediate dependencies of node.
 */
com.stuartsierra.dependency.immediate_dependencies = (function com$stuartsierra$dependency$immediate_dependencies(graph,node){
if((((!((graph == null)))) && ((!((graph.com$stuartsierra$dependency$DependencyGraph$immediate_dependencies$arity$2 == null)))))){
return graph.com$stuartsierra$dependency$DependencyGraph$immediate_dependencies$arity$2(graph,node);
} else {
return com$stuartsierra$dependency$DependencyGraph$immediate_dependencies$dyn_38882(graph,node);
}
});

var com$stuartsierra$dependency$DependencyGraph$immediate_dependents$dyn_38883 = (function (graph,node){
var x__4428__auto__ = (((graph == null))?null:graph);
var m__4429__auto__ = (com.stuartsierra.dependency.immediate_dependents[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$2(graph,node) : m__4429__auto__.call(null,graph,node));
} else {
var m__4426__auto__ = (com.stuartsierra.dependency.immediate_dependents["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$2(graph,node) : m__4426__auto__.call(null,graph,node));
} else {
throw cljs.core.missing_protocol("DependencyGraph.immediate-dependents",graph);
}
}
});
/**
 * Returns the set of immediate dependents of node.
 */
com.stuartsierra.dependency.immediate_dependents = (function com$stuartsierra$dependency$immediate_dependents(graph,node){
if((((!((graph == null)))) && ((!((graph.com$stuartsierra$dependency$DependencyGraph$immediate_dependents$arity$2 == null)))))){
return graph.com$stuartsierra$dependency$DependencyGraph$immediate_dependents$arity$2(graph,node);
} else {
return com$stuartsierra$dependency$DependencyGraph$immediate_dependents$dyn_38883(graph,node);
}
});

var com$stuartsierra$dependency$DependencyGraph$transitive_dependencies$dyn_38884 = (function (graph,node){
var x__4428__auto__ = (((graph == null))?null:graph);
var m__4429__auto__ = (com.stuartsierra.dependency.transitive_dependencies[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$2(graph,node) : m__4429__auto__.call(null,graph,node));
} else {
var m__4426__auto__ = (com.stuartsierra.dependency.transitive_dependencies["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$2(graph,node) : m__4426__auto__.call(null,graph,node));
} else {
throw cljs.core.missing_protocol("DependencyGraph.transitive-dependencies",graph);
}
}
});
/**
 * Returns the set of all things which node depends on, directly or
 *  transitively.
 */
com.stuartsierra.dependency.transitive_dependencies = (function com$stuartsierra$dependency$transitive_dependencies(graph,node){
if((((!((graph == null)))) && ((!((graph.com$stuartsierra$dependency$DependencyGraph$transitive_dependencies$arity$2 == null)))))){
return graph.com$stuartsierra$dependency$DependencyGraph$transitive_dependencies$arity$2(graph,node);
} else {
return com$stuartsierra$dependency$DependencyGraph$transitive_dependencies$dyn_38884(graph,node);
}
});

var com$stuartsierra$dependency$DependencyGraph$transitive_dependencies_set$dyn_38885 = (function (graph,node_set){
var x__4428__auto__ = (((graph == null))?null:graph);
var m__4429__auto__ = (com.stuartsierra.dependency.transitive_dependencies_set[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$2(graph,node_set) : m__4429__auto__.call(null,graph,node_set));
} else {
var m__4426__auto__ = (com.stuartsierra.dependency.transitive_dependencies_set["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$2(graph,node_set) : m__4426__auto__.call(null,graph,node_set));
} else {
throw cljs.core.missing_protocol("DependencyGraph.transitive-dependencies-set",graph);
}
}
});
/**
 * Returns the set of all things which any node in node-set depends
 *  on, directly or transitively.
 */
com.stuartsierra.dependency.transitive_dependencies_set = (function com$stuartsierra$dependency$transitive_dependencies_set(graph,node_set){
if((((!((graph == null)))) && ((!((graph.com$stuartsierra$dependency$DependencyGraph$transitive_dependencies_set$arity$2 == null)))))){
return graph.com$stuartsierra$dependency$DependencyGraph$transitive_dependencies_set$arity$2(graph,node_set);
} else {
return com$stuartsierra$dependency$DependencyGraph$transitive_dependencies_set$dyn_38885(graph,node_set);
}
});

var com$stuartsierra$dependency$DependencyGraph$transitive_dependents$dyn_38886 = (function (graph,node){
var x__4428__auto__ = (((graph == null))?null:graph);
var m__4429__auto__ = (com.stuartsierra.dependency.transitive_dependents[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$2(graph,node) : m__4429__auto__.call(null,graph,node));
} else {
var m__4426__auto__ = (com.stuartsierra.dependency.transitive_dependents["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$2(graph,node) : m__4426__auto__.call(null,graph,node));
} else {
throw cljs.core.missing_protocol("DependencyGraph.transitive-dependents",graph);
}
}
});
/**
 * Returns the set of all things which depend upon node, directly or
 *  transitively.
 */
com.stuartsierra.dependency.transitive_dependents = (function com$stuartsierra$dependency$transitive_dependents(graph,node){
if((((!((graph == null)))) && ((!((graph.com$stuartsierra$dependency$DependencyGraph$transitive_dependents$arity$2 == null)))))){
return graph.com$stuartsierra$dependency$DependencyGraph$transitive_dependents$arity$2(graph,node);
} else {
return com$stuartsierra$dependency$DependencyGraph$transitive_dependents$dyn_38886(graph,node);
}
});

var com$stuartsierra$dependency$DependencyGraph$transitive_dependents_set$dyn_38887 = (function (graph,node_set){
var x__4428__auto__ = (((graph == null))?null:graph);
var m__4429__auto__ = (com.stuartsierra.dependency.transitive_dependents_set[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$2(graph,node_set) : m__4429__auto__.call(null,graph,node_set));
} else {
var m__4426__auto__ = (com.stuartsierra.dependency.transitive_dependents_set["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$2(graph,node_set) : m__4426__auto__.call(null,graph,node_set));
} else {
throw cljs.core.missing_protocol("DependencyGraph.transitive-dependents-set",graph);
}
}
});
/**
 * Returns the set of all things which depend upon any node in
 *  node-set, directly or transitively.
 */
com.stuartsierra.dependency.transitive_dependents_set = (function com$stuartsierra$dependency$transitive_dependents_set(graph,node_set){
if((((!((graph == null)))) && ((!((graph.com$stuartsierra$dependency$DependencyGraph$transitive_dependents_set$arity$2 == null)))))){
return graph.com$stuartsierra$dependency$DependencyGraph$transitive_dependents_set$arity$2(graph,node_set);
} else {
return com$stuartsierra$dependency$DependencyGraph$transitive_dependents_set$dyn_38887(graph,node_set);
}
});

var com$stuartsierra$dependency$DependencyGraph$nodes$dyn_38888 = (function (graph){
var x__4428__auto__ = (((graph == null))?null:graph);
var m__4429__auto__ = (com.stuartsierra.dependency.nodes[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$1 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$1(graph) : m__4429__auto__.call(null,graph));
} else {
var m__4426__auto__ = (com.stuartsierra.dependency.nodes["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$1 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$1(graph) : m__4426__auto__.call(null,graph));
} else {
throw cljs.core.missing_protocol("DependencyGraph.nodes",graph);
}
}
});
/**
 * Returns the set of all nodes in graph.
 */
com.stuartsierra.dependency.nodes = (function com$stuartsierra$dependency$nodes(graph){
if((((!((graph == null)))) && ((!((graph.com$stuartsierra$dependency$DependencyGraph$nodes$arity$1 == null)))))){
return graph.com$stuartsierra$dependency$DependencyGraph$nodes$arity$1(graph);
} else {
return com$stuartsierra$dependency$DependencyGraph$nodes$dyn_38888(graph);
}
});


/**
 * @interface
 */
com.stuartsierra.dependency.DependencyGraphUpdate = function(){};

var com$stuartsierra$dependency$DependencyGraphUpdate$depend$dyn_38889 = (function (graph,node,dep){
var x__4428__auto__ = (((graph == null))?null:graph);
var m__4429__auto__ = (com.stuartsierra.dependency.depend[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$3 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$3(graph,node,dep) : m__4429__auto__.call(null,graph,node,dep));
} else {
var m__4426__auto__ = (com.stuartsierra.dependency.depend["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$3 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$3(graph,node,dep) : m__4426__auto__.call(null,graph,node,dep));
} else {
throw cljs.core.missing_protocol("DependencyGraphUpdate.depend",graph);
}
}
});
/**
 * Returns a new graph with a dependency from node to dep ("node depends
 *  on dep"). Forbids circular dependencies.
 */
com.stuartsierra.dependency.depend = (function com$stuartsierra$dependency$depend(graph,node,dep){
if((((!((graph == null)))) && ((!((graph.com$stuartsierra$dependency$DependencyGraphUpdate$depend$arity$3 == null)))))){
return graph.com$stuartsierra$dependency$DependencyGraphUpdate$depend$arity$3(graph,node,dep);
} else {
return com$stuartsierra$dependency$DependencyGraphUpdate$depend$dyn_38889(graph,node,dep);
}
});

var com$stuartsierra$dependency$DependencyGraphUpdate$remove_edge$dyn_38890 = (function (graph,node,dep){
var x__4428__auto__ = (((graph == null))?null:graph);
var m__4429__auto__ = (com.stuartsierra.dependency.remove_edge[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$3 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$3(graph,node,dep) : m__4429__auto__.call(null,graph,node,dep));
} else {
var m__4426__auto__ = (com.stuartsierra.dependency.remove_edge["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$3 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$3(graph,node,dep) : m__4426__auto__.call(null,graph,node,dep));
} else {
throw cljs.core.missing_protocol("DependencyGraphUpdate.remove-edge",graph);
}
}
});
/**
 * Returns a new graph with the dependency from node to dep removed.
 */
com.stuartsierra.dependency.remove_edge = (function com$stuartsierra$dependency$remove_edge(graph,node,dep){
if((((!((graph == null)))) && ((!((graph.com$stuartsierra$dependency$DependencyGraphUpdate$remove_edge$arity$3 == null)))))){
return graph.com$stuartsierra$dependency$DependencyGraphUpdate$remove_edge$arity$3(graph,node,dep);
} else {
return com$stuartsierra$dependency$DependencyGraphUpdate$remove_edge$dyn_38890(graph,node,dep);
}
});

var com$stuartsierra$dependency$DependencyGraphUpdate$remove_all$dyn_38893 = (function (graph,node){
var x__4428__auto__ = (((graph == null))?null:graph);
var m__4429__auto__ = (com.stuartsierra.dependency.remove_all[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$2(graph,node) : m__4429__auto__.call(null,graph,node));
} else {
var m__4426__auto__ = (com.stuartsierra.dependency.remove_all["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$2(graph,node) : m__4426__auto__.call(null,graph,node));
} else {
throw cljs.core.missing_protocol("DependencyGraphUpdate.remove-all",graph);
}
}
});
/**
 * Returns a new dependency graph with all references to node removed.
 */
com.stuartsierra.dependency.remove_all = (function com$stuartsierra$dependency$remove_all(graph,node){
if((((!((graph == null)))) && ((!((graph.com$stuartsierra$dependency$DependencyGraphUpdate$remove_all$arity$2 == null)))))){
return graph.com$stuartsierra$dependency$DependencyGraphUpdate$remove_all$arity$2(graph,node);
} else {
return com$stuartsierra$dependency$DependencyGraphUpdate$remove_all$dyn_38893(graph,node);
}
});

var com$stuartsierra$dependency$DependencyGraphUpdate$remove_node$dyn_38894 = (function (graph,node){
var x__4428__auto__ = (((graph == null))?null:graph);
var m__4429__auto__ = (com.stuartsierra.dependency.remove_node[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$2(graph,node) : m__4429__auto__.call(null,graph,node));
} else {
var m__4426__auto__ = (com.stuartsierra.dependency.remove_node["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$2 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$2(graph,node) : m__4426__auto__.call(null,graph,node));
} else {
throw cljs.core.missing_protocol("DependencyGraphUpdate.remove-node",graph);
}
}
});
/**
 * Removes the node from the dependency graph without removing it as a
 *  dependency of other nodes. That is, removes all outgoing edges from
 *  node.
 */
com.stuartsierra.dependency.remove_node = (function com$stuartsierra$dependency$remove_node(graph,node){
if((((!((graph == null)))) && ((!((graph.com$stuartsierra$dependency$DependencyGraphUpdate$remove_node$arity$2 == null)))))){
return graph.com$stuartsierra$dependency$DependencyGraphUpdate$remove_node$arity$2(graph,node);
} else {
return com$stuartsierra$dependency$DependencyGraphUpdate$remove_node$dyn_38894(graph,node);
}
});

com.stuartsierra.dependency.remove_from_map = (function com$stuartsierra$dependency$remove_from_map(amap,x){
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (m,p__38778){
var vec__38779 = p__38778;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38779,(0),null);
var vs = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38779,(1),null);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(m,k,cljs.core.disj.cljs$core$IFn$_invoke$arity$2(vs,x));
}),cljs.core.PersistentArrayMap.EMPTY,cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(amap,x));
});
/**
 * Recursively expands the set of dependency relationships starting
 *   at (get neighbors x), for each x in node-set
 */
com.stuartsierra.dependency.transitive = (function com$stuartsierra$dependency$transitive(neighbors,node_set){
var unexpanded = cljs.core.mapcat.cljs$core$IFn$_invoke$arity$variadic(neighbors,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([node_set], 0));
var expanded = cljs.core.PersistentHashSet.EMPTY;
while(true){
var temp__5733__auto__ = cljs.core.seq(unexpanded);
if(temp__5733__auto__){
var vec__38787 = temp__5733__auto__;
var seq__38789 = cljs.core.seq(vec__38787);
var first__38790 = cljs.core.first(seq__38789);
var seq__38789__$1 = cljs.core.next(seq__38789);
var node = first__38790;
var more = seq__38789__$1;
if(cljs.core.contains_QMARK_(expanded,node)){
var G__38895 = more;
var G__38896 = expanded;
unexpanded = G__38895;
expanded = G__38896;
continue;
} else {
var G__38897 = cljs.core.concat.cljs$core$IFn$_invoke$arity$2(more,(neighbors.cljs$core$IFn$_invoke$arity$1 ? neighbors.cljs$core$IFn$_invoke$arity$1(node) : neighbors.call(null,node)));
var G__38898 = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(expanded,node);
unexpanded = G__38897;
expanded = G__38898;
continue;
}
} else {
return expanded;
}
break;
}
});
com.stuartsierra.dependency.set_conj = cljs.core.fnil.cljs$core$IFn$_invoke$arity$2(cljs.core.conj,cljs.core.PersistentHashSet.EMPTY);

/**
* @constructor
 * @implements {cljs.core.IRecord}
 * @implements {cljs.core.IKVReduce}
 * @implements {cljs.core.IEquiv}
 * @implements {cljs.core.IHash}
 * @implements {cljs.core.ICollection}
 * @implements {cljs.core.ICounted}
 * @implements {com.stuartsierra.dependency.DependencyGraph}
 * @implements {cljs.core.ISeqable}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.ICloneable}
 * @implements {com.stuartsierra.dependency.DependencyGraphUpdate}
 * @implements {cljs.core.IPrintWithWriter}
 * @implements {cljs.core.IIterable}
 * @implements {cljs.core.IWithMeta}
 * @implements {cljs.core.IAssociative}
 * @implements {cljs.core.IMap}
 * @implements {cljs.core.ILookup}
*/
com.stuartsierra.dependency.MapDependencyGraph = (function (dependencies,dependents,__meta,__extmap,__hash){
this.dependencies = dependencies;
this.dependents = dependents;
this.__meta = __meta;
this.__extmap = __extmap;
this.__hash = __hash;
this.cljs$lang$protocol_mask$partition0$ = 2230716170;
this.cljs$lang$protocol_mask$partition1$ = 139264;
});
(com.stuartsierra.dependency.MapDependencyGraph.prototype.cljs$core$ILookup$_lookup$arity$2 = (function (this__4380__auto__,k__4381__auto__){
var self__ = this;
var this__4380__auto____$1 = this;
return this__4380__auto____$1.cljs$core$ILookup$_lookup$arity$3(null,k__4381__auto__,null);
}));

(com.stuartsierra.dependency.MapDependencyGraph.prototype.cljs$core$ILookup$_lookup$arity$3 = (function (this__4382__auto__,k38806,else__4383__auto__){
var self__ = this;
var this__4382__auto____$1 = this;
var G__38813 = k38806;
var G__38813__$1 = (((G__38813 instanceof cljs.core.Keyword))?G__38813.fqn:null);
switch (G__38813__$1) {
case "dependencies":
return self__.dependencies;

break;
case "dependents":
return self__.dependents;

break;
default:
return cljs.core.get.cljs$core$IFn$_invoke$arity$3(self__.__extmap,k38806,else__4383__auto__);

}
}));

(com.stuartsierra.dependency.MapDependencyGraph.prototype.cljs$core$IKVReduce$_kv_reduce$arity$3 = (function (this__4399__auto__,f__4400__auto__,init__4401__auto__){
var self__ = this;
var this__4399__auto____$1 = this;
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (ret__4402__auto__,p__38814){
var vec__38815 = p__38814;
var k__4403__auto__ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38815,(0),null);
var v__4404__auto__ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38815,(1),null);
return (f__4400__auto__.cljs$core$IFn$_invoke$arity$3 ? f__4400__auto__.cljs$core$IFn$_invoke$arity$3(ret__4402__auto__,k__4403__auto__,v__4404__auto__) : f__4400__auto__.call(null,ret__4402__auto__,k__4403__auto__,v__4404__auto__));
}),init__4401__auto__,this__4399__auto____$1);
}));

(com.stuartsierra.dependency.MapDependencyGraph.prototype.cljs$core$IPrintWithWriter$_pr_writer$arity$3 = (function (this__4394__auto__,writer__4395__auto__,opts__4396__auto__){
var self__ = this;
var this__4394__auto____$1 = this;
var pr_pair__4397__auto__ = (function (keyval__4398__auto__){
return cljs.core.pr_sequential_writer(writer__4395__auto__,cljs.core.pr_writer,""," ","",opts__4396__auto__,keyval__4398__auto__);
});
return cljs.core.pr_sequential_writer(writer__4395__auto__,pr_pair__4397__auto__,"#com.stuartsierra.dependency.MapDependencyGraph{",", ","}",opts__4396__auto__,cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(new cljs.core.PersistentVector(null,2,(5),cljs.core.PersistentVector.EMPTY_NODE,[new cljs.core.Keyword(null,"dependencies","dependencies",1108064605),self__.dependencies],null)),(new cljs.core.PersistentVector(null,2,(5),cljs.core.PersistentVector.EMPTY_NODE,[new cljs.core.Keyword(null,"dependents","dependents",136812837),self__.dependents],null))], null),self__.__extmap));
}));

(com.stuartsierra.dependency.MapDependencyGraph.prototype.cljs$core$IIterable$_iterator$arity$1 = (function (G__38805){
var self__ = this;
var G__38805__$1 = this;
return (new cljs.core.RecordIter((0),G__38805__$1,2,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"dependencies","dependencies",1108064605),new cljs.core.Keyword(null,"dependents","dependents",136812837)], null),(cljs.core.truth_(self__.__extmap)?cljs.core._iterator(self__.__extmap):cljs.core.nil_iter())));
}));

(com.stuartsierra.dependency.MapDependencyGraph.prototype.cljs$core$IMeta$_meta$arity$1 = (function (this__4378__auto__){
var self__ = this;
var this__4378__auto____$1 = this;
return self__.__meta;
}));

(com.stuartsierra.dependency.MapDependencyGraph.prototype.cljs$core$ICloneable$_clone$arity$1 = (function (this__4375__auto__){
var self__ = this;
var this__4375__auto____$1 = this;
return (new com.stuartsierra.dependency.MapDependencyGraph(self__.dependencies,self__.dependents,self__.__meta,self__.__extmap,self__.__hash));
}));

(com.stuartsierra.dependency.MapDependencyGraph.prototype.cljs$core$ICounted$_count$arity$1 = (function (this__4384__auto__){
var self__ = this;
var this__4384__auto____$1 = this;
return (2 + cljs.core.count(self__.__extmap));
}));

(com.stuartsierra.dependency.MapDependencyGraph.prototype.cljs$core$IHash$_hash$arity$1 = (function (this__4376__auto__){
var self__ = this;
var this__4376__auto____$1 = this;
var h__4238__auto__ = self__.__hash;
if((!((h__4238__auto__ == null)))){
return h__4238__auto__;
} else {
var h__4238__auto____$1 = (function (coll__4377__auto__){
return (1526449397 ^ cljs.core.hash_unordered_coll(coll__4377__auto__));
})(this__4376__auto____$1);
(self__.__hash = h__4238__auto____$1);

return h__4238__auto____$1;
}
}));

(com.stuartsierra.dependency.MapDependencyGraph.prototype.cljs$core$IEquiv$_equiv$arity$2 = (function (this38807,other38808){
var self__ = this;
var this38807__$1 = this;
return (((!((other38808 == null)))) && ((this38807__$1.constructor === other38808.constructor)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this38807__$1.dependencies,other38808.dependencies)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this38807__$1.dependents,other38808.dependents)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this38807__$1.__extmap,other38808.__extmap)));
}));

(com.stuartsierra.dependency.MapDependencyGraph.prototype.cljs$core$IMap$_dissoc$arity$2 = (function (this__4389__auto__,k__4390__auto__){
var self__ = this;
var this__4389__auto____$1 = this;
if(cljs.core.contains_QMARK_(new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"dependents","dependents",136812837),null,new cljs.core.Keyword(null,"dependencies","dependencies",1108064605),null], null), null),k__4390__auto__)){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core._with_meta(cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,this__4389__auto____$1),self__.__meta),k__4390__auto__);
} else {
return (new com.stuartsierra.dependency.MapDependencyGraph(self__.dependencies,self__.dependents,self__.__meta,cljs.core.not_empty(cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(self__.__extmap,k__4390__auto__)),null));
}
}));

(com.stuartsierra.dependency.MapDependencyGraph.prototype.cljs$core$IAssociative$_assoc$arity$3 = (function (this__4387__auto__,k__4388__auto__,G__38805){
var self__ = this;
var this__4387__auto____$1 = this;
var pred__38824 = cljs.core.keyword_identical_QMARK_;
var expr__38825 = k__4388__auto__;
if(cljs.core.truth_((pred__38824.cljs$core$IFn$_invoke$arity$2 ? pred__38824.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"dependencies","dependencies",1108064605),expr__38825) : pred__38824.call(null,new cljs.core.Keyword(null,"dependencies","dependencies",1108064605),expr__38825)))){
return (new com.stuartsierra.dependency.MapDependencyGraph(G__38805,self__.dependents,self__.__meta,self__.__extmap,null));
} else {
if(cljs.core.truth_((pred__38824.cljs$core$IFn$_invoke$arity$2 ? pred__38824.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"dependents","dependents",136812837),expr__38825) : pred__38824.call(null,new cljs.core.Keyword(null,"dependents","dependents",136812837),expr__38825)))){
return (new com.stuartsierra.dependency.MapDependencyGraph(self__.dependencies,G__38805,self__.__meta,self__.__extmap,null));
} else {
return (new com.stuartsierra.dependency.MapDependencyGraph(self__.dependencies,self__.dependents,self__.__meta,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(self__.__extmap,k__4388__auto__,G__38805),null));
}
}
}));

(com.stuartsierra.dependency.MapDependencyGraph.prototype.cljs$core$ISeqable$_seq$arity$1 = (function (this__4392__auto__){
var self__ = this;
var this__4392__auto____$1 = this;
return cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(new cljs.core.MapEntry(new cljs.core.Keyword(null,"dependencies","dependencies",1108064605),self__.dependencies,null)),(new cljs.core.MapEntry(new cljs.core.Keyword(null,"dependents","dependents",136812837),self__.dependents,null))], null),self__.__extmap));
}));

(com.stuartsierra.dependency.MapDependencyGraph.prototype.com$stuartsierra$dependency$DependencyGraph$ = cljs.core.PROTOCOL_SENTINEL);

(com.stuartsierra.dependency.MapDependencyGraph.prototype.com$stuartsierra$dependency$DependencyGraph$immediate_dependencies$arity$2 = (function (graph,node){
var self__ = this;
var graph__$1 = this;
return cljs.core.get.cljs$core$IFn$_invoke$arity$3(self__.dependencies,node,cljs.core.PersistentHashSet.EMPTY);
}));

(com.stuartsierra.dependency.MapDependencyGraph.prototype.com$stuartsierra$dependency$DependencyGraph$immediate_dependents$arity$2 = (function (graph,node){
var self__ = this;
var graph__$1 = this;
return cljs.core.get.cljs$core$IFn$_invoke$arity$3(self__.dependents,node,cljs.core.PersistentHashSet.EMPTY);
}));

(com.stuartsierra.dependency.MapDependencyGraph.prototype.com$stuartsierra$dependency$DependencyGraph$transitive_dependencies$arity$2 = (function (graph,node){
var self__ = this;
var graph__$1 = this;
return com.stuartsierra.dependency.transitive(self__.dependencies,cljs.core.PersistentHashSet.createAsIfByAssoc([node]));
}));

(com.stuartsierra.dependency.MapDependencyGraph.prototype.com$stuartsierra$dependency$DependencyGraph$transitive_dependencies_set$arity$2 = (function (graph,node_set){
var self__ = this;
var graph__$1 = this;
return com.stuartsierra.dependency.transitive(self__.dependencies,node_set);
}));

(com.stuartsierra.dependency.MapDependencyGraph.prototype.com$stuartsierra$dependency$DependencyGraph$transitive_dependents$arity$2 = (function (graph,node){
var self__ = this;
var graph__$1 = this;
return com.stuartsierra.dependency.transitive(self__.dependents,cljs.core.PersistentHashSet.createAsIfByAssoc([node]));
}));

(com.stuartsierra.dependency.MapDependencyGraph.prototype.com$stuartsierra$dependency$DependencyGraph$transitive_dependents_set$arity$2 = (function (graph,node_set){
var self__ = this;
var graph__$1 = this;
return com.stuartsierra.dependency.transitive(self__.dependents,node_set);
}));

(com.stuartsierra.dependency.MapDependencyGraph.prototype.com$stuartsierra$dependency$DependencyGraph$nodes$arity$1 = (function (graph){
var self__ = this;
var graph__$1 = this;
return clojure.set.union.cljs$core$IFn$_invoke$arity$2(cljs.core.set(cljs.core.keys(self__.dependencies)),cljs.core.set(cljs.core.keys(self__.dependents)));
}));

(com.stuartsierra.dependency.MapDependencyGraph.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (this__4379__auto__,G__38805){
var self__ = this;
var this__4379__auto____$1 = this;
return (new com.stuartsierra.dependency.MapDependencyGraph(self__.dependencies,self__.dependents,G__38805,self__.__extmap,self__.__hash));
}));

(com.stuartsierra.dependency.MapDependencyGraph.prototype.cljs$core$ICollection$_conj$arity$2 = (function (this__4385__auto__,entry__4386__auto__){
var self__ = this;
var this__4385__auto____$1 = this;
if(cljs.core.vector_QMARK_(entry__4386__auto__)){
return this__4385__auto____$1.cljs$core$IAssociative$_assoc$arity$3(null,cljs.core._nth(entry__4386__auto__,(0)),cljs.core._nth(entry__4386__auto__,(1)));
} else {
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3(cljs.core._conj,this__4385__auto____$1,entry__4386__auto__);
}
}));

(com.stuartsierra.dependency.MapDependencyGraph.prototype.com$stuartsierra$dependency$DependencyGraphUpdate$ = cljs.core.PROTOCOL_SENTINEL);

(com.stuartsierra.dependency.MapDependencyGraph.prototype.com$stuartsierra$dependency$DependencyGraphUpdate$depend$arity$3 = (function (graph,node,dep){
var self__ = this;
var graph__$1 = this;
if(cljs.core.truth_((function (){var or__4126__auto__ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(node,dep);
if(or__4126__auto__){
return or__4126__auto__;
} else {
return (com.stuartsierra.dependency.depends_QMARK_.cljs$core$IFn$_invoke$arity$3 ? com.stuartsierra.dependency.depends_QMARK_.cljs$core$IFn$_invoke$arity$3(graph__$1,dep,node) : com.stuartsierra.dependency.depends_QMARK_.call(null,graph__$1,dep,node));
}
})())){
throw cljs.core.ex_info.cljs$core$IFn$_invoke$arity$2(["Circular dependency between ",cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([node], 0))," and ",cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([dep], 0))].join(''),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"reason","reason",-2070751759),new cljs.core.Keyword("com.stuartsierra.dependency","circular-dependency","com.stuartsierra.dependency/circular-dependency",1926538656),new cljs.core.Keyword(null,"node","node",581201198),node,new cljs.core.Keyword(null,"dependency","dependency",1300131203),dep], null));
} else {
}

return (new com.stuartsierra.dependency.MapDependencyGraph(cljs.core.update_in.cljs$core$IFn$_invoke$arity$4(self__.dependencies,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [node], null),com.stuartsierra.dependency.set_conj,dep),cljs.core.update_in.cljs$core$IFn$_invoke$arity$4(self__.dependents,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [dep], null),com.stuartsierra.dependency.set_conj,node),null,null,null));
}));

(com.stuartsierra.dependency.MapDependencyGraph.prototype.com$stuartsierra$dependency$DependencyGraphUpdate$remove_edge$arity$3 = (function (graph,node,dep){
var self__ = this;
var graph__$1 = this;
return (new com.stuartsierra.dependency.MapDependencyGraph(cljs.core.update_in.cljs$core$IFn$_invoke$arity$4(self__.dependencies,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [node], null),cljs.core.disj,dep),cljs.core.update_in.cljs$core$IFn$_invoke$arity$4(self__.dependents,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [dep], null),cljs.core.disj,node),null,null,null));
}));

(com.stuartsierra.dependency.MapDependencyGraph.prototype.com$stuartsierra$dependency$DependencyGraphUpdate$remove_all$arity$2 = (function (graph,node){
var self__ = this;
var graph__$1 = this;
return (new com.stuartsierra.dependency.MapDependencyGraph(com.stuartsierra.dependency.remove_from_map(self__.dependencies,node),com.stuartsierra.dependency.remove_from_map(self__.dependents,node),null,null,null));
}));

(com.stuartsierra.dependency.MapDependencyGraph.prototype.com$stuartsierra$dependency$DependencyGraphUpdate$remove_node$arity$2 = (function (graph,node){
var self__ = this;
var graph__$1 = this;
return (new com.stuartsierra.dependency.MapDependencyGraph(cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(self__.dependencies,node),self__.dependents,null,null,null));
}));

(com.stuartsierra.dependency.MapDependencyGraph.getBasis = (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"dependencies","dependencies",-1546371164,null),new cljs.core.Symbol(null,"dependents","dependents",1777344364,null)], null);
}));

(com.stuartsierra.dependency.MapDependencyGraph.cljs$lang$type = true);

(com.stuartsierra.dependency.MapDependencyGraph.cljs$lang$ctorPrSeq = (function (this__4423__auto__){
return (new cljs.core.List(null,"com.stuartsierra.dependency/MapDependencyGraph",null,(1),null));
}));

(com.stuartsierra.dependency.MapDependencyGraph.cljs$lang$ctorPrWriter = (function (this__4423__auto__,writer__4424__auto__){
return cljs.core._write(writer__4424__auto__,"com.stuartsierra.dependency/MapDependencyGraph");
}));

/**
 * Positional factory function for com.stuartsierra.dependency/MapDependencyGraph.
 */
com.stuartsierra.dependency.__GT_MapDependencyGraph = (function com$stuartsierra$dependency$__GT_MapDependencyGraph(dependencies,dependents){
return (new com.stuartsierra.dependency.MapDependencyGraph(dependencies,dependents,null,null,null));
});

/**
 * Factory function for com.stuartsierra.dependency/MapDependencyGraph, taking a map of keywords to field values.
 */
com.stuartsierra.dependency.map__GT_MapDependencyGraph = (function com$stuartsierra$dependency$map__GT_MapDependencyGraph(G__38812){
var extmap__4419__auto__ = (function (){var G__38847 = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(G__38812,new cljs.core.Keyword(null,"dependencies","dependencies",1108064605),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"dependents","dependents",136812837)], 0));
if(cljs.core.record_QMARK_(G__38812)){
return cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,G__38847);
} else {
return G__38847;
}
})();
return (new com.stuartsierra.dependency.MapDependencyGraph(new cljs.core.Keyword(null,"dependencies","dependencies",1108064605).cljs$core$IFn$_invoke$arity$1(G__38812),new cljs.core.Keyword(null,"dependents","dependents",136812837).cljs$core$IFn$_invoke$arity$1(G__38812),null,cljs.core.not_empty(extmap__4419__auto__),null));
});

/**
 * Returns a new, empty, dependency graph.
 */
com.stuartsierra.dependency.graph = (function com$stuartsierra$dependency$graph(){
return com.stuartsierra.dependency.__GT_MapDependencyGraph(cljs.core.PersistentArrayMap.EMPTY,cljs.core.PersistentArrayMap.EMPTY);
});
/**
 * True if x is directly or transitively dependent on y.
 */
com.stuartsierra.dependency.depends_QMARK_ = (function com$stuartsierra$dependency$depends_QMARK_(graph,x,y){
return cljs.core.contains_QMARK_(com.stuartsierra.dependency.transitive_dependencies(graph,x),y);
});
/**
 * True if y is a dependent of x.
 */
com.stuartsierra.dependency.dependent_QMARK_ = (function com$stuartsierra$dependency$dependent_QMARK_(graph,x,y){
return cljs.core.contains_QMARK_(com.stuartsierra.dependency.transitive_dependents(graph,x),y);
});
/**
 * Returns a topologically-sorted list of nodes in graph.
 */
com.stuartsierra.dependency.topo_sort = (function com$stuartsierra$dependency$topo_sort(graph){
var sorted = cljs.core.List.EMPTY;
var g = graph;
var todo = cljs.core.set(cljs.core.filter.cljs$core$IFn$_invoke$arity$2(((function (sorted,g){
return (function (p1__38848_SHARP_){
return cljs.core.empty_QMARK_(com.stuartsierra.dependency.immediate_dependents(graph,p1__38848_SHARP_));
});})(sorted,g))
,com.stuartsierra.dependency.nodes(graph)));
while(true){
if(cljs.core.empty_QMARK_(todo)){
return sorted;
} else {
var vec__38876 = cljs.core.seq(todo);
var seq__38877 = cljs.core.seq(vec__38876);
var first__38878 = cljs.core.first(seq__38877);
var seq__38877__$1 = cljs.core.next(seq__38877);
var node = first__38878;
var more = seq__38877__$1;
var deps = com.stuartsierra.dependency.immediate_dependencies(g,node);
var vec__38879 = (function (){var deps__$1 = deps;
var g__$1 = g;
var add = cljs.core.PersistentHashSet.EMPTY;
while(true){
if(cljs.core.seq(deps__$1)){
var d = cljs.core.first(deps__$1);
var g_SINGLEQUOTE_ = com.stuartsierra.dependency.remove_edge(g__$1,node,d);
if(cljs.core.empty_QMARK_(com.stuartsierra.dependency.immediate_dependents(g_SINGLEQUOTE_,d))){
var G__38913 = cljs.core.rest(deps__$1);
var G__38914 = g_SINGLEQUOTE_;
var G__38915 = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(add,d);
deps__$1 = G__38913;
g__$1 = G__38914;
add = G__38915;
continue;
} else {
var G__38916 = cljs.core.rest(deps__$1);
var G__38917 = g_SINGLEQUOTE_;
var G__38918 = add;
deps__$1 = G__38916;
g__$1 = G__38917;
add = G__38918;
continue;
}
} else {
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [add,g__$1], null);
}
break;
}
})();
var add = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38879,(0),null);
var g_SINGLEQUOTE_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38879,(1),null);
var G__38919 = cljs.core.cons(node,sorted);
var G__38920 = com.stuartsierra.dependency.remove_node(g_SINGLEQUOTE_,node);
var G__38921 = clojure.set.union.cljs$core$IFn$_invoke$arity$2(cljs.core.set(more),cljs.core.set(add));
sorted = G__38919;
g = G__38920;
todo = G__38921;
continue;
}
break;
}
});
com.stuartsierra.dependency.max_number = Number.MAX_VALUE;
/**
 * Returns a comparator fn which produces a topological sort based on
 *   the dependencies in graph. Nodes not present in the graph will sort
 *   after nodes in the graph.
 */
com.stuartsierra.dependency.topo_comparator = (function com$stuartsierra$dependency$topo_comparator(graph){
var pos = cljs.core.zipmap(com.stuartsierra.dependency.topo_sort(graph),cljs.core.range.cljs$core$IFn$_invoke$arity$0());
return (function (a,b){
return cljs.core.compare(cljs.core.get.cljs$core$IFn$_invoke$arity$3(pos,a,com.stuartsierra.dependency.max_number),cljs.core.get.cljs$core$IFn$_invoke$arity$3(pos,b,com.stuartsierra.dependency.max_number));
});
});

//# sourceMappingURL=com.stuartsierra.dependency.js.map
