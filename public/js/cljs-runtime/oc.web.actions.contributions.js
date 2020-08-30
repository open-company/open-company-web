goog.provide('oc.web.actions.contributions');
oc.web.actions.contributions.watch_boards = (function oc$web$actions$contributions$watch_boards(posts_data){
if(cljs.core.truth_(oc.web.lib.jwt.jwt())){
var board_slugs = cljs.core.distinct.cljs$core$IFn$_invoke$arity$1(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"board-slug","board-slug",99003663),posts_data));
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
var org_boards = new cljs.core.Keyword(null,"boards","boards",1912049694).cljs$core$IFn$_invoke$arity$1(org_data);
var org_board_map = cljs.core.zipmap(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850),org_boards),cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),org_boards));
return oc.web.ws.interaction_client.board_unwatch((function (rep){
var board_uuids = cljs.core.map.cljs$core$IFn$_invoke$arity$2(org_board_map,board_slugs);
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.actions.contributions",null,24,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Watching on socket ",board_slugs,board_uuids], null);
}),null)),null,399432202);

return oc.web.ws.interaction_client.boards_watch.cljs$core$IFn$_invoke$arity$1(board_uuids);
}));
} else {
return null;
}
});
oc.web.actions.contributions.is_currently_shown_QMARK_ = (function oc$web$actions$contributions$is_currently_shown_QMARK_(author_uuid){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_contributions_id.cljs$core$IFn$_invoke$arity$0(),author_uuid);
});
/**
 * Request the reads count data only for the items we don't have already.
 */
oc.web.actions.contributions.request_reads_count = (function oc$web$actions$contributions$request_reads_count(author_uuid,contrib_data){
var member_QMARK_ = new cljs.core.Keyword(null,"member?","member?",486668360).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0());
if(cljs.core.truth_((function (){var and__4115__auto__ = member_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.seq(new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(contrib_data));
} else {
return and__4115__auto__;
}
})())){
var item_ids = cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(contrib_data));
var cleaned_ids = oc.web.utils.activity.clean_who_reads_count_ids(item_ids,oc.web.dispatcher.activity_read_data());
if(cljs.core.seq(cleaned_ids)){
return oc.web.api.request_reads_count(cleaned_ids);
} else {
return null;
}
} else {
return null;
}
});
oc.web.actions.contributions.contributions_get_success = (function oc$web$actions$contributions$contributions_get_success(org_slug,author_uuid,sort_type,contrib_data){
var is_currently_shown = oc.web.actions.contributions.is_currently_shown_QMARK_(author_uuid);
var member_QMARK_ = new cljs.core.Keyword(null,"member?","member?",486668360).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0());
if(is_currently_shown){
if(cljs.core.truth_(member_QMARK_)){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_contributions_id.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"author-id","author-id",807115351).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(contrib_data)))){
oc.web.actions.contributions.watch_boards(new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(contrib_data)));
} else {
}

oc.web.actions.contributions.request_reads_count(author_uuid,new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(contrib_data));
} else {
}
} else {
}

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("contributions-get","finish","contributions-get/finish",1354948902),org_slug,author_uuid,sort_type,contrib_data], null));
});
oc.web.actions.contributions.contributions_get_finish = (function oc$web$actions$contributions$contributions_get_finish(org_slug,author_uuid,sort_type,p__42470){
var map__42471 = p__42470;
var map__42471__$1 = (((((!((map__42471 == null))))?(((((map__42471.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42471.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42471):map__42471);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42471__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42471__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42471__$1,new cljs.core.Keyword(null,"success","success",1890645906));
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,(404))){
return oc.web.router.redirect_404_BANG_();
} else {
return oc.web.actions.contributions.contributions_get_success(org_slug,author_uuid,sort_type,(cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):cljs.core.PersistentArrayMap.EMPTY));
}
});
oc.web.actions.contributions.contributions_real_get = (function oc$web$actions$contributions$contributions_real_get(org_data,author_uuid,contrib_link){
return oc.web.api.get_contributions(contrib_link,(function (p__42473){
var map__42474 = p__42473;
var map__42474__$1 = (((((!((map__42474 == null))))?(((((map__42474.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42474.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42474):map__42474);
var resp = map__42474__$1;
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42474__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42474__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42474__$1,new cljs.core.Keyword(null,"success","success",1890645906));
return oc.web.actions.contributions.contributions_get_finish(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),author_uuid,oc.web.dispatcher.recently_posted_sort,resp);
}));
});
oc.web.actions.contributions.contributions_link = (function oc$web$actions$contributions$contributions_link(org_data,author_uuid){
return oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"partial-contributions",cljs.core.PersistentArrayMap.EMPTY,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"author-uuid","author-uuid",371566491),author_uuid], null)], 0));
});
oc.web.actions.contributions.contributions_get = (function oc$web$actions$contributions$contributions_get(var_args){
var G__42477 = arguments.length;
switch (G__42477) {
case 1:
return oc.web.actions.contributions.contributions_get.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.actions.contributions.contributions_get.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.contributions.contributions_get.cljs$core$IFn$_invoke$arity$1 = (function (author_uuid){
return oc.web.actions.contributions.contributions_get.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0(),author_uuid);
}));

(oc.web.actions.contributions.contributions_get.cljs$core$IFn$_invoke$arity$2 = (function (org_data,author_uuid){
var temp__5735__auto__ = oc.web.actions.contributions.contributions_link(org_data,author_uuid);
if(cljs.core.truth_(temp__5735__auto__)){
var contrib_link = temp__5735__auto__;
return oc.web.actions.contributions.contributions_real_get(org_data,author_uuid,contrib_link);
} else {
return null;
}
}));

(oc.web.actions.contributions.contributions_get.cljs$lang$maxFixedArity = 2);

/**
 * If the user is looking at a contributions view we need to reload all the items that are visible right now.
 *   Instead, if the user is looking at another view we can just reload the first page.
 */
oc.web.actions.contributions.contributions_refresh = (function oc$web$actions$contributions$contributions_refresh(var_args){
var G__42479 = arguments.length;
switch (G__42479) {
case 1:
return oc.web.actions.contributions.contributions_refresh.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.actions.contributions.contributions_refresh.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.contributions.contributions_refresh.cljs$core$IFn$_invoke$arity$1 = (function (author_uuid){
return oc.web.actions.contributions.contributions_refresh.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0(),author_uuid);
}));

(oc.web.actions.contributions.contributions_refresh.cljs$core$IFn$_invoke$arity$2 = (function (org_data,author_uuid){
var temp__33762__auto__ = oc.web.dispatcher.contributions_data.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([author_uuid], 0));
if(cljs.core.truth_(temp__33762__auto__)){
var contrib_data = temp__33762__auto__;
var temp__33762__auto____$1 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(contrib_data),"refresh"], 0));
if(cljs.core.truth_(temp__33762__auto____$1)){
var refresh_link = temp__33762__auto____$1;
return oc.web.actions.contributions.contributions_real_get(org_data,author_uuid,refresh_link);
} else {
return oc.web.actions.contributions.contributions_get.cljs$core$IFn$_invoke$arity$2(org_data,author_uuid);
}
} else {
return oc.web.actions.contributions.contributions_get.cljs$core$IFn$_invoke$arity$2(org_data,author_uuid);
}
}));

