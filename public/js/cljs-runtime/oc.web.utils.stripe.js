goog.provide('oc.web.utils.stripe');
oc.web.utils.stripe.stripe_obj = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
oc.web.utils.stripe.init = (function oc$web$utils$stripe$init(){
return cljs.core.reset_BANG_(oc.web.utils.stripe.stripe_obj,(new Stripe(oc.web.local_settings.stripe_api_key)));
});
oc.web.utils.stripe.stripe = (function oc$web$utils$stripe$stripe(){
if((cljs.core.deref(oc.web.utils.stripe.stripe_obj) == null)){
oc.web.utils.stripe.init();
} else {
}

return cljs.core.deref(oc.web.utils.stripe.stripe_obj);
});
oc.web.utils.stripe.redirect_to_checkout = (function oc$web$utils$stripe$redirect_to_checkout(var_args){
var args__4742__auto__ = [];
var len__4736__auto___37970 = arguments.length;
var i__4737__auto___37971 = (0);
while(true){
if((i__4737__auto___37971 < len__4736__auto___37970)){
args__4742__auto__.push((arguments[i__4737__auto___37971]));

var G__37972 = (i__4737__auto___37971 + (1));
i__4737__auto___37971 = G__37972;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.utils.stripe.redirect_to_checkout.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.utils.stripe.redirect_to_checkout.cljs$core$IFn$_invoke$arity$variadic = (function (session_data,p__37964){
var vec__37965 = p__37964;
var callback = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37965,(0),null);
var s = oc.web.utils.stripe.stripe();
var checkout_promise = s.redirectToCheckout(cljs.core.clj__GT_js(session_data));
return checkout_promise.then((function (res){
if(cljs.core.fn_QMARK_(callback)){
return (callback.cljs$core$IFn$_invoke$arity$1 ? callback.cljs$core$IFn$_invoke$arity$1(res) : callback.call(null,res));
} else {
return null;
}
}));
}));

(oc.web.utils.stripe.redirect_to_checkout.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.utils.stripe.redirect_to_checkout.cljs$lang$applyTo = (function (seq37962){
var G__37963 = cljs.core.first(seq37962);
var seq37962__$1 = cljs.core.next(seq37962);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__37963,seq37962__$1);
}));


//# sourceMappingURL=oc.web.utils.stripe.js.map
