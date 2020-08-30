goog.provide('cljs.repl');
cljs.repl.print_doc = (function cljs$repl$print_doc(p__37225){
var map__37226 = p__37225;
var map__37226__$1 = (((((!((map__37226 == null))))?(((((map__37226.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__37226.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__37226):map__37226);
var m = map__37226__$1;
var n = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37226__$1,new cljs.core.Keyword(null,"ns","ns",441598760));
var nm = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37226__$1,new cljs.core.Keyword(null,"name","name",1843675177));
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["-------------------------"], 0));

cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(function (){var or__4126__auto__ = new cljs.core.Keyword(null,"spec","spec",347520401).cljs$core$IFn$_invoke$arity$1(m);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return [(function (){var temp__5735__auto__ = new cljs.core.Keyword(null,"ns","ns",441598760).cljs$core$IFn$_invoke$arity$1(m);
if(cljs.core.truth_(temp__5735__auto__)){
var ns = temp__5735__auto__;
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(ns),"/"].join('');
} else {
return null;
}
})(),cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(m))].join('');
}
})()], 0));

if(cljs.core.truth_(new cljs.core.Keyword(null,"protocol","protocol",652470118).cljs$core$IFn$_invoke$arity$1(m))){
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["Protocol"], 0));
} else {
}

if(cljs.core.truth_(new cljs.core.Keyword(null,"forms","forms",2045992350).cljs$core$IFn$_invoke$arity$1(m))){
var seq__37229_37424 = cljs.core.seq(new cljs.core.Keyword(null,"forms","forms",2045992350).cljs$core$IFn$_invoke$arity$1(m));
var chunk__37230_37425 = null;
var count__37231_37426 = (0);
var i__37232_37427 = (0);
while(true){
if((i__37232_37427 < count__37231_37426)){
var f_37432 = chunk__37230_37425.cljs$core$IIndexed$_nth$arity$2(null,i__37232_37427);
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["  ",f_37432], 0));


var G__37433 = seq__37229_37424;
var G__37434 = chunk__37230_37425;
var G__37435 = count__37231_37426;
var G__37436 = (i__37232_37427 + (1));
seq__37229_37424 = G__37433;
chunk__37230_37425 = G__37434;
count__37231_37426 = G__37435;
i__37232_37427 = G__37436;
continue;
} else {
var temp__5735__auto___37441 = cljs.core.seq(seq__37229_37424);
if(temp__5735__auto___37441){
var seq__37229_37442__$1 = temp__5735__auto___37441;
if(cljs.core.chunked_seq_QMARK_(seq__37229_37442__$1)){
var c__4556__auto___37443 = cljs.core.chunk_first(seq__37229_37442__$1);
var G__37444 = cljs.core.chunk_rest(seq__37229_37442__$1);
var G__37445 = c__4556__auto___37443;
var G__37446 = cljs.core.count(c__4556__auto___37443);
var G__37447 = (0);
seq__37229_37424 = G__37444;
chunk__37230_37425 = G__37445;
count__37231_37426 = G__37446;
i__37232_37427 = G__37447;
continue;
} else {
var f_37448 = cljs.core.first(seq__37229_37442__$1);
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["  ",f_37448], 0));


var G__37449 = cljs.core.next(seq__37229_37442__$1);
var G__37450 = null;
var G__37451 = (0);
var G__37452 = (0);
seq__37229_37424 = G__37449;
chunk__37230_37425 = G__37450;
count__37231_37426 = G__37451;
i__37232_37427 = G__37452;
continue;
}
} else {
}
}
break;
}
} else {
if(cljs.core.truth_(new cljs.core.Keyword(null,"arglists","arglists",1661989754).cljs$core$IFn$_invoke$arity$1(m))){
var arglists_37453 = new cljs.core.Keyword(null,"arglists","arglists",1661989754).cljs$core$IFn$_invoke$arity$1(m);
if(cljs.core.truth_((function (){var or__4126__auto__ = new cljs.core.Keyword(null,"macro","macro",-867863404).cljs$core$IFn$_invoke$arity$1(m);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"repl-special-function","repl-special-function",1262603725).cljs$core$IFn$_invoke$arity$1(m);
}
})())){
cljs.core.prn.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([arglists_37453], 0));
} else {
cljs.core.prn.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Symbol(null,"quote","quote",1377916282,null),cljs.core.first(arglists_37453)))?cljs.core.second(arglists_37453):arglists_37453)], 0));
}
} else {
}
}

