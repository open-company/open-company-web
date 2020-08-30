goog.provide('oc.web.actions.cmail');
oc.web.actions.cmail.get_entry_cache_key = (function oc$web$actions$cmail$get_entry_cache_key(entry_uuid){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1((function (){var or__4126__auto__ = entry_uuid;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
}
})()),"-entry-edit"].join('');
});
oc.web.actions.cmail.remove_cached_item = (function oc$web$actions$cmail$remove_cached_item(item_uuid){
return oc.web.lib.user_cache.remove_item(oc.web.actions.cmail.get_entry_cache_key(item_uuid));
});
oc.web.actions.cmail.load_cached_item = (function oc$web$actions$cmail$load_cached_item(var_args){
var args__4742__auto__ = [];
var len__4736__auto___42376 = arguments.length;
var i__4737__auto___42377 = (0);
while(true){
if((i__4737__auto___42377 < len__4736__auto___42376)){
args__4742__auto__.push((arguments[i__4737__auto___42377]));

var G__42378 = (i__4737__auto___42377 + (1));
i__4737__auto___42377 = G__42378;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((2) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((2)),(0),null)):null);
return oc.web.actions.cmail.load_cached_item.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__4743__auto__);
});

(oc.web.actions.cmail.load_cached_item.cljs$core$IFn$_invoke$arity$variadic = (function (entry_data,edit_key,p__42276){
var vec__42277 = p__42276;
var completed_cb = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42277,(0),null);
var cache_key = oc.web.actions.cmail.get_entry_cache_key(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data));
return oc.web.lib.user_cache.get_item(cache_key,(function (item,err){
if(((cljs.core.not(err)) && (cljs.core.map_QMARK_(item)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"updated-at","updated-at",-1592622336).cljs$core$IFn$_invoke$arity$1(entry_data),new cljs.core.Keyword(null,"updated-at","updated-at",-1592622336).cljs$core$IFn$_invoke$arity$1(item))))){
var entry_to_save_42379 = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([item,cljs.core.select_keys(entry_data,new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"links","links",-654507394),new cljs.core.Keyword(null,"board-slug","board-slug",99003663),new cljs.core.Keyword(null,"board-name","board-name",-677515056),new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803)], null))], 0));
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [edit_key], null),entry_to_save_42379], null));
} else {
if(cljs.core.truth_(item)){
oc.web.actions.cmail.remove_cached_item(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data));
} else {
}

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [edit_key], null),entry_data], null));
}

if(cljs.core.fn_QMARK_(completed_cb)){
return (completed_cb.cljs$core$IFn$_invoke$arity$0 ? completed_cb.cljs$core$IFn$_invoke$arity$0() : completed_cb.call(null));
} else {
return null;
}
}));
}));

(oc.web.actions.cmail.load_cached_item.cljs$lang$maxFixedArity = (2));

/** @this {Function} */
(oc.web.actions.cmail.load_cached_item.cljs$lang$applyTo = (function (seq42272){
var G__42273 = cljs.core.first(seq42272);
var seq42272__$1 = cljs.core.next(seq42272);
var G__42274 = cljs.core.first(seq42272__$1);
var seq42272__$2 = cljs.core.next(seq42272__$1);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__42273,G__42274,seq42272__$2);
}));

