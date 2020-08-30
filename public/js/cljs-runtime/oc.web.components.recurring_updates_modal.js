goog.provide('oc.web.components.recurring_updates_modal');
oc.web.components.recurring_updates_modal.recurring_updates_modal = rum.core.build_defcs((function (s){
var reminders_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"reminders-data","reminders-data",-1331370092));
var reminders_list = new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(reminders_data);
var is_tablet_or_mobile_QMARK_ = oc.web.lib.responsive.is_tablet_or_mobile_QMARK_();
return React.createElement("div",({"className": "recurring-updates-modal-container"}),React.createElement("button",({"onClick": oc.web.actions.nav_sidebar.close_all_panels, "className": "mlb-reset modal-close-bt"})),React.createElement("div",({"className": "recurring-updates-modal"}),React.createElement("div",({"className": "recurring-updates-modal-header"}),React.createElement("div",({"className": "recurring-updates-modal-header-title"}),"Recurring updates"),React.createElement("button",({"onClick": oc.web.actions.reminder.new_reminder, "className": "mlb-reset new-recurring-update-bt"}),"New"),React.createElement("button",({"onClick": oc.web.actions.nav_sidebar.close_reminders, "className": "mlb-reset cancel-bt"}),"Back")),((cljs.core.seq(reminders_list))?React.createElement("div",({"className": "recurring-updates-list"}),cljs.core.into_array.cljs$core$IFn$_invoke$arity$1((function (){var iter__4529__auto__ = (function oc$web$components$recurring_updates_modal$iter__40493(s__40494){
return (new cljs.core.LazySeq(null,(function (){
var s__40494__$1 = s__40494;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__40494__$1);
if(temp__5735__auto__){
var s__40494__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__40494__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__40494__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__40496 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__40495 = (0);
while(true){
if((i__40495 < size__4528__auto__)){
var reminder = cljs.core._nth(c__4527__auto__,i__40495);
var patch_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(reminder),"partial-update"], 0));
var now_year = oc.web.lib.utils.js_date().getFullYear();
var next_send_date = oc.web.lib.utils.js_date.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"next-send","next-send",-245355311).cljs$core$IFn$_invoke$arity$1(reminder)], 0));
var show_year = cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(now_year,next_send_date.getFullYear());
cljs.core.chunk_append(b__40496,React.createElement("div",({"key": ["reminder-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(reminder))].join(''), "onClick": ((function (i__40495,patch_link,now_year,next_send_date,show_year,reminder,c__4527__auto__,size__4528__auto__,b__40496,s__40494__$2,temp__5735__auto__,reminders_data,reminders_list,is_tablet_or_mobile_QMARK_){
return (function (){
if(cljs.core.truth_(patch_link)){
return oc.web.actions.reminder.edit_reminder(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(reminder));
} else {
return null;
}
});})(i__40495,patch_link,now_year,next_send_date,show_year,reminder,c__4527__auto__,size__4528__auto__,b__40496,s__40494__$2,temp__5735__auto__,reminders_data,reminders_list,is_tablet_or_mobile_QMARK_))
, "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["recurring-updates-list-item","group",(cljs.core.truth_(patch_link)?"editable-reminder":null)], null))}),React.createElement("div",({"title": oc.lib.user.name_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"assignee","assignee",-1242457026).cljs$core$IFn$_invoke$arity$1(reminder)], 0)), "data-toggle": ((is_tablet_or_mobile_QMARK_)?null:"tooltip"), "data-placement": "top", "data-container": "body", "data-delay": "{\"show\":\"500\", \"hide\":\"0\"}", "className": "reminder-assignee"}),sablono.interpreter.interpret((function (){var G__40502 = new cljs.core.Keyword(null,"assignee","assignee",-1242457026).cljs$core$IFn$_invoke$arity$1(reminder);
return (oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1(G__40502) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,G__40502));
})())),React.createElement("div",({"className": "reminder-data"}),(function (){var attrs40503 = new cljs.core.Keyword(null,"headline","headline",-157157727).cljs$core$IFn$_invoke$arity$1(reminder);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs40503))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["reminder-title"], null)], null),attrs40503], 0))):({"className": "reminder-title"})),((cljs.core.map_QMARK_(attrs40503))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs40503)], null)));
})(),(function (){var attrs40505 = [cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.lib.user.name_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"assignee","assignee",-1242457026).cljs$core$IFn$_invoke$arity$1(reminder)], 0))),", ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.get_week_day.cljs$core$IFn$_invoke$arity$variadic(next_send_date.getDay(),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true], 0)))," ",oc.web.lib.utils.date_string.cljs$core$IFn$_invoke$arity$variadic(next_send_date,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"short-month","short-month",863577052),((show_year)?new cljs.core.Keyword(null,"year","year",335913393):null)], null)], 0))," (",cljs.core.name(new cljs.core.Keyword(null,"frequency","frequency",-1408891382).cljs$core$IFn$_invoke$arity$1(reminder)),")"].join('');
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs40505))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["reminder-description"], null)], null),attrs40505], 0))):({"className": "reminder-description"})),((cljs.core.map_QMARK_(attrs40505))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs40505)], null)));
})())));

