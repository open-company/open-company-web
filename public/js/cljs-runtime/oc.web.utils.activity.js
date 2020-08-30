goog.provide('oc.web.utils.activity');
oc.web.utils.activity.headline_placeholder = "Add a title";
oc.web.utils.activity.empty_body_html = "<p><br></p>";
oc.web.utils.activity.show_separators_QMARK_ = (function oc$web$utils$activity$show_separators_QMARK_(var_args){
var G__41693 = arguments.length;
switch (G__41693) {
case 1:
return oc.web.utils.activity.show_separators_QMARK_.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.utils.activity.show_separators_QMARK_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.utils.activity.show_separators_QMARK_.cljs$core$IFn$_invoke$arity$1 = (function (container_slug){
return oc.web.utils.activity.show_separators_QMARK_.cljs$core$IFn$_invoke$arity$2(container_slug,oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.utils.activity.show_separators_QMARK_.cljs$core$IFn$_invoke$arity$2 = (function (container_slug,sort_type){
if((!(cuerdas.core.blank_QMARK_(container_slug)))){
if(cljs.core.not(oc.web.lib.responsive.is_mobile_size_QMARK_())){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(sort_type,oc.web.dispatcher.recently_posted_sort)){
var G__41703 = cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(container_slug);
var fexpr__41702 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"all-posts","all-posts",-1285476533),null,new cljs.core.Keyword(null,"following","following",-2049193617),null,new cljs.core.Keyword(null,"contributions","contributions",-1280485964),null,new cljs.core.Keyword(null,"unfollowing","unfollowing",-1076165830),null], null), null);
return (fexpr__41702.cljs$core$IFn$_invoke$arity$1 ? fexpr__41702.cljs$core$IFn$_invoke$arity$1(G__41703) : fexpr__41702.call(null,G__41703));
} else {
return false;
}
} else {
return false;
}
} else {
return false;
}
}));

(oc.web.utils.activity.show_separators_QMARK_.cljs$lang$maxFixedArity = 2);

oc.web.utils.activity.post_month_date_from_date = (function oc$web$utils$activity$post_month_date_from_date(post_date){
var G__41704 = post_date;
G__41704.setDate((1));

G__41704.setHours((0));

G__41704.setMinutes((0));

G__41704.setSeconds((0));

G__41704.setMilliseconds((0));

return G__41704;
});
oc.web.utils.activity.separator_from_date = (function oc$web$utils$activity$separator_from_date(d,last_monday,two_weeks_ago,first_month){
var now = oc.web.lib.utils.js_date();
var month_string = oc.web.lib.utils.full_month_string((d.getMonth() + (1)));
if((d > last_monday)){
return new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"label","label",1718410804),"Recent",new cljs.core.Keyword(null,"resource-type","resource-type",1844262326),new cljs.core.Keyword(null,"separator","separator",-1628749125),new cljs.core.Keyword(null,"last-activity-at","last-activity-at",670511998),last_monday,new cljs.core.Keyword(null,"date","date",-1463434462),last_monday], null);
} else {
if((d > two_weeks_ago)){
return new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"label","label",1718410804),"Last week",new cljs.core.Keyword(null,"resource-type","resource-type",1844262326),new cljs.core.Keyword(null,"separator","separator",-1628749125),new cljs.core.Keyword(null,"date","date",-1463434462),two_weeks_ago,new cljs.core.Keyword(null,"last-activity-at","last-activity-at",670511998),two_weeks_ago], null);
} else {
if((d > first_month)){
return new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"label","label",1718410804),"2 weeks ago",new cljs.core.Keyword(null,"resource-type","resource-type",1844262326),new cljs.core.Keyword(null,"separator","separator",-1628749125),new cljs.core.Keyword(null,"date","date",-1463434462),first_month,new cljs.core.Keyword(null,"last-activity-at","last-activity-at",670511998),first_month], null);
} else {
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(now.getMonth(),d.getMonth())) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(now.getFullYear(),d.getFullYear())))){
return new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"label","label",1718410804),"This month",new cljs.core.Keyword(null,"resource-type","resource-type",1844262326),new cljs.core.Keyword(null,"separator","separator",-1628749125),new cljs.core.Keyword(null,"date","date",-1463434462),oc.web.utils.activity.post_month_date_from_date(d),new cljs.core.Keyword(null,"last-activity-at","last-activity-at",670511998),oc.web.utils.activity.post_month_date_from_date(d)], null);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(now.getFullYear(),d.getFullYear())){
return new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"label","label",1718410804),month_string,new cljs.core.Keyword(null,"resource-type","resource-type",1844262326),new cljs.core.Keyword(null,"separator","separator",-1628749125),new cljs.core.Keyword(null,"last-activity-at","last-activity-at",670511998),oc.web.utils.activity.post_month_date_from_date(d)], null);
} else {
return new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"label","label",1718410804),[cljs.core.str.cljs$core$IFn$_invoke$arity$1(month_string),", ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(d.getFullYear())].join(''),new cljs.core.Keyword(null,"resource-type","resource-type",1844262326),new cljs.core.Keyword(null,"separator","separator",-1628749125),new cljs.core.Keyword(null,"date","date",-1463434462),oc.web.utils.activity.post_month_date_from_date(d),new cljs.core.Keyword(null,"last-activity-at","last-activity-at",670511998),oc.web.utils.activity.post_month_date_from_date(d)], null);

}
}
}
}
}
});
oc.web.utils.activity.preserved_keys = new cljs.core.PersistentVector(null, 15, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"resource-type","resource-type",1844262326),new cljs.core.Keyword(null,"uuid","uuid",-2145095719),new cljs.core.Keyword(null,"sort-value","sort-value",-585546274),new cljs.core.Keyword(null,"unseen","unseen",1063275592),new cljs.core.Keyword(null,"unseen-comments","unseen-comments",-793262869),new cljs.core.Keyword(null,"replies-data","replies-data",1118937948),new cljs.core.Keyword(null,"board-slug","board-slug",99003663),new cljs.core.Keyword(null,"ignore-comments","ignore-comments",1137086177),new cljs.core.Keyword(null,"container-seen-at","container-seen-at",-1262643681),new cljs.core.Keyword(null,"publisher?","publisher?",30448149),new cljs.core.Keyword(null,"published-at","published-at",249684621),new cljs.core.Keyword(null,"expanded-replies","expanded-replies",-930394348),new cljs.core.Keyword(null,"comments-loaded?","comments-loaded?",-595034611),new cljs.core.Keyword(null,"comments-count","comments-count",1713184539),new cljs.core.Keyword(null,"for-you-context","for-you-context",1744709462)], null);
oc.web.utils.activity.add_posts_to_separator = (function oc$web$utils$activity$add_posts_to_separator(post_data,separators_map,last_monday,two_weeks_ago,first_month){
var post_date = oc.web.lib.utils.js_date.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"published-at","published-at",249684621).cljs$core$IFn$_invoke$arity$1(post_data)], 0));
var item_data = cljs.core.select_keys(post_data,oc.web.utils.activity.preserved_keys);
if(((cljs.core.seq(separators_map)) && ((post_date > new cljs.core.Keyword(null,"date","date",-1463434462).cljs$core$IFn$_invoke$arity$1(cljs.core.last(separators_map)))))){
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(separators_map,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(cljs.core.count(separators_map) - (1)),new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897)], null),(function (p1__41705_SHARP_){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(p1__41705_SHARP_,item_data));
}));
} else {
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(separators_map,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(oc.web.utils.activity.separator_from_date(post_date,last_monday,two_weeks_ago,first_month),new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [item_data], null))));
}
});
oc.web.utils.activity.grouped_posts = (function oc$web$utils$activity$grouped_posts(var_args){
var G__41709 = arguments.length;
switch (G__41709) {
case 1:
return oc.web.utils.activity.grouped_posts.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.utils.activity.grouped_posts.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.utils.activity.grouped_posts.cljs$core$IFn$_invoke$arity$1 = (function (full_items_list){
var items_list = cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__41706_SHARP_){
return cljs.core.select_keys(p1__41706_SHARP_,oc.web.utils.activity.preserved_keys);
}),full_items_list);
var items_map = cljs.core.zipmap(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),full_items_list),full_items_list);
return oc.web.utils.activity.grouped_posts.cljs$core$IFn$_invoke$arity$2(items_list,items_map);
}));

(oc.web.utils.activity.grouped_posts.cljs$core$IFn$_invoke$arity$2 = (function (items_list,fixed_items){
var last_monday = oc.web.lib.utils.js_date();
var _last_monday = (function (){var G__41710 = last_monday;
G__41710.setDate((last_monday.getDate() - cljs.core.mod((last_monday.getDay() + (8)),(7))));

G__41710.setHours((23));

G__41710.setMinutes((59));

G__41710.setSeconds((59));

G__41710.setMilliseconds((999));

return G__41710;
})();
var two_weeks_ago = oc.web.lib.utils.js_date();
var _two_weeks_ago = (function (){var G__41711 = two_weeks_ago;
G__41711.setDate((two_weeks_ago.getDate() - (cljs.core.mod((two_weeks_ago.getDay() + (8)),(7)) + (7))));

G__41711.setHours((23));

G__41711.setMinutes((59));

G__41711.setSeconds((59));

G__41711.setMilliseconds((999));

return G__41711;
})();
var first_month = oc.web.lib.utils.js_date();
var _first_month = (function (){var G__41712 = first_month;
G__41712.setDate((first_month.getDate() - (cljs.core.mod((first_month.getDay() + (8)),(7)) + (14))));

G__41712.setHours((23));

G__41712.setMinutes((59));

G__41712.setSeconds((59));

G__41712.setMilliseconds((999));

return G__41712;
})();
var last_date = new cljs.core.Keyword(null,"published-at","published-at",249684621).cljs$core$IFn$_invoke$arity$1(cljs.core.last(items_list));
var separators_data = (function (){var separators = cljs.core.PersistentVector.EMPTY;
var posts = items_list;
while(true){
if(cljs.core.empty_QMARK_(posts)){
return separators;
} else {
var G__42293 = oc.web.utils.activity.add_posts_to_separator(cljs.core.first(posts),separators,last_monday,two_weeks_ago,first_month);
var G__42294 = cljs.core.rest(posts);
separators = G__42293;
posts = G__42294;
continue;
}
break;
}
})();
return cljs.core.vec(cljs.core.rest(cljs.core.mapcat.cljs$core$IFn$_invoke$arity$variadic((function (p1__41707_SHARP_){
return cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(p1__41707_SHARP_,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897))], null),cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897).cljs$core$IFn$_invoke$arity$1(p1__41707_SHARP_)));
}),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([separators_data], 0))));
}));

(oc.web.utils.activity.grouped_posts.cljs$lang$maxFixedArity = 2);

/**
 * @param {...*} var_args
 */
oc.web.utils.activity.resource_type_QMARK_ = (function() { 
var oc$web$utils$activity$resource_type_QMARK___delegate = function (args__33705__auto__){
var ocr_41715 = cljs.core.vec(args__33705__auto__);
try{if(((cljs.core.vector_QMARK_(ocr_41715)) && ((cljs.core.count(ocr_41715) === 2)))){
try{var ocr_41715_1__41718 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41715,(1));
if(cljs.core.coll_QMARK_(ocr_41715_1__41718)){
var resource_types = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41715,(1));
var resource_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41715,(0));
return cljs.core.some(cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.utils.activity.resource_type_QMARK_,resource_data),resource_types);
} else {
throw cljs.core.match.backtrack;

}
}catch (e41722){if((e41722 instanceof Error)){
var e__32662__auto__ = e41722;
if((e__32662__auto__ === cljs.core.match.backtrack)){
try{var ocr_41715_1__41718 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41715,(1));
if(typeof ocr_41715_1__41718 === 'string'){
var resource_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41715,(1));
var resource_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41715,(0));
if(cuerdas.core.blank_QMARK_(resource_type)){
return null;
} else {
return (oc.web.utils.activity.resource_type_QMARK_.cljs$core$IFn$_invoke$arity$2 ? oc.web.utils.activity.resource_type_QMARK_.cljs$core$IFn$_invoke$arity$2(resource_data,resource_type) : oc.web.utils.activity.resource_type_QMARK_.call(null,resource_data,resource_type));
}
} else {
throw cljs.core.match.backtrack;

}
}catch (e41723){if((e41723 instanceof Error)){
var e__32662__auto____$1 = e41723;
if((e__32662__auto____$1 === cljs.core.match.backtrack)){
try{var ocr_41715_1__41718 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41715,(1));
if((ocr_41715_1__41718 instanceof cljs.core.Keyword)){
var resource_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41715,(1));
var resource_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41715,(0));
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"resource-type","resource-type",1844262326).cljs$core$IFn$_invoke$arity$1(resource_data)),cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(resource_type));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41724){if((e41724 instanceof Error)){
var e__32662__auto____$2 = e41724;
if((e__32662__auto____$2 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$2;
}
} else {
throw e41724;

}
}} else {
throw e__32662__auto____$1;
}
} else {
throw e41723;

}
}} else {
throw e__32662__auto__;
}
} else {
throw e41722;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41719){if((e41719 instanceof Error)){
var e__32662__auto__ = e41719;
if((e__32662__auto__ === cljs.core.match.backtrack)){
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ocr_41715)].join('')));
} else {
throw e__32662__auto__;
}
} else {
throw e41719;

}
}};
var oc$web$utils$activity$resource_type_QMARK_ = function (var_args){
var args__33705__auto__ = null;
if (arguments.length > 0) {
var G__42303__i = 0, G__42303__a = new Array(arguments.length -  0);
while (G__42303__i < G__42303__a.length) {G__42303__a[G__42303__i] = arguments[G__42303__i + 0]; ++G__42303__i;}
  args__33705__auto__ = new cljs.core.IndexedSeq(G__42303__a,0,null);
} 
return oc$web$utils$activity$resource_type_QMARK___delegate.call(this,args__33705__auto__);};
oc$web$utils$activity$resource_type_QMARK_.cljs$lang$maxFixedArity = 0;
oc$web$utils$activity$resource_type_QMARK_.cljs$lang$applyTo = (function (arglist__42305){
var args__33705__auto__ = cljs.core.seq(arglist__42305);
return oc$web$utils$activity$resource_type_QMARK___delegate(args__33705__auto__);
});
oc$web$utils$activity$resource_type_QMARK_.cljs$core$IFn$_invoke$arity$variadic = oc$web$utils$activity$resource_type_QMARK___delegate;
return oc$web$utils$activity$resource_type_QMARK_;
})()
;
oc.web.utils.activity.user_QMARK_ = (function oc$web$utils$activity$user_QMARK_(user_data){
return oc.web.utils.activity.resource_type_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user_data,new cljs.core.Keyword(null,"user","user",1532431356)], 0));
});
oc.web.utils.activity.board_QMARK_ = (function oc$web$utils$activity$board_QMARK_(board_data){
return oc.web.utils.activity.resource_type_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([board_data,new cljs.core.Keyword(null,"board","board",-1907017633)], 0));
});
oc.web.utils.activity.container_QMARK_ = (function oc$web$utils$activity$container_QMARK_(container_data){
return oc.web.utils.activity.resource_type_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([container_data,new cljs.core.Keyword(null,"container","container",-1736937707)], 0));
});
oc.web.utils.activity.contributions_QMARK_ = (function oc$web$utils$activity$contributions_QMARK_(contrib_data){
return oc.web.utils.activity.resource_type_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([contrib_data,new cljs.core.Keyword(null,"contributions","contributions",-1280485964)], 0));
});
oc.web.utils.activity.entry_QMARK_ = (function oc$web$utils$activity$entry_QMARK_(entry_data){
return oc.web.utils.activity.resource_type_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([entry_data,new cljs.core.Keyword(null,"entry","entry",505168823)], 0));
});
oc.web.utils.activity.comment_QMARK_ = (function oc$web$utils$activity$comment_QMARK_(comment_data){
return oc.web.utils.activity.resource_type_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([comment_data,new cljs.core.Keyword(null,"comment","comment",532206069)], 0));
});
/**
 * @param {...*} var_args
 */
oc.web.utils.activity.is_published_QMARK_ = (function() { 
var oc$web$utils$activity$is_published_QMARK___delegate = function (args__33705__auto__){
var ocr_41737 = cljs.core.vec(args__33705__auto__);
try{if(((cljs.core.vector_QMARK_(ocr_41737)) && ((cljs.core.count(ocr_41737) === 1)))){
try{var ocr_41737_0__41739 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41737,(0));
if(cljs.core.map_QMARK_(ocr_41737_0__41739)){
var entry_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41737,(0));
var G__41743 = new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(entry_data);
return (oc.web.utils.activity.is_published_QMARK_.cljs$core$IFn$_invoke$arity$1 ? oc.web.utils.activity.is_published_QMARK_.cljs$core$IFn$_invoke$arity$1(G__41743) : oc.web.utils.activity.is_published_QMARK_.call(null,G__41743));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41741){if((e41741 instanceof Error)){
var e__32662__auto__ = e41741;
if((e__32662__auto__ === cljs.core.match.backtrack)){
try{var ocr_41737_0__41739 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41737,(0));
if((function (p1__41736_SHARP_){
return ((typeof p1__41736_SHARP_ === 'string') || ((p1__41736_SHARP_ == null)) || ((p1__41736_SHARP_ instanceof cljs.core.Keyword)));
})(ocr_41737_0__41739)){
var entry_status = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41737,(0));
var and__4115__auto__ = entry_status;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(entry_status),new cljs.core.Keyword(null,"published","published",-514587618));
} else {
return and__4115__auto__;
}
} else {
throw cljs.core.match.backtrack;

}
}catch (e41742){if((e41742 instanceof Error)){
var e__32662__auto____$1 = e41742;
if((e__32662__auto____$1 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$1;
}
} else {
throw e41742;

}
}} else {
throw e__32662__auto__;
}
} else {
throw e41741;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41740){if((e41740 instanceof Error)){
var e__32662__auto__ = e41740;
if((e__32662__auto__ === cljs.core.match.backtrack)){
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ocr_41737)].join('')));
} else {
throw e__32662__auto__;
}
} else {
throw e41740;

}
}};
var oc$web$utils$activity$is_published_QMARK_ = function (var_args){
var args__33705__auto__ = null;
if (arguments.length > 0) {
var G__42340__i = 0, G__42340__a = new Array(arguments.length -  0);
while (G__42340__i < G__42340__a.length) {G__42340__a[G__42340__i] = arguments[G__42340__i + 0]; ++G__42340__i;}
  args__33705__auto__ = new cljs.core.IndexedSeq(G__42340__a,0,null);
} 
return oc$web$utils$activity$is_published_QMARK___delegate.call(this,args__33705__auto__);};
oc$web$utils$activity$is_published_QMARK_.cljs$lang$maxFixedArity = 0;
oc$web$utils$activity$is_published_QMARK_.cljs$lang$applyTo = (function (arglist__42341){
var args__33705__auto__ = cljs.core.seq(arglist__42341);
return oc$web$utils$activity$is_published_QMARK___delegate(args__33705__auto__);
});
oc$web$utils$activity$is_published_QMARK_.cljs$core$IFn$_invoke$arity$variadic = oc$web$utils$activity$is_published_QMARK___delegate;
return oc$web$utils$activity$is_published_QMARK_;
})()
;
/**
 * @param {...*} var_args
 */
