goog.provide('oc.web.components.stream_item');
var module$node_modules$hammerjs$hammer=shadow.js.require("module$node_modules$hammerjs$hammer", {});
oc.web.components.stream_item.stream_item_summary = (function oc$web$components$stream_item$stream_item_summary(activity_data){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.stream-item-body.oc-mentions","div.stream-item-body.oc-mentions",-357013753),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"data-itemuuid","data-itemuuid",1896878903),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data),new cljs.core.Keyword(null,"ref","ref",1289896967),new cljs.core.Keyword(null,"item-body","item-body",218561373),new cljs.core.Keyword(null,"dangerouslySetInnerHTML","dangerouslySetInnerHTML",-554971138),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"__html","__html",674048345),new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(activity_data)], null)], null)], null);
});
oc.web.components.stream_item.stream_item_activity_preview = (function oc$web$components$stream_item$stream_item_activity_preview(is_mobile_QMARK_,for_you_context){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.stream-item-activity-preview","div.stream-item-activity-preview",-675723168),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.for-you-body-label","span.for-you-body-label",738724464),new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(for_you_context)], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.separator-dot","div.separator-dot",2056473245)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.time-since","span.time-since",139629259),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687),(cljs.core.truth_(is_mobile_QMARK_)?null:"tooltip"),new cljs.core.Keyword(null,"data-placement","data-placement",166529525),"top",new cljs.core.Keyword(null,"data-container","data-container",1473653353),"body",new cljs.core.Keyword(null,"data-delay","data-delay",1974747786),"{\"show\":\"1000\", \"hide\":\"0\"}",new cljs.core.Keyword(null,"data-title","data-title",-83549535),oc.web.lib.utils.activity_date_tooltip(new cljs.core.Keyword(null,"timestamp","timestamp",579478971).cljs$core$IFn$_invoke$arity$1(for_you_context))], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"time","time",1385887882),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"date-time","date-time",177938180),new cljs.core.Keyword(null,"timestamp","timestamp",579478971).cljs$core$IFn$_invoke$arity$1(for_you_context)], null),oc.web.lib.utils.time_since.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"timestamp","timestamp",579478971).cljs$core$IFn$_invoke$arity$1(for_you_context),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"short","short",1928760516),new cljs.core.Keyword(null,"lower-case","lower-case",-212358583)], null)], 0))], null)], null)], null);
});
oc.web.components.stream_item.win_width = (function oc$web$components$stream_item$win_width(){
var or__4126__auto__ = document.documentElement.clientWidth;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return window.innerWidth;
}
});
oc.web.components.stream_item.calc_video_height = (function oc$web$components$stream_item$calc_video_height(s){
if(oc.web.lib.responsive.is_tablet_or_mobile_QMARK_()){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.stream-item","mobile-video-height","oc.web.components.stream-item/mobile-video-height",1317485109).cljs$core$IFn$_invoke$arity$1(s),oc.web.lib.utils.calc_video_height(oc.web.components.stream_item.win_width()));
} else {
return null;
}
});
oc.web.components.stream_item.show_mobile_menu = (function oc$web$components$stream_item$show_mobile_menu(s){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.stream-item","force-show-menu","oc.web.components.stream-item/force-show-menu",1207906181).cljs$core$IFn$_invoke$arity$1(s),true);
});
oc.web.components.stream_item.show_swipe_button = (function oc$web$components$stream_item$show_swipe_button(s,ref_kw){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"mobile-swipe-menu","mobile-swipe-menu",897943653)], null),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"activity-data","activity-data",1293689136).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s))))], null));

if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(ref_kw,new cljs.core.Keyword("oc.web.components.stream-item","show-mobile-dismiss-bt","oc.web.components.stream-item/show-mobile-dismiss-bt",-2020556129))){
cljs.core.compare_and_set_BANG_(new cljs.core.Keyword("oc.web.components.stream-item","show-mobile-more-bt","oc.web.components.stream-item/show-mobile-more-bt",5884493).cljs$core$IFn$_invoke$arity$1(s),true,false);

return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("oc.web.components.stream-item","show-mobile-dismiss-bt","oc.web.components.stream-item/show-mobile-dismiss-bt",-2020556129).cljs$core$IFn$_invoke$arity$1(s),cljs.core.not);
} else {
cljs.core.compare_and_set_BANG_(new cljs.core.Keyword("oc.web.components.stream-item","show-mobile-dismiss-bt","oc.web.components.stream-item/show-mobile-dismiss-bt",-2020556129).cljs$core$IFn$_invoke$arity$1(s),true,false);

return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("oc.web.components.stream-item","show-mobile-more-bt","oc.web.components.stream-item/show-mobile-more-bt",5884493).cljs$core$IFn$_invoke$arity$1(s),cljs.core.not);
}
});
oc.web.components.stream_item.dismiss_swipe_button = (function oc$web$components$stream_item$dismiss_swipe_button(var_args){
var args__4742__auto__ = [];
var len__4736__auto___46436 = arguments.length;
var i__4737__auto___46437 = (0);
while(true){
if((i__4737__auto___46437 < len__4736__auto___46436)){
args__4742__auto__.push((arguments[i__4737__auto___46437]));

var G__46438 = (i__4737__auto___46437 + (1));
i__4737__auto___46437 = G__46438;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.components.stream_item.dismiss_swipe_button.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.components.stream_item.dismiss_swipe_button.cljs$core$IFn$_invoke$arity$variadic = (function (s,p__46382){
var vec__46383 = p__46382;
var e = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__46383,(0),null);
var ref_kw = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__46383,(1),null);
if(cljs.core.truth_(e)){
oc.web.lib.utils.event_stop(e);
} else {
}

if(((cljs.core.not(ref_kw)) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(ref_kw,new cljs.core.Keyword("oc.web.components.stream-item","show-mobile-more-bt","oc.web.components.stream-item/show-mobile-more-bt",5884493))))){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.stream-item","show-mobile-more-bt","oc.web.components.stream-item/show-mobile-more-bt",5884493).cljs$core$IFn$_invoke$arity$1(s),false);
} else {
}

if(((cljs.core.not(ref_kw)) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(ref_kw,new cljs.core.Keyword("oc.web.components.stream-item","show-mobile-dismiss-bt","oc.web.components.stream-item/show-mobile-dismiss-bt",-2020556129))))){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.stream-item","show-mobile-dismiss-bt","oc.web.components.stream-item/show-mobile-dismiss-bt",-2020556129).cljs$core$IFn$_invoke$arity$1(s),false);
} else {
}

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.stream-item","last-mobile-swipe-menu","oc.web.components.stream-item/last-mobile-swipe-menu",-1097306954).cljs$core$IFn$_invoke$arity$1(s),null);

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"mobile-swipe-menu","mobile-swipe-menu",897943653)], null),null], null));
}));

