goog.provide('sablono.core');
/**
 * Add an optional attribute argument to a function that returns a element vector.
 */
sablono.core.wrap_attrs = (function sablono$core$wrap_attrs(func){
return (function() { 
var G__37459__delegate = function (args){
if(cljs.core.map_QMARK_(cljs.core.first(args))){
var vec__36951 = cljs.core.apply.cljs$core$IFn$_invoke$arity$2(func,cljs.core.rest(args));
var seq__36952 = cljs.core.seq(vec__36951);
var first__36953 = cljs.core.first(seq__36952);
var seq__36952__$1 = cljs.core.next(seq__36952);
var tag = first__36953;
var body = seq__36952__$1;
if(cljs.core.map_QMARK_(cljs.core.first(body))){
return cljs.core.into.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [tag,cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.first(body),cljs.core.first(args)], 0))], null),cljs.core.rest(body));
} else {
return cljs.core.into.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [tag,cljs.core.first(args)], null),body);
}
} else {
return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(func,args);
}
};
var G__37459 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__37464__i = 0, G__37464__a = new Array(arguments.length -  0);
while (G__37464__i < G__37464__a.length) {G__37464__a[G__37464__i] = arguments[G__37464__i + 0]; ++G__37464__i;}
  args = new cljs.core.IndexedSeq(G__37464__a,0,null);
} 
return G__37459__delegate.call(this,args);};
G__37459.cljs$lang$maxFixedArity = 0;
G__37459.cljs$lang$applyTo = (function (arglist__37465){
var args = cljs.core.seq(arglist__37465);
return G__37459__delegate(args);
});
G__37459.cljs$core$IFn$_invoke$arity$variadic = G__37459__delegate;
return G__37459;
})()
;
});
sablono.core.update_arglists = (function sablono$core$update_arglists(arglists){
var iter__4529__auto__ = (function sablono$core$update_arglists_$_iter__36964(s__36965){
return (new cljs.core.LazySeq(null,(function (){
var s__36965__$1 = s__36965;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__36965__$1);
if(temp__5735__auto__){
var s__36965__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__36965__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__36965__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__36967 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__36966 = (0);
while(true){
if((i__36966 < size__4528__auto__)){
var args = cljs.core._nth(c__4527__auto__,i__36966);
cljs.core.chunk_append(b__36967,cljs.core.vec(cljs.core.cons(new cljs.core.Symbol(null,"attr-map?","attr-map?",116307443,null),args)));

var G__37481 = (i__36966 + (1));
i__36966 = G__37481;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__36967),sablono$core$update_arglists_$_iter__36964(cljs.core.chunk_rest(s__36965__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__36967),null);
}
} else {
var args = cljs.core.first(s__36965__$2);
return cljs.core.cons(cljs.core.vec(cljs.core.cons(new cljs.core.Symbol(null,"attr-map?","attr-map?",116307443,null),args)),sablono$core$update_arglists_$_iter__36964(cljs.core.rest(s__36965__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(arglists);
});
/**
 * Include a list of external stylesheet files.
 */
sablono.core.include_css = (function sablono$core$include_css(var_args){
var args__4742__auto__ = [];
var len__4736__auto___37485 = arguments.length;
var i__4737__auto___37486 = (0);
while(true){
if((i__4737__auto___37486 < len__4736__auto___37485)){
args__4742__auto__.push((arguments[i__4737__auto___37486]));

var G__37488 = (i__4737__auto___37486 + (1));
i__4737__auto___37486 = G__37488;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return sablono.core.include_css.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(sablono.core.include_css.cljs$core$IFn$_invoke$arity$variadic = (function (styles){
var iter__4529__auto__ = (function sablono$core$iter__36977(s__36978){
return (new cljs.core.LazySeq(null,(function (){
var s__36978__$1 = s__36978;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__36978__$1);
if(temp__5735__auto__){
var s__36978__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__36978__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__36978__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__36980 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__36979 = (0);
while(true){
if((i__36979 < size__4528__auto__)){
var style = cljs.core._nth(c__4527__auto__,i__36979);
cljs.core.chunk_append(b__36980,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"link","link",-1769163468),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"type","type",1174270348),"text/css",new cljs.core.Keyword(null,"href","href",-793805698),sablono.util.as_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([style], 0)),new cljs.core.Keyword(null,"rel","rel",1378823488),"stylesheet"], null)], null));

var G__37496 = (i__36979 + (1));
i__36979 = G__37496;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__36980),sablono$core$iter__36977(cljs.core.chunk_rest(s__36978__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__36980),null);
}
} else {
var style = cljs.core.first(s__36978__$2);
return cljs.core.cons(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"link","link",-1769163468),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"type","type",1174270348),"text/css",new cljs.core.Keyword(null,"href","href",-793805698),sablono.util.as_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([style], 0)),new cljs.core.Keyword(null,"rel","rel",1378823488),"stylesheet"], null)], null),sablono$core$iter__36977(cljs.core.rest(s__36978__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(styles);
}));

(sablono.core.include_css.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(sablono.core.include_css.cljs$lang$applyTo = (function (seq36974){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq36974));
}));

/**
 * Include the JavaScript library at `src`.
 */
sablono.core.include_js = (function sablono$core$include_js(src){
return goog.dom.appendChild(goog.dom.getDocument().body,goog.dom.createDom("script",({"src": src})));
});
/**
 * Include Facebook's React JavaScript library.
 */
sablono.core.include_react = (function sablono$core$include_react(){
return sablono.core.include_js("http://fb.me/react-0.12.2.js");
});
/**
 * Wraps some content in a HTML hyperlink with the supplied URL.
 */
sablono.core.link_to36991 = (function sablono$core$link_to36991(var_args){
var args__4742__auto__ = [];
var len__4736__auto___37504 = arguments.length;
var i__4737__auto___37505 = (0);
while(true){
if((i__4737__auto___37505 < len__4736__auto___37504)){
args__4742__auto__.push((arguments[i__4737__auto___37505]));

var G__37506 = (i__4737__auto___37505 + (1));
i__4737__auto___37505 = G__37506;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return sablono.core.link_to36991.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(sablono.core.link_to36991.cljs$core$IFn$_invoke$arity$variadic = (function (url,content){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"href","href",-793805698),sablono.util.as_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([url], 0))], null),content], null);
}));

(sablono.core.link_to36991.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(sablono.core.link_to36991.cljs$lang$applyTo = (function (seq36997){
var G__36998 = cljs.core.first(seq36997);
var seq36997__$1 = cljs.core.next(seq36997);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__36998,seq36997__$1);
}));


sablono.core.link_to = sablono.core.wrap_attrs(sablono.core.link_to36991);
/**
 * Wraps some content in a HTML hyperlink with the supplied e-mail
 *   address. If no content provided use the e-mail address as content.
 */
sablono.core.mail_to37012 = (function sablono$core$mail_to37012(var_args){
var args__4742__auto__ = [];
var len__4736__auto___37511 = arguments.length;
var i__4737__auto___37512 = (0);
while(true){
if((i__4737__auto___37512 < len__4736__auto___37511)){
args__4742__auto__.push((arguments[i__4737__auto___37512]));

var G__37514 = (i__4737__auto___37512 + (1));
i__4737__auto___37512 = G__37514;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return sablono.core.mail_to37012.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(sablono.core.mail_to37012.cljs$core$IFn$_invoke$arity$variadic = (function (e_mail,p__37019){
var vec__37020 = p__37019;
var content = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37020,(0),null);
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"href","href",-793805698),["mailto:",cljs.core.str.cljs$core$IFn$_invoke$arity$1(e_mail)].join('')], null),(function (){var or__4126__auto__ = content;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return e_mail;
}
})()], null);
}));

(sablono.core.mail_to37012.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(sablono.core.mail_to37012.cljs$lang$applyTo = (function (seq37013){
var G__37014 = cljs.core.first(seq37013);
var seq37013__$1 = cljs.core.next(seq37013);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__37014,seq37013__$1);
}));


sablono.core.mail_to = sablono.core.wrap_attrs(sablono.core.mail_to37012);
/**
 * Wrap a collection in an unordered list.
 */
sablono.core.unordered_list37042 = (function sablono$core$unordered_list37042(coll){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"ul","ul",-1349521403),(function (){var iter__4529__auto__ = (function sablono$core$unordered_list37042_$_iter__37043(s__37044){
return (new cljs.core.LazySeq(null,(function (){
var s__37044__$1 = s__37044;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__37044__$1);
if(temp__5735__auto__){
var s__37044__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__37044__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__37044__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__37046 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__37045 = (0);
while(true){
if((i__37045 < size__4528__auto__)){
var x = cljs.core._nth(c__4527__auto__,i__37045);
cljs.core.chunk_append(b__37046,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li","li",723558921),x], null));

var G__37518 = (i__37045 + (1));
i__37045 = G__37518;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__37046),sablono$core$unordered_list37042_$_iter__37043(cljs.core.chunk_rest(s__37044__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__37046),null);
}
} else {
var x = cljs.core.first(s__37044__$2);
return cljs.core.cons(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li","li",723558921),x], null),sablono$core$unordered_list37042_$_iter__37043(cljs.core.rest(s__37044__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(coll);
})()], null);
});

sablono.core.unordered_list = sablono.core.wrap_attrs(sablono.core.unordered_list37042);
/**
 * Wrap a collection in an ordered list.
 */
sablono.core.ordered_list37056 = (function sablono$core$ordered_list37056(coll){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"ol","ol",932524051),(function (){var iter__4529__auto__ = (function sablono$core$ordered_list37056_$_iter__37065(s__37066){
return (new cljs.core.LazySeq(null,(function (){
var s__37066__$1 = s__37066;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__37066__$1);
if(temp__5735__auto__){
var s__37066__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__37066__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__37066__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__37068 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__37067 = (0);
while(true){
if((i__37067 < size__4528__auto__)){
var x = cljs.core._nth(c__4527__auto__,i__37067);
cljs.core.chunk_append(b__37068,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li","li",723558921),x], null));

var G__37522 = (i__37067 + (1));
i__37067 = G__37522;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__37068),sablono$core$ordered_list37056_$_iter__37065(cljs.core.chunk_rest(s__37066__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__37068),null);
}
} else {
var x = cljs.core.first(s__37066__$2);
return cljs.core.cons(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li","li",723558921),x], null),sablono$core$ordered_list37056_$_iter__37065(cljs.core.rest(s__37066__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(coll);
})()], null);
});

sablono.core.ordered_list = sablono.core.wrap_attrs(sablono.core.ordered_list37056);
/**
 * Create an image element.
 */
sablono.core.image37073 = (function sablono$core$image37073(var_args){
var G__37076 = arguments.length;
switch (G__37076) {
case 1:
return sablono.core.image37073.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.image37073.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(sablono.core.image37073.cljs$core$IFn$_invoke$arity$1 = (function (src){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"img","img",1442687358),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"src","src",-1651076051),sablono.util.as_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([src], 0))], null)], null);
}));

(sablono.core.image37073.cljs$core$IFn$_invoke$arity$2 = (function (src,alt){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"img","img",1442687358),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"src","src",-1651076051),sablono.util.as_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([src], 0)),new cljs.core.Keyword(null,"alt","alt",-3214426),alt], null)], null);
}));

(sablono.core.image37073.cljs$lang$maxFixedArity = 2);


sablono.core.image = sablono.core.wrap_attrs(sablono.core.image37073);
sablono.core._STAR_group_STAR_ = cljs.core.PersistentVector.EMPTY;
/**
 * Create a field name from the supplied argument the current field group.
 */
sablono.core.make_name = (function sablono$core$make_name(name){
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$2((function (p1__37079_SHARP_,p2__37080_SHARP_){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(p1__37079_SHARP_),"[",cljs.core.str.cljs$core$IFn$_invoke$arity$1(p2__37080_SHARP_),"]"].join('');
}),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(sablono.core._STAR_group_STAR_,sablono.util.as_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([name], 0))));
});
/**
 * Create a field id from the supplied argument and current field group.
 */
