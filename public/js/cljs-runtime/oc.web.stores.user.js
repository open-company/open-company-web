goog.provide('oc.web.stores.user');
var module$node_modules$moment_timezone$index=shadow.js.require("module$node_modules$moment_timezone$index", {});
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.stores !== 'undefined') && (typeof oc.web.stores.user !== 'undefined') && (typeof oc.web.stores.user.default_avatar_url !== 'undefined')){
} else {
oc.web.stores.user.default_avatar_url = oc.web.utils.user.random_avatar();
}
oc.web.stores.user.user_icon = (function oc$web$stores$user$user_icon(user_id){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(user_id,oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"user-id","user-id",-206822291)))){
return oc.web.utils.user.default_avatar;
} else {
return cljs.core.first(oc.web.utils.user.other_default_avatars);
}
});
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.stores !== 'undefined') && (typeof oc.web.stores.user !== 'undefined') && (typeof oc.web.stores.user.show_login_overlay_QMARK_ !== 'undefined')){
} else {
oc.web.stores.user.show_login_overlay_QMARK_ = oc.web.dispatcher.show_login_overlay_key;
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.stores !== 'undefined') && (typeof oc.web.stores.user !== 'undefined') && (typeof oc.web.stores.user.signup_with_email !== 'undefined')){
} else {
oc.web.stores.user.signup_with_email = new cljs.core.Keyword(null,"signup-with-email","signup-with-email",-22609037);
}
oc.web.stores.user.get_show_login_overlay = (function oc$web$stores$user$get_show_login_overlay(){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [oc.web.dispatcher.show_login_overlay_key], null));
});
oc.web.stores.user.auth_settings_QMARK_ = (function oc$web$stores$user$auth_settings_QMARK_(){
return cljs.core.contains_QMARK_(cljs.core.deref(oc.web.dispatcher.app_state),cljs.core.first(oc.web.dispatcher.auth_settings_key));
});
oc.web.stores.user.auth_settings_status_QMARK_ = (function oc$web$stores$user$auth_settings_status_QMARK_(){
return ((oc.web.stores.user.auth_settings_QMARK_()) && (cljs.core.contains_QMARK_(oc.web.dispatcher.auth_settings.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"status","status",-1997798413))));
});
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"auth-settings","auth-settings",-1775088219),(function (db,p__42489){
var vec__42490 = p__42489;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42490,(0),null);
var body = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42490,(1),null);
var next_db = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"latest-auth-settings","latest-auth-settings",-572090527),(new Date()).getTime());
return cljs.core.assoc_in(next_db,oc.web.dispatcher.auth_settings_key,body);
}));
oc.web.stores.user.fixed_avatar_url = (function oc$web$stores$user$fixed_avatar_url(avatar_url){
if(cljs.core.empty_QMARK_(avatar_url)){
return oc.web.lib.utils.cdn.cljs$core$IFn$_invoke$arity$variadic(oc.web.stores.user.default_avatar_url,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true], 0));
} else {
return avatar_url;
}
});
oc.web.stores.user.default_invite_type = "email";
oc.web.stores.user.parse_users = (function oc$web$stores$user$parse_users(users_list,org_data,follow_publishers_list){
var follow_publishers_set = ((cljs.core.every_QMARK_(cljs.core.map_QMARK_,follow_publishers_list))?cljs.core.set(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291),follow_publishers_list)):cljs.core.set(follow_publishers_list));
return cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (u){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3((function (){var user = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.update.cljs$core$IFn$_invoke$arity$3(cljs.core.update.cljs$core$IFn$_invoke$arity$3(u,new cljs.core.Keyword(null,"name","name",1843675177),(function (p1__42498_SHARP_){
var or__4126__auto__ = p1__42498_SHARP_;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.lib.user.name_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([u], 0));
}
})),new cljs.core.Keyword(null,"short-name","short-name",-1767085022),(function (p1__42499_SHARP_){
var or__4126__auto__ = p1__42499_SHARP_;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.lib.user.short_name_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([u], 0));
}
})),new cljs.core.Keyword(null,"follow","follow",-809317662),(function (){var G__42502 = new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(u);
return (follow_publishers_set.cljs$core$IFn$_invoke$arity$1 ? follow_publishers_set.cljs$core$IFn$_invoke$arity$1(G__42502) : follow_publishers_set.call(null,G__42502));
})());
var user__$1 = ((cljs.core.map_QMARK_(org_data))?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(user,new cljs.core.Keyword(null,"role","role",-736691072),oc.web.utils.user.get_user_type.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user,org_data], 0))):user);
if(cljs.core.truth_(new cljs.core.Keyword(null,"role","role",-736691072).cljs$core$IFn$_invoke$arity$1(user__$1))){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(user__$1,new cljs.core.Keyword(null,"role-string","role-string",82910575),oc.web.utils.user.user_role_string(new cljs.core.Keyword(null,"role","role",-736691072).cljs$core$IFn$_invoke$arity$1(user__$1)));
} else {
return user__$1;
}
})(),new cljs.core.Keyword(null,"self?","self?",-701815921),cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(u),oc.web.lib.jwt.user_id()));
}),users_list);
});
oc.web.stores.user.parse_user_data = (function oc$web$stores$user$parse_user_data(user_data,org_data,active_users){
var active_user_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(active_users,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user_data));
var u = user_data;
var u__$1 = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([active_user_data,u], 0));
var u__$2 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(u__$1,new cljs.core.Keyword(null,"role","role",-736691072),oc.web.utils.user.get_user_type.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([u__$1,org_data], 0)));
var u__$3 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(u__$2,new cljs.core.Keyword(null,"role-string","role-string",82910575),oc.web.utils.user.user_role_string(new cljs.core.Keyword(null,"role","role",-736691072).cljs$core$IFn$_invoke$arity$1(u__$2)));
var u__$4 = cljs.core.update.cljs$core$IFn$_invoke$arity$3(u__$3,new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103),oc.web.stores.user.fixed_avatar_url);
var u__$5 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(u__$4,new cljs.core.Keyword(null,"auth-source","auth-source",1912135250),(function (){var or__4126__auto__ = oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"auth-source","auth-source",1912135250));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.stores.user.default_invite_type;
}
})());
var u__$6 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(u__$5,new cljs.core.Keyword(null,"name","name",1843675177),oc.lib.user.name_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user_data], 0)));
var u__$7 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(u__$6,new cljs.core.Keyword(null,"short-name","short-name",-1767085022),oc.lib.user.short_name_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user_data], 0)));
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(u__$7,new cljs.core.Keyword(null,"digest-delivery","digest-delivery",-1355287749),cljs.core.set(cljs.core.map.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword,(function (){var or__4126__auto__ = new cljs.core.Keyword(null,"digest-delivery","digest-delivery",-1355287749).cljs$core$IFn$_invoke$arity$1(user_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.PersistentVector.EMPTY;
}
})())));
});
oc.web.stores.user.empty_user_STAR_ = new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"first-name","first-name",-1559982131),"",new cljs.core.Keyword(null,"last-name","last-name",-1695738974),"",new cljs.core.Keyword(null,"password","password",417022471),"",new cljs.core.Keyword(null,"email","email",1415816706),"",new cljs.core.Keyword(null,"blurb","blurb",-769928228),"",new cljs.core.Keyword(null,"location","location",1815599388),"",new cljs.core.Keyword(null,"title","title",636505583),"",new cljs.core.Keyword(null,"profiles","profiles",507634713),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"twitter","twitter",-589267671),"",new cljs.core.Keyword(null,"linked-in","linked-in",-558412808),"",new cljs.core.Keyword(null,"instagram","instagram",239403223),"",new cljs.core.Keyword(null,"facebook","facebook",-1297545787),""], null)], null);
/**
 * This is a function to call the timezone guess when needed and not only one time on page load.
 */
