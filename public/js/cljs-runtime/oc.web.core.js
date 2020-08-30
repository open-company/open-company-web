goog.provide('oc.web.core');
cljs.core.enable_console_print_BANG_();
oc.web.core.drv_root = (function oc$web$core$drv_root(component,target){
oc.web.rum_utils.drv_root(new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"state","state",-1988618099),oc.web.dispatcher.app_state,new cljs.core.Keyword(null,"drv-spec","drv-spec",-1268834882),oc.web.dispatcher.drv_spec(oc.web.dispatcher.app_state),new cljs.core.Keyword(null,"component","component",1555936782),component,new cljs.core.Keyword(null,"target","target",253001721),target], null));

var temp__5735__auto__ = document.querySelector("div#oc-notifications-container");
if(cljs.core.truth_(temp__5735__auto__)){
var notifications_mount_point = temp__5735__auto__;
return oc.web.rum_utils.drv_root(new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"state","state",-1988618099),oc.web.dispatcher.app_state,new cljs.core.Keyword(null,"drv-spec","drv-spec",-1268834882),oc.web.dispatcher.drv_spec(oc.web.dispatcher.app_state),new cljs.core.Keyword(null,"component","component",1555936782),oc.web.components.ui.notifications.notifications,new cljs.core.Keyword(null,"target","target",253001721),notifications_mount_point], null));
} else {
return null;
}
});
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.core !== 'undefined') && (typeof oc.web.core.sentry !== 'undefined')){
} else {
oc.web.core.sentry = oc.web.lib.sentry.sentry_setup();
}
oc.web.core.check_get_params = (function oc$web$core$check_get_params(query_params){
if(cljs.core.contains_QMARK_(query_params,new cljs.core.Keyword(null,"browser-type","browser-type",-695048188))){
return oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$3(new cljs.core.Keyword(null,"force-browser-type","force-browser-type",-630659403),new cljs.core.Keyword(null,"browser-type","browser-type",-695048188).cljs$core$IFn$_invoke$arity$1(query_params),((((60) * (60)) * (24)) * (6)));
} else {
return null;
}
});
oc.web.core.inject_loading = (function oc$web$core$inject_loading(){
var target = document.querySelector("div#oc-loading");
return oc.web.core.drv_root(oc.web.components.ui.loading.loading,target);
});
oc.web.core.rewrite_url = (function oc$web$core$rewrite_url(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45397 = arguments.length;
var i__4737__auto___45398 = (0);
while(true){
if((i__4737__auto___45398 < len__4736__auto___45397)){
args__4742__auto__.push((arguments[i__4737__auto___45398]));

var G__45399 = (i__4737__auto___45398 + (1));
i__4737__auto___45398 = G__45399;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.rewrite_url.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.rewrite_url.cljs$core$IFn$_invoke$arity$variadic = (function (p__45035){
var vec__45036 = p__45035;
var map__45039 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__45036,(0),null);
var map__45039__$1 = (((((!((map__45039 == null))))?(((((map__45039.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45039.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45039):map__45039);
var query_params = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45039__$1,new cljs.core.Keyword(null,"query-params","query-params",900640534));
var keep_params = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45039__$1,new cljs.core.Keyword(null,"keep-params","keep-params",-1309113933));
var l = window.location;
var rewrite_to = [cljs.core.str.cljs$core$IFn$_invoke$arity$1(l.pathname),cljs.core.str.cljs$core$IFn$_invoke$arity$1(l.hash)].join('');
var search_values = ((cljs.core.seq(keep_params))?cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__45033_SHARP_){
if(cljs.core.truth_(cljs.core.get.cljs$core$IFn$_invoke$arity$2(query_params,p1__45033_SHARP_))){
return [cljs.core.name(p1__45033_SHARP_),"=",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.get.cljs$core$IFn$_invoke$arity$2(query_params,p1__45033_SHARP_))].join('');
} else {
return null;
}
}),keep_params)):null);
var with_search = (((cljs.core.count(search_values) > (0)))?[rewrite_to,"?",clojure.string.join.cljs$core$IFn$_invoke$arity$2("&",search_values)].join(''):rewrite_to);
if(cljs.core.seq(l.search)){
return window.history.pushState(({}),document.title,with_search);
} else {
return null;
}
}));

(oc.web.core.rewrite_url.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.rewrite_url.cljs$lang$applyTo = (function (seq45034){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45034));
}));

oc.web.core.pre_routing = (function oc$web$core$pre_routing(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45400 = arguments.length;
var i__4737__auto___45401 = (0);
while(true){
if((i__4737__auto___45401 < len__4736__auto___45400)){
args__4742__auto__.push((arguments[i__4737__auto___45401]));

var G__45402 = (i__4737__auto___45401 + (1));
i__4737__auto___45401 = G__45402;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.core.pre_routing.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.core.pre_routing.cljs$core$IFn$_invoke$arity$variadic = (function (params,p__45043){
var vec__45044 = p__45043;
var should_rewrite_url = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__45044,(0),null);
var rewrite_params = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__45044,(1),null);
var body_45403 = document.querySelector("body");
if(oc.shared.useragent.desktop_app_QMARK_){
dommy.core.add_class_BANG_.cljs$core$IFn$_invoke$arity$2(body_45403,new cljs.core.Keyword(null,"electron","electron",1312019442));

if(cljs.core.truth_(oc.shared.useragent.mac_QMARK_)){
dommy.core.add_class_BANG_.cljs$core$IFn$_invoke$arity$2(body_45403,new cljs.core.Keyword(null,"mac-electron","mac-electron",80452601));
} else {
}

if(cljs.core.truth_(oc.shared.useragent.windows_QMARK_)){
dommy.core.add_class_BANG_.cljs$core$IFn$_invoke$arity$2(body_45403,new cljs.core.Keyword(null,"win-electron","win-electron",-1374777994));
} else {
}
} else {
}

if(cljs.core.truth_(new cljs.core.Keyword(null,"log-level","log-level",862121670).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"query-params","query-params",900640534).cljs$core$IFn$_invoke$arity$1(params)))){
oc.web.lib.logging.config_log_level_BANG_(new cljs.core.Keyword(null,"log-level","log-level",862121670).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"query-params","query-params",900640534).cljs$core$IFn$_invoke$arity$1(params)));
} else {
}

var pathname_45404 = window.location.pathname;
if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(pathname_45404,cuerdas.core.lower(pathname_45404))){
var lower_location_45405 = [cljs.core.str.cljs$core$IFn$_invoke$arity$1(cuerdas.core.lower(pathname_45404)),cljs.core.str.cljs$core$IFn$_invoke$arity$1(window.location.search),cljs.core.str.cljs$core$IFn$_invoke$arity$1(window.location.hash)].join('');
(window.location = lower_location_45405);
} else {
}

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"routing","routing",1440253662),cljs.core.PersistentArrayMap.EMPTY], null));

if(((cljs.core.contains_QMARK_(new cljs.core.Keyword(null,"query-params","query-params",900640534).cljs$core$IFn$_invoke$arity$1(params),new cljs.core.Keyword(null,"jwt","jwt",1504015441))) && (cljs.core.map_QMARK_(cljs.core.js__GT_clj.cljs$core$IFn$_invoke$arity$1(oc.web.lib.jwt.decode(new cljs.core.Keyword(null,"jwt","jwt",1504015441).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"query-params","query-params",900640534).cljs$core$IFn$_invoke$arity$1(params)))))))){
oc.web.actions.jwt.update_jwt(new cljs.core.Keyword(null,"jwt","jwt",1504015441).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"query-params","query-params",900640534).cljs$core$IFn$_invoke$arity$1(params)));
} else {
}

if(((cljs.core.not(oc.web.lib.jwt.jwt())) && (cljs.core.contains_QMARK_(new cljs.core.Keyword(null,"query-params","query-params",900640534).cljs$core$IFn$_invoke$arity$1(params),new cljs.core.Keyword(null,"id","id",-1388402092))) && (cljs.core.map_QMARK_(cljs.core.js__GT_clj.cljs$core$IFn$_invoke$arity$1(oc.web.lib.jwt.decode(new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"query-params","query-params",900640534).cljs$core$IFn$_invoke$arity$1(params)))))))){
oc.web.actions.jwt.update_id_token(new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"query-params","query-params",900640534).cljs$core$IFn$_invoke$arity$1(params)));
} else {
}

oc.web.core.check_get_params(new cljs.core.Keyword(null,"query-params","query-params",900640534).cljs$core$IFn$_invoke$arity$1(params));

if(cljs.core.truth_(should_rewrite_url)){
oc.web.core.rewrite_url.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([rewrite_params], 0));
} else {
}

if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"new","new",-2085437848).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"query-params","query-params",900640534).cljs$core$IFn$_invoke$arity$1(params)),"true")){
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(oc.web.dispatcher.app_state,cljs.core.assoc,new cljs.core.Keyword(null,"new-slack-user","new-slack-user",-146614755),true);
} else {
}

if(cljs.core.contains_QMARK_(params,new cljs.core.Keyword(null,"org","org",1495985))){
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(oc.web.dispatcher.app_state,cljs.core.assoc,new cljs.core.Keyword(null,"foc-layout","foc-layout",-1925028965),oc.web.dispatcher.default_foc_layout);
} else {
}

return oc.web.core.inject_loading();
}));

(oc.web.core.pre_routing.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.core.pre_routing.cljs$lang$applyTo = (function (seq45041){
var G__45042 = cljs.core.first(seq45041);
var seq45041__$1 = cljs.core.next(seq45041);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__45042,seq45041__$1);
}));

oc.web.core.post_routing = (function oc$web$core$post_routing(){
oc.web.actions.routing.post_routing();

return oc.web.actions.user.initial_loading.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true], 0));
});
oc.web.core.check_nux = (function oc$web$core$check_nux(query_params){
var has_at_param = cljs.core.contains_QMARK_(query_params,new cljs.core.Keyword(null,"at","at",1476951349));
var user_settings = (cljs.core.truth_(((cljs.core.contains_QMARK_(query_params,new cljs.core.Keyword(null,"user-settings","user-settings",124032100)))?(function (){var G__45052 = cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-settings","user-settings",124032100).cljs$core$IFn$_invoke$arity$1(query_params));
var fexpr__45051 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"notifications","notifications",1685638001),null,new cljs.core.Keyword(null,"profile","profile",-545963874),null], null), null);
return (fexpr__45051.cljs$core$IFn$_invoke$arity$1 ? fexpr__45051.cljs$core$IFn$_invoke$arity$1(G__45052) : fexpr__45051.call(null,G__45052));
})():false))?cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-settings","user-settings",124032100).cljs$core$IFn$_invoke$arity$1(query_params)):null);
var org_settings = (cljs.core.truth_(((cljs.core.not(user_settings))?((cljs.core.contains_QMARK_(query_params,new cljs.core.Keyword(null,"org-settings","org-settings",-785943661)))?(function (){var G__45058 = cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"org-settings","org-settings",-785943661).cljs$core$IFn$_invoke$arity$1(query_params));
var fexpr__45057 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"payments","payments",-1324138047),null,new cljs.core.Keyword(null,"invite-slack","invite-slack",-1290785659),null,new cljs.core.Keyword(null,"invite-email","invite-email",1375794598),null,new cljs.core.Keyword(null,"integrations","integrations",1844532423),null,new cljs.core.Keyword(null,"org","org",1495985),null,new cljs.core.Keyword(null,"team","team",1355747699),null,new cljs.core.Keyword(null,"invite-picker","invite-picker",1426151962),null], null), null);
return (fexpr__45057.cljs$core$IFn$_invoke$arity$1 ? fexpr__45057.cljs$core$IFn$_invoke$arity$1(G__45058) : fexpr__45057.call(null,G__45058));
})():false):false))?cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"org-settings","org-settings",-785943661).cljs$core$IFn$_invoke$arity$1(query_params)):null);
var reminders = ((((oc.web.local_settings.reminders_enabled_QMARK_) && (cljs.core.not(org_settings)) && (cljs.core.contains_QMARK_(query_params,new cljs.core.Keyword(null,"reminders","reminders",-2135532712)))))?new cljs.core.Keyword(null,"reminders","reminders",-2135532712):null);
var panel_stack = (cljs.core.truth_(org_settings)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [org_settings], null):(cljs.core.truth_(user_settings)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [user_settings], null):(cljs.core.truth_(reminders)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [reminders], null):cljs.core.PersistentVector.EMPTY
)));
var bot_access = ((cljs.core.contains_QMARK_(query_params,new cljs.core.Keyword(null,"access","access",2027349272)))?new cljs.core.Keyword(null,"access","access",2027349272).cljs$core$IFn$_invoke$arity$1(query_params):null);
var billing_checkout_map = ((((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(org_settings,new cljs.core.Keyword(null,"payments","payments",-1324138047))) && (cljs.core.contains_QMARK_(query_params,new cljs.core.Keyword(null,"result","result",1415092211)))))?cljs.core.PersistentArrayMap.createAsIfByAssoc([oc.web.dispatcher.checkout_result_key,cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"result","result",1415092211).cljs$core$IFn$_invoke$arity$1(query_params),"true"),oc.web.dispatcher.checkout_update_plan_key,new cljs.core.Keyword(null,"update-plan","update-plan",215364405).cljs$core$IFn$_invoke$arity$1(query_params)]):null);
var next_app_state = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"panel-stack","panel-stack",1084180004),panel_stack,new cljs.core.Keyword(null,"bot-access","bot-access",1631196357),bot_access], null),billing_checkout_map], 0));
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(oc.web.dispatcher.app_state,cljs.core.merge,next_app_state);
});
/**
 * Read the sort order from the cookie, fallback to the default,
 * if it's on drafts board force the recently posted sort since that has only that
 */
oc.web.core.read_sort_type_from_cookie = (function oc$web$core$read_sort_type_from_cookie(params){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"board","board",-1907017633).cljs$core$IFn$_invoke$arity$1(params),"replies")){
return oc.web.dispatcher.recent_activity_sort;
} else {
return oc.web.dispatcher.recently_posted_sort;
}
});
oc.web.core.org_handler = (function oc$web$core$org_handler(route,target,component,params){
var org__$1 = new cljs.core.Keyword(null,"org","org",1495985).cljs$core$IFn$_invoke$arity$1(params);
var board = new cljs.core.Keyword(null,"board","board",-1907017633).cljs$core$IFn$_invoke$arity$1(params);
var sort_type = oc.web.core.read_sort_type_from_cookie(params);
var query_params = new cljs.core.Keyword(null,"query-params","query-params",900640534).cljs$core$IFn$_invoke$arity$1(params);
var first_ever_cookie_name = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(route,oc.web.urls.default_board_slug))?oc.web.router.first_ever_landing_cookie(oc.web.lib.jwt.user_id()):null);
var first_ever_cookie = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(route,oc.web.urls.default_board_slug))?oc.web.lib.cookies.get_cookie(first_ever_cookie_name):null);
if(cljs.core.truth_(first_ever_cookie)){
oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$1(first_ever_cookie_name);

return oc.web.router.redirect_BANG_(oc.web.urls.first_ever_landing.cljs$core$IFn$_invoke$arity$1(org__$1));
} else {
oc.web.core.pre_routing.cljs$core$IFn$_invoke$arity$variadic(params,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"query-params","query-params",900640534),query_params,new cljs.core.Keyword(null,"keep-params","keep-params",-1309113933),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"at","at",1476951349)], null)], null)], 0));

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"routing","routing",1440253662),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"org","org",1495985),org__$1,new cljs.core.Keyword(null,"board","board",-1907017633),board,new cljs.core.Keyword(null,"sort-type","sort-type",-2053499504),sort_type,new cljs.core.Keyword(null,"query-params","query-params",900640534),new cljs.core.Keyword(null,"query-params","query-params",900640534).cljs$core$IFn$_invoke$arity$1(params),new cljs.core.Keyword(null,"route","route",329891309),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [org__$1,route], null)], null)], null));

if(cljs.core.truth_(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0())){
} else {
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(oc.web.dispatcher.app_state,cljs.core.merge,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"loading","loading",-737050189),true], null));
}

oc.web.core.check_nux(query_params);

oc.web.core.post_routing();

return oc.web.core.drv_root(component,target);
}
});
oc.web.core.simple_handler = (function oc$web$core$simple_handler(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45406 = arguments.length;
var i__4737__auto___45407 = (0);
while(true){
if((i__4737__auto___45407 < len__4736__auto___45406)){
args__4742__auto__.push((arguments[i__4737__auto___45407]));

var G__45408 = (i__4737__auto___45407 + (1));
i__4737__auto___45407 = G__45408;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((4) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((4)),(0),null)):null);
return oc.web.core.simple_handler.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),argseq__4743__auto__);
});

(oc.web.core.simple_handler.cljs$core$IFn$_invoke$arity$variadic = (function (component,route_name,target,params,p__45064){
var vec__45065 = p__45064;
var rewrite_url = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__45065,(0),null);
oc.web.core.pre_routing.cljs$core$IFn$_invoke$arity$variadic(params,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([rewrite_url], 0));

var org_45409__$1 = new cljs.core.Keyword(null,"org","org",1495985).cljs$core$IFn$_invoke$arity$1(params);
var route_45410 = cljs.core.vec(cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [route_name,org_45409__$1], null)));
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"routing","routing",1440253662),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"org","org",1495985),org_45409__$1,new cljs.core.Keyword(null,"query-params","query-params",900640534),new cljs.core.Keyword(null,"query-params","query-params",900640534).cljs$core$IFn$_invoke$arity$1(params),new cljs.core.Keyword(null,"route","route",329891309),route_45410], null)], null));

oc.web.core.post_routing();

if(cljs.core.contains_QMARK_(new cljs.core.Keyword(null,"query-params","query-params",900640534).cljs$core$IFn$_invoke$arity$1(params),new cljs.core.Keyword(null,"jwt","jwt",1504015441))){
return null;
} else {
rum.core.unmount(target);

return oc.web.core.drv_root(component,target);
}
}));

(oc.web.core.simple_handler.cljs$lang$maxFixedArity = (4));

