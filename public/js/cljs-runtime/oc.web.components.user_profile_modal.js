goog.provide('oc.web.components.user_profile_modal');
var module$node_modules$moment_timezone$index=shadow.js.require("module$node_modules$moment_timezone$index", {});
oc.web.components.user_profile_modal.real_close_cb = (function oc$web$components$user_profile_modal$real_close_cb(editing_user_data,dismiss_action,mobile_back_bt){
if(cljs.core.truth_(new cljs.core.Keyword(null,"has-changes","has-changes",-631476764).cljs$core$IFn$_invoke$arity$1(editing_user_data))){
oc.web.actions.user.user_profile_reset();
} else {
}

(dismiss_action.cljs$core$IFn$_invoke$arity$0 ? dismiss_action.cljs$core$IFn$_invoke$arity$0() : dismiss_action.call(null));

if(cljs.core.truth_(mobile_back_bt)){
return oc.web.actions.nav_sidebar.menu_toggle();
} else {
return null;
}
});
oc.web.components.user_profile_modal.update_tooltip = (function oc$web$components$user_profile_modal$update_tooltip(s){
return oc.web.lib.utils.after((100),(function (){
var header_avatar = rum.core.ref_node(s,"user-profile-avatar");
var $header_avatar = $(header_avatar);
var edit_user_profile_avatar = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"edit-user-profile-avatar","edit-user-profile-avatar",303025729)));
var title = ((cljs.core.empty_QMARK_(edit_user_profile_avatar))?"Add a photo":"Change photo");
var profile_tab_QMARK_ = $header_avatar.hasClass("profile-tab");
if(cljs.core.truth_(profile_tab_QMARK_)){
return $header_avatar.tooltip(({"title": title, "trigger": "hover focus", "position": "top", "container": "body"}));
} else {
return $header_avatar.tooltip("destroy");
}
}));
});
oc.web.components.user_profile_modal.close_cb = (function oc$web$components$user_profile_modal$close_cb(var_args){
var args__4742__auto__ = [];
var len__4736__auto___46754 = arguments.length;
var i__4737__auto___46755 = (0);
while(true){
if((i__4737__auto___46755 < len__4736__auto___46754)){
args__4742__auto__.push((arguments[i__4737__auto___46755]));

var G__46756 = (i__4737__auto___46755 + (1));
i__4737__auto___46755 = G__46756;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((2) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((2)),(0),null)):null);
return oc.web.components.user_profile_modal.close_cb.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__4743__auto__);
});

(oc.web.components.user_profile_modal.close_cb.cljs$core$IFn$_invoke$arity$variadic = (function (current_user_data,dismiss_action,p__46697){
var vec__46698 = p__46697;
var mobile_back_bt = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__46698,(0),null);
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"latest-entry-point","latest-entry-point",2059134699)], null),(0)], null));

if(cljs.core.truth_(new cljs.core.Keyword(null,"has-changes","has-changes",-631476764).cljs$core$IFn$_invoke$arity$1(current_user_data))){
var alert_data = new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"icon","icon",1679606541),"/img/ML/trash.svg",new cljs.core.Keyword(null,"action","action",-811238024),"user-profile-unsaved-edits",new cljs.core.Keyword(null,"message","message",-406056002),"Leave without saving your changes?",new cljs.core.Keyword(null,"link-button-title","link-button-title",-812305976),"Stay",new cljs.core.Keyword(null,"link-button-cb","link-button-cb",559710301),(function (){
return oc.web.components.ui.alert_modal.hide_alert();
}),new cljs.core.Keyword(null,"solid-button-style","solid-button-style",-723792244),new cljs.core.Keyword(null,"red","red",-969428204),new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"Lose changes",new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),(function (){
oc.web.components.ui.alert_modal.hide_alert();

return oc.web.components.user_profile_modal.real_close_cb(current_user_data,dismiss_action,mobile_back_bt);
})], null);
return oc.web.components.ui.alert_modal.show_alert(alert_data);
} else {
return oc.web.components.user_profile_modal.real_close_cb(current_user_data,dismiss_action,mobile_back_bt);
}
}));

(oc.web.components.user_profile_modal.close_cb.cljs$lang$maxFixedArity = (2));

/** @this {Function} */
(oc.web.components.user_profile_modal.close_cb.cljs$lang$applyTo = (function (seq46694){
var G__46695 = cljs.core.first(seq46694);
var seq46694__$1 = cljs.core.next(seq46694);
var G__46696 = cljs.core.first(seq46694__$1);
var seq46694__$2 = cljs.core.next(seq46694__$1);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__46695,G__46696,seq46694__$2);
}));

