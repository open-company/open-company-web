goog.provide('oc.web.components.search');
oc.web.components.search.entry_display = rum.core.build_defcs((function (s,data){
var result = new cljs.core.Keyword(null,"_source","_source",-812884485).cljs$core$IFn$_invoke$arity$1(data);
var title = oc.web.lib.utils.emojify(new cljs.core.Keyword(null,"headline","headline",-157157727).cljs$core$IFn$_invoke$arity$1(result));
var author = cljs.core.first(new cljs.core.Keyword(null,"author-name","author-name",1603882305).cljs$core$IFn$_invoke$arity$1(result));
var activity_url = oc.web.urls.entry.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(result),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(result));
return React.createElement("div",({"onClick": (function (s__$1){
oc.web.actions.search.result_clicked(result,activity_url);

return s__$1;
}), "className": "search-result"}),(function (){var attrs38430 = (function (){var G__38433 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"user-id","user-id",-206822291),cljs.core.first(new cljs.core.Keyword(null,"author-id","author-id",807115351).cljs$core$IFn$_invoke$arity$1(result)),new cljs.core.Keyword(null,"name","name",1843675177),author,new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103),cljs.core.first(new cljs.core.Keyword(null,"author-url","author-url",1091920533).cljs$core$IFn$_invoke$arity$1(result))], null);
return (oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1(G__38433) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,G__38433));
})();
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs38430))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["search-result-box"], null)], null),attrs38430], 0))):({"className": "search-result-box"})),((cljs.core.map_QMARK_(attrs38430))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [React.createElement("div",({"dangerouslySetInnerHTML": title, "className": "title"})),(function (){var attrs38431 = (function (){var t = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"published-at","published-at",249684621).cljs$core$IFn$_invoke$arity$1(result);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"created-at","created-at",-89248644).cljs$core$IFn$_invoke$arity$1(result);
}
})();
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"time","time",1385887882),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"date-time","date-time",177938180),t,new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687),"tooltip",new cljs.core.Keyword(null,"data-placement","data-placement",166529525),"top",new cljs.core.Keyword(null,"data-delay","data-delay",1974747786),"{\"show\":\"500\", \"hide\":\"0\"}",new cljs.core.Keyword(null,"data-title","data-title",-83549535),oc.web.lib.utils.activity_date_tooltip(result)], null),oc.web.lib.utils.time_since(t)], null);
})();
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs38431))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["time-since"], null)], null),attrs38431], 0))):({"className": "time-since"})),((cljs.core.map_QMARK_(attrs38431))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs38431)], null)));
})()], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs38430),React.createElement("div",({"dangerouslySetInnerHTML": title, "className": "title"})),(function (){var attrs38432 = (function (){var t = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"published-at","published-at",249684621).cljs$core$IFn$_invoke$arity$1(result);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"created-at","created-at",-89248644).cljs$core$IFn$_invoke$arity$1(result);
}
})();
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"time","time",1385887882),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"date-time","date-time",177938180),t,new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687),"tooltip",new cljs.core.Keyword(null,"data-placement","data-placement",166529525),"top",new cljs.core.Keyword(null,"data-delay","data-delay",1974747786),"{\"show\":\"500\", \"hide\":\"0\"}",new cljs.core.Keyword(null,"data-title","data-title",-83549535),oc.web.lib.utils.activity_date_tooltip(result)], null),oc.web.lib.utils.time_since(t)], null);
})();
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs38432))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["time-since"], null)], null),attrs38432], 0))):({"className": "time-since"})),((cljs.core.map_QMARK_(attrs38432))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs38432)], null)));
})()], null)));
})());
}),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$], null),"entry-display");
oc.web.components.search.board_display = rum.core.build_defcs((function (s,data){
var board = new cljs.core.Keyword(null,"_source","_source",-812884485).cljs$core$IFn$_invoke$arity$1(data);
return React.createElement("div",({"className": "search-result"}),React.createElement("div",({"className": "content"}),(function (){var attrs38436 = new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(board);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"span",((cljs.core.map_QMARK_(attrs38436))?sablono.interpreter.attributes(attrs38436):null),((cljs.core.map_QMARK_(attrs38436))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs38436)], null)));
})()));
}),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$], null),"board-display");
oc.web.components.search.results_header = rum.core.build_defcs((function (s,result_count,failed_QMARK_){
return React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["header","group",(cljs.core.truth_(failed_QMARK_)?"failed":null)], null))}),(function (){var attrs38440 = (cljs.core.truth_(failed_QMARK_)?"SEARCH FAILED":"SEARCH RESULTS");
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"span",((cljs.core.map_QMARK_(attrs38440))?sablono.interpreter.attributes(attrs38440):null),((cljs.core.map_QMARK_(attrs38440))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs38440)], null)));
})(),sablono.interpreter.interpret((((result_count > (0)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.count","span.count",1779679026),["(",cljs.core.str.cljs$core$IFn$_invoke$arity$1(result_count),")"].join('')], null):null)));
}),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$], null),"results-header");
oc.web.components.search.default_page_size = (cljs.core.truth_(oc.web.lib.responsive.is_mobile_size_QMARK_())?(300):(5));
oc.web.components.search.search_results_view = rum.core.build_defcs((function (s,p__38443){
var map__38444 = p__38443;
var map__38444__$1 = (((((!((map__38444 == null))))?(((((map__38444.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38444.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38444):map__38444);
var did_select_history_item = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38444__$1,new cljs.core.Keyword(null,"did-select-history-item","did-select-history-item",-1133470327));
var search_results = org.martinklepsch.derivatives.react(s,oc.web.stores.search.search_key);
var search_active_QMARK_ = org.martinklepsch.derivatives.react(s,oc.web.stores.search.search_active_QMARK_);
var result_count = (((oc.web.stores.search.search_limit < new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(search_results)))?oc.web.stores.search.search_limit:new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(search_results));
var is_mobile_QMARK_ = oc.web.lib.responsive.is_mobile_size_QMARK_();
if(cljs.core.empty_QMARK_(search_results)){
var attrs38446 = (function (){var history = oc.web.actions.search.search_history();
var iter__4529__auto__ = (function oc$web$components$search$iter__38453(s__38454){
return (new cljs.core.LazySeq(null,(function (){
var s__38454__$1 = s__38454;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__38454__$1);
if(temp__5735__auto__){
var s__38454__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__38454__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__38454__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__38456 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__38455 = (0);
while(true){
if((i__38455 < size__4528__auto__)){
var idx = cljs.core._nth(c__4527__auto__,i__38455);
var q = cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.vec(cljs.core.reverse(history)),idx);
cljs.core.chunk_append(b__38456,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.search-history-row","div.search-history-row",-1857026039),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"key","key",-1516042587),["search-history-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(idx)].join(''),new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (i__38455,q,idx,c__4527__auto__,size__4528__auto__,b__38456,s__38454__$2,temp__5735__auto__,history,search_results,search_active_QMARK_,result_count,is_mobile_QMARK_,map__38444,map__38444__$1,did_select_history_item){
return (function (){
if(cljs.core.fn_QMARK_(did_select_history_item)){
(did_select_history_item.cljs$core$IFn$_invoke$arity$1 ? did_select_history_item.cljs$core$IFn$_invoke$arity$1(q) : did_select_history_item.call(null,q));
} else {
}

return oc.web.actions.search.query(q,false);
});})(i__38455,q,idx,c__4527__auto__,size__4528__auto__,b__38456,s__38454__$2,temp__5735__auto__,history,search_results,search_active_QMARK_,result_count,is_mobile_QMARK_,map__38444,map__38444__$1,did_select_history_item))
], null),q], null));

var G__38490 = (i__38455 + (1));
i__38455 = G__38490;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__38456),oc$web$components$search$iter__38453(cljs.core.chunk_rest(s__38454__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__38456),null);
}
} else {
var idx = cljs.core.first(s__38454__$2);
var q = cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.vec(cljs.core.reverse(history)),idx);
return cljs.core.cons(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.search-history-row","div.search-history-row",-1857026039),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"key","key",-1516042587),["search-history-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(idx)].join(''),new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (q,idx,s__38454__$2,temp__5735__auto__,history,search_results,search_active_QMARK_,result_count,is_mobile_QMARK_,map__38444,map__38444__$1,did_select_history_item){
return (function (){
if(cljs.core.fn_QMARK_(did_select_history_item)){
(did_select_history_item.cljs$core$IFn$_invoke$arity$1 ? did_select_history_item.cljs$core$IFn$_invoke$arity$1(q) : did_select_history_item.call(null,q));
} else {
}

return oc.web.actions.search.query(q,false);
});})(q,idx,s__38454__$2,temp__5735__auto__,history,search_results,search_active_QMARK_,result_count,is_mobile_QMARK_,map__38444,map__38444__$1,did_select_history_item))
], null),q], null),oc$web$components$search$iter__38453(cljs.core.rest(s__38454__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(cljs.core.range.cljs$core$IFn$_invoke$arity$1(cljs.core.count(history)));
})();
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs38446))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["search-history"], null)], null),attrs38446], 0))):({"className": "search-history"})),((cljs.core.map_QMARK_(attrs38446))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs38446)], null)));
} else {
return React.createElement("div",({"ref": "results", "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["search-results",(cljs.core.truth_(search_active_QMARK_)?null:"inactive")], null))}),sablono.interpreter.interpret((cljs.core.truth_(is_mobile_QMARK_)?null:(function (){var G__38458 = result_count;
var G__38459 = new cljs.core.Keyword(null,"failed","failed",-1397425762).cljs$core$IFn$_invoke$arity$1(search_results);
return (oc.web.components.search.results_header.cljs$core$IFn$_invoke$arity$2 ? oc.web.components.search.results_header.cljs$core$IFn$_invoke$arity$2(G__38458,G__38459) : oc.web.components.search.results_header.call(null,G__38458,G__38459));
})())),(function (){var attrs38457 = (cljs.core.truth_(is_mobile_QMARK_)?(function (){var G__38460 = result_count;
var G__38461 = new cljs.core.Keyword(null,"failed","failed",-1397425762).cljs$core$IFn$_invoke$arity$1(search_results);
return (oc.web.components.search.results_header.cljs$core$IFn$_invoke$arity$2 ? oc.web.components.search.results_header.cljs$core$IFn$_invoke$arity$2(G__38460,G__38461) : oc.web.components.search.results_header.call(null,G__38460,G__38461));
})():null);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs38457))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["search-results-container"], null)], null),attrs38457], 0))):({"className": "search-results-container"})),((cljs.core.map_QMARK_(attrs38457))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret((((result_count > (0)))?(function (){var results = cljs.core.reverse(new cljs.core.Keyword(null,"results","results",-1134170113).cljs$core$IFn$_invoke$arity$1(search_results));
var iter__4529__auto__ = (function oc$web$components$search$iter__38462(s__38463){
return (new cljs.core.LazySeq(null,(function (){
var s__38463__$1 = s__38463;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__38463__$1);
if(temp__5735__auto__){
var s__38463__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__38463__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__38463__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__38465 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__38464 = (0);
while(true){
if((i__38464 < size__4528__auto__)){
var sr = cljs.core._nth(c__4527__auto__,i__38464);
cljs.core.chunk_append(b__38465,(function (){var key = ["result-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"_source","_source",-812884485).cljs$core$IFn$_invoke$arity$1(sr)))].join('');
var G__38466 = new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"_source","_source",-812884485).cljs$core$IFn$_invoke$arity$1(sr));
switch (G__38466) {
case "entry":
return rum.core.with_key((oc.web.components.search.entry_display.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.search.entry_display.cljs$core$IFn$_invoke$arity$1(sr) : oc.web.components.search.entry_display.call(null,sr)),key);

break;
case "board":
return rum.core.with_key((oc.web.components.search.board_display.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.search.board_display.cljs$core$IFn$_invoke$arity$1(sr) : oc.web.components.search.board_display.call(null,sr)),key);

break;
default:
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__38466)].join('')));

}
})());

var G__38501 = (i__38464 + (1));
i__38464 = G__38501;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__38465),oc$web$components$search$iter__38462(cljs.core.chunk_rest(s__38463__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__38465),null);
}
} else {
var sr = cljs.core.first(s__38463__$2);
return cljs.core.cons((function (){var key = ["result-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"_source","_source",-812884485).cljs$core$IFn$_invoke$arity$1(sr)))].join('');
var G__38467 = new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"_source","_source",-812884485).cljs$core$IFn$_invoke$arity$1(sr));
switch (G__38467) {
case "entry":
return rum.core.with_key((oc.web.components.search.entry_display.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.search.entry_display.cljs$core$IFn$_invoke$arity$1(sr) : oc.web.components.search.entry_display.call(null,sr)),key);

break;
case "board":
return rum.core.with_key((oc.web.components.search.board_display.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.search.board_display.cljs$core$IFn$_invoke$arity$1(sr) : oc.web.components.search.board_display.call(null,sr)),key);

break;
default:
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__38467)].join('')));

}
})(),oc$web$components$search$iter__38462(cljs.core.rest(s__38463__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(cljs.core.take.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(new cljs.core.Keyword("oc.web.components.search","page-size","oc.web.components.search/page-size",421197949).cljs$core$IFn$_invoke$arity$1(s)),results));
})():(cljs.core.truth_(new cljs.core.Keyword(null,"failed","failed",-1397425762).cljs$core$IFn$_invoke$arity$1(search_results))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.empty-result","div.empty-result",-511911037),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.message","div.message",197515312),"An error occurred, please try again..."], null)], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.empty-result","div.empty-result",-511911037),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.message","div.message",197515312),"No matching results..."], null)], null)
)))], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs38457),sablono.interpreter.interpret((((result_count > (0)))?(function (){var results = cljs.core.reverse(new cljs.core.Keyword(null,"results","results",-1134170113).cljs$core$IFn$_invoke$arity$1(search_results));
var iter__4529__auto__ = (function oc$web$components$search$iter__38468(s__38469){
return (new cljs.core.LazySeq(null,(function (){
var s__38469__$1 = s__38469;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__38469__$1);
if(temp__5735__auto__){
var s__38469__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__38469__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__38469__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__38471 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__38470 = (0);
while(true){
if((i__38470 < size__4528__auto__)){
var sr = cljs.core._nth(c__4527__auto__,i__38470);
cljs.core.chunk_append(b__38471,(function (){var key = ["result-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"_source","_source",-812884485).cljs$core$IFn$_invoke$arity$1(sr)))].join('');
var G__38476 = new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"_source","_source",-812884485).cljs$core$IFn$_invoke$arity$1(sr));
switch (G__38476) {
case "entry":
return rum.core.with_key((oc.web.components.search.entry_display.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.search.entry_display.cljs$core$IFn$_invoke$arity$1(sr) : oc.web.components.search.entry_display.call(null,sr)),key);

break;
case "board":
return rum.core.with_key((oc.web.components.search.board_display.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.search.board_display.cljs$core$IFn$_invoke$arity$1(sr) : oc.web.components.search.board_display.call(null,sr)),key);

break;
default:
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__38476)].join('')));

}
})());

var G__38506 = (i__38470 + (1));
i__38470 = G__38506;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__38471),oc$web$components$search$iter__38468(cljs.core.chunk_rest(s__38469__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__38471),null);
}
} else {
var sr = cljs.core.first(s__38469__$2);
return cljs.core.cons((function (){var key = ["result-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"_source","_source",-812884485).cljs$core$IFn$_invoke$arity$1(sr)))].join('');
var G__38478 = new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"_source","_source",-812884485).cljs$core$IFn$_invoke$arity$1(sr));
switch (G__38478) {
case "entry":
return rum.core.with_key((oc.web.components.search.entry_display.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.search.entry_display.cljs$core$IFn$_invoke$arity$1(sr) : oc.web.components.search.entry_display.call(null,sr)),key);

break;
case "board":
return rum.core.with_key((oc.web.components.search.board_display.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.search.board_display.cljs$core$IFn$_invoke$arity$1(sr) : oc.web.components.search.board_display.call(null,sr)),key);

break;
default:
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__38478)].join('')));

}
})(),oc$web$components$search$iter__38468(cljs.core.rest(s__38469__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(cljs.core.take.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(new cljs.core.Keyword("oc.web.components.search","page-size","oc.web.components.search/page-size",421197949).cljs$core$IFn$_invoke$arity$1(s)),results));
})():(cljs.core.truth_(new cljs.core.Keyword(null,"failed","failed",-1397425762).cljs$core$IFn$_invoke$arity$1(search_results))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.empty-result","div.empty-result",-511911037),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.message","div.message",197515312),"An error occurred, please try again..."], null)], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.empty-result","div.empty-result",-511911037),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.message","div.message",197515312),"No matching results..."], null)], null)
)))], null)));
})(),sablono.interpreter.interpret((((cljs.core.deref(new cljs.core.Keyword("oc.web.components.search","page-size","oc.web.components.search/page-size",421197949).cljs$core$IFn$_invoke$arity$1(s)) < result_count))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.show-more","div.show-more",-1221053520),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (e){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.search","page-size","oc.web.components.search/page-size",421197949).cljs$core$IFn$_invoke$arity$1(s),(cljs.core.deref(new cljs.core.Keyword("oc.web.components.search","page-size","oc.web.components.search/page-size",421197949).cljs$core$IFn$_invoke$arity$1(s)) + (15)));
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset","button.mlb-reset",-1088020204),"Show More"], null)], null):null)));
}
}),new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([oc.web.stores.search.search_key], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([oc.web.stores.search.search_active_QMARK_], 0)),rum.core.reactive,rum.core.static$,rum.core.local.cljs$core$IFn$_invoke$arity$2(oc.web.components.search.default_page_size,new cljs.core.Keyword("oc.web.components.search","page-size","oc.web.components.search/page-size",421197949)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"before-render","before-render",71256781),(function (s){
if(((cljs.core.not(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,oc.web.stores.search.search_active_QMARK_)))) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(new cljs.core.Keyword("oc.web.components.search","page-size","oc.web.components.search/page-size",421197949).cljs$core$IFn$_invoke$arity$1(s)),oc.web.components.search.default_page_size)))){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.search","page-size","oc.web.components.search/page-size",421197949).cljs$core$IFn$_invoke$arity$1(s),oc.web.components.search.default_page_size);
} else {
}

