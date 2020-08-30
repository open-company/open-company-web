goog.provide('oc.web.actions.section');
oc.web.actions.section.is_currently_shown_QMARK_ = (function oc$web$actions$section$is_currently_shown_QMARK_(section){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(section));
});
oc.web.actions.section.watch_single_section = (function oc$web$actions$section$watch_single_section(section){
return oc.web.ws.interaction_client.board_unwatch((function (rep){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.actions.section",null,23,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [rep,"Watching on socket ",new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(section)], null);
}),null)),null,162094084);

return oc.web.ws.interaction_client.boards_watch.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(section)], null));
}));
});
/**
 * Request the reads count data only for the items we don't have already.
 */
oc.web.actions.section.request_reads_count = (function oc$web$actions$section$request_reads_count(section){
var user_is_part_of_the_team = new cljs.core.Keyword(null,"member?","member?",486668360).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0());
if(cljs.core.truth_((function (){var and__4115__auto__ = user_is_part_of_the_team;
if(cljs.core.truth_(and__4115__auto__)){
return ((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(section),oc.web.lib.utils.default_drafts_board_slug)) && (cljs.core.seq(new cljs.core.Keyword(null,"entries","entries",-86943161).cljs$core$IFn$_invoke$arity$1(section))));
} else {
return and__4115__auto__;
}
})())){
var item_ids = cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),new cljs.core.Keyword(null,"entries","entries",-86943161).cljs$core$IFn$_invoke$arity$1(section));
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
oc.web.actions.section.section_get_finish = (function oc$web$actions$section$section_get_finish(org_slug,section_slug,sort_type,section){
var is_currently_shown = oc.web.actions.section.is_currently_shown_QMARK_(section_slug);
var user_is_part_of_the_team = new cljs.core.Keyword(null,"member?","member?",486668360).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0());
if(is_currently_shown){
if(cljs.core.truth_(user_is_part_of_the_team)){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0(),section_slug)){
oc.web.actions.section.watch_single_section(section);

oc.web.actions.section.request_reads_count(section);
} else {
}
} else {
}
} else {
}

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"section","section",-300141526),org_slug,section_slug,sort_type,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(section,new cljs.core.Keyword(null,"is-loaded","is-loaded",1456967468),is_currently_shown)], null));
});
oc.web.actions.section.load_other_sections = (function oc$web$actions$section$load_other_sections(sections){
var seq__42280 = cljs.core.seq(sections);
var chunk__42283 = null;
var count__42284 = (0);
var i__42285 = (0);
while(true){
if((i__42285 < count__42284)){
var section = chunk__42283.cljs$core$IIndexed$_nth$arity$2(null,i__42285);
if((!(oc.web.actions.section.is_currently_shown_QMARK_(section)))){
var board_link_42404 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(section),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["item","self"], null),"GET"], 0));
oc.web.api.get_board(board_link_42404,((function (seq__42280,chunk__42283,count__42284,i__42285,board_link_42404,section){
return (function (p__42304){
var map__42306 = p__42304;
var map__42306__$1 = (((((!((map__42306 == null))))?(((((map__42306.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42306.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42306):map__42306);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42306__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42306__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42306__$1,new cljs.core.Keyword(null,"success","success",1890645906));
if(cljs.core.truth_(success)){
return oc.web.actions.section.section_get_finish(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0()),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(section),oc.web.dispatcher.recently_posted_sort,oc.web.lib.json.json__GT_cljs(body));
} else {
return null;
}
});})(seq__42280,chunk__42283,count__42284,i__42285,board_link_42404,section))
);


var G__42409 = seq__42280;
var G__42410 = chunk__42283;
var G__42411 = count__42284;
var G__42412 = (i__42285 + (1));
seq__42280 = G__42409;
chunk__42283 = G__42410;
count__42284 = G__42411;
i__42285 = G__42412;
continue;
} else {
var G__42413 = seq__42280;
var G__42414 = chunk__42283;
var G__42415 = count__42284;
var G__42416 = (i__42285 + (1));
seq__42280 = G__42413;
chunk__42283 = G__42414;
count__42284 = G__42415;
i__42285 = G__42416;
continue;
}
} else {
var temp__5735__auto__ = cljs.core.seq(seq__42280);
if(temp__5735__auto__){
var seq__42280__$1 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__42280__$1)){
var c__4556__auto__ = cljs.core.chunk_first(seq__42280__$1);
var G__42417 = cljs.core.chunk_rest(seq__42280__$1);
var G__42418 = c__4556__auto__;
var G__42419 = cljs.core.count(c__4556__auto__);
var G__42420 = (0);
seq__42280 = G__42417;
chunk__42283 = G__42418;
count__42284 = G__42419;
i__42285 = G__42420;
continue;
} else {
var section = cljs.core.first(seq__42280__$1);
if((!(oc.web.actions.section.is_currently_shown_QMARK_(section)))){
var board_link_42421 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(section),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["item","self"], null),"GET"], 0));
oc.web.api.get_board(board_link_42421,((function (seq__42280,chunk__42283,count__42284,i__42285,board_link_42421,section,seq__42280__$1,temp__5735__auto__){
return (function (p__42315){
var map__42316 = p__42315;
var map__42316__$1 = (((((!((map__42316 == null))))?(((((map__42316.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42316.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42316):map__42316);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42316__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42316__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42316__$1,new cljs.core.Keyword(null,"success","success",1890645906));
if(cljs.core.truth_(success)){
return oc.web.actions.section.section_get_finish(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0()),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(section),oc.web.dispatcher.recently_posted_sort,oc.web.lib.json.json__GT_cljs(body));
} else {
return null;
}
});})(seq__42280,chunk__42283,count__42284,i__42285,board_link_42421,section,seq__42280__$1,temp__5735__auto__))
);


var G__42422 = cljs.core.next(seq__42280__$1);
var G__42423 = null;
var G__42424 = (0);
var G__42425 = (0);
seq__42280 = G__42422;
chunk__42283 = G__42423;
count__42284 = G__42424;
i__42285 = G__42425;
continue;
} else {
var G__42426 = cljs.core.next(seq__42280__$1);
var G__42427 = null;
var G__42428 = (0);
var G__42429 = (0);
seq__42280 = G__42426;
chunk__42283 = G__42427;
count__42284 = G__42428;
i__42285 = G__42429;
continue;
}
}
} else {
return null;
}
}
break;
}
});
oc.web.actions.section.section_get = (function oc$web$actions$section$section_get(var_args){
var G__42322 = arguments.length;
switch (G__42322) {
case 1:
return oc.web.actions.section.section_get.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
var args_arr__4757__auto__ = [];
var len__4736__auto___42431 = arguments.length;
var i__4737__auto___42432 = (0);
while(true){
if((i__4737__auto___42432 < len__4736__auto___42431)){
args_arr__4757__auto__.push((arguments[i__4737__auto___42432]));

var G__42433 = (i__4737__auto___42432 + (1));
i__4737__auto___42432 = G__42433;
continue;
} else {
}
break;
}

var argseq__4758__auto__ = (new cljs.core.IndexedSeq(args_arr__4757__auto__.slice((2)),(0),null));
return oc.web.actions.section.section_get.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__4758__auto__);

}
});

