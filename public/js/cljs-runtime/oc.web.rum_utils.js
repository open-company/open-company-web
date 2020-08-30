goog.provide('oc.web.rum_utils');
oc.web.rum_utils.app = rum.core.build_defcs((function (state,spec,component){
return sablono.interpreter.interpret((component.cljs$core$IFn$_invoke$arity$0 ? component.cljs$core$IFn$_invoke$arity$0() : component.call(null)));
}),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [org.martinklepsch.derivatives.rum_derivatives_STAR_(cljs.core.first),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"did-catch","did-catch",2139522313),(function (s,error,error_info){
oc.web.lib.sentry.capture_error_BANG_.cljs$core$IFn$_invoke$arity$2(error,error_info);

return s;
})], null)], null),"app");
oc.web.rum_utils.drv_root = (function oc$web$rum_utils$drv_root(p__33609){
var map__33623 = p__33609;
var map__33623__$1 = (((((!((map__33623 == null))))?(((((map__33623.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__33623.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__33623):map__33623);
var state = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__33623__$1,new cljs.core.Keyword(null,"state","state",-1988618099));
var drv_spec = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__33623__$1,new cljs.core.Keyword(null,"drv-spec","drv-spec",-1268834882));
var target = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__33623__$1,new cljs.core.Keyword(null,"target","target",253001721));
var component = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__33623__$1,new cljs.core.Keyword(null,"component","component",1555936782));
rum.core.unmount(target);

return rum.core.mount((oc.web.rum_utils.app.cljs$core$IFn$_invoke$arity$2 ? oc.web.rum_utils.app.cljs$core$IFn$_invoke$arity$2(drv_spec,component) : oc.web.rum_utils.app.call(null,drv_spec,component)),target);
});

//# sourceMappingURL=oc.web.rum_utils.js.map
