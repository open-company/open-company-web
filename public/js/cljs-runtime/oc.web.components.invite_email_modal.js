goog.provide('oc.web.components.invite_email_modal');
oc.web.components.invite_email_modal.close_clicked = (function oc$web$components$invite_email_modal$close_clicked(s,dismiss_action){
var invite_users = cljs.core.filterv((function (p1__44722_SHARP_){
return cljs.core.not(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(p1__44722_SHARP_));
}),new cljs.core.Keyword(null,"invite-users","invite-users",107417337).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"invite-data","invite-data",-758838050)))));
var has_unsent_invites = (((cljs.core.count(invite_users) > (0)))?cljs.core.some((function (p1__44723_SHARP_){
return cljs.core.seq(new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(p1__44723_SHARP_));
}),invite_users):false);
if(cljs.core.truth_(has_unsent_invites)){
var alert_data = new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"icon","icon",1679606541),"/img/ML/trash.svg",new cljs.core.Keyword(null,"action","action",-811238024),"invite-unsaved-edits",new cljs.core.Keyword(null,"message","message",-406056002),"Leave without saving your changes?",new cljs.core.Keyword(null,"link-button-title","link-button-title",-812305976),"Stay",new cljs.core.Keyword(null,"link-button-cb","link-button-cb",559710301),(function (){
return oc.web.components.ui.alert_modal.hide_alert();
}),new cljs.core.Keyword(null,"solid-button-style","solid-button-style",-723792244),new cljs.core.Keyword(null,"red","red",-969428204),new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"Lose changes",new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),(function (){
oc.web.components.ui.alert_modal.hide_alert();

return (dismiss_action.cljs$core$IFn$_invoke$arity$0 ? dismiss_action.cljs$core$IFn$_invoke$arity$0() : dismiss_action.call(null));
})], null);
return oc.web.components.ui.alert_modal.show_alert(alert_data);
} else {
return (dismiss_action.cljs$core$IFn$_invoke$arity$0 ? dismiss_action.cljs$core$IFn$_invoke$arity$0() : dismiss_action.call(null));
}
});
/**
 * Select the whole content of the share link filed.
 */
oc.web.components.invite_email_modal.highlight_url = (function oc$web$components$invite_email_modal$highlight_url(s){
var temp__5735__auto__ = rum.core.ref_node(s,"invite-token-url-field");
if(cljs.core.truth_(temp__5735__auto__)){
var url_field = temp__5735__auto__;
return url_field.select();
} else {
return null;
}
});
oc.web.components.invite_email_modal.invite_email_modal = rum.core.build_defcs((function (s){
var org_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"org-data","org-data",96720321));
var invite_users_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"invite-data","invite-data",-758838050));
var team_data = new cljs.core.Keyword(null,"team-data","team-data",-732020079).cljs$core$IFn$_invoke$arity$1(invite_users_data);
var invite_users = new cljs.core.Keyword(null,"invite-users","invite-users",107417337).cljs$core$IFn$_invoke$arity$1(invite_users_data);
var cur_user_data = new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915).cljs$core$IFn$_invoke$arity$1(invite_users_data);
var is_admin_QMARK_ = oc.web.lib.jwt.is_admin_QMARK_(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(org_data));
return React.createElement("div",({"className": "invite-email-modal"}),React.createElement("button",({"onClick": (function (){
return oc.web.components.invite_email_modal.close_clicked(s,oc.web.actions.nav_sidebar.close_all_panels);
}), "className": "mlb-reset modal-close-bt"})),React.createElement("div",({"className": "invite-email-modal-inner"}),React.createElement("div",({"className": "invite-email-header"}),React.createElement("div",({"className": "invite-email-header-title"}),"Invite via email"),React.createElement("button",({"onClick": (function (_){
return oc.web.components.invite_email_modal.close_clicked(s,(function (){
return oc.web.actions.nav_sidebar.show_org_settings(null);
}));
}), "className": "mlb-reset cancel-bt"}),"Back")),(function (){var attrs44724 = (cljs.core.truth_(is_admin_QMARK_)?((cljs.core.seq(new cljs.core.Keyword(null,"invite-token","invite-token",610560078).cljs$core$IFn$_invoke$arity$1(team_data)))?new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.invite-token-container","div.invite-token-container",1360781731),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.invite-token-title","div.invite-token-title",-1698954354),"Share an invite link"], null),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.invite-token-description","div.invite-token-description",1568532753),"Anyone can use this link to join your Wut team as a ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"strong","strong",269529000),"contributor"], null),"."], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.invite-token-description","div.invite-token-description",1568532753),"Invite link"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.invite-token-field","div.invite-token-field",-2119761030),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input.invite-token-field-input","input.invite-token-field-input",610871604),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"value","value",305978217),new cljs.core.Keyword(null,"href","href",-793805698).cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(team_data),"invite-token"], 0))),new cljs.core.Keyword(null,"read-only","read-only",-191706886),true,new cljs.core.Keyword(null,"ref","ref",1289896967),"invite-token-url-field",new cljs.core.Keyword(null,"content-editable","content-editable",636764967),false,new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.components.invite_email_modal.highlight_url(s);
})], null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.invite-token-field-bt","button.mlb-reset.invite-token-field-bt",1613960800),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"ref","ref",1289896967),"invite-token-copy-btn",new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (e){
oc.web.lib.utils.event_stop(e);

