goog.provide('clojure.contrib.humanize');
clojure.contrib.humanize.num_format = (function clojure$contrib$humanize$num_format(p1__51989_SHARP_,p2__51990_SHARP_){
return goog.string.format(p1__51989_SHARP_,p2__51990_SHARP_);
});
clojure.contrib.humanize.expt = Math.pow;
clojure.contrib.humanize.floor = Math.floor;
clojure.contrib.humanize.round = Math.round;
clojure.contrib.humanize.abs = Math.abs;
clojure.contrib.humanize.log = Math.log;
clojure.contrib.humanize.rounding_const = (1000000);
clojure.contrib.humanize.log10 = (function (){var or__4126__auto__ = Math.log10;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return (function (p1__51993_SHARP_){
return (Math.round((clojure.contrib.humanize.rounding_const * (Math.log(p1__51993_SHARP_) / Math.LN10))) / clojure.contrib.humanize.rounding_const);
});
}
})();
clojure.contrib.humanize.char__GT_int = (function clojure$contrib$humanize$char__GT_int(p1__51995_SHARP_){
return (p1__51995_SHARP_ | (0));
});
/**
 * Converts an integer to a string containing commas. every three digits.
 * For example, 3000 becomes '3,000' and 45000 becomes '45,000'. 
 */
clojure.contrib.humanize.intcomma = (function clojure$contrib$humanize$intcomma(num){
var decimal = clojure.contrib.humanize.abs((num | (0)));
var sign = (((num < (0)))?"-":"");
var repr = cljs.core.str.cljs$core$IFn$_invoke$arity$1(decimal);
var repr_len = ((repr).length);
var partitioned = new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.subs.cljs$core$IFn$_invoke$arity$3(repr,(0),cljs.core.rem(repr_len,(3))),cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__51996_SHARP_){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.str,p1__51996_SHARP_);
}),cljs.core.partition.cljs$core$IFn$_invoke$arity$2((3),cljs.core.subs.cljs$core$IFn$_invoke$arity$2(repr,cljs.core.rem(repr_len,(3)))))], null);
var partitioned__$1 = cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.empty_QMARK_,cljs.core.flatten(partitioned));
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(cljs.core.str,sign,cljs.core.interpose.cljs$core$IFn$_invoke$arity$2(",",partitioned__$1));
});
/**
 * Converts an integer to its ordinal as a string. 1 is '1st', 2 is '2nd',
 * 3 is '3rd', etc.
 */
clojure.contrib.humanize.ordinal = (function clojure$contrib$humanize$ordinal(num){
var ordinals = new cljs.core.PersistentVector(null, 10, 5, cljs.core.PersistentVector.EMPTY_NODE, ["th","st","nd","rd","th","th","th","th","th","th"], null);
var remainder_100 = cljs.core.rem(num,(100));
var remainder_10 = cljs.core.rem(num,(10));
if(cljs.core.truth_(clojure.contrib.inflect.in_QMARK_(remainder_100,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [(11),(12),(13)], null)))){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(num),cljs.core.str.cljs$core$IFn$_invoke$arity$1((ordinals.cljs$core$IFn$_invoke$arity$1 ? ordinals.cljs$core$IFn$_invoke$arity$1((0)) : ordinals.call(null,(0))))].join('');
} else {
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(num),cljs.core.str.cljs$core$IFn$_invoke$arity$1((ordinals.cljs$core$IFn$_invoke$arity$1 ? ordinals.cljs$core$IFn$_invoke$arity$1(remainder_10) : ordinals.call(null,remainder_10)))].join('');
}
});
clojure.contrib.humanize.logn = (function clojure$contrib$humanize$logn(num,base){
return (clojure.contrib.humanize.round(clojure.contrib.humanize.log(num)) / clojure.contrib.humanize.round(clojure.contrib.humanize.log(base)));
});
clojure.contrib.humanize.human_pows = new cljs.core.PersistentVector(null, 12, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(100)," googol"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(33)," decillion"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(30)," nonillion"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(27)," octillion"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(24)," septillion"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(21)," sextillion"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(18)," quintillion"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(15)," quadrillion"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(12)," trillion"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(9)," billion"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(6)," million"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(0),""], null)], null);
/**
 * Converts a large integer to a friendly text representation. Works best for
 * numbers over 1 million. For example, 1000000 becomes '1.0 million', 1200000
 * becomes '1.2 million' and '1200000000' becomes '1.2 billion'.  Supports up to
 * decillion (33 digits) and googol (100 digits).
 */