if(cljs.core.truth_(new cljs.core.Keyword(null,"special-form","special-form",-1326536374).cljs$core$IFn$_invoke$arity$1(m))){
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["Special Form"], 0));

cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([" ",new cljs.core.Keyword(null,"doc","doc",1913296891).cljs$core$IFn$_invoke$arity$1(m)], 0));

if(cljs.core.contains_QMARK_(m,new cljs.core.Keyword(null,"url","url",276297046))){
if(cljs.core.truth_(new cljs.core.Keyword(null,"url","url",276297046).cljs$core$IFn$_invoke$arity$1(m))){
return cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([["\n  Please see http://clojure.org/",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"url","url",276297046).cljs$core$IFn$_invoke$arity$1(m))].join('')], 0));
} else {
return null;
}
} else {
return cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([["\n  Please see http://clojure.org/special_forms#",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(m))].join('')], 0));
}
} else {
if(cljs.core.truth_(new cljs.core.Keyword(null,"macro","macro",-867863404).cljs$core$IFn$_invoke$arity$1(m))){
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["Macro"], 0));
} else {
}

if(cljs.core.truth_(new cljs.core.Keyword(null,"spec","spec",347520401).cljs$core$IFn$_invoke$arity$1(m))){
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["Spec"], 0));
} else {
}

if(cljs.core.truth_(new cljs.core.Keyword(null,"repl-special-function","repl-special-function",1262603725).cljs$core$IFn$_invoke$arity$1(m))){
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["REPL Special Function"], 0));
} else {
}

cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([" ",new cljs.core.Keyword(null,"doc","doc",1913296891).cljs$core$IFn$_invoke$arity$1(m)], 0));

if(cljs.core.truth_(new cljs.core.Keyword(null,"protocol","protocol",652470118).cljs$core$IFn$_invoke$arity$1(m))){
var seq__37235_37458 = cljs.core.seq(new cljs.core.Keyword(null,"methods","methods",453930866).cljs$core$IFn$_invoke$arity$1(m));
var chunk__37236_37459 = null;
var count__37237_37460 = (0);
var i__37238_37461 = (0);
while(true){
if((i__37238_37461 < count__37237_37460)){
var vec__37252_37462 = chunk__37236_37459.cljs$core$IIndexed$_nth$arity$2(null,i__37238_37461);
var name_37463 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37252_37462,(0),null);
var map__37255_37464 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37252_37462,(1),null);
var map__37255_37465__$1 = (((((!((map__37255_37464 == null))))?(((((map__37255_37464.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__37255_37464.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__37255_37464):map__37255_37464);
var doc_37466 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37255_37465__$1,new cljs.core.Keyword(null,"doc","doc",1913296891));
var arglists_37467 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37255_37465__$1,new cljs.core.Keyword(null,"arglists","arglists",1661989754));
cljs.core.println();

cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([" ",name_37463], 0));

cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([" ",arglists_37467], 0));

if(cljs.core.truth_(doc_37466)){
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([" ",doc_37466], 0));
} else {
}


var G__37468 = seq__37235_37458;
var G__37469 = chunk__37236_37459;
var G__37470 = count__37237_37460;
var G__37471 = (i__37238_37461 + (1));
seq__37235_37458 = G__37468;
chunk__37236_37459 = G__37469;
count__37237_37460 = G__37470;
i__37238_37461 = G__37471;
continue;
} else {
var temp__5735__auto___37472 = cljs.core.seq(seq__37235_37458);
if(temp__5735__auto___37472){
var seq__37235_37473__$1 = temp__5735__auto___37472;
if(cljs.core.chunked_seq_QMARK_(seq__37235_37473__$1)){
var c__4556__auto___37474 = cljs.core.chunk_first(seq__37235_37473__$1);
var G__37475 = cljs.core.chunk_rest(seq__37235_37473__$1);
var G__37476 = c__4556__auto___37474;
var G__37477 = cljs.core.count(c__4556__auto___37474);
var G__37478 = (0);
seq__37235_37458 = G__37475;
chunk__37236_37459 = G__37476;
count__37237_37460 = G__37477;
i__37238_37461 = G__37478;
continue;
} else {
var vec__37257_37480 = cljs.core.first(seq__37235_37473__$1);
var name_37481 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37257_37480,(0),null);
var map__37260_37482 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37257_37480,(1),null);
var map__37260_37483__$1 = (((((!((map__37260_37482 == null))))?(((((map__37260_37482.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__37260_37482.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__37260_37482):map__37260_37482);
var doc_37484 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37260_37483__$1,new cljs.core.Keyword(null,"doc","doc",1913296891));
var arglists_37485 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37260_37483__$1,new cljs.core.Keyword(null,"arglists","arglists",1661989754));
cljs.core.println();

cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([" ",name_37481], 0));

cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([" ",arglists_37485], 0));

if(cljs.core.truth_(doc_37484)){
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([" ",doc_37484], 0));
} else {
}


var G__37486 = cljs.core.next(seq__37235_37473__$1);
var G__37487 = null;
var G__37488 = (0);
var G__37489 = (0);
seq__37235_37458 = G__37486;
chunk__37236_37459 = G__37487;
count__37237_37460 = G__37488;
i__37238_37461 = G__37489;
continue;
}
} else {
}
}
break;
}
} else {
}

