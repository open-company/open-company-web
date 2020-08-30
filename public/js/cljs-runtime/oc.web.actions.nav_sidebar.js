goog.provide('oc.web.actions.nav_sidebar');
oc.web.actions.nav_sidebar.refresh_contributions_data = (function oc$web$actions$nav_sidebar$refresh_contributions_data(author_uuid){
if(cljs.core.truth_(author_uuid)){
return oc.web.actions.contributions.contributions_get.cljs$core$IFn$_invoke$arity$1(author_uuid);
} else {
return null;
}
});
oc.web.actions.nav_sidebar.nav_to_author_BANG_ = (function oc$web$actions$nav_sidebar$nav_to_author_BANG_(var_args){
var G__38758 = arguments.length;
switch (G__38758) {
case 3:
return oc.web.actions.nav_sidebar.nav_to_author_BANG_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 5:
return oc.web.actions.nav_sidebar.nav_to_author_BANG_.cljs$core$IFn$_invoke$arity$5((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.nav_sidebar.nav_to_author_BANG_.cljs$core$IFn$_invoke$arity$3 = (function (e,author_uuid,url){
return oc.web.actions.nav_sidebar.nav_to_author_BANG_.cljs$core$IFn$_invoke$arity$5(e,author_uuid,url,(function (){var or__4126__auto__ = oc.web.dispatcher.route_param.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"back-y","back-y",871917742));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.lib.utils.page_scroll_top();
}
})(),true);
}));

(oc.web.actions.nav_sidebar.nav_to_author_BANG_.cljs$core$IFn$_invoke$arity$5 = (function (e,author_uuid,url,back_y,refresh_QMARK_){
if(cljs.core.truth_((function (){var and__4115__auto__ = e;
if(cljs.core.truth_(and__4115__auto__)){
return e.preventDefault;
} else {
return and__4115__auto__;
}
})())){
e.preventDefault();
} else {
}

if(cljs.core.truth_(oc.shared.useragent.mobile_QMARK_)){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"mobile-navigation-sidebar","mobile-navigation-sidebar",-1723544081)], null),false], null));
} else {
}

return oc.web.lib.utils.after((0),(function (){
var current_path = [cljs.core.str.cljs$core$IFn$_invoke$arity$1(window.location.pathname),cljs.core.str.cljs$core$IFn$_invoke$arity$1(window.location.search)].join('');
var org_slug = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
var sort_type = oc.web.actions.activity.saved_sort_type(org_slug,author_uuid);
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(current_path,url)){
oc.web.actions.routing.post_routing();

return oc.web.actions.user.initial_loading.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([refresh_QMARK_], 0));
} else {
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"routing","routing",1440253662),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"org","org",1495985),org_slug,new cljs.core.Keyword(null,"contributions","contributions",-1280485964),author_uuid,new cljs.core.Keyword(null,"sort-type","sort-type",-2053499504),sort_type,new cljs.core.Keyword(null,"scroll-y","scroll-y",-1381960567),back_y,new cljs.core.Keyword(null,"query-params","query-params",900640534),oc.web.dispatcher.query_params.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"route","route",329891309),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [org_slug,author_uuid,sort_type,"dashboard"], null)], null)], null));

window.history.pushState(({}),document.title,url);

(document.scrollingElement.scrollTop = oc.web.lib.utils.page_scroll_top());

if(cljs.core.truth_(refresh_QMARK_)){
return oc.web.actions.nav_sidebar.refresh_contributions_data(author_uuid);
} else {
return null;
}
}
}));
}));

(oc.web.actions.nav_sidebar.nav_to_author_BANG_.cljs$lang$maxFixedArity = 5);