oc.web.actions.cmail.get_default_section = (function oc$web$actions$cmail$get_default_section(var_args){
var args__4742__auto__ = [];
var len__4736__auto___42380 = arguments.length;
var i__4737__auto___42381 = (0);
while(true){
if((i__4737__auto___42381 < len__4736__auto___42380)){
args__4742__auto__.push((arguments[i__4737__auto___42381]));

var G__42382 = (i__4737__auto___42381 + (1));
i__4737__auto___42381 = G__42382;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.actions.cmail.get_default_section.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.actions.cmail.get_default_section.cljs$core$IFn$_invoke$arity$variadic = (function (p__42296){
var vec__42297 = p__42296;
var editable_boards = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42297,(0),null);
var editable_boards__$1 = (function (){var or__4126__auto__ = editable_boards;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.vals(oc.web.dispatcher.editable_boards_data.cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0()));
}
})();
var cookie_value = oc.web.utils.activity.last_used_section();
var board_from_cookie = (cljs.core.truth_(cookie_value)?cljs.core.some((function (p1__42291_SHARP_){
if(((cljs.core.not(new cljs.core.Keyword(null,"draft","draft",1421831058).cljs$core$IFn$_invoke$arity$1(p1__42291_SHARP_))) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(p1__42291_SHARP_),cookie_value)))){
return p1__42291_SHARP_;
} else {
return null;
}
}),editable_boards__$1):null);
var filtered_boards = cljs.core.filterv((function (p1__42292_SHARP_){
if(cljs.core.not(new cljs.core.Keyword(null,"draft","draft",1421831058).cljs$core$IFn$_invoke$arity$1(p1__42292_SHARP_))){
var or__4126__auto__ = (function (){var and__4115__auto__ = new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803).cljs$core$IFn$_invoke$arity$1(p1__42292_SHARP_);
if(cljs.core.truth_(and__4115__auto__)){
return oc.web.local_settings.publisher_board_enabled_QMARK_;
} else {
return and__4115__auto__;
}
})();
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.not(new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803).cljs$core$IFn$_invoke$arity$1(p1__42292_SHARP_));
}
} else {
return false;
}
}),editable_boards__$1);
var board_data = (function (){var or__4126__auto__ = board_from_cookie;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.first(cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"name","name",1843675177),filtered_boards));
}
})();
if(cljs.core.truth_(board_data)){
return new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"board-name","board-name",-677515056),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(board_data),new cljs.core.Keyword(null,"board-slug","board-slug",99003663),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(board_data),new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803),new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803).cljs$core$IFn$_invoke$arity$1(board_data)], null);
} else {
return null;
}
}));

(oc.web.actions.cmail.get_default_section.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.actions.cmail.get_default_section.cljs$lang$applyTo = (function (seq42295){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq42295));
}));

oc.web.actions.cmail.get_board_for_edit = (function oc$web$actions$cmail$get_board_for_edit(var_args){
var args__4742__auto__ = [];
var len__4736__auto___42386 = arguments.length;
var i__4737__auto___42387 = (0);
while(true){
if((i__4737__auto___42387 < len__4736__auto___42386)){
args__4742__auto__.push((arguments[i__4737__auto___42387]));

var G__42388 = (i__4737__auto___42387 + (1));
i__4737__auto___42387 = G__42388;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.actions.cmail.get_board_for_edit.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.actions.cmail.get_board_for_edit.cljs$core$IFn$_invoke$arity$variadic = (function (p__42311){
var vec__42312 = p__42311;
var board_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42312,(0),null);
var editable_boards = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42312,(1),null);
var sorted_editable_boards = cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"name","name",1843675177),editable_boards);
var board_data = (function (){var or__4126__auto__ = cljs.core.some((function (p1__42308_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(p1__42308_SHARP_),board_slug)){
return p1__42308_SHARP_;
} else {
return null;
}
}),sorted_editable_boards);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = cljs.core.some((function (p1__42309_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(p1__42309_SHARP_),oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0())){
return p1__42309_SHARP_;
} else {
return null;
}
}),sorted_editable_boards);
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
return cljs.core.first(sorted_editable_boards);
}
}
})();
if(cljs.core.truth_((function (){var or__4126__auto__ = cljs.core.not(board_data);
if(or__4126__auto__){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(board_data),oc.web.lib.utils.default_drafts_board_slug);
if(or__4126__auto____$1){
return or__4126__auto____$1;
} else {
var or__4126__auto____$2 = new cljs.core.Keyword(null,"draft","draft",1421831058).cljs$core$IFn$_invoke$arity$1(board_data);
if(cljs.core.truth_(or__4126__auto____$2)){
return or__4126__auto____$2;
} else {
return cljs.core.not(oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(board_data),"create"], 0)));
}
}
}
})())){
return oc.web.actions.cmail.get_default_section.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([sorted_editable_boards], 0));
} else {
return new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"board-name","board-name",-677515056),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(board_data),new cljs.core.Keyword(null,"board-slug","board-slug",99003663),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(board_data),new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803),new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803).cljs$core$IFn$_invoke$arity$1(board_data)], null);
}
}));

(oc.web.actions.cmail.get_board_for_edit.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.actions.cmail.get_board_for_edit.cljs$lang$applyTo = (function (seq42310){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq42310));
}));

oc.web.actions.cmail.get_entry_with_uuid = (function oc$web$actions$cmail$get_entry_with_uuid(var_args){
var args__4742__auto__ = [];
var len__4736__auto___42389 = arguments.length;
var i__4737__auto___42390 = (0);
while(true){
if((i__4737__auto___42390 < len__4736__auto___42389)){
args__4742__auto__.push((arguments[i__4737__auto___42390]));

var G__42391 = (i__4737__auto___42390 + (1));
i__4737__auto___42390 = G__42391;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((2) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((2)),(0),null)):null);
return oc.web.actions.cmail.get_entry_with_uuid.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__4743__auto__);
});

