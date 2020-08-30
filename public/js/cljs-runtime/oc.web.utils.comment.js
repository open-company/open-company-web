goog.provide('oc.web.utils.comment');
var module$node_modules$medium_editor$dist$js$medium_editor=shadow.js.require("module$node_modules$medium_editor$dist$js$medium_editor", {});
oc.web.utils.comment.setup_medium_editor = (function oc$web$utils$comment$setup_medium_editor(comment_node,users_list){
var extentions = ((cljs.core.seq(users_list))?({"mention": oc.web.utils.mention.mention_ext(users_list)}):({}));
var config = cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"autoLink","autoLink",984791968),new cljs.core.Keyword(null,"imageDragging","imageDragging",-1464910814),new cljs.core.Keyword(null,"placeholder","placeholder",-104873083),new cljs.core.Keyword(null,"paste","paste",1975741548),new cljs.core.Keyword(null,"extensions","extensions",-1103629196),new cljs.core.Keyword(null,"targetBlank","targetBlank",-750943627),new cljs.core.Keyword(null,"toolbar","toolbar",-1172789065),new cljs.core.Keyword(null,"keyboardCommands","keyboardCommands",754527288),new cljs.core.Keyword(null,"anchor","anchor",1549638489),new cljs.core.Keyword(null,"anchorPreview","anchorPreview",-420809858)],[true,false,({"text": "Add a reply\u2026", "hideOnClick": true}),({"forcePlainText": false, "cleanPastedHTML": true, "cleanAttrs": ["style","alt","dir","size","face","color","itemprop","name","id"], "cleanTags": ["meta","video","audio","img","button","svg","canvas","figure","input","textarea"], "unwrapTags": ["div","label","font","h1","h2","h3","h4","h5","div","p","ul","ol","li","h6","strong","section","time","em","main","u","form","header","footer","details","summary","nav","abbr","a"]}),extentions,true,false,({"commands": [({"command": false, "key": "B", "meta": true, "shift": false, "alt": false}),({"command": false, "key": "I", "meta": true, "shift": false, "alt": false}),({"command": false, "key": "U", "meta": true, "shift": false, "alt": false})]}),false,false]);
return (new module$node_modules$medium_editor$dist$js$medium_editor(comment_node,cljs.core.clj__GT_js(config)));
});
oc.web.utils.comment.add_comment_content = (function oc$web$utils$comment$add_comment_content(comment_node){
var comment_html = comment_node.innerHTML;
var $comment_node = $("<div/>").html(comment_html);
var _remove_mentions_popup = $comment_node.remove(".oc-mention-popup");
return oc.web.lib.utils.clean_body_html($comment_node.html());
});
oc.web.utils.comment.ungroup_comments = (function oc$web$utils$comment$ungroup_comments(comments){
return cljs.core.filterv((function (p1__41612_SHARP_){
var G__41614 = new cljs.core.Keyword(null,"resource-type","resource-type",1844262326).cljs$core$IFn$_invoke$arity$1(p1__41612_SHARP_);
var fexpr__41613 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"thread","thread",947001524),null,new cljs.core.Keyword(null,"comment","comment",532206069),null], null), null);
return (fexpr__41613.cljs$core$IFn$_invoke$arity$1 ? fexpr__41613.cljs$core$IFn$_invoke$arity$1(G__41614) : fexpr__41613.call(null,G__41614));
}),comments);
});
/**
 * @param {...*} var_args
 */
