goog.provide('oc.web.components.ui.media_video_modal');
oc.web.components.ui.media_video_modal.media_video_modal_height = (153);
oc.web.components.ui.media_video_modal.youtube_regexp = "https?://(?:www\\.|m\\.)*(?:youtube\\.com|youtu\\.be)/watch/?\\?(?:(?:time_continue|t)=\\d+s?&)?v=([a-zA-Z0-9_-]{11}).*";
oc.web.components.ui.media_video_modal.youtube_short_regexp = "https?://(?:www\\.|m\\.)*(?:youtube\\.com|youtu\\.be)/([a-zA-Z0-9_-]{11}/?)";
oc.web.components.ui.media_video_modal.vimeo_regexp = ["(?:http|https)?:\\/\\/(?:www\\.)?vimeo.com\\/","(?:channels\\/(?:\\w+\\/)?|groups\\/(?:[?:^\\/]*)","\\/videos\\/|)(\\d+)(?:|\\/\\?)"].join('');
oc.web.components.ui.media_video_modal.loom_regexp = ["(?:http|https)?:\\/\\/(?:www\\.)?(?:useloom|loom).com\\/share\\/","([a-zA-Z0-9_-]*/?)"].join('');
oc.web.components.ui.media_video_modal.get_video_data = (function oc$web$components$ui$media_video_modal$get_video_data(url){
var yr = RegExp(oc.web.components.ui.media_video_modal.youtube_regexp,"ig");
var yr2 = RegExp(oc.web.components.ui.media_video_modal.youtube_short_regexp,"ig");
var vr = RegExp(oc.web.components.ui.media_video_modal.vimeo_regexp,"ig");
var loomr = RegExp(oc.web.components.ui.media_video_modal.loom_regexp,"ig");
var y_groups = yr.exec(url);
var y2_groups = yr2.exec(url);
var v_groups = vr.exec(url);
var loom_groups = loomr.exec(url);
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"id","id",-1388402092),(cljs.core.truth_(cljs.core.nth.cljs$core$IFn$_invoke$arity$2(y_groups,(1)))?cljs.core.nth.cljs$core$IFn$_invoke$arity$2(y_groups,(1)):(cljs.core.truth_(cljs.core.nth.cljs$core$IFn$_invoke$arity$2(y2_groups,(1)))?cljs.core.nth.cljs$core$IFn$_invoke$arity$2(y2_groups,(1)):(cljs.core.truth_(cljs.core.nth.cljs$core$IFn$_invoke$arity$2(v_groups,(1)))?cljs.core.nth.cljs$core$IFn$_invoke$arity$2(v_groups,(1)):(cljs.core.truth_(cljs.core.nth.cljs$core$IFn$_invoke$arity$2(loom_groups,(1)))?cljs.core.nth.cljs$core$IFn$_invoke$arity$2(loom_groups,(1)):null)))),new cljs.core.Keyword(null,"type","type",1174270348),(cljs.core.truth_((function (){var or__4126__auto__ = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(y_groups,(1));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.nth.cljs$core$IFn$_invoke$arity$2(y2_groups,(1));
}
})())?new cljs.core.Keyword(null,"youtube","youtube",-1932361085):(cljs.core.truth_(cljs.core.nth.cljs$core$IFn$_invoke$arity$2(v_groups,(1)))?new cljs.core.Keyword(null,"vimeo","vimeo",-1217463574):(cljs.core.truth_(cljs.core.nth.cljs$core$IFn$_invoke$arity$2(loom_groups,(1)))?new cljs.core.Keyword(null,"loom","loom",1998784953):null)))], null);
});
oc.web.components.ui.media_video_modal.get_vimeo_thumbnail_success = (function oc$web$components$ui$media_video_modal$get_vimeo_thumbnail_success(s,video,res){
var resp = (res[(0)]);
var thumbnail = (resp["thumbnail_medium"]);
var video_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(video,new cljs.core.Keyword(null,"thumbnail","thumbnail",-867906798),thumbnail);
var dismiss_modal_cb = new cljs.core.Keyword(null,"dismiss-cb","dismiss-cb",-1282537857).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s)));
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"media-input","media-input",107658136),new cljs.core.Keyword(null,"media-video","media-video",339369994)], null),video_data], null));

return (dismiss_modal_cb.cljs$core$IFn$_invoke$arity$0 ? dismiss_modal_cb.cljs$core$IFn$_invoke$arity$0() : dismiss_modal_cb.call(null));
});
oc.web.components.ui.media_video_modal._retry = cljs.core.atom.cljs$core$IFn$_invoke$arity$1((0));
oc.web.components.ui.media_video_modal.get_vimeo_thumbnail_retry = (function oc$web$components$ui$media_video_modal$get_vimeo_thumbnail_retry(s,video){
if((cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(oc.web.components.ui.media_video_modal._retry,cljs.core.inc) < (3))){
return (oc.web.components.ui.media_video_modal.get_vimeo_thumbnail.cljs$core$IFn$_invoke$arity$2 ? oc.web.components.ui.media_video_modal.get_vimeo_thumbnail.cljs$core$IFn$_invoke$arity$2(s,video) : oc.web.components.ui.media_video_modal.get_vimeo_thumbnail.call(null,s,video));
} else {
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"media-input","media-input",107658136),new cljs.core.Keyword(null,"media-video","media-video",339369994)], null),video], null));

