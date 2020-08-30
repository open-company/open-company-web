goog.provide('oc.web.actions.org');
oc.web.actions.org.get_ap_url = (function oc$web$actions$org$get_ap_url(org_slug){
var first_ever_landing_name = oc.web.router.first_ever_landing_cookie(oc.web.lib.jwt.user_id());
var first_ever_landing = oc.web.lib.cookies.get_cookie(first_ever_landing_name);
if(cljs.core.truth_(first_ever_landing)){
return oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1(org_slug);
} else {
oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$1(first_ever_landing_name);

return oc.web.urls.first_ever_landing.cljs$core$IFn$_invoke$arity$1(org_slug);
}
});
oc.web.actions.org.bot_auth = (function oc$web$actions$org$bot_auth(var_args){
var args__4742__auto__ = [];
var len__4736__auto___43277 = arguments.length;
var i__4737__auto___43278 = (0);
while(true){
if((i__4737__auto___43278 < len__4736__auto___43277)){
args__4742__auto__.push((arguments[i__4737__auto___43278]));

var G__43279 = (i__4737__auto___43278 + (1));
i__4737__auto___43278 = G__43279;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((2) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((2)),(0),null)):null);
return oc.web.actions.org.bot_auth.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__4743__auto__);
});

(oc.web.actions.org.bot_auth.cljs$core$IFn$_invoke$arity$variadic = (function (team_data,user_data,p__43193){
var vec__43194 = p__43193;
var redirect_to = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43194,(0),null);
var redirect = (function (){var or__4126__auto__ = redirect_to;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.router.get_token();
}
})();
var auth_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(team_data),"bot"], 0));
var fixed_auth_url = oc.web.utils.user.auth_link_with_state(new cljs.core.Keyword(null,"href","href",-793805698).cljs$core$IFn$_invoke$arity$1(auth_link),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"user-id","user-id",-206822291),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user_data),new cljs.core.Keyword(null,"team-id","team-id",-14505725),new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(team_data),new cljs.core.Keyword(null,"redirect","redirect",-1975673286),redirect], null));
return oc.web.router.redirect_BANG_(fixed_auth_url);
}));

(oc.web.actions.org.bot_auth.cljs$lang$maxFixedArity = (2));

/** @this {Function} */
(oc.web.actions.org.bot_auth.cljs$lang$applyTo = (function (seq43190){
var G__43191 = cljs.core.first(seq43190);
var seq43190__$1 = cljs.core.next(seq43190);
var G__43192 = cljs.core.first(seq43190__$1);
var seq43190__$2 = cljs.core.next(seq43190__$1);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__43191,G__43192,seq43190__$2);
}));

oc.web.actions.org.maybe_show_integration_added_notification_QMARK_ = (function oc$web$actions$org$maybe_show_integration_added_notification_QMARK_(){
var temp__33774__auto__ = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
if(cljs.core.truth_(temp__33774__auto__)){
var org_data = temp__33774__auto__;
var temp__33774__auto____$1 = oc.web.dispatcher.bot_access.cljs$core$IFn$_invoke$arity$0();
if(cljs.core.truth_(temp__33774__auto____$1)){
var bot_access = temp__33774__auto____$1;
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(bot_access,"bot")){
oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"title","title",636505583),"Wut Bot enabled",new cljs.core.Keyword(null,"primary-bt-title","primary-bt-title",653140150),"OK",new cljs.core.Keyword(null,"primary-bt-dismiss","primary-bt-dismiss",-820688058),true,new cljs.core.Keyword(null,"expire","expire",-70657108),(5),new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"slack-bot-added","slack-bot-added",-616757346)], null));
} else {
}

if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(bot_access,"team")) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"new","new",-2085437848).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.query_params.cljs$core$IFn$_invoke$arity$0()),"true")))){
oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"title","title",636505583),"Integration added",new cljs.core.Keyword(null,"primary-bt-title","primary-bt-title",653140150),"OK",new cljs.core.Keyword(null,"primary-bt-dismiss","primary-bt-dismiss",-820688058),true,new cljs.core.Keyword(null,"expire","expire",-70657108),(5),new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"slack-team-added","slack-team-added",-1812849253)], null));
} else {
}

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"bot-access","bot-access",1631196357)], null),null], null));
} else {
return null;
}
} else {
return null;
}
});
oc.web.actions.org.max_retry_count = (3);
oc.web.actions.org.load_active_users = (function oc$web$actions$org$load_active_users(var_args){
var args__4742__auto__ = [];
var len__4736__auto___43301 = arguments.length;
var i__4737__auto___43302 = (0);
while(true){
if((i__4737__auto___43302 < len__4736__auto___43301)){
args__4742__auto__.push((arguments[i__4737__auto___43302]));

var G__43303 = (i__4737__auto___43302 + (1));
i__4737__auto___43302 = G__43303;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.actions.org.load_active_users.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.actions.org.load_active_users.cljs$core$IFn$_invoke$arity$variadic = (function (active_users_link,p__43202){
var vec__43203 = p__43202;
var retry = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43203,(0),null);
if(cljs.core.truth_(active_users_link)){
return oc.web.api.get_active_users(active_users_link,(function (p__43206){
var map__43207 = p__43206;
var map__43207__$1 = (((((!((map__43207 == null))))?(((((map__43207.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43207.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43207):map__43207);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43207__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43207__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43207__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
if(cljs.core.truth_(success)){
var resp = (cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):null);
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"active-users","active-users",-329555191),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),resp], null));
} else {
if((retry < oc.web.actions.org.max_retry_count)){
return oc.web.lib.utils.after((1000),(function (){
return oc.web.actions.org.load_active_users.cljs$core$IFn$_invoke$arity$variadic(active_users_link,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([((function (){var or__4126__auto__ = retry;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return (0);
}
})() + (1))], 0));
}));
} else {
return null;
}
}
}));
} else {
return null;
}
}));

(oc.web.actions.org.load_active_users.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.actions.org.load_active_users.cljs$lang$applyTo = (function (seq43200){
var G__43201 = cljs.core.first(seq43200);
var seq43200__$1 = cljs.core.next(seq43200);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__43201,seq43200__$1);
}));

