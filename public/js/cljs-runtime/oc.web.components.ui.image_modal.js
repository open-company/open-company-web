goog.provide('oc.web.components.ui.image_modal');
oc.web.components.ui.image_modal.image_modal = rum.core.build_defc((function (p__52499){
var map__52500 = p__52499;
var map__52500__$1 = (((((!((map__52500 == null))))?(((((map__52500.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__52500.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__52500):map__52500);
var src = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__52500__$1,new cljs.core.Keyword(null,"src","src",-1651076051));
return sablono.interpreter.interpret((cljs.core.truth_(src)?(function (){var close_cb = (function (){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"expand-image-src","expand-image-src",-899766588)], null),null], null));
});
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.image-modal-container","div.image-modal-container",-204903206),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),close_cb], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.image-modal-close","span.image-modal-close",510568550),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),close_cb], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"img.image-modal-content","img.image-modal-content",1695169758),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"src","src",-1651076051),src], null)], null)], null);
})():null));
}),null,"image-modal");

//# sourceMappingURL=oc.web.components.ui.image_modal.js.map