(oc.web.actions.section.section_get.cljs$core$IFn$_invoke$arity$1 = (function (board_slug){
var temp__33774__auto__ = oc.web.dispatcher.org_board_data.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([board_slug], 0));
if(cljs.core.truth_(temp__33774__auto__)){
var section_data = temp__33774__auto__;
var temp__33774__auto____$1 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(section_data),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["item","self"], null),"GET"], 0));
if(cljs.core.truth_(temp__33774__auto____$1)){
var section_link = temp__33774__auto____$1;
return oc.web.actions.section.section_get(board_slug,section_link);
} else {
return null;
}
} else {
return null;
}
}));

(oc.web.actions.section.section_get.cljs$core$IFn$_invoke$arity$variadic = (function (board_slug,link,p__42330){
var vec__42331 = p__42330;
var finish_cb = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42331,(0),null);
return oc.web.api.get_board(link,(function (p__42334){
var map__42335 = p__42334;
var map__42335__$1 = (((((!((map__42335 == null))))?(((((map__42335.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42335.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42335):map__42335);
var resp = map__42335__$1;
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42335__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42335__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42335__$1,new cljs.core.Keyword(null,"success","success",1890645906));
if(cljs.core.truth_(success)){
oc.web.actions.section.section_get_finish(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0()),board_slug,oc.web.dispatcher.recently_posted_sort,oc.web.lib.json.json__GT_cljs(body));
} else {
}

if(cljs.core.fn_QMARK_(finish_cb)){
return (finish_cb.cljs$core$IFn$_invoke$arity$1 ? finish_cb.cljs$core$IFn$_invoke$arity$1(resp) : finish_cb.call(null,resp));
} else {
return null;
}
}));
}));

/** @this {Function} */
(oc.web.actions.section.section_get.cljs$lang$applyTo = (function (seq42319){
var G__42320 = cljs.core.first(seq42319);
var seq42319__$1 = cljs.core.next(seq42319);
var G__42321 = cljs.core.first(seq42319__$1);
var seq42319__$2 = cljs.core.next(seq42319__$1);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__42320,G__42321,seq42319__$2);
}));

(oc.web.actions.section.section_get.cljs$lang$maxFixedArity = (2));

oc.web.actions.section.section_refresh = (function oc$web$actions$section$section_refresh(board_slug){
var temp__33762__auto__ = oc.web.dispatcher.board_data.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([board_slug], 0));
if(cljs.core.truth_(temp__33762__auto__)){
var section_data = temp__33762__auto__;
var temp__33762__auto____$1 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(section_data),"refresh"], 0));
if(cljs.core.truth_(temp__33762__auto____$1)){
var refresh_link = temp__33762__auto____$1;
return oc.web.actions.section.section_get(board_slug,refresh_link);
} else {
return oc.web.actions.section.section_get.cljs$core$IFn$_invoke$arity$1(board_slug);
}
} else {
return oc.web.actions.section.section_get.cljs$core$IFn$_invoke$arity$1(board_slug);
}
});
oc.web.actions.section.drafts_get = (function oc$web$actions$section$drafts_get(){
var drafts_board = oc.web.dispatcher.org_board_data.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([oc.web.lib.utils.default_drafts_board_slug], 0));
var drafts_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(drafts_board),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["item","self"], null),"GET"], 0));
if(cljs.core.truth_(drafts_link)){
return oc.web.actions.section.section_get(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(drafts_board),drafts_link);
} else {
return null;
}
});
oc.web.actions.section.section_change = (function oc$web$actions$section$section_change(var_args){
var args__4742__auto__ = [];
var len__4736__auto___42434 = arguments.length;
var i__4737__auto___42435 = (0);
while(true){
if((i__4737__auto___42435 < len__4736__auto___42434)){
args__4742__auto__.push((arguments[i__4737__auto___42435]));

var G__42436 = (i__4737__auto___42435 + (1));
i__4737__auto___42435 = G__42436;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.actions.section.section_change.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.actions.section.section_change.cljs$core$IFn$_invoke$arity$variadic = (function (section_uuid,p__42344){
var vec__42345 = p__42344;
var finish_cb = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42345,(0),null);
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.actions.section",null,91,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Section change:",section_uuid], null);
}),null)),null,1375309702);

