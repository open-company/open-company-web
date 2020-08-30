goog.provide('oc.web.components.paginated_stream');
var module$node_modules$react_virtualized$dist$commonjs$index=shadow.js.require("module$node_modules$react_virtualized$dist$commonjs$index", {});
oc.web.components.paginated_stream.virtualized_grid = cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.lib.react_utils.build,module$node_modules$react_virtualized$dist$commonjs$index.Grid);
oc.web.components.paginated_stream.window_scroller = cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.lib.react_utils.build,module$node_modules$react_virtualized$dist$commonjs$index.WindowScroller);
oc.web.components.paginated_stream.cell_measurer = cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.lib.react_utils.build,module$node_modules$react_virtualized$dist$commonjs$index.CellMeasurer);
oc.web.components.paginated_stream.RVCellMeasurerCache = module$node_modules$react_virtualized$dist$commonjs$index.CellMeasurerCache;
oc.web.components.paginated_stream.collapsed_foc_height = (56);
oc.web.components.paginated_stream.foc_height = (204);
oc.web.components.paginated_stream.mobile_foc_height = (166);
oc.web.components.paginated_stream.calc_card_height = (function oc$web$components$paginated_stream$calc_card_height(mobile_QMARK_,foc_layout){
if(cljs.core.truth_(mobile_QMARK_)){
return oc.web.components.paginated_stream.mobile_foc_height;
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(foc_layout,oc.web.dispatcher.other_foc_layout)){
return oc.web.components.paginated_stream.collapsed_foc_height;
} else {
return oc.web.components.paginated_stream.foc_height;

}
}
});
oc.web.components.paginated_stream.wrapped_stream_item = rum.core.build_defc((function (p__46543){
var map__46544 = p__46543;
var map__46544__$1 = (((((!((map__46544 == null))))?(((((map__46544.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46544.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46544):map__46544);
var props = map__46544__$1;
var foc_layout = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46544__$1,new cljs.core.Keyword(null,"foc-layout","foc-layout",-1925028965));
var org_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46544__$1,new cljs.core.Keyword(null,"org-data","org-data",96720321));
var current_user_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46544__$1,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915));
var row_index = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46544__$1,new cljs.core.Keyword(null,"row-index","row-index",-828710296));
var item = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46544__$1,new cljs.core.Keyword(null,"item","item",249373802));
var clear_cell_measure_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46544__$1,new cljs.core.Keyword(null,"clear-cell-measure-cb","clear-cell-measure-cb",1839014573));
var is_mobile = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46544__$1,new cljs.core.Keyword(null,"is-mobile","is-mobile",1973305231));
var add_comment_force_update = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46544__$1,new cljs.core.Keyword(null,"add-comment-force-update","add-comment-force-update",1376707794));
var editable_boards = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46544__$1,new cljs.core.Keyword(null,"editable-boards","editable-boards",1897056658));
var read_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46544__$1,new cljs.core.Keyword(null,"read-data","read-data",-715156010));
var container_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46544__$1,new cljs.core.Keyword(null,"container-data","container-data",-53681130));
var member_QMARK_ = new cljs.core.Keyword(null,"member?","member?",486668360).cljs$core$IFn$_invoke$arity$1(org_data);
var replies_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"container-slug","container-slug",365736492).cljs$core$IFn$_invoke$arity$1(container_data),new cljs.core.Keyword(null,"replies","replies",-1389888974));
var show_wrt_QMARK_ = (function (){var and__4115__auto__ = member_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return (!(replies_QMARK_));
} else {
return and__4115__auto__;
}
})();
var show_new_comments_QMARK_ = replies_QMARK_;
var collapsed_item_QMARK_ = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(foc_layout,oc.web.dispatcher.other_foc_layout)) && (cljs.core.not(is_mobile)));
return React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["virtualized-list-row",oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"collapsed-item","collapsed-item",1401917624),collapsed_item_QMARK_,new cljs.core.Keyword(null,"open-item","open-item",-1938301269),new cljs.core.Keyword(null,"open-item","open-item",-1938301269).cljs$core$IFn$_invoke$arity$1(item),new cljs.core.Keyword(null,"close-item","close-item",-38717813),new cljs.core.Keyword(null,"close-item","close-item",-38717813).cljs$core$IFn$_invoke$arity$1(item)], null))], null))}),sablono.interpreter.interpret(((collapsed_item_QMARK_)?(function (){var G__46553 = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"activity-data","activity-data",1293689136),item,new cljs.core.Keyword(null,"member?","member?",486668360),member_QMARK_,new cljs.core.Keyword(null,"read-data","read-data",-715156010),read_data,new cljs.core.Keyword(null,"show-wrt?","show-wrt?",-1492163707),show_wrt_QMARK_,new cljs.core.Keyword(null,"editable-boards","editable-boards",1897056658),editable_boards,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915),current_user_data], null);
return (oc.web.components.stream_collapsed_item.stream_collapsed_item.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.stream_collapsed_item.stream_collapsed_item.cljs$core$IFn$_invoke$arity$1(G__46553) : oc.web.components.stream_collapsed_item.stream_collapsed_item.call(null,G__46553));
})():(function (){var G__46554 = cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915),new cljs.core.Keyword(null,"boards-count","boards-count",1873110277),new cljs.core.Keyword(null,"show-wrt?","show-wrt?",-1492163707),new cljs.core.Keyword(null,"member?","member?",486668360),new cljs.core.Keyword(null,"show-new-comments?","show-new-comments?",-832920630),new cljs.core.Keyword(null,"container-slug","container-slug",365736492),new cljs.core.Keyword(null,"activity-data","activity-data",1293689136),new cljs.core.Keyword(null,"editable-boards","editable-boards",1897056658),new cljs.core.Keyword(null,"foc-board","foc-board",-1668164909),new cljs.core.Keyword(null,"read-data","read-data",-715156010),new cljs.core.Keyword(null,"replies?","replies?",1753919327)],[current_user_data,cljs.core.count(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__46540_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(p1__46540_SHARP_),oc.web.lib.utils.default_drafts_board_slug);
}),new cljs.core.Keyword(null,"boards","boards",1912049694).cljs$core$IFn$_invoke$arity$1(org_data))),show_wrt_QMARK_,member_QMARK_,show_new_comments_QMARK_,new cljs.core.Keyword(null,"container-slug","container-slug",365736492).cljs$core$IFn$_invoke$arity$1(container_data),item,editable_boards,cljs.core.not(oc.web.utils.activity.board_QMARK_(container_data)),read_data,replies_QMARK_]);
return (oc.web.components.stream_item.stream_item.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.stream_item.stream_item.cljs$core$IFn$_invoke$arity$1(G__46554) : oc.web.components.stream_item.stream_item.call(null,G__46554));
})()
)));
}),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$], null),"wrapped-stream-item");
oc.web.components.paginated_stream.load_more = rum.core.build_defc((function (p__46556){
var map__46557 = p__46556;
var map__46557__$1 = (((((!((map__46557 == null))))?(((((map__46557.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46557.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46557):map__46557);
var item = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46557__$1,new cljs.core.Keyword(null,"item","item",249373802));
var attrs46555 = new cljs.core.Keyword(null,"message","message",-406056002).cljs$core$IFn$_invoke$arity$1(item);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46555))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["loading-updates","bottom-loading"], null)], null),attrs46555], 0))):({"className": "loading-updates bottom-loading"})),((cljs.core.map_QMARK_(attrs46555))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46555)], null)));
}),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$], null),"load-more");
oc.web.components.paginated_stream.closing_item = rum.core.build_defc((function (p__46560){
var map__46561 = p__46560;
var map__46561__$1 = (((((!((map__46561 == null))))?(((((map__46561.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46561.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46561):map__46561);
var item = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46561__$1,new cljs.core.Keyword(null,"item","item",249373802));
var attrs46559 = new cljs.core.Keyword(null,"message","message",-406056002).cljs$core$IFn$_invoke$arity$1(item);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46559))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["closing-item"], null)], null),attrs46559], 0))):({"className": "closing-item"})),((cljs.core.map_QMARK_(attrs46559))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46559)], null)));
}),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$], null),"closing-item");
oc.web.components.paginated_stream.separator_item = rum.core.build_defc((function (p__46563){
var map__46564 = p__46563;
var map__46564__$1 = (((((!((map__46564 == null))))?(((((map__46564.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46564.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46564):map__46564);
var props = map__46564__$1;
var foc_layout = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46564__$1,new cljs.core.Keyword(null,"foc-layout","foc-layout",-1925028965));
var item = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46564__$1,new cljs.core.Keyword(null,"item","item",249373802));
return React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["virtualized-list-separator",((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(foc_layout,oc.web.dispatcher.other_foc_layout))?"expanded-list":null)], null))}),sablono.interpreter.interpret(new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(item)));
}),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$], null),"separator-item");
oc.web.components.paginated_stream.caught_up_wrapper = rum.core.build_defc((function (p__46567){
var map__46568 = p__46567;
var map__46568__$1 = (((((!((map__46568 == null))))?(((((map__46568.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46568.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46568):map__46568);
var item = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46568__$1,new cljs.core.Keyword(null,"item","item",249373802));
var attrs46566 = (oc.web.components.ui.all_caught_up.caught_up_line.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.all_caught_up.caught_up_line.cljs$core$IFn$_invoke$arity$1(item) : oc.web.components.ui.all_caught_up.caught_up_line.call(null,item));
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46566))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["caught-up-wrapper"], null)], null),attrs46566], 0))):({"className": "caught-up-wrapper"})),((cljs.core.map_QMARK_(attrs46566))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46566)], null)));
}),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$], null),"caught-up-wrapper");
oc.web.components.paginated_stream.list_item = rum.core.build_defcs((function (s,p__46570,p__46571,props){
var map__46572 = p__46570;
var map__46572__$1 = (((((!((map__46572 == null))))?(((((map__46572.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46572.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46572):map__46572);
var derivatives = map__46572__$1;
var items = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46572__$1,new cljs.core.Keyword(null,"items","items",1031954938));
var foc_layout = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46572__$1,new cljs.core.Keyword(null,"foc-layout","foc-layout",-1925028965));
var is_mobile_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46572__$1,new cljs.core.Keyword(null,"is-mobile?","is-mobile?",2146205507));
var current_user_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46572__$1,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915));
var activities_read = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46572__$1,new cljs.core.Keyword(null,"activities-read","activities-read",2125722631));
var row_index = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46572__$1,new cljs.core.Keyword(null,"row-index","row-index",-828710296));
var clear_cell_measure_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46572__$1,new cljs.core.Keyword(null,"clear-cell-measure-cb","clear-cell-measure-cb",1839014573));
var add_comment_force_update = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46572__$1,new cljs.core.Keyword(null,"add-comment-force-update","add-comment-force-update",1376707794));
var container_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46572__$1,new cljs.core.Keyword(null,"container-data","container-data",-53681130));
var map__46573 = p__46571;
var map__46573__$1 = (((((!((map__46573 == null))))?(((((map__46573.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46573.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46573):map__46573);
var row_props = map__46573__$1;
var rowIndex = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46573__$1,new cljs.core.Keyword(null,"rowIndex","rowIndex",-821650233));
var key = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46573__$1,new cljs.core.Keyword(null,"key","key",-1516042587));
var style = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46573__$1,new cljs.core.Keyword(null,"style","style",-496642736));
var isScrolling = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46573__$1,new cljs.core.Keyword(null,"isScrolling","isScrolling",-1204024548));
var map__46576 = cljs.core.js__GT_clj.cljs$core$IFn$_invoke$arity$variadic(props,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"keywordize-keys","keywordize-keys",1310784252),true], 0));
var map__46576__$1 = (((((!((map__46576 == null))))?(((((map__46576.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46576.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46576):map__46576);
var clj_props = map__46576__$1;
var registerChild = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46576__$1,new cljs.core.Keyword(null,"registerChild","registerChild",-1580726275));
var measure = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46576__$1,new cljs.core.Keyword(null,"measure","measure",-1857519826));
var item = cljs.core.get.cljs$core$IFn$_invoke$arity$2(items,rowIndex);
var entry_QMARK_ = oc.web.utils.activity.entry_QMARK_(item);
var read_data = (cljs.core.truth_(entry_QMARK_)?cljs.core.get.cljs$core$IFn$_invoke$arity$2(activities_read,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(item)):null);
var replies_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"container-slug","container-slug",365736492).cljs$core$IFn$_invoke$arity$1(container_data),new cljs.core.Keyword(null,"replies","replies",-1389888974));
return React.createElement("div",({"key": [cljs.core.name(new cljs.core.Keyword(null,"resource-type","resource-type",1844262326).cljs$core$IFn$_invoke$arity$1(item)),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(key),"-",(cljs.core.truth_(entry_QMARK_)?((replies_QMARK_)?[cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(item)),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"last-activity-at","last-activity-at",670511998).cljs$core$IFn$_invoke$arity$1(item)),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.count(new cljs.core.Keyword(null,"replies-data","replies-data",1118937948).cljs$core$IFn$_invoke$arity$1(item)))].join(''):clojure.string.join.cljs$core$IFn$_invoke$arity$2("-",cljs.core.select_keys(item,new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"uuid","uuid",-2145095719),new cljs.core.Keyword(null,"created-at","created-at",-89248644),new cljs.core.Keyword(null,"published-at","published-at",249684621),new cljs.core.Keyword(null,"updated-at","updated-at",-1592622336)], null)))
):[cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(item)),cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"sort-value","sort-value",-585546274).cljs$core$IFn$_invoke$arity$1(item)),cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"last-activity-at","last-activity-at",670511998).cljs$core$IFn$_invoke$arity$1(item))].join(''))].join(''), "ref": registerChild, "style": sablono.interpreter.attributes(style), "className": "virtualized-list-item"}),sablono.interpreter.interpret((cljs.core.truth_(oc.web.utils.activity.resource_type_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([item,new cljs.core.Keyword(null,"caught-up","caught-up",-1338440788)], 0)))?(function (){var G__46578 = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"item","item",249373802),item], null);
return (oc.web.components.paginated_stream.caught_up_wrapper.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.paginated_stream.caught_up_wrapper.cljs$core$IFn$_invoke$arity$1(G__46578) : oc.web.components.paginated_stream.caught_up_wrapper.call(null,G__46578));
})():(cljs.core.truth_(oc.web.utils.activity.resource_type_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([item,new cljs.core.Keyword(null,"closing-item","closing-item",458331664)], 0)))?(function (){var G__46579 = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"item","item",249373802),item], null);
return (oc.web.components.paginated_stream.closing_item.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.paginated_stream.closing_item.cljs$core$IFn$_invoke$arity$1(G__46579) : oc.web.components.paginated_stream.closing_item.call(null,G__46579));
})():(cljs.core.truth_(oc.web.utils.activity.resource_type_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([item,new cljs.core.Keyword(null,"loading-more","loading-more",-1145525667)], 0)))?(function (){var G__46580 = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"item","item",249373802),item], null);
return (oc.web.components.paginated_stream.load_more.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.paginated_stream.load_more.cljs$core$IFn$_invoke$arity$1(G__46580) : oc.web.components.paginated_stream.load_more.call(null,G__46580));
})():(cljs.core.truth_(oc.web.utils.activity.resource_type_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([item,new cljs.core.Keyword(null,"separator","separator",-1628749125)], 0)))?(function (){var G__46581 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"item","item",249373802),item,new cljs.core.Keyword(null,"foc-layout","foc-layout",-1925028965),foc_layout], null);
return (oc.web.components.paginated_stream.separator_item.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.paginated_stream.separator_item.cljs$core$IFn$_invoke$arity$1(G__46581) : oc.web.components.paginated_stream.separator_item.call(null,G__46581));
})():(function (){var G__46582 = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([derivatives,new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"item","item",249373802),item,new cljs.core.Keyword(null,"read-data","read-data",-715156010),read_data,new cljs.core.Keyword(null,"is-mobile","is-mobile",1973305231),is_mobile_QMARK_,new cljs.core.Keyword(null,"foc-layout","foc-layout",-1925028965),foc_layout,new cljs.core.Keyword(null,"container-data","container-data",-53681130),container_data,new cljs.core.Keyword(null,"add-comment-force-update","add-comment-force-update",1376707794),add_comment_force_update,new cljs.core.Keyword(null,"clear-cell-measure-cb","clear-cell-measure-cb",1839014573),clear_cell_measure_cb,new cljs.core.Keyword(null,"row-index","row-index",-828710296),row_index], null)], 0));
return (oc.web.components.paginated_stream.wrapped_stream_item.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.paginated_stream.wrapped_stream_item.cljs$core$IFn$_invoke$arity$1(G__46582) : oc.web.components.paginated_stream.wrapped_stream_item.call(null,G__46582));
})()
))))));
}),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$], null),"list-item");
oc.web.components.paginated_stream.unique_row_string = (function oc$web$components$paginated_stream$unique_row_string(replies_QMARK_,item){
var entry_QMARK_ = oc.web.utils.activity.entry_QMARK_(item);
var static_part = [cljs.core.name(new cljs.core.Keyword(null,"resource-type","resource-type",1844262326).cljs$core$IFn$_invoke$arity$1(item)),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(item))].join('');
var variable_part = (cljs.core.truth_(entry_QMARK_)?(function (){var or__4126__auto__ = new cljs.core.Keyword(null,"updated-at","updated-at",-1592622336).cljs$core$IFn$_invoke$arity$1(item);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"created-at","created-at",-89248644).cljs$core$IFn$_invoke$arity$1(item);
}
})():new cljs.core.Keyword(null,"last-activity-at","last-activity-at",670511998).cljs$core$IFn$_invoke$arity$1(item)
);
return [static_part,"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(variable_part)].join('');
});
oc.web.components.paginated_stream.clear_cell_measure = (function oc$web$components$paginated_stream$clear_cell_measure(var_args){
var G__46584 = arguments.length;
switch (G__46584) {
case 2:
return oc.web.components.paginated_stream.clear_cell_measure.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.components.paginated_stream.clear_cell_measure.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.components.paginated_stream.clear_cell_measure.cljs$core$IFn$_invoke$arity$2 = (function (rum_state,row_index){
return oc.web.components.paginated_stream.clear_cell_measure.cljs$core$IFn$_invoke$arity$3(rum_state,row_index,(0));
}));

(oc.web.components.paginated_stream.clear_cell_measure.cljs$core$IFn$_invoke$arity$3 = (function (rum_state,row_index,column_index){
var temp__5735__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.paginated-stream","cache","oc.web.components.paginated-stream/cache",1753465102).cljs$core$IFn$_invoke$arity$1(rum_state));
if(cljs.core.truth_(temp__5735__auto__)){
var cache = temp__5735__auto__;
cache.clear(row_index,column_index);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.paginated-stream","force-re-render","oc.web.components.paginated-stream/force-re-render",295163146).cljs$core$IFn$_invoke$arity$1(rum_state),oc.web.lib.utils.activity_uuid());
} else {
return null;
}
}));

(oc.web.components.paginated_stream.clear_cell_measure.cljs$lang$maxFixedArity = 3);

oc.web.components.paginated_stream.clear_changed_cells_cache = (function oc$web$components$paginated_stream$clear_changed_cells_cache(s,next_row_keys){
var props = cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s));
var container_data = new cljs.core.Keyword(null,"container-data","container-data",-53681130).cljs$core$IFn$_invoke$arity$1(props);
var items = new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(props);
var resource_types = new cljs.core.Keyword("oc.web.components.paginated-stream","row-keys","oc.web.components.paginated-stream/row-keys",-618219090).cljs$core$IFn$_invoke$arity$1(s);
var cache = cljs.core.deref(new cljs.core.Keyword("oc.web.components.paginated-stream","cache","oc.web.components.paginated-stream/cache",1753465102).cljs$core$IFn$_invoke$arity$1(s));
var seq__46585_46635 = cljs.core.seq(cljs.core.range.cljs$core$IFn$_invoke$arity$1(cljs.core.count(items)));
var chunk__46588_46636 = null;
var count__46589_46637 = (0);
var i__46590_46638 = (0);
while(true){
if((i__46590_46638 < count__46589_46637)){
var idx_46639 = chunk__46588_46636.cljs$core$IIndexed$_nth$arity$2(null,i__46590_46638);
var old_resource_type_46640 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(resource_types),idx_46639);
var new_resource_type_46641 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(next_row_keys,idx_46639);
if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(old_resource_type_46640,new_resource_type_46641)){
oc.web.components.paginated_stream.clear_cell_measure.cljs$core$IFn$_invoke$arity$3(s,idx_46639,(0));


var G__46642 = seq__46585_46635;
var G__46643 = chunk__46588_46636;
var G__46644 = count__46589_46637;
var G__46645 = (i__46590_46638 + (1));
seq__46585_46635 = G__46642;
chunk__46588_46636 = G__46643;
count__46589_46637 = G__46644;
i__46590_46638 = G__46645;
continue;
} else {
var G__46646 = seq__46585_46635;
var G__46647 = chunk__46588_46636;
var G__46648 = count__46589_46637;
var G__46649 = (i__46590_46638 + (1));
seq__46585_46635 = G__46646;
chunk__46588_46636 = G__46647;
count__46589_46637 = G__46648;
i__46590_46638 = G__46649;
continue;
}
} else {
var temp__5735__auto___46650 = cljs.core.seq(seq__46585_46635);
if(temp__5735__auto___46650){
var seq__46585_46651__$1 = temp__5735__auto___46650;
if(cljs.core.chunked_seq_QMARK_(seq__46585_46651__$1)){
var c__4556__auto___46652 = cljs.core.chunk_first(seq__46585_46651__$1);
var G__46653 = cljs.core.chunk_rest(seq__46585_46651__$1);
var G__46654 = c__4556__auto___46652;
var G__46655 = cljs.core.count(c__4556__auto___46652);
var G__46656 = (0);
seq__46585_46635 = G__46653;
chunk__46588_46636 = G__46654;
count__46589_46637 = G__46655;
i__46590_46638 = G__46656;
continue;
} else {
var idx_46657 = cljs.core.first(seq__46585_46651__$1);
var old_resource_type_46658 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(resource_types),idx_46657);
var new_resource_type_46659 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(next_row_keys,idx_46657);
if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(old_resource_type_46658,new_resource_type_46659)){
oc.web.components.paginated_stream.clear_cell_measure.cljs$core$IFn$_invoke$arity$3(s,idx_46657,(0));


var G__46660 = cljs.core.next(seq__46585_46651__$1);
var G__46661 = null;
var G__46662 = (0);
var G__46663 = (0);
seq__46585_46635 = G__46660;
chunk__46588_46636 = G__46661;
count__46589_46637 = G__46662;
i__46590_46638 = G__46663;
continue;
} else {
var G__46664 = cljs.core.next(seq__46585_46651__$1);
var G__46665 = null;
var G__46666 = (0);
var G__46667 = (0);
seq__46585_46635 = G__46664;
chunk__46588_46636 = G__46665;
count__46589_46637 = G__46666;
i__46590_46638 = G__46667;
continue;
}
}
} else {
}
}
break;
}