(oc.web.actions.contributions.contributions_refresh.cljs$lang$maxFixedArity = 2);

oc.web.actions.contributions.contributions_more_finish = (function oc$web$actions$contributions$contributions_more_finish(org_slug,author_uuid,sort_type,direction,p__42480){
var map__42481 = p__42480;
var map__42481__$1 = (((((!((map__42481 == null))))?(((((map__42481.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42481.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42481):map__42481);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42481__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42481__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var contrib_data = (cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):null);
if(cljs.core.truth_(success)){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_contributions_id.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"author-id","author-id",807115351).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(contrib_data)))){
oc.web.actions.contributions.watch_boards(new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(contrib_data)));
} else {
}

oc.web.actions.contributions.request_reads_count(author_uuid,new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(contrib_data));
} else {
}

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("contributions-more","finish","contributions-more/finish",1981208147),org_slug,author_uuid,sort_type,direction,(cljs.core.truth_(success)?new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(contrib_data):null)], null));
});
oc.web.actions.contributions.contributions_more = (function oc$web$actions$contributions$contributions_more(more_link,direction){
var org_slug = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
var author_uuid = oc.web.dispatcher.current_contributions_id.cljs$core$IFn$_invoke$arity$0();
oc.web.api.load_more_items(more_link,direction,cljs.core.partial.cljs$core$IFn$_invoke$arity$variadic(oc.web.actions.contributions.contributions_more_finish,org_slug,author_uuid,oc.web.dispatcher.recently_posted_sort,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([direction], 0)));

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"contributions-more","contributions-more",1062383218),org_slug,author_uuid,oc.web.dispatcher.recently_posted_sort], null));
});
oc.web.actions.contributions.subscribe = (function oc$web$actions$contributions$subscribe(){
return oc.web.ws.change_client.subscribe(new cljs.core.Keyword("item","change","item/change",-1168388949),(function (data){
if(cljs.core.truth_(oc.web.dispatcher.current_contributions_id.cljs$core$IFn$_invoke$arity$0())){
var change_data = new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(data);
var activity_uuid = new cljs.core.Keyword(null,"item-id","item-id",-1804511607).cljs$core$IFn$_invoke$arity$1(change_data);
var change_type = new cljs.core.Keyword(null,"change-type","change-type",1354898425).cljs$core$IFn$_invoke$arity$1(change_data);
var activity_data = oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),activity_uuid);
if(cljs.core.truth_((function (){var or__4126__auto__ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(change_type,new cljs.core.Keyword(null,"add","add",235287739));
if(or__4126__auto__){
return or__4126__auto__;
} else {
var and__4115__auto__ = (function (){var fexpr__42488 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"update","update",1045576396),null,new cljs.core.Keyword(null,"delete","delete",-1768633620),null], null), null);
return (fexpr__42488.cljs$core$IFn$_invoke$arity$1 ? fexpr__42488.cljs$core$IFn$_invoke$arity$1(change_type) : fexpr__42488.call(null,change_type));
})();
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_contributions_id.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"publisher","publisher",-153364540).cljs$core$IFn$_invoke$arity$1(activity_data)));
} else {
return and__4115__auto__;
}
}
})())){
return oc.web.actions.contributions.contributions_get.cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.current_contributions_id.cljs$core$IFn$_invoke$arity$0());
} else {
return null;
}
} else {
return null;
}
}));
});

//# sourceMappingURL=oc.web.actions.contributions.js.map