/** @this {Function} */
(oc.web.core.simple_handler.cljs$lang$applyTo = (function (seq45059){
var G__45060 = cljs.core.first(seq45059);
var seq45059__$1 = cljs.core.next(seq45059);
var G__45061 = cljs.core.first(seq45059__$1);
var seq45059__$2 = cljs.core.next(seq45059__$1);
var G__45062 = cljs.core.first(seq45059__$2);
var seq45059__$3 = cljs.core.next(seq45059__$2);
var G__45063 = cljs.core.first(seq45059__$3);
var seq45059__$4 = cljs.core.next(seq45059__$3);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__45060,G__45061,G__45062,G__45063,seq45059__$4);
}));

oc.web.core.board_handler = (function oc$web$core$board_handler(route,target,component,params){
var org__$1 = new cljs.core.Keyword(null,"org","org",1495985).cljs$core$IFn$_invoke$arity$1(params);
var board = new cljs.core.Keyword(null,"board","board",-1907017633).cljs$core$IFn$_invoke$arity$1(params);
var entry_board = new cljs.core.Keyword(null,"entry-board","entry-board",1825593318).cljs$core$IFn$_invoke$arity$1(params);
var sort_type = oc.web.core.read_sort_type_from_cookie(params);
var entry = new cljs.core.Keyword(null,"entry","entry",505168823).cljs$core$IFn$_invoke$arity$1(params);
var comment = new cljs.core.Keyword(null,"comment","comment",532206069).cljs$core$IFn$_invoke$arity$1(params);
var query_params = new cljs.core.Keyword(null,"query-params","query-params",900640534).cljs$core$IFn$_invoke$arity$1(params);
var has_at_param = cljs.core.contains_QMARK_(query_params,new cljs.core.Keyword(null,"at","at",1476951349));
oc.web.core.pre_routing.cljs$core$IFn$_invoke$arity$variadic(params,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"query-params","query-params",900640534),query_params,new cljs.core.Keyword(null,"keep-params","keep-params",-1309113933),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"at","at",1476951349)], null)], null)], 0));

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"routing","routing",1440253662),new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"org","org",1495985),org__$1,new cljs.core.Keyword(null,"board","board",-1907017633),board,new cljs.core.Keyword(null,"sort-type","sort-type",-2053499504),sort_type,new cljs.core.Keyword(null,"entry-board","entry-board",1825593318),entry_board,new cljs.core.Keyword(null,"activity","activity",-1179221455),entry,new cljs.core.Keyword(null,"comment","comment",532206069),comment,new cljs.core.Keyword(null,"query-params","query-params",900640534),query_params,new cljs.core.Keyword(null,"route","route",329891309),cljs.core.vec(cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [org__$1,board,(cljs.core.truth_(entry)?entry:null),(cljs.core.truth_(comment)?comment:null),route], null)))], null)], null));

oc.web.core.check_nux(query_params);

oc.web.core.post_routing();

return oc.web.core.drv_root(component,target);
});
oc.web.core.contributions_handler = (function oc$web$core$contributions_handler(route,target,component,params){
var org__$1 = new cljs.core.Keyword(null,"org","org",1495985).cljs$core$IFn$_invoke$arity$1(params);
var sort_type = oc.web.core.read_sort_type_from_cookie(params);
var contributions = new cljs.core.Keyword(null,"contributions","contributions",-1280485964).cljs$core$IFn$_invoke$arity$1(params);
var query_params = new cljs.core.Keyword(null,"query-params","query-params",900640534).cljs$core$IFn$_invoke$arity$1(params);
oc.web.core.pre_routing.cljs$core$IFn$_invoke$arity$variadic(params,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"query-params","query-params",900640534),query_params], null)], 0));

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"routing","routing",1440253662),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"org","org",1495985),org__$1,new cljs.core.Keyword(null,"contributions","contributions",-1280485964),contributions,new cljs.core.Keyword(null,"sort-type","sort-type",-2053499504),sort_type,new cljs.core.Keyword(null,"query-params","query-params",900640534),query_params,new cljs.core.Keyword(null,"route","route",329891309),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [org__$1,contributions,route], null)], null)], null));

oc.web.core.check_nux(query_params);

oc.web.core.post_routing();

return oc.web.core.drv_root(component,target);
});
oc.web.core.secure_activity_handler = (function oc$web$core$secure_activity_handler(component,route,target,params,pre_routing_QMARK_){
var org__$1 = new cljs.core.Keyword(null,"org","org",1495985).cljs$core$IFn$_invoke$arity$1(params);
var secure_id = new cljs.core.Keyword(null,"secure-id","secure-id",-626735882).cljs$core$IFn$_invoke$arity$1(params);
var query_params = new cljs.core.Keyword(null,"query-params","query-params",900640534).cljs$core$IFn$_invoke$arity$1(params);
if(cljs.core.truth_(pre_routing_QMARK_)){
oc.web.core.pre_routing.cljs$core$IFn$_invoke$arity$variadic(params,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true], 0));
} else {
}

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"routing","routing",1440253662),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"org","org",1495985),org__$1,new cljs.core.Keyword(null,"activity","activity",-1179221455),new cljs.core.Keyword(null,"entry","entry",505168823).cljs$core$IFn$_invoke$arity$1(params),new cljs.core.Keyword(null,"secure-id","secure-id",-626735882),(function (){var or__4126__auto__ = secure_id;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"secure-uuid","secure-uuid",-1972075067).cljs$core$IFn$_invoke$arity$1(oc.web.lib.jwt.get_id_token_contents.cljs$core$IFn$_invoke$arity$0());
}
})(),new cljs.core.Keyword(null,"comment","comment",532206069),new cljs.core.Keyword(null,"comment","comment",532206069).cljs$core$IFn$_invoke$arity$1(params),new cljs.core.Keyword(null,"query-params","query-params",900640534),query_params,new cljs.core.Keyword(null,"route","route",329891309),cljs.core.vec(cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [org__$1,route,secure_id], null)))], null)], null));

if(((cljs.core.not(oc.web.dispatcher.board_data())) || (cljs.core.not(new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.board_data()))) || (cljs.core.not(oc.web.dispatcher.secure_activity_data.cljs$core$IFn$_invoke$arity$0())))){
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(oc.web.dispatcher.app_state,cljs.core.merge,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"loading","loading",-737050189),true], null));
} else {
}

oc.web.core.post_routing();

return oc.web.core.drv_root(component,target);
});
oc.web.core.entry_handler = (function oc$web$core$entry_handler(target,params){
var with_default_board = cljs.core.update.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),(function (p1__45068_SHARP_){
var or__4126__auto__ = p1__45068_SHARP_;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "following";
}
}));
oc.web.core.pre_routing.cljs$core$IFn$_invoke$arity$variadic(with_default_board,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true], 0));

if(cljs.core.truth_(((cljs.core.not(oc.web.lib.jwt.jwt()))?new cljs.core.Keyword(null,"secure-uuid","secure-uuid",-1972075067).cljs$core$IFn$_invoke$arity$1(oc.web.lib.jwt.get_id_token_contents.cljs$core$IFn$_invoke$arity$0()):false))){
return oc.web.core.secure_activity_handler(oc.web.components.secure_activity.secure_activity,"secure-activity",target,with_default_board,false);
} else {
return oc.web.core.board_handler("activity",target,oc.web.components.org_dashboard.org_dashboard,with_default_board);
}
});
oc.web.core.slack_lander_check = (function oc$web$core$slack_lander_check(params){
oc.web.core.pre_routing.cljs$core$IFn$_invoke$arity$variadic(params,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true], 0));

var new_user = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"new","new",-2085437848).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"query-params","query-params",900640534).cljs$core$IFn$_invoke$arity$1(params)),"true");
if(new_user){
oc.web.actions.nux.new_user_registered("slack");
} else {
}

return oc.web.actions.user.lander_check_team_redirect();
});
oc.web.core.google_lander_check = (function oc$web$core$google_lander_check(params){
oc.web.core.pre_routing.cljs$core$IFn$_invoke$arity$variadic(params,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true], 0));

var new_user = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"new","new",-2085437848).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"query-params","query-params",900640534).cljs$core$IFn$_invoke$arity$1(params)),"true");
if(new_user){
oc.web.actions.nux.new_user_registered("google");
} else {
}

return oc.web.actions.user.lander_check_team_redirect();
});
var temp__5733__auto___45411 = document.querySelector("div#app");
if(cljs.core.truth_(temp__5733__auto___45411)){
var target_45412 = temp__5733__auto___45411;
var action__27254__auto___45413 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45074 = params__27255__auto__;
var map__45074__$1 = (((((!((map__45074 == null))))?(((((map__45074.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45074.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45074):map__45074);
var params = map__45074__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,337,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing _loading_route __loading"], null);
}),null)),null,1735509249);

return oc.web.core.pre_routing(params);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45076 = params__27255__auto__;
var map__45076__$1 = (((((!((map__45076 == null))))?(((((map__45076.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45076.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45076):map__45076);
var params = map__45076__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,337,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing _loading_route __loading"], null);
}),null)),null,1770171233);

return oc.web.core.pre_routing(params);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_("/__loading",action__27254__auto___45413);

oc.web.core._loading_route = (function oc$web$core$_loading_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45414 = arguments.length;
var i__4737__auto___45415 = (0);
while(true){
if((i__4737__auto___45415 < len__4736__auto___45414)){
args__4742__auto__.push((arguments[i__4737__auto___45415]));

var G__45416 = (i__4737__auto___45415 + (1));
i__4737__auto___45415 = G__45416;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core._loading_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core._loading_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,"/__loading",args__27253__auto__);
}));

(oc.web.core._loading_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core._loading_route.cljs$lang$applyTo = (function (seq45079){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45079));
}));


var action__27254__auto___45417 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45080 = params__27255__auto__;
var map__45080__$1 = (((((!((map__45080 == null))))?(((((map__45080.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45080.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45080):map__45080);
var params = map__45080__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,341,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing login-route",oc.web.urls.login], null);
}),null)),null,-975079295);

var last_org_cookie = oc.web.lib.cookies.get_cookie(oc.web.router.last_org_cookie());
if(cljs.core.truth_((function (){var and__4115__auto__ = oc.web.lib.jwt.jwt();
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.seq(last_org_cookie);
} else {
return and__4115__auto__;
}
})())){
oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$1(oc.web.router.last_org_cookie());

return oc.web.router.redirect_BANG_(oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1(last_org_cookie));
} else {
return oc.web.core.simple_handler.cljs$core$IFn$_invoke$arity$variadic((function (){
var G__45082 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"title","title",636505583),"Welcome back!",new cljs.core.Keyword(null,"desc","desc",2093485764),""], null);
return (oc.web.components.ui.login_wall.login_wall.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.login_wall.login_wall.cljs$core$IFn$_invoke$arity$1(G__45082) : oc.web.components.ui.login_wall.login_wall.call(null,G__45082));
}),"login",target_45412,params,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true], 0));
}
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45083 = params__27255__auto__;
var map__45083__$1 = (((((!((map__45083 == null))))?(((((map__45083.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45083.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45083):map__45083);
var params = map__45083__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,341,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing login-route",oc.web.urls.login], null);
}),null)),null,-1199315427);

var last_org_cookie = oc.web.lib.cookies.get_cookie(oc.web.router.last_org_cookie());
if(cljs.core.truth_((function (){var and__4115__auto__ = oc.web.lib.jwt.jwt();
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.seq(last_org_cookie);
} else {
return and__4115__auto__;
}
})())){
oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$1(oc.web.router.last_org_cookie());

return oc.web.router.redirect_BANG_(oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1(last_org_cookie));
} else {
return oc.web.core.simple_handler.cljs$core$IFn$_invoke$arity$variadic((function (){
var G__45085 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"title","title",636505583),"Welcome back!",new cljs.core.Keyword(null,"desc","desc",2093485764),""], null);
return (oc.web.components.ui.login_wall.login_wall.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.login_wall.login_wall.cljs$core$IFn$_invoke$arity$1(G__45085) : oc.web.components.ui.login_wall.login_wall.call(null,G__45085));
}),"login",target_45412,params,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true], 0));
}
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.login,action__27254__auto___45417);

oc.web.core.login_route = (function oc$web$core$login_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45418 = arguments.length;
var i__4737__auto___45419 = (0);
while(true){
if((i__4737__auto___45419 < len__4736__auto___45418)){
args__4742__auto__.push((arguments[i__4737__auto___45419]));

var G__45420 = (i__4737__auto___45419 + (1));
i__4737__auto___45419 = G__45420;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.login_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.login_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.login,args__27253__auto__);
}));

(oc.web.core.login_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.login_route.cljs$lang$applyTo = (function (seq45086){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45086));
}));


var action__27254__auto___45421 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45087 = params__27255__auto__;
var map__45087__$1 = (((((!((map__45087 == null))))?(((((map__45087.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45087.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45087):map__45087);
var params = map__45087__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,357,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing signup-route",oc.web.urls.sign_up], null);
}),null)),null,766771583);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
if(cljs.core.seq(oc.web.lib.cookies.get_cookie(oc.web.router.last_org_cookie()))){
oc.web.router.redirect_BANG_(oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1(oc.web.lib.cookies.get_cookie(oc.web.router.last_org_cookie())));
} else {
oc.web.router.redirect_BANG_(oc.web.urls.sign_up_profile);
}
} else {
}

return oc.web.core.simple_handler((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"lander","lander",439860228)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"lander","lander",439860228)));
}),"sign-up",target_45412,params);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45089 = params__27255__auto__;
var map__45089__$1 = (((((!((map__45089 == null))))?(((((map__45089.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45089.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45089):map__45089);
var params = map__45089__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,357,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing signup-route",oc.web.urls.sign_up], null);
}),null)),null,1659804030);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
if(cljs.core.seq(oc.web.lib.cookies.get_cookie(oc.web.router.last_org_cookie()))){
oc.web.router.redirect_BANG_(oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1(oc.web.lib.cookies.get_cookie(oc.web.router.last_org_cookie())));
} else {
oc.web.router.redirect_BANG_(oc.web.urls.sign_up_profile);
}
} else {
}

return oc.web.core.simple_handler((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"lander","lander",439860228)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"lander","lander",439860228)));
}),"sign-up",target_45412,params);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.sign_up,action__27254__auto___45421);

oc.web.core.signup_route = (function oc$web$core$signup_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45422 = arguments.length;
var i__4737__auto___45423 = (0);
while(true){
if((i__4737__auto___45423 < len__4736__auto___45422)){
args__4742__auto__.push((arguments[i__4737__auto___45423]));

var G__45424 = (i__4737__auto___45423 + (1));
i__4737__auto___45423 = G__45424;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.signup_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.signup_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.sign_up,args__27253__auto__);
}));

(oc.web.core.signup_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.signup_route.cljs$lang$applyTo = (function (seq45091){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45091));
}));


var action__27254__auto___45425 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45092 = params__27255__auto__;
var map__45092__$1 = (((((!((map__45092 == null))))?(((((map__45092.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45092.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45092):map__45092);
var params = map__45092__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,365,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing signup-slash-route",[oc.web.urls.sign_up,"/"].join('')], null);
}),null)),null,-402899993);

if(cljs.core.truth_((function (){var and__4115__auto__ = oc.web.lib.jwt.jwt();
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.seq(oc.web.lib.cookies.get_cookie(oc.web.router.last_org_cookie()));
} else {
return and__4115__auto__;
}
})())){
oc.web.router.redirect_BANG_(oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1(oc.web.lib.cookies.get_cookie(oc.web.router.last_org_cookie())));
} else {
}

return oc.web.core.simple_handler((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"lander","lander",439860228)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"lander","lander",439860228)));
}),"sign-up",target_45412,params);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45094 = params__27255__auto__;
var map__45094__$1 = (((((!((map__45094 == null))))?(((((map__45094.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45094.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45094):map__45094);
var params = map__45094__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,365,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing signup-slash-route",[oc.web.urls.sign_up,"/"].join('')], null);
}),null)),null,-879314187);

if(cljs.core.truth_((function (){var and__4115__auto__ = oc.web.lib.jwt.jwt();
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.seq(oc.web.lib.cookies.get_cookie(oc.web.router.last_org_cookie()));
} else {
return and__4115__auto__;
}
})())){
oc.web.router.redirect_BANG_(oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1(oc.web.lib.cookies.get_cookie(oc.web.router.last_org_cookie())));
} else {
}

return oc.web.core.simple_handler((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"lander","lander",439860228)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"lander","lander",439860228)));
}),"sign-up",target_45412,params);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_([oc.web.urls.sign_up,"/"].join(''),action__27254__auto___45425);

oc.web.core.signup_slash_route = (function oc$web$core$signup_slash_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45426 = arguments.length;
var i__4737__auto___45427 = (0);
while(true){
if((i__4737__auto___45427 < len__4736__auto___45426)){
args__4742__auto__.push((arguments[i__4737__auto___45427]));

var G__45428 = (i__4737__auto___45427 + (1));
i__4737__auto___45427 = G__45428;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.signup_slash_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.signup_slash_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,[oc.web.urls.sign_up,"/"].join(''),args__27253__auto__);
}));

(oc.web.core.signup_slash_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.signup_slash_route.cljs$lang$applyTo = (function (seq45096){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45096));
}));


var action__27254__auto___45429 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45097 = params__27255__auto__;
var map__45097__$1 = (((((!((map__45097 == null))))?(((((map__45097.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45097.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45097):map__45097);
var params = map__45097__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,372,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing signup-profile-route",oc.web.urls.sign_up_profile], null);
}),null)),null,-83776572);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
} else {
oc.web.router.redirect_BANG_(oc.web.urls.sign_up);
}

return oc.web.core.simple_handler((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"lander-profile","lander-profile",-846994671)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"lander-profile","lander-profile",-846994671)));
}),"sign-up",target_45412,params);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45099 = params__27255__auto__;
var map__45099__$1 = (((((!((map__45099 == null))))?(((((map__45099.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45099.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45099):map__45099);
var params = map__45099__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,372,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing signup-profile-route",oc.web.urls.sign_up_profile], null);
}),null)),null,-771900771);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
} else {
oc.web.router.redirect_BANG_(oc.web.urls.sign_up);
}

