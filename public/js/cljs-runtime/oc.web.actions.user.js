goog.provide('oc.web.actions.user');
var module$node_modules$moment_timezone$index=shadow.js.require("module$node_modules$moment_timezone$index", {});
oc.web.actions.user.dismiss_invite_box = (function oc$web$actions$user$dismiss_invite_box(var_args){
var G__43285 = arguments.length;
switch (G__43285) {
case 0:
return oc.web.actions.user.dismiss_invite_box.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.actions.user.dismiss_invite_box.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.actions.user.dismiss_invite_box.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.user.dismiss_invite_box.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.actions.user.dismiss_invite_box.cljs$core$IFn$_invoke$arity$2(oc.web.lib.jwt.user_id(),false);
}));

(oc.web.actions.user.dismiss_invite_box.cljs$core$IFn$_invoke$arity$1 = (function (user_id){
return oc.web.actions.user.dismiss_invite_box.cljs$core$IFn$_invoke$arity$2(user_id,false);
}));

(oc.web.actions.user.dismiss_invite_box.cljs$core$IFn$_invoke$arity$2 = (function (user_id,remove_now_QMARK_){
oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$1(oc.web.router.show_invite_box_cookie(user_id));

if(cljs.core.truth_(remove_now_QMARK_)){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [oc.web.dispatcher.show_invite_box_key], null),false], null));
} else {
return null;
}
}));

(oc.web.actions.user.dismiss_invite_box.cljs$lang$maxFixedArity = 2);

/**
 * Check if one of the following is present and redirect to the proper wall if needed:
 *   :password-required redirect to password collect
 *   :name-required redirect to first and last name collect
 * 
 *   Use the orgs value to determine if the user has already at least one org set
 */
oc.web.actions.user.check_user_walls = (function oc$web$actions$user$check_user_walls(var_args){
var G__43291 = arguments.length;
switch (G__43291) {
case 0:
return oc.web.actions.user.check_user_walls.cljs$core$IFn$_invoke$arity$0();

break;
case 2:
return oc.web.actions.user.check_user_walls.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.user.check_user_walls.cljs$core$IFn$_invoke$arity$0 = (function (){
if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
return oc.web.lib.utils.after((100),(function (){
if(((oc.web.stores.user.orgs_QMARK_()) && (oc.web.stores.user.auth_settings_QMARK_()) && (oc.web.stores.user.auth_settings_status_QMARK_()))){
return oc.web.actions.user.check_user_walls.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.auth_settings.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.orgs_data.cljs$core$IFn$_invoke$arity$0());
} else {
return null;
}
}));
} else {
return null;
}
}));

(oc.web.actions.user.check_user_walls.cljs$core$IFn$_invoke$arity$2 = (function (auth_settings,orgs){
var status_response = cljs.core.set(cljs.core.map.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword,new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(auth_settings)));
var has_orgs = (cljs.core.count(orgs) > (0));
if(cljs.core.truth_((status_response.cljs$core$IFn$_invoke$arity$1 ? status_response.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"password-required","password-required",-340780220)) : status_response.call(null,new cljs.core.Keyword(null,"password-required","password-required",-340780220))))){
return oc.web.router.nav_BANG_(oc.web.urls.confirm_invitation_password);
} else {
if(cljs.core.truth_((status_response.cljs$core$IFn$_invoke$arity$1 ? status_response.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"name-required","name-required",1319996199)) : status_response.call(null,new cljs.core.Keyword(null,"name-required","name-required",1319996199))))){
if(has_orgs){
return oc.web.router.nav_BANG_(oc.web.urls.confirm_invitation_profile);
} else {
return oc.web.router.nav_BANG_(oc.web.urls.sign_up_profile);
}
} else {
if(has_orgs){
return null;
} else {
return oc.web.router.nav_BANG_(oc.web.urls.sign_up_profile);
}

}
}
}));

(oc.web.actions.user.check_user_walls.cljs$lang$maxFixedArity = 2);

oc.web.actions.user.entry_point_get_finished = (function oc$web$actions$user$entry_point_get_finished(var_args){
var G__43293 = arguments.length;
switch (G__43293) {
case 2:
return oc.web.actions.user.entry_point_get_finished.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.actions.user.entry_point_get_finished.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.user.entry_point_get_finished.cljs$core$IFn$_invoke$arity$2 = (function (success,body){
return oc.web.actions.user.entry_point_get_finished.cljs$core$IFn$_invoke$arity$3(success,body,null);
}));

(oc.web.actions.user.entry_point_get_finished.cljs$core$IFn$_invoke$arity$3 = (function (success,body,callback){
var collection = new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(body);
if(cljs.core.truth_(success)){
var orgs = new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(collection);
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"entry-point","entry-point",794189075),orgs,collection], null));

oc.web.actions.user.check_user_walls.cljs$core$IFn$_invoke$arity$0();

if(cljs.core.fn_QMARK_(callback)){
return (callback.cljs$core$IFn$_invoke$arity$2 ? callback.cljs$core$IFn$_invoke$arity$2(orgs,collection) : callback.call(null,orgs,collection));
} else {
return null;
}
} else {
return oc.web.actions.notifications.show_notification(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(oc.web.lib.utils.network_error,new cljs.core.Keyword(null,"expire","expire",-70657108),(0)));
}
}));

(oc.web.actions.user.entry_point_get_finished.cljs$lang$maxFixedArity = 3);

