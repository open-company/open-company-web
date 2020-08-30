goog.provide('oc.web.stores.reaction');
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.stores !== 'undefined') && (typeof oc.web.stores.reaction !== 'undefined') && (typeof oc.web.stores.reaction.reactions_atom !== 'undefined')){
} else {
oc.web.stores.reaction.reactions_atom = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
}
oc.web.stores.reaction.make_activity_index = (function oc$web$stores$reaction$make_activity_index(uuid){
return cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(["activity-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(uuid)].join(''));
});
oc.web.stores.reaction.make_comment_index = (function oc$web$stores$reaction$make_comment_index(uuid){
return cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(["comment-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(uuid)].join(''));
});
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.stores !== 'undefined') && (typeof oc.web.stores.reaction !== 'undefined') && (typeof oc.web.stores.reaction.reducer !== 'undefined')){
} else {
oc.web.stores.reaction.reducer = (function (){var method_table__4619__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var prefer_table__4620__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var method_cache__4621__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var cached_hierarchy__4622__auto__ = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var hierarchy__4623__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$3(cljs.core.PersistentArrayMap.EMPTY,new cljs.core.Keyword(null,"hierarchy","hierarchy",-1053470341),(function (){var fexpr__43625 = cljs.core.get_global_hierarchy;
return (fexpr__43625.cljs$core$IFn$_invoke$arity$0 ? fexpr__43625.cljs$core$IFn$_invoke$arity$0() : fexpr__43625.call(null));
})());
return (new cljs.core.MultiFn(cljs.core.symbol.cljs$core$IFn$_invoke$arity$2("oc.web.stores.reaction","reducer"),(function (db,p__43626){
var vec__43629 = p__43626;
var seq__43631 = cljs.core.seq(vec__43629);
var first__43633 = cljs.core.first(seq__43631);
var seq__43631__$1 = cljs.core.next(seq__43631);
var action_type = first__43633;
var _ = seq__43631__$1;
if(cljs.core.truth_(cljs.core.some(cljs.core.PersistentHashSet.createAsIfByAssoc([action_type]),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),new cljs.core.Keyword(null,"input","input",556931961)], null)))){
} else {
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.stores.reaction",null,21,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Dispatching reaction reducer:",action_type], null);
}),null)),null,-1716321539);
}

