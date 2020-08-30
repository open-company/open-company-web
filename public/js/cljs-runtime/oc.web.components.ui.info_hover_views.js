goog.provide('oc.web.components.ui.info_hover_views');
oc.web.components.ui.info_hover_views.board_info_view = rum.core.build_defc((function (p__38938){
var map__38939 = p__38938;
var map__38939__$1 = (((((!((map__38939 == null))))?(((((map__38939.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38939.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38939):map__38939);
var activity_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38939__$1,new cljs.core.Keyword(null,"activity-data","activity-data",1293689136));
var above_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38939__$1,new cljs.core.Keyword(null,"above?","above?",-19378631));
var following = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38939__$1,new cljs.core.Keyword(null,"following","following",-2049193617));
var followers_count = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38939__$1,new cljs.core.Keyword(null,"followers-count","followers-count",448882417));
return React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["board-info-view",oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"above","above",-1286866470),above_QMARK_], null))], null))}),React.createElement("div",({"className": "board-info-header"}),React.createElement("div",({"className": "board-info-right"}),(function (){var attrs38941 = new cljs.core.Keyword(null,"board-name","board-name",-677515056).cljs$core$IFn$_invoke$arity$1(activity_data);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs38941))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["board-info-name"], null)], null),attrs38941], 0))):({"className": "board-info-name"})),((cljs.core.map_QMARK_(attrs38941))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs38941)], null)));
})(),(function (){var attrs38942 = (((followers_count > (0)))?[cljs.core.str.cljs$core$IFn$_invoke$arity$1(followers_count)," follower",((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(followers_count,(1)))?"s":null)].join(''):null);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs38942))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["board-info-subline"], null)], null),attrs38942], 0))):({"className": "board-info-subline"})),((cljs.core.map_QMARK_(attrs38942))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs38942)], null)));
})())),React.createElement("div",({"className": "board-info-buttons group"}),React.createElement("button",({"onClick": (function (p1__38937_SHARP_){
oc.web.lib.utils.event_stop(p1__38937_SHARP_);

return oc.web.actions.nav_sidebar.nav_to_url_BANG_.cljs$core$IFn$_invoke$arity$3(p1__38937_SHARP_,new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(activity_data),oc.web.urls.board.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(activity_data)));
}), "className": "mlb-reset posts-bt"}),"Posts"),sablono.interpreter.interpret((function (){var G__38943 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"following","following",-2049193617),following,new cljs.core.Keyword(null,"resource-type","resource-type",1844262326),new cljs.core.Keyword(null,"board","board",-1907017633),new cljs.core.Keyword(null,"resource-uuid","resource-uuid",1798351789),new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127).cljs$core$IFn$_invoke$arity$1(activity_data)], null);
return (oc.web.components.ui.follow_button.follow_button.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.follow_button.follow_button.cljs$core$IFn$_invoke$arity$1(G__38943) : oc.web.components.ui.follow_button.follow_button.call(null,G__38943));
})())));
}),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$], null),"board-info-view");
oc.web.components.ui.info_hover_views.user_info_view = rum.core.build_defc((function (p__38945){
var map__38946 = p__38945;
var map__38946__$1 = (((((!((map__38946 == null))))?(((((map__38946.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38946.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38946):map__38946);
var above_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38946__$1,new cljs.core.Keyword(null,"above?","above?",-19378631));
var hide_last_name_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38946__$1,new cljs.core.Keyword(null,"hide-last-name?","hide-last-name?",-1527823457));
var hide_buttons = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38946__$1,new cljs.core.Keyword(null,"hide-buttons","hide-buttons",39532420));
var user_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38946__$1,new cljs.core.Keyword(null,"user-id","user-id",-206822291));
var following = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38946__$1,new cljs.core.Keyword(null,"following","following",-2049193617));
var user_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38946__$1,new cljs.core.Keyword(null,"user-data","user-data",2143823568));
var otf = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38946__$1,new cljs.core.Keyword(null,"otf","otf",2012710449));
var followers_count = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38946__$1,new cljs.core.Keyword(null,"followers-count","followers-count",448882417));
var inline_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38946__$1,new cljs.core.Keyword(null,"inline?","inline?",-1674483791));
var my_profile_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38946__$1,new cljs.core.Keyword(null,"my-profile?","my-profile?",1504512209));
var timezone_location_string = oc.web.utils.user.timezone_location_string(user_data);
return React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["user-info-view",oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"otf","otf",2012710449),otf,new cljs.core.Keyword(null,"inline","inline",1399884222),inline_QMARK_,new cljs.core.Keyword(null,"above","above",-1286866470),above_QMARK_], null))], null))}),(function (){var attrs38948 = (function (){var G__38949 = user_data;
var G__38950 = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"preferred-avatar-size","preferred-avatar-size",498036456),(96)], null);
return (oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$2 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$2(G__38949,G__38950) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,G__38949,G__38950));
})();
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs38948))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["user-info-header"], null)], null),attrs38948], 0))):({"className": "user-info-header"})),((cljs.core.map_QMARK_(attrs38948))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [React.createElement("div",({"className": "user-info-right"}),(function (){var attrs38951 = (cljs.core.truth_((function (){var and__4115__auto__ = hide_last_name_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.seq(new cljs.core.Keyword(null,"first-name","first-name",-1559982131).cljs$core$IFn$_invoke$arity$1(user_data));
} else {
return and__4115__auto__;
}
})())?new cljs.core.Keyword(null,"first-name","first-name",-1559982131).cljs$core$IFn$_invoke$arity$1(user_data):new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(user_data));
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs38951))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["user-info-name"], null)], null),attrs38951], 0))):({"className": "user-info-name"})),((cljs.core.map_QMARK_(attrs38951))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs38951)], null)));
})(),sablono.interpreter.interpret(((cljs.core.seq(new cljs.core.Keyword(null,"title","title",636505583).cljs$core$IFn$_invoke$arity$1(user_data)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-info-line","div.user-info-line",-166394648),new cljs.core.Keyword(null,"title","title",636505583).cljs$core$IFn$_invoke$arity$1(user_data)], null):null)),sablono.interpreter.interpret(((cljs.core.seq(timezone_location_string))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-info-subline","div.user-info-subline",1834483493),timezone_location_string], null):((cljs.core.seq(new cljs.core.Keyword(null,"slack-username","slack-username",-757727350).cljs$core$IFn$_invoke$arity$1(user_data)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-info-subline.slack-icon","div.user-info-subline.slack-icon",-506398807),new cljs.core.Keyword(null,"slack-username","slack-username",-757727350).cljs$core$IFn$_invoke$arity$1(user_data)], null):((cljs.core.seq(new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(user_data)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-info-subline","div.user-info-subline",1834483493),new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(user_data)], null):null)))),(function (){var attrs38952 = (((followers_count > (0)))?[cljs.core.str.cljs$core$IFn$_invoke$arity$1(followers_count)," follower",((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(followers_count,(1)))?"s":null)].join(''):null);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs38952))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["user-info-subline"], null)], null),attrs38952], 0))):({"className": "user-info-subline"})),((cljs.core.map_QMARK_(attrs38952))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs38952)], null)));
})(),sablono.interpreter.interpret((cljs.core.truth_(hide_buttons)?null:new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.profile-bt","button.mlb-reset.profile-bt",208749542),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (p1__38944_SHARP_){
return oc.web.actions.nav_sidebar.nav_to_author_BANG_.cljs$core$IFn$_invoke$arity$3(p1__38944_SHARP_,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user_data),oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user_data)));
})], null),"View profile and posts"], null))))], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs38948),React.createElement("div",({"className": "user-info-right"}),(function (){var attrs38953 = (cljs.core.truth_((function (){var and__4115__auto__ = hide_last_name_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.seq(new cljs.core.Keyword(null,"first-name","first-name",-1559982131).cljs$core$IFn$_invoke$arity$1(user_data));
} else {
return and__4115__auto__;
}
})())?new cljs.core.Keyword(null,"first-name","first-name",-1559982131).cljs$core$IFn$_invoke$arity$1(user_data):new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(user_data));
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs38953))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["user-info-name"], null)], null),attrs38953], 0))):({"className": "user-info-name"})),((cljs.core.map_QMARK_(attrs38953))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs38953)], null)));
})(),sablono.interpreter.interpret(((cljs.core.seq(new cljs.core.Keyword(null,"title","title",636505583).cljs$core$IFn$_invoke$arity$1(user_data)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-info-line","div.user-info-line",-166394648),new cljs.core.Keyword(null,"title","title",636505583).cljs$core$IFn$_invoke$arity$1(user_data)], null):null)),sablono.interpreter.interpret(((cljs.core.seq(timezone_location_string))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-info-subline","div.user-info-subline",1834483493),timezone_location_string], null):((cljs.core.seq(new cljs.core.Keyword(null,"slack-username","slack-username",-757727350).cljs$core$IFn$_invoke$arity$1(user_data)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-info-subline.slack-icon","div.user-info-subline.slack-icon",-506398807),new cljs.core.Keyword(null,"slack-username","slack-username",-757727350).cljs$core$IFn$_invoke$arity$1(user_data)], null):((cljs.core.seq(new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(user_data)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-info-subline","div.user-info-subline",1834483493),new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(user_data)], null):null)))),(function (){var attrs38954 = (((followers_count > (0)))?[cljs.core.str.cljs$core$IFn$_invoke$arity$1(followers_count)," follower",((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(followers_count,(1)))?"s":null)].join(''):null);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs38954))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["user-info-subline"], null)], null),attrs38954], 0))):({"className": "user-info-subline"})),((cljs.core.map_QMARK_(attrs38954))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs38954)], null)));
})(),sablono.interpreter.interpret((cljs.core.truth_(hide_buttons)?null:new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.profile-bt","button.mlb-reset.profile-bt",208749542),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (p1__38944_SHARP_){
return oc.web.actions.nav_sidebar.nav_to_author_BANG_.cljs$core$IFn$_invoke$arity$3(p1__38944_SHARP_,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user_data),oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user_data)));
})], null),"View profile and posts"], null))))], null)));
})());
}),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$], null),"user-info-view");
oc.web.components.ui.info_hover_views.user_info_otf = rum.core.build_defc((function (p__38955){
var map__38956 = p__38955;
var map__38956__$1 = (((((!((map__38956 == null))))?(((((map__38956.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38956.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38956):map__38956);
var props = map__38956__$1;
var portal_el = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38956__$1,new cljs.core.Keyword(null,"portal-el","portal-el",1623429907));
return sablono.interpreter.interpret((cljs.core.truth_(((cljs.core.not(oc.web.lib.responsive.is_mobile_size_QMARK_()))?(function (){var and__4115__auto__ = portal_el;
if(cljs.core.truth_(and__4115__auto__)){
return portal_el.parentElement;
} else {
return and__4115__auto__;
}
})():false))?(function (){var viewport_size = oc.web.utils.dom.viewport_size();
var pos = oc.web.utils.dom.viewport_offset(portal_el);
var above_QMARK_ = (new cljs.core.Keyword(null,"y","y",-1757859776).cljs$core$IFn$_invoke$arity$1(pos) >= (new cljs.core.Keyword(null,"height","height",1025178622).cljs$core$IFn$_invoke$arity$1(viewport_size) / (2)));
var next_props = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([props,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"above?","above?",-19378631),above_QMARK_,new cljs.core.Keyword(null,"otf","otf",2012710449),true], null)], 0));
return rum.core.portal((function (){var G__38958 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(next_props,new cljs.core.Keyword(null,"above?","above?",-19378631),above_QMARK_);
return (oc.web.components.ui.info_hover_views.user_info_view.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.info_hover_views.user_info_view.cljs$core$IFn$_invoke$arity$1(G__38958) : oc.web.components.ui.info_hover_views.user_info_view.call(null,G__38958));
})(),portal_el);
})():null));
}),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$], null),"user-info-otf");
oc.web.components.ui.info_hover_views.default_positioning = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"vertical-position","vertical-position",-661228796),null,new cljs.core.Keyword(null,"horizontal-position","horizontal-position",-1646056328),null], null);
oc.web.components.ui.info_hover_views.popup_size = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"width","width",-384071477),(200),new cljs.core.Keyword(null,"height","height",1025178622),(211)], null);
oc.web.components.ui.info_hover_views.padding = (16);
oc.web.components.ui.info_hover_views.popup_offset = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"x","x",2099068185),oc.web.components.ui.info_hover_views.padding,new cljs.core.Keyword(null,"y","y",-1757859776),(oc.web.components.ui.info_hover_views.padding + oc.web.lib.responsive.navbar_height)], null);
oc.web.components.ui.info_hover_views.check_hover = (function oc$web$components$ui$info_hover_views$check_hover(s,parent_el){
var pos = oc.web.utils.dom.viewport_offset(parent_el);
var vertical_position = ((((new cljs.core.Keyword(null,"y","y",-1757859776).cljs$core$IFn$_invoke$arity$1(pos) - new cljs.core.Keyword(null,"y","y",-1757859776).cljs$core$IFn$_invoke$arity$1(oc.web.components.ui.info_hover_views.popup_offset)) > new cljs.core.Keyword(null,"height","height",1025178622).cljs$core$IFn$_invoke$arity$1(oc.web.components.ui.info_hover_views.popup_size)))?new cljs.core.Keyword(null,"above","above",-1286866470):new cljs.core.Keyword(null,"below","below",-926774883));
var horizontal_offset = (((new cljs.core.Keyword(null,"x","x",2099068185).cljs$core$IFn$_invoke$arity$1(pos) > new cljs.core.Keyword(null,"x","x",2099068185).cljs$core$IFn$_invoke$arity$1(oc.web.components.ui.info_hover_views.popup_offset)))?(0):(((new cljs.core.Keyword(null,"x","x",2099068185).cljs$core$IFn$_invoke$arity$1(pos) < (0)))?((new cljs.core.Keyword(null,"x","x",2099068185).cljs$core$IFn$_invoke$arity$1(pos) * (-1)) + new cljs.core.Keyword(null,"x","x",2099068185).cljs$core$IFn$_invoke$arity$1(oc.web.components.ui.info_hover_views.popup_offset)):(new cljs.core.Keyword(null,"x","x",2099068185).cljs$core$IFn$_invoke$arity$1(oc.web.components.ui.info_hover_views.popup_offset) - new cljs.core.Keyword(null,"x","x",2099068185).cljs$core$IFn$_invoke$arity$1(pos))));
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"vertical-position","vertical-position",-661228796),vertical_position,new cljs.core.Keyword(null,"horizontal-offset","horizontal-offset",-516051643),horizontal_offset], null);
});
oc.web.components.ui.info_hover_views.enter_BANG_ = (function oc$web$components$ui$info_hover_views$enter_BANG_(s,parent_el){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","enter-timeout","oc.web.components.ui.info-hover-views/enter-timeout",1411397175).cljs$core$IFn$_invoke$arity$1(s),null);

if(cljs.core.compare_and_set_BANG_(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","hovering","oc.web.components.ui.info-hover-views/hovering",1146479257).cljs$core$IFn$_invoke$arity$1(s),false,true)){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","positioning","oc.web.components.ui.info-hover-views/positioning",-878014318).cljs$core$IFn$_invoke$arity$1(s),oc.web.components.ui.info_hover_views.check_hover(s,parent_el));
} else {
return null;
}
});
oc.web.components.ui.info_hover_views.leave_BANG_ = (function oc$web$components$ui$info_hover_views$leave_BANG_(s){
if(cljs.core.compare_and_set_BANG_(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","hovering","oc.web.components.ui.info-hover-views/hovering",1146479257).cljs$core$IFn$_invoke$arity$1(s),true,false)){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","positioning","oc.web.components.ui.info-hover-views/positioning",-878014318).cljs$core$IFn$_invoke$arity$1(s),oc.web.components.ui.info_hover_views.default_positioning);
} else {
return null;
}
});
oc.web.components.ui.info_hover_views.enter_ev = (function oc$web$components$ui$info_hover_views$enter_ev(s,parent_el){
if(cljs.core.truth_(new cljs.core.Keyword(null,"disabled","disabled",-1529784218).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s))))){
return null;
} else {
window.clearTimeout(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","enter-timeout","oc.web.components.ui.info-hover-views/enter-timeout",1411397175).cljs$core$IFn$_invoke$arity$1(s)));

window.clearTimeout(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","leave-timeout","oc.web.components.ui.info-hover-views/leave-timeout",-2047886350).cljs$core$IFn$_invoke$arity$1(s)));

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","enter-timeout","oc.web.components.ui.info-hover-views/enter-timeout",1411397175).cljs$core$IFn$_invoke$arity$1(s),window.setTimeout((function (){
return oc.web.components.ui.info_hover_views.enter_BANG_(s,parent_el);
}),(500)));
}
});
oc.web.components.ui.info_hover_views.leave_ev = (function oc$web$components$ui$info_hover_views$leave_ev(s){
if(cljs.core.truth_(new cljs.core.Keyword(null,"disabled","disabled",-1529784218).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s))))){
return null;
} else {
window.clearTimeout(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","enter-timeout","oc.web.components.ui.info-hover-views/enter-timeout",1411397175).cljs$core$IFn$_invoke$arity$1(s)));

window.clearTimeout(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","leave-timeout","oc.web.components.ui.info-hover-views/leave-timeout",-2047886350).cljs$core$IFn$_invoke$arity$1(s)));

if(cljs.core.truth_(new cljs.core.Keyword(null,"leave-delay?","leave-delay?",654972741).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s))))){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","leave-timeout","oc.web.components.ui.info-hover-views/leave-timeout",-2047886350).cljs$core$IFn$_invoke$arity$1(s),window.setTimeout((function (){
return oc.web.components.ui.info_hover_views.leave_BANG_(s);
}),(500)));
} else {
return oc.web.components.ui.info_hover_views.leave_BANG_(s);
}
}
});
oc.web.components.ui.info_hover_views.click = (function oc$web$components$ui$info_hover_views$click(s,e){
var user_id = new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-data","user-data",2143823568).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s))));
return oc.web.actions.nav_sidebar.nav_to_author_BANG_.cljs$core$IFn$_invoke$arity$3(e,user_id,oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$1(user_id));
});
oc.web.components.ui.info_hover_views.user_info_hover = rum.core.build_defcs((function (s,p__38968){
var map__38969 = p__38968;
var map__38969__$1 = (((((!((map__38969 == null))))?(((((map__38969.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38969.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38969):map__38969);
var disabled = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38969__$1,new cljs.core.Keyword(null,"disabled","disabled",-1529784218));
var user_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38969__$1,new cljs.core.Keyword(null,"user-data","user-data",2143823568));
var current_user_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38969__$1,new cljs.core.Keyword(null,"current-user-id","current-user-id",-2013633451));
var leave_delay_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38969__$1,new cljs.core.Keyword(null,"leave-delay?","leave-delay?",654972741));
if(cljs.core.truth_(oc.web.lib.responsive.is_mobile_size_QMARK_())){
return React.createElement("div",({"className": "info-hover-view"}));
} else {
var my_profile_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user_data),current_user_id);
var pos = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","positioning","oc.web.components.ui.info-hover-views/positioning",-878014318).cljs$core$IFn$_invoke$arity$1(s));
var users_info = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"users-info-hover","users-info-hover",-941434570));
var active_user_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(users_info,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user_data));
var complete_user_data = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user_data,active_user_data], 0));
var follow_publishers_list = cljs.core.set(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291),org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"follow-publishers-list","follow-publishers-list",-374150342))));
var following_QMARK_ = (function (){var G__38979 = new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user_data);
return (follow_publishers_list.cljs$core$IFn$_invoke$arity$1 ? follow_publishers_list.cljs$core$IFn$_invoke$arity$1(G__38979) : follow_publishers_list.call(null,G__38979));
})();
var followers_publishers_count = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"followers-publishers-count","followers-publishers-count",-692976579));
var followers_count = cljs.core.get.cljs$core$IFn$_invoke$arity$2(followers_publishers_count,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user_data));
return React.createElement("div",({"onClick": (function (p1__38965_SHARP_){
if(cljs.core.truth_(oc.web.lib.utils.button_clicked_QMARK_(p1__38965_SHARP_))){
return null;
} else {
return oc.web.lib.utils.event_stop(p1__38965_SHARP_);
}
}), "style": ({"marginLeft": [cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"horizontal-offset","horizontal-offset",-516051643).cljs$core$IFn$_invoke$arity$1(pos)),"px"].join('')}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["info-hover-view",oc.web.lib.utils.class_set(cljs.core.PersistentArrayMap.createAsIfByAssoc([new cljs.core.Keyword(null,"show","show",-576705889),cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","hovering","oc.web.components.ui.info-hover-views/hovering",1146479257).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"vertical-position","vertical-position",-661228796).cljs$core$IFn$_invoke$arity$1(pos),true]))], null))}),sablono.interpreter.interpret((function (){var G__38980 = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"user-data","user-data",2143823568),complete_user_data,new cljs.core.Keyword(null,"inline?","inline?",-1674483791),cljs.core.not(active_user_data),new cljs.core.Keyword(null,"hide-buttons","hide-buttons",39532420),cljs.core.not(active_user_data),new cljs.core.Keyword(null,"my-profile?","my-profile?",1504512209),my_profile_QMARK_,new cljs.core.Keyword(null,"following","following",-2049193617),following_QMARK_,new cljs.core.Keyword(null,"followers-count","followers-count",448882417),new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(followers_count)], null);
return (oc.web.components.ui.info_hover_views.user_info_view.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.info_hover_views.user_info_view.cljs$core$IFn$_invoke$arity$1(G__38980) : oc.web.components.ui.info_hover_views.user_info_view.call(null,G__38980));
})()));
}
}),new cljs.core.PersistentVector(null, 13, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$,rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"users-info-hover","users-info-hover",-941434570)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"follow-publishers-list","follow-publishers-list",-374150342)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"followers-publishers-count","followers-publishers-count",-692976579)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.ui.info-hover-views","mouse-enter","oc.web.components.ui.info-hover-views/mouse-enter",248471427)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.ui.info-hover-views","mouse-leave","oc.web.components.ui.info-hover-views/mouse-leave",-1666462879)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.ui.info-hover-views","click","oc.web.components.ui.info-hover-views/click",1207112909)),rum.core.local.cljs$core$IFn$_invoke$arity$2(oc.web.components.ui.info_hover_views.default_positioning,new cljs.core.Keyword("oc.web.components.ui.info-hover-views","positioning","oc.web.components.ui.info-hover-views/positioning",-878014318)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.info-hover-views","hovering","oc.web.components.ui.info-hover-views/hovering",1146479257)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.ui.info-hover-views","enter-timeout","oc.web.components.ui.info-hover-views/enter-timeout",1411397175)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.ui.info-hover-views","leave-timeout","oc.web.components.ui.info-hover-views/leave-timeout",-2047886350)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
var temp__33777__auto___39110 = rum.core.dom_node(s);
if(cljs.core.truth_(temp__33777__auto___39110)){
var el_39111 = temp__33777__auto___39110;
var temp__33777__auto___39113__$1 = el_39111.parentElement;
if(cljs.core.truth_(temp__33777__auto___39113__$1)){
var parent_el_39114 = temp__33777__auto___39113__$1;
if(cljs.core.truth_(oc.web.lib.responsive.is_mobile_size_QMARK_())){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","click","oc.web.components.ui.info-hover-views/click",1207112909).cljs$core$IFn$_invoke$arity$1(s),goog.events.listen(parent_el_39114,goog.events.EventType.CLICK,cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.components.ui.info_hover_views.click,s)));
} else {
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","mouse-enter","oc.web.components.ui.info-hover-views/mouse-enter",248471427).cljs$core$IFn$_invoke$arity$1(s),goog.events.listen(parent_el_39114,goog.events.EventType.MOUSEENTER,(function (){
return oc.web.components.ui.info_hover_views.enter_ev(s,parent_el_39114);
})));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","mouse-leave","oc.web.components.ui.info-hover-views/mouse-leave",-1666462879).cljs$core$IFn$_invoke$arity$1(s),goog.events.listen(parent_el_39114,goog.events.EventType.MOUSELEAVE,(function (){
return oc.web.components.ui.info_hover_views.leave_ev(s);
})));
}
} else {
}
} else {
}

