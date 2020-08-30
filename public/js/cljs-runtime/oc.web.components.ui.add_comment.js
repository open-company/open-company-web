goog.provide('oc.web.components.ui.add_comment');
oc.web.components.ui.add_comment.focus_value = (function oc$web$components$ui$add_comment$focus_value(s){
var map__46040 = cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s));
var map__46040__$1 = (((((!((map__46040 == null))))?(((((map__46040.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46040.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46040):map__46040);
var activity_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46040__$1,new cljs.core.Keyword(null,"activity-data","activity-data",1293689136));
var parent_comment_uuid = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46040__$1,new cljs.core.Keyword(null,"parent-comment-uuid","parent-comment-uuid",-1757329834));
var add_comment_focus_prefix = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46040__$1,new cljs.core.Keyword(null,"add-comment-focus-prefix","add-comment-focus-prefix",1635349699));
var edit_comment_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46040__$1,new cljs.core.Keyword(null,"edit-comment-data","edit-comment-data",-1510189888));
return oc.web.utils.comment.add_comment_focus_value.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([add_comment_focus_prefix,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data),parent_comment_uuid,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(edit_comment_data)], 0));
});
oc.web.components.ui.add_comment.add_comment_field = (function oc$web$components$ui$add_comment$add_comment_field(s){
return rum.core.ref_node(s,"editor-node");
});
oc.web.components.ui.add_comment.add_comment_body = (function oc$web$components$ui$add_comment$add_comment_body(s){
var temp__5735__auto__ = oc.web.components.ui.add_comment.add_comment_field(s);
if(cljs.core.truth_(temp__5735__auto__)){
var field = temp__5735__auto__;
return field.innerHTML;
} else {
return null;
}
});
oc.web.components.ui.add_comment.add_comment_data = (function oc$web$components$ui$add_comment$add_comment_data(var_args){
var G__46043 = arguments.length;
switch (G__46043) {
case 1:
return oc.web.components.ui.add_comment.add_comment_data.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.components.ui.add_comment.add_comment_data.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.components.ui.add_comment.add_comment_data.cljs$core$IFn$_invoke$arity$1 = (function (s){
return oc.web.components.ui.add_comment.add_comment_data.cljs$core$IFn$_invoke$arity$2(s,cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.add-comment","add-comment-key","oc.web.components.ui.add-comment/add-comment-key",-400996276).cljs$core$IFn$_invoke$arity$1(s)));
}));

(oc.web.components.ui.add_comment.add_comment_data.cljs$core$IFn$_invoke$arity$2 = (function (s,add_comment_key){
var add_comment_data = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"add-comment-data","add-comment-data",-1679568046)));
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(add_comment_data,add_comment_key);
}));

(oc.web.components.ui.add_comment.add_comment_data.cljs$lang$maxFixedArity = 2);

oc.web.components.ui.add_comment.enable_post_button_QMARK_ = (function oc$web$components$ui$add_comment$enable_post_button_QMARK_(s){
var map__46044 = cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s));
var map__46044__$1 = (((((!((map__46044 == null))))?(((((map__46044.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46044.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46044):map__46044);
var edit_comment_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46044__$1,new cljs.core.Keyword(null,"edit-comment-data","edit-comment-data",-1510189888));
var activity_add_comment_data = oc.web.components.ui.add_comment.add_comment_data.cljs$core$IFn$_invoke$arity$1(s);
var comment_text = oc.web.components.ui.add_comment.add_comment_body(s);
return cljs.core.boolean$(((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(comment_text,new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(edit_comment_data))) && ((!(oc.web.utils.activity.empty_body_QMARK_(comment_text))))));
});
oc.web.components.ui.add_comment.toggle_post_button = (function oc$web$components$ui$add_comment$toggle_post_button(s){
var enabled_QMARK_ = oc.web.components.ui.add_comment.enable_post_button_QMARK_(s);
cljs.core.compare_and_set_BANG_(new cljs.core.Keyword("oc.web.components.ui.add-comment","post-enabled","oc.web.components.ui.add-comment/post-enabled",-1405037618).cljs$core$IFn$_invoke$arity$1(s),(!(enabled_QMARK_)),enabled_QMARK_);

return enabled_QMARK_;
});
oc.web.components.ui.add_comment.maybe_expand = (function oc$web$components$ui$add_comment$maybe_expand(s){
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.add-comment","collapsed","oc.web.components.ui.add-comment/collapsed",-1138653880).cljs$core$IFn$_invoke$arity$1(s)))){
if(cljs.core.truth_(oc.web.components.ui.add_comment.add_comment_field(s))){
return cljs.core.compare_and_set_BANG_(new cljs.core.Keyword("oc.web.components.ui.add-comment","collapsed","oc.web.components.ui.add-comment/collapsed",-1138653880).cljs$core$IFn$_invoke$arity$1(s),true,false);
} else {
return null;
}
} else {
return null;
}
});
oc.web.components.ui.add_comment.multiple_lines_QMARK_ = (function oc$web$components$ui$add_comment$multiple_lines_QMARK_(s){
if(((cljs.core.not(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.add-comment","collapsed","oc.web.components.ui.add-comment/collapsed",-1138653880).cljs$core$IFn$_invoke$arity$1(s)))) && (cljs.core.not(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.add-comment","multiple-lines","oc.web.components.ui.add-comment/multiple-lines",765343770).cljs$core$IFn$_invoke$arity$1(s)))))){
var temp__5735__auto__ = oc.web.components.ui.add_comment.add_comment_field(s);
if(cljs.core.truth_(temp__5735__auto__)){
var f = temp__5735__auto__;
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.add-comment","multiple-lines","oc.web.components.ui.add-comment/multiple-lines",765343770).cljs$core$IFn$_invoke$arity$1(s),(((f.scrollWidth > cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.add-comment","inline-reply-max-width","oc.web.components.ui.add-comment/inline-reply-max-width",-533206291).cljs$core$IFn$_invoke$arity$1(s)))) || ((f.scrollHeight > (20)))));
} else {
return null;
}
} else {
return null;
}
});
oc.web.components.ui.add_comment.fix_selection = (function oc$web$components$ui$add_comment$fix_selection(s){
var el = oc.web.components.ui.add_comment.add_comment_field(s);
var sel = OCStaticTextareaSaveSelection();
if(cljs.core.truth_((function (){var and__4115__auto__ = el;
if(cljs.core.truth_(and__4115__auto__)){
var and__4115__auto____$1 = sel;
if(cljs.core.truth_(and__4115__auto____$1)){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(sel.anchorTarget,el)){
var and__4115__auto____$2 = el.firstElementChild;
if(cljs.core.truth_(and__4115__auto____$2)){
var and__4115__auto____$3 = el.firstElementChild.firstElementChild;
if(cljs.core.truth_(and__4115__auto____$3)){
return oc.web.utils.activity.empty_body_QMARK_(el.innerHTML);
} else {
return and__4115__auto____$3;
}
} else {
return and__4115__auto____$2;
}
} else {
return false;
}
} else {
return and__4115__auto____$1;
}
} else {
return and__4115__auto__;
}
})())){
sel.removeAllRanges();

return sel.addRange((new Range(el.firstElementChild,(0))));
} else {
return null;
}
});
oc.web.components.ui.add_comment.focus = (function oc$web$components$ui$add_comment$focus(s){
oc.web.components.ui.add_comment.maybe_expand(s);

oc.web.components.ui.add_comment.multiple_lines_QMARK_(s);

return oc.web.components.ui.add_comment.toggle_post_button(s);
});
oc.web.components.ui.add_comment.blur = (function oc$web$components$ui$add_comment$blur(s){
oc.web.actions.comment.add_comment_blur(oc.web.components.ui.add_comment.focus_value(s));

var toggle = oc.web.components.ui.add_comment.toggle_post_button(s);
if(cljs.core.truth_((((!(toggle)))?new cljs.core.Keyword(null,"collapse?","collapse?",720716709).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s))):false))){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.add-comment","collapsed","oc.web.components.ui.add-comment/collapsed",-1138653880).cljs$core$IFn$_invoke$arity$1(s),true);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.add-comment","multiple-lines","oc.web.components.ui.add-comment/multiple-lines",765343770).cljs$core$IFn$_invoke$arity$1(s),false);
} else {
return null;
}
});
oc.web.components.ui.add_comment.send_clicked = (function oc$web$components$ui$add_comment$send_clicked(event,s){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.add-comment","collapsed","oc.web.components.ui.add-comment/collapsed",-1138653880).cljs$core$IFn$_invoke$arity$1(s),true);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.add-comment","multiple-lines","oc.web.components.ui.add-comment/multiple-lines",765343770).cljs$core$IFn$_invoke$arity$1(s),false);