clojure.contrib.humanize.intword = (function clojure$contrib$humanize$intword(var_args){
var args__4742__auto__ = [];
var len__4736__auto___52119 = arguments.length;
var i__4737__auto___52120 = (0);
while(true){
if((i__4737__auto___52120 < len__4736__auto___52119)){
args__4742__auto__.push((arguments[i__4737__auto___52120]));

var G__52121 = (i__4737__auto___52120 + (1));
i__4737__auto___52120 = G__52121;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return clojure.contrib.humanize.intword.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(clojure.contrib.humanize.intword.cljs$core$IFn$_invoke$arity$variadic = (function (num,p__52002){
var map__52003 = p__52002;
var map__52003__$1 = (((((!((map__52003 == null))))?(((((map__52003.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__52003.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__52003):map__52003);
var format = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__52003__$1,new cljs.core.Keyword(null,"format","format",-1306924766),"%.1f");
var base_pow = (clojure.contrib.humanize.floor((clojure.contrib.humanize.log10.cljs$core$IFn$_invoke$arity$1 ? clojure.contrib.humanize.log10.cljs$core$IFn$_invoke$arity$1(num) : clojure.contrib.humanize.log10.call(null,num))) | (0));
var vec__52005 = cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p__52008){
var vec__52009 = p__52008;
var base = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__52009,(0),null);
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__52009,(1),null);
return (base_pow >= base);
}),clojure.contrib.humanize.human_pows));
var base_pow__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__52005,(0),null);
var suffix = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__52005,(1),null);
var value = (num / clojure.contrib.humanize.expt((10),base_pow__$1));
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(clojure.contrib.humanize.num_format(format,value)),cljs.core.str.cljs$core$IFn$_invoke$arity$1(suffix)].join('');
}));

(clojure.contrib.humanize.intword.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(clojure.contrib.humanize.intword.cljs$lang$applyTo = (function (seq52000){
var G__52001 = cljs.core.first(seq52000);
var seq52000__$1 = cljs.core.next(seq52000);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__52001,seq52000__$1);
}));

clojure.contrib.humanize.numap = cljs.core.PersistentHashMap.fromArrays([(0),(70),(7),(20),(60),(1),(4),(15),(50),(40),(13),(90),(6),(17),(3),(12),(2),(19),(11),(9),(5),(14),(16),(30),(10),(18),(80),(8)],["","seventy","seven","twenty","sixty","one","four","fifteen","fifty","forty","thirteen","ninety","six","seventeen","three","twelve","two","nineteen","eleven","nine","five","fourteen","sixteen","thirty","ten","eighteen","eighty","eight"]);
/**
 * Takes a number and return a full written string form. For example,
 * 23237897 will be written as "twenty-three million two hundred and
 * thirty-seven thousand eight hundred and ninety-seven".  
 */
