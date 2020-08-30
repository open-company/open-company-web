goog.provide('oc.web.utils.notification');
oc.web.utils.notification.notification_title = (function oc$web$utils$notification$notification_title(var_args){
var args__4742__auto__ = [];
var len__4736__auto___38153 = arguments.length;
var i__4737__auto___38154 = (0);
while(true){
if((i__4737__auto___38154 < len__4736__auto___38153)){
args__4742__auto__.push((arguments[i__4737__auto___38154]));

var G__38155 = (i__4737__auto___38154 + (1));
i__4737__auto___38154 = G__38155;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.utils.notification.notification_title.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.utils.notification.notification_title.cljs$core$IFn$_invoke$arity$variadic = (function (notification,p__38031){
var vec__38032 = p__38031;
var no_first_name_QMARK_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38032,(0),null);
var mention_QMARK_ = new cljs.core.Keyword(null,"mention?","mention?",1073941922).cljs$core$IFn$_invoke$arity$1(notification);
var reminder_QMARK_ = new cljs.core.Keyword(null,"reminder?","reminder?",-1585276037).cljs$core$IFn$_invoke$arity$1(notification);
var author = new cljs.core.Keyword(null,"author","author",2111686192).cljs$core$IFn$_invoke$arity$1(notification);
var first_name = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"first-name","first-name",-1559982131).cljs$core$IFn$_invoke$arity$1(author);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.first(clojure.string.split.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(author),/\s/));
}
})();
var reminder = (cljs.core.truth_(reminder_QMARK_)?new cljs.core.Keyword(null,"reminder","reminder",194643212).cljs$core$IFn$_invoke$arity$1(notification):null);
var notification_type = (cljs.core.truth_(reminder_QMARK_)?new cljs.core.Keyword(null,"notification-type","notification-type",-1128323787).cljs$core$IFn$_invoke$arity$1(reminder):null);
var reminder_assignee = (cljs.core.truth_(reminder_QMARK_)?new cljs.core.Keyword(null,"assignee","assignee",-1242457026).cljs$core$IFn$_invoke$arity$1(reminder):null);
var entry_publisher = new cljs.core.Keyword(null,"entry-publisher","entry-publisher",377441546).cljs$core$IFn$_invoke$arity$1(notification);
var user_id = new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(notification);
if(cljs.core.truth_((function (){var and__4115__auto__ = reminder;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(notification_type,"reminder-notification");
} else {
return and__4115__auto__;
}
})())){
return [(cljs.core.truth_(no_first_name_QMARK_)?null:[cljs.core.str.cljs$core$IFn$_invoke$arity$1(first_name)," "].join('')),"created a new reminder for you"].join('');
} else {
if(cljs.core.truth_((function (){var and__4115__auto__ = reminder;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(notification_type,"reminder-alert");
} else {
return and__4115__auto__;
}
})())){
return ["Hi ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.first(clojure.string.split.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(reminder_assignee),/\s/))),", it's time to update your team"].join('');
} else {
if(cljs.core.truth_(mention_QMARK_)){
return [(cljs.core.truth_(no_first_name_QMARK_)?null:[cljs.core.str.cljs$core$IFn$_invoke$arity$1(first_name)," "].join('')),"mentioned you"].join('');
} else {
if(cljs.core.truth_(new cljs.core.Keyword(null,"interaction-id","interaction-id",1872154585).cljs$core$IFn$_invoke$arity$1(notification))){
return [(cljs.core.truth_(no_first_name_QMARK_)?null:[cljs.core.str.cljs$core$IFn$_invoke$arity$1(first_name)," "].join('')),"added a comment"].join('');
} else {
return null;

}
}
}
}
}));

(oc.web.utils.notification.notification_title.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.utils.notification.notification_title.cljs$lang$applyTo = (function (seq38029){
var G__38030 = cljs.core.first(seq38029);
var seq38029__$1 = cljs.core.next(seq38029);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__38030,seq38029__$1);
}));

