goog.provide('oc.web.actions.reaction');
oc.web.actions.reaction.react_from_picker = (function oc$web$actions$reaction$react_from_picker(activity_data,emoji){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"handle-reaction-to-entry","handle-reaction-to-entry",-1153536571),activity_data,new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"reaction","reaction",490869788),emoji,new cljs.core.Keyword(null,"count","count",2139924085),(1),new cljs.core.Keyword(null,"reacted","reacted",523485502),true,new cljs.core.Keyword(null,"links","links",-654507394),cljs.core.PersistentVector.EMPTY,new cljs.core.Keyword(null,"authors","authors",2063018172),cljs.core.PersistentVector.EMPTY], null),oc.web.dispatcher.activity_key(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data))], null));

if(cljs.core.truth_((function (){var and__4115__auto__ = emoji;
if(cljs.core.truth_(and__4115__auto__)){
return oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(activity_data),"react"], 0));
} else {
return and__4115__auto__;
}
})())){
var react_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(activity_data),"react"], 0));
return oc.web.api.react_from_picker(react_link,emoji,(function (p__44162){
var map__44163 = p__44162;
var map__44163__$1 = (((((!((map__44163 == null))))?(((((map__44163.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__44163.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__44163):map__44163);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__44163__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__44163__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__44163__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
return oc.web.actions.activity.get_entry(activity_data);
}));
} else {
return null;
}
});
oc.web.actions.reaction.reaction_toggle = (function oc$web$actions$reaction$reaction_toggle(activity_data,reaction_data,reacting_QMARK_){
var activity_key = oc.web.dispatcher.activity_key(oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));
var link_method = (cljs.core.truth_(reacting_QMARK_)?"PUT":"DELETE");
var reaction_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(reaction_data),"react",link_method], 0));
var fixed_count = (cljs.core.truth_(reacting_QMARK_)?((function (){var or__4126__auto__ = new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(reaction_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return (0);
}
})() + (1)):((function (){var or__4126__auto__ = new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(reaction_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return (0);
}
})() - (1)));
var fixed_reaction_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(reaction_data,new cljs.core.Keyword(null,"count","count",2139924085),fixed_count),new cljs.core.Keyword(null,"reacted","reacted",523485502),reacting_QMARK_);
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"handle-reaction-to-entry","handle-reaction-to-entry",-1153536571),activity_data,fixed_reaction_data,activity_key], null));

return oc.web.api.toggle_reaction(reaction_link,(function (p__44165){
var map__44166 = p__44165;
var map__44166__$1 = (((((!((map__44166 == null))))?(((((map__44166.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__44166.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__44166):map__44166);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__44166__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__44166__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__44166__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
return oc.web.actions.activity.get_entry(activity_data);
}));
});
oc.web.actions.reaction.reaction_resource = (function oc$web$actions$reaction$reaction_resource(org_slug,item_uuid){
var entry_data = oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$2(org_slug,item_uuid);
var comment_data = oc.web.dispatcher.comment_data.cljs$core$IFn$_invoke$arity$2(org_slug,item_uuid);
var or__4126__auto__ = entry_data;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return comment_data;
}
});
oc.web.actions.reaction.refresh_entry_if_needed = (function oc$web$actions$reaction$refresh_entry_if_needed(org_slug,board_slug,resource_uuid,entry_data){
if(cljs.core.truth_(entry_data)){
return oc.web.actions.activity.get_entry(entry_data);
} else {
return oc.web.actions.cmail.get_entry_with_uuid(board_slug,resource_uuid);
}
});
oc.web.actions.reaction.refresh_comments_if_needed = (function oc$web$actions$reaction$refresh_comments_if_needed(org_slug,board_slug,comment_data){
var entry_uuid = new cljs.core.Keyword(null,"resource-uuid","resource-uuid",1798351789).cljs$core$IFn$_invoke$arity$1(comment_data);
var entry_data = oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$2(org_slug,entry_uuid);
if(cljs.core.truth_(entry_data)){
return oc.web.utils.activity.get_comments(entry_data);
} else {
return oc.web.actions.cmail.get_entry_with_uuid.cljs$core$IFn$_invoke$arity$variadic(board_slug,entry_uuid,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(function (success,status){
if(cljs.core.truth_(success)){
return oc.web.utils.activity.get_comments(oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$2(org_slug,entry_uuid));
} else {
return null;
}
})], 0));
}
});
oc.web.actions.reaction.refresh_resource = (function oc$web$actions$reaction$refresh_resource(org_slug,board_slug,item_uuid){
var resource_data = oc.web.actions.reaction.reaction_resource(org_slug,item_uuid);
if(cljs.core.truth_(oc.web.utils.activity.comment_QMARK_(resource_data))){
return oc.web.actions.reaction.refresh_comments_if_needed(org_slug,board_slug,resource_data);
} else {
return oc.web.actions.reaction.refresh_entry_if_needed(org_slug,board_slug,item_uuid,resource_data);
}
});
oc.web.actions.reaction.ws_interaction_reaction_add = (function oc$web$actions$reaction$ws_interaction_reaction_add(interaction_data){
var org_slug = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
var board_uuid = new cljs.core.Keyword(null,"container-id","container-id",1274665684).cljs$core$IFn$_invoke$arity$1(interaction_data);
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("ws-interaction","reaction-add","ws-interaction/reaction-add",1150724264),interaction_data], null));

return oc.web.actions.reaction.refresh_resource(org_slug,board_uuid,new cljs.core.Keyword(null,"resource-uuid","resource-uuid",1798351789).cljs$core$IFn$_invoke$arity$1(interaction_data));
});
oc.web.actions.reaction.ws_interaction_reaction_delete = (function oc$web$actions$reaction$ws_interaction_reaction_delete(interaction_data){
var org_slug = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
var board_uuid = new cljs.core.Keyword(null,"container-id","container-id",1274665684).cljs$core$IFn$_invoke$arity$1(interaction_data);
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("ws-interaction","reaction-delete","ws-interaction/reaction-delete",341142900),interaction_data], null));

return oc.web.actions.reaction.refresh_resource(org_slug,board_uuid,new cljs.core.Keyword(null,"resource-uuid","resource-uuid",1798351789).cljs$core$IFn$_invoke$arity$1(interaction_data));
});
oc.web.actions.reaction.subscribe = (function oc$web$actions$reaction$subscribe(){
oc.web.ws.interaction_client.subscribe(new cljs.core.Keyword("interaction-reaction","add","interaction-reaction/add",1243084351),(function (p1__44168_SHARP_){
return oc.web.actions.reaction.ws_interaction_reaction_add(new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(p1__44168_SHARP_));
}));

return oc.web.ws.interaction_client.subscribe(new cljs.core.Keyword("interaction-reaction","delete","interaction-reaction/delete",1121937008),(function (p1__44169_SHARP_){
return oc.web.actions.reaction.ws_interaction_reaction_delete(new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(p1__44169_SHARP_));
}));
});

//# sourceMappingURL=oc.web.actions.reaction.js.map
