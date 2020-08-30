goog.provide('oc.web.components.ui.poll');
oc.web.components.ui.poll.get_dispatch_key = (function oc$web$components$ui$poll$get_dispatch_key(poll_key){
return cljs.core.subvec.cljs$core$IFn$_invoke$arity$3(poll_key,(0),(cljs.core.count(poll_key) - (2)));
});
oc.web.components.ui.poll.poll_read = rum.core.build_defcs((function (s,p__38863){
var map__38864 = p__38863;
var map__38864__$1 = (((((!((map__38864 == null))))?(((((map__38864.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38864.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38864):map__38864);
var poll_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38864__$1,new cljs.core.Keyword(null,"poll-data","poll-data",-172934296));
var poll_key = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38864__$1,new cljs.core.Keyword(null,"poll-key","poll-key",-992015670));
var current_user_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38864__$1,new cljs.core.Keyword(null,"current-user-id","current-user-id",-2013633451));
var can_vote_QMARK_ = ((cljs.core.seq(current_user_id)) && (cljs.core.not(new cljs.core.Keyword(null,"preview","preview",451279890).cljs$core$IFn$_invoke$arity$1(poll_data))));
var user_voted_QMARK_ = ((cljs.core.not(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.poll","adding-reply","oc.web.components.ui.poll/adding-reply",1883405595).cljs$core$IFn$_invoke$arity$1(s)))) && (cljs.core.seq(cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__38858_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(current_user_id,p1__38858_SHARP_);
}),cljs.core.mapcat.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"votes","votes",-1161459422),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"replies","replies",-1389888974).cljs$core$IFn$_invoke$arity$1(poll_data)], 0))))));
var total_votes_count = cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (p1__38860_SHARP_,p2__38859_SHARP_){
return (cljs.core.count(new cljs.core.Keyword(null,"votes","votes",-1161459422).cljs$core$IFn$_invoke$arity$1(p2__38859_SHARP_)) + p1__38860_SHARP_);
}),(0),cljs.core.vals(new cljs.core.Keyword(null,"replies","replies",-1389888974).cljs$core$IFn$_invoke$arity$1(poll_data)));
return React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["poll","poll-read",oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"voted","voted",179456584),user_voted_QMARK_], null))], null))}),React.createElement("div",({"className": "poll-question"}),(function (){var attrs38867 = new cljs.core.Keyword(null,"question","question",-1411720117).cljs$core$IFn$_invoke$arity$1(poll_data);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs38867))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["poll-question-body"], null)], null),attrs38867], 0))):({"className": "poll-question-body"})),((cljs.core.map_QMARK_(attrs38867))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs38867)], null)));
})(),(function (){var attrs38868 = [cljs.core.str.cljs$core$IFn$_invoke$arity$1(total_votes_count)," vote",((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(total_votes_count,(1)))?"s":null)].join('');
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs38868))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["poll-total-count"], null)], null),attrs38868], 0))):({"className": "poll-total-count"})),((cljs.core.map_QMARK_(attrs38868))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs38868)], null)));
})()),React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["poll-replies",oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"can-vote","can-vote",-262500878),can_vote_QMARK_,new cljs.core.Keyword(null,"animate","animate",1850194573),cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.poll","animate","oc.web.components.ui.poll/animate",-1859864604).cljs$core$IFn$_invoke$arity$1(s))], null))], null))}),cljs.core.into_array.cljs$core$IFn$_invoke$arity$1((function (){var iter__4529__auto__ = (function oc$web$components$ui$poll$iter__38872(s__38873){
return (new cljs.core.LazySeq(null,(function (){
var s__38873__$1 = s__38873;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__38873__$1);
if(temp__5735__auto__){
var s__38873__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__38873__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__38873__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__38875 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__38874 = (0);
while(true){
if((i__38874 < size__4528__auto__)){
var reply = cljs.core._nth(c__4527__auto__,i__38874);
if(cljs.core.seq(cuerdas.core.trim.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(reply)))){
var votes_percent = ((cljs.core.count(new cljs.core.Keyword(null,"votes","votes",-1161459422).cljs$core$IFn$_invoke$arity$1(reply)) / total_votes_count) * (100));
var rounded_votes_percent = (cljs.core.truth_(isNaN(votes_percent))?(0):(Math.round(((10) * votes_percent)) / (10)));
var reply_voted_QMARK_ = ((can_vote_QMARK_)?cljs.core.some(((function (i__38874,s__38873__$1,votes_percent,rounded_votes_percent,reply,c__4527__auto__,size__4528__auto__,b__38875,s__38873__$2,temp__5735__auto__,can_vote_QMARK_,user_voted_QMARK_,total_votes_count,map__38864,map__38864__$1,poll_data,poll_key,current_user_id){
return (function (p1__38861_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(p1__38861_SHARP_,current_user_id)){
return p1__38861_SHARP_;
} else {
return null;
}
});})(i__38874,s__38873__$1,votes_percent,rounded_votes_percent,reply,c__4527__auto__,size__4528__auto__,b__38875,s__38873__$2,temp__5735__auto__,can_vote_QMARK_,user_voted_QMARK_,total_votes_count,map__38864,map__38864__$1,poll_data,poll_key,current_user_id))
,new cljs.core.Keyword(null,"votes","votes",-1161459422).cljs$core$IFn$_invoke$arity$1(reply)):false);
cljs.core.chunk_append(b__38875,React.createElement("div",({"key": ["poll-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"poll-uuid","poll-uuid",110210618).cljs$core$IFn$_invoke$arity$1(poll_data)),"-reply-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"reply-id","reply-id",-1840116616).cljs$core$IFn$_invoke$arity$1(reply))].join(''), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["poll-reply-outer","group",oc.web.lib.utils.class_set(cljs.core.PersistentArrayMap.createAsIfByAssoc([["percent-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(Math.round(rounded_votes_percent))].join(''),true,new cljs.core.Keyword(null,"voted","voted",179456584),((cljs.core.not(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.poll","adding-reply","oc.web.components.ui.poll/adding-reply",1883405595).cljs$core$IFn$_invoke$arity$1(s))))?reply_voted_QMARK_:false)]))], null))}),React.createElement("button",({"type": "button", "onClick": ((can_vote_QMARK_)?((function (i__38874,s__38873__$1,votes_percent,rounded_votes_percent,reply_voted_QMARK_,reply,c__4527__auto__,size__4528__auto__,b__38875,s__38873__$2,temp__5735__auto__,can_vote_QMARK_,user_voted_QMARK_,total_votes_count,map__38864,map__38864__$1,poll_data,poll_key,current_user_id){
return (function (){
if(cljs.core.truth_(reply_voted_QMARK_)){
return oc.web.actions.poll.unvote_reply(poll_data,poll_key,new cljs.core.Keyword(null,"reply-id","reply-id",-1840116616).cljs$core$IFn$_invoke$arity$1(reply));
} else {
return oc.web.actions.poll.vote_reply(poll_data,poll_key,new cljs.core.Keyword(null,"reply-id","reply-id",-1840116616).cljs$core$IFn$_invoke$arity$1(reply));
}
});})(i__38874,s__38873__$1,votes_percent,rounded_votes_percent,reply_voted_QMARK_,reply,c__4527__auto__,size__4528__auto__,b__38875,s__38873__$2,temp__5735__auto__,can_vote_QMARK_,user_voted_QMARK_,total_votes_count,map__38864,map__38864__$1,poll_data,poll_key,current_user_id))
:null), "className": "mlb-reset poll-reply"}),(function (){var attrs38877 = new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(reply);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs38877))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["poll-reply-body"], null)], null),attrs38877], 0))):({"className": "poll-reply-body"})),((cljs.core.map_QMARK_(attrs38877))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs38877)], null)));
})()),(function (){var attrs38876 = [cljs.core.str.cljs$core$IFn$_invoke$arity$1(rounded_votes_percent),"%"].join('');
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs38876))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["poll-reply-count"], null)], null),attrs38876], 0))):({"className": "poll-reply-count"})),((cljs.core.map_QMARK_(attrs38876))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs38876)], null)));
})()));

