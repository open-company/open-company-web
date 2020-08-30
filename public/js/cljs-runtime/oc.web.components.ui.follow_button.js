goog.provide('oc.web.components.ui.follow_button');
oc.web.components.ui.follow_button.follow_button = rum.core.build_defc((function (p__38898){
var map__38899 = p__38898;
var map__38899__$1 = (((((!((map__38899 == null))))?(((((map__38899.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38899.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38899):map__38899);
var following = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38899__$1,new cljs.core.Keyword(null,"following","following",-2049193617));
var resource_type = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38899__$1,new cljs.core.Keyword(null,"resource-type","resource-type",1844262326));
var resource_uuid = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38899__$1,new cljs.core.Keyword(null,"resource-uuid","resource-uuid",1798351789));
var button_copy = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38899__$1,new cljs.core.Keyword(null,"button-copy","button-copy",754789266));
var disabled = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38899__$1,new cljs.core.Keyword(null,"disabled","disabled",-1529784218));
var map__38901 = ((cljs.core.map_QMARK_(button_copy))?button_copy:new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"active-on","active-on",-2047155083),"Subscribed",new cljs.core.Keyword(null,"active-off","active-off",-487236478),"Subscribe",new cljs.core.Keyword(null,"hover-on","hover-on",1667489962),"Unsubscribe",new cljs.core.Keyword(null,"hover-off","hover-off",1730461818),"Subscribe"], null));
var map__38901__$1 = (((((!((map__38901 == null))))?(((((map__38901.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38901.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38901):map__38901);
var active_on = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38901__$1,new cljs.core.Keyword(null,"active-on","active-on",-2047155083));
var active_off = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38901__$1,new cljs.core.Keyword(null,"active-off","active-off",-487236478));
var hover_on = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38901__$1,new cljs.core.Keyword(null,"hover-on","hover-on",1667489962));
var hover_off = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38901__$1,new cljs.core.Keyword(null,"hover-off","hover-off",1730461818));
return React.createElement("button",({"onClick": (function (){
if(cljs.core.truth_(disabled)){
return null;
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(resource_type,new cljs.core.Keyword(null,"board","board",-1907017633))){
return oc.web.actions.user.toggle_board(resource_uuid);
} else {
return oc.web.actions.user.toggle_publisher(resource_uuid);
}
}
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["mlb-reset","follow-button",oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"on","on",173873944),following,new cljs.core.Keyword(null,"off","off",606440789),cljs.core.not(following),new cljs.core.Keyword(null,"default-copy","default-copy",-371550177),(!(cljs.core.map_QMARK_(button_copy))),new cljs.core.Keyword(null,"disabled","disabled",-1529784218),disabled], null))], null))}),(function (){var attrs38903 = (cljs.core.truth_(following)?active_on:active_off);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"span",((cljs.core.map_QMARK_(attrs38903))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["main-title"], null)], null),attrs38903], 0))):({"className": "main-title"})),((cljs.core.map_QMARK_(attrs38903))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs38903)], null)));
})(),(function (){var attrs38904 = (cljs.core.truth_(following)?hover_on:hover_off);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"span",((cljs.core.map_QMARK_(attrs38904))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["hover-title"], null)], null),attrs38904], 0))):({"className": "hover-title"})),((cljs.core.map_QMARK_(attrs38904))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs38904)], null)));
})());
}),null,"follow-button");
oc.web.components.ui.follow_button.follow_banner = rum.core.build_defc((function (board_data){
return React.createElement("div",({"className": "follow-banner"}),(function (){var attrs38908 = ["Previewing ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(board_data))].join('');
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"span",((cljs.core.map_QMARK_(attrs38908))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["follow-banner-copy"], null)], null),attrs38908], 0))):({"className": "follow-banner-copy"})),((cljs.core.map_QMARK_(attrs38908))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs38908)], null)));
})(),sablono.interpreter.interpret((function (){var G__38909 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"resource-type","resource-type",1844262326),new cljs.core.Keyword(null,"board","board",-1907017633),new cljs.core.Keyword(null,"following","following",-2049193617),new cljs.core.Keyword(null,"following","following",-2049193617).cljs$core$IFn$_invoke$arity$1(board_data),new cljs.core.Keyword(null,"resource-uuid","resource-uuid",1798351789),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(board_data)], null);
return (oc.web.components.ui.follow_button.follow_button.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.follow_button.follow_button.cljs$core$IFn$_invoke$arity$1(G__38909) : oc.web.components.ui.follow_button.follow_button.call(null,G__38909));
})()));
}),null,"follow-banner");

//# sourceMappingURL=oc.web.components.ui.follow_button.js.map
