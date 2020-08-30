goog.provide('dommy.core');
/**
 * Returns a selector in string format.
 * Accepts string, keyword, or collection.
 */
dommy.core.selector = (function dommy$core$selector(data){
if(cljs.core.coll_QMARK_(data)){
return clojure.string.join.cljs$core$IFn$_invoke$arity$2(" ",cljs.core.map.cljs$core$IFn$_invoke$arity$2(dommy.core.selector,data));
} else {
if(((typeof data === 'string') || ((data instanceof cljs.core.Keyword)))){
return cljs.core.name(data);
} else {
return null;
}
}
});
dommy.core.text = (function dommy$core$text(elem){
var or__4126__auto__ = elem.textContent;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return elem.innerText;
}
});
dommy.core.html = (function dommy$core$html(elem){
return elem.innerHTML;
});
dommy.core.value = (function dommy$core$value(elem){
return elem.value;
});
dommy.core.class$ = (function dommy$core$class(elem){
return elem.className;
});
dommy.core.attr = (function dommy$core$attr(elem,k){
if(cljs.core.truth_(k)){
return elem.getAttribute(dommy.utils.as_str(k));
} else {
return null;
}
});
/**
 * The computed style of `elem`, optionally specifying the key of
 * a particular style to return
 */
dommy.core.style = (function dommy$core$style(var_args){
var G__31913 = arguments.length;
switch (G__31913) {
case 1:
return dommy.core.style.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return dommy.core.style.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(dommy.core.style.cljs$core$IFn$_invoke$arity$1 = (function (elem){
return cljs.core.js__GT_clj.cljs$core$IFn$_invoke$arity$1(window.getComputedStyle(elem));
}));

(dommy.core.style.cljs$core$IFn$_invoke$arity$2 = (function (elem,k){
return (window.getComputedStyle(elem)[dommy.utils.as_str(k)]);
}));

(dommy.core.style.cljs$lang$maxFixedArity = 2);

dommy.core.px = (function dommy$core$px(elem,k){

var pixels = dommy.core.style.cljs$core$IFn$_invoke$arity$2(elem,k);
if(cljs.core.seq(pixels)){
return parseInt(pixels);
} else {
return null;
}
});
/**
 * Does `elem` contain `c` in its class list
 */
dommy.core.has_class_QMARK_ = (function dommy$core$has_class_QMARK_(elem,c){
var c__$1 = dommy.utils.as_str(c);
var temp__5733__auto__ = elem.classList;
if(cljs.core.truth_(temp__5733__auto__)){
var class_list = temp__5733__auto__;
return class_list.contains(c__$1);
} else {
var temp__5735__auto__ = dommy.core.class$(elem);
if(cljs.core.truth_(temp__5735__auto__)){
var class_name = temp__5735__auto__;
var temp__5735__auto____$1 = dommy.utils.class_index(class_name,c__$1);
if(cljs.core.truth_(temp__5735__auto____$1)){
var i = temp__5735__auto____$1;
return (i >= (0));
} else {
return null;
}
} else {
return null;
}
}
});
/**
 * Is `elem` hidden (as associated with hide!/show!/toggle!, using display: none)
 */
dommy.core.hidden_QMARK_ = (function dommy$core$hidden_QMARK_(elem){
return (dommy.core.style.cljs$core$IFn$_invoke$arity$2(elem,new cljs.core.Keyword(null,"display","display",242065432)) === "none");
});
/**
 * Returns a map of the bounding client rect of `elem`
 * as a map with [:top :left :right :bottom :width :height]
 */
dommy.core.bounding_client_rect = (function dommy$core$bounding_client_rect(elem){
var r = elem.getBoundingClientRect();
return new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"top","top",-1856271961),r.top,new cljs.core.Keyword(null,"bottom","bottom",-1550509018),r.bottom,new cljs.core.Keyword(null,"left","left",-399115937),r.left,new cljs.core.Keyword(null,"right","right",-452581833),r.right,new cljs.core.Keyword(null,"width","width",-384071477),r.width,new cljs.core.Keyword(null,"height","height",1025178622),r.height], null);
});
dommy.core.parent = (function dommy$core$parent(elem){
return elem.parentNode;
});
dommy.core.children = (function dommy$core$children(elem){
return elem.children;
});
/**
 * Lazy seq of the ancestors of `elem`
 */
dommy.core.ancestors = (function dommy$core$ancestors(elem){
return cljs.core.take_while.cljs$core$IFn$_invoke$arity$2(cljs.core.identity,cljs.core.iterate(dommy.core.parent,elem));
});
dommy.core.ancestor_nodes = dommy.core.ancestors;
/**
 * Returns a predicate on nodes that match `selector` at the
 * time of this `matches-pred` call (may return outdated results
 * if you fuck with the DOM)
 */
