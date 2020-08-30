goog.provide('oc.web.components.ui.more_menu');
oc.web.components.ui.more_menu.show_hide_menu = (function oc$web$components$ui$more_menu$show_hide_menu(s,will_open,will_close){
oc.web.lib.utils.remove_tooltips();

var current_showing_menu = (function (){var or__4126__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.more-menu","showing-menu","oc.web.components.ui.more-menu/showing-menu",-748425394).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"force-show-menu","force-show-menu",1246398041).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s)));
}
})();
var next_showing_menu = cljs.core.not(current_showing_menu);
if(next_showing_menu){
if(cljs.core.fn_QMARK_(will_open)){
(will_open.cljs$core$IFn$_invoke$arity$0 ? will_open.cljs$core$IFn$_invoke$arity$0() : will_open.call(null));
} else {
}
} else {
if(cljs.core.fn_QMARK_(will_close)){
(will_close.cljs$core$IFn$_invoke$arity$0 ? will_close.cljs$core$IFn$_invoke$arity$0() : will_close.call(null));
} else {
}
}

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.more-menu","showing-menu","oc.web.components.ui.more-menu/showing-menu",-748425394).cljs$core$IFn$_invoke$arity$1(s),next_showing_menu);
});
oc.web.components.ui.more_menu.delete_clicked = (function oc$web$components$ui$more_menu$delete_clicked(e,current_org_slug,current_board_slug,current_contributions_id,activity_data){
var alert_data = cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"bottom-button-style","bottom-button-style",1586529892),new cljs.core.Keyword(null,"link-button-style","link-button-style",1552381990),new cljs.core.Keyword(null,"link-button-title","link-button-title",-812305976),new cljs.core.Keyword(null,"solid-button-style","solid-button-style",-723792244),new cljs.core.Keyword(null,"bottom-button-cb","bottom-button-cb",1482855598),new cljs.core.Keyword(null,"title","title",636505583),new cljs.core.Keyword(null,"bottom-button-title","bottom-button-title",1550148886),new cljs.core.Keyword(null,"action","action",-811238024),new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),new cljs.core.Keyword(null,"link-button-cb","link-button-cb",559710301),new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),new cljs.core.Keyword(null,"message","message",-406056002)],[new cljs.core.Keyword(null,"red","red",-969428204),new cljs.core.Keyword(null,"green","green",-945526839),"No, keep it",new cljs.core.Keyword(null,"red","red",-969428204),(function (){
oc.web.actions.activity.delete_samples();

return oc.web.components.ui.alert_modal.hide_alert();
}),"Delete this post?",(cljs.core.truth_((function (){var and__4115__auto__ = new cljs.core.Keyword(null,"sample","sample",79023601).cljs$core$IFn$_invoke$arity$1(activity_data);
if(cljs.core.truth_(and__4115__auto__)){
return oc.web.actions.activity.has_sample_posts_QMARK_();
} else {
return and__4115__auto__;
}
})())?"Delete all sample posts":null),"delete-entry","Yes, delete it",(function (){
return oc.web.components.ui.alert_modal.hide_alert();
}),(function (){
var board_url = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug),new cljs.core.Keyword(null,"following","following",-2049193617)))?oc.web.urls.following.cljs$core$IFn$_invoke$arity$1(current_org_slug):((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug),new cljs.core.Keyword(null,"replies","replies",-1389888974)))?oc.web.urls.replies.cljs$core$IFn$_invoke$arity$1(current_org_slug):((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug),new cljs.core.Keyword(null,"all-posts","all-posts",-1285476533)))?oc.web.urls.all_posts.cljs$core$IFn$_invoke$arity$1(current_org_slug):((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug),new cljs.core.Keyword(null,"inbox","inbox",1888669443)))?oc.web.urls.inbox.cljs$core$IFn$_invoke$arity$1(current_org_slug):((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug),new cljs.core.Keyword(null,"unfollowing","unfollowing",-1076165830)))?oc.web.urls.unfollowing.cljs$core$IFn$_invoke$arity$1(current_org_slug):((cljs.core.seq(current_contributions_id))?oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$2(current_org_slug,current_contributions_id):oc.web.urls.board.cljs$core$IFn$_invoke$arity$2(current_org_slug,current_board_slug)
))))));
oc.web.router.nav_BANG_(board_url);

oc.web.actions.activity.activity_delete(activity_data);

