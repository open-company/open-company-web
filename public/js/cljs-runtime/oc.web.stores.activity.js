goog.provide('oc.web.stores.activity');
oc.web.stores.activity.item_from_entity = (function oc$web$stores$activity$item_from_entity(entry){
return cljs.core.select_keys(entry,oc.web.utils.activity.preserved_keys);
});
/**
 * Calculate the sort value as used on the server while quering the data.
 * 
 * This is a limited sort-value in respect of what the server is using. Since this is applied
 * only to posts the user publishes we can avoid all the unread and cap window related cals.
 */
oc.web.stores.activity.sort_value = (function oc$web$stores$activity$sort_value(sort_type,item){
return cljs.core.update.cljs$core$IFn$_invoke$arity$3(item,new cljs.core.Keyword(null,"sort-value","sort-value",-585546274),(function (v){
var sort_key = (function (){var G__43570 = sort_type;
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Symbol("dispatcher","recent-activity-sort","dispatcher/recent-activity-sort",-1582406011,null),G__43570)){
return new cljs.core.Keyword(null,"last-activity-at","last-activity-at",670511998);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"bookmarked-at","bookmarked-at",1451784060),G__43570)){
return new cljs.core.Keyword(null,"bookmarked-at","bookmarked-at",1451784060);
} else {
return new cljs.core.Keyword(null,"published-at","published-at",249684621);

}
}
})();
var activity_data = ((cljs.core.contains_QMARK_(item,sort_key))?item:oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(item)));
return oc.web.lib.utils.js_date.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.get.cljs$core$IFn$_invoke$arity$2(activity_data,sort_key)], 0)).getTime();
}));
});
/**
 * Given an activity map adds or remove it from the all-posts list of posts depending on the activity
 * status
 */
oc.web.stores.activity.add_remove_item_from_all_posts = (function oc$web$stores$activity$add_remove_item_from_all_posts(db,org_slug,activity_data){
if(cljs.core.truth_(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data))){
var is_published_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(activity_data),"published");
var ap_rp_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,new cljs.core.Keyword(null,"all-posts","all-posts",-1285476533),oc.web.dispatcher.recently_posted_sort);
var ap_ra_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,new cljs.core.Keyword(null,"all-posts","all-posts",-1285476533),oc.web.dispatcher.recent_activity_sort);
var old_ap_rp_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,ap_rp_key);
var old_ap_ra_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,ap_ra_key);
var old_ap_rp_data_posts = cljs.core.get.cljs$core$IFn$_invoke$arity$2(old_ap_rp_data,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897));
var old_ap_ra_data_posts = cljs.core.get.cljs$core$IFn$_invoke$arity$2(old_ap_ra_data,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897));
var ap_rp_without_uuid = cljs.core.filterv((function (p1__43571_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__43571_SHARP_),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));
}),old_ap_rp_data_posts);
var ap_ra_without_uuid = cljs.core.filterv((function (p1__43572_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__43572_SHARP_),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));
}),old_ap_ra_data_posts);
var new_ap_rp_list = cljs.core.vec(((is_published_QMARK_)?cljs.core.conj.cljs$core$IFn$_invoke$arity$2(ap_rp_without_uuid,oc.web.stores.activity.item_from_entity(activity_data)):ap_rp_without_uuid));
var new_ap_ra_list = cljs.core.vec(((is_published_QMARK_)?cljs.core.conj.cljs$core$IFn$_invoke$arity$2(ap_ra_without_uuid,oc.web.stores.activity.item_from_entity(activity_data)):ap_ra_without_uuid));
var new_ap_rp_with_sort_value = cljs.core.map.cljs$core$IFn$_invoke$arity$2(cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.stores.activity.sort_value,oc.web.dispatcher.recently_posted_sort),new_ap_rp_list);
var new_ap_ra_with_sort_value = cljs.core.map.cljs$core$IFn$_invoke$arity$2(cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.stores.activity.sort_value,oc.web.dispatcher.recent_activity_sort),new_ap_rp_list);
var sorted_new_ap_rp_uuids = cljs.core.reverse(cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"sort-value","sort-value",-585546274),new_ap_rp_with_sort_value));
var sorted_new_ap_ra_uuids = cljs.core.reverse(cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"sort-value","sort-value",-585546274),new_ap_ra_with_sort_value));
var next_ap_rp_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(old_ap_rp_data,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897),sorted_new_ap_rp_uuids);
var next_ap_ra_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(old_ap_ra_data,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897),sorted_new_ap_ra_uuids);
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$2(db,org_slug);
var active_users = oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org_slug,db);
var parsed_ap_rp_data = (cljs.core.truth_(old_ap_rp_data)?cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$5(next_ap_rp_data,cljs.core.PersistentArrayMap.EMPTY,org_data,active_users,oc.web.dispatcher.recently_posted_sort),new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159)):null);
var parsed_ap_ra_data = (cljs.core.truth_(old_ap_ra_data)?cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$5(next_ap_ra_data,cljs.core.PersistentArrayMap.EMPTY,org_data,active_users,oc.web.dispatcher.recent_activity_sort),new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159)):null);
var tdb = db;
var tdb__$1 = (cljs.core.truth_(old_ap_rp_data)?cljs.core.assoc_in(tdb,ap_rp_key,parsed_ap_rp_data):tdb);
if(cljs.core.truth_(old_ap_ra_data)){
return cljs.core.assoc_in(tdb__$1,ap_ra_key,parsed_ap_ra_data);
} else {
return tdb__$1;
}
} else {
return db;
}
});
/**
 * Given an activity map adds or remove it from it's board's list of posts depending on the activity status
 */
oc.web.stores.activity.add_remove_item_from_board = (function oc$web$stores$activity$add_remove_item_from_board(db,org_slug,activity_data){
if(cljs.core.truth_(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data))){
var is_published_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(activity_data),"published");
var rp_board_data_key = oc.web.dispatcher.board_data_key.cljs$core$IFn$_invoke$arity$3(org_slug,new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(activity_data),oc.web.dispatcher.recently_posted_sort);
var ra_board_data_key = oc.web.dispatcher.board_data_key.cljs$core$IFn$_invoke$arity$3(org_slug,new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(activity_data),oc.web.dispatcher.recent_activity_sort);
var rp_old_board_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,rp_board_data_key);
var ra_old_board_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,ra_board_data_key);
var rp_old_board_data_posts = cljs.core.get.cljs$core$IFn$_invoke$arity$2(rp_old_board_data,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897));
var ra_old_board_data_posts = cljs.core.get.cljs$core$IFn$_invoke$arity$2(ra_old_board_data,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897));
var rp_board_without_uuid = cljs.core.filterv((function (p1__43573_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__43573_SHARP_),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));
}),rp_old_board_data_posts);
var ra_board_without_uuid = cljs.core.filterv((function (p1__43574_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__43574_SHARP_),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));
}),ra_old_board_data_posts);
var new_rp_list = cljs.core.vec(((is_published_QMARK_)?cljs.core.conj.cljs$core$IFn$_invoke$arity$2(rp_board_without_uuid,oc.web.stores.activity.item_from_entity(activity_data)):rp_board_without_uuid));
var new_ra_list = cljs.core.vec(((is_published_QMARK_)?cljs.core.conj.cljs$core$IFn$_invoke$arity$2(ra_board_without_uuid,oc.web.stores.activity.item_from_entity(activity_data)):ra_board_without_uuid));
var new_rp_with_sort_value = cljs.core.map.cljs$core$IFn$_invoke$arity$2(cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.stores.activity.sort_value,oc.web.dispatcher.recently_posted_sort),new_rp_list);
var new_ra_with_sort_value = cljs.core.map.cljs$core$IFn$_invoke$arity$2(cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.stores.activity.sort_value,oc.web.dispatcher.recent_activity_sort),new_rp_list);
var sorted_new_rp_uuids = cljs.core.reverse(cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"sort-value","sort-value",-585546274),new_rp_with_sort_value));
var sorted_new_ra_uuids = cljs.core.reverse(cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"sort-value","sort-value",-585546274),new_ra_with_sort_value));
var change_data = oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$2(db,org_slug);
var active_users = oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org_slug,db);
var follow_boards_list = oc.web.dispatcher.follow_boards_list.cljs$core$IFn$_invoke$arity$2(org_slug,db);
var parsed_rp_board_data = (cljs.core.truth_(rp_old_board_data)?cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(oc.web.utils.activity.parse_board(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(rp_old_board_data,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897),sorted_new_rp_uuids),change_data,active_users,follow_boards_list,oc.web.dispatcher.recently_posted_sort),new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159)):null);
var parsed_ra_board_data = (cljs.core.truth_(ra_old_board_data)?cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(oc.web.utils.activity.parse_board(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(ra_old_board_data,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897),sorted_new_ra_uuids),change_data,active_users,follow_boards_list,oc.web.dispatcher.recent_activity_sort),new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159)):null);
var tdb = db;
var tdb__$1 = (cljs.core.truth_(rp_old_board_data)?cljs.core.assoc_in(tdb,rp_board_data_key,parsed_rp_board_data):tdb);
if(cljs.core.truth_(ra_old_board_data)){
return cljs.core.assoc_in(tdb__$1,ra_board_data_key,parsed_ra_board_data);
} else {
return tdb__$1;
}
} else {
return db;
}
});
/**
 * Given an activity map adds or remove it from the bookmarks list of posts.
 */
oc.web.stores.activity.add_remove_item_from_bookmarks = (function oc$web$stores$activity$add_remove_item_from_bookmarks(db,org_slug,activity_data){
if(cljs.core.truth_(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data))){
var is_bookmark_QMARK_ = ((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(activity_data),"draft"))?new cljs.core.Keyword(null,"bookmarked-at","bookmarked-at",1451784060).cljs$core$IFn$_invoke$arity$1(activity_data):false);
var bm_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$2(org_slug,new cljs.core.Keyword(null,"bookmarks","bookmarks",1877375283));
var old_bm_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,bm_key);
var old_bm_data_posts = cljs.core.get.cljs$core$IFn$_invoke$arity$2(old_bm_data,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897));
var bm_without_item = cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__43575_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__43575_SHARP_),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));
}),old_bm_data_posts);
var new_bm_items = cljs.core.vec((cljs.core.truth_(is_bookmark_QMARK_)?cljs.core.conj.cljs$core$IFn$_invoke$arity$2(bm_without_item,oc.web.stores.activity.item_from_entity(activity_data)):bm_without_item));
var with_bookmarked_at = cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__43576_SHARP_){
var item = p1__43576_SHARP_;
var item__$1 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(item,new cljs.core.Keyword(null,"bookmarked-at","bookmarked-at",1451784060),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(item),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data)))?new cljs.core.Keyword(null,"bookmarked-at","bookmarked-at",1451784060).cljs$core$IFn$_invoke$arity$1(activity_data):new cljs.core.Keyword(null,"bookmarked-at","bookmarked-at",1451784060).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(item)))));
var item__$2 = oc.web.stores.activity.sort_value(new cljs.core.Keyword(null,"bookmarked-at","bookmarked-at",1451784060),item__$1);
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(item__$2,new cljs.core.Keyword(null,"bookmarked-at","bookmarked-at",1451784060));
}),new_bm_items);
var sorted_new_bm_posts = cljs.core.vec(cljs.core.reverse(cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"sort-value","sort-value",-585546274),with_bookmarked_at)));
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$2(db,org_slug);
var change_data = oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$2(db,org_slug);
var active_users = oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org_slug,db);
var next_bm_data = (cljs.core.truth_(old_bm_data)?cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$5(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(old_bm_data,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897),sorted_new_bm_posts),change_data,org_data,active_users,oc.web.dispatcher.recently_posted_sort),new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159)):null);
return cljs.core.assoc_in(db,bm_key,next_bm_data);
} else {
return db;
}
});
oc.web.stores.activity.add_published_post_to_home = (function oc$web$stores$activity$add_published_post_to_home(db,org_slug,activity_data){
var fl_rp_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,new cljs.core.Keyword(null,"following","following",-2049193617),oc.web.dispatcher.recently_posted_sort);
var fl_ra_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,new cljs.core.Keyword(null,"following","following",-2049193617),oc.web.dispatcher.recent_activity_sort);
var old_fl_rp_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,fl_rp_key);
var old_fl_ra_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,fl_ra_key);
var activity_item = oc.web.stores.activity.item_from_entity(activity_data);
var first_rp_item = cljs.core.first(new cljs.core.Keyword(null,"items-to-render","items-to-render",660219762).cljs$core$IFn$_invoke$arity$1(old_fl_rp_data));
var first_ra_item = cljs.core.first(new cljs.core.Keyword(null,"items-to-render","items-to-render",660219762).cljs$core$IFn$_invoke$arity$1(old_fl_ra_data));
var rp_activity_item = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(activity_item,new cljs.core.Keyword(null,"container-seen-at","container-seen-at",-1262643681),new cljs.core.Keyword(null,"last-seen-at","last-seen-at",1929467667).cljs$core$IFn$_invoke$arity$1(old_fl_rp_data)),new cljs.core.Keyword(null,"unseen","unseen",1063275592),false),new cljs.core.Keyword(null,"unread","unread",-1950424572),false),new cljs.core.Keyword(null,"open-item","open-item",-1938301269),true),new cljs.core.Keyword(null,"close-item","close-item",-38717813),cljs.core.not(oc.web.utils.activity.entry_QMARK_(first_rp_item)));
var ra_activity_item = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(activity_item,new cljs.core.Keyword(null,"container-seen-at","container-seen-at",-1262643681),new cljs.core.Keyword(null,"last-seen-at","last-seen-at",1929467667).cljs$core$IFn$_invoke$arity$1(old_fl_ra_data)),new cljs.core.Keyword(null,"unseen","unseen",1063275592),false),new cljs.core.Keyword(null,"open-item","open-item",-1938301269),true),new cljs.core.Keyword(null,"close-item","close-item",-38717813),cljs.core.not(oc.web.utils.activity.entry_QMARK_(first_ra_item)));
var new_fl_rp_data = (cljs.core.truth_(old_fl_rp_data)?cljs.core.update.cljs$core$IFn$_invoke$arity$3(cljs.core.update.cljs$core$IFn$_invoke$arity$3(cljs.core.update.cljs$core$IFn$_invoke$arity$3(old_fl_rp_data,new cljs.core.Keyword(null,"sort-value","sort-value",-585546274),cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.stores.activity.sort_value,oc.web.dispatcher.recently_posted_sort)),new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897),(function (p1__43577_SHARP_){
return cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [activity_item], null),p1__43577_SHARP_));
})),new cljs.core.Keyword(null,"items-to-render","items-to-render",660219762),(function (p1__43578_SHARP_){
return cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [rp_activity_item], null),p1__43578_SHARP_));
})):null);
var new_fl_ra_data = (cljs.core.truth_(old_fl_ra_data)?cljs.core.update.cljs$core$IFn$_invoke$arity$3(cljs.core.update.cljs$core$IFn$_invoke$arity$3(cljs.core.update.cljs$core$IFn$_invoke$arity$3(old_fl_ra_data,new cljs.core.Keyword(null,"sort-value","sort-value",-585546274),cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.stores.activity.sort_value,oc.web.dispatcher.recent_activity_sort)),new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897),(function (p1__43579_SHARP_){
return cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [activity_item], null),p1__43579_SHARP_));
})),new cljs.core.Keyword(null,"items-to-render","items-to-render",660219762),(function (p1__43580_SHARP_){
return cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [ra_activity_item], null),p1__43580_SHARP_));
})):null);
var tdb = db;
var tdb__$1 = (cljs.core.truth_(old_fl_rp_data)?cljs.core.assoc_in(tdb,fl_rp_key,new_fl_rp_data):tdb);
if(cljs.core.truth_(old_fl_ra_data)){
return cljs.core.assoc_in(tdb__$1,fl_ra_key,new_fl_ra_data);
} else {
return tdb__$1;
}
});
/**
 * Given an activity map adds or remove it from the bookmarks list of posts.
 */