oc.web.stores.user.empty_user_data = (function oc$web$stores$user$empty_user_data(edit_QMARK_){
var G__42511 = oc.web.stores.user.empty_user_STAR_;
var G__42511__$1 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__42511,new cljs.core.Keyword(null,"timezone","timezone",1831928099),(function (){var or__4126__auto__ = module$node_modules$moment_timezone$index.tz.guess();
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "";
}
})())
;
if(cljs.core.truth_(edit_QMARK_)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__42511__$1,new cljs.core.Keyword(null,"has-changes","has-changes",-631476764),false);
} else {
return G__42511__$1;
}
});
oc.web.stores.user.editable_user_data = (function oc$web$stores$user$editable_user_data(edit_user_data,new_user_data){
var changed_QMARK_ = new cljs.core.Keyword(null,"has-changes","has-changes",-631476764).cljs$core$IFn$_invoke$arity$1(edit_user_data);
var changed_user_data = cljs.core.select_keys(edit_user_data,new cljs.core.PersistentVector(null, 10, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"first-name","first-name",-1559982131),new cljs.core.Keyword(null,"last-name","last-name",-1695738974),new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103),new cljs.core.Keyword(null,"password","password",417022471),new cljs.core.Keyword(null,"timezone","timezone",1831928099),new cljs.core.Keyword(null,"blurb","blurb",-769928228),new cljs.core.Keyword(null,"location","location",1815599388),new cljs.core.Keyword(null,"title","title",636505583),new cljs.core.Keyword(null,"profiles","profiles",507634713),new cljs.core.Keyword(null,"has-changes","has-changes",-631476764)], null));
var G__42515 = oc.web.stores.user.empty_user_data(true);
var G__42515__$1 = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([G__42515,new_user_data], 0))
;
var G__42515__$2 = (cljs.core.truth_(changed_QMARK_)?cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([G__42515__$1,changed_user_data], 0)):G__42515__$1);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__42515__$2,new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103),oc.web.stores.user.fixed_avatar_url(new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103).cljs$core$IFn$_invoke$arity$1(new_user_data)));

});
oc.web.stores.user.update_user_data = (function oc$web$stores$user$update_user_data(var_args){
var G__42518 = arguments.length;
switch (G__42518) {
case 2:
return oc.web.stores.user.update_user_data.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.stores.user.update_user_data.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.stores.user.update_user_data.cljs$core$IFn$_invoke$arity$2 = (function (db,user_data){
return oc.web.stores.user.update_user_data.cljs$core$IFn$_invoke$arity$3(db,user_data,false);
}));

(oc.web.stores.user.update_user_data.cljs$core$IFn$_invoke$arity$3 = (function (db,user_data,force_edit_reset_QMARK_){
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$1(db);
var active_users = oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),db);
var fixed_user_data = oc.web.stores.user.parse_user_data(user_data,org_data,active_users);
var active_user_key = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.active_users_key(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data)),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user_data));
var next_db = (cljs.core.truth_(org_data)?cljs.core.update_in.cljs$core$IFn$_invoke$arity$4(db,active_user_key,cljs.core.merge,fixed_user_data):db);
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.update.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(next_db,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915),fixed_user_data),new cljs.core.Keyword(null,"edit-user-profile","edit-user-profile",-479059118),(function (p1__42516_SHARP_){
return oc.web.stores.user.editable_user_data((cljs.core.truth_(force_edit_reset_QMARK_)?null:p1__42516_SHARP_),fixed_user_data);
})),new cljs.core.Keyword(null,"edit-user-profile-avatar","edit-user-profile-avatar",303025729),new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103).cljs$core$IFn$_invoke$arity$1(fixed_user_data)),new cljs.core.Keyword(null,"edit-user-profile-failed","edit-user-profile-failed",418884834));
}));