return cljs.core.reset_BANG_(resource_types,next_row_keys);
});
oc.web.components.paginated_stream.setup_onload_recalc = (function oc$web$components$paginated_stream$setup_onload_recalc(s){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"container-slug","container-slug",365736492).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"container-data","container-data",-53681130).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s)))),new cljs.core.Keyword(null,"replies","replies",-1389888974))){
var dom_node = rum.core.dom_node(s);
if(cljs.core.truth_(dom_node)){
var parent_sel = ["div.",oc.web.utils.dom.onload_recalc_measure_class].join('');
var nodes = cljs.core.array_seq.cljs$core$IFn$_invoke$arity$1(dom_node.querySelectorAll([parent_sel," img, ",parent_sel," iframe"].join('')));
var seq__46593 = cljs.core.seq(nodes);
var chunk__46594 = null;
var count__46595 = (0);
var i__46596 = (0);
while(true){
if((i__46596 < count__46595)){
var el = chunk__46594.cljs$core$IIndexed$_nth$arity$2(null,i__46596);
var comment_node_46668 = el.closest(parent_sel);
var row_index_46669 = comment_node_46668.dataset.rowIndex;
oc.web.components.paginated_stream.clear_cell_measure.cljs$core$IFn$_invoke$arity$3(s,row_index_46669,(0));

(el.onload = ((function (seq__46593,chunk__46594,count__46595,i__46596,comment_node_46668,row_index_46669,el,parent_sel,nodes,dom_node){
return (function (){
return oc.web.components.paginated_stream.clear_cell_measure.cljs$core$IFn$_invoke$arity$3(s,row_index_46669,(0));
});})(seq__46593,chunk__46594,count__46595,i__46596,comment_node_46668,row_index_46669,el,parent_sel,nodes,dom_node))
);


var G__46670 = seq__46593;
var G__46671 = chunk__46594;
var G__46672 = count__46595;
var G__46673 = (i__46596 + (1));
seq__46593 = G__46670;
chunk__46594 = G__46671;
count__46595 = G__46672;
i__46596 = G__46673;
continue;
} else {
var temp__5735__auto__ = cljs.core.seq(seq__46593);
if(temp__5735__auto__){
var seq__46593__$1 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__46593__$1)){
var c__4556__auto__ = cljs.core.chunk_first(seq__46593__$1);
var G__46674 = cljs.core.chunk_rest(seq__46593__$1);
var G__46675 = c__4556__auto__;
var G__46676 = cljs.core.count(c__4556__auto__);
var G__46677 = (0);
seq__46593 = G__46674;
chunk__46594 = G__46675;
count__46595 = G__46676;
i__46596 = G__46677;
continue;
} else {
var el = cljs.core.first(seq__46593__$1);
var comment_node_46678 = el.closest(parent_sel);
var row_index_46679 = comment_node_46678.dataset.rowIndex;
oc.web.components.paginated_stream.clear_cell_measure.cljs$core$IFn$_invoke$arity$3(s,row_index_46679,(0));

(el.onload = ((function (seq__46593,chunk__46594,count__46595,i__46596,comment_node_46678,row_index_46679,el,seq__46593__$1,temp__5735__auto__,parent_sel,nodes,dom_node){
return (function (){
return oc.web.components.paginated_stream.clear_cell_measure.cljs$core$IFn$_invoke$arity$3(s,row_index_46679,(0));
});})(seq__46593,chunk__46594,count__46595,i__46596,comment_node_46678,row_index_46679,el,seq__46593__$1,temp__5735__auto__,parent_sel,nodes,dom_node))
);


var G__46680 = cljs.core.next(seq__46593__$1);
var G__46681 = null;
var G__46682 = (0);
var G__46683 = (0);
seq__46593 = G__46680;
chunk__46594 = G__46681;
count__46595 = G__46682;
i__46596 = G__46683;
continue;
}
} else {
return null;
}
}
break;
}
} else {
return null;
}
} else {
return null;
}
});
oc.web.components.paginated_stream.virtualized_stream = rum.core.build_defcs((function (s,p__46597,virtualized_props){
var map__46598 = p__46597;
var map__46598__$1 = (((((!((map__46598 == null))))?(((((map__46598.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46598.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46598):map__46598);
var derivatives = map__46598__$1;
var items = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46598__$1,new cljs.core.Keyword(null,"items","items",1031954938));
var activities_read = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46598__$1,new cljs.core.Keyword(null,"activities-read","activities-read",2125722631));
var foc_layout = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46598__$1,new cljs.core.Keyword(null,"foc-layout","foc-layout",-1925028965));
var is_mobile_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46598__$1,new cljs.core.Keyword(null,"is-mobile?","is-mobile?",2146205507));
var container_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46598__$1,new cljs.core.Keyword(null,"container-data","container-data",-53681130));
var current_user_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46598__$1,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915));
var map__46600 = cljs.core.js__GT_clj.cljs$core$IFn$_invoke$arity$variadic(virtualized_props,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"keywordize-keys","keywordize-keys",1310784252),true], 0));
var map__46600__$1 = (((((!((map__46600 == null))))?(((((map__46600.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46600.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46600):map__46600);
var height = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46600__$1,new cljs.core.Keyword(null,"height","height",1025178622));
var isScrolling = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46600__$1,new cljs.core.Keyword(null,"isScrolling","isScrolling",-1204024548));
var onChildScroll = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46600__$1,new cljs.core.Keyword(null,"onChildScroll","onChildScroll",1727261800));
var scrollTop = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46600__$1,new cljs.core.Keyword(null,"scrollTop","scrollTop",-1143661921));
var registerChild = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46600__$1,new cljs.core.Keyword(null,"registerChild","registerChild",-1580726275));
var key_prefix = (cljs.core.truth_(is_mobile_QMARK_)?"mobile":foc_layout);
var cell_measurer_renderer = (function (p__46602,props){
var map__46603 = p__46602;
var map__46603__$1 = (((((!((map__46603 == null))))?(((((map__46603.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46603.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46603):map__46603);
var cache = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46603__$1,new cljs.core.Keyword(null,"cache","cache",-1237023054));
var map__46605 = cljs.core.js__GT_clj.cljs$core$IFn$_invoke$arity$variadic(props,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"keywordize-keys","keywordize-keys",1310784252),true], 0));
var map__46605__$1 = (((((!((map__46605 == null))))?(((((map__46605.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46605.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46605):map__46605);
var row_props = map__46605__$1;
var rowIndex = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46605__$1,new cljs.core.Keyword(null,"rowIndex","rowIndex",-821650233));
var key = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46605__$1,new cljs.core.Keyword(null,"key","key",-1516042587));
var parent = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46605__$1,new cljs.core.Keyword(null,"parent","parent",-878878779));
var columnIndex = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46605__$1,new cljs.core.Keyword(null,"columnIndex","columnIndex",-338692359));
var derived_props = cljs.core.assoc.cljs$core$IFn$_invoke$arity$variadic(derivatives,new cljs.core.Keyword(null,"clear-cell-measure-cb","clear-cell-measure-cb",1839014573),(function (){
return oc.web.components.paginated_stream.clear_cell_measure.cljs$core$IFn$_invoke$arity$3(s,rowIndex,columnIndex);
}),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"row-index","row-index",-828710296),rowIndex], 0));
return oc.web.components.paginated_stream.cell_measurer(cljs.core.clj__GT_js(new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"cache","cache",-1237023054),cache,new cljs.core.Keyword(null,"columnIndex","columnIndex",-338692359),columnIndex,new cljs.core.Keyword(null,"rowIndex","rowIndex",-821650233),rowIndex,new cljs.core.Keyword(null,"index","index",-1531685915),rowIndex,new cljs.core.Keyword(null,"key","key",-1516042587),key,new cljs.core.Keyword(null,"parent","parent",-878878779),parent], null)),cljs.core.partial.cljs$core$IFn$_invoke$arity$3(oc.web.components.paginated_stream.list_item,derived_props,row_props));
});
var width = (cljs.core.truth_(is_mobile_QMARK_)?window.innerWidth:(620));
return React.createElement("div",({"ref": registerChild, "data-render-key": cljs.core.deref(new cljs.core.Keyword("oc.web.components.paginated-stream","force-re-render","oc.web.components.paginated-stream/force-re-render",295163146).cljs$core$IFn$_invoke$arity$1(s)), "key": ["virtualized-list-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(key_prefix)].join(''), "className": "virtualized-list-container"}),sablono.interpreter.interpret(oc.web.components.paginated_stream.virtualized_grid(cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"ref","ref",1289896967),new cljs.core.Keyword(null,"columnWidth","columnWidth",-88892889),new cljs.core.Keyword(null,"width","width",-384071477),new cljs.core.Keyword(null,"deferredMeasurementCache","deferredMeasurementCache",1865837),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.Keyword(null,"autoHeight","autoHeight",278057202),new cljs.core.Keyword(null,"columnCount","columnCount",-1668864970),new cljs.core.Keyword(null,"cellRenderer","cellRenderer",-537451658),new cljs.core.Keyword(null,"rowCount","rowCount",917416504),new cljs.core.Keyword(null,"onScroll","onScroll",-1660866632),new cljs.core.Keyword(null,"estimatedRowSize","estimatedRowSize",1595076793),new cljs.core.Keyword(null,"rowHeight","rowHeight",1730581244),new cljs.core.Keyword(null,"isScrolling","isScrolling",-1204024548),new cljs.core.Keyword(null,"height","height",1025178622),new cljs.core.Keyword(null,"scrollTop","scrollTop",-1143661921)],[new cljs.core.Keyword(null,"virtualized-list-comp","virtualized-list-comp",-536282811),width,width,cljs.core.deref(new cljs.core.Keyword("oc.web.components.paginated-stream","cache","oc.web.components.paginated-stream/cache",1753465102).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"outline","outline",793464534),"none"], null),true,(1),cljs.core.partial.cljs$core$IFn$_invoke$arity$2(cell_measurer_renderer,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"cache","cache",-1237023054),cljs.core.deref(new cljs.core.Keyword("oc.web.components.paginated-stream","cache","oc.web.components.paginated-stream/cache",1753465102).cljs$core$IFn$_invoke$arity$1(s))], null)),cljs.core.count(items),onChildScroll,(cljs.core.truth_(is_mobile_QMARK_)?oc.web.components.paginated_stream.mobile_foc_height:oc.web.components.paginated_stream.foc_height),cljs.core.deref(new cljs.core.Keyword("oc.web.components.paginated-stream","cache","oc.web.components.paginated-stream/cache",1753465102).cljs$core$IFn$_invoke$arity$1(s)).rowHeight,isScrolling,height,scrollTop]))));
}),new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$,oc.web.mixins.seen.container_nav_mixin(),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.paginated-stream","row-keys","oc.web.components.paginated-stream/row-keys",-618219090)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.paginated-stream","cache","oc.web.components.paginated-stream/cache",1753465102)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.paginated-stream","force-re-render","oc.web.components.paginated-stream/force-re-render",295163146)),oc.web.mixins.ui.mounted_flag,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
var props_46684 = cljs.core.first((function (){var G__46607 = new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s);
return (s.cljs$core$IFn$_invoke$arity$1 ? s.cljs$core$IFn$_invoke$arity$1(G__46607) : s.call(null,G__46607));
})());
var replies_QMARK__46685 = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"container-slug","container-slug",365736492).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"container-data","container-data",-53681130).cljs$core$IFn$_invoke$arity$1(props_46684)),new cljs.core.Keyword(null,"replies","replies",-1389888974));
var next_row_keys_46686 = cljs.core.mapv.cljs$core$IFn$_invoke$arity$2(cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.components.paginated_stream.unique_row_string,replies_QMARK__46685),new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(props_46684));
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.paginated-stream","force-re-render","oc.web.components.paginated-stream/force-re-render",295163146).cljs$core$IFn$_invoke$arity$1(s),oc.web.lib.utils.activity_uuid());

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.paginated-stream","cache","oc.web.components.paginated-stream/cache",1753465102).cljs$core$IFn$_invoke$arity$1(s),(new oc.web.components.paginated_stream.RVCellMeasurerCache(cljs.core.clj__GT_js(new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"defaultHeight","defaultHeight",295585992),oc.web.components.paginated_stream.calc_card_height(new cljs.core.Keyword(null,"is-mobile?","is-mobile?",2146205507).cljs$core$IFn$_invoke$arity$1(props_46684),new cljs.core.Keyword(null,"foc-layout","foc-layout",-1925028965).cljs$core$IFn$_invoke$arity$1(props_46684)),new cljs.core.Keyword(null,"minHeight","minHeight",-1635998980),(1),new cljs.core.Keyword(null,"fixedWidth","fixedWidth",-2132946700),true], null)))));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.paginated-stream","row-keys","oc.web.components.paginated-stream/row-keys",-618219090).cljs$core$IFn$_invoke$arity$1(s),next_row_keys_46686);