dommy.core.matches_pred = (function dommy$core$matches_pred(var_args){
var G__31928 = arguments.length;
switch (G__31928) {
case 2:
return dommy.core.matches_pred.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 1:
return dommy.core.matches_pred.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(dommy.core.matches_pred.cljs$core$IFn$_invoke$arity$2 = (function (base,selector){
var matches = dommy.utils.__GT_Array(base.querySelectorAll(dommy.core.selector(selector)));
return (function (elem){
return (matches.indexOf(elem) >= (0));
});
}));

(dommy.core.matches_pred.cljs$core$IFn$_invoke$arity$1 = (function (selector){
return dommy.core.matches_pred.cljs$core$IFn$_invoke$arity$2(document,selector);
}));

(dommy.core.matches_pred.cljs$lang$maxFixedArity = 2);

/**
 * Closest ancestor of `elem` (up to `base`, if provided)
 * that matches `selector`
 */
dommy.core.closest = (function dommy$core$closest(var_args){
var G__31935 = arguments.length;
switch (G__31935) {
case 3:
return dommy.core.closest.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 2:
return dommy.core.closest.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(dommy.core.closest.cljs$core$IFn$_invoke$arity$3 = (function (base,elem,selector){
return cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2(dommy.core.matches_pred.cljs$core$IFn$_invoke$arity$2(base,selector),cljs.core.take_while.cljs$core$IFn$_invoke$arity$2((function (p1__31932_SHARP_){
return (!((p1__31932_SHARP_ === base)));
}),dommy.core.ancestors(elem))));
}));

(dommy.core.closest.cljs$core$IFn$_invoke$arity$2 = (function (elem,selector){
return dommy.core.closest.cljs$core$IFn$_invoke$arity$3(document.body,elem,selector);
}));

(dommy.core.closest.cljs$lang$maxFixedArity = 3);

/**
 * Is `descendant` a descendant of `ancestor`?
 * (http://goo.gl/T8pgCX)
 */
dommy.core.descendant_QMARK_ = (function dommy$core$descendant_QMARK_(descendant,ancestor){
if(cljs.core.truth_(ancestor.contains)){
return ancestor.contains(descendant);
} else {
if(cljs.core.truth_(ancestor.compareDocumentPosition)){
return ((ancestor.compareDocumentPosition(descendant) & (1 << (4))) != 0);
} else {
return null;
}
}
});
/**
 * Set the textContent of `elem` to `text`, fall back to innerText
 */
dommy.core.set_text_BANG_ = (function dommy$core$set_text_BANG_(elem,text){
if((!((void 0 === elem.textContent)))){
(elem.textContent = text);
} else {
(elem.innerText = text);
}

return elem;
});
/**
 * Set the innerHTML of `elem` to `html`
 */
dommy.core.set_html_BANG_ = (function dommy$core$set_html_BANG_(elem,html){
(elem.innerHTML = html);

return elem;
});
/**
 * Set the value of `elem` to `value`
 */
dommy.core.set_value_BANG_ = (function dommy$core$set_value_BANG_(elem,value){
(elem.value = value);

return elem;
});
/**
 * Set the css class of `elem` to `elem`
 */
dommy.core.set_class_BANG_ = (function dommy$core$set_class_BANG_(elem,c){
return (elem.className = c);
});
/**
 * Set the style of `elem` using key-value pairs:
 * 
 *    (set-style! elem :display "block" :color "red")
 */
dommy.core.set_style_BANG_ = (function dommy$core$set_style_BANG_(var_args){
var args__4742__auto__ = [];
var len__4736__auto___32891 = arguments.length;
var i__4737__auto___32892 = (0);
while(true){
if((i__4737__auto___32892 < len__4736__auto___32891)){
args__4742__auto__.push((arguments[i__4737__auto___32892]));

var G__32895 = (i__4737__auto___32892 + (1));
i__4737__auto___32892 = G__32895;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return dommy.core.set_style_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(dommy.core.set_style_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (elem,kvs){
if(cljs.core.even_QMARK_(cljs.core.count(kvs))){
} else {
throw (new Error("Assert failed: (even? (count kvs))"));
}

var style = elem.style;
var seq__31968_32900 = cljs.core.seq(cljs.core.partition.cljs$core$IFn$_invoke$arity$2((2),kvs));
var chunk__31969_32901 = null;
var count__31970_32902 = (0);
var i__31971_32903 = (0);
while(true){
if((i__31971_32903 < count__31970_32902)){
var vec__31978_32905 = chunk__31969_32901.cljs$core$IIndexed$_nth$arity$2(null,i__31971_32903);
var k_32906 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31978_32905,(0),null);
var v_32907 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31978_32905,(1),null);
style.setProperty(dommy.utils.as_str(k_32906),v_32907);


var G__32911 = seq__31968_32900;
var G__32912 = chunk__31969_32901;
var G__32913 = count__31970_32902;
var G__32914 = (i__31971_32903 + (1));
seq__31968_32900 = G__32911;
chunk__31969_32901 = G__32912;
count__31970_32902 = G__32913;
i__31971_32903 = G__32914;
continue;
} else {
var temp__5735__auto___32916 = cljs.core.seq(seq__31968_32900);
if(temp__5735__auto___32916){
var seq__31968_32917__$1 = temp__5735__auto___32916;
if(cljs.core.chunked_seq_QMARK_(seq__31968_32917__$1)){
var c__4556__auto___32918 = cljs.core.chunk_first(seq__31968_32917__$1);
var G__32919 = cljs.core.chunk_rest(seq__31968_32917__$1);
var G__32920 = c__4556__auto___32918;
var G__32921 = cljs.core.count(c__4556__auto___32918);
var G__32922 = (0);
seq__31968_32900 = G__32919;
chunk__31969_32901 = G__32920;
count__31970_32902 = G__32921;
i__31971_32903 = G__32922;
continue;
} else {
var vec__31989_32923 = cljs.core.first(seq__31968_32917__$1);
var k_32924 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31989_32923,(0),null);
var v_32925 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31989_32923,(1),null);
style.setProperty(dommy.utils.as_str(k_32924),v_32925);


var G__32926 = cljs.core.next(seq__31968_32917__$1);
var G__32927 = null;
var G__32928 = (0);
var G__32929 = (0);
seq__31968_32900 = G__32926;
chunk__31969_32901 = G__32927;
count__31970_32902 = G__32928;
i__31971_32903 = G__32929;
continue;
}
} else {
}
}
break;
}

return elem;
}));

(dommy.core.set_style_BANG_.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(dommy.core.set_style_BANG_.cljs$lang$applyTo = (function (seq31948){
var G__31949 = cljs.core.first(seq31948);
var seq31948__$1 = cljs.core.next(seq31948);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__31949,seq31948__$1);
}));

/**
 * Remove the style of `elem` using keywords:
 *   
 *    (remove-style! elem :display :color)
 */
dommy.core.remove_style_BANG_ = (function dommy$core$remove_style_BANG_(var_args){
var args__4742__auto__ = [];
var len__4736__auto___32930 = arguments.length;
var i__4737__auto___32931 = (0);
while(true){
if((i__4737__auto___32931 < len__4736__auto___32930)){
args__4742__auto__.push((arguments[i__4737__auto___32931]));

var G__32932 = (i__4737__auto___32931 + (1));
i__4737__auto___32931 = G__32932;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return dommy.core.remove_style_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(dommy.core.remove_style_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (elem,keywords){
var style = elem.style;
var seq__31994_32940 = cljs.core.seq(keywords);
var chunk__31995_32941 = null;
var count__31996_32942 = (0);
var i__31997_32943 = (0);
while(true){
if((i__31997_32943 < count__31996_32942)){
var kw_32944 = chunk__31995_32941.cljs$core$IIndexed$_nth$arity$2(null,i__31997_32943);
style.removeProperty(dommy.utils.as_str(kw_32944));


var G__32945 = seq__31994_32940;
var G__32946 = chunk__31995_32941;
var G__32947 = count__31996_32942;
var G__32948 = (i__31997_32943 + (1));
seq__31994_32940 = G__32945;
chunk__31995_32941 = G__32946;
count__31996_32942 = G__32947;
i__31997_32943 = G__32948;
continue;
} else {
var temp__5735__auto___32949 = cljs.core.seq(seq__31994_32940);
if(temp__5735__auto___32949){
var seq__31994_32950__$1 = temp__5735__auto___32949;
if(cljs.core.chunked_seq_QMARK_(seq__31994_32950__$1)){
var c__4556__auto___32951 = cljs.core.chunk_first(seq__31994_32950__$1);
var G__32952 = cljs.core.chunk_rest(seq__31994_32950__$1);
var G__32953 = c__4556__auto___32951;
var G__32954 = cljs.core.count(c__4556__auto___32951);
var G__32955 = (0);
seq__31994_32940 = G__32952;
chunk__31995_32941 = G__32953;
count__31996_32942 = G__32954;
i__31997_32943 = G__32955;
continue;
} else {
var kw_32957 = cljs.core.first(seq__31994_32950__$1);
style.removeProperty(dommy.utils.as_str(kw_32957));


var G__32960 = cljs.core.next(seq__31994_32950__$1);
var G__32961 = null;
var G__32962 = (0);
var G__32963 = (0);
seq__31994_32940 = G__32960;
chunk__31995_32941 = G__32961;
count__31996_32942 = G__32962;
i__31997_32943 = G__32963;
continue;
}
} else {
}
}
break;
}

return elem;
}));

(dommy.core.remove_style_BANG_.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(dommy.core.remove_style_BANG_.cljs$lang$applyTo = (function (seq31992){
var G__31993 = cljs.core.first(seq31992);
var seq31992__$1 = cljs.core.next(seq31992);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__31993,seq31992__$1);
}));

dommy.core.set_px_BANG_ = (function dommy$core$set_px_BANG_(var_args){
var args__4742__auto__ = [];
var len__4736__auto___32967 = arguments.length;
var i__4737__auto___32968 = (0);
while(true){
if((i__4737__auto___32968 < len__4736__auto___32967)){
args__4742__auto__.push((arguments[i__4737__auto___32968]));

var G__32969 = (i__4737__auto___32968 + (1));
i__4737__auto___32968 = G__32969;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return dommy.core.set_px_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(dommy.core.set_px_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (elem,kvs){

if(cljs.core.even_QMARK_(cljs.core.count(kvs))){
} else {
throw (new Error("Assert failed: (even? (count kvs))"));
}

var seq__32003_32970 = cljs.core.seq(cljs.core.partition.cljs$core$IFn$_invoke$arity$2((2),kvs));
var chunk__32004_32971 = null;
var count__32005_32972 = (0);
var i__32006_32973 = (0);
while(true){
if((i__32006_32973 < count__32005_32972)){
var vec__32017_32974 = chunk__32004_32971.cljs$core$IIndexed$_nth$arity$2(null,i__32006_32973);
var k_32975 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32017_32974,(0),null);
var v_32976 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32017_32974,(1),null);
dommy.core.set_style_BANG_.cljs$core$IFn$_invoke$arity$variadic(elem,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([k_32975,[cljs.core.str.cljs$core$IFn$_invoke$arity$1(v_32976),"px"].join('')], 0));


var G__32979 = seq__32003_32970;
var G__32980 = chunk__32004_32971;
var G__32981 = count__32005_32972;
var G__32982 = (i__32006_32973 + (1));
seq__32003_32970 = G__32979;
chunk__32004_32971 = G__32980;
count__32005_32972 = G__32981;
i__32006_32973 = G__32982;
continue;
} else {
var temp__5735__auto___32984 = cljs.core.seq(seq__32003_32970);
if(temp__5735__auto___32984){
var seq__32003_32985__$1 = temp__5735__auto___32984;
if(cljs.core.chunked_seq_QMARK_(seq__32003_32985__$1)){
var c__4556__auto___32986 = cljs.core.chunk_first(seq__32003_32985__$1);
var G__32987 = cljs.core.chunk_rest(seq__32003_32985__$1);
var G__32988 = c__4556__auto___32986;
var G__32989 = cljs.core.count(c__4556__auto___32986);
var G__32990 = (0);
seq__32003_32970 = G__32987;
chunk__32004_32971 = G__32988;
count__32005_32972 = G__32989;
i__32006_32973 = G__32990;
continue;
} else {
var vec__32024_32991 = cljs.core.first(seq__32003_32985__$1);
var k_32992 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32024_32991,(0),null);
var v_32993 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32024_32991,(1),null);
dommy.core.set_style_BANG_.cljs$core$IFn$_invoke$arity$variadic(elem,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([k_32992,[cljs.core.str.cljs$core$IFn$_invoke$arity$1(v_32993),"px"].join('')], 0));


var G__33002 = cljs.core.next(seq__32003_32985__$1);
var G__33003 = null;
var G__33004 = (0);
var G__33005 = (0);
seq__32003_32970 = G__33002;
chunk__32004_32971 = G__33003;
count__32005_32972 = G__33004;
i__32006_32973 = G__33005;
continue;
}
} else {
}
}
break;
}

return elem;
}));

(dommy.core.set_px_BANG_.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(dommy.core.set_px_BANG_.cljs$lang$applyTo = (function (seq32001){
var G__32002 = cljs.core.first(seq32001);
var seq32001__$1 = cljs.core.next(seq32001);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__32002,seq32001__$1);
}));

/**
 * Sets dom attributes on and returns `elem`.
 * Attributes without values will be set to their name:
 * 
 *     (set-attr! elem :disabled)
 * 
 * With values, the function takes variadic kv pairs:
 * 
 *     (set-attr! elem :id "some-id"
 *                     :name "some-name")
 */
dommy.core.set_attr_BANG_ = (function dommy$core$set_attr_BANG_(var_args){
var G__32032 = arguments.length;
switch (G__32032) {
case 2:
return dommy.core.set_attr_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return dommy.core.set_attr_BANG_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
var args_arr__4757__auto__ = [];
var len__4736__auto___33016 = arguments.length;
var i__4737__auto___33017 = (0);
while(true){
if((i__4737__auto___33017 < len__4736__auto___33016)){
args_arr__4757__auto__.push((arguments[i__4737__auto___33017]));

var G__33018 = (i__4737__auto___33017 + (1));
i__4737__auto___33017 = G__33018;
continue;
} else {
}
break;
}

var argseq__4758__auto__ = (new cljs.core.IndexedSeq(args_arr__4757__auto__.slice((3)),(0),null));
return dommy.core.set_attr_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),argseq__4758__auto__);

}
});

(dommy.core.set_attr_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (elem,k){
return dommy.core.set_attr_BANG_.cljs$core$IFn$_invoke$arity$3(elem,k,dommy.utils.as_str(k));
}));

(dommy.core.set_attr_BANG_.cljs$core$IFn$_invoke$arity$3 = (function (elem,k,v){
var k__$1 = dommy.utils.as_str(k);
if(cljs.core.truth_(v)){
if(cljs.core.fn_QMARK_(v)){
var G__32033 = elem;
(G__32033[k__$1] = v);

return G__32033;
} else {
var G__32040 = elem;
G__32040.setAttribute(k__$1,v);

return G__32040;
}
} else {
return null;
}
}));

(dommy.core.set_attr_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (elem,k,v,kvs){
if(cljs.core.even_QMARK_(cljs.core.count(kvs))){
} else {
throw (new Error("Assert failed: (even? (count kvs))"));
}

var seq__32041_33019 = cljs.core.seq(cljs.core.cons(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [k,v], null),cljs.core.partition.cljs$core$IFn$_invoke$arity$2((2),kvs)));
var chunk__32042_33020 = null;
var count__32043_33021 = (0);
var i__32044_33022 = (0);
while(true){
if((i__32044_33022 < count__32043_33021)){
var vec__32053_33026 = chunk__32042_33020.cljs$core$IIndexed$_nth$arity$2(null,i__32044_33022);
var k_33027__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32053_33026,(0),null);
var v_33028__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32053_33026,(1),null);
dommy.core.set_attr_BANG_.cljs$core$IFn$_invoke$arity$3(elem,k_33027__$1,v_33028__$1);


var G__33029 = seq__32041_33019;
var G__33030 = chunk__32042_33020;
var G__33031 = count__32043_33021;
var G__33032 = (i__32044_33022 + (1));
seq__32041_33019 = G__33029;
chunk__32042_33020 = G__33030;
count__32043_33021 = G__33031;
i__32044_33022 = G__33032;
continue;
} else {
var temp__5735__auto___33033 = cljs.core.seq(seq__32041_33019);
if(temp__5735__auto___33033){
var seq__32041_33035__$1 = temp__5735__auto___33033;
if(cljs.core.chunked_seq_QMARK_(seq__32041_33035__$1)){
var c__4556__auto___33036 = cljs.core.chunk_first(seq__32041_33035__$1);
var G__33037 = cljs.core.chunk_rest(seq__32041_33035__$1);
var G__33038 = c__4556__auto___33036;
var G__33039 = cljs.core.count(c__4556__auto___33036);
var G__33040 = (0);
seq__32041_33019 = G__33037;
chunk__32042_33020 = G__33038;
count__32043_33021 = G__33039;
i__32044_33022 = G__33040;
continue;
} else {
var vec__32056_33042 = cljs.core.first(seq__32041_33035__$1);
var k_33043__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32056_33042,(0),null);
var v_33044__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32056_33042,(1),null);
dommy.core.set_attr_BANG_.cljs$core$IFn$_invoke$arity$3(elem,k_33043__$1,v_33044__$1);


var G__33048 = cljs.core.next(seq__32041_33035__$1);
var G__33049 = null;
var G__33050 = (0);
var G__33051 = (0);
seq__32041_33019 = G__33048;
chunk__32042_33020 = G__33049;
count__32043_33021 = G__33050;
i__32044_33022 = G__33051;
continue;
}
} else {
}
}
break;
}