oc.web.utils.comment.sort_comments = (function() { 
var oc$web$utils$comment$sort_comments__delegate = function (args__33705__auto__){
var ocr_41617 = cljs.core.vec(args__33705__auto__);
try{if(((cljs.core.vector_QMARK_(ocr_41617)) && ((cljs.core.count(ocr_41617) === 1)))){
try{var ocr_41617_0__41619 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41617,(0));
if((ocr_41617_0__41619 == null)){
var comments = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41617,(0));
return cljs.core.PersistentVector.EMPTY;
} else {
throw cljs.core.match.backtrack;

}
}catch (e41625){if((e41625 instanceof Error)){
var e__32662__auto__ = e41625;
if((e__32662__auto__ === cljs.core.match.backtrack)){
try{var ocr_41617_0__41619 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41617,(0));
if(cljs.core.map_QMARK_(ocr_41617_0__41619)){
var comments = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41617,(0));
var G__41629 = cljs.core.vals(comments);
return (oc.web.utils.comment.sort_comments.cljs$core$IFn$_invoke$arity$1 ? oc.web.utils.comment.sort_comments.cljs$core$IFn$_invoke$arity$1(G__41629) : oc.web.utils.comment.sort_comments.call(null,G__41629));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41626){if((e41626 instanceof Error)){
var e__32662__auto____$1 = e41626;
if((e__32662__auto____$1 === cljs.core.match.backtrack)){
try{var ocr_41617_0__41619 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41617,(0));
if(cljs.core.coll_QMARK_(ocr_41617_0__41619)){
var comments = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41617,(0));
return cljs.core.vec(cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"created-at","created-at",-89248644),comments));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41628){if((e41628 instanceof Error)){
var e__32662__auto____$2 = e41628;
if((e__32662__auto____$2 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$2;
}
} else {
throw e41628;

}
}} else {
throw e__32662__auto____$1;
}
} else {
throw e41626;

}
}} else {
throw e__32662__auto__;
}
} else {
throw e41625;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41622){if((e41622 instanceof Error)){
var e__32662__auto__ = e41622;
if((e__32662__auto__ === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41617)) && ((cljs.core.count(ocr_41617) === 2)))){
try{var ocr_41617_0__41620 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41617,(0));
if(cljs.core.coll_QMARK_(ocr_41617_0__41620)){
var comments = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41617,(0));
var parent_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41617,(1));
var check_fn = (cljs.core.truth_(parent_uuid)?(function (p1__41615_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"parent-uuid","parent-uuid",-2003485227).cljs$core$IFn$_invoke$arity$1(p1__41615_SHARP_),parent_uuid);
}):cljs.core.comp.cljs$core$IFn$_invoke$arity$2(cljs.core.empty_QMARK_,new cljs.core.Keyword(null,"parent-uuid","parent-uuid",-2003485227)));
var filtered_comments = cljs.core.filterv(check_fn,comments);
var sorted_comments = cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"created-at","created-at",-89248644),filtered_comments);
if((parent_uuid == null)){
return cljs.core.vec(cljs.core.reverse(sorted_comments));
} else {
return cljs.core.vec(sorted_comments);
}
} else {
throw cljs.core.match.backtrack;

}
}catch (e41624){if((e41624 instanceof Error)){
var e__32662__auto____$1 = e41624;
if((e__32662__auto____$1 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$1;
}
} else {
throw e41624;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41623){if((e41623 instanceof Error)){
var e__32662__auto____$1 = e41623;
if((e__32662__auto____$1 === cljs.core.match.backtrack)){
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ocr_41617)].join('')));
} else {
throw e__32662__auto____$1;
}
} else {
throw e41623;

}
}} else {
throw e__32662__auto__;
}
} else {
throw e41622;

}
}};
var oc$web$utils$comment$sort_comments = function (var_args){
var args__33705__auto__ = null;
if (arguments.length > 0) {
var G__41700__i = 0, G__41700__a = new Array(arguments.length -  0);
while (G__41700__i < G__41700__a.length) {G__41700__a[G__41700__i] = arguments[G__41700__i + 0]; ++G__41700__i;}
  args__33705__auto__ = new cljs.core.IndexedSeq(G__41700__a,0,null);
} 
return oc$web$utils$comment$sort_comments__delegate.call(this,args__33705__auto__);};
oc$web$utils$comment$sort_comments.cljs$lang$maxFixedArity = 0;
oc$web$utils$comment$sort_comments.cljs$lang$applyTo = (function (arglist__41701){
var args__33705__auto__ = cljs.core.seq(arglist__41701);
return oc$web$utils$comment$sort_comments__delegate(args__33705__auto__);
});
oc$web$utils$comment$sort_comments.cljs$core$IFn$_invoke$arity$variadic = oc$web$utils$comment$sort_comments__delegate;
return oc$web$utils$comment$sort_comments;
})()
;
oc.web.utils.comment.get_collapsed_item = (function oc$web$utils$comment$get_collapsed_item(item,collapsed_map){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([item,cljs.core.get.cljs$core$IFn$_invoke$arity$2(collapsed_map,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(item))], 0));
});
oc.web.utils.comment.is_collapsed_QMARK_ = (function oc$web$utils$comment$is_collapsed_QMARK_(item,collapsed_map){
return new cljs.core.Keyword(null,"collapsed","collapsed",-628494523).cljs$core$IFn$_invoke$arity$1(oc.web.utils.comment.get_collapsed_item(item,collapsed_map));
});
/**
 * A comment is unread if it's created-at is past the last seen-at of the contianer it belongs to.
 * @param {...*} var_args
 */