return s;
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","mouse-enter","oc.web.components.ui.info-hover-views/mouse-enter",248471427).cljs$core$IFn$_invoke$arity$1(s)))){
goog.events.unlistenByKey(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","mouse-enter","oc.web.components.ui.info-hover-views/mouse-enter",248471427).cljs$core$IFn$_invoke$arity$1(s)));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","mouse-enter","oc.web.components.ui.info-hover-views/mouse-enter",248471427).cljs$core$IFn$_invoke$arity$1(s),null);
} else {
}

if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","mouse-leave","oc.web.components.ui.info-hover-views/mouse-leave",-1666462879).cljs$core$IFn$_invoke$arity$1(s)))){
goog.events.unlistenByKey(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","mouse-leave","oc.web.components.ui.info-hover-views/mouse-leave",-1666462879).cljs$core$IFn$_invoke$arity$1(s)));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","mouse-leave","oc.web.components.ui.info-hover-views/mouse-leave",-1666462879).cljs$core$IFn$_invoke$arity$1(s),null);
} else {
}

if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","click","oc.web.components.ui.info-hover-views/click",1207112909).cljs$core$IFn$_invoke$arity$1(s)))){
goog.events.unlistenByKey(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","click","oc.web.components.ui.info-hover-views/click",1207112909).cljs$core$IFn$_invoke$arity$1(s)));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","click","oc.web.components.ui.info-hover-views/click",1207112909).cljs$core$IFn$_invoke$arity$1(s),null);
} else {
}