(oc.web.stores.user.update_user_data.cljs$lang$maxFixedArity = 3);

oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("user-profile-avatar-update","failed","user-profile-avatar-update/failed",864759691),(function (db,p__42538){
var vec__42539 = p__42538;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42539,(0),null);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"edit-user-profile-avatar","edit-user-profile-avatar",303025729),new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915).cljs$core$IFn$_invoke$arity$1(db)));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"user-data","user-data",2143823568),(function (db,p__42542){
var vec__42543 = p__42542;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42543,(0),null);
var user_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42543,(1),null);
return oc.web.stores.user.update_user_data.cljs$core$IFn$_invoke$arity$2(db,user_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("user-profile-avatar-update","success","user-profile-avatar-update/success",-380653217),(function (db,p__42546){
var vec__42548 = p__42546;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42548,(0),null);
var user_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42548,(1),null);
return oc.web.stores.user.update_user_data.cljs$core$IFn$_invoke$arity$2(db,user_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"login-overlay-show","login-overlay-show",-1173520092),(function (db,p__42553){
var vec__42554 = p__42553;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42554,(0),null);
var show_login_overlay = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42554,(1),null);
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(show_login_overlay,new cljs.core.Keyword(null,"login-with-email","login-with-email",-1597480700))){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,oc.web.dispatcher.show_login_overlay_key,show_login_overlay),new cljs.core.Keyword(null,"login-with-email","login-with-email",-1597480700),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"email","email",1415816706),"",new cljs.core.Keyword(null,"pswd","pswd",278786885),""], null)),new cljs.core.Keyword(null,"login-with-email-error","login-with-email-error",-373631840));
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(show_login_overlay,new cljs.core.Keyword(null,"signup-with-email","signup-with-email",-22609037))){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,oc.web.dispatcher.show_login_overlay_key,show_login_overlay),new cljs.core.Keyword(null,"signup-with-email","signup-with-email",-22609037),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"firstname","firstname",1659984849),"",new cljs.core.Keyword(null,"lastname","lastname",-265181465),"",new cljs.core.Keyword(null,"email","email",1415816706),"",new cljs.core.Keyword(null,"pswd","pswd",278786885),""], null)),new cljs.core.Keyword(null,"signup-with-email-error","signup-with-email-error",-805023801));
} else {
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,oc.web.dispatcher.show_login_overlay_key,show_login_overlay);

}
}
}));
oc.web.stores.user.dissoc_auth = (function oc$web$stores$user$dissoc_auth(db){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(db,new cljs.core.Keyword(null,"latest-auth-settings","latest-auth-settings",-572090527),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"latest-entry-point","latest-entry-point",2059134699)], 0));
});
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"login-with-email","login-with-email",-1597480700),(function (db,p__42557){
var vec__42558 = p__42557;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42558,(0),null);
return oc.web.stores.user.dissoc_auth(cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(db,new cljs.core.Keyword(null,"login-with-email-error","login-with-email-error",-373631840)));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"login-with-slack","login-with-slack",1915911677),(function (db,p__42561){
var vec__42562 = p__42561;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42562,(0),null);
return oc.web.stores.user.dissoc_auth(db);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("login-with-email","failed","login-with-email/failed",1547028965),(function (db,p__42565){
var vec__42566 = p__42565;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42566,(0),null);
var error = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42566,(1),null);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"login-with-email-error","login-with-email-error",-373631840),error);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("login-with-email","success","login-with-email/success",-80639303),(function (db,p__42578){
var vec__42579 = p__42578;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42579,(0),null);
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(db,oc.web.dispatcher.show_login_overlay_key);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"auth-with-token","auth-with-token",1887574080),(function (db,p__42582){
var vec__42583 = p__42582;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42583,(0),null);
var token_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42583,(1),null);
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"auth-with-token-type","auth-with-token-type",-1400794966),token_type),new cljs.core.Keyword(null,"latest-auth-settings","latest-auth-settings",-572090527),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"latest-entry-point","latest-entry-point",2059134699)], 0));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("auth-with-token","failed","auth-with-token/failed",55615787),(function (db,p__42586){
var vec__42587 = p__42586;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42587,(0),null);
var error = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42587,(1),null);
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"auth-with-token-type","auth-with-token-type",-1400794966).cljs$core$IFn$_invoke$arity$1(db),new cljs.core.Keyword(null,"password-reset","password-reset",1971592302))){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"collect-pswd-error","collect-pswd-error",-2005825999),error);
} else {
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"email-verification-error","email-verification-error",358093817),error);
}
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("auth-with-token","success","auth-with-token/success",1511390719),(function (db,p__42590){
var vec__42591 = p__42590;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42591,(0),null);
var jwt = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42591,(1),null);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"email-verification-success","email-verification-success",-1955344779),true);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"pswd-collect","pswd-collect",-692397310),(function (db,p__42594){
var vec__42595 = p__42594;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42595,(0),null);
var password_reset_QMARK_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42595,(1),null);
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"is-password-reset","is-password-reset",-1970055648),password_reset_QMARK_),new cljs.core.Keyword(null,"latest-entry-point","latest-entry-point",2059134699),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"latest-auth-settings","latest-auth-settings",-572090527)], 0));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("pswd-collect","finish","pswd-collect/finish",-1819836763),(function (db,p__42598){
var vec__42599 = p__42598;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42599,(0),null);
var status = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42599,(1),null);
if((((status >= (200))) && ((status <= (299))))){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(db,new cljs.core.Keyword(null,"show-login-overlay","show-login-overlay",1026669411));
} else {
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"collect-password-error","collect-password-error",-1137727549),status);
}
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"password-reset","password-reset",1971592302),(function (db,p__42602){
var vec__42603 = p__42602;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42603,(0),null);
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(db,new cljs.core.Keyword(null,"latest-entry-point","latest-entry-point",2059134699),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"latest-auth-settings","latest-auth-settings",-572090527)], 0));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("password-reset","finish","password-reset/finish",-57929867),(function (db,p__42606){
var vec__42607 = p__42606;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42607,(0),null);
var status = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42607,(1),null);
return cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"password-reset","password-reset",1971592302),new cljs.core.Keyword(null,"success","success",1890645906)], null),(((status >= (200))) && ((status <= (299)))));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"user-profile-reset","user-profile-reset",-287174589),(function (db,p__42610){
var vec__42611 = p__42610;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42611,(0),null);
return oc.web.stores.user.update_user_data.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915).cljs$core$IFn$_invoke$arity$1(db),true);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"user-profile-save","user-profile-save",1054717798),(function (db,p__42614){
var vec__42615 = p__42614;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42615,(0),null);
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(cljs.core.update.cljs$core$IFn$_invoke$arity$4(db,new cljs.core.Keyword(null,"edit-user-profile","edit-user-profile",-479059118),cljs.core.merge,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"loading","loading",-737050189),true,new cljs.core.Keyword(null,"has-changes","has-changes",-631476764),false], null)),new cljs.core.Keyword(null,"latest-entry-point","latest-entry-point",2059134699),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"latest-auth-settings","latest-auth-settings",-572090527)], 0)),new cljs.core.Keyword(null,"new-slack-user","new-slack-user",-146614755));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("user-profile-update","failed","user-profile-update/failed",148620531),(function (db,p__42618){
var vec__42619 = p__42618;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42619,(0),null);
return cljs.core.update.cljs$core$IFn$_invoke$arity$4(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"edit-user-profile-failed","edit-user-profile-failed",418884834),true),new cljs.core.Keyword(null,"edit-user-profile","edit-user-profile",-479059118),cljs.core.merge,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"loading","loading",-737050189),false,new cljs.core.Keyword(null,"has-changes","has-changes",-631476764),true], null));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"signup-with-email","signup-with-email",-22609037),(function (db,p__42622){
var vec__42623 = p__42622;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42623,(0),null);
return cljs.core.assoc_in(cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(db,new cljs.core.Keyword(null,"signup-with-email-error","signup-with-email-error",-805023801),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"latest-auth-settings","latest-auth-settings",-572090527),new cljs.core.Keyword(null,"latest-entry-point","latest-entry-point",2059134699)], 0)),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"signup-with-email","signup-with-email",-22609037),new cljs.core.Keyword(null,"error","error",-978969032)], null),null);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("signup-with-email","failed","signup-with-email/failed",-421316360),(function (db,p__42626){
var vec__42627 = p__42626;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42627,(0),null);
var status = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42627,(1),null);
return cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"signup-with-email","signup-with-email",-22609037),new cljs.core.Keyword(null,"error","error",-978969032)], null),status);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("signup-with-email","success","signup-with-email/success",779597292),(function (db,p__42636){
var vec__42637 = p__42636;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42637,(0),null);
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(db,new cljs.core.Keyword(null,"signup-with-email-error","signup-with-email-error",-805023801));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"logout","logout",1418564329),(function (db,_){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(db,new cljs.core.Keyword(null,"jwt","jwt",1504015441),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"latest-entry-point","latest-entry-point",2059134699),new cljs.core.Keyword(null,"latest-auth-settings","latest-auth-settings",-572090527)], 0));
}));
oc.web.stores.user.orgs_QMARK_ = (function oc$web$stores$user$orgs_QMARK_(){
return cljs.core.contains_QMARK_(cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.orgs_key);
});
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"entry-point","entry-point",794189075),(function (db,p__42640){
var vec__42641 = p__42640;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42641,(0),null);
var orgs = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42641,(1),null);
var collection = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42641,(2),null);
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(cljs.core.assoc_in(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"latest-entry-point","latest-entry-point",2059134699),(new Date()).getTime()),new cljs.core.Keyword(null,"loading","loading",-737050189)),oc.web.dispatcher.orgs_key,orgs),oc.web.dispatcher.api_entry_point_key,new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(collection)),new cljs.core.Keyword(null,"slack-lander-check-team-redirect","slack-lander-check-team-redirect",-1106137789),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"email-lander-check-team-redirect","email-lander-check-team-redirect",-1706094922)], 0));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"invitation-confirmed","invitation-confirmed",-298226477),(function (db,p__42644){
var vec__42645 = p__42644;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42645,(0),null);
var confirmed = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42645,(1),null);
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"email-confirmed","email-confirmed",-2125677510),confirmed),new cljs.core.Keyword(null,"latest-entry-point","latest-entry-point",2059134699),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"latest-auth-settings","latest-auth-settings",-572090527)], 0));
}));
oc.web.stores.user.has_slack_bot_QMARK_ = (function oc$web$stores$user$has_slack_bot_QMARK_(org_data){
return oc.web.lib.jwt.team_has_bot_QMARK_(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(org_data));
});
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"user-notifications","user-notifications",-2046716914),(function (db,p__42652){
var vec__42653 = p__42652;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42653,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42653,(1),null);
var notifications = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42653,(2),null);
return cljs.core.assoc_in(db,oc.web.dispatcher.user_notifications_key(org_slug),notifications);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"user-notification","user-notification",-437916696),(function (db,p__42656){
var vec__42657 = p__42656;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42657,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42657,(1),null);
var notification = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42657,(2),null);
var user_notifications_key = oc.web.dispatcher.user_notifications_key(org_slug);
var old_notifications = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,oc.web.dispatcher.sorted_user_notifications_key(org_slug));
var new_notifications = cljs.core.cons(notification,old_notifications);
return cljs.core.assoc_in(db,user_notifications_key,oc.web.utils.notification.fix_notifications.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([db,new_notifications], 0)));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("user-notifications","read","user-notifications/read",1021558955),(function (db,p__42667){
var vec__42668 = p__42667;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42668,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42668,(1),null);
var user_notifications_key = oc.web.dispatcher.user_notifications_key(org_slug);
var old_notifications = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,oc.web.dispatcher.sorted_user_notifications_key(org_slug));
var read_notifications = cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__42666_SHARP_){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__42666_SHARP_,new cljs.core.Keyword(null,"unread","unread",-1950424572),false);
}),old_notifications);
return cljs.core.assoc_in(db,user_notifications_key,oc.web.utils.notification.fix_notifications.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([db,read_notifications], 0)));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("user-notification","read","user-notification/read",-2030866956),(function (db,p__42672){
var vec__42673 = p__42672;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42673,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42673,(1),null);
var notification = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42673,(2),null);
var user_notifications_key = oc.web.dispatcher.user_notifications_key(org_slug);
var old_notifications = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,oc.web.dispatcher.sorted_user_notifications_key(org_slug));
var read_notifications = cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__42671_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"notify-at","notify-at",-1494324812).cljs$core$IFn$_invoke$arity$1(p1__42671_SHARP_),new cljs.core.Keyword(null,"notify-at","notify-at",-1494324812).cljs$core$IFn$_invoke$arity$1(notification))){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__42671_SHARP_,new cljs.core.Keyword(null,"unread","unread",-1950424572),false);
} else {
return p1__42671_SHARP_;
}
}),old_notifications);
return cljs.core.assoc_in(db,user_notifications_key,oc.web.utils.notification.fix_notifications.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([db,read_notifications], 0)));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"user-notification-remove-by-entry","user-notification-remove-by-entry",-1249722649),(function (db,p__42677){
var vec__42678 = p__42677;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42678,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42678,(1),null);
var board_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42678,(2),null);
var entry_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42678,(3),null);
var notifications = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,oc.web.dispatcher.sorted_user_notifications_key(org_slug));
var filtered_notifications = cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__42676_SHARP_){
return ((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(board_id,new cljs.core.Keyword(null,"board-id","board-id",-1767919501).cljs$core$IFn$_invoke$arity$1(p1__42676_SHARP_))) || (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(entry_id,new cljs.core.Keyword(null,"entry-id","entry-id",591934358).cljs$core$IFn$_invoke$arity$1(p1__42676_SHARP_))));
}),notifications);
return cljs.core.assoc_in(db,oc.web.dispatcher.user_notifications_key(org_slug),oc.web.utils.notification.fix_notifications.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([db,filtered_notifications], 0)));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"expo-push-token","expo-push-token",-2073999140),(function (db,p__42681){
var vec__42682 = p__42681;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42682,(0),null);
var push_token = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42682,(1),null);
if(cljs.core.truth_(push_token)){
return cljs.core.assoc_in(db,oc.web.dispatcher.expo_push_token_key,push_token);
} else {
return db;
}
}));
oc.web.stores.user.enrich_publishers_list = (function oc$web$stores$user$enrich_publishers_list(publishers_list,active_users_map){
if(((cljs.core.seq(publishers_list)) && (cljs.core.seq(active_users_map)))){
var publisher_uuids = cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,((cljs.core.every_QMARK_(cljs.core.map_QMARK_,publishers_list))?cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291),publishers_list):publishers_list));
return cljs.core.vec(cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"short-name","short-name",-1767085022),cljs.core.map.cljs$core$IFn$_invoke$arity$2(active_users_map,publisher_uuids)));
} else {
return publishers_list;
}
});
oc.web.stores.user.filter_org_boards = (function oc$web$stores$user$filter_org_boards(boards_data){
return cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__42685_SHARP_){
return ((cljs.core.not(new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803).cljs$core$IFn$_invoke$arity$1(p1__42685_SHARP_))) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__42685_SHARP_),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.default_drafts_board))));
}),boards_data);
});
oc.web.stores.user.enrich_boards_list = (function oc$web$stores$user$enrich_boards_list(unfollow_board_uuids,org_boards){
if(cljs.core.seq(org_boards)){
var all_board_uuids = cljs.core.set(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),oc.web.stores.user.filter_org_boards(org_boards)));
var follow_board_uuids = clojure.set.difference.cljs$core$IFn$_invoke$arity$2(all_board_uuids,cljs.core.set(unfollow_board_uuids));
var boards_map = cljs.core.zipmap(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),org_boards),org_boards);
return cljs.core.vec(cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"name","name",1843675177),cljs.core.map.cljs$core$IFn$_invoke$arity$2(boards_map,follow_board_uuids)));
} else {
return null;
}
});
/**
 * Given the new list of board and publisher followers, update the following flag in each board and contributions data we have.
 */