var G__38928 = (i__38874 + (1));
i__38874 = G__38928;
continue;
} else {
var G__38929 = (i__38874 + (1));
i__38874 = G__38929;
continue;
}
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__38875),oc$web$components$ui$poll$iter__38872(cljs.core.chunk_rest(s__38873__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__38875),null);
}
} else {
var reply = cljs.core.first(s__38873__$2);
if(cljs.core.seq(cuerdas.core.trim.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(reply)))){
var votes_percent = ((cljs.core.count(new cljs.core.Keyword(null,"votes","votes",-1161459422).cljs$core$IFn$_invoke$arity$1(reply)) / total_votes_count) * (100));
var rounded_votes_percent = (cljs.core.truth_(isNaN(votes_percent))?(0):(Math.round(((10) * votes_percent)) / (10)));
var reply_voted_QMARK_ = ((can_vote_QMARK_)?cljs.core.some(((function (s__38873__$1,votes_percent,rounded_votes_percent,reply,s__38873__$2,temp__5735__auto__,can_vote_QMARK_,user_voted_QMARK_,total_votes_count,map__38864,map__38864__$1,poll_data,poll_key,current_user_id){
return (function (p1__38861_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(p1__38861_SHARP_,current_user_id)){
return p1__38861_SHARP_;
} else {
return null;
}
});})(s__38873__$1,votes_percent,rounded_votes_percent,reply,s__38873__$2,temp__5735__auto__,can_vote_QMARK_,user_voted_QMARK_,total_votes_count,map__38864,map__38864__$1,poll_data,poll_key,current_user_id))
,new cljs.core.Keyword(null,"votes","votes",-1161459422).cljs$core$IFn$_invoke$arity$1(reply)):false);
return cljs.core.cons(React.createElement("div",({"key": ["poll-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"poll-uuid","poll-uuid",110210618).cljs$core$IFn$_invoke$arity$1(poll_data)),"-reply-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"reply-id","reply-id",-1840116616).cljs$core$IFn$_invoke$arity$1(reply))].join(''), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["poll-reply-outer","group",oc.web.lib.utils.class_set(cljs.core.PersistentArrayMap.createAsIfByAssoc([["percent-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(Math.round(rounded_votes_percent))].join(''),true,new cljs.core.Keyword(null,"voted","voted",179456584),((cljs.core.not(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.poll","adding-reply","oc.web.components.ui.poll/adding-reply",1883405595).cljs$core$IFn$_invoke$arity$1(s))))?reply_voted_QMARK_:false)]))], null))}),React.createElement("button",({"type": "button", "onClick": ((can_vote_QMARK_)?((function (s__38873__$1,votes_percent,rounded_votes_percent,reply_voted_QMARK_,reply,s__38873__$2,temp__5735__auto__,can_vote_QMARK_,user_voted_QMARK_,total_votes_count,map__38864,map__38864__$1,poll_data,poll_key,current_user_id){
return (function (){
if(cljs.core.truth_(reply_voted_QMARK_)){
return oc.web.actions.poll.unvote_reply(poll_data,poll_key,new cljs.core.Keyword(null,"reply-id","reply-id",-1840116616).cljs$core$IFn$_invoke$arity$1(reply));
} else {
return oc.web.actions.poll.vote_reply(poll_data,poll_key,new cljs.core.Keyword(null,"reply-id","reply-id",-1840116616).cljs$core$IFn$_invoke$arity$1(reply));
}
});})(s__38873__$1,votes_percent,rounded_votes_percent,reply_voted_QMARK_,reply,s__38873__$2,temp__5735__auto__,can_vote_QMARK_,user_voted_QMARK_,total_votes_count,map__38864,map__38864__$1,poll_data,poll_key,current_user_id))
:null), "className": "mlb-reset poll-reply"}),(function (){var attrs38877 = new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(reply);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs38877))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["poll-reply-body"], null)], null),attrs38877], 0))):({"className": "poll-reply-body"})),((cljs.core.map_QMARK_(attrs38877))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs38877)], null)));
})()),(function (){var attrs38876 = [cljs.core.str.cljs$core$IFn$_invoke$arity$1(rounded_votes_percent),"%"].join('');
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs38876))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["poll-reply-count"], null)], null),attrs38876], 0))):({"className": "poll-reply-count"})),((cljs.core.map_QMARK_(attrs38876))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs38876)], null)));
})()),oc$web$components$ui$poll$iter__38872(cljs.core.rest(s__38873__$2)));
} else {
var G__38930 = cljs.core.rest(s__38873__$2);
s__38873__$1 = G__38930;
continue;
}
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(oc.web.utils.poll.sorted_replies.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([poll_data], 0)));
})())),sablono.interpreter.interpret((cljs.core.truth_((function (){var or__4126__auto__ = (function (){var and__4115__auto__ = new cljs.core.Keyword(null,"can-add-reply","can-add-reply",-975368010).cljs$core$IFn$_invoke$arity$1(poll_data);
if(cljs.core.truth_(and__4115__auto__)){
return can_vote_QMARK_;
} else {
return and__4115__auto__;
}
})();
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"preview","preview",451279890).cljs$core$IFn$_invoke$arity$1(poll_data);
}
})())?(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.poll","adding-reply","oc.web.components.ui.poll/adding-reply",1883405595).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.poll-reply-new","div.poll-reply-new",1539769442),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.poll-reply.group","div.poll-reply.group",-921842812),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input.poll-reply-body","input.poll-reply-body",-2140446394),new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"type","type",1174270348),"text",new cljs.core.Keyword(null,"ref","ref",1289896967),new cljs.core.Keyword(null,"new-reply","new-reply",1851336688),new cljs.core.Keyword(null,"value","value",305978217),cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.poll","new-reply","oc.web.components.ui.poll/new-reply",-1856644677).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"max-length","max-length",-254826109),oc.web.utils.poll.max_reply_length,new cljs.core.Keyword(null,"placeholder","placeholder",-104873083),"Press Enter to add or Escape to cancel",new cljs.core.Keyword(null,"on-change","on-change",-732046149),(function (p1__38862_SHARP_){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.poll","new-reply","oc.web.components.ui.poll/new-reply",-1856644677).cljs$core$IFn$_invoke$arity$1(s),p1__38862_SHARP_.target.value);
}),new cljs.core.Keyword(null,"on-key-up","on-key-up",884441808),(function (e){
if(((((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(e.key,"Enter")) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(e.keyCode,(13))))) && (cljs.core.seq(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.poll","new-reply","oc.web.components.ui.poll/new-reply",-1856644677).cljs$core$IFn$_invoke$arity$1(s)))))){
oc.web.actions.poll.add_new_reply(poll_data,poll_key,cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.poll","new-reply","oc.web.components.ui.poll/new-reply",-1856644677).cljs$core$IFn$_invoke$arity$1(s)));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.poll","new-reply","oc.web.components.ui.poll/new-reply",-1856644677).cljs$core$IFn$_invoke$arity$1(s),"");

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.poll","adding-reply","oc.web.components.ui.poll/adding-reply",1883405595).cljs$core$IFn$_invoke$arity$1(s),false);
} else {
}

if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(e.key,"Escape")) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(e.keyCode,(27))))){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.poll","new-reply","oc.web.components.ui.poll/new-reply",-1856644677).cljs$core$IFn$_invoke$arity$1(s),"");

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.poll","adding-reply","oc.web.components.ui.poll/adding-reply",1883405595).cljs$core$IFn$_invoke$arity$1(s),false);
} else {
return null;
}
})], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.delete-reply.read-poll","button.mlb-reset.delete-reply.read-poll",-1508235387),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),"button",new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.poll","adding-reply","oc.web.components.ui.poll/adding-reply",1883405595).cljs$core$IFn$_invoke$arity$1(s),false);
})], null)], null)], null)], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.poll-reply-new","div.poll-reply-new",1539769442),(cljs.core.truth_(new cljs.core.Keyword(null,"can-add-reply","can-add-reply",-975368010).cljs$core$IFn$_invoke$arity$1(poll_data))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.poll-reply-new","button.mlb-reset.poll-reply-new",-1055090969),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),"button",new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (e){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.poll","adding-reply","oc.web.components.ui.poll/adding-reply",1883405595).cljs$core$IFn$_invoke$arity$1(s),true);

return oc.web.lib.utils.after((800),(function (){
var temp__5735__auto__ = rum.core.ref_node(s,new cljs.core.Keyword(null,"new-reply","new-reply",1851336688));
if(cljs.core.truth_(temp__5735__auto__)){
var new_input = temp__5735__auto__;
return new_input.focus();
} else {
return null;
}
}));
})], null),"Add option"], null):null),(cljs.core.truth_(new cljs.core.Keyword(null,"preview","preview",451279890).cljs$core$IFn$_invoke$arity$1(poll_data))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.poll-preview-bt","button.mlb-reset.poll-preview-bt",887355265),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),"button",new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.actions.poll.hide_preview(poll_key);
})], null),"Edit poll"], null):null)], null)):null)));
}),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$,rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.poll","animate","oc.web.components.ui.poll/animate",-1859864604)),rum.core.local.cljs$core$IFn$_invoke$arity$2("",new cljs.core.Keyword("oc.web.components.ui.poll","new-reply","oc.web.components.ui.poll/new-reply",-1856644677)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.poll","adding-reply","oc.web.components.ui.poll/adding-reply",1883405595)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
oc.web.lib.utils.after((180),(function (){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.poll","animate","oc.web.components.ui.poll/animate",-1859864604).cljs$core$IFn$_invoke$arity$1(s),true);
}));