return s;
})], null)], null),"user-info-hover");
oc.web.components.ui.info_hover_views.board_info_hover = rum.core.build_defcs((function (s,p__38997){
var map__38998 = p__38997;
var map__38998__$1 = (((((!((map__38998 == null))))?(((((map__38998.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38998.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38998):map__38998);
var disabled = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38998__$1,new cljs.core.Keyword(null,"disabled","disabled",-1529784218));
var activity_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38998__$1,new cljs.core.Keyword(null,"activity-data","activity-data",1293689136));
var leave_delay_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38998__$1,new cljs.core.Keyword(null,"leave-delay?","leave-delay?",654972741));
if(cljs.core.truth_(oc.web.lib.responsive.is_mobile_size_QMARK_())){
return React.createElement("div",({"className": "info-hover-view"}));
} else {
var pos = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","positioning","oc.web.components.ui.info-hover-views/positioning",-878014318).cljs$core$IFn$_invoke$arity$1(s));
var follow_boards_list = cljs.core.set(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"follow-boards-list","follow-boards-list",-461166530))));
var following_QMARK_ = (function (){var G__39003 = new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127).cljs$core$IFn$_invoke$arity$1(activity_data);
return (follow_boards_list.cljs$core$IFn$_invoke$arity$1 ? follow_boards_list.cljs$core$IFn$_invoke$arity$1(G__39003) : follow_boards_list.call(null,G__39003));
})();
var followers_boards_count = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"followers-boards-count","followers-boards-count",334424133));
var followers_count = cljs.core.get.cljs$core$IFn$_invoke$arity$2(followers_boards_count,new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127).cljs$core$IFn$_invoke$arity$1(activity_data));
return React.createElement("div",({"onClick": (function (p1__38996_SHARP_){
if(cljs.core.truth_(oc.web.lib.utils.button_clicked_QMARK_(p1__38996_SHARP_))){
return null;
} else {
return oc.web.lib.utils.event_stop(p1__38996_SHARP_);
}
}), "style": ({"marginLeft": [cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"horizontal-offset","horizontal-offset",-516051643).cljs$core$IFn$_invoke$arity$1(pos)),"px"].join('')}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["info-hover-view",oc.web.lib.utils.class_set(cljs.core.PersistentArrayMap.createAsIfByAssoc([new cljs.core.Keyword(null,"show","show",-576705889),cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","hovering","oc.web.components.ui.info-hover-views/hovering",1146479257).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"vertical-position","vertical-position",-661228796).cljs$core$IFn$_invoke$arity$1(pos),true]))], null))}),sablono.interpreter.interpret((function (){var G__39004 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"activity-data","activity-data",1293689136),activity_data,new cljs.core.Keyword(null,"following","following",-2049193617),following_QMARK_,new cljs.core.Keyword(null,"followers-count","followers-count",448882417),new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(followers_count)], null);
return (oc.web.components.ui.info_hover_views.board_info_view.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.info_hover_views.board_info_view.cljs$core$IFn$_invoke$arity$1(G__39004) : oc.web.components.ui.info_hover_views.board_info_view.call(null,G__39004));
})()));
}
}),new cljs.core.PersistentVector(null, 13, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$,rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"org-data","org-data",96720321)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"follow-boards-list","follow-boards-list",-461166530)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"followers-boards-count","followers-boards-count",334424133)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.ui.info-hover-views","mouse-enter","oc.web.components.ui.info-hover-views/mouse-enter",248471427)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.ui.info-hover-views","mouse-leave","oc.web.components.ui.info-hover-views/mouse-leave",-1666462879)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.ui.info-hover-views","click","oc.web.components.ui.info-hover-views/click",1207112909)),rum.core.local.cljs$core$IFn$_invoke$arity$2(oc.web.components.ui.info_hover_views.default_positioning,new cljs.core.Keyword("oc.web.components.ui.info-hover-views","positioning","oc.web.components.ui.info-hover-views/positioning",-878014318)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.info-hover-views","hovering","oc.web.components.ui.info-hover-views/hovering",1146479257)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.ui.info-hover-views","enter-timeout","oc.web.components.ui.info-hover-views/enter-timeout",1411397175)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.ui.info-hover-views","leave-timeout","oc.web.components.ui.info-hover-views/leave-timeout",-2047886350)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
var temp__33777__auto___39165 = rum.core.dom_node(s);
if(cljs.core.truth_(temp__33777__auto___39165)){
var el_39166 = temp__33777__auto___39165;
var temp__33777__auto___39168__$1 = el_39166.parentElement;
if(cljs.core.truth_(temp__33777__auto___39168__$1)){
var parent_el_39169 = temp__33777__auto___39168__$1;
if(cljs.core.truth_(oc.web.lib.responsive.is_mobile_size_QMARK_())){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","click","oc.web.components.ui.info-hover-views/click",1207112909).cljs$core$IFn$_invoke$arity$1(s),goog.events.listen(parent_el_39169,goog.events.EventType.CLICK,cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.components.ui.info_hover_views.click,s)));
} else {
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","mouse-enter","oc.web.components.ui.info-hover-views/mouse-enter",248471427).cljs$core$IFn$_invoke$arity$1(s),goog.events.listen(parent_el_39169,goog.events.EventType.MOUSEENTER,(function (){
return oc.web.components.ui.info_hover_views.enter_ev(s,parent_el_39169);
})));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","mouse-leave","oc.web.components.ui.info-hover-views/mouse-leave",-1666462879).cljs$core$IFn$_invoke$arity$1(s),goog.events.listen(parent_el_39169,goog.events.EventType.MOUSELEAVE,(function (){
return oc.web.components.ui.info_hover_views.leave_ev(s);
})));
}
} else {
}
} else {
}