if(cljs.core.truth_(n)){
var temp__5735__auto__ = cljs.spec.alpha.get_spec(cljs.core.symbol.cljs$core$IFn$_invoke$arity$2(cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.ns_name(n)),cljs.core.name(nm)));
if(cljs.core.truth_(temp__5735__auto__)){
var fnspec = temp__5735__auto__;
cljs.core.print.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["Spec"], 0));

var seq__37263 = cljs.core.seq(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"args","args",1315556576),new cljs.core.Keyword(null,"ret","ret",-468222814),new cljs.core.Keyword(null,"fn","fn",-1175266204)], null));
var chunk__37264 = null;
var count__37265 = (0);
var i__37266 = (0);
while(true){
if((i__37266 < count__37265)){
var role = chunk__37264.cljs$core$IIndexed$_nth$arity$2(null,i__37266);
var temp__5735__auto___37491__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(fnspec,role);
if(cljs.core.truth_(temp__5735__auto___37491__$1)){
var spec_37492 = temp__5735__auto___37491__$1;
cljs.core.print.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([["\n ",cljs.core.name(role),":"].join(''),cljs.spec.alpha.describe(spec_37492)], 0));
} else {
}


var G__37493 = seq__37263;
var G__37494 = chunk__37264;
var G__37495 = count__37265;
var G__37496 = (i__37266 + (1));
seq__37263 = G__37493;
chunk__37264 = G__37494;
count__37265 = G__37495;
i__37266 = G__37496;
continue;
} else {
var temp__5735__auto____$1 = cljs.core.seq(seq__37263);
if(temp__5735__auto____$1){
var seq__37263__$1 = temp__5735__auto____$1;
if(cljs.core.chunked_seq_QMARK_(seq__37263__$1)){
var c__4556__auto__ = cljs.core.chunk_first(seq__37263__$1);
var G__37497 = cljs.core.chunk_rest(seq__37263__$1);
var G__37498 = c__4556__auto__;
var G__37499 = cljs.core.count(c__4556__auto__);
var G__37500 = (0);
seq__37263 = G__37497;
chunk__37264 = G__37498;
count__37265 = G__37499;
i__37266 = G__37500;
continue;
} else {
var role = cljs.core.first(seq__37263__$1);
var temp__5735__auto___37501__$2 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(fnspec,role);
if(cljs.core.truth_(temp__5735__auto___37501__$2)){
var spec_37502 = temp__5735__auto___37501__$2;
cljs.core.print.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([["\n ",cljs.core.name(role),":"].join(''),cljs.spec.alpha.describe(spec_37502)], 0));
} else {
}


var G__37503 = cljs.core.next(seq__37263__$1);
var G__37504 = null;
var G__37505 = (0);
var G__37506 = (0);
seq__37263 = G__37503;
chunk__37264 = G__37504;
count__37265 = G__37505;
i__37266 = G__37506;
continue;
}
} else {
return null;
}
}
break;
}
} else {
return null;
}
} else {
return null;
}
}
});
/**
 * Constructs a data representation for a Error with keys:
 *  :cause - root cause message
 *  :phase - error phase
 *  :via - cause chain, with cause keys:
 *           :type - exception class symbol
 *           :message - exception message
 *           :data - ex-data
 *           :at - top stack element
 *  :trace - root cause stack elements
 */
