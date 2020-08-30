goog.provide('oc.web.components.stream_reply_item');
var module$node_modules$emoji_mart$dist$index=shadow.js.require("module$node_modules$emoji_mart$dist$index", {});
oc.web.components.stream_reply_item.delete_clicked = (function oc$web$components$stream_reply_item$delete_clicked(e,entry_data,comment_data,clear_cell_measure_cb){
var alert_data = new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"icon","icon",1679606541),"/img/ML/trash.svg",new cljs.core.Keyword(null,"action","action",-811238024),"delete-comment",new cljs.core.Keyword(null,"message","message",-406056002),"Delete this comment?",new cljs.core.Keyword(null,"link-button-title","link-button-title",-812305976),"No",new cljs.core.Keyword(null,"link-button-cb","link-button-cb",559710301),(function (){
return oc.web.components.ui.alert_modal.hide_alert();
}),new cljs.core.Keyword(null,"solid-button-style","solid-button-style",-723792244),new cljs.core.Keyword(null,"red","red",-969428204),new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"Yes",new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),(function (_){
oc.web.actions.comment.delete_comment(entry_data,comment_data);

oc.web.components.ui.alert_modal.hide_alert();

return (clear_cell_measure_cb.cljs$core$IFn$_invoke$arity$0 ? clear_cell_measure_cb.cljs$core$IFn$_invoke$arity$0() : clear_cell_measure_cb.call(null));
})], null);
return oc.web.components.ui.alert_modal.show_alert(alert_data);
});
oc.web.components.stream_reply_item.finish_edit = (function oc$web$components$stream_reply_item$finish_edit(s,clear_cell_measure_cb){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.stream-reply-item","editing?","oc.web.components.stream-reply-item/editing?",-1549995393).cljs$core$IFn$_invoke$arity$1(s),false);

return (clear_cell_measure_cb.cljs$core$IFn$_invoke$arity$0 ? clear_cell_measure_cb.cljs$core$IFn$_invoke$arity$0() : clear_cell_measure_cb.call(null));
});
oc.web.components.stream_reply_item.start_edit = (function oc$web$components$stream_reply_item$start_edit(s,entry_data,comment_data,clear_cell_measure_cb){
oc.web.actions.comment.edit_comment(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data),comment_data);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.stream-reply-item","show-more-menu","oc.web.components.stream-reply-item/show-more-menu",900951581).cljs$core$IFn$_invoke$arity$1(s),null);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.stream-reply-item","editing?","oc.web.components.stream-reply-item/editing?",-1549995393).cljs$core$IFn$_invoke$arity$1(s),true);

