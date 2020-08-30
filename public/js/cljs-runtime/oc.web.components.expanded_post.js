goog.provide('oc.web.components.expanded_post');
oc.web.components.expanded_post.close_expanded_post = (function oc$web$components$expanded_post$close_expanded_post(e){
return oc.web.actions.nav_sidebar.dismiss_post_modal(e);
});
oc.web.components.expanded_post.save_fixed_comment_height_BANG_ = (function oc$web$components$expanded_post$save_fixed_comment_height_BANG_(s){
var cur_height = $(rum.core.ref_node(s,new cljs.core.Keyword(null,"expanded-post-fixed-add-comment","expanded-post-fixed-add-comment",383680572))).outerHeight();
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(new cljs.core.Keyword("oc.web.components.expanded-post","comment-height","oc.web.components.expanded-post/comment-height",-1951379674).cljs$core$IFn$_invoke$arity$1(s)),cur_height)){
return null;
} else {
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.expanded-post","comment-height","oc.web.components.expanded-post/comment-height",-1951379674).cljs$core$IFn$_invoke$arity$1(s),cur_height);
}
});
oc.web.components.expanded_post.win_width = (function oc$web$components$expanded_post$win_width(){
var or__4126__auto__ = document.documentElement.clientWidth;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return window.innerWidth;
}
});
oc.web.components.expanded_post.set_mobile_video_height_BANG_ = (function oc$web$components$expanded_post$set_mobile_video_height_BANG_(s){
if(oc.web.lib.responsive.is_tablet_or_mobile_QMARK_()){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.expanded-post","mobile-video-height","oc.web.components.expanded-post/mobile-video-height",-1370420991).cljs$core$IFn$_invoke$arity$1(s),oc.web.lib.utils.calc_video_height(oc.web.components.expanded_post.win_width()));
} else {
return null;
}
});
oc.web.components.expanded_post.load_comments = (function oc$web$components$expanded_post$load_comments(s,force_QMARK_){
var activity_data = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"activity-data","activity-data",1293689136)));
if(cljs.core.truth_(force_QMARK_)){
return oc.web.utils.activity.get_comments(activity_data);
} else {
return oc.web.utils.activity.get_comments_if_needed.cljs$core$IFn$_invoke$arity$2(activity_data,cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"comments-data","comments-data",1871210833))));
}
});
oc.web.components.expanded_post.save_initial_read_data = (function oc$web$components$expanded_post$save_initial_read_data(s){
var activity_data = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"activity-data","activity-data",1293689136)));
if(cljs.core.truth_((((cljs.core.deref(new cljs.core.Keyword("oc.web.components.expanded-post","initial-last-read-at","oc.web.components.expanded-post/initial-last-read-at",-923623415).cljs$core$IFn$_invoke$arity$1(s)) == null))?(function (){var or__4126__auto__ = new cljs.core.Keyword(null,"last-read-at","last-read-at",-216601930).cljs$core$IFn$_invoke$arity$1(activity_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"unread","unread",-1950424572).cljs$core$IFn$_invoke$arity$1(activity_data);
}
})():false))){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.expanded-post","initial-last-read-at","oc.web.components.expanded-post/initial-last-read-at",-923623415).cljs$core$IFn$_invoke$arity$1(s),(function (){var or__4126__auto__ = new cljs.core.Keyword(null,"last-read-at","last-read-at",-216601930).cljs$core$IFn$_invoke$arity$1(activity_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "";
}
})());
} else {
return null;
}
});
oc.web.components.expanded_post.mark_read = (function oc$web$components$expanded_post$mark_read(s){
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.expanded-post","mark-as-read?","oc.web.components.expanded-post/mark-as-read?",-1298099681).cljs$core$IFn$_invoke$arity$1(s)))){
return oc.web.actions.activity.mark_read.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.deref(new cljs.core.Keyword("oc.web.components.expanded-post","activity-uuid","oc.web.components.expanded-post/activity-uuid",793820766).cljs$core$IFn$_invoke$arity$1(s))], 0));
} else {
return null;
}
});
oc.web.components.expanded_post.big_web_collapse_min_height = (134);
oc.web.components.expanded_post.mobile_collapse_min_height = (160);
oc.web.components.expanded_post.min_body_length_for_truncation = (450);
oc.web.components.expanded_post.check_collapse_post = (function oc$web$components$expanded_post$check_collapse_post(s){
if((cljs.core.deref(new cljs.core.Keyword("oc.web.components.expanded-post","collapse-post","oc.web.components.expanded-post/collapse-post",-556601733).cljs$core$IFn$_invoke$arity$1(s)) == null)){
var is_mobile_QMARK_ = oc.web.lib.responsive.is_mobile_size_QMARK_();
var comparing_height = (cljs.core.truth_(is_mobile_QMARK_)?oc.web.components.expanded_post.mobile_collapse_min_height:oc.web.components.expanded_post.big_web_collapse_min_height);
var activity_data = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"activity-data","activity-data",1293689136)));
var comments_count = new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(activity_data),"comments"], 0)));
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.expanded-post","collapse-post","oc.web.components.expanded-post/collapse-post",-556601733).cljs$core$IFn$_invoke$arity$1(s),(((cljs.core.count(new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(activity_data)) > oc.web.components.expanded_post.min_body_length_for_truncation))?((cljs.core.not(cljs.core.seq(new cljs.core.Keyword(null,"polls","polls",-580623582).cljs$core$IFn$_invoke$arity$1(activity_data))))?(function (){var and__4115__auto__ = new cljs.core.Keyword(null,"member?","member?",486668360).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"org-data","org-data",96720321))));
if(cljs.core.truth_(and__4115__auto__)){
return ((cljs.core.not(new cljs.core.Keyword(null,"unread","unread",-1950424572).cljs$core$IFn$_invoke$arity$1(activity_data))) && ((comments_count > (0))));
} else {
return and__4115__auto__;
}
})():false):false));
} else {
return null;
}
});
oc.web.components.expanded_post.add_comment_prefix = "main-comment";
oc.web.components.expanded_post.expanded_post = rum.core.build_defcs((function (s){
var activity_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"activity-data","activity-data",1293689136));
var comments_drv = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"comments-data","comments-data",1871210833));
var _add_comment_focus = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"add-comment-focus","add-comment-focus",-452934461));
var _users_info_hover = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"users-info-hover","users-info-hover",-941434570));
var _follow_publishers_list = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"follow-publishers-list","follow-publishers-list",-374150342));
var _followers_publishers_count = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"followers-publishers-count","followers-publishers-count",-692976579));
var activities_read = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"activities-read","activities-read",2125722631));
var read_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(activities_read,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));
var editable_boards = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"editable-boards","editable-boards",1897056658));
var comments_data = oc.web.utils.activity.activity_comments(activity_data,comments_drv);
var dom_element_id = ["expanded-post-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data))].join('');
var dom_node_class = ["expanded-post-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data))].join('');
var publisher = new cljs.core.Keyword(null,"publisher","publisher",-153364540).cljs$core$IFn$_invoke$arity$1(activity_data);
var is_mobile_QMARK_ = oc.web.lib.responsive.is_mobile_size_QMARK_();
var route = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"route","route",329891309));
var org_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"org-data","org-data",96720321));
var has_video = cljs.core.seq(new cljs.core.Keyword(null,"fixed-video-id","fixed-video-id",-1380335259).cljs$core$IFn$_invoke$arity$1(activity_data));
var uploading_video = oc.web.dispatcher.uploading_video_data.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"video-id","video-id",2132630536).cljs$core$IFn$_invoke$arity$1(activity_data));
var current_user_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915));
var current_user_id = new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(current_user_data);
var is_publisher_QMARK_ = new cljs.core.Keyword(null,"publisher?","publisher?",30448149).cljs$core$IFn$_invoke$arity$1(publisher);
var video_player_show = (function (){var and__4115__auto__ = is_publisher_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return uploading_video;
} else {
return and__4115__auto__;
}
})();
var video_size = ((has_video)?(cljs.core.truth_(is_mobile_QMARK_)?new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"width","width",-384071477),oc.web.components.expanded_post.win_width(),new cljs.core.Keyword(null,"height","height",1025178622),cljs.core.deref(new cljs.core.Keyword("oc.web.components.expanded-post","mobile-video-height","oc.web.components.expanded-post/mobile-video-height",-1370420991).cljs$core$IFn$_invoke$arity$1(s))], null):new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"width","width",-384071477),(638),new cljs.core.Keyword(null,"height","height",1025178622),oc.web.lib.utils.calc_video_height((638))], null)):null);
var expand_image_src = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"expand-image-src","expand-image-src",-899766588));
var assigned_follow_up_data = cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__46247_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"assignee","assignee",-1242457026).cljs$core$IFn$_invoke$arity$1(p1__46247_SHARP_)),current_user_id);
}),new cljs.core.Keyword(null,"follow-ups","follow-ups",-1947583003).cljs$core$IFn$_invoke$arity$1(activity_data)));
var add_comment_force_update_STAR_ = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"add-comment-force-update","add-comment-force-update",1376707794));
var add_comment_force_update = cljs.core.get.cljs$core$IFn$_invoke$arity$2(add_comment_force_update_STAR_,oc.web.dispatcher.add_comment_string_key.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data)));
var mobile_more_menu_el = document.querySelector("div.mobile-more-menu");
var show_mobile_menu_QMARK_ = (function (){var and__4115__auto__ = is_mobile_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return mobile_more_menu_el;
} else {
return and__4115__auto__;
}
})();
var more_menu_comp = (function (){
var G__46251 = cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"show-delete?","show-delete?",-753527136),new cljs.core.Keyword(null,"show-edit?","show-edit?",-1476204765),new cljs.core.Keyword(null,"external-share","external-share",-2131927863),new cljs.core.Keyword(null,"will-close","will-close",1889428842),new cljs.core.Keyword(null,"share-container-id","share-container-id",775325900),new cljs.core.Keyword(null,"entity-data","entity-data",1608056141),new cljs.core.Keyword(null,"editable-boards","editable-boards",1897056658),new cljs.core.Keyword(null,"show-move?","show-move?",274288117),new cljs.core.Keyword(null,"tooltip-position","tooltip-position",936197013),new cljs.core.Keyword(null,"external-follow","external-follow",158310616),new cljs.core.Keyword(null,"force-show-menu","force-show-menu",1246398041),new cljs.core.Keyword(null,"external-bookmark","external-bookmark",-1631278980),new cljs.core.Keyword(null,"mobile-tray-menu","mobile-tray-menu",-1905495105)],[true,true,cljs.core.not(is_mobile_QMARK_),(cljs.core.truth_(show_mobile_menu_QMARK_)?(function (){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.expanded-post","force-show-menu","oc.web.components.expanded-post/force-show-menu",1747514089).cljs$core$IFn$_invoke$arity$1(s),false);
}):null),dom_element_id,activity_data,editable_boards,cljs.core.not(is_mobile_QMARK_),"top",cljs.core.not(is_mobile_QMARK_),(function (){var and__4115__auto__ = is_mobile_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.deref(new cljs.core.Keyword("oc.web.components.expanded-post","force-show-menu","oc.web.components.expanded-post/force-show-menu",1747514089).cljs$core$IFn$_invoke$arity$1(s));
} else {
return and__4115__auto__;
}
})(),cljs.core.not(is_mobile_QMARK_),show_mobile_menu_QMARK_]);
return (oc.web.components.ui.more_menu.more_menu.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.more_menu.more_menu.cljs$core$IFn$_invoke$arity$1(G__46251) : oc.web.components.ui.more_menu.more_menu.call(null,G__46251));
});
var muted_post_QMARK_ = cljs.core.map_QMARK_(oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(activity_data),"follow"], 0)));
var comments_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(activity_data),"comments"], 0));
var bookmarked_QMARK_ = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"must-see","must-see",-2009706697).cljs$core$IFn$_invoke$arity$1(activity_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"bookmarked-at","bookmarked-at",1451784060).cljs$core$IFn$_invoke$arity$1(activity_data);
}
})();
return React.createElement("div",({"data-new-comments-count": new cljs.core.Keyword(null,"new-comments-count","new-comments-count",46784695).cljs$core$IFn$_invoke$arity$1(activity_data), "data-unseen-comments": new cljs.core.Keyword(null,"unseen-comments","unseen-comments",-793262869).cljs$core$IFn$_invoke$arity$1(activity_data), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["expanded-post",dom_node_class], null)), "style": ({"paddingBottom": [cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(new cljs.core.Keyword("oc.web.components.expanded-post","comment-height","oc.web.components.expanded-post/comment-height",-1951379674).cljs$core$IFn$_invoke$arity$1(s))),"px"].join('')}), "id": dom_element_id, "data-initial-last-read-at": cljs.core.deref(new cljs.core.Keyword("oc.web.components.expanded-post","initial-last-read-at","oc.web.components.expanded-post/initial-last-read-at",-923623415).cljs$core$IFn$_invoke$arity$1(s)), "data-last-activity-at": new cljs.core.Keyword(null,"last-activity-at","last-activity-at",670511998).cljs$core$IFn$_invoke$arity$1(activity_data), "data-last-read-at": new cljs.core.Keyword(null,"last-read-at","last-read-at",-216601930).cljs$core$IFn$_invoke$arity$1(activity_data), "onClick": (function (p1__46248_SHARP_){
if(cljs.core.truth_(oc.web.lib.utils.event_inside_QMARK_(p1__46248_SHARP_,rum.core.ref_node(s,new cljs.core.Keyword(null,"expanded-post-container","expanded-post-container",1027679776))))){
return null;
} else {
return oc.web.components.expanded_post.close_expanded_post(p1__46248_SHARP_);
}
})}),sablono.interpreter.interpret((function (){var G__46254 = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"src","src",-1651076051),expand_image_src], null);
return (oc.web.components.ui.image_modal.image_modal.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.image_modal.image_modal.cljs$core$IFn$_invoke$arity$1(G__46254) : oc.web.components.ui.image_modal.image_modal.call(null,G__46254));
})()),React.createElement("div",({"ref": "expanded-post-container", "className": "expanded-post-container"}),React.createElement("div",({"className": "activity-share-container"})),React.createElement("div",({"className": "expanded-post-header group"}),React.createElement("button",({"onClick": oc.web.components.expanded_post.close_expanded_post, "data-toggle": (cljs.core.truth_(is_mobile_QMARK_)?null:"tooltip"), "data-placement": "top", "title": "Close", "className": "mlb-reset back-to-board"})),(function (){var attrs46257 = (function (){var G__46268 = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"activity-data","activity-data",1293689136),activity_data,new cljs.core.Keyword(null,"user-avatar?","user-avatar?",-1162947881),true,new cljs.core.Keyword(null,"activity-board?","activity-board?",-1568829907),true,new cljs.core.Keyword(null,"user-hover?","user-hover?",-1460761243),true,new cljs.core.Keyword(null,"board-hover?","board-hover?",1064995646),true,new cljs.core.Keyword(null,"current-user-id","current-user-id",-2013633451),current_user_id], null);
return (oc.web.components.ui.post_authorship.post_authorship.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.post_authorship.post_authorship.cljs$core$IFn$_invoke$arity$1(G__46268) : oc.web.components.ui.post_authorship.post_authorship.call(null,G__46268));
})();
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46257))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["expanded-post-header-center"], null)], null),attrs46257], 0))):({"className": "expanded-post-header-center"})),((cljs.core.map_QMARK_(attrs46257))?new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [React.createElement("div",({"className": "separator-dot"})),React.createElement("time",({"dateTime": new cljs.core.Keyword(null,"published-at","published-at",249684621).cljs$core$IFn$_invoke$arity$1(activity_data), "data-toggle": (cljs.core.truth_(is_mobile_QMARK_)?null:"tooltip"), "data-placement": "top", "data-container": "body", "data-delay": "{\"show\":\"1000\", \"hide\":\"0\"}", "data-title": oc.web.lib.utils.activity_date_tooltip(activity_data)}),sablono.interpreter.interpret(oc.web.lib.utils.foc_date_time(new cljs.core.Keyword(null,"published-at","published-at",249684621).cljs$core$IFn$_invoke$arity$1(activity_data)))),sablono.interpreter.interpret((cljs.core.truth_(bookmarked_QMARK_)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.separator-dot","div.separator-dot",2056473245)], null):null)),sablono.interpreter.interpret((cljs.core.truth_(bookmarked_QMARK_)?(cljs.core.truth_(new cljs.core.Keyword(null,"bookmarked-at","bookmarked-at",1451784060).cljs$core$IFn$_invoke$arity$1(activity_data))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.bookmark-tag","div.bookmark-tag",2075036244)], null):new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.must-see-tag","div.must-see-tag",-2077968396)], null)):null)),sablono.interpreter.interpret(((muted_post_QMARK_)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.separator-dot.muted-dot","div.separator-dot.muted-dot",-497406868)], null):null)),sablono.interpreter.interpret(((muted_post_QMARK_)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.muted-activity","div.muted-activity",57557738),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687),(cljs.core.truth_(is_mobile_QMARK_)?null:"tooltip"),new cljs.core.Keyword(null,"data-placement","data-placement",166529525),"top",new cljs.core.Keyword(null,"title","title",636505583),"Muted"], null)], null):null))], null):new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46257),React.createElement("div",({"className": "separator-dot"})),React.createElement("time",({"dateTime": new cljs.core.Keyword(null,"published-at","published-at",249684621).cljs$core$IFn$_invoke$arity$1(activity_data), "data-toggle": (cljs.core.truth_(is_mobile_QMARK_)?null:"tooltip"), "data-placement": "top", "data-container": "body", "data-delay": "{\"show\":\"1000\", \"hide\":\"0\"}", "data-title": oc.web.lib.utils.activity_date_tooltip(activity_data)}),sablono.interpreter.interpret(oc.web.lib.utils.foc_date_time(new cljs.core.Keyword(null,"published-at","published-at",249684621).cljs$core$IFn$_invoke$arity$1(activity_data)))),sablono.interpreter.interpret((cljs.core.truth_(bookmarked_QMARK_)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.separator-dot","div.separator-dot",2056473245)], null):null)),sablono.interpreter.interpret((cljs.core.truth_(bookmarked_QMARK_)?(cljs.core.truth_(new cljs.core.Keyword(null,"bookmarked-at","bookmarked-at",1451784060).cljs$core$IFn$_invoke$arity$1(activity_data))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.bookmark-tag","div.bookmark-tag",2075036244)], null):new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.must-see-tag","div.must-see-tag",-2077968396)], null)):null)),sablono.interpreter.interpret(((muted_post_QMARK_)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.separator-dot.muted-dot","div.separator-dot.muted-dot",-497406868)], null):null)),sablono.interpreter.interpret(((muted_post_QMARK_)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.muted-activity","div.muted-activity",57557738),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687),(cljs.core.truth_(is_mobile_QMARK_)?null:"tooltip"),new cljs.core.Keyword(null,"data-placement","data-placement",166529525),"top",new cljs.core.Keyword(null,"title","title",636505583),"Muted"], null)], null):null))], null)));
})(),(cljs.core.truth_(show_mobile_menu_QMARK_)?sablono.interpreter.interpret(rum.core.portal(more_menu_comp(),mobile_more_menu_el)):sablono.interpreter.interpret(more_menu_comp())),React.createElement("button",({"onClick": (function (){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("oc.web.components.expanded-post","force-show-menu","oc.web.components.expanded-post/force-show-menu",1747514089).cljs$core$IFn$_invoke$arity$1(s),cljs.core.not);
}), "className": "mlb-reset mobile-more-bt"}))),sablono.interpreter.interpret(((cljs.core.not(activity_data))?(oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.small_loading.small_loading.call(null)):new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.expanded-post-container-inner","div.expanded-post-container-inner",-200968650),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.expanded-post-headline","div.expanded-post-headline",-1008338383),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.hide_class], null),new cljs.core.Keyword(null,"headline","headline",-157157727).cljs$core$IFn$_invoke$arity$1(activity_data)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.expanded-post-body.oc-mentions.oc-mentions-hover","div.expanded-post-body.oc-mentions.oc-mentions-hover",-529772280),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"ref","ref",1289896967),"post-body",new cljs.core.Keyword(null,"on-click","on-click",1632826543),(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.expanded-post","collapse-post","oc.web.components.expanded-post/collapse-post",-556601733).cljs$core$IFn$_invoke$arity$1(s)))?(function (){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.expanded-post","collapse-post","oc.web.components.expanded-post/collapse-post",-556601733).cljs$core$IFn$_invoke$arity$1(s),false);
}):null),new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.class_set(cljs.core.PersistentArrayMap.createAsIfByAssoc([oc.web.lib.utils.hide_class,true,new cljs.core.Keyword(null,"collapsed","collapsed",-628494523),cljs.core.deref(new cljs.core.Keyword("oc.web.components.expanded-post","collapse-post","oc.web.components.expanded-post/collapse-post",-556601733).cljs$core$IFn$_invoke$arity$1(s))])),new cljs.core.Keyword(null,"dangerouslySetInnerHTML","dangerouslySetInnerHTML",-554971138),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"__html","__html",674048345),new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(activity_data)], null)], null)], null),(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.expanded-post","collapse-post","oc.web.components.expanded-post/collapse-post",-556601733).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.expand-button","button.mlb-reset.expand-button",-1289052525),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.expanded-post","collapse-post","oc.web.components.expanded-post/collapse-post",-556601733).cljs$core$IFn$_invoke$arity$1(s),false);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.expand-button-inner","div.expand-button-inner",932407102),"View more"], null)], null):null),((cljs.core.seq(new cljs.core.Keyword(null,"polls","polls",-580623582).cljs$core$IFn$_invoke$arity$1(activity_data)))?(function (){var G__46269 = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"polls-data","polls-data",-975205110),new cljs.core.Keyword(null,"polls","polls",-580623582).cljs$core$IFn$_invoke$arity$1(activity_data),new cljs.core.Keyword(null,"editing?","editing?",1646440800),false,new cljs.core.Keyword(null,"current-user-id","current-user-id",-2013633451),current_user_id,new cljs.core.Keyword(null,"container-selector","container-selector",6506114),"div.expanded-post",new cljs.core.Keyword(null,"activity-data","activity-data",1293689136),activity_data,new cljs.core.Keyword(null,"dispatch-key","dispatch-key",733619510),oc.web.dispatcher.activity_key(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data))], null);
return (oc.web.components.ui.poll.polls_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.poll.polls_wrapper.cljs$core$IFn$_invoke$arity$1(G__46269) : oc.web.components.ui.poll.polls_wrapper.call(null,G__46269));
})():null),(function (){var G__46270 = new cljs.core.Keyword(null,"attachments","attachments",-1535547830).cljs$core$IFn$_invoke$arity$1(activity_data);
return (oc.web.components.ui.stream_attachments.stream_attachments.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.stream_attachments.stream_attachments.cljs$core$IFn$_invoke$arity$1(G__46270) : oc.web.components.ui.stream_attachments.stream_attachments.call(null,G__46270));
})(),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.expanded-post-footer.group","div.expanded-post-footer.group",-636285265),(function (){var G__46271 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"entity-data","entity-data",1608056141),activity_data,new cljs.core.Keyword(null,"only-thumb?","only-thumb?",959195446),true], null);
return (oc.web.components.reactions.reactions.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.reactions.reactions.cljs$core$IFn$_invoke$arity$1(G__46271) : oc.web.components.reactions.reactions.call(null,G__46271));
})(),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.expanded-post-footer-mobile-group","div.expanded-post-footer-mobile-group",957803256),(cljs.core.truth_(new cljs.core.Keyword(null,"member?","member?",486668360).cljs$core$IFn$_invoke$arity$1(org_data))?(function (){var G__46272 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"entry-data","entry-data",1970939662),activity_data,new cljs.core.Keyword(null,"add-comment-focus-prefix","add-comment-focus-prefix",1635349699),"main-comment",new cljs.core.Keyword(null,"current-activity-id","current-activity-id",-930108529),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data)], null);
return (oc.web.components.ui.comments_summary.foc_comments_summary.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.comments_summary.foc_comments_summary.cljs$core$IFn$_invoke$arity$1(G__46272) : oc.web.components.ui.comments_summary.foc_comments_summary.call(null,G__46272));
})():null)], null),(cljs.core.truth_(new cljs.core.Keyword(null,"member?","member?",486668360).cljs$core$IFn$_invoke$arity$1(org_data))?(function (){var G__46273 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"activity-data","activity-data",1293689136),activity_data,new cljs.core.Keyword(null,"read-data","read-data",-715156010),read_data], null);
return (oc.web.components.ui.wrt.wrt_count.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.wrt.wrt_count.cljs$core$IFn$_invoke$arity$1(G__46273) : oc.web.components.ui.wrt.wrt_count.call(null,G__46273));
})():null)], null),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.expanded-post-comments.group","div.expanded-post-comments.group",-1281886502),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),(cljs.core.truth_(oc.shared.useragent.android_QMARK_)?"android":null)], null),(function (){var G__46274 = new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"activity-data","activity-data",1293689136),activity_data,new cljs.core.Keyword(null,"comments-data","comments-data",1871210833),comments_data,new cljs.core.Keyword(null,"loading-comments-count","loading-comments-count",190347930),((cljs.core.seq(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(comments_drv,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data),new cljs.core.Keyword(null,"sorted-comments","sorted-comments",1988882718)], null))))?null:(new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(comments_link) - cljs.core.count(comments_data))),new cljs.core.Keyword(null,"member?","member?",486668360),new cljs.core.Keyword(null,"member?","member?",486668360).cljs$core$IFn$_invoke$arity$1(org_data),new cljs.core.Keyword(null,"last-read-at","last-read-at",-216601930),cljs.core.deref(new cljs.core.Keyword("oc.web.components.expanded-post","initial-last-read-at","oc.web.components.expanded-post/initial-last-read-at",-923623415).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"reply-add-comment-prefix","reply-add-comment-prefix",1123094795),oc.web.components.expanded_post.add_comment_prefix,new cljs.core.Keyword(null,"current-user-id","current-user-id",-2013633451),current_user_id], null);
return (oc.web.components.stream_comments.stream_comments.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.stream_comments.stream_comments.cljs$core$IFn$_invoke$arity$1(G__46274) : oc.web.components.stream_comments.stream_comments.call(null,G__46274));
})(),(cljs.core.truth_(new cljs.core.Keyword(null,"can-comment","can-comment",718623455).cljs$core$IFn$_invoke$arity$1(activity_data))?rum.core.with_key((function (){var G__46275 = new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"activity-data","activity-data",1293689136),activity_data,new cljs.core.Keyword(null,"scroll-after-posting?","scroll-after-posting?",-721807257),true,new cljs.core.Keyword(null,"collapse?","collapse?",720716709),true,new cljs.core.Keyword(null,"internal-max-width","internal-max-width",-816752310),(cljs.core.truth_(is_mobile_QMARK_)?(oc.web.utils.dom.viewport_width() - (((24) + (1)) * (2))):(606)),new cljs.core.Keyword(null,"add-comment-focus-prefix","add-comment-focus-prefix",1635349699),"main-comment"], null);
return (oc.web.components.ui.add_comment.add_comment.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.add_comment.add_comment.cljs$core$IFn$_invoke$arity$1(G__46275) : oc.web.components.ui.add_comment.add_comment.call(null,G__46275));
})(),["expanded-post-add-comment-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data)),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(add_comment_force_update)].join('')):null)], null)], null)))));
}),new cljs.core.PersistentVector(null, 26, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"route","route",329891309)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"org-data","org-data",96720321)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"activity-data","activity-data",1293689136)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"comments-data","comments-data",1871210833)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"add-comment-focus","add-comment-focus",-452934461)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"activities-read","activities-read",2125722631)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"expand-image-src","expand-image-src",-899766588)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"add-comment-force-update","add-comment-force-update",1376707794)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"editable-boards","editable-boards",1897056658)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"users-info-hover","users-info-hover",-941434570)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"follow-publishers-list","follow-publishers-list",-374150342)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"followers-publishers-count","followers-publishers-count",-692976579)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.expanded-post","wh","oc.web.components.expanded-post/wh",541574978)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.expanded-post","comment-height","oc.web.components.expanded-post/comment-height",-1951379674)),rum.core.local.cljs$core$IFn$_invoke$arity$2((0),new cljs.core.Keyword("oc.web.components.expanded-post","mobile-video-height","oc.web.components.expanded-post/mobile-video-height",-1370420991)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.expanded-post","initial-last-read-at","oc.web.components.expanded-post/initial-last-read-at",-923623415)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.expanded-post","activity-uuid","oc.web.components.expanded-post/activity-uuid",793820766)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.expanded-post","force-show-menu","oc.web.components.expanded-post/force-show-menu",1747514089)),rum.core.local.cljs$core$IFn$_invoke$arity$2(true,new cljs.core.Keyword("oc.web.components.expanded-post","mark-as-read?","oc.web.components.expanded-post/mark-as-read?",-1298099681)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.expanded-post","collapse-post","oc.web.components.expanded-post/collapse-post",-556601733)),oc.web.mixins.mention.oc_mentions_hover.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"click?","click?",-1210364665),true], null)], 0)),oc.web.mixins.ui.interactive_images_mixin("div.expanded-post-body"),oc.web.mixins.ui.no_scroll_mixin,new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
oc.web.components.expanded_post.check_collapse_post(s);

