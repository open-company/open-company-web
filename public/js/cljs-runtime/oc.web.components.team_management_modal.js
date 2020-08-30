goog.provide('oc.web.components.team_management_modal');
oc.web.components.team_management_modal.user_action = (function oc$web$components$team_management_modal$user_action(team_id,user,action,method,other_link_params,remove_cb){
$("[data-toggle=\"tooltip\"]").tooltip("hide");

return oc.web.actions.team.user_action.cljs$core$IFn$_invoke$arity$variadic(team_id,user,action,method,other_link_params,null,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([remove_cb], 0));
});
oc.web.components.team_management_modal.real_remove_fn = (function oc$web$components$team_management_modal$real_remove_fn(s,author,user,team_id,remove_cb){
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("oc.web.components.team-management-modal","removing","oc.web.components.team-management-modal/removing",1059780791).cljs$core$IFn$_invoke$arity$1(s),(function (p1__44808_SHARP_){
return cljs.core.set(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(p1__44808_SHARP_,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user)));
}));

if(cljs.core.truth_(author)){
oc.web.actions.team.remove_author(author);
} else {
}

return oc.web.components.team_management_modal.user_action(team_id,user,"remove","DELETE",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ref","ref",1289896967),"application/vnd.open-company.user.v1+json"], null),remove_cb);
});
oc.web.components.team_management_modal.alert_resend_done = (function oc$web$components$team_management_modal$alert_resend_done(){
return oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"title","title",636505583),"Invitation resent",new cljs.core.Keyword(null,"primary-bt-title","primary-bt-title",653140150),"OK",new cljs.core.Keyword(null,"primary-bt-dismiss","primary-bt-dismiss",-820688058),true,new cljs.core.Keyword(null,"expire","expire",-70657108),(3),new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"invitation-resent","invitation-resent",575319479)], null));
});
oc.web.components.team_management_modal.user_match = (function oc$web$components$team_management_modal$user_match(query,user){
var r = RegExp(["^.*(",cljs.core.str.cljs$core$IFn$_invoke$arity$1(query),").*$"].join(''),"i");
var or__4126__auto__ = (function (){var and__4115__auto__ = new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(user);
if(cljs.core.truth_(and__4115__auto__)){
return new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(user).match(r);
} else {
return and__4115__auto__;
}
})();
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = (function (){var and__4115__auto__ = new cljs.core.Keyword(null,"first-name","first-name",-1559982131).cljs$core$IFn$_invoke$arity$1(user);
if(cljs.core.truth_(and__4115__auto__)){
return new cljs.core.Keyword(null,"first-name","first-name",-1559982131).cljs$core$IFn$_invoke$arity$1(user).match(r);
} else {
return and__4115__auto__;
}
})();
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
var or__4126__auto____$2 = (function (){var and__4115__auto__ = new cljs.core.Keyword(null,"last-name","last-name",-1695738974).cljs$core$IFn$_invoke$arity$1(user);
if(cljs.core.truth_(and__4115__auto__)){
return new cljs.core.Keyword(null,"last-name","last-name",-1695738974).cljs$core$IFn$_invoke$arity$1(user).match(r);
} else {
return and__4115__auto__;
}
})();
if(cljs.core.truth_(or__4126__auto____$2)){
return or__4126__auto____$2;
} else {
var and__4115__auto__ = new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(user);
if(cljs.core.truth_(and__4115__auto__)){
return new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(user).match(r);
} else {
return and__4115__auto__;
}
}
}
}
});
oc.web.components.team_management_modal.team_management_modal = rum.core.build_defcs((function (s){
var org_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"org-data","org-data",96720321));
var invite_users_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"invite-data","invite-data",-758838050));
var team_data = new cljs.core.Keyword(null,"team-data","team-data",-732020079).cljs$core$IFn$_invoke$arity$1(invite_users_data);
var team_roster = new cljs.core.Keyword(null,"team-roster","team-roster",-1945092859).cljs$core$IFn$_invoke$arity$1(invite_users_data);
var cur_user_data = new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915).cljs$core$IFn$_invoke$arity$1(invite_users_data);
var org_authors = new cljs.core.Keyword(null,"authors","authors",2063018172).cljs$core$IFn$_invoke$arity$1(org_data);
var all_users = (cljs.core.truth_(team_data)?new cljs.core.Keyword(null,"users","users",-713552705).cljs$core$IFn$_invoke$arity$1(team_data):cljs.core.filter.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291),new cljs.core.Keyword(null,"users","users",-713552705).cljs$core$IFn$_invoke$arity$1(team_roster)));
var filtered_users = ((cljs.core.seq(cljs.core.deref(new cljs.core.Keyword("oc.web.components.team-management-modal","query","oc.web.components.team-management-modal/query",-1300152145).cljs$core$IFn$_invoke$arity$1(s))))?cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__44809_SHARP_){
return oc.web.components.team_management_modal.user_match(cljs.core.deref(new cljs.core.Keyword("oc.web.components.team-management-modal","query","oc.web.components.team-management-modal/query",-1300152145).cljs$core$IFn$_invoke$arity$1(s)),p1__44809_SHARP_);
}),all_users):all_users);
var splitted_users = cljs.core.group_by((function (p1__44810_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__44810_SHARP_),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(cur_user_data));
}),filtered_users);
var self_user = cljs.core.first(cljs.core.get.cljs$core$IFn$_invoke$arity$2(splitted_users,true));
var other_users = cljs.core.get.cljs$core$IFn$_invoke$arity$2(splitted_users,false);
var other_sorted_users = cljs.core.reverse(cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(oc.lib.user.name_for,other_users));
var sorted_users = (cljs.core.truth_(self_user)?cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [self_user], null),other_sorted_users):other_sorted_users);
var is_admin_or_author_QMARK_ = (function (){var G__44818 = new cljs.core.Keyword(null,"role","role",-736691072).cljs$core$IFn$_invoke$arity$1(cur_user_data);
var fexpr__44817 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"admin","admin",-1239101627),null,new cljs.core.Keyword(null,"author","author",2111686192),null], null), null);
return (fexpr__44817.cljs$core$IFn$_invoke$arity$1 ? fexpr__44817.cljs$core$IFn$_invoke$arity$1(G__44818) : fexpr__44817.call(null,G__44818));
})();
var is_admin_QMARK_ = oc.web.lib.jwt.is_admin_QMARK_(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(org_data));
return React.createElement("div",({"className": "team-management-modal"}),React.createElement("button",({"onClick": oc.web.actions.nav_sidebar.close_all_panels, "className": "mlb-reset modal-close-bt"})),React.createElement("div",({"className": "team-management"}),React.createElement("div",({"className": "team-management-header"}),(function (){var attrs44819 = (cljs.core.truth_(is_admin_QMARK_)?"Manage team":"View team");
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs44819))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["team-management-header-title"], null)], null),attrs44819], 0))):({"className": "team-management-header-title"})),((cljs.core.map_QMARK_(attrs44819))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs44819)], null)));
})(),sablono.interpreter.interpret((cljs.core.truth_(is_admin_or_author_QMARK_)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.save-bt","button.mlb-reset.save-bt",1152501214),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (){
return oc.web.actions.nav_sidebar.show_org_settings(new cljs.core.Keyword(null,"invite-picker","invite-picker",1426151962));
})], null),"Invite"], null):null)),React.createElement("button",({"onClick": (function (){
return oc.web.actions.nav_sidebar.show_org_settings(null);
}), "className": "mlb-reset cancel-bt"}),"Back")),React.createElement("div",({"className": "team-management-body"}),(function (){var attrs44820 = [cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.count(all_users))," member",(((cljs.core.count(all_users) > (1)))?"s":null)].join('');
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs44820))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["team-management-body-title"], null)], null),attrs44820], 0))):({"className": "team-management-body-title"})),((cljs.core.map_QMARK_(attrs44820))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs44820)], null)));
})(),React.createElement("div",({"className": "team-management-search-users"}),sablono.interpreter.create_element("input",({"value": cljs.core.deref(new cljs.core.Keyword("oc.web.components.team-management-modal","query","oc.web.components.team-management-modal/query",-1300152145).cljs$core$IFn$_invoke$arity$1(s)), "placeholder": "Search by name...", "onChange": (function (p1__44811_SHARP_){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.team-management-modal","query","oc.web.components.team-management-modal/query",-1300152145).cljs$core$IFn$_invoke$arity$1(s),p1__44811_SHARP_.target.value);
}), "className": "org-settings-team-search-field oc-input"}))),React.createElement("div",({"className": "team-management-users-list"}),cljs.core.into_array.cljs$core$IFn$_invoke$arity$1((function (){var iter__4529__auto__ = (function oc$web$components$team_management_modal$iter__44821(s__44822){
return (new cljs.core.LazySeq(null,(function (){
var s__44822__$1 = s__44822;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__44822__$1);
if(temp__5735__auto__){
var s__44822__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__44822__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__44822__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__44824 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__44823 = (0);
while(true){
if((i__44823 < size__4528__auto__)){
var user = cljs.core._nth(c__4527__auto__,i__44823);
var user_type = new cljs.core.Keyword(null,"role","role",-736691072).cljs$core$IFn$_invoke$arity$1(user);
var author = cljs.core.some(((function (i__44823,user_type,user,c__4527__auto__,size__4528__auto__,b__44824,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_){
return (function (p1__44812_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__44812_SHARP_),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user))){
return p1__44812_SHARP_;
} else {
return null;
}
});})(i__44823,user_type,user,c__4527__auto__,size__4528__auto__,b__44824,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_))
,org_authors);
var pending_QMARK_ = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2("pending",new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(user))) && (((cljs.core.contains_QMARK_(user,new cljs.core.Keyword(null,"email","email",1415816706))) || (cljs.core.contains_QMARK_(user,new cljs.core.Keyword(null,"slack-id","slack-id",862141985))))));
var current_user = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(cur_user_data));
var display_name = oc.lib.user.name_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user], 0));
var removing_QMARK_ = (function (){var G__44826 = new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user);
var fexpr__44825 = cljs.core.deref(new cljs.core.Keyword("oc.web.components.team-management-modal","removing","oc.web.components.team-management-modal/removing",1059780791).cljs$core$IFn$_invoke$arity$1(s));
return (fexpr__44825.cljs$core$IFn$_invoke$arity$1 ? fexpr__44825.cljs$core$IFn$_invoke$arity$1(G__44826) : fexpr__44825.call(null,G__44826));
})();
var remove_fn = ((function (i__44823,user_type,author,pending_QMARK_,current_user,display_name,removing_QMARK_,user,c__4527__auto__,size__4528__auto__,b__44824,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_){
return (function (){
var alert_data = cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"link-button-title","link-button-title",-812305976),new cljs.core.Keyword(null,"solid-button-style","solid-button-style",-723792244),new cljs.core.Keyword(null,"icon","icon",1679606541),new cljs.core.Keyword(null,"title","title",636505583),new cljs.core.Keyword(null,"action","action",-811238024),new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),new cljs.core.Keyword(null,"link-button-cb","link-button-cb",559710301),new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),new cljs.core.Keyword(null,"message","message",-406056002)],[((pending_QMARK_)?"No, keep it":"No, keep them"),new cljs.core.Keyword(null,"red","red",-969428204),"/img/ML/trash.svg",["Remove ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(display_name),"?"].join(''),((pending_QMARK_)?"cancel-invitation":"remove-user"),((pending_QMARK_)?"Yes, cancel":"Yes, remove"),((function (i__44823,user_type,author,pending_QMARK_,current_user,display_name,removing_QMARK_,user,c__4527__auto__,size__4528__auto__,b__44824,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_){
return (function (){
return oc.web.components.ui.alert_modal.hide_alert();
});})(i__44823,user_type,author,pending_QMARK_,current_user,display_name,removing_QMARK_,user,c__4527__auto__,size__4528__auto__,b__44824,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_))
,((function (i__44823,user_type,author,pending_QMARK_,current_user,display_name,removing_QMARK_,user,c__4527__auto__,size__4528__auto__,b__44824,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_){
return (function (){
oc.web.components.team_management_modal.real_remove_fn(s,author,user,new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(team_data),((function (i__44823,user_type,author,pending_QMARK_,current_user,display_name,removing_QMARK_,user,c__4527__auto__,size__4528__auto__,b__44824,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_){
return (function (p__44827){
var map__44828 = p__44827;
var map__44828__$1 = (((((!((map__44828 == null))))?(((((map__44828.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__44828.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__44828):map__44828);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__44828__$1,new cljs.core.Keyword(null,"success","success",1890645906));
if(cljs.core.truth_(success)){
return oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"title","title",636505583),((pending_QMARK_)?"Invitation cancelled":"Member removed from team"),new cljs.core.Keyword(null,"primary-bt-title","primary-bt-title",653140150),"OK",new cljs.core.Keyword(null,"primary-bt-dismiss","primary-bt-dismiss",-820688058),true,new cljs.core.Keyword(null,"expire","expire",-70657108),(3),new cljs.core.Keyword(null,"id","id",-1388402092),((pending_QMARK_)?new cljs.core.Keyword(null,"cancel-invitation","cancel-invitation",1517824759):new cljs.core.Keyword(null,"member-removed-from-team","member-removed-from-team",-1908267292))], null));
} else {
return null;
}
});})(i__44823,user_type,author,pending_QMARK_,current_user,display_name,removing_QMARK_,user,c__4527__auto__,size__4528__auto__,b__44824,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_))
);

return oc.web.components.ui.alert_modal.hide_alert();
});})(i__44823,user_type,author,pending_QMARK_,current_user,display_name,removing_QMARK_,user,c__4527__auto__,size__4528__auto__,b__44824,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_))
,((pending_QMARK_)?"Are you sure you want to cancel this invitation?":new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"This user will no longer be able to access your team on Wut.",new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"br","br",934104792)], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"br","br",934104792)], null),"Are you sure you want to remove this user?"], null))]);
return oc.web.components.ui.alert_modal.show_alert(alert_data);
});})(i__44823,user_type,author,pending_QMARK_,current_user,display_name,removing_QMARK_,user,c__4527__auto__,size__4528__auto__,b__44824,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_))
;
var roster_user = cljs.core.first(cljs.core.filterv(((function (i__44823,user_type,author,pending_QMARK_,current_user,display_name,removing_QMARK_,remove_fn,user,c__4527__auto__,size__4528__auto__,b__44824,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_){
return (function (p1__44813_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__44813_SHARP_),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user));
});})(i__44823,user_type,author,pending_QMARK_,current_user,display_name,removing_QMARK_,remove_fn,user,c__4527__auto__,size__4528__auto__,b__44824,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_))
,new cljs.core.Keyword(null,"users","users",-713552705).cljs$core$IFn$_invoke$arity$1(team_roster)));
var resend_fn = ((function (i__44823,user_type,author,pending_QMARK_,current_user,display_name,removing_QMARK_,remove_fn,roster_user,user,c__4527__auto__,size__4528__auto__,b__44824,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_){
return (function (){
var invitation_type = ((cljs.core.contains_QMARK_(roster_user,new cljs.core.Keyword(null,"slack-id","slack-id",862141985)))?"slack":"email");
var inviting_user = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(invitation_type,"email"))?new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(user):cljs.core.select_keys(roster_user,new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"email","email",1415816706),new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103),new cljs.core.Keyword(null,"first-name","first-name",-1559982131),new cljs.core.Keyword(null,"last-name","last-name",-1695738974),new cljs.core.Keyword(null,"slack-id","slack-id",862141985),new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561)], null)));
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"invite-users","invite-users",107417337)], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"user","user",1532431356),inviting_user,new cljs.core.Keyword(null,"type","type",1174270348),invitation_type,new cljs.core.Keyword(null,"role","role",-736691072),new cljs.core.Keyword(null,"role","role",-736691072).cljs$core$IFn$_invoke$arity$1(user),new cljs.core.Keyword(null,"error","error",-978969032),null], null)], null)], null));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.team-management-modal","resending-invite","oc.web.components.team-management-modal/resending-invite",-1107039795).cljs$core$IFn$_invoke$arity$1(s),true);

