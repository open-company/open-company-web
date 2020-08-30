goog.provide('oc.web.components.ui.follow_picker');
oc.web.components.ui.follow_picker.is_user_QMARK_ = (function oc$web$components$ui$follow_picker$is_user_QMARK_(item){
return oc.web.utils.activity.resource_type_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([item,new cljs.core.Keyword(null,"user","user",1532431356)], 0));
});
oc.web.components.ui.follow_picker.sort_items = (function oc$web$components$ui$follow_picker$sort_items(items){
return cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2((function (p1__46111_SHARP_){
if(cljs.core.truth_(oc.web.utils.activity.board_QMARK_(p1__46111_SHARP_))){
return new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(p1__46111_SHARP_);
} else {
return new cljs.core.Keyword(null,"short-name","short-name",-1767085022).cljs$core$IFn$_invoke$arity$1(p1__46111_SHARP_);
}
}),items);
});
oc.web.components.ui.follow_picker.search_string = (function oc$web$components$ui$follow_picker$search_string(v,q){
return cuerdas.core.includes_QMARK_(cuerdas.core.lower(v),q);
});
oc.web.components.ui.follow_picker.search_item = (function oc$web$components$ui$follow_picker$search_item(item,q){
var or__4126__auto__ = oc.web.components.ui.follow_picker.search_string(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(item),q);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = oc.web.components.ui.follow_picker.search_string(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(item),q);
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
var or__4126__auto____$2 = oc.web.components.ui.follow_picker.search_string(new cljs.core.Keyword(null,"first-name","first-name",-1559982131).cljs$core$IFn$_invoke$arity$1(item),q);
if(cljs.core.truth_(or__4126__auto____$2)){
return or__4126__auto____$2;
} else {
var or__4126__auto____$3 = oc.web.components.ui.follow_picker.search_string(new cljs.core.Keyword(null,"last-name","last-name",-1695738974).cljs$core$IFn$_invoke$arity$1(item),q);
if(cljs.core.truth_(or__4126__auto____$3)){
return or__4126__auto____$3;
} else {
var or__4126__auto____$4 = oc.web.components.ui.follow_picker.search_string(new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(item),q);
if(cljs.core.truth_(or__4126__auto____$4)){
return or__4126__auto____$4;
} else {
var or__4126__auto____$5 = oc.web.components.ui.follow_picker.search_string(new cljs.core.Keyword(null,"title","title",636505583).cljs$core$IFn$_invoke$arity$1(item),q);
if(cljs.core.truth_(or__4126__auto____$5)){
return or__4126__auto____$5;
} else {
return oc.web.components.ui.follow_picker.search_string(new cljs.core.Keyword(null,"location","location",1815599388).cljs$core$IFn$_invoke$arity$1(item),q);
}
}
}
}
}
}
});
oc.web.components.ui.follow_picker.filter_item = (function oc$web$components$ui$follow_picker$filter_item(s,current_user_id,item,q){
var and__4115__auto__ = (function (){var or__4126__auto__ = (function (){var and__4115__auto__ = oc.web.components.ui.follow_picker.is_user_QMARK_(item);
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(current_user_id,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(item));
} else {
return and__4115__auto__;
}
})();
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var and__4115__auto__ = oc.web.utils.activity.board_QMARK_(item);
if(cljs.core.truth_(and__4115__auto__)){
return ((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(item),oc.web.lib.utils.default_drafts_board_slug)) && (cljs.core.not(new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803).cljs$core$IFn$_invoke$arity$1(item))));
} else {
return and__4115__auto__;
}
}
})();
if(cljs.core.truth_(and__4115__auto__)){
var or__4126__auto__ = cljs.core.not(cljs.core.seq(q));
if(or__4126__auto__){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = oc.web.components.ui.follow_picker.search_item(item,q);
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
var or__4126__auto____$2 = cljs.core.some(cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.components.ui.follow_picker.search_item,item),cuerdas.core.split.cljs$core$IFn$_invoke$arity$2(q,/\s/));
if(cljs.core.truth_(or__4126__auto____$2)){
return or__4126__auto____$2;
} else {
var or__4126__auto____$3 = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(q,"follow"))?new cljs.core.Keyword(null,"follow","follow",-809317662).cljs$core$IFn$_invoke$arity$1(item):false);
if(cljs.core.truth_(or__4126__auto____$3)){
return or__4126__auto____$3;
} else {
return ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(q,"unfollow")) && (cljs.core.not(new cljs.core.Keyword(null,"follow","follow",-809317662).cljs$core$IFn$_invoke$arity$1(item))));
}
}
}
}
} else {
return and__4115__auto__;
}
});
oc.web.components.ui.follow_picker.filter_sort_items = (function oc$web$components$ui$follow_picker$filter_sort_items(s,current_user_id,items,q){
return oc.web.components.ui.follow_picker.sort_items(cljs.core.filterv((function (p1__46152_SHARP_){
return oc.web.components.ui.follow_picker.filter_item(s,current_user_id,p1__46152_SHARP_,cuerdas.core.lower(q));
}),items));
});
oc.web.components.ui.follow_picker.empty_user_component = rum.core.build_defc((function (p__46159){
var map__46160 = p__46159;
var map__46160__$1 = (((((!((map__46160 == null))))?(((((map__46160.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46160.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46160):map__46160);
var org_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46160__$1,new cljs.core.Keyword(null,"org-data","org-data",96720321));
var current_user_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46160__$1,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915));
return React.createElement("div",({"className": "follow-picker-empty-users"}),React.createElement("div",({"className": "invite-users-box"}),React.createElement("div",({"className": "invite-users-box-inner group"}),React.createElement("div",({"className": "invite-users-title"}),"Invite your team to join you!"),sablono.interpreter.interpret((function (){var G__46183 = new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"rows-num","rows-num",-1183466189),(3),new cljs.core.Keyword(null,"hide-user-role","hide-user-role",2103996678),true,new cljs.core.Keyword(null,"save-title","save-title",-2115503227),"Send invites",new cljs.core.Keyword(null,"saving-title","saving-title",-465556280),"Sending invites",new cljs.core.Keyword(null,"saved-title","saved-title",-1142856092),"Invites sent!"], null);
return (oc.web.components.ui.invite_email.invite_email.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.invite_email.invite_email.cljs$core$IFn$_invoke$arity$1(G__46183) : oc.web.components.ui.invite_email.invite_email.call(null,G__46183));
})()),React.createElement("div",({"className": "invite-users-footer"}),React.createElement("span",({"className": "invite-user-or"}),"Or, "),React.createElement("button",({"onClick": (function (){
return oc.web.actions.nav_sidebar.show_org_settings(new cljs.core.Keyword(null,"invite-email","invite-email",1375794598));
}), "className": "mlb-reset invite-link-bt"}),"generate an invite link to share")))),React.createElement("div",({"className": "follow-picker-empty-header"}),"People (1)"),(function (){var attrs46167 = (oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1(current_user_data) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,current_user_data));
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46167))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["follow-picker-empty-self-user"], null)], null),attrs46167], 0))):({"className": "follow-picker-empty-self-user"})),((cljs.core.map_QMARK_(attrs46167))?new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [(function (){var attrs46168 = [cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(current_user_data))," (you)"].join('');
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"span",((cljs.core.map_QMARK_(attrs46168))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["user-name"], null)], null),attrs46168], 0))):({"className": "user-name"})),((cljs.core.map_QMARK_(attrs46168))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46168)], null)));
})(),(function (){var attrs46169 = new cljs.core.Keyword(null,"title","title",636505583).cljs$core$IFn$_invoke$arity$1(current_user_data);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"span",((cljs.core.map_QMARK_(attrs46169))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["user-role"], null)], null),attrs46169], 0))):({"className": "user-role"})),((cljs.core.map_QMARK_(attrs46169))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46169)], null)));
})(),React.createElement("span",({"className": "followers-count"})),React.createElement("button",({"onClick": (function (){
return oc.web.actions.nav_sidebar.show_user_settings(new cljs.core.Keyword(null,"profile","profile",-545963874));
}), "className": "mlb-reset edit-profile-bt"}),"Edit profile")], null):new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46167),(function (){var attrs46173 = [cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(current_user_data))," (you)"].join('');
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"span",((cljs.core.map_QMARK_(attrs46173))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["user-name"], null)], null),attrs46173], 0))):({"className": "user-name"})),((cljs.core.map_QMARK_(attrs46173))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46173)], null)));
})(),(function (){var attrs46174 = new cljs.core.Keyword(null,"title","title",636505583).cljs$core$IFn$_invoke$arity$1(current_user_data);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"span",((cljs.core.map_QMARK_(attrs46174))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["user-role"], null)], null),attrs46174], 0))):({"className": "user-role"})),((cljs.core.map_QMARK_(attrs46174))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46174)], null)));
})(),React.createElement("span",({"className": "followers-count"})),React.createElement("button",({"onClick": (function (){
return oc.web.actions.nav_sidebar.show_user_settings(new cljs.core.Keyword(null,"profile","profile",-545963874));
}), "className": "mlb-reset edit-profile-bt"}),"Edit profile")], null)));
})());
}),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$], null),"empty-user-component");
oc.web.components.ui.follow_picker.follow_picker_items = (function oc$web$components$ui$follow_picker$follow_picker_items(state,items,prefix){
var followers_boards_count = org.martinklepsch.derivatives.react(state,new cljs.core.Keyword(null,"followers-boards-count","followers-boards-count",334424133));
var followers_publishers_count = org.martinklepsch.derivatives.react(state,new cljs.core.Keyword(null,"followers-publishers-count","followers-publishers-count",-692976579));
var is_mobile_QMARK_ = oc.web.lib.responsive.is_mobile_size_QMARK_();
var iter__4529__auto__ = (function oc$web$components$ui$follow_picker$follow_picker_items_$_iter__46194(s__46195){
return (new cljs.core.LazySeq(null,(function (){
var s__46195__$1 = s__46195;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__46195__$1);
if(temp__5735__auto__){
var s__46195__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__46195__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__46195__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__46197 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__46196 = (0);
while(true){
if((i__46196 < size__4528__auto__)){
var i = cljs.core._nth(c__4527__auto__,i__46196);
var board_QMARK_ = oc.web.utils.activity.board_QMARK_(i);
cljs.core.chunk_append(b__46197,new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.follow-picker-item-row.group","div.follow-picker-item-row.group",-972635463),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"key","key",-1516042587),[cljs.core.str.cljs$core$IFn$_invoke$arity$1(prefix),"-picker-",cljs.core.str.cljs$core$IFn$_invoke$arity$1((cljs.core.truth_(board_QMARK_)?new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(i):new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(i)))].join(''),new cljs.core.Keyword(null,"class","class",-2030961996),(cljs.core.truth_(new cljs.core.Keyword(null,"follow","follow",-809317662).cljs$core$IFn$_invoke$arity$1(i))?"selected":null)], null),(cljs.core.truth_(board_QMARK_)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.follow-picker-board-item","div.follow-picker-board-item",-927135731),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(i)], null):new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.follow-picker-user-item","div.follow-picker-user-item",1932471435),(oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1(i) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,i)),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.user-name","span.user-name",-1373633834),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(i)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.user-role","span.user-role",-1447298029),new cljs.core.Keyword(null,"title","title",636505583).cljs$core$IFn$_invoke$arity$1(i)], null)], null)),(cljs.core.truth_((function (){var and__4115__auto__ = board_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(new cljs.core.Keyword(null,"read-only","read-only",-191706886).cljs$core$IFn$_invoke$arity$1(i));
} else {
return and__4115__auto__;
}
})())?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.board-settings-bt","button.mlb-reset.board-settings-bt",1230722968),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687),(cljs.core.truth_(is_mobile_QMARK_)?null:"tooltip"),new cljs.core.Keyword(null,"data-placement","data-placement",166529525),"top",new cljs.core.Keyword(null,"data-container","data-container",1473653353),"body",new cljs.core.Keyword(null,"title","title",636505583),[cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(i))," settings"].join(''),new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (i__46196,board_QMARK_,i,c__4527__auto__,size__4528__auto__,b__46197,s__46195__$2,temp__5735__auto__,followers_boards_count,followers_publishers_count,is_mobile_QMARK_){
return (function (){
return oc.web.actions.nav_sidebar.show_section_editor(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(i));
});})(i__46196,board_QMARK_,i,c__4527__auto__,size__4528__auto__,b__46197,s__46195__$2,temp__5735__auto__,followers_boards_count,followers_publishers_count,is_mobile_QMARK_))
], null)], null):null),(function (){var followers = (cljs.core.truth_(board_QMARK_)?cljs.core.get.cljs$core$IFn$_invoke$arity$2(followers_boards_count,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(i)):cljs.core.get.cljs$core$IFn$_invoke$arity$2(followers_publishers_count,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(i)));
var followers_count = new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(followers);
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.followers-count","span.followers-count",1137441189),(((followers_count > (0)))?[cljs.core.str.cljs$core$IFn$_invoke$arity$1(followers_count)," follower",((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(followers_count,(1)))?"s":null)].join(''):null)], null);
})(),(function (){var G__46210 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"following","following",-2049193617),new cljs.core.Keyword(null,"follow","follow",-809317662).cljs$core$IFn$_invoke$arity$1(i),new cljs.core.Keyword(null,"resource-type","resource-type",1844262326),new cljs.core.Keyword(null,"resource-type","resource-type",1844262326).cljs$core$IFn$_invoke$arity$1(i),new cljs.core.Keyword(null,"resource-uuid","resource-uuid",1798351789),(cljs.core.truth_(board_QMARK_)?new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(i):new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(i))], null);
return (oc.web.components.ui.follow_button.follow_button.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.follow_button.follow_button.cljs$core$IFn$_invoke$arity$1(G__46210) : oc.web.components.ui.follow_button.follow_button.call(null,G__46210));
})()], null));