oc.web.components.user_profile_modal.error_cb = (function oc$web$components$user_profile_modal$error_cb(res,error){
return oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"title","title",636505583),"Image upload error",new cljs.core.Keyword(null,"description","description",-1428560544),"An error occurred while processing your image. Please retry.",new cljs.core.Keyword(null,"expire","expire",-70657108),(3),new cljs.core.Keyword(null,"dismiss","dismiss",412569545),true], null));
});
oc.web.components.user_profile_modal.success_cb = (function oc$web$components$user_profile_modal$success_cb(res){
var url = goog.object.get(res,"url");
if(cljs.core.not(url)){
return oc.web.components.user_profile_modal.error_cb(null,null);
} else {
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"edit-user-profile-avatar","edit-user-profile-avatar",303025729)], null),url], null));

return oc.web.actions.user.user_avatar_save(url);
}
});
oc.web.components.user_profile_modal.progress_cb = (function oc$web$components$user_profile_modal$progress_cb(res,progress){
return null;
});
oc.web.components.user_profile_modal.upload_user_profile_pictuer_clicked = (function oc$web$components$user_profile_modal$upload_user_profile_pictuer_clicked(){
return oc.web.lib.image_upload.upload_BANG_(oc.web.utils.user.user_avatar_filestack_config,oc.web.components.user_profile_modal.success_cb,oc.web.components.user_profile_modal.progress_cb,oc.web.components.user_profile_modal.error_cb);
});
oc.web.components.user_profile_modal.change_BANG_ = (function oc$web$components$user_profile_modal$change_BANG_(s,kc,v){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.user-profile-modal","name-error","oc.web.components.user-profile-modal/name-error",408428786).cljs$core$IFn$_invoke$arity$1(s),false);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.user-profile-modal","email-error","oc.web.components.user-profile-modal/email-error",1213327404).cljs$core$IFn$_invoke$arity$1(s),false);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.user-profile-modal","password-error","oc.web.components.user-profile-modal/password-error",-288924753).cljs$core$IFn$_invoke$arity$1(s),false);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.user-profile-modal","current-password-error","oc.web.components.user-profile-modal/current-password-error",1682446622).cljs$core$IFn$_invoke$arity$1(s),false);

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"edit-user-profile","edit-user-profile",-479059118)], null),kc)),v], null));

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"edit-user-profile","edit-user-profile",-479059118),new cljs.core.Keyword(null,"has-changes","has-changes",-631476764)], null),true], null));
});
oc.web.components.user_profile_modal.save_clicked = (function oc$web$components$user_profile_modal$save_clicked(s){
if(cljs.core.compare_and_set_BANG_(new cljs.core.Keyword("oc.web.components.user-profile-modal","loading","oc.web.components.user-profile-modal/loading",1607512970).cljs$core$IFn$_invoke$arity$1(s),false,true)){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.user-profile-modal","name-error","oc.web.components.user-profile-modal/name-error",408428786).cljs$core$IFn$_invoke$arity$1(s),false);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.user-profile-modal","email-error","oc.web.components.user-profile-modal/email-error",1213327404).cljs$core$IFn$_invoke$arity$1(s),false);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.user-profile-modal","password-error","oc.web.components.user-profile-modal/password-error",-288924753).cljs$core$IFn$_invoke$arity$1(s),false);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.user-profile-modal","current-password-error","oc.web.components.user-profile-modal/current-password-error",1682446622).cljs$core$IFn$_invoke$arity$1(s),false);