oc.web.actions.nav_sidebar.current_container_data = (function oc$web$actions$nav_sidebar$current_container_data(){
var board_slug = oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0();
var contributions_id = oc.web.dispatcher.current_contributions_id.cljs$core$IFn$_invoke$arity$0();
if(cljs.core.seq(contributions_id)){
return oc.web.dispatcher.contributions_data.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),contributions_id], 0));
} else {
if(cljs.core.truth_(oc.web.dispatcher.is_container_QMARK_(board_slug))){
return oc.web.dispatcher.container_data.cljs$core$IFn$_invoke$arity$3(cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),board_slug);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(board_slug),new cljs.core.Keyword(null,"topic","topic",-1960480691))){
return null;
} else {
return oc.web.dispatcher.board_data.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),board_slug], 0));

}
}
}
});
oc.web.actions.nav_sidebar.reload_board_data = (function oc$web$actions$nav_sidebar$reload_board_data(){
if(cljs.core.truth_(oc.web.dispatcher.current_activity_id.cljs$core$IFn$_invoke$arity$0())){
return null;
} else {
var board_slug = oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0();
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
var board_data = oc.web.actions.nav_sidebar.current_container_data();
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(board_slug,"inbox")){
return oc.web.actions.activity.inbox_get(org_data);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(board_slug,"replies")){
return oc.web.actions.activity.replies_get.cljs$core$IFn$_invoke$arity$3(org_data,true,null);
} else {
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(board_slug,"all-posts")) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.recently_posted_sort)))){
return oc.web.actions.activity.all_posts_get(org_data);
} else {
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(board_slug,"all-posts")) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.recent_activity_sort)))){
return oc.web.actions.activity.recent_all_posts_get(org_data);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(board_slug,"bookmarks")){
return oc.web.actions.activity.bookmarks_get(org_data);
} else {
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(board_slug,"following")) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.recently_posted_sort)))){
return oc.web.actions.activity.following_get.cljs$core$IFn$_invoke$arity$3(org_data,true,null);
} else {
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(board_slug,"following")) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.recent_activity_sort)))){
return oc.web.actions.activity.recent_following_get.cljs$core$IFn$_invoke$arity$1(org_data);
} else {
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(board_slug,"unfollowing")) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.recently_posted_sort)))){
return oc.web.actions.activity.unfollowing_get(org_data);
} else {
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(board_slug,"unfollowing")) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.recent_activity_sort)))){
return oc.web.actions.activity.recent_unfollowing_get(org_data);
} else {
var fixed_board_data = (function (){var or__4126__auto__ = board_data;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.dispatcher.org_board_data.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([org_data,board_slug], 0));
}
})();
var board_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(fixed_board_data),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["item","self"], null),"GET"], 0));
if(cljs.core.truth_(board_link)){
return oc.web.actions.section.section_get(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(fixed_board_data),board_link);
} else {
return null;
}

}
}
}
}
}
}
}
}
}
}
});
oc.web.actions.nav_sidebar.refresh_board_data = (function oc$web$actions$nav_sidebar$refresh_board_data(){
if(cljs.core.truth_(oc.web.dispatcher.current_activity_id.cljs$core$IFn$_invoke$arity$0())){
return null;
} else {
var current_board_slug = oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0();
var current_contrib_id = oc.web.dispatcher.current_contributions_id.cljs$core$IFn$_invoke$arity$0();
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
var container_data = oc.web.actions.nav_sidebar.current_container_data();
var board_kw = cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug);
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(board_kw,new cljs.core.Keyword(null,"all-posts","all-posts",-1285476533))){
return oc.web.actions.activity.all_posts_refresh.cljs$core$IFn$_invoke$arity$1(org_data);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(board_kw,new cljs.core.Keyword(null,"bookmarks","bookmarks",1877375283))){
return oc.web.actions.activity.bookmarks_refresh.cljs$core$IFn$_invoke$arity$1(org_data);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(board_kw,new cljs.core.Keyword(null,"replies","replies",-1389888974))){
return oc.web.actions.activity.replies_refresh.cljs$core$IFn$_invoke$arity$2(org_data,true);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(board_kw,new cljs.core.Keyword(null,"following","following",-2049193617))){
return oc.web.actions.activity.following_refresh.cljs$core$IFn$_invoke$arity$2(org_data,true);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(board_kw,new cljs.core.Keyword(null,"unfollowing","unfollowing",-1076165830))){
return oc.web.actions.activity.unfollowing_refresh.cljs$core$IFn$_invoke$arity$2(org_data,true);
} else {
if(cljs.core.seq(current_contrib_id)){
return oc.web.actions.contributions.contributions_refresh.cljs$core$IFn$_invoke$arity$2(org_data,current_contrib_id);
} else {
if(cljs.core.not(oc.web.dispatcher.is_container_QMARK_(current_board_slug))){
return oc.web.actions.section.section_refresh(current_board_slug);
} else {
return oc.web.actions.nav_sidebar.reload_board_data();

}
}
}
}
}
}
}
}
});
oc.web.actions.nav_sidebar.nav_to_url_BANG_ = (function oc$web$actions$nav_sidebar$nav_to_url_BANG_(var_args){
var G__38760 = arguments.length;
switch (G__38760) {
case 3:
return oc.web.actions.nav_sidebar.nav_to_url_BANG_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 5:
return oc.web.actions.nav_sidebar.nav_to_url_BANG_.cljs$core$IFn$_invoke$arity$5((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.nav_sidebar.nav_to_url_BANG_.cljs$core$IFn$_invoke$arity$3 = (function (e,board_slug,url){
return oc.web.actions.nav_sidebar.nav_to_url_BANG_.cljs$core$IFn$_invoke$arity$5(e,board_slug,url,(function (){var or__4126__auto__ = oc.web.dispatcher.route_param.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"back-y","back-y",871917742));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.lib.utils.page_scroll_top();
}
})(),true);
}));

(oc.web.actions.nav_sidebar.nav_to_url_BANG_.cljs$core$IFn$_invoke$arity$5 = (function (e,board_slug,url,back_y,refresh_QMARK_){
if(cljs.core.truth_((function (){var and__4115__auto__ = e;
if(cljs.core.truth_(and__4115__auto__)){
return e.preventDefault;
} else {
return and__4115__auto__;
}
})())){
e.preventDefault();
} else {
}

if(cljs.core.truth_(oc.shared.useragent.mobile_QMARK_)){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"mobile-navigation-sidebar","mobile-navigation-sidebar",-1723544081)], null),false], null));
} else {
}