return oc.web.core.simple_handler((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"lander-profile","lander-profile",-846994671)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"lander-profile","lander-profile",-846994671)));
}),"sign-up",target_45412,params);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.sign_up_profile,action__27254__auto___45429);

oc.web.core.signup_profile_route = (function oc$web$core$signup_profile_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45430 = arguments.length;
var i__4737__auto___45431 = (0);
while(true){
if((i__4737__auto___45431 < len__4736__auto___45430)){
args__4742__auto__.push((arguments[i__4737__auto___45431]));

var G__45432 = (i__4737__auto___45431 + (1));
i__4737__auto___45431 = G__45432;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.signup_profile_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.signup_profile_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.sign_up_profile,args__27253__auto__);
}));

(oc.web.core.signup_profile_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.signup_profile_route.cljs$lang$applyTo = (function (seq45101){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45101));
}));


var action__27254__auto___45433 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45103 = params__27255__auto__;
var map__45103__$1 = (((((!((map__45103 == null))))?(((((map__45103.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45103.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45103):map__45103);
var params = map__45103__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,378,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing signup-profile-slash-route",[oc.web.urls.sign_up_profile,"/"].join('')], null);
}),null)),null,-766620967);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
} else {
oc.web.router.redirect_BANG_(oc.web.urls.sign_up);
}

return oc.web.core.simple_handler((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"lander-profile","lander-profile",-846994671)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"lander-profile","lander-profile",-846994671)));
}),"sign-up",target_45412,params);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45105 = params__27255__auto__;
var map__45105__$1 = (((((!((map__45105 == null))))?(((((map__45105.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45105.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45105):map__45105);
var params = map__45105__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,378,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing signup-profile-slash-route",[oc.web.urls.sign_up_profile,"/"].join('')], null);
}),null)),null,1166662041);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
} else {
oc.web.router.redirect_BANG_(oc.web.urls.sign_up);
}

return oc.web.core.simple_handler((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"lander-profile","lander-profile",-846994671)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"lander-profile","lander-profile",-846994671)));
}),"sign-up",target_45412,params);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_([oc.web.urls.sign_up_profile,"/"].join(''),action__27254__auto___45433);

oc.web.core.signup_profile_slash_route = (function oc$web$core$signup_profile_slash_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45434 = arguments.length;
var i__4737__auto___45435 = (0);
while(true){
if((i__4737__auto___45435 < len__4736__auto___45434)){
args__4742__auto__.push((arguments[i__4737__auto___45435]));

var G__45436 = (i__4737__auto___45435 + (1));
i__4737__auto___45435 = G__45436;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.signup_profile_slash_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.signup_profile_slash_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,[oc.web.urls.sign_up_profile,"/"].join(''),args__27253__auto__);
}));

(oc.web.core.signup_profile_slash_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.signup_profile_slash_route.cljs$lang$applyTo = (function (seq45107){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45107));
}));


var action__27254__auto___45437 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45110 = params__27255__auto__;
var map__45110__$1 = (((((!((map__45110 == null))))?(((((map__45110.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45110.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45110):map__45110);
var params = map__45110__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,384,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing signup-team-route",oc.web.urls.sign_up_team], null);
}),null)),null,2045343953);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
if(cljs.core.seq(oc.web.lib.cookies.get_cookie(oc.web.router.last_org_cookie()))){
oc.web.router.redirect_BANG_(oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1(oc.web.lib.cookies.get_cookie(oc.web.router.last_org_cookie())));
} else {
}
} else {
oc.web.router.redirect_BANG_(oc.web.urls.sign_up);
}

return oc.web.core.simple_handler((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"lander-team","lander-team",1452579437)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"lander-team","lander-team",1452579437)));
}),"sign-up",target_45412,params);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45112 = params__27255__auto__;
var map__45112__$1 = (((((!((map__45112 == null))))?(((((map__45112.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45112.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45112):map__45112);
var params = map__45112__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,384,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing signup-team-route",oc.web.urls.sign_up_team], null);
}),null)),null,777293452);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
if(cljs.core.seq(oc.web.lib.cookies.get_cookie(oc.web.router.last_org_cookie()))){
oc.web.router.redirect_BANG_(oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1(oc.web.lib.cookies.get_cookie(oc.web.router.last_org_cookie())));
} else {
}
} else {
oc.web.router.redirect_BANG_(oc.web.urls.sign_up);
}

return oc.web.core.simple_handler((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"lander-team","lander-team",1452579437)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"lander-team","lander-team",1452579437)));
}),"sign-up",target_45412,params);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.sign_up_team,action__27254__auto___45437);

oc.web.core.signup_team_route = (function oc$web$core$signup_team_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45438 = arguments.length;
var i__4737__auto___45439 = (0);
while(true){
if((i__4737__auto___45439 < len__4736__auto___45438)){
args__4742__auto__.push((arguments[i__4737__auto___45439]));

var G__45440 = (i__4737__auto___45439 + (1));
i__4737__auto___45439 = G__45440;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.signup_team_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.signup_team_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.sign_up_team,args__27253__auto__);
}));

(oc.web.core.signup_team_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.signup_team_route.cljs$lang$applyTo = (function (seq45114){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45114));
}));


var action__27254__auto___45441 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45115 = params__27255__auto__;
var map__45115__$1 = (((((!((map__45115 == null))))?(((((map__45115.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45115.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45115):map__45115);
var params = map__45115__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,392,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing signup-team-slash-route",[oc.web.urls.sign_up_team,"/"].join('')], null);
}),null)),null,1378011469);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
if(cljs.core.seq(oc.web.lib.cookies.get_cookie(oc.web.router.last_org_cookie()))){
oc.web.router.redirect_BANG_(oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1(oc.web.lib.cookies.get_cookie(oc.web.router.last_org_cookie())));
} else {
}
} else {
oc.web.router.redirect_BANG_(oc.web.urls.sign_up);
}

return oc.web.core.simple_handler((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"lander-team","lander-team",1452579437)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"lander-team","lander-team",1452579437)));
}),"sign-up",target_45412,params);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45118 = params__27255__auto__;
var map__45118__$1 = (((((!((map__45118 == null))))?(((((map__45118.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45118.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45118):map__45118);
var params = map__45118__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,392,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing signup-team-slash-route",[oc.web.urls.sign_up_team,"/"].join('')], null);
}),null)),null,-126457886);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
if(cljs.core.seq(oc.web.lib.cookies.get_cookie(oc.web.router.last_org_cookie()))){
oc.web.router.redirect_BANG_(oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1(oc.web.lib.cookies.get_cookie(oc.web.router.last_org_cookie())));
} else {
}
} else {
oc.web.router.redirect_BANG_(oc.web.urls.sign_up);
}

return oc.web.core.simple_handler((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"lander-team","lander-team",1452579437)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"lander-team","lander-team",1452579437)));
}),"sign-up",target_45412,params);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_([oc.web.urls.sign_up_team,"/"].join(''),action__27254__auto___45441);

oc.web.core.signup_team_slash_route = (function oc$web$core$signup_team_slash_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45442 = arguments.length;
var i__4737__auto___45443 = (0);
while(true){
if((i__4737__auto___45443 < len__4736__auto___45442)){
args__4742__auto__.push((arguments[i__4737__auto___45443]));

var G__45444 = (i__4737__auto___45443 + (1));
i__4737__auto___45443 = G__45444;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.signup_team_slash_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.signup_team_slash_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,[oc.web.urls.sign_up_team,"/"].join(''),args__27253__auto__);
}));

(oc.web.core.signup_team_slash_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.signup_team_slash_route.cljs$lang$applyTo = (function (seq45120){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45120));
}));


var action__27254__auto___45445 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45121 = params__27255__auto__;
var map__45121__$1 = (((((!((map__45121 == null))))?(((((map__45121.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45121.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45121):map__45121);
var params = map__45121__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,400,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing signup-update-team-route",oc.web.urls.sign_up_update_team.cljs$core$IFn$_invoke$arity$1(":org")], null);
}),null)),null,-621355907);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
} else {
oc.web.router.redirect_BANG_(oc.web.urls.sign_up);
}

return oc.web.core.simple_handler((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"lander-profile","lander-profile",-846994671)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"lander-profile","lander-profile",-846994671)));
}),"sign-up",target_45412,params);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45123 = params__27255__auto__;
var map__45123__$1 = (((((!((map__45123 == null))))?(((((map__45123.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45123.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45123):map__45123);
var params = map__45123__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,400,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing signup-update-team-route",oc.web.urls.sign_up_update_team.cljs$core$IFn$_invoke$arity$1(":org")], null);
}),null)),null,-20068471);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
} else {
oc.web.router.redirect_BANG_(oc.web.urls.sign_up);
}

return oc.web.core.simple_handler((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"lander-profile","lander-profile",-846994671)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"lander-profile","lander-profile",-846994671)));
}),"sign-up",target_45412,params);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.sign_up_update_team.cljs$core$IFn$_invoke$arity$1(":org"),action__27254__auto___45445);

oc.web.core.signup_update_team_route = (function oc$web$core$signup_update_team_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45446 = arguments.length;
var i__4737__auto___45447 = (0);
while(true){
if((i__4737__auto___45447 < len__4736__auto___45446)){
args__4742__auto__.push((arguments[i__4737__auto___45447]));

var G__45448 = (i__4737__auto___45447 + (1));
i__4737__auto___45447 = G__45448;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.signup_update_team_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.signup_update_team_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.sign_up_update_team.cljs$core$IFn$_invoke$arity$1(":org"),args__27253__auto__);
}));

(oc.web.core.signup_update_team_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.signup_update_team_route.cljs$lang$applyTo = (function (seq45125){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45125));
}));


var action__27254__auto___45449 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45126 = params__27255__auto__;
var map__45126__$1 = (((((!((map__45126 == null))))?(((((map__45126.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45126.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45126):map__45126);
var params = map__45126__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,406,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing signup-update-team-slash-route",[oc.web.urls.sign_up_update_team.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join('')], null);
}),null)),null,525497676);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
} else {
oc.web.router.redirect_BANG_(oc.web.urls.sign_up);
}

return oc.web.core.simple_handler((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"lander-profile","lander-profile",-846994671)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"lander-profile","lander-profile",-846994671)));
}),"sign-up",target_45412,params);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45128 = params__27255__auto__;
var map__45128__$1 = (((((!((map__45128 == null))))?(((((map__45128.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45128.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45128):map__45128);
var params = map__45128__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,406,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing signup-update-team-slash-route",[oc.web.urls.sign_up_update_team.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join('')], null);
}),null)),null,863803274);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
} else {
oc.web.router.redirect_BANG_(oc.web.urls.sign_up);
}

return oc.web.core.simple_handler((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"lander-profile","lander-profile",-846994671)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"lander-profile","lander-profile",-846994671)));
}),"sign-up",target_45412,params);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_([oc.web.urls.sign_up_update_team.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join(''),action__27254__auto___45449);

oc.web.core.signup_update_team_slash_route = (function oc$web$core$signup_update_team_slash_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45450 = arguments.length;
var i__4737__auto___45451 = (0);
while(true){
if((i__4737__auto___45451 < len__4736__auto___45450)){
args__4742__auto__.push((arguments[i__4737__auto___45451]));

var G__45452 = (i__4737__auto___45451 + (1));
i__4737__auto___45451 = G__45452;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.signup_update_team_slash_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.signup_update_team_slash_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,[oc.web.urls.sign_up_update_team.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join(''),args__27253__auto__);
}));

(oc.web.core.signup_update_team_slash_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.signup_update_team_slash_route.cljs$lang$applyTo = (function (seq45130){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45130));
}));


var action__27254__auto___45453 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45131 = params__27255__auto__;
var map__45131__$1 = (((((!((map__45131 == null))))?(((((map__45131.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45131.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45131):map__45131);
var params = map__45131__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,412,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing signup-invite-route",oc.web.urls.sign_up_invite.cljs$core$IFn$_invoke$arity$1(":org")], null);
}),null)),null,1657890270);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
} else {
oc.web.router.redirect_BANG_(oc.web.urls.sign_up);
}

return oc.web.core.simple_handler((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"lander-invite","lander-invite",-1360189189)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"lander-invite","lander-invite",-1360189189)));
}),"sign-up",target_45412,params);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45133 = params__27255__auto__;
var map__45133__$1 = (((((!((map__45133 == null))))?(((((map__45133.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45133.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45133):map__45133);
var params = map__45133__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,412,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing signup-invite-route",oc.web.urls.sign_up_invite.cljs$core$IFn$_invoke$arity$1(":org")], null);
}),null)),null,-426344949);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
} else {
oc.web.router.redirect_BANG_(oc.web.urls.sign_up);
}

return oc.web.core.simple_handler((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"lander-invite","lander-invite",-1360189189)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"lander-invite","lander-invite",-1360189189)));
}),"sign-up",target_45412,params);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.sign_up_invite.cljs$core$IFn$_invoke$arity$1(":org"),action__27254__auto___45453);

oc.web.core.signup_invite_route = (function oc$web$core$signup_invite_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45454 = arguments.length;
var i__4737__auto___45455 = (0);
while(true){
if((i__4737__auto___45455 < len__4736__auto___45454)){
args__4742__auto__.push((arguments[i__4737__auto___45455]));

var G__45456 = (i__4737__auto___45455 + (1));
i__4737__auto___45455 = G__45456;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.signup_invite_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.signup_invite_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.sign_up_invite.cljs$core$IFn$_invoke$arity$1(":org"),args__27253__auto__);
}));

(oc.web.core.signup_invite_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.signup_invite_route.cljs$lang$applyTo = (function (seq45135){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45135));
}));


var action__27254__auto___45457 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45136 = params__27255__auto__;
var map__45136__$1 = (((((!((map__45136 == null))))?(((((map__45136.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45136.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45136):map__45136);
var params = map__45136__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,418,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing signup-invite-slash-route",[oc.web.urls.sign_up_invite.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join('')], null);
}),null)),null,-1850586561);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
} else {
oc.web.router.redirect_BANG_(oc.web.urls.sign_up);
}

return oc.web.core.simple_handler((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"lander-invite","lander-invite",-1360189189)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"lander-invite","lander-invite",-1360189189)));
}),"sign-up",target_45412,params);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45138 = params__27255__auto__;
var map__45138__$1 = (((((!((map__45138 == null))))?(((((map__45138.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45138.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45138):map__45138);
var params = map__45138__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,418,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing signup-invite-slash-route",[oc.web.urls.sign_up_invite.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join('')], null);
}),null)),null,-2019069235);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
} else {
oc.web.router.redirect_BANG_(oc.web.urls.sign_up);
}

return oc.web.core.simple_handler((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"lander-invite","lander-invite",-1360189189)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"lander-invite","lander-invite",-1360189189)));
}),"sign-up",target_45412,params);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_([oc.web.urls.sign_up_invite.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join(''),action__27254__auto___45457);

oc.web.core.signup_invite_slash_route = (function oc$web$core$signup_invite_slash_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45458 = arguments.length;
var i__4737__auto___45459 = (0);
while(true){
if((i__4737__auto___45459 < len__4736__auto___45458)){
args__4742__auto__.push((arguments[i__4737__auto___45459]));

var G__45460 = (i__4737__auto___45459 + (1));
i__4737__auto___45459 = G__45460;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.signup_invite_slash_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.signup_invite_slash_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,[oc.web.urls.sign_up_invite.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join(''),args__27253__auto__);
}));

(oc.web.core.signup_invite_slash_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.signup_invite_slash_route.cljs$lang$applyTo = (function (seq45140){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45140));
}));


var action__27254__auto___45461 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45141 = params__27255__auto__;
var map__45141__$1 = (((((!((map__45141 == null))))?(((((map__45141.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45141.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45141):map__45141);
var params = map__45141__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,424,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing slack-lander-check-route",oc.web.urls.slack_lander_check], null);
}),null)),null,-1238494637);

return oc.web.core.slack_lander_check(params);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45143 = params__27255__auto__;
var map__45143__$1 = (((((!((map__45143 == null))))?(((((map__45143.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45143.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45143):map__45143);
var params = map__45143__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,424,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing slack-lander-check-route",oc.web.urls.slack_lander_check], null);
}),null)),null,613916303);

return oc.web.core.slack_lander_check(params);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.slack_lander_check,action__27254__auto___45461);

oc.web.core.slack_lander_check_route = (function oc$web$core$slack_lander_check_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45462 = arguments.length;
var i__4737__auto___45463 = (0);
while(true){
if((i__4737__auto___45463 < len__4736__auto___45462)){
args__4742__auto__.push((arguments[i__4737__auto___45463]));

var G__45464 = (i__4737__auto___45463 + (1));
i__4737__auto___45463 = G__45464;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.slack_lander_check_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.slack_lander_check_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.slack_lander_check,args__27253__auto__);
}));

(oc.web.core.slack_lander_check_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.slack_lander_check_route.cljs$lang$applyTo = (function (seq45145){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45145));
}));


var action__27254__auto___45465 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45146 = params__27255__auto__;
var map__45146__$1 = (((((!((map__45146 == null))))?(((((map__45146.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45146.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45146):map__45146);
var params = map__45146__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,429,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing slack-lander-check-slash-route",[oc.web.urls.slack_lander_check,"/"].join('')], null);
}),null)),null,2077444305);

return oc.web.core.slack_lander_check(params);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45148 = params__27255__auto__;
var map__45148__$1 = (((((!((map__45148 == null))))?(((((map__45148.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45148.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45148):map__45148);
var params = map__45148__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,429,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing slack-lander-check-slash-route",[oc.web.urls.slack_lander_check,"/"].join('')], null);
}),null)),null,-2140360492);

return oc.web.core.slack_lander_check(params);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_([oc.web.urls.slack_lander_check,"/"].join(''),action__27254__auto___45465);

oc.web.core.slack_lander_check_slash_route = (function oc$web$core$slack_lander_check_slash_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45466 = arguments.length;
var i__4737__auto___45467 = (0);
while(true){
if((i__4737__auto___45467 < len__4736__auto___45466)){
args__4742__auto__.push((arguments[i__4737__auto___45467]));

var G__45468 = (i__4737__auto___45467 + (1));
i__4737__auto___45467 = G__45468;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.slack_lander_check_slash_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.slack_lander_check_slash_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,[oc.web.urls.slack_lander_check,"/"].join(''),args__27253__auto__);
}));

(oc.web.core.slack_lander_check_slash_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.slack_lander_check_slash_route.cljs$lang$applyTo = (function (seq45150){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45150));
}));