return (clear_cell_measure_cb.cljs$core$IFn$_invoke$arity$0 ? clear_cell_measure_cb.cljs$core$IFn$_invoke$arity$0() : clear_cell_measure_cb.call(null));
});
oc.web.components.stream_reply_item.quoted_reply_header = (function oc$web$components$stream_reply_item$quoted_reply_header(comment_data){
return ["<span class=\"oc-replying-to\" contenteditable=\"false\">\u21A9\uFE0E Replying to ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"author","author",2111686192).cljs$core$IFn$_invoke$arity$1(comment_data))),"</span><br>"].join('');
});
oc.web.components.stream_reply_item.reply_to = (function oc$web$components$stream_reply_item$reply_to(comment_data,add_comment_focus_key){
return oc.web.actions.comment.reply_to.cljs$core$IFn$_invoke$arity$variadic(add_comment_focus_key,[oc.web.components.stream_reply_item.quoted_reply_header(comment_data),cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(comment_data))].join(''),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([true], 0));
});
oc.web.components.stream_reply_item.emoji_picker = rum.core.build_defc((function (p__46446){
var map__46447 = p__46446;
var map__46447__$1 = (((((!((map__46447 == null))))?(((((map__46447.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46447.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46447):map__46447);
var add_emoji_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46447__$1,new cljs.core.Keyword(null,"add-emoji-cb","add-emoji-cb",-375710574));
var dismiss_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46447__$1,new cljs.core.Keyword(null,"dismiss-cb","dismiss-cb",-1282537857));
return React.createElement("div",({"className": "emoji-picker-container"}),React.createElement("button",({"onClick": dismiss_cb, "className": "mlb-reset close-bt"}),"Cancel"),sablono.interpreter.interpret(oc.web.lib.react_utils.build(module$node_modules$emoji_mart$dist$index.Picker,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"native","native",-613060878),true,new cljs.core.Keyword(null,"autoFocus","autoFocus",-552622425),true,new cljs.core.Keyword(null,"onClick","onClick",-1991238530),(function (emoji,_){
return (add_emoji_cb.cljs$core$IFn$_invoke$arity$1 ? add_emoji_cb.cljs$core$IFn$_invoke$arity$1(emoji) : add_emoji_cb.call(null,emoji));
})], null))));
}),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$,(cljs.core.truth_(oc.web.lib.responsive.is_mobile_size_QMARK_())?oc.web.mixins.ui.no_scroll_mixin:null)], null),"emoji-picker");
oc.web.components.stream_reply_item.emoji_picker_container = (function oc$web$components$stream_reply_item$emoji_picker_container(s,entry_data,reply_data,seen_reply_cb){
var showing_picker_QMARK_ = ((cljs.core.seq(cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-reply-item","show-picker","oc.web.components.stream-reply-item/show-picker",-1615000722).cljs$core$IFn$_invoke$arity$1(s)))) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-reply-item","show-picker","oc.web.components.stream-reply-item/show-picker",-1615000722).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(reply_data))));
if(showing_picker_QMARK_){
var G__46449 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"dismiss-cb","dismiss-cb",-1282537857),(function (){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.stream-reply-item","show-picker","oc.web.components.stream-reply-item/show-picker",-1615000722).cljs$core$IFn$_invoke$arity$1(s),null);
}),new cljs.core.Keyword(null,"add-emoji-cb","add-emoji-cb",-375710574),(function (emoji){
if(oc.web.utils.reaction.can_pick_reaction_QMARK_(goog.object.get(emoji,"native"),new cljs.core.Keyword(null,"reactions","reactions",2029850654).cljs$core$IFn$_invoke$arity$1(reply_data))){
var G__46450_46548 = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(reply_data);
(seen_reply_cb.cljs$core$IFn$_invoke$arity$1 ? seen_reply_cb.cljs$core$IFn$_invoke$arity$1(G__46450_46548) : seen_reply_cb.call(null,G__46450_46548));

oc.web.actions.comment.react_from_picker(entry_data,reply_data,goog.object.get(emoji,"native"));
} else {
}

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.stream-reply-item","show-picker","oc.web.components.stream-reply-item/show-picker",-1615000722).cljs$core$IFn$_invoke$arity$1(s),null);
})], null);
return (oc.web.components.stream_reply_item.emoji_picker.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.stream_reply_item.emoji_picker.cljs$core$IFn$_invoke$arity$1(G__46449) : oc.web.components.stream_reply_item.emoji_picker.call(null,G__46449));
} else {
return null;
}
});
oc.web.components.stream_reply_item.reply_comment = rum.core.build_defcs((function (s,p__46454){
var map__46455 = p__46454;
var map__46455__$1 = (((((!((map__46455 == null))))?(((((map__46455.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46455.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46455):map__46455);
var comment_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46455__$1,new cljs.core.Keyword(null,"comment-data","comment-data",736267705));
var emoji_picker = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46455__$1,new cljs.core.Keyword(null,"emoji-picker","emoji-picker",-447764229));
var did_react_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46455__$1,new cljs.core.Keyword(null,"did-react-cb","did-react-cb",1627613154));
var is_mobile_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46455__$1,new cljs.core.Keyword(null,"is-mobile?","is-mobile?",2146205507));
var react_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46455__$1,new cljs.core.Keyword(null,"react-cb","react-cb",-2110368957));
var replying_to = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46455__$1,new cljs.core.Keyword(null,"replying-to","replying-to",-862743673));
var row_index = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46455__$1,new cljs.core.Keyword(null,"row-index","row-index",-828710296));
var member_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46455__$1,new cljs.core.Keyword(null,"member?","member?",486668360));
var showing_picker_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46455__$1,new cljs.core.Keyword(null,"showing-picker?","showing-picker?",-369252247));
var reply_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46455__$1,new cljs.core.Keyword(null,"reply-cb","reply-cb",-120094452));
var clear_cell_measure_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46455__$1,new cljs.core.Keyword(null,"clear-cell-measure-cb","clear-cell-measure-cb",1839014573));
var entry_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46455__$1,new cljs.core.Keyword(null,"entry-data","entry-data",1970939662));
var mouse_leave_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46455__$1,new cljs.core.Keyword(null,"mouse-leave-cb","mouse-leave-cb",1257434096));
var add_comment_force_update = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46455__$1,new cljs.core.Keyword(null,"add-comment-force-update","add-comment-force-update",1376707794));
var current_user_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46455__$1,new cljs.core.Keyword(null,"current-user-id","current-user-id",-2013633451));
var reply_focus_value = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46455__$1,new cljs.core.Keyword(null,"reply-focus-value","reply-focus-value",1337337974));
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-reply-item","editing?","oc.web.components.stream-reply-item/editing?",-1549995393).cljs$core$IFn$_invoke$arity$1(s)))){
var add_comment_string_key = oc.web.dispatcher.add_comment_string_key.cljs$core$IFn$_invoke$arity$3(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data),new cljs.core.Keyword(null,"reply-parent","reply-parent",579138103).cljs$core$IFn$_invoke$arity$1(comment_data),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(comment_data));
return sablono.interpreter.interpret((function (){var G__46457 = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"activity-data","activity-data",1293689136),entry_data,new cljs.core.Keyword(null,"comment-data","comment-data",736267705),comment_data,new cljs.core.Keyword(null,"dismiss-reply-cb","dismiss-reply-cb",1894601324),(function (){
return oc.web.components.stream_reply_item.finish_edit(s,clear_cell_measure_cb);
}),new cljs.core.Keyword(null,"add-comment-did-change","add-comment-did-change",-325940488),clear_cell_measure_cb,new cljs.core.Keyword(null,"add-comment-cb","add-comment-cb",1940125120),clear_cell_measure_cb,new cljs.core.Keyword(null,"edit-comment-key","edit-comment-key",-1517723522),cljs.core.get.cljs$core$IFn$_invoke$arity$2(add_comment_force_update,add_comment_string_key)], null);
return (oc.web.components.stream_comments.edit_comment.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.stream_comments.edit_comment.cljs$core$IFn$_invoke$arity$1(G__46457) : oc.web.components.stream_comments.edit_comment.call(null,G__46457));
})());
} else {
var show_new_comment_tag = new cljs.core.Keyword(null,"unseen","unseen",1063275592).cljs$core$IFn$_invoke$arity$1(comment_data);
return React.createElement("div",({"key": ["reply-comment-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"created-at","created-at",-89248644).cljs$core$IFn$_invoke$arity$1(comment_data))].join(''), "data-comment-uuid": new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(comment_data), "data-unseen": new cljs.core.Keyword(null,"unseen","unseen",1063275592).cljs$core$IFn$_invoke$arity$1(comment_data), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["reply-comment-outer","open-reply",oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"new-comment","new-comment",1342914881),new cljs.core.Keyword(null,"unseen","unseen",1063275592).cljs$core$IFn$_invoke$arity$1(comment_data),new cljs.core.Keyword(null,"showing-picker","showing-picker",-1081995369),showing_picker_QMARK_], null))], null))}),React.createElement("div",({"ref": ["reply-comment-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(comment_data))].join(''), "onMouseLeave": (function (e){
cljs.core.compare_and_set_BANG_(new cljs.core.Keyword("oc.web.components.stream-reply-item","show-more-menu","oc.web.components.stream-reply-item/show-more-menu",900951581).cljs$core$IFn$_invoke$arity$1(s),true,false);

if(cljs.core.fn_QMARK_(mouse_leave_cb)){
return (mouse_leave_cb.cljs$core$IFn$_invoke$arity$1 ? mouse_leave_cb.cljs$core$IFn$_invoke$arity$1(e) : mouse_leave_cb.call(null,e));
} else {
return null;
}
}), "className": "reply-comment"}),(function (){var attrs46458 = (cljs.core.truth_(is_mobile_QMARK_)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.reply-comment-mobile-menu","div.reply-comment-mobile-menu",864566500),(function (){var G__46459 = new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"entity-data","entity-data",1608056141),comment_data,new cljs.core.Keyword(null,"external-share","external-share",-2131927863),false,new cljs.core.Keyword(null,"entity-type","entity-type",-1957300125),"comment",new cljs.core.Keyword(null,"can-react?","can-react?",680341130),true,new cljs.core.Keyword(null,"react-cb","react-cb",-2110368957),react_cb,new cljs.core.Keyword(null,"can-reply?","can-reply?",-61994911),true,new cljs.core.Keyword(null,"reply-cb","reply-cb",-120094452),reply_cb], null);
return (oc.web.components.ui.more_menu.more_menu.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.more_menu.more_menu.cljs$core$IFn$_invoke$arity$1(G__46459) : oc.web.components.ui.more_menu.more_menu.call(null,G__46459));
})(),emoji_picker], null):null);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46458))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["reply-comment-inner"], null)], null),attrs46458], 0))):({"className": "reply-comment-inner"})),((cljs.core.map_QMARK_(attrs46458))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [React.createElement("div",({"className": "reply-comment-right"}),React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["reply-comment-header","group",oc.web.lib.utils.hide_class], null))}),(function (){var attrs46460 = (function (){var comment_author = new cljs.core.Keyword(null,"author","author",2111686192).cljs$core$IFn$_invoke$arity$1(comment_data);
return new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.reply-comment-author-right-group","div.reply-comment-author-right-group",1195881068),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),(cljs.core.truth_(new cljs.core.Keyword(null,"unseen","unseen",1063275592).cljs$core$IFn$_invoke$arity$1(comment_data))?"new-comment":null)], null),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.reply-comment-author-name-container","div.reply-comment-author-name-container",-1655890704),(function (){var G__46461 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"user-data","user-data",2143823568),comment_author,new cljs.core.Keyword(null,"current-user-id","current-user-id",-2013633451),current_user_id,new cljs.core.Keyword(null,"leave-delay?","leave-delay?",654972741),true], null);
return (oc.web.components.ui.info_hover_views.user_info_hover.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.info_hover_views.user_info_hover.cljs$core$IFn$_invoke$arity$1(G__46461) : oc.web.components.ui.info_hover_views.user_info_hover.call(null,G__46461));
})(),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.reply-comment-author-avatar","div.reply-comment-author-avatar",353023642),(oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1(comment_author) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,comment_author))], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a.reply-comment-author-name","a.reply-comment-author-name",-1401200288),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"class","class",-2030961996),(cljs.core.truth_(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(comment_author))?"clickable-name":null),new cljs.core.Keyword(null,"href","href",-793805698),(cljs.core.truth_(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(comment_author))?oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(comment_author)):null),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (p1__46452_SHARP_){
oc.web.lib.utils.event_stop(p1__46452_SHARP_);

return oc.web.actions.nav_sidebar.nav_to_author_BANG_.cljs$core$IFn$_invoke$arity$3(p1__46452_SHARP_,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(comment_author),oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(comment_author)));
})], null),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(comment_author)], null)], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.separator-dot","div.separator-dot",2056473245)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.reply-comment-author-timestamp","div.reply-comment-author-timestamp",583355938),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"time","time",1385887882),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"date-time","date-time",177938180),new cljs.core.Keyword(null,"created-at","created-at",-89248644).cljs$core$IFn$_invoke$arity$1(comment_data),new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687),(cljs.core.truth_(is_mobile_QMARK_)?null:"tooltip"),new cljs.core.Keyword(null,"data-placement","data-placement",166529525),"top",new cljs.core.Keyword(null,"data-container","data-container",1473653353),"body",new cljs.core.Keyword(null,"data-delay","data-delay",1974747786),"{\"show\":\"1000\", \"hide\":\"0\"}",new cljs.core.Keyword(null,"data-title","data-title",-83549535),oc.web.lib.utils.activity_date_tooltip(comment_data)], null),oc.web.lib.utils.foc_date_time(new cljs.core.Keyword(null,"created-at","created-at",-89248644).cljs$core$IFn$_invoke$arity$1(comment_data))], null)], null)], null);
})();
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46460))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["reply-comment-author-right"], null)], null),attrs46460], 0))):({"className": "reply-comment-author-right"})),((cljs.core.map_QMARK_(attrs46460))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret((cljs.core.truth_(show_new_comment_tag)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.separator-dot","div.separator-dot",2056473245)], null):null)),sablono.interpreter.interpret((cljs.core.truth_(show_new_comment_tag)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.new-comment-tag","div.new-comment-tag",2093863634)], null):null)),(cljs.core.truth_(oc.web.lib.responsive.is_mobile_size_QMARK_())?(function (){var attrs46462 = (function (){var G__46463 = comment_data;
var G__46464 = null;
var G__46465 = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"external-share","external-share",-2131927863),false,new cljs.core.Keyword(null,"entity-type","entity-type",-1957300125),"comment",new cljs.core.Keyword(null,"can-react?","can-react?",680341130),true,new cljs.core.Keyword(null,"react-cb","react-cb",-2110368957),react_cb,new cljs.core.Keyword(null,"can-reply?","can-reply?",-61994911),true,new cljs.core.Keyword(null,"reply-cb","reply-cb",-120094452),reply_cb], null);
return (oc.web.components.ui.more_menu.more_menu.cljs$core$IFn$_invoke$arity$3 ? oc.web.components.ui.more_menu.more_menu.cljs$core$IFn$_invoke$arity$3(G__46463,G__46464,G__46465) : oc.web.components.ui.more_menu.more_menu.call(null,G__46463,G__46464,G__46465));
})();
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46462))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["reply-comment-mobile-menu"], null)], null),attrs46462], 0))):({"className": "reply-comment-mobile-menu"})),((cljs.core.map_QMARK_(attrs46462))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(emoji_picker)], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46462),sablono.interpreter.interpret(emoji_picker)], null)));
})():React.createElement("div",({"key": "reply-comment-floating-buttons", "className": "reply-comment-floating-buttons"}),sablono.interpreter.interpret((cljs.core.truth_((function (){var or__4126__auto__ = new cljs.core.Keyword(null,"can-edit","can-edit",442089902).cljs$core$IFn$_invoke$arity$1(comment_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"can-delete","can-delete",1620748590).cljs$core$IFn$_invoke$arity$1(comment_data);
}
})())?new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.more-bt-container","div.more-bt-container",1959759254),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ref","ref",1289896967),new cljs.core.Keyword(null,"more-bt-container","more-bt-container",-732259673)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.floating-bt.more-bt","button.mlb-reset.floating-bt.more-bt",1027167270),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("oc.web.components.stream-reply-item","show-more-menu","oc.web.components.stream-reply-item/show-more-menu",900951581).cljs$core$IFn$_invoke$arity$1(s),cljs.core.not);
})], null)], null),(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-reply-item","show-more-menu","oc.web.components.stream-reply-item/show-more-menu",900951581).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.comment-more-menu-container","div.comment-more-menu-container",65564444),(cljs.core.truth_(new cljs.core.Keyword(null,"can-delete","can-delete",1620748590).cljs$core$IFn$_invoke$arity$1(comment_data))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.delete-bt","button.mlb-reset.delete-bt",-168688558),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (p1__46453_SHARP_){
return oc.web.components.stream_reply_item.delete_clicked(p1__46453_SHARP_,entry_data,comment_data,clear_cell_measure_cb);
})], null),"Delete"], null):null),(cljs.core.truth_(new cljs.core.Keyword(null,"can-edit","can-edit",442089902).cljs$core$IFn$_invoke$arity$1(comment_data))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.edit-bt","button.mlb-reset.edit-bt",1545149757),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.components.stream_reply_item.start_edit(s,entry_data,comment_data,clear_cell_measure_cb);
})], null),"Edit"], null):null)], null):null)], null):null)),React.createElement("button",({"data-toggle": "tooltip", "data-placement": "top", "onClick": reply_cb, "title": "Reply", "className": "mlb-reset floating-bt reply-bt"})),React.createElement("div",({"className": "react-bt-container"}),React.createElement("button",({"data-toggle": "tooltip", "data-placement": "top", "title": "Add reaction", "onClick": react_cb, "className": "mlb-reset floating-bt react-bt"})),sablono.interpreter.interpret(emoji_picker))))], null):new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46460),sablono.interpreter.interpret((cljs.core.truth_(show_new_comment_tag)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.separator-dot","div.separator-dot",2056473245)], null):null)),sablono.interpreter.interpret((cljs.core.truth_(show_new_comment_tag)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.new-comment-tag","div.new-comment-tag",2093863634)], null):null)),(cljs.core.truth_(oc.web.lib.responsive.is_mobile_size_QMARK_())?(function (){var attrs46466 = (function (){var G__46467 = comment_data;
var G__46468 = null;
var G__46469 = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"external-share","external-share",-2131927863),false,new cljs.core.Keyword(null,"entity-type","entity-type",-1957300125),"comment",new cljs.core.Keyword(null,"can-react?","can-react?",680341130),true,new cljs.core.Keyword(null,"react-cb","react-cb",-2110368957),react_cb,new cljs.core.Keyword(null,"can-reply?","can-reply?",-61994911),true,new cljs.core.Keyword(null,"reply-cb","reply-cb",-120094452),reply_cb], null);
return (oc.web.components.ui.more_menu.more_menu.cljs$core$IFn$_invoke$arity$3 ? oc.web.components.ui.more_menu.more_menu.cljs$core$IFn$_invoke$arity$3(G__46467,G__46468,G__46469) : oc.web.components.ui.more_menu.more_menu.call(null,G__46467,G__46468,G__46469));
})();
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46466))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["reply-comment-mobile-menu"], null)], null),attrs46466], 0))):({"className": "reply-comment-mobile-menu"})),((cljs.core.map_QMARK_(attrs46466))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(emoji_picker)], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46466),sablono.interpreter.interpret(emoji_picker)], null)));
})():React.createElement("div",({"key": "reply-comment-floating-buttons", "className": "reply-comment-floating-buttons"}),sablono.interpreter.interpret((cljs.core.truth_((function (){var or__4126__auto__ = new cljs.core.Keyword(null,"can-edit","can-edit",442089902).cljs$core$IFn$_invoke$arity$1(comment_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"can-delete","can-delete",1620748590).cljs$core$IFn$_invoke$arity$1(comment_data);
}
})())?new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.more-bt-container","div.more-bt-container",1959759254),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ref","ref",1289896967),new cljs.core.Keyword(null,"more-bt-container","more-bt-container",-732259673)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.floating-bt.more-bt","button.mlb-reset.floating-bt.more-bt",1027167270),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("oc.web.components.stream-reply-item","show-more-menu","oc.web.components.stream-reply-item/show-more-menu",900951581).cljs$core$IFn$_invoke$arity$1(s),cljs.core.not);
})], null)], null),(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-reply-item","show-more-menu","oc.web.components.stream-reply-item/show-more-menu",900951581).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.comment-more-menu-container","div.comment-more-menu-container",65564444),(cljs.core.truth_(new cljs.core.Keyword(null,"can-delete","can-delete",1620748590).cljs$core$IFn$_invoke$arity$1(comment_data))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.delete-bt","button.mlb-reset.delete-bt",-168688558),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (p1__46453_SHARP_){
return oc.web.components.stream_reply_item.delete_clicked(p1__46453_SHARP_,entry_data,comment_data,clear_cell_measure_cb);
})], null),"Delete"], null):null),(cljs.core.truth_(new cljs.core.Keyword(null,"can-edit","can-edit",442089902).cljs$core$IFn$_invoke$arity$1(comment_data))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.edit-bt","button.mlb-reset.edit-bt",1545149757),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.components.stream_reply_item.start_edit(s,entry_data,comment_data,clear_cell_measure_cb);
})], null),"Edit"], null):null)], null):null)], null):null)),React.createElement("button",({"data-toggle": "tooltip", "data-placement": "top", "onClick": reply_cb, "title": "Reply", "className": "mlb-reset floating-bt reply-bt"})),React.createElement("div",({"className": "react-bt-container"}),React.createElement("button",({"data-toggle": "tooltip", "data-placement": "top", "title": "Add reaction", "onClick": react_cb, "className": "mlb-reset floating-bt react-bt"})),sablono.interpreter.interpret(emoji_picker))))], null)));
})()),React.createElement("div",({"className": "reply-comment-content"}),React.createElement("div",({"dangerouslySetInnerHTML": oc.web.lib.utils.emojify(new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(comment_data)), "ref": "reply-comment-body", "data-row-index": row_index, "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["reply-comment-body","oc-mentions","oc-mentions-hover",oc.web.lib.utils.class_set(cljs.core.PersistentArrayMap.createAsIfByAssoc([new cljs.core.Keyword(null,"emoji-comment","emoji-comment",1722031366),new cljs.core.Keyword(null,"is-emoji","is-emoji",-1643384064).cljs$core$IFn$_invoke$arity$1(comment_data),oc.web.lib.utils.hide_class,true,oc.web.utils.dom.onload_recalc_measure_class,true]))], null))}))),sablono.interpreter.interpret(((cljs.core.seq(new cljs.core.Keyword(null,"reactions","reactions",2029850654).cljs$core$IFn$_invoke$arity$1(comment_data)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.reply-comment-reactions-footer.group","div.reply-comment-reactions-footer.group",1478696427),(function (){var G__46470 = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"entity-data","entity-data",1608056141),comment_data,new cljs.core.Keyword(null,"hide-picker","hide-picker",-1886003178),(cljs.core.count(new cljs.core.Keyword(null,"reactions","reactions",2029850654).cljs$core$IFn$_invoke$arity$1(comment_data)) === (0)),new cljs.core.Keyword(null,"did-react-cb","did-react-cb",1627613154),did_react_cb,new cljs.core.Keyword(null,"optional-activity-data","optional-activity-data",-1392718475),entry_data], null);
return (oc.web.components.reactions.reactions.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.reactions.reactions.cljs$core$IFn$_invoke$arity$1(G__46470) : oc.web.components.reactions.reactions.call(null,G__46470));
})()], null):null)))], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46458),React.createElement("div",({"className": "reply-comment-right"}),React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["reply-comment-header","group",oc.web.lib.utils.hide_class], null))}),(function (){var attrs46471 = (function (){var comment_author = new cljs.core.Keyword(null,"author","author",2111686192).cljs$core$IFn$_invoke$arity$1(comment_data);
return new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.reply-comment-author-right-group","div.reply-comment-author-right-group",1195881068),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),(cljs.core.truth_(new cljs.core.Keyword(null,"unseen","unseen",1063275592).cljs$core$IFn$_invoke$arity$1(comment_data))?"new-comment":null)], null),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.reply-comment-author-name-container","div.reply-comment-author-name-container",-1655890704),(function (){var G__46472 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"user-data","user-data",2143823568),comment_author,new cljs.core.Keyword(null,"current-user-id","current-user-id",-2013633451),current_user_id,new cljs.core.Keyword(null,"leave-delay?","leave-delay?",654972741),true], null);
return (oc.web.components.ui.info_hover_views.user_info_hover.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.info_hover_views.user_info_hover.cljs$core$IFn$_invoke$arity$1(G__46472) : oc.web.components.ui.info_hover_views.user_info_hover.call(null,G__46472));
})(),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.reply-comment-author-avatar","div.reply-comment-author-avatar",353023642),(oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1(comment_author) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,comment_author))], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a.reply-comment-author-name","a.reply-comment-author-name",-1401200288),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"class","class",-2030961996),(cljs.core.truth_(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(comment_author))?"clickable-name":null),new cljs.core.Keyword(null,"href","href",-793805698),(cljs.core.truth_(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(comment_author))?oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(comment_author)):null),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (p1__46452_SHARP_){
oc.web.lib.utils.event_stop(p1__46452_SHARP_);

return oc.web.actions.nav_sidebar.nav_to_author_BANG_.cljs$core$IFn$_invoke$arity$3(p1__46452_SHARP_,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(comment_author),oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(comment_author)));
})], null),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(comment_author)], null)], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.separator-dot","div.separator-dot",2056473245)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.reply-comment-author-timestamp","div.reply-comment-author-timestamp",583355938),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"time","time",1385887882),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"date-time","date-time",177938180),new cljs.core.Keyword(null,"created-at","created-at",-89248644).cljs$core$IFn$_invoke$arity$1(comment_data),new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687),(cljs.core.truth_(is_mobile_QMARK_)?null:"tooltip"),new cljs.core.Keyword(null,"data-placement","data-placement",166529525),"top",new cljs.core.Keyword(null,"data-container","data-container",1473653353),"body",new cljs.core.Keyword(null,"data-delay","data-delay",1974747786),"{\"show\":\"1000\", \"hide\":\"0\"}",new cljs.core.Keyword(null,"data-title","data-title",-83549535),oc.web.lib.utils.activity_date_tooltip(comment_data)], null),oc.web.lib.utils.foc_date_time(new cljs.core.Keyword(null,"created-at","created-at",-89248644).cljs$core$IFn$_invoke$arity$1(comment_data))], null)], null)], null);
})();
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46471))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["reply-comment-author-right"], null)], null),attrs46471], 0))):({"className": "reply-comment-author-right"})),((cljs.core.map_QMARK_(attrs46471))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret((cljs.core.truth_(show_new_comment_tag)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.separator-dot","div.separator-dot",2056473245)], null):null)),sablono.interpreter.interpret((cljs.core.truth_(show_new_comment_tag)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.new-comment-tag","div.new-comment-tag",2093863634)], null):null)),(cljs.core.truth_(oc.web.lib.responsive.is_mobile_size_QMARK_())?(function (){var attrs46473 = (function (){var G__46474 = comment_data;
var G__46475 = null;
var G__46476 = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"external-share","external-share",-2131927863),false,new cljs.core.Keyword(null,"entity-type","entity-type",-1957300125),"comment",new cljs.core.Keyword(null,"can-react?","can-react?",680341130),true,new cljs.core.Keyword(null,"react-cb","react-cb",-2110368957),react_cb,new cljs.core.Keyword(null,"can-reply?","can-reply?",-61994911),true,new cljs.core.Keyword(null,"reply-cb","reply-cb",-120094452),reply_cb], null);
return (oc.web.components.ui.more_menu.more_menu.cljs$core$IFn$_invoke$arity$3 ? oc.web.components.ui.more_menu.more_menu.cljs$core$IFn$_invoke$arity$3(G__46474,G__46475,G__46476) : oc.web.components.ui.more_menu.more_menu.call(null,G__46474,G__46475,G__46476));
})();
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46473))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["reply-comment-mobile-menu"], null)], null),attrs46473], 0))):({"className": "reply-comment-mobile-menu"})),((cljs.core.map_QMARK_(attrs46473))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(emoji_picker)], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46473),sablono.interpreter.interpret(emoji_picker)], null)));
})():React.createElement("div",({"key": "reply-comment-floating-buttons", "className": "reply-comment-floating-buttons"}),sablono.interpreter.interpret((cljs.core.truth_((function (){var or__4126__auto__ = new cljs.core.Keyword(null,"can-edit","can-edit",442089902).cljs$core$IFn$_invoke$arity$1(comment_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"can-delete","can-delete",1620748590).cljs$core$IFn$_invoke$arity$1(comment_data);
}
})())?new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.more-bt-container","div.more-bt-container",1959759254),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ref","ref",1289896967),new cljs.core.Keyword(null,"more-bt-container","more-bt-container",-732259673)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.floating-bt.more-bt","button.mlb-reset.floating-bt.more-bt",1027167270),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("oc.web.components.stream-reply-item","show-more-menu","oc.web.components.stream-reply-item/show-more-menu",900951581).cljs$core$IFn$_invoke$arity$1(s),cljs.core.not);
})], null)], null),(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-reply-item","show-more-menu","oc.web.components.stream-reply-item/show-more-menu",900951581).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.comment-more-menu-container","div.comment-more-menu-container",65564444),(cljs.core.truth_(new cljs.core.Keyword(null,"can-delete","can-delete",1620748590).cljs$core$IFn$_invoke$arity$1(comment_data))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.delete-bt","button.mlb-reset.delete-bt",-168688558),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (p1__46453_SHARP_){
return oc.web.components.stream_reply_item.delete_clicked(p1__46453_SHARP_,entry_data,comment_data,clear_cell_measure_cb);
})], null),"Delete"], null):null),(cljs.core.truth_(new cljs.core.Keyword(null,"can-edit","can-edit",442089902).cljs$core$IFn$_invoke$arity$1(comment_data))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.edit-bt","button.mlb-reset.edit-bt",1545149757),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.components.stream_reply_item.start_edit(s,entry_data,comment_data,clear_cell_measure_cb);
})], null),"Edit"], null):null)], null):null)], null):null)),React.createElement("button",({"data-toggle": "tooltip", "data-placement": "top", "onClick": reply_cb, "title": "Reply", "className": "mlb-reset floating-bt reply-bt"})),React.createElement("div",({"className": "react-bt-container"}),React.createElement("button",({"data-toggle": "tooltip", "data-placement": "top", "title": "Add reaction", "onClick": react_cb, "className": "mlb-reset floating-bt react-bt"})),sablono.interpreter.interpret(emoji_picker))))], null):new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46471),sablono.interpreter.interpret((cljs.core.truth_(show_new_comment_tag)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.separator-dot","div.separator-dot",2056473245)], null):null)),sablono.interpreter.interpret((cljs.core.truth_(show_new_comment_tag)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.new-comment-tag","div.new-comment-tag",2093863634)], null):null)),(cljs.core.truth_(oc.web.lib.responsive.is_mobile_size_QMARK_())?(function (){var attrs46477 = (function (){var G__46478 = comment_data;
var G__46479 = null;
var G__46480 = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"external-share","external-share",-2131927863),false,new cljs.core.Keyword(null,"entity-type","entity-type",-1957300125),"comment",new cljs.core.Keyword(null,"can-react?","can-react?",680341130),true,new cljs.core.Keyword(null,"react-cb","react-cb",-2110368957),react_cb,new cljs.core.Keyword(null,"can-reply?","can-reply?",-61994911),true,new cljs.core.Keyword(null,"reply-cb","reply-cb",-120094452),reply_cb], null);
return (oc.web.components.ui.more_menu.more_menu.cljs$core$IFn$_invoke$arity$3 ? oc.web.components.ui.more_menu.more_menu.cljs$core$IFn$_invoke$arity$3(G__46478,G__46479,G__46480) : oc.web.components.ui.more_menu.more_menu.call(null,G__46478,G__46479,G__46480));
})();
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46477))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["reply-comment-mobile-menu"], null)], null),attrs46477], 0))):({"className": "reply-comment-mobile-menu"})),((cljs.core.map_QMARK_(attrs46477))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(emoji_picker)], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46477),sablono.interpreter.interpret(emoji_picker)], null)));
})():React.createElement("div",({"key": "reply-comment-floating-buttons", "className": "reply-comment-floating-buttons"}),sablono.interpreter.interpret((cljs.core.truth_((function (){var or__4126__auto__ = new cljs.core.Keyword(null,"can-edit","can-edit",442089902).cljs$core$IFn$_invoke$arity$1(comment_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"can-delete","can-delete",1620748590).cljs$core$IFn$_invoke$arity$1(comment_data);
}
})())?new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.more-bt-container","div.more-bt-container",1959759254),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ref","ref",1289896967),new cljs.core.Keyword(null,"more-bt-container","more-bt-container",-732259673)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.floating-bt.more-bt","button.mlb-reset.floating-bt.more-bt",1027167270),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("oc.web.components.stream-reply-item","show-more-menu","oc.web.components.stream-reply-item/show-more-menu",900951581).cljs$core$IFn$_invoke$arity$1(s),cljs.core.not);
})], null)], null),(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-reply-item","show-more-menu","oc.web.components.stream-reply-item/show-more-menu",900951581).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.comment-more-menu-container","div.comment-more-menu-container",65564444),(cljs.core.truth_(new cljs.core.Keyword(null,"can-delete","can-delete",1620748590).cljs$core$IFn$_invoke$arity$1(comment_data))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.delete-bt","button.mlb-reset.delete-bt",-168688558),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (p1__46453_SHARP_){
return oc.web.components.stream_reply_item.delete_clicked(p1__46453_SHARP_,entry_data,comment_data,clear_cell_measure_cb);
})], null),"Delete"], null):null),(cljs.core.truth_(new cljs.core.Keyword(null,"can-edit","can-edit",442089902).cljs$core$IFn$_invoke$arity$1(comment_data))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.edit-bt","button.mlb-reset.edit-bt",1545149757),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.components.stream_reply_item.start_edit(s,entry_data,comment_data,clear_cell_measure_cb);
})], null),"Edit"], null):null)], null):null)], null):null)),React.createElement("button",({"data-toggle": "tooltip", "data-placement": "top", "onClick": reply_cb, "title": "Reply", "className": "mlb-reset floating-bt reply-bt"})),React.createElement("div",({"className": "react-bt-container"}),React.createElement("button",({"data-toggle": "tooltip", "data-placement": "top", "title": "Add reaction", "onClick": react_cb, "className": "mlb-reset floating-bt react-bt"})),sablono.interpreter.interpret(emoji_picker))))], null)));
})()),React.createElement("div",({"className": "reply-comment-content"}),React.createElement("div",({"dangerouslySetInnerHTML": oc.web.lib.utils.emojify(new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(comment_data)), "ref": "reply-comment-body", "data-row-index": row_index, "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["reply-comment-body","oc-mentions","oc-mentions-hover",oc.web.lib.utils.class_set(cljs.core.PersistentArrayMap.createAsIfByAssoc([new cljs.core.Keyword(null,"emoji-comment","emoji-comment",1722031366),new cljs.core.Keyword(null,"is-emoji","is-emoji",-1643384064).cljs$core$IFn$_invoke$arity$1(comment_data),oc.web.lib.utils.hide_class,true,oc.web.utils.dom.onload_recalc_measure_class,true]))], null))}))),sablono.interpreter.interpret(((cljs.core.seq(new cljs.core.Keyword(null,"reactions","reactions",2029850654).cljs$core$IFn$_invoke$arity$1(comment_data)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.reply-comment-reactions-footer.group","div.reply-comment-reactions-footer.group",1478696427),(function (){var G__46481 = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"entity-data","entity-data",1608056141),comment_data,new cljs.core.Keyword(null,"hide-picker","hide-picker",-1886003178),(cljs.core.count(new cljs.core.Keyword(null,"reactions","reactions",2029850654).cljs$core$IFn$_invoke$arity$1(comment_data)) === (0)),new cljs.core.Keyword(null,"did-react-cb","did-react-cb",1627613154),did_react_cb,new cljs.core.Keyword(null,"optional-activity-data","optional-activity-data",-1392718475),entry_data], null);
return (oc.web.components.reactions.reactions.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.reactions.reactions.cljs$core$IFn$_invoke$arity$1(G__46481) : oc.web.components.reactions.reactions.call(null,G__46481));
})()], null):null)))], null)));
})()));
}
}),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$,oc.web.mixins.ui.on_click_out.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"more-bt-container","more-bt-container",-732259673),(function (p1__46451_SHARP_){
return cljs.core.compare_and_set_BANG_(new cljs.core.Keyword("oc.web.components.stream-reply-item","show-more-menu","oc.web.components.stream-reply-item/show-more-menu",900951581).cljs$core$IFn$_invoke$arity$1(p1__46451_SHARP_),true,false);
})),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.stream-reply-item","show-more-menu","oc.web.components.stream-reply-item/show-more-menu",900951581)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.stream-reply-item","editing?","oc.web.components.stream-reply-item/editing?",-1549995393))], null),"reply-comment");
oc.web.components.stream_reply_item.reply_top = rum.core.build_defc((function (p__46482){
var map__46483 = p__46482;
var map__46483__$1 = (((((!((map__46483 == null))))?(((((map__46483.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46483.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46483):map__46483);
var map__46484 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46483__$1,new cljs.core.Keyword(null,"entry-data","entry-data",1970939662));
var map__46484__$1 = (((((!((map__46484 == null))))?(((((map__46484.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46484.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46484):map__46484);
var entry_data = map__46484__$1;
var publisher = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46484__$1,new cljs.core.Keyword(null,"publisher","publisher",-153364540));
var board_name = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46484__$1,new cljs.core.Keyword(null,"board-name","board-name",-677515056));
var published_at = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46484__$1,new cljs.core.Keyword(null,"published-at","published-at",249684621));
var headline = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46484__$1,new cljs.core.Keyword(null,"headline","headline",-157157727));
var links = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46484__$1,new cljs.core.Keyword(null,"links","links",-654507394));
var current_user_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46483__$1,new cljs.core.Keyword(null,"current-user-id","current-user-id",-2013633451));
var add_comment_focus_prefix = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46483__$1,new cljs.core.Keyword(null,"add-comment-focus-prefix","add-comment-focus-prefix",1635349699));
var read_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46483__$1,new cljs.core.Keyword(null,"read-data","read-data",-715156010));
var member_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46483__$1,new cljs.core.Keyword(null,"member?","member?",486668360));
var show_wrt_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46483__$1,new cljs.core.Keyword(null,"show-wrt?","show-wrt?",-1492163707));
var follow_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([links,"follow"], 0));
var unfollow_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([links,"unfollow"], 0));
return React.createElement("div",({"className": "reply-item-top"}),(function (){var attrs46487 = (function (){var G__46496 = new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"activity-data","activity-data",1293689136),entry_data,new cljs.core.Keyword(null,"user-avatar?","user-avatar?",-1162947881),true,new cljs.core.Keyword(null,"user-hover?","user-hover?",-1460761243),true,new cljs.core.Keyword(null,"board-hover?","board-hover?",1064995646),false,new cljs.core.Keyword(null,"activity-board?","activity-board?",-1568829907),true,new cljs.core.Keyword(null,"leave-delay?","leave-delay?",654972741),true,new cljs.core.Keyword(null,"current-user-id","current-user-id",-2013633451),current_user_id], null);
return (oc.web.components.ui.post_authorship.post_authorship.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.post_authorship.post_authorship.cljs$core$IFn$_invoke$arity$1(G__46496) : oc.web.components.ui.post_authorship.post_authorship.call(null,G__46496));
})();
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46487))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["reply-item-header"], null)], null),attrs46487], 0))):({"className": "reply-item-header"})),((cljs.core.map_QMARK_(attrs46487))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [React.createElement("div",({"className": "separator-dot"})),React.createElement("span",({"className": "time-since"}),React.createElement("time",({"dateTime": published_at, "data-toggle": (cljs.core.truth_(oc.web.lib.responsive.is_mobile_size_QMARK_())?null:"tooltip"), "data-placement": "top", "data-container": "body", "title": oc.web.lib.utils.activity_date_tooltip(oc.web.lib.utils.js_date.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([published_at], 0)))}),sablono.interpreter.interpret(oc.web.lib.utils.time_since.cljs$core$IFn$_invoke$arity$variadic(published_at,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"short","short",1928760516),new cljs.core.Keyword(null,"lower-case","lower-case",-212358583)], null)], 0))))),sablono.interpreter.interpret((cljs.core.truth_((function (){var or__4126__auto__ = follow_link;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return unfollow_link;
}
})())?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.mute-bt","button.mlb-reset.mute-bt",-1951644268),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"title","title",636505583),(cljs.core.truth_(follow_link)?"Get notified about new activity":"Don't show future replies to this update"),new cljs.core.Keyword(null,"class","class",-2030961996),(cljs.core.truth_(follow_link)?"unfollowing":"following"),new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687),(cljs.core.truth_(oc.web.lib.responsive.is_mobile_size_QMARK_())?null:"tooltip"),new cljs.core.Keyword(null,"data-placement","data-placement",166529525),"top",new cljs.core.Keyword(null,"data-container","data-container",1473653353),"body",new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.actions.activity.entry_unfollow(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data));
})], null)], null):null))], null):new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46487),React.createElement("div",({"className": "separator-dot"})),React.createElement("span",({"className": "time-since"}),React.createElement("time",({"dateTime": published_at, "data-toggle": (cljs.core.truth_(oc.web.lib.responsive.is_mobile_size_QMARK_())?null:"tooltip"), "data-placement": "top", "data-container": "body", "title": oc.web.lib.utils.activity_date_tooltip(oc.web.lib.utils.js_date.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([published_at], 0)))}),sablono.interpreter.interpret(oc.web.lib.utils.time_since.cljs$core$IFn$_invoke$arity$variadic(published_at,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"short","short",1928760516),new cljs.core.Keyword(null,"lower-case","lower-case",-212358583)], null)], 0))))),sablono.interpreter.interpret((cljs.core.truth_((function (){var or__4126__auto__ = follow_link;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return unfollow_link;
}
})())?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.mute-bt","button.mlb-reset.mute-bt",-1951644268),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"title","title",636505583),(cljs.core.truth_(follow_link)?"Get notified about new activity":"Don't show future replies to this update"),new cljs.core.Keyword(null,"class","class",-2030961996),(cljs.core.truth_(follow_link)?"unfollowing":"following"),new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687),(cljs.core.truth_(oc.web.lib.responsive.is_mobile_size_QMARK_())?null:"tooltip"),new cljs.core.Keyword(null,"data-placement","data-placement",166529525),"top",new cljs.core.Keyword(null,"data-container","data-container",1473653353),"body",new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.actions.activity.entry_unfollow(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data));
})], null)], null):null))], null)));
})(),(function (){var attrs46492 = headline;
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46492))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["reply-item-title"], null)], null),attrs46492], 0))):({"className": "reply-item-title"})),((cljs.core.map_QMARK_(attrs46492))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46492)], null)));
})(),React.createElement("div",({"data-itemuuid": new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data), "dangerouslySetInnerHTML": ({"__html": new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(entry_data)}), "className": "reply-item-body oc-mentions"})),(function (){var attrs46493 = (cljs.core.truth_(member_QMARK_)?(function (){var G__46497 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"entity-data","entity-data",1608056141),entry_data,new cljs.core.Keyword(null,"only-thumb?","only-thumb?",959195446),true], null);
return (oc.web.components.reactions.reactions.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.reactions.reactions.cljs$core$IFn$_invoke$arity$1(G__46497) : oc.web.components.reactions.reactions.call(null,G__46497));
})():null);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46493))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["reply-item-footer","group"], null)], null),attrs46493], 0))):({"className": "reply-item-footer group"})),((cljs.core.map_QMARK_(attrs46493))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [(function (){var attrs46494 = (cljs.core.truth_(member_QMARK_)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.reply-item-comments-summary","div.reply-item-comments-summary",373914203),(function (){var G__46498 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"entry-data","entry-data",1970939662),entry_data,new cljs.core.Keyword(null,"add-comment-focus-prefix","add-comment-focus-prefix",1635349699),add_comment_focus_prefix], null);
return (oc.web.components.ui.comments_summary.foc_comments_summary.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.comments_summary.foc_comments_summary.cljs$core$IFn$_invoke$arity$1(G__46498) : oc.web.components.ui.comments_summary.foc_comments_summary.call(null,G__46498));
})()], null):null);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46494))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["reply-item-footer-mobile-group"], null)], null),attrs46494], 0))):({"className": "reply-item-footer-mobile-group"})),((cljs.core.map_QMARK_(attrs46494))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret((cljs.core.truth_(show_wrt_QMARK_)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.reply-item-wrt","div.reply-item-wrt",-1257758703),(function (){var G__46499 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"activity-data","activity-data",1293689136),entry_data,new cljs.core.Keyword(null,"read-data","read-data",-715156010),read_data], null);
return (oc.web.components.ui.wrt.wrt_count.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.wrt.wrt_count.cljs$core$IFn$_invoke$arity$1(G__46499) : oc.web.components.ui.wrt.wrt_count.call(null,G__46499));
})()], null):null))], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46494),sablono.interpreter.interpret((cljs.core.truth_(show_wrt_QMARK_)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.reply-item-wrt","div.reply-item-wrt",-1257758703),(function (){var G__46500 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"activity-data","activity-data",1293689136),entry_data,new cljs.core.Keyword(null,"read-data","read-data",-715156010),read_data], null);
return (oc.web.components.ui.wrt.wrt_count.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.wrt.wrt_count.cljs$core$IFn$_invoke$arity$1(G__46500) : oc.web.components.ui.wrt.wrt_count.call(null,G__46500));
})()], null):null))], null)));
})()], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46493),(function (){var attrs46495 = (cljs.core.truth_(member_QMARK_)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.reply-item-comments-summary","div.reply-item-comments-summary",373914203),(function (){var G__46501 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"entry-data","entry-data",1970939662),entry_data,new cljs.core.Keyword(null,"add-comment-focus-prefix","add-comment-focus-prefix",1635349699),add_comment_focus_prefix], null);
return (oc.web.components.ui.comments_summary.foc_comments_summary.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.comments_summary.foc_comments_summary.cljs$core$IFn$_invoke$arity$1(G__46501) : oc.web.components.ui.comments_summary.foc_comments_summary.call(null,G__46501));
})()], null):null);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46495))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["reply-item-footer-mobile-group"], null)], null),attrs46495], 0))):({"className": "reply-item-footer-mobile-group"})),((cljs.core.map_QMARK_(attrs46495))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret((cljs.core.truth_(show_wrt_QMARK_)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.reply-item-wrt","div.reply-item-wrt",-1257758703),(function (){var G__46502 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"activity-data","activity-data",1293689136),entry_data,new cljs.core.Keyword(null,"read-data","read-data",-715156010),read_data], null);
return (oc.web.components.ui.wrt.wrt_count.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.wrt.wrt_count.cljs$core$IFn$_invoke$arity$1(G__46502) : oc.web.components.ui.wrt.wrt_count.call(null,G__46502));
})()], null):null))], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46495),sablono.interpreter.interpret((cljs.core.truth_(show_wrt_QMARK_)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.reply-item-wrt","div.reply-item-wrt",-1257758703),(function (){var G__46503 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"activity-data","activity-data",1293689136),entry_data,new cljs.core.Keyword(null,"read-data","read-data",-715156010),read_data], null);
return (oc.web.components.ui.wrt.wrt_count.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.wrt.wrt_count.cljs$core$IFn$_invoke$arity$1(G__46503) : oc.web.components.ui.wrt.wrt_count.call(null,G__46503));
})()], null):null))], null)));
})()], null)));
})());
}),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$,oc.web.mixins.ui.refresh_tooltips_mixin], null),"reply-top");
oc.web.components.stream_reply_item.reply_item_unique_class = (function oc$web$components$stream_reply_item$reply_item_unique_class(p__46504){
var map__46505 = p__46504;
var map__46505__$1 = (((((!((map__46505 == null))))?(((((map__46505.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46505.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46505):map__46505);
var uuid = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46505__$1,new cljs.core.Keyword(null,"uuid","uuid",-2145095719));
return ["reply-item-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(uuid)].join('');
});
oc.web.components.stream_reply_item.add_comment_focus_prefix = (function oc$web$components$stream_reply_item$add_comment_focus_prefix(){
return ["reply-comment-",cljs.core.str.cljs$core$IFn$_invoke$arity$1((cljs.core.rand.cljs$core$IFn$_invoke$arity$1((10000)) | (0))),"-prefix"].join('');
});
oc.web.components.stream_reply_item.reply_expand = (function oc$web$components$stream_reply_item$reply_expand(entry_data,reply_data){
return oc.web.actions.reply.reply_expand(entry_data,reply_data);
});
oc.web.components.stream_reply_item.reply_mark_seen = (function oc$web$components$stream_reply_item$reply_mark_seen(entry_data,reply_data){
return oc.web.actions.reply.reply_mark_seen(entry_data,reply_data);
});
oc.web.components.stream_reply_item.replies_mark_seen = (function oc$web$components$stream_reply_item$replies_mark_seen(entry_data){
return oc.web.actions.reply.replies_mark_seen(entry_data);
});
oc.web.components.stream_reply_item.reply_unwrap_body = (function oc$web$components$stream_reply_item$reply_unwrap_body(entry_data,reply_data){
return oc.web.actions.reply.reply_unwrap_body(entry_data,reply_data);
});
oc.web.components.stream_reply_item.replies_expand = (function oc$web$components$stream_reply_item$replies_expand(entry_data){
return oc.web.actions.reply.replies_expand(entry_data);
});
oc.web.components.stream_reply_item.comment_item = (function oc$web$components$stream_reply_item$comment_item(s,p__46507){
var map__46508 = p__46507;
var map__46508__$1 = (((((!((map__46508 == null))))?(((((map__46508.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46508.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46508):map__46508);
var is_mobile_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46508__$1,new cljs.core.Keyword(null,"is-mobile?","is-mobile?",2146205507));
var row_index = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46508__$1,new cljs.core.Keyword(null,"row-index","row-index",-828710296));
var member_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46508__$1,new cljs.core.Keyword(null,"member?","member?",486668360));
var seen_reply_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46508__$1,new cljs.core.Keyword(null,"seen-reply-cb","seen-reply-cb",39643564));
var comments_loaded_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46508__$1,new cljs.core.Keyword(null,"comments-loaded?","comments-loaded?",-595034611));
var clear_cell_measure_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46508__$1,new cljs.core.Keyword(null,"clear-cell-measure-cb","clear-cell-measure-cb",1839014573));
var entry_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46508__$1,new cljs.core.Keyword(null,"entry-data","entry-data",1970939662));
var reply_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46508__$1,new cljs.core.Keyword(null,"reply-data","reply-data",471571087));
var add_comment_force_update = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46508__$1,new cljs.core.Keyword(null,"add-comment-force-update","add-comment-force-update",1376707794));
var current_user_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46508__$1,new cljs.core.Keyword(null,"current-user-id","current-user-id",-2013633451));
var reply_focus_value = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46508__$1,new cljs.core.Keyword(null,"reply-focus-value","reply-focus-value",1337337974));
var showing_picker_QMARK_ = ((cljs.core.seq(cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-reply-item","show-picker","oc.web.components.stream-reply-item/show-picker",-1615000722).cljs$core$IFn$_invoke$arity$1(s)))) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-reply-item","show-picker","oc.web.components.stream-reply-item/show-picker",-1615000722).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(reply_data))));
var replying_to = (function (){var G__46511 = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(reply_data);
var fexpr__46510 = cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-reply-item","replying","oc.web.components.stream-reply-item/replying",-1915074329).cljs$core$IFn$_invoke$arity$1(s));
return (fexpr__46510.cljs$core$IFn$_invoke$arity$1 ? fexpr__46510.cljs$core$IFn$_invoke$arity$1(G__46511) : fexpr__46510.call(null,G__46511));
})();
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.reply-item-block.vertical-line.group","div.reply-item-block.vertical-line.group",-1206777017),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"key","key",-1516042587),["reply-thread-item-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(reply_data))].join('')], null),(function (){var G__46512 = cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"did-react-cb","did-react-cb",1627613154),new cljs.core.Keyword(null,"react-cb","react-cb",-2110368957),new cljs.core.Keyword(null,"is-mobile?","is-mobile?",2146205507),new cljs.core.Keyword(null,"replying-to","replying-to",-862743673),new cljs.core.Keyword(null,"member?","member?",486668360),new cljs.core.Keyword(null,"row-index","row-index",-828710296),new cljs.core.Keyword(null,"showing-picker?","showing-picker?",-369252247),new cljs.core.Keyword(null,"reply-cb","reply-cb",-120094452),new cljs.core.Keyword(null,"react-disabled?","react-disabled?",1235972269),new cljs.core.Keyword(null,"clear-cell-measure-cb","clear-cell-measure-cb",1839014573),new cljs.core.Keyword(null,"entry-data","entry-data",1970939662),new cljs.core.Keyword(null,"add-comment-force-update","add-comment-force-update",1376707794),new cljs.core.Keyword(null,"current-user-id","current-user-id",-2013633451),new cljs.core.Keyword(null,"reply-focus-value","reply-focus-value",1337337974),new cljs.core.Keyword(null,"comment-data","comment-data",736267705),new cljs.core.Keyword(null,"emoji-picker","emoji-picker",-447764229)],[(function (){
var G__46513_46610 = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(reply_data);
(seen_reply_cb.cljs$core$IFn$_invoke$arity$1 ? seen_reply_cb.cljs$core$IFn$_invoke$arity$1(G__46513_46610) : seen_reply_cb.call(null,G__46513_46610));

return (clear_cell_measure_cb.cljs$core$IFn$_invoke$arity$0 ? clear_cell_measure_cb.cljs$core$IFn$_invoke$arity$0() : clear_cell_measure_cb.call(null));
}),(function (){
if(cljs.core.truth_(comments_loaded_QMARK_)){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.stream-reply-item","show-picker","oc.web.components.stream-reply-item/show-picker",-1615000722).cljs$core$IFn$_invoke$arity$1(s),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(reply_data));
} else {
return null;
}
}),is_mobile_QMARK_,replying_to,member_QMARK_,row_index,showing_picker_QMARK_,(function (){
oc.web.components.stream_reply_item.reply_to(reply_data,reply_focus_value);

return (clear_cell_measure_cb.cljs$core$IFn$_invoke$arity$0 ? clear_cell_measure_cb.cljs$core$IFn$_invoke$arity$0() : clear_cell_measure_cb.call(null));
}),cljs.core.not(comments_loaded_QMARK_),clear_cell_measure_cb,entry_data,add_comment_force_update,current_user_id,reply_focus_value,reply_data,((showing_picker_QMARK_)?oc.web.components.stream_reply_item.emoji_picker_container(s,entry_data,reply_data,seen_reply_cb):null)]);
return (oc.web.components.stream_reply_item.reply_comment.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.stream_reply_item.reply_comment.cljs$core$IFn$_invoke$arity$1(G__46512) : oc.web.components.stream_reply_item.reply_comment.call(null,G__46512));
})()], null);
});
oc.web.components.stream_reply_item.collapsed_comments_button = rum.core.build_defc((function (p__46514){
var map__46515 = p__46514;
var map__46515__$1 = (((((!((map__46515 == null))))?(((((map__46515.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46515.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46515):map__46515);
var message = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46515__$1,new cljs.core.Keyword(null,"message","message",-406056002));
var expand_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46515__$1,new cljs.core.Keyword(null,"expand-cb","expand-cb",502558728));
return React.createElement("button",({"onClick": expand_cb, "className": "mlb-reset view-more-bt"}),sablono.interpreter.interpret(message));
}),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$], null),"collapsed-comments-button");
oc.web.components.stream_reply_item.setup_add_comment_focus_listener = (function oc$web$components$stream_reply_item$setup_add_comment_focus_listener(s){
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-reply-item","add-comment-focus-listener","oc.web.components.stream-reply-item/add-comment-focus-listener",9334911).cljs$core$IFn$_invoke$arity$1(s)))){
goog.events.unlistenByKey(cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-reply-item","add-comment-focus-listener","oc.web.components.stream-reply-item/add-comment-focus-listener",9334911).cljs$core$IFn$_invoke$arity$1(s)));
} else {
}

var temp__33774__auto__ = rum.core.dom_node(s);
if(cljs.core.truth_(temp__33774__auto__)){
var el = temp__33774__auto__;
var temp__33774__auto____$1 = el.querySelector(["div.add-comment-box-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-reply-item","add-comment-focus-prefix","oc.web.components.stream-reply-item/add-comment-focus-prefix",-1660209692).cljs$core$IFn$_invoke$arity$1(s)))," div.add-comment"].join(''));
if(cljs.core.truth_(temp__33774__auto____$1)){
var add_comment_element = temp__33774__auto____$1;
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.stream-reply-item","add-comment-focus-listener","oc.web.components.stream-reply-item/add-comment-focus-listener",9334911).cljs$core$IFn$_invoke$arity$1(s),goog.events.listen(add_comment_element,[goog.events.EventType.BLUR,goog.events.EventType.FOCUS],(function (p1__46517_SHARP_){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.stream-reply-item","add-comment-focused","oc.web.components.stream-reply-item/add-comment-focused",1813299804).cljs$core$IFn$_invoke$arity$1(s),cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(p1__46517_SHARP_.type,goog.events.EventType.FOCUS));
})));
} else {
return null;
}
} else {
return null;
}
});
oc.web.components.stream_reply_item.stream_reply_item = rum.core.build_defcs((function (s,p__46518){
var map__46519 = p__46518;
var map__46519__$1 = (((((!((map__46519 == null))))?(((((map__46519.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46519.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46519):map__46519);
var member_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46519__$1,new cljs.core.Keyword(null,"member?","member?",486668360));
var reply_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46519__$1,new cljs.core.Keyword(null,"reply-data","reply-data",471571087));
var show_wrt_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46519__$1,new cljs.core.Keyword(null,"show-wrt?","show-wrt?",-1492163707));
var row_index = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46519__$1,new cljs.core.Keyword(null,"row-index","row-index",-828710296));
var read_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46519__$1,new cljs.core.Keyword(null,"read-data","read-data",-715156010));
var current_user_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46519__$1,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915));
var clear_cell_measure_cb_STAR_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46519__$1,new cljs.core.Keyword(null,"clear-cell-measure-cb","clear-cell-measure-cb",1839014573));
var add_comment_force_update = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46519__$1,new cljs.core.Keyword(null,"add-comment-force-update","add-comment-force-update",1376707794));
var _users_info_hover = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"users-info-hover","users-info-hover",-941434570));
var _follow_publishers_list = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"follow-publishers-list","follow-publishers-list",-374150342));
var _followers_publishers_count = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"followers-publishers-count","followers-publishers-count",-692976579));
var map__46521 = reply_data;
var map__46521__$1 = (((((!((map__46521 == null))))?(((((map__46521.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46521.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46521):map__46521);
var entry_data = map__46521__$1;
var uuid = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46521__$1,new cljs.core.Keyword(null,"uuid","uuid",-2145095719));
var comments_count = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46521__$1,new cljs.core.Keyword(null,"comments-count","comments-count",1713184539));
var replies_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46521__$1,new cljs.core.Keyword(null,"replies-data","replies-data",1118937948));
var publisher = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46521__$1,new cljs.core.Keyword(null,"publisher","publisher",-153364540));
var unseen = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46521__$1,new cljs.core.Keyword(null,"unseen","unseen",1063275592));
var comments_loaded_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46521__$1,new cljs.core.Keyword(null,"comments-loaded?","comments-loaded?",-595034611));
var published_at = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46521__$1,new cljs.core.Keyword(null,"published-at","published-at",249684621));
var expanded_replies = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46521__$1,new cljs.core.Keyword(null,"expanded-replies","expanded-replies",-930394348));
var is_mobile_QMARK_ = oc.web.lib.responsive.is_mobile_size_QMARK_();
var reply_item_class = oc.web.components.stream_reply_item.reply_item_unique_class(entry_data);
var add_comment_focus_value = oc.web.utils.comment.add_comment_focus_value.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-reply-item","add-comment-focus-prefix","oc.web.components.stream-reply-item/add-comment-focus-prefix",-1660209692).cljs$core$IFn$_invoke$arity$1(s)),uuid], 0));
var show_expand_replies_QMARK_ = ((cljs.core.not(expanded_replies)) && (cljs.core.seq(cljs.core.filter.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"collapsed","collapsed",-628494523),replies_data))));
var clear_cell_measure_cb = (function (){
if(cljs.core.fn_QMARK_(clear_cell_measure_cb_STAR_)){
return oc.web.lib.utils.after((10),clear_cell_measure_cb_STAR_);
} else {
return null;
}
});
return React.createElement("div",({"data-activity-uuid": uuid, "ref": "reply-item", "onClick": (function (e){
var reply_el = rum.core.ref_node(s,new cljs.core.Keyword(null,"reply-item","reply-item",-1705952625));
if(((cljs.core.not(oc.web.lib.utils.button_clicked_QMARK_(e))) && (cljs.core.not(oc.web.lib.utils.input_clicked_QMARK_(e))) && (cljs.core.not(oc.web.lib.utils.anchor_clicked_QMARK_(e))) && (cljs.core.not(oc.web.lib.utils.content_editable_clicked_QMARK_(e))) && ((!(oc.web.utils.dom.event_inside_QMARK_(e,reply_el.querySelector("div.emoji-mart"))))) && ((!(oc.web.utils.dom.event_inside_QMARK_(e,reply_el.querySelector("div.add-comment-box-container"))))) && (cljs.core.not(oc.web.utils.dom.event_cotainer_has_class(e,"reply-comment-body"))))){
return oc.web.actions.nav_sidebar.open_post_modal.cljs$core$IFn$_invoke$arity$2(entry_data,false);
} else {
return null;
}
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["reply-item","group",oc.web.lib.utils.class_set(cljs.core.PersistentArrayMap.createAsIfByAssoc([new cljs.core.Keyword(null,"unseen","unseen",1063275592),unseen,new cljs.core.Keyword(null,"open-item","open-item",-1938301269),true,new cljs.core.Keyword(null,"close-item","close-item",-38717813),true,new cljs.core.Keyword(null,"reply-item-add-comment-focus","reply-item-add-comment-focus",-1900952537),cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-reply-item","add-comment-focused","oc.web.components.stream-reply-item/add-comment-focused",1813299804).cljs$core$IFn$_invoke$arity$1(s)),reply_item_class,true]))], null))}),sablono.interpreter.interpret((function (){var G__46524 = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"entry-data","entry-data",1970939662),entry_data,new cljs.core.Keyword(null,"current-user-id","current-user-id",-2013633451),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(current_user_data),new cljs.core.Keyword(null,"member?","member?",486668360),member_QMARK_,new cljs.core.Keyword(null,"show-wrt?","show-wrt?",-1492163707),show_wrt_QMARK_,new cljs.core.Keyword(null,"add-comment-focus-prefix","add-comment-focus-prefix",1635349699),cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-reply-item","add-comment-focus-prefix","oc.web.components.stream-reply-item/add-comment-focus-prefix",-1660209692).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"read-data","read-data",-715156010),read_data], null);
return (oc.web.components.stream_reply_item.reply_top.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.stream_reply_item.reply_top.cljs$core$IFn$_invoke$arity$1(G__46524) : oc.web.components.stream_reply_item.reply_top.call(null,G__46524));
})()),sablono.interpreter.interpret((cljs.core.truth_(((cljs.core.not(comments_loaded_QMARK_))?expanded_replies:false))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.reply-item-blocks.group","div.reply-item-blocks.group",-1594027653),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.reply-item-loading.group","div.reply-item-loading.group",-198997802),(oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.small_loading.small_loading.call(null)),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.reply-item-loading-inner","span.reply-item-loading-inner",375213206),"Loading more replies..."], null)], null)], null):null)),(function (){var attrs46523 = ((show_expand_replies_QMARK_)?rum.core.with_key((function (){var G__46525 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"expand-cb","expand-cb",502558728),(function (){
oc.web.components.stream_reply_item.replies_expand(entry_data);

return clear_cell_measure_cb();
}),new cljs.core.Keyword(null,"message","message",-406056002),["View all ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(comments_count)," comments"].join('')], null);
return (oc.web.components.stream_reply_item.collapsed_comments_button.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.stream_reply_item.collapsed_comments_button.cljs$core$IFn$_invoke$arity$1(G__46525) : oc.web.components.stream_reply_item.collapsed_comments_button.call(null,G__46525));
})(),["collapsed-comments-bt-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(uuid),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(comments_count)].join('')):null);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46523))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["reply-item-blocks","group"], null)], null),attrs46523], 0))):({"className": "reply-item-blocks group"})),((cljs.core.map_QMARK_(attrs46523))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.into_array.cljs$core$IFn$_invoke$arity$1((function (){var iter__4529__auto__ = (function oc$web$components$stream_reply_item$iter__46526(s__46527){
return (new cljs.core.LazySeq(null,(function (){
var s__46527__$1 = s__46527;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__46527__$1);
if(temp__5735__auto__){
var s__46527__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__46527__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__46527__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__46529 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__46528 = (0);
while(true){
if((i__46528 < size__4528__auto__)){
var reply = cljs.core._nth(c__4527__auto__,i__46528);
if(cljs.core.truth_((function (){var or__4126__auto__ = expanded_replies;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.not(new cljs.core.Keyword(null,"collapsed","collapsed",-628494523).cljs$core$IFn$_invoke$arity$1(reply));
}
})())){
cljs.core.chunk_append(b__46529,sablono.interpreter.interpret(oc.web.components.stream_reply_item.comment_item(s,cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"is-mobile?","is-mobile?",2146205507),new cljs.core.Keyword(null,"member?","member?",486668360),new cljs.core.Keyword(null,"row-index","row-index",-828710296),new cljs.core.Keyword(null,"seen-reply-cb","seen-reply-cb",39643564),new cljs.core.Keyword(null,"comments-loaded?","comments-loaded?",-595034611),new cljs.core.Keyword(null,"clear-cell-measure-cb","clear-cell-measure-cb",1839014573),new cljs.core.Keyword(null,"entry-data","entry-data",1970939662),new cljs.core.Keyword(null,"reply-data","reply-data",471571087),new cljs.core.Keyword(null,"add-comment-force-update","add-comment-force-update",1376707794),new cljs.core.Keyword(null,"current-user-id","current-user-id",-2013633451),new cljs.core.Keyword(null,"reply-focus-value","reply-focus-value",1337337974)],[is_mobile_QMARK_,member_QMARK_,row_index,((function (i__46528,s__46527__$1,reply,c__4527__auto__,size__4528__auto__,b__46529,s__46527__$2,temp__5735__auto__,attrs46523,_users_info_hover,_follow_publishers_list,_followers_publishers_count,map__46521,map__46521__$1,entry_data,uuid,comments_count,replies_data,publisher,unseen,comments_loaded_QMARK_,published_at,expanded_replies,is_mobile_QMARK_,reply_item_class,add_comment_focus_value,show_expand_replies_QMARK_,clear_cell_measure_cb,map__46519,map__46519__$1,member_QMARK_,reply_data,show_wrt_QMARK_,row_index,read_data,current_user_data,clear_cell_measure_cb_STAR_,add_comment_force_update){
return (function (){
oc.web.components.stream_reply_item.reply_mark_seen(entry_data,reply);

return clear_cell_measure_cb();
});})(i__46528,s__46527__$1,reply,c__4527__auto__,size__4528__auto__,b__46529,s__46527__$2,temp__5735__auto__,attrs46523,_users_info_hover,_follow_publishers_list,_followers_publishers_count,map__46521,map__46521__$1,entry_data,uuid,comments_count,replies_data,publisher,unseen,comments_loaded_QMARK_,published_at,expanded_replies,is_mobile_QMARK_,reply_item_class,add_comment_focus_value,show_expand_replies_QMARK_,clear_cell_measure_cb,map__46519,map__46519__$1,member_QMARK_,reply_data,show_wrt_QMARK_,row_index,read_data,current_user_data,clear_cell_measure_cb_STAR_,add_comment_force_update))
,comments_loaded_QMARK_,clear_cell_measure_cb,entry_data,reply,add_comment_force_update,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(current_user_data),add_comment_focus_value]))));

var G__46618 = (i__46528 + (1));
i__46528 = G__46618;
continue;
} else {
var G__46619 = (i__46528 + (1));
i__46528 = G__46619;
continue;
}
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__46529),oc$web$components$stream_reply_item$iter__46526(cljs.core.chunk_rest(s__46527__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__46529),null);
}
} else {
var reply = cljs.core.first(s__46527__$2);
if(cljs.core.truth_((function (){var or__4126__auto__ = expanded_replies;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.not(new cljs.core.Keyword(null,"collapsed","collapsed",-628494523).cljs$core$IFn$_invoke$arity$1(reply));
}
})())){
return cljs.core.cons(sablono.interpreter.interpret(oc.web.components.stream_reply_item.comment_item(s,cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"is-mobile?","is-mobile?",2146205507),new cljs.core.Keyword(null,"member?","member?",486668360),new cljs.core.Keyword(null,"row-index","row-index",-828710296),new cljs.core.Keyword(null,"seen-reply-cb","seen-reply-cb",39643564),new cljs.core.Keyword(null,"comments-loaded?","comments-loaded?",-595034611),new cljs.core.Keyword(null,"clear-cell-measure-cb","clear-cell-measure-cb",1839014573),new cljs.core.Keyword(null,"entry-data","entry-data",1970939662),new cljs.core.Keyword(null,"reply-data","reply-data",471571087),new cljs.core.Keyword(null,"add-comment-force-update","add-comment-force-update",1376707794),new cljs.core.Keyword(null,"current-user-id","current-user-id",-2013633451),new cljs.core.Keyword(null,"reply-focus-value","reply-focus-value",1337337974)],[is_mobile_QMARK_,member_QMARK_,row_index,((function (s__46527__$1,reply,s__46527__$2,temp__5735__auto__,attrs46523,_users_info_hover,_follow_publishers_list,_followers_publishers_count,map__46521,map__46521__$1,entry_data,uuid,comments_count,replies_data,publisher,unseen,comments_loaded_QMARK_,published_at,expanded_replies,is_mobile_QMARK_,reply_item_class,add_comment_focus_value,show_expand_replies_QMARK_,clear_cell_measure_cb,map__46519,map__46519__$1,member_QMARK_,reply_data,show_wrt_QMARK_,row_index,read_data,current_user_data,clear_cell_measure_cb_STAR_,add_comment_force_update){
return (function (){
oc.web.components.stream_reply_item.reply_mark_seen(entry_data,reply);

return clear_cell_measure_cb();
});})(s__46527__$1,reply,s__46527__$2,temp__5735__auto__,attrs46523,_users_info_hover,_follow_publishers_list,_followers_publishers_count,map__46521,map__46521__$1,entry_data,uuid,comments_count,replies_data,publisher,unseen,comments_loaded_QMARK_,published_at,expanded_replies,is_mobile_QMARK_,reply_item_class,add_comment_focus_value,show_expand_replies_QMARK_,clear_cell_measure_cb,map__46519,map__46519__$1,member_QMARK_,reply_data,show_wrt_QMARK_,row_index,read_data,current_user_data,clear_cell_measure_cb_STAR_,add_comment_force_update))
,comments_loaded_QMARK_,clear_cell_measure_cb,entry_data,reply,add_comment_force_update,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(current_user_data),add_comment_focus_value]))),oc$web$components$stream_reply_item$iter__46526(cljs.core.rest(s__46527__$2)));
} else {
var G__46620 = cljs.core.rest(s__46527__$2);
s__46527__$1 = G__46620;
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
return iter__4529__auto__(replies_data);
})()),sablono.interpreter.interpret(rum.core.with_key((function (){var G__46530 = new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"activity-data","activity-data",1293689136),entry_data,new cljs.core.Keyword(null,"collapse?","collapse?",720716709),true,new cljs.core.Keyword(null,"add-comment-placeholder","add-comment-placeholder",1617238414),"Reply...",new cljs.core.Keyword(null,"internal-max-width","internal-max-width",-816752310),(cljs.core.truth_(is_mobile_QMARK_)?(oc.web.utils.dom.viewport_width() - (((24) + (1)) * (2))):(524)),new cljs.core.Keyword(null,"add-comment-did-change","add-comment-did-change",-325940488),(function (){
return clear_cell_measure_cb();
}),new cljs.core.Keyword(null,"add-comment-cb","add-comment-cb",1940125120),(function (new_comment_data){
oc.web.actions.reply.replies_add(entry_data,new_comment_data);

return clear_cell_measure_cb();
}),new cljs.core.Keyword(null,"row-index","row-index",-828710296),row_index,new cljs.core.Keyword(null,"add-comment-focus-prefix","add-comment-focus-prefix",1635349699),cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-reply-item","add-comment-focus-prefix","oc.web.components.stream-reply-item/add-comment-focus-prefix",-1660209692).cljs$core$IFn$_invoke$arity$1(s))], null);
return (oc.web.components.ui.add_comment.add_comment.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.add_comment.add_comment.cljs$core$IFn$_invoke$arity$1(G__46530) : oc.web.components.ui.add_comment.add_comment.call(null,G__46530));
})(),["add-comment-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-reply-item","add-comment-focus-prefix","oc.web.components.stream-reply-item/add-comment-focus-prefix",-1660209692).cljs$core$IFn$_invoke$arity$1(s))),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(uuid)].join('')))], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46523),cljs.core.into_array.cljs$core$IFn$_invoke$arity$1((function (){var iter__4529__auto__ = (function oc$web$components$stream_reply_item$iter__46531(s__46532){
return (new cljs.core.LazySeq(null,(function (){
var s__46532__$1 = s__46532;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__46532__$1);
if(temp__5735__auto__){
var s__46532__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__46532__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__46532__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__46534 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__46533 = (0);
while(true){
if((i__46533 < size__4528__auto__)){
var reply = cljs.core._nth(c__4527__auto__,i__46533);
if(cljs.core.truth_((function (){var or__4126__auto__ = expanded_replies;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.not(new cljs.core.Keyword(null,"collapsed","collapsed",-628494523).cljs$core$IFn$_invoke$arity$1(reply));
}
})())){
cljs.core.chunk_append(b__46534,sablono.interpreter.interpret(oc.web.components.stream_reply_item.comment_item(s,cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"is-mobile?","is-mobile?",2146205507),new cljs.core.Keyword(null,"member?","member?",486668360),new cljs.core.Keyword(null,"row-index","row-index",-828710296),new cljs.core.Keyword(null,"seen-reply-cb","seen-reply-cb",39643564),new cljs.core.Keyword(null,"comments-loaded?","comments-loaded?",-595034611),new cljs.core.Keyword(null,"clear-cell-measure-cb","clear-cell-measure-cb",1839014573),new cljs.core.Keyword(null,"entry-data","entry-data",1970939662),new cljs.core.Keyword(null,"reply-data","reply-data",471571087),new cljs.core.Keyword(null,"add-comment-force-update","add-comment-force-update",1376707794),new cljs.core.Keyword(null,"current-user-id","current-user-id",-2013633451),new cljs.core.Keyword(null,"reply-focus-value","reply-focus-value",1337337974)],[is_mobile_QMARK_,member_QMARK_,row_index,((function (i__46533,s__46532__$1,reply,c__4527__auto__,size__4528__auto__,b__46534,s__46532__$2,temp__5735__auto__,attrs46523,_users_info_hover,_follow_publishers_list,_followers_publishers_count,map__46521,map__46521__$1,entry_data,uuid,comments_count,replies_data,publisher,unseen,comments_loaded_QMARK_,published_at,expanded_replies,is_mobile_QMARK_,reply_item_class,add_comment_focus_value,show_expand_replies_QMARK_,clear_cell_measure_cb,map__46519,map__46519__$1,member_QMARK_,reply_data,show_wrt_QMARK_,row_index,read_data,current_user_data,clear_cell_measure_cb_STAR_,add_comment_force_update){
return (function (){
oc.web.components.stream_reply_item.reply_mark_seen(entry_data,reply);

return clear_cell_measure_cb();
});})(i__46533,s__46532__$1,reply,c__4527__auto__,size__4528__auto__,b__46534,s__46532__$2,temp__5735__auto__,attrs46523,_users_info_hover,_follow_publishers_list,_followers_publishers_count,map__46521,map__46521__$1,entry_data,uuid,comments_count,replies_data,publisher,unseen,comments_loaded_QMARK_,published_at,expanded_replies,is_mobile_QMARK_,reply_item_class,add_comment_focus_value,show_expand_replies_QMARK_,clear_cell_measure_cb,map__46519,map__46519__$1,member_QMARK_,reply_data,show_wrt_QMARK_,row_index,read_data,current_user_data,clear_cell_measure_cb_STAR_,add_comment_force_update))
,comments_loaded_QMARK_,clear_cell_measure_cb,entry_data,reply,add_comment_force_update,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(current_user_data),add_comment_focus_value]))));

var G__46623 = (i__46533 + (1));
i__46533 = G__46623;
continue;
} else {
var G__46624 = (i__46533 + (1));
i__46533 = G__46624;
continue;
}
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__46534),oc$web$components$stream_reply_item$iter__46531(cljs.core.chunk_rest(s__46532__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__46534),null);
}
} else {
var reply = cljs.core.first(s__46532__$2);
if(cljs.core.truth_((function (){var or__4126__auto__ = expanded_replies;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.not(new cljs.core.Keyword(null,"collapsed","collapsed",-628494523).cljs$core$IFn$_invoke$arity$1(reply));
}
})())){
return cljs.core.cons(sablono.interpreter.interpret(oc.web.components.stream_reply_item.comment_item(s,cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"is-mobile?","is-mobile?",2146205507),new cljs.core.Keyword(null,"member?","member?",486668360),new cljs.core.Keyword(null,"row-index","row-index",-828710296),new cljs.core.Keyword(null,"seen-reply-cb","seen-reply-cb",39643564),new cljs.core.Keyword(null,"comments-loaded?","comments-loaded?",-595034611),new cljs.core.Keyword(null,"clear-cell-measure-cb","clear-cell-measure-cb",1839014573),new cljs.core.Keyword(null,"entry-data","entry-data",1970939662),new cljs.core.Keyword(null,"reply-data","reply-data",471571087),new cljs.core.Keyword(null,"add-comment-force-update","add-comment-force-update",1376707794),new cljs.core.Keyword(null,"current-user-id","current-user-id",-2013633451),new cljs.core.Keyword(null,"reply-focus-value","reply-focus-value",1337337974)],[is_mobile_QMARK_,member_QMARK_,row_index,((function (s__46532__$1,reply,s__46532__$2,temp__5735__auto__,attrs46523,_users_info_hover,_follow_publishers_list,_followers_publishers_count,map__46521,map__46521__$1,entry_data,uuid,comments_count,replies_data,publisher,unseen,comments_loaded_QMARK_,published_at,expanded_replies,is_mobile_QMARK_,reply_item_class,add_comment_focus_value,show_expand_replies_QMARK_,clear_cell_measure_cb,map__46519,map__46519__$1,member_QMARK_,reply_data,show_wrt_QMARK_,row_index,read_data,current_user_data,clear_cell_measure_cb_STAR_,add_comment_force_update){
return (function (){
oc.web.components.stream_reply_item.reply_mark_seen(entry_data,reply);

return clear_cell_measure_cb();
});})(s__46532__$1,reply,s__46532__$2,temp__5735__auto__,attrs46523,_users_info_hover,_follow_publishers_list,_followers_publishers_count,map__46521,map__46521__$1,entry_data,uuid,comments_count,replies_data,publisher,unseen,comments_loaded_QMARK_,published_at,expanded_replies,is_mobile_QMARK_,reply_item_class,add_comment_focus_value,show_expand_replies_QMARK_,clear_cell_measure_cb,map__46519,map__46519__$1,member_QMARK_,reply_data,show_wrt_QMARK_,row_index,read_data,current_user_data,clear_cell_measure_cb_STAR_,add_comment_force_update))
,comments_loaded_QMARK_,clear_cell_measure_cb,entry_data,reply,add_comment_force_update,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(current_user_data),add_comment_focus_value]))),oc$web$components$stream_reply_item$iter__46531(cljs.core.rest(s__46532__$2)));
} else {
var G__46627 = cljs.core.rest(s__46532__$2);
s__46532__$1 = G__46627;
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
return iter__4529__auto__(replies_data);
})()),sablono.interpreter.interpret(rum.core.with_key((function (){var G__46535 = new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"activity-data","activity-data",1293689136),entry_data,new cljs.core.Keyword(null,"collapse?","collapse?",720716709),true,new cljs.core.Keyword(null,"add-comment-placeholder","add-comment-placeholder",1617238414),"Reply...",new cljs.core.Keyword(null,"internal-max-width","internal-max-width",-816752310),(cljs.core.truth_(is_mobile_QMARK_)?(oc.web.utils.dom.viewport_width() - (((24) + (1)) * (2))):(524)),new cljs.core.Keyword(null,"add-comment-did-change","add-comment-did-change",-325940488),(function (){
return clear_cell_measure_cb();
}),new cljs.core.Keyword(null,"add-comment-cb","add-comment-cb",1940125120),(function (new_comment_data){
oc.web.actions.reply.replies_add(entry_data,new_comment_data);

return clear_cell_measure_cb();
}),new cljs.core.Keyword(null,"row-index","row-index",-828710296),row_index,new cljs.core.Keyword(null,"add-comment-focus-prefix","add-comment-focus-prefix",1635349699),cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-reply-item","add-comment-focus-prefix","oc.web.components.stream-reply-item/add-comment-focus-prefix",-1660209692).cljs$core$IFn$_invoke$arity$1(s))], null);
return (oc.web.components.ui.add_comment.add_comment.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.add_comment.add_comment.cljs$core$IFn$_invoke$arity$1(G__46535) : oc.web.components.ui.add_comment.add_comment.call(null,G__46535));
})(),["add-comment-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-reply-item","add-comment-focus-prefix","oc.web.components.stream-reply-item/add-comment-focus-prefix",-1660209692).cljs$core$IFn$_invoke$arity$1(s))),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(uuid)].join('')))], null)));
})());
}),new cljs.core.PersistentVector(null, 16, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$,rum.core.reactive,rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.stream-reply-item","show-picker","oc.web.components.stream-reply-item/show-picker",-1615000722)),rum.core.local.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentHashSet.EMPTY,new cljs.core.Keyword("oc.web.components.stream-reply-item","replying","oc.web.components.stream-reply-item/replying",-1915074329)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.stream-reply-item","add-comment-focus-prefix","oc.web.components.stream-reply-item/add-comment-focus-prefix",-1660209692)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.stream-reply-item","add-comment-focus-listener","oc.web.components.stream-reply-item/add-comment-focus-listener",9334911)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.stream-reply-item","add-comment-focused","oc.web.components.stream-reply-item/add-comment-focused",1813299804)),oc.web.mixins.ui.refresh_tooltips_mixin,oc.web.mixins.ui.interactive_images_mixin("div.reply-comment-body"),oc.web.mixins.ui.on_window_click_mixin((function (s,e){
if(cljs.core.truth_((function (){var and__4115__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-reply-item","show-picker","oc.web.components.stream-reply-item/show-picker",-1615000722).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(oc.web.lib.utils.event_inside_QMARK_(e,$("div.emoji-mart",rum.core.dom_node(s)).get((0))));
} else {
return and__4115__auto__;
}
})())){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.stream-reply-item","show-picker","oc.web.components.stream-reply-item/show-picker",-1615000722).cljs$core$IFn$_invoke$arity$1(s),null);
} else {
return null;
}
})),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"users-info-hover","users-info-hover",-941434570)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"follow-publishers-list","follow-publishers-list",-374150342)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"followers-publishers-count","followers-publishers-count",-692976579)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915)], 0)),oc.web.mixins.mention.oc_mentions_hover.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"click?","click?",-1210364665),true], null)], 0)),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.stream-reply-item","add-comment-focus-prefix","oc.web.components.stream-reply-item/add-comment-focus-prefix",-1660209692).cljs$core$IFn$_invoke$arity$1(s),oc.web.components.stream_reply_item.add_comment_focus_prefix());

