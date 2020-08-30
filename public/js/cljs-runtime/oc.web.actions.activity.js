goog.provide('oc.web.actions.activity');
oc.web.actions.activity.initial_revision = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
oc.web.actions.activity.watch_boards = (function oc$web$actions$activity$watch_boards(posts_data){
if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
var board_slugs = cljs.core.distinct.cljs$core$IFn$_invoke$arity$1(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"board-slug","board-slug",99003663),posts_data));
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
var org_boards = new cljs.core.Keyword(null,"boards","boards",1912049694).cljs$core$IFn$_invoke$arity$1(org_data);
var org_board_map = cljs.core.zipmap(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850),org_boards),cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),org_boards));
return oc.web.ws.interaction_client.board_unwatch((function (rep){
var board_uuids = cljs.core.map.cljs$core$IFn$_invoke$arity$2(org_board_map,board_slugs);
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.actions.activity",null,41,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Watching on socket ",board_slugs,board_uuids], null);
}),null)),null,-701345827);

return oc.web.ws.interaction_client.boards_watch.cljs$core$IFn$_invoke$arity$1(board_uuids);
}));
} else {
return null;
}
});
/**
 * Request the list of readers of the given item.
 */
oc.web.actions.activity.request_reads_data = (function oc$web$actions$activity$request_reads_data(item_id){
return oc.web.api.request_reads_data(item_id);
});
/**
 * Request the reads count data only for the items we don't have already.
 */
oc.web.actions.activity.request_reads_count = (function oc$web$actions$activity$request_reads_count(item_ids){
var cleaned_ids = oc.web.utils.activity.clean_who_reads_count_ids(item_ids,oc.web.dispatcher.activity_read_data());
if(cljs.core.seq(cleaned_ids)){
return oc.web.api.request_reads_count(cleaned_ids);
} else {
return null;
}
});
oc.web.actions.activity.bookmarks_get_finish = (function oc$web$actions$activity$bookmarks_get_finish(org_slug,sort_type,refresh_QMARK_,p__42493){
var map__42494 = p__42493;
var map__42494__$1 = (((((!((map__42494 == null))))?(((((map__42494.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42494.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42494):map__42494);
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42494__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42494__$1,new cljs.core.Keyword(null,"success","success",1890645906));
if(cljs.core.truth_(body)){
var posts_data_key = oc.web.dispatcher.posts_data_key(org_slug);
var bookmarks_data = (cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):null);
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0(),"bookmarks")){
oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$3(oc.web.router.last_board_cookie(org_slug),"bookmarks",((((60) * (60)) * (24)) * (365)));

oc.web.actions.activity.request_reads_count(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(bookmarks_data))));

oc.web.actions.activity.watch_boards(new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(bookmarks_data)));
} else {
}

if(cljs.core.truth_(refresh_QMARK_)){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("bookmarks-refresh","finish","bookmarks-refresh/finish",-933941561),org_slug,sort_type,bookmarks_data], null));
} else {
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("bookmarks-get","finish","bookmarks-get/finish",1980734268),org_slug,sort_type,bookmarks_data], null));
}
} else {
return null;
}
});
oc.web.actions.activity.bookmarks_real_get = (function oc$web$actions$activity$bookmarks_real_get(var_args){
var G__42501 = arguments.length;
switch (G__42501) {
case 4:
return oc.web.actions.activity.bookmarks_real_get.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
case 5:
return oc.web.actions.activity.bookmarks_real_get.cljs$core$IFn$_invoke$arity$5((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.activity.bookmarks_real_get.cljs$core$IFn$_invoke$arity$4 = (function (bookmarks_link,org_slug,sort_type,finish_cb){
return oc.web.actions.activity.bookmarks_real_get.cljs$core$IFn$_invoke$arity$5(bookmarks_link,org_slug,sort_type,false,finish_cb);
}));

(oc.web.actions.activity.bookmarks_real_get.cljs$core$IFn$_invoke$arity$5 = (function (bookmarks_link,org_slug,sort_type,refresh_QMARK_,finish_cb){
return oc.web.api.get_all_posts(bookmarks_link,(function (resp){
oc.web.actions.activity.bookmarks_get_finish(org_slug,sort_type,refresh_QMARK_,resp);

if(cljs.core.fn_QMARK_(finish_cb)){
return (finish_cb.cljs$core$IFn$_invoke$arity$1 ? finish_cb.cljs$core$IFn$_invoke$arity$1(resp) : finish_cb.call(null,resp));
} else {
return null;
}
}));
}));

(oc.web.actions.activity.bookmarks_real_get.cljs$lang$maxFixedArity = 5);

oc.web.actions.activity.bookmarks_get = (function oc$web$actions$activity$bookmarks_get(var_args){
var args__4742__auto__ = [];
var len__4736__auto___43121 = arguments.length;
var i__4737__auto___43122 = (0);
while(true){
if((i__4737__auto___43122 < len__4736__auto___43121)){
args__4742__auto__.push((arguments[i__4737__auto___43122]));

var G__43123 = (i__4737__auto___43122 + (1));
i__4737__auto___43122 = G__43123;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.actions.activity.bookmarks_get.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.actions.activity.bookmarks_get.cljs$core$IFn$_invoke$arity$variadic = (function (org_data,p__42505){
var vec__42506 = p__42505;
var finish_cb = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42506,(0),null);
var temp__5735__auto__ = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"bookmarks"], 0));
if(cljs.core.truth_(temp__5735__auto__)){
var bookmarks_link = temp__5735__auto__;
return oc.web.actions.activity.bookmarks_real_get.cljs$core$IFn$_invoke$arity$5(bookmarks_link,new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),oc.web.dispatcher.recently_posted_sort,false,finish_cb);
} else {
return null;
}
}));

(oc.web.actions.activity.bookmarks_get.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.actions.activity.bookmarks_get.cljs$lang$applyTo = (function (seq42503){
var G__42504 = cljs.core.first(seq42503);
var seq42503__$1 = cljs.core.next(seq42503);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__42504,seq42503__$1);
}));

/**
 * If the user is looking at the bookmarks view we need to reload all the items that are visible right now.
 *   Instead, if the user is looking at another view we can just reload the first page.
 */
oc.web.actions.activity.bookmarks_refresh = (function oc$web$actions$activity$bookmarks_refresh(var_args){
var G__42510 = arguments.length;
switch (G__42510) {
case 0:
return oc.web.actions.activity.bookmarks_refresh.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.actions.activity.bookmarks_refresh.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.activity.bookmarks_refresh.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.actions.activity.bookmarks_refresh.cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.actions.activity.bookmarks_refresh.cljs$core$IFn$_invoke$arity$1 = (function (org_data){
var temp__33762__auto__ = oc.web.dispatcher.bookmarks_data.cljs$core$IFn$_invoke$arity$0();
if(cljs.core.truth_(temp__33762__auto__)){
var bookmarks_data = temp__33762__auto__;
var temp__33762__auto____$1 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(bookmarks_data),"refresh"], 0));
if(cljs.core.truth_(temp__33762__auto____$1)){
var refresh_link = temp__33762__auto____$1;
return oc.web.actions.activity.bookmarks_real_get.cljs$core$IFn$_invoke$arity$5(refresh_link,new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),oc.web.dispatcher.recently_posted_sort,true,null);
} else {
return oc.web.actions.activity.bookmarks_get(org_data);
}
} else {
return oc.web.actions.activity.bookmarks_get(org_data);
}
}));

(oc.web.actions.activity.bookmarks_refresh.cljs$lang$maxFixedArity = 1);

oc.web.actions.activity.bookmarks_more_finish = (function oc$web$actions$activity$bookmarks_more_finish(org_slug,sort_type,direction,p__42512){
var map__42513 = p__42512;
var map__42513__$1 = (((((!((map__42513 == null))))?(((((map__42513.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42513.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42513):map__42513);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42513__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42513__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
if(cljs.core.truth_(success)){
oc.web.actions.activity.request_reads_count(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(oc.web.lib.json.json__GT_cljs(body)))));
} else {
}

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("bookmarks-more","finish","bookmarks-more/finish",119195869),org_slug,sort_type,direction,(cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):null)], null));
});
oc.web.actions.activity.bookmarks_more = (function oc$web$actions$activity$bookmarks_more(more_link,direction){
oc.web.api.load_more_items(more_link,direction,cljs.core.partial.cljs$core$IFn$_invoke$arity$4(oc.web.actions.activity.bookmarks_more_finish,oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.recently_posted_sort,direction));

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"bookmarks-more","bookmarks-more",399174414),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.recently_posted_sort], null));
});
oc.web.actions.activity.all_posts_get_finish = (function oc$web$actions$activity$all_posts_get_finish(org_slug,sort_type,refresh_QMARK_,p__42519){
var map__42520 = p__42519;
var map__42520__$1 = (((((!((map__42520 == null))))?(((((map__42520.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42520.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42520):map__42520);
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42520__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42520__$1,new cljs.core.Keyword(null,"success","success",1890645906));
if(cljs.core.truth_(body)){
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
var posts_data_key = oc.web.dispatcher.posts_data_key(org_slug);
var all_posts_data = (cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):null);
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0(),"all-posts")){
oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$3(oc.web.router.last_board_cookie(org_slug),"all-posts",((((60) * (60)) * (24)) * (365)));

oc.web.actions.activity.request_reads_count(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(all_posts_data))));

oc.web.actions.activity.watch_boards(new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(all_posts_data)));
} else {
}

if(cljs.core.truth_(refresh_QMARK_)){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("all-posts-refresh","finish","all-posts-refresh/finish",340160013),org_slug,sort_type,all_posts_data], null));
} else {
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("all-posts-get","finish","all-posts-get/finish",-1262488350),org_slug,sort_type,all_posts_data], null));
}
} else {
return null;
}
});
oc.web.actions.activity.activity_real_get = (function oc$web$actions$activity$activity_real_get(var_args){
var G__42523 = arguments.length;
switch (G__42523) {
case 4:
return oc.web.actions.activity.activity_real_get.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
case 5:
return oc.web.actions.activity.activity_real_get.cljs$core$IFn$_invoke$arity$5((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.activity.activity_real_get.cljs$core$IFn$_invoke$arity$4 = (function (activity_link,org_slug,sort_type,finish_cb){
return oc.web.actions.activity.activity_real_get.cljs$core$IFn$_invoke$arity$5(activity_link,org_slug,sort_type,false,finish_cb);
}));

(oc.web.actions.activity.activity_real_get.cljs$core$IFn$_invoke$arity$5 = (function (activity_link,org_slug,sort_type,refresh_QMARK_,finish_cb){
return oc.web.api.get_all_posts(activity_link,(function (resp){
oc.web.actions.activity.all_posts_get_finish(org_slug,sort_type,refresh_QMARK_,resp);

if(cljs.core.fn_QMARK_(finish_cb)){
return (finish_cb.cljs$core$IFn$_invoke$arity$1 ? finish_cb.cljs$core$IFn$_invoke$arity$1(resp) : finish_cb.call(null,resp));
} else {
return null;
}
}));
}));

(oc.web.actions.activity.activity_real_get.cljs$lang$maxFixedArity = 5);

oc.web.actions.activity.all_posts_get = (function oc$web$actions$activity$all_posts_get(var_args){
var args__4742__auto__ = [];
var len__4736__auto___43131 = arguments.length;
var i__4737__auto___43132 = (0);
while(true){
if((i__4737__auto___43132 < len__4736__auto___43131)){
args__4742__auto__.push((arguments[i__4737__auto___43132]));

var G__43133 = (i__4737__auto___43132 + (1));
i__4737__auto___43132 = G__43133;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.actions.activity.all_posts_get.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.actions.activity.all_posts_get.cljs$core$IFn$_invoke$arity$variadic = (function (org_data,p__42526){
var vec__42527 = p__42526;
var finish_cb = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42527,(0),null);
var temp__5735__auto__ = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"entries","GET"], 0));
if(cljs.core.truth_(temp__5735__auto__)){
var activity_link = temp__5735__auto__;
return oc.web.actions.activity.activity_real_get.cljs$core$IFn$_invoke$arity$4(activity_link,new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),oc.web.dispatcher.recently_posted_sort,finish_cb);
} else {
return null;
}
}));

(oc.web.actions.activity.all_posts_get.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.actions.activity.all_posts_get.cljs$lang$applyTo = (function (seq42524){
var G__42525 = cljs.core.first(seq42524);
var seq42524__$1 = cljs.core.next(seq42524);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__42525,seq42524__$1);
}));

oc.web.actions.activity.recent_all_posts_get = (function oc$web$actions$activity$recent_all_posts_get(var_args){
var args__4742__auto__ = [];
var len__4736__auto___43134 = arguments.length;
var i__4737__auto___43135 = (0);
while(true){
if((i__4737__auto___43135 < len__4736__auto___43134)){
args__4742__auto__.push((arguments[i__4737__auto___43135]));

var G__43137 = (i__4737__auto___43135 + (1));
i__4737__auto___43135 = G__43137;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.actions.activity.recent_all_posts_get.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.actions.activity.recent_all_posts_get.cljs$core$IFn$_invoke$arity$variadic = (function (org_data,p__42532){
var vec__42533 = p__42532;
var finish_cb = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42533,(0),null);
var temp__5735__auto__ = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"activity","GET"], 0));
if(cljs.core.truth_(temp__5735__auto__)){
var activity_link = temp__5735__auto__;
return oc.web.actions.activity.activity_real_get.cljs$core$IFn$_invoke$arity$4(activity_link,new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),oc.web.dispatcher.recent_activity_sort,finish_cb);
} else {
return null;
}
}));

(oc.web.actions.activity.recent_all_posts_get.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.actions.activity.recent_all_posts_get.cljs$lang$applyTo = (function (seq42530){
var G__42531 = cljs.core.first(seq42530);
var seq42530__$1 = cljs.core.next(seq42530);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__42531,seq42530__$1);
}));

/**
 * If the user is looking at the all-posts view we need to reload all the items that are visible right now.
 *   Instead, if the user is looking at another view we can just reload the first page.
 */
oc.web.actions.activity.all_posts_refresh = (function oc$web$actions$activity$all_posts_refresh(var_args){
var G__42537 = arguments.length;
switch (G__42537) {
case 0:
return oc.web.actions.activity.all_posts_refresh.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.actions.activity.all_posts_refresh.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.activity.all_posts_refresh.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.actions.activity.all_posts_refresh.cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.actions.activity.all_posts_refresh.cljs$core$IFn$_invoke$arity$1 = (function (org_data){
var temp__33762__auto__ = oc.web.dispatcher.all_posts_data.cljs$core$IFn$_invoke$arity$0();
if(cljs.core.truth_(temp__33762__auto__)){
var all_posts_data = temp__33762__auto__;
var temp__33762__auto____$1 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(all_posts_data),"refresh"], 0));
if(cljs.core.truth_(temp__33762__auto____$1)){
var refresh_link = temp__33762__auto____$1;
return oc.web.actions.activity.activity_real_get.cljs$core$IFn$_invoke$arity$5(refresh_link,new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),oc.web.dispatcher.recently_posted_sort,true,null);
} else {
return oc.web.actions.activity.all_posts_get(org_data);
}
} else {
return oc.web.actions.activity.all_posts_get(org_data);
}
}));

(oc.web.actions.activity.all_posts_refresh.cljs$lang$maxFixedArity = 1);