oc.web.utils.comment.comment_unread_QMARK_ = (function() { 
var oc$web$utils$comment$comment_unread_QMARK___delegate = function (args__33705__auto__){
var ocr_41632 = cljs.core.vec(args__33705__auto__);
try{if(((cljs.core.vector_QMARK_(ocr_41632)) && ((cljs.core.count(ocr_41632) === 2)))){
try{var ocr_41632_0__41634 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41632,(0));
if(cljs.core.map_QMARK_(ocr_41632_0__41634)){
var comment_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41632,(0));
var last_read_at = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41632,(1));
var G__41639 = new cljs.core.Keyword(null,"created-at","created-at",-89248644).cljs$core$IFn$_invoke$arity$1(comment_data);
var G__41640 = last_read_at;
return (oc.web.utils.comment.comment_unread_QMARK_.cljs$core$IFn$_invoke$arity$2 ? oc.web.utils.comment.comment_unread_QMARK_.cljs$core$IFn$_invoke$arity$2(G__41639,G__41640) : oc.web.utils.comment.comment_unread_QMARK_.call(null,G__41639,G__41640));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41637){if((e41637 instanceof Error)){
var e__32662__auto__ = e41637;
if((e__32662__auto__ === cljs.core.match.backtrack)){
try{var ocr_41632_1__41635 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41632,(1));
if((function (p1__41631_SHARP_){
return (((p1__41631_SHARP_ == null)) || (typeof p1__41631_SHARP_ === 'string'));
})(ocr_41632_1__41635)){
var last_read_at = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41632,(1));
var created_at = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41632,(0));
return (cljs.core.compare(created_at,last_read_at) > (0));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41638){if((e41638 instanceof Error)){
var e__32662__auto____$1 = e41638;
if((e__32662__auto____$1 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$1;
}
} else {
throw e41638;

}
}} else {
throw e__32662__auto__;
}
} else {
throw e41637;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41636){if((e41636 instanceof Error)){
var e__32662__auto__ = e41636;
if((e__32662__auto__ === cljs.core.match.backtrack)){
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ocr_41632)].join('')));
} else {
throw e__32662__auto__;
}
} else {
throw e41636;

}
}};
var oc$web$utils$comment$comment_unread_QMARK_ = function (var_args){
var args__33705__auto__ = null;
if (arguments.length > 0) {
var G__41713__i = 0, G__41713__a = new Array(arguments.length -  0);
while (G__41713__i < G__41713__a.length) {G__41713__a[G__41713__i] = arguments[G__41713__i + 0]; ++G__41713__i;}
  args__33705__auto__ = new cljs.core.IndexedSeq(G__41713__a,0,null);
} 
return oc$web$utils$comment$comment_unread_QMARK___delegate.call(this,args__33705__auto__);};
oc$web$utils$comment$comment_unread_QMARK_.cljs$lang$maxFixedArity = 0;
oc$web$utils$comment$comment_unread_QMARK_.cljs$lang$applyTo = (function (arglist__41714){
var args__33705__auto__ = cljs.core.seq(arglist__41714);
return oc$web$utils$comment$comment_unread_QMARK___delegate(args__33705__auto__);
});
oc$web$utils$comment$comment_unread_QMARK_.cljs$core$IFn$_invoke$arity$variadic = oc$web$utils$comment$comment_unread_QMARK___delegate;
return oc$web$utils$comment$comment_unread_QMARK_;
})()
;
oc.web.utils.comment.is_unseen_QMARK_ = (function oc$web$utils$comment$is_unseen_QMARK_(item,collapsed_map){
return cljs.core.not(new cljs.core.Keyword(null,"unseen","unseen",1063275592).cljs$core$IFn$_invoke$arity$1(oc.web.utils.comment.get_collapsed_item(item,collapsed_map)));
});
/**
 * A comment is unseen if it's created-at is later than the last seen-at of the container it belongs to.
 * @param {...*} var_args
 */