(oc.web.components.stream_item.dismiss_swipe_button.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.components.stream_item.dismiss_swipe_button.cljs$lang$applyTo = (function (seq46375){
var G__46376 = cljs.core.first(seq46375);
var seq46375__$1 = cljs.core.next(seq46375);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__46376,seq46375__$1);
}));

oc.web.components.stream_item.swipe_left_handler = (function oc$web$components$stream_item$swipe_left_handler(s,_){
return oc.web.components.stream_item.show_swipe_button(s,new cljs.core.Keyword("oc.web.components.stream-item","show-mobile-dismiss-bt","oc.web.components.stream-item/show-mobile-dismiss-bt",-2020556129));
});
oc.web.components.stream_item.swipe_right_handler = (function oc$web$components$stream_item$swipe_right_handler(s,_){
return oc.web.components.stream_item.show_swipe_button(s,new cljs.core.Keyword("oc.web.components.stream-item","show-mobile-more-bt","oc.web.components.stream-item/show-mobile-more-bt",5884493));
});
oc.web.components.stream_item.long_press_handler = (function oc$web$components$stream_item$long_press_handler(s,_){
oc.web.components.stream_item.dismiss_swipe_button(s);

return oc.web.lib.utils.after((180),(function (){
return oc.web.components.stream_item.show_mobile_menu(s);
}));
});
oc.web.components.stream_item.swipe_gesture_manager = (function oc$web$components$stream_item$swipe_gesture_manager(p__46388){
var map__46389 = p__46388;
var map__46389__$1 = (((((!((map__46389 == null))))?(((((map__46389.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46389.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46389):map__46389);
var options = map__46389__$1;
var swipe_left = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46389__$1,new cljs.core.Keyword(null,"swipe-left","swipe-left",865941635));
var swipe_right = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46389__$1,new cljs.core.Keyword(null,"swipe-right","swipe-right",1701568715));
var long_press = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46389__$1,new cljs.core.Keyword(null,"long-press","long-press",-521084018));
var disabled = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46389__$1,new cljs.core.Keyword(null,"disabled","disabled",-1529784218));
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
if(((cljs.core.fn_QMARK_(disabled)) && (cljs.core.not((disabled.cljs$core$IFn$_invoke$arity$1 ? disabled.cljs$core$IFn$_invoke$arity$1(s) : disabled.call(null,s)))))){
var el_46439 = rum.core.dom_node(s);
var hr_46440 = (new module$node_modules$hammerjs$hammer(el_46439));
var current_board_slug_46441 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"board-slug","board-slug",99003663)));
if(((cljs.core.fn_QMARK_(swipe_left)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(current_board_slug_46441,"inbox")))){
hr_46440.on("swipeleft",cljs.core.partial.cljs$core$IFn$_invoke$arity$2(swipe_left,s));
} else {
}

if(cljs.core.fn_QMARK_(swipe_right)){
hr_46440.on("swiperight",cljs.core.partial.cljs$core$IFn$_invoke$arity$2(swipe_right,s));
} else {
}

if(cljs.core.fn_QMARK_(long_press)){
hr_46440.on("press",cljs.core.partial.cljs$core$IFn$_invoke$arity$2(long_press,s));
} else {
}

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.stream-item","hammer-recognizer","oc.web.components.stream-item/hammer-recognizer",-673165734).cljs$core$IFn$_invoke$arity$1(s),hr_46440);
} else {
}

return s;
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-item","hammer-recognizer","oc.web.components.stream-item/hammer-recognizer",-673165734).cljs$core$IFn$_invoke$arity$1(s)))){
cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-item","hammer-recognizer","oc.web.components.stream-item/hammer-recognizer",-673165734).cljs$core$IFn$_invoke$arity$1(s)).remove("swipeleft");

cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-item","hammer-recognizer","oc.web.components.stream-item/hammer-recognizer",-673165734).cljs$core$IFn$_invoke$arity$1(s)).remove("swiperight");

cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-item","hammer-recognizer","oc.web.components.stream-item/hammer-recognizer",-673165734).cljs$core$IFn$_invoke$arity$1(s)).remove("pressup");

cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-item","hammer-recognizer","oc.web.components.stream-item/hammer-recognizer",-673165734).cljs$core$IFn$_invoke$arity$1(s)).destroy();
} else {
}