oc.web.actions.user.entry_point_get = (function oc$web$actions$user$entry_point_get(var_args){
var args__4742__auto__ = [];
var len__4736__auto___43458 = arguments.length;
var i__4737__auto___43459 = (0);
while(true){
if((i__4737__auto___43459 < len__4736__auto___43458)){
args__4742__auto__.push((arguments[i__4737__auto___43459]));

var G__43460 = (i__4737__auto___43459 + (1));
i__4737__auto___43459 = G__43460;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.actions.user.entry_point_get.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.actions.user.entry_point_get.cljs$core$IFn$_invoke$arity$variadic = (function (org_slug,p__43297){
var vec__43298 = p__43297;
var force_refresh_QMARK_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43298,(0),null);
return oc.web.api.get_entry_point(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),(function (success,body){
return oc.web.actions.user.entry_point_get_finished.cljs$core$IFn$_invoke$arity$3(success,body,(function (orgs,collection){
if(cljs.core.truth_(org_slug)){
var temp__5733__auto__ = cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__43294_SHARP_){
return ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(p1__43294_SHARP_),org_slug)) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__43294_SHARP_),org_slug)));
}),orgs));
if(cljs.core.truth_(temp__5733__auto__)){
var org_data = temp__5733__auto__;
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(org_data),org_slug)){
return oc.web.router.rewrite_org_uuid_as_slug(org_slug,new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data));
} else {
return oc.web.actions.org.get_org.cljs$core$IFn$_invoke$arity$2(org_data,cljs.core.not(force_refresh_QMARK_));
}
} else {
if(cljs.core.truth_(oc.web.dispatcher.current_secure_activity_id.cljs$core$IFn$_invoke$arity$0())){
return oc.web.actions.activity.secure_activity_get.cljs$core$IFn$_invoke$arity$1((function (){
return oc.web.utils.activity.get_comments_if_needed.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.secure_activity_data.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.comments_data.cljs$core$IFn$_invoke$arity$0());
}));
} else {
if((cljs.core.count(orgs) > (0))){
oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$3(oc.web.router.last_org_cookie(),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(cljs.core.first(orgs)),oc.web.lib.cookies.default_cookie_expire);
} else {
oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$1(oc.web.router.last_org_cookie());
}

if(cljs.core.truth_(oc.web.dispatcher.current_secure_activity_id.cljs$core$IFn$_invoke$arity$0())){
return null;
} else {
return oc.web.actions.routing.maybe_404.cljs$core$IFn$_invoke$arity$0();
}
}
}
} else {
if(cljs.core.truth_((function (){var and__4115__auto__ = oc.web.lib.jwt.jwt();
if(cljs.core.truth_(and__4115__auto__)){
var and__4115__auto____$1 = oc.web.dispatcher.in_route_QMARK_.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"login","login",55217519));
if(cljs.core.truth_(and__4115__auto____$1)){
return (cljs.core.count(orgs) > (0));
} else {
return and__4115__auto____$1;
}
} else {
return and__4115__auto__;
}
})())){
return oc.web.router.nav_BANG_(oc.web.urls.org.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(cljs.core.first(orgs))));
} else {
return null;
}
}
}));
}));
}));

(oc.web.actions.user.entry_point_get.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.actions.user.entry_point_get.cljs$lang$applyTo = (function (seq43295){
var G__43296 = cljs.core.first(seq43295);
var seq43295__$1 = cljs.core.next(seq43295);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__43296,seq43295__$1);
}));

oc.web.actions.user.save_login_redirect = (function oc$web$actions$user$save_login_redirect(var_args){
var args__4742__auto__ = [];
var len__4736__auto___43461 = arguments.length;
var i__4737__auto___43462 = (0);
while(true){
if((i__4737__auto___43462 < len__4736__auto___43461)){
args__4742__auto__.push((arguments[i__4737__auto___43462]));

var G__43463 = (i__4737__auto___43462 + (1));
i__4737__auto___43462 = G__43463;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.actions.user.save_login_redirect.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.actions.user.save_login_redirect.cljs$core$IFn$_invoke$arity$variadic = (function (p__43307){
var vec__43309 = p__43307;
var url = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43309,(0),null);
var url__$1 = (function (){var or__4126__auto__ = url;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return window.location.href;
}
})();
if(cljs.core.truth_(url__$1)){
return oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$2(oc.web.router.login_redirect_cookie,url__$1);
} else {
return null;
}
}));

(oc.web.actions.user.save_login_redirect.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.actions.user.save_login_redirect.cljs$lang$applyTo = (function (seq43304){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq43304));
}));

oc.web.actions.user.maybe_save_login_redirect = (function oc$web$actions$user$maybe_save_login_redirect(){
var url_pathname = window.location.pathname;
var is_login_route_QMARK_ = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(url_pathname,oc.web.urls.login_wall)) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(url_pathname,oc.web.urls.login)) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(url_pathname,oc.web.urls.native_login)));
if(cljs.core.truth_(((is_login_route_QMARK_)?new cljs.core.Keyword(null,"login-redirect","login-redirect",-2132232884).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"query-params","query-params",900640534).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state))):false))){
return oc.web.actions.user.save_login_redirect.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"login-redirect","login-redirect",-2132232884).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"query-params","query-params",900640534).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state)))], 0));
} else {
if((!(is_login_route_QMARK_))){
return oc.web.actions.user.save_login_redirect();
} else {
return null;
}
}
});
oc.web.actions.user.newest_org = (function oc$web$actions$user$newest_org(orgs){
return cljs.core.first(cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"created-at","created-at",-89248644),orgs));
});
oc.web.actions.user.get_default_org = (function oc$web$actions$user$get_default_org(orgs){
var temp__5733__auto__ = oc.web.lib.cookies.get_cookie(oc.web.router.last_org_cookie());
if(cljs.core.truth_(temp__5733__auto__)){
var last_org_slug = temp__5733__auto__;
var last_org = cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__43312_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(p1__43312_SHARP_),last_org_slug);
}),orgs));
var or__4126__auto__ = last_org;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.actions.user.newest_org(orgs);
}
} else {
return oc.web.actions.user.newest_org(orgs);
}
});
oc.web.actions.user.login_redirect = (function oc$web$actions$user$login_redirect(){
var redirect_url = oc.web.lib.cookies.get_cookie(oc.web.router.login_redirect_cookie);
var orgs = oc.web.dispatcher.orgs_data.cljs$core$IFn$_invoke$arity$0();
oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$1(oc.web.router.login_redirect_cookie);

if(cljs.core.truth_(redirect_url)){
return oc.web.router.redirect_BANG_(redirect_url);
} else {
return oc.web.router.nav_BANG_((((cljs.core.count(orgs) === (0)))?oc.web.urls.sign_up_profile:oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(oc.web.actions.user.get_default_org(orgs)))));
}
});
oc.web.actions.user.lander_check_team_redirect = (function oc$web$actions$user$lander_check_team_redirect(){
return oc.web.lib.utils.after((100),(function (){
return oc.web.api.get_entry_point(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),(function (success,body){
return oc.web.actions.user.entry_point_get_finished.cljs$core$IFn$_invoke$arity$3(success,body,oc.web.actions.user.login_redirect);
}));
}));
});
oc.web.actions.user.login_with_email_finish = (function oc$web$actions$user$login_with_email_finish(user_email,success,body,status){
if(cljs.core.truth_(success)){
if(cljs.core.empty_QMARK_(body)){
oc.web.lib.utils.after((10),(function (){
return oc.web.router.nav_BANG_([oc.web.urls.email_wall,"?e=",cljs.core.str.cljs$core$IFn$_invoke$arity$1(user_email)].join(''));
}));
} else {
oc.web.actions.jwt.update_jwt_cookie(body);

oc.web.actions.user.lander_check_team_redirect();
}

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("login-with-email","success","login-with-email/success",-80639303),body], null));
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,(401))){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("login-with-email","failed","login-with-email/failed",1547028965),(401)], null));
} else {
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("login-with-email","failed","login-with-email/failed",1547028965),(500)], null));

}
}
});
oc.web.actions.user.login_with_email = (function oc$web$actions$user$login_with_email(email,pswd){
var email_links = new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.auth_settings.cljs$core$IFn$_invoke$arity$0());
var auth_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([email_links,"authenticate","GET",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"auth-source","auth-source",1912135250),"email"], null)], 0));
oc.web.api.auth_with_email(auth_link,email,pswd,cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.actions.user.login_with_email_finish,email));

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"login-with-email","login-with-email",-1597480700)], null));
});
oc.web.actions.user.login_with_slack = (function oc$web$actions$user$login_with_slack(var_args){
var args__4742__auto__ = [];
var len__4736__auto___43464 = arguments.length;
var i__4737__auto___43465 = (0);
while(true){
if((i__4737__auto___43465 < len__4736__auto___43464)){
args__4742__auto__.push((arguments[i__4737__auto___43465]));

var G__43466 = (i__4737__auto___43465 + (1));
i__4737__auto___43465 = G__43466;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.actions.user.login_with_slack.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.actions.user.login_with_slack.cljs$core$IFn$_invoke$arity$variadic = (function (auth_url,p__43341){
var vec__43342 = p__43341;
var state_map = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43342,(0),null);
var auth_url_with_redirect = oc.web.utils.user.auth_link_with_state(new cljs.core.Keyword(null,"href","href",-793805698).cljs$core$IFn$_invoke$arity$1(auth_url),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"team-id","team-id",-14505725),"open-company-auth",new cljs.core.Keyword(null,"redirect","redirect",-1975673286),oc.web.urls.slack_lander_check], null),state_map], 0)));
oc.web.router.redirect_BANG_(auth_url_with_redirect);

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"login-with-slack","login-with-slack",1915911677)], null));
}));