cljs.repl.Error__GT_map = (function cljs$repl$Error__GT_map(o){
var base = (function (t){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"type","type",1174270348),(((t instanceof cljs.core.ExceptionInfo))?new cljs.core.Symbol(null,"ExceptionInfo","ExceptionInfo",294935087,null):(((t instanceof Error))?cljs.core.symbol.cljs$core$IFn$_invoke$arity$2("js",t.name):null
))], null),(function (){var temp__5735__auto__ = cljs.core.ex_message(t);
if(cljs.core.truth_(temp__5735__auto__)){
var msg = temp__5735__auto__;
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"message","message",-406056002),msg], null);
} else {
return null;
}
})(),(function (){var temp__5735__auto__ = cljs.core.ex_data(t);
if(cljs.core.truth_(temp__5735__auto__)){
var ed = temp__5735__auto__;
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"data","data",-232669377),ed], null);
} else {
return null;
}
})()], 0));
});
var via = (function (){var via = cljs.core.PersistentVector.EMPTY;
var t = o;
while(true){
if(cljs.core.truth_(t)){
var G__37508 = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(via,t);
var G__37509 = cljs.core.ex_cause(t);
via = G__37508;
t = G__37509;
continue;
} else {
return via;
}
break;
}
})();
var root = cljs.core.peek(via);
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"via","via",-1904457336),cljs.core.vec(cljs.core.map.cljs$core$IFn$_invoke$arity$2(base,via)),new cljs.core.Keyword(null,"trace","trace",-1082747415),null], null),(function (){var temp__5735__auto__ = cljs.core.ex_message(root);
if(cljs.core.truth_(temp__5735__auto__)){
var root_msg = temp__5735__auto__;
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"cause","cause",231901252),root_msg], null);
} else {
return null;
}
})(),(function (){var temp__5735__auto__ = cljs.core.ex_data(root);
if(cljs.core.truth_(temp__5735__auto__)){
var data = temp__5735__auto__;
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"data","data",-232669377),data], null);
} else {
return null;
}
})(),(function (){var temp__5735__auto__ = new cljs.core.Keyword("clojure.error","phase","clojure.error/phase",275140358).cljs$core$IFn$_invoke$arity$1(cljs.core.ex_data(o));
if(cljs.core.truth_(temp__5735__auto__)){
var phase = temp__5735__auto__;
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"phase","phase",575722892),phase], null);
} else {
return null;
}
})()], 0));
});
/**
 * Returns an analysis of the phase, error, cause, and location of an error that occurred
 *   based on Throwable data, as returned by Throwable->map. All attributes other than phase
 *   are optional:
 *  :clojure.error/phase - keyword phase indicator, one of:
 *    :read-source :compile-syntax-check :compilation :macro-syntax-check :macroexpansion
 *    :execution :read-eval-result :print-eval-result
 *  :clojure.error/source - file name (no path)
 *  :clojure.error/line - integer line number
 *  :clojure.error/column - integer column number
 *  :clojure.error/symbol - symbol being expanded/compiled/invoked
 *  :clojure.error/class - cause exception class symbol
 *  :clojure.error/cause - cause exception message
 *  :clojure.error/spec - explain-data for spec error
 */