sablono.core.make_id = (function sablono$core$make_id(name){
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$2((function (p1__37088_SHARP_,p2__37089_SHARP_){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(p1__37088_SHARP_),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(p2__37089_SHARP_)].join('');
}),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(sablono.core._STAR_group_STAR_,sablono.util.as_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([name], 0))));
});
/**
 * Creates a new <input> element.
 */
sablono.core.input_field_STAR_ = (function sablono$core$input_field_STAR_(var_args){
var G__37094 = arguments.length;
switch (G__37094) {
case 2:
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$2 = (function (type,name){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"type","type",1174270348),type,new cljs.core.Keyword(null,"name","name",1843675177),sablono.core.make_name(name),new cljs.core.Keyword(null,"id","id",-1388402092),sablono.core.make_id(name)], null)], null);
}));

(sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$3 = (function (type,name,value){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"type","type",1174270348),type,new cljs.core.Keyword(null,"name","name",1843675177),sablono.core.make_name(name),new cljs.core.Keyword(null,"id","id",-1388402092),sablono.core.make_id(name),new cljs.core.Keyword(null,"value","value",305978217),(function (){var or__4126__auto__ = value;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return undefined;
}
})()], null)], null);
}));

(sablono.core.input_field_STAR_.cljs$lang$maxFixedArity = 3);