return action_type;
}),new cljs.core.Keyword(null,"default","default",-1987822328),hierarchy__4623__auto__,method_table__4619__auto__,prefer_table__4620__auto__,method_cache__4621__auto__,cached_hierarchy__4622__auto__));
})();
}
oc.web.stores.reaction.reactions_dispatch = oc.web.dispatcher.actions.cljs_flux$dispatcher$IDispatcher$register$arity$2(null,(function (payload){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(oc.web.dispatcher.app_state,oc.web.stores.reaction.reducer,payload);
}));
oc.web.stores.reaction.handle_reaction_to_entry_finish = (function oc$web$stores$reaction$handle_reaction_to_entry_finish(db,activity_data,reaction,reaction_data,activity_key){
var activity_uuid = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data);
var next_reactions_loading = oc.web.lib.utils.vec_dissoc(new cljs.core.Keyword(null,"reactions-loading","reactions-loading",-1859557973).cljs$core$IFn$_invoke$arity$1(activity_data),reaction);
if((reaction_data == null)){
var updated_activity_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(activity_data,new cljs.core.Keyword(null,"reactions-loading","reactions-loading",-1859557973),next_reactions_loading);
return cljs.core.assoc_in(db,activity_key,updated_activity_data);
} else {
var reaction__$1 = cljs.core.first(cljs.core.keys(reaction_data));
var next_reaction_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.get.cljs$core$IFn$_invoke$arity$2(reaction_data,reaction__$1),new cljs.core.Keyword(null,"reaction","reaction",490869788),cljs.core.name(reaction__$1));
var reactions_data = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"reactions","reactions",2029850654).cljs$core$IFn$_invoke$arity$1(activity_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.PersistentVector.EMPTY;
}
})();
var reaction_idx = oc.web.lib.utils.index_of(reactions_data,(function (p1__43639_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"reaction","reaction",490869788).cljs$core$IFn$_invoke$arity$1(p1__43639_SHARP_),cljs.core.name(reaction__$1));
}));
var next_reactions_data = (((((reaction_idx < (0))) || ((reaction_idx == null))))?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(reactions_data,cljs.core.count(reactions_data),next_reaction_data):cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(reactions_data,reaction_idx,next_reaction_data));
var updated_activity_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(activity_data,new cljs.core.Keyword(null,"reactions-loading","reactions-loading",-1859557973),next_reactions_loading),new cljs.core.Keyword(null,"reactions","reactions",2029850654),next_reactions_data);
return cljs.core.assoc_in(db,activity_key,updated_activity_data);
}
});
oc.web.stores.reaction.handle_reaction_to_entry = (function oc$web$stores$reaction$handle_reaction_to_entry(db,activity_data,reaction_data,activity_key){
var old_reactions_loading = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"reactions-loading","reactions-loading",-1859557973).cljs$core$IFn$_invoke$arity$1(activity_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.PersistentVector.EMPTY;
}
})();
var next_reactions_loading = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(old_reactions_loading,new cljs.core.Keyword(null,"reaction","reaction",490869788).cljs$core$IFn$_invoke$arity$1(reaction_data));
var updated_activity_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(activity_data,new cljs.core.Keyword(null,"reactions-loading","reactions-loading",-1859557973),next_reactions_loading);
return cljs.core.assoc_in(db,activity_key,updated_activity_data);
});
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"handle-reaction-to-entry","handle-reaction-to-entry",-1153536571),(function (db,p__43677){
var vec__43678 = p__43677;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43678,(0),null);
var activity_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43678,(1),null);
var reaction_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43678,(2),null);
var activity_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43678,(3),null);
return oc.web.stores.reaction.handle_reaction_to_entry_finish(db,activity_data,new cljs.core.Keyword(null,"reaction","reaction",490869788).cljs$core$IFn$_invoke$arity$1(reaction_data),cljs.core.PersistentArrayMap.createAsIfByAssoc([cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"reaction","reaction",490869788).cljs$core$IFn$_invoke$arity$1(reaction_data)),reaction_data]),activity_key);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("react-from-picker","finish","react-from-picker/finish",761785599),(function (db,p__43681){
var vec__43682 = p__43681;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43682,(0),null);
var map__43685 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43682,(1),null);
var map__43685__$1 = (((((!((map__43685 == null))))?(((((map__43685.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43685.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43685):map__43685);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43685__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var activity_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43685__$1,new cljs.core.Keyword(null,"activity-data","activity-data",1293689136));
var reaction = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43685__$1,new cljs.core.Keyword(null,"reaction","reaction",490869788));
var reaction_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43685__$1,new cljs.core.Keyword(null,"reaction-data","reaction-data",807008847));
var activity_key = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43685__$1,new cljs.core.Keyword(null,"activity-key","activity-key",145369555));
if((((status >= (200))) && ((status < (300))))){
var reaction_key = cljs.core.first(cljs.core.keys(reaction_data));
var reaction__$1 = cljs.core.name(reaction_key);
return oc.web.stores.reaction.handle_reaction_to_entry_finish(db,activity_data,reaction__$1,reaction_data,activity_key);
} else {
return db;
}
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("activity-reaction-toggle","finish","activity-reaction-toggle/finish",720972056),(function (db,p__43691){
var vec__43692 = p__43691;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43692,(0),null);
var activity_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43692,(1),null);
var reaction = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43692,(2),null);
var reaction_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43692,(3),null);
var activity_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43692,(4),null);
return oc.web.stores.reaction.handle_reaction_to_entry_finish(db,activity_data,reaction,reaction_data,activity_key);
}));
oc.web.stores.reaction.update_entry_reaction_data = (function oc$web$stores$reaction$update_entry_reaction_data(add_event_QMARK_,interaction_data,entry_data){
if(cljs.core.truth_(entry_data)){
var reaction_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(new cljs.core.Keyword(null,"interaction","interaction",-2143888916).cljs$core$IFn$_invoke$arity$1(interaction_data),new cljs.core.Keyword(null,"count","count",2139924085),new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(interaction_data));
var reaction = new cljs.core.Keyword(null,"reaction","reaction",490869788).cljs$core$IFn$_invoke$arity$1(reaction_data);
var reactions_data = new cljs.core.Keyword(null,"reactions","reactions",2029850654).cljs$core$IFn$_invoke$arity$1(entry_data);
var reaction_idx = oc.web.lib.utils.index_of(reactions_data,(function (p1__43695_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"reaction","reaction",490869788).cljs$core$IFn$_invoke$arity$1(p1__43695_SHARP_),reaction);
}));
var reaction_found = ((typeof reaction_idx === 'number') && ((reaction_idx >= (0))));
var old_reaction_data = ((reaction_found)?cljs.core.get.cljs$core$IFn$_invoke$arity$2(reactions_data,reaction_idx):null);
var is_current_user = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"user-id","user-id",-206822291)),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"author","author",2111686192).cljs$core$IFn$_invoke$arity$1(reaction_data)));
var authors_fn = (cljs.core.truth_(add_event_QMARK_)?cljs.core.conj:oc.web.lib.utils.vec_dissoc);
var with_authors = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(reaction_data,new cljs.core.Keyword(null,"authors","authors",2063018172),(function (){var G__43696 = new cljs.core.Keyword(null,"authors","authors",2063018172).cljs$core$IFn$_invoke$arity$1(old_reaction_data);
var G__43697 = new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"author","author",2111686192).cljs$core$IFn$_invoke$arity$1(reaction_data));
return (authors_fn.cljs$core$IFn$_invoke$arity$2 ? authors_fn.cljs$core$IFn$_invoke$arity$2(G__43696,G__43697) : authors_fn.call(null,G__43696,G__43697));
})()),new cljs.core.Keyword(null,"author-ids","author-ids",-27587133),(function (){var G__43698 = new cljs.core.Keyword(null,"author-ids","author-ids",-27587133).cljs$core$IFn$_invoke$arity$1(old_reaction_data);
var G__43699 = new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"author","author",2111686192).cljs$core$IFn$_invoke$arity$1(reaction_data));
return (authors_fn.cljs$core$IFn$_invoke$arity$2 ? authors_fn.cljs$core$IFn$_invoke$arity$2(G__43698,G__43699) : authors_fn.call(null,G__43698,G__43699));
})());
var with_reacted = ((is_current_user)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(with_authors,new cljs.core.Keyword(null,"reacted","reacted",523485502),add_event_QMARK_):cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(with_authors,new cljs.core.Keyword(null,"reacted","reacted",523485502),(cljs.core.truth_(old_reaction_data)?new cljs.core.Keyword(null,"reacted","reacted",523485502).cljs$core$IFn$_invoke$arity$1(old_reaction_data):false)));
var new_reactions_data = (cljs.core.truth_(reactions_data)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(reactions_data,((reaction_found)?reaction_idx:cljs.core.count(reactions_data)),with_reacted):new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [with_reacted], null));
var new_entry_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(entry_data,new cljs.core.Keyword(null,"reactions","reactions",2029850654),new_reactions_data);
return new_entry_data;
} else {
return null;
}
});
oc.web.stores.reaction.update_entry_reaction = (function oc$web$stores$reaction$update_entry_reaction(db,interaction_data,add_event_QMARK_){
var item_uuid = new cljs.core.Keyword(null,"resource-uuid","resource-uuid",1798351789).cljs$core$IFn$_invoke$arity$1(interaction_data);
var entries_key = cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.stores.reaction.reactions_atom),oc.web.stores.reaction.make_activity_index(item_uuid));
var keys = cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [entries_key], null));
var new_data = cljs.core.map.cljs$core$IFn$_invoke$arity$2(cljs.core.vec,cljs.core.filter.cljs$core$IFn$_invoke$arity$2(cljs.core.second,cljs.core.partition.cljs$core$IFn$_invoke$arity$2((2),cljs.core.mapcat.cljs$core$IFn$_invoke$arity$variadic((function (p1__43704_SHARP_){
return (new cljs.core.PersistentVector(null,2,(5),cljs.core.PersistentVector.EMPTY_NODE,[p1__43704_SHARP_,oc.web.stores.reaction.update_entry_reaction_data(add_event_QMARK_,interaction_data,cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,p1__43704_SHARP_))],null));
}),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([keys], 0)))));
if(cljs.core.seq(new_data)){
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (p1__43707_SHARP_,p2__43708_SHARP_){
return cljs.core.assoc_in(p1__43707_SHARP_,cljs.core.first(p2__43708_SHARP_),cljs.core.second(p2__43708_SHARP_));
}),db,new_data);
} else {
return null;
}
});
oc.web.stores.reaction.get_comments_data = (function oc$web$stores$reaction$get_comments_data(db,data_key,item_uuid){
var comments_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,data_key);
var comment_idx = oc.web.lib.utils.index_of(comments_data,(function (p1__43715_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(item_uuid,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__43715_SHARP_));
}));
if(cljs.core.truth_(comment_idx)){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"index","index",-1531685915),comment_idx,new cljs.core.Keyword(null,"data","data",-232669377),comments_data], null);
} else {
return null;
}
});
oc.web.stores.reaction.update_comments_data = (function oc$web$stores$reaction$update_comments_data(add_event_QMARK_,interaction_data,data){
if(cljs.core.truth_(data)){
var reaction_data = new cljs.core.Keyword(null,"interaction","interaction",-2143888916).cljs$core$IFn$_invoke$arity$1(interaction_data);
var reaction = new cljs.core.Keyword(null,"reaction","reaction",490869788).cljs$core$IFn$_invoke$arity$1(reaction_data);
var comment_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(data),new cljs.core.Keyword(null,"index","index",-1531685915).cljs$core$IFn$_invoke$arity$1(data));
var reactions_data = new cljs.core.Keyword(null,"reactions","reactions",2029850654).cljs$core$IFn$_invoke$arity$1(comment_data);
var reaction_idx = oc.web.lib.utils.index_of(reactions_data,(function (p1__43716_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"reaction","reaction",490869788).cljs$core$IFn$_invoke$arity$1(p1__43716_SHARP_),reaction);
}));
var is_current_user = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"user-id","user-id",-206822291)),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"author","author",2111686192).cljs$core$IFn$_invoke$arity$1(reaction_data)));
var old_reaction_data = (cljs.core.truth_(reaction_idx)?cljs.core.nth.cljs$core$IFn$_invoke$arity$2(reactions_data,reaction_idx):new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"reacted","reacted",523485502),((is_current_user)?add_event_QMARK_:null),new cljs.core.Keyword(null,"authors","authors",2063018172),cljs.core.PersistentVector.EMPTY,new cljs.core.Keyword(null,"author-ids","author-ids",-27587133),cljs.core.PersistentVector.EMPTY,new cljs.core.Keyword(null,"links","links",-654507394),new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(reaction_data)], null));
var authors_fn = (cljs.core.truth_(add_event_QMARK_)?cljs.core.conj:oc.web.lib.utils.vec_dissoc);
var with_reacted = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([reaction_data,new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"count","count",2139924085),new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(interaction_data),new cljs.core.Keyword(null,"reacted","reacted",523485502),new cljs.core.Keyword(null,"reacted","reacted",523485502).cljs$core$IFn$_invoke$arity$1(old_reaction_data),new cljs.core.Keyword(null,"authors","authors",2063018172),(function (){var G__43725 = new cljs.core.Keyword(null,"authors","authors",2063018172).cljs$core$IFn$_invoke$arity$1(old_reaction_data);
var G__43726 = new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"author","author",2111686192).cljs$core$IFn$_invoke$arity$1(reaction_data));
return (authors_fn.cljs$core$IFn$_invoke$arity$2 ? authors_fn.cljs$core$IFn$_invoke$arity$2(G__43725,G__43726) : authors_fn.call(null,G__43725,G__43726));
})(),new cljs.core.Keyword(null,"author-ids","author-ids",-27587133),(function (){var G__43728 = new cljs.core.Keyword(null,"author-ids","author-ids",-27587133).cljs$core$IFn$_invoke$arity$1(old_reaction_data);
var G__43729 = new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"author","author",2111686192).cljs$core$IFn$_invoke$arity$1(reaction_data));
return (authors_fn.cljs$core$IFn$_invoke$arity$2 ? authors_fn.cljs$core$IFn$_invoke$arity$2(G__43728,G__43729) : authors_fn.call(null,G__43728,G__43729));
})(),new cljs.core.Keyword(null,"links","links",-654507394),new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(old_reaction_data)], null)], 0));
var new_reactions_data = (cljs.core.truth_(reaction_idx)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(reactions_data,reaction_idx,with_reacted):cljs.core.conj.cljs$core$IFn$_invoke$arity$2((function (){var or__4126__auto__ = reactions_data;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.PersistentVector.EMPTY;
}
})(),with_reacted));
var new_comment_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(comment_data,new cljs.core.Keyword(null,"reactions","reactions",2029850654),new_reactions_data);
var new_comments_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(data),new cljs.core.Keyword(null,"index","index",-1531685915).cljs$core$IFn$_invoke$arity$1(data),new_comment_data);
return new_comments_data;
} else {
return null;
}
});
oc.web.stores.reaction.update_comment_reaction = (function oc$web$stores$reaction$update_comment_reaction(db,interaction_data,add_event_QMARK_){
var item_uuid = new cljs.core.Keyword(null,"resource-uuid","resource-uuid",1798351789).cljs$core$IFn$_invoke$arity$1(interaction_data);
var comments_key = cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.stores.reaction.reactions_atom),oc.web.stores.reaction.make_comment_index(item_uuid));
var keys = cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [comments_key], null));
var new_data = cljs.core.map.cljs$core$IFn$_invoke$arity$2(cljs.core.vec,cljs.core.filter.cljs$core$IFn$_invoke$arity$2(cljs.core.second,cljs.core.partition.cljs$core$IFn$_invoke$arity$2((2),cljs.core.mapcat.cljs$core$IFn$_invoke$arity$variadic((function (p1__43737_SHARP_){
return (new cljs.core.PersistentVector(null,2,(5),cljs.core.PersistentVector.EMPTY_NODE,[p1__43737_SHARP_,oc.web.stores.reaction.update_comments_data(add_event_QMARK_,interaction_data,oc.web.stores.reaction.get_comments_data(db,p1__43737_SHARP_,item_uuid))],null));
}),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([keys], 0)))));
if(cljs.core.seq(new_data)){
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (p1__43738_SHARP_,p2__43739_SHARP_){
return cljs.core.assoc_in(p1__43738_SHARP_,cljs.core.first(p2__43739_SHARP_),cljs.core.second(p2__43739_SHARP_));
}),db,new_data);
} else {
return null;
}
});
oc.web.stores.reaction.update_reaction = (function oc$web$stores$reaction$update_reaction(db,interaction_data,add_event_QMARK_){
var with_updated_comment = oc.web.stores.reaction.update_comment_reaction(db,interaction_data,add_event_QMARK_);
var with_updated_activity = oc.web.stores.reaction.update_entry_reaction(db,interaction_data,add_event_QMARK_);
var or__4126__auto__ = with_updated_comment;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = with_updated_activity;
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
return db;
}
}
});
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("ws-interaction","reaction-add","ws-interaction/reaction-add",1150724264),(function (db,p__43754){
var vec__43759 = p__43754;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43759,(0),null);
var interaction_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43759,(1),null);
return oc.web.stores.reaction.update_reaction(db,interaction_data,true);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("ws-interaction","reaction-delete","ws-interaction/reaction-delete",341142900),(function (db,p__43763){
var vec__43764 = p__43763;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43764,(0),null);
var interaction_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43764,(1),null);
return oc.web.stores.reaction.update_reaction(db,interaction_data,false);
}));
oc.web.stores.reaction.reducer.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"default","default",-1987822328),(function (db,payload){
return db;
}));
oc.web.stores.reaction.index_comments = (function oc$web$stores$reaction$index_comments(ra,org__$1,board_slug,activity_uuid,comments){
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (acc,comment){
var idx = oc.web.stores.reaction.make_comment_index(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(comment));
var comment_key = oc.web.dispatcher.activity_comments_key(org__$1,activity_uuid);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(acc,idx,comment_key);
}),ra,comments);
});
oc.web.stores.reaction.index_posts = (function oc$web$stores$reaction$index_posts(ra,org__$1,posts){
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (acc,post){
var board_slug = new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(post);
var activity_uuid = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(post);
var idx = oc.web.stores.reaction.make_activity_index(activity_uuid);
var activity_key = oc.web.dispatcher.activity_key(org__$1,activity_uuid);
var next_acc = oc.web.stores.reaction.index_comments(acc,org__$1,board_slug,activity_uuid,new cljs.core.Keyword(null,"comments","comments",-293346423).cljs$core$IFn$_invoke$arity$1(post));
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(next_acc,idx,activity_key);
}),ra,posts);
});
oc.web.stores.reaction.reducer.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("all-posts-get","finish","all-posts-get/finish",-1262488350),(function (db,p__43775){
var vec__43776 = p__43775;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43776,(0),null);
var org__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43776,(1),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43776,(2),null);
var fixed_body = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43776,(3),null);
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(oc.web.stores.reaction.reactions_atom,oc.web.stores.reaction.index_posts,org__$1,cljs.core.vals(new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(fixed_body)));

