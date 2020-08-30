goog.provide('oc.web.actions.routing');
oc.web.actions.routing.post_routing = (function oc$web$actions$routing$post_routing(){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("container","status","container/status",1617877110),oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$0(),true], null));
});
oc.web.actions.routing.maybe_404 = (function oc$web$actions$routing$maybe_404(var_args){
var G__42393 = arguments.length;
switch (G__42393) {
case 0:
return oc.web.actions.routing.maybe_404.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.actions.routing.maybe_404.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.routing.maybe_404.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.actions.routing.maybe_404.cljs$core$IFn$_invoke$arity$1(false);
}));

(oc.web.actions.routing.maybe_404.cljs$core$IFn$_invoke$arity$1 = (function (force_404_QMARK_){
if(cljs.core.truth_((function (){var or__4126__auto__ = oc.web.lib.jwt.jwt();
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = oc.web.lib.jwt.id_token();
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
return force_404_QMARK_;
}
}
})())){
return oc.web.router.redirect_404_BANG_();
} else {
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"show-login-wall","show-login-wall",148038757)], null));
}
}));

(oc.web.actions.routing.maybe_404.cljs$lang$maxFixedArity = 1);

oc.web.actions.routing.switch_org_dashboard = (function oc$web$actions$routing$switch_org_dashboard(org__$1){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"org-nav-out","org-nav-out",-1475673674),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org__$1)], null));

return oc.web.router.nav_BANG_(oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org__$1)));
});

//# sourceMappingURL=oc.web.actions.routing.js.map
