goog.provide('oc.web.components.ui.login_overlay');
oc.web.components.ui.login_overlay.close_overlay = (function oc$web$components$ui$login_overlay$close_overlay(e){
oc.web.lib.utils.event_stop(e);

return oc.web.actions.user.show_login(false);
});
oc.web.components.ui.login_overlay.dont_scroll = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
if(oc.web.stores.user.auth_settings_QMARK_()){
} else {
oc.web.lib.utils.after((100),(function (){
return oc.web.actions.user.auth_settings_get();
}));
}

return s;
}),new cljs.core.Keyword(null,"before-render","before-render",71256781),(function (s){
if(cljs.core.truth_(oc.web.lib.responsive.is_mobile_size_QMARK_())){
var display_none_45995 = ({"display": "none"});
if(cljs.core.truth_(document.querySelector("div.main"))){
goog.style.setStyle(document.querySelector("div.main"),display_none_45995);
} else {
}

if(cljs.core.truth_(document.querySelector("nav.navbar-bottom"))){
goog.style.setStyle(document.querySelector("nav.navbar-bottom"),display_none_45995);
} else {
}

if(cljs.core.truth_(document.querySelector("div.fullscreen-page"))){
goog.style.setStyle(document.querySelector("div.fullscreen-page"),display_none_45995);
} else {
}
} else {
}

return s;
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
if(cljs.core.truth_(oc.web.lib.responsive.is_mobile_size_QMARK_())){
var display_block_45996 = ({"display": "block"});
if(cljs.core.truth_(document.querySelector("div.main"))){
goog.style.setStyle(document.querySelector("div.main"),display_block_45996);
} else {
}

if(cljs.core.truth_(document.querySelector("nav.navbar-bottom"))){
goog.style.setStyle(document.querySelector("nav.navbar-bottom"),display_block_45996);
} else {
}

if(cljs.core.truth_(document.querySelector("div.fullscreen-page"))){
goog.style.setStyle(document.querySelector("div.fullscreen-page"),display_block_45996);
} else {
}
} else {
}

return s;
})], null);
oc.web.components.ui.login_overlay.login_with_email = rum.core.build_defcs((function (state){
var auth_settings = org.martinklepsch.derivatives.react(state,new cljs.core.Keyword(null,"auth-settings","auth-settings",-1775088219));
var login_enabled = (function (){var and__4115__auto__ = auth_settings;
if(cljs.core.truth_(and__4115__auto__)){
return (!((oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(auth_settings),"authenticate","GET",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"auth-source","auth-source",1912135250),"email"], null)], 0)) == null)));
} else {
return and__4115__auto__;
}
})();
var login_action = (function (p1__45956_SHARP_){
if(cljs.core.truth_(login_enabled)){
var login_with_email = new cljs.core.Keyword(null,"login-with-email","login-with-email",-1597480700).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
var email = new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(login_with_email);
var pswd = new cljs.core.Keyword(null,"pswd","pswd",278786885).cljs$core$IFn$_invoke$arity$1(login_with_email);
p1__45956_SHARP_.preventDefault();

oc.web.actions.user.maybe_save_login_redirect();

return oc.web.actions.user.login_with_email(email,pswd);
} else {
return null;
}
});
var login_with_email = org.martinklepsch.derivatives.react(state,new cljs.core.Keyword(null,"login-with-email","login-with-email",-1597480700));
return React.createElement("div",({"onClick": cljs.core.partial.cljs$core$IFn$_invoke$arity$1(oc.web.components.ui.login_overlay.close_overlay), "className": "login-overlay-container group"}),React.createElement("button",({"onClick": cljs.core.partial.cljs$core$IFn$_invoke$arity$1(oc.web.components.ui.login_overlay.close_overlay), "className": "settings-modal-close mlb-reset"})),React.createElement("div",({"onClick": (function (p1__45957_SHARP_){
return oc.web.lib.utils.event_stop(p1__45957_SHARP_);
}), "className": "login-overlay login-with-email group"}),React.createElement("div",({"className": "login-overlay-cta group"}),React.createElement("button",({"onTouchStart": cljs.core.identity, "onClick": (function (){
return oc.web.actions.user.show_login(null);
}), "aria-label": "Back", "className": "mlb-reset top-back-button"})),React.createElement("div",({"className": "sign-in-cta"}),"Sign In"),React.createElement("button",({"aria-label": "Login", "onClick": login_action, "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["mlb-reset","top-continue",(cljs.core.truth_(login_enabled)?null:"disabled")], null))}))),React.createElement("button",({"onClick": (function (p1__45958_SHARP_){
p1__45958_SHARP_.preventDefault();

var temp__5735__auto__ = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(auth_settings),"authenticate","GET",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"auth-source","auth-source",1912135250),"slack"], null)], 0));
if(cljs.core.truth_(temp__5735__auto__)){
var auth_link = temp__5735__auto__;
oc.web.actions.user.maybe_save_login_redirect();

return oc.web.actions.user.login_with_slack(auth_link);
} else {
return null;
}
}), "onTouchStart": cljs.core.identity, "className": "mlb-reset signin-with-slack"}),React.createElement("div",({"className": "signin-with-slack-content"}),React.createElement("div",({"aria-label": "slack", "className": "slack-icon"})),"Continue with Slack")),React.createElement("button",({"onClick": (function (p1__45959_SHARP_){
p1__45959_SHARP_.preventDefault();

var temp__5735__auto__ = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(auth_settings),"authenticate","GET",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"auth-source","auth-source",1912135250),"google"], null)], 0));
if(cljs.core.truth_(temp__5735__auto__)){
var auth_link = temp__5735__auto__;
oc.web.actions.user.maybe_save_login_redirect();

return oc.web.actions.user.login_with_google(auth_link);
} else {
return null;
}
}), "onTouchStart": cljs.core.identity, "className": "mlb-reset signin-with-google"}),React.createElement("div",({"className": "signin-with-google-content"}),React.createElement("div",({"aria-label": "google", "className": "google-icon"})),"Continue with Google")),React.createElement("div",({"className": "or-with-email"}),React.createElement("div",({"className": "or-with-email-copy"}),"Or, sign in with email")),(function (){var attrs45964 = (((new cljs.core.Keyword(null,"login-with-email-error","login-with-email-error",-373631840).cljs$core$IFn$_invoke$arity$1(rum.core.react(oc.web.dispatcher.app_state)) == null))?null:((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"login-with-email-error","login-with-email-error",-373631840).cljs$core$IFn$_invoke$arity$1(rum.core.react(oc.web.dispatcher.app_state)),new cljs.core.Keyword(null,"verify-email","verify-email",464870696)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.small-caps.green","span.small-caps.green",1161015991),"Hey buddy, go verify your email, again, eh?"], null):((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"login-with-email-error","login-with-email-error",-373631840).cljs$core$IFn$_invoke$arity$1(rum.core.react(oc.web.dispatcher.app_state)),(401)))?new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.small-caps.red","span.small-caps.red",-2126101566),"The email or password you entered is incorrect.",new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"br","br",934104792)], null),"Please try again, or ",new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a.underline.red","a.underline.red",-1878793609),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.actions.user.show_login(new cljs.core.Keyword(null,"password-reset","password-reset",1971592302));
})], null),"reset your password"], null),"."], null):new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.small-caps.red","span.small-caps.red",-2126101566),"System troubles logging in.",new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"br","br",934104792)], null),"Please try again, then ",new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a.underline.red","a.underline.red",-1878793609),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"href","href",-793805698),oc.web.urls.contact_mail_to], null),"contact support"], null),"."], null)
)));
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs45964))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["group"], null)], null),attrs45964], 0))):({"className": "group"})),((cljs.core.map_QMARK_(attrs45964))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [React.createElement("form",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["sign-in-form",oc.web.lib.utils.hide_class], null))}),React.createElement("div",({"className": "sign-in-label-container"}),React.createElement("label",({"className": "sign-in-label"}),"Work Email")),React.createElement("div",({"className": "sign-in-field-container"}),sablono.interpreter.create_element("input",({"value": new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(login_with_email), "onChange": (function (p1__45960_SHARP_){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"login-with-email","login-with-email",-1597480700),new cljs.core.Keyword(null,"email","email",1415816706)], null),p1__45960_SHARP_.target.value], null));
}), "type": "email", "autoFocus": true, "tabIndex": (1), "autoCapitalize": "none", "name": "email", "className": "sign-in-field email oc-input"}))),React.createElement("div",({"className": "sign-in-label-container"}),React.createElement("label",({"className": "sign-in-label"}),"Password")),React.createElement("div",({"className": "sign-in-field-container"}),sablono.interpreter.create_element("input",({"value": new cljs.core.Keyword(null,"pswd","pswd",278786885).cljs$core$IFn$_invoke$arity$1(login_with_email), "onChange": (function (p1__45961_SHARP_){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"login-with-email","login-with-email",-1597480700),new cljs.core.Keyword(null,"pswd","pswd",278786885)], null),p1__45961_SHARP_.target.value], null));
}), "type": "password", "tabIndex": (2), "name": "pswd", "className": "sign-in-field pswd oc-input"})),React.createElement("div",({"className": "forgot-password"}),React.createElement("a",({"onClick": (function (){
return oc.web.actions.user.show_login(new cljs.core.Keyword(null,"password-reset","password-reset",1971592302));
})}),"Forgot Password?"))),React.createElement("button",({"onTouchStart": cljs.core.identity, "onClick": login_action, "disabled": ((cljs.core.not(cljs.core.seq(new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(login_with_email)))) || (cljs.core.not(cljs.core.seq(new cljs.core.Keyword(null,"pswd","pswd",278786885).cljs$core$IFn$_invoke$arity$1(login_with_email))))), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["mlb-reset","mlb-default","continue",(cljs.core.truth_(login_enabled)?null:"disabled")], null))}),"Sign In"))], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs45964),React.createElement("form",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["sign-in-form",oc.web.lib.utils.hide_class], null))}),React.createElement("div",({"className": "sign-in-label-container"}),React.createElement("label",({"className": "sign-in-label"}),"Work Email")),React.createElement("div",({"className": "sign-in-field-container"}),sablono.interpreter.create_element("input",({"value": new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(login_with_email), "onChange": (function (p1__45960_SHARP_){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"login-with-email","login-with-email",-1597480700),new cljs.core.Keyword(null,"email","email",1415816706)], null),p1__45960_SHARP_.target.value], null));
}), "type": "email", "autoFocus": true, "tabIndex": (1), "autoCapitalize": "none", "name": "email", "className": "sign-in-field email oc-input"}))),React.createElement("div",({"className": "sign-in-label-container"}),React.createElement("label",({"className": "sign-in-label"}),"Password")),React.createElement("div",({"className": "sign-in-field-container"}),sablono.interpreter.create_element("input",({"value": new cljs.core.Keyword(null,"pswd","pswd",278786885).cljs$core$IFn$_invoke$arity$1(login_with_email), "onChange": (function (p1__45961_SHARP_){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"login-with-email","login-with-email",-1597480700),new cljs.core.Keyword(null,"pswd","pswd",278786885)], null),p1__45961_SHARP_.target.value], null));
}), "type": "password", "tabIndex": (2), "name": "pswd", "className": "sign-in-field pswd oc-input"})),React.createElement("div",({"className": "forgot-password"}),React.createElement("a",({"onClick": (function (){
return oc.web.actions.user.show_login(new cljs.core.Keyword(null,"password-reset","password-reset",1971592302));
})}),"Forgot Password?"))),React.createElement("button",({"onTouchStart": cljs.core.identity, "onClick": login_action, "disabled": ((cljs.core.not(cljs.core.seq(new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(login_with_email)))) || (cljs.core.not(cljs.core.seq(new cljs.core.Keyword(null,"pswd","pswd",278786885).cljs$core$IFn$_invoke$arity$1(login_with_email))))), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["mlb-reset","mlb-default","continue",(cljs.core.truth_(login_enabled)?null:"disabled")], null))}),"Sign In"))], null)));
})(),React.createElement("div",({"className": "footer-link"}),"Don't have an account yet?",React.createElement("a",({"href": oc.web.urls.sign_up, "onClick": (function (e){
oc.web.lib.utils.event_stop(e);

return oc.web.router.nav_BANG_(oc.web.urls.sign_up);
})}),"Sign up here"))));
}),new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,oc.web.components.ui.login_overlay.dont_scroll,oc.web.mixins.ui.no_scroll_mixin,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"auth-settings","auth-settings",-1775088219)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"login-with-email","login-with-email",-1597480700)], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"login-with-email","login-with-email",-1597480700)], null),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"email","email",1415816706),"",new cljs.core.Keyword(null,"pswd","pswd",278786885),""], null)], null));

