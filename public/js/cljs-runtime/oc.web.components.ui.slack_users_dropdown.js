goog.provide('oc.web.components.ui.slack_users_dropdown');
oc.web.components.ui.slack_users_dropdown.check_user = (function oc$web$components$ui$slack_users_dropdown$check_user(user,s){
var or__4126__auto__ = cuerdas.core.includes_QMARK_(cuerdas.core.lower(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(user)),s);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = cuerdas.core.includes_QMARK_(cuerdas.core.lower(new cljs.core.Keyword(null,"first-name","first-name",-1559982131).cljs$core$IFn$_invoke$arity$1(user)),s);
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
var or__4126__auto____$2 = cuerdas.core.includes_QMARK_(cuerdas.core.lower(new cljs.core.Keyword(null,"last-name","last-name",-1695738974).cljs$core$IFn$_invoke$arity$1(user)),s);
if(cljs.core.truth_(or__4126__auto____$2)){
return or__4126__auto____$2;
} else {
return cuerdas.core.includes_QMARK_(cuerdas.core.lower(new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(user)),s);
}
}
}
});
oc.web.components.ui.slack_users_dropdown.filter_users = (function oc$web$components$ui$slack_users_dropdown$filter_users(users,s){
var look_for = cuerdas.core.lower(s);
return cljs.core.filterv((function (p1__46771_SHARP_){
return oc.web.components.ui.slack_users_dropdown.check_user(p1__46771_SHARP_,look_for);
}),users);
});
/**
 * Read the roster users, group them by slack org and sort by slack org and name/email.
 * Save the result in the local component state to be used during render.
 */
oc.web.components.ui.slack_users_dropdown.setup_sorted_users = (function oc$web$components$ui$slack_users_dropdown$setup_sorted_users(s){
var roster_data = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"team-roster","team-roster",-1945092859)));
var all_users = cljs.core.filterv((function (p1__46772_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(p1__46772_SHARP_),"uninvited");
}),new cljs.core.Keyword(null,"users","users",-713552705).cljs$core$IFn$_invoke$arity$1(roster_data));
var team_roster = cljs.core.vals(cljs.core.group_by(new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561),all_users));
var sorted_team_roster = cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (team){
return cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2((function (p1__46773_SHARP_){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"first-name","first-name",-1559982131).cljs$core$IFn$_invoke$arity$1(p1__46773_SHARP_))," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"last-name","last-name",-1695738974).cljs$core$IFn$_invoke$arity$1(p1__46773_SHARP_))].join('');
}),team);
}),team_roster);
var all_sorted_users = cljs.core.vec(cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.concat,sorted_team_roster));
if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.slack-users-dropdown","all-sorted-users","oc.web.components.ui.slack-users-dropdown/all-sorted-users",-552868166).cljs$core$IFn$_invoke$arity$1(s)),all_sorted_users)){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-users-dropdown","all-sorted-users","oc.web.components.ui.slack-users-dropdown/all-sorted-users",-552868166).cljs$core$IFn$_invoke$arity$1(s),all_sorted_users);
} else {
return null;
}
});
/**
 * Filter ::all-sorted-users on the fly with the passed filter-fn if present.
 */
