goog.provide('oc.web.components.ui.wrt');
oc.web.components.ui.wrt.filter_by_query = (function oc$web$components$ui$wrt$filter_by_query(user,query){
var complete_name = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(user);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"first-name","first-name",-1559982131).cljs$core$IFn$_invoke$arity$1(user))," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"last-name","last-name",-1695738974).cljs$core$IFn$_invoke$arity$1(user))].join('');
}
})();
var or__4126__auto__ = cuerdas.core.includes_QMARK_(cuerdas.core.lower(new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(user)),query);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = cuerdas.core.includes_QMARK_(cuerdas.core.lower(complete_name),query);
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
var or__4126__auto____$2 = cuerdas.core.includes_QMARK_(cuerdas.core.lower(new cljs.core.Keyword(null,"first-name","first-name",-1559982131).cljs$core$IFn$_invoke$arity$1(user)),query);
if(cljs.core.truth_(or__4126__auto____$2)){
return or__4126__auto____$2;
} else {
return cuerdas.core.includes_QMARK_(cuerdas.core.lower(new cljs.core.Keyword(null,"last-name","last-name",-1695738974).cljs$core$IFn$_invoke$arity$1(user)),query);
}
}
}
});
oc.web.components.ui.wrt.reset_search = (function oc$web$components$ui$wrt$reset_search(s){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.wrt","search-active","oc.web.components.ui.wrt/search-active",571990445).cljs$core$IFn$_invoke$arity$1(s),false);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.wrt","query","oc.web.components.ui.wrt/query",-624128005).cljs$core$IFn$_invoke$arity$1(s),"");

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.wrt","search-focused","oc.web.components.ui.wrt/search-focused",-1468987657).cljs$core$IFn$_invoke$arity$1(s),false);
});
oc.web.components.ui.wrt.sort_users = (function oc$web$components$ui$wrt$sort_users(user_id,users){
var map__45762 = cljs.core.group_by((function (p1__45761_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__45761_SHARP_),user_id)){
return new cljs.core.Keyword(null,"self-user","self-user",999064184);
} else {
return new cljs.core.Keyword(null,"other-users","other-users",1073979007);
}
}),users);
var map__45762__$1 = (((((!((map__45762 == null))))?(((((map__45762.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45762.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45762):map__45762);
var self_user = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45762__$1,new cljs.core.Keyword(null,"self-user","self-user",999064184));
var other_users = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45762__$1,new cljs.core.Keyword(null,"other-users","other-users",1073979007));
var sorted_other_users = cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(oc.lib.user.name_for,other_users);
return cljs.core.vec(cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,cljs.core.concat.cljs$core$IFn$_invoke$arity$2(self_user,sorted_other_users)));
});
oc.web.components.ui.wrt.dropdown_label = (function oc$web$components$ui$wrt$dropdown_label(val,total){
var G__45769 = val;
var G__45769__$1 = (((G__45769 instanceof cljs.core.Keyword))?G__45769.fqn:null);
switch (G__45769__$1) {
case "all":
return ["Everyone (",cljs.core.str.cljs$core$IFn$_invoke$arity$1(total),")"].join('');

break;
case "seen":
return "Viewed";

break;
case "unseen":
return "Unopened";

break;
default:
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__45769__$1)].join('')));

}
});
oc.web.components.ui.wrt.remind_to = (function oc$web$components$ui$wrt$remind_to(s,p__45771){
var map__45772 = p__45771;
var map__45772__$1 = (((((!((map__45772 == null))))?(((((map__45772.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45772.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45772):map__45772);
var current_user_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45772__$1,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915));
var activity_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45772__$1,new cljs.core.Keyword(null,"activity-data","activity-data",1293689136));
var users_list = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45772__$1,new cljs.core.Keyword(null,"users-list","users-list",1881920809));
var slack_bot_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45772__$1,new cljs.core.Keyword(null,"slack-bot-data","slack-bot-data",1066147116));
var wrt_share_base = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"note","note",1426297904),"When you have a moment, please check out this post.",new cljs.core.Keyword(null,"subject","subject",-1411880451),["You may have missed: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"headline","headline",-157157727).cljs$core$IFn$_invoke$arity$1(activity_data))].join('')], null);
var wrt_share = cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.merge,(function (){var iter__4529__auto__ = (function oc$web$components$ui$wrt$remind_to_$_iter__45774(s__45775){
return (new cljs.core.LazySeq(null,(function (){
var s__45775__$1 = s__45775;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__45775__$1);
if(temp__5735__auto__){
var s__45775__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__45775__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__45775__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__45777 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__45776 = (0);
while(true){
if((i__45776 < size__4528__auto__)){
var u = cljs.core._nth(c__4527__auto__,i__45776);
if(((cljs.core.not(new cljs.core.Keyword(null,"seen","seen",-518999789).cljs$core$IFn$_invoke$arity$1(u))) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(current_user_data),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(u))) && (cljs.core.not(cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.wrt","sending-notice","oc.web.components.ui.wrt/sending-notice",1071997023).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(u)))))){
var slack_user = cljs.core.get.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slack-users","slack-users",-434149941).cljs$core$IFn$_invoke$arity$1(u),cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(slack_bot_data)));
cljs.core.chunk_append(b__45777,cljs.core.PersistentArrayMap.createAsIfByAssoc([new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(u),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([wrt_share_base,(cljs.core.truth_((function (){var and__4115__auto__ = slack_user;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"notification-medium","notification-medium",195200470).cljs$core$IFn$_invoke$arity$1(u),"slack");
} else {
return and__4115__auto__;
}
})())?new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"medium","medium",-1864319384),"slack",new cljs.core.Keyword(null,"channel","channel",734187692),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561),new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(slack_user),new cljs.core.Keyword(null,"channel-id","channel-id",138191095),new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(slack_user),new cljs.core.Keyword(null,"channel-name","channel-name",-188505362),"Wut",new cljs.core.Keyword(null,"type","type",1174270348),"user"], null)], null):new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"medium","medium",-1864319384),"email",new cljs.core.Keyword(null,"to","to",192099007),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(u)], null)], null))], 0))]));