oc.web.actions.org.check_org_404 = (function oc$web$actions$org$check_org_404(){
var orgs = oc.web.dispatcher.orgs_data.cljs$core$IFn$_invoke$arity$0();
if((cljs.core.count(orgs) > (0))){
oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$3(oc.web.router.last_org_cookie(),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(cljs.core.first(orgs)),oc.web.lib.cookies.default_cookie_expire);
} else {
oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$1(oc.web.router.last_org_cookie());
}

return oc.web.actions.routing.maybe_404.cljs$core$IFn$_invoke$arity$0();
});
oc.web.actions.org.get_default_board = (function oc$web$actions$org$get_default_board(org_data){
var last_board_slug = oc.web.urls.default_board_slug;
if(cljs.core.truth_(((((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(last_board_slug,"all-posts")) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(last_board_slug,"following"))))?oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"following"], 0)):false))){
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"slug","slug",2029314850),"home"], null);
} else {
if(cljs.core.truth_(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(last_board_slug,"unfollowing"))?oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"unfollowing"], 0)):false))){
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"slug","slug",2029314850),"unfollowing"], null);
} else {
var boards = new cljs.core.Keyword(null,"boards","boards",1912049694).cljs$core$IFn$_invoke$arity$1(org_data);
var board = cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__43210_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(p1__43210_SHARP_),last_board_slug);
}),boards));
var or__4126__auto__ = board;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var sorted_boards = cljs.core.vec(cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"name","name",1843675177),boards));
return cljs.core.first(sorted_boards);
}

}
}
});
oc.web.actions.org.other_resources_delay = (1500);
/**
 * Dispatch the org data into the app-state to be used by all the components.
 * Do all the needed loading when the org data are loaded if complete-refresh? is true.
 * The saved? flag is used as a strict boolean, if it's nil it means no org data PATCH happened, false
 * means that the save went wrong, true went well.
 */
oc.web.actions.org.org_loaded = (function oc$web$actions$org$org_loaded(var_args){
var args__4742__auto__ = [];
var len__4736__auto___43305 = arguments.length;
var i__4737__auto___43306 = (0);
while(true){
if((i__4737__auto___43306 < len__4736__auto___43305)){
args__4742__auto__.push((arguments[i__4737__auto___43306]));

var G__43308 = (i__4737__auto___43306 + (1));
i__4737__auto___43306 = G__43308;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.actions.org.org_loaded.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.actions.org.org_loaded.cljs$core$IFn$_invoke$arity$variadic = (function (org_data,p__43219){
var vec__43220 = p__43219;
var saved_QMARK_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43220,(0),null);
var email_domain = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43220,(1),null);
var complete_refresh_QMARK_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43220,(2),null);
if(cljs.core.truth_((function (){var and__4115__auto__ = org_data;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data));
} else {
return and__4115__auto__;
}
})())){
oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$3(oc.web.router.last_org_cookie(),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),oc.web.lib.cookies.default_cookie_expire);
} else {
}

