goog.provide('oc.web.actions.payments');
oc.web.actions.payments.default_trial_status = "trialing";
oc.web.actions.payments.default_active_status = "active";
oc.web.actions.payments.default_trial_expired_status = "past_due";
oc.web.actions.payments.default_positive_statuses = cljs.core.PersistentHashSet.createAsIfByAssoc([oc.web.actions.payments.default_active_status,oc.web.actions.payments.default_trial_status]);
oc.web.actions.payments.get_payments_cb = (function oc$web$actions$payments$get_payments_cb(org_slug,p__38204){
var map__38205 = p__38204;
var map__38205__$1 = (((((!((map__38205 == null))))?(((((map__38205.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38205.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38205):map__38205);
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38205__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38205__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38205__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var payments_data = (cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,(404)))?new cljs.core.Keyword(null,"404","404",948666615):null
));
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"payments","payments",-1324138047),org_slug,payments_data], null));
});
oc.web.actions.payments.get_payments = (function oc$web$actions$payments$get_payments(payments_link){
if(oc.web.local_settings.payments_enabled){
return oc.web.api.get_payments(payments_link,cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.actions.payments.get_payments_cb,oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0()));
} else {
return null;
}
});
oc.web.actions.payments.maybe_load_payments_data = (function oc$web$actions$payments$maybe_load_payments_data(var_args){
var args__4742__auto__ = [];
var len__4736__auto___38241 = arguments.length;
var i__4737__auto___38242 = (0);
while(true){
if((i__4737__auto___38242 < len__4736__auto___38241)){
args__4742__auto__.push((arguments[i__4737__auto___38242]));

var G__38243 = (i__4737__auto___38242 + (1));
i__4737__auto___38242 = G__38243;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.actions.payments.maybe_load_payments_data.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.actions.payments.maybe_load_payments_data.cljs$core$IFn$_invoke$arity$variadic = (function (org_data,p__38209){
var vec__38210 = p__38209;
var force_refresh_QMARK_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38210,(0),null);
if(oc.web.local_settings.payments_enabled){
var payments_data = oc.web.dispatcher.payments_data.cljs$core$IFn$_invoke$arity$0();
if(cljs.core.truth_((function (){var and__4115__auto__ = org_data;
if(cljs.core.truth_(and__4115__auto__)){
var or__4126__auto__ = cljs.core.not(payments_data);
if(or__4126__auto__){
return or__4126__auto__;
} else {
return force_refresh_QMARK_;
}
} else {
return and__4115__auto__;
}
})())){
var temp__5735__auto__ = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"payments"], 0));
if(cljs.core.truth_(temp__5735__auto__)){
var payments_link = temp__5735__auto__;
return oc.web.actions.payments.get_payments(payments_link);
} else {
return null;
}
} else {
return null;
}
} else {
return null;
}
}));

(oc.web.actions.payments.maybe_load_payments_data.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.actions.payments.maybe_load_payments_data.cljs$lang$applyTo = (function (seq38207){
var G__38208 = cljs.core.first(seq38207);
var seq38207__$1 = cljs.core.next(seq38207);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__38208,seq38207__$1);
}));

oc.web.actions.payments.create_plan_subscription = (function oc$web$actions$payments$create_plan_subscription(var_args){
var args__4742__auto__ = [];
var len__4736__auto___38244 = arguments.length;
var i__4737__auto___38245 = (0);
while(true){
if((i__4737__auto___38245 < len__4736__auto___38244)){
args__4742__auto__.push((arguments[i__4737__auto___38245]));

var G__38246 = (i__4737__auto___38245 + (1));
i__4737__auto___38245 = G__38246;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((2) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((2)),(0),null)):null);
return oc.web.actions.payments.create_plan_subscription.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__4743__auto__);
});