var url_input = rum.core.ref_node(s,"invite-token-url-field");
oc.web.components.invite_email_modal.highlight_url(s);

var copied_QMARK_ = oc.web.lib.utils.copy_to_clipboard(url_input);
return oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"title","title",636505583),(cljs.core.truth_(copied_QMARK_)?"Invite URL copied to clipboard":"Error copying the URL"),new cljs.core.Keyword(null,"description","description",-1428560544),(cljs.core.truth_(copied_QMARK_)?null:"Please try copying the URL manually"),new cljs.core.Keyword(null,"primary-bt-title","primary-bt-title",653140150),"OK",new cljs.core.Keyword(null,"primary-bt-dismiss","primary-bt-dismiss",-820688058),true,new cljs.core.Keyword(null,"primary-bt-inline","primary-bt-inline",-796141614),copied_QMARK_,new cljs.core.Keyword(null,"expire","expire",-70657108),(3),new cljs.core.Keyword(null,"id","id",-1388402092),(cljs.core.truth_(copied_QMARK_)?new cljs.core.Keyword(null,"invite-token-url-copied","invite-token-url-copied",-1136752454):new cljs.core.Keyword(null,"invite-token-url-copy-error","invite-token-url-copy-error",-1317872102))], null));
})], null),"Copy"], null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.deactivate-link-bt","button.mlb-reset.deactivate-link-bt",-812994911),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (_){
var alert_data = cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"link-button-title","link-button-title",-812305976),new cljs.core.Keyword(null,"solid-button-style","solid-button-style",-723792244),new cljs.core.Keyword(null,"icon","icon",1679606541),new cljs.core.Keyword(null,"title","title",636505583),new cljs.core.Keyword(null,"action","action",-811238024),new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),new cljs.core.Keyword(null,"link-button-cb","link-button-cb",559710301),new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),new cljs.core.Keyword(null,"message","message",-406056002)],["No, keep it",new cljs.core.Keyword(null,"red","red",-969428204),"/img/ML/trash.svg","Are you sure?","deactivate-invite-email-link","OK, got it",(function (){
return oc.web.components.ui.alert_modal.hide_alert();
}),(function (){
oc.web.components.ui.alert_modal.hide_alert();

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.invite-email-modal","creating-invite-link","oc.web.components.invite-email-modal/creating-invite-link",-1203035254).cljs$core$IFn$_invoke$arity$1(s),true);

return oc.web.actions.team.delete_invite_token_link.cljs$core$IFn$_invoke$arity$variadic(oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(team_data),"delete-invite-link"], 0)),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(function (success_QMARK_){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.invite-email-modal","creating-invite-link","oc.web.components.invite-email-modal/creating-invite-link",-1203035254).cljs$core$IFn$_invoke$arity$1(s),false);
})], 0));
}),"Anyone that has this link already won't be able to use it to access your team."]);
return oc.web.components.ui.alert_modal.show_alert(alert_data);
}),new cljs.core.Keyword(null,"disabled","disabled",-1529784218),cljs.core.deref(new cljs.core.Keyword("oc.web.components.invite-email-modal","creating-invite-link","oc.web.components.invite-email-modal/creating-invite-link",-1203035254).cljs$core$IFn$_invoke$arity$1(s))], null),"Deactivate invite link"], null)], null):new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.invite-token-container","div.invite-token-container",1360781731),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.invite-token-title","div.invite-token-title",-1698954354),"Share an invite link"], null),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.invite-token-description","div.invite-token-description",1568532753),"Anyone can use this link to join your Wut team as a ",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"strong","strong",269529000),"contributor"], null),"."], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.generate-link-bt","button.mlb-reset.generate-link-bt",1444606004),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.invite-email-modal","creating-invite-link","oc.web.components.invite-email-modal/creating-invite-link",-1203035254).cljs$core$IFn$_invoke$arity$1(s),true);