var G__46249 = (i__46196 + (1));
i__46196 = G__46249;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__46197),oc$web$components$ui$follow_picker$follow_picker_items_$_iter__46194(cljs.core.chunk_rest(s__46195__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__46197),null);
}
} else {
var i = cljs.core.first(s__46195__$2);
var board_QMARK_ = oc.web.utils.activity.board_QMARK_(i);
return cljs.core.cons(new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.follow-picker-item-row.group","div.follow-picker-item-row.group",-972635463),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"key","key",-1516042587),[cljs.core.str.cljs$core$IFn$_invoke$arity$1(prefix),"-picker-",cljs.core.str.cljs$core$IFn$_invoke$arity$1((cljs.core.truth_(board_QMARK_)?new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(i):new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(i)))].join(''),new cljs.core.Keyword(null,"class","class",-2030961996),(cljs.core.truth_(new cljs.core.Keyword(null,"follow","follow",-809317662).cljs$core$IFn$_invoke$arity$1(i))?"selected":null)], null),(cljs.core.truth_(board_QMARK_)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.follow-picker-board-item","div.follow-picker-board-item",-927135731),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(i)], null):new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.follow-picker-user-item","div.follow-picker-user-item",1932471435),(oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1(i) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,i)),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.user-name","span.user-name",-1373633834),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(i)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.user-role","span.user-role",-1447298029),new cljs.core.Keyword(null,"title","title",636505583).cljs$core$IFn$_invoke$arity$1(i)], null)], null)),(cljs.core.truth_((function (){var and__4115__auto__ = board_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(new cljs.core.Keyword(null,"read-only","read-only",-191706886).cljs$core$IFn$_invoke$arity$1(i));
} else {
return and__4115__auto__;
}
})())?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.board-settings-bt","button.mlb-reset.board-settings-bt",1230722968),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687),(cljs.core.truth_(is_mobile_QMARK_)?null:"tooltip"),new cljs.core.Keyword(null,"data-placement","data-placement",166529525),"top",new cljs.core.Keyword(null,"data-container","data-container",1473653353),"body",new cljs.core.Keyword(null,"title","title",636505583),[cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(i))," settings"].join(''),new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (board_QMARK_,i,s__46195__$2,temp__5735__auto__,followers_boards_count,followers_publishers_count,is_mobile_QMARK_){
return (function (){
return oc.web.actions.nav_sidebar.show_section_editor(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(i));
});})(board_QMARK_,i,s__46195__$2,temp__5735__auto__,followers_boards_count,followers_publishers_count,is_mobile_QMARK_))
], null)], null):null),(function (){var followers = (cljs.core.truth_(board_QMARK_)?cljs.core.get.cljs$core$IFn$_invoke$arity$2(followers_boards_count,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(i)):cljs.core.get.cljs$core$IFn$_invoke$arity$2(followers_publishers_count,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(i)));
var followers_count = new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(followers);
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.followers-count","span.followers-count",1137441189),(((followers_count > (0)))?[cljs.core.str.cljs$core$IFn$_invoke$arity$1(followers_count)," follower",((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(followers_count,(1)))?"s":null)].join(''):null)], null);
})(),(function (){var G__46213 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"following","following",-2049193617),new cljs.core.Keyword(null,"follow","follow",-809317662).cljs$core$IFn$_invoke$arity$1(i),new cljs.core.Keyword(null,"resource-type","resource-type",1844262326),new cljs.core.Keyword(null,"resource-type","resource-type",1844262326).cljs$core$IFn$_invoke$arity$1(i),new cljs.core.Keyword(null,"resource-uuid","resource-uuid",1798351789),(cljs.core.truth_(board_QMARK_)?new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(i):new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(i))], null);
return (oc.web.components.ui.follow_button.follow_button.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.follow_button.follow_button.cljs$core$IFn$_invoke$arity$1(G__46213) : oc.web.components.ui.follow_button.follow_button.call(null,G__46213));
})()], null),oc$web$components$ui$follow_picker$follow_picker_items_$_iter__46194(cljs.core.rest(s__46195__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(items);
});
oc.web.components.ui.follow_picker.follow_picker = rum.core.build_defcs((function (s){
var org_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"org-data","org-data",96720321));
var current_user_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915));
var all_active_users = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"active-users","active-users",-329555191));
var follow_boards_list = cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"follow-boards-list","follow-boards-list",-461166530)));
var follow_publishers_list = cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291),org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"follow-publishers-list","follow-publishers-list",-374150342)));
var all_boards = cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__46214_SHARP_){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__46214_SHARP_,new cljs.core.Keyword(null,"resource-type","resource-type",1844262326),new cljs.core.Keyword(null,"board","board",-1907017633));
}),new cljs.core.Keyword(null,"boards","boards",1912049694).cljs$core$IFn$_invoke$arity$1(org_data));
var authors_uuids = cljs.core.set(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291),new cljs.core.Keyword(null,"authors","authors",2063018172).cljs$core$IFn$_invoke$arity$1(org_data)));
var all_authors = cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__46216_SHARP_){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__46216_SHARP_,new cljs.core.Keyword(null,"resource-type","resource-type",1844262326),new cljs.core.Keyword(null,"user","user",1532431356));
}),cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__46215_SHARP_){
var and__4115__auto__ = (function (){var G__46228 = new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__46215_SHARP_);
return (authors_uuids.cljs$core$IFn$_invoke$arity$1 ? authors_uuids.cljs$core$IFn$_invoke$arity$1(G__46228) : authors_uuids.call(null,G__46228));
})();
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(current_user_data),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__46215_SHARP_));
} else {
return and__4115__auto__;
}
}),cljs.core.vals(all_active_users)));
var all_items = (function (){var G__46229 = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.follow-picker","filter","oc.web.components.ui.follow-picker/filter",-809670618).cljs$core$IFn$_invoke$arity$1(s));
var G__46229__$1 = (((G__46229 instanceof cljs.core.Keyword))?G__46229.fqn:null);
switch (G__46229__$1) {
case "users":
return all_authors;

break;
case "boards":
return all_boards;

break;
default:
return cljs.core.concat.cljs$core$IFn$_invoke$arity$2(all_boards,all_authors);

}
})();
var with_follow = cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__46217_SHARP_){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__46217_SHARP_,new cljs.core.Keyword(null,"follow","follow",-809317662),(function (){var or__4126__auto__ = (function (){var and__4115__auto__ = oc.web.components.ui.follow_picker.is_user_QMARK_(p1__46217_SHARP_);
if(cljs.core.truth_(and__4115__auto__)){
return oc.web.lib.utils.in_QMARK_(follow_publishers_list,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__46217_SHARP_));
} else {
return and__4115__auto__;
}
})();
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var and__4115__auto__ = oc.web.utils.activity.board_QMARK_(p1__46217_SHARP_);
if(cljs.core.truth_(and__4115__auto__)){
return oc.web.lib.utils.in_QMARK_(follow_boards_list,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__46217_SHARP_));
} else {
return and__4115__auto__;
}
}
})());
}),all_items);
var sorted_items = oc.web.components.ui.follow_picker.filter_sort_items(s,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(current_user_data),with_follow,cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.follow-picker","query","oc.web.components.ui.follow-picker/query",-611587002).cljs$core$IFn$_invoke$arity$1(s)));
var following_items = cljs.core.filter.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"follow","follow",-809317662),sorted_items);
var unfollowing_items = cljs.core.filter.cljs$core$IFn$_invoke$arity$2(cljs.core.comp.cljs$core$IFn$_invoke$arity$2(cljs.core.not,new cljs.core.Keyword(null,"follow","follow",-809317662)),sorted_items);
var show_following_QMARK_ = ((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.count(all_authors),(1))) || (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.count(all_boards),(1))));
return React.createElement("div",({"className": "follow-picker"}),React.createElement("div",({"className": "follow-picker-modal"}),React.createElement("button",({"onClick": (function (){
return oc.web.actions.nav_sidebar.close_all_panels();
}), "className": "mlb-reset modal-close-bt"})),React.createElement("div",({"className": "follow-picker-header"}),React.createElement("button",({"onClick": (function (){
return oc.web.actions.nav_sidebar.show_section_add();
}), "className": "mlb-reset create-board-bt"}),"New topic"),React.createElement("h3",({"className": "follow-picker-title"}),"Personalize your Home feed")),(function (){var attrs46239 = (((!(show_following_QMARK_)))?new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.follow-picker-empty-items","div.follow-picker-empty-items",814098114),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.follow-picker-empty-icon","div.follow-picker-empty-icon",-565831868)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.follow-picker-empty-copy","div.follow-picker-empty-copy",1329126686),"There are no topics to follow yet. ",(cljs.core.truth_(oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(org_data),"create"], 0)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.follow-picker-empty-invite-bt","button.mlb-reset.follow-picker-empty-invite-bt",404712463),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.actions.nav_sidebar.show_org_settings(new cljs.core.Keyword(null,"invite-picker","invite-picker",1426151962));
})], null),"Add a topic to get started."], null):null)], null),(function (){var G__46243 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"org-data","org-data",96720321),org_data,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915),current_user_data], null);
return (oc.web.components.ui.follow_picker.empty_user_component.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.follow_picker.empty_user_component.cljs$core$IFn$_invoke$arity$1(G__46243) : oc.web.components.ui.follow_picker.empty_user_component.call(null,G__46243));
})()], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.follow-picker-body-inner.group","div.follow-picker-body-inner.group",593815222),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input.follow-picker-search-field-input.oc-input","input.follow-picker-search-field-input.oc-input",-1652721914),new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"value","value",305978217),cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.follow-picker","query","oc.web.components.ui.follow-picker/query",-611587002).cljs$core$IFn$_invoke$arity$1(s)),new cljs.core.Keyword(null,"type","type",1174270348),"text",new cljs.core.Keyword(null,"ref","ref",1289896967),new cljs.core.Keyword(null,"query","query",-1288509510),new cljs.core.Keyword(null,"class","class",-2030961996),cljs.core.name(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.follow-picker","filter","oc.web.components.ui.follow-picker/filter",-809670618).cljs$core$IFn$_invoke$arity$1(s))),new cljs.core.Keyword(null,"placeholder","placeholder",-104873083),(function (){var G__46244 = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.follow-picker","filter","oc.web.components.ui.follow-picker/filter",-809670618).cljs$core$IFn$_invoke$arity$1(s));
var G__46244__$1 = (((G__46244 instanceof cljs.core.Keyword))?G__46244.fqn:null);
switch (G__46244__$1) {
case "all":
return "Find a topic or person";

break;
case "users":
return "Find a person";

break;
case "boards":
return "Find a topic";

break;
default:
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__46244__$1)].join('')));

}
})(),new cljs.core.Keyword(null,"on-change","on-change",-732046149),(function (p1__46218_SHARP_){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.follow-picker","query","oc.web.components.ui.follow-picker/query",-611587002).cljs$core$IFn$_invoke$arity$1(s),p1__46218_SHARP_.target.value);
})], null)], null),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.follow-picker-items-list.group","div.follow-picker-items-list.group",-1759070673),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.follow-picker-row-header.group","div.follow-picker-row-header.group",-1790515166),((cljs.core.seq(following_items))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.follow-picker-row-header-left","div.follow-picker-row-header-left",1422805768),"Subscriptions"], null):null),null], null),oc.web.components.ui.follow_picker.follow_picker_items(s,following_items,"follow"),((cljs.core.seq(unfollowing_items))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.follow-picker-row-header","div.follow-picker-row-header",917324194),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.follow-picker-row-header-left.unfollow","div.follow-picker-row-header-left.unfollow",1545455186),"Suggestions"], null)], null):null),((cljs.core.seq(unfollowing_items))?oc.web.components.ui.follow_picker.follow_picker_items(s,unfollowing_items,"unfollow"):null)], null)], null));
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs46239))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["follow-picker-body"], null)], null),attrs46239], 0))):({"className": "follow-picker-body"})),((cljs.core.map_QMARK_(attrs46239))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs46239)], null)));
})()));
}),new cljs.core.PersistentVector(null, 15, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"org-data","org-data",96720321)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"active-users","active-users",-329555191)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"follow-boards-list","follow-boards-list",-461166530)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"follow-publishers-list","follow-publishers-list",-374150342)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"followers-boards-count","followers-boards-count",334424133)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"followers-publishers-count","followers-publishers-count",-692976579)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2("",new cljs.core.Keyword("oc.web.components.ui.follow-picker","query","oc.web.components.ui.follow-picker/query",-611587002)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.follow-picker","saving","oc.web.components.ui.follow-picker/saving",-1031238739)),rum.core.local.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"boards","boards",1912049694),new cljs.core.Keyword("oc.web.components.ui.follow-picker","filter","oc.web.components.ui.follow-picker/filter",-809670618)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.follow-picker","filter-open","oc.web.components.ui.follow-picker/filter-open",-1549456609)),oc.web.mixins.ui.strict_refresh_tooltips_mixin,oc.web.mixins.ui.on_window_click_mixin((function (s,e){
if(cljs.core.truth_((function (){var and__4115__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.follow-picker","filter-open","oc.web.components.ui.follow-picker/filter-open",-1549456609).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(oc.web.lib.utils.event_inside_QMARK_(e,rum.core.ref_node(s,new cljs.core.Keyword(null,"follow-filter-bt","follow-filter-bt",333198710))));
} else {
return and__4115__auto__;
}
})())){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.follow-picker","filter-open","oc.web.components.ui.follow-picker/filter-open",-1549456609).cljs$core$IFn$_invoke$arity$1(s),false);
} else {
return null;
}
})),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"init","init",-1875481434),(function (s){
oc.web.actions.user.load_follow_list();

oc.web.actions.user.load_followers_count();

return s;
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
oc.web.actions.user.refresh_follow_containers();

return s;
})], null)], null),"follow-picker");

//# sourceMappingURL=oc.web.components.ui.follow_picker.js.map
