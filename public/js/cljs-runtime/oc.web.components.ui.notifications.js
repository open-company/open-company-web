goog.provide('oc.web.components.ui.notifications');
oc.web.components.ui.notifications.button_wrapper = (function oc$web$components$ui$notifications$button_wrapper(s,bt_ref,bt_cb,bt_title,bt_style,bt_dismiss){
var has_html = typeof bt_title === 'string';
var button_base_map = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (e){
if(cljs.core.fn_QMARK_(bt_cb)){
(bt_cb.cljs$core$IFn$_invoke$arity$1 ? bt_cb.cljs$core$IFn$_invoke$arity$1(e) : bt_cb.call(null,e));
} else {
}

if(cljs.core.truth_(bt_dismiss)){
return oc.web.actions.notifications.remove_notification(cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s)));
} else {
return null;
}
}),new cljs.core.Keyword(null,"ref","ref",1289896967),bt_ref,new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"solid-green","solid-green",2061420812),cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(bt_style,new cljs.core.Keyword(null,"solid-green","solid-green",2061420812)),new cljs.core.Keyword(null,"default-link","default-link",-1226863124),cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(bt_style,new cljs.core.Keyword(null,"default-link","default-link",-1226863124))], null))], null);
var button_map = ((has_html)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(button_base_map,new cljs.core.Keyword(null,"dangerouslySetInnerHTML","dangerouslySetInnerHTML",-554971138),({"__html": bt_title})):button_base_map);
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.notification-button.group","button.mlb-reset.notification-button.group",-736176293),button_map,((has_html)?null:bt_title)], null);
});
oc.web.components.ui.notifications.clear_timeout = (function oc$web$components$ui$notifications$clear_timeout(s){
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.notifications","timeout","oc.web.components.ui.notifications/timeout",1717976780).cljs$core$IFn$_invoke$arity$1(s)))){
clearTimeout(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.notifications","timeout","oc.web.components.ui.notifications/timeout",1717976780).cljs$core$IFn$_invoke$arity$1(s)));

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.notifications","timeout","oc.web.components.ui.notifications/timeout",1717976780).cljs$core$IFn$_invoke$arity$1(s),null);
} else {
return null;
}
});
oc.web.components.ui.notifications.setup_timeout = (function oc$web$components$ui$notifications$setup_timeout(s){
oc.web.components.ui.notifications.clear_timeout(s);

var n_data = cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s));
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.notifications","old-expire","oc.web.components.ui.notifications/old-expire",-1080393501).cljs$core$IFn$_invoke$arity$1(s),new cljs.core.Keyword(null,"expire","expire",-70657108).cljs$core$IFn$_invoke$arity$1(n_data));

