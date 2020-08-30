goog.provide('oc.web.expo');
/**
 * Raises an event on the native side of the bridge with name `op` and accompanying `data`.
 *   Supported ops are implemented in the open-company-mobile repository.
 */
oc.web.expo.bridge_call_BANG_ = (function oc$web$expo$bridge_call_BANG_(op,data){
var event = cljs.core.clj__GT_js(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"op","op",-1882987955),op,new cljs.core.Keyword(null,"data","data",-232669377),data], null));
var json_event = JSON.stringify(event);
return window.ReactNativeWebView.postMessage(json_event);
});
/**
 * Exported web functions called by the native side of the bridge accept only
 *   one arg, and should use this function to deserialize the arg into clj data.
 */
oc.web.expo.parse_bridge_data = (function oc$web$expo$parse_bridge_data(json_str){
return cljs.core.js__GT_clj.cljs$core$IFn$_invoke$arity$variadic(JSON.parse(json_str),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"keywordize-keys","keywordize-keys",1310784252),true], 0));
});
/**
 * Logs the given data to the native console.
 */
oc.web.expo.bridge_log_BANG_ = (function oc$web$expo$bridge_log_BANG_(data){
return oc.web.expo.bridge_call_BANG_("log",data);
});
/**
 * Displays the native push notification permission dialog on iOS. If the user has already
 *   granted access, the response simply contains the push notification token. If the user denies
 *   access, the response will contain nil.
 *   Note: this method only pertains to iOS devices, as Android permissions are granted at the time
 *   of installation. On Android, this method will simply return the push notification token.
 */
oc.web.expo.bridge_request_push_notification_permission_BANG_ = (function oc$web$expo$bridge_request_push_notification_permission_BANG_(){
return oc.web.expo.bridge_call_BANG_("request-push-notification-permission",null);
});
/**
 * Callback for the `bridge-request-push-notification-permission!` bridge method. Response will
 *   contain the push token if the user granted permission (or had already granted permission). Response
 *   is `nil` if the user denied the permission (or previously denied the permission).
 */
oc.web.expo.on_push_notification_permission = (function oc$web$expo$on_push_notification_permission(json_str){
oc.web.expo.bridge_log_BANG_(["on-push-notification-permission: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(json_str)].join(''));

var temp__5733__auto__ = oc.web.expo.parse_bridge_data(json_str);
if(cljs.core.truth_(temp__5733__auto__)){
var token = temp__5733__auto__;
oc.web.expo.bridge_log_BANG_("Adding Expo push token");

return oc.web.actions.user.add_expo_push_token(token);
} else {
oc.web.expo.bridge_log_BANG_("Push notification permission denied by user");

return oc.web.actions.user.deny_push_notification_permission();
}
});
goog.exportSymbol('oc.web.expo.on_push_notification_permission', oc.web.expo.on_push_notification_permission);
oc.web.expo.deep_link_origin = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
oc.web.expo.get_deep_link_origin = (function oc$web$expo$get_deep_link_origin(){
return cljs.core.deref(oc.web.expo.deep_link_origin);
});
/**
 * 
 */
oc.web.expo.bridge_get_deep_link_origin = (function oc$web$expo$bridge_get_deep_link_origin(){
return oc.web.expo.bridge_call_BANG_("get-deep-link-origin",null);
});
/**
 * 
 */
oc.web.expo.on_deep_link_origin = (function oc$web$expo$on_deep_link_origin(json_str){
var temp__5735__auto__ = oc.web.expo.parse_bridge_data(json_str);
if(cljs.core.truth_(temp__5735__auto__)){
var origin = temp__5735__auto__;
oc.web.expo.bridge_log_BANG_(origin);

cljs.core.reset_BANG_(oc.web.expo.deep_link_origin,origin);

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),oc.web.dispatcher.expo_deep_link_origin_key,origin], null));
} else {
return null;
}
});
goog.exportSymbol('oc.web.expo.on_deep_link_origin', oc.web.expo.on_deep_link_origin);
oc.web.expo.app_version = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
oc.web.expo.get_app_version = (function oc$web$expo$get_app_version(){
return cljs.core.deref(oc.web.expo.app_version);
});
/**
 * 
 */
oc.web.expo.bridge_get_app_version = (function oc$web$expo$bridge_get_app_version(){
return oc.web.expo.bridge_call_BANG_("get-app-version",null);
});
/**
 * 
 */
oc.web.expo.on_app_version = (function oc$web$expo$on_app_version(av){
if(cljs.core.truth_(av)){
oc.web.expo.bridge_log_BANG_(["on-app-version ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(av)].join(''));

cljs.core.reset_BANG_(oc.web.expo.app_version,av);

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),oc.web.dispatcher.expo_app_version_key,av], null));
} else {
return null;
}
});
goog.exportSymbol('oc.web.expo.on_app_version', oc.web.expo.on_app_version);

//# sourceMappingURL=oc.web.expo.js.map