(oc.web.actions.user.login_with_slack.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.actions.user.login_with_slack.cljs$lang$applyTo = (function (seq43339){
var G__43340 = cljs.core.first(seq43339);
var seq43339__$1 = cljs.core.next(seq43339);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__43340,seq43339__$1);
}));

oc.web.actions.user.login_with_google = (function oc$web$actions$user$login_with_google(var_args){
var args__4742__auto__ = [];
var len__4736__auto___43467 = arguments.length;
var i__4737__auto___43468 = (0);
while(true){
if((i__4737__auto___43468 < len__4736__auto___43467)){
args__4742__auto__.push((arguments[i__4737__auto___43468]));

var G__43469 = (i__4737__auto___43468 + (1));
i__4737__auto___43468 = G__43469;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.actions.user.login_with_google.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.actions.user.login_with_google.cljs$core$IFn$_invoke$arity$variadic = (function (auth_url,p__43347){
var vec__43348 = p__43347;
var state_map = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43348,(0),null);
var auth_url_with_redirect = oc.web.utils.user.auth_link_with_state(new cljs.core.Keyword(null,"href","href",-793805698).cljs$core$IFn$_invoke$arity$1(auth_url),(function (){var or__4126__auto__ = state_map;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.PersistentArrayMap.EMPTY;
}
})());
oc.web.router.redirect_BANG_(auth_url_with_redirect);

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"login-with-google","login-with-google",-360269339)], null));
}));

(oc.web.actions.user.login_with_google.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.actions.user.login_with_google.cljs$lang$applyTo = (function (seq43345){
var G__43346 = cljs.core.first(seq43345);
var seq43345__$1 = cljs.core.next(seq43345);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__43346,seq43345__$1);
}));

oc.web.actions.user.refresh_slack_user = (function oc$web$actions$user$refresh_slack_user(){
var refresh_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.auth_settings.cljs$core$IFn$_invoke$arity$0()),"refresh"], 0));
return oc.web.api.refresh_slack_user(refresh_link,(function (status,body,success){
if(cljs.core.truth_(success)){
return oc.web.actions.jwt.update_jwt(body);
} else {
return oc.web.router.redirect_BANG_(oc.web.urls.logout);
}
}));
});
oc.web.actions.user.show_login = (function oc$web$actions$user$show_login(login_type){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"login-overlay-show","login-overlay-show",-1173520092),login_type], null));
});
oc.web.actions.user.patch_timezone_if_needed = (function oc$web$actions$user$patch_timezone_if_needed(user_map){
var temp__33774__auto__ = clojure.string.blank_QMARK_(new cljs.core.Keyword(null,"timezone","timezone",1831928099).cljs$core$IFn$_invoke$arity$1(user_map));
if(temp__33774__auto__){
var _notz = temp__33774__auto__;
var temp__33774__auto____$1 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(user_map),"partial-update","PATCH"], 0));
if(cljs.core.truth_(temp__33774__auto____$1)){
var user_profile_link = temp__33774__auto____$1;
var temp__33774__auto____$2 = module$node_modules$moment_timezone$index.tz.guess();
if(cljs.core.truth_(temp__33774__auto____$2)){
var guessed_timezone = temp__33774__auto____$2;
return oc.web.api.patch_user(user_profile_link,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"timezone","timezone",1831928099),guessed_timezone], null),(function (status,body,success){
if(cljs.core.truth_(success)){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"user-data","user-data",2143823568),oc.web.lib.json.json__GT_cljs(body)], null));
} else {
return null;
}
}));
} else {
return null;
}
} else {
return null;
}
} else {
return null;
}
});
oc.web.actions.user.get_user = (function oc$web$actions$user$get_user(user_link){
var temp__5735__auto__ = (function (){var or__4126__auto__ = user_link;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.auth_settings.cljs$core$IFn$_invoke$arity$0()),"user","GET"], 0));
}
})();
if(cljs.core.truth_(temp__5735__auto__)){
var fixed_user_link = temp__5735__auto__;
return oc.web.api.get_user(fixed_user_link,(function (success,data){
var user_map = (cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(data):null);
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"user-data","user-data",2143823568),user_map], null));

oc.web.lib.utils.after((100),oc.web.actions.nux.check_nux);

return oc.web.actions.user.patch_timezone_if_needed(user_map);
}));
} else {
return null;
}
});
/**
 * Entry point call for auth service.
 */
