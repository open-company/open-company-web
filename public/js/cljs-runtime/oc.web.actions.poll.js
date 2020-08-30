goog.provide('oc.web.actions.poll');
oc.web.actions.poll.add_poll = (function oc$web$actions$poll$add_poll(dispatch_key,poll_id){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.actions.poll",null,13,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Adding poll with id",poll_id], null);
}),null)),null,355191706);

var cur_user = oc.web.dispatcher.current_user_data.cljs$core$IFn$_invoke$arity$0();
var poll_data = oc.web.utils.poll.poll_data(cur_user,poll_id);
var poll_key = ((cljs.core.coll_QMARK_(dispatch_key))?cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(dispatch_key,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"polls","polls",-580623582),cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(poll_id)], null))):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [dispatch_key,new cljs.core.Keyword(null,"polls","polls",-580623582),cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(poll_id)], null));
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),poll_key,poll_data], null));
});
oc.web.actions.poll.remove_poll = (function oc$web$actions$poll$remove_poll(dispatch_key,poll_data){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.actions.poll",null,22,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Remove poll",dispatch_key,new cljs.core.Keyword(null,"poll-uuid","poll-uuid",110210618).cljs$core$IFn$_invoke$arity$1(poll_data)], null);
}),null)),null,521709601);

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(dispatch_key,new cljs.core.Keyword(null,"polls","polls",-580623582))),(function (p1__38822_SHARP_){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(p1__38822_SHARP_,new cljs.core.Keyword(null,"poll-uuid","poll-uuid",110210618).cljs$core$IFn$_invoke$arity$1(poll_data));
})], null));

var temp__5735__auto__ = oc.web.utils.poll.get_poll_portal_element(new cljs.core.Keyword(null,"poll-uuid","poll-uuid",110210618).cljs$core$IFn$_invoke$arity$1(poll_data));
if(cljs.core.truth_(temp__5735__auto__)){
var poll_element = temp__5735__auto__;
return poll_element.parentElement.removeChild(poll_element);
} else {
return null;
}
});
oc.web.actions.poll.remove_poll_for_max_retry = (function oc$web$actions$poll$remove_poll_for_max_retry(dispatch_key,poll_data){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.actions.poll",null,28,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Remove poll for max retry",dispatch_key,new cljs.core.Keyword(null,"poll-uuid","poll-uuid",110210618).cljs$core$IFn$_invoke$arity$1(poll_data)], null);
}),null)),null,-1945670874);

return oc.web.actions.poll.remove_poll(dispatch_key,poll_data);
});
oc.web.actions.poll.show_preview = (function oc$web$actions$poll$show_preview(poll_key){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),poll_key,(function (p1__38823_SHARP_){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__38823_SHARP_,new cljs.core.Keyword(null,"preview","preview",451279890),true);
})], null));
});
oc.web.actions.poll.hide_preview = (function oc$web$actions$poll$hide_preview(poll_key){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),poll_key,(function (p1__38827_SHARP_){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(p1__38827_SHARP_,new cljs.core.Keyword(null,"preview","preview",451279890));
})], null));
});
oc.web.actions.poll.update_question = (function oc$web$actions$poll$update_question(poll_key,poll_data,question){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(poll_key,new cljs.core.Keyword(null,"question","question",-1411720117))),question], null));

