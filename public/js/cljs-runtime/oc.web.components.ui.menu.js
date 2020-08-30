goog.provide('oc.web.components.ui.menu');
oc.web.components.ui.menu.menu_close = (function oc$web$components$ui$menu$menu_close(var_args){
var args__4742__auto__ = [];
var len__4736__auto___45911 = arguments.length;
var i__4737__auto___45912 = (0);
while(true){
if((i__4737__auto___45912 < len__4736__auto___45911)){
args__4742__auto__.push((arguments[i__4737__auto___45912]));

var G__45913 = (i__4737__auto___45912 + (1));
i__4737__auto___45912 = G__45913;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.components.ui.menu.menu_close.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.components.ui.menu.menu_close.cljs$core$IFn$_invoke$arity$variadic = (function (p__45853){
var vec__45854 = p__45853;
var s = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__45854,(0),null);
return oc.web.actions.nav_sidebar.menu_close();
}));

(oc.web.components.ui.menu.menu_close.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.components.ui.menu.menu_close.cljs$lang$applyTo = (function (seq45852){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq45852));
}));

oc.web.components.ui.menu.logout_click = (function oc$web$components$ui$menu$logout_click(s,e){
e.preventDefault();

oc.web.components.ui.menu.menu_close.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([s], 0));

return oc.web.actions.jwt.logout.cljs$core$IFn$_invoke$arity$0();
});
oc.web.components.ui.menu.profile_edit_click = (function oc$web$components$ui$menu$profile_edit_click(s,e){
e.preventDefault();

return oc.web.actions.nav_sidebar.show_user_settings(new cljs.core.Keyword(null,"profile","profile",-545963874));
});
oc.web.components.ui.menu.my_profile = (function oc$web$components$ui$menu$my_profile(s,cur_user_id,e){
e.preventDefault();

return oc.web.actions.nav_sidebar.nav_to_author_BANG_.cljs$core$IFn$_invoke$arity$3(e,cur_user_id,oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$1(cur_user_id));
});
oc.web.components.ui.menu.my_posts_click = (function oc$web$components$ui$menu$my_posts_click(s,cur_user_id,e){
e.preventDefault();

oc.web.components.ui.menu.menu_close.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([s], 0));

return oc.web.actions.nav_sidebar.nav_to_author_BANG_.cljs$core$IFn$_invoke$arity$3(e,cur_user_id,oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$1(cur_user_id));
});
oc.web.components.ui.menu.notifications_settings_click = (function oc$web$components$ui$menu$notifications_settings_click(s,e){
e.preventDefault();

return oc.web.actions.nav_sidebar.show_user_settings(new cljs.core.Keyword(null,"notifications","notifications",1685638001));
});
oc.web.components.ui.menu.team_settings_click = (function oc$web$components$ui$menu$team_settings_click(s,e){
e.preventDefault();

return oc.web.actions.nav_sidebar.show_org_settings(new cljs.core.Keyword(null,"org","org",1495985));
});
oc.web.components.ui.menu.manage_team_click = (function oc$web$components$ui$menu$manage_team_click(s,e){
e.preventDefault();

return oc.web.actions.nav_sidebar.show_org_settings(new cljs.core.Keyword(null,"team","team",1355747699));
});
oc.web.components.ui.menu.invite_team_click = (function oc$web$components$ui$menu$invite_team_click(s,e){
e.preventDefault();

return oc.web.actions.nav_sidebar.show_org_settings(new cljs.core.Keyword(null,"invite-picker","invite-picker",1426151962));
});
oc.web.components.ui.menu.integrations_click = (function oc$web$components$ui$menu$integrations_click(s,e){
e.preventDefault();

return oc.web.actions.nav_sidebar.show_org_settings(new cljs.core.Keyword(null,"integrations","integrations",1844532423));
});
oc.web.components.ui.menu.sign_in_sign_up_click = (function oc$web$components$ui$menu$sign_in_sign_up_click(s,e){
oc.web.components.ui.menu.menu_close.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([s], 0));

e.preventDefault();

return oc.web.actions.user.show_login(new cljs.core.Keyword(null,"login-with-slack","login-with-slack",1915911677));
});
oc.web.components.ui.menu.whats_new_click = (function oc$web$components$ui$menu$whats_new_click(s,e){
e.preventDefault();

return oc.web.lib.whats_new.show();
});
oc.web.components.ui.menu.reminders_click = (function oc$web$components$ui$menu$reminders_click(s,e){
e.preventDefault();

return oc.web.actions.nav_sidebar.show_reminders();
});
oc.web.components.ui.menu.payments_click = (function oc$web$components$ui$menu$payments_click(e){
e.preventDefault();

return oc.web.actions.nav_sidebar.show_org_settings(new cljs.core.Keyword(null,"payments","payments",-1324138047));
});
oc.web.components.ui.menu.detect_desktop_app = (function oc$web$components$ui$menu$detect_desktop_app(){
if(oc.shared.useragent.desktop_app_QMARK_){
return null;
} else {
if(cljs.core.truth_(oc.shared.useragent.mac_QMARK_)){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"title","title",636505583),"Download Mac app",new cljs.core.Keyword(null,"href","href",-793805698),"https://github.com/open-company/open-company-web/releases/latest/download/Carrot.dmg"], null);
} else {
if(cljs.core.truth_(oc.shared.useragent.windows_QMARK_)){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"title","title",636505583),"Download Windows app",new cljs.core.Keyword(null,"href","href",-793805698),"https://github.com/open-company/open-company-web/releases/latest/download/Carrot.exe"], null);
} else {
return null;

}
}
}
});
oc.web.components.ui.menu.get_desktop_version = (function oc$web$components$ui$menu$get_desktop_version(){
if(cljs.core.truth_(OCCarrotDesktop.getElectronAppVersion)){
return ["Version ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(OCCarrotDesktop.getElectronAppVersion())].join('');
} else {
return "";
}
});
oc.web.components.ui.menu.theme_settings_click = (function oc$web$components$ui$menu$theme_settings_click(s,e){
e.preventDefault();

return oc.web.actions.nav_sidebar.show_theme_settings();
});
oc.web.components.ui.menu.menu = rum.core.build_defcs((function (s){
var map__45879 = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"navbar-data","navbar-data",2074703570));
var map__45879__$1 = (((((!((map__45879 == null))))?(((((map__45879.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45879.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45879):map__45879);
var panel_stack = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45879__$1,new cljs.core.Keyword(null,"panel-stack","panel-stack",1084180004));
var org_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45879__$1,new cljs.core.Keyword(null,"org-data","org-data",96720321));
var board_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45879__$1,new cljs.core.Keyword(null,"board-data","board-data",1372958925));
var current_org_slug = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45879__$1,new cljs.core.Keyword(null,"current-org-slug","current-org-slug",1185011374));
var current_user_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915));
var is_mobile_QMARK_ = oc.web.lib.responsive.is_mobile_size_QMARK_();
var show_reminders_QMARK_ = ((oc.web.local_settings.reminders_enabled_QMARK_)?oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"reminders"], 0)):null);
var expanded_user_menu = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.last(panel_stack),new cljs.core.Keyword(null,"menu","menu",352255198));
var is_admin_or_author_QMARK_ = (function (){var G__45888 = new cljs.core.Keyword(null,"role","role",-736691072).cljs$core$IFn$_invoke$arity$1(current_user_data);
var fexpr__45887 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"admin","admin",-1239101627),null,new cljs.core.Keyword(null,"author","author",2111686192),null], null), null);
return (fexpr__45887.cljs$core$IFn$_invoke$arity$1 ? fexpr__45887.cljs$core$IFn$_invoke$arity$1(G__45888) : fexpr__45887.call(null,G__45888));
})();
var expo_app_version = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"expo-app-version","expo-app-version",-1636851181));
var show_invite_people_QMARK_ = (function (){var and__4115__auto__ = current_org_slug;
if(cljs.core.truth_(and__4115__auto__)){
return is_admin_or_author_QMARK_;
} else {
return and__4115__auto__;
}
})();
var desktop_app_data = oc.web.components.ui.menu.detect_desktop_app();
var app_version = ((oc.shared.useragent.mobile_app_QMARK_)?["Version ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(expo_app_version)].join(''):((oc.shared.useragent.desktop_app_QMARK_)?oc.web.components.ui.menu.get_desktop_version():""
));
var show_billing_QMARK_ = ((oc.web.local_settings.payments_enabled)?((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"role","role",-736691072).cljs$core$IFn$_invoke$arity$1(current_user_data),new cljs.core.Keyword(null,"admin","admin",-1239101627)))?current_org_slug:false):false);
return React.createElement("div",({"onClick": (function (p1__45871_SHARP_){
if(cljs.core.truth_(oc.web.lib.utils.event_inside_QMARK_(p1__45871_SHARP_,rum.core.ref_node(s,new cljs.core.Keyword(null,"menu-container","menu-container",-2027612799))))){
return null;
} else {
return oc.web.components.ui.menu.menu_close.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([s], 0));
}
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["menu",oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"expanded-user-menu","expanded-user-menu",-1187915345),expanded_user_menu], null))], null))}),React.createElement("button",({"onClick": (function (){
return oc.web.components.ui.menu.menu_close.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([s], 0));
}), "className": "mlb-reset modal-close-bt"})),React.createElement("div",({"ref": "menu-container", "className": "menu-container"}),React.createElement("div",({"className": "menu-header group"}),React.createElement("button",({"onClick": (function (){
return oc.web.components.ui.menu.menu_close.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([s], 0));
}), "className": "mlb-reset mobile-close-bt"})),sablono.interpreter.interpret((cljs.core.truth_(is_mobile_QMARK_)?(oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1(current_user_data) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,current_user_data)):null)),React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["user-name",oc.web.lib.utils.hide_class], null))}),sablono.interpreter.interpret([cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"first-name","first-name",-1559982131)))," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"last-name","last-name",-1695738974)))].join(''))),sablono.interpreter.interpret((cljs.core.truth_(is_mobile_QMARK_)?null:(oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1(current_user_data) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,current_user_data))))),sablono.interpreter.interpret((cljs.core.truth_((function (){var and__4115__auto__ = oc.web.lib.jwt.jwt();
if(cljs.core.truth_(and__4115__auto__)){
return current_user_data;
} else {
return and__4115__auto__;
}
})())?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"href","href",-793805698),"#",new cljs.core.Keyword(null,"on-click","on-click",1632826543),cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.components.ui.menu.profile_edit_click,s)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.oc-menu-item.personal-profile","div.oc-menu-item.personal-profile",1795645938),"My profile"], null)], null):null)),sablono.interpreter.interpret((cljs.core.truth_((function (){var and__4115__auto__ = oc.web.lib.jwt.jwt();
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(is_mobile_QMARK_);
} else {
return and__4115__auto__;
}
})())?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"href","href",-793805698),"#",new cljs.core.Keyword(null,"on-click","on-click",1632826543),cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.components.ui.menu.notifications_settings_click,s)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.oc-menu-item.notifications-settings","div.oc-menu-item.notifications-settings",17447169),"Notifications"], null)], null):null)),sablono.interpreter.interpret((cljs.core.truth_(oc.web.lib.jwt.jwt())?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.oc-menu-separator","div.oc-menu-separator",74383214)], null):null)),React.createElement("a",({"href": "#", "onClick": cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.components.ui.menu.theme_settings_click,s)}),"Theme"),sablono.interpreter.interpret((cljs.core.truth_((function (){var and__4115__auto__ = show_reminders_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(is_mobile_QMARK_);
} else {
return and__4115__auto__;
}
})())?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.oc-menu-separator","div.oc-menu-separator",74383214)], null):null)),sablono.interpreter.interpret((cljs.core.truth_((function (){var and__4115__auto__ = show_reminders_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(is_mobile_QMARK_);
} else {
return and__4115__auto__;
}
})())?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"href","href",-793805698),"#",new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (p1__45872_SHARP_){
return oc.web.components.ui.menu.reminders_click(s,p1__45872_SHARP_);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.oc-menu-item.reminders","div.oc-menu-item.reminders",-1987443313),"Recurring updates"], null)], null):null)),sablono.interpreter.interpret((cljs.core.truth_(((cljs.core.not(is_mobile_QMARK_))?(function (){var or__4126__auto__ = current_org_slug;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = show_invite_people_QMARK_;
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
return show_billing_QMARK_;
}
}
})():false))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.oc-menu-separator","div.oc-menu-separator",74383214)], null):null)),sablono.interpreter.interpret((cljs.core.truth_(((cljs.core.not(is_mobile_QMARK_))?((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"role","role",-736691072).cljs$core$IFn$_invoke$arity$1(current_user_data),new cljs.core.Keyword(null,"admin","admin",-1239101627)))?current_org_slug:false):false))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"href","href",-793805698),"#",new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (p1__45873_SHARP_){
return oc.web.components.ui.menu.team_settings_click(s,p1__45873_SHARP_);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.oc-menu-item.digest-settings","div.oc-menu-item.digest-settings",47008651),"Admin settings"], null)], null):null)),sablono.interpreter.interpret((cljs.core.truth_(((cljs.core.not(is_mobile_QMARK_))?show_invite_people_QMARK_:false))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"href","href",-793805698),"#",new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (p1__45874_SHARP_){
return oc.web.components.ui.menu.invite_team_click(s,p1__45874_SHARP_);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.oc-menu-item.invite-team","div.oc-menu-item.invite-team",2014747674),"Invite people"], null)], null):null)),sablono.interpreter.interpret((cljs.core.truth_(((cljs.core.not(is_mobile_QMARK_))?current_org_slug:false))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"href","href",-793805698),"#",new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (p1__45875_SHARP_){
return oc.web.components.ui.menu.manage_team_click(s,p1__45875_SHARP_);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.oc-menu-item.manage-team","div.oc-menu-item.manage-team",-60488181),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"role","role",-736691072).cljs$core$IFn$_invoke$arity$1(current_user_data),new cljs.core.Keyword(null,"admin","admin",-1239101627)))?"Manage team":"View team")], null)], null):null)),sablono.interpreter.interpret((cljs.core.truth_(((cljs.core.not(is_mobile_QMARK_))?(function (){var and__4115__auto__ = current_org_slug;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"role","role",-736691072).cljs$core$IFn$_invoke$arity$1(current_user_data),new cljs.core.Keyword(null,"admin","admin",-1239101627));
} else {
return and__4115__auto__;
}
})():false))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"href","href",-793805698),"#",new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (p1__45876_SHARP_){
return oc.web.components.ui.menu.integrations_click(s,p1__45876_SHARP_);
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.oc-menu-item.team-integrations","div.oc-menu-item.team-integrations",1932626447),"Integrations"], null)], null):null)),sablono.interpreter.interpret((cljs.core.truth_(((cljs.core.not(is_mobile_QMARK_))?show_billing_QMARK_:false))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a.payments","a.payments",780605302),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"href","href",-793805698),"#",new cljs.core.Keyword(null,"on-click","on-click",1632826543),oc.web.components.ui.menu.payments_click], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.oc-menu-item","div.oc-menu-item",573452428),"Billing"], null)], null):null)),React.createElement("div",({"className": "oc-menu-separator"})),(function (){var attrs45891 = (cljs.core.truth_(oc.shared.useragent.mobile_QMARK_)?new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"href","href",-793805698),oc.web.urls.what_s_new,new cljs.core.Keyword(null,"target","target",253001721),"_blank"], null):new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.components.ui.menu.whats_new_click,s)], null));
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"a",((cljs.core.map_QMARK_(attrs45891))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["whats-new-link"], null)], null),attrs45891], 0))):({"className": "whats-new-link"})),((cljs.core.map_QMARK_(attrs45891))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [React.createElement("div",({"className": "oc-menu-item whats-new"}),"What\u2019s new")], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs45891),React.createElement("div",({"className": "oc-menu-item whats-new"}),"What\u2019s new")], null)));
})(),React.createElement("a",({"href": oc.web.urls.contact_mail_to, "className": "intercom-chat-link"}),React.createElement("div",({"className": "oc-menu-item support"}),"Get support")),sablono.interpreter.interpret((cljs.core.truth_((function (){var and__4115__auto__ = is_mobile_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return show_billing_QMARK_;
} else {
return and__4115__auto__;
}
})())?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a.payments","a.payments",780605302),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"href","href",-793805698),"#",new cljs.core.Keyword(null,"on-click","on-click",1632826543),oc.web.components.ui.menu.payments_click], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.oc-menu-item","div.oc-menu-item",573452428),"Billing"], null)], null):null)),sablono.interpreter.interpret((cljs.core.truth_(desktop_app_data)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"a","a",-2123407586),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"href","href",-793805698),new cljs.core.Keyword(null,"href","href",-793805698).cljs$core$IFn$_invoke$arity$1(desktop_app_data),new cljs.core.Keyword(null,"target","target",253001721),"_blank"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.oc-menu-item.native-app","div.oc-menu-item.native-app",-1023138513),new cljs.core.Keyword(null,"title","title",636505583).cljs$core$IFn$_invoke$arity$1(desktop_app_data),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.beta","span.beta",-875928593),"BETA"], null)], null)], null):null)),React.createElement("div",({"className": "oc-menu-separator"})),(cljs.core.truth_(oc.web.lib.jwt.jwt())?React.createElement("a",({"href": oc.web.urls.logout, "onClick": cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.components.ui.menu.logout_click,s), "className": "sign-out"}),React.createElement("div",({"className": "oc-menu-item logout"}),"Sign out")):React.createElement("a",({"href": "", "onClick": cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.components.ui.menu.sign_in_sign_up_click,s)}),React.createElement("div",({"className": "oc-menu-item"}),"Sign in / Sign up"))),sablono.interpreter.interpret(((oc.shared.useragent.pseudo_native_QMARK_)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.oc-menu-separator","div.oc-menu-separator",74383214)], null):null)),sablono.interpreter.interpret(((oc.shared.useragent.pseudo_native_QMARK_)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.oc-menu-item.app-version","div.oc-menu-item.app-version",-806890496),app_version], null):null))));
}),new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"navbar-data","navbar-data",2074703570)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"expo-app-version","expo-app-version",-1636851181)], 0)),oc.web.mixins.ui.refresh_tooltips_mixin,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
if(cljs.core.truth_(oc.web.lib.responsive.is_mobile_size_QMARK_())){
oc.web.lib.whats_new.check_whats_new_badge();
} else {
}

return s;
}),new cljs.core.Keyword(null,"did-remount","did-remount",1362550500),(function (_,s){
if(cljs.core.truth_(oc.web.lib.responsive.is_mobile_size_QMARK_())){
oc.web.lib.whats_new.check_whats_new_badge();
} else {
}

return s;
})], null)], null),"menu");

//# sourceMappingURL=oc.web.components.ui.menu.js.map
