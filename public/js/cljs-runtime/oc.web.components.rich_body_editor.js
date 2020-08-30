goog.provide('oc.web.components.rich_body_editor');
oc.web.components.rich_body_editor.body_on_change = (function oc$web$components$rich_body_editor$body_on_change(state){
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.rich-body-editor","did-change","oc.web.components.rich-body-editor/did-change",450720187).cljs$core$IFn$_invoke$arity$1(state)))){
} else {
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.rich-body-editor","did-change","oc.web.components.rich-body-editor/did-change",450720187).cljs$core$IFn$_invoke$arity$1(state),true);
}

var options = cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(state));
var on_change = new cljs.core.Keyword(null,"on-change","on-change",-732046149).cljs$core$IFn$_invoke$arity$1(options);
return (on_change.cljs$core$IFn$_invoke$arity$0 ? on_change.cljs$core$IFn$_invoke$arity$0() : on_change.call(null));
});
oc.web.components.rich_body_editor.rich_body_editor = rum.core.build_defcs((function (s,p__45823){
var map__45824 = p__45823;
var map__45824__$1 = (((((!((map__45824 == null))))?(((((map__45824.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45824.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45824):map__45824);
var paywall_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45824__$1,new cljs.core.Keyword(null,"paywall?","paywall?",-1706178021));
var on_change = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45824__$1,new cljs.core.Keyword(null,"on-change","on-change",-732046149));
var attachment_dom_selector = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45824__$1,new cljs.core.Keyword(null,"attachment-dom-selector","attachment-dom-selector",1000930748));
var classes = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45824__$1,new cljs.core.Keyword(null,"classes","classes",2037804510));
var cmail_key = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45824__$1,new cljs.core.Keyword(null,"cmail-key","cmail-key",1231846078));
var show_placeholder = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45824__$1,new cljs.core.Keyword(null,"show-placeholder","show-placeholder",385667262));
var fullscreen = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45824__$1,new cljs.core.Keyword(null,"fullscreen","fullscreen",-4371054));
var initial_body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45824__$1,new cljs.core.Keyword(null,"initial-body","initial-body",-1797899085));
var dispatch_input_key = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45824__$1,new cljs.core.Keyword(null,"dispatch-input-key","dispatch-input-key",-1224535693));
var upload_progress_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45824__$1,new cljs.core.Keyword(null,"upload-progress-cb","upload-progress-cb",-1327280683));
var _mention_users = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"mention-users","mention-users",-525519758));
var _media_input = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"media-input","media-input",107658136));
var _users_info_hover = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"users-info-hover","users-info-hover",-941434570));
var _current_user_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915));
var _follow_publishers_list = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"follow-publishers-list","follow-publishers-list",-374150342));
var _followers_publishers_count = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"followers-publishers-count","followers-publishers-count",-692976579));
var hide_placeholder_QMARK_ = (function (){var or__4126__auto__ = cljs.core.not(show_placeholder);
if(or__4126__auto__){
return or__4126__auto__;
} else {
return cljs.core.deref(new cljs.core.Keyword("oc.web.components.rich-body-editor","did-change","oc.web.components.rich-body-editor/did-change",450720187).cljs$core$IFn$_invoke$arity$1(s));
}
})();
return React.createElement("div",({"key": ["rich-body-editor-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cmail_key)].join(''), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["rich-body-editor-outer-container",(cljs.core.truth_(paywall_QMARK_)?"block-edit":null)], null))}),React.createElement("div",({"className": "rich-body-editor-container"}),React.createElement("div",({"ref": "editor-node", "contentEditable": cljs.core.not(paywall_QMARK_), "dangerouslySetInnerHTML": oc.web.lib.utils.emojify(initial_body), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, ["rich-body-editor","oc-mentions","oc-mentions-hover","editing",[cljs.core.str.cljs$core$IFn$_invoke$arity$1(classes),oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"medium-editor-placeholder-hidden","medium-editor-placeholder-hidden",30561611),hide_placeholder_QMARK_,new cljs.core.Keyword(null,"medium-editor-placeholder-relative","medium-editor-placeholder-relative",145573649),cljs.core.not(hide_placeholder_QMARK_),new cljs.core.Keyword(null,"medium-editor-element","medium-editor-element",1287568604),true,new cljs.core.Keyword(null,"uploading","uploading",1069939393),cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","upload-lock","oc.web.utils.medium-editor-media/upload-lock",-1921399674).cljs$core$IFn$_invoke$arity$1(s))], null))].join('')], null))}))),sablono.interpreter.interpret((cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","showing-media-video-modal","oc.web.utils.medium-editor-media/showing-media-video-modal",1606579887).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.video-container","div.video-container",-1024597571),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ref","ref",1289896967),new cljs.core.Keyword(null,"video-container","video-container",-271437239)], null),(function (){var G__45826 = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"fullscreen","fullscreen",-4371054),fullscreen,new cljs.core.Keyword(null,"dismiss-cb","dismiss-cb",-1282537857),(function (){
oc.web.utils.medium_editor_media.media_video_add(s,cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-picker-ext","oc.web.utils.medium-editor-media/media-picker-ext",805783011).cljs$core$IFn$_invoke$arity$1(s)),null);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","showing-media-video-modal","oc.web.utils.medium-editor-media/showing-media-video-modal",1606579887).cljs$core$IFn$_invoke$arity$1(s),false);
}),new cljs.core.Keyword(null,"offset-element-selector","offset-element-selector",-64578356),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.rich-body-editor-outer-container","div.rich-body-editor-outer-container",-1412600987)], null),new cljs.core.Keyword(null,"outer-container-selector","outer-container-selector",1406024934),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.cmail-content-outer","div.cmail-content-outer",1386545837)], null)], null);
return (oc.web.components.ui.media_video_modal.media_video_modal.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.media_video_modal.media_video_modal.cljs$core$IFn$_invoke$arity$1(G__45826) : oc.web.components.ui.media_video_modal.media_video_modal.call(null,G__45826));
})()], null):null)),sablono.interpreter.interpret((cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","showing-gif-selector","oc.web.utils.medium-editor-media/showing-gif-selector",1051253553).cljs$core$IFn$_invoke$arity$1(s)))?(function (){var G__45827 = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"fullscreen","fullscreen",-4371054),fullscreen,new cljs.core.Keyword(null,"pick-emoji-cb","pick-emoji-cb",-830701499),(function (gif_obj){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","showing-gif-selector","oc.web.utils.medium-editor-media/showing-gif-selector",1051253553).cljs$core$IFn$_invoke$arity$1(s),false);

