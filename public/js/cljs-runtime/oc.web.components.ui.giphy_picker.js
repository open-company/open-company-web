goog.provide('oc.web.components.ui.giphy_picker');
oc.web.components.ui.giphy_picker.giphy_picker_max_height = (408);
oc.web.components.ui.giphy_picker.giphy_picker = rum.core.build_defcs((function (s,p__46236){
var map__46237 = p__46236;
var map__46237__$1 = (((((!((map__46237 == null))))?(((((map__46237.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46237.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46237):map__46237);
var fullscreen = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46237__$1,new cljs.core.Keyword(null,"fullscreen","fullscreen",-4371054));
var pick_emoji_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46237__$1,new cljs.core.Keyword(null,"pick-emoji-cb","pick-emoji-cb",-830701499));
var outer_container_selector = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46237__$1,new cljs.core.Keyword(null,"outer-container-selector","outer-container-selector",1406024934));
var offset_element_selector = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46237__$1,new cljs.core.Keyword(null,"offset-element-selector","offset-element-selector",-64578356));
var scrolling_element = (cljs.core.truth_(fullscreen)?document.querySelector(dommy.core.selector(outer_container_selector)):document.scrollingElement);
var win_height = (function (){var or__4126__auto__ = document.documentElement.clientHeight;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return window.innerHeight;
}
})();
var top_offset_limit = (cljs.core.truth_(offset_element_selector)?document.querySelector(dommy.core.selector(offset_element_selector)).offsetTop:(0));
var scroll_top = (cljs.core.truth_(offset_element_selector)?scrolling_element.scrollTop:(0));
var top_position = (function (){var x__4214__auto__ = (0);
var y__4215__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.giphy-picker","offset-top","oc.web.components.ui.giphy-picker/offset-top",-94901361).cljs$core$IFn$_invoke$arity$1(s));
return ((x__4214__auto__ > y__4215__auto__) ? x__4214__auto__ : y__4215__auto__);
})();
var relative_position = (((top_position + top_offset_limit) + (scroll_top * (-1))) + oc.web.components.ui.giphy_picker.giphy_picker_max_height);
var adjusted_position = (((relative_position > win_height))?(function (){var x__4214__auto__ = (0);
var y__4215__auto__ = ((top_position - (relative_position - win_height)) - (16));
return ((x__4214__auto__ > y__4215__auto__) ? x__4214__auto__ : y__4215__auto__);
})():top_position);
return React.createElement("div",({"style": ({"top": [cljs.core.str.cljs$core$IFn$_invoke$arity$1(adjusted_position),"px"].join('')}), "className": "giphy-picker"}));
}),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.local.cljs$core$IFn$_invoke$arity$2((0),new cljs.core.Keyword("oc.web.components.ui.giphy-picker","offset-top","oc.web.components.ui.giphy-picker/offset-top",-94901361)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
var outer_container_selector_46273 = new cljs.core.Keyword(null,"outer-container-selector","outer-container-selector",1406024934).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s)));
var temp__5735__auto___46274 = document.querySelector(dommy.core.selector(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(outer_container_selector_46273,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.medium-editor-media-picker","div.medium-editor-media-picker",1293519554)], null))));
if(cljs.core.truth_(temp__5735__auto___46274)){
var picker_el_46275 = temp__5735__auto___46274;
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.giphy-picker","offset-top","oc.web.components.ui.giphy-picker/offset-top",-94901361).cljs$core$IFn$_invoke$arity$1(s),picker_el_46275.offsetTop);
} else {
}

return s;
})], null)], null),"giphy-picker");

//# sourceMappingURL=oc.web.components.ui.giphy_picker.js.map