oc.web.lib.utils.after((0),(function (){
var current_board_data = oc.web.dispatcher.board_data();
var current_board_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(current_board_data),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["item","self"], null),"GET"], 0));
var drafts_board = oc.web.dispatcher.org_board_data.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([oc.web.lib.utils.default_drafts_board_slug], 0));
var drafts_board_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(drafts_board),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["item","self"], null),"GET"], 0));
var refresh_slug = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(section_uuid,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.default_drafts_board)))?oc.web.lib.utils.default_drafts_board_slug:((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(section_uuid,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(current_board_data)))?new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(current_board_data):null));
var refresh_link = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(section_uuid,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.default_drafts_board)))?drafts_board_link:((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(section_uuid,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(current_board_data)))?current_board_link:null));
if(cljs.core.truth_(refresh_link)){
return oc.web.actions.section.section_get.cljs$core$IFn$_invoke$arity$variadic(refresh_slug,refresh_link,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([finish_cb], 0));
} else {
return null;
}
}));

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"section-change","section-change",1643975649),section_uuid], null));
}));

(oc.web.actions.section.section_change.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.actions.section.section_change.cljs$lang$applyTo = (function (seq42342){
var G__42343 = cljs.core.first(seq42342);
var seq42342__$1 = cljs.core.next(seq42342);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__42343,seq42342__$1);
}));