return s;
}),new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
oc.web.components.paginated_stream.setup_onload_recalc(s);

return s;
}),new cljs.core.Keyword(null,"did-remount","did-remount",1362550500),(function (_,s){
var props_46687 = cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s));
var replies_QMARK__46688 = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"container-slug","container-slug",365736492).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"container-data","container-data",-53681130).cljs$core$IFn$_invoke$arity$1(props_46687)),new cljs.core.Keyword(null,"replies","replies",-1389888974));
var next_row_keys_46689 = cljs.core.mapv.cljs$core$IFn$_invoke$arity$2(cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.components.paginated_stream.unique_row_string,replies_QMARK__46688),new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(props_46687));
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(new cljs.core.Keyword("oc.web.components.paginated-stream","row-keys","oc.web.components.paginated-stream/row-keys",-618219090).cljs$core$IFn$_invoke$arity$1(s)),next_row_keys_46689)){
} else {
oc.web.components.paginated_stream.clear_changed_cells_cache(s,next_row_keys_46689);
}

oc.web.components.paginated_stream.setup_onload_recalc(s);

return s;
})], null)], null),"virtualized-stream");
if((typeof oc !== 'undefined') && (typeof oc.web !== 'undefined') && (typeof oc.web.components !== 'undefined') && (typeof oc.web.components.paginated_stream !== 'undefined') && (typeof oc.web.components.paginated_stream.last_scroll_top !== 'undefined')){
} else {
oc.web.components.paginated_stream.last_scroll_top = cljs.core.atom.cljs$core$IFn$_invoke$arity$1((0));
}
/**
 * Scroll listener, load more activities when the scroll is close to a margin.
 */
