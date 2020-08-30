goog.provide('oc.web.stores.routing');
oc.web.stores.routing.clean_route = (function oc$web$stores$routing$clean_route(route_map){
return cljs.core.update.cljs$core$IFn$_invoke$arity$3(route_map,new cljs.core.Keyword(null,"route","route",329891309),(function (p1__38473_SHARP_){
return cljs.core.set(cljs.core.map.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword,cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,p1__38473_SHARP_)));
}));
});
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"routing","routing",1440253662),(function (db,p__38477){
var vec__38478 = p__38477;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38478,(0),null);
var route = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38478,(1),null);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,oc.web.dispatcher.router_key,oc.web.stores.routing.clean_route(route));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"show-login-wall","show-login-wall",148038757),(function (db,p__38481){
var vec__38482 = p__38481;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38482,(0),null);
var route = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38482,(1),null);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"force-login-wall","force-login-wall",1907021508),true);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("route","rewrite","route/rewrite",711205411),(function (db,p__38490){
var vec__38491 = p__38490;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38491,(0),null);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38491,(1),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38491,(2),null);
return cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [oc.web.dispatcher.router_key,k], null),v);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"org-nav-out","org-nav-out",-1475673674),(function (db,p__38496){
var vec__38499 = p__38496;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38499,(0),null);
var from_org = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38499,(1),null);
var to_org = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38499,(2),null);
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"orgs-dropdown-visible","orgs-dropdown-visible",801323944),false),new cljs.core.Keyword(null,"mobile-navigation-sidebar","mobile-navigation-sidebar",-1723544081),false),cljs.core.first(oc.web.dispatcher.cmail_state_key)),cljs.core.first(oc.web.dispatcher.cmail_data_key));
}));

//# sourceMappingURL=oc.web.stores.routing.js.map