return s;
})], null)], null),"search-results-view");
oc.web.components.search.add_window_click_listener = (function oc$web$components$search$add_window_click_listener(s){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.search","win-click-listener","oc.web.components.search/win-click-listener",-203056739).cljs$core$IFn$_invoke$arity$1(s),goog.events.listen(window,goog.events.EventType.CLICK,(function (p1__38481_SHARP_){
var temp__5735__auto__ = rum.core.dom_node(s);
if(cljs.core.truth_(temp__5735__auto__)){
var search_box_el = temp__5735__auto__;
if(cljs.core.truth_((function (){var and__4115__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.search","last-search-active","oc.web.components.search/last-search-active",851015554).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(and__4115__auto__)){
var and__4115__auto____$1 = search_box_el;
if(cljs.core.truth_(and__4115__auto____$1)){
return cljs.core.not(oc.web.lib.utils.event_inside_QMARK_(p1__38481_SHARP_,search_box_el));
} else {
return and__4115__auto____$1;
}
} else {
return and__4115__auto__;
}
})())){
return oc.web.actions.search.inactive();
} else {
return null;
}
} else {
return null;
}
})));
});
oc.web.components.search.remove_window_click_listener = (function oc$web$components$search$remove_window_click_listener(s){
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.search","win-click-listener","oc.web.components.search/win-click-listener",-203056739).cljs$core$IFn$_invoke$arity$1(s)))){
goog.events.unlistenByKey(cljs.core.deref(new cljs.core.Keyword("oc.web.components.search","win-click-listener","oc.web.components.search/win-click-listener",-203056739).cljs$core$IFn$_invoke$arity$1(s)));

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.search","win-click-listener","oc.web.components.search/win-click-listener",-203056739).cljs$core$IFn$_invoke$arity$1(s),null);
} else {
return null;
}
});
oc.web.components.search.auto_search_query = (function oc$web$components$search$auto_search_query(s){
return oc.web.actions.search.query(cljs.core.deref(new cljs.core.Keyword("oc.web.components.search","query","oc.web.components.search/query",-1621664498).cljs$core$IFn$_invoke$arity$1(s)),true);
});
oc.web.components.search.debounced_auto_search_BANG_ = (function oc$web$components$search$debounced_auto_search_BANG_(s){
return cljs.core.deref(new cljs.core.Keyword("oc.web.components.search","debounced-auto-search","oc.web.components.search/debounced-auto-search",-1976552221).cljs$core$IFn$_invoke$arity$1(s)).fire();
});
oc.web.components.search.cancel_auto_search_BANG_ = (function oc$web$components$search$cancel_auto_search_BANG_(s){
return cljs.core.deref(new cljs.core.Keyword("oc.web.components.search","debounced-auto-search","oc.web.components.search/debounced-auto-search",-1976552221).cljs$core$IFn$_invoke$arity$1(s)).stop();
});
oc.web.components.search.search_reset = (function oc$web$components$search$search_reset(s){
oc.web.components.search.cancel_auto_search_BANG_(s);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.search","query","oc.web.components.search/query",-1621664498).cljs$core$IFn$_invoke$arity$1(s),"");

return oc.web.actions.search.reset();
});
oc.web.components.search.search_box = rum.core.build_defcs((function (s){
return sablono.interpreter.interpret(((oc.web.stores.search.should_display())?(function (){var search_active_QMARK_ = org.martinklepsch.derivatives.react(s,oc.web.stores.search.search_active_QMARK_);
var search_results = org.martinklepsch.derivatives.react(s,oc.web.stores.search.search_key);
var is_mobile_QMARK_ = oc.web.lib.responsive.is_mobile_size_QMARK_();
return new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.search-box","div.search-box",770128033),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),(cljs.core.truth_(search_active_QMARK_)?"active":null),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (e){
if(((cljs.core.not(search_active_QMARK_)) && (cljs.core.not(oc.web.lib.utils.event_inside_QMARK_(e,rum.core.ref_node(s,new cljs.core.Keyword(null,"search-close","search-close",514923084))))))){
return rum.core.ref_node(s,"search-input").focus();
} else {
return null;
}
})], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.search-close","button.mlb-reset.search-close",1158328561),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"ref","ref",1289896967),new cljs.core.Keyword(null,"search-close","search-close",514923084),new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.components.search.search_reset(s);
})], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.spyglass-icon","div.spyglass-icon",913954962),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.actions.search.active();
}),new cljs.core.Keyword(null,"class","class",-2030961996),(cljs.core.truth_(((cljs.core.map_QMARK_(search_results))?new cljs.core.Keyword(null,"loading","loading",-737050189).cljs$core$IFn$_invoke$arity$1(search_results):false))?"loading":null)], null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"form","form",-1624062471),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"on-submit","on-submit",1227871159),(function (p1__38482_SHARP_){
return p1__38482_SHARP_.preventDefault();
}),new cljs.core.Keyword(null,"action","action",-811238024),"."], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input.search.oc-input","input.search.oc-input",-224173644),new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"inactive","inactive",-306247616),cljs.core.not(search_active_QMARK_),new cljs.core.Keyword(null,"loading","loading",-737050189),((cljs.core.map_QMARK_(search_results))?new cljs.core.Keyword(null,"loading","loading",-737050189).cljs$core$IFn$_invoke$arity$1(search_results):false)], null)),new cljs.core.Keyword(null,"ref","ref",1289896967),"search-input",new cljs.core.Keyword(null,"type","type",1174270348),"search",new cljs.core.Keyword(null,"value","value",305978217),cljs.core.deref(new cljs.core.Keyword("oc.web.components.search","query","oc.web.components.search/query",-1621664498).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"placeholder","placeholder",-104873083),(cljs.core.truth_(is_mobile_QMARK_)?"Search posts...":"Search"),new cljs.core.Keyword(null,"on-focus","on-focus",-13737624),(function (){
return oc.web.actions.search.active();
}),new cljs.core.Keyword(null,"on-change","on-change",-732046149),(function (e){
var v = e.target.value;
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.search","query","oc.web.components.search/query",-1621664498).cljs$core$IFn$_invoke$arity$1(s),v);

return oc.web.components.search.debounced_auto_search_BANG_(s);
}),new cljs.core.Keyword(null,"on-key-press","on-key-press",-399563677),(function (e){
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(e.key,"Enter")) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(e.keyCode,(13))))){
oc.web.components.search.cancel_auto_search_BANG_(s);

oc.web.actions.search.query(cljs.core.deref(new cljs.core.Keyword("oc.web.components.search","query","oc.web.components.search/query",-1621664498).cljs$core$IFn$_invoke$arity$1(s)),false);

return rum.core.ref_node(s,"search-input").blur();
} else {
return null;
}
})], null)], null)], null),(function (){var G__38484 = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"did-select-history-item","did-select-history-item",-1133470327),(function (p1__38483_SHARP_){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.search","query","oc.web.components.search/query",-1621664498).cljs$core$IFn$_invoke$arity$1(s),p1__38483_SHARP_);
})], null);
return (oc.web.components.search.search_results_view.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.search.search_results_view.cljs$core$IFn$_invoke$arity$1(G__38484) : oc.web.components.search.search_results_view.call(null,G__38484));
})()], null);
})():null));
}),new cljs.core.PersistentVector(null, 10, 5, cljs.core.PersistentVector.EMPTY_NODE, [org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([oc.web.stores.search.search_key], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([oc.web.stores.search.search_active_QMARK_], 0)),rum.core.reactive,rum.core.static$,rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.search","search-timeout","oc.web.components.search/search-timeout",1035804895)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.search","win-click-listener","oc.web.components.search/win-click-listener",-203056739)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.search","last-search-active","oc.web.components.search/last-search-active",851015554)),rum.core.local.cljs$core$IFn$_invoke$arity$2("",new cljs.core.Keyword("oc.web.components.search","query","oc.web.components.search/query",-1621664498)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.search","debounced-auto-search","oc.web.components.search/debounced-auto-search",-1976552221)),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.search","debounced-auto-search","oc.web.components.search/debounced-auto-search",-1976552221).cljs$core$IFn$_invoke$arity$1(s),(new goog.async.Debouncer(cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.components.search.auto_search_query,s),(cljs.core.truth_(oc.web.lib.responsive.is_mobile_size_QMARK_())?(800):(500)))));