var G__40519 = (i__40495 + (1));
i__40495 = G__40519;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__40496),oc$web$components$recurring_updates_modal$iter__40493(cljs.core.chunk_rest(s__40494__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__40496),null);
}
} else {
var reminder = cljs.core.first(s__40494__$2);
var patch_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(reminder),"partial-update"], 0));
var now_year = oc.web.lib.utils.js_date().getFullYear();
var next_send_date = oc.web.lib.utils.js_date.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"next-send","next-send",-245355311).cljs$core$IFn$_invoke$arity$1(reminder)], 0));
var show_year = cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(now_year,next_send_date.getFullYear());
return cljs.core.cons(React.createElement("div",({"key": ["reminder-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(reminder))].join(''), "onClick": ((function (patch_link,now_year,next_send_date,show_year,reminder,s__40494__$2,temp__5735__auto__,reminders_data,reminders_list,is_tablet_or_mobile_QMARK_){
return (function (){
if(cljs.core.truth_(patch_link)){
return oc.web.actions.reminder.edit_reminder(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(reminder));
} else {
return null;
}
});})(patch_link,now_year,next_send_date,show_year,reminder,s__40494__$2,temp__5735__auto__,reminders_data,reminders_list,is_tablet_or_mobile_QMARK_))
, "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["recurring-updates-list-item","group",(cljs.core.truth_(patch_link)?"editable-reminder":null)], null))}),React.createElement("div",({"title": oc.lib.user.name_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"assignee","assignee",-1242457026).cljs$core$IFn$_invoke$arity$1(reminder)], 0)), "data-toggle": ((is_tablet_or_mobile_QMARK_)?null:"tooltip"), "data-placement": "top", "data-container": "body", "data-delay": "{\"show\":\"500\", \"hide\":\"0\"}", "className": "reminder-assignee"}),sablono.interpreter.interpret((function (){var G__40506 = new cljs.core.Keyword(null,"assignee","assignee",-1242457026).cljs$core$IFn$_invoke$arity$1(reminder);
return (oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1(G__40506) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,G__40506));
})())),React.createElement("div",({"className": "reminder-data"}),(function (){var attrs40503 = new cljs.core.Keyword(null,"headline","headline",-157157727).cljs$core$IFn$_invoke$arity$1(reminder);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs40503))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["reminder-title"], null)], null),attrs40503], 0))):({"className": "reminder-title"})),((cljs.core.map_QMARK_(attrs40503))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs40503)], null)));
})(),(function (){var attrs40505 = [cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.lib.user.name_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"assignee","assignee",-1242457026).cljs$core$IFn$_invoke$arity$1(reminder)], 0))),", ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.get_week_day.cljs$core$IFn$_invoke$arity$variadic(next_send_date.getDay(),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true], 0)))," ",oc.web.lib.utils.date_string.cljs$core$IFn$_invoke$arity$variadic(next_send_date,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"short-month","short-month",863577052),((show_year)?new cljs.core.Keyword(null,"year","year",335913393):null)], null)], 0))," (",cljs.core.name(new cljs.core.Keyword(null,"frequency","frequency",-1408891382).cljs$core$IFn$_invoke$arity$1(reminder)),")"].join('');
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs40505))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["reminder-description"], null)], null),attrs40505], 0))):({"className": "reminder-description"})),((cljs.core.map_QMARK_(attrs40505))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs40505)], null)));
})())),oc$web$components$recurring_updates_modal$iter__40493(cljs.core.rest(s__40494__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(reminders_list);
})())):React.createElement("div",({"className": "recurring-updates-empty-list"}),React.createElement("div",({"className": "recurring-updates-empty-list-title"}),"Set up recurring updates"),(function (){var attrs40513 = ["Wut reminds you when it's time to update your team. ","You can create reminders for yourself and others."].join('');
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs40513))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["recurring-updates-empty-list-desc"], null)], null),attrs40513], 0))):({"className": "recurring-updates-empty-list-desc"})),((cljs.core.map_QMARK_(attrs40513))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs40513)], null)));
})()))));
}),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"reminders-data","reminders-data",-1331370092)], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
oc.web.actions.reminder.load_reminders_roster();

oc.web.actions.reminder.load_reminders();

return s;
})], null)], null),"recurring-updates-modal");

//# sourceMappingURL=oc.web.components.recurring_updates_modal.js.map