return s;
})], null);
});
oc.web.components.stream_item.on_scroll = (function oc$web$components$stream_item$on_scroll(s){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.stream-item","show-mobile-dismiss-bt","oc.web.components.stream-item/show-mobile-dismiss-bt",-2020556129).cljs$core$IFn$_invoke$arity$1(s),false);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.stream-item","show-mobile-more-bt","oc.web.components.stream-item/show-mobile-more-bt",5884493).cljs$core$IFn$_invoke$arity$1(s),false);
});
oc.web.components.stream_item.stream_item = rum.core.build_defcs((function (s,p__46401){
var map__46402 = p__46401;
var map__46402__$1 = (((((!((map__46402 == null))))?(((((map__46402.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46402.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46402):map__46402);
var replies_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46402__$1,new cljs.core.Keyword(null,"replies?","replies?",1753919327));
var show_wrt_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46402__$1,new cljs.core.Keyword(null,"show-wrt?","show-wrt?",-1492163707));
var current_user_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46402__$1,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915));
var boards_count = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46402__$1,new cljs.core.Keyword(null,"boards-count","boards-count",1873110277));
var member_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46402__$1,new cljs.core.Keyword(null,"member?","member?",486668360));
var show_new_comments_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46402__$1,new cljs.core.Keyword(null,"show-new-comments?","show-new-comments?",-832920630));
var container_slug = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46402__$1,new cljs.core.Keyword(null,"container-slug","container-slug",365736492));
var activity_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46402__$1,new cljs.core.Keyword(null,"activity-data","activity-data",1293689136));
var editable_boards = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46402__$1,new cljs.core.Keyword(null,"editable-boards","editable-boards",1897056658));
var foc_board = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46402__$1,new cljs.core.Keyword(null,"foc-board","foc-board",-1668164909));
var read_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46402__$1,new cljs.core.Keyword(null,"read-data","read-data",-715156010));
var is_mobile_QMARK_ = oc.web.lib.responsive.is_mobile_size_QMARK_();
var current_user_id = new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(current_user_data);
var activity_attachments = new cljs.core.Keyword(null,"attachments","attachments",-1535547830).cljs$core$IFn$_invoke$arity$1(activity_data);
var current_board_slug = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"board-slug","board-slug",99003663));
var current_activity_id = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"activity-uuid","activity-uuid",-663317778));
var dom_element_id = ["stream-item-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data))].join('');
var is_published_QMARK_ = oc.web.utils.activity.is_published_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([activity_data], 0));
var publisher = (cljs.core.truth_(is_published_QMARK_)?new cljs.core.Keyword(null,"publisher","publisher",-153364540).cljs$core$IFn$_invoke$arity$1(activity_data):cljs.core.first(new cljs.core.Keyword(null,"author","author",2111686192).cljs$core$IFn$_invoke$arity$1(activity_data)));
var dom_node_class = ["stream-item-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data))].join('');
var has_video = cljs.core.seq(new cljs.core.Keyword(null,"fixed-video-id","fixed-video-id",-1380335259).cljs$core$IFn$_invoke$arity$1(activity_data));
var uploading_video = oc.web.dispatcher.uploading_video_data.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"video-id","video-id",2132630536).cljs$core$IFn$_invoke$arity$1(activity_data));
var video_player_show = (function (){var and__4115__auto__ = new cljs.core.Keyword(null,"publisher?","publisher?",30448149).cljs$core$IFn$_invoke$arity$1(activity_data);
if(cljs.core.truth_(and__4115__auto__)){
return uploading_video;
} else {
return and__4115__auto__;
}
})();
var video_size = ((has_video)?(cljs.core.truth_(is_mobile_QMARK_)?new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"width","width",-384071477),oc.web.components.stream_item.win_width(),new cljs.core.Keyword(null,"height","height",1025178622),cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-item","mobile-video-height","oc.web.components.stream-item/mobile-video-height",1317485109).cljs$core$IFn$_invoke$arity$1(s))], null):new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"width","width",-384071477),(136),new cljs.core.Keyword(null,"height","height",1025178622),oc.web.lib.utils.calc_video_height((136))], null)):null);
var mobile_more_menu_el = document.querySelector("div.mobile-more-menu");
var show_mobile_menu_QMARK_ = (function (){var and__4115__auto__ = is_mobile_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return mobile_more_menu_el;
} else {
return and__4115__auto__;
}
})();
var more_menu_comp = (function (){
var G__46404 = cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"show-delete?","show-delete?",-753527136),new cljs.core.Keyword(null,"show-edit?","show-edit?",-1476204765),new cljs.core.Keyword(null,"external-share","external-share",-2131927863),new cljs.core.Keyword(null,"will-close","will-close",1889428842),new cljs.core.Keyword(null,"share-container-id","share-container-id",775325900),new cljs.core.Keyword(null,"entity-data","entity-data",1608056141),new cljs.core.Keyword(null,"editable-boards","editable-boards",1897056658),new cljs.core.Keyword(null,"show-move?","show-move?",274288117),new cljs.core.Keyword(null,"external-follow","external-follow",158310616),new cljs.core.Keyword(null,"force-show-menu","force-show-menu",1246398041),new cljs.core.Keyword(null,"external-bookmark","external-bookmark",-1631278980),new cljs.core.Keyword(null,"mobile-tray-menu","mobile-tray-menu",-1905495105)],[true,true,cljs.core.not(is_mobile_QMARK_),(function (){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.stream-item","force-show-menu","oc.web.components.stream-item/force-show-menu",1207906181).cljs$core$IFn$_invoke$arity$1(s),false);
}),dom_element_id,activity_data,editable_boards,cljs.core.not(is_mobile_QMARK_),cljs.core.not(is_mobile_QMARK_),cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-item","force-show-menu","oc.web.components.stream-item/force-show-menu",1207906181).cljs$core$IFn$_invoke$arity$1(s)),cljs.core.not(is_mobile_QMARK_),show_mobile_menu_QMARK_]);
return (oc.web.components.ui.more_menu.more_menu.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.more_menu.more_menu.cljs$core$IFn$_invoke$arity$1(G__46404) : oc.web.components.ui.more_menu.more_menu.call(null,G__46404));
});
var mobile_swipe_menu_uuid = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"mobile-swipe-menu","mobile-swipe-menu",897943653));
var is_home_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(container_slug),new cljs.core.Keyword(null,"following","following",-2049193617));
var show_new_item_tag = ((is_home_QMARK_)?(function (){var and__4115__auto__ = new cljs.core.Keyword(null,"unseen","unseen",1063275592).cljs$core$IFn$_invoke$arity$1(activity_data);
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(new cljs.core.Keyword(null,"publisher?","publisher?",30448149).cljs$core$IFn$_invoke$arity$1(activity_data));
} else {
return and__4115__auto__;
}
})():false);
var show_body_thumbnail_QMARK_ = ((cljs.core.not(replies_QMARK_))?new cljs.core.Keyword(null,"body-thumbnail","body-thumbnail",1858934017).cljs$core$IFn$_invoke$arity$1(activity_data):false);
return React.createElement("div",({"data-last-activity-at": new cljs.core.Keyword("oc.web.components.stream-item","last-activity-at","oc.web.components.stream-item/last-activity-at",847229210).cljs$core$IFn$_invoke$arity$1(activity_data), "data-last-read-at": new cljs.core.Keyword(null,"last-read-at","last-read-at",-216601930).cljs$core$IFn$_invoke$arity$1(activity_data), "onClick": (function (e){
if((!((mobile_swipe_menu_uuid == null)))){
return oc.web.components.stream_item.dismiss_swipe_button.cljs$core$IFn$_invoke$arity$variadic(s,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([e], 0));
} else {
if(cljs.core.not(is_published_QMARK_)){
return oc.web.actions.activity.activity_edit.cljs$core$IFn$_invoke$arity$1(activity_data);
} else {
var more_menu_el = $(["#",dom_element_id," div.more-menu"].join('')).get((0));
var comments_summary_el = $(["#",dom_element_id," div.is-comments"].join('')).get((0));
var stream_item_wrt_el = rum.core.ref_node(s,new cljs.core.Keyword(null,"stream-item-wrt","stream-item-wrt",-508275659));
var emoji_picker = $(["#",dom_element_id," div.emoji-mart"].join('')).get((0));
var attachments_el = rum.core.ref_node(s,new cljs.core.Keyword(null,"stream-item-attachments","stream-item-attachments",337586814));
if(((cljs.core.not(oc.web.lib.utils.event_inside_QMARK_(e,more_menu_el))) && (cljs.core.not(oc.web.lib.utils.event_inside_QMARK_(e,comments_summary_el))) && (cljs.core.not(oc.web.lib.utils.event_inside_QMARK_(e,stream_item_wrt_el))) && (cljs.core.not(oc.web.lib.utils.event_inside_QMARK_(e,attachments_el))) && (cljs.core.not(oc.web.lib.utils.event_inside_QMARK_(e,emoji_picker))) && (cljs.core.not(oc.web.lib.utils.button_clicked_QMARK_(e))) && (cljs.core.not(oc.web.lib.utils.input_clicked_QMARK_(e))) && (cljs.core.not(oc.web.lib.utils.anchor_clicked_QMARK_(e))))){
oc.web.actions.nux.dismiss_post_added_tooltip();

return oc.web.actions.nav_sidebar.open_post_modal.cljs$core$IFn$_invoke$arity$2(activity_data,false);
} else {
return null;
}
}
}
}), "id": dom_element_id, "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["stream-item",oc.web.lib.utils.class_set(cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"showing-share","showing-share",931530336),new cljs.core.Keyword(null,"show-mobile-more-bt","show-mobile-more-bt",47600321),new cljs.core.Keyword(null,"show-mobile-dismiss-bt","show-mobile-dismiss-bt",-1849280509),new cljs.core.Keyword(null,"draft","draft",1421831058),new cljs.core.Keyword(null,"bookmark-item","bookmark-item",1286116436),new cljs.core.Keyword(null,"expandable","expandable",-704609097),dom_node_class,new cljs.core.Keyword(null,"unseen-item","unseen-item",-2083706536),new cljs.core.Keyword(null,"muted-item","muted-item",-1413795329)],[cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"activity-share-container","activity-share-container",-1384168706)),dom_element_id),true,true,cljs.core.not(is_published_QMARK_),new cljs.core.Keyword(null,"bookmarked-at","bookmarked-at",1451784060).cljs$core$IFn$_invoke$arity$1(activity_data),is_published_QMARK_,true,new cljs.core.Keyword(null,"unseen","unseen",1063275592).cljs$core$IFn$_invoke$arity$1(activity_data),oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(activity_data),"follow"], 0))]))], null))}),React.createElement("button",({"onClick": (function (e){
oc.web.components.stream_item.dismiss_swipe_button.cljs$core$IFn$_invoke$arity$variadic(s,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([e,new cljs.core.Keyword("oc.web.components.stream-item","show-mobile-more-bt","oc.web.components.stream-item/show-mobile-more-bt",5884493)], 0));

return oc.web.components.stream_item.show_mobile_menu(s);
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["mlb-reset","mobile-more-bt",(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-item","show-mobile-more-bt","oc.web.components.stream-item/show-mobile-more-bt",5884493).cljs$core$IFn$_invoke$arity$1(s)))?"visible":null)], null))}),React.createElement("span",null,"More")),React.createElement("button",({"onClick": (function (e){
oc.web.components.stream_item.dismiss_swipe_button.cljs$core$IFn$_invoke$arity$variadic(s,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([e,new cljs.core.Keyword("oc.web.components.stream-item","show-mobile-dismiss-bt","oc.web.components.stream-item/show-mobile-dismiss-bt",-2020556129)], 0));

return oc.web.actions.activity.inbox_dismiss(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["mlb-reset","mobile-dismiss-bt",(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-item","show-mobile-dismiss-bt","oc.web.components.stream-item/show-mobile-dismiss-bt",-2020556129).cljs$core$IFn$_invoke$arity$1(s)))?"visible":null)], null))}),React.createElement("span",null,"Dismiss")),React.createElement("div",({"className": "stream-item-header group"}),(function (){var attrs46409 = (function (){var G__46426 = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"activity-data","activity-data",1293689136),activity_data,new cljs.core.Keyword(null,"user-avatar?","user-avatar?",-1162947881),true,new cljs.core.Keyword(null,"user-hover?","user-hover?",-1460761243),true,new cljs.core.Keyword(null,"board-hover?","board-hover?",1064995646),true,new cljs.core.Keyword(null,"activity-board?","activity-board?",-1568829907),((cljs.core.not(new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803).cljs$core$IFn$_invoke$arity$1(activity_data))) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(activity_data),current_board_slug)) && ((boards_count > (1)))),new cljs.core.Keyword(null,"current-user-id","current-user-id",-2013633451),current_user_id], null);
return (oc.web.components.ui.post_authorship.post_authorship.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.post_authorship.post_authorship.cljs$core$IFn$_invoke$arity$1(G__46426) : oc.web.components.ui.post_authorship.post_authorship.call(null,G__46426));
})();
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46409))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["stream-header-head-author"], null)], null),attrs46409], 0))):({"className": "stream-header-head-author"})),((cljs.core.map_QMARK_(attrs46409))?new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [React.createElement("div",({"className": "separator-dot"})),(function (){var t = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"published-at","published-at",249684621).cljs$core$IFn$_invoke$arity$1(activity_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"created-at","created-at",-89248644).cljs$core$IFn$_invoke$arity$1(activity_data);
}
})();
return React.createElement("span",({"data-toggle": (cljs.core.truth_(is_mobile_QMARK_)?null:"tooltip"), "data-placement": "top", "data-container": "body", "data-delay": "{\"show\":\"1000\", \"hide\":\"0\"}", "data-title": oc.web.lib.utils.activity_date_tooltip(activity_data), "className": "time-since"}),React.createElement("time",({"dateTime": t}),sablono.interpreter.interpret(oc.web.lib.utils.time_since.cljs$core$IFn$_invoke$arity$variadic(t,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"short","short",1928760516),new cljs.core.Keyword(null,"lower-case","lower-case",-212358583)], null)], 0)))));
})(),React.createElement("div",({"data-toggle": (cljs.core.truth_(is_mobile_QMARK_)?null:"tooltip"), "data-placement": "top", "title": "Muted", "className": "muted-activity"})),sablono.interpreter.interpret((cljs.core.truth_(show_new_item_tag)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.new-item-tag","div.new-item-tag",1142900666)], null):null)),React.createElement("div",({"className": "bookmark-tag-small mobile-only"})),React.createElement("div",({"className": "bookmark-tag big-web-tablet-only"}))], null):new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46409),React.createElement("div",({"className": "separator-dot"})),(function (){var t = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"published-at","published-at",249684621).cljs$core$IFn$_invoke$arity$1(activity_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"created-at","created-at",-89248644).cljs$core$IFn$_invoke$arity$1(activity_data);
}
})();
return React.createElement("span",({"data-toggle": (cljs.core.truth_(is_mobile_QMARK_)?null:"tooltip"), "data-placement": "top", "data-container": "body", "data-delay": "{\"show\":\"1000\", \"hide\":\"0\"}", "data-title": oc.web.lib.utils.activity_date_tooltip(activity_data), "className": "time-since"}),React.createElement("time",({"dateTime": t}),sablono.interpreter.interpret(oc.web.lib.utils.time_since.cljs$core$IFn$_invoke$arity$variadic(t,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"short","short",1928760516),new cljs.core.Keyword(null,"lower-case","lower-case",-212358583)], null)], 0)))));
})(),React.createElement("div",({"data-toggle": (cljs.core.truth_(is_mobile_QMARK_)?null:"tooltip"), "data-placement": "top", "title": "Muted", "className": "muted-activity"})),sablono.interpreter.interpret((cljs.core.truth_(show_new_item_tag)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.new-item-tag","div.new-item-tag",1142900666)], null):null)),React.createElement("div",({"className": "bookmark-tag-small mobile-only"})),React.createElement("div",({"className": "bookmark-tag big-web-tablet-only"}))], null)));
})(),sablono.interpreter.interpret((cljs.core.truth_(is_published_QMARK_)?(cljs.core.truth_((function (){var and__4115__auto__ = is_mobile_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return mobile_more_menu_el;
} else {
return and__4115__auto__;
}
})())?rum.core.portal(more_menu_comp(),mobile_more_menu_el):more_menu_comp()):null)),React.createElement("div",({"className": "activity-share-container"}))),React.createElement("div",({"className": "stream-item-body-ext group"}),React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["thumbnail-container","group",(cljs.core.truth_(show_body_thumbnail_QMARK_)?"has-preview":null)], null))}),sablono.interpreter.interpret((cljs.core.truth_(show_body_thumbnail_QMARK_)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.body-thumbnail-wrapper","div.body-thumbnail-wrapper",1877515111),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"body-thumbnail","body-thumbnail",1858934017).cljs$core$IFn$_invoke$arity$1(activity_data))], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"img.body-thumbnail","img.body-thumbnail",-637208980),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"data-image","data-image",-605281633),new cljs.core.Keyword(null,"thumbnail","thumbnail",-867906798).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"body-thumbnail","body-thumbnail",1858934017).cljs$core$IFn$_invoke$arity$1(activity_data)),new cljs.core.Keyword(null,"src","src",-1651076051),oc.web.images.optimize_image_url(new cljs.core.Keyword(null,"thumbnail","thumbnail",-867906798).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"body-thumbnail","body-thumbnail",1858934017).cljs$core$IFn$_invoke$arity$1(activity_data)),((102) * (3)))], null)], null)], null):null)),React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["stream-body-left","group",oc.web.lib.utils.class_set(cljs.core.PersistentArrayMap.createAsIfByAssoc([new cljs.core.Keyword(null,"has-thumbnail","has-thumbnail",649369963),(function (){var and__4115__auto__ = show_body_thumbnail_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(new cljs.core.Keyword(null,"fixed-video-id","fixed-video-id",-1380335259).cljs$core$IFn$_invoke$arity$1(activity_data));
} else {
return and__4115__auto__;
}
})(),new cljs.core.Keyword(null,"has-video","has-video",1628793657),(function (){var and__4115__auto__ = show_body_thumbnail_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return new cljs.core.Keyword(null,"fixed-video-id","fixed-video-id",-1380335259).cljs$core$IFn$_invoke$arity$1(activity_data);
} else {
return and__4115__auto__;
}
})(),oc.web.lib.utils.hide_class,true]))], null))}),React.createElement("div",({"ref": "activity-headline", "data-itemuuid": new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data), "dangerouslySetInnerHTML": oc.web.lib.utils.emojify(new cljs.core.Keyword(null,"headline","headline",-157157727).cljs$core$IFn$_invoke$arity$1(activity_data)), "className": "stream-item-headline ap-seen-item-headline"})),(cljs.core.truth_(replies_QMARK_)?sablono.interpreter.interpret(oc.web.components.stream_item.stream_item_activity_preview(is_mobile_QMARK_,new cljs.core.Keyword(null,"for-you-context","for-you-context",1744709462).cljs$core$IFn$_invoke$arity$1(activity_data))):sablono.interpreter.interpret(oc.web.components.stream_item.stream_item_summary(activity_data))))),sablono.interpreter.interpret(((cljs.core.not(is_published_QMARK_))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.stream-item-footer.group","div.stream-item-footer.group",-1653913034),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.stream-body-draft-edit","div.stream-body-draft-edit",-885341613),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.edit-draft-bt","button.mlb-reset.edit-draft-bt",100156985),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.actions.activity.activity_edit.cljs$core$IFn$_invoke$arity$1(activity_data);
})], null),"Continue editing"], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.stream-body-draft-delete","div.stream-body-draft-delete",1062919147),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.delete-draft-bt","button.mlb-reset.delete-draft-bt",-1280449262),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (p1__46400_SHARP_){
return oc.web.utils.draft.delete_draft_clicked(activity_data,p1__46400_SHARP_);
})], null),"Delete draft"], null)], null)], null):new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.stream-item-footer.group","div.stream-item-footer.group",-1653913034),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ref","ref",1289896967),"stream-item-reactions"], null),(cljs.core.truth_(member_QMARK_)?(function (){var G__46428 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"entity-data","entity-data",1608056141),activity_data,new cljs.core.Keyword(null,"only-thumb?","only-thumb?",959195446),true], null);
return (oc.web.components.reactions.reactions.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.reactions.reactions.cljs$core$IFn$_invoke$arity$1(G__46428) : oc.web.components.reactions.reactions.call(null,G__46428));
})():null),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.stream-item-footer-mobile-group","div.stream-item-footer-mobile-group",-434071112),(cljs.core.truth_(member_QMARK_)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.stream-item-comments-summary","div.stream-item-comments-summary",406489076),(function (){var G__46429 = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"entry-data","entry-data",1970939662),activity_data,new cljs.core.Keyword(null,"add-comment-focus-prefix","add-comment-focus-prefix",1635349699),"main-comment",new cljs.core.Keyword(null,"current-activity-id","current-activity-id",-930108529),current_activity_id,new cljs.core.Keyword(null,"new-comments-count","new-comments-count",46784695),(cljs.core.truth_(show_new_comments_QMARK_)?cljs.core.count(cljs.core.filter.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"unseen","unseen",1063275592),new cljs.core.Keyword(null,"replies-data","replies-data",1118937948).cljs$core$IFn$_invoke$arity$1(activity_data))):null)], null);
return (oc.web.components.ui.comments_summary.foc_comments_summary.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.comments_summary.foc_comments_summary.cljs$core$IFn$_invoke$arity$1(G__46429) : oc.web.components.ui.comments_summary.foc_comments_summary.call(null,G__46429));
})()], null):null),(cljs.core.truth_(show_wrt_QMARK_)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.stream-item-wrt","div.stream-item-wrt",53842703),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ref","ref",1289896967),new cljs.core.Keyword(null,"stream-item-wrt","stream-item-wrt",-508275659)], null),(function (){var G__46430 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"activity-data","activity-data",1293689136),activity_data,new cljs.core.Keyword(null,"read-data","read-data",-715156010),read_data], null);
return (oc.web.components.ui.wrt.wrt_count.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.wrt.wrt_count.cljs$core$IFn$_invoke$arity$1(G__46430) : oc.web.components.ui.wrt.wrt_count.call(null,G__46430));
})()], null):null),((cljs.core.seq(activity_attachments))?((cljs.core.not(is_mobile_QMARK_))?new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.stream-item-attachments","div.stream-item-attachments",828172249),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ref","ref",1289896967),new cljs.core.Keyword(null,"stream-item-attachments","stream-item-attachments",337586814)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.stream-item-attachments-count","div.stream-item-attachments-count",-994486515),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687),(cljs.core.truth_(is_mobile_QMARK_)?null:"tooltip"),new cljs.core.Keyword(null,"data-placement","data-placement",166529525),"top",new cljs.core.Keyword(null,"data-container","data-container",1473653353),"body",new cljs.core.Keyword(null,"title","title",636505583),[cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.count(activity_attachments))," attachment",(((cljs.core.count(activity_attachments) > (1)))?"s":null)].join('')], null),cljs.core.count(activity_attachments)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.stream-item-attachments-list","div.stream-item-attachments-list",112298400),(function (){var iter__4529__auto__ = (function oc$web$components$stream_item$iter__46431(s__46432){
return (new cljs.core.LazySeq(null,(function (){
var s__46432__$1 = s__46432;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__46432__$1);
if(temp__5735__auto__){
var s__46432__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__46432__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__46432__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__46434 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__46433 = (0);
while(true){
if((i__46433 < size__4528__auto__)){
var atc = cljs.core._nth(c__4527__auto__,i__46433);
cljs.core.chunk_append(b__46434,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a.stream-item-attachments-item","a.stream-item-attachments-item",262459802),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"href","href",-793805698),new cljs.core.Keyword(null,"file-url","file-url",-863738963).cljs$core$IFn$_invoke$arity$1(atc),new cljs.core.Keyword(null,"target","target",253001721),"_blank"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.stream-item-attachments-item-desc","div.stream-item-attachments-item-desc",-2077475476),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.file-name","span.file-name",-605410282),new cljs.core.Keyword(null,"file-name","file-name",-1654217259).cljs$core$IFn$_invoke$arity$1(atc)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.file-size","span.file-size",-1452586396),["(",clojure.contrib.humanize.filesize.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"file-size","file-size",-1900760755).cljs$core$IFn$_invoke$arity$1(atc),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"binary","binary",-1802232288),false,new cljs.core.Keyword(null,"format","format",-1306924766),"%.2f"], 0)),")"].join('')], null)], null)], null));

