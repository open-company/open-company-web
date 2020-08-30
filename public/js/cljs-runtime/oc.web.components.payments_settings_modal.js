goog.provide('oc.web.components.payments_settings_modal');
oc.web.components.payments_settings_modal.change_tab = (function oc$web$components$payments_settings_modal$change_tab(s,tab){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [oc.web.dispatcher.checkout_result_key], null),null], null));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.payments-settings-modal","checkout-result","oc.web.components.payments-settings-modal/checkout-result",1164297983).cljs$core$IFn$_invoke$arity$1(s),null);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.payments-settings-modal","payments-tab","oc.web.components.payments-settings-modal/payments-tab",1657216204).cljs$core$IFn$_invoke$arity$1(s),tab);
});
oc.web.components.payments_settings_modal.plan_amount_to_human = (function oc$web$components$payments_settings_modal$plan_amount_to_human(amount,currency){
var int_plan_amount = ((amount / (100)) | (0));
var decimal_plan_amount_STAR_ = cljs.core.mod(amount,(100));
var decimal_plan_amount = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(((cljs.core.str.cljs$core$IFn$_invoke$arity$1(decimal_plan_amount_STAR_)).length),(1)))?["0",cljs.core.str.cljs$core$IFn$_invoke$arity$1(decimal_plan_amount_STAR_)].join(''):cljs.core.str.cljs$core$IFn$_invoke$arity$1(decimal_plan_amount_STAR_));
var currency_symbol = (function (){var G__46813 = currency;
switch (G__46813) {
case "usd":
return "$";

break;
case "eur":
return "\u20AC";

break;
default:
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(currency)," "].join('');

}
})();
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(currency_symbol),cljs.core.str.cljs$core$IFn$_invoke$arity$1(int_plan_amount),".",decimal_plan_amount].join('');
});
oc.web.components.payments_settings_modal.plan_price = (function oc$web$components$payments_settings_modal$plan_price(plan_data,quantity){
if(cljs.core.not(cljs.core.seq(new cljs.core.Keyword(null,"tiers","tiers",-46242789).cljs$core$IFn$_invoke$arity$1(plan_data)))){
return oc.web.components.payments_settings_modal.plan_amount_to_human((new cljs.core.Keyword(null,"amount","amount",364489504).cljs$core$IFn$_invoke$arity$1(plan_data) * quantity),new cljs.core.Keyword(null,"currency","currency",-898327568).cljs$core$IFn$_invoke$arity$1(plan_data));
} else {
var tier = cljs.core.first(cljs.core.filterv((function (p1__46814_SHARP_){
var or__4126__auto__ = (function (){var and__4115__auto__ = new cljs.core.Keyword(null,"up-to","up-to",2117292887).cljs$core$IFn$_invoke$arity$1(p1__46814_SHARP_);
if(cljs.core.truth_(and__4115__auto__)){
return (quantity <= new cljs.core.Keyword(null,"up-to","up-to",2117292887).cljs$core$IFn$_invoke$arity$1(p1__46814_SHARP_));
} else {
return and__4115__auto__;
}
})();
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.not(new cljs.core.Keyword(null,"up-to","up-to",2117292887).cljs$core$IFn$_invoke$arity$1(p1__46814_SHARP_));
}
}),new cljs.core.Keyword(null,"tiers","tiers",-46242789).cljs$core$IFn$_invoke$arity$1(plan_data)));
var tier_price = (cljs.core.truth_(new cljs.core.Keyword(null,"up-to","up-to",2117292887).cljs$core$IFn$_invoke$arity$1(tier))?(new cljs.core.Keyword(null,"flat-amount","flat-amount",-464197621).cljs$core$IFn$_invoke$arity$1(tier) + (quantity * new cljs.core.Keyword(null,"unit-amount","unit-amount",-670065832).cljs$core$IFn$_invoke$arity$1(tier))):(quantity * new cljs.core.Keyword(null,"unit-amount","unit-amount",-670065832).cljs$core$IFn$_invoke$arity$1(tier)));
return oc.web.components.payments_settings_modal.plan_amount_to_human(tier_price,new cljs.core.Keyword(null,"currency","currency",-898327568).cljs$core$IFn$_invoke$arity$1(plan_data));
}
});
oc.web.components.payments_settings_modal.plan_minimum_price = (function oc$web$components$payments_settings_modal$plan_minimum_price(plan_data){
var tier = cljs.core.first(new cljs.core.Keyword(null,"tiers","tiers",-46242789).cljs$core$IFn$_invoke$arity$1(plan_data));
return oc.web.components.payments_settings_modal.plan_amount_to_human(new cljs.core.Keyword(null,"flat-amount","flat-amount",-464197621).cljs$core$IFn$_invoke$arity$1(tier),new cljs.core.Keyword(null,"currency","currency",-898327568).cljs$core$IFn$_invoke$arity$1(plan_data));
});
oc.web.components.payments_settings_modal.price_per_user = (function oc$web$components$payments_settings_modal$price_per_user(plan_data){
var tier = cljs.core.second(new cljs.core.Keyword(null,"tiers","tiers",-46242789).cljs$core$IFn$_invoke$arity$1(plan_data));
return oc.web.components.payments_settings_modal.plan_amount_to_human(new cljs.core.Keyword(null,"unit-amount","unit-amount",-670065832).cljs$core$IFn$_invoke$arity$1(tier),new cljs.core.Keyword(null,"currency","currency",-898327568).cljs$core$IFn$_invoke$arity$1(plan_data));
});
oc.web.components.payments_settings_modal.plan_description = (function oc$web$components$payments_settings_modal$plan_description(plan_interval){
var G__46815 = plan_interval;
switch (G__46815) {
case "month":
return "monthly";

break;
default:
return "annual";

}
});
oc.web.components.payments_settings_modal.plan_label = (function oc$web$components$payments_settings_modal$plan_label(plan_nickname){
var G__46816 = plan_nickname;
switch (G__46816) {
case "Annual":
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(plan_nickname)," (save 20%)"].join('');

break;
default:
return plan_nickname;

}
});
oc.web.components.payments_settings_modal.date_string = (function oc$web$components$payments_settings_modal$date_string(linux_epoch){
return oc.web.lib.utils.js_date.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(linux_epoch * (1000))], 0)).toDateString();
});
oc.web.components.payments_settings_modal.is_trial_QMARK_ = (function oc$web$components$payments_settings_modal$is_trial_QMARK_(subs_data){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(subs_data),oc.web.actions.payments.default_trial_status);
});
oc.web.components.payments_settings_modal.is_trial_expired_QMARK_ = (function oc$web$components$payments_settings_modal$is_trial_expired_QMARK_(subs_data){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(subs_data),oc.web.actions.payments.default_trial_expired_status);
});
oc.web.components.payments_settings_modal.trial_remaining_days_string = (function oc$web$components$payments_settings_modal$trial_remaining_days_string(subscription_data){
var remaining_seconds = (new cljs.core.Keyword(null,"trial-end","trial-end",1137155148).cljs$core$IFn$_invoke$arity$1(subscription_data) - (oc.web.lib.utils.js_date().getTime() / (1000)));
var days_left = (((remaining_seconds / (((60) * (60)) * (24))) | (0)) + (1));
if((remaining_seconds < (0))){
return "Your trial has ended. Please select a plan to continue.";
} else {
return ["Your trial is set to expire in ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(days_left)," day",((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(days_left,(1)))?null:"s"),". Please choose a plan."].join('');
}
});
oc.web.components.payments_settings_modal.cancel_subscription = (function oc$web$components$payments_settings_modal$cancel_subscription(s,payments_data){
var alert_data = new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"title","title",636505583),"Are you sure?",new cljs.core.Keyword(null,"message","message",-406056002),"Are you sure you want to cancel your current plan?",new cljs.core.Keyword(null,"link-button-style","link-button-style",1552381990),new cljs.core.Keyword(null,"red","red",-969428204),new cljs.core.Keyword(null,"link-button-title","link-button-title",-812305976),"No, keep it",new cljs.core.Keyword(null,"link-button-cb","link-button-cb",559710301),(function (){
return oc.web.components.ui.alert_modal.hide_alert();
}),new cljs.core.Keyword(null,"solid-button-style","solid-button-style",-723792244),new cljs.core.Keyword(null,"green","green",-945526839),new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"Yes, cancel it",new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),(function (_){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.payments-settings-modal","canceling-subscription","oc.web.components.payments-settings-modal/canceling-subscription",1104452100).cljs$core$IFn$_invoke$arity$1(s),true);

oc.web.actions.payments.delete_plan_subscription.cljs$core$IFn$_invoke$arity$variadic(payments_data,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(function (){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.payments-settings-modal","canceling-subscription","oc.web.components.payments-settings-modal/canceling-subscription",1104452100).cljs$core$IFn$_invoke$arity$1(s),false);
})], 0));

return oc.web.components.ui.alert_modal.hide_alert();
})], null);
return oc.web.components.ui.alert_modal.show_alert(alert_data);
});
oc.web.components.payments_settings_modal.plan_summary = (function oc$web$components$payments_settings_modal$plan_summary(s,payments_data){
var subscription_data = oc.web.actions.payments.get_active_subscription(payments_data);
var next_payment_due = oc.web.components.payments_settings_modal.date_string(new cljs.core.Keyword(null,"next-payment-attempt","next-payment-attempt",-337205205).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"upcoming-invoice","upcoming-invoice",1243287178).cljs$core$IFn$_invoke$arity$1(payments_data)));
var current_plan = new cljs.core.Keyword(null,"plan","plan",1118952668).cljs$core$IFn$_invoke$arity$1(subscription_data);
var checkout_result = cljs.core.deref(new cljs.core.Keyword("oc.web.components.payments-settings-modal","checkout-result","oc.web.components.payments-settings-modal/checkout-result",1164297983).cljs$core$IFn$_invoke$arity$1(s));
var quantity = new cljs.core.Keyword(null,"quantity","quantity",-1929050694).cljs$core$IFn$_invoke$arity$1(subscription_data);
return new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.plan-summary","div.plan-summary",-1226706685),(cljs.core.truth_(subscription_data)?(cljs.core.truth_(new cljs.core.Keyword(null,"cancel-at-period-end?","cancel-at-period-end?",-239104172).cljs$core$IFn$_invoke$arity$1(subscription_data))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.plan-summary-details.success.bottom-margin","div.plan-summary-details.success.bottom-margin",-1756927987),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.emoji-icon","div.emoji-icon",1705651938),"\uD83D\uDDD3"], null),["Your ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cuerdas.core.lower(new cljs.core.Keyword(null,"nickname","nickname",-802027190).cljs$core$IFn$_invoke$arity$1(current_plan)))," plan is set to cancel on ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.components.payments_settings_modal.date_string(new cljs.core.Keyword(null,"current-period-end","current-period-end",-1440884990).cljs$core$IFn$_invoke$arity$1(subscription_data))),"."].join('')], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.plan-summary-details.success.bottom-margin","div.plan-summary-details.success.bottom-margin",-1756927987),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.emoji-icon","div.emoji-icon",1705651938),"\uD83D\uDC4D"], null),["Your ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cuerdas.core.lower(new cljs.core.Keyword(null,"nickname","nickname",-802027190).cljs$core$IFn$_invoke$arity$1(current_plan)))," plan is active."].join('')], null)):null),(cljs.core.truth_(subscription_data)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.plan-summary-separator.bottom-margin","div.plan-summary-separator.bottom-margin",43675844)], null):null),((cljs.core.seq(new cljs.core.Keyword(null,"payment-methods","payment-methods",-337606176).cljs$core$IFn$_invoke$arity$1(payments_data)))?new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.plan-summary-details","div.plan-summary-details",2079964181),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"strong","strong",269529000),"Payment methods"], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"br","br",934104792)], null),(function (){var iter__4529__auto__ = (function oc$web$components$payments_settings_modal$plan_summary_$_iter__46817(s__46818){
return (new cljs.core.LazySeq(null,(function (){
var s__46818__$1 = s__46818;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__46818__$1);
if(temp__5735__auto__){
var s__46818__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__46818__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__46818__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__46820 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__46819 = (0);
while(true){
if((i__46819 < size__4528__auto__)){
var c = cljs.core._nth(c__4527__auto__,i__46819);
var card = new cljs.core.Keyword(null,"card","card",-1430355152).cljs$core$IFn$_invoke$arity$1(c);
cljs.core.chunk_append(b__46820,new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.plan-summary-details-card-row","div.plan-summary-details-card-row",25017071),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"key","key",-1516042587),["pay-method-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(c))].join(''),new cljs.core.Keyword(null,"class","class",-2030961996),(cljs.core.truth_(new cljs.core.Keyword(null,"default?","default?",-1410951180).cljs$core$IFn$_invoke$arity$1(c))?null:"hidden")], null),(function (){var G__46821 = new cljs.core.Keyword(null,"brand","brand",557863343).cljs$core$IFn$_invoke$arity$1(card);
switch (G__46821) {
case "visa":
return "Visa";

break;
case "amex":
return "American Express";

break;
case "mastercard":
return "Mastercard";

break;
default:
return cuerdas.core.phrase(new cljs.core.Keyword(null,"brand","brand",557863343).cljs$core$IFn$_invoke$arity$1(card));

}
})(),((cljs.core.seq(new cljs.core.Keyword(null,"last-4","last-4",1867271884).cljs$core$IFn$_invoke$arity$1(card)))?[" ending in ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"last-4","last-4",1867271884).cljs$core$IFn$_invoke$arity$1(card))].join(''):null),", exp: ",oc.web.lib.utils.add_zero((new cljs.core.Keyword(null,"exp-month","exp-month",-583291489).cljs$core$IFn$_invoke$arity$1(card) | (0))),"/",new cljs.core.Keyword(null,"exp-year","exp-year",2074559848).cljs$core$IFn$_invoke$arity$1(card)], null));

var G__46866 = (i__46819 + (1));
i__46819 = G__46866;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__46820),oc$web$components$payments_settings_modal$plan_summary_$_iter__46817(cljs.core.chunk_rest(s__46818__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__46820),null);
}
} else {
var c = cljs.core.first(s__46818__$2);
var card = new cljs.core.Keyword(null,"card","card",-1430355152).cljs$core$IFn$_invoke$arity$1(c);
return cljs.core.cons(new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.plan-summary-details-card-row","div.plan-summary-details-card-row",25017071),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"key","key",-1516042587),["pay-method-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(c))].join(''),new cljs.core.Keyword(null,"class","class",-2030961996),(cljs.core.truth_(new cljs.core.Keyword(null,"default?","default?",-1410951180).cljs$core$IFn$_invoke$arity$1(c))?null:"hidden")], null),(function (){var G__46822 = new cljs.core.Keyword(null,"brand","brand",557863343).cljs$core$IFn$_invoke$arity$1(card);
switch (G__46822) {
case "visa":
return "Visa";

break;
case "amex":
return "American Express";

break;
case "mastercard":
return "Mastercard";

break;
default:
return cuerdas.core.phrase(new cljs.core.Keyword(null,"brand","brand",557863343).cljs$core$IFn$_invoke$arity$1(card));

}
})(),((cljs.core.seq(new cljs.core.Keyword(null,"last-4","last-4",1867271884).cljs$core$IFn$_invoke$arity$1(card)))?[" ending in ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"last-4","last-4",1867271884).cljs$core$IFn$_invoke$arity$1(card))].join(''):null),", exp: ",oc.web.lib.utils.add_zero((new cljs.core.Keyword(null,"exp-month","exp-month",-583291489).cljs$core$IFn$_invoke$arity$1(card) | (0))),"/",new cljs.core.Keyword(null,"exp-year","exp-year",2074559848).cljs$core$IFn$_invoke$arity$1(card)], null),oc$web$components$payments_settings_modal$plan_summary_$_iter__46817(cljs.core.rest(s__46818__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(new cljs.core.Keyword(null,"payment-methods","payment-methods",-337606176).cljs$core$IFn$_invoke$arity$1(payments_data));
})(),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.change-pay-method-bt","button.mlb-reset.change-pay-method-bt",-774374083),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.actions.payments.add_payment_method(payments_data);
})], null),"Update payment information"], null)], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.plan-summary-details","div.plan-summary-details",2079964181),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.change-pay-method-bt","button.mlb-reset.change-pay-method-bt",-774374083),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.actions.payments.add_payment_method(payments_data);
})], null),"Subscribe to Wut"], null)], null)),((((oc.web.components.payments_settings_modal.is_trial_QMARK_(subscription_data)) || (oc.web.components.payments_settings_modal.is_trial_expired_QMARK_(subscription_data))))?new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.plan-summary-details.bottom-margin","div.plan-summary-details.bottom-margin",-405307228),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"strong","strong",269529000),"Trial"], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"br","br",934104792)], null),oc.web.components.payments_settings_modal.trial_remaining_days_string(subscription_data)], null):null),(cljs.core.truth_(subscription_data)?new cljs.core.PersistentVector(null, 11, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.plan-summary-details.bottom-margin","div.plan-summary-details.bottom-margin",-405307228),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"strong","strong",269529000),"Billing period"], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"br","br",934104792)], null),"Plan billed ",oc.web.components.payments_settings_modal.plan_description(new cljs.core.Keyword(null,"interval","interval",1708495417).cljs$core$IFn$_invoke$arity$1(current_plan))," (",oc.web.components.payments_settings_modal.plan_price(current_plan,quantity),")",new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"br","br",934104792)], null),(cljs.core.truth_(new cljs.core.Keyword(null,"cancel-at-period-end?","cancel-at-period-end?",-239104172).cljs$core$IFn$_invoke$arity$1(subscription_data))?["Your plan is scheduled to cancel on ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.components.payments_settings_modal.date_string(new cljs.core.Keyword(null,"current-period-end","current-period-end",-1440884990).cljs$core$IFn$_invoke$arity$1(subscription_data)))].join(''):["Next payment due on ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(next_payment_due)].join('')),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.change-pay-method-bt","button.mlb-reset.change-pay-method-bt",-774374083),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.components.payments_settings_modal.change_tab(s,new cljs.core.Keyword(null,"change","change",-1163046502));
})], null),"Change"], null)], null):null),(cljs.core.truth_((function (){var and__4115__auto__ = subscription_data;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(new cljs.core.Keyword(null,"cancel-at-period-end?","cancel-at-period-end?",-239104172).cljs$core$IFn$_invoke$arity$1(subscription_data));
} else {
return and__4115__auto__;
}
})())?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.plan-summary-details","div.plan-summary-details",2079964181),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.cancel-subscription-bt","button.mlb-reset.cancel-subscription-bt",631150605),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.components.payments_settings_modal.cancel_subscription(s,payments_data);
})], null),"Cancel subscription"], null),(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.payments-settings-modal","canceling-subscription","oc.web.components.payments-settings-modal/canceling-subscription",1104452100).cljs$core$IFn$_invoke$arity$1(s)))?(oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.small_loading.small_loading.call(null)):null)], null):null),null], null);
});
oc.web.components.payments_settings_modal.show_error_alert = (function oc$web$components$payments_settings_modal$show_error_alert(s){
var alert_data = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"icon","icon",1679606541),"/img/ML/trash.svg",new cljs.core.Keyword(null,"title","title",636505583),"Oops",new cljs.core.Keyword(null,"message","message",-406056002),"An error occurred while saving your change of plan, please try again.",new cljs.core.Keyword(null,"solid-button-style","solid-button-style",-723792244),new cljs.core.Keyword(null,"red","red",-969428204),new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"OK, got it",new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),oc.web.components.ui.alert_modal.hide_alert], null);
return oc.web.components.ui.alert_modal.show_alert(alert_data);
});
oc.web.components.payments_settings_modal.default_minimum_price = (6000);
oc.web.components.payments_settings_modal.save_plan_change = (function oc$web$components$payments_settings_modal$save_plan_change(s,payments_data,current_plan_data){
return oc.web.actions.payments.create_plan_subscription.cljs$core$IFn$_invoke$arity$variadic(payments_data,new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(current_plan_data),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(function (success){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.payments-settings-modal","saving-plan","oc.web.components.payments-settings-modal/saving-plan",-1661996787).cljs$core$IFn$_invoke$arity$1(s),false);

if(cljs.core.truth_(success)){
oc.web.components.payments_settings_modal.change_tab(s,new cljs.core.Keyword(null,"summary","summary",380847952));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.payments-settings-modal","initial-plan","oc.web.components.payments-settings-modal/initial-plan",-1398087311).cljs$core$IFn$_invoke$arity$1(s),new cljs.core.Keyword(null,"nickname","nickname",-802027190).cljs$core$IFn$_invoke$arity$1(current_plan_data));

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.payments-settings-modal","payments-plan","oc.web.components.payments-settings-modal/payments-plan",854807500).cljs$core$IFn$_invoke$arity$1(s),new cljs.core.Keyword(null,"nickname","nickname",-802027190).cljs$core$IFn$_invoke$arity$1(current_plan_data));
} else {
return oc.web.components.payments_settings_modal.show_error_alert(s);
}
})], 0));
});
oc.web.components.payments_settings_modal.different_plans_price = (function oc$web$components$payments_settings_modal$different_plans_price(plans_data,quantity){
var annual_plan_data = cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__46823_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"interval","interval",1708495417).cljs$core$IFn$_invoke$arity$1(p1__46823_SHARP_),"year");
}),plans_data));
var monthly_plan_data = cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__46824_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"interval","interval",1708495417).cljs$core$IFn$_invoke$arity$1(p1__46824_SHARP_),"month");
}),plans_data));
var annual_tier = cljs.core.first(cljs.core.filterv((function (p1__46825_SHARP_){
var or__4126__auto__ = (function (){var and__4115__auto__ = new cljs.core.Keyword(null,"up-to","up-to",2117292887).cljs$core$IFn$_invoke$arity$1(p1__46825_SHARP_);
if(cljs.core.truth_(and__4115__auto__)){
return (quantity <= new cljs.core.Keyword(null,"up-to","up-to",2117292887).cljs$core$IFn$_invoke$arity$1(p1__46825_SHARP_));
} else {
return and__4115__auto__;
}
})();
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.not(new cljs.core.Keyword(null,"up-to","up-to",2117292887).cljs$core$IFn$_invoke$arity$1(p1__46825_SHARP_));
}
}),new cljs.core.Keyword(null,"tiers","tiers",-46242789).cljs$core$IFn$_invoke$arity$1(annual_plan_data)));
var monthly_tier = cljs.core.first(cljs.core.filterv((function (p1__46826_SHARP_){
var or__4126__auto__ = (function (){var and__4115__auto__ = new cljs.core.Keyword(null,"up-to","up-to",2117292887).cljs$core$IFn$_invoke$arity$1(p1__46826_SHARP_);
if(cljs.core.truth_(and__4115__auto__)){
return (quantity <= new cljs.core.Keyword(null,"up-to","up-to",2117292887).cljs$core$IFn$_invoke$arity$1(p1__46826_SHARP_));
} else {
return and__4115__auto__;
}
})();
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.not(new cljs.core.Keyword(null,"up-to","up-to",2117292887).cljs$core$IFn$_invoke$arity$1(p1__46826_SHARP_));
}
}),new cljs.core.Keyword(null,"tiers","tiers",-46242789).cljs$core$IFn$_invoke$arity$1(monthly_plan_data)));
var annual_price = (((!((new cljs.core.Keyword(null,"up-to","up-to",2117292887).cljs$core$IFn$_invoke$arity$1(annual_tier) == null))))?(new cljs.core.Keyword(null,"flat-amount","flat-amount",-464197621).cljs$core$IFn$_invoke$arity$1(annual_tier) + (quantity * new cljs.core.Keyword(null,"unit-amount","unit-amount",-670065832).cljs$core$IFn$_invoke$arity$1(annual_tier))):((quantity | (0)) * (new cljs.core.Keyword(null,"unit-amount","unit-amount",-670065832).cljs$core$IFn$_invoke$arity$1(annual_tier) | (0))));
var monthly_price = (((!((new cljs.core.Keyword(null,"up-to","up-to",2117292887).cljs$core$IFn$_invoke$arity$1(monthly_tier) == null))))?(new cljs.core.Keyword(null,"flat-amount","flat-amount",-464197621).cljs$core$IFn$_invoke$arity$1(monthly_tier) + (quantity * new cljs.core.Keyword(null,"unit-amount","unit-amount",-670065832).cljs$core$IFn$_invoke$arity$1(monthly_tier))):(quantity * new cljs.core.Keyword(null,"unit-amount","unit-amount",-670065832).cljs$core$IFn$_invoke$arity$1(monthly_tier)));
var diff_price = ((monthly_price * (12)) - annual_price);
if((diff_price > (0))){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991)," An annual plan saves you ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"strong","strong",269529000),oc.web.components.payments_settings_modal.plan_amount_to_human(diff_price,new cljs.core.Keyword(null,"currency","currency",-898327568).cljs$core$IFn$_invoke$arity$1(annual_plan_data))], null)," per year."], null);
} else {
return null;
}
});
oc.web.components.payments_settings_modal.plan_change = (function oc$web$components$payments_settings_modal$plan_change(s,payments_data){
var initial_plan = cljs.core.deref(new cljs.core.Keyword("oc.web.components.payments-settings-modal","initial-plan","oc.web.components.payments-settings-modal/initial-plan",-1398087311).cljs$core$IFn$_invoke$arity$1(s));
var current_plan = new cljs.core.Keyword("oc.web.components.payments-settings-modal","payments-plan","oc.web.components.payments-settings-modal/payments-plan",854807500).cljs$core$IFn$_invoke$arity$1(s);
var subscription_data = oc.web.actions.payments.get_active_subscription(payments_data);
var quantity = new cljs.core.Keyword(null,"quantity","quantity",-1929050694).cljs$core$IFn$_invoke$arity$1(subscription_data);
var monthly_plan = cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__46827_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"interval","interval",1708495417).cljs$core$IFn$_invoke$arity$1(p1__46827_SHARP_),"month");
}),new cljs.core.Keyword(null,"available-plans","available-plans",1575808312).cljs$core$IFn$_invoke$arity$1(payments_data)));
var annual_plan = cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__46828_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"interval","interval",1708495417).cljs$core$IFn$_invoke$arity$1(p1__46828_SHARP_),"year");
}),new cljs.core.Keyword(null,"available-plans","available-plans",1575808312).cljs$core$IFn$_invoke$arity$1(payments_data)));
var current_plan_data = (cljs.core.truth_((function (){var and__4115__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.payments-settings-modal","plan-has-changed","oc.web.components.payments-settings-modal/plan-has-changed",1960933478).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(current_plan),initial_plan);
} else {
return and__4115__auto__;
}
})())?cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__46829_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"nickname","nickname",-802027190).cljs$core$IFn$_invoke$arity$1(p1__46829_SHARP_),cljs.core.deref(current_plan));
}),new cljs.core.Keyword(null,"available-plans","available-plans",1575808312).cljs$core$IFn$_invoke$arity$1(payments_data))):new cljs.core.Keyword(null,"plan","plan",1118952668).cljs$core$IFn$_invoke$arity$1(subscription_data));
var total_plan_price = oc.web.components.payments_settings_modal.plan_price(current_plan_data,quantity);
var different_plans_price_span = oc.web.components.payments_settings_modal.different_plans_price(new cljs.core.Keyword(null,"available-plans","available-plans",1575808312).cljs$core$IFn$_invoke$arity$1(payments_data),quantity);
var up_to = new cljs.core.Keyword(null,"up-to","up-to",2117292887).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword(null,"tiers","tiers",-46242789).cljs$core$IFn$_invoke$arity$1(current_plan_data)));
var flat_amount = oc.web.components.payments_settings_modal.plan_amount_to_human(new cljs.core.Keyword(null,"flat-amount","flat-amount",-464197621).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword(null,"tiers","tiers",-46242789).cljs$core$IFn$_invoke$arity$1(current_plan_data))),new cljs.core.Keyword(null,"currency","currency",-898327568).cljs$core$IFn$_invoke$arity$1(current_plan_data));
var unit_amount = ((cljs.core.seq(new cljs.core.Keyword(null,"tiers","tiers",-46242789).cljs$core$IFn$_invoke$arity$1(current_plan_data)))?oc.web.components.payments_settings_modal.plan_amount_to_human(new cljs.core.Keyword(null,"unit-amount","unit-amount",-670065832).cljs$core$IFn$_invoke$arity$1(cljs.core.second(new cljs.core.Keyword(null,"tiers","tiers",-46242789).cljs$core$IFn$_invoke$arity$1(current_plan_data))),new cljs.core.Keyword(null,"currency","currency",-898327568).cljs$core$IFn$_invoke$arity$1(current_plan_data)):oc.web.components.payments_settings_modal.plan_amount_to_human(new cljs.core.Keyword(null,"amount","amount",364489504).cljs$core$IFn$_invoke$arity$1(current_plan_data),new cljs.core.Keyword(null,"currency","currency",-898327568).cljs$core$IFn$_invoke$arity$1(current_plan_data)));
var available_plans_STAR_ = cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (p1__46830_SHARP_){
return cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"value","value",305978217),new cljs.core.Keyword(null,"label","label",1718410804)],[new cljs.core.Keyword(null,"nickname","nickname",-802027190).cljs$core$IFn$_invoke$arity$1(p1__46830_SHARP_),oc.web.components.payments_settings_modal.plan_label(new cljs.core.Keyword(null,"nickname","nickname",-802027190).cljs$core$IFn$_invoke$arity$1(p1__46830_SHARP_))]);
}),new cljs.core.Keyword(null,"available-plans","available-plans",1575808312).cljs$core$IFn$_invoke$arity$1(payments_data));
var contains_current_subscription_QMARK_ = cljs.core.some((function (p1__46831_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"value","value",305978217).cljs$core$IFn$_invoke$arity$1(p1__46831_SHARP_),new cljs.core.Keyword(null,"nickname","nickname",-802027190).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"plan","plan",1118952668).cljs$core$IFn$_invoke$arity$1(subscription_data)));
}),available_plans_STAR_);
var available_plans = (cljs.core.truth_(contains_current_subscription_QMARK_)?available_plans_STAR_:cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"value","value",305978217),new cljs.core.Keyword(null,"nickname","nickname",-802027190).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"plan","plan",1118952668).cljs$core$IFn$_invoke$arity$1(subscription_data)),new cljs.core.Keyword(null,"label","label",1718410804),oc.web.components.payments_settings_modal.plan_label(new cljs.core.Keyword(null,"nickname","nickname",-802027190).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"plan","plan",1118952668).cljs$core$IFn$_invoke$arity$1(subscription_data)))], null)], null),available_plans_STAR_));
var has_payment_info_QMARK_ = cljs.core.seq(new cljs.core.Keyword(null,"payment-methods","payment-methods",-337606176).cljs$core$IFn$_invoke$arity$1(payments_data));
var is_annual_default_plan_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"nickname","nickname",-802027190).cljs$core$IFn$_invoke$arity$1(current_plan_data),"Annual");
var is_under_up_to_QMARK_ = (quantity < up_to);
return new cljs.core.PersistentVector(null, 12, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.plan-change","div.plan-change",1785011505),((((((oc.web.components.payments_settings_modal.is_trial_QMARK_(subscription_data)) || (oc.web.components.payments_settings_modal.is_trial_expired_QMARK_(subscription_data)))) && (cljs.core.not(has_payment_info_QMARK_))))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.plan-change-details.expiration-trial.bottom-margin","div.plan-change-details.expiration-trial.bottom-margin",-190836342),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.emoji-icon","div.emoji-icon",1705651938),"\uD83D\uDDD3"], null),oc.web.components.payments_settings_modal.trial_remaining_days_string(subscription_data)], null):null),((((oc.web.components.payments_settings_modal.is_trial_QMARK_(subscription_data)) && (cljs.core.not(has_payment_info_QMARK_))))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.plan-change-separator.bottom-margin","div.plan-change-separator.bottom-margin",-404295202)], null):null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.plans-dropdown-bt","button.mlb-reset.plans-dropdown-bt",-241804061),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.payments-settings-modal","show-plans-dropdown","oc.web.components.payments-settings-modal/show-plans-dropdown",-109724178).cljs$core$IFn$_invoke$arity$1(s),true);
})], null),(function (){var or__4126__auto__ = new cljs.core.Keyword(null,"nickname","nickname",-802027190).cljs$core$IFn$_invoke$arity$1(current_plan_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "Free";
}
})()], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.plan-change-dropdown","div.plan-change-dropdown",-1628891153),(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.payments-settings-modal","show-plans-dropdown","oc.web.components.payments-settings-modal/show-plans-dropdown",-109724178).cljs$core$IFn$_invoke$arity$1(s)))?(function (){var G__46838 = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"items","items",1031954938),available_plans,new cljs.core.Keyword(null,"value","value",305978217),cljs.core.deref(current_plan),new cljs.core.Keyword(null,"on-blur","on-blur",814300747),(function (){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.payments-settings-modal","show-plans-dropdown","oc.web.components.payments-settings-modal/show-plans-dropdown",-109724178).cljs$core$IFn$_invoke$arity$1(s),false);
}),new cljs.core.Keyword(null,"on-change","on-change",-732046149),(function (selected_item){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.payments-settings-modal","plan-has-changed","oc.web.components.payments-settings-modal/plan-has-changed",1960933478).cljs$core$IFn$_invoke$arity$1(s),true);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.payments-settings-modal","show-plans-dropdown","oc.web.components.payments-settings-modal/show-plans-dropdown",-109724178).cljs$core$IFn$_invoke$arity$1(s),false);

return cljs.core.reset_BANG_(current_plan,new cljs.core.Keyword(null,"value","value",305978217).cljs$core$IFn$_invoke$arity$1(selected_item));
})], null);
return (oc.web.components.ui.dropdown_list.dropdown_list.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.dropdown_list.dropdown_list.cljs$core$IFn$_invoke$arity$1(G__46838) : oc.web.components.ui.dropdown_list.dropdown_list.call(null,G__46838));
})():null)], null),((is_under_up_to_QMARK_)?new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.plan-change-description","div.plan-change-description",1762665636),["The ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cuerdas.core.lower(new cljs.core.Keyword(null,"nickname","nickname",-802027190).cljs$core$IFn$_invoke$arity$1(current_plan_data)))," plan ",((is_annual_default_plan_QMARK_)?["is 20% lower than monthly. The ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cuerdas.core.lower(new cljs.core.Keyword(null,"nickname","nickname",-802027190).cljs$core$IFn$_invoke$arity$1(current_plan_data)))," plan "].join(''):null),"starts at "].join(''),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"strong","strong",269529000),flat_amount], null),[", which includes your first ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(up_to)," team members",". Then it's "].join(''),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"strong","strong",269529000),unit_amount], null)," per additional person.",((is_annual_default_plan_QMARK_)?different_plans_price_span:null)], null):new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.plan-change-description","div.plan-change-description",1762665636),["For your team of ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(quantity),((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(quantity,(1)))?" people":" person"),", your plan will cost "].join(''),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"strong","strong",269529000),total_plan_price], null),[" per ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"interval","interval",1708495417).cljs$core$IFn$_invoke$arity$1(current_plan_data))," (",cljs.core.str.cljs$core$IFn$_invoke$arity$1(quantity)," user",((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(quantity,(1)))?"s":null)," X ",unit_amount,")."].join(''),((is_annual_default_plan_QMARK_)?different_plans_price_span:null)], null)),(cljs.core.truth_((function (){var G__46843 = new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(subscription_data);
return (oc.web.actions.payments.default_positive_statuses.cljs$core$IFn$_invoke$arity$1 ? oc.web.actions.payments.default_positive_statuses.cljs$core$IFn$_invoke$arity$1(G__46843) : oc.web.actions.payments.default_positive_statuses.call(null,G__46843));
})())?null:new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.plan-change-title","div.plan-change-title",2136162798),["Due today: ",total_plan_price].join('')], null)),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.payment-info-bt","button.mlb-reset.payment-info-bt",800741605),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"disabled","disabled",-1529784218),(function (){var or__4126__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.payments-settings-modal","saving-plan","oc.web.components.payments-settings-modal/saving-plan",-1661996787).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = cljs.core.deref(new cljs.core.Keyword("oc.web.components.payments-settings-modal","canceling-subscription","oc.web.components.payments-settings-modal/canceling-subscription",1104452100).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
return ((has_payment_info_QMARK_) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(current_plan),initial_plan)) && (cljs.core.not(new cljs.core.Keyword(null,"cancel-at-period-end?","cancel-at-period-end?",-239104172).cljs$core$IFn$_invoke$arity$1(subscription_data))));
}
}
})(),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
if(has_payment_info_QMARK_){
if(cljs.core.truth_(new cljs.core.Keyword(null,"cancel-at-period-end?","cancel-at-period-end?",-239104172).cljs$core$IFn$_invoke$arity$1(subscription_data))){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.payments-settings-modal","saving-plan","oc.web.components.payments-settings-modal/saving-plan",-1661996787).cljs$core$IFn$_invoke$arity$1(s),true);

return oc.web.components.payments_settings_modal.save_plan_change(s,payments_data,current_plan_data);
} else {
var alert_data = new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"title","title",636505583),"Are you sure?",new cljs.core.Keyword(null,"message","message",-406056002),"Are you sure you want to change your current plan?",new cljs.core.Keyword(null,"link-button-style","link-button-style",1552381990),new cljs.core.Keyword(null,"red","red",-969428204),new cljs.core.Keyword(null,"link-button-title","link-button-title",-812305976),"No, keep it",new cljs.core.Keyword(null,"link-button-cb","link-button-cb",559710301),(function (){
return oc.web.components.ui.alert_modal.hide_alert();
}),new cljs.core.Keyword(null,"solid-button-style","solid-button-style",-723792244),new cljs.core.Keyword(null,"green","green",-945526839),new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"Yes, change it",new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),(function (_){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.payments-settings-modal","saving-plan","oc.web.components.payments-settings-modal/saving-plan",-1661996787).cljs$core$IFn$_invoke$arity$1(s),true);

oc.web.components.payments_settings_modal.save_plan_change(s,payments_data,current_plan_data);

return oc.web.components.ui.alert_modal.hide_alert();
})], null);
return oc.web.components.ui.alert_modal.show_alert(alert_data);
}
} else {
return oc.web.actions.payments.add_payment_method.cljs$core$IFn$_invoke$arity$variadic(payments_data,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(initial_plan,cljs.core.deref(current_plan)))?current_plan_data:null)], 0));
}
})], null),((has_payment_info_QMARK_)?(cljs.core.truth_(new cljs.core.Keyword(null,"cancel-at-period-end?","cancel-at-period-end?",-239104172).cljs$core$IFn$_invoke$arity$1(subscription_data))?"Subscribe to Wut":"Change plan"):"Subscribe to Wut")], null),(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.payments-settings-modal","saving-plan","oc.web.components.payments-settings-modal/saving-plan",-1661996787).cljs$core$IFn$_invoke$arity$1(s)))?(oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.small_loading.small_loading.call(null)):null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.plan-change-separator","div.plan-change-separator",-381354555)], null),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.plan-change-details","div.plan-change-details",-901909866),"Have a question about billing?",new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"br","br",934104792)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a.chat-with-us","a.chat-with-us",-233773837),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),"intercom-chat-link",new cljs.core.Keyword(null,"href","href",-793805698),oc.web.urls.contact_mail_to], null),"Chat with us"], null)], null),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.plan-change-details","div.plan-change-details",-901909866),"Team of 250+? ",new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a.chat-with-us","a.chat-with-us",-233773837),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),"intercom-chat-link",new cljs.core.Keyword(null,"href","href",-793805698),oc.web.urls.contact_mail_to], null),"Contact us"], null)," about an enterprise plan."], null)], null);
});
/**
 * Setup the view data, need to make sure the payments data have been loaded to show it.
 */