oc.web.actions.activity.all_posts_more_finish = (function oc$web$actions$activity$all_posts_more_finish(org_slug,sort_type,direction,p__42547){
var map__42551 = p__42547;
var map__42551__$1 = (((((!((map__42551 == null))))?(((((map__42551.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42551.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42551):map__42551);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42551__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42551__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
if(cljs.core.truth_(success)){
oc.web.actions.activity.request_reads_count(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(oc.web.lib.json.json__GT_cljs(body)))));
} else {
}

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("all-posts-more","finish","all-posts-more/finish",-1854949097),org_slug,sort_type,direction,(cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):null)], null));
});
oc.web.actions.activity.all_posts_more = (function oc$web$actions$activity$all_posts_more(more_link,direction){
oc.web.api.load_more_items(more_link,direction,cljs.core.partial.cljs$core$IFn$_invoke$arity$4(oc.web.actions.activity.all_posts_more_finish,oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0(),direction));

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"all-posts-more","all-posts-more",584168585),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0()], null));
});
oc.web.actions.activity.inbox_get_finish = (function oc$web$actions$activity$inbox_get_finish(org_slug,sort_type,p__42569){
var map__42570 = p__42569;
var map__42570__$1 = (((((!((map__42570 == null))))?(((((map__42570.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42570.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42570):map__42570);
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42570__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42570__$1,new cljs.core.Keyword(null,"success","success",1890645906));
if(cljs.core.truth_(body)){
var posts_data_key = oc.web.dispatcher.posts_data_key(org_slug);
var inbox_data = (cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):null);
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0(),"inbox")){
oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$3(oc.web.router.last_board_cookie(org_slug),"inbox",((((60) * (60)) * (24)) * (365)));

oc.web.actions.activity.request_reads_count(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(inbox_data))));

oc.web.actions.activity.watch_boards(new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(inbox_data)));
} else {
}

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("inbox-get","finish","inbox-get/finish",1442342723),org_slug,sort_type,inbox_data], null));
} else {
return null;
}
});
oc.web.actions.activity.inbox_get = (function oc$web$actions$activity$inbox_get(var_args){
var args__4742__auto__ = [];
var len__4736__auto___43152 = arguments.length;
var i__4737__auto___43153 = (0);
while(true){
if((i__4737__auto___43153 < len__4736__auto___43152)){
args__4742__auto__.push((arguments[i__4737__auto___43153]));

var G__43154 = (i__4737__auto___43153 + (1));
i__4737__auto___43153 = G__43154;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.actions.activity.inbox_get.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.actions.activity.inbox_get.cljs$core$IFn$_invoke$arity$variadic = (function (org_data,p__42574){
var vec__42575 = p__42574;
var finish_cb = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42575,(0),null);
var temp__5735__auto__ = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"following-inbox"], 0));
if(cljs.core.truth_(temp__5735__auto__)){
var inbox_link = temp__5735__auto__;
return oc.web.api.get_all_posts(inbox_link,(function (resp){
oc.web.actions.activity.inbox_get_finish(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),oc.web.dispatcher.recently_posted_sort,resp);

if(cljs.core.fn_QMARK_(finish_cb)){
return (finish_cb.cljs$core$IFn$_invoke$arity$1 ? finish_cb.cljs$core$IFn$_invoke$arity$1(resp) : finish_cb.call(null,resp));
} else {
return null;
}
}));
} else {
return null;
}
}));

(oc.web.actions.activity.inbox_get.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.actions.activity.inbox_get.cljs$lang$applyTo = (function (seq42572){
var G__42573 = cljs.core.first(seq42572);
var seq42572__$1 = cljs.core.next(seq42572);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__42573,seq42572__$1);
}));

oc.web.actions.activity.inbox_more_finish = (function oc$web$actions$activity$inbox_more_finish(org_slug,sort_type,direction,p__42630){
var map__42631 = p__42630;
var map__42631__$1 = (((((!((map__42631 == null))))?(((((map__42631.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42631.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42631):map__42631);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42631__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42631__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
if(cljs.core.truth_(success)){
oc.web.actions.activity.request_reads_count(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(oc.web.lib.json.json__GT_cljs(body)))));
} else {
}

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("inbox-more","finish","inbox-more/finish",705199254),org_slug,sort_type,direction,(cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):null)], null));
});
oc.web.actions.activity.inbox_more = (function oc$web$actions$activity$inbox_more(more_link,direction){
oc.web.api.load_more_items(more_link,direction,cljs.core.partial.cljs$core$IFn$_invoke$arity$4(oc.web.actions.activity.inbox_more_finish,oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.recently_posted_sort,direction));

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"inbox-more","inbox-more",517262879),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.recently_posted_sort], null));
});
oc.web.actions.activity.following_get_finish = (function oc$web$actions$activity$following_get_finish(org_slug,sort_type,keep_seen_at_QMARK_,refresh_QMARK_,p__42633){
var map__42634 = p__42633;
var map__42634__$1 = (((((!((map__42634 == null))))?(((((map__42634.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42634.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42634):map__42634);
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42634__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42634__$1,new cljs.core.Keyword(null,"success","success",1890645906));
if(cljs.core.truth_(body)){
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
var posts_data_key = oc.web.dispatcher.posts_data_key(org_slug);
var following_data = (cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):null);
var current_board_slug = oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0();
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(current_board_slug,"following")){
oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$3(oc.web.router.last_board_cookie(org_slug),"following",((((60) * (60)) * (24)) * (365)));

oc.web.actions.activity.request_reads_count(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(following_data))));

oc.web.actions.activity.watch_boards(new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(following_data)));
} else {
}

if(cljs.core.truth_(refresh_QMARK_)){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("following-get","finish","following-get/finish",-2078338056),org_slug,sort_type,current_board_slug,keep_seen_at_QMARK_,following_data], null));
} else {
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("following-get","finish","following-get/finish",-2078338056),org_slug,sort_type,current_board_slug,keep_seen_at_QMARK_,following_data], null));
}
} else {
return null;
}
});
oc.web.actions.activity.following_real_get = (function oc$web$actions$activity$following_real_get(var_args){
var G__42649 = arguments.length;
switch (G__42649) {
case 5:
return oc.web.actions.activity.following_real_get.cljs$core$IFn$_invoke$arity$5((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]));

break;
case 6:
return oc.web.actions.activity.following_real_get.cljs$core$IFn$_invoke$arity$6((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]),(arguments[(5)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.activity.following_real_get.cljs$core$IFn$_invoke$arity$5 = (function (following_link,org_slug,sort_type,keep_seen_at_QMARK_,finish_cb){
return oc.web.actions.activity.following_real_get.cljs$core$IFn$_invoke$arity$6(following_link,org_slug,sort_type,keep_seen_at_QMARK_,false,finish_cb);
}));

(oc.web.actions.activity.following_real_get.cljs$core$IFn$_invoke$arity$6 = (function (following_link,org_slug,sort_type,keep_seen_at_QMARK_,refresh_QMARK_,finish_cb){
return oc.web.api.get_all_posts(following_link,(function (resp){
oc.web.actions.activity.following_get_finish(org_slug,sort_type,keep_seen_at_QMARK_,refresh_QMARK_,resp);

if(cljs.core.fn_QMARK_(finish_cb)){
return (finish_cb.cljs$core$IFn$_invoke$arity$1 ? finish_cb.cljs$core$IFn$_invoke$arity$1(resp) : finish_cb.call(null,resp));
} else {
return null;
}
}));
}));

(oc.web.actions.activity.following_real_get.cljs$lang$maxFixedArity = 6);

oc.web.actions.activity.following_get = (function oc$web$actions$activity$following_get(var_args){
var G__42651 = arguments.length;
switch (G__42651) {
case 0:
return oc.web.actions.activity.following_get.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.actions.activity.following_get.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.actions.activity.following_get.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.actions.activity.following_get.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.activity.following_get.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.actions.activity.following_get.cljs$core$IFn$_invoke$arity$3(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0(),false,null);
}));

(oc.web.actions.activity.following_get.cljs$core$IFn$_invoke$arity$1 = (function (org_data){
return oc.web.actions.activity.following_get.cljs$core$IFn$_invoke$arity$3(org_data,false,null);
}));

(oc.web.actions.activity.following_get.cljs$core$IFn$_invoke$arity$2 = (function (org_data,finish_cb){
return oc.web.actions.activity.following_get.cljs$core$IFn$_invoke$arity$3(org_data,false,finish_cb);
}));

(oc.web.actions.activity.following_get.cljs$core$IFn$_invoke$arity$3 = (function (org_data,keep_seen_at_QMARK_,finish_cb){
var temp__5735__auto__ = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"following"], 0));
if(cljs.core.truth_(temp__5735__auto__)){
var following_link = temp__5735__auto__;
return oc.web.actions.activity.following_real_get.cljs$core$IFn$_invoke$arity$5(following_link,new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),oc.web.dispatcher.recently_posted_sort,keep_seen_at_QMARK_,finish_cb);
} else {
return null;
}
}));

(oc.web.actions.activity.following_get.cljs$lang$maxFixedArity = 3);

/**
 * If the user is looking at the following view we need to reload all the items that are visible right now.
 *   Instead, if the user is looking at another view we can just reload the first page.
 */
oc.web.actions.activity.following_refresh = (function oc$web$actions$activity$following_refresh(var_args){
var G__42661 = arguments.length;
switch (G__42661) {
case 0:
return oc.web.actions.activity.following_refresh.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.actions.activity.following_refresh.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.actions.activity.following_refresh.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.activity.following_refresh.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.actions.activity.following_refresh.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0(),true);
}));

(oc.web.actions.activity.following_refresh.cljs$core$IFn$_invoke$arity$1 = (function (org_data){
return oc.web.actions.activity.following_refresh.cljs$core$IFn$_invoke$arity$2(org_data,true);
}));

(oc.web.actions.activity.following_refresh.cljs$core$IFn$_invoke$arity$2 = (function (org_data,keep_seen_at_QMARK_){
var temp__33762__auto__ = oc.web.dispatcher.following_data.cljs$core$IFn$_invoke$arity$0();
if(cljs.core.truth_(temp__33762__auto__)){
var following_data = temp__33762__auto__;
var temp__33762__auto____$1 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(following_data),"refresh"], 0));
if(cljs.core.truth_(temp__33762__auto____$1)){
var refresh_link = temp__33762__auto____$1;
return oc.web.actions.activity.following_real_get.cljs$core$IFn$_invoke$arity$6(refresh_link,new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),oc.web.dispatcher.recently_posted_sort,keep_seen_at_QMARK_,true,null);
} else {
return oc.web.actions.activity.following_get.cljs$core$IFn$_invoke$arity$3(org_data,keep_seen_at_QMARK_,null);
}
} else {
return oc.web.actions.activity.following_get.cljs$core$IFn$_invoke$arity$3(org_data,keep_seen_at_QMARK_,null);
}
}));

(oc.web.actions.activity.following_refresh.cljs$lang$maxFixedArity = 2);

oc.web.actions.activity.following_did_change = (function oc$web$actions$activity$following_did_change(){
var current_board_slug = cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0());
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(current_board_slug,new cljs.core.Keyword(null,"following","following",-2049193617))){
return oc.web.actions.activity.following_refresh.cljs$core$IFn$_invoke$arity$2(org_data,true);
} else {
return oc.web.actions.activity.following_get.cljs$core$IFn$_invoke$arity$3(org_data,true,null);
}
});
oc.web.actions.activity.recent_following_get = (function oc$web$actions$activity$recent_following_get(var_args){
var G__42663 = arguments.length;
switch (G__42663) {
case 0:
return oc.web.actions.activity.recent_following_get.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.actions.activity.recent_following_get.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.actions.activity.recent_following_get.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.activity.recent_following_get.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.actions.activity.recent_following_get.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0(),null);
}));

(oc.web.actions.activity.recent_following_get.cljs$core$IFn$_invoke$arity$1 = (function (org_data){
return oc.web.actions.activity.recent_following_get.cljs$core$IFn$_invoke$arity$2(org_data,null);
}));

(oc.web.actions.activity.recent_following_get.cljs$core$IFn$_invoke$arity$2 = (function (org_data,finish_cb){
var temp__5735__auto__ = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"recent-following"], 0));
if(cljs.core.truth_(temp__5735__auto__)){
var recent_following_link = temp__5735__auto__;
return oc.web.actions.activity.following_real_get.cljs$core$IFn$_invoke$arity$5(recent_following_link,new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),oc.web.dispatcher.recent_activity_sort,false,finish_cb);
} else {
return null;
}
}));

(oc.web.actions.activity.recent_following_get.cljs$lang$maxFixedArity = 2);

oc.web.actions.activity.following_more_finish = (function oc$web$actions$activity$following_more_finish(org_slug,sort_type,direction,p__42664){
var map__42665 = p__42664;
var map__42665__$1 = (((((!((map__42665 == null))))?(((((map__42665.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42665.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42665):map__42665);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42665__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42665__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
if(cljs.core.truth_(success)){
oc.web.actions.activity.request_reads_count(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(oc.web.lib.json.json__GT_cljs(body)))));
} else {
}

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("following-more","finish","following-more/finish",940309697),org_slug,sort_type,direction,(cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):null)], null));
});
oc.web.actions.activity.following_more = (function oc$web$actions$activity$following_more(more_link,direction){
oc.web.api.load_more_items(more_link,direction,cljs.core.partial.cljs$core$IFn$_invoke$arity$4(oc.web.actions.activity.following_more_finish,oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0(),direction));

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"following-more","following-more",2064786273),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0()], null));
});
oc.web.actions.activity.replies_get_finish = (function oc$web$actions$activity$replies_get_finish(org_slug,sort_type,keep_seen_at_QMARK_,refresh_QMARK_,p__42687){
var map__42688 = p__42687;
var map__42688__$1 = (((((!((map__42688 == null))))?(((((map__42688.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42688.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42688):map__42688);
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42688__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42688__$1,new cljs.core.Keyword(null,"success","success",1890645906));
if(cljs.core.truth_(body)){
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
var posts_data_key = oc.web.dispatcher.posts_data_key(org_slug);
var replies_data = (cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):null);
var current_board_slug = oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0();
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(current_board_slug,"replies")){
oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$3(oc.web.router.last_board_cookie(org_slug),"replies",((((60) * (60)) * (24)) * (365)));

oc.web.actions.activity.request_reads_count(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(replies_data))));

oc.web.actions.activity.watch_boards(new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(replies_data)));
} else {
}

if(cljs.core.truth_(refresh_QMARK_)){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("replies-refresh","finish","replies-refresh/finish",-2015978772),org_slug,sort_type,current_board_slug,keep_seen_at_QMARK_,replies_data], null));
} else {
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("replies-get","finish","replies-get/finish",68864193),org_slug,sort_type,current_board_slug,keep_seen_at_QMARK_,replies_data], null));
}
} else {
return null;
}
});
oc.web.actions.activity.replies_real_get = (function oc$web$actions$activity$replies_real_get(var_args){
var G__42691 = arguments.length;
switch (G__42691) {
case 5:
return oc.web.actions.activity.replies_real_get.cljs$core$IFn$_invoke$arity$5((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]));

break;
case 6:
return oc.web.actions.activity.replies_real_get.cljs$core$IFn$_invoke$arity$6((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]),(arguments[(5)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.activity.replies_real_get.cljs$core$IFn$_invoke$arity$5 = (function (replies_link,org_slug,sort_type,keep_seen_at_QMARK_,finish_cb){
return oc.web.actions.activity.replies_real_get.cljs$core$IFn$_invoke$arity$6(replies_link,org_slug,sort_type,keep_seen_at_QMARK_,false,finish_cb);
}));

(oc.web.actions.activity.replies_real_get.cljs$core$IFn$_invoke$arity$6 = (function (replies_link,org_slug,sort_type,keep_seen_at_QMARK_,refresh_QMARK_,finish_cb){
return oc.web.api.get_all_posts(replies_link,(function (resp){
oc.web.actions.activity.replies_get_finish(org_slug,sort_type,keep_seen_at_QMARK_,refresh_QMARK_,resp);

if(cljs.core.fn_QMARK_(finish_cb)){
return (finish_cb.cljs$core$IFn$_invoke$arity$1 ? finish_cb.cljs$core$IFn$_invoke$arity$1(resp) : finish_cb.call(null,resp));
} else {
return null;
}
}));
}));

(oc.web.actions.activity.replies_real_get.cljs$lang$maxFixedArity = 6);

oc.web.actions.activity.replies_get = (function oc$web$actions$activity$replies_get(var_args){
var G__42697 = arguments.length;
switch (G__42697) {
case 0:
return oc.web.actions.activity.replies_get.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.actions.activity.replies_get.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.actions.activity.replies_get.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.actions.activity.replies_get.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.activity.replies_get.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.actions.activity.replies_get.cljs$core$IFn$_invoke$arity$3(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0(),false,null);
}));

(oc.web.actions.activity.replies_get.cljs$core$IFn$_invoke$arity$1 = (function (org_data){
return oc.web.actions.activity.replies_get.cljs$core$IFn$_invoke$arity$3(org_data,false,null);
}));

(oc.web.actions.activity.replies_get.cljs$core$IFn$_invoke$arity$2 = (function (org_data,finish_cb){
return oc.web.actions.activity.replies_get.cljs$core$IFn$_invoke$arity$3(org_data,false,finish_cb);
}));

(oc.web.actions.activity.replies_get.cljs$core$IFn$_invoke$arity$3 = (function (org_data,keep_seen_at_QMARK_,finish_cb){
var temp__5735__auto__ = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"replies"], 0));
if(cljs.core.truth_(temp__5735__auto__)){
var replies_link = temp__5735__auto__;
return oc.web.actions.activity.replies_real_get.cljs$core$IFn$_invoke$arity$5(replies_link,new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),oc.web.dispatcher.recent_activity_sort,keep_seen_at_QMARK_,finish_cb);
} else {
return null;
}
}));

(oc.web.actions.activity.replies_get.cljs$lang$maxFixedArity = 3);

oc.web.actions.activity.replies_refresh = (function oc$web$actions$activity$replies_refresh(var_args){
var G__42717 = arguments.length;
switch (G__42717) {
case 0:
return oc.web.actions.activity.replies_refresh.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.actions.activity.replies_refresh.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.actions.activity.replies_refresh.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.activity.replies_refresh.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.actions.activity.replies_refresh.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0(),true);
}));

