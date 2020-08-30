goog.provide('oc.web.utils.poll');
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.utils !== 'undefined') && (typeof oc.web.utils.poll !== 'undefined') && (typeof oc.web.utils.poll.poll_selector_prefix !== 'undefined')){
} else {
oc.web.utils.poll.poll_selector_prefix = "oc-poll-portal-";
goog.exportSymbol('oc.web.utils.poll.poll_selector_prefix', oc.web.utils.poll.poll_selector_prefix);
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.utils !== 'undefined') && (typeof oc.web.utils.poll !== 'undefined') && (typeof oc.web.utils.poll.min_poll_replies !== 'undefined')){
} else {
oc.web.utils.poll.min_poll_replies = (2);
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.utils !== 'undefined') && (typeof oc.web.utils.poll !== 'undefined') && (typeof oc.web.utils.poll.max_question_length !== 'undefined')){
} else {
oc.web.utils.poll.max_question_length = (128);
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.utils !== 'undefined') && (typeof oc.web.utils.poll !== 'undefined') && (typeof oc.web.utils.poll.max_reply_length !== 'undefined')){
} else {
oc.web.utils.poll.max_reply_length = (64);
}
oc.web.utils.poll.created_at = (function oc$web$utils$poll$created_at(){
return oc.web.lib.utils.as_of_now();
});
oc.web.utils.poll.new_poll_id = (function oc$web$utils$poll$new_poll_id(){
return oc.web.lib.utils.activity_uuid();
});
oc.web.utils.poll.new_reply_id = (function oc$web$utils$poll$new_reply_id(){
return oc.web.lib.utils.activity_uuid();
});
oc.web.utils.poll.author_for_user = (function oc$web$utils$poll$author_for_user(user_data){
return new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"name","name",1843675177),oc.lib.user.name_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user_data], 0)),new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103),new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103).cljs$core$IFn$_invoke$arity$1(user_data),new cljs.core.Keyword(null,"user-id","user-id",-206822291),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user_data)], null);
});
oc.web.utils.poll.poll_reply = (function oc$web$utils$poll$poll_reply(var_args){
var args__4742__auto__ = [];
var len__4736__auto___37276 = arguments.length;
var i__4737__auto___37277 = (0);
while(true){
if((i__4737__auto___37277 < len__4736__auto___37276)){
args__4742__auto__.push((arguments[i__4737__auto___37277]));

var G__37278 = (i__4737__auto___37277 + (1));
i__4737__auto___37277 = G__37278;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((2) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((2)),(0),null)):null);
return oc.web.utils.poll.poll_reply.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__4743__auto__);
});

(oc.web.utils.poll.poll_reply.cljs$core$IFn$_invoke$arity$variadic = (function (user_data,body,p__37245){
var vec__37246 = p__37245;
var ts = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37246,(0),null);
return new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"created-at","created-at",-89248644),(function (){var or__4126__auto__ = ts;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.utils.poll.created_at();
}
})(),new cljs.core.Keyword(null,"body","body",-2049205669),(function (){var or__4126__auto__ = body;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "";
}
})(),new cljs.core.Keyword(null,"author","author",2111686192),oc.web.utils.poll.author_for_user(user_data),new cljs.core.Keyword(null,"reply-id","reply-id",-1840116616),oc.web.utils.poll.new_reply_id(),new cljs.core.Keyword(null,"votes","votes",-1161459422),cljs.core.PersistentVector.EMPTY], null);
}));

(oc.web.utils.poll.poll_reply.cljs$lang$maxFixedArity = (2));

/** @this {Function} */
(oc.web.utils.poll.poll_reply.cljs$lang$applyTo = (function (seq37242){
var G__37243 = cljs.core.first(seq37242);
var seq37242__$1 = cljs.core.next(seq37242);
var G__37244 = cljs.core.first(seq37242__$1);
var seq37242__$2 = cljs.core.next(seq37242__$1);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__37243,G__37244,seq37242__$2);
}));