return db;
}));
oc.web.stores.reaction.reducer.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("all-posts-more","finish","all-posts-more/finish",-1854949097),(function (db,p__43785){
var vec__43786 = p__43785;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43786,(0),null);
var org__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43786,(1),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43786,(2),null);
var direction = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43786,(3),null);
var fixed_body = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43786,(4),null);
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(oc.web.stores.reaction.reactions_atom,oc.web.stores.reaction.index_posts,org__$1,cljs.core.vals(new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(fixed_body)));

return db;
}));
oc.web.stores.reaction.reducer.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("inbox-get","finish","inbox-get/finish",1442342723),(function (db,p__43790){
var vec__43791 = p__43790;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43791,(0),null);
var org__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43791,(1),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43791,(2),null);
var fixed_body = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43791,(3),null);
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(oc.web.stores.reaction.reactions_atom,oc.web.stores.reaction.index_posts,org__$1,cljs.core.vals(new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(fixed_body)));

return db;
}));
oc.web.stores.reaction.reducer.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("bookmarks-get","finish","bookmarks-get/finish",1980734268),(function (db,p__43794){
var vec__43795 = p__43794;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43795,(0),null);
var org__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43795,(1),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43795,(2),null);
var fixed_body = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43795,(3),null);
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(oc.web.stores.reaction.reactions_atom,oc.web.stores.reaction.index_posts,org__$1,cljs.core.vals(new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(fixed_body)));