(oc.web.actions.activity.replies_refresh.cljs$core$IFn$_invoke$arity$1 = (function (org_data){
return oc.web.actions.activity.replies_refresh.cljs$core$IFn$_invoke$arity$2(org_data,true);
}));

(oc.web.actions.activity.replies_refresh.cljs$core$IFn$_invoke$arity$2 = (function (org_data,keep_seen_at_QMARK_){
var temp__33762__auto__ = oc.web.dispatcher.following_data.cljs$core$IFn$_invoke$arity$0();
if(cljs.core.truth_(temp__33762__auto__)){
var replies_data = temp__33762__auto__;
var temp__33762__auto____$1 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(replies_data),"refresh"], 0));
if(cljs.core.truth_(temp__33762__auto____$1)){
var refresh_link = temp__33762__auto____$1;
return oc.web.actions.activity.replies_real_get.cljs$core$IFn$_invoke$arity$6(refresh_link,new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),oc.web.dispatcher.recently_posted_sort,keep_seen_at_QMARK_,true,null);
} else {
return oc.web.actions.activity.replies_get.cljs$core$IFn$_invoke$arity$3(org_data,keep_seen_at_QMARK_,null);
}
} else {
return oc.web.actions.activity.replies_get.cljs$core$IFn$_invoke$arity$3(org_data,keep_seen_at_QMARK_,null);
}
}));

(oc.web.actions.activity.replies_refresh.cljs$lang$maxFixedArity = 2);

oc.web.actions.activity.replies_more_finish = (function oc$web$actions$activity$replies_more_finish(org_slug,sort_type,direction,p__42718){
var map__42719 = p__42718;
var map__42719__$1 = (((((!((map__42719 == null))))?(((((map__42719.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42719.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42719):map__42719);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42719__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42719__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
if(cljs.core.truth_(success)){
oc.web.actions.activity.request_reads_count(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(oc.web.lib.json.json__GT_cljs(body)))));
} else {
}

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("replies-more","finish","replies-more/finish",-1231427368),org_slug,sort_type,direction,(cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):null)], null));
});
oc.web.actions.activity.replies_more = (function oc$web$actions$activity$replies_more(more_link,direction){
oc.web.api.load_more_items(more_link,direction,cljs.core.partial.cljs$core$IFn$_invoke$arity$4(oc.web.actions.activity.replies_more_finish,oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0(),direction));

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"replies-more","replies-more",-636194406),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0()], null));
});
oc.web.actions.activity.unfollowing_get_finish = (function oc$web$actions$activity$unfollowing_get_finish(org_slug,sort_type,refresh_QMARK_,p__42736){
var map__42737 = p__42736;
var map__42737__$1 = (((((!((map__42737 == null))))?(((((map__42737.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42737.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42737):map__42737);
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42737__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42737__$1,new cljs.core.Keyword(null,"success","success",1890645906));
if(cljs.core.truth_(body)){
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
var posts_data_key = oc.web.dispatcher.posts_data_key(org_slug);
var unfollowing_data = (cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):null);
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0(),"unfollowing")){
oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$3(oc.web.router.last_board_cookie(org_slug),"unfollowing",((((60) * (60)) * (24)) * (365)));

oc.web.actions.activity.request_reads_count(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(unfollowing_data))));

oc.web.actions.activity.watch_boards(new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(unfollowing_data)));
} else {
}

if(cljs.core.truth_(refresh_QMARK_)){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("unfollowing-refresh","finish","unfollowing-refresh/finish",148683740),org_slug,sort_type,unfollowing_data], null));
} else {
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("unfollowing-get","finish","unfollowing-get/finish",546005489),org_slug,sort_type,unfollowing_data], null));
}
} else {
return null;
}
});
oc.web.actions.activity.unfollowing_real_get = (function oc$web$actions$activity$unfollowing_real_get(var_args){
var G__42740 = arguments.length;
switch (G__42740) {
case 4:
return oc.web.actions.activity.unfollowing_real_get.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
case 5:
return oc.web.actions.activity.unfollowing_real_get.cljs$core$IFn$_invoke$arity$5((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.activity.unfollowing_real_get.cljs$core$IFn$_invoke$arity$4 = (function (unfollowing_link,org_slug,sort_type,finish_cb){
return oc.web.actions.activity.unfollowing_real_get.cljs$core$IFn$_invoke$arity$5(unfollowing_link,org_slug,sort_type,false,finish_cb);
}));

(oc.web.actions.activity.unfollowing_real_get.cljs$core$IFn$_invoke$arity$5 = (function (unfollowing_link,org_slug,sort_type,refresh_QMARK_,finish_cb){
return oc.web.api.get_all_posts(unfollowing_link,(function (resp){
oc.web.actions.activity.unfollowing_get_finish(org_slug,sort_type,refresh_QMARK_,resp);

if(cljs.core.fn_QMARK_(finish_cb)){
return (finish_cb.cljs$core$IFn$_invoke$arity$1 ? finish_cb.cljs$core$IFn$_invoke$arity$1(resp) : finish_cb.call(null,resp));
} else {
return null;
}
}));
}));

(oc.web.actions.activity.unfollowing_real_get.cljs$lang$maxFixedArity = 5);

oc.web.actions.activity.unfollowing_get = (function oc$web$actions$activity$unfollowing_get(var_args){
var args__4742__auto__ = [];
var len__4736__auto___43172 = arguments.length;
var i__4737__auto___43173 = (0);
while(true){
if((i__4737__auto___43173 < len__4736__auto___43172)){
args__4742__auto__.push((arguments[i__4737__auto___43173]));

var G__43174 = (i__4737__auto___43173 + (1));
i__4737__auto___43173 = G__43174;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.actions.activity.unfollowing_get.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.actions.activity.unfollowing_get.cljs$core$IFn$_invoke$arity$variadic = (function (org_data,p__42749){
var vec__42750 = p__42749;
var finish_cb = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42750,(0),null);
var temp__5735__auto__ = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"unfollowing"], 0));
if(cljs.core.truth_(temp__5735__auto__)){
var unfollowing_link = temp__5735__auto__;
return oc.web.actions.activity.unfollowing_real_get.cljs$core$IFn$_invoke$arity$4(unfollowing_link,new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),oc.web.dispatcher.recently_posted_sort,finish_cb);
} else {
return null;
}
}));

(oc.web.actions.activity.unfollowing_get.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.actions.activity.unfollowing_get.cljs$lang$applyTo = (function (seq42747){
var G__42748 = cljs.core.first(seq42747);
var seq42747__$1 = cljs.core.next(seq42747);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__42748,seq42747__$1);
}));

oc.web.actions.activity.recent_unfollowing_get = (function oc$web$actions$activity$recent_unfollowing_get(var_args){
var args__4742__auto__ = [];
var len__4736__auto___43175 = arguments.length;
var i__4737__auto___43176 = (0);
while(true){
if((i__4737__auto___43176 < len__4736__auto___43175)){
args__4742__auto__.push((arguments[i__4737__auto___43176]));

var G__43177 = (i__4737__auto___43176 + (1));
i__4737__auto___43176 = G__43177;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.actions.activity.recent_unfollowing_get.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.actions.activity.recent_unfollowing_get.cljs$core$IFn$_invoke$arity$variadic = (function (org_data,p__42755){
var vec__42756 = p__42755;
var finish_cb = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42756,(0),null);
var temp__5735__auto__ = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"recent-unfollowing"], 0));
if(cljs.core.truth_(temp__5735__auto__)){
var recent_unfollowing_link = temp__5735__auto__;
return oc.web.actions.activity.unfollowing_real_get.cljs$core$IFn$_invoke$arity$4(recent_unfollowing_link,new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),oc.web.dispatcher.recent_activity_sort,finish_cb);
} else {
return null;
}
}));

(oc.web.actions.activity.recent_unfollowing_get.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.actions.activity.recent_unfollowing_get.cljs$lang$applyTo = (function (seq42753){
var G__42754 = cljs.core.first(seq42753);
var seq42753__$1 = cljs.core.next(seq42753);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__42754,seq42753__$1);
}));

/**
 * If the user is looking at the unfollowing view we need to reload all the items that are visible right now.
 *   Instead, if the user is looking at another view we can just reload the first page.
 */
oc.web.actions.activity.unfollowing_refresh = (function oc$web$actions$activity$unfollowing_refresh(var_args){
var G__42760 = arguments.length;
switch (G__42760) {
case 0:
return oc.web.actions.activity.unfollowing_refresh.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.actions.activity.unfollowing_refresh.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.actions.activity.unfollowing_refresh.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.activity.unfollowing_refresh.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.actions.activity.unfollowing_refresh.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0(),true);
}));

(oc.web.actions.activity.unfollowing_refresh.cljs$core$IFn$_invoke$arity$1 = (function (org_data){
return oc.web.actions.activity.unfollowing_refresh.cljs$core$IFn$_invoke$arity$2(org_data,true);
}));

(oc.web.actions.activity.unfollowing_refresh.cljs$core$IFn$_invoke$arity$2 = (function (org_data,keep_seen_at_QMARK_){
var temp__33762__auto__ = oc.web.dispatcher.unfollowing_data.cljs$core$IFn$_invoke$arity$0();
if(cljs.core.truth_(temp__33762__auto__)){
var unfollowing_data = temp__33762__auto__;
var temp__33762__auto____$1 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(unfollowing_data),"refresh"], 0));
if(cljs.core.truth_(temp__33762__auto____$1)){
var refresh_link = temp__33762__auto____$1;
return oc.web.actions.activity.unfollowing_real_get.cljs$core$IFn$_invoke$arity$5(refresh_link,new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),oc.web.dispatcher.recently_posted_sort,true,null);
} else {
return oc.web.actions.activity.unfollowing_get.cljs$core$IFn$_invoke$arity$variadic(org_data,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([keep_seen_at_QMARK_,null], 0));
}
} else {
return oc.web.actions.activity.unfollowing_get.cljs$core$IFn$_invoke$arity$variadic(org_data,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([keep_seen_at_QMARK_,null], 0));
}
}));

(oc.web.actions.activity.unfollowing_refresh.cljs$lang$maxFixedArity = 2);

oc.web.actions.activity.unfollowing_more_finish = (function oc$web$actions$activity$unfollowing_more_finish(org_slug,sort_type,direction,p__42761){
var map__42762 = p__42761;
var map__42762__$1 = (((((!((map__42762 == null))))?(((((map__42762.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42762.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42762):map__42762);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42762__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42762__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
if(cljs.core.truth_(success)){
oc.web.actions.activity.request_reads_count(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(oc.web.lib.json.json__GT_cljs(body)))));
} else {
}

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("unfollowing-more","finish","unfollowing-more/finish",-870420952),org_slug,sort_type,direction,(cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):null)], null));
});
oc.web.actions.activity.unfollowing_more = (function oc$web$actions$activity$unfollowing_more(more_link,direction){
oc.web.api.load_more_items(more_link,direction,cljs.core.partial.cljs$core$IFn$_invoke$arity$4(oc.web.actions.activity.unfollowing_more_finish,oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0(),direction));

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"unfollowing-more","unfollowing-more",-1771670350),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0()], null));
});
oc.web.actions.activity.refresh_org_data_cb = (function oc$web$actions$activity$refresh_org_data_cb(var_args){
var G__42766 = arguments.length;
switch (G__42766) {
case 1:
return oc.web.actions.activity.refresh_org_data_cb.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 3:
return oc.web.actions.activity.refresh_org_data_cb.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.activity.refresh_org_data_cb.cljs$core$IFn$_invoke$arity$1 = (function (resp){
return oc.web.actions.activity.refresh_org_data_cb.cljs$core$IFn$_invoke$arity$3(oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0(),resp);
}));

(oc.web.actions.activity.refresh_org_data_cb.cljs$core$IFn$_invoke$arity$3 = (function (board_slug,sort_type,p__42767){
var map__42768 = p__42767;
var map__42768__$1 = (((((!((map__42768 == null))))?(((((map__42768.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42768.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42768):map__42768);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42768__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42768__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42768__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var org_data = oc.web.lib.json.json__GT_cljs(body);
var board_kw = cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(board_slug);
var is_all_posts = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(board_kw,new cljs.core.Keyword(null,"all-posts","all-posts",-1285476533));
var is_bookmarks = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(board_kw,new cljs.core.Keyword(null,"bookmarks","bookmarks",1877375283));
var is_inbox = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(board_kw,new cljs.core.Keyword(null,"inbox","inbox",1888669443));
var is_following = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(board_kw,new cljs.core.Keyword(null,"following","following",-2049193617));
var is_unfollowing = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(board_kw,new cljs.core.Keyword(null,"unfollowing","unfollowing",-1076165830));
var is_replies = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(board_kw,new cljs.core.Keyword(null,"replies","replies",-1389888974));
var is_drafts = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(board_slug,oc.web.lib.utils.default_drafts_board_slug);
var active_users = oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$0();
var is_contributions_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(active_users,board_slug);
var board_data = oc.web.dispatcher.org_board_data.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([org_data,board_slug], 0));
var board_link = (((((!(is_all_posts))) && ((!(is_bookmarks))) && ((!(is_inbox)))))?oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(board_data),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["item","self"], null),"GET"], 0)):null);
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"org-loaded","org-loaded",-1554103541),org_data], null));

if(((is_all_posts) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(sort_type,oc.web.dispatcher.recently_posted_sort)))){
return oc.web.actions.activity.all_posts_get(org_data);
} else {
if(((is_all_posts) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(sort_type,oc.web.dispatcher.recent_activity_sort)))){
return oc.web.actions.activity.recent_all_posts_get(org_data);
} else {
if(is_bookmarks){
return oc.web.actions.activity.bookmarks_get(org_data);
} else {
if(((is_following) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(sort_type,oc.web.dispatcher.recently_posted_sort)))){
return oc.web.actions.activity.following_get.cljs$core$IFn$_invoke$arity$1(org_data);
} else {
if(((is_following) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(sort_type,oc.web.dispatcher.recent_activity_sort)))){
return oc.web.actions.activity.recent_following_get.cljs$core$IFn$_invoke$arity$1(org_data);
} else {
if(((is_unfollowing) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(sort_type,oc.web.dispatcher.recently_posted_sort)))){
return oc.web.actions.activity.unfollowing_get(org_data);
} else {
if(((is_unfollowing) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(sort_type,oc.web.dispatcher.recent_activity_sort)))){
return oc.web.actions.activity.recent_unfollowing_get(org_data);
} else {
if(is_replies){
return oc.web.actions.activity.replies_get.cljs$core$IFn$_invoke$arity$1(org_data);
} else {
if(cljs.core.truth_(is_contributions_QMARK_)){
return oc.web.actions.contributions.contributions_get.cljs$core$IFn$_invoke$arity$1(board_slug);
} else {
if(cljs.core.map_QMARK_(board_link)){
return oc.web.actions.section.section_get(board_slug,board_link);
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
}));

(oc.web.actions.activity.refresh_org_data_cb.cljs$lang$maxFixedArity = 3);

oc.web.actions.activity.refresh_org_data = (function oc$web$actions$activity$refresh_org_data(){
var org_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0()),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["item","self"], null),"GET"], 0));
return oc.web.api.get_org(org_link,oc.web.actions.activity.refresh_org_data_cb);
});
oc.web.actions.activity.entry_edit = (function oc$web$actions$activity$entry_edit(initial_entry_data){
oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$3(oc.web.actions.cmail.edit_open_cookie(),(function (){var or__4126__auto__ = [cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(initial_entry_data)),"/",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(initial_entry_data))].join('');
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return true;
}
})(),((60) * (30)));

return oc.web.actions.cmail.load_cached_item(initial_entry_data,new cljs.core.Keyword(null,"entry-editing","entry-editing",-1938994964));
});
oc.web.actions.activity.entry_edit_dismiss = (function oc$web$actions$activity$entry_edit_dismiss(){
if(cljs.core.truth_(oc.web.dispatcher.current_activity_id.cljs$core$IFn$_invoke$arity$0())){
oc.web.lib.utils.after((1),(function (){
var is_all_posts = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0(),"all-posts");
var is_must_see = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0(),"must-see");
return oc.web.router.nav_BANG_(((is_all_posts)?oc.web.urls.all_posts.cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0()):((is_must_see)?oc.web.urls.must_see.cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0()):oc.web.urls.board.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0())
)));
}));
} else {
}