return s;
})], null)], null),"poll-read");
oc.web.components.ui.poll.add_option = (function oc$web$components$ui$poll$add_option(s,poll_key,poll_data){
oc.web.actions.poll.add_reply(poll_key);

return oc.web.lib.utils.after((400),(function (){
var temp__5735__auto__ = rum.core.ref_node(s,["choice-",cljs.core.str.cljs$core$IFn$_invoke$arity$1((cljs.core.count(new cljs.core.Keyword(null,"replies","replies",-1389888974).cljs$core$IFn$_invoke$arity$1(poll_data)) + (1)))].join(''));
if(cljs.core.truth_(temp__5735__auto__)){
var last_option = temp__5735__auto__;
return last_option.focus();
} else {
return null;
}
}));
});
oc.web.components.ui.poll.poll_edit = rum.core.build_defcs((function (s,p__38884){
var map__38885 = p__38884;
var map__38885__$1 = (((((!((map__38885 == null))))?(((((map__38885.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38885.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38885):map__38885);
var props = map__38885__$1;
var poll_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38885__$1,new cljs.core.Keyword(null,"poll-data","poll-data",-172934296));
var poll_key = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38885__$1,new cljs.core.Keyword(null,"poll-key","poll-key",-992015670));
var current_user_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38885__$1,new cljs.core.Keyword(null,"current-user-id","current-user-id",-2013633451));
var should_show_delete_reply_QMARK_ = (cljs.core.count(new cljs.core.Keyword(null,"replies","replies",-1389888974).cljs$core$IFn$_invoke$arity$1(poll_data)) > oc.web.utils.poll.min_poll_replies);
var is_mobile_QMARK_ = oc.web.lib.responsive.is_mobile_size_QMARK_();
var tab_index_base = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.poll","tab-index-base","oc.web.components.ui.poll/tab-index-base",61583811).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(new cljs.core.Keyword(null,"preview","preview",451279890).cljs$core$IFn$_invoke$arity$1(poll_data))){
return sablono.interpreter.interpret((oc.web.components.ui.poll.poll_read.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.poll.poll_read.cljs$core$IFn$_invoke$arity$1(props) : oc.web.components.ui.poll.poll_read.call(null,props)));
} else {
return React.createElement("div",({"className": "poll"}),React.createElement("form",({"onSubmit": (function (p1__38881_SHARP_){
return oc.web.lib.utils.event_stop(p1__38881_SHARP_);
}), "action": "."}),React.createElement("button",({"type": "button", "onClick": (function (e){
var alert_data = new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"icon","icon",1679606541),"/img/ML/trash.svg",new cljs.core.Keyword(null,"action","action",-811238024),"delete-poll",new cljs.core.Keyword(null,"message","message",-406056002),"Are you sure you want to delete this poll? This can\u2019t be undone.",new cljs.core.Keyword(null,"link-button-title","link-button-title",-812305976),"Keep",new cljs.core.Keyword(null,"link-button-cb","link-button-cb",559710301),(function (){
return oc.web.components.ui.alert_modal.hide_alert();
}),new cljs.core.Keyword(null,"solid-button-style","solid-button-style",-723792244),new cljs.core.Keyword(null,"red","red",-969428204),new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"Yes",new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),(function (){
oc.web.actions.poll.remove_poll(oc.web.components.ui.poll.get_dispatch_key(poll_key),poll_data);

return oc.web.components.ui.alert_modal.hide_alert();
})], null);
return oc.web.components.ui.alert_modal.show_alert(alert_data);
}), "data-toggle": (cljs.core.truth_(is_mobile_QMARK_)?null:"tooltip"), "data-placement": "top", "data-container": "body", "title": "Remove poll", "className": "mlb-reset delete-poll-bt"})),React.createElement("div",({"className": "poll-question-label"}),"What would you like to ask?"),sablono.interpreter.create_element("input",({"type": "text", "ref": "question", "maxLength": oc.web.utils.poll.max_question_length, "tabIndex": tab_index_base, "placeholder": "Ask your question...", "value": new cljs.core.Keyword(null,"question","question",-1411720117).cljs$core$IFn$_invoke$arity$1(poll_data), "onChange": (function (p1__38882_SHARP_){
return oc.web.actions.poll.update_question(poll_key,poll_data,p1__38882_SHARP_.target.value);
}), "className": "poll-question"})),(function (){var idx = cljs.core.atom.cljs$core$IFn$_invoke$arity$1((0));
return cljs.core.into_array.cljs$core$IFn$_invoke$arity$1((function (){var iter__4529__auto__ = (function oc$web$components$ui$poll$iter__38892(s__38893){
return (new cljs.core.LazySeq(null,(function (){
var s__38893__$1 = s__38893;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__38893__$1);
if(temp__5735__auto__){
var s__38893__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__38893__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__38893__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__38895 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__38894 = (0);
while(true){
if((i__38894 < size__4528__auto__)){
var reply = cljs.core._nth(c__4527__auto__,i__38894);
var idx__$1 = cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(idx,cljs.core.inc);
var last_option_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(idx__$1,cljs.core.count(new cljs.core.Keyword(null,"replies","replies",-1389888974).cljs$core$IFn$_invoke$arity$1(poll_data)));
cljs.core.chunk_append(b__38895,React.createElement("div",({"key": ["poll-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"poll-uuid","poll-uuid",110210618).cljs$core$IFn$_invoke$arity$1(poll_data)),"-reply-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"reply-id","reply-id",-1840116616).cljs$core$IFn$_invoke$arity$1(reply))].join(''), "className": "poll-reply group"}),(function (){var attrs38896 = ["Choice ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(idx__$1)].join('');
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs38896))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["poll-reply-label"], null)], null),attrs38896], 0))):({"className": "poll-reply-label"})),((cljs.core.map_QMARK_(attrs38896))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs38896)], null)));
})(),sablono.interpreter.create_element("input",({"tabIndex": (tab_index_base + idx__$1), "placeholder": ["Choice ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(idx__$1)].join(''), "ref": ["choice-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(idx__$1)].join(''), "value": new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(reply), "type": "text", "className": "poll-reply-body", "onKeyPress": ((function (i__38894,idx__$1,last_option_QMARK_,reply,c__4527__auto__,size__4528__auto__,b__38895,s__38893__$2,temp__5735__auto__,idx,should_show_delete_reply_QMARK_,is_mobile_QMARK_,tab_index_base,map__38885,map__38885__$1,props,poll_data,poll_key,current_user_id){
return (function (e){
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(e.key,"Enter")) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(e.keyCode,(13))))){
return oc.web.lib.utils.event_stop(e);
} else {
return null;
}
});})(i__38894,idx__$1,last_option_QMARK_,reply,c__4527__auto__,size__4528__auto__,b__38895,s__38893__$2,temp__5735__auto__,idx,should_show_delete_reply_QMARK_,is_mobile_QMARK_,tab_index_base,map__38885,map__38885__$1,props,poll_data,poll_key,current_user_id))
, "maxLength": oc.web.utils.poll.max_reply_length, "onKeyDown": ((function (i__38894,idx__$1,last_option_QMARK_,reply,c__4527__auto__,size__4528__auto__,b__38895,s__38893__$2,temp__5735__auto__,idx,should_show_delete_reply_QMARK_,is_mobile_QMARK_,tab_index_base,map__38885,map__38885__$1,props,poll_data,poll_key,current_user_id){
return (function (e){
if(((last_option_QMARK_) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(e.keyCode,(9))) && (cljs.core.not(e.shiftKey)))){
oc.web.lib.utils.event_stop(e);

return oc.web.components.ui.poll.add_option(s,poll_key,poll_data);
} else {
return null;
}
});})(i__38894,idx__$1,last_option_QMARK_,reply,c__4527__auto__,size__4528__auto__,b__38895,s__38893__$2,temp__5735__auto__,idx,should_show_delete_reply_QMARK_,is_mobile_QMARK_,tab_index_base,map__38885,map__38885__$1,props,poll_data,poll_key,current_user_id))
, "onChange": ((function (i__38894,idx__$1,last_option_QMARK_,reply,c__4527__auto__,size__4528__auto__,b__38895,s__38893__$2,temp__5735__auto__,idx,should_show_delete_reply_QMARK_,is_mobile_QMARK_,tab_index_base,map__38885,map__38885__$1,props,poll_data,poll_key,current_user_id){
return (function (p1__38883_SHARP_){
return oc.web.actions.poll.update_reply(poll_key,new cljs.core.Keyword(null,"reply-id","reply-id",-1840116616).cljs$core$IFn$_invoke$arity$1(reply),p1__38883_SHARP_.target.value);
});})(i__38894,idx__$1,last_option_QMARK_,reply,c__4527__auto__,size__4528__auto__,b__38895,s__38893__$2,temp__5735__auto__,idx,should_show_delete_reply_QMARK_,is_mobile_QMARK_,tab_index_base,map__38885,map__38885__$1,props,poll_data,poll_key,current_user_id))
})),sablono.interpreter.interpret(((should_show_delete_reply_QMARK_)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.delete-reply","button.mlb-reset.delete-reply",947496119),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),"button",new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (i__38894,idx__$1,last_option_QMARK_,reply,c__4527__auto__,size__4528__auto__,b__38895,s__38893__$2,temp__5735__auto__,idx,should_show_delete_reply_QMARK_,is_mobile_QMARK_,tab_index_base,map__38885,map__38885__$1,props,poll_data,poll_key,current_user_id){
return (function (){
return oc.web.actions.poll.delete_reply(poll_key,new cljs.core.Keyword(null,"reply-id","reply-id",-1840116616).cljs$core$IFn$_invoke$arity$1(reply));
});})(i__38894,idx__$1,last_option_QMARK_,reply,c__4527__auto__,size__4528__auto__,b__38895,s__38893__$2,temp__5735__auto__,idx,should_show_delete_reply_QMARK_,is_mobile_QMARK_,tab_index_base,map__38885,map__38885__$1,props,poll_data,poll_key,current_user_id))
], null)], null):null))));

