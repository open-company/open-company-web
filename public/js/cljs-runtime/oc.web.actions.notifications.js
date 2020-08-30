goog.provide('oc.web.actions.notifications');
oc.web.actions.notifications.default_expiration_time = (3);
oc.web.actions.notifications.potentially_show_desktop_notification_BANG_ = (function oc$web$actions$notifications$potentially_show_desktop_notification_BANG_(p__47744){
var map__47746 = p__47744;
var map__47746__$1 = (((((!((map__47746 == null))))?(((((map__47746.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__47746.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__47746):map__47746);
var notification_data = map__47746__$1;
var title = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__47746__$1,new cljs.core.Keyword(null,"title","title",636505583));
var click = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__47746__$1,new cljs.core.Keyword(null,"click","click",1912301393));
if(((oc.shared.useragent.desktop_app_QMARK_) && (cljs.core.not(window.OCCarrotDesktop.windowHasFocus())))){
var notif = (new Notification(title));
return (notif.onclick = (function (){
window.OCCarrotDesktop.showDesktopWindow();

return (click.cljs$core$IFn$_invoke$arity$0 ? click.cljs$core$IFn$_invoke$arity$0() : click.call(null));
}));
} else {
return null;
}
});
oc.web.actions.notifications.show_notification = (function oc$web$actions$notifications$show_notification(notification_data){
var expiration_time = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"expire","expire",-70657108).cljs$core$IFn$_invoke$arity$1(notification_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.actions.notifications.default_expiration_time;
}
})();
var fixed_notification_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(notification_data,new cljs.core.Keyword(null,"created-at","created-at",-89248644),(new Date()).getTime()),new cljs.core.Keyword(null,"expire","expire",-70657108),expiration_time);
oc.web.actions.notifications.potentially_show_desktop_notification_BANG_(fixed_notification_data);

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"notification-add","notification-add",-85956164),fixed_notification_data], null));
});
oc.web.actions.notifications.remove_notification = (function oc$web$actions$notifications$remove_notification(notification_data){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"notification-remove","notification-remove",339373948),notification_data], null));
});
oc.web.actions.notifications.remove_notification_by_id = (function oc$web$actions$notifications$remove_notification_by_id(notification_id){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"notification-remove-by-id","notification-remove-by-id",587468368),notification_id], null));
});

//# sourceMappingURL=oc.web.actions.notifications.js.map
