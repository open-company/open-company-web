goog.provide('oc.web.components.ui.sections_picker');
oc.web.components.ui.sections_picker.self_board_name = "All";
oc.web.components.ui.sections_picker.self_board = (function oc$web$components$ui$sections_picker$self_board(user_data){
return new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"name","name",1843675177),"",new cljs.core.Keyword(null,"slug","slug",2029314850),oc.web.lib.utils.default_section_slug,new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803),true,new cljs.core.Keyword(null,"access","access",2027349272),"team",new cljs.core.Keyword(null,"authors","authors",2063018172),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user_data)], null)], null);
});
oc.web.components.ui.sections_picker.distance_from_bottom = (80);
oc.web.components.ui.sections_picker.calc_max_height = (function oc$web$components$ui$sections_picker$calc_max_height(s){
var win_height = (function (){var or__4126__auto__ = document.documentElement.clientHeight;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return window.innerHeight;
}
})();
var dom_node = rum.core.dom_node(s);
var scroll_top = document.scrollingElement.scrollTop;
var body_rect = document.body.getBoundingClientRect();
var elem_rect = dom_node.getBoundingClientRect();
var offset_top = (elem_rect.top - (body_rect.top + scroll_top));
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.sections-picker","container-max-height","oc.web.components.ui.sections-picker/container-max-height",929447429).cljs$core$IFn$_invoke$arity$1(s),(function (){var x__4214__auto__ = (239);
var y__4215__auto__ = (((win_height - offset_top) - (8)) - oc.web.components.ui.sections_picker.distance_from_bottom);
return ((x__4214__auto__ > y__4215__auto__) ? x__4214__auto__ : y__4215__auto__);
})());
});
oc.web.components.ui.sections_picker.sections_picker = rum.core.build_defcs((function (s,p__39420){
var map__39421 = p__39420;
var map__39421__$1 = (((((!((map__39421 == null))))?(((((map__39421.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__39421.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__39421):map__39421);
var active_slug = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39421__$1,new cljs.core.Keyword(null,"active-slug","active-slug",-1213605190));
var on_change = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39421__$1,new cljs.core.Keyword(null,"on-change","on-change",-732046149));
var moving_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39421__$1,new cljs.core.Keyword(null,"moving?","moving?",1843710132));
var current_user_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39421__$1,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915));
var org_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"org-data","org-data",96720321));
var editable_boards = cljs.core.vals(org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"editable-boards","editable-boards",1897056658)));
var author_QMARK_ = cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"role","role",-736691072).cljs$core$IFn$_invoke$arity$1(current_user_data),new cljs.core.Keyword(null,"viewer","viewer",-783949853));
var user_publisher_board = cljs.core.some((function (p1__39418_SHARP_){
if(cljs.core.truth_((function (){var and__4115__auto__ = new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803).cljs$core$IFn$_invoke$arity$1(p1__39418_SHARP_);
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"author","author",2111686192).cljs$core$IFn$_invoke$arity$1(p1__39418_SHARP_)),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(current_user_data));
} else {
return and__4115__auto__;
}
})())){
return p1__39418_SHARP_;
} else {
return null;
}
}),editable_boards);
var filtered_boards = cljs.core.filter.cljs$core$IFn$_invoke$arity$2(cljs.core.comp.cljs$core$IFn$_invoke$arity$2(cljs.core.not,new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803)),editable_boards);
var sorted_boards = cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"name","name",1843675177),filtered_boards);
var fixed_publisher_board = (function (){var or__4126__auto__ = user_publisher_board;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.components.ui.sections_picker.self_board(current_user_data);
}
})();
var all_sections = ((((author_QMARK_) && (oc.web.local_settings.publisher_board_enabled_QMARK_)))?cljs.core.cons(fixed_publisher_board,sorted_boards):sorted_boards);
var container_style = (cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.sections-picker","container-max-height","oc.web.components.ui.sections-picker/container-max-height",929447429).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"max-height","max-height",-612563804),[cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.sections-picker","container-max-height","oc.web.components.ui.sections-picker/container-max-height",929447429).cljs$core$IFn$_invoke$arity$1(s))),"px"].join('')], null):cljs.core.PersistentArrayMap.EMPTY);
var scroller_style = (cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.sections-picker","container-max-height","oc.web.components.ui.sections-picker/container-max-height",929447429).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"max-height","max-height",-612563804),[cljs.core.str.cljs$core$IFn$_invoke$arity$1((cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.sections-picker","container-max-height","oc.web.components.ui.sections-picker/container-max-height",929447429).cljs$core$IFn$_invoke$arity$1(s)) - (55))),"px"].join('')], null):cljs.core.PersistentArrayMap.EMPTY);
var is_mobile_QMARK_ = oc.web.lib.responsive.is_tablet_or_mobile_QMARK_();
return React.createElement("div",({"style": sablono.interpreter.attributes(container_style), "className": "sections-picker"}),React.createElement("div",({"style": sablono.interpreter.attributes(scroller_style), "className": "sections-picker-content"}),sablono.interpreter.interpret((((cljs.core.count(all_sections) > (0)))?(function (){var iter__4529__auto__ = (function oc$web$components$ui$sections_picker$iter__39423(s__39424){
return (new cljs.core.LazySeq(null,(function (){
var s__39424__$1 = s__39424;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__39424__$1);
if(temp__5735__auto__){
var s__39424__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__39424__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__39424__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__39426 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__39425 = (0);
while(true){
if((i__39425 < size__4528__auto__)){
var b = cljs.core._nth(c__4527__auto__,i__39425);
var active = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(b),active_slug);
var self_board_QMARK_ = new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803).cljs$core$IFn$_invoke$arity$1(b);
var fixed_board = ((((author_QMARK_) && (cljs.core.not(self_board_QMARK_)) && (active)))?fixed_publisher_board:cljs.core.update.cljs$core$IFn$_invoke$arity$3(b,new cljs.core.Keyword(null,"name","name",1843675177),((function (i__39425,active,self_board_QMARK_,b,c__4527__auto__,size__4528__auto__,b__39426,s__39424__$2,temp__5735__auto__,org_data,editable_boards,author_QMARK_,user_publisher_board,filtered_boards,sorted_boards,fixed_publisher_board,all_sections,container_style,scroller_style,is_mobile_QMARK_,map__39421,map__39421__$1,active_slug,on_change,moving_QMARK_,current_user_data){
return (function (p1__39419_SHARP_){
if(cljs.core.truth_(self_board_QMARK_)){
return oc.web.components.ui.sections_picker.self_board_name;
} else {
return p1__39419_SHARP_;
}
});})(i__39425,active,self_board_QMARK_,b,c__4527__auto__,size__4528__auto__,b__39426,s__39424__$2,temp__5735__auto__,org_data,editable_boards,author_QMARK_,user_publisher_board,filtered_boards,sorted_boards,fixed_publisher_board,all_sections,container_style,scroller_style,is_mobile_QMARK_,map__39421,map__39421__$1,active_slug,on_change,moving_QMARK_,current_user_data))
));
cljs.core.chunk_append(b__39426,new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.sections-picker-section","div.sections-picker-section",1973971678),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"key","key",-1516042587),["sections-picker-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(b))].join(''),new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"active","active",1895962068),active,new cljs.core.Keyword(null,"has-access-icon","has-access-icon",-989545776),(function (){var G__39428 = new cljs.core.Keyword(null,"access","access",2027349272).cljs$core$IFn$_invoke$arity$1(b);
var fexpr__39427 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, ["private",null,"public",null], null), null);
return (fexpr__39427.cljs$core$IFn$_invoke$arity$1 ? fexpr__39427.cljs$core$IFn$_invoke$arity$1(G__39428) : fexpr__39427.call(null,G__39428));
})(),new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803),new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803).cljs$core$IFn$_invoke$arity$1(b)], null)),new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (i__39425,active,self_board_QMARK_,fixed_board,b,c__4527__auto__,size__4528__auto__,b__39426,s__39424__$2,temp__5735__auto__,org_data,editable_boards,author_QMARK_,user_publisher_board,filtered_boards,sorted_boards,fixed_publisher_board,all_sections,container_style,scroller_style,is_mobile_QMARK_,map__39421,map__39421__$1,active_slug,on_change,moving_QMARK_,current_user_data){
return (function (){
if(cljs.core.fn_QMARK_(on_change)){
return (on_change.cljs$core$IFn$_invoke$arity$1 ? on_change.cljs$core$IFn$_invoke$arity$1(fixed_board) : on_change.call(null,fixed_board));
} else {
return null;
}
});})(i__39425,active,self_board_QMARK_,fixed_board,b,c__4527__auto__,size__4528__auto__,b__39426,s__39424__$2,temp__5735__auto__,org_data,editable_boards,author_QMARK_,user_publisher_board,filtered_boards,sorted_boards,fixed_publisher_board,all_sections,container_style,scroller_style,is_mobile_QMARK_,map__39421,map__39421__$1,active_slug,on_change,moving_QMARK_,current_user_data))
], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.sections-picker-section-name","div.sections-picker-section-name",-659055497),(cljs.core.truth_(self_board_QMARK_)?oc.web.components.ui.sections_picker.self_board_name:new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(b))], null),(function (){var G__39429 = new cljs.core.Keyword(null,"access","access",2027349272).cljs$core$IFn$_invoke$arity$1(b);
switch (G__39429) {
case "private":
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.private-icon","div.private-icon",-1368458406)], null);

break;
case "public":
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.public-icon","div.public-icon",-889402079)], null);

break;
default:
return null;

}
})()], null));

