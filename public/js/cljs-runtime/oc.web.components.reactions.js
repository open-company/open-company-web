goog.provide('oc.web.components.reactions');
var module$node_modules$emoji_mart$dist$index=shadow.js.require("module$node_modules$emoji_mart$dist$index", {});
oc.web.components.reactions.default_reaction_number = (3);
oc.web.components.reactions.reactions = rum.core.build_defcs((function (s,p__45992){
var map__45993 = p__45992;
var map__45993__$1 = (((((!((map__45993 == null))))?(((((map__45993.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45993.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45993):map__45993);
var entity_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45993__$1,new cljs.core.Keyword(null,"entity-data","entity-data",1608056141));
var hide_picker = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45993__$1,new cljs.core.Keyword(null,"hide-picker","hide-picker",-1886003178));
var optional_activity_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45993__$1,new cljs.core.Keyword(null,"optional-activity-data","optional-activity-data",-1392718475));
var max_reactions = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45993__$1,new cljs.core.Keyword(null,"max-reactions","max-reactions",-27019827));
var did_react_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45993__$1,new cljs.core.Keyword(null,"did-react-cb","did-react-cb",1627613154));
var only_thumb_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45993__$1,new cljs.core.Keyword(null,"only-thumb?","only-thumb?",959195446));
var filtered_reactions = (cljs.core.truth_(only_thumb_QMARK_)?cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__45991_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"reaction","reaction",490869788).cljs$core$IFn$_invoke$arity$1(p1__45991_SHARP_),"\uD83D\uDC4D");
}),new cljs.core.Keyword(null,"reactions","reactions",2029850654).cljs$core$IFn$_invoke$arity$1(entity_data)):new cljs.core.Keyword(null,"reactions","reactions",2029850654).cljs$core$IFn$_invoke$arity$1(entity_data));
var reactions_max_count = (cljs.core.truth_(only_thumb_QMARK_)?(1):(function (){var or__4126__auto__ = max_reactions;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.components.reactions.default_reaction_number;
}
})());
var reactions_data = cljs.core.vec(cljs.core.take.cljs$core$IFn$_invoke$arity$2(reactions_max_count,filtered_reactions));
var reactions_loading = new cljs.core.Keyword(null,"reactions-loading","reactions-loading",-1859557973).cljs$core$IFn$_invoke$arity$1(entity_data);
var react_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(entity_data),"react"], 0));
var should_show_picker_QMARK_ = ((cljs.core.not(only_thumb_QMARK_))?((cljs.core.not(hide_picker))?(function (){var and__4115__auto__ = react_link;
if(cljs.core.truth_(and__4115__auto__)){
return (cljs.core.count(reactions_data) < reactions_max_count);
} else {
return and__4115__auto__;
}
})():false):false);
var is_mobile_QMARK_ = oc.web.lib.responsive.is_tablet_or_mobile_QMARK_();
return sablono.interpreter.interpret((cljs.core.truth_((function (){var or__4126__auto__ = cljs.core.seq(reactions_data);
if(or__4126__auto__){
return or__4126__auto__;
} else {
return should_show_picker_QMARK_;
}
})())?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.reactions","div.reactions",1975642674),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.reactions-list.group","div.reactions-list.group",1320215081),((cljs.core.seq(reactions_data))?(function (){var iter__4529__auto__ = (function oc$web$components$reactions$iter__45997(s__45998){
return (new cljs.core.LazySeq(null,(function (){
var s__45998__$1 = s__45998;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__45998__$1);
if(temp__5735__auto__){
var s__45998__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__45998__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__45998__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__46000 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__45999 = (0);
while(true){
if((i__45999 < size__4528__auto__)){
var idx = cljs.core._nth(c__4527__auto__,i__45999);
var reaction_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(reactions_data,idx);
var is_loading = oc.web.lib.utils.in_QMARK_(reactions_loading,new cljs.core.Keyword(null,"reaction","reaction",490869788).cljs$core$IFn$_invoke$arity$1(reaction_data));
var reacted = new cljs.core.Keyword(null,"reacted","reacted",523485502).cljs$core$IFn$_invoke$arity$1(reaction_data);
var reaction_authors = new cljs.core.Keyword(null,"authors","authors",2063018172).cljs$core$IFn$_invoke$arity$1(reaction_data);
var multiple_reaction_authors_QMARK_ = (cljs.core.count(reaction_authors) > (1));
var attribution_start = ((multiple_reaction_authors_QMARK_)?"Reactions":"Reaction");
var attribution_end = ((multiple_reaction_authors_QMARK_)?[clojure.string.join.cljs$core$IFn$_invoke$arity$2(", ",cljs.core.butlast(reaction_authors))," and ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.last(reaction_authors))].join(''):cljs.core.first(reaction_authors));
var reaction_attribution = ((cljs.core.empty_QMARK_(reaction_authors))?"":[attribution_start," by ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(attribution_end)].join(''));
var read_only_reaction = cljs.core.not(oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(reaction_data),"react",(cljs.core.truth_(reacted)?"DELETE":"PUT")], 0)));
var r = (cljs.core.truth_(is_loading)?cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([reaction_data,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"count","count",2139924085),(cljs.core.truth_(reacted)?(new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(reaction_data) - (1)):(new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(reaction_data) + (1))),new cljs.core.Keyword(null,"reacted","reacted",523485502),cljs.core.not(reacted)], null)], 0)):reaction_data);
cljs.core.chunk_append(b__46000,new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.reaction-btn.btn-reset","button.reaction-btn.btn-reset",-1177053637),new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"key","key",-1516042587),["reaction-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entity_data)),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(idx)].join(''),new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.class_set(cljs.core.PersistentArrayMap.createAsIfByAssoc([new cljs.core.Keyword(null,"reacted","reacted",523485502),new cljs.core.Keyword(null,"reacted","reacted",523485502).cljs$core$IFn$_invoke$arity$1(r),new cljs.core.Keyword(null,"can-react","can-react",283229018),(!(read_only_reaction)),new cljs.core.Keyword(null,"has-reactions","has-reactions",-1912854313),(new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(r) > (0)),new cljs.core.Keyword(null,"only-thumb","only-thumb",1824344116),only_thumb_QMARK_,oc.web.lib.utils.hide_class,true])),new cljs.core.Keyword(null,"on-mouse-leave","on-mouse-leave",-1864319528),((is_mobile_QMARK_)?null:((function (i__45999,reaction_data,is_loading,reacted,reaction_authors,multiple_reaction_authors_QMARK_,attribution_start,attribution_end,reaction_attribution,read_only_reaction,r,idx,c__4527__auto__,size__4528__auto__,b__46000,s__45998__$2,temp__5735__auto__,filtered_reactions,reactions_max_count,reactions_data,reactions_loading,react_link,should_show_picker_QMARK_,is_mobile_QMARK_,map__45993,map__45993__$1,entity_data,hide_picker,optional_activity_data,max_reactions,did_react_cb,only_thumb_QMARK_){
return (function (){
var this$ = this;
oc.web.lib.utils.remove_tooltips();

return $(this$).tooltip();
});})(i__45999,reaction_data,is_loading,reacted,reaction_authors,multiple_reaction_authors_QMARK_,attribution_start,attribution_end,reaction_attribution,read_only_reaction,r,idx,c__4527__auto__,size__4528__auto__,b__46000,s__45998__$2,temp__5735__auto__,filtered_reactions,reactions_max_count,reactions_data,reactions_loading,react_link,should_show_picker_QMARK_,is_mobile_QMARK_,map__45993,map__45993__$1,entity_data,hide_picker,optional_activity_data,max_reactions,did_react_cb,only_thumb_QMARK_))
),new cljs.core.Keyword(null,"title","title",636505583),reaction_attribution,new cljs.core.Keyword(null,"data-placement","data-placement",166529525),"top",new cljs.core.Keyword(null,"data-container","data-container",1473653353),"body",new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687),((is_mobile_QMARK_)?null:"tooltip"),new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (i__45999,reaction_data,is_loading,reacted,reaction_authors,multiple_reaction_authors_QMARK_,attribution_start,attribution_end,reaction_attribution,read_only_reaction,r,idx,c__4527__auto__,size__4528__auto__,b__46000,s__45998__$2,temp__5735__auto__,filtered_reactions,reactions_max_count,reactions_data,reactions_loading,react_link,should_show_picker_QMARK_,is_mobile_QMARK_,map__45993,map__45993__$1,entity_data,hide_picker,optional_activity_data,max_reactions,did_react_cb,only_thumb_QMARK_){
return (function (e){
if(((cljs.core.not(is_loading)) && ((!(read_only_reaction))))){
if(cljs.core.fn_QMARK_(did_react_cb)){
(did_react_cb.cljs$core$IFn$_invoke$arity$0 ? did_react_cb.cljs$core$IFn$_invoke$arity$0() : did_react_cb.call(null));
} else {
}

if(cljs.core.truth_(optional_activity_data)){
return oc.web.actions.comment.comment_reaction_toggle(optional_activity_data,entity_data,r,cljs.core.not(reacted));
} else {
return oc.web.actions.reaction.reaction_toggle(entity_data,r,cljs.core.not(reacted));
}
} else {
return null;
}
});})(i__45999,reaction_data,is_loading,reacted,reaction_authors,multiple_reaction_authors_QMARK_,attribution_start,attribution_end,reaction_attribution,read_only_reaction,r,idx,c__4527__auto__,size__4528__auto__,b__46000,s__45998__$2,temp__5735__auto__,filtered_reactions,reactions_max_count,reactions_data,reactions_loading,react_link,should_show_picker_QMARK_,is_mobile_QMARK_,map__45993,map__45993__$1,entity_data,hide_picker,optional_activity_data,max_reactions,did_react_cb,only_thumb_QMARK_))
], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.reaction","span.reaction",-435217168),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"has-count","has-count",1603629980),(new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(r) > (0)),new cljs.core.Keyword(null,"safari","safari",497115653),oc.shared.useragent.safari_QMARK_], null))], null),(cljs.core.truth_(only_thumb_QMARK_)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.thumb-up-icon","span.thumb-up-icon",314616728)], null):new cljs.core.Keyword(null,"reaction","reaction",490869788).cljs$core$IFn$_invoke$arity$1(r))], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.count","div.count",1251198110),new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(r)], null)], null));