return oc.web.components.ui.alert_modal.hide_alert();
}),"This action cannot be undone."]);
return oc.web.components.ui.alert_modal.show_alert(alert_data);
});
oc.web.components.ui.more_menu.more_menu = rum.core.build_defcs((function (s,p__46030){
var map__46031 = p__46030;
var map__46031__$1 = (((((!((map__46031 == null))))?(((((map__46031.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46031.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46031):map__46031);
var force_show_menu = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46031__$1,new cljs.core.Keyword(null,"force-show-menu","force-show-menu",1246398041));
var delete_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46031__$1,new cljs.core.Keyword(null,"delete-cb","delete-cb",895299386));
var external_bookmark = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46031__$1,new cljs.core.Keyword(null,"external-bookmark","external-bookmark",-1631278980));
var show_unread = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46031__$1,new cljs.core.Keyword(null,"show-unread","show-unread",1139681406));
var can_comment_share_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46031__$1,new cljs.core.Keyword(null,"can-comment-share?","can-comment-share?",1320150206));
var mobile_tray_menu = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46031__$1,new cljs.core.Keyword(null,"mobile-tray-menu","mobile-tray-menu",-1905495105));
var show_delete_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46031__$1,new cljs.core.Keyword(null,"show-delete?","show-delete?",-753527136));
var can_reply_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46031__$1,new cljs.core.Keyword(null,"can-reply?","can-reply?",-61994911));
var show_edit_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46031__$1,new cljs.core.Keyword(null,"show-edit?","show-edit?",-1476204765));
var react_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46031__$1,new cljs.core.Keyword(null,"react-cb","react-cb",-2110368957));
var edit_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46031__$1,new cljs.core.Keyword(null,"edit-cb","edit-cb",-974954589));
var show_inbox_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46031__$1,new cljs.core.Keyword(null,"show-inbox?","show-inbox?",-1549891064));
var external_share = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46031__$1,new cljs.core.Keyword(null,"external-share","external-share",-2131927863));
var can_react_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46031__$1,new cljs.core.Keyword(null,"can-react?","can-react?",680341130));
var will_close = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46031__$1,new cljs.core.Keyword(null,"will-close","will-close",1889428842));
var will_open = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46031__$1,new cljs.core.Keyword(null,"will-open","will-open",-1825064821));
var share_container_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46031__$1,new cljs.core.Keyword(null,"share-container-id","share-container-id",775325900));
var reply_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46031__$1,new cljs.core.Keyword(null,"reply-cb","reply-cb",-120094452));
var react_disabled_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46031__$1,new cljs.core.Keyword(null,"react-disabled?","react-disabled?",1235972269));
var entity_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46031__$1,new cljs.core.Keyword(null,"entity-data","entity-data",1608056141));
var mark_unread_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46031__$1,new cljs.core.Keyword(null,"mark-unread-cb","mark-unread-cb",525132945));
var capture_clicks = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46031__$1,new cljs.core.Keyword(null,"capture-clicks","capture-clicks",85843730));
var editable_boards = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46031__$1,new cljs.core.Keyword(null,"editable-boards","editable-boards",1897056658));
var comment_share_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46031__$1,new cljs.core.Keyword(null,"comment-share-cb","comment-share-cb",-65537869));
var remove_bookmark_title = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46031__$1,new cljs.core.Keyword(null,"remove-bookmark-title","remove-bookmark-title",-1493663020));
var show_move_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46031__$1,new cljs.core.Keyword(null,"show-move?","show-move?",274288117));
var tooltip_position = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46031__$1,new cljs.core.Keyword(null,"tooltip-position","tooltip-position",936197013));
var external_follow = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46031__$1,new cljs.core.Keyword(null,"external-follow","external-follow",158310616));
var map__46033 = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"route","route",329891309));
var map__46033__$1 = (((((!((map__46033 == null))))?(((((map__46033.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46033.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46033):map__46033);
var current_org_slug = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46033__$1,new cljs.core.Keyword(null,"org","org",1495985));
var current_board_slug = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46033__$1,new cljs.core.Keyword(null,"board","board",-1907017633));
var current_contributions_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46033__$1,new cljs.core.Keyword(null,"contributions","contributions",-1280485964));
var current_activity_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46033__$1,new cljs.core.Keyword(null,"activity","activity",-1179221455));
var delete_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(entity_data),"delete"], 0));
var edit_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(entity_data),"partial-update"], 0));
var share_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(entity_data),"share"], 0));
var inbox_unread_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(entity_data),"unread"], 0));
var is_mobile_QMARK_ = oc.web.lib.responsive.is_tablet_or_mobile_QMARK_();
var add_bookmark_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(entity_data),"bookmark","POST"], 0));
var remove_bookmark_link = (cljs.core.truth_(new cljs.core.Keyword(null,"bookmarked-at","bookmarked-at",1451784060).cljs$core$IFn$_invoke$arity$1(entity_data))?oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(entity_data),"bookmark","DELETE"], 0)):null);
var should_show_more_bt = (function (){var or__4126__auto__ = (function (){var and__4115__auto__ = show_edit_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return edit_link;
} else {
return and__4115__auto__;
}
})();
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = (function (){var and__4115__auto__ = show_delete_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return delete_link;
} else {
return and__4115__auto__;
}
})();
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
var or__4126__auto____$2 = can_comment_share_QMARK_;
if(cljs.core.truth_(or__4126__auto____$2)){
return or__4126__auto____$2;
} else {
var or__4126__auto____$3 = can_react_QMARK_;
if(cljs.core.truth_(or__4126__auto____$3)){
return or__4126__auto____$3;
} else {
var or__4126__auto____$4 = can_reply_QMARK_;
if(cljs.core.truth_(or__4126__auto____$4)){
return or__4126__auto____$4;
} else {
var or__4126__auto____$5 = ((cljs.core.not(external_share))?share_link:false);
if(cljs.core.truth_(or__4126__auto____$5)){
return or__4126__auto____$5;
} else {
var and__4115__auto__ = inbox_unread_link;
if(cljs.core.truth_(and__4115__auto__)){
return show_unread;
} else {
return and__4115__auto__;
}
}
}
}
}
}
}
})();
var follow_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(entity_data),"follow"], 0));
var unfollow_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(entity_data),"unfollow"], 0));
var show_menu = (function (){var or__4126__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.more-menu","showing-menu","oc.web.components.ui.more-menu/showing-menu",-748425394).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return force_show_menu;
}
})();
return sablono.interpreter.interpret((cljs.core.truth_((function (){var or__4126__auto__ = edit_link;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = share_link;
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
var or__4126__auto____$2 = inbox_unread_link;
if(cljs.core.truth_(or__4126__auto____$2)){
return or__4126__auto____$2;
} else {
var or__4126__auto____$3 = delete_link;
if(cljs.core.truth_(or__4126__auto____$3)){
return or__4126__auto____$3;
} else {
var or__4126__auto____$4 = can_comment_share_QMARK_;
if(cljs.core.truth_(or__4126__auto____$4)){
return or__4126__auto____$4;
} else {
var or__4126__auto____$5 = can_react_QMARK_;
if(cljs.core.truth_(or__4126__auto____$5)){
return or__4126__auto____$5;
} else {
var or__4126__auto____$6 = can_reply_QMARK_;
if(cljs.core.truth_(or__4126__auto____$6)){
return or__4126__auto____$6;
} else {
var or__4126__auto____$7 = add_bookmark_link;
if(cljs.core.truth_(or__4126__auto____$7)){
return or__4126__auto____$7;
} else {
var or__4126__auto____$8 = remove_bookmark_link;
if(cljs.core.truth_(or__4126__auto____$8)){
return or__4126__auto____$8;
} else {
return inbox_unread_link;
}
}
}
}
}
}
}
}
}
})())?new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.more-menu","div.more-menu",-54685194),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"ref","ref",1289896967),"more-menu",new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"menu-expanded","menu-expanded",1259526972),(function (){var or__4126__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.more-menu","move-activity","oc.web.components.ui.more-menu/move-activity",411327013).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return show_menu;
}
})(),new cljs.core.Keyword(null,"has-more-menu-bt","has-more-menu-bt",1754355068),should_show_more_bt,new cljs.core.Keyword(null,"mobile-tray-menu","mobile-tray-menu",-1905495105),mobile_tray_menu,new cljs.core.Keyword(null,"android-browser","android-browser",1761063755),(function (){var and__4115__auto__ = oc.shared.useragent.android_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return (!(oc.shared.useragent.mobile_app_QMARK_));
} else {
return and__4115__auto__;
}
})(),new cljs.core.Keyword(null,"ios-browser","ios-browser",1539238938),(function (){var and__4115__auto__ = oc.shared.useragent.ios_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return (!(oc.shared.useragent.mobile_app_QMARK_));
} else {
return and__4115__auto__;
}
})()], null)),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(cljs.core.truth_(mobile_tray_menu)?(function (p1__46025_SHARP_){
if(cljs.core.truth_((function (){var and__4115__auto__ = show_menu;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.more-menu","can-unmount","oc.web.components.ui.more-menu/can-unmount",1366328316).cljs$core$IFn$_invoke$arity$1(s));
} else {
return and__4115__auto__;
}
})())){
p1__46025_SHARP_.stopPropagation();

return oc.web.components.ui.more_menu.show_hide_menu(s,will_open,will_close);
} else {
return null;
}
}):null)], null),(cljs.core.truth_(should_show_more_bt)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.more-menu-bt","button.mlb-reset.more-menu-bt",960933473),cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"ref","ref",1289896967),new cljs.core.Keyword(null,"data-container","data-container",1473653353),new cljs.core.Keyword(null,"data-delay","data-delay",1974747786),new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"on-click","on-click",1632826543),new cljs.core.Keyword(null,"title","title",636505583),new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.Keyword(null,"data-placement","data-placement",166529525),new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687)],["more-menu-bt","body","{\"show\":\"100\", \"hide\":\"0\"}","button",(function (){
return oc.web.components.ui.more_menu.show_hide_menu(s,will_open,will_close);
}),"More",(cljs.core.truth_(show_menu)?"active":null),(function (){var or__4126__auto__ = tooltip_position;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "top";
}
})(),((is_mobile_QMARK_)?"":"tooltip")])], null):null),(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.more-menu","move-activity","oc.web.components.ui.more-menu/move-activity",411327013).cljs$core$IFn$_invoke$arity$1(s)))?(function (){var G__46035 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"boards-list","boards-list",1343890425),cljs.core.vals(editable_boards),new cljs.core.Keyword(null,"activity-data","activity-data",1293689136),entity_data,new cljs.core.Keyword(null,"dismiss-cb","dismiss-cb",-1282537857),(function (){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.more-menu","move-activity","oc.web.components.ui.more-menu/move-activity",411327013).cljs$core$IFn$_invoke$arity$1(s),false);
})], null);
return (oc.web.components.ui.activity_move.activity_move.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.activity_move.activity_move.cljs$core$IFn$_invoke$arity$1(G__46035) : oc.web.components.ui.activity_move.activity_move.call(null,G__46035));
})():(cljs.core.truth_(show_menu)?new cljs.core.PersistentVector(null, 13, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"ul.more-menu-list","ul.more-menu-list",2107878836),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"has-remove-bookmark","has-remove-bookmark",1408801830),(function (){var and__4115__auto__ = add_bookmark_link;
if(cljs.core.truth_(and__4115__auto__)){
return ((is_mobile_QMARK_) || (cljs.core.not(external_bookmark)));
} else {
return and__4115__auto__;
}
})(),new cljs.core.Keyword(null,"has-add-bookmark","has-add-bookmark",-2093598979),(function (){var and__4115__auto__ = remove_bookmark_link;
if(cljs.core.truth_(and__4115__auto__)){
return ((is_mobile_QMARK_) || (cljs.core.not(external_bookmark)));
} else {
return and__4115__auto__;
}
})(),new cljs.core.Keyword(null,"has-mark-unread","has-mark-unread",1138218254),inbox_unread_link], null))], null),(cljs.core.truth_((function (){var and__4115__auto__ = edit_link;
if(cljs.core.truth_(and__4115__auto__)){
return show_edit_QMARK_;
} else {
return and__4115__auto__;
}
})())?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li.edit.top-rounded","li.edit.top-rounded",1168395016),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (p1__46026_SHARP_){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.more-menu","showing-menu","oc.web.components.ui.more-menu/showing-menu",-748425394).cljs$core$IFn$_invoke$arity$1(s),false);