oc.web.stores.user.update_contributions_and_boards = (function oc$web$stores$user$update_contributions_and_boards(db,org_slug,follow_boards_list,follow_publishers_list){
var change_data = oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$1(db);
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$2(db,org_slug);
var follow_publisher_uuids_set = cljs.core.set(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291),follow_publishers_list));
var contributions_list_key = oc.web.dispatcher.contributions_list_key(org_slug);
var next_active_users = cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.merge,cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p__42698){
var vec__42699 = p__42698;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42699,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42699,(1),null);
return cljs.core.PersistentHashMap.fromArrays([k],[cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(v,new cljs.core.Keyword(null,"following","following",-2049193617),(function (){var G__42702 = new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(v);
return (follow_publisher_uuids_set.cljs$core$IFn$_invoke$arity$1 ? follow_publisher_uuids_set.cljs$core$IFn$_invoke$arity$1(G__42702) : follow_publisher_uuids_set.call(null,G__42702));
})())]);
}),oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org_slug,db)));
var next_db_STAR_ = cljs.core.assoc_in(db,oc.web.dispatcher.active_users_key(org_slug),next_active_users);
var next_db = cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (tdb,contrib_key){
var rp_contrib_data_key = oc.web.dispatcher.contributions_data_key.cljs$core$IFn$_invoke$arity$3(org_slug,contrib_key,oc.web.dispatcher.recently_posted_sort);
var ra_contrib_data_key = oc.web.dispatcher.contributions_data_key.cljs$core$IFn$_invoke$arity$3(org_slug,contrib_key,oc.web.dispatcher.recent_activity_sort);
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(tdb,rp_contrib_data_key,(function (p1__42692_SHARP_){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(oc.web.utils.activity.parse_contributions(p1__42692_SHARP_,change_data,org_data,next_active_users,follow_publishers_list,oc.web.dispatcher.recently_posted_sort),new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159));
})),ra_contrib_data_key,(function (p1__42693_SHARP_){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(oc.web.utils.activity.parse_contributions(p1__42693_SHARP_,change_data,org_data,next_active_users,follow_publishers_list,oc.web.dispatcher.recent_activity_sort),new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159));
}));
}),next_db_STAR_,cljs.core.keys(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,contributions_list_key)));
var boards_key = oc.web.dispatcher.boards_key(org_slug);
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (tdb,board_key){
var rp_board_data_key = oc.web.dispatcher.board_data_key.cljs$core$IFn$_invoke$arity$3(org_slug,board_key,oc.web.dispatcher.recently_posted_sort);
var ra_board_data_key = oc.web.dispatcher.board_data_key.cljs$core$IFn$_invoke$arity$3(org_slug,board_key,oc.web.dispatcher.recent_activity_sort);
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(tdb,rp_board_data_key,(function (p1__42694_SHARP_){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(oc.web.utils.activity.parse_board(p1__42694_SHARP_,change_data,next_active_users,follow_boards_list,oc.web.dispatcher.recently_posted_sort),new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159));
})),ra_board_data_key,(function (p1__42695_SHARP_){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(oc.web.utils.activity.parse_board(p1__42695_SHARP_,change_data,next_active_users,follow_boards_list,oc.web.dispatcher.recent_activity_sort),new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159));
}));
}),next_db,cljs.core.keys(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,boards_key)));
});
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("follow","loaded","follow/loaded",-111999302),(function (db,p__42704){
var vec__42705 = p__42704;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42705,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42705,(1),null);
var map__42708 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42705,(2),null);
var map__42708__$1 = (((((!((map__42708 == null))))?(((((map__42708.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42708.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42708):map__42708);
var resp = map__42708__$1;
var follow_publisher_uuids = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42708__$1,new cljs.core.Keyword(null,"follow-publisher-uuids","follow-publisher-uuids",-874573180));
var unfollow_board_uuids = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42708__$1,new cljs.core.Keyword(null,"unfollow-board-uuids","unfollow-board-uuids",-201121143));
var user_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42708__$1,new cljs.core.Keyword(null,"user-id","user-id",-206822291));
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(org_slug,new cljs.core.Keyword(null,"org-slug","org-slug",-726595051).cljs$core$IFn$_invoke$arity$1(resp))){
var org_data_key = oc.web.dispatcher.org_data_key(org_slug);
var org_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,org_data_key);
var unfollow_boards_uuids_set = cljs.core.set(unfollow_board_uuids);
var updated_org_data = cljs.core.update.cljs$core$IFn$_invoke$arity$3(org_data,new cljs.core.Keyword(null,"boards","boards",1912049694),(function (boards){
return cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__42703_SHARP_){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__42703_SHARP_,new cljs.core.Keyword(null,"following","following",-2049193617),cljs.core.not((function (){var G__42710 = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__42703_SHARP_);
return (unfollow_boards_uuids_set.cljs$core$IFn$_invoke$arity$1 ? unfollow_boards_uuids_set.cljs$core$IFn$_invoke$arity$1(G__42710) : unfollow_boards_uuids_set.call(null,G__42710));
})()));
}),boards);
}));
var follow_publisher_uuids_set = cljs.core.set(follow_publisher_uuids);
var active_users = oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org_slug,db);
var follow_publishers_list_key = oc.web.dispatcher.follow_publishers_list_key(org_slug);
var follow_boards_list_key = oc.web.dispatcher.follow_boards_list_key(org_slug);
var next_follow_boards_data = oc.web.stores.user.enrich_boards_list(unfollow_board_uuids,new cljs.core.Keyword(null,"boards","boards",1912049694).cljs$core$IFn$_invoke$arity$1(org_data));
var next_follow_publishers_data = oc.web.stores.user.enrich_publishers_list(follow_publisher_uuids,active_users);
return oc.web.stores.user.update_contributions_and_boards(cljs.core.assoc_in(cljs.core.assoc_in(cljs.core.assoc_in(cljs.core.assoc_in(db,org_data_key,updated_org_data),follow_publishers_list_key,next_follow_publishers_data),follow_boards_list_key,next_follow_boards_data),oc.web.dispatcher.unfollow_board_uuids_key(org_slug),unfollow_board_uuids),org_slug,next_follow_boards_data,next_follow_publishers_data);
} else {
return db;
}
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("followers-count","finish","followers-count/finish",1304966571),(function (db,p__42712){
var vec__42713 = p__42712;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42713,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42713,(1),null);
var data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42713,(2),null);
var publisher_uuids = cljs.core.filter.cljs$core$IFn$_invoke$arity$2(oc.web.utils.activity.user_QMARK_,data);
var publishers_map = cljs.core.zipmap(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"resource-uuid","resource-uuid",1798351789),publisher_uuids),publisher_uuids);
var unfollow_boards = cljs.core.filter.cljs$core$IFn$_invoke$arity$2(oc.web.utils.activity.board_QMARK_,data);
var unfollow_boards_map = cljs.core.zipmap(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"resource-uuid","resource-uuid",1798351789),unfollow_boards),unfollow_boards);
var active_users_count = cljs.core.count(oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org_slug,db));
var all_board_uuids = cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),oc.web.stores.user.filter_org_boards(new cljs.core.Keyword(null,"boards","boards",1912049694).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$2(db,org_slug))));
var all_boards_count = cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.merge,cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__42711_SHARP_){
return cljs.core.PersistentHashMap.fromArrays([p1__42711_SHARP_],[new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"resource-uuid","resource-uuid",1798351789),p1__42711_SHARP_,new cljs.core.Keyword(null,"resource-type","resource-type",1844262326),new cljs.core.Keyword(null,"board","board",-1907017633),new cljs.core.Keyword(null,"count","count",2139924085),(function (){var temp__5733__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(unfollow_boards_map,p1__42711_SHARP_);
if(cljs.core.truth_(temp__5733__auto__)){
var unfollow_board = temp__5733__auto__;
return (active_users_count - new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(unfollow_board));
} else {
return active_users_count;
}
})()], null)]);
}),all_board_uuids));
return cljs.core.assoc_in(cljs.core.assoc_in(db,oc.web.dispatcher.followers_publishers_count_key(org_slug),publishers_map),oc.web.dispatcher.followers_boards_count_key(org_slug),all_boards_count);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("publisher","follow","publisher/follow",973499022),(function (db,p__42722){
var vec__42723 = p__42722;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42723,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42723,(1),null);
var map__42726 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42723,(2),null);
var map__42726__$1 = (((((!((map__42726 == null))))?(((((map__42726.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42726.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42726):map__42726);
var resp = map__42726__$1;
var publisher_uuids = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42726__$1,new cljs.core.Keyword(null,"publisher-uuids","publisher-uuids",1855461704));
var follow_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42726__$1,new cljs.core.Keyword(null,"follow?","follow?",474567445));
var publisher_uuid = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42726__$1,new cljs.core.Keyword(null,"publisher-uuid","publisher-uuid",-1907490269));
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(org_slug,new cljs.core.Keyword(null,"org-slug","org-slug",-726595051).cljs$core$IFn$_invoke$arity$1(resp))){
var follow_publishers_list_key = oc.web.dispatcher.follow_publishers_list_key(org_slug);
var active_users = oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org_slug,db);
var next_follow_publishers_data = oc.web.stores.user.enrich_publishers_list(publisher_uuids,active_users);
var followers_count_key = oc.web.dispatcher.followers_publishers_count_key(org_slug);
var publisher_count_key = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(followers_count_key,publisher_uuid);
var fn = ((follow_QMARK_ === true)?cljs.core.inc:((follow_QMARK_ === false)?cljs.core.dec:cljs.core.identity
));
var follow_boards_data = oc.web.dispatcher.follow_boards_list.cljs$core$IFn$_invoke$arity$2(org_slug,db);
return oc.web.stores.user.update_contributions_and_boards(cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc_in(db,follow_publishers_list_key,next_follow_publishers_data),publisher_count_key,(function (p1__42721_SHARP_){
if(cljs.core.truth_(p1__42721_SHARP_)){
return cljs.core.update.cljs$core$IFn$_invoke$arity$3(p1__42721_SHARP_,new cljs.core.Keyword(null,"count","count",2139924085),fn);
} else {
return new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"org-slug","org-slug",-726595051),org_slug,new cljs.core.Keyword(null,"resource-uuid","resource-uuid",1798351789),publisher_uuid,new cljs.core.Keyword(null,"resource-type","resource-type",1844262326),new cljs.core.Keyword(null,"user","user",1532431356),new cljs.core.Keyword(null,"count","count",2139924085),(cljs.core.truth_(follow_QMARK_)?(1):(0))], null);
}
})),org_slug,follow_boards_data,next_follow_publishers_data);
} else {
return db;
}
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("board","follow","board/follow",-915817092),(function (db,p__42730){
var vec__42731 = p__42730;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42731,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42731,(1),null);
var map__42734 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42731,(2),null);
var map__42734__$1 = (((((!((map__42734 == null))))?(((((map__42734.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42734.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42734):map__42734);
var resp = map__42734__$1;
var board_uuids = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42734__$1,new cljs.core.Keyword(null,"board-uuids","board-uuids",515371926));
var follow_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42734__$1,new cljs.core.Keyword(null,"follow?","follow?",474567445));
var board_uuid = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42734__$1,new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127));
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(org_slug,new cljs.core.Keyword(null,"org-slug","org-slug",-726595051).cljs$core$IFn$_invoke$arity$1(resp))){
var follow_boards_list_key = oc.web.dispatcher.follow_boards_list_key(org_slug);
var org_data_key = oc.web.dispatcher.org_data_key(org_slug);
var org_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,org_data_key);
var org_boards = new cljs.core.Keyword(null,"boards","boards",1912049694).cljs$core$IFn$_invoke$arity$1(org_data);
var unfollow_board_uuids_key = oc.web.dispatcher.unfollow_board_uuids_key(org_slug);
var all_board_uuids = cljs.core.set(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),org_boards));
var updated_org_data = cljs.core.update.cljs$core$IFn$_invoke$arity$3(org_data,new cljs.core.Keyword(null,"boards","boards",1912049694),(function (boards){
return cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__42728_SHARP_){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__42728_SHARP_,new cljs.core.Keyword(null,"following","following",-2049193617),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__42728_SHARP_),board_uuid))?follow_QMARK_:new cljs.core.Keyword(null,"following","following",-2049193617).cljs$core$IFn$_invoke$arity$1(p1__42728_SHARP_)));
}),boards);
}));
var next_unfollow_uuids = clojure.set.difference.cljs$core$IFn$_invoke$arity$2(all_board_uuids,cljs.core.set(board_uuids));
var next_follow_boards_data = oc.web.stores.user.enrich_boards_list(next_unfollow_uuids,org_boards);
var followers_count_key = oc.web.dispatcher.followers_boards_count_key(org_slug);
var board_count_key = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(followers_count_key,board_uuid);
var fn = ((follow_QMARK_ === true)?cljs.core.inc:((follow_QMARK_ === false)?cljs.core.dec:cljs.core.identity
));
var follow_publishers_data = oc.web.dispatcher.follow_publishers_list.cljs$core$IFn$_invoke$arity$2(org_slug,db);
return oc.web.stores.user.update_contributions_and_boards(cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc_in(cljs.core.assoc_in(cljs.core.assoc_in(db,follow_boards_list_key,next_follow_boards_data),unfollow_board_uuids_key,next_unfollow_uuids),org_data_key,updated_org_data),board_count_key,(function (p1__42729_SHARP_){
if(cljs.core.truth_(p1__42729_SHARP_)){
return cljs.core.update.cljs$core$IFn$_invoke$arity$3(p1__42729_SHARP_,new cljs.core.Keyword(null,"count","count",2139924085),fn);
} else {
return new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"org-slug","org-slug",-726595051),org_slug,new cljs.core.Keyword(null,"resource-uuid","resource-uuid",1798351789),board_uuid,new cljs.core.Keyword(null,"resource-type","resource-type",1844262326),new cljs.core.Keyword(null,"board","board",-1907017633),new cljs.core.Keyword(null,"count","count",2139924085),(cljs.core.truth_(follow_QMARK_)?(1):(0))], null);
}
})),org_slug,next_follow_boards_data,follow_publishers_data);
} else {
return db;
}
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"follow-list-last-added","follow-list-last-added",782919652),(function (db,p__42741){
var vec__42742 = p__42741;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42742,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42742,(1),null);
var map__42745 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42742,(2),null);
var map__42745__$1 = (((((!((map__42745 == null))))?(((((map__42745.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42745.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42745):map__42745);
var x = map__42745__$1;
var last_added_uuid = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42745__$1,new cljs.core.Keyword(null,"last-added-uuid","last-added-uuid",925482366));
var resource_type = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42745__$1,new cljs.core.Keyword(null,"resource-type","resource-type",1844262326));
var follow_list_last_added_key = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.follow_list_last_added_key(org_slug),resource_type);
return cljs.core.assoc_in(db,follow_list_last_added_key,last_added_uuid);
}));

//# sourceMappingURL=oc.web.stores.user.js.map