oc.web.utils.activity.is_publisher_QMARK_ = (function() { 
var oc$web$utils$activity$is_publisher_QMARK___delegate = function (args__33705__auto__){
var ocr_41744 = cljs.core.vec(args__33705__auto__);
try{if(((cljs.core.vector_QMARK_(ocr_41744)) && ((cljs.core.count(ocr_41744) === 1)))){
try{var ocr_41744_0__41748 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41744,(0));
if((ocr_41744_0__41748 === null)){
return false;
} else {
throw cljs.core.match.backtrack;

}
}catch (e41823){if((e41823 instanceof Error)){
var e__32662__auto__ = e41823;
if((e__32662__auto__ === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto__;
}
} else {
throw e41823;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41761){if((e41761 instanceof Error)){
var e__32662__auto__ = e41761;
if((e__32662__auto__ === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41744)) && ((cljs.core.count(ocr_41744) === 2)))){
try{var ocr_41744_1__41753 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41744,(1));
if((ocr_41744_1__41753 === null)){
return false;
} else {
throw cljs.core.match.backtrack;

}
}catch (e41821){if((e41821 instanceof Error)){
var e__32662__auto____$1 = e41821;
if((e__32662__auto____$1 === cljs.core.match.backtrack)){
try{var ocr_41744_0__41752 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41744,(0));
if((ocr_41744_0__41752 === null)){
return false;
} else {
throw cljs.core.match.backtrack;

}
}catch (e41822){if((e41822 instanceof Error)){
var e__32662__auto____$2 = e41822;
if((e__32662__auto____$2 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$2;
}
} else {
throw e41822;

}
}} else {
throw e__32662__auto____$1;
}
} else {
throw e41821;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41762){if((e41762 instanceof Error)){
var e__32662__auto____$1 = e41762;
if((e__32662__auto____$1 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41744)) && ((cljs.core.count(ocr_41744) === 1)))){
try{var ocr_41744_0__41756 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41744,(0));
if(cljs.core.map_QMARK_(ocr_41744_0__41756)){
var entry_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41744,(0));
var G__41797 = entry_data;
var G__41798 = oc.web.lib.jwt.user_id();
return (oc.web.utils.activity.is_publisher_QMARK_.cljs$core$IFn$_invoke$arity$2 ? oc.web.utils.activity.is_publisher_QMARK_.cljs$core$IFn$_invoke$arity$2(G__41797,G__41798) : oc.web.utils.activity.is_publisher_QMARK_.call(null,G__41797,G__41798));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41792){if((e41792 instanceof Error)){
var e__32662__auto____$2 = e41792;
if((e__32662__auto____$2 === cljs.core.match.backtrack)){
try{var ocr_41744_0__41756 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41744,(0));
if(typeof ocr_41744_0__41756 === 'string'){
var entry_author_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41744,(0));
var G__41795 = entry_author_id;
var G__41796 = oc.web.lib.jwt.user_id();
return (oc.web.utils.activity.is_publisher_QMARK_.cljs$core$IFn$_invoke$arity$2 ? oc.web.utils.activity.is_publisher_QMARK_.cljs$core$IFn$_invoke$arity$2(G__41795,G__41796) : oc.web.utils.activity.is_publisher_QMARK_.call(null,G__41795,G__41796));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41794){if((e41794 instanceof Error)){
var e__32662__auto____$3 = e41794;
if((e__32662__auto____$3 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$3;
}
} else {
throw e41794;

}
}} else {
throw e__32662__auto____$2;
}
} else {
throw e41792;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41763){if((e41763 instanceof Error)){
var e__32662__auto____$2 = e41763;
if((e__32662__auto____$2 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41744)) && ((cljs.core.count(ocr_41744) === 2)))){
try{var ocr_41744_0__41757 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41744,(0));
if(cljs.core.map_QMARK_(ocr_41744_0__41757)){
try{var ocr_41744_1__41758 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41744,(1));
if(cljs.core.truth_(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(ocr_41744_1__41758))){
var user_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41744,(1));
var entry_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41744,(0));
var G__41788 = entry_data;
var G__41789 = new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user_data);
return (oc.web.utils.activity.is_publisher_QMARK_.cljs$core$IFn$_invoke$arity$2 ? oc.web.utils.activity.is_publisher_QMARK_.cljs$core$IFn$_invoke$arity$2(G__41788,G__41789) : oc.web.utils.activity.is_publisher_QMARK_.call(null,G__41788,G__41789));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41779){if((e41779 instanceof Error)){
var e__32662__auto____$3 = e41779;
if((e__32662__auto____$3 === cljs.core.match.backtrack)){
try{var ocr_41744_1__41758 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41744,(1));
if(typeof ocr_41744_1__41758 === 'string'){
var user_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41744,(1));
var entry_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41744,(0));
if(cljs.core.truth_((function (){var or__4126__auto__ = new cljs.core.Keyword(null,"published?","published?",-1603043839).cljs$core$IFn$_invoke$arity$1(entry_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.utils.activity.is_published_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([entry_data], 0));
}
})())){
var G__41785 = new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"publisher","publisher",-153364540).cljs$core$IFn$_invoke$arity$1(entry_data));
var G__41786 = user_id;
return (oc.web.utils.activity.is_publisher_QMARK_.cljs$core$IFn$_invoke$arity$2 ? oc.web.utils.activity.is_publisher_QMARK_.cljs$core$IFn$_invoke$arity$2(G__41785,G__41786) : oc.web.utils.activity.is_publisher_QMARK_.call(null,G__41785,G__41786));
} else {
return null;
}
} else {
throw cljs.core.match.backtrack;

}
}catch (e41782){if((e41782 instanceof Error)){
var e__32662__auto____$4 = e41782;
if((e__32662__auto____$4 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$4;
}
} else {
throw e41782;

}
}} else {
throw e__32662__auto____$3;
}
} else {
throw e41779;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41766){if((e41766 instanceof Error)){
var e__32662__auto____$3 = e41766;
if((e__32662__auto____$3 === cljs.core.match.backtrack)){
try{var ocr_41744_0__41757 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41744,(0));
if(typeof ocr_41744_0__41757 === 'string'){
try{var ocr_41744_1__41758 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41744,(1));
if(typeof ocr_41744_1__41758 === 'string'){
var user_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41744,(1));
var entry_author_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41744,(0));
return ((cljs.core.seq(entry_author_id)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(user_id,entry_author_id)));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41771){if((e41771 instanceof Error)){
var e__32662__auto____$4 = e41771;
if((e__32662__auto____$4 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$4;
}
} else {
throw e41771;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41768){if((e41768 instanceof Error)){
var e__32662__auto____$4 = e41768;
if((e__32662__auto____$4 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$4;
}
} else {
throw e41768;

}
}} else {
throw e__32662__auto____$3;
}
} else {
throw e41766;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41764){if((e41764 instanceof Error)){
var e__32662__auto____$3 = e41764;
if((e__32662__auto____$3 === cljs.core.match.backtrack)){
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ocr_41744)].join('')));
} else {
throw e__32662__auto____$3;
}
} else {
throw e41764;

}
}} else {
throw e__32662__auto____$2;
}
} else {
throw e41763;

}
}} else {
throw e__32662__auto____$1;
}
} else {
throw e41762;

}
}} else {
throw e__32662__auto__;
}
} else {
throw e41761;

}
}};
var oc$web$utils$activity$is_publisher_QMARK_ = function (var_args){
var args__33705__auto__ = null;
if (arguments.length > 0) {
var G__42365__i = 0, G__42365__a = new Array(arguments.length -  0);
while (G__42365__i < G__42365__a.length) {G__42365__a[G__42365__i] = arguments[G__42365__i + 0]; ++G__42365__i;}
  args__33705__auto__ = new cljs.core.IndexedSeq(G__42365__a,0,null);
} 
return oc$web$utils$activity$is_publisher_QMARK___delegate.call(this,args__33705__auto__);};
oc$web$utils$activity$is_publisher_QMARK_.cljs$lang$maxFixedArity = 0;
oc$web$utils$activity$is_publisher_QMARK_.cljs$lang$applyTo = (function (arglist__42366){
var args__33705__auto__ = cljs.core.seq(arglist__42366);
return oc$web$utils$activity$is_publisher_QMARK___delegate(args__33705__auto__);
});
oc$web$utils$activity$is_publisher_QMARK_.cljs$core$IFn$_invoke$arity$variadic = oc$web$utils$activity$is_publisher_QMARK___delegate;
return oc$web$utils$activity$is_publisher_QMARK_;
})()
;
/**
 * Check if current user is the author of the entry/comment.
 * @param {...*} var_args
 */
oc.web.utils.activity.is_author_QMARK_ = (function() { 
var oc$web$utils$activity$is_author_QMARK___delegate = function (args__33705__auto__){
var ocr_41824 = cljs.core.vec(args__33705__auto__);
try{if(((cljs.core.vector_QMARK_(ocr_41824)) && ((cljs.core.count(ocr_41824) === 1)))){
try{var ocr_41824_0__41828 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41824,(0));
if((ocr_41824_0__41828 === null)){
return false;
} else {
throw cljs.core.match.backtrack;

}
}catch (e41888){if((e41888 instanceof Error)){
var e__32662__auto__ = e41888;
if((e__32662__auto__ === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto__;
}
} else {
throw e41888;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41850){if((e41850 instanceof Error)){
var e__32662__auto__ = e41850;
if((e__32662__auto__ === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41824)) && ((cljs.core.count(ocr_41824) === 2)))){
try{var ocr_41824_1__41830 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41824,(1));
if((ocr_41824_1__41830 === null)){
return false;
} else {
throw cljs.core.match.backtrack;

}
}catch (e41886){if((e41886 instanceof Error)){
var e__32662__auto____$1 = e41886;
if((e__32662__auto____$1 === cljs.core.match.backtrack)){
try{var ocr_41824_0__41829 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41824,(0));
if((ocr_41824_0__41829 === null)){
return false;
} else {
throw cljs.core.match.backtrack;

}
}catch (e41887){if((e41887 instanceof Error)){
var e__32662__auto____$2 = e41887;
if((e__32662__auto____$2 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$2;
}
} else {
throw e41887;

}
}} else {
throw e__32662__auto____$1;
}
} else {
throw e41886;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41851){if((e41851 instanceof Error)){
var e__32662__auto____$1 = e41851;
if((e__32662__auto____$1 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41824)) && ((cljs.core.count(ocr_41824) === 1)))){
try{var ocr_41824_0__41831 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41824,(0));
if(cljs.core.map_QMARK_(ocr_41824_0__41831)){
var entity_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41824,(0));
var G__41884 = entity_data;
var G__41885 = oc.web.lib.jwt.user_id();
return (oc.web.utils.activity.is_author_QMARK_.cljs$core$IFn$_invoke$arity$2 ? oc.web.utils.activity.is_author_QMARK_.cljs$core$IFn$_invoke$arity$2(G__41884,G__41885) : oc.web.utils.activity.is_author_QMARK_.call(null,G__41884,G__41885));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41883){if((e41883 instanceof Error)){
var e__32662__auto____$2 = e41883;
if((e__32662__auto____$2 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$2;
}
} else {
throw e41883;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41852){if((e41852 instanceof Error)){
var e__32662__auto____$2 = e41852;
if((e__32662__auto____$2 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41824)) && ((cljs.core.count(ocr_41824) === 2)))){
try{var ocr_41824_0__41832 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41824,(0));
if(cljs.core.map_QMARK_(ocr_41824_0__41832)){
try{var ocr_41824_1__41833 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41824,(1));
if(cljs.core.truth_(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(ocr_41824_1__41833))){
var user_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41824,(1));
var entity_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41824,(0));
var G__41881 = entity_data;
var G__41882 = new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user_data);
return (oc.web.utils.activity.is_author_QMARK_.cljs$core$IFn$_invoke$arity$2 ? oc.web.utils.activity.is_author_QMARK_.cljs$core$IFn$_invoke$arity$2(G__41881,G__41882) : oc.web.utils.activity.is_author_QMARK_.call(null,G__41881,G__41882));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41880){if((e41880 instanceof Error)){
var e__32662__auto____$3 = e41880;
if((e__32662__auto____$3 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$3;
}
} else {
throw e41880;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41864){if((e41864 instanceof Error)){
var e__32662__auto____$3 = e41864;
if((e__32662__auto____$3 === cljs.core.match.backtrack)){
try{var ocr_41824_0__41832 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41824,(0));
if(cljs.core.truth_(oc.web.utils.activity.contributions_QMARK_(ocr_41824_0__41832))){
try{var ocr_41824_1__41833 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41824,(1));
if(typeof ocr_41824_1__41833 === 'string'){
var user_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41824,(1));
var contrib_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41824,(0));
var G__41878 = new cljs.core.Keyword(null,"author-uuid","author-uuid",371566491).cljs$core$IFn$_invoke$arity$1(contrib_data);
var G__41879 = user_id;
return (oc.web.utils.activity.is_author_QMARK_.cljs$core$IFn$_invoke$arity$2 ? oc.web.utils.activity.is_author_QMARK_.cljs$core$IFn$_invoke$arity$2(G__41878,G__41879) : oc.web.utils.activity.is_author_QMARK_.call(null,G__41878,G__41879));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41874){if((e41874 instanceof Error)){
var e__32662__auto____$4 = e41874;
if((e__32662__auto____$4 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$4;
}
} else {
throw e41874;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41865){if((e41865 instanceof Error)){
var e__32662__auto____$4 = e41865;
if((e__32662__auto____$4 === cljs.core.match.backtrack)){
try{var ocr_41824_0__41832 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41824,(0));
if(cljs.core.truth_(oc.web.utils.activity.entry_QMARK_(ocr_41824_0__41832))){
try{var ocr_41824_1__41833 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41824,(1));
if(typeof ocr_41824_1__41833 === 'string'){
var user_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41824,(1));
var entity_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41824,(0));
var G__41872 = new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword(null,"author","author",2111686192).cljs$core$IFn$_invoke$arity$1(entity_data)));
var G__41873 = user_id;
return (oc.web.utils.activity.is_author_QMARK_.cljs$core$IFn$_invoke$arity$2 ? oc.web.utils.activity.is_author_QMARK_.cljs$core$IFn$_invoke$arity$2(G__41872,G__41873) : oc.web.utils.activity.is_author_QMARK_.call(null,G__41872,G__41873));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41871){if((e41871 instanceof Error)){
var e__32662__auto____$5 = e41871;
if((e__32662__auto____$5 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$5;
}
} else {
throw e41871;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41866){if((e41866 instanceof Error)){
var e__32662__auto____$5 = e41866;
if((e__32662__auto____$5 === cljs.core.match.backtrack)){
try{var ocr_41824_0__41832 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41824,(0));
if(cljs.core.map_QMARK_(ocr_41824_0__41832)){
try{var ocr_41824_1__41833 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41824,(1));
if(typeof ocr_41824_1__41833 === 'string'){
var user_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41824,(1));
var entity_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41824,(0));
var G__41869 = new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"author","author",2111686192).cljs$core$IFn$_invoke$arity$1(entity_data));
var G__41870 = user_id;
return (oc.web.utils.activity.is_author_QMARK_.cljs$core$IFn$_invoke$arity$2 ? oc.web.utils.activity.is_author_QMARK_.cljs$core$IFn$_invoke$arity$2(G__41869,G__41870) : oc.web.utils.activity.is_author_QMARK_.call(null,G__41869,G__41870));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41868){if((e41868 instanceof Error)){
var e__32662__auto____$6 = e41868;
if((e__32662__auto____$6 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$6;
}
} else {
throw e41868;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41867){if((e41867 instanceof Error)){
var e__32662__auto____$6 = e41867;
if((e__32662__auto____$6 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$6;
}
} else {
throw e41867;

}
}} else {
throw e__32662__auto____$5;
}
} else {
throw e41866;

}
}} else {
throw e__32662__auto____$4;
}
} else {
throw e41865;

}
}} else {
throw e__32662__auto____$3;
}
} else {
throw e41864;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41853){if((e41853 instanceof Error)){
var e__32662__auto____$3 = e41853;
if((e__32662__auto____$3 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41824)) && ((cljs.core.count(ocr_41824) === 1)))){
try{var ocr_41824_0__41847 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41824,(0));
if(typeof ocr_41824_0__41847 === 'string'){
var author_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41824,(0));
var G__41862 = author_id;
var G__41863 = oc.web.lib.jwt.user_id();
return (oc.web.utils.activity.is_author_QMARK_.cljs$core$IFn$_invoke$arity$2 ? oc.web.utils.activity.is_author_QMARK_.cljs$core$IFn$_invoke$arity$2(G__41862,G__41863) : oc.web.utils.activity.is_author_QMARK_.call(null,G__41862,G__41863));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41858){if((e41858 instanceof Error)){
var e__32662__auto____$4 = e41858;
if((e__32662__auto____$4 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$4;
}
} else {
throw e41858;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41854){if((e41854 instanceof Error)){
var e__32662__auto____$4 = e41854;
if((e__32662__auto____$4 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41824)) && ((cljs.core.count(ocr_41824) === 2)))){
try{var ocr_41824_0__41848 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41824,(0));
if(typeof ocr_41824_0__41848 === 'string'){
try{var ocr_41824_1__41849 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41824,(1));
if(typeof ocr_41824_1__41849 === 'string'){
var user_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41824,(1));
var author_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41824,(0));
return ((cljs.core.seq(user_id)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(user_id,author_id)));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41857){if((e41857 instanceof Error)){
var e__32662__auto____$5 = e41857;
if((e__32662__auto____$5 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$5;
}
} else {
throw e41857;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41856){if((e41856 instanceof Error)){
var e__32662__auto____$5 = e41856;
if((e__32662__auto____$5 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$5;
}
} else {
throw e41856;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41855){if((e41855 instanceof Error)){
var e__32662__auto____$5 = e41855;
if((e__32662__auto____$5 === cljs.core.match.backtrack)){
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ocr_41824)].join('')));
} else {
throw e__32662__auto____$5;
}
} else {
throw e41855;

}
}} else {
throw e__32662__auto____$4;
}
} else {
throw e41854;

}
}} else {
throw e__32662__auto____$3;
}
} else {
throw e41853;

}
}} else {
throw e__32662__auto____$2;
}
} else {
throw e41852;

}
}} else {
throw e__32662__auto____$1;
}
} else {
throw e41851;

}
}} else {
throw e__32662__auto__;
}
} else {
throw e41850;

}
}};
var oc$web$utils$activity$is_author_QMARK_ = function (var_args){
var args__33705__auto__ = null;
if (arguments.length > 0) {
var G__42402__i = 0, G__42402__a = new Array(arguments.length -  0);
while (G__42402__i < G__42402__a.length) {G__42402__a[G__42402__i] = arguments[G__42402__i + 0]; ++G__42402__i;}
  args__33705__auto__ = new cljs.core.IndexedSeq(G__42402__a,0,null);
} 
return oc$web$utils$activity$is_author_QMARK___delegate.call(this,args__33705__auto__);};
oc$web$utils$activity$is_author_QMARK_.cljs$lang$maxFixedArity = 0;
oc$web$utils$activity$is_author_QMARK_.cljs$lang$applyTo = (function (arglist__42403){
var args__33705__auto__ = cljs.core.seq(arglist__42403);
return oc$web$utils$activity$is_author_QMARK___delegate(args__33705__auto__);
});
oc$web$utils$activity$is_author_QMARK_.cljs$core$IFn$_invoke$arity$variadic = oc$web$utils$activity$is_author_QMARK___delegate;
return oc$web$utils$activity$is_author_QMARK_;
})()
;
oc.web.utils.activity.board_by_uuid = (function oc$web$utils$activity$board_by_uuid(board_uuid){
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
var boards = new cljs.core.Keyword(null,"boards","boards",1912049694).cljs$core$IFn$_invoke$arity$1(org_data);
return cljs.core.some((function (p1__41889_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__41889_SHARP_),board_uuid)){
return p1__41889_SHARP_;
} else {
return null;
}
}),boards);
});
/**
 * Reset dotdotdot for the give body element.
 */
