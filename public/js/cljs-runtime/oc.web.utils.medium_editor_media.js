goog.provide('oc.web.utils.medium_editor_media');
var module$node_modules$medium_editor$dist$js$medium_editor=shadow.js.require("module$node_modules$medium_editor$dist$js$medium_editor", {});
oc.web.utils.medium_editor_media.get_media_picker_extension = (function oc$web$utils$medium_editor_media$get_media_picker_extension(s){
var body_el = rum.core.ref_node(s,"editor-node");
var editor = module$node_modules$medium_editor$dist$js$medium_editor.getEditorFromElement(body_el);
var media_picker_ext = editor.getExtensionByName("media-picker");
return media_picker_ext;
});
oc.web.utils.medium_editor_media.add_poll = (function oc$web$utils$medium_editor_media$add_poll(s,options,editable){
var delay = (cljs.core.truth_(new cljs.core.Keyword(null,"collapsed","collapsed",-628494523).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"cmail-state","cmail-state",-747393321).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state))))?(500):(0));
return oc.web.lib.utils.maybe_after(delay,(function (){
if(cljs.core.truth_(new cljs.core.Keyword(null,"use-inline-media-picker","use-inline-media-picker",1605876743).cljs$core$IFn$_invoke$arity$1(options))){
} else {
var editable_44331__$1 = (function (){var or__4126__auto__ = editable;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.utils.medium_editor_media.get_media_picker_extension(s);
}
})();
editable_44331__$1.saveSelection();
}

var poll_id = oc.web.utils.poll.new_poll_id();
var dispatch_input_key = new cljs.core.Keyword(null,"dispatch-input-key","dispatch-input-key",-1224535693).cljs$core$IFn$_invoke$arity$1(options);
editable.addPoll(poll_id);

return oc.web.actions.poll.add_poll(dispatch_input_key,poll_id);
}));
});
oc.web.utils.medium_editor_media.add_gif = (function oc$web$utils$medium_editor_media$add_gif(s,editable){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","showing-gif-selector","oc.web.utils.medium-editor-media/showing-gif-selector",1051253553).cljs$core$IFn$_invoke$arity$1(s),true);
});
oc.web.utils.medium_editor_media.media_gif_add = (function oc$web$utils$medium_editor_media$media_gif_add(s,editable,gif_data){
if((gif_data == null)){
return editable.addGIF(null,null,null,null);
} else {
var original = (function (){var target_obj_44235 = gif_data;
var _STAR_runtime_state_STAR__orig_val__44238 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__44239 = oops.state.prepare_state(target_obj_44235,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__44239);

try{var next_obj_44236 = ((oops.core.validate_object_access_dynamically(target_obj_44235,(0),"images",true,true,false))?(target_obj_44235["images"]):null);
var next_obj_44237 = ((oops.core.validate_object_access_dynamically(next_obj_44236,(0),"original",true,true,false))?(next_obj_44236["original"]):null);
return next_obj_44237;
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__44238);
}})();
var original_url = (function (){var or__4126__auto__ = (function (){var target_obj_44244 = original;
var _STAR_runtime_state_STAR__orig_val__44246 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__44247 = oops.state.prepare_state(target_obj_44244,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__44247);

try{var next_obj_44245 = ((oops.core.validate_object_access_dynamically(target_obj_44244,(1),"url",true,true,false))?(target_obj_44244["url"]):null);
if((!((next_obj_44245 == null)))){
return next_obj_44245;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__44246);
}})();
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var target_obj_44248 = original;
var _STAR_runtime_state_STAR__orig_val__44250 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__44251 = oops.state.prepare_state(target_obj_44248,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__44251);

try{var next_obj_44249 = ((oops.core.validate_object_access_dynamically(target_obj_44248,(1),"gif_url",true,true,false))?(target_obj_44248["gif_url"]):null);
if((!((next_obj_44249 == null)))){
return next_obj_44249;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__44250);
}}
})();
var fixed_width_still = (function (){var target_obj_44252 = gif_data;
var _STAR_runtime_state_STAR__orig_val__44255 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__44256 = oops.state.prepare_state(target_obj_44252,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__44256);

try{var next_obj_44253 = ((oops.core.validate_object_access_dynamically(target_obj_44252,(0),"images",true,true,false))?(target_obj_44252["images"]):null);
var next_obj_44254 = ((oops.core.validate_object_access_dynamically(next_obj_44253,(0),"fixed_width_still",true,true,false))?(next_obj_44253["fixed_width_still"]):null);
return next_obj_44254;
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__44255);
}})();
var fixed_width_still_url = (function (){var or__4126__auto__ = (function (){var target_obj_44261 = fixed_width_still;
var _STAR_runtime_state_STAR__orig_val__44263 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__44264 = oops.state.prepare_state(target_obj_44261,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__44264);

try{var next_obj_44262 = ((oops.core.validate_object_access_dynamically(target_obj_44261,(1),"url",true,true,false))?(target_obj_44261["url"]):null);
if((!((next_obj_44262 == null)))){
return next_obj_44262;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__44263);
}})();
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var target_obj_44265 = fixed_width_still;
var _STAR_runtime_state_STAR__orig_val__44267 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__44268 = oops.state.prepare_state(target_obj_44265,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__44268);

try{var next_obj_44266 = ((oops.core.validate_object_access_dynamically(target_obj_44265,(1),"gif_url",true,true,false))?(target_obj_44265["gif_url"]):null);
if((!((next_obj_44266 == null)))){
return next_obj_44266;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__44267);
}}
})();
var original_width = (function (){var target_obj_44269 = original;
var _STAR_runtime_state_STAR__orig_val__44271 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__44272 = oops.state.prepare_state(target_obj_44269,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__44272);

try{var next_obj_44270 = ((oops.core.validate_object_access_dynamically(target_obj_44269,(0),"width",true,true,false))?(target_obj_44269["width"]):null);
return next_obj_44270;
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__44271);
}})();
var original_height = (function (){var target_obj_44273 = original;
var _STAR_runtime_state_STAR__orig_val__44275 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__44276 = oops.state.prepare_state(target_obj_44273,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__44276);

try{var next_obj_44274 = ((oops.core.validate_object_access_dynamically(target_obj_44273,(0),"height",true,true,false))?(target_obj_44273["height"]):null);
return next_obj_44274;
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__44275);
}})();
return editable.addGIF(original_url,fixed_width_still_url,original_width,original_height);
}
});
/**
 * Called every time the image picke close, reset to inital state.
 */
