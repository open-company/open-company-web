goog.provide('oc.web.stores.org');
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"org-loaded","org-loaded",-1554103541),(function (db,p__43400){
var vec__43401 = p__43400;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43401,(0),null);
var org_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43401,(1),null);
var saved_QMARK_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43401,(2),null);
var email_domain = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43401,(3),null);
var fixed_org_data = oc.web.utils.activity.parse_org(db,org_data);
var org_data_key = oc.web.dispatcher.org_data_key(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(fixed_org_data));
var boards_key = oc.web.dispatcher.boards_key(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(fixed_org_data));
var old_boards = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,boards_key);
var board_slugs = cljs.core.set(cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (p1__43395_SHARP_){
return cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(p1__43395_SHARP_)));
}),new cljs.core.Keyword(null,"boards","boards",1912049694).cljs$core$IFn$_invoke$arity$1(org_data)));
var filter_board = (function (p__43404){
var vec__43405 = p__43404;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43405,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43405,(1),null);
return (board_slugs.cljs$core$IFn$_invoke$arity$1 ? board_slugs.cljs$core$IFn$_invoke$arity$1(k) : board_slugs.call(null,k));
});
var next_boards = cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,cljs.core.filter.cljs$core$IFn$_invoke$arity$2(filter_board,old_boards));
var with_saved_QMARK_ = (((saved_QMARK_ == null))?org_data:cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(org_data,new cljs.core.Keyword(null,"saved","saved",288760660),saved_QMARK_));
var next_org_editing = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(with_saved_QMARK_,new cljs.core.Keyword(null,"email-domain","email-domain",-768613335),email_domain),new cljs.core.Keyword(null,"has-changes","has-changes",-631476764));
var editable_boards = cljs.core.filterv((function (p1__43396_SHARP_){
if(cljs.core.not(new cljs.core.Keyword(null,"draft","draft",1421831058).cljs$core$IFn$_invoke$arity$1(p1__43396_SHARP_))){
return oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(p1__43396_SHARP_),"create","POST"], 0));
} else {
return false;
}
}),new cljs.core.Keyword(null,"boards","boards",1912049694).cljs$core$IFn$_invoke$arity$1(org_data));
var current_board_slug = oc.web.dispatcher.current_board_slug.cljs$core$IFn$_invoke$arity$0();
var editing_board = ((cljs.core.seq(editable_boards))?oc.web.actions.cmail.get_board_for_edit.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(cljs.core.truth_(oc.web.dispatcher.is_container_QMARK_(current_board_slug))?null:current_board_slug),editable_boards], 0)):null);
var active_users = oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(fixed_org_data),db);
var follow_boards_list_key = oc.web.dispatcher.follow_boards_list_key(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(fixed_org_data));
var unfollow_board_uuids = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,oc.web.dispatcher.unfollow_board_uuids_key(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(fixed_org_data)));
var follow_publishers_list = oc.web.dispatcher.follow_publishers_list.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(fixed_org_data),db);
var team_data_key = oc.web.dispatcher.team_data_key(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(fixed_org_data));
var update_team_users_QMARK_ = cljs.core.contains_QMARK_(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,team_data_key),new cljs.core.Keyword(null,"users","users",-713552705));
var team_roster_key = oc.web.dispatcher.team_roster_key(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(fixed_org_data));
var update_roster_users_QMARK_ = cljs.core.contains_QMARK_(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,team_roster_key),new cljs.core.Keyword(null,"users","users",-713552705));
var setup_cmail_QMARK_ = (((!(cljs.core.contains_QMARK_(db,cljs.core.first(oc.web.dispatcher.cmail_state_key)))))?editing_board:false);
var ndb = db;
var ndb__$1 = cljs.core.assoc_in(ndb,org_data_key,fixed_org_data);
var ndb__$2 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(ndb__$1,new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915),next_org_editing);
var ndb__$3 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(ndb__$2,new cljs.core.Keyword(null,"org-avatar-editing","org-avatar-editing",-1933353352),cljs.core.select_keys(fixed_org_data,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"logo-url","logo-url",-1629105032)], null)));
var ndb__$4 = cljs.core.update.cljs$core$IFn$_invoke$arity$3(ndb__$3,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915),(function (p1__43397_SHARP_){
return oc.web.stores.user.parse_user_data(p1__43397_SHARP_,fixed_org_data,active_users);
}));
var ndb__$5 = (cljs.core.truth_(setup_cmail_QMARK_)?cljs.core.assoc_in(ndb__$4,oc.web.dispatcher.cmail_state_key,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"key","key",-1516042587),oc.web.lib.utils.activity_uuid(),new cljs.core.Keyword(null,"fullscreen","fullscreen",-4371054),false,new cljs.core.Keyword(null,"collapsed","collapsed",-628494523),true], null)):ndb__$4);
var ndb__$6 = (cljs.core.truth_(setup_cmail_QMARK_)?cljs.core.update_in.cljs$core$IFn$_invoke$arity$4(ndb__$5,oc.web.dispatcher.cmail_data_key,cljs.core.merge,editing_board):ndb__$5);
var ndb__$7 = ((update_team_users_QMARK_)?cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(ndb__$6,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(team_data_key,new cljs.core.Keyword(null,"users","users",-713552705)),(function (p1__43398_SHARP_){
return oc.web.stores.user.parse_users(p1__43398_SHARP_,fixed_org_data,follow_publishers_list);
})):ndb__$6);
var ndb__$8 = ((update_roster_users_QMARK_)?cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(ndb__$7,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(team_roster_key,new cljs.core.Keyword(null,"users","users",-713552705)),(function (p1__43399_SHARP_){
return oc.web.stores.user.parse_users(p1__43399_SHARP_,fixed_org_data,follow_publishers_list);
})):ndb__$7);
var ndb__$9 = cljs.core.assoc_in(ndb__$8,boards_key,next_boards);
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(ndb__$9,follow_boards_list_key,(function (){
return oc.web.stores.user.enrich_boards_list(unfollow_board_uuids,new cljs.core.Keyword(null,"boards","boards",1912049694).cljs$core$IFn$_invoke$arity$1(fixed_org_data));
}));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("org-avatar-update","failed","org-avatar-update/failed",246419978),(function (db,p__43409){
var vec__43410 = p__43409;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43410,(0),null);
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$1(db);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"org-avatar-editing","org-avatar-editing",-1933353352),cljs.core.select_keys(org_data,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"logo-url","logo-url",-1629105032)], null)));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"org-create","org-create",619717847),(function (db,p__43414){
var vec__43415 = p__43414;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43415,(0),null);
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(db,new cljs.core.Keyword(null,"latest-entry-point","latest-entry-point",2059134699),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"latest-auth-settings","latest-auth-settings",-572090527),cljs.core.first(oc.web.dispatcher.api_entry_point_key),cljs.core.first(oc.web.dispatcher.auth_settings_key),oc.web.dispatcher.orgs_key], 0));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"org-edit-setup","org-edit-setup",-2083658838),(function (db,p__43418){
var vec__43419 = p__43418;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43419,(0),null);
var org_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43419,(1),null);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"org-editing","org-editing",-1868272915),org_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("bookmarks-nav","show","bookmarks-nav/show",-1028987900),(function (db,p__43423){
var vec__43424 = p__43423;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43424,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43424,(1),null);
var bookmarks_count_key = cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_data_key(org_slug),new cljs.core.Keyword(null,"bookmarks-count","bookmarks-count",-405810102)));
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(db,bookmarks_count_key,(function (p1__43422_SHARP_){
var or__4126__auto__ = p1__43422_SHARP_;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return (0);
}
}));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("drafts-nav","show","drafts-nav/show",-356771177),(function (db,p__43428){
var vec__43432 = p__43428;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43432,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43432,(1),null);
var drafts_count_key = cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_data_key(org_slug),new cljs.core.Keyword(null,"drafts-count","drafts-count",-1710494641)));
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(db,drafts_count_key,(function (p1__43427_SHARP_){
var or__4126__auto__ = p1__43427_SHARP_;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return (0);
}
}));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"maybe-badge-following","maybe-badge-following",-1127324050),(function (db,p__43435){
var vec__43436 = p__43435;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43436,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43436,(1),null);
var current_board_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43436,(2),null);
var badges_key = oc.web.dispatcher.badges_key(org_slug);
var badges_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,badges_key);
if(((((cljs.core.not(badges_data)) || ((!(cljs.core.contains_QMARK_(badges_data,new cljs.core.Keyword(null,"following","following",-2049193617))))))) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug),new cljs.core.Keyword(null,"following","following",-2049193617))))){
return cljs.core.assoc_in(db,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(badges_key,new cljs.core.Keyword(null,"following","following",-2049193617)),true);
} else {
return db;
}
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"maybe-badge-replies","maybe-badge-replies",-1675783392),(function (db,p__43439){
var vec__43440 = p__43439;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43440,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43440,(1),null);
var current_board_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43440,(2),null);
var badges_key = oc.web.dispatcher.badges_key(org_slug);
var badges_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,badges_key);
if(((((cljs.core.not(badges_data)) || ((!(cljs.core.contains_QMARK_(badges_data,new cljs.core.Keyword(null,"replies","replies",-1389888974))))))) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug),new cljs.core.Keyword(null,"replies","replies",-1389888974))))){
return cljs.core.assoc_in(db,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(badges_key,new cljs.core.Keyword(null,"replies","replies",-1389888974)),true);
} else {
return db;
}
}));

//# sourceMappingURL=oc.web.stores.org.js.map