var action__27254__auto___45469 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45151 = params__27255__auto__;
var map__45151__$1 = (((((!((map__45151 == null))))?(((((map__45151.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45151.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45151):map__45151);
var params = map__45151__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,434,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing google-lander-check-route",oc.web.urls.google_lander_check], null);
}),null)),null,-2097120188);

return oc.web.core.google_lander_check(params);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45153 = params__27255__auto__;
var map__45153__$1 = (((((!((map__45153 == null))))?(((((map__45153.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45153.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45153):map__45153);
var params = map__45153__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,434,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing google-lander-check-route",oc.web.urls.google_lander_check], null);
}),null)),null,-695918334);

return oc.web.core.google_lander_check(params);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.google_lander_check,action__27254__auto___45469);

oc.web.core.google_lander_check_route = (function oc$web$core$google_lander_check_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45470 = arguments.length;
var i__4737__auto___45471 = (0);
while(true){
if((i__4737__auto___45471 < len__4736__auto___45470)){
args__4742__auto__.push((arguments[i__4737__auto___45471]));

var G__45472 = (i__4737__auto___45471 + (1));
i__4737__auto___45471 = G__45472;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.google_lander_check_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.google_lander_check_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.google_lander_check,args__27253__auto__);
}));

(oc.web.core.google_lander_check_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.google_lander_check_route.cljs$lang$applyTo = (function (seq45155){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45155));
}));


var action__27254__auto___45473 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45156 = params__27255__auto__;
var map__45156__$1 = (((((!((map__45156 == null))))?(((((map__45156.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45156.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45156):map__45156);
var params = map__45156__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,439,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing google-lander-check-slash-route",[oc.web.urls.google_lander_check,"/"].join('')], null);
}),null)),null,-652141862);

return oc.web.core.google_lander_check(params);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45158 = params__27255__auto__;
var map__45158__$1 = (((((!((map__45158 == null))))?(((((map__45158.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45158.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45158):map__45158);
var params = map__45158__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,439,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing google-lander-check-slash-route",[oc.web.urls.google_lander_check,"/"].join('')], null);
}),null)),null,-1763634138);

return oc.web.core.google_lander_check(params);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_([oc.web.urls.google_lander_check,"/"].join(''),action__27254__auto___45473);

oc.web.core.google_lander_check_slash_route = (function oc$web$core$google_lander_check_slash_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45474 = arguments.length;
var i__4737__auto___45475 = (0);
while(true){
if((i__4737__auto___45475 < len__4736__auto___45474)){
args__4742__auto__.push((arguments[i__4737__auto___45475]));

var G__45476 = (i__4737__auto___45475 + (1));
i__4737__auto___45475 = G__45476;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.google_lander_check_slash_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.google_lander_check_slash_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,[oc.web.urls.google_lander_check,"/"].join(''),args__27253__auto__);
}));

(oc.web.core.google_lander_check_slash_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.google_lander_check_slash_route.cljs$lang$applyTo = (function (seq45160){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45160));
}));


var action__27254__auto___45477 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45161 = params__27255__auto__;
var map__45161__$1 = (((((!((map__45161 == null))))?(((((map__45161.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45161.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45161):map__45161);
var params = map__45161__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,444,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing email-confirmation-route",oc.web.urls.email_confirmation], null);
}),null)),null,-50837889);

if(cljs.core.seq(new cljs.core.Keyword(null,"token","token",-1211463215).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"query-params","query-params",900640534).cljs$core$IFn$_invoke$arity$1(params)))){
} else {
oc.web.router.redirect_BANG_((cljs.core.truth_(oc.web.lib.jwt.jwt())?oc.web.urls.your_digest_url():oc.web.urls.home));
}

oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"jwt","jwt",1504015441));

oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"show-login-overlay","show-login-overlay",1026669411));

return oc.web.core.simple_handler((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"email-verified","email-verified",-1932659306)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"email-verified","email-verified",-1932659306)));
}),"email-verification",target_45412,params);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45163 = params__27255__auto__;
var map__45163__$1 = (((((!((map__45163 == null))))?(((((map__45163.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45163.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45163):map__45163);
var params = map__45163__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,444,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing email-confirmation-route",oc.web.urls.email_confirmation], null);
}),null)),null,-278757969);

if(cljs.core.seq(new cljs.core.Keyword(null,"token","token",-1211463215).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"query-params","query-params",900640534).cljs$core$IFn$_invoke$arity$1(params)))){
} else {
oc.web.router.redirect_BANG_((cljs.core.truth_(oc.web.lib.jwt.jwt())?oc.web.urls.your_digest_url():oc.web.urls.home));
}

oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"jwt","jwt",1504015441));

oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"show-login-overlay","show-login-overlay",1026669411));

return oc.web.core.simple_handler((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"email-verified","email-verified",-1932659306)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"email-verified","email-verified",-1932659306)));
}),"email-verification",target_45412,params);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.email_confirmation,action__27254__auto___45477);

oc.web.core.email_confirmation_route = (function oc$web$core$email_confirmation_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45478 = arguments.length;
var i__4737__auto___45479 = (0);
while(true){
if((i__4737__auto___45479 < len__4736__auto___45478)){
args__4742__auto__.push((arguments[i__4737__auto___45479]));

var G__45480 = (i__4737__auto___45479 + (1));
i__4737__auto___45479 = G__45480;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.email_confirmation_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.email_confirmation_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.email_confirmation,args__27253__auto__);
}));

(oc.web.core.email_confirmation_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.email_confirmation_route.cljs$lang$applyTo = (function (seq45165){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45165));
}));


var action__27254__auto___45481 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45166 = params__27255__auto__;
var map__45166__$1 = (((((!((map__45166 == null))))?(((((map__45166.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45166.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45166):map__45166);
var params = map__45166__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,452,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing password-reset-route",oc.web.urls.password_reset], null);
}),null)),null,976937464);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
oc.web.router.redirect_BANG_(oc.web.urls.home);
} else {
}

return oc.web.core.simple_handler((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"password-reset-lander","password-reset-lander",-1929664691)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"password-reset-lander","password-reset-lander",-1929664691)));
}),"password-reset",target_45412,params);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45168 = params__27255__auto__;
var map__45168__$1 = (((((!((map__45168 == null))))?(((((map__45168.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45168.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45168):map__45168);
var params = map__45168__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,452,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing password-reset-route",oc.web.urls.password_reset], null);
}),null)),null,580451735);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
oc.web.router.redirect_BANG_(oc.web.urls.home);
} else {
}

return oc.web.core.simple_handler((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"password-reset-lander","password-reset-lander",-1929664691)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"password-reset-lander","password-reset-lander",-1929664691)));
}),"password-reset",target_45412,params);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.password_reset,action__27254__auto___45481);

oc.web.core.password_reset_route = (function oc$web$core$password_reset_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45482 = arguments.length;
var i__4737__auto___45483 = (0);
while(true){
if((i__4737__auto___45483 < len__4736__auto___45482)){
args__4742__auto__.push((arguments[i__4737__auto___45483]));

var G__45484 = (i__4737__auto___45483 + (1));
i__4737__auto___45483 = G__45484;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.password_reset_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.password_reset_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.password_reset,args__27253__auto__);
}));

(oc.web.core.password_reset_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.password_reset_route.cljs$lang$applyTo = (function (seq45170){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45170));
}));


var action__27254__auto___45485 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45171 = params__27255__auto__;
var map__45171__$1 = (((((!((map__45171 == null))))?(((((map__45171.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45171.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45171):map__45171);
var params = map__45171__$1;
var query_params = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45171__$1,new cljs.core.Keyword(null,"query-params","query-params",900640534));
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,458,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing confirm-invitation-route",oc.web.urls.confirm_invitation], null);
}),null)),null,1421212686);

if(((cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"token","token",-1211463215).cljs$core$IFn$_invoke$arity$1(query_params))) && (cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"invite-token","invite-token",610560078).cljs$core$IFn$_invoke$arity$1(query_params))))){
oc.web.router.redirect_BANG_(oc.web.urls.home);
} else {
}

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"jwt","jwt",1504015441));

oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"show-login-overlay","show-login-overlay",1026669411));
} else {
}

var invitee_type = ((cljs.core.contains_QMARK_(query_params,new cljs.core.Keyword(null,"invite-token","invite-token",610560078)))?new cljs.core.Keyword(null,"invitee-team-lander","invitee-team-lander",-1075916464):new cljs.core.Keyword(null,"invitee-lander","invitee-lander",1021558602));
return oc.web.core.simple_handler((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(invitee_type) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,invitee_type));
}),"confirm-invitation",target_45412,params);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45173 = params__27255__auto__;
var map__45173__$1 = (((((!((map__45173 == null))))?(((((map__45173.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45173.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45173):map__45173);
var params = map__45173__$1;
var query_params = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45173__$1,new cljs.core.Keyword(null,"query-params","query-params",900640534));
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,458,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing confirm-invitation-route",oc.web.urls.confirm_invitation], null);
}),null)),null,-1372757408);

if(((cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"token","token",-1211463215).cljs$core$IFn$_invoke$arity$1(query_params))) && (cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"invite-token","invite-token",610560078).cljs$core$IFn$_invoke$arity$1(query_params))))){
oc.web.router.redirect_BANG_(oc.web.urls.home);
} else {
}

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"jwt","jwt",1504015441));

oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"show-login-overlay","show-login-overlay",1026669411));
} else {
}

var invitee_type = ((cljs.core.contains_QMARK_(query_params,new cljs.core.Keyword(null,"invite-token","invite-token",610560078)))?new cljs.core.Keyword(null,"invitee-team-lander","invitee-team-lander",-1075916464):new cljs.core.Keyword(null,"invitee-lander","invitee-lander",1021558602));
return oc.web.core.simple_handler((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(invitee_type) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,invitee_type));
}),"confirm-invitation",target_45412,params);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.confirm_invitation,action__27254__auto___45485);

oc.web.core.confirm_invitation_route = (function oc$web$core$confirm_invitation_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45486 = arguments.length;
var i__4737__auto___45487 = (0);
while(true){
if((i__4737__auto___45487 < len__4736__auto___45486)){
args__4742__auto__.push((arguments[i__4737__auto___45487]));

var G__45488 = (i__4737__auto___45487 + (1));
i__4737__auto___45487 = G__45488;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.confirm_invitation_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.confirm_invitation_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.confirm_invitation,args__27253__auto__);
}));

(oc.web.core.confirm_invitation_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.confirm_invitation_route.cljs$lang$applyTo = (function (seq45175){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45175));
}));


var action__27254__auto___45489 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45176 = params__27255__auto__;
var map__45176__$1 = (((((!((map__45176 == null))))?(((((map__45176.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45176.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45176):map__45176);
var params = map__45176__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,471,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing confirm-invitation-password-route",oc.web.urls.confirm_invitation_password], null);
}),null)),null,-1688232635);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
} else {
oc.web.router.redirect_BANG_(oc.web.urls.home);
}

return oc.web.core.simple_handler((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"invitee-lander-password","invitee-lander-password",-173530491)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"invitee-lander-password","invitee-lander-password",-173530491)));
}),"confirm-invitation",target_45412,params);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45178 = params__27255__auto__;
var map__45178__$1 = (((((!((map__45178 == null))))?(((((map__45178.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45178.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45178):map__45178);
var params = map__45178__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,471,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing confirm-invitation-password-route",oc.web.urls.confirm_invitation_password], null);
}),null)),null,1039814536);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
} else {
oc.web.router.redirect_BANG_(oc.web.urls.home);
}

return oc.web.core.simple_handler((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"invitee-lander-password","invitee-lander-password",-173530491)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"invitee-lander-password","invitee-lander-password",-173530491)));
}),"confirm-invitation",target_45412,params);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.confirm_invitation_password,action__27254__auto___45489);

oc.web.core.confirm_invitation_password_route = (function oc$web$core$confirm_invitation_password_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45490 = arguments.length;
var i__4737__auto___45491 = (0);
while(true){
if((i__4737__auto___45491 < len__4736__auto___45490)){
args__4742__auto__.push((arguments[i__4737__auto___45491]));

var G__45492 = (i__4737__auto___45491 + (1));
i__4737__auto___45491 = G__45492;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.confirm_invitation_password_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.confirm_invitation_password_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.confirm_invitation_password,args__27253__auto__);
}));

(oc.web.core.confirm_invitation_password_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.confirm_invitation_password_route.cljs$lang$applyTo = (function (seq45180){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45180));
}));


var action__27254__auto___45493 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45181 = params__27255__auto__;
var map__45181__$1 = (((((!((map__45181 == null))))?(((((map__45181.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45181.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45181):map__45181);
var params = map__45181__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,477,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing confirm-invitation-profile-route",oc.web.urls.confirm_invitation_profile], null);
}),null)),null,1681062695);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
} else {
oc.web.router.redirect_BANG_(oc.web.urls.home);
}

return oc.web.core.simple_handler((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"invitee-lander-profile","invitee-lander-profile",514933281)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"invitee-lander-profile","invitee-lander-profile",514933281)));
}),"confirm-invitation",target_45412,params);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45183 = params__27255__auto__;
var map__45183__$1 = (((((!((map__45183 == null))))?(((((map__45183.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45183.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45183):map__45183);
var params = map__45183__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,477,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing confirm-invitation-profile-route",oc.web.urls.confirm_invitation_profile], null);
}),null)),null,1463490020);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
} else {
oc.web.router.redirect_BANG_(oc.web.urls.home);
}

return oc.web.core.simple_handler((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"invitee-lander-profile","invitee-lander-profile",514933281)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"invitee-lander-profile","invitee-lander-profile",514933281)));
}),"confirm-invitation",target_45412,params);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.confirm_invitation_profile,action__27254__auto___45493);

oc.web.core.confirm_invitation_profile_route = (function oc$web$core$confirm_invitation_profile_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45494 = arguments.length;
var i__4737__auto___45495 = (0);
while(true){
if((i__4737__auto___45495 < len__4736__auto___45494)){
args__4742__auto__.push((arguments[i__4737__auto___45495]));

var G__45496 = (i__4737__auto___45495 + (1));
i__4737__auto___45495 = G__45496;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.confirm_invitation_profile_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.confirm_invitation_profile_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.confirm_invitation_profile,args__27253__auto__);
}));

(oc.web.core.confirm_invitation_profile_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.confirm_invitation_profile_route.cljs$lang$applyTo = (function (seq45185){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45185));
}));


var action__27254__auto___45497 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45186 = params__27255__auto__;
var map__45186__$1 = (((((!((map__45186 == null))))?(((((map__45186.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45186.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45186):map__45186);
var params = map__45186__$1;
var query_params = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45186__$1,new cljs.core.Keyword(null,"query-params","query-params",900640534));
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,483,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing email-wall-route",oc.web.urls.email_wall], null);
}),null)),null,-2079669034);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
oc.web.router.redirect_BANG_(oc.web.urls.home);
} else {
}

return oc.web.core.simple_handler.cljs$core$IFn$_invoke$arity$variadic((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"email-wall","email-wall",-1908696318)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"email-wall","email-wall",-1908696318)));
}),"email-wall",target_45412,params,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true], 0));
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45188 = params__27255__auto__;
var map__45188__$1 = (((((!((map__45188 == null))))?(((((map__45188.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45188.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45188):map__45188);
var params = map__45188__$1;
var query_params = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45188__$1,new cljs.core.Keyword(null,"query-params","query-params",900640534));
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,483,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing email-wall-route",oc.web.urls.email_wall], null);
}),null)),null,620473057);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
oc.web.router.redirect_BANG_(oc.web.urls.home);
} else {
}

return oc.web.core.simple_handler.cljs$core$IFn$_invoke$arity$variadic((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"email-wall","email-wall",-1908696318)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"email-wall","email-wall",-1908696318)));
}),"email-wall",target_45412,params,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true], 0));
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.email_wall,action__27254__auto___45497);

oc.web.core.email_wall_route = (function oc$web$core$email_wall_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45498 = arguments.length;
var i__4737__auto___45499 = (0);
while(true){
if((i__4737__auto___45499 < len__4736__auto___45498)){
args__4742__auto__.push((arguments[i__4737__auto___45499]));

var G__45500 = (i__4737__auto___45499 + (1));
i__4737__auto___45499 = G__45500;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.email_wall_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.email_wall_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.email_wall,args__27253__auto__);
}));

(oc.web.core.email_wall_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.email_wall_route.cljs$lang$applyTo = (function (seq45190){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45190));
}));


var action__27254__auto___45501 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45191 = params__27255__auto__;
var map__45191__$1 = (((((!((map__45191 == null))))?(((((map__45191.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45191.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45191):map__45191);
var params = map__45191__$1;
var query_params = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45191__$1,new cljs.core.Keyword(null,"query-params","query-params",900640534));
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,490,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing email-wall-slash-route",[oc.web.urls.email_wall,"/"].join('')], null);
}),null)),null,1859512556);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
oc.web.router.redirect_BANG_(oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1(oc.web.lib.cookies.get_cookie(oc.web.router.last_org_cookie())));
} else {
}