var G__45837 = (i__45776 + (1));
i__45776 = G__45837;
continue;
} else {
var G__45838 = (i__45776 + (1));
i__45776 = G__45838;
continue;
}
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__45777),oc$web$components$ui$wrt$remind_to_$_iter__45774(cljs.core.chunk_rest(s__45775__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__45777),null);
}
} else {
var u = cljs.core.first(s__45775__$2);
if(((cljs.core.not(new cljs.core.Keyword(null,"seen","seen",-518999789).cljs$core$IFn$_invoke$arity$1(u))) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(current_user_data),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(u))) && (cljs.core.not(cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.wrt","sending-notice","oc.web.components.ui.wrt/sending-notice",1071997023).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(u)))))){
var slack_user = cljs.core.get.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slack-users","slack-users",-434149941).cljs$core$IFn$_invoke$arity$1(u),cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(slack_bot_data)));
return cljs.core.cons(cljs.core.PersistentArrayMap.createAsIfByAssoc([new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(u),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([wrt_share_base,(cljs.core.truth_((function (){var and__4115__auto__ = slack_user;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"notification-medium","notification-medium",195200470).cljs$core$IFn$_invoke$arity$1(u),"slack");
} else {
return and__4115__auto__;
}
})())?new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"medium","medium",-1864319384),"slack",new cljs.core.Keyword(null,"channel","channel",734187692),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561),new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(slack_user),new cljs.core.Keyword(null,"channel-id","channel-id",138191095),new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(slack_user),new cljs.core.Keyword(null,"channel-name","channel-name",-188505362),"Wut",new cljs.core.Keyword(null,"type","type",1174270348),"user"], null)], null):new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"medium","medium",-1864319384),"email",new cljs.core.Keyword(null,"to","to",192099007),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(u)], null)], null))], 0))]),oc$web$components$ui$wrt$remind_to_$_iter__45774(cljs.core.rest(s__45775__$2)));
} else {
var G__45842 = cljs.core.rest(s__45775__$2);
s__45775__$1 = G__45842;
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
return iter__4529__auto__(users_list);
})());
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(new cljs.core.Keyword("oc.web.components.ui.wrt","sending-notice","oc.web.components.ui.wrt/sending-notice",1071997023).cljs$core$IFn$_invoke$arity$1(s),cljs.core.merge,cljs.core.zipmap(cljs.core.keys(wrt_share),cljs.core.repeat.cljs$core$IFn$_invoke$arity$2(cljs.core.count(wrt_share),new cljs.core.Keyword(null,"loading","loading",-737050189))));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.wrt","show-remind-all-bt","oc.web.components.ui.wrt/show-remind-all-bt",445715702).cljs$core$IFn$_invoke$arity$1(s),false);