return s;
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","mouse-enter","oc.web.components.ui.info-hover-views/mouse-enter",248471427).cljs$core$IFn$_invoke$arity$1(s)))){
goog.events.unlistenByKey(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","mouse-enter","oc.web.components.ui.info-hover-views/mouse-enter",248471427).cljs$core$IFn$_invoke$arity$1(s)));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","mouse-enter","oc.web.components.ui.info-hover-views/mouse-enter",248471427).cljs$core$IFn$_invoke$arity$1(s),null);
} else {
}

if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","mouse-leave","oc.web.components.ui.info-hover-views/mouse-leave",-1666462879).cljs$core$IFn$_invoke$arity$1(s)))){
goog.events.unlistenByKey(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","mouse-leave","oc.web.components.ui.info-hover-views/mouse-leave",-1666462879).cljs$core$IFn$_invoke$arity$1(s)));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","mouse-leave","oc.web.components.ui.info-hover-views/mouse-leave",-1666462879).cljs$core$IFn$_invoke$arity$1(s),null);
} else {
}

if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","click","oc.web.components.ui.info-hover-views/click",1207112909).cljs$core$IFn$_invoke$arity$1(s)))){
goog.events.unlistenByKey(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","click","oc.web.components.ui.info-hover-views/click",1207112909).cljs$core$IFn$_invoke$arity$1(s)));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.info-hover-views","click","oc.web.components.ui.info-hover-views/click",1207112909).cljs$core$IFn$_invoke$arity$1(s),null);
} else {
}

return s;
})], null)], null),"board-info-hover");

//# sourceMappingURL=oc.web.components.ui.info_hover_views.js.map