var boards_43313 = new cljs.core.Keyword(null,"boards","boards",1912049694).cljs$core$IFn$_invoke$arity$1(org_data);
var current_board_slug_43314 = oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0();
var all_posts_link_43315 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"entries"], 0));
var bookmarks_link_43316 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"bookmarks"], 0));
var following_link_43317 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"following"], 0));
var unfollowing_link_43318 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"unfollowing"], 0));
var contrib_link_43319 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"partial-contributions"], 0));
var drafts_board_43320 = cljs.core.some((function (p1__43212_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(p1__43212_SHARP_),oc.web.lib.utils.default_drafts_board_slug)){
return p1__43212_SHARP_;
} else {
return null;
}
}),boards_43313);
var drafts_link_43321 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(drafts_board_43320),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["self","item"], null),"GET"], 0));
var replies_link_43322 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"replies"], 0));
var active_users_link_43323 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"active-users"], 0));
var is_following_QMARK__43324 = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(current_board_slug_43314,"following");
var is_replies_QMARK__43325 = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(current_board_slug_43314,"replies");
var is_bookmarks_QMARK__43326 = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(current_board_slug_43314,"bookmarks");
var is_drafts_QMARK__43327 = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(current_board_slug_43314,oc.web.lib.utils.default_drafts_board_slug);
var is_topics_QMARK__43328 = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(current_board_slug_43314,"topics");
var is_contributions_QMARK__43329 = cljs.core.seq(oc.web.dispatcher.current_contributions_id.cljs$core$IFn$_invoke$arity$0());
var is_unfollowing_QMARK__43330 = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(current_board_slug_43314,"unfollowing");
var sort_type_43331 = oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0();
var delay_count_43332 = cljs.core.atom.cljs$core$IFn$_invoke$arity$1((1));
var following_delay_43333 = ((((is_following_QMARK__43324) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(sort_type_43331,oc.web.dispatcher.recently_posted_sort))))?(0):(oc.web.actions.org.other_resources_delay * cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(delay_count_43332,cljs.core.inc)));
var replies_delay_43334 = ((is_replies_QMARK__43325)?(0):(oc.web.actions.org.other_resources_delay * cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(delay_count_43332,cljs.core.inc)));
var bookmarks_delay_43335 = ((is_bookmarks_QMARK__43326)?(0):(oc.web.actions.org.other_resources_delay * cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(delay_count_43332,cljs.core.inc)));
var drafts_delay_43336 = ((is_drafts_QMARK__43327)?(0):(oc.web.actions.org.other_resources_delay * cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(delay_count_43332,cljs.core.inc)));
var contributions_delay_43337 = ((is_contributions_QMARK__43329)?(0):(oc.web.actions.org.other_resources_delay * cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(delay_count_43332,cljs.core.inc)));
var route_43338 = oc.web.dispatcher.route_param.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"route","route",329891309));
if(is_bookmarks_QMARK__43326){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("bookmarks-nav","show","bookmarks-nav/show",-1028987900),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data)], null));
} else {
}

if(is_drafts_QMARK__43327){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("drafts-nav","show","drafts-nav/show",-356771177),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data)], null));
} else {
}

if(cljs.core.truth_(complete_refresh_QMARK_)){
if(cljs.core.truth_(oc.web.dispatcher.current_secure_activity_id.cljs$core$IFn$_invoke$arity$0())){
oc.web.actions.activity.secure_activity_get.cljs$core$IFn$_invoke$arity$0();
} else {
if(cljs.core.truth_(active_users_link_43323)){
oc.web.actions.org.load_active_users(active_users_link_43323);
} else {
}

if(cljs.core.truth_((function (){var and__4115__auto__ = oc.web.dispatcher.current_activity_id.cljs$core$IFn$_invoke$arity$0();
if(cljs.core.truth_(and__4115__auto__)){
return oc.web.dispatcher.current_entry_board_slug.cljs$core$IFn$_invoke$arity$0();
} else {
return and__4115__auto__;
}
})())){
oc.web.actions.cmail.get_entry_with_uuid(oc.web.dispatcher.current_entry_board_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_activity_id.cljs$core$IFn$_invoke$arity$0());
} else {
}

if(cljs.core.truth_(following_link_43317)){
oc.web.lib.utils.maybe_after(following_delay_43333,(function (){
return oc.web.actions.activity.following_get.cljs$core$IFn$_invoke$arity$1(org_data);
}));
} else {
}

if(cljs.core.truth_(replies_link_43322)){
oc.web.lib.utils.maybe_after(replies_delay_43334,(function (){
return oc.web.actions.activity.replies_get.cljs$core$IFn$_invoke$arity$1(org_data);
}));
} else {
}

if(cljs.core.truth_(bookmarks_link_43316)){
oc.web.lib.utils.maybe_after(bookmarks_delay_43335,(function (){
return oc.web.actions.activity.bookmarks_get(org_data);
}));
} else {
}

if(cljs.core.truth_(drafts_link_43321)){
oc.web.lib.utils.maybe_after(drafts_delay_43336,(function (){
return oc.web.actions.section.section_get(oc.web.lib.utils.default_drafts_board_slug,drafts_link_43321);
}));
} else {
}

if(cljs.core.truth_((function (){var and__4115__auto__ = contrib_link_43319;
if(cljs.core.truth_(and__4115__auto__)){
return oc.web.dispatcher.current_contributions_id.cljs$core$IFn$_invoke$arity$0();
} else {
return and__4115__auto__;
}
})())){
oc.web.lib.utils.maybe_after(contributions_delay_43337,(function (){
return oc.web.actions.contributions.contributions_get.cljs$core$IFn$_invoke$arity$2(org_data,oc.web.dispatcher.current_contributions_id.cljs$core$IFn$_invoke$arity$0());
}));
} else {
}

if(cljs.core.truth_(((is_unfollowing_QMARK__43330)?unfollowing_link_43318:false))){
oc.web.actions.activity.unfollowing_get(org_data);
} else {
}
}

if(cljs.core.truth_(new cljs.core.Keyword(null,"badge-following","badge-following",-1836997336).cljs$core$IFn$_invoke$arity$1(org_data))){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"maybe-badge-following","maybe-badge-following",-1127324050),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),current_board_slug_43314], null));
} else {
}