return oc.web.lib.utils.after((0),(function (){
var current_path = [cljs.core.str.cljs$core$IFn$_invoke$arity$1(window.location.pathname),cljs.core.str.cljs$core$IFn$_invoke$arity$1(window.location.search)].join('');
var org_slug = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
var sort_type = oc.web.actions.activity.saved_sort_type(org_slug,board_slug);
var is_drafts_board_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(board_slug,oc.web.lib.utils.default_drafts_board_slug);
var is_container_QMARK_ = oc.web.dispatcher.is_container_QMARK_(board_slug);
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
var current_activity_id = oc.web.dispatcher.current_activity_id.cljs$core$IFn$_invoke$arity$0();
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(current_path,url)){
oc.web.actions.routing.post_routing();

return oc.web.actions.user.initial_loading.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([refresh_QMARK_], 0));
} else {
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"routing","routing",1440253662),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"org","org",1495985),org_slug,new cljs.core.Keyword(null,"board","board",-1907017633),board_slug,new cljs.core.Keyword(null,"sort-type","sort-type",-2053499504),sort_type,new cljs.core.Keyword(null,"scroll-y","scroll-y",-1381960567),back_y,new cljs.core.Keyword(null,"query-params","query-params",900640534),oc.web.dispatcher.query_params.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"route","route",329891309),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [org_slug,(cljs.core.truth_(is_container_QMARK_)?"dashboard":board_slug),sort_type], null)], null)], null));

window.history.pushState(({}),document.title,url);

if(cljs.core.truth_(refresh_QMARK_)){
oc.web.actions.nav_sidebar.reload_board_data();
} else {
}

if(cljs.core.truth_(((cljs.core.not(is_container_QMARK_))?(((!(is_drafts_board_QMARK_)))?new cljs.core.Keyword(null,"collapsed","collapsed",-628494523).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.cmail_state.cljs$core$IFn$_invoke$arity$0()):false):false))){
var temp__33777__auto__ = oc.web.dispatcher.org_board_data.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([org_data,board_slug], 0));
if(cljs.core.truth_(temp__33777__auto__)){
var nav_to_board_data = temp__33777__auto__;
var temp__33777__auto____$1 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(nav_to_board_data),"create","POST"], 0));
if(cljs.core.truth_(temp__33777__auto____$1)){
var edit_link = temp__33777__auto____$1;
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),oc.web.dispatcher.cmail_data_key,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"board-slug","board-slug",99003663),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(nav_to_board_data),new cljs.core.Keyword(null,"board-name","board-name",-677515056),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(nav_to_board_data),new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803),new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803).cljs$core$IFn$_invoke$arity$1(nav_to_board_data)], null)], null));

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.cmail_state_key,new cljs.core.Keyword(null,"key","key",-1516042587)),oc.web.lib.utils.activity_uuid()], null));
} else {
return null;
}
} else {
return null;
}
} else {
return null;
}
}
}));
}));