var G__38931 = (i__38894 + (1));
i__38894 = G__38931;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__38895),oc$web$components$ui$poll$iter__38892(cljs.core.chunk_rest(s__38893__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__38895),null);
}
} else {
var reply = cljs.core.first(s__38893__$2);
var idx__$1 = cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(idx,cljs.core.inc);
var last_option_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(idx__$1,cljs.core.count(new cljs.core.Keyword(null,"replies","replies",-1389888974).cljs$core$IFn$_invoke$arity$1(poll_data)));
return cljs.core.cons(React.createElement("div",({"key": ["poll-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"poll-uuid","poll-uuid",110210618).cljs$core$IFn$_invoke$arity$1(poll_data)),"-reply-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"reply-id","reply-id",-1840116616).cljs$core$IFn$_invoke$arity$1(reply))].join(''), "className": "poll-reply group"}),(function (){var attrs38896 = ["Choice ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(idx__$1)].join('');
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs38896))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["poll-reply-label"], null)], null),attrs38896], 0))):({"className": "poll-reply-label"})),((cljs.core.map_QMARK_(attrs38896))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs38896)], null)));
})(),sablono.interpreter.create_element("input",({"tabIndex": (tab_index_base + idx__$1), "placeholder": ["Choice ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(idx__$1)].join(''), "ref": ["choice-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(idx__$1)].join(''), "value": new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(reply), "type": "text", "className": "poll-reply-body", "onKeyPress": ((function (idx__$1,last_option_QMARK_,reply,s__38893__$2,temp__5735__auto__,idx,should_show_delete_reply_QMARK_,is_mobile_QMARK_,tab_index_base,map__38885,map__38885__$1,props,poll_data,poll_key,current_user_id){
return (function (e){
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(e.key,"Enter")) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(e.keyCode,(13))))){
return oc.web.lib.utils.event_stop(e);
} else {
return null;
}
});})(idx__$1,last_option_QMARK_,reply,s__38893__$2,temp__5735__auto__,idx,should_show_delete_reply_QMARK_,is_mobile_QMARK_,tab_index_base,map__38885,map__38885__$1,props,poll_data,poll_key,current_user_id))
, "maxLength": oc.web.utils.poll.max_reply_length, "onKeyDown": ((function (idx__$1,last_option_QMARK_,reply,s__38893__$2,temp__5735__auto__,idx,should_show_delete_reply_QMARK_,is_mobile_QMARK_,tab_index_base,map__38885,map__38885__$1,props,poll_data,poll_key,current_user_id){
return (function (e){
if(((last_option_QMARK_) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(e.keyCode,(9))) && (cljs.core.not(e.shiftKey)))){
oc.web.lib.utils.event_stop(e);

return oc.web.components.ui.poll.add_option(s,poll_key,poll_data);
} else {
return null;
}
});})(idx__$1,last_option_QMARK_,reply,s__38893__$2,temp__5735__auto__,idx,should_show_delete_reply_QMARK_,is_mobile_QMARK_,tab_index_base,map__38885,map__38885__$1,props,poll_data,poll_key,current_user_id))
, "onChange": ((function (idx__$1,last_option_QMARK_,reply,s__38893__$2,temp__5735__auto__,idx,should_show_delete_reply_QMARK_,is_mobile_QMARK_,tab_index_base,map__38885,map__38885__$1,props,poll_data,poll_key,current_user_id){
return (function (p1__38883_SHARP_){
return oc.web.actions.poll.update_reply(poll_key,new cljs.core.Keyword(null,"reply-id","reply-id",-1840116616).cljs$core$IFn$_invoke$arity$1(reply),p1__38883_SHARP_.target.value);
});})(idx__$1,last_option_QMARK_,reply,s__38893__$2,temp__5735__auto__,idx,should_show_delete_reply_QMARK_,is_mobile_QMARK_,tab_index_base,map__38885,map__38885__$1,props,poll_data,poll_key,current_user_id))
})),sablono.interpreter.interpret(((should_show_delete_reply_QMARK_)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.delete-reply","button.mlb-reset.delete-reply",947496119),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),"button",new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (idx__$1,last_option_QMARK_,reply,s__38893__$2,temp__5735__auto__,idx,should_show_delete_reply_QMARK_,is_mobile_QMARK_,tab_index_base,map__38885,map__38885__$1,props,poll_data,poll_key,current_user_id){
return (function (){
return oc.web.actions.poll.delete_reply(poll_key,new cljs.core.Keyword(null,"reply-id","reply-id",-1840116616).cljs$core$IFn$_invoke$arity$1(reply));
});})(idx__$1,last_option_QMARK_,reply,s__38893__$2,temp__5735__auto__,idx,should_show_delete_reply_QMARK_,is_mobile_QMARK_,tab_index_base,map__38885,map__38885__$1,props,poll_data,poll_key,current_user_id))
], null)], null):null))),oc$web$components$ui$poll$iter__38892(cljs.core.rest(s__38893__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(oc.web.utils.poll.sorted_replies.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([poll_data], 0)));
})());
})()),React.createElement("div",({"className": "poll-reply-new"}),React.createElement("button",({"type": "button", "onClick": cljs.core.partial.cljs$core$IFn$_invoke$arity$4(oc.web.components.ui.poll.add_option,s,poll_key,poll_data), "className": "mlb-reset poll-reply-new"}),"Add option"),React.createElement("button",({"type": "button", "onClick": (function (){
return oc.web.actions.poll.show_preview(poll_key);
}), "className": "mlb-reset poll-preview-bt"}),"Show preview")));
}
}),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$,rum.core.local.cljs$core$IFn$_invoke$arity$2("",new cljs.core.Keyword("oc.web.components.ui.poll","new-reply","oc.web.components.ui.poll/new-reply",-1856644677)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.ui.poll","tab-index-base","oc.web.components.ui.poll/tab-index-base",61583811)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.poll","tab-index-base","oc.web.components.ui.poll/tab-index-base",61583811).cljs$core$IFn$_invoke$arity$1(s),(cljs.core.rand.cljs$core$IFn$_invoke$arity$1((1000)) | (0)));

