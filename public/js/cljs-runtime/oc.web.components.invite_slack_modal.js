goog.provide('oc.web.components.invite_slack_modal');
oc.web.components.invite_slack_modal.close_clicked = (function oc$web$components$invite_slack_modal$close_clicked(s,dismiss_action){
var invite_users = cljs.core.filterv((function (p1__44727_SHARP_){
return cljs.core.not(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(p1__44727_SHARP_));
}),new cljs.core.Keyword(null,"invite-users","invite-users",107417337).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"invite-data","invite-data",-758838050)))));
var has_unsent_invites = (((cljs.core.count(invite_users) > (0)))?cljs.core.some((function (p1__44728_SHARP_){
return cljs.core.seq(new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(p1__44728_SHARP_));
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
oc.web.components.invite_slack_modal.default_row_num = (1);
oc.web.components.invite_slack_modal.default_slack_user = cljs.core.PersistentArrayMap.EMPTY;
oc.web.components.invite_slack_modal.default_user_role = new cljs.core.Keyword(null,"author","author",2111686192);
oc.web.components.invite_slack_modal.default_user_row = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"temp-user","temp-user",1357534508),"",new cljs.core.Keyword(null,"user","user",1532431356),oc.web.components.invite_slack_modal.default_slack_user,new cljs.core.Keyword(null,"role","role",-736691072),oc.web.components.invite_slack_modal.default_user_role], null);
oc.web.components.invite_slack_modal.new_user_row = (function oc$web$components$invite_slack_modal$new_user_row(s){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(oc.web.components.invite_slack_modal.default_user_row,new cljs.core.Keyword(null,"type","type",1174270348),"slack");
});
oc.web.components.invite_slack_modal.valid_user_QMARK_ = (function oc$web$components$invite_slack_modal$valid_user_QMARK_(user_map){
return ((cljs.core.contains_QMARK_(new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(user_map),new cljs.core.Keyword(null,"slack-id","slack-id",862141985))) && (cljs.core.contains_QMARK_(new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(user_map),new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561))));
});
oc.web.components.invite_slack_modal.has_valid_user_QMARK_ = (function oc$web$components$invite_slack_modal$has_valid_user_QMARK_(users_list){
return cljs.core.some(oc.web.components.invite_slack_modal.valid_user_QMARK_,users_list);
});
oc.web.components.invite_slack_modal.user_type_did_change = (function oc$web$components$invite_slack_modal$user_type_did_change(s,invite_users,value){
var seq__44737 = cljs.core.seq(cljs.core.range.cljs$core$IFn$_invoke$arity$1(cljs.core.count(invite_users)));
var chunk__44739 = null;
var count__44740 = (0);
var i__44741 = (0);
while(true){
if((i__44741 < count__44740)){
var i = chunk__44739.cljs$core$IIndexed$_nth$arity$2(null,i__44741);
var user_44782 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(invite_users,i);
if(((cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(user_44782))) && (cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"temp-user","temp-user",1357534508).cljs$core$IFn$_invoke$arity$1(user_44782))))){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"invite-users","invite-users",107417337),i,new cljs.core.Keyword(null,"type","type",1174270348)], null),value], null));
} else {
}


var G__44783 = seq__44737;
var G__44784 = chunk__44739;
var G__44785 = count__44740;
var G__44786 = (i__44741 + (1));
seq__44737 = G__44783;
chunk__44739 = G__44784;
count__44740 = G__44785;
i__44741 = G__44786;
continue;
} else {
var temp__5735__auto__ = cljs.core.seq(seq__44737);
if(temp__5735__auto__){
var seq__44737__$1 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__44737__$1)){
var c__4556__auto__ = cljs.core.chunk_first(seq__44737__$1);
var G__44787 = cljs.core.chunk_rest(seq__44737__$1);
var G__44788 = c__4556__auto__;
var G__44789 = cljs.core.count(c__4556__auto__);
var G__44790 = (0);
seq__44737 = G__44787;
chunk__44739 = G__44788;
count__44740 = G__44789;
i__44741 = G__44790;
continue;
} else {
var i = cljs.core.first(seq__44737__$1);
var user_44791 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(invite_users,i);
if(((cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(user_44791))) && (cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"temp-user","temp-user",1357534508).cljs$core$IFn$_invoke$arity$1(user_44791))))){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"invite-users","invite-users",107417337),i,new cljs.core.Keyword(null,"type","type",1174270348)], null),value], null));
} else {
}