(oc.web.actions.nav_sidebar.nav_to_url_BANG_.cljs$lang$maxFixedArity = 5);

oc.web.actions.nav_sidebar.dismiss_post_modal = (function oc$web$actions$nav_sidebar$dismiss_post_modal(e){
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
var route = oc.web.dispatcher.route.cljs$core$IFn$_invoke$arity$0();
var board = oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0();
var contributions_id = oc.web.dispatcher.current_contributions_id.cljs$core$IFn$_invoke$arity$0();
var is_contributions_QMARK_ = cljs.core.seq(contributions_id);
var to_url = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"back-to","back-to",789993386).cljs$core$IFn$_invoke$arity$1(route);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.urls.following.cljs$core$IFn$_invoke$arity$0();
}
})();
var cont_data = oc.web.actions.nav_sidebar.current_container_data();
var should_refresh_data_QMARK_ = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"refresh","refresh",1947415525).cljs$core$IFn$_invoke$arity$1(route);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.not(cont_data);
}
})();
var default_back_y = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"back-y","back-y",871917742).cljs$core$IFn$_invoke$arity$1(route);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.lib.utils.page_scroll_top();
}
})();
var back_y = ((cljs.core.contains_QMARK_(route,new cljs.core.Keyword(null,"back-to","back-to",789993386)))?document.scrollingElement.scrollTop:oc.web.lib.utils.page_scroll_top());
if(is_contributions_QMARK_){
oc.web.actions.nav_sidebar.nav_to_author_BANG_.cljs$core$IFn$_invoke$arity$5(e,contributions_id,to_url,back_y,false);
} else {
oc.web.actions.nav_sidebar.nav_to_url_BANG_.cljs$core$IFn$_invoke$arity$5(e,board,to_url,back_y,false);
}

if(cljs.core.truth_(should_refresh_data_QMARK_)){
return oc.web.lib.utils.after((180),oc.web.actions.nav_sidebar.refresh_board_data);
} else {
return null;
}
});
oc.web.actions.nav_sidebar.open_post_modal = (function oc$web$actions$nav_sidebar$open_post_modal(var_args){
var G__38762 = arguments.length;
switch (G__38762) {
case 2:
return oc.web.actions.nav_sidebar.open_post_modal.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.actions.nav_sidebar.open_post_modal.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.nav_sidebar.open_post_modal.cljs$core$IFn$_invoke$arity$2 = (function (entry_data,dont_scroll){
return oc.web.actions.nav_sidebar.open_post_modal.cljs$core$IFn$_invoke$arity$3(entry_data,dont_scroll,null);
}));

(oc.web.actions.nav_sidebar.open_post_modal.cljs$core$IFn$_invoke$arity$3 = (function (entry_data,dont_scroll,comment_uuid){
var org__$1 = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
var entry_board_slug = new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(entry_data);
var current_sort_type = oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0();
var current_contributions_id = oc.web.dispatcher.current_contributions_id.cljs$core$IFn$_invoke$arity$0();
var current_board_slug = oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0();
var back_to = ((((cljs.core.seq(current_contributions_id)) && (cljs.core.not(current_board_slug))))?oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$1(current_contributions_id):((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug),new cljs.core.Keyword(null,"replies","replies",-1389888974)))?oc.web.urls.replies.cljs$core$IFn$_invoke$arity$0():((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug),new cljs.core.Keyword(null,"topics","topics",625768208)))?oc.web.urls.topics.cljs$core$IFn$_invoke$arity$0():((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug),new cljs.core.Keyword(null,"following","following",-2049193617)))?oc.web.urls.following.cljs$core$IFn$_invoke$arity$0():oc.web.urls.board.cljs$core$IFn$_invoke$arity$1(entry_board_slug)
))));
var entry_uuid = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data);
var post_url = (cljs.core.truth_(comment_uuid)?oc.web.urls.comment_url.cljs$core$IFn$_invoke$arity$4(org__$1,entry_board_slug,entry_uuid,comment_uuid):oc.web.urls.entry.cljs$core$IFn$_invoke$arity$3(org__$1,entry_board_slug,entry_uuid));
var query_params = oc.web.dispatcher.query_params.cljs$core$IFn$_invoke$arity$0();
var route = cljs.core.vec(cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [org__$1,current_board_slug,current_contributions_id,current_sort_type,entry_uuid,"activity"], null)));
var route_path_STAR_ = cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"entry-board","entry-board",1825593318),new cljs.core.Keyword(null,"back-to","back-to",789993386),new cljs.core.Keyword(null,"route","route",329891309),new cljs.core.Keyword(null,"sort-type","sort-type",-2053499504),new cljs.core.Keyword(null,"activity","activity",-1179221455),new cljs.core.Keyword(null,"org","org",1495985),new cljs.core.Keyword(null,"contributions","contributions",-1280485964),new cljs.core.Keyword(null,"query-params","query-params",900640534),new cljs.core.Keyword(null,"board","board",-1907017633)],[entry_board_slug,back_to,route,current_sort_type,entry_uuid,org__$1,oc.web.dispatcher.current_contributions_id.cljs$core$IFn$_invoke$arity$0(),query_params,current_board_slug]);
var route_path = (cljs.core.truth_(comment_uuid)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(route_path_STAR_,new cljs.core.Keyword(null,"comment","comment",532206069),comment_uuid):route_path_STAR_);
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"routing","routing",1440253662),route_path], null));