return s;
}),new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
document.querySelector("input.email").focus();

return s;
})], null)], null),"login-with-email");
oc.web.components.ui.login_overlay.password_reset = rum.core.build_defcs((function (state){
var auth_settings = org.martinklepsch.derivatives.react(state,new cljs.core.Keyword(null,"auth-settings","auth-settings",-1775088219));
return React.createElement("div",({"onClick": cljs.core.partial.cljs$core$IFn$_invoke$arity$1(oc.web.components.ui.login_overlay.close_overlay), "className": "login-overlay-container group"}),React.createElement("button",({"onClick": cljs.core.partial.cljs$core$IFn$_invoke$arity$1(oc.web.components.ui.login_overlay.close_overlay), "className": "settings-modal-close mlb-reset"})),React.createElement("div",({"onClick": (function (p1__45979_SHARP_){
return oc.web.lib.utils.event_stop(p1__45979_SHARP_);
}), "className": "login-overlay password-reset"}),React.createElement("div",({"className": "login-overlay-cta group"}),React.createElement("div",({"className": "sign-in-cta"}),"Password Reset",sablono.interpreter.interpret((cljs.core.truth_(auth_settings)?null:(oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.small_loading.small_loading.call(null)))))),(function (){var attrs45981 = ((cljs.core.contains_QMARK_(new cljs.core.Keyword(null,"password-reset","password-reset",1971592302).cljs$core$IFn$_invoke$arity$1(rum.core.react(oc.web.dispatcher.app_state)),new cljs.core.Keyword(null,"success","success",1890645906)))?(cljs.core.truth_(new cljs.core.Keyword(null,"success","success",1890645906).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"password-reset","password-reset",1971592302).cljs$core$IFn$_invoke$arity$1(rum.core.react(oc.web.dispatcher.app_state))))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.sent-email-copy","div.sent-email-copy",836048288),"We sent you an email with the instructions to reset your account password."], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.error-copy","div.error-copy",-1605940447),"An error occurred, please try again."], null)
):null);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs45981))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, ["pt2","pl3","pr3","pb2","group"], null)], null),attrs45981], 0))):({"className": "pt2 pl3 pr3 pb2 group"})),((cljs.core.map_QMARK_(attrs45981))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [React.createElement("form",({"className": "sign-in-form"}),React.createElement("div",({"className": "sign-in-field-container email"}),sablono.interpreter.create_element("input",({"tabIndex": (1), "placeholder": "Please enter your email address", "autoFocus": true, "name": "email", "value": new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"password-reset","password-reset",1971592302).cljs$core$IFn$_invoke$arity$1(rum.core.react(oc.web.dispatcher.app_state))), "type": "email", "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["sign-in-field","oc-input",oc.web.lib.utils.hide_class], null)), "onChange": (function (p1__45980_SHARP_){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"password-reset","password-reset",1971592302),new cljs.core.Keyword(null,"email","email",1415816706)], null),p1__45980_SHARP_.target.value], null));
}), "autoCapitalize": "none"}))),(cljs.core.truth_(new cljs.core.Keyword(null,"success","success",1890645906).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"password-reset","password-reset",1971592302).cljs$core$IFn$_invoke$arity$1(rum.core.react(oc.web.dispatcher.app_state))))?React.createElement("div",({"className": "group pb3 mt1"}),React.createElement("button",({"onClick": (function (){
return oc.web.actions.user.show_login(null);
}), "className": "mlb-reset continue"}),"Done")):React.createElement("div",({"className": "group"}),React.createElement("button",({"onClick": (function (){
return oc.web.actions.user.password_reset(new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"password-reset","password-reset",1971592302).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state))));
}), "disabled": cljs.core.not(oc.web.lib.utils.valid_email_QMARK_(new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"password-reset","password-reset",1971592302).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state))))), "onTouchStart": cljs.core.identity, "className": "mlb-reset continue"}),"Reset Password"),React.createElement("button",({"onClick": (function (){
return oc.web.actions.user.show_login(null);
}), "disabled": cljs.core.not(auth_settings), "onTouchStart": cljs.core.identity, "className": "mlb-reset cancel-bt"}),"Cancel"))))], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs45981),React.createElement("form",({"className": "sign-in-form"}),React.createElement("div",({"className": "sign-in-field-container email"}),sablono.interpreter.create_element("input",({"tabIndex": (1), "placeholder": "Please enter your email address", "autoFocus": true, "name": "email", "value": new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"password-reset","password-reset",1971592302).cljs$core$IFn$_invoke$arity$1(rum.core.react(oc.web.dispatcher.app_state))), "type": "email", "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["sign-in-field","oc-input",oc.web.lib.utils.hide_class], null)), "onChange": (function (p1__45980_SHARP_){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"password-reset","password-reset",1971592302),new cljs.core.Keyword(null,"email","email",1415816706)], null),p1__45980_SHARP_.target.value], null));
}), "autoCapitalize": "none"}))),(cljs.core.truth_(new cljs.core.Keyword(null,"success","success",1890645906).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"password-reset","password-reset",1971592302).cljs$core$IFn$_invoke$arity$1(rum.core.react(oc.web.dispatcher.app_state))))?React.createElement("div",({"className": "group pb3 mt1"}),React.createElement("button",({"onClick": (function (){
return oc.web.actions.user.show_login(null);
}), "className": "mlb-reset continue"}),"Done")):React.createElement("div",({"className": "group"}),React.createElement("button",({"onClick": (function (){
return oc.web.actions.user.password_reset(new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"password-reset","password-reset",1971592302).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state))));
}), "disabled": cljs.core.not(oc.web.lib.utils.valid_email_QMARK_(new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"password-reset","password-reset",1971592302).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state))))), "onTouchStart": cljs.core.identity, "className": "mlb-reset continue"}),"Reset Password"),React.createElement("button",({"onClick": (function (){
return oc.web.actions.user.show_login(null);
}), "disabled": cljs.core.not(auth_settings), "onTouchStart": cljs.core.identity, "className": "mlb-reset cancel-bt"}),"Cancel"))))], null)));
})()));
}),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,oc.web.components.ui.login_overlay.dont_scroll,oc.web.mixins.ui.no_scroll_mixin,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"auth-settings","auth-settings",-1775088219)], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"password-reset","password-reset",1971592302)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"email","email",1415816706),""], null)], null));