var add_comment_div = oc.web.components.ui.add_comment.add_comment_field(s);
var comment_body = oc.web.utils.comment.add_comment_content(add_comment_div);
var map__46047 = cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s));
var map__46047__$1 = (((((!((map__46047 == null))))?(((((map__46047.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46047.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46047):map__46047);
var activity_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46047__$1,new cljs.core.Keyword(null,"activity-data","activity-data",1293689136));
var parent_comment_uuid = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46047__$1,new cljs.core.Keyword(null,"parent-comment-uuid","parent-comment-uuid",-1757329834));
var dismiss_reply_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46047__$1,new cljs.core.Keyword(null,"dismiss-reply-cb","dismiss-reply-cb",1894601324));
var edit_comment_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46047__$1,new cljs.core.Keyword(null,"edit-comment-data","edit-comment-data",-1510189888));
var scroll_after_posting_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46047__$1,new cljs.core.Keyword(null,"scroll-after-posting?","scroll-after-posting?",-721807257));
var add_comment_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46047__$1,new cljs.core.Keyword(null,"add-comment-cb","add-comment-cb",1940125120));
var save_done_cb = (function (success){
if(cljs.core.truth_(success)){
var temp__5735__auto__ = oc.web.components.ui.add_comment.add_comment_field(s);
if(cljs.core.truth_(temp__5735__auto__)){
var el = temp__5735__auto__;
return (el.innerHTML = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.add-comment","initial-add-comment","oc.web.components.ui.add-comment/initial-add-comment",1313376568).cljs$core$IFn$_invoke$arity$1(s)));
} else {
return null;
}
} else {
return oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"title","title",636505583),"An error occurred while saving your comment.",new cljs.core.Keyword(null,"description","description",-1428560544),"Please try again",new cljs.core.Keyword(null,"dismiss","dismiss",412569545),true,new cljs.core.Keyword(null,"expire","expire",-70657108),(3),new cljs.core.Keyword(null,"id","id",-1388402092),(cljs.core.truth_(edit_comment_data)?new cljs.core.Keyword(null,"update-comment-error","update-comment-error",-799165147):new cljs.core.Keyword(null,"add-comment-error","add-comment-error",1800913309))], null));
}
});
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("oc.web.components.ui.add-comment","initial-add-comment","oc.web.components.ui.add-comment/initial-add-comment",1313376568).cljs$core$IFn$_invoke$arity$1(s),(function (p1__46046_SHARP_){
if(cljs.core.truth_(edit_comment_data)){
return p1__46046_SHARP_;
} else {
return oc.web.utils.activity.empty_body_html;
}
}));

if(cljs.core.truth_(add_comment_div)){
(add_comment_div.innerHTML = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.add-comment","initial-add-comment","oc.web.components.ui.add-comment/initial-add-comment",1313376568).cljs$core$IFn$_invoke$arity$1(s)));
} else {
}

var updated_comment = (cljs.core.truth_(edit_comment_data)?oc.web.actions.comment.save_comment(activity_data,edit_comment_data,comment_body,save_done_cb):oc.web.actions.comment.add_comment(activity_data,comment_body,parent_comment_uuid,save_done_cb));
if(cljs.core.truth_(((cljs.core.not(oc.web.lib.responsive.is_mobile_size_QMARK_()))?((cljs.core.not(edit_comment_data))?((cljs.core.not(dismiss_reply_cb))?(function (){var and__4115__auto__ = scroll_after_posting_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return (!(oc.web.utils.dom.is_element_top_in_viewport_QMARK_.cljs$core$IFn$_invoke$arity$variadic(document.querySelector("div.stream-comments"),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(-40)], 0))));
} else {
return and__4115__auto__;
}
})():false):false):false))){
var temp__5735__auto___46068 = (rum.core.dom_node(s).offsetTop - (72));
if(cljs.core.truth_(temp__5735__auto___46068)){
var vertical_offset_46069 = temp__5735__auto___46068;
oc.web.lib.utils.after((10),(function (){
return window.scrollTo((0),vertical_offset_46069);
}));
} else {
}
} else {
}

