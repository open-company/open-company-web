goog.provide('oc.web.components.cmail');
oc.web.components.cmail.self_board_name = "All";
oc.web.components.cmail.board_tooltip = "Select a topic";
/**
 * Called every time the image picke close, reset to inital state.
 */
oc.web.components.cmail.media_attachment_dismiss_picker = (function oc$web$components$cmail$media_attachment_dismiss_picker(s){
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","media-attachment-did-success","oc.web.components.cmail/media-attachment-did-success",1340629320).cljs$core$IFn$_invoke$arity$1(s)))){
return null;
} else {
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","media-attachment","oc.web.components.cmail/media-attachment",1000917759).cljs$core$IFn$_invoke$arity$1(s),false);
}
});
oc.web.components.cmail.attachment_upload_failed_cb = (function oc$web$components$cmail$attachment_upload_failed_cb(state){
var alert_data = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"icon","icon",1679606541),"/img/ML/error_icon.png",new cljs.core.Keyword(null,"action","action",-811238024),"attachment-upload-error",new cljs.core.Keyword(null,"title","title",636505583),"Sorry!",new cljs.core.Keyword(null,"message","message",-406056002),"An error occurred with your file.",new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"OK",new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),(function (){
return oc.web.components.ui.alert_modal.hide_alert();
})], null);
oc.web.components.ui.alert_modal.show_alert(alert_data);

return oc.web.lib.utils.after((10),(function (){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","media-attachment-did-success","oc.web.components.cmail/media-attachment-did-success",1340629320).cljs$core$IFn$_invoke$arity$1(state),false);

return oc.web.components.cmail.media_attachment_dismiss_picker(state);
}));
});
oc.web.components.cmail.attachment_upload_success_cb = (function oc$web$components$cmail$attachment_upload_success_cb(state,res){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","media-attachment-did-success","oc.web.components.cmail/media-attachment-did-success",1340629320).cljs$core$IFn$_invoke$arity$1(state),true);

var url = goog.object.get(res,"url");
if(cljs.core.not(url)){
return oc.web.components.cmail.attachment_upload_failed_cb(state);
} else {
var size = goog.object.get(res,"size");
var mimetype = goog.object.get(res,"mimetype");
var filename = goog.object.get(res,"filename");
var createdat = oc.web.lib.utils.js_date();
var prefix = ["Uploaded by ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"name","name",1843675177)))," on ",oc.web.lib.utils.date_string.cljs$core$IFn$_invoke$arity$variadic(createdat,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"year","year",335913393)], null)], 0))," - "].join('');
var subtitle = [prefix,clojure.contrib.humanize.filesize.cljs$core$IFn$_invoke$arity$variadic(size,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"binary","binary",-1802232288),false,new cljs.core.Keyword(null,"format","format",-1306924766),"%.2f"], 0))].join('');
var icon = oc.web.utils.activity.icon_for_mimetype(mimetype);
var attachment_data = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"file-name","file-name",-1654217259),filename,new cljs.core.Keyword(null,"file-type","file-type",1274948820),mimetype,new cljs.core.Keyword(null,"file-size","file-size",-1900760755),size,new cljs.core.Keyword(null,"file-url","file-url",-863738963),url], null);
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","media-attachment","oc.web.components.cmail/media-attachment",1000917759).cljs$core$IFn$_invoke$arity$1(state),false);

oc.web.actions.activity.add_attachment(cljs.core.first(oc.web.dispatcher.cmail_data_key),attachment_data);

return oc.web.lib.utils.after((1000),(function (){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","media-attachment-did-success","oc.web.components.cmail/media-attachment-did-success",1340629320).cljs$core$IFn$_invoke$arity$1(state),false);
}));
}
});
oc.web.components.cmail.attachment_upload_error_cb = (function oc$web$components$cmail$attachment_upload_error_cb(state,res,error){
return oc.web.components.cmail.attachment_upload_failed_cb(state);
});
oc.web.components.cmail.add_attachment = (function oc$web$components$cmail$add_attachment(s){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","media-attachment","oc.web.components.cmail/media-attachment",1000917759).cljs$core$IFn$_invoke$arity$1(s),true);

return oc.web.lib.image_upload.upload_BANG_.cljs$core$IFn$_invoke$arity$variadic(null,cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.components.cmail.attachment_upload_success_cb,s),null,cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.components.cmail.attachment_upload_error_cb,s),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(function (){
return oc.web.lib.utils.after((400),(function (){
return oc.web.components.cmail.media_attachment_dismiss_picker(s);
}));
})], 0));
});
oc.web.components.cmail.body_element = (function oc$web$components$cmail$body_element(){
return document.querySelector("div.rich-body-editor");
});
oc.web.components.cmail.cleaned_body = (function oc$web$components$cmail$cleaned_body(){
var temp__5735__auto__ = oc.web.components.cmail.body_element();
if(cljs.core.truth_(temp__5735__auto__)){
var body_el = temp__5735__auto__;
return oc.web.lib.utils.clean_body_html(body_el.innerHTML);
} else {
return null;
}
});
oc.web.components.cmail.real_close = (function oc$web$components$cmail$real_close(){
return oc.web.actions.cmail.cmail_hide();
});
oc.web.components.cmail.headline_element = (function oc$web$components$cmail$headline_element(s){
return rum.core.ref_node(s,"headline");
});
oc.web.components.cmail.fix_headline = (function oc$web$components$cmail$fix_headline(headline){
return oc.web.lib.utils.trim(cuerdas.core.replace((function (){var or__4126__auto__ = headline;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "";
}
})(),/\n/,""));
});
oc.web.components.cmail.clean_body = (function oc$web$components$cmail$clean_body(s){
if(cljs.core.truth_(oc.web.components.cmail.body_element())){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.cmail_data_key,new cljs.core.Keyword(null,"body","body",-2049205669)),oc.web.components.cmail.cleaned_body()], null));
} else {
return null;
}
});
oc.web.components.cmail.autosave = (function oc$web$components$cmail$autosave(var_args){
var G__45851 = arguments.length;
switch (G__45851) {
case 1:
return oc.web.components.cmail.autosave.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.components.cmail.autosave.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.components.cmail.autosave.cljs$core$IFn$_invoke$arity$1 = (function (s){
return oc.web.components.cmail.autosave.cljs$core$IFn$_invoke$arity$2(s,false);
}));

(oc.web.components.cmail.autosave.cljs$core$IFn$_invoke$arity$2 = (function (s,reset_cmail_QMARK_){
var cmail_data = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"cmail-data","cmail-data",-550846261)));
var section_editing = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167)));
return oc.web.actions.activity.entry_save_on_exit.cljs$core$IFn$_invoke$arity$5(cljs.core.first(oc.web.dispatcher.cmail_data_key),cmail_data,oc.web.components.cmail.cleaned_body(),section_editing,(cljs.core.truth_(reset_cmail_QMARK_)?(function (p1__45849_SHARP_){
if(cljs.core.truth_(p1__45849_SHARP_)){
return oc.web.actions.cmail.cmail_reset();
} else {
return null;
}
}):null));
}));

(oc.web.components.cmail.autosave.cljs$lang$maxFixedArity = 2);