oc.web.utils.medium_editor_media.media_attachment_dismiss_picker = (function oc$web$utils$medium_editor_media$media_attachment_dismiss_picker(s,editable){
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-attachment-did-success","oc.web.utils.medium-editor-media/media-attachment-did-success",-111823171).cljs$core$IFn$_invoke$arity$1(s)))){
return null;
} else {
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-attachment","oc.web.utils.medium-editor-media/media-attachment",-971860820).cljs$core$IFn$_invoke$arity$1(s),false);
}
});
oc.web.utils.medium_editor_media.attachment_upload_failed_cb = (function oc$web$utils$medium_editor_media$attachment_upload_failed_cb(state,editable){
var alert_data = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"icon","icon",1679606541),"/img/ML/error_icon.png",new cljs.core.Keyword(null,"action","action",-811238024),"attachment-upload-error",new cljs.core.Keyword(null,"title","title",636505583),"Sorry!",new cljs.core.Keyword(null,"message","message",-406056002),"An error occurred with your file.",new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"OK",new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),(function (){
return oc.web.components.ui.alert_modal.hide_alert();
})], null);
oc.web.components.ui.alert_modal.show_alert(alert_data);

return oc.web.lib.utils.after((10),(function (){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-attachment-did-success","oc.web.utils.medium-editor-media/media-attachment-did-success",-111823171).cljs$core$IFn$_invoke$arity$1(state),false);

return oc.web.utils.medium_editor_media.media_attachment_dismiss_picker(state,editable);
}));
});
oc.web.utils.medium_editor_media.attachment_upload_success_cb = (function oc$web$utils$medium_editor_media$attachment_upload_success_cb(state,options,editable,res){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-attachment-did-success","oc.web.utils.medium-editor-media/media-attachment-did-success",-111823171).cljs$core$IFn$_invoke$arity$1(state),true);

var url = (function (){var target_obj_44277 = res;
var _STAR_runtime_state_STAR__orig_val__44279 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__44280 = oops.state.prepare_state(target_obj_44277,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__44280);

try{var next_obj_44278 = ((oops.core.validate_object_access_dynamically(target_obj_44277,(0),"url",true,true,false))?(target_obj_44277["url"]):null);
return next_obj_44278;
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__44279);
}})();
if(cljs.core.not(url)){
return oc.web.utils.medium_editor_media.attachment_upload_failed_cb(state,editable);
} else {
var size = (function (){var target_obj_44281 = res;
var _STAR_runtime_state_STAR__orig_val__44283 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__44284 = oops.state.prepare_state(target_obj_44281,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__44284);

try{var next_obj_44282 = ((oops.core.validate_object_access_dynamically(target_obj_44281,(0),"size",true,true,false))?(target_obj_44281["size"]):null);
return next_obj_44282;
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__44283);
}})();
var mimetype = (function (){var target_obj_44285 = res;
var _STAR_runtime_state_STAR__orig_val__44287 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__44288 = oops.state.prepare_state(target_obj_44285,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__44288);

try{var next_obj_44286 = ((oops.core.validate_object_access_dynamically(target_obj_44285,(0),"mimetype",true,true,false))?(target_obj_44285["mimetype"]):null);
return next_obj_44286;
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__44287);
}})();
var filename = (function (){var target_obj_44289 = res;
var _STAR_runtime_state_STAR__orig_val__44291 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__44292 = oops.state.prepare_state(target_obj_44289,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__44292);

try{var next_obj_44290 = ((oops.core.validate_object_access_dynamically(target_obj_44289,(0),"filename",true,true,false))?(target_obj_44289["filename"]):null);
return next_obj_44290;
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__44291);
}})();
var createdat = oc.web.lib.utils.js_date();
var prefix = ["Uploaded by ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"name","name",1843675177)))," on ",oc.web.lib.utils.date_string.cljs$core$IFn$_invoke$arity$variadic(createdat,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"year","year",335913393)], null)], 0))," - "].join('');
var subtitle = [prefix,clojure.contrib.humanize.filesize.cljs$core$IFn$_invoke$arity$variadic(size,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"binary","binary",-1802232288),false,new cljs.core.Keyword(null,"format","format",-1306924766),"%.2f"], 0))].join('');
var icon = oc.web.utils.activity.icon_for_mimetype(mimetype);
var attachment_data = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"file-name","file-name",-1654217259),filename,new cljs.core.Keyword(null,"file-type","file-type",1274948820),mimetype,new cljs.core.Keyword(null,"file-size","file-size",-1900760755),size,new cljs.core.Keyword(null,"file-url","file-url",-863738963),url], null);
var dispatch_input_key = new cljs.core.Keyword(null,"dispatch-input-key","dispatch-input-key",-1224535693).cljs$core$IFn$_invoke$arity$1(options);
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-attachment","oc.web.utils.medium-editor-media/media-attachment",-971860820).cljs$core$IFn$_invoke$arity$1(state),false);