var G__44792 = cljs.core.next(seq__44737__$1);
var G__44793 = null;
var G__44794 = (0);
var G__44795 = (0);
seq__44737 = G__44792;
chunk__44739 = G__44793;
count__44740 = G__44794;
i__44741 = G__44795;
continue;
}
} else {
return null;
}
}
break;
}
});
oc.web.components.invite_slack_modal.setup_initial_rows = (function oc$web$components$invite_slack_modal$setup_initial_rows(s){
var inviting_users_data = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"invite-data","invite-data",-758838050)));
var invite_users = new cljs.core.Keyword(null,"invite-users","invite-users",107417337).cljs$core$IFn$_invoke$arity$1(inviting_users_data);
var cur_user_data = new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"invite-data","invite-data",-758838050))));
var team_data = new cljs.core.Keyword(null,"team-data","team-data",-732020079).cljs$core$IFn$_invoke$arity$1(inviting_users_data);
if((cljs.core.count(invite_users) === (0))){
var new_row = oc.web.components.invite_slack_modal.new_user_row(s);
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"invite-users","invite-users",107417337)], null),cljs.core.vec(cljs.core.repeat.cljs$core$IFn$_invoke$arity$2(oc.web.components.invite_slack_modal.default_row_num,new_row))], null));
} else {
return null;
}
});
/**
 * Select the whole content of the share link filed.
 */