oc.web.components.cmail.debounced_autosave_BANG_ = (function oc$web$components$cmail$debounced_autosave_BANG_(s){
return cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","debounced-autosave","oc.web.components.cmail/debounced-autosave",1937334018).cljs$core$IFn$_invoke$arity$1(s)).fire();
});
oc.web.components.cmail.cancel_autosave_BANG_ = (function oc$web$components$cmail$cancel_autosave_BANG_(s){
return cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","debounced-autosave","oc.web.components.cmail/debounced-autosave",1937334018).cljs$core$IFn$_invoke$arity$1(s)).stop();
});
oc.web.components.cmail.cancel_clicked = (function oc$web$components$cmail$cancel_clicked(s){
var cmail_data = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"cmail-data","cmail-data",-550846261)));
var clean_fn = (function (dismiss_modal_QMARK_){
oc.web.actions.activity.entry_clear_local_cache(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(cmail_data),cljs.core.first(oc.web.dispatcher.cmail_data_key),cmail_data);

if(cljs.core.truth_(dismiss_modal_QMARK_)){
oc.web.components.ui.alert_modal.hide_alert();
} else {
}

return oc.web.components.cmail.real_close();
});
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","uploading-media","oc.web.components.cmail/uploading-media",-930090348).cljs$core$IFn$_invoke$arity$1(s)))){
var alert_data = new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"icon","icon",1679606541),"/img/ML/trash.svg",new cljs.core.Keyword(null,"action","action",-811238024),"dismiss-edit-uploading-media",new cljs.core.Keyword(null,"message","message",-406056002),"Leave before finishing upload?",new cljs.core.Keyword(null,"link-button-title","link-button-title",-812305976),"Stay",new cljs.core.Keyword(null,"link-button-cb","link-button-cb",559710301),(function (){
return oc.web.components.ui.alert_modal.hide_alert();
}),new cljs.core.Keyword(null,"solid-button-style","solid-button-style",-723792244),new cljs.core.Keyword(null,"red","red",-969428204),new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"Cancel upload",new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),(function (){
return clean_fn(true);
})], null);
return oc.web.components.ui.alert_modal.show_alert(alert_data);
} else {
if(cljs.core.truth_(new cljs.core.Keyword(null,"has-changes","has-changes",-631476764).cljs$core$IFn$_invoke$arity$1(cmail_data))){
var alert_data = new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"icon","icon",1679606541),"/img/ML/trash.svg",new cljs.core.Keyword(null,"action","action",-811238024),"dismiss-edit-dirty-data",new cljs.core.Keyword(null,"message","message",-406056002),"Leave without saving your changes?",new cljs.core.Keyword(null,"link-button-title","link-button-title",-812305976),"Stay",new cljs.core.Keyword(null,"link-button-cb","link-button-cb",559710301),(function (){
return oc.web.components.ui.alert_modal.hide_alert();
}),new cljs.core.Keyword(null,"solid-button-style","solid-button-style",-723792244),new cljs.core.Keyword(null,"red","red",-969428204),new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"Lose changes",new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),(function (){
return clean_fn(true);
})], null);
return oc.web.components.ui.alert_modal.show_alert(alert_data);
} else {
return clean_fn(false);
}
}
});
oc.web.components.cmail.body_on_change = (function oc$web$components$cmail$body_on_change(state){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.cmail_data_key,new cljs.core.Keyword(null,"has-changes","has-changes",-631476764)),true], null));

oc.web.components.cmail.debounced_autosave_BANG_(state);

var temp__5735__auto__ = oc.web.components.cmail.body_element();
if(cljs.core.truth_(temp__5735__auto__)){
var body_el = temp__5735__auto__;
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","last-body","oc.web.components.cmail/last-body",166956190).cljs$core$IFn$_invoke$arity$1(state),body_el.innerHTML);
} else {
return null;
}
});
oc.web.components.cmail.setup_top_padding = (function oc$web$components$cmail$setup_top_padding(s){
var temp__5735__auto__ = oc.web.components.cmail.headline_element(s);
if(cljs.core.truth_(temp__5735__auto__)){
var headline = temp__5735__auto__;
if(cljs.core.truth_(new cljs.core.Keyword(null,"fullscreen","fullscreen",-4371054).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"cmail-state","cmail-state",-747393321)))))){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","top-padding","oc.web.components.cmail/top-padding",481343021).cljs$core$IFn$_invoke$arity$1(s),headline.parentElement.scrollHeight);
} else {
return null;
}
} else {
return null;
}
});
oc.web.components.cmail.headline_on_change = (function oc$web$components$cmail$headline_on_change(state){
var temp__5735__auto__ = oc.web.components.cmail.headline_element(state);
if(cljs.core.truth_(temp__5735__auto__)){
var headline = temp__5735__auto__;
var clean_headline_45940 = oc.web.components.cmail.fix_headline(headline.innerText);
var post_button_title_45941 = ((cljs.core.seq(clean_headline_45940))?null:new cljs.core.Keyword(null,"title","title",636505583));
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),oc.web.dispatcher.cmail_data_key,(function (p1__45857_SHARP_){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__45857_SHARP_,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"headline","headline",-157157727),clean_headline_45940,new cljs.core.Keyword(null,"has-changes","has-changes",-631476764),true], null)], 0));
})], null));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","post-tt-kw","oc.web.components.cmail/post-tt-kw",1997423179).cljs$core$IFn$_invoke$arity$1(state),post_button_title_45941);

oc.web.components.cmail.debounced_autosave_BANG_(state);

return oc.web.components.cmail.setup_top_padding(state);
} else {
return null;
}
});
oc.web.components.cmail.fullscreen_focus_headline = (function oc$web$components$cmail$fullscreen_focus_headline(state){
var headline_el = oc.web.components.cmail.headline_element(state);
if(cljs.core.truth_((function (){var and__4115__auto__ = new cljs.core.Keyword(null,"fullscreen","fullscreen",-4371054).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(org.martinklepsch.derivatives.get_ref(state,new cljs.core.Keyword(null,"cmail-state","cmail-state",-747393321))));
if(cljs.core.truth_(and__4115__auto__)){
return headline_el;
} else {
return and__4115__auto__;
}
})())){
return oc.web.lib.utils.to_end_of_content_editable(headline_el);
} else {
return null;
}
});
oc.web.components.cmail.setup_headline = (function oc$web$components$cmail$setup_headline(state){
var temp__5735__auto__ = oc.web.components.cmail.headline_element(state);
if(cljs.core.truth_(temp__5735__auto__)){
var headline_el = temp__5735__auto__;
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","headline-input-listener","oc.web.components.cmail/headline-input-listener",-827978490).cljs$core$IFn$_invoke$arity$1(state),goog.events.listen(headline_el,goog.events.EventType.INPUT,(function (){
return oc.web.components.cmail.headline_on_change(state);
})));

emojiAutocomplete();

return oc.web.components.cmail.fullscreen_focus_headline(state);
} else {
return null;
}
});
/**
 * Avoid to paste rich text into headline, replace it with the plain text clipboard data.
 */
oc.web.components.cmail.headline_on_paste = (function oc$web$components$cmail$headline_on_paste(state,e){
oc.web.lib.utils.event_stop(e);

var clipboardData = (function (){var or__4126__auto__ = e.clipboardData;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return window.clipboardData;
}
})();
var pasted_data = clipboardData.getData("text/plain");
replaceSelectedText(pasted_data);

oc.web.components.cmail.headline_on_change(state);

if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(document.activeElement,document.body)){
var temp__5735__auto__ = oc.web.components.cmail.headline_element(state);
if(cljs.core.truth_(temp__5735__auto__)){
var headline_el = temp__5735__auto__;
return oc.web.lib.utils.to_end_of_content_editable(headline_el);
} else {
return null;
}
} else {
return null;
}
});
oc.web.components.cmail.add_emoji_cb = (function oc$web$components$cmail$add_emoji_cb(s){
return oc.web.lib.utils.after((180),(function (){
oc.web.components.cmail.headline_on_change(s);

return oc.web.components.cmail.body_on_change(s);
}));
});
oc.web.components.cmail.is_publishable_QMARK_ = (function oc$web$components$cmail$is_publishable_QMARK_(cmail_data){
return ((cljs.core.seq(oc.web.components.cmail.fix_headline(new cljs.core.Keyword(null,"headline","headline",-157157727).cljs$core$IFn$_invoke$arity$1(cmail_data)))) && (cljs.core.seq(new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(cmail_data))));
});
oc.web.components.cmail.maybe_publish = (function oc$web$components$cmail$maybe_publish(s,retry){
var latest_cmail_data = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"cmail-data","cmail-data",-550846261)));
var section_editing = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167)));
if(cljs.core.truth_((function (){var and__4115__auto__ = new cljs.core.Keyword(null,"auto-saving","auto-saving",68752642).cljs$core$IFn$_invoke$arity$1(latest_cmail_data);
if(cljs.core.truth_(and__4115__auto__)){
return (retry < (10));
} else {
return and__4115__auto__;
}
})())){
return oc.web.lib.utils.after((250),(function (){
var G__45866 = s;
var G__45867 = (retry + (1));
return (oc.web.components.cmail.real_post_action.cljs$core$IFn$_invoke$arity$2 ? oc.web.components.cmail.real_post_action.cljs$core$IFn$_invoke$arity$2(G__45866,G__45867) : oc.web.components.cmail.real_post_action.call(null,G__45866,G__45867));
}));
} else {
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","publishing","oc.web.components.cmail/publishing",-1953058566).cljs$core$IFn$_invoke$arity$1(s),true);

return oc.web.actions.activity.entry_publish.cljs$core$IFn$_invoke$arity$variadic(cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(latest_cmail_data,new cljs.core.Keyword(null,"status","status",-1997798413)),section_editing,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.first(oc.web.dispatcher.cmail_data_key)], 0));
}
});
oc.web.components.cmail.real_post_action = (function oc$web$components$cmail$real_post_action(var_args){
var G__45870 = arguments.length;
switch (G__45870) {
case 1:
return oc.web.components.cmail.real_post_action.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.components.cmail.real_post_action.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.components.cmail.real_post_action.cljs$core$IFn$_invoke$arity$1 = (function (s){
return oc.web.components.cmail.real_post_action.cljs$core$IFn$_invoke$arity$2(s,(0));
}));

(oc.web.components.cmail.real_post_action.cljs$core$IFn$_invoke$arity$2 = (function (s,retry){
var cmail_data = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"cmail-data","cmail-data",-550846261)));
var fixed_headline = oc.web.components.cmail.fix_headline(new cljs.core.Keyword(null,"headline","headline",-157157727).cljs$core$IFn$_invoke$arity$1(cmail_data));
var published_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(cmail_data),"published");
if(oc.web.components.cmail.is_publishable_QMARK_(cmail_data)){
var _ = oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),oc.web.dispatcher.cmail_data_key,(function (p1__45868_SHARP_){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__45868_SHARP_,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"headline","headline",-157157727),fixed_headline], null)], 0));
})], null));
var updated_cmail_data = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"cmail-data","cmail-data",-550846261)));
var section_editing = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167)));
if(published_QMARK_){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","saving","oc.web.components.cmail/saving",138854703).cljs$core$IFn$_invoke$arity$1(s),true);

return oc.web.actions.activity.entry_save.cljs$core$IFn$_invoke$arity$3(cljs.core.first(oc.web.dispatcher.cmail_data_key),updated_cmail_data,section_editing);
} else {
return oc.web.components.cmail.maybe_publish(s,retry);
}
} else {
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","show-post-tooltip","oc.web.components.cmail/show-post-tooltip",379256152).cljs$core$IFn$_invoke$arity$1(s),true);

oc.web.lib.utils.after((3000),(function (){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","show-post-tooltip","oc.web.components.cmail/show-post-tooltip",379256152).cljs$core$IFn$_invoke$arity$1(s),false);
}));

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","disable-post","oc.web.components.cmail/disable-post",-1180693829).cljs$core$IFn$_invoke$arity$1(s),false);
}
}));