oc.web.components.ui.slack_users_dropdown.get_filtered_sorted_users = (function oc$web$components$ui$slack_users_dropdown$get_filtered_sorted_users(s){
var filter_fn = new cljs.core.Keyword(null,"filter-fn","filter-fn",1689475675).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s)));
var all_sorted_users = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.slack-users-dropdown","all-sorted-users","oc.web.components.ui.slack-users-dropdown/all-sorted-users",-552868166).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.fn_QMARK_(filter_fn)){
return cljs.core.filter.cljs$core$IFn$_invoke$arity$2(filter_fn,all_sorted_users);
} else {
return all_sorted_users;
}
});
oc.web.components.ui.slack_users_dropdown.slack_users_dropdown = rum.core.build_defcs((function (s,p__46786){
var map__46788 = p__46786;
var map__46788__$1 = (((((!((map__46788 == null))))?(((((map__46788.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46788.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46788):map__46788);
var data = map__46788__$1;
var disabled = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46788__$1,new cljs.core.Keyword(null,"disabled","disabled",-1529784218));
var initial_value = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46788__$1,new cljs.core.Keyword(null,"initial-value","initial-value",470619381));
var on_change = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46788__$1,new cljs.core.Keyword(null,"on-change","on-change",-732046149));
var on_intermediate_change = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46788__$1,new cljs.core.Keyword(null,"on-intermediate-change","on-intermediate-change",-1144231725));
var on_focus = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46788__$1,new cljs.core.Keyword(null,"on-focus","on-focus",-13737624));
var on_blur = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46788__$1,new cljs.core.Keyword(null,"on-blur","on-blur",814300747));
var filter_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46788__$1,new cljs.core.Keyword(null,"filter-fn","filter-fn",1689475675));
var _ = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"team-roster","team-roster",-1945092859));
var all_sorted_users = oc.web.components.ui.slack_users_dropdown.get_filtered_sorted_users(s);
var slack_orgs = new cljs.core.Keyword(null,"slack-orgs","slack-orgs",-1806634042).cljs$core$IFn$_invoke$arity$1(org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"team-data","team-data",-732020079)));
var slack_orgs_map = cljs.core.zipmap(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561),slack_orgs),cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"name","name",1843675177),slack_orgs));
return React.createElement("div",({"key": ["slack-users-dropdown-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.count(all_sorted_users))].join(''), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["slack-users-dropdown",(cljs.core.truth_(disabled)?"disabled":"")], null))}),sablono.interpreter.create_element("input",({"value": cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.slack-users-dropdown","slack-user","oc.web.components.ui.slack-users-dropdown/slack-user",1966358437).cljs$core$IFn$_invoke$arity$1(s)), "onFocus": (function (){
if(cljs.core.fn_QMARK_(on_focus)){
(on_focus.cljs$core$IFn$_invoke$arity$0 ? on_focus.cljs$core$IFn$_invoke$arity$0() : on_focus.call(null));
} else {
}

return oc.web.lib.utils.after((100),(function (){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-users-dropdown","typing","oc.web.components.ui.slack-users-dropdown/typing",-1218200547).cljs$core$IFn$_invoke$arity$1(s),false);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-users-dropdown","show-users-dropdown","oc.web.components.ui.slack-users-dropdown/show-users-dropdown",1449284986).cljs$core$IFn$_invoke$arity$1(s),true);
}));
}), "onBlur": (function (){
if(cljs.core.fn_QMARK_(on_blur)){
return (on_blur.cljs$core$IFn$_invoke$arity$0 ? on_blur.cljs$core$IFn$_invoke$arity$0() : on_blur.call(null));
} else {
return null;
}
}), "onChange": (function (p1__46783_SHARP_){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-users-dropdown","typing","oc.web.components.ui.slack-users-dropdown/typing",-1218200547).cljs$core$IFn$_invoke$arity$1(s),true);

if(cljs.core.fn_QMARK_(on_intermediate_change)){
var G__46792_46806 = p1__46783_SHARP_.target.value;
(on_intermediate_change.cljs$core$IFn$_invoke$arity$1 ? on_intermediate_change.cljs$core$IFn$_invoke$arity$1(G__46792_46806) : on_intermediate_change.call(null,G__46792_46806));
} else {
}

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-users-dropdown","slack-user","oc.web.components.ui.slack-users-dropdown/slack-user",1966358437).cljs$core$IFn$_invoke$arity$1(s),p1__46783_SHARP_.target.value);
}), "disabled": disabled, "placeholder": (((cljs.core.count(all_sorted_users) > (0)))?"Select a person to invite...":"No more members to add"), "className": "slack-users-dropdown oc-input"})),sablono.interpreter.interpret((cljs.core.truth_(disabled)?null:new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.arrows","div.arrows",-450482963),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (p1__46784_SHARP_){
if(cljs.core.truth_(disabled)){
return null;
} else {
var next_value_46807 = cljs.core.not(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.slack-users-dropdown","show-users-dropdown","oc.web.components.ui.slack-users-dropdown/show-users-dropdown",1449284986).cljs$core$IFn$_invoke$arity$1(s)));
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-users-dropdown","typing","oc.web.components.ui.slack-users-dropdown/typing",-1218200547).cljs$core$IFn$_invoke$arity$1(s),false);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-users-dropdown","show-users-dropdown","oc.web.components.ui.slack-users-dropdown/show-users-dropdown",1449284986).cljs$core$IFn$_invoke$arity$1(s),next_value_46807);

if(((next_value_46807) && (cljs.core.fn_QMARK_(on_focus)))){
(on_focus.cljs$core$IFn$_invoke$arity$0 ? on_focus.cljs$core$IFn$_invoke$arity$0() : on_focus.call(null));
} else {
}

if((((!(next_value_46807))) && (cljs.core.fn_QMARK_(on_blur)))){
(on_blur.cljs$core$IFn$_invoke$arity$0 ? on_blur.cljs$core$IFn$_invoke$arity$0() : on_blur.call(null));
} else {
}

return oc.web.lib.utils.event_stop(p1__46784_SHARP_);
}
})], null)], null))),sablono.interpreter.interpret((cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.slack-users-dropdown","show-users-dropdown","oc.web.components.ui.slack-users-dropdown/show-users-dropdown",1449284986).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.slack-users-dropdown-list","div.slack-users-dropdown-list",-1992100300),(function (){var iter__4529__auto__ = (function oc$web$components$ui$slack_users_dropdown$iter__46797(s__46798){
return (new cljs.core.LazySeq(null,(function (){
var s__46798__$1 = s__46798;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__46798__$1);
if(temp__5735__auto__){
var s__46798__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__46798__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__46798__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__46800 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__46799 = (0);
while(true){
if((i__46799 < size__4528__auto__)){
var user = cljs.core._nth(c__4527__auto__,i__46799);
cljs.core.chunk_append(b__46800,new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user.group","div.user.group",1771798688),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"key","key",-1516042587),["slack-users-dd-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(user)),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slack-id","slack-id",862141985).cljs$core$IFn$_invoke$arity$1(user))].join(''),new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (i__46799,user,c__4527__auto__,size__4528__auto__,b__46800,s__46798__$2,temp__5735__auto__,_,all_sorted_users,slack_orgs,slack_orgs_map,map__46788,map__46788__$1,data,disabled,initial_value,on_change,on_intermediate_change,on_focus,on_blur,filter_fn){
return (function (){
(on_change.cljs$core$IFn$_invoke$arity$1 ? on_change.cljs$core$IFn$_invoke$arity$1(user) : on_change.call(null,user));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-users-dropdown","slack-user","oc.web.components.ui.slack-users-dropdown/slack-user",1966358437).cljs$core$IFn$_invoke$arity$1(s),oc.lib.user.name_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user], 0)));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-users-dropdown","show-users-dropdown","oc.web.components.ui.slack-users-dropdown/show-users-dropdown",1449284986).cljs$core$IFn$_invoke$arity$1(s),false);

if(cljs.core.fn_QMARK_(on_blur)){
(on_blur.cljs$core$IFn$_invoke$arity$0 ? on_blur.cljs$core$IFn$_invoke$arity$0() : on_blur.call(null));
} else {
}

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-users-dropdown","typing","oc.web.components.ui.slack-users-dropdown/typing",-1218200547).cljs$core$IFn$_invoke$arity$1(s),false);
});})(i__46799,user,c__4527__auto__,size__4528__auto__,b__46800,s__46798__$2,temp__5735__auto__,_,all_sorted_users,slack_orgs,slack_orgs_map,map__46788,map__46788__$1,data,disabled,initial_value,on_change,on_intermediate_change,on_focus,on_blur,filter_fn))
], null),(oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1(user) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,user)),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-name","div.user-name",298595493),oc.lib.user.name_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user], 0)),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.slack-org","span.slack-org",-165139283),cljs.core.get.cljs$core$IFn$_invoke$arity$2(slack_orgs_map,new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(user))], null)], null)], null));