var G__46443 = (i__46433 + (1));
i__46433 = G__46443;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__46434),oc$web$components$stream_item$iter__46431(cljs.core.chunk_rest(s__46432__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__46434),null);
}
} else {
var atc = cljs.core.first(s__46432__$2);
return cljs.core.cons(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a.stream-item-attachments-item","a.stream-item-attachments-item",262459802),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"href","href",-793805698),new cljs.core.Keyword(null,"file-url","file-url",-863738963).cljs$core$IFn$_invoke$arity$1(atc),new cljs.core.Keyword(null,"target","target",253001721),"_blank"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.stream-item-attachments-item-desc","div.stream-item-attachments-item-desc",-2077475476),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.file-name","span.file-name",-605410282),new cljs.core.Keyword(null,"file-name","file-name",-1654217259).cljs$core$IFn$_invoke$arity$1(atc)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.file-size","span.file-size",-1452586396),["(",clojure.contrib.humanize.filesize.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"file-size","file-size",-1900760755).cljs$core$IFn$_invoke$arity$1(atc),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"binary","binary",-1802232288),false,new cljs.core.Keyword(null,"format","format",-1306924766),"%.2f"], 0)),")"].join('')], null)], null)], null),oc$web$components$stream_item$iter__46431(cljs.core.rest(s__46432__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(activity_attachments);
})()], null)], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.stream-item-mobile-attachments","div.stream-item-mobile-attachments",-1983696185),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.mobile-attachments-icon","span.mobile-attachments-icon",847943427)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.mobile-attachments-count","span.mobile-attachments-count",976377849),cljs.core.count(activity_attachments)], null)], null)):null)], null)], null)))));
}),new cljs.core.PersistentVector(null, 17, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$,rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"activity-share-container","activity-share-container",-1384168706)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"mobile-swipe-menu","mobile-swipe-menu",897943653)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"board-slug","board-slug",99003663)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"activity-uuid","activity-uuid",-663317778)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2((0),new cljs.core.Keyword("oc.web.components.stream-item","mobile-video-height","oc.web.components.stream-item/mobile-video-height",1317485109)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.stream-item","hammer-recognizer","oc.web.components.stream-item/hammer-recognizer",-673165734)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.stream-item","force-show-menu","oc.web.components.stream-item/force-show-menu",1207906181)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.stream-item","show-mobile-dismiss-bt","oc.web.components.stream-item/show-mobile-dismiss-bt",-2020556129)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.stream-item","show-mobile-more-bt","oc.web.components.stream-item/show-mobile-more-bt",5884493)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.stream-item","on-scroll","oc.web.components.stream-item/on-scroll",1778312633)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.stream-item","last-mobile-swipe-menu","oc.web.components.stream-item/last-mobile-swipe-menu",-1097306954)),oc.web.mixins.ui.render_on_resize(oc.web.components.stream_item.calc_video_height),(cljs.core.truth_(oc.shared.useragent.mobile_QMARK_)?oc.web.components.stream_item.swipe_gesture_manager(new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"swipe-left","swipe-left",865941635),oc.web.components.stream_item.swipe_left_handler,new cljs.core.Keyword(null,"swipe-right","swipe-right",1701568715),oc.web.components.stream_item.swipe_right_handler,new cljs.core.Keyword(null,"long-press","long-press",-521084018),oc.web.components.stream_item.long_press_handler,new cljs.core.Keyword(null,"disabled","disabled",-1529784218),(function (p1__46399_SHARP_){
return cljs.core.not(oc.web.utils.activity.is_published_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"activity-data","activity-data",1293689136).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(p1__46399_SHARP_)))], 0)));
})], null)):null),oc.web.mixins.ui.strict_refresh_tooltips_mixin,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
oc.web.components.stream_item.calc_video_height(s);