oc.web.utils.activity.reset_truncate_body = (function oc$web$utils$activity$reset_truncate_body(body_el){
var $body_els = $(">*",body_el);
return $body_els.each((function (idx,el){
var this$ = this;
return $(this$).trigger("destroy");
}));
});
oc.web.utils.activity.default_body_height = (72);
oc.web.utils.activity.default_all_posts_body_height = (144);
oc.web.utils.activity.default_draft_body_height = (48);
/**
 * Given a body element truncate the body. It iterate on the elements
 *   of the body and truncate the first exceeded element found.
 *   This is to avoid truncating a DIV with multiple spaced P inside,
 *   since this is a problem for the dotdotdot library that we are using.
 */
oc.web.utils.activity.truncate_body = (function oc$web$utils$activity$truncate_body(body_el,height){
oc.web.utils.activity.reset_truncate_body(body_el);

return $(body_el).dotdotdot(({"height": height, "wrap": "word", "watch": true, "ellipsis": "..."}));
});
/**
 * Thanks to https://gist.github.com/colemanw/9c9a12aae16a4bfe2678de86b661d922
 */
oc.web.utils.activity.icon_for_mimetype = (function oc$web$utils$activity$icon_for_mimetype(mimetype){
var G__41890 = cuerdas.core.lower(mimetype);
switch (G__41890) {
case "image":
return "fa-file-image-o";

break;
case "image/png":
return "fa-file-image-o";

break;
case "image/bmp":
return "fa-file-image-o";

break;
case "image/jpg":
return "fa-file-image-o";

break;
case "image/jpeg":
return "fa-file-image-o";

break;
case "image/gif":
return "fa-file-image-o";

break;
case ".jpg":
return "fa-file-image-o";

break;
case "audio":
return "fa-file-audio-o";

break;
case "video":
return "fa-file-video-o";

break;
case "application/pdf":
return "fa-file-pdf-o";

break;
case "application/msword":
return "fa-file-word-o";

break;
case "application/vnd.ms-word":
return "fa-file-word-o";

break;
case "application/vnd.oasis.opendocument.text":
return "fa-file-word-o";

break;
case "application/vnd.openxmlformats-officedocument.wordprocessingml":
return "fa-file-word-o";

break;
case "application/vnd.ms-excel":
return "fa-file-excel-o";

break;
case "application/vnd.openxmlformats-officedocument.spreadsheetml":
return "fa-file-excel-o";

break;
case "application/vnd.oasis.opendocument.spreadsheet":
return "fa-file-excel-o";

break;
case "application/vnd.ms-powerpoint":
return "fa-file-powerpoint-o";

break;
case "application/vnd.openxmlformats-officedocument.presentationml":
return "fa-file-powerpoint-o";

break;
case "application/vnd.oasis.opendocument.presentation":
return "fa-file-powerpoint-o";

break;
case "text/plain":
return "fa-file-text-o";

break;
case "text/html":
return "fa-file-code-o";

break;
case "application/json":
return "fa-file-code-o";

break;
case "application/gzip":
return "fa-file-archive-o";

break;
case "application/zip":
return "fa-file-archive-o";

break;
case "text/css":
return "fa-file-code-o";

break;
case "text/php":
return "fa-file-code-o";

break;
default:
return "fa-file";

}
});
oc.web.utils.activity.get_activity_date = (function oc$web$utils$activity$get_activity_date(activity){
var or__4126__auto__ = new cljs.core.Keyword(null,"published-at","published-at",249684621).cljs$core$IFn$_invoke$arity$1(activity);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"created-at","created-at",-89248644).cljs$core$IFn$_invoke$arity$1(activity);
}
});
oc.web.utils.activity.compare_activities = (function oc$web$utils$activity$compare_activities(act_1,act_2){
var time_1 = oc.web.utils.activity.get_activity_date(act_1);
var time_2 = oc.web.utils.activity.get_activity_date(act_2);
return cljs.core.compare(time_2,time_1);
});
oc.web.utils.activity.get_sorted_activities = (function oc$web$utils$activity$get_sorted_activities(posts_data){
return cljs.core.vec(cljs.core.sort.cljs$core$IFn$_invoke$arity$2(oc.web.utils.activity.compare_activities,cljs.core.vals(posts_data)));
});
oc.web.utils.activity.readonly_org_QMARK_ = (function oc$web$utils$activity$readonly_org_QMARK_(links){
var update_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([links,"partial-update"], 0));
return (update_link == null);
});
oc.web.utils.activity.readonly_board_QMARK_ = (function oc$web$utils$activity$readonly_board_QMARK_(links){
var new_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([links,"create"], 0));
var update_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([links,"partial-update"], 0));
var delete_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([links,"delete"], 0));
return (((new_link == null)) && ((update_link == null)) && ((delete_link == null)));
});
oc.web.utils.activity.readonly_entry_QMARK_ = (function oc$web$utils$activity$readonly_entry_QMARK_(links){
var partial_update = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([links,"partial-update"], 0));
var delete$ = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([links,"delete"], 0));
return (((partial_update == null)) && ((delete$ == null)));
});
/**
 * An entry is new if its uuid is contained in container's unseen.
 * @param {...*} var_args
 */
oc.web.utils.activity.entry_unseen_QMARK_ = (function() { 
var oc$web$utils$activity$entry_unseen_QMARK___delegate = function (args__33705__auto__){
var ocr_41892 = cljs.core.vec(args__33705__auto__);
try{if(((cljs.core.vector_QMARK_(ocr_41892)) && ((cljs.core.count(ocr_41892) === 2)))){
try{var ocr_41892_0__41894 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41892,(0));
if(cljs.core.map_QMARK_(ocr_41892_0__41894)){
var entry = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41892,(0));
var last_seen_at = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41892,(1));
if(cljs.core.not(new cljs.core.Keyword(null,"publisher?","publisher?",30448149).cljs$core$IFn$_invoke$arity$1(entry))){
var G__41901 = new cljs.core.Keyword(null,"published-at","published-at",249684621).cljs$core$IFn$_invoke$arity$1(entry);
var G__41902 = last_seen_at;
return (oc.web.utils.activity.entry_unseen_QMARK_.cljs$core$IFn$_invoke$arity$2 ? oc.web.utils.activity.entry_unseen_QMARK_.cljs$core$IFn$_invoke$arity$2(G__41901,G__41902) : oc.web.utils.activity.entry_unseen_QMARK_.call(null,G__41901,G__41902));
} else {
return false;
}
} else {
throw cljs.core.match.backtrack;

}
}catch (e41897){if((e41897 instanceof Error)){
var e__32662__auto__ = e41897;
if((e__32662__auto__ === cljs.core.match.backtrack)){
try{var ocr_41892_1__41895 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41892,(1));
if((function (p1__41891_SHARP_){
return (((p1__41891_SHARP_ == null)) || (typeof p1__41891_SHARP_ === 'string'));
})(ocr_41892_1__41895)){
var last_seen_at = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41892,(1));
var published_at = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41892,(0));
return (cljs.core.compare(published_at,last_seen_at) > (0));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41898){if((e41898 instanceof Error)){
var e__32662__auto____$1 = e41898;
if((e__32662__auto____$1 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$1;
}
} else {
throw e41898;

}
}} else {
throw e__32662__auto__;
}
} else {
throw e41897;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41896){if((e41896 instanceof Error)){
var e__32662__auto__ = e41896;
if((e__32662__auto__ === cljs.core.match.backtrack)){
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ocr_41892)].join('')));
} else {
throw e__32662__auto__;
}
} else {
throw e41896;

}
}};
var oc$web$utils$activity$entry_unseen_QMARK_ = function (var_args){
var args__33705__auto__ = null;
if (arguments.length > 0) {
var G__42441__i = 0, G__42441__a = new Array(arguments.length -  0);
while (G__42441__i < G__42441__a.length) {G__42441__a[G__42441__i] = arguments[G__42441__i + 0]; ++G__42441__i;}
  args__33705__auto__ = new cljs.core.IndexedSeq(G__42441__a,0,null);
} 
return oc$web$utils$activity$entry_unseen_QMARK___delegate.call(this,args__33705__auto__);};
oc$web$utils$activity$entry_unseen_QMARK_.cljs$lang$maxFixedArity = 0;
oc$web$utils$activity$entry_unseen_QMARK_.cljs$lang$applyTo = (function (arglist__42442){
var args__33705__auto__ = cljs.core.seq(arglist__42442);
return oc$web$utils$activity$entry_unseen_QMARK___delegate(args__33705__auto__);
});
oc$web$utils$activity$entry_unseen_QMARK_.cljs$core$IFn$_invoke$arity$variadic = oc$web$utils$activity$entry_unseen_QMARK___delegate;
return oc$web$utils$activity$entry_unseen_QMARK_;
})()
;
/**
 * An entry is new if its uuid is contained in container's unread.
 */
oc.web.utils.activity.entry_unread_QMARK_ = (function oc$web$utils$activity$entry_unread_QMARK_(entry,changes){
var board_uuid = new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127).cljs$core$IFn$_invoke$arity$1(entry);
var board_change_data = cljs.core.get.cljs$core$IFn$_invoke$arity$3(changes,board_uuid,cljs.core.PersistentArrayMap.EMPTY);
var board_unread = new cljs.core.Keyword(null,"unread","unread",-1950424572).cljs$core$IFn$_invoke$arity$1(board_change_data);
if(cljs.core.truth_(board_unread)){
return oc.web.lib.utils.in_QMARK_(board_unread,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry));
} else {
return (new cljs.core.Keyword(null,"last-read-at","last-read-at",-216601930).cljs$core$IFn$_invoke$arity$1(entry) == null);
}
});
oc.web.utils.activity.comments_unseen_QMARK_ = (function oc$web$utils$activity$comments_unseen_QMARK_(entry_data,last_seen_at){
var not_self_comments = cljs.core.filter.cljs$core$IFn$_invoke$arity$2(cljs.core.comp.cljs$core$IFn$_invoke$arity$2(cljs.core.not,new cljs.core.Keyword(null,"self?","self?",-701815921)),new cljs.core.Keyword(null,"replies-data","replies-data",1118937948).cljs$core$IFn$_invoke$arity$1(entry_data));
return cljs.core.some((function (p1__41903_SHARP_){
return oc.web.utils.comment.comment_unseen_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__41903_SHARP_,last_seen_at], 0));
}),not_self_comments);
});
oc.web.utils.activity.comments_ignore_QMARK_ = (function oc$web$utils$activity$comments_ignore_QMARK_(entry_data,last_seen_at){
var all_unseen = cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__41904_SHARP_){
return oc.web.utils.comment.comment_unseen_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__41904_SHARP_,last_seen_at], 0));
}),new cljs.core.Keyword(null,"replies-data","replies-data",1118937948).cljs$core$IFn$_invoke$arity$1(entry_data));
return cljs.core.boolean$(((cljs.core.seq(all_unseen)) && (cljs.core.every_QMARK_(new cljs.core.Keyword(null,"author?","author?",-1083349935),all_unseen))));
});
oc.web.utils.activity.has_attachments_QMARK_ = (function oc$web$utils$activity$has_attachments_QMARK_(data){
return cljs.core.seq(new cljs.core.Keyword(null,"attachments","attachments",-1535547830).cljs$core$IFn$_invoke$arity$1(data));
});
oc.web.utils.activity.has_headline_QMARK_ = (function oc$web$utils$activity$has_headline_QMARK_(data){
return (!(cuerdas.core.blank_QMARK_(cuerdas.core.trim.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"headline","headline",-157157727).cljs$core$IFn$_invoke$arity$1(data)))));
});
oc.web.utils.activity.empty_body_QMARK_ = (function oc$web$utils$activity$empty_body_QMARK_(body){
return cljs.core.boolean$((function (){var or__4126__auto__ = cuerdas.core.blank_QMARK_(body);
if(or__4126__auto__){
return or__4126__auto__;
} else {
return cljs.core.re_matches(/^(\s*<p[^>]*>\s*(<br[^>]*\/?>)*?\s*<\/p>\s*)*$/i,body);
}
})());
});
oc.web.utils.activity.has_body_QMARK_ = (function oc$web$utils$activity$has_body_QMARK_(data){
return (!(oc.web.utils.activity.empty_body_QMARK_(new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(data))));
});
oc.web.utils.activity.has_text_QMARK_ = (function oc$web$utils$activity$has_text_QMARK_(data){
return ((oc.web.utils.activity.has_headline_QMARK_(data)) || (oc.web.utils.activity.has_body_QMARK_(data)));
});
oc.web.utils.activity.has_content_QMARK_ = (function oc$web$utils$activity$has_content_QMARK_(data){
return (((!((new cljs.core.Keyword(null,"video-id","video-id",2132630536).cljs$core$IFn$_invoke$arity$1(data) == null)))) || (oc.web.utils.activity.has_attachments_QMARK_(data)) || (oc.web.utils.activity.has_text_QMARK_(data)));
});
/**
 * Given 2 list of reduced items (reduced since they contains only the uuid and resource-type props)
 * return a single list without duplicates depending on the direction.
 * This is necessary since the lists could contain duplicates because they are loaded in 2 different
 * moments and new activity could lead to changes in the sort.
 */