(oc.web.actions.payments.create_plan_subscription.cljs$core$IFn$_invoke$arity$variadic = (function (payments_data,plan_id,p__38216){
var vec__38217 = p__38216;
var callback = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38217,(0),null);
var create_subscription_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(payments_data),"create"], 0));
var org_slug = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
if(cljs.core.truth_(create_subscription_link)){
return oc.web.api.update_plan_subscription(create_subscription_link,plan_id,(function (p__38220){
var map__38221 = p__38220;
var map__38221__$1 = (((((!((map__38221 == null))))?(((((map__38221.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38221.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38221):map__38221);
var resp = map__38221__$1;
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38221__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38221__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38221__$1,new cljs.core.Keyword(null,"success","success",1890645906));
oc.web.actions.payments.get_payments_cb(org_slug,resp);

return (callback.cljs$core$IFn$_invoke$arity$1 ? callback.cljs$core$IFn$_invoke$arity$1(success) : callback.call(null,success));
}));
} else {
return null;
}
}));

(oc.web.actions.payments.create_plan_subscription.cljs$lang$maxFixedArity = (2));

/** @this {Function} */
(oc.web.actions.payments.create_plan_subscription.cljs$lang$applyTo = (function (seq38213){
var G__38214 = cljs.core.first(seq38213);
var seq38213__$1 = cljs.core.next(seq38213);
var G__38215 = cljs.core.first(seq38213__$1);
var seq38213__$2 = cljs.core.next(seq38213__$1);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__38214,G__38215,seq38213__$2);
}));

oc.web.actions.payments.delete_plan_subscription = (function oc$web$actions$payments$delete_plan_subscription(var_args){
var args__4742__auto__ = [];
var len__4736__auto___38247 = arguments.length;
var i__4737__auto___38248 = (0);
while(true){
if((i__4737__auto___38248 < len__4736__auto___38247)){
args__4742__auto__.push((arguments[i__4737__auto___38248]));

var G__38249 = (i__4737__auto___38248 + (1));
i__4737__auto___38248 = G__38249;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.actions.payments.delete_plan_subscription.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.actions.payments.delete_plan_subscription.cljs$core$IFn$_invoke$arity$variadic = (function (payments_data,p__38225){
var vec__38226 = p__38225;
var callback = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38226,(0),null);
var delete_subscription_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(payments_data),"delete"], 0));
var org_slug = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
if(cljs.core.truth_(delete_subscription_link)){
return oc.web.api.update_plan_subscription(delete_subscription_link,null,(function (p__38229){
var map__38230 = p__38229;
var map__38230__$1 = (((((!((map__38230 == null))))?(((((map__38230.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38230.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38230):map__38230);
var resp = map__38230__$1;
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38230__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38230__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38230__$1,new cljs.core.Keyword(null,"success","success",1890645906));
oc.web.actions.payments.get_payments_cb(org_slug,resp);

return (callback.cljs$core$IFn$_invoke$arity$1 ? callback.cljs$core$IFn$_invoke$arity$1(success) : callback.call(null,success));
}));
} else {
return null;
}
}));

(oc.web.actions.payments.delete_plan_subscription.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.actions.payments.delete_plan_subscription.cljs$lang$applyTo = (function (seq38223){
var G__38224 = cljs.core.first(seq38223);
var seq38223__$1 = cljs.core.next(seq38223);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__38224,seq38223__$1);
}));

oc.web.actions.payments.add_payment_method = (function oc$web$actions$payments$add_payment_method(var_args){
var args__4742__auto__ = [];
var len__4736__auto___38250 = arguments.length;
var i__4737__auto___38251 = (0);
while(true){
if((i__4737__auto___38251 < len__4736__auto___38250)){
args__4742__auto__.push((arguments[i__4737__auto___38251]));

var G__38252 = (i__4737__auto___38251 + (1));
i__4737__auto___38251 = G__38252;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.actions.payments.add_payment_method.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.actions.payments.add_payment_method.cljs$core$IFn$_invoke$arity$variadic = (function (payments_data,p__38234){
var vec__38235 = p__38234;
var change_plan_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38235,(0),null);
var fixed_payments_data = (function (){var or__4126__auto__ = payments_data;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.dispatcher.payments_data.cljs$core$IFn$_invoke$arity$0();
}
})();
var checkout_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(fixed_payments_data),"checkout"], 0));
var base_domain = ((oc.shared.useragent.mobile_app_QMARK_)?clojure.string.join.cljs$core$IFn$_invoke$arity$2("",cljs.core.butlast(oc.web.dispatcher.expo_deep_link_origin.cljs$core$IFn$_invoke$arity$0())):oc.web.local_settings.web_server_domain);
var base_redirect_url = [base_domain,oc.web.router.get_token(),"?org-settings=payments&result="].join('');
var success_redirect_url = [base_redirect_url,"true",(cljs.core.truth_(change_plan_data)?["&update-plan=",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(change_plan_data))].join(''):null)].join('');
var cancel_redirect_url = [base_redirect_url,"false"].join('');
return oc.web.api.get_checkout_session_id(checkout_link,success_redirect_url,cancel_redirect_url,(function (p__38238){
var map__38239 = p__38238;
var map__38239__$1 = (((((!((map__38239 == null))))?(((((map__38239.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38239.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38239):map__38239);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38239__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38239__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38239__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
if(cljs.core.truth_(success)){
var session_data = oc.web.lib.json.json__GT_cljs(body);
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"payments-checkout-session-id","payments-checkout-session-id",222054171),session_data], null));

return oc.web.utils.stripe.redirect_to_checkout.cljs$core$IFn$_invoke$arity$variadic(session_data,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(function (res){
if(cljs.core.truth_(res)){
return null;
} else {
var alert_data = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"icon","icon",1679606541),"/img/ML/trash.svg",new cljs.core.Keyword(null,"title","title",636505583),"Oops",new cljs.core.Keyword(null,"message","message",-406056002),"An error occurred, please try again.",new cljs.core.Keyword(null,"solid-button-style","solid-button-style",-723792244),new cljs.core.Keyword(null,"red","red",-969428204),new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"OK, got it",new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),oc.web.components.ui.alert_modal.hide_alert], null);
return oc.web.components.ui.alert_modal.show_alert(alert_data);
}
})], 0));
} else {
return null;
}
}));
}));