oc.web.stores.activity.add_remove_item_from_follow = (function oc$web$stores$activity$add_remove_item_from_follow(db,org_slug,activity_data,following_container_QMARK_){
if(cljs.core.truth_(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data))){
var follow_list = oc.web.dispatcher.follow_list.cljs$core$IFn$_invoke$arity$2(org_slug,db);
var publisher_id = new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"publisher","publisher",-153364540).cljs$core$IFn$_invoke$arity$1(activity_data));
var include_activity_QMARK_ = ((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(activity_data),"draft"))?(function (){var or__4126__auto__ = (function (){var and__4115__auto__ = following_container_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
var or__4126__auto__ = (function (){var fexpr__43608 = cljs.core.set(new cljs.core.Keyword(null,"publisher-uuids","publisher-uuids",1855461704).cljs$core$IFn$_invoke$arity$1(follow_list));
return (fexpr__43608.cljs$core$IFn$_invoke$arity$1 ? fexpr__43608.cljs$core$IFn$_invoke$arity$1(publisher_id) : fexpr__43608.call(null,publisher_id));
})();
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = (function (){var G__43612 = new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127).cljs$core$IFn$_invoke$arity$1(activity_data);
var fexpr__43611 = cljs.core.set(new cljs.core.Keyword(null,"board-uuids","board-uuids",515371926).cljs$core$IFn$_invoke$arity$1(follow_list));
return (fexpr__43611.cljs$core$IFn$_invoke$arity$1 ? fexpr__43611.cljs$core$IFn$_invoke$arity$1(G__43612) : fexpr__43611.call(null,G__43612));
})();
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(publisher_id,oc.web.lib.jwt.user_id());
}
}
} else {
return and__4115__auto__;
}
})();
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return ((cljs.core.not(following_container_QMARK_)) && (cljs.core.not((function (){var fexpr__43616 = cljs.core.set(new cljs.core.Keyword(null,"publisher-uuids","publisher-uuids",1855461704).cljs$core$IFn$_invoke$arity$1(follow_list));
return (fexpr__43616.cljs$core$IFn$_invoke$arity$1 ? fexpr__43616.cljs$core$IFn$_invoke$arity$1(publisher_id) : fexpr__43616.call(null,publisher_id));
})())) && (cljs.core.not((function (){var G__43618 = new cljs.core.Keyword(null,"board-uuids","board-uuids",515371926).cljs$core$IFn$_invoke$arity$1(activity_data);
var fexpr__43617 = cljs.core.set(new cljs.core.Keyword(null,"board-uuids","board-uuids",515371926).cljs$core$IFn$_invoke$arity$1(follow_list));
return (fexpr__43617.cljs$core$IFn$_invoke$arity$1 ? fexpr__43617.cljs$core$IFn$_invoke$arity$1(G__43618) : fexpr__43617.call(null,G__43618));
})())) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(publisher_id,oc.web.lib.jwt.user_id())));
}
})():false);
var container_slug = (cljs.core.truth_(following_container_QMARK_)?new cljs.core.Keyword(null,"following","following",-2049193617):new cljs.core.Keyword(null,"unfollowing","unfollowing",-1076165830));
var fl_rp_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,container_slug,oc.web.dispatcher.recently_posted_sort);
var fl_ra_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,container_slug,oc.web.dispatcher.recent_activity_sort);
var old_fl_rp_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,fl_rp_key);
var old_fl_ra_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,fl_ra_key);
var old_fl_rp_data_posts = cljs.core.get.cljs$core$IFn$_invoke$arity$2(old_fl_rp_data,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897));
var old_fl_ra_data_posts = cljs.core.get.cljs$core$IFn$_invoke$arity$2(old_fl_ra_data,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897));
var fl_rp_without_uuid = cljs.core.filterv((function (p1__43581_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__43581_SHARP_),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));
}),old_fl_rp_data_posts);
var fl_ra_without_uuid = cljs.core.filterv((function (p1__43582_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__43582_SHARP_),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));
}),old_fl_ra_data_posts);
var new_fl_rp_uuids = cljs.core.vec(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(following_container_QMARK_,include_activity_QMARK_))?cljs.core.conj.cljs$core$IFn$_invoke$arity$2(fl_rp_without_uuid,oc.web.stores.activity.item_from_entity(activity_data)):fl_rp_without_uuid));
var new_fl_ra_uuids = cljs.core.vec(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(following_container_QMARK_,include_activity_QMARK_))?cljs.core.conj.cljs$core$IFn$_invoke$arity$2(fl_ra_without_uuid,oc.web.stores.activity.item_from_entity(activity_data)):fl_ra_without_uuid));
var new_fl_rp_with_sort_value = cljs.core.map.cljs$core$IFn$_invoke$arity$2(cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.stores.activity.sort_value,oc.web.dispatcher.recently_posted_sort),new_fl_rp_uuids);
var new_fl_ra_with_sort_value = cljs.core.map.cljs$core$IFn$_invoke$arity$2(cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.stores.activity.sort_value,oc.web.dispatcher.recent_activity_sort),new_fl_ra_uuids);
var sorted_new_fl_rp_uuids = cljs.core.reverse(cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"sort-value","sort-value",-585546274),new_fl_rp_with_sort_value));
var sorted_new_fl_ra_uuids = cljs.core.reverse(cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"sort-value","sort-value",-585546274),new_fl_ra_with_sort_value));
var next_fl_rp_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(old_fl_rp_data,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897),sorted_new_fl_rp_uuids);
var next_fl_ra_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(old_fl_ra_data,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897),sorted_new_fl_ra_uuids);
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$2(db,org_slug);
var active_users = oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org_slug,db);
var parsed_fl_rp_data = (cljs.core.truth_(old_fl_rp_data)?cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$5(next_fl_rp_data,cljs.core.PersistentArrayMap.EMPTY,org_data,active_users,oc.web.dispatcher.recently_posted_sort),new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159)):null);
var parsed_fl_ra_data = (cljs.core.truth_(old_fl_ra_data)?cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$5(next_fl_ra_data,cljs.core.PersistentArrayMap.EMPTY,org_data,active_users,oc.web.dispatcher.recent_activity_sort),new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159)):null);
var tdb = db;
var tdb__$1 = (cljs.core.truth_(old_fl_rp_data)?cljs.core.assoc_in(tdb,fl_rp_key,parsed_fl_rp_data):tdb);
if(cljs.core.truth_(old_fl_ra_data)){
return cljs.core.assoc_in(tdb__$1,fl_ra_key,parsed_fl_ra_data);
} else {
return tdb__$1;
}
} else {
return db;
}
});
/**
 * Given an activity map adds or remove it from it's contributions' list of posts depending on the activity status
 */
