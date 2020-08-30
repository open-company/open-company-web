goog.provide('oc.web.components.org_dashboard');
oc.web.components.org_dashboard.init_whats_new = (function oc$web$components$org_dashboard$init_whats_new(){
if(oc.web.lib.responsive.is_tablet_or_mobile_QMARK_()){
return null;
} else {
return oc.web.lib.whats_new.init();
}
});
oc.web.components.org_dashboard.org_dashboard = rum.core.build_defcs((function (s){
var map__46927 = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"org-dashboard-data","org-dashboard-data",-593195621));
var map__46927__$1 = (((((!((map__46927 == null))))?(((((map__46927.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46927.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46927):map__46927);
var wrt_read_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46927__$1,new cljs.core.Keyword(null,"wrt-read-data","wrt-read-data",-1241718247));
var contributions_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46927__$1,new cljs.core.Keyword(null,"contributions-data","contributions-data",242246811));
var activity_share_container = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46927__$1,new cljs.core.Keyword(null,"activity-share-container","activity-share-container",-1384168706));
var org_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46927__$1,new cljs.core.Keyword(null,"org-data","org-data",96720321));
var app_loading = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46927__$1,new cljs.core.Keyword(null,"app-loading","app-loading",248815106));
var panel_stack = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46927__$1,new cljs.core.Keyword(null,"panel-stack","panel-stack",1084180004));
var force_login_wall = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46927__$1,new cljs.core.Keyword(null,"force-login-wall","force-login-wall",1907021508));
var current_board_slug = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46927__$1,new cljs.core.Keyword(null,"current-board-slug","current-board-slug",1670379364));
var current_user_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46927__$1,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915));
var is_showing_alert = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46927__$1,new cljs.core.Keyword(null,"is-showing-alert","is-showing-alert",1899000552));
var active_users = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46927__$1,new cljs.core.Keyword(null,"active-users","active-users",-329555191));
var posts_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46927__$1,new cljs.core.Keyword(null,"posts-data","posts-data",-788590901));
var jwt_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46927__$1,new cljs.core.Keyword(null,"jwt-data","jwt-data",1687811404));
var payments_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46927__$1,new cljs.core.Keyword(null,"payments-data","payments-data",1676265421));
var current_org_slug = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46927__$1,new cljs.core.Keyword(null,"current-org-slug","current-org-slug",1185011374));
var current_activity_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46927__$1,new cljs.core.Keyword(null,"current-activity-id","current-activity-id",-930108529));
var is_sharing_activity = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46927__$1,new cljs.core.Keyword(null,"is-sharing-activity","is-sharing-activity",-1623884847));
var orgs = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46927__$1,new cljs.core.Keyword(null,"orgs","orgs",155776628));
var initial_section_editing = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46927__$1,new cljs.core.Keyword(null,"initial-section-editing","initial-section-editing",1587721238));
var container_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46927__$1,new cljs.core.Keyword(null,"container-data","container-data",-53681130));
var cmail_state = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46927__$1,new cljs.core.Keyword(null,"cmail-state","cmail-state",-747393321));
var current_contributions_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46927__$1,new cljs.core.Keyword(null,"current-contributions-id","current-contributions-id",-1702467529));
var show_section_add_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46927__$1,new cljs.core.Keyword(null,"show-section-add-cb","show-section-add-cb",2030550136));
var user_info_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46927__$1,new cljs.core.Keyword(null,"user-info-data","user-info-data",-1081830984));
var is_mobile_QMARK_ = oc.web.lib.responsive.is_tablet_or_mobile_QMARK_();
var loading_QMARK_ = (function (){var or__4126__auto__ = app_loading;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return ((cljs.core.not(org_data)) || (((cljs.core.not(current_board_slug)) && (cljs.core.not(current_contributions_id)) && ((cljs.core.count(new cljs.core.Keyword(null,"boards","boards",1912049694).cljs$core$IFn$_invoke$arity$1(org_data)) > (0))))) || ((((!(is_mobile_QMARK_))) && ((!(cljs.core.map_QMARK_(active_users)))))));
}
})();
var org_not_found = (((!((orgs == null)))) && (cljs.core.not((function (){var fexpr__46930 = cljs.core.set(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850),orgs));
return (fexpr__46930.cljs$core$IFn$_invoke$arity$1 ? fexpr__46930.cljs$core$IFn$_invoke$arity$1(current_org_slug) : fexpr__46930.call(null,current_org_slug));
})())));
var section_not_found = (((!(org_not_found)))?(function (){var and__4115__auto__ = org_data;
if(cljs.core.truth_(and__4115__auto__)){
return ((cljs.core.not(current_contributions_id)) && (cljs.core.not(oc.web.dispatcher.is_container_QMARK_(current_board_slug))) && (cljs.core.not((function (){var fexpr__46932 = cljs.core.set(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850),new cljs.core.Keyword(null,"boards","boards",1912049694).cljs$core$IFn$_invoke$arity$1(org_data)));
return (fexpr__46932.cljs$core$IFn$_invoke$arity$1 ? fexpr__46932.cljs$core$IFn$_invoke$arity$1(current_board_slug) : fexpr__46932.call(null,current_board_slug));
})())));
} else {
return and__4115__auto__;
}
})():false);
var current_activity_data = (cljs.core.truth_(current_activity_id)?cljs.core.get.cljs$core$IFn$_invoke$arity$2(posts_data,current_activity_id):null);
var entry_not_found = (((!(org_not_found)))?(((!(cljs.core.map_QMARK_(active_users))))?((cljs.core.not(section_not_found))?(function (){var and__4115__auto__ = current_activity_data;
if(cljs.core.truth_(and__4115__auto__)){
return ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(current_activity_data,new cljs.core.Keyword(null,"404","404",948666615))) || (((cljs.core.map_QMARK_(current_activity_data)) && (cljs.core.not(new cljs.core.Keyword(null,"loading","loading",-737050189).cljs$core$IFn$_invoke$arity$1(current_activity_data))) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(current_activity_data),current_board_slug)) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127).cljs$core$IFn$_invoke$arity$1(current_activity_data),current_board_slug)))));
} else {
return and__4115__auto__;
}
})():false):false):false);
var show_login_wall = ((cljs.core.not(jwt_data))?(function (){var or__4126__auto__ = force_login_wall;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var and__4115__auto__ = current_activity_id;
if(cljs.core.truth_(and__4115__auto__)){
var or__4126__auto____$1 = org_not_found;
if(or__4126__auto____$1){
return or__4126__auto____$1;
} else {
var or__4126__auto____$2 = section_not_found;
if(cljs.core.truth_(or__4126__auto____$2)){
return or__4126__auto____$2;
} else {
return entry_not_found;
}
}
} else {
return and__4115__auto__;
}
}
})():false);
var show_activity_removed = (function (){var and__4115__auto__ = jwt_data;
if(cljs.core.truth_(and__4115__auto__)){
var and__4115__auto____$1 = current_activity_id;
if(cljs.core.truth_(and__4115__auto____$1)){
var or__4126__auto__ = org_not_found;
if(or__4126__auto__){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = section_not_found;
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
return entry_not_found;
}
}
} else {
return and__4115__auto____$1;
}
} else {
return and__4115__auto__;
}
})();
var is_loading = ((cljs.core.not(show_login_wall))?((cljs.core.not(show_activity_removed))?loading_QMARK_:false):false);
var open_panel = cljs.core.last(panel_stack);
var show_section_editor = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(open_panel,new cljs.core.Keyword(null,"section-edit","section-edit",-155333386));
var show_section_add = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(open_panel,new cljs.core.Keyword(null,"section-add","section-add",1356275896));
var show_reminders_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(open_panel,new cljs.core.Keyword(null,"reminders","reminders",-2135532712));
var show_reminder_edit_QMARK_ = (function (){var and__4115__auto__ = open_panel;
if(cljs.core.truth_(and__4115__auto__)){
return clojure.string.starts_with_QMARK_(cljs.core.name(open_panel),"reminder-");
} else {
return and__4115__auto__;
}
})();
var show_reminders_view_QMARK_ = (function (){var or__4126__auto__ = show_reminders_QMARK_;
if(or__4126__auto__){
return or__4126__auto__;
} else {
return show_reminder_edit_QMARK_;
}
})();
var show_wrt_view_QMARK_ = (function (){var and__4115__auto__ = open_panel;
if(cljs.core.truth_(and__4115__auto__)){
return clojure.string.starts_with_QMARK_(cljs.core.name(open_panel),"wrt-");
} else {
return and__4115__auto__;
}
})();
var show_mobile_cmail_QMARK_ = (function (){var and__4115__auto__ = cmail_state;
if(cljs.core.truth_(and__4115__auto__)){
return ((cljs.core.not(new cljs.core.Keyword(null,"collapsed","collapsed",-628494523).cljs$core$IFn$_invoke$arity$1(cmail_state))) && (is_mobile_QMARK_));
} else {
return and__4115__auto__;
}
})();
var user_responded_to_push_permission_QMARK_ = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"user-responded-to-push-permission?","user-responded-to-push-permission?",1261192686));
var show_push_notification_permissions_modal_QMARK_ = ((oc.shared.useragent.mobile_app_QMARK_)?(function (){var and__4115__auto__ = jwt_data;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(user_responded_to_push_permission_QMARK_);
} else {
return and__4115__auto__;
}
})():false);
var show_trial_expired_QMARK_ = oc.web.actions.payments.show_paywall_alert_QMARK_(payments_data);
var show_user_info_QMARK_ = (function (){var and__4115__auto__ = open_panel;
if(cljs.core.truth_(and__4115__auto__)){
return clojure.string.starts_with_QMARK_(cljs.core.name(open_panel),"user-info-");
} else {
return and__4115__auto__;
}
})();
var show_follow_picker = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(open_panel,new cljs.core.Keyword(null,"follow-picker","follow-picker",-381856085));
if(cljs.core.truth_(is_loading)){
var attrs46933 = (function (){var G__46934 = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"loading","loading",-737050189),true], null);
return (oc.web.components.ui.loading.loading.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.loading.loading.cljs$core$IFn$_invoke$arity$1(G__46934) : oc.web.components.ui.loading.loading.call(null,G__46934));
})();
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46933))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["org-dashboard"], null)], null),attrs46933], 0))):({"className": "org-dashboard"})),((cljs.core.map_QMARK_(attrs46933))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46933)], null)));
} else {
return React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"org-dashboard","org-dashboard",511983568),true,new cljs.core.Keyword(null,"mobile-or-tablet","mobile-or-tablet",43010116),is_mobile_QMARK_,new cljs.core.Keyword(null,"login-wall","login-wall",-787271322),show_login_wall,new cljs.core.Keyword(null,"activity-removed","activity-removed",-620911981),show_activity_removed,new cljs.core.Keyword(null,"expanded-activity","expanded-activity",-1744892679),current_activity_id,new cljs.core.Keyword(null,"trial-expired","trial-expired",-1719455227),show_trial_expired_QMARK_,new cljs.core.Keyword(null,"show-menu","show-menu",14072792),cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(open_panel,new cljs.core.Keyword(null,"menu","menu",352255198))], null))], null))}),sablono.interpreter.interpret((oc.web.components.ui.login_overlay.login_overlays_handler.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.login_overlay.login_overlays_handler.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.login_overlay.login_overlays_handler.call(null))),sablono.interpreter.interpret((cljs.core.truth_(show_activity_removed)?(oc.web.components.ui.activity_removed.activity_removed.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.activity_removed.activity_removed.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.activity_removed.activity_removed.call(null)):(cljs.core.truth_(show_login_wall)?(oc.web.components.ui.login_wall.login_wall.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.login_wall.login_wall.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.login_wall.login_wall.call(null)):((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(open_panel,new cljs.core.Keyword(null,"payments","payments",-1324138047)))?(function (){var G__46935 = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"org-data","org-data",96720321),org_data], null);
return (oc.web.components.payments_settings_modal.payments_settings_modal.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.payments_settings_modal.payments_settings_modal.cljs$core$IFn$_invoke$arity$1(G__46935) : oc.web.components.payments_settings_modal.payments_settings_modal.call(null,G__46935));
})():((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(open_panel,new cljs.core.Keyword(null,"org","org",1495985)))?(oc.web.components.org_settings_modal.org_settings_modal.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.org_settings_modal.org_settings_modal.cljs$core$IFn$_invoke$arity$0() : oc.web.components.org_settings_modal.org_settings_modal.call(null)):((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(open_panel,new cljs.core.Keyword(null,"integrations","integrations",1844532423)))?(oc.web.components.integrations_settings_modal.integrations_settings_modal.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.integrations_settings_modal.integrations_settings_modal.cljs$core$IFn$_invoke$arity$0() : oc.web.components.integrations_settings_modal.integrations_settings_modal.call(null)):((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(open_panel,new cljs.core.Keyword(null,"invite-picker","invite-picker",1426151962)))?(oc.web.components.invite_picker_modal.invite_picker_modal.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.invite_picker_modal.invite_picker_modal.cljs$core$IFn$_invoke$arity$0() : oc.web.components.invite_picker_modal.invite_picker_modal.call(null)):((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(open_panel,new cljs.core.Keyword(null,"invite-email","invite-email",1375794598)))?(oc.web.components.invite_email_modal.invite_email_modal.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.invite_email_modal.invite_email_modal.cljs$core$IFn$_invoke$arity$0() : oc.web.components.invite_email_modal.invite_email_modal.call(null)):((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(open_panel,new cljs.core.Keyword(null,"invite-slack","invite-slack",-1290785659)))?(oc.web.components.invite_slack_modal.invite_slack_modal.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.invite_slack_modal.invite_slack_modal.cljs$core$IFn$_invoke$arity$0() : oc.web.components.invite_slack_modal.invite_slack_modal.call(null)):((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(open_panel,new cljs.core.Keyword(null,"team","team",1355747699)))?(oc.web.components.team_management_modal.team_management_modal.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.team_management_modal.team_management_modal.cljs$core$IFn$_invoke$arity$0() : oc.web.components.team_management_modal.team_management_modal.call(null)):((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(open_panel,new cljs.core.Keyword(null,"profile","profile",-545963874)))?(oc.web.components.user_profile_modal.user_profile_modal.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.user_profile_modal.user_profile_modal.cljs$core$IFn$_invoke$arity$0() : oc.web.components.user_profile_modal.user_profile_modal.call(null)):((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(open_panel,new cljs.core.Keyword(null,"notifications","notifications",1685638001)))?(oc.web.components.user_notifications_modal.user_notifications_modal.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.user_notifications_modal.user_notifications_modal.cljs$core$IFn$_invoke$arity$0() : oc.web.components.user_notifications_modal.user_notifications_modal.call(null)):((show_reminders_QMARK_)?(oc.web.components.recurring_updates_modal.recurring_updates_modal.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.recurring_updates_modal.recurring_updates_modal.cljs$core$IFn$_invoke$arity$0() : oc.web.components.recurring_updates_modal.recurring_updates_modal.call(null)):(cljs.core.truth_(show_reminder_edit_QMARK_)?(oc.web.components.edit_recurring_update_modal.edit_recurring_update_modal.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.edit_recurring_update_modal.edit_recurring_update_modal.cljs$core$IFn$_invoke$arity$0() : oc.web.components.edit_recurring_update_modal.edit_recurring_update_modal.call(null)):((show_section_editor)?(function (){var G__46936 = initial_section_editing;
var G__46937 = (function (sec_data,note,dismiss_action){
if(cljs.core.truth_(sec_data)){
return oc.web.actions.section.section_save.cljs$core$IFn$_invoke$arity$3(sec_data,note,dismiss_action);
} else {
return (dismiss_action.cljs$core$IFn$_invoke$arity$0 ? dismiss_action.cljs$core$IFn$_invoke$arity$0() : dismiss_action.call(null));
}
});
return (oc.web.components.ui.section_editor.section_editor.cljs$core$IFn$_invoke$arity$2 ? oc.web.components.ui.section_editor.section_editor.cljs$core$IFn$_invoke$arity$2(G__46936,G__46937) : oc.web.components.ui.section_editor.section_editor.call(null,G__46936,G__46937));
})():((show_section_add)?(oc.web.components.ui.section_editor.section_editor.cljs$core$IFn$_invoke$arity$2 ? oc.web.components.ui.section_editor.section_editor.cljs$core$IFn$_invoke$arity$2(null,show_section_add_cb) : oc.web.components.ui.section_editor.section_editor.call(null,null,show_section_add_cb)):(cljs.core.truth_(((is_mobile_QMARK_)?is_sharing_activity:false))?(oc.web.components.ui.activity_share.activity_share.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.activity_share.activity_share.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.activity_share.activity_share.call(null)):(cljs.core.truth_(show_wrt_view_QMARK_)?(oc.web.components.ui.wrt.wrt.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.wrt.wrt.cljs$core$IFn$_invoke$arity$1(org_data) : oc.web.components.ui.wrt.wrt.call(null,org_data)):((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(open_panel,new cljs.core.Keyword(null,"theme","theme",-1247880880)))?(function (){var G__46938 = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"ui-theme","ui-theme",1992064701));
return (oc.web.components.theme_settings_modal.theme_settings_modal.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.theme_settings_modal.theme_settings_modal.cljs$core$IFn$_invoke$arity$1(G__46938) : oc.web.components.theme_settings_modal.theme_settings_modal.call(null,G__46938));
})():(cljs.core.truth_(show_user_info_QMARK_)?(function (){var G__46939 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"user-data","user-data",2143823568),user_info_data,new cljs.core.Keyword(null,"org-data","org-data",96720321),org_data], null);
return (oc.web.components.user_info_modal.user_info_modal.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.user_info_modal.user_info_modal.cljs$core$IFn$_invoke$arity$1(G__46939) : oc.web.components.user_info_modal.user_info_modal.call(null,G__46939));
})():((show_follow_picker)?(oc.web.components.ui.follow_picker.follow_picker.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.follow_picker.follow_picker.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.follow_picker.follow_picker.call(null)):null))))))))))))))))))))),sablono.interpreter.interpret((cljs.core.truth_((((!(is_mobile_QMARK_)))?is_sharing_activity:false))?(function (){var portal_element = (cljs.core.truth_(activity_share_container)?$(["#",cljs.core.str.cljs$core$IFn$_invoke$arity$1(activity_share_container)].join('')).find(".activity-share-container").get((0)):null);
if(cljs.core.truth_(portal_element)){
return rum.core.portal((oc.web.components.ui.activity_share.activity_share.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.activity_share.activity_share.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.activity_share.activity_share.call(null)),portal_element);
} else {
return (oc.web.components.ui.activity_share.activity_share.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.activity_share.activity_share.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.activity_share.activity_share.call(null));
}
})():null)),sablono.interpreter.interpret((cljs.core.truth_(show_mobile_cmail_QMARK_)?(oc.web.components.cmail.cmail.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.cmail.cmail.cljs$core$IFn$_invoke$arity$0() : oc.web.components.cmail.cmail.call(null)):null)),sablono.interpreter.interpret((((((!(is_mobile_QMARK_))) || (((is_mobile_QMARK_) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(open_panel,new cljs.core.Keyword(null,"menu","menu",352255198)))))))?(oc.web.components.ui.menu.menu.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.menu.menu.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.menu.menu.call(null)):null)),sablono.interpreter.interpret((cljs.core.truth_(show_push_notification_permissions_modal_QMARK_)?(oc.web.components.push_notifications_permission_modal.push_notifications_permission_modal.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.push_notifications_permission_modal.push_notifications_permission_modal.cljs$core$IFn$_invoke$arity$0() : oc.web.components.push_notifications_permission_modal.push_notifications_permission_modal.call(null)):null)),sablono.interpreter.interpret((cljs.core.truth_(is_showing_alert)?(oc.web.components.ui.alert_modal.alert_modal.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.alert_modal.alert_modal.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.alert_modal.alert_modal.call(null)):null)),sablono.interpreter.interpret((((((!(is_mobile_QMARK_))) || (((cljs.core.not(is_sharing_activity)) && (cljs.core.not(show_mobile_cmail_QMARK_)) && (cljs.core.not(show_push_notification_permissions_modal_QMARK_))))))?new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.page","div.page",1917984906),((show_trial_expired_QMARK_)?(oc.web.components.ui.trial_expired_banner.trial_expired_banner.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.trial_expired_banner.trial_expired_banner.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.trial_expired_banner.trial_expired_banner.call(null)):null),(oc.web.components.ui.navbar.navbar.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.navbar.navbar.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.navbar.navbar.call(null)),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.org-dashboard-container","div.org-dashboard-container",-1941579986),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.org-dashboard-inner","div.org-dashboard-inner",-1592645094),(oc.web.components.dashboard_layout.dashboard_layout.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.dashboard_layout.dashboard_layout.cljs$core$IFn$_invoke$arity$0() : oc.web.components.dashboard_layout.dashboard_layout.call(null))], null),(cljs.core.truth_(current_activity_id)?(oc.web.components.expanded_post.expanded_post.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.expanded_post.expanded_post.cljs$core$IFn$_invoke$arity$0() : oc.web.components.expanded_post.expanded_post.call(null)):null)], null)], null):null)));
}
}),new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$,rum.core.reactive,oc.web.mixins.ui.render_on_resize(null),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"org-dashboard-data","org-dashboard-data",-593195621)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"user-responded-to-push-permission?","user-responded-to-push-permission?",1261192686)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"ui-theme","ui-theme",1992064701)], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
oc.web.lib.utils.after((100),(function (){
return (document.scrollingElement.scrollTop = oc.web.lib.utils.page_scroll_top());
}));

oc.web.components.org_dashboard.init_whats_new();

return s;
}),new cljs.core.Keyword(null,"did-remount","did-remount",1362550500),(function (_,s){
oc.web.components.org_dashboard.init_whats_new();

return s;
})], null)], null),"org-dashboard");

//# sourceMappingURL=oc.web.components.org_dashboard.js.map