oc.web.lib.utils.after((1000),(function (){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"entry-edit-dissmissing","entry-edit-dissmissing",-1058375764)], null),false], null));
}));

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("entry-edit","dismiss","entry-edit/dismiss",-1867820720)], null));
});
oc.web.actions.activity.entry_save_on_exit = (function oc$web$actions$activity$entry_save_on_exit(var_args){
var G__42773 = arguments.length;
switch (G__42773) {
case 4:
return oc.web.actions.activity.entry_save_on_exit.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
case 5:
return oc.web.actions.activity.entry_save_on_exit.cljs$core$IFn$_invoke$arity$5((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.activity.entry_save_on_exit.cljs$core$IFn$_invoke$arity$4 = (function (edit_key,entry_data,entry_body,section_editing){
return oc.web.actions.activity.entry_save_on_exit.cljs$core$IFn$_invoke$arity$5(edit_key,entry_data,entry_body,section_editing,null);
}));

(oc.web.actions.activity.entry_save_on_exit.cljs$core$IFn$_invoke$arity$5 = (function (edit_key,entry_data,entry_body,section_editing,callback){
var entry_map = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(entry_data,new cljs.core.Keyword(null,"body","body",-2049205669),entry_body);
var cache_key = oc.web.actions.cmail.get_entry_cache_key(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data));
return oc.web.lib.user_cache.set_item.cljs$core$IFn$_invoke$arity$variadic(cache_key,cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(entry_map,new cljs.core.Keyword(null,"auto-saving","auto-saving",68752642)),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(function (err){
if(cljs.core.truth_(err)){
return null;
} else {
if(cljs.core.truth_(((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2("published",new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(entry_map)))?(function (){var and__4115__auto__ = new cljs.core.Keyword(null,"has-changes","has-changes",-631476764).cljs$core$IFn$_invoke$arity$1(entry_map);
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(new cljs.core.Keyword(null,"auto-saving","auto-saving",68752642).cljs$core$IFn$_invoke$arity$1(entry_map));
} else {
return and__4115__auto__;
}
})():false))){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [edit_key], null),(function (p1__42770_SHARP_){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__42770_SHARP_,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"auto-saving","auto-saving",68752642),true,new cljs.core.Keyword(null,"body","body",-2049205669),new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(entry_map)], null)], 0));
})], null));

var G__42774_43181 = edit_key;
var G__42775_43182 = entry_map;
var G__42776_43183 = section_editing;
var G__42777_43184 = (function (entry_data_saved,edit_key_saved,p__42778){
var map__42779 = p__42778;
var map__42779__$1 = (((((!((map__42779 == null))))?(((((map__42779.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42779.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42779):map__42779);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42779__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42779__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42779__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
if(cljs.core.not(success)){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [edit_key], null),(function (p1__42771_SHARP_){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__42771_SHARP_,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"auto-saving","auto-saving",68752642),false,new cljs.core.Keyword(null,"has-changes","has-changes",-631476764),true], null)], 0));
})], null));
} else {
var json_body_43185 = oc.web.lib.json.json__GT_cljs(body);
var board_data_43186 = (cljs.core.truth_(new cljs.core.Keyword(null,"entries","entries",-86943161).cljs$core$IFn$_invoke$arity$1(json_body_43185))?oc.web.utils.activity.parse_board.cljs$core$IFn$_invoke$arity$1(json_body_43185):false);
var fixed_items_43187 = new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(board_data_43186);
var entry_saved_43188 = (cljs.core.truth_(fixed_items_43187)?cljs.core.first(cljs.core.vals(fixed_items_43187)):json_body_43185);
oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$3(oc.web.actions.cmail.edit_open_cookie(),[cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(entry_saved_43188)),"/",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_saved_43188))].join(''),((((60) * (60)) * (24)) * (365)));

if(cljs.core.truth_((((new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_map) == null))?new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_saved_43188):false))){
oc.web.actions.cmail.remove_cached_item(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_map));
} else {
}

if((cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.actions.activity.initial_revision),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_saved_43188)) == null)){
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(oc.web.actions.activity.initial_revision,cljs.core.assoc,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_saved_43188),(function (){var or__4126__auto__ = new cljs.core.Keyword(null,"revision-id","revision-id",1301980641).cljs$core$IFn$_invoke$arity$1(entry_map);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return (-1);
}
})());
} else {
}

if(cljs.core.truth_(board_data_43186)){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("entry-save-with-board","finish","entry-save-with-board/finish",916268014),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),board_data_43186], null));
} else {
}

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("entry-auto-save","finish","entry-auto-save/finish",292395922),entry_saved_43188,edit_key,entry_map], null));
}

if(cljs.core.fn_QMARK_(callback)){
return (callback.cljs$core$IFn$_invoke$arity$1 ? callback.cljs$core$IFn$_invoke$arity$1(success) : callback.call(null,success));
} else {
return null;
}
});
(oc.web.actions.activity.entry_save.cljs$core$IFn$_invoke$arity$4 ? oc.web.actions.activity.entry_save.cljs$core$IFn$_invoke$arity$4(G__42774_43181,G__42775_43182,G__42776_43183,G__42777_43184) : oc.web.actions.activity.entry_save.call(null,G__42774_43181,G__42775_43182,G__42776_43183,G__42777_43184));

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"entry-toggle-save-on-exit","entry-toggle-save-on-exit",-963399746),false], null));
} else {
return null;
}
}
})], 0));
}));

(oc.web.actions.activity.entry_save_on_exit.cljs$lang$maxFixedArity = 5);

oc.web.actions.activity.entry_toggle_save_on_exit = (function oc$web$actions$activity$entry_toggle_save_on_exit(enable_QMARK_){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"entry-toggle-save-on-exit","entry-toggle-save-on-exit",-963399746),enable_QMARK_], null));
});
oc.web.actions.activity.entry_save_finish = (function oc$web$actions$activity$entry_save_finish(board_slug,entry_data,initial_uuid,edit_key){
var org_slug = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
var is_published_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(entry_data),"published");
if(cljs.core.truth_((function (){var and__4115__auto__ = oc.web.dispatcher.current_activity_id.cljs$core$IFn$_invoke$arity$0();
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(board_slug,oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0());
} else {
return and__4115__auto__;
}
})())){
oc.web.router.nav_BANG_(oc.web.urls.entry.cljs$core$IFn$_invoke$arity$3(org_slug,board_slug,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data)));
} else {
}

oc.web.utils.activity.save_last_used_section(board_slug);

if(is_published_QMARK_){
} else {
oc.web.actions.section.drafts_get();
}

oc.web.actions.cmail.remove_cached_item(initial_uuid);

cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(oc.web.actions.activity.initial_revision,cljs.core.dissoc,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data));

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("entry-save","finish","entry-save/finish",1157760970),cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(entry_data,new cljs.core.Keyword(null,"board-slug","board-slug",99003663),board_slug),edit_key], null));

if(is_published_QMARK_){
var G__42783_43189 = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data);
(oc.web.actions.activity.send_item_read.cljs$core$IFn$_invoke$arity$1 ? oc.web.actions.activity.send_item_read.cljs$core$IFn$_invoke$arity$1(G__42783_43189) : oc.web.actions.activity.send_item_read.call(null,G__42783_43189));

return oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"title","title",636505583),"Changes have been saved",new cljs.core.Keyword(null,"primary-bt-dismiss","primary-bt-dismiss",-820688058),true,new cljs.core.Keyword(null,"primary-bt-title","primary-bt-title",653140150),"OK",new cljs.core.Keyword(null,"primary-bt-inline","primary-bt-inline",-796141614),true,new cljs.core.Keyword(null,"expire","expire",-70657108),(3),new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"entry-updated-notification","entry-updated-notification",1268334412)], null));
} else {
return null;
}
});
oc.web.actions.activity.create_update_entry_cb = (function oc$web$actions$activity$create_update_entry_cb(entry_data,edit_key,p__42784){
var map__42785 = p__42784;
var map__42785__$1 = (((((!((map__42785 == null))))?(((((map__42785.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42785.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42785):map__42785);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42785__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42785__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42785__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
if(cljs.core.truth_(success)){
return oc.web.actions.activity.entry_save_finish(new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(entry_data),(cljs.core.truth_(body)?oc.web.lib.json.json__GT_cljs(body):null),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data),edit_key);
} else {
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("entry-save","failed","entry-save/failed",613328806),edit_key], null));
}
});
oc.web.actions.activity.board_name_exists_error = (function oc$web$actions$activity$board_name_exists_error(edit_key){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [edit_key], null),(function (p1__42787_SHARP_){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__42787_SHARP_,new cljs.core.Keyword(null,"section-name-error","section-name-error",-581249210),oc.web.lib.utils.section_name_exists_error),new cljs.core.Keyword(null,"loading","loading",-737050189));
})], null));
});
oc.web.actions.activity.add_attachment = (function oc$web$actions$activity$add_attachment(dispatch_input_key,attachment_data){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"activity-add-attachment","activity-add-attachment",-637529825),dispatch_input_key,attachment_data], null));

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [dispatch_input_key,new cljs.core.Keyword(null,"has-changes","has-changes",-631476764)], null),true], null));
});
oc.web.actions.activity.remove_attachment = (function oc$web$actions$activity$remove_attachment(dispatch_input_key,attachment_data){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"activity-remove-attachment","activity-remove-attachment",1236800634),dispatch_input_key,attachment_data], null));

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [dispatch_input_key,new cljs.core.Keyword(null,"has-changes","has-changes",-631476764)], null),true], null));
});
oc.web.actions.activity.get_entry = (function oc$web$actions$activity$get_entry(entry_data){
if(cljs.core.truth_(oc.web.dispatcher.current_secure_activity_id.cljs$core$IFn$_invoke$arity$0())){
return (oc.web.actions.activity.secure_activity_get.cljs$core$IFn$_invoke$arity$0 ? oc.web.actions.activity.secure_activity_get.cljs$core$IFn$_invoke$arity$0() : oc.web.actions.activity.secure_activity_get.call(null));
} else {
var entry_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(entry_data),"self"], 0));
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"activity-get","activity-get",1006703884),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"org-slug","org-slug",-726595051),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"board-slug","board-slug",99003663),new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(entry_data),new cljs.core.Keyword(null,"activity-uuid","activity-uuid",-663317778),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data)], null)], null));

return oc.web.api.get_entry(entry_link,(function (p__42788){
var map__42789 = p__42788;
var map__42789__$1 = (((((!((map__42789 == null))))?(((((map__42789.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42789.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42789):map__42789);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42789__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42789__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42789__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,(404))) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data),oc.web.dispatcher.current_activity_id.cljs$core$IFn$_invoke$arity$0())))){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("activity-get","not-found","activity-get/not-found",998070428),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data),null], null));

return oc.web.actions.routing.maybe_404.cljs$core$IFn$_invoke$arity$0();
} else {
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("activity-get","finish","activity-get/finish",538149738),status,oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.lib.json.json__GT_cljs(body),null], null));
}
}));
}
});
oc.web.actions.activity.entry_clear_local_cache = (function oc$web$actions$activity$entry_clear_local_cache(item_uuid,edit_key,item){

oc.web.actions.cmail.remove_cached_item(item_uuid);

taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.actions.activity",null,578,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Reverting to ",cljs.core.deref(oc.web.actions.activity.initial_revision),item_uuid], null);
}),null)),null,1204653386);

if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2("published",new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(item))){
var temp__5735__auto___43197 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.actions.activity.initial_revision),item_uuid);
if(cljs.core.truth_(temp__5735__auto___43197)){
var revision_id_43198 = temp__5735__auto___43197;
(oc.web.actions.activity.entry_revert.cljs$core$IFn$_invoke$arity$2 ? oc.web.actions.activity.entry_revert.cljs$core$IFn$_invoke$arity$2(revision_id_43198,item) : oc.web.actions.activity.entry_revert.call(null,revision_id_43198,item));
} else {
}
} else {
}

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"entry-clear-local-cache","entry-clear-local-cache",-1029730143),edit_key], null));
});
oc.web.actions.activity.entry_save = (function oc$web$actions$activity$entry_save(var_args){
var G__42806 = arguments.length;
switch (G__42806) {
case 3:
return oc.web.actions.activity.entry_save.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 4:
return oc.web.actions.activity.entry_save.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.activity.entry_save.cljs$core$IFn$_invoke$arity$3 = (function (edit_key,edited_data,section_editing){
return oc.web.actions.activity.entry_save.cljs$core$IFn$_invoke$arity$4(edit_key,edited_data,section_editing,oc.web.actions.activity.create_update_entry_cb);
}));

(oc.web.actions.activity.entry_save.cljs$core$IFn$_invoke$arity$4 = (function (edit_key,edited_data,section_editing,entry_save_cb){
if(oc.web.actions.payments.show_paywall_alert_QMARK_(oc.web.dispatcher.payments_data.cljs$core$IFn$_invoke$arity$0())){
return null;
} else {
var publisher_board = cljs.core.some((function (p1__42800_SHARP_){
if(cljs.core.truth_(new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803).cljs$core$IFn$_invoke$arity$1(p1__42800_SHARP_))){
return p1__42800_SHARP_;
} else {
return null;
}
}),oc.web.dispatcher.editable_boards_data.cljs$core$IFn$_invoke$arity$0());
var fixed_edited_data = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([edited_data,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"status","status",-1997798413),(function (){var or__4126__auto__ = new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(edited_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "draft";
}
})(),new cljs.core.Keyword(null,"board-slug","board-slug",99003663),(cljs.core.truth_((function (){var and__4115__auto__ = publisher_board;
if(cljs.core.truth_(and__4115__auto__)){
return new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803).cljs$core$IFn$_invoke$arity$1(edited_data);
} else {
return and__4115__auto__;
}
})())?new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(publisher_board):new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(edited_data))], null)], 0));
var fixed_edit_key = (function (){var or__4126__auto__ = edit_key;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"entry-editing","entry-editing",-1938994964);
}
})();
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"entry-save","entry-save",-1431215303),fixed_edit_key], null));

if(cljs.core.truth_(new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(fixed_edited_data))){
if(cljs.core.truth_(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(fixed_edited_data),oc.web.lib.utils.default_section_slug))?new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803).cljs$core$IFn$_invoke$arity$1(fixed_edited_data):false))){
var fixed_entry_data = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(fixed_edited_data,new cljs.core.Keyword(null,"board-slug","board-slug",99003663),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"board-name","board-name",-677515056),new cljs.core.Keyword(null,"invite-note","invite-note",38467972),new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803)], 0));
var final_board_data = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"board-name","board-name",-677515056).cljs$core$IFn$_invoke$arity$1(fixed_edited_data),new cljs.core.Keyword(null,"entries","entries",-86943161),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [fixed_entry_data], null),new cljs.core.Keyword(null,"access","access",2027349272),new cljs.core.Keyword(null,"board-access","board-access",1233510317).cljs$core$IFn$_invoke$arity$1(fixed_edited_data),new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803),new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803).cljs$core$IFn$_invoke$arity$1(fixed_edited_data)], null);
var create_board_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"create"], 0));
return oc.web.api.create_board(create_board_link,final_board_data,new cljs.core.Keyword(null,"invite-note","invite-note",38467972).cljs$core$IFn$_invoke$arity$1(fixed_edited_data),(function (p__42829){
var map__42830 = p__42829;
var map__42830__$1 = (((((!((map__42830 == null))))?(((((map__42830.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42830.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42830):map__42830);
var response = map__42830__$1;
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42830__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42830__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42830__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,(409))){
return oc.web.actions.activity.board_name_exists_error(fixed_edit_key);
} else {
var response_board_data = (cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):null);
var updated_entry_data = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([fixed_edited_data,new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"board-name","board-name",-677515056),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(response_board_data),new cljs.core.Keyword(null,"board-slug","board-slug",99003663),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(response_board_data),new cljs.core.Keyword(null,"board-access","board-access",1233510317),new cljs.core.Keyword(null,"access","access",2027349272).cljs$core$IFn$_invoke$arity$1(response_board_data),new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803),new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803).cljs$core$IFn$_invoke$arity$1(response_board_data),new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(response_board_data)], null)], 0));
return (entry_save_cb.cljs$core$IFn$_invoke$arity$3 ? entry_save_cb.cljs$core$IFn$_invoke$arity$3(updated_entry_data,fixed_edit_key,response) : entry_save_cb.call(null,updated_entry_data,fixed_edit_key,response));
}
}));
} else {
var patch_entry_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(edited_data),"partial-update"], 0));
return oc.web.api.patch_entry(patch_entry_link,fixed_edited_data,fixed_edit_key,entry_save_cb);
}
} else {
if(cljs.core.truth_(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(fixed_edited_data),oc.web.lib.utils.default_section_slug))?new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803).cljs$core$IFn$_invoke$arity$1(fixed_edited_data):false))){
var fixed_entry_data = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(fixed_edited_data,new cljs.core.Keyword(null,"board-slug","board-slug",99003663),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"board-name","board-name",-677515056),new cljs.core.Keyword(null,"invite-note","invite-note",38467972),new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803)], 0));
var final_board_data = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"board-name","board-name",-677515056).cljs$core$IFn$_invoke$arity$1(fixed_edited_data),new cljs.core.Keyword(null,"entries","entries",-86943161),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [fixed_entry_data], null),new cljs.core.Keyword(null,"access","access",2027349272),new cljs.core.Keyword(null,"board-access","board-access",1233510317).cljs$core$IFn$_invoke$arity$1(fixed_edited_data),new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803),new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803).cljs$core$IFn$_invoke$arity$1(fixed_edited_data)], null);
var create_board_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"create"], 0));
return oc.web.api.create_board(create_board_link,final_board_data,new cljs.core.Keyword(null,"invite-note","invite-note",38467972).cljs$core$IFn$_invoke$arity$1(fixed_edited_data),(function (p__42832){
var map__42833 = p__42832;
var map__42833__$1 = (((((!((map__42833 == null))))?(((((map__42833.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42833.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42833):map__42833);
var response = map__42833__$1;
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42833__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42833__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42833__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,(409))){
return oc.web.actions.activity.board_name_exists_error(fixed_edit_key);
} else {
var response_board_data = (cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):null);
var updated_entry_data = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([fixed_edited_data,new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"board-name","board-name",-677515056),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(response_board_data),new cljs.core.Keyword(null,"board-slug","board-slug",99003663),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(response_board_data),new cljs.core.Keyword(null,"board-access","board-access",1233510317),new cljs.core.Keyword(null,"access","access",2027349272).cljs$core$IFn$_invoke$arity$1(response_board_data),new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803),new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803).cljs$core$IFn$_invoke$arity$1(response_board_data),new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(response_board_data)], null)], 0));
return (entry_save_cb.cljs$core$IFn$_invoke$arity$3 ? entry_save_cb.cljs$core$IFn$_invoke$arity$3(updated_entry_data,fixed_edit_key,response) : entry_save_cb.call(null,updated_entry_data,fixed_edit_key,response));
}
}));
} else {
var org_slug = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
var entry_board_data = oc.web.dispatcher.org_board_data.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([org_data,new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(fixed_edited_data)], 0));
var entry_create_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(entry_board_data),"create"], 0));
return oc.web.api.create_entry(entry_create_link,fixed_edited_data,fixed_edit_key,entry_save_cb);
}
}
}
}));

