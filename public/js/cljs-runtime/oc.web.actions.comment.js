goog.provide('oc.web.actions.comment');
oc.web.actions.comment.add_comment_focus = (function oc$web$actions$comment$add_comment_focus(focus_value){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"add-comment-focus","add-comment-focus",-452934461),focus_value], null));
});
oc.web.actions.comment.reply_to = (function oc$web$actions$comment$reply_to(var_args){
var args__4742__auto__ = [];
var len__4736__auto___43160 = arguments.length;
var i__4737__auto___43161 = (0);
while(true){
if((i__4737__auto___43161 < len__4736__auto___43160)){
args__4742__auto__.push((arguments[i__4737__auto___43161]));

var G__43162 = (i__4737__auto___43161 + (1));
i__4737__auto___43161 = G__43162;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((2) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((2)),(0),null)):null);
return oc.web.actions.comment.reply_to.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__4743__auto__);
});

(oc.web.actions.comment.reply_to.cljs$core$IFn$_invoke$arity$variadic = (function (focus_value,parent_body,p__43116){
var vec__43117 = p__43116;
var focus_field_QMARK_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43117,(0),null);
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("add-comment","reply","add-comment/reply",-2059207478),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),focus_value,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"body","body",-2049205669),parent_body,new cljs.core.Keyword(null,"focus","focus",234677911),focus_field_QMARK_], null)], null));
}));

(oc.web.actions.comment.reply_to.cljs$lang$maxFixedArity = (2));

/** @this {Function} */
(oc.web.actions.comment.reply_to.cljs$lang$applyTo = (function (seq43113){
var G__43114 = cljs.core.first(seq43113);
var seq43113__$1 = cljs.core.next(seq43113);
var G__43115 = cljs.core.first(seq43113__$1);
var seq43113__$2 = cljs.core.next(seq43113__$1);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__43114,G__43115,seq43113__$2);
}));

oc.web.actions.comment.reset_reply_to = (function oc$web$actions$comment$reset_reply_to(focus_value){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("add-comment","reset-reply","add-comment/reset-reply",558424878),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),focus_value], null));
});
oc.web.actions.comment.add_comment_blur = (function oc$web$actions$comment$add_comment_blur(focus_value){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"add-comment-blur","add-comment-blur",1415256628),focus_value], null));
});
oc.web.actions.comment.edit_comment = (function oc$web$actions$comment$edit_comment(activity_uuid,comment_data){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"add-comment-change","add-comment-change",1301937897),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),activity_uuid,new cljs.core.Keyword(null,"reply-parent","reply-parent",579138103).cljs$core$IFn$_invoke$arity$1(comment_data),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(comment_data),new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(comment_data)], null));
});
oc.web.actions.comment.stop_comment_edit = (function oc$web$actions$comment$stop_comment_edit(activity_uuid,comment_data){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"add-comment-change","add-comment-change",1301937897),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),activity_uuid,new cljs.core.Keyword(null,"reply-parent","reply-parent",579138103).cljs$core$IFn$_invoke$arity$1(comment_data),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(comment_data),null], null));
});
oc.web.actions.comment.add_comment_change = (function oc$web$actions$comment$add_comment_change(activity_data,parent_comment_uuid,comment_uuid,comment_body){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"add-comment-change","add-comment-change",1301937897),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data),parent_comment_uuid,comment_uuid,comment_body], null));
});
oc.web.actions.comment.add_comment_reset = (function oc$web$actions$comment$add_comment_reset(prefix,activity_uuid,parent_comment_uuid,comment_uuid){
oc.web.actions.comment.add_comment_blur(oc.web.utils.comment.add_comment_focus_value.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([prefix,activity_uuid,parent_comment_uuid,comment_uuid], 0)));

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"add-comment-reset","add-comment-reset",130264416),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),activity_uuid,parent_comment_uuid,comment_uuid], null));
});
oc.web.actions.comment.add_comment = (function oc$web$actions$comment$add_comment(activity_data,comment_body,parent_comment_uuid,save_done_cb){
var org_slug = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
var comments_key = oc.web.dispatcher.activity_comments_key(org_slug,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));
var comments_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),comments_key);
var add_comment_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(activity_data),"create","POST"], 0));
var current_user_id = oc.web.lib.jwt.user_id();
var new_comment_uuid = oc.web.lib.utils.activity_uuid();
var user_data = (cljs.core.truth_(oc.web.lib.jwt.jwt())?oc.web.lib.jwt.get_contents():oc.web.lib.jwt.get_id_token_contents.cljs$core$IFn$_invoke$arity$0());
var new_comment_map = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"body","body",-2049205669),comment_body,new cljs.core.Keyword(null,"created-at","created-at",-89248644),oc.web.lib.utils.as_of_now(),new cljs.core.Keyword(null,"parent-uuid","parent-uuid",-2003485227),parent_comment_uuid,new cljs.core.Keyword(null,"resource-uuid","resource-uuid",1798351789),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data),new cljs.core.Keyword(null,"uuid","uuid",-2145095719),new_comment_uuid,new cljs.core.Keyword(null,"author","author",2111686192),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(user_data),new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103),new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103).cljs$core$IFn$_invoke$arity$1(user_data),new cljs.core.Keyword(null,"user-id","user-id",-206822291),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user_data)], null)], null);
var first_comment_from_user_QMARK_ = (cljs.core.truth_(new cljs.core.Keyword(null,"publisher?","publisher?",30448149).cljs$core$IFn$_invoke$arity$1(activity_data))?null:cljs.core.not(cljs.core.seq(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__43124_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"author","author",2111686192).cljs$core$IFn$_invoke$arity$1(p1__43124_SHARP_)),current_user_id);
}),comments_data))));
var should_show_follow_notification_QMARK_ = (function (){var and__4115__auto__ = first_comment_from_user_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(activity_data),"follow"], 0));
} else {
return and__4115__auto__;
}
})();
var current_board_slug = oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0();
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"add-comment-reset","add-comment-reset",130264416),org_slug,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data),parent_comment_uuid,null], null));

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"comment-add","comment-add",1843969593),org_slug,activity_data,new_comment_map,parent_comment_uuid,comments_key,new_comment_uuid], null));

