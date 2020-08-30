goog.provide('oc.web.components.ui.navbar');
oc.web.components.ui.navbar.mobile_nav_BANG_ = (function oc$web$components$ui$navbar$mobile_nav_BANG_(e,board_slug){
return oc.web.router.nav_BANG_(oc.web.urls.board.cljs$core$IFn$_invoke$arity$1(board_slug));
});
oc.web.components.ui.navbar.scroll_top_offset = (46);
oc.web.components.ui.navbar.check_scroll = (function oc$web$components$ui$navbar$check_scroll(s,e){
if((((document.scrollingElement.scrollTop > oc.web.components.ui.navbar.scroll_top_offset)) && (cljs.core.not(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.navbar","scrolled","oc.web.components.ui.navbar/scrolled",-2005795939).cljs$core$IFn$_invoke$arity$1(s)))))){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.navbar","scrolled","oc.web.components.ui.navbar/scrolled",-2005795939).cljs$core$IFn$_invoke$arity$1(s),true);
} else {
}

if(cljs.core.truth_((((document.scrollingElement.scrollTop <= oc.web.components.ui.navbar.scroll_top_offset))?cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.navbar","scrolled","oc.web.components.ui.navbar/scrolled",-2005795939).cljs$core$IFn$_invoke$arity$1(s)):false))){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.navbar","scrolled","oc.web.components.ui.navbar/scrolled",-2005795939).cljs$core$IFn$_invoke$arity$1(s),false);
} else {
return null;
}
});
oc.web.components.ui.navbar.navbar = rum.core.build_defcs((function (s){
var map__46001 = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"navbar-data","navbar-data",2074703570));
var map__46001__$1 = (((((!((map__46001 == null))))?(((((map__46001.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46001.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46001):map__46001);
var navbar_data = map__46001__$1;
var org_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46001__$1,new cljs.core.Keyword(null,"org-data","org-data",96720321));
var show_login_overlay = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46001__$1,new cljs.core.Keyword(null,"show-login-overlay","show-login-overlay",1026669411));
var panel_stack = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46001__$1,new cljs.core.Keyword(null,"panel-stack","panel-stack",1084180004));
var current_board_slug = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46001__$1,new cljs.core.Keyword(null,"current-board-slug","current-board-slug",1670379364));
var current_user_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46001__$1,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915));
var orgs_dropdown_visible = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46001__$1,new cljs.core.Keyword(null,"orgs-dropdown-visible","orgs-dropdown-visible",801323944));
var contributions_user_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46001__$1,new cljs.core.Keyword(null,"contributions-user-data","contributions-user-data",799006441));
var search_active = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46001__$1,new cljs.core.Keyword(null,"search-active","search-active",913672682));
var show_whats_new_green_dot = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46001__$1,new cljs.core.Keyword(null,"show-whats-new-green-dot","show-whats-new-green-dot",-206531957));
var board_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46001__$1,new cljs.core.Keyword(null,"board-data","board-data",1372958925));
var current_org_slug = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46001__$1,new cljs.core.Keyword(null,"current-org-slug","current-org-slug",1185011374));
var current_contributions_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46001__$1,new cljs.core.Keyword(null,"current-contributions-id","current-contributions-id",-1702467529));
var is_mobile_QMARK_ = oc.web.lib.responsive.is_mobile_size_QMARK_();
var current_panel = cljs.core.last(panel_stack);
var expanded_user_menu = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(current_panel,new cljs.core.Keyword(null,"menu","menu",352255198));
var cmail_state = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"cmail-state","cmail-state",-747393321));
var mobile_title = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug),new cljs.core.Keyword(null,"replies","replies",-1389888974)))?"For you":((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug),new cljs.core.Keyword(null,"inbox","inbox",1888669443)))?"Unread":((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug),new cljs.core.Keyword(null,"all-posts","all-posts",-1285476533)))?"All":((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug),new cljs.core.Keyword(null,"bookmarks","bookmarks",1877375283)))?"Bookmarks":((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug),new cljs.core.Keyword(null,"following","following",-2049193617)))?"Home":((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug),new cljs.core.Keyword(null,"topics","topics",625768208)))?"Explore":(cljs.core.truth_((function (){var and__4115__auto__ = current_contributions_id;
if(cljs.core.truth_(and__4115__auto__)){
return new cljs.core.Keyword(null,"self?","self?",-701815921).cljs$core$IFn$_invoke$arity$1(contributions_user_data);
} else {
return and__4115__auto__;
}
})())?"You":(cljs.core.truth_((function (){var and__4115__auto__ = current_contributions_id;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.map_QMARK_(contributions_user_data);
} else {
return and__4115__auto__;
}
})())?new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(contributions_user_data):new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(board_data)
))))))));
var search_active_QMARK_ = org.martinklepsch.derivatives.react(s,oc.web.stores.search.search_active_QMARK_);
return React.createElement("nav",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["oc-navbar","group",oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"show-login-overlay","show-login-overlay",1026669411),show_login_overlay,new cljs.core.Keyword(null,"expanded-user-menu","expanded-user-menu",-1187915345),expanded_user_menu,new cljs.core.Keyword(null,"has-prior-updates","has-prior-updates",1663459079),(function (){var and__4115__auto__ = current_org_slug;
if(cljs.core.truth_(and__4115__auto__)){
return (new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"collection","GET"], 0))) > (0));
} else {
return and__4115__auto__;
}
})(),new cljs.core.Keyword(null,"showing-orgs-dropdown","showing-orgs-dropdown",-699224587),orgs_dropdown_visible,new cljs.core.Keyword(null,"can-edit-board","can-edit-board",-1378078290),(function (){var and__4115__auto__ = current_org_slug;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(new cljs.core.Keyword(null,"read-only","read-only",-191706886).cljs$core$IFn$_invoke$arity$1(org_data));
} else {
return and__4115__auto__;
}
})()], null))], null))}),sablono.interpreter.interpret(((oc.web.lib.utils.is_test_env_QMARK_())?null:(oc.web.components.ui.login_overlay.login_overlays_handler.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.login_overlay.login_overlays_handler.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.login_overlay.login_overlays_handler.call(null)))),(cljs.core.truth_((function (){var and__4115__auto__ = is_mobile_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return search_active_QMARK_;
} else {
return and__4115__auto__;
}
})())?React.createElement("div",({"className": "mobile-header"}),React.createElement("button",({"onClick": (function (e){
oc.web.lib.utils.event_stop(e);

oc.web.actions.search.reset();

return oc.web.actions.search.inactive();
}), "className": "mlb-reset search-close-bt"})),React.createElement("div",({"className": "mobile-header-title"}),"Search")):React.createElement("div",({"className": "oc-navbar-header group"}),React.createElement("div",({"className": "oc-navbar-header-container group"}),(function (){var attrs46005 = (cljs.core.truth_(is_mobile_QMARK_)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.mobile-ham-menu","button.mlb-reset.mobile-ham-menu",-659094677),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"mobile-navigation-sidebar","mobile-navigation-sidebar",-1723544081)], null),cljs.core.not], null));
})], null)], null):(oc.web.components.ui.orgs_dropdown.orgs_dropdown.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.orgs_dropdown.orgs_dropdown.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.orgs_dropdown.orgs_dropdown.call(null)));
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46005))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["navbar-left"], null)], null),attrs46005], 0))):({"className": "navbar-left"})),((cljs.core.map_QMARK_(attrs46005))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46005)], null)));
})(),(cljs.core.truth_(is_mobile_QMARK_)?React.createElement("div",({"className": "navbar-center"}),(function (){var attrs46006 = mobile_title;
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46006))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["navbar-mobile-title"], null)], null),attrs46006], 0))):({"className": "navbar-mobile-title"})),((cljs.core.map_QMARK_(attrs46006))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46006)], null)));
})()):React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["navbar-center",(cljs.core.truth_(search_active)?"search-active":null)], null))}),sablono.interpreter.interpret((oc.web.components.search.search_box.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.search.search_box.cljs$core$IFn$_invoke$arity$0() : oc.web.components.search.search_box.call(null))))),(cljs.core.truth_(oc.web.lib.jwt.jwt())?React.createElement("div",({"className": "navbar-right group"}),React.createElement("div",({"className": "user-menu"}),React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["user-menu-button",(cljs.core.truth_(show_whats_new_green_dot)?"green-dot":null)], null))}),sablono.interpreter.interpret((function (){var G__46008 = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"click-cb","click-cb",1953404727),(function (){
return oc.web.actions.nav_sidebar.menu_toggle();
})], null);
return (oc.web.components.ui.user_avatar.user_avatar.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_avatar.user_avatar.cljs$core$IFn$_invoke$arity$1(G__46008) : oc.web.components.ui.user_avatar.user_avatar.call(null,G__46008));
})())))):(function (){var attrs46007 = (oc.web.components.ui.login_button.login_button.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.login_button.login_button.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.login_button.login_button.call(null));
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46007))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["navbar-right","anonymous-user"], null)], null),attrs46007], 0))):({"className": "navbar-right anonymous-user"})),((cljs.core.map_QMARK_(attrs46007))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46007)], null)));
})())))));
}),new cljs.core.PersistentVector(null, 10, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"navbar-data","navbar-data",2074703570)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"cmail-state","cmail-state",-747393321)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"show-add-post-tooltip","show-add-post-tooltip",1769173942)], 0)),oc.web.mixins.ui.render_on_resize(null),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.ui.navbar","throttled-scroll-check","oc.web.components.ui.navbar/throttled-scroll-check",1390114976)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.navbar","scrolled","oc.web.components.ui.navbar/scrolled",-2005795939)),oc.web.mixins.ui.on_window_scroll_mixin((function (s,e){
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.navbar","throttled-scroll-check","oc.web.components.ui.navbar/throttled-scroll-check",1390114976).cljs$core$IFn$_invoke$arity$1(s)))){
return cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.navbar","throttled-scroll-check","oc.web.components.ui.navbar/throttled-scroll-check",1390114976).cljs$core$IFn$_invoke$arity$1(s)).fire();
} else {
return null;
}
})),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([oc.web.stores.search.search_active_QMARK_], 0)),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.navbar","throttled-scroll-check","oc.web.components.ui.navbar/throttled-scroll-check",1390114976).cljs$core$IFn$_invoke$arity$1(s),(new goog.async.Throttle(cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.components.ui.navbar.check_scroll,s),(500))));

return s;
}),new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.navbar","throttled-scroll-check","oc.web.components.ui.navbar/throttled-scroll-check",1390114976).cljs$core$IFn$_invoke$arity$1(s)).fire();

return s;
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.navbar","throttled-scroll-check","oc.web.components.ui.navbar/throttled-scroll-check",1390114976).cljs$core$IFn$_invoke$arity$1(s)))){
cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.navbar","throttled-scroll-check","oc.web.components.ui.navbar/throttled-scroll-check",1390114976).cljs$core$IFn$_invoke$arity$1(s)).dispose();
} else {
}

return s;
})], null)], null),"navbar");

//# sourceMappingURL=oc.web.components.ui.navbar.js.map
