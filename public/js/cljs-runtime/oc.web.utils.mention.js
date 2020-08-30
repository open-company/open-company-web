goog.provide('oc.web.utils.mention');
oc.web.utils.mention.mention_ext = (function oc$web$utils$mention$mention_ext(users_list){
var mention_props = new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"tagName","tagName",1235959275),"span",new cljs.core.Keyword(null,"extraPanelClassName","extraPanelClassName",-868658054),"oc-mention-panel",new cljs.core.Keyword(null,"extraTriggerClassNameMap","extraTriggerClassNameMap",438839852),new cljs.core.PersistentArrayMap(null, 1, ["@","oc-mention"], null),new cljs.core.Keyword(null,"renderPanelContent","renderPanelContent",1380590632),(function (panel_el,current_mention_text,select_mention_callback){
return ReactDOM.render(React.createElement(CustomizedTagComponent,cljs.core.clj__GT_js(new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"currentMentionText","currentMentionText",1100942431),current_mention_text,new cljs.core.Keyword(null,"users","users",-713552705),cljs.core.clj__GT_js(users_list),new cljs.core.Keyword(null,"selectMentionCallback","selectMentionCallback",-96172139),select_mention_callback], null))),panel_el);
}),new cljs.core.Keyword(null,"activeTriggerList","activeTriggerList",718217064),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["@"], null)], null);
return (new TCMention(cljs.core.clj__GT_js(mention_props)));
});
oc.web.utils.mention.get_slack_usernames = (function oc$web$utils$mention$get_slack_usernames(user){
var slack_display_name = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"slack-display-name","slack-display-name",-1387558465).cljs$core$IFn$_invoke$arity$1(user)], null);
var slack_users_usernames = cljs.core.vec(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"display-name","display-name",694513143),cljs.core.vals(new cljs.core.Keyword(null,"slack-users","slack-users",-434149941).cljs$core$IFn$_invoke$arity$1(user))));
return cljs.core.remove.cljs$core$IFn$_invoke$arity$2((function (p1__48197_SHARP_){
return (((p1__48197_SHARP_ == null)) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(p1__48197_SHARP_,"-")));
}),cljs.core.concat.cljs$core$IFn$_invoke$arity$2(slack_users_usernames,slack_display_name));
});
oc.web.utils.mention.compact_slack_usernames = (function oc$web$utils$mention$compact_slack_usernames(users){
return cljs.core.doall.cljs$core$IFn$_invoke$arity$1(cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__48198_SHARP_){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__48198_SHARP_,new cljs.core.Keyword(null,"slack-usernames","slack-usernames",-1586678774),oc.web.utils.mention.get_slack_usernames(p1__48198_SHARP_));
}),users));
});
oc.web.utils.mention.users_for_mentions = (function oc$web$utils$mention$users_for_mentions(users){
var fixed_users = ((cljs.core.map_QMARK_(users))?cljs.core.vals(users):users);
return oc.web.utils.mention.compact_slack_usernames(cljs.core.filterv((function (p1__48199_SHARP_){
return ((cljs.core.seq(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__48199_SHARP_))) && (((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(p1__48199_SHARP_),"active")) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(p1__48199_SHARP_),"unverified")))));
}),fixed_users));
});

//# sourceMappingURL=oc.web.utils.mention.js.map