if((new cljs.core.Keyword(null,"expire","expire",-70657108).cljs$core$IFn$_invoke$arity$1(n_data) > (0))){
var expire = (new cljs.core.Keyword(null,"expire","expire",-70657108).cljs$core$IFn$_invoke$arity$1(n_data) * (1000));
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.notifications","timeout","oc.web.components.ui.notifications/timeout",1717976780).cljs$core$IFn$_invoke$arity$1(s),oc.web.lib.utils.after(expire,(function (){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.notifications","dismiss","oc.web.components.ui.notifications/dismiss",-1749384069).cljs$core$IFn$_invoke$arity$1(s),true);
})));
} else {
return null;
}
});
oc.web.components.ui.notifications.notification = rum.core.build_defcs((function (s,p__47003,light_theme){
var map__47005 = p__47003;
var map__47005__$1 = (((((!((map__47005 == null))))?(((((map__47005.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__47005.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__47005):map__47005);
var notification_data = map__47005__$1;
var secondary_bt_dismiss = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__47005__$1,new cljs.core.Keyword(null,"secondary-bt-dismiss","secondary-bt-dismiss",-382072295));
var web_app_update = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__47005__$1,new cljs.core.Keyword(null,"web-app-update","web-app-update",-162792996));
var dismiss_x = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__47005__$1,new cljs.core.Keyword(null,"dismiss-x","dismiss-x",991922748));
var secondary_bt_title = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__47005__$1,new cljs.core.Keyword(null,"secondary-bt-title","secondary-bt-title",70655776));
var description = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__47005__$1,new cljs.core.Keyword(null,"description","description",-1428560544));
var opac = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__47005__$1,new cljs.core.Keyword(null,"opac","opac",1835692673));
var secondary_bt_style = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__47005__$1,new cljs.core.Keyword(null,"secondary-bt-style","secondary-bt-style",-210591485));
var slack_bot = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__47005__$1,new cljs.core.Keyword(null,"slack-bot","slack-bot",1612314086));
var primary_bt_dismiss = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__47005__$1,new cljs.core.Keyword(null,"primary-bt-dismiss","primary-bt-dismiss",-820688058));
var server_error = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__47005__$1,new cljs.core.Keyword(null,"server-error","server-error",-426815993));
var slack_icon = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__47005__$1,new cljs.core.Keyword(null,"slack-icon","slack-icon",101120969));
var dismiss = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__47005__$1,new cljs.core.Keyword(null,"dismiss","dismiss",412569545));
var primary_bt_style = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__47005__$1,new cljs.core.Keyword(null,"primary-bt-style","primary-bt-style",-1540183382));
var mention_author = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__47005__$1,new cljs.core.Keyword(null,"mention-author","mention-author",-1184076083));
var title = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__47005__$1,new cljs.core.Keyword(null,"title","title",636505583));
var click = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__47005__$1,new cljs.core.Keyword(null,"click","click",1912301393));
var primary_bt_inline = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__47005__$1,new cljs.core.Keyword(null,"primary-bt-inline","primary-bt-inline",-796141614));
var mention = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__47005__$1,new cljs.core.Keyword(null,"mention","mention",-1057367181));
var primary_bt_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__47005__$1,new cljs.core.Keyword(null,"primary-bt-cb","primary-bt-cb",-104130157));
var id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__47005__$1,new cljs.core.Keyword(null,"id","id",-1388402092));
var secondary_bt_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__47005__$1,new cljs.core.Keyword(null,"secondary-bt-cb","secondary-bt-cb",-1097849386));
var primary_bt_title = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__47005__$1,new cljs.core.Keyword(null,"primary-bt-title","primary-bt-title",653140150));
return React.createElement("div",({"onMouseEnter": (function (){
return oc.web.components.ui.notifications.clear_timeout(s);
}), "onMouseLeave": (function (){
return oc.web.components.ui.notifications.setup_timeout(s);
}), "onClick": (function (p1__47000_SHARP_){
if(((cljs.core.fn_QMARK_(click)) && (cljs.core.not(oc.web.lib.utils.event_inside_QMARK_(p1__47000_SHARP_,rum.core.ref_node(s,new cljs.core.Keyword(null,"dismiss-bt","dismiss-bt",-806582616))))) && (cljs.core.not(oc.web.lib.utils.event_inside_QMARK_(p1__47000_SHARP_,rum.core.ref_node(s,new cljs.core.Keyword(null,"first-bt","first-bt",145323359))))) && (cljs.core.not(oc.web.lib.utils.event_inside_QMARK_(p1__47000_SHARP_,rum.core.ref_node(s,new cljs.core.Keyword(null,"second-bt","second-bt",-1524822097))))))){
(click.cljs$core$IFn$_invoke$arity$1 ? click.cljs$core$IFn$_invoke$arity$1(p1__47000_SHARP_) : click.call(null,p1__47000_SHARP_));

oc.web.components.ui.notifications.clear_timeout(s);

return oc.web.actions.notifications.remove_notification(notification_data);
} else {
return null;
}
}), "data-notificationid": id, "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["notification","group",oc.web.lib.utils.class_set(cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"mention-notification","mention-notification",-709579744),new cljs.core.Keyword(null,"opac","opac",1835692673),new cljs.core.Keyword(null,"clickable","clickable",440045281),new cljs.core.Keyword(null,"slack-bot","slack-bot",1612314086),new cljs.core.Keyword(null,"server-error","server-error",-426815993),new cljs.core.Keyword(null,"dismiss","dismiss",412569545),new cljs.core.Keyword(null,"inline-bt","inline-bt",-1350127924),new cljs.core.Keyword(null,"dismiss-button","dismiss-button",-475869332),new cljs.core.Keyword(null,"bottom-notch","bottom-notch",1197694266),new cljs.core.Keyword(null,"app-update","app-update",2141035803),new cljs.core.Keyword(null,"light-theme","light-theme",-629344676)],[(function (){var and__4115__auto__ = mention;
if(cljs.core.truth_(and__4115__auto__)){
return mention_author;
} else {
return and__4115__auto__;
}
})(),opac,cljs.core.fn_QMARK_(click),slack_bot,server_error,dismiss,(function (){var or__4126__auto__ = primary_bt_inline;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var and__4115__auto__ = id;
if(cljs.core.truth_(and__4115__auto__)){
var G__47013 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 10, [new cljs.core.Keyword(null,"reminder-deleted","reminder-deleted",-668143136),null,new cljs.core.Keyword(null,"resend-verification-ok","resend-verification-ok",1258208292),null,new cljs.core.Keyword(null,"member-removed-from-team","member-removed-from-team",-1908267292),null,new cljs.core.Keyword(null,"reminder-updated","reminder-updated",479505447),null,new cljs.core.Keyword(null,"org-settings-saved","org-settings-saved",-24203284),null,new cljs.core.Keyword(null,"reminder-created","reminder-created",-975443986),null,new cljs.core.Keyword(null,"invitation-resent","invitation-resent",575319479),null,new cljs.core.Keyword(null,"cancel-invitation","cancel-invitation",1517824759),null,new cljs.core.Keyword(null,"slack-team-added","slack-team-added",-1812849253),null,new cljs.core.Keyword(null,"slack-bot-added","slack-bot-added",-616757346),null], null), null);
var fexpr__47012 = cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(id);
return (fexpr__47012.cljs$core$IFn$_invoke$arity$1 ? fexpr__47012.cljs$core$IFn$_invoke$arity$1(G__47013) : fexpr__47012.call(null,G__47013));
} else {
return and__4115__auto__;
}
}
})(),dismiss,isiPhoneWithoutPhysicalHomeBt(),web_app_update,light_theme]))], null))}),sablono.interpreter.interpret((cljs.core.truth_(dismiss)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.notification-dismiss-bt","button.mlb-reset.notification-dismiss-bt",1755786573),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (p1__47001_SHARP_){
if(cljs.core.fn_QMARK_(dismiss)){
(dismiss.cljs$core$IFn$_invoke$arity$1 ? dismiss.cljs$core$IFn$_invoke$arity$1(p1__47001_SHARP_) : dismiss.call(null,p1__47001_SHARP_));
} else {
}

oc.web.components.ui.notifications.clear_timeout(s);

return oc.web.actions.notifications.remove_notification(notification_data);
}),new cljs.core.Keyword(null,"class","class",-2030961996),(cljs.core.truth_(dismiss_x)?"dismiss-x":null),new cljs.core.Keyword(null,"ref","ref",1289896967),new cljs.core.Keyword(null,"dismiss-bt","dismiss-bt",-806582616)], null),(cljs.core.truth_(dismiss_x)?null:"OK")], null):null)),React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["notification-title","group",((cljs.core.seq(description))?null:"no-description")], null))}),sablono.interpreter.interpret((cljs.core.truth_(slack_icon)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.slack-icon","span.slack-icon",-598682315)], null):null)),sablono.interpreter.interpret(title)),sablono.interpreter.interpret(((cljs.core.seq(description))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.notification-description","div.notification-description",-996101928),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"dangerouslySetInnerHTML","dangerouslySetInnerHTML",-554971138),({"__html": description}),new cljs.core.Keyword(null,"class","class",-2030961996),(cljs.core.truth_(mention)?"oc-mentions":null)], null)], null):null)),sablono.interpreter.interpret(((cljs.core.seq(secondary_bt_title))?oc.web.components.ui.notifications.button_wrapper(s,new cljs.core.Keyword(null,"second-bt","second-bt",-1524822097),secondary_bt_cb,secondary_bt_title,secondary_bt_style,secondary_bt_dismiss):null)),sablono.interpreter.interpret(((cljs.core.seq(primary_bt_title))?oc.web.components.ui.notifications.button_wrapper(s,new cljs.core.Keyword(null,"first-bt","first-bt",145323359),primary_bt_cb,primary_bt_title,primary_bt_style,primary_bt_dismiss):null)));
}),new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$,oc.web.mixins.ui.first_render_mixin,rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.notifications","dismiss","oc.web.components.ui.notifications/dismiss",-1749384069)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.notifications","notification-removed","oc.web.components.ui.notifications/notification-removed",-181683924)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.notifications","timeout","oc.web.components.ui.notifications/timeout",1717976780)),rum.core.local.cljs$core$IFn$_invoke$arity$2((0),new cljs.core.Keyword("oc.web.components.ui.notifications","old-expire","oc.web.components.ui.notifications/old-expire",-1080393501)),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
oc.web.components.ui.notifications.setup_timeout(s);