oc.web.actions.activity.add_attachment(dispatch_input_key,attachment_data);

return oc.web.lib.utils.after((1000),(function (){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-attachment-did-success","oc.web.utils.medium-editor-media/media-attachment-did-success",-111823171).cljs$core$IFn$_invoke$arity$1(state),false);
}));
}
});
oc.web.utils.medium_editor_media.attachment_upload_error_cb = (function oc$web$utils$medium_editor_media$attachment_upload_error_cb(state,editable,res,error){
return oc.web.utils.medium_editor_media.attachment_upload_failed_cb(state,editable);
});
oc.web.utils.medium_editor_media.add_attachment = (function oc$web$utils$medium_editor_media$add_attachment(s,options,editable){
var editable__$1 = (function (){var or__4126__auto__ = editable;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.utils.medium_editor_media.get_media_picker_extension(s);
}
})();
editable__$1.saveSelection();

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-attachment","oc.web.utils.medium-editor-media/media-attachment",-971860820).cljs$core$IFn$_invoke$arity$1(s),true);

return oc.web.lib.image_upload.upload_BANG_.cljs$core$IFn$_invoke$arity$variadic(null,cljs.core.partial.cljs$core$IFn$_invoke$arity$4(oc.web.utils.medium_editor_media.attachment_upload_success_cb,s,options,editable__$1),null,cljs.core.partial.cljs$core$IFn$_invoke$arity$3(oc.web.utils.medium_editor_media.attachment_upload_error_cb,s,editable__$1),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(function (){
return oc.web.lib.utils.after((400),(function (){
return oc.web.utils.medium_editor_media.media_attachment_dismiss_picker(s,editable__$1);
}));
})], 0));
});
oc.web.utils.medium_editor_media.add_video = (function oc$web$utils$medium_editor_media$add_video(s,options,editable){
if(cljs.core.truth_(new cljs.core.Keyword(null,"use-inline-media-picker","use-inline-media-picker",1605876743).cljs$core$IFn$_invoke$arity$1(options))){
} else {
var editable_44337__$1 = (function (){var or__4126__auto__ = editable;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.utils.medium_editor_media.get_media_picker_extension(s);
}
})();
editable_44337__$1.saveSelection();
}

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"media-input","media-input",107658136),new cljs.core.Keyword(null,"media-video","media-video",339369994)], null),true], null));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-video","oc.web.utils.medium-editor-media/media-video",1963418271).cljs$core$IFn$_invoke$arity$1(s),true);

if(cljs.core.truth_(new cljs.core.Keyword(null,"use-inline-media-picker","use-inline-media-picker",1605876743).cljs$core$IFn$_invoke$arity$1(options))){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","showing-media-video-modal","oc.web.utils.medium-editor-media/showing-media-video-modal",1606579887).cljs$core$IFn$_invoke$arity$1(s),true);
} else {
return null;
}
});
oc.web.utils.medium_editor_media.get_video_thumbnail = (function oc$web$utils$medium_editor_media$get_video_thumbnail(video){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(video),new cljs.core.Keyword(null,"loom","loom",1998784953))){
return ["https://cdn.loom.com/sessions/thumbnails/",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(video)),"-00001.jpg"].join('');
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(video),new cljs.core.Keyword(null,"youtube","youtube",-1932361085))){
return ["https://img.youtube.com/vi/",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(video)),"/0.jpg"].join('');
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(video),new cljs.core.Keyword(null,"vimeo","vimeo",-1217463574))){
return new cljs.core.Keyword(null,"thumbnail","thumbnail",-867906798).cljs$core$IFn$_invoke$arity$1(video);
} else {
return null;
}
}
}
});
oc.web.utils.medium_editor_media.get_video_src = (function oc$web$utils$medium_editor_media$get_video_src(video){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(video),new cljs.core.Keyword(null,"loom","loom",1998784953))){
return ["https://www.loom.com/embed/",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(video))].join('');
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(video),new cljs.core.Keyword(null,"youtube","youtube",-1932361085))){
return ["https://www.youtube.com/embed/",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(video))].join('');
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(video),new cljs.core.Keyword(null,"vimeo","vimeo",-1217463574))){
return ["https://player.vimeo.com/video/",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(video))].join('');
} else {
return null;
}
}
}
});
oc.web.utils.medium_editor_media.media_video_add = (function oc$web$utils$medium_editor_media$media_video_add(s,editable,video_data){
if((video_data == null)){
return editable.addVideo(null,null,null,null);
} else {
return editable.addVideo(oc.web.utils.medium_editor_media.get_video_src(video_data),cljs.core.name(new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(video_data)),new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(video_data),oc.web.utils.medium_editor_media.get_video_thumbnail(video_data));
}
});
/**
 * Show an error alert view for failed uploads.
 */
oc.web.utils.medium_editor_media.media_photo_add_error = (function oc$web$utils$medium_editor_media$media_photo_add_error(s,options){
var alert_data = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"icon","icon",1679606541),"/img/ML/error_icon.png",new cljs.core.Keyword(null,"action","action",-811238024),"media-photo-upload-error",new cljs.core.Keyword(null,"title","title",636505583),"Sorry!",new cljs.core.Keyword(null,"message","message",-406056002),"An error occurred with your image.",new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"OK",new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),(function (){
return oc.web.components.ui.alert_modal.hide_alert();
})], null);
var upload_progress_cb = new cljs.core.Keyword(null,"upload-progress-cb","upload-progress-cb",-1327280683).cljs$core$IFn$_invoke$arity$1(options);
if(cljs.core.fn_QMARK_(upload_progress_cb)){
(upload_progress_cb.cljs$core$IFn$_invoke$arity$1 ? upload_progress_cb.cljs$core$IFn$_invoke$arity$1(false) : upload_progress_cb.call(null,false));
} else {
}

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","upload-lock","oc.web.utils.medium-editor-media/upload-lock",-1921399674).cljs$core$IFn$_invoke$arity$1(s),false);

