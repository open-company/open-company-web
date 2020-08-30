goog.provide('oc.web.components.ui.activity_share');
oc.web.components.ui.activity_share.dismiss = (function oc$web$components$ui$activity_share$dismiss(){
return oc.web.actions.activity.activity_share_hide();
});
oc.web.components.ui.activity_share.close_clicked = (function oc$web$components$ui$activity_share$close_clicked(s){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.activity-share","dismiss","oc.web.components.ui.activity-share/dismiss",-863539934).cljs$core$IFn$_invoke$arity$1(s),true);

return oc.web.lib.utils.after((180),oc.web.components.ui.activity_share.dismiss);
});
/**
 * Check if the current team has a bot associated.
 */
oc.web.components.ui.activity_share.has_slack_bot_QMARK_ = (function oc$web$components$ui$activity_share$has_slack_bot_QMARK_(org_data){
return oc.web.lib.jwt.team_has_bot_QMARK_(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(org_data));
});
/**
 * Select the whole content of the share link filed.
 */
oc.web.components.ui.activity_share.highlight_url = (function oc$web$components$ui$activity_share$highlight_url(s){
var temp__5735__auto__ = rum.core.ref_node(s,"activity-share-url-field");
if(cljs.core.truth_(temp__5735__auto__)){
var url_field = temp__5735__auto__;
return url_field.select();
} else {
return null;
}
});
oc.web.components.ui.activity_share.activity_share = rum.core.build_defcs((function (s){
var activity_data = new cljs.core.Keyword(null,"share-data","share-data",1810949431).cljs$core$IFn$_invoke$arity$1(org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"activity-share","activity-share",-127339936)));
var org_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"org-data","org-data",96720321));
var slack_data = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.activity-share","slack-data","oc.web.components.ui.activity-share/slack-data",1596868248).cljs$core$IFn$_invoke$arity$1(s));
var secure_uuid = new cljs.core.Keyword(null,"secure-uuid","secure-uuid",-1972075067).cljs$core$IFn$_invoke$arity$1(activity_data);
var _ = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"activity-shared-data","activity-shared-data",-674728784));
var is_mobile_QMARK_ = oc.web.lib.responsive.is_tablet_or_mobile_QMARK_();
var medium = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"activity-share-medium","activity-share-medium",866045149));
var has_bot_QMARK_ = oc.web.components.ui.activity_share.has_slack_bot_QMARK_(org_data);
var can_share_to_slack_QMARK_ = (((!(is_mobile_QMARK_)))?has_bot_QMARK_:false);
var disallow_public_share_QMARK_ = (function (){var and__4115__auto__ = new cljs.core.Keyword(null,"content-visibility","content-visibility",550828155).cljs$core$IFn$_invoke$arity$1(org_data);
if(cljs.core.truth_(and__4115__auto__)){
return new cljs.core.Keyword(null,"disallow-public-share","disallow-public-share",-1943877372).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"content-visibility","content-visibility",550828155).cljs$core$IFn$_invoke$arity$1(org_data));
} else {
return and__4115__auto__;
}
})();
return React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["activity-share-modal-container",oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"will-appear","will-appear",579342096),(function (){var or__4126__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.activity-share","dismiss","oc.web.components.ui.activity-share/dismiss",-863539934).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.not(cljs.core.deref(new cljs.core.Keyword(null,"first-render-done","first-render-done",1105112667).cljs$core$IFn$_invoke$arity$1(s)));
}
})(),new cljs.core.Keyword(null,"appear","appear",440543877),((cljs.core.not(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.activity-share","dismiss","oc.web.components.ui.activity-share/dismiss",-863539934).cljs$core$IFn$_invoke$arity$1(s))))?cljs.core.deref(new cljs.core.Keyword(null,"first-render-done","first-render-done",1105112667).cljs$core$IFn$_invoke$arity$1(s)):false)], null))], null))}),React.createElement("div",({"className": "activity-share-modal"}),(function (){var attrs46351 = ((is_mobile_QMARK_)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mobile-modal-close-bt.mlb-reset","button.mobile-modal-close-bt.mlb-reset",911996906),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.components.ui.activity_share.close_clicked(s);
})], null)], null):null);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46351))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["activity-share-main-cta"], null)], null),attrs46351], 0))):({"className": "activity-share-main-cta"})),((cljs.core.map_QMARK_(attrs46351))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Share post"], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46351),"Share post"], null)));
})(),sablono.interpreter.interpret(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(medium,new cljs.core.Keyword(null,"url","url",276297046)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.activity-share-modal-shared.group","div.activity-share-modal-shared.group",-336250564),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"form","form",-1624062471),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-submit","on-submit",1227871159),(function (p1__46347_SHARP_){
return oc.web.lib.utils.event_stop(p1__46347_SHARP_);
})], null),(cljs.core.truth_(disallow_public_share_QMARK_)?null:new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.medium-row.group","div.medium-row.group",-827021847),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.fields","div.fields",1362682190),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.checkbox-row","button.mlb-reset.checkbox-row",1385973019),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (___$1){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("oc.web.components.ui.activity-share","url-audience","oc.web.components.ui.activity-share/url-audience",1010765285).cljs$core$IFn$_invoke$arity$1(s),(function (p1__46348_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(p1__46348_SHARP_,new cljs.core.Keyword(null,"team","team",1355747699))){
return new cljs.core.Keyword(null,"all","all",892129742);
} else {
return new cljs.core.Keyword(null,"team","team",1355747699);
}
}));
})], null),(function (){var G__46352 = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"selected","selected",574897764),cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.activity-share","url-audience","oc.web.components.ui.activity-share/url-audience",1010765285).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"team","team",1355747699))], null);
return (oc.web.components.ui.carrot_option_button.carrot_option_button.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.carrot_option_button.carrot_option_button.cljs$core$IFn$_invoke$arity$1(G__46352) : oc.web.components.ui.carrot_option_button.carrot_option_button.call(null,G__46352));
})(),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.checkbox-label","div.checkbox-label",-870128947),"Require authentication"], null)], null),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.checkbox-row","button.mlb-reset.checkbox-row",1385973019),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (___$1){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("oc.web.components.ui.activity-share","url-audience","oc.web.components.ui.activity-share/url-audience",1010765285).cljs$core$IFn$_invoke$arity$1(s),(function (p1__46349_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(p1__46349_SHARP_,new cljs.core.Keyword(null,"all","all",892129742))){
return new cljs.core.Keyword(null,"team","team",1355747699);
} else {
return new cljs.core.Keyword(null,"all","all",892129742);
}
}));
})], null),(function (){var G__46353 = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"selected","selected",574897764),cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.activity-share","url-audience","oc.web.components.ui.activity-share/url-audience",1010765285).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"all","all",892129742))], null);
return (oc.web.components.ui.carrot_option_button.carrot_option_button.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.carrot_option_button.carrot_option_button.cljs$core$IFn$_invoke$arity$1(G__46353) : oc.web.components.ui.carrot_option_button.carrot_option_button.call(null,G__46353));
})(),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.checkbox-label","div.checkbox-label",-870128947),"Public (anyone with this link)"], null)], null)], null)], null)),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.medium-row.group","div.medium-row.group",-827021847),(function (){var url_protocol = ["http",((oc.web.local_settings.jwt_cookie_secure)?"s":null),"://"].join('');
var secure_url = oc.web.urls.secure_activity.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),secure_uuid);
var post_url = oc.web.urls.entry.cljs$core$IFn$_invoke$arity$3(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(activity_data),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));
var share_url = [url_protocol,oc.web.local_settings.web_server,((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.activity-share","url-audience","oc.web.components.ui.activity-share/url-audience",1010765285).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"team","team",1355747699)))?post_url:secure_url)].join('');
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.shared-url-container.group","div.shared-url-container.group",1072122545),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input.oc-input","input.oc-input",-1802295716),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"value","value",305978217),share_url,new cljs.core.Keyword(null,"key","key",-1516042587),share_url,new cljs.core.Keyword(null,"read-only","read-only",-191706886),true,new cljs.core.Keyword(null,"content-editable","content-editable",636764967),false,new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.components.ui.activity_share.highlight_url(s);
}),new cljs.core.Keyword(null,"ref","ref",1289896967),"activity-share-url-field"], null)], null)], null);
})(),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.copy-btn","button.mlb-reset.copy-btn",533889742),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"ref","ref",1289896967),"activity-share-url-copy-btn",new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (e){
oc.web.lib.utils.event_stop(e);

