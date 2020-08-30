goog.provide('oc.web.components.ui.comments_summary');
oc.web.components.ui.comments_summary.get_author_name = (function oc$web$components$ui$comments_summary$get_author_name(author){
if(cljs.core.truth_(new cljs.core.Keyword(null,"author?","author?",-1083349935).cljs$core$IFn$_invoke$arity$1(author))){
return "you";
} else {
return new cljs.core.Keyword(null,"short-name","short-name",-1767085022).cljs$core$IFn$_invoke$arity$1(author);
}
});
oc.web.components.ui.comments_summary.comment_summary_string = (function oc$web$components$ui$comments_summary$comment_summary_string(authors){
var G__44489 = cljs.core.count(authors);
switch (G__44489) {
case (0):
return "";

break;
case (1):
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(cuerdas.core.capital(oc.web.components.ui.comments_summary.get_author_name(cljs.core.first(authors))))," commented"].join('');

break;
case (2):
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(cuerdas.core.capital(oc.web.components.ui.comments_summary.get_author_name(cljs.core.first(authors))))," and ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.components.ui.comments_summary.get_author_name(cljs.core.second(authors)))," commented"].join('');

break;
case (3):
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(cuerdas.core.capital(oc.web.components.ui.comments_summary.get_author_name(cljs.core.first(authors)))),", ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.components.ui.comments_summary.get_author_name(cljs.core.second(authors)))," and ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.components.ui.comments_summary.get_author_name(cljs.core.nth.cljs$core$IFn$_invoke$arity$2(authors,(2))))," commented"].join('');