return oc.web.components.ui.alert_modal.show_alert(alert_data);
});
oc.web.utils.medium_editor_media.media_photo_add_if_finished = (function oc$web$utils$medium_editor_media$media_photo_add_if_finished(s,options,editable){
var image = cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-photo","oc.web.utils.medium-editor-media/media-photo",1416359036).cljs$core$IFn$_invoke$arity$1(s));
var upload_progress_cb = new cljs.core.Keyword(null,"upload-progress-cb","upload-progress-cb",-1327280683).cljs$core$IFn$_invoke$arity$1(options);
if(((cljs.core.contains_QMARK_(image,new cljs.core.Keyword(null,"url","url",276297046))) && (cljs.core.contains_QMARK_(image,new cljs.core.Keyword(null,"width","width",-384071477))) && (cljs.core.contains_QMARK_(image,new cljs.core.Keyword(null,"height","height",1025178622))) && (cljs.core.contains_QMARK_(image,new cljs.core.Keyword(null,"thumbnail","thumbnail",-867906798))))){
editable.addPhoto(new cljs.core.Keyword(null,"url","url",276297046).cljs$core$IFn$_invoke$arity$1(image),new cljs.core.Keyword(null,"thumbnail","thumbnail",-867906798).cljs$core$IFn$_invoke$arity$1(image),new cljs.core.Keyword(null,"width","width",-384071477).cljs$core$IFn$_invoke$arity$1(image),new cljs.core.Keyword(null,"height","height",1025178622).cljs$core$IFn$_invoke$arity$1(image));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-photo","oc.web.utils.medium-editor-media/media-photo",1416359036).cljs$core$IFn$_invoke$arity$1(s),null);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-photo-did-success","oc.web.utils.medium-editor-media/media-photo-did-success",13253331).cljs$core$IFn$_invoke$arity$1(s),false);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","upload-lock","oc.web.utils.medium-editor-media/upload-lock",-1921399674).cljs$core$IFn$_invoke$arity$1(s),false);

if(cljs.core.fn_QMARK_(upload_progress_cb)){
return (upload_progress_cb.cljs$core$IFn$_invoke$arity$1 ? upload_progress_cb.cljs$core$IFn$_invoke$arity$1(false) : upload_progress_cb.call(null,false));
} else {
return null;
}
} else {
return null;
}
});
oc.web.utils.medium_editor_media.img_on_load = (function oc$web$utils$medium_editor_media$img_on_load(s,options,editable,url,img){
if(cljs.core.truth_((function (){var and__4115__auto__ = url;
if(cljs.core.truth_(and__4115__auto__)){
return img;
} else {
return and__4115__auto__;
}
})())){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-photo","oc.web.utils.medium-editor-media/media-photo",1416359036).cljs$core$IFn$_invoke$arity$1(s),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-photo","oc.web.utils.medium-editor-media/media-photo",1416359036).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"width","width",-384071477),img.width,new cljs.core.Keyword(null,"height","height",1025178622),img.height], null)], 0)));

goog.dom.removeNode(img);

return oc.web.utils.medium_editor_media.media_photo_add_if_finished(s,options,editable);
} else {
return oc.web.utils.medium_editor_media.media_photo_add_error(s,options);
}
});
/**
 * Called every time the image picke close, reset to inital state.
 */
oc.web.utils.medium_editor_media.media_photo_dismiss_picker = (function oc$web$utils$medium_editor_media$media_photo_dismiss_picker(s,editable){
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-photo-did-success","oc.web.utils.medium-editor-media/media-photo-did-success",13253331).cljs$core$IFn$_invoke$arity$1(s)))){
return null;
} else {
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-photo","oc.web.utils.medium-editor-media/media-photo",1416359036).cljs$core$IFn$_invoke$arity$1(s),false);

return editable.addPhoto(null,null,null,null);
}
});
oc.web.utils.medium_editor_media.add_photo = (function oc$web$utils$medium_editor_media$add_photo(s,options,editable){
var editable__$1 = (function (){var or__4126__auto__ = editable;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.utils.medium_editor_media.get_media_picker_extension(s);
}
})();
editable__$1.saveSelection();

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-photo","oc.web.utils.medium-editor-media/media-photo",1416359036).cljs$core$IFn$_invoke$arity$1(s),true);

var upload_progress_cb = new cljs.core.Keyword(null,"upload-progress-cb","upload-progress-cb",-1327280683).cljs$core$IFn$_invoke$arity$1(options);
return oc.web.lib.image_upload.upload_BANG_.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"accept","accept",1874130431),"image/*"], null),(function (res){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-photo-did-success","oc.web.utils.medium-editor-media/media-photo-did-success",13253331).cljs$core$IFn$_invoke$arity$1(s),true);

var url = (function (){var target_obj_44293 = res;
var _STAR_runtime_state_STAR__orig_val__44295 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__44296 = oops.state.prepare_state(target_obj_44293,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__44296);

try{var next_obj_44294 = ((oops.core.validate_object_access_dynamically(target_obj_44293,(0),"url",true,true,false))?(target_obj_44293["url"]):null);
return next_obj_44294;
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__44295);
}})();
var img = goog.dom.createDom("img");
(img.onload = (function (){
return oc.web.utils.medium_editor_media.img_on_load(s,options,editable__$1,url,img);
}));

(img.onerror = (function (){
return oc.web.utils.medium_editor_media.img_on_load(s,options,editable__$1,null,null);
}));

(img.className = "hidden");

goog.dom.append(document.body,img);

(img.src = url);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-photo","oc.web.utils.medium-editor-media/media-photo",1416359036).cljs$core$IFn$_invoke$arity$1(s),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"res","res",-1395007879),res,new cljs.core.Keyword(null,"url","url",276297046),url], null));

if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cuerdas.core.lower((function (){var target_obj_44305 = res;
var _STAR_runtime_state_STAR__orig_val__44307 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__44308 = oops.state.prepare_state(target_obj_44305,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__44308);

try{var next_obj_44306 = ((oops.core.validate_object_access_dynamically(target_obj_44305,(0),"mimetype",true,true,false))?(target_obj_44305["mimetype"]):null);
return next_obj_44306;
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__44307);
}})()),"image/svg+xml")) || (cuerdas.core.ends_with_QMARK_(cuerdas.core.lower((function (){var target_obj_44309 = res;
var _STAR_runtime_state_STAR__orig_val__44311 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__44312 = oops.state.prepare_state(target_obj_44309,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__44312);

try{var next_obj_44310 = ((oops.core.validate_object_access_dynamically(target_obj_44309,(0),"filename",true,true,false))?(target_obj_44309["filename"]):null);
return next_obj_44310;
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__44311);
}})()),".svg")))){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-photo","oc.web.utils.medium-editor-media/media-photo",1416359036).cljs$core$IFn$_invoke$arity$1(s),cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-photo","oc.web.utils.medium-editor-media/media-photo",1416359036).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"thumbnail","thumbnail",-867906798),url));

