goog.provide('oc.web.actions.jwt');
oc.web.actions.jwt.logout = (function oc$web$actions$jwt$logout(var_args){
var G__41906 = arguments.length;
switch (G__41906) {
case 0:
return oc.web.actions.jwt.logout.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.actions.jwt.logout.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.jwt.logout.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.actions.jwt.logout.cljs$core$IFn$_invoke$arity$1(((oc.shared.useragent.pseudo_native_QMARK_)?oc.web.urls.native_login:oc.web.urls.home));
}));

(oc.web.actions.jwt.logout.cljs$core$IFn$_invoke$arity$1 = (function (location){
oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"jwt","jwt",1504015441));

oc.web.router.redirect_BANG_(location);

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"logout","logout",1418564329)], null));
}));

(oc.web.actions.jwt.logout.cljs$lang$maxFixedArity = 1);

oc.web.actions.jwt.dispatch_id_token = (function oc$web$actions$jwt$dispatch_id_token(){
var temp__5735__auto__ = oc.web.lib.jwt.get_id_token_contents.cljs$core$IFn$_invoke$arity$0();
if(cljs.core.truth_(temp__5735__auto__)){
var id_token_contents = temp__5735__auto__;
oc.web.lib.utils.after((1),(function (){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"id-token","id-token",-339268306),id_token_contents], null));
}));

if(cljs.core.truth_(id_token_contents)){
return oc.web.lib.fullstory.identify();
} else {
return null;
}
} else {
return null;
}
});
oc.web.actions.jwt.update_id_token_cookie = (function oc$web$actions$jwt$update_id_token_cookie(id_token){
return oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$6(new cljs.core.Keyword(null,"id-token","id-token",-339268306),id_token,(-1),window.location.pathname,oc.web.local_settings.jwt_cookie_domain,oc.web.local_settings.jwt_cookie_secure);
});
oc.web.actions.jwt.update_id_token = (function oc$web$actions$jwt$update_id_token(token_body){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.actions.jwt",null,39,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Updating id-token:",token_body], null);
}),null)),null,-383723522);

if(cljs.core.truth_(token_body)){
oc.web.actions.jwt.update_id_token_cookie(token_body);

return oc.web.actions.jwt.dispatch_id_token();
} else {
return null;
}
});
oc.web.actions.jwt.update_jwt_cookie = (function oc$web$actions$jwt$update_jwt_cookie(jwt){
return oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$6(new cljs.core.Keyword(null,"jwt","jwt",1504015441),jwt,((((60) * (60)) * (24)) * (60)),"/",oc.web.local_settings.jwt_cookie_domain,oc.web.local_settings.jwt_cookie_secure);
});
oc.web.actions.jwt.dispatch_jwt = (function oc$web$actions$jwt$dispatch_jwt(){
if(cljs.core.truth_((function (){var and__4115__auto__ = oc.web.lib.cookies.get_cookie(new cljs.core.Keyword(null,"show-login-overlay","show-login-overlay",1026669411));
if(cljs.core.truth_(and__4115__auto__)){
return ((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.lib.cookies.get_cookie(new cljs.core.Keyword(null,"show-login-overlay","show-login-overlay",1026669411)),"collect-name-password")) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.lib.cookies.get_cookie(new cljs.core.Keyword(null,"show-login-overlay","show-login-overlay",1026669411)),"collect-password")));
} else {
return and__4115__auto__;
}
})())){
oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"show-login-overlay","show-login-overlay",1026669411));
} else {
}

var jwt_contents = oc.web.lib.jwt.get_contents();
oc.web.lib.utils.after((1),(function (){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"jwt","jwt",1504015441),jwt_contents], null));
}));

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [oc.web.dispatcher.show_invite_box_key], null),cljs.core.seq(oc.web.lib.cookies.get_cookie(oc.web.router.show_invite_box_cookie(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(jwt_contents))))], null));

if(cljs.core.truth_(jwt_contents)){
return oc.web.lib.fullstory.identify();
} else {
return null;
}
});
oc.web.actions.jwt.update_jwt = (function oc$web$actions$jwt$update_jwt(jbody){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.actions.jwt",null,62,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Updating jwt:",jbody], null);
}),null)),null,1074503996);

if(cljs.core.truth_(jbody)){
oc.web.actions.jwt.update_jwt_cookie(jbody);

return oc.web.actions.jwt.dispatch_jwt();
} else {
return null;
}
});
oc.web.actions.jwt.jwt_refresh = (function oc$web$actions$jwt$jwt_refresh(var_args){
var G__41925 = arguments.length;
switch (G__41925) {
case 0:
return oc.web.actions.jwt.jwt_refresh.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.actions.jwt.jwt_refresh.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.jwt.jwt_refresh.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.api.jwt_refresh(oc.web.actions.jwt.update_jwt,oc.web.actions.jwt.logout);
}));

(oc.web.actions.jwt.jwt_refresh.cljs$core$IFn$_invoke$arity$1 = (function (success_cb){
return oc.web.api.jwt_refresh((function (p1__41923_SHARP_){
oc.web.actions.jwt.update_jwt(p1__41923_SHARP_);

return (success_cb.cljs$core$IFn$_invoke$arity$0 ? success_cb.cljs$core$IFn$_invoke$arity$0() : success_cb.call(null));
}),oc.web.actions.jwt.logout);
}));

(oc.web.actions.jwt.jwt_refresh.cljs$lang$maxFixedArity = 1);


//# sourceMappingURL=oc.web.actions.jwt.js.map