(oc.web.actions.activity.entry_save.cljs$lang$maxFixedArity = 4);

oc.web.actions.activity.entry_publish_finish = (function oc$web$actions$activity$entry_publish_finish(initial_uuid,edit_key,org_slug,board_slug,entry_data){
oc.web.utils.activity.save_last_used_section(board_slug);

oc.web.actions.cmail.remove_cached_item(initial_uuid);

cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(oc.web.actions.activity.initial_revision,cljs.core.dissoc,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data));

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("entry-publish","finish","entry-publish/finish",-808852722),org_slug,edit_key,entry_data], null));

var G__42835_43209 = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data);
(oc.web.actions.activity.send_item_read.cljs$core$IFn$_invoke$arity$1 ? oc.web.actions.activity.send_item_read.cljs$core$IFn$_invoke$arity$1(G__42835_43209) : oc.web.actions.activity.send_item_read.call(null,G__42835_43209));

oc.web.actions.nux.show_post_added_tooltip(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data));

var drafts_board = oc.web.dispatcher.org_board_data.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([oc.web.lib.utils.default_drafts_board_slug], 0));
var drafts_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(drafts_board),"self"], 0));
if(cljs.core.truth_(drafts_link)){
return oc.web.actions.section.section_get(oc.web.lib.utils.default_drafts_board_slug,drafts_link);
} else {
return null;
}
});
oc.web.actions.activity.entry_publish_cb = (function oc$web$actions$activity$entry_publish_cb(entry_uuid,posted_to_board_slug,edit_key,p__42836){
var map__42837 = p__42836;
var map__42837__$1 = (((((!((map__42837 == null))))?(((((map__42837.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42837.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42837):map__42837);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42837__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42837__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42837__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
if(cljs.core.truth_(success)){
return oc.web.actions.activity.entry_publish_finish(entry_uuid,edit_key,oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),posted_to_board_slug,(cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):null));
} else {
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("entry-publish","failed","entry-publish/failed",-1116278806),edit_key], null));
}
});
oc.web.actions.activity.entry_publish_with_board_finish = (function oc$web$actions$activity$entry_publish_with_board_finish(entry_uuid,edit_key,new_board_data){
var board_slug = new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(new_board_data);
var saved_entry_data = cljs.core.first(new cljs.core.Keyword(null,"entries","entries",-86943161).cljs$core$IFn$_invoke$arity$1(new_board_data));
oc.web.utils.activity.save_last_used_section(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(new_board_data));

oc.web.actions.cmail.remove_cached_item(entry_uuid);

cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(oc.web.actions.activity.initial_revision,cljs.core.dissoc,entry_uuid);

if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(new_board_data),oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0())){
} else {
oc.web.ws.change_client.container_watch.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(new_board_data)], 0));
}

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("entry-publish-with-board","finish","entry-publish-with-board/finish",-759409110),new_board_data,edit_key], null));

var G__42858_43211 = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(saved_entry_data);
(oc.web.actions.activity.send_item_read.cljs$core$IFn$_invoke$arity$1 ? oc.web.actions.activity.send_item_read.cljs$core$IFn$_invoke$arity$1(G__42858_43211) : oc.web.actions.activity.send_item_read.call(null,G__42858_43211));

return oc.web.actions.nux.show_post_added_tooltip(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(saved_entry_data));
});
oc.web.actions.activity.entry_publish_with_board_cb = (function oc$web$actions$activity$entry_publish_with_board_cb(entry_uuid,edit_key,p__42859){
var map__42860 = p__42859;
var map__42860__$1 = (((((!((map__42860 == null))))?(((((map__42860.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42860.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42860):map__42860);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42860__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42860__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42860__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,(409))){
return oc.web.actions.activity.board_name_exists_error(new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167));
} else {
return oc.web.actions.activity.entry_publish_with_board_finish(entry_uuid,edit_key,(cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):null));
}
});
oc.web.actions.activity.entry_publish = (function oc$web$actions$activity$entry_publish(var_args){
var args__4742__auto__ = [];
var len__4736__auto___43213 = arguments.length;
var i__4737__auto___43214 = (0);
while(true){
if((i__4737__auto___43214 < len__4736__auto___43213)){
args__4742__auto__.push((arguments[i__4737__auto___43214]));

var G__43216 = (i__4737__auto___43214 + (1));
i__4737__auto___43214 = G__43216;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((2) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((2)),(0),null)):null);
return oc.web.actions.activity.entry_publish.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__4743__auto__);
});

(oc.web.actions.activity.entry_publish.cljs$core$IFn$_invoke$arity$variadic = (function (entry_editing,section_editing,p__42865){
var vec__42866 = p__42865;
var edit_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42866,(0),null);
if(oc.web.actions.payments.show_paywall_alert_QMARK_(oc.web.dispatcher.payments_data.cljs$core$IFn$_invoke$arity$0())){
return null;
} else {
var fixed_edit_key = (function (){var or__4126__auto__ = edit_key;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"entry-editing","entry-editing",-1938994964);
}
})();
if(cljs.core.truth_(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.app_state,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [edit_key,new cljs.core.Keyword(null,"auto-saving","auto-saving",68752642)], null)))){
return oc.web.lib.utils.after((1000),(function (){
return oc.web.actions.activity.entry_publish.cljs$core$IFn$_invoke$arity$variadic(entry_editing,section_editing,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([edit_key], 0));
}));
} else {
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
var fixed_entry_editing = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(entry_editing,new cljs.core.Keyword(null,"status","status",-1997798413),"published");
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"entry-publish","entry-publish",-395041510),fixed_edit_key], null));

if(cljs.core.truth_(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(fixed_entry_editing),oc.web.lib.utils.default_section_slug))?section_editing:false))){
var fixed_entry_data = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(fixed_entry_editing,new cljs.core.Keyword(null,"board-slug","board-slug",99003663),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"board-name","board-name",-677515056),new cljs.core.Keyword(null,"invite-note","invite-note",38467972),new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803)], 0));
var final_board_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(section_editing,new cljs.core.Keyword(null,"entries","entries",-86943161),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [fixed_entry_data], null));
var create_board_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"create"], 0));
return oc.web.api.create_board(create_board_link,final_board_data,new cljs.core.Keyword(null,"invite-note","invite-note",38467972).cljs$core$IFn$_invoke$arity$1(section_editing),cljs.core.partial.cljs$core$IFn$_invoke$arity$3(oc.web.actions.activity.entry_publish_with_board_cb,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(fixed_entry_editing),fixed_edit_key));
} else {
var entry_exists_QMARK_ = cljs.core.seq(new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(fixed_entry_editing));
var board_data = oc.web.dispatcher.org_board_data.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([org_data,new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(fixed_entry_editing)], 0));
var publish_entry_link = ((entry_exists_QMARK_)?oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(fixed_entry_editing),"publish"], 0)):oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(board_data),"create"], 0)));
return oc.web.api.publish_entry(publish_entry_link,fixed_entry_editing,cljs.core.partial.cljs$core$IFn$_invoke$arity$4(oc.web.actions.activity.entry_publish_cb,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(fixed_entry_editing),new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(fixed_entry_editing),fixed_edit_key));
}
}
}
}));

(oc.web.actions.activity.entry_publish.cljs$lang$maxFixedArity = (2));

/** @this {Function} */
(oc.web.actions.activity.entry_publish.cljs$lang$applyTo = (function (seq42862){
var G__42863 = cljs.core.first(seq42862);
var seq42862__$1 = cljs.core.next(seq42862);
var G__42864 = cljs.core.first(seq42862__$1);
var seq42862__$2 = cljs.core.next(seq42862__$1);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__42863,G__42864,seq42862__$2);
}));

oc.web.actions.activity.activity_delete_finish = (function oc$web$actions$activity$activity_delete_finish(){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.lib.utils.default_drafts_board_slug)){
return oc.web.actions.section.drafts_get();
} else {
return null;
}
});
oc.web.actions.activity.real_activity_delete = (function oc$web$actions$activity$real_activity_delete(activity_data){
var activity_delete_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(activity_data),"delete"], 0));
oc.web.api.delete_entry(activity_delete_link,oc.web.actions.activity.activity_delete_finish);

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"activity-delete","activity-delete",-1780584608),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),activity_data], null));
});
oc.web.actions.activity.activity_delete = (function oc$web$actions$activity$activity_delete(activity_data){
oc.web.actions.nux.dismiss_post_added_tooltip();

oc.web.actions.cmail.remove_cached_item(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));

if(cljs.core.truth_(new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(activity_data))){
return oc.web.actions.activity.real_activity_delete(activity_data);
} else {
if(cljs.core.truth_(new cljs.core.Keyword(null,"auto-saving","auto-saving",68752642).cljs$core$IFn$_invoke$arity$1(activity_data))){
return oc.web.lib.utils.after((1000),(function (){
return oc.web.actions.activity.real_activity_delete(oc.web.dispatcher.cmail_data.cljs$core$IFn$_invoke$arity$0());
}));
} else {
return null;
}
}
});
oc.web.actions.activity.activity_move = (function oc$web$actions$activity$activity_move(activity_data,board_data){
var fixed_activity_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(activity_data,new cljs.core.Keyword(null,"board-slug","board-slug",99003663),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(board_data));
var patch_entry_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(activity_data),"partial-update"], 0));
oc.web.api.patch_entry(patch_entry_link,fixed_activity_data,null,oc.web.actions.activity.create_update_entry_cb);

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"activity-move","activity-move",-355927804),activity_data,oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),board_data], null));
});
oc.web.actions.activity.activity_share_show = (function oc$web$actions$activity$activity_share_show(var_args){
var args__4742__auto__ = [];
var len__4736__auto___43223 = arguments.length;
var i__4737__auto___43224 = (0);
while(true){
if((i__4737__auto___43224 < len__4736__auto___43223)){
args__4742__auto__.push((arguments[i__4737__auto___43224]));

var G__43225 = (i__4737__auto___43224 + (1));
i__4737__auto___43224 = G__43225;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.actions.activity.activity_share_show.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.actions.activity.activity_share_show.cljs$core$IFn$_invoke$arity$variadic = (function (activity_data,p__42948){
var vec__42952 = p__42948;
var element_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42952,(0),null);
var share_medium = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42952,(1),null);
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"activity-share-show","activity-share-show",-1149834881),activity_data,element_id,(function (){var or__4126__auto__ = share_medium;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"url","url",276297046);
}
})()], null));
}));

(oc.web.actions.activity.activity_share_show.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.actions.activity.activity_share_show.cljs$lang$applyTo = (function (seq42930){
var G__42931 = cljs.core.first(seq42930);
var seq42930__$1 = cljs.core.next(seq42930);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__42931,seq42930__$1);
}));

oc.web.actions.activity.activity_share_hide = (function oc$web$actions$activity$activity_share_hide(){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"activity-share-hide","activity-share-hide",-1709177315)], null));
});
oc.web.actions.activity.activity_share_reset = (function oc$web$actions$activity$activity_share_reset(){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"activity-share-reset","activity-share-reset",-1772920483)], null));
});
oc.web.actions.activity.activity_share_cb = (function oc$web$actions$activity$activity_share_cb(p__42960){
var map__42961 = p__42960;
var map__42961__$1 = (((((!((map__42961 == null))))?(((((map__42961.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42961.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42961):map__42961);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42961__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42961__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42961__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("activity-share","finish","activity-share/finish",1829129073),success,(cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):null)], null));
});
oc.web.actions.activity.activity_share = (function oc$web$actions$activity$activity_share(var_args){
var args__4742__auto__ = [];
var len__4736__auto___43226 = arguments.length;
var i__4737__auto___43227 = (0);
while(true){
if((i__4737__auto___43227 < len__4736__auto___43226)){
args__4742__auto__.push((arguments[i__4737__auto___43227]));

var G__43228 = (i__4737__auto___43227 + (1));
i__4737__auto___43227 = G__43228;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((2) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((2)),(0),null)):null);
return oc.web.actions.activity.activity_share.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__4743__auto__);
});

(oc.web.actions.activity.activity_share.cljs$core$IFn$_invoke$arity$variadic = (function (activity_data,share_data,p__42973){
var vec__42974 = p__42973;
var share_cb = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42974,(0),null);
var share_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(activity_data),"share"], 0));
var callback = ((cljs.core.fn_QMARK_(share_cb))?share_cb:oc.web.actions.activity.activity_share_cb);
oc.web.api.share_entry(share_link,share_data,callback);

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"activity-share","activity-share",-127339936),share_data], null));
}));

(oc.web.actions.activity.activity_share.cljs$lang$maxFixedArity = (2));

/** @this {Function} */
(oc.web.actions.activity.activity_share.cljs$lang$applyTo = (function (seq42967){
var G__42968 = cljs.core.first(seq42967);
var seq42967__$1 = cljs.core.next(seq42967);
var G__42969 = cljs.core.first(seq42967__$1);
var seq42967__$2 = cljs.core.next(seq42967__$1);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__42968,G__42969,seq42967__$2);
}));