if(cljs.core.fn_QMARK_(dismiss_reply_cb)){
(dismiss_reply_cb.cljs$core$IFn$_invoke$arity$1 ? dismiss_reply_cb.cljs$core$IFn$_invoke$arity$1(false) : dismiss_reply_cb.call(null,false));
} else {
}

if(cljs.core.fn_QMARK_(add_comment_cb)){
return (add_comment_cb.cljs$core$IFn$_invoke$arity$1 ? add_comment_cb.cljs$core$IFn$_invoke$arity$1(updated_comment) : add_comment_cb.call(null,updated_comment));
} else {
return null;
}
});
oc.web.components.ui.add_comment.add_comment_unique_class = (function oc$web$components$ui$add_comment$add_comment_unique_class(s){
return ["add-comment-box-container-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.add-comment","add-comment-id","oc.web.components.ui.add-comment/add-comment-id",60438102).cljs$core$IFn$_invoke$arity$1(s)))].join('');
});
oc.web.components.ui.add_comment.me_options = (function oc$web$components$ui$add_comment$me_options(s,parent_uuid,placeholder){
return new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"media-config","media-config",1588458658),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["gif"], null),new cljs.core.Keyword(null,"comment-parent-uuid","comment-parent-uuid",1514531851),parent_uuid,new cljs.core.Keyword(null,"placeholder","placeholder",-104873083),(function (){var or__4126__auto__ = placeholder;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
if(cljs.core.truth_(parent_uuid)){
return "Reply\u2026";
} else {
return "Add a comment\u2026";
}
}
})(),new cljs.core.Keyword(null,"use-inline-media-picker","use-inline-media-picker",1605876743),true,new cljs.core.Keyword(null,"static-positioned-media-picker","static-positioned-media-picker",-888920383),true,new cljs.core.Keyword(null,"media-picker-initially-visible","media-picker-initially-visible",1391698183),false,new cljs.core.Keyword(null,"media-picker-container-selector","media-picker-container-selector",1731260615),["div.",oc.web.components.ui.add_comment.add_comment_unique_class(s)," div.add-comment-box div.add-comment-internal div.add-comment-footer-media-picker"].join('')], null);
});
oc.web.components.ui.add_comment.did_change = (function oc$web$components$ui$add_comment$did_change(s){
var post_enabled = oc.web.components.ui.add_comment.enable_post_button_QMARK_(s);
var map__46049 = cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s));
var map__46049__$1 = (((((!((map__46049 == null))))?(((((map__46049.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46049.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46049):map__46049);
var activity_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46049__$1,new cljs.core.Keyword(null,"activity-data","activity-data",1293689136));
var parent_comment_uuid = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46049__$1,new cljs.core.Keyword(null,"parent-comment-uuid","parent-comment-uuid",-1757329834));
var edit_comment_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46049__$1,new cljs.core.Keyword(null,"edit-comment-data","edit-comment-data",-1510189888));
oc.web.components.ui.add_comment.multiple_lines_QMARK_(s);

oc.web.actions.comment.add_comment_change(activity_data,parent_comment_uuid,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(edit_comment_data),oc.web.components.ui.add_comment.add_comment_body(s));

cljs.core.compare_and_set_BANG_(new cljs.core.Keyword("oc.web.components.ui.add-comment","post-enabled","oc.web.components.ui.add-comment/post-enabled",-1405037618).cljs$core$IFn$_invoke$arity$1(s),(!(post_enabled)),post_enabled);

var temp__5735__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.add-comment","did-change-throttled","oc.web.components.ui.add-comment/did-change-throttled",1350759359).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(temp__5735__auto__)){
var throttled_did_change = temp__5735__auto__;
return throttled_did_change.fire();
} else {
return null;
}
});
oc.web.components.ui.add_comment.should_focus_QMARK_ = (function oc$web$components$ui$add_comment$should_focus_QMARK_(s){
var add_comment_focus = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"add-comment-focus","add-comment-focus",-452934461)));
var component_focus_id = oc.web.components.ui.add_comment.focus_value(s);
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(add_comment_focus,component_focus_id);
});
oc.web.components.ui.add_comment.focus_BANG_ = (function oc$web$components$ui$add_comment$focus_BANG_(s){
var temp__5735__auto__ = oc.web.components.ui.add_comment.add_comment_field(s);
if(cljs.core.truth_(temp__5735__auto__)){
var field = temp__5735__auto__;
field.focus();

return oc.web.lib.utils.after((0),(function (){
return oc.web.lib.utils.to_end_of_content_editable(field);
}));
} else {
return null;
}
});
oc.web.components.ui.add_comment.maybe_focus = (function oc$web$components$ui$add_comment$maybe_focus(s){
if(oc.web.components.ui.add_comment.should_focus_QMARK_(s)){
return oc.web.components.ui.add_comment.focus_BANG_(s);
} else {
return null;
}
});
oc.web.components.ui.add_comment.close_reply_clicked = (function oc$web$components$ui$add_comment$close_reply_clicked(s){
var map__46051 = cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s));
var map__46051__$1 = (((((!((map__46051 == null))))?(((((map__46051.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46051.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46051):map__46051);
var activity_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46051__$1,new cljs.core.Keyword(null,"activity-data","activity-data",1293689136));
var parent_comment_uuid = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46051__$1,new cljs.core.Keyword(null,"parent-comment-uuid","parent-comment-uuid",-1757329834));
var add_comment_focus_prefix = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46051__$1,new cljs.core.Keyword(null,"add-comment-focus-prefix","add-comment-focus-prefix",1635349699));
var dismiss_reply_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46051__$1,new cljs.core.Keyword(null,"dismiss-reply-cb","dismiss-reply-cb",1894601324));
var edit_comment_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46051__$1,new cljs.core.Keyword(null,"edit-comment-data","edit-comment-data",-1510189888));
var dismiss_fn = (function (){
oc.web.actions.comment.add_comment_reset(add_comment_focus_prefix,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data),parent_comment_uuid,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(edit_comment_data));

var temp__5735__auto___46074 = oc.web.components.ui.add_comment.add_comment_field(s);
if(cljs.core.truth_(temp__5735__auto___46074)){
var el_46075 = temp__5735__auto___46074;
(el_46075.innerHTML = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"edit-comment-data","edit-comment-data",-1510189888).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s))));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.utils.activity.empty_body_html;
}
})());
} else {
}

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.add-comment","collapsed","oc.web.components.ui.add-comment/collapsed",-1138653880).cljs$core$IFn$_invoke$arity$1(s),true);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.add-comment","multiple-lines","oc.web.components.ui.add-comment/multiple-lines",765343770).cljs$core$IFn$_invoke$arity$1(s),false);

