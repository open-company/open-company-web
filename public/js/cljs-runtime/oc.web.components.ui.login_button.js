goog.provide('oc.web.components.ui.login_button');
oc.web.components.ui.login_button.login_button = rum.core.build_defc((function (){
var is_mobile_QMARK_ = oc.web.lib.responsive.is_mobile_size_QMARK_();
return React.createElement("div",({"className": "login-button-container group"}),React.createElement("a",({"href": oc.web.urls.login, "onClick": (function (p1__39529_SHARP_){
oc.web.lib.utils.event_stop(p1__39529_SHARP_);

return oc.web.actions.user.show_login(new cljs.core.Keyword(null,"login-with-email","login-with-email",-1597480700));
}), "className": "login-bt"}),"Log in"),React.createElement("span",({"className": "or"})," or "),React.createElement("a",({"href": oc.web.urls.sign_up, "onClick": (function (p1__39530_SHARP_){
oc.web.lib.utils.event_stop(p1__39530_SHARP_);

return oc.web.router.nav_BANG_(oc.web.urls.sign_up);
}), "className": "signup-bt"}),"Sign up"));
}),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$,rum.core.reactive,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
if(oc.web.lib.utils.is_test_env_QMARK_()){
} else {
oc.web.actions.user.auth_settings_get();
}

return s;
})], null)], null),"login-button");

//# sourceMappingURL=oc.web.components.ui.login_button.js.map