oc.web.stores.activity.add_remove_item_from_contributions = (function oc$web$stores$activity$add_remove_item_from_contributions(db,org_slug,activity_data){
var contributions_list_key = oc.web.dispatcher.contributions_list_key(org_slug);
var map__43621 = activity_data;
var map__43621__$1 = (((((!((map__43621 == null))))?(((((map__43621.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43621.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43621):map__43621);
var map__43622 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43621__$1,new cljs.core.Keyword(null,"publisher","publisher",-153364540));
var map__43622__$1 = (((((!((map__43622 == null))))?(((((map__43622.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43622.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43622):map__43622);
var author_uuid = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43622__$1,new cljs.core.Keyword(null,"user-id","user-id",-206822291));
if(cljs.core.truth_((function (){var and__4115__auto__ = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data);
if(cljs.core.truth_(and__4115__auto__)){
return ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(activity_data),"published")) && (cljs.core.contains_QMARK_(cljs.core.get.cljs$core$IFn$_invoke$arity$2(db,contributions_list_key),author_uuid)));
} else {
return and__4115__auto__;
}
})())){
var rp_contributions_data_key = oc.web.dispatcher.contributions_data_key.cljs$core$IFn$_invoke$arity$3(org_slug,author_uuid,oc.web.dispatcher.recently_posted_sort);
var ra_contributions_data_key = oc.web.dispatcher.contributions_data_key.cljs$core$IFn$_invoke$arity$3(org_slug,author_uuid,oc.web.dispatcher.recent_activity_sort);
var rp_old_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,rp_contributions_data_key);
var ra_old_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,ra_contributions_data_key);
var rp_old_data_posts = cljs.core.get.cljs$core$IFn$_invoke$arity$2(rp_old_data,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897));
var ra_old_data_posts = cljs.core.get.cljs$core$IFn$_invoke$arity$2(ra_old_data,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897));
var rp_without_uuid = cljs.core.filterv((function (p1__43619_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__43619_SHARP_),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));
}),rp_old_data_posts);
var ra_without_uuid = cljs.core.filterv((function (p1__43620_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__43620_SHARP_),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));
}),ra_old_data_posts);
var rp_new_uuids = cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(rp_without_uuid,oc.web.stores.activity.item_from_entity(activity_data)));
var ra_new_uuids = cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(ra_without_uuid,oc.web.stores.activity.item_from_entity(activity_data)));
var rp_with_sort_value = cljs.core.map.cljs$core$IFn$_invoke$arity$2(cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.stores.activity.sort_value,oc.web.dispatcher.recently_posted_sort),rp_new_uuids);
var ra_with_sort_value = cljs.core.map.cljs$core$IFn$_invoke$arity$2(cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.stores.activity.sort_value,oc.web.dispatcher.recent_activity_sort),ra_new_uuids);
var rp_sorted_new_uuids = cljs.core.reverse(cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"sort-value","sort-value",-585546274),rp_with_sort_value));
var ra_sorted_new_uuids = cljs.core.reverse(cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"sort-value","sort-value",-585546274),ra_with_sort_value));
var rp_new_posts_list = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(rp_old_data,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897),rp_sorted_new_uuids);
var ra_new_posts_list = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(ra_old_data,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897),ra_sorted_new_uuids);
var change_data = oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$2(db,org_slug);
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$2(db,org_slug);
var active_users = oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org_slug,db);
var follow_publishers_list = oc.web.dispatcher.follow_publishers_list.cljs$core$IFn$_invoke$arity$2(org_slug,db);
var parsed_rp_data = (cljs.core.truth_(rp_old_data)?(function (){
oc.web.utils.activity.parse_contributions(rp_new_posts_list,change_data,org_data,active_users,follow_publishers_list,oc.web.dispatcher.recently_posted_sort);

return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159));
})()
:null);
var parsed_ra_data = (cljs.core.truth_(ra_old_data)?cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(oc.web.utils.activity.parse_contributions(ra_new_posts_list,change_data,org_data,active_users,follow_publishers_list,oc.web.dispatcher.recent_activity_sort),new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159)):null);
var tdb = db;
var tdb__$1 = (cljs.core.truth_(rp_old_data)?cljs.core.assoc_in(tdb,rp_contributions_data_key,parsed_rp_data):tdb);
if(cljs.core.truth_(ra_old_data)){
return cljs.core.assoc_in(tdb__$1,ra_contributions_data_key,parsed_ra_data);
} else {
return tdb__$1;
}
} else {
return db;
}
});
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("entry-edit","dismiss","entry-edit/dismiss",-1867820720),(function (db,p__43627){
var vec__43628 = p__43627;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43628,(0),null);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(db,new cljs.core.Keyword(null,"entry-editing","entry-editing",-1938994964)),new cljs.core.Keyword(null,"entry-edit-dissmissing","entry-edit-dissmissing",-1058375764),true);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"entry-toggle-save-on-exit","entry-toggle-save-on-exit",-963399746),(function (db,p__43634){
var vec__43635 = p__43634;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43635,(0),null);
var enabled_QMARK_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43635,(1),null);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"entry-save-on-exit","entry-save-on-exit",-1377879888),enabled_QMARK_);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"activity-add-attachment","activity-add-attachment",-637529825),(function (db,p__43638){
var vec__43640 = p__43638;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43640,(0),null);
var dispatch_input_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43640,(1),null);
var attachment_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43640,(2),null);
var old_attachments = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"attachments","attachments",-1535547830).cljs$core$IFn$_invoke$arity$1((dispatch_input_key.cljs$core$IFn$_invoke$arity$1 ? dispatch_input_key.cljs$core$IFn$_invoke$arity$1(db) : dispatch_input_key.call(null,db)));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.PersistentVector.EMPTY;
}
})();
var next_attachments = cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(old_attachments,attachment_data));
return cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [dispatch_input_key,new cljs.core.Keyword(null,"attachments","attachments",-1535547830)], null),next_attachments);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"activity-remove-attachment","activity-remove-attachment",1236800634),(function (db,p__43644){
var vec__43645 = p__43644;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43645,(0),null);
var dispatch_input_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43645,(1),null);
var attachment_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43645,(2),null);
var old_attachments = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"attachments","attachments",-1535547830).cljs$core$IFn$_invoke$arity$1((dispatch_input_key.cljs$core$IFn$_invoke$arity$1 ? dispatch_input_key.cljs$core$IFn$_invoke$arity$1(db) : dispatch_input_key.call(null,db)));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.PersistentVector.EMPTY;
}
})();
var next_attachments = cljs.core.filterv((function (p1__43643_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"file-url","file-url",-863738963).cljs$core$IFn$_invoke$arity$1(p1__43643_SHARP_),new cljs.core.Keyword(null,"file-url","file-url",-863738963).cljs$core$IFn$_invoke$arity$1(attachment_data));
}),old_attachments);
return cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [dispatch_input_key,new cljs.core.Keyword(null,"attachments","attachments",-1535547830)], null),next_attachments);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"entry-clear-local-cache","entry-clear-local-cache",-1029730143),(function (db,p__43648){
var vec__43649 = p__43648;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43649,(0),null);
var edit_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43649,(1),null);
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(db,new cljs.core.Keyword(null,"entry-save-on-exit","entry-save-on-exit",-1377879888));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"entry-save","entry-save",-1431215303),(function (db,p__43652){
var vec__43653 = p__43652;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43653,(0),null);
var edit_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43653,(1),null);
return cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [edit_key,new cljs.core.Keyword(null,"loading","loading",-737050189)], null),true);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("entry-save","finish","entry-save/finish",1157760970),(function (db,p__43656){
var vec__43657 = p__43656;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43657,(0),null);
var activity_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43657,(1),null);
var edit_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43657,(2),null);
var org_slug = oc.web.lib.utils.post_org_slug(activity_data);
var board_slug = new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(activity_data);
var activity_key = oc.web.dispatcher.activity_key(org_slug,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));
var activity_board_data = oc.web.dispatcher.board_data.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([db,org_slug,board_slug], 0));
var fixed_activity_data = oc.web.utils.activity.parse_entry.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([activity_data,activity_board_data,oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$2(db,org_slug)], 0));
var next_db = cljs.core.assoc_in(db,activity_key,fixed_activity_data);
var with_edited_key = (cljs.core.truth_(edit_key)?cljs.core.update_in.cljs$core$IFn$_invoke$arity$4(next_db,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [edit_key], null),cljs.core.dissoc,new cljs.core.Keyword(null,"loading","loading",-737050189)):next_db);
var without_entry_save_on_exit = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(with_edited_key,new cljs.core.Keyword(null,"entry-toggle-save-on-exit","entry-toggle-save-on-exit",-963399746));
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(without_entry_save_on_exit,new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("entry-save","failed","entry-save/failed",613328806),(function (db,p__43660){
var vec__43661 = p__43660;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43661,(0),null);
var edit_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43661,(1),null);
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$5(cljs.core.update_in.cljs$core$IFn$_invoke$arity$4(db,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [edit_key], null),cljs.core.dissoc,new cljs.core.Keyword(null,"loading","loading",-737050189)),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [edit_key], null),cljs.core.assoc,new cljs.core.Keyword(null,"error","error",-978969032),true);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"entry-publish","entry-publish",-395041510),(function (db,p__43664){
var vec__43665 = p__43664;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43665,(0),null);
var edit_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43665,(1),null);
return cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [edit_key,new cljs.core.Keyword(null,"publishing","publishing",-244219384)], null),true);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("section-edit","error","section-edit/error",430343686),(function (db,p__43668){
var vec__43669 = p__43668;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43669,(0),null);
var error = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43669,(1),null);
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$4(cljs.core.assoc_in(db,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167),new cljs.core.Keyword(null,"section-name-error","section-name-error",-581249210)], null),error),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167)], null),cljs.core.dissoc,new cljs.core.Keyword(null,"loading","loading",-737050189));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("entry-publish-with-board","finish","entry-publish-with-board/finish",-759409110),(function (db,p__43672){
var vec__43673 = p__43672;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43673,(0),null);
var new_board_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43673,(1),null);
var edit_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43673,(2),null);
var org_slug = oc.web.lib.utils.section_org_slug(new_board_data);
var org_data_key = oc.web.dispatcher.org_data_key(org_slug);
var contributions_count_key = cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(org_data_key,new cljs.core.Keyword(null,"contributions-count","contributions-count",-1846821144)));
var board_slug = new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(new_board_data);
var posts_key = oc.web.dispatcher.posts_data_key(org_slug);
var board_key = oc.web.dispatcher.board_data_key.cljs$core$IFn$_invoke$arity$2(org_slug,board_slug);
var fixed_board_data = oc.web.utils.activity.parse_board.cljs$core$IFn$_invoke$arity$2(new_board_data,oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$2(db,org_slug));
var merged_items = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,posts_key),new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(fixed_board_data)], 0));
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core.assoc_in(cljs.core.assoc_in(cljs.core.update_in.cljs$core$IFn$_invoke$arity$4(cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core.assoc_in(cljs.core.assoc_in(cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(db,contributions_count_key,cljs.core.inc),board_key,cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(fixed_board_data,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159))),posts_key,merged_items),new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167)),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [edit_key], null),cljs.core.dissoc,new cljs.core.Keyword(null,"publishing","publishing",-244219384)),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [edit_key,new cljs.core.Keyword(null,"board-slug","board-slug",99003663)], null),new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(fixed_board_data)),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [edit_key,new cljs.core.Keyword(null,"new-section","new-section",-985556033)], null),true),new cljs.core.Keyword(null,"entry-toggle-save-on-exit","entry-toggle-save-on-exit",-963399746));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("entry-publish","finish","entry-publish/finish",-808852722),(function (db,p__43687){
var vec__43688 = p__43687;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43688,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43688,(1),null);
var edit_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43688,(2),null);
var activity_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43688,(3),null);
var org_data_key = oc.web.dispatcher.org_data_key(org_slug);
var contributions_count_key = cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(org_data_key,new cljs.core.Keyword(null,"contributions-count","contributions-count",-1846821144)));
var board_data = oc.web.utils.activity.board_by_uuid(new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127).cljs$core$IFn$_invoke$arity$1(activity_data));
var fixed_activity_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(oc.web.utils.activity.parse_entry.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.update.cljs$core$IFn$_invoke$arity$3(activity_data,new cljs.core.Keyword(null,"published-at","published-at",249684621),(function (p1__43676_SHARP_){
if(cljs.core.seq(p1__43676_SHARP_)){
return p1__43676_SHARP_;
} else {
return oc.web.lib.utils.as_of_now();
}
})),board_data,oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$2(db,org_slug)], 0)),new cljs.core.Keyword(null,"unseen","unseen",1063275592),false),new cljs.core.Keyword(null,"unread","unread",-1950424572),false);
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core.update_in.cljs$core$IFn$_invoke$arity$4(oc.web.stores.activity.add_remove_item_from_contributions(oc.web.stores.activity.add_remove_item_from_board(oc.web.stores.activity.add_remove_item_from_follow((function (){var ndb = oc.web.stores.activity.add_remove_item_from_bookmarks(oc.web.stores.activity.add_remove_item_from_all_posts(cljs.core.assoc_in(cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(db,contributions_count_key,cljs.core.inc),oc.web.dispatcher.activity_key(org_slug,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data)),fixed_activity_data),org_slug,fixed_activity_data),org_slug,fixed_activity_data);
if(cljs.core.truth_(new cljs.core.Keyword(null,"following","following",-2049193617).cljs$core$IFn$_invoke$arity$1(board_data))){
return oc.web.stores.activity.add_published_post_to_home(ndb,org_slug,fixed_activity_data);
} else {
return ndb;
}
})(),org_slug,fixed_activity_data,false),org_slug,fixed_activity_data),org_slug,fixed_activity_data),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [edit_key], null),cljs.core.dissoc,new cljs.core.Keyword(null,"publishing","publishing",-244219384)),new cljs.core.Keyword(null,"entry-toggle-save-on-exit","entry-toggle-save-on-exit",-963399746));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("entry-publish","failed","entry-publish/failed",-1116278806),(function (db,p__43700){
var vec__43701 = p__43700;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43701,(0),null);
var edit_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43701,(1),null);
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$5(cljs.core.update_in.cljs$core$IFn$_invoke$arity$4(db,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [edit_key], null),cljs.core.dissoc,new cljs.core.Keyword(null,"publishing","publishing",-244219384)),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [edit_key], null),cljs.core.assoc,new cljs.core.Keyword(null,"error","error",-978969032),true);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"activity-delete","activity-delete",-1780584608),(function (db,p__43711){
var vec__43712 = p__43711;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43712,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43712,(1),null);
var activity_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43712,(2),null);
var org_data_key = oc.web.dispatcher.org_data_key(org_slug);
var contributions_count_key = cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(org_data_key,new cljs.core.Keyword(null,"contributions-count","contributions-count",-1846821144)));
var posts_key = oc.web.dispatcher.posts_data_key(org_slug);
var posts_data = oc.web.dispatcher.posts_data.cljs$core$IFn$_invoke$arity$0();
var next_posts = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(posts_data,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));
var containers_key = oc.web.dispatcher.containers_key(org_slug);
var change_data = oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$2(db,org_slug);
var org_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,org_data_key);
var active_users = oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org_slug,db);
var follow_publishers_list = oc.web.dispatcher.follow_publishers_list.cljs$core$IFn$_invoke$arity$2(org_slug,db);
var follow_boards_list = oc.web.dispatcher.follow_boards_list.cljs$core$IFn$_invoke$arity$2(org_slug,db);
var with_fixed_containers = cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (ndb,ckey){
var container_rp_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,ckey,oc.web.dispatcher.recently_posted_sort);
var container_ra_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,ckey,oc.web.dispatcher.recent_activity_sort);
var rp_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(ndb,container_rp_key);
var ra_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(ndb,container_ra_key);
var rp_QMARK_ = cljs.core.map_QMARK_(rp_data);
var ra_QMARK_ = cljs.core.map_QMARK_(ra_data);
var updated_rp_data = ((rp_QMARK_)?cljs.core.update.cljs$core$IFn$_invoke$arity$3(rp_data,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897),(function (items_list){
return cljs.core.filterv((function (p1__43705_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__43705_SHARP_),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));
}),items_list);
})):null);
var updated_ra_data = ((ra_QMARK_)?cljs.core.update.cljs$core$IFn$_invoke$arity$3(ra_data,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897),(function (items_list){
return cljs.core.filterv((function (p1__43706_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__43706_SHARP_),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));
}),items_list);
})):null);
var parsed_rp_data = oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$5(updated_rp_data,change_data,org_data,active_users,oc.web.dispatcher.recently_posted_sort);
var parsed_ra_data = oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$5(updated_ra_data,change_data,org_data,active_users,oc.web.dispatcher.recent_activity_sort);
var tndb = ndb;
var tndb__$1 = ((rp_QMARK_)?cljs.core.assoc_in(tndb,container_rp_key,cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(parsed_rp_data,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159))):tndb);
if(ra_QMARK_){
return cljs.core.assoc_in(tndb__$1,container_ra_key,cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(parsed_ra_data,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159)));
} else {
return tndb__$1;
}
}),db,cljs.core.keys(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,containers_key)));
var contributions_list_key = oc.web.dispatcher.contributions_list_key(org_slug);
var with_fixed_contribs = cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (ndb,ckey){
var contrib_data_key = oc.web.dispatcher.contributions_key.cljs$core$IFn$_invoke$arity$2(org_slug,ckey);
var contrib_posts_list_key = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(contrib_data_key,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897));
var contrib_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(ndb,contrib_data_key);
var updated_contrib_data = cljs.core.update.cljs$core$IFn$_invoke$arity$3(contrib_data,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897),(function (posts_list){
return cljs.core.filterv((function (p1__43709_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__43709_SHARP_),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));
}),posts_list);
}));
var parsed_contrib_data = oc.web.utils.activity.parse_contributions(updated_contrib_data,change_data,org_data,active_users,follow_publishers_list,oc.web.dispatcher.recently_posted_sort);
return cljs.core.assoc_in(ndb,contrib_data_key,cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(parsed_contrib_data,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159)));
}),with_fixed_containers,cljs.core.keys(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,contributions_list_key)));
var boards_key = oc.web.dispatcher.boards_key(org_slug);
var with_fixed_boards = cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (ndb,ckey){
var board_data_key = oc.web.dispatcher.board_data_key.cljs$core$IFn$_invoke$arity$2(org_slug,ckey);
var posts_list_key = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(board_data_key,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897));
var board_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(ndb,board_data_key);
var updated_board_data = cljs.core.update.cljs$core$IFn$_invoke$arity$3(board_data,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897),(function (posts_list){
return cljs.core.filterv((function (p1__43710_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__43710_SHARP_),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));
}),posts_list);
}));
var parsed_board_data = oc.web.utils.activity.parse_board(updated_board_data,change_data,active_users,follow_boards_list,oc.web.dispatcher.recently_posted_sort);
return cljs.core.assoc_in(ndb,board_data_key,cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(parsed_board_data,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159)));
}),with_fixed_contribs,cljs.core.keys(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,boards_key)));
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.cmail_data_key,new cljs.core.Keyword(null,"uuid","uuid",-2145095719))),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data))){
return cljs.core.assoc_in(cljs.core.assoc_in(cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(with_fixed_boards,contributions_count_key,cljs.core.dec),oc.web.dispatcher.cmail_data_key,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"delete","delete",-1768633620),true], null)),posts_key,next_posts);
} else {
return cljs.core.assoc_in(with_fixed_boards,posts_key,next_posts);
}
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"activity-move","activity-move",-355927804),(function (db,p__43717){
var vec__43718 = p__43717;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43718,(0),null);
var activity_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43718,(1),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43718,(2),null);
var board_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43718,(3),null);
var change_data = oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$2(db,org_slug);
var fixed_activity_data = oc.web.utils.activity.parse_entry.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([activity_data,board_data,change_data], 0));
var activity_key = oc.web.dispatcher.activity_key(org_slug,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));
return cljs.core.assoc_in(db,activity_key,fixed_activity_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"activity-share-show","activity-share-show",-1149834881),(function (db,p__43721){
var vec__43722 = p__43721;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43722,(0),null);
var activity_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43722,(1),null);
var container_element_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43722,(2),null);
var share_medium = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43722,(3),null);
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"activity-share","activity-share",-127339936),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"share-data","share-data",1810949431),activity_data], null)),new cljs.core.Keyword(null,"activity-share-container","activity-share-container",-1384168706),container_element_id),new cljs.core.Keyword(null,"activity-share-medium","activity-share-medium",866045149),share_medium),new cljs.core.Keyword(null,"activity-shared-data","activity-shared-data",-674728784));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"activity-share-hide","activity-share-hide",-1709177315),(function (db,p__43727){
var vec__43730 = p__43727;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43730,(0),null);
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(db,new cljs.core.Keyword(null,"activity-share","activity-share",-127339936)),new cljs.core.Keyword(null,"activity-share-medium","activity-share-medium",866045149)),new cljs.core.Keyword(null,"activity-share-container","activity-share-container",-1384168706));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"activity-share-reset","activity-share-reset",-1772920483),(function (db,p__43733){
var vec__43734 = p__43733;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43734,(0),null);
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(db,new cljs.core.Keyword(null,"activity-shared-data","activity-shared-data",-674728784));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"activity-share","activity-share",-127339936),(function (db,p__43740){
var vec__43741 = p__43740;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43741,(0),null);
var share_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43741,(1),null);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"activity-share-data","activity-share-data",-810597437),share_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("activity-share","finish","activity-share/finish",1829129073),(function (db,p__43744){
var vec__43745 = p__43744;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43745,(0),null);
var success = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43745,(1),null);
var shared_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43745,(2),null);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"activity-shared-data","activity-shared-data",-674728784),(cljs.core.truth_(success)?oc.web.utils.activity.parse_entry.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([shared_data,new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(shared_data),oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$1(db)], 0)):new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"error","error",-978969032),true], null)));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"entry-revert","entry-revert",-1402208640),(function (db,p__43748){
var vec__43749 = p__43748;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43749,(0),null);
var entry = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43749,(1),null);
return db;
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"activity-get","activity-get",1006703884),(function (db,p__43753){
var vec__43755 = p__43753;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43755,(0),null);
var map__43758 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43755,(1),null);
var map__43758__$1 = (((((!((map__43758 == null))))?(((((map__43758.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__43758.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__43758):map__43758);
var org_slug = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43758__$1,new cljs.core.Keyword(null,"org-slug","org-slug",-726595051));
var board_slug = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43758__$1,new cljs.core.Keyword(null,"board-slug","board-slug",99003663));
var board_uuid = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43758__$1,new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127));
var activity_uuid = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43758__$1,new cljs.core.Keyword(null,"activity-uuid","activity-uuid",-663317778));
var secure_uuid = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__43758__$1,new cljs.core.Keyword(null,"secure-uuid","secure-uuid",-1972075067));
var activity_key = (cljs.core.truth_(secure_uuid)?oc.web.dispatcher.secure_activity_key(org_slug,secure_uuid):oc.web.dispatcher.activity_key(org_slug,activity_uuid));
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(db,activity_key,(function (p1__43752_SHARP_){
var updated_activity_data = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"loading","loading",-737050189),true,new cljs.core.Keyword(null,"uuid","uuid",-2145095719),activity_uuid,new cljs.core.Keyword(null,"board-slug","board-slug",99003663),board_slug,new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127),board_uuid], null);
if(cljs.core.map_QMARK_(p1__43752_SHARP_)){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__43752_SHARP_,updated_activity_data], 0));
} else {
return updated_activity_data;
}
}));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("activity-get","not-found","activity-get/not-found",998070428),(function (db,p__43767){
var vec__43768 = p__43767;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43768,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43768,(1),null);
var activity_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43768,(2),null);
var secure_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43768,(3),null);
var activity_key = (cljs.core.truth_(secure_uuid)?oc.web.dispatcher.secure_activity_key(org_slug,secure_uuid):oc.web.dispatcher.activity_key(org_slug,activity_uuid));
return cljs.core.assoc_in(db,activity_key,new cljs.core.Keyword(null,"404","404",948666615));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("activity-get","failed","activity-get/failed",-1917791994),(function (db,p__43771){
var vec__43772 = p__43771;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43772,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43772,(1),null);
var activity_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43772,(2),null);
var secure_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43772,(3),null);
var activity_key = (cljs.core.truth_(secure_uuid)?oc.web.dispatcher.secure_activity_key(org_slug,secure_uuid):oc.web.dispatcher.activity_key(org_slug,activity_uuid));
var old_activity_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,activity_key);
var failed_activity_data = ((cljs.core.map_QMARK_(old_activity_data))?cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(old_activity_data,new cljs.core.Keyword(null,"loading","loading",-737050189)):old_activity_data);
return cljs.core.assoc_in(db,activity_key,failed_activity_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("activity-get","finish","activity-get/finish",538149738),(function (db,p__43781){
var vec__43782 = p__43781;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43782,(0),null);
var status = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43782,(1),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43782,(2),null);
var activity_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43782,(3),null);
var secure_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43782,(4),null);
var activity_uuid = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data);
var board_data = oc.web.utils.activity.board_by_uuid(new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127).cljs$core$IFn$_invoke$arity$1(activity_data));
var activity_key = (cljs.core.truth_(secure_uuid)?oc.web.dispatcher.secure_activity_key(org_slug,secure_uuid):oc.web.dispatcher.activity_key(org_slug,activity_uuid));
var fixed_activity_data = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(oc.web.utils.activity.parse_entry.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([activity_data,board_data,oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$2(db,org_slug)], 0)),new cljs.core.Keyword(null,"loading","loading",-737050189));
var update_cmail_QMARK_ = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.cmail_data_key,new cljs.core.Keyword(null,"uuid","uuid",-2145095719))),activity_uuid)) && ((cljs.core.compare(new cljs.core.Keyword(null,"updated-at","updated-at",-1592622336).cljs$core$IFn$_invoke$arity$1(fixed_activity_data),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.cmail_data_key,new cljs.core.Keyword(null,"updated-at","updated-at",-1592622336)))) > (0))));
var G__43789 = db;
var G__43789__$1 = ((update_cmail_QMARK_)?cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(G__43789,oc.web.dispatcher.cmail_data_key,(function (p1__43779_SHARP_){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([p1__43779_SHARP_,fixed_activity_data], 0));
})):G__43789);
var G__43789__$2 = ((update_cmail_QMARK_)?cljs.core.update_in.cljs$core$IFn$_invoke$arity$5(G__43789__$1,oc.web.dispatcher.cmail_state_key,cljs.core.assoc,new cljs.core.Keyword(null,"key","key",-1516042587),oc.web.lib.utils.activity_uuid()):G__43789__$1);
var G__43789__$3 = cljs.core.assoc_in(G__43789__$2,activity_key,fixed_activity_data)
;
var ndb = G__43789__$3;
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(ndb,oc.web.dispatcher.user_notifications_key(org_slug),(function (p1__43780_SHARP_){
return oc.web.utils.notification.fix_notifications.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([ndb,p1__43780_SHARP_], 0));
}));

}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"bookmark-toggle","bookmark-toggle",187214294),(function (db,p__43800){
var vec__43801 = p__43800;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43801,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43801,(1),null);
var activity_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43801,(2),null);
var bookmark_QMARK_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43801,(3),null);
var bookmarks_count_key = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_data_key(org_slug),new cljs.core.Keyword(null,"bookmarks-count","bookmarks-count",-405810102));
var current_bookmarks_count = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,bookmarks_count_key);
var activity_key = oc.web.dispatcher.activity_key(org_slug,activity_uuid);
var activity_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,activity_key);
var bookmark_link_index = (cljs.core.truth_(activity_data)?oc.web.lib.utils.index_of(new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(activity_data),(function (p1__43798_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"rel","rel",1378823488).cljs$core$IFn$_invoke$arity$1(p1__43798_SHARP_),"bookmark");
})):null);
var next_activity_data_STAR_ = (cljs.core.truth_(activity_data)?(cljs.core.truth_(bookmark_QMARK_)?cljs.core.update.cljs$core$IFn$_invoke$arity$3(activity_data,new cljs.core.Keyword(null,"bookmarked-at","bookmarked-at",1451784060),(function (){
return oc.web.lib.utils.as_of_now();
})):cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(activity_data,new cljs.core.Keyword(null,"bookmarked-at","bookmarked-at",1451784060))):null);
var next_activity_data = (cljs.core.truth_((function (){var and__4115__auto__ = activity_data;
if(cljs.core.truth_(and__4115__auto__)){
return bookmark_link_index;
} else {
return and__4115__auto__;
}
})())?cljs.core.assoc_in(next_activity_data_STAR_,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"links","links",-654507394),bookmark_link_index,new cljs.core.Keyword(null,"method","method",55703592)], null),(cljs.core.truth_(bookmark_QMARK_)?"DELETE":"POST")):null);
var next_db = (cljs.core.truth_(activity_data)?cljs.core.assoc_in(db,activity_key,next_activity_data):db);
var next_bookmarks_count = (cljs.core.truth_((function (){var and__4115__auto__ = bookmark_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(new cljs.core.Keyword(null,"bookmarked-at","bookmarked-at",1451784060).cljs$core$IFn$_invoke$arity$1(activity_data));
} else {
return and__4115__auto__;
}
})())?(current_bookmarks_count + (1)):(cljs.core.truth_(((cljs.core.not(bookmark_QMARK_))?new cljs.core.Keyword(null,"bookmarked-at","bookmarked-at",1451784060).cljs$core$IFn$_invoke$arity$1(activity_data):false))?(current_bookmarks_count - (1)):current_bookmarks_count
));
var tmp = oc.web.stores.activity.add_remove_item_from_bookmarks(next_db,org_slug,next_activity_data);
var bk_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$2(org_slug,new cljs.core.Keyword(null,"bookmarks","bookmarks",1877375283));
return cljs.core.assoc_in(oc.web.stores.activity.add_remove_item_from_bookmarks(next_db,org_slug,next_activity_data),bookmarks_count_key,next_bookmarks_count);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("entry-save-with-board","finish","entry-save-with-board/finish",916268014),(function (db,p__43825){
var vec__43826 = p__43825;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43826,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43826,(1),null);
var fixed_board_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43826,(2),null);
var board_key = oc.web.dispatcher.board_data_key.cljs$core$IFn$_invoke$arity$2(org_slug,new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(fixed_board_data));
var posts_key = oc.web.dispatcher.posts_data_key(org_slug);
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core.update_in.cljs$core$IFn$_invoke$arity$4(cljs.core.assoc_in(db,board_key,cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(fixed_board_data,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159))),posts_key,cljs.core.merge,cljs.core.get.cljs$core$IFn$_invoke$arity$2(fixed_board_data,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159))),new cljs.core.Keyword(null,"section-editing","section-editing",-1296927167)),new cljs.core.Keyword(null,"entry-toggle-save-on-exit","entry-toggle-save-on-exit",-963399746));
}));
oc.web.stores.activity.all_posts_get_finish = (function oc$web$stores$activity$all_posts_get_finish(db,org_slug,sort_type,all_posts_data){
var org_data_key = oc.web.dispatcher.org_data_key(org_slug);
var org_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,org_data_key);
var change_data = oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$2(db,org_slug);
var active_users = oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org_slug,db);
var prepare_all_posts_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(all_posts_data),new cljs.core.Keyword(null,"container-slug","container-slug",365736492),new cljs.core.Keyword(null,"all-posts","all-posts",-1285476533));
var fixed_all_posts_data = oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$5(prepare_all_posts_data,change_data,org_data,active_users,sort_type);
var posts_key = oc.web.dispatcher.posts_data_key(org_slug);
var old_posts = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,posts_key);
var merged_items = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([old_posts,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(fixed_all_posts_data)], 0));
var container_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,new cljs.core.Keyword(null,"all-posts","all-posts",-1285476533),sort_type);
var ndb = db;
var ndb__$1 = cljs.core.assoc_in(ndb,container_key,cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(fixed_all_posts_data,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159)));
var ndb__$2 = cljs.core.assoc_in(ndb__$1,posts_key,merged_items);
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(ndb__$2,oc.web.dispatcher.user_notifications_key(org_slug),(function (p1__43829_SHARP_){
return oc.web.utils.notification.fix_notifications.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([ndb__$2,p1__43829_SHARP_], 0));
}));
});
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("all-posts-get","finish","all-posts-get/finish",-1262488350),(function (db,p__43851){
var vec__43856 = p__43851;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43856,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43856,(1),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43856,(2),null);
var all_posts_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43856,(3),null);
return oc.web.stores.activity.all_posts_get_finish(db,org_slug,sort_type,all_posts_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("all-posts-refresh","finish","all-posts-refresh/finish",340160013),(function (db,p__43859){
var vec__43860 = p__43859;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43860,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43860,(1),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43860,(2),null);
var all_posts_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43860,(3),null);
return oc.web.stores.activity.all_posts_get_finish(db,org_slug,sort_type,all_posts_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"all-posts-more","all-posts-more",584168585),(function (db,p__43874){
var vec__43875 = p__43874;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43875,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43875,(1),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43875,(2),null);
var container_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,new cljs.core.Keyword(null,"all-posts","all-posts",-1285476533),sort_type);
var container_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,container_key);
var next_posts_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(container_data,new cljs.core.Keyword(null,"loading-more","loading-more",-1145525667),true);
return cljs.core.assoc_in(db,container_key,next_posts_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("all-posts-more","finish","all-posts-more/finish",-1854949097),(function (db,p__43883){
var vec__43884 = p__43883;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43884,(0),null);
var org__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43884,(1),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43884,(2),null);
var direction = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43884,(3),null);
var posts_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43884,(4),null);
if(cljs.core.truth_(posts_data)){
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$2(db,org__$1);
var container_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org__$1,new cljs.core.Keyword(null,"all-posts","all-posts",-1285476533),sort_type);
var container_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,container_key);
var posts_data_key = oc.web.dispatcher.posts_data_key(org__$1);
var old_posts = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,posts_data_key);
var prepare_all_posts_data = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(posts_data),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897),new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897).cljs$core$IFn$_invoke$arity$1(container_data),new cljs.core.Keyword(null,"old-links","old-links",340078773),new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(container_data),new cljs.core.Keyword(null,"container-slug","container-slug",365736492),new cljs.core.Keyword(null,"all-posts","all-posts",-1285476533)], null)], 0));
var active_users = oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org__$1,db);
var fixed_all_posts_data = oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$6(prepare_all_posts_data,oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$2(db,org__$1),org_data,active_users,sort_type,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"direction","direction",-633359395),direction], null));
var new_items_map = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([old_posts,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(fixed_all_posts_data)], 0));
var new_container_data = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(fixed_all_posts_data,new cljs.core.Keyword(null,"direction","direction",-633359395),direction),new cljs.core.Keyword(null,"loading-more","loading-more",-1145525667),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159)], 0));
var ndb = db;
var ndb__$1 = cljs.core.assoc_in(ndb,container_key,new_container_data);
var ndb__$2 = cljs.core.assoc_in(ndb__$1,posts_data_key,new_items_map);
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(ndb__$2,oc.web.dispatcher.user_notifications_key(org__$1),(function (p1__43879_SHARP_){
return oc.web.utils.notification.fix_notifications.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([ndb__$2,p1__43879_SHARP_], 0));
}));
} else {
return db;
}
}));
oc.web.stores.activity.bookmarks_get_finish = (function oc$web$stores$activity$bookmarks_get_finish(db,org_slug,sort_type,bookmarks_data){
var org_data_key = oc.web.dispatcher.org_data_key(org_slug);
var org_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,org_data_key);
var change_data = oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$2(db,org_slug);
var active_users = oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org_slug,db);
var prepare_bookmarks_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(bookmarks_data),new cljs.core.Keyword(null,"container-slug","container-slug",365736492),new cljs.core.Keyword(null,"bookmarks","bookmarks",1877375283));
var fixed_bookmarks_data = oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$5(prepare_bookmarks_data,change_data,org_data,active_users,oc.web.dispatcher.recently_posted_sort);
var posts_key = oc.web.dispatcher.posts_data_key(org_slug);
var old_posts = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,posts_key);
var merged_items = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([old_posts,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(fixed_bookmarks_data)], 0));
var container_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$2(org_slug,new cljs.core.Keyword(null,"bookmarks","bookmarks",1877375283));
var ndb = db;
var ndb__$1 = cljs.core.assoc_in(ndb,container_key,cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(fixed_bookmarks_data,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159)));
var ndb__$2 = cljs.core.assoc_in(ndb__$1,posts_key,merged_items);
var ndb__$3 = cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(ndb__$2,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(org_data_key,new cljs.core.Keyword(null,"bookmarks-count","bookmarks-count",-405810102)),(function (p1__43891_SHARP_){
return oc.web.utils.org.disappearing_count_value(p1__43891_SHARP_,new cljs.core.Keyword(null,"total-count","total-count",-1999441386).cljs$core$IFn$_invoke$arity$1(fixed_bookmarks_data));
}));
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(ndb__$3,oc.web.dispatcher.user_notifications_key(org_slug),(function (p1__43892_SHARP_){
return oc.web.utils.notification.fix_notifications.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([ndb__$3,p1__43892_SHARP_], 0));
}));
});
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("bookmarks-get","finish","bookmarks-get/finish",1980734268),(function (db,p__43893){
var vec__43894 = p__43893;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43894,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43894,(1),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43894,(2),null);
var bookmarks_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43894,(3),null);
return oc.web.stores.activity.bookmarks_get_finish(db,org_slug,sort_type,bookmarks_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("bookmarks-refresh","finish","bookmarks-refresh/finish",-933941561),(function (db,p__43897){
var vec__43898 = p__43897;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43898,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43898,(1),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43898,(2),null);
var bookmarks_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43898,(3),null);
return oc.web.stores.activity.bookmarks_get_finish(db,org_slug,sort_type,bookmarks_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"bookmarks-more","bookmarks-more",399174414),(function (db,p__43902){
var vec__43903 = p__43902;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43903,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43903,(1),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43903,(2),null);
var container_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$2(org_slug,new cljs.core.Keyword(null,"bookmarks","bookmarks",1877375283));
var container_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,container_key);
var next_posts_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(container_data,new cljs.core.Keyword(null,"loading-more","loading-more",-1145525667),true);
var bookmarks_count_key = cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.org_data_key(org_slug),new cljs.core.Keyword(null,"bookmarks-count","bookmarks-count",-405810102)));
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc_in(db,container_key,next_posts_data),bookmarks_count_key,(function (p1__43901_SHARP_){
return oc.web.utils.org.disappearing_count_value(p1__43901_SHARP_,new cljs.core.Keyword(null,"total-count","total-count",-1999441386).cljs$core$IFn$_invoke$arity$1(next_posts_data));
}));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("bookmarks-more","finish","bookmarks-more/finish",119195869),(function (db,p__43908){
var vec__43909 = p__43908;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43909,(0),null);
var org__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43909,(1),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43909,(2),null);
var direction = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43909,(3),null);
var posts_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43909,(4),null);
if(cljs.core.truth_(posts_data)){
var org_data_key = oc.web.dispatcher.org_data_key(org__$1);
var org_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,org_data_key);
var container_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$2(org__$1,new cljs.core.Keyword(null,"bookmarks","bookmarks",1877375283));
var container_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,container_key);
var posts_data_key = oc.web.dispatcher.posts_data_key(org__$1);
var old_posts = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,posts_data_key);
var prepare_bookmarks_data = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(posts_data),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897),new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897).cljs$core$IFn$_invoke$arity$1(container_data),new cljs.core.Keyword(null,"old-links","old-links",340078773),new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(container_data),new cljs.core.Keyword(null,"container-slug","container-slug",365736492),new cljs.core.Keyword(null,"bookmarks","bookmarks",1877375283)], null)], 0));
var active_users = oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org__$1,db);
var fixed_bookmarks_data = oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$6(prepare_bookmarks_data,oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$2(db,org__$1),org_data,active_users,oc.web.dispatcher.recently_posted_sort,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"direction","direction",-633359395),direction], null));
var new_items_map = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([old_posts,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(fixed_bookmarks_data)], 0));
var new_container_data = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(fixed_bookmarks_data,new cljs.core.Keyword(null,"direction","direction",-633359395),direction),new cljs.core.Keyword(null,"loading-more","loading-more",-1145525667),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159)], 0));
var ndb = db;
var ndb__$1 = cljs.core.assoc_in(ndb,container_key,new_container_data);
var ndb__$2 = cljs.core.assoc_in(ndb__$1,posts_data_key,new_items_map);
var ndb__$3 = cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(ndb__$2,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(org_data_key,new cljs.core.Keyword(null,"bookmarks-count","bookmarks-count",-405810102)),(function (p1__43906_SHARP_){
return oc.web.utils.org.disappearing_count_value(p1__43906_SHARP_,new cljs.core.Keyword(null,"total-count","total-count",-1999441386).cljs$core$IFn$_invoke$arity$1(fixed_bookmarks_data));
}));
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(ndb__$3,oc.web.dispatcher.user_notifications_key(org__$1),(function (p1__43907_SHARP_){
return oc.web.utils.notification.fix_notifications.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([ndb__$3,p1__43907_SHARP_], 0));
}));
} else {
return db;
}
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"remove-bookmark","remove-bookmark",-320753542),(function (db,p__43913){
var vec__43914 = p__43913;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43914,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43914,(1),null);
var entry_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43914,(2),null);
var activity_key = oc.web.dispatcher.activity_key(org_slug,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(entry_data));
var bookmarks_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$2(org_slug,new cljs.core.Keyword(null,"bookmarks","bookmarks",1877375283));
var bookmarks_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,bookmarks_key);
var org_data_key = oc.web.dispatcher.org_data_key(org_slug);
return oc.web.stores.activity.add_remove_item_from_bookmarks(cljs.core.assoc_in(cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(db,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(org_data_key,new cljs.core.Keyword(null,"bookmarks-count","bookmarks-count",-405810102)),(function (p1__43912_SHARP_){
return oc.web.utils.org.disappearing_count_value(p1__43912_SHARP_,(p1__43912_SHARP_ - (1)));
})),activity_key,entry_data),org_slug,entry_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"add-bookmark","add-bookmark",134611091),(function (db,p__43918){
var vec__43919 = p__43918;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43919,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43919,(1),null);
var activity_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43919,(2),null);
var org_data_key = oc.web.dispatcher.org_data_key(org_slug);
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(db,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(org_data_key,new cljs.core.Keyword(null,"bookmarks-count","bookmarks-count",-405810102)),(function (p1__43917_SHARP_){
return oc.web.utils.org.disappearing_count_value(p1__43917_SHARP_,(p1__43917_SHARP_ + (1)));
}));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"activities-count","activities-count",2119299965),(function (db,p__43936){
var vec__43937 = p__43936;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43937,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43937,(1),null);
var items_count = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43937,(2),null);
var old_reads_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,oc.web.dispatcher.activities_read_key);
var ks = cljs.core.vec(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"item-id","item-id",-1804511607),items_count));
var vs = cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__43935_SHARP_){
return cljs.core.zipmap(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"count","count",2139924085),new cljs.core.Keyword(null,"reads","reads",-1215067361),new cljs.core.Keyword(null,"item-id","item-id",-1804511607)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(p1__43935_SHARP_),cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(old_reads_data,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"item-id","item-id",-1804511607).cljs$core$IFn$_invoke$arity$1(p1__43935_SHARP_),new cljs.core.Keyword(null,"reads","reads",-1215067361)], null)),new cljs.core.Keyword(null,"item-id","item-id",-1804511607).cljs$core$IFn$_invoke$arity$1(p1__43935_SHARP_)], null));
}),items_count);
var new_items_count = cljs.core.zipmap(ks,vs);
var last_read_at_map = cljs.core.zipmap(ks,cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"last-read-at","last-read-at",-216601930),items_count));
var activities_key = oc.web.dispatcher.posts_data_key(org_slug);
var tdb = db;
var tdb__$1 = cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (tdb__$1,p__43944){
var vec__43945 = p__43944;
var activity_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43945,(0),null);
var activity_last_read_at = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43945,(1),null);
var entry_data = (oc.web.dispatcher.entry_data.cljs$core$IFn$_invoke$arity$3 ? oc.web.dispatcher.entry_data.cljs$core$IFn$_invoke$arity$3(org_slug,activity_uuid,tdb__$1) : oc.web.dispatcher.entry_data.call(null,org_slug,activity_uuid,tdb__$1));
if(cljs.core.map_QMARK_(entry_data)){
return cljs.core.assoc_in(tdb__$1,oc.web.dispatcher.activity_last_read_at_key(org_slug,activity_uuid),activity_last_read_at);
} else {
return tdb__$1;
}
}),tdb,last_read_at_map);
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$4(tdb__$1,oc.web.dispatcher.activities_read_key,cljs.core.merge,new_items_count);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"activity-reads","activity-reads",-203392960),(function (db,p__43953){
var vec__43954 = p__43953;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43954,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43954,(1),null);
var item_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43954,(2),null);
var read_data_count = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43954,(3),null);
var read_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43954,(4),null);
var team_roster = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43954,(5),null);
var activity_data = oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$3(org_slug,item_id,db);
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$2(db,org_slug);
var roster_data = (function (){var or__4126__auto__ = team_roster;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.dispatcher.team_roster.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(org_data),db);
}
})();
var board_data = cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__43948_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(p1__43948_SHARP_),new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(activity_data));
}),new cljs.core.Keyword(null,"boards","boards",1912049694).cljs$core$IFn$_invoke$arity$1(org_data)));
var fixed_read_data = cljs.core.vec(cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__43949_SHARP_){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__43949_SHARP_,new cljs.core.Keyword(null,"seen","seen",-518999789),true);
}),read_data));
var team_users = oc.web.utils.user.filter_active_users(new cljs.core.Keyword(null,"users","users",-713552705).cljs$core$IFn$_invoke$arity$1(roster_data));
var seen_ids = cljs.core.set(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291),read_data));
var private_access_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"access","access",2027349272).cljs$core$IFn$_invoke$arity$1(board_data),"private");
var all_private_users = ((private_access_QMARK_)?cljs.core.set(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"authors","authors",2063018172).cljs$core$IFn$_invoke$arity$1(board_data),new cljs.core.Keyword(null,"viewers","viewers",-415894011).cljs$core$IFn$_invoke$arity$1(board_data))):null);
var filtered_users = ((private_access_QMARK_)?cljs.core.filterv((function (p1__43950_SHARP_){
var G__43979 = new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__43950_SHARP_);
return (all_private_users.cljs$core$IFn$_invoke$arity$1 ? all_private_users.cljs$core$IFn$_invoke$arity$1(G__43979) : all_private_users.call(null,G__43979));
}),team_users):team_users);
var all_ids = cljs.core.set(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291),filtered_users));
var unseen_ids = clojure.set.difference.cljs$core$IFn$_invoke$arity$2(all_ids,seen_ids);
var unseen_users = cljs.core.vec(cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (user_id){
return cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__43951_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__43951_SHARP_),user_id);
}),team_users));
}),unseen_ids));
var current_user_id = oc.web.lib.jwt.user_id();
var current_user_reads = cljs.core.filterv((function (p1__43952_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__43952_SHARP_),current_user_id);
}),read_data);
var last_read_at = new cljs.core.Keyword(null,"read-at","read-at",-936006929).cljs$core$IFn$_invoke$arity$1(cljs.core.last(cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"read-at","read-at",-936006929),current_user_reads)));
var current_posts_uuids = cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),oc.web.dispatcher.filtered_posts_data.cljs$core$IFn$_invoke$arity$1(db));
var tdb = db;
var tdb__$1 = cljs.core.assoc_in(tdb,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.activities_read_key,item_id),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"count","count",2139924085),read_data_count,new cljs.core.Keyword(null,"reads","reads",-1215067361),fixed_read_data,new cljs.core.Keyword(null,"item-id","item-id",-1804511607),item_id,new cljs.core.Keyword(null,"unreads","unreads",-329347545),unseen_users,new cljs.core.Keyword(null,"private-access?","private-access?",1011589803),private_access_QMARK_], null));
if(cljs.core.map_QMARK_(activity_data)){
return cljs.core.assoc_in(tdb__$1,oc.web.dispatcher.activity_last_read_at_key(org_slug,item_id),last_read_at);
} else {
return tdb__$1;
}
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"uploading-video","uploading-video",1488067388),(function (db,p__43994){
var vec__43995 = p__43994;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43995,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43995,(1),null);
var video_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43995,(2),null);
var uploading_video_key = oc.web.dispatcher.uploading_video_key(org_slug,video_id);
return cljs.core.assoc_in(db,uploading_video_key,true);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("entry-auto-save","finish","entry-auto-save/finish",292395922),(function (db,p__43998){
var vec__43999 = p__43998;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43999,(0),null);
var activity_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43999,(1),null);
var edit_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43999,(2),null);
var initial_entry_map = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43999,(3),null);
var org_slug = oc.web.lib.utils.post_org_slug(activity_data);
var board_slug = new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(activity_data);
var activity_key = oc.web.dispatcher.activity_key(org_slug,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));
var activity_board_data = oc.web.dispatcher.board_data.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([db,org_slug,board_slug], 0));
var fixed_activity_data = oc.web.utils.activity.parse_entry.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([activity_data,activity_board_data,oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$2(db,org_slug)], 0));
var keys_for_edit = new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"uuid","uuid",-2145095719),new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127),new cljs.core.Keyword(null,"links","links",-654507394),new cljs.core.Keyword(null,"revision-id","revision-id",1301980641),new cljs.core.Keyword(null,"secure-uuid","secure-uuid",-1972075067),new cljs.core.Keyword(null,"status","status",-1997798413)], null);
var current_edit = cljs.core.get.cljs$core$IFn$_invoke$arity$2(db,edit_key);
var with_board_keys = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"board-name","board-name",-677515056).cljs$core$IFn$_invoke$arity$1(current_edit),new cljs.core.Keyword(null,"board-name","board-name",-677515056).cljs$core$IFn$_invoke$arity$1(initial_entry_map)))?cljs.core.concat.cljs$core$IFn$_invoke$arity$2(keys_for_edit,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"board-slug","board-slug",99003663),new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127)], null)):keys_for_edit);
var is_same_video_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"video-id","video-id",2132630536).cljs$core$IFn$_invoke$arity$1(current_edit),new cljs.core.Keyword(null,"video-id","video-id",2132630536).cljs$core$IFn$_invoke$arity$1(initial_entry_map));
var with_video_keys = ((is_same_video_QMARK_)?cljs.core.concat.cljs$core$IFn$_invoke$arity$2(with_board_keys,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"video-processed","video-processed",1222643416)], null)):with_board_keys);
var video_error = ((is_same_video_QMARK_)?(function (){var or__4126__auto__ = new cljs.core.Keyword(null,"video-error","video-error",331887081).cljs$core$IFn$_invoke$arity$1(current_edit);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"video-error","video-error",331887081).cljs$core$IFn$_invoke$arity$1(activity_data);
}
})():new cljs.core.Keyword(null,"video-error","video-error",331887081).cljs$core$IFn$_invoke$arity$1(current_edit));
var map_for_edit = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.select_keys(activity_data,with_video_keys),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"auto-saving","auto-saving",68752642),false,new cljs.core.Keyword(null,"video-error","video-error",331887081),video_error], null)], 0));
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$4(cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core.assoc_in(db,activity_key,fixed_activity_data),new cljs.core.Keyword(null,"entry-toggle-save-on-exit","entry-toggle-save-on-exit",-963399746)),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [edit_key], null),cljs.core.merge,map_for_edit);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("entry-revert","finish","entry-revert/finish",-1481043085),(function (db,p__44002){
var vec__44003 = p__44002;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44003,(0),null);
var activity_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44003,(1),null);
var org_slug = oc.web.lib.utils.post_org_slug(activity_data);
var board_slug = new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(activity_data);
var activity_key = oc.web.dispatcher.activity_key(org_slug,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));
if(cljs.core.seq(new cljs.core.Keyword(null,"board-slug","board-slug",99003663).cljs$core$IFn$_invoke$arity$1(activity_data))){
var activity_board_data = oc.web.dispatcher.board_data.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([db,org_slug,board_slug], 0));
var fixed_activity_data = oc.web.utils.activity.parse_entry.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([activity_data,activity_board_data,oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$2(db,org_slug)], 0));
return cljs.core.assoc_in(db,activity_key,fixed_activity_data);
} else {
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$4(db,cljs.core.butlast(activity_key),cljs.core.dissoc,cljs.core.last(activity_key));
}
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"mark-unread","mark-unread",-421378020),(function (db,p__44011){
var vec__44012 = p__44011;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44012,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44012,(1),null);
var activity_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44012,(2),null);
var board_uuid = new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127).cljs$core$IFn$_invoke$arity$1(activity_data);
var activity_uuid = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data);
var section_change_key = cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.change_data_key(org_slug),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [board_uuid,new cljs.core.Keyword(null,"unread","unread",-1950424572)], null)));
var activity_key = oc.web.dispatcher.activity_key(org_slug,activity_uuid);
var next_activity_data = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,activity_key),new cljs.core.Keyword(null,"unread","unread",-1950424572),true),new cljs.core.Keyword(null,"last-read-at","last-read-at",-216601930));
var activity_read_key = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.activities_read_key,activity_uuid);
return cljs.core.assoc_in(cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(db,section_change_key,(function (p1__44010_SHARP_){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2((function (){var or__4126__auto__ = p1__44010_SHARP_;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.PersistentVector.EMPTY;
}
})(),activity_uuid));
})),activity_key,next_activity_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"mark-read","mark-read",332267257),(function (db,p__44017){
var vec__44018 = p__44017;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44018,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44018,(1),null);
var activity_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44018,(2),null);
var dismiss_at = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44018,(3),null);
var board_uuid = new cljs.core.Keyword(null,"board-uuid","board-uuid",-242514127).cljs$core$IFn$_invoke$arity$1(activity_data);
var activity_uuid = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data);
var section_change_key = cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.change_data_key(org_slug),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [board_uuid,new cljs.core.Keyword(null,"unread","unread",-1950424572)], null)));
var all_comments_data = oc.web.dispatcher.activity_comments_data.cljs$core$IFn$_invoke$arity$3(org_slug,activity_uuid,db);
var comments_data = cljs.core.filterv((function (p1__44015_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(oc.web.lib.jwt.user_id(),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"author","author",2111686192).cljs$core$IFn$_invoke$arity$1(p1__44015_SHARP_)));
}),all_comments_data);
var activity_key = oc.web.dispatcher.activity_key(org_slug,activity_uuid);
var old_activity_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,activity_key);
var next_activity_data = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([old_activity_data,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"unread","unread",-1950424572),false,new cljs.core.Keyword(null,"last-read-at","last-read-at",-216601930),dismiss_at,new cljs.core.Keyword(null,"last-activity-at","last-activity-at",670511998),((((cljs.core.seq(comments_data)) && ((cljs.core.compare(new cljs.core.Keyword(null,"created-at","created-at",-89248644).cljs$core$IFn$_invoke$arity$1(cljs.core.last(comments_data)),new cljs.core.Keyword(null,"last-activity-at","last-activity-at",670511998).cljs$core$IFn$_invoke$arity$1(old_activity_data)) > (0)))))?new cljs.core.Keyword(null,"created-at","created-at",-89248644).cljs$core$IFn$_invoke$arity$1(cljs.core.last(comments_data)):new cljs.core.Keyword(null,"last-activity-at","last-activity-at",670511998).cljs$core$IFn$_invoke$arity$1(old_activity_data))], null)], 0));
var activity_read_key = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.activities_read_key,activity_uuid);
return cljs.core.assoc_in(cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(db,section_change_key,(function (unreads){
return cljs.core.filterv((function (p1__44016_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(p1__44016_SHARP_,activity_uuid);
}),(function (){var or__4126__auto__ = unreads;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.PersistentVector.EMPTY;
}
})());
})),activity_key,next_activity_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("inbox-get","finish","inbox-get/finish",1442342723),(function (db,p__44022){
var vec__44023 = p__44022;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44023,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44023,(1),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44023,(2),null);
var inbox_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44023,(3),null);
var org_data_key = oc.web.dispatcher.org_data_key(org_slug);
var org_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,org_data_key);
var change_data = oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$2(db,org_slug);
var active_users = oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org_slug,db);
var prepare_inbox_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(inbox_data),new cljs.core.Keyword(null,"container-slug","container-slug",365736492),new cljs.core.Keyword(null,"inbox","inbox",1888669443));
var fixed_inbox_data = oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$5(prepare_inbox_data,change_data,org_data,active_users,sort_type);
var posts_key = oc.web.dispatcher.posts_data_key(org_slug);
var old_posts = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,posts_key);
var merged_items = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([old_posts,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(fixed_inbox_data)], 0));
var container_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,new cljs.core.Keyword(null,"inbox","inbox",1888669443),sort_type);
var ndb = db;
var ndb__$1 = cljs.core.assoc_in(ndb,container_key,cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(fixed_inbox_data,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159)));
var ndb__$2 = cljs.core.assoc_in(ndb__$1,posts_key,merged_items);
var ndb__$3 = cljs.core.assoc_in(ndb__$2,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(org_data_key,new cljs.core.Keyword(null,"following-inbox-count","following-inbox-count",-1849702342)),new cljs.core.Keyword(null,"total-count","total-count",-1999441386).cljs$core$IFn$_invoke$arity$1(fixed_inbox_data));
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(ndb__$3,oc.web.dispatcher.user_notifications_key(org_slug),(function (p1__44021_SHARP_){
return oc.web.utils.notification.fix_notifications.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([ndb__$3,p1__44021_SHARP_], 0));
}));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"inbox-more","inbox-more",517262879),(function (db,p__44026){
var vec__44027 = p__44026;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44027,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44027,(1),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44027,(2),null);
var container_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,new cljs.core.Keyword(null,"inbox","inbox",1888669443),sort_type);
var container_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,container_key);
var next_posts_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(container_data,new cljs.core.Keyword(null,"loading-more","loading-more",-1145525667),true);
return cljs.core.assoc_in(db,container_key,next_posts_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("inbox-more","finish","inbox-more/finish",705199254),(function (db,p__44031){
var vec__44032 = p__44031;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44032,(0),null);
var org__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44032,(1),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44032,(2),null);
var direction = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44032,(3),null);
var posts_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44032,(4),null);
if(cljs.core.truth_(posts_data)){
var org_data_key = oc.web.dispatcher.org_data_key(org__$1);
var org_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,org_data_key);
var container_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org__$1,new cljs.core.Keyword(null,"inbox","inbox",1888669443),sort_type);
var container_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,container_key);
var posts_data_key = oc.web.dispatcher.posts_data_key(org__$1);
var old_posts = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,posts_data_key);
var prepare_inbox_data = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(posts_data),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897),new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897).cljs$core$IFn$_invoke$arity$1(container_data),new cljs.core.Keyword(null,"old-links","old-links",340078773),new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(container_data),new cljs.core.Keyword(null,"container-slug","container-slug",365736492),new cljs.core.Keyword(null,"inbox","inbox",1888669443)], null)], 0));
var active_users = oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org__$1,db);
var fixed_inbox_data = oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$6(prepare_inbox_data,oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$2(db,org__$1),org_data,active_users,sort_type,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"direction","direction",-633359395),direction], null));
var new_items_map = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([old_posts,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(fixed_inbox_data)], 0));
var new_container_data = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(fixed_inbox_data,new cljs.core.Keyword(null,"direction","direction",-633359395),direction),new cljs.core.Keyword(null,"loading-more","loading-more",-1145525667),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159)], 0));
var ndb = db;
var ndb__$1 = cljs.core.assoc_in(ndb,container_key,new_container_data);
var ndb__$2 = cljs.core.assoc_in(ndb__$1,posts_data_key,new_items_map);
var ndb__$3 = cljs.core.assoc_in(ndb__$2,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(org_data_key,new cljs.core.Keyword(null,"following-inbox-count","following-inbox-count",-1849702342)),new cljs.core.Keyword(null,"total-count","total-count",-1999441386).cljs$core$IFn$_invoke$arity$1(fixed_inbox_data));
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(ndb__$3,oc.web.dispatcher.user_notifications_key(org__$1),(function (p1__44030_SHARP_){
return oc.web.utils.notification.fix_notifications.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([ndb__$3,p1__44030_SHARP_], 0));
}));
} else {
return db;
}
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("inbox","dismiss","inbox/dismiss",445803855),(function (db,p__44038){
var vec__44039 = p__44038;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44039,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44039,(1),null);
var item_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44039,(2),null);
var temp__5733__auto__ = oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$1(item_id);
if(cljs.core.truth_(temp__5733__auto__)){
var activity_data = temp__5733__auto__;
var inbox_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$2(org_slug,"inbox");
var inbox_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,inbox_key);
var without_item = cljs.core.update.cljs$core$IFn$_invoke$arity$3(cljs.core.update.cljs$core$IFn$_invoke$arity$3(inbox_data,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897),(function (posts_list){
return cljs.core.filterv((function (p1__44036_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__44036_SHARP_),item_id);
}),posts_list);
})),new cljs.core.Keyword(null,"items-to-render","items-to-render",660219762),(function (posts_list){
return cljs.core.filterv((function (p1__44037_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(p1__44037_SHARP_,item_id);
}),posts_list);
}));
var org_data_key = oc.web.dispatcher.org_data_key(org_slug);
var update_count_QMARK_ = cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.count(new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897).cljs$core$IFn$_invoke$arity$1(inbox_data)),cljs.core.count(new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897).cljs$core$IFn$_invoke$arity$1(without_item)));
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc_in(db,inbox_key,without_item),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(org_data_key,new cljs.core.Keyword(null,"following-inbox-count","following-inbox-count",-1849702342)),((update_count_QMARK_)?cljs.core.dec:cljs.core.identity));
} else {
return db;
}
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("inbox","unread","inbox/unread",-2047670138),(function (db,p__44042){
var vec__44043 = p__44042;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44043,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44043,(1),null);
var current_board_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44043,(2),null);
var item_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44043,(3),null);
var temp__5733__auto__ = oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$1(item_id);
if(cljs.core.truth_(temp__5733__auto__)){
var activity_data = temp__5733__auto__;
var inbox_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$2(org_slug,"inbox");
var posts_list_key = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(inbox_key,new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897));
var items_to_render_key = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(inbox_key,new cljs.core.Keyword(null,"items-to-render","items-to-render",660219762));
var inbox_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,inbox_key);
var next_db = (cljs.core.truth_(inbox_data)?cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(db,posts_list_key,(function (posts_list){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(cljs.core.set(posts_list),oc.web.stores.activity.item_from_entity(activity_data)));
})),items_to_render_key,(function (posts_list){
return cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(cljs.core.set(posts_list),oc.web.stores.activity.item_from_entity(activity_data)));
})):db);
var activity_key = oc.web.dispatcher.activity_key(org_slug,item_id);
var activity_data__$1 = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,activity_key);
var fixed_activity_data = cljs.core.update.cljs$core$IFn$_invoke$arity$3(activity_data__$1,new cljs.core.Keyword(null,"links","links",-654507394),(function (links){
return cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (link){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"rel","rel",1378823488).cljs$core$IFn$_invoke$arity$1(link),"follow")){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([link,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"href","href",-793805698),cuerdas.core.replace(new cljs.core.Keyword(null,"href","href",-793805698).cljs$core$IFn$_invoke$arity$1(link),/\/follow\/?$/,"/unfollow/"),new cljs.core.Keyword(null,"rel","rel",1378823488),"unfollow"], null)], 0));
} else {
return link;
}
}),links);
}));
var org_data_key = oc.web.dispatcher.org_data_key(org_slug);
var update_count_QMARK_ = (function (){var and__4115__auto__ = inbox_data;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.count(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,posts_list_key)),cljs.core.count(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(next_db,posts_list_key)));
} else {
return and__4115__auto__;
}
})();
return cljs.core.assoc_in(cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(next_db,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(org_data_key,new cljs.core.Keyword(null,"following-inbox-count","following-inbox-count",-1849702342)),(cljs.core.truth_(update_count_QMARK_)?cljs.core.inc:cljs.core.identity)),activity_key,fixed_activity_data);
} else {
return db;
}
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("inbox","dismiss-all","inbox/dismiss-all",1284483349),(function (db,p__44046){
var vec__44047 = p__44046;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44047,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44047,(1),null);
var inbox_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$2(org_slug,"inbox");
var inbox_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,inbox_key);
var without_items = cljs.core.assoc_in(cljs.core.assoc_in(inbox_data,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897)], null),cljs.core.PersistentVector.EMPTY),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"items-to-render","items-to-render",660219762)], null),cljs.core.PersistentVector.EMPTY);
var org_data_key = oc.web.dispatcher.org_data_key(org_slug);
return cljs.core.assoc_in(cljs.core.assoc_in(db,inbox_key,without_items),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(org_data_key,new cljs.core.Keyword(null,"following-inbox-count","following-inbox-count",-1849702342)),(0));
}));
oc.web.stores.activity.latest_seen_at = (function oc$web$stores$activity$latest_seen_at(new_seen_at,old_seen_at){
if((cljs.core.compare(new_seen_at,old_seen_at) > (0))){
return new_seen_at;
} else {
return old_seen_at;
}
});
oc.web.stores.activity.following_get_finish = (function oc$web$stores$activity$following_get_finish(db,org_slug,sort_type,current_container_slug,keep_seen_at_QMARK_,following_data){
var org_data_key = oc.web.dispatcher.org_data_key(org_slug);
var org_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,org_data_key);
var change_data = oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$2(db,org_slug);
var active_users = oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org_slug,db);
var container_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,new cljs.core.Keyword(null,"following","following",-2049193617),sort_type);
var old_container_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,container_key);
var prepare_following_data = cljs.core.update.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(following_data),new cljs.core.Keyword(null,"container-slug","container-slug",365736492),new cljs.core.Keyword(null,"following","following",-2049193617)),new cljs.core.Keyword(null,"last-seen-at","last-seen-at",1929467667),(function (p1__44050_SHARP_){
if(cljs.core.truth_((function (){var and__4115__auto__ = keep_seen_at_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.map_QMARK_(old_container_data);
} else {
return and__4115__auto__;
}
})())){
return new cljs.core.Keyword(null,"last-seen-at","last-seen-at",1929467667).cljs$core$IFn$_invoke$arity$1(old_container_data);
} else {
return p1__44050_SHARP_;
}
}));
var fixed_following_data = oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$5(prepare_following_data,change_data,org_data,active_users,sort_type);
var posts_key = oc.web.dispatcher.posts_data_key(org_slug);
var old_posts = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,posts_key);
var merged_items = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([old_posts,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(fixed_following_data)], 0));
var following_badge_key = oc.web.dispatcher.following_badge_key(org_slug);
var badge_following_QMARK_ = cljs.core.some(new cljs.core.Keyword(null,"unseen","unseen",1063275592),new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897).cljs$core$IFn$_invoke$arity$1(fixed_following_data));
var ndb = db;
var ndb__$1 = cljs.core.assoc_in(ndb,container_key,cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(fixed_following_data,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159)));
var ndb__$2 = cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(ndb__$1,following_badge_key,(function (){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_container_slug),new cljs.core.Keyword(null,"following","following",-2049193617))){
return false;
} else {
return cljs.core.boolean$(badge_following_QMARK_);
}
}));
var ndb__$3 = cljs.core.assoc_in(ndb__$2,posts_key,merged_items);
var ndb__$4 = cljs.core.assoc_in(ndb__$3,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(org_data_key,new cljs.core.Keyword(null,"following-count","following-count",-1471090832)),new cljs.core.Keyword(null,"total-count","total-count",-1999441386).cljs$core$IFn$_invoke$arity$1(fixed_following_data));
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(ndb__$4,oc.web.dispatcher.user_notifications_key(org_slug),(function (p1__44051_SHARP_){
return oc.web.utils.notification.fix_notifications.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([ndb__$4,p1__44051_SHARP_], 0));
}));
});
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("following-get","finish","following-get/finish",-2078338056),(function (db,p__44052){
var vec__44053 = p__44052;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44053,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44053,(1),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44053,(2),null);
var current_container_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44053,(3),null);
var keep_seen_at_QMARK_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44053,(4),null);
var following_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44053,(5),null);
return oc.web.stores.activity.following_get_finish(db,org_slug,sort_type,current_container_slug,keep_seen_at_QMARK_,following_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("following-refresh","finish","following-refresh/finish",896471923),(function (db,p__44056){
var vec__44057 = p__44056;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44057,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44057,(1),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44057,(2),null);
var current_container_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44057,(3),null);
var keep_seen_at_QMARK_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44057,(4),null);
var following_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44057,(5),null);
return oc.web.stores.activity.following_get_finish(db,org_slug,sort_type,current_container_slug,keep_seen_at_QMARK_,following_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"following-more","following-more",2064786273),(function (db,p__44060){
var vec__44061 = p__44060;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44061,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44061,(1),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44061,(2),null);
var container_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,new cljs.core.Keyword(null,"following","following",-2049193617),sort_type);
var container_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,container_key);
var next_posts_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(container_data,new cljs.core.Keyword(null,"loading-more","loading-more",-1145525667),true);
return cljs.core.assoc_in(db,container_key,next_posts_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("following-more","finish","following-more/finish",940309697),(function (db,p__44065){
var vec__44066 = p__44065;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44066,(0),null);
var org__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44066,(1),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44066,(2),null);
var direction = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44066,(3),null);
var posts_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44066,(4),null);
if(cljs.core.truth_(posts_data)){
var org_data_key = oc.web.dispatcher.org_data_key(org__$1);
var org_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,org_data_key);
var container_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org__$1,new cljs.core.Keyword(null,"following","following",-2049193617),sort_type);
var container_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,container_key);
var posts_data_key = oc.web.dispatcher.posts_data_key(org__$1);
var old_posts = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,posts_data_key);
var prepare_following_data = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(posts_data),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897),new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897).cljs$core$IFn$_invoke$arity$1(container_data),new cljs.core.Keyword(null,"old-links","old-links",340078773),new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(container_data),new cljs.core.Keyword(null,"container-slug","container-slug",365736492),new cljs.core.Keyword(null,"following","following",-2049193617),new cljs.core.Keyword(null,"last-seen-at","last-seen-at",1929467667),new cljs.core.Keyword(null,"last-seen-at","last-seen-at",1929467667).cljs$core$IFn$_invoke$arity$1(container_data),new cljs.core.Keyword(null,"next-seen-at","next-seen-at",320928727),new cljs.core.Keyword(null,"next-seen-at","next-seen-at",320928727).cljs$core$IFn$_invoke$arity$1(container_data)], null)], 0));
var active_users = oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org__$1,db);
var fixed_following_data = oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$6(prepare_following_data,oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$2(db,org__$1),org_data,active_users,sort_type,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"direction","direction",-633359395),direction], null));
var new_items_map = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([old_posts,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(fixed_following_data)], 0));
var new_container_data = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(fixed_following_data,new cljs.core.Keyword(null,"direction","direction",-633359395),direction),new cljs.core.Keyword(null,"loading-more","loading-more",-1145525667),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159)], 0));
var ndb = db;
var ndb__$1 = cljs.core.assoc_in(ndb,container_key,new_container_data);
var ndb__$2 = cljs.core.assoc_in(ndb__$1,posts_data_key,new_items_map);
var ndb__$3 = cljs.core.assoc_in(ndb__$2,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(org_data_key,new cljs.core.Keyword(null,"following-count","following-count",-1471090832)),new cljs.core.Keyword(null,"total-count","total-count",-1999441386).cljs$core$IFn$_invoke$arity$1(fixed_following_data));
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(ndb__$3,oc.web.dispatcher.user_notifications_key(org__$1),(function (p1__44064_SHARP_){
return oc.web.utils.notification.fix_notifications.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([ndb__$3,p1__44064_SHARP_], 0));
}));
} else {
return db;
}
}));
oc.web.stores.activity.replies_get_finish = (function oc$web$stores$activity$replies_get_finish(db,org_slug,sort_type,current_container_slug,keep_seen_at_QMARK_,replies_data){
var org_data_key = oc.web.dispatcher.org_data_key(org_slug);
var org_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,org_data_key);
var change_data = oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$2(db,org_slug);
var active_users = oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org_slug,db);
var posts_data_key = oc.web.dispatcher.posts_data_key(org_slug);
var old_posts = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,posts_data_key);
var replies_container_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,new cljs.core.Keyword(null,"replies","replies",-1389888974),sort_type);
var old_container_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,replies_container_key);
var prepare_replies_data = cljs.core.update.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(replies_data),new cljs.core.Keyword(null,"container-slug","container-slug",365736492),new cljs.core.Keyword(null,"replies","replies",-1389888974)),new cljs.core.Keyword(null,"last-seen-at","last-seen-at",1929467667),(function (p1__44069_SHARP_){
if(cljs.core.truth_((function (){var and__4115__auto__ = keep_seen_at_QMARK_;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.map_QMARK_(old_container_data);
} else {
return and__4115__auto__;
}
})())){
return new cljs.core.Keyword(null,"last-seen-at","last-seen-at",1929467667).cljs$core$IFn$_invoke$arity$1(old_container_data);
} else {
return p1__44069_SHARP_;
}
}));
var fixed_replies_data = oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$5(prepare_replies_data,change_data,org_data,active_users,sort_type);
var merged_items = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([old_posts,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(fixed_replies_data)], 0));
var replies_badge_key = oc.web.dispatcher.replies_badge_key(org_slug);
var badge_replies_QMARK_ = cljs.core.some(new cljs.core.Keyword(null,"unseen-comments","unseen-comments",-793262869),new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897).cljs$core$IFn$_invoke$arity$1(fixed_replies_data));
var ndb = db;
var ndb__$1 = cljs.core.assoc_in(ndb,replies_container_key,cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(fixed_replies_data,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159)));
var ndb__$2 = cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(ndb__$1,replies_badge_key,(function (){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_container_slug),new cljs.core.Keyword(null,"replies","replies",-1389888974))){
return false;
} else {
return cljs.core.boolean$(badge_replies_QMARK_);
}
}));
var ndb__$3 = cljs.core.assoc_in(ndb__$2,posts_data_key,merged_items);
var ndb__$4 = cljs.core.assoc_in(ndb__$3,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(org_data_key,new cljs.core.Keyword(null,"replies-count","replies-count",-172405938)),new cljs.core.Keyword(null,"total-count","total-count",-1999441386).cljs$core$IFn$_invoke$arity$1(fixed_replies_data));
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(ndb__$4,oc.web.dispatcher.user_notifications_key(org_slug),(function (p1__44070_SHARP_){
return oc.web.utils.notification.fix_notifications.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([ndb__$4,p1__44070_SHARP_], 0));
}));
});
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("replies-get","finish","replies-get/finish",68864193),(function (db,p__44071){
var vec__44072 = p__44071;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44072,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44072,(1),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44072,(2),null);
var current_container_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44072,(3),null);
var keep_seen_at_QMARK_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44072,(4),null);
var replies_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44072,(5),null);
return oc.web.stores.activity.replies_get_finish(db,org_slug,sort_type,current_container_slug,keep_seen_at_QMARK_,replies_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("replies-refresh","finish","replies-refresh/finish",-2015978772),(function (db,p__44075){
var vec__44076 = p__44075;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44076,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44076,(1),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44076,(2),null);
var current_container_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44076,(3),null);
var keep_seen_at_QMARK_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44076,(4),null);
var replies_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44076,(5),null);
return oc.web.stores.activity.replies_get_finish(db,org_slug,sort_type,current_container_slug,keep_seen_at_QMARK_,replies_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"replies-more","replies-more",-636194406),(function (db,p__44079){
var vec__44080 = p__44079;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44080,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44080,(1),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44080,(2),null);
var replies_container_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,new cljs.core.Keyword(null,"replies","replies",-1389888974),sort_type);
var container_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,replies_container_key);
var next_replies_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(container_data,new cljs.core.Keyword(null,"loading-more","loading-more",-1145525667),true);
return cljs.core.assoc_in(db,replies_container_key,next_replies_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("replies-more","finish","replies-more/finish",-1231427368),(function (db,p__44084){
var vec__44085 = p__44084;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44085,(0),null);
var org__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44085,(1),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44085,(2),null);
var direction = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44085,(3),null);
var replies_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44085,(4),null);
if(cljs.core.truth_(replies_data)){
var org_data_key = oc.web.dispatcher.org_data_key(org__$1);
var org_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,org_data_key);
var replies_container_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org__$1,new cljs.core.Keyword(null,"replies","replies",-1389888974),sort_type);
var container_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,replies_container_key);
var posts_data_key = oc.web.dispatcher.posts_data_key(org__$1);
var old_posts = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,posts_data_key);
var prepare_replies_data = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(replies_data),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897),new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897).cljs$core$IFn$_invoke$arity$1(container_data),new cljs.core.Keyword(null,"old-links","old-links",340078773),new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(container_data),new cljs.core.Keyword(null,"container-slug","container-slug",365736492),new cljs.core.Keyword(null,"replies","replies",-1389888974),new cljs.core.Keyword(null,"last-seen-at","last-seen-at",1929467667),new cljs.core.Keyword(null,"last-seen-at","last-seen-at",1929467667).cljs$core$IFn$_invoke$arity$1(container_data),new cljs.core.Keyword(null,"next-seen-at","next-seen-at",320928727),new cljs.core.Keyword(null,"next-seen-at","next-seen-at",320928727).cljs$core$IFn$_invoke$arity$1(container_data)], null)], 0));
var active_users = oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org__$1,db);
var fixed_replies_data = oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$6(prepare_replies_data,oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$2(db,org__$1),org_data,active_users,sort_type,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"direction","direction",-633359395),direction], null));
var new_posts_map = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([old_posts,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(fixed_replies_data)], 0));
var new_container_data = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(fixed_replies_data,new cljs.core.Keyword(null,"direction","direction",-633359395),direction),new cljs.core.Keyword(null,"loading-more","loading-more",-1145525667),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159)], 0));
var ndb = db;
var ndb__$1 = cljs.core.assoc_in(ndb,replies_container_key,new_container_data);
var ndb__$2 = cljs.core.assoc_in(ndb__$1,posts_data_key,new_posts_map);
var ndb__$3 = cljs.core.assoc_in(ndb__$2,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(org_data_key,new cljs.core.Keyword(null,"replies-count","replies-count",-172405938)),new cljs.core.Keyword(null,"total-count","total-count",-1999441386).cljs$core$IFn$_invoke$arity$1(fixed_replies_data));
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(ndb__$3,oc.web.dispatcher.user_notifications_key(org__$1),(function (p1__44083_SHARP_){
return oc.web.utils.notification.fix_notifications.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([ndb__$3,p1__44083_SHARP_], 0));
}));
} else {
return db;
}
}));
oc.web.stores.activity.unfollowing_get_finish = (function oc$web$stores$activity$unfollowing_get_finish(db,org_slug,sort_type,unfollowing_data){
var org_data_key = oc.web.dispatcher.org_data_key(org_slug);
var org_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,org_data_key);
var change_data = oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$2(db,org_slug);
var active_users = oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org_slug,db);
var prepare_unfollowing_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(unfollowing_data),new cljs.core.Keyword(null,"container-slug","container-slug",365736492),new cljs.core.Keyword(null,"unfollowing","unfollowing",-1076165830));
var fixed_unfollowing_data = oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$5(prepare_unfollowing_data,change_data,org_data,active_users,sort_type);
var posts_key = oc.web.dispatcher.posts_data_key(org_slug);
var old_posts = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,posts_key);
var merged_items = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([old_posts,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(fixed_unfollowing_data)], 0));
var container_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,new cljs.core.Keyword(null,"unfollowing","unfollowing",-1076165830),sort_type);
var ndb = db;
var ndb__$1 = cljs.core.assoc_in(ndb,container_key,cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(fixed_unfollowing_data,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159)));
var ndb__$2 = cljs.core.assoc_in(ndb__$1,posts_key,merged_items);
var ndb__$3 = cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(ndb__$2,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(org_data_key,new cljs.core.Keyword(null,"unfollowing-count","unfollowing-count",-2050794903)),(function (p1__44088_SHARP_){
return oc.web.utils.org.disappearing_count_value(p1__44088_SHARP_,new cljs.core.Keyword(null,"total-count","total-count",-1999441386).cljs$core$IFn$_invoke$arity$1(fixed_unfollowing_data));
}));
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(ndb__$3,oc.web.dispatcher.user_notifications_key(org_slug),(function (p1__44089_SHARP_){
return oc.web.utils.notification.fix_notifications.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([ndb__$3,p1__44089_SHARP_], 0));
}));
});
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("unfollowing-get","finish","unfollowing-get/finish",546005489),(function (db,p__44090){
var vec__44091 = p__44090;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44091,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44091,(1),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44091,(2),null);
var unfollowing_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44091,(3),null);
return oc.web.stores.activity.unfollowing_get_finish(db,org_slug,sort_type,unfollowing_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("unfollowing-refresh","finish","unfollowing-refresh/finish",148683740),(function (db,p__44094){
var vec__44095 = p__44094;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44095,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44095,(1),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44095,(2),null);
var unfollowing_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44095,(3),null);
return oc.web.stores.activity.unfollowing_get_finish(db,org_slug,sort_type,unfollowing_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"unfollowing-more","unfollowing-more",-1771670350),(function (db,p__44098){
var vec__44099 = p__44098;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44099,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44099,(1),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44099,(2),null);
var container_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,new cljs.core.Keyword(null,"unfollowing","unfollowing",-1076165830),sort_type);
var container_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,container_key);
var next_posts_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(container_data,new cljs.core.Keyword(null,"loading-more","loading-more",-1145525667),true);
return cljs.core.assoc_in(db,container_key,next_posts_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("unfollowing-more","finish","unfollowing-more/finish",-870420952),(function (db,p__44104){
var vec__44105 = p__44104;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44105,(0),null);
var org__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44105,(1),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44105,(2),null);
var direction = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44105,(3),null);
var posts_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44105,(4),null);
if(cljs.core.truth_(posts_data)){
var org_data_key = oc.web.dispatcher.org_data_key(org__$1);
var org_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,org_data_key);
var container_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org__$1,new cljs.core.Keyword(null,"unfollowing","unfollowing",-1076165830),sort_type);
var container_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,container_key);
var posts_data_key = oc.web.dispatcher.posts_data_key(org__$1);
var old_posts = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,posts_data_key);
var prepare_unfollowing_data = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(posts_data),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897),new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897).cljs$core$IFn$_invoke$arity$1(container_data),new cljs.core.Keyword(null,"old-links","old-links",340078773),new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(container_data),new cljs.core.Keyword(null,"container-slug","container-slug",365736492),new cljs.core.Keyword(null,"unfollowing","unfollowing",-1076165830)], null)], 0));
var active_users = oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org__$1,db);
var fixed_unfollowing_data = oc.web.utils.activity.parse_container.cljs$core$IFn$_invoke$arity$6(prepare_unfollowing_data,oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$2(db,org__$1),org_data,active_users,sort_type,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"direction","direction",-633359395),direction], null));
var new_items_map = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([old_posts,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(fixed_unfollowing_data)], 0));
var new_container_data = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(fixed_unfollowing_data,new cljs.core.Keyword(null,"direction","direction",-633359395),direction),new cljs.core.Keyword(null,"loading-more","loading-more",-1145525667),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159)], 0));
var ndb = db;
var ndb__$1 = cljs.core.assoc_in(ndb,container_key,new_container_data);
var ndb__$2 = cljs.core.assoc_in(ndb__$1,posts_data_key,new_items_map);
var ndb__$3 = cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(ndb__$2,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(org_data_key,new cljs.core.Keyword(null,"unfollowing-count","unfollowing-count",-2050794903)),(function (p1__44102_SHARP_){
return oc.web.utils.org.disappearing_count_value(p1__44102_SHARP_,new cljs.core.Keyword(null,"total-count","total-count",-1999441386).cljs$core$IFn$_invoke$arity$1(fixed_unfollowing_data));
}));
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(ndb__$3,oc.web.dispatcher.user_notifications_key(org__$1),(function (p1__44103_SHARP_){
return oc.web.utils.notification.fix_notifications.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([ndb__$3,p1__44103_SHARP_], 0));
}));
} else {
return db;
}
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("following-badge","on","following-badge/on",-922760591),(function (db,p__44108){
var vec__44109 = p__44108;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44109,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44109,(1),null);
return cljs.core.assoc_in(db,oc.web.dispatcher.following_badge_key(org_slug),true);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("following-badge","off","following-badge/off",-98059084),(function (db,p__44112){
var vec__44113 = p__44112;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44113,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44113,(1),null);
return cljs.core.assoc_in(db,oc.web.dispatcher.following_badge_key(org_slug),false);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("replies-badge","on","replies-badge/on",1447213434),(function (db,p__44116){
var vec__44117 = p__44116;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44117,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44117,(1),null);
return cljs.core.assoc_in(db,oc.web.dispatcher.replies_badge_key(org_slug),true);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("replies-badge","off","replies-badge/off",-932384325),(function (db,p__44120){
var vec__44121 = p__44120;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44121,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44121,(1),null);
return cljs.core.assoc_in(db,oc.web.dispatcher.replies_badge_key(org_slug),false);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"container-seen","container-seen",1269698583),(function (db,p__44124){
var vec__44125 = p__44124;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44125,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44125,(1),null);
var container_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44125,(2),null);
var seen_at = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44125,(3),null);
return db;
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"container-nav-in","container-nav-in",-1013166248),(function (db,p__44128){
var vec__44129 = p__44128;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44129,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44129,(1),null);
var container_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44129,(2),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44129,(3),null);
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(container_slug,new cljs.core.Keyword(null,"replies","replies",-1389888974))){
return cljs.core.assoc_in(db,oc.web.dispatcher.replies_badge_key(org_slug),false);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(container_slug,new cljs.core.Keyword(null,"following","following",-2049193617))){
return cljs.core.assoc_in(db,oc.web.dispatcher.following_badge_key(org_slug),false);
} else {
return db;

}
}
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"container-nav-out","container-nav-out",-468973567),(function (db,p__44132){
var vec__44133 = p__44132;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44133,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44133,(1),null);
var container_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44133,(2),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44133,(3),null);
var container_key = oc.web.dispatcher.container_key.cljs$core$IFn$_invoke$arity$3(org_slug,container_slug,sort_type);
var container_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,container_key);
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$2(db,org_slug);
var change_data = oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$2(db,org_slug);
var active_users = oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org_slug,db);
return oc.web.utils.activity.update_container.cljs$core$IFn$_invoke$arity$5(cljs.core.assoc_in(db,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(container_key,new cljs.core.Keyword(null,"last-seen-at","last-seen-at",1929467667)),new cljs.core.Keyword(null,"next-seen-at","next-seen-at",320928727).cljs$core$IFn$_invoke$arity$1(container_data)),container_slug,org_data,change_data,active_users);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"update-replies-comments","update-replies-comments",-1613944614),(function (db,p__44136){
var vec__44137 = p__44136;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44137,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44137,(1),null);
var current_board_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44137,(2),null);
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$2(db,org_slug);
var change_data = oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$2(db,org_slug);
var active_users = oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org_slug,db);
return oc.web.utils.activity.update_replies_comments(db,org_data,change_data,active_users);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"update-container","update-container",130540459),(function (db,p__44142){
var vec__44143 = p__44142;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44143,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44143,(1),null);
var current_board_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44143,(2),null);
var container_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44143,(3),null);
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$2(db,org_slug);
var change_data = oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$1(db);
var active_users = oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org_slug,db);
var next_db = oc.web.utils.activity.update_container.cljs$core$IFn$_invoke$arity$5(db,container_slug,org_data,change_data,active_users);
var badge_following_QMARK_ = ((((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(current_board_slug,new cljs.core.Keyword(null,"following","following",-2049193617))) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(container_slug,new cljs.core.Keyword(null,"following","following",-2049193617)))))?cljs.core.some(new cljs.core.Keyword(null,"unseen","unseen",1063275592),new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.following_data.cljs$core$IFn$_invoke$arity$2(org_slug,next_db))):null);
var badge_replies_QMARK_ = ((((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(current_board_slug),new cljs.core.Keyword(null,"replies","replies",-1389888974))) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(container_slug,new cljs.core.Keyword(null,"replies","replies",-1389888974)))))?cljs.core.some(new cljs.core.Keyword(null,"unseen-comments","unseen-comments",-793262869),new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.replies_data.cljs$core$IFn$_invoke$arity$2(org_slug,next_db))):null);
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(next_db,oc.web.dispatcher.following_badge_key(org_slug),(function (p1__44140_SHARP_){
return cljs.core.boolean$((function (){var or__4126__auto__ = badge_following_QMARK_;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return p1__44140_SHARP_;
}
})());
})),oc.web.dispatcher.replies_badge_key(org_slug),(function (p1__44141_SHARP_){
return cljs.core.boolean$((function (){var or__4126__auto__ = badge_replies_QMARK_;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return p1__44141_SHARP_;
}
})());
}));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"update-containers","update-containers",-96113256),(function (db,p__44146){
var vec__44147 = p__44146;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44147,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44147,(1),null);
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$2(db,org_slug);
var change_data = oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$1(db);
var active_users = oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org_slug,db);
return oc.web.utils.activity.update_containers(db,org_data,change_data,active_users);
}));

//# sourceMappingURL=oc.web.stores.activity.js.map
