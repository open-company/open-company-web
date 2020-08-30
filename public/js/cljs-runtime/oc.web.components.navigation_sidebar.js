goog.provide('oc.web.components.navigation_sidebar');
oc.web.components.navigation_sidebar.drafts_board_prefix = [cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.default_drafts_board)),"-"].join('');
oc.web.components.navigation_sidebar.navigation_sidebar = rum.core.build_defcs((function (s){
var org_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"org-data","org-data",96720321));
var board_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"board-data","board-data",1372958925));
var current_user_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915));
var change_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"change-data","change-data",2068475383));
var org_slug = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"org-slug","org-slug",-726595051));
var current_board_slug = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"board-slug","board-slug",99003663));
var current_contributions_id = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"contributions-id","contributions-id",-67679488));
var show_invite_box = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"show-invite-box","show-invite-box",1572056533));
var filtered_change_data = cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__45930_SHARP_){
return (((!(clojure.string.starts_with_QMARK_(cljs.core.first(p1__45930_SHARP_),oc.web.components.navigation_sidebar.drafts_board_prefix)))) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(p1__45930_SHARP_,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(org_data))));
}),change_data));
var left_navigation_sidebar_width = (oc.web.lib.responsive.left_navigation_sidebar_width - (20));
var all_boards = new cljs.core.Keyword(null,"boards","boards",1912049694).cljs$core$IFn$_invoke$arity$1(org_data);
var user_is_part_of_the_team_QMARK_ = new cljs.core.Keyword(null,"member?","member?",486668360).cljs$core$IFn$_invoke$arity$1(org_data);
var is_replies = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug),new cljs.core.Keyword(null,"replies","replies",-1389888974));
var is_following = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug),new cljs.core.Keyword(null,"following","following",-2049193617));
var is_drafts_board = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(current_board_slug,oc.web.lib.utils.default_drafts_board_slug);
var is_topics = (function (){var or__4126__auto__ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug),new cljs.core.Keyword(null,"topics","topics",625768208));
if(or__4126__auto__){
return or__4126__auto__;
} else {
var and__4115__auto__ = current_board_slug;
if(cljs.core.truth_(and__4115__auto__)){
return ((cljs.core.not(oc.web.dispatcher.is_container_QMARK_(current_board_slug))) && ((!(is_drafts_board))));
} else {
return and__4115__auto__;
}
}
})();
var is_bookmarks = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug),new cljs.core.Keyword(null,"bookmarks","bookmarks",1877375283));
var is_contributions = cljs.core.seq(current_contributions_id);
var is_self_profile_QMARK_ = ((is_contributions) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(current_contributions_id,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(current_user_data))));
var create_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"create"], 0));
var drafts_board = cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__45931_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(p1__45931_SHARP_),oc.web.lib.utils.default_drafts_board_slug);
}),all_boards));
var drafts_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(drafts_board),"self"], 0));
var show_following = (function (){var and__4115__auto__ = user_is_part_of_the_team_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"following"], 0));
} else {
return and__4115__auto__;
}
})();
var show_bookmarks = (function (){var and__4115__auto__ = user_is_part_of_the_team_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"bookmarks"], 0));
} else {
return and__4115__auto__;
}
})();
var show_drafts = (function (){var and__4115__auto__ = user_is_part_of_the_team_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return drafts_link;
} else {
return and__4115__auto__;
}
})();
var show_replies = (function (){var and__4115__auto__ = user_is_part_of_the_team_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"replies"], 0));
} else {
return and__4115__auto__;
}
})();
var show_profile = (function (){var or__4126__auto__ = user_is_part_of_the_team_QMARK_;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return is_contributions;
}
})();
var is_mobile_QMARK_ = oc.web.lib.responsive.is_mobile_size_QMARK_();
var drafts_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"drafts-data","drafts-data",-1247967685));
var all_unread_items = cljs.core.mapcat.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"unread","unread",-1950424572),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.vals(filtered_change_data)], 0));
var following_badge = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"following-badge","following-badge",1108067285));
var replies_badge = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"replies-badge","replies-badge",-112768699));
var is_admin_or_author_QMARK_ = (function (){var G__45938 = new cljs.core.Keyword(null,"role","role",-736691072).cljs$core$IFn$_invoke$arity$1(current_user_data);
var fexpr__45937 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"admin","admin",-1239101627),null,new cljs.core.Keyword(null,"author","author",2111686192),null], null), null);
return (fexpr__45937.cljs$core$IFn$_invoke$arity$1 ? fexpr__45937.cljs$core$IFn$_invoke$arity$1(G__45938) : fexpr__45937.call(null,G__45938));
})();
var show_invite_people_QMARK_ = (function (){var and__4115__auto__ = org_slug;
if(cljs.core.truth_(and__4115__auto__)){
var and__4115__auto____$1 = is_admin_or_author_QMARK_;
if(cljs.core.truth_(and__4115__auto____$1)){
return show_invite_box;
} else {
return and__4115__auto____$1;
}
} else {
return and__4115__auto__;
}
})();
var show_topics = user_is_part_of_the_team_QMARK_;
var show_add_post_tooltip = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"show-add-post-tooltip","show-add-post-tooltip",1769173942));
var cmail_state = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"cmail-state","cmail-state",-747393321));
var show_plus_button_QMARK_ = ((cljs.core.not(is_mobile_QMARK_))?new cljs.core.Keyword(null,"can-compose?","can-compose?",-1069735052).cljs$core$IFn$_invoke$arity$1(org_data):false);
return React.createElement("div",({"onClick": (function (p1__45932_SHARP_){
if(cljs.core.truth_(oc.web.lib.utils.event_inside_QMARK_(p1__45932_SHARP_,rum.core.ref_node(s,new cljs.core.Keyword(null,"left-navigation-sidebar-content","left-navigation-sidebar-content",-1968295181))))){
return null;
} else {
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"mobile-navigation-sidebar","mobile-navigation-sidebar",-1723544081)], null),false], null));
}
}), "ref": "left-navigation-sidebar", "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["left-navigation-sidebar","group",oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"mobile-show-side-panel","mobile-show-side-panel",654629689),org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"mobile-navigation-sidebar","mobile-navigation-sidebar",-1723544081))], null))], null))}),React.createElement("div",({"ref": "left-navigation-sidebar-content", "className": "left-navigation-sidebar-content"}),sablono.interpreter.interpret((cljs.core.truth_(is_mobile_QMARK_)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.left-navigation-sidebar-mobile-header","div.left-navigation-sidebar-mobile-header",-1866620898),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.mobile-close-bt","button.mlb-reset.mobile-close-bt",-2125956810),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"mobile-navigation-sidebar","mobile-navigation-sidebar",-1723544081)], null),false], null));
})], null)], null),(oc.web.components.ui.orgs_dropdown.orgs_dropdown.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.orgs_dropdown.orgs_dropdown.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.orgs_dropdown.orgs_dropdown.call(null))], null):null)),sablono.interpreter.interpret((cljs.core.truth_(show_following)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.left-navigation-sidebar-top","div.left-navigation-sidebar-top",768204463),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a.nav-link.home.hover-item.group","a.nav-link.home.hover-item.group",-441006442),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"item-selected","item-selected",-1038120635),is_following,new cljs.core.Keyword(null,"new","new",-2085437848),following_badge], null)),new cljs.core.Keyword(null,"href","href",-793805698),oc.web.urls.following.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (p1__45933_SHARP_){
return oc.web.actions.nav_sidebar.nav_to_url_BANG_.cljs$core$IFn$_invoke$arity$3(p1__45933_SHARP_,"following",oc.web.urls.following.cljs$core$IFn$_invoke$arity$0());
})], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.nav-link-icon","div.nav-link-icon",857533562)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.nav-link-label","div.nav-link-label",392849776),"Home"], null),(cljs.core.truth_(following_badge)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.unread-dot","span.unread-dot",1360166772)], null):null)], null)], null):null)),sablono.interpreter.interpret((cljs.core.truth_(show_topics)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.left-navigation-sidebar-top","div.left-navigation-sidebar-top",768204463),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a.nav-link.topics.hover-item.group","a.nav-link.topics.hover-item.group",210631027),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"item-selected","item-selected",-1038120635),is_topics], null)),new cljs.core.Keyword(null,"href","href",-793805698),oc.web.urls.unfollowing.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (p1__45934_SHARP_){
return oc.web.actions.nav_sidebar.nav_to_url_BANG_.cljs$core$IFn$_invoke$arity$3(p1__45934_SHARP_,"topics",oc.web.urls.topics.cljs$core$IFn$_invoke$arity$0());
})], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.nav-link-icon","div.nav-link-icon",857533562)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.nav-link-label","div.nav-link-label",392849776),"Explore"], null)], null)], null):null)),sablono.interpreter.interpret((cljs.core.truth_(show_replies)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.left-navigation-sidebar-top","div.left-navigation-sidebar-top",768204463),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),(cljs.core.truth_((function (){var or__4126__auto__ = show_following;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return show_topics;
}
})())?"top-border":null)], null),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a.nav-link.replies-view.hover-item.group","a.nav-link.replies-view.hover-item.group",1206406053),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"item-selected","item-selected",-1038120635),is_replies,new cljs.core.Keyword(null,"new","new",-2085437848),replies_badge], null)),new cljs.core.Keyword(null,"href","href",-793805698),oc.web.urls.replies.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (e){
oc.web.lib.utils.event_stop(e);