var dismiss_modal_cb = new cljs.core.Keyword(null,"dismiss-cb","dismiss-cb",-1282537857).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s)));
return (dismiss_modal_cb.cljs$core$IFn$_invoke$arity$0 ? dismiss_modal_cb.cljs$core$IFn$_invoke$arity$0() : dismiss_modal_cb.call(null));
}
});
oc.web.components.ui.media_video_modal.get_vimeo_thumbnail = (function oc$web$components$ui$media_video_modal$get_vimeo_thumbnail(s,video){
return $.ajax(({"method": "GET", "url": ["https://vimeo.com/api/v2/video/",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(video)),".json"].join(''), "success": (function (p1__45793_SHARP_){
return oc.web.components.ui.media_video_modal.get_vimeo_thumbnail_success(s,video,p1__45793_SHARP_);
}), "error": (function (){
return oc.web.components.ui.media_video_modal.get_vimeo_thumbnail_retry(s,video);
})}));
});
oc.web.components.ui.media_video_modal.valid_video_url_QMARK_ = (function oc$web$components$ui$media_video_modal$valid_video_url_QMARK_(url){
var trimmed_url = cuerdas.core.trim.cljs$core$IFn$_invoke$arity$1(url);
var yr = RegExp(oc.web.components.ui.media_video_modal.youtube_regexp,"ig");
var yr2 = RegExp(oc.web.components.ui.media_video_modal.youtube_short_regexp,"ig");
var vr = RegExp(oc.web.components.ui.media_video_modal.vimeo_regexp,"ig");
var loomr = RegExp(oc.web.components.ui.media_video_modal.loom_regexp,"ig");
if(cljs.core.seq(trimmed_url)){
var or__4126__auto__ = trimmed_url.match(yr);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = trimmed_url.match(yr2);
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
var or__4126__auto____$2 = trimmed_url.match(vr);
if(cljs.core.truth_(or__4126__auto____$2)){
return or__4126__auto____$2;
} else {
return trimmed_url.match(loomr);
}
}
}
} else {
return null;
}
});
oc.web.components.ui.media_video_modal.video_add_click = (function oc$web$components$ui$media_video_modal$video_add_click(s){
var video_data = oc.web.components.ui.media_video_modal.get_video_data(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.media-video-modal","video-url","oc.web.components.ui.media-video-modal/video-url",1364440848).cljs$core$IFn$_invoke$arity$1(s)));
var dismiss_modal_cb = new cljs.core.Keyword(null,"dismiss-cb","dismiss-cb",-1282537857).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s)));
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"vimeo","vimeo",-1217463574),new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(video_data))){
return oc.web.components.ui.media_video_modal.get_vimeo_thumbnail(s,video_data);
} else {
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"media-input","media-input",107658136),new cljs.core.Keyword(null,"media-video","media-video",339369994)], null),video_data], null));