var G__39440 = (i__39425 + (1));
i__39425 = G__39440;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__39426),oc$web$components$ui$sections_picker$iter__39423(cljs.core.chunk_rest(s__39424__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__39426),null);
}
} else {
var b = cljs.core.first(s__39424__$2);
var active = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(b),active_slug);
var self_board_QMARK_ = new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803).cljs$core$IFn$_invoke$arity$1(b);
var fixed_board = ((((author_QMARK_) && (cljs.core.not(self_board_QMARK_)) && (active)))?fixed_publisher_board:cljs.core.update.cljs$core$IFn$_invoke$arity$3(b,new cljs.core.Keyword(null,"name","name",1843675177),((function (active,self_board_QMARK_,b,s__39424__$2,temp__5735__auto__,org_data,editable_boards,author_QMARK_,user_publisher_board,filtered_boards,sorted_boards,fixed_publisher_board,all_sections,container_style,scroller_style,is_mobile_QMARK_,map__39421,map__39421__$1,active_slug,on_change,moving_QMARK_,current_user_data){
return (function (p1__39419_SHARP_){
if(cljs.core.truth_(self_board_QMARK_)){
return oc.web.components.ui.sections_picker.self_board_name;
} else {
return p1__39419_SHARP_;
}
});})(active,self_board_QMARK_,b,s__39424__$2,temp__5735__auto__,org_data,editable_boards,author_QMARK_,user_publisher_board,filtered_boards,sorted_boards,fixed_publisher_board,all_sections,container_style,scroller_style,is_mobile_QMARK_,map__39421,map__39421__$1,active_slug,on_change,moving_QMARK_,current_user_data))
));
return cljs.core.cons(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.sections-picker-section","div.sections-picker-section",1973971678),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"key","key",-1516042587),["sections-picker-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(b))].join(''),new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"active","active",1895962068),active,new cljs.core.Keyword(null,"has-access-icon","has-access-icon",-989545776),(function (){var G__39433 = new cljs.core.Keyword(null,"access","access",2027349272).cljs$core$IFn$_invoke$arity$1(b);
var fexpr__39432 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, ["private",null,"public",null], null), null);
return (fexpr__39432.cljs$core$IFn$_invoke$arity$1 ? fexpr__39432.cljs$core$IFn$_invoke$arity$1(G__39433) : fexpr__39432.call(null,G__39433));
})(),new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803),new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803).cljs$core$IFn$_invoke$arity$1(b)], null)),new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (active,self_board_QMARK_,fixed_board,b,s__39424__$2,temp__5735__auto__,org_data,editable_boards,author_QMARK_,user_publisher_board,filtered_boards,sorted_boards,fixed_publisher_board,all_sections,container_style,scroller_style,is_mobile_QMARK_,map__39421,map__39421__$1,active_slug,on_change,moving_QMARK_,current_user_data){
return (function (){
if(cljs.core.fn_QMARK_(on_change)){
return (on_change.cljs$core$IFn$_invoke$arity$1 ? on_change.cljs$core$IFn$_invoke$arity$1(fixed_board) : on_change.call(null,fixed_board));
} else {
return null;
}
});})(active,self_board_QMARK_,fixed_board,b,s__39424__$2,temp__5735__auto__,org_data,editable_boards,author_QMARK_,user_publisher_board,filtered_boards,sorted_boards,fixed_publisher_board,all_sections,container_style,scroller_style,is_mobile_QMARK_,map__39421,map__39421__$1,active_slug,on_change,moving_QMARK_,current_user_data))
], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.sections-picker-section-name","div.sections-picker-section-name",-659055497),(cljs.core.truth_(self_board_QMARK_)?oc.web.components.ui.sections_picker.self_board_name:new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(b))], null),(function (){var G__39436 = new cljs.core.Keyword(null,"access","access",2027349272).cljs$core$IFn$_invoke$arity$1(b);
switch (G__39436) {
case "private":
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.private-icon","div.private-icon",-1368458406)], null);

break;
case "public":
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.public-icon","div.public-icon",-889402079)], null);

break;
default:
return null;

}
})()], null),oc$web$components$ui$sections_picker$iter__39423(cljs.core.rest(s__39424__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(all_sections);
})():null))));
}),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"org-data","org-data",96720321)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"editable-boards","editable-boards",1897056658)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.ui.sections-picker","container-max-height","oc.web.components.ui.sections-picker/container-max-height",929447429)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
oc.web.components.ui.sections_picker.calc_max_height(s);

return s;
}),new cljs.core.Keyword(null,"did-remount","did-remount",1362550500),(function (_,s){
oc.web.components.ui.sections_picker.calc_max_height(s);

return s;
})], null)], null),"sections-picker");

//# sourceMappingURL=oc.web.components.ui.sections_picker.js.map