if(cljs.core.fn_QMARK_(will_close)){
(will_close.cljs$core$IFn$_invoke$arity$0 ? will_close.cljs$core$IFn$_invoke$arity$0() : will_close.call(null));
} else {
}

if(cljs.core.fn_QMARK_(edit_cb)){
return (edit_cb.cljs$core$IFn$_invoke$arity$1 ? edit_cb.cljs$core$IFn$_invoke$arity$1(entity_data) : edit_cb.call(null,entity_data));
} else {
if(cljs.core.truth_(current_activity_id)){
oc.web.actions.nav_sidebar.dismiss_post_modal(p1__46026_SHARP_);
} else {
}

return oc.web.actions.activity.activity_edit.cljs$core$IFn$_invoke$arity$1(entity_data);
}
})], null),"Edit"], null):null),(cljs.core.truth_((function (){var and__4115__auto__ = delete_link;
if(cljs.core.truth_(and__4115__auto__)){
return show_delete_QMARK_;
} else {
return and__4115__auto__;
}
})())?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li.delete.bottom-rounded.bottom-margin","li.delete.bottom-rounded.bottom-margin",-1651503659),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (p1__46027_SHARP_){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.more-menu","showing-menu","oc.web.components.ui.more-menu/showing-menu",-748425394).cljs$core$IFn$_invoke$arity$1(s),false);