return oc.web.utils.medium_editor_media.media_gif_add(s,cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-picker-ext","oc.web.utils.medium-editor-media/media-picker-ext",805783011).cljs$core$IFn$_invoke$arity$1(s)),gif_obj);
}),new cljs.core.Keyword(null,"offset-element-selector","offset-element-selector",-64578356),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.rich-body-editor-outer-container","div.rich-body-editor-outer-container",-1412600987)], null),new cljs.core.Keyword(null,"outer-container-selector","outer-container-selector",1406024934),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.cmail-content-outer","div.cmail-content-outer",1386545837)], null)], null);
return (oc.web.components.ui.giphy_picker.giphy_picker.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.giphy_picker.giphy_picker.cljs$core$IFn$_invoke$arity$1(G__45827) : oc.web.components.ui.giphy_picker.giphy_picker.call(null,G__45827));
})():null)));
}),new cljs.core.PersistentVector(null, 21, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.rich-body-editor","did-change","oc.web.components.rich-body-editor/did-change",450720187)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.utils.medium-editor-media","editor","oc.web.utils.medium-editor-media/editor",1463568801)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-picker-ext","oc.web.utils.medium-editor-media/media-picker-ext",805783011)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-photo","oc.web.utils.medium-editor-media/media-photo",1416359036)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-video","oc.web.utils.medium-editor-media/media-video",1963418271)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-attachment","oc.web.utils.medium-editor-media/media-attachment",-971860820)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-photo-did-success","oc.web.utils.medium-editor-media/media-photo-did-success",13253331)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-attachment-did-success","oc.web.utils.medium-editor-media/media-attachment-did-success",-111823171)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.utils.medium-editor-media","showing-media-video-modal","oc.web.utils.medium-editor-media/showing-media-video-modal",1606579887)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.utils.medium-editor-media","showing-gif-selector","oc.web.utils.medium-editor-media/showing-gif-selector",1051253553)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.utils.medium-editor-media","upload-lock","oc.web.utils.medium-editor-media/upload-lock",-1921399674)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"media-input","media-input",107658136)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"mention-users","mention-users",-525519758)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"users-info-hover","users-info-hover",-941434570)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"follow-publishers-list","follow-publishers-list",-374150342)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"followers-publishers-count","followers-publishers-count",-692976579)], 0)),oc.web.mixins.mention.oc_mentions_hover(),oc.web.mixins.ui.on_window_click_mixin((function (s,e){
if(cljs.core.truth_((function (){var and__4115__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","showing-media-video-modal","oc.web.utils.medium-editor-media/showing-media-video-modal",1606579887).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(and__4115__auto__)){
return ((cljs.core.not(oc.web.lib.utils.event_inside_QMARK_(e,document.querySelector("button.media.media-video")))) && (cljs.core.not(oc.web.lib.utils.event_inside_QMARK_(e,rum.core.ref_node(s,new cljs.core.Keyword(null,"video-container","video-container",-271437239))))));
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
return ((cljs.core.not(oc.web.lib.utils.event_inside_QMARK_(e,document.querySelector("button.media.media-gif")))) && (cljs.core.not(oc.web.lib.utils.event_inside_QMARK_(e,document.querySelector("div.giphy-picker")))));
} else {
return and__4115__auto__;
}
})())){
oc.web.utils.medium_editor_media.media_gif_add(s,cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-picker-ext","oc.web.utils.medium-editor-media/media-picker-ext",805783011).cljs$core$IFn$_invoke$arity$1(s)),null);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","showing-gif-selector","oc.web.utils.medium-editor-media/showing-gif-selector",1051253553).cljs$core$IFn$_invoke$arity$1(s),false);
} else {
return null;
}
})),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
var props_45839 = cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s));
oc.web.lib.utils.after((300),(function (){
return oc.web.utils.medium_editor_media.setup_editor(s,oc.web.components.rich_body_editor.body_on_change,cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s)));
}));