oc.web.components.payments_settings_modal.initial_setup = (function oc$web$components$payments_settings_modal$initial_setup(s){
var payments_data = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"payments","payments",-1324138047)));
var org_data = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"org-data","org-data",96720321)));
var current_user_data = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915)));
if(cljs.core.truth_((function (){var and__4115__auto__ = org_data;
if(cljs.core.truth_(and__4115__auto__)){
var and__4115__auto____$1 = current_user_data;
if(cljs.core.truth_(and__4115__auto____$1)){
var and__4115__auto____$2 = new cljs.core.Keyword(null,"role","role",-736691072).cljs$core$IFn$_invoke$arity$1(current_user_data);
if(cljs.core.truth_(and__4115__auto____$2)){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"role","role",-736691072).cljs$core$IFn$_invoke$arity$1(current_user_data),new cljs.core.Keyword(null,"admin","admin",-1239101627));
} else {
return and__4115__auto____$2;
}
} else {
return and__4115__auto____$1;
}
} else {
return and__4115__auto__;
}
})())){
oc.web.actions.nav_sidebar.close_all_panels();
} else {
}

if(cljs.core.truth_(((cljs.core.not(cljs.core.deref(new cljs.core.Keyword("oc.web.components.payments-settings-modal","initial-setup","oc.web.components.payments-settings-modal/initial-setup",-571922832).cljs$core$IFn$_invoke$arity$1(s))))?payments_data:false))){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.payments-settings-modal","initial-setup","oc.web.components.payments-settings-modal/initial-setup",-571922832).cljs$core$IFn$_invoke$arity$1(s),true);

var subscription_data = oc.web.actions.payments.get_active_subscription(payments_data);
var initial_plan_nickname = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"nickname","nickname",-802027190).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"plan","plan",1118952668).cljs$core$IFn$_invoke$arity$1(subscription_data));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "Monthly";
}
})();
var checkout_result = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,oc.web.dispatcher.checkout_result_key));
var has_payment_info_QMARK_ = cljs.core.seq(new cljs.core.Keyword(null,"payment-methods","payment-methods",-337606176).cljs$core$IFn$_invoke$arity$1(payments_data));
var updating_plan = (cljs.core.truth_(checkout_result)?cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,oc.web.dispatcher.checkout_update_plan_key)):null);
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.payments-settings-modal","payments-tab","oc.web.components.payments-settings-modal/payments-tab",1657216204).cljs$core$IFn$_invoke$arity$1(s),((((cljs.core.not(oc.web.actions.payments.get_active_subscription(payments_data))) || (cljs.core.not(has_payment_info_QMARK_))))?new cljs.core.Keyword(null,"change","change",-1163046502):new cljs.core.Keyword(null,"summary","summary",380847952)));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.payments-settings-modal","payments-plan","oc.web.components.payments-settings-modal/payments-plan",854807500).cljs$core$IFn$_invoke$arity$1(s),initial_plan_nickname);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.payments-settings-modal","initial-plan","oc.web.components.payments-settings-modal/initial-plan",-1398087311).cljs$core$IFn$_invoke$arity$1(s),initial_plan_nickname);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.payments-settings-modal","checkout-result","oc.web.components.payments-settings-modal/checkout-result",1164297983).cljs$core$IFn$_invoke$arity$1(s),checkout_result);
} else {
return null;
}
});
oc.web.components.payments_settings_modal.payments_settings_modal = rum.core.build_defcs((function (s,p__46852){
var map__46853 = p__46852;
var map__46853__$1 = (((((!((map__46853 == null))))?(((((map__46853.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46853.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46853):map__46853);
var org_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46853__$1,new cljs.core.Keyword(null,"org-data","org-data",96720321));
var org_data__$1 = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"org-data","org-data",96720321));
var payments_tab = new cljs.core.Keyword("oc.web.components.payments-settings-modal","payments-tab","oc.web.components.payments-settings-modal/payments-tab",1657216204).cljs$core$IFn$_invoke$arity$1(s);
var is_change_tab_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(payments_tab),new cljs.core.Keyword(null,"change","change",-1163046502));
var payments_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"payments","payments",-1324138047));
var has_payment_info_QMARK_ = cljs.core.seq(new cljs.core.Keyword(null,"payment-methods","payment-methods",-337606176).cljs$core$IFn$_invoke$arity$1(payments_data));
return React.createElement("div",({"className": "payments-settings-modal"}),React.createElement("button",({"onClick": (function (){
return oc.web.actions.nav_sidebar.close_all_panels();
}), "className": "mlb-reset modal-close-bt"})),React.createElement("div",({"className": "payments-settings-modal-container"}),React.createElement("div",({"className": "payments-settings-header group"}),(function (){var attrs46856 = (cljs.core.truth_(((is_change_tab_QMARK_)?payments_data:false))?((has_payment_info_QMARK_)?"Change plan":"Select a plan"):"Billing");
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46856))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["payments-settings-header-title"], null)], null),attrs46856], 0))):({"className": "payments-settings-header-title"})),((cljs.core.map_QMARK_(attrs46856))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46856)], null)));
})(),sablono.interpreter.interpret((cljs.core.truth_((function (){var and__4115__auto__ = payments_data;
if(cljs.core.truth_(and__4115__auto__)){
return (((!(is_change_tab_QMARK_))) && ((cljs.core.deref(new cljs.core.Keyword("oc.web.components.payments-settings-modal","checkout-result","oc.web.components.payments-settings-modal/checkout-result",1164297983).cljs$core$IFn$_invoke$arity$1(s)) == null)));
} else {
return and__4115__auto__;
}
})())?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.save-bt","button.mlb-reset.save-bt",1152501214),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.components.payments_settings_modal.change_tab(s,new cljs.core.Keyword(null,"change","change",-1163046502));
}),new cljs.core.Keyword(null,"disabled","disabled",-1529784218),(function (){var or__4126__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.payments-settings-modal","saving-plan","oc.web.components.payments-settings-modal/saving-plan",-1661996787).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.deref(new cljs.core.Keyword("oc.web.components.payments-settings-modal","canceling-subscription","oc.web.components.payments-settings-modal/canceling-subscription",1104452100).cljs$core$IFn$_invoke$arity$1(s));
}
})()], null),"Change plan"], null):null)),sablono.interpreter.interpret((cljs.core.truth_((function (){var and__4115__auto__ = payments_data;
if(cljs.core.truth_(and__4115__auto__)){
return (((cljs.core.deref(new cljs.core.Keyword("oc.web.components.payments-settings-modal","checkout-result","oc.web.components.payments-settings-modal/checkout-result",1164297983).cljs$core$IFn$_invoke$arity$1(s)) == null)) && (has_payment_info_QMARK_));
} else {
return and__4115__auto__;
}
})())?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.cancel-bt","button.mlb-reset.cancel-bt",617197847),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
if(is_change_tab_QMARK_){
return oc.web.components.payments_settings_modal.change_tab(s,new cljs.core.Keyword(null,"summary","summary",380847952));
} else {
return oc.web.actions.nav_sidebar.show_org_settings(null);
}
})], null),"Back"], null):null))),(function (){var attrs46855 = ((cljs.core.not(payments_data))?(oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.small_loading.small_loading.call(null)):((is_change_tab_QMARK_)?oc.web.components.payments_settings_modal.plan_change(s,payments_data):oc.web.components.payments_settings_modal.plan_summary(s,payments_data)));
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46855))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["payments-settings-body"], null)], null),attrs46855], 0))):({"className": "payments-settings-body"})),((cljs.core.map_QMARK_(attrs46855))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46855)], null)));
})()));
}),new cljs.core.PersistentVector(null, 17, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"org-data","org-data",96720321)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"payments","payments",-1324138047)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([oc.web.dispatcher.checkout_result_key], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([oc.web.dispatcher.checkout_update_plan_key], 0)),oc.web.mixins.ui.refresh_tooltips_mixin,rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.payments-settings-modal","initial-setup","oc.web.components.payments-settings-modal/initial-setup",-571922832)),rum.core.local.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"summary","summary",380847952),new cljs.core.Keyword("oc.web.components.payments-settings-modal","payments-tab","oc.web.components.payments-settings-modal/payments-tab",1657216204)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.payments-settings-modal","payments-plan","oc.web.components.payments-settings-modal/payments-plan",854807500)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.payments-settings-modal","show-plans-dropdown","oc.web.components.payments-settings-modal/show-plans-dropdown",-109724178)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.payments-settings-modal","initial-plan","oc.web.components.payments-settings-modal/initial-plan",-1398087311)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.payments-settings-modal","plan-has-changed","oc.web.components.payments-settings-modal/plan-has-changed",1960933478)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.payments-settings-modal","checkout-result","oc.web.components.payments-settings-modal/checkout-result",1164297983)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.payments-settings-modal","saving-plan","oc.web.components.payments-settings-modal/saving-plan",-1661996787)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.payments-settings-modal","canceling-subscription","oc.web.components.payments-settings-modal/canceling-subscription",1104452100)),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
oc.web.actions.payments.maybe_load_payments_data.cljs$core$IFn$_invoke$arity$variadic(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"org-data","org-data",96720321))),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true], 0));

oc.web.components.payments_settings_modal.initial_setup(s);

return s;
}),new cljs.core.Keyword(null,"will-update","will-update",328062998),(function (s){
oc.web.components.payments_settings_modal.initial_setup(s);

return s;
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [oc.web.dispatcher.checkout_result_key], null),null], null));

return s;
})], null)], null),"payments-settings-modal");

//# sourceMappingURL=oc.web.components.payments_settings_modal.js.map