if(cljs.core.fn_QMARK_(will_close)){
(will_close.cljs$core$IFn$_invoke$arity$0 ? will_close.cljs$core$IFn$_invoke$arity$0() : will_close.call(null));
} else {
}

if(cljs.core.fn_QMARK_(delete_cb)){
return (delete_cb.cljs$core$IFn$_invoke$arity$1 ? delete_cb.cljs$core$IFn$_invoke$arity$1(entity_data) : delete_cb.call(null,entity_data));
} else {
return oc.web.components.ui.more_menu.delete_clicked(p1__46027_SHARP_,current_org_slug,current_board_slug,current_contributions_id,entity_data);
}
})], null),"Delete"], null):null),(cljs.core.truth_((function (){var and__4115__auto__ = edit_link;
if(cljs.core.truth_(and__4115__auto__)){
return show_move_QMARK_;
} else {
return and__4115__auto__;
}
})())?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li.move.top-rounded","li.move.top-rounded",1669659764),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),((((cljs.core.not(((cljs.core.not(external_share))?share_link:false))) && ((!(((is_mobile_QMARK_) || (cljs.core.not(external_follow)))))) && ((!(((is_mobile_QMARK_) || (cljs.core.not(external_bookmark))))))))?"bottom-rounded bottom-margin":null),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.more-menu","showing-menu","oc.web.components.ui.more-menu/showing-menu",-748425394).cljs$core$IFn$_invoke$arity$1(s),false);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.more-menu","move-activity","oc.web.components.ui.more-menu/move-activity",411327013).cljs$core$IFn$_invoke$arity$1(s),true);
})], null),"Move"], null):null),(cljs.core.truth_(((cljs.core.not(external_share))?share_link:false))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li.share","li.share",-1604222259),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),((((cljs.core.not(inbox_unread_link)) && ((!(((is_mobile_QMARK_) || (cljs.core.not(external_follow)))))) && ((!(((is_mobile_QMARK_) || (cljs.core.not(external_bookmark))))))))?"bottom-rounded bottom-margin":null),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.more-menu","showing-menu","oc.web.components.ui.more-menu/showing-menu",-748425394).cljs$core$IFn$_invoke$arity$1(s),false);