if(cljs.core.fn_QMARK_(dismiss_reply_cb)){
return (dismiss_reply_cb.cljs$core$IFn$_invoke$arity$1 ? dismiss_reply_cb.cljs$core$IFn$_invoke$arity$1(true) : dismiss_reply_cb.call(null,true));
} else {
return null;
}
});
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.add-comment","post-enabled","oc.web.components.ui.add-comment/post-enabled",-1405037618).cljs$core$IFn$_invoke$arity$1(s)))){
var alert_data = new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"icon","icon",1679606541),"/img/ML/trash.svg",new cljs.core.Keyword(null,"action","action",-811238024),"cancel-comment-edit",new cljs.core.Keyword(null,"message","message",-406056002),"Are you sure you want to cancel? Your comment will be lost.",new cljs.core.Keyword(null,"link-button-title","link-button-title",-812305976),"Keep",new cljs.core.Keyword(null,"link-button-cb","link-button-cb",559710301),(function (){
return oc.web.components.ui.alert_modal.hide_alert();
}),new cljs.core.Keyword(null,"solid-button-style","solid-button-style",-723792244),new cljs.core.Keyword(null,"red","red",-969428204),new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"Yes",new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),(function (){
dismiss_fn();

return oc.web.components.ui.alert_modal.hide_alert();
})], null);
return oc.web.components.ui.alert_modal.show_alert(alert_data);
} else {
return dismiss_fn();
}
});
oc.web.components.ui.add_comment.add_comment = rum.core.build_defcs((function (s,p__46055){
var map__46056 = p__46055;
var map__46056__$1 = (((((!((map__46056 == null))))?(((((map__46056.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46056.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46056):map__46056);
var add_comment_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46056__$1,new cljs.core.Keyword(null,"add-comment-cb","add-comment-cb",1940125120));
var edit_comment_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46056__$1,new cljs.core.Keyword(null,"edit-comment-data","edit-comment-data",-1510189888));
var add_comment_focus_prefix = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46056__$1,new cljs.core.Keyword(null,"add-comment-focus-prefix","add-comment-focus-prefix",1635349699));
var collapse_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46056__$1,new cljs.core.Keyword(null,"collapse?","collapse?",720716709));
var scroll_after_posting_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46056__$1,new cljs.core.Keyword(null,"scroll-after-posting?","scroll-after-posting?",-721807257));
var row_index = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46056__$1,new cljs.core.Keyword(null,"row-index","row-index",-828710296));
var internal_max_width = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46056__$1,new cljs.core.Keyword(null,"internal-max-width","internal-max-width",-816752310));
var dismiss_reply_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46056__$1,new cljs.core.Keyword(null,"dismiss-reply-cb","dismiss-reply-cb",1894601324));
var activity_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46056__$1,new cljs.core.Keyword(null,"activity-data","activity-data",1293689136));
var parent_comment_uuid = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46056__$1,new cljs.core.Keyword(null,"parent-comment-uuid","parent-comment-uuid",-1757329834));
var add_comment_did_change = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46056__$1,new cljs.core.Keyword(null,"add-comment-did-change","add-comment-did-change",-325940488));
var _add_comment_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"add-comment-data","add-comment-data",-1679568046));
var _media_input = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"media-input","media-input",107658136));
var _mention_users = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"mention-users","mention-users",-525519758));
var _users_info_hover = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"users-info-hover","users-info-hover",-941434570));
var _current_user_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915));
var _follow_publishers_list = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"follow-publishers-list","follow-publishers-list",-374150342));
var _followers_publishers_count = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"followers-publishers-count","followers-publishers-count",-692976579));
var _reply_to = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"comment-reply-to","comment-reply-to",1200575357));
var current_user_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915));
var container_class = oc.web.components.ui.add_comment.add_comment_unique_class(s);
var is_mobile_QMARK_ = oc.web.lib.responsive.is_mobile_size_QMARK_();
var attachment_uploading = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"attachment-uploading","attachment-uploading",-646342864));
var uploading_QMARK_ = (function (){var and__4115__auto__ = attachment_uploading;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"comment-parent-uuid","comment-parent-uuid",1514531851).cljs$core$IFn$_invoke$arity$1(attachment_uploading),parent_comment_uuid);
} else {
return and__4115__auto__;
}
})();
var add_comment_class = ["add-comment-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.add-comment","add-comment-id","oc.web.components.ui.add-comment/add-comment-id",60438102).cljs$core$IFn$_invoke$arity$1(s)))].join('');
var multiple_lines = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.add-comment","multiple-lines","oc.web.components.ui.add-comment/multiple-lines",765343770).cljs$core$IFn$_invoke$arity$1(s));
var collapsed_QMARK_ = (function (){var and__4115__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.add-comment","collapsed","oc.web.components.ui.add-comment/collapsed",-1138653880).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(multiple_lines);
} else {
return and__4115__auto__;
}
})();
return React.createElement("div",({"onClick": (cljs.core.truth_((function (){var or__4126__auto__ = collapsed_QMARK_;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.not(multiple_lines);
}
})())?(function (){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(rum.core.ref_node(s,"editor-node"),document.activeElement)){
return null;
} else {
return oc.web.components.ui.add_comment.focus_BANG_(s);
}
}):null), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["add-comment-box-container",oc.web.lib.utils.class_set(cljs.core.PersistentArrayMap.createAsIfByAssoc([container_class,true,["add-comment-box-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(add_comment_focus_prefix)].join(''),true,new cljs.core.Keyword(null,"collapsed-box","collapsed-box",-370770434),collapsed_QMARK_,new cljs.core.Keyword(null,"inline-reply","inline-reply",1220920749),cljs.core.not(multiple_lines)]))], null))}),React.createElement("div",({"className": "add-comment-box"}),React.createElement("div",({"ref": "add-comment-internal", "onClick": (function (){
return null;

}), "className": "add-comment-internal"}),React.createElement("div",({"ref": "editor-node", "data-row-index": row_index, "onFocus": (function (){
return oc.web.components.ui.add_comment.focus(s);
}), "onBlur": (function (){
return oc.web.components.ui.add_comment.blur(s);
}), "onKeyDown": (function (e){
var add_comment_node = rum.core.ref_node(s,"editor-node");
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(e.key,"Escape")) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(document.activeElement,add_comment_node)))){
if(cljs.core.truth_(edit_comment_data)){
if(cljs.core.fn_QMARK_(dismiss_reply_cb)){
(dismiss_reply_cb.cljs$core$IFn$_invoke$arity$1 ? dismiss_reply_cb.cljs$core$IFn$_invoke$arity$1(true) : dismiss_reply_cb.call(null,true));
} else {
}
} else {
add_comment_node.blur();
}
} else {
}

if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(document.activeElement,add_comment_node)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(e.key,"Enter")))){
if(cljs.core.truth_(e.metaKey)){
return oc.web.components.ui.add_comment.send_clicked(e,s);
} else {
return cljs.core.compare_and_set_BANG_(new cljs.core.Keyword("oc.web.components.ui.add-comment","multiple-lines","oc.web.components.ui.add-comment/multiple-lines",765343770).cljs$core$IFn$_invoke$arity$1(s),false,true);
}
} else {
return null;
}
}), "contentEditable": true, "dangerouslySetInnerHTML": ({"__html": cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.add-comment","initial-add-comment","oc.web.components.ui.add-comment/initial-add-comment",1313376568).cljs$core$IFn$_invoke$arity$1(s))}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, ["add-comment","emoji-autocomplete","emojiable","oc-mentions","oc-mentions-hover","editing",oc.web.lib.utils.class_set(cljs.core.PersistentArrayMap.createAsIfByAssoc([add_comment_class,true,new cljs.core.Keyword(null,"medium-editor-placeholder-hidden","medium-editor-placeholder-hidden",30561611),(function (){var and__4115__auto__ = collapse_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(collapsed_QMARK_);
} else {
return and__4115__auto__;
}
})(),new cljs.core.Keyword(null,"medium-editor-placeholder-relative","medium-editor-placeholder-relative",145573649),true,new cljs.core.Keyword(null,"medium-editor-element","medium-editor-element",1287568604),true,oc.web.lib.utils.hide_class,true,oc.web.utils.dom.onload_recalc_measure_class,true]))], null))})),React.createElement("div",({"className": "add-comment-footer group"}),React.createElement("button",({"onClick": (function (){
return oc.web.components.ui.add_comment.close_reply_clicked(s);
}), "data-toggle": ((oc.web.lib.responsive.is_tablet_or_mobile_QMARK_())?"":"tooltip"), "data-placement": "top", "data-container": "body", "title": (cljs.core.truth_(edit_comment_data)?"Cancel edit":"Cancel"), "className": "mlb-reset close-reply-bt"})),sablono.interpreter.interpret((cljs.core.truth_(uploading_QMARK_)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.upload-progress","div.upload-progress",-1578967141),(oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.small_loading.small_loading.call(null)),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.attachment-uploading","span.attachment-uploading",1954262115),["Uploading ",cljs.core.str.cljs$core$IFn$_invoke$arity$1((function (){var or__4126__auto__ = new cljs.core.Keyword(null,"progress","progress",244323547).cljs$core$IFn$_invoke$arity$1(attachment_uploading);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return (0);
}
})()),"%..."].join('')], null)], null):null)),sablono.interpreter.interpret((function (){var G__46060 = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"add-emoji-cb","add-emoji-cb",-375710574),(function (){
return oc.web.components.ui.add_comment.did_change(s);
}),new cljs.core.Keyword(null,"width","width",-384071477),(24),new cljs.core.Keyword(null,"height","height",1025178622),(24),new cljs.core.Keyword(null,"position","position",-2011731912),"bottom",new cljs.core.Keyword(null,"default-field-selector","default-field-selector",179085335),["div.",add_comment_class].join(''),new cljs.core.Keyword(null,"container-selector","container-selector",6506114),["div.",add_comment_class].join('')], null);
return (oc.web.components.ui.emoji_picker.emoji_picker.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.emoji_picker.emoji_picker.cljs$core$IFn$_invoke$arity$1(G__46060) : oc.web.components.ui.emoji_picker.emoji_picker.call(null,G__46060));
})()),sablono.interpreter.interpret((cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","showing-gif-selector","oc.web.utils.medium-editor-media/showing-gif-selector",1051253553).cljs$core$IFn$_invoke$arity$1(s)))?(function (){var G__46061 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"fullscreen","fullscreen",-4371054),false,new cljs.core.Keyword(null,"pick-emoji-cb","pick-emoji-cb",-830701499),(function (gif_obj){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","showing-gif-selector","oc.web.utils.medium-editor-media/showing-gif-selector",1051253553).cljs$core$IFn$_invoke$arity$1(s),false);

