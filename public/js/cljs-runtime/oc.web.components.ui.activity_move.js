goog.provide('oc.web.components.ui.activity_move');
oc.web.components.ui.activity_move.move_post = (function oc$web$components$ui$activity_move$move_post(s){
var opts = cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s));
var activity_data = new cljs.core.Keyword(null,"activity-data","activity-data",1293689136).cljs$core$IFn$_invoke$arity$1(opts);
var new_board = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.activity-move","selected-board","oc.web.components.ui.activity-move/selected-board",220386427).cljs$core$IFn$_invoke$arity$1(s));
var on_change = new cljs.core.Keyword(null,"on-change","on-change",-732046149).cljs$core$IFn$_invoke$arity$1(opts);
var dismiss_cb = new cljs.core.Keyword(null,"dismiss-cb","dismiss-cb",-1282537857).cljs$core$IFn$_invoke$arity$1(opts);
oc.web.actions.activity.activity_move(activity_data,new_board);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.activity-move","show-boards-list","oc.web.components.ui.activity-move/show-boards-list",527378222).cljs$core$IFn$_invoke$arity$1(s),false);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.activity-move","selected-board","oc.web.components.ui.activity-move/selected-board",220386427).cljs$core$IFn$_invoke$arity$1(s),null);

if(cljs.core.fn_QMARK_(on_change)){
(on_change.cljs$core$IFn$_invoke$arity$0 ? on_change.cljs$core$IFn$_invoke$arity$0() : on_change.call(null));
} else {
}

if(cljs.core.fn_QMARK_(dismiss_cb)){
return (dismiss_cb.cljs$core$IFn$_invoke$arity$0 ? dismiss_cb.cljs$core$IFn$_invoke$arity$0() : dismiss_cb.call(null));
} else {
return null;
}
});
oc.web.components.ui.activity_move.activity_move = rum.core.build_defcs((function (s,p__46012){
var map__46013 = p__46012;
var map__46013__$1 = (((((!((map__46013 == null))))?(((((map__46013.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46013.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46013):map__46013);
var boards_list = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46013__$1,new cljs.core.Keyword(null,"boards-list","boards-list",1343890425));
var activity_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46013__$1,new cljs.core.Keyword(null,"activity-data","activity-data",1293689136));
var dismiss_cb = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46013__$1,new cljs.core.Keyword(null,"dismiss-cb","dismiss-cb",-1282537857));
var sorted_boards_list = cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"name","name",1843675177),boards_list);
return React.createElement("div",({"onClick": (function (p1__46009_SHARP_){
oc.web.lib.utils.event_stop(p1__46009_SHARP_);

if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.activity-move","show-boards-list","oc.web.components.ui.activity-move/show-boards-list",527378222).cljs$core$IFn$_invoke$arity$1(s)))){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.activity-move","show-boards-list","oc.web.components.ui.activity-move/show-boards-list",527378222).cljs$core$IFn$_invoke$arity$1(s),false);
} else {
return null;
}
}), "className": "activity-move"}),React.createElement("div",({"className": "move-post-inner"}),React.createElement("div",({"className": "move-post-title"}),"Move"),React.createElement("div",({"onClick": (function (p1__46010_SHARP_){
oc.web.lib.utils.event_stop(p1__46010_SHARP_);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.activity-move","show-boards-list","oc.web.components.ui.activity-move/show-boards-list",527378222).cljs$core$IFn$_invoke$arity$1(s),cljs.core.not(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.activity-move","show-boards-list","oc.web.components.ui.activity-move/show-boards-list",527378222).cljs$core$IFn$_invoke$arity$1(s))));
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["select-new-board","oc-input",oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"placeholder","placeholder",-104873083),(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.activity-move","selected-board","oc.web.components.ui.activity-move/selected-board",220386427).cljs$core$IFn$_invoke$arity$1(s)) == null),new cljs.core.Keyword(null,"active","active",1895962068),cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.activity-move","show-boards-list","oc.web.components.ui.activity-move/show-boards-list",527378222).cljs$core$IFn$_invoke$arity$1(s))], null))], null))}),sablono.interpreter.interpret((function (){var or__4126__auto__ = new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.activity-move","selected-board","oc.web.components.ui.activity-move/selected-board",220386427).cljs$core$IFn$_invoke$arity$1(s)));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "Move to...";
}
})())),sablono.interpreter.interpret((cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.activity-move","show-boards-list","oc.web.components.ui.activity-move/show-boards-list",527378222).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.boards-list","div.boards-list",-1720140521),(function (){var iter__4529__auto__ = (function oc$web$components$ui$activity_move$iter__46018(s__46019){
return (new cljs.core.LazySeq(null,(function (){
var s__46019__$1 = s__46019;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__46019__$1);
if(temp__5735__auto__){
var s__46019__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__46019__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__46019__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__46021 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__46020 = (0);
while(true){
if((i__46020 < size__4528__auto__)){
var board = cljs.core._nth(c__4527__auto__,i__46020);
cljs.core.chunk_append(b__46021,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.board-item","div.board-item",-1492471991),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"key","key",-1516042587),["activity-move-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data)),"-board-list-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(board))].join(''),new cljs.core.Keyword(null,"class","class",-2030961996),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(activity_data),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(board)))?"disabled":null),new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (i__46020,board,c__4527__auto__,size__4528__auto__,b__46021,s__46019__$2,temp__5735__auto__,sorted_boards_list,map__46013,map__46013__$1,boards_list,activity_data,dismiss_cb){
return (function (){
if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(activity_data),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(board))){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.activity-move","selected-board","oc.web.components.ui.activity-move/selected-board",220386427).cljs$core$IFn$_invoke$arity$1(s),board);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.activity-move","show-boards-list","oc.web.components.ui.activity-move/show-boards-list",527378222).cljs$core$IFn$_invoke$arity$1(s),false);
} else {
return null;
}
});})(i__46020,board,c__4527__auto__,size__4528__auto__,b__46021,s__46019__$2,temp__5735__auto__,sorted_boards_list,map__46013,map__46013__$1,boards_list,activity_data,dismiss_cb))
], null),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(board)], null));

