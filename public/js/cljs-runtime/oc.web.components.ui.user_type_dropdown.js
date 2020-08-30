goog.provide('oc.web.components.ui.user_type_dropdown');
oc.web.components.ui.user_type_dropdown.user_type_dropdown = rum.core.build_defc((function (p__39838){
var map__39839 = p__39838;
var map__39839__$1 = (((((!((map__39839 == null))))?(((((map__39839.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__39839.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__39839):map__39839);
var user_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39839__$1,new cljs.core.Keyword(null,"user-id","user-id",-206822291));
var user_type = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39839__$1,new cljs.core.Keyword(null,"user-type","user-type",738868936));
var on_change = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39839__$1,new cljs.core.Keyword(null,"on-change","on-change",-732046149));
var hide_admin = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39839__$1,new cljs.core.Keyword(null,"hide-admin","hide-admin",-823852536));
var on_remove = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39839__$1,new cljs.core.Keyword(null,"on-remove","on-remove",-268656163));
var disabled_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39839__$1,new cljs.core.Keyword(null,"disabled?","disabled?",-1523234181));
var user_dropdown_id = ["dropdown-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(user_id)].join('');
return React.createElement("div",({"className": "dropdown"}),React.createElement("button",({"id": user_dropdown_id, "data-toggle": "dropdown", "aria-haspopup": true, "aria-expanded": false, "disabled": disabled_QMARK_, "className": "btn-reset user-type-btn dropdown-toggle oc-input"}),sablono.interpreter.interpret(cuerdas.core.capital(oc.web.utils.user.user_role_string(user_type)))),React.createElement("ul",({"aria-labelledby": user_dropdown_id, "className": "dropdown-menu user-type-dropdown-menu"}),React.createElement("li",({"onClick": (function (){
if(cljs.core.fn_QMARK_(on_change)){
return (on_change.cljs$core$IFn$_invoke$arity$1 ? on_change.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"viewer","viewer",-783949853)) : on_change.call(null,new cljs.core.Keyword(null,"viewer","viewer",-783949853)));
} else {
return null;
}
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(user_type,new cljs.core.Keyword(null,"viewer","viewer",-783949853)))?"selected":null)], null))}),"Viewer"),React.createElement("li",({"onClick": (function (){
if(cljs.core.fn_QMARK_(on_change)){
return (on_change.cljs$core$IFn$_invoke$arity$1 ? on_change.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"author","author",2111686192)) : on_change.call(null,new cljs.core.Keyword(null,"author","author",2111686192)));
} else {
return null;
}
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(user_type,new cljs.core.Keyword(null,"author","author",2111686192)))?"selected":null)], null))}),"Contributor"),sablono.interpreter.interpret((cljs.core.truth_(hide_admin)?null:new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li","li",723558921),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
if(cljs.core.fn_QMARK_(on_change)){
return (on_change.cljs$core$IFn$_invoke$arity$1 ? on_change.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"admin","admin",-1239101627)) : on_change.call(null,new cljs.core.Keyword(null,"admin","admin",-1239101627)));
} else {
return null;
}
}),new cljs.core.Keyword(null,"class","class",-2030961996),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(user_type,new cljs.core.Keyword(null,"admin","admin",-1239101627)))?"selected":null)], null),"Admin"], null))),sablono.interpreter.interpret(((cljs.core.fn_QMARK_(on_remove))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li.remove-li","li.remove-li",1457583745),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return (on_remove.cljs$core$IFn$_invoke$arity$0 ? on_remove.cljs$core$IFn$_invoke$arity$0() : on_remove.call(null));
})], null),"Remove User"], null):null))));
}),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$], null),"user-type-dropdown");

//# sourceMappingURL=oc.web.components.ui.user_type_dropdown.js.map