(oc.web.components.cmail.real_post_action.cljs$lang$maxFixedArity = 2);

oc.web.components.cmail.post_clicked = (function oc$web$components$cmail$post_clicked(s){
oc.web.components.cmail.clean_body(s);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","disable-post","oc.web.components.cmail/disable-post",-1180693829).cljs$core$IFn$_invoke$arity$1(s),true);

oc.web.components.cmail.cancel_autosave_BANG_(s);

return oc.web.components.cmail.real_post_action.cljs$core$IFn$_invoke$arity$1(s);
});
/**
 * Fix the tooltips
 */
oc.web.components.cmail.fix_tooltips = (function oc$web$components$cmail$fix_tooltips(s){
var G__45877 = $(rum.core.dom_node(s)).find("[data-toggle=\"tooltip\"]");
G__45877.tooltip("hide");

G__45877.tooltip("fixTitle");

return G__45877;
});
oc.web.components.cmail.delete_clicked = (function oc$web$components$cmail$delete_clicked(s,e,activity_data){
if(cljs.core.truth_((function (){var or__4126__auto__ = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(activity_data);
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
return new cljs.core.Keyword(null,"auto-saving","auto-saving",68752642).cljs$core$IFn$_invoke$arity$1(activity_data);
}
}
})())){
var post_type = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(activity_data),"published"))?"post":"draft");
var alert_data = new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"icon","icon",1679606541),"/img/ML/trash.svg",new cljs.core.Keyword(null,"action","action",-811238024),"delete-entry",new cljs.core.Keyword(null,"message","message",-406056002),["Delete this ",post_type,"?"].join(''),new cljs.core.Keyword(null,"link-button-title","link-button-title",-812305976),"No",new cljs.core.Keyword(null,"link-button-cb","link-button-cb",559710301),(function (){
return oc.web.components.ui.alert_modal.hide_alert();
}),new cljs.core.Keyword(null,"solid-button-style","solid-button-style",-723792244),new cljs.core.Keyword(null,"red","red",-969428204),new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"Yes",new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),(function (){
oc.web.actions.activity.activity_delete(activity_data);

return oc.web.components.ui.alert_modal.hide_alert();
})], null);
return oc.web.components.ui.alert_modal.show_alert(alert_data);
} else {
if(cljs.core.truth_(new cljs.core.Keyword(null,"has-changes","has-changes",-631476764).cljs$core$IFn$_invoke$arity$1(activity_data))){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),oc.web.dispatcher.cmail_data_key,(function (p1__45878_SHARP_){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(p1__45878_SHARP_,new cljs.core.Keyword(null,"has-changes","has-changes",-631476764));
})], null));
} else {
}

return oc.web.actions.cmail.cmail_hide();
}
});
oc.web.components.cmail.win_width = (function oc$web$components$cmail$win_width(){
var or__4126__auto__ = document.documentElement.clientWidth;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return window.innerWidth;
}
});
oc.web.components.cmail.calc_video_height = (function oc$web$components$cmail$calc_video_height(s){
if(oc.web.lib.responsive.is_tablet_or_mobile_QMARK_()){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","mobile-video-height","oc.web.components.cmail/mobile-video-height",2131502731).cljs$core$IFn$_invoke$arity$1(s),oc.web.lib.utils.calc_video_height(oc.web.components.cmail.win_width()));
} else {
return null;
}
});
oc.web.components.cmail.collapse_if_needed = (function oc$web$components$cmail$collapse_if_needed(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45943 = arguments.length;
var i__4737__auto___45944 = (0);
while(true){
if((i__4737__auto___45944 < len__4736__auto___45943)){
args__4742__auto__.push((arguments[i__4737__auto___45944]));

var G__45945 = (i__4737__auto___45944 + (1));
i__4737__auto___45944 = G__45945;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.components.cmail.collapse_if_needed.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.components.cmail.collapse_if_needed.cljs$core$IFn$_invoke$arity$variadic = (function (s,p__45883){
var vec__45884 = p__45883;
var e = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__45884,(0),null);
var cmail_data = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"cmail-data","cmail-data",-550846261)));
var cmail_state = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"cmail-state","cmail-state",-747393321)));
var showing_section_picker_QMARK_ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","show-section-picker","oc.web.components.cmail/show-section-picker",1876580802).cljs$core$IFn$_invoke$arity$1(s));
var event_in_QMARK_ = (cljs.core.truth_(e)?oc.web.lib.utils.event_inside_QMARK_(e,rum.core.dom_node(s)):false);
if(cljs.core.truth_((function (){var or__4126__auto__ = new cljs.core.Keyword(null,"fullscren","fullscren",1990736975).cljs$core$IFn$_invoke$arity$1(cmail_state);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = new cljs.core.Keyword(null,"collapsed","collapsed",-628494523).cljs$core$IFn$_invoke$arity$1(cmail_state);
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
var or__4126__auto____$2 = event_in_QMARK_;
if(cljs.core.truth_(or__4126__auto____$2)){
return or__4126__auto____$2;
} else {
var or__4126__auto____$3 = new cljs.core.Keyword(null,"has-changes","has-changes",-631476764).cljs$core$IFn$_invoke$arity$1(cmail_data);
if(cljs.core.truth_(or__4126__auto____$3)){
return or__4126__auto____$3;
} else {
var or__4126__auto____$4 = new cljs.core.Keyword(null,"auto-saving","auto-saving",68752642).cljs$core$IFn$_invoke$arity$1(cmail_data);
if(cljs.core.truth_(or__4126__auto____$4)){
return or__4126__auto____$4;
} else {
var or__4126__auto____$5 = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(cmail_data);
if(cljs.core.truth_(or__4126__auto____$5)){
return or__4126__auto____$5;
} else {
return showing_section_picker_QMARK_;
}
}
}
}
}
}
})())){
return null;
} else {
oc.web.components.cmail.real_close();

return oc.web.components.cmail.headline_element(s).blur();
}
}));

(oc.web.components.cmail.collapse_if_needed.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.components.cmail.collapse_if_needed.cljs$lang$applyTo = (function (seq45880){
var G__45881 = cljs.core.first(seq45880);
var seq45880__$1 = cljs.core.next(seq45880);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__45881,seq45880__$1);
}));

