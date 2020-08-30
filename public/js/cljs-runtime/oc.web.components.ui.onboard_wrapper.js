goog.provide('oc.web.components.ui.onboard_wrapper');
oc.web.components.ui.onboard_wrapper.clean_org_name = (function oc$web$components$ui$onboard_wrapper$clean_org_name(org_name){
return cuerdas.core.trim.cljs$core$IFn$_invoke$arity$1(org_name);
});
/**
 * Given a Rum state and a ref, async focus the filed if it exists.
 */
oc.web.components.ui.onboard_wrapper.delay_focus_field_with_ref = (function oc$web$components$ui$onboard_wrapper$delay_focus_field_with_ref(s,r){
return oc.web.lib.utils.after((0),(function (){
var temp__5735__auto__ = rum.core.ref_node(s,r);
if(cljs.core.truth_(temp__5735__auto__)){
var field = temp__5735__auto__;
return field.focus();
} else {
return null;
}
}));
});
oc.web.components.ui.onboard_wrapper.lander = rum.core.build_defcs((function (s){
var signup_with_email = org.martinklepsch.derivatives.react(s,oc.web.stores.user.signup_with_email);
var auth_settings = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"auth-settings","auth-settings",-1775088219));
var deep_link_origin = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"expo-deep-link-origin","expo-deep-link-origin",404078070));
var continue_disabled_QMARK_ = ((cljs.core.not(oc.web.lib.utils.valid_email_QMARK_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","email","oc.web.components.ui.onboard-wrapper/email",1982191867).cljs$core$IFn$_invoke$arity$1(s))))) || ((cljs.core.count(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","pswd","oc.web.components.ui.onboard-wrapper/pswd",1910006868).cljs$core$IFn$_invoke$arity$1(s))) <= (7))));
var continue_fn = ((continue_disabled_QMARK_)?(function (_){
if(cljs.core.truth_(oc.web.lib.utils.valid_email_QMARK_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","email","oc.web.components.ui.onboard-wrapper/email",1982191867).cljs$core$IFn$_invoke$arity$1(s))))){
} else {
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","email-error","oc.web.components.ui.onboard-wrapper/email-error",-582475544).cljs$core$IFn$_invoke$arity$1(s),true);
}

if((cljs.core.count(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","pswd","oc.web.components.ui.onboard-wrapper/pswd",1910006868).cljs$core$IFn$_invoke$arity$1(s))) <= (7))){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","password-error","oc.web.components.ui.onboard-wrapper/password-error",1673013435).cljs$core$IFn$_invoke$arity$1(s),true);
} else {
return null;
}
}):(function (){
return oc.web.actions.user.signup_with_email(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"email","email",1415816706),cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","email","oc.web.components.ui.onboard-wrapper/email",1982191867).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"pswd","pswd",278786885),cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","pswd","oc.web.components.ui.onboard-wrapper/pswd",1910006868).cljs$core$IFn$_invoke$arity$1(s))], null));
}));
return React.createElement("div",({"className": "onboard-lander lander group"}),React.createElement("header",({"className": "main-cta"}),React.createElement("button",({"onTouchStart": cljs.core.identity, "onClick": (function (){
return oc.web.router.history_back_BANG_();
}), "className": "mlb-reset top-back-button"}),"Back"),React.createElement("div",({"className": "title main-lander"}),"Let\u2019s get started!"),React.createElement("button",({"onClick": continue_fn, "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["mlb-reset","top-continue",((continue_disabled_QMARK_)?"disabled":null)], null))}),"Continue")),React.createElement("div",({"className": "onboard-form"}),React.createElement("div",({"className": "form-title"}),"Your profile"),React.createElement("div",({"className": "signup-buttons group"}),React.createElement("button",({"onTouchStart": cljs.core.identity, "onClick": (function (p1__46942_SHARP_){
p1__46942_SHARP_.preventDefault();

var temp__5735__auto__ = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(auth_settings),"authenticate","GET",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"auth-source","auth-source",1912135250),"slack"], null)], 0));
if(cljs.core.truth_(temp__5735__auto__)){
var auth_link = temp__5735__auto__;
return oc.web.actions.user.login_with_slack.cljs$core$IFn$_invoke$arity$variadic(auth_link,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([((oc.shared.useragent.mobile_app_QMARK_)?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"redirect-origin","redirect-origin",1138035106),deep_link_origin], null):null)], 0));
} else {
return null;
}
}), "className": "mlb-reset signup-with-slack"}),React.createElement("div",({"aria-label": "slack", "className": "slack-icon"})),React.createElement("div",({"className": "slack-text"}),"Slack")),React.createElement("button",({"onTouchStart": cljs.core.identity, "onClick": (function (p1__46944_SHARP_){
p1__46944_SHARP_.preventDefault();

var temp__5735__auto__ = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(auth_settings),"authenticate","GET",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"auth-source","auth-source",1912135250),"google"], null)], 0));
if(cljs.core.truth_(temp__5735__auto__)){
var auth_link = temp__5735__auto__;
return oc.web.actions.user.login_with_google.cljs$core$IFn$_invoke$arity$variadic(auth_link,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([((oc.shared.useragent.mobile_app_QMARK_)?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"redirect-origin","redirect-origin",1138035106),deep_link_origin], null):null)], 0));
} else {
return null;
}
}), "className": "mlb-reset signup-with-google"}),React.createElement("div",({"aria-label": "google", "className": "google-icon"})),React.createElement("div",({"className": "google-text"}),"Google"))),React.createElement("div",({"className": "or-with-email"}),React.createElement("div",({"className": "or-with-email-copy"}),"Or, sign up with email")),React.createElement("form",({"onSubmit": (function (e){
return e.preventDefault();
})}),React.createElement("div",({"className": "field-label email-field"}),"Work email",sablono.interpreter.interpret(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(signup_with_email),(409)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.error","span.error",-283487575),"Email already exists"], null):(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","email-error","oc.web.components.ui.onboard-wrapper/email-error",-582475544).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.error","span.error",-283487575),"Email is not valid"], null):null)))),sablono.interpreter.create_element("input",({"type": "email", "pattern": oc.web.lib.utils.valid_email_pattern, "value": cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","email","oc.web.components.ui.onboard-wrapper/email",1982191867).cljs$core$IFn$_invoke$arity$1(s)), "onChange": (function (p1__46945_SHARP_){
var v = p1__46945_SHARP_.target.value;
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","password-error","oc.web.components.ui.onboard-wrapper/password-error",1673013435).cljs$core$IFn$_invoke$arity$1(s),false);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","email-error","oc.web.components.ui.onboard-wrapper/email-error",-582475544).cljs$core$IFn$_invoke$arity$1(s),false);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","email","oc.web.components.ui.onboard-wrapper/email",1982191867).cljs$core$IFn$_invoke$arity$1(s),v);
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["field","oc-input",oc.web.lib.utils.class_set(cljs.core.PersistentArrayMap.createAsIfByAssoc([new cljs.core.Keyword(null,"error","error",-978969032),cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(signup_with_email),(409)),oc.web.lib.utils.hide_class,true]))], null))})),React.createElement("div",({"className": "field-label"}),"Password",sablono.interpreter.interpret((cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","password-error","oc.web.components.ui.onboard-wrapper/password-error",1673013435).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.error","span.error",-283487575),"Minimum 8 characters"], null):null))),sablono.interpreter.create_element("input",({"type": "password", "pattern": ".{8,}", "value": cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","pswd","oc.web.components.ui.onboard-wrapper/pswd",1910006868).cljs$core$IFn$_invoke$arity$1(s)), "placeholder": "Minimum 8 characters", "onChange": (function (p1__46947_SHARP_){
var v = p1__46947_SHARP_.target.value;
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","password-error","oc.web.components.ui.onboard-wrapper/password-error",1673013435).cljs$core$IFn$_invoke$arity$1(s),false);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","email-error","oc.web.components.ui.onboard-wrapper/email-error",-582475544).cljs$core$IFn$_invoke$arity$1(s),false);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","pswd","oc.web.components.ui.onboard-wrapper/pswd",1910006868).cljs$core$IFn$_invoke$arity$1(s),v);
}), "className": "field oc-input"})),React.createElement("button",({"onTouchStart": cljs.core.identity, "onClick": continue_fn, "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["continue",((continue_disabled_QMARK_)?"disabled":null)], null))}),"Continue"),React.createElement("div",({"className": "field-description"}),"By clicking continue, you agree to our ",React.createElement("a",({"href": oc.web.urls.terms}),"terms of service")," and ",React.createElement("a",({"href": oc.web.urls.privacy}),"privacy policy"),"."))),React.createElement("div",({"className": "footer-link"}),"Already have an account?",React.createElement("div",null,React.createElement("a",({"href": ((oc.shared.useragent.pseudo_native_QMARK_)?oc.web.urls.native_login:oc.web.urls.login), "onClick": (function (e){
oc.web.lib.utils.event_stop(e);

return oc.web.router.nav_BANG_(((oc.shared.useragent.pseudo_native_QMARK_)?oc.web.urls.native_login:oc.web.urls.login));
})}),"Sign in here"))));
}),new cljs.core.PersistentVector(null, 10, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$,rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([oc.web.stores.user.signup_with_email], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"auth-settings","auth-settings",-1775088219)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"expo-deep-link-origin","expo-deep-link-origin",404078070)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","email-error","oc.web.components.ui.onboard-wrapper/email-error",-582475544)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","password-error","oc.web.components.ui.onboard-wrapper/password-error",1673013435)),rum.core.local.cljs$core$IFn$_invoke$arity$2("",new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","email","oc.web.components.ui.onboard-wrapper/email",1982191867)),rum.core.local.cljs$core$IFn$_invoke$arity$2("",new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","pswd","oc.web.components.ui.onboard-wrapper/pswd",1910006868)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
oc.web.actions.user.signup_with_email_reset_errors();