return s;
}),new cljs.core.Keyword(null,"did-update","did-update",-2143702256),(function (s){
var current_search_active_38508 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,oc.web.stores.search.search_active_QMARK_));
var search_input_38509 = rum.core.ref_node(s,"search-input");
var saved_search_38510 = cljs.core.deref(oc.web.stores.search.savedsearch);
var last_search_active_38511 = new cljs.core.Keyword("oc.web.components.search","last-search-active","oc.web.components.search/last-search-active",851015554).cljs$core$IFn$_invoke$arity$1(s);
if(cljs.core.compare_and_set_BANG_(last_search_active_38511,cljs.core.not(current_search_active_38508),cljs.core.boolean$(current_search_active_38508))){
if(cljs.core.truth_(oc.web.lib.responsive.is_mobile_size_QMARK_())){
} else {
if(cljs.core.truth_(current_search_active_38508)){
oc.web.components.search.add_window_click_listener(s);
} else {
oc.web.components.search.remove_window_click_listener(s);
}
}

if(cljs.core.truth_(current_search_active_38508)){
} else {
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.search","query","oc.web.components.search/query",-1621664498).cljs$core$IFn$_invoke$arity$1(s),"");
}

if(cljs.core.truth_((function (){var and__4115__auto__ = current_search_active_38508;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.seq(saved_search_38510);
} else {
return and__4115__auto__;
}
})())){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.search","query","oc.web.components.search/query",-1621664498).cljs$core$IFn$_invoke$arity$1(s),oc.web.stores.search.saved_search());
} else {
}
} else {
}

return s;
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
oc.web.components.search.remove_window_click_listener(s);

var temp__5735__auto___38512 = cljs.core.deref(new cljs.core.Keyword("oc.web.components.search","debounced-auto-search","oc.web.components.search/debounced-auto-search",-1976552221).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(temp__5735__auto___38512)){
var debounced_auto_search_38513 = temp__5735__auto___38512;
debounced_auto_search_38513.dispose();
} else {
}

return s;
})], null)], null),"search-box");

//# sourceMappingURL=oc.web.components.search.js.map