oc.web.actions.activity.entry_revert = (function oc$web$actions$activity$entry_revert(revision_id,entry_editing){
if((revision_id == null)){
return null;
} else {
var entry_exists_QMARK_ = cljs.core.seq(new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(entry_editing));
var entry_version = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(entry_editing,new cljs.core.Keyword(null,"revision-id","revision-id",1301980641),revision_id);
var org_slug = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
var board_data = oc.web.dispatcher.board_data.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.deref(oc.web.dispatcher.app_state),org_slug,new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(entry_editing)], 0));
var revert_entry_link = ((entry_exists_QMARK_)?oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(entry_editing),"revert"], 0)):null);
if(entry_exists_QMARK_){
return oc.web.api.revert_entry(revert_entry_link,entry_version,(function (p__42995){
var map__42996 = p__42995;
var map__42996__$1 = (((((!((map__42996 == null))))?(((((map__42996.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42996.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42996):map__42996);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42996__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42996__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"entry-revert","entry-revert",-1402208640),entry_version], null));

if(cljs.core.truth_(success)){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("entry-revert","finish","entry-revert/finish",-1481043085),oc.web.lib.json.json__GT_cljs(body)], null));
} else {
return null;
}
}));
} else {
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"entry-revert","entry-revert",-1402208640),false], null));
}
}
});
oc.web.actions.activity.activity_get_finish = (function oc$web$actions$activity$activity_get_finish(status,activity_data,secure_uuid){
if(cljs.core.truth_(cljs.core.some(cljs.core.PersistentHashSet.createAsIfByAssoc([status]),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(401),(404)], null)))){
return oc.web.actions.routing.maybe_404.cljs$core$IFn$_invoke$arity$1(((cljs.core.not(oc.web.lib.jwt.jwt())) && (cljs.core.not(oc.web.lib.jwt.id_token())) && ((!((oc.web.dispatcher.current_secure_activity_id.cljs$core$IFn$_invoke$arity$0() == null))))));
} else {
if((((!((oc.web.dispatcher.current_activity_id.cljs$core$IFn$_invoke$arity$0() == null)))) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data),oc.web.dispatcher.current_activity_id.cljs$core$IFn$_invoke$arity$0())))){
return oc.web.actions.routing.maybe_404.cljs$core$IFn$_invoke$arity$0();
} else {
if(cljs.core.truth_((function (){var and__4115__auto__ = secure_uuid;
if(cljs.core.truth_(and__4115__auto__)){
var and__4115__auto____$1 = oc.web.lib.jwt.jwt();
if(cljs.core.truth_(and__4115__auto____$1)){
return new cljs.core.Keyword(null,"member?","member?",486668360).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0());
} else {
return and__4115__auto____$1;
}
} else {
return and__4115__auto__;
}
})())){
return oc.web.router.redirect_BANG_(oc.web.urls.entry.cljs$core$IFn$_invoke$arity$3(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(activity_data),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data)));
} else {
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("activity-get","finish","activity-get/finish",538149738),status,oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),activity_data,secure_uuid], null));

}
}
}
});
oc.web.actions.activity.org_data_from_secure_activity = (function oc$web$actions$activity$org_data_from_secure_activity(secure_activity_data){
var old_org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([clojure.set.rename_keys(cljs.core.select_keys(secure_activity_data,new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"org-uuid","org-uuid",1539092089),new cljs.core.Keyword(null,"org-name","org-name",53378517),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051),new cljs.core.Keyword(null,"org-logo-url","org-logo-url",58175997),new cljs.core.Keyword(null,"team-id","team-id",-14505725)], null)),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"org-uuid","org-uuid",1539092089),new cljs.core.Keyword(null,"uuid","uuid",-2145095719),new cljs.core.Keyword(null,"org-name","org-name",53378517),new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051),new cljs.core.Keyword(null,"slug","slug",2029314850),new cljs.core.Keyword(null,"org-logo-url","org-logo-url",58175997),new cljs.core.Keyword(null,"logo-url","logo-url",-1629105032)], null)),old_org_data], 0));
});
oc.web.actions.activity.secure_activity_get_finish = (function oc$web$actions$activity$secure_activity_get_finish(p__43009){
var map__43010 = p__43009;
var map__43010__$1 = (((((!((map__43010 == null))))?(((((map__43010.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43010.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43010):map__43010);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43010__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43010__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43010__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var secure_activity_data = (cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):cljs.core.PersistentArrayMap.EMPTY);
var org_data = oc.web.actions.activity.org_data_from_secure_activity(secure_activity_data);
oc.web.actions.activity.activity_get_finish(status,secure_activity_data,oc.web.dispatcher.current_secure_activity_id.cljs$core$IFn$_invoke$arity$0());

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"org-loaded","org-loaded",-1554103541),org_data], null));
});
oc.web.actions.activity.get_org = (function oc$web$actions$activity$get_org(org_data,cb){
var fixed_org_data = (function (){var or__4126__auto__ = org_data;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
}
})();
var org_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(fixed_org_data),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["item","self"], null),"GET"], 0));
return oc.web.api.get_org(org_link,(function (p__43021){
var map__43022 = p__43021;
var map__43022__$1 = (((((!((map__43022 == null))))?(((((map__43022.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43022.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43022):map__43022);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43022__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43022__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43022__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var org_data__$1 = oc.web.lib.json.json__GT_cljs(body);
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"org-loaded","org-loaded",-1554103541),org_data__$1], null));

return (cb.cljs$core$IFn$_invoke$arity$1 ? cb.cljs$core$IFn$_invoke$arity$1(success) : cb.call(null,success));
}));
});
oc.web.actions.activity.build_secure_activity_link = (function oc$web$actions$activity$build_secure_activity_link(org_slug,secure_activity_id){
return new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"href","href",-793805698),["/orgs/",cljs.core.str.cljs$core$IFn$_invoke$arity$1(org_slug),"/entries/",cljs.core.str.cljs$core$IFn$_invoke$arity$1(secure_activity_id)].join(''),new cljs.core.Keyword(null,"method","method",55703592),"GET",new cljs.core.Keyword(null,"rel","rel",1378823488),"",new cljs.core.Keyword(null,"accept","accept",1874130431),"application/vnd.open-company.entry.v1+json"], null);
});
oc.web.actions.activity.secure_activity_get = (function oc$web$actions$activity$secure_activity_get(var_args){
var G__43038 = arguments.length;
switch (G__43038) {
case 0:
return oc.web.actions.activity.secure_activity_get.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.actions.activity.secure_activity_get.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.actions.activity.secure_activity_get.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.activity.secure_activity_get.cljs$core$IFn$_invoke$arity$0 = (function (){
return oc.web.actions.activity.secure_activity_get.cljs$core$IFn$_invoke$arity$2(null,oc.web.dispatcher.current_secure_activity_id.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.actions.activity.secure_activity_get.cljs$core$IFn$_invoke$arity$1 = (function (cb){
return oc.web.actions.activity.secure_activity_get.cljs$core$IFn$_invoke$arity$2(cb,oc.web.dispatcher.current_secure_activity_id.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.actions.activity.secure_activity_get.cljs$core$IFn$_invoke$arity$2 = (function (cb,secure_uuid){
var link_with_replacements = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.api_entry_point.cljs$core$IFn$_invoke$arity$0()),"partial-secure",cljs.core.PersistentArrayMap.EMPTY,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"org-slug","org-slug",-726595051),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"secure-uuid","secure-uuid",-1972075067),secure_uuid], null)], 0));
var secure_link = (function (){var or__4126__auto__ = link_with_replacements;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.actions.activity.build_secure_activity_link(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),secure_uuid);
}
})();
return oc.web.api.get_secure_entry(secure_link,(function (resp){
oc.web.actions.activity.secure_activity_get_finish(resp);

if(cljs.core.fn_QMARK_(cb)){
return (cb.cljs$core$IFn$_invoke$arity$1 ? cb.cljs$core$IFn$_invoke$arity$1(resp) : cb.call(null,resp));
} else {
return null;
}
}));
}));

(oc.web.actions.activity.secure_activity_get.cljs$lang$maxFixedArity = 2);

oc.web.actions.activity.entry_change = (function oc$web$actions$activity$entry_change(org_slug,entry_uuid){
var temp__5735__auto__ = oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$2(org_slug,entry_uuid);
if(cljs.core.truth_(temp__5735__auto__)){
var entry_data = temp__5735__auto__;
return oc.web.actions.activity.get_entry(entry_data);
} else {
return null;
}
});
oc.web.actions.activity.check_entry_for_badges = (function oc$web$actions$activity$check_entry_for_badges(container_id,entry_uuid){
var org_slug = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
var current_board_slug = oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0();
var board_data = oc.web.utils.activity.board_by_uuid(container_id);
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(board_data),oc.web.lib.utils.default_drafts_board_slug)){
return null;
} else {
return oc.web.actions.cmail.get_entry_with_uuid.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(board_data),entry_uuid,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(function (success,status){
if(cljs.core.truth_(success)){
var entry_data = oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$1(entry_uuid);
var follow_boards_list = oc.web.dispatcher.follow_boards_list.cljs$core$IFn$_invoke$arity$1(org_slug);
var following_data = oc.web.dispatcher.container_data.cljs$core$IFn$_invoke$arity$4(cljs.core.deref(oc.web.dispatcher.app_state),org_slug,new cljs.core.Keyword(null,"following","following",-2049193617),oc.web.dispatcher.recently_posted_sort);
var replies_data = oc.web.dispatcher.container_data.cljs$core$IFn$_invoke$arity$4(cljs.core.deref(oc.web.dispatcher.app_state),org_slug,new cljs.core.Keyword(null,"replies","replies",-1389888974),oc.web.dispatcher.recent_activity_sort);
var is_following_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug),new cljs.core.Keyword(null,"following","following",-2049193617));
var is_replies_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug),new cljs.core.Keyword(null,"replies","replies",-1389888974));
var is_published_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(entry_data)),new cljs.core.Keyword(null,"published","published",-514587618));
var current_user_data = oc.web.dispatcher.current_user_data.cljs$core$IFn$_invoke$arity$0();
var is_publisher_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"publisher","publisher",-153364540).cljs$core$IFn$_invoke$arity$1(entry_data)),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(current_user_data));
var following_entry_QMARK_ = (function (){var or__4126__auto__ = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(entry_data),"unfollow"], 0));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var fexpr__43054 = cljs.core.set(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),follow_boards_list));
return (fexpr__43054.cljs$core$IFn$_invoke$arity$1 ? fexpr__43054.cljs$core$IFn$_invoke$arity$1(container_id) : fexpr__43054.call(null,container_id));
}
})();
var following_item = cljs.core.some((function (p1__43039_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__43039_SHARP_),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data))){
return p1__43039_SHARP_;
} else {
return null;
}
}),new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897).cljs$core$IFn$_invoke$arity$1(following_data));
var should_badge_following_QMARK_ = (((!(is_following_QMARK_)))?((is_published_QMARK_)?(function (){var and__4115__auto__ = following_entry_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
if((!(is_publisher_QMARK_))){
var or__4126__auto__ = new cljs.core.Keyword(null,"unseen","unseen",1063275592).cljs$core$IFn$_invoke$arity$1(following_item);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.utils.activity.entry_unseen_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([entry_data,new cljs.core.Keyword(null,"last-seen-at","last-seen-at",1929467667).cljs$core$IFn$_invoke$arity$1(following_data)], 0));
}
} else {
return false;
}
} else {
return and__4115__auto__;
}
})():false):false);
var should_badge_replies_QMARK_ = ((is_replies_QMARK_)?((is_published_QMARK_)?(function (){var and__4115__auto__ = following_entry_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
if((new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(entry_data),"comments"], 0))) > (0))){
return oc.web.utils.activity.comments_unseen_QMARK_(entry_data,new cljs.core.Keyword(null,"last-seen-at","last-seen-at",1929467667).cljs$core$IFn$_invoke$arity$1(replies_data));
} else {
return false;
}
} else {
return and__4115__auto__;
}
})():false):false);
if(cljs.core.truth_(should_badge_following_QMARK_)){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("following-badge","on","following-badge/on",-922760591),org_slug], null));
} else {
}

if(cljs.core.truth_(should_badge_replies_QMARK_)){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("replies-badge","on","replies-badge/on",1447213434),org_slug], null));
} else {
return null;
}
} else {
return null;
}
})], 0));
}
});
oc.web.actions.activity.ws_change_subscribe = (function oc$web$actions$activity$ws_change_subscribe(){
oc.web.ws.change_client.subscribe(new cljs.core.Keyword("container","change","container/change",-1507058407),(function (data){
var change_data = new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(data);
var container_id = new cljs.core.Keyword(null,"item-id","item-id",-1804511607).cljs$core$IFn$_invoke$arity$1(change_data);
var change_type = new cljs.core.Keyword(null,"change-type","change-type",1354898425).cljs$core$IFn$_invoke$arity$1(change_data);
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0(),"bookmarks")){
oc.web.actions.activity.bookmarks_get(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0());
} else {
}

if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0(),"unfollowing")){
return oc.web.actions.activity.unfollowing_get(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0());
} else {
return null;
}
}));

oc.web.ws.change_client.subscribe(new cljs.core.Keyword("entry","inbox-action","entry/inbox-action",1514943739),(function (data){
if(cljs.core.truth_((function (){var and__4115__auto__ = (function (){var G__43072 = new cljs.core.Keyword(null,"change-type","change-type",1354898425).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(data));
var fexpr__43071 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"follow","follow",-809317662),null,new cljs.core.Keyword(null,"unread","unread",-1950424572),null,new cljs.core.Keyword(null,"dismiss","dismiss",412569545),null,new cljs.core.Keyword(null,"comment-add","comment-add",1843969593),null,new cljs.core.Keyword(null,"unfollow","unfollow",1286126365),null], null), null);
return (fexpr__43071.cljs$core$IFn$_invoke$arity$1 ? fexpr__43071.cljs$core$IFn$_invoke$arity$1(G__43072) : fexpr__43071.call(null,G__43072));
})();
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(data)),oc.web.lib.jwt.user_id());
} else {
return and__4115__auto__;
}
})())){
var change_data_43238 = new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(data);
var org_slug_43239 = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
var container_id_43240 = new cljs.core.Keyword(null,"container-id","container-id",1274665684).cljs$core$IFn$_invoke$arity$1(change_data_43238);
var board_data_43241 = oc.web.utils.activity.board_by_uuid(container_id_43240);
var entry_uuid_43242 = new cljs.core.Keyword(null,"item-id","item-id",-1804511607).cljs$core$IFn$_invoke$arity$1(change_data_43238);
var change_type_43243 = new cljs.core.Keyword(null,"change-type","change-type",1354898425).cljs$core$IFn$_invoke$arity$1(change_data_43238);
var inbox_action_43244 = new cljs.core.Keyword(null,"inbox-action","inbox-action",1175006601).cljs$core$IFn$_invoke$arity$1(change_data_43238);
var current_board_slug_43245 = oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0();
var org_data_43246 = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(change_type_43243,new cljs.core.Keyword(null,"follow","follow",-809317662))){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.actions.activity",null,950,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Follow for",entry_uuid_43242], null);
}),null)),null,-1140927135);

oc.web.actions.activity.check_entry_for_badges(container_id_43240,entry_uuid_43242);

if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug_43245),new cljs.core.Keyword(null,"replies","replies",-1389888974))){
oc.web.actions.activity.replies_get.cljs$core$IFn$_invoke$arity$1(org_data_43246);
} else {
}

oc.web.actions.activity.following_did_change();
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(change_type_43243,new cljs.core.Keyword(null,"unfollow","unfollow",1286126365))){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.actions.activity",null,961,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Unfollow for",entry_uuid_43242], null);
}),null)),null,-605959727);

oc.web.actions.activity.check_entry_for_badges(container_id_43240,entry_uuid_43242);

if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug_43245),new cljs.core.Keyword(null,"replies","replies",-1389888974))){
oc.web.actions.activity.replies_get.cljs$core$IFn$_invoke$arity$1(org_data_43246);
} else {
}

oc.web.actions.activity.following_did_change();
} else {
}
}
} else {
}

if(cljs.core.truth_((function (){var and__4115__auto__ = oc.web.lib.utils.in_QMARK_(new cljs.core.Keyword(null,"users","users",-713552705).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(data)),oc.web.lib.jwt.user_id());
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"comment-add","comment-add",1843969593),new cljs.core.Keyword(null,"change-type","change-type",1354898425).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(data)));
} else {
return and__4115__auto__;
}
})())){
var change_data = new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(data);
var entry_uuid = new cljs.core.Keyword(null,"item-id","item-id",-1804511607).cljs$core$IFn$_invoke$arity$1(change_data);
var container_id = new cljs.core.Keyword(null,"container-id","container-id",1274665684).cljs$core$IFn$_invoke$arity$1(change_data);
var change_type = new cljs.core.Keyword(null,"change-type","change-type",1354898425).cljs$core$IFn$_invoke$arity$1(change_data);
var inbox_action = new cljs.core.Keyword(null,"inbox-action","inbox-action",1175006601).cljs$core$IFn$_invoke$arity$1(change_data);
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
var current_board_slug = oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0();
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.actions.activity",null,979,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Comment added for",entry_uuid], null);
}),null)),null,411164393);

oc.web.actions.activity.check_entry_for_badges(container_id,entry_uuid);

if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug),new cljs.core.Keyword(null,"replies","replies",-1389888974))){
return oc.web.actions.activity.replies_get.cljs$core$IFn$_invoke$arity$1(org_data);
} else {
return null;
}
} else {
return null;
}
}));