return db;
}));
oc.web.stores.reaction.reducer.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("following-get","finish","following-get/finish",-2078338056),(function (db,p__43799){
var vec__43804 = p__43799;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43804,(0),null);
var org__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43804,(1),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43804,(2),null);
var fixed_body = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43804,(3),null);
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(oc.web.stores.reaction.reactions_atom,oc.web.stores.reaction.index_posts,org__$1,cljs.core.vals(new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(fixed_body)));

return db;
}));
oc.web.stores.reaction.reducer.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("unfollowing-get","finish","unfollowing-get/finish",546005489),(function (db,p__43807){
var vec__43808 = p__43807;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43808,(0),null);
var org__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43808,(1),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43808,(2),null);
var fixed_body = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43808,(3),null);
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(oc.web.stores.reaction.reactions_atom,oc.web.stores.reaction.index_posts,org__$1,cljs.core.vals(new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(fixed_body)));

return db;
}));
oc.web.stores.reaction.reducer.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"section","section",-300141526),(function (db,p__43811){
var vec__43812 = p__43811;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43812,(0),null);
var org__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43812,(1),null);
var section_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43812,(2),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43812,(3),null);
var board_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43812,(4),null);
var fixed_board_data_43928 = oc.web.utils.activity.parse_board(board_data,oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$1(db),oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org__$1,db),oc.web.dispatcher.follow_boards_list.cljs$core$IFn$_invoke$arity$2(org__$1,db),sort_type);
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(oc.web.stores.reaction.reactions_atom,oc.web.stores.reaction.index_posts,org__$1,cljs.core.vals(new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(fixed_board_data_43928)));