var G__46808 = (i__46799 + (1));
i__46799 = G__46808;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__46800),oc$web$components$ui$slack_users_dropdown$iter__46797(cljs.core.chunk_rest(s__46798__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__46800),null);
}
} else {
var user = cljs.core.first(s__46798__$2);
return cljs.core.cons(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user.group","div.user.group",1771798688),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"key","key",-1516042587),["slack-users-dd-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(user)),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slack-id","slack-id",862141985).cljs$core$IFn$_invoke$arity$1(user))].join(''),new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (user,s__46798__$2,temp__5735__auto__,_,all_sorted_users,slack_orgs,slack_orgs_map,map__46788,map__46788__$1,data,disabled,initial_value,on_change,on_intermediate_change,on_focus,on_blur,filter_fn){
return (function (){
(on_change.cljs$core$IFn$_invoke$arity$1 ? on_change.cljs$core$IFn$_invoke$arity$1(user) : on_change.call(null,user));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-users-dropdown","slack-user","oc.web.components.ui.slack-users-dropdown/slack-user",1966358437).cljs$core$IFn$_invoke$arity$1(s),oc.lib.user.name_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user], 0)));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-users-dropdown","show-users-dropdown","oc.web.components.ui.slack-users-dropdown/show-users-dropdown",1449284986).cljs$core$IFn$_invoke$arity$1(s),false);

if(cljs.core.fn_QMARK_(on_blur)){
(on_blur.cljs$core$IFn$_invoke$arity$0 ? on_blur.cljs$core$IFn$_invoke$arity$0() : on_blur.call(null));
} else {
}

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-users-dropdown","typing","oc.web.components.ui.slack-users-dropdown/typing",-1218200547).cljs$core$IFn$_invoke$arity$1(s),false);
});})(user,s__46798__$2,temp__5735__auto__,_,all_sorted_users,slack_orgs,slack_orgs_map,map__46788,map__46788__$1,data,disabled,initial_value,on_change,on_intermediate_change,on_focus,on_blur,filter_fn))
], null),(oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1(user) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,user)),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.user-name","div.user-name",298595493),oc.lib.user.name_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user], 0)),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.slack-org","span.slack-org",-165139283),cljs.core.get.cljs$core$IFn$_invoke$arity$2(slack_orgs_map,new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(user))], null)], null)], null),oc$web$components$ui$slack_users_dropdown$iter__46797(cljs.core.rest(s__46798__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__((cljs.core.truth_((function (){var and__4115__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.slack-users-dropdown","typing","oc.web.components.ui.slack-users-dropdown/typing",-1218200547).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.slack-users-dropdown","slack-user","oc.web.components.ui.slack-users-dropdown/slack-user",1966358437).cljs$core$IFn$_invoke$arity$1(s));
} else {
return and__4115__auto__;
}
})())?oc.web.components.ui.slack_users_dropdown.filter_users(all_sorted_users,cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.slack-users-dropdown","slack-user","oc.web.components.ui.slack-users-dropdown/slack-user",1966358437).cljs$core$IFn$_invoke$arity$1(s))):all_sorted_users));
})()], null):null)));
}),new cljs.core.PersistentVector(null, 10, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.ui.slack-users-dropdown","show-users-dropdown","oc.web.components.ui.slack-users-dropdown/show-users-dropdown",1449284986)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.ui.slack-users-dropdown","field-value","oc.web.components.ui.slack-users-dropdown/field-value",-572190982)),rum.core.local.cljs$core$IFn$_invoke$arity$2("",new cljs.core.Keyword("oc.web.components.ui.slack-users-dropdown","slack-user","oc.web.components.ui.slack-users-dropdown/slack-user",1966358437)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.slack-users-dropdown","typing","oc.web.components.ui.slack-users-dropdown/typing",-1218200547)),rum.core.local.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentVector.EMPTY,new cljs.core.Keyword("oc.web.components.ui.slack-users-dropdown","all-sorted-users","oc.web.components.ui.slack-users-dropdown/all-sorted-users",-552868166)),rum.core.reactive,oc.web.mixins.ui.on_window_click_mixin((function (s,e){
if(cljs.core.truth_((function (){var and__4115__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.slack-users-dropdown","show-users-dropdown","oc.web.components.ui.slack-users-dropdown/show-users-dropdown",1449284986).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(oc.web.lib.utils.event_inside_QMARK_(e,document.querySelector("input.slack-users-dropdown").parentElement));
} else {
return and__4115__auto__;
}
})())){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-users-dropdown","show-users-dropdown","oc.web.components.ui.slack-users-dropdown/show-users-dropdown",1449284986).cljs$core$IFn$_invoke$arity$1(s),false);

var map__46802 = cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s));
var map__46802__$1 = (((((!((map__46802 == null))))?(((((map__46802.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46802.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46802):map__46802);
var on_blur = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46802__$1,new cljs.core.Keyword(null,"on-blur","on-blur",814300747));
if(cljs.core.fn_QMARK_(on_blur)){
return (on_blur.cljs$core$IFn$_invoke$arity$0 ? on_blur.cljs$core$IFn$_invoke$arity$0() : on_blur.call(null));
} else {
return null;
}
} else {
return null;
}
})),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"team-data","team-data",-732020079)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"team-roster","team-roster",-1945092859)], 0)),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
var initial_value_46809 = new cljs.core.Keyword(null,"initial-value","initial-value",470619381).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s)));
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-users-dropdown","slack-user","oc.web.components.ui.slack-users-dropdown/slack-user",1966358437).cljs$core$IFn$_invoke$arity$1(s),(function (){var or__4126__auto__ = initial_value_46809;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "";
}
})());

oc.web.components.ui.slack_users_dropdown.setup_sorted_users(s);

return s;
}),new cljs.core.Keyword(null,"will-update","will-update",328062998),(function (s){
oc.web.components.ui.slack_users_dropdown.setup_sorted_users(s);

return s;
}),new cljs.core.Keyword(null,"before-render","before-render",71256781),(function (s){
oc.web.actions.team.teams_get_if_needed();

return s;
})], null)], null),"slack-users-dropdown");

//# sourceMappingURL=oc.web.components.ui.slack_users_dropdown.js.map