var G__46022 = (i__46020 + (1));
i__46020 = G__46022;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__46021),oc$web$components$ui$activity_move$iter__46018(cljs.core.chunk_rest(s__46019__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__46021),null);
}
} else {
var board = cljs.core.first(s__46019__$2);
return cljs.core.cons(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.board-item","div.board-item",-1492471991),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"key","key",-1516042587),["activity-move-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data)),"-board-list-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(board))].join(''),new cljs.core.Keyword(null,"class","class",-2030961996),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(activity_data),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(board)))?"disabled":null),new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (board,s__46019__$2,temp__5735__auto__,sorted_boards_list,map__46013,map__46013__$1,boards_list,activity_data,dismiss_cb){
return (function (){
if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(activity_data),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(board))){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.activity-move","selected-board","oc.web.components.ui.activity-move/selected-board",220386427).cljs$core$IFn$_invoke$arity$1(s),board);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.activity-move","show-boards-list","oc.web.components.ui.activity-move/show-boards-list",527378222).cljs$core$IFn$_invoke$arity$1(s),false);
} else {
return null;
}
});})(board,s__46019__$2,temp__5735__auto__,sorted_boards_list,map__46013,map__46013__$1,boards_list,activity_data,dismiss_cb))
], null),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(board)], null),oc$web$components$ui$activity_move$iter__46018(cljs.core.rest(s__46019__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(sorted_boards_list);
})()], null):null)),React.createElement("button",({"onClick": (function (p1__46011_SHARP_){
oc.web.lib.utils.event_stop(p1__46011_SHARP_);

return oc.web.components.ui.activity_move.move_post(s);
}), "disabled": cljs.core.not(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.activity-move","selected-board","oc.web.components.ui.activity-move/selected-board",220386427).cljs$core$IFn$_invoke$arity$1(s))), "className": "mlb-reset mlb-default"}),"Move post")));
}),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.ui.activity-move","show-boards-list","oc.web.components.ui.activity-move/show-boards-list",527378222)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.ui.activity-move","selected-board","oc.web.components.ui.activity-move/selected-board",220386427)),oc.web.mixins.ui.on_click_out.cljs$core$IFn$_invoke$arity$1((function (s,e){
var opts = cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s));
var dismiss_cb = new cljs.core.Keyword(null,"dismiss-cb","dismiss-cb",-1282537857).cljs$core$IFn$_invoke$arity$1(opts);
if(cljs.core.fn_QMARK_(dismiss_cb)){
return (dismiss_cb.cljs$core$IFn$_invoke$arity$0 ? dismiss_cb.cljs$core$IFn$_invoke$arity$0() : dismiss_cb.call(null));
} else {
return null;
}
}))], null),"activity-move");

//# sourceMappingURL=oc.web.components.ui.activity_move.js.map