oc.web.actions.section.section_delete = (function oc$web$actions$section$section_delete(var_args){
var args__4742__auto__ = [];
var len__4736__auto___42437 = arguments.length;
var i__4737__auto___42438 = (0);
while(true){
if((i__4737__auto___42438 < len__4736__auto___42437)){
args__4742__auto__.push((arguments[i__4737__auto___42438]));

var G__42439 = (i__4737__auto___42438 + (1));
i__4737__auto___42438 = G__42439;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.actions.section.section_delete.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.actions.section.section_delete.cljs$core$IFn$_invoke$arity$variadic = (function (section_slug,callback){
var section_data = oc.web.dispatcher.board_data.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),section_slug], 0));
var delete_section_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(section_data),"delete"], 0));
return oc.web.api.delete_board(delete_section_link,section_slug,(function (status,success,body){
if(cljs.core.truth_(success)){
var org_slug = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
var last_used_section_slug = oc.web.utils.activity.last_used_section();
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(last_used_section_slug,section_slug)){
oc.web.utils.activity.save_last_used_section(null);
} else {
}

if(cljs.core.fn_QMARK_(callback)){
(callback.cljs$core$IFn$_invoke$arity$1 ? callback.cljs$core$IFn$_invoke$arity$1(section_slug) : callback.call(null,section_slug));
} else {
}

if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(section_slug,oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0())){
oc.web.router.nav_BANG_(oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1(org_slug));

var org_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0()),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["item","self"], null),"GET"], 0));
return oc.web.api.get_org(org_link,(function (p__42356){
var map__42358 = p__42356;
var map__42358__$1 = (((((!((map__42358 == null))))?(((((map__42358.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42358.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42358):map__42358);
var status__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42358__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var body__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42358__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42358__$1,new cljs.core.Keyword(null,"success","success",1890645906));
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"org-loaded","org-loaded",-1554103541),oc.web.lib.json.json__GT_cljs(body__$1)], null));
}));
} else {
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"section-delete","section-delete",-321340384),org_slug,section_slug], null));
}
} else {
return window.location.reload();
}
}));
}));

(oc.web.actions.section.section_delete.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.actions.section.section_delete.cljs$lang$applyTo = (function (seq42354){
var G__42355 = cljs.core.first(seq42354);
var seq42354__$1 = cljs.core.next(seq42354);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__42355,seq42354__$1);
}));

oc.web.actions.section.refresh_org_data = (function oc$web$actions$section$refresh_org_data(){
var org_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0()),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["item","self"], null),"GET"], 0));
return oc.web.api.get_org(org_link,(function (p__42362){
var map__42363 = p__42362;
var map__42363__$1 = (((((!((map__42363 == null))))?(((((map__42363.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42363.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42363):map__42363);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42363__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42363__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42363__$1,new cljs.core.Keyword(null,"success","success",1890645906));
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"org-loaded","org-loaded",-1554103541),oc.web.lib.json.json__GT_cljs(body)], null));
}));
});
oc.web.actions.section.section_save_error = (function oc$web$actions$section$section_save_error(status){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167)], null),(function (p1__42367_SHARP_){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__42367_SHARP_,new cljs.core.Keyword(null,"section-name-error","section-name-error",-581249210),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,(409)))?"Team name already exists or isn't allowed":null)),new cljs.core.Keyword(null,"section-error","section-error",738290699),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,(409)))?null:"An error occurred, please retry.")),new cljs.core.Keyword(null,"loading","loading",-737050189));
})], null));
});
oc.web.actions.section.section_save = (function oc$web$actions$section$section_save(var_args){
var G__42369 = arguments.length;
switch (G__42369) {
case 2:
return oc.web.actions.section.section_save.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.actions.section.section_save.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 4:
return oc.web.actions.section.section_save.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.actions.section.section_save.cljs$core$IFn$_invoke$arity$2 = (function (section_data,note){
return oc.web.actions.section.section_save.cljs$core$IFn$_invoke$arity$3(section_data,note,null);
}));

(oc.web.actions.section.section_save.cljs$core$IFn$_invoke$arity$3 = (function (section_data,note,success_cb){
return oc.web.actions.section.section_save.cljs$core$IFn$_invoke$arity$4(section_data,note,success_cb,oc.web.actions.section.section_save_error);
}));

(oc.web.actions.section.section_save.cljs$core$IFn$_invoke$arity$4 = (function (section_data,note,success_cb,error_cb){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.actions.section",null,152,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [section_data], null);
}),null)),null,-266049937);