oc.web.components.cmail.close_cmail = (function oc$web$components$cmail$close_cmail(s,e){
var cmail_data = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"cmail-data","cmail-data",-550846261)));
if(oc.web.utils.activity.has_content_QMARK_(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cmail_data,new cljs.core.Keyword(null,"body","body",-2049205669),oc.web.components.cmail.cleaned_body()))){
oc.web.components.cmail.autosave.cljs$core$IFn$_invoke$arity$2(s,true);
} else {
oc.web.actions.activity.activity_delete(cmail_data);
}

if(cljs.core.truth_(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(cmail_data)),new cljs.core.Keyword(null,"published","published",-514587618)))?new cljs.core.Keyword(null,"has-changes","has-changes",-631476764).cljs$core$IFn$_invoke$arity$1(cmail_data):false))){
return oc.web.components.cmail.cancel_clicked(s);
} else {
return oc.web.actions.cmail.cmail_hide();
}
});
oc.web.components.cmail.reset_cmail = (function oc$web$components$cmail$reset_cmail(s){
var cmail_data = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"cmail-data","cmail-data",-550846261)));
var cmail_state = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"cmail-state","cmail-state",-747393321)));
var initial_body = ((cljs.core.seq(new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(cmail_data)))?new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(cmail_data):oc.web.utils.activity.empty_body_html);
var initial_headline = oc.web.lib.utils.emojify(((cljs.core.seq(new cljs.core.Keyword(null,"headline","headline",-157157727).cljs$core$IFn$_invoke$arity$1(cmail_data)))?new cljs.core.Keyword(null,"headline","headline",-157157727).cljs$core$IFn$_invoke$arity$1(cmail_data):""));
var body_text = $("<div/>").html(initial_body).text();
if(cljs.core.seq(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(cmail_data))){
} else {
oc.web.actions.nux.dismiss_add_post_tooltip();
}

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","last-body","oc.web.components.cmail/last-body",166956190).cljs$core$IFn$_invoke$arity$1(s),initial_body);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","initial-body","oc.web.components.cmail/initial-body",1455098281).cljs$core$IFn$_invoke$arity$1(s),initial_body);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","initial-headline","oc.web.components.cmail/initial-headline",-1364928210).cljs$core$IFn$_invoke$arity$1(s),initial_headline);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","initial-uuid","oc.web.components.cmail/initial-uuid",-932181711).cljs$core$IFn$_invoke$arity$1(s),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(cmail_data));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","publishing","oc.web.components.cmail/publishing",-1953058566).cljs$core$IFn$_invoke$arity$1(s),new cljs.core.Keyword(null,"publishing","publishing",-244219384).cljs$core$IFn$_invoke$arity$1(cmail_data));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","show-placeholder","oc.web.components.cmail/show-placeholder",-1055554100).cljs$core$IFn$_invoke$arity$1(s),cljs.core.not(initial_body.match(/.*(<iframe\s?.*>).*/i)));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","post-tt-kw","oc.web.components.cmail/post-tt-kw",1997423179).cljs$core$IFn$_invoke$arity$1(s),((cljs.core.seq(new cljs.core.Keyword(null,"headline","headline",-157157727).cljs$core$IFn$_invoke$arity$1(cmail_data)))?null:new cljs.core.Keyword(null,"title","title",636505583)));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","latest-key","oc.web.components.cmail/latest-key",-1446427183).cljs$core$IFn$_invoke$arity$1(s),new cljs.core.Keyword(null,"key","key",-1516042587).cljs$core$IFn$_invoke$arity$1(cmail_state));

oc.web.lib.utils.after((300),(function (){
return oc.web.components.cmail.setup_headline(s);
}));

if(cljs.core.truth_(oc.web.lib.responsive.is_mobile_size_QMARK_())){
return oc.web.utils.dom.lock_page_scroll();
} else {
return null;
}
});
oc.web.components.cmail.hide_section_picker = (function oc$web$components$cmail$hide_section_picker(s){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","show-section-picker","oc.web.components.cmail/show-section-picker",1876580802).cljs$core$IFn$_invoke$arity$1(s),false);
});
oc.web.components.cmail.clear_delayed_show_section_picker = (function oc$web$components$cmail$clear_delayed_show_section_picker(s){
var delayed_show_section_picker = new cljs.core.Keyword("oc.web.components.cmail","delayed-show-section-picker","oc.web.components.cmail/delayed-show-section-picker",-187815495).cljs$core$IFn$_invoke$arity$1(s);
if(cljs.core.truth_(cljs.core.deref(delayed_show_section_picker))){
window.clearTimeout(cljs.core.deref(delayed_show_section_picker));

return cljs.core.reset_BANG_(delayed_show_section_picker,null);
} else {
return null;
}
});
oc.web.components.cmail.maybe_hide_section_picker = (function oc$web$components$cmail$maybe_hide_section_picker(s){
oc.web.components.cmail.clear_delayed_show_section_picker(s);

if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","show-section-picker","oc.web.components.cmail/show-section-picker",1876580802).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"hover","hover",-341141711))){
return oc.web.components.cmail.hide_section_picker(s);
} else {
return null;
}
});
oc.web.components.cmail.show_section_picker = (function oc$web$components$cmail$show_section_picker(s,v){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","show-section-picker","oc.web.components.cmail/show-section-picker",1876580802).cljs$core$IFn$_invoke$arity$1(s),v);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","delayed-show-section-picker","oc.web.components.cmail/delayed-show-section-picker",-187815495).cljs$core$IFn$_invoke$arity$1(s),null);
});
oc.web.components.cmail.maybe_show_section_picker = (function oc$web$components$cmail$maybe_show_section_picker(s){
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","show-section-picker","oc.web.components.cmail/show-section-picker",1876580802).cljs$core$IFn$_invoke$arity$1(s)))){
return null;
} else {
oc.web.components.cmail.clear_delayed_show_section_picker(s);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","delayed-show-section-picker","oc.web.components.cmail/delayed-show-section-picker",-187815495).cljs$core$IFn$_invoke$arity$1(s),oc.web.lib.utils.after((720),(function (){
return oc.web.components.cmail.show_section_picker(s,new cljs.core.Keyword(null,"hover","hover",-341141711));
})));
}
});
oc.web.components.cmail.toggle_section_picker = (function oc$web$components$cmail$toggle_section_picker(s){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","show-section-picker","oc.web.components.cmail/show-section-picker",1876580802).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"click","click",1912301393))){
return oc.web.components.cmail.hide_section_picker(s);
} else {
return oc.web.components.cmail.show_section_picker(s,new cljs.core.Keyword(null,"click","click",1912301393));
}
});
oc.web.components.cmail.cmail = rum.core.build_defcs((function (s){
var is_mobile_QMARK_ = oc.web.lib.responsive.is_tablet_or_mobile_QMARK_();
var _current_board_slug = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"board-slug","board-slug",99003663));
var cmail_state = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"cmail-state","cmail-state",-747393321));
var cmail_data_STAR_ = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"cmail-data","cmail-data",-550846261));
var cmail_data = cljs.core.update.cljs$core$IFn$_invoke$arity$3(cmail_data_STAR_,new cljs.core.Keyword(null,"board-name","board-name",-677515056),(function (p1__45901_SHARP_){
if(cljs.core.truth_(new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803).cljs$core$IFn$_invoke$arity$1(cmail_data_STAR_))){
return oc.web.components.cmail.self_board_name;
} else {
return p1__45901_SHARP_;
}
}));
var payments_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"payments","payments",-1324138047));
var show_paywall_alert_QMARK_ = oc.web.actions.payments.show_paywall_alert_QMARK_(payments_data);
var published_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(cmail_data),"published");
var video_size = ((is_mobile_QMARK_)?new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"width","width",-384071477),oc.web.components.cmail.win_width(),new cljs.core.Keyword(null,"height","height",1025178622),cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","mobile-video-height","oc.web.components.cmail/mobile-video-height",2131502731).cljs$core$IFn$_invoke$arity$1(s))], null):new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"width","width",-384071477),(548),new cljs.core.Keyword(null,"height","height",1025178622),oc.web.lib.utils.calc_video_height((548))], null));
var show_edit_tooltip = (function (){var and__4115__auto__ = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"show-edit-tooltip","show-edit-tooltip",2074919569));
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(cljs.core.seq(cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","initial-uuid","oc.web.components.cmail/initial-uuid",-932181711).cljs$core$IFn$_invoke$arity$1(s))));
} else {
return and__4115__auto__;
}
})();
var publishable_QMARK_ = oc.web.components.cmail.is_publishable_QMARK_(cmail_data);
var show_post_bt_tooltip_QMARK_ = (!(publishable_QMARK_));
var post_tt_kw = cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","post-tt-kw","oc.web.components.cmail/post-tt-kw",1997423179).cljs$core$IFn$_invoke$arity$1(s));
var disabled_QMARK_ = (function (){var or__4126__auto__ = show_post_bt_tooltip_QMARK_;
if(or__4126__auto__){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = show_paywall_alert_QMARK_;
if(or__4126__auto____$1){
return or__4126__auto____$1;
} else {
var or__4126__auto____$2 = (!(publishable_QMARK_));
if(or__4126__auto____$2){
return or__4126__auto____$2;
} else {
var or__4126__auto____$3 = cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","publishing","oc.web.components.cmail/publishing",-1953058566).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(or__4126__auto____$3)){
return or__4126__auto____$3;
} else {
return cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","disable-post","oc.web.components.cmail/disable-post",-1180693829).cljs$core$IFn$_invoke$arity$1(s));
}
}
}
}
})();
var working_QMARK_ = (function (){var or__4126__auto__ = ((published_QMARK_)?cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","saving","oc.web.components.cmail/saving",138854703).cljs$core$IFn$_invoke$arity$1(s)):false);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
if((!(published_QMARK_))){
return cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","publishing","oc.web.components.cmail/publishing",-1953058566).cljs$core$IFn$_invoke$arity$1(s));
} else {
return false;
}
}
})();
var unpublished_QMARK_ = cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(cmail_data),"published");
var post_button_title = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(cmail_data),"published"))?"Save":"Share update");
var did_pick_section = (function (board_data,note,dismiss_action){
oc.web.components.cmail.hide_section_picker(s);

if(cljs.core.truth_((function (){var and__4115__auto__ = board_data;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.seq(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(board_data));
} else {
return and__4115__auto__;
}
})())){
var has_changes_45948 = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"has-changes","has-changes",-631476764).cljs$core$IFn$_invoke$arity$1(cmail_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = cljs.core.seq(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(cmail_data));
if(or__4126__auto____$1){
return or__4126__auto____$1;
} else {
return new cljs.core.Keyword(null,"auto-saving","auto-saving",68752642).cljs$core$IFn$_invoke$arity$1(cmail_data);
}
}
})();
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),oc.web.dispatcher.cmail_data_key,cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cmail_data,new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"board-slug","board-slug",99003663),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(board_data),new cljs.core.Keyword(null,"board-name","board-name",-677515056),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(board_data),new cljs.core.Keyword(null,"board-access","board-access",1233510317),new cljs.core.Keyword(null,"access","access",2027349272).cljs$core$IFn$_invoke$arity$1(board_data),new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803),new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803).cljs$core$IFn$_invoke$arity$1(board_data),new cljs.core.Keyword(null,"has-changes","has-changes",-631476764),has_changes_45948,new cljs.core.Keyword(null,"invite-note","invite-note",38467972),note], null)], 0))], null));