if(cljs.core.truth_(new cljs.core.Keyword(null,"badge-replies","badge-replies",-1766263392).cljs$core$IFn$_invoke$arity$1(org_data))){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"maybe-badge-replies","maybe-badge-replies",-1675783392),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),current_board_slug_43314], null));
} else {
}
} else {
}

if(is_topics_QMARK__43328){
} else {
if(cljs.core.truth_(oc.web.dispatcher.is_container_QMARK_(current_board_slug_43314))){
if(((((is_replies_QMARK__43325) && (cljs.core.not(replies_link_43322)))) || (((is_bookmarks_QMARK__43326) && (cljs.core.not(bookmarks_link_43316)))) || (((is_following_QMARK__43324) && (cljs.core.not(following_link_43317)))) || (((is_drafts_QMARK__43327) && (cljs.core.not(drafts_link_43321)))) || (((is_unfollowing_QMARK__43330) && (cljs.core.not(unfollowing_link_43318)))))){
oc.web.actions.org.check_org_404();
} else {
}
} else {
if(cljs.core.truth_(current_board_slug_43314)){
var temp__5733__auto___43351 = cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__43215_SHARP_){
return ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(p1__43215_SHARP_),current_board_slug_43314)) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__43215_SHARP_),current_board_slug_43314)));
}),boards_43313));
if(cljs.core.truth_(temp__5733__auto___43351)){
var board_data_43352 = temp__5733__auto___43351;
if(is_drafts_QMARK__43327){
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(board_data_43352),current_board_slug_43314)){
oc.web.router.rewrite_board_uuid_as_slug(current_board_slug_43314,new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(board_data_43352));
} else {
}

var temp__33774__auto___43353 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(board_data_43352),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["item","self"], null),"GET"], 0));
if(cljs.core.truth_(temp__33774__auto___43353)){
var board_link_43354 = temp__33774__auto___43353;
oc.web.actions.section.section_get(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(board_data_43352),board_link_43354);
} else {
}
}
} else {
if(is_drafts_QMARK__43327){
oc.web.lib.utils.after((100),(function (){
return oc.web.actions.section.section_get_finish(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),oc.web.lib.utils.default_drafts_board_slug,oc.web.dispatcher.recently_posted_sort,oc.web.lib.utils.default_drafts_board);
}));
} else {
if(cljs.core.truth_(oc.web.dispatcher.current_activity_id.cljs$core$IFn$_invoke$arity$0())){
} else {
oc.web.actions.routing.maybe_404.cljs$core$IFn$_invoke$arity$0();
}
}
}
} else {
if(((cljs.core.not(oc.web.dispatcher.in_route_QMARK_.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"org-settings-invite","org-settings-invite",-941960323)))) && (cljs.core.not(oc.web.dispatcher.in_route_QMARK_.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"org-settings-team","org-settings-team",-2014515345)))) && (cljs.core.not(oc.web.dispatcher.in_route_QMARK_.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"org-settings","org-settings",-785943661)))) && (cljs.core.not(oc.web.dispatcher.in_route_QMARK_.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"email-verification","email-verification",-2006200871)))) && (cljs.core.not(oc.web.dispatcher.in_route_QMARK_.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"sign-up","sign-up",-1190725688)))) && (cljs.core.not(oc.web.dispatcher.in_route_QMARK_.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"email-wall","email-wall",-1908696318)))) && (cljs.core.not(oc.web.dispatcher.in_route_QMARK_.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"confirm-invitation","confirm-invitation",1250144934)))) && (cljs.core.not(oc.web.dispatcher.in_route_QMARK_.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"secure-activity","secure-activity",1915866237)))) && (cljs.core.not(oc.web.dispatcher.current_contributions_id.cljs$core$IFn$_invoke$arity$0())))){
var board_to_43355 = oc.web.actions.org.get_default_board(org_data);
oc.web.router.nav_BANG_((cljs.core.truth_(board_to_43355)?oc.web.urls.board.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(board_to_43355)):oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data))));
} else {
}
}
}
}

if(cljs.core.truth_((function (){var or__4126__auto__ = oc.web.lib.jwt.jwt();
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.lib.jwt.id_token();
}
})())){
var temp__5735__auto___43356 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"changes"], 0));
if(cljs.core.truth_(temp__5735__auto___43356)){
var ws_link_43357 = temp__5735__auto___43356;
oc.web.ws.change_client.reconnect(ws_link_43357,oc.web.lib.jwt.user_id(),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),new cljs.core.Keyword(null,"boards","boards",1912049694).cljs$core$IFn$_invoke$arity$1(org_data)),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(org_data)));
} else {
}
} else {
}

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
var temp__5735__auto___43358 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"interactions"], 0));
if(cljs.core.truth_(temp__5735__auto___43358)){
var ws_link_43359 = temp__5735__auto___43358;
oc.web.ws.interaction_client.reconnect(ws_link_43359,oc.web.lib.jwt.user_id());
} else {
}
} else {
}