return oc.web.utils.medium_editor_media.media_gif_add(s,cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-picker-ext","oc.web.utils.medium-editor-media/media-picker-ext",805783011).cljs$core$IFn$_invoke$arity$1(s)),gif_obj);
})], null);
return (oc.web.components.ui.giphy_picker.giphy_picker.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.giphy_picker.giphy_picker.cljs$core$IFn$_invoke$arity$1(G__46061) : oc.web.components.ui.giphy_picker.giphy_picker.call(null,G__46061));
})():null)),sablono.interpreter.interpret((cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","showing-media-video-modal","oc.web.utils.medium-editor-media/showing-media-video-modal",1606579887).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.video-container","div.video-container",-1024597571),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ref","ref",1289896967),new cljs.core.Keyword(null,"video-container","video-container",-271437239)], null),(function (){var G__46062 = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"fullscreen","fullscreen",-4371054),false,new cljs.core.Keyword(null,"dismiss-cb","dismiss-cb",-1282537857),(function (){
oc.web.utils.medium_editor_media.media_video_add(s,cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-picker-ext","oc.web.utils.medium-editor-media/media-picker-ext",805783011).cljs$core$IFn$_invoke$arity$1(s)),null);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","showing-media-video-modal","oc.web.utils.medium-editor-media/showing-media-video-modal",1606579887).cljs$core$IFn$_invoke$arity$1(s),false);
}),new cljs.core.Keyword(null,"offset-element-selector","offset-element-selector",-64578356),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(["div.",container_class," div.add-comment-box"].join(''))], null),new cljs.core.Keyword(null,"outer-container-selector","outer-container-selector",1406024934),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(["div.",container_class].join(''))], null)], null);
return (oc.web.components.ui.media_video_modal.media_video_modal.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.media_video_modal.media_video_modal.cljs$core$IFn$_invoke$arity$1(G__46062) : oc.web.components.ui.media_video_modal.media_video_modal.call(null,G__46062));
})()], null):null)),React.createElement("div",({"className": "add-comment-footer-media-picker group"})),React.createElement("button",({"onClick": (function (p1__46054_SHARP_){
return oc.web.components.ui.add_comment.send_clicked(p1__46054_SHARP_,s);
}), "disabled": cljs.core.not(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.add-comment","post-enabled","oc.web.components.ui.add-comment/post-enabled",-1405037618).cljs$core$IFn$_invoke$arity$1(s))), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["mlb-reset","send-btn",(cljs.core.truth_(uploading_QMARK_)?"separator-line":null)], null))}),(cljs.core.truth_(edit_comment_data)?"Save":"Reply"))))));
}),cljs.core.PersistentVector.fromArray([rum.core.static$,rum.core.reactive,rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.utils.medium-editor-media","editor","oc.web.utils.medium-editor-media/editor",1463568801)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-picker-ext","oc.web.utils.medium-editor-media/media-picker-ext",805783011)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-photo","oc.web.utils.medium-editor-media/media-photo",1416359036)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-video","oc.web.utils.medium-editor-media/media-video",1963418271)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-attachment","oc.web.utils.medium-editor-media/media-attachment",-971860820)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-photo-did-success","oc.web.utils.medium-editor-media/media-photo-did-success",13253331)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-attachment-did-success","oc.web.utils.medium-editor-media/media-attachment-did-success",-111823171)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.utils.medium-editor-media","showing-media-video-modal","oc.web.utils.medium-editor-media/showing-media-video-modal",1606579887)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.utils.medium-editor-media","showing-gif-selector","oc.web.utils.medium-editor-media/showing-gif-selector",1051253553)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.utils.medium-editor-media","upload-lock","oc.web.utils.medium-editor-media/upload-lock",-1921399674)),rum.core.local.cljs$core$IFn$_invoke$arity$2("",new cljs.core.Keyword("oc.web.components.ui.add-comment","add-comment-id","oc.web.components.ui.add-comment/add-comment-id",60438102)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.add-comment","comment-reply-to-reset","oc.web.components.ui.add-comment/comment-reply-to-reset",-357326311)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"media-input","media-input",107658136)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"add-comment-focus","add-comment-focus",-452934461)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"add-comment-data","add-comment-data",-1679568046)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"mention-users","mention-users",-525519758)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"attachment-uploading","attachment-uploading",-646342864)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"users-info-hover","users-info-hover",-941434570)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"follow-publishers-list","follow-publishers-list",-374150342)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"followers-publishers-count","followers-publishers-count",-692976579)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"comment-reply-to","comment-reply-to",1200575357)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.ui.add-comment","add-comment-key","oc.web.components.ui.add-comment/add-comment-key",-400996276)),rum.core.local.cljs$core$IFn$_invoke$arity$2(true,new cljs.core.Keyword("oc.web.components.ui.add-comment","collapsed","oc.web.components.ui.add-comment/collapsed",-1138653880)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.add-comment","multiple-lines","oc.web.components.ui.add-comment/multiple-lines",765343770)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.add-comment","post-enabled","oc.web.components.ui.add-comment/post-enabled",-1405037618)),rum.core.local.cljs$core$IFn$_invoke$arity$2(oc.web.utils.activity.empty_body_html,new cljs.core.Keyword("oc.web.components.ui.add-comment","initial-add-comment","oc.web.components.ui.add-comment/initial-add-comment",1313376568)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.add-comment","last-add-comment-focus","oc.web.components.ui.add-comment/last-add-comment-focus",880516397)),rum.core.local.cljs$core$IFn$_invoke$arity$2((10000),new cljs.core.Keyword("oc.web.components.ui.add-comment","inline-reply-max-width","oc.web.components.ui.add-comment/inline-reply-max-width",-533206291)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.ui.add-comment","did-change-throttled","oc.web.components.ui.add-comment/did-change-throttled",1350759359)),oc.web.mixins.ui.first_render_mixin,oc.web.mixins.mention.oc_mentions_hover(),oc.web.mixins.ui.on_window_click_mixin((function (s,e){
if(cljs.core.truth_((function (){var and__4115__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","showing-media-video-modal","oc.web.utils.medium-editor-media/showing-media-video-modal",1606579887).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(and__4115__auto__)){
return ((cljs.core.not(e.target.classList.contains("media-video"))) && (cljs.core.not(oc.web.lib.utils.event_inside_QMARK_(e,rum.core.ref_node(s,new cljs.core.Keyword(null,"video-container","video-container",-271437239))))));
} else {
return and__4115__auto__;
}
})())){
oc.web.utils.medium_editor_media.media_video_add(s,cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-picker-ext","oc.web.utils.medium-editor-media/media-picker-ext",805783011).cljs$core$IFn$_invoke$arity$1(s)),null);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","showing-media-video-modal","oc.web.utils.medium-editor-media/showing-media-video-modal",1606579887).cljs$core$IFn$_invoke$arity$1(s),false);
} else {
}

if(cljs.core.truth_((function (){var and__4115__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","showing-gif-selector","oc.web.utils.medium-editor-media/showing-gif-selector",1051253553).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(and__4115__auto__)){
return ((cljs.core.not(e.target.classList.contains("media-gif"))) && (cljs.core.not(oc.web.lib.utils.event_inside_QMARK_(e,document.querySelector("div.giphy-picker")))));
} else {
return and__4115__auto__;
}
})())){
oc.web.utils.medium_editor_media.media_gif_add(s,cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-picker-ext","oc.web.utils.medium-editor-media/media-picker-ext",805783011).cljs$core$IFn$_invoke$arity$1(s)),null);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","showing-gif-selector","oc.web.utils.medium-editor-media/showing-gif-selector",1051253553).cljs$core$IFn$_invoke$arity$1(s),false);
} else {
return null;
}
})),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.add-comment","add-comment-id","oc.web.components.ui.add-comment/add-comment-id",60438102).cljs$core$IFn$_invoke$arity$1(s),oc.web.lib.utils.activity_uuid());

