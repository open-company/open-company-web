goog.provide('oc.web.components.edit_recurring_update_modal');
oc.web.components.edit_recurring_update_modal.cancel_clicked = (function oc$web$components$edit_recurring_update_modal$cancel_clicked(reminder_data,dismiss_action){
if(cljs.core.truth_(new cljs.core.Keyword(null,"has-changes","has-changes",-631476764).cljs$core$IFn$_invoke$arity$1(reminder_data))){
var alert_data = new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"icon","icon",1679606541),"/img/ML/trash.svg",new cljs.core.Keyword(null,"action","action",-811238024),"reminders-unsaved-edits",new cljs.core.Keyword(null,"message","message",-406056002),"Leave without saving your changes?",new cljs.core.Keyword(null,"link-button-title","link-button-title",-812305976),"Stay",new cljs.core.Keyword(null,"link-button-cb","link-button-cb",559710301),(function (){
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
oc.web.components.edit_recurring_update_modal.update_reminder = (function oc$web$components$edit_recurring_update_modal$update_reminder(s,v){
var reminder_data = cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s));
return oc.web.actions.reminder.update_reminder(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(reminder_data),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([v,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"has-changes","has-changes",-631476764),true], null)], 0)));
});
oc.web.components.edit_recurring_update_modal.delete_reminder_clicked = (function oc$web$components$edit_recurring_update_modal$delete_reminder_clicked(s){
var reminder_data = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"reminder-edit","reminder-edit",1168054794)));
var alert_data = new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"icon","icon",1679606541),"/img/ML/trash.svg",new cljs.core.Keyword(null,"action","action",-811238024),"reminder-delete",new cljs.core.Keyword(null,"message","message",-406056002),"Delete this reminder?",new cljs.core.Keyword(null,"link-button-title","link-button-title",-812305976),"No",new cljs.core.Keyword(null,"link-button-cb","link-button-cb",559710301),(function (){
return oc.web.components.ui.alert_modal.hide_alert();
}),new cljs.core.Keyword(null,"solid-button-style","solid-button-style",-723792244),new cljs.core.Keyword(null,"red","red",-969428204),new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"Yes",new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),(function (){
oc.web.components.ui.alert_modal.hide_alert();

return oc.web.actions.reminder.delete_reminder(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(reminder_data));
})], null);
return oc.web.components.ui.alert_modal.show_alert(alert_data);
});
oc.web.components.edit_recurring_update_modal.edit_recurring_update_modal = rum.core.build_defcs((function (s){
var reminder_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"reminder-edit","reminder-edit",1168054794));
var reminders_roster = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"reminders-roster","reminders-roster",507031882));
var users_list = new cljs.core.Keyword(null,"users-list","users-list",1881920809).cljs$core$IFn$_invoke$arity$1(reminders_roster);
var on_label = (function (){var G__46870 = new cljs.core.Keyword(null,"frequency","frequency",-1408891382).cljs$core$IFn$_invoke$arity$1(reminder_data);
var G__46870__$1 = (((G__46870 instanceof cljs.core.Keyword))?G__46870.fqn:null);
switch (G__46870__$1) {
case "weekly":
case "biweekly":
return "On";

break;
default:
return "On the";

}
})();
var occurrence_field_name = cljs.core.get.cljs$core$IFn$_invoke$arity$2(oc.web.utils.reminder.occurrence_fields,new cljs.core.Keyword(null,"frequency","frequency",-1408891382).cljs$core$IFn$_invoke$arity$1(reminder_data));
var possible_values = cljs.core.get.cljs$core$IFn$_invoke$arity$2(oc.web.utils.reminder.occurrence_values,new cljs.core.Keyword(null,"frequency","frequency",-1408891382).cljs$core$IFn$_invoke$arity$1(reminder_data));
var values = cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p__46871){
var vec__46872 = p__46871;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__46872,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__46872,(1),null);
return cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"value","value",305978217),new cljs.core.Keyword(null,"label","label",1718410804)],[k,v]);
}),possible_values);
var occurrence_field = new cljs.core.Keyword(null,"occurrence-label","occurrence-label",-716121971).cljs$core$IFn$_invoke$arity$1(reminder_data);
var occurrence_field_value = cljs.core.get.cljs$core$IFn$_invoke$arity$2(reminder_data,occurrence_field);
var occurrence_label_value = new cljs.core.Keyword(null,"occurrence-value","occurrence-value",-886146126).cljs$core$IFn$_invoke$arity$1(reminder_data);
var self_assignee_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.lib.jwt.user_id(),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"assignee","assignee",-1242457026).cljs$core$IFn$_invoke$arity$1(reminder_data)));
return React.createElement("div",({"className": "edit-recurring-update-modal-container"}),React.createElement("button",({"onClick": (function (_){
return oc.web.components.edit_recurring_update_modal.cancel_clicked(reminder_data,oc.web.actions.nav_sidebar.close_all_panels);
}), "className": "mlb-reset modal-close-bt"})),React.createElement("div",({"className": "edit-recurring-update-modal"}),React.createElement("div",({"className": "edit-recurring-update-modal-header"}),React.createElement("div",({"className": "edit-recurring-update-modal-header-title"}),"Recurring updates"),(function (){var save_disabled_QMARK_ = ((clojure.string.blank_QMARK_(new cljs.core.Keyword(null,"headline","headline",-157157727).cljs$core$IFn$_invoke$arity$1(reminder_data))) || (cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"assignee","assignee",-1242457026).cljs$core$IFn$_invoke$arity$1(reminder_data))) || (cljs.core.not(new cljs.core.Keyword(null,"frequency","frequency",-1408891382).cljs$core$IFn$_invoke$arity$1(reminder_data))) || (cljs.core.not(new cljs.core.Keyword(null,"occurrence-label","occurrence-label",-716121971).cljs$core$IFn$_invoke$arity$1(reminder_data))) || (cljs.core.not(cljs.core.get.cljs$core$IFn$_invoke$arity$2(reminder_data,new cljs.core.Keyword(null,"occurrence-label","occurrence-label",-716121971).cljs$core$IFn$_invoke$arity$1(reminder_data)))));
return React.createElement("button",({"onClick": (function (){
if(save_disabled_QMARK_){
return null;
} else {
oc.web.actions.reminder.save_reminder(reminder_data);

return oc.web.actions.nav_sidebar.close_reminders();
}
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["mlb-reset","save-bt",((save_disabled_QMARK_)?"disabled":null)], null))}),"Save");
})(),React.createElement("button",({"onClick": (function (_){
return oc.web.components.edit_recurring_update_modal.cancel_clicked(reminder_data,(function (){
return oc.web.actions.reminder.cancel_edit_reminder();
}));
}), "className": "mlb-reset cancel-bt"}),"Back")),React.createElement("div",({"className": "edit-recurring-update-body"}),React.createElement("div",({"className": "field-label"}),"Assign to"),((cljs.core.empty_QMARK_(users_list))?(function (){var attrs46885 = (oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.small_loading.small_loading.call(null));
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46885))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["loading-users"], null)], null),attrs46885], 0))):({"className": "loading-users"})),((cljs.core.map_QMARK_(attrs46885))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46885)], null)));
})():React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [((cljs.core.empty_QMARK_(users_list))?"loading-users":null)], null))}),React.createElement("div",({"ref": "assignee-bt", "onClick": (function (){
if(cljs.core.empty_QMARK_(users_list)){
return null;
} else {
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("oc.web.components.edit-recurring-update-modal","assignee-dropdown","oc.web.components.edit-recurring-update-modal/assignee-dropdown",955450050).cljs$core$IFn$_invoke$arity$1(s),cljs.core.not);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.edit-recurring-update-modal","frequency-dropdown","oc.web.components.edit-recurring-update-modal/frequency-dropdown",-1230753227).cljs$core$IFn$_invoke$arity$1(s),false);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.edit-recurring-update-modal","on-dropdown","oc.web.components.edit-recurring-update-modal/on-dropdown",-1174273227).cljs$core$IFn$_invoke$arity$1(s),false);
}
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["field-value","dropdown-field-value","oc-input",oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"placeholder","placeholder",-104873083),cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"assignee","assignee",-1242457026).cljs$core$IFn$_invoke$arity$1(reminder_data)),new cljs.core.Keyword(null,"active","active",1895962068),cljs.core.deref(new cljs.core.Keyword("oc.web.components.edit-recurring-update-modal","assignee-dropdown","oc.web.components.edit-recurring-update-modal/assignee-dropdown",955450050).cljs$core$IFn$_invoke$arity$1(s))], null))], null))}),(cljs.core.truth_(new cljs.core.Keyword(null,"assignee","assignee",-1242457026).cljs$core$IFn$_invoke$arity$1(reminder_data))?sablono.interpreter.interpret([cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.lib.user.name_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"assignee","assignee",-1242457026).cljs$core$IFn$_invoke$arity$1(reminder_data)], 0))),((self_assignee_QMARK_)?" (you)":null)].join('')):"Pick a user")),sablono.interpreter.interpret(((cljs.core.empty_QMARK_(users_list))?(oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.small_loading.small_loading.call(null)):null)),sablono.interpreter.interpret((cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.edit-recurring-update-modal","assignee-dropdown","oc.web.components.edit-recurring-update-modal/assignee-dropdown",955450050).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.dropdown-container.users-list","div.dropdown-container.users-list",-978409704),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ref","ref",1289896967),new cljs.core.Keyword(null,"assignee-dd-node","assignee-dd-node",-1390840894)], null),(function (){var G__46886 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"items","items",1031954938),users_list,new cljs.core.Keyword(null,"value","value",305978217),(function (){var or__4126__auto__ = new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"assignee","assignee",-1242457026).cljs$core$IFn$_invoke$arity$1(reminder_data));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"value","value",305978217).cljs$core$IFn$_invoke$arity$1(cljs.core.first(users_list));
}
})(),new cljs.core.Keyword(null,"on-change","on-change",-732046149),(function (item){
var selected_user_46909 = cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__46869_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__46869_SHARP_),new cljs.core.Keyword(null,"value","value",305978217).cljs$core$IFn$_invoke$arity$1(item));
}),new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(reminders_roster)));
oc.web.components.edit_recurring_update_modal.update_reminder(s,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"assignee","assignee",-1242457026),selected_user_46909], null));

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.edit-recurring-update-modal","assignee-dropdown","oc.web.components.edit-recurring-update-modal/assignee-dropdown",955450050).cljs$core$IFn$_invoke$arity$1(s),false);
})], null);
return (oc.web.components.ui.dropdown_list.dropdown_list.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.dropdown_list.dropdown_list.cljs$core$IFn$_invoke$arity$1(G__46886) : oc.web.components.ui.dropdown_list.dropdown_list.call(null,G__46886));
})()], null):null)))),React.createElement("div",({"className": "field-label"}),"To update the team about"),sablono.interpreter.create_element("input",({"value": (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"headline","headline",-157157727).cljs$core$IFn$_invoke$arity$1(reminder_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "";
}
})(), "ref": "reminder-title", "type": "text", "maxLength": (65), "placeholder": "CEO update, Week in review, etc.", "onChange": (function (){
return oc.web.components.edit_recurring_update_modal.update_reminder(s,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headline","headline",-157157727),clojure.string.trim(rum.core.ref_node(s,new cljs.core.Keyword(null,"reminder-title","reminder-title",-149679736)).value)], null));
}), "className": "field-value oc-input"})),React.createElement("div",({"className": "field-label"}),"Every"),(function (){var frequency_value = cljs.core.get.cljs$core$IFn$_invoke$arity$2(oc.web.utils.reminder.frequency_values,new cljs.core.Keyword(null,"frequency","frequency",-1408891382).cljs$core$IFn$_invoke$arity$1(reminder_data));
return React.createElement("div",null,React.createElement("div",({"ref": "frequency-bt", "onClick": (function (){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.edit-recurring-update-modal","assignee-dropdown","oc.web.components.edit-recurring-update-modal/assignee-dropdown",955450050).cljs$core$IFn$_invoke$arity$1(s),false);

cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("oc.web.components.edit-recurring-update-modal","frequency-dropdown","oc.web.components.edit-recurring-update-modal/frequency-dropdown",-1230753227).cljs$core$IFn$_invoke$arity$1(s),cljs.core.not);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.edit-recurring-update-modal","on-dropdown","oc.web.components.edit-recurring-update-modal/on-dropdown",-1174273227).cljs$core$IFn$_invoke$arity$1(s),false);
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["field-value","dropdown-field-value","oc-input",oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"placeholder","placeholder",-104873083),cljs.core.empty_QMARK_(frequency_value),new cljs.core.Keyword(null,"active","active",1895962068),cljs.core.deref(new cljs.core.Keyword("oc.web.components.edit-recurring-update-modal","frequency-dropdown","oc.web.components.edit-recurring-update-modal/frequency-dropdown",-1230753227).cljs$core$IFn$_invoke$arity$1(s))], null))], null))}),sablono.interpreter.interpret((function (){var or__4126__auto__ = frequency_value;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "Pick a frequency";
}
})())),sablono.interpreter.interpret((cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.edit-recurring-update-modal","frequency-dropdown","oc.web.components.edit-recurring-update-modal/frequency-dropdown",-1230753227).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.dropdown-container","div.dropdown-container",1041685958),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ref","ref",1289896967),new cljs.core.Keyword(null,"frequency-dd-node","frequency-dd-node",-381175698)], null),(function (){var G__46890 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"items","items",1031954938),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"value","value",305978217),new cljs.core.Keyword(null,"weekly","weekly",319200344),new cljs.core.Keyword(null,"label","label",1718410804),new cljs.core.Keyword(null,"weekly","weekly",319200344).cljs$core$IFn$_invoke$arity$1(oc.web.utils.reminder.frequency_values),new cljs.core.Keyword(null,"occurrence-value","occurrence-value",-886146126),new cljs.core.Keyword(null,"friday","friday",459046165)], null),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"value","value",305978217),new cljs.core.Keyword(null,"biweekly","biweekly",2110558214),new cljs.core.Keyword(null,"label","label",1718410804),new cljs.core.Keyword(null,"biweekly","biweekly",2110558214).cljs$core$IFn$_invoke$arity$1(oc.web.utils.reminder.frequency_values),new cljs.core.Keyword(null,"occurrence-value","occurrence-value",-886146126),new cljs.core.Keyword(null,"friday","friday",459046165)], null),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"value","value",305978217),new cljs.core.Keyword(null,"monthly","monthly",1596693261),new cljs.core.Keyword(null,"label","label",1718410804),new cljs.core.Keyword(null,"monthly","monthly",1596693261).cljs$core$IFn$_invoke$arity$1(oc.web.utils.reminder.frequency_values),new cljs.core.Keyword(null,"occurrence-value","occurrence-value",-886146126),new cljs.core.Keyword(null,"first","first",-644103046)], null),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"value","value",305978217),new cljs.core.Keyword(null,"quarterly","quarterly",1061741469),new cljs.core.Keyword(null,"label","label",1718410804),new cljs.core.Keyword(null,"quarterly","quarterly",1061741469).cljs$core$IFn$_invoke$arity$1(oc.web.utils.reminder.frequency_values),new cljs.core.Keyword(null,"occurrence-value","occurrence-value",-886146126),new cljs.core.Keyword(null,"first","first",-644103046)], null)], null),new cljs.core.Keyword(null,"value","value",305978217),new cljs.core.Keyword(null,"frequency","frequency",-1408891382).cljs$core$IFn$_invoke$arity$1(reminder_data),new cljs.core.Keyword(null,"on-change","on-change",-732046149),(function (item){
var old_freq_46911 = new cljs.core.Keyword(null,"frequency","frequency",-1408891382).cljs$core$IFn$_invoke$arity$1(reminder_data);
var new_freq_46912 = new cljs.core.Keyword(null,"value","value",305978217).cljs$core$IFn$_invoke$arity$1(item);
var with_freq_46913 = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"frequency","frequency",-1408891382),new_freq_46912], null);
var occurrence_field_name_46914__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(oc.web.utils.reminder.occurrence_fields,new cljs.core.Keyword(null,"value","value",305978217).cljs$core$IFn$_invoke$arity$1(item));
var should_update_occurrence_46915 = ((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new_freq_46912,old_freq_46911)) && (((cljs.core.not((function (){var fexpr__46901 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"biweekly","biweekly",2110558214),null,new cljs.core.Keyword(null,"weekly","weekly",319200344),null], null), null);
return (fexpr__46901.cljs$core$IFn$_invoke$arity$1 ? fexpr__46901.cljs$core$IFn$_invoke$arity$1(new_freq_46912) : fexpr__46901.call(null,new_freq_46912));
})())) || (cljs.core.not((function (){var fexpr__46902 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"biweekly","biweekly",2110558214),null,new cljs.core.Keyword(null,"weekly","weekly",319200344),null], null), null);
return (fexpr__46902.cljs$core$IFn$_invoke$arity$1 ? fexpr__46902.cljs$core$IFn$_invoke$arity$1(old_freq_46911) : fexpr__46902.call(null,old_freq_46911));
})())))));
var occurrence_value_46916 = ((should_update_occurrence_46915)?cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(oc.web.utils.reminder.occurrence_values,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"value","value",305978217).cljs$core$IFn$_invoke$arity$1(item),new cljs.core.Keyword(null,"occurrence-value","occurrence-value",-886146126).cljs$core$IFn$_invoke$arity$1(item)], null)):null);
var with_occurrence_46917 = ((should_update_occurrence_46915)?cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([with_freq_46913,cljs.core.PersistentArrayMap.createAsIfByAssoc([new cljs.core.Keyword(null,"occurrence-label","occurrence-label",-716121971),occurrence_field_name_46914__$1,occurrence_field_name_46914__$1,new cljs.core.Keyword(null,"occurrence-value","occurrence-value",-886146126).cljs$core$IFn$_invoke$arity$1(item)])], 0)):with_freq_46913);
var with_occurrence_value_46918 = ((should_update_occurrence_46915)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(with_occurrence_46917,new cljs.core.Keyword(null,"occurrence-value","occurrence-value",-886146126),occurrence_value_46916):with_occurrence_46917);
oc.web.components.edit_recurring_update_modal.update_reminder(s,with_occurrence_value_46918);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.edit-recurring-update-modal","frequency-dropdown","oc.web.components.edit-recurring-update-modal/frequency-dropdown",-1230753227).cljs$core$IFn$_invoke$arity$1(s),false);
})], null);
return (oc.web.components.ui.dropdown_list.dropdown_list.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.dropdown_list.dropdown_list.cljs$core$IFn$_invoke$arity$1(G__46890) : oc.web.components.ui.dropdown_list.dropdown_list.call(null,G__46890));
})()], null):null)));
})(),(function (){var attrs46884 = on_label;
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46884))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["field-label"], null)], null),attrs46884], 0))):({"className": "field-label"})),((cljs.core.map_QMARK_(attrs46884))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46884)], null)));
})(),React.createElement("div",({"ref": "on-bt", "onClick": (function (){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.edit-recurring-update-modal","assignee-dropdown","oc.web.components.edit-recurring-update-modal/assignee-dropdown",955450050).cljs$core$IFn$_invoke$arity$1(s),false);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.edit-recurring-update-modal","frequency-dropdown","oc.web.components.edit-recurring-update-modal/frequency-dropdown",-1230753227).cljs$core$IFn$_invoke$arity$1(s),false);

return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("oc.web.components.edit-recurring-update-modal","on-dropdown","oc.web.components.edit-recurring-update-modal/on-dropdown",-1174273227).cljs$core$IFn$_invoke$arity$1(s),cljs.core.not);
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["field-value","dropdown-field-value","oc-input",(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.edit-recurring-update-modal","on-dropdown","oc.web.components.edit-recurring-update-modal/on-dropdown",-1174273227).cljs$core$IFn$_invoke$arity$1(s)))?"active":null)], null))}),sablono.interpreter.interpret(occurrence_label_value)),sablono.interpreter.interpret((cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.edit-recurring-update-modal","on-dropdown","oc.web.components.edit-recurring-update-modal/on-dropdown",-1174273227).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.dropdown-container","div.dropdown-container",1041685958),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ref","ref",1289896967),new cljs.core.Keyword(null,"on-dd-node","on-dd-node",2009656244)], null),(function (){var G__46903 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"items","items",1031954938),values,new cljs.core.Keyword(null,"value","value",305978217),occurrence_field_value,new cljs.core.Keyword(null,"on-change","on-change",-732046149),(function (item){
oc.web.components.edit_recurring_update_modal.update_reminder(s,cljs.core.PersistentArrayMap.createAsIfByAssoc([occurrence_field_name,new cljs.core.Keyword(null,"value","value",305978217).cljs$core$IFn$_invoke$arity$1(item),new cljs.core.Keyword(null,"occurrence-value","occurrence-value",-886146126),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(oc.web.utils.reminder.occurrence_values,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"frequency","frequency",-1408891382).cljs$core$IFn$_invoke$arity$1(reminder_data),new cljs.core.Keyword(null,"value","value",305978217).cljs$core$IFn$_invoke$arity$1(item)], null))]));

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.edit-recurring-update-modal","on-dropdown","oc.web.components.edit-recurring-update-modal/on-dropdown",-1174273227).cljs$core$IFn$_invoke$arity$1(s),false);
})], null);
return (oc.web.components.ui.dropdown_list.dropdown_list.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.dropdown_list.dropdown_list.cljs$core$IFn$_invoke$arity$1(G__46903) : oc.web.components.ui.dropdown_list.dropdown_list.call(null,G__46903));
})()], null):null)),sablono.interpreter.interpret((cljs.core.truth_(oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(reminder_data),"delete"], 0)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.edit-recurring-update-footer.group","div.edit-recurring-update-footer.group",-1584013182),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.delete-bt","button.mlb-reset.delete-bt",-168688558),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.components.edit_recurring_update_modal.delete_reminder_clicked(s);
})], null),"Delete recurring update"], null)], null):null)))));
}),new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"org-data","org-data",96720321)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"reminder-edit","reminder-edit",1168054794)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"reminders-roster","reminders-roster",507031882)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.edit-recurring-update-modal","assignee-dropdown","oc.web.components.edit-recurring-update-modal/assignee-dropdown",955450050)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.edit-recurring-update-modal","frequency-dropdown","oc.web.components.edit-recurring-update-modal/frequency-dropdown",-1230753227)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.edit-recurring-update-modal","on-dropdown","oc.web.components.edit-recurring-update-modal/on-dropdown",-1174273227)),oc.web.mixins.ui.on_window_click_mixin((function (s,e){
if(((cljs.core.not(oc.web.lib.utils.event_inside_QMARK_(e,rum.core.ref_node(s,new cljs.core.Keyword(null,"frequency-dd-node","frequency-dd-node",-381175698))))) && (cljs.core.not(oc.web.lib.utils.event_inside_QMARK_(e,rum.core.ref_node(s,new cljs.core.Keyword(null,"frequency-bt","frequency-bt",-2039153907))))) && (cljs.core.not(oc.web.lib.utils.event_inside_QMARK_(e,rum.core.ref_node(s,new cljs.core.Keyword(null,"on-dd-node","on-dd-node",2009656244))))) && (cljs.core.not(oc.web.lib.utils.event_inside_QMARK_(e,rum.core.ref_node(s,new cljs.core.Keyword(null,"on-bt","on-bt",1100462919))))))){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.edit-recurring-update-modal","frequency-dropdown","oc.web.components.edit-recurring-update-modal/frequency-dropdown",-1230753227).cljs$core$IFn$_invoke$arity$1(s),false);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.edit-recurring-update-modal","on-dropdown","oc.web.components.edit-recurring-update-modal/on-dropdown",-1174273227).cljs$core$IFn$_invoke$arity$1(s),false);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.edit-recurring-update-modal","assignee-dropdown","oc.web.components.edit-recurring-update-modal/assignee-dropdown",955450050).cljs$core$IFn$_invoke$arity$1(s),false);
} else {
return null;
}
}))], null),"edit-recurring-update-modal");

//# sourceMappingURL=oc.web.components.edit_recurring_update_modal.js.map