return oc.web.actions.nav_sidebar.nav_to_url_BANG_.cljs$core$IFn$_invoke$arity$3(e,"replies",oc.web.urls.replies.cljs$core$IFn$_invoke$arity$0());
})], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.nav-link-icon","div.nav-link-icon",857533562)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.nav-link-label","div.nav-link-label",392849776),"For you"], null),(cljs.core.truth_(replies_badge)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.unread-dot","span.unread-dot",1360166772)], null):null)], null)], null):null)),sablono.interpreter.interpret((cljs.core.truth_(show_profile)?(function (){var contrib_user_id = ((is_contributions)?current_contributions_id:new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(current_user_data));
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.left-navigation-sidebar-top","div.left-navigation-sidebar-top",768204463),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),(cljs.core.truth_((function (){var and__4115__auto__ = (function (){var or__4126__auto__ = show_following;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return show_topics;
}
})();
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(show_replies);
} else {
return and__4115__auto__;
}
})())?"top-border":null)], null),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a.nav-link.profile.hover-item.group","a.nav-link.profile.hover-item.group",1051580138),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"item-selected","item-selected",-1038120635),is_contributions], null)),new cljs.core.Keyword(null,"href","href",-793805698),oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(current_user_data)),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (e){
oc.web.lib.utils.event_stop(e);