oc.web.components.paginated_stream.did_scroll = (function oc$web$components$paginated_stream$did_scroll(var_args){
var G__46609 = arguments.length;
switch (G__46609) {
case 1:
return oc.web.components.paginated_stream.did_scroll.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.components.paginated_stream.did_scroll.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.components.paginated_stream.did_scroll.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.components.paginated_stream.did_scroll.cljs$core$IFn$_invoke$arity$1 = (function (s){
return oc.web.components.paginated_stream.did_scroll.cljs$core$IFn$_invoke$arity$3(s,oc.web.lib.responsive.is_mobile_size_QMARK_(),null);
}));

(oc.web.components.paginated_stream.did_scroll.cljs$core$IFn$_invoke$arity$2 = (function (s,mobile_QMARK_){
return oc.web.components.paginated_stream.did_scroll.cljs$core$IFn$_invoke$arity$3(s,mobile_QMARK_,null);
}));

(oc.web.components.paginated_stream.did_scroll.cljs$core$IFn$_invoke$arity$3 = (function (s,mobile_QMARK_,e){
var scroll_top = (function (){var or__4126__auto__ = document.scrollingElement.scrollTop;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return window.pageYOffset;
}
})();
var direction = (((cljs.core.deref(oc.web.components.paginated_stream.last_scroll_top) > scroll_top))?new cljs.core.Keyword(null,"up","up",-269712113):(((cljs.core.deref(oc.web.components.paginated_stream.last_scroll_top) < scroll_top))?new cljs.core.Keyword(null,"down","down",1565245570):new cljs.core.Keyword(null,"stale","stale",395586896)));
var win_height = oc.web.utils.dom.viewport_height();
var max_scroll = (document.scrollingElement.scrollHeight - win_height);
var is_mobile_QMARK_ = (((mobile_QMARK_ == null))?oc.web.lib.responsive.is_mobile_size_QMARK_():mobile_QMARK_);
var pnr = (max_scroll - win_height);
var current_board_slug = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"board-slug","board-slug",99003663)));
var current_contributions_id = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"contributions-id","contributions-id",-67679488)));
var current_contributions_id__$1 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"contributions-id","contributions-id",-67679488)));
var board_kw = cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug);
if(cljs.core.truth_(((cljs.core.not(cljs.core.deref(new cljs.core.Keyword("oc.web.components.paginated-stream","bottom-loading","oc.web.components.paginated-stream/bottom-loading",-200703807).cljs$core$IFn$_invoke$arity$1(s))))?(function (){var and__4115__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.paginated-stream","has-next","oc.web.components.paginated-stream/has-next",-143153620).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(and__4115__auto__)){
return ((((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(direction,new cljs.core.Keyword(null,"down","down",1565245570))) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(direction,new cljs.core.Keyword(null,"stale","stale",395586896))))) && ((scroll_top >= pnr)));
} else {
return and__4115__auto__;
}
})():false))){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.paginated-stream","bottom-loading","oc.web.components.paginated-stream/bottom-loading",-200703807).cljs$core$IFn$_invoke$arity$1(s),true);

if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(board_kw,new cljs.core.Keyword(null,"replies","replies",-1389888974))){
oc.web.actions.activity.replies_more(cljs.core.deref(new cljs.core.Keyword("oc.web.components.paginated-stream","has-next","oc.web.components.paginated-stream/has-next",-143153620).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"down","down",1565245570));
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(board_kw,new cljs.core.Keyword(null,"following","following",-2049193617))){
oc.web.actions.activity.following_more(cljs.core.deref(new cljs.core.Keyword("oc.web.components.paginated-stream","has-next","oc.web.components.paginated-stream/has-next",-143153620).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"down","down",1565245570));
} else {
if(cljs.core.seq(current_contributions_id__$1)){
oc.web.actions.contributions.contributions_more(cljs.core.deref(new cljs.core.Keyword("oc.web.components.paginated-stream","has-next","oc.web.components.paginated-stream/has-next",-143153620).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"down","down",1565245570));
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(board_kw,new cljs.core.Keyword(null,"inbox","inbox",1888669443))){
oc.web.actions.activity.inbox_more(cljs.core.deref(new cljs.core.Keyword("oc.web.components.paginated-stream","has-next","oc.web.components.paginated-stream/has-next",-143153620).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"down","down",1565245570));
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(board_kw,new cljs.core.Keyword(null,"all-posts","all-posts",-1285476533))){
oc.web.actions.activity.all_posts_more(cljs.core.deref(new cljs.core.Keyword("oc.web.components.paginated-stream","has-next","oc.web.components.paginated-stream/has-next",-143153620).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"down","down",1565245570));
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(board_kw,new cljs.core.Keyword(null,"bookmarks","bookmarks",1877375283))){
oc.web.actions.activity.bookmarks_more(cljs.core.deref(new cljs.core.Keyword("oc.web.components.paginated-stream","has-next","oc.web.components.paginated-stream/has-next",-143153620).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"down","down",1565245570));
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(board_kw,new cljs.core.Keyword(null,"unfollowing","unfollowing",-1076165830))){
oc.web.actions.activity.unfollowing_more(cljs.core.deref(new cljs.core.Keyword("oc.web.components.paginated-stream","has-next","oc.web.components.paginated-stream/has-next",-143153620).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"down","down",1565245570));
} else {
if(cljs.core.not(oc.web.dispatcher.is_container_QMARK_(current_board_slug))){
oc.web.actions.section.section_more(cljs.core.deref(new cljs.core.Keyword("oc.web.components.paginated-stream","has-next","oc.web.components.paginated-stream/has-next",-143153620).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"down","down",1565245570));
} else {
}
}
}
}
}
}
}
}
} else {
}