return oc.web.core.simple_handler.cljs$core$IFn$_invoke$arity$variadic((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"email-wall","email-wall",-1908696318)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"email-wall","email-wall",-1908696318)));
}),"email-wall",target_45412,params,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true], 0));
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45193 = params__27255__auto__;
var map__45193__$1 = (((((!((map__45193 == null))))?(((((map__45193.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45193.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45193):map__45193);
var params = map__45193__$1;
var query_params = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45193__$1,new cljs.core.Keyword(null,"query-params","query-params",900640534));
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,490,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing email-wall-slash-route",[oc.web.urls.email_wall,"/"].join('')], null);
}),null)),null,1114599457);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
oc.web.router.redirect_BANG_(oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1(oc.web.lib.cookies.get_cookie(oc.web.router.last_org_cookie())));
} else {
}

return oc.web.core.simple_handler.cljs$core$IFn$_invoke$arity$variadic((function (){
return (oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.onboard_wrapper.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"email-wall","email-wall",-1908696318)) : oc.web.components.ui.onboard_wrapper.onboard_wrapper.call(null,new cljs.core.Keyword(null,"email-wall","email-wall",-1908696318)));
}),"email-wall",target_45412,params,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true], 0));
} else {
return null;
}
}
});
secretary.core.add_route_BANG_([oc.web.urls.email_wall,"/"].join(''),action__27254__auto___45501);

oc.web.core.email_wall_slash_route = (function oc$web$core$email_wall_slash_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45502 = arguments.length;
var i__4737__auto___45503 = (0);
while(true){
if((i__4737__auto___45503 < len__4736__auto___45502)){
args__4742__auto__.push((arguments[i__4737__auto___45503]));

var G__45504 = (i__4737__auto___45503 + (1));
i__4737__auto___45503 = G__45504;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.email_wall_slash_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.email_wall_slash_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,[oc.web.urls.email_wall,"/"].join(''),args__27253__auto__);
}));

(oc.web.core.email_wall_slash_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.email_wall_slash_route.cljs$lang$applyTo = (function (seq45195){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45195));
}));


var action__27254__auto___45505 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45196 = params__27255__auto__;
var map__45196__$1 = (((((!((map__45196 == null))))?(((((map__45196.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45196.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45196):map__45196);
var params = map__45196__$1;
var query_params = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45196__$1,new cljs.core.Keyword(null,"query-params","query-params",900640534));
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,496,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing login-wall-route",oc.web.urls.login_wall], null);
}),null)),null,948638478);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
oc.web.router.redirect_404_BANG_();
} else {
}

return oc.web.core.simple_handler.cljs$core$IFn$_invoke$arity$variadic(oc.web.components.ui.login_wall.login_wall,"login-wall",target_45412,params,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true], 0));
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45198 = params__27255__auto__;
var map__45198__$1 = (((((!((map__45198 == null))))?(((((map__45198.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45198.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45198):map__45198);
var params = map__45198__$1;
var query_params = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45198__$1,new cljs.core.Keyword(null,"query-params","query-params",900640534));
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,496,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing login-wall-route",oc.web.urls.login_wall], null);
}),null)),null,-1156181890);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
oc.web.router.redirect_404_BANG_();
} else {
}

return oc.web.core.simple_handler.cljs$core$IFn$_invoke$arity$variadic(oc.web.components.ui.login_wall.login_wall,"login-wall",target_45412,params,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true], 0));
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.login_wall,action__27254__auto___45505);

oc.web.core.login_wall_route = (function oc$web$core$login_wall_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45506 = arguments.length;
var i__4737__auto___45507 = (0);
while(true){
if((i__4737__auto___45507 < len__4736__auto___45506)){
args__4742__auto__.push((arguments[i__4737__auto___45507]));

var G__45508 = (i__4737__auto___45507 + (1));
i__4737__auto___45507 = G__45508;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.login_wall_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.login_wall_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.login_wall,args__27253__auto__);
}));

(oc.web.core.login_wall_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.login_wall_route.cljs$lang$applyTo = (function (seq45200){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45200));
}));


var action__27254__auto___45509 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45201 = params__27255__auto__;
var map__45201__$1 = (((((!((map__45201 == null))))?(((((map__45201.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45201.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45201):map__45201);
var params = map__45201__$1;
var query_params = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45201__$1,new cljs.core.Keyword(null,"query-params","query-params",900640534));
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,503,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing login-wall-slash-route",[oc.web.urls.login_wall,"/"].join('')], null);
}),null)),null,446778423);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
oc.web.router.redirect_404_BANG_();
} else {
}

return oc.web.core.simple_handler.cljs$core$IFn$_invoke$arity$variadic(oc.web.components.ui.login_wall.login_wall,"login-wall",target_45412,params,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true], 0));
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45203 = params__27255__auto__;
var map__45203__$1 = (((((!((map__45203 == null))))?(((((map__45203.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45203.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45203):map__45203);
var params = map__45203__$1;
var query_params = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45203__$1,new cljs.core.Keyword(null,"query-params","query-params",900640534));
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,503,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing login-wall-slash-route",[oc.web.urls.login_wall,"/"].join('')], null);
}),null)),null,-1710984454);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
oc.web.router.redirect_404_BANG_();
} else {
}

return oc.web.core.simple_handler.cljs$core$IFn$_invoke$arity$variadic(oc.web.components.ui.login_wall.login_wall,"login-wall",target_45412,params,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true], 0));
} else {
return null;
}
}
});
secretary.core.add_route_BANG_([oc.web.urls.login_wall,"/"].join(''),action__27254__auto___45509);

oc.web.core.login_wall_slash_route = (function oc$web$core$login_wall_slash_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45510 = arguments.length;
var i__4737__auto___45511 = (0);
while(true){
if((i__4737__auto___45511 < len__4736__auto___45510)){
args__4742__auto__.push((arguments[i__4737__auto___45511]));

var G__45512 = (i__4737__auto___45511 + (1));
i__4737__auto___45511 = G__45512;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.login_wall_slash_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.login_wall_slash_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,[oc.web.urls.login_wall,"/"].join(''),args__27253__auto__);
}));

(oc.web.core.login_wall_slash_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.login_wall_slash_route.cljs$lang$applyTo = (function (seq45205){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45205));
}));


var action__27254__auto___45513 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45206 = params__27255__auto__;
var map__45206__$1 = (((((!((map__45206 == null))))?(((((map__45206.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45206.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45206):map__45206);
var params = map__45206__$1;
var query_params = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45206__$1,new cljs.core.Keyword(null,"query-params","query-params",900640534));
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,509,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing native-login-route",oc.web.urls.native_login], null);
}),null)),null,1057407745);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
return oc.web.router.redirect_BANG_(((cljs.core.seq(oc.web.lib.cookies.get_cookie(oc.web.router.last_org_cookie())))?oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1(oc.web.lib.cookies.get_cookie(oc.web.router.last_org_cookie())):oc.web.urls.login));
} else {
return oc.web.core.simple_handler.cljs$core$IFn$_invoke$arity$variadic((function (){
var G__45208 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"title","title",636505583),"Welcome back!",new cljs.core.Keyword(null,"desc","desc",2093485764),""], null);
return (oc.web.components.ui.login_wall.login_wall.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.login_wall.login_wall.cljs$core$IFn$_invoke$arity$1(G__45208) : oc.web.components.ui.login_wall.login_wall.call(null,G__45208));
}),"login-wall",target_45412,params,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true], 0));
}
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45209 = params__27255__auto__;
var map__45209__$1 = (((((!((map__45209 == null))))?(((((map__45209.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45209.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45209):map__45209);
var params = map__45209__$1;
var query_params = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45209__$1,new cljs.core.Keyword(null,"query-params","query-params",900640534));
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,509,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing native-login-route",oc.web.urls.native_login], null);
}),null)),null,2006293327);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
return oc.web.router.redirect_BANG_(((cljs.core.seq(oc.web.lib.cookies.get_cookie(oc.web.router.last_org_cookie())))?oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1(oc.web.lib.cookies.get_cookie(oc.web.router.last_org_cookie())):oc.web.urls.login));
} else {
return oc.web.core.simple_handler.cljs$core$IFn$_invoke$arity$variadic((function (){
var G__45211 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"title","title",636505583),"Welcome back!",new cljs.core.Keyword(null,"desc","desc",2093485764),""], null);
return (oc.web.components.ui.login_wall.login_wall.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.login_wall.login_wall.cljs$core$IFn$_invoke$arity$1(G__45211) : oc.web.components.ui.login_wall.login_wall.call(null,G__45211));
}),"login-wall",target_45412,params,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true], 0));
}
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.native_login,action__27254__auto___45513);

oc.web.core.native_login_route = (function oc$web$core$native_login_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45514 = arguments.length;
var i__4737__auto___45515 = (0);
while(true){
if((i__4737__auto___45515 < len__4736__auto___45514)){
args__4742__auto__.push((arguments[i__4737__auto___45515]));

var G__45516 = (i__4737__auto___45515 + (1));
i__4737__auto___45515 = G__45516;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.native_login_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.native_login_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.native_login,args__27253__auto__);
}));

(oc.web.core.native_login_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.native_login_route.cljs$lang$applyTo = (function (seq45212){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45212));
}));


var action__27254__auto___45517 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45213 = params__27255__auto__;
var map__45213__$1 = (((((!((map__45213 == null))))?(((((map__45213.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45213.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45213):map__45213);
var params = map__45213__$1;
var query_params = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45213__$1,new cljs.core.Keyword(null,"query-params","query-params",900640534));
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,518,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing native-login-slash-route",[oc.web.urls.native_login,"/"].join('')], null);
}),null)),null,1323282959);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
return oc.web.router.redirect_BANG_(((cljs.core.seq(oc.web.lib.cookies.get_cookie(oc.web.router.last_org_cookie())))?oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1(oc.web.lib.cookies.get_cookie(oc.web.router.last_org_cookie())):oc.web.urls.login));
} else {
return oc.web.core.simple_handler.cljs$core$IFn$_invoke$arity$variadic((function (){
var G__45215 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"title","title",636505583),"Welcome back!",new cljs.core.Keyword(null,"desc","desc",2093485764),""], null);
return (oc.web.components.ui.login_wall.login_wall.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.login_wall.login_wall.cljs$core$IFn$_invoke$arity$1(G__45215) : oc.web.components.ui.login_wall.login_wall.call(null,G__45215));
}),"login-wall",target_45412,params,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true], 0));
}
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45216 = params__27255__auto__;
var map__45216__$1 = (((((!((map__45216 == null))))?(((((map__45216.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45216.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45216):map__45216);
var params = map__45216__$1;
var query_params = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45216__$1,new cljs.core.Keyword(null,"query-params","query-params",900640534));
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,518,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing native-login-slash-route",[oc.web.urls.native_login,"/"].join('')], null);
}),null)),null,1518705085);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
return oc.web.router.redirect_BANG_(((cljs.core.seq(oc.web.lib.cookies.get_cookie(oc.web.router.last_org_cookie())))?oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1(oc.web.lib.cookies.get_cookie(oc.web.router.last_org_cookie())):oc.web.urls.login));
} else {
return oc.web.core.simple_handler.cljs$core$IFn$_invoke$arity$variadic((function (){
var G__45218 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"title","title",636505583),"Welcome back!",new cljs.core.Keyword(null,"desc","desc",2093485764),""], null);
return (oc.web.components.ui.login_wall.login_wall.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.login_wall.login_wall.cljs$core$IFn$_invoke$arity$1(G__45218) : oc.web.components.ui.login_wall.login_wall.call(null,G__45218));
}),"login-wall",target_45412,params,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true], 0));
}
} else {
return null;
}
}
});
secretary.core.add_route_BANG_([oc.web.urls.native_login,"/"].join(''),action__27254__auto___45517);

oc.web.core.native_login_slash_route = (function oc$web$core$native_login_slash_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45518 = arguments.length;
var i__4737__auto___45519 = (0);
while(true){
if((i__4737__auto___45519 < len__4736__auto___45518)){
args__4742__auto__.push((arguments[i__4737__auto___45519]));

var G__45520 = (i__4737__auto___45519 + (1));
i__4737__auto___45519 = G__45520;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.native_login_slash_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.native_login_slash_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,[oc.web.urls.native_login,"/"].join(''),args__27253__auto__);
}));

(oc.web.core.native_login_slash_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.native_login_slash_route.cljs$lang$applyTo = (function (seq45219){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45219));
}));


var action__27254__auto___45521 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45220 = params__27255__auto__;
var map__45220__$1 = (((((!((map__45220 == null))))?(((((map__45220.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45220.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45220):map__45220);
var params = map__45220__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,527,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing logout-route",oc.web.urls.logout], null);
}),null)),null,-2012701739);

oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"jwt","jwt",1504015441));

oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"show-login-overlay","show-login-overlay",1026669411));

return oc.web.router.redirect_BANG_(((oc.shared.useragent.pseudo_native_QMARK_)?oc.web.urls.native_login:oc.web.urls.home));
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45222 = params__27255__auto__;
var map__45222__$1 = (((((!((map__45222 == null))))?(((((map__45222.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45222.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45222):map__45222);
var params = map__45222__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,527,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing logout-route",oc.web.urls.logout], null);
}),null)),null,-1578108225);

oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"jwt","jwt",1504015441));

oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"show-login-overlay","show-login-overlay",1026669411));

return oc.web.router.redirect_BANG_(((oc.shared.useragent.pseudo_native_QMARK_)?oc.web.urls.native_login:oc.web.urls.home));
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.logout,action__27254__auto___45521);

oc.web.core.logout_route = (function oc$web$core$logout_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45522 = arguments.length;
var i__4737__auto___45523 = (0);
while(true){
if((i__4737__auto___45523 < len__4736__auto___45522)){
args__4742__auto__.push((arguments[i__4737__auto___45523]));

var G__45524 = (i__4737__auto___45523 + (1));
i__4737__auto___45523 = G__45524;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.logout_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.logout_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.logout,args__27253__auto__);
}));

(oc.web.core.logout_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.logout_route.cljs$lang$applyTo = (function (seq45224){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45224));
}));


var action__27254__auto___45525 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45225 = params__27255__auto__;
var map__45225__$1 = (((((!((map__45225 == null))))?(((((map__45225.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45225.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45225):map__45225);
var params = map__45225__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,535,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing apps-detect-route",oc.web.urls.apps_detect], null);
}),null)),null,-1142144080);

return oc.web.router.redirect_BANG_((cljs.core.truth_(oc.shared.useragent.mac_QMARK_)?oc.web.local_settings.mac_app_url:(cljs.core.truth_(oc.shared.useragent.windows_QMARK_)?oc.web.local_settings.win_app_url:(cljs.core.truth_(oc.shared.useragent.ios_QMARK_)?oc.web.local_settings.ios_app_url:(cljs.core.truth_(oc.shared.useragent.android_QMARK_)?oc.web.local_settings.android_app_url:oc.web.urls.home
)))));
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45227 = params__27255__auto__;
var map__45227__$1 = (((((!((map__45227 == null))))?(((((map__45227.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45227.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45227):map__45227);
var params = map__45227__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,535,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing apps-detect-route",oc.web.urls.apps_detect], null);
}),null)),null,1930422144);

return oc.web.router.redirect_BANG_((cljs.core.truth_(oc.shared.useragent.mac_QMARK_)?oc.web.local_settings.mac_app_url:(cljs.core.truth_(oc.shared.useragent.windows_QMARK_)?oc.web.local_settings.win_app_url:(cljs.core.truth_(oc.shared.useragent.ios_QMARK_)?oc.web.local_settings.ios_app_url:(cljs.core.truth_(oc.shared.useragent.android_QMARK_)?oc.web.local_settings.android_app_url:oc.web.urls.home
)))));
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.apps_detect,action__27254__auto___45525);

oc.web.core.apps_detect_route = (function oc$web$core$apps_detect_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45526 = arguments.length;
var i__4737__auto___45527 = (0);
while(true){
if((i__4737__auto___45527 < len__4736__auto___45526)){
args__4742__auto__.push((arguments[i__4737__auto___45527]));

var G__45528 = (i__4737__auto___45527 + (1));
i__4737__auto___45527 = G__45528;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.apps_detect_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.apps_detect_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.apps_detect,args__27253__auto__);
}));

(oc.web.core.apps_detect_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.apps_detect_route.cljs$lang$applyTo = (function (seq45229){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45229));
}));


var action__27254__auto___45529 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45230 = params__27255__auto__;
var map__45230__$1 = (((((!((map__45230 == null))))?(((((map__45230.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45230.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45230):map__45230);
var params = map__45230__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,545,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing org-route",oc.web.urls.org.cljs$core$IFn$_invoke$arity$1(":org")], null);
}),null)),null,1763334635);

return oc.web.core.org_handler("org",target_45412,oc.web.components.org_dashboard.org_dashboard,params);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45232 = params__27255__auto__;
var map__45232__$1 = (((((!((map__45232 == null))))?(((((map__45232.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45232.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45232):map__45232);
var params = map__45232__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,545,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing org-route",oc.web.urls.org.cljs$core$IFn$_invoke$arity$1(":org")], null);
}),null)),null,-366565951);

return oc.web.core.org_handler("org",target_45412,oc.web.components.org_dashboard.org_dashboard,params);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.org.cljs$core$IFn$_invoke$arity$1(":org"),action__27254__auto___45529);

oc.web.core.org_route = (function oc$web$core$org_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45530 = arguments.length;
var i__4737__auto___45531 = (0);
while(true){
if((i__4737__auto___45531 < len__4736__auto___45530)){
args__4742__auto__.push((arguments[i__4737__auto___45531]));

var G__45532 = (i__4737__auto___45531 + (1));
i__4737__auto___45531 = G__45532;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.org_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.org_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.org.cljs$core$IFn$_invoke$arity$1(":org"),args__27253__auto__);
}));

(oc.web.core.org_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.org_route.cljs$lang$applyTo = (function (seq45234){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45234));
}));