return elem;
}));

/** @this {Function} */
(dommy.core.set_attr_BANG_.cljs$lang$applyTo = (function (seq32028){
var G__32029 = cljs.core.first(seq32028);
var seq32028__$1 = cljs.core.next(seq32028);
var G__32030 = cljs.core.first(seq32028__$1);
var seq32028__$2 = cljs.core.next(seq32028__$1);
var G__32031 = cljs.core.first(seq32028__$2);
var seq32028__$3 = cljs.core.next(seq32028__$2);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__32029,G__32030,G__32031,seq32028__$3);
}));

(dommy.core.set_attr_BANG_.cljs$lang$maxFixedArity = (3));

/**
 * Removes dom attributes on and returns `elem`.
 * `class` and `classes` are special cases which clear
 * out the class name on removal.
 */
dommy.core.remove_attr_BANG_ = (function dommy$core$remove_attr_BANG_(var_args){
var G__32068 = arguments.length;
switch (G__32068) {
case 2:
return dommy.core.remove_attr_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
var args_arr__4757__auto__ = [];
var len__4736__auto___33062 = arguments.length;
var i__4737__auto___33063 = (0);
while(true){
if((i__4737__auto___33063 < len__4736__auto___33062)){
args_arr__4757__auto__.push((arguments[i__4737__auto___33063]));

var G__33064 = (i__4737__auto___33063 + (1));
i__4737__auto___33063 = G__33064;
continue;
} else {
}
break;
}

var argseq__4758__auto__ = (new cljs.core.IndexedSeq(args_arr__4757__auto__.slice((2)),(0),null));
return dommy.core.remove_attr_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__4758__auto__);

}
});

(dommy.core.remove_attr_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (elem,k){
var k_33075__$1 = dommy.utils.as_str(k);
if(cljs.core.truth_((function (){var fexpr__32080 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, ["class",null,"classes",null], null), null);
return (fexpr__32080.cljs$core$IFn$_invoke$arity$1 ? fexpr__32080.cljs$core$IFn$_invoke$arity$1(k_33075__$1) : fexpr__32080.call(null,k_33075__$1));
})())){
dommy.core.set_class_BANG_(elem,"");
} else {
elem.removeAttribute(k_33075__$1);
}

return elem;
}));

(dommy.core.remove_attr_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (elem,k,ks){
var seq__32084_33080 = cljs.core.seq(cljs.core.cons(k,ks));
var chunk__32085_33081 = null;
var count__32086_33082 = (0);
var i__32087_33083 = (0);
while(true){
if((i__32087_33083 < count__32086_33082)){
var k_33084__$1 = chunk__32085_33081.cljs$core$IIndexed$_nth$arity$2(null,i__32087_33083);
dommy.core.remove_attr_BANG_.cljs$core$IFn$_invoke$arity$2(elem,k_33084__$1);


var G__33085 = seq__32084_33080;
var G__33086 = chunk__32085_33081;
var G__33087 = count__32086_33082;
var G__33088 = (i__32087_33083 + (1));
seq__32084_33080 = G__33085;
chunk__32085_33081 = G__33086;
count__32086_33082 = G__33087;
i__32087_33083 = G__33088;
continue;
} else {
var temp__5735__auto___33089 = cljs.core.seq(seq__32084_33080);
if(temp__5735__auto___33089){
var seq__32084_33090__$1 = temp__5735__auto___33089;
if(cljs.core.chunked_seq_QMARK_(seq__32084_33090__$1)){
var c__4556__auto___33091 = cljs.core.chunk_first(seq__32084_33090__$1);
var G__33092 = cljs.core.chunk_rest(seq__32084_33090__$1);
var G__33093 = c__4556__auto___33091;
var G__33094 = cljs.core.count(c__4556__auto___33091);
var G__33095 = (0);
seq__32084_33080 = G__33092;
chunk__32085_33081 = G__33093;
count__32086_33082 = G__33094;
i__32087_33083 = G__33095;
continue;
} else {
var k_33096__$1 = cljs.core.first(seq__32084_33090__$1);
dommy.core.remove_attr_BANG_.cljs$core$IFn$_invoke$arity$2(elem,k_33096__$1);


var G__33097 = cljs.core.next(seq__32084_33090__$1);
var G__33098 = null;
var G__33099 = (0);
var G__33100 = (0);
seq__32084_33080 = G__33097;
chunk__32085_33081 = G__33098;
count__32086_33082 = G__33099;
i__32087_33083 = G__33100;
continue;
}
} else {
}
}
break;
}

return elem;
}));

/** @this {Function} */
(dommy.core.remove_attr_BANG_.cljs$lang$applyTo = (function (seq32065){
var G__32066 = cljs.core.first(seq32065);
var seq32065__$1 = cljs.core.next(seq32065);
var G__32067 = cljs.core.first(seq32065__$1);
var seq32065__$2 = cljs.core.next(seq32065__$1);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__32066,G__32067,seq32065__$2);
}));

(dommy.core.remove_attr_BANG_.cljs$lang$maxFixedArity = (2));

/**
 * Toggles a dom attribute `k` on `elem`, optionally specifying
 * the boolean value with `add?`
 */
dommy.core.toggle_attr_BANG_ = (function dommy$core$toggle_attr_BANG_(var_args){
var G__32089 = arguments.length;
switch (G__32089) {
case 2:
return dommy.core.toggle_attr_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return dommy.core.toggle_attr_BANG_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(dommy.core.toggle_attr_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (elem,k){
return dommy.core.toggle_attr_BANG_.cljs$core$IFn$_invoke$arity$3(elem,k,cljs.core.boolean$(dommy.core.attr(elem,k)));
}));

(dommy.core.toggle_attr_BANG_.cljs$core$IFn$_invoke$arity$3 = (function (elem,k,add_QMARK_){
if(add_QMARK_){
return dommy.core.set_attr_BANG_.cljs$core$IFn$_invoke$arity$2(elem,k);
} else {
return dommy.core.remove_attr_BANG_.cljs$core$IFn$_invoke$arity$2(elem,k);
}
}));

(dommy.core.toggle_attr_BANG_.cljs$lang$maxFixedArity = 3);

/**
 * Add `classes` to `elem`, trying to use Element::classList, and
 * falling back to fast string parsing/manipulation
 */
dommy.core.add_class_BANG_ = (function dommy$core$add_class_BANG_(var_args){
var G__32111 = arguments.length;
switch (G__32111) {
case 2:
return dommy.core.add_class_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
var args_arr__4757__auto__ = [];
var len__4736__auto___33115 = arguments.length;
var i__4737__auto___33116 = (0);
while(true){
if((i__4737__auto___33116 < len__4736__auto___33115)){
args_arr__4757__auto__.push((arguments[i__4737__auto___33116]));

var G__33117 = (i__4737__auto___33116 + (1));
i__4737__auto___33116 = G__33117;
continue;
} else {
}
break;
}

var argseq__4758__auto__ = (new cljs.core.IndexedSeq(args_arr__4757__auto__.slice((2)),(0),null));
return dommy.core.add_class_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__4758__auto__);

}
});

(dommy.core.add_class_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (elem,classes){
var classes__$1 = clojure.string.trim(dommy.utils.as_str(classes)).split(/\s+/);
if(cljs.core.seq(classes__$1)){
var temp__5733__auto___33121 = elem.classList;
if(cljs.core.truth_(temp__5733__auto___33121)){
var class_list_33122 = temp__5733__auto___33121;
var seq__32120_33123 = cljs.core.seq(classes__$1);
var chunk__32121_33124 = null;
var count__32122_33125 = (0);
var i__32123_33126 = (0);
while(true){
if((i__32123_33126 < count__32122_33125)){
var c_33129 = chunk__32121_33124.cljs$core$IIndexed$_nth$arity$2(null,i__32123_33126);
class_list_33122.add(c_33129);


var G__33130 = seq__32120_33123;
var G__33131 = chunk__32121_33124;
var G__33132 = count__32122_33125;
var G__33133 = (i__32123_33126 + (1));
seq__32120_33123 = G__33130;
chunk__32121_33124 = G__33131;
count__32122_33125 = G__33132;
i__32123_33126 = G__33133;
continue;
} else {
var temp__5735__auto___33134 = cljs.core.seq(seq__32120_33123);
if(temp__5735__auto___33134){
var seq__32120_33135__$1 = temp__5735__auto___33134;
if(cljs.core.chunked_seq_QMARK_(seq__32120_33135__$1)){
var c__4556__auto___33137 = cljs.core.chunk_first(seq__32120_33135__$1);
var G__33138 = cljs.core.chunk_rest(seq__32120_33135__$1);
var G__33139 = c__4556__auto___33137;
var G__33140 = cljs.core.count(c__4556__auto___33137);
var G__33141 = (0);
seq__32120_33123 = G__33138;
chunk__32121_33124 = G__33139;
count__32122_33125 = G__33140;
i__32123_33126 = G__33141;
continue;
} else {
var c_33142 = cljs.core.first(seq__32120_33135__$1);
class_list_33122.add(c_33142);


var G__33143 = cljs.core.next(seq__32120_33135__$1);
var G__33144 = null;
var G__33145 = (0);
var G__33146 = (0);
seq__32120_33123 = G__33143;
chunk__32121_33124 = G__33144;
count__32122_33125 = G__33145;
i__32123_33126 = G__33146;
continue;
}
} else {
}
}
break;
}
} else {
var seq__32136_33149 = cljs.core.seq(classes__$1);
var chunk__32137_33150 = null;
var count__32138_33151 = (0);
var i__32139_33152 = (0);
while(true){
if((i__32139_33152 < count__32138_33151)){
var c_33155 = chunk__32137_33150.cljs$core$IIndexed$_nth$arity$2(null,i__32139_33152);
var class_name_33156 = dommy.core.class$(elem);
if(cljs.core.truth_(dommy.utils.class_index(class_name_33156,c_33155))){
} else {
dommy.core.set_class_BANG_(elem,(((class_name_33156 === ""))?c_33155:[cljs.core.str.cljs$core$IFn$_invoke$arity$1(class_name_33156)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(c_33155)].join('')));
}


var G__33157 = seq__32136_33149;
var G__33158 = chunk__32137_33150;
var G__33159 = count__32138_33151;
var G__33160 = (i__32139_33152 + (1));
seq__32136_33149 = G__33157;
chunk__32137_33150 = G__33158;
count__32138_33151 = G__33159;
i__32139_33152 = G__33160;
continue;
} else {
var temp__5735__auto___33162 = cljs.core.seq(seq__32136_33149);
if(temp__5735__auto___33162){
var seq__32136_33163__$1 = temp__5735__auto___33162;
if(cljs.core.chunked_seq_QMARK_(seq__32136_33163__$1)){
var c__4556__auto___33166 = cljs.core.chunk_first(seq__32136_33163__$1);
var G__33167 = cljs.core.chunk_rest(seq__32136_33163__$1);
var G__33168 = c__4556__auto___33166;
var G__33169 = cljs.core.count(c__4556__auto___33166);
var G__33170 = (0);
seq__32136_33149 = G__33167;
chunk__32137_33150 = G__33168;
count__32138_33151 = G__33169;
i__32139_33152 = G__33170;
continue;
} else {
var c_33171 = cljs.core.first(seq__32136_33163__$1);
var class_name_33172 = dommy.core.class$(elem);
if(cljs.core.truth_(dommy.utils.class_index(class_name_33172,c_33171))){
} else {
dommy.core.set_class_BANG_(elem,(((class_name_33172 === ""))?c_33171:[cljs.core.str.cljs$core$IFn$_invoke$arity$1(class_name_33172)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(c_33171)].join('')));
}


var G__33173 = cljs.core.next(seq__32136_33163__$1);
var G__33174 = null;
var G__33175 = (0);
var G__33176 = (0);
seq__32136_33149 = G__33173;
chunk__32137_33150 = G__33174;
count__32138_33151 = G__33175;
i__32139_33152 = G__33176;
continue;
}
} else {
}
}
break;
}
}
} else {
}

return elem;
}));