if(cljs.core.truth_(has_changes_45948)){
oc.web.components.cmail.debounced_autosave_BANG_(s);
} else {
}

if(cljs.core.fn_QMARK_(dismiss_action)){
return (dismiss_action.cljs$core$IFn$_invoke$arity$0 ? dismiss_action.cljs$core$IFn$_invoke$arity$0() : dismiss_action.call(null));
} else {
return null;
}
} else {
return null;
}
});
var current_user_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915));
var editable_boards = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"editable-boards","editable-boards",1897056658));
var show_section_picker_QMARK_ = ((((cljs.core.not(cljs.core.some(new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803),editable_boards))) && (oc.web.local_settings.publisher_board_enabled_QMARK_) && ((cljs.core.count(editable_boards) > (0))))) || ((cljs.core.count(editable_boards) > (1))));
return React.createElement("div",({"onClick": (cljs.core.truth_((((!(is_mobile_QMARK_)))?(function (){var and__4115__auto__ = new cljs.core.Keyword(null,"collapsed","collapsed",-628494523).cljs$core$IFn$_invoke$arity$1(cmail_state);
if(cljs.core.truth_(and__4115__auto__)){
return (((!(show_paywall_alert_QMARK_))) && (cljs.core.not(new cljs.core.Keyword(null,"fullscreen","fullscreen",-4371054).cljs$core$IFn$_invoke$arity$1(cmail_state))));
} else {
return and__4115__auto__;
}
})():false))?(function (e){
oc.web.actions.nux.dismiss_add_post_tooltip();

oc.web.actions.cmail.cmail_expand(cmail_data,cmail_state);

return oc.web.lib.utils.after((280),(function (){
var temp__5735__auto__ = oc.web.components.cmail.headline_element(s);
if(cljs.core.truth_(temp__5735__auto__)){
var el = temp__5735__auto__;
return oc.web.lib.utils.to_end_of_content_editable(el);
} else {
return null;
}
}));
}):null), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["cmail-outer",oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"quick-post-collapsed","quick-post-collapsed",-1893561749),(function (){var or__4126__auto__ = new cljs.core.Keyword(null,"collapsed","collapsed",-628494523).cljs$core$IFn$_invoke$arity$1(cmail_state);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return show_paywall_alert_QMARK_;
}
})(),new cljs.core.Keyword(null,"show-trial-expired-alert","show-trial-expired-alert",2113617531),show_paywall_alert_QMARK_,new cljs.core.Keyword(null,"fullscreen","fullscreen",-4371054),((cljs.core.not(new cljs.core.Keyword(null,"collapsed","collapsed",-628494523).cljs$core$IFn$_invoke$arity$1(cmail_state)))?new cljs.core.Keyword(null,"fullscreen","fullscreen",-4371054).cljs$core$IFn$_invoke$arity$1(cmail_state):false)], null))], null))}),sablono.interpreter.interpret((cljs.core.truth_(((show_paywall_alert_QMARK_)?new cljs.core.Keyword(null,"collapsed","collapsed",-628494523).cljs$core$IFn$_invoke$arity$1(cmail_state):false))?(function (){var G__45914 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"top","top",-1856271961),"48px",new cljs.core.Keyword(null,"left","left",-399115937),"50%"], null);
return (oc.web.components.ui.trial_expired_banner.trial_expired_alert.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.trial_expired_banner.trial_expired_alert.cljs$core$IFn$_invoke$arity$1(G__45914) : oc.web.components.ui.trial_expired_banner.trial_expired_alert.call(null,G__45914));
})():null)),React.createElement("div",({"ref": "cmail-container", "className": "cmail-container"}),React.createElement("div",({"className": "cmail-mobile-header"}),React.createElement("button",({"onClick": cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.components.cmail.close_cmail,s), "className": "mlb-reset mobile-close-bt"})),React.createElement("div",({"className": "cmail-mobile-header-right"}),React.createElement("button",({"onClick": (function (){
return oc.web.components.cmail.add_attachment(s);
}), "className": "mlb-reset mobile-attachment-button"})),(function (){var attrs45915 = (function (){var G__45916 = new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"on-submit","on-submit",1227871159),(function (){
return oc.web.components.cmail.post_clicked(s);
}),new cljs.core.Keyword(null,"disabled","disabled",-1529784218),disabled_QMARK_,new cljs.core.Keyword(null,"title","title",636505583),post_button_title,new cljs.core.Keyword(null,"post-tt-kw","post-tt-kw",825208149),post_tt_kw,new cljs.core.Keyword(null,"force-show-tooltip","force-show-tooltip",-76488892),cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","show-post-tooltip","oc.web.components.cmail/show-post-tooltip",379256152).cljs$core$IFn$_invoke$arity$1(s))], null);
return (oc.web.components.ui.post_to_button.post_to_button.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.post_to_button.post_to_button.cljs$core$IFn$_invoke$arity$1(G__45916) : oc.web.components.ui.post_to_button.post_to_button.call(null,G__45916));
})();
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs45915))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["post-button-container","group"], null)], null),attrs45915], 0))):({"className": "post-button-container group"})),((cljs.core.map_QMARK_(attrs45915))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs45915)], null)));
})())),React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["dismiss-inline-cmail-container",((unpublished_QMARK_)?"long-tooltip":null)], null))}),React.createElement("button",({"onClick": cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.components.cmail.close_cmail,s), "data-toggle": ((is_mobile_QMARK_)?null:"tooltip"), "data-placement": "top", "title": ((unpublished_QMARK_)?"Save & Close":"Close"), "className": "mlb-reset dismiss-inline-cmail"}))),React.createElement("div",({"style": sablono.interpreter.attributes((cljs.core.truth_((((!(is_mobile_QMARK_)))?new cljs.core.Keyword(null,"fullscreen","fullscreen",-4371054).cljs$core$IFn$_invoke$arity$1(cmail_state):false))?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"padding-top","padding-top",1929675955),[cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","top-padding","oc.web.components.cmail/top-padding",481343021).cljs$core$IFn$_invoke$arity$1(s))),"px"].join('')], null):null)), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["cmail-content-outer",oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"showing-edit-tooltip","showing-edit-tooltip",2045137677),show_edit_tooltip], null))], null))}),React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["cmail-content",((show_section_picker_QMARK_)?"section-picker-visible":null)], null))}),sablono.interpreter.interpret(((is_mobile_QMARK_)?new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.section-picker-bt-container","div.section-picker-bt-container",-1047414542),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ref","ref",1289896967),new cljs.core.Keyword(null,"section-picker-container","section-picker-container",1114978110)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.post-to","span.post-to",1696435903),"Post to"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.section-picker-bt","button.mlb-reset.section-picker-bt",-1943849965),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.components.cmail.toggle_section_picker(s);
})], null),new cljs.core.Keyword(null,"board-name","board-name",-677515056).cljs$core$IFn$_invoke$arity$1(cmail_data)], null),(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","show-section-picker","oc.web.components.cmail/show-section-picker",1876580802).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.section-picker-container","div.section-picker-container",-1857927303),(function (){var G__45917 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"active-slug","active-slug",-1213605190),new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(cmail_data),new cljs.core.Keyword(null,"on-change","on-change",-732046149),did_pick_section,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915),current_user_data], null);
return (oc.web.components.ui.sections_picker.sections_picker.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.sections_picker.sections_picker.cljs$core$IFn$_invoke$arity$1(G__45917) : oc.web.components.ui.sections_picker.sections_picker.call(null,G__45917));
})()], null):null)], null):null)),React.createElement("div",({"className": "cmail-content-headline-container group"}),React.createElement("div",({"contentEditable": true, "key": ["cmail-headline-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"key","key",-1516042587).cljs$core$IFn$_invoke$arity$1(cmail_state))].join(''), "placeholder": oc.web.utils.activity.headline_placeholder, "ref": "headline", "onPaste": (function (p1__45906_SHARP_){
return oc.web.components.cmail.headline_on_paste(s,p1__45906_SHARP_);
}), "onKeyDown": (function (e){
oc.web.lib.utils.after((10),(function (){
return oc.web.components.cmail.headline_on_change(s);
}));

if(cljs.core.truth_((function (){var and__4115__auto__ = e.metaKey;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2("Enter",e.key);
} else {
return and__4115__auto__;
}
})())){
return oc.web.components.cmail.post_clicked(s);
} else {
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(e.key,"Enter")) && (cljs.core.not(e.metaKey)))){
oc.web.lib.utils.event_stop(e);

return oc.web.lib.utils.to_end_of_content_editable(oc.web.components.cmail.body_element());
} else {
return null;
}
}
}), "dangerouslySetInnerHTML": cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","initial-headline","oc.web.components.cmail/initial-headline",-1364928210).cljs$core$IFn$_invoke$arity$1(s)), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, ["cmail-content-headline","emoji-autocomplete","emojiable",oc.web.lib.utils.hide_class], null))}))),sablono.interpreter.interpret(((is_mobile_QMARK_)?null:new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.cmail-content-collapsed-placeholder","div.cmail-content-collapsed-placeholder",-887431068),[oc.web.lib.utils.default_body_placeholder,"..."].join('')], null))),sablono.interpreter.interpret((function (){var G__45918 = cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"static-positioned-media-picker","static-positioned-media-picker",-888920383),new cljs.core.Keyword(null,"media-config","media-config",1588458658),new cljs.core.Keyword(null,"placeholder","placeholder",-104873083),new cljs.core.Keyword(null,"use-inline-media-picker","use-inline-media-picker",1605876743),new cljs.core.Keyword(null,"media-picker-container-selector","media-picker-container-selector",1731260615),new cljs.core.Keyword(null,"media-picker-initially-visible","media-picker-initially-visible",1391698183),new cljs.core.Keyword(null,"attachments-enabled","attachments-enabled",772816647),new cljs.core.Keyword(null,"show-h2","show-h2",-66668656),new cljs.core.Keyword(null,"initial-body","initial-body",-1797899085),new cljs.core.Keyword(null,"dispatch-input-key","dispatch-input-key",-1224535693),new cljs.core.Keyword(null,"upload-progress-cb","upload-progress-cb",-1327280683),new cljs.core.Keyword(null,"cmd-enter-cb","cmd-enter-cb",-2018742375),new cljs.core.Keyword(null,"on-change","on-change",-732046149),new cljs.core.Keyword(null,"paywall?","paywall?",-1706178021),new cljs.core.Keyword(null,"show-placeholder","show-placeholder",385667262),new cljs.core.Keyword(null,"classes","classes",2037804510),new cljs.core.Keyword(null,"cmail-key","cmail-key",1231846078)],[true,new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, ["poll","code","gif","photo","video"], null),[oc.web.lib.utils.default_body_placeholder,"..."].join(''),true,"div.cmail-outer div.cmail-container div.cmail-footer div.cmail-footer-media-picker-container",false,true,true,cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","initial-body","oc.web.components.cmail/initial-body",1455098281).cljs$core$IFn$_invoke$arity$1(s)),cljs.core.first(oc.web.dispatcher.cmail_data_key),(function (is_uploading_QMARK_){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","uploading-media","oc.web.components.cmail/uploading-media",-930090348).cljs$core$IFn$_invoke$arity$1(s),is_uploading_QMARK_);
}),(function (){
return oc.web.components.cmail.post_clicked(s);
}),cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.components.cmail.body_on_change,s),show_paywall_alert_QMARK_,cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","show-placeholder","oc.web.components.cmail/show-placeholder",-1055554100).cljs$core$IFn$_invoke$arity$1(s)),[((show_paywall_alert_QMARK_)?null:"emoji-autocomplete "),"emojiable ",oc.web.lib.utils.hide_class].join(''),new cljs.core.Keyword(null,"key","key",-1516042587).cljs$core$IFn$_invoke$arity$1(cmail_state)]);
return (oc.web.components.rich_body_editor.rich_body_editor.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.rich_body_editor.rich_body_editor.cljs$core$IFn$_invoke$arity$1(G__45918) : oc.web.components.rich_body_editor.rich_body_editor.call(null,G__45918));
})()),sablono.interpreter.interpret((function (){var G__45919 = new cljs.core.Keyword(null,"attachments","attachments",-1535547830).cljs$core$IFn$_invoke$arity$1(cmail_data);
var G__45920 = null;
var G__45921 = (function (p1__45907_SHARP_){
return oc.web.actions.activity.remove_attachment(cljs.core.first(oc.web.dispatcher.cmail_data_key),p1__45907_SHARP_);
});
return (oc.web.components.ui.stream_attachments.stream_attachments.cljs$core$IFn$_invoke$arity$3 ? oc.web.components.ui.stream_attachments.stream_attachments.cljs$core$IFn$_invoke$arity$3(G__45919,G__45920,G__45921) : oc.web.components.ui.stream_attachments.stream_attachments.call(null,G__45919,G__45920,G__45921));
})()),sablono.interpreter.interpret(((cljs.core.seq(new cljs.core.Keyword(null,"polls","polls",-580623582).cljs$core$IFn$_invoke$arity$1(cmail_data)))?(function (){var G__45922 = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"polls-data","polls-data",-975205110),new cljs.core.Keyword(null,"polls","polls",-580623582).cljs$core$IFn$_invoke$arity$1(cmail_data),new cljs.core.Keyword(null,"editing?","editing?",1646440800),true,new cljs.core.Keyword(null,"current-user-id","current-user-id",-2013633451),oc.web.lib.jwt.user_id(),new cljs.core.Keyword(null,"container-selector","container-selector",6506114),"div.cmail-content",new cljs.core.Keyword(null,"dispatch-key","dispatch-key",733619510),cljs.core.first(oc.web.dispatcher.cmail_data_key),new cljs.core.Keyword(null,"activity-data","activity-data",1293689136),cmail_data], null);
return (oc.web.components.ui.poll.polls_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.poll.polls_wrapper.cljs$core$IFn$_invoke$arity$1(G__45922) : oc.web.components.ui.poll.polls_wrapper.call(null,G__45922));
})():null)))),React.createElement("div",({"className": "cmail-footer"}),(function (){var attrs45923 = (function (){var G__45927 = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"on-submit","on-submit",1227871159),(function (){
return oc.web.components.cmail.post_clicked(s);
}),new cljs.core.Keyword(null,"disabled","disabled",-1529784218),disabled_QMARK_,new cljs.core.Keyword(null,"title","title",636505583),post_button_title,new cljs.core.Keyword(null,"post-tt-kw","post-tt-kw",825208149),post_tt_kw,new cljs.core.Keyword(null,"force-show-tooltip","force-show-tooltip",-76488892),cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","show-post-tooltip","oc.web.components.cmail/show-post-tooltip",379256152).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"show-on-hover","show-on-hover",-9980645),true], null);
return (oc.web.components.ui.post_to_button.post_to_button.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.post_to_button.post_to_button.cljs$core$IFn$_invoke$arity$1(G__45927) : oc.web.components.ui.post_to_button.post_to_button.call(null,G__45927));
})();
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs45923))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["post-button-container","group"], null)], null),attrs45923], 0))):({"className": "post-button-container group"})),((cljs.core.map_QMARK_(attrs45923))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs45923)], null)));
})(),React.createElement("div",({"onMouseLeave": (function (){
return oc.web.components.cmail.maybe_hide_section_picker(s);
}), "onMouseEnter": (function (){
return oc.web.components.cmail.maybe_show_section_picker(s);
}), "ref": "section-picker-container", "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["section-picker-bt-container",((show_section_picker_QMARK_)?null:"hidden")], null))}),React.createElement("button",({"onClick": (function (){
return oc.web.components.cmail.toggle_section_picker(s);
}), "data-placement": "top", "data-toggle": "tooltip", "title": oc.web.components.cmail.board_tooltip, "className": "mlb-reset section-picker-bt"}),sablono.interpreter.interpret(new cljs.core.Keyword(null,"board-name","board-name",-677515056).cljs$core$IFn$_invoke$arity$1(cmail_data))),sablono.interpreter.interpret((cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","show-section-picker","oc.web.components.cmail/show-section-picker",1876580802).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.section-picker-container","div.section-picker-container",-1857927303),(function (){var G__45928 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"active-slug","active-slug",-1213605190),new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(cmail_data),new cljs.core.Keyword(null,"on-change","on-change",-732046149),did_pick_section,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915),current_user_data], null);
return (oc.web.components.ui.sections_picker.sections_picker.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.sections_picker.sections_picker.cljs$core$IFn$_invoke$arity$1(G__45928) : oc.web.components.ui.sections_picker.sections_picker.call(null,G__45928));
})()], null):null))),sablono.interpreter.interpret((function (){var G__45929 = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"add-emoji-cb","add-emoji-cb",-375710574),cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.components.cmail.add_emoji_cb,s),new cljs.core.Keyword(null,"width","width",-384071477),(24),new cljs.core.Keyword(null,"height","height",1025178622),(32),new cljs.core.Keyword(null,"position","position",-2011731912),"bottom",new cljs.core.Keyword(null,"default-field-selector","default-field-selector",179085335),"div.cmail-content div.rich-body-editor",new cljs.core.Keyword(null,"container-selector","container-selector",6506114),"div.cmail-content"], null);
return (oc.web.components.ui.emoji_picker.emoji_picker.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.emoji_picker.emoji_picker.cljs$core$IFn$_invoke$arity$1(G__45929) : oc.web.components.ui.emoji_picker.emoji_picker.call(null,G__45929));
})()),React.createElement("button",({"onClick": (function (){
return oc.web.components.cmail.add_attachment(s);
}), "data-toggle": "tooltip", "data-placement": "top", "data-container": "body", "title": "Add attachment", "className": "mlb-reset attachment-button"})),React.createElement("div",({"className": "cmail-footer-media-picker-container group"})),(function (){var attrs45926 = (cljs.core.truth_(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(cmail_data))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.delete-bt-container","div.delete-bt-container",568194315),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.delete-bt","button.mlb-reset.delete-bt",-168688558),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (p1__45908_SHARP_){
return oc.web.components.cmail.delete_clicked(s,p1__45908_SHARP_,cmail_data);
}),new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687),((is_mobile_QMARK_)?null:"tooltip"),new cljs.core.Keyword(null,"data-placement","data-placement",166529525),"top",new cljs.core.Keyword(null,"title","title",636505583),"Delete"], null)], null)], null):null);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs45926))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["cmail-footer-right"], null)], null),attrs45926], 0))):({"className": "cmail-footer-right"})),((cljs.core.map_QMARK_(attrs45926))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs45926)], null)));
})())));
}),cljs.core.PersistentVector.fromArray([rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"cmail-state","cmail-state",-747393321)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"cmail-data","cmail-data",-550846261)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"show-edit-tooltip","show-edit-tooltip",2074919569)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"payments","payments",-1324138047)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"follow-boards-list","follow-boards-list",-461166530)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"editable-boards","editable-boards",1897056658)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"board-slug","board-slug",99003663)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2("",new cljs.core.Keyword("oc.web.components.cmail","initial-body","oc.web.components.cmail/initial-body",1455098281)),rum.core.local.cljs$core$IFn$_invoke$arity$2("",new cljs.core.Keyword("oc.web.components.cmail","initial-headline","oc.web.components.cmail/initial-headline",-1364928210)),rum.core.local.cljs$core$IFn$_invoke$arity$2(true,new cljs.core.Keyword("oc.web.components.cmail","show-placeholder","oc.web.components.cmail/show-placeholder",-1055554100)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.cmail","initial-uuid","oc.web.components.cmail/initial-uuid",-932181711)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.cmail","headline-input-listener","oc.web.components.cmail/headline-input-listener",-827978490)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.cmail","uploading-media","oc.web.components.cmail/uploading-media",-930090348)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.cmail","saving","oc.web.components.cmail/saving",138854703)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.cmail","publishing","oc.web.components.cmail/publishing",-1953058566)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.cmail","disable-post","oc.web.components.cmail/disable-post",-1180693829)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.cmail","debounced-autosave","oc.web.components.cmail/debounced-autosave",1937334018)),rum.core.local.cljs$core$IFn$_invoke$arity$2((0),new cljs.core.Keyword("oc.web.components.cmail","mobile-video-height","oc.web.components.cmail/mobile-video-height",2131502731)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.cmail","media-attachment-did-success","oc.web.components.cmail/media-attachment-did-success",1340629320)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.cmail","media-attachment","oc.web.components.cmail/media-attachment",1000917759)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.cmail","latest-key","oc.web.components.cmail/latest-key",-1446427183)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.cmail","show-post-tooltip","oc.web.components.cmail/show-post-tooltip",379256152)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.cmail","show-section-picker","oc.web.components.cmail/show-section-picker",1876580802)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.cmail","delayed-show-section-picker","oc.web.components.cmail/delayed-show-section-picker",-187815495)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.cmail","last-body","oc.web.components.cmail/last-body",166956190)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.cmail","post-tt-kw","oc.web.components.cmail/post-tt-kw",1997423179)),rum.core.local.cljs$core$IFn$_invoke$arity$2((68),new cljs.core.Keyword("oc.web.components.cmail","top-padding","oc.web.components.cmail/top-padding",481343021)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.cmail","last-fullscreen-state","oc.web.components.cmail/last-fullscreen-state",-173641173)),oc.web.mixins.ui.render_on_resize(oc.web.components.cmail.calc_video_height),oc.web.mixins.ui.refresh_tooltips_mixin,(cljs.core.truth_(oc.web.lib.responsive.is_mobile_size_QMARK_())?null:oc.web.mixins.ui.on_window_click_mixin(oc.web.components.cmail.collapse_if_needed)),oc.web.mixins.ui.on_click_out.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"section-picker-container","section-picker-container",1114978110),(function (s,e){
return oc.web.components.cmail.hide_section_picker(s);
})),oc.web.mixins.ui.on_click_out.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"cmail-container","cmail-container",2093929253),(function (p1__45898_SHARP_,p2__45899_SHARP_){
if(cljs.core.truth_(((cljs.core.not(oc.web.lib.responsive.is_mobile_size_QMARK_()))?new cljs.core.Keyword(null,"fullscreen","fullscreen",-4371054).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(org.martinklepsch.derivatives.get_ref(p1__45898_SHARP_,new cljs.core.Keyword(null,"cmail-state","cmail-state",-747393321)))):false))){
return oc.web.components.cmail.close_cmail(p1__45898_SHARP_,p2__45899_SHARP_);
} else {
return null;
}
})),new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
oc.web.components.cmail.reset_cmail(s);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","last-fullscreen-state","oc.web.components.cmail/last-fullscreen-state",-173641173).cljs$core$IFn$_invoke$arity$1(s),new cljs.core.Keyword(null,"fullscreen","fullscreen",-4371054).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"cmail-state","cmail-state",-747393321)))));