oc.web.utils.notification.notification_content = (function oc$web$utils$notification$notification_content(notification){
var reminder_QMARK_ = new cljs.core.Keyword(null,"reminder?","reminder?",-1585276037).cljs$core$IFn$_invoke$arity$1(notification);
var reminder = (cljs.core.truth_(reminder_QMARK_)?new cljs.core.Keyword(null,"reminder","reminder",194643212).cljs$core$IFn$_invoke$arity$1(notification):null);
var notification_type = (cljs.core.truth_(reminder_QMARK_)?new cljs.core.Keyword(null,"notification-type","notification-type",-1128323787).cljs$core$IFn$_invoke$arity$1(reminder):null);
if(cljs.core.truth_((function (){var and__4115__auto__ = reminder;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(notification_type,"reminder-notification");
} else {
return and__4115__auto__;
}
})())){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"headline","headline",-157157727).cljs$core$IFn$_invoke$arity$1(reminder)),": ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"frequency","frequency",-1408891382).cljs$core$IFn$_invoke$arity$1(reminder))," starting ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.utils.activity.post_date(new cljs.core.Keyword(null,"next-send","next-send",-245355311).cljs$core$IFn$_invoke$arity$1(reminder)))].join('');
} else {
if(cljs.core.truth_((function (){var and__4115__auto__ = reminder;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(notification_type,"reminder-alert");
} else {
return and__4115__auto__;
}
})())){
return new cljs.core.Keyword(null,"headline","headline",-157157727).cljs$core$IFn$_invoke$arity$1(reminder);
} else {
return new cljs.core.Keyword(null,"content","content",15833224).cljs$core$IFn$_invoke$arity$1(notification);

}
}
});
oc.web.utils.notification.notification_click = (function oc$web$utils$notification$notification_click(activity_data,interaction_uuid,status){
var url = (cljs.core.truth_(activity_data)?((cljs.core.seq(interaction_uuid))?oc.web.urls.comment_url.cljs$core$IFn$_invoke$arity$3(new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(activity_data),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data),interaction_uuid):oc.web.urls.entry.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(activity_data),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data))):null);
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0(),"replies")) && (cljs.core.seq(activity_data)))){
return (function (){
return oc.web.actions.nav_sidebar.open_post_modal(activity_data,false,interaction_uuid);
});
} else {
if(cljs.core.seq(activity_data)){
return (function (){
return oc.web.router.nav_BANG_(url);
});
} else {
return (function (){
var alert_data = new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"icon","icon",1679606541),"/img/ML/trash.svg",new cljs.core.Keyword(null,"action","action",-811238024),"notification-click-item-load",new cljs.core.Keyword(null,"title","title",636505583),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,(404)))?"Post not found":"An error occurred"),new cljs.core.Keyword(null,"message","message",-406056002),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,(404)))?"The post you're trying to access may have been moved or deleted.":"Please try again"),new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"Ok",new cljs.core.Keyword(null,"solid-button-style","solid-button-style",-723792244),new cljs.core.Keyword(null,"red","red",-969428204),new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),oc.web.components.ui.alert_modal.hide_alert], null);
return oc.web.components.ui.alert_modal.show_alert(alert_data);
});

}
}
});
oc.web.utils.notification.load_item = (function oc$web$utils$notification$load_item(db,org_slug,board_slug,entry_uuid,interaction_uuid){
oc.web.actions.cmail.get_entry_with_uuid.cljs$core$IFn$_invoke$arity$variadic(board_slug,entry_uuid,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(function (success,status){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((404),status)){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"user-notification-remove-by-entry","user-notification-remove-by-entry",-1249722649),org_slug,board_slug,entry_uuid], null));
} else {
return null;
}
})], 0));