(oc.web.actions.payments.add_payment_method.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.actions.payments.add_payment_method.cljs$lang$applyTo = (function (seq38232){
var G__38233 = cljs.core.first(seq38232);
var seq38232__$1 = cljs.core.next(seq38232);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__38233,seq38232__$1);
}));

oc.web.actions.payments.get_active_subscription = (function oc$web$actions$payments$get_active_subscription(payments_data){
return cljs.core.last(new cljs.core.Keyword(null,"subscriptions","subscriptions",1250949776).cljs$core$IFn$_invoke$arity$1(payments_data));
});
oc.web.actions.payments.get_current_subscription = (function oc$web$actions$payments$get_current_subscription(payments_data){
return cljs.core.first(new cljs.core.Keyword(null,"subscriptions","subscriptions",1250949776).cljs$core$IFn$_invoke$arity$1(payments_data));
});
/**
 * Given the loaded payments data return true if the UI needs to show the paywall and prevent any publish.
 *   Condition to show the paywall, or:
 *   - status different than trialing/active
 */
oc.web.actions.payments.show_paywall_alert_QMARK_ = (function oc$web$actions$payments$show_paywall_alert_QMARK_(payments_data){
var fixed_payments_data = (function (){var or__4126__auto__ = payments_data;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.dispatcher.payments_data.cljs$core$IFn$_invoke$arity$0();
}
})();
var subscription_data = oc.web.actions.payments.get_current_subscription(fixed_payments_data);
var subscription_status = new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(subscription_data);
var is_trial_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(fixed_payments_data),oc.web.actions.payments.default_trial_status);
var trial_expired_QMARK_ = ((new cljs.core.Keyword(null,"trial-end","trial-end",1137155148).cljs$core$IFn$_invoke$arity$1(fixed_payments_data) * (1000)) > (new Date()).getDate());
var period_expired_QMARK_ = ((new cljs.core.Keyword(null,"current-period-end","current-period-end",-1440884990).cljs$core$IFn$_invoke$arity$1(fixed_payments_data) * (1000)) > (new Date()).getDate());
if(oc.web.local_settings.payments_enabled){
var and__4115__auto___38253 = fixed_payments_data;
if(cljs.core.truth_(and__4115__auto___38253)){
((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(fixed_payments_data,new cljs.core.Keyword(null,"404","404",948666615))) || (((cljs.core.map_QMARK_(subscription_data)) && (cljs.core.not((oc.web.actions.payments.default_positive_statuses.cljs$core$IFn$_invoke$arity$1 ? oc.web.actions.payments.default_positive_statuses.cljs$core$IFn$_invoke$arity$1(subscription_status) : oc.web.actions.payments.default_positive_statuses.call(null,subscription_status)))))));
} else {
}
} else {
}

return false;
});

//# sourceMappingURL=oc.web.actions.payments.js.map