/**
 * Creates a color input field.
 */
sablono.core.color_field37101 = (function sablono$core$color_field37101(var_args){
var G__37109 = arguments.length;
switch (G__37109) {
case 1:
return sablono.core.color_field37101.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.color_field37101.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(sablono.core.color_field37101.cljs$core$IFn$_invoke$arity$1 = (function (name__36940__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$2(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"color","color",-1642760596,null)),name__36940__auto__);
}));

(sablono.core.color_field37101.cljs$core$IFn$_invoke$arity$2 = (function (name__36940__auto__,value__36941__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$3(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"color","color",-1642760596,null)),name__36940__auto__,value__36941__auto__);
}));

(sablono.core.color_field37101.cljs$lang$maxFixedArity = 2);


sablono.core.color_field = sablono.core.wrap_attrs(sablono.core.color_field37101);

/**
 * Creates a date input field.
 */
sablono.core.date_field37117 = (function sablono$core$date_field37117(var_args){
var G__37122 = arguments.length;
switch (G__37122) {
case 1:
return sablono.core.date_field37117.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.date_field37117.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(sablono.core.date_field37117.cljs$core$IFn$_invoke$arity$1 = (function (name__36940__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$2(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"date","date",177097065,null)),name__36940__auto__);
}));

(sablono.core.date_field37117.cljs$core$IFn$_invoke$arity$2 = (function (name__36940__auto__,value__36941__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$3(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"date","date",177097065,null)),name__36940__auto__,value__36941__auto__);
}));

(sablono.core.date_field37117.cljs$lang$maxFixedArity = 2);


sablono.core.date_field = sablono.core.wrap_attrs(sablono.core.date_field37117);

/**
 * Creates a datetime input field.
 */