return oc.web.utils.medium_editor_media.media_photo_add_if_finished(s,options,editable__$1);
} else {
return oc.web.lib.image_upload.thumbnail_BANG_.cljs$core$IFn$_invoke$arity$variadic(url,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(function (thumbnail_url){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-photo","oc.web.utils.medium-editor-media/media-photo",1416359036).cljs$core$IFn$_invoke$arity$1(s),cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-photo","oc.web.utils.medium-editor-media/media-photo",1416359036).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"thumbnail","thumbnail",-867906798),thumbnail_url));

return oc.web.utils.medium_editor_media.media_photo_add_if_finished(s,options,editable__$1);
}),(function (res__$1,progress){
return null;
}),(function (res__$1,err){
return oc.web.utils.medium_editor_media.media_photo_add_error(s,options);
})], 0));
}
}),(function (res,progress){
return null;
}),(function (err){
return oc.web.utils.medium_editor_media.media_photo_add_error(s,options);
}),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(function (){
return oc.web.lib.utils.after((1000),(function (){
return oc.web.utils.medium_editor_media.media_photo_dismiss_picker(s,editable__$1);
}));
}),(function (res){
return null;
}),(function (res){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","upload-lock","oc.web.utils.medium-editor-media/upload-lock",-1921399674).cljs$core$IFn$_invoke$arity$1(s),true);

if(cljs.core.fn_QMARK_(upload_progress_cb)){
return (upload_progress_cb.cljs$core$IFn$_invoke$arity$1 ? upload_progress_cb.cljs$core$IFn$_invoke$arity$1(true) : upload_progress_cb.call(null,true));
} else {
return null;
}
}),(function (res){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","upload-lock","oc.web.utils.medium-editor-media/upload-lock",-1921399674).cljs$core$IFn$_invoke$arity$1(s),true);

if(cljs.core.fn_QMARK_(upload_progress_cb)){
return (upload_progress_cb.cljs$core$IFn$_invoke$arity$1 ? upload_progress_cb.cljs$core$IFn$_invoke$arity$1(true) : upload_progress_cb.call(null,true));
} else {
return null;
}
})], 0));
});
oc.web.utils.medium_editor_media.on_picker_click = (function oc$web$utils$medium_editor_media$on_picker_click(s,options,editable,type){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(type,"poll")){
return oc.web.utils.medium_editor_media.add_poll(s,options,editable);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(type,"gif")){
return oc.web.utils.medium_editor_media.add_gif(s,editable);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(type,"photo")){
return oc.web.utils.medium_editor_media.add_photo(s,options,editable);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(type,"video")){
return oc.web.utils.medium_editor_media.add_video(s,options,editable);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(type,"attachment")){
return oc.web.utils.medium_editor_media.add_attachment(s,options,editable);
} else {
return null;
}
}
}
}
}
});
oc.web.utils.medium_editor_media.file_dnd_handler = (function oc$web$utils$medium_editor_media$file_dnd_handler(s,options,editor_ext,file){
if(((function (){var target_obj_44313 = file;
var _STAR_runtime_state_STAR__orig_val__44315 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__44316 = oops.state.prepare_state(target_obj_44313,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__44316);

try{var next_obj_44314 = ((oops.core.validate_object_access_dynamically(target_obj_44313,(0),"size",true,true,false))?(target_obj_44313["size"]):null);
return next_obj_44314;
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__44315);
}})() < oc.web.local_settings.file_upload_size)){
if(cljs.core.truth_(new cljs.core.Keyword(null,"attachment-uploading","attachment-uploading",-646342864).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state)))){
var alert_data = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"icon","icon",1679606541),"/img/ML/error_icon.png",new cljs.core.Keyword(null,"action","action",-811238024),"dnd-already-running",new cljs.core.Keyword(null,"title","title",636505583),"Sorry!",new cljs.core.Keyword(null,"message","message",-406056002),"You are already uploading a file, wait until it finishes to add another.",new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"OK",new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),(function (){
return oc.web.components.ui.alert_modal.hide_alert();
})], null);
return oc.web.components.ui.alert_modal.show_alert(alert_data);
} else {
var cmail_state = new cljs.core.Keyword(null,"cmail-state","cmail-state",-747393321).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state));
if(cljs.core.truth_(new cljs.core.Keyword(null,"collapsed","collapsed",-628494523).cljs$core$IFn$_invoke$arity$1(cmail_state))){
oc.web.actions.cmail.cmail_show.cljs$core$IFn$_invoke$arity$variadic(oc.web.actions.cmail.get_board_for_edit(),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"collapsed","collapsed",-628494523),false,new cljs.core.Keyword(null,"fullscreen","fullscreen",-4371054),false,new cljs.core.Keyword(null,"key","key",-1516042587),new cljs.core.Keyword(null,"key","key",-1516042587).cljs$core$IFn$_invoke$arity$1(cmail_state)], null)], 0));
} else {
}