oc.web.components.expanded_post.save_initial_read_data(s);

return s;
}),new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
oc.web.components.expanded_post.save_fixed_comment_height_BANG_(s);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.expanded-post","activity-uuid","oc.web.components.expanded-post/activity-uuid",793820766).cljs$core$IFn$_invoke$arity$1(s),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"activity-data","activity-data",1293689136)))));

oc.web.components.expanded_post.load_comments(s,true);

oc.web.components.expanded_post.mark_read(s);

return s;
}),new cljs.core.Keyword(null,"did-remount","did-remount",1362550500),(function (_,s){
oc.web.components.expanded_post.save_initial_read_data(s);

oc.web.components.expanded_post.load_comments(s,false);

return s;
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
oc.web.components.expanded_post.mark_read(s);

oc.web.actions.comment.add_comment_blur(oc.web.utils.comment.add_comment_focus_value.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([oc.web.components.expanded_post.add_comment_prefix,cljs.core.deref(new cljs.core.Keyword("oc.web.components.expanded-post","activity-uuid","oc.web.components.expanded-post/activity-uuid",793820766).cljs$core$IFn$_invoke$arity$1(s))], 0)));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.expanded-post","activity-uuid","oc.web.components.expanded-post/activity-uuid",793820766).cljs$core$IFn$_invoke$arity$1(s),null);

return s;
})], null)], null),"expanded-post");

//# sourceMappingURL=oc.web.components.expanded_post.js.map