oc.web.components.invite_slack_modal.highlight_url = (function oc$web$components$invite_slack_modal$highlight_url(s){
var temp__5735__auto__ = rum.core.ref_node(s,"invite-token-url-field");
if(cljs.core.truth_(temp__5735__auto__)){
var url_field = temp__5735__auto__;
return url_field.select();
} else {
return null;
}
});
oc.web.components.invite_slack_modal.invite_slack_modal = rum.core.build_defcs((function (s){
var org_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"org-data","org-data",96720321));
var invite_users_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"invite-data","invite-data",-758838050));
var team_data = new cljs.core.Keyword(null,"team-data","team-data",-732020079).cljs$core$IFn$_invoke$arity$1(invite_users_data);
var invite_users = new cljs.core.Keyword(null,"invite-users","invite-users",107417337).cljs$core$IFn$_invoke$arity$1(invite_users_data);
var cur_user_data = new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915).cljs$core$IFn$_invoke$arity$1(invite_users_data);
var team_roster = new cljs.core.Keyword(null,"team-roster","team-roster",-1945092859).cljs$core$IFn$_invoke$arity$1(invite_users_data);
var uninvited_users = cljs.core.filterv((function (p1__44745_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(p1__44745_SHARP_),"uninvited");
}),new cljs.core.Keyword(null,"users","users",-713552705).cljs$core$IFn$_invoke$arity$1(team_roster));
var is_admin_QMARK_ = oc.web.lib.jwt.is_admin_QMARK_(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(org_data));
return React.createElement("div",({"className": "invite-slack-modal"}),React.createElement("button",({"onClick": (function (){
return oc.web.components.invite_slack_modal.close_clicked(s,oc.web.actions.nav_sidebar.close_all_panels);
}), "className": "mlb-reset modal-close-bt"})),React.createElement("div",({"className": "invite-slack"}),React.createElement("div",({"className": "invite-slack-header"}),React.createElement("div",({"className": "invite-slack-header-title"}),"Invite via Slack"),React.createElement("button",({"onClick": (function (_){
return oc.web.components.invite_slack_modal.close_clicked(s,(function (){
return oc.web.actions.nav_sidebar.show_org_settings(null);
}));
}), "className": "mlb-reset cancel-bt"}),"Back")),React.createElement("div",({"className": "invite-slack-body"}),React.createElement("div",({"className": "invite-token-container"}),React.createElement("div",({"className": "invite-token-title"}),"Share this link in Slack ",React.createElement("i",({"className": "mdi mdi-slack"}))),React.createElement("div",({"className": "invite-token-description"}),"Anyone on your Slack team can use this link to join Wut as a ",React.createElement("strong",null,"contributor"),"."),React.createElement("div",({"className": "invite-token-description"}),"Invite link"),React.createElement("div",({"className": "invite-token-field"}),sablono.interpreter.create_element("input",({"value": [oc.web.local_settings.web_server_domain,oc.web.urls.sign_up_slack].join(''), "readOnly": true, "ref": "invite-token-url-field", "contentEditable": false, "onClick": (function (){
return oc.web.components.invite_slack_modal.highlight_url(s);
}), "className": "invite-token-field-input"})),React.createElement("button",({"ref": "invite-token-copy-btn", "onClick": (function (e){
oc.web.lib.utils.event_stop(e);

var url_input = rum.core.ref_node(s,"invite-token-url-field");
oc.web.components.invite_slack_modal.highlight_url(s);

var copied_QMARK_ = oc.web.lib.utils.copy_to_clipboard(url_input);
return oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"title","title",636505583),(cljs.core.truth_(copied_QMARK_)?"Invite URL copied to clipboard":"Error copying the URL"),new cljs.core.Keyword(null,"description","description",-1428560544),(cljs.core.truth_(copied_QMARK_)?null:"Please try copying the URL manually"),new cljs.core.Keyword(null,"primary-bt-title","primary-bt-title",653140150),"OK",new cljs.core.Keyword(null,"primary-bt-dismiss","primary-bt-dismiss",-820688058),true,new cljs.core.Keyword(null,"primary-bt-inline","primary-bt-inline",-796141614),copied_QMARK_,new cljs.core.Keyword(null,"expire","expire",-70657108),(3),new cljs.core.Keyword(null,"id","id",-1388402092),(cljs.core.truth_(copied_QMARK_)?new cljs.core.Keyword(null,"invite-token-url-copied","invite-token-url-copied",-1136752454):new cljs.core.Keyword(null,"invite-token-url-copy-error","invite-token-url-copy-error",-1317872102))], null));
}), "className": "mlb-reset invite-token-field-bt"}),"Copy"))),(cljs.core.truth_(new cljs.core.Keyword(null,"can-slack-invite","can-slack-invite",1450736688).cljs$core$IFn$_invoke$arity$1(team_data))?React.createElement("div",({"className": "invites-list top-border"}),React.createElement("div",({"className": "invites-list-title"}),"Invite someone with a specific role"),React.createElement("div",({"className": "invites-list-description"}),"Admin, Contributor, or Viewer"),cljs.core.into_array.cljs$core$IFn$_invoke$arity$1((function (){var iter__4529__auto__ = (function oc$web$components$invite_slack_modal$iter__44762(s__44763){
return (new cljs.core.LazySeq(null,(function (){
var s__44763__$1 = s__44763;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__44763__$1);
if(temp__5735__auto__){
var s__44763__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__44763__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__44763__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__44765 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__44764 = (0);
while(true){
if((i__44764 < size__4528__auto__)){
var i = cljs.core._nth(c__4527__auto__,i__44764);
var user_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(invite_users,i);
var key_string = ["invite-users-tabe-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(i)].join('');
cljs.core.chunk_append(b__44765,React.createElement("div",({"key": key_string, "className": "invites-list-item"}),React.createElement("div",({"className": "invites-list-item-inner group"}),React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["user-name-dropdown",(cljs.core.truth_(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(user_data))?"error":null)], null))}),sablono.interpreter.interpret(rum.core.with_key((function (){var G__44767 = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"on-change","on-change",-732046149),((function (i__44764,user_data,key_string,i,c__4527__auto__,size__4528__auto__,b__44765,s__44763__$2,temp__5735__auto__,org_data,invite_users_data,team_data,invite_users,cur_user_data,team_roster,uninvited_users,is_admin_QMARK_){
return (function (p1__44746_SHARP_){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"invite-users","invite-users",107417337),i], null),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user_data,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"user","user",1532431356),p1__44746_SHARP_,new cljs.core.Keyword(null,"error","error",-978969032),null,new cljs.core.Keyword(null,"temp-user","temp-user",1357534508),null], null)], 0))], null));
});})(i__44764,user_data,key_string,i,c__4527__auto__,size__4528__auto__,b__44765,s__44763__$2,temp__5735__auto__,org_data,invite_users_data,team_data,invite_users,cur_user_data,team_roster,uninvited_users,is_admin_QMARK_))
,new cljs.core.Keyword(null,"filter-fn","filter-fn",1689475675),((function (i__44764,user_data,key_string,i,c__4527__auto__,size__4528__auto__,b__44765,s__44763__$2,temp__5735__auto__,org_data,invite_users_data,team_data,invite_users,cur_user_data,team_roster,uninvited_users,is_admin_QMARK_){
return (function (user){
var check_fn = ((function (i__44764,user_data,key_string,i,c__4527__auto__,size__4528__auto__,b__44765,s__44763__$2,temp__5735__auto__,org_data,invite_users_data,team_data,invite_users,cur_user_data,team_roster,uninvited_users,is_admin_QMARK_){
return (function (p1__44747_SHARP_){
return ((cljs.core.not(new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(p1__44747_SHARP_))) || (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(p1__44747_SHARP_)),new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(user))) || (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slack-id","slack-id",862141985).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(p1__44747_SHARP_)),new cljs.core.Keyword(null,"slack-id","slack-id",862141985).cljs$core$IFn$_invoke$arity$1(user))));
});})(i__44764,user_data,key_string,i,c__4527__auto__,size__4528__auto__,b__44765,s__44763__$2,temp__5735__auto__,org_data,invite_users_data,team_data,invite_users,cur_user_data,team_roster,uninvited_users,is_admin_QMARK_))
;
return cljs.core.every_QMARK_(check_fn,invite_users);
});})(i__44764,user_data,key_string,i,c__4527__auto__,size__4528__auto__,b__44765,s__44763__$2,temp__5735__auto__,org_data,invite_users_data,team_data,invite_users,cur_user_data,team_roster,uninvited_users,is_admin_QMARK_))
,new cljs.core.Keyword(null,"on-intermediate-change","on-intermediate-change",-1144231725),((function (i__44764,user_data,key_string,i,c__4527__auto__,size__4528__auto__,b__44765,s__44763__$2,temp__5735__auto__,org_data,invite_users_data,team_data,invite_users,cur_user_data,team_roster,uninvited_users,is_admin_QMARK_){
return (function (p1__44748_SHARP_){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"invite-users","invite-users",107417337)], null),cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(invite_users,i,cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user_data,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"user","user",1532431356),null,new cljs.core.Keyword(null,"error","error",-978969032),null,new cljs.core.Keyword(null,"temp-user","temp-user",1357534508),p1__44748_SHARP_], null)], 0)))], null));
});})(i__44764,user_data,key_string,i,c__4527__auto__,size__4528__auto__,b__44765,s__44763__$2,temp__5735__auto__,org_data,invite_users_data,team_data,invite_users,cur_user_data,team_roster,uninvited_users,is_admin_QMARK_))
,new cljs.core.Keyword(null,"initial-value","initial-value",470619381),oc.lib.user.name_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(user_data)], 0))], null);
return (oc.web.components.ui.slack_users_dropdown.slack_users_dropdown.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.slack_users_dropdown.slack_users_dropdown.cljs$core$IFn$_invoke$arity$1(G__44767) : oc.web.components.ui.slack_users_dropdown.slack_users_dropdown.call(null,G__44767));
})(),["slack-users-dropdown-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.count(uninvited_users)),"-row-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(i)].join(''))))),(function (){var attrs44766 = (function (){var G__44768 = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"user-id","user-id",-206822291),oc.web.lib.utils.guid(),new cljs.core.Keyword(null,"user-type","user-type",738868936),new cljs.core.Keyword(null,"role","role",-736691072).cljs$core$IFn$_invoke$arity$1(user_data),new cljs.core.Keyword(null,"hide-admin","hide-admin",-823852536),cljs.core.not(oc.web.lib.jwt.is_admin_QMARK_(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(org_data))),new cljs.core.Keyword(null,"on-change","on-change",-732046149),((function (i__44764,user_data,key_string,i,c__4527__auto__,size__4528__auto__,b__44765,s__44763__$2,temp__5735__auto__,org_data,invite_users_data,team_data,invite_users,cur_user_data,team_roster,uninvited_users,is_admin_QMARK_){
return (function (p1__44749_SHARP_){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"invite-users","invite-users",107417337)], null),cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(invite_users,i,cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user_data,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"role","role",-736691072),p1__44749_SHARP_,new cljs.core.Keyword(null,"error","error",-978969032),null], null)], 0)))], null));
});})(i__44764,user_data,key_string,i,c__4527__auto__,size__4528__auto__,b__44765,s__44763__$2,temp__5735__auto__,org_data,invite_users_data,team_data,invite_users,cur_user_data,team_roster,uninvited_users,is_admin_QMARK_))
], null);
return (oc.web.components.ui.user_type_dropdown.user_type_dropdown.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_type_dropdown.user_type_dropdown.cljs$core$IFn$_invoke$arity$1(G__44768) : oc.web.components.ui.user_type_dropdown.user_type_dropdown.call(null,G__44768));
})();
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs44766))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["user-type-dropdown"], null)], null),attrs44766], 0))):({"className": "user-type-dropdown"})),((cljs.core.map_QMARK_(attrs44766))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs44766)], null)));
})(),React.createElement("button",({"onClick": ((function (i__44764,user_data,key_string,i,c__4527__auto__,size__4528__auto__,b__44765,s__44763__$2,temp__5735__auto__,org_data,invite_users_data,team_data,invite_users,cur_user_data,team_roster,uninvited_users,is_admin_QMARK_){
return (function (){
var before = cljs.core.subvec.cljs$core$IFn$_invoke$arity$3(invite_users,(0),i);
var after = cljs.core.subvec.cljs$core$IFn$_invoke$arity$3(invite_users,(i + (1)),cljs.core.count(invite_users));
var next_invite_users = cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(before,after));
var fixed_next_invite_users = (((cljs.core.count(next_invite_users) === (0)))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(oc.web.components.invite_slack_modal.default_user_row,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(user_data))], null):next_invite_users);
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"invite-users","invite-users",107417337)], null),fixed_next_invite_users], null));
});})(i__44764,user_data,key_string,i,c__4527__auto__,size__4528__auto__,b__44765,s__44763__$2,temp__5735__auto__,org_data,invite_users_data,team_data,invite_users,cur_user_data,team_roster,uninvited_users,is_admin_QMARK_))
, "className": "mlb-reset remove-user"}))));