return null;
});
oc.web.utils.notification.load_item_if_needed = (function oc$web$utils$notification$load_item_if_needed(db,board_slug,entry_uuid,interaction_uuid){
if(cljs.core.truth_((function (){var and__4115__auto__ = board_slug;
if(cljs.core.truth_(and__4115__auto__)){
return entry_uuid;
} else {
return and__4115__auto__;
}
})())){
var activity_data = oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$3(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$1(db),entry_uuid,db);
if(cljs.core.map_QMARK_(activity_data)){
} else {
oc.web.utils.notification.load_item(db,oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$1(db),board_slug,entry_uuid,interaction_uuid);
}

if(cljs.core.truth_(((cljs.core.map_QMARK_(activity_data))?(function (){var and__4115__auto__ = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data);
if(cljs.core.truth_(and__4115__auto__)){
return new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(activity_data);
} else {
return and__4115__auto__;
}
})():false))){
return oc.web.utils.notification.notification_click(activity_data,interaction_uuid,null);
} else {
return null;
}
} else {
return null;
}
});
oc.web.utils.notification.fix_notification = (function oc$web$utils$notification$fix_notification(db,notification){
var board_id = new cljs.core.Keyword(null,"board-id","board-id",-1767919501).cljs$core$IFn$_invoke$arity$1(notification);
var board_data = oc.web.utils.activity.board_by_uuid(board_id);
var title = oc.web.utils.notification.notification_title(notification);
var body = oc.web.utils.notification.notification_content(notification);
var reminder_data = new cljs.core.Keyword(null,"reminder","reminder",194643212).cljs$core$IFn$_invoke$arity$1(notification);
var entry_uuid = new cljs.core.Keyword(null,"entry-id","entry-id",591934358).cljs$core$IFn$_invoke$arity$1(notification);
var interaction_uuid = new cljs.core.Keyword(null,"interaction-id","interaction-id",1872154585).cljs$core$IFn$_invoke$arity$1(notification);
var activity_data = oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$1(entry_uuid);
var current_user_data = new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915).cljs$core$IFn$_invoke$arity$1(db);
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([notification,new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"activity-data","activity-data",1293689136),activity_data,new cljs.core.Keyword(null,"title","title",636505583),title,new cljs.core.Keyword(null,"body","body",-2049205669),body,new cljs.core.Keyword(null,"unread","unread",-1950424572),((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(current_user_data),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"author","author",2111686192).cljs$core$IFn$_invoke$arity$1(notification))))?(function (){var or__4126__auto__ = new cljs.core.Keyword(null,"unread","unread",-1950424572).cljs$core$IFn$_invoke$arity$1(notification);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return (new cljs.core.Keyword(null,"notify-at","notify-at",-1494324812).cljs$core$IFn$_invoke$arity$1(notification) > new cljs.core.Keyword(null,"last-read-at","last-read-at",-216601930).cljs$core$IFn$_invoke$arity$1(activity_data));
}
})():false),new cljs.core.Keyword(null,"current-user-id","current-user-id",-2013633451),(function (){var or__4126__auto__ = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915),new cljs.core.Keyword(null,"user-id","user-id",-206822291)], null));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"jwt","jwt",1504015441),new cljs.core.Keyword(null,"user-id","user-id",-206822291)], null));
}
})(),new cljs.core.Keyword(null,"click","click",1912301393),(cljs.core.truth_(new cljs.core.Keyword(null,"reminder?","reminder?",-1585276037).cljs$core$IFn$_invoke$arity$1(notification))?(cljs.core.truth_(oc.web.lib.responsive.is_mobile_size_QMARK_())?null:(cljs.core.truth_((function (){var and__4115__auto__ = reminder_data;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"notification-type","notification-type",-1128323787).cljs$core$IFn$_invoke$arity$1(reminder_data),"reminder-notification");
} else {
return and__4115__auto__;
}
})())?(function (){
return oc.web.actions.nav_sidebar.show_reminders();
}):(function (){
return oc.web.utils.ui.ui_compose();
}))):oc.web.utils.notification.load_item_if_needed(db,(function (){var or__4126__auto__ = new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(board_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return board_id;
}
})(),entry_uuid,new cljs.core.Keyword(null,"interaction-id","interaction-id",1872154585).cljs$core$IFn$_invoke$arity$1(notification)))], null)], 0));
});
oc.web.utils.notification.reply_notifications = (function oc$web$utils$notification$reply_notifications(comment_uuid,ns){
return cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"notify-at","notify-at",-1494324812),cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__38064_SHARP_){
return ((cljs.core.seq(new cljs.core.Keyword(null,"interaction-id","interaction-id",1872154585).cljs$core$IFn$_invoke$arity$1(p1__38064_SHARP_))) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"parent-interaction-id","parent-interaction-id",881943973).cljs$core$IFn$_invoke$arity$1(p1__38064_SHARP_),comment_uuid)));
}),ns));
});
oc.web.utils.notification.comment_notifications = (function oc$web$utils$notification$comment_notifications(ns){
return cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__38066_SHARP_){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__38066_SHARP_,new cljs.core.Keyword(null,"replies","replies",-1389888974),oc.web.utils.notification.reply_notifications(new cljs.core.Keyword(null,"interaction-id","interaction-id",1872154585).cljs$core$IFn$_invoke$arity$1(p1__38066_SHARP_),ns));
}),cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__38065_SHARP_){
return ((cljs.core.seq(new cljs.core.Keyword(null,"interaction-id","interaction-id",1872154585).cljs$core$IFn$_invoke$arity$1(p1__38065_SHARP_))) && (cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"parent-interaction-id","parent-interaction-id",881943973).cljs$core$IFn$_invoke$arity$1(p1__38065_SHARP_))));
}),ns));
});
/**
 * @param {...*} var_args
 */