oc.web.actions.activity.send_item_read(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));

oc.web.api.add_comment(add_comment_link,comment_body,new_comment_uuid,parent_comment_uuid,(function (p__43127){
var map__43128 = p__43127;
var map__43128__$1 = (((((!((map__43128 == null))))?(((((map__43128.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43128.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43128):map__43128);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43128__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43128__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43128__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
(save_done_cb.cljs$core$IFn$_invoke$arity$1 ? save_done_cb.cljs$core$IFn$_invoke$arity$1(success) : save_done_cb.call(null,success));

if(cljs.core.truth_(success)){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("comment-add","replace","comment-add/replace",762118999),activity_data,oc.web.lib.json.json__GT_cljs(body),comments_key,new_comment_uuid], null));

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("ropute","rewrite","ropute/rewrite",-322605451),new cljs.core.Keyword(null,"refresh","refresh",1947415525),true], null));

if(cljs.core.truth_(should_show_follow_notification_QMARK_)){
oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"title","title",636505583),"You are now following this post.",new cljs.core.Keyword(null,"dismiss","dismiss",412569545),true,new cljs.core.Keyword(null,"expire","expire",-70657108),(3),new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"first-comment-follow-post","first-comment-follow-post",-2087124623)], null));
} else {
}
} else {
}

var comments_link_43164 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(activity_data),"comments"], 0));
oc.web.api.get_comments(comments_link_43164,(function (p1__43126_SHARP_){
var current_board_slug__$1 = oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0();
oc.web.utils.activity.get_comments_finished(org_slug,comments_key,activity_data,p1__43126_SHARP_);

if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug__$1),new cljs.core.Keyword(null,"replies","replies",-1389888974))){
return oc.web.actions.activity.replies_get.cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0());
} else {
return null;
}
}));

if(cljs.core.truth_(success)){
return null;
} else {
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("comment-add","failed","comment-add/failed",828640623),activity_data,new_comment_map,comments_key], null));

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"add-comment-change","add-comment-change",1301937897),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data),parent_comment_uuid,null,comment_body,true], null));
}
}));

return new_comment_map;
});
oc.web.actions.comment.get_comments = (function oc$web$actions$comment$get_comments(activity_data){
return oc.web.utils.activity.get_comments(activity_data);
});
oc.web.actions.comment.get_comments_if_needed = (function oc$web$actions$comment$get_comments_if_needed(activity_data,comments_data){
return oc.web.utils.activity.get_comments_if_needed.cljs$core$IFn$_invoke$arity$2(activity_data,comments_data);
});
oc.web.actions.comment.delete_comment = (function oc$web$actions$comment$delete_comment(activity_data,comment_data){
oc.web.actions.activity.send_item_read(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));

var org_slug = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
var comments_key = oc.web.dispatcher.activity_comments_key(org_slug,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));
var delte_comment_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(comment_data),"delete"], 0));
var current_board_slug = oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0();
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"comment-delete","comment-delete",-1631742525),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data),comment_data,comments_key], null));