var G__44799 = (i__44764 + (1));
i__44764 = G__44799;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__44765),oc$web$components$invite_slack_modal$iter__44762(cljs.core.chunk_rest(s__44763__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__44765),null);
}
} else {
var i = cljs.core.first(s__44763__$2);
var user_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(invite_users,i);
var key_string = ["invite-users-tabe-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(i)].join('');
return cljs.core.cons(React.createElement("div",({"key": key_string, "className": "invites-list-item"}),React.createElement("div",({"className": "invites-list-item-inner group"}),React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["user-name-dropdown",(cljs.core.truth_(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(user_data))?"error":null)], null))}),sablono.interpreter.interpret(rum.core.with_key((function (){var G__44769 = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"on-change","on-change",-732046149),((function (user_data,key_string,i,s__44763__$2,temp__5735__auto__,org_data,invite_users_data,team_data,invite_users,cur_user_data,team_roster,uninvited_users,is_admin_QMARK_){
return (function (p1__44746_SHARP_){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"invite-users","invite-users",107417337),i], null),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user_data,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"user","user",1532431356),p1__44746_SHARP_,new cljs.core.Keyword(null,"error","error",-978969032),null,new cljs.core.Keyword(null,"temp-user","temp-user",1357534508),null], null)], 0))], null));
});})(user_data,key_string,i,s__44763__$2,temp__5735__auto__,org_data,invite_users_data,team_data,invite_users,cur_user_data,team_roster,uninvited_users,is_admin_QMARK_))
,new cljs.core.Keyword(null,"filter-fn","filter-fn",1689475675),((function (user_data,key_string,i,s__44763__$2,temp__5735__auto__,org_data,invite_users_data,team_data,invite_users,cur_user_data,team_roster,uninvited_users,is_admin_QMARK_){
return (function (user){
var check_fn = (function (p1__44747_SHARP_){
return ((cljs.core.not(new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(p1__44747_SHARP_))) || (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(p1__44747_SHARP_)),new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(user))) || (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slack-id","slack-id",862141985).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(p1__44747_SHARP_)),new cljs.core.Keyword(null,"slack-id","slack-id",862141985).cljs$core$IFn$_invoke$arity$1(user))));
});
return cljs.core.every_QMARK_(check_fn,invite_users);
});})(user_data,key_string,i,s__44763__$2,temp__5735__auto__,org_data,invite_users_data,team_data,invite_users,cur_user_data,team_roster,uninvited_users,is_admin_QMARK_))
,new cljs.core.Keyword(null,"on-intermediate-change","on-intermediate-change",-1144231725),((function (user_data,key_string,i,s__44763__$2,temp__5735__auto__,org_data,invite_users_data,team_data,invite_users,cur_user_data,team_roster,uninvited_users,is_admin_QMARK_){
return (function (p1__44748_SHARP_){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"invite-users","invite-users",107417337)], null),cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(invite_users,i,cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user_data,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"user","user",1532431356),null,new cljs.core.Keyword(null,"error","error",-978969032),null,new cljs.core.Keyword(null,"temp-user","temp-user",1357534508),p1__44748_SHARP_], null)], 0)))], null));
});})(user_data,key_string,i,s__44763__$2,temp__5735__auto__,org_data,invite_users_data,team_data,invite_users,cur_user_data,team_roster,uninvited_users,is_admin_QMARK_))
,new cljs.core.Keyword(null,"initial-value","initial-value",470619381),oc.lib.user.name_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(user_data)], 0))], null);
return (oc.web.components.ui.slack_users_dropdown.slack_users_dropdown.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.slack_users_dropdown.slack_users_dropdown.cljs$core$IFn$_invoke$arity$1(G__44769) : oc.web.components.ui.slack_users_dropdown.slack_users_dropdown.call(null,G__44769));
})(),["slack-users-dropdown-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.count(uninvited_users)),"-row-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(i)].join(''))))),(function (){var attrs44766 = (function (){var G__44770 = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"user-id","user-id",-206822291),oc.web.lib.utils.guid(),new cljs.core.Keyword(null,"user-type","user-type",738868936),new cljs.core.Keyword(null,"role","role",-736691072).cljs$core$IFn$_invoke$arity$1(user_data),new cljs.core.Keyword(null,"hide-admin","hide-admin",-823852536),cljs.core.not(oc.web.lib.jwt.is_admin_QMARK_(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(org_data))),new cljs.core.Keyword(null,"on-change","on-change",-732046149),((function (user_data,key_string,i,s__44763__$2,temp__5735__auto__,org_data,invite_users_data,team_data,invite_users,cur_user_data,team_roster,uninvited_users,is_admin_QMARK_){
return (function (p1__44749_SHARP_){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"invite-users","invite-users",107417337)], null),cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(invite_users,i,cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user_data,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"role","role",-736691072),p1__44749_SHARP_,new cljs.core.Keyword(null,"error","error",-978969032),null], null)], 0)))], null));
});})(user_data,key_string,i,s__44763__$2,temp__5735__auto__,org_data,invite_users_data,team_data,invite_users,cur_user_data,team_roster,uninvited_users,is_admin_QMARK_))
], null);
return (oc.web.components.ui.user_type_dropdown.user_type_dropdown.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_type_dropdown.user_type_dropdown.cljs$core$IFn$_invoke$arity$1(G__44770) : oc.web.components.ui.user_type_dropdown.user_type_dropdown.call(null,G__44770));
})();
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs44766))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["user-type-dropdown"], null)], null),attrs44766], 0))):({"className": "user-type-dropdown"})),((cljs.core.map_QMARK_(attrs44766))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs44766)], null)));
})(),React.createElement("button",({"onClick": ((function (user_data,key_string,i,s__44763__$2,temp__5735__auto__,org_data,invite_users_data,team_data,invite_users,cur_user_data,team_roster,uninvited_users,is_admin_QMARK_){
return (function (){
var before = cljs.core.subvec.cljs$core$IFn$_invoke$arity$3(invite_users,(0),i);
var after = cljs.core.subvec.cljs$core$IFn$_invoke$arity$3(invite_users,(i + (1)),cljs.core.count(invite_users));
var next_invite_users = cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(before,after));
var fixed_next_invite_users = (((cljs.core.count(next_invite_users) === (0)))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(oc.web.components.invite_slack_modal.default_user_row,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(user_data))], null):next_invite_users);
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"invite-users","invite-users",107417337)], null),fixed_next_invite_users], null));
});})(user_data,key_string,i,s__44763__$2,temp__5735__auto__,org_data,invite_users_data,team_data,invite_users,cur_user_data,team_roster,uninvited_users,is_admin_QMARK_))
, "className": "mlb-reset remove-user"}))),oc$web$components$invite_slack_modal$iter__44762(cljs.core.rest(s__44763__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(cljs.core.range.cljs$core$IFn$_invoke$arity$1(cljs.core.count(invite_users)));
})()),React.createElement("button",({"onClick": (function (){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"invite-users","invite-users",107417337)], null),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(invite_users,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(oc.web.components.invite_slack_modal.default_user_row,new cljs.core.Keyword(null,"type","type",1174270348),"slack"))], null));
}), "className": "mlb-reset add-button"}),React.createElement("div",({"className": "add-button-plus"})),"Add another"),React.createElement("button",({"onClick": (function (){
var valid_count = cljs.core.count(cljs.core.filterv(oc.web.components.invite_slack_modal.valid_user_QMARK_,invite_users));
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.invite-slack-modal","sending","oc.web.components.invite-slack-modal/sending",2114495154).cljs$core$IFn$_invoke$arity$1(s),valid_count);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.invite-slack-modal","initial-sending","oc.web.components.invite-slack-modal/initial-sending",840025360).cljs$core$IFn$_invoke$arity$1(s),valid_count);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.invite-slack-modal","send-bt-cta","oc.web.components.invite-slack-modal/send-bt-cta",1663457241).cljs$core$IFn$_invoke$arity$1(s),"Sending Slack invitations");

return oc.web.actions.team.invite_users(new cljs.core.Keyword(null,"invite-users","invite-users",107417337).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"invite-data","invite-data",-758838050)))));
}), "disabled": ((cljs.core.not(oc.web.components.invite_slack_modal.has_valid_user_QMARK_(invite_users))) || ((cljs.core.deref(new cljs.core.Keyword("oc.web.components.invite-slack-modal","sending","oc.web.components.invite-slack-modal/sending",2114495154).cljs$core$IFn$_invoke$arity$1(s)) > (0)))), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["mlb-reset","save-bt",((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2("Slack invitations sent!",cljs.core.deref(new cljs.core.Keyword("oc.web.components.invite-slack-modal","send-bt-cta","oc.web.components.invite-slack-modal/send-bt-cta",1663457241).cljs$core$IFn$_invoke$arity$1(s))))?"no-disable":null)], null))}),sablono.interpreter.interpret(cljs.core.deref(new cljs.core.Keyword("oc.web.components.invite-slack-modal","send-bt-cta","oc.web.components.invite-slack-modal/send-bt-cta",1663457241).cljs$core$IFn$_invoke$arity$1(s))))):sablono.interpreter.interpret((cljs.core.truth_(is_admin_QMARK_)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.invites-list.top-border","div.invites-list.top-border",-1531845813),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.invites-list-title","div.invites-list-title",121354849),"Invite someone with a specific permission level"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.enable-carrot-bot-bt","button.mlb-reset.enable-carrot-bot-bt",664055695),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.actions.org.bot_auth.cljs$core$IFn$_invoke$arity$variadic(team_data,cur_user_data,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([[oc.web.router.get_token(),"?org-settings=invite-slack"].join('')], 0));
})], null),"Enable the Wut bot for Slack"], null)], null):null))))));
}),new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"org-data","org-data",96720321)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"invite-data","invite-data",-758838050)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2((cljs.core.rand.cljs$core$IFn$_invoke$arity$1((10000)) | (0)),new cljs.core.Keyword("oc.web.components.invite-slack-modal","rand","oc.web.components.invite-slack-modal/rand",-311692682)),rum.core.local.cljs$core$IFn$_invoke$arity$2("Send",new cljs.core.Keyword("oc.web.components.invite-slack-modal","send-bt-cta","oc.web.components.invite-slack-modal/send-bt-cta",1663457241)),rum.core.local.cljs$core$IFn$_invoke$arity$2((0),new cljs.core.Keyword("oc.web.components.invite-slack-modal","sending","oc.web.components.invite-slack-modal/sending",2114495154)),rum.core.local.cljs$core$IFn$_invoke$arity$2((0),new cljs.core.Keyword("oc.web.components.invite-slack-modal","initial-sending","oc.web.components.invite-slack-modal/initial-sending",840025360)),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
oc.web.components.invite_slack_modal.setup_initial_rows(s);