(dommy.core.add_class_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (elem,classes,more_classes){
var seq__32143_33177 = cljs.core.seq(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(more_classes,classes));
var chunk__32144_33178 = null;
var count__32145_33179 = (0);
var i__32146_33180 = (0);
while(true){
if((i__32146_33180 < count__32145_33179)){
var c_33181 = chunk__32144_33178.cljs$core$IIndexed$_nth$arity$2(null,i__32146_33180);
dommy.core.add_class_BANG_.cljs$core$IFn$_invoke$arity$2(elem,c_33181);


var G__33182 = seq__32143_33177;
var G__33183 = chunk__32144_33178;
var G__33184 = count__32145_33179;
var G__33185 = (i__32146_33180 + (1));
seq__32143_33177 = G__33182;
chunk__32144_33178 = G__33183;
count__32145_33179 = G__33184;
i__32146_33180 = G__33185;
continue;
} else {
var temp__5735__auto___33188 = cljs.core.seq(seq__32143_33177);
if(temp__5735__auto___33188){
var seq__32143_33189__$1 = temp__5735__auto___33188;
if(cljs.core.chunked_seq_QMARK_(seq__32143_33189__$1)){
var c__4556__auto___33190 = cljs.core.chunk_first(seq__32143_33189__$1);
var G__33191 = cljs.core.chunk_rest(seq__32143_33189__$1);
var G__33192 = c__4556__auto___33190;
var G__33193 = cljs.core.count(c__4556__auto___33190);
var G__33194 = (0);
seq__32143_33177 = G__33191;
chunk__32144_33178 = G__33192;
count__32145_33179 = G__33193;
i__32146_33180 = G__33194;
continue;
} else {
var c_33195 = cljs.core.first(seq__32143_33189__$1);
dommy.core.add_class_BANG_.cljs$core$IFn$_invoke$arity$2(elem,c_33195);


var G__33196 = cljs.core.next(seq__32143_33189__$1);
var G__33197 = null;
var G__33198 = (0);
var G__33199 = (0);
seq__32143_33177 = G__33196;
chunk__32144_33178 = G__33197;
count__32145_33179 = G__33198;
i__32146_33180 = G__33199;
continue;
}
} else {
}
}
break;
}

return elem;
}));

/** @this {Function} */
(dommy.core.add_class_BANG_.cljs$lang$applyTo = (function (seq32103){
var G__32104 = cljs.core.first(seq32103);
var seq32103__$1 = cljs.core.next(seq32103);
var G__32105 = cljs.core.first(seq32103__$1);
var seq32103__$2 = cljs.core.next(seq32103__$1);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__32104,G__32105,seq32103__$2);
}));

(dommy.core.add_class_BANG_.cljs$lang$maxFixedArity = (2));

/**
 * Remove `c` from `elem` class list
 */
dommy.core.remove_class_BANG_ = (function dommy$core$remove_class_BANG_(var_args){
var G__32174 = arguments.length;
switch (G__32174) {
case 2:
return dommy.core.remove_class_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
var args_arr__4757__auto__ = [];
var len__4736__auto___33201 = arguments.length;
var i__4737__auto___33202 = (0);
while(true){
if((i__4737__auto___33202 < len__4736__auto___33201)){
args_arr__4757__auto__.push((arguments[i__4737__auto___33202]));

var G__33203 = (i__4737__auto___33202 + (1));
i__4737__auto___33202 = G__33203;
continue;
} else {
}
break;
}

var argseq__4758__auto__ = (new cljs.core.IndexedSeq(args_arr__4757__auto__.slice((2)),(0),null));
return dommy.core.remove_class_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__4758__auto__);

}
});

(dommy.core.remove_class_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (elem,c){
var c__$1 = dommy.utils.as_str(c);
var temp__5733__auto___33207 = elem.classList;
if(cljs.core.truth_(temp__5733__auto___33207)){
var class_list_33210 = temp__5733__auto___33207;
class_list_33210.remove(c__$1);
} else {
var class_name_33211 = dommy.core.class$(elem);
var new_class_name_33212 = dommy.utils.remove_class_str(class_name_33211,c__$1);
if((class_name_33211 === new_class_name_33212)){
} else {
dommy.core.set_class_BANG_(elem,new_class_name_33212);
}
}

return elem;
}));

(dommy.core.remove_class_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (elem,class$,classes){
var seq__32184 = cljs.core.seq(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(classes,class$));
var chunk__32185 = null;
var count__32186 = (0);
var i__32187 = (0);
while(true){
if((i__32187 < count__32186)){
var c = chunk__32185.cljs$core$IIndexed$_nth$arity$2(null,i__32187);
dommy.core.remove_class_BANG_.cljs$core$IFn$_invoke$arity$2(elem,c);


var G__33214 = seq__32184;
var G__33215 = chunk__32185;
var G__33216 = count__32186;
var G__33217 = (i__32187 + (1));
seq__32184 = G__33214;
chunk__32185 = G__33215;
count__32186 = G__33216;
i__32187 = G__33217;
continue;
} else {
var temp__5735__auto__ = cljs.core.seq(seq__32184);
if(temp__5735__auto__){
var seq__32184__$1 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__32184__$1)){
var c__4556__auto__ = cljs.core.chunk_first(seq__32184__$1);
var G__33222 = cljs.core.chunk_rest(seq__32184__$1);
var G__33223 = c__4556__auto__;
var G__33224 = cljs.core.count(c__4556__auto__);
var G__33225 = (0);
seq__32184 = G__33222;
chunk__32185 = G__33223;
count__32186 = G__33224;
i__32187 = G__33225;
continue;
} else {
var c = cljs.core.first(seq__32184__$1);
dommy.core.remove_class_BANG_.cljs$core$IFn$_invoke$arity$2(elem,c);


var G__33231 = cljs.core.next(seq__32184__$1);
var G__33232 = null;
var G__33233 = (0);
var G__33234 = (0);
seq__32184 = G__33231;
chunk__32185 = G__33232;
count__32186 = G__33233;
i__32187 = G__33234;
continue;
}
} else {
return null;
}
}
break;
}
}));

/** @this {Function} */
(dommy.core.remove_class_BANG_.cljs$lang$applyTo = (function (seq32171){
var G__32172 = cljs.core.first(seq32171);
var seq32171__$1 = cljs.core.next(seq32171);
var G__32173 = cljs.core.first(seq32171__$1);
var seq32171__$2 = cljs.core.next(seq32171__$1);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__32172,G__32173,seq32171__$2);
}));

(dommy.core.remove_class_BANG_.cljs$lang$maxFixedArity = (2));

/**
 * (toggle-class! elem class) will add-class! if elem does not have class
 * and remove-class! otherwise.
 * (toggle-class! elem class add?) will add-class! if add? is truthy,
 * otherwise it will remove-class!
 */