return s;
})], null)], null),"lander");
/**
 * 
 */
oc.web.components.ui.onboard_wrapper.profile_setup_team_data = (function oc$web$components$ui$onboard_wrapper$profile_setup_team_data(s){
oc.web.actions.team.teams_get_if_needed();

var org_editing = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915)));
var teams_data = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"teams-data","teams-data",-808450077)));
if((((cljs.core.count(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(org_editing)) === (0))) && (cljs.core.seq(teams_data)))){
var first_team = cljs.core.select_keys(cljs.core.first(teams_data),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"logo-url","logo-url",-1629105032)], null));
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915)], null),(function (p1__46985_SHARP_){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__46985_SHARP_,first_team], 0));
})], null));

if(cljs.core.seq(new cljs.core.Keyword(null,"logo-url","logo-url",-1629105032).cljs$core$IFn$_invoke$arity$1(first_team))){
var img = goog.dom.createDom("img");
(img.onload = (function (){
return goog.dom.removeNode(img);
}));

(img.onerror = (function (){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915)], null),(function (p1__46986_SHARP_){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(p1__46986_SHARP_,new cljs.core.Keyword(null,"logo-url","logo-url",-1629105032));
})], null));

return goog.dom.removeNode(img);
}));

goog.dom.append(document.body,img);

return (img.src = new cljs.core.Keyword(null,"logo-url","logo-url",-1629105032).cljs$core$IFn$_invoke$arity$1(first_team));
} else {
return null;
}
} else {
return null;
}
});
oc.web.components.ui.onboard_wrapper.update_tooltip = (function oc$web$components$ui$onboard_wrapper$update_tooltip(s){
return oc.web.lib.utils.after((100),(function (){
var header_avatar = rum.core.ref_node(s,"user-profile-avatar");
var $header_avatar = $(header_avatar);
var edit_user_profile_avatar = new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-data","user-data",2143823568).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"edit-user-profile","edit-user-profile",-479059118)))));
var title = (cljs.core.truth_(cuerdas.core.includes_QMARK_(edit_user_profile_avatar,"/img/ML/happy_face_"))?"Add a photo":"Change photo");
return $header_avatar.tooltip(({"title": title, "trigger": "hover focus", "position": "top", "container": "body"}));
}));
});
oc.web.components.ui.onboard_wrapper.error_cb = (function oc$web$components$ui$onboard_wrapper$error_cb(s,res,error){
oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"title","title",636505583),"Image upload error",new cljs.core.Keyword(null,"description","description",-1428560544),"An error occurred while processing your image. Please retry.",new cljs.core.Keyword(null,"expire","expire",-70657108),(3),new cljs.core.Keyword(null,"dismiss","dismiss",412569545),true], null));

return oc.web.components.ui.onboard_wrapper.update_tooltip(s);
});
oc.web.components.ui.onboard_wrapper.success_cb = (function oc$web$components$ui$onboard_wrapper$success_cb(s,res){
var url = goog.object.get(res,"url");
if(cljs.core.not(url)){
oc.web.components.ui.onboard_wrapper.error_cb(s,null,null);
} else {
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"edit-user-profile","edit-user-profile",-479059118),new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103)], null),url], null));
}

return oc.web.components.ui.onboard_wrapper.update_tooltip(s);
});
oc.web.components.ui.onboard_wrapper.progress_cb = (function oc$web$components$ui$onboard_wrapper$progress_cb(res,progress){
return null;
});
oc.web.components.ui.onboard_wrapper.upload_user_profile_picture_clicked = (function oc$web$components$ui$onboard_wrapper$upload_user_profile_picture_clicked(s){
return oc.web.lib.image_upload.upload_BANG_(oc.web.utils.user.user_avatar_filestack_config,cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.components.ui.onboard_wrapper.success_cb,s),oc.web.components.ui.onboard_wrapper.progress_cb,cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.components.ui.onboard_wrapper.error_cb,s));
});
oc.web.components.ui.onboard_wrapper.lander_profile = rum.core.build_defcs((function (s){
var has_org_QMARK_ = (cljs.core.count(org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"orgs","orgs",155776628))) > (0));
var edit_user_profile = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"edit-user-profile","edit-user-profile",-479059118));
var current_user_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915));
var teams_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"teams-data","teams-data",-808450077));
var org_editing = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915));
var user_data = new cljs.core.Keyword(null,"user-data","user-data",2143823568).cljs$core$IFn$_invoke$arity$1(edit_user_profile);
var continue_disabled = (function (){var or__4126__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","saving","oc.web.components.ui.onboard-wrapper/saving",1021445936).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return ((((cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"first-name","first-name",-1559982131).cljs$core$IFn$_invoke$arity$1(user_data))) && (cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"last-name","last-name",-1695738974).cljs$core$IFn$_invoke$arity$1(user_data))))) || ((((!(has_org_QMARK_))) && ((cljs.core.count(oc.web.components.ui.onboard_wrapper.clean_org_name(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(org_editing))) <= (1))))));
}
})();
var is_mobile_QMARK_ = oc.web.lib.responsive.is_tablet_or_mobile_QMARK_();
var continue_fn = (function (){
if(cljs.core.truth_(continue_disabled)){
return null;
} else {
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","saving","oc.web.components.ui.onboard-wrapper/saving",1021445936).cljs$core$IFn$_invoke$arity$1(s),true);

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915),new cljs.core.Keyword(null,"name","name",1843675177)], null),oc.web.components.ui.onboard_wrapper.clean_org_name], null));

return oc.web.actions.user.onboard_profile_save.cljs$core$IFn$_invoke$arity$3(current_user_data,edit_user_profile,new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915));
}
});
return React.createElement("div",({"className": "onboard-lander lander-profile"}),React.createElement("header",({"className": "main-cta"}),(function (){var attrs46990 = ((has_org_QMARK_)?"Tell us about you":"Create your team");
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46990))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["title","about-yourself"], null)], null),attrs46990], 0))):({"className": "title about-yourself"})),((cljs.core.map_QMARK_(attrs46990))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46990)], null)));
})()),sablono.interpreter.interpret((cljs.core.truth_(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(edit_user_profile))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.subtitle.error","div.subtitle.error",-11627825),"An error occurred while saving your data, please try again"], null):null)),React.createElement("div",({"className": "onboard-form"}),React.createElement("form",({"onSubmit": (function (e){
return e.preventDefault();
})}),React.createElement("div",({"className": "form-title"}),"Your profile"),React.createElement("button",({"onClick": (function (){
return oc.web.components.ui.onboard_wrapper.upload_user_profile_picture_clicked(s);
}), "ref": "user-profile-avatar", "data-toggle": ((is_mobile_QMARK_)?null:"tooltip"), "title": "Change avatar", "data-placement": "top", "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["mlb-reset","user-profile-avatar",(cljs.core.truth_(oc.web.utils.user.default_avatar_QMARK_(new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103).cljs$core$IFn$_invoke$arity$1(user_data)))?"default-avatar":null)], null))}),sablono.interpreter.interpret((oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1(user_data) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,user_data))),React.createElement("div",({"className": "plus-icon"}))),React.createElement("div",({"className": "field-label name-fields"}),"First name"),sablono.interpreter.create_element("input",({"type": "text", "ref": "first-name", "placeholder": "First name...", "maxLength": oc.web.utils.user.user_name_max_lenth, "value": (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"first-name","first-name",-1559982131).cljs$core$IFn$_invoke$arity$1(user_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "";
}
})(), "onChange": (function (p1__46987_SHARP_){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"edit-user-profile","edit-user-profile",-479059118),new cljs.core.Keyword(null,"first-name","first-name",-1559982131)], null),p1__46987_SHARP_.target.value], null));
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["field","oc-input",oc.web.lib.utils.hide_class], null))})),React.createElement("div",({"className": "field-label"}),"Last name"),sablono.interpreter.create_element("input",({"type": "text", "placeholder": "Last name...", "value": (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"last-name","last-name",-1695738974).cljs$core$IFn$_invoke$arity$1(user_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "";
}
})(), "maxLength": oc.web.utils.user.user_name_max_lenth, "onChange": (function (p1__46988_SHARP_){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"edit-user-profile","edit-user-profile",-479059118),new cljs.core.Keyword(null,"last-name","last-name",-1695738974)], null),p1__46988_SHARP_.target.value], null));
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["field","oc-input",oc.web.lib.utils.hide_class], null))})),sablono.interpreter.interpret(((has_org_QMARK_)?null:new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.field-label.company-name","div.field-label.company-name",-1679597649),"Company name"], null))),sablono.interpreter.interpret(((has_org_QMARK_)?null:new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input.field.oc-input","input.field.oc-input",900730712),new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"type","type",1174270348),"text",new cljs.core.Keyword(null,"ref","ref",1289896967),"org-name",new cljs.core.Keyword(null,"placeholder","placeholder",-104873083),"Enter a team name...",new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.class_set(cljs.core.PersistentArrayMap.createAsIfByAssoc([new cljs.core.Keyword(null,"error","error",-978969032),new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(org_editing),oc.web.lib.utils.hide_class,true])),new cljs.core.Keyword(null,"max-length","max-length",-254826109),oc.web.utils.org.org_name_max_length,new cljs.core.Keyword(null,"value","value",305978217),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(org_editing),new cljs.core.Keyword(null,"on-change","on-change",-732046149),(function (p1__46989_SHARP_){
var new_name = p1__46989_SHARP_.target.value;
var clean_org_name = cljs.core.subs.cljs$core$IFn$_invoke$arity$3(new_name,(0),(function (){var x__4217__auto__ = cljs.core.count(new_name);
var y__4218__auto__ = oc.web.utils.org.org_name_max_length;
return ((x__4217__auto__ < y__4218__auto__) ? x__4217__auto__ : y__4218__auto__);
})());
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915)], null),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([org_editing,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"error","error",-978969032),null,new cljs.core.Keyword(null,"name","name",1843675177),clean_org_name,new cljs.core.Keyword(null,"rand","rand",908504774),cljs.core.rand.cljs$core$IFn$_invoke$arity$1((1000))], null)], 0))], null));
})], null)], null))),sablono.interpreter.interpret((cljs.core.truth_(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(org_editing))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.error","div.error",314336058),"Must be between 3 and 50 characters"], null):null)),React.createElement("button",({"onTouchStart": cljs.core.identity, "onClick": continue_fn, "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["continue",(cljs.core.truth_(continue_disabled)?"disabled":null)], null))}),"Create team"))),React.createElement("div",({"className": "logout-cancel"}),"Need to start over? ",React.createElement("button",({"onClick": (function (){
return oc.web.actions.jwt.logout.cljs$core$IFn$_invoke$arity$1(oc.web.urls.sign_up);
}), "className": "mlb-reset logout-cancel"}),"Click here")));
}),new cljs.core.PersistentVector(null, 9, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"edit-user-profile","edit-user-profile",-479059118)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"teams-data","teams-data",-808450077)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"orgs","orgs",155776628)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","saving","oc.web.components.ui.onboard-wrapper/saving",1021445936)),oc.web.mixins.ui.refresh_tooltips_mixin,new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915),new cljs.core.Keyword(null,"name","name",1843675177)], null),""], null));