cljs.repl.ex_triage = (function cljs$repl$ex_triage(datafied_throwable){
var map__37272 = datafied_throwable;
var map__37272__$1 = (((((!((map__37272 == null))))?(((((map__37272.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__37272.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__37272):map__37272);
var via = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37272__$1,new cljs.core.Keyword(null,"via","via",-1904457336));
var trace = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37272__$1,new cljs.core.Keyword(null,"trace","trace",-1082747415));
var phase = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__37272__$1,new cljs.core.Keyword(null,"phase","phase",575722892),new cljs.core.Keyword(null,"execution","execution",253283524));
var map__37273 = cljs.core.last(via);
var map__37273__$1 = (((((!((map__37273 == null))))?(((((map__37273.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__37273.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__37273):map__37273);
var type = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37273__$1,new cljs.core.Keyword(null,"type","type",1174270348));
var message = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37273__$1,new cljs.core.Keyword(null,"message","message",-406056002));
var data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37273__$1,new cljs.core.Keyword(null,"data","data",-232669377));
var map__37274 = data;
var map__37274__$1 = (((((!((map__37274 == null))))?(((((map__37274.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__37274.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__37274):map__37274);
var problems = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37274__$1,new cljs.core.Keyword("cljs.spec.alpha","problems","cljs.spec.alpha/problems",447400814));
var fn = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37274__$1,new cljs.core.Keyword("cljs.spec.alpha","fn","cljs.spec.alpha/fn",408600443));
var caller = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37274__$1,new cljs.core.Keyword("cljs.spec.test.alpha","caller","cljs.spec.test.alpha/caller",-398302390));
var map__37275 = new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(cljs.core.first(via));
var map__37275__$1 = (((((!((map__37275 == null))))?(((((map__37275.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__37275.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__37275):map__37275);
var top_data = map__37275__$1;
var source = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37275__$1,new cljs.core.Keyword("clojure.error","source","clojure.error/source",-2011936397));
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3((function (){var G__37282 = phase;
var G__37282__$1 = (((G__37282 instanceof cljs.core.Keyword))?G__37282.fqn:null);
switch (G__37282__$1) {
case "read-source":
var map__37283 = data;
var map__37283__$1 = (((((!((map__37283 == null))))?(((((map__37283.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__37283.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__37283):map__37283);
var line = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37283__$1,new cljs.core.Keyword("clojure.error","line","clojure.error/line",-1816287471));
var column = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37283__$1,new cljs.core.Keyword("clojure.error","column","clojure.error/column",304721553));
var G__37285 = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(cljs.core.second(via)),top_data], 0));
var G__37285__$1 = (cljs.core.truth_(source)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__37285,new cljs.core.Keyword("clojure.error","source","clojure.error/source",-2011936397),source):G__37285);
var G__37285__$2 = (cljs.core.truth_((function (){var fexpr__37286 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, ["NO_SOURCE_PATH",null,"NO_SOURCE_FILE",null], null), null);
return (fexpr__37286.cljs$core$IFn$_invoke$arity$1 ? fexpr__37286.cljs$core$IFn$_invoke$arity$1(source) : fexpr__37286.call(null,source));
})())?cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(G__37285__$1,new cljs.core.Keyword("clojure.error","source","clojure.error/source",-2011936397)):G__37285__$1);
if(cljs.core.truth_(message)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__37285__$2,new cljs.core.Keyword("clojure.error","cause","clojure.error/cause",-1879175742),message);
} else {
return G__37285__$2;
}

break;
case "compile-syntax-check":
case "compilation":
case "macro-syntax-check":
case "macroexpansion":
var G__37287 = top_data;
var G__37287__$1 = (cljs.core.truth_(source)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__37287,new cljs.core.Keyword("clojure.error","source","clojure.error/source",-2011936397),source):G__37287);
var G__37287__$2 = (cljs.core.truth_((function (){var fexpr__37288 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, ["NO_SOURCE_PATH",null,"NO_SOURCE_FILE",null], null), null);
return (fexpr__37288.cljs$core$IFn$_invoke$arity$1 ? fexpr__37288.cljs$core$IFn$_invoke$arity$1(source) : fexpr__37288.call(null,source));
})())?cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(G__37287__$1,new cljs.core.Keyword("clojure.error","source","clojure.error/source",-2011936397)):G__37287__$1);
var G__37287__$3 = (cljs.core.truth_(type)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__37287__$2,new cljs.core.Keyword("clojure.error","class","clojure.error/class",278435890),type):G__37287__$2);
var G__37287__$4 = (cljs.core.truth_(message)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__37287__$3,new cljs.core.Keyword("clojure.error","cause","clojure.error/cause",-1879175742),message):G__37287__$3);
if(cljs.core.truth_(problems)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__37287__$4,new cljs.core.Keyword("clojure.error","spec","clojure.error/spec",2055032595),data);
} else {
return G__37287__$4;
}

break;
case "read-eval-result":
case "print-eval-result":
var vec__37289 = cljs.core.first(trace);
var source__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37289,(0),null);
var method = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37289,(1),null);
var file = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37289,(2),null);
var line = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37289,(3),null);
var G__37292 = top_data;
var G__37292__$1 = (cljs.core.truth_(line)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__37292,new cljs.core.Keyword("clojure.error","line","clojure.error/line",-1816287471),line):G__37292);
var G__37292__$2 = (cljs.core.truth_(file)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__37292__$1,new cljs.core.Keyword("clojure.error","source","clojure.error/source",-2011936397),file):G__37292__$1);
var G__37292__$3 = (cljs.core.truth_((function (){var and__4115__auto__ = source__$1;
if(cljs.core.truth_(and__4115__auto__)){
return method;
} else {
return and__4115__auto__;
}
})())?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__37292__$2,new cljs.core.Keyword("clojure.error","symbol","clojure.error/symbol",1544821994),(new cljs.core.PersistentVector(null,2,(5),cljs.core.PersistentVector.EMPTY_NODE,[source__$1,method],null))):G__37292__$2);
var G__37292__$4 = (cljs.core.truth_(type)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__37292__$3,new cljs.core.Keyword("clojure.error","class","clojure.error/class",278435890),type):G__37292__$3);
if(cljs.core.truth_(message)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__37292__$4,new cljs.core.Keyword("clojure.error","cause","clojure.error/cause",-1879175742),message);
} else {
return G__37292__$4;
}

break;
case "execution":
var vec__37293 = cljs.core.first(trace);
var source__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37293,(0),null);
var method = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37293,(1),null);
var file = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37293,(2),null);
var line = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37293,(3),null);
var file__$1 = cljs.core.first(cljs.core.remove.cljs$core$IFn$_invoke$arity$2((function (p1__37271_SHARP_){
var or__4126__auto__ = (p1__37271_SHARP_ == null);
if(or__4126__auto__){
return or__4126__auto__;
} else {
var fexpr__37297 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, ["NO_SOURCE_PATH",null,"NO_SOURCE_FILE",null], null), null);
return (fexpr__37297.cljs$core$IFn$_invoke$arity$1 ? fexpr__37297.cljs$core$IFn$_invoke$arity$1(p1__37271_SHARP_) : fexpr__37297.call(null,p1__37271_SHARP_));
}
}),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"file","file",-1269645878).cljs$core$IFn$_invoke$arity$1(caller),file], null)));
var err_line = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"line","line",212345235).cljs$core$IFn$_invoke$arity$1(caller);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return line;
}
})();
var G__37302 = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword("clojure.error","class","clojure.error/class",278435890),type], null);
var G__37302__$1 = (cljs.core.truth_(err_line)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__37302,new cljs.core.Keyword("clojure.error","line","clojure.error/line",-1816287471),err_line):G__37302);
var G__37302__$2 = (cljs.core.truth_(message)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__37302__$1,new cljs.core.Keyword("clojure.error","cause","clojure.error/cause",-1879175742),message):G__37302__$1);
var G__37302__$3 = (cljs.core.truth_((function (){var or__4126__auto__ = fn;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var and__4115__auto__ = source__$1;
if(cljs.core.truth_(and__4115__auto__)){
return method;
} else {
return and__4115__auto__;
}
}
})())?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__37302__$2,new cljs.core.Keyword("clojure.error","symbol","clojure.error/symbol",1544821994),(function (){var or__4126__auto__ = fn;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return (new cljs.core.PersistentVector(null,2,(5),cljs.core.PersistentVector.EMPTY_NODE,[source__$1,method],null));
}
})()):G__37302__$2);
var G__37302__$4 = (cljs.core.truth_(file__$1)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__37302__$3,new cljs.core.Keyword("clojure.error","source","clojure.error/source",-2011936397),file__$1):G__37302__$3);
if(cljs.core.truth_(problems)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__37302__$4,new cljs.core.Keyword("clojure.error","spec","clojure.error/spec",2055032595),data);
} else {
return G__37302__$4;
}