return oc.web.actions.team.invite_users.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"invite-users","invite-users",107417337).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"invite-data","invite-data",-758838050)))),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([""], 0));
});})(i__44823,user_type,author,pending_QMARK_,current_user,display_name,removing_QMARK_,remove_fn,roster_user,user,c__4527__auto__,size__4528__auto__,b__44824,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_))
;
var slack_display_name = ((((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(user),"uninvited")) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(user),"pending"))))?new cljs.core.Keyword(null,"slack-display-name","slack-display-name",-1387558465).cljs$core$IFn$_invoke$arity$1(roster_user):cljs.core.some(((function (i__44823,user_type,author,pending_QMARK_,current_user,display_name,removing_QMARK_,remove_fn,roster_user,resend_fn,user,c__4527__auto__,size__4528__auto__,b__44824,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_){
return (function (p1__44814_SHARP_){
if(cljs.core.seq(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(p1__44814_SHARP_))){
return new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(p1__44814_SHARP_);
} else {
return null;
}
});})(i__44823,user_type,author,pending_QMARK_,current_user,display_name,removing_QMARK_,remove_fn,roster_user,resend_fn,user,c__4527__auto__,size__4528__auto__,b__44824,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_))
,cljs.core.vals(new cljs.core.Keyword(null,"slack-users","slack-users",-434149941).cljs$core$IFn$_invoke$arity$1(roster_user))));
var fixed_display_name = ((((cljs.core.seq(slack_display_name)) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(slack_display_name,"-")) && ((!(clojure.string.starts_with_QMARK_(slack_display_name,"@"))))))?["@",cljs.core.str.cljs$core$IFn$_invoke$arity$1(slack_display_name)].join(''):slack_display_name);
cljs.core.chunk_append(b__44824,React.createElement("div",({"key": ["org-settings-team-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user))].join(''), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["team-management-users-item","group",((pending_QMARK_)?"is-pending-user":null)], null))}),(cljs.core.truth_(removing_QMARK_)?sablono.interpreter.interpret((oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.small_loading.small_loading.call(null))):sablono.interpreter.interpret((oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1(user) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,user)))),React.createElement("div",({"className": "user-name"}),React.createElement("button",({"title": ["<span>",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(user)),((cljs.core.seq(fixed_display_name))?[" | <i class=\"mdi mdi-slack\"></i>",((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(fixed_display_name,"-"))?null:[" ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(fixed_display_name)].join(''))].join(''):null),"</span>"].join(''), "onClick": ((function (i__44823,user_type,author,pending_QMARK_,current_user,display_name,removing_QMARK_,remove_fn,roster_user,resend_fn,slack_display_name,fixed_display_name,user,c__4527__auto__,size__4528__auto__,b__44824,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_){
return (function (p1__44815_SHARP_){
if(cljs.core.truth_(oc.web.utils.user.active_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user], 0)))){
oc.web.lib.utils.event_stop(p1__44815_SHARP_);

return oc.web.actions.nav_sidebar.nav_to_author_BANG_.cljs$core$IFn$_invoke$arity$3(p1__44815_SHARP_,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user),oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user)));
} else {
return null;
}
});})(i__44823,user_type,author,pending_QMARK_,current_user,display_name,removing_QMARK_,remove_fn,roster_user,resend_fn,slack_display_name,fixed_display_name,user,c__4527__auto__,size__4528__auto__,b__44824,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_))
, "data-toggle": "tooltip", "data-html": "true", "data-placement": "top", "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["mlb-reset","user-name-label",oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"pending","pending",-220036727),pending_QMARK_,new cljs.core.Keyword(null,"removing","removing",1104822312),removing_QMARK_], null))], null))}),sablono.interpreter.interpret(display_name),sablono.interpreter.interpret(((current_user)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.current-user","span.current-user",-815975610)," (you)"], null):null))),sablono.interpreter.interpret(((pending_QMARK_)?new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.pending-user","div.pending-user",888873424)," (pending",(cljs.core.truth_(is_admin_QMARK_)?": ":null),(cljs.core.truth_(is_admin_QMARK_)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.resend-pending-bt","button.mlb-reset.resend-pending-bt",-1268446188),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),resend_fn,new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687),"tooltip",new cljs.core.Keyword(null,"data-placement","data-placement",166529525),"top",new cljs.core.Keyword(null,"data-container","data-container",1473653353),"body",new cljs.core.Keyword(null,"title","title",636505583),["Resend invitation via ",((cljs.core.seq(fixed_display_name))?"slack":"email")].join('')], null),"resend"], null):null),(cljs.core.truth_(is_admin_QMARK_)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.remove-pending-bt","button.mlb-reset.remove-pending-bt",-1627874597),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),remove_fn,new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687),"tooltip",new cljs.core.Keyword(null,"data-placement","data-placement",166529525),"top",new cljs.core.Keyword(null,"data-container","data-container",1473653353),"body",new cljs.core.Keyword(null,"title","title",636505583),"Cancel invitation"], null),"cancel"], null):null),")"], null):null))),(function (){var attrs44830 = ((((current_user) || (cljs.core.not(is_admin_QMARK_))))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.self-user-type","span.self-user-type",1488736841),clojure.string.capitalize(new cljs.core.Keyword(null,"role-string","role-string",82910575).cljs$core$IFn$_invoke$arity$1(user))], null):(function (){var G__44831 = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"user-id","user-id",-206822291),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user),new cljs.core.Keyword(null,"user-type","user-type",738868936),new cljs.core.Keyword(null,"role","role",-736691072).cljs$core$IFn$_invoke$arity$1(user),new cljs.core.Keyword(null,"disabled?","disabled?",-1523234181),removing_QMARK_,new cljs.core.Keyword(null,"on-change","on-change",-732046149),((function (i__44823,user_type,author,pending_QMARK_,current_user,display_name,removing_QMARK_,remove_fn,roster_user,resend_fn,slack_display_name,fixed_display_name,user,c__4527__auto__,size__4528__auto__,b__44824,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_){
return (function (p1__44816_SHARP_){
return oc.web.actions.team.switch_user_type.cljs$core$IFn$_invoke$arity$variadic(user,new cljs.core.Keyword(null,"role","role",-736691072).cljs$core$IFn$_invoke$arity$1(user),p1__44816_SHARP_,user,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([author], 0));
});})(i__44823,user_type,author,pending_QMARK_,current_user,display_name,removing_QMARK_,remove_fn,roster_user,resend_fn,slack_display_name,fixed_display_name,user,c__4527__auto__,size__4528__auto__,b__44824,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_))
,new cljs.core.Keyword(null,"hide-admin","hide-admin",-823852536),cljs.core.not(is_admin_QMARK_),new cljs.core.Keyword(null,"on-remove","on-remove",-268656163),((((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2("pending",new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(user))) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(cur_user_data)))))?remove_fn:null)], null);
return (oc.web.components.ui.user_type_dropdown.user_type_dropdown.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_type_dropdown.user_type_dropdown.cljs$core$IFn$_invoke$arity$1(G__44831) : oc.web.components.ui.user_type_dropdown.user_type_dropdown.call(null,G__44831));
})());
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs44830))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["user-role"], null)], null),attrs44830], 0))):({"className": "user-role"})),((cljs.core.map_QMARK_(attrs44830))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs44830)], null)));
})()));

