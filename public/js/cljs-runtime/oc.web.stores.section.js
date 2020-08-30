goog.provide('oc.web.stores.section');
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"section","section",-300141526),(function (db,p__43842){
var vec__43843 = p__43842;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43843,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43843,(1),null);
var section_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43843,(2),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43843,(3),null);
var section_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43843,(4),null);
var db_loading = (cljs.core.truth_(new cljs.core.Keyword(null,"is-loaded","is-loaded",1456967468).cljs$core$IFn$_invoke$arity$1(section_data))?cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(db,new cljs.core.Keyword(null,"loading","loading",-737050189)):db);
var with_entries = new cljs.core.Keyword(null,"entries","entries",-86943161).cljs$core$IFn$_invoke$arity$1(section_data);
var fixed_section_data = oc.web.utils.activity.parse_board(section_data,oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$1(db),oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.follow_boards_list.cljs$core$IFn$_invoke$arity$0(),sort_type);
var old_section_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,oc.web.dispatcher.board_data_key.cljs$core$IFn$_invoke$arity$3(org_slug,new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(section_data),sort_type));
var with_current_edit = (cljs.core.truth_((function (){var and__4115__auto__ = new cljs.core.Keyword(null,"is-loaded","is-loaded",1456967468).cljs$core$IFn$_invoke$arity$1(section_data);
if(cljs.core.truth_(and__4115__auto__)){
return new cljs.core.Keyword(null,"entry-editing","entry-editing",-1938994964).cljs$core$IFn$_invoke$arity$1(db);
} else {
return and__4115__auto__;
}
})())?old_section_data:fixed_section_data);
var posts_key = oc.web.dispatcher.posts_data_key(org_slug);
var merged_items = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,posts_key),new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(fixed_section_data)], 0));
var with_merged_items = (cljs.core.truth_(with_entries)?cljs.core.assoc_in(db_loading,posts_key,merged_items):db_loading);
var is_drafts_board_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(section_data),oc.web.lib.utils.default_drafts_board_slug);
var org_drafts_count_key = cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_data_key(org_slug),new cljs.core.Keyword(null,"drafts-count","drafts-count",-1710494641)));
var ndb = cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc_in(with_merged_items,oc.web.dispatcher.board_data_key.cljs$core$IFn$_invoke$arity$3(org_slug,new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(section_data),sort_type),cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(with_current_edit,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159))),org_drafts_count_key,(function (p1__43840_SHARP_){
if(is_drafts_board_QMARK_){
return oc.web.utils.org.disappearing_count_value(p1__43840_SHARP_,new cljs.core.Keyword(null,"total-count","total-count",-1999441386).cljs$core$IFn$_invoke$arity$1(section_data));
} else {
return p1__43840_SHARP_;
}
}));
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(ndb,oc.web.dispatcher.user_notifications_key(org_slug),(function (p1__43841_SHARP_){
return oc.web.utils.notification.fix_notifications.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([ndb,p1__43841_SHARP_], 0));
}));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"section-change","section-change",1643975649),(function (db,p__43846){
var vec__43847 = p__43846;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43847,(0),null);
var section_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43847,(1),null);
return db;
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("section-edit-save","finish","section-edit-save/finish",-757307318),(function (db,p__43852){
var vec__43853 = p__43852;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43853,(0),null);
var section_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43853,(1),null);
var org_slug = oc.web.lib.utils.section_org_slug(section_data);
var section_slug = new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(section_data);
var board_key = oc.web.dispatcher.board_data_key.cljs$core$IFn$_invoke$arity$2(org_slug,section_slug);
var fixed_section_data = oc.web.utils.activity.parse_board.cljs$core$IFn$_invoke$arity$4(section_data,oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$1(db),oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.follow_boards_list.cljs$core$IFn$_invoke$arity$0());
var old_board_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,board_key);
var next_board_data = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([fixed_section_data,cljs.core.select_keys(old_board_data,new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897),new cljs.core.Keyword(null,"items-to-render","items-to-render",660219762),new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159),new cljs.core.Keyword(null,"links","links",-654507394)], null))], 0));
var ndb = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core.assoc_in(db,board_key,next_board_data),new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167));
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(ndb,oc.web.dispatcher.user_notifications_key(org_slug),(function (p1__43850_SHARP_){
return oc.web.utils.notification.fix_notifications.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([ndb,p1__43850_SHARP_], 0));
}));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("section-edit","dismiss","section-edit/dismiss",1016461243),(function (db,p__43863){
var vec__43864 = p__43863;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43864,(0),null);
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(db,new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"private-section-user-add","private-section-user-add",1657184076),(function (db,p__43870){
var vec__43871 = p__43870;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43871,(0),null);
var user = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43871,(1),null);
var user_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43871,(2),null);
var section_data = new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167).cljs$core$IFn$_invoke$arity$1(db);
var current_notifications = cljs.core.filterv((function (p1__43867_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__43867_SHARP_),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user));
}),new cljs.core.Keyword(null,"private-notifications","private-notifications",2119502043).cljs$core$IFn$_invoke$arity$1(section_data));
var current_authors = cljs.core.filterv((function (p1__43868_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(p1__43868_SHARP_,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user));
}),new cljs.core.Keyword(null,"authors","authors",2063018172).cljs$core$IFn$_invoke$arity$1(section_data));
var current_viewers = cljs.core.filterv((function (p1__43869_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(p1__43869_SHARP_,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user));
}),new cljs.core.Keyword(null,"viewers","viewers",-415894011).cljs$core$IFn$_invoke$arity$1(section_data));
var next_authors = (cljs.core.truth_((function (){var fexpr__43878 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"admin","admin",-1239101627),null,new cljs.core.Keyword(null,"author","author",2111686192),null], null), null);
return (fexpr__43878.cljs$core$IFn$_invoke$arity$1 ? fexpr__43878.cljs$core$IFn$_invoke$arity$1(user_type) : fexpr__43878.call(null,user_type));
})())?cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(current_authors,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user))):current_authors);
var next_viewers = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(user_type,new cljs.core.Keyword(null,"viewer","viewer",-783949853)))?cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(current_viewers,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user))):current_viewers);
var next_notifications = cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(current_notifications,user));
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([section_data,new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"has-changes","has-changes",-631476764),true,new cljs.core.Keyword(null,"authors","authors",2063018172),next_authors,new cljs.core.Keyword(null,"viewers","viewers",-415894011),next_viewers,new cljs.core.Keyword(null,"private-notifications","private-notifications",2119502043),next_notifications], null)], 0)));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"private-section-user-remove","private-section-user-remove",1608853385),(function (db,p__43887){
var vec__43888 = p__43887;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43888,(0),null);
var user = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43888,(1),null);
var section_data = new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167).cljs$core$IFn$_invoke$arity$1(db);
var private_notifications = cljs.core.filterv((function (p1__43880_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__43880_SHARP_),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user));
}),new cljs.core.Keyword(null,"private-notifications","private-notifications",2119502043).cljs$core$IFn$_invoke$arity$1(section_data));
var next_authors = cljs.core.filterv((function (p1__43881_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(p1__43881_SHARP_,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user));
}),new cljs.core.Keyword(null,"authors","authors",2063018172).cljs$core$IFn$_invoke$arity$1(section_data));
var next_viewers = cljs.core.filterv((function (p1__43882_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(p1__43882_SHARP_,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user));
}),new cljs.core.Keyword(null,"viewers","viewers",-415894011).cljs$core$IFn$_invoke$arity$1(section_data));
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([section_data,new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"has-changes","has-changes",-631476764),true,new cljs.core.Keyword(null,"authors","authors",2063018172),next_authors,new cljs.core.Keyword(null,"viewers","viewers",-415894011),next_viewers,new cljs.core.Keyword(null,"private-notifications","private-notifications",2119502043),private_notifications], null)], 0)));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("private-section-kick-out-self","finish","private-section-kick-out-self/finish",779691698),(function (db,p__43922){
var vec__43923 = p__43922;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43923,(0),null);
var success = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43923,(1),null);
if(cljs.core.truth_(success)){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(db,new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167));
} else {
return db;
}
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"section-delete","section-delete",-321340384),(function (db,p__43929){
var vec__43930 = p__43929;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43930,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43930,(1),null);
var section_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43930,(2),null);
var section_key = oc.web.dispatcher.board_key.cljs$core$IFn$_invoke$arity$2(org_slug,section_slug);
var org_sections_key = cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_data_key(org_slug),new cljs.core.Keyword(null,"boards","boards",1912049694)));
var remaining_sections = cljs.core.remove.cljs$core$IFn$_invoke$arity$2((function (p1__43926_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(p1__43926_SHARP_),section_slug);
}),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,org_sections_key));
var posts_key = oc.web.dispatcher.posts_data_key(org_slug);
var old_posts = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,posts_key);
var removed_posts = cljs.core.filterv((function (p){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(p),section_slug);
}),cljs.core.vals(old_posts));
var cmail_state = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,oc.web.dispatcher.cmail_state_key);
var first_editable_section = cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__43927_SHARP_){
if(cljs.core.not(new cljs.core.Keyword(null,"draft","draft",1421831058).cljs$core$IFn$_invoke$arity$1(p1__43927_SHARP_))){
return oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(p1__43927_SHARP_),"create"], 0));
} else {
return false;
}
}),cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"name","name",1843675177),remaining_sections)));
var next_db = (cljs.core.truth_((function (){var and__4115__auto__ = new cljs.core.Keyword(null,"collapsed","collapsed",-628494523).cljs$core$IFn$_invoke$arity$1(cmail_state);
if(cljs.core.truth_(and__4115__auto__)){
return first_editable_section;
} else {
return and__4115__auto__;
}
})())?cljs.core.assoc_in(cljs.core.assoc_in(db,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.cmail_state_key,new cljs.core.Keyword(null,"key","key",-1516042587)),oc.web.lib.utils.activity_uuid()),oc.web.dispatcher.cmail_data_key,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"board-name","board-name",-677515056),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(first_editable_section),new cljs.core.Keyword(null,"board-slug","board-slug",99003663),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(first_editable_section),new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803),new cljs.core.Keyword(null,"publisher-board","publisher-board",-1354523803).cljs$core$IFn$_invoke$arity$1(first_editable_section)], null)):db);
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.update_in.cljs$core$IFn$_invoke$arity$4(next_db,cljs.core.butlast(section_key),cljs.core.dissoc,cljs.core.last(section_key)),posts_key,cljs.core.zipmap(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),removed_posts),removed_posts)),org_sections_key,remaining_sections),new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("container","status","container/status",1617877110),(function (db,p__43940){
var vec__43941 = p__43940;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43941,(0),null);
var change_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43941,(1),null);
var replace_change_data_QMARK_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43941,(2),null);
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.stores.section",null,149,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Change status received:",change_data], null);
}),null)),null,-353847669);