return oc.web.actions.nav_sidebar.nav_to_author_BANG_.cljs$core$IFn$_invoke$arity$3(e,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(current_user_data),oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(current_user_data)));
})], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.nav-link-icon","div.nav-link-icon",857533562)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.nav-link-label","div.nav-link-label",392849776),"Profile"], null)], null)], null);
})():null)),sablono.interpreter.interpret((cljs.core.truth_(show_bookmarks)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.left-navigation-sidebar-top","div.left-navigation-sidebar-top",768204463),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),(cljs.core.truth_((function (){var and__4115__auto__ = (function (){var or__4126__auto__ = show_following;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = show_topics;
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
return show_replies;
}
}
})();
if(cljs.core.truth_(and__4115__auto__)){
return ((cljs.core.not(show_replies)) && (cljs.core.not(show_profile)));
} else {
return and__4115__auto__;
}
})())?"top-border":null)], null),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a.nav-link.bookmarks.hover-item.group","a.nav-link.bookmarks.hover-item.group",1652374729),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"item-selected","item-selected",-1038120635),is_bookmarks], null)),new cljs.core.Keyword(null,"href","href",-793805698),oc.web.urls.bookmarks.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (p1__45935_SHARP_){
return oc.web.actions.nav_sidebar.nav_to_url_BANG_.cljs$core$IFn$_invoke$arity$3(p1__45935_SHARP_,"bookmarks",oc.web.urls.bookmarks.cljs$core$IFn$_invoke$arity$0());
})], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.nav-link-icon","div.nav-link-icon",857533562)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.nav-link-label","div.nav-link-label",392849776),"Bookmarks"], null),(((new cljs.core.Keyword(null,"bookmarks-count","bookmarks-count",-405810102).cljs$core$IFn$_invoke$arity$1(org_data) > (0)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.count","span.count",1779679026),new cljs.core.Keyword(null,"bookmarks-count","bookmarks-count",-405810102).cljs$core$IFn$_invoke$arity$1(org_data)], null):null)], null)], null):null)),sablono.interpreter.interpret((cljs.core.truth_(show_drafts)?(function (){var board_url = oc.web.urls.board.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(drafts_board));
var draft_count = (cljs.core.truth_(drafts_data)?cljs.core.count(new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897).cljs$core$IFn$_invoke$arity$1(drafts_data)):new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(drafts_link));
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.left-navigation-sidebar-top","div.left-navigation-sidebar-top",768204463),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),(cljs.core.truth_((function (){var and__4115__auto__ = (function (){var or__4126__auto__ = show_following;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return show_topics;
}
})();
if(cljs.core.truth_(and__4115__auto__)){
return ((cljs.core.not(show_replies)) && (cljs.core.not(show_profile)) && (cljs.core.not(show_bookmarks)));
} else {
return and__4115__auto__;
}
})())?"top-border":null)], null),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a.nav-link.drafts.hover-item.group","a.nav-link.drafts.hover-item.group",-1186227183),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"item-selected","item-selected",-1038120635),(((!(is_following))) && (cljs.core.not(is_topics)) && ((!(is_bookmarks))) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(current_board_slug,new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(drafts_board))))], null)),new cljs.core.Keyword(null,"data-board","data-board",-1470008728),cljs.core.name(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(drafts_board)),new cljs.core.Keyword(null,"key","key",-1516042587),["board-list-",cljs.core.name(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(drafts_board))].join(''),new cljs.core.Keyword(null,"href","href",-793805698),board_url,new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (p1__45936_SHARP_){
return oc.web.actions.nav_sidebar.nav_to_url_BANG_.cljs$core$IFn$_invoke$arity$3(p1__45936_SHARP_,new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(drafts_board),board_url);
})], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.nav-link-icon","div.nav-link-icon",857533562)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.nav-link-label.group","div.nav-link-label.group",95332819),"Drafts"], null),(((draft_count > (0)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.count","span.count",1779679026),draft_count], null):null)], null)], null);
})():null)),sablono.interpreter.interpret((cljs.core.truth_(show_plus_button_QMARK_)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.create-bt","button.mlb-reset.create-bt",-563068069),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.actions.cmail.cmail_fullscreen();
}),new cljs.core.Keyword(null,"disabled","disabled",-1529784218),cljs.core.not(new cljs.core.Keyword(null,"collapsed","collapsed",-628494523).cljs$core$IFn$_invoke$arity$1(cmail_state))], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.copy-text","span.copy-text",-1394507326),"New update"], null)], null):null)),sablono.interpreter.interpret((cljs.core.truth_(show_invite_people_QMARK_)?new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.invite-people-box","div.invite-people-box",-1591214628),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.invite-people-close","button.mlb-reset.invite-people-close",-1742532908),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.actions.user.dismiss_invite_box.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(current_user_data),true);
})], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"label.explore-label","label.explore-label",-1140823755),"Explore Wut together"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.invite-people-bt","button.mlb-reset.invite-people-bt",1176198577),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.actions.nav_sidebar.show_org_settings(new cljs.core.Keyword(null,"invite-picker","invite-picker",1426151962));
})], null),"Invite teammates"], null)], null):null))));
}),new cljs.core.PersistentVector(null, 19, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"org-data","org-data",96720321)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"board-data","board-data",1372958925)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"board-slug","board-slug",99003663)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"contributions-id","contributions-id",-67679488)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"change-data","change-data",2068475383)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"replies-badge","replies-badge",-112768699)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"following-badge","following-badge",1108067285)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"mobile-navigation-sidebar","mobile-navigation-sidebar",-1723544081)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"drafts-data","drafts-data",-1247967685)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"cmail-state","cmail-state",-747393321)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"show-add-post-tooltip","show-add-post-tooltip",1769173942)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"show-invite-box","show-invite-box",1572056533)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.navigation-sidebar","last-mobile-navigation-panel","oc.web.components.navigation-sidebar/last-mobile-navigation-panel",-1510678588)),rum.core.local.cljs$core$IFn$_invoke$arity$2(true,new cljs.core.Keyword("oc.web.components.navigation-sidebar","show-invite-people?","oc.web.components.navigation-sidebar/show-invite-people?",-701488690)),oc.web.mixins.ui.first_render_mixin,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"before-render","before-render",71256781),(function (s){
oc.web.actions.nux.check_nux();

