goog.provide('oc.web.components.org_settings_modal');
oc.web.components.org_settings_modal.close_clicked = (function oc$web$components$org_settings_modal$close_clicked(s,dismiss_action){
var org_editing = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915)));
if(cljs.core.truth_(new cljs.core.Keyword(null,"has-changes","has-changes",-631476764).cljs$core$IFn$_invoke$arity$1(org_editing))){
var alert_data = new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"icon","icon",1679606541),"/img/ML/trash.svg",new cljs.core.Keyword(null,"action","action",-811238024),"org-settings-unsaved-edits",new cljs.core.Keyword(null,"message","message",-406056002),"Leave without saving your changes?",new cljs.core.Keyword(null,"link-button-title","link-button-title",-812305976),"Stay",new cljs.core.Keyword(null,"link-button-cb","link-button-cb",559710301),(function (){
return oc.web.components.ui.alert_modal.hide_alert();
}),new cljs.core.Keyword(null,"solid-button-style","solid-button-style",-723792244),new cljs.core.Keyword(null,"red","red",-969428204),new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"Lose changes",new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),(function (){
oc.web.components.ui.alert_modal.hide_alert();

return (dismiss_action.cljs$core$IFn$_invoke$arity$0 ? dismiss_action.cljs$core$IFn$_invoke$arity$0() : dismiss_action.call(null));
})], null);
return oc.web.components.ui.alert_modal.show_alert(alert_data);
} else {
return (dismiss_action.cljs$core$IFn$_invoke$arity$0 ? dismiss_action.cljs$core$IFn$_invoke$arity$0() : dismiss_action.call(null));
}
});
oc.web.components.org_settings_modal.form_is_clean_QMARK_ = (function oc$web$components$org_settings_modal$form_is_clean_QMARK_(s){
var has_org_edit_changes = new cljs.core.Keyword(null,"has-changes","has-changes",-631476764).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915))));
var map__46774 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"org-settings-team-management","org-settings-team-management",-2085361788)));
var map__46774__$1 = (((((!((map__46774 == null))))?(((((map__46774.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46774.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46774):map__46774);
var um_domain_invite = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46774__$1,new cljs.core.Keyword(null,"um-domain-invite","um-domain-invite",-1217004114));
return ((cljs.core.not(has_org_edit_changes)) && (cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"domain","domain",1847214937).cljs$core$IFn$_invoke$arity$1(um_domain_invite))));
});
oc.web.components.org_settings_modal.reset_form = (function oc$web$components$org_settings_modal$reset_form(s){
var org_data = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"org-data","org-data",96720321)));
var um_domain_invite = new cljs.core.Keyword(null,"um-domain-invite","um-domain-invite",-1217004114).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"org-settings-team-management","org-settings-team-management",-2085361788))));
oc.web.actions.org.org_edit_setup(org_data);

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"um-domain-invite","um-domain-invite",-1217004114),new cljs.core.Keyword(null,"domain","domain",1847214937)], null),""], null));

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"add-email-domain-team-error","add-email-domain-team-error",-1479228464)], null),null], null));
});
oc.web.components.org_settings_modal.change_content_visibility = (function oc$web$components$org_settings_modal$change_content_visibility(content_visibility_data,k,v){
var new_content_visibility = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([content_visibility_data,cljs.core.PersistentArrayMap.createAsIfByAssoc([k,v])], 0));
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915)], null),(function (p1__46776_SHARP_){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__46776_SHARP_,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"has-changes","has-changes",-631476764),true,new cljs.core.Keyword(null,"content-visibility","content-visibility",550828155),new_content_visibility], null)], 0));
})], null));
});
oc.web.components.org_settings_modal.logo_on_load = (function oc$web$components$org_settings_modal$logo_on_load(org_avatar_editing,url,img){
oc.web.actions.org.org_avatar_edit_save(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"logo-url","logo-url",-1629105032),url], null));

return goog.dom.removeNode(img);
});
/**
 * Show an error alert view for failed uploads.
 */
oc.web.components.org_settings_modal.logo_add_error = (function oc$web$components$org_settings_modal$logo_add_error(img){
oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"title","title",636505583),"Image upload error",new cljs.core.Keyword(null,"description","description",-1428560544),"An error occurred while processing your company avatar. Please retry.",new cljs.core.Keyword(null,"expire","expire",-70657108),(3),new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"org-avatar-upload-failed","org-avatar-upload-failed",-1869436514),new cljs.core.Keyword(null,"dismiss","dismiss",412569545),true], null));