oc.web.utils.comment.comment_unseen_QMARK_ = (function() { 
var oc$web$utils$comment$comment_unseen_QMARK___delegate = function (args__33705__auto__){
var ocr_41642 = cljs.core.vec(args__33705__auto__);
try{if(((cljs.core.vector_QMARK_(ocr_41642)) && ((cljs.core.count(ocr_41642) === 2)))){
try{var ocr_41642_0__41644 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41642,(0));
if(cljs.core.map_QMARK_(ocr_41642_0__41644)){
var comment_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41642,(0));
var container_seen_at = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41642,(1));
var G__41649 = new cljs.core.Keyword(null,"created-at","created-at",-89248644).cljs$core$IFn$_invoke$arity$1(comment_data);
var G__41650 = container_seen_at;
return (oc.web.utils.comment.comment_unseen_QMARK_.cljs$core$IFn$_invoke$arity$2 ? oc.web.utils.comment.comment_unseen_QMARK_.cljs$core$IFn$_invoke$arity$2(G__41649,G__41650) : oc.web.utils.comment.comment_unseen_QMARK_.call(null,G__41649,G__41650));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41647){if((e41647 instanceof Error)){
var e__32662__auto__ = e41647;
if((e__32662__auto__ === cljs.core.match.backtrack)){
try{var ocr_41642_1__41645 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41642,(1));
if((function (p1__41641_SHARP_){
return (((p1__41641_SHARP_ == null)) || (typeof p1__41641_SHARP_ === 'string'));
})(ocr_41642_1__41645)){
var container_seen_at = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41642,(1));
var created_at = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41642,(0));
return (cljs.core.compare(created_at,container_seen_at) > (0));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41648){if((e41648 instanceof Error)){
var e__32662__auto____$1 = e41648;
if((e__32662__auto____$1 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$1;
}
} else {
throw e41648;

}
}} else {
throw e__32662__auto__;
}
} else {
throw e41647;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41646){if((e41646 instanceof Error)){
var e__32662__auto__ = e41646;
if((e__32662__auto__ === cljs.core.match.backtrack)){
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ocr_41642)].join('')));
} else {
throw e__32662__auto__;
}
} else {
throw e41646;

}
}};
var oc$web$utils$comment$comment_unseen_QMARK_ = function (var_args){
var args__33705__auto__ = null;
if (arguments.length > 0) {
var G__41720__i = 0, G__41720__a = new Array(arguments.length -  0);
while (G__41720__i < G__41720__a.length) {G__41720__a[G__41720__i] = arguments[G__41720__i + 0]; ++G__41720__i;}
  args__33705__auto__ = new cljs.core.IndexedSeq(G__41720__a,0,null);
} 
return oc$web$utils$comment$comment_unseen_QMARK___delegate.call(this,args__33705__auto__);};
oc$web$utils$comment$comment_unseen_QMARK_.cljs$lang$maxFixedArity = 0;
oc$web$utils$comment$comment_unseen_QMARK_.cljs$lang$applyTo = (function (arglist__41721){
var args__33705__auto__ = cljs.core.seq(arglist__41721);
return oc$web$utils$comment$comment_unseen_QMARK___delegate(args__33705__auto__);
});
oc$web$utils$comment$comment_unseen_QMARK_.cljs$core$IFn$_invoke$arity$variadic = oc$web$utils$comment$comment_unseen_QMARK___delegate;
return oc$web$utils$comment$comment_unseen_QMARK_;
})()
;
oc.web.utils.comment.default_expanded_comments_count = (3);
/**
 * Add a collapsed flag to every comment that is a reply and is not unseen.
 * Also add unseen? flag to every unseen one. Add a count of the collapsed
 * comments to each root comment.
 * @param {...*} var_args
 */