return s;
}),new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
oc.web.components.stream_reply_item.setup_add_comment_focus_listener(s);

return s;
}),new cljs.core.Keyword(null,"did-remount","did-remount",1362550500),(function (_,s){
oc.web.components.stream_reply_item.setup_add_comment_focus_listener(s);

return s;
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
var temp__5735__auto___46631 = cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-reply-item","add-comment-focus-listener","oc.web.components.stream-reply-item/add-comment-focus-listener",9334911).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(temp__5735__auto___46631)){
var add_comment_focus_listener_46632 = temp__5735__auto___46631;
goog.events.unlistenByKey(add_comment_focus_listener_46632);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.stream-reply-item","add-comment-focused","oc.web.components.stream-reply-item/add-comment-focused",1813299804).cljs$core$IFn$_invoke$arity$1(s),false);
} else {
}

return s;
})], null)], null),"stream-reply-item");
oc.web.components.stream_reply_item.count_unseen_comments = (function oc$web$components$stream_reply_item$count_unseen_comments(items){
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (c,item){
return (c + cljs.core.count(cljs.core.filter.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"unseen","unseen",1063275592),new cljs.core.Keyword(null,"replies-data","replies-data",1118937948).cljs$core$IFn$_invoke$arity$1(item))));
}),(0),items);
});
oc.web.components.stream_reply_item.replies_refresh_button = rum.core.build_defcs((function (s,p__46536){
var map__46537 = p__46536;
var map__46537__$1 = (((((!((map__46537 == null))))?(((((map__46537.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46537.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46537):map__46537);
var items_to_render = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46537__$1,new cljs.core.Keyword(null,"items-to-render","items-to-render",660219762));
var replies_badge = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"replies-badge","replies-badge",-112768699));
var delta_new_comments = (oc.web.components.stream_reply_item.count_unseen_comments(items_to_render) - cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-reply-item","initial-unseen-comments","oc.web.components.stream-reply-item/initial-unseen-comments",-699093859).cljs$core$IFn$_invoke$arity$1(s)));
var show_refresh_button_QMARK_ = (function (){var and__4115__auto__ = replies_badge;
if(cljs.core.truth_(and__4115__auto__)){
return (delta_new_comments > (0));
} else {
return and__4115__auto__;
}
})();
return sablono.interpreter.interpret((cljs.core.truth_(show_refresh_button_QMARK_)?(function (){var G__46539 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"message","message",-406056002),(((delta_new_comments > (0)))?[cljs.core.str.cljs$core$IFn$_invoke$arity$1(delta_new_comments)," unread comment",((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(delta_new_comments,(1)))?null:"s")].join(''):"New replies available"),new cljs.core.Keyword(null,"visible","visible",-1024216805),show_refresh_button_QMARK_,new cljs.core.Keyword(null,"class-name","class-name",945142584),new cljs.core.Keyword(null,"replies-refresh-button-container","replies-refresh-button-container",-132312815)], null);
return (oc.web.components.ui.refresh_button.refresh_button.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.refresh_button.refresh_button.cljs$core$IFn$_invoke$arity$1(G__46539) : oc.web.components.ui.refresh_button.refresh_button.call(null,G__46539));
})():null));
}),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"replies-badge","replies-badge",-112768699)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2((0),new cljs.core.Keyword("oc.web.components.stream-reply-item","initial-unseen-comments","oc.web.components.stream-reply-item/initial-unseen-comments",-699093859)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
var props_46633 = cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s));
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.stream-reply-item","initial-unseen-comments","oc.web.components.stream-reply-item/initial-unseen-comments",-699093859).cljs$core$IFn$_invoke$arity$1(s),oc.web.components.stream_reply_item.count_unseen_comments(new cljs.core.Keyword(null,"items-to-render","items-to-render",660219762).cljs$core$IFn$_invoke$arity$1(props_46633)));

return s;
})], null)], null),"replies-refresh-button");

//# sourceMappingURL=oc.web.components.stream_reply_item.js.map