if(cljs.core.fn_QMARK_(will_close)){
(will_close.cljs$core$IFn$_invoke$arity$0 ? will_close.cljs$core$IFn$_invoke$arity$0() : will_close.call(null));
} else {
}

return oc.web.actions.activity.activity_share_show.cljs$core$IFn$_invoke$arity$variadic(entity_data,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([share_container_id], 0));
})], null),"Share"], null):null),(cljs.core.truth_((function (){var and__4115__auto__ = inbox_unread_link;
if(cljs.core.truth_(and__4115__auto__)){
return show_unread;
} else {
return and__4115__auto__;
}
})())?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li.unread","li.unread",-330456345),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),(((((!(((is_mobile_QMARK_) || (cljs.core.not(external_follow)))))) && ((!(((is_mobile_QMARK_) || (cljs.core.not(external_bookmark))))))))?"bottom-rounded bottom-margin":null),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.more-menu","showing-menu","oc.web.components.ui.more-menu/showing-menu",-748425394).cljs$core$IFn$_invoke$arity$1(s),false);

if(cljs.core.fn_QMARK_(will_close)){
(will_close.cljs$core$IFn$_invoke$arity$0 ? will_close.cljs$core$IFn$_invoke$arity$0() : will_close.call(null));
} else {
}

oc.web.actions.activity.mark_unread(entity_data);

if(cljs.core.fn_QMARK_(mark_unread_cb)){
return (mark_unread_cb.cljs$core$IFn$_invoke$arity$0 ? mark_unread_cb.cljs$core$IFn$_invoke$arity$0() : mark_unread_cb.call(null));
} else {
return null;
}
})], null),"Mark as unread"], null):null),((((is_mobile_QMARK_) || (cljs.core.not(external_follow))))?(cljs.core.truth_(follow_link)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li.follow","li.follow",-1418689263),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),((((is_mobile_QMARK_) || (cljs.core.not(external_bookmark))))?null:"bottom-rounded bottom-margin"),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.more-menu","showing-menu","oc.web.components.ui.more-menu/showing-menu",-748425394).cljs$core$IFn$_invoke$arity$1(s),false);

if(cljs.core.fn_QMARK_(will_close)){
(will_close.cljs$core$IFn$_invoke$arity$0 ? will_close.cljs$core$IFn$_invoke$arity$0() : will_close.call(null));
} else {
}

return oc.web.actions.activity.entry_follow(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entity_data));
})], null),"Follow"], null):(cljs.core.truth_(unfollow_link)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li.unfollow","li.unfollow",-1407566491),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),((((is_mobile_QMARK_) || (cljs.core.not(external_bookmark))))?null:"bottom-rounded bottom-margin"),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.more-menu","showing-menu","oc.web.components.ui.more-menu/showing-menu",-748425394).cljs$core$IFn$_invoke$arity$1(s),false);

if(cljs.core.fn_QMARK_(will_close)){
(will_close.cljs$core$IFn$_invoke$arity$0 ? will_close.cljs$core$IFn$_invoke$arity$0() : will_close.call(null));
} else {
}

return oc.web.actions.activity.entry_unfollow(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entity_data));
})], null),"Mute"], null):null)):null),((((is_mobile_QMARK_) || (cljs.core.not(external_bookmark))))?(cljs.core.truth_(remove_bookmark_link)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li.remove-bookmark.bottom-rounded.bottom-margin","li.remove-bookmark.bottom-rounded.bottom-margin",1716030246),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"ref","ref",1289896967),"more-menu-remove-bookmark-bt",new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.more-menu","showing-menu","oc.web.components.ui.more-menu/showing-menu",-748425394).cljs$core$IFn$_invoke$arity$1(s),false);

if(cljs.core.fn_QMARK_(will_close)){
(will_close.cljs$core$IFn$_invoke$arity$0 ? will_close.cljs$core$IFn$_invoke$arity$0() : will_close.call(null));
} else {
}

return oc.web.actions.activity.remove_bookmark(entity_data,remove_bookmark_link);
})], null),"Remove bookmark"], null):(cljs.core.truth_(add_bookmark_link)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li.add-bookmark.bottom-rounded.bottom-margin","li.add-bookmark.bottom-rounded.bottom-margin",39427363),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"ref","ref",1289896967),"more-menu-add-bookmark-bt",new cljs.core.Keyword(null,"data-container","data-container",1473653353),"body",new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.more-menu","showing-menu","oc.web.components.ui.more-menu/showing-menu",-748425394).cljs$core$IFn$_invoke$arity$1(s),false);

if(cljs.core.fn_QMARK_(will_close)){
(will_close.cljs$core$IFn$_invoke$arity$0 ? will_close.cljs$core$IFn$_invoke$arity$0() : will_close.call(null));
} else {
}

return oc.web.actions.activity.add_bookmark(entity_data,add_bookmark_link);
})], null),"Bookmark"], null):null)):null),(cljs.core.truth_(can_react_QMARK_)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li.react.top-rounded","li.react.top-rounded",1836750666),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.more-menu","showing-menu","oc.web.components.ui.more-menu/showing-menu",-748425394).cljs$core$IFn$_invoke$arity$1(s),false);

