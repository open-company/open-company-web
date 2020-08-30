goog.provide('oc.web.components.ui.user_avatar');
oc.web.components.ui.user_avatar.user_avatar_image = rum.core.build_defcs((function (s,user_data,p__45692){
var map__45693 = p__45692;
var map__45693__$1 = (((((!((map__45693 == null))))?(((((map__45693.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45693.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45693):map__45693);
var preferred_avatar_size = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__45693__$1,new cljs.core.Keyword(null,"preferred-avatar-size","preferred-avatar-size",498036456),(72));
var tooltip_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45693__$1,new cljs.core.Keyword(null,"tooltip?","tooltip?",-642753154));
var use_default = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.user-avatar","use-default","oc.web.components.ui.user-avatar/use-default",375539739).cljs$core$IFn$_invoke$arity$1(s));
var default_avatar = oc.web.stores.user.user_icon(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user_data));
var user_avatar_url = (cljs.core.truth_((function (){var or__4126__auto__ = use_default;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103).cljs$core$IFn$_invoke$arity$1(user_data));
}
})())?oc.web.lib.utils.cdn(default_avatar):(((preferred_avatar_size > (0)))?oc.web.images.optimize_user_avatar_url(new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103).cljs$core$IFn$_invoke$arity$1(user_data),preferred_avatar_size):new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103).cljs$core$IFn$_invoke$arity$1(user_data)
));
return React.createElement("div",({"data-user-id": new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user_data), "data-intercom-target": "User avatar dropdown", "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["user-avatar-img-container",oc.web.lib.utils.hide_class], null))}),React.createElement("div",({"className": "user-avatar-img-helper"})),React.createElement("img",({"src": user_avatar_url, "onError": (function (){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.user-avatar","use-default","oc.web.components.ui.user-avatar/use-default",375539739).cljs$core$IFn$_invoke$arity$1(s),true);
}), "data-toggle": (cljs.core.truth_(tooltip_QMARK_)?"tooltip":""), "data-placement": "top", "data-container": "body", "data-delay": "{\"show\":\"500\", \"hide\":\"0\"}", "title": (cljs.core.truth_(tooltip_QMARK_)?new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(user_data):""), "className": "user-avatar-img"})));
}),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$,rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.user-avatar","use-default","oc.web.components.ui.user-avatar/use-default",375539739)),oc.web.mixins.ui.refresh_tooltips_mixin], null),"user-avatar-image");
oc.web.components.ui.user_avatar.user_avatar = rum.core.build_defcs((function (s,p__45704){
var map__45705 = p__45704;
var map__45705__$1 = (((((!((map__45705 == null))))?(((((map__45705.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45705.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45705):map__45705);
var click_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45705__$1,new cljs.core.Keyword(null,"click-cb","click-cb",1953404727));
var is_mobile_QMARK_ = oc.web.lib.responsive.is_mobile_size_QMARK_();
var user_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915));
return React.createElement("button",({"type": "button", "onClick": (function (){
if(cljs.core.fn_QMARK_(click_cb)){
return (click_cb.cljs$core$IFn$_invoke$arity$0 ? click_cb.cljs$core$IFn$_invoke$arity$0() : click_cb.call(null));
} else {
return null;
}
}), "aria-haspopup": true, "aria-expanded": false, "className": "mlb-reset user-avatar-button group"}),sablono.interpreter.interpret((oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1(user_data) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,user_data))),sablono.interpreter.interpret((cljs.core.truth_(is_mobile_QMARK_)?null:new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.user-name","span.user-name",-1373633834),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(user_data)], null))));
}),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$,rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915)], 0))], null),"user-avatar");

//# sourceMappingURL=oc.web.components.ui.user_avatar.js.map