return oc.web.api.delete_comment(delte_comment_link,(function (p__43138){
var map__43139 = p__43138;
var map__43139__$1 = (((((!((map__43139 == null))))?(((((map__43139.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43139.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43139):map__43139);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43139__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43139__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43139__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var comments_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(activity_data),"comments"], 0));
return oc.web.api.get_comments(comments_link,(function (p1__43136_SHARP_){
oc.web.utils.activity.get_comments_finished(org_slug,comments_key,activity_data,p1__43136_SHARP_);

if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug),new cljs.core.Keyword(null,"replies","replies",-1389888974))){
return oc.web.actions.activity.replies_get.cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0());
} else {
return null;
}
}));
}));
});
oc.web.actions.comment.comment_reaction_toggle = (function oc$web$actions$comment$comment_reaction_toggle(activity_data,comment_data,reaction_data,reacting_QMARK_){
oc.web.actions.activity.send_item_read(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));

var comments_key = oc.web.dispatcher.activity_comments_key(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));
var link_method = (cljs.core.truth_(reacting_QMARK_)?"PUT":"DELETE");
var reaction_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(reaction_data),"react",link_method], 0));
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"comment-reaction-toggle","comment-reaction-toggle",-1399257510),comments_key,activity_data,comment_data,reaction_data,reacting_QMARK_], null));

return oc.web.api.toggle_reaction(reaction_link,(function (p__43142){
var map__43143 = p__43142;
var map__43143__$1 = (((((!((map__43143 == null))))?(((((map__43143.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43143.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43143):map__43143);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43143__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43143__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43143__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
return oc.web.actions.comment.get_comments(activity_data);
}));
});
oc.web.actions.comment.react_from_picker = (function oc$web$actions$comment$react_from_picker(activity_data,comment_data,emoji){
var react_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(comment_data),"react","POST"], 0));
var comments_key = oc.web.dispatcher.activity_comments_key(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"comment-react-from-picker","comment-react-from-picker",2131407811),comments_key,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(comment_data),emoji], null));

return oc.web.api.react_from_picker(react_link,emoji,(function (p__43145){
var map__43146 = p__43145;
var map__43146__$1 = (((((!((map__43146 == null))))?(((((map__43146.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43146.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43146):map__43146);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43146__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var succes = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43146__$1,new cljs.core.Keyword(null,"succes","succes",-1963041977));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43146__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
return oc.web.actions.comment.get_comments(activity_data);
}));
});
oc.web.actions.comment.inc_time = (function oc$web$actions$comment$inc_time(t){
return (new Date(((new Date(t)).getTime() + (1)))).getTime();
});
oc.web.actions.comment.save_comment = (function oc$web$actions$comment$save_comment(activity_data,comment_data,new_body,save_done_cb){
oc.web.actions.activity.send_item_read(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));

var org_slug = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
var comments_key = oc.web.dispatcher.activity_comments_key(org_slug,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));
var patch_comment_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(comment_data),"partial-update"], 0));
var updated_comment_map = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([comment_data,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"body","body",-2049205669),new_body,new cljs.core.Keyword(null,"updated-at","updated-at",-1592622336),oc.web.actions.comment.inc_time(new cljs.core.Keyword(null,"updated-at","updated-at",-1592622336).cljs$core$IFn$_invoke$arity$1(comment_data))], null)], 0));
var current_board_slug = oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0();
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"comment-save","comment-save",-691862293),org_slug,comments_key,updated_comment_map], null));

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"add-comment-reset","add-comment-reset",130264416),org_slug,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data),new cljs.core.Keyword(null,"reply-parent","reply-parent",579138103).cljs$core$IFn$_invoke$arity$1(comment_data),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(comment_data)], null));

oc.web.api.patch_comment(patch_comment_link,new_body,(function (p__43149){
var map__43150 = p__43149;
var map__43150__$1 = (((((!((map__43150 == null))))?(((((map__43150.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43150.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43150):map__43150);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43150__$1,new cljs.core.Keyword(null,"success","success",1890645906));
(save_done_cb.cljs$core$IFn$_invoke$arity$1 ? save_done_cb.cljs$core$IFn$_invoke$arity$1(success) : save_done_cb.call(null,success));

var comments_link_43165 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(activity_data),"comments"], 0));
oc.web.api.get_comments(comments_link_43165,(function (p1__43148_SHARP_){
return oc.web.utils.activity.get_comments_finished(org_slug,comments_key,activity_data,p1__43148_SHARP_);
}));

if(cljs.core.truth_(success)){
} else {
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("comment-save","failed","comment-save/failed",-1040739977),activity_data,comment_data,comments_key], null));

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"add-comment-change","add-comment-change",1301937897),org_slug,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data),new cljs.core.Keyword(null,"parent-uuid","parent-uuid",-2003485227).cljs$core$IFn$_invoke$arity$1(comment_data),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(comment_data),new_body,true], null));
}

