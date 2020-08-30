goog.provide('oc.web.actions.reply');
oc.web.actions.reply.reply_expand = (function oc$web$actions$reply$reply_expand(entry_data,reply_data){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"reply-expand","reply-expand",850997660),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),entry_data,reply_data], null));
});
oc.web.actions.reply.reply_mark_seen = (function oc$web$actions$reply$reply_mark_seen(entry_data,reply_data){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"replies-mark-seen","replies-mark-seen",1194812310),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),entry_data,reply_data], null));
});
oc.web.actions.reply.replies_mark_seen = (function oc$web$actions$reply$replies_mark_seen(entry_data){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"replies-entry-mark-seen","replies-entry-mark-seen",-907180618),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),entry_data], null));
});
oc.web.actions.reply.reply_unwrap_body = (function oc$web$actions$reply$reply_unwrap_body(entry_data,reply_data){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"reply-unwrap-body","reply-unwrap-body",-1040001908),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),entry_data,reply_data], null));
});
oc.web.actions.reply.replies_expand = (function oc$web$actions$reply$replies_expand(entry_data){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"replies-expand","replies-expand",-356775657),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),entry_data], null));
});
oc.web.actions.reply.replies_add = (function oc$web$actions$reply$replies_add(entry_data,new_comment_data){
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
var replies_data = oc.web.dispatcher.container_data.cljs$core$IFn$_invoke$arity$4(cljs.core.deref(oc.web.dispatcher.app_state),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),new cljs.core.Keyword(null,"replies","replies",-1389888974),oc.web.dispatcher.recent_activity_sort);
var parsed_comment_data = oc.web.utils.activity.parse_comment.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([org_data,entry_data,new_comment_data,new cljs.core.Keyword(null,"last-seen-at","last-seen-at",1929467667).cljs$core$IFn$_invoke$arity$1(replies_data)], 0));
var parsed_reply_data = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([parsed_comment_data,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"unwrapped-body","unwrapped-body",457764863),true,new cljs.core.Keyword(null,"collapsed","collapsed",-628494523),false,new cljs.core.Keyword(null,"unseen","unseen",1063275592),false], null)], 0));
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"replies-add","replies-add",-1488759926),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),entry_data,parsed_reply_data], null));
});

//# sourceMappingURL=oc.web.actions.reply.js.map