oc.web.utils.activity.merge_items_lists = (function oc$web$utils$activity$merge_items_lists(new_items_list,old_items_list,direction){
if(cljs.core.truth_((function (){var and__4115__auto__ = direction;
if(cljs.core.truth_(and__4115__auto__)){
return ((cljs.core.seq(old_items_list)) && (cljs.core.seq(new_items_list)));
} else {
return and__4115__auto__;
}
})())){
var old_uuids = cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),old_items_list);
var new_uuids = cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),new_items_list);
var next_items_uuids = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(direction,new cljs.core.Keyword(null,"down","down",1565245570)))?cljs.core.vec(cljs.core.distinct.cljs$core$IFn$_invoke$arity$1(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(old_uuids,new_uuids))):cljs.core.vec(cljs.core.reverse(cljs.core.distinct.cljs$core$IFn$_invoke$arity$1(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(cljs.core.reverse(old_uuids),cljs.core.reverse(new_uuids))))));
var old_items_map = cljs.core.zipmap(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),old_items_list),old_items_list);
var items_map = cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (merge_items_map,item){
var merged_item = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([item,cljs.core.get.cljs$core$IFn$_invoke$arity$2(old_items_map,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(item))], 0));
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(merge_items_map,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(item),merged_item);
}),old_items_map,new_items_list);
return cljs.core.mapv.cljs$core$IFn$_invoke$arity$2(items_map,next_items_uuids);
} else {
if(cljs.core.seq(old_items_list)){
return cljs.core.vec(old_items_list);
} else {
return cljs.core.vec(new_items_list);

}
}
});
oc.web.utils.activity.default_caught_up_message = "You\u2019re up to date";
oc.web.utils.activity.next_activity_timestamp = (function oc$web$utils$activity$next_activity_timestamp(prev_item){
if(cljs.core.truth_(new cljs.core.Keyword(null,"last-activity-at","last-activity-at",670511998).cljs$core$IFn$_invoke$arity$1(prev_item))){
return oc.web.lib.utils.js_date.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(oc.web.lib.utils.js_date.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"last-activity-at","last-activity-at",670511998).cljs$core$IFn$_invoke$arity$1(prev_item)], 0)).getTime() + (1))], 0)).toISOString();
} else {
return oc.web.lib.utils.as_of_now();
}
});
oc.web.utils.activity.caught_up_map = (function oc$web$utils$activity$caught_up_map(var_args){
var G__41908 = arguments.length;
switch (G__41908) {
case 2:
return oc.web.utils.activity.caught_up_map.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.utils.activity.caught_up_map.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.utils.activity.caught_up_map.cljs$core$IFn$_invoke$arity$2 = (function (container_slug,prev_items){
return oc.web.utils.activity.caught_up_map.cljs$core$IFn$_invoke$arity$3(container_slug,prev_items,oc.web.utils.activity.default_caught_up_message);
}));

(oc.web.utils.activity.caught_up_map.cljs$core$IFn$_invoke$arity$3 = (function (container_slug,prev_items,message){
var t = oc.web.utils.activity.next_activity_timestamp(cljs.core.last(prev_items));
return new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"resource-type","resource-type",1844262326),new cljs.core.Keyword(null,"caught-up","caught-up",-1338440788),new cljs.core.Keyword(null,"last-activity-at","last-activity-at",670511998),t,new cljs.core.Keyword(null,"message","message",-406056002),message,new cljs.core.Keyword(null,"gray-style","gray-style",-1595613431),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(container_slug,new cljs.core.Keyword(null,"following","following",-2049193617)))?((cljs.core.not(cljs.core.seq(prev_items))) || (cljs.core.every_QMARK_(new cljs.core.Keyword(null,"publisher?","publisher?",30448149),prev_items))):cljs.core.not(cljs.core.seq(prev_items)))], null);
}));

(oc.web.utils.activity.caught_up_map.cljs$lang$maxFixedArity = 3);

oc.web.utils.activity.insert_caught_up = (function oc$web$utils$activity$insert_caught_up(var_args){
var args__4742__auto__ = [];
var len__4736__auto___42444 = arguments.length;
var i__4737__auto___42445 = (0);
while(true){
if((i__4737__auto___42445 < len__4736__auto___42444)){
args__4742__auto__.push((arguments[i__4737__auto___42445]));

var G__42446 = (i__4737__auto___42445 + (1));
i__4737__auto___42445 = G__42446;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((4) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((4)),(0),null)):null);
return oc.web.utils.activity.insert_caught_up.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),argseq__4743__auto__);
});

(oc.web.utils.activity.insert_caught_up.cljs$core$IFn$_invoke$arity$variadic = (function (container_slug,items_list,check_fn,ignore_fn,p__41914){
var vec__41915 = p__41914;
var map__41918 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41915,(0),null);
var map__41918__$1 = (((((!((map__41918 == null))))?(((((map__41918.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__41918.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__41918):map__41918);
var opts = map__41918__$1;
var hide_top_line = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41918__$1,new cljs.core.Keyword(null,"hide-top-line","hide-top-line",7433671));
var hide_bottom_line = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41918__$1,new cljs.core.Keyword(null,"hide-bottom-line","hide-bottom-line",407605427));
var has_next = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41918__$1,new cljs.core.Keyword(null,"has-next","has-next",683185232));
if(cljs.core.seq(items_list)){
var index = (function (){var last_valid_idx = (0);
var current_idx = (0);
while(true){
var item = cljs.core.get.cljs$core$IFn$_invoke$arity$2(items_list,current_idx);
if((item == null)){
return last_valid_idx;
} else {
if(cljs.core.truth_(((cljs.core.fn_QMARK_(ignore_fn))?(ignore_fn.cljs$core$IFn$_invoke$arity$1 ? ignore_fn.cljs$core$IFn$_invoke$arity$1(item) : ignore_fn.call(null,item)):false))){
var G__42447 = last_valid_idx;
var G__42448 = (current_idx + (1));
last_valid_idx = G__42447;
current_idx = G__42448;
continue;
} else {
if(cljs.core.truth_((check_fn.cljs$core$IFn$_invoke$arity$1 ? check_fn.cljs$core$IFn$_invoke$arity$1(item) : check_fn.call(null,item)))){
return last_valid_idx;
} else {
var G__42449 = (current_idx + (1));
var G__42450 = (current_idx + (1));
last_valid_idx = G__42449;
current_idx = G__42450;
continue;

}
}
}
break;
}
})();
var vec__41920 = cljs.core.split_at(index,items_list);
var before = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41920,(0),null);
var after = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41920,(1),null);
if(cljs.core.truth_(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(index,cljs.core.count(items_list)))?(function (){var or__4126__auto__ = has_next;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return hide_bottom_line;
}
})():false))){
return cljs.core.vec(items_list);
} else {
if(cljs.core.truth_((function (){var and__4115__auto__ = hide_top_line;
if(cljs.core.truth_(and__4115__auto__)){
return (index === (0));
} else {
return and__4115__auto__;
}
})())){
return cljs.core.vec(items_list);
} else {
return cljs.core.vec(cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,cljs.core.concat.cljs$core$IFn$_invoke$arity$variadic(before,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [oc.web.utils.activity.caught_up_map.cljs$core$IFn$_invoke$arity$2(container_slug,before)], null),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([after], 0))));

}
}
} else {
return null;
}
}));

(oc.web.utils.activity.insert_caught_up.cljs$lang$maxFixedArity = (4));

/** @this {Function} */
(oc.web.utils.activity.insert_caught_up.cljs$lang$applyTo = (function (seq41909){
var G__41910 = cljs.core.first(seq41909);
var seq41909__$1 = cljs.core.next(seq41909);
var G__41911 = cljs.core.first(seq41909__$1);
var seq41909__$2 = cljs.core.next(seq41909__$1);
var G__41912 = cljs.core.first(seq41909__$2);
var seq41909__$3 = cljs.core.next(seq41909__$2);
var G__41913 = cljs.core.first(seq41909__$3);
var seq41909__$4 = cljs.core.next(seq41909__$3);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__41910,G__41911,G__41912,G__41913,seq41909__$4);
}));

oc.web.utils.activity.insert_open_close_item = (function oc$web$utils$activity$insert_open_close_item(items_list,check_fn){
return cljs.core.vec(cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (idx){
var prev_item = cljs.core.get.cljs$core$IFn$_invoke$arity$2(items_list,(idx - (1)));
var item = cljs.core.get.cljs$core$IFn$_invoke$arity$2(items_list,idx);
var next_item = cljs.core.get.cljs$core$IFn$_invoke$arity$2(items_list,(idx + (1)));
if(cljs.core.truth_(item)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$variadic(item,new cljs.core.Keyword(null,"open-item","open-item",-1938301269),(check_fn.cljs$core$IFn$_invoke$arity$3 ? check_fn.cljs$core$IFn$_invoke$arity$3(new cljs.core.Keyword(null,"open","open",-1763596448),item,prev_item) : check_fn.call(null,new cljs.core.Keyword(null,"open","open",-1763596448),item,prev_item)),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"close-item","close-item",-38717813),(check_fn.cljs$core$IFn$_invoke$arity$3 ? check_fn.cljs$core$IFn$_invoke$arity$3(new cljs.core.Keyword(null,"close","close",1835149582),item,next_item) : check_fn.call(null,new cljs.core.Keyword(null,"close","close",1835149582),item,next_item))], 0));
} else {
return null;
}
}),cljs.core.range.cljs$core$IFn$_invoke$arity$1(cljs.core.count(items_list)))));
});
oc.web.utils.activity.insert_ending_item = (function oc$web$utils$activity$insert_ending_item(items_list,has_next){
var closing_item_QMARK_ = (cljs.core.count(items_list) > (10));
var last_item = cljs.core.last(items_list);
if(cljs.core.truth_((function (){var and__4115__auto__ = cljs.core.seq(items_list);
if(and__4115__auto__){
return has_next;
} else {
return and__4115__auto__;
}
})())){
return cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(items_list,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"resource-type","resource-type",1844262326),new cljs.core.Keyword(null,"loading-more","loading-more",-1145525667),new cljs.core.Keyword(null,"last-activity-at","last-activity-at",670511998),oc.web.utils.activity.next_activity_timestamp(last_item),new cljs.core.Keyword(null,"message","message",-406056002),"Loading more updates..."], null)], null)));
} else {
if(closing_item_QMARK_){
return cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(items_list,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"resource-type","resource-type",1844262326),new cljs.core.Keyword(null,"closing-item","closing-item",458331664),new cljs.core.Keyword(null,"last-activity-at","last-activity-at",670511998),oc.web.utils.activity.next_activity_timestamp(cljs.core.last(items_list)),new cljs.core.Keyword(null,"message","message",-406056002),"\uD83E\uDD20 You've reached the end, partner"], null)], null)));
} else {
return cljs.core.vec(items_list);

}
}
});
oc.web.utils.activity.get_comments_finished = (function oc$web$utils$activity$get_comments_finished(org_slug,comments_key,activity_data,p__41929){
var map__41930 = p__41929;
var map__41930__$1 = (((((!((map__41930 == null))))?(((((map__41930.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__41930.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__41930):map__41930);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41930__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41930__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__41930__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
if(cljs.core.truth_(success)){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("comments-get","finish","comments-get/finish",-1883926059),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"success","success",1890645906),success,new cljs.core.Keyword(null,"error","error",-978969032),(cljs.core.truth_(success)?null:body),new cljs.core.Keyword(null,"comments-key","comments-key",-1941842753),comments_key,new cljs.core.Keyword(null,"body","body",-2049205669),((cljs.core.seq(body))?oc.web.lib.json.json__GT_cljs(body):null),new cljs.core.Keyword(null,"activity-uuid","activity-uuid",-663317778),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data),new cljs.core.Keyword(null,"secure-activity-uuid","secure-activity-uuid",597625027),oc.web.dispatcher.current_secure_activity_id.cljs$core$IFn$_invoke$arity$0()], null)], null));

var replies_data = oc.web.dispatcher.replies_data.cljs$core$IFn$_invoke$arity$2(org_slug,cljs.core.deref(oc.web.dispatcher.app_state));
var current_board_slug = oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0();
var is_replies_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug),new cljs.core.Keyword(null,"replies","replies",-1389888974));
var entry_is_in_replies_QMARK_ = cljs.core.some((function (p1__41927_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__41927_SHARP_),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data))){
return p1__41927_SHARP_;
} else {
return null;
}
}),new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897).cljs$core$IFn$_invoke$arity$1(replies_data));
if(cljs.core.truth_(((is_replies_QMARK_)?entry_is_in_replies_QMARK_:false))){
return oc.web.lib.utils.after((10),(function (){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"update-replies-comments","update-replies-comments",-1613944614),org_slug,current_board_slug], null));
}));
} else {
return null;
}
} else {
return null;
}
});
oc.web.utils.activity.get_comments = (function oc$web$utils$activity$get_comments(activity_data){
if(cljs.core.truth_(activity_data)){
var org_slug = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
var comments_key = oc.web.dispatcher.activity_comments_key(org_slug,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));
var comments_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(activity_data),"comments"], 0));
if(cljs.core.truth_(comments_link)){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"comments-get","comments-get",477068556),comments_key,activity_data], null));

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"reply-comments-loaded","reply-comments-loaded",-1365821825),org_slug,activity_data], null));

return oc.web.api.get_comments(comments_link,(function (p1__41932_SHARP_){
return oc.web.utils.activity.get_comments_finished(org_slug,comments_key,activity_data,p1__41932_SHARP_);
}));
} else {
return null;
}
} else {
return null;
}
});
oc.web.utils.activity.get_comments_if_needed = (function oc$web$utils$activity$get_comments_if_needed(var_args){
var G__41934 = arguments.length;
switch (G__41934) {
case 1:
return oc.web.utils.activity.get_comments_if_needed.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.utils.activity.get_comments_if_needed.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.utils.activity.get_comments_if_needed.cljs$core$IFn$_invoke$arity$1 = (function (activity_data){
return oc.web.utils.activity.get_comments_if_needed.cljs$core$IFn$_invoke$arity$2(activity_data,oc.web.dispatcher.comments_data.cljs$core$IFn$_invoke$arity$0());
}));

(oc.web.utils.activity.get_comments_if_needed.cljs$core$IFn$_invoke$arity$2 = (function (activity_data,all_comments_data){
var comments_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(activity_data),"comments"], 0));
var activity_uuid = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data);
var comments_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(all_comments_data,activity_uuid);
var should_load_comments_QMARK_ = ((cljs.core.map_QMARK_(comments_link)) && ((new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(comments_link) > (0))) && (cljs.core.not(new cljs.core.Keyword(null,"loading","loading",-737050189).cljs$core$IFn$_invoke$arity$1(comments_data))) && ((!(cljs.core.contains_QMARK_(comments_data,new cljs.core.Keyword(null,"sorted-comments","sorted-comments",1988882718))))));
if(should_load_comments_QMARK_){
return oc.web.utils.activity.get_comments(activity_data);
} else {
return null;
}
}));

(oc.web.utils.activity.get_comments_if_needed.cljs$lang$maxFixedArity = 2);

oc.web.utils.activity.is_emoji = (function oc$web$utils$activity$is_emoji(body){
var plain_text = $(["<div>",cljs.core.str.cljs$core$IFn$_invoke$arity$1(body),"</div>"].join('')).text();
var is_emoji_QMARK_ = RegExp("^([\uD800-\uDBFF])([\uDC00-\uDFFF])","g");
var is_text_message_QMARK_ = RegExp("[a-zA-Z0-9\\s!?@#\\$%\\^&(())_=\\-<>,\\.\\*;':\"]","g");
if((cljs.core.count(plain_text) <= (11))){
var and__4115__auto__ = plain_text.match(is_emoji_QMARK_);
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(plain_text.match(is_text_message_QMARK_));
} else {
return and__4115__auto__;
}
} else {
return false;
}
});
/**
 * @param {...*} var_args
 */