var action__27254__auto___45533 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45235 = params__27255__auto__;
var map__45235__$1 = (((((!((map__45235 == null))))?(((((map__45235.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45235.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45235):map__45235);
var params = map__45235__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,549,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing org-slash-route",[oc.web.urls.org.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join('')], null);
}),null)),null,-1096737884);

return oc.web.core.org_handler("org",target_45412,oc.web.components.org_dashboard.org_dashboard,params);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45237 = params__27255__auto__;
var map__45237__$1 = (((((!((map__45237 == null))))?(((((map__45237.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45237.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45237):map__45237);
var params = map__45237__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,549,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing org-slash-route",[oc.web.urls.org.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join('')], null);
}),null)),null,-1613041026);

return oc.web.core.org_handler("org",target_45412,oc.web.components.org_dashboard.org_dashboard,params);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_([oc.web.urls.org.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join(''),action__27254__auto___45533);

oc.web.core.org_slash_route = (function oc$web$core$org_slash_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45534 = arguments.length;
var i__4737__auto___45535 = (0);
while(true){
if((i__4737__auto___45535 < len__4736__auto___45534)){
args__4742__auto__.push((arguments[i__4737__auto___45535]));

var G__45536 = (i__4737__auto___45535 + (1));
i__4737__auto___45535 = G__45536;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.org_slash_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.org_slash_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,[oc.web.urls.org.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join(''),args__27253__auto__);
}));

(oc.web.core.org_slash_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.org_slash_route.cljs$lang$applyTo = (function (seq45239){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45239));
}));


var action__27254__auto___45537 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45240 = params__27255__auto__;
var map__45240__$1 = (((((!((map__45240 == null))))?(((((map__45240.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45240.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45240):map__45240);
var params = map__45240__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,553,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing inbox-route",oc.web.urls.inbox.cljs$core$IFn$_invoke$arity$1(":org")], null);
}),null)),null,481467156);

return oc.web.core.org_handler("inbox",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"inbox"));
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45242 = params__27255__auto__;
var map__45242__$1 = (((((!((map__45242 == null))))?(((((map__45242.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45242.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45242):map__45242);
var params = map__45242__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,553,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing inbox-route",oc.web.urls.inbox.cljs$core$IFn$_invoke$arity$1(":org")], null);
}),null)),null,1847122113);

return oc.web.core.org_handler("inbox",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"inbox"));
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.inbox.cljs$core$IFn$_invoke$arity$1(":org"),action__27254__auto___45537);

oc.web.core.inbox_route = (function oc$web$core$inbox_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45538 = arguments.length;
var i__4737__auto___45539 = (0);
while(true){
if((i__4737__auto___45539 < len__4736__auto___45538)){
args__4742__auto__.push((arguments[i__4737__auto___45539]));

var G__45540 = (i__4737__auto___45539 + (1));
i__4737__auto___45539 = G__45540;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.inbox_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.inbox_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.inbox.cljs$core$IFn$_invoke$arity$1(":org"),args__27253__auto__);
}));

(oc.web.core.inbox_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.inbox_route.cljs$lang$applyTo = (function (seq45244){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45244));
}));


var action__27254__auto___45541 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45245 = params__27255__auto__;
var map__45245__$1 = (((((!((map__45245 == null))))?(((((map__45245.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45245.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45245):map__45245);
var params = map__45245__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,557,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing inbox-slash-route",[oc.web.urls.inbox.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join('')], null);
}),null)),null,-2086940374);

return oc.web.core.org_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"inbox"));
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45247 = params__27255__auto__;
var map__45247__$1 = (((((!((map__45247 == null))))?(((((map__45247.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45247.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45247):map__45247);
var params = map__45247__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,557,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing inbox-slash-route",[oc.web.urls.inbox.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join('')], null);
}),null)),null,727015086);

return oc.web.core.org_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"inbox"));
} else {
return null;
}
}
});
secretary.core.add_route_BANG_([oc.web.urls.inbox.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join(''),action__27254__auto___45541);

oc.web.core.inbox_slash_route = (function oc$web$core$inbox_slash_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45542 = arguments.length;
var i__4737__auto___45543 = (0);
while(true){
if((i__4737__auto___45543 < len__4736__auto___45542)){
args__4742__auto__.push((arguments[i__4737__auto___45543]));

var G__45544 = (i__4737__auto___45543 + (1));
i__4737__auto___45543 = G__45544;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.inbox_slash_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.inbox_slash_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,[oc.web.urls.inbox.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join(''),args__27253__auto__);
}));

(oc.web.core.inbox_slash_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.inbox_slash_route.cljs$lang$applyTo = (function (seq45249){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45249));
}));


var action__27254__auto___45545 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45250 = params__27255__auto__;
var map__45250__$1 = (((((!((map__45250 == null))))?(((((map__45250.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45250.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45250):map__45250);
var params = map__45250__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,561,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing all-posts-route",oc.web.urls.all_posts.cljs$core$IFn$_invoke$arity$1(":org")], null);
}),null)),null,-1542173660);

return oc.web.core.org_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"all-posts"));
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45252 = params__27255__auto__;
var map__45252__$1 = (((((!((map__45252 == null))))?(((((map__45252.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45252.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45252):map__45252);
var params = map__45252__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,561,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing all-posts-route",oc.web.urls.all_posts.cljs$core$IFn$_invoke$arity$1(":org")], null);
}),null)),null,958347005);

return oc.web.core.org_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"all-posts"));
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.all_posts.cljs$core$IFn$_invoke$arity$1(":org"),action__27254__auto___45545);

oc.web.core.all_posts_route = (function oc$web$core$all_posts_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45546 = arguments.length;
var i__4737__auto___45547 = (0);
while(true){
if((i__4737__auto___45547 < len__4736__auto___45546)){
args__4742__auto__.push((arguments[i__4737__auto___45547]));

var G__45548 = (i__4737__auto___45547 + (1));
i__4737__auto___45547 = G__45548;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.all_posts_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.all_posts_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.all_posts.cljs$core$IFn$_invoke$arity$1(":org"),args__27253__auto__);
}));

(oc.web.core.all_posts_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.all_posts_route.cljs$lang$applyTo = (function (seq45254){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45254));
}));


var action__27254__auto___45549 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45255 = params__27255__auto__;
var map__45255__$1 = (((((!((map__45255 == null))))?(((((map__45255.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45255.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45255):map__45255);
var params = map__45255__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,565,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing all-posts-slash-route",[oc.web.urls.all_posts.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join('')], null);
}),null)),null,-908907945);

return oc.web.core.org_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"all-posts"));
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45257 = params__27255__auto__;
var map__45257__$1 = (((((!((map__45257 == null))))?(((((map__45257.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45257.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45257):map__45257);
var params = map__45257__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,565,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing all-posts-slash-route",[oc.web.urls.all_posts.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join('')], null);
}),null)),null,-315730140);

return oc.web.core.org_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"all-posts"));
} else {
return null;
}
}
});
secretary.core.add_route_BANG_([oc.web.urls.all_posts.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join(''),action__27254__auto___45549);

oc.web.core.all_posts_slash_route = (function oc$web$core$all_posts_slash_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45550 = arguments.length;
var i__4737__auto___45551 = (0);
while(true){
if((i__4737__auto___45551 < len__4736__auto___45550)){
args__4742__auto__.push((arguments[i__4737__auto___45551]));

var G__45552 = (i__4737__auto___45551 + (1));
i__4737__auto___45551 = G__45552;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.all_posts_slash_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.all_posts_slash_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,[oc.web.urls.all_posts.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join(''),args__27253__auto__);
}));

(oc.web.core.all_posts_slash_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.all_posts_slash_route.cljs$lang$applyTo = (function (seq45259){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45259));
}));


var action__27254__auto___45553 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45260 = params__27255__auto__;
var map__45260__$1 = (((((!((map__45260 == null))))?(((((map__45260.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45260.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45260):map__45260);
var params = map__45260__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,569,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing following-route",oc.web.urls.following.cljs$core$IFn$_invoke$arity$1(":org")], null);
}),null)),null,834313042);

return oc.web.core.org_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"following"));
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45262 = params__27255__auto__;
var map__45262__$1 = (((((!((map__45262 == null))))?(((((map__45262.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45262.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45262):map__45262);
var params = map__45262__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,569,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing following-route",oc.web.urls.following.cljs$core$IFn$_invoke$arity$1(":org")], null);
}),null)),null,-1545208747);

return oc.web.core.org_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"following"));
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.following.cljs$core$IFn$_invoke$arity$1(":org"),action__27254__auto___45553);

oc.web.core.following_route = (function oc$web$core$following_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45554 = arguments.length;
var i__4737__auto___45555 = (0);
while(true){
if((i__4737__auto___45555 < len__4736__auto___45554)){
args__4742__auto__.push((arguments[i__4737__auto___45555]));

var G__45556 = (i__4737__auto___45555 + (1));
i__4737__auto___45555 = G__45556;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.following_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.following_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.following.cljs$core$IFn$_invoke$arity$1(":org"),args__27253__auto__);
}));

(oc.web.core.following_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.following_route.cljs$lang$applyTo = (function (seq45264){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45264));
}));


var action__27254__auto___45557 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45265 = params__27255__auto__;
var map__45265__$1 = (((((!((map__45265 == null))))?(((((map__45265.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45265.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45265):map__45265);
var params = map__45265__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,573,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing following-slash-route",[oc.web.urls.following.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join('')], null);
}),null)),null,2002136001);

return oc.web.core.org_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"following"));
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45267 = params__27255__auto__;
var map__45267__$1 = (((((!((map__45267 == null))))?(((((map__45267.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45267.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45267):map__45267);
var params = map__45267__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,573,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing following-slash-route",[oc.web.urls.following.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join('')], null);
}),null)),null,-1247071997);

return oc.web.core.org_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"following"));
} else {
return null;
}
}
});
secretary.core.add_route_BANG_([oc.web.urls.following.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join(''),action__27254__auto___45557);

oc.web.core.following_slash_route = (function oc$web$core$following_slash_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45558 = arguments.length;
var i__4737__auto___45559 = (0);
while(true){
if((i__4737__auto___45559 < len__4736__auto___45558)){
args__4742__auto__.push((arguments[i__4737__auto___45559]));

var G__45560 = (i__4737__auto___45559 + (1));
i__4737__auto___45559 = G__45560;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.following_slash_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.following_slash_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,[oc.web.urls.following.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join(''),args__27253__auto__);
}));

(oc.web.core.following_slash_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.following_slash_route.cljs$lang$applyTo = (function (seq45269){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45269));
}));


var action__27254__auto___45561 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45270 = params__27255__auto__;
var map__45270__$1 = (((((!((map__45270 == null))))?(((((map__45270.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45270.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45270):map__45270);
var params = map__45270__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,577,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing unfollowing-route",oc.web.urls.unfollowing.cljs$core$IFn$_invoke$arity$1(":org")], null);
}),null)),null,1869596853);

return oc.web.core.org_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"unfollowing"));
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45272 = params__27255__auto__;
var map__45272__$1 = (((((!((map__45272 == null))))?(((((map__45272.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45272.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45272):map__45272);
var params = map__45272__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,577,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing unfollowing-route",oc.web.urls.unfollowing.cljs$core$IFn$_invoke$arity$1(":org")], null);
}),null)),null,713485904);

return oc.web.core.org_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"unfollowing"));
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.unfollowing.cljs$core$IFn$_invoke$arity$1(":org"),action__27254__auto___45561);

oc.web.core.unfollowing_route = (function oc$web$core$unfollowing_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45562 = arguments.length;
var i__4737__auto___45563 = (0);
while(true){
if((i__4737__auto___45563 < len__4736__auto___45562)){
args__4742__auto__.push((arguments[i__4737__auto___45563]));

var G__45564 = (i__4737__auto___45563 + (1));
i__4737__auto___45563 = G__45564;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.unfollowing_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.unfollowing_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.unfollowing.cljs$core$IFn$_invoke$arity$1(":org"),args__27253__auto__);
}));

(oc.web.core.unfollowing_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.unfollowing_route.cljs$lang$applyTo = (function (seq45274){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45274));
}));


var action__27254__auto___45565 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45275 = params__27255__auto__;
var map__45275__$1 = (((((!((map__45275 == null))))?(((((map__45275.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45275.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45275):map__45275);
var params = map__45275__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,581,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing unfollowing-slash-route",[oc.web.urls.unfollowing.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join('')], null);
}),null)),null,-432489293);

return oc.web.core.org_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"unfollowing"));
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45277 = params__27255__auto__;
var map__45277__$1 = (((((!((map__45277 == null))))?(((((map__45277.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45277.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45277):map__45277);
var params = map__45277__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,581,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing unfollowing-slash-route",[oc.web.urls.unfollowing.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join('')], null);
}),null)),null,2005310306);

return oc.web.core.org_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"unfollowing"));
} else {
return null;
}
}
});
secretary.core.add_route_BANG_([oc.web.urls.unfollowing.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join(''),action__27254__auto___45565);

oc.web.core.unfollowing_slash_route = (function oc$web$core$unfollowing_slash_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45566 = arguments.length;
var i__4737__auto___45567 = (0);
while(true){
if((i__4737__auto___45567 < len__4736__auto___45566)){
args__4742__auto__.push((arguments[i__4737__auto___45567]));

var G__45568 = (i__4737__auto___45567 + (1));
i__4737__auto___45567 = G__45568;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.unfollowing_slash_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.unfollowing_slash_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,[oc.web.urls.unfollowing.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join(''),args__27253__auto__);
}));

(oc.web.core.unfollowing_slash_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.unfollowing_slash_route.cljs$lang$applyTo = (function (seq45279){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45279));
}));


var action__27254__auto___45569 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45280 = params__27255__auto__;
var map__45280__$1 = (((((!((map__45280 == null))))?(((((map__45280.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45280.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45280):map__45280);
var params = map__45280__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,585,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing first-ever-landing-route",oc.web.urls.first_ever_landing.cljs$core$IFn$_invoke$arity$1(":org")], null);
}),null)),null,205990309);

return oc.web.core.org_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),oc.web.urls.default_board_slug));
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45282 = params__27255__auto__;
var map__45282__$1 = (((((!((map__45282 == null))))?(((((map__45282.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45282.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45282):map__45282);
var params = map__45282__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,585,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing first-ever-landing-route",oc.web.urls.first_ever_landing.cljs$core$IFn$_invoke$arity$1(":org")], null);
}),null)),null,-993157230);

return oc.web.core.org_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),oc.web.urls.default_board_slug));
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.first_ever_landing.cljs$core$IFn$_invoke$arity$1(":org"),action__27254__auto___45569);

oc.web.core.first_ever_landing_route = (function oc$web$core$first_ever_landing_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45570 = arguments.length;
var i__4737__auto___45571 = (0);
while(true){
if((i__4737__auto___45571 < len__4736__auto___45570)){
args__4742__auto__.push((arguments[i__4737__auto___45571]));

var G__45572 = (i__4737__auto___45571 + (1));
i__4737__auto___45571 = G__45572;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.first_ever_landing_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.first_ever_landing_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.first_ever_landing.cljs$core$IFn$_invoke$arity$1(":org"),args__27253__auto__);
}));

(oc.web.core.first_ever_landing_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.first_ever_landing_route.cljs$lang$applyTo = (function (seq45284){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45284));
}));


var action__27254__auto___45573 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45285 = params__27255__auto__;
var map__45285__$1 = (((((!((map__45285 == null))))?(((((map__45285.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45285.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45285):map__45285);
var params = map__45285__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,589,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing first-ever-landing-slash-route",[oc.web.urls.first_ever_landing.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join('')], null);
}),null)),null,-1185674865);

return oc.web.core.org_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),oc.web.urls.default_board_slug));
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45287 = params__27255__auto__;
var map__45287__$1 = (((((!((map__45287 == null))))?(((((map__45287.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45287.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45287):map__45287);
var params = map__45287__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,589,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing first-ever-landing-slash-route",[oc.web.urls.first_ever_landing.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join('')], null);
}),null)),null,-1959187341);

return oc.web.core.org_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),oc.web.urls.default_board_slug));
} else {
return null;
}
}
});
secretary.core.add_route_BANG_([oc.web.urls.first_ever_landing.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join(''),action__27254__auto___45573);

oc.web.core.first_ever_landing_slash_route = (function oc$web$core$first_ever_landing_slash_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45574 = arguments.length;
var i__4737__auto___45575 = (0);
while(true){
if((i__4737__auto___45575 < len__4736__auto___45574)){
args__4742__auto__.push((arguments[i__4737__auto___45575]));

var G__45576 = (i__4737__auto___45575 + (1));
i__4737__auto___45575 = G__45576;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.first_ever_landing_slash_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.first_ever_landing_slash_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,[oc.web.urls.first_ever_landing.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join(''),args__27253__auto__);
}));

(oc.web.core.first_ever_landing_slash_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.first_ever_landing_slash_route.cljs$lang$applyTo = (function (seq45289){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45289));
}));


var action__27254__auto___45577 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45290 = params__27255__auto__;
var map__45290__$1 = (((((!((map__45290 == null))))?(((((map__45290.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45290.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45290):map__45290);
var params = map__45290__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,593,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing follow-ups-route",oc.web.urls.follow_ups.cljs$core$IFn$_invoke$arity$1(":org")], null);
}),null)),null,968574023);

return oc.web.router.redirect_BANG_(oc.web.urls.bookmarks.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"org","org",1495985).cljs$core$IFn$_invoke$arity$1(params)));
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45292 = params__27255__auto__;
var map__45292__$1 = (((((!((map__45292 == null))))?(((((map__45292.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45292.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45292):map__45292);
var params = map__45292__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,593,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing follow-ups-route",oc.web.urls.follow_ups.cljs$core$IFn$_invoke$arity$1(":org")], null);
}),null)),null,-882106617);

return oc.web.router.redirect_BANG_(oc.web.urls.bookmarks.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"org","org",1495985).cljs$core$IFn$_invoke$arity$1(params)));
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.follow_ups.cljs$core$IFn$_invoke$arity$1(":org"),action__27254__auto___45577);

oc.web.core.follow_ups_route = (function oc$web$core$follow_ups_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45578 = arguments.length;
var i__4737__auto___45579 = (0);
while(true){
if((i__4737__auto___45579 < len__4736__auto___45578)){
args__4742__auto__.push((arguments[i__4737__auto___45579]));

var G__45580 = (i__4737__auto___45579 + (1));
i__4737__auto___45579 = G__45580;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.follow_ups_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.follow_ups_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.follow_ups.cljs$core$IFn$_invoke$arity$1(":org"),args__27253__auto__);
}));

