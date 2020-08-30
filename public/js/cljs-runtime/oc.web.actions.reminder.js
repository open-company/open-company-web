goog.provide('oc.web.actions.reminder');
/**
 * Load the roster of the users that can be assigned to reminders.
 */
oc.web.actions.reminder.load_reminders_roster = (function oc$web$actions$reminder$load_reminders_roster(){
if(oc.web.local_settings.reminders_enabled_QMARK_){
var reminders_data = oc.web.dispatcher.reminders_data.cljs$core$IFn$_invoke$arity$0();
var roster_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(reminders_data),"roster"], 0));
if(cljs.core.truth_(roster_link)){
return oc.web.api.get_reminders_roster(roster_link,(function (p__39961){
var map__39962 = p__39961;
var map__39962__$1 = (((((!((map__39962 == null))))?(((((map__39962.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__39962.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__39962):map__39962);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39962__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39962__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39962__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
if(cljs.core.truth_(success)){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"reminders-roster-loaded","reminders-roster-loaded",505132649),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.lib.json.json__GT_cljs(body)], null));
} else {
return null;
}
}));
} else {
return null;
}
} else {
return null;
}
});
/**
 * Reminders data loaded, parse and dispatch the content to the app-state.
 */
oc.web.actions.reminder.reminders_loaded = (function oc$web$actions$reminder$reminders_loaded(p__39964){
var map__39965 = p__39964;
var map__39965__$1 = (((((!((map__39965 == null))))?(((((map__39965.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__39965.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__39965):map__39965);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39965__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39965__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39965__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
if(cljs.core.truth_(success)){
var parsed_body = oc.web.lib.json.json__GT_cljs(body);
var reminders_data = new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(parsed_body);
var parsed_reminders = oc.web.utils.reminder.parse_reminders(reminders_data);
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"reminders-loaded","reminders-loaded",-626131474),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),parsed_reminders], null));

if(cljs.core.truth_(oc.web.dispatcher.reminders_roster_data.cljs$core$IFn$_invoke$arity$0())){
return null;
} else {
return oc.web.actions.reminder.load_reminders_roster();
}
} else {
return null;
}
});
/**
 * 
 *   Load the reminders list.
 *   NB: first reminders is loaded in did-mount of dashboard-layout component.
 */
oc.web.actions.reminder.load_reminders = (function oc$web$actions$reminder$load_reminders(){
if(oc.web.local_settings.reminders_enabled_QMARK_){
var temp__33777__auto__ = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
if(cljs.core.truth_(temp__33777__auto__)){
var org_data = temp__33777__auto__;
var temp__33777__auto____$1 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"reminders"], 0));
if(cljs.core.truth_(temp__33777__auto____$1)){
var reminders_link = temp__33777__auto____$1;
return oc.web.api.get_reminders(reminders_link,oc.web.actions.reminder.reminders_loaded);
} else {
return null;
}
} else {
return null;
}
} else {
return null;
}
});
/**
 * Move a reminder in the edit location of the app-state and open the edit component.
 */
oc.web.actions.reminder.edit_reminder = (function oc$web$actions$reminder$edit_reminder(reminder_uuid){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"edit-reminder","edit-reminder",1674513040),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),reminder_uuid], null));

return oc.web.actions.nav_sidebar.edit_reminder(reminder_uuid);
});
/**
 * Move an empty reminder in the edit location of the app-state and open the edit component.
 */
oc.web.actions.reminder.new_reminder = (function oc$web$actions$reminder$new_reminder(){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"edit-reminder","edit-reminder",1674513040),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0()], null));

return oc.web.actions.nav_sidebar.show_new_reminder();
});
/**
 * Update a reminder map.
 */
oc.web.actions.reminder.update_reminder = (function oc$web$actions$reminder$update_reminder(reminder_uuid,value_or_fn){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update-reminder","update-reminder",1622451323),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),reminder_uuid,value_or_fn], null));
});
/**
 * 
 *   Save a reminder moving it into the local list and also by creating or updating it on the server.
 *   Refresh the list of reminders when finished.
 *   
 */
oc.web.actions.reminder.save_reminder = (function oc$web$actions$reminder$save_reminder(reminder_data){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"save-reminder","save-reminder",605492069),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),reminder_data], null));

