goog.provide('oc.web.stores.payments');
oc.web.stores.payments.attempting_checkout_key = new cljs.core.Keyword(null,"checkout-session-id","checkout-session-id",1075338447);
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"payments","payments",-1324138047),(function (db,p__50830){
var vec__50831 = p__50830;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__50831,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__50831,(1),null);
var payments_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__50831,(2),null);
var payments_key = oc.web.dispatcher.payments_key(org_slug);
return cljs.core.assoc_in(db,payments_key,payments_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"payments-checkout-session-id","payments-checkout-session-id",222054171),(function (db,p__50834){
var vec__50835 = p__50834;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__50835,(0),null);
var session_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__50835,(1),null);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"checkout-session-data","checkout-session-data",2130832343),session_data);
}));

//# sourceMappingURL=oc.web.stores.payments.js.map