clojure.contrib.humanize.numberword = (function clojure$contrib$humanize$numberword(num){
if((num === (0))){
return "zero";
} else {
var digitcnt = ((clojure.contrib.humanize.log10.cljs$core$IFn$_invoke$arity$1 ? clojure.contrib.humanize.log10.cljs$core$IFn$_invoke$arity$1(num) : clojure.contrib.humanize.log10.call(null,num)) | (0));
var divisible_QMARK_ = (function (num__$1,div){
return (cljs.core.rem(num__$1,div) === (0));
});
var n_digit = (function (num__$1,n){
return clojure.contrib.humanize.char__GT_int(cljs.core.str.cljs$core$IFn$_invoke$arity$1(num__$1).charAt(n));
});
if((digitcnt >= (6))){
return clojure.string.join.cljs$core$IFn$_invoke$arity$2(" ",new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [(function (){var G__52012 = ((num / (1000000)) | (0));
return (clojure.contrib.humanize.numberword.cljs$core$IFn$_invoke$arity$1 ? clojure.contrib.humanize.numberword.cljs$core$IFn$_invoke$arity$1(G__52012) : clojure.contrib.humanize.numberword.call(null,G__52012));
})(),"million",(function (){var G__52013 = cljs.core.rem(num,(1000000));
return (clojure.contrib.humanize.numberword.cljs$core$IFn$_invoke$arity$1 ? clojure.contrib.humanize.numberword.cljs$core$IFn$_invoke$arity$1(G__52013) : clojure.contrib.humanize.numberword.call(null,G__52013));
})()], null));
} else {
if((digitcnt >= (3))){
return clojure.string.join.cljs$core$IFn$_invoke$arity$2(" ",new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [(function (){var G__52014 = ((num / (1000)) | (0));
return (clojure.contrib.humanize.numberword.cljs$core$IFn$_invoke$arity$1 ? clojure.contrib.humanize.numberword.cljs$core$IFn$_invoke$arity$1(G__52014) : clojure.contrib.humanize.numberword.call(null,G__52014));
})(),"thousand",(function (){var G__52015 = cljs.core.rem(num,(1000));
return (clojure.contrib.humanize.numberword.cljs$core$IFn$_invoke$arity$1 ? clojure.contrib.humanize.numberword.cljs$core$IFn$_invoke$arity$1(G__52015) : clojure.contrib.humanize.numberword.call(null,G__52015));
})()], null));
} else {
if((digitcnt >= (2))){
if(divisible_QMARK_(num,(100))){
return clojure.string.join.cljs$core$IFn$_invoke$arity$2(" ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(function (){var G__52016 = ((num / (100)) | (0));
return (clojure.contrib.humanize.numap.cljs$core$IFn$_invoke$arity$1 ? clojure.contrib.humanize.numap.cljs$core$IFn$_invoke$arity$1(G__52016) : clojure.contrib.humanize.numap.call(null,G__52016));
})(),"hundred"], null));
} else {
return clojure.string.join.cljs$core$IFn$_invoke$arity$2(" ",new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [(function (){var G__52017 = ((num / (100)) | (0));
return (clojure.contrib.humanize.numap.cljs$core$IFn$_invoke$arity$1 ? clojure.contrib.humanize.numap.cljs$core$IFn$_invoke$arity$1(G__52017) : clojure.contrib.humanize.numap.call(null,G__52017));
})(),"hundred","and",(function (){var G__52019 = cljs.core.rem(num,(100));
return (clojure.contrib.humanize.numberword.cljs$core$IFn$_invoke$arity$1 ? clojure.contrib.humanize.numberword.cljs$core$IFn$_invoke$arity$1(G__52019) : clojure.contrib.humanize.numberword.call(null,G__52019));
})()], null));
}
} else {
if((num < (20))){
return (clojure.contrib.humanize.numap.cljs$core$IFn$_invoke$arity$1 ? clojure.contrib.humanize.numap.cljs$core$IFn$_invoke$arity$1(num) : clojure.contrib.humanize.numap.call(null,num));
} else {
if(divisible_QMARK_(num,(10))){
return (clojure.contrib.humanize.numap.cljs$core$IFn$_invoke$arity$1 ? clojure.contrib.humanize.numap.cljs$core$IFn$_invoke$arity$1(num) : clojure.contrib.humanize.numap.call(null,num));
} else {
return clojure.string.join.cljs$core$IFn$_invoke$arity$2("-",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(function (){var G__52022 = ((10) * n_digit(num,(0)));
return (clojure.contrib.humanize.numap.cljs$core$IFn$_invoke$arity$1 ? clojure.contrib.humanize.numap.cljs$core$IFn$_invoke$arity$1(G__52022) : clojure.contrib.humanize.numap.call(null,G__52022));
})(),(function (){var G__52023 = n_digit(num,(1));
return (clojure.contrib.humanize.numap.cljs$core$IFn$_invoke$arity$1 ? clojure.contrib.humanize.numap.cljs$core$IFn$_invoke$arity$1(G__52023) : clojure.contrib.humanize.numap.call(null,G__52023));
})()], null));

}
}
}
}
}
}
});
/**
 * Format a number of bytes as a human readable filesize (eg. 10 kB). By
 * default, decimal suffixes (kB, MB) are used.  Passing :binary true will use
 * binary suffixes (KiB, MiB) instead.
 */