return oc.web.lib.utils.after((0),(function (){
return oc.web.utils.poll.set_poll_element_question_BANG_(new cljs.core.Keyword(null,"poll-uuid","poll-uuid",110210618).cljs$core$IFn$_invoke$arity$1(poll_data),question);
}));
});
oc.web.actions.poll.add_reply = (function oc$web$actions$poll$add_reply(var_args){
var args__4742__auto__ = [];
var len__4736__auto___38855 = arguments.length;
var i__4737__auto___38856 = (0);
while(true){
if((i__4737__auto___38856 < len__4736__auto___38855)){
args__4742__auto__.push((arguments[i__4737__auto___38856]));

var G__38857 = (i__4737__auto___38856 + (1));
i__4737__auto___38856 = G__38857;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.actions.poll.add_reply.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.actions.poll.add_reply.cljs$core$IFn$_invoke$arity$variadic = (function (poll_key,p__38834){
var vec__38835 = p__38834;
var reply_body = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38835,(0),null);
var new_reply_data = oc.web.utils.poll.poll_reply(oc.web.dispatcher.current_user_data.cljs$core$IFn$_invoke$arity$0(),reply_body);
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),poll_key,(function (p1__38828_SHARP_){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__38828_SHARP_,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"replies","replies",-1389888974),cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(new cljs.core.Keyword(null,"replies","replies",-1389888974).cljs$core$IFn$_invoke$arity$1(p1__38828_SHARP_),cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"reply-id","reply-id",-1840116616).cljs$core$IFn$_invoke$arity$1(new_reply_data)),new_reply_data),new cljs.core.Keyword(null,"updated-at","updated-at",-1592622336),oc.web.lib.utils.as_of_now()], null)], 0));
})], null));
}));

(oc.web.actions.poll.add_reply.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.actions.poll.add_reply.cljs$lang$applyTo = (function (seq38832){
var G__38833 = cljs.core.first(seq38832);
var seq38832__$1 = cljs.core.next(seq38832);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__38833,seq38832__$1);
}));

oc.web.actions.poll.update_reply = (function oc$web$actions$poll$update_reply(poll_key,reply_id,body){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(poll_key,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"replies","replies",-1389888974),cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(reply_id),new cljs.core.Keyword(null,"body","body",-2049205669)], null))),body], null));
});
oc.web.actions.poll.delete_reply = (function oc$web$actions$poll$delete_reply(poll_key,poll_reply_id){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),poll_key,(function (p1__38838_SHARP_){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__38838_SHARP_,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"replies","replies",-1389888974),cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"replies","replies",-1389888974).cljs$core$IFn$_invoke$arity$1(p1__38838_SHARP_),cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(poll_reply_id)),new cljs.core.Keyword(null,"updated-at","updated-at",-1592622336),oc.web.lib.utils.as_of_now()], null)], 0));
})], null));
});
oc.web.actions.poll.add_new_reply = (function oc$web$actions$poll$add_new_reply(poll_data,poll_key,reply_body){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.actions.poll",null,56,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Adding new reply to",poll_key,"body:",reply_body], null);
}),null)),null,-2106318906);

var add_reply_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(poll_data),"reply","POST"], 0));
oc.web.actions.poll.add_reply.cljs$core$IFn$_invoke$arity$variadic(poll_key,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([reply_body], 0));

return oc.web.api.poll_add_reply(add_reply_link,reply_body,(function (p__38839){
var map__38840 = p__38839;
var map__38840__$1 = (((((!((map__38840 == null))))?(((((map__38840.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38840.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38840):map__38840);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38840__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38840__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38840__$1,new cljs.core.Keyword(null,"success","success",1890645906));
return oc.web.actions.activity.activity_get_finish(status,(cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):cljs.core.PersistentArrayMap.EMPTY),null);
}));
});
oc.web.actions.poll.delete_existing_reply = (function oc$web$actions$poll$delete_existing_reply(poll_data,poll_key,poll_reply_id){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.actions.poll",null,63,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Deleting existing reply from",poll_key,"reply:",poll_reply_id], null);
}),null)),null,-307861935);

var reply_data = cljs.core.keyword.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"replies","replies",-1389888974).cljs$core$IFn$_invoke$arity$1(poll_data),poll_reply_id);
var delete_reply_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(reply_data),"delete","DELETE"], 0));
oc.web.actions.poll.delete_reply(poll_key,poll_reply_id);