var reminders_data = oc.web.dispatcher.reminders_data.cljs$core$IFn$_invoke$arity$0();
var reminders_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(reminders_data),"self"], 0));
var refresh_reminders = (function (){
return oc.web.api.get_reminders(reminders_link,oc.web.actions.reminder.reminders_loaded);
});
if(cljs.core.truth_(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(reminder_data))){
var update_reminder_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(reminder_data),"partial-update"], 0));
return oc.web.api.update_reminder(update_reminder_link,reminder_data,(function (p__39972){
var map__39973 = p__39972;
var map__39973__$1 = (((((!((map__39973 == null))))?(((((map__39973.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__39973.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__39973):map__39973);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39973__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39973__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39973__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
if(cljs.core.truth_(success)){
oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"title","title",636505583),"Reminder updated",new cljs.core.Keyword(null,"primary-bt-title","primary-bt-title",653140150),"OK",new cljs.core.Keyword(null,"primary-bt-dismiss","primary-bt-dismiss",-820688058),true,new cljs.core.Keyword(null,"expire","expire",-70657108),(3),new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"reminder-updated","reminder-updated",479505447)], null));
} else {
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("save-reminder","error","save-reminder/error",-1211089034),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),reminder_data], null));

oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"title","title",636505583),"An error occurred",new cljs.core.Keyword(null,"description","description",-1428560544),"Please try again",new cljs.core.Keyword(null,"primary-bt-title","primary-bt-title",653140150),"OK",new cljs.core.Keyword(null,"primary-bt-dismiss","primary-bt-dismiss",-820688058),true,new cljs.core.Keyword(null,"expire","expire",-70657108),(3),new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"reminder-update-failed","reminder-update-failed",-1731275020)], null));

oc.web.actions.nav_sidebar.edit_reminder(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(reminder_data));
}

return refresh_reminders();
}));
} else {
var add_reminder_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(reminders_data),"create"], 0));
return oc.web.api.add_reminder(add_reminder_link,reminder_data,(function (p__39983){
var map__39984 = p__39983;
var map__39984__$1 = (((((!((map__39984 == null))))?(((((map__39984.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__39984.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__39984):map__39984);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39984__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39984__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39984__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
if(cljs.core.truth_(success)){
var self_reminder_40002 = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"assignee","assignee",-1242457026).cljs$core$IFn$_invoke$arity$1(reminder_data)),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.current_user_data.cljs$core$IFn$_invoke$arity$0()));
oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"title","title",636505583),((self_reminder_40002)?"Reminder created":"Reminder created and teammate notified"),new cljs.core.Keyword(null,"primary-bt-title","primary-bt-title",653140150),"OK",new cljs.core.Keyword(null,"primary-bt-dismiss","primary-bt-dismiss",-820688058),true,new cljs.core.Keyword(null,"expire","expire",-70657108),(3),new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"reminder-create","reminder-create",-1592050664)], null));
} else {
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("save-reminder","error","save-reminder/error",-1211089034),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),reminder_data], null));

oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"title","title",636505583),"An error occurred",new cljs.core.Keyword(null,"description","description",-1428560544),"Please try again",new cljs.core.Keyword(null,"primary-bt-title","primary-bt-title",653140150),"OK",new cljs.core.Keyword(null,"primary-bt-dismiss","primary-bt-dismiss",-820688058),true,new cljs.core.Keyword(null,"expire","expire",-70657108),(3),new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"reminder-update-failed","reminder-update-failed",-1731275020)], null));

oc.web.actions.nav_sidebar.show_new_reminder();
}

return refresh_reminders();
}));
}
});
/**
 * Exit edit losing changes.
 */
oc.web.actions.reminder.cancel_edit_reminder = (function oc$web$actions$reminder$cancel_edit_reminder(){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"cancel-edit-reminder","cancel-edit-reminder",-1112196612),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0()], null));

return oc.web.actions.nav_sidebar.close_reminders();
});
/**
 * Delete a reminder.
 */
oc.web.actions.reminder.delete_reminder = (function oc$web$actions$reminder$delete_reminder(reminder_uuid){
var reminders_data = oc.web.dispatcher.reminders_data.cljs$core$IFn$_invoke$arity$0();
var reminder_data = cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__39986_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__39986_SHARP_),reminder_uuid);
}),new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(reminders_data)));
var delete_reminder_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(reminder_data),"delete"], 0));
var reminders_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(reminders_data),"self"], 0));
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"delete-reminder","delete-reminder",788982201),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),reminder_uuid], null));

oc.web.actions.nav_sidebar.show_reminders();

return oc.web.api.delete_reminder(delete_reminder_link,(function (p__39990){
var map__39991 = p__39990;
var map__39991__$1 = (((((!((map__39991 == null))))?(((((map__39991.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__39991.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__39991):map__39991);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39991__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39991__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39991__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
if(cljs.core.truth_(success)){
oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"title","title",636505583),"Reminder deleted",new cljs.core.Keyword(null,"primary-bt-title","primary-bt-title",653140150),"OK",new cljs.core.Keyword(null,"primary-bt-dismiss","primary-bt-dismiss",-820688058),true,new cljs.core.Keyword(null,"expire","expire",-70657108),(3),new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"reminder-deleted","reminder-deleted",-668143136)], null));
} else {
}

return oc.web.api.get_reminders(reminders_link,oc.web.actions.reminder.reminders_loaded);
}));
});

//# sourceMappingURL=oc.web.actions.reminder.js.map