dommy.core.toggle_class_BANG_ = (function dommy$core$toggle_class_BANG_(var_args){
var G__32237 = arguments.length;
switch (G__32237) {
case 2:
return dommy.core.toggle_class_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return dommy.core.toggle_class_BANG_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(dommy.core.toggle_class_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (elem,c){
var c__$1 = dommy.utils.as_str(c);
var temp__5733__auto___33242 = elem.classList;
if(cljs.core.truth_(temp__5733__auto___33242)){
var class_list_33243 = temp__5733__auto___33242;
class_list_33243.toggle(c__$1);
} else {
dommy.core.toggle_class_BANG_.cljs$core$IFn$_invoke$arity$3(elem,c__$1,(!(dommy.core.has_class_QMARK_(elem,c__$1))));
}

return elem;
}));

(dommy.core.toggle_class_BANG_.cljs$core$IFn$_invoke$arity$3 = (function (elem,class$,add_QMARK_){
if(add_QMARK_){
dommy.core.add_class_BANG_.cljs$core$IFn$_invoke$arity$2(elem,class$);
} else {
dommy.core.remove_class_BANG_.cljs$core$IFn$_invoke$arity$2(elem,class$);
}

return elem;
}));

(dommy.core.toggle_class_BANG_.cljs$lang$maxFixedArity = 3);

/**
 * Display or hide the given `elem` (using display: none).
 * Takes an optional boolean `show?`
 */
dommy.core.toggle_BANG_ = (function dommy$core$toggle_BANG_(var_args){
var G__32239 = arguments.length;
switch (G__32239) {
case 2:
return dommy.core.toggle_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 1:
return dommy.core.toggle_BANG_.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(dommy.core.toggle_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (elem,show_QMARK_){
return dommy.core.set_style_BANG_.cljs$core$IFn$_invoke$arity$variadic(elem,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"display","display",242065432),((show_QMARK_)?"":"none")], 0));
}));

(dommy.core.toggle_BANG_.cljs$core$IFn$_invoke$arity$1 = (function (elem){
return dommy.core.toggle_BANG_.cljs$core$IFn$_invoke$arity$2(elem,dommy.core.hidden_QMARK_(elem));
}));

(dommy.core.toggle_BANG_.cljs$lang$maxFixedArity = 2);

dommy.core.hide_BANG_ = (function dommy$core$hide_BANG_(elem){
return dommy.core.toggle_BANG_.cljs$core$IFn$_invoke$arity$2(elem,false);
});
dommy.core.show_BANG_ = (function dommy$core$show_BANG_(elem){
return dommy.core.toggle_BANG_.cljs$core$IFn$_invoke$arity$2(elem,true);
});
dommy.core.scroll_into_view = (function dommy$core$scroll_into_view(elem,align_with_top_QMARK_){
var top = new cljs.core.Keyword(null,"top","top",-1856271961).cljs$core$IFn$_invoke$arity$1(dommy.core.bounding_client_rect(elem));
if((window.innerHeight < (top + elem.offsetHeight))){
return elem.scrollIntoView(align_with_top_QMARK_);
} else {
return null;
}
});
dommy.core.create_element = (function dommy$core$create_element(var_args){
var G__32268 = arguments.length;
switch (G__32268) {
case 1:
return dommy.core.create_element.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return dommy.core.create_element.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(dommy.core.create_element.cljs$core$IFn$_invoke$arity$1 = (function (tag){
return document.createElement(dommy.utils.as_str(tag));
}));

(dommy.core.create_element.cljs$core$IFn$_invoke$arity$2 = (function (tag_ns,tag){
return document.createElementNS(dommy.utils.as_str(tag_ns),dommy.utils.as_str(tag));
}));

(dommy.core.create_element.cljs$lang$maxFixedArity = 2);

dommy.core.create_text_node = (function dommy$core$create_text_node(text){
return document.createTextNode(text);
});
/**
 * Clears all children from `elem`
 */
dommy.core.clear_BANG_ = (function dommy$core$clear_BANG_(elem){
return dommy.core.set_html_BANG_(elem,"");
});
/**
 * Append `child` to `parent`
 */
dommy.core.append_BANG_ = (function dommy$core$append_BANG_(var_args){
var G__32276 = arguments.length;
switch (G__32276) {
case 2:
return dommy.core.append_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
var args_arr__4757__auto__ = [];
var len__4736__auto___33254 = arguments.length;
var i__4737__auto___33255 = (0);
while(true){
if((i__4737__auto___33255 < len__4736__auto___33254)){
args_arr__4757__auto__.push((arguments[i__4737__auto___33255]));

var G__33258 = (i__4737__auto___33255 + (1));
i__4737__auto___33255 = G__33258;
continue;
} else {
}
break;
}

var argseq__4758__auto__ = (new cljs.core.IndexedSeq(args_arr__4757__auto__.slice((2)),(0),null));
return dommy.core.append_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__4758__auto__);

}
});

(dommy.core.append_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (parent,child){
var G__32283 = parent;
G__32283.appendChild(child);

return G__32283;
}));

(dommy.core.append_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (parent,child,more_children){
var seq__32284_33260 = cljs.core.seq(cljs.core.cons(child,more_children));
var chunk__32285_33261 = null;
var count__32286_33262 = (0);
var i__32287_33263 = (0);
while(true){
if((i__32287_33263 < count__32286_33262)){
var c_33264 = chunk__32285_33261.cljs$core$IIndexed$_nth$arity$2(null,i__32287_33263);
dommy.core.append_BANG_.cljs$core$IFn$_invoke$arity$2(parent,c_33264);


var G__33265 = seq__32284_33260;
var G__33266 = chunk__32285_33261;
var G__33267 = count__32286_33262;
var G__33268 = (i__32287_33263 + (1));
seq__32284_33260 = G__33265;
chunk__32285_33261 = G__33266;
count__32286_33262 = G__33267;
i__32287_33263 = G__33268;
continue;
} else {
var temp__5735__auto___33269 = cljs.core.seq(seq__32284_33260);
if(temp__5735__auto___33269){
var seq__32284_33270__$1 = temp__5735__auto___33269;
if(cljs.core.chunked_seq_QMARK_(seq__32284_33270__$1)){
var c__4556__auto___33271 = cljs.core.chunk_first(seq__32284_33270__$1);
var G__33272 = cljs.core.chunk_rest(seq__32284_33270__$1);
var G__33273 = c__4556__auto___33271;
var G__33274 = cljs.core.count(c__4556__auto___33271);
var G__33275 = (0);
seq__32284_33260 = G__33272;
chunk__32285_33261 = G__33273;
count__32286_33262 = G__33274;
i__32287_33263 = G__33275;
continue;
} else {
var c_33276 = cljs.core.first(seq__32284_33270__$1);
dommy.core.append_BANG_.cljs$core$IFn$_invoke$arity$2(parent,c_33276);


var G__33277 = cljs.core.next(seq__32284_33270__$1);
var G__33278 = null;
var G__33279 = (0);
var G__33280 = (0);
seq__32284_33260 = G__33277;
chunk__32285_33261 = G__33278;
count__32286_33262 = G__33279;
i__32287_33263 = G__33280;
continue;
}
} else {
}
}
break;
}

return parent;
}));

/** @this {Function} */
(dommy.core.append_BANG_.cljs$lang$applyTo = (function (seq32273){
var G__32274 = cljs.core.first(seq32273);
var seq32273__$1 = cljs.core.next(seq32273);
var G__32275 = cljs.core.first(seq32273__$1);
var seq32273__$2 = cljs.core.next(seq32273__$1);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__32274,G__32275,seq32273__$2);
}));

(dommy.core.append_BANG_.cljs$lang$maxFixedArity = (2));

/**
 * Prepend `child` to `parent`
 */
dommy.core.prepend_BANG_ = (function dommy$core$prepend_BANG_(var_args){
var G__32309 = arguments.length;
switch (G__32309) {
case 2:
return dommy.core.prepend_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
var args_arr__4757__auto__ = [];
var len__4736__auto___33284 = arguments.length;
var i__4737__auto___33285 = (0);
while(true){
if((i__4737__auto___33285 < len__4736__auto___33284)){
args_arr__4757__auto__.push((arguments[i__4737__auto___33285]));

var G__33286 = (i__4737__auto___33285 + (1));
i__4737__auto___33285 = G__33286;
continue;
} else {
}
break;
}

var argseq__4758__auto__ = (new cljs.core.IndexedSeq(args_arr__4757__auto__.slice((2)),(0),null));
return dommy.core.prepend_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__4758__auto__);

}
});

(dommy.core.prepend_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (parent,child){
var G__32315 = parent;
G__32315.insertBefore(child,parent.firstChild);

return G__32315;
}));

(dommy.core.prepend_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (parent,child,more_children){
var seq__32316_33289 = cljs.core.seq(cljs.core.cons(child,more_children));
var chunk__32317_33290 = null;
var count__32318_33291 = (0);
var i__32319_33292 = (0);
while(true){
if((i__32319_33292 < count__32318_33291)){
var c_33294 = chunk__32317_33290.cljs$core$IIndexed$_nth$arity$2(null,i__32319_33292);
dommy.core.prepend_BANG_.cljs$core$IFn$_invoke$arity$2(parent,c_33294);


var G__33295 = seq__32316_33289;
var G__33296 = chunk__32317_33290;
var G__33297 = count__32318_33291;
var G__33298 = (i__32319_33292 + (1));
seq__32316_33289 = G__33295;
chunk__32317_33290 = G__33296;
count__32318_33291 = G__33297;
i__32319_33292 = G__33298;
continue;
} else {
var temp__5735__auto___33299 = cljs.core.seq(seq__32316_33289);
if(temp__5735__auto___33299){
var seq__32316_33300__$1 = temp__5735__auto___33299;
if(cljs.core.chunked_seq_QMARK_(seq__32316_33300__$1)){
var c__4556__auto___33301 = cljs.core.chunk_first(seq__32316_33300__$1);
var G__33304 = cljs.core.chunk_rest(seq__32316_33300__$1);
var G__33305 = c__4556__auto___33301;
var G__33306 = cljs.core.count(c__4556__auto___33301);
var G__33307 = (0);
seq__32316_33289 = G__33304;
chunk__32317_33290 = G__33305;
count__32318_33291 = G__33306;
i__32319_33292 = G__33307;
continue;
} else {
var c_33308 = cljs.core.first(seq__32316_33300__$1);
dommy.core.prepend_BANG_.cljs$core$IFn$_invoke$arity$2(parent,c_33308);


var G__33310 = cljs.core.next(seq__32316_33300__$1);
var G__33311 = null;
var G__33312 = (0);
var G__33314 = (0);
seq__32316_33289 = G__33310;
chunk__32317_33290 = G__33311;
count__32318_33291 = G__33312;
i__32319_33292 = G__33314;
continue;
}
} else {
}
}
break;
}

return parent;
}));

/** @this {Function} */
(dommy.core.prepend_BANG_.cljs$lang$applyTo = (function (seq32305){
var G__32306 = cljs.core.first(seq32305);
var seq32305__$1 = cljs.core.next(seq32305);
var G__32307 = cljs.core.first(seq32305__$1);
var seq32305__$2 = cljs.core.next(seq32305__$1);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__32306,G__32307,seq32305__$2);
}));

(dommy.core.prepend_BANG_.cljs$lang$maxFixedArity = (2));

/**
 * Insert `elem` before `other`, `other` must have a parent
 */
dommy.core.insert_before_BANG_ = (function dommy$core$insert_before_BANG_(elem,other){
var p = dommy.core.parent(other);
if(cljs.core.truth_(p)){
} else {
throw (new Error(["Assert failed: ","Target element must have a parent","\n","p"].join('')));
}

p.insertBefore(elem,other);

return elem;
});
/**
 * Insert `elem` after `other`, `other` must have a parent
 */
dommy.core.insert_after_BANG_ = (function dommy$core$insert_after_BANG_(elem,other){
var temp__5733__auto___33317 = other.nextSibling;
if(cljs.core.truth_(temp__5733__auto___33317)){
var next_33318 = temp__5733__auto___33317;
dommy.core.insert_before_BANG_(elem,next_33318);
} else {
dommy.core.append_BANG_.cljs$core$IFn$_invoke$arity$2(dommy.core.parent(other),elem);
}

return elem;
});
/**
 * Replace `elem` with `new`, return `new`
 */
dommy.core.replace_BANG_ = (function dommy$core$replace_BANG_(elem,new$){
var p = dommy.core.parent(elem);
if(cljs.core.truth_(p)){
} else {
throw (new Error(["Assert failed: ","Target element must have a parent","\n","p"].join('')));
}

p.replaceChild(new$,elem);

return new$;
});
/**
 * Replace children of `elem` with `child`
 */
dommy.core.replace_contents_BANG_ = (function dommy$core$replace_contents_BANG_(p,child){
return dommy.core.append_BANG_.cljs$core$IFn$_invoke$arity$2(dommy.core.clear_BANG_(p),child);
});
/**
 * Remove `elem` from `parent`, return `parent`
 */
dommy.core.remove_BANG_ = (function dommy$core$remove_BANG_(var_args){
var G__32373 = arguments.length;
switch (G__32373) {
case 1:
return dommy.core.remove_BANG_.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return dommy.core.remove_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(dommy.core.remove_BANG_.cljs$core$IFn$_invoke$arity$1 = (function (elem){
var p = dommy.core.parent(elem);
if(cljs.core.truth_(p)){
} else {
throw (new Error(["Assert failed: ","Target element must have a parent","\n","p"].join('')));
}

return dommy.core.remove_BANG_.cljs$core$IFn$_invoke$arity$2(p,elem);
}));

(dommy.core.remove_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (p,elem){
var G__32394 = p;
G__32394.removeChild(elem);

return G__32394;
}));

(dommy.core.remove_BANG_.cljs$lang$maxFixedArity = 2);

dommy.core.special_listener_makers = cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p__32395){
var vec__32396 = p__32395;
var special_mouse_event = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32396,(0),null);
var real_mouse_event = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32396,(1),null);
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [special_mouse_event,cljs.core.PersistentArrayMap.createAsIfByAssoc([real_mouse_event,(function (f){
return (function (event){
var related_target = event.relatedTarget;
var listener_target = (function (){var or__4126__auto__ = event.selectedTarget;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return event.currentTarget;
}
})();
if(cljs.core.truth_((function (){var and__4115__auto__ = related_target;
if(cljs.core.truth_(and__4115__auto__)){
return dommy.core.descendant_QMARK_(related_target,listener_target);
} else {
return and__4115__auto__;
}
})())){
return null;
} else {
return (f.cljs$core$IFn$_invoke$arity$1 ? f.cljs$core$IFn$_invoke$arity$1(event) : f.call(null,event));
}
});
})])], null);
}),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"mouseenter","mouseenter",-1792413560),new cljs.core.Keyword(null,"mouseover","mouseover",-484272303),new cljs.core.Keyword(null,"mouseleave","mouseleave",531566580),new cljs.core.Keyword(null,"mouseout","mouseout",2049446890)], null)));
/**
 * fires f if event.target is found with `selector`
 */