oc.web.utils.comment.collapse_comments = (function() { 
var oc$web$utils$comment$collapse_comments__delegate = function (args__33705__auto__){
var ocr_41657 = cljs.core.vec(args__33705__auto__);
try{if(((cljs.core.vector_QMARK_(ocr_41657)) && ((cljs.core.count(ocr_41657) === 2)))){
try{var ocr_41657_0__41659 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41657,(0));
if(cljs.core.empty_QMARK_(ocr_41657_0__41659)){
var _comments = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41657,(0));
var _container_seen_at = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41657,(1));
return cljs.core.PersistentVector.EMPTY;
} else {
throw cljs.core.match.backtrack;

}
}catch (e41662){if((e41662 instanceof Error)){
var e__32662__auto__ = e41662;
if((e__32662__auto__ === cljs.core.match.backtrack)){
try{var ocr_41657_0__41659 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41657,(0));
if((function (p1__41651_SHARP_){
return ((cljs.core.coll_QMARK_(p1__41651_SHARP_)) && ((cljs.core.count(p1__41651_SHARP_) <= oc.web.utils.comment.default_expanded_comments_count)));
})(ocr_41657_0__41659)){
try{var ocr_41657_1__41660 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41657,(1));
if((function (p1__41652_SHARP_){
return (((p1__41652_SHARP_ == null)) || (typeof p1__41652_SHARP_ === 'string'));
})(ocr_41657_1__41660)){
var container_seen_at = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41657,(1));
var comments = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41657,(0));
return cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__41653_SHARP_){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__41653_SHARP_,new cljs.core.Keyword(null,"collapsed","collapsed",-628494523),false);
}),comments);
} else {
throw cljs.core.match.backtrack;

}
}catch (e41666){if((e41666 instanceof Error)){
var e__32662__auto____$1 = e41666;
if((e__32662__auto____$1 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$1;
}
} else {
throw e41666;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41663){if((e41663 instanceof Error)){
var e__32662__auto____$1 = e41663;
if((e__32662__auto____$1 === cljs.core.match.backtrack)){
try{var ocr_41657_0__41659 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41657,(0));
if((function (cs){
return ((cljs.core.coll_QMARK_(cs)) && ((cljs.core.count(cs) > oc.web.utils.comment.default_expanded_comments_count)));
})(ocr_41657_0__41659)){
try{var ocr_41657_1__41660 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41657,(1));
if((function (p1__41654_SHARP_){
return (((p1__41654_SHARP_ == null)) || (typeof p1__41654_SHARP_ === 'string'));
})(ocr_41657_1__41660)){
var container_seen_at = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41657,(1));
var comments = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41657,(0));
var comments_count = cljs.core.count(comments);
var min_expanded_index = (comments_count - oc.web.utils.comment.default_expanded_comments_count);
var min_unseen_index = oc.web.lib.utils.index_of(comments,new cljs.core.Keyword(null,"unseen","unseen",1063275592));
var first_expanded_index = ((typeof min_unseen_index === 'number')?(function (){var x__4217__auto__ = min_unseen_index;
var y__4218__auto__ = min_expanded_index;
return ((x__4217__auto__ < y__4218__auto__) ? x__4217__auto__ : y__4218__auto__);
})():min_expanded_index);
var collapsed_comments = cljs.core.subvec.cljs$core$IFn$_invoke$arity$3(cljs.core.vec(comments),(0),first_expanded_index);
var expanded_comments = cljs.core.subvec.cljs$core$IFn$_invoke$arity$3(cljs.core.vec(comments),first_expanded_index,comments_count);
return cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__41655_SHARP_){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__41655_SHARP_,new cljs.core.Keyword(null,"collapsed","collapsed",-628494523),true);
}),collapsed_comments),cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__41656_SHARP_){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__41656_SHARP_,new cljs.core.Keyword(null,"collapsed","collapsed",-628494523),false);
}),expanded_comments)));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41665){if((e41665 instanceof Error)){
var e__32662__auto____$2 = e41665;
if((e__32662__auto____$2 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$2;
}
} else {
throw e41665;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41664){if((e41664 instanceof Error)){
var e__32662__auto____$2 = e41664;
if((e__32662__auto____$2 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$2;
}
} else {
throw e41664;

}
}} else {
throw e__32662__auto____$1;
}
} else {
throw e41663;

}
}} else {
throw e__32662__auto__;
}
} else {
throw e41662;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41661){if((e41661 instanceof Error)){
var e__32662__auto__ = e41661;
if((e__32662__auto__ === cljs.core.match.backtrack)){
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ocr_41657)].join('')));
} else {
throw e__32662__auto__;
}
} else {
throw e41661;

}
}};
var oc$web$utils$comment$collapse_comments = function (var_args){
var args__33705__auto__ = null;
if (arguments.length > 0) {
var G__41749__i = 0, G__41749__a = new Array(arguments.length -  0);
while (G__41749__i < G__41749__a.length) {G__41749__a[G__41749__i] = arguments[G__41749__i + 0]; ++G__41749__i;}
  args__33705__auto__ = new cljs.core.IndexedSeq(G__41749__a,0,null);
} 
return oc$web$utils$comment$collapse_comments__delegate.call(this,args__33705__auto__);};
oc$web$utils$comment$collapse_comments.cljs$lang$maxFixedArity = 0;
oc$web$utils$comment$collapse_comments.cljs$lang$applyTo = (function (arglist__41750){
var args__33705__auto__ = cljs.core.seq(arglist__41750);
return oc$web$utils$comment$collapse_comments__delegate(args__33705__auto__);
});
oc$web$utils$comment$collapse_comments.cljs$core$IFn$_invoke$arity$variadic = oc$web$utils$comment$collapse_comments__delegate;
return oc$web$utils$comment$collapse_comments;
})()
;
/**
 * @param {...*} var_args
 */