(oc.web.actions.cmail.get_entry_with_uuid.cljs$core$IFn$_invoke$arity$variadic = (function (board_slug,entry_uuid,p__42326){
var vec__42327 = p__42326;
var loaded_cb = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42327,(0),null);
var entry_data = oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$1(entry_uuid);
if(cljs.core.truth_(((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(entry_data,new cljs.core.Keyword(null,"404","404",948666615)))?(function (){var or__4126__auto__ = board_slug;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(entry_data);
}
})():false))){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"activity-get","activity-get",1006703884),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"org-slug","org-slug",-726595051),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"board-slug","board-slug",99003663),(function (){var or__4126__auto__ = board_slug;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(entry_data);
}
})(),new cljs.core.Keyword(null,"activity-uuid","activity-uuid",-663317778),entry_uuid], null)], null));

return oc.web.api.get_entry_with_uuid(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),board_slug,entry_uuid,(function (p__42337){
var map__42338 = p__42337;
var map__42338__$1 = (((((!((map__42338 == null))))?(((((map__42338.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42338.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42338):map__42338);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42338__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42338__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42338__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,(404))){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("activity-get","not-found","activity-get/not-found",998070428),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),entry_uuid,null], null));
} else {
if(cljs.core.not(success)){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("activity-get","failed","activity-get/failed",-1917791994),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),entry_uuid,null], null));
} else {
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("activity-get","finish","activity-get/finish",538149738),status,oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),(cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):null),null], null));

}
}

if(cljs.core.fn_QMARK_(loaded_cb)){
return oc.web.lib.utils.after((100),(function (){
return (loaded_cb.cljs$core$IFn$_invoke$arity$2 ? loaded_cb.cljs$core$IFn$_invoke$arity$2(success,status) : loaded_cb.call(null,success,status));
}));
} else {
return null;
}
}));
} else {
return null;
}
}));

(oc.web.actions.cmail.get_entry_with_uuid.cljs$lang$maxFixedArity = (2));

/** @this {Function} */
(oc.web.actions.cmail.get_entry_with_uuid.cljs$lang$applyTo = (function (seq42323){
var G__42324 = cljs.core.first(seq42323);
var seq42323__$1 = cljs.core.next(seq42323);
var G__42325 = cljs.core.first(seq42323__$1);
var seq42323__$2 = cljs.core.next(seq42323__$1);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__42324,G__42325,seq42323__$2);
}));

oc.web.actions.cmail.edit_open_cookie = (function oc$web$actions$cmail$edit_open_cookie(){
return ["edit-open-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.jwt.user_id()),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0()))].join('');
});
oc.web.actions.cmail.cmail_fullscreen_cookie = (function oc$web$actions$cmail$cmail_fullscreen_cookie(){
return ["cmail-fullscreen-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.jwt.user_id())].join('');
});
oc.web.actions.cmail.cmail_fullscreen_save = (function oc$web$actions$cmail$cmail_fullscreen_save(fullscreen_QMARK_){
return oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$3(oc.web.actions.cmail.cmail_fullscreen_cookie(),fullscreen_QMARK_,((((60) * (60)) * (24)) * (30)));
});
oc.web.actions.cmail.save_edit_open_cookie = (function oc$web$actions$cmail$save_edit_open_cookie(entry_data){
return oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$3(oc.web.actions.cmail.edit_open_cookie(),(function (){var or__4126__auto__ = [cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(entry_data)),"/",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data))].join('');
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return true;
}
})(),((((60) * (60)) * (24)) * (365)));
});
oc.web.actions.cmail.cmail_show = (function oc$web$actions$cmail$cmail_show(var_args){
var args__4742__auto__ = [];
var len__4736__auto___42406 = arguments.length;
var i__4737__auto___42407 = (0);
while(true){
if((i__4737__auto___42407 < len__4736__auto___42406)){
args__4742__auto__.push((arguments[i__4737__auto___42407]));

var G__42408 = (i__4737__auto___42407 + (1));
i__4737__auto___42407 = G__42408;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.actions.cmail.cmail_show.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.actions.cmail.cmail_show.cljs$core$IFn$_invoke$arity$variadic = (function (initial_entry_data,p__42350){
var vec__42351 = p__42350;
var cmail_state = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42351,(0),null);
var fixed_cmail_state = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cmail_state,new cljs.core.Keyword(null,"auto","auto",-566279492));
if(cljs.core.truth_(new cljs.core.Keyword(null,"collapsed","collapsed",-628494523).cljs$core$IFn$_invoke$arity$1(cmail_state))){
} else {
oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$1(oc.web.actions.cmail.cmail_fullscreen_cookie());
}

if(((cljs.core.not(new cljs.core.Keyword(null,"auto","auto",-566279492).cljs$core$IFn$_invoke$arity$1(cmail_state))) && (cljs.core.not(new cljs.core.Keyword(null,"collapsed","collapsed",-628494523).cljs$core$IFn$_invoke$arity$1(cmail_state))))){
oc.web.actions.cmail.save_edit_open_cookie(initial_entry_data);
} else {
}

return oc.web.actions.cmail.load_cached_item.cljs$core$IFn$_invoke$arity$variadic(initial_entry_data,cljs.core.first(oc.web.dispatcher.cmail_data_key),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(function (){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),oc.web.dispatcher.cmail_state_key,fixed_cmail_state], null));
})], 0));
}));

