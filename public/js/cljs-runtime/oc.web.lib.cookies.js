goog.provide('oc.web.lib.cookies');
oc.web.lib.cookies.default_cookie_expire = ((((60) * (60)) * (24)) * (6));
oc.web.lib.cookies.cookies_static_obj = (new goog.net.Cookies(document));
oc.web.lib.cookies.cookie_name = (function oc$web$lib$cookies$cookie_name(c_name){
return [oc.web.local_settings.cookie_name_prefix,cljs.core.name(c_name)].join('');
});
oc.web.lib.cookies.set_cookie_BANG_ = (function oc$web$lib$cookies$set_cookie_BANG_(var_args){
var G__39149 = arguments.length;
switch (G__39149) {
case 2:
return oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 4:
return oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
case 6:
return oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$6((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]),(arguments[(5)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});
goog.exportSymbol('oc.web.lib.cookies.set_cookie_BANG_', oc.web.lib.cookies.set_cookie_BANG_);

(oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (c_name,c_value){
return oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$3(c_name,c_value,(-1));
}));

(oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$3 = (function (c_name,c_value,expiry){
return oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$4(c_name,c_value,expiry,"/");
}));

(oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$4 = (function (c_name,c_value,expiry,c_path){
return oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$6(c_name,c_value,expiry,c_path,oc.web.local_settings.jwt_cookie_domain,oc.web.local_settings.jwt_cookie_secure);
}));

(oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$6 = (function (c_name,c_value,expiry,c_path,c_domain,c_secure){
return oc.web.lib.cookies.cookies_static_obj.set(oc.web.lib.cookies.cookie_name(c_name),c_value,expiry,c_path,c_domain,c_secure);
}));

(oc.web.lib.cookies.set_cookie_BANG_.cljs$lang$maxFixedArity = 6);

/**
 * Get a cookie with the name provided pre-fixed by the environment.
 */
oc.web.lib.cookies.get_cookie = (function oc$web$lib$cookies$get_cookie(c_name){
return oc.web.lib.cookies.cookies_static_obj.get(oc.web.lib.cookies.cookie_name(c_name));
});
goog.exportSymbol('oc.web.lib.cookies.get_cookie', oc.web.lib.cookies.get_cookie);
/**
 * Remove a cookie with the name provided pre-fixed by the environment.
 */
oc.web.lib.cookies.remove_cookie_BANG_ = (function oc$web$lib$cookies$remove_cookie_BANG_(var_args){
var G__39151 = arguments.length;
switch (G__39151) {
case 1:
return oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$1 = (function (c_name){
return oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$2(cljs.core.name(c_name),"/");
}));

(oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (c_name,opt_path){
return oc.web.lib.cookies.cookies_static_obj.remove(oc.web.lib.cookies.cookie_name(c_name),opt_path,oc.web.local_settings.jwt_cookie_domain);
}));

(oc.web.lib.cookies.remove_cookie_BANG_.cljs$lang$maxFixedArity = 2);


//# sourceMappingURL=oc.web.lib.cookies.js.map