oc.web.utils.activity.parse_comment = (function() { 
var oc$web$utils$activity$parse_comment__delegate = function (args__33705__auto__){
var ocr_41938 = cljs.core.vec(args__33705__auto__);
try{if(((cljs.core.vector_QMARK_(ocr_41938)) && ((cljs.core.count(ocr_41938) === 3)))){
try{var ocr_41938_2__41943 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41938,(2));
if((ocr_41938_2__41943 === null)){
var org_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41938,(0));
var activity_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41938,(1));
return cljs.core.PersistentArrayMap.EMPTY;
} else {
throw cljs.core.match.backtrack;

}
}catch (e41974){if((e41974 instanceof Error)){
var e__32662__auto__ = e41974;
if((e__32662__auto__ === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto__;
}
} else {
throw e41974;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41955){if((e41955 instanceof Error)){
var e__32662__auto__ = e41955;
if((e__32662__auto__ === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41938)) && ((cljs.core.count(ocr_41938) === 4)))){
try{var ocr_41938_2__41946 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41938,(2));
if((ocr_41938_2__41946 === null)){
var org_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41938,(0));
var activity_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41938,(1));
return cljs.core.PersistentArrayMap.EMPTY;
} else {
throw cljs.core.match.backtrack;

}
}catch (e41966){if((e41966 instanceof Error)){
var e__32662__auto____$1 = e41966;
if((e__32662__auto____$1 === cljs.core.match.backtrack)){
try{var ocr_41938_2__41946 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41938,(2));
if(cljs.core.truth_(new cljs.core.Keyword(null,"sorted-comments","sorted-comments",1988882718).cljs$core$IFn$_invoke$arity$1(ocr_41938_2__41946))){
var comments_map = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41938,(2));
var org_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41938,(0));
var activity_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41938,(1));
var container_seen_at = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41938,(3));
var G__41970 = org_data;
var G__41971 = activity_data;
var G__41972 = new cljs.core.Keyword(null,"sorted-comments","sorted-comments",1988882718).cljs$core$IFn$_invoke$arity$1(comments_map);
var G__41973 = container_seen_at;
return (oc.web.utils.activity.parse_comment.cljs$core$IFn$_invoke$arity$4 ? oc.web.utils.activity.parse_comment.cljs$core$IFn$_invoke$arity$4(G__41970,G__41971,G__41972,G__41973) : oc.web.utils.activity.parse_comment.call(null,G__41970,G__41971,G__41972,G__41973));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41967){if((e41967 instanceof Error)){
var e__32662__auto____$2 = e41967;
if((e__32662__auto____$2 === cljs.core.match.backtrack)){
try{var ocr_41938_2__41946 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41938,(2));
if(cljs.core.sequential_QMARK_(ocr_41938_2__41946)){
try{var ocr_41938_3__41947 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41938,(3));
if((function (p1__41935_SHARP_){
return (((p1__41935_SHARP_ == null)) || (typeof p1__41935_SHARP_ === 'string'));
})(ocr_41938_3__41947)){
var container_seen_at = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41938,(3));
var comments = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41938,(2));
var org_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41938,(0));
var activity_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41938,(1));
return cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__41936_SHARP_){
return (oc.web.utils.activity.parse_comment.cljs$core$IFn$_invoke$arity$4 ? oc.web.utils.activity.parse_comment.cljs$core$IFn$_invoke$arity$4(org_data,activity_data,p1__41936_SHARP_,container_seen_at) : oc.web.utils.activity.parse_comment.call(null,org_data,activity_data,p1__41936_SHARP_,container_seen_at));
}),comments);
} else {
throw cljs.core.match.backtrack;

}
}catch (e41969){if((e41969 instanceof Error)){
var e__32662__auto____$3 = e41969;
if((e__32662__auto____$3 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$3;
}
} else {
throw e41969;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41968){if((e41968 instanceof Error)){
var e__32662__auto____$3 = e41968;
if((e__32662__auto____$3 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$3;
}
} else {
throw e41968;

}
}} else {
throw e__32662__auto____$2;
}
} else {
throw e41967;

}
}} else {
throw e__32662__auto____$1;
}
} else {
throw e41966;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41956){if((e41956 instanceof Error)){
var e__32662__auto____$1 = e41956;
if((e__32662__auto____$1 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41938)) && ((cljs.core.count(ocr_41938) === 3)))){
try{var ocr_41938_0__41948 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41938,(0));
if(cljs.core.map_QMARK_(ocr_41938_0__41948)){
try{var ocr_41938_1__41949 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41938,(1));
if(cljs.core.map_QMARK_(ocr_41938_1__41949)){
try{var ocr_41938_2__41950 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41938,(2));
if(cljs.core.map_QMARK_(ocr_41938_2__41950)){
var comment_map = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41938,(2));
var activity_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41938,(1));
var org_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41938,(0));
return (oc.web.utils.activity.parse_comment.cljs$core$IFn$_invoke$arity$4 ? oc.web.utils.activity.parse_comment.cljs$core$IFn$_invoke$arity$4(org_data,activity_data,comment_map,null) : oc.web.utils.activity.parse_comment.call(null,org_data,activity_data,comment_map,null));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41965){if((e41965 instanceof Error)){
var e__32662__auto____$2 = e41965;
if((e__32662__auto____$2 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$2;
}
} else {
throw e41965;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41964){if((e41964 instanceof Error)){
var e__32662__auto____$2 = e41964;
if((e__32662__auto____$2 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$2;
}
} else {
throw e41964;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41963){if((e41963 instanceof Error)){
var e__32662__auto____$2 = e41963;
if((e__32662__auto____$2 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$2;
}
} else {
throw e41963;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41957){if((e41957 instanceof Error)){
var e__32662__auto____$2 = e41957;
if((e__32662__auto____$2 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41938)) && ((cljs.core.count(ocr_41938) === 4)))){
try{var ocr_41938_0__41951 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41938,(0));
if(cljs.core.map_QMARK_(ocr_41938_0__41951)){
try{var ocr_41938_1__41952 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41938,(1));
if(cljs.core.map_QMARK_(ocr_41938_1__41952)){
try{var ocr_41938_2__41953 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41938,(2));
if(cljs.core.map_QMARK_(ocr_41938_2__41953)){
try{var ocr_41938_3__41954 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41938,(3));
if((function (p1__41937_SHARP_){
return (((p1__41937_SHARP_ == null)) || (typeof p1__41937_SHARP_ === 'string'));
})(ocr_41938_3__41954)){
var container_seen_at = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41938,(3));
var comment_map = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41938,(2));
var activity_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41938,(1));
var org_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41938,(0));
var edit_comment_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(comment_map),"partial-update"], 0));
var delete_comment_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(comment_map),"delete"], 0));
var can_react_QMARK_ = cljs.core.boolean$(oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(comment_map),"react","POST"], 0)));
var reply_parent = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"parent-uuid","parent-uuid",-2003485227).cljs$core$IFn$_invoke$arity$1(comment_map);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(comment_map);
}
})();
var is_root_comment = cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"parent-uuid","parent-uuid",-2003485227).cljs$core$IFn$_invoke$arity$1(comment_map));
var author_QMARK_ = oc.web.utils.activity.is_author_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([comment_map], 0));
var unread_QMARK_ = ((cljs.core.not(author_QMARK_))?oc.web.utils.comment.comment_unread_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([comment_map,new cljs.core.Keyword(null,"last-read-at","last-read-at",-216601930).cljs$core$IFn$_invoke$arity$1(activity_data)], 0)):false);
var unseen_QMARK__STAR_ = oc.web.utils.comment.comment_unseen_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([comment_map,container_seen_at], 0));
var ingore_QMARK_ = (function (){var and__4115__auto__ = author_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return unseen_QMARK__STAR_;
} else {
return and__4115__auto__;
}
})();
var unseen_QMARK_ = ((cljs.core.not(author_QMARK_))?unseen_QMARK__STAR_:false);
var is_emoji_comment_QMARK_ = oc.web.utils.activity.is_emoji(new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(comment_map));
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(comment_map,new cljs.core.Keyword(null,"resource-type","resource-type",1844262326),new cljs.core.Keyword(null,"comment","comment",532206069)),new cljs.core.Keyword(null,"author?","author?",-1083349935),author_QMARK_),new cljs.core.Keyword(null,"ingore?","ingore?",-733172152),ingore_QMARK_),new cljs.core.Keyword(null,"unread","unread",-1950424572),unread_QMARK_),new cljs.core.Keyword(null,"unseen","unseen",1063275592),unseen_QMARK_),new cljs.core.Keyword(null,"is-emoji","is-emoji",-1643384064),is_emoji_comment_QMARK_),new cljs.core.Keyword(null,"can-edit","can-edit",442089902),cljs.core.boolean$((function (){var and__4115__auto__ = edit_comment_link;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(is_emoji_comment_QMARK_);
} else {
return and__4115__auto__;
}
})())),new cljs.core.Keyword(null,"can-delete","can-delete",1620748590),cljs.core.boolean$(delete_comment_link)),new cljs.core.Keyword(null,"can-react","can-react",283229018),can_react_QMARK_),new cljs.core.Keyword(null,"reply-parent","reply-parent",579138103),reply_parent),new cljs.core.Keyword(null,"resource-uuid","resource-uuid",1798351789),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data)),new cljs.core.Keyword(null,"url","url",276297046),[oc.web.local_settings.web_server_domain,oc.web.urls.comment_url.cljs$core$IFn$_invoke$arity$4(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(activity_data),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(comment_map))].join(''));
} else {
throw cljs.core.match.backtrack;

}
}catch (e41962){if((e41962 instanceof Error)){
var e__32662__auto____$3 = e41962;
if((e__32662__auto____$3 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$3;
}
} else {
throw e41962;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41961){if((e41961 instanceof Error)){
var e__32662__auto____$3 = e41961;
if((e__32662__auto____$3 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$3;
}
} else {
throw e41961;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41960){if((e41960 instanceof Error)){
var e__32662__auto____$3 = e41960;
if((e__32662__auto____$3 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$3;
}
} else {
throw e41960;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41959){if((e41959 instanceof Error)){
var e__32662__auto____$3 = e41959;
if((e__32662__auto____$3 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$3;
}
} else {
throw e41959;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41958){if((e41958 instanceof Error)){
var e__32662__auto____$3 = e41958;
if((e__32662__auto____$3 === cljs.core.match.backtrack)){
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ocr_41938)].join('')));
} else {
throw e__32662__auto____$3;
}
} else {
throw e41958;

}
}} else {
throw e__32662__auto____$2;
}
} else {
throw e41957;

}
}} else {
throw e__32662__auto____$1;
}
} else {
throw e41956;

}
}} else {
throw e__32662__auto__;
}
} else {
throw e41955;

}
}};
var oc$web$utils$activity$parse_comment = function (var_args){
var args__33705__auto__ = null;
if (arguments.length > 0) {
var G__42452__i = 0, G__42452__a = new Array(arguments.length -  0);
while (G__42452__i < G__42452__a.length) {G__42452__a[G__42452__i] = arguments[G__42452__i + 0]; ++G__42452__i;}
  args__33705__auto__ = new cljs.core.IndexedSeq(G__42452__a,0,null);
} 
return oc$web$utils$activity$parse_comment__delegate.call(this,args__33705__auto__);};
oc$web$utils$activity$parse_comment.cljs$lang$maxFixedArity = 0;
oc$web$utils$activity$parse_comment.cljs$lang$applyTo = (function (arglist__42453){
var args__33705__auto__ = cljs.core.seq(arglist__42453);
return oc$web$utils$activity$parse_comment__delegate(args__33705__auto__);
});
oc$web$utils$activity$parse_comment.cljs$core$IFn$_invoke$arity$variadic = oc$web$utils$activity$parse_comment__delegate;
return oc$web$utils$activity$parse_comment;
})()
;
oc.web.utils.activity.parse_comments = (function oc$web$utils$activity$parse_comments(org_data,entry_data,new_comments,container_seen_at,reset_collapse_comments_QMARK_){
var old_comments = new cljs.core.Keyword(null,"replies-data","replies-data",1118937948).cljs$core$IFn$_invoke$arity$1(entry_data);
var old_comments_keep = cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__41975_SHARP_){
return cljs.core.select_keys(p1__41975_SHARP_,new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"uuid","uuid",-2145095719),new cljs.core.Keyword(null,"collapsed","collapsed",-628494523),new cljs.core.Keyword(null,"unseen","unseen",1063275592),new cljs.core.Keyword(null,"unwrapped-body","unwrapped-body",457764863)], null));
}),old_comments);
var keep_comments_map = cljs.core.zipmap(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),old_comments_keep),old_comments_keep);
var new_parsed_comments = cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (p1__41976_SHARP_){
var c = p1__41976_SHARP_;
var c__$1 = oc.web.utils.activity.parse_comment.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([org_data,entry_data,c,container_seen_at], 0));
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([c__$1,cljs.core.get.cljs$core$IFn$_invoke$arity$2(keep_comments_map,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(c__$1))], 0));
}),new_comments);
var new_sorted_comments = oc.web.utils.comment.sort_comments.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new_parsed_comments], 0));
if(cljs.core.truth_(reset_collapse_comments_QMARK_)){
return oc.web.utils.comment.collapse_comments.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new_sorted_comments,container_seen_at], 0));
} else {
return new_sorted_comments;
}
});
oc.web.utils.activity.for_you_context = (function oc$web$utils$activity$for_you_context(entry_data,current_user_id){
var replies_data = new cljs.core.Keyword(null,"replies-data","replies-data",1118937948).cljs$core$IFn$_invoke$arity$1(entry_data);
var last_comment = cljs.core.last(replies_data);
var subject = (cljs.core.truth_(new cljs.core.Keyword(null,"author?","author?",-1083349935).cljs$core$IFn$_invoke$arity$1(last_comment))?"You ":[cljs.core.str.cljs$core$IFn$_invoke$arity$1((function (){var or__4126__auto__ = new cljs.core.Keyword(null,"short-name","short-name",-1767085022).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"author","author",2111686192).cljs$core$IFn$_invoke$arity$1(last_comment));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = new cljs.core.Keyword(null,"first-name","first-name",-1559982131).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"author","author",2111686192).cljs$core$IFn$_invoke$arity$1(last_comment));
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
return new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"author","author",2111686192).cljs$core$IFn$_invoke$arity$1(last_comment));
}
}
})())," "].join(''));
var mention_regexp = (new RegExp(["data-user-id=\"",cljs.core.str.cljs$core$IFn$_invoke$arity$1(current_user_id),"\""].join(''),"ig"));
var mention_QMARK_ = new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(last_comment).match(mention_regexp);
var publisher_QMARK_ = new cljs.core.Keyword(null,"publisher?","publisher?",30448149).cljs$core$IFn$_invoke$arity$1(entry_data);
var unseen_QMARK_ = new cljs.core.Keyword(null,"unseen","unseen",1063275592).cljs$core$IFn$_invoke$arity$1(last_comment);
var verb = (cljs.core.truth_(unseen_QMARK_)?" left a new comment":(cljs.core.truth_(mention_QMARK_)?"mentioned you in a comment":" commented"
));
var direct_object = (cljs.core.truth_(publisher_QMARK_)?" on your update":" on an update you are watching");
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"label","label",1718410804),[subject,verb,direct_object].join(''),new cljs.core.Keyword(null,"timestamp","timestamp",579478971),new cljs.core.Keyword(null,"created-at","created-at",-89248644).cljs$core$IFn$_invoke$arity$1(last_comment)], null);
});
oc.web.utils.activity.entry_replies_data = (function oc$web$utils$activity$entry_replies_data(entry_data,org_data,fixed_items,container_seen_at){
var comments = oc.web.dispatcher.activity_sorted_comments_data.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data));
var full_entry = cljs.core.get.cljs$core$IFn$_invoke$arity$2(fixed_items,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data));
var comments_count = new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(full_entry),"comments"], 0)));
var fallback_to_inline_QMARK_ = (((comments_count > (0))) && (cljs.core.empty_QMARK_(comments)));
var temp_comments = ((cljs.core.seq(comments))?comments:new cljs.core.Keyword(null,"comments","comments",-293346423).cljs$core$IFn$_invoke$arity$1(full_entry));
var comments_loaded_QMARK_ = cljs.core.boolean$((((comments_count === (0))) || (cljs.core.seq(comments))));
var reset_collapse_comments_QMARK_ = ((cljs.core.not(new cljs.core.Keyword(null,"expanded-replies?","expanded-replies?",-1540742179).cljs$core$IFn$_invoke$arity$1(entry_data))) && ((((!(cljs.core.contains_QMARK_(entry_data,new cljs.core.Keyword(null,"replies-data","replies-data",1118937948))))) || (((comments_loaded_QMARK_) && (cljs.core.not(new cljs.core.Keyword(null,"comments-loaded?","comments-loaded?",-595034611).cljs$core$IFn$_invoke$arity$1(entry_data))))))));
var e = entry_data;
var e__$1 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(e,new cljs.core.Keyword(null,"comments-loaded?","comments-loaded?",-595034611),comments_loaded_QMARK_);
var e__$2 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(e__$1,new cljs.core.Keyword(null,"replies-data","replies-data",1118937948),oc.web.utils.activity.parse_comments(org_data,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(e__$1,new cljs.core.Keyword(null,"headline","headline",-157157727),new cljs.core.Keyword(null,"headline","headline",-157157727).cljs$core$IFn$_invoke$arity$1(full_entry)),temp_comments,container_seen_at,reset_collapse_comments_QMARK_));
var e__$3 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(e__$2,new cljs.core.Keyword(null,"for-you-context","for-you-context",1744709462),oc.web.utils.activity.for_you_context(e__$2,oc.web.lib.jwt.user_id()));
var e__$4 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(e__$3,new cljs.core.Keyword(null,"comments-count","comments-count",1713184539),((comments_loaded_QMARK_)?cljs.core.count(comments):comments_count));
var e__$5 = cljs.core.update.cljs$core$IFn$_invoke$arity$3(e__$4,new cljs.core.Keyword(null,"unseen-comments","unseen-comments",-793262869),(function (p1__41979_SHARP_){
return cljs.core.boolean$(((cljs.core.not(cljs.core.seq(comments)))?p1__41979_SHARP_:cljs.core.seq(cljs.core.filter.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"unseen","unseen",1063275592),new cljs.core.Keyword(null,"replies-data","replies-data",1118937948).cljs$core$IFn$_invoke$arity$1(e__$4)))));
}));
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(e__$5,new cljs.core.Keyword(null,"ignore-comments","ignore-comments",1137086177),oc.web.utils.activity.comments_ignore_QMARK_(e__$5,container_seen_at));
});
/**
 * Add `:read-only`, `:board-slug`, `:board-name` and `:resource-type` keys to the entry map.
 * @param {...*} var_args
 */