return oc.web.actions.activity.activity_share.cljs$core$IFn$_invoke$arity$variadic(activity_data,cljs.core.vals(wrt_share),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(function (p__45781){
var map__45782 = p__45781;
var map__45782__$1 = (((((!((map__45782 == null))))?(((((map__45782.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45782.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45782):map__45782);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45782__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45782__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.wrt","show-remind-all-bt","oc.web.components.ui.wrt/show-remind-all-bt",445715702).cljs$core$IFn$_invoke$arity$1(s),true);

if(cljs.core.truth_(success)){
var noticed_users = cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.merge,(function (){var iter__4529__auto__ = (function oc$web$components$ui$wrt$remind_to_$_iter__45784(s__45785){
return (new cljs.core.LazySeq(null,(function (){
var s__45785__$1 = s__45785;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__45785__$1);
if(temp__5735__auto__){
var s__45785__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__45785__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__45785__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__45787 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__45786 = (0);
while(true){
if((i__45786 < size__4528__auto__)){
var user_id = cljs.core._nth(c__4527__auto__,i__45786);
var u = cljs.core.some(((function (i__45786,user_id,c__4527__auto__,size__4528__auto__,b__45787,s__45785__$2,temp__5735__auto__,map__45782,map__45782__$1,success,body,wrt_share_base,wrt_share,map__45772,map__45772__$1,current_user_data,activity_data,users_list,slack_bot_data){
return (function (p1__45770_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__45770_SHARP_),user_id)){
return p1__45770_SHARP_;
} else {
return null;
}
});})(i__45786,user_id,c__4527__auto__,size__4528__auto__,b__45787,s__45785__$2,temp__5735__auto__,map__45782,map__45782__$1,success,body,wrt_share_base,wrt_share,map__45772,map__45772__$1,current_user_data,activity_data,users_list,slack_bot_data))
,users_list);
var slack_user = cljs.core.get.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slack-users","slack-users",-434149941).cljs$core$IFn$_invoke$arity$1(u),cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(slack_bot_data)));
cljs.core.chunk_append(b__45787,cljs.core.PersistentArrayMap.createAsIfByAssoc([new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(u),(cljs.core.truth_((function (){var and__4115__auto__ = slack_user;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"notification-medium","notification-medium",195200470).cljs$core$IFn$_invoke$arity$1(u),"slack");
} else {
return and__4115__auto__;
}
})())?(cljs.core.truth_((function (){var and__4115__auto__ = slack_user;
if(cljs.core.truth_(and__4115__auto__)){
return ((cljs.core.seq(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user))) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user),"-")));
} else {
return and__4115__auto__;
}
})())?["Sent to: @",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user))," (Slack)"].join(''):"Sent via Slack"):["Sent to: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(u))].join(''))]));

var G__45843 = (i__45786 + (1));
i__45786 = G__45843;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__45787),oc$web$components$ui$wrt$remind_to_$_iter__45784(cljs.core.chunk_rest(s__45785__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__45787),null);
}
} else {
var user_id = cljs.core.first(s__45785__$2);
var u = cljs.core.some(((function (user_id,s__45785__$2,temp__5735__auto__,map__45782,map__45782__$1,success,body,wrt_share_base,wrt_share,map__45772,map__45772__$1,current_user_data,activity_data,users_list,slack_bot_data){
return (function (p1__45770_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__45770_SHARP_),user_id)){
return p1__45770_SHARP_;
} else {
return null;
}
});})(user_id,s__45785__$2,temp__5735__auto__,map__45782,map__45782__$1,success,body,wrt_share_base,wrt_share,map__45772,map__45772__$1,current_user_data,activity_data,users_list,slack_bot_data))
,users_list);
var slack_user = cljs.core.get.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slack-users","slack-users",-434149941).cljs$core$IFn$_invoke$arity$1(u),cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(slack_bot_data)));
return cljs.core.cons(cljs.core.PersistentArrayMap.createAsIfByAssoc([new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(u),(cljs.core.truth_((function (){var and__4115__auto__ = slack_user;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"notification-medium","notification-medium",195200470).cljs$core$IFn$_invoke$arity$1(u),"slack");
} else {
return and__4115__auto__;
}
})())?(cljs.core.truth_((function (){var and__4115__auto__ = slack_user;
if(cljs.core.truth_(and__4115__auto__)){
return ((cljs.core.seq(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user))) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user),"-")));
} else {
return and__4115__auto__;
}
})())?["Sent to: @",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(slack_user))," (Slack)"].join(''):"Sent via Slack"):["Sent to: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(u))].join(''))]),oc$web$components$ui$wrt$remind_to_$_iter__45784(cljs.core.rest(s__45785__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(cljs.core.keys(wrt_share));
})());
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(new cljs.core.Keyword("oc.web.components.ui.wrt","sending-notice","oc.web.components.ui.wrt/sending-notice",1071997023).cljs$core$IFn$_invoke$arity$1(s),cljs.core.merge,noticed_users);
} else {
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(new cljs.core.Keyword("oc.web.components.ui.wrt","sending-notice","oc.web.components.ui.wrt/sending-notice",1071997023).cljs$core$IFn$_invoke$arity$1(s),cljs.core.merge,cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.merge,(function (){var iter__4529__auto__ = (function oc$web$components$ui$wrt$remind_to_$_iter__45789(s__45790){
return (new cljs.core.LazySeq(null,(function (){
var s__45790__$1 = s__45790;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__45790__$1);
if(temp__5735__auto__){
var s__45790__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__45790__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__45790__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__45792 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__45791 = (0);
while(true){
if((i__45791 < size__4528__auto__)){
var user_id = cljs.core._nth(c__4527__auto__,i__45791);
cljs.core.chunk_append(b__45792,(function (){
oc.web.lib.utils.after((5000),((function (i__45791,user_id,c__4527__auto__,size__4528__auto__,b__45792,s__45790__$2,temp__5735__auto__,map__45782,map__45782__$1,success,body,wrt_share_base,wrt_share,map__45772,map__45772__$1,current_user_data,activity_data,users_list,slack_bot_data){
return (function (){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(new cljs.core.Keyword("oc.web.components.ui.wrt","sending-notice","oc.web.components.ui.wrt/sending-notice",1071997023).cljs$core$IFn$_invoke$arity$1(s),cljs.core.dissoc,user_id);
});})(i__45791,user_id,c__4527__auto__,size__4528__auto__,b__45792,s__45790__$2,temp__5735__auto__,map__45782,map__45782__$1,success,body,wrt_share_base,wrt_share,map__45772,map__45772__$1,current_user_data,activity_data,users_list,slack_bot_data))
);

return cljs.core.PersistentArrayMap.createAsIfByAssoc([user_id,"An error occurred, please retry..."]);
})()
);

var G__45844 = (i__45791 + (1));
i__45791 = G__45844;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__45792),oc$web$components$ui$wrt$remind_to_$_iter__45789(cljs.core.chunk_rest(s__45790__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__45792),null);
}
} else {
var user_id = cljs.core.first(s__45790__$2);
return cljs.core.cons((function (){
oc.web.lib.utils.after((5000),((function (user_id,s__45790__$2,temp__5735__auto__,map__45782,map__45782__$1,success,body,wrt_share_base,wrt_share,map__45772,map__45772__$1,current_user_data,activity_data,users_list,slack_bot_data){
return (function (){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(new cljs.core.Keyword("oc.web.components.ui.wrt","sending-notice","oc.web.components.ui.wrt/sending-notice",1071997023).cljs$core$IFn$_invoke$arity$1(s),cljs.core.dissoc,user_id);
});})(user_id,s__45790__$2,temp__5735__auto__,map__45782,map__45782__$1,success,body,wrt_share_base,wrt_share,map__45772,map__45772__$1,current_user_data,activity_data,users_list,slack_bot_data))
);

return cljs.core.PersistentArrayMap.createAsIfByAssoc([user_id,"An error occurred, please retry..."]);
})()
,oc$web$components$ui$wrt$remind_to_$_iter__45789(cljs.core.rest(s__45790__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(cljs.core.keys(wrt_share));
})()));
}
})], 0));
});
oc.web.components.ui.wrt.remind_to_all = (function oc$web$components$ui$wrt$remind_to_all(s,p__45798){
var map__45799 = p__45798;
var map__45799__$1 = (((((!((map__45799 == null))))?(((((map__45799.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45799.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45799):map__45799);
var props = map__45799__$1;
var unopened_count = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45799__$1,new cljs.core.Keyword(null,"unopened-count","unopened-count",-1677253664));
var alert_data = new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"emoji-icon","emoji-icon",99205896),"\uD83D\uDC4C",new cljs.core.Keyword(null,"action","action",-811238024),"remind-all-unseens",new cljs.core.Keyword(null,"message","message",-406056002),["Do you want to send a reminder to everyone that hasn\u2019t opened it? (",cljs.core.str.cljs$core$IFn$_invoke$arity$1(unopened_count)," users)"].join(''),new cljs.core.Keyword(null,"link-button-title","link-button-title",-812305976),"No",new cljs.core.Keyword(null,"link-button-cb","link-button-cb",559710301),(function (){
return oc.web.components.ui.alert_modal.hide_alert();
}),new cljs.core.Keyword(null,"solid-button-style","solid-button-style",-723792244),new cljs.core.Keyword(null,"red","red",-969428204),new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"Yes, remind them",new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),(function (){
oc.web.components.ui.wrt.remind_to(s,props);

return oc.web.components.ui.alert_modal.hide_alert();
})], null);
return oc.web.components.ui.alert_modal.show_alert(alert_data);
});
oc.web.components.ui.wrt.wrt = rum.core.build_defcs((function (s,org_data){
var activity_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"wrt-activity-data","wrt-activity-data",-1368150621));
var current_user_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915));
var current_org_slug = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"org-slug","org-slug",-726595051));
var is_author_QMARK_ = new cljs.core.Keyword(null,"publisher?","publisher?",30448149).cljs$core$IFn$_invoke$arity$1(activity_data);
var read_data_STAR_ = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"wrt-read-data","wrt-read-data",-1241718247));
var read_data = (function (){var rd = read_data_STAR_;
var rd__$1 = cljs.core.update.cljs$core$IFn$_invoke$arity$3(rd,new cljs.core.Keyword(null,"unreads","unreads",-329347545),(function (unreads){
if(cljs.core.truth_(is_author_QMARK_)){
return cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__45801_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__45801_SHARP_),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(current_user_data));
}),unreads);
} else {
return unreads;
}
}));
return cljs.core.update.cljs$core$IFn$_invoke$arity$3(rd__$1,new cljs.core.Keyword(null,"reads","reads",-1215067361),(function (reads){
if(cljs.core.truth_(is_author_QMARK_)){
return cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__45802_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__45802_SHARP_),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(current_user_data));
}),reads);
} else {
return reads;
}
}));
})();
var item_id = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data);
var seen_users = cljs.core.vec(cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(oc.lib.user.name_for,new cljs.core.Keyword(null,"reads","reads",-1215067361).cljs$core$IFn$_invoke$arity$1(read_data)));
var seen_ids = cljs.core.set(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291),seen_users));
var unseen_users = cljs.core.vec(cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(oc.lib.user.name_for,new cljs.core.Keyword(null,"unreads","unreads",-329347545).cljs$core$IFn$_invoke$arity$1(read_data)));
var all_users = cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(oc.lib.user.name_for,cljs.core.concat.cljs$core$IFn$_invoke$arity$2(seen_users,unseen_users));
var read_count = new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(read_data);
var query = new cljs.core.Keyword("oc.web.components.ui.wrt","query","oc.web.components.ui.wrt/query",-624128005).cljs$core$IFn$_invoke$arity$1(s);
var lower_query = cuerdas.core.lower(cljs.core.deref(query));
var list_view = new cljs.core.Keyword("oc.web.components.ui.wrt","list-view","oc.web.components.ui.wrt/list-view",318473816).cljs$core$IFn$_invoke$arity$1(s);
var filtered_users = (function (){var G__45810 = cljs.core.deref(list_view);
var G__45810__$1 = (((G__45810 instanceof cljs.core.Keyword))?G__45810.fqn:null);
switch (G__45810__$1) {
case "all":
return cljs.core.filterv((function (p1__45803_SHARP_){
return oc.web.components.ui.wrt.filter_by_query(p1__45803_SHARP_,cuerdas.core.lower((function (){var or__4126__auto__ = cljs.core.deref(query);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "";
}
})()));
}),all_users);

break;
case "seen":
return seen_users;

break;
case "unseen":
return unseen_users;

break;
default:
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__45810__$1)].join('')));

}
})();
var sorted_filtered_users = oc.web.components.ui.wrt.sort_users(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(current_user_data),filtered_users);
var is_mobile_QMARK_ = oc.web.lib.responsive.is_tablet_or_mobile_QMARK_();
var seen_percent = (((cljs.core.count(seen_users) / cljs.core.count(all_users)) * (100)) | (0));
var team_id = new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(org_data);
var slack_bot_data = cljs.core.first(oc.web.lib.jwt.team_has_bot_QMARK_(team_id));
var remind_all_users = cljs.core.filterv((function (p1__45804_SHARP_){
return ((cljs.core.not(cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.wrt","sending-notice","oc.web.components.ui.wrt/sending-notice",1071997023).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__45804_SHARP_)))) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__45804_SHARP_),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(current_user_data))));
}),unseen_users);
return React.createElement("div",({"onClick": (function (p1__45805_SHARP_){
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.wrt","list-view-dropdown-open","oc.web.components.ui.wrt/list-view-dropdown-open",449956297).cljs$core$IFn$_invoke$arity$1(s)))){
if(cljs.core.truth_(oc.web.lib.utils.event_inside_QMARK_(p1__45805_SHARP_,rum.core.ref_node(s,new cljs.core.Keyword(null,"wrt-pop-up-tabs","wrt-pop-up-tabs",1553787792))))){
return null;
} else {
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.wrt","list-view-dropdown-open","oc.web.components.ui.wrt/list-view-dropdown-open",449956297).cljs$core$IFn$_invoke$arity$1(s),false);
}
} else {
if(cljs.core.truth_(oc.web.lib.utils.event_inside_QMARK_(p1__45805_SHARP_,rum.core.ref_node(s,new cljs.core.Keyword(null,"wrt-popup","wrt-popup",-783439380))))){
return null;
} else {
return oc.web.actions.nav_sidebar.hide_wrt();
}
}
}), "className": "wrt-popup-container"}),React.createElement("button",({"onClick": oc.web.actions.nav_sidebar.hide_wrt, "className": "mlb-reset modal-close-bt"})),React.createElement("div",({"ref": "wrt-popup", "onClick": (function (p1__45806_SHARP_){
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.wrt","list-view-dropdown-open","oc.web.components.ui.wrt/list-view-dropdown-open",449956297).cljs$core$IFn$_invoke$arity$1(s)))){
if(cljs.core.truth_(oc.web.lib.utils.event_inside_QMARK_(p1__45806_SHARP_,rum.core.ref_node(s,new cljs.core.Keyword(null,"wrt-pop-up-tabs","wrt-pop-up-tabs",1553787792))))){
} else {
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.wrt","list-view-dropdown-open","oc.web.components.ui.wrt/list-view-dropdown-open",449956297).cljs$core$IFn$_invoke$arity$1(s),false);
}

return oc.web.lib.utils.event_stop(p1__45806_SHARP_);
} else {
return null;
}
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["wrt-popup",oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"loading","loading",-737050189),cljs.core.not(new cljs.core.Keyword(null,"reads","reads",-1215067361).cljs$core$IFn$_invoke$arity$1(read_data))], null))], null))}),React.createElement("div",({"className": "wrt-popup-header"}),React.createElement("button",({"onClick": oc.web.actions.nav_sidebar.hide_wrt, "className": "mlb-reset mobile-close-bt"})),React.createElement("div",({"className": "wrt-popup-header-title"}),"Post analytics")),sablono.interpreter.interpret(((cljs.core.not(new cljs.core.Keyword(null,"reads","reads",-1215067361).cljs$core$IFn$_invoke$arity$1(read_data)))?(oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.small_loading.small_loading.call(null)):new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.wrt-popup-inner","div.wrt-popup-inner",689351563),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.wrt-chart-container","div.wrt-chart-container",1793150625),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.wrt-chart","div.wrt-chart",1306900191),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"svg","svg",856789142),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"width","width",-384071477),"116px",new cljs.core.Keyword(null,"height","height",1025178622),"116px",new cljs.core.Keyword(null,"viewBox","viewBox",-469489477),"0 0 116 116",new cljs.core.Keyword(null,"version","version",425292698),"1.1",new cljs.core.Keyword(null,"xmlns","xmlns",-1862095571),"http://www.w3.org/2000/svg",new cljs.core.Keyword(null,"xmlnsXlink","xmlnsXlink",1737638153),"http://www.w3.org/1999/xlink"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"circle.wrt-donut-ring","circle.wrt-donut-ring",-343447460),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"cx","cx",1272694324),"58px",new cljs.core.Keyword(null,"cy","cy",755331060),"58px",new cljs.core.Keyword(null,"r","r",-471384190),"50px",new cljs.core.Keyword(null,"fill","fill",883462889),"transparent",new cljs.core.Keyword(null,"stroke","stroke",1741823555),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.actions.ui_theme.computed_value(oc.web.actions.ui_theme.get_ui_theme_setting()),new cljs.core.Keyword(null,"dark","dark",1818973999)))?"#DDDDDD":"#ECECEC"),new cljs.core.Keyword(null,"stroke-width","stroke-width",716836435),"16px"], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"circle.wrt-donut-segment","circle.wrt-donut-segment",1675869154),new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"cx","cx",1272694324),"58",new cljs.core.Keyword(null,"cy","cy",755331060),"58",new cljs.core.Keyword(null,"r","r",-471384190),"50",new cljs.core.Keyword(null,"fill","fill",883462889),"transparent",new cljs.core.Keyword(null,"stroke","stroke",1741823555),"#6833F1",new cljs.core.Keyword(null,"stroke-width","stroke-width",716836435),"16",new cljs.core.Keyword(null,"class","class",-2030961996),(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword(null,"first-render-done","first-render-done",1105112667).cljs$core$IFn$_invoke$arity$1(s)))?["wrt-donut-segment-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(seen_percent)].join(''):null)], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"g.wrt-chart-text","g.wrt-chart-text",838472476),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"text.wrt-chart-number","text.wrt-chart-number",720243158),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"x","x",2099068185),"50%",new cljs.core.Keyword(null,"y","y",-1757859776),"50%"], null),[cljs.core.str.cljs$core$IFn$_invoke$arity$1(seen_percent),"%"].join('')], null)], null)], null)], null),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.wrt-chart-label","div.wrt-chart-label",1949198270),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.count(all_users),cljs.core.count(seen_users)))?"\uD83D\uDC4F Everyone has seen this post!":((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((1),cljs.core.count(seen_users)))?"1 person has viewed this post.":(((cljs.core.count(seen_users) === (0)))?"No one has viewed this post.":[cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.count(seen_users))," of ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.count(all_users))," people viewed this ",(cljs.core.truth_(new cljs.core.Keyword(null,"private-access?","private-access?",1011589803).cljs$core$IFn$_invoke$arity$1(read_data))?"private ":null),"post."].join('')
))),(cljs.core.truth_((function (){var and__4115__auto__ = new cljs.core.Keyword(null,"private-access?","private-access?",1011589803).cljs$core$IFn$_invoke$arity$1(read_data);
if(cljs.core.truth_(and__4115__auto__)){
return oc.web.dispatcher.board_data.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([current_org_slug,new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(activity_data)], 0));
} else {
return and__4115__auto__;
}
})())?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.manage-section-bt","button.mlb-reset.manage-section-bt",1012341070),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.actions.nav_sidebar.show_section_editor(new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(activity_data));
})], null),"Manage team members?"], null):null),(cljs.core.truth_((((cljs.core.count(remind_all_users) > (1)))?cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.wrt","show-remind-all-bt","oc.web.components.ui.wrt/show-remind-all-bt",445715702).cljs$core$IFn$_invoke$arity$1(s)):false))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.send-to-all-bt","button.mlb-reset.send-to-all-bt",-206027247),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.components.ui.wrt.remind_to_all(s,new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"activity-data","activity-data",1293689136),activity_data,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915),current_user_data,new cljs.core.Keyword(null,"users-list","users-list",1881920809),remind_all_users,new cljs.core.Keyword(null,"slack-bot-data","slack-bot-data",1066147116),slack_bot_data,new cljs.core.Keyword(null,"unopened-count","unopened-count",-1677253664),cljs.core.count(unseen_users)], null));
}),new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687),((is_mobile_QMARK_)?null:"tooltip"),new cljs.core.Keyword(null,"data-placement","data-placement",166529525),"top",new cljs.core.Keyword(null,"title","title",636505583),"Send a reminder to everyone that hasn\u2019t opened it"], null),"Send reminders"], null):null)], null)], null),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.wrt-popup-tabs","div.wrt-popup-tabs",-1885606209),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ref","ref",1289896967),new cljs.core.Keyword(null,"wrt-pop-up-tabs","wrt-pop-up-tabs",1553787792)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.wrt-popup-tabs-select.oc-input","div.wrt-popup-tabs-select.oc-input",1866323174),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("oc.web.components.ui.wrt","list-view-dropdown-open","oc.web.components.ui.wrt/list-view-dropdown-open",449956297).cljs$core$IFn$_invoke$arity$1(s),cljs.core.not);
}),new cljs.core.Keyword(null,"class","class",-2030961996),(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.wrt","list-view-dropdown-open","oc.web.components.ui.wrt/list-view-dropdown-open",449956297).cljs$core$IFn$_invoke$arity$1(s)))?"active":null)], null),oc.web.components.ui.wrt.dropdown_label(cljs.core.deref(list_view),cljs.core.count(all_users))], null),(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.wrt","list-view-dropdown-open","oc.web.components.ui.wrt/list-view-dropdown-open",449956297).cljs$core$IFn$_invoke$arity$1(s)))?(function (){var G__45817 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"items","items",1031954938),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"value","value",305978217),new cljs.core.Keyword(null,"all","all",892129742),new cljs.core.Keyword(null,"label","label",1718410804),oc.web.components.ui.wrt.dropdown_label(new cljs.core.Keyword(null,"all","all",892129742),cljs.core.count(all_users))], null),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"value","value",305978217),new cljs.core.Keyword(null,"seen","seen",-518999789),new cljs.core.Keyword(null,"label","label",1718410804),oc.web.components.ui.wrt.dropdown_label(new cljs.core.Keyword(null,"seen","seen",-518999789),cljs.core.count(all_users))], null),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"value","value",305978217),new cljs.core.Keyword(null,"unseen","unseen",1063275592),new cljs.core.Keyword(null,"label","label",1718410804),oc.web.components.ui.wrt.dropdown_label(new cljs.core.Keyword(null,"unseen","unseen",1063275592),cljs.core.count(all_users))], null)], null),new cljs.core.Keyword(null,"value","value",305978217),cljs.core.deref(list_view),new cljs.core.Keyword(null,"on-change","on-change",-732046149),(function (p1__45807_SHARP_){
cljs.core.reset_BANG_(list_view,new cljs.core.Keyword(null,"value","value",305978217).cljs$core$IFn$_invoke$arity$1(p1__45807_SHARP_));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.wrt","list-view-dropdown-open","oc.web.components.ui.wrt/list-view-dropdown-open",449956297).cljs$core$IFn$_invoke$arity$1(s),false);

return cljs.core.reset_BANG_(query,"");
})], null);
return (oc.web.components.ui.dropdown_list.dropdown_list.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.dropdown_list.dropdown_list.cljs$core$IFn$_invoke$arity$1(G__45817) : oc.web.components.ui.dropdown_list.dropdown_list.call(null,G__45817));
})():null)], null),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(list_view),new cljs.core.Keyword(null,"all","all",892129742)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.wrt-popup-search-container.group","div.wrt-popup-search-container.group",1901097848),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input.wrt-popup-query.oc-input","input.wrt-popup-query.oc-input",426625018),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"value","value",305978217),cljs.core.deref(query),new cljs.core.Keyword(null,"type","type",1174270348),"text",new cljs.core.Keyword(null,"placeholder","placeholder",-104873083),"Search by name...",new cljs.core.Keyword(null,"ref","ref",1289896967),new cljs.core.Keyword(null,"search-field","search-field",546910583),new cljs.core.Keyword(null,"on-key-up","on-key-up",884441808),(function (p1__45808_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(p1__45808_SHARP_.key,"Escape")){
return oc.web.components.ui.wrt.reset_search(s);
} else {
return null;
}
}),new cljs.core.Keyword(null,"on-change","on-change",-732046149),(function (p1__45809_SHARP_){
return cljs.core.reset_BANG_(query,p1__45809_SHARP_.target.value);
})], null)], null)], null):null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.wrt-popup-list","div.wrt-popup-list",-1038961820),(function (){var iter__4529__auto__ = (function oc$web$components$ui$wrt$iter__45818(s__45819){
return (new cljs.core.LazySeq(null,(function (){
var s__45819__$1 = s__45819;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__45819__$1);
if(temp__5735__auto__){
var s__45819__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__45819__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__45819__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__45821 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__45820 = (0);
while(true){
if((i__45820 < size__4528__auto__)){
var u = cljs.core._nth(c__4527__auto__,i__45820);
var user_sending_notice = cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.wrt","sending-notice","oc.web.components.ui.wrt/sending-notice",1071997023).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(u));
var is_self_user_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(current_user_data),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(u));
var slack_user = cljs.core.get.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slack-users","slack-users",-434149941).cljs$core$IFn$_invoke$arity$1(u),cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(slack_bot_data)));
cljs.core.chunk_append(b__45821,new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.wrt-popup-list-row","div.wrt-popup-list-row",-1986862972),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"key","key",-1516042587),["wrt-popup-row-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(u))].join(''),new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"seen","seen",-518999789),(function (){var and__4115__auto__ = new cljs.core.Keyword(null,"seen","seen",-518999789).cljs$core$IFn$_invoke$arity$1(u);
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(list_view),new cljs.core.Keyword(null,"all","all",892129742));
} else {
return and__4115__auto__;
}
})(),new cljs.core.Keyword(null,"sent","sent",-1537501490),user_sending_notice], null))], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.wrt-popup-list-row-avatar","div.wrt-popup-list-row-avatar",634214359),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),(cljs.core.truth_(new cljs.core.Keyword(null,"seen","seen",-518999789).cljs$core$IFn$_invoke$arity$1(u))?"seen":null)], null),(oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1(u) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,u))], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.wrt-popup-list-row-name","div.wrt-popup-list-row-name",-2033018096),oc.lib.user.name_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([u], 0)),((is_self_user_QMARK_)?" (you)":null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.wrt-popup-list-row-seen","div.wrt-popup-list-row-seen",-1735307023),(cljs.core.truth_(new cljs.core.Keyword(null,"seen","seen",-518999789).cljs$core$IFn$_invoke$arity$1(u))?cuerdas.core.capital(cuerdas.core.lower(oc.web.lib.utils.time_since(new cljs.core.Keyword(null,"read-at","read-at",-936006929).cljs$core$IFn$_invoke$arity$1(u)))):(cljs.core.truth_(user_sending_notice)?((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(user_sending_notice,new cljs.core.Keyword(null,"loading","loading",-737050189)))?"Sending...":user_sending_notice):"Unopened"))], null),((((cljs.core.not(new cljs.core.Keyword(null,"seen","seen",-518999789).cljs$core$IFn$_invoke$arity$1(u))) && ((!(is_self_user_QMARK_))) && (cljs.core.not(user_sending_notice))))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.send-reminder-bt","button.mlb-reset.send-reminder-bt",1010529035),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (i__45820,user_sending_notice,is_self_user_QMARK_,slack_user,u,c__4527__auto__,size__4528__auto__,b__45821,s__45819__$2,temp__5735__auto__,activity_data,current_user_data,current_org_slug,is_author_QMARK_,read_data_STAR_,read_data,item_id,seen_users,seen_ids,unseen_users,all_users,read_count,query,lower_query,list_view,filtered_users,sorted_filtered_users,is_mobile_QMARK_,seen_percent,team_id,slack_bot_data,remind_all_users){
return (function (){
return oc.web.components.ui.wrt.remind_to(s,new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"activity-data","activity-data",1293689136),activity_data,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915),current_user_data,new cljs.core.Keyword(null,"users-list","users-list",1881920809),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [u], null),new cljs.core.Keyword(null,"slack-bot-data","slack-bot-data",1066147116),slack_bot_data], null));
});})(i__45820,user_sending_notice,is_self_user_QMARK_,slack_user,u,c__4527__auto__,size__4528__auto__,b__45821,s__45819__$2,temp__5735__auto__,activity_data,current_user_data,current_org_slug,is_author_QMARK_,read_data_STAR_,read_data,item_id,seen_users,seen_ids,unseen_users,all_users,read_count,query,lower_query,list_view,filtered_users,sorted_filtered_users,is_mobile_QMARK_,seen_percent,team_id,slack_bot_data,remind_all_users))
], null),"Remind"], null):null)], null));

