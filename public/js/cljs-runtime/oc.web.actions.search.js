goog.provide('oc.web.actions.search');
oc.web.actions.search.query_finished = (function oc$web$actions$search$query_finished(result){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("search-query","finish","search-query/finish",49456799),result], null));
});
oc.web.actions.search.reset = (function oc$web$actions$search$reset(){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"search-reset","search-reset",1085204484)], null));
});
oc.web.actions.search.inactive = (function oc$web$actions$search$inactive(){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"search-inactive","search-inactive",-2004281816)], null));
});
oc.web.actions.search.active = (function oc$web$actions$search$active(){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"search-active","search-active",913672682)], null));
});
oc.web.actions.search.search_history_cookie = ["search-history-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(oc.web.lib.jwt.user_id())].join('');
oc.web.actions.search.search_history_length = (5);
oc.web.actions.search.search_history = (function oc$web$actions$search$search_history(){
var res = oc.web.lib.cookies.get_cookie(oc.web.actions.search.search_history_cookie);
if(cljs.core.seq(res)){
return cljs.core.vec(cljs.core.js__GT_clj.cljs$core$IFn$_invoke$arity$1($.parseJSON(res)));
} else {
return cljs.core.PersistentHashSet.EMPTY;
}
});
/**
 * Use the search service to query for results.
 * Keep tracl of the last 
 */
oc.web.actions.search.query = (function oc$web$actions$search$query(search_query,auto_search_QMARK_){
var trimmed_query = oc.web.lib.utils.trim(search_query);
if(cljs.core.seq(trimmed_query)){
if(((cljs.core.not(auto_search_QMARK_)) || ((cljs.core.count(trimmed_query) > (2))))){
var temp_history_44368 = oc.web.lib.utils.vec_dissoc(oc.web.actions.search.search_history(),trimmed_query);
var last_search_44369 = cljs.core.last(temp_history_44368);
var temp_history_STAR__44370 = (cljs.core.truth_((function (){var and__4115__auto__ = auto_search_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(last_search_44369,cuerdas.core.join.cljs$core$IFn$_invoke$arity$2("",cljs.core.rest(trimmed_query)))) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(last_search_44369,cuerdas.core.join.cljs$core$IFn$_invoke$arity$2("",cljs.core.butlast(trimmed_query)))) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(trimmed_query,cuerdas.core.join.cljs$core$IFn$_invoke$arity$2("",cljs.core.rest(last_search_44369)))) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(trimmed_query,cuerdas.core.join.cljs$core$IFn$_invoke$arity$2("",cljs.core.butlast(last_search_44369)))));
} else {
return and__4115__auto__;
}
})())?cljs.core.butlast(temp_history_44368):temp_history_44368);
var with_new_44371 = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(temp_history_STAR__44370,trimmed_query);
var history_44372 = JSON.stringify(cljs.core.clj__GT_js(cljs.core.take_last(oc.web.actions.search.search_history_length,with_new_44371)));
oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$3(oc.web.actions.search.search_history_cookie,history_44372,oc.web.lib.cookies.default_cookie_expire);
} else {
}

oc.web.actions.search.active();

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("search-query","start","search-query/start",284734800),trimmed_query], null));

return oc.web.api.query(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0()),trimmed_query,oc.web.actions.search.query_finished);
} else {
return oc.web.actions.search.reset();
}
});
oc.web.actions.search.result_clicked = (function oc$web$actions$search$result_clicked(entry_result,url){
var post_loaded_QMARK_ = oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_result));
var open_post_cb = (function (success,status){
if(cljs.core.truth_(success)){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"search-result-clicked","search-result-clicked",-570975869)], null));

return oc.web.lib.utils.after((10),oc.web.router.nav_BANG_(url));
} else {
var is_404_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,(404));
var alert_data = new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"icon","icon",1679606541),"/img/ML/trash.svg",new cljs.core.Keyword(null,"action","action",-811238024),"search-result-load-failed",new cljs.core.Keyword(null,"title","title",636505583),((is_404_QMARK_)?"Post moved or deleted":"An error occurred"),new cljs.core.Keyword(null,"message","message",-406056002),((is_404_QMARK_)?"The selected update was moved to another team or deleted.":"An error occurred while loading the selected post. Please try again."),new cljs.core.Keyword(null,"solid-button-style","solid-button-style",-723792244),new cljs.core.Keyword(null,"red","red",-969428204),new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),"Ok",new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),oc.web.components.ui.alert_modal.hide_alert], null);
return oc.web.components.ui.alert_modal.show_alert(alert_data);
}
});
if(cljs.core.truth_(post_loaded_QMARK_)){
return open_post_cb(true,null);
} else {
return oc.web.actions.cmail.get_entry_with_uuid.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(entry_result),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_result),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([open_post_cb], 0));
}
});

//# sourceMappingURL=oc.web.actions.search.js.map