oc.web.actions.cmail.cmail_hide();

return window.history.pushState(({}),document.title,post_url);
}));

(oc.web.actions.nav_sidebar.open_post_modal.cljs$lang$maxFixedArity = 3);

/**
 * Push a panel at the top of the stack.
 */
oc.web.actions.nav_sidebar.push_panel = (function oc$web$actions$nav_sidebar$push_panel(panel){
if((cljs.core.count(cljs.core.get.cljs$core$IFn$_invoke$arity$3(cljs.core.deref(oc.web.dispatcher.app_state),new cljs.core.Keyword(null,"panel-stack","panel-stack",1084180004),cljs.core.PersistentVector.EMPTY)) === (0))){
oc.web.utils.dom.lock_page_scroll();
} else {
}

if(cljs.core.truth_((function (){var fexpr__38764 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"invite-slack","invite-slack",-1290785659),null,new cljs.core.Keyword(null,"invite-email","invite-email",1375794598),null,new cljs.core.Keyword(null,"invite-picker","invite-picker",1426151962),null], null), null);
return (fexpr__38764.cljs$core$IFn$_invoke$arity$1 ? fexpr__38764.cljs$core$IFn$_invoke$arity$1(panel) : fexpr__38764.call(null,panel));
})())){
oc.web.actions.user.dismiss_invite_box.cljs$core$IFn$_invoke$arity$0();
} else {
}

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"panel-stack","panel-stack",1084180004)], null),(function (p1__38763_SHARP_){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2((function (){var or__4126__auto__ = p1__38763_SHARP_;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.PersistentVector.EMPTY;
}
})(),panel));
})], null));
});
/**
 * Pop the panel at the top of stack from it and return it.
 */
oc.web.actions.nav_sidebar.pop_panel = (function oc$web$actions$nav_sidebar$pop_panel(){
var panel_stack = new cljs.core.Keyword(null,"panel-stack","panel-stack",1084180004).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
if((cljs.core.count(panel_stack) > (0))){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"panel-stack","panel-stack",1084180004)], null),cljs.core.pop], null));
} else {
}

if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.count(panel_stack),(1))){
oc.web.utils.dom.unlock_page_scroll();
} else {
}

return cljs.core.peek(panel_stack);
});
/**
 * Remove all the panels from the stack and return the stack itself.
 * The return value is never used, but it's being returned to be consistent with
 * the pop-panel function.
 */
oc.web.actions.nav_sidebar.close_all_panels = (function oc$web$actions$nav_sidebar$close_all_panels(){
var panel_stack = new cljs.core.Keyword(null,"panel-stack","panel-stack",1084180004).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"panel-stack","panel-stack",1084180004)], null),cljs.core.PersistentVector.EMPTY], null));

