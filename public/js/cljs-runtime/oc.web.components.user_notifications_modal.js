goog.provide('oc.web.components.user_notifications_modal');
oc.web.components.user_notifications_modal.change_BANG_ = (function oc$web$components$user_notifications_modal$change_BANG_(s,k,v){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"edit-user-profile","edit-user-profile",-479059118),k], null),v], null));

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"edit-user-profile","edit-user-profile",-479059118),new cljs.core.Keyword(null,"has-changes","has-changes",-631476764)], null),true], null));
});
oc.web.components.user_notifications_modal.save_clicked = (function oc$web$components$user_notifications_modal$save_clicked(s){
if(cljs.core.compare_and_set_BANG_(new cljs.core.Keyword("oc.web.components.user-notifications-modal","loading","oc.web.components.user-notifications-modal/loading",1234950153).cljs$core$IFn$_invoke$arity$1(s),false,true)){
var edit_user_profile = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"edit-user-profile","edit-user-profile",-479059118)));
var current_user_data = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915)));
return oc.web.actions.user.user_profile_save.cljs$core$IFn$_invoke$arity$2(current_user_data,edit_user_profile);
} else {
return null;
}
});
oc.web.components.user_notifications_modal.close_clicked = (function oc$web$components$user_notifications_modal$close_clicked(current_user_data,dismiss_action){
if(cljs.core.truth_(new cljs.core.Keyword(null,"has-changes","has-changes",-631476764).cljs$core$IFn$_invoke$arity$1(current_user_data))){
var alert_data = new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"icon","icon",1679606541),"/img/ML/trash.svg",new cljs.core.Keyword(null,"action","action",-811238024),"user-profile-unsaved-edits",new cljs.core.Keyword(null,"message","message",-406056002),"Leave without saving your changes?",new cljs.core.Keyword(null,"link-button-title","link-button-title",-812305976),"Stay",new cljs.core.Keyword(null,"link-button-cb","link-button-cb",559710301),(function (){
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
oc.web.components.user_notifications_modal.digest_time_label = (function oc$web$components$user_notifications_modal$digest_time_label(t){
var time_string = cljs.core.name(t);
var minutes = cljs.core.subs.cljs$core$IFn$_invoke$arity$3(time_string,(((time_string).length) - (2)),((time_string).length));
var hours_STAR_ = window.parseInt(cljs.core.subs.cljs$core$IFn$_invoke$arity$3(time_string,(0),(((time_string).length) - (2))),(10));
var hours = (((hours_STAR_ > (12)))?(hours_STAR_ - (12)):hours_STAR_);
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(hours),":",minutes,(((hours_STAR_ > (11)))?" PM":" AM")].join('');
});
oc.web.components.user_notifications_modal.user_notifications_modal = rum.core.build_defcs((function (s){
var org_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"org-data","org-data",96720321));
var user_profile_drv = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"edit-user-profile","edit-user-profile",-479059118));
var current_user_data = new cljs.core.Keyword(null,"user-data","user-data",2143823568).cljs$core$IFn$_invoke$arity$1(user_profile_drv);
var bots_data = oc.web.lib.jwt.team_has_bot_QMARK_(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(org_data));
var team_roster = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"team-roster","team-roster",-1945092859));
var slack_enabled_QMARK_ = oc.web.utils.user.user_has_slack_with_bot_QMARK_(current_user_data,bots_data,team_roster);
return React.createElement("div",({"className": "user-notifications-modal-container"}),React.createElement("button",({"onClick": (function (){
return oc.web.components.user_notifications_modal.close_clicked(current_user_data,oc.web.actions.nav_sidebar.close_all_panels);
}), "className": "mlb-reset modal-close-bt"})),React.createElement("div",({"className": "user-notifications-modal"}),React.createElement("div",({"className": "user-notifications-header"}),React.createElement("div",({"className": "user-notifications-header-title"}),"Notification settings"),React.createElement("button",({"onClick": (function (){
if(cljs.core.truth_(new cljs.core.Keyword(null,"has-changes","has-changes",-631476764).cljs$core$IFn$_invoke$arity$1(current_user_data))){
return oc.web.components.user_notifications_modal.save_clicked(s);
} else {
return oc.web.actions.nav_sidebar.show_user_settings(null);
}
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["mlb-reset","save-bt",(cljs.core.truth_((function (){var or__4126__auto__ = cljs.core.not(new cljs.core.Keyword(null,"has-changes","has-changes",-631476764).cljs$core$IFn$_invoke$arity$1(current_user_data));
if(or__4126__auto__){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = cljs.core.deref(new cljs.core.Keyword("oc.web.components.user-notifications-modal","show-success","oc.web.components.user-notifications-modal/show-success",1638240233).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
return cljs.core.deref(new cljs.core.Keyword("oc.web.components.user-notifications-modal","loading","oc.web.components.user-notifications-modal/loading",1234950153).cljs$core$IFn$_invoke$arity$1(s));
}
}
})())?"disabled":null)], null))}),(cljs.core.truth_(new cljs.core.Keyword(null,"loading","loading",-737050189).cljs$core$IFn$_invoke$arity$1(current_user_data))?"Saving...":(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.user-notifications-modal","show-success","oc.web.components.user-notifications-modal/show-success",1638240233).cljs$core$IFn$_invoke$arity$1(s)))?"Saved!":"Save"))),React.createElement("button",({"onClick": (function (_){
return oc.web.components.user_notifications_modal.close_clicked(current_user_data,(function (){
return oc.web.actions.nav_sidebar.show_user_settings(null);
}));
}), "className": "mlb-reset cancel-bt"}),"Back")),React.createElement("div",({"className": "user-notifications-body"}),React.createElement("div",({"className": "user-profile-modal-fields"}),React.createElement("div",({"className": "field-label"}),"The latest updates will be sent to you in a digest at your preferred times."),React.createElement("div",({"className": "field-value-group"}),cljs.core.into_array.cljs$core$IFn$_invoke$arity$1((function (){var iter__4529__auto__ = (function oc$web$components$user_notifications_modal$iter__46839(s__46840){
return (new cljs.core.LazySeq(null,(function (){
var s__46840__$1 = s__46840;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__46840__$1);
if(temp__5735__auto__){
var s__46840__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__46840__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__46840__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__46842 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__46841 = (0);
while(true){
if((i__46841 < size__4528__auto__)){
var t = cljs.core._nth(c__4527__auto__,i__46841);
var selected_QMARK_ = (function (){var fexpr__46844 = new cljs.core.Keyword(null,"digest-delivery","digest-delivery",-1355287749).cljs$core$IFn$_invoke$arity$1(current_user_data);
return (fexpr__46844.cljs$core$IFn$_invoke$arity$1 ? fexpr__46844.cljs$core$IFn$_invoke$arity$1(t) : fexpr__46844.call(null,t));
})();
var change_cb = ((function (i__46841,selected_QMARK_,t,c__4527__auto__,size__4528__auto__,b__46842,s__46840__$2,temp__5735__auto__,org_data,user_profile_drv,current_user_data,bots_data,team_roster,slack_enabled_QMARK_){
return (function (){
return oc.web.components.user_notifications_modal.change_BANG_(s,new cljs.core.Keyword(null,"digest-delivery","digest-delivery",-1355287749),(cljs.core.truth_(selected_QMARK_)?cljs.core.disj.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"digest-delivery","digest-delivery",-1355287749).cljs$core$IFn$_invoke$arity$1(current_user_data),t):cljs.core.conj.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"digest-delivery","digest-delivery",-1355287749).cljs$core$IFn$_invoke$arity$1(current_user_data),t)));
});})(i__46841,selected_QMARK_,t,c__4527__auto__,size__4528__auto__,b__46842,s__46840__$2,temp__5735__auto__,org_data,user_profile_drv,current_user_data,bots_data,team_roster,slack_enabled_QMARK_))
;
cljs.core.chunk_append(b__46842,React.createElement("div",({"key": cljs.core.name(t), "className": "field-value group"}),sablono.interpreter.interpret((function (){var G__46845 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"selected","selected",574897764),selected_QMARK_,new cljs.core.Keyword(null,"disabled","disabled",-1529784218),false,new cljs.core.Keyword(null,"did-change-cb","did-change-cb",116554135),change_cb], null);
return (oc.web.components.ui.carrot_checkbox.carrot_checkbox.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.carrot_checkbox.carrot_checkbox.cljs$core$IFn$_invoke$arity$1(G__46845) : oc.web.components.ui.carrot_checkbox.carrot_checkbox.call(null,G__46845));
})()),React.createElement("span",({"onClick": change_cb, "className": "digest-time"}),sablono.interpreter.interpret(oc.web.components.user_notifications_modal.digest_time_label(t)))));

var G__46864 = (i__46841 + (1));
i__46841 = G__46864;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__46842),oc$web$components$user_notifications_modal$iter__46839(cljs.core.chunk_rest(s__46840__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__46842),null);
}
} else {
var t = cljs.core.first(s__46840__$2);
var selected_QMARK_ = (function (){var fexpr__46846 = new cljs.core.Keyword(null,"digest-delivery","digest-delivery",-1355287749).cljs$core$IFn$_invoke$arity$1(current_user_data);
return (fexpr__46846.cljs$core$IFn$_invoke$arity$1 ? fexpr__46846.cljs$core$IFn$_invoke$arity$1(t) : fexpr__46846.call(null,t));
})();
var change_cb = ((function (selected_QMARK_,t,s__46840__$2,temp__5735__auto__,org_data,user_profile_drv,current_user_data,bots_data,team_roster,slack_enabled_QMARK_){
return (function (){
return oc.web.components.user_notifications_modal.change_BANG_(s,new cljs.core.Keyword(null,"digest-delivery","digest-delivery",-1355287749),(cljs.core.truth_(selected_QMARK_)?cljs.core.disj.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"digest-delivery","digest-delivery",-1355287749).cljs$core$IFn$_invoke$arity$1(current_user_data),t):cljs.core.conj.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"digest-delivery","digest-delivery",-1355287749).cljs$core$IFn$_invoke$arity$1(current_user_data),t)));
});})(selected_QMARK_,t,s__46840__$2,temp__5735__auto__,org_data,user_profile_drv,current_user_data,bots_data,team_roster,slack_enabled_QMARK_))
;
return cljs.core.cons(React.createElement("div",({"key": cljs.core.name(t), "className": "field-value group"}),sablono.interpreter.interpret((function (){var G__46847 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"selected","selected",574897764),selected_QMARK_,new cljs.core.Keyword(null,"disabled","disabled",-1529784218),false,new cljs.core.Keyword(null,"did-change-cb","did-change-cb",116554135),change_cb], null);
return (oc.web.components.ui.carrot_checkbox.carrot_checkbox.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.carrot_checkbox.carrot_checkbox.cljs$core$IFn$_invoke$arity$1(G__46847) : oc.web.components.ui.carrot_checkbox.carrot_checkbox.call(null,G__46847));
})()),React.createElement("span",({"onClick": change_cb, "className": "digest-time"}),sablono.interpreter.interpret(oc.web.components.user_notifications_modal.digest_time_label(t)))),oc$web$components$user_notifications_modal$iter__46839(cljs.core.rest(s__46840__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(oc.web.local_settings.digest_times);
})())),React.createElement("div",({"className": "field-description"}),"Your timezone is ",React.createElement("a",({"href": "?user-settings=profile", "onClick": (function (){
return oc.web.actions.nav_sidebar.show_user_settings(new cljs.core.Keyword(null,"profile","profile",-545963874));
}), "data-toggle": (cljs.core.truth_(oc.web.lib.responsive.is_mobile_size_QMARK_())?null:"tooltip"), "data-placement": "top", "data-container": "body", "title": "Change your timezone"}),sablono.interpreter.interpret(oc.web.utils.user.readable_tz(new cljs.core.Keyword(null,"timezone","timezone",1831928099).cljs$core$IFn$_invoke$arity$1(current_user_data)))),".")),React.createElement("div",({"className": "user-profile-modal-fields"}),React.createElement("div",({"className": "field-label"}),"Mentions:"),sablono.interpreter.create_element.cljs$core$IFn$_invoke$arity$variadic("select",({"value": new cljs.core.Keyword(null,"notification-medium","notification-medium",195200470).cljs$core$IFn$_invoke$arity$1(current_user_data), "onChange": (function (p1__46832_SHARP_){
return oc.web.components.user_notifications_modal.change_BANG_(s,new cljs.core.Keyword(null,"notification-medium","notification-medium",195200470),p1__46832_SHARP_.target.value);
}), "className": "field-value oc-input"}),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([React.createElement("option",({"value": "email"}),"Via email"),sablono.interpreter.interpret((cljs.core.truth_((function (){var or__4126__auto__ = slack_enabled_QMARK_;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"notification-medium","notification-medium",195200470).cljs$core$IFn$_invoke$arity$1(current_user_data),"slack");
}
})())?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"option","option",65132272),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"value","value",305978217),"slack",new cljs.core.Keyword(null,"disabled","disabled",-1529784218),cljs.core.not(slack_enabled_QMARK_)], null),"Via Slack"], null):null)),React.createElement("option",({"value": "in-app"}),"In-app only")], 0)),React.createElement("div",({"className": "field-description"}),"Notifications are sent in real-time if someone mentions you.")),sablono.interpreter.interpret(((oc.web.local_settings.reminders_enabled_QMARK_)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-profile-modal-fields","div.user-profile-modal-fields",867822299),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.field-label","div.field-label",1429859759),"Recurring update reminders"], null),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"select.field-value.oc-input","select.field-value.oc-input",-1851537268),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"value","value",305978217),new cljs.core.Keyword(null,"reminder-medium","reminder-medium",679614122).cljs$core$IFn$_invoke$arity$1(current_user_data),new cljs.core.Keyword(null,"on-change","on-change",-732046149),(function (p1__46833_SHARP_){
return oc.web.components.user_notifications_modal.change_BANG_(s,new cljs.core.Keyword(null,"reminder-medium","reminder-medium",679614122),p1__46833_SHARP_.target.value);
})], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"option","option",65132272),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"value","value",305978217),"email"], null),"Via email"], null),(cljs.core.truth_((function (){var or__4126__auto__ = slack_enabled_QMARK_;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"reminder-medium","reminder-medium",679614122).cljs$core$IFn$_invoke$arity$1(current_user_data),"slack");
}
})())?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"option","option",65132272),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"value","value",305978217),"slack",new cljs.core.Keyword(null,"disabled","disabled",-1529784218),cljs.core.not(slack_enabled_QMARK_)], null),"Via Slack"], null):null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"option","option",65132272),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"value","value",305978217),"in-app"], null),"In-app only"], null)], null)], null):null)))));
}),new cljs.core.PersistentVector(null, 9, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"org-data","org-data",96720321)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"team-roster","team-roster",-1945092859)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"edit-user-profile","edit-user-profile",-479059118)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.user-notifications-modal","loading","oc.web.components.user-notifications-modal/loading",1234950153)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.user-notifications-modal","show-success","oc.web.components.user-notifications-modal/show-success",1638240233)),oc.web.mixins.ui.refresh_tooltips_mixin,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
oc.web.actions.user.get_user(null);