return s;
}),new cljs.core.Keyword(null,"did-remount","did-remount",1362550500),(function (o,s){
oc.web.utils.medium_editor_media.setup_editor(s,oc.web.components.rich_body_editor.body_on_change,cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s)));

if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"cmail-key","cmail-key",1231846078).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(o))),new cljs.core.Keyword(null,"cmail-key","cmail-key",1231846078).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s))))){
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","editor","oc.web.utils.medium-editor-media/editor",1463568801).cljs$core$IFn$_invoke$arity$1(s)))){
cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","editor","oc.web.utils.medium-editor-media/editor",1463568801).cljs$core$IFn$_invoke$arity$1(s)).destroy();
} else {
}

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","editor","oc.web.utils.medium-editor-media/editor",1463568801).cljs$core$IFn$_invoke$arity$1(s),null);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-picker-ext","oc.web.utils.medium-editor-media/media-picker-ext",805783011).cljs$core$IFn$_invoke$arity$1(s),null);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.rich-body-editor","did-change","oc.web.components.rich-body-editor/did-change",450720187).cljs$core$IFn$_invoke$arity$1(s),false);

oc.web.lib.utils.after((10),(function (){
return oc.web.utils.medium_editor_media.setup_editor(s,oc.web.components.rich_body_editor.body_on_change,cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s)));
}));
} else {
}

return s;
}),new cljs.core.Keyword(null,"will-update","will-update",328062998),(function (s){
var data_45840 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"media-input","media-input",107658136)));
var video_data_45841 = new cljs.core.Keyword(null,"media-video","media-video",339369994).cljs$core$IFn$_invoke$arity$1(data_45840);
if(cljs.core.truth_((function (){var and__4115__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-video","oc.web.utils.medium-editor-media/media-video",1963418271).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(and__4115__auto__)){
return ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(video_data_45841,new cljs.core.Keyword(null,"dismiss","dismiss",412569545))) || (cljs.core.map_QMARK_(video_data_45841)));
} else {
return and__4115__auto__;
}
})())){
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(video_data_45841,new cljs.core.Keyword(null,"dismiss","dismiss",412569545))) || (cljs.core.map_QMARK_(video_data_45841)))){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-video","oc.web.utils.medium-editor-media/media-video",1963418271).cljs$core$IFn$_invoke$arity$1(s),false);

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"media-input","media-input",107658136)], null),(function (p1__45822_SHARP_){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(p1__45822_SHARP_,new cljs.core.Keyword(null,"media-video","media-video",339369994));
})], null));
} else {
}

if(cljs.core.map_QMARK_(video_data_45841)){
oc.web.utils.medium_editor_media.media_video_add(s,cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-picker-ext","oc.web.utils.medium-editor-media/media-picker-ext",805783011).cljs$core$IFn$_invoke$arity$1(s)),video_data_45841);
} else {
oc.web.utils.medium_editor_media.media_video_add(s,cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-picker-ext","oc.web.utils.medium-editor-media/media-picker-ext",805783011).cljs$core$IFn$_invoke$arity$1(s)),null);
}
} else {
}

return s;
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","editor","oc.web.utils.medium-editor-media/editor",1463568801).cljs$core$IFn$_invoke$arity$1(s)))){
cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","editor","oc.web.utils.medium-editor-media/editor",1463568801).cljs$core$IFn$_invoke$arity$1(s)).destroy();
} else {
}

return s;
})], null)], null),"rich-body-editor");

//# sourceMappingURL=oc.web.components.rich_body_editor.js.map
