goog.provide('oc.web.stores.reply');
oc.web.stores.reply.remove_collapse_items_inner = (function oc$web$stores$reply$remove_collapse_items_inner(entry_uuid,entries){
return cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (entry_data){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data),entry_uuid)){
return cljs.core.update.cljs$core$IFn$_invoke$arity$3(entry_data,new cljs.core.Keyword(null,"replies-data","replies-data",1118937948),(function (replies_data){
return cljs.core.filterv((function (p1__38653_SHARP_){
return cljs.core.not(oc.web.utils.activity.resource_type_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__38653_SHARP_,new cljs.core.Keyword(null,"collapsed-comments","collapsed-comments",-492771788)], 0)));
}),replies_data);
}));
} else {
return entry_data;
}
}),entries);
});
oc.web.stores.reply.remove_collapse_items = (function oc$web$stores$reply$remove_collapse_items(db,replies_key,entry_uuid){
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(db,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(replies_key,new cljs.core.Keyword(null,"items-to-render","items-to-render",660219762)),cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.stores.reply.remove_collapse_items_inner,entry_uuid)),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(replies_key,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897)),cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.stores.reply.remove_collapse_items_inner,entry_uuid));
});
oc.web.stores.reply.update_field = (function oc$web$stores$reply$update_field(item,kv,fv){
if(cljs.core.fn_QMARK_(fv)){
return cljs.core.update.cljs$core$IFn$_invoke$arity$3(item,kv,fv);
} else {
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(item,kv,fv);
}
});
oc.web.stores.reply.update_reply_inner = (function oc$web$stores$reply$update_reply_inner(entry_uuid,reply_uuid_or_set,kv,fv,entries){
return cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (entry_data){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data),entry_uuid)){
return cljs.core.update.cljs$core$IFn$_invoke$arity$3(entry_data,new cljs.core.Keyword(null,"replies-data","replies-data",1118937948),(function (replies_data){
return cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (reply_data){
if(cljs.core.truth_((function (){var or__4126__auto__ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(reply_uuid_or_set,new cljs.core.Keyword(null,"all","all",892129742));
if(or__4126__auto__){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(reply_uuid_or_set,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(reply_data));
if(or__4126__auto____$1){
return or__4126__auto____$1;
} else {
if(cljs.core.set_QMARK_(reply_uuid_or_set)){
var G__38665 = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(reply_data);
return (reply_uuid_or_set.cljs$core$IFn$_invoke$arity$1 ? reply_uuid_or_set.cljs$core$IFn$_invoke$arity$1(G__38665) : reply_uuid_or_set.call(null,G__38665));
} else {
return false;
}
}
}
})())){
return oc.web.stores.reply.update_field(reply_data,kv,fv);
} else {
return reply_data;
}
}),replies_data);
}));
} else {
return entry_data;
}
}),entries);
});
oc.web.stores.reply.update_reply = (function oc$web$stores$reply$update_reply(db,replies_key,entry_uuid,reply_uuid,kv,fv){
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(db,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(replies_key,new cljs.core.Keyword(null,"items-to-render","items-to-render",660219762)),cljs.core.partial.cljs$core$IFn$_invoke$arity$variadic(oc.web.stores.reply.update_reply_inner,entry_uuid,reply_uuid,kv,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([fv], 0))),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(replies_key,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897)),cljs.core.partial.cljs$core$IFn$_invoke$arity$variadic(oc.web.stores.reply.update_reply_inner,entry_uuid,reply_uuid,kv,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([fv], 0)));
});
oc.web.stores.reply.update_replies = (function oc$web$stores$reply$update_replies(db,replies_key,entry_uuid,kv,fv){
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(db,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(replies_key,new cljs.core.Keyword(null,"items-to-render","items-to-render",660219762)),cljs.core.partial.cljs$core$IFn$_invoke$arity$variadic(oc.web.stores.reply.update_reply_inner,entry_uuid,new cljs.core.Keyword(null,"all","all",892129742),kv,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([fv], 0))),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(replies_key,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897)),cljs.core.partial.cljs$core$IFn$_invoke$arity$variadic(oc.web.stores.reply.update_reply_inner,entry_uuid,new cljs.core.Keyword(null,"all","all",892129742),kv,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([fv], 0)));
});
oc.web.stores.reply.add_reply_inner = (function oc$web$stores$reply$add_reply_inner(entry_uuid,parsed_reply_data,entries){
return cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (entry_data){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data),entry_uuid)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.update.cljs$core$IFn$_invoke$arity$3(entry_data,new cljs.core.Keyword(null,"replies-data","replies-data",1118937948),(function (replies_data){
var with_seen = cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__38669_SHARP_){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__38669_SHARP_,new cljs.core.Keyword(null,"unseen","unseen",1063275592),false);
}),replies_data);
return cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(with_seen,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [parsed_reply_data], null)));
})),new cljs.core.Keyword(null,"unseen-comments","unseen-comments",-793262869),false);
} else {
return entry_data;
}
}),entries);
});
oc.web.stores.reply.add_reply = (function oc$web$stores$reply$add_reply(db,replies_key,entry_uuid,parsed_reply_data){
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(db,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(replies_key,new cljs.core.Keyword(null,"items-to-render","items-to-render",660219762)),cljs.core.partial.cljs$core$IFn$_invoke$arity$3(oc.web.stores.reply.add_reply_inner,entry_uuid,parsed_reply_data)),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(replies_key,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897)),cljs.core.partial.cljs$core$IFn$_invoke$arity$3(oc.web.stores.reply.add_reply_inner,entry_uuid,parsed_reply_data));
});
oc.web.stores.reply.update_entry_inner = (function oc$web$stores$reply$update_entry_inner(entry_uuid,kv,fv,entries){
return cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (entry_data){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data),entry_uuid)){
return oc.web.stores.reply.update_field(entry_data,kv,fv);
} else {
return entry_data;
}
}),entries);
});
oc.web.stores.reply.update_entry = (function oc$web$stores$reply$update_entry(db,replies_key,entry_uuid,kv,fv){
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(db,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(replies_key,new cljs.core.Keyword(null,"items-to-render","items-to-render",660219762)),cljs.core.partial.cljs$core$IFn$_invoke$arity$4(oc.web.stores.reply.update_entry_inner,entry_uuid,kv,fv)),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(replies_key,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897)),cljs.core.partial.cljs$core$IFn$_invoke$arity$4(oc.web.stores.reply.update_entry_inner,entry_uuid,kv,fv));
});
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"reply-expand","reply-expand",850997660),(function (db,p__38670){
var vec__38671 = p__38670;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38671,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38671,(1),null);
var entry_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38671,(2),null);
var reply_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38671,(3),null);
var replies_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,new cljs.core.Keyword(null,"replies","replies",-1389888974),oc.web.dispatcher.recent_activity_sort);
return oc.web.stores.reply.update_reply(db,replies_key,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(reply_data),new cljs.core.Keyword(null,"collapsed","collapsed",-628494523),false);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"reply-mark-seen","reply-mark-seen",944831375),(function (db,p__38674){
var vec__38675 = p__38674;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38675,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38675,(1),null);
var entry_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38675,(2),null);
var reply_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38675,(3),null);
var replies_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,new cljs.core.Keyword(null,"replies","replies",-1389888974),oc.web.dispatcher.recent_activity_sort);
return oc.web.stores.reply.update_reply(db,replies_key,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(reply_data),new cljs.core.Keyword(null,"unseen","unseen",1063275592),false);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"reply-unwrap-body","reply-unwrap-body",-1040001908),(function (db,p__38678){
var vec__38679 = p__38678;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38679,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38679,(1),null);
var entry_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38679,(2),null);
var reply_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38679,(3),null);
var replies_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,new cljs.core.Keyword(null,"replies","replies",-1389888974),oc.web.dispatcher.recent_activity_sort);
return oc.web.stores.reply.update_reply(db,replies_key,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(reply_data),new cljs.core.Keyword(null,"unwrapped-body","unwrapped-body",457764863),true);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"replies-entry-mark","replies-entry-mark",2062302259),(function (db,p__38682){
var vec__38683 = p__38682;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38683,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38683,(1),null);
var entry_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38683,(2),null);
var replies_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,new cljs.core.Keyword(null,"replies","replies",-1389888974),oc.web.dispatcher.recent_activity_sort);
return oc.web.stores.reply.update_replies(db,replies_key,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data),new cljs.core.Keyword(null,"unseen","unseen",1063275592),false);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"replies-expand","replies-expand",-356775657),(function (db,p__38686){
var vec__38687 = p__38686;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38687,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38687,(1),null);
var entry_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38687,(2),null);
var replies_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,new cljs.core.Keyword(null,"replies","replies",-1389888974),oc.web.dispatcher.recent_activity_sort);
return oc.web.stores.reply.remove_collapse_items(oc.web.stores.reply.update_entry(db,replies_key,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data),new cljs.core.Keyword(null,"expanded-replies","expanded-replies",-930394348),true),replies_key,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"replies-add","replies-add",-1488759926),(function (db,p__38690){
var vec__38691 = p__38690;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38691,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38691,(1),null);
var entry_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38691,(2),null);
var parsed_reply_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38691,(3),null);
var replies_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,new cljs.core.Keyword(null,"replies","replies",-1389888974),oc.web.dispatcher.recent_activity_sort);
return oc.web.stores.reply.add_reply(db,replies_key,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data),parsed_reply_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"reply-comments-loaded","reply-comments-loaded",-1365821825),(function (db,p__38694){
var vec__38695 = p__38694;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38695,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38695,(1),null);
var entry_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38695,(2),null);
var replies_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,new cljs.core.Keyword(null,"replies","replies",-1389888974),oc.web.dispatcher.recent_activity_sort);
return oc.web.stores.reply.update_replies(db,replies_key,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data),new cljs.core.Keyword(null,"comments-loaded?","comments-loaded?",-595034611),true);
}));

//# sourceMappingURL=oc.web.stores.reply.js.map