return s;
}),new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
var temp__5735__auto___38932 = rum.core.ref_node(s,new cljs.core.Keyword(null,"question","question",-1411720117));
if(cljs.core.truth_(temp__5735__auto___38932)){
var q_el_38933 = temp__5735__auto___38932;
q_el_38933.focus();
} else {
}

return s;
})], null)], null),"poll-edit");
oc.web.components.ui.poll.poll = rum.core.build_defcs((function (s,p__38905){
var map__38906 = p__38905;
var map__38906__$1 = (((((!((map__38906 == null))))?(((((map__38906.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38906.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38906):map__38906);
var props = map__38906__$1;
var editing_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38906__$1,new cljs.core.Keyword(null,"editing?","editing?",1646440800));
var poll_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38906__$1,new cljs.core.Keyword(null,"poll-data","poll-data",-172934296));
return React.createElement("div",({"key": ["oc-poll-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"poll-uuid","poll-uuid",110210618).cljs$core$IFn$_invoke$arity$1(poll_data))].join(''), "contentEditable": false, "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["oc-poll-container",["oc-poll-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"poll-uuid","poll-uuid",110210618).cljs$core$IFn$_invoke$arity$1(poll_data))].join('')], null))}),(cljs.core.truth_(editing_QMARK_)?sablono.interpreter.interpret((oc.web.components.ui.poll.poll_edit.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.poll.poll_edit.cljs$core$IFn$_invoke$arity$1(props) : oc.web.components.ui.poll.poll_edit.call(null,props))):sablono.interpreter.interpret((oc.web.components.ui.poll.poll_read.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.poll.poll_read.cljs$core$IFn$_invoke$arity$1(props) : oc.web.components.ui.poll.poll_read.call(null,props)))));
}),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
var args_38934 = cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s));
$([cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"container-selector","container-selector",6506114).cljs$core$IFn$_invoke$arity$1(args_38934))," .oc-poll-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"poll-uuid","poll-uuid",110210618).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"poll-data","poll-data",-172934296).cljs$core$IFn$_invoke$arity$1(args_38934)))].join('')).remove();

return s;
})], null)], null),"poll");
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.components !== 'undefined') && (typeof oc.web.components.ui !== 'undefined') && (typeof oc.web.components.ui.poll !== 'undefined') && (typeof oc.web.components.ui.poll.max_mount_retry !== 'undefined')){
} else {
oc.web.components.ui.poll.max_mount_retry = (5);
}
oc.web.components.ui.poll.poll_portal = rum.core.build_defcs((function (s,p__38910){
var map__38911 = p__38910;
var map__38911__$1 = (((((!((map__38911 == null))))?(((((map__38911.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38911.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38911):map__38911);
var props = map__38911__$1;
var poll_portal_selector = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38911__$1,new cljs.core.Keyword(null,"poll-portal-selector","poll-portal-selector",1758674772));
var poll_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38911__$1,new cljs.core.Keyword(null,"poll-data","poll-data",-172934296));
var poll_key = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38911__$1,new cljs.core.Keyword(null,"poll-key","poll-key",-992015670));
var container_selector = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38911__$1,new cljs.core.Keyword(null,"container-selector","container-selector",6506114));
var activity_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38911__$1,new cljs.core.Keyword(null,"activity-data","activity-data",1293689136));
return sablono.interpreter.interpret((function (){var temp__5735__auto__ = document.querySelector(dommy.core.selector(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [container_selector,poll_portal_selector], null)));
if(cljs.core.truth_(temp__5735__auto__)){
var portal_element = temp__5735__auto__;
return rum.core.portal((oc.web.components.ui.poll.poll.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.poll.poll.cljs$core$IFn$_invoke$arity$1(props) : oc.web.components.ui.poll.poll.call(null,props)),portal_element);
} else {
return null;
}
})());
}),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$,rum.core.local.cljs$core$IFn$_invoke$arity$2((0),new cljs.core.Keyword("oc.web.components.ui.poll","retry","oc.web.components.ui.poll/retry",-1230267919)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.poll","mounted","oc.web.components.ui.poll/mounted",478705549)),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.poll","mounted","oc.web.components.ui.poll/mounted",478705549).cljs$core$IFn$_invoke$arity$1(s),true);