dommy.core.live_listener = (function dommy$core$live_listener(elem,selector,f){
return (function (event){
var selected_target = dommy.core.closest.cljs$core$IFn$_invoke$arity$3(elem,event.target,selector);
if(cljs.core.truth_((function (){var and__4115__auto__ = selected_target;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(dommy.core.attr(selected_target,new cljs.core.Keyword(null,"disabled","disabled",-1529784218)));
} else {
return and__4115__auto__;
}
})())){
(event.selectedTarget = selected_target);

return (f.cljs$core$IFn$_invoke$arity$1 ? f.cljs$core$IFn$_invoke$arity$1(event) : f.call(null,event));
} else {
return null;
}
});
});
/**
 * Returns a nested map of event listeners on `elem`
 */
dommy.core.event_listeners = (function dommy$core$event_listeners(elem){
var or__4126__auto__ = elem.dommyEventListeners;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.PersistentArrayMap.EMPTY;
}
});
dommy.core.update_event_listeners_BANG_ = (function dommy$core$update_event_listeners_BANG_(var_args){
var args__4742__auto__ = [];
var len__4736__auto___33340 = arguments.length;
var i__4737__auto___33341 = (0);
while(true){
if((i__4737__auto___33341 < len__4736__auto___33340)){
args__4742__auto__.push((arguments[i__4737__auto___33341]));

var G__33342 = (i__4737__auto___33341 + (1));
i__4737__auto___33341 = G__33342;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((2) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((2)),(0),null)):null);
return dommy.core.update_event_listeners_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__4743__auto__);
});

(dommy.core.update_event_listeners_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (elem,f,args){
var elem__$1 = elem;
return (elem__$1.dommyEventListeners = cljs.core.apply.cljs$core$IFn$_invoke$arity$3(f,dommy.core.event_listeners(elem__$1),args));
}));

(dommy.core.update_event_listeners_BANG_.cljs$lang$maxFixedArity = (2));

/** @this {Function} */
(dommy.core.update_event_listeners_BANG_.cljs$lang$applyTo = (function (seq32399){
var G__32400 = cljs.core.first(seq32399);
var seq32399__$1 = cljs.core.next(seq32399);
var G__32401 = cljs.core.first(seq32399__$1);
var seq32399__$2 = cljs.core.next(seq32399__$1);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__32400,G__32401,seq32399__$2);
}));

dommy.core.elem_and_selector = (function dommy$core$elem_and_selector(elem_sel){
if(cljs.core.sequential_QMARK_(elem_sel)){
return cljs.core.juxt.cljs$core$IFn$_invoke$arity$2(cljs.core.first,cljs.core.rest)(elem_sel);
} else {
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [elem_sel,null], null);
}
});
/**
 * Adds `f` as a listener for events of type `event-type` on
 * `elem-sel`, which must either be a DOM node, or a sequence
 * whose first item is a DOM node.
 * 
 * In other words, the call to `listen!` can take two forms:
 * 
 * If `elem-sel` is a DOM node, i.e., you're doing something like:
 * 
 *     (listen! elem :click click-handler)
 * 
 * then `click-handler` will be set as a listener for `click` events
 * on the `elem`.
 * 
 * If `elem-sel` is a sequence:
 * 
 *     (listen! [elem :.selector.for :.some.descendants] :click click-handler)
 * 
 * then `click-handler` will be set as a listener for `click` events
 * on descendants of `elem` that match the selector
 * 
 * Also accepts any number of event-type and handler pairs for setting
 * multiple listeners at once:
 * 
 *     (listen! some-elem :click click-handler :hover hover-handler)
 */
dommy.core.listen_BANG_ = (function dommy$core$listen_BANG_(var_args){
var args__4742__auto__ = [];
var len__4736__auto___33348 = arguments.length;
var i__4737__auto___33349 = (0);
while(true){
if((i__4737__auto___33349 < len__4736__auto___33348)){
args__4742__auto__.push((arguments[i__4737__auto___33349]));

var G__33351 = (i__4737__auto___33349 + (1));
i__4737__auto___33349 = G__33351;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return dommy.core.listen_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(dommy.core.listen_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (elem_sel,type_fs){
if(cljs.core.even_QMARK_(cljs.core.count(type_fs))){
} else {
throw (new Error("Assert failed: (even? (count type-fs))"));
}

var vec__32420_33353 = dommy.core.elem_and_selector(elem_sel);
var elem_33354 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32420_33353,(0),null);
var selector_33355 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32420_33353,(1),null);
var seq__32423_33356 = cljs.core.seq(cljs.core.partition.cljs$core$IFn$_invoke$arity$2((2),type_fs));
var chunk__32430_33357 = null;
var count__32431_33358 = (0);
var i__32432_33359 = (0);
while(true){
if((i__32432_33359 < count__32431_33358)){
var vec__32562_33361 = chunk__32430_33357.cljs$core$IIndexed$_nth$arity$2(null,i__32432_33359);
var orig_type_33362 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32562_33361,(0),null);
var f_33363 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32562_33361,(1),null);
var seq__32433_33364 = cljs.core.seq(cljs.core.get.cljs$core$IFn$_invoke$arity$3(dommy.core.special_listener_makers,orig_type_33362,cljs.core.PersistentArrayMap.createAsIfByAssoc([orig_type_33362,cljs.core.identity])));
var chunk__32435_33365 = null;
var count__32436_33366 = (0);
var i__32437_33367 = (0);
while(true){
if((i__32437_33367 < count__32436_33366)){
var vec__32592_33369 = chunk__32435_33365.cljs$core$IIndexed$_nth$arity$2(null,i__32437_33367);
var actual_type_33370 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32592_33369,(0),null);
var factory_33371 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32592_33369,(1),null);
var canonical_f_33372 = (function (){var G__32597 = (factory_33371.cljs$core$IFn$_invoke$arity$1 ? factory_33371.cljs$core$IFn$_invoke$arity$1(f_33363) : factory_33371.call(null,f_33363));
var fexpr__32596 = (cljs.core.truth_(selector_33355)?cljs.core.partial.cljs$core$IFn$_invoke$arity$3(dommy.core.live_listener,elem_33354,selector_33355):cljs.core.identity);
return (fexpr__32596.cljs$core$IFn$_invoke$arity$1 ? fexpr__32596.cljs$core$IFn$_invoke$arity$1(G__32597) : fexpr__32596.call(null,G__32597));
})();
dommy.core.update_event_listeners_BANG_.cljs$core$IFn$_invoke$arity$variadic(elem_33354,cljs.core.assoc_in,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [selector_33355,actual_type_33370,f_33363], null),canonical_f_33372], 0));

if(cljs.core.truth_(elem_33354.addEventListener)){
elem_33354.addEventListener(cljs.core.name(actual_type_33370),canonical_f_33372);
} else {
elem_33354.attachEvent(cljs.core.name(actual_type_33370),canonical_f_33372);
}


var G__33374 = seq__32433_33364;
var G__33375 = chunk__32435_33365;
var G__33376 = count__32436_33366;
var G__33377 = (i__32437_33367 + (1));
seq__32433_33364 = G__33374;
chunk__32435_33365 = G__33375;
count__32436_33366 = G__33376;
i__32437_33367 = G__33377;
continue;
} else {
var temp__5735__auto___33378 = cljs.core.seq(seq__32433_33364);
if(temp__5735__auto___33378){
var seq__32433_33379__$1 = temp__5735__auto___33378;
if(cljs.core.chunked_seq_QMARK_(seq__32433_33379__$1)){
var c__4556__auto___33380 = cljs.core.chunk_first(seq__32433_33379__$1);
var G__33381 = cljs.core.chunk_rest(seq__32433_33379__$1);
var G__33382 = c__4556__auto___33380;
var G__33383 = cljs.core.count(c__4556__auto___33380);
var G__33384 = (0);
seq__32433_33364 = G__33381;
chunk__32435_33365 = G__33382;
count__32436_33366 = G__33383;
i__32437_33367 = G__33384;
continue;
} else {
var vec__32602_33385 = cljs.core.first(seq__32433_33379__$1);
var actual_type_33386 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32602_33385,(0),null);
var factory_33387 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32602_33385,(1),null);
var canonical_f_33390 = (function (){var G__32608 = (factory_33387.cljs$core$IFn$_invoke$arity$1 ? factory_33387.cljs$core$IFn$_invoke$arity$1(f_33363) : factory_33387.call(null,f_33363));
var fexpr__32607 = (cljs.core.truth_(selector_33355)?cljs.core.partial.cljs$core$IFn$_invoke$arity$3(dommy.core.live_listener,elem_33354,selector_33355):cljs.core.identity);
return (fexpr__32607.cljs$core$IFn$_invoke$arity$1 ? fexpr__32607.cljs$core$IFn$_invoke$arity$1(G__32608) : fexpr__32607.call(null,G__32608));
})();
dommy.core.update_event_listeners_BANG_.cljs$core$IFn$_invoke$arity$variadic(elem_33354,cljs.core.assoc_in,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [selector_33355,actual_type_33386,f_33363], null),canonical_f_33390], 0));

if(cljs.core.truth_(elem_33354.addEventListener)){
elem_33354.addEventListener(cljs.core.name(actual_type_33386),canonical_f_33390);
} else {
elem_33354.attachEvent(cljs.core.name(actual_type_33386),canonical_f_33390);
}


var G__33394 = cljs.core.next(seq__32433_33379__$1);
var G__33395 = null;
var G__33396 = (0);
var G__33397 = (0);
seq__32433_33364 = G__33394;
chunk__32435_33365 = G__33395;
count__32436_33366 = G__33396;
i__32437_33367 = G__33397;
continue;
}
} else {
}
}
break;
}

