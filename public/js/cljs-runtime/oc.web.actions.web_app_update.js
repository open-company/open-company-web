goog.provide('oc.web.actions.web_app_update');
oc.web.actions.web_app_update.default_update_interval_ms = (((1000) * (60)) * (5));
oc.web.actions.web_app_update.extended_update_interval_ms = ((((1000) * (60)) * (60)) * (24));
oc.web.actions.web_app_update.update_verbage = "Get the latest";
oc.web.actions.web_app_update.on_notification_dismissed = (function oc$web$actions$web_app_update$on_notification_dismissed(){
return oc.shared.interval.restart_interval_BANG_.cljs$core$IFn$_invoke$arity$2(oc.web.actions.web_app_update.web_app_update_interval,oc.web.actions.web_app_update.extended_update_interval_ms);
});
/**
 * Check for app updates, show the notification if necessary, set a new timeout else.
 */
oc.web.actions.web_app_update.real_web_app_update_check = (function oc$web$actions$web_app_update$real_web_app_update_check(){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.actions.web-app-update",null,26,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Checking for web app updates"], null);
}),null)),null,386358069);

oc.shared.interval.stop_interval_BANG_(oc.web.actions.web_app_update.web_app_update_interval);

return oc.web.api.web_app_version_check((function (p__38750){
var map__38751 = p__38750;
var map__38751__$1 = (((((!((map__38751 == null))))?(((((map__38751.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38751.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38751):map__38751);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38751__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38751__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38751__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,(404))){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.actions.web-app-update",null,32,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["New app update avalable! Showing notification to the user"], null);
}),null)),null,-804039663);

var click_cb = (function (e){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"loading","loading",-737050189)], null),true], null));

if(cljs.core.truth_(e)){
oc.web.lib.utils.event_stop(e);
} else {
}

return window.location.reload();
});
return oc.web.actions.notifications.show_notification(cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"secondary-bt-title","secondary-bt-title",70655776),new cljs.core.Keyword(null,"secondary-bt-style","secondary-bt-style",-210591485),new cljs.core.Keyword(null,"dismiss","dismiss",412569545),new cljs.core.Keyword(null,"secondary-bt-class","secondary-bt-class",-645630166),new cljs.core.Keyword(null,"expire","expire",-70657108),new cljs.core.Keyword(null,"title","title",636505583),new cljs.core.Keyword(null,"click","click",1912301393),new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"secondary-bt-cb","secondary-bt-cb",-1097849386),new cljs.core.Keyword(null,"secondary-bt-dismiss","secondary-bt-dismiss",-382072295),new cljs.core.Keyword(null,"web-app-update","web-app-update",-162792996),new cljs.core.Keyword(null,"dismiss-x","dismiss-x",991922748)],[oc.web.actions.web_app_update.update_verbage,new cljs.core.Keyword(null,"green","green",-945526839),oc.web.actions.web_app_update.on_notification_dismissed,new cljs.core.Keyword(null,"update-app-bt","update-app-bt",189024324),(0),"New version of Wut available!",click_cb,new cljs.core.Keyword(null,"web-app-update-error","web-app-update-error",663471633),click_cb,true,true,true]));
} else {
return oc.shared.interval.restart_interval_BANG_.cljs$core$IFn$_invoke$arity$2(oc.web.actions.web_app_update.web_app_update_interval,oc.web.actions.web_app_update.default_update_interval_ms);
}
}));
});
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.actions !== 'undefined') && (typeof oc.web.actions.web_app_update !== 'undefined') && (typeof oc.web.actions.web_app_update.web_app_update_interval !== 'undefined')){
} else {
oc.web.actions.web_app_update.web_app_update_interval = oc.shared.interval.make_interval(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"fn","fn",-1175266204),oc.web.actions.web_app_update.real_web_app_update_check,new cljs.core.Keyword(null,"ms","ms",-1152709733),oc.web.actions.web_app_update.default_update_interval_ms], null));
}
/**
 * Start the app update cycle, make sure it's started only once.
 */
oc.web.actions.web_app_update.start_web_app_update_check_BANG_ = (function oc$web$actions$web_app_update$start_web_app_update_check_BANG_(){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.actions.web-app-update",null,58,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Starting web app update checking cycle"], null);
}),null)),null,-322433087);

return oc.shared.interval.start_interval_BANG_(oc.web.actions.web_app_update.web_app_update_interval);
});

//# sourceMappingURL=oc.web.actions.web_app_update.js.map