oc.web.actions.user.auth_settings_get = (function oc$web$actions$user$auth_settings_get(){
return oc.web.api.get_auth_settings((function (body,status){
if(cljs.core.truth_(body)){
var temp__5735__auto___43470 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(body),"user","GET"], 0));
if(cljs.core.truth_(temp__5735__auto___43470)){
var user_link_43471 = temp__5735__auto___43470;
oc.web.actions.user.get_user(user_link_43471);
} else {
}

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"auth-settings","auth-settings",-1775088219),body], null));

oc.web.actions.user.check_user_walls.cljs$core$IFn$_invoke$arity$0();

return oc.web.actions.team.teams_get();
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,(401))){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"auth-settings","auth-settings",-1775088219),status], null));
} else {
return null;
}
}
}));
});
oc.web.actions.user.auth_with_token_failed = (function oc$web$actions$user$auth_with_token_failed(error){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("auth-with-token","failed","auth-with-token/failed",55615787),error], null));
});
oc.web.actions.user.invitation_confirmed = (function oc$web$actions$user$invitation_confirmed(status,body,success){
if(cljs.core.truth_(success)){
oc.web.actions.jwt.update_jwt(body);

if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,(201))){
oc.web.actions.nux.new_user_registered("email");

oc.web.api.get_entry_point(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.actions.user.entry_point_get_finished);

oc.web.actions.user.auth_settings_get();
} else {
}

oc.web.router.nav_BANG_(oc.web.urls.confirm_invitation_password);
} else {
}

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"invitation-confirmed","invitation-confirmed",-298226477),success], null));
});
oc.web.actions.user.confirm_invitation = (function oc$web$actions$user$confirm_invitation(token){
var auth_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.auth_settings.cljs$core$IFn$_invoke$arity$0()),"authenticate","GET",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"auth-source","auth-source",1912135250),"email"], null)], 0));
return oc.web.api.confirm_invitation(auth_link,token,oc.web.actions.user.invitation_confirmed);
});
oc.web.actions.user.auth_with_token_success = (function oc$web$actions$user$auth_with_token_success(token_type,jwt){
oc.web.api.get_auth_settings((function (auth_body){
return oc.web.api.get_entry_point(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),(function (success,body){
oc.web.actions.user.entry_point_get_finished.cljs$core$IFn$_invoke$arity$2(success,body);

var orgs = new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(body));
var to_org = oc.web.actions.user.get_default_org(orgs);
return oc.web.router.redirect_BANG_((cljs.core.truth_(to_org)?oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(to_org)):oc.web.urls.sign_up_profile));
}));
}));

if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(token_type,new cljs.core.Keyword(null,"password-reset","password-reset",1971592302))){
oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"show-login-overlay","show-login-overlay",1026669411),"collect-password");
} else {
}

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("auth-with-token","success","auth-with-token/success",1511390719),jwt], null));
});
oc.web.actions.user.auth_with_token_callback = (function oc$web$actions$user$auth_with_token_callback(token_type,success,body,status){
if(cljs.core.truth_(success)){
oc.web.actions.jwt.update_jwt(body);

if(((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(token_type,new cljs.core.Keyword(null,"password-reset","password-reset",1971592302))) && (cljs.core.empty_QMARK_(oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"name","name",1843675177)))))){
oc.web.actions.nux.new_user_registered("email");
} else {
}

return oc.web.actions.user.auth_with_token_success(token_type,body);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,(401))){
return oc.web.actions.user.auth_with_token_failed((401));
} else {
return oc.web.actions.user.auth_with_token_failed((500));

}
}
});
oc.web.actions.user.auth_with_token = (function oc$web$actions$user$auth_with_token(token_type){
var token_links = new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.auth_settings.cljs$core$IFn$_invoke$arity$0());
var auth_url = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([token_links,"authenticate","GET",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"auth-source","auth-source",1912135250),"email"], null)], 0));
var token = oc.web.dispatcher.query_param.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"token","token",-1211463215));
oc.web.api.auth_with_token(auth_url,token,cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.actions.user.auth_with_token_callback,token_type));

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"auth-with-token","auth-with-token",1887574080),token_type], null));
});
oc.web.actions.user.signup_with_email_failed = (function oc$web$actions$user$signup_with_email_failed(status){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("signup-with-email","failed","signup-with-email/failed",-421316360),status], null));
});
oc.web.actions.user.signup_with_email_success = (function oc$web$actions$user$signup_with_email_success(user_email,team_token_signup_QMARK_,status,jwt){
var signup_redirect = (cljs.core.truth_(team_token_signup_QMARK_)?oc.web.urls.confirm_invitation_profile:oc.web.urls.sign_up_profile);
var current_org_slug = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,(204))){
return oc.web.lib.utils.after((10),(function (){
return oc.web.router.nav_BANG_([oc.web.urls.email_wall,"?e=",cljs.core.str.cljs$core$IFn$_invoke$arity$1(user_email)].join(''));
}));
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,(200))){
if(((((cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"first-name","first-name",-1559982131).cljs$core$IFn$_invoke$arity$1(jwt))) && (cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"last-name","last-name",-1695738974).cljs$core$IFn$_invoke$arity$1(jwt))))) || (cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103).cljs$core$IFn$_invoke$arity$1(jwt))))){
oc.web.lib.utils.after((200),(function (){
return oc.web.router.nav_BANG_(signup_redirect);
}));

return oc.web.api.get_entry_point(current_org_slug,oc.web.actions.user.entry_point_get_finished);
} else {
return oc.web.api.get_entry_point(current_org_slug,(function (success,body){
return oc.web.actions.user.entry_point_get_finished.cljs$core$IFn$_invoke$arity$3(success,body,(function (orgs,collection){
if((cljs.core.count(orgs) > (0))){
return oc.web.router.nav_BANG_(oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(oc.web.actions.user.get_default_org(orgs))));
} else {
return null;
}
}));
}));
}
} else {
oc.web.actions.jwt.update_jwt_cookie(jwt);

oc.web.actions.nux.new_user_registered("email");

oc.web.lib.utils.after((200),(function (){
return oc.web.router.nav_BANG_(signup_redirect);
}));

oc.web.api.get_entry_point(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.actions.user.entry_point_get_finished);

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("signup-with-email","success","signup-with-email/success",779597292)], null));

}
}
});
oc.web.actions.user.signup_with_email_callback = (function oc$web$actions$user$signup_with_email_callback(user_email,team_token_signup_QMARK_,success,body,status){
if(cljs.core.truth_(success)){
return oc.web.actions.user.signup_with_email_success(user_email,team_token_signup_QMARK_,status,body);
} else {
return oc.web.actions.user.signup_with_email_failed(status);
}
});
oc.web.actions.user.signup_with_email = (function oc$web$actions$user$signup_with_email(var_args){
var args__4742__auto__ = [];
var len__4736__auto___43472 = arguments.length;
var i__4737__auto___43473 = (0);
while(true){
if((i__4737__auto___43473 < len__4736__auto___43472)){
args__4742__auto__.push((arguments[i__4737__auto___43473]));

var G__43474 = (i__4737__auto___43473 + (1));
i__4737__auto___43473 = G__43474;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.actions.user.signup_with_email.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.actions.user.signup_with_email.cljs$core$IFn$_invoke$arity$variadic = (function (signup_data,p__43365){
var vec__43366 = p__43365;
var team_token_signup_QMARK_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43366,(0),null);
var email_links = new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.auth_settings.cljs$core$IFn$_invoke$arity$0());
var auth_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([email_links,"create","POST",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"auth-source","auth-source",1912135250),"email"], null)], 0));
oc.web.api.signup_with_email(auth_link,(function (){var or__4126__auto__ = new cljs.core.Keyword(null,"firstname","firstname",1659984849).cljs$core$IFn$_invoke$arity$1(signup_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "";
}
})(),(function (){var or__4126__auto__ = new cljs.core.Keyword(null,"lastname","lastname",-265181465).cljs$core$IFn$_invoke$arity$1(signup_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "";
}
})(),new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(signup_data),new cljs.core.Keyword(null,"pswd","pswd",278786885).cljs$core$IFn$_invoke$arity$1(signup_data),module$node_modules$moment_timezone$index.tz.guess(),cljs.core.partial.cljs$core$IFn$_invoke$arity$3(oc.web.actions.user.signup_with_email_callback,new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(signup_data),team_token_signup_QMARK_));

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"signup-with-email","signup-with-email",-22609037)], null));
}));