sablono.core.datetime_field37127 = (function sablono$core$datetime_field37127(var_args){
var G__37129 = arguments.length;
switch (G__37129) {
case 1:
return sablono.core.datetime_field37127.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.datetime_field37127.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(sablono.core.datetime_field37127.cljs$core$IFn$_invoke$arity$1 = (function (name__36940__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$2(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"datetime","datetime",2135207229,null)),name__36940__auto__);
}));

(sablono.core.datetime_field37127.cljs$core$IFn$_invoke$arity$2 = (function (name__36940__auto__,value__36941__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$3(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"datetime","datetime",2135207229,null)),name__36940__auto__,value__36941__auto__);
}));

(sablono.core.datetime_field37127.cljs$lang$maxFixedArity = 2);


sablono.core.datetime_field = sablono.core.wrap_attrs(sablono.core.datetime_field37127);

/**
 * Creates a datetime-local input field.
 */
sablono.core.datetime_local_field37155 = (function sablono$core$datetime_local_field37155(var_args){
var G__37158 = arguments.length;
switch (G__37158) {
case 1:
return sablono.core.datetime_local_field37155.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.datetime_local_field37155.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(sablono.core.datetime_local_field37155.cljs$core$IFn$_invoke$arity$1 = (function (name__36940__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$2(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"datetime-local","datetime-local",-507312697,null)),name__36940__auto__);
}));

(sablono.core.datetime_local_field37155.cljs$core$IFn$_invoke$arity$2 = (function (name__36940__auto__,value__36941__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$3(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"datetime-local","datetime-local",-507312697,null)),name__36940__auto__,value__36941__auto__);
}));

(sablono.core.datetime_local_field37155.cljs$lang$maxFixedArity = 2);


sablono.core.datetime_local_field = sablono.core.wrap_attrs(sablono.core.datetime_local_field37155);

/**
 * Creates a email input field.
 */
sablono.core.email_field37161 = (function sablono$core$email_field37161(var_args){
var G__37164 = arguments.length;
switch (G__37164) {
case 1:
return sablono.core.email_field37161.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.email_field37161.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(sablono.core.email_field37161.cljs$core$IFn$_invoke$arity$1 = (function (name__36940__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$2(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"email","email",-1238619063,null)),name__36940__auto__);
}));

(sablono.core.email_field37161.cljs$core$IFn$_invoke$arity$2 = (function (name__36940__auto__,value__36941__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$3(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"email","email",-1238619063,null)),name__36940__auto__,value__36941__auto__);
}));

(sablono.core.email_field37161.cljs$lang$maxFixedArity = 2);


sablono.core.email_field = sablono.core.wrap_attrs(sablono.core.email_field37161);

/**
 * Creates a file input field.
 */
sablono.core.file_field37174 = (function sablono$core$file_field37174(var_args){
var G__37178 = arguments.length;
switch (G__37178) {
case 1:
return sablono.core.file_field37174.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.file_field37174.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(sablono.core.file_field37174.cljs$core$IFn$_invoke$arity$1 = (function (name__36940__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$2(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"file","file",370885649,null)),name__36940__auto__);
}));

(sablono.core.file_field37174.cljs$core$IFn$_invoke$arity$2 = (function (name__36940__auto__,value__36941__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$3(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"file","file",370885649,null)),name__36940__auto__,value__36941__auto__);
}));

(sablono.core.file_field37174.cljs$lang$maxFixedArity = 2);


sablono.core.file_field = sablono.core.wrap_attrs(sablono.core.file_field37174);

/**
 * Creates a hidden input field.
 */
sablono.core.hidden_field37183 = (function sablono$core$hidden_field37183(var_args){
var G__37185 = arguments.length;
switch (G__37185) {
case 1:
return sablono.core.hidden_field37183.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.hidden_field37183.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(sablono.core.hidden_field37183.cljs$core$IFn$_invoke$arity$1 = (function (name__36940__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$2(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"hidden","hidden",1328025435,null)),name__36940__auto__);
}));

(sablono.core.hidden_field37183.cljs$core$IFn$_invoke$arity$2 = (function (name__36940__auto__,value__36941__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$3(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"hidden","hidden",1328025435,null)),name__36940__auto__,value__36941__auto__);
}));

(sablono.core.hidden_field37183.cljs$lang$maxFixedArity = 2);


sablono.core.hidden_field = sablono.core.wrap_attrs(sablono.core.hidden_field37183);

/**
 * Creates a month input field.
 */
sablono.core.month_field37193 = (function sablono$core$month_field37193(var_args){
var G__37196 = arguments.length;
switch (G__37196) {
case 1:
return sablono.core.month_field37193.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.month_field37193.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(sablono.core.month_field37193.cljs$core$IFn$_invoke$arity$1 = (function (name__36940__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$2(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"month","month",-319717006,null)),name__36940__auto__);
}));

(sablono.core.month_field37193.cljs$core$IFn$_invoke$arity$2 = (function (name__36940__auto__,value__36941__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$3(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"month","month",-319717006,null)),name__36940__auto__,value__36941__auto__);
}));

(sablono.core.month_field37193.cljs$lang$maxFixedArity = 2);