var G__33398 = seq__32423_33356;
var G__33399 = chunk__32430_33357;
var G__33400 = count__32431_33358;
var G__33401 = (i__32432_33359 + (1));
seq__32423_33356 = G__33398;
chunk__32430_33357 = G__33399;
count__32431_33358 = G__33400;
i__32432_33359 = G__33401;
continue;
} else {
var temp__5735__auto___33402 = cljs.core.seq(seq__32423_33356);
if(temp__5735__auto___33402){
var seq__32423_33403__$1 = temp__5735__auto___33402;
if(cljs.core.chunked_seq_QMARK_(seq__32423_33403__$1)){
var c__4556__auto___33404 = cljs.core.chunk_first(seq__32423_33403__$1);
var G__33405 = cljs.core.chunk_rest(seq__32423_33403__$1);
var G__33406 = c__4556__auto___33404;
var G__33407 = cljs.core.count(c__4556__auto___33404);
var G__33408 = (0);
seq__32423_33356 = G__33405;
chunk__32430_33357 = G__33406;
count__32431_33358 = G__33407;
i__32432_33359 = G__33408;
continue;
} else {
var vec__32610_33409 = cljs.core.first(seq__32423_33403__$1);
var orig_type_33410 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32610_33409,(0),null);
var f_33411 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32610_33409,(1),null);
var seq__32424_33412 = cljs.core.seq(cljs.core.get.cljs$core$IFn$_invoke$arity$3(dommy.core.special_listener_makers,orig_type_33410,cljs.core.PersistentArrayMap.createAsIfByAssoc([orig_type_33410,cljs.core.identity])));
var chunk__32426_33413 = null;
var count__32427_33414 = (0);
var i__32428_33415 = (0);
while(true){
if((i__32428_33415 < count__32427_33414)){
var vec__32634_33418 = chunk__32426_33413.cljs$core$IIndexed$_nth$arity$2(null,i__32428_33415);
var actual_type_33419 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32634_33418,(0),null);
var factory_33420 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32634_33418,(1),null);
var canonical_f_33422 = (function (){var G__32640 = (factory_33420.cljs$core$IFn$_invoke$arity$1 ? factory_33420.cljs$core$IFn$_invoke$arity$1(f_33411) : factory_33420.call(null,f_33411));
var fexpr__32639 = (cljs.core.truth_(selector_33355)?cljs.core.partial.cljs$core$IFn$_invoke$arity$3(dommy.core.live_listener,elem_33354,selector_33355):cljs.core.identity);
return (fexpr__32639.cljs$core$IFn$_invoke$arity$1 ? fexpr__32639.cljs$core$IFn$_invoke$arity$1(G__32640) : fexpr__32639.call(null,G__32640));
})();
dommy.core.update_event_listeners_BANG_.cljs$core$IFn$_invoke$arity$variadic(elem_33354,cljs.core.assoc_in,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [selector_33355,actual_type_33419,f_33411], null),canonical_f_33422], 0));

if(cljs.core.truth_(elem_33354.addEventListener)){
elem_33354.addEventListener(cljs.core.name(actual_type_33419),canonical_f_33422);
} else {
elem_33354.attachEvent(cljs.core.name(actual_type_33419),canonical_f_33422);
}


var G__33425 = seq__32424_33412;
var G__33426 = chunk__32426_33413;
var G__33427 = count__32427_33414;
var G__33428 = (i__32428_33415 + (1));
seq__32424_33412 = G__33425;
chunk__32426_33413 = G__33426;
count__32427_33414 = G__33427;
i__32428_33415 = G__33428;
continue;
} else {
var temp__5735__auto___33429__$1 = cljs.core.seq(seq__32424_33412);
if(temp__5735__auto___33429__$1){
var seq__32424_33430__$1 = temp__5735__auto___33429__$1;
if(cljs.core.chunked_seq_QMARK_(seq__32424_33430__$1)){
var c__4556__auto___33431 = cljs.core.chunk_first(seq__32424_33430__$1);
var G__33432 = cljs.core.chunk_rest(seq__32424_33430__$1);
var G__33433 = c__4556__auto___33431;
var G__33434 = cljs.core.count(c__4556__auto___33431);
var G__33435 = (0);
seq__32424_33412 = G__33432;
chunk__32426_33413 = G__33433;
count__32427_33414 = G__33434;
i__32428_33415 = G__33435;
continue;
} else {
var vec__32645_33436 = cljs.core.first(seq__32424_33430__$1);
var actual_type_33437 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32645_33436,(0),null);
var factory_33438 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32645_33436,(1),null);
var canonical_f_33439 = (function (){var G__32651 = (factory_33438.cljs$core$IFn$_invoke$arity$1 ? factory_33438.cljs$core$IFn$_invoke$arity$1(f_33411) : factory_33438.call(null,f_33411));
var fexpr__32650 = (cljs.core.truth_(selector_33355)?cljs.core.partial.cljs$core$IFn$_invoke$arity$3(dommy.core.live_listener,elem_33354,selector_33355):cljs.core.identity);
return (fexpr__32650.cljs$core$IFn$_invoke$arity$1 ? fexpr__32650.cljs$core$IFn$_invoke$arity$1(G__32651) : fexpr__32650.call(null,G__32651));
})();
dommy.core.update_event_listeners_BANG_.cljs$core$IFn$_invoke$arity$variadic(elem_33354,cljs.core.assoc_in,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [selector_33355,actual_type_33437,f_33411], null),canonical_f_33439], 0));

if(cljs.core.truth_(elem_33354.addEventListener)){
elem_33354.addEventListener(cljs.core.name(actual_type_33437),canonical_f_33439);
} else {
elem_33354.attachEvent(cljs.core.name(actual_type_33437),canonical_f_33439);
}


var G__33442 = cljs.core.next(seq__32424_33430__$1);
var G__33443 = null;
var G__33444 = (0);
var G__33445 = (0);
seq__32424_33412 = G__33442;
chunk__32426_33413 = G__33443;
count__32427_33414 = G__33444;
i__32428_33415 = G__33445;
continue;
}
} else {
}
}
break;
}

var G__33446 = cljs.core.next(seq__32423_33403__$1);
var G__33447 = null;
var G__33448 = (0);
var G__33449 = (0);
seq__32423_33356 = G__33446;
chunk__32430_33357 = G__33447;
count__32431_33358 = G__33448;
i__32432_33359 = G__33449;
continue;
}
} else {
}
}
break;
}

return elem_sel;
}));

(dommy.core.listen_BANG_.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(dommy.core.listen_BANG_.cljs$lang$applyTo = (function (seq32417){
var G__32418 = cljs.core.first(seq32417);
var seq32417__$1 = cljs.core.next(seq32417);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__32418,seq32417__$1);
}));

/**
 * Removes event listener for the element defined in `elem-sel`,
 * which is the same format as listen!.
 * 
 *   The following forms are allowed, and will remove all handlers
 *   that match the parameters passed in:
 * 
 *    (unlisten! [elem :.selector] :click event-listener)
 * 
 *    (unlisten! [elem :.selector]
 *      :click event-listener
 *      :mouseover other-event-listener)
 */
dommy.core.unlisten_BANG_ = (function dommy$core$unlisten_BANG_(var_args){
var args__4742__auto__ = [];
var len__4736__auto___33453 = arguments.length;
var i__4737__auto___33454 = (0);
while(true){
if((i__4737__auto___33454 < len__4736__auto___33453)){
args__4742__auto__.push((arguments[i__4737__auto___33454]));

var G__33455 = (i__4737__auto___33454 + (1));
i__4737__auto___33454 = G__33455;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return dommy.core.unlisten_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(dommy.core.unlisten_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (elem_sel,type_fs){
if(cljs.core.even_QMARK_(cljs.core.count(type_fs))){
} else {
throw (new Error("Assert failed: (even? (count type-fs))"));
}

var vec__32672_33456 = dommy.core.elem_and_selector(elem_sel);
var elem_33457 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32672_33456,(0),null);
var selector_33458 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32672_33456,(1),null);
var seq__32675_33461 = cljs.core.seq(cljs.core.partition.cljs$core$IFn$_invoke$arity$2((2),type_fs));
var chunk__32682_33462 = null;
var count__32683_33463 = (0);
var i__32684_33464 = (0);
while(true){
if((i__32684_33464 < count__32683_33463)){
var vec__32756_33466 = chunk__32682_33462.cljs$core$IIndexed$_nth$arity$2(null,i__32684_33464);
var orig_type_33467 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32756_33466,(0),null);
var f_33468 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32756_33466,(1),null);
var seq__32685_33469 = cljs.core.seq(cljs.core.get.cljs$core$IFn$_invoke$arity$3(dommy.core.special_listener_makers,orig_type_33467,cljs.core.PersistentArrayMap.createAsIfByAssoc([orig_type_33467,cljs.core.identity])));
var chunk__32687_33470 = null;
var count__32688_33471 = (0);
var i__32689_33472 = (0);
while(true){
if((i__32689_33472 < count__32688_33471)){
var vec__32769_33474 = chunk__32687_33470.cljs$core$IIndexed$_nth$arity$2(null,i__32689_33472);
var actual_type_33475 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32769_33474,(0),null);
var __33476 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32769_33474,(1),null);
var keys_33477 = new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [selector_33458,actual_type_33475,f_33468], null);
var canonical_f_33478 = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(dommy.core.event_listeners(elem_33457),keys_33477);
dommy.core.update_event_listeners_BANG_.cljs$core$IFn$_invoke$arity$variadic(elem_33457,dommy.utils.dissoc_in,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([keys_33477], 0));

if(cljs.core.truth_(elem_33457.removeEventListener)){
elem_33457.removeEventListener(cljs.core.name(actual_type_33475),canonical_f_33478);
} else {
elem_33457.detachEvent(cljs.core.name(actual_type_33475),canonical_f_33478);
}


var G__33479 = seq__32685_33469;
var G__33480 = chunk__32687_33470;
var G__33481 = count__32688_33471;
var G__33482 = (i__32689_33472 + (1));
seq__32685_33469 = G__33479;
chunk__32687_33470 = G__33480;
count__32688_33471 = G__33481;
i__32689_33472 = G__33482;
continue;
} else {
var temp__5735__auto___33483 = cljs.core.seq(seq__32685_33469);
if(temp__5735__auto___33483){
var seq__32685_33484__$1 = temp__5735__auto___33483;
if(cljs.core.chunked_seq_QMARK_(seq__32685_33484__$1)){
var c__4556__auto___33485 = cljs.core.chunk_first(seq__32685_33484__$1);
var G__33486 = cljs.core.chunk_rest(seq__32685_33484__$1);
var G__33487 = c__4556__auto___33485;
var G__33488 = cljs.core.count(c__4556__auto___33485);
var G__33489 = (0);
seq__32685_33469 = G__33486;
chunk__32687_33470 = G__33487;
count__32688_33471 = G__33488;
i__32689_33472 = G__33489;
continue;
} else {
var vec__32774_33490 = cljs.core.first(seq__32685_33484__$1);
var actual_type_33491 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32774_33490,(0),null);
var __33492 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32774_33490,(1),null);
var keys_33493 = new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [selector_33458,actual_type_33491,f_33468], null);
var canonical_f_33494 = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(dommy.core.event_listeners(elem_33457),keys_33493);
dommy.core.update_event_listeners_BANG_.cljs$core$IFn$_invoke$arity$variadic(elem_33457,dommy.utils.dissoc_in,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([keys_33493], 0));

if(cljs.core.truth_(elem_33457.removeEventListener)){
elem_33457.removeEventListener(cljs.core.name(actual_type_33491),canonical_f_33494);
} else {
elem_33457.detachEvent(cljs.core.name(actual_type_33491),canonical_f_33494);
}


var G__33495 = cljs.core.next(seq__32685_33484__$1);
var G__33496 = null;
var G__33497 = (0);
var G__33498 = (0);
seq__32685_33469 = G__33495;
chunk__32687_33470 = G__33496;
count__32688_33471 = G__33497;
i__32689_33472 = G__33498;
continue;
}
} else {
}
}
break;
}