var G__44862 = (i__44823 + (1));
i__44823 = G__44862;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__44824),oc$web$components$team_management_modal$iter__44821(cljs.core.chunk_rest(s__44822__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__44824),null);
}
} else {
var user = cljs.core.first(s__44822__$2);
var user_type = new cljs.core.Keyword(null,"role","role",-736691072).cljs$core$IFn$_invoke$arity$1(user);
var author = cljs.core.some(((function (user_type,user,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_){
return (function (p1__44812_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__44812_SHARP_),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user))){
return p1__44812_SHARP_;
} else {
return null;
}
});})(user_type,user,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_))
,org_authors);
var pending_QMARK_ = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2("pending",new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(user))) && (((cljs.core.contains_QMARK_(user,new cljs.core.Keyword(null,"email","email",1415816706))) || (cljs.core.contains_QMARK_(user,new cljs.core.Keyword(null,"slack-id","slack-id",862141985))))));
var current_user = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(cur_user_data));
var display_name = oc.lib.user.name_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user], 0));
var removing_QMARK_ = (function (){var G__44833 = new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user);
var fexpr__44832 = cljs.core.deref(new cljs.core.Keyword("oc.web.components.team-management-modal","removing","oc.web.components.team-management-modal/removing",1059780791).cljs$core$IFn$_invoke$arity$1(s));
return (fexpr__44832.cljs$core$IFn$_invoke$arity$1 ? fexpr__44832.cljs$core$IFn$_invoke$arity$1(G__44833) : fexpr__44832.call(null,G__44833));
})();
var remove_fn = ((function (user_type,author,pending_QMARK_,current_user,display_name,removing_QMARK_,user,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_){
return (function (){
var alert_data = cljs.core.PersistentHashMap.fromArrays([new cljs.core.Keyword(null,"link-button-title","link-button-title",-812305976),new cljs.core.Keyword(null,"solid-button-style","solid-button-style",-723792244),new cljs.core.Keyword(null,"icon","icon",1679606541),new cljs.core.Keyword(null,"title","title",636505583),new cljs.core.Keyword(null,"action","action",-811238024),new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567),new cljs.core.Keyword(null,"link-button-cb","link-button-cb",559710301),new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787),new cljs.core.Keyword(null,"message","message",-406056002)],[((pending_QMARK_)?"No, keep it":"No, keep them"),new cljs.core.Keyword(null,"red","red",-969428204),"/img/ML/trash.svg",["Remove ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(display_name),"?"].join(''),((pending_QMARK_)?"cancel-invitation":"remove-user"),((pending_QMARK_)?"Yes, cancel":"Yes, remove"),(function (){
return oc.web.components.ui.alert_modal.hide_alert();
}),(function (){
oc.web.components.team_management_modal.real_remove_fn(s,author,user,new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(team_data),(function (p__44834){
var map__44835 = p__44834;
var map__44835__$1 = (((((!((map__44835 == null))))?(((((map__44835.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__44835.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__44835):map__44835);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__44835__$1,new cljs.core.Keyword(null,"success","success",1890645906));
if(cljs.core.truth_(success)){
return oc.web.actions.notifications.show_notification(new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"title","title",636505583),((pending_QMARK_)?"Invitation cancelled":"Member removed from team"),new cljs.core.Keyword(null,"primary-bt-title","primary-bt-title",653140150),"OK",new cljs.core.Keyword(null,"primary-bt-dismiss","primary-bt-dismiss",-820688058),true,new cljs.core.Keyword(null,"expire","expire",-70657108),(3),new cljs.core.Keyword(null,"id","id",-1388402092),((pending_QMARK_)?new cljs.core.Keyword(null,"cancel-invitation","cancel-invitation",1517824759):new cljs.core.Keyword(null,"member-removed-from-team","member-removed-from-team",-1908267292))], null));
} else {
return null;
}
}));

return oc.web.components.ui.alert_modal.hide_alert();
}),((pending_QMARK_)?"Are you sure you want to cancel this invitation?":new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span","span",1394872991),"This user will no longer be able to access your team on Wut.",new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"br","br",934104792)], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"br","br",934104792)], null),"Are you sure you want to remove this user?"], null))]);
return oc.web.components.ui.alert_modal.show_alert(alert_data);
});})(user_type,author,pending_QMARK_,current_user,display_name,removing_QMARK_,user,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_))
;
var roster_user = cljs.core.first(cljs.core.filterv(((function (user_type,author,pending_QMARK_,current_user,display_name,removing_QMARK_,remove_fn,user,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_){
return (function (p1__44813_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__44813_SHARP_),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user));
});})(user_type,author,pending_QMARK_,current_user,display_name,removing_QMARK_,remove_fn,user,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_))
,new cljs.core.Keyword(null,"users","users",-713552705).cljs$core$IFn$_invoke$arity$1(team_roster)));
var resend_fn = ((function (user_type,author,pending_QMARK_,current_user,display_name,removing_QMARK_,remove_fn,roster_user,user,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_){
return (function (){
var invitation_type = ((cljs.core.contains_QMARK_(roster_user,new cljs.core.Keyword(null,"slack-id","slack-id",862141985)))?"slack":"email");
var inviting_user = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(invitation_type,"email"))?new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(user):cljs.core.select_keys(roster_user,new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"email","email",1415816706),new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103),new cljs.core.Keyword(null,"first-name","first-name",-1559982131),new cljs.core.Keyword(null,"last-name","last-name",-1695738974),new cljs.core.Keyword(null,"slack-id","slack-id",862141985),new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561)], null)));
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"invite-users","invite-users",107417337)], null),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"user","user",1532431356),inviting_user,new cljs.core.Keyword(null,"type","type",1174270348),invitation_type,new cljs.core.Keyword(null,"role","role",-736691072),new cljs.core.Keyword(null,"role","role",-736691072).cljs$core$IFn$_invoke$arity$1(user),new cljs.core.Keyword(null,"error","error",-978969032),null], null)], null)], null));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.team-management-modal","resending-invite","oc.web.components.team-management-modal/resending-invite",-1107039795).cljs$core$IFn$_invoke$arity$1(s),true);

