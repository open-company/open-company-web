goog.provide('oc.web.components.invite_picker_modal');
oc.web.components.invite_picker_modal.invite_picker_modal = rum.core.build_defcs((function (s){
var org_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"org-data","org-data",96720321));
var team_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"team-data","team-data",-732020079));
var current_user_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915));
return React.createElement("div",({"className": "invite-picker-modal"}),React.createElement("button",({"onClick": oc.web.actions.nav_sidebar.close_all_panels, "className": "mlb-reset modal-close-bt"})),React.createElement("div",({"className": "invite-picker"}),React.createElement("div",({"className": "invite-picker-header"}),React.createElement("div",({"className": "invite-picker-header-title"}),"Invite people"),React.createElement("button",({"onClick": (function (){
return oc.web.actions.nav_sidebar.show_org_settings(null);
}), "className": "mlb-reset cancel-bt"}),"Back")),React.createElement("div",({"className": "invite-picker-body"}),React.createElement("div",({"className": "invite-picker-body-description"}),"Ready to share your updates? Invite your team!"),React.createElement("button",({"onClick": (function (){
return oc.web.actions.nav_sidebar.show_org_settings(new cljs.core.Keyword(null,"invite-email","invite-email",1375794598));
}), "className": "mlb-reset invite-email-bt"}),"Invite via email"),(cljs.core.truth_((function (){var or__4126__auto__ = (cljs.core.count(new cljs.core.Keyword(null,"slack-orgs","slack-orgs",-1806634042).cljs$core$IFn$_invoke$arity$1(team_data)) > (0));
if(or__4126__auto__){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = new cljs.core.Keyword(null,"can-slack-invite","can-slack-invite",1450736688).cljs$core$IFn$_invoke$arity$1(team_data);
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"auth-source","auth-source",1912135250)),"slack");
}
}
})())?React.createElement("button",({"onClick": (function (){
return oc.web.actions.nav_sidebar.show_org_settings(new cljs.core.Keyword(null,"invite-slack","invite-slack",-1290785659));
}), "className": "mlb-reset invite-slack-bt"}),"Invite via Slack"):sablono.interpreter.interpret((cljs.core.truth_(oc.web.lib.jwt.is_admin_QMARK_(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(team_data)))?new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.invite-slack-bt","button.mlb-reset.invite-slack-bt",-58779204),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.actions.org.bot_auth.cljs$core$IFn$_invoke$arity$variadic(team_data,current_user_data,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([[oc.web.router.get_token(),"?org-settings=invite-picker"].join('')], 0));
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.disabled","span.disabled",-36063841),"Invite via Slack"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.enabled","span.enabled",-798779468),"(add Slack)"], null)], null):null))),sablono.interpreter.interpret(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"role","role",-736691072).cljs$core$IFn$_invoke$arity$1(current_user_data),new cljs.core.Keyword(null,"admin","admin",-1239101627)))?(oc.web.components.ui.email_domains.email_domains.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.email_domains.email_domains.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.email_domains.email_domains.call(null)):null)))));
}),new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"query-params","query-params",900640534)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"org-data","org-data",96720321)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"team-data","team-data",-732020079)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"invite-add-slack-checked","invite-add-slack-checked",285177890)], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
var query_params_44796 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"query-params","query-params",900640534)));
var invite_add_slack_checked_44797 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"invite-add-slack-checked","invite-add-slack-checked",285177890)));
if(cljs.core.truth_(invite_add_slack_checked_44797)){
} else {
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"invite-add-slack-checked","invite-add-slack-checked",285177890)], null),true], null));

if(cljs.core.truth_(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"org-settings","org-settings",-785943661).cljs$core$IFn$_invoke$arity$1(query_params_44796),"invite-picker"))?(function (){var G__44781 = new cljs.core.Keyword(null,"access","access",2027349272).cljs$core$IFn$_invoke$arity$1(query_params_44796);
var fexpr__44780 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, ["bot",null,"team",null], null), null);
return (fexpr__44780.cljs$core$IFn$_invoke$arity$1 ? fexpr__44780.cljs$core$IFn$_invoke$arity$1(G__44781) : fexpr__44780.call(null,G__44781));
})():false))){
oc.web.actions.nav_sidebar.show_org_settings(new cljs.core.Keyword(null,"invite-slack","invite-slack",-1290785659));
} else {
}
}

var org_data_44798 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"org-data","org-data",96720321)));
oc.web.actions.org.get_org.cljs$core$IFn$_invoke$arity$2(org_data_44798,true);

oc.web.actions.team.teams_get();

return s;
})], null)], null),"invite-picker-modal");

//# sourceMappingURL=oc.web.components.invite_picker_modal.js.map
