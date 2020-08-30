goog.provide('oc.web.components.ui.all_caught_up');
oc.web.components.ui.all_caught_up.all_caught_up = rum.core.build_defc((function (message){
return React.createElement("div",({"className": "all-caught-up"}),React.createElement("div",({"className": "all-caught-up-inner"}),(function (){var attrs53280 = (function (){var or__4126__auto__ = message;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "That\u2019s all for now!";
}
})();
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs53280))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["message"], null)], null),attrs53280], 0))):({"className": "message"})),((cljs.core.map_QMARK_(attrs53280))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs53280)], null)));
})()));
}),null,"all-caught-up");
oc.web.components.ui.all_caught_up.caught_up_line = rum.core.build_defc((function (p__53281){
var map__53282 = p__53281;
var map__53282__$1 = (((((!((map__53282 == null))))?(((((map__53282.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__53282.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__53282):map__53282);
var message = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__53282__$1,new cljs.core.Keyword(null,"message","message",-406056002));
var gray_style = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__53282__$1,new cljs.core.Keyword(null,"gray-style","gray-style",-1595613431));
return React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["caught-up-line",(cljs.core.truth_(gray_style)?"gray-style":null)], null))}),sablono.interpreter.interpret((oc.web.components.ui.all_caught_up.all_caught_up.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.all_caught_up.all_caught_up.cljs$core$IFn$_invoke$arity$1(message) : oc.web.components.ui.all_caught_up.all_caught_up.call(null,message))));
}),null,"caught-up-line");

//# sourceMappingURL=oc.web.components.ui.all_caught_up.js.map