(oc.web.actions.cmail.cmail_show.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.actions.cmail.cmail_show.cljs$lang$applyTo = (function (seq42348){
var G__42349 = cljs.core.first(seq42348);
var seq42348__$1 = cljs.core.next(seq42348);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__42349,seq42348__$1);
}));

oc.web.actions.cmail.cmail_expand = (function oc$web$actions$cmail$cmail_expand(initial_entry_data,cmail_state){
oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$1(oc.web.actions.cmail.cmail_fullscreen_cookie());

oc.web.actions.cmail.save_edit_open_cookie(initial_entry_data);

return oc.web.actions.cmail.load_cached_item.cljs$core$IFn$_invoke$arity$variadic(initial_entry_data,cljs.core.first(oc.web.dispatcher.cmail_data_key),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(function (){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),oc.web.dispatcher.cmail_state_key,cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cmail_state,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"collapsed","collapsed",-628494523),false], null)], 0))], null));
})], 0));
});
oc.web.actions.cmail.cmail_reset = (function oc$web$actions$cmail$cmail_reset(){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),oc.web.dispatcher.cmail_data_key,oc.web.actions.cmail.get_default_section()], null));

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),oc.web.dispatcher.cmail_state_key,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"collapsed","collapsed",-628494523),true,new cljs.core.Keyword(null,"key","key",-1516042587),oc.web.lib.utils.activity_uuid()], null)], null));
});
oc.web.actions.cmail.cmail_hide = (function oc$web$actions$cmail$cmail_hide(){
oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$1(oc.web.actions.cmail.edit_open_cookie());

oc.web.actions.cmail.cmail_reset();

return oc.web.utils.dom.unlock_page_scroll();
});
oc.web.actions.cmail.cmail_fullscreen = (function oc$web$actions$cmail$cmail_fullscreen(){
var current_state = oc.web.dispatcher.cmail_state.cljs$core$IFn$_invoke$arity$0();
oc.web.actions.cmail.cmail_fullscreen_save(true);

oc.web.lib.utils.scroll_to_y.cljs$core$IFn$_invoke$arity$variadic((0),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(0)], 0));

oc.web.utils.dom.lock_page_scroll();

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),oc.web.dispatcher.cmail_state_key,(function (p1__42357_SHARP_){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__42357_SHARP_,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"fullscreen","fullscreen",-4371054),true,new cljs.core.Keyword(null,"collapsed","collapsed",-628494523),false], null)], 0));
})], null));
});
oc.web.actions.cmail.cmail_toggle_fullscreen = (function oc$web$actions$cmail$cmail_toggle_fullscreen(){
var next_fullscreen_value = cljs.core.not(new cljs.core.Keyword(null,"fullscreen","fullscreen",-4371054).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.cmail_state.cljs$core$IFn$_invoke$arity$0()));
oc.web.actions.cmail.cmail_fullscreen_save(next_fullscreen_value);

if(next_fullscreen_value){
oc.web.lib.utils.scroll_to_y.cljs$core$IFn$_invoke$arity$variadic((0),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(0)], 0));
} else {
}

