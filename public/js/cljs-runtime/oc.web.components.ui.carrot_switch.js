goog.provide('oc.web.components.ui.carrot_switch');
oc.web.components.ui.carrot_switch.carrot_switch = rum.core.build_defc((function (p__39480){
var map__39481 = p__39480;
var map__39481__$1 = (((((!((map__39481 == null))))?(((((map__39481.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__39481.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__39481):map__39481);
var selected = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39481__$1,new cljs.core.Keyword(null,"selected","selected",574897764));
var disabled = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39481__$1,new cljs.core.Keyword(null,"disabled","disabled",-1529784218));
var did_change_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39481__$1,new cljs.core.Keyword(null,"did-change-cb","did-change-cb",116554135));
var selected_label = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39481__$1,new cljs.core.Keyword(null,"selected-label","selected-label",-902280703));
var unselected_label = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39481__$1,new cljs.core.Keyword(null,"unselected-label","unselected-label",-72520568));
return React.createElement("div",({"onClick": (function (){
var G__39486 = cljs.core.not(selected);
return (did_change_cb.cljs$core$IFn$_invoke$arity$1 ? did_change_cb.cljs$core$IFn$_invoke$arity$1(G__39486) : did_change_cb.call(null,G__39486));
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["carrot-switch",oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"selected","selected",574897764),selected,new cljs.core.Keyword(null,"disabled","disabled",-1529784218),disabled], null))], null))}),sablono.interpreter.interpret((cljs.core.truth_((function (){var and__4115__auto__ = selected;
if(cljs.core.truth_(and__4115__auto__)){
return selected_label;
} else {
return and__4115__auto__;
}
})())?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.selected-label","span.selected-label",1615898060),selected_label], null):null)),React.createElement("span",({"className": "dot"})),sablono.interpreter.interpret((cljs.core.truth_(((cljs.core.not(selected))?unselected_label:false))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.unselected-label","span.unselected-label",-736719184),unselected_label], null):null)));
}),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$], null),"carrot-switch");

//# sourceMappingURL=oc.web.components.ui.carrot_switch.js.map