var edit_user_profile = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"edit-user-profile","edit-user-profile",-479059118)));
var current_user_data = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915)));
var user_data = new cljs.core.Keyword(null,"user-data","user-data",2143823568).cljs$core$IFn$_invoke$arity$1(edit_user_profile);
if(((cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"first-name","first-name",-1559982131).cljs$core$IFn$_invoke$arity$1(user_data))) && (cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"last-name","last-name",-1695738974).cljs$core$IFn$_invoke$arity$1(user_data))))){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.user-profile-modal","name-error","oc.web.components.user-profile-modal/name-error",408428786).cljs$core$IFn$_invoke$arity$1(s),true);
} else {
if(cljs.core.not(oc.web.lib.utils.valid_email_QMARK_(new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(user_data)))){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.user-profile-modal","email-error","oc.web.components.user-profile-modal/email-error",1213327404).cljs$core$IFn$_invoke$arity$1(s),true);
} else {
if(((cljs.core.seq(new cljs.core.Keyword(null,"password","password",417022471).cljs$core$IFn$_invoke$arity$1(user_data))) && (cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"current-password","current-password",-10574282).cljs$core$IFn$_invoke$arity$1(user_data))))){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.user-profile-modal","current-password-error","oc.web.components.user-profile-modal/current-password-error",1682446622).cljs$core$IFn$_invoke$arity$1(s),true);
} else {
if(((cljs.core.seq(new cljs.core.Keyword(null,"password","password",417022471).cljs$core$IFn$_invoke$arity$1(user_data))) && ((cljs.core.count(new cljs.core.Keyword(null,"password","password",417022471).cljs$core$IFn$_invoke$arity$1(user_data)) < (8))))){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.user-profile-modal","password-error","oc.web.components.user-profile-modal/password-error",-288924753).cljs$core$IFn$_invoke$arity$1(s),true);
} else {
return oc.web.actions.user.user_profile_save.cljs$core$IFn$_invoke$arity$3(current_user_data,edit_user_profile,(function (success){
if(cljs.core.truth_(success)){
oc.web.components.user_profile_modal.real_close_cb(edit_user_profile,(function (){
return oc.web.actions.nav_sidebar.show_user_settings(null);
}),null);
} else {
}

return oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"title","title",636505583),(cljs.core.truth_(success)?"Profile saved":"Error"),new cljs.core.Keyword(null,"description","description",-1428560544),(cljs.core.truth_(success)?"Your profile has been updated.":"An error occurred while saving your profile. Please retry."),new cljs.core.Keyword(null,"expire","expire",-70657108),(3),new cljs.core.Keyword(null,"id","id",-1388402092),(cljs.core.truth_(success)?new cljs.core.Keyword(null,"user-profile-save-succeeded","user-profile-save-succeeded",-446102787):new cljs.core.Keyword(null,"user-profile-save-failed","user-profile-save-failed",-1858127985)),new cljs.core.Keyword(null,"dismiss","dismiss",412569545),true], null));
}));

}
}
}
}
} else {
return null;
}
});
oc.web.components.user_profile_modal.placeholder = (function oc$web$components$user_profile_modal$placeholder(k){
var G__46701 = k;
var G__46701__$1 = (((G__46701 instanceof cljs.core.Keyword))?G__46701.fqn:null);
switch (G__46701__$1) {
case "facebook":
return "facebook.com/...";

break;
case "linked-in":
return "linkedin.com/in/...";

break;
case "instagram":
return "instagram.com/...";

break;
case "twitter":
return "twitter.com/...";

break;
case "email":
return "Your email address";

break;
case "title":
return "CEO, CTO, Designer, Engineer...";

break;
case "location":
return "e.g. New York, NY";

break;
case "blurb":
return "Fascinating facts...";

break;
default:
return "";

}
});
oc.web.components.user_profile_modal.default_value = (function oc$web$components$user_profile_modal$default_value(k){
var G__46702 = k;
var G__46702__$1 = (((G__46702 instanceof cljs.core.Keyword))?G__46702.fqn:null);
switch (G__46702__$1) {
case "facebook":
return "facebook.com/";

break;
case "linked-in":
return "linkedin.com/in/";

break;
case "instagram":
return "instagram.com/";

break;
case "twitter":
return "twitter.com/";

break;
default:
return "";

}
});
oc.web.components.user_profile_modal.user_profile_modal = rum.core.build_defcs((function (s){
var edit_user_profile_avatar = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"edit-user-profile-avatar","edit-user-profile-avatar",303025729));
var is_jelly_head_avatar = cuerdas.core.includes_QMARK_(edit_user_profile_avatar,"/img/ML/happy_face_");
var user_profile_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"edit-user-profile","edit-user-profile",-479059118));
var current_user_data = new cljs.core.Keyword(null,"user-data","user-data",2143823568).cljs$core$IFn$_invoke$arity$1(user_profile_data);
var user_for_avatar = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([current_user_data,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103),edit_user_profile_avatar], null)], 0));
var timezones = module$node_modules$moment_timezone$index.tz.names();
var guessed_timezone = module$node_modules$moment_timezone$index.tz.guess();
var show_password_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"auth-source","auth-source",1912135250).cljs$core$IFn$_invoke$arity$1(current_user_data),"email");
var links_tab_index = cljs.core.atom.cljs$core$IFn$_invoke$arity$1((5));
return React.createElement("div",({"className": "user-profile-modal-container"}),React.createElement("button",({"onClick": (function (){
return oc.web.components.user_profile_modal.close_cb(current_user_data,oc.web.actions.nav_sidebar.close_all_panels);
}), "className": "mlb-reset modal-close-bt"})),React.createElement("div",({"className": "user-profile-modal"}),React.createElement("div",({"className": "user-profile-header"}),React.createElement("div",({"className": "user-profile-header-title"}),"My profile"),React.createElement("button",({"onClick": (function (){
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.user-profile-modal","loading","oc.web.components.user-profile-modal/loading",1607512970).cljs$core$IFn$_invoke$arity$1(s)))){
return null;
} else {
if(cljs.core.truth_(new cljs.core.Keyword(null,"has-changes","has-changes",-631476764).cljs$core$IFn$_invoke$arity$1(current_user_data))){
return oc.web.components.user_profile_modal.save_clicked(s);
} else {
return oc.web.actions.nav_sidebar.show_user_settings(null);
}
}
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["mlb-reset","save-bt",oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"disabled","disabled",-1529784218),(function (){var or__4126__auto__ = cljs.core.not(new cljs.core.Keyword(null,"has-changes","has-changes",-631476764).cljs$core$IFn$_invoke$arity$1(current_user_data));
if(or__4126__auto__){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = cljs.core.deref(new cljs.core.Keyword("oc.web.components.user-profile-modal","show-success","oc.web.components.user-profile-modal/show-success",393393008).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
return cljs.core.deref(new cljs.core.Keyword("oc.web.components.user-profile-modal","loading","oc.web.components.user-profile-modal/loading",1607512970).cljs$core$IFn$_invoke$arity$1(s));
}
}
})()], null))], null))}),(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.user-profile-modal","show-success","oc.web.components.user-profile-modal/show-success",393393008).cljs$core$IFn$_invoke$arity$1(s)))?"Saved!":"Save")),React.createElement("button",({"onClick": (function (_){
return oc.web.components.user_profile_modal.close_cb(current_user_data,(function (){
return oc.web.actions.nav_sidebar.show_user_settings(null);
}));
}), "className": "mlb-reset cancel-bt"}),"Back")),React.createElement("div",({"className": "user-profile-body"}),React.createElement("div",({"ref": "user-profile-avatar", "onClick": (function (){
return oc.web.components.user_profile_modal.upload_user_profile_pictuer_clicked();
}), "className": "user-profile-avatar"}),(cljs.core.truth_(is_jelly_head_avatar)?React.createElement("div",({"className": "empty-user-avatar-placeholder"})):sablono.interpreter.interpret((oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1(user_for_avatar) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,user_for_avatar)))),React.createElement("div",({"className": "user-profile-avatar-label"}),"Select profile photo")),React.createElement("div",({"className": "user-profile-modal-fields"}),React.createElement("form",({"action": ".", "onSubmit": (function (p1__46703_SHARP_){
return oc.web.lib.utils.event_stop(p1__46703_SHARP_);
})}),React.createElement("div",({"className": "field-label big-web-tablet-only"}),React.createElement("label",({"className": "half-field-label", "htmlFor": "profile-first-name"}),"First name"),React.createElement("label",({"className": "half-field-label", "htmlFor": "profile-last-name"}),"Last name"),sablono.interpreter.interpret((cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.user-profile-modal","name-error","oc.web.components.user-profile-modal/name-error",408428786).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.error","span.error",-283487575),"Please provide your name."], null):null))),React.createElement("label",({"className": "field-label mobile-only", "htmlFor": "profile-first-name"}),"First name",sablono.interpreter.interpret((cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.user-profile-modal","name-error","oc.web.components.user-profile-modal/name-error",408428786).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.error","span.error",-283487575),"Please provide your name."], null):null))),sablono.interpreter.create_element("input",({"value": new cljs.core.Keyword(null,"first-name","first-name",-1559982131).cljs$core$IFn$_invoke$arity$1(current_user_data), "type": "text", "tabIndex": (1), "placeholder": oc.web.components.user_profile_modal.placeholder(new cljs.core.Keyword(null,"first-name","first-name",-1559982131)), "id": "profile-first-name", "maxLength": oc.web.utils.user.user_name_max_lenth, "onChange": (function (p1__46704_SHARP_){
return oc.web.components.user_profile_modal.change_BANG_(s,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"first-name","first-name",-1559982131)], null),p1__46704_SHARP_.target.value);
}), "className": "field-value oc-input half-field"})),React.createElement("label",({"className": "field-label mobile-only", "htmlFor": "profile-last-name"}),"Last name"),sablono.interpreter.create_element("input",({"value": new cljs.core.Keyword(null,"last-name","last-name",-1695738974).cljs$core$IFn$_invoke$arity$1(current_user_data), "type": "text", "tabIndex": (2), "id": "profile-last-name", "placeholder": oc.web.components.user_profile_modal.placeholder(new cljs.core.Keyword(null,"last-name","last-name",-1695738974)), "maxLength": oc.web.utils.user.user_name_max_lenth, "onChange": (function (p1__46705_SHARP_){
return oc.web.components.user_profile_modal.change_BANG_(s,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"last-name","last-name",-1695738974)], null),p1__46705_SHARP_.target.value);
}), "className": "field-value oc-input half-field"})),React.createElement("label",({"className": "field-label", "htmlFor": "profile-role"}),"Role"),sablono.interpreter.create_element("input",({"value": new cljs.core.Keyword(null,"title","title",636505583).cljs$core$IFn$_invoke$arity$1(current_user_data), "type": "text", "id": "profile-role", "placeholder": oc.web.components.user_profile_modal.placeholder(new cljs.core.Keyword(null,"title","title",636505583)), "tabIndex": (3), "maxLength": (56), "onChange": (function (p1__46706_SHARP_){
return oc.web.components.user_profile_modal.change_BANG_(s,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"title","title",636505583)], null),p1__46706_SHARP_.target.value);
}), "className": "field-value oc-input"})),React.createElement("div",({"className": "field-label"}),"Email",sablono.interpreter.interpret((cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.user-profile-modal","email-error","oc.web.components.user-profile-modal/email-error",1213327404).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.error","span.error",-283487575),"This email isn't valid."], null):null))),sablono.interpreter.create_element("input",({"value": new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(current_user_data), "placeholder": oc.web.components.user_profile_modal.placeholder(new cljs.core.Keyword(null,"email","email",1415816706)), "readOnly": true, "type": "text", "className": "field-value not-allowed oc-input"})),React.createElement("label",({"className": "field-label", "htmlFor": "profile-blurb"}),"Blurb"),sablono.interpreter.create_element("textarea",({"tabIndex": (3), "placeholder": oc.web.components.user_profile_modal.placeholder(new cljs.core.Keyword(null,"blurb","blurb",-769928228)), "ref": "blurb", "columns": (2), "value": new cljs.core.Keyword(null,"blurb","blurb",-769928228).cljs$core$IFn$_invoke$arity$1(current_user_data), "className": "field-value oc-input", "id": "profile-blurb", "maxLength": (256), "onChange": (function (p1__46707_SHARP_){
return oc.web.components.user_profile_modal.change_BANG_(s,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"blurb","blurb",-769928228)], null),p1__46707_SHARP_.target.value);
})})),React.createElement("label",({"className": "field-label", "htmlFor": "profile-location"}),"Location"),sablono.interpreter.create_element("input",({"value": new cljs.core.Keyword(null,"location","location",1815599388).cljs$core$IFn$_invoke$arity$1(current_user_data), "type": "text", "id": "profile-location", "placeholder": oc.web.components.user_profile_modal.placeholder(new cljs.core.Keyword(null,"location","location",1815599388)), "tabIndex": (4), "maxLength": (56), "onChange": (function (p1__46708_SHARP_){
return oc.web.components.user_profile_modal.change_BANG_(s,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"location","location",1815599388)], null),p1__46708_SHARP_.target.value);
}), "className": "field-value oc-input"})),React.createElement("label",({"className": "field-label", "htmlFor": "profile-timezone"}),"Timezone"),sablono.interpreter.create_element.cljs$core$IFn$_invoke$arity$variadic("select",({"value": new cljs.core.Keyword(null,"timezone","timezone",1831928099).cljs$core$IFn$_invoke$arity$1(current_user_data), "id": "profile-timezone", "tabIndex": (5), "onChange": (function (p1__46709_SHARP_){
return oc.web.components.user_profile_modal.change_BANG_(s,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"timezone","timezone",1831928099)], null),p1__46709_SHARP_.target.value);
}), "className": "field-value oc-input"}),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.into_array.cljs$core$IFn$_invoke$arity$1((function (){var iter__4529__auto__ = (function oc$web$components$user_profile_modal$iter__46736(s__46737){
return (new cljs.core.LazySeq(null,(function (){
var s__46737__$1 = s__46737;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__46737__$1);
if(temp__5735__auto__){
var s__46737__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__46737__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__46737__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__46739 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__46738 = (0);
while(true){
if((i__46738 < size__4528__auto__)){
var t = cljs.core._nth(c__4527__auto__,i__46738);
cljs.core.chunk_append(b__46739,React.createElement("option",({"key": ["timezone-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(t),"-promoted"].join(''), "value": t}),sablono.interpreter.interpret(oc.web.utils.user.readable_tz(t))));

var G__46759 = (i__46738 + (1));
i__46738 = G__46759;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__46739),oc$web$components$user_profile_modal$iter__46736(cljs.core.chunk_rest(s__46737__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__46739),null);
}
} else {
var t = cljs.core.first(s__46737__$2);
return cljs.core.cons(React.createElement("option",({"key": ["timezone-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(t),"-promoted"].join(''), "value": t}),sablono.interpreter.interpret(oc.web.utils.user.readable_tz(t))),oc$web$components$user_profile_modal$iter__46736(cljs.core.rest(s__46737__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, ["US/Eastern","US/Central","US/Mountain","US/Pacific",guessed_timezone], null)));
})()),React.createElement("option",({"disabled": true, "value": ""}),"------------"),cljs.core.into_array.cljs$core$IFn$_invoke$arity$1((function (){var iter__4529__auto__ = (function oc$web$components$user_profile_modal$iter__46740(s__46741){
return (new cljs.core.LazySeq(null,(function (){
var s__46741__$1 = s__46741;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__46741__$1);
if(temp__5735__auto__){
var s__46741__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__46741__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__46741__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__46743 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__46742 = (0);
while(true){
if((i__46742 < size__4528__auto__)){
var t = cljs.core._nth(c__4527__auto__,i__46742);
cljs.core.chunk_append(b__46743,React.createElement("option",({"key": ["timezone-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(t)].join(''), "value": t}),sablono.interpreter.interpret(oc.web.utils.user.readable_tz(t))));

var G__46760 = (i__46742 + (1));
i__46742 = G__46760;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__46743),oc$web$components$user_profile_modal$iter__46740(cljs.core.chunk_rest(s__46741__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__46743),null);
}
} else {
var t = cljs.core.first(s__46741__$2);
return cljs.core.cons(React.createElement("option",({"key": ["timezone-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(t)].join(''), "value": t}),sablono.interpreter.interpret(oc.web.utils.user.readable_tz(t))),oc$web$components$user_profile_modal$iter__46740(cljs.core.rest(s__46741__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(timezones);
})())], 0)),cljs.core.into_array.cljs$core$IFn$_invoke$arity$1((function (){var iter__4529__auto__ = (function oc$web$components$user_profile_modal$iter__46744(s__46745){
return (new cljs.core.LazySeq(null,(function (){
var s__46745__$1 = s__46745;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__46745__$1);
if(temp__5735__auto__){
var s__46745__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__46745__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__46745__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__46747 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__46746 = (0);
while(true){
if((i__46746 < size__4528__auto__)){
var vec__46748 = cljs.core._nth(c__4527__auto__,i__46746);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__46748,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__46748,(1),null);
var field_name = ["profile-profiles-",cljs.core.name(k)].join('');
var tab_index = cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(links_tab_index,cljs.core.inc);
cljs.core.chunk_append(b__46747,React.createElement("div",({"key": field_name, "className": "profile-group"}),React.createElement("label",({"className": "field-label", "htmlFor": field_name}),sablono.interpreter.interpret(cuerdas.core.capital(cuerdas.core.camel(k)))),sablono.interpreter.create_element("input",({"tabIndex": tab_index, "placeholder": oc.web.components.user_profile_modal.placeholder(k), "value": cljs.core.get.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"profiles","profiles",507634713).cljs$core$IFn$_invoke$arity$1(current_user_data),k), "type": "text", "className": "field-value oc-input", "id": field_name, "maxLength": (128), "onChange": ((function (i__46746,field_name,tab_index,vec__46748,k,v,c__4527__auto__,size__4528__auto__,b__46747,s__46745__$2,temp__5735__auto__,edit_user_profile_avatar,is_jelly_head_avatar,user_profile_data,current_user_data,user_for_avatar,timezones,guessed_timezone,show_password_QMARK_,links_tab_index){
return (function (p1__46711_SHARP_){
return oc.web.components.user_profile_modal.change_BANG_(s,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"profiles","profiles",507634713),k], null),p1__46711_SHARP_.target.value);
});})(i__46746,field_name,tab_index,vec__46748,k,v,c__4527__auto__,size__4528__auto__,b__46747,s__46745__$2,temp__5735__auto__,edit_user_profile_avatar,is_jelly_head_avatar,user_profile_data,current_user_data,user_for_avatar,timezones,guessed_timezone,show_password_QMARK_,links_tab_index))
, "onFocus": ((function (i__46746,field_name,tab_index,vec__46748,k,v,c__4527__auto__,size__4528__auto__,b__46747,s__46745__$2,temp__5735__auto__,edit_user_profile_avatar,is_jelly_head_avatar,user_profile_data,current_user_data,user_for_avatar,timezones,guessed_timezone,show_password_QMARK_,links_tab_index){
return (function (p1__46710_SHARP_){
if(cljs.core.seq(v)){
return null;
} else {
return (p1__46710_SHARP_.target.value = oc.web.components.user_profile_modal.default_value(k));
}
});})(i__46746,field_name,tab_index,vec__46748,k,v,c__4527__auto__,size__4528__auto__,b__46747,s__46745__$2,temp__5735__auto__,edit_user_profile_avatar,is_jelly_head_avatar,user_profile_data,current_user_data,user_for_avatar,timezones,guessed_timezone,show_password_QMARK_,links_tab_index))
}))));

var G__46761 = (i__46746 + (1));
i__46746 = G__46761;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__46747),oc$web$components$user_profile_modal$iter__46744(cljs.core.chunk_rest(s__46745__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__46747),null);
}
} else {
var vec__46751 = cljs.core.first(s__46745__$2);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__46751,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__46751,(1),null);
var field_name = ["profile-profiles-",cljs.core.name(k)].join('');
var tab_index = cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(links_tab_index,cljs.core.inc);
return cljs.core.cons(React.createElement("div",({"key": field_name, "className": "profile-group"}),React.createElement("label",({"className": "field-label", "htmlFor": field_name}),sablono.interpreter.interpret(cuerdas.core.capital(cuerdas.core.camel(k)))),sablono.interpreter.create_element("input",({"tabIndex": tab_index, "placeholder": oc.web.components.user_profile_modal.placeholder(k), "value": cljs.core.get.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"profiles","profiles",507634713).cljs$core$IFn$_invoke$arity$1(current_user_data),k), "type": "text", "className": "field-value oc-input", "id": field_name, "maxLength": (128), "onChange": ((function (field_name,tab_index,vec__46751,k,v,s__46745__$2,temp__5735__auto__,edit_user_profile_avatar,is_jelly_head_avatar,user_profile_data,current_user_data,user_for_avatar,timezones,guessed_timezone,show_password_QMARK_,links_tab_index){
return (function (p1__46711_SHARP_){
return oc.web.components.user_profile_modal.change_BANG_(s,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"profiles","profiles",507634713),k], null),p1__46711_SHARP_.target.value);
});})(field_name,tab_index,vec__46751,k,v,s__46745__$2,temp__5735__auto__,edit_user_profile_avatar,is_jelly_head_avatar,user_profile_data,current_user_data,user_for_avatar,timezones,guessed_timezone,show_password_QMARK_,links_tab_index))
, "onFocus": ((function (field_name,tab_index,vec__46751,k,v,s__46745__$2,temp__5735__auto__,edit_user_profile_avatar,is_jelly_head_avatar,user_profile_data,current_user_data,user_for_avatar,timezones,guessed_timezone,show_password_QMARK_,links_tab_index){
return (function (p1__46710_SHARP_){
if(cljs.core.seq(v)){
return null;
} else {
return (p1__46710_SHARP_.target.value = oc.web.components.user_profile_modal.default_value(k));
}
});})(field_name,tab_index,vec__46751,k,v,s__46745__$2,temp__5735__auto__,edit_user_profile_avatar,is_jelly_head_avatar,user_profile_data,current_user_data,user_for_avatar,timezones,guessed_timezone,show_password_QMARK_,links_tab_index))
}))),oc$web$components$user_profile_modal$iter__46744(cljs.core.rest(s__46745__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(new cljs.core.Keyword(null,"profiles","profiles",507634713).cljs$core$IFn$_invoke$arity$1(current_user_data));
})()),sablono.interpreter.interpret(((show_password_QMARK_)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.fields-divider-line","div.fields-divider-line",1250882349)], null):null)),sablono.interpreter.interpret(((show_password_QMARK_)?new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"label.field-label","label.field-label",-1921557125),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"for","for",-1323786319),"profile-password"], null),"Currrent password",(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.user-profile-modal","current-password-error","oc.web.components.user-profile-modal/current-password-error",1682446622).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.error","span.error",-283487575),"Current password required"], null):null)], null):null)),sablono.interpreter.interpret(((show_password_QMARK_)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input.field-value.oc-input","input.field-value.oc-input",417079742),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"type","type",1174270348),"password",new cljs.core.Keyword(null,"id","id",-1388402092),"profile-password",new cljs.core.Keyword(null,"tab-index","tab-index",895755393),(((4) + cljs.core.count(new cljs.core.Keyword(null,"profiles","profiles",507634713).cljs$core$IFn$_invoke$arity$1(current_user_data))) + (1)),new cljs.core.Keyword(null,"placeholder","placeholder",-104873083),oc.web.components.user_profile_modal.placeholder(new cljs.core.Keyword(null,"password","password",417022471)),new cljs.core.Keyword(null,"on-change","on-change",-732046149),(function (p1__46712_SHARP_){
return oc.web.components.user_profile_modal.change_BANG_(s,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"current-password","current-password",-10574282)], null),p1__46712_SHARP_.target.value);
}),new cljs.core.Keyword(null,"value","value",305978217),new cljs.core.Keyword(null,"current-password","current-password",-10574282).cljs$core$IFn$_invoke$arity$1(current_user_data)], null)], null):null)),sablono.interpreter.interpret(((show_password_QMARK_)?new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"label.field-label","label.field-label",-1921557125),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"for","for",-1323786319),"profile-new-password"], null),"New password",(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.user-profile-modal","password-error","oc.web.components.user-profile-modal/password-error",-288924753).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.error","span.error",-283487575),"Minimum 8 characters"], null):null)], null):null)),sablono.interpreter.interpret(((show_password_QMARK_)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input.field-value.oc-input","input.field-value.oc-input",417079742),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"type","type",1174270348),"password",new cljs.core.Keyword(null,"id","id",-1388402092),"profile-new-password",new cljs.core.Keyword(null,"tab-index","tab-index",895755393),(((4) + cljs.core.count(new cljs.core.Keyword(null,"profiles","profiles",507634713).cljs$core$IFn$_invoke$arity$1(current_user_data))) + (1)),new cljs.core.Keyword(null,"placeholder","placeholder",-104873083),oc.web.components.user_profile_modal.placeholder(new cljs.core.Keyword(null,"new-password","new-password",-1530942754)),new cljs.core.Keyword(null,"on-change","on-change",-732046149),(function (p1__46713_SHARP_){
return oc.web.components.user_profile_modal.change_BANG_(s,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"password","password",417022471)], null),p1__46713_SHARP_.target.value);
}),new cljs.core.Keyword(null,"value","value",305978217),new cljs.core.Keyword(null,"password","password",417022471).cljs$core$IFn$_invoke$arity$1(current_user_data)], null)], null):null)))))));
}),new cljs.core.PersistentVector(null, 13, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"edit-user-profile","edit-user-profile",-479059118)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"edit-user-profile-avatar","edit-user-profile-avatar",303025729)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.user-profile-modal","loading","oc.web.components.user-profile-modal/loading",1607512970)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.user-profile-modal","show-success","oc.web.components.user-profile-modal/show-success",393393008)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.user-profile-modal","name-error","oc.web.components.user-profile-modal/name-error",408428786)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.user-profile-modal","email-error","oc.web.components.user-profile-modal/email-error",1213327404)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.user-profile-modal","password-error","oc.web.components.user-profile-modal/password-error",-288924753)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.user-profile-modal","current-password-error","oc.web.components.user-profile-modal/current-password-error",1682446622)),oc.web.mixins.ui.refresh_tooltips_mixin,oc.web.mixins.ui.autoresize_textarea(new cljs.core.Keyword(null,"blurb","blurb",-769928228)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
oc.web.actions.user.get_user(null);

return s;
}),new cljs.core.Keyword(null,"did-update","did-update",-2143702256),(function (s){
var user_data_46762 = new cljs.core.Keyword(null,"user-data","user-data",2143823568).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"edit-user-profile","edit-user-profile",-479059118))));
if(cljs.core.truth_((function (){var and__4115__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.user-profile-modal","loading","oc.web.components.user-profile-modal/loading",1607512970).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(new cljs.core.Keyword(null,"has-changes","has-changes",-631476764).cljs$core$IFn$_invoke$arity$1(s));
} else {
return and__4115__auto__;
}
})())){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.user-profile-modal","show-success","oc.web.components.user-profile-modal/show-success",393393008).cljs$core$IFn$_invoke$arity$1(s),true);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.user-profile-modal","loading","oc.web.components.user-profile-modal/loading",1607512970).cljs$core$IFn$_invoke$arity$1(s),false);

oc.web.lib.utils.after((2500),(function (){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.user-profile-modal","show-success","oc.web.components.user-profile-modal/show-success",393393008).cljs$core$IFn$_invoke$arity$1(s),false);
}));
} else {
}

return s;
})], null)], null),"user-profile-modal");

//# sourceMappingURL=oc.web.components.user_profile_modal.js.map
