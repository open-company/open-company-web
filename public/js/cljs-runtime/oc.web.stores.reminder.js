goog.provide('oc.web.stores.reminder');
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"edit-reminder","edit-reminder",1674513040),(function (db,p__38715){
var vec__38716 = p__38715;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38716,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38716,(1),null);
var reminder_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38716,(2),null);
var reminders_data = oc.web.dispatcher.reminders_data.cljs$core$IFn$_invoke$arity$2(org_slug,db);
var reminder_edit_key = oc.web.dispatcher.reminder_edit_key(org_slug);
var old_edit_reminder_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,reminder_edit_key);
var new_reminder_data = oc.web.utils.reminder.parse_reminder(oc.web.utils.reminder.new_reminder_data());
var reminder_data = (function (){var or__4126__auto__ = old_edit_reminder_data;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
if(cljs.core.truth_(reminder_uuid)){
return cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__38714_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__38714_SHARP_),reminder_uuid);
}),new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(reminders_data)));
} else {
return new_reminder_data;
}
}
})();
return cljs.core.assoc_in(db,reminder_edit_key,reminder_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"reminders-loaded","reminders-loaded",-626131474),(function (db,p__38719){
var vec__38720 = p__38719;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38720,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38720,(1),null);
var reminders_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38720,(2),null);
var sorted_reminders = oc.web.utils.reminder.sort_reminders(new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(reminders_data));
return cljs.core.assoc_in(db,oc.web.dispatcher.reminders_data_key(org_slug),cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(reminders_data,new cljs.core.Keyword(null,"items","items",1031954938),sorted_reminders));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"reminders-roster-loaded","reminders-roster-loaded",505132649),(function (db,p__38723){
var vec__38724 = p__38723;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38724,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38724,(1),null);
var roster_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38724,(2),null);
var parsed_roster = oc.web.utils.reminder.parse_reminders_roster(roster_data);
return cljs.core.assoc_in(db,oc.web.dispatcher.reminders_roster_key(org_slug),parsed_roster);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"update-reminder","update-reminder",1622451323),(function (db,p__38727){
var vec__38728 = p__38727;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38728,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38728,(1),null);
var reminder_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38728,(2),null);
var value_or_fn = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38728,(3),null);
var reminder_edit_key = oc.web.dispatcher.reminder_edit_key(org_slug);
var old_reminder_edit_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,reminder_edit_key);
if(cljs.core.fn_QMARK_(value_or_fn)){
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(db,reminder_edit_key,value_or_fn);
} else {
return cljs.core.assoc_in(db,reminder_edit_key,cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([old_reminder_edit_data,value_or_fn], 0)));
}
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("save-reminder","error","save-reminder/error",-1211089034),(function (db,p__38732){
var vec__38733 = p__38732;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38733,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38733,(1),null);
var reminder_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38733,(2),null);
var old_reminders_data = oc.web.dispatcher.reminders_data.cljs$core$IFn$_invoke$arity$2(org_slug,db);
var filtered_reminders = cljs.core.filterv((function (p1__38731_SHARP_){
return (new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__38731_SHARP_) == null);
}),new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(old_reminders_data));
var new_reminders_data = oc.web.utils.reminder.sort_reminders(filtered_reminders);
var reminders_list_key = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.reminders_data_key(org_slug),new cljs.core.Keyword(null,"items","items",1031954938));
return cljs.core.assoc_in(cljs.core.assoc_in(db,reminders_list_key,new_reminders_data),oc.web.dispatcher.reminder_edit_key(org_slug),reminder_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"save-reminder","save-reminder",605492069),(function (db,p__38737){
var vec__38738 = p__38737;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38738,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38738,(1),null);
var reminder_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38738,(2),null);
var fixed_reminder_data = (cljs.core.truth_(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(reminder_data))?reminder_data:cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(reminder_data,new cljs.core.Keyword(null,"uuid","uuid",-2145095719),oc.web.lib.utils.activity_uuid()));
var old_reminders_data = oc.web.dispatcher.reminders_data.cljs$core$IFn$_invoke$arity$2(org_slug,db);
var filtered_reminders = cljs.core.filterv((function (p1__38736_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(reminder_data),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__38736_SHARP_));
}),new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(old_reminders_data));
var new_reminders_data = oc.web.utils.reminder.sort_reminders(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(filtered_reminders,fixed_reminder_data));
var reminders_list_key = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.reminders_data_key(org_slug),new cljs.core.Keyword(null,"items","items",1031954938));
return cljs.core.assoc_in(cljs.core.assoc_in(db,reminders_list_key,new_reminders_data),oc.web.dispatcher.reminder_edit_key(org_slug),null);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"cancel-edit-reminder","cancel-edit-reminder",-1112196612),(function (db,p__38741){
var vec__38742 = p__38741;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38742,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38742,(1),null);
return cljs.core.assoc_in(db,oc.web.dispatcher.reminder_edit_key(org_slug),null);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"delete-reminder","delete-reminder",788982201),(function (db,p__38746){
var vec__38747 = p__38746;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38747,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38747,(1),null);
var reminder_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38747,(2),null);
var reminders_key = oc.web.dispatcher.reminders_data_key(org_slug);
var old_reminders_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,reminders_key);
var reminders_items_key = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(reminders_key,new cljs.core.Keyword(null,"items","items",1031954938));
var filtered_reminders = oc.web.utils.reminder.sort_reminders(cljs.core.filterv((function (p1__38745_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__38745_SHARP_),reminder_uuid);
}),new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(old_reminders_data)));
return cljs.core.assoc_in(cljs.core.assoc_in(db,reminders_items_key,filtered_reminders),oc.web.dispatcher.reminder_edit_key(org_slug),null);
}));

//# sourceMappingURL=oc.web.stores.reminder.js.map