if(next_fullscreen_value){
oc.web.utils.dom.lock_page_scroll();
} else {
oc.web.utils.dom.unlock_page_scroll();
}

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),oc.web.dispatcher.cmail_state_key,(function (p1__42360_SHARP_){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__42360_SHARP_,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"fullscreen","fullscreen",-4371054),next_fullscreen_value], null)], 0));
})], null));
});
oc.web.actions.cmail.cmail_toggle_must_see = (function oc$web$actions$cmail$cmail_toggle_must_see(){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),oc.web.dispatcher.cmail_data_key,(function (p1__42361_SHARP_){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__42361_SHARP_,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"must-see","must-see",-2009706697),cljs.core.not(new cljs.core.Keyword(null,"must-see","must-see",-2009706697).cljs$core$IFn$_invoke$arity$1(p1__42361_SHARP_)),new cljs.core.Keyword(null,"has-changes","has-changes",-631476764),true], null)], 0));
})], null));
});
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.actions !== 'undefined') && (typeof oc.web.actions.cmail !== 'undefined') && (typeof oc.web.actions.cmail.cmail_reopen_only_one !== 'undefined')){
} else {
oc.web.actions.cmail.cmail_reopen_only_one = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(false);
}
oc.web.actions.cmail.cmail_reopen_QMARK_ = (function oc$web$actions$cmail$cmail_reopen_QMARK_(){
if(cljs.core.compare_and_set_BANG_(oc.web.actions.cmail.cmail_reopen_only_one,false,true)){
return oc.web.lib.utils.after((100),(function (){
if(cljs.core.truth_((function (){var or__4126__auto__ = cljs.core.not(oc.web.dispatcher.cmail_state.cljs$core$IFn$_invoke$arity$0());
if(or__4126__auto__){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"collapsed","collapsed",-628494523).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.cmail_state.cljs$core$IFn$_invoke$arity$0());
}
})())){
var cmail_state = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"auto","auto",-566279492),true,new cljs.core.Keyword(null,"fullscreen","fullscreen",-4371054),cljs.core.not(oc.web.lib.responsive.is_mobile_size_QMARK_()),new cljs.core.Keyword(null,"collapsed","collapsed",-628494523),false,new cljs.core.Keyword(null,"key","key",-1516042587),oc.web.lib.utils.activity_uuid()], null);
if(((cljs.core.contains_QMARK_(oc.web.dispatcher.query_params.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"new","new",-2085437848))) && ((!(cljs.core.contains_QMARK_(oc.web.dispatcher.query_params.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"access","access",2027349272))))))){
var new_data = oc.web.actions.cmail.get_board_for_edit.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([oc.web.dispatcher.query_param.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"new","new",-2085437848))], 0));
var with_headline = (cljs.core.truth_(oc.web.dispatcher.query_param.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"headline","headline",-157157727)))?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(new_data,new cljs.core.Keyword(null,"headline","headline",-157157727),oc.web.dispatcher.query_param.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"headline","headline",-157157727))):new_data);
if(cljs.core.truth_(new_data)){
return oc.web.actions.cmail.cmail_show.cljs$core$IFn$_invoke$arity$variadic(with_headline,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cmail_state], 0));
} else {
return null;
}
} else {
var temp__5735__auto__ = (function (){var or__4126__auto__ = oc.web.dispatcher.query_param.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"edit","edit",-1641834166));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.lib.cookies.get_cookie(oc.web.actions.cmail.edit_open_cookie());
}
})();
if(cljs.core.truth_(temp__5735__auto__)){
var edit_activity_param = temp__5735__auto__;
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(edit_activity_param,"true")){
return oc.web.actions.cmail.cmail_show.cljs$core$IFn$_invoke$arity$variadic(cljs.core.PersistentArrayMap.EMPTY,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cmail_state], 0));
} else {
var vec__42370 = clojure.string.split.cljs$core$IFn$_invoke$arity$2(edit_activity_param,/\//);
var board_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42370,(0),null);
var activity_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42370,(1),null);
var edit_activity_data = oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$1(activity_uuid);
if(cljs.core.truth_(edit_activity_data)){
return oc.web.actions.cmail.cmail_show.cljs$core$IFn$_invoke$arity$variadic(edit_activity_data,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cmail_state], 0));
} else {
if(cljs.core.truth_((function (){var and__4115__auto__ = board_slug;
if(cljs.core.truth_(and__4115__auto__)){
return activity_uuid;
} else {
return and__4115__auto__;
}
})())){
return oc.web.actions.cmail.get_entry_with_uuid.cljs$core$IFn$_invoke$arity$variadic(board_slug,activity_uuid,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(function (success,status){
if(cljs.core.truth_(success)){
return oc.web.actions.cmail.cmail_show.cljs$core$IFn$_invoke$arity$variadic(oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$1(activity_uuid),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cmail_state], 0));
} else {
return null;
}
})], 0));
} else {
return null;
}
}
}
} else {
return null;
}
}
} else {
return null;
}
}));
} else {
return null;
}
});

//# sourceMappingURL=oc.web.actions.cmail.js.map