oc.web.utils.activity.parse_entry = (function() { 
var oc$web$utils$activity$parse_entry__delegate = function (args__33705__auto__){
var ocr_41988 = cljs.core.vec(args__33705__auto__);
try{if(((cljs.core.vector_QMARK_(ocr_41988)) && ((cljs.core.count(ocr_41988) === 3)))){
var entry_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41988,(0));
var board_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41988,(1));
var changes = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41988,(2));
var G__42033 = entry_data;
var G__42034 = board_data;
var G__42035 = changes;
var G__42036 = oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$0();
var G__42037 = null;
return (oc.web.utils.activity.parse_entry.cljs$core$IFn$_invoke$arity$5 ? oc.web.utils.activity.parse_entry.cljs$core$IFn$_invoke$arity$5(G__42033,G__42034,G__42035,G__42036,G__42037) : oc.web.utils.activity.parse_entry.call(null,G__42033,G__42034,G__42035,G__42036,G__42037));
} else {
throw cljs.core.match.backtrack;

}
}catch (e42002){if((e42002 instanceof Error)){
var e__32662__auto__ = e42002;
if((e__32662__auto__ === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41988)) && ((cljs.core.count(ocr_41988) === 4)))){
var entry_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41988,(0));
var board_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41988,(1));
var changes = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41988,(2));
var active_users = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41988,(3));
return (oc.web.utils.activity.parse_entry.cljs$core$IFn$_invoke$arity$5 ? oc.web.utils.activity.parse_entry.cljs$core$IFn$_invoke$arity$5(entry_data,board_data,changes,active_users,null) : oc.web.utils.activity.parse_entry.call(null,entry_data,board_data,changes,active_users,null));
} else {
throw cljs.core.match.backtrack;

}
}catch (e42003){if((e42003 instanceof Error)){
var e__32662__auto____$1 = e42003;
if((e__32662__auto____$1 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41988)) && ((cljs.core.count(ocr_41988) === 5)))){
try{var ocr_41988_4__42001 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41988,(4));
if((function (p1__41986_SHARP_){
return (((p1__41986_SHARP_ == null)) || (typeof p1__41986_SHARP_ === 'string'));
})(ocr_41988_4__42001)){
var container_seen_at = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41988,(4));
var entry_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41988,(0));
var board_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41988,(1));
var changes = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41988,(2));
var active_users = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41988,(3));
if(cljs.core.truth_((function (){var or__4126__auto__ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(entry_data,new cljs.core.Keyword(null,"404","404",948666615));
if(or__4126__auto__){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"loading","loading",-737050189).cljs$core$IFn$_invoke$arity$1(entry_data);
}
})())){
return entry_data;
} else {
var comments_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(entry_data),"comments"], 0));
var add_comment_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(entry_data),"create","POST"], 0));
var published_QMARK_ = oc.web.utils.activity.is_published_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([entry_data], 0));
var fixed_board_uuid = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127).cljs$core$IFn$_invoke$arity$1(entry_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(board_data);
}
})();
var fixed_board_slug = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(entry_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(board_data);
}
})();
var fixed_board_name = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"board-name","board-name",-677515056).cljs$core$IFn$_invoke$arity$1(entry_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(board_data);
}
})();
var fixed_board_access = (cljs.core.truth_(published_QMARK_)?(function (){var or__4126__auto__ = new cljs.core.Keyword(null,"board-access","board-access",1233510317).cljs$core$IFn$_invoke$arity$1(entry_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"access","access",2027349272).cljs$core$IFn$_invoke$arity$1(board_data);
}
})():"private");
var fixed_publisher_board = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803).cljs$core$IFn$_invoke$arity$1(entry_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803).cljs$core$IFn$_invoke$arity$1(board_data);
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
return false;
}
}
})();
var is_uploading_video_QMARK_ = oc.web.dispatcher.uploading_video_data.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"video-id","video-id",2132630536).cljs$core$IFn$_invoke$arity$1(entry_data));
var fixed_video_id = new cljs.core.Keyword(null,"video-id","video-id",2132630536).cljs$core$IFn$_invoke$arity$1(entry_data);
var fixed_publisher = (cljs.core.truth_(published_QMARK_)?cljs.core.get.cljs$core$IFn$_invoke$arity$2(active_users,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"publisher","publisher",-153364540).cljs$core$IFn$_invoke$arity$1(entry_data))):null);
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3((function (){var e = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(entry_data,new cljs.core.Keyword(null,"resource-type","resource-type",1844262326),new cljs.core.Keyword(null,"entry","entry",505168823)),new cljs.core.Keyword(null,"published?","published?",-1603043839),published_QMARK_);
var e__$1 = (cljs.core.truth_(published_QMARK_)?cljs.core.update.cljs$core$IFn$_invoke$arity$4(e,new cljs.core.Keyword(null,"publisher","publisher",-153364540),cljs.core.merge,fixed_publisher):e);
var e__$2 = (cljs.core.truth_(published_QMARK_)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(e__$1,new cljs.core.Keyword(null,"publisher?","publisher?",30448149),oc.web.utils.activity.is_publisher_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([e__$1], 0))):e__$1);
var e__$3 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(e__$2,new cljs.core.Keyword(null,"unseen","unseen",1063275592),oc.web.utils.activity.entry_unseen_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([e__$2,container_seen_at], 0)));
var e__$4 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(e__$3,new cljs.core.Keyword(null,"unread","unread",-1950424572),oc.web.utils.activity.entry_unread_QMARK_(e__$3,changes));
var e__$5 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(e__$4,new cljs.core.Keyword(null,"read-only","read-only",-191706886),oc.web.utils.activity.readonly_entry_QMARK_(new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(e__$4)));
return cljs.core.update.cljs$core$IFn$_invoke$arity$3(e__$5,new cljs.core.Keyword(null,"comments","comments",-293346423),(function (comments){
return cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (p1__41987_SHARP_){
return oc.web.utils.activity.parse_comment.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([org_data,e__$5,p1__41987_SHARP_,container_seen_at], 0));
}),comments);
}));
})(),new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127),fixed_board_uuid),new cljs.core.Keyword(null,"board-slug","board-slug",99003663),fixed_board_slug),new cljs.core.Keyword(null,"board-name","board-name",-677515056),fixed_board_name),new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803),fixed_publisher_board),new cljs.core.Keyword(null,"board-access","board-access",1233510317),fixed_board_access),new cljs.core.Keyword(null,"has-comments","has-comments",-2105661284),cljs.core.boolean$(comments_link)),new cljs.core.Keyword(null,"can-comment","can-comment",718623455),cljs.core.boolean$(add_comment_link)),new cljs.core.Keyword(null,"fixed-video-id","fixed-video-id",-1380335259),fixed_video_id),new cljs.core.Keyword(null,"has-headline","has-headline",-863567038),oc.web.utils.activity.has_headline_QMARK_(entry_data)),new cljs.core.Keyword(null,"body-thumbnail","body-thumbnail",1858934017),((cljs.core.seq(new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(entry_data)))?oc.lib.html.first_body_thumbnail(new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(entry_data)):null)),new cljs.core.Keyword(null,"container-seen-at","container-seen-at",-1262643681),container_seen_at);
}
} else {
throw cljs.core.match.backtrack;

}
}catch (e42005){if((e42005 instanceof Error)){
var e__32662__auto____$2 = e42005;
if((e__32662__auto____$2 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__32662__auto____$2;
}
} else {
throw e42005;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e42004){if((e42004 instanceof Error)){
var e__32662__auto____$2 = e42004;
if((e__32662__auto____$2 === cljs.core.match.backtrack)){
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ocr_41988)].join('')));
} else {
throw e__32662__auto____$2;
}
} else {
throw e42004;

}
}} else {
throw e__32662__auto____$1;
}
} else {
throw e42003;

}
}} else {
throw e__32662__auto__;
}
} else {
throw e42002;

}
}};
var oc$web$utils$activity$parse_entry = function (var_args){
var args__33705__auto__ = null;
if (arguments.length > 0) {
var G__42454__i = 0, G__42454__a = new Array(arguments.length -  0);
while (G__42454__i < G__42454__a.length) {G__42454__a[G__42454__i] = arguments[G__42454__i + 0]; ++G__42454__i;}
  args__33705__auto__ = new cljs.core.IndexedSeq(G__42454__a,0,null);
} 
return oc$web$utils$activity$parse_entry__delegate.call(this,args__33705__auto__);};
oc$web$utils$activity$parse_entry.cljs$lang$maxFixedArity = 0;
oc$web$utils$activity$parse_entry.cljs$lang$applyTo = (function (arglist__42455){
var args__33705__auto__ = cljs.core.seq(arglist__42455);
return oc$web$utils$activity$parse_entry__delegate(args__33705__auto__);
});
oc$web$utils$activity$parse_entry.cljs$core$IFn$_invoke$arity$variadic = oc$web$utils$activity$parse_entry__delegate;
return oc$web$utils$activity$parse_entry;
})()
;
/**
 * Fix org data coming from the API.
 */
oc.web.utils.activity.parse_org = (function oc$web$utils$activity$parse_org(db,org_data){
if(cljs.core.truth_(org_data)){
var unfollow_boards_set = cljs.core.set(oc.web.dispatcher.unfollow_board_uuids.cljs$core$IFn$_invoke$arity$0());
var follow_lists_loaded_QMARK_ = cljs.core.map_QMARK_(oc.web.dispatcher.follow_list.cljs$core$IFn$_invoke$arity$0());
var fixed_boards = cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__42038_SHARP_){
var b = p1__42038_SHARP_;
var b__$1 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(b,new cljs.core.Keyword(null,"read-only","read-only",-191706886),oc.web.utils.activity.readonly_board_QMARK_(new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(p1__42038_SHARP_)));
if(follow_lists_loaded_QMARK_){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(b__$1,new cljs.core.Keyword(null,"following","following",-2049193617),cljs.core.not((function (){var G__42040 = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__42038_SHARP_);
return (unfollow_boards_set.cljs$core$IFn$_invoke$arity$1 ? unfollow_boards_set.cljs$core$IFn$_invoke$arity$1(G__42040) : unfollow_boards_set.call(null,G__42040));
})()));
} else {
return b__$1;
}
}),new cljs.core.Keyword(null,"boards","boards",1912049694).cljs$core$IFn$_invoke$arity$1(org_data));
var drafts_board = oc.web.dispatcher.org_board_data.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([org_data,oc.web.lib.utils.default_drafts_board_slug], 0));
var drafts_link = (cljs.core.truth_(drafts_board)?oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(drafts_board),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["item","self"], null),"GET"], 0)):null);
var previous_org_drafts_count = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_data_key(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data)),new cljs.core.Keyword(null,"drafts-count","drafts-count",-1710494641)));
var previous_bookmarks_count = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_data_key(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data)),new cljs.core.Keyword(null,"bookmarks-count","bookmarks-count",-405810102)));
var can_compose_QMARK_ = cljs.core.boolean$(cljs.core.seq(cljs.core.some((function (p1__42039_SHARP_){
if(cljs.core.not(new cljs.core.Keyword(null,"draft","draft",1421831058).cljs$core$IFn$_invoke$arity$1(p1__42039_SHARP_))){
return oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(p1__42039_SHARP_),"create","POST"], 0));
} else {
return false;
}
}),new cljs.core.Keyword(null,"boards","boards",1912049694).cljs$core$IFn$_invoke$arity$1(org_data))));
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(org_data,new cljs.core.Keyword(null,"read-only","read-only",-191706886),oc.web.utils.activity.readonly_org_QMARK_(new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data))),new cljs.core.Keyword(null,"boards","boards",1912049694),fixed_boards),new cljs.core.Keyword(null,"author?","author?",-1083349935),oc.web.utils.activity.is_author_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([org_data], 0))),new cljs.core.Keyword(null,"member?","member?",486668360),oc.web.lib.jwt.user_is_part_of_the_team(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(org_data))),new cljs.core.Keyword(null,"drafts-count","drafts-count",-1710494641),oc.web.utils.org.disappearing_count_value(previous_org_drafts_count,new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(drafts_link))),new cljs.core.Keyword(null,"bookmarks-count","bookmarks-count",-405810102),oc.web.utils.org.disappearing_count_value(previous_bookmarks_count,new cljs.core.Keyword(null,"bookmarks-count","bookmarks-count",-405810102).cljs$core$IFn$_invoke$arity$1(org_data))),new cljs.core.Keyword(null,"unfollowing-count","unfollowing-count",-2050794903),oc.web.utils.org.disappearing_count_value(previous_bookmarks_count,new cljs.core.Keyword(null,"unfollowing-count","unfollowing-count",-2050794903).cljs$core$IFn$_invoke$arity$1(org_data))),new cljs.core.Keyword(null,"can-compose?","can-compose?",-1069735052),can_compose_QMARK_);
} else {
return null;
}
});
/**
 * Parse board data coming from the API.
 */
oc.web.utils.activity.parse_board = (function oc$web$utils$activity$parse_board(var_args){
var G__42070 = arguments.length;
switch (G__42070) {
case 1:
return oc.web.utils.activity.parse_board.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.utils.activity.parse_board.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.utils.activity.parse_board.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 4:
return oc.web.utils.activity.parse_board.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
var args_arr__4757__auto__ = [];
var len__4736__auto___42457 = arguments.length;
var i__4737__auto___42458 = (0);
while(true){
if((i__4737__auto___42458 < len__4736__auto___42457)){
args_arr__4757__auto__.push((arguments[i__4737__auto___42458]));

var G__42459 = (i__4737__auto___42458 + (1));
i__4737__auto___42458 = G__42459;
continue;
} else {
}
break;
}

var argseq__4758__auto__ = (new cljs.core.IndexedSeq(args_arr__4757__auto__.slice((5)),(0),null));
return oc.web.utils.activity.parse_board.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]),argseq__4758__auto__);

}
});

(oc.web.utils.activity.parse_board.cljs$core$IFn$_invoke$arity$1 = (function (board_data){
return oc.web.utils.activity.parse_board(board_data,cljs.core.PersistentArrayMap.EMPTY,oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.follow_boards_list.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.recently_posted_sort);
}));

(oc.web.utils.activity.parse_board.cljs$core$IFn$_invoke$arity$2 = (function (board_data,change_data){
return oc.web.utils.activity.parse_board(board_data,change_data,oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.follow_boards_list.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.recently_posted_sort);
}));

(oc.web.utils.activity.parse_board.cljs$core$IFn$_invoke$arity$3 = (function (board_data,change_data,active_users){
return oc.web.utils.activity.parse_board(board_data,change_data,active_users,oc.web.dispatcher.follow_boards_list.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.recently_posted_sort);
}));

(oc.web.utils.activity.parse_board.cljs$core$IFn$_invoke$arity$4 = (function (board_data,change_data,active_users,follow_boards_list){
return oc.web.utils.activity.parse_board(board_data,change_data,active_users,follow_boards_list,oc.web.dispatcher.recently_posted_sort);
}));

(oc.web.utils.activity.parse_board.cljs$core$IFn$_invoke$arity$variadic = (function (board_data,change_data,active_users,follow_boards_list,sort_type,p__42093){
var vec__42094 = p__42093;
var direction = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42094,(0),null);
if(cljs.core.truth_(board_data)){
var all_boards = new cljs.core.Keyword(null,"boards","boards",1912049694).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0());
var boards_map = cljs.core.zipmap(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850),all_boards),all_boards);
var with_fixed_activities_STAR_ = cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (ret,item){
return cljs.core.assoc_in(ret,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(item)], null),oc.web.utils.activity.parse_entry.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([item,cljs.core.get.cljs$core$IFn$_invoke$arity$2(boards_map,new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(item)),change_data,active_users,new cljs.core.Keyword(null,"last-seen-at","last-seen-at",1929467667).cljs$core$IFn$_invoke$arity$1(board_data)], 0)));
}),board_data,new cljs.core.Keyword(null,"entries","entries",-86943161).cljs$core$IFn$_invoke$arity$1(board_data));
var with_fixed_activities = cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (ret,item){
if(cljs.core.contains_QMARK_(new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(ret),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(item))){
return ret;
} else {
var entry_board_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(boards_map,new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(item));
var full_entry = oc.web.utils.activity.parse_entry.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(item)),item], 0)),entry_board_data,change_data,active_users,new cljs.core.Keyword(null,"last-seen-at","last-seen-at",1929467667).cljs$core$IFn$_invoke$arity$1(board_data)], 0));
return cljs.core.assoc_in(ret,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(item)], null),full_entry);
}
}),with_fixed_activities_STAR_,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897).cljs$core$IFn$_invoke$arity$1(board_data));
var keep_link_rel = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(direction,new cljs.core.Keyword(null,"down","down",1565245570)))?"previous":"next");
var next_links = (cljs.core.truth_(direction)?cljs.core.vec(cljs.core.remove.cljs$core$IFn$_invoke$arity$2((function (p1__42054_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"rel","rel",1378823488).cljs$core$IFn$_invoke$arity$1(p1__42054_SHARP_),keep_link_rel);
}),new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(board_data))):null);
var link_to_move = (cljs.core.truth_(direction)?oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"old-links","old-links",340078773).cljs$core$IFn$_invoke$arity$1(board_data),keep_link_rel], 0)):null);
var fixed_next_links = (cljs.core.truth_(direction)?(cljs.core.truth_(link_to_move)?cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(next_links,link_to_move)):next_links):new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(board_data));
var items_list = ((cljs.core.contains_QMARK_(board_data,new cljs.core.Keyword(null,"entries","entries",-86943161)))?cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__42055_SHARP_){
return cljs.core.select_keys(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__42055_SHARP_,new cljs.core.Keyword(null,"resource-type","resource-type",1844262326),new cljs.core.Keyword(null,"entry","entry",505168823)),oc.web.utils.activity.preserved_keys);
}),new cljs.core.Keyword(null,"entries","entries",-86943161).cljs$core$IFn$_invoke$arity$1(board_data)):null);
var full_items_list = oc.web.utils.activity.merge_items_lists(items_list,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897).cljs$core$IFn$_invoke$arity$1(board_data),direction);
var grouped_items = (cljs.core.truth_(oc.web.utils.activity.show_separators_QMARK_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(board_data),sort_type))?oc.web.utils.activity.grouped_posts.cljs$core$IFn$_invoke$arity$2(full_items_list,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(with_fixed_activities)):full_items_list);
var with_open_close_items = oc.web.utils.activity.insert_open_close_item(grouped_items,(function (p1__42058_SHARP_,p2__42056_SHARP_,p3__42057_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"resource-type","resource-type",1844262326).cljs$core$IFn$_invoke$arity$1(p2__42056_SHARP_),new cljs.core.Keyword(null,"resource-type","resource-type",1844262326).cljs$core$IFn$_invoke$arity$1(p3__42057_SHARP_));
}));
var with_ending_item = oc.web.utils.activity.insert_ending_item(with_open_close_items,oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([fixed_next_links,"next"], 0)));
var follow_board_uuids = cljs.core.set(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),follow_boards_list));
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(with_fixed_activities,new cljs.core.Keyword(null,"resource-type","resource-type",1844262326),new cljs.core.Keyword(null,"board","board",-1907017633)),new cljs.core.Keyword(null,"read-only","read-only",-191706886),oc.web.utils.activity.readonly_board_QMARK_(new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(board_data))),new cljs.core.Keyword(null,"old-links","old-links",340078773),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"entries","entries",-86943161)], 0)),new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897),full_items_list),new cljs.core.Keyword(null,"items-to-render","items-to-render",660219762),with_ending_item),new cljs.core.Keyword(null,"following","following",-2049193617),cljs.core.boolean$((function (){var G__42097 = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(board_data);
return (follow_board_uuids.cljs$core$IFn$_invoke$arity$1 ? follow_board_uuids.cljs$core$IFn$_invoke$arity$1(G__42097) : follow_board_uuids.call(null,G__42097));
})()));
} else {
return null;
}
}));