(oc.web.actions.user.signup_with_email.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.actions.user.signup_with_email.cljs$lang$applyTo = (function (seq43363){
var G__43364 = cljs.core.first(seq43363);
var seq43363__$1 = cljs.core.next(seq43363);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__43364,seq43363__$1);
}));

oc.web.actions.user.signup_with_email_reset_errors = (function oc$web$actions$user$signup_with_email_reset_errors(){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"signup-with-email","signup-with-email",-22609037)], null),cljs.core.PersistentArrayMap.EMPTY], null));
});
oc.web.actions.user.pswd_collect = (function oc$web$actions$user$pswd_collect(form_data,password_reset_QMARK_){
var update_link_43475 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state))),"partial-update","PATCH"], 0));
oc.web.api.collect_password(update_link_43475,new cljs.core.Keyword(null,"pswd","pswd",278786885).cljs$core$IFn$_invoke$arity$1(form_data),(function (status,body,success){
if(cljs.core.truth_(success)){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"user-data","user-data",2143823568),oc.web.lib.json.json__GT_cljs(body)], null));
} else {
}

if((((status >= (200))) && ((status <= (299))))){
if(cljs.core.truth_(password_reset_QMARK_)){
oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"show-login-overlay","show-login-overlay",1026669411));

oc.web.lib.utils.after((200),(function (){
return oc.web.router.nav_BANG_(oc.web.urls.login);
}));
} else {
oc.web.actions.nux.new_user_registered("email");

oc.web.router.nav_BANG_(oc.web.urls.confirm_invitation_profile);
}
} else {
}

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("pswd-collect","finish","pswd-collect/finish",-1819836763),status], null));
}));

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"pswd-collect","pswd-collect",-692397310),password_reset_QMARK_], null));
});
oc.web.actions.user.password_reset = (function oc$web$actions$user$password_reset(email){
var reset_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.auth_settings.cljs$core$IFn$_invoke$arity$0()),"reset"], 0));
oc.web.api.password_reset(reset_link,email,(function (p1__43369_SHARP_){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("password-reset","finish","password-reset/finish",-57929867),p1__43369_SHARP_], null));
}));

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"password-reset","password-reset",1971592302)], null));
});
oc.web.actions.user.clean_user_data = (function oc$web$actions$user$clean_user_data(current_user_data,edit_user_profile){
var new_password = new cljs.core.Keyword(null,"password","password",417022471).cljs$core$IFn$_invoke$arity$1(edit_user_profile);
var password_did_change = (cljs.core.count(new_password) > (0));
var with_pswd = ((((password_did_change) && ((cljs.core.count(new_password) >= (8)))))?edit_user_profile:cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(edit_user_profile,new cljs.core.Keyword(null,"password","password",417022471)));
var new_email = new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(edit_user_profile);
var email_did_change = cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new_email,new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(current_user_data));
var with_email = (cljs.core.truth_(((email_did_change)?oc.web.lib.utils.valid_email_QMARK_(new_email):false))?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(with_pswd,new cljs.core.Keyword(null,"email","email",1415816706),new_email):cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(with_pswd,new cljs.core.Keyword(null,"email","email",1415816706),new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(current_user_data)));
var timezone = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"timezone","timezone",1831928099).cljs$core$IFn$_invoke$arity$1(edit_user_profile);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = new cljs.core.Keyword(null,"timezone","timezone",1831928099).cljs$core$IFn$_invoke$arity$1(current_user_data);
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
return module$node_modules$moment_timezone$index.tz.guess();
}
}
})();
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(with_email,new cljs.core.Keyword(null,"timezone","timezone",1831928099),timezone);
});
oc.web.actions.user.user_profile_patch = (function oc$web$actions$user$user_profile_patch(user_data,user_profile_link,patch_cb){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"user-profile-save","user-profile-save",1054717798)], null));

return oc.web.api.patch_user(user_profile_link,user_data,(function (status,body,success){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,(422))){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("user-profile-update","failed","user-profile-update/failed",148620531)], null));
} else {
var resp = (cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):null);
if(cljs.core.fn_QMARK_(patch_cb)){
(patch_cb.cljs$core$IFn$_invoke$arity$2 ? patch_cb.cljs$core$IFn$_invoke$arity$2(success,resp) : patch_cb.call(null,success,resp));
} else {
}