return cljs.core.reset_BANG_(oc.web.components.paginated_stream.last_scroll_top,(function (){var x__4214__auto__ = (0);
var y__4215__auto__ = scroll_top;
return ((x__4214__auto__ > y__4215__auto__) ? x__4214__auto__ : y__4215__auto__);
})());
}));

(oc.web.components.paginated_stream.did_scroll.cljs$lang$maxFixedArity = 3);

oc.web.components.paginated_stream.check_pagination = (function oc$web$components$paginated_stream$check_pagination(s){
var container_data = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"container-data","container-data",-53681130)));
var next_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(container_data),"next"], 0));
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.paginated-stream","has-next","oc.web.components.paginated-stream/has-next",-143153620).cljs$core$IFn$_invoke$arity$1(s),next_link);

return oc.web.components.paginated_stream.did_scroll.cljs$core$IFn$_invoke$arity$1(s);
});
oc.web.components.paginated_stream.paginated_stream = rum.core.build_defcs((function (s){
var org_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"org-data","org-data",96720321));
var _board_slug = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"board-slug","board-slug",99003663));
var _contributions_id = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"contributions-id","contributions-id",-67679488));
var editable_boards = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"editable-boards","editable-boards",1897056658));
var container_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"container-data","container-data",-53681130));
var items = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"items-to-render","items-to-render",660219762));
var activities_read = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"activities-read","activities-read",2125722631));
var current_user_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915));
var add_comment_force_update = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"add-comment-force-update","add-comment-force-update",1376707794));
var viewport_height = oc.web.utils.dom.viewport_height();
var is_mobile_QMARK_ = oc.web.lib.responsive.is_mobile_size_QMARK_();
var member_QMARK_ = new cljs.core.Keyword(null,"member?","member?",486668360).cljs$core$IFn$_invoke$arity$1(org_data);
var replies_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"container-slug","container-slug",365736492).cljs$core$IFn$_invoke$arity$1(container_data),new cljs.core.Keyword(null,"replies","replies",-1389888974));
return React.createElement("div",({"className": "paginated-stream group"}),React.createElement("div",({"className": "paginated-stream-cards"}),(function (){var attrs46611 = ((replies_QMARK_)?rum.core.with_key((function (){var G__46612 = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"items-to-render","items-to-render",660219762),items], null);
return (oc.web.components.stream_reply_item.replies_refresh_button.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.stream_reply_item.replies_refresh_button.cljs$core$IFn$_invoke$arity$1(G__46612) : oc.web.components.stream_reply_item.replies_refresh_button.call(null,G__46612));
})(),["replies-refresh-button-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"last-seen-at","last-seen-at",1929467667).cljs$core$IFn$_invoke$arity$1(container_data))].join('')):null);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46611))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["paginated-stream-cards-inner","group"], null)], null),attrs46611], 0))):({"className": "paginated-stream-cards-inner group"})),((cljs.core.map_QMARK_(attrs46611))?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(oc.web.components.paginated_stream.window_scroller(cljs.core.PersistentArrayMap.EMPTY,cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.components.paginated_stream.virtualized_stream,new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"org-data","org-data",96720321),org_data,new cljs.core.Keyword(null,"items","items",1031954938),items,new cljs.core.Keyword(null,"container-data","container-data",-53681130),container_data,new cljs.core.Keyword(null,"is-mobile?","is-mobile?",2146205507),is_mobile_QMARK_,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915),current_user_data,new cljs.core.Keyword(null,"activities-read","activities-read",2125722631),activities_read,new cljs.core.Keyword(null,"editable-boards","editable-boards",1897056658),editable_boards], null))))], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46611),sablono.interpreter.interpret(oc.web.components.paginated_stream.window_scroller(cljs.core.PersistentArrayMap.EMPTY,cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.components.paginated_stream.virtualized_stream,new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"org-data","org-data",96720321),org_data,new cljs.core.Keyword(null,"items","items",1031954938),items,new cljs.core.Keyword(null,"container-data","container-data",-53681130),container_data,new cljs.core.Keyword(null,"is-mobile?","is-mobile?",2146205507),is_mobile_QMARK_,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915),current_user_data,new cljs.core.Keyword(null,"activities-read","activities-read",2125722631),activities_read,new cljs.core.Keyword(null,"editable-boards","editable-boards",1897056658),editable_boards], null))))], null)));
})()));
}),new cljs.core.PersistentVector(null, 18, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$,rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"org-data","org-data",96720321)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"items-to-render","items-to-render",660219762)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"container-data","container-data",-53681130)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"activities-read","activities-read",2125722631)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"editable-boards","editable-boards",1897056658)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"board-slug","board-slug",99003663)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"contributions-id","contributions-id",-67679488)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"add-comment-force-update","add-comment-force-update",1376707794)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.paginated-stream","scroll-listener","oc.web.components.paginated-stream/scroll-listener",630043020)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.paginated-stream","has-next","oc.web.components.paginated-stream/has-next",-143153620)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.paginated-stream","bottom-loading","oc.web.components.paginated-stream/bottom-loading",-200703807)),oc.web.mixins.ui.first_render_mixin,oc.web.mixins.section.container_nav_in,oc.web.mixins.section.load_entry_comments.cljs$core$IFn$_invoke$arity$1((function (s){
var container_data = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"container-data","container-data",-53681130)));
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"container-slug","container-slug",365736492).cljs$core$IFn$_invoke$arity$1(container_data),new cljs.core.Keyword(null,"replies","replies",-1389888974))){
return container_data;
} else {
return null;
}
})),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
cljs.core.reset_BANG_(oc.web.components.paginated_stream.last_scroll_top,document.scrollingElement.scrollTop);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.paginated-stream","scroll-listener","oc.web.components.paginated-stream/scroll-listener",630043020).cljs$core$IFn$_invoke$arity$1(s),goog.events.listen(window,goog.events.EventType.SCROLL,cljs.core.partial.cljs$core$IFn$_invoke$arity$3(oc.web.components.paginated_stream.did_scroll,s,oc.web.lib.responsive.is_mobile_size_QMARK_())));