var G__45846 = (i__45820 + (1));
i__45820 = G__45846;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__45821),oc$web$components$ui$wrt$iter__45818(cljs.core.chunk_rest(s__45819__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__45821),null);
}
} else {
var u = cljs.core.first(s__45819__$2);
var user_sending_notice = cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.wrt","sending-notice","oc.web.components.ui.wrt/sending-notice",1071997023).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(u));
var is_self_user_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(current_user_data),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(u));
var slack_user = cljs.core.get.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slack-users","slack-users",-434149941).cljs$core$IFn$_invoke$arity$1(u),cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(slack_bot_data)));
return cljs.core.cons(new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.wrt-popup-list-row","div.wrt-popup-list-row",-1986862972),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"key","key",-1516042587),["wrt-popup-row-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(u))].join(''),new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"seen","seen",-518999789),(function (){var and__4115__auto__ = new cljs.core.Keyword(null,"seen","seen",-518999789).cljs$core$IFn$_invoke$arity$1(u);
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(list_view),new cljs.core.Keyword(null,"all","all",892129742));
} else {
return and__4115__auto__;
}
})(),new cljs.core.Keyword(null,"sent","sent",-1537501490),user_sending_notice], null))], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.wrt-popup-list-row-avatar","div.wrt-popup-list-row-avatar",634214359),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),(cljs.core.truth_(new cljs.core.Keyword(null,"seen","seen",-518999789).cljs$core$IFn$_invoke$arity$1(u))?"seen":null)], null),(oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1(u) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,u))], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.wrt-popup-list-row-name","div.wrt-popup-list-row-name",-2033018096),oc.lib.user.name_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([u], 0)),((is_self_user_QMARK_)?" (you)":null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.wrt-popup-list-row-seen","div.wrt-popup-list-row-seen",-1735307023),(cljs.core.truth_(new cljs.core.Keyword(null,"seen","seen",-518999789).cljs$core$IFn$_invoke$arity$1(u))?cuerdas.core.capital(cuerdas.core.lower(oc.web.lib.utils.time_since(new cljs.core.Keyword(null,"read-at","read-at",-936006929).cljs$core$IFn$_invoke$arity$1(u)))):(cljs.core.truth_(user_sending_notice)?((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(user_sending_notice,new cljs.core.Keyword(null,"loading","loading",-737050189)))?"Sending...":user_sending_notice):"Unopened"))], null),((((cljs.core.not(new cljs.core.Keyword(null,"seen","seen",-518999789).cljs$core$IFn$_invoke$arity$1(u))) && ((!(is_self_user_QMARK_))) && (cljs.core.not(user_sending_notice))))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.send-reminder-bt","button.mlb-reset.send-reminder-bt",1010529035),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (user_sending_notice,is_self_user_QMARK_,slack_user,u,s__45819__$2,temp__5735__auto__,activity_data,current_user_data,current_org_slug,is_author_QMARK_,read_data_STAR_,read_data,item_id,seen_users,seen_ids,unseen_users,all_users,read_count,query,lower_query,list_view,filtered_users,sorted_filtered_users,is_mobile_QMARK_,seen_percent,team_id,slack_bot_data,remind_all_users){
return (function (){
return oc.web.components.ui.wrt.remind_to(s,new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"activity-data","activity-data",1293689136),activity_data,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915),current_user_data,new cljs.core.Keyword(null,"users-list","users-list",1881920809),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [u], null),new cljs.core.Keyword(null,"slack-bot-data","slack-bot-data",1066147116),slack_bot_data], null));
});})(user_sending_notice,is_self_user_QMARK_,slack_user,u,s__45819__$2,temp__5735__auto__,activity_data,current_user_data,current_org_slug,is_author_QMARK_,read_data_STAR_,read_data,item_id,seen_users,seen_ids,unseen_users,all_users,read_count,query,lower_query,list_view,filtered_users,sorted_filtered_users,is_mobile_QMARK_,seen_percent,team_id,slack_bot_data,remind_all_users))
], null),"Remind"], null):null)], null),oc$web$components$ui$wrt$iter__45818(cljs.core.rest(s__45819__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(sorted_filtered_users);
})()], null)], null)))));
}),new cljs.core.PersistentVector(null, 16, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"wrt-read-data","wrt-read-data",-1241718247)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"wrt-activity-data","wrt-activity-data",-1368150621)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"org-slug","org-slug",-726595051)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.wrt","search-active","oc.web.components.ui.wrt/search-active",571990445)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.wrt","search-focused","oc.web.components.ui.wrt/search-focused",-1468987657)),rum.core.local.cljs$core$IFn$_invoke$arity$2("",new cljs.core.Keyword("oc.web.components.ui.wrt","query","oc.web.components.ui.wrt/query",-624128005)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.wrt","list-view-dropdown-open","oc.web.components.ui.wrt/list-view-dropdown-open",449956297)),rum.core.local.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"all","all",892129742),new cljs.core.Keyword("oc.web.components.ui.wrt","list-view","oc.web.components.ui.wrt/list-view",318473816)),rum.core.local.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,new cljs.core.Keyword("oc.web.components.ui.wrt","sending-notice","oc.web.components.ui.wrt/sending-notice",1071997023)),rum.core.local.cljs$core$IFn$_invoke$arity$2(true,new cljs.core.Keyword("oc.web.components.ui.wrt","show-remind-all-bt","oc.web.components.ui.wrt/show-remind-all-bt",445715702)),oc.web.mixins.ui.no_scroll_mixin,oc.web.mixins.ui.first_render_mixin,oc.web.mixins.ui.refresh_tooltips_mixin,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
var temp__5735__auto___45847 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"wrt-activity-data","wrt-activity-data",-1368150621)));
if(cljs.core.truth_(temp__5735__auto___45847)){
var activity_data_45848 = temp__5735__auto___45847;
oc.web.actions.activity.request_reads_data(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data_45848));
} else {
}