oc.web.actions.user.user_profile_reset();

return s;
}),new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
oc.web.components.ui.onboard_wrapper.profile_setup_team_data(s);

oc.web.components.ui.onboard_wrapper.delay_focus_field_with_ref(s,"first-name");

oc.web.components.ui.onboard_wrapper.update_tooltip(s);

return s;
}),new cljs.core.Keyword(null,"did-remount","did-remount",1362550500),(function (o,s){
oc.web.components.ui.onboard_wrapper.update_tooltip(s);

return s;
}),new cljs.core.Keyword(null,"will-update","will-update",328062998),(function (s){
oc.web.components.ui.onboard_wrapper.profile_setup_team_data(s);

var edit_user_profile_47095 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"edit-user-profile","edit-user-profile",-479059118)));
var org_editing_47096 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915)));
if(cljs.core.truth_((function (){var and__4115__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","saving","oc.web.components.ui.onboard-wrapper/saving",1021445936).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(and__4115__auto__)){
var or__4126__auto__ = new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(edit_user_profile_47095);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(org_editing_47096);
}
} else {
return and__4115__auto__;
}
})())){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","saving","oc.web.components.ui.onboard-wrapper/saving",1021445936).cljs$core$IFn$_invoke$arity$1(s),false);
} else {
}

return s;
})], null)], null),"lander-profile");
/**
 * 
 */
oc.web.components.ui.onboard_wrapper.setup_team_data = (function oc$web$components$ui$onboard_wrapper$setup_team_data(s){
oc.web.actions.team.teams_get_if_needed();

var org_editing = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915)));
var teams_data = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"teams-data","teams-data",-808450077)));
if((((cljs.core.count(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(org_editing)) === (0))) && ((cljs.core.count(new cljs.core.Keyword(null,"logo-url","logo-url",-1629105032).cljs$core$IFn$_invoke$arity$1(org_editing)) === (0))) && (cljs.core.seq(teams_data)))){
var first_team = cljs.core.select_keys(cljs.core.first(teams_data),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"logo-url","logo-url",-1629105032)], null));
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915)], null),(function (p1__46999_SHARP_){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__46999_SHARP_,first_team], 0));
})], null));

if((((!((cljs.core.count(new cljs.core.Keyword(null,"logo-url","logo-url",-1629105032).cljs$core$IFn$_invoke$arity$1(first_team)) === (0))))) && (cljs.core.not(new cljs.core.Keyword(null,"logo-height","logo-height",-2066989379).cljs$core$IFn$_invoke$arity$1(first_team))))){
var img = goog.dom.createDom("img");
(img.onload = (function (){
return goog.dom.removeNode(img);
}));

goog.dom.append(document.body,img);

return (img.src = new cljs.core.Keyword(null,"logo-url","logo-url",-1629105032).cljs$core$IFn$_invoke$arity$1(first_team));
} else {
return null;
}
} else {
return null;
}
});
oc.web.components.ui.onboard_wrapper.lander_team = rum.core.build_defcs((function (s){
var teams_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"teams-data","teams-data",-808450077));
var org_editing = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915));
var is_mobile_QMARK_ = oc.web.lib.responsive.is_tablet_or_mobile_QMARK_();
var continue_disabled = (cljs.core.count(oc.web.components.ui.onboard_wrapper.clean_org_name(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(org_editing))) < (3));
var continue_fn = (function (){
if(continue_disabled){
return null;
} else {
var org_name = oc.web.components.ui.onboard_wrapper.clean_org_name(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(org_editing));
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915),new cljs.core.Keyword(null,"name","name",1843675177)], null),org_name], null));

if(((cljs.core.seq(org_name)) && ((cljs.core.count(org_name) >= (2))))){
return oc.web.actions.org.create_or_update_org(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915))));
} else {
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915),new cljs.core.Keyword(null,"error","error",-978969032)], null),true], null));
}
}
});
return React.createElement("div",({"className": "onboard-lander lander-team"}),React.createElement("header",({"className": "main-cta"}),React.createElement("div",({"className": "title company-setup"}),"Set up your company")),React.createElement("div",({"className": "onboard-form"}),React.createElement("form",({"onSubmit": (function (e){
return e.preventDefault();
})}),sablono.interpreter.interpret(((is_mobile_QMARK_)?null:new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.logo-upload-container.org-logo.group","div.logo-upload-container.org-logo.group",353141173),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.hide_class,new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (_){
if(cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"logo-url","logo-url",-1629105032).cljs$core$IFn$_invoke$arity$1(org_editing))){
return oc.web.lib.image_upload.upload_BANG_.cljs$core$IFn$_invoke$arity$variadic(oc.web.utils.org.org_avatar_filestack_config,(function (res){
var url = goog.object.get(res,"url");
var img = goog.dom.createDom("img");
(img.onload = (function (){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915)], null),(function (p1__47002_SHARP_){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__47002_SHARP_,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"logo-url","logo-url",-1629105032),url], null)], 0));
})], null));

return goog.dom.removeNode(img);
}));

(img.className = "hidden");

goog.dom.append(document.body,img);

return (img.src = url);
}),null,(function (___$1){
return null;
}),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([null], 0));
} else {
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915)], null),(function (p1__47004_SHARP_){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(p1__47004_SHARP_,new cljs.core.Keyword(null,"logo-url","logo-url",-1629105032),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"logo-width","logo-width",-4247589),new cljs.core.Keyword(null,"logo-height","logo-height",-2066989379)], 0));
})], null));
}
})], null),(oc.web.components.ui.org_avatar.org_avatar.cljs$core$IFn$_invoke$arity$3 ? oc.web.components.ui.org_avatar.org_avatar.cljs$core$IFn$_invoke$arity$3(org_editing,false,new cljs.core.Keyword(null,"never","never",50472977)) : oc.web.components.ui.org_avatar.org_avatar.call(null,org_editing,false,new cljs.core.Keyword(null,"never","never",50472977))),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.add-picture-link","div.add-picture-link",-480203941),((cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"logo-url","logo-url",-1629105032).cljs$core$IFn$_invoke$arity$1(org_editing)))?"Add company logo":"Change company logo")], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.add-picture-link-subtitle","div.add-picture-link-subtitle",-436991932),"A 160x160 transparent Gif or PNG works best."], null)], null))),React.createElement("div",({"className": "field-label"}),"Company name"),sablono.interpreter.create_element("input",({"type": "text", "ref": "org-name", "value": new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(org_editing), "onChange": (function (p1__47007_SHARP_){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915)], null),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([org_editing,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"error","error",-978969032),null,new cljs.core.Keyword(null,"name","name",1843675177),p1__47007_SHARP_.target.value], null)], 0))], null));
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["field","oc-input",oc.web.lib.utils.class_set(cljs.core.PersistentArrayMap.createAsIfByAssoc([new cljs.core.Keyword(null,"error","error",-978969032),new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(org_editing),oc.web.lib.utils.hide_class,true]))], null))})),sablono.interpreter.interpret((cljs.core.truth_(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(org_editing))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.error","div.error",314336058),"Must be between 3 and 50 characters"], null):null)),React.createElement("button",({"onTouchStart": cljs.core.identity, "onClick": continue_fn, "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["continue",((continue_disabled)?"disabled":null)], null))}),"Continue"))));
}),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"teams-data","teams-data",-808450077)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","saving","oc.web.components.ui.onboard-wrapper/saving",1021445936)),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915),new cljs.core.Keyword(null,"name","name",1843675177)], null),""], null));

return s;
}),new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
oc.web.components.ui.onboard_wrapper.setup_team_data(s);