return s;
}),new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
document.querySelector("div.sign-in-field-container.email").focus();

return s;
})], null)], null),"password-reset");
oc.web.components.ui.login_overlay.collect_password = rum.core.build_defcs((function (state){
var auth_settings = org.martinklepsch.derivatives.react(state,new cljs.core.Keyword(null,"auth-settings","auth-settings",-1775088219));
return React.createElement("div",({"onClick": (function (p1__45982_SHARP_){
return oc.web.lib.utils.event_stop(p1__45982_SHARP_);
}), "className": "login-overlay-container group"}),React.createElement("div",({"className": "login-overlay collect-pswd group"}),React.createElement("div",({"className": "login-overlay-cta pl2 pr2 group"}),React.createElement("div",({"className": "sign-in-cta"}),"Enter your new password",sablono.interpreter.interpret((cljs.core.truth_(auth_settings)?null:(oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.small_loading.small_loading.call(null)))))),(function (){var attrs45985 = (((new cljs.core.Keyword(null,"collect-password-error","collect-password-error",-1137727549).cljs$core$IFn$_invoke$arity$1(rum.core.react(oc.web.dispatcher.app_state)) == null))?null:new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.small-caps.red","span.small-caps.red",-2126101566),"System troubles logging in.",new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"br","br",934104792)], null),"Please try again, then ",new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a.underline.red","a.underline.red",-1878793609),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"href","href",-793805698),oc.web.urls.contact_mail_to], null),"contact support"], null),"."], null));
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs45985))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, ["pt2","pl3","pr3","pb2","group"], null)], null),attrs45985], 0))):({"className": "pt2 pl3 pr3 pb2 group"})),((cljs.core.map_QMARK_(attrs45985))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [React.createElement("form",({"className": "sign-in-form"}),React.createElement("div",({"className": "sign-in-label-container"}),React.createElement("label",({"className": "sign-in-label", "htmlFor": "signup-pswd"}),"Password")),React.createElement("div",({"className": "sign-in-field-container"}),sablono.interpreter.create_element("input",({"tabIndex": (4), "placeholder": "Minimum 8 characters", "name": "pswd", "value": new cljs.core.Keyword(null,"pswd","pswd",278786885).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collect-pswd","collect-pswd",-1287645874).cljs$core$IFn$_invoke$arity$1(rum.core.react(oc.web.dispatcher.app_state))), "type": "password", "className": "sign-in-field pswd oc-input", "id": "collect-pswd-pswd", "onChange": (function (p1__45983_SHARP_){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"collect-pswd","collect-pswd",-1287645874),new cljs.core.Keyword(null,"pswd","pswd",278786885)], null),p1__45983_SHARP_.target.value], null));
}), "pattern": ".{8,}"}))),React.createElement("button",({"disabled": (cljs.core.count(new cljs.core.Keyword(null,"pswd","pswd",278786885).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collect-pswd","collect-pswd",-1287645874).cljs$core$IFn$_invoke$arity$1(rum.core.react(oc.web.dispatcher.app_state)))) < (8)), "onClick": (function (p1__45984_SHARP_){
oc.web.lib.utils.event_stop(p1__45984_SHARP_);

return oc.web.actions.user.pswd_collect(new cljs.core.Keyword(null,"collect-pswd","collect-pswd",-1287645874).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state)),true);
}), "onTouchStart": cljs.core.identity, "className": "mlb-reset mlb-default continue"}),"Let Me In"))], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs45985),React.createElement("form",({"className": "sign-in-form"}),React.createElement("div",({"className": "sign-in-label-container"}),React.createElement("label",({"className": "sign-in-label", "htmlFor": "signup-pswd"}),"Password")),React.createElement("div",({"className": "sign-in-field-container"}),sablono.interpreter.create_element("input",({"tabIndex": (4), "placeholder": "Minimum 8 characters", "name": "pswd", "value": new cljs.core.Keyword(null,"pswd","pswd",278786885).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collect-pswd","collect-pswd",-1287645874).cljs$core$IFn$_invoke$arity$1(rum.core.react(oc.web.dispatcher.app_state))), "type": "password", "className": "sign-in-field pswd oc-input", "id": "collect-pswd-pswd", "onChange": (function (p1__45983_SHARP_){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"collect-pswd","collect-pswd",-1287645874),new cljs.core.Keyword(null,"pswd","pswd",278786885)], null),p1__45983_SHARP_.target.value], null));
}), "pattern": ".{8,}"}))),React.createElement("button",({"disabled": (cljs.core.count(new cljs.core.Keyword(null,"pswd","pswd",278786885).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collect-pswd","collect-pswd",-1287645874).cljs$core$IFn$_invoke$arity$1(rum.core.react(oc.web.dispatcher.app_state)))) < (8)), "onClick": (function (p1__45984_SHARP_){
oc.web.lib.utils.event_stop(p1__45984_SHARP_);

return oc.web.actions.user.pswd_collect(new cljs.core.Keyword(null,"collect-pswd","collect-pswd",-1287645874).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state)),true);
}), "onTouchStart": cljs.core.identity, "className": "mlb-reset mlb-default continue"}),"Let Me In"))], null)));
})()));
}),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,oc.web.components.ui.login_overlay.dont_scroll,oc.web.mixins.ui.no_scroll_mixin,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"auth-settings","auth-settings",-1775088219)], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"collect-pswd","collect-pswd",-1287645874)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"pswd","pswd",278786885),""], null)], null));