if(cljs.core.truth_(success)){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"user-data","user-data",2143823568),resp], null));
} else {
return null;
}
}
}));
});
oc.web.actions.user.user_profile_save = (function oc$web$actions$user$user_profile_save(var_args){
var G__43392 = arguments.length;
switch (G__43392) {
case 2:
return oc.web.actions.user.user_profile_save.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.actions.user.user_profile_save.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.user.user_profile_save.cljs$core$IFn$_invoke$arity$2 = (function (current_user_data,edit_data){
return oc.web.actions.user.user_profile_save.cljs$core$IFn$_invoke$arity$3(current_user_data,edit_data,null);
}));

(oc.web.actions.user.user_profile_save.cljs$core$IFn$_invoke$arity$3 = (function (current_user_data,edit_data,save_cb){
var user_data = oc.web.actions.user.clean_user_data(current_user_data,(function (){var or__4126__auto__ = new cljs.core.Keyword(null,"user-data","user-data",2143823568).cljs$core$IFn$_invoke$arity$1(edit_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return edit_data;
}
})());
var user_profile_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(current_user_data),"partial-update","PATCH"], 0));
return oc.web.actions.user.user_profile_patch(user_data,user_profile_link,(function (success,resp){
if(cljs.core.truth_(success)){
oc.web.lib.utils.after((100),(function (){
return oc.web.actions.jwt.jwt_refresh.cljs$core$IFn$_invoke$arity$0();
}));
} else {
}

if(cljs.core.fn_QMARK_(save_cb)){
return oc.web.lib.utils.after((280),(function (){
return (save_cb.cljs$core$IFn$_invoke$arity$2 ? save_cb.cljs$core$IFn$_invoke$arity$2(success,resp) : save_cb.call(null,success,resp));
}));
} else {
return null;
}
}));
}));

(oc.web.actions.user.user_profile_save.cljs$lang$maxFixedArity = 3);

oc.web.actions.user.onboard_profile_save = (function oc$web$actions$user$onboard_profile_save(var_args){
var G__43394 = arguments.length;
switch (G__43394) {
case 2:
return oc.web.actions.user.onboard_profile_save.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.actions.user.onboard_profile_save.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.user.onboard_profile_save.cljs$core$IFn$_invoke$arity$2 = (function (current_user_data,edit_data){
return oc.web.actions.user.onboard_profile_save.cljs$core$IFn$_invoke$arity$3(current_user_data,edit_data,null);
}));

(oc.web.actions.user.onboard_profile_save.cljs$core$IFn$_invoke$arity$3 = (function (current_user_data,edit_data,org_editing_kw){
var org_editing = (cljs.core.truth_(org_editing_kw)?cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),org_editing_kw):null);
var user_data = oc.web.actions.user.clean_user_data(current_user_data,(function (){var or__4126__auto__ = new cljs.core.Keyword(null,"user-data","user-data",2143823568).cljs$core$IFn$_invoke$arity$1(edit_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return edit_data;
}
})());
var user_profile_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(current_user_data),"partial-update","PATCH"], 0));
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"user-profile-save","user-profile-save",1054717798)], null));

return oc.web.actions.user.user_profile_patch(user_data,user_profile_link,(function (success,resp){
if(cljs.core.truth_(org_editing)){
} else {
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"ap-loading","ap-loading",1563043900)], null),true], null));
}

return oc.web.lib.utils.after((100),(function (){
return oc.web.actions.jwt.jwt_refresh.cljs$core$IFn$_invoke$arity$1((function (){
if(cljs.core.truth_(org_editing)){
return oc.web.actions.org.create_or_update_org(org_editing);
} else {
return oc.web.api.get_entry_point(null,(function (_,entry_point_body){
return oc.web.router.nav_BANG_(oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(entry_point_body))))));
}));
}
}));
}));
}));
}));

(oc.web.actions.user.onboard_profile_save.cljs$lang$maxFixedArity = 3);

oc.web.actions.user.user_avatar_save = (function oc$web$actions$user$user_avatar_save(avatar_url){
var user_avatar_data = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103),avatar_url], null);
var current_user_data = oc.web.dispatcher.current_user_data.cljs$core$IFn$_invoke$arity$0();
var user_profile_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(current_user_data),"partial-update","PATCH"], 0));
return oc.web.api.patch_user(user_profile_link,user_avatar_data,(function (status,body,success){
if(cljs.core.not(success)){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("user-profile-avatar-update","failed","user-profile-avatar-update/failed",864759691)], null));

return oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"title","title",636505583),"Image upload error",new cljs.core.Keyword(null,"description","description",-1428560544),"An error occurred while processing your image. Please retry.",new cljs.core.Keyword(null,"expire","expire",-70657108),(3),new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"user-avatar-upload-failed","user-avatar-upload-failed",1374461962),new cljs.core.Keyword(null,"dismiss","dismiss",412569545),true], null));
} else {
oc.web.lib.utils.after((1000),oc.web.actions.jwt.jwt_refresh);

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("user-profile-avatar-update","success","user-profile-avatar-update/success",-380653217),oc.web.lib.json.json__GT_cljs(body)], null));

return oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"title","title",636505583),"Image update succeeded",new cljs.core.Keyword(null,"description","description",-1428560544),"Your image was succesfully updated.",new cljs.core.Keyword(null,"expire","expire",-70657108),(3),new cljs.core.Keyword(null,"dismiss","dismiss",412569545),true], null));
}
}));
});
oc.web.actions.user.user_profile_reset = (function oc$web$actions$user$user_profile_reset(){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"user-profile-reset","user-profile-reset",-287174589)], null));
});
oc.web.actions.user.resend_verification_email = (function oc$web$actions$user$resend_verification_email(){
var user_data = oc.web.dispatcher.current_user_data.cljs$core$IFn$_invoke$arity$0();
var resend_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(user_data),"resend-verification","POST"], 0));
if(cljs.core.truth_(resend_link)){
return oc.web.api.resend_verification_email(resend_link,(function (success){
return oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"title","title",636505583),(cljs.core.truth_(success)?"Verification email re-sent!":"An error occurred"),new cljs.core.Keyword(null,"description","description",-1428560544),(cljs.core.truth_(success)?null:"Please try again."),new cljs.core.Keyword(null,"expire","expire",-70657108),(3),new cljs.core.Keyword(null,"primary-bt-title","primary-bt-title",653140150),"OK",new cljs.core.Keyword(null,"primary-bt-dismiss","primary-bt-dismiss",-820688058),true,new cljs.core.Keyword(null,"id","id",-1388402092),cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(["resend-verification-",(cljs.core.truth_(success)?"ok":"failed")].join(''))], null));
}));
} else {
return null;
}
});
oc.web.actions.user.verify_continue = (function oc$web$actions$user$verify_continue(orgs){
var org__$1 = oc.web.actions.user.get_default_org(orgs);
return oc.web.router.nav_BANG_((cljs.core.truth_(org__$1)?((((cljs.core.empty_QMARK_(oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"first-name","first-name",-1559982131)))) && (cljs.core.empty_QMARK_(oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"last-name","last-name",-1695738974))))))?oc.web.urls.confirm_invitation_profile:oc.web.urls.org.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org__$1))):oc.web.urls.sign_up_profile));
});
oc.web.actions.user.expo_push_token_expiry = (((((60) * (60)) * (24)) * (352)) * (10));
/**
 * Save the expo push token in a cookie (or re-save to extend the cookie expire time)
 * and dispatch the value into the app-state.
 */