oc.web.utils.dom.unlock_page_scroll();

return panel_stack;
});
oc.web.actions.nav_sidebar.show_section_editor = (function oc$web$actions$nav_sidebar$show_section_editor(section_slug){
oc.web.actions.section.setup_section_editing(section_slug);

return oc.web.actions.nav_sidebar.push_panel(new cljs.core.Keyword(null,"section-edit","section-edit",-155333386));
});
oc.web.actions.nav_sidebar.hide_section_editor = (function oc$web$actions$nav_sidebar$hide_section_editor(){
return oc.web.actions.nav_sidebar.pop_panel();
});
oc.web.actions.nav_sidebar.show_section_add = (function oc$web$actions$nav_sidebar$show_section_add(){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"show-section-add-cb","show-section-add-cb",2030550136)], null),(function (sec_data,note,dismiss_action){
if(cljs.core.truth_(sec_data)){
return oc.web.actions.section.section_save.cljs$core$IFn$_invoke$arity$3(sec_data,note,dismiss_action);
} else {
if(cljs.core.fn_QMARK_(dismiss_action)){
return (dismiss_action.cljs$core$IFn$_invoke$arity$0 ? dismiss_action.cljs$core$IFn$_invoke$arity$0() : dismiss_action.call(null));
} else {
return null;
}
}
})], null));

return oc.web.actions.nav_sidebar.push_panel(new cljs.core.Keyword(null,"section-add","section-add",1356275896));
});
oc.web.actions.nav_sidebar.show_section_add_with_callback = (function oc$web$actions$nav_sidebar$show_section_add_with_callback(callback){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"show-section-add-cb","show-section-add-cb",2030550136)], null),(function (sec_data,note,dismiss_action){
(callback.cljs$core$IFn$_invoke$arity$3 ? callback.cljs$core$IFn$_invoke$arity$3(sec_data,note,dismiss_action) : callback.call(null,sec_data,note,dismiss_action));

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"show-section-add-cb","show-section-add-cb",2030550136)], null),null], null));

return oc.web.actions.nav_sidebar.pop_panel();
})], null));

