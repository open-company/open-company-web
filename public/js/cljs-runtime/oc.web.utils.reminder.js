goog.provide('oc.web.utils.reminder');
oc.web.utils.reminder.occurrence_values = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"weekly","weekly",319200344),new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"monday","monday",-1107743655),"Monday",new cljs.core.Keyword(null,"tuesday","tuesday",299624080),"Tuesday",new cljs.core.Keyword(null,"wednesday","wednesday",-2061677647),"Wednesday",new cljs.core.Keyword(null,"thursday","thursday",1681780767),"Thursday",new cljs.core.Keyword(null,"friday","friday",459046165),"Friday",new cljs.core.Keyword(null,"saturday","saturday",-1342278228),"Saturday",new cljs.core.Keyword(null,"sunday","sunday",1381770036),"Sunday"], null),new cljs.core.Keyword(null,"biweekly","biweekly",2110558214),new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"monday","monday",-1107743655),"Monday",new cljs.core.Keyword(null,"tuesday","tuesday",299624080),"Tuesday",new cljs.core.Keyword(null,"wednesday","wednesday",-2061677647),"Wednesday",new cljs.core.Keyword(null,"thursday","thursday",1681780767),"Thursday",new cljs.core.Keyword(null,"friday","friday",459046165),"Friday",new cljs.core.Keyword(null,"saturday","saturday",-1342278228),"Saturday",new cljs.core.Keyword(null,"sunday","sunday",1381770036),"Sunday"], null),new cljs.core.Keyword(null,"monthly","monthly",1596693261),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"first","first",-644103046),"First day of the month",new cljs.core.Keyword(null,"first-monday","first-monday",1736857060),"First Monday of the month",new cljs.core.Keyword(null,"last-friday","last-friday",-676954451),"Last Friday of the month",new cljs.core.Keyword(null,"last","last",1105735132),"Last day of the month"], null),new cljs.core.Keyword(null,"quarterly","quarterly",1061741469),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"first","first",-644103046),"First day of the quarter",new cljs.core.Keyword(null,"first-monday","first-monday",1736857060),"First Monday of the quarter",new cljs.core.Keyword(null,"last-friday","last-friday",-676954451),"Last Friday of the quarter",new cljs.core.Keyword(null,"last","last",1105735132),"Last day of the quarter"], null)], null);
oc.web.utils.reminder.frequency_values = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"weekly","weekly",319200344),"Week",new cljs.core.Keyword(null,"biweekly","biweekly",2110558214),"Two weeks",new cljs.core.Keyword(null,"monthly","monthly",1596693261),"Month",new cljs.core.Keyword(null,"quarterly","quarterly",1061741469),"Quarter"], null);
oc.web.utils.reminder.occurrence_fields = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"weekly","weekly",319200344),new cljs.core.Keyword(null,"week-occurrence","week-occurrence",-1563004993),new cljs.core.Keyword(null,"biweekly","biweekly",2110558214),new cljs.core.Keyword(null,"week-occurrence","week-occurrence",-1563004993),new cljs.core.Keyword(null,"monthly","monthly",1596693261),new cljs.core.Keyword(null,"period-occurrence","period-occurrence",-953948637),new cljs.core.Keyword(null,"quarterly","quarterly",1061741469),new cljs.core.Keyword(null,"period-occurrence","period-occurrence",-953948637)], null);
oc.web.utils.reminder.short_assignee_name = (function oc$web$utils$reminder$short_assignee_name(assignee){
if((((!(clojure.string.blank_QMARK_(new cljs.core.Keyword(null,"first-name","first-name",-1559982131).cljs$core$IFn$_invoke$arity$1(assignee))))) && ((!(clojure.string.blank_QMARK_(new cljs.core.Keyword(null,"last-name","last-name",-1695738974).cljs$core$IFn$_invoke$arity$1(assignee))))))){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"first-name","first-name",-1559982131).cljs$core$IFn$_invoke$arity$1(assignee))," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword(null,"last-name","last-name",-1695738974).cljs$core$IFn$_invoke$arity$1(assignee))),"."].join('');
} else {
var splitted_name = clojure.string.split.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(assignee),/\s/);
var has_last_name_QMARK_ = (!(clojure.string.blank_QMARK_(cljs.core.get.cljs$core$IFn$_invoke$arity$2(splitted_name,(1)))));
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.first(splitted_name)),((has_last_name_QMARK_)?" ":null),cljs.core.str.cljs$core$IFn$_invoke$arity$1(((has_last_name_QMARK_)?cljs.core.first(cljs.core.second(splitted_name)):null)),((has_last_name_QMARK_)?".":null)].join('');
}
});
/**
 * Given the map of a reminder denormalize it with the assignee and author map.
 */