return s;
}),new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
cljs.core.reset_BANG_(oc.web.components.paginated_stream.last_scroll_top,document.scrollingElement.scrollTop);

oc.web.components.paginated_stream.check_pagination(s);

return s;
}),new cljs.core.Keyword(null,"did-remount","did-remount",1362550500),(function (_,s){
oc.web.components.paginated_stream.check_pagination(s);

return s;
}),new cljs.core.Keyword(null,"before-render","before-render",71256781),(function (s){
var container_data_46691 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"container-data","container-data",-53681130)));
if(cljs.core.truth_(((cljs.core.not(new cljs.core.Keyword(null,"loading-more","loading-more",-1145525667).cljs$core$IFn$_invoke$arity$1(container_data_46691)))?cljs.core.deref(new cljs.core.Keyword("oc.web.components.paginated-stream","bottom-loading","oc.web.components.paginated-stream/bottom-loading",-200703807).cljs$core$IFn$_invoke$arity$1(s)):false))){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.paginated-stream","bottom-loading","oc.web.components.paginated-stream/bottom-loading",-200703807).cljs$core$IFn$_invoke$arity$1(s),false);

oc.web.components.paginated_stream.check_pagination(s);
} else {
}

return s;
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.paginated-stream","scroll-listener","oc.web.components.paginated-stream/scroll-listener",630043020).cljs$core$IFn$_invoke$arity$1(s)))){
goog.events.unlistenByKey(cljs.core.deref(new cljs.core.Keyword("oc.web.components.paginated-stream","scroll-listener","oc.web.components.paginated-stream/scroll-listener",630043020).cljs$core$IFn$_invoke$arity$1(s)));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.paginated-stream","scroll-listener","oc.web.components.paginated-stream/scroll-listener",630043020).cljs$core$IFn$_invoke$arity$1(s),null);
} else {
}

return s;
})], null)], null),"paginated-stream");

//# sourceMappingURL=oc.web.components.paginated_stream.js.map
