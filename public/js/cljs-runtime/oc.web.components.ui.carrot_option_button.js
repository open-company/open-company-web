goog.provide('oc.web.components.ui.carrot_option_button');
oc.web.components.ui.carrot_option_button.carrot_option_button = rum.core.build_defc((function (p__39957){
var map__39958 = p__39957;
var map__39958__$1 = (((((!((map__39958 == null))))?(((((map__39958.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__39958.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__39958):map__39958);
var selected = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39958__$1,new cljs.core.Keyword(null,"selected","selected",574897764));
var disabled = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39958__$1,new cljs.core.Keyword(null,"disabled","disabled",-1529784218));
var did_change_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39958__$1,new cljs.core.Keyword(null,"did-change-cb","did-change-cb",116554135));
return React.createElement("div",({"onClick": (function (){
if(cljs.core.truth_(disabled)){
return null;
} else {
var G__39960 = cljs.core.not(selected);
return (did_change_cb.cljs$core$IFn$_invoke$arity$1 ? did_change_cb.cljs$core$IFn$_invoke$arity$1(G__39960) : did_change_cb.call(null,G__39960));
}
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["carrot-option-button",oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"selected","selected",574897764),selected,new cljs.core.Keyword(null,"disabled","disabled",-1529784218),disabled], null))], null))}));
}),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$], null),"carrot-option-button");

//# sourceMappingURL=oc.web.components.ui.carrot_option_button.js.map