var url_input = rum.core.ref_node(s,"activity-share-url-field");
oc.web.components.ui.activity_share.highlight_url(s);

var copied_QMARK_ = oc.web.lib.utils.copy_to_clipboard(url_input);
return oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"title","title",636505583),(cljs.core.truth_(copied_QMARK_)?"Share URL copied to clipboard":"Error copying the share URL"),new cljs.core.Keyword(null,"description","description",-1428560544),(cljs.core.truth_(copied_QMARK_)?null:"Please try copying the URL manually"),new cljs.core.Keyword(null,"primary-bt-title","primary-bt-title",653140150),"OK",new cljs.core.Keyword(null,"primary-bt-dismiss","primary-bt-dismiss",-820688058),true,new cljs.core.Keyword(null,"primary-bt-inline","primary-bt-inline",-796141614),copied_QMARK_,new cljs.core.Keyword(null,"expire","expire",-70657108),(3),new cljs.core.Keyword(null,"id","id",-1388402092),(cljs.core.truth_(copied_QMARK_)?new cljs.core.Keyword(null,"share-url-copied","share-url-copied",2070754526):new cljs.core.Keyword(null,"share-url-copy-error","share-url-copy-error",-1024812768))], null));
})], null),"Copy URL"], null)], null)], null)], null):null)),sablono.interpreter.interpret(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(medium,new cljs.core.Keyword(null,"slack","slack",-2083685671)))?new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.activity-share-share","div.activity-share-share",1262470984),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.hide_class], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.mediums-box","div.mediums-box",-148595707),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.medium","div.medium",2101019995),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.slack-medium.group","div.slack-medium.group",1031646659),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.medium-row.group","div.medium-row.group",-827021847),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.labels","div.labels",39026236),"Send post to"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.fields","div.fields",1362682190),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),(cljs.core.truth_(new cljs.core.Keyword(null,"channel-error","channel-error",1047221440).cljs$core$IFn$_invoke$arity$1(slack_data))?"error":null),new cljs.core.Keyword(null,"key","key",-1516042587),["slack-share-channels-dropdown-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.activity-share","slack-channels-dropdown-key","oc.web.components.ui.activity-share/slack-channels-dropdown-key",-158256696).cljs$core$IFn$_invoke$arity$1(s)))].join('')], null),(function (){var G__46354 = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"on-change","on-change",-732046149),(function (team,channel){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.activity-share","slack-data","oc.web.components.ui.activity-share/slack-data",1596868248).cljs$core$IFn$_invoke$arity$1(s),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([slack_data,cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([slack_data,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"channel","channel",734187692),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"channel-id","channel-id",138191095),new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(channel),new cljs.core.Keyword(null,"channel-name","channel-name",-188505362),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(channel),new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561),new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(team)], null),new cljs.core.Keyword(null,"channel-error","channel-error",1047221440),false], null)], 0))], 0)));
}),new cljs.core.Keyword(null,"on-intermediate-change","on-intermediate-change",-1144231725),(function (___$1){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.activity-share","slack-data","oc.web.components.ui.activity-share/slack-data",1596868248).cljs$core$IFn$_invoke$arity$1(s),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([slack_data,cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([slack_data,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"channel-error","channel-error",1047221440),false], null)], 0))], 0)));
}),new cljs.core.Keyword(null,"initial-value","initial-value",470619381),"",new cljs.core.Keyword(null,"disabled","disabled",-1529784218),false], null);
return (oc.web.components.ui.slack_channels_dropdown.slack_channels_dropdown.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.slack_channels_dropdown.slack_channels_dropdown.cljs$core$IFn$_invoke$arity$1(G__46354) : oc.web.components.ui.slack_channels_dropdown.slack_channels_dropdown.call(null,G__46354));
})()], null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.medium-row.note.group","div.medium-row.note.group",-1567888503),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.labels","div.labels",39026236),"Personal note"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.fields","div.fields",1362682190),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"textarea","textarea",-650375824),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"value","value",305978217),new cljs.core.Keyword(null,"note","note",1426297904).cljs$core$IFn$_invoke$arity$1(slack_data),new cljs.core.Keyword(null,"on-change","on-change",-732046149),(function (e){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.activity-share","slack-data","oc.web.components.ui.activity-share/slack-data",1596868248).cljs$core$IFn$_invoke$arity$1(s),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([slack_data,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"note","note",1426297904),e.target.value], null)], 0)));
})], null)], null)], null)], null)], null)], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.share-footer.group","div.share-footer.group",-807033304),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.right-buttons","div.right-buttons",-414621288),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.mlb-black-link","button.mlb-reset.mlb-black-link",-1930733508),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.components.ui.activity_share.close_clicked(s);
})], null),"Cancel"], null),(function (){var send_bt_disabled_QMARK_ = (function (){var or__4126__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.activity-share","sharing","oc.web.components.ui.activity-share/sharing",-1538011448).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"channel","channel",734187692).cljs$core$IFn$_invoke$arity$1(slack_data));
}
})();
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.share-button","button.mlb-reset.share-button",1780280021),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (___$1){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.activity-share","sharing","oc.web.components.ui.activity-share/sharing",-1538011448).cljs$core$IFn$_invoke$arity$1(s),true);