var G__33499 = seq__32675_33461;
var G__33500 = chunk__32682_33462;
var G__33501 = count__32683_33463;
var G__33502 = (i__32684_33464 + (1));
seq__32675_33461 = G__33499;
chunk__32682_33462 = G__33500;
count__32683_33463 = G__33501;
i__32684_33464 = G__33502;
continue;
} else {
var temp__5735__auto___33503 = cljs.core.seq(seq__32675_33461);
if(temp__5735__auto___33503){
var seq__32675_33504__$1 = temp__5735__auto___33503;
if(cljs.core.chunked_seq_QMARK_(seq__32675_33504__$1)){
var c__4556__auto___33505 = cljs.core.chunk_first(seq__32675_33504__$1);
var G__33506 = cljs.core.chunk_rest(seq__32675_33504__$1);
var G__33507 = c__4556__auto___33505;
var G__33508 = cljs.core.count(c__4556__auto___33505);
var G__33509 = (0);
seq__32675_33461 = G__33506;
chunk__32682_33462 = G__33507;
count__32683_33463 = G__33508;
i__32684_33464 = G__33509;
continue;
} else {
var vec__32779_33510 = cljs.core.first(seq__32675_33504__$1);
var orig_type_33511 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32779_33510,(0),null);
var f_33512 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32779_33510,(1),null);
var seq__32676_33513 = cljs.core.seq(cljs.core.get.cljs$core$IFn$_invoke$arity$3(dommy.core.special_listener_makers,orig_type_33511,cljs.core.PersistentArrayMap.createAsIfByAssoc([orig_type_33511,cljs.core.identity])));
var chunk__32678_33514 = null;
var count__32679_33515 = (0);
var i__32680_33516 = (0);
while(true){
if((i__32680_33516 < count__32679_33515)){
var vec__32804_33518 = chunk__32678_33514.cljs$core$IIndexed$_nth$arity$2(null,i__32680_33516);
var actual_type_33519 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32804_33518,(0),null);
var __33520 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32804_33518,(1),null);
var keys_33521 = new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [selector_33458,actual_type_33519,f_33512], null);
var canonical_f_33522 = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(dommy.core.event_listeners(elem_33457),keys_33521);
dommy.core.update_event_listeners_BANG_.cljs$core$IFn$_invoke$arity$variadic(elem_33457,dommy.utils.dissoc_in,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([keys_33521], 0));

if(cljs.core.truth_(elem_33457.removeEventListener)){
elem_33457.removeEventListener(cljs.core.name(actual_type_33519),canonical_f_33522);
} else {
elem_33457.detachEvent(cljs.core.name(actual_type_33519),canonical_f_33522);
}


var G__33524 = seq__32676_33513;
var G__33525 = chunk__32678_33514;
var G__33526 = count__32679_33515;
var G__33527 = (i__32680_33516 + (1));
seq__32676_33513 = G__33524;
chunk__32678_33514 = G__33525;
count__32679_33515 = G__33526;
i__32680_33516 = G__33527;
continue;
} else {
var temp__5735__auto___33528__$1 = cljs.core.seq(seq__32676_33513);
if(temp__5735__auto___33528__$1){
var seq__32676_33530__$1 = temp__5735__auto___33528__$1;
if(cljs.core.chunked_seq_QMARK_(seq__32676_33530__$1)){
var c__4556__auto___33531 = cljs.core.chunk_first(seq__32676_33530__$1);
var G__33532 = cljs.core.chunk_rest(seq__32676_33530__$1);
var G__33533 = c__4556__auto___33531;
var G__33534 = cljs.core.count(c__4556__auto___33531);
var G__33535 = (0);
seq__32676_33513 = G__33532;
chunk__32678_33514 = G__33533;
count__32679_33515 = G__33534;
i__32680_33516 = G__33535;
continue;
} else {
var vec__32811_33536 = cljs.core.first(seq__32676_33530__$1);
var actual_type_33537 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32811_33536,(0),null);
var __33538 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32811_33536,(1),null);
var keys_33539 = new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [selector_33458,actual_type_33537,f_33512], null);
var canonical_f_33540 = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(dommy.core.event_listeners(elem_33457),keys_33539);
dommy.core.update_event_listeners_BANG_.cljs$core$IFn$_invoke$arity$variadic(elem_33457,dommy.utils.dissoc_in,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([keys_33539], 0));

if(cljs.core.truth_(elem_33457.removeEventListener)){
elem_33457.removeEventListener(cljs.core.name(actual_type_33537),canonical_f_33540);
} else {
elem_33457.detachEvent(cljs.core.name(actual_type_33537),canonical_f_33540);
}


var G__33542 = cljs.core.next(seq__32676_33530__$1);
var G__33543 = null;
var G__33544 = (0);
var G__33545 = (0);
seq__32676_33513 = G__33542;
chunk__32678_33514 = G__33543;
count__32679_33515 = G__33544;
i__32680_33516 = G__33545;
continue;
}
} else {
}
}
break;
}

var G__33546 = cljs.core.next(seq__32675_33504__$1);
var G__33547 = null;
var G__33548 = (0);
var G__33549 = (0);
seq__32675_33461 = G__33546;
chunk__32682_33462 = G__33547;
count__32683_33463 = G__33548;
i__32684_33464 = G__33549;
continue;
}
} else {
}
}
break;
}

return elem_sel;
}));

(dommy.core.unlisten_BANG_.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(dommy.core.unlisten_BANG_.cljs$lang$applyTo = (function (seq32662){
var G__32664 = cljs.core.first(seq32662);
var seq32662__$1 = cljs.core.next(seq32662);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__32664,seq32662__$1);
}));

/**
 * Behaves like `listen!`, but removes the listener after the first event occurs.
 */
dommy.core.listen_once_BANG_ = (function dommy$core$listen_once_BANG_(var_args){
var args__4742__auto__ = [];
var len__4736__auto___33554 = arguments.length;
var i__4737__auto___33555 = (0);
while(true){
if((i__4737__auto___33555 < len__4736__auto___33554)){
args__4742__auto__.push((arguments[i__4737__auto___33555]));

var G__33556 = (i__4737__auto___33555 + (1));
i__4737__auto___33555 = G__33556;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return dommy.core.listen_once_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(dommy.core.listen_once_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (elem_sel,type_fs){
if(cljs.core.even_QMARK_(cljs.core.count(type_fs))){
} else {
throw (new Error("Assert failed: (even? (count type-fs))"));
}

var vec__32824_33561 = dommy.core.elem_and_selector(elem_sel);
var elem_33562 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32824_33561,(0),null);
var selector_33563 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32824_33561,(1),null);
var seq__32827_33564 = cljs.core.seq(cljs.core.partition.cljs$core$IFn$_invoke$arity$2((2),type_fs));
var chunk__32828_33565 = null;
var count__32829_33566 = (0);
var i__32830_33567 = (0);
while(true){
if((i__32830_33567 < count__32829_33566)){
var vec__32853_33568 = chunk__32828_33565.cljs$core$IIndexed$_nth$arity$2(null,i__32830_33567);
var type_33569 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32853_33568,(0),null);
var f_33570 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32853_33568,(1),null);
dommy.core.listen_BANG_.cljs$core$IFn$_invoke$arity$variadic(elem_sel,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([type_33569,((function (seq__32827_33564,chunk__32828_33565,count__32829_33566,i__32830_33567,vec__32853_33568,type_33569,f_33570,vec__32824_33561,elem_33562,selector_33563){
return (function dommy$core$this_fn(e){
dommy.core.unlisten_BANG_.cljs$core$IFn$_invoke$arity$variadic(elem_sel,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([type_33569,dommy$core$this_fn], 0));

return (f_33570.cljs$core$IFn$_invoke$arity$1 ? f_33570.cljs$core$IFn$_invoke$arity$1(e) : f_33570.call(null,e));
});})(seq__32827_33564,chunk__32828_33565,count__32829_33566,i__32830_33567,vec__32853_33568,type_33569,f_33570,vec__32824_33561,elem_33562,selector_33563))
], 0));


var G__33576 = seq__32827_33564;
var G__33577 = chunk__32828_33565;
var G__33578 = count__32829_33566;
var G__33579 = (i__32830_33567 + (1));
seq__32827_33564 = G__33576;
chunk__32828_33565 = G__33577;
count__32829_33566 = G__33578;
i__32830_33567 = G__33579;
continue;
} else {
var temp__5735__auto___33581 = cljs.core.seq(seq__32827_33564);
if(temp__5735__auto___33581){
var seq__32827_33582__$1 = temp__5735__auto___33581;
if(cljs.core.chunked_seq_QMARK_(seq__32827_33582__$1)){
var c__4556__auto___33583 = cljs.core.chunk_first(seq__32827_33582__$1);
var G__33584 = cljs.core.chunk_rest(seq__32827_33582__$1);
var G__33585 = c__4556__auto___33583;
var G__33586 = cljs.core.count(c__4556__auto___33583);
var G__33587 = (0);
seq__32827_33564 = G__33584;
chunk__32828_33565 = G__33585;
count__32829_33566 = G__33586;
i__32830_33567 = G__33587;
continue;
} else {
var vec__32859_33588 = cljs.core.first(seq__32827_33582__$1);
var type_33589 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32859_33588,(0),null);
var f_33590 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__32859_33588,(1),null);
dommy.core.listen_BANG_.cljs$core$IFn$_invoke$arity$variadic(elem_sel,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([type_33589,((function (seq__32827_33564,chunk__32828_33565,count__32829_33566,i__32830_33567,vec__32859_33588,type_33589,f_33590,seq__32827_33582__$1,temp__5735__auto___33581,vec__32824_33561,elem_33562,selector_33563){
return (function dommy$core$this_fn(e){
dommy.core.unlisten_BANG_.cljs$core$IFn$_invoke$arity$variadic(elem_sel,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([type_33589,dommy$core$this_fn], 0));

return (f_33590.cljs$core$IFn$_invoke$arity$1 ? f_33590.cljs$core$IFn$_invoke$arity$1(e) : f_33590.call(null,e));
});})(seq__32827_33564,chunk__32828_33565,count__32829_33566,i__32830_33567,vec__32859_33588,type_33589,f_33590,seq__32827_33582__$1,temp__5735__auto___33581,vec__32824_33561,elem_33562,selector_33563))
], 0));


var G__33594 = cljs.core.next(seq__32827_33582__$1);
var G__33595 = null;
var G__33596 = (0);
var G__33597 = (0);
seq__32827_33564 = G__33594;
chunk__32828_33565 = G__33595;
count__32829_33566 = G__33596;
i__32830_33567 = G__33597;
continue;
}
} else {
}
}
break;
}

return elem_sel;
}));

(dommy.core.listen_once_BANG_.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(dommy.core.listen_once_BANG_.cljs$lang$applyTo = (function (seq32820){
var G__32821 = cljs.core.first(seq32820);
var seq32820__$1 = cljs.core.next(seq32820);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__32821,seq32820__$1);
}));


//# sourceMappingURL=dommy.core.js.map