var map__46063_46087 = cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s));
var map__46063_46088__$1 = (((((!((map__46063_46087 == null))))?(((((map__46063_46087.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46063_46087.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46063_46087):map__46063_46087);
var activity_data_46089 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46063_46088__$1,new cljs.core.Keyword(null,"activity-data","activity-data",1293689136));
var parent_comment_uuid_46090 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46063_46088__$1,new cljs.core.Keyword(null,"parent-comment-uuid","parent-comment-uuid",-1757329834));
var edit_comment_data_46091 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46063_46088__$1,new cljs.core.Keyword(null,"edit-comment-data","edit-comment-data",-1510189888));
var collapse_QMARK__46092 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46063_46088__$1,new cljs.core.Keyword(null,"collapse?","collapse?",720716709));
var add_comment_did_change_46093 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46063_46088__$1,new cljs.core.Keyword(null,"add-comment-did-change","add-comment-did-change",-325940488));
var add_comment_key_46094 = oc.web.dispatcher.add_comment_string_key.cljs$core$IFn$_invoke$arity$3(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data_46089),parent_comment_uuid_46090,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(edit_comment_data_46091));
var activity_add_comment_data_46095 = oc.web.components.ui.add_comment.add_comment_data.cljs$core$IFn$_invoke$arity$2(s,add_comment_key_46094);
var initial_body_46096 = (function (){var or__4126__auto__ = activity_add_comment_data_46095;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(edit_comment_data_46091);
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
return oc.web.utils.activity.empty_body_html;
}
}
})();
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.add-comment","add-comment-key","oc.web.components.ui.add-comment/add-comment-key",-400996276).cljs$core$IFn$_invoke$arity$1(s),add_comment_key_46094);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.add-comment","initial-add-comment","oc.web.components.ui.add-comment/initial-add-comment",1313376568).cljs$core$IFn$_invoke$arity$1(s),initial_body_46096);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.add-comment","collapsed","oc.web.components.ui.add-comment/collapsed",-1138653880).cljs$core$IFn$_invoke$arity$1(s),(function (){var and__4115__auto__ = collapse_QMARK__46092;
if(cljs.core.truth_(and__4115__auto__)){
return oc.web.utils.activity.empty_body_QMARK_(initial_body_46096);
} else {
return and__4115__auto__;
}
})());

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.add-comment","post-enabled","oc.web.components.ui.add-comment/post-enabled",-1405037618).cljs$core$IFn$_invoke$arity$1(s),cljs.core.boolean$(((cljs.core.seq(activity_add_comment_data_46095)) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(activity_add_comment_data_46095,new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(edit_comment_data_46091))) && ((!(oc.web.utils.activity.empty_body_QMARK_(activity_add_comment_data_46095)))))));