oc.web.utils.notification.latest_notify_at = (function() { 
var oc$web$utils$notification$latest_notify_at__delegate = function (args__33705__auto__){
var ocr_38073 = cljs.core.vec(args__33705__auto__);
try{if(((cljs.core.vector_QMARK_(ocr_38073)) && ((cljs.core.count(ocr_38073) === 1)))){
try{var ocr_38073_0__38075 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_38073,(0));
if(cljs.core.sequential_QMARK_(ocr_38073_0__38075)){
var ns = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_38073,(0));
return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.max,cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"notify-at","notify-at",-1494324812),ns));
} else {
throw cljs.core.match.backtrack;

}
}catch (e38085){if((e38085 instanceof Error)){
var e__32662__auto__ = e38085;
if((e__32662__auto__ === cljs.core.match.backtrack)){
try{var ocr_38073_0__38075 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_38073,(0));
if((function (p1__38071_SHARP_){
return cljs.core.contains_QMARK_(p1__38071_SHARP_,new cljs.core.Keyword(null,"notify-at","notify-at",-1494324812));
})(ocr_38073_0__38075)){
var n = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_38073,(0));
return new cljs.core.Keyword(null,"notify-at","notify-at",-1494324812).cljs$core$IFn$_invoke$arity$1(n);
} else {
throw cljs.core.match.backtrack;

}
}catch (e38086){if((e38086 instanceof Error)){
var e__32662__auto____$1 = e38086;
if((e__32662__auto____$1 === cljs.core.match.backtrack)){
try{var ocr_38073_0__38075 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_38073,(0));
if(cljs.core.map_QMARK_(ocr_38073_0__38075)){
var n = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_38073,(0));
if(cljs.core.contains_QMARK_(n,new cljs.core.Keyword(null,"replies","replies",-1389888974))){
var x__4214__auto__ = new cljs.core.Keyword(null,"notify-at","notify-at",-1494324812).cljs$core$IFn$_invoke$arity$1(n);
var y__4215__auto__ = (function (){var G__38089 = new cljs.core.Keyword(null,"replies","replies",-1389888974).cljs$core$IFn$_invoke$arity$1(n);
return (oc.web.utils.notification.latest_notify_at.cljs$core$IFn$_invoke$arity$1 ? oc.web.utils.notification.latest_notify_at.cljs$core$IFn$_invoke$arity$1(G__38089) : oc.web.utils.notification.latest_notify_at.call(null,G__38089));
})();
return ((x__4214__auto__ > y__4215__auto__) ? x__4214__auto__ : y__4215__auto__);
} else {
return new cljs.core.Keyword(null,"notify-at","notify-at",-1494324812).cljs$core$IFn$_invoke$arity$1(n);
}
} else {
throw cljs.core.match.backtrack;

}
}catch (e38087){if((e38087 instanceof Error)){
var e__32662__auto____$2 = e38087;
if((e__32662__auto____$2 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$2;
}
} else {
throw e38087;

}
}} else {
throw e__32662__auto____$1;
}
} else {
throw e38086;

}
}} else {
throw e__32662__auto__;
}
} else {
throw e38085;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e38084){if((e38084 instanceof Error)){
var e__32662__auto__ = e38084;
if((e__32662__auto__ === cljs.core.match.backtrack)){
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ocr_38073)].join('')));
} else {
throw e__32662__auto__;
}
} else {
throw e38084;

}
}};
var oc$web$utils$notification$latest_notify_at = function (var_args){
var args__33705__auto__ = null;
if (arguments.length > 0) {
var G__38167__i = 0, G__38167__a = new Array(arguments.length -  0);
while (G__38167__i < G__38167__a.length) {G__38167__a[G__38167__i] = arguments[G__38167__i + 0]; ++G__38167__i;}
  args__33705__auto__ = new cljs.core.IndexedSeq(G__38167__a,0,null);
} 
return oc$web$utils$notification$latest_notify_at__delegate.call(this,args__33705__auto__);};
oc$web$utils$notification$latest_notify_at.cljs$lang$maxFixedArity = 0;
oc$web$utils$notification$latest_notify_at.cljs$lang$applyTo = (function (arglist__38168){
var args__33705__auto__ = cljs.core.seq(arglist__38168);
return oc$web$utils$notification$latest_notify_at__delegate(args__33705__auto__);
});
oc$web$utils$notification$latest_notify_at.cljs$core$IFn$_invoke$arity$variadic = oc$web$utils$notification$latest_notify_at__delegate;
return oc$web$utils$notification$latest_notify_at;
})()
;
oc.web.utils.notification.entry_notifications = (function oc$web$utils$notification$entry_notifications(db,entry_id,ns){
var all_roots = cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__38094_SHARP_){
return ((cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"interaction-id","interaction-id",1872154585).cljs$core$IFn$_invoke$arity$1(p1__38094_SHARP_))) && (cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"parent-interaction-id","parent-interaction-id",881943973).cljs$core$IFn$_invoke$arity$1(p1__38094_SHARP_))));
}),ns);
var all_comments = oc.web.utils.notification.comment_notifications(ns);
var included_notify_at = cljs.core.set(cljs.core.concat.cljs$core$IFn$_invoke$arity$variadic(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"notify-at","notify-at",-1494324812),all_roots),cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"notify-at","notify-at",-1494324812),all_comments),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.mapcat.cljs$core$IFn$_invoke$arity$variadic((function (p1__38095_SHARP_){
return cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"notify-at","notify-at",-1494324812),new cljs.core.Keyword(null,"replies","replies",-1389888974).cljs$core$IFn$_invoke$arity$1(p1__38095_SHARP_));
}),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([all_comments], 0))], 0)));
var excluded_ns = cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__38096_SHARP_){
return cljs.core.not((function (){var G__38106 = new cljs.core.Keyword(null,"notify-at","notify-at",-1494324812).cljs$core$IFn$_invoke$arity$1(p1__38096_SHARP_);
return (included_notify_at.cljs$core$IFn$_invoke$arity$1 ? included_notify_at.cljs$core$IFn$_invoke$arity$1(G__38106) : included_notify_at.call(null,G__38106));
})());
}),ns);
var all_ns = cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,cljs.core.concat.cljs$core$IFn$_invoke$arity$variadic(all_roots,all_comments,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([excluded_ns], 0)));
var with_notify_at = cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__38097_SHARP_){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__38097_SHARP_,new cljs.core.Keyword(null,"latest-notify-at","latest-notify-at",-954939418),oc.web.utils.notification.latest_notify_at.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__38097_SHARP_], 0)));
}),all_ns);
var activity_data = oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$3(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$1(db),entry_id,db);
var board_id = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(activity_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"board-id","board-id",-1767919501).cljs$core$IFn$_invoke$arity$1(cljs.core.first(ns));
}
})();
return new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"entry-id","entry-id",591934358),entry_id,new cljs.core.Keyword(null,"notifications","notifications",1685638001),cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"latest-notify-at","latest-notify-at",-954939418),with_notify_at),new cljs.core.Keyword(null,"activity-data","activity-data",1293689136),oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$3(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$1(db),entry_id,db),new cljs.core.Keyword(null,"current-user-id","current-user-id",-2013633451),(function (){var or__4126__auto__ = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915),new cljs.core.Keyword(null,"user-id","user-id",-206822291)], null));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"jwt","jwt",1504015441),new cljs.core.Keyword(null,"user-id","user-id",-206822291)], null));
}
})(),new cljs.core.Keyword(null,"latest-notify-at","latest-notify-at",-954939418),oc.web.utils.notification.latest_notify_at.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([with_notify_at], 0)),new cljs.core.Keyword(null,"click","click",1912301393),oc.web.utils.notification.load_item_if_needed(db,board_id,entry_id,null)], null);
});
oc.web.utils.notification.caught_up_map = (function oc$web$utils$notification$caught_up_map(var_args){
var G__38112 = arguments.length;
switch (G__38112) {
case 0:
return oc.web.utils.notification.caught_up_map.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.utils.notification.caught_up_map.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.utils.notification.caught_up_map.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.utils.notification.caught_up_map.cljs$core$IFn$_invoke$arity$1(null);
}));