break;
default:
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(cuerdas.core.capital(oc.web.components.ui.comments_summary.get_author_name(cljs.core.first(authors)))),", ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.components.ui.comments_summary.get_author_name(cljs.core.second(authors)))," and ",cljs.core.str.cljs$core$IFn$_invoke$arity$1((cljs.core.count(authors) - (2)))," others commented"].join('');

}
});
oc.web.components.ui.comments_summary.max_face_pile = (3);
oc.web.components.ui.comments_summary.comments_summary = rum.core.build_defc((function (p__44491){
var map__44492 = p__44491;
var map__44492__$1 = (((((!((map__44492 == null))))?(((((map__44492.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__44492.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__44492):map__44492);
var hide_face_pile_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__44492__$1,new cljs.core.Keyword(null,"hide-face-pile?","hide-face-pile?",-445239907));
var add_comment_focus_prefix = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__44492__$1,new cljs.core.Keyword(null,"add-comment-focus-prefix","add-comment-focus-prefix",1635349699));
var show_bubble_icon_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__44492__$1,new cljs.core.Keyword(null,"show-bubble-icon?","show-bubble-icon?",-1751785051));
var hide_label_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__44492__$1,new cljs.core.Keyword(null,"hide-label?","hide-label?",628080329));
var entry_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__44492__$1,new cljs.core.Keyword(null,"entry-data","entry-data",1970939662));
var current_activity_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__44492__$1,new cljs.core.Keyword(null,"current-activity-id","current-activity-id",-930108529));
var comments_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__44492__$1,new cljs.core.Keyword(null,"comments-data","comments-data",1871210833));
var publisher_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__44492__$1,new cljs.core.Keyword(null,"publisher?","publisher?",30448149));
var new_comments_count = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__44492__$1,new cljs.core.Keyword(null,"new-comments-count","new-comments-count",46784695));
var entry_comments = cljs.core.get.cljs$core$IFn$_invoke$arity$2(comments_data,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data));
var sorted_comments = new cljs.core.Keyword(null,"sorted-comments","sorted-comments",1988882718).cljs$core$IFn$_invoke$arity$1(entry_comments);
var comments_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(entry_data),"comments"], 0));
var comments_loaded_QMARK_ = cljs.core.seq(sorted_comments);
var unwrapped_comments = cljs.core.vec(cljs.core.mapcat.cljs$core$IFn$_invoke$arity$variadic((function (p1__44490_SHARP_){
return cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [p1__44490_SHARP_], null),new cljs.core.Keyword(null,"thread-children","thread-children",78675219).cljs$core$IFn$_invoke$arity$1(p1__44490_SHARP_));
}),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([sorted_comments], 0)));
var comments_authors = ((comments_loaded_QMARK_)?cljs.core.vec(cljs.core.map.cljs$core$IFn$_invoke$arity$2(cljs.core.first,cljs.core.vals(cljs.core.group_by(new cljs.core.Keyword(null,"user-id","user-id",-206822291),cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"author","author",2111686192),cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"created-at","created-at",-89248644),unwrapped_comments)))))):cljs.core.reverse(new cljs.core.Keyword(null,"authors","authors",2063018172).cljs$core$IFn$_invoke$arity$1(comments_link)));
var comments_count = ((comments_loaded_QMARK_)?cljs.core.count(unwrapped_comments):new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(comments_link));
var face_pile_count = (cljs.core.truth_(hide_face_pile_QMARK_)?(0):(function (){var x__4217__auto__ = oc.web.components.ui.comments_summary.max_face_pile;
var y__4218__auto__ = cljs.core.count(comments_authors);
return ((x__4217__auto__ < y__4218__auto__) ? x__4217__auto__ : y__4218__auto__);
})());
var is_mobile_QMARK_ = oc.web.lib.responsive.is_mobile_size_QMARK_();
var faces_to_render = cljs.core.take.cljs$core$IFn$_invoke$arity$2(oc.web.components.ui.comments_summary.max_face_pile,comments_authors);
var face_pile_width = (((face_pile_count > (0)))?(cljs.core.truth_(is_mobile_QMARK_)?((8) + ((12) * face_pile_count)):((10) + ((12) * face_pile_count))):(0));
var show_new_tag_QMARK_ = (new_comments_count > (0));
return sablono.interpreter.interpret((cljs.core.truth_(comments_count)?new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.is-comments","div.is-comments",-195023501),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"class","class",-2030961996),((show_new_tag_QMARK_)?"has-new-comments":null),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (e){
if(cljs.core.seq(current_activity_id)){
var temp__5735__auto___44506 = document.querySelector("div.add-comment");
if(cljs.core.truth_(temp__5735__auto___44506)){
var add_comment_div_44507 = temp__5735__auto___44506;
add_comment_div_44507.scrollIntoView(({"behavior": "smooth", "block": "center"}));
} else {
}
} else {
oc.web.actions.nav_sidebar.open_post_modal.cljs$core$IFn$_invoke$arity$2(entry_data,true);
}

return oc.web.actions.comment.add_comment_focus(oc.web.utils.comment.add_comment_focus_value.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([add_comment_focus_prefix,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data)], 0)));
}),new cljs.core.Keyword(null,"data-placement","data-placement",166529525),"top",new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687),(cljs.core.truth_(is_mobile_QMARK_)?null:"tooltip"),new cljs.core.Keyword(null,"data-container","data-container",1473653353),"body",new cljs.core.Keyword(null,"title","title",636505583),(((comments_count === (0)))?"Add a comment":((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new_comments_count,comments_count))?[cljs.core.str.cljs$core$IFn$_invoke$arity$1(new_comments_count)," new comment",((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new_comments_count,(1)))?"s":null)].join(''):[cljs.core.str.cljs$core$IFn$_invoke$arity$1(comments_count)," comment",((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(comments_count,(1)))?"s":null),(((new_comments_count > (0)))?[", ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new_comments_count)," new"].join(''):null)].join('')
))], null),(cljs.core.truth_(show_bubble_icon_QMARK_)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.is-comments-bubble","div.is-comments-bubble",25815564)], null):null),((((cljs.core.not(hide_face_pile_QMARK_)) && (((cljs.core.not(hide_label_QMARK_)) || ((!((comments_count === (0)))))))))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.is-comments-authors.group","div.is-comments-authors.group",1883571492),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"width","width",-384071477),[cljs.core.str.cljs$core$IFn$_invoke$arity$1(face_pile_width),"px"].join('')], null),new cljs.core.Keyword(null,"class","class",-2030961996),(((cljs.core.count(faces_to_render) > (1)))?"show-border":null)], null),(function (){var iter__4529__auto__ = (function oc$web$components$ui$comments_summary$iter__44494(s__44495){
return (new cljs.core.LazySeq(null,(function (){
var s__44495__$1 = s__44495;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__44495__$1);
if(temp__5735__auto__){
var s__44495__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__44495__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__44495__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__44497 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__44496 = (0);
while(true){
if((i__44496 < size__4528__auto__)){
var user_data = cljs.core._nth(c__4527__auto__,i__44496);
cljs.core.chunk_append(b__44497,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.is-comments-author","div.is-comments-author",582567860),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),["entry-comment-author-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data)),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user_data))].join('')], null),(function (){var G__44498 = user_data;
var G__44499 = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"tooltip?","tooltip?",-642753154),(!(oc.web.lib.responsive.is_tablet_or_mobile_QMARK_()))], null);
return (oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$2 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$2(G__44498,G__44499) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,G__44498,G__44499));
})()], null));