oc.web.actions.nux.dismiss_post_added_tooltip();

var org_data_44800 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"org-data","org-data",96720321)));
oc.web.actions.org.get_org.cljs$core$IFn$_invoke$arity$2(org_data_44800,true);

oc.web.actions.team.teams_get();

return s;
}),new cljs.core.Keyword(null,"after-render","after-render",1997533433),(function (s){
var G__44775_44801 = $("[data-toggle=\"tooltip\"]");
G__44775_44801.tooltip("fixTitle");

G__44775_44801.tooltip("hide");


return s;
}),new cljs.core.Keyword(null,"will-update","will-update",328062998),(function (s){
var sending_44802 = new cljs.core.Keyword("oc.web.components.invite-slack-modal","sending","oc.web.components.invite-slack-modal/sending",2114495154).cljs$core$IFn$_invoke$arity$1(s);
var initial_sending_44803 = new cljs.core.Keyword("oc.web.components.invite-slack-modal","initial-sending","oc.web.components.invite-slack-modal/initial-sending",840025360).cljs$core$IFn$_invoke$arity$1(s);
if((cljs.core.deref(sending_44802) > (0))){
var invite_drv_44804 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"invite-data","invite-data",-758838050)));
var no_error_invites_44805 = cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__44744_SHARP_){
return cljs.core.not(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(p1__44744_SHARP_));
}),new cljs.core.Keyword(null,"invite-users","invite-users",107417337).cljs$core$IFn$_invoke$arity$1(invite_drv_44804));
var error_invites_44806 = cljs.core.filter.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"error","error",-978969032),new cljs.core.Keyword(null,"invite-users","invite-users",107417337).cljs$core$IFn$_invoke$arity$1(invite_drv_44804));
var hold_initial_sending_44807 = cljs.core.deref(initial_sending_44803);
cljs.core.reset_BANG_(sending_44802,cljs.core.count(no_error_invites_44805));