return db;
}));
oc.web.stores.reaction.reducer.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("ws-interaction","comment-add","ws-interaction/comment-add",-1026147104),(function (db,p__43815){
var vec__43816 = p__43815;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43816,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43816,(1),null);
var entry_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43816,(2),null);
var interaction_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43816,(3),null);
var activity_uuid_43933 = new cljs.core.Keyword(null,"resource-uuid","resource-uuid",1798351789).cljs$core$IFn$_invoke$arity$1(interaction_data);
var activity_key_43934 = (function (){var G__43820 = oc.web.stores.reaction.make_activity_index(activity_uuid_43933);
var fexpr__43819 = cljs.core.deref(oc.web.stores.reaction.reactions_atom);
return (fexpr__43819.cljs$core$IFn$_invoke$arity$1 ? fexpr__43819.cljs$core$IFn$_invoke$arity$1(G__43820) : fexpr__43819.call(null,G__43820));
})();
var board_slug_43959 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(activity_key_43934,(2));
if(cljs.core.truth_(activity_key_43934)){
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$variadic(oc.web.stores.reaction.reactions_atom,oc.web.stores.reaction.index_comments,org_slug,board_slug_43959,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([activity_uuid_43933,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"interaction","interaction",-2143888916).cljs$core$IFn$_invoke$arity$1(interaction_data)], null)], 0));
} else {
}

