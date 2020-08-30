goog.provide('oc.web.components.ui.email_domains');
oc.web.components.ui.email_domains.email_domain_removed = (function oc$web$components$ui$email_domains$email_domain_removed(success_QMARK_){
return oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"title","title",636505583),(cljs.core.truth_(success_QMARK_)?"Email domain successfully removed":"Error"),new cljs.core.Keyword(null,"description","description",-1428560544),(cljs.core.truth_(success_QMARK_)?null:"An error occurred while removing the email domain, please try again."),new cljs.core.Keyword(null,"expire","expire",-70657108),(3),new cljs.core.Keyword(null,"id","id",-1388402092),(cljs.core.truth_(success_QMARK_)?new cljs.core.Keyword(null,"email-domain-remove-success","email-domain-remove-success",-693596800):new cljs.core.Keyword(null,"email-domain-remove-error","email-domain-remove-error",-1231972074)),new cljs.core.Keyword(null,"dismiss","dismiss",412569545),true], null));
});
oc.web.components.ui.email_domains.email_domain_added = (function oc$web$components$ui$email_domains$email_domain_added(success_QMARK_){
return oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"title","title",636505583),(cljs.core.truth_(success_QMARK_)?"Email domain successfully added":"Error"),new cljs.core.Keyword(null,"description","description",-1428560544),(cljs.core.truth_(success_QMARK_)?null:"An error occurred while adding the email domain, please try again."),new cljs.core.Keyword(null,"expire","expire",-70657108),(3),new cljs.core.Keyword(null,"id","id",-1388402092),(cljs.core.truth_(success_QMARK_)?new cljs.core.Keyword(null,"email-domain-add-success","email-domain-add-success",669034638):new cljs.core.Keyword(null,"email-domain-add-error","email-domain-add-error",942029061)),new cljs.core.Keyword(null,"dismiss","dismiss",412569545),true], null));
});
oc.web.components.ui.email_domains.remove_email_domain_prompt = (function oc$web$components$ui$email_domains$remove_email_domain_prompt(domain){
var alert_data = new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"icon","icon",1679606541),"/img/ML/trash.svg",new cljs.core.Keyword(null,"action","action",-811238024),"org-email-domain-remove",new cljs.core.Keyword(null,"message","message",-406056002),"Are you sure you want to remove this email domain?",new cljs.core.Keyword(null,"link-button-title","link-button-title",-812305976),"Keep",new cljs.core.Keyword(null,"link-button-cb","link-button-cb",559710301),(function (){
return oc.web.components.ui.alert_modal.hide_alert();
}),new cljs.core.Keyword(null,"solid-button-style","solid-button-style",-723792244),new cljs.core.Keyword(null,"red","red",-969428204),new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"Yes",new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),(function (){
oc.web.components.ui.alert_modal.hide_alert();

return oc.web.actions.team.remove_team.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(domain),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([oc.web.components.ui.email_domains.email_domain_removed], 0));
})], null);
return oc.web.components.ui.alert_modal.show_alert(alert_data);
});
oc.web.components.ui.email_domains.email_domains = rum.core.build_defcs((function (s){
var map__46764 = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"org-settings-team-management","org-settings-team-management",-2085361788));
var map__46764__$1 = (((((!((map__46764 == null))))?(((((map__46764.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46764.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46764):map__46764);
var um_domain_invite = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46764__$1,new cljs.core.Keyword(null,"um-domain-invite","um-domain-invite",-1217004114));
var team_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46764__$1,new cljs.core.Keyword(null,"team-data","team-data",-732020079));
var is_mobile_QMARK_ = oc.web.lib.responsive.is_mobile_size_QMARK_();
return React.createElement("div",({"className": "email-domain-container"}),React.createElement("div",({"className": "email-domain-title"}),"Allowed email domains",React.createElement("i",({"title": "Any user that signs up with an allowed email domain and verifies their email address will have contributor access to your team.", "data-toggle": (cljs.core.truth_(is_mobile_QMARK_)?null:"tooltip"), "data-placement": "top", "data-container": "body", "className": "mdi mdi-information-outline"}))),React.createElement("div",({"className": "email-domain-field-container oc-input group"}),sablono.interpreter.create_element("input",({"type": "text", "placeholder": "@domain.com", "autoCapitalize": "none", "value": (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"domain","domain",1847214937).cljs$core$IFn$_invoke$arity$1(um_domain_invite);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "";
}
})(), "pattern": "@?[a-z0-9.-]+\\.[a-z]{2,4}$", "onChange": (function (p1__46763_SHARP_){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"um-domain-invite","um-domain-invite",-1217004114),new cljs.core.Keyword(null,"domain","domain",1847214937)], null),p1__46763_SHARP_.target.value], null));
}), "onKeyPress": (function (e){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(e.key,"Enter")){
var domain = new cljs.core.Keyword(null,"domain","domain",1847214937).cljs$core$IFn$_invoke$arity$1(um_domain_invite);
if(cljs.core.truth_(oc.web.lib.utils.valid_domain_QMARK_(domain))){
return oc.web.actions.team.email_domain_team_add(domain,oc.web.components.ui.email_domains.email_domain_added);
} else {
return null;
}
} else {
return null;
}
}), "className": "email-domain-field"})),React.createElement("button",({"disabled": cljs.core.not(oc.web.lib.utils.valid_domain_QMARK_(new cljs.core.Keyword(null,"domain","domain",1847214937).cljs$core$IFn$_invoke$arity$1(um_domain_invite))), "onClick": (function (e){
var domain = new cljs.core.Keyword(null,"domain","domain",1847214937).cljs$core$IFn$_invoke$arity$1(um_domain_invite);
if(cljs.core.truth_(oc.web.lib.utils.valid_domain_QMARK_(domain))){
return oc.web.actions.team.email_domain_team_add(domain,oc.web.components.ui.email_domains.email_domain_added);
} else {
return null;
}
}), "className": "mlb-reset add-email-domain-bt"}),"Add")),React.createElement("div",({"className": "email-domain-list"}),cljs.core.into_array.cljs$core$IFn$_invoke$arity$1((function (){var iter__4529__auto__ = (function oc$web$components$ui$email_domains$iter__46766(s__46767){
return (new cljs.core.LazySeq(null,(function (){
var s__46767__$1 = s__46767;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__46767__$1);
if(temp__5735__auto__){
var s__46767__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__46767__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__46767__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__46769 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__46768 = (0);
while(true){
if((i__46768 < size__4528__auto__)){
var domain = cljs.core._nth(c__4527__auto__,i__46768);
cljs.core.chunk_append(b__46769,React.createElement("div",({"key": ["org-settings-domain-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(domain)].join(''), "className": "email-domain-row"}),sablono.interpreter.interpret(["@",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"domain","domain",1847214937).cljs$core$IFn$_invoke$arity$1(domain))].join('')),sablono.interpreter.interpret((cljs.core.truth_(oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(domain),"remove"], 0)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.remove-email-bt","button.mlb-reset.remove-email-bt",1409549870),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (i__46768,domain,c__4527__auto__,size__4528__auto__,b__46769,s__46767__$2,temp__5735__auto__,map__46764,map__46764__$1,um_domain_invite,team_data,is_mobile_QMARK_){
return (function (){
return oc.web.components.ui.email_domains.remove_email_domain_prompt(domain);
});})(i__46768,domain,c__4527__auto__,size__4528__auto__,b__46769,s__46767__$2,temp__5735__auto__,map__46764,map__46764__$1,um_domain_invite,team_data,is_mobile_QMARK_))
], null),"Remove"], null):null))));

var G__46770 = (i__46768 + (1));
i__46768 = G__46770;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__46769),oc$web$components$ui$email_domains$iter__46766(cljs.core.chunk_rest(s__46767__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__46769),null);
}
} else {
var domain = cljs.core.first(s__46767__$2);
return cljs.core.cons(React.createElement("div",({"key": ["org-settings-domain-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(domain)].join(''), "className": "email-domain-row"}),sablono.interpreter.interpret(["@",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"domain","domain",1847214937).cljs$core$IFn$_invoke$arity$1(domain))].join('')),sablono.interpreter.interpret((cljs.core.truth_(oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(domain),"remove"], 0)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.remove-email-bt","button.mlb-reset.remove-email-bt",1409549870),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (domain,s__46767__$2,temp__5735__auto__,map__46764,map__46764__$1,um_domain_invite,team_data,is_mobile_QMARK_){
return (function (){
return oc.web.components.ui.email_domains.remove_email_domain_prompt(domain);
});})(domain,s__46767__$2,temp__5735__auto__,map__46764,map__46764__$1,um_domain_invite,team_data,is_mobile_QMARK_))
], null),"Remove"], null):null))),oc$web$components$ui$email_domains$iter__46766(cljs.core.rest(s__46767__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(new cljs.core.Keyword(null,"email-domains","email-domains",437428384).cljs$core$IFn$_invoke$arity$1(team_data));
})())));
}),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"org-settings-team-management","org-settings-team-management",-2085361788)], 0)),oc.web.mixins.ui.refresh_tooltips_mixin], null),"email-domains");

//# sourceMappingURL=oc.web.components.ui.email_domains.js.map