oc.web.components.ui.onboard_wrapper.delay_focus_field_with_ref(s,"org-name");

return s;
}),new cljs.core.Keyword(null,"will-update","will-update",328062998),(function (s){
oc.web.components.ui.onboard_wrapper.setup_team_data(s);

return s;
})], null)], null),"lander-team");
oc.web.components.ui.onboard_wrapper.default_invite_row = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"user","user",1532431356),"",new cljs.core.Keyword(null,"type","type",1174270348),"email",new cljs.core.Keyword(null,"role","role",-736691072),new cljs.core.Keyword(null,"author","author",2111686192),new cljs.core.Keyword(null,"error","error",-978969032),false], null);
oc.web.components.ui.onboard_wrapper.check_invite_row = (function oc$web$components$ui$onboard_wrapper$check_invite_row(invite){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(invite,new cljs.core.Keyword(null,"error","error",-978969032),((cljs.core.seq(new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(invite))) && (cljs.core.not(oc.web.lib.utils.valid_email_QMARK_(new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(invite))))));
});
oc.web.components.ui.onboard_wrapper.check_invites = (function oc$web$components$ui$onboard_wrapper$check_invites(s){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","invite-rows","oc.web.components.ui.onboard-wrapper/invite-rows",-204272148).cljs$core$IFn$_invoke$arity$1(s),cljs.core.vec(cljs.core.map.cljs$core$IFn$_invoke$arity$2(oc.web.components.ui.onboard_wrapper.check_invite_row,cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","invite-rows","oc.web.components.ui.onboard-wrapper/invite-rows",-204272148).cljs$core$IFn$_invoke$arity$1(s)))));
});
oc.web.components.ui.onboard_wrapper.default_invite_note = ["Hey there, let's explore Wut together. It's a place to make sure important ","announcements, updates, and decisions don't get lost in the noise."].join('');
oc.web.components.ui.onboard_wrapper.lander_invite = rum.core.build_defcs((function (s){
var _ = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"invite-users","invite-users",107417337));
var org_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"org-data","org-data",96720321));
var valid_rows = cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__47021_SHARP_){
return ((cljs.core.seq(new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(p1__47021_SHARP_))) && (cljs.core.not(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(p1__47021_SHARP_))));
}),cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","invite-rows","oc.web.components.ui.onboard-wrapper/invite-rows",-204272148).cljs$core$IFn$_invoke$arity$1(s)));
var error_rows = cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__47022_SHARP_){
var and__4115__auto__ = cljs.core.seq(new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(p1__47022_SHARP_));
if(and__4115__auto__){
return new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(p1__47022_SHARP_);
} else {
return and__4115__auto__;
}
}),cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","invite-rows","oc.web.components.ui.onboard-wrapper/invite-rows",-204272148).cljs$core$IFn$_invoke$arity$1(s)));
var continue_fn = (function (){
var ___$1 = oc.web.components.ui.onboard_wrapper.check_invites(s);
var errors = cljs.core.filter.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"error","error",-978969032),cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","invite-rows","oc.web.components.ui.onboard-wrapper/invite-rows",-204272148).cljs$core$IFn$_invoke$arity$1(s)));
if((cljs.core.count(errors) === (0))){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","inviting","oc.web.components.ui.onboard-wrapper/inviting",-1609819584).cljs$core$IFn$_invoke$arity$1(s),true);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","invite-error","oc.web.components.ui.onboard-wrapper/invite-error",477343172).cljs$core$IFn$_invoke$arity$1(s),null);

var not_empty_invites = cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__47023_SHARP_){
return cljs.core.seq(new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(p1__47023_SHARP_));
}),cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","invite-rows","oc.web.components.ui.onboard-wrapper/invite-rows",-204272148).cljs$core$IFn$_invoke$arity$1(s)));
return oc.web.actions.team.invite_users.cljs$core$IFn$_invoke$arity$variadic(not_empty_invites,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([""], 0));
} else {
return null;
}
});
var continue_disabled = (!((cljs.core.count(error_rows) === (0))));
return React.createElement("div",({"className": "onboard-lander lander-invite"}),React.createElement("header",({"className": "main-cta"}),React.createElement("div",({"className": "title"}),"Invite your team")),React.createElement("div",({"className": "subtitle"}),"Invite some colleagues to explore Wut with you."),React.createElement("div",({"className": "onboard-form"}),React.createElement("form",({"onSubmit": (function (e){
return e.preventDefault();
})}),React.createElement("div",({"className": "field-label invite-teammates"}),"Invite teammates ",React.createElement("span",({"className": "info"}),"(optional)"),React.createElement("button",({"onClick": (function (){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","invite-rows","oc.web.components.ui.onboard-wrapper/invite-rows",-204272148).cljs$core$IFn$_invoke$arity$1(s),cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","invite-rows","oc.web.components.ui.onboard-wrapper/invite-rows",-204272148).cljs$core$IFn$_invoke$arity$1(s)),oc.web.components.ui.onboard_wrapper.default_invite_row)));
}), "className": "mlb-reset add-another-invite-row"}),"+ add more")),sablono.interpreter.interpret((cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","invite-error","oc.web.components.ui.onboard-wrapper/invite-error",477343172).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.error","div.error",314336058),cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","invite-error","oc.web.components.ui.onboard-wrapper/invite-error",477343172).cljs$core$IFn$_invoke$arity$1(s))], null):null)),React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["invite-rows",oc.web.lib.utils.hide_class], null))}),cljs.core.into_array.cljs$core$IFn$_invoke$arity$1((function (){var iter__4529__auto__ = (function oc$web$components$ui$onboard_wrapper$iter__47030(s__47031){
return (new cljs.core.LazySeq(null,(function (){
var s__47031__$1 = s__47031;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__47031__$1);
if(temp__5735__auto__){
var s__47031__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__47031__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__47031__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__47033 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__47032 = (0);
while(true){
if((i__47032 < size__4528__auto__)){
var idx = cljs.core._nth(c__4527__auto__,i__47032);
var invite = cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","invite-rows","oc.web.components.ui.onboard-wrapper/invite-rows",-204272148).cljs$core$IFn$_invoke$arity$1(s)),idx);
cljs.core.chunk_append(b__47033,React.createElement("div",({"key": ["invite-row-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","invite-rand","oc.web.components.ui.onboard-wrapper/invite-rand",-1271301569).cljs$core$IFn$_invoke$arity$1(s))),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(idx)].join(''), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["invite-row",(cljs.core.truth_(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(invite))?"error":null)], null))}),sablono.interpreter.create_element("input",({"type": "text", "placeholder": "name@example.com", "onChange": ((function (i__47032,invite,idx,c__4527__auto__,size__4528__auto__,b__47033,s__47031__$2,temp__5735__auto__,_,org_data,valid_rows,error_rows,continue_fn,continue_disabled){
return (function (e){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","invite-rows","oc.web.components.ui.onboard-wrapper/invite-rows",-204272148).cljs$core$IFn$_invoke$arity$1(s),cljs.core.vec(cljs.core.assoc_in(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","invite-rows","oc.web.components.ui.onboard-wrapper/invite-rows",-204272148).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [idx], null),cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(invite,new cljs.core.Keyword(null,"user","user",1532431356),e.target.value))));

return oc.web.components.ui.onboard_wrapper.check_invites(s);
});})(i__47032,invite,idx,c__4527__auto__,size__4528__auto__,b__47033,s__47031__$2,temp__5735__auto__,_,org_data,valid_rows,error_rows,continue_fn,continue_disabled))
, "value": new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(invite), "className": "oc-input"}))));

var G__47097 = (i__47032 + (1));
i__47032 = G__47097;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__47033),oc$web$components$ui$onboard_wrapper$iter__47030(cljs.core.chunk_rest(s__47031__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__47033),null);
}
} else {
var idx = cljs.core.first(s__47031__$2);
var invite = cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","invite-rows","oc.web.components.ui.onboard-wrapper/invite-rows",-204272148).cljs$core$IFn$_invoke$arity$1(s)),idx);
return cljs.core.cons(React.createElement("div",({"key": ["invite-row-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","invite-rand","oc.web.components.ui.onboard-wrapper/invite-rand",-1271301569).cljs$core$IFn$_invoke$arity$1(s))),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(idx)].join(''), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["invite-row",(cljs.core.truth_(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(invite))?"error":null)], null))}),sablono.interpreter.create_element("input",({"type": "text", "placeholder": "name@example.com", "onChange": ((function (invite,idx,s__47031__$2,temp__5735__auto__,_,org_data,valid_rows,error_rows,continue_fn,continue_disabled){
return (function (e){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","invite-rows","oc.web.components.ui.onboard-wrapper/invite-rows",-204272148).cljs$core$IFn$_invoke$arity$1(s),cljs.core.vec(cljs.core.assoc_in(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","invite-rows","oc.web.components.ui.onboard-wrapper/invite-rows",-204272148).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [idx], null),cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(invite,new cljs.core.Keyword(null,"user","user",1532431356),e.target.value))));

return oc.web.components.ui.onboard_wrapper.check_invites(s);
});})(invite,idx,s__47031__$2,temp__5735__auto__,_,org_data,valid_rows,error_rows,continue_fn,continue_disabled))
, "value": new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(invite), "className": "oc-input"}))),oc$web$components$ui$onboard_wrapper$iter__47030(cljs.core.rest(s__47031__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(cljs.core.range.cljs$core$IFn$_invoke$arity$1(cljs.core.count(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","invite-rows","oc.web.components.ui.onboard-wrapper/invite-rows",-204272148).cljs$core$IFn$_invoke$arity$1(s)))));
})())),React.createElement("button",({"onTouchStart": cljs.core.identity, "onClick": continue_fn, "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["continue",(cljs.core.truth_((function (){var or__4126__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","inviting","oc.web.components.ui.onboard-wrapper/inviting",-1609819584).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return continue_disabled;
}
})())?"disabled":null)], null))}),"Invite team"),React.createElement("button",({"onClick": (function (){
return oc.web.actions.org.signup_invite_completed(org_data);
}), "className": "mlb-reset skip-for-now"}),"Skip for now"))));
}),new cljs.core.PersistentVector(null, 9, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"org-data","org-data",96720321)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"invite-users","invite-users",107417337)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","inviting","oc.web.components.ui.onboard-wrapper/inviting",-1609819584)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","invite-error","oc.web.components.ui.onboard-wrapper/invite-error",477343172)),rum.core.local.cljs$core$IFn$_invoke$arity$2(cljs.core.rand.cljs$core$IFn$_invoke$arity$1((3)),new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","invite-rand","oc.web.components.ui.onboard-wrapper/invite-rand",-1271301569)),rum.core.local.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentVector.EMPTY,new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","invite-rows","oc.web.components.ui.onboard-wrapper/invite-rows",-204272148)),rum.core.local.cljs$core$IFn$_invoke$arity$2(oc.web.components.ui.onboard_wrapper.default_invite_note,new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","invite-note","oc.web.components.ui.onboard-wrapper/invite-note",1670276779)),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
var rows_47098 = ((oc.web.lib.responsive.is_tablet_or_mobile_QMARK_())?(2):(3));
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","invite-rows","oc.web.components.ui.onboard-wrapper/invite-rows",-204272148).cljs$core$IFn$_invoke$arity$1(s),cljs.core.vec(cljs.core.repeat.cljs$core$IFn$_invoke$arity$2(rows_47098,oc.web.components.ui.onboard_wrapper.default_invite_row)));

return s;
}),new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
oc.web.actions.team.teams_get_if_needed();