var slack_share = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"medium","medium",-1864319384),new cljs.core.Keyword(null,"slack","slack",-2083685671),new cljs.core.Keyword(null,"note","note",1426297904),new cljs.core.Keyword(null,"note","note",1426297904).cljs$core$IFn$_invoke$arity$1(slack_data),new cljs.core.Keyword(null,"channel","channel",734187692),new cljs.core.Keyword(null,"channel","channel",734187692).cljs$core$IFn$_invoke$arity$1(slack_data)], null);
return oc.web.actions.activity.activity_share(activity_data,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [slack_share], null));
}),new cljs.core.Keyword(null,"disabled","disabled",-1529784218),send_bt_disabled_QMARK_], null),(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.activity-share","shared","oc.web.components.ui.activity-share/shared",-1257668064).cljs$core$IFn$_invoke$arity$1(s)))?((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.activity-share","shared","oc.web.components.ui.activity-share/shared",-1257668064).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"shared","shared",-384145993)))?"Sent!":"Ops..."):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.activity-share","sharing","oc.web.components.ui.activity-share/sharing",-1538011448).cljs$core$IFn$_invoke$arity$1(s)))?(oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.small_loading.small_loading.call(null)):null),"Send"], null))], null);
})()], null)], null)], null):null))));
}),new cljs.core.PersistentVector(null, 15, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"org-data","org-data",96720321)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"activity-share","activity-share",-127339936)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"activity-shared-data","activity-shared-data",-674728784)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"activity-share-medium","activity-share-medium",866045149)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"note","note",1426297904),""], null),new cljs.core.Keyword("oc.web.components.ui.activity-share","slack-data","oc.web.components.ui.activity-share/slack-data",1596868248)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.activity-share","dismiss","oc.web.components.ui.activity-share/dismiss",-863539934)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.activity-share","sharing","oc.web.components.ui.activity-share/sharing",-1538011448)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.activity-share","shared","oc.web.components.ui.activity-share/shared",-1257668064)),rum.core.local.cljs$core$IFn$_invoke$arity$2(cljs.core.rand.cljs$core$IFn$_invoke$arity$1((1000)),new cljs.core.Keyword("oc.web.components.ui.activity-share","slack-channels-dropdown-key","oc.web.components.ui.activity-share/slack-channels-dropdown-key",-158256696)),rum.core.local.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"team","team",1355747699),new cljs.core.Keyword("oc.web.components.ui.activity-share","url-audience","oc.web.components.ui.activity-share/url-audience",1010765285)),oc.web.mixins.ui.first_render_mixin,oc.web.mixins.ui.on_click_out.cljs$core$IFn$_invoke$arity$1((function (p1__46346_SHARP_){
return oc.web.components.ui.activity_share.close_clicked(p1__46346_SHARP_);
})),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
oc.web.actions.team.teams_get_if_needed();