if((cljs.core.count(no_error_invites_44805) === (0))){
oc.web.lib.utils.after((1000),(function (){
cljs.core.reset_BANG_(sending_44802,(0));

cljs.core.reset_BANG_(initial_sending_44803,(0));

if((cljs.core.count(error_invites_44806) === (0))){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.invite-slack-modal","send-bt-cta","oc.web.components.invite-slack-modal/send-bt-cta",1663457241).cljs$core$IFn$_invoke$arity$1(s),"Slack invitation sent!");

oc.web.lib.utils.after((2500),(function (){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.invite-slack-modal","send-bt-cta","oc.web.components.invite-slack-modal/send-bt-cta",1663457241).cljs$core$IFn$_invoke$arity$1(s),"Send Slack invitations");
}));

oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"title","title",636505583),["Slack invite",(((hold_initial_sending_44807 > (1)))?"s":null)," sent."].join(''),new cljs.core.Keyword(null,"primary-bt-title","primary-bt-title",653140150),"OK",new cljs.core.Keyword(null,"primary-bt-dismiss","primary-bt-dismiss",-820688058),true,new cljs.core.Keyword(null,"expire","expire",-70657108),(3),new cljs.core.Keyword(null,"primary-bt-inline","primary-bt-inline",-796141614),true,new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"invites-sent","invites-sent",555070572)], null));

return oc.web.components.invite_slack_modal.setup_initial_rows(s);
} else {
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.invite-slack-modal","send-bt-cta","oc.web.components.invite-slack-modal/send-bt-cta",1663457241).cljs$core$IFn$_invoke$arity$1(s),"Send Slack invitations");
}
}));
} else {
}
} else {
}

return s;
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"invite-users","invite-users",107417337)], null),null], null));

return s;
})], null)], null),"invite-slack-modal");

//# sourceMappingURL=oc.web.components.invite_slack_modal.js.map