if(cljs.core.truth_(img)){
return goog.dom.removeNode(img);
} else {
return null;
}
});
oc.web.components.org_settings_modal.update_tooltip = (function oc$web$components$org_settings_modal$update_tooltip(s){
return oc.web.lib.utils.after((100),(function (){
var header_logo = rum.core.ref_node(s,"org-settings-header-logo");
var $header_logo = $(header_logo);
var org_avatar_editing = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"org-avatar-editing","org-avatar-editing",-1933353352)));
var title = ((cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"logo-url","logo-url",-1629105032).cljs$core$IFn$_invoke$arity$1(org_avatar_editing)))?"Add a logo":"Change logo");
return $header_logo.tooltip(({"title": title, "trigger": "hover focus", "position": "top", "container": "body"}));
}));
});
oc.web.components.org_settings_modal.logo_on_click = (function oc$web$components$org_settings_modal$logo_on_click(org_avatar_editing){
return oc.web.lib.image_upload.upload_BANG_(oc.web.utils.org.org_avatar_filestack_config,(function (res){
var url = goog.object.get(res,"url");
var img = goog.dom.createDom("img");
(img.onerror = (function (){
return oc.web.components.org_settings_modal.logo_add_error(img);
}));

(img.onload = (function (){
return oc.web.components.org_settings_modal.logo_on_load(org_avatar_editing,url,img);
}));

(img.className = "hidden");

goog.dom.append(document.body,img);

return (img.src = url);
}),null,(function (err){
return oc.web.components.org_settings_modal.logo_add_error(null);
}));
});
oc.web.components.org_settings_modal.org_settings_modal = rum.core.build_defcs((function (s){
var org_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"org-data","org-data",96720321));
var org_avatar_editing = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"org-avatar-editing","org-avatar-editing",-1933353352));
var org_data_for_avatar = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([org_data,org_avatar_editing], 0));
var org_editing = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915));
var is_tablet_or_mobile_QMARK_ = oc.web.lib.responsive.is_tablet_or_mobile_QMARK_();
var map__46785 = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"org-settings-team-management","org-settings-team-management",-2085361788));
var map__46785__$1 = (((((!((map__46785 == null))))?(((((map__46785.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46785.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46785):map__46785);
var team_management_data = map__46785__$1;
var query_params = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46785__$1,new cljs.core.Keyword(null,"query-params","query-params",900640534));
var um_domain_invite = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46785__$1,new cljs.core.Keyword(null,"um-domain-invite","um-domain-invite",-1217004114));
var add_email_domain_team_error = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46785__$1,new cljs.core.Keyword(null,"add-email-domain-team-error","add-email-domain-team-error",-1479228464));
var team_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46785__$1,new cljs.core.Keyword(null,"team-data","team-data",-732020079));
var content_visibility_data = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"content-visibility","content-visibility",550828155).cljs$core$IFn$_invoke$arity$1(org_editing);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.PersistentArrayMap.EMPTY;
}
})();
return React.createElement("div",({"className": "org-settings-modal"}),React.createElement("button",({"onClick": (function (){
return oc.web.components.org_settings_modal.close_clicked(s,oc.web.actions.nav_sidebar.close_all_panels);
}), "className": "mlb-reset modal-close-bt"})),React.createElement("div",({"className": "org-settings-modal-container"}),React.createElement("div",({"className": "org-settings-header"}),React.createElement("div",({"className": "org-settings-header-title"}),"Admin settings"),React.createElement("button",({"onClick": (function (){
if(cljs.core.compare_and_set_BANG_(new cljs.core.Keyword("oc.web.components.org-settings-modal","saving","oc.web.components.org-settings-modal/saving",1327315953).cljs$core$IFn$_invoke$arity$1(s),false,true)){
return oc.web.actions.org.org_edit_save(org_editing);
} else {
return null;
}
}), "disabled": (function (){var or__4126__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.org-settings-modal","saving","oc.web.components.org-settings-modal/saving",1327315953).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = new cljs.core.Keyword(null,"saved","saved",288760660).cljs$core$IFn$_invoke$arity$1(org_editing);
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
return ((cljs.core.not(cljs.core.seq(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(org_editing)))) || ((((clojure.string.trim(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(org_editing))).length) < (3))));
}
}
})(), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["mlb-reset","save-bt",(cljs.core.truth_(new cljs.core.Keyword(null,"saved","saved",288760660).cljs$core$IFn$_invoke$arity$1(org_editing))?"no-disable":null)], null))}),"Save"),React.createElement("button",({"onClick": (function (_){
return oc.web.components.org_settings_modal.close_clicked(s,(function (){
return oc.web.actions.nav_sidebar.show_org_settings(null);
}));
}), "className": "mlb-reset cancel-bt"}),"Back")),React.createElement("div",({"className": "org-settings-body"}),React.createElement("div",({"ref": "org-settings-header-logo", "onClick": oc.web.components.org_settings_modal.logo_on_click, "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["org-settings-header-avatar","group",oc.web.lib.utils.class_set(cljs.core.PersistentArrayMap.createAsIfByAssoc([new cljs.core.Keyword(null,"missing-logo","missing-logo",896340977),cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"logo-url","logo-url",-1629105032).cljs$core$IFn$_invoke$arity$1(org_avatar_editing)),oc.web.lib.utils.hide_class,true]))], null))}),sablono.interpreter.interpret((oc.web.components.ui.org_avatar.org_avatar.cljs$core$IFn$_invoke$arity$3 ? oc.web.components.ui.org_avatar.org_avatar.cljs$core$IFn$_invoke$arity$3(org_data_for_avatar,false,new cljs.core.Keyword(null,"never","never",50472977)) : oc.web.components.ui.org_avatar.org_avatar.call(null,org_data_for_avatar,false,new cljs.core.Keyword(null,"never","never",50472977)))),React.createElement("span",({"className": "edit-company-logo"}),"Edit company logo")),React.createElement("div",({"className": "org-settings-fields"}),React.createElement("div",({"className": "org-settings-label"}),"Company name"),sablono.interpreter.create_element("input",({"type": "text", "value": (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(org_editing);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "";
}
})(), "maxLength": oc.web.utils.org.org_name_max_length, "onChange": (function (p1__46779_SHARP_){
var org_name = p1__46779_SHARP_.target.value;
var clean_org_name = cljs.core.subs.cljs$core$IFn$_invoke$arity$3(org_name,(0),(function (){var x__4217__auto__ = cljs.core.count(org_name);
var y__4218__auto__ = oc.web.utils.org.org_name_max_length;
return ((x__4217__auto__ < y__4218__auto__) ? x__4217__auto__ : y__4218__auto__);
})());
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915)], null),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([org_editing,new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"name","name",1843675177),clean_org_name,new cljs.core.Keyword(null,"has-changes","has-changes",-631476764),true,new cljs.core.Keyword(null,"error","error",-978969032),false,new cljs.core.Keyword(null,"rand","rand",908504774),cljs.core.rand.cljs$core$IFn$_invoke$arity$1((1000))], null)], 0))], null));
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["org-settings-field","oc-input",(cljs.core.truth_(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(org_editing))?"error":null)], null))})),sablono.interpreter.interpret((cljs.core.truth_(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(org_editing))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.error","div.error",314336058),"Must be between 3 and 50 characters"], null):null)),sablono.interpreter.interpret((oc.web.components.ui.email_domains.email_domains.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.email_domains.email_domains.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.email_domains.email_domains.call(null)))),sablono.interpreter.interpret(((cljs.core.not(cljs.core.deref(new cljs.core.Keyword("oc.web.components.org-settings-modal","show-advanced-settings","oc.web.components.org-settings-modal/show-advanced-settings",1516524351).cljs$core$IFn$_invoke$arity$1(s))))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.org-settings-advanced","div.org-settings-advanced",1433197232),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.advanced-settings-bt","button.mlb-reset.advanced-settings-bt",1842897303),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (_){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.org-settings-modal","show-advanced-settings","oc.web.components.org-settings-modal/show-advanced-settings",1516524351).cljs$core$IFn$_invoke$arity$1(s),true);