oc.web.utils.poll.poll_default_replies = (function oc$web$utils$poll$poll_default_replies(user_data){
var ts = oc.web.lib.utils.js_date().getTime();
var reps = cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__37249_SHARP_){
return oc.web.utils.poll.poll_reply.cljs$core$IFn$_invoke$arity$variadic(user_data,"",cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(new Date((ts + p1__37249_SHARP_))).toISOString()], 0));
}),cljs.core.range.cljs$core$IFn$_invoke$arity$1(oc.web.utils.poll.min_poll_replies));
return cljs.core.zipmap(cljs.core.map.cljs$core$IFn$_invoke$arity$2(cljs.core.comp.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword,new cljs.core.Keyword(null,"reply-id","reply-id",-1840116616)),reps),reps);
});
oc.web.utils.poll.poll_data = (function oc$web$utils$poll$poll_data(user_data,poll_id){
return new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"question","question",-1411720117),"",new cljs.core.Keyword(null,"poll-uuid","poll-uuid",110210618),poll_id,new cljs.core.Keyword(null,"can-add-reply","can-add-reply",-975368010),cljs.core.boolean$(oc.web.local_settings.poll_can_add_reply),new cljs.core.Keyword(null,"created-at","created-at",-89248644),oc.web.utils.poll.created_at(),new cljs.core.Keyword(null,"updated-at","updated-at",-1592622336),oc.web.utils.poll.created_at(),new cljs.core.Keyword(null,"author","author",2111686192),oc.web.utils.poll.author_for_user(user_data),new cljs.core.Keyword(null,"replies","replies",-1389888974),oc.web.utils.poll.poll_default_replies(user_data)], null);
});
oc.web.utils.poll.clean_poll_reply = (function oc$web$utils$poll$clean_poll_reply(poll_reply_data){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(poll_reply_data,new cljs.core.Keyword(null,"links","links",-654507394));
});
oc.web.utils.poll.clean_poll = (function oc$web$utils$poll$clean_poll(poll_data){
return cljs.core.update.cljs$core$IFn$_invoke$arity$3(cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(poll_data,new cljs.core.Keyword(null,"links","links",-654507394),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"preview","preview",451279890)], 0)),new cljs.core.Keyword(null,"replies","replies",-1389888974),(function (replies){
return cljs.core.zipmap(cljs.core.mapv.cljs$core$IFn$_invoke$arity$2(cljs.core.comp.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword,new cljs.core.Keyword(null,"reply-id","reply-id",-1840116616)),cljs.core.vals(replies)),cljs.core.mapv.cljs$core$IFn$_invoke$arity$2(oc.web.utils.poll.clean_poll_reply,cljs.core.vals(replies)));
}));
});
/**
 * Clean not needed keys from poll maps.
 */
oc.web.utils.poll.clean_polls = (function oc$web$utils$poll$clean_polls(activity_data){
if(cljs.core.seq(new cljs.core.Keyword(null,"polls","polls",-580623582).cljs$core$IFn$_invoke$arity$1(activity_data))){
return cljs.core.update.cljs$core$IFn$_invoke$arity$3(activity_data,new cljs.core.Keyword(null,"polls","polls",-580623582),(function (polls){
return cljs.core.zipmap(cljs.core.mapv.cljs$core$IFn$_invoke$arity$2(cljs.core.comp.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword,new cljs.core.Keyword(null,"poll-uuid","poll-uuid",110210618)),cljs.core.vals(polls)),cljs.core.mapv.cljs$core$IFn$_invoke$arity$2(oc.web.utils.poll.clean_poll,cljs.core.vals(polls)));
}));
} else {
return activity_data;
}
});
oc.web.utils.poll.get_poll_portal_element = (function oc$web$utils$poll$get_poll_portal_element(poll_uuid){
return document.querySelector(dommy.core.selector([".",oc.web.utils.poll.poll_selector_prefix,cljs.core.str.cljs$core$IFn$_invoke$arity$1(poll_uuid)].join('')));
});
oc.web.utils.poll.set_poll_element_question_BANG_ = (function oc$web$utils$poll$set_poll_element_question_BANG_(poll_uuid,question_string){
var temp__5735__auto__ = oc.web.utils.poll.get_poll_portal_element(poll_uuid);
if(cljs.core.truth_(temp__5735__auto__)){
var portal_el = temp__5735__auto__;
var target_obj_37250 = portal_el;
var _STAR_runtime_state_STAR__orig_val__37253 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__37254 = oops.state.prepare_state(target_obj_37250,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__37254);

try{var parent_obj_37251_37282 = (function (){var next_obj_37252 = ((oops.core.validate_object_access_dynamically(target_obj_37250,(0),"dataset",true,true,false))?(target_obj_37250["dataset"]):null);
return next_obj_37252;
})();
if(oops.core.validate_object_access_dynamically(parent_obj_37251_37282,(2),"question",true,true,true)){
(parent_obj_37251_37282["question"] = question_string);
} else {
}

return target_obj_37250;
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__37253);
}} else {
return null;
}
});
oc.web.utils.poll.report_unmounted_poll = (function oc$web$utils$poll$report_unmounted_poll(p__37255){
var map__37256 = p__37255;
var map__37256__$1 = (((((!((map__37256 == null))))?(((((map__37256.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__37256.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__37256):map__37256);
var _props = map__37256__$1;
var activity_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37256__$1,new cljs.core.Keyword(null,"activity-data","activity-data",1293689136));
var poll_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37256__$1,new cljs.core.Keyword(null,"poll-data","poll-data",-172934296));
var container_selector = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37256__$1,new cljs.core.Keyword(null,"container-selector","container-selector",6506114));
return oc.web.lib.sentry.capture_message_with_extra_context_BANG_(new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"poll-uuid","poll-uuid",110210618),new cljs.core.Keyword(null,"poll-uuid","poll-uuid",110210618).cljs$core$IFn$_invoke$arity$1(poll_data),new cljs.core.Keyword(null,"activity-uuid","activity-uuid",-663317778),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data),new cljs.core.Keyword(null,"revision-id","revision-id",1301980641),new cljs.core.Keyword(null,"revision-id","revision-id",1301980641).cljs$core$IFn$_invoke$arity$1(activity_data),new cljs.core.Keyword(null,"poll-updated-at","poll-updated-at",1590005583),new cljs.core.Keyword(null,"updated-at","updated-at",-1592622336).cljs$core$IFn$_invoke$arity$1(poll_data),new cljs.core.Keyword(null,"org-slug","org-slug",-726595051),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"container-selector","container-selector",6506114),container_selector,new cljs.core.Keyword(null,"win-url","win-url",-1938636271),window.location.href], null),"Failed creating portal for poll");
});
/**
 * @param {...*} var_args
 */