if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
var temp__5735__auto___43360 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"notifications"], 0));
if(cljs.core.truth_(temp__5735__auto___43360)){
var ws_link_43361 = temp__5735__auto___43360;
oc.web.ws.notify_client.reconnect(ws_link_43361,oc.web.lib.jwt.user_id());
} else {
}
} else {
}

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"org-loaded","org-loaded",-1554103541),org_data,saved_QMARK_,email_domain], null));

oc.web.lib.utils.after((100),oc.web.actions.org.maybe_show_integration_added_notification_QMARK_);

oc.web.lib.fullstory.track_org(org_data);

oc.web.lib.chat.identify();

oc.web.actions.payments.maybe_load_payments_data.cljs$core$IFn$_invoke$arity$variadic(org_data,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([complete_refresh_QMARK_], 0));

return (document.title = ["Wut | ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(org_data))].join(''));
}));

(oc.web.actions.org.org_loaded.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.actions.org.org_loaded.cljs$lang$applyTo = (function (seq43217){
var G__43218 = cljs.core.first(seq43217);
var seq43217__$1 = cljs.core.next(seq43217);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__43218,seq43217__$1);
}));

oc.web.actions.org.get_org_cb = (function oc$web$actions$org$get_org_cb(prevent_complete_refresh_QMARK_,p__43229){
var map__43230 = p__43229;
var map__43230__$1 = (((((!((map__43230 == null))))?(((((map__43230.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43230.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43230):map__43230);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43230__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43230__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43230__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var org_data = oc.web.lib.json.json__GT_cljs(body);
return oc.web.actions.org.org_loaded.cljs$core$IFn$_invoke$arity$variadic(org_data,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([null,null,cljs.core.not(prevent_complete_refresh_QMARK_)], 0));
});
oc.web.actions.org.get_org = (function oc$web$actions$org$get_org(var_args){
var G__43234 = arguments.length;
switch (G__43234) {
case 0:
return oc.web.actions.org.get_org.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.actions.org.get_org.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.actions.org.get_org.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.actions.org.get_org.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.org.get_org.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.actions.org.get_org.cljs$core$IFn$_invoke$arity$3(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0(),false,null);
}));

(oc.web.actions.org.get_org.cljs$core$IFn$_invoke$arity$1 = (function (org_data){
return oc.web.actions.org.get_org.cljs$core$IFn$_invoke$arity$3(org_data,false,null);
}));

(oc.web.actions.org.get_org.cljs$core$IFn$_invoke$arity$2 = (function (org_data,prevent_complete_refresh_QMARK_){
return oc.web.actions.org.get_org.cljs$core$IFn$_invoke$arity$3(org_data,prevent_complete_refresh_QMARK_,null);
}));

(oc.web.actions.org.get_org.cljs$core$IFn$_invoke$arity$3 = (function (org_data,prevent_complete_refresh_QMARK_,callback){
var fixed_org_data = (function (){var or__4126__auto__ = org_data;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
}
})();
var org_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(fixed_org_data),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["item","self"], null),"GET"], 0));
return oc.web.api.get_org(org_link,(function (p__43235){
var map__43236 = p__43235;
var map__43236__$1 = (((((!((map__43236 == null))))?(((((map__43236.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43236.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43236):map__43236);
var resp = map__43236__$1;
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43236__$1,new cljs.core.Keyword(null,"success","success",1890645906));
oc.web.actions.org.get_org_cb(prevent_complete_refresh_QMARK_,resp);

if(cljs.core.fn_QMARK_(callback)){
return (callback.cljs$core$IFn$_invoke$arity$1 ? callback.cljs$core$IFn$_invoke$arity$1(success) : callback.call(null,success));
} else {
return null;
}
}));
}));

(oc.web.actions.org.get_org.cljs$lang$maxFixedArity = 3);

oc.web.actions.org.org_redirect = (function oc$web$actions$org$org_redirect(org_data){
if(cljs.core.truth_(org_data)){
var org_slug = new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data);
return oc.web.lib.utils.after((100),(function (){
return oc.web.router.redirect_BANG_(oc.web.actions.org.get_ap_url(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data)));
}));
} else {
return null;
}
});
oc.web.actions.org.org_created = (function oc$web$actions$org$org_created(org_data){
return oc.web.lib.utils.after((0),(function (){
return oc.web.router.nav_BANG_(oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data)));
}));
});
oc.web.actions.org.team_patch_cb = (function oc$web$actions$org$team_patch_cb(org_data,p__43247){
var map__43248 = p__43247;
var map__43248__$1 = (((((!((map__43248 == null))))?(((((map__43248.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43248.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43248):map__43248);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43248__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43248__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43248__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
if(cljs.core.truth_(success)){
return oc.web.actions.org.org_created(org_data);
} else {
return null;
}
});
oc.web.actions.org.handle_org_redirect = (function oc$web$actions$org$handle_org_redirect(team_data,org_data,email_domain){
if(cljs.core.truth_(((cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(team_data)))?oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(team_data),"partial-update"], 0)):false))){
var team_id = new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(org_data);
var team_data__$1 = oc.web.dispatcher.team_data.cljs$core$IFn$_invoke$arity$1(team_id);
var team_patch_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(team_data__$1),"partial-update"], 0));
return oc.web.api.patch_team(team_patch_link,team_id,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(org_data)], null),cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.actions.org.team_patch_cb,org_data));
} else {
return oc.web.actions.org.org_created(org_data);
}
});
oc.web.actions.org.update_email_domains = (function oc$web$actions$org$update_email_domains(email_domain,org_data){
var team_data = oc.web.dispatcher.team_data.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(org_data));
var redirect_cb = (function (){
return oc.web.actions.org.handle_org_redirect(team_data,org_data,email_domain);
});
if(cljs.core.seq(email_domain)){
var add_email_domain_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(team_data),"add","POST",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"content-type","content-type",-508222634),"application/vnd.open-company.team.email-domain.v1+json"], null)], 0));
return oc.web.api.add_email_domain(add_email_domain_link,email_domain,redirect_cb,team_data);
} else {
return redirect_cb();
}
});
oc.web.actions.org.pre_flight_email_domain = (function oc$web$actions$org$pre_flight_email_domain(email_domain,team_id,cb){
var team_data = (function (){var or__4126__auto__ = oc.web.dispatcher.team_data.cljs$core$IFn$_invoke$arity$1(team_id);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__43250_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(p1__43250_SHARP_),team_id);
}),oc.web.dispatcher.teams_data.cljs$core$IFn$_invoke$arity$0()));
}
})();
var add_email_domain_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(team_data),"add","POST",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"content-type","content-type",-508222634),"application/vnd.open-company.team.email-domain.v1+json"], null)], 0));
var redirect_cb = (function (p__43251){
var map__43252 = p__43251;
var map__43252__$1 = (((((!((map__43252 == null))))?(((((map__43252.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43252.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43252):map__43252);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43252__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43252__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43252__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
return (cb.cljs$core$IFn$_invoke$arity$2 ? cb.cljs$core$IFn$_invoke$arity$2(success,status) : cb.call(null,success,status));
});
return oc.web.api.add_email_domain.cljs$core$IFn$_invoke$arity$variadic(add_email_domain_link,email_domain,redirect_cb,team_data,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true], 0));
});
oc.web.actions.org.org_create_check_errors = (function oc$web$actions$org$org_create_check_errors(status){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,(409))){
return oc.web.router.nav_BANG_(oc.web.urls.org.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(cljs.core.first(oc.web.dispatcher.orgs_data.cljs$core$IFn$_invoke$arity$0()))));
} else {
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915),new cljs.core.Keyword(null,"error","error",-978969032)], null),true], null));
}
});
oc.web.actions.org.org_create_cb = (function oc$web$actions$org$org_create_cb(email_domain,p__43254){
var map__43255 = p__43254;
var map__43255__$1 = (((((!((map__43255 == null))))?(((((map__43255.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43255.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43255):map__43255);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43255__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43255__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43255__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
if(cljs.core.truth_(success)){
var temp__5735__auto__ = (cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):null);
if(cljs.core.truth_(temp__5735__auto__)){
var org_data = temp__5735__auto__;
history.replaceState(({}),document.title,oc.web.urls.sign_up_update_team.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data)));

oc.web.actions.org.org_loaded.cljs$core$IFn$_invoke$arity$variadic(org_data,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([null,email_domain], 0));

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"org-create","org-create",619717847)], null));

return oc.web.actions.org.update_email_domains(email_domain,org_data);
} else {
return null;
}
} else {
return oc.web.actions.org.org_create_check_errors(status);
}
});
oc.web.actions.org.org_update_cb = (function oc$web$actions$org$org_update_cb(email_domain,p__43257){
var map__43258 = p__43257;
var map__43258__$1 = (((((!((map__43258 == null))))?(((((map__43258.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43258.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43258):map__43258);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43258__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43258__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43258__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
if(cljs.core.truth_(success)){
var temp__5735__auto__ = (cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):null);
if(cljs.core.truth_(temp__5735__auto__)){
var org_data = temp__5735__auto__;
oc.web.actions.org.org_loaded.cljs$core$IFn$_invoke$arity$variadic(org_data,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([success,email_domain], 0));

return oc.web.actions.org.update_email_domains(email_domain,org_data);
} else {
return null;
}
} else {
return oc.web.actions.org.org_create_check_errors(status);
}
});
/**
 * 
 *   Truncate a string based on length
 *   
 */
oc.web.actions.org.trunc = (function oc$web$actions$org$trunc(s,n){
return cljs.core.subs.cljs$core$IFn$_invoke$arity$3(s,(0),(function (){var x__4217__auto__ = cljs.core.count(s);
var y__4218__auto__ = n;
return ((x__4217__auto__ < y__4218__auto__) ? x__4217__auto__ : y__4218__auto__);
})());
});
/**
 * 
 *   Augment org data with utm values stored in cookies.
 * 
 *   Remove utm cookies if present.
 *   
 */
oc.web.actions.org.add_utm_data = (function oc$web$actions$org$add_utm_data(org_data){
var source = oc.web.lib.cookies.get_cookie("utm_source");
var term = oc.web.lib.cookies.get_cookie("utm_term");
var medium = oc.web.lib.cookies.get_cookie("utm_medium");
var campaign = oc.web.lib.cookies.get_cookie("utm_campaign");
var seq__43264_43370 = cljs.core.seq(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["utm_source","utm_term","utm_medium","utm_campaign"], null));
var chunk__43265_43371 = null;
var count__43266_43372 = (0);
var i__43267_43373 = (0);
while(true){
if((i__43267_43373 < count__43266_43372)){
var c_name_43374 = chunk__43265_43371.cljs$core$IIndexed$_nth$arity$2(null,i__43267_43373);
OCStaticDeleteCookie(c_name_43374);


var G__43375 = seq__43264_43370;
var G__43376 = chunk__43265_43371;
var G__43377 = count__43266_43372;
var G__43378 = (i__43267_43373 + (1));
seq__43264_43370 = G__43375;
chunk__43265_43371 = G__43376;
count__43266_43372 = G__43377;
i__43267_43373 = G__43378;
continue;
} else {
var temp__5735__auto___43379 = cljs.core.seq(seq__43264_43370);
if(temp__5735__auto___43379){
var seq__43264_43380__$1 = temp__5735__auto___43379;
if(cljs.core.chunked_seq_QMARK_(seq__43264_43380__$1)){
var c__4556__auto___43381 = cljs.core.chunk_first(seq__43264_43380__$1);
var G__43382 = cljs.core.chunk_rest(seq__43264_43380__$1);
var G__43383 = c__4556__auto___43381;
var G__43384 = cljs.core.count(c__4556__auto___43381);
var G__43385 = (0);
seq__43264_43370 = G__43382;
chunk__43265_43371 = G__43383;
count__43266_43372 = G__43384;
i__43267_43373 = G__43385;
continue;
} else {
var c_name_43386 = cljs.core.first(seq__43264_43380__$1);
OCStaticDeleteCookie(c_name_43386);


var G__43387 = cljs.core.next(seq__43264_43380__$1);
var G__43388 = null;
var G__43389 = (0);
var G__43390 = (0);
seq__43264_43370 = G__43387;
chunk__43265_43371 = G__43388;
count__43266_43372 = G__43389;
i__43267_43373 = G__43390;
continue;
}
} else {
}
}
break;
}

if(cljs.core.truth_((function (){var or__4126__auto__ = source;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = term;
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
var or__4126__auto____$2 = medium;
if(cljs.core.truth_(or__4126__auto____$2)){
return or__4126__auto____$2;
} else {
return campaign;
}
}
}
})())){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([org_data,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"utm-data","utm-data",-1997478583),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"utm-source","utm-source",-1511770063),(function (){var or__4126__auto__ = source;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "";
}
})(),new cljs.core.Keyword(null,"utm-term","utm-term",106854977),(function (){var or__4126__auto__ = term;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "";
}
})(),new cljs.core.Keyword(null,"utm-medium","utm-medium",1751291188),(function (){var or__4126__auto__ = medium;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "";
}
})(),new cljs.core.Keyword(null,"utm-campaign","utm-campaign",1694385576),(function (){var or__4126__auto__ = campaign;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "";
}
})()], null)], null)], 0));
} else {
return org_data;
}
});
oc.web.actions.org.create_or_update_org = (function oc$web$actions$org$create_or_update_org(org_data){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915),new cljs.core.Keyword(null,"error","error",-978969032)], null),false], null));