var activity_data_46356 = new cljs.core.Keyword(null,"share-data","share-data",1810949431).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"activity-share","activity-share",-127339936))));
var org_data_46357 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"org-data","org-data",96720321)));
var subject_46358 = $("<div />").html(new cljs.core.Keyword(null,"headline","headline",-157157727).cljs$core$IFn$_invoke$arity$1(activity_data_46356)).text();
if(cljs.core.truth_(((cljs.core.not(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"activity-share-medium","activity-share-medium",866045149)))))?oc.web.components.ui.activity_share.has_slack_bot_QMARK_(org_data_46357):false))){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"activity-share-medium","activity-share-medium",866045149)], null),new cljs.core.Keyword(null,"slack","slack",-2083685671)], null));
} else {
}

return s;
}),new cljs.core.Keyword(null,"did-update","did-update",-2143702256),(function (s){
var temp__5735__auto___46359 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"activity-shared-data","activity-shared-data",-674728784)));
if(cljs.core.truth_(temp__5735__auto___46359)){
var shared_data_46360 = temp__5735__auto___46359;
if(cljs.core.compare_and_set_BANG_(new cljs.core.Keyword("oc.web.components.ui.activity-share","sharing","oc.web.components.ui.activity-share/sharing",-1538011448).cljs$core$IFn$_invoke$arity$1(s),true,false)){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.activity-share","shared","oc.web.components.ui.activity-share/shared",-1257668064).cljs$core$IFn$_invoke$arity$1(s),(cljs.core.truth_(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(shared_data_46360))?new cljs.core.Keyword(null,"error","error",-978969032):new cljs.core.Keyword(null,"shared","shared",-384145993)));

if(cljs.core.truth_(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(shared_data_46360))){
} else {
var medium_46361 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"activity-share-medium","activity-share-medium",866045149)));
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(medium_46361,new cljs.core.Keyword(null,"slack","slack",-2083685671))){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.activity-share","slack-channels-dropdown-key","oc.web.components.ui.activity-share/slack-channels-dropdown-key",-158256696).cljs$core$IFn$_invoke$arity$1(s),cljs.core.rand.cljs$core$IFn$_invoke$arity$1((1000)));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.activity-share","slack-data","oc.web.components.ui.activity-share/slack-data",1596868248).cljs$core$IFn$_invoke$arity$1(s),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"note","note",1426297904),""], null));
} else {
}
}

oc.web.lib.utils.after((2000),(function (){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.activity-share","shared","oc.web.components.ui.activity-share/shared",-1257668064).cljs$core$IFn$_invoke$arity$1(s),false);
}));
} else {
}

oc.web.actions.activity.activity_share_reset();
} else {
}

return s;
})], null)], null),"activity-share");

//# sourceMappingURL=oc.web.components.ui.activity_share.js.map