return s;
}),new cljs.core.Keyword(null,"after-render","after-render",1997533433),(function (s){
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.wrt","search-active","oc.web.components.ui.wrt/search-active",571990445).cljs$core$IFn$_invoke$arity$1(s)))){
if(cljs.core.compare_and_set_BANG_(new cljs.core.Keyword("oc.web.components.ui.wrt","search-focused","oc.web.components.ui.wrt/search-focused",-1468987657).cljs$core$IFn$_invoke$arity$1(s),false,true)){
rum.core.ref_node(s,new cljs.core.Keyword(null,"search-field","search-field",546910583)).focus();
} else {
}
} else {
}

return s;
})], null)], null),"wrt");
oc.web.components.ui.wrt.under_middle_screen_QMARK_ = (function oc$web$components$ui$wrt$under_middle_screen_QMARK_(el){
var el_offset_top = ($(el).offset()["top"]);
var fixed_top_position = (el_offset_top - document.scrollingElement.scrollTop);
var win_height = window.innerHeight;
return (fixed_top_position >= (win_height / (2)));
});
oc.web.components.ui.wrt.wrt_count = rum.core.build_defc((function (p__45828){
var map__45829 = p__45828;
var map__45829__$1 = (((((!((map__45829 == null))))?(((((map__45829.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45829.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45829):map__45829);
var activity_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45829__$1,new cljs.core.Keyword(null,"activity-data","activity-data",1293689136));
var read_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45829__$1,new cljs.core.Keyword(null,"read-data","read-data",-715156010));
var item_id = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data);
var is_author_QMARK_ = new cljs.core.Keyword(null,"publisher?","publisher?",30448149).cljs$core$IFn$_invoke$arity$1(activity_data);
var reads_count = (cljs.core.truth_((function (){var and__4115__auto__ = is_author_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return new cljs.core.Keyword(null,"last-read-at","last-read-at",-216601930).cljs$core$IFn$_invoke$arity$1(activity_data);
} else {
return and__4115__auto__;
}
})())?(new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(read_data) - (1)):new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(read_data));
var is_mobile_QMARK_ = oc.web.lib.responsive.is_tablet_or_mobile_QMARK_();
return sablono.interpreter.interpret(((cljs.core.map_QMARK_(read_data))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.wrt-count-container","div.wrt-count-container",-297896936),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687),((is_mobile_QMARK_)?null:"tooltip"),new cljs.core.Keyword(null,"data-placement","data-placement",166529525),"top",new cljs.core.Keyword(null,"data-container","data-container",1473653353),"body",new cljs.core.Keyword(null,"title","title",636505583),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(reads_count,(1)))?"1 person viewed":(((reads_count > (0)))?[cljs.core.str.cljs$core$IFn$_invoke$arity$1(reads_count)," people viewed"].join(''):"No views yet"
))], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.wrt-count","div.wrt-count",84979435),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"ref","ref",1289896967),new cljs.core.Keyword(null,"wrt-count","wrt-count",2113259352),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.actions.nav_sidebar.show_wrt(item_id);
})], null),reads_count], null)], null):null));
}),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$], null),"wrt-count");

//# sourceMappingURL=oc.web.components.ui.wrt.js.map