oc.web.utils.poll.sorted_replies = (function() { 
var oc$web$utils$poll$sorted_replies__delegate = function (args__33708__auto__){
var ocr_37260 = cljs.core.vec(args__33708__auto__);
try{if(((cljs.core.vector_QMARK_(ocr_37260)) && ((cljs.core.count(ocr_37260) === 1)))){
try{var ocr_37260_0__37262 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_37260,(0));
if((function (p1__37258_SHARP_){
return ((cljs.core.map_QMARK_(p1__37258_SHARP_)) && (cljs.core.contains_QMARK_(p1__37258_SHARP_,new cljs.core.Keyword(null,"replies","replies",-1389888974))));
})(ocr_37260_0__37262)){
var poll_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_37260,(0));
var G__37270 = cljs.core.vals(new cljs.core.Keyword(null,"replies","replies",-1389888974).cljs$core$IFn$_invoke$arity$1(poll_data));
return (oc.web.utils.poll.sorted_replies.cljs$core$IFn$_invoke$arity$1 ? oc.web.utils.poll.sorted_replies.cljs$core$IFn$_invoke$arity$1(G__37270) : oc.web.utils.poll.sorted_replies.call(null,G__37270));
} else {
throw cljs.core.match.backtrack;

}
}catch (e37264){if((e37264 instanceof Error)){
var e__32662__auto__ = e37264;
if((e__32662__auto__ === cljs.core.match.backtrack)){
try{var ocr_37260_0__37262 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_37260,(0));
if(cljs.core.map_QMARK_(ocr_37260_0__37262)){
var replies_map = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_37260,(0));
var G__37269 = cljs.core.vals(replies_map);
return (oc.web.utils.poll.sorted_replies.cljs$core$IFn$_invoke$arity$1 ? oc.web.utils.poll.sorted_replies.cljs$core$IFn$_invoke$arity$1(G__37269) : oc.web.utils.poll.sorted_replies.call(null,G__37269));
} else {
throw cljs.core.match.backtrack;

}
}catch (e37265){if((e37265 instanceof Error)){
var e__32662__auto____$1 = e37265;
if((e__32662__auto____$1 === cljs.core.match.backtrack)){
try{var ocr_37260_0__37262 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_37260,(0));
if(cljs.core.coll_QMARK_(ocr_37260_0__37262)){
var replies_coll = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_37260,(0));
return cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2((function (p1__37259_SHARP_){
return oc.web.lib.utils.js_date.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"created-at","created-at",-89248644).cljs$core$IFn$_invoke$arity$1(p1__37259_SHARP_)], 0)).getTime();
}),replies_coll);
} else {
throw cljs.core.match.backtrack;

}
}catch (e37266){if((e37266 instanceof Error)){
var e__32662__auto____$2 = e37266;
if((e__32662__auto____$2 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$2;
}
} else {
throw e37266;

}
}} else {
throw e__32662__auto____$1;
}
} else {
throw e37265;

}
}} else {
throw e__32662__auto__;
}
} else {
throw e37264;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e37263){if((e37263 instanceof Error)){
var e__32662__auto__ = e37263;
if((e__32662__auto__ === cljs.core.match.backtrack)){
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ocr_37260)].join('')));
} else {
throw e__32662__auto__;
}
} else {
throw e37263;

}
}};
var oc$web$utils$poll$sorted_replies = function (var_args){
var args__33708__auto__ = null;
if (arguments.length > 0) {
var G__37283__i = 0, G__37283__a = new Array(arguments.length -  0);
while (G__37283__i < G__37283__a.length) {G__37283__a[G__37283__i] = arguments[G__37283__i + 0]; ++G__37283__i;}
  args__33708__auto__ = new cljs.core.IndexedSeq(G__37283__a,0,null);
} 
return oc$web$utils$poll$sorted_replies__delegate.call(this,args__33708__auto__);};
oc$web$utils$poll$sorted_replies.cljs$lang$maxFixedArity = 0;
oc$web$utils$poll$sorted_replies.cljs$lang$applyTo = (function (arglist__37284){
var args__33708__auto__ = cljs.core.seq(arglist__37284);
return oc$web$utils$poll$sorted_replies__delegate(args__33708__auto__);
});
oc$web$utils$poll$sorted_replies.cljs$core$IFn$_invoke$arity$variadic = oc$web$utils$poll$sorted_replies__delegate;
return oc$web$utils$poll$sorted_replies;
})()
;

//# sourceMappingURL=oc.web.utils.poll.js.map