var G__46017 = (i__45999 + (1));
i__45999 = G__46017;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__46000),oc$web$components$reactions$iter__45997(cljs.core.chunk_rest(s__45998__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__46000),null);
}
} else {
var idx = cljs.core.first(s__45998__$2);
var reaction_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(reactions_data,idx);
var is_loading = oc.web.lib.utils.in_QMARK_(reactions_loading,new cljs.core.Keyword(null,"reaction","reaction",490869788).cljs$core$IFn$_invoke$arity$1(reaction_data));
var reacted = new cljs.core.Keyword(null,"reacted","reacted",523485502).cljs$core$IFn$_invoke$arity$1(reaction_data);
var reaction_authors = new cljs.core.Keyword(null,"authors","authors",2063018172).cljs$core$IFn$_invoke$arity$1(reaction_data);
var multiple_reaction_authors_QMARK_ = (cljs.core.count(reaction_authors) > (1));
var attribution_start = ((multiple_reaction_authors_QMARK_)?"Reactions":"Reaction");
var attribution_end = ((multiple_reaction_authors_QMARK_)?[clojure.string.join.cljs$core$IFn$_invoke$arity$2(", ",cljs.core.butlast(reaction_authors))," and ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.last(reaction_authors))].join(''):cljs.core.first(reaction_authors));
var reaction_attribution = ((cljs.core.empty_QMARK_(reaction_authors))?"":[attribution_start," by ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(attribution_end)].join(''));
var read_only_reaction = cljs.core.not(oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(reaction_data),"react",(cljs.core.truth_(reacted)?"DELETE":"PUT")], 0)));
var r = (cljs.core.truth_(is_loading)?cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([reaction_data,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"count","count",2139924085),(cljs.core.truth_(reacted)?(new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(reaction_data) - (1)):(new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(reaction_data) + (1))),new cljs.core.Keyword(null,"reacted","reacted",523485502),cljs.core.not(reacted)], null)], 0)):reaction_data);
return cljs.core.cons(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.reaction-btn.btn-reset","button.reaction-btn.btn-reset",-1177053637),new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"key","key",-1516042587),["reaction-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entity_data)),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(idx)].join(''),new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.class_set(cljs.core.PersistentArrayMap.createAsIfByAssoc([new cljs.core.Keyword(null,"reacted","reacted",523485502),new cljs.core.Keyword(null,"reacted","reacted",523485502).cljs$core$IFn$_invoke$arity$1(r),new cljs.core.Keyword(null,"can-react","can-react",283229018),(!(read_only_reaction)),new cljs.core.Keyword(null,"has-reactions","has-reactions",-1912854313),(new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(r) > (0)),new cljs.core.Keyword(null,"only-thumb","only-thumb",1824344116),only_thumb_QMARK_,oc.web.lib.utils.hide_class,true])),new cljs.core.Keyword(null,"on-mouse-leave","on-mouse-leave",-1864319528),((is_mobile_QMARK_)?null:((function (reaction_data,is_loading,reacted,reaction_authors,multiple_reaction_authors_QMARK_,attribution_start,attribution_end,reaction_attribution,read_only_reaction,r,idx,s__45998__$2,temp__5735__auto__,filtered_reactions,reactions_max_count,reactions_data,reactions_loading,react_link,should_show_picker_QMARK_,is_mobile_QMARK_,map__45993,map__45993__$1,entity_data,hide_picker,optional_activity_data,max_reactions,did_react_cb,only_thumb_QMARK_){
return (function (){
var this$ = this;
oc.web.lib.utils.remove_tooltips();

return $(this$).tooltip();
});})(reaction_data,is_loading,reacted,reaction_authors,multiple_reaction_authors_QMARK_,attribution_start,attribution_end,reaction_attribution,read_only_reaction,r,idx,s__45998__$2,temp__5735__auto__,filtered_reactions,reactions_max_count,reactions_data,reactions_loading,react_link,should_show_picker_QMARK_,is_mobile_QMARK_,map__45993,map__45993__$1,entity_data,hide_picker,optional_activity_data,max_reactions,did_react_cb,only_thumb_QMARK_))
),new cljs.core.Keyword(null,"title","title",636505583),reaction_attribution,new cljs.core.Keyword(null,"data-placement","data-placement",166529525),"top",new cljs.core.Keyword(null,"data-container","data-container",1473653353),"body",new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687),((is_mobile_QMARK_)?null:"tooltip"),new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (reaction_data,is_loading,reacted,reaction_authors,multiple_reaction_authors_QMARK_,attribution_start,attribution_end,reaction_attribution,read_only_reaction,r,idx,s__45998__$2,temp__5735__auto__,filtered_reactions,reactions_max_count,reactions_data,reactions_loading,react_link,should_show_picker_QMARK_,is_mobile_QMARK_,map__45993,map__45993__$1,entity_data,hide_picker,optional_activity_data,max_reactions,did_react_cb,only_thumb_QMARK_){
return (function (e){
if(((cljs.core.not(is_loading)) && ((!(read_only_reaction))))){
if(cljs.core.fn_QMARK_(did_react_cb)){
(did_react_cb.cljs$core$IFn$_invoke$arity$0 ? did_react_cb.cljs$core$IFn$_invoke$arity$0() : did_react_cb.call(null));
} else {
}

if(cljs.core.truth_(optional_activity_data)){
return oc.web.actions.comment.comment_reaction_toggle(optional_activity_data,entity_data,r,cljs.core.not(reacted));
} else {
return oc.web.actions.reaction.reaction_toggle(entity_data,r,cljs.core.not(reacted));
}
} else {
return null;
}
});})(reaction_data,is_loading,reacted,reaction_authors,multiple_reaction_authors_QMARK_,attribution_start,attribution_end,reaction_attribution,read_only_reaction,r,idx,s__45998__$2,temp__5735__auto__,filtered_reactions,reactions_max_count,reactions_data,reactions_loading,react_link,should_show_picker_QMARK_,is_mobile_QMARK_,map__45993,map__45993__$1,entity_data,hide_picker,optional_activity_data,max_reactions,did_react_cb,only_thumb_QMARK_))
], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.reaction","span.reaction",-435217168),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"has-count","has-count",1603629980),(new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(r) > (0)),new cljs.core.Keyword(null,"safari","safari",497115653),oc.shared.useragent.safari_QMARK_], null))], null),(cljs.core.truth_(only_thumb_QMARK_)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.thumb-up-icon","span.thumb-up-icon",314616728)], null):new cljs.core.Keyword(null,"reaction","reaction",490869788).cljs$core$IFn$_invoke$arity$1(r))], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.count","div.count",1251198110),new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(r)], null)], null),oc$web$components$reactions$iter__45997(cljs.core.rest(s__45998__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(cljs.core.range.cljs$core$IFn$_invoke$arity$1(cljs.core.count(reactions_data)));
})():null),(cljs.core.truth_(should_show_picker_QMARK_)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.reaction-btn.btn-reset.can-react.reaction-picker","button.reaction-btn.btn-reset.can-react.reaction-picker",-371231247),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"key","key",-1516042587),["reaction-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entity_data)),"-picker"].join(''),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.reactions","show-picker","oc.web.components.reactions/show-picker",-1564653293).cljs$core$IFn$_invoke$arity$1(s),cljs.core.not(cljs.core.deref(new cljs.core.Keyword("oc.web.components.reactions","show-picker","oc.web.components.reactions/show-picker",-1564653293).cljs$core$IFn$_invoke$arity$1(s))));
}),new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687),((is_mobile_QMARK_)?null:"tooltip"),new cljs.core.Keyword(null,"data-placement","data-placement",166529525),"top",new cljs.core.Keyword(null,"data-container","data-container",1473653353),"body",new cljs.core.Keyword(null,"title","title",636505583),"Pick a reaction"], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.reaction","span.reaction",-435217168)], null)], null):null)], null),(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.reactions","show-picker","oc.web.components.reactions/show-picker",-1564653293).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.reactions-picker-container","div.reactions-picker-container",45783127),((oc.web.lib.responsive.is_tablet_or_mobile_QMARK_())?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.dismiss-mobile-picker","button.mlb-reset.dismiss-mobile-picker",-2004465895),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.reactions","show-picker","oc.web.components.reactions/show-picker",-1564653293).cljs$core$IFn$_invoke$arity$1(s),false);
})], null),"Cancel"], null):null),((oc.web.lib.utils.is_test_env_QMARK_())?null:oc.web.lib.react_utils.build(module$node_modules$emoji_mart$dist$index.Picker,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"native","native",-613060878),true,new cljs.core.Keyword(null,"autoFocus","autoFocus",-552622425),true,new cljs.core.Keyword(null,"onClick","onClick",-1991238530),(function (emoji,event){
if(oc.web.utils.reaction.can_pick_reaction_QMARK_(goog.object.get(emoji,"native"),reactions_data)){
if(cljs.core.fn_QMARK_(did_react_cb)){
(did_react_cb.cljs$core$IFn$_invoke$arity$0 ? did_react_cb.cljs$core$IFn$_invoke$arity$0() : did_react_cb.call(null));
} else {
}

if(cljs.core.truth_(optional_activity_data)){
oc.web.actions.comment.react_from_picker(optional_activity_data,entity_data,goog.object.get(emoji,"native"));
} else {
oc.web.actions.reaction.react_from_picker(entity_data,goog.object.get(emoji,"native"));
}
} else {
}

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.reactions","show-picker","oc.web.components.reactions/show-picker",-1564653293).cljs$core$IFn$_invoke$arity$1(s),false);
})], null)))], null):null)], null):null));
}),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.reactions","show-picker","oc.web.components.reactions/show-picker",-1564653293)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.reactions","mounted","oc.web.components.reactions/mounted",972108798)),oc.web.mixins.ui.refresh_tooltips_mixin,oc.web.mixins.ui.on_click_out.cljs$core$IFn$_invoke$arity$1((function (p1__45990_SHARP_){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.reactions","show-picker","oc.web.components.reactions/show-picker",-1564653293).cljs$core$IFn$_invoke$arity$1(p1__45990_SHARP_),false);
}))], null),"reactions");

//# sourceMappingURL=oc.web.components.reactions.js.map