if(cljs.core.fn_QMARK_(add_comment_did_change_46093)){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.add-comment","did-change-throttled","oc.web.components.ui.add-comment/did-change-throttled",1350759359).cljs$core$IFn$_invoke$arity$1(s),(new goog.async.Throttle(add_comment_did_change_46093,(2000))));
} else {
}

return s;
}),new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
var props_46104 = cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s));
var me_opts_46105 = oc.web.components.ui.add_comment.me_options(s,new cljs.core.Keyword(null,"parent-comment-uuid","parent-comment-uuid",-1757329834).cljs$core$IFn$_invoke$arity$1(props_46104),new cljs.core.Keyword(null,"add-comment-placeholder","add-comment-placeholder",1617238414).cljs$core$IFn$_invoke$arity$1(props_46104));
var add_comment_internal_46106 = rum.core.ref_node(s,new cljs.core.Keyword(null,"add-comment-internal","add-comment-internal",-1715481853));
var bounding_box_46107 = add_comment_internal_46106.getBoundingClientRect();
var computed_style_46108 = window.getComputedStyle(add_comment_internal_46106);
var max_width_46109 = (new cljs.core.Keyword(null,"internal-max-width","internal-max-width",-816752310).cljs$core$IFn$_invoke$arity$1(props_46104) - (140));
oc.web.utils.medium_editor_media.setup_editor(s,oc.web.components.ui.add_comment.did_change,me_opts_46105);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.add-comment","inline-reply-max-width","oc.web.components.ui.add-comment/inline-reply-max-width",-533206291).cljs$core$IFn$_invoke$arity$1(s),max_width_46109);

oc.web.components.ui.add_comment.maybe_focus(s);

oc.web.components.ui.add_comment.multiple_lines_QMARK_(s);

oc.web.lib.utils.after((2500),(function (){
return emojiAutocomplete();
}));