return s;
}),new cljs.core.Keyword(null,"did-remount","did-remount",1362550500),(function (o,s){
oc.web.components.ui.notifications.setup_timeout(s);

return s;
}),new cljs.core.Keyword(null,"after-render","after-render",1997533433),(function (s){
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.notifications","dismiss","oc.web.components.ui.notifications/dismiss",-1749384069).cljs$core$IFn$_invoke$arity$1(s)))){
if(cljs.core.compare_and_set_BANG_(new cljs.core.Keyword("oc.web.components.ui.notifications","notification-removed","oc.web.components.ui.notifications/notification-removed",-181683924).cljs$core$IFn$_invoke$arity$1(s),false,true)){
oc.web.actions.notifications.remove_notification(cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s)));
} else {
}
} else {
}

return s;
})], null)], null),"notification");
oc.web.components.ui.notifications.notifications = rum.core.build_defcs((function (s){
var notifications_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"notifications-data","notifications-data",260044272));
var panel_stack = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"panel-stack","panel-stack",1084180004));
var map__47014 = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"ui-theme","ui-theme",1992064701));
var map__47014__$1 = (((((!((map__47014 == null))))?(((((map__47014.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__47014.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__47014):map__47014);
var computed_value = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__47014__$1,new cljs.core.Keyword(null,"computed-value","computed-value",1336702311));
var light_theme_QMARK_ = (((cljs.core.count(panel_stack) > (0))) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(computed_value,new cljs.core.Keyword(null,"dark","dark",1818973999))));
return React.createElement("div",({"className": "notifications"}),cljs.core.into_array.cljs$core$IFn$_invoke$arity$1((function (){var iter__4529__auto__ = (function oc$web$components$ui$notifications$iter__47016(s__47017){
return (new cljs.core.LazySeq(null,(function (){
var s__47017__$1 = s__47017;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__47017__$1);
if(temp__5735__auto__){
var s__47017__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__47017__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__47017__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__47019 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__47018 = (0);
while(true){
if((i__47018 < size__4528__auto__)){
var idx = cljs.core._nth(c__4527__auto__,i__47018);
var n = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(notifications_data,idx);
cljs.core.chunk_append(b__47019,sablono.interpreter.interpret(rum.core.with_key((oc.web.components.ui.notifications.notification.cljs$core$IFn$_invoke$arity$2 ? oc.web.components.ui.notifications.notification.cljs$core$IFn$_invoke$arity$2(n,light_theme_QMARK_) : oc.web.components.ui.notifications.notification.call(null,n,light_theme_QMARK_)),["notif-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(n))].join(''))));

var G__47034 = (i__47018 + (1));
i__47018 = G__47034;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__47019),oc$web$components$ui$notifications$iter__47016(cljs.core.chunk_rest(s__47017__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__47019),null);
}
} else {
var idx = cljs.core.first(s__47017__$2);
var n = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(notifications_data,idx);
return cljs.core.cons(sablono.interpreter.interpret(rum.core.with_key((oc.web.components.ui.notifications.notification.cljs$core$IFn$_invoke$arity$2 ? oc.web.components.ui.notifications.notification.cljs$core$IFn$_invoke$arity$2(n,light_theme_QMARK_) : oc.web.components.ui.notifications.notification.call(null,n,light_theme_QMARK_)),["notif-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(n))].join(''))),oc$web$components$ui$notifications$iter__47016(cljs.core.rest(s__47017__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(cljs.core.range.cljs$core$IFn$_invoke$arity$1(cljs.core.count(notifications_data)));
})()));
}),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$,rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"notifications-data","notifications-data",260044272)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"panel-stack","panel-stack",1084180004)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"ui-theme","ui-theme",1992064701)], 0))], null),"notifications");

//# sourceMappingURL=oc.web.components.ui.notifications.js.map