return oc.web.actions.team.create_invite_token_link.cljs$core$IFn$_invoke$arity$variadic(oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(team_data),"create-invite-link"], 0)),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(function (success_QMARK_){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.invite-email-modal","creating-invite-link","oc.web.components.invite-email-modal/creating-invite-link",-1203035254).cljs$core$IFn$_invoke$arity$1(s),false);
})], 0));
}),new cljs.core.Keyword(null,"disabled","disabled",-1529784218),cljs.core.deref(new cljs.core.Keyword("oc.web.components.invite-email-modal","creating-invite-link","oc.web.components.invite-email-modal/creating-invite-link",-1203035254).cljs$core$IFn$_invoke$arity$1(s))], null),"Generate invite link"], null)], null)):null);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs44724))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["invite-email-body"], null)], null),attrs44724], 0))):({"className": "invite-email-body"})),((cljs.core.map_QMARK_(attrs44724))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["invites-list-container",(cljs.core.truth_(is_admin_QMARK_)?"top-border":null)], null))}),React.createElement("div",({"className": "invites-list-title"}),"Invite someone with a specific role"),React.createElement("div",({"className": "invites-list-description"}),"Admin, Contributor, or Viewer"),sablono.interpreter.interpret((oc.web.components.ui.invite_email.invite_email.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.invite_email.invite_email.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.invite_email.invite_email.call(null))))], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs44724),React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["invites-list-container",(cljs.core.truth_(is_admin_QMARK_)?"top-border":null)], null))}),React.createElement("div",({"className": "invites-list-title"}),"Invite someone with a specific role"),React.createElement("div",({"className": "invites-list-description"}),"Admin, Contributor, or Viewer"),sablono.interpreter.interpret((oc.web.components.ui.invite_email.invite_email.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.invite_email.invite_email.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.invite_email.invite_email.call(null))))], null)));
})()));
}),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"org-data","org-data",96720321)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"invite-data","invite-data",-758838050)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.invite-email-modal","creating-invite-link","oc.web.components.invite-email-modal/creating-invite-link",-1203035254)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
var org_data_44743 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"org-data","org-data",96720321)));
oc.web.actions.org.get_org.cljs$core$IFn$_invoke$arity$2(org_data_44743,true);

oc.web.actions.team.teams_get();

return s;
})], null)], null),"invite-email-modal");

//# sourceMappingURL=oc.web.components.invite_email_modal.js.map
