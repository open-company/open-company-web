goog.provide('oc.web.components.ui.refresh_button');
oc.web.components.ui.refresh_button.refresh_button = rum.core.build_defc((function (p__40081){
var map__40082 = p__40081;
var map__40082__$1 = (((((!((map__40082 == null))))?(((((map__40082.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__40082.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__40082):map__40082);
var click_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__40082__$1,new cljs.core.Keyword(null,"click-cb","click-cb",1953404727));
var message = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__40082__$1,new cljs.core.Keyword(null,"message","message",-406056002),"New updates available");
var button_copy = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__40082__$1,new cljs.core.Keyword(null,"button-copy","button-copy",754789266),"Refresh");
var visible = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__40082__$1,new cljs.core.Keyword(null,"visible","visible",-1024216805),true);
var class_name = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__40082__$1,new cljs.core.Keyword(null,"class-name","class-name",945142584),"");
var fixed_click_cb = ((cljs.core.fn_QMARK_(click_cb))?click_cb:(function (){
return oc.web.actions.user.initial_loading.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true], 0));
}));
return React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["refresh-button-container",oc.web.lib.utils.class_set(cljs.core.PersistentArrayMap.createAsIfByAssoc([new cljs.core.Keyword(null,"visible","visible",-1024216805),visible,class_name,true]))], null))}),React.createElement("div",({"className": "refresh-button-inner"}),(function (){var attrs40084 = message;
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"span",((cljs.core.map_QMARK_(attrs40084))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["comments-number"], null)], null),attrs40084], 0))):({"className": "comments-number"})),((cljs.core.map_QMARK_(attrs40084))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs40084)], null)));
})(),React.createElement("button",({"onClick": (function (p1__40080_SHARP_){
return (fixed_click_cb.cljs$core$IFn$_invoke$arity$1 ? fixed_click_cb.cljs$core$IFn$_invoke$arity$1(p1__40080_SHARP_) : fixed_click_cb.call(null,p1__40080_SHARP_));
}), "className": "mlb-reset refresh-button"}),sablono.interpreter.interpret(button_copy))));
}),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$], null),"refresh-button");

//# sourceMappingURL=oc.web.components.ui.refresh_button.js.map