var G__44508 = (i__44496 + (1));
i__44496 = G__44508;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__44497),oc$web$components$ui$comments_summary$iter__44494(cljs.core.chunk_rest(s__44495__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__44497),null);
}
} else {
var user_data = cljs.core.first(s__44495__$2);
return cljs.core.cons(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.is-comments-author","div.is-comments-author",582567860),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),["entry-comment-author-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data)),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user_data))].join('')], null),(function (){var G__44500 = user_data;
var G__44501 = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"tooltip?","tooltip?",-642753154),(!(oc.web.lib.responsive.is_tablet_or_mobile_QMARK_()))], null);
return (oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$2 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$2(G__44500,G__44501) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,G__44500,G__44501));
})()], null),oc$web$components$ui$comments_summary$iter__44494(cljs.core.rest(s__44495__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(faces_to_render);
})()], null):null),(cljs.core.truth_((function (){var or__4126__auto__ = show_bubble_icon_QMARK_;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return ((cljs.core.not(hide_label_QMARK_)) && ((comments_count > (0))));
}
})())?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.is-comments-summary","div.is-comments-summary",310721424),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.class_set(cljs.core.PersistentArrayMap.createAsIfByAssoc([["comments-count-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data))].join(''),true,new cljs.core.Keyword(null,"add-a-comment","add-a-comment",440941314),(!((comments_count > (0))))]))], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.is-comments-summary-inner","div.is-comments-summary-inner",-778217352),[cljs.core.str.cljs$core$IFn$_invoke$arity$1(comments_count),((show_new_tag_QMARK_)?(cljs.core.truth_(hide_label_QMARK_)?null:[" new comment",((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new_comments_count,(1)))?"s":null)].join('')):(cljs.core.truth_(hide_label_QMARK_)?null:[" comment",((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(comments_count,(1)))?"s":null)].join('')))].join('')], null)], null):null)], null):null));
}),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$], null),"comments-summary");
oc.web.components.ui.comments_summary.foc_comments_summary = rum.core.build_defc((function (p__44502){
var map__44503 = p__44502;
var map__44503__$1 = (((((!((map__44503 == null))))?(((((map__44503.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__44503.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__44503):map__44503);
var entry_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__44503__$1,new cljs.core.Keyword(null,"entry-data","entry-data",1970939662));
var add_comment_focus_prefix = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__44503__$1,new cljs.core.Keyword(null,"add-comment-focus-prefix","add-comment-focus-prefix",1635349699));
var current_activity_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__44503__$1,new cljs.core.Keyword(null,"current-activity-id","current-activity-id",-930108529));
var new_comments_count = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__44503__$1,new cljs.core.Keyword(null,"new-comments-count","new-comments-count",46784695));
var sorted_comments = oc.web.dispatcher.activity_sorted_comments_data.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data));
var comments_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(entry_data),"comments"], 0));
var comments_loaded_QMARK_ = cljs.core.seq(sorted_comments);
var comments_count = (function (){var x__4214__auto__ = (0);
var y__4215__auto__ = (cljs.core.truth_(sorted_comments)?cljs.core.count(sorted_comments):new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(comments_link));
return ((x__4214__auto__ > y__4215__auto__) ? x__4214__auto__ : y__4215__auto__);
})();
var is_mobile_QMARK_ = oc.web.lib.responsive.is_mobile_size_QMARK_();
return sablono.interpreter.interpret((cljs.core.truth_(comments_count)?new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.is-comments","div.is-comments",-195023501),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (e){
if(cljs.core.seq(current_activity_id)){
var temp__5735__auto___44512 = document.querySelector("div.add-comment");
if(cljs.core.truth_(temp__5735__auto___44512)){
var add_comment_div_44513 = temp__5735__auto___44512;
add_comment_div_44513.scrollIntoView(({"behavior": "smooth", "block": "center"}));
} else {
}
} else {
oc.web.actions.nav_sidebar.open_post_modal.cljs$core$IFn$_invoke$arity$2(entry_data,true);
}

return oc.web.actions.comment.add_comment_focus(oc.web.utils.comment.add_comment_focus_value.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([add_comment_focus_prefix,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data)], 0)));
}),new cljs.core.Keyword(null,"data-placement","data-placement",166529525),"top",new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687),(cljs.core.truth_(is_mobile_QMARK_)?null:"tooltip"),new cljs.core.Keyword(null,"data-container","data-container",1473653353),"body",new cljs.core.Keyword(null,"title","title",636505583),[cljs.core.str.cljs$core$IFn$_invoke$arity$1(comments_count)," comment",((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(comments_count,(1)))?"s":null)].join(''),new cljs.core.Keyword(null,"class","class",-2030961996),(((new_comments_count > (0)))?"foc-new-comments":null)], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.is-comments-bubble","div.is-comments-bubble",25815564)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.is-comments-summary","div.is-comments-summary",310721424),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.class_set(cljs.core.PersistentArrayMap.createAsIfByAssoc([["comments-count-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data))].join(''),true,new cljs.core.Keyword(null,"add-a-comment","add-a-comment",440941314),(comments_count === (0))]))], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.is-comments-summary-inner","div.is-comments-summary-inner",-778217352),comments_count,new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.new-comments-count","span.new-comments-count",931919937),"(",new_comments_count," NEW)"], null)], null)], null)], null):null));
}),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$], null),"foc-comments-summary");

//# sourceMappingURL=oc.web.components.ui.comments_summary.js.map