return s;
}),new cljs.core.Keyword(null,"after-render","after-render",1997533433),(function (s){
var retry_38935 = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.poll","retry","oc.web.components.ui.poll/retry",-1230267919).cljs$core$IFn$_invoke$arity$1(s));
oc.web.lib.utils.after(((180) * (retry_38935 + (1))),(function (){
var retry_QMARK_ = (cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.poll","mounted","oc.web.components.ui.poll/mounted",478705549).cljs$core$IFn$_invoke$arity$1(s)))?cljs.core.not(rum.core.dom_node(s)):null);
if(cljs.core.truth_((function (){var and__4115__auto__ = retry_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return (retry_38935 < oc.web.components.ui.poll.max_mount_retry);
} else {
return and__4115__auto__;
}
})())){
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("oc.web.components.ui.poll","retry","oc.web.components.ui.poll/retry",-1230267919).cljs$core$IFn$_invoke$arity$1(s),cljs.core.inc);
} else {
}

if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(retry_38935,oc.web.components.ui.poll.max_mount_retry)){
var props = cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s));
return oc.web.utils.poll.report_unmounted_poll(props);
} else {
return null;
}
}));

return s;
}),new cljs.core.Keyword(null,"did-remount","did-remount",1362550500),(function (_,s){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.poll","retry","oc.web.components.ui.poll/retry",-1230267919).cljs$core$IFn$_invoke$arity$1(s),(0));

return s;
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.poll","mounted","oc.web.components.ui.poll/mounted",478705549).cljs$core$IFn$_invoke$arity$1(s),false);

return s;
})], null)], null),"poll-portal");
oc.web.components.ui.poll.polls_wrapper = rum.core.build_defc((function (p__38913){
var map__38914 = p__38913;
var map__38914__$1 = (((((!((map__38914 == null))))?(((((map__38914.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38914.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38914):map__38914);
var polls_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38914__$1,new cljs.core.Keyword(null,"polls-data","polls-data",-975205110));
var editing_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38914__$1,new cljs.core.Keyword(null,"editing?","editing?",1646440800));
var dispatch_key = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38914__$1,new cljs.core.Keyword(null,"dispatch-key","dispatch-key",733619510));
var current_user_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38914__$1,new cljs.core.Keyword(null,"current-user-id","current-user-id",-2013633451));
var container_selector = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38914__$1,new cljs.core.Keyword(null,"container-selector","container-selector",6506114));
var activity_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38914__$1,new cljs.core.Keyword(null,"activity-data","activity-data",1293689136));
return cljs.core.into_array.cljs$core$IFn$_invoke$arity$1((function (){var iter__4529__auto__ = (function oc$web$components$ui$poll$iter__38916(s__38917){
return (new cljs.core.LazySeq(null,(function (){
var s__38917__$1 = s__38917;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__38917__$1);
if(temp__5735__auto__){
var s__38917__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__38917__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__38917__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__38919 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__38918 = (0);
while(true){
if((i__38918 < size__4528__auto__)){
var vec__38920 = cljs.core._nth(c__4527__auto__,i__38918);
var poll_uuid_k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38920,(0),null);
var poll = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38920,(1),null);
var poll_uuid = cljs.core.name(poll_uuid_k);
var poll_selector = [".",oc.web.utils.poll.poll_selector_prefix,poll_uuid].join('');
cljs.core.chunk_append(b__38919,sablono.interpreter.interpret(rum.core.with_key((function (){var G__38923 = new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"poll-data","poll-data",-172934296),poll,new cljs.core.Keyword(null,"editing?","editing?",1646440800),editing_QMARK_,new cljs.core.Keyword(null,"container-selector","container-selector",6506114),container_selector,new cljs.core.Keyword(null,"current-user-id","current-user-id",-2013633451),current_user_id,new cljs.core.Keyword(null,"poll-key","poll-key",-992015670),cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(((cljs.core.coll_QMARK_(dispatch_key))?dispatch_key:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [dispatch_key], null)),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"polls","polls",-580623582),poll_uuid_k], null))),new cljs.core.Keyword(null,"poll-portal-selector","poll-portal-selector",1758674772),poll_selector,new cljs.core.Keyword(null,"activity-data","activity-data",1293689136),activity_data], null);
return (oc.web.components.ui.poll.poll_portal.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.poll.poll_portal.cljs$core$IFn$_invoke$arity$1(G__38923) : oc.web.components.ui.poll.poll_portal.call(null,G__38923));
})(),[oc.web.utils.poll.poll_selector_prefix,poll_uuid].join(''))));

var G__38936 = (i__38918 + (1));
i__38918 = G__38936;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__38919),oc$web$components$ui$poll$iter__38916(cljs.core.chunk_rest(s__38917__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__38919),null);
}
} else {
var vec__38924 = cljs.core.first(s__38917__$2);
var poll_uuid_k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38924,(0),null);
var poll = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38924,(1),null);
var poll_uuid = cljs.core.name(poll_uuid_k);
var poll_selector = [".",oc.web.utils.poll.poll_selector_prefix,poll_uuid].join('');
return cljs.core.cons(sablono.interpreter.interpret(rum.core.with_key((function (){var G__38927 = new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"poll-data","poll-data",-172934296),poll,new cljs.core.Keyword(null,"editing?","editing?",1646440800),editing_QMARK_,new cljs.core.Keyword(null,"container-selector","container-selector",6506114),container_selector,new cljs.core.Keyword(null,"current-user-id","current-user-id",-2013633451),current_user_id,new cljs.core.Keyword(null,"poll-key","poll-key",-992015670),cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(((cljs.core.coll_QMARK_(dispatch_key))?dispatch_key:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [dispatch_key], null)),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"polls","polls",-580623582),poll_uuid_k], null))),new cljs.core.Keyword(null,"poll-portal-selector","poll-portal-selector",1758674772),poll_selector,new cljs.core.Keyword(null,"activity-data","activity-data",1293689136),activity_data], null);
return (oc.web.components.ui.poll.poll_portal.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.poll.poll_portal.cljs$core$IFn$_invoke$arity$1(G__38927) : oc.web.components.ui.poll.poll_portal.call(null,G__38927));
})(),[oc.web.utils.poll.poll_selector_prefix,poll_uuid].join(''))),oc$web$components$ui$poll$iter__38916(cljs.core.rest(s__38917__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(polls_data);
})());
}),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$], null),"polls-wrapper");

//# sourceMappingURL=oc.web.components.ui.poll.js.map