return s;
}),new cljs.core.Keyword(null,"will-update","will-update",328062998),(function (s){
oc.web.actions.team.teams_get_if_needed();

if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","inviting","oc.web.components.ui.onboard-wrapper/inviting",-1609819584).cljs$core$IFn$_invoke$arity$1(s)))){
var invite_users_47099 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"invite-users","invite-users",107417337)));
var invite_errors_47100 = cljs.core.filter.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"error","error",-978969032),invite_users_47099);
var to_send_47101 = cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__47020_SHARP_){
return cljs.core.not(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(p1__47020_SHARP_));
}),invite_users_47099);
if((cljs.core.count(to_send_47101) === (0))){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","inviting","oc.web.components.ui.onboard-wrapper/inviting",-1609819584).cljs$core$IFn$_invoke$arity$1(s),false);

if((cljs.core.count(invite_errors_47100) > (0))){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","invite-rand","oc.web.components.ui.onboard-wrapper/invite-rand",-1271301569).cljs$core$IFn$_invoke$arity$1(s),cljs.core.rand.cljs$core$IFn$_invoke$arity$1((3)));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","invite-error","oc.web.components.ui.onboard-wrapper/invite-error",477343172).cljs$core$IFn$_invoke$arity$1(s),"An error occurred inviting the following users, please try again.");

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","invite-rows","oc.web.components.ui.onboard-wrapper/invite-rows",-204272148).cljs$core$IFn$_invoke$arity$1(s),cljs.core.vec(invite_errors_47100));
} else {
oc.web.actions.org.signup_invite_completed(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"org-data","org-data",96720321))));
}
} else {
}
} else {
}

return s;
})], null)], null),"lander-invite");
oc.web.components.ui.onboard_wrapper.dots_animation = (function oc$web$components$ui$onboard_wrapper$dots_animation(s){
var temp__5735__auto__ = rum.core.ref_node(s,new cljs.core.Keyword(null,"dots","dots",714343900));
if(cljs.core.truth_(temp__5735__auto__)){
var dots_node = temp__5735__auto__;
var dots = dots_node.innerText;
var next_dots = (function (){var G__47035 = dots;
switch (G__47035) {
case ".":
return "..";

break;
case "..":
return "...";

break;
default:
return ".";

}
})();
(dots_node.innerText = next_dots);

return oc.web.lib.utils.after((800),(function (){
return (oc.web.components.ui.onboard_wrapper.dots_animation.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.onboard_wrapper.dots_animation.cljs$core$IFn$_invoke$arity$1(s) : oc.web.components.ui.onboard_wrapper.dots_animation.call(null,s));
}));
} else {
return null;
}
});
oc.web.components.ui.onboard_wrapper.invitee_team_lander = rum.core.build_defcs((function (s){
var team_invite_drv = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"team-invite","team-invite",-340365962));
var auth_settings = new cljs.core.Keyword(null,"auth-settings","auth-settings",-1775088219).cljs$core$IFn$_invoke$arity$1(team_invite_drv);
var email_signup_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(auth_settings),"create","POST",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"auth-source","auth-source",1912135250),"email"], null)], 0));
var team_data = new cljs.core.Keyword(null,"team","team",1355747699).cljs$core$IFn$_invoke$arity$1(auth_settings);
var signup_with_email = org.martinklepsch.derivatives.react(s,oc.web.stores.user.signup_with_email);
return React.createElement("div",({"className": "onboard-lander invitee-team-lander"}),(function (){var attrs47038 = ((oc.shared.useragent.mobile_app_QMARK_)?null:new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.top-back-button","button.mlb-reset.top-back-button",949619457),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"on-touch-start","on-touch-start",447239419),cljs.core.identity,new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.router.history_back_BANG_();
}),new cljs.core.Keyword(null,"aria-label","aria-label",455891514),"Back"], null)], null));
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"header",((cljs.core.map_QMARK_(attrs47038))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["main-cta"], null)], null),attrs47038], 0))):({"className": "main-cta"})),((cljs.core.map_QMARK_(attrs47038))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [(cljs.core.truth_(auth_settings)?(cljs.core.truth_(new cljs.core.Keyword(null,"team","team",1355747699).cljs$core$IFn$_invoke$arity$1(auth_settings))?React.createElement("div",({"className": "title"}),"Your team is using Wut to share updates"):React.createElement("div",({"className": "title"}),"Oh oh...")):React.createElement("div",({"className": "title"}),"Please wait"))], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs47038),(cljs.core.truth_(auth_settings)?(cljs.core.truth_(new cljs.core.Keyword(null,"team","team",1355747699).cljs$core$IFn$_invoke$arity$1(auth_settings))?React.createElement("div",({"className": "title"}),"Your team is using Wut to share updates"):React.createElement("div",({"className": "title"}),"Oh oh...")):React.createElement("div",({"className": "title"}),"Please wait"))], null)));
})(),(cljs.core.truth_(auth_settings)?(cljs.core.truth_(new cljs.core.Keyword(null,"team","team",1355747699).cljs$core$IFn$_invoke$arity$1(auth_settings))?React.createElement("div",({"className": "onboard-form"}),React.createElement("form",({"onSubmit": (function (e){
return e.preventDefault();
})}),React.createElement("div",({"className": "title-container"}),(function (){var attrs47057 = (oc.web.components.ui.org_avatar.org_avatar.cljs$core$IFn$_invoke$arity$3 ? oc.web.components.ui.org_avatar.org_avatar.cljs$core$IFn$_invoke$arity$3(team_data,false,new cljs.core.Keyword(null,"never","never",50472977)) : oc.web.components.ui.org_avatar.org_avatar.call(null,team_data,false,new cljs.core.Keyword(null,"never","never",50472977)));
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs47057))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["team-logo-container"], null)], null),attrs47057], 0))):({"className": "team-logo-container"})),((cljs.core.map_QMARK_(attrs47057))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs47057)], null)));
})(),React.createElement("div",({"className": "title main-lander"}),"Join ",sablono.interpreter.interpret(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(team_data))," on Wut")),React.createElement("div",({"className": "field-label email-field"}),"Work email",sablono.interpreter.interpret(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(signup_with_email),(409)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.error","span.error",-283487575),"Email already exists"], null):(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","email-error","oc.web.components.ui.onboard-wrapper/email-error",-582475544).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.error","span.error",-283487575),"Email is not valid"], null):null)))),sablono.interpreter.create_element("input",({"type": "email", "pattern": oc.web.lib.utils.valid_email_pattern, "value": cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","email","oc.web.components.ui.onboard-wrapper/email",1982191867).cljs$core$IFn$_invoke$arity$1(s)), "onChange": (function (p1__47036_SHARP_){
var v = p1__47036_SHARP_.target.value;
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","password-error","oc.web.components.ui.onboard-wrapper/password-error",1673013435).cljs$core$IFn$_invoke$arity$1(s),false);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","email-error","oc.web.components.ui.onboard-wrapper/email-error",-582475544).cljs$core$IFn$_invoke$arity$1(s),false);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","email","oc.web.components.ui.onboard-wrapper/email",1982191867).cljs$core$IFn$_invoke$arity$1(s),v);
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["field","oc-input",oc.web.lib.utils.class_set(cljs.core.PersistentArrayMap.createAsIfByAssoc([new cljs.core.Keyword(null,"error","error",-978969032),cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(signup_with_email),(409)),oc.web.lib.utils.hide_class,true]))], null))})),React.createElement("div",({"className": "field-label"}),"Password",sablono.interpreter.interpret((cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","password-error","oc.web.components.ui.onboard-wrapper/password-error",1673013435).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.error","span.error",-283487575),"Minimum 8 characters"], null):null))),sablono.interpreter.create_element("input",({"type": "password", "pattern": ".{8,}", "value": cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","pswd","oc.web.components.ui.onboard-wrapper/pswd",1910006868).cljs$core$IFn$_invoke$arity$1(s)), "placeholder": "Minimum 8 characters", "onChange": (function (p1__47037_SHARP_){
var v = p1__47037_SHARP_.target.value;
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","password-error","oc.web.components.ui.onboard-wrapper/password-error",1673013435).cljs$core$IFn$_invoke$arity$1(s),false);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","email-error","oc.web.components.ui.onboard-wrapper/email-error",-582475544).cljs$core$IFn$_invoke$arity$1(s),false);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","pswd","oc.web.components.ui.onboard-wrapper/pswd",1910006868).cljs$core$IFn$_invoke$arity$1(s),v);
}), "className": "field oc-input"})),React.createElement("div",({"className": "field-description"}),"By signing up you are agreeing to our ",React.createElement("a",({"href": oc.web.urls.terms}),"terms of service")," and ",React.createElement("a",({"href": oc.web.urls.privacy}),"privacy policy"),"."),React.createElement("button",({"onTouchStart": cljs.core.identity, "onClick": (function (){
if(((cljs.core.not(oc.web.lib.utils.valid_email_QMARK_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","email","oc.web.components.ui.onboard-wrapper/email",1982191867).cljs$core$IFn$_invoke$arity$1(s))))) || ((cljs.core.count(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","pswd","oc.web.components.ui.onboard-wrapper/pswd",1910006868).cljs$core$IFn$_invoke$arity$1(s))) <= (7))))){
if(cljs.core.truth_(oc.web.lib.utils.valid_email_QMARK_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","email","oc.web.components.ui.onboard-wrapper/email",1982191867).cljs$core$IFn$_invoke$arity$1(s))))){
} else {
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","email-error","oc.web.components.ui.onboard-wrapper/email-error",-582475544).cljs$core$IFn$_invoke$arity$1(s),true);
}

if((cljs.core.count(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","pswd","oc.web.components.ui.onboard-wrapper/pswd",1910006868).cljs$core$IFn$_invoke$arity$1(s))) <= (7))){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","password-error","oc.web.components.ui.onboard-wrapper/password-error",1673013435).cljs$core$IFn$_invoke$arity$1(s),true);
} else {
return null;
}
} else {
return oc.web.actions.user.signup_with_email.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"email","email",1415816706),cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","email","oc.web.components.ui.onboard-wrapper/email",1982191867).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"pswd","pswd",278786885),cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","pswd","oc.web.components.ui.onboard-wrapper/pswd",1910006868).cljs$core$IFn$_invoke$arity$1(s))], null),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true], 0));
}
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["continue",((((cljs.core.not(oc.web.lib.utils.valid_email_QMARK_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","email","oc.web.components.ui.onboard-wrapper/email",1982191867).cljs$core$IFn$_invoke$arity$1(s))))) || ((cljs.core.count(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","pswd","oc.web.components.ui.onboard-wrapper/pswd",1910006868).cljs$core$IFn$_invoke$arity$1(s))) <= (7)))))?"disabled":null)], null))}),sablono.interpreter.interpret(["Join ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(team_data))].join(''))))):(function (){var attrs47056 = ["The invite link you\u2019re trying to access ","has been deactivated by your account admin ","and is no longer valid."].join('');
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs47056))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["subtitle","token-error"], null)], null),attrs47056], 0))):({"className": "subtitle token-error"})),((cljs.core.map_QMARK_(attrs47056))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs47056)], null)));
})()):(function (){var attrs47051 = (oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.small_loading.small_loading.call(null));
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs47051))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["subtitle","checking-invitation"], null)], null),attrs47051], 0))):({"className": "subtitle checking-invitation"})),((cljs.core.map_QMARK_(attrs47051))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Checking invitation link",React.createElement("span",({"ref": "dots", "className": "dots"}),".")], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs47051),"Checking invitation link",React.createElement("span",({"ref": "dots", "className": "dots"}),".")], null)));
})()));
}),new cljs.core.PersistentVector(null, 9, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"team-invite","team-invite",-340365962)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([oc.web.stores.user.signup_with_email], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","email-error","oc.web.components.ui.onboard-wrapper/email-error",-582475544)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","password-error","oc.web.components.ui.onboard-wrapper/password-error",1673013435)),rum.core.local.cljs$core$IFn$_invoke$arity$2("",new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","email","oc.web.components.ui.onboard-wrapper/email",1982191867)),rum.core.local.cljs$core$IFn$_invoke$arity$2("",new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","pswd","oc.web.components.ui.onboard-wrapper/pswd",1910006868)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","auth-settings-loaded","oc.web.components.ui.onboard-wrapper/auth-settings-loaded",35520884)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
oc.web.components.ui.onboard_wrapper.dots_animation(s);