/** @this {Function} */
(oc.web.utils.activity.parse_board.cljs$lang$applyTo = (function (seq42063){
var G__42064 = cljs.core.first(seq42063);
var seq42063__$1 = cljs.core.next(seq42063);
var G__42065 = cljs.core.first(seq42063__$1);
var seq42063__$2 = cljs.core.next(seq42063__$1);
var G__42066 = cljs.core.first(seq42063__$2);
var seq42063__$3 = cljs.core.next(seq42063__$2);
var G__42067 = cljs.core.first(seq42063__$3);
var seq42063__$4 = cljs.core.next(seq42063__$3);
var G__42068 = cljs.core.first(seq42063__$4);
var seq42063__$5 = cljs.core.next(seq42063__$4);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__42064,G__42065,G__42066,G__42067,G__42068,seq42063__$5);
}));

(oc.web.utils.activity.parse_board.cljs$lang$maxFixedArity = (5));

/**
 * Parse data coming from the API for a certain user's posts.
 */
oc.web.utils.activity.parse_contributions = (function oc$web$utils$activity$parse_contributions(var_args){
var G__42115 = arguments.length;
switch (G__42115) {
case 1:
return oc.web.utils.activity.parse_contributions.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.utils.activity.parse_contributions.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.utils.activity.parse_contributions.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 4:
return oc.web.utils.activity.parse_contributions.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
case 5:
return oc.web.utils.activity.parse_contributions.cljs$core$IFn$_invoke$arity$5((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]));

break;
default:
var args_arr__4757__auto__ = [];
var len__4736__auto___42461 = arguments.length;
var i__4737__auto___42462 = (0);
while(true){
if((i__4737__auto___42462 < len__4736__auto___42461)){
args_arr__4757__auto__.push((arguments[i__4737__auto___42462]));

var G__42463 = (i__4737__auto___42462 + (1));
i__4737__auto___42462 = G__42463;
continue;
} else {
}
break;
}

var argseq__4758__auto__ = (new cljs.core.IndexedSeq(args_arr__4757__auto__.slice((6)),(0),null));
return oc.web.utils.activity.parse_contributions.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]),(arguments[(5)]),argseq__4758__auto__);

}
});

(oc.web.utils.activity.parse_contributions.cljs$core$IFn$_invoke$arity$1 = (function (contributions_data){
return oc.web.utils.activity.parse_contributions(contributions_data,cljs.core.PersistentArrayMap.EMPTY,oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.follow_publishers_list.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.recently_posted_sort);
}));

(oc.web.utils.activity.parse_contributions.cljs$core$IFn$_invoke$arity$2 = (function (contributions_data,change_data){
return oc.web.utils.activity.parse_contributions(contributions_data,change_data,oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.follow_publishers_list.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.recently_posted_sort);
}));

(oc.web.utils.activity.parse_contributions.cljs$core$IFn$_invoke$arity$3 = (function (contributions_data,change_data,org_data){
return oc.web.utils.activity.parse_contributions(contributions_data,change_data,org_data,oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.follow_publishers_list.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.recently_posted_sort);
}));

(oc.web.utils.activity.parse_contributions.cljs$core$IFn$_invoke$arity$4 = (function (contributions_data,change_data,org_data,active_users){
return oc.web.utils.activity.parse_contributions(contributions_data,change_data,org_data,active_users,oc.web.dispatcher.follow_publishers_list.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.recently_posted_sort);
}));

(oc.web.utils.activity.parse_contributions.cljs$core$IFn$_invoke$arity$5 = (function (contributions_data,change_data,org_data,active_users,follow_publishers_list){
return oc.web.utils.activity.parse_contributions(contributions_data,change_data,org_data,active_users,follow_publishers_list,oc.web.dispatcher.recently_posted_sort);
}));

(oc.web.utils.activity.parse_contributions.cljs$core$IFn$_invoke$arity$variadic = (function (contributions_data,change_data,org_data,active_users,follow_publishers_list,sort_type,p__42116){
var vec__42117 = p__42116;
var direction = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42117,(0),null);
if(cljs.core.truth_(contributions_data)){
var all_boards = new cljs.core.Keyword(null,"boards","boards",1912049694).cljs$core$IFn$_invoke$arity$1(org_data);
var boards_map = cljs.core.zipmap(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850),all_boards),all_boards);
var with_fixed_activities_STAR_ = cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (ret,item){
var board_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(boards_map,new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(item));
var fixed_entry = oc.web.utils.activity.parse_entry.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([item,board_data,change_data,active_users,new cljs.core.Keyword(null,"last-seen-at","last-seen-at",1929467667).cljs$core$IFn$_invoke$arity$1(contributions_data)], 0));
return cljs.core.assoc_in(ret,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(item)], null),fixed_entry);
}),contributions_data,new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(contributions_data));
var with_fixed_activities = cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (ret,item){
if(cljs.core.contains_QMARK_(new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(ret),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(item))){
return ret;
} else {
var entry_board_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(boards_map,new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(item));
var full_entry = oc.web.utils.activity.parse_entry.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(item)),item], 0)),entry_board_data,change_data,active_users,new cljs.core.Keyword(null,"last-seen-at","last-seen-at",1929467667).cljs$core$IFn$_invoke$arity$1(contributions_data)], 0));
return cljs.core.assoc_in(ret,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(item)], null),full_entry);
}
}),with_fixed_activities_STAR_,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897).cljs$core$IFn$_invoke$arity$1(contributions_data));
var keep_link_rel = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(direction,new cljs.core.Keyword(null,"down","down",1565245570)))?"previous":"next");
var next_links = (cljs.core.truth_(direction)?cljs.core.vec(cljs.core.remove.cljs$core$IFn$_invoke$arity$2((function (p1__42100_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"rel","rel",1378823488).cljs$core$IFn$_invoke$arity$1(p1__42100_SHARP_),keep_link_rel);
}),new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(contributions_data))):null);
var link_to_move = (cljs.core.truth_(direction)?oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"old-links","old-links",340078773).cljs$core$IFn$_invoke$arity$1(contributions_data),keep_link_rel], 0)):null);
var fixed_next_links = (cljs.core.truth_(direction)?(cljs.core.truth_(link_to_move)?cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(next_links,link_to_move)):next_links):new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(contributions_data));
var items_list = ((cljs.core.contains_QMARK_(contributions_data,new cljs.core.Keyword(null,"items","items",1031954938)))?cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__42102_SHARP_){
return cljs.core.select_keys(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__42102_SHARP_,new cljs.core.Keyword(null,"resource-type","resource-type",1844262326),new cljs.core.Keyword(null,"entry","entry",505168823)),oc.web.utils.activity.preserved_keys);
}),new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(contributions_data)):null);
var full_items_list = oc.web.utils.activity.merge_items_lists(items_list,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897).cljs$core$IFn$_invoke$arity$1(contributions_data),direction);
var grouped_items = (cljs.core.truth_(oc.web.utils.activity.show_separators_QMARK_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"contributions","contributions",-1280485964),sort_type))?oc.web.utils.activity.grouped_posts.cljs$core$IFn$_invoke$arity$2(full_items_list,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(with_fixed_activities)):full_items_list);
var next_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([fixed_next_links,"next"], 0));
var with_open_close_items = oc.web.utils.activity.insert_open_close_item(grouped_items,(function (p1__42105_SHARP_,p2__42103_SHARP_,p3__42104_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"resource-type","resource-type",1844262326).cljs$core$IFn$_invoke$arity$1(p2__42103_SHARP_),new cljs.core.Keyword(null,"resource-type","resource-type",1844262326).cljs$core$IFn$_invoke$arity$1(p3__42104_SHARP_));
}));
var with_ending_item = oc.web.utils.activity.insert_ending_item(with_open_close_items,next_link);
var follow_publishers_ids = cljs.core.set(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291),follow_publishers_list));
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(with_fixed_activities,new cljs.core.Keyword(null,"resource-type","resource-type",1844262326),new cljs.core.Keyword(null,"contributions","contributions",-1280485964)),new cljs.core.Keyword(null,"old-links","old-links",340078773),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"items","items",1031954938)], 0)),new cljs.core.Keyword(null,"links","links",-654507394),fixed_next_links),new cljs.core.Keyword(null,"self?","self?",-701815921),oc.web.utils.activity.is_author_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([contributions_data], 0))),new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897),full_items_list),new cljs.core.Keyword(null,"items-to-render","items-to-render",660219762),with_ending_item),new cljs.core.Keyword(null,"following","following",-2049193617),cljs.core.boolean$((function (){var G__42143 = new cljs.core.Keyword(null,"author-uuid","author-uuid",371566491).cljs$core$IFn$_invoke$arity$1(contributions_data);
return (follow_publishers_ids.cljs$core$IFn$_invoke$arity$1 ? follow_publishers_ids.cljs$core$IFn$_invoke$arity$1(G__42143) : follow_publishers_ids.call(null,G__42143));
})()));
} else {
return null;
}
}));

/** @this {Function} */
(oc.web.utils.activity.parse_contributions.cljs$lang$applyTo = (function (seq42108){
var G__42109 = cljs.core.first(seq42108);
var seq42108__$1 = cljs.core.next(seq42108);
var G__42110 = cljs.core.first(seq42108__$1);
var seq42108__$2 = cljs.core.next(seq42108__$1);
var G__42111 = cljs.core.first(seq42108__$2);
var seq42108__$3 = cljs.core.next(seq42108__$2);
var G__42112 = cljs.core.first(seq42108__$3);
var seq42108__$4 = cljs.core.next(seq42108__$3);
var G__42113 = cljs.core.first(seq42108__$4);
var seq42108__$5 = cljs.core.next(seq42108__$4);
var G__42114 = cljs.core.first(seq42108__$5);
var seq42108__$6 = cljs.core.next(seq42108__$5);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__42109,G__42110,G__42111,G__42112,G__42113,G__42114,seq42108__$6);
}));

(oc.web.utils.activity.parse_contributions.cljs$lang$maxFixedArity = (6));

/**
 * Parse container data coming from the API, like Following or Replies (AP, Bookmarks etc).
 */
oc.web.utils.activity.parse_container = (function oc$web$utils$activity$parse_container(var_args){
var G__42175 = arguments.length;
switch (G__42175) {
case 1:
return oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 4:
return oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
case 5:
return oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$5((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]));

break;
case 6:
return oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$6((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]),(arguments[(5)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$1 = (function (container_data){
return oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$6(container_data,cljs.core.PersistentArrayMap.EMPTY,oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0(),null);
}));

(oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$2 = (function (container_data,change_data){
return oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$6(container_data,change_data,oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0(),null);
}));

(oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$3 = (function (container_data,change_data,org_data){
return oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$6(container_data,change_data,org_data,oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0(),null);
}));

(oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$4 = (function (container_data,change_data,org_data,active_users){
return oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$6(container_data,change_data,org_data,active_users,oc.web.dispatcher.current_sort_type.cljs$core$IFn$_invoke$arity$0(),null);
}));

(oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$5 = (function (container_data,change_data,org_data,active_users,sort_type){
return oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$6(container_data,change_data,org_data,active_users,sort_type,null);
}));

(oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$6 = (function (container_data,change_data,org_data,active_users,sort_type,p__42179){
var map__42180 = p__42179;
var map__42180__$1 = (((((!((map__42180 == null))))?(((((map__42180.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42180.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42180):map__42180);
var options = map__42180__$1;
var direction = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42180__$1,new cljs.core.Keyword(null,"direction","direction",-633359395));
var load_comments_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42180__$1,new cljs.core.Keyword(null,"load-comments?","load-comments?",-594683459));
var keep_caught_up_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42180__$1,new cljs.core.Keyword(null,"keep-caught-up?","keep-caught-up?",-223227745));
if(cljs.core.truth_(container_data)){
var all_boards = new cljs.core.Keyword(null,"boards","boards",1912049694).cljs$core$IFn$_invoke$arity$1(org_data);
var boards_map = cljs.core.zipmap(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850),all_boards),all_boards);
var with_fixed_activities_STAR_ = cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (ret,item){
var board_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(boards_map,new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(item));
var fixed_entry = oc.web.utils.activity.parse_entry.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([item,board_data,change_data,active_users,new cljs.core.Keyword(null,"last-seen-at","last-seen-at",1929467667).cljs$core$IFn$_invoke$arity$1(container_data)], 0));
return cljs.core.assoc_in(ret,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(item)], null),fixed_entry);
}),container_data,new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(container_data));
var with_fixed_activities = cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (ret,item){
if(cljs.core.contains_QMARK_(new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(ret),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(item))){
return ret;
} else {
var entry_board_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(boards_map,new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(item));
var app_state_entry = oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(item));
if(cljs.core.truth_(app_state_entry)){
var parsed_entry = oc.web.utils.activity.parse_entry.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([app_state_entry,item], 0)),entry_board_data,change_data,active_users,new cljs.core.Keyword(null,"last-seen-at","last-seen-at",1929467667).cljs$core$IFn$_invoke$arity$1(container_data)], 0));
return cljs.core.assoc_in(ret,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(item)], null),parsed_entry);
} else {
return null;
}
}
}),with_fixed_activities_STAR_,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897).cljs$core$IFn$_invoke$arity$1(container_data));
var keep_link_rel = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(direction,new cljs.core.Keyword(null,"down","down",1565245570)))?"previous":"next");
var next_links = (cljs.core.truth_(direction)?cljs.core.vec(cljs.core.remove.cljs$core$IFn$_invoke$arity$2((function (p1__42161_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"rel","rel",1378823488).cljs$core$IFn$_invoke$arity$1(p1__42161_SHARP_),keep_link_rel);
}),new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(container_data))):null);
var link_to_move = (cljs.core.truth_(direction)?oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"old-links","old-links",340078773).cljs$core$IFn$_invoke$arity$1(container_data),keep_link_rel], 0)):null);
var fixed_next_links = (cljs.core.truth_(direction)?(cljs.core.truth_(link_to_move)?cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(next_links,link_to_move)):next_links):new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(container_data));
var replies_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"container-slug","container-slug",365736492).cljs$core$IFn$_invoke$arity$1(container_data)),new cljs.core.Keyword(null,"replies","replies",-1389888974));
var items_list = ((cljs.core.contains_QMARK_(container_data,new cljs.core.Keyword(null,"items","items",1031954938)))?cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__42162_SHARP_){
return cljs.core.select_keys(cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__42162_SHARP_,new cljs.core.Keyword(null,"resource-type","resource-type",1844262326),new cljs.core.Keyword(null,"entry","entry",505168823)),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(with_fixed_activities,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__42162_SHARP_)], null))], 0)),oc.web.utils.activity.preserved_keys);
}),new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(container_data)):null);
var items_list_STAR_ = oc.web.utils.activity.merge_items_lists(items_list,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897).cljs$core$IFn$_invoke$arity$1(container_data),direction);
var full_items_list = ((replies_QMARK_)?cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (p1__42163_SHARP_){
return oc.web.utils.activity.entry_replies_data(p1__42163_SHARP_,org_data,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(with_fixed_activities),new cljs.core.Keyword(null,"last-seen-at","last-seen-at",1929467667).cljs$core$IFn$_invoke$arity$1(container_data));
}),items_list_STAR_):items_list_STAR_);
var grouped_items = (cljs.core.truth_(oc.web.utils.activity.show_separators_QMARK_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"container-slug","container-slug",365736492).cljs$core$IFn$_invoke$arity$1(container_data),sort_type))?oc.web.utils.activity.grouped_posts.cljs$core$IFn$_invoke$arity$2(full_items_list,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(with_fixed_activities)):full_items_list);
var next_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([fixed_next_links,"next"], 0));
var check_item_fn = (cljs.core.truth_((function (){var G__42191 = new cljs.core.Keyword(null,"container-slug","container-slug",365736492).cljs$core$IFn$_invoke$arity$1(container_data);
var fexpr__42190 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"following","following",-2049193617),null,new cljs.core.Keyword(null,"unfollowing","unfollowing",-1076165830),null], null), null);
return (fexpr__42190.cljs$core$IFn$_invoke$arity$1 ? fexpr__42190.cljs$core$IFn$_invoke$arity$1(G__42191) : fexpr__42190.call(null,G__42191));
})())?(function (p1__42164_SHARP_){
var and__4115__auto__ = oc.web.utils.activity.entry_QMARK_(p1__42164_SHARP_);
if(cljs.core.truth_(and__4115__auto__)){
return ((cljs.core.not(new cljs.core.Keyword(null,"unseen","unseen",1063275592).cljs$core$IFn$_invoke$arity$1(p1__42164_SHARP_))) && (((cljs.core.not(new cljs.core.Keyword(null,"publisher?","publisher?",30448149).cljs$core$IFn$_invoke$arity$1(p1__42164_SHARP_))) || ((!((cljs.core.compare(new cljs.core.Keyword(null,"published-at","published-at",249684621).cljs$core$IFn$_invoke$arity$1(p1__42164_SHARP_),new cljs.core.Keyword(null,"last-seen-at","last-seen-at",1929467667).cljs$core$IFn$_invoke$arity$1(container_data)) > (0))))))));
} else {
return and__4115__auto__;
}
}):(function (p1__42165_SHARP_){
var and__4115__auto__ = oc.web.utils.activity.entry_QMARK_(p1__42165_SHARP_);
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(new cljs.core.Keyword(null,"unseen-comments","unseen-comments",-793262869).cljs$core$IFn$_invoke$arity$1(p1__42165_SHARP_));
} else {
return and__4115__auto__;
}
}));
var ignore_item_fn = ((replies_QMARK_)?(function (p1__42166_SHARP_){
var or__4126__auto__ = cljs.core.not(oc.web.utils.activity.entry_QMARK_(p1__42166_SHARP_));
if(or__4126__auto__){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"ignore-comments","ignore-comments",1137086177).cljs$core$IFn$_invoke$arity$1(p1__42166_SHARP_);
}
}):(function (p1__42167_SHARP_){
return cljs.core.not(oc.web.utils.activity.entry_QMARK_(p1__42167_SHARP_));
}));
var opts = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"has-next","has-next",683185232),next_link,new cljs.core.Keyword(null,"hide-bottom-line","hide-bottom-line",407605427),true], null);
var caught_up_item = (cljs.core.truth_((function (){var and__4115__auto__ = keep_caught_up_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.seq(new cljs.core.Keyword(null,"items-to-render","items-to-render",660219762).cljs$core$IFn$_invoke$arity$1(container_data));
} else {
return and__4115__auto__;
}
})())?cljs.core.some((function (p1__42168_SHARP_){
if(cljs.core.truth_(oc.web.utils.activity.resource_type_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__42168_SHARP_,new cljs.core.Keyword(null,"caught-up","caught-up",-1338440788)], 0)))){
return p1__42168_SHARP_;
} else {
return null;
}
}),new cljs.core.Keyword(null,"items-to-render","items-to-render",660219762).cljs$core$IFn$_invoke$arity$1(container_data)):null);
var caught_up_index = (cljs.core.truth_(caught_up_item)?oc.web.lib.utils.index_of(cljs.core.vec(new cljs.core.Keyword(null,"items-to-render","items-to-render",660219762).cljs$core$IFn$_invoke$arity$1(container_data)),(function (p1__42169_SHARP_){
return oc.web.utils.activity.resource_type_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__42169_SHARP_,new cljs.core.Keyword(null,"caught-up","caught-up",-1338440788)], 0));
})):null);
var with_caught_up = ((typeof caught_up_index === 'number')?(function (){var vec__42194 = cljs.core.split_at(caught_up_index,cljs.core.vec(grouped_items));
var before = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42194,(0),null);
var after = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42194,(1),null);
return cljs.core.vec(cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,cljs.core.concat.cljs$core$IFn$_invoke$arity$variadic(before,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [caught_up_item], null),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([after], 0))));
})():(cljs.core.truth_((function (){var G__42198 = new cljs.core.Keyword(null,"container-slug","container-slug",365736492).cljs$core$IFn$_invoke$arity$1(container_data);
var fexpr__42197 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"following","following",-2049193617),null,new cljs.core.Keyword(null,"replies","replies",-1389888974),null], null), null);
return (fexpr__42197.cljs$core$IFn$_invoke$arity$1 ? fexpr__42197.cljs$core$IFn$_invoke$arity$1(G__42198) : fexpr__42197.call(null,G__42198));
})())?oc.web.utils.activity.insert_caught_up.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"container-slug","container-slug",365736492).cljs$core$IFn$_invoke$arity$1(container_data),grouped_items,check_item_fn,ignore_item_fn,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([opts], 0)):grouped_items
));
var with_open_close_items = oc.web.utils.activity.insert_open_close_item(with_caught_up,(function (p1__42172_SHARP_,p2__42170_SHARP_,p3__42171_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"resource-type","resource-type",1844262326).cljs$core$IFn$_invoke$arity$1(p2__42170_SHARP_),new cljs.core.Keyword(null,"resource-type","resource-type",1844262326).cljs$core$IFn$_invoke$arity$1(p3__42171_SHARP_));
}));
var with_ending_item = oc.web.utils.activity.insert_ending_item(with_open_close_items,next_link);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(with_fixed_activities,new cljs.core.Keyword(null,"resource-type","resource-type",1844262326),new cljs.core.Keyword(null,"container","container",-1736937707)),new cljs.core.Keyword(null,"old-links","old-links",340078773),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"items","items",1031954938)], 0)),new cljs.core.Keyword(null,"links","links",-654507394),fixed_next_links),new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897),full_items_list),new cljs.core.Keyword(null,"items-to-render","items-to-render",660219762),with_ending_item);
} else {
return null;
}
}));