sablono.core.month_field = sablono.core.wrap_attrs(sablono.core.month_field37193);

/**
 * Creates a number input field.
 */
sablono.core.number_field37199 = (function sablono$core$number_field37199(var_args){
var G__37202 = arguments.length;
switch (G__37202) {
case 1:
return sablono.core.number_field37199.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.number_field37199.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(sablono.core.number_field37199.cljs$core$IFn$_invoke$arity$1 = (function (name__36940__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$2(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"number","number",-1084057331,null)),name__36940__auto__);
}));

(sablono.core.number_field37199.cljs$core$IFn$_invoke$arity$2 = (function (name__36940__auto__,value__36941__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$3(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"number","number",-1084057331,null)),name__36940__auto__,value__36941__auto__);
}));

(sablono.core.number_field37199.cljs$lang$maxFixedArity = 2);


sablono.core.number_field = sablono.core.wrap_attrs(sablono.core.number_field37199);

/**
 * Creates a password input field.
 */
sablono.core.password_field37208 = (function sablono$core$password_field37208(var_args){
var G__37211 = arguments.length;
switch (G__37211) {
case 1:
return sablono.core.password_field37208.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.password_field37208.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(sablono.core.password_field37208.cljs$core$IFn$_invoke$arity$1 = (function (name__36940__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$2(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"password","password",2057553998,null)),name__36940__auto__);
}));

(sablono.core.password_field37208.cljs$core$IFn$_invoke$arity$2 = (function (name__36940__auto__,value__36941__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$3(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"password","password",2057553998,null)),name__36940__auto__,value__36941__auto__);
}));

(sablono.core.password_field37208.cljs$lang$maxFixedArity = 2);


sablono.core.password_field = sablono.core.wrap_attrs(sablono.core.password_field37208);

/**
 * Creates a range input field.
 */
sablono.core.range_field37213 = (function sablono$core$range_field37213(var_args){
var G__37217 = arguments.length;
switch (G__37217) {
case 1:
return sablono.core.range_field37213.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.range_field37213.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(sablono.core.range_field37213.cljs$core$IFn$_invoke$arity$1 = (function (name__36940__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$2(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"range","range",-1014743483,null)),name__36940__auto__);
}));

(sablono.core.range_field37213.cljs$core$IFn$_invoke$arity$2 = (function (name__36940__auto__,value__36941__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$3(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"range","range",-1014743483,null)),name__36940__auto__,value__36941__auto__);
}));

(sablono.core.range_field37213.cljs$lang$maxFixedArity = 2);


sablono.core.range_field = sablono.core.wrap_attrs(sablono.core.range_field37213);

/**
 * Creates a search input field.
 */
sablono.core.search_field37218 = (function sablono$core$search_field37218(var_args){
var G__37220 = arguments.length;
switch (G__37220) {
case 1:
return sablono.core.search_field37218.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.search_field37218.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(sablono.core.search_field37218.cljs$core$IFn$_invoke$arity$1 = (function (name__36940__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$2(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"search","search",-1089495947,null)),name__36940__auto__);
}));

(sablono.core.search_field37218.cljs$core$IFn$_invoke$arity$2 = (function (name__36940__auto__,value__36941__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$3(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"search","search",-1089495947,null)),name__36940__auto__,value__36941__auto__);
}));

(sablono.core.search_field37218.cljs$lang$maxFixedArity = 2);


sablono.core.search_field = sablono.core.wrap_attrs(sablono.core.search_field37218);

/**
 * Creates a tel input field.
 */
sablono.core.tel_field37225 = (function sablono$core$tel_field37225(var_args){
var G__37230 = arguments.length;
switch (G__37230) {
case 1:
return sablono.core.tel_field37225.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.tel_field37225.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(sablono.core.tel_field37225.cljs$core$IFn$_invoke$arity$1 = (function (name__36940__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$2(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"tel","tel",1864669686,null)),name__36940__auto__);
}));

(sablono.core.tel_field37225.cljs$core$IFn$_invoke$arity$2 = (function (name__36940__auto__,value__36941__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$3(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"tel","tel",1864669686,null)),name__36940__auto__,value__36941__auto__);
}));

(sablono.core.tel_field37225.cljs$lang$maxFixedArity = 2);


sablono.core.tel_field = sablono.core.wrap_attrs(sablono.core.tel_field37225);

/**
 * Creates a text input field.
 */
sablono.core.text_field37235 = (function sablono$core$text_field37235(var_args){
var G__37237 = arguments.length;
switch (G__37237) {
case 1:
return sablono.core.text_field37235.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.text_field37235.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(sablono.core.text_field37235.cljs$core$IFn$_invoke$arity$1 = (function (name__36940__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$2(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"text","text",-150030170,null)),name__36940__auto__);
}));

(sablono.core.text_field37235.cljs$core$IFn$_invoke$arity$2 = (function (name__36940__auto__,value__36941__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$3(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"text","text",-150030170,null)),name__36940__auto__,value__36941__auto__);
}));

(sablono.core.text_field37235.cljs$lang$maxFixedArity = 2);


sablono.core.text_field = sablono.core.wrap_attrs(sablono.core.text_field37235);

