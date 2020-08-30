goog.provide('org.martinklepsch.derived');

/**
 * @interface
 */
org.martinklepsch.derived.IDisposable = function(){};

var org$martinklepsch$derived$IDisposable$dispose_BANG_$dyn_38822 = (function (this$){
var x__4428__auto__ = (((this$ == null))?null:this$);
var m__4429__auto__ = (org.martinklepsch.derived.dispose_BANG_[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$1 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__4429__auto__.call(null,this$));
} else {
var m__4426__auto__ = (org.martinklepsch.derived.dispose_BANG_["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$1 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__4426__auto__.call(null,this$));
} else {
throw cljs.core.missing_protocol("IDisposable.dispose!",this$);
}
}
});
org.martinklepsch.derived.dispose_BANG_ = (function org$martinklepsch$derived$dispose_BANG_(this$){
if((((!((this$ == null)))) && ((!((this$.org$martinklepsch$derived$IDisposable$dispose_BANG_$arity$1 == null)))))){
return this$.org$martinklepsch$derived$IDisposable$dispose_BANG_$arity$1(this$);
} else {
return org$martinklepsch$derived$IDisposable$dispose_BANG_$dyn_38822(this$);
}
});


/**
* @constructor
 * @implements {org.martinklepsch.derived.IDisposable}
 * @implements {cljs.core.IWatchable}
 * @implements {cljs.core.IDeref}
*/
org.martinklepsch.derived.DerivedValue = (function (sink,sources,key){
this.sink = sink;
this.sources = sources;
this.key = key;
this.cljs$lang$protocol_mask$partition0$ = 32768;
this.cljs$lang$protocol_mask$partition1$ = 2;
});
(org.martinklepsch.derived.DerivedValue.prototype.cljs$core$IDeref$_deref$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.deref(self__.sink);
}));

(org.martinklepsch.derived.DerivedValue.prototype.cljs$core$IWatchable$_add_watch$arity$3 = (function (self,key__$1,cb){
var self__ = this;
var self__$1 = this;
return cljs.core.add_watch(self__.sink,key__$1,cb);
}));

(org.martinklepsch.derived.DerivedValue.prototype.cljs$core$IWatchable$_remove_watch$arity$2 = (function (_,key__$1){
var self__ = this;
var ___$1 = this;
return cljs.core.remove_watch(self__.sink,key__$1);
}));

(org.martinklepsch.derived.DerivedValue.prototype.org$martinklepsch$derived$IDisposable$ = cljs.core.PROTOCOL_SENTINEL);

(org.martinklepsch.derived.DerivedValue.prototype.org$martinklepsch$derived$IDisposable$dispose_BANG_$arity$1 = (function (this$){
var self__ = this;
var this$__$1 = this;
var seq__38770 = cljs.core.seq(self__.sources);
var chunk__38771 = null;
var count__38772 = (0);
var i__38773 = (0);
while(true){
if((i__38773 < count__38772)){
var s = chunk__38771.cljs$core$IIndexed$_nth$arity$2(null,i__38773);
cljs.core.remove_watch(s,self__.key);


var G__38833 = seq__38770;
var G__38834 = chunk__38771;
var G__38835 = count__38772;
var G__38836 = (i__38773 + (1));
seq__38770 = G__38833;
chunk__38771 = G__38834;
count__38772 = G__38835;
i__38773 = G__38836;
continue;
} else {
var temp__5735__auto__ = cljs.core.seq(seq__38770);
if(temp__5735__auto__){
var seq__38770__$1 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__38770__$1)){
var c__4556__auto__ = cljs.core.chunk_first(seq__38770__$1);
var G__38837 = cljs.core.chunk_rest(seq__38770__$1);
var G__38838 = c__4556__auto__;
var G__38839 = cljs.core.count(c__4556__auto__);
var G__38840 = (0);
seq__38770 = G__38837;
chunk__38771 = G__38838;
count__38772 = G__38839;
i__38773 = G__38840;
continue;
} else {
var s = cljs.core.first(seq__38770__$1);
cljs.core.remove_watch(s,self__.key);


var G__38841 = cljs.core.next(seq__38770__$1);
var G__38842 = null;
var G__38843 = (0);
var G__38844 = (0);
seq__38770 = G__38841;
chunk__38771 = G__38842;
count__38772 = G__38843;
i__38773 = G__38844;
continue;
}
} else {
return null;
}
}
break;
}
}));

(org.martinklepsch.derived.DerivedValue.getBasis = (function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"sink","sink",1827367460,null),new cljs.core.Symbol(null,"sources","sources",1319365103,null),new cljs.core.Symbol(null,"key","key",124488940,null)], null);
}));

(org.martinklepsch.derived.DerivedValue.cljs$lang$type = true);

(org.martinklepsch.derived.DerivedValue.cljs$lang$ctorStr = "org.martinklepsch.derived/DerivedValue");

(org.martinklepsch.derived.DerivedValue.cljs$lang$ctorPrWriter = (function (this__4369__auto__,writer__4370__auto__,opt__4371__auto__){
return cljs.core._write(writer__4370__auto__,"org.martinklepsch.derived/DerivedValue");
}));

/**
 * Positional factory function for org.martinklepsch.derived/DerivedValue.
 */
org.martinklepsch.derived.__GT_DerivedValue = (function org$martinklepsch$derived$__GT_DerivedValue(sink,sources,key){
return (new org.martinklepsch.derived.DerivedValue(sink,sources,key));
});