return oc.web.actions.team.invite_users.cljs$core$IFn$_invoke$arity$variadic(new cljs.core.Keyword(null,"invite-users","invite-users",107417337).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"invite-data","invite-data",-758838050)))),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([""], 0));
});})(user_type,author,pending_QMARK_,current_user,display_name,removing_QMARK_,remove_fn,roster_user,user,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_))
;
var slack_display_name = ((((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(user),"uninvited")) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(user),"pending"))))?new cljs.core.Keyword(null,"slack-display-name","slack-display-name",-1387558465).cljs$core$IFn$_invoke$arity$1(roster_user):cljs.core.some(((function (user_type,author,pending_QMARK_,current_user,display_name,removing_QMARK_,remove_fn,roster_user,resend_fn,user,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_){
return (function (p1__44814_SHARP_){
if(cljs.core.seq(new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(p1__44814_SHARP_))){
return new cljs.core.Keyword(null,"display-name","display-name",694513143).cljs$core$IFn$_invoke$arity$1(p1__44814_SHARP_);
} else {
return null;
}
});})(user_type,author,pending_QMARK_,current_user,display_name,removing_QMARK_,remove_fn,roster_user,resend_fn,user,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_))
,cljs.core.vals(new cljs.core.Keyword(null,"slack-users","slack-users",-434149941).cljs$core$IFn$_invoke$arity$1(roster_user))));
var fixed_display_name = ((((cljs.core.seq(slack_display_name)) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(slack_display_name,"-")) && ((!(clojure.string.starts_with_QMARK_(slack_display_name,"@"))))))?["@",cljs.core.str.cljs$core$IFn$_invoke$arity$1(slack_display_name)].join(''):slack_display_name);
return cljs.core.cons(React.createElement("div",({"key": ["org-settings-team-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user))].join(''), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["team-management-users-item","group",((pending_QMARK_)?"is-pending-user":null)], null))}),(cljs.core.truth_(removing_QMARK_)?sablono.interpreter.interpret((oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0 ? oc.web.components.ui.small_loading.small_loading.cljs$core$IFn$_invoke$arity$0() : oc.web.components.ui.small_loading.small_loading.call(null))):sablono.interpreter.interpret((oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1(user) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,user)))),React.createElement("div",({"className": "user-name"}),React.createElement("button",({"title": ["<span>",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(user)),((cljs.core.seq(fixed_display_name))?[" | <i class=\"mdi mdi-slack\"></i>",((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(fixed_display_name,"-"))?null:[" ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(fixed_display_name)].join(''))].join(''):null),"</span>"].join(''), "onClick": ((function (user_type,author,pending_QMARK_,current_user,display_name,removing_QMARK_,remove_fn,roster_user,resend_fn,slack_display_name,fixed_display_name,user,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_){
return (function (p1__44815_SHARP_){
if(cljs.core.truth_(oc.web.utils.user.active_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user], 0)))){
oc.web.lib.utils.event_stop(p1__44815_SHARP_);

return oc.web.actions.nav_sidebar.nav_to_author_BANG_.cljs$core$IFn$_invoke$arity$3(p1__44815_SHARP_,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user),oc.web.urls.contributions.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user)));
} else {
return null;
}
});})(user_type,author,pending_QMARK_,current_user,display_name,removing_QMARK_,remove_fn,roster_user,resend_fn,slack_display_name,fixed_display_name,user,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_))
, "data-toggle": "tooltip", "data-html": "true", "data-placement": "top", "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["mlb-reset","user-name-label",oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"pending","pending",-220036727),pending_QMARK_,new cljs.core.Keyword(null,"removing","removing",1104822312),removing_QMARK_], null))], null))}),sablono.interpreter.interpret(display_name),sablono.interpreter.interpret(((current_user)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.current-user","span.current-user",-815975610)," (you)"], null):null))),sablono.interpreter.interpret(((pending_QMARK_)?new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.pending-user","div.pending-user",888873424)," (pending",(cljs.core.truth_(is_admin_QMARK_)?": ":null),(cljs.core.truth_(is_admin_QMARK_)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.resend-pending-bt","button.mlb-reset.resend-pending-bt",-1268446188),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),resend_fn,new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687),"tooltip",new cljs.core.Keyword(null,"data-placement","data-placement",166529525),"top",new cljs.core.Keyword(null,"data-container","data-container",1473653353),"body",new cljs.core.Keyword(null,"title","title",636505583),["Resend invitation via ",((cljs.core.seq(fixed_display_name))?"slack":"email")].join('')], null),"resend"], null):null),(cljs.core.truth_(is_admin_QMARK_)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.remove-pending-bt","button.mlb-reset.remove-pending-bt",-1627874597),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),remove_fn,new cljs.core.Keyword(null,"data-toggle","data-toggle",436966687),"tooltip",new cljs.core.Keyword(null,"data-placement","data-placement",166529525),"top",new cljs.core.Keyword(null,"data-container","data-container",1473653353),"body",new cljs.core.Keyword(null,"title","title",636505583),"Cancel invitation"], null),"cancel"], null):null),")"], null):null))),(function (){var attrs44830 = ((((current_user) || (cljs.core.not(is_admin_QMARK_))))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.self-user-type","span.self-user-type",1488736841),clojure.string.capitalize(new cljs.core.Keyword(null,"role-string","role-string",82910575).cljs$core$IFn$_invoke$arity$1(user))], null):(function (){var G__44839 = new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"user-id","user-id",-206822291),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user),new cljs.core.Keyword(null,"user-type","user-type",738868936),new cljs.core.Keyword(null,"role","role",-736691072).cljs$core$IFn$_invoke$arity$1(user),new cljs.core.Keyword(null,"disabled?","disabled?",-1523234181),removing_QMARK_,new cljs.core.Keyword(null,"on-change","on-change",-732046149),((function (user_type,author,pending_QMARK_,current_user,display_name,removing_QMARK_,remove_fn,roster_user,resend_fn,slack_display_name,fixed_display_name,user,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_){
return (function (p1__44816_SHARP_){
return oc.web.actions.team.switch_user_type.cljs$core$IFn$_invoke$arity$variadic(user,new cljs.core.Keyword(null,"role","role",-736691072).cljs$core$IFn$_invoke$arity$1(user),p1__44816_SHARP_,user,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([author], 0));
});})(user_type,author,pending_QMARK_,current_user,display_name,removing_QMARK_,remove_fn,roster_user,resend_fn,slack_display_name,fixed_display_name,user,s__44822__$2,temp__5735__auto__,org_data,invite_users_data,team_data,team_roster,cur_user_data,org_authors,all_users,filtered_users,splitted_users,self_user,other_users,other_sorted_users,sorted_users,is_admin_or_author_QMARK_,is_admin_QMARK_))
,new cljs.core.Keyword(null,"hide-admin","hide-admin",-823852536),cljs.core.not(is_admin_QMARK_),new cljs.core.Keyword(null,"on-remove","on-remove",-268656163),((((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2("pending",new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(user))) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(cur_user_data)))))?remove_fn:null)], null);
return (oc.web.components.ui.user_type_dropdown.user_type_dropdown.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_type_dropdown.user_type_dropdown.cljs$core$IFn$_invoke$arity$1(G__44839) : oc.web.components.ui.user_type_dropdown.user_type_dropdown.call(null,G__44839));
})());
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs44830))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["user-role"], null)], null),attrs44830], 0))):({"className": "user-role"})),((cljs.core.map_QMARK_(attrs44830))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs44830)], null)));
})()),oc$web$components$team_management_modal$iter__44821(cljs.core.rest(s__44822__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(sorted_users);
})())))));
}),new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"org-data","org-data",96720321)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"invite-data","invite-data",-758838050)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.team-management-modal","resending-invite","oc.web.components.team-management-modal/resending-invite",-1107039795)),rum.core.local.cljs$core$IFn$_invoke$arity$2("",new cljs.core.Keyword("oc.web.components.team-management-modal","query","oc.web.components.team-management-modal/query",-1300152145)),rum.core.local.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentHashSet.EMPTY,new cljs.core.Keyword("oc.web.components.team-management-modal","removing","oc.web.components.team-management-modal/removing",1059780791)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
var org_data_44864 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"org-data","org-data",96720321)));
oc.web.actions.team.refresh_team_data(org_data_44864);

return s;
}),new cljs.core.Keyword(null,"after-render","after-render",1997533433),(function (s){
var G__44842_44865 = $("[data-toggle=\"tooltip\"]");
G__44842_44865.tooltip("fixTitle");

G__44842_44865.tooltip("hide");


if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.team-management-modal","resending-invite","oc.web.components.team-management-modal/resending-invite",-1107039795).cljs$core$IFn$_invoke$arity$1(s)))){
var invite_users_data_44866 = new cljs.core.Keyword(null,"invite-users","invite-users",107417337).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"invite-data","invite-data",-758838050))));
if((cljs.core.count(invite_users_data_44866) === (0))){
oc.web.components.team_management_modal.alert_resend_done();

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.team-management-modal","resending-invite","oc.web.components.team-management-modal/resending-invite",-1107039795).cljs$core$IFn$_invoke$arity$1(s),false);
} else {
}
} else {
}

return s;
})], null)], null),"team-management-modal");

//# sourceMappingURL=oc.web.components.team_management_modal.js.map