return db;
}));
oc.web.stores.reaction.reducer.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("ws-interaction","comment-delete","ws-interaction/comment-delete",-908789150),(function (db,p__43821){
var vec__43822 = p__43821;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43822,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43822,(1),null);
var interaction_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43822,(2),null);
var activity_uuid_43964 = new cljs.core.Keyword(null,"resource-uuid","resource-uuid",1798351789).cljs$core$IFn$_invoke$arity$1(interaction_data);
var activity_key_43965 = (function (){var G__43831 = oc.web.stores.reaction.make_activity_index(activity_uuid_43964);
var fexpr__43830 = cljs.core.deref(oc.web.stores.reaction.reactions_atom);
return (fexpr__43830.cljs$core$IFn$_invoke$arity$1 ? fexpr__43830.cljs$core$IFn$_invoke$arity$1(G__43831) : fexpr__43830.call(null,G__43831));
})();
var board_slug_43966 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(activity_key_43965,(2));
if(cljs.core.truth_(activity_key_43965)){
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$variadic(oc.web.stores.reaction.reactions_atom,oc.web.stores.reaction.index_comments,org_slug,board_slug_43966,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([activity_uuid_43964,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"interaction","interaction",-2143888916).cljs$core$IFn$_invoke$arity$1(interaction_data)], null)], 0));
} else {
}