/**
 * Creates a time input field.
 */
sablono.core.time_field37260 = (function sablono$core$time_field37260(var_args){
var G__37267 = arguments.length;
switch (G__37267) {
case 1:
return sablono.core.time_field37260.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.time_field37260.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(sablono.core.time_field37260.cljs$core$IFn$_invoke$arity$1 = (function (name__36940__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$2(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"time","time",-1268547887,null)),name__36940__auto__);
}));

(sablono.core.time_field37260.cljs$core$IFn$_invoke$arity$2 = (function (name__36940__auto__,value__36941__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$3(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"time","time",-1268547887,null)),name__36940__auto__,value__36941__auto__);
}));

(sablono.core.time_field37260.cljs$lang$maxFixedArity = 2);


sablono.core.time_field = sablono.core.wrap_attrs(sablono.core.time_field37260);

/**
 * Creates a url input field.
 */
sablono.core.url_field37270 = (function sablono$core$url_field37270(var_args){
var G__37274 = arguments.length;
switch (G__37274) {
case 1:
return sablono.core.url_field37270.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.url_field37270.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(sablono.core.url_field37270.cljs$core$IFn$_invoke$arity$1 = (function (name__36940__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$2(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"url","url",1916828573,null)),name__36940__auto__);
}));

(sablono.core.url_field37270.cljs$core$IFn$_invoke$arity$2 = (function (name__36940__auto__,value__36941__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$3(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"url","url",1916828573,null)),name__36940__auto__,value__36941__auto__);
}));

(sablono.core.url_field37270.cljs$lang$maxFixedArity = 2);


sablono.core.url_field = sablono.core.wrap_attrs(sablono.core.url_field37270);

/**
 * Creates a week input field.
 */
sablono.core.week_field37289 = (function sablono$core$week_field37289(var_args){
var G__37291 = arguments.length;
switch (G__37291) {
case 1:
return sablono.core.week_field37289.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.week_field37289.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(sablono.core.week_field37289.cljs$core$IFn$_invoke$arity$1 = (function (name__36940__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$2(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"week","week",314058249,null)),name__36940__auto__);
}));

(sablono.core.week_field37289.cljs$core$IFn$_invoke$arity$2 = (function (name__36940__auto__,value__36941__auto__){
return sablono.core.input_field_STAR_.cljs$core$IFn$_invoke$arity$3(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Symbol(null,"week","week",314058249,null)),name__36940__auto__,value__36941__auto__);
}));

(sablono.core.week_field37289.cljs$lang$maxFixedArity = 2);


sablono.core.week_field = sablono.core.wrap_attrs(sablono.core.week_field37289);
sablono.core.file_upload = sablono.core.file_field;
/**
 * Creates a check box.
 */
sablono.core.check_box37300 = (function sablono$core$check_box37300(var_args){
var G__37305 = arguments.length;
switch (G__37305) {
case 1:
return sablono.core.check_box37300.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.check_box37300.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return sablono.core.check_box37300.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(sablono.core.check_box37300.cljs$core$IFn$_invoke$arity$1 = (function (name){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"type","type",1174270348),"checkbox",new cljs.core.Keyword(null,"name","name",1843675177),sablono.core.make_name(name),new cljs.core.Keyword(null,"id","id",-1388402092),sablono.core.make_id(name)], null)], null);
}));

(sablono.core.check_box37300.cljs$core$IFn$_invoke$arity$2 = (function (name,checked_QMARK_){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"type","type",1174270348),"checkbox",new cljs.core.Keyword(null,"name","name",1843675177),sablono.core.make_name(name),new cljs.core.Keyword(null,"id","id",-1388402092),sablono.core.make_id(name),new cljs.core.Keyword(null,"checked","checked",-50955819),checked_QMARK_], null)], null);
}));

(sablono.core.check_box37300.cljs$core$IFn$_invoke$arity$3 = (function (name,checked_QMARK_,value){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"type","type",1174270348),"checkbox",new cljs.core.Keyword(null,"name","name",1843675177),sablono.core.make_name(name),new cljs.core.Keyword(null,"id","id",-1388402092),sablono.core.make_id(name),new cljs.core.Keyword(null,"value","value",305978217),value,new cljs.core.Keyword(null,"checked","checked",-50955819),checked_QMARK_], null)], null);
}));

(sablono.core.check_box37300.cljs$lang$maxFixedArity = 3);


sablono.core.check_box = sablono.core.wrap_attrs(sablono.core.check_box37300);
/**
 * Creates a radio button.
 */
sablono.core.radio_button37316 = (function sablono$core$radio_button37316(var_args){
var G__37319 = arguments.length;
switch (G__37319) {
case 1:
return sablono.core.radio_button37316.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.radio_button37316.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return sablono.core.radio_button37316.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(sablono.core.radio_button37316.cljs$core$IFn$_invoke$arity$1 = (function (group){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"type","type",1174270348),"radio",new cljs.core.Keyword(null,"name","name",1843675177),sablono.core.make_name(group),new cljs.core.Keyword(null,"id","id",-1388402092),sablono.core.make_id(sablono.util.as_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([group], 0)))], null)], null);
}));