if(cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(section_data))){
var create_board_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0()),"create"], 0));
return oc.web.api.create_board(create_board_link,section_data,note,(function (p__42373){
var map__42374 = p__42373;
var map__42374__$1 = (((((!((map__42374 == null))))?(((((map__42374.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42374.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42374):map__42374);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42374__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42374__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42374__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var section_data__$1 = (cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):null);
var editable_board_QMARK_ = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(section_data__$1),"create"], 0));
if(cljs.core.not(success)){
if(cljs.core.fn_QMARK_(error_cb)){
return (error_cb.cljs$core$IFn$_invoke$arity$1 ? error_cb.cljs$core$IFn$_invoke$arity$1(status) : error_cb.call(null,status));
} else {
return null;
}
} else {
oc.web.lib.utils.after((100),(function (){
if(cljs.core.truth_((function (){var and__4115__auto__ = editable_board_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return new cljs.core.Keyword(null,"collapsed","collapsed",-628494523).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.cmail_state.cljs$core$IFn$_invoke$arity$0());
} else {
return and__4115__auto__;
}
})())){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),oc.web.dispatcher.cmail_data_key,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"board-slug","board-slug",99003663),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(section_data__$1),new cljs.core.Keyword(null,"board-name","board-name",-677515056),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(section_data__$1),new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803),new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803).cljs$core$IFn$_invoke$arity$1(section_data__$1)], null)], null));

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.cmail_state_key,new cljs.core.Keyword(null,"key","key",-1516042587)),oc.web.lib.utils.activity_uuid()], null));
} else {
return null;
}
}));

oc.web.lib.utils.after((500),oc.web.actions.section.refresh_org_data);

oc.web.ws.change_client.container_watch.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(section_data__$1)], 0));

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("section-edit-save","finish","section-edit-save/finish",-757307318),section_data__$1], null));

if(cljs.core.fn_QMARK_(success_cb)){
return (success_cb.cljs$core$IFn$_invoke$arity$1 ? success_cb.cljs$core$IFn$_invoke$arity$1(section_data__$1) : success_cb.call(null,section_data__$1));
} else {
return null;
}
}
}));
} else {
var board_patch_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(section_data),"partial-update"], 0));
return oc.web.api.patch_board(board_patch_link,section_data,note,(function (success,body,status){
var section_data__$1 = (cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):null);
if(cljs.core.not(success)){
if(cljs.core.fn_QMARK_(error_cb)){
return (error_cb.cljs$core$IFn$_invoke$arity$1 ? error_cb.cljs$core$IFn$_invoke$arity$1(status) : error_cb.call(null,status));
} else {
return null;
}
} else {
oc.web.actions.section.refresh_org_data();

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("section-edit-save","finish","section-edit-save/finish",-757307318)], null));

if(cljs.core.fn_QMARK_(success_cb)){
return (success_cb.cljs$core$IFn$_invoke$arity$1 ? success_cb.cljs$core$IFn$_invoke$arity$1(section_data__$1) : success_cb.call(null,section_data__$1));
} else {
return null;
}
}
}));
}
}));

(oc.web.actions.section.section_save.cljs$lang$maxFixedArity = 4);