oc.web.ws.change_client.subscribe(new cljs.core.Keyword("item","change","item/change",-1168388949),(function (data){
var change_data = new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(data);
var container_id = new cljs.core.Keyword(null,"container-id","container-id",1274665684).cljs$core$IFn$_invoke$arity$1(change_data);
var entry_uuid = new cljs.core.Keyword(null,"item-id","item-id",-1804511607).cljs$core$IFn$_invoke$arity$1(change_data);
var container_id__$1 = new cljs.core.Keyword(null,"container-id","container-id",1274665684).cljs$core$IFn$_invoke$arity$1(change_data);
var change_type = new cljs.core.Keyword(null,"change-type","change-type",1354898425).cljs$core$IFn$_invoke$arity$1(change_data);
var item_status = cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"item-status","item-status",-1422018315).cljs$core$IFn$_invoke$arity$1(change_data));
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
var dispatch_unread = ((((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(change_type,new cljs.core.Keyword(null,"add","add",235287739))) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(change_data),oc.web.lib.jwt.user_id()))))?(function (p__43073){
var map__43074 = p__43073;
var map__43074__$1 = (((((!((map__43074 == null))))?(((((map__43074.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43074.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43074):map__43074);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43074__$1,new cljs.core.Keyword(null,"success","success",1890645906));
if(cljs.core.truth_(success)){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"mark-unread","mark-unread",-421378020),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"uuid","uuid",-2145095719),entry_uuid,new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127),container_id__$1], null)], null));
} else {
return null;
}
}):null);
var current_board_slug = oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0();
if(cljs.core.not(oc.web.utils.activity.is_published_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([item_status], 0)))){
return oc.web.actions.section.drafts_get();
} else {
if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(change_type,new cljs.core.Keyword(null,"delete","delete",-1768633620))){
oc.web.actions.activity.check_entry_for_badges(container_id__$1,entry_uuid);
} else {
}

if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(change_type,new cljs.core.Keyword(null,"add","add",235287739))) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(change_type,new cljs.core.Keyword(null,"delete","delete",-1768633620))))){
oc.web.actions.activity.bookmarks_get.cljs$core$IFn$_invoke$arity$variadic(org_data,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([dispatch_unread], 0));

oc.web.actions.activity.following_did_change();

if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(current_board_slug,"unfollowing")){
oc.web.actions.activity.unfollowing_get.cljs$core$IFn$_invoke$arity$variadic(org_data,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([dispatch_unread], 0));
} else {
if(((cljs.core.not(oc.web.dispatcher.is_container_QMARK_(current_board_slug))) && (cljs.core.not(oc.web.dispatcher.current_contributions_id.cljs$core$IFn$_invoke$arity$0())))){
oc.web.actions.section.section_change.cljs$core$IFn$_invoke$arity$variadic(container_id__$1,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([dispatch_unread], 0));
} else {
}
}
} else {
}

if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(change_type,new cljs.core.Keyword(null,"delete","delete",-1768633620))){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"activity-delete","activity-delete",-1780584608),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"uuid","uuid",-2145095719),entry_uuid], null)], null));
} else {
}

if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(change_type,new cljs.core.Keyword(null,"update","update",1045576396))){
return oc.web.actions.activity.entry_change(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),entry_uuid);
} else {
return null;
}
}
}));

oc.web.ws.change_client.subscribe(new cljs.core.Keyword("item","counts","item/counts",237352049),(function (data){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"activities-count","activities-count",2119299965),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(data)], null));
}));

return oc.web.ws.change_client.subscribe(new cljs.core.Keyword("item","status","item/status",-2086762296),(function (data){
var read_data = new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(data);
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"activity-reads","activity-reads",-203392960),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"item-id","item-id",-1804511607).cljs$core$IFn$_invoke$arity$1(read_data),cljs.core.count(new cljs.core.Keyword(null,"reads","reads",-1215067361).cljs$core$IFn$_invoke$arity$1(read_data)),new cljs.core.Keyword(null,"reads","reads",-1215067361).cljs$core$IFn$_invoke$arity$1(read_data)], null));
}));
});
/**
 * Actually send the seen. Needs to get the entry data from the app-state
 *   to read the published-at.
 */
oc.web.actions.activity.send_item_seen = (function oc$web$actions$activity$send_item_seen(entry_uuid){
var temp__33774__auto__ = (function (){var G__43077 = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
var G__43078 = entry_uuid;
return (oc.web.dispatcher.entry_data.cljs$core$IFn$_invoke$arity$2 ? oc.web.dispatcher.entry_data.cljs$core$IFn$_invoke$arity$2(G__43077,G__43078) : oc.web.dispatcher.entry_data.call(null,G__43077,G__43078));
})();
if(cljs.core.truth_(temp__33774__auto__)){
var entry_data = temp__33774__auto__;
var temp__33774__auto____$1 = new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"publisher","publisher",-153364540).cljs$core$IFn$_invoke$arity$1(entry_data));
if(cljs.core.truth_(temp__33774__auto____$1)){
var publisher_id = temp__33774__auto____$1;
var temp__33774__auto____$2 = new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127).cljs$core$IFn$_invoke$arity$1(entry_data);
if(cljs.core.truth_(temp__33774__auto____$2)){
var container_id = temp__33774__auto____$2;
var temp__33774__auto____$3 = new cljs.core.Keyword(null,"org-uuid","org-uuid",1539092089).cljs$core$IFn$_invoke$arity$1(entry_data);
if(cljs.core.truth_(temp__33774__auto____$3)){
var org_id = temp__33774__auto____$3;
return oc.web.ws.change_client.item_seen(publisher_id,org_id,container_id,entry_uuid);
} else {
return null;
}
} else {
return null;
}
} else {
return null;
}
} else {
return null;
}
});
/**
 * Send a seen message for the given container-id to Change service.
 */
oc.web.actions.activity.send_container_seen = (function oc$web$actions$activity$send_container_seen(container_id,seen_at){
var temp__33774__auto__ = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
if(cljs.core.truth_(temp__33774__auto__)){
var org_data = temp__33774__auto__;
var temp__33774__auto____$1 = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(org_data);
if(cljs.core.truth_(temp__33774__auto____$1)){
var org_id = temp__33774__auto____$1;
var temp__33774__auto____$2 = seen_at;
if(cljs.core.truth_(temp__33774__auto____$2)){
var _seen_at = temp__33774__auto____$2;
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.actions.activity",null,1069,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Send seen for container:",container_id,"at:",seen_at], null);
}),null)),null,-520306939);

oc.web.ws.change_client.container_seen(org_id,container_id,seen_at);

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"container-seen","container-seen",1269698583),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051).cljs$core$IFn$_invoke$arity$1(org_data),container_id,seen_at], null));
} else {
return null;
}
} else {
return null;
}
} else {
return null;
}
});
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.actions !== 'undefined') && (typeof oc.web.actions.activity !== 'undefined') && (typeof oc.web.actions.activity.last_sent_seen !== 'undefined')){
} else {
oc.web.actions.activity.last_sent_seen = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
}
oc.web.actions.activity.container_nav_in = (function oc$web$actions$activity$container_nav_in(container_slug,sort_type){
var container_data = oc.web.dispatcher.container_data.cljs$core$IFn$_invoke$arity$4(cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),container_slug,sort_type);
var next_sent_seen = ["last-sent-seen-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"container-slug","container-slug",365736492).cljs$core$IFn$_invoke$arity$1(container_data)),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"next-seen-at","next-seen-at",320928727).cljs$core$IFn$_invoke$arity$1(container_data))].join('');
if(cljs.core.truth_((function (){var and__4115__auto__ = new cljs.core.Keyword(null,"container-id","container-id",1274665684).cljs$core$IFn$_invoke$arity$1(container_data);
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.actions.activity.last_sent_seen),next_sent_seen);
} else {
return and__4115__auto__;
}
})())){
oc.web.actions.activity.send_container_seen(new cljs.core.Keyword(null,"container-id","container-id",1274665684).cljs$core$IFn$_invoke$arity$1(container_data),new cljs.core.Keyword(null,"next-seen-at","next-seen-at",320928727).cljs$core$IFn$_invoke$arity$1(container_data));

cljs.core.reset_BANG_(oc.web.actions.activity.last_sent_seen,next_sent_seen);

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"container-nav-in","container-nav-in",-1013166248),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),container_slug,sort_type], null));
} else {
return null;
}
});
oc.web.actions.activity.container_nav_out = (function oc$web$actions$activity$container_nav_out(container_slug,sort_type){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"container-nav-out","container-nav-out",-468973567),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),container_slug,sort_type], null));
});
oc.web.actions.activity.send_secure_item_seen_read = (function oc$web$actions$activity$send_secure_item_seen_read(){
var temp__33774__auto__ = oc.web.dispatcher.secure_activity_data.cljs$core$IFn$_invoke$arity$0();
if(cljs.core.truth_(temp__33774__auto__)){
var activity_data = temp__33774__auto__;
var temp__33774__auto____$1 = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data);
if(cljs.core.truth_(temp__33774__auto____$1)){
var activity_id = temp__33774__auto____$1;
var temp__33774__auto____$2 = new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"publisher","publisher",-153364540).cljs$core$IFn$_invoke$arity$1(activity_data));
if(cljs.core.truth_(temp__33774__auto____$2)){
var publisher_id = temp__33774__auto____$2;
var temp__33774__auto____$3 = new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127).cljs$core$IFn$_invoke$arity$1(activity_data);
if(cljs.core.truth_(temp__33774__auto____$3)){
var container_id = temp__33774__auto____$3;
var temp__33774__auto____$4 = oc.web.lib.jwt.get_id_token_contents.cljs$core$IFn$_invoke$arity$0();
if(cljs.core.truth_(temp__33774__auto____$4)){
var token_data = temp__33774__auto____$4;
var temp__33774__auto____$5 = new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(token_data);
if(cljs.core.truth_(temp__33774__auto____$5)){
var user_name = temp__33774__auto____$5;
var temp__33774__auto____$6 = new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103).cljs$core$IFn$_invoke$arity$1(token_data);
if(cljs.core.truth_(temp__33774__auto____$6)){
var avatar_url = temp__33774__auto____$6;
var temp__33774__auto____$7 = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0());
if(cljs.core.truth_(temp__33774__auto____$7)){
var org_id = temp__33774__auto____$7;
oc.web.ws.change_client.item_seen(publisher_id,org_id,container_id,activity_id);

return oc.web.ws.change_client.item_read(org_id,container_id,activity_id,user_name,avatar_url);
} else {
return null;
}
} else {
return null;
}
} else {
return null;
}
} else {
return null;
}
} else {
return null;
}
} else {
return null;
}
} else {
return null;
}
} else {
return null;
}
});
/**
 * Actually send the read. Needs to get the activity data from the app-state
 *   to read the published-id and the board uuid.
 */
oc.web.actions.activity.send_item_read = (function oc$web$actions$activity$send_item_read(activity_id){
var temp__33774__auto__ = oc.web.dispatcher.activity_key(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),activity_id);
if(cljs.core.truth_(temp__33774__auto__)){
var activity_key = temp__33774__auto__;
var temp__33774__auto____$1 = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),activity_key);
if(cljs.core.truth_(temp__33774__auto____$1)){
var activity_data = temp__33774__auto____$1;
var temp__33774__auto____$2 = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0());
if(cljs.core.truth_(temp__33774__auto____$2)){
var org_id = temp__33774__auto____$2;
var temp__33774__auto____$3 = new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127).cljs$core$IFn$_invoke$arity$1(activity_data);
if(cljs.core.truth_(temp__33774__auto____$3)){
var container_id = temp__33774__auto____$3;
var temp__33774__auto____$4 = oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"name","name",1843675177));
if(cljs.core.truth_(temp__33774__auto____$4)){
var user_name = temp__33774__auto____$4;
var temp__33774__auto____$5 = oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103));
if(cljs.core.truth_(temp__33774__auto____$5)){
var avatar_url = temp__33774__auto____$5;
return oc.web.ws.change_client.item_read(org_id,container_id,activity_id,user_name,avatar_url);
} else {
return null;
}
} else {
return null;
}
} else {
return null;
}
} else {
return null;
}
} else {
return null;
}
} else {
return null;
}
});
/**
 * @param {...*} var_args
 */
oc.web.actions.activity.mark_read = (function() { 
var oc$web$actions$activity$mark_read__delegate = function (args__33705__auto__){
var ocr_43079 = cljs.core.vec(args__33705__auto__);
try{if(((cljs.core.vector_QMARK_(ocr_43079)) && ((cljs.core.count(ocr_43079) === 1)))){
try{var ocr_43079_0__43081 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_43079,(0));
if(typeof ocr_43079_0__43081 === 'string'){
var activity_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_43079,(0));
var G__43085 = oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$1(activity_uuid);
return (oc.web.actions.activity.mark_read.cljs$core$IFn$_invoke$arity$1 ? oc.web.actions.activity.mark_read.cljs$core$IFn$_invoke$arity$1(G__43085) : oc.web.actions.activity.mark_read.call(null,G__43085));
} else {
throw cljs.core.match.backtrack;

}
}catch (e43083){if((e43083 instanceof Error)){
var e__32662__auto__ = e43083;
if((e__32662__auto__ === cljs.core.match.backtrack)){
var activity_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_43079,(0));
if(cljs.core.truth_((function (){var and__4115__auto__ = activity_data;
if(cljs.core.truth_(and__4115__auto__)){
if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(activity_data,new cljs.core.Keyword(null,"404","404",948666615))){
var and__4115__auto____$1 = oc.web.utils.activity.entry_QMARK_(activity_data);
if(cljs.core.truth_(and__4115__auto____$1)){
return cljs.core.not(new cljs.core.Keyword(null,"loading","loading",-737050189).cljs$core$IFn$_invoke$arity$1(activity_data));
} else {
return and__4115__auto____$1;
}
} else {
return false;
}
} else {
return and__4115__auto__;
}
})())){
oc.web.actions.activity.send_item_read(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"mark-read","mark-read",332267257),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),activity_data,oc.web.lib.utils.as_of_now()], null));

var G__43084_43259 = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data);
(oc.web.actions.activity.inbox_dismiss.cljs$core$IFn$_invoke$arity$1 ? oc.web.actions.activity.inbox_dismiss.cljs$core$IFn$_invoke$arity$1(G__43084_43259) : oc.web.actions.activity.inbox_dismiss.call(null,G__43084_43259));

return true;
} else {
return null;
}
} else {
throw e__32662__auto__;
}
} else {
throw e43083;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e43082){if((e43082 instanceof Error)){
var e__32662__auto__ = e43082;
if((e__32662__auto__ === cljs.core.match.backtrack)){
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ocr_43079)].join('')));
} else {
throw e__32662__auto__;
}
} else {
throw e43082;

}
}};
var oc$web$actions$activity$mark_read = function (var_args){
var args__33705__auto__ = null;
if (arguments.length > 0) {
var G__43260__i = 0, G__43260__a = new Array(arguments.length -  0);
while (G__43260__i < G__43260__a.length) {G__43260__a[G__43260__i] = arguments[G__43260__i + 0]; ++G__43260__i;}
  args__33705__auto__ = new cljs.core.IndexedSeq(G__43260__a,0,null);
} 
return oc$web$actions$activity$mark_read__delegate.call(this,args__33705__auto__);};
oc$web$actions$activity$mark_read.cljs$lang$maxFixedArity = 0;
oc$web$actions$activity$mark_read.cljs$lang$applyTo = (function (arglist__43261){
var args__33705__auto__ = cljs.core.seq(arglist__43261);
return oc$web$actions$activity$mark_read__delegate(args__33705__auto__);
});
oc$web$actions$activity$mark_read.cljs$core$IFn$_invoke$arity$variadic = oc$web$actions$activity$mark_read__delegate;
return oc$web$actions$activity$mark_read;
})()
;
oc.web.actions.activity.remove_video = (function oc$web$actions$activity$remove_video(edit_key,activity_data){
var has_changes = ((oc.web.utils.activity.has_attachments_QMARK_(activity_data)) || (oc.web.utils.activity.has_text_QMARK_(activity_data)));
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [edit_key], null),(function (p1__43086_SHARP_){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__43086_SHARP_,new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"fixed-video-id","fixed-video-id",-1380335259),null,new cljs.core.Keyword(null,"video-id","video-id",2132630536),null,new cljs.core.Keyword(null,"video-processed","video-processed",1222643416),false,new cljs.core.Keyword(null,"video-error","video-error",331887081),false,new cljs.core.Keyword(null,"has-changes","has-changes",-631476764),has_changes], null)], 0));
})], null));
});
oc.web.actions.activity.prompt_remove_video = (function oc$web$actions$activity$prompt_remove_video(edit_key,activity_data){
var alert_data = new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"icon","icon",1679606541),"/img/ML/trash.svg",new cljs.core.Keyword(null,"action","action",-811238024),"rerecord-video",new cljs.core.Keyword(null,"message","message",-406056002),"Are you sure you want to delete the current video? This can\u2019t be undone.",new cljs.core.Keyword(null,"link-button-title","link-button-title",-812305976),"Keep",new cljs.core.Keyword(null,"link-button-cb","link-button-cb",559710301),(function (){
return oc.web.components.ui.alert_modal.hide_alert();
}),new cljs.core.Keyword(null,"solid-button-style","solid-button-style",-723792244),new cljs.core.Keyword(null,"red","red",-969428204),new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"Yes",new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),(function (){
oc.web.actions.activity.remove_video(edit_key,activity_data);

return oc.web.components.ui.alert_modal.hide_alert();
})], null);
return oc.web.components.ui.alert_modal.show_alert(alert_data);
});
oc.web.actions.activity.delete_samples = (function oc$web$actions$activity$delete_samples(){
oc.web.actions.nux.dismiss_post_added_tooltip();

var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
var org_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["item","self"], null),"GET"], 0));
var delete_samples_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"delete-samples","DELETE"], 0));
if(cljs.core.truth_(delete_samples_link)){
return oc.web.api.delete_samples(delete_samples_link,(function (){
oc.web.api.get_org(org_link,oc.web.actions.activity.refresh_org_data_cb);

return oc.web.router.nav_BANG_(oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$0());
}));
} else {
return null;
}
});
oc.web.actions.activity.has_sample_posts_QMARK_ = (function oc$web$actions$activity$has_sample_posts_QMARK_(){
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
return oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"delete-samples","DELETE"], 0));
});
oc.web.actions.activity.activity_edit = (function oc$web$actions$activity$activity_edit(var_args){
var G__43088 = arguments.length;
switch (G__43088) {
case 0:
return oc.web.actions.activity.activity_edit.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return oc.web.actions.activity.activity_edit.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.activity.activity_edit.cljs$core$IFn$_invoke$arity$0 = (function (){
var cmail_state = oc.web.dispatcher.cmail_state.cljs$core$IFn$_invoke$arity$0();
var cmail_data = oc.web.dispatcher.cmail_data.cljs$core$IFn$_invoke$arity$0();
var next_cmail_data = (cljs.core.truth_((function (){var and__4115__auto__ = cmail_data;
if(cljs.core.truth_(and__4115__auto__)){
return cmail_state;
} else {
return and__4115__auto__;
}
})())?cmail_data:oc.web.actions.cmail.get_board_for_edit());
return oc.web.actions.activity.activity_edit.cljs$core$IFn$_invoke$arity$1(next_cmail_data);
}));

(oc.web.actions.activity.activity_edit.cljs$core$IFn$_invoke$arity$1 = (function (activity_data){
var is_published_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(activity_data),"published");
var cmail_state = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"key","key",-1516042587),oc.web.lib.utils.activity_uuid(),new cljs.core.Keyword(null,"fullscreen","fullscreen",-4371054),true,new cljs.core.Keyword(null,"collapsed","collapsed",-628494523),false], null);
return oc.web.actions.cmail.cmail_show.cljs$core$IFn$_invoke$arity$variadic(activity_data,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cmail_state], 0));
}));