return s;
}),new cljs.core.Keyword(null,"did-remount","did-remount",1362550500),(function (_,new_state){
var user_data_46868 = new cljs.core.Keyword(null,"user-data","user-data",2143823568).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(org.martinklepsch.derivatives.get_ref(new_state,new cljs.core.Keyword(null,"edit-user-profile","edit-user-profile",-479059118))));
if(cljs.core.truth_((function (){var and__4115__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.user-notifications-modal","loading","oc.web.components.user-notifications-modal/loading",1234950153).cljs$core$IFn$_invoke$arity$1(new_state));
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(new cljs.core.Keyword(null,"has-changes","has-changes",-631476764).cljs$core$IFn$_invoke$arity$1(user_data_46868));
} else {
return and__4115__auto__;
}
})())){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.user-notifications-modal","show-success","oc.web.components.user-notifications-modal/show-success",1638240233).cljs$core$IFn$_invoke$arity$1(new_state),true);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.user-notifications-modal","loading","oc.web.components.user-notifications-modal/loading",1234950153).cljs$core$IFn$_invoke$arity$1(new_state),false);

oc.web.lib.utils.after((2500),(function (){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.user-notifications-modal","show-success","oc.web.components.user-notifications-modal/show-success",1638240233).cljs$core$IFn$_invoke$arity$1(new_state),false);
}));
} else {
}

return new_state;
})], null)], null),"user-notifications-modal");

//# sourceMappingURL=oc.web.components.user_notifications_modal.js.map