return oc.web.lib.utils.after((1000),(function (){
return $("[data-toggle=\"tooltip\"]").tooltip();
}));
})], null),"Show advanced settings"], null)], null):new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.org-settings-advanced","div.org-settings-advanced",1433197232),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.org-settings-advanced-title","div.org-settings-advanced-title",-1851960748),"Advanced settings"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.org-settings-advanced-row.digest-links.group","div.org-settings-advanced-row.digest-links.group",-1438582934),(function (){var G__46801 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"selected","selected",574897764),new cljs.core.Keyword(null,"disallow-secure-links","disallow-secure-links",-1225374283).cljs$core$IFn$_invoke$arity$1(content_visibility_data),new cljs.core.Keyword(null,"disabled","disabled",-1529784218),false,new cljs.core.Keyword(null,"did-change-cb","did-change-cb",116554135),(function (p1__46780_SHARP_){
return oc.web.components.org_settings_modal.change_content_visibility(content_visibility_data,new cljs.core.Keyword(null,"disallow-secure-links","disallow-secure-links",-1225374283),p1__46780_SHARP_);
})], null);
return (oc.web.components.ui.carrot_checkbox.carrot_checkbox.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.carrot_checkbox.carrot_checkbox.cljs$core$IFn$_invoke$arity$1(G__46801) : oc.web.components.ui.carrot_checkbox.carrot_checkbox.call(null,G__46801));
})(),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.checkbox-label","div.checkbox-label",-870128947),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),(cljs.core.truth_(new cljs.core.Keyword(null,"disallow-secure-links","disallow-secure-links",-1225374283).cljs$core$IFn$_invoke$arity$1(content_visibility_data))?null:"unselected"),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.components.org_settings_modal.change_content_visibility(content_visibility_data,new cljs.core.Keyword(null,"disallow-secure-links","disallow-secure-links",-1225374283),cljs.core.not(new cljs.core.Keyword(null,"disallow-secure-links","disallow-secure-links",-1225374283).cljs$core$IFn$_invoke$arity$1(content_visibility_data)));
})], null),"Do not allow secure links to open posts from email or Slack",new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"i.mdi.mdi-information-outline","i.mdi.mdi-information-outline",-1933132410),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"title","title",636505583),["When team members receive Wut posts via an email or Slack morning digest, secure ","links allow them to read the post without first logging in. A login is still required ","to access additional posts. If you turn off secure links, your team will always need to ","be logged in to view posts."].join(''),new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687),((is_tablet_or_mobile_QMARK_)?null:"tooltip"),new cljs.core.Keyword(null,"data-placement","data-placement",166529525),"top",new cljs.core.Keyword(null,"data-container","data-container",1473653353),"body"], null)], null)], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.org-settings-advanced-row.public-sections.group","div.org-settings-advanced-row.public-sections.group",1759744174),(function (){var G__46804 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"selected","selected",574897764),new cljs.core.Keyword(null,"disallow-public-board","disallow-public-board",-716419746).cljs$core$IFn$_invoke$arity$1(content_visibility_data),new cljs.core.Keyword(null,"disabled","disabled",-1529784218),false,new cljs.core.Keyword(null,"did-change-cb","did-change-cb",116554135),(function (p1__46781_SHARP_){
return oc.web.components.org_settings_modal.change_content_visibility(content_visibility_data,new cljs.core.Keyword(null,"disallow-public-board","disallow-public-board",-716419746),p1__46781_SHARP_);
})], null);
return (oc.web.components.ui.carrot_checkbox.carrot_checkbox.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.carrot_checkbox.carrot_checkbox.cljs$core$IFn$_invoke$arity$1(G__46804) : oc.web.components.ui.carrot_checkbox.carrot_checkbox.call(null,G__46804));
})()], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.org-settings-advanced-row.public-share.group","div.org-settings-advanced-row.public-share.group",-1641883662),(function (){var G__46805 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"selected","selected",574897764),new cljs.core.Keyword(null,"disallow-public-share","disallow-public-share",-1943877372).cljs$core$IFn$_invoke$arity$1(content_visibility_data),new cljs.core.Keyword(null,"disabled","disabled",-1529784218),false,new cljs.core.Keyword(null,"did-change-cb","did-change-cb",116554135),(function (p1__46782_SHARP_){
return oc.web.components.org_settings_modal.change_content_visibility(content_visibility_data,new cljs.core.Keyword(null,"disallow-public-share","disallow-public-share",-1943877372),p1__46782_SHARP_);
})], null);
return (oc.web.components.ui.carrot_checkbox.carrot_checkbox.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.carrot_checkbox.carrot_checkbox.cljs$core$IFn$_invoke$arity$1(G__46805) : oc.web.components.ui.carrot_checkbox.carrot_checkbox.call(null,G__46805));
})(),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.checkbox-label","div.checkbox-label",-870128947),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),(cljs.core.truth_(new cljs.core.Keyword(null,"disallow-public-share","disallow-public-share",-1943877372).cljs$core$IFn$_invoke$arity$1(content_visibility_data))?null:"unselected"),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.components.org_settings_modal.change_content_visibility(content_visibility_data,new cljs.core.Keyword(null,"disallow-public-share","disallow-public-share",-1943877372),cljs.core.not(new cljs.core.Keyword(null,"disallow-public-share","disallow-public-share",-1943877372).cljs$core$IFn$_invoke$arity$1(content_visibility_data)));
})], null),"Do not allow public share links"], null)], null)], null))))));
}),new cljs.core.PersistentVector(null, 10, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"org-data","org-data",96720321)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"team-data","team-data",-732020079)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"org-avatar-editing","org-avatar-editing",-1933353352)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"org-settings-team-management","org-settings-team-management",-2085361788)], 0)),oc.web.mixins.ui.refresh_tooltips_mixin,rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.org-settings-modal","saving","oc.web.components.org-settings-modal/saving",1327315953)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.org-settings-modal","show-advanced-settings","oc.web.components.org-settings-modal/show-advanced-settings",1516524351)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
var org_data_46810 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"org-data","org-data",96720321)));
oc.web.actions.org.get_org.cljs$core$IFn$_invoke$arity$2(org_data_46810,true);