if(cljs.core.fn_QMARK_(will_close)){
(will_close.cljs$core$IFn$_invoke$arity$0 ? will_close.cljs$core$IFn$_invoke$arity$0() : will_close.call(null));
} else {
}

if(cljs.core.fn_QMARK_(react_cb)){
return (react_cb.cljs$core$IFn$_invoke$arity$0 ? react_cb.cljs$core$IFn$_invoke$arity$0() : react_cb.call(null));
} else {
return null;
}
}),new cljs.core.Keyword(null,"disabled","disabled",-1529784218),react_disabled_QMARK_], null),"React"], null):null),(cljs.core.truth_(can_reply_QMARK_)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li.reply","li.reply",-1840633160),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.more-menu","showing-menu","oc.web.components.ui.more-menu/showing-menu",-748425394).cljs$core$IFn$_invoke$arity$1(s),false);

if(cljs.core.fn_QMARK_(will_close)){
(will_close.cljs$core$IFn$_invoke$arity$0 ? will_close.cljs$core$IFn$_invoke$arity$0() : will_close.call(null));
} else {
}

if(cljs.core.fn_QMARK_(reply_cb)){
return (reply_cb.cljs$core$IFn$_invoke$arity$0 ? reply_cb.cljs$core$IFn$_invoke$arity$0() : reply_cb.call(null));
} else {
return null;
}
})], null),"Reply"], null):null),(cljs.core.truth_(can_comment_share_QMARK_)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li.comment-share.bottom-rounded.bottom-margin","li.comment-share.bottom-rounded.bottom-margin",-1572353462),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.more-menu","showing-menu","oc.web.components.ui.more-menu/showing-menu",-748425394).cljs$core$IFn$_invoke$arity$1(s),false);

if(cljs.core.fn_QMARK_(will_close)){
(will_close.cljs$core$IFn$_invoke$arity$0 ? will_close.cljs$core$IFn$_invoke$arity$0() : will_close.call(null));
} else {
}

if(cljs.core.fn_QMARK_(comment_share_cb)){
return (comment_share_cb.cljs$core$IFn$_invoke$arity$0 ? comment_share_cb.cljs$core$IFn$_invoke$arity$0() : comment_share_cb.call(null));
} else {
return null;
}
})], null),"Copy link"], null):null),(cljs.core.truth_(show_inbox_QMARK_)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li.dismiss.top-rounded.bottom-rounded","li.dismiss.top-rounded.bottom-rounded",1616835942),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (p1__46028_SHARP_){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.more-menu","showing-menu","oc.web.components.ui.more-menu/showing-menu",-748425394).cljs$core$IFn$_invoke$arity$1(s),false);

if(cljs.core.fn_QMARK_(will_close)){
(will_close.cljs$core$IFn$_invoke$arity$0 ? will_close.cljs$core$IFn$_invoke$arity$0() : will_close.call(null));
} else {
}

oc.web.actions.activity.inbox_dismiss(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entity_data));

if(cljs.core.seq(current_activity_id)){
return oc.web.actions.nav_sidebar.dismiss_post_modal(p1__46028_SHARP_);
} else {
return null;
}
})], null),"Dismiss"], null):null)], null):null)),(cljs.core.truth_((function (){var and__4115__auto__ = external_share;
if(cljs.core.truth_(and__4115__auto__)){
return share_link;
} else {
return and__4115__auto__;
}
})())?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.more-menu-share-bt","button.mlb-reset.more-menu-share-bt",1322908813),cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"ref","ref",1289896967),new cljs.core.Keyword(null,"data-container","data-container",1473653353),new cljs.core.Keyword(null,"data-delay","data-delay",1974747786),new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"on-click","on-click",1632826543),new cljs.core.Keyword(null,"title","title",636505583),new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.Keyword(null,"data-placement","data-placement",166529525),new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687)],["tile-menu-share-bt","body","{\"show\":\"100\", \"hide\":\"0\"}","button",(function (){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.more-menu","showing-menu","oc.web.components.ui.more-menu/showing-menu",-748425394).cljs$core$IFn$_invoke$arity$1(s),false);

if(cljs.core.fn_QMARK_(will_close)){
(will_close.cljs$core$IFn$_invoke$arity$0 ? will_close.cljs$core$IFn$_invoke$arity$0() : will_close.call(null));
} else {
}

return oc.web.actions.activity.activity_share_show.cljs$core$IFn$_invoke$arity$variadic(entity_data,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([share_container_id], 0));
}),"Share",(cljs.core.truth_((function (){var or__4126__auto__ = follow_link;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = unfollow_link;
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
var and__4115__auto__ = external_bookmark;
if(cljs.core.truth_(and__4115__auto__)){
var or__4126__auto____$2 = add_bookmark_link;
if(cljs.core.truth_(or__4126__auto____$2)){
return or__4126__auto____$2;
} else {
return remove_bookmark_link;
}
} else {
return and__4115__auto__;
}
}
}
})())?"has-next-bt":null),(function (){var or__4126__auto__ = tooltip_position;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "top";
}
})(),((is_mobile_QMARK_)?"":"tooltip")])], null):null),(cljs.core.truth_(external_follow)?(cljs.core.truth_(follow_link)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.more-menu-entry-follow-bt","button.mlb-reset.more-menu-entry-follow-bt",631030122),cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.Keyword(null,"ref","ref",1289896967),new cljs.core.Keyword(null,"data-container","data-container",1473653353),new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"on-click","on-click",1632826543),new cljs.core.Keyword(null,"title","title",636505583),new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.Keyword(null,"data-placement","data-placement",166529525),new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687)],["more-menu-entry-follow-bt","more-menu-entry-follow-bt","body","button",(function (){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.more-menu","showing-menu","oc.web.components.ui.more-menu/showing-menu",-748425394).cljs$core$IFn$_invoke$arity$1(s),false);