return db;
}));
oc.web.stores.reaction.reducer.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("comments-get","finish","comments-get/finish",-1883926059),(function (db,p__43832){
var vec__43833 = p__43832;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43833,(0),null);
var map__43836 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43833,(1),null);
var map__43836__$1 = (((((!((map__43836 == null))))?(((((map__43836.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43836.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43836):map__43836);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43836__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var error = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43836__$1,new cljs.core.Keyword(null,"error","error",-978969032));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43836__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var activity_uuid = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43836__$1,new cljs.core.Keyword(null,"activity-uuid","activity-uuid",-663317778));
var activity_key_43967 = (function (){var G__43839 = oc.web.stores.reaction.make_activity_index(activity_uuid);
var fexpr__43838 = cljs.core.deref(oc.web.stores.reaction.reactions_atom);
return (fexpr__43838.cljs$core$IFn$_invoke$arity$1 ? fexpr__43838.cljs$core$IFn$_invoke$arity$1(G__43839) : fexpr__43838.call(null,G__43839));
})();
var org_43968__$1 = cljs.core.first(activity_key_43967);
var board_slug_43969 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(activity_key_43967,(2));
if(cljs.core.truth_(activity_key_43967)){
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$variadic(oc.web.stores.reaction.reactions_atom,oc.web.stores.reaction.index_comments,org_43968__$1,board_slug_43969,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([activity_uuid,new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(body))], 0));
} else {
}

return db;
}));

//# sourceMappingURL=oc.web.stores.reaction.js.map