clojure.contrib.humanize.filesize = (function clojure$contrib$humanize$filesize(var_args){
var args__4742__auto__ = [];
var len__4736__auto___52142 = arguments.length;
var i__4737__auto___52143 = (0);
while(true){
if((i__4737__auto___52143 < len__4736__auto___52142)){
args__4742__auto__.push((arguments[i__4737__auto___52143]));

var G__52144 = (i__4737__auto___52143 + (1));
i__4737__auto___52143 = G__52144;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return clojure.contrib.humanize.filesize.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(clojure.contrib.humanize.filesize.cljs$core$IFn$_invoke$arity$variadic = (function (bytes,p__52026){
var map__52027 = p__52026;
var map__52027__$1 = (((((!((map__52027 == null))))?(((((map__52027.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__52027.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__52027):map__52027);
var binary = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__52027__$1,new cljs.core.Keyword(null,"binary","binary",-1802232288),false);
var format = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__52027__$1,new cljs.core.Keyword(null,"format","format",-1306924766),"%.1f");
if((bytes === (0))){
return "0";
} else {
var decimal_sizes = new cljs.core.PersistentVector(null, 9, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"B","B",-1422503380),new cljs.core.Keyword(null,"KB","KB",-1995214355),new cljs.core.Keyword(null,"MB","MB",-822095316),new cljs.core.Keyword(null,"GB","GB",1529633027),new cljs.core.Keyword(null,"TB","TB",1543563109),new cljs.core.Keyword(null,"PB","PB",-1255943909),new cljs.core.Keyword(null,"EB","EB",-1311440000),new cljs.core.Keyword(null,"ZB","ZB",1556539844),new cljs.core.Keyword(null,"YB","YB",1125554647)], null);
var binary_sizes = new cljs.core.PersistentVector(null, 9, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"B","B",-1422503380),new cljs.core.Keyword(null,"KiB","KiB",568616254),new cljs.core.Keyword(null,"MiB","MiB",-395924856),new cljs.core.Keyword(null,"GiB","GiB",154798556),new cljs.core.Keyword(null,"TiB","TiB",1016674856),new cljs.core.Keyword(null,"PiB","PiB",2017567873),new cljs.core.Keyword(null,"EiB","EiB",-1332230598),new cljs.core.Keyword(null,"ZiB","ZiB",1819403163),new cljs.core.Keyword(null,"YiB","YiB",866096766)], null);
var units = (cljs.core.truth_(binary)?binary_sizes:decimal_sizes);
var base = (cljs.core.truth_(binary)?(1024):(1000));
var base_pow = (clojure.contrib.humanize.floor(clojure.contrib.humanize.logn(bytes,base)) | (0));
var base_pow__$1 = (((base_pow < cljs.core.count(units)))?base_pow:(cljs.core.count(units) - (1)));
var suffix = cljs.core.name(cljs.core.get.cljs$core$IFn$_invoke$arity$2(units,base_pow__$1));
var value = (bytes / clojure.contrib.humanize.expt(base,base_pow__$1));
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(clojure.contrib.humanize.num_format(format,value)),suffix].join('');
}
}));

(clojure.contrib.humanize.filesize.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(clojure.contrib.humanize.filesize.cljs$lang$applyTo = (function (seq52024){
var G__52025 = cljs.core.first(seq52024);
var seq52024__$1 = cljs.core.next(seq52024);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__52025,seq52024__$1);
}));

/**
 * Truncate a string with suffix (ellipsis by default) if it is
 * longer than specified length.
 */
clojure.contrib.humanize.truncate = (function clojure$contrib$humanize$truncate(var_args){
var G__52030 = arguments.length;
switch (G__52030) {
case 3:
return clojure.contrib.humanize.truncate.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 2:
return clojure.contrib.humanize.truncate.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(clojure.contrib.humanize.truncate.cljs$core$IFn$_invoke$arity$3 = (function (string,length,suffix){
var string_len = cljs.core.count(string);
var suffix_len = cljs.core.count(suffix);
if((string_len <= length)){
return string;
} else {
return [cljs.core.subs.cljs$core$IFn$_invoke$arity$3(string,(0),(length - suffix_len)),cljs.core.str.cljs$core$IFn$_invoke$arity$1(suffix)].join('');
}
}));

(clojure.contrib.humanize.truncate.cljs$core$IFn$_invoke$arity$2 = (function (string,length){
return clojure.contrib.humanize.truncate.cljs$core$IFn$_invoke$arity$3(string,length,"...");
}));

(clojure.contrib.humanize.truncate.cljs$lang$maxFixedArity = 3);

/**
 * Converts a list of items to a human readable string
 * with an optional limit.
 */
clojure.contrib.humanize.oxford = (function clojure$contrib$humanize$oxford(var_args){
var args__4742__auto__ = [];
var len__4736__auto___52146 = arguments.length;
var i__4737__auto___52147 = (0);
while(true){
if((i__4737__auto___52147 < len__4736__auto___52146)){
args__4742__auto__.push((arguments[i__4737__auto___52147]));

var G__52149 = (i__4737__auto___52147 + (1));
i__4737__auto___52147 = G__52149;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return clojure.contrib.humanize.oxford.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(clojure.contrib.humanize.oxford.cljs$core$IFn$_invoke$arity$variadic = (function (coll,p__52033){
var map__52034 = p__52033;
var map__52034__$1 = (((((!((map__52034 == null))))?(((((map__52034.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__52034.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__52034):map__52034);
var maximum_display = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__52034__$1,new cljs.core.Keyword(null,"maximum-display","maximum-display",125512845),(4));
var truncate_noun = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__52034__$1,new cljs.core.Keyword(null,"truncate-noun","truncate-noun",2088972907),null);
var coll_length = cljs.core.count(coll);
if((coll_length < (2))){
return clojure.string.join.cljs$core$IFn$_invoke$arity$1(coll);
} else {
if((coll_length <= maximum_display)){
var before_last = cljs.core.take.cljs$core$IFn$_invoke$arity$2((coll_length - (1)),coll);
var last_item = cljs.core.last(coll);
return [clojure.string.join.cljs$core$IFn$_invoke$arity$1(cljs.core.interpose.cljs$core$IFn$_invoke$arity$2(", ",before_last)),", and ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(last_item)].join('');
} else {
if((coll_length > maximum_display)){
var display_coll = cljs.core.take.cljs$core$IFn$_invoke$arity$2(maximum_display,coll);
var remaining = (coll_length - maximum_display);
var last_item = ((cljs.core.empty_QMARK_(truncate_noun))?[cljs.core.str.cljs$core$IFn$_invoke$arity$1(remaining)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(clojure.contrib.inflect.pluralize_noun(remaining,"other"))].join(''):[cljs.core.str.cljs$core$IFn$_invoke$arity$1(remaining)," other ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(clojure.contrib.inflect.pluralize_noun(remaining,truncate_noun))].join(''));
return [clojure.string.join.cljs$core$IFn$_invoke$arity$1(cljs.core.interpose.cljs$core$IFn$_invoke$arity$2(", ",display_coll)),", and ",last_item].join('');
} else {
return coll_length;

}
}
}
}));

(clojure.contrib.humanize.oxford.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(clojure.contrib.humanize.oxford.cljs$lang$applyTo = (function (seq52031){
var G__52032 = cljs.core.first(seq52031);
var seq52031__$1 = cljs.core.next(seq52031);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__52032,seq52031__$1);
}));

clojure.contrib.humanize.in_decades = (function clojure$contrib$humanize$in_decades(diff){
return (cljs_time.core.in_years(diff) / (10));
});
clojure.contrib.humanize.in_centuries = (function clojure$contrib$humanize$in_centuries(diff){
return (cljs_time.core.in_years(diff) / (100));
});
clojure.contrib.humanize.in_millennia = (function clojure$contrib$humanize$in_millennia(diff){
return (cljs_time.core.in_years(diff) / (1000));
});
/**
 * Given a datetime or date, return a human-friendly representation
 * of the amount of time elapsed. 
 */
clojure.contrib.humanize.datetime = (function clojure$contrib$humanize$datetime(var_args){
var args__4742__auto__ = [];
var len__4736__auto___52173 = arguments.length;
var i__4737__auto___52174 = (0);
while(true){
if((i__4737__auto___52174 < len__4736__auto___52173)){
args__4742__auto__.push((arguments[i__4737__auto___52174]));

var G__52175 = (i__4737__auto___52174 + (1));
i__4737__auto___52174 = G__52175;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return clojure.contrib.humanize.datetime.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(clojure.contrib.humanize.datetime.cljs$core$IFn$_invoke$arity$variadic = (function (then_dt,p__52038){
var map__52039 = p__52038;
var map__52039__$1 = (((((!((map__52039 == null))))?(((((map__52039.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__52039.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__52039):map__52039);
var now_dt = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__52039__$1,new cljs.core.Keyword(null,"now-dt","now-dt",1406756612),cljs_time.local.local_now());
var suffix = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__52039__$1,new cljs.core.Keyword(null,"suffix","suffix",367373057),"ago");
var prefix = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__52039__$1,new cljs.core.Keyword(null,"prefix","prefix",-265908465),"in");
var then_dt__$1 = cljs_time.coerce.to_date_time(then_dt);
var now_dt__$1 = cljs_time.coerce.to_date_time(now_dt);
var future_time_QMARK_ = cljs_time.core.after_QMARK_(then_dt__$1,now_dt__$1);
var diff = (cljs.core.truth_(future_time_QMARK_)?cljs_time.core.interval(now_dt__$1,then_dt__$1):cljs_time.core.interval(then_dt__$1,now_dt__$1));
if((clojure.contrib.humanize.in_millennia(diff) >= (1))){
var d__51982__auto__ = clojure.contrib.humanize.in_millennia(diff);
var t__51983__auto__ = clojure.contrib.inflect.pluralize_noun(clojure.contrib.humanize.in_millennia(diff),"millenium");
if(cljs.core.truth_(future_time_QMARK_)){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(prefix)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(d__51982__auto__)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(t__51983__auto__)].join('');
} else {
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(d__51982__auto__)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(t__51983__auto__)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(suffix)].join('');
}
} else {
if((clojure.contrib.humanize.in_centuries(diff) >= (1))){
var d__51982__auto__ = clojure.contrib.humanize.in_centuries(diff);
var t__51983__auto__ = clojure.contrib.inflect.pluralize_noun(clojure.contrib.humanize.in_centuries(diff),"century");
if(cljs.core.truth_(future_time_QMARK_)){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(prefix)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(d__51982__auto__)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(t__51983__auto__)].join('');
} else {
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(d__51982__auto__)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(t__51983__auto__)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(suffix)].join('');
}
} else {
if((clojure.contrib.humanize.in_decades(diff) >= (1))){
var d__51982__auto__ = clojure.contrib.humanize.in_decades(diff);
var t__51983__auto__ = clojure.contrib.inflect.pluralize_noun(clojure.contrib.humanize.in_decades(diff),"decade");
if(cljs.core.truth_(future_time_QMARK_)){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(prefix)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(d__51982__auto__)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(t__51983__auto__)].join('');
} else {
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(d__51982__auto__)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(t__51983__auto__)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(suffix)].join('');
}
} else {
if((cljs_time.core.in_years(diff) >= (1))){
var d__51982__auto__ = cljs_time.core.in_years(diff);
var t__51983__auto__ = clojure.contrib.inflect.pluralize_noun(cljs_time.core.in_years(diff),"year");
if(cljs.core.truth_(future_time_QMARK_)){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(prefix)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(d__51982__auto__)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(t__51983__auto__)].join('');
} else {
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(d__51982__auto__)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(t__51983__auto__)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(suffix)].join('');
}
} else {
if((cljs_time.core.in_months(diff) >= (1))){
var d__51982__auto__ = cljs_time.core.in_months(diff);
var t__51983__auto__ = clojure.contrib.inflect.pluralize_noun(cljs_time.core.in_months(diff),"month");
if(cljs.core.truth_(future_time_QMARK_)){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(prefix)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(d__51982__auto__)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(t__51983__auto__)].join('');
} else {
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(d__51982__auto__)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(t__51983__auto__)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(suffix)].join('');
}
} else {
if((cljs_time.core.in_weeks(diff) >= (1))){
var d__51982__auto__ = cljs_time.core.in_weeks(diff);
var t__51983__auto__ = clojure.contrib.inflect.pluralize_noun(cljs_time.core.in_weeks(diff),"week");
if(cljs.core.truth_(future_time_QMARK_)){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(prefix)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(d__51982__auto__)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(t__51983__auto__)].join('');
} else {
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(d__51982__auto__)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(t__51983__auto__)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(suffix)].join('');
}
} else {
if((cljs_time.core.in_days(diff) >= (1))){
var d__51982__auto__ = cljs_time.core.in_days(diff);
var t__51983__auto__ = clojure.contrib.inflect.pluralize_noun(cljs_time.core.in_days(diff),"day");
if(cljs.core.truth_(future_time_QMARK_)){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(prefix)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(d__51982__auto__)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(t__51983__auto__)].join('');
} else {
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(d__51982__auto__)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(t__51983__auto__)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(suffix)].join('');
}
} else {
if((cljs_time.core.in_hours(diff) >= (1))){
var d__51982__auto__ = cljs_time.core.in_hours(diff);
var t__51983__auto__ = clojure.contrib.inflect.pluralize_noun(cljs_time.core.in_hours(diff),"hour");
if(cljs.core.truth_(future_time_QMARK_)){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(prefix)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(d__51982__auto__)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(t__51983__auto__)].join('');
} else {
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(d__51982__auto__)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(t__51983__auto__)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(suffix)].join('');
}
} else {
if((cljs_time.core.in_minutes(diff) >= (1))){
var d__51982__auto__ = cljs_time.core.in_minutes(diff);
var t__51983__auto__ = clojure.contrib.inflect.pluralize_noun(cljs_time.core.in_minutes(diff),"minute");
if(cljs.core.truth_(future_time_QMARK_)){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(prefix)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(d__51982__auto__)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(t__51983__auto__)].join('');
} else {
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(d__51982__auto__)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(t__51983__auto__)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(suffix)].join('');
}
} else {
if((cljs_time.core.in_seconds(diff) >= (1))){
var d__51982__auto__ = cljs_time.core.in_seconds(diff);
var t__51983__auto__ = clojure.contrib.inflect.pluralize_noun(cljs_time.core.in_seconds(diff),"second");
if(cljs.core.truth_(future_time_QMARK_)){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(prefix)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(d__51982__auto__)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(t__51983__auto__)].join('');
} else {
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(d__51982__auto__)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(t__51983__auto__)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(suffix)].join('');
}
} else {
if(cljs.core.truth_(future_time_QMARK_)){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(prefix)," a moment"].join('');
} else {
return ["a moment ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(suffix)].join('');
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
}));

(clojure.contrib.humanize.datetime.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(clojure.contrib.humanize.datetime.cljs$lang$applyTo = (function (seq52036){
var G__52037 = cljs.core.first(seq52036);
var seq52036__$1 = cljs.core.next(seq52036);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__52037,seq52036__$1);
}));

clojure.contrib.humanize.duration_periods = new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(((((1000) * (60)) * (60)) * (24)) * (365)),"year"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(((((1000) * (60)) * (60)) * (24)) * (31)),"month"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(((((1000) * (60)) * (60)) * (24)) * (7)),"week"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [((((1000) * (60)) * (60)) * (24)),"day"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(((1000) * (60)) * (60)),"hour"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [((1000) * (60)),"minute"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(1000),"second"], null)], null);
/**
 * Converts a duration, in milliseconds, to a set of terms describing the duration.
 *   The terms are in descending order, largest period to smallest.
 * 
 *   Each term is a tuple of count and period name, e.g., `[5 "second"]`.
 * 
 *   After seconds are accounted for, remaining milliseconds are ignored.
 */
clojure.contrib.humanize.duration_terms = (function clojure$contrib$humanize$duration_terms(duration_ms){
if(((0) <= duration_ms)){
} else {
throw (new Error("Assert failed: (<= 0 duration-ms)"));
}

var remainder = duration_ms;
var G__52047 = clojure.contrib.humanize.duration_periods;
var vec__52048 = G__52047;
var seq__52049 = cljs.core.seq(vec__52048);
var first__52050 = cljs.core.first(seq__52049);
var seq__52049__$1 = cljs.core.next(seq__52049);
var vec__52051 = first__52050;
var period_ms = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__52051,(0),null);
var period_name = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__52051,(1),null);
var more_periods = seq__52049__$1;
var terms = cljs.core.PersistentVector.EMPTY;
var remainder__$1 = remainder;
var G__52047__$1 = G__52047;
var terms__$1 = terms;
while(true){
var remainder__$2 = remainder__$1;
var vec__52060 = G__52047__$1;
var seq__52061 = cljs.core.seq(vec__52060);
var first__52062 = cljs.core.first(seq__52061);
var seq__52061__$1 = cljs.core.next(seq__52061);
var vec__52063 = first__52062;
var period_ms__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__52063,(0),null);
var period_name__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__52063,(1),null);
var more_periods__$1 = seq__52061__$1;
var terms__$2 = terms__$1;
if((period_ms__$1 == null)){
return terms__$2;
} else {
if((remainder__$2 < period_ms__$1)){
var G__52192 = remainder__$2;
var G__52193 = more_periods__$1;
var G__52194 = terms__$2;
remainder__$1 = G__52192;
G__52047__$1 = G__52193;
terms__$1 = G__52194;
continue;
} else {
var period_count = ((remainder__$2 / period_ms__$1) | (0));
var next_remainder = cljs.core.mod(remainder__$2,period_ms__$1);
var G__52195 = next_remainder;
var G__52196 = more_periods__$1;
var G__52197 = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(terms__$2,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [period_count,period_name__$1], null));
remainder__$1 = G__52195;
G__52047__$1 = G__52196;
terms__$1 = G__52197;
continue;

}
}
break;
}
});
/**
 * Converts duration, in milliseconds, into a string describing it in terms
 *   of years, months, weeks, days, hours, minutes, and seconds.
 * 
 *   Ex:
 * 
 *   (duration 325100) => "five minutes, twenty-five seconds"
 * 
 *   The months and years periods are not based on actual calendar, so are approximate; this
 *   function works best for shorter periods of time.
 * 
 *   The optional options map allow some control over the result.
 * 
 *   :list-format (default: a function) can be set to a function such as oxford
 * 
 *   :number-format (default: numberword) function used to format period counts
 * 
 *   :short-text (default: "less than a second") 
 */
clojure.contrib.humanize.duration = (function clojure$contrib$humanize$duration(var_args){
var G__52068 = arguments.length;
switch (G__52068) {
case 1:
return clojure.contrib.humanize.duration.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return clojure.contrib.humanize.duration.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(clojure.contrib.humanize.duration.cljs$core$IFn$_invoke$arity$1 = (function (duration_ms){
return clojure.contrib.humanize.duration.cljs$core$IFn$_invoke$arity$2(duration_ms,null);
}));

(clojure.contrib.humanize.duration.cljs$core$IFn$_invoke$arity$2 = (function (duration_ms,options){
var terms = clojure.contrib.humanize.duration_terms(duration_ms);
var map__52069 = options;
var map__52069__$1 = (((((!((map__52069 == null))))?(((((map__52069.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__52069.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__52069):map__52069);
var number_format = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__52069__$1,new cljs.core.Keyword(null,"number-format","number-format",-1955958496),clojure.contrib.humanize.numberword);
var list_format = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__52069__$1,new cljs.core.Keyword(null,"list-format","list-format",-1200142930),(function (p1__52066_SHARP_){
return clojure.string.join.cljs$core$IFn$_invoke$arity$2(", ",p1__52066_SHARP_);
}));
var short_text = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__52069__$1,new cljs.core.Keyword(null,"short-text","short-text",-721727681),"less than a second");
if(cljs.core.seq(terms)){
var G__52071 = cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p__52072){
var vec__52073 = p__52072;
var period_count = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__52073,(0),null);
var period_name = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__52073,(1),null);
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1((number_format.cljs$core$IFn$_invoke$arity$1 ? number_format.cljs$core$IFn$_invoke$arity$1(period_count) : number_format.call(null,period_count)))," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(clojure.contrib.inflect.pluralize_noun(period_count,period_name))].join('');
}),terms);
return (list_format.cljs$core$IFn$_invoke$arity$1 ? list_format.cljs$core$IFn$_invoke$arity$1(G__52071) : list_format.call(null,G__52071));
} else {
return short_text;
}
}));

(clojure.contrib.humanize.duration.cljs$lang$maxFixedArity = 2);


//# sourceMappingURL=clojure.contrib.humanize.js.map