if(cljs.core.fn_QMARK_(will_close)){
(will_close.cljs$core$IFn$_invoke$arity$0 ? will_close.cljs$core$IFn$_invoke$arity$0() : will_close.call(null));
} else {
}

return oc.web.actions.activity.entry_follow(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entity_data));
}),"Get notified about new post activity",(cljs.core.truth_((function (){var and__4115__auto__ = external_bookmark;
if(cljs.core.truth_(and__4115__auto__)){
var or__4126__auto__ = add_bookmark_link;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return remove_bookmark_link;
}
} else {
return and__4115__auto__;
}
})())?"has-next-bt":null),(function (){var or__4126__auto__ = tooltip_position;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "top";
}
})(),((is_mobile_QMARK_)?"":"tooltip")])], null):(cljs.core.truth_(unfollow_link)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.more-menu-entry-unfollow-bt","button.mlb-reset.more-menu-entry-unfollow-bt",900039927),cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.Keyword(null,"ref","ref",1289896967),new cljs.core.Keyword(null,"data-container","data-container",1473653353),new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"on-click","on-click",1632826543),new cljs.core.Keyword(null,"title","title",636505583),new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.Keyword(null,"data-placement","data-placement",166529525),new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687)],["more-menu-entry-unfollow-bt","more-menu-entry-unfollow-bt","body","button",(function (){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.more-menu","showing-menu","oc.web.components.ui.more-menu/showing-menu",-748425394).cljs$core$IFn$_invoke$arity$1(s),false);

if(cljs.core.fn_QMARK_(will_close)){
(will_close.cljs$core$IFn$_invoke$arity$0 ? will_close.cljs$core$IFn$_invoke$arity$0() : will_close.call(null));
} else {
}

return oc.web.actions.activity.entry_unfollow(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entity_data));
}),"Don't show replies to this update",(cljs.core.truth_((function (){var and__4115__auto__ = external_bookmark;
if(cljs.core.truth_(and__4115__auto__)){
var or__4126__auto__ = add_bookmark_link;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return remove_bookmark_link;
}
} else {
return and__4115__auto__;
}
})())?"has-next-bt":null),(function (){var or__4126__auto__ = tooltip_position;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "top";
}
})(),((is_mobile_QMARK_)?"":"tooltip")])], null):null)):null),(cljs.core.truth_(external_bookmark)?(cljs.core.truth_(remove_bookmark_link)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.more-menu-remove-bookmark-bt","button.mlb-reset.more-menu-remove-bookmark-bt",73265820),new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"type","type",1174270348),"button",new cljs.core.Keyword(null,"ref","ref",1289896967),"more-menu-remove-bookmark-bt",new cljs.core.Keyword(null,"class","class",-2030961996),(cljs.core.truth_(show_inbox_QMARK_)?"has-next-bt":null),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.more-menu","showing-menu","oc.web.components.ui.more-menu/showing-menu",-748425394).cljs$core$IFn$_invoke$arity$1(s),false);

if(cljs.core.fn_QMARK_(will_close)){
(will_close.cljs$core$IFn$_invoke$arity$0 ? will_close.cljs$core$IFn$_invoke$arity$0() : will_close.call(null));
} else {
}

return oc.web.actions.activity.remove_bookmark(entity_data,remove_bookmark_link);
}),new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687),((is_mobile_QMARK_)?"":"tooltip"),new cljs.core.Keyword(null,"data-placement","data-placement",166529525),(function (){var or__4126__auto__ = tooltip_position;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "top";
}
})(),new cljs.core.Keyword(null,"data-container","data-container",1473653353),"body",new cljs.core.Keyword(null,"title","title",636505583),"Remove bookmark"], null),remove_bookmark_title], null):(cljs.core.truth_(add_bookmark_link)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.more-menu-add-bookmark-bt-container","div.more-menu-add-bookmark-bt-container",-1831620848),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.more-menu-add-bookmark-bt","button.mlb-reset.more-menu-add-bookmark-bt",-1381183411),new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"type","type",1174270348),"button",new cljs.core.Keyword(null,"ref","ref",1289896967),"more-menu-add-bookmark-bt",new cljs.core.Keyword(null,"class","class",-2030961996),(cljs.core.truth_(show_inbox_QMARK_)?"has-next-bt":null),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.more-menu","showing-menu","oc.web.components.ui.more-menu/showing-menu",-748425394).cljs$core$IFn$_invoke$arity$1(s),false);