return (dismiss_modal_cb.cljs$core$IFn$_invoke$arity$0 ? dismiss_modal_cb.cljs$core$IFn$_invoke$arity$0() : dismiss_modal_cb.call(null));
}
});
oc.web.components.ui.media_video_modal.media_video_modal = rum.core.build_defcs((function (s,p__45795){
var map__45796 = p__45795;
var map__45796__$1 = (((((!((map__45796 == null))))?(((((map__45796.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45796.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45796):map__45796);
var fullscreen = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45796__$1,new cljs.core.Keyword(null,"fullscreen","fullscreen",-4371054));
var dismiss_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45796__$1,new cljs.core.Keyword(null,"dismiss-cb","dismiss-cb",-1282537857));
var outer_container_selector = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45796__$1,new cljs.core.Keyword(null,"outer-container-selector","outer-container-selector",1406024934));
var offset_element_selector = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45796__$1,new cljs.core.Keyword(null,"offset-element-selector","offset-element-selector",-64578356));
var current_user_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915));
var valid_url = oc.web.components.ui.media_video_modal.valid_video_url_QMARK_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.media-video-modal","video-url","oc.web.components.ui.media-video-modal/video-url",1364440848).cljs$core$IFn$_invoke$arity$1(s)));
var scrolling_element = (cljs.core.truth_(fullscreen)?document.querySelector(dommy.core.selector(outer_container_selector)):document.scrollingElement);
var win_height = (function (){var or__4126__auto__ = document.documentElement.clientHeight;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return window.innerHeight;
}
})();
var top_offset_limit = document.querySelector(dommy.core.selector(offset_element_selector)).offsetTop;
var scroll_top = scrolling_element.scrollTop;
var top_position = (function (){var x__4214__auto__ = (0);
var y__4215__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.media-video-modal","offset-top","oc.web.components.ui.media-video-modal/offset-top",1422606869).cljs$core$IFn$_invoke$arity$1(s));
return ((x__4214__auto__ > y__4215__auto__) ? x__4214__auto__ : y__4215__auto__);
})();
var relative_position = (((top_position + top_offset_limit) + (scroll_top * (-1))) + oc.web.components.ui.media_video_modal.media_video_modal_height);
var adjusted_position = (((relative_position > win_height))?(function (){var x__4214__auto__ = (0);
var y__4215__auto__ = ((top_position - (relative_position - win_height)) - (16));
return ((x__4214__auto__ > y__4215__auto__) ? x__4214__auto__ : y__4215__auto__);
})():top_position);
return React.createElement("div",({"style": ({"top": [cljs.core.str.cljs$core$IFn$_invoke$arity$1(adjusted_position),"px"].join('')}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["media-video-modal-container",(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.media-video-modal","video-url-focused","oc.web.components.ui.media-video-modal/video-url-focused",421024018).cljs$core$IFn$_invoke$arity$1(s)))?"video-url-focused":null)], null))}),React.createElement("div",({"className": "media-video-modal-title"}),"Embed video"),sablono.interpreter.create_element("input",({"placeholder": "Paste the video link\u2026", "ref": "video-input", "value": cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.media-video-modal","video-url","oc.web.components.ui.media-video-modal/video-url",1364440848).cljs$core$IFn$_invoke$arity$1(s)), "type": "text", "onBlur": (function (){
if(clojure.string.blank_QMARK_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.media-video-modal","video-url","oc.web.components.ui.media-video-modal/video-url",1364440848).cljs$core$IFn$_invoke$arity$1(s)))){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.media-video-modal","video-url-focused","oc.web.components.ui.media-video-modal/video-url-focused",421024018).cljs$core$IFn$_invoke$arity$1(s),false);
} else {
return null;
}
}), "className": "media-video-modal-input oc-input", "onKeyPress": (function (e){
if(cljs.core.truth_((function (){var and__4115__auto__ = valid_url;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(e.key,"Enter");
} else {
return and__4115__auto__;
}
})())){
return oc.web.components.ui.media_video_modal.video_add_click(s);
} else {
return null;
}
}), "onChange": (function (p1__45794_SHARP_){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.media-video-modal","video-url","oc.web.components.ui.media-video-modal/video-url",1364440848).cljs$core$IFn$_invoke$arity$1(s),p1__45794_SHARP_.target.value);
}), "onFocus": (function (){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.media-video-modal","video-url-focused","oc.web.components.ui.media-video-modal/video-url-focused",421024018).cljs$core$IFn$_invoke$arity$1(s),true);
})})),React.createElement("button",({"onClick": (function (){
if(cljs.core.truth_(valid_url)){
return oc.web.components.ui.media_video_modal.video_add_click(s);
} else {
return null;
}
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["mlb-reset","embed-video-bt",(cljs.core.truth_(valid_url)?null:"disabled")], null))}),"Embed video"),React.createElement("div",({"className": "media-video-description"}),"Works with Loom, Youtube, and Vimeo"));
}),new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.media-video-modal","dismiss","oc.web.components.ui.media-video-modal/dismiss",1774594053)),rum.core.local.cljs$core$IFn$_invoke$arity$2("",new cljs.core.Keyword("oc.web.components.ui.media-video-modal","video-url","oc.web.components.ui.media-video-modal/video-url",1364440848)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.media-video-modal","video-url-focused","oc.web.components.ui.media-video-modal/video-url-focused",421024018)),rum.core.local.cljs$core$IFn$_invoke$arity$2((0),new cljs.core.Keyword("oc.web.components.ui.media-video-modal","offset-top","oc.web.components.ui.media-video-modal/offset-top",1422606869)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915)], 0)),oc.web.mixins.ui.first_render_mixin,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
var outer_container_selector_45831 = new cljs.core.Keyword(null,"outer-container-selector","outer-container-selector",1406024934).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s)));
var temp__5735__auto___45832 = document.querySelector(dommy.core.selector(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(outer_container_selector_45831,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.medium-editor-media-picker","div.medium-editor-media-picker",1293519554)], null))));
if(cljs.core.truth_(temp__5735__auto___45832)){
var picker_el_45833 = temp__5735__auto___45832;
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.media-video-modal","offset-top","oc.web.components.ui.media-video-modal/offset-top",1422606869).cljs$core$IFn$_invoke$arity$1(s),picker_el_45833.offsetTop);
} else {
}

return s;
}),new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
var temp__5735__auto___45834 = rum.core.ref_node(s,"video-input");
if(cljs.core.truth_(temp__5735__auto___45834)){
var video_field_45835 = temp__5735__auto___45834;
video_field_45835.focus();
} else {
}

return s;
})], null)], null),"media-video-modal");

//# sourceMappingURL=oc.web.components.ui.media_video_modal.js.map