return oc.web.api.poll_delete_reply(delete_reply_link,(function (p__38842){
var map__38843 = p__38842;
var map__38843__$1 = (((((!((map__38843 == null))))?(((((map__38843.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38843.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38843):map__38843);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38843__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38843__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38843__$1,new cljs.core.Keyword(null,"success","success",1890645906));
return oc.web.actions.activity.activity_get_finish(status,(cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):cljs.core.PersistentArrayMap.EMPTY),null);
}));
});
oc.web.actions.poll.update_reply_vote = (function oc$web$actions$poll$update_reply_vote(user_id,reply,add_QMARK_){
if(cljs.core.truth_(add_QMARK_)){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(cljs.core.set(new cljs.core.Keyword(null,"votes","votes",-1161459422).cljs$core$IFn$_invoke$arity$1(reply)),user_id));
} else {
return cljs.core.filterv((function (p1__38845_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(p1__38845_SHARP_,user_id);
}),new cljs.core.Keyword(null,"votes","votes",-1161459422).cljs$core$IFn$_invoke$arity$1(reply));
}
});
oc.web.actions.poll.update_vote_reply = (function oc$web$actions$poll$update_vote_reply(user_id,replies,reply_id){
var reps_coll = cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (p1__38846_SHARP_){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__38846_SHARP_,new cljs.core.Keyword(null,"votes","votes",-1161459422),oc.web.actions.poll.update_reply_vote(user_id,p1__38846_SHARP_,cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"reply-id","reply-id",-1840116616).cljs$core$IFn$_invoke$arity$1(p1__38846_SHARP_),reply_id)));
}),cljs.core.vals(replies));
return cljs.core.zipmap(cljs.core.mapv.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"reply-id","reply-id",-1840116616),reps_coll),reps_coll);
});
oc.web.actions.poll.vote_reply = (function oc$web$actions$poll$vote_reply(poll_data,poll_key,reply_id){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.actions.poll",null,85,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Voting reply",reply_id], null);
}),null)),null,1443284299);

var reply = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(poll_data,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"replies","replies",-1389888974),cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(reply_id)], null));
var temp__5735__auto__ = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(reply),"vote"], 0));
if(cljs.core.truth_(temp__5735__auto__)){
var vote_link = temp__5735__auto__;
var user_id_38866 = new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.current_user_data.cljs$core$IFn$_invoke$arity$0());
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(poll_key,new cljs.core.Keyword(null,"replies","replies",-1389888974))),(function (p1__38847_SHARP_){
return oc.web.actions.poll.update_vote_reply(user_id_38866,p1__38847_SHARP_,reply_id);
})], null));

return oc.web.api.poll_vote(vote_link,(function (p__38848){
var map__38849 = p__38848;
var map__38849__$1 = (((((!((map__38849 == null))))?(((((map__38849.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38849.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38849):map__38849);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38849__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38849__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38849__$1,new cljs.core.Keyword(null,"success","success",1890645906));
return oc.web.actions.activity.activity_get_finish(status,(cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):cljs.core.PersistentArrayMap.EMPTY),null);
}));
} else {
return null;
}
});
oc.web.actions.poll.unvote_reply = (function oc$web$actions$poll$unvote_reply(poll_data,poll_key,reply_id){
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"info","info",-317069002),"oc.web.actions.poll",null,94,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Unvoting reply",reply_id], null);
}),null)),null,346227573);

var reply = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(poll_data,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"replies","replies",-1389888974),cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(reply_id)], null));
var temp__5735__auto__ = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(reply),"unvote"], 0));
if(cljs.core.truth_(temp__5735__auto__)){
var unvote_link = temp__5735__auto__;
var user_id_38869 = new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.current_user_data.cljs$core$IFn$_invoke$arity$0());
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),cljs.core.concat.cljs$core$IFn$_invoke$arity$2(poll_key,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"replies","replies",-1389888974),cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(reply_id),new cljs.core.Keyword(null,"votes","votes",-1161459422)], null)),(function (p1__38851_SHARP_){
return oc.web.actions.poll.update_reply_vote(user_id_38869,p1__38851_SHARP_,false);
})], null));

return oc.web.api.poll_vote(unvote_link,(function (p__38852){
var map__38853 = p__38852;
var map__38853__$1 = (((((!((map__38853 == null))))?(((((map__38853.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38853.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38853):map__38853);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38853__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38853__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38853__$1,new cljs.core.Keyword(null,"success","success",1890645906));
return oc.web.actions.activity.activity_get_finish(status,(cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):cljs.core.PersistentArrayMap.EMPTY),null);
}));
} else {
return null;
}
});

//# sourceMappingURL=oc.web.actions.poll.js.map