if(cljs.core.truth_(change_data)){
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$1(db);
var old_change_data = oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$1(db);
var new_change_data = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([old_change_data,change_data], 0));
taoensso.timbre._log_BANG_.cljs$core$IFn$_invoke$arity$10(taoensso.timbre._STAR_config_STAR_,new cljs.core.Keyword(null,"debug","debug",-1608172596),"oc.web.stores.section",null,157,new cljs.core.Keyword(null,"p","p",151049309),new cljs.core.Keyword(null,"auto","auto",-566279492),(new cljs.core.Delay((function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Change status data:",new_change_data], null);
}),null)),null,-1121856780);

return cljs.core.assoc_in(db,oc.web.dispatcher.change_data_key(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data)),new_change_data);
} else {
return db;
}
}));
oc.web.stores.section.update_unseen_unread_remove = (function oc$web$stores$section$update_unseen_unread_remove(old_change_data,item_id,container_id,new_changes){
var old_container_change_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(old_change_data,container_id);
var old_unseen = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"unseen","unseen",1063275592).cljs$core$IFn$_invoke$arity$1(old_container_change_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.PersistentVector.EMPTY;
}
})();
var next_unseen = cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__43957_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(p1__43957_SHARP_,item_id);
}),old_unseen);
var old_unread = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"unread","unread",-1950424572).cljs$core$IFn$_invoke$arity$1(old_container_change_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.PersistentVector.EMPTY;
}
})();
var next_unread = cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__43958_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(p1__43958_SHARP_,item_id);
}),old_unread);
var next_container_change_data = (cljs.core.truth_(old_container_change_data)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$variadic(old_container_change_data,new cljs.core.Keyword(null,"unseen","unseen",1063275592),next_unseen,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"unread","unread",-1950424572),next_unread], 0)):new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"container-id","container-id",1274665684),container_id,new cljs.core.Keyword(null,"unseen","unseen",1063275592),next_unseen,new cljs.core.Keyword(null,"unread","unread",-1950424572),next_unread], null));
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(old_change_data,container_id,next_container_change_data);
});
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("item-delete","unseen","item-delete/unseen",-1112816887),(function (db,p__43960){
var vec__43961 = p__43960;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43961,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43961,(1),null);
var change_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43961,(2),null);
var item_id = new cljs.core.Keyword(null,"item-id","item-id",-1804511607).cljs$core$IFn$_invoke$arity$1(change_data);
var container_id = new cljs.core.Keyword(null,"container-id","container-id",1274665684).cljs$core$IFn$_invoke$arity$1(change_data);
var change_key = oc.web.dispatcher.change_data_key(org_slug);
var old_change_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,change_key);
return cljs.core.assoc_in(db,change_key,oc.web.stores.section.update_unseen_unread_remove(old_change_data,item_id,container_id,change_data));
}));
oc.web.stores.section.update_unseen_unread_add = (function oc$web$stores$section$update_unseen_unread_add(old_change_data,item_id,container_id,new_changes){
var old_container_change_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(old_change_data,container_id);
var old_unseen = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"unseen","unseen",1063275592).cljs$core$IFn$_invoke$arity$1(old_container_change_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.PersistentVector.EMPTY;
}
})();
var next_unseen = cljs.core.vec(cljs.core.seq(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(old_unseen,item_id)));
var old_unread = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"unread","unread",-1950424572).cljs$core$IFn$_invoke$arity$1(old_container_change_data);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.PersistentVector.EMPTY;
}
})();
var next_unread = cljs.core.vec(cljs.core.seq(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(old_unread,item_id)));
var next_container_change_data = (cljs.core.truth_(old_container_change_data)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$variadic(old_container_change_data,new cljs.core.Keyword(null,"unseen","unseen",1063275592),next_unseen,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"unread","unread",-1950424572),next_unread], 0)):new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"container-id","container-id",1274665684),container_id,new cljs.core.Keyword(null,"unseen","unseen",1063275592),next_unseen,new cljs.core.Keyword(null,"unread","unread",-1950424572),next_unread], null));
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(old_change_data,container_id,next_container_change_data);
});
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("item-add","unseen","item-add/unseen",168133967),(function (db,p__43970){
var vec__43971 = p__43970;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43971,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43971,(1),null);
var change_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43971,(2),null);
var item_id = new cljs.core.Keyword(null,"item-id","item-id",-1804511607).cljs$core$IFn$_invoke$arity$1(change_data);
var container_id = new cljs.core.Keyword(null,"container-id","container-id",1274665684).cljs$core$IFn$_invoke$arity$1(change_data);
var change_key = oc.web.dispatcher.change_data_key(org_slug);
var old_change_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,change_key);
return cljs.core.assoc_in(db,change_key,oc.web.stores.section.update_unseen_unread_add(old_change_data,item_id,container_id,change_data));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"section-more","section-more",1599322644),(function (db,p__43974){
var vec__43975 = p__43974;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43975,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43975,(1),null);
var board_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43975,(2),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43975,(3),null);
var container_key = oc.web.dispatcher.board_data_key.cljs$core$IFn$_invoke$arity$3(org_slug,board_slug,sort_type);
var container_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,container_key);
var next_container_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(container_data,new cljs.core.Keyword(null,"loading-more","loading-more",-1145525667),true);
return cljs.core.assoc_in(db,container_key,next_container_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("section-more","finish","section-more/finish",1628075413),(function (db,p__43980){
var vec__43981 = p__43980;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43981,(0),null);
var org__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43981,(1),null);
var board = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43981,(2),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43981,(3),null);
var direction = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43981,(4),null);
var next_board_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43981,(5),null);
if(cljs.core.truth_(next_board_data)){
var container_key = oc.web.dispatcher.board_data_key.cljs$core$IFn$_invoke$arity$3(org__$1,board,sort_type);
var container_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,container_key);
var posts_data_key = oc.web.dispatcher.posts_data_key(org__$1);
var old_posts = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,posts_data_key);
var prepare_board_data = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([next_board_data,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897),new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897).cljs$core$IFn$_invoke$arity$1(container_data),new cljs.core.Keyword(null,"old-links","old-links",340078773),new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(container_data)], null)], 0));
var fixed_posts_data = oc.web.utils.activity.parse_board(prepare_board_data,oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$1(db),oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$0(),oc.web.dispatcher.follow_boards_list.cljs$core$IFn$_invoke$arity$0(),direction);
var new_items_map = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([old_posts,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(fixed_posts_data)], 0));
var new_container_data = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(fixed_posts_data,new cljs.core.Keyword(null,"direction","direction",-633359395),direction),new cljs.core.Keyword(null,"loading-more","loading-more",-1145525667));
var ndb = cljs.core.assoc_in(cljs.core.assoc_in(db,container_key,new_container_data),posts_data_key,new_items_map);
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(ndb,oc.web.dispatcher.user_notifications_key(org__$1),(function (p1__43978_SHARP_){
return oc.web.utils.notification.fix_notifications.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([ndb,p1__43978_SHARP_], 0));
}));
} else {
return db;
}
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"setup-section-editing","setup-section-editing",-295480073),(function (db,p__43984){
var vec__43985 = p__43984;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43985,(0),null);
var board_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43985,(1),null);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"initial-section-editing","initial-section-editing",1587721238),board_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"item-move","item-move",-790509039),(function (db,p__43990){
var vec__43991 = p__43990;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43991,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43991,(1),null);
var change_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43991,(2),null);
var old_container_id = new cljs.core.Keyword(null,"old-container-id","old-container-id",-892178585).cljs$core$IFn$_invoke$arity$1(change_data);
var container_id = new cljs.core.Keyword(null,"container-id","container-id",1274665684).cljs$core$IFn$_invoke$arity$1(change_data);
var item_id = new cljs.core.Keyword(null,"item-id","item-id",-1804511607).cljs$core$IFn$_invoke$arity$1(change_data);
var change_key = oc.web.dispatcher.change_data_key(org_slug);
var old_change_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,change_key);
var old_container_change_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(old_change_data,old_container_id);
var is_unseen_QMARK_ = oc.web.lib.utils.in_QMARK_(new cljs.core.Keyword(null,"unseen","unseen",1063275592).cljs$core$IFn$_invoke$arity$1(old_container_change_data),item_id);
var is_unread_QMARK_ = oc.web.lib.utils.in_QMARK_(new cljs.core.Keyword(null,"unread","unread",-1950424572).cljs$core$IFn$_invoke$arity$1(old_container_change_data),item_id);
var next_old_unseen = cljs.core.filterv((function (p1__43988_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(p1__43988_SHARP_,item_id);
}),new cljs.core.Keyword(null,"unseen","unseen",1063275592).cljs$core$IFn$_invoke$arity$1(old_container_change_data));
var next_old_unread = cljs.core.filterv((function (p1__43989_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(p1__43989_SHARP_,item_id);
}),new cljs.core.Keyword(null,"unread","unread",-1950424572).cljs$core$IFn$_invoke$arity$1(old_container_change_data));
var next_old_container_change_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(old_container_change_data,new cljs.core.Keyword(null,"unseen","unseen",1063275592),next_old_unseen),new cljs.core.Keyword(null,"unread","unread",-1950424572),next_old_unread);
var new_container_change_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(old_change_data,container_id);
var next_new_unseen = cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"unseen","unseen",1063275592).cljs$core$IFn$_invoke$arity$1(new_container_change_data),(cljs.core.truth_(is_unseen_QMARK_)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [item_id], null):cljs.core.PersistentVector.EMPTY));
var next_new_unread = cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"unread","unread",-1950424572).cljs$core$IFn$_invoke$arity$1(new_container_change_data),(cljs.core.truth_(is_unread_QMARK_)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [item_id], null):cljs.core.PersistentVector.EMPTY));
var next_new_container_change_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(new_container_change_data,new cljs.core.Keyword(null,"unseen","unseen",1063275592),next_new_unseen),new cljs.core.Keyword(null,"unread","unread",-1950424572),next_new_unread);
var next_change_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(old_change_data,old_container_id,next_old_container_change_data),container_id,next_new_container_change_data);
return cljs.core.assoc_in(db,change_key,next_change_data);
}));

//# sourceMappingURL=oc.web.stores.section.js.map