return s;
}),new cljs.core.Keyword(null,"will-update","will-update",328062998),(function (s){
var auth_settings_47103 = new cljs.core.Keyword(null,"auth-settings","auth-settings",-1775088219).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"team-invite","team-invite",-340365962))));
if(cljs.core.truth_(((cljs.core.not(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","auth-settings-loaded","oc.web.components.ui.onboard-wrapper/auth-settings-loaded",35520884).cljs$core$IFn$_invoke$arity$1(s))))?(function (){var and__4115__auto__ = auth_settings_47103;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(new cljs.core.Keyword(null,"team","team",1355747699).cljs$core$IFn$_invoke$arity$1(auth_settings_47103));
} else {
return and__4115__auto__;
}
})():false))){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","auth-settings-loaded","oc.web.components.ui.onboard-wrapper/auth-settings-loaded",35520884).cljs$core$IFn$_invoke$arity$1(s),true);
} else {
}

return s;
})], null)], null),"invitee-team-lander");
oc.web.components.ui.onboard_wrapper.confirm_invitation_when_ready = (function oc$web$components$ui$onboard_wrapper$confirm_invitation_when_ready(s){
var confirm_invitation = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"confirm-invitation","confirm-invitation",1250144934)));
if(cljs.core.truth_((function (){var and__4115__auto__ = new cljs.core.Keyword(null,"auth-settings","auth-settings",-1775088219).cljs$core$IFn$_invoke$arity$1(confirm_invitation);
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","exchange-started","oc.web.components.ui.onboard-wrapper/exchange-started",-1369071517).cljs$core$IFn$_invoke$arity$1(s)));
} else {
return and__4115__auto__;
}
})())){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","exchange-started","oc.web.components.ui.onboard-wrapper/exchange-started",-1369071517).cljs$core$IFn$_invoke$arity$1(s),true);

return oc.web.actions.user.confirm_invitation(new cljs.core.Keyword(null,"token","token",-1211463215).cljs$core$IFn$_invoke$arity$1(confirm_invitation));
} else {
return null;
}
});
oc.web.components.ui.onboard_wrapper.invitee_lander = rum.core.build_defcs((function (s){
var confirm_invitation = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"confirm-invitation","confirm-invitation",1250144934));
return React.createElement("div",({"className": "onboard-lander invitee-lander"}),React.createElement("header",({"className": "main-cta"}),React.createElement("div",({"className": "invite-container"}),React.createElement("div",({"className": "title"}),"Join your team on Wut"))),(cljs.core.truth_(new cljs.core.Keyword(null,"invitation-error","invitation-error",1846843525).cljs$core$IFn$_invoke$arity$1(confirm_invitation))?React.createElement("div",({"className": "subtitle token-error"}),"An error occurred while confirming your invitation, please try again."):(function (){var attrs47062 = (oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.small_loading.small_loading.call(null));
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs47062))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["subtitle","checking-invitation"], null)], null),attrs47062], 0))):({"className": "subtitle checking-invitation"})),((cljs.core.map_QMARK_(attrs47062))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Checking invitation link",React.createElement("span",({"ref": "dots", "className": "dots"}),".")], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs47062),"Checking invitation link",React.createElement("span",({"ref": "dots", "className": "dots"}),".")], null)));
})()));
}),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"confirm-invitation","confirm-invitation",1250144934)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","exchange-started","oc.web.components.ui.onboard-wrapper/exchange-started",-1369071517)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","exchange-ended","oc.web.components.ui.onboard-wrapper/exchange-ended",1723367961)),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
oc.web.components.ui.onboard_wrapper.confirm_invitation_when_ready(s);

return s;
}),new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
oc.web.components.ui.onboard_wrapper.dots_animation(s);

oc.web.components.ui.onboard_wrapper.confirm_invitation_when_ready(s);