break;
default:
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__37282__$1)].join('')));

}
})(),new cljs.core.Keyword("clojure.error","phase","clojure.error/phase",275140358),phase);
});
/**
 * Returns a string from exception data, as produced by ex-triage.
 *   The first line summarizes the exception phase and location.
 *   The subsequent lines describe the cause.
 */
cljs.repl.ex_str = (function cljs$repl$ex_str(p__37318){
var map__37323 = p__37318;
var map__37323__$1 = (((((!((map__37323 == null))))?(((((map__37323.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__37323.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__37323):map__37323);
var triage_data = map__37323__$1;
var phase = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37323__$1,new cljs.core.Keyword("clojure.error","phase","clojure.error/phase",275140358));
var source = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37323__$1,new cljs.core.Keyword("clojure.error","source","clojure.error/source",-2011936397));
var line = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37323__$1,new cljs.core.Keyword("clojure.error","line","clojure.error/line",-1816287471));
var column = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37323__$1,new cljs.core.Keyword("clojure.error","column","clojure.error/column",304721553));
var symbol = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37323__$1,new cljs.core.Keyword("clojure.error","symbol","clojure.error/symbol",1544821994));
var class$ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37323__$1,new cljs.core.Keyword("clojure.error","class","clojure.error/class",278435890));
var cause = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37323__$1,new cljs.core.Keyword("clojure.error","cause","clojure.error/cause",-1879175742));
var spec = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37323__$1,new cljs.core.Keyword("clojure.error","spec","clojure.error/spec",2055032595));
var loc = [cljs.core.str.cljs$core$IFn$_invoke$arity$1((function (){var or__4126__auto__ = source;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "<cljs repl>";
}
})()),":",cljs.core.str.cljs$core$IFn$_invoke$arity$1((function (){var or__4126__auto__ = line;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return (1);
}
})()),(cljs.core.truth_(column)?[":",cljs.core.str.cljs$core$IFn$_invoke$arity$1(column)].join(''):"")].join('');
var class_name = cljs.core.name((function (){var or__4126__auto__ = class$;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "";
}
})());
var simple_class = class_name;
var cause_type = ((cljs.core.contains_QMARK_(new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, ["RuntimeException",null,"Exception",null], null), null),simple_class))?"":[" (",simple_class,")"].join(''));
var format = goog.string.format;
var G__37328 = phase;
var G__37328__$1 = (((G__37328 instanceof cljs.core.Keyword))?G__37328.fqn:null);
switch (G__37328__$1) {
case "read-source":
return (format.cljs$core$IFn$_invoke$arity$3 ? format.cljs$core$IFn$_invoke$arity$3("Syntax error reading source at (%s).\n%s\n",loc,cause) : format.call(null,"Syntax error reading source at (%s).\n%s\n",loc,cause));

break;
case "macro-syntax-check":
var G__37334 = "Syntax error macroexpanding %sat (%s).\n%s";
var G__37335 = (cljs.core.truth_(symbol)?[cljs.core.str.cljs$core$IFn$_invoke$arity$1(symbol)," "].join(''):"");
var G__37336 = loc;
var G__37337 = (cljs.core.truth_(spec)?(function (){var sb__4667__auto__ = (new goog.string.StringBuffer());
var _STAR_print_newline_STAR__orig_val__37339_37515 = cljs.core._STAR_print_newline_STAR_;
var _STAR_print_fn_STAR__orig_val__37340_37516 = cljs.core._STAR_print_fn_STAR_;
var _STAR_print_newline_STAR__temp_val__37342_37517 = true;
var _STAR_print_fn_STAR__temp_val__37343_37518 = (function (x__4668__auto__){
return sb__4667__auto__.append(x__4668__auto__);
});
(cljs.core._STAR_print_newline_STAR_ = _STAR_print_newline_STAR__temp_val__37342_37517);

(cljs.core._STAR_print_fn_STAR_ = _STAR_print_fn_STAR__temp_val__37343_37518);

try{cljs.spec.alpha.explain_out(cljs.core.update.cljs$core$IFn$_invoke$arity$3(spec,new cljs.core.Keyword("cljs.spec.alpha","problems","cljs.spec.alpha/problems",447400814),(function (probs){
return cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__37316_SHARP_){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(p1__37316_SHARP_,new cljs.core.Keyword(null,"in","in",-1531184865));
}),probs);
}))
);
}finally {(cljs.core._STAR_print_fn_STAR_ = _STAR_print_fn_STAR__orig_val__37340_37516);

(cljs.core._STAR_print_newline_STAR_ = _STAR_print_newline_STAR__orig_val__37339_37515);
}
return cljs.core.str.cljs$core$IFn$_invoke$arity$1(sb__4667__auto__);
})():(format.cljs$core$IFn$_invoke$arity$2 ? format.cljs$core$IFn$_invoke$arity$2("%s\n",cause) : format.call(null,"%s\n",cause)));
return (format.cljs$core$IFn$_invoke$arity$4 ? format.cljs$core$IFn$_invoke$arity$4(G__37334,G__37335,G__37336,G__37337) : format.call(null,G__37334,G__37335,G__37336,G__37337));