return s;
}),new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
oc.web.components.cmail.calc_video_height(s);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","debounced-autosave","oc.web.components.cmail/debounced-autosave",1937334018).cljs$core$IFn$_invoke$arity$1(s),(new goog.async.Debouncer((function (){
return oc.web.components.cmail.autosave.cljs$core$IFn$_invoke$arity$1(s);
}),(2000))));

oc.web.components.cmail.setup_top_padding(s);

return s;
}),new cljs.core.Keyword(null,"will-update","will-update",328062998),(function (s){
var cmail_state_45949 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"cmail-state","cmail-state",-747393321)));
if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","latest-key","oc.web.components.cmail/latest-key",-1446427183).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"key","key",-1516042587).cljs$core$IFn$_invoke$arity$1(cmail_state_45949))){
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","latest-key","oc.web.components.cmail/latest-key",-1446427183).cljs$core$IFn$_invoke$arity$1(s)))){
oc.web.components.cmail.reset_cmail(s);
} else {
}

if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","latest-key","oc.web.components.cmail/latest-key",-1446427183).cljs$core$IFn$_invoke$arity$1(s)))){
} else {
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","latest-key","oc.web.components.cmail/latest-key",-1446427183).cljs$core$IFn$_invoke$arity$1(s),new cljs.core.Keyword(null,"key","key",-1516042587).cljs$core$IFn$_invoke$arity$1(cmail_state_45949));
}
} else {
}