oc.web.actions.user.dispatch_expo_push_token = (function oc$web$actions$user$dispatch_expo_push_token(push_token){
if(cljs.core.truth_(push_token)){
oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$3(oc.web.router.expo_push_token_cookie,push_token,oc.web.actions.user.expo_push_token_expiry);

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"expo-push-token","expo-push-token",-2073999140),push_token], null));
} else {
return null;
}
});
oc.web.actions.user.recall_expo_push_token = (function oc$web$actions$user$recall_expo_push_token(){
return oc.web.actions.user.dispatch_expo_push_token(oc.web.lib.cookies.get_cookie(oc.web.router.expo_push_token_cookie));
});
oc.web.actions.user.add_expo_push_token = (function oc$web$actions$user$add_expo_push_token(push_token){
var user_data = oc.web.dispatcher.current_user_data.cljs$core$IFn$_invoke$arity$0();
var add_token_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(user_data),"add-expo-push-token","POST"], 0));
var need_to_add_QMARK_ = cljs.core.not(oc.web.utils.user.user_has_push_token_QMARK_(user_data,push_token));
if((!(need_to_add_QMARK_))){
return oc.web.actions.user.dispatch_expo_push_token(push_token);
} else {
if(cljs.core.truth_((function (){var and__4115__auto__ = add_token_link;
if(cljs.core.truth_(and__4115__auto__)){
return push_token;
} else {
return and__4115__auto__;
}
})())){
oc.web.actions.user.dispatch_expo_push_token("PENDING_PUSH_TOKEN");

return oc.web.api.add_expo_push_token(add_token_link,push_token,(function (success){
oc.web.actions.user.dispatch_expo_push_token(push_token);

return taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.actions.user",null,542,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Successfully saved Expo push notification token"], null);
}),null)),null,1215235343);
}));
} else {
return null;
}
}
});
/**
 * Push notification permission was denied.
 */
oc.web.actions.user.deny_push_notification_permission = (function oc$web$actions$user$deny_push_notification_permission(){
return oc.web.actions.user.dispatch_expo_push_token("");
});
oc.web.actions.user.initial_loading = (function oc$web$actions$user$initial_loading(var_args){
var args__4742__auto__ = [];
var len__4736__auto___43478 = arguments.length;
var i__4737__auto___43479 = (0);
while(true){
if((i__4737__auto___43479 < len__4736__auto___43478)){
args__4742__auto__.push((arguments[i__4737__auto___43479]));

var G__43480 = (i__4737__auto___43479 + (1));
i__4737__auto___43479 = G__43480;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.actions.user.initial_loading.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.actions.user.initial_loading.cljs$core$IFn$_invoke$arity$variadic = (function (p__43413){
var vec__43429 = p__43413;
var force_refresh_QMARK_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43429,(0),null);
var force_refresh = (function (){var or__4126__auto__ = force_refresh_QMARK_;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = oc.web.dispatcher.in_route_QMARK_.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"org","org",1495985));
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
return oc.web.dispatcher.in_route_QMARK_.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"login","login",55217519));
}
}
})();
var latest_entry_point = (cljs.core.truth_((function (){var or__4126__auto__ = force_refresh_QMARK_;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return (new cljs.core.Keyword(null,"latest-entry-point","latest-entry-point",2059134699).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state)) == null);
}
})())?(0):new cljs.core.Keyword(null,"latest-entry-point","latest-entry-point",2059134699).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state)));
var latest_auth_settings = (cljs.core.truth_((function (){var or__4126__auto__ = force_refresh_QMARK_;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return (new cljs.core.Keyword(null,"latest-auth-settings","latest-auth-settings",-572090527).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state)) == null);
}
})())?(0):new cljs.core.Keyword(null,"latest-auth-settings","latest-auth-settings",-572090527).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state)));
var now = (new Date()).getTime();
var reload_time = (((1000) * (60)) * (20));
if(cljs.core.truth_((function (){var or__4126__auto__ = ((now - latest_entry_point) > reload_time);
if(or__4126__auto__){
return or__4126__auto__;
} else {
var and__4115__auto__ = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
if(cljs.core.truth_(and__4115__auto__)){
return (oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0() == null);
} else {
return and__4115__auto__;
}
}
})())){
oc.web.actions.user.entry_point_get.cljs$core$IFn$_invoke$arity$variadic(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([force_refresh_QMARK_], 0));
} else {
}

if(((now - latest_auth_settings) > reload_time)){
return oc.web.actions.user.auth_settings_get();
} else {
return null;
}
}));

(oc.web.actions.user.initial_loading.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.actions.user.initial_loading.cljs$lang$applyTo = (function (seq43408){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq43408));
}));

oc.web.actions.user.read_notifications = (function oc$web$actions$user$read_notifications(){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("user-notifications","read","user-notifications/read",1021558955),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0()], null));
});
oc.web.actions.user.read_notification = (function oc$web$actions$user$read_notification(notification){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("user-notification","read","user-notification/read",-2030866956),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),notification], null));
});
oc.web.actions.user.load_follow_list = (function oc$web$actions$user$load_follow_list(){
return oc.web.ws.change_client.follow_list();
});
oc.web.actions.user.load_followers_count = (function oc$web$actions$user$load_followers_count(){
return oc.web.ws.change_client.followers_count();
});
oc.web.actions.user.refresh_follow_containers = (function oc$web$actions$user$refresh_follow_containers(){
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
var current_board_slug = oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0();
var is_following_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(current_board_slug,"following");
var is_replies_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(current_board_slug,"replies");
var following_delay = ((is_following_QMARK_)?(1):(500));
var replies_delay = ((is_replies_QMARK_)?(1):(500));
oc.web.lib.utils.maybe_after(following_delay,(function (){
return oc.web.actions.activity.following_get.cljs$core$IFn$_invoke$arity$3(org_data,is_following_QMARK_,null);
}));

return oc.web.lib.utils.maybe_after(replies_delay,(function (){
return oc.web.actions.activity.replies_get.cljs$core$IFn$_invoke$arity$3(org_data,is_replies_QMARK_,null);
}));
});
oc.web.actions.user.toggle_publisher = (function oc$web$actions$user$toggle_publisher(publisher_uuid){
var org_slug = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
var current_publishers = cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291),oc.web.dispatcher.follow_publishers_list.cljs$core$IFn$_invoke$arity$1(org_slug));
var follow_QMARK_ = cljs.core.not(oc.web.lib.utils.in_QMARK_(current_publishers,publisher_uuid));
var next_publishers = ((follow_QMARK_)?cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(cljs.core.set(current_publishers),publisher_uuid)):cljs.core.vec(cljs.core.disj.cljs$core$IFn$_invoke$arity$2(cljs.core.set(current_publishers),publisher_uuid)));
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("publisher","follow","publisher/follow",973499022),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"org-slug","org-slug",-726595051),org_slug,new cljs.core.Keyword(null,"publisher-uuids","publisher-uuids",1855461704),next_publishers,new cljs.core.Keyword(null,"follow?","follow?",474567445),follow_QMARK_,new cljs.core.Keyword(null,"publisher-uuid","publisher-uuid",-1907490269),publisher_uuid], null)], null));