break;
case "macroexpansion":
var G__37355 = "Unexpected error%s macroexpanding %sat (%s).\n%s\n";
var G__37356 = cause_type;
var G__37357 = (cljs.core.truth_(symbol)?[cljs.core.str.cljs$core$IFn$_invoke$arity$1(symbol)," "].join(''):"");
var G__37358 = loc;
var G__37359 = cause;
return (format.cljs$core$IFn$_invoke$arity$5 ? format.cljs$core$IFn$_invoke$arity$5(G__37355,G__37356,G__37357,G__37358,G__37359) : format.call(null,G__37355,G__37356,G__37357,G__37358,G__37359));

break;
case "compile-syntax-check":
var G__37365 = "Syntax error%s compiling %sat (%s).\n%s\n";
var G__37366 = cause_type;
var G__37367 = (cljs.core.truth_(symbol)?[cljs.core.str.cljs$core$IFn$_invoke$arity$1(symbol)," "].join(''):"");
var G__37368 = loc;
var G__37369 = cause;
return (format.cljs$core$IFn$_invoke$arity$5 ? format.cljs$core$IFn$_invoke$arity$5(G__37365,G__37366,G__37367,G__37368,G__37369) : format.call(null,G__37365,G__37366,G__37367,G__37368,G__37369));

