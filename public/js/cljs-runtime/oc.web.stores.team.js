goog.provide('oc.web.stores.team');
oc.web.stores.team.deep_merge_users = (function oc$web$stores$team$deep_merge_users(new_users,old_users){
var filtered_new_users = cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__43481_SHARP_){
var and__4115__auto__ = cljs.core.seq(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__43481_SHARP_));
if(and__4115__auto__){
return oc.web.utils.user.active_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__43481_SHARP_], 0));
} else {
return and__4115__auto__;
}
}),((cljs.core.map_QMARK_(new_users))?cljs.core.vals(new_users):new_users));
var new_users_map = cljs.core.zipmap(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291),filtered_new_users),filtered_new_users);
return cljs.core.merge_with.cljs$core$IFn$_invoke$arity$variadic(cljs.core.merge,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([old_users,new_users_map], 0));
});
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"active-users","active-users",-329555191),(function (db,p__43483){
var vec__43484 = p__43483;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43484,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43484,(1),null);
var active_users_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43484,(2),null);
var temp__5733__auto__ = new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(active_users_data));
if(cljs.core.truth_(temp__5733__auto__)){
var users = temp__5733__auto__;
var follow_publishers_list = oc.web.dispatcher.follow_publishers_list.cljs$core$IFn$_invoke$arity$2(org_slug,db);
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$2(db,org_slug);
var fixed_users = oc.web.stores.user.parse_users(users,org_data,follow_publishers_list);
var change_data = oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$1(db);
var users_map = cljs.core.zipmap(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291),users),fixed_users);
var follow_publishers_list_key = oc.web.dispatcher.follow_publishers_list_key(org_slug);
var old_follow_publishers_list = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,follow_publishers_list_key);
var next_follow_publishers_list = oc.web.stores.user.enrich_publishers_list(old_follow_publishers_list,users_map);
return cljs.core.update.cljs$core$IFn$_invoke$arity$3(oc.web.utils.activity.update_all_containers(cljs.core.assoc_in(cljs.core.assoc_in(cljs.core.assoc_in(db,oc.web.dispatcher.active_users_key(org_slug),users_map),oc.web.dispatcher.mention_users_key(org_slug),oc.web.utils.mention.users_for_mentions(users_map)),follow_publishers_list_key,next_follow_publishers_list),org_data,change_data,users_map,next_follow_publishers_list),new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915),(function (p1__43482_SHARP_){
return oc.web.stores.user.parse_user_data(p1__43482_SHARP_,org_data,users_map);
}));
} else {
return db;
}
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"teams-get","teams-get",1862968866),(function (db,p__43487){
var vec__43488 = p__43487;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43488,(0),null);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"teams-data-requested","teams-data-requested",-1880742826),true);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"teams-loaded","teams-loaded",-1387044499),(function (db,p__43491){
var vec__43492 = p__43491;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43492,(0),null);
var teams = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43492,(1),null);
return cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"teams-data","teams-data",-808450077),new cljs.core.Keyword(null,"teams","teams",1677714510)], null),teams);
}));
/**
 * Given the previous users map and the new users vector coming from team or roster.
 * Create a map of the new users with only some arbitrary data and merge them with the old users.
 */