(oc.web.core.follow_ups_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.follow_ups_route.cljs$lang$applyTo = (function (seq45294){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45294));
}));


var action__27254__auto___45581 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45295 = params__27255__auto__;
var map__45295__$1 = (((((!((map__45295 == null))))?(((((map__45295.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45295.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45295):map__45295);
var params = map__45295__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,597,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing follow-ups-slash-route",[oc.web.urls.follow_ups.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join('')], null);
}),null)),null,324908349);

return oc.web.router.redirect_BANG_(oc.web.urls.bookmarks.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"org","org",1495985).cljs$core$IFn$_invoke$arity$1(params)));
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45297 = params__27255__auto__;
var map__45297__$1 = (((((!((map__45297 == null))))?(((((map__45297.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45297.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45297):map__45297);
var params = map__45297__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,597,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing follow-ups-slash-route",[oc.web.urls.follow_ups.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join('')], null);
}),null)),null,-503146786);

return oc.web.router.redirect_BANG_(oc.web.urls.bookmarks.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"org","org",1495985).cljs$core$IFn$_invoke$arity$1(params)));
} else {
return null;
}
}
});
secretary.core.add_route_BANG_([oc.web.urls.follow_ups.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join(''),action__27254__auto___45581);

oc.web.core.follow_ups_slash_route = (function oc$web$core$follow_ups_slash_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45582 = arguments.length;
var i__4737__auto___45583 = (0);
while(true){
if((i__4737__auto___45583 < len__4736__auto___45582)){
args__4742__auto__.push((arguments[i__4737__auto___45583]));

var G__45584 = (i__4737__auto___45583 + (1));
i__4737__auto___45583 = G__45584;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.follow_ups_slash_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.follow_ups_slash_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,[oc.web.urls.follow_ups.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join(''),args__27253__auto__);
}));

(oc.web.core.follow_ups_slash_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.follow_ups_slash_route.cljs$lang$applyTo = (function (seq45299){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45299));
}));


var action__27254__auto___45585 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45300 = params__27255__auto__;
var map__45300__$1 = (((((!((map__45300 == null))))?(((((map__45300.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45300.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45300):map__45300);
var params = map__45300__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,601,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing bookmarks-route",oc.web.urls.bookmarks.cljs$core$IFn$_invoke$arity$1(":org")], null);
}),null)),null,-102758132);

return oc.web.core.org_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"bookmarks"));
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45302 = params__27255__auto__;
var map__45302__$1 = (((((!((map__45302 == null))))?(((((map__45302.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45302.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45302):map__45302);
var params = map__45302__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,601,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing bookmarks-route",oc.web.urls.bookmarks.cljs$core$IFn$_invoke$arity$1(":org")], null);
}),null)),null,-2066138956);

return oc.web.core.org_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"bookmarks"));
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.bookmarks.cljs$core$IFn$_invoke$arity$1(":org"),action__27254__auto___45585);

oc.web.core.bookmarks_route = (function oc$web$core$bookmarks_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45586 = arguments.length;
var i__4737__auto___45587 = (0);
while(true){
if((i__4737__auto___45587 < len__4736__auto___45586)){
args__4742__auto__.push((arguments[i__4737__auto___45587]));

var G__45588 = (i__4737__auto___45587 + (1));
i__4737__auto___45587 = G__45588;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.bookmarks_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.bookmarks_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.bookmarks.cljs$core$IFn$_invoke$arity$1(":org"),args__27253__auto__);
}));

(oc.web.core.bookmarks_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.bookmarks_route.cljs$lang$applyTo = (function (seq45304){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45304));
}));


var action__27254__auto___45589 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45305 = params__27255__auto__;
var map__45305__$1 = (((((!((map__45305 == null))))?(((((map__45305.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45305.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45305):map__45305);
var params = map__45305__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,605,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing bookmarks-slash-route",[oc.web.urls.bookmarks.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join('')], null);
}),null)),null,1597760911);

return oc.web.core.org_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"bookmarks"));
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45307 = params__27255__auto__;
var map__45307__$1 = (((((!((map__45307 == null))))?(((((map__45307.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45307.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45307):map__45307);
var params = map__45307__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,605,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing bookmarks-slash-route",[oc.web.urls.bookmarks.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join('')], null);
}),null)),null,-1319704093);

return oc.web.core.org_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"bookmarks"));
} else {
return null;
}
}
});
secretary.core.add_route_BANG_([oc.web.urls.bookmarks.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join(''),action__27254__auto___45589);

oc.web.core.bookmarks_slash_route = (function oc$web$core$bookmarks_slash_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45590 = arguments.length;
var i__4737__auto___45591 = (0);
while(true){
if((i__4737__auto___45591 < len__4736__auto___45590)){
args__4742__auto__.push((arguments[i__4737__auto___45591]));

var G__45592 = (i__4737__auto___45591 + (1));
i__4737__auto___45591 = G__45592;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.bookmarks_slash_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.bookmarks_slash_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,[oc.web.urls.bookmarks.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join(''),args__27253__auto__);
}));

(oc.web.core.bookmarks_slash_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.bookmarks_slash_route.cljs$lang$applyTo = (function (seq45309){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45309));
}));


var action__27254__auto___45593 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45310 = params__27255__auto__;
var map__45310__$1 = (((((!((map__45310 == null))))?(((((map__45310.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45310.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45310):map__45310);
var params = map__45310__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,609,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing board-route",oc.web.urls.drafts.cljs$core$IFn$_invoke$arity$1(":org")], null);
}),null)),null,1833911477);

return oc.web.core.board_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"drafts"));
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45312 = params__27255__auto__;
var map__45312__$1 = (((((!((map__45312 == null))))?(((((map__45312.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45312.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45312):map__45312);
var params = map__45312__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,609,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing board-route",oc.web.urls.drafts.cljs$core$IFn$_invoke$arity$1(":org")], null);
}),null)),null,906857707);

return oc.web.core.board_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"drafts"));
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.drafts.cljs$core$IFn$_invoke$arity$1(":org"),action__27254__auto___45593);

oc.web.core.drafts_route = (function oc$web$core$drafts_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45594 = arguments.length;
var i__4737__auto___45595 = (0);
while(true){
if((i__4737__auto___45595 < len__4736__auto___45594)){
args__4742__auto__.push((arguments[i__4737__auto___45595]));

var G__45596 = (i__4737__auto___45595 + (1));
i__4737__auto___45595 = G__45596;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.drafts_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.drafts_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.drafts.cljs$core$IFn$_invoke$arity$1(":org"),args__27253__auto__);
}));

(oc.web.core.drafts_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.drafts_route.cljs$lang$applyTo = (function (seq45314){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45314));
}));


var action__27254__auto___45597 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45315 = params__27255__auto__;
var map__45315__$1 = (((((!((map__45315 == null))))?(((((map__45315.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45315.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45315):map__45315);
var params = map__45315__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,613,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing board-slash-route",[oc.web.urls.drafts.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join('')], null);
}),null)),null,1394587386);

return oc.web.core.board_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"drafts"));
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45317 = params__27255__auto__;
var map__45317__$1 = (((((!((map__45317 == null))))?(((((map__45317.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45317.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45317):map__45317);
var params = map__45317__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,613,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing board-slash-route",[oc.web.urls.drafts.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join('')], null);
}),null)),null,1072639823);

return oc.web.core.board_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"drafts"));
} else {
return null;
}
}
});
secretary.core.add_route_BANG_([oc.web.urls.drafts.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join(''),action__27254__auto___45597);

oc.web.core.drafts_slash_route = (function oc$web$core$drafts_slash_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45598 = arguments.length;
var i__4737__auto___45599 = (0);
while(true){
if((i__4737__auto___45599 < len__4736__auto___45598)){
args__4742__auto__.push((arguments[i__4737__auto___45599]));

var G__45600 = (i__4737__auto___45599 + (1));
i__4737__auto___45599 = G__45600;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.drafts_slash_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.drafts_slash_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,[oc.web.urls.drafts.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join(''),args__27253__auto__);
}));

(oc.web.core.drafts_slash_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.drafts_slash_route.cljs$lang$applyTo = (function (seq45319){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45319));
}));


var action__27254__auto___45601 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45320 = params__27255__auto__;
var map__45320__$1 = (((((!((map__45320 == null))))?(((((map__45320.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45320.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45320):map__45320);
var params = map__45320__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,617,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing replies-route",oc.web.urls.replies.cljs$core$IFn$_invoke$arity$1(":org")], null);
}),null)),null,-1161684895);

return oc.web.core.org_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"replies"));
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45322 = params__27255__auto__;
var map__45322__$1 = (((((!((map__45322 == null))))?(((((map__45322.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45322.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45322):map__45322);
var params = map__45322__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,617,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing replies-route",oc.web.urls.replies.cljs$core$IFn$_invoke$arity$1(":org")], null);
}),null)),null,312855107);

return oc.web.core.org_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"replies"));
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.replies.cljs$core$IFn$_invoke$arity$1(":org"),action__27254__auto___45601);

oc.web.core.replies_route = (function oc$web$core$replies_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45602 = arguments.length;
var i__4737__auto___45603 = (0);
while(true){
if((i__4737__auto___45603 < len__4736__auto___45602)){
args__4742__auto__.push((arguments[i__4737__auto___45603]));

var G__45604 = (i__4737__auto___45603 + (1));
i__4737__auto___45603 = G__45604;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.replies_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.replies_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.replies.cljs$core$IFn$_invoke$arity$1(":org"),args__27253__auto__);
}));

(oc.web.core.replies_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.replies_route.cljs$lang$applyTo = (function (seq45324){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45324));
}));


var action__27254__auto___45605 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45325 = params__27255__auto__;
var map__45325__$1 = (((((!((map__45325 == null))))?(((((map__45325.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45325.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45325):map__45325);
var params = map__45325__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,621,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing replies-slash-route",[oc.web.urls.replies.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join('')], null);
}),null)),null,-1390922750);

return oc.web.core.org_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"replies"));
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45327 = params__27255__auto__;
var map__45327__$1 = (((((!((map__45327 == null))))?(((((map__45327.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45327.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45327):map__45327);
var params = map__45327__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,621,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing replies-slash-route",[oc.web.urls.replies.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join('')], null);
}),null)),null,630994681);

return oc.web.core.org_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"replies"));
} else {
return null;
}
}
});
secretary.core.add_route_BANG_([oc.web.urls.replies.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join(''),action__27254__auto___45605);

oc.web.core.replies_slash_route = (function oc$web$core$replies_slash_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45606 = arguments.length;
var i__4737__auto___45607 = (0);
while(true){
if((i__4737__auto___45607 < len__4736__auto___45606)){
args__4742__auto__.push((arguments[i__4737__auto___45607]));

var G__45608 = (i__4737__auto___45607 + (1));
i__4737__auto___45607 = G__45608;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.replies_slash_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.replies_slash_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,[oc.web.urls.replies.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join(''),args__27253__auto__);
}));

(oc.web.core.replies_slash_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.replies_slash_route.cljs$lang$applyTo = (function (seq45329){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45329));
}));


var action__27254__auto___45609 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45330 = params__27255__auto__;
var map__45330__$1 = (((((!((map__45330 == null))))?(((((map__45330.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45330.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45330):map__45330);
var params = map__45330__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,625,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing topics-route",oc.web.urls.topics.cljs$core$IFn$_invoke$arity$1(":org")], null);
}),null)),null,1051395887);

return oc.web.core.org_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"topics"));
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45332 = params__27255__auto__;
var map__45332__$1 = (((((!((map__45332 == null))))?(((((map__45332.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45332.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45332):map__45332);
var params = map__45332__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,625,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing topics-route",oc.web.urls.topics.cljs$core$IFn$_invoke$arity$1(":org")], null);
}),null)),null,-401111019);

return oc.web.core.org_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"topics"));
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.topics.cljs$core$IFn$_invoke$arity$1(":org"),action__27254__auto___45609);

oc.web.core.topics_route = (function oc$web$core$topics_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45610 = arguments.length;
var i__4737__auto___45611 = (0);
while(true){
if((i__4737__auto___45611 < len__4736__auto___45610)){
args__4742__auto__.push((arguments[i__4737__auto___45611]));

var G__45612 = (i__4737__auto___45611 + (1));
i__4737__auto___45611 = G__45612;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.topics_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.topics_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.topics.cljs$core$IFn$_invoke$arity$1(":org"),args__27253__auto__);
}));

(oc.web.core.topics_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.topics_route.cljs$lang$applyTo = (function (seq45334){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45334));
}));


var action__27254__auto___45613 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45335 = params__27255__auto__;
var map__45335__$1 = (((((!((map__45335 == null))))?(((((map__45335.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45335.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45335):map__45335);
var params = map__45335__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,629,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing topics-slash-route",[oc.web.urls.topics.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join('')], null);
}),null)),null,-851363518);

return oc.web.core.org_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"topics"));
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45337 = params__27255__auto__;
var map__45337__$1 = (((((!((map__45337 == null))))?(((((map__45337.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45337.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45337):map__45337);
var params = map__45337__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,629,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing topics-slash-route",[oc.web.urls.topics.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join('')], null);
}),null)),null,573515739);

return oc.web.core.org_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(params,new cljs.core.Keyword(null,"board","board",-1907017633),"topics"));
} else {
return null;
}
}
});
secretary.core.add_route_BANG_([oc.web.urls.topics.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join(''),action__27254__auto___45613);

oc.web.core.topics_slash_route = (function oc$web$core$topics_slash_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45614 = arguments.length;
var i__4737__auto___45615 = (0);
while(true){
if((i__4737__auto___45615 < len__4736__auto___45614)){
args__4742__auto__.push((arguments[i__4737__auto___45615]));

var G__45616 = (i__4737__auto___45615 + (1));
i__4737__auto___45615 = G__45616;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.topics_slash_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.topics_slash_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,[oc.web.urls.topics.cljs$core$IFn$_invoke$arity$1(":org"),"/"].join(''),args__27253__auto__);
}));

(oc.web.core.topics_slash_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.topics_slash_route.cljs$lang$applyTo = (function (seq45339){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45339));
}));


var action__27254__auto___45617 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45340 = params__27255__auto__;
var map__45340__$1 = (((((!((map__45340 == null))))?(((((map__45340.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45340.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45340):map__45340);
var params = map__45340__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,633,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing secure-activity-route",oc.web.urls.secure_activity.cljs$core$IFn$_invoke$arity$2(":org",":secure-id")], null);
}),null)),null,-897237069);

return oc.web.core.secure_activity_handler(oc.web.components.secure_activity.secure_activity,"secure-activity",target_45412,params,true);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45342 = params__27255__auto__;
var map__45342__$1 = (((((!((map__45342 == null))))?(((((map__45342.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45342.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45342):map__45342);
var params = map__45342__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,633,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing secure-activity-route",oc.web.urls.secure_activity.cljs$core$IFn$_invoke$arity$2(":org",":secure-id")], null);
}),null)),null,-1344913171);

return oc.web.core.secure_activity_handler(oc.web.components.secure_activity.secure_activity,"secure-activity",target_45412,params,true);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.secure_activity.cljs$core$IFn$_invoke$arity$2(":org",":secure-id"),action__27254__auto___45617);

oc.web.core.secure_activity_route = (function oc$web$core$secure_activity_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45618 = arguments.length;
var i__4737__auto___45619 = (0);
while(true){
if((i__4737__auto___45619 < len__4736__auto___45618)){
args__4742__auto__.push((arguments[i__4737__auto___45619]));

var G__45620 = (i__4737__auto___45619 + (1));
i__4737__auto___45619 = G__45620;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.secure_activity_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.secure_activity_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.secure_activity.cljs$core$IFn$_invoke$arity$2(":org",":secure-id"),args__27253__auto__);
}));

(oc.web.core.secure_activity_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.secure_activity_route.cljs$lang$applyTo = (function (seq45344){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45344));
}));


var action__27254__auto___45621 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45345 = params__27255__auto__;
var map__45345__$1 = (((((!((map__45345 == null))))?(((((map__45345.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45345.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45345):map__45345);
var params = map__45345__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,637,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing secure-activity-slash-route",[oc.web.urls.secure_activity.cljs$core$IFn$_invoke$arity$2(":org",":secure-id"),"/"].join('')], null);
}),null)),null,2034995984);

return oc.web.core.secure_activity_handler(oc.web.components.secure_activity.secure_activity,"secure-activity",target_45412,params,true);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45347 = params__27255__auto__;
var map__45347__$1 = (((((!((map__45347 == null))))?(((((map__45347.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45347.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45347):map__45347);
var params = map__45347__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,637,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing secure-activity-slash-route",[oc.web.urls.secure_activity.cljs$core$IFn$_invoke$arity$2(":org",":secure-id"),"/"].join('')], null);
}),null)),null,316475269);

return oc.web.core.secure_activity_handler(oc.web.components.secure_activity.secure_activity,"secure-activity",target_45412,params,true);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_([oc.web.urls.secure_activity.cljs$core$IFn$_invoke$arity$2(":org",":secure-id"),"/"].join(''),action__27254__auto___45621);

oc.web.core.secure_activity_slash_route = (function oc$web$core$secure_activity_slash_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45622 = arguments.length;
var i__4737__auto___45623 = (0);
while(true){
if((i__4737__auto___45623 < len__4736__auto___45622)){
args__4742__auto__.push((arguments[i__4737__auto___45623]));

var G__45624 = (i__4737__auto___45623 + (1));
i__4737__auto___45623 = G__45624;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.secure_activity_slash_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.secure_activity_slash_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,[oc.web.urls.secure_activity.cljs$core$IFn$_invoke$arity$2(":org",":secure-id"),"/"].join(''),args__27253__auto__);
}));

(oc.web.core.secure_activity_slash_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.secure_activity_slash_route.cljs$lang$applyTo = (function (seq45349){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45349));
}));