break;
case "compilation":
var G__37376 = "Unexpected error%s compiling %sat (%s).\n%s\n";
var G__37377 = cause_type;
var G__37378 = (cljs.core.truth_(symbol)?[cljs.core.str.cljs$core$IFn$_invoke$arity$1(symbol)," "].join(''):"");
var G__37379 = loc;
var G__37380 = cause;
return (format.cljs$core$IFn$_invoke$arity$5 ? format.cljs$core$IFn$_invoke$arity$5(G__37376,G__37377,G__37378,G__37379,G__37380) : format.call(null,G__37376,G__37377,G__37378,G__37379,G__37380));

break;
case "read-eval-result":
return (format.cljs$core$IFn$_invoke$arity$5 ? format.cljs$core$IFn$_invoke$arity$5("Error reading eval result%s at %s (%s).\n%s\n",cause_type,symbol,loc,cause) : format.call(null,"Error reading eval result%s at %s (%s).\n%s\n",cause_type,symbol,loc,cause));

break;
case "print-eval-result":
return (format.cljs$core$IFn$_invoke$arity$5 ? format.cljs$core$IFn$_invoke$arity$5("Error printing return value%s at %s (%s).\n%s\n",cause_type,symbol,loc,cause) : format.call(null,"Error printing return value%s at %s (%s).\n%s\n",cause_type,symbol,loc,cause));

break;
case "execution":
if(cljs.core.truth_(spec)){
var G__37390 = "Execution error - invalid arguments to %s at (%s).\n%s";
var G__37391 = symbol;
var G__37392 = loc;
var G__37393 = (function (){var sb__4667__auto__ = (new goog.string.StringBuffer());
var _STAR_print_newline_STAR__orig_val__37394_37519 = cljs.core._STAR_print_newline_STAR_;
var _STAR_print_fn_STAR__orig_val__37395_37520 = cljs.core._STAR_print_fn_STAR_;
var _STAR_print_newline_STAR__temp_val__37396_37521 = true;
var _STAR_print_fn_STAR__temp_val__37397_37522 = (function (x__4668__auto__){
return sb__4667__auto__.append(x__4668__auto__);
});
(cljs.core._STAR_print_newline_STAR_ = _STAR_print_newline_STAR__temp_val__37396_37521);

(cljs.core._STAR_print_fn_STAR_ = _STAR_print_fn_STAR__temp_val__37397_37522);

try{cljs.spec.alpha.explain_out(cljs.core.update.cljs$core$IFn$_invoke$arity$3(spec,new cljs.core.Keyword("cljs.spec.alpha","problems","cljs.spec.alpha/problems",447400814),(function (probs){
return cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__37317_SHARP_){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(p1__37317_SHARP_,new cljs.core.Keyword(null,"in","in",-1531184865));
}),probs);
}))
);
}finally {(cljs.core._STAR_print_fn_STAR_ = _STAR_print_fn_STAR__orig_val__37395_37520);

(cljs.core._STAR_print_newline_STAR_ = _STAR_print_newline_STAR__orig_val__37394_37519);
}
return cljs.core.str.cljs$core$IFn$_invoke$arity$1(sb__4667__auto__);
})();
return (format.cljs$core$IFn$_invoke$arity$4 ? format.cljs$core$IFn$_invoke$arity$4(G__37390,G__37391,G__37392,G__37393) : format.call(null,G__37390,G__37391,G__37392,G__37393));
} else {
var G__37398 = "Execution error%s at %s(%s).\n%s\n";
var G__37399 = cause_type;
var G__37400 = (cljs.core.truth_(symbol)?[cljs.core.str.cljs$core$IFn$_invoke$arity$1(symbol)," "].join(''):"");
var G__37401 = loc;
var G__37402 = cause;
return (format.cljs$core$IFn$_invoke$arity$5 ? format.cljs$core$IFn$_invoke$arity$5(G__37398,G__37399,G__37400,G__37401,G__37402) : format.call(null,G__37398,G__37399,G__37400,G__37401,G__37402));
}

break;
default:
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__37328__$1)].join('')));

}
});
cljs.repl.error__GT_str = (function cljs$repl$error__GT_str(error){
return cljs.repl.ex_str(cljs.repl.ex_triage(cljs.repl.Error__GT_map(error)));
});

//# sourceMappingURL=cljs.repl.js.map