if(cljs.core.truth_(oc.shared.useragent.mobile_QMARK_)){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.stream-item","on-scroll","oc.web.components.stream-item/on-scroll",1778312633).cljs$core$IFn$_invoke$arity$1(s),goog.events.listen(window,goog.events.EventType.SCROLL,cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.components.stream_item.on_scroll,s)));
} else {
}

return s;
}),new cljs.core.Keyword(null,"did-update","did-update",-2143702256),(function (s){
if(cljs.core.truth_(oc.shared.useragent.mobile_QMARK_)){
var mobile_swipe_menu_46444 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"mobile-swipe-menu","mobile-swipe-menu",897943653)));
var activity_uuid_46445 = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"activity-data","activity-data",1293689136).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s))));
if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-item","last-mobile-swipe-menu","oc.web.components.stream-item/last-mobile-swipe-menu",-1097306954).cljs$core$IFn$_invoke$arity$1(s)),mobile_swipe_menu_46444)){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.stream-item","last-mobile-swipe-menu","oc.web.components.stream-item/last-mobile-swipe-menu",-1097306954).cljs$core$IFn$_invoke$arity$1(s),mobile_swipe_menu_46444);

if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(activity_uuid_46445,mobile_swipe_menu_46444)){
cljs.core.compare_and_set_BANG_(new cljs.core.Keyword("oc.web.components.stream-item","show-mobile-dismiss-bt","oc.web.components.stream-item/show-mobile-dismiss-bt",-2020556129).cljs$core$IFn$_invoke$arity$1(s),true,false);

cljs.core.compare_and_set_BANG_(new cljs.core.Keyword("oc.web.components.stream-item","show-mobile-more-bt","oc.web.components.stream-item/show-mobile-more-bt",5884493).cljs$core$IFn$_invoke$arity$1(s),true,false);
} else {
}
} else {
}
} else {
}

return s;
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-item","on-scroll","oc.web.components.stream-item/on-scroll",1778312633).cljs$core$IFn$_invoke$arity$1(s)))){
goog.events.unlistenByKey(cljs.core.deref(new cljs.core.Keyword("oc.web.components.stream-item","on-scroll","oc.web.components.stream-item/on-scroll",1778312633).cljs$core$IFn$_invoke$arity$1(s)));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.stream-item","on-scroll","oc.web.components.stream-item/on-scroll",1778312633).cljs$core$IFn$_invoke$arity$1(s),null);
} else {
}

return s;
})], null)], null),"stream-item");

//# sourceMappingURL=oc.web.components.stream_item.js.map