(sablono.core.radio_button37316.cljs$core$IFn$_invoke$arity$2 = (function (group,checked_QMARK_){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"type","type",1174270348),"radio",new cljs.core.Keyword(null,"name","name",1843675177),sablono.core.make_name(group),new cljs.core.Keyword(null,"id","id",-1388402092),sablono.core.make_id(sablono.util.as_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([group], 0))),new cljs.core.Keyword(null,"checked","checked",-50955819),checked_QMARK_], null)], null);
}));

(sablono.core.radio_button37316.cljs$core$IFn$_invoke$arity$3 = (function (group,checked_QMARK_,value){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"type","type",1174270348),"radio",new cljs.core.Keyword(null,"name","name",1843675177),sablono.core.make_name(group),new cljs.core.Keyword(null,"id","id",-1388402092),sablono.core.make_id([sablono.util.as_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([group], 0)),"-",sablono.util.as_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([value], 0))].join('')),new cljs.core.Keyword(null,"value","value",305978217),value,new cljs.core.Keyword(null,"checked","checked",-50955819),checked_QMARK_], null)], null);
}));

(sablono.core.radio_button37316.cljs$lang$maxFixedArity = 3);


sablono.core.radio_button = sablono.core.wrap_attrs(sablono.core.radio_button37316);
sablono.core.hash_key = (function sablono$core$hash_key(x){
return goog.string.hashCode(cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([x], 0)));
});
/**
 * Creates a seq of option tags from a collection.
 */
sablono.core.select_options37328 = (function sablono$core$select_options37328(coll){
var iter__4529__auto__ = (function sablono$core$select_options37328_$_iter__37337(s__37338){
return (new cljs.core.LazySeq(null,(function (){
var s__37338__$1 = s__37338;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__37338__$1);
if(temp__5735__auto__){
var s__37338__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__37338__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__37338__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__37340 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__37339 = (0);
while(true){
if((i__37339 < size__4528__auto__)){
var x = cljs.core._nth(c__4527__auto__,i__37339);
cljs.core.chunk_append(b__37340,((cljs.core.sequential_QMARK_(x))?(function (){var vec__37351 = x;
var text = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37351,(0),null);
var val = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37351,(1),null);
var disabled_QMARK_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37351,(2),null);
var disabled_QMARK___$1 = cljs.core.boolean$(disabled_QMARK_);
if(cljs.core.sequential_QMARK_(val)){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"optgroup","optgroup",1738282218),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"key","key",-1516042587),sablono.core.hash_key(text),new cljs.core.Keyword(null,"label","label",1718410804),text], null),(sablono.core.select_options37328.cljs$core$IFn$_invoke$arity$1 ? sablono.core.select_options37328.cljs$core$IFn$_invoke$arity$1(val) : sablono.core.select_options37328.call(null,val))], null);
} else {
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"option","option",65132272),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"disabled","disabled",-1529784218),disabled_QMARK___$1,new cljs.core.Keyword(null,"key","key",-1516042587),sablono.core.hash_key(val),new cljs.core.Keyword(null,"value","value",305978217),val], null),text], null);
}
})():new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"option","option",65132272),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"key","key",-1516042587),sablono.core.hash_key(x),new cljs.core.Keyword(null,"value","value",305978217),x], null),x], null)));

var G__37695 = (i__37339 + (1));
i__37339 = G__37695;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__37340),sablono$core$select_options37328_$_iter__37337(cljs.core.chunk_rest(s__37338__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__37340),null);
}
} else {
var x = cljs.core.first(s__37338__$2);
return cljs.core.cons(((cljs.core.sequential_QMARK_(x))?(function (){var vec__37360 = x;
var text = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37360,(0),null);
var val = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37360,(1),null);
var disabled_QMARK_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37360,(2),null);
var disabled_QMARK___$1 = cljs.core.boolean$(disabled_QMARK_);
if(cljs.core.sequential_QMARK_(val)){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"optgroup","optgroup",1738282218),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"key","key",-1516042587),sablono.core.hash_key(text),new cljs.core.Keyword(null,"label","label",1718410804),text], null),(sablono.core.select_options37328.cljs$core$IFn$_invoke$arity$1 ? sablono.core.select_options37328.cljs$core$IFn$_invoke$arity$1(val) : sablono.core.select_options37328.call(null,val))], null);
} else {
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"option","option",65132272),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"disabled","disabled",-1529784218),disabled_QMARK___$1,new cljs.core.Keyword(null,"key","key",-1516042587),sablono.core.hash_key(val),new cljs.core.Keyword(null,"value","value",305978217),val], null),text], null);
}
})():new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"option","option",65132272),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"key","key",-1516042587),sablono.core.hash_key(x),new cljs.core.Keyword(null,"value","value",305978217),x], null),x], null)),sablono$core$select_options37328_$_iter__37337(cljs.core.rest(s__37338__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(coll);
});

sablono.core.select_options = sablono.core.wrap_attrs(sablono.core.select_options37328);
/**
 * Creates a drop-down box using the <select> tag.
 */