oc.web.actions.section.private_section_user_add = (function oc$web$actions$section$private_section_user_add(user,user_type){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"private-section-user-add","private-section-user-add",1657184076),user,user_type], null));
});
oc.web.actions.section.private_section_user_remove = (function oc$web$actions$section$private_section_user_remove(user){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"private-section-user-remove","private-section-user-remove",1608853385),user], null));
});
oc.web.actions.section.private_section_kick_out_self = (function oc$web$actions$section$private_section_kick_out_self(user){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user),oc.web.lib.jwt.user_id())){
var remove_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(user),"remove"], 0));
return oc.web.api.remove_user_from_private_board(remove_link,(function (status,success,body){
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
var all_boards = new cljs.core.Keyword(null,"boards","boards",1912049694).cljs$core$IFn$_invoke$arity$1(org_data);
var current_board_slug = oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0();
var except_this_boards = cljs.core.remove.cljs$core$IFn$_invoke$arity$2((function (p1__42383_SHARP_){
var G__42385 = new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(p1__42383_SHARP_);
var fexpr__42384 = cljs.core.PersistentHashSet.createAsIfByAssoc(["drafts",current_board_slug]);
return (fexpr__42384.cljs$core$IFn$_invoke$arity$1 ? fexpr__42384.cljs$core$IFn$_invoke$arity$1(G__42385) : fexpr__42384.call(null,G__42385));
}),all_boards);
var redirect_url = (function (){var temp__5733__auto__ = cljs.core.first(except_this_boards);
if(cljs.core.truth_(temp__5733__auto__)){
var next_board = temp__5733__auto__;
return oc.web.urls.board.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(next_board));
} else {
return oc.web.urls.org.cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0());
}
})();
oc.web.actions.section.refresh_org_data();

oc.web.lib.utils.after((0),(function (){
return oc.web.router.nav_BANG_(redirect_url);
}));

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("private-section-kick-out-self","finish","private-section-kick-out-self/finish",779691698),success], null));
}));
} else {
return null;
}
});
oc.web.actions.section.ws_change_subscribe = (function oc$web$actions$section$ws_change_subscribe(){
oc.web.ws.change_client.subscribe(new cljs.core.Keyword("container","status","container/status",1617877110),(function (data){
var status_by_uuid = cljs.core.group_by(new cljs.core.Keyword(null,"container-id","container-id",1274665684),new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(data));
var clean_change_data = cljs.core.zipmap(cljs.core.keys(status_by_uuid),cljs.core.map.cljs$core$IFn$_invoke$arity$2(cljs.core.first,cljs.core.vals(status_by_uuid)));
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("container","status","container/status",1617877110),clean_change_data], null));
}));

oc.web.ws.change_client.subscribe(new cljs.core.Keyword("container","change","container/change",-1507058407),(function (data){
var change_data = new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(data);
var section_uuid = new cljs.core.Keyword(null,"item-id","item-id",-1804511607).cljs$core$IFn$_invoke$arity$1(change_data);
var change_type = new cljs.core.Keyword(null,"change-type","change-type",1354898425).cljs$core$IFn$_invoke$arity$1(change_data);
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(change_type,new cljs.core.Keyword(null,"update","update",1045576396))){
return oc.web.actions.section.section_change(section_uuid);
} else {
return null;
}
}));

return oc.web.ws.change_client.subscribe(new cljs.core.Keyword("item","change","item/change",-1168388949),(function (data){
var change_data = new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(data);
var section_uuid = new cljs.core.Keyword(null,"container-id","container-id",1274665684).cljs$core$IFn$_invoke$arity$1(change_data);
var change_type = new cljs.core.Keyword(null,"change-type","change-type",1354898425).cljs$core$IFn$_invoke$arity$1(change_data);
var org_slug = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
var item_id = new cljs.core.Keyword(null,"item-id","item-id",-1804511607).cljs$core$IFn$_invoke$arity$1(change_data);
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(change_type,new cljs.core.Keyword(null,"add","add",235287739))) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(change_type,new cljs.core.Keyword(null,"delete","delete",-1768633620))) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(change_type,new cljs.core.Keyword(null,"move","move",-2110884309))))){
oc.web.actions.section.section_change(section_uuid);
} else {
}