if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug),new cljs.core.Keyword(null,"replies","replies",-1389888974))){
return oc.web.actions.activity.replies_get.cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0());
} else {
return null;
}
}));

return updated_comment_map;
});
oc.web.actions.comment.ws_comment_update = (function oc$web$actions$comment$ws_comment_update(interaction_data){
var current_org_slug = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
var entry_uuid = new cljs.core.Keyword(null,"resource-uuid","resource-uuid",1798351789).cljs$core$IFn$_invoke$arity$1(interaction_data);
var comments_key = oc.web.dispatcher.activity_comments_key(current_org_slug,entry_uuid);
var entry_data = oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$2(current_org_slug,entry_uuid);
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("ws-interaction","comment-update","ws-interaction/comment-update",-1083818759),comments_key,interaction_data], null));

if(cljs.core.truth_(entry_data)){
return oc.web.actions.comment.get_comments(entry_data);
} else {
return null;
}
});
oc.web.actions.comment.ws_comment_delete = (function oc$web$actions$comment$ws_comment_delete(interaction_data){
var current_org_slug = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
var entry_uuid = new cljs.core.Keyword(null,"resource-uuid","resource-uuid",1798351789).cljs$core$IFn$_invoke$arity$1(interaction_data);
var entry_data = oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$2(current_org_slug,entry_uuid);
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("ws-interaction","comment-delete","ws-interaction/comment-delete",-908789150),current_org_slug,interaction_data], null));

if(cljs.core.truth_(entry_data)){
return oc.web.actions.comment.get_comments(entry_data);
} else {
return null;
}
});
oc.web.actions.comment.ws_comment_add = (function oc$web$actions$comment$ws_comment_add(interaction_data){
var current_org_slug = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
var container_id = new cljs.core.Keyword(null,"container-id","container-id",1274665684).cljs$core$IFn$_invoke$arity$1(interaction_data);
var entry_uuid = new cljs.core.Keyword(null,"resource-uuid","resource-uuid",1798351789).cljs$core$IFn$_invoke$arity$1(interaction_data);
var entry_data = oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$2(current_org_slug,entry_uuid);
var board_data = oc.web.utils.activity.board_by_uuid(container_id);
var new_comment_uuid = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"interaction","interaction",-2143888916).cljs$core$IFn$_invoke$arity$1(interaction_data));
var comments_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),oc.web.dispatcher.activity_comments_key(current_org_slug,entry_uuid));
var comment_exists_QMARK_ = cljs.core.seq(cljs.core.filterv((function (p1__43155_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__43155_SHARP_),new_comment_uuid);
}),comments_data));
var current_board_slug = oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0();
if(cljs.core.not(entry_data)){
oc.web.actions.cmail.get_entry_with_uuid.cljs$core$IFn$_invoke$arity$variadic((function (){var or__4126__auto__ = new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(board_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return container_id;
}
})(),entry_uuid,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(function (success,status){
if(cljs.core.truth_(success)){
return oc.web.actions.comment.get_comments(oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$2(current_org_slug,entry_uuid));
} else {
return null;
}
})], 0));
} else {
if(cljs.core.not(comment_exists_QMARK_)){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("ws-interaction","comment-add","ws-interaction/comment-add",-1026147104),current_org_slug,entry_data,interaction_data], null));

if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug),new cljs.core.Keyword(null,"replies","replies",-1389888974))){
oc.web.actions.activity.replies_get.cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0());
} else {
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update-replies-comments","update-replies-comments",-1613944614),current_org_slug,current_board_slug], null));
}
} else {
}
}

if(cljs.core.truth_(entry_data)){
return oc.web.actions.comment.get_comments(entry_data);
} else {
return null;
}
});
oc.web.actions.comment.subscribe = (function oc$web$actions$comment$subscribe(){
oc.web.ws.interaction_client.subscribe(new cljs.core.Keyword("interaction-comment","add","interaction-comment/add",-357724065),(function (p1__43156_SHARP_){
return oc.web.lib.utils.after((0),(function (){
return oc.web.actions.comment.ws_comment_add(new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(p1__43156_SHARP_));
}));
}));

oc.web.ws.interaction_client.subscribe(new cljs.core.Keyword("interaction-comment","update","interaction-comment/update",1637403952),(function (p1__43157_SHARP_){
return oc.web.actions.comment.ws_comment_update(new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(p1__43157_SHARP_));
}));

return oc.web.ws.interaction_client.subscribe(new cljs.core.Keyword("interaction-comment","delete","interaction-comment/delete",2035105872),(function (p1__43158_SHARP_){
return oc.web.actions.comment.ws_comment_delete(new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(p1__43158_SHARP_));
}));
});

//# sourceMappingURL=oc.web.actions.comment.js.map