sablono.core.drop_down37377 = (function sablono$core$drop_down37377(var_args){
var G__37380 = arguments.length;
switch (G__37380) {
case 2:
return sablono.core.drop_down37377.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return sablono.core.drop_down37377.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(sablono.core.drop_down37377.cljs$core$IFn$_invoke$arity$2 = (function (name,options){
return sablono.core.drop_down37377.cljs$core$IFn$_invoke$arity$3(name,options,null);
}));

(sablono.core.drop_down37377.cljs$core$IFn$_invoke$arity$3 = (function (name,options,selected){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"select","select",1147833503),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"name","name",1843675177),sablono.core.make_name(name),new cljs.core.Keyword(null,"id","id",-1388402092),sablono.core.make_id(name)], null),sablono.core.select_options(options,selected)], null);
}));

(sablono.core.drop_down37377.cljs$lang$maxFixedArity = 3);


sablono.core.drop_down = sablono.core.wrap_attrs(sablono.core.drop_down37377);
/**
 * Creates a text area element.
 */
sablono.core.text_area37406 = (function sablono$core$text_area37406(var_args){
var G__37408 = arguments.length;
switch (G__37408) {
case 1:
return sablono.core.text_area37406.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return sablono.core.text_area37406.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(sablono.core.text_area37406.cljs$core$IFn$_invoke$arity$1 = (function (name){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"textarea","textarea",-650375824),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"name","name",1843675177),sablono.core.make_name(name),new cljs.core.Keyword(null,"id","id",-1388402092),sablono.core.make_id(name)], null)], null);
}));

(sablono.core.text_area37406.cljs$core$IFn$_invoke$arity$2 = (function (name,value){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"textarea","textarea",-650375824),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"name","name",1843675177),sablono.core.make_name(name),new cljs.core.Keyword(null,"id","id",-1388402092),sablono.core.make_id(name),new cljs.core.Keyword(null,"value","value",305978217),(function (){var or__4126__auto__ = value;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return undefined;
}
})()], null)], null);
}));

(sablono.core.text_area37406.cljs$lang$maxFixedArity = 2);


sablono.core.text_area = sablono.core.wrap_attrs(sablono.core.text_area37406);
/**
 * Creates a label for an input field with the supplied name.
 */
sablono.core.label37420 = (function sablono$core$label37420(name,text){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"label","label",1718410804),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"htmlFor","htmlFor",-1050291720),sablono.core.make_id(name)], null),text], null);
});

sablono.core.label = sablono.core.wrap_attrs(sablono.core.label37420);
/**
 * Creates a submit button.
 */
sablono.core.submit_button37422 = (function sablono$core$submit_button37422(text){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),"submit",new cljs.core.Keyword(null,"value","value",305978217),text], null)], null);
});

sablono.core.submit_button = sablono.core.wrap_attrs(sablono.core.submit_button37422);
/**
 * Creates a form reset button.
 */
sablono.core.reset_button37427 = (function sablono$core$reset_button37427(text){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),"reset",new cljs.core.Keyword(null,"value","value",305978217),text], null)], null);
});

sablono.core.reset_button = sablono.core.wrap_attrs(sablono.core.reset_button37427);
/**
 * Create a form that points to a particular method and route.
 *   e.g. (form-to [:put "/post"]
 *       ...)
 */
sablono.core.form_to37439 = (function sablono$core$form_to37439(var_args){
var args__4742__auto__ = [];
var len__4736__auto___37712 = arguments.length;
var i__4737__auto___37713 = (0);
while(true){
if((i__4737__auto___37713 < len__4736__auto___37712)){
args__4742__auto__.push((arguments[i__4737__auto___37713]));

var G__37714 = (i__4737__auto___37713 + (1));
i__4737__auto___37713 = G__37714;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return sablono.core.form_to37439.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(sablono.core.form_to37439.cljs$core$IFn$_invoke$arity$variadic = (function (p__37443,body){
var vec__37444 = p__37443;
var method = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37444,(0),null);
var action = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37444,(1),null);
var method_str = clojure.string.upper_case(cljs.core.name(method));
var action_uri = sablono.util.to_uri(action);
return cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(((cljs.core.contains_QMARK_(new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"get","get",1683182755),null,new cljs.core.Keyword(null,"post","post",269697687),null], null), null),method))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"form","form",-1624062471),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"method","method",55703592),method_str,new cljs.core.Keyword(null,"action","action",-811238024),action_uri], null)], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"form","form",-1624062471),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"method","method",55703592),"POST",new cljs.core.Keyword(null,"action","action",-811238024),action_uri], null),sablono.core.hidden_field(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),(3735928559)], null),"_method",method_str)], null)),body));
}));

(sablono.core.form_to37439.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(sablono.core.form_to37439.cljs$lang$applyTo = (function (seq37440){
var G__37441 = cljs.core.first(seq37440);
var seq37440__$1 = cljs.core.next(seq37440);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__37441,seq37440__$1);
}));


sablono.core.form_to = sablono.core.wrap_attrs(sablono.core.form_to37439);

//# sourceMappingURL=sablono.core.js.map