return s;
}),new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
oc.web.lib.utils.after((500),(function (){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"collect-pswd","collect-pswd",-1287645874)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"pswd","pswd",278786885),(function (){var or__4126__auto__ = new cljs.core.Keyword(null,"pswd","pswd",278786885).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collect-pswd","collect-pswd",-1287645874).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state)));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "";
}
})()], null)], null));
}));

oc.web.lib.utils.after((1000),(function (){
var temp__5735__auto__ = document.querySelector("input.sign-in-field.pswd");
if(cljs.core.truth_(temp__5735__auto__)){
var pswd_el = temp__5735__auto__;
return pswd_el.focus();
} else {
return null;
}
}));

return s;
})], null)], null),"collect-password");
oc.web.components.ui.login_overlay.login_overlays_handler = rum.core.build_defcs((function (s){
return sablono.interpreter.interpret(((((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(org.martinklepsch.derivatives.react(s,oc.web.dispatcher.show_login_overlay_key),new cljs.core.Keyword(null,"login-with-email","login-with-email",-1597480700))) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(org.martinklepsch.derivatives.react(s,oc.web.dispatcher.show_login_overlay_key),new cljs.core.Keyword(null,"login-with-slack","login-with-slack",1915911677)))))?(oc.web.components.ui.login_overlay.login_with_email.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.login_overlay.login_with_email.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.login_overlay.login_with_email.call(null)):((((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(org.martinklepsch.derivatives.react(s,oc.web.dispatcher.show_login_overlay_key),new cljs.core.Keyword(null,"signup-with-email","signup-with-email",-22609037))) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(org.martinklepsch.derivatives.react(s,oc.web.dispatcher.show_login_overlay_key),new cljs.core.Keyword(null,"signup-with-slack","signup-with-slack",-665887243)))))?(function (){
oc.web.lib.utils.after((150),(function (){
return oc.web.router.nav_BANG_(oc.web.urls.sign_up);
}));

return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632)], null);
})()
:((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(org.martinklepsch.derivatives.react(s,oc.web.dispatcher.show_login_overlay_key),new cljs.core.Keyword(null,"password-reset","password-reset",1971592302)))?(oc.web.components.ui.login_overlay.password_reset.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.login_overlay.password_reset.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.login_overlay.password_reset.call(null)):((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(org.martinklepsch.derivatives.react(s,oc.web.dispatcher.show_login_overlay_key),new cljs.core.Keyword(null,"collect-password","collect-password",690937675)))?(oc.web.components.ui.login_overlay.collect_password.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.login_overlay.collect_password.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.login_overlay.collect_password.call(null)):new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.hidden","div.hidden",-470755407)], null)
)))));
}),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$,rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([oc.web.stores.user.show_login_overlay_QMARK_], 0))], null),"login-overlays-handler");

//# sourceMappingURL=oc.web.components.ui.login_overlay.js.map
