goog.provide('oc.web.components.ui.post_authorship');
oc.web.components.ui.post_authorship.post_authorship = rum.core.build_defc((function (p__39715){
var map__39716 = p__39715;
var map__39716__$1 = (((((!((map__39716 == null))))?(((((map__39716.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__39716.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__39716):map__39716);
var map__39717 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39716__$1,new cljs.core.Keyword(null,"activity-data","activity-data",1293689136));
var map__39717__$1 = (((((!((map__39717 == null))))?(((((map__39717.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__39717.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__39717):map__39717);
var activity_data = map__39717__$1;
var publisher = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39717__$1,new cljs.core.Keyword(null,"publisher","publisher",-153364540));
var author = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39717__$1,new cljs.core.Keyword(null,"author","author",2111686192));
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39717__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var board_name = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39717__$1,new cljs.core.Keyword(null,"board-name","board-name",-677515056));
var board_slug = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39717__$1,new cljs.core.Keyword(null,"board-slug","board-slug",99003663));
var board_access = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39717__$1,new cljs.core.Keyword(null,"board-access","board-access",1233510317));
var board_uuid = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39717__$1,new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127));
var user_avatar_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39716__$1,new cljs.core.Keyword(null,"user-avatar?","user-avatar?",-1162947881));
var user_hover_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39716__$1,new cljs.core.Keyword(null,"user-hover?","user-hover?",-1460761243));
var board_hover_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39716__$1,new cljs.core.Keyword(null,"board-hover?","board-hover?",1064995646));
var leave_delay_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39716__$1,new cljs.core.Keyword(null,"leave-delay?","leave-delay?",654972741));
var activity_board_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39716__$1,new cljs.core.Keyword(null,"activity-board?","activity-board?",-1568829907));
var current_user_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39716__$1,new cljs.core.Keyword(null,"current-user-id","current-user-id",-2013633451));
var hide_last_name_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39716__$1,new cljs.core.Keyword(null,"hide-last-name?","hide-last-name?",-1527823457));
var published_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,"published");
var author_data = ((published_QMARK_)?publisher:((cljs.core.sequential_QMARK_(author))?cljs.core.first(author):((cljs.core.map_QMARK_(author))?author:new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"name","name",1843675177),"Anonymous"], null)
)));
var show_board_QMARK_ = (function (){var and__4115__auto__ = activity_board_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return board_slug;
} else {
return and__4115__auto__;
}
})();
return React.createElement("div",({"className": "post-authorship"}),(function (){var attrs39720 = (cljs.core.truth_(user_hover_QMARK_)?(function (){var G__39721 = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"user-data","user-data",2143823568),author_data,new cljs.core.Keyword(null,"current-user-id","current-user-id",-2013633451),current_user_id,new cljs.core.Keyword(null,"hide-last-name?","hide-last-name?",-1527823457),hide_last_name_QMARK_,new cljs.core.Keyword(null,"leave-delay?","leave-delay?",654972741),leave_delay_QMARK_], null);
return (oc.web.components.ui.info_hover_views.user_info_hover.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.info_hover_views.user_info_hover.cljs$core$IFn$_invoke$arity$1(G__39721) : oc.web.components.ui.info_hover_views.user_info_hover.call(null,G__39721));
})():null);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs39720))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["user-hover-container"], null)], null),attrs39720], 0))):({"className": "user-hover-container"})),((cljs.core.map_QMARK_(attrs39720))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret((cljs.core.truth_(user_avatar_QMARK_)?(oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1(author_data) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,author_data)):null)),React.createElement("a",({"href": (cljs.core.truth_(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(author_data))?oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(author_data)):null), "onClick": (function (p1__39713_SHARP_){
if(cljs.core.truth_(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(author_data))){
oc.web.lib.utils.event_stop(p1__39713_SHARP_);

return oc.web.actions.nav_sidebar.nav_to_author_BANG_.cljs$core$IFn$_invoke$arity$3(p1__39713_SHARP_,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(author_data),oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(author_data)));
} else {
return null;
}
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["publisher-name",oc.web.lib.utils.hide_class], null))}),sablono.interpreter.interpret(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(author_data)))], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs39720),sablono.interpreter.interpret((cljs.core.truth_(user_avatar_QMARK_)?(oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1(author_data) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,author_data)):null)),React.createElement("a",({"href": (cljs.core.truth_(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(author_data))?oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(author_data)):null), "onClick": (function (p1__39713_SHARP_){
if(cljs.core.truth_(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(author_data))){
oc.web.lib.utils.event_stop(p1__39713_SHARP_);

return oc.web.actions.nav_sidebar.nav_to_author_BANG_.cljs$core$IFn$_invoke$arity$3(p1__39713_SHARP_,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(author_data),oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(author_data)));
} else {
return null;
}
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["publisher-name",oc.web.lib.utils.hide_class], null))}),sablono.interpreter.interpret(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(author_data)))], null)));
})(),sablono.interpreter.interpret((cljs.core.truth_(show_board_QMARK_)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.in","span.in",-1803293663),"in "], null):null)),sablono.interpreter.interpret((cljs.core.truth_(show_board_QMARK_)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.board-hover-container","div.board-hover-container",1603755124),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a.board-name","a.board-name",-1368074252),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.hide_class,new cljs.core.Keyword(null,"href","href",-793805698),oc.web.urls.board.cljs$core$IFn$_invoke$arity$1(board_slug),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (p1__39714_SHARP_){
oc.web.lib.utils.event_stop(p1__39714_SHARP_);

return oc.web.actions.nav_sidebar.nav_to_url_BANG_.cljs$core$IFn$_invoke$arity$3(p1__39714_SHARP_,new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(activity_data),oc.web.urls.board.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(activity_data)));
})], null),[cljs.core.str.cljs$core$IFn$_invoke$arity$1(board_name),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(board_access,"private"))?" (private)":null),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(board_access,"public"))?" (public)":null)].join('')], null)], null):null)));
}),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$], null),"post-authorship");

//# sourceMappingURL=oc.web.components.ui.post_authorship.js.map