return s;
}),new cljs.core.Keyword(null,"did-update","did-update",-2143702256),(function (s){
oc.web.components.ui.onboard_wrapper.confirm_invitation_when_ready(s);

return s;
})], null)], null),"invitee-lander");
oc.web.components.ui.onboard_wrapper.invitee_lander_password = rum.core.build_defcs((function (s){
var collect_password_drv = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"collect-password","collect-password",690937675));
var jwt = new cljs.core.Keyword(null,"jwt","jwt",1504015441).cljs$core$IFn$_invoke$arity$1(collect_password_drv);
var collect_pswd = new cljs.core.Keyword(null,"collect-pswd","collect-pswd",-1287645874).cljs$core$IFn$_invoke$arity$1(collect_password_drv);
var collect_pswd_error = new cljs.core.Keyword(null,"collect-pswd-error","collect-pswd-error",-2005825999).cljs$core$IFn$_invoke$arity$1(collect_password_drv);
var continue_disabled_QMARK_ = (cljs.core.count(new cljs.core.Keyword(null,"pswd","pswd",278786885).cljs$core$IFn$_invoke$arity$1(collect_pswd)) <= (7));
var continue_fn = (function (){
if(continue_disabled_QMARK_){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","password-error","oc.web.components.ui.onboard-wrapper/password-error",1673013435).cljs$core$IFn$_invoke$arity$1(s),true);
} else {
return oc.web.actions.user.pswd_collect(collect_pswd,false);
}
});
var is_mobile_QMARK_ = oc.web.lib.responsive.is_mobile_size_QMARK_();
return React.createElement("div",({"className": "onboard-lander invitee-lander-password"}),React.createElement("header",({"className": "main-cta"}),React.createElement("div",({"className": "title"}),"Join your team on Wut"),React.createElement("button",({"onClick": continue_fn, "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["mlb-reset","top-continue",((continue_disabled_QMARK_)?"disabled":null)], null))}),"Continue")),React.createElement("div",({"className": "onboard-form"}),React.createElement("div",({"className": "form-title"}),"Set a password to get started"),React.createElement("form",({"onSubmit": (function (e){
return e.preventDefault();
})}),React.createElement("div",({"className": "field-label email-field"}),"Work email"),React.createElement("div",({"className": "email-field-tooltip-container"})),sablono.interpreter.create_element("input",({"type": "email", "value": new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(jwt), "readOnly": true, "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["field","oc-input","email-field",oc.web.lib.utils.hide_class], null))})),React.createElement("div",({"className": "field-description"}),"You were invited to join your team using this email."),React.createElement("div",({"className": "field-label"}),"Password",sablono.interpreter.interpret((cljs.core.truth_(collect_pswd_error)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.error","span.error",-283487575),"An error occurred, please try again."], null):null)),sablono.interpreter.interpret((cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","password-error","oc.web.components.ui.onboard-wrapper/password-error",1673013435).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.error","span.error",-283487575),"Minimum 8 characters"], null):null))),sablono.interpreter.create_element("input",({"type": "password", "value": (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"pswd","pswd",278786885).cljs$core$IFn$_invoke$arity$1(collect_pswd);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "";
}
})(), "ref": "password", "onChange": (function (p1__47067_SHARP_){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","password-error","oc.web.components.ui.onboard-wrapper/password-error",1673013435).cljs$core$IFn$_invoke$arity$1(s),false);

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"collect-pswd","collect-pswd",-1287645874),new cljs.core.Keyword(null,"pswd","pswd",278786885)], null),p1__47067_SHARP_.target.value], null));
}), "placeholder": "Minimum 8 characters", "pattern": ".{8,}", "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["field","oc-input",(cljs.core.truth_(collect_pswd_error)?"error":null)], null))})),React.createElement("button",({"onTouchStart": cljs.core.identity, "onClick": continue_fn, "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["continue",((continue_disabled_QMARK_)?"disabled":null)], null))}),"Continue"),React.createElement("div",({"className": "field-description"}),"By signing up you are agreeing to our ",React.createElement("a",({"href": oc.web.urls.terms}),"terms of service")," and ",React.createElement("a",({"href": oc.web.urls.privacy}),"privacy policy"),"."))));
}),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"collect-password","collect-password",690937675)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","password-error","oc.web.components.ui.onboard-wrapper/password-error",1673013435)),oc.web.mixins.ui.refresh_tooltips_mixin,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
oc.web.components.ui.onboard_wrapper.delay_focus_field_with_ref(s,"password");

return s;
})], null)], null),"invitee-lander-password");
oc.web.components.ui.onboard_wrapper.invitee_lander_profile = rum.core.build_defcs((function (s){
var edit_user_profile = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"edit-user-profile","edit-user-profile",-479059118));
var current_user_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915));
var user_data = new cljs.core.Keyword(null,"user-data","user-data",2143823568).cljs$core$IFn$_invoke$arity$1(edit_user_profile);
var is_mobile_QMARK_ = oc.web.lib.responsive.is_mobile_size_QMARK_();
return React.createElement("div",({"className": "onboard-lander invitee-lander-profile"}),React.createElement("header",({"className": "main-cta"}),React.createElement("div",({"className": "title about-yourself"}),"Tell us about you")),sablono.interpreter.interpret((cljs.core.truth_(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(edit_user_profile))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.subtitle.error","div.subtitle.error",-11627825),"An error occurred while saving your data, please try again"], null):null)),React.createElement("div",({"className": "onboard-form"}),React.createElement("form",({"onSubmit": (function (e){
return e.preventDefault();
})}),React.createElement("div",({"className": "form-title"}),"Sign up"),React.createElement("button",({"onClick": (function (){
return oc.web.components.ui.onboard_wrapper.upload_user_profile_picture_clicked(s);
}), "ref": "user-profile-avatar", "data-toggle": (cljs.core.truth_(is_mobile_QMARK_)?null:"tooltip"), "title": "Change avatar", "data-placement": "top", "className": "mlb-reset user-profile-avatar"}),sablono.interpreter.interpret((oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1(user_data) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,user_data))),React.createElement("div",({"className": "plus-icon"}))),React.createElement("div",({"className": "field-label"}),"First name"),sablono.interpreter.create_element("input",({"type": "text", "ref": "first-name", "placeholder": "First name...", "value": new cljs.core.Keyword(null,"first-name","first-name",-1559982131).cljs$core$IFn$_invoke$arity$1(user_data), "maxLength": oc.web.utils.user.user_name_max_lenth, "onChange": (function (p1__47078_SHARP_){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"edit-user-profile","edit-user-profile",-479059118),new cljs.core.Keyword(null,"first-name","first-name",-1559982131)], null),p1__47078_SHARP_.target.value], null));
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["field","oc-input",oc.web.lib.utils.hide_class], null))})),React.createElement("div",({"className": "field-label"}),"Last name"),sablono.interpreter.create_element("input",({"type": "text", "placeholder": "Last name...", "value": new cljs.core.Keyword(null,"last-name","last-name",-1695738974).cljs$core$IFn$_invoke$arity$1(user_data), "maxLength": oc.web.utils.user.user_name_max_lenth, "onChange": (function (p1__47079_SHARP_){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"edit-user-profile","edit-user-profile",-479059118),new cljs.core.Keyword(null,"last-name","last-name",-1695738974)], null),p1__47079_SHARP_.target.value], null));
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["field","oc-input",oc.web.lib.utils.hide_class], null))})),React.createElement("button",({"disabled": ((cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"first-name","first-name",-1559982131).cljs$core$IFn$_invoke$arity$1(user_data))) && (cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"last-name","last-name",-1695738974).cljs$core$IFn$_invoke$arity$1(user_data)))), "onTouchStart": cljs.core.identity, "onClick": (function (){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","saving","oc.web.components.ui.onboard-wrapper/saving",1021445936).cljs$core$IFn$_invoke$arity$1(s),true);

return oc.web.actions.user.onboard_profile_save.cljs$core$IFn$_invoke$arity$2(current_user_data,edit_user_profile);
}), "className": "continue start-using-carrot"}),"Start using Wut"))));
}),new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"edit-user-profile","edit-user-profile",-479059118)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"orgs","orgs",155776628)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","saving","oc.web.components.ui.onboard-wrapper/saving",1021445936)),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
oc.web.actions.user.user_profile_reset();

return s;
}),new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
oc.web.components.ui.onboard_wrapper.delay_focus_field_with_ref(s,"first-name");