(oc.web.utils.notification.caught_up_map.cljs$core$IFn$_invoke$arity$1 = (function (n){
var t = (cljs.core.truth_(new cljs.core.Keyword(null,"latest-notify-at","latest-notify-at",-954939418).cljs$core$IFn$_invoke$arity$1(n))?oc.web.lib.utils.js_date.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(oc.web.lib.utils.js_date.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"latest-notify-at","latest-notify-at",-954939418).cljs$core$IFn$_invoke$arity$1(n)], 0)).getTime() + (1))], 0)).toISOString():oc.web.lib.utils.as_of_now());
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"resource-type","resource-type",1844262326),new cljs.core.Keyword(null,"caught-up","caught-up",-1338440788),new cljs.core.Keyword(null,"latest-notify-at","latest-notify-at",-954939418),t], null);
}));

(oc.web.utils.notification.caught_up_map.cljs$lang$maxFixedArity = 1);

oc.web.utils.notification.insert_open_close_items = (function oc$web$utils$notification$insert_open_close_items(ns){
if(cljs.core.seq(ns)){
return cljs.core.update.cljs$core$IFn$_invoke$arity$3(cljs.core.update.cljs$core$IFn$_invoke$arity$3(cljs.core.vec(ns),(0),(function (p1__38115_SHARP_){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__38115_SHARP_,new cljs.core.Keyword(null,"open-item","open-item",-1938301269),true);
})),(cljs.core.count(ns) - (1)),(function (p1__38116_SHARP_){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__38116_SHARP_,new cljs.core.Keyword(null,"close-item","close-item",-38717813),true);
}));
} else {
return null;
}
});
oc.web.utils.notification.insert_caught_up = (function oc$web$utils$notification$insert_caught_up(ns){
if(cljs.core.seq(ns)){
var first_read_index = oc.web.lib.utils.index_of(ns,cljs.core.comp.cljs$core$IFn$_invoke$arity$2(cljs.core.not,new cljs.core.Keyword(null,"unread","unread",-1950424572)));
var insert_QMARK_ = (function (){var and__4115__auto__ = first_read_index;
if(cljs.core.truth_(and__4115__auto__)){
return (first_read_index > (-1));
} else {
return and__4115__auto__;
}
})();
var vec__38123 = (cljs.core.truth_(insert_QMARK_)?cljs.core.split_at(first_read_index,ns):null);
var ns_before = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38123,(0),null);
var ns_after = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38123,(1),null);
if(cljs.core.truth_(insert_QMARK_)){
return cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$variadic(oc.web.utils.notification.insert_open_close_items(ns_before),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [oc.web.utils.notification.caught_up_map.cljs$core$IFn$_invoke$arity$1(cljs.core.last(ns_before))], null),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([oc.web.utils.notification.insert_open_close_items(ns_after)], 0)));
} else {
return cljs.core.vec(cljs.core.cons(oc.web.utils.notification.caught_up_map.cljs$core$IFn$_invoke$arity$0(),oc.web.utils.notification.insert_open_close_items(ns)));
}
} else {
return null;
}
});
oc.web.utils.notification.group_notifications = (function oc$web$utils$notification$group_notifications(db,ns){
var grouped_ns = cljs.core.group_by(new cljs.core.Keyword(null,"entry-id","entry-id",591934358),ns);
var three_ns = cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p__38126){
var vec__38127 = p__38126;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38127,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38127,(1),null);
return oc.web.utils.notification.entry_notifications(db,k,v);
}),grouped_ns);
var sorted_ns = cljs.core.reverse(cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"latest-notify-at","latest-notify-at",-954939418),three_ns));
var or__4126__auto__ = oc.web.utils.notification.insert_caught_up(sorted_ns);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.PersistentVector.EMPTY;
}
});
oc.web.utils.notification.sorted_notifications = (function oc$web$utils$notification$sorted_notifications(notifications){
return cljs.core.vec(cljs.core.reverse(cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"notify-at","notify-at",-1494324812),notifications)));
});
/**
 * @param {...*} var_args
 */