oc.web.stores.team.users_info_hover_from_roster = (function oc$web$stores$team$users_info_hover_from_roster(old_users_map,roster_data){
var filtered_users = oc.web.utils.user.filter_active_users(new cljs.core.Keyword(null,"users","users",-713552705).cljs$core$IFn$_invoke$arity$1(roster_data));
var new_users_map = cljs.core.zipmap(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291),filtered_users),cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__43495_SHARP_){
return cljs.core.select_keys(p1__43495_SHARP_,new cljs.core.PersistentVector(null, 9, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"user-id","user-id",-206822291),new cljs.core.Keyword(null,"first-name","first-name",-1559982131),new cljs.core.Keyword(null,"last-name","last-name",-1695738974),new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103),new cljs.core.Keyword(null,"name","name",1843675177),new cljs.core.Keyword(null,"short-name","short-name",-1767085022),new cljs.core.Keyword(null,"location","location",1815599388),new cljs.core.Keyword(null,"timezone","timezone",1831928099),new cljs.core.Keyword(null,"title","title",636505583)], null));
}),filtered_users));
return cljs.core.merge_with.cljs$core$IFn$_invoke$arity$variadic(cljs.core.merge,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(function (){var or__4126__auto__ = old_users_map;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.PersistentArrayMap.EMPTY;
}
})(),new_users_map], 0));
});
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"team-roster-loaded","team-roster-loaded",75281503),(function (db,p__43498){
var vec__43499 = p__43498;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43499,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43499,(1),null);
var roster_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43499,(2),null);
if(cljs.core.truth_(roster_data)){
var follow_publishers_list_key = oc.web.dispatcher.follow_publishers_list_key(org_slug);
var old_follow_publishers_list = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,follow_publishers_list_key);
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$2(db,org_slug);
var parsed_roster_data = cljs.core.update.cljs$core$IFn$_invoke$arity$3(roster_data,new cljs.core.Keyword(null,"users","users",-713552705),(function (p1__43496_SHARP_){
return oc.web.stores.user.parse_users(p1__43496_SHARP_,org_data,old_follow_publishers_list);
}));
var merged_users_data = oc.web.stores.team.deep_merge_users(new cljs.core.Keyword(null,"users","users",-713552705).cljs$core$IFn$_invoke$arity$1(parsed_roster_data),oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org_slug,db));
var next_follow_publishers_list = oc.web.stores.user.enrich_publishers_list(old_follow_publishers_list,merged_users_data);
return cljs.core.assoc_in(cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc_in(cljs.core.assoc_in(cljs.core.assoc_in(db,oc.web.dispatcher.team_roster_key(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(roster_data)),parsed_roster_data),oc.web.dispatcher.mention_users_key(org_slug),oc.web.utils.mention.users_for_mentions(merged_users_data)),oc.web.dispatcher.active_users_key(org_slug),merged_users_data),oc.web.dispatcher.users_info_hover_key(org_slug),(function (p1__43497_SHARP_){
return oc.web.stores.team.users_info_hover_from_roster(p1__43497_SHARP_,parsed_roster_data);
})),follow_publishers_list_key,next_follow_publishers_list);
} else {
return db;
}
}));
oc.web.stores.team.parse_team_data = (function oc$web$stores$team$parse_team_data(team_data,org_data,follow_publishers_list){
var team_has_bot_QMARK_ = oc.web.lib.jwt.team_has_bot_QMARK_(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(team_data));
var slack_orgs = new cljs.core.Keyword(null,"slack-orgs","slack-orgs",-1806634042).cljs$core$IFn$_invoke$arity$1(team_data);
var slack_users = oc.web.lib.jwt.get_key(new cljs.core.Keyword(null,"slack-users","slack-users",-434149941));
var can_add_bot_QMARK_ = cljs.core.some((function (p1__43508_SHARP_){
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(slack_users,cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(p1__43508_SHARP_)));
}),slack_orgs);
return cljs.core.update.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(team_data,new cljs.core.Keyword(null,"can-slack-invite","can-slack-invite",1450736688),team_has_bot_QMARK_),new cljs.core.Keyword(null,"can-add-bot","can-add-bot",-1494909026),((cljs.core.not(team_has_bot_QMARK_))?can_add_bot_QMARK_:false)),new cljs.core.Keyword(null,"users","users",-713552705),(function (p1__43509_SHARP_){
return oc.web.stores.user.parse_users(p1__43509_SHARP_,org_data,follow_publishers_list);
}));
});
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"team-loaded","team-loaded",1150677177),(function (db,p__43517){
var vec__43518 = p__43517;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43518,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43518,(1),null);
var team_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43518,(2),null);
if(cljs.core.truth_(team_data)){
var follow_publishers_list_key = oc.web.dispatcher.follow_publishers_list_key(org_slug);
var old_follow_publishers_list = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,follow_publishers_list_key);
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$2(db,org_slug);
var parsed_team_data = oc.web.stores.team.parse_team_data(team_data,org_data,old_follow_publishers_list);
var merged_users_data = oc.web.stores.team.deep_merge_users(new cljs.core.Keyword(null,"users","users",-713552705).cljs$core$IFn$_invoke$arity$1(parsed_team_data),oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org_slug,db));
var next_follow_publishers_list = oc.web.stores.user.enrich_publishers_list(old_follow_publishers_list,merged_users_data);
return cljs.core.assoc_in(cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc_in(cljs.core.assoc_in(cljs.core.assoc_in(db,oc.web.dispatcher.team_data_key(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(team_data)),parsed_team_data),oc.web.dispatcher.mention_users_key(org_slug),oc.web.utils.mention.users_for_mentions(merged_users_data)),oc.web.dispatcher.active_users_key(org_slug),merged_users_data),oc.web.dispatcher.users_info_hover_key(org_slug),(function (p1__43516_SHARP_){
return oc.web.stores.team.users_info_hover_from_roster(p1__43516_SHARP_,parsed_team_data);
})),follow_publishers_list_key,next_follow_publishers_list);
} else {
return db;
}
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"channels-enumerate","channels-enumerate",375374686),(function (db,p__43529){
var vec__43530 = p__43529;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43530,(0),null);
var team_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43530,(1),null);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"enumerate-channels-requested","enumerate-channels-requested",-1130647943),true);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("channels-enumerate","success","channels-enumerate/success",-1838190865),(function (db,p__43537){
var vec__43538 = p__43537;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43538,(0),null);
var team_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43538,(1),null);
var channels = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43538,(2),null);
var channels_key = oc.web.dispatcher.team_channels_key(team_id);
if(cljs.core.truth_(channels)){
return cljs.core.assoc_in(db,channels_key,channels);
} else {
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core.update_in.cljs$core$IFn$_invoke$arity$4(db,cljs.core.butlast(channels_key),cljs.core.dissoc,cljs.core.last(channels_key)),new cljs.core.Keyword(null,"enumerate-channels-requested","enumerate-channels-requested",-1130647943));
}
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"invite-users","invite-users",107417337),(function (db,p__43545){
var vec__43546 = p__43545;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43546,(0),null);
var checked_users = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43546,(1),null);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"invite-users","invite-users",107417337),checked_users);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("invite-user","success","invite-user/success",365990759),(function (db,p__43549){
var vec__43550 = p__43549;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43550,(0),null);
var user = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43550,(1),null);
var inviting_users = new cljs.core.Keyword(null,"invite-users","invite-users",107417337).cljs$core$IFn$_invoke$arity$1(db);
var next_inviting_users = oc.web.lib.utils.vec_dissoc(inviting_users,user);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"invite-users","invite-users",107417337),next_inviting_users);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("invite-user","failed","invite-user/failed",1226102163),(function (db,p__43554){
var vec__43555 = p__43554;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43555,(0),null);
var user = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43555,(1),null);
var invite_users = new cljs.core.Keyword(null,"invite-users","invite-users",107417337).cljs$core$IFn$_invoke$arity$1(db);
var idx = oc.web.lib.utils.index_of(invite_users,(function (p1__43553_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(p1__43553_SHARP_),new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(user));
}));
var next_invite_users = cljs.core.assoc_in(invite_users,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [idx,new cljs.core.Keyword(null,"error","error",-978969032)], null),true);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"invite-users","invite-users",107417337),next_invite_users);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"user-action","user-action",1326176390),(function (db,p__43558){
var vec__43559 = p__43558;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43559,(0),null);
var team_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43559,(1),null);
var idx = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43559,(2),null);
return cljs.core.assoc_in(db,cljs.core.concat.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.team_data_key(team_id),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"users","users",-713552705),idx,new cljs.core.Keyword(null,"loading","loading",-737050189)], null)),true);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"email-domain-team-add","email-domain-team-add",-665391449),(function (db,p__43562){
var vec__43563 = p__43562;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43563,(0),null);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"add-email-domain-team-error","add-email-domain-team-error",-1479228464),false);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("email-domain-team-add","finish","email-domain-team-add/finish",-917388775),(function (db,p__43566){
var vec__43567 = p__43566;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43567,(0),null);
var success = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43567,(1),null);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"um-domain-invite","um-domain-invite",-1217004114),new cljs.core.Keyword(null,"domain","domain",1847214937)], null),(cljs.core.truth_(success)?"":new cljs.core.Keyword(null,"domain","domain",1847214937).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"um-domain-invite","um-domain-invite",-1217004114).cljs$core$IFn$_invoke$arity$1(db)))),new cljs.core.Keyword(null,"add-email-domain-team-error","add-email-domain-team-error",-1479228464),(cljs.core.truth_(success)?false:true));
}));

//# sourceMappingURL=oc.web.stores.team.js.map
