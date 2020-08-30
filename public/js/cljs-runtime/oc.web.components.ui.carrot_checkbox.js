goog.provide('oc.web.components.ui.carrot_checkbox');
oc.web.components.ui.carrot_checkbox.carrot_checkbox = rum.core.build_defc((function (p__39678){
var map__39679 = p__39678;
var map__39679__$1 = (((((!((map__39679 == null))))?(((((map__39679.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__39679.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__39679):map__39679);
var selected = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39679__$1,new cljs.core.Keyword(null,"selected","selected",574897764));
var disabled = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39679__$1,new cljs.core.Keyword(null,"disabled","disabled",-1529784218));
var did_change_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39679__$1,new cljs.core.Keyword(null,"did-change-cb","did-change-cb",116554135));
var tooltip = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39679__$1,new cljs.core.Keyword(null,"tooltip","tooltip",-1809677058));
var tooltip_placement = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39679__$1,new cljs.core.Keyword(null,"tooltip-placement","tooltip-placement",1392194953));
return React.createElement("div",({"onClick": (function (){
if(((cljs.core.not(disabled)) && (cljs.core.fn_QMARK_(did_change_cb)))){
var G__39681 = cljs.core.not(selected);
return (did_change_cb.cljs$core$IFn$_invoke$arity$1 ? did_change_cb.cljs$core$IFn$_invoke$arity$1(G__39681) : did_change_cb.call(null,G__39681));
} else {
return null;
}
}), "data-toggle": (cljs.core.truth_(tooltip)?"tooltip":""), "data-placement": (cljs.core.truth_(tooltip)?(function (){var or__4126__auto__ = tooltip_placement;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "top";
}
})():""), "data-container": "body", "title": tooltip, "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["carrot-checkbox",oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"selected","selected",574897764),selected,new cljs.core.Keyword(null,"disabled","disabled",-1529784218),disabled], null))], null))}));
}),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
$("[data-toggle=\"tooltip\"]",rum.core.dom_node(s)).tooltip();

return s;
}),new cljs.core.Keyword(null,"did-remount","did-remount",1362550500),(function (_,s){
$("[data-toggle=\"tooltip\"]",rum.core.dom_node(s)).each((function (p1__39677_SHARP_,p2__39676_SHARP_){
var G__39682 = $(p2__39676_SHARP_);
G__39682.tooltip("fixTitle");

G__39682.tooltip("hide");

return G__39682;
}));

return s;
})], null)], null),"carrot-checkbox");

//# sourceMappingURL=oc.web.components.ui.carrot_checkbox.js.map