return oc.web.actions.nav_sidebar.push_panel(new cljs.core.Keyword(null,"section-add","section-add",1356275896));
});
oc.web.actions.nav_sidebar.hide_section_add = (function oc$web$actions$nav_sidebar$hide_section_add(){
return oc.web.actions.nav_sidebar.pop_panel();
});
oc.web.actions.nav_sidebar.show_reminders = (function oc$web$actions$nav_sidebar$show_reminders(){
return oc.web.actions.nav_sidebar.push_panel(new cljs.core.Keyword(null,"reminders","reminders",-2135532712));
});
oc.web.actions.nav_sidebar.show_new_reminder = (function oc$web$actions$nav_sidebar$show_new_reminder(){
return oc.web.actions.nav_sidebar.push_panel(new cljs.core.Keyword(null,"reminder-new","reminder-new",181439137));
});
oc.web.actions.nav_sidebar.edit_reminder = (function oc$web$actions$nav_sidebar$edit_reminder(reminder_uuid){
return oc.web.actions.nav_sidebar.push_panel(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(["reminder-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(reminder_uuid)].join('')));
});
oc.web.actions.nav_sidebar.close_reminders = (function oc$web$actions$nav_sidebar$close_reminders(){
return oc.web.actions.nav_sidebar.pop_panel();
});
oc.web.actions.nav_sidebar.menu_toggle = (function oc$web$actions$nav_sidebar$menu_toggle(){
var panel_stack = new cljs.core.Keyword(null,"panel-stack","panel-stack",1084180004).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.peek(panel_stack),new cljs.core.Keyword(null,"menu","menu",352255198))){
return oc.web.actions.nav_sidebar.pop_panel();
} else {
return oc.web.actions.nav_sidebar.push_panel(new cljs.core.Keyword(null,"menu","menu",352255198));
}
});
oc.web.actions.nav_sidebar.menu_close = (function oc$web$actions$nav_sidebar$menu_close(){
return oc.web.actions.nav_sidebar.pop_panel();
});
oc.web.actions.nav_sidebar.show_org_settings = (function oc$web$actions$nav_sidebar$show_org_settings(panel){
if(cljs.core.truth_(panel)){
if(((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(panel,new cljs.core.Keyword(null,"payments","payments",-1324138047))) || (oc.web.local_settings.payments_enabled))){
return oc.web.actions.nav_sidebar.push_panel(panel);
} else {
return null;
}
} else {
return oc.web.actions.nav_sidebar.pop_panel();
}
});
oc.web.actions.nav_sidebar.show_user_settings = (function oc$web$actions$nav_sidebar$show_user_settings(panel){
if(cljs.core.truth_(panel)){
return oc.web.actions.nav_sidebar.push_panel(panel);
} else {
return oc.web.actions.nav_sidebar.pop_panel();
}
});
oc.web.actions.nav_sidebar.show_wrt = (function oc$web$actions$nav_sidebar$show_wrt(activity_uuid){
return oc.web.actions.nav_sidebar.push_panel(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(["wrt-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(activity_uuid)].join('')));
});
oc.web.actions.nav_sidebar.hide_wrt = (function oc$web$actions$nav_sidebar$hide_wrt(){
return oc.web.actions.nav_sidebar.pop_panel();
});
oc.web.actions.nav_sidebar.open_integrations_panel = (function oc$web$actions$nav_sidebar$open_integrations_panel(e){
if(cljs.core.truth_(e)){
e.preventDefault();

e.stopPropagation();
} else {
}

if(cljs.core.truth_(oc.web.lib.responsive.is_mobile_size_QMARK_())){
var alert_data = new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"action","action",-811238024),"mobile-integrations-link",new cljs.core.Keyword(null,"message","message",-406056002),"Wut integrations need to be configured in a desktop browser.",new cljs.core.Keyword(null,"solid-button-style","solid-button-style",-723792244),new cljs.core.Keyword(null,"green","green",-945526839),new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"OK, got it",new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),(function (){
return oc.web.components.ui.alert_modal.hide_alert();
})], null);
return oc.web.components.ui.alert_modal.show_alert(alert_data);
} else {
return oc.web.actions.nav_sidebar.show_org_settings(new cljs.core.Keyword(null,"integrations","integrations",1844532423));
}
});
(window.OCWebStaticOpenIntegrationsPanel = oc.web.actions.nav_sidebar.open_integrations_panel);
oc.web.actions.nav_sidebar.show_theme_settings = (function oc$web$actions$nav_sidebar$show_theme_settings(){
return oc.web.actions.nav_sidebar.push_panel(new cljs.core.Keyword(null,"theme","theme",-1247880880));
});
oc.web.actions.nav_sidebar.hide_theme_settings = (function oc$web$actions$nav_sidebar$hide_theme_settings(){
return oc.web.actions.nav_sidebar.pop_panel();
});
oc.web.actions.nav_sidebar.show_user_info = (function oc$web$actions$nav_sidebar$show_user_info(user_id){
return oc.web.actions.nav_sidebar.push_panel(["user-info-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(user_id)].join(''));
});
oc.web.actions.nav_sidebar.hide_user_info = (function oc$web$actions$nav_sidebar$hide_user_info(){
return oc.web.actions.nav_sidebar.pop_panel();
});
oc.web.actions.nav_sidebar.show_follow_picker = (function oc$web$actions$nav_sidebar$show_follow_picker(){
return oc.web.actions.nav_sidebar.push_panel(new cljs.core.Keyword(null,"follow-picker","follow-picker",-381856085));
});
oc.web.actions.nav_sidebar.hide_follow_picker = (function oc$web$actions$nav_sidebar$hide_follow_picker(){
return oc.web.actions.nav_sidebar.pop_panel();
});
oc.web.actions.nav_sidebar.open_invite_picker = (function oc$web$actions$nav_sidebar$open_invite_picker(e){
if(cljs.core.truth_(e)){
oc.web.lib.utils.event_stop(e);
} else {
}

return oc.web.actions.nav_sidebar.push_panel(new cljs.core.Keyword(null,"invite-picker","invite-picker",1426151962));
});
goog.exportSymbol('oc.web.actions.nav_sidebar.open_invite_picker', oc.web.actions.nav_sidebar.open_invite_picker);

//# sourceMappingURL=oc.web.actions.nav_sidebar.js.map