(oc.web.utils.activity.parse_container.cljs$lang$maxFixedArity = 6);

oc.web.utils.activity.activity_comments = (function oc$web$utils$activity$activity_comments(activity_data,comments_data){
var or__4126__auto__ = new cljs.core.Keyword(null,"sorted-comments","sorted-comments",1988882718).cljs$core$IFn$_invoke$arity$1(cljs.core.get.cljs$core$IFn$_invoke$arity$2(comments_data,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data)));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"created-at","created-at",-89248644),new cljs.core.Keyword(null,"comments","comments",-293346423).cljs$core$IFn$_invoke$arity$1(activity_data));
}
});
/**
 * Given a DOM element return true if it's actually visible in the viewport.
 */
oc.web.utils.activity.is_element_visible_QMARK_ = (function oc$web$utils$activity$is_element_visible_QMARK_(el){
var rect = el.getBoundingClientRect();
var zero_pos_QMARK_ = (function (p1__42203_SHARP_){
return (((p1__42203_SHARP_ === (0))) || ((p1__42203_SHARP_ > (0))));
});
var doc_element = document.documentElement;
var win_height = (function (){var or__4126__auto__ = doc_element.clientHeight;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return window.innerHeight;
}
})();
return (((((rect.top >= oc.web.lib.responsive.navbar_height)) && ((rect.top < win_height)))) || ((((rect.bottom <= win_height)) && ((rect.bottom > oc.web.lib.responsive.navbar_height)))));
});
/**
 * Given a list of items we want to request the who reads count
 * and the current read data, filter out the ids we already have data.
 */
oc.web.utils.activity.clean_who_reads_count_ids = (function oc$web$utils$activity$clean_who_reads_count_ids(item_ids,activities_read_data){
var all_items = cljs.core.set(cljs.core.keys(activities_read_data));
var request_set = cljs.core.set(item_ids);
var diff_ids = clojure.set.difference.cljs$core$IFn$_invoke$arity$2(request_set,all_items);
return cljs.core.vec(diff_ids);
});
oc.web.utils.activity.last_used_section = (function oc$web$utils$activity$last_used_section(){
var temp__5735__auto__ = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
if(cljs.core.truth_(temp__5735__auto__)){
var org_slug = temp__5735__auto__;
var cookie_name = oc.web.router.last_used_board_slug_cookie(org_slug);
return oc.web.lib.cookies.get_cookie(cookie_name);
} else {
return null;
}
});
oc.web.utils.activity.save_last_used_section = (function oc$web$utils$activity$save_last_used_section(section_slug){
var org_slug = oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0();
var last_board_cookie = oc.web.router.last_used_board_slug_cookie(org_slug);
if(cljs.core.truth_(section_slug)){
return oc.web.lib.cookies.set_cookie_BANG_.cljs$core$IFn$_invoke$arity$3(last_board_cookie,section_slug,((((60) * (60)) * (24)) * (365)));
} else {
return oc.web.lib.cookies.remove_cookie_BANG_.cljs$core$IFn$_invoke$arity$1(last_board_cookie);
}
});
oc.web.utils.activity.iso_format = (cljs_time.format.formatters.cljs$core$IFn$_invoke$arity$1 ? cljs_time.format.formatters.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"date-time","date-time",177938180)) : cljs_time.format.formatters.call(null,new cljs.core.Keyword(null,"date-time","date-time",177938180)));
oc.web.utils.activity.date_format = cljs_time.format.formatter.cljs$core$IFn$_invoke$arity$1("MMMM d");
oc.web.utils.activity.date_format_year = cljs_time.format.formatter.cljs$core$IFn$_invoke$arity$1("MMMM d YYYY");
oc.web.utils.activity.post_date = (function oc$web$utils$activity$post_date(var_args){
var args__4742__auto__ = [];
var len__4736__auto___42465 = arguments.length;
var i__4737__auto___42466 = (0);
while(true){
if((i__4737__auto___42466 < len__4736__auto___42465)){
args__4742__auto__.push((arguments[i__4737__auto___42466]));

var G__42467 = (i__4737__auto___42466 + (1));
i__4737__auto___42466 = G__42467;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.utils.activity.post_date.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.utils.activity.post_date.cljs$core$IFn$_invoke$arity$variadic = (function (timestamp,p__42224){
var vec__42225 = p__42224;
var force_year = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42225,(0),null);
var d = cljs_time.format.parse.cljs$core$IFn$_invoke$arity$2(oc.web.utils.activity.iso_format,timestamp);
var now_year = oc.web.lib.utils.js_date().getFullYear();
var timestamp_year = oc.web.lib.utils.js_date.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([timestamp], 0)).getFullYear();
var show_year = (function (){var or__4126__auto__ = force_year;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(now_year,timestamp_year);
}
})();
var f = (cljs.core.truth_(show_year)?oc.web.utils.activity.date_format_year:oc.web.utils.activity.date_format);
return cljs_time.format.unparse(f,d);
}));

(oc.web.utils.activity.post_date.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.utils.activity.post_date.cljs$lang$applyTo = (function (seq42221){
var G__42222 = cljs.core.first(seq42221);
var seq42221__$1 = cljs.core.next(seq42221);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__42222,seq42221__$1);
}));

oc.web.utils.activity.update_contributions = (function oc$web$utils$activity$update_contributions(db,org_data,change_data,active_users,follow_publishers_list){
var org_slug = new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data);
var contributions_list_key = oc.web.dispatcher.contributions_list_key(org_slug);
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (tdb,contrib_key){
var rp_contrib_data_key = oc.web.dispatcher.contributions_data_key.cljs$core$IFn$_invoke$arity$3(org_slug,contrib_key,oc.web.dispatcher.recently_posted_sort);
var ra_contrib_data_key = oc.web.dispatcher.contributions_data_key.cljs$core$IFn$_invoke$arity$3(org_slug,contrib_key,oc.web.dispatcher.recent_activity_sort);
var tdb_STAR_ = tdb;
var tdb_STAR___$1 = ((cljs.core.contains_QMARK_(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(tdb_STAR_,cljs.core.butlast(rp_contrib_data_key)),cljs.core.last(rp_contrib_data_key)))?cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(tdb_STAR_,rp_contrib_data_key,(function (p1__42231_SHARP_){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(oc.web.utils.activity.parse_contributions(p1__42231_SHARP_,change_data,org_data,active_users,follow_publishers_list,oc.web.dispatcher.recently_posted_sort),new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159));
})):tdb_STAR_);
if(cljs.core.contains_QMARK_(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(tdb_STAR___$1,cljs.core.butlast(ra_contrib_data_key)),cljs.core.last(ra_contrib_data_key))){
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(tdb_STAR___$1,ra_contrib_data_key,(function (p1__42232_SHARP_){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(oc.web.utils.activity.parse_contributions(p1__42232_SHARP_,change_data,org_data,active_users,follow_publishers_list,oc.web.dispatcher.recent_activity_sort),new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159));
}));
} else {
return tdb_STAR___$1;
}
}),db,cljs.core.keys(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,contributions_list_key)));
});
oc.web.utils.activity.update_boards = (function oc$web$utils$activity$update_boards(db,org_data,change_data,active_users){
var org_slug = new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data);
var boards_key = oc.web.dispatcher.boards_key(org_slug);
var following_boards = oc.web.dispatcher.follow_boards_list.cljs$core$IFn$_invoke$arity$2(org_slug,db);
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (tdb,board_key){
var rp_board_data_key = oc.web.dispatcher.board_data_key.cljs$core$IFn$_invoke$arity$3(org_slug,board_key,oc.web.dispatcher.recently_posted_sort);
var ra_board_data_key = oc.web.dispatcher.board_data_key.cljs$core$IFn$_invoke$arity$3(org_slug,board_key,oc.web.dispatcher.recent_activity_sort);
var tdb_STAR_ = tdb;
var tdb_STAR___$1 = ((cljs.core.contains_QMARK_(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(tdb_STAR_,cljs.core.butlast(rp_board_data_key)),cljs.core.last(rp_board_data_key)))?cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(tdb_STAR_,rp_board_data_key,(function (p1__42245_SHARP_){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(oc.web.utils.activity.parse_board(p1__42245_SHARP_,change_data,active_users,following_boards,oc.web.dispatcher.recently_posted_sort),new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159));
})):tdb_STAR_);
if(cljs.core.contains_QMARK_(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(tdb_STAR___$1,cljs.core.butlast(ra_board_data_key)),cljs.core.last(ra_board_data_key))){
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(tdb_STAR___$1,ra_board_data_key,(function (p1__42247_SHARP_){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(oc.web.utils.activity.parse_board(p1__42247_SHARP_,change_data,active_users,following_boards,oc.web.dispatcher.recent_activity_sort),new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159));
}));
} else {
return tdb_STAR___$1;
}
}),db,cljs.core.keys(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,boards_key)));
});
oc.web.utils.activity.update_container = (function oc$web$utils$activity$update_container(var_args){
var G__42256 = arguments.length;
switch (G__42256) {
case 5:
return oc.web.utils.activity.update_container.cljs$core$IFn$_invoke$arity$5((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]));

break;
case 6:
return oc.web.utils.activity.update_container.cljs$core$IFn$_invoke$arity$6((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]),(arguments[(5)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.utils.activity.update_container.cljs$core$IFn$_invoke$arity$5 = (function (db,container_slug,org_data,change_data,active_users){
return oc.web.utils.activity.update_container.cljs$core$IFn$_invoke$arity$6(db,container_slug,org_data,change_data,active_users,false);
}));

(oc.web.utils.activity.update_container.cljs$core$IFn$_invoke$arity$6 = (function (db,container_slug,org_data,change_data,active_users,keep_caught_up_QMARK_){
var org_slug = new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data);
var rp_container_data_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,container_slug,oc.web.dispatcher.recently_posted_sort);
var ra_container_data_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,container_slug,oc.web.dispatcher.recent_activity_sort);
var tdb = db;
var tdb__$1 = ((cljs.core.contains_QMARK_(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(tdb,cljs.core.butlast(rp_container_data_key)),cljs.core.last(rp_container_data_key)))?cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(tdb,rp_container_data_key,(function (p1__42252_SHARP_){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$6(p1__42252_SHARP_,change_data,org_data,active_users,oc.web.dispatcher.recently_posted_sort,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"keep-caught-up?","keep-caught-up?",-223227745),keep_caught_up_QMARK_], null)),new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159));
})):tdb);
if(cljs.core.contains_QMARK_(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(tdb__$1,cljs.core.butlast(ra_container_data_key)),cljs.core.last(ra_container_data_key))){
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(tdb__$1,ra_container_data_key,(function (p1__42253_SHARP_){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$6(p1__42253_SHARP_,change_data,org_data,active_users,oc.web.dispatcher.recent_activity_sort,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"keep-caught-up?","keep-caught-up?",-223227745),keep_caught_up_QMARK_], null)),new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159));
}));
} else {
return tdb__$1;
}
}));

(oc.web.utils.activity.update_container.cljs$lang$maxFixedArity = 6);

oc.web.utils.activity.update_replies_container = (function oc$web$utils$activity$update_replies_container(var_args){
var G__42261 = arguments.length;
switch (G__42261) {
case 4:
return oc.web.utils.activity.update_replies_container.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
case 5:
return oc.web.utils.activity.update_replies_container.cljs$core$IFn$_invoke$arity$5((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.web.utils.activity.update_replies_container.cljs$core$IFn$_invoke$arity$4 = (function (db,org_data,change_data,active_users){
return oc.web.utils.activity.update_replies_container.cljs$core$IFn$_invoke$arity$5(db,org_data,change_data,active_users,false);
}));

(oc.web.utils.activity.update_replies_container.cljs$core$IFn$_invoke$arity$5 = (function (db,org_data,change_data,active_users,keep_caught_up_QMARK_){
return oc.web.utils.activity.update_container.cljs$core$IFn$_invoke$arity$6(db,new cljs.core.Keyword(null,"replies","replies",-1389888974),org_data,change_data,active_users,keep_caught_up_QMARK_);
}));

(oc.web.utils.activity.update_replies_container.cljs$lang$maxFixedArity = 5);

oc.web.utils.activity.update_replies_comments = (function oc$web$utils$activity$update_replies_comments(db,org_data,change_data,active_users){
return oc.web.utils.activity.update_replies_container.cljs$core$IFn$_invoke$arity$5(db,org_data,change_data,active_users,true);
});
oc.web.utils.activity.update_containers = (function oc$web$utils$activity$update_containers(db,org_data,change_data,active_users){
var org_slug = new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data);
var containers_key = oc.web.dispatcher.containers_key(org_slug);
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (p1__42262_SHARP_,p2__42263_SHARP_){
return oc.web.utils.activity.update_container.cljs$core$IFn$_invoke$arity$5(p1__42262_SHARP_,p2__42263_SHARP_,org_data,change_data,active_users);
}),db,cljs.core.keys(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,containers_key)));
});
oc.web.utils.activity.update_posts = (function oc$web$utils$activity$update_posts(db,org_data,change_data,active_users){
var org_slug = new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data);
var posts_key = oc.web.dispatcher.posts_data_key(org_slug);
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (tdb,post_uuid){
var post_data_key = cljs.core.concat.cljs$core$IFn$_invoke$arity$2(posts_key,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [post_uuid], null));
var old_post_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(tdb,post_data_key);
var board_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(tdb,oc.web.dispatcher.board_data_key.cljs$core$IFn$_invoke$arity$2(org_slug,new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(old_post_data)));
return cljs.core.assoc_in(tdb,post_data_key,oc.web.utils.activity.parse_entry.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([old_post_data,board_data,change_data,active_users], 0)));
}),db,cljs.core.keys(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,posts_key)));
});
oc.web.utils.activity.update_all_containers = (function oc$web$utils$activity$update_all_containers(db,org_data,change_data,active_users,follow_publishers_list){
return oc.web.utils.activity.update_contributions(oc.web.utils.activity.update_containers(oc.web.utils.activity.update_boards(oc.web.utils.activity.update_posts(db,org_data,change_data,active_users),org_data,change_data,active_users),org_data,change_data,active_users),org_data,change_data,active_users,follow_publishers_list);
});

//# sourceMappingURL=oc.web.utils.activity.js.map
