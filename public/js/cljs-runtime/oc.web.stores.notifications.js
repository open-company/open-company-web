goog.provide('oc.web.stores.notifications');
oc.web.stores.notifications.find_duplicate = (function oc$web$stores$notifications$find_duplicate(n_data,notifs){
if(cljs.core.truth_(new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(n_data))){
return cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__38698_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(p1__38698_SHARP_),new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(n_data));
}),notifs));
} else {
return null;
}
});
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"notification-add","notification-add",-85956164),(function (db,p__38700){
var vec__38701 = p__38700;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38701,(0),null);
var notification_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38701,(1),null);
var current_notifications = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,oc.web.dispatcher.notifications_key);
var dup = oc.web.stores.notifications.find_duplicate(notification_data,current_notifications);
var old_notifications = cljs.core.remove.cljs$core$IFn$_invoke$arity$2((function (p1__38699_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(p1__38699_SHARP_),new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(notification_data));
}),current_notifications);
var fixed_notification_data = (cljs.core.truth_(dup)?(((((new cljs.core.Keyword(null,"expire","expire",-70657108).cljs$core$IFn$_invoke$arity$1(notification_data) === (0))) || ((new cljs.core.Keyword(null,"expire","expire",-70657108).cljs$core$IFn$_invoke$arity$1(dup) === (0)))))?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(notification_data,new cljs.core.Keyword(null,"expire","expire",-70657108),(0)):cljs.core.update_in.cljs$core$IFn$_invoke$arity$4(notification_data,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"expire","expire",-70657108)], null),cljs.core.max,new cljs.core.Keyword(null,"expire","expire",-70657108).cljs$core$IFn$_invoke$arity$1(dup))):notification_data);
var next_notifications = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(old_notifications,fixed_notification_data);
return cljs.core.assoc_in(db,oc.web.dispatcher.notifications_key,next_notifications);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"notification-remove","notification-remove",339373948),(function (db,p__38705){
var vec__38706 = p__38705;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38706,(0),null);
var notification_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38706,(1),null);
var current_notifications = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,oc.web.dispatcher.notifications_key);
var next_notifications = cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__38704_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"created-at","created-at",-89248644).cljs$core$IFn$_invoke$arity$1(p1__38704_SHARP_),new cljs.core.Keyword(null,"created-at","created-at",-89248644).cljs$core$IFn$_invoke$arity$1(notification_data));
}),current_notifications);
return cljs.core.assoc_in(db,oc.web.dispatcher.notifications_key,next_notifications);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"notification-remove-by-id","notification-remove-by-id",587468368),(function (db,p__38710){
var vec__38711 = p__38710;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38711,(0),null);
var notification_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38711,(1),null);
var current_notifications = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,oc.web.dispatcher.notifications_key);
var next_notifications = cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__38709_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(p1__38709_SHARP_),notification_id);
}),current_notifications);
return cljs.core.assoc_in(db,oc.web.dispatcher.notifications_key,next_notifications);
}));

//# sourceMappingURL=oc.web.stores.notifications.js.map