if(follow_QMARK_){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"follow-list-last-added","follow-list-last-added",782919652),org_slug,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"last-added-uuid","last-added-uuid",925482366),publisher_uuid,new cljs.core.Keyword(null,"resource-type","resource-type",1844262326),new cljs.core.Keyword(null,"user","user",1532431356)], null)], null));
} else {
}

if(follow_QMARK_){
return oc.web.ws.change_client.publisher_follow(publisher_uuid);
} else {
return oc.web.ws.change_client.publisher_unfollow(publisher_uuid);
}
});
oc.web.actions.user.toggle_board = (function oc$web$actions$user$toggle_board(board_uuid){
var org_slug = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
var current_boards = cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),oc.web.dispatcher.follow_boards_list.cljs$core$IFn$_invoke$arity$1(org_slug));
var follow_QMARK_ = cljs.core.not(oc.web.lib.utils.in_QMARK_(current_boards,board_uuid));
var next_boards = ((follow_QMARK_)?cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(cljs.core.set(current_boards),board_uuid)):cljs.core.vec(cljs.core.disj.cljs$core$IFn$_invoke$arity$2(cljs.core.set(current_boards),board_uuid)));
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("board","follow","board/follow",-915817092),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"org-slug","org-slug",-726595051),org_slug,new cljs.core.Keyword(null,"board-uuids","board-uuids",515371926),next_boards,new cljs.core.Keyword(null,"follow?","follow?",474567445),follow_QMARK_,new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127),board_uuid], null)], null));

if(follow_QMARK_){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"follow-list-last-added","follow-list-last-added",782919652),org_slug,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"last-added-uuid","last-added-uuid",925482366),board_uuid,new cljs.core.Keyword(null,"resource-type","resource-type",1844262326),new cljs.core.Keyword(null,"board","board",-1907017633)], null)], null));
} else {
}

if(follow_QMARK_){
oc.web.ws.change_client.board_follow(board_uuid);
} else {
oc.web.ws.change_client.board_unfollow(board_uuid);
}

return oc.web.actions.user.refresh_follow_containers();
});
oc.web.actions.user.subscribe = (function oc$web$actions$user$subscribe(){
oc.web.ws.notify_client.subscribe(new cljs.core.Keyword("user","notifications","user/notifications",1689372584),(function (p__43443){
var map__43444 = p__43443;
var map__43444__$1 = (((((!((map__43444 == null))))?(((((map__43444.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43444.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43444):map__43444);
var data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43444__$1,new cljs.core.Keyword(null,"data","data",-232669377));
return null;
}));

oc.web.ws.notify_client.subscribe(new cljs.core.Keyword("user","notification","user/notification",-217694338),(function (p__43446){
var map__43447 = p__43446;
var map__43447__$1 = (((((!((map__43447 == null))))?(((((map__43447.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43447.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43447):map__43447);
var data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43447__$1,new cljs.core.Keyword(null,"data","data",-232669377));
var temp__5735__auto__ = oc.web.utils.notification.fix_notification(cljs.core.deref(oc.web.dispatcher.app_state),cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(data,new cljs.core.Keyword(null,"unread","unread",-1950424572),true));
if(cljs.core.truth_(temp__5735__auto__)){
var fixed_notification = temp__5735__auto__;
return oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"title","title",636505583),new cljs.core.Keyword(null,"title","title",636505583).cljs$core$IFn$_invoke$arity$1(fixed_notification),new cljs.core.Keyword(null,"mention","mention",-1057367181),true,new cljs.core.Keyword(null,"dismiss","dismiss",412569545),true,new cljs.core.Keyword(null,"click","click",1912301393),new cljs.core.Keyword(null,"click","click",1912301393).cljs$core$IFn$_invoke$arity$1(fixed_notification),new cljs.core.Keyword(null,"mention-author","mention-author",-1184076083),new cljs.core.Keyword(null,"author","author",2111686192).cljs$core$IFn$_invoke$arity$1(fixed_notification),new cljs.core.Keyword(null,"description","description",-1428560544),new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(fixed_notification),new cljs.core.Keyword(null,"id","id",-1388402092),["notif-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"created-at","created-at",-89248644).cljs$core$IFn$_invoke$arity$1(fixed_notification))].join(''),new cljs.core.Keyword(null,"expire","expire",-70657108),(5)], null));
} else {
return null;
}
}));

oc.web.ws.change_client.subscribe(new cljs.core.Keyword("follow","list","follow/list",1912779396),(function (p__43449){
var map__43450 = p__43449;
var map__43450__$1 = (((((!((map__43450 == null))))?(((((map__43450.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43450.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43450):map__43450);
var data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43450__$1,new cljs.core.Keyword(null,"data","data",-232669377));
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("follow","loaded","follow/loaded",-111999302),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),data], null));
}));

return oc.web.ws.change_client.subscribe(new cljs.core.Keyword("followers","count","followers/count",-1253725598),(function (p__43452){
var map__43453 = p__43452;
var map__43453__$1 = (((((!((map__43453 == null))))?(((((map__43453.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43453.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43453):map__43453);
var data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43453__$1,new cljs.core.Keyword(null,"data","data",-232669377));
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("followers-count","finish","followers-count/finish",1304966571),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),data], null));
}));
});
oc.web.actions.user.force_jwt_refresh = (function oc$web$actions$user$force_jwt_refresh(){
if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
return oc.web.actions.jwt.jwt_refresh.cljs$core$IFn$_invoke$arity$0();
} else {
return null;
}
});
(window.OCWebForceRefreshToken = oc.web.actions.user.force_jwt_refresh);

//# sourceMappingURL=oc.web.actions.user.js.map
