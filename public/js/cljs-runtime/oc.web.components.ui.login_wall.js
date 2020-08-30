goog.provide('oc.web.components.ui.login_wall');
oc.web.components.ui.login_wall.default_title = "Login to Wut";
oc.web.components.ui.login_wall.default_desc = "You need to be logged in to view a post.";
oc.web.components.ui.login_wall.login_wall = rum.core.build_defcs((function (s,p__44413){
var map__44414 = p__44413;
var map__44414__$1 = (((((!((map__44414 == null))))?(((((map__44414.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__44414.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__44414):map__44414);
var title = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__44414__$1,new cljs.core.Keyword(null,"title","title",636505583));
var desc = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__44414__$1,new cljs.core.Keyword(null,"desc","desc",2093485764));
var auth_settings = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"auth-settings","auth-settings",-1775088219));
var deep_link_origin = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"expo-deep-link-origin","expo-deep-link-origin",404078070));
var email_auth_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(auth_settings),"authenticate","GET",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"auth-source","auth-source",1912135250),"email"], null)], 0));
var login_enabled = (function (){var and__4115__auto__ = auth_settings;
if(cljs.core.truth_(and__4115__auto__)){
return ((cljs.core.map_QMARK_(email_auth_link)) && (cljs.core.seq(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.login-wall","email","oc.web.components.ui.login-wall/email",-271375922).cljs$core$IFn$_invoke$arity$1(s)))) && (cljs.core.seq(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.login-wall","pswd","oc.web.components.ui.login-wall/pswd",-267749663).cljs$core$IFn$_invoke$arity$1(s)))));
} else {
return and__4115__auto__;
}
})();
var login_action = (function (p1__44407_SHARP_){
if(cljs.core.truth_(login_enabled)){
p1__44407_SHARP_.preventDefault();

oc.web.actions.user.maybe_save_login_redirect();

return oc.web.actions.user.login_with_email(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.login-wall","email","oc.web.components.ui.login-wall/email",-271375922).cljs$core$IFn$_invoke$arity$1(s)),cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.login-wall","pswd","oc.web.components.ui.login-wall/pswd",-267749663).cljs$core$IFn$_invoke$arity$1(s)));
} else {
return null;
}
});
var login_with_email_error = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"login-with-email-error","login-with-email-error",-373631840));
if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
var attrs44417 = (oc.web.components.ui.loading.loading.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.loading.loading.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.loading.loading.call(null));
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs44417))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["login-wall-container"], null)], null),attrs44417], 0))):({"className": "login-wall-container"})),((cljs.core.map_QMARK_(attrs44417))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs44417)], null)));
} else {
var attrs44418 = (oc.web.components.ui.login_overlay.login_overlays_handler.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.login_overlay.login_overlays_handler.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.login_overlay.login_overlays_handler.call(null));
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs44418))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["login-wall-container"], null)], null),attrs44418], 0))):({"className": "login-wall-container"})),((cljs.core.map_QMARK_(attrs44418))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [React.createElement("header",({"className": "login-wall-header"}),React.createElement("button",({"onTouchStart": cljs.core.identity, "onClick": (function (p1__44408_SHARP_){
if(oc.shared.useragent.mobile_app_QMARK_){
return oc.web.router.redirect_BANG_(oc.web.urls.home);
} else {
p1__44408_SHARP_.preventDefault();

return oc.web.router.redirect_BANG_(oc.web.urls.home);
}
}), "aria-label": "Back", "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["mlb-reset","top-back-button",((oc.shared.useragent.mobile_app_QMARK_)?"mobile-app":null)], null))}),"Back"),(function (){var attrs44426 = (function (){var or__4126__auto__ = title;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.components.ui.login_wall.default_title;
}
})();
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs44426))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["title"], null)], null),attrs44426], 0))):({"className": "title"})),((cljs.core.map_QMARK_(attrs44426))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs44426)], null)));
})(),React.createElement("button",({"onClick": login_action, "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["mlb-reset","top-continue",(cljs.core.truth_(login_enabled)?null:"disabled")], null))}),"Log in")),React.createElement("div",({"className": "login-wall-wrapper"}),React.createElement("div",({"className": "login-wall-internal"}),React.createElement("div",({"className": "login-wall-content"}),React.createElement("div",({"className": "login-overlay-cta group"}),React.createElement("div",({"className": "login-title"}),"Log in")),sablono.interpreter.interpret(((cljs.core.seq((function (){var or__4126__auto__ = desc;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.components.ui.login_wall.default_desc;
}
})()))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.login-description","div.login-description",1471492458),(function (){var or__4126__auto__ = desc;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.components.ui.login_wall.default_desc;
}
})()], null):null)),React.createElement("div",({"className": "login-buttons group"}),React.createElement("button",({"onTouchStart": cljs.core.identity, "onClick": (function (p1__44409_SHARP_){
p1__44409_SHARP_.preventDefault();

var temp__5735__auto__ = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(auth_settings),"authenticate","GET",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"auth-source","auth-source",1912135250),"slack"], null)], 0));
if(cljs.core.truth_(temp__5735__auto__)){
var auth_link = temp__5735__auto__;
oc.web.actions.user.maybe_save_login_redirect();

return oc.web.actions.user.login_with_slack.cljs$core$IFn$_invoke$arity$variadic(auth_link,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([((oc.shared.useragent.mobile_app_QMARK_)?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"redirect-origin","redirect-origin",1138035106),deep_link_origin], null):null)], 0));
} else {
return null;
}
}), "className": "mlb-reset signup-with-slack"}),React.createElement("div",({"aria-label": "slack", "className": "slack-icon"})),React.createElement("div",({"className": "slack-text"}),"Slack")),React.createElement("button",({"onTouchStart": cljs.core.identity, "onClick": (function (p1__44410_SHARP_){
p1__44410_SHARP_.preventDefault();

var temp__5735__auto__ = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(auth_settings),"authenticate","GET",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"auth-source","auth-source",1912135250),"google"], null)], 0));
if(cljs.core.truth_(temp__5735__auto__)){
var auth_link = temp__5735__auto__;
oc.web.actions.user.maybe_save_login_redirect();

return oc.web.actions.user.login_with_google.cljs$core$IFn$_invoke$arity$variadic(auth_link,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([((oc.shared.useragent.mobile_app_QMARK_)?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"redirect-origin","redirect-origin",1138035106),deep_link_origin], null):null)], 0));
} else {
return null;
}
}), "className": "mlb-reset signup-with-google"}),React.createElement("div",({"aria-label": "google", "className": "google-icon"})),React.createElement("div",({"className": "google-text"}),"Google"))),React.createElement("div",({"className": "or-login"}),React.createElement("div",({"className": "or-login-copy"}),"Or, login with email")),(function (){var attrs44435 = (((login_with_email_error == null))?null:((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(login_with_email_error,new cljs.core.Keyword(null,"verify-email","verify-email",464870696)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.small-caps.green","span.small-caps.green",1161015991),"Hey buddy, go verify your email, again, eh?"], null):((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(login_with_email_error,(401)))?new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.small-caps.red","span.small-caps.red",-2126101566),"The email or password you entered is incorrect.",new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"br","br",934104792)], null),"Please try again, or ",new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a.underline.red","a.underline.red",-1878793609),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.actions.user.show_login(new cljs.core.Keyword(null,"password-reset","password-reset",1971592302));
})], null),"reset your password"], null),"."], null):new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.small-caps.red","span.small-caps.red",-2126101566),"System troubles logging in.",new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"br","br",934104792)], null),"Please try again, then ",new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a.underline.red","a.underline.red",-1878793609),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"href","href",-793805698),oc.web.urls.contact_mail_to], null),"contact support"], null),"."], null)
)));
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs44435))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["group"], null)], null),attrs44435], 0))):({"className": "group"})),((cljs.core.map_QMARK_(attrs44435))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [React.createElement("form",({"className": "sign-in-form"}),React.createElement("div",({"className": "fields-container group"}),React.createElement("div",({"className": "field-label"}),"Work email"),sablono.interpreter.create_element("input",({"type": "email", "name": "email", "value": cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.login-wall","email","oc.web.components.ui.login-wall/email",-271375922).cljs$core$IFn$_invoke$arity$1(s)), "onChange": (function (p1__44411_SHARP_){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.login-wall","email","oc.web.components.ui.login-wall/email",-271375922).cljs$core$IFn$_invoke$arity$1(s),p1__44411_SHARP_.target.value);
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["field-content","email",oc.web.lib.utils.hide_class], null))})),React.createElement("div",({"className": "field-label"}),"Password"),sablono.interpreter.create_element("input",({"type": "password", "name": "password", "value": cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.login-wall","pswd","oc.web.components.ui.login-wall/pswd",-267749663).cljs$core$IFn$_invoke$arity$1(s)), "onChange": (function (p1__44412_SHARP_){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.login-wall","pswd","oc.web.components.ui.login-wall/pswd",-267749663).cljs$core$IFn$_invoke$arity$1(s),p1__44412_SHARP_.target.value);
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["field-content","password",oc.web.lib.utils.hide_class], null))})),React.createElement("div",({"className": "forgot-password"}),React.createElement("a",({"onClick": (function (){
return oc.web.actions.user.show_login(new cljs.core.Keyword(null,"password-reset","password-reset",1971592302));
})}),"Forgot password?"))),React.createElement("button",({"aria-label": "Login", "disabled": cljs.core.not(login_enabled), "onClick": login_action, "className": "mlb-reset continue-btn"}),"Log in"))], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs44435),React.createElement("form",({"className": "sign-in-form"}),React.createElement("div",({"className": "fields-container group"}),React.createElement("div",({"className": "field-label"}),"Work email"),sablono.interpreter.create_element("input",({"type": "email", "name": "email", "value": cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.login-wall","email","oc.web.components.ui.login-wall/email",-271375922).cljs$core$IFn$_invoke$arity$1(s)), "onChange": (function (p1__44411_SHARP_){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.login-wall","email","oc.web.components.ui.login-wall/email",-271375922).cljs$core$IFn$_invoke$arity$1(s),p1__44411_SHARP_.target.value);
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["field-content","email",oc.web.lib.utils.hide_class], null))})),React.createElement("div",({"className": "field-label"}),"Password"),sablono.interpreter.create_element("input",({"type": "password", "name": "password", "value": cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.login-wall","pswd","oc.web.components.ui.login-wall/pswd",-267749663).cljs$core$IFn$_invoke$arity$1(s)), "onChange": (function (p1__44412_SHARP_){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.login-wall","pswd","oc.web.components.ui.login-wall/pswd",-267749663).cljs$core$IFn$_invoke$arity$1(s),p1__44412_SHARP_.target.value);
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["field-content","password",oc.web.lib.utils.hide_class], null))})),React.createElement("div",({"className": "forgot-password"}),React.createElement("a",({"onClick": (function (){
return oc.web.actions.user.show_login(new cljs.core.Keyword(null,"password-reset","password-reset",1971592302));
})}),"Forgot password?"))),React.createElement("button",({"aria-label": "Login", "disabled": cljs.core.not(login_enabled), "onClick": login_action, "className": "mlb-reset continue-btn"}),"Log in"))], null)));
})())),React.createElement("div",({"className": "footer-link"}),"Don't have an account yet?",React.createElement("div",({"className": "footer-link-inner"}),React.createElement("a",({"href": oc.web.urls.sign_up, "onClick": (function (e){
oc.web.lib.utils.event_stop(e);

return oc.web.router.nav_BANG_(oc.web.urls.sign_up);
})}),"Sign up here"))))], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs44418),React.createElement("header",({"className": "login-wall-header"}),React.createElement("button",({"onTouchStart": cljs.core.identity, "onClick": (function (p1__44408_SHARP_){
if(oc.shared.useragent.mobile_app_QMARK_){
return oc.web.router.redirect_BANG_(oc.web.urls.home);
} else {
p1__44408_SHARP_.preventDefault();

return oc.web.router.redirect_BANG_(oc.web.urls.home);
}
}), "aria-label": "Back", "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["mlb-reset","top-back-button",((oc.shared.useragent.mobile_app_QMARK_)?"mobile-app":null)], null))}),"Back"),(function (){var attrs44452 = (function (){var or__4126__auto__ = title;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.components.ui.login_wall.default_title;
}
})();
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs44452))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["title"], null)], null),attrs44452], 0))):({"className": "title"})),((cljs.core.map_QMARK_(attrs44452))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs44452)], null)));
})(),React.createElement("button",({"onClick": login_action, "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["mlb-reset","top-continue",(cljs.core.truth_(login_enabled)?null:"disabled")], null))}),"Log in")),React.createElement("div",({"className": "login-wall-wrapper"}),React.createElement("div",({"className": "login-wall-internal"}),React.createElement("div",({"className": "login-wall-content"}),React.createElement("div",({"className": "login-overlay-cta group"}),React.createElement("div",({"className": "login-title"}),"Log in")),sablono.interpreter.interpret(((cljs.core.seq((function (){var or__4126__auto__ = desc;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.components.ui.login_wall.default_desc;
}
})()))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.login-description","div.login-description",1471492458),(function (){var or__4126__auto__ = desc;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.components.ui.login_wall.default_desc;
}
})()], null):null)),React.createElement("div",({"className": "login-buttons group"}),React.createElement("button",({"onTouchStart": cljs.core.identity, "onClick": (function (p1__44409_SHARP_){
p1__44409_SHARP_.preventDefault();

var temp__5735__auto__ = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(auth_settings),"authenticate","GET",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"auth-source","auth-source",1912135250),"slack"], null)], 0));
if(cljs.core.truth_(temp__5735__auto__)){
var auth_link = temp__5735__auto__;
oc.web.actions.user.maybe_save_login_redirect();

return oc.web.actions.user.login_with_slack.cljs$core$IFn$_invoke$arity$variadic(auth_link,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([((oc.shared.useragent.mobile_app_QMARK_)?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"redirect-origin","redirect-origin",1138035106),deep_link_origin], null):null)], 0));
} else {
return null;
}
}), "className": "mlb-reset signup-with-slack"}),React.createElement("div",({"aria-label": "slack", "className": "slack-icon"})),React.createElement("div",({"className": "slack-text"}),"Slack")),React.createElement("button",({"onTouchStart": cljs.core.identity, "onClick": (function (p1__44410_SHARP_){
p1__44410_SHARP_.preventDefault();

var temp__5735__auto__ = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(auth_settings),"authenticate","GET",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"auth-source","auth-source",1912135250),"google"], null)], 0));
if(cljs.core.truth_(temp__5735__auto__)){
var auth_link = temp__5735__auto__;
oc.web.actions.user.maybe_save_login_redirect();

return oc.web.actions.user.login_with_google.cljs$core$IFn$_invoke$arity$variadic(auth_link,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([((oc.shared.useragent.mobile_app_QMARK_)?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"redirect-origin","redirect-origin",1138035106),deep_link_origin], null):null)], 0));
} else {
return null;
}
}), "className": "mlb-reset signup-with-google"}),React.createElement("div",({"aria-label": "google", "className": "google-icon"})),React.createElement("div",({"className": "google-text"}),"Google"))),React.createElement("div",({"className": "or-login"}),React.createElement("div",({"className": "or-login-copy"}),"Or, login with email")),(function (){var attrs44458 = (((login_with_email_error == null))?null:((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(login_with_email_error,new cljs.core.Keyword(null,"verify-email","verify-email",464870696)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.small-caps.green","span.small-caps.green",1161015991),"Hey buddy, go verify your email, again, eh?"], null):((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(login_with_email_error,(401)))?new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.small-caps.red","span.small-caps.red",-2126101566),"The email or password you entered is incorrect.",new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"br","br",934104792)], null),"Please try again, or ",new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a.underline.red","a.underline.red",-1878793609),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.actions.user.show_login(new cljs.core.Keyword(null,"password-reset","password-reset",1971592302));
})], null),"reset your password"], null),"."], null):new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.small-caps.red","span.small-caps.red",-2126101566),"System troubles logging in.",new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"br","br",934104792)], null),"Please try again, then ",new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a.underline.red","a.underline.red",-1878793609),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"href","href",-793805698),oc.web.urls.contact_mail_to], null),"contact support"], null),"."], null)
)));
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs44458))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["group"], null)], null),attrs44458], 0))):({"className": "group"})),((cljs.core.map_QMARK_(attrs44458))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [React.createElement("form",({"className": "sign-in-form"}),React.createElement("div",({"className": "fields-container group"}),React.createElement("div",({"className": "field-label"}),"Work email"),sablono.interpreter.create_element("input",({"type": "email", "name": "email", "value": cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.login-wall","email","oc.web.components.ui.login-wall/email",-271375922).cljs$core$IFn$_invoke$arity$1(s)), "onChange": (function (p1__44411_SHARP_){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.login-wall","email","oc.web.components.ui.login-wall/email",-271375922).cljs$core$IFn$_invoke$arity$1(s),p1__44411_SHARP_.target.value);
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["field-content","email",oc.web.lib.utils.hide_class], null))})),React.createElement("div",({"className": "field-label"}),"Password"),sablono.interpreter.create_element("input",({"type": "password", "name": "password", "value": cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.login-wall","pswd","oc.web.components.ui.login-wall/pswd",-267749663).cljs$core$IFn$_invoke$arity$1(s)), "onChange": (function (p1__44412_SHARP_){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.login-wall","pswd","oc.web.components.ui.login-wall/pswd",-267749663).cljs$core$IFn$_invoke$arity$1(s),p1__44412_SHARP_.target.value);
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["field-content","password",oc.web.lib.utils.hide_class], null))})),React.createElement("div",({"className": "forgot-password"}),React.createElement("a",({"onClick": (function (){
return oc.web.actions.user.show_login(new cljs.core.Keyword(null,"password-reset","password-reset",1971592302));
})}),"Forgot password?"))),React.createElement("button",({"aria-label": "Login", "disabled": cljs.core.not(login_enabled), "onClick": login_action, "className": "mlb-reset continue-btn"}),"Log in"))], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs44458),React.createElement("form",({"className": "sign-in-form"}),React.createElement("div",({"className": "fields-container group"}),React.createElement("div",({"className": "field-label"}),"Work email"),sablono.interpreter.create_element("input",({"type": "email", "name": "email", "value": cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.login-wall","email","oc.web.components.ui.login-wall/email",-271375922).cljs$core$IFn$_invoke$arity$1(s)), "onChange": (function (p1__44411_SHARP_){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.login-wall","email","oc.web.components.ui.login-wall/email",-271375922).cljs$core$IFn$_invoke$arity$1(s),p1__44411_SHARP_.target.value);
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["field-content","email",oc.web.lib.utils.hide_class], null))})),React.createElement("div",({"className": "field-label"}),"Password"),sablono.interpreter.create_element("input",({"type": "password", "name": "password", "value": cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.login-wall","pswd","oc.web.components.ui.login-wall/pswd",-267749663).cljs$core$IFn$_invoke$arity$1(s)), "onChange": (function (p1__44412_SHARP_){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.login-wall","pswd","oc.web.components.ui.login-wall/pswd",-267749663).cljs$core$IFn$_invoke$arity$1(s),p1__44412_SHARP_.target.value);
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["field-content","password",oc.web.lib.utils.hide_class], null))})),React.createElement("div",({"className": "forgot-password"}),React.createElement("a",({"onClick": (function (){
return oc.web.actions.user.show_login(new cljs.core.Keyword(null,"password-reset","password-reset",1971592302));
})}),"Forgot password?"))),React.createElement("button",({"aria-label": "Login", "disabled": cljs.core.not(login_enabled), "onClick": login_action, "className": "mlb-reset continue-btn"}),"Log in"))], null)));
})())),React.createElement("div",({"className": "footer-link"}),"Don't have an account yet?",React.createElement("div",({"className": "footer-link-inner"}),React.createElement("a",({"href": oc.web.urls.sign_up, "onClick": (function (e){
oc.web.lib.utils.event_stop(e);

return oc.web.router.nav_BANG_(oc.web.urls.sign_up);
})}),"Sign up here"))))], null)));
}
}),new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"auth-settings","auth-settings",-1775088219)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"login-with-email-error","login-with-email-error",-373631840)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"expo-deep-link-origin","expo-deep-link-origin",404078070)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2("",new cljs.core.Keyword("oc.web.components.ui.login-wall","email","oc.web.components.ui.login-wall/email",-271375922)),rum.core.local.cljs$core$IFn$_invoke$arity$2("",new cljs.core.Keyword("oc.web.components.ui.login-wall","pswd","oc.web.components.ui.login-wall/pswd",-267749663))], null),"login-wall");

//# sourceMappingURL=oc.web.components.ui.login_wall.js.map