return s;
}),new cljs.core.Keyword(null,"did-update","did-update",-2143702256),(function (s){
var temp__5735__auto___45950 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"cmail-state","cmail-state",-747393321)));
if(cljs.core.truth_(temp__5735__auto___45950)){
var cmail_state_45951 = temp__5735__auto___45950;
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"fullscreen","fullscreen",-4371054).cljs$core$IFn$_invoke$arity$1(cmail_state_45951),cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","last-fullscreen-state","oc.web.components.cmail/last-fullscreen-state",-173641173).cljs$core$IFn$_invoke$arity$1(s)))){
} else {
if(cljs.core.truth_(new cljs.core.Keyword(null,"fullscreen","fullscreen",-4371054).cljs$core$IFn$_invoke$arity$1(cmail_state_45951))){
oc.web.components.cmail.fullscreen_focus_headline(s);
} else {
}

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","last-fullscreen-state","oc.web.components.cmail/last-fullscreen-state",-173641173).cljs$core$IFn$_invoke$arity$1(s),new cljs.core.Keyword(null,"fullscreen","fullscreen",-4371054).cljs$core$IFn$_invoke$arity$1(cmail_state_45951));
}
} else {
}

return s;
}),new cljs.core.Keyword(null,"before-render","before-render",71256781),(function (s){
var temp__5735__auto___45952 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"cmail-data","cmail-data",-550846261)));
if(cljs.core.truth_(temp__5735__auto___45952)){
var cmail_data_45953 = temp__5735__auto___45952;
if(cljs.core.truth_(new cljs.core.Keyword(null,"delete","delete",-1768633620).cljs$core$IFn$_invoke$arity$1(cmail_data_45953))){
oc.web.components.cmail.real_close();
} else {
}

if(cljs.core.truth_((function (){var and__4115__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","saving","oc.web.components.cmail/saving",138854703).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(new cljs.core.Keyword(null,"loading","loading",-737050189).cljs$core$IFn$_invoke$arity$1(cmail_data_45953));
} else {
return and__4115__auto__;
}
})())){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","saving","oc.web.components.cmail/saving",138854703).cljs$core$IFn$_invoke$arity$1(s),false);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","disable-post","oc.web.components.cmail/disable-post",-1180693829).cljs$core$IFn$_invoke$arity$1(s),false);