oc.web.utils.notification.fix_notifications = (function() { 
var oc$web$utils$notification$fix_notifications__delegate = function (args__33705__auto__){
var ocr_38136 = cljs.core.vec(args__33705__auto__);
try{if(((cljs.core.vector_QMARK_(ocr_38136)) && ((cljs.core.count(ocr_38136) === 2)))){
try{var ocr_38136_1__38140 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_38136,(1));
if(cljs.core.map_QMARK_(ocr_38136_1__38140)){
var notifications = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_38136,(1));
var db = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_38136,(0));
var G__38148 = db;
var G__38149 = new cljs.core.Keyword(null,"sorted","sorted",-896746253).cljs$core$IFn$_invoke$arity$1(notifications);
return (oc.web.utils.notification.fix_notifications.cljs$core$IFn$_invoke$arity$2 ? oc.web.utils.notification.fix_notifications.cljs$core$IFn$_invoke$arity$2(G__38148,G__38149) : oc.web.utils.notification.fix_notifications.call(null,G__38148,G__38149));
} else {
throw cljs.core.match.backtrack;

}
}catch (e38145){if((e38145 instanceof Error)){
var e__32662__auto__ = e38145;
if((e__32662__auto__ === cljs.core.match.backtrack)){
try{var ocr_38136_1__38140 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_38136,(1));
if(cljs.core.sequential_QMARK_(ocr_38136_1__38140)){
var notifications = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_38136,(1));
var db = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_38136,(0));
var fixed_notifications = cljs.core.map.cljs$core$IFn$_invoke$arity$2(cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.utils.notification.fix_notification,db),notifications);
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"sorted","sorted",-896746253),oc.web.utils.notification.sorted_notifications(cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,fixed_notifications)),new cljs.core.Keyword(null,"grouped","grouped",15624546),oc.web.utils.notification.group_notifications(db,fixed_notifications)], null);
} else {
throw cljs.core.match.backtrack;

}
}catch (e38146){if((e38146 instanceof Error)){
var e__32662__auto____$1 = e38146;
if((e__32662__auto____$1 === cljs.core.match.backtrack)){
try{var ocr_38136_1__38140 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_38136,(1));
if((ocr_38136_1__38140 == null)){
var _notifications = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_38136,(1));
var _db = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_38136,(0));
return cljs.core.PersistentVector.EMPTY;
} else {
throw cljs.core.match.backtrack;

}
}catch (e38147){if((e38147 instanceof Error)){
var e__32662__auto____$2 = e38147;
if((e__32662__auto____$2 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$2;
}
} else {
throw e38147;

}
}} else {
throw e__32662__auto____$1;
}
} else {
throw e38146;

}
}} else {
throw e__32662__auto__;
}
} else {
throw e38145;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e38144){if((e38144 instanceof Error)){
var e__32662__auto__ = e38144;
if((e__32662__auto__ === cljs.core.match.backtrack)){
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ocr_38136)].join('')));
} else {
throw e__32662__auto__;
}
} else {
throw e38144;

}
}};
var oc$web$utils$notification$fix_notifications = function (var_args){
var args__33705__auto__ = null;
if (arguments.length > 0) {
var G__38185__i = 0, G__38185__a = new Array(arguments.length -  0);
while (G__38185__i < G__38185__a.length) {G__38185__a[G__38185__i] = arguments[G__38185__i + 0]; ++G__38185__i;}
  args__33705__auto__ = new cljs.core.IndexedSeq(G__38185__a,0,null);
} 
return oc$web$utils$notification$fix_notifications__delegate.call(this,args__33705__auto__);};
oc$web$utils$notification$fix_notifications.cljs$lang$maxFixedArity = 0;
oc$web$utils$notification$fix_notifications.cljs$lang$applyTo = (function (arglist__38186){
var args__33705__auto__ = cljs.core.seq(arglist__38186);
return oc$web$utils$notification$fix_notifications__delegate(args__33705__auto__);
});
oc$web$utils$notification$fix_notifications.cljs$core$IFn$_invoke$arity$variadic = oc$web$utils$notification$fix_notifications__delegate;
return oc$web$utils$notification$fix_notifications;
})()
;

//# sourceMappingURL=oc.web.utils.notification.js.map