org.martinklepsch.derived.derived_value = (function org$martinklepsch$derived$derived_value(var_args){
var G__38783 = arguments.length;
switch (G__38783) {
case 3:
return org.martinklepsch.derived.derived_value.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 4:
return org.martinklepsch.derived.derived_value.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(org.martinklepsch.derived.derived_value.cljs$core$IFn$_invoke$arity$3 = (function (refs,key,f){
return org.martinklepsch.derived.derived_value.cljs$core$IFn$_invoke$arity$4(refs,key,f,cljs.core.PersistentArrayMap.EMPTY);
}));

(org.martinklepsch.derived.derived_value.cljs$core$IFn$_invoke$arity$4 = (function (refs,key,f,opts){
var map__38788 = opts;
var map__38788__$1 = (((((!((map__38788 == null))))?(((((map__38788.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38788.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38788):map__38788);
var check_equals_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__38788__$1,new cljs.core.Keyword(null,"check-equals?","check-equals?",-2005755315),true);
var recalc = (function (){var G__38792 = cljs.core.count(refs);
switch (G__38792) {
case (1):
var vec__38793 = refs;
var a = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38793,(0),null);
return (function (){
var G__38796 = cljs.core.deref(a);
return (f.cljs$core$IFn$_invoke$arity$1 ? f.cljs$core$IFn$_invoke$arity$1(G__38796) : f.call(null,G__38796));
});

break;
case (2):
var vec__38797 = refs;
var a = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38797,(0),null);
var b = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38797,(1),null);
return (function (){
var G__38800 = cljs.core.deref(a);
var G__38801 = cljs.core.deref(b);
return (f.cljs$core$IFn$_invoke$arity$2 ? f.cljs$core$IFn$_invoke$arity$2(G__38800,G__38801) : f.call(null,G__38800,G__38801));
});

break;
case (3):
var vec__38802 = refs;
var a = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38802,(0),null);
var b = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38802,(1),null);
var c = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38802,(2),null);
return (function (){
var G__38809 = cljs.core.deref(a);
var G__38810 = cljs.core.deref(b);
var G__38811 = cljs.core.deref(c);
return (f.cljs$core$IFn$_invoke$arity$3 ? f.cljs$core$IFn$_invoke$arity$3(G__38809,G__38810,G__38811) : f.call(null,G__38809,G__38810,G__38811));
});

break;
default:
return (function (){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(f,cljs.core.map.cljs$core$IFn$_invoke$arity$2(cljs.core.deref,refs));
});

}
})();
var sink = cljs.core.atom.cljs$core$IFn$_invoke$arity$1((recalc.cljs$core$IFn$_invoke$arity$0 ? recalc.cljs$core$IFn$_invoke$arity$0() : recalc.call(null)));
var watch = (cljs.core.truth_(check_equals_QMARK_)?(function (_,___$1,___$2,___$3){
var new_val = (recalc.cljs$core$IFn$_invoke$arity$0 ? recalc.cljs$core$IFn$_invoke$arity$0() : recalc.call(null));
if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(sink),new_val)){
return cljs.core.reset_BANG_(sink,new_val);
} else {
return null;
}
}):(function (_,___$1,___$2,___$3){
return cljs.core.reset_BANG_(sink,(recalc.cljs$core$IFn$_invoke$arity$0 ? recalc.cljs$core$IFn$_invoke$arity$0() : recalc.call(null)));
}));
var seq__38818_38855 = cljs.core.seq(refs);
var chunk__38819_38856 = null;
var count__38820_38857 = (0);
var i__38821_38858 = (0);
while(true){
if((i__38821_38858 < count__38820_38857)){
var ref_38859 = chunk__38819_38856.cljs$core$IIndexed$_nth$arity$2(null,i__38821_38858);
cljs.core.add_watch(ref_38859,key,watch);


var G__38860 = seq__38818_38855;
var G__38861 = chunk__38819_38856;
var G__38862 = count__38820_38857;
var G__38863 = (i__38821_38858 + (1));
seq__38818_38855 = G__38860;
chunk__38819_38856 = G__38861;
count__38820_38857 = G__38862;
i__38821_38858 = G__38863;
continue;
} else {
var temp__5735__auto___38864 = cljs.core.seq(seq__38818_38855);
if(temp__5735__auto___38864){
var seq__38818_38865__$1 = temp__5735__auto___38864;
if(cljs.core.chunked_seq_QMARK_(seq__38818_38865__$1)){
var c__4556__auto___38866 = cljs.core.chunk_first(seq__38818_38865__$1);
var G__38867 = cljs.core.chunk_rest(seq__38818_38865__$1);
var G__38868 = c__4556__auto___38866;
var G__38869 = cljs.core.count(c__4556__auto___38866);
var G__38870 = (0);
seq__38818_38855 = G__38867;
chunk__38819_38856 = G__38868;
count__38820_38857 = G__38869;
i__38821_38858 = G__38870;
continue;
} else {
var ref_38871 = cljs.core.first(seq__38818_38865__$1);
cljs.core.add_watch(ref_38871,key,watch);


var G__38872 = cljs.core.next(seq__38818_38865__$1);
var G__38873 = null;
var G__38874 = (0);
var G__38875 = (0);
seq__38818_38855 = G__38872;
chunk__38819_38856 = G__38873;
count__38820_38857 = G__38874;
i__38821_38858 = G__38875;
continue;
}
} else {
}
}
break;
}

return org.martinklepsch.derived.__GT_DerivedValue(sink,refs,key);
}));

(org.martinklepsch.derived.derived_value.cljs$lang$maxFixedArity = 4);


//# sourceMappingURL=org.martinklepsch.derived.js.map