oc.web.components.org_settings_modal.reset_form(s);

var content_visibility_data_46811 = new cljs.core.Keyword(null,"content-visibility","content-visibility",550828155).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"org-data","org-data",96720321))));
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.org-settings-modal","show-advanced-settings","oc.web.components.org-settings-modal/show-advanced-settings",1516524351).cljs$core$IFn$_invoke$arity$1(s),cljs.core.some((function (p1__46777_SHARP_){
return (content_visibility_data_46811.cljs$core$IFn$_invoke$arity$1 ? content_visibility_data_46811.cljs$core$IFn$_invoke$arity$1(p1__46777_SHARP_) : content_visibility_data_46811.call(null,p1__46777_SHARP_));
}),cljs.core.keys(content_visibility_data_46811)));

return s;
}),new cljs.core.Keyword(null,"will-update","will-update",328062998),(function (s){
var org_editing_46812 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915)));
if(cljs.core.truth_((function (){var and__4115__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.org-settings-modal","saving","oc.web.components.org-settings-modal/saving",1327315953).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.contains_QMARK_(org_editing_46812,new cljs.core.Keyword(null,"saved","saved",288760660));
} else {
return and__4115__auto__;
}
})())){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.org-settings-modal","saving","oc.web.components.org-settings-modal/saving",1327315953).cljs$core$IFn$_invoke$arity$1(s),false);

oc.web.lib.utils.after((2500),(function (_){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update","update",1045576396),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915)], null),(function (p1__46778_SHARP_){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(p1__46778_SHARP_,new cljs.core.Keyword(null,"saved","saved",288760660));
})], null));
}));

oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"title","title",636505583),(cljs.core.truth_(new cljs.core.Keyword(null,"saved","saved",288760660).cljs$core$IFn$_invoke$arity$1(org_editing_46812))?"Settings saved":"Error saving, please retry"),new cljs.core.Keyword(null,"primary-bt-title","primary-bt-title",653140150),"OK",new cljs.core.Keyword(null,"primary-bt-dismiss","primary-bt-dismiss",-820688058),true,new cljs.core.Keyword(null,"expire","expire",-70657108),(3),new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"org-settings-saved","org-settings-saved",-24203284)], null));
} else {
}

return s;
})], null)], null),"org-settings-modal");

//# sourceMappingURL=oc.web.components.org_settings_modal.js.map