if(cljs.core.truth_(file.type.match("image"))){
cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-picker-ext","oc.web.utils.medium-editor-media/media-picker-ext",805783011).cljs$core$IFn$_invoke$arity$1(s)).hide();

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"attachment-uploading","attachment-uploading",-646342864)], null),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"progress","progress",244323547),"0",new cljs.core.Keyword(null,"comment-parent-uuid","comment-parent-uuid",1514531851),new cljs.core.Keyword(null,"comment-parent-uuid","comment-parent-uuid",1514531851).cljs$core$IFn$_invoke$arity$1(options)], null)], null));

return oc.web.lib.image_upload.upload_file_BANG_.cljs$core$IFn$_invoke$arity$variadic(file,(function (url){
editor_ext.insertImageFile(file,url,null);

return oc.web.lib.utils.after((500),(function (){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"attachment-uploading","attachment-uploading",-646342864)], null),null], null));

oc.web.lib.utils.to_end_of_content_editable(rum.core.ref_node(s,"editor-node"));

return oc.web.lib.utils.after((500),(function (){
return cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-picker-ext","oc.web.utils.medium-editor-media/media-picker-ext",805783011).cljs$core$IFn$_invoke$arity$1(s)).togglePicker();
}));
}));
}),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(function (){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"attachment-uploading","attachment-uploading",-646342864)], null),null], null));

var alert_data = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"icon","icon",1679606541),"/img/ML/error_icon.png",new cljs.core.Keyword(null,"action","action",-811238024),"dnd-image-upload-error",new cljs.core.Keyword(null,"title","title",636505583),"Sorry!",new cljs.core.Keyword(null,"message","message",-406056002),"An error occurred while uploading your file.",new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"OK",new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),(function (){
return oc.web.components.ui.alert_modal.hide_alert();
})], null);
return oc.web.components.ui.alert_modal.show_alert(alert_data);
}),(function (progress_percentage){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"attachment-uploading","attachment-uploading",-646342864)], null),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"progress","progress",244323547),progress_percentage,new cljs.core.Keyword(null,"comment-parent-uuid","comment-parent-uuid",1514531851),new cljs.core.Keyword(null,"comment-parent-uuid","comment-parent-uuid",1514531851).cljs$core$IFn$_invoke$arity$1(options)], null)], null));
})], 0));
} else {
if(cljs.core.truth_(new cljs.core.Keyword(null,"attachments-enabled","attachments-enabled",772816647).cljs$core$IFn$_invoke$arity$1(options))){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"attachment-uploading","attachment-uploading",-646342864)], null),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"progress","progress",244323547),"0",new cljs.core.Keyword(null,"comment-parent-uuid","comment-parent-uuid",1514531851),new cljs.core.Keyword(null,"comment-parent-uuid","comment-parent-uuid",1514531851).cljs$core$IFn$_invoke$arity$1(options)], null)], null));

return oc.web.lib.image_upload.upload_file_BANG_.cljs$core$IFn$_invoke$arity$variadic(file,(function (url){
var size = (function (){var target_obj_44317 = file;
var _STAR_runtime_state_STAR__orig_val__44319 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__44320 = oops.state.prepare_state(target_obj_44317,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__44320);

try{var next_obj_44318 = ((oops.core.validate_object_access_dynamically(target_obj_44317,(0),"size",true,true,false))?(target_obj_44317["size"]):null);
return next_obj_44318;
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__44319);
}})();
var mimetype = (function (){var target_obj_44321 = file;
var _STAR_runtime_state_STAR__orig_val__44323 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__44324 = oops.state.prepare_state(target_obj_44321,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__44324);

try{var next_obj_44322 = ((oops.core.validate_object_access_dynamically(target_obj_44321,(0),"type",true,true,false))?(target_obj_44321["type"]):null);
return next_obj_44322;
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__44323);
}})();
var filename = (function (){var target_obj_44325 = file;
var _STAR_runtime_state_STAR__orig_val__44327 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__44328 = oops.state.prepare_state(target_obj_44325,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__44328);

try{var next_obj_44326 = ((oops.core.validate_object_access_dynamically(target_obj_44325,(0),"name",true,true,false))?(target_obj_44325["name"]):null);
return next_obj_44326;
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__44327);
}})();
var createdat = oc.web.lib.utils.js_date();
var prefix = ["Uploaded by ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"name","name",1843675177)))," on ",oc.web.lib.utils.date_string.cljs$core$IFn$_invoke$arity$variadic(createdat,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"year","year",335913393)], null)], 0))," - "].join('');
var subtitle = [prefix,clojure.contrib.humanize.filesize.cljs$core$IFn$_invoke$arity$variadic(size,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"binary","binary",-1802232288),false,new cljs.core.Keyword(null,"format","format",-1306924766),"%.2f"], 0))].join('');
var icon = oc.web.utils.activity.icon_for_mimetype(mimetype);
var attachment_data = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"file-name","file-name",-1654217259),filename,new cljs.core.Keyword(null,"file-type","file-type",1274948820),mimetype,new cljs.core.Keyword(null,"file-size","file-size",-1900760755),size,new cljs.core.Keyword(null,"file-url","file-url",-863738963),url], null);
var dispatch_input_key = new cljs.core.Keyword(null,"dispatch-input-key","dispatch-input-key",-1224535693).cljs$core$IFn$_invoke$arity$1(options);
oc.web.actions.activity.add_attachment(dispatch_input_key,attachment_data);

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"attachment-uploading","attachment-uploading",-646342864)], null),null], null));
}),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(function (){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"attachment-uploading","attachment-uploading",-646342864)], null),null], null));