return s;
}),new cljs.core.Keyword(null,"will-update","will-update",328062998),(function (s){
if(cljs.core.truth_(oc.web.lib.responsive.is_mobile_size_QMARK_())){
var mobile_navigation_panel_45946 = cljs.core.boolean$(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"mobile-navigation-sidebar","mobile-navigation-sidebar",-1723544081))));
var last_mobile_navigation_panel_45947 = cljs.core.boolean$(cljs.core.deref(new cljs.core.Keyword("oc.web.components.navigation-sidebar","last-mobile-navigation-panel","oc.web.components.navigation-sidebar/last-mobile-navigation-panel",-1510678588).cljs$core$IFn$_invoke$arity$1(s)));
if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(mobile_navigation_panel_45946,last_mobile_navigation_panel_45947)){
if(mobile_navigation_panel_45946){
oc.web.utils.dom.lock_page_scroll();

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.navigation-sidebar","last-mobile-navigation-panel","oc.web.components.navigation-sidebar/last-mobile-navigation-panel",-1510678588).cljs$core$IFn$_invoke$arity$1(s),true);
} else {
oc.web.utils.dom.unlock_page_scroll();

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.navigation-sidebar","last-mobile-navigation-panel","oc.web.components.navigation-sidebar/last-mobile-navigation-panel",-1510678588).cljs$core$IFn$_invoke$arity$1(s),false);
}
} else {
}
} else {
}

return s;
})], null)], null),"navigation-sidebar");

//# sourceMappingURL=oc.web.components.navigation_sidebar.js.map
