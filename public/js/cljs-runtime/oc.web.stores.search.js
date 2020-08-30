goog.provide('oc.web.stores.search');
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.stores !== 'undefined') && (typeof oc.web.stores.search !== 'undefined') && (typeof oc.web.stores.search.search_limit !== 'undefined')){
} else {
oc.web.stores.search.search_limit = (20);
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.stores !== 'undefined') && (typeof oc.web.stores.search !== 'undefined') && (typeof oc.web.stores.search.savedsearch !== 'undefined')){
} else {
oc.web.stores.search.savedsearch = cljs.core.atom.cljs$core$IFn$_invoke$arity$1("");
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.stores !== 'undefined') && (typeof oc.web.stores.search !== 'undefined') && (typeof oc.web.stores.search.search_key !== 'undefined')){
} else {
oc.web.stores.search.search_key = new cljs.core.Keyword(null,"search-results","search-results",306464634);
}
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.stores !== 'undefined') && (typeof oc.web.stores.search !== 'undefined') && (typeof oc.web.stores.search.search_active_QMARK_ !== 'undefined')){
} else {
oc.web.stores.search.search_active_QMARK_ = new cljs.core.Keyword(null,"search-active","search-active",913672682);
}
oc.web.stores.search.search_results = (function oc$web$stores$search$search_results(){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(oc.web.dispatcher.app_state),oc.web.stores.search.search_key);
});
oc.web.stores.search.saved_search = (function oc$web$stores$search$saved_search(){
var tmp = cljs.core.deref(oc.web.stores.search.savedsearch);
cljs.core.reset_BANG_(oc.web.stores.search.savedsearch,"");

return tmp;
});
oc.web.stores.search.should_display = (function oc$web$stores$search$should_display(){

if(cljs.core.not(oc.web.lib.jwt.jwt())){
return false;
} else {
if(cljs.core.truth_(new cljs.core.Keyword(null,"member?","member?",486668360).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0()))){
return true;
} else {
return false;
}
}
});
oc.web.stores.search.board_slug_with_uuid = (function oc$web$stores$search$board_slug_with_uuid(board_uuid){
var temp__5735__auto__ = oc.web.utils.activity.board_by_uuid(board_uuid);
if(cljs.core.truth_(temp__5735__auto__)){
var uuid_board = temp__5735__auto__;
return new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(uuid_board);
} else {
return null;
}
});
oc.web.stores.search.cleanup_result = (function oc$web$stores$search$cleanup_result(result){
var source = new cljs.core.Keyword(null,"_source","_source",-812884485).cljs$core$IFn$_invoke$arity$1(result);
var fixed_entry_uuid = clojure.string.replace(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(source),"entry-","");
var fixed_board_slug = ((cljs.core.seq(new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(source)))?new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(source):((cljs.core.seq(new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127).cljs$core$IFn$_invoke$arity$1(source)))?oc.web.stores.search.board_slug_with_uuid(new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127).cljs$core$IFn$_invoke$arity$1(source)):null
));
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(result,new cljs.core.Keyword(null,"_source","_source",-812884485),cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(source,new cljs.core.Keyword(null,"uuid","uuid",-2145095719),fixed_entry_uuid),new cljs.core.Keyword(null,"board-slug","board-slug",99003663),fixed_board_slug));
});
/**
 * Entries have the uuid in this format: entry-0000-0000-0000-0000
 * replace those with only the UUID and make sure they have a board-slug,
 * if the slug is missing replace it with the board-uuid.
 * Finally filter out all the results that still don't have a board-slug.
 */
oc.web.stores.search.cleanup_results = (function oc$web$stores$search$cleanup_results(results){
return cljs.core.filterv((function (p1__43502_SHARP_){
return cljs.core.seq(new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"_source","_source",-812884485).cljs$core$IFn$_invoke$arity$1(p1__43502_SHARP_)));
}),cljs.core.mapv.cljs$core$IFn$_invoke$arity$2(oc.web.stores.search.cleanup_result,results));
});
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("search-query","start","search-query/start",284734800),(function (db,p__43503){
var vec__43504 = p__43503;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43504,(0),null);
var search_query = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43504,(1),null);
return cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [oc.web.stores.search.search_key,new cljs.core.Keyword(null,"loading","loading",-737050189)], null),true);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("search-query","finish","search-query/finish",49456799),(function (db,p__43510){
var vec__43511 = p__43510;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43511,(0),null);
var map__43514 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43511,(1),null);
var map__43514__$1 = (((((!((map__43514 == null))))?(((((map__43514.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43514.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43514):map__43514);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43514__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var error = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43514__$1,new cljs.core.Keyword(null,"error","error",-978969032));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43514__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var query = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43514__$1,new cljs.core.Keyword(null,"query","query",-1288509510));
var total_hits = ((cljs.core.map_QMARK_(new cljs.core.Keyword(null,"total","total",1916810418).cljs$core$IFn$_invoke$arity$1(body)))?new cljs.core.Keyword(null,"value","value",305978217).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"total","total",1916810418).cljs$core$IFn$_invoke$arity$1(body)):new cljs.core.Keyword(null,"total","total",1916810418).cljs$core$IFn$_invoke$arity$1(body));
var results = cljs.core.vec(cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2((function (p1__43507_SHARP_){
return new cljs.core.Keyword(null,"created-at","created-at",-89248644).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"_source","_source",-812884485).cljs$core$IFn$_invoke$arity$1(p1__43507_SHARP_));
}),new cljs.core.Keyword(null,"hits","hits",-2120002930).cljs$core$IFn$_invoke$arity$1(body)));
if(cljs.core.truth_(success)){
cljs.core.reset_BANG_(oc.web.stores.search.savedsearch,query);
} else {
}

if(cljs.core.truth_(success)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,oc.web.stores.search.search_key,new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"count","count",2139924085),total_hits,new cljs.core.Keyword(null,"loading","loading",-737050189),false,new cljs.core.Keyword(null,"results","results",-1134170113),oc.web.stores.search.cleanup_results(results),new cljs.core.Keyword(null,"query","query",-1288509510),query], null));
} else {
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,oc.web.stores.search.search_key,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"failed","failed",-1397425762),true,new cljs.core.Keyword(null,"loading","loading",-737050189),false,new cljs.core.Keyword(null,"query","query",-1288509510),query], null));
}
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"search-active","search-active",913672682),(function (db,p__43521){
var vec__43522 = p__43521;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43522,(0),null);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,oc.web.stores.search.search_active_QMARK_,true);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"search-inactive","search-inactive",-2004281816),(function (db,p__43525){
var vec__43526 = p__43525;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43526,(0),null);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,oc.web.stores.search.search_active_QMARK_,false);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"search-reset","search-reset",1085204484),(function (db,p__43533){
var vec__43534 = p__43533;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43534,(0),null);
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(db,oc.web.stores.search.search_key);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"search-result-clicked","search-result-clicked",-570975869),(function (db,p__43541){
var vec__43542 = p__43541;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43542,(0),null);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,oc.web.stores.search.search_active_QMARK_,false);
}));

//# sourceMappingURL=oc.web.stores.search.js.map