var alert_data = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"icon","icon",1679606541),"/img/ML/error_icon.png",new cljs.core.Keyword(null,"action","action",-811238024),"dnd-attachment-upload-error",new cljs.core.Keyword(null,"title","title",636505583),"Sorry!",new cljs.core.Keyword(null,"message","message",-406056002),"An error occurred while uploading your file.",new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"OK",new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),(function (){
return oc.web.components.ui.alert_modal.hide_alert();
})], null);
return oc.web.components.ui.alert_modal.show_alert(alert_data);
}),(function (progress_percentage){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"attachment-uploading","attachment-uploading",-646342864)], null),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"progress","progress",244323547),progress_percentage,new cljs.core.Keyword(null,"comment-parent-uuid","comment-parent-uuid",1514531851),new cljs.core.Keyword(null,"comment-parent-uuid","comment-parent-uuid",1514531851).cljs$core$IFn$_invoke$arity$1(options)], null)], null));
})], 0));
} else {
return null;
}
}
}
} else {
var alert_data = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"icon","icon",1679606541),"/img/ML/error_icon.png",new cljs.core.Keyword(null,"action","action",-811238024),"dnd-file-too-big",new cljs.core.Keyword(null,"title","title",636505583),"Sorry!",new cljs.core.Keyword(null,"message","message",-406056002),"Error, please use files smaller than 20MB.",new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"OK",new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),(function (){
return oc.web.components.ui.alert_modal.hide_alert();
})], null);
return oc.web.components.ui.alert_modal.show_alert(alert_data);
}
});
oc.web.utils.medium_editor_media.setup_editor = (function oc$web$utils$medium_editor_media$setup_editor(s,body_on_change,options){
var users_list = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"mention-users","mention-users",-525519758)));
if(((cljs.core.seq(users_list)) && ((cljs.core.deref(new cljs.core.Keyword("oc.web.utils.medium-editor-media","editor","oc.web.utils.medium-editor-media/editor",1463568801).cljs$core$IFn$_invoke$arity$1(s)) == null)))){
var mobile_editor = oc.web.lib.responsive.is_tablet_or_mobile_QMARK_();
var media_config = new cljs.core.Keyword(null,"media-config","media-config",1588458658).cljs$core$IFn$_invoke$arity$1(options);
var placeholder = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"placeholder","placeholder",-104873083).cljs$core$IFn$_invoke$arity$1(options);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "What would you like to share?";
}
})();
var body_el = rum.core.ref_node(s,"editor-node");
var media_picker_opts = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"buttons","buttons",-1953831197),cljs.core.clj__GT_js(media_config),new cljs.core.Keyword(null,"hidePlaceholderOnExpand","hidePlaceholderOnExpand",-2029764776),false,new cljs.core.Keyword(null,"inlinePlusButtonOptions","inlinePlusButtonOptions",-127332648),({"inlineButtons": new cljs.core.Keyword(null,"use-inline-media-picker","use-inline-media-picker",1605876743).cljs$core$IFn$_invoke$arity$1(options), "staticPositioning": new cljs.core.Keyword(null,"static-positioned-media-picker","static-positioned-media-picker",-888920383).cljs$core$IFn$_invoke$arity$1(options), "mediaPickerContainerSelector": new cljs.core.Keyword(null,"media-picker-container-selector","media-picker-container-selector",1731260615).cljs$core$IFn$_invoke$arity$1(options), "alwaysExpanded": new cljs.core.Keyword(null,"use-inline-media-picker","use-inline-media-picker",1605876743).cljs$core$IFn$_invoke$arity$1(options), "initiallyVisible": new cljs.core.Keyword(null,"media-picker-initially-visible","media-picker-initially-visible",1391698183).cljs$core$IFn$_invoke$arity$1(options), "disableButtons": new cljs.core.Keyword(null,"paywall?","paywall?",-1706178021).cljs$core$IFn$_invoke$arity$1(options)}),new cljs.core.Keyword(null,"delegateMethods","delegateMethods",-1239981282),({"onPickerClick": cljs.core.partial.cljs$core$IFn$_invoke$arity$3(oc.web.utils.medium_editor_media.on_picker_click,s,options)})], null);
var media_picker_ext = ((mobile_editor)?null:(new MediaPicker(cljs.core.clj__GT_js(media_picker_opts))));
var file_dragging_ext = ((mobile_editor)?null:(new CarrotFileDragging(cljs.core.clj__GT_js(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"uploadHandler","uploadHandler",1177955898),cljs.core.partial.cljs$core$IFn$_invoke$arity$3(oc.web.utils.medium_editor_media.file_dnd_handler,s,options)], null)))));
var buttons = new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, ["bold","italic","unorderedlist","anchor","quote","highlighter","h1","h2"], null);
var paste_ext_options = ({"forcePlainText": false, "cleanPastedHTML": true, "cleanAttrs": ["style","alt","dir","size","face","color","itemprop","name","id"], "cleanTags": ["meta","video","audio","img","button","svg","canvas","figure","input","textarea","style","javascript"], "unwrapTags": cljs.core.clj__GT_js(cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,cljs.core.PersistentVector.fromArray(["!doctype","abbr","acronym","address","applet","area","article","aside","base","basefont","bb","bdo","big","body","br","caption","center","cite","col","colgroup","command","datagrid","datalist","dd","del","details","dfn","dialog","dir","div","dl","dt","em","embed","eventsource","fieldset","figcaption","font","footer","form","frame","frameset","h3","h4","h5","h6","head","header","hgroup","hr","html","iframe","ins","isindex","kbd","keygen","label","legend","link","main","map","mark","menu","meter","nav","noframes","noscript","object","ol","optgroup","option","output","p","param","progress","q","rp","rt","ruby","s","samp","script","section","select","small","source","span","strike","strong","sub","summary","sup","table","tbody","td","tfoot","th","thead","time","title","tr","track","tt","u","var","wbr"], true)))});
var extensions = (function (){var G__44329 = new cljs.core.PersistentArrayMap(null, 3, ["autolist",(new AutoList()),"mention",oc.web.utils.mention.mention_ext(users_list),"fileDragging",false], null);
var G__44329__$1 = (((!(mobile_editor)))?cljs.core.assoc.cljs$core$IFn$_invoke$arity$variadic(G__44329,"media-picker",media_picker_ext,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["autoquote",(new AutoQuote()),"autocode",(new AutoCode()),"autoinlinecode",(new AutoInlinecode()),"inlinecode",(new InlineCodeButton()),"highlighter",(new HighlighterButton()),"carrotFileDragging",file_dragging_ext], 0)):G__44329);
return cljs.core.clj__GT_js(G__44329__$1);

})();
var options__$1 = cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"autoLink","autoLink",984791968),new cljs.core.Keyword(null,"imageDragging","imageDragging",-1464910814),new cljs.core.Keyword(null,"placeholder","placeholder",-104873083),new cljs.core.Keyword(null,"disableEditing","disableEditing",-2000439446),new cljs.core.Keyword(null,"paste","paste",1975741548),new cljs.core.Keyword(null,"spellcheck","spellcheck",-508643726),new cljs.core.Keyword(null,"extensions","extensions",-1103629196),new cljs.core.Keyword(null,"targetBlank","targetBlank",-750943627),new cljs.core.Keyword(null,"toolbar","toolbar",-1172789065),new cljs.core.Keyword(null,"keyboardCommands","keyboardCommands",754527288),new cljs.core.Keyword(null,"anchor","anchor",1549638489),new cljs.core.Keyword(null,"buttonLabels","buttonLabels",1777868890),new cljs.core.Keyword(null,"anchorPreview","anchorPreview",-420809858)],[true,false,({"text": placeholder, "hideOnClick": false, "hide-on-click": false}),new cljs.core.Keyword(null,"paywall?","paywall?",-1706178021).cljs$core$IFn$_invoke$arity$1(options),paste_ext_options,false,extensions,true,((mobile_editor)?false:({"buttons": cljs.core.clj__GT_js(buttons), "allowMultiParagraphSelection": false})),({"commands": [({"command": "bold", "key": "B", "meta": true, "shift": false, "alt": false}),({"command": "italic", "key": "I", "meta": true, "shift": false, "alt": false}),({"command": false, "key": "U", "meta": true, "shift": false, "alt": false})]}),({"customClassOption": null, "customClassOptionText": "Button", "linkValidation": true, "placeholderText": "Paste or type a link", "targetCheckbox": false, "targetCheckboxText": "Open in new window"}),"fontawesome",((mobile_editor)?false:({"hideDelay": (500), "previewValueSelector": "a"}))]);
var body_editor = (new module$node_modules$medium_editor$dist$js$medium_editor(body_el,cljs.core.clj__GT_js(options__$1)));
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","media-picker-ext","oc.web.utils.medium-editor-media/media-picker-ext",805783011).cljs$core$IFn$_invoke$arity$1(s),media_picker_ext);