if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(change_type,new cljs.core.Keyword(null,"add","add",235287739))) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(change_data),oc.web.lib.jwt.user_id())))){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("item-add","unseen","item-add/unseen",168133967),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),change_data], null));
} else {
}

if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(change_type,new cljs.core.Keyword(null,"delete","delete",-1768633620))){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("item-delete","unseen","item-delete/unseen",-1112816887),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),change_data], null));
} else {
}

if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(change_type,new cljs.core.Keyword(null,"move","move",-2110884309))){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"item-move","item-move",-790509039),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),change_data], null));
} else {
return null;
}
}));
});
oc.web.actions.section.min_section_name_length = (2);
oc.web.actions.section.section_save_create = (function oc$web$actions$section$section_save_create(section_editing,section_name,success_cb){
if((cljs.core.count(section_name) < oc.web.actions.section.min_section_name_length)){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("section-edit","error","section-edit/error",430343686),["Name must be at least ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.actions.section.min_section_name_length)," characters."].join('')], null));
} else {
var next_section_editing = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([section_editing,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"loading","loading",-737050189),true,new cljs.core.Keyword(null,"name","name",1843675177),section_name], null)], 0));
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167)], null),next_section_editing], null));

return (success_cb.cljs$core$IFn$_invoke$arity$1 ? success_cb.cljs$core$IFn$_invoke$arity$1(next_section_editing) : success_cb.call(null,next_section_editing));
}
});
oc.web.actions.section.pre_flight_check = (function oc$web$actions$section$pre_flight_check(section_slug,section_name){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167)], null),(function (p1__42394_SHARP_){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__42394_SHARP_,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"has-changes","has-changes",-631476764),true,new cljs.core.Keyword(null,"pre-flight-loading","pre-flight-loading",-1550718393),true], null)], 0));
})], null));

var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
var pre_flight_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"pre-flight-create"], 0));
return oc.web.api.pre_flight_section_check(pre_flight_link,section_slug,section_name,(function (p__42395){
var map__42396 = p__42395;
var map__42396__$1 = (((((!((map__42396 == null))))?(((((map__42396.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42396.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42396):map__42396);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42396__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42396__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42396__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
if(cljs.core.truth_(success)){
} else {
oc.web.actions.section.section_save_error((409));
}

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167),new cljs.core.Keyword(null,"pre-flight-loading","pre-flight-loading",-1550718393)], null),false], null));
}));
});
oc.web.actions.section.section_more_finish = (function oc$web$actions$section$section_more_finish(org_slug,board_slug,sort_type,direction,p__42399){
var map__42400 = p__42399;
var map__42400__$1 = (((((!((map__42400 == null))))?(((((map__42400.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42400.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42400):map__42400);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42400__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42400__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
if(cljs.core.truth_(success)){
oc.web.actions.section.request_reads_count(oc.web.lib.json.json__GT_cljs(body));
} else {
}

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("section-more","finish","section-more/finish",1628075413),org_slug,board_slug,sort_type,direction,(cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):null)], null));
});
oc.web.actions.section.section_more = (function oc$web$actions$section$section_more(more_link,direction){
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
var board_slug = oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0();
oc.web.api.load_more_items(more_link,direction,cljs.core.partial.cljs$core$IFn$_invoke$arity$variadic(oc.web.actions.section.section_more_finish,new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),board_slug,oc.web.dispatcher.recently_posted_sort,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([direction], 0)));

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"section-more","section-more",1599322644),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),board_slug,oc.web.dispatcher.recently_posted_sort], null));
});
oc.web.actions.section.setup_section_editing = (function oc$web$actions$section$setup_section_editing(section_slug){
var temp__5735__auto__ = oc.web.dispatcher.org_board_data.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([section_slug], 0));
if(cljs.core.truth_(temp__5735__auto__)){
var board_data = temp__5735__auto__;
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"setup-section-editing","setup-section-editing",-295480073),board_data], null));
} else {
return null;
}
});

//# sourceMappingURL=oc.web.actions.section.js.map