var action__27254__auto___45625 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45350 = params__27255__auto__;
var map__45350__$1 = (((((!((map__45350 == null))))?(((((map__45350.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45350.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45350):map__45350);
var params = map__45350__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,641,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing board-route",oc.web.urls.board.cljs$core$IFn$_invoke$arity$2(":org",":board")], null);
}),null)),null,573965268);

return oc.web.core.board_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,params);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45352 = params__27255__auto__;
var map__45352__$1 = (((((!((map__45352 == null))))?(((((map__45352.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45352.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45352):map__45352);
var params = map__45352__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,641,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing board-route",oc.web.urls.board.cljs$core$IFn$_invoke$arity$2(":org",":board")], null);
}),null)),null,634185825);

return oc.web.core.board_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,params);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.board.cljs$core$IFn$_invoke$arity$2(":org",":board"),action__27254__auto___45625);

oc.web.core.board_route = (function oc$web$core$board_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45626 = arguments.length;
var i__4737__auto___45627 = (0);
while(true){
if((i__4737__auto___45627 < len__4736__auto___45626)){
args__4742__auto__.push((arguments[i__4737__auto___45627]));

var G__45628 = (i__4737__auto___45627 + (1));
i__4737__auto___45627 = G__45628;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.board_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.board_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.board.cljs$core$IFn$_invoke$arity$2(":org",":board"),args__27253__auto__);
}));

(oc.web.core.board_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.board_route.cljs$lang$applyTo = (function (seq45354){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45354));
}));


var action__27254__auto___45629 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45355 = params__27255__auto__;
var map__45355__$1 = (((((!((map__45355 == null))))?(((((map__45355.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45355.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45355):map__45355);
var params = map__45355__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,645,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing board-route-slash",[oc.web.urls.board.cljs$core$IFn$_invoke$arity$2(":org",":board"),"/"].join('')], null);
}),null)),null,-407089499);

return oc.web.core.board_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,params);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45357 = params__27255__auto__;
var map__45357__$1 = (((((!((map__45357 == null))))?(((((map__45357.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45357.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45357):map__45357);
var params = map__45357__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,645,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing board-route-slash",[oc.web.urls.board.cljs$core$IFn$_invoke$arity$2(":org",":board"),"/"].join('')], null);
}),null)),null,-1576190995);

return oc.web.core.board_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,params);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_([oc.web.urls.board.cljs$core$IFn$_invoke$arity$2(":org",":board"),"/"].join(''),action__27254__auto___45629);

oc.web.core.board_slash_route = (function oc$web$core$board_slash_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45630 = arguments.length;
var i__4737__auto___45631 = (0);
while(true){
if((i__4737__auto___45631 < len__4736__auto___45630)){
args__4742__auto__.push((arguments[i__4737__auto___45631]));

var G__45632 = (i__4737__auto___45631 + (1));
i__4737__auto___45631 = G__45632;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.board_slash_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.board_slash_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,[oc.web.urls.board.cljs$core$IFn$_invoke$arity$2(":org",":board"),"/"].join(''),args__27253__auto__);
}));

(oc.web.core.board_slash_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.board_slash_route.cljs$lang$applyTo = (function (seq45359){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45359));
}));


var action__27254__auto___45633 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45360 = params__27255__auto__;
var map__45360__$1 = (((((!((map__45360 == null))))?(((((map__45360.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45360.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45360):map__45360);
var params = map__45360__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,649,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing contributions-route",oc.web.urls.board.cljs$core$IFn$_invoke$arity$2(":org",":contributions")], null);
}),null)),null,-2045350212);

return oc.web.core.contributions_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,params);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45362 = params__27255__auto__;
var map__45362__$1 = (((((!((map__45362 == null))))?(((((map__45362.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45362.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45362):map__45362);
var params = map__45362__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,649,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing contributions-route",oc.web.urls.board.cljs$core$IFn$_invoke$arity$2(":org",":contributions")], null);
}),null)),null,1682117680);

return oc.web.core.contributions_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,params);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$2(":org",":contributions"),action__27254__auto___45633);

oc.web.core.contributions_route = (function oc$web$core$contributions_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45634 = arguments.length;
var i__4737__auto___45635 = (0);
while(true){
if((i__4737__auto___45635 < len__4736__auto___45634)){
args__4742__auto__.push((arguments[i__4737__auto___45635]));

var G__45636 = (i__4737__auto___45635 + (1));
i__4737__auto___45635 = G__45636;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.contributions_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.contributions_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$2(":org",":contributions"),args__27253__auto__);
}));

(oc.web.core.contributions_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.contributions_route.cljs$lang$applyTo = (function (seq45364){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45364));
}));


var action__27254__auto___45637 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45365 = params__27255__auto__;
var map__45365__$1 = (((((!((map__45365 == null))))?(((((map__45365.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45365.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45365):map__45365);
var params = map__45365__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,653,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing contributions-slash-route",[oc.web.urls.board.cljs$core$IFn$_invoke$arity$2(":org",":contributions"),"/"].join('')], null);
}),null)),null,-1979377013);

return oc.web.core.contributions_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,params);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45367 = params__27255__auto__;
var map__45367__$1 = (((((!((map__45367 == null))))?(((((map__45367.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45367.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45367):map__45367);
var params = map__45367__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,653,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing contributions-slash-route",[oc.web.urls.board.cljs$core$IFn$_invoke$arity$2(":org",":contributions"),"/"].join('')], null);
}),null)),null,528840229);

return oc.web.core.contributions_handler("dashboard",target_45412,oc.web.components.org_dashboard.org_dashboard,params);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_([oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$2(":org",":contributions"),"/"].join(''),action__27254__auto___45637);

oc.web.core.contributions_slash_route = (function oc$web$core$contributions_slash_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45638 = arguments.length;
var i__4737__auto___45639 = (0);
while(true){
if((i__4737__auto___45639 < len__4736__auto___45638)){
args__4742__auto__.push((arguments[i__4737__auto___45639]));

var G__45640 = (i__4737__auto___45639 + (1));
i__4737__auto___45639 = G__45640;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.contributions_slash_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.contributions_slash_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,[oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$2(":org",":contributions"),"/"].join(''),args__27253__auto__);
}));

(oc.web.core.contributions_slash_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.contributions_slash_route.cljs$lang$applyTo = (function (seq45369){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45369));
}));


var action__27254__auto___45641 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45370 = params__27255__auto__;
var map__45370__$1 = (((((!((map__45370 == null))))?(((((map__45370.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45370.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45370):map__45370);
var params = map__45370__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,657,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing entry-route",oc.web.urls.entry.cljs$core$IFn$_invoke$arity$3(":org",":entry-board",":entry")], null);
}),null)),null,-1202646027);

return oc.web.core.entry_handler(target_45412,params);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45372 = params__27255__auto__;
var map__45372__$1 = (((((!((map__45372 == null))))?(((((map__45372.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45372.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45372):map__45372);
var params = map__45372__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,657,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing entry-route",oc.web.urls.entry.cljs$core$IFn$_invoke$arity$3(":org",":entry-board",":entry")], null);
}),null)),null,2019084237);

return oc.web.core.entry_handler(target_45412,params);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.entry.cljs$core$IFn$_invoke$arity$3(":org",":entry-board",":entry"),action__27254__auto___45641);

oc.web.core.entry_route = (function oc$web$core$entry_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45642 = arguments.length;
var i__4737__auto___45643 = (0);
while(true){
if((i__4737__auto___45643 < len__4736__auto___45642)){
args__4742__auto__.push((arguments[i__4737__auto___45643]));

var G__45644 = (i__4737__auto___45643 + (1));
i__4737__auto___45643 = G__45644;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.entry_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.entry_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.entry.cljs$core$IFn$_invoke$arity$3(":org",":entry-board",":entry"),args__27253__auto__);
}));

(oc.web.core.entry_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.entry_route.cljs$lang$applyTo = (function (seq45374){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45374));
}));


var action__27254__auto___45645 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45375 = params__27255__auto__;
var map__45375__$1 = (((((!((map__45375 == null))))?(((((map__45375.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45375.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45375):map__45375);
var params = map__45375__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,661,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing entry-route",[oc.web.urls.entry.cljs$core$IFn$_invoke$arity$3(":org",":entry-board",":entry"),"/"].join('')], null);
}),null)),null,705466179);

return oc.web.core.entry_handler(target_45412,params);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45377 = params__27255__auto__;
var map__45377__$1 = (((((!((map__45377 == null))))?(((((map__45377.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45377.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45377):map__45377);
var params = map__45377__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,661,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing entry-route",[oc.web.urls.entry.cljs$core$IFn$_invoke$arity$3(":org",":entry-board",":entry"),"/"].join('')], null);
}),null)),null,-1498280891);

return oc.web.core.entry_handler(target_45412,params);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_([oc.web.urls.entry.cljs$core$IFn$_invoke$arity$3(":org",":entry-board",":entry"),"/"].join(''),action__27254__auto___45645);

oc.web.core.entry_slash_route = (function oc$web$core$entry_slash_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45646 = arguments.length;
var i__4737__auto___45647 = (0);
while(true){
if((i__4737__auto___45647 < len__4736__auto___45646)){
args__4742__auto__.push((arguments[i__4737__auto___45647]));

var G__45648 = (i__4737__auto___45647 + (1));
i__4737__auto___45647 = G__45648;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.entry_slash_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.entry_slash_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,[oc.web.urls.entry.cljs$core$IFn$_invoke$arity$3(":org",":entry-board",":entry"),"/"].join(''),args__27253__auto__);
}));

(oc.web.core.entry_slash_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.entry_slash_route.cljs$lang$applyTo = (function (seq45379){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45379));
}));


var action__27254__auto___45649 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45380 = params__27255__auto__;
var map__45380__$1 = (((((!((map__45380 == null))))?(((((map__45380.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45380.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45380):map__45380);
var params = map__45380__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,665,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing comment-route",oc.web.urls.comment_url.cljs$core$IFn$_invoke$arity$4(":org",":entry-board",":entry",":comment")], null);
}),null)),null,-1642834124);

return oc.web.core.entry_handler(target_45412,params);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45382 = params__27255__auto__;
var map__45382__$1 = (((((!((map__45382 == null))))?(((((map__45382.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45382.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45382):map__45382);
var params = map__45382__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,665,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing comment-route",oc.web.urls.comment_url.cljs$core$IFn$_invoke$arity$4(":org",":entry-board",":entry",":comment")], null);
}),null)),null,-525135107);

return oc.web.core.entry_handler(target_45412,params);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_(oc.web.urls.comment_url.cljs$core$IFn$_invoke$arity$4(":org",":entry-board",":entry",":comment"),action__27254__auto___45649);

oc.web.core.comment_route = (function oc$web$core$comment_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45650 = arguments.length;
var i__4737__auto___45651 = (0);
while(true){
if((i__4737__auto___45651 < len__4736__auto___45650)){
args__4742__auto__.push((arguments[i__4737__auto___45651]));

var G__45652 = (i__4737__auto___45651 + (1));
i__4737__auto___45651 = G__45652;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.comment_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.comment_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,oc.web.urls.comment_url.cljs$core$IFn$_invoke$arity$4(":org",":entry-board",":entry",":comment"),args__27253__auto__);
}));

(oc.web.core.comment_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.comment_route.cljs$lang$applyTo = (function (seq45384){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45384));
}));


var action__27254__auto___45653 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45385 = params__27255__auto__;
var map__45385__$1 = (((((!((map__45385 == null))))?(((((map__45385.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45385.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45385):map__45385);
var params = map__45385__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,669,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing comment-slash-route",[oc.web.urls.comment_url.cljs$core$IFn$_invoke$arity$4(":org",":entry-board",":entry",":comment"),"/"].join('')], null);
}),null)),null,1049316236);

return oc.web.core.entry_handler(target_45412,params);
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var map__45387 = params__27255__auto__;
var map__45387__$1 = (((((!((map__45387 == null))))?(((((map__45387.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45387.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45387):map__45387);
var params = map__45387__$1;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,669,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing comment-slash-route",[oc.web.urls.comment_url.cljs$core$IFn$_invoke$arity$4(":org",":entry-board",":entry",":comment"),"/"].join('')], null);
}),null)),null,-1789653925);

return oc.web.core.entry_handler(target_45412,params);
} else {
return null;
}
}
});
secretary.core.add_route_BANG_([oc.web.urls.comment_url.cljs$core$IFn$_invoke$arity$4(":org",":entry-board",":entry",":comment"),"/"].join(''),action__27254__auto___45653);

oc.web.core.comment_slash_route = (function oc$web$core$comment_slash_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45654 = arguments.length;
var i__4737__auto___45655 = (0);
while(true){
if((i__4737__auto___45655 < len__4736__auto___45654)){
args__4742__auto__.push((arguments[i__4737__auto___45655]));

var G__45656 = (i__4737__auto___45655 + (1));
i__4737__auto___45655 = G__45656;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.comment_slash_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.comment_slash_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,[oc.web.urls.comment_url.cljs$core$IFn$_invoke$arity$4(":org",":entry-board",":entry",":comment"),"/"].join(''),args__27253__auto__);
}));

(oc.web.core.comment_slash_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.comment_slash_route.cljs$lang$applyTo = (function (seq45389){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45389));
}));


var action__27254__auto___45657 = (function (params__27255__auto__){
if(cljs.core.map_QMARK_(params__27255__auto__)){
var map__45390 = params__27255__auto__;
var map__45390__$1 = (((((!((map__45390 == null))))?(((((map__45390.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45390.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45390):map__45390);
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,673,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing not-found-route","*"], null);
}),null)),null,973344001);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
return oc.web.router.redirect_404_BANG_();
} else {
return oc.web.router.redirect_BANG_([oc.web.urls.login_wall,"?login-redirect=",cljs.core.str.cljs$core$IFn$_invoke$arity$1(encodeURIComponent(oc.web.router.get_token()))].join(''));
}
} else {
if(cljs.core.vector_QMARK_(params__27255__auto__)){
var vec__45392 = params__27255__auto__;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.core",null,673,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Routing not-found-route","*"], null);
}),null)),null,-871621336);

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
return oc.web.router.redirect_404_BANG_();
} else {
return oc.web.router.redirect_BANG_([oc.web.urls.login_wall,"?login-redirect=",cljs.core.str.cljs$core$IFn$_invoke$arity$1(encodeURIComponent(oc.web.router.get_token()))].join(''));
}
} else {
return null;
}
}
});
secretary.core.add_route_BANG_("*",action__27254__auto___45657);

oc.web.core.not_found_route = (function oc$web$core$not_found_route(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45658 = arguments.length;
var i__4737__auto___45659 = (0);
while(true){
if((i__4737__auto___45659 < len__4736__auto___45658)){
args__4742__auto__.push((arguments[i__4737__auto___45659]));

var G__45660 = (i__4737__auto___45659 + (1));
i__4737__auto___45659 = G__45660;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.core.not_found_route.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.core.not_found_route.cljs$core$IFn$_invoke$arity$variadic = (function (args__27253__auto__){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(secretary.core.render_route_STAR_,"*",args__27253__auto__);
}));

(oc.web.core.not_found_route.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.core.not_found_route.cljs$lang$applyTo = (function (seq45395){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45395));
}));


oc.web.core.handle_url_change = (function oc$web$core$handle_url_change(e){
if(cljs.core.truth_(e.isNavigation)){
} else {
if(cljs.core.truth_(oc.shared.useragent.edge_QMARK_)){
(document.scrollingElement.scrollTop = oc.web.lib.utils.page_scroll_top());
} else {
window.scrollTo(oc.web.lib.utils.page_scroll_top(),(0));
}
}

secretary.core.dispatch_BANG_(oc.web.router.get_token());

return oc.web.lib.utils.after((100),(function (){
return oc.web.lib.utils.remove_tooltips();
}));
});
} else {
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"error","error",-978969032),"oc.web.core",null,694,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Error: div#app is not defined!"], null);
}),null)),null,293875209);

oc.web.lib.sentry.capture_message_BANG_("Error: div#app is not defined!");
}
oc.web.core.init = (function oc$web$core$init(){
oc.web.lib.logging.config_log_level_BANG_((function (){var or__4126__auto__ = oc.web.dispatcher.query_param.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"log-level","log-level",862121670));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.local_settings.log_level;
}
})());

oc.web.api.config_request((function (p1__45396_SHARP_){
return oc.web.actions.jwt.update_jwt(p1__45396_SHARP_);
}),(function (){
return oc.web.actions.jwt.logout.cljs$core$IFn$_invoke$arity$0();
}),(function (){
return oc.web.actions.notifications.show_notification(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(oc.web.lib.utils.network_error,new cljs.core.Keyword(null,"expire","expire",-70657108),(5)));
}));

oc.web.actions.jwt.dispatch_jwt();

oc.web.actions.jwt.dispatch_id_token();

oc.web.actions.user.recall_expo_push_token();

if(oc.shared.useragent.mobile_app_QMARK_){
oc.web.expo.bridge_get_deep_link_origin();

oc.web.expo.bridge_get_app_version();
} else {
}

oc.web.actions.activity.ws_change_subscribe();

oc.web.actions.section.ws_change_subscribe();

oc.web.actions.contributions.subscribe();

oc.web.actions.org.subscribe();

oc.web.actions.reaction.subscribe();

oc.web.actions.comment.subscribe();

oc.web.actions.user.subscribe();

oc.web.actions.web_app_update.start_web_app_update_check_BANG_();

$(window).click((function (){
return oc.web.lib.utils.remove_tooltips();
}));

if(cljs.core.truth_(oc.web.core.handle_url_change)){
return oc.web.router.setup_navigation_BANG_(oc.web.core.handle_url_change);
} else {
return null;
}
});
oc.web.core.on_js_reload = (function oc$web$core$on_js_reload(){
console.clear();

return secretary.core.dispatch_BANG_(oc.web.router.get_token());
});

//# sourceMappingURL=oc.web.core.js.map