body_editor.subscribe("editableInput",(function (event,editable){
return (body_on_change.cljs$core$IFn$_invoke$arity$1 ? body_on_change.cljs$core$IFn$_invoke$arity$1(s) : body_on_change.call(null,s));
}));

body_editor.subscribe("editableKeydown",(function (e,editable){
if(cljs.core.truth_(((cljs.core.fn_QMARK_(new cljs.core.Keyword(null,"cmd-enter-cb","cmd-enter-cb",-2018742375).cljs$core$IFn$_invoke$arity$1(options__$1)))?(function (){var and__4115__auto__ = e.metaKey;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2("Enter",e.key);
} else {
return and__4115__auto__;
}
})():false))){
var fexpr__44330 = new cljs.core.Keyword(null,"cmd-enter-cb","cmd-enter-cb",-2018742375).cljs$core$IFn$_invoke$arity$1(options__$1);
return (fexpr__44330.cljs$core$IFn$_invoke$arity$1 ? fexpr__44330.cljs$core$IFn$_invoke$arity$1(e) : fexpr__44330.call(null,e));
} else {
return null;
}
}));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.utils.medium-editor-media","editor","oc.web.utils.medium-editor-media/editor",1463568801).cljs$core$IFn$_invoke$arity$1(s),body_editor);

var classes = new cljs.core.Keyword(null,"classes","classes",2037804510).cljs$core$IFn$_invoke$arity$1(options__$1);
if(cljs.core.truth_((function (){var and__4115__auto__ = cuerdas.core.includes_QMARK_(classes,"emoji-autocomplete");
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(new cljs.core.Keyword(null,"paywall?","paywall?",-1706178021).cljs$core$IFn$_invoke$arity$1(options__$1));
} else {
return and__4115__auto__;
}
})())){
return emojiAutocomplete();
} else {
return null;
}
} else {
return null;
}
});

//# sourceMappingURL=oc.web.utils.medium_editor_media.js.map