oc.web.utils.reminder.parse_reminder = (function oc$web$utils$reminder$parse_reminder(reminder_data){
var js_date = oc.web.lib.utils.js_date.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"next-send","next-send",-245355311).cljs$core$IFn$_invoke$arity$1(reminder_data)], 0));
var now_year = oc.web.lib.utils.js_date().getFullYear();
var show_year_QMARK_ = cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(js_date.getFullYear(),now_year);
var parsed_date = oc.web.lib.utils.date_string.cljs$core$IFn$_invoke$arity$variadic(js_date,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"short","short",1928760516),((show_year_QMARK_)?new cljs.core.Keyword(null,"year","year",335913393):null)], null)], 0));
var with_parsed_date = (cljs.core.truth_(new cljs.core.Keyword(null,"next-send","next-send",-245355311).cljs$core$IFn$_invoke$arity$1(reminder_data))?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(reminder_data,new cljs.core.Keyword(null,"parsed-next-send","parsed-next-send",-451157145),parsed_date):reminder_data);
var frequency_kw = cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"frequency","frequency",-1408891382).cljs$core$IFn$_invoke$arity$1(reminder_data));
var occurrence_field = cljs.core.get.cljs$core$IFn$_invoke$arity$2(oc.web.utils.reminder.occurrence_fields,frequency_kw);
var occurrence_value = cljs.core.get.cljs$core$IFn$_invoke$arity$2(reminder_data,occurrence_field);
var occurrence_value_kw = cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(occurrence_value);
var occurrence_value__$1 = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(oc.web.utils.reminder.occurrence_values,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [frequency_kw,occurrence_value_kw], null));
var assignee_map = new cljs.core.Keyword(null,"assignee","assignee",-1242457026).cljs$core$IFn$_invoke$arity$1(reminder_data);
var assignee_name = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(assignee_map);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.lib.user.name_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([assignee_map], 0));
}
})();
var short_assignee = oc.web.utils.reminder.short_assignee_name(assignee_map);
return cljs.core.assoc_in(cljs.core.assoc_in(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(with_parsed_date,new cljs.core.Keyword(null,"frequency","frequency",-1408891382),frequency_kw),new cljs.core.Keyword(null,"occurrence-label","occurrence-label",-716121971),occurrence_field),occurrence_field,occurrence_value_kw),new cljs.core.Keyword(null,"occurrence-value","occurrence-value",-886146126),occurrence_value__$1),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"assignee","assignee",-1242457026),new cljs.core.Keyword(null,"name","name",1843675177)], null),assignee_name),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"assignee","assignee",-1242457026),new cljs.core.Keyword(null,"short-name","short-name",-1767085022)], null),short_assignee);
});
oc.web.utils.reminder.parse_reminders = (function oc$web$utils$reminder$parse_reminders(reminders_data){
var parsed_reminders = cljs.core.vec(cljs.core.map.cljs$core$IFn$_invoke$arity$2(oc.web.utils.reminder.parse_reminder,new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(reminders_data)));
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(reminders_data,new cljs.core.Keyword(null,"items","items",1031954938),parsed_reminders);
});
oc.web.utils.reminder.new_reminder_data = (function oc$web$utils$reminder$new_reminder_data(){
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
var current_user_data = oc.web.dispatcher.current_user_data.cljs$core$IFn$_invoke$arity$0();
return cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"headline","headline",-157157727),new cljs.core.Keyword(null,"last-sent","last-sent",-1752565464),new cljs.core.Keyword(null,"assignee-tz","assignee-tz",-147568502),new cljs.core.Keyword(null,"frequency","frequency",-1408891382),new cljs.core.Keyword(null,"author","author",2111686192),new cljs.core.Keyword(null,"next-send","next-send",-245355311),new cljs.core.Keyword(null,"org-uuid","org-uuid",1539092089),new cljs.core.Keyword(null,"assignee","assignee",-1242457026),new cljs.core.Keyword(null,"week-occurrence","week-occurrence",-1563004993)],["",null,new cljs.core.Keyword(null,"timezone","timezone",1831928099).cljs$core$IFn$_invoke$arity$1(current_user_data),"weekly",cljs.core.select_keys(current_user_data,new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"user-id","user-id",-206822291),new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103),new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"first-name","first-name",-1559982131),new cljs.core.Keyword(null,"last-name","last-name",-1695738974)], null)),null,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(org_data),cljs.core.select_keys(current_user_data,new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"user-id","user-id",-206822291),new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103),new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"first-name","first-name",-1559982131),new cljs.core.Keyword(null,"last-name","last-name",-1695738974)], null)),"friday"]);
});
oc.web.utils.reminder.users_for_reminders = (function oc$web$utils$reminder$users_for_reminders(roster_data){
var fixed_roster = cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__44006_SHARP_){
var status = new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(p1__44006_SHARP_);
var tooltip = (function (){var G__44009 = status;
switch (G__44009) {
case "pending":
case "unverified":
return "This user has an unverified email";

break;
default:
return null;

}
})();
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__44006_SHARP_,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"disabled","disabled",-1529784218),cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(status,"active"),new cljs.core.Keyword(null,"tooltip","tooltip",-1809677058),tooltip], null)], 0));
}),new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(roster_data));
var users_list = cljs.core.vec(cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__44007_SHARP_){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(clojure.set.rename_keys(cljs.core.select_keys(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__44007_SHARP_,new cljs.core.Keyword(null,"name","name",1843675177),[cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.lib.user.name_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__44007_SHARP_], 0))),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__44007_SHARP_),oc.web.lib.jwt.user_id()))?" (you)":null)].join('')),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"user-id","user-id",-206822291),new cljs.core.Keyword(null,"disabled","disabled",-1529784218),new cljs.core.Keyword(null,"tooltip","tooltip",-1809677058)], null)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"label","label",1718410804),new cljs.core.Keyword(null,"user-id","user-id",-206822291),new cljs.core.Keyword(null,"value","value",305978217)], null)),new cljs.core.Keyword(null,"user-map","user-map",-1037585771),p1__44007_SHARP_);
}),fixed_roster));
var splitted = cljs.core.group_by((function (p1__44008_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"value","value",305978217).cljs$core$IFn$_invoke$arity$1(p1__44008_SHARP_),oc.web.lib.jwt.user_id());
}),users_list);
var self_user = cljs.core.first(cljs.core.get.cljs$core$IFn$_invoke$arity$2(splitted,true));
var without_user = cljs.core.get.cljs$core$IFn$_invoke$arity$2(splitted,false);
return cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [self_user], null),cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"label","label",1718410804),without_user));
});
oc.web.utils.reminder.sort_fn = (function oc$web$utils$reminder$sort_fn(reminder_a,reminder_b){
var assignee_compare = cljs.core.compare(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"assignee","assignee",-1242457026).cljs$core$IFn$_invoke$arity$1(reminder_a)),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"assignee","assignee",-1242457026).cljs$core$IFn$_invoke$arity$1(reminder_b)));
if((assignee_compare === (0))){
return cljs.core.compare(new cljs.core.Keyword(null,"headline","headline",-157157727).cljs$core$IFn$_invoke$arity$1(reminder_a),new cljs.core.Keyword(null,"headline","headline",-157157727).cljs$core$IFn$_invoke$arity$1(reminder_b));
} else {
return assignee_compare;
}
});
oc.web.utils.reminder.sort_reminders = (function oc$web$utils$reminder$sort_reminders(reminders_items){
return cljs.core.sort.cljs$core$IFn$_invoke$arity$2(oc.web.utils.reminder.sort_fn,reminders_items);
});
oc.web.utils.reminder.parse_reminders_roster = (function oc$web$utils$reminder$parse_reminders_roster(roster_data){
var collection = new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(roster_data);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(collection,new cljs.core.Keyword(null,"users-list","users-list",1881920809),oc.web.utils.reminder.users_for_reminders(collection));
});

//# sourceMappingURL=oc.web.utils.reminder.js.map