return s;
}),new cljs.core.Keyword(null,"will-update","will-update",328062998),(function (s){
var edit_user_profile_47104 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"edit-user-profile","edit-user-profile",-479059118)));
var orgs_47105 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"orgs","orgs",155776628)));
if(cljs.core.truth_((function (){var and__4115__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","saving","oc.web.components.ui.onboard-wrapper/saving",1021445936).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(and__4115__auto__)){
return ((cljs.core.not(new cljs.core.Keyword(null,"loading","loading",-737050189).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-data","user-data",2143823568).cljs$core$IFn$_invoke$arity$1(edit_user_profile_47104)))) && (cljs.core.not(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(edit_user_profile_47104))));
} else {
return and__4115__auto__;
}
})())){
oc.web.lib.utils.after((100),(function (){
return oc.web.router.nav_BANG_(oc.web.urls.org.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(cljs.core.first(orgs_47105))));
}));
} else {
}

return s;
})], null)], null),"invitee-lander-profile");
oc.web.components.ui.onboard_wrapper.email_wall = rum.core.build_defcs((function (s){
var email = new cljs.core.Keyword(null,"e","e",1381269198).cljs$core$IFn$_invoke$arity$1(org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"query-params","query-params",900640534)));
return React.createElement("div",({"className": "onboard-email-container email-wall"}),React.createElement("div",({"className": "email-wall-icon"})),"Please verify your email",(function (){var attrs47092 = ["Before you can join your team, we just need to verify your idetity. ","Please check your email, and continue the registration process from there."].join('');
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs47092))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["email-wall-subtitle"], null)], null),attrs47092], 0))):({"className": "email-wall-subtitle"})),((cljs.core.map_QMARK_(attrs47092))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs47092)], null)));
})(),React.createElement("div",({"className": "email-wall-sent-link"}),"We have sent an email to",((cljs.core.seq(email))?":":" "),((cljs.core.seq(email))?React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["email-address",oc.web.lib.utils.hide_class], null))}),sablono.interpreter.interpret(email)):"your email address"),"."));
}),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"query-params","query-params",900640534)], 0))], null),"email-wall");
oc.web.components.ui.onboard_wrapper.exchange_token_when_ready = (function oc$web$components$ui$onboard_wrapper$exchange_token_when_ready(s){
var temp__5735__auto__ = new cljs.core.Keyword(null,"auth-settings","auth-settings",-1775088219).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"email-verification","email-verification",-2006200871))));
if(cljs.core.truth_(temp__5735__auto__)){
var auth_settings = temp__5735__auto__;
if(cljs.core.truth_(((cljs.core.not(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","exchange-started","oc.web.components.ui.onboard-wrapper/exchange-started",-1369071517).cljs$core$IFn$_invoke$arity$1(s))))?oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(auth_settings),"authenticate","GET",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"auth-source","auth-source",1912135250),"email"], null)], 0)):false))){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","exchange-started","oc.web.components.ui.onboard-wrapper/exchange-started",-1369071517).cljs$core$IFn$_invoke$arity$1(s),true);

return oc.web.actions.user.auth_with_token(new cljs.core.Keyword(null,"email-verification","email-verification",-2006200871));
} else {
return null;
}
} else {
return null;
}
});
oc.web.components.ui.onboard_wrapper.email_verified = rum.core.build_defcs((function (s){
var email_verification = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"email-verification","email-verification",-2006200871));
var orgs = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"orgs","orgs",155776628));
return sablono.interpreter.interpret(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(email_verification),(401)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.onboard-email-container.error","div.onboard-email-container.error",525393315),"This link is no longer valid."], null):(cljs.core.truth_(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(email_verification))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.onboard-email-container.error","div.onboard-email-container.error",525393315),"An error occurred, please try again."], null):(cljs.core.truth_(new cljs.core.Keyword(null,"success","success",1890645906).cljs$core$IFn$_invoke$arity$1(email_verification))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.onboard-email-container","div.onboard-email-container",-1517940617),"Thanks for verifying",new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.continue","button.mlb-reset.continue",-2080677764),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.actions.user.verify_continue(orgs);
}),new cljs.core.Keyword(null,"on-touch-start","on-touch-start",447239419),cljs.core.identity], null),"Get Started"], null)], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.onboard-email-container.small.dot-animation","div.onboard-email-container.small.dot-animation",-1007899558),"Verifying, please wait",new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.dots","span.dots",-1988809513),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ref","ref",1289896967),new cljs.core.Keyword(null,"dots","dots",714343900)], null),"."], null)], null)
))));
}),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"email-verification","email-verification",-2006200871)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"orgs","orgs",155776628)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","exchange-started","oc.web.components.ui.onboard-wrapper/exchange-started",-1369071517)),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
oc.web.components.ui.onboard_wrapper.exchange_token_when_ready(s);

return s;
}),new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
oc.web.components.ui.onboard_wrapper.dots_animation(s);

oc.web.components.ui.onboard_wrapper.exchange_token_when_ready(s);

return s;
}),new cljs.core.Keyword(null,"did-update","did-update",-2143702256),(function (s){
oc.web.components.ui.onboard_wrapper.exchange_token_when_ready(s);

return s;
})], null)], null),"email-verified");
oc.web.components.ui.onboard_wrapper.exchange_pswd_reset_token_when_ready = (function oc$web$components$ui$onboard_wrapper$exchange_pswd_reset_token_when_ready(s){
var temp__5735__auto__ = new cljs.core.Keyword(null,"auth-settings","auth-settings",-1775088219).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"password-reset","password-reset",1971592302))));
if(cljs.core.truth_(temp__5735__auto__)){
var auth_settings = temp__5735__auto__;
if(cljs.core.truth_(((cljs.core.not(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","exchange-started","oc.web.components.ui.onboard-wrapper/exchange-started",-1369071517).cljs$core$IFn$_invoke$arity$1(s))))?oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(auth_settings),"authenticate","GET",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"auth-source","auth-source",1912135250),"email"], null)], 0)):false))){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","exchange-started","oc.web.components.ui.onboard-wrapper/exchange-started",-1369071517).cljs$core$IFn$_invoke$arity$1(s),true);

return oc.web.actions.user.auth_with_token(new cljs.core.Keyword(null,"password-reset","password-reset",1971592302));
} else {
return null;
}
} else {
return null;
}
});
oc.web.components.ui.onboard_wrapper.password_reset_lander = rum.core.build_defcs((function (s){
var password_reset = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"password-reset","password-reset",1971592302));
return sablono.interpreter.interpret(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(password_reset),(401)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.onboard-email-container.error","div.onboard-email-container.error",525393315),"This link is no longer valid."], null):(cljs.core.truth_(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(password_reset))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.onboard-email-container.error","div.onboard-email-container.error",525393315),"An error occurred, please try again."], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.onboard-email-container.small.dot-animation","div.onboard-email-container.small.dot-animation",-1007899558),"Verifying, please wait",new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.dots","span.dots",-1988809513),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ref","ref",1289896967),new cljs.core.Keyword(null,"dots","dots",714343900)], null),"."], null)], null)
)));
}),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"password-reset","password-reset",1971592302)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.onboard-wrapper","exchange-started","oc.web.components.ui.onboard-wrapper/exchange-started",-1369071517)),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
oc.web.components.ui.onboard_wrapper.exchange_pswd_reset_token_when_ready(s);

return s;
}),new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
oc.web.components.ui.onboard_wrapper.dots_animation(s);

oc.web.components.ui.onboard_wrapper.exchange_pswd_reset_token_when_ready(s);

return s;
}),new cljs.core.Keyword(null,"did-update","did-update",-2143702256),(function (s){
oc.web.components.ui.onboard_wrapper.exchange_pswd_reset_token_when_ready(s);

return s;
})], null)], null),"password-reset-lander");
oc.web.components.ui.onboard_wrapper.get_component = (function oc$web$components$ui$onboard_wrapper$get_component(c){
var G__47093 = c;
var G__47093__$1 = (((G__47093 instanceof cljs.core.Keyword))?G__47093.fqn:null);
switch (G__47093__$1) {
case "lander":
return (oc.web.components.ui.onboard_wrapper.lander.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.onboard_wrapper.lander.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.onboard_wrapper.lander.call(null));

break;
case "lander-profile":
return (oc.web.components.ui.onboard_wrapper.lander_profile.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.onboard_wrapper.lander_profile.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.onboard_wrapper.lander_profile.call(null));

break;
case "lander-team":
return (oc.web.components.ui.onboard_wrapper.lander_team.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.onboard_wrapper.lander_team.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.onboard_wrapper.lander_team.call(null));

break;
case "lander-invite":
return (oc.web.components.ui.onboard_wrapper.lander_invite.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.onboard_wrapper.lander_invite.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.onboard_wrapper.lander_invite.call(null));

break;
case "invitee-lander":
return (oc.web.components.ui.onboard_wrapper.invitee_lander.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.onboard_wrapper.invitee_lander.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.onboard_wrapper.invitee_lander.call(null));

break;
case "invitee-lander-password":
return (oc.web.components.ui.onboard_wrapper.invitee_lander_password.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.onboard_wrapper.invitee_lander_password.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.onboard_wrapper.invitee_lander_password.call(null));

break;
case "invitee-lander-profile":
return (oc.web.components.ui.onboard_wrapper.invitee_lander_profile.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.onboard_wrapper.invitee_lander_profile.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.onboard_wrapper.invitee_lander_profile.call(null));

break;
case "invitee-team-lander":
return (oc.web.components.ui.onboard_wrapper.invitee_team_lander.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.onboard_wrapper.invitee_team_lander.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.onboard_wrapper.invitee_team_lander.call(null));

break;
case "email-wall":
return (oc.web.components.ui.onboard_wrapper.email_wall.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.onboard_wrapper.email_wall.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.onboard_wrapper.email_wall.call(null));

break;
case "email-verified":
return (oc.web.components.ui.onboard_wrapper.email_verified.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.onboard_wrapper.email_verified.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.onboard_wrapper.email_verified.call(null));

break;
case "password-reset-lander":
return (oc.web.components.ui.onboard_wrapper.password_reset_lander.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.onboard_wrapper.password_reset_lander.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.onboard_wrapper.password_reset_lander.call(null));

break;
default:
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632)], null);

}
});
oc.web.components.ui.onboard_wrapper.onboard_wrapper = rum.core.build_defcs((function (s,component){
var ap_loading = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"ap-loading","ap-loading",1563043900));
var user_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915));
var loading_QMARK_ = (function (){var or__4126__auto__ = ap_loading;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var and__4115__auto__ = oc.web.lib.jwt.jwt();
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(user_data);
} else {
return and__4115__auto__;
}
}
})();
return React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["onboard-wrapper-container",(cljs.core.truth_(loading_QMARK_)?"loading":null)], null))}),(cljs.core.truth_(loading_QMARK_)?sablono.interpreter.interpret((function (){var G__47094 = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"loading","loading",-737050189),true], null);
return (oc.web.components.ui.loading.loading.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.loading.loading.cljs$core$IFn$_invoke$arity$1(G__47094) : oc.web.components.ui.loading.loading.call(null,G__47094));
})()):React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["onboard-wrapper",["onboard-",cljs.core.name(component)].join('')], null))}),sablono.interpreter.interpret(oc.web.components.ui.onboard_wrapper.get_component(component)))));
}),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"ap-loading","ap-loading",1563043900)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915)], 0))], null),"onboard-wrapper");

//# sourceMappingURL=oc.web.components.ui.onboard_wrapper.js.map