if(cljs.core.fn_QMARK_(will_close)){
(will_close.cljs$core$IFn$_invoke$arity$0 ? will_close.cljs$core$IFn$_invoke$arity$0() : will_close.call(null));
} else {
}

return oc.web.actions.activity.add_bookmark(entity_data,add_bookmark_link);
}),new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687),((is_mobile_QMARK_)?"":"tooltip"),new cljs.core.Keyword(null,"data-placement","data-placement",166529525),(function (){var or__4126__auto__ = tooltip_position;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "top";
}
})(),new cljs.core.Keyword(null,"data-container","data-container",1473653353),"body",new cljs.core.Keyword(null,"title","title",636505583),"Bookmark"], null)], null)], null):null)):null),(cljs.core.truth_(show_inbox_QMARK_)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.more-menu-inbox-dismiss-bt","button.mlb-reset.more-menu-inbox-dismiss-bt",810145671),new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"type","type",1174270348),"button",new cljs.core.Keyword(null,"ref","ref",1289896967),"more-menu-inbox-dismiss-bt",new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (p1__46029_SHARP_){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.more-menu","showing-menu","oc.web.components.ui.more-menu/showing-menu",-748425394).cljs$core$IFn$_invoke$arity$1(s),false);

if(cljs.core.fn_QMARK_(will_close)){
(will_close.cljs$core$IFn$_invoke$arity$0 ? will_close.cljs$core$IFn$_invoke$arity$0() : will_close.call(null));
} else {
}

oc.web.actions.activity.inbox_dismiss(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entity_data));

if(cljs.core.seq(current_activity_id)){
return oc.web.actions.nav_sidebar.dismiss_post_modal(p1__46029_SHARP_);
} else {
return null;
}
}),new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687),((is_mobile_QMARK_)?"":"tooltip"),new cljs.core.Keyword(null,"data-placement","data-placement",166529525),(function (){var or__4126__auto__ = tooltip_position;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "top";
}
})(),new cljs.core.Keyword(null,"data-container","data-container",1473653353),"body",new cljs.core.Keyword(null,"title","title",636505583),"Dismiss"], null)], null):null)], null):null));
}),new cljs.core.PersistentVector(null, 9, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,rum.core.static$,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"route","route",329891309)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.more-menu","showing-menu","oc.web.components.ui.more-menu/showing-menu",-748425394)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.more-menu","move-activity","oc.web.components.ui.more-menu/move-activity",411327013)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.more-menu","can-unmount","oc.web.components.ui.more-menu/can-unmount",1366328316)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.more-menu","last-force-show-menu","oc.web.components.ui.more-menu/last-force-show-menu",-1938312626)),oc.web.mixins.ui.on_click_out.cljs$core$IFn$_invoke$arity$2("more-menu",(function (s,e){
var temp__5735__auto___46037 = new cljs.core.Keyword(null,"will-close","will-close",1889428842).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s)));
if(cljs.core.truth_(temp__5735__auto___46037)){
var will_close_46038 = temp__5735__auto___46037;
if(cljs.core.fn_QMARK_(will_close_46038)){
(will_close_46038.cljs$core$IFn$_invoke$arity$0 ? will_close_46038.cljs$core$IFn$_invoke$arity$0() : will_close_46038.call(null));
} else {
}
} else {
}

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.more-menu","showing-menu","oc.web.components.ui.more-menu/showing-menu",-748425394).cljs$core$IFn$_invoke$arity$1(s),false);
})),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
$("[data-toggle=\"tooltip\"]",rum.core.dom_node(s)).tooltip();

return s;
}),new cljs.core.Keyword(null,"will-update","will-update",328062998),(function (s){
var next_force_show_menu_46039 = new cljs.core.Keyword(null,"force-show-menu","force-show-menu",1246398041).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s)));
if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.more-menu","last-force-show-menu","oc.web.components.ui.more-menu/last-force-show-menu",-1938312626).cljs$core$IFn$_invoke$arity$1(s)),next_force_show_menu_46039)){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.more-menu","last-force-show-menu","oc.web.components.ui.more-menu/last-force-show-menu",-1938312626).cljs$core$IFn$_invoke$arity$1(s),next_force_show_menu_46039);

if(cljs.core.truth_(next_force_show_menu_46039)){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.more-menu","can-unmount","oc.web.components.ui.more-menu/can-unmount",1366328316).cljs$core$IFn$_invoke$arity$1(s),false);

oc.web.lib.utils.after((1000),(function (){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.more-menu","can-unmount","oc.web.components.ui.more-menu/can-unmount",1366328316).cljs$core$IFn$_invoke$arity$1(s),true);
}));
} else {
}
} else {
}

return s;
}),new cljs.core.Keyword(null,"did-update","did-update",-2143702256),(function (s){
$("[data-toggle=\"tooltip\"]",rum.core.dom_node(s)).each((function (p1__46024_SHARP_,p2__46023_SHARP_){
var G__46036 = $(p2__46023_SHARP_);
G__46036.tooltip("fixTitle");

G__46036.tooltip("hide");

return G__46036;
}));

return s;
})], null)], null),"more-menu");

//# sourceMappingURL=oc.web.components.ui.more_menu.js.map