oc.web.utils.comment.add_comment_focus_value = (function() { 
var oc$web$utils$comment$add_comment_focus_value__delegate = function (args__33705__auto__){
var ocr_41667 = cljs.core.vec(args__33705__auto__);
try{if(((cljs.core.vector_QMARK_(ocr_41667)) && ((cljs.core.count(ocr_41667) === 2)))){
try{var ocr_41667_0__41669 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41667,(0));
if(typeof ocr_41667_0__41669 === 'string'){
try{var ocr_41667_1__41670 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41667,(1));
if(cljs.core.map_QMARK_(ocr_41667_1__41670)){
var comment_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41667,(1));
var prefix = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41667,(0));
var G__41688 = prefix;
var G__41689 = new cljs.core.Keyword(null,"resource-uuid","resource-uuid",1798351789).cljs$core$IFn$_invoke$arity$1(comment_data);
var G__41690 = new cljs.core.Keyword(null,"parent-uuid","parent-uuid",-2003485227).cljs$core$IFn$_invoke$arity$1(comment_data);
var G__41691 = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(comment_data);
return (oc.web.utils.comment.add_comment_focus_value.cljs$core$IFn$_invoke$arity$4 ? oc.web.utils.comment.add_comment_focus_value.cljs$core$IFn$_invoke$arity$4(G__41688,G__41689,G__41690,G__41691) : oc.web.utils.comment.add_comment_focus_value.call(null,G__41688,G__41689,G__41690,G__41691));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41686){if((e41686 instanceof Error)){
var e__32662__auto__ = e41686;
if((e__32662__auto__ === cljs.core.match.backtrack)){
try{var ocr_41667_1__41670 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41667,(1));
if(typeof ocr_41667_1__41670 === 'string'){
var entry_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41667,(1));
var prefix = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41667,(0));
return (oc.web.utils.comment.add_comment_focus_value.cljs$core$IFn$_invoke$arity$4 ? oc.web.utils.comment.add_comment_focus_value.cljs$core$IFn$_invoke$arity$4(prefix,entry_uuid,null,null) : oc.web.utils.comment.add_comment_focus_value.call(null,prefix,entry_uuid,null,null));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41687){if((e41687 instanceof Error)){
var e__32662__auto____$1 = e41687;
if((e__32662__auto____$1 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$1;
}
} else {
throw e41687;

}
}} else {
throw e__32662__auto__;
}
} else {
throw e41686;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41685){if((e41685 instanceof Error)){
var e__32662__auto__ = e41685;
if((e__32662__auto__ === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto__;
}
} else {
throw e41685;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41678){if((e41678 instanceof Error)){
var e__32662__auto__ = e41678;
if((e__32662__auto__ === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41667)) && ((cljs.core.count(ocr_41667) === 3)))){
try{var ocr_41667_0__41671 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41667,(0));
if(typeof ocr_41667_0__41671 === 'string'){
try{var ocr_41667_1__41672 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41667,(1));
if(typeof ocr_41667_1__41672 === 'string'){
var entry_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41667,(1));
var prefix = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41667,(0));
var parent_comment_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41667,(2));
return (oc.web.utils.comment.add_comment_focus_value.cljs$core$IFn$_invoke$arity$4 ? oc.web.utils.comment.add_comment_focus_value.cljs$core$IFn$_invoke$arity$4(prefix,entry_uuid,parent_comment_uuid,null) : oc.web.utils.comment.add_comment_focus_value.call(null,prefix,entry_uuid,parent_comment_uuid,null));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41684){if((e41684 instanceof Error)){
var e__32662__auto____$1 = e41684;
if((e__32662__auto____$1 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$1;
}
} else {
throw e41684;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41683){if((e41683 instanceof Error)){
var e__32662__auto____$1 = e41683;
if((e__32662__auto____$1 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$1;
}
} else {
throw e41683;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41679){if((e41679 instanceof Error)){
var e__32662__auto____$1 = e41679;
if((e__32662__auto____$1 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41667)) && ((cljs.core.count(ocr_41667) === 4)))){
try{var ocr_41667_0__41674 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41667,(0));
if(typeof ocr_41667_0__41674 === 'string'){
try{var ocr_41667_1__41675 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41667,(1));
if(typeof ocr_41667_1__41675 === 'string'){
var entry_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41667,(1));
var prefix = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41667,(0));
var parent_comment_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41667,(2));
var edit_comment_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41667,(3));
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(prefix),"-",oc.web.dispatcher.add_comment_string_key.cljs$core$IFn$_invoke$arity$3(entry_uuid,parent_comment_uuid,edit_comment_uuid)].join('');
} else {
throw cljs.core.match.backtrack;

}
}catch (e41682){if((e41682 instanceof Error)){
var e__32662__auto____$2 = e41682;
if((e__32662__auto____$2 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$2;
}
} else {
throw e41682;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41681){if((e41681 instanceof Error)){
var e__32662__auto____$2 = e41681;
if((e__32662__auto____$2 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$2;
}
} else {
throw e41681;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41680){if((e41680 instanceof Error)){
var e__32662__auto____$2 = e41680;
if((e__32662__auto____$2 === cljs.core.match.backtrack)){
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ocr_41667)].join('')));
} else {
throw e__32662__auto____$2;
}
} else {
throw e41680;

}
}} else {
throw e__32662__auto____$1;
}
} else {
throw e41679;

}
}} else {
throw e__32662__auto__;
}
} else {
throw e41678;

}
}};
var oc$web$utils$comment$add_comment_focus_value = function (var_args){
var args__33705__auto__ = null;
if (arguments.length > 0) {
var G__41776__i = 0, G__41776__a = new Array(arguments.length -  0);
while (G__41776__i < G__41776__a.length) {G__41776__a[G__41776__i] = arguments[G__41776__i + 0]; ++G__41776__i;}
  args__33705__auto__ = new cljs.core.IndexedSeq(G__41776__a,0,null);
} 
return oc$web$utils$comment$add_comment_focus_value__delegate.call(this,args__33705__auto__);};
oc$web$utils$comment$add_comment_focus_value.cljs$lang$maxFixedArity = 0;
oc$web$utils$comment$add_comment_focus_value.cljs$lang$applyTo = (function (arglist__41777){
var args__33705__auto__ = cljs.core.seq(arglist__41777);
return oc$web$utils$comment$add_comment_focus_value__delegate(args__33705__auto__);
});
oc$web$utils$comment$add_comment_focus_value.cljs$core$IFn$_invoke$arity$variadic = oc$web$utils$comment$add_comment_focus_value__delegate;
return oc$web$utils$comment$add_comment_focus_value;
})()
;

//# sourceMappingURL=oc.web.utils.comment.js.map