return s;
}),new cljs.core.Keyword(null,"will-update","will-update",328062998),(function (s){
var props_46112 = cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s));
var reply_to_46113 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"comment-reply-to","comment-reply-to",1200575357)));
var focus_val_46114 = oc.web.components.ui.add_comment.focus_value(s);
var map__46065_46115 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(reply_to_46113,focus_val_46114);
var map__46065_46116__$1 = (((((!((map__46065_46115 == null))))?(((((map__46065_46115.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46065_46115.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46065_46115):map__46065_46115);
var r_46117 = map__46065_46116__$1;
var focus_46118 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46065_46116__$1,new cljs.core.Keyword(null,"focus","focus",234677911));
var body_46119 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46065_46116__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
if(typeof body_46119 === 'string'){
var body_field_46124 = oc.web.components.ui.add_comment.add_comment_field(s);
var current_body_46125 = body_field_46124.innerHTML;
var is_empty_QMARK__46126 = oc.web.utils.activity.empty_body_QMARK_(current_body_46125);
var quoted_body_46127 = ["<blockquote>",body_46119,"</blockquote>",oc.web.utils.activity.empty_body_html].join('');
var last_element_46128 = ((is_empty_QMARK__46126)?null:body_field_46124.lastElementChild);
var last_element_tag_46129 = (cljs.core.truth_(last_element_46128)?last_element_46128.nodeName.toLowerCase():null);
var next_body_46130 = ((is_empty_QMARK__46126)?quoted_body_46127:(cljs.core.truth_((function (){var and__4115__auto__ = last_element_46128;
if(cljs.core.truth_(and__4115__auto__)){
return ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(last_element_tag_46129,"blockquote")) || (((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(last_element_tag_46129,"blockquote")) && (cljs.core.not(last_element_46128.isContentEditable)))));
} else {
return and__4115__auto__;
}
})())?[cljs.core.str.cljs$core$IFn$_invoke$arity$1(current_body_46125),oc.web.utils.activity.empty_body_html,quoted_body_46127].join(''):[cljs.core.str.cljs$core$IFn$_invoke$arity$1(current_body_46125),quoted_body_46127].join('')
));
if(cljs.core.truth_(focus_46118)){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.add-comment","last-add-comment-focus","oc.web.components.ui.add-comment/last-add-comment-focus",880516397).cljs$core$IFn$_invoke$arity$1(s),null);
} else {
}

(body_field_46124.innerHTML = next_body_46130);

oc.web.components.ui.add_comment.multiple_lines_QMARK_(s);

oc.web.actions.comment.add_comment_change(new cljs.core.Keyword(null,"activity-data","activity-data",1293689136).cljs$core$IFn$_invoke$arity$1(props_46112),new cljs.core.Keyword(null,"parent-comment-uuid","parent-comment-uuid",-1757329834).cljs$core$IFn$_invoke$arity$1(props_46112),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"edit-comment-data","edit-comment-data",-1510189888).cljs$core$IFn$_invoke$arity$1(props_46112)),oc.web.components.ui.add_comment.add_comment_body(s));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.add-comment","collapsed","oc.web.components.ui.add-comment/collapsed",-1138653880).cljs$core$IFn$_invoke$arity$1(s),false);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.add-comment","post-enabled","oc.web.components.ui.add-comment/post-enabled",-1405037618).cljs$core$IFn$_invoke$arity$1(s),true);

oc.web.actions.comment.reset_reply_to(focus_val_46114);
} else {
}

var props_46131 = cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s));
var data_46132 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"media-input","media-input",107658136)));
var video_data_46133 = new cljs.core.Keyword(null,"media-video","media-video",339369994).cljs$core$IFn$_invoke$arity$1(data_46132);
if(cljs.core.truth_((function (){var and__4115__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-video","oc.web.utils.medium-editor-media/media-video",1963418271).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(and__4115__auto__)){
return ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(video_data_46133,new cljs.core.Keyword(null,"dismiss","dismiss",412569545))) || (cljs.core.map_QMARK_(video_data_46133)));
} else {
return and__4115__auto__;
}
})())){
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(video_data_46133,new cljs.core.Keyword(null,"dismiss","dismiss",412569545))) || (cljs.core.map_QMARK_(video_data_46133)))){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-video","oc.web.utils.medium-editor-media/media-video",1963418271).cljs$core$IFn$_invoke$arity$1(s),false);

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"media-input","media-input",107658136)], null),(function (p1__46053_SHARP_){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(p1__46053_SHARP_,new cljs.core.Keyword(null,"media-video","media-video",339369994));
})], null));
} else {
}

if(cljs.core.map_QMARK_(video_data_46133)){
oc.web.utils.medium_editor_media.media_video_add(s,cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-picker-ext","oc.web.utils.medium-editor-media/media-picker-ext",805783011).cljs$core$IFn$_invoke$arity$1(s)),video_data_46133);
} else {
oc.web.utils.medium_editor_media.media_video_add(s,cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-picker-ext","oc.web.utils.medium-editor-media/media-picker-ext",805783011).cljs$core$IFn$_invoke$arity$1(s)),null);
}
} else {
}

return s;
}),new cljs.core.Keyword(null,"did-update","did-update",-2143702256),(function (s){
var add_comment_focus_46134 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"add-comment-focus","add-comment-focus",-452934461)));
if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.add-comment","last-add-comment-focus","oc.web.components.ui.add-comment/last-add-comment-focus",880516397).cljs$core$IFn$_invoke$arity$1(s)),add_comment_focus_46134)){
oc.web.components.ui.add_comment.maybe_focus(s);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.add-comment","last-add-comment-focus","oc.web.components.ui.add-comment/last-add-comment-focus",880516397).cljs$core$IFn$_invoke$arity$1(s),add_comment_focus_46134);
} else {
}

return s;
}),new cljs.core.Keyword(null,"did-remount","did-remount",1362550500),(function (o,s){
var props_46136 = cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s));
var me_opts_46137 = oc.web.components.ui.add_comment.me_options(s,new cljs.core.Keyword(null,"parent-comment-uuid","parent-comment-uuid",-1757329834).cljs$core$IFn$_invoke$arity$1(props_46136),new cljs.core.Keyword(null,"add-comment-placeholder","add-comment-placeholder",1617238414).cljs$core$IFn$_invoke$arity$1(props_46136));
oc.web.utils.medium_editor_media.setup_editor(s,oc.web.components.ui.add_comment.did_change,me_opts_46137);

return s;
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","editor","oc.web.utils.medium-editor-media/editor",1463568801).cljs$core$IFn$_invoke$arity$1(s)))){
cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","editor","oc.web.utils.medium-editor-media/editor",1463568801).cljs$core$IFn$_invoke$arity$1(s)).destroy();

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","editor","oc.web.utils.medium-editor-media/editor",1463568801).cljs$core$IFn$_invoke$arity$1(s),null);
} else {
}

var temp__5735__auto___46138 = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.add-comment","did-change-throttled","oc.web.components.ui.add-comment/did-change-throttled",1350759359).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(temp__5735__auto___46138)){
var throttled_did_change_46139 = temp__5735__auto___46138;
throttled_did_change_46139.dispose();
} else {
}

return s;
})], null)], true),"add-comment");

//# sourceMappingURL=oc.web.components.ui.add_comment.js.map