var email_domain = new cljs.core.Keyword(null,"email-domain","email-domain",-768613335).cljs$core$IFn$_invoke$arity$1(org_data);
var existing_org = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
var logo_org_data = ((cljs.core.seq(new cljs.core.Keyword(null,"logo-url","logo-url",-1629105032).cljs$core$IFn$_invoke$arity$1(org_data)))?org_data:cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(org_data,new cljs.core.Keyword(null,"logo-url","logo-url",-1629105032),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"logo-width","logo-width",-4247589),new cljs.core.Keyword(null,"logo-height","logo-height",-2066989379)], 0)));
var clean_org_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(logo_org_data,new cljs.core.Keyword(null,"name","name",1843675177),oc.web.actions.org.trunc(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(logo_org_data),(127)));
if(cljs.core.seq(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(existing_org))){
var org_patch_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0()),"partial-update"], 0));
return oc.web.api.patch_org(org_patch_link,clean_org_data,cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.actions.org.org_update_cb,email_domain));
} else {
var create_org_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([oc.web.dispatcher.api_entry_point.cljs$core$IFn$_invoke$arity$0(),"create"], 0));
return oc.web.api.create_org(create_org_link,oc.web.actions.org.add_utm_data(clean_org_data),cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.actions.org.org_create_cb,email_domain));
}
});
oc.web.actions.org.org_edit_setup = (function oc$web$actions$org$org_edit_setup(org_data){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"org-edit-setup","org-edit-setup",-2083658838),org_data], null));
});
oc.web.actions.org.org_edit_save_cb = (function oc$web$actions$org$org_edit_save_cb(p__43268){
var map__43269 = p__43268;
var map__43269__$1 = (((((!((map__43269 == null))))?(((((map__43269.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43269.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43269):map__43269);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43269__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43269__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43269__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
return oc.web.actions.org.org_loaded.cljs$core$IFn$_invoke$arity$variadic(oc.web.lib.json.json__GT_cljs(body),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([success], 0));
});
oc.web.actions.org.org_edit_save = (function oc$web$actions$org$org_edit_save(org_data){
var org_patch_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0()),"partial-update"], 0));
var with_trimmed_name = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(org_data,new cljs.core.Keyword(null,"name","name",1843675177),clojure.string.trim(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(org_data)));
return oc.web.api.patch_org(org_patch_link,with_trimmed_name,(function (p__43271){
var map__43272 = p__43271;
var map__43272__$1 = (((((!((map__43272 == null))))?(((((map__43272.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43272.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43272):map__43272);
var resp = map__43272__$1;
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43272__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43272__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
if(cljs.core.truth_(success)){
return oc.web.actions.org.org_edit_save_cb(resp);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,(422))){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915),new cljs.core.Keyword(null,"error","error",-978969032)], null),true], null));
} else {
return null;
}
}
}));
});
oc.web.actions.org.org_avatar_edit_save_cb = (function oc$web$actions$org$org_avatar_edit_save_cb(p__43274){
var map__43275 = p__43274;
var map__43275__$1 = (((((!((map__43275 == null))))?(((((map__43275.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43275.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43275):map__43275);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43275__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43275__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43275__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
if(cljs.core.truth_(success)){
oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"title","title",636505583),"Image update succeeded",new cljs.core.Keyword(null,"description","description",-1428560544),"Your image was succesfully updated.",new cljs.core.Keyword(null,"expire","expire",-70657108),(3),new cljs.core.Keyword(null,"dismiss","dismiss",412569545),true], null));

return oc.web.actions.org.org_loaded(oc.web.lib.json.json__GT_cljs(body));
} else {
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("org-avatar-update","failed","org-avatar-update/failed",246419978)], null));

return oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"title","title",636505583),"Image upload error",new cljs.core.Keyword(null,"description","description",-1428560544),"An error occurred while processing your company avatar. Please retry.",new cljs.core.Keyword(null,"expire","expire",-70657108),(3),new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"org-avatar-upload-failed","org-avatar-upload-failed",-1869436514),new cljs.core.Keyword(null,"dismiss","dismiss",412569545),true], null));
}
});
oc.web.actions.org.org_avatar_edit_save = (function oc$web$actions$org$org_avatar_edit_save(org_avatar_data){
var org_patch_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0()),"partial-update"], 0));
return oc.web.api.patch_org(org_patch_link,org_avatar_data,oc.web.actions.org.org_avatar_edit_save_cb);
});
oc.web.actions.org.org_change = (function oc$web$actions$org$org_change(data,org_data){
var change_data = new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(data);
var container_id = new cljs.core.Keyword(null,"container-id","container-id",1274665684).cljs$core$IFn$_invoke$arity$1(change_data);
var user_id = new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(change_data);
if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.lib.jwt.user_id(),user_id)){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(container_id,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(org_data))){
return oc.web.lib.utils.after((1000),(function (){
return oc.web.actions.org.get_org.cljs$core$IFn$_invoke$arity$2(org_data,true);
}));
} else {
return null;
}
} else {
return null;
}
});
oc.web.actions.org.subscribe = (function oc$web$actions$org$subscribe(){
oc.web.ws.change_client.subscribe(new cljs.core.Keyword("org","status","org/status",-1997621993),(function (data){
return oc.web.actions.org.get_org.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0(),true);
}));

return oc.web.ws.change_client.subscribe(new cljs.core.Keyword("container","change","container/change",-1507058407),(function (data){
var change_data = new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(data);
var change_type = new cljs.core.Keyword(null,"change-type","change-type",1354898425).cljs$core$IFn$_invoke$arity$1(change_data);
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
oc.web.actions.org.org_change(data,org_data);

if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(change_type,new cljs.core.Keyword(null,"delete","delete",-1768633620))) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"container-id","container-id",1274665684).cljs$core$IFn$_invoke$arity$1(change_data),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(org_data))))){
var current_board_data = oc.web.dispatcher.board_data();
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"item-id","item-id",-1804511607).cljs$core$IFn$_invoke$arity$1(change_data),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(current_board_data))){
return oc.web.router.nav_BANG_(oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data)));
} else {
return null;
}
} else {
return null;
}
}));
});
oc.web.actions.org.signup_invite_completed = (function oc$web$actions$org$signup_invite_completed(org_data){
return oc.web.router.nav_BANG_(oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data)));
});

//# sourceMappingURL=oc.web.actions.org.js.map