if(cljs.core.truth_(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(cmail_data_45953))){
} else {
oc.web.lib.utils.after((100),oc.web.components.cmail.real_close);
}
} else {
}

if(cljs.core.truth_((function (){var and__4115__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","publishing","oc.web.components.cmail/publishing",-1953058566).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(new cljs.core.Keyword(null,"publishing","publishing",-244219384).cljs$core$IFn$_invoke$arity$1(cmail_data_45953));
} else {
return and__4115__auto__;
}
})())){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","publishing","oc.web.components.cmail/publishing",-1953058566).cljs$core$IFn$_invoke$arity$1(s),false);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","disable-post","oc.web.components.cmail/disable-post",-1180693829).cljs$core$IFn$_invoke$arity$1(s),false);

if(cljs.core.truth_(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(cmail_data_45953))){
} else {
if(cljs.core.seq(new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(cmail_data_45953))){
oc.web.components.cmail.real_close();

oc.web.lib.utils.after((180),(function (){
var follow_boards_list = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"follow-boards-list","follow-boards-list",-461166530)));
var following_board_QMARK_ = cljs.core.some((function (p1__45900_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(p1__45900_SHARP_),new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(cmail_data_45953))){
return p1__45900_SHARP_;
} else {
return null;
}
}),follow_boards_list);
var current_board_slug = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"board-slug","board-slug",99003663)));
var posting_to_current_board_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug),cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(cmail_data_45953)));
var to_url = (cljs.core.truth_((function (){var and__4115__auto__ = following_board_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return (!(posting_to_current_board_QMARK_));
} else {
return and__4115__auto__;
}
})())?new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"slug","slug",2029314850),"following",new cljs.core.Keyword(null,"url","url",276297046),oc.web.urls.following.cljs$core$IFn$_invoke$arity$0(),new cljs.core.Keyword(null,"refresh","refresh",1947415525),false], null):new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"slug","slug",2029314850),new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(cmail_data_45953),new cljs.core.Keyword(null,"url","url",276297046),oc.web.urls.board.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(cmail_data_45953)),new cljs.core.Keyword(null,"refresh","refresh",1947415525),true], null));
return oc.web.actions.nav_sidebar.nav_to_url_BANG_.cljs$core$IFn$_invoke$arity$5(null,new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(to_url),new cljs.core.Keyword(null,"url","url",276297046).cljs$core$IFn$_invoke$arity$1(to_url),(0),new cljs.core.Keyword(null,"refresh","refresh",1947415525).cljs$core$IFn$_invoke$arity$1(to_url));
}));
} else {
}
}
} else {
}
} else {
}

return s;
}),new cljs.core.Keyword(null,"after-render","after-render",1997533433),(function (s){
oc.web.components.cmail.fix_tooltips(s);

return s;
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
oc.web.actions.nux.dismiss_edit_tooltip();

if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","headline-input-listener","oc.web.components.cmail/headline-input-listener",-827978490).cljs$core$IFn$_invoke$arity$1(s)))){
goog.events.unlistenByKey(cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","headline-input-listener","oc.web.components.cmail/headline-input-listener",-827978490).cljs$core$IFn$_invoke$arity$1(s)));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.cmail","headline-input-listener","oc.web.components.cmail/headline-input-listener",-827978490).cljs$core$IFn$_invoke$arity$1(s),null);
} else {
}

if(cljs.core.truth_(oc.web.lib.responsive.is_mobile_size_QMARK_())){
oc.web.utils.dom.unlock_page_scroll();
} else {
}

var temp__5735__auto___45954 = cljs.core.deref(new cljs.core.Keyword("oc.web.components.cmail","debounced-autosave","oc.web.components.cmail/debounced-autosave",1937334018).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(temp__5735__auto___45954)){
var debounced_autosave_45955 = temp__5735__auto___45954;
debounced_autosave_45955.dispose();
} else {
}

return s;
})], null)], true),"cmail");

//# sourceMappingURL=oc.web.components.cmail.js.map