(oc.web.actions.activity.activity_edit.cljs$lang$maxFixedArity = 1);

oc.web.actions.activity.add_bookmark = (function oc$web$actions$activity$add_bookmark(activity_data,add_bookmark_link){
if(cljs.core.truth_(add_bookmark_link)){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"bookmark-toggle","bookmark-toggle",187214294),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data),true], null));

return oc.web.api.toggle_bookmark(add_bookmark_link,(function (p__43089){
var map__43090 = p__43089;
var map__43090__$1 = (((((!((map__43090 == null))))?(((((map__43090.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43090.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43090):map__43090);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43090__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43090__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43090__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var new_activity_data = (cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):cljs.core.PersistentArrayMap.EMPTY);
return oc.web.actions.activity.activity_get_finish(status,new_activity_data,null);
}));
} else {
return null;
}
});
oc.web.actions.activity.remove_bookmark = (function oc$web$actions$activity$remove_bookmark(activity_data,remove_bookmark_link){
if(cljs.core.truth_(remove_bookmark_link)){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"bookmark-toggle","bookmark-toggle",187214294),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data),false], null));

return oc.web.api.toggle_bookmark(remove_bookmark_link,(function (p__43092){
var map__43093 = p__43092;
var map__43093__$1 = (((((!((map__43093 == null))))?(((((map__43093.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43093.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43093):map__43093);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43093__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43093__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43093__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var new_activity_data = (cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):cljs.core.PersistentArrayMap.EMPTY);
return oc.web.actions.activity.activity_get_finish(status,new_activity_data,null);
}));
} else {
return null;
}
});
oc.web.actions.activity.saved_sort_type = (function oc$web$actions$activity$saved_sort_type(org_slug,board_slug){
var sort_type_cookie = oc.web.lib.cookies.get_cookie(oc.web.router.last_sort_cookie(org_slug));
if(cljs.core.truth_((function (){var and__4115__auto__ = oc.web.dispatcher.is_container_with_sort_QMARK_(board_slug);
if(cljs.core.truth_(and__4115__auto__)){
return typeof sort_type_cookie === 'string';
} else {
return and__4115__auto__;
}
})())){
return cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(sort_type_cookie);
} else {
if(cljs.core.truth_(oc.web.dispatcher.is_recent_activity_QMARK_(board_slug))){
return oc.web.dispatcher.recent_activity_sort;
} else {
return oc.web.dispatcher.recently_posted_sort;
}
}
});
oc.web.actions.activity.change_sort_type = (function oc$web$actions$activity$change_sort_type(type){
oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$3(oc.web.router.last_sort_cookie(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0()),cljs.core.name(type),oc.web.lib.cookies.default_cookie_expire);

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("route","rewrite","route/rewrite",711205411),new cljs.core.Keyword(null,"sort-type","sort-type",-2053499504),type], null));
});
oc.web.actions.activity.refresh_board_data = (function oc$web$actions$activity$refresh_board_data(to_slug){
if(cljs.core.truth_(((cljs.core.not(oc.web.dispatcher.current_activity_id.cljs$core$IFn$_invoke$arity$0()))?to_slug:false))){
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
var active_users = oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$0();
var is_container_QMARK_ = oc.web.dispatcher.is_container_QMARK_(to_slug);
var is_board_QMARK_ = oc.web.dispatcher.org_board_data.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([org_data,to_slug], 0));
var is_contributions_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(active_users,to_slug);
var board_data = (cljs.core.truth_(is_container_QMARK_)?oc.web.dispatcher.container_data.cljs$core$IFn$_invoke$arity$3(cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),to_slug):(cljs.core.truth_(is_board_QMARK_)?oc.web.dispatcher.board_data.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([to_slug], 0)):null));
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(to_slug,"inbox")){
return oc.web.actions.activity.inbox_get(org_data);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(to_slug,"replies")){
return oc.web.actions.activity.replies_get.cljs$core$IFn$_invoke$arity$1(org_data);
} else {
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(to_slug,"all-posts")) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.recently_posted_sort)))){
return oc.web.actions.activity.all_posts_get(org_data);
} else {
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(to_slug,"all-posts")) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.recent_activity_sort)))){
return oc.web.actions.activity.recent_all_posts_get(org_data);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(to_slug,"bookmarks")){
return oc.web.actions.activity.bookmarks_get(org_data);
} else {
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(to_slug,"following")) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.recently_posted_sort)))){
return oc.web.actions.activity.following_get.cljs$core$IFn$_invoke$arity$1(org_data);
} else {
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(to_slug,"following")) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.recent_activity_sort)))){
return oc.web.actions.activity.recent_following_get.cljs$core$IFn$_invoke$arity$1(org_data);
} else {
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(to_slug,"unfollowing")) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.recently_posted_sort)))){
return oc.web.actions.activity.unfollowing_get(org_data);
} else {
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(to_slug,"unfollowing")) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.recent_activity_sort)))){
return oc.web.actions.activity.recent_unfollowing_get(org_data);
} else {
if(cljs.core.truth_(((cljs.core.not(board_data))?is_contributions_QMARK_:false))){
return oc.web.actions.contributions.contributions_get.cljs$core$IFn$_invoke$arity$1(to_slug);
} else {
var temp__33774__auto__ = (function (){var or__4126__auto__ = board_data;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.dispatcher.org_board_data.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([org_data,to_slug], 0));
}
})();
if(cljs.core.truth_(temp__33774__auto__)){
var fixed_board_data = temp__33774__auto__;
var temp__33774__auto____$1 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(fixed_board_data),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["item","self"], null),"GET"], 0));
if(cljs.core.truth_(temp__33774__auto____$1)){
var board_link = temp__33774__auto____$1;
return oc.web.actions.section.section_get(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(fixed_board_data),board_link);
} else {
return null;
}
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
} else {
return null;
}
});
oc.web.actions.activity.saved_foc_layout = (function oc$web$actions$activity$saved_foc_layout(org_slug){
var temp__5733__auto__ = oc.web.lib.cookies.get_cookie(oc.web.router.last_foc_layout_cookie(org_slug));
if(cljs.core.truth_(temp__5733__auto__)){
var foc_layout_cookie = temp__5733__auto__;
return cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(foc_layout_cookie);
} else {
return oc.web.dispatcher.default_foc_layout;
}
});
oc.web.actions.activity.toggle_foc_layout = (function oc$web$actions$activity$toggle_foc_layout(){
var current_value = new cljs.core.Keyword(null,"foc-layout","foc-layout",-1925028965).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
var next_value = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(current_value,oc.web.dispatcher.default_foc_layout))?oc.web.dispatcher.other_foc_layout:oc.web.dispatcher.default_foc_layout);
oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$3(oc.web.router.last_foc_layout_cookie(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0()),cljs.core.name(next_value),oc.web.lib.cookies.default_cookie_expire);

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"foc-layout","foc-layout",-1925028965)], null),next_value], null));
});
oc.web.actions.activity.entry_follow = (function oc$web$actions$activity$entry_follow(entry_uuid){
var activity_data = oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$1(entry_uuid);
var follow_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(activity_data),"follow"], 0));
return oc.web.api.inbox_follow(follow_link,(function (p__43095){
var map__43096 = p__43095;
var map__43096__$1 = (((((!((map__43096 == null))))?(((((map__43096.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43096.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43096):map__43096);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43096__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43096__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43096__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,(404))) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data),oc.web.dispatcher.current_activity_id.cljs$core$IFn$_invoke$arity$0())))){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("activity-get","not-found","activity-get/not-found",998070428),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data),null], null));

return oc.web.actions.routing.maybe_404.cljs$core$IFn$_invoke$arity$0();
} else {
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("activity-get","finish","activity-get/finish",538149738),status,oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.lib.json.json__GT_cljs(body),null], null));
}
}));
});
oc.web.actions.activity.entry_unfollow = (function oc$web$actions$activity$entry_unfollow(entry_uuid){
var activity_data = oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$1(entry_uuid);
var unfollow_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(activity_data),"unfollow"], 0));
return oc.web.api.inbox_unfollow(unfollow_link,(function (p__43098){
var map__43099 = p__43098;
var map__43099__$1 = (((((!((map__43099 == null))))?(((((map__43099.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43099.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43099):map__43099);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43099__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43099__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43099__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,(404))) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data),oc.web.dispatcher.current_activity_id.cljs$core$IFn$_invoke$arity$0())))){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("activity-get","not-found","activity-get/not-found",998070428),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data),null], null));

return oc.web.actions.routing.maybe_404.cljs$core$IFn$_invoke$arity$0();
} else {
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("activity-get","finish","activity-get/finish",538149738),status,oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.lib.json.json__GT_cljs(body),null], null));
}
}));
});
oc.web.actions.activity.inbox_dismiss = (function oc$web$actions$activity$inbox_dismiss(entry_uuid){
var dismiss_at = oc.web.lib.utils.as_of_now();
var activity_data = oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$1(entry_uuid);
var dismiss_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(activity_data),"dismiss"], 0));
if(cljs.core.truth_(dismiss_link)){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("inbox","dismiss","inbox/dismiss",445803855),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),entry_uuid,dismiss_at], null));

return oc.web.api.inbox_dismiss(dismiss_link,dismiss_at,(function (p__43101){
var map__43102 = p__43101;
var map__43102__$1 = (((((!((map__43102 == null))))?(((((map__43102.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43102.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43102):map__43102);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43102__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43102__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43102__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,(404))) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data),oc.web.dispatcher.current_activity_id.cljs$core$IFn$_invoke$arity$0())))){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("activity-get","not-found","activity-get/not-found",998070428),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data),null], null));

return oc.web.actions.routing.maybe_404.cljs$core$IFn$_invoke$arity$0();
} else {
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("activity-get","finish","activity-get/finish",538149738),status,oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.lib.json.json__GT_cljs(body),null], null));
}
}));
} else {
return null;
}
});
oc.web.actions.activity.mark_unread = (function oc$web$actions$activity$mark_unread(activity_data){
(oc.web.actions.activity.inbox_unread.cljs$core$IFn$_invoke$arity$1 ? oc.web.actions.activity.inbox_unread.cljs$core$IFn$_invoke$arity$1(activity_data) : oc.web.actions.activity.inbox_unread.call(null,activity_data));

var temp__5735__auto__ = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(activity_data),"mark-unread"], 0));
if(cljs.core.truth_(temp__5735__auto__)){
var mark_unread_link = temp__5735__auto__;
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"mark-unread","mark-unread",-421378020),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),activity_data], null));

return oc.web.api.mark_unread(mark_unread_link,new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127).cljs$core$IFn$_invoke$arity$1(activity_data),(function (p__43104){
var map__43105 = p__43104;
var map__43105__$1 = (((((!((map__43105 == null))))?(((((map__43105.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43105.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43105):map__43105);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43105__$1,new cljs.core.Keyword(null,"success","success",1890645906));
return oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"title","title",636505583),(cljs.core.truth_(success)?"Post marked as unread":"An error occurred"),new cljs.core.Keyword(null,"description","description",-1428560544),(cljs.core.truth_(success)?null:"Please try again"),new cljs.core.Keyword(null,"dismiss","dismiss",412569545),true,new cljs.core.Keyword(null,"expire","expire",-70657108),(3),new cljs.core.Keyword(null,"id","id",-1388402092),(cljs.core.truth_(success)?new cljs.core.Keyword(null,"mark-unread-success","mark-unread-success",219482473):new cljs.core.Keyword(null,"mark-unread-error","mark-unread-error",-1344442747))], null));
}));
} else {
return null;
}
});
oc.web.actions.activity.inbox_unread = (function oc$web$actions$activity$inbox_unread(activity_data){
var temp__5735__auto__ = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(activity_data),"unread"], 0));
if(cljs.core.truth_(temp__5735__auto__)){
var unread_link = temp__5735__auto__;
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("inbox","unread","inbox/unread",-2047670138),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data)], null));

return oc.web.api.inbox_unread(unread_link,(function (p__43107){
var map__43108 = p__43107;
var map__43108__$1 = (((((!((map__43108 == null))))?(((((map__43108.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43108.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43108):map__43108);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43108__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43108__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43108__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,(404))) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data),oc.web.dispatcher.current_activity_id.cljs$core$IFn$_invoke$arity$0())))){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("activity-get","not-found","activity-get/not-found",998070428),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data),null], null));

return oc.web.actions.routing.maybe_404.cljs$core$IFn$_invoke$arity$0();
} else {
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("activity-get","finish","activity-get/finish",538149738),status,oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.lib.json.json__GT_cljs(body),null], null));
}
}));
} else {
return null;
}
});
oc.web.actions.activity.inbox_real_dismiss_all = (function oc$web$actions$activity$inbox_real_dismiss_all(){
var dismiss_at = oc.web.lib.utils.as_of_now();
var inbox_data = oc.web.dispatcher.container_data.cljs$core$IFn$_invoke$arity$3(cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),"inbox");
var dismiss_all_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(inbox_data),"dismiss-all"], 0));
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("inbox","dismiss-all","inbox/dismiss-all",1284483349),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0()], null));

return oc.web.api.inbox_dismiss_all(dismiss_all_link,dismiss_at,(function (p__43110){
var map__43111 = p__43110;
var map__43111__$1 = (((((!((map__43111 == null))))?(((((map__43111.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43111.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43111):map__43111);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43111__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43111__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43111__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
return null;
}));
});
oc.web.actions.activity.inbox_dismiss_all = (function oc$web$actions$activity$inbox_dismiss_all(){
var alert_data = cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"link-button-style","link-button-style",1552381990),new cljs.core.Keyword(null,"link-button-title","link-button-title",-812305976),new cljs.core.Keyword(null,"solid-button-style","solid-button-style",-723792244),new cljs.core.Keyword(null,"title","title",636505583),new cljs.core.Keyword(null,"action","action",-811238024),new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),new cljs.core.Keyword(null,"link-button-cb","link-button-cb",559710301),new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),new cljs.core.Keyword(null,"message","message",-406056002)],[new cljs.core.Keyword(null,"green","green",-945526839),"No, keep them",new cljs.core.Keyword(null,"red","red",-969428204),"Dismiss all posts?","dismiss-all","Yes, dismiss them",(function (){
return oc.web.components.ui.alert_modal.hide_alert();
}),(function (){
oc.web.actions.activity.inbox_real_dismiss_all();

return oc.web.components.ui.alert_modal.hide_alert();
})," This action cannot be undone."]);
return oc.web.components.ui.alert_modal.show_alert(alert_data);
});

//# sourceMappingURL=oc.web.actions.activity.js.map
